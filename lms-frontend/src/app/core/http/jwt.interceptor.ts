import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const token = auth.token();

  const cloned = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(cloned).pipe(
    // Optional: handle 401/403 here; for brevity we’ll just redirect on 401
    // catchError(err => { if (err.status === 401 || err.status === 403) { auth.clear(); router.navigateByUrl('/login'); } return throwError(() => err); })
  );
};
