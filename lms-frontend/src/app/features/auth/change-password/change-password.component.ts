import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, ValidatorFn, AbstractControl } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { UsersService } from '../../../core/users/users.service';

function matchValidator(a: string, b: string): ValidatorFn {
  return (group: AbstractControl) => {
    const va = group.get(a)?.value;
    const vb = group.get(b)?.value;
    return va === vb ? null : { mismatch: true };
  };
}

@Component({
  standalone: true,
  selector: 'app-change-password',
  imports: [CommonModule, ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent {
  private fb = inject(FormBuilder);
  private users = inject(UsersService);
  private snack = inject(MatSnackBar);
  private router = inject(Router);

  form = this.fb.nonNullable.group({
    oldPassword: ['', Validators.required],
    newPassword: ['', [Validators.required, Validators.minLength(6)]],
    confirm:     ['', Validators.required]
  }, { validators: matchValidator('newPassword', 'confirm') });

  submit() {
    if (this.form.invalid) return;
    const { oldPassword, newPassword } = this.form.getRawValue();
    this.users.changePassword(oldPassword, newPassword).subscribe({
      next: () => {
        this.snack.open('Password changed', 'Close', { duration: 1600 });
        this.router.navigateByUrl('/profile');
      },
      error: err => this.snack.open(err?.error?.message || 'Change failed', 'Close', { duration: 2200 })
    });
  }
}
