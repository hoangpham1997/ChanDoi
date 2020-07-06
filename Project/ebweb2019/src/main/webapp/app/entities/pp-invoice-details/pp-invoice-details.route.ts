import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PPInvoiceDetails } from 'app/shared/model/pp-invoice-details.model';
import { PPInvoiceDetailsService } from './pp-invoice-details.service';
import { PPInvoiceDetailsComponent } from './pp-invoice-details.component';
import { PPInvoiceDetailsDetailComponent } from './pp-invoice-details-detail.component';
import { PPInvoiceDetailsUpdateComponent } from './pp-invoice-details-update.component';
import { PPInvoiceDetailsDeletePopupComponent } from './pp-invoice-details-delete-dialog.component';
import { IPPInvoiceDetails } from 'app/shared/model/pp-invoice-details.model';

@Injectable({ providedIn: 'root' })
export class PPInvoiceDetailsResolve implements Resolve<IPPInvoiceDetails> {
    constructor(private service: PPInvoiceDetailsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((pPInvoiceDetails: HttpResponse<PPInvoiceDetails>) => pPInvoiceDetails.body));
        }
        return of(new PPInvoiceDetails());
    }
}

export const pPInvoiceDetailsRoute: Routes = [
    {
        path: 'pp-invoice-details',
        component: PPInvoiceDetailsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.pPInvoiceDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pp-invoice-details/:id/view',
        component: PPInvoiceDetailsDetailComponent,
        resolve: {
            pPInvoiceDetails: PPInvoiceDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pPInvoiceDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pp-invoice-details/new',
        component: PPInvoiceDetailsUpdateComponent,
        resolve: {
            pPInvoiceDetails: PPInvoiceDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pPInvoiceDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pp-invoice-details/:id/edit',
        component: PPInvoiceDetailsUpdateComponent,
        resolve: {
            pPInvoiceDetails: PPInvoiceDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pPInvoiceDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pPInvoiceDetailsPopupRoute: Routes = [
    {
        path: 'pp-invoice-details/:id/delete',
        component: PPInvoiceDetailsDeletePopupComponent,
        resolve: {
            pPInvoiceDetails: PPInvoiceDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pPInvoiceDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
