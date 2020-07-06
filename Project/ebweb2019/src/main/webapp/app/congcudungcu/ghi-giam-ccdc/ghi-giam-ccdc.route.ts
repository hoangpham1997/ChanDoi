import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';
import { GhiGiamCcdcComponent } from 'app/congcudungcu/ghi-giam-ccdc/ghi-giam-ccdc.component';
import { GhiGiamCcdcDetailComponent } from 'app/congcudungcu/ghi-giam-ccdc/ghi-giam-ccdc-detail.component';
import { GhiGiamCcdcUpdateComponent } from 'app/congcudungcu/ghi-giam-ccdc/ghi-giam-ccdc-update.component';
import { GhiGiamCcdcDeletePopupComponent } from 'app/congcudungcu/ghi-giam-ccdc/ghi-giam-ccdc-delete-dialog.component';
import { ITIDecrement, TIDecrement } from 'app/shared/model/ti-decrement.model';
import { GhiGiamCcdcService } from 'app/congcudungcu/ghi-giam-ccdc/ghi-giam-ccdc.service';

@Injectable({ providedIn: 'root' })
export class ITIDecrementResolve implements Resolve<ITIDecrement> {
    constructor(private service: GhiGiamCcdcService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((tiDecrement: HttpResponse<ITIDecrement>) => tiDecrement.body));
        }
        return of(new TIDecrement());
    }
}

export const GhiGiamCcdcRoute: Routes = [
    {
        path: '',
        component: GhiGiamCcdcComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.tIIncrement.home.title2'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: GhiGiamCcdcDetailComponent,
        resolve: {
            tiDecrement: ITIDecrementResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIIncrement.home.title2'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: GhiGiamCcdcUpdateComponent,
        resolve: {
            tiDecrement: ITIDecrementResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIIncrement.home.title2'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'hasSearch/:isSearch',
        component: GhiGiamCcdcComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.tIIncrement.home.title2'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit/:rowNum',
        component: GhiGiamCcdcUpdateComponent,
        resolve: {
            tiDecrement: ITIDecrementResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIIncrement.home.title2'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: GhiGiamCcdcUpdateComponent,
        resolve: {
            tiDecrement: ITIDecrementResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIIncrement.home.title2'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-g-other-voucher',
        component: GhiGiamCcdcUpdateComponent,
        resolve: {
            tiDecrement: ITIDecrementResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIIncrement.home.title2'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const GhiGiamCcdcPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: GhiGiamCcdcDeletePopupComponent,
        resolve: {
            tiDecrement: ITIDecrementResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIIncrement.home.title2'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
