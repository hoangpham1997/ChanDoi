import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MaterialGoodsResourceTaxGroup } from 'app/shared/model/material-goods-resource-tax-group.model';
import { MaterialGoodsResourceTaxGroupService } from './material-goods-resource-tax-group.service';
import { MaterialGoodsResourceTaxGroupComponent } from './material-goods-resource-tax-group.component';
import { MaterialGoodsResourceTaxGroupDetailComponent } from './material-goods-resource-tax-group-detail.component';
import { MaterialGoodsResourceTaxGroupUpdateComponent } from './material-goods-resource-tax-group-update.component';
import { MaterialGoodsResourceTaxGroupDeletePopupComponent } from './material-goods-resource-tax-group-delete-dialog.component';
import { IMaterialGoodsResourceTaxGroup } from 'app/shared/model/material-goods-resource-tax-group.model';

@Injectable({ providedIn: 'root' })
export class MaterialGoodsResourceTaxGroupResolve implements Resolve<IMaterialGoodsResourceTaxGroup> {
    constructor(private service: MaterialGoodsResourceTaxGroupService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(
                    map((materialGoodsResourceTaxGroup: HttpResponse<MaterialGoodsResourceTaxGroup>) => materialGoodsResourceTaxGroup.body)
                );
        }
        return of(new MaterialGoodsResourceTaxGroup());
    }
}

export const materialGoodsResourceTaxGroupRoute: Routes = [
    {
        path: 'material-goods-resource-tax-group',
        component: MaterialGoodsResourceTaxGroupComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.materialGoodsResourceTaxGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-goods-resource-tax-group/:id/view',
        component: MaterialGoodsResourceTaxGroupDetailComponent,
        resolve: {
            materialGoodsResourceTaxGroup: MaterialGoodsResourceTaxGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsResourceTaxGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-goods-resource-tax-group/new',
        component: MaterialGoodsResourceTaxGroupUpdateComponent,
        resolve: {
            materialGoodsResourceTaxGroup: MaterialGoodsResourceTaxGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsResourceTaxGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-goods-resource-tax-group/:id/edit',
        component: MaterialGoodsResourceTaxGroupUpdateComponent,
        resolve: {
            materialGoodsResourceTaxGroup: MaterialGoodsResourceTaxGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsResourceTaxGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const materialGoodsResourceTaxGroupPopupRoute: Routes = [
    {
        path: 'material-goods-resource-tax-group/:id/delete',
        component: MaterialGoodsResourceTaxGroupDeletePopupComponent,
        resolve: {
            materialGoodsResourceTaxGroup: MaterialGoodsResourceTaxGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsResourceTaxGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
