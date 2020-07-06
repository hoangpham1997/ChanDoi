import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Injectable } from '@angular/core';
import { UserRouteAccessService } from 'app/core';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { SoDuDauKyInsertUpdateComponent, SoDuDauKyResultComponent } from './index';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class SoDuDauKyResolve implements Resolve<any> {
    constructor() {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        return route.params['id'] ? route.params['id'] : null;
    }
}

export const soDuDauKyRoute: Routes = [
    {
        path: '',
        component: SoDuDauKyResultComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.SoDuDauKy_Xem],
            pageTitle: 'ebwebApp.tienIch.soDuDauKy.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'update',
        component: SoDuDauKyInsertUpdateComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.SoDuDauKy_Xem],
            pageTitle: 'ebwebApp.tienIch.soDuDauKy.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];
