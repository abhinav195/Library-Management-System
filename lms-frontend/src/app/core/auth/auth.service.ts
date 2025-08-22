import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export type LoginReq = { username: string; password: string };
export type SignupReq = { username: string; password: string; email?: string; fullName?: string };
export type TokenResponse = { token: string } | string;

type JwtPayload = { sub: string; roles?: string[]; exp?: number };

@Injectable({ providedIn: 'root' })
export class AuthService {
  private base = environment.apiBaseUrl;
  private tokenKey = 'lms_token';

  private _token = signal<string | null>(this.readToken());
  token = computed(() => this._token());
  isLoggedIn = computed(() => !!this._token());
  roles = computed<string[]>(() => this.decode(this._token())?.roles ?? []);
  username = computed<string | null>(() => this.decode(this._token())?.sub ?? null);
  isAdmin = computed(() => this.roles().includes('ADMIN'));

  constructor(private http: HttpClient) {}

  private readToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }
  private storeToken(t: string | null) {
    if (t) localStorage.setItem(this.tokenKey, t); else localStorage.removeItem(this.tokenKey);
    this._token.set(t);
  }

  private decode(token: string | null): JwtPayload | null {
    try {
      if (!token) return null;
      const base64 = token.split('.')[1];
      return JSON.parse(atob(base64));
    } catch { return null; }
  }

  login(req: LoginReq) {
    return this.http.post<TokenResponse>(`${this.base}/api/auth/login`, req).pipe(
      // accept raw string or { token }
      // map isn’t necessary here; we’ll normalize in subscribe
    );
  }

  signup(req: SignupReq) {
    return this.http.post(`${this.base}/api/auth/signup`, req);
  }

  finalizeLogin(resp: TokenResponse) {
    const token = typeof resp === 'string' ? resp : resp.token;
    this.storeToken(token);
  }

  logout() {
    // backend will blacklist current token
    return this.http.post(`${this.base}/api/auth/logout`, {});
  }

  clear() {
    this.storeToken(null);
  }
}
