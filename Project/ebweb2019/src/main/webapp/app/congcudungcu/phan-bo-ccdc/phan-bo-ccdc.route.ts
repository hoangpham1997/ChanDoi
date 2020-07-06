import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';
import { PhanBoCcdcComponent } from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc.component';
import { PhanBoCcdcDetailComponent } from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc-detail.component';
import { PhanBoCcdcUpdateComponent } from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc-update.component';
import { PhanBoCcdcDeletePopupComponent } from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc-delete-dialog.component';
import { ITIAllocation, TIAllocation } from 'app/shared/model/ti-allocation.model';
import { PhanBoCcdcService } from 'app/congcudungcu/phan-bo-ccdc/phan-bo-ccdc.service';

@Injectable({ providedIn: 'root' })
export class TIAllocationResolve implements Resolve<ITIAllocation> {
    constructor(private service: PhanBoCcdcService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((tiAllocation: HttpResponse<TIAllocation>) => tiAllocation.body));
        }
        return of(new TIAllocation());
    }
}

export const phanBoCcdcRoute: Routes = [
    {
        path: '',
        component: PhanBoCcdcComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.tIAllocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: PhanBoCcdcDetailComponent,
        resolve: {
            tiAllocation: TIAllocationResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAllocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: PhanBoCcdcUpdateComponent,
        resolve: {
            tiAllocation: TIAllocationResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAllocation.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'hasSearch/:isSearch',
        component: PhanBoCcdcComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.tIAllocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit/:rowNum',
        component: PhanBoCcdcUpdateComponent,
        resolve: {
            tiAllocation: TIAllocationResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAllocation.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: PhanBoCcdcUpdateComponent,
        resolve: {
            tiAllocation: TIAllocationResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAllocation.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-g-other-voucher',
        component: PhanBoCcdcUpdateComponent,
        resolve: {
            tiAllocation: TIAllocationResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAllocation.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const PhanBoCcdcPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: PhanBoCcdcDeletePopupComponent,
        resolve: {
            tiAllocation: TIAllocationResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAllocation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
