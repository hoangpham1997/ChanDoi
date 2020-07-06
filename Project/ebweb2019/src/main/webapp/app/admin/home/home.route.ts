import { Route } from '@angular/router';
import { HomeAdminComponent } from 'app/admin/home/home.component';

export const HOME_ADMIN_ROUTE: Route = {
    path: 'home',
    component: HomeAdminComponent,
    data: {
        authorities: [],
        pageTitle: 'home.title'
    }
};
