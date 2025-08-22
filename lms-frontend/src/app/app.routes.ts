import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth.guard';
import { adminGuard } from './core/auth/admin.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'books' },

  // Auth
  { path: 'login',  loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent) },
  { path: 'signup', loadComponent: () => import('./features/auth/signup/signup.component').then(m => m.SignupComponent) },

  // Books (public list & details; borrow needs auth)
  { path: 'books',      loadComponent: () => import('./features/books/list/list.component').then(m => m.ListComponent) },
  { path: 'books/:id',  loadComponent: () => import('./features/books/details/details.component').then(m => m.DetailsComponent) },

  // My borrows (requires login)
  { path: 'my/borrows', canActivate: [authGuard],
    loadComponent: () => import('./features/issues/my-borrows/my-borrows.component').then(m => m.MyBorrowsComponent)
  },

  // Admin area
  { path: 'admin/books', canActivate: [adminGuard],
    loadComponent: () => import('./features/admin/books-admin/books-admin.component').then(m => m.BooksAdminComponent)
  },
  { path: 'admin/users', canActivate: [adminGuard],
    loadComponent: () => import('./features/admin/users-admin/users-admin.component').then(m => m.UsersAdminComponent)
  },

  { path: 'profile', canActivate: [authGuard],
    loadComponent: () => import('./features/users/profile/profile.component').then(m => m.ProfileComponent)
  },
  { path: 'change-password', canActivate: [authGuard],
    loadComponent: () => import('./features/auth/change-password/change-password.component').then(m => m.ChangePasswordComponent)
  },

  { path: '**', redirectTo: 'books' }
];
