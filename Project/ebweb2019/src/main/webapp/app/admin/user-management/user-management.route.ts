import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';

import { Principal, User, UserService } from 'app/core';
import { UserMgmtDetailAdminComponent } from './user-management-detail.component';
import { UserMgmtUpdateAdminComponent } from './user-management-update.component';
import { UserMgmtAdminComponent } from 'app/admin';

@Injectable({ providedIn: 'root' })
export class UserAdminResolve implements CanActivate {
    constructor(private principal: Principal) {}

    canActivate() {
        return this.principal.identity().then(account => this.principal.hasAnyAuthority(['ROLE_ADMIN']));
    }
}

@Injectable({ providedIn: 'root' })
export class UserMgmtAdminResolve implements Resolve<any> {
    constructor(private service: UserService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['login'] ? route.params['login'] : null;
        if (id) {
            return this.service.find(id);
        }
        return new User();
    }
}

export const userMgmtAdminRoute: Routes = [
    {
        path: 'user-management',
        component: UserMgmtAdminComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            pageTitle: 'userManagement.home.title',
            defaultSort: 'id,asc'
        }
    },
    {
        path: 'user-management/:login/view',
        component: UserMgmtDetailAdminComponent,
        resolve: {
            user: UserMgmtAdminResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'userManagement.home.title'
        }
    },
    {
        path: 'user-management/new',
        component: UserMgmtUpdateAdminComponent,
        resolve: {
            user: UserMgmtAdminResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'userManagement.home.title'
        }
    },
    {
        path: 'user-management/:login/edit',
        component: UserMgmtUpdateAdminComponent,
        resolve: {
            user: UserMgmtAdminResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'userManagement.home.title'
        }
    }
];
