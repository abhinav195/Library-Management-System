import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IssuesService, Issue } from '../../../core/issues/issues.service';
import { MatTableModule } from '@angular/material/table';

@Component({
  standalone: true,
  selector: 'app-users-admin',
  imports: [CommonModule, MatTableModule],
  templateUrl: './users-admin.component.html',
  styleUrls: ['./users-admin.component.css']
})
export class UsersAdminComponent implements OnInit {
  private issues = inject(IssuesService);
  data: Issue[] = [];
  displayed = ['username', 'book', 'borrowedAt', 'dueAt'];

  ngOnInit() {
    this.issues.allActive().subscribe(r => this.data = r);
  }
}
