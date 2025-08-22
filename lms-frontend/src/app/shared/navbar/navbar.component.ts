import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule }  from '@angular/material/button';
import { MatMenuModule }    from '@angular/material/menu';
import { MatIconModule }    from '@angular/material/icon';
import { MatChipsModule }   from '@angular/material/chips';
import { NgIf } from '@angular/common';
import { AuthService } from '../../core/auth/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { UsersService } from '../../core/users/users.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [MatToolbarModule, MatButtonModule, MatMenuModule, MatIconModule, MatChipsModule, RouterLink, NgIf],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  auth = inject(AuthService);
  snack = inject(MatSnackBar);
  dialog = inject(MatDialog);
  router = inject(Router);
  users = inject(UsersService);

  penalty = signal<number | null>(null);

  constructor() {
    if (this.auth.isLoggedIn()) {
      this.users.getPenalty().subscribe(p => this.penalty.set(p));
    }
  }

  doLogout() {
    const ref = this.dialog.open(ConfirmDialogComponent, {
      data: { title: 'Sign out', message: 'Do you want to sign out now?', okText: 'Sign out' }
    });
    ref.afterClosed().subscribe(ok => {
      if (!ok) return;
      this.auth.logout().subscribe({
        next: () => {
          this.auth.clear();
          this.snack.open('Signed out', 'Close', { duration: 2200 });
          this.router.navigateByUrl('/login');
        },
        error: () => {
          this.auth.clear();
          this.router.navigateByUrl('/login');
        }
      });
    });
  }
}
