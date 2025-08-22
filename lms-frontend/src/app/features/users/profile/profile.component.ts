import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatChipsModule } from '@angular/material/chips';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { UsersService, Me } from '../../../core/users/users.service';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  standalone: true,
  selector: 'app-profile',
  imports: [CommonModule, MatCardModule, MatListModule, MatChipsModule, MatButtonModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  private users = inject(UsersService);
  private router = inject(Router);
  auth = inject(AuthService);

  me = signal<Me | null>(null);
  penalty = signal<number | null>(null);

  ngOnInit() {
    this.users.me().subscribe(v => this.me.set(v));
    // If your /me already includes penalties, you can omit this call and read v.penalties instead
    this.users.getPenalty().subscribe(p => this.penalty.set(p));
  }

  goChangePassword() { this.router.navigateByUrl('/change-password'); }
}
