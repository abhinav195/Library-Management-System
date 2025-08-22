import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BooksService, Book } from '../../../core/books/books.service';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  standalone: true,
  selector: 'app-books-admin',
  imports: [CommonModule, MatTableModule, MatButtonModule, MatDialogModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule],
  templateUrl: './books-admin.component.html',
  styleUrls: ['./books-admin.component.css']
})
export class BooksAdminComponent implements OnInit {
  private books = inject(BooksService);
  private snack = inject(MatSnackBar);
  private fb = inject(FormBuilder);
  data: Book[] = [];
  displayedColumns = ['id','title','author','copies','actions'];

  form = this.fb.nonNullable.group({
    title: ['', Validators.required],
    author: ['', Validators.required],
    copies: [1, [Validators.required]],
    description: ['']
  });

  editingId: number | null = null;

  ngOnInit() { this.load(); }
  load() { this.books.getAll().subscribe(r => this.data = r); }

  edit(row: Book) {
    this.editingId = row.id;
    this.form.patchValue(row);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  reset() { this.editingId = null; this.form.reset({ title:'', author:'', copies:1, description:'' }); }

  save() {
    if (this.form.invalid) return;
    const value = this.form.getRawValue();
    const call = this.editingId ? this.books.update(this.editingId, value) : this.books.add(value);
    call.subscribe({
      next: () => { this.snack.open('Saved', 'Close', { duration: 1200 }); this.reset(); this.load(); },
      error: err => this.snack.open(err?.error?.message || 'Save failed', 'Close', { duration: 2000 })
    });
  }

  remove(id: number) {
    this.books.remove(id).subscribe({
      next: () => { this.snack.open('Deleted', 'Close', { duration: 1200 }); this.load(); },
      error: err => this.snack.open(err?.error?.message || 'Delete failed', 'Close', { duration: 2000 })
    });
  }
}
