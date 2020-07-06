import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { FixedAsset } from 'app/shared/model/fixed-asset.model';
import { FixedAssetService } from './fixed-asset.service';
import { FixedAssetComponent } from './fixed-asset.component';
import { FixedAssetDetailComponent } from './fixed-asset-detail.component';
import { FixedAssetUpdateComponent } from './fixed-asset-update.component';
import { FixedAssetDeletePopupComponent } from './fixed-asset-delete-dialog.component';
import { IFixedAsset } from 'app/shared/model/fixed-asset.model';

@Injectable({ providedIn: 'root' })
export class FixedAssetResolve implements Resolve<IFixedAsset> {
    constructor(private service: FixedAssetService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((fixedAsset: HttpResponse<FixedAsset>) => fixedAsset.body));
        }
        return of(new FixedAsset());
    }
}

export const fixedAssetRoute: Routes = [
    {
        path: 'fixed-asset',
        component: FixedAssetComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.fixedAsset.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fixed-asset/:id/view',
        component: FixedAssetDetailComponent,
        resolve: {
            fixedAsset: FixedAssetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAsset.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fixed-asset/new',
        component: FixedAssetUpdateComponent,
        resolve: {
            fixedAsset: FixedAssetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAsset.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fixed-asset/:id/edit',
        component: FixedAssetUpdateComponent,
        resolve: {
            fixedAsset: FixedAssetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAsset.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const fixedAssetPopupRoute: Routes = [
    {
        path: 'fixed-asset/:id/delete',
        component: FixedAssetDeletePopupComponent,
        resolve: {
            fixedAsset: FixedAssetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAsset.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
