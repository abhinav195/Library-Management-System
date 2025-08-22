import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface Me {
  username: string;
  fullName?: string;
  email?: string;
  roles: string[];
  penalties?: number;
}

@Injectable({ providedIn: 'root' })
export class UsersService {
  private base = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  me() {
    // adjust if your endpoint differs
    return this.http.get<Me>(`${this.base}/api/users/me`);
  }

  changePassword(oldPassword: string, newPassword: string) {
    return this.http.post(`${this.base}/api/users/me/change-password`, { oldPassword, newPassword });
  }

  // penalties (if you exposed them)
  getPenalty()  { return this.http.get<number>(`${this.base}/api/users/me/penalty`); }
}
