import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MBInternalTransfer } from 'app/shared/model/mb-internal-transfer.model';
import { MBInternalTransferService } from './mb-internal-transfer.service';
import { MBInternalTransferComponent } from './mb-internal-transfer.component';
import { MBInternalTransferDetailComponent } from './mb-internal-transfer-detail.component';
import { MBInternalTransferUpdateComponent } from './mb-internal-transfer-update.component';
import { MBInternalTransferDeletePopupComponent } from './mb-internal-transfer-delete-dialog.component';
import { IMBInternalTransfer } from 'app/shared/model/mb-internal-transfer.model';

@Injectable({ providedIn: 'root' })
export class MBInternalTransferResolve implements Resolve<IMBInternalTransfer> {
    constructor(private service: MBInternalTransferService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((mBInternalTransfer: HttpResponse<MBInternalTransfer>) => mBInternalTransfer.body));
        }
        return of(new MBInternalTransfer());
    }
}

export const mBInternalTransferRoute: Routes = [
    {
        path: '',
        component: MBInternalTransferComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.mBInternalTransfer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: MBInternalTransferDetailComponent,
        resolve: {
            mBInternalTransfer: MBInternalTransferResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBInternalTransfer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: MBInternalTransferUpdateComponent,
        resolve: {
            mBInternalTransfer: MBInternalTransferResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBInternalTransfer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: MBInternalTransferUpdateComponent,
        resolve: {
            mBInternalTransfer: MBInternalTransferResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBInternalTransfer.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const mBInternalTransferPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MBInternalTransferDeletePopupComponent,
        resolve: {
            mBInternalTransfer: MBInternalTransferResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBInternalTransfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
