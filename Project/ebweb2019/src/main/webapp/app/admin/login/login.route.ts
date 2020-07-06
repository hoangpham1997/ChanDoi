import { Routes } from '@angular/router';
import { AdminLoginComponent } from 'app/admin/login/login.component';

export const loginAdminRoute: Routes = [
    {
        path: 'admin/login',
        component: AdminLoginComponent,
        data: {
            authorities: [],
            pageTitle: 'login.title'
        }
    }
];
