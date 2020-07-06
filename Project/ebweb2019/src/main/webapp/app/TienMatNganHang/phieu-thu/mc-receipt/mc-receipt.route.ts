import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MCReceipt } from 'app/shared/model/mc-receipt.model';
import { MCReceiptService } from './mc-receipt.service';
import { MCReceiptComponent } from './mc-receipt.component';
import { MCReceiptDetailComponent } from './mc-receipt-detail.component';
import { MCReceiptUpdateComponent } from './mc-receipt-update.component';
import { MCReceiptDeletePopupComponent } from './mc-receipt-delete-dialog.component';
import { IMCReceipt } from 'app/shared/model/mc-receipt.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class MCReceiptResolve implements Resolve<IMCReceipt> {
    constructor(private service: MCReceiptService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((mCReceipt: HttpResponse<MCReceipt>) => mCReceipt.body));
        }
        return of(new MCReceipt());
    }
}

export const mCReceiptRoute: Routes = [
    {
        path: '',
        component: MCReceiptComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuThu_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.mCReceipt.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'hasSearch/:isSearch',
        component: MCReceiptComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuThu_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.mCReceipt.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: MCReceiptDetailComponent,
        resolve: {
            mCReceipt: MCReceiptResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuThu_Xem],
            pageTitle: 'ebwebApp.mCReceipt.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: MCReceiptUpdateComponent,
        resolve: {
            mCReceipt: MCReceiptResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuThu_Xem],
            pageTitle: 'ebwebApp.mCReceipt.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: MCReceiptUpdateComponent,
        resolve: {
            mCReceipt: MCReceiptResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuThu_Xem],
            pageTitle: 'ebwebApp.mCReceipt.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: MCReceiptUpdateComponent,
        resolve: {
            mCReceipt: MCReceiptResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuThu_Xem],
            pageTitle: 'ebwebApp.mCReceipt.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const mCReceiptPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MCReceiptDeletePopupComponent,
        resolve: {
            mCReceipt: MCReceiptResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mCReceipt.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
