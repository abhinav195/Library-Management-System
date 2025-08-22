import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

export const adminGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const snack = inject(MatSnackBar);

  if (auth.isLoggedIn() && auth.isAdmin()) return true;

  snack.open('Admin access required', 'Close', { duration: 2000 });
  router.navigateByUrl('/');
  return false;
};
