import { Route } from '@angular/router';

import { SidebarAdminComponent } from './sidebar.component';

export const sidebarAdminRoute: Route = {
    path: '',
    component: SidebarAdminComponent,
    outlet: 'admin-sidebar'
};
