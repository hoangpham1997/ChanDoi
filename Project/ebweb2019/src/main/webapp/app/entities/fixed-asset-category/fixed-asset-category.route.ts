import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { FixedAssetCategory } from 'app/shared/model/fixed-asset-category.model';
import { FixedAssetCategoryService } from './fixed-asset-category.service';
import { FixedAssetCategoryComponent } from './fixed-asset-category.component';
import { FixedAssetCategoryDetailComponent } from './fixed-asset-category-detail.component';
import { FixedAssetCategoryUpdateComponent } from './fixed-asset-category-update.component';
import { FixedAssetCategoryDeletePopupComponent } from './fixed-asset-category-delete-dialog.component';
import { IFixedAssetCategory } from 'app/shared/model/fixed-asset-category.model';

@Injectable({ providedIn: 'root' })
export class FixedAssetCategoryResolve implements Resolve<IFixedAssetCategory> {
    constructor(private service: FixedAssetCategoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((fixedAssetCategory: HttpResponse<FixedAssetCategory>) => fixedAssetCategory.body));
        }
        return of(new FixedAssetCategory());
    }
}

export const fixedAssetCategoryRoute: Routes = [
    {
        path: 'fixed-asset-category',
        component: FixedAssetCategoryComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.fixedAssetCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fixed-asset-category/:id/view',
        component: FixedAssetCategoryDetailComponent,
        resolve: {
            fixedAssetCategory: FixedAssetCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fixed-asset-category/new',
        component: FixedAssetCategoryUpdateComponent,
        resolve: {
            fixedAssetCategory: FixedAssetCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fixed-asset-category/:id/edit',
        component: FixedAssetCategoryUpdateComponent,
        resolve: {
            fixedAssetCategory: FixedAssetCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const fixedAssetCategoryPopupRoute: Routes = [
    {
        path: 'fixed-asset-category/:id/delete',
        component: FixedAssetCategoryDeletePopupComponent,
        resolve: {
            fixedAssetCategory: FixedAssetCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
