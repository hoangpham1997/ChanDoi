import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { GOtherVoucher } from 'app/shared/model/g-other-voucher.model';

import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { PhanBoChiPhiTraTruocService } from 'app/tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc.service';
import { PhanBoChiPhiTraTruocComponent } from 'app/tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc.component';
import { PhanBoChiPhiTraTruocDetailComponent } from 'app/tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc-detail.component';
import { PhanBoChiPhiTraTruocUpdateComponent } from 'app/tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc-update.component';
import { PhanBoChiPhiTraTruocDeletePopupComponent } from 'app/tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc-delete-dialog.component';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class GOtherVoucherResolve implements Resolve<IGOtherVoucher> {
    constructor(private service: PhanBoChiPhiTraTruocService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.findDetailPB(id).pipe(map((gOtherVoucher: HttpResponse<GOtherVoucher>) => gOtherVoucher.body));
        }
        return of(new GOtherVoucher());
    }
}

export const phanBoChiPhiTraTruocRoute: Routes = [
    {
        path: '',
        component: PhanBoChiPhiTraTruocComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.gOtherVoucher.home.titleGOtherVoucher'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: PhanBoChiPhiTraTruocDetailComponent,
        resolve: {
            gOtherVoucher: GOtherVoucherResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.gOtherVoucher.home.titleGOtherVoucher'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: PhanBoChiPhiTraTruocUpdateComponent,
        resolve: {
            gOtherVoucher: GOtherVoucherResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.gOtherVoucher.home.titleGOtherVoucher'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'hasSearch/:isSearch',
        component: PhanBoChiPhiTraTruocComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.gOtherVoucher.home.titleGOtherVoucher'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit/:rowNum',
        component: PhanBoChiPhiTraTruocUpdateComponent,
        resolve: {
            gOtherVoucher: GOtherVoucherResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.gOtherVoucher.home.titleGOtherVoucher'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: PhanBoChiPhiTraTruocUpdateComponent,
        resolve: {
            gOtherVoucher: GOtherVoucherResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.gOtherVoucher.home.titleGOtherVoucher'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-g-other-voucher',
        component: PhanBoChiPhiTraTruocUpdateComponent,
        resolve: {
            gOtherVoucher: GOtherVoucherResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.gOtherVoucher.home.titleGOtherVoucher'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const PhanBoChiPhiTraTruocPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: PhanBoChiPhiTraTruocDeletePopupComponent,
        resolve: {
            gOtherVoucher: GOtherVoucherResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.gOtherVoucher.home.titleGOtherVoucher'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
