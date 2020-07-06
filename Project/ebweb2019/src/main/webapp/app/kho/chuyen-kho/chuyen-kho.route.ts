import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { ChuyenKhoComponent } from './chuyen-kho.component';
import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { of } from 'rxjs';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE_ADMIN } from 'app/app.constants';
import { ROLE } from 'app/role.constants';
import { ChuyenKhoUpdateComponent } from 'app/kho/chuyen-kho/chuyen-kho-update.component';
import { ChuyenKhoDeletePopupComponent } from 'app/kho/chuyen-kho/chuyen-kho-delete-dialog.component';
import { RSTransfer } from 'app/shared/model/rs-transfer.model';
import { RsTransferService } from 'app/kho/chuyen-kho/rs-transfer.service';

@Injectable({ providedIn: 'root' })
export class RSTransferResolve implements Resolve<RSTransfer> {
    constructor(private service: RsTransferService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((rsTransfer: HttpResponse<RSTransfer>) => rsTransfer.body));
        }
        return of(new RSTransfer());
    }
}

export const ChuyenKhoRoute: Routes = [
    {
        path: '',
        component: ChuyenKhoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.ChuyenKho_XEM],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.warehouseTransfer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: ChuyenKhoUpdateComponent,
        data: {
            authorities: ['ROLE_ADMIN', ROLE.ChuyenKho_THEM],
            pageTitle: 'ebwebApp.warehouseTransfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/:rowNum',
        component: ChuyenKhoUpdateComponent,
        resolve: {
            rsTransfer: RSTransferResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.ChuyenKho_XEM],
            pageTitle: 'ebwebApp.warehouseTransfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'chuyen-kho/:id/edit',
        component: ChuyenKhoUpdateComponent,
        resolve: {
            rsTransfer: RSTransferResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.ChuyenKho_XEM],
            pageTitle: 'ebwebApp.warehouseTransfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: ChuyenKhoUpdateComponent,
        resolve: {
            rsTransfer: RSTransferResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.ChuyenKho_XEM],
            pageTitle: 'ebwebApp.warehouseTransfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const ChuyenKhoPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ChuyenKhoDeletePopupComponent,
        resolve: {
            rsTransfer: RSTransferResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.ChuyenKho_XEM],
            pageTitle: 'ebwebApp.mBTellerPaper.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
