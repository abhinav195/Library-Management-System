import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  standalone: true,
  selector: 'app-signup',
  imports: [CommonModule, ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  fb = inject(FormBuilder);
  auth = inject(AuthService);
  snack = inject(MatSnackBar);
  router = inject(Router);

  form = this.fb.nonNullable.group({
    username: ['', Validators.required],
    email:    ['', [Validators.email]],
    fullName: [''],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  submit() {
    if (this.form.invalid) return;
    this.auth.signup(this.form.getRawValue()).subscribe({
      next: () => {
        this.snack.open('Account created. Please sign in.', 'Close', { duration: 1800 });
        this.router.navigateByUrl('/login');
      },
      error: err => this.snack.open(err?.error?.message || 'Sign up failed', 'Close', { duration: 2500 })
    });
  }
}
