import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Injectable } from '@angular/core';
import { MuaDichVuComponent } from './mua-dich-vu.component';
import { UserRouteAccessService } from 'app/core';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ONBROADCASTEVENT } from 'app/muahang/mua-dich-vu/mua-dich-vu-event-name.constant';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class MuaDichVuResolve implements Resolve<any> {
    constructor() {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        return route.params['id'] ? route.params['id'] : null;
    }
}

export const muaDichVuRoute: Routes = [
    {
        path: '',
        component: MuaDichVuComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaDichVu_Xem],
            pageTitle: 'ebwebApp.muaHang.muaDichVu.title.name'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'new',
        component: MuaDichVuComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaDichVu_Them],
            viewType: 1,
            pageTitle: 'ebwebApp.muaHang.muaDichVu.title.name'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/pp-service',
        component: MuaDichVuComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams,
            ppServiceId: MuaDichVuResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaDichVu_Xem],
            viewType: 1,
            pageTitle: 'ebwebApp.muaHang.muaDichVu.title.name',
            actionClose: ONBROADCASTEVENT.fromPPService
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: MuaDichVuComponent,
        resolve: {
            ppServiceId: MuaDichVuResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaDichVu_Xem],
            pageTitle: 'ebwebApp.muaHang.muaDichVu.title.name',
            viewType: 1,
            actionClose: ONBROADCASTEVENT.selectViewVoucher
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-mc-payment/:mcpaymentID',
        component: MuaDichVuComponent,
        resolve: {
            ppServiceId: MuaDichVuResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaDichVu_Xem],
            pageTitle: 'ebwebApp.muaHang.muaDichVu.title.name',
            viewType: 1,
            actionClose: ONBROADCASTEVENT.fromMCPayment
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-mb-credit-card/:mBCreditCardID',
        component: MuaDichVuComponent,
        resolve: {
            ppServiceId: MuaDichVuResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaDichVu_Xem],
            pageTitle: 'ebwebApp.muaHang.muaDichVu.title.name',
            viewType: 1,
            actionClose: ONBROADCASTEVENT.fromMBCreditCard
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-mb-teller-paper/:mBTellerPaperID',
        component: MuaDichVuComponent,
        resolve: {
            ppServiceId: MuaDichVuResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaDichVu_Xem],
            pageTitle: 'ebwebApp.muaHang.muaDichVu.title.name',
            viewType: 1,
            actionClose: ONBROADCASTEVENT.fromMBTellerPaper
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-mb-credit-card/:mbCreditCardID',
        component: MuaDichVuComponent,
        resolve: {
            ppServiceId: MuaDichVuResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaDichVu_Xem],
            pageTitle: 'ebwebApp.muaHang.muaDichVu.title.name',
            viewType: 1,
            actionClose: ONBROADCASTEVENT.fromMBCreditCard
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];
