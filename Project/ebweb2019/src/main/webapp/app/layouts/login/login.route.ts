import { Routes } from '@angular/router';

import { LoginComponent } from 'app/layouts';

export const loginRoute: Routes = [
    {
        path: 'login',
        component: LoginComponent,
        data: {
            authorities: [],
            pageTitle: 'login.title'
        }
    }
];
