import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface Issue {
  id: number;
  bookId: number;
  bookTitle?: string;
  username?: string;
  borrowedAt: string;   // ISO string
  returnedAt?: string;  // ISO string | null
  dueAt?: string;       // optional (30d policy)
}

@Injectable({ providedIn: 'root' })
export class IssuesService {
  private base = `${environment.apiBaseUrl}/api/issues`;

  constructor(private http: HttpClient) {}

  borrow(bookId: number)     { return this.http.post(`${this.base}/borrow`, { bookId }); }
  return(bookId: number)     { return this.http.post(`${this.base}/return`, { bookId }); }
  myActive()                 { return this.http.get<Issue[]>(`${this.base}/my`); }

  // admin
  allActive()                { return this.http.get<Issue[]>(`${this.base}/admin/active`); }
}
