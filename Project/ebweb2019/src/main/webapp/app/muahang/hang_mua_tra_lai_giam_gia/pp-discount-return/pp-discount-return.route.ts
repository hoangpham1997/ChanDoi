import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PPDiscountReturn } from 'app/shared/model/pp-discount-return.model';
import { PPDiscountReturnService } from './pp-discount-return.service';
import { PPDiscountReturnComponent } from './pp-discount-return.component';
import { PPDiscountReturnDetailComponent } from './pp-discount-return-detail.component';
import { PPDiscountReturnUpdateComponent } from './pp-discount-return-update.component';
import { PPDiscountReturnDeletePopupComponent } from './pp-discount-return-delete-dialog.component';
import { IPPDiscountReturn } from 'app/shared/model/pp-discount-return.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class PPDiscountReturnResolve implements Resolve<IPPDiscountReturn> {
    constructor(private service: PPDiscountReturnService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((pPDiscountReturn: HttpResponse<PPDiscountReturn>) => pPDiscountReturn.body));
        }
        return of(new PPDiscountReturn());
    }
}

export const pPDiscountReturnRoute: Routes = [
    {
        path: 'tra-lai',
        component: PPDiscountReturnComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaTraLai_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason',
            statusPurchase: false
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'giam-gia',
        component: PPDiscountReturnComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaGiamGia_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReasonTitle',
            statusPurchase: true
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tra-lai/:id/view',
        component: PPDiscountReturnUpdateComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaTraLai_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason',
            statusPurchase: false
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tra-lai/:id/from-ref',
        component: PPDiscountReturnUpdateComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaTraLai_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason',
            statusPurchase: false
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tra-lai/:id/edit',
        component: PPDiscountReturnUpdateComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaTraLai_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason',
            statusPurchase: false
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tra-lai/:id/edit-rs-inward-outward',
        component: PPDiscountReturnUpdateComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaTraLai_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'giam-gia/:id/view',
        component: PPDiscountReturnUpdateComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaGiamGia_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReasonTitle',
            statusPurchase: true
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'giam-gia/:id/from-ref',
        component: PPDiscountReturnUpdateComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaGiamGia_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReasonTitle',
            statusPurchase: true
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'giam-gia/:id/edit',
        component: PPDiscountReturnUpdateComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaGiamGia_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReasonTitle',
            statusPurchase: true
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tra-lai/new',
        component: PPDiscountReturnUpdateComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaTraLai_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason',
            statusPurchase: false
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'giam-gia/new',
        component: PPDiscountReturnUpdateComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaGiamGia_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReasonTitle',
            statusPurchase: true
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'tra-lai/:id/edit/:rowNum',
        component: PPDiscountReturnUpdateComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaTraLai_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason',
            statusPurchase: false
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'giam-gia/:id/edit/:rowNum',
        component: PPDiscountReturnUpdateComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaGiamGia_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReasonTitle',
            statusPurchase: true
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'tra-lai/:id/new/:rowNum',
        component: PPDiscountReturnComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaTraLai_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason',
            statusPurchase: false
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'giam-gia/:id/new/:rowNum',
        component: PPDiscountReturnComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaGiamGia_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReasonTitle',
            statusPurchase: true
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tra-lai/:id/edit/from-einvoice',
        component: PPDiscountReturnUpdateComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaTraLai_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason',
            statusPurchase: false
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const pPDiscountReturnPopupRoute: Routes = [
    {
        path: 'tra-lai/:id/delete',
        component: PPDiscountReturnDeletePopupComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaTraLai_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason',
            statusPurchase: false
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'giam-gia/:id/delete',
        component: PPDiscountReturnDeletePopupComponent,
        resolve: {
            pPDiscountReturn: PPDiscountReturnResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.HangMuaGiamGia_Xem],
            pageTitle: 'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReasonTitle',
            statusPurchase: true
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
