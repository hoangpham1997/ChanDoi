import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MBInternalTransferTax } from 'app/shared/model/mb-internal-transfer-tax.model';
import { MBInternalTransferTaxService } from './mb-internal-transfer-tax.service';
import { MBInternalTransferTaxComponent } from './mb-internal-transfer-tax.component';
import { MBInternalTransferTaxDetailComponent } from './mb-internal-transfer-tax-detail.component';
import { MBInternalTransferTaxUpdateComponent } from './mb-internal-transfer-tax-update.component';
import { MBInternalTransferTaxDeletePopupComponent } from './mb-internal-transfer-tax-delete-dialog.component';
import { IMBInternalTransferTax } from 'app/shared/model/mb-internal-transfer-tax.model';

@Injectable({ providedIn: 'root' })
export class MBInternalTransferTaxResolve implements Resolve<IMBInternalTransferTax> {
    constructor(private service: MBInternalTransferTaxService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((mBInternalTransferTax: HttpResponse<MBInternalTransferTax>) => mBInternalTransferTax.body));
        }
        return of(new MBInternalTransferTax());
    }
}

export const mBInternalTransferTaxRoute: Routes = [
    {
        path: '',
        component: MBInternalTransferTaxComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.mBInternalTransferTax.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: MBInternalTransferTaxDetailComponent,
        resolve: {
            mBInternalTransferTax: MBInternalTransferTaxResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBInternalTransferTax.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: MBInternalTransferTaxUpdateComponent,
        resolve: {
            mBInternalTransferTax: MBInternalTransferTaxResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBInternalTransferTax.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: MBInternalTransferTaxUpdateComponent,
        resolve: {
            mBInternalTransferTax: MBInternalTransferTaxResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBInternalTransferTax.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const mBInternalTransferTaxPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MBInternalTransferTaxDeletePopupComponent,
        resolve: {
            mBInternalTransferTax: MBInternalTransferTaxResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBInternalTransferTax.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
