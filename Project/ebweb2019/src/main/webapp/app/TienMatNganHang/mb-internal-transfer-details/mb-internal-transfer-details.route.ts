import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MBInternalTransferDetails } from 'app/shared/model/mb-internal-transfer-details.model';
import { MBInternalTransferDetailsService } from './mb-internal-transfer-details.service';
import { MBInternalTransferDetailsComponent } from './mb-internal-transfer-details.component';
import { MBInternalTransferDetailsDetailComponent } from './mb-internal-transfer-details-detail.component';
import { MBInternalTransferDetailsUpdateComponent } from './mb-internal-transfer-details-update.component';
import { MBInternalTransferDetailsDeletePopupComponent } from './mb-internal-transfer-details-delete-dialog.component';
import { IMBInternalTransferDetails } from 'app/shared/model/mb-internal-transfer-details.model';

@Injectable({ providedIn: 'root' })
export class MBInternalTransferDetailsResolve implements Resolve<IMBInternalTransferDetails> {
    constructor(private service: MBInternalTransferDetailsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((mBInternalTransferDetails: HttpResponse<MBInternalTransferDetails>) => mBInternalTransferDetails.body));
        }
        return of(new MBInternalTransferDetails());
    }
}

export const mBInternalTransferDetailsRoute: Routes = [
    {
        path: '',
        component: MBInternalTransferDetailsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.mBInternalTransferDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: MBInternalTransferDetailsDetailComponent,
        resolve: {
            mBInternalTransferDetails: MBInternalTransferDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBInternalTransferDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: MBInternalTransferDetailsUpdateComponent,
        resolve: {
            mBInternalTransferDetails: MBInternalTransferDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBInternalTransferDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: MBInternalTransferDetailsUpdateComponent,
        resolve: {
            mBInternalTransferDetails: MBInternalTransferDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBInternalTransferDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const mBInternalTransferDetailsPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MBInternalTransferDetailsDeletePopupComponent,
        resolve: {
            mBInternalTransferDetails: MBInternalTransferDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBInternalTransferDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
