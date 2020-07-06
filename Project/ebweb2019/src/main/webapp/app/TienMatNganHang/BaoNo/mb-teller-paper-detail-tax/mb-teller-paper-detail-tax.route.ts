import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MBTellerPaperDetailTax } from 'app/shared/model/mb-teller-paper-detail-tax.model';
import { MBTellerPaperDetailTaxService } from './mb-teller-paper-detail-tax.service';
import { MBTellerPaperDetailTaxComponent } from './mb-teller-paper-detail-tax.component';
import { MBTellerPaperDetailTaxDetailComponent } from './mb-teller-paper-detail-tax-detail.component';
import { MBTellerPaperDetailTaxUpdateComponent } from './mb-teller-paper-detail-tax-update.component';
import { MBTellerPaperDetailTaxDeletePopupComponent } from './mb-teller-paper-detail-tax-delete-dialog.component';
import { IMBTellerPaperDetailTax } from 'app/shared/model/mb-teller-paper-detail-tax.model';

@Injectable({ providedIn: 'root' })
export class MBTellerPaperDetailTaxResolve implements Resolve<IMBTellerPaperDetailTax> {
    constructor(private service: MBTellerPaperDetailTaxService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((mBTellerPaperDetailTax: HttpResponse<MBTellerPaperDetailTax>) => mBTellerPaperDetailTax.body));
        }
        return of(new MBTellerPaperDetailTax());
    }
}

export const mBTellerPaperDetailTaxRoute: Routes = [
    {
        path: '',
        component: MBTellerPaperDetailTaxComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.mBTellerPaperDetailTax.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: MBTellerPaperDetailTaxDetailComponent,
        resolve: {
            mBTellerPaperDetailTax: MBTellerPaperDetailTaxResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBTellerPaperDetailTax.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: MBTellerPaperDetailTaxUpdateComponent,
        resolve: {
            mBTellerPaperDetailTax: MBTellerPaperDetailTaxResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBTellerPaperDetailTax.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: MBTellerPaperDetailTaxUpdateComponent,
        resolve: {
            mBTellerPaperDetailTax: MBTellerPaperDetailTaxResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBTellerPaperDetailTax.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const mBTellerPaperDetailTaxPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MBTellerPaperDetailTaxDeletePopupComponent,
        resolve: {
            mBTellerPaperDetailTax: MBTellerPaperDetailTaxResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBTellerPaperDetailTax.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
