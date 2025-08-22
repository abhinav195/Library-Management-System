import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BooksService, Book } from '../../../core/books/books.service';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { IssuesService } from '../../../core/issues/issues.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  standalone: true,
  selector: 'app-books-list',
  imports: [CommonModule, MatTableModule, MatButtonModule, RouterLink],
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {
  private booksSvc = inject(BooksService);
  private issuesSvc = inject(IssuesService);
  private snack = inject(MatSnackBar);
  auth = inject(AuthService);

  displayedColumns = ['title', 'author', 'copies', 'actions'];
  data: Book[] = [];

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.booksSvc.getAll().subscribe(res => this.data = res);
  }

  borrow(b: Book) {
    if (!this.auth.isLoggedIn()) { this.snack.open('Please login first', 'Close', { duration: 1600 }); return; }
    this.issuesSvc.borrow(b.id).subscribe({
      next: () => { this.snack.open('Borrowed!', 'Close', { duration: 1200 }); this.refresh(); },
      error: err => this.snack.open(err?.error?.message || 'Cannot borrow', 'Close', { duration: 2000 })
    });
  }
}
