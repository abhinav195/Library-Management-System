import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { IssuesService, Issue } from '../../../core/issues/issues.service';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConfirmDialogComponent } from '../../../shared/confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  standalone: true,
  selector: 'app-my-borrows',
  imports: [CommonModule, MatTableModule, MatButtonModule, DatePipe],
  templateUrl: './my-borrows.component.html',
  styleUrls: ['./my-borrows.component.css']
})
export class MyBorrowsComponent implements OnInit {
  private issues = inject(IssuesService);
  private snack = inject(MatSnackBar);
  private dialog = inject(MatDialog);

  displayedColumns = ['title', 'borrowedAt', 'dueAt', 'actions'];
  data: Issue[] = [];

  ngOnInit() { this.load(); }

  load() {
    this.issues.myActive().subscribe(res => this.data = res);
  }

  return(bookId: number) {
    const ref = this.dialog.open(ConfirmDialogComponent, {
      data: { title: 'Return book', message: 'Return this book now?', okText: 'Return' }
    });
    ref.afterClosed().subscribe(ok => {
      if (!ok) return;
      this.issues.return(bookId).subscribe({
        next: () => { this.snack.open('Returned', 'Close', { duration: 1200 }); this.load(); },
        error: err => this.snack.open(err?.error?.message || 'Cannot return', 'Close', { duration: 1800 })
      });
    });
  }
}
