import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';
import { PPDiscountReturnDetailsService } from './pp-discount-return-details.service';
import { PPDiscountReturnDetailsComponent } from './pp-discount-return-details.component';
import { PPDiscountReturnDetailsDetailComponent } from './pp-discount-return-details-detail.component';
import { PPDiscountReturnDetailsUpdateComponent } from './pp-discount-return-details-update.component';
import { PPDiscountReturnDetailsDeletePopupComponent } from './pp-discount-return-details-delete-dialog.component';
import { IPPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';

@Injectable({ providedIn: 'root' })
export class PPDiscountReturnDetailsResolve implements Resolve<IPPDiscountReturnDetails> {
    constructor(private service: PPDiscountReturnDetailsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((pPDiscountReturnDetails: HttpResponse<PPDiscountReturnDetails>) => pPDiscountReturnDetails.body));
        }
        return of(new PPDiscountReturnDetails());
    }
}

export const pPDiscountReturnDetailsRoute: Routes = [
    {
        path: 'pp-discount-return-details',
        component: PPDiscountReturnDetailsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.pPDiscountReturnDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pp-discount-return-details/:id/view',
        component: PPDiscountReturnDetailsDetailComponent,
        resolve: {
            pPDiscountReturnDetails: PPDiscountReturnDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pPDiscountReturnDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pp-discount-return-details/new',
        component: PPDiscountReturnDetailsUpdateComponent,
        resolve: {
            pPDiscountReturnDetails: PPDiscountReturnDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pPDiscountReturnDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pp-discount-return-details/:id/edit',
        component: PPDiscountReturnDetailsUpdateComponent,
        resolve: {
            pPDiscountReturnDetails: PPDiscountReturnDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pPDiscountReturnDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pPDiscountReturnDetailsPopupRoute: Routes = [
    {
        path: 'pp-discount-return-details/:id/delete',
        component: PPDiscountReturnDetailsDeletePopupComponent,
        resolve: {
            pPDiscountReturnDetails: PPDiscountReturnDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pPDiscountReturnDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
