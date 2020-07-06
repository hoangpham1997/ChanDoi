import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';
import { DieuChinhCcdcComponent } from 'app/congcudungcu/dieu-chinh-ccdc/dieu-chinh-ccdc.component';
import { DieuChinhCcdcDetailComponent } from 'app/congcudungcu/dieu-chinh-ccdc/dieu-chinh-ccdc-detail.component';
import { DieuChinhCcdcUpdateComponent } from 'app/congcudungcu/dieu-chinh-ccdc/dieu-chinh-ccdc-update.component';
import { DieuChinhCcdcDeletePopupComponent } from 'app/congcudungcu/dieu-chinh-ccdc/dieu-chinh-ccdc-delete-dialog.component';
import { DieuChinhCcdcService } from 'app/congcudungcu/dieu-chinh-ccdc/dieu-chinh-ccdc.service';
import { ITIAdjustment, TIAdjustment } from 'app/shared/model/ti-adjustment.model';

@Injectable({ providedIn: 'root' })
export class ITIAdjustmentResolve implements Resolve<ITIAdjustment> {
    constructor(private service: DieuChinhCcdcService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((tiAdjustment: HttpResponse<ITIAdjustment>) => tiAdjustment.body));
        }
        return of(new TIAdjustment());
    }
}

export const dieuChinhCcdcRoute: Routes = [
    {
        path: '',
        component: DieuChinhCcdcComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.tIAdjustment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DieuChinhCcdcDetailComponent,
        resolve: {
            tiAdjustment: ITIAdjustmentResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAdjustment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DieuChinhCcdcUpdateComponent,
        resolve: {
            tiAdjustment: ITIAdjustmentResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAdjustment.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'hasSearch/:isSearch',
        component: DieuChinhCcdcComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.tIAdjustment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit/:rowNum',
        component: DieuChinhCcdcUpdateComponent,
        resolve: {
            tiAdjustment: ITIAdjustmentResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAdjustment.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: DieuChinhCcdcUpdateComponent,
        resolve: {
            tiAdjustment: ITIAdjustmentResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAdjustment.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-g-other-voucher',
        component: DieuChinhCcdcUpdateComponent,
        resolve: {
            tiAdjustment: ITIAdjustmentResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAdjustment.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const DieuChinhCcdcPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DieuChinhCcdcDeletePopupComponent,
        resolve: {
            tiAdjustment: ITIAdjustmentResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAdjustment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
