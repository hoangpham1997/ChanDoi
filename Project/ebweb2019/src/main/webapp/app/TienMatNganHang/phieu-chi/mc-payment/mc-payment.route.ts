import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MCPayment } from 'app/shared/model/mc-payment.model';
import { MCPaymentService } from './mc-payment.service';
import { MCPaymentComponent } from './mc-payment.component';
import { MCPaymentDetailComponent } from './mc-payment-detail.component';
import { MCPaymentUpdateComponent } from './mc-payment-update.component';
import { MCPaymentDeletePopupComponent } from './mc-payment-delete-dialog.component';
import { IMCPayment } from 'app/shared/model/mc-payment.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class MCPaymentResolve implements Resolve<IMCPayment> {
    constructor(private service: MCPaymentService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((mCPayment: HttpResponse<MCPayment>) => mCPayment.body));
        }
        return of(new MCPayment());
    }
}

export const mCPaymentRoute: Routes = [
    {
        path: '',
        component: MCPaymentComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuChi_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.mCPayment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'hasSearch/:isSearch',
        component: MCPaymentComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuChi_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.mCPayment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: MCPaymentDetailComponent,
        resolve: {
            mCPayment: MCPaymentResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuChi_Xem],
            pageTitle: 'ebwebApp.mCPayment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: MCPaymentUpdateComponent,
        resolve: {
            mCPayment: MCPaymentResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuChi_Xem],
            pageTitle: 'ebwebApp.mCPayment.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: MCPaymentUpdateComponent,
        resolve: {
            mCPayment: MCPaymentResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuChi_Xem],
            pageTitle: 'ebwebApp.mCPayment.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: MCPaymentUpdateComponent,
        resolve: {
            mCPayment: MCPaymentResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuChi_Xem],
            pageTitle: 'ebwebApp.mCPayment.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const mCPaymentPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MCPaymentDeletePopupComponent,
        resolve: {
            mCPayment: MCPaymentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mCPayment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
