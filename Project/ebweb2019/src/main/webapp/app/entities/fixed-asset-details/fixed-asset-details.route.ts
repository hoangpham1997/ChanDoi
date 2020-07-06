import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { FixedAssetDetails } from 'app/shared/model/fixed-asset-details.model';
import { FixedAssetDetailsService } from './fixed-asset-details.service';
import { FixedAssetDetailsComponent } from './fixed-asset-details.component';
import { FixedAssetDetailsDetailComponent } from './fixed-asset-details-detail.component';
import { FixedAssetDetailsUpdateComponent } from './fixed-asset-details-update.component';
import { FixedAssetDetailsDeletePopupComponent } from './fixed-asset-details-delete-dialog.component';
import { IFixedAssetDetails } from 'app/shared/model/fixed-asset-details.model';

@Injectable({ providedIn: 'root' })
export class FixedAssetDetailsResolve implements Resolve<IFixedAssetDetails> {
    constructor(private service: FixedAssetDetailsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((fixedAssetDetails: HttpResponse<FixedAssetDetails>) => fixedAssetDetails.body));
        }
        return of(new FixedAssetDetails());
    }
}

export const fixedAssetDetailsRoute: Routes = [
    {
        path: 'fixed-asset-details',
        component: FixedAssetDetailsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.fixedAssetDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fixed-asset-details/:id/view',
        component: FixedAssetDetailsDetailComponent,
        resolve: {
            fixedAssetDetails: FixedAssetDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fixed-asset-details/new',
        component: FixedAssetDetailsUpdateComponent,
        resolve: {
            fixedAssetDetails: FixedAssetDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fixed-asset-details/:id/edit',
        component: FixedAssetDetailsUpdateComponent,
        resolve: {
            fixedAssetDetails: FixedAssetDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const fixedAssetDetailsPopupRoute: Routes = [
    {
        path: 'fixed-asset-details/:id/delete',
        component: FixedAssetDetailsDeletePopupComponent,
        resolve: {
            fixedAssetDetails: FixedAssetDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
