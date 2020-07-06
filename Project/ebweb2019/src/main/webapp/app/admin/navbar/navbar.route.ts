import { Route } from '@angular/router';

import { NavbarAdminComponent } from './navbar.component';

export const navbarAdminRoute: Route = {
    path: '',
    component: NavbarAdminComponent,
    outlet: 'admin-navbar'
};
