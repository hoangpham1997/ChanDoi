import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';
import { DieuChuyenCcdcComponent } from 'app/congcudungcu/dieu-chuyen-ccdc/dieu-chuyen-ccdc.component';
import { DieuChuyenCcdcDetailComponent } from 'app/congcudungcu/dieu-chuyen-ccdc/dieu-chuyen-ccdc-detail.component';
import { DieuChuyenCcdcUpdateComponent } from 'app/congcudungcu/dieu-chuyen-ccdc/dieu-chuyen-ccdc-update.component';
import { DieuChuyenCcdcDeletePopupComponent } from 'app/congcudungcu/dieu-chuyen-ccdc/dieu-chuyen-ccdc-delete-dialog.component';
import { ITITransfer, TITransfer } from 'app/shared/model/ti-transfer.model';
import { DieuChuyenCcdcService } from 'app/congcudungcu/dieu-chuyen-ccdc/dieu-chuyen-ccdc.service';

@Injectable({ providedIn: 'root' })
export class ITITransferResolve implements Resolve<ITITransfer> {
    constructor(private service: DieuChuyenCcdcService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((tiTransfer: HttpResponse<ITITransfer>) => tiTransfer.body));
        }
        return of(new TITransfer());
    }
}
// @Injectable({ providedIn: 'root' })
// export class ITITransferResolve implements Resolve<ITITransfer> {
//     constructor(private service: KiemKeCcdcService) {}
//
//     resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
//         const id = route.params['id'] ? route.params['id'] : null;
//         if (id) {
//             return this.service.find(id).pipe(map((tiTransfer: HttpResponse<ITITransfer>) => tiTransfer.body));
//         }
//         return of(new TITransfer());
//     }
// }

export const DieuChuyenCcdcRoute: Routes = [
    {
        path: '',
        component: DieuChuyenCcdcComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.tITransfer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DieuChuyenCcdcDetailComponent,
        resolve: {
            tiTransfer: ITITransferResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tITransfer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DieuChuyenCcdcUpdateComponent,
        resolve: {
            tiTransfer: ITITransferResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tITransfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'hasSearch/:isSearch',
        component: DieuChuyenCcdcComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.tITransfer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit/:rowNum',
        component: DieuChuyenCcdcUpdateComponent,
        resolve: {
            tiTransfer: ITITransferResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tITransfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: DieuChuyenCcdcUpdateComponent,
        resolve: {
            tiTransfer: ITITransferResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tITransfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-g-other-voucher',
        component: DieuChuyenCcdcUpdateComponent,
        resolve: {
            tiTransfer: ITITransferResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tITransfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const DieuChuyenCcdcPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DieuChuyenCcdcDeletePopupComponent,
        resolve: {
            tiTransfer: ITITransferResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tITransfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
