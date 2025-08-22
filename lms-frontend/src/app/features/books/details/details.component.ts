import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { BooksService, Book } from '../../../core/books/books.service';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { IssuesService } from '../../../core/issues/issues.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  standalone: true,
  selector: 'app-book-details',
  imports: [CommonModule, MatCardModule, MatButtonModule],
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit {
  route = inject(ActivatedRoute);
  books = inject(BooksService);
  issues = inject(IssuesService);
  snack = inject(MatSnackBar);
  auth = inject(AuthService);

  book?: Book;

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.books.getById(id).subscribe(b => this.book = b);
  }

  borrow() {
    if (!this.book) return;
    if (!this.auth.isLoggedIn()) { this.snack.open('Please login first', 'Close', { duration: 1600 }); return; }
    this.issues.borrow(this.book.id).subscribe({
      next: () => this.snack.open('Borrowed!', 'Close', { duration: 1200 }),
      error: err => this.snack.open(err?.error?.message || 'Cannot borrow', 'Close', { duration: 2000 })
    });
  }
}
