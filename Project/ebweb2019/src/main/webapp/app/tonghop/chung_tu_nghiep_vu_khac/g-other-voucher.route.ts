import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { GOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { GOtherVoucherService } from './g-other-voucher.service';
import { GOtherVoucherComponent } from './g-other-voucher.component';
import { GOtherVoucherDetailComponent } from './g-other-voucher-detail.component';
import { GOtherVoucherUpdateComponent } from './g-other-voucher-update.component';
import { GOtherVoucherDeletePopupComponent } from './g-other-voucher-delete-dialog.component';
import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { MBDepositResolve, MBDepositUpdateComponent } from 'app/TienMatNganHang/BaoCo/mb-deposit';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class GOtherVoucherResolve implements Resolve<IGOtherVoucher> {
    constructor(private service: GOtherVoucherService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((gOtherVoucher: HttpResponse<GOtherVoucher>) => gOtherVoucher.body));
        }
        return of(new GOtherVoucher());
    }
}

export const gOtherVoucherRoute: Routes = [
    {
        path: '',
        component: GOtherVoucherComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChungTuNghiepVuKhac_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.gOtherVoucher.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: GOtherVoucherDetailComponent,
        resolve: {
            gOtherVoucher: GOtherVoucherResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChungTuNghiepVuKhac_Xem],
            pageTitle: 'ebwebApp.gOtherVoucher.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: GOtherVoucherUpdateComponent,
        resolve: {
            gOtherVoucher: GOtherVoucherResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChungTuNghiepVuKhac_Them],
            pageTitle: 'ebwebApp.gOtherVoucher.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'hasSearch/:isSearch',
        component: GOtherVoucherComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChungTuNghiepVuKhac_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.gOtherVoucher.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit/:rowNum',
        component: GOtherVoucherUpdateComponent,
        resolve: {
            gOtherVoucher: GOtherVoucherResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChungTuNghiepVuKhac_Sua],
            pageTitle: 'ebwebApp.gOtherVoucher.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: GOtherVoucherUpdateComponent,
        resolve: {
            gOtherVoucher: GOtherVoucherResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChungTuNghiepVuKhac_Sua],
            pageTitle: 'ebwebApp.gOtherVoucher.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: GOtherVoucherUpdateComponent,
        resolve: {
            mCPayment: GOtherVoucherResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChungTuNghiepVuKhac_Sua],
            pageTitle: 'ebwebApp.gOtherVoucher.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const gOtherVoucherPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: GOtherVoucherDeletePopupComponent,
        resolve: {
            gOtherVoucher: GOtherVoucherResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChungTuNghiepVuKhac_Xoa],
            pageTitle: 'ebwebApp.gOtherVoucher.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
