import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { FixedAssetAccessories } from 'app/shared/model/fixed-asset-accessories.model';
import { FixedAssetAccessoriesService } from './fixed-asset-accessories.service';
import { FixedAssetAccessoriesComponent } from './fixed-asset-accessories.component';
import { FixedAssetAccessoriesDetailComponent } from './fixed-asset-accessories-detail.component';
import { FixedAssetAccessoriesUpdateComponent } from './fixed-asset-accessories-update.component';
import { FixedAssetAccessoriesDeletePopupComponent } from './fixed-asset-accessories-delete-dialog.component';
import { IFixedAssetAccessories } from 'app/shared/model/fixed-asset-accessories.model';

@Injectable({ providedIn: 'root' })
export class FixedAssetAccessoriesResolve implements Resolve<IFixedAssetAccessories> {
    constructor(private service: FixedAssetAccessoriesService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((fixedAssetAccessories: HttpResponse<FixedAssetAccessories>) => fixedAssetAccessories.body));
        }
        return of(new FixedAssetAccessories());
    }
}

export const fixedAssetAccessoriesRoute: Routes = [
    {
        path: 'fixed-asset-accessories',
        component: FixedAssetAccessoriesComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.fixedAssetAccessories.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fixed-asset-accessories/:id/view',
        component: FixedAssetAccessoriesDetailComponent,
        resolve: {
            fixedAssetAccessories: FixedAssetAccessoriesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetAccessories.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fixed-asset-accessories/new',
        component: FixedAssetAccessoriesUpdateComponent,
        resolve: {
            fixedAssetAccessories: FixedAssetAccessoriesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetAccessories.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fixed-asset-accessories/:id/edit',
        component: FixedAssetAccessoriesUpdateComponent,
        resolve: {
            fixedAssetAccessories: FixedAssetAccessoriesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetAccessories.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const fixedAssetAccessoriesPopupRoute: Routes = [
    {
        path: 'fixed-asset-accessories/:id/delete',
        component: FixedAssetAccessoriesDeletePopupComponent,
        resolve: {
            fixedAssetAccessories: FixedAssetAccessoriesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetAccessories.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
