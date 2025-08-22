import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const snack = inject(MatSnackBar);

  if (auth.isLoggedIn()) return true;

  snack.open('Please sign in to continue', 'Close', { duration: 2000 });
  router.navigateByUrl('/login');
  return false;
};
