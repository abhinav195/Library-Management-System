import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface Book {
  id: number;
  title: string;
  author: string;
  copies: number;
  description?: string;
}

@Injectable({ providedIn: 'root' })
export class BooksService {
  private base = `${environment.apiBaseUrl}/api/books`;

  constructor(private http: HttpClient) {}

  getAll()      { return this.http.get<Book[]>(this.base); }
  getById(id: number) { return this.http.get<Book>(`${this.base}/${id}`); }

  // Admin
  add(body: Partial<Book>)          { return this.http.post<Book>(this.base, body); }
  update(id: number, body: Partial<Book>) { return this.http.put<Book>(`${this.base}/${id}`, body); }
  remove(id: number)                { return this.http.delete<void>(`${this.base}/${id}`); }
}
