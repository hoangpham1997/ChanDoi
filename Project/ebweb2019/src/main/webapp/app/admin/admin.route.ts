import { Routes } from '@angular/router';

import {
    auditsRoute,
    configurationRoute,
    docsRoute,
    ebPackagePopupRoute,
    ebPackageRoute,
    healthRoute,
    logsRoute,
    metricsRoute,
    userMgmtAdminRoute
} from './';

import { UserRouteAccessService } from 'app/core';
import { HOME_ADMIN_ROUTE } from 'app/admin/home/home.route';
import { sidebarAdminRoute } from 'app/admin/sidebar/sidebar.route';
import { navbarAdminRoute } from 'app/admin/navbar/navbar.route';
import { organizationUnitAdminRoute, organizationUnitPopupAdminRoute } from 'app/admin/organization-unit/organization-unit.route';

const ADMIN_ROUTES = [
    auditsRoute,
    configurationRoute,
    docsRoute,
    healthRoute,
    logsRoute,
    ...userMgmtAdminRoute,
    ...organizationUnitAdminRoute,
    ...organizationUnitPopupAdminRoute,
    ...ebPackageRoute,
    ...ebPackagePopupRoute,
    metricsRoute,
    HOME_ADMIN_ROUTE,
    sidebarAdminRoute,
    navbarAdminRoute
];

export const adminState: Routes = [
    {
        path: '',
        data: {
            authorities: ['ROLE_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        children: ADMIN_ROUTES
    }
];
