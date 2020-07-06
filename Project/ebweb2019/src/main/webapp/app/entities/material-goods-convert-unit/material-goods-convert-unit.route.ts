import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MaterialGoodsConvertUnit } from 'app/shared/model/material-goods-convert-unit.model';
import { MaterialGoodsConvertUnitService } from './material-goods-convert-unit.service';
import { MaterialGoodsConvertUnitComponent } from './material-goods-convert-unit.component';
import { MaterialGoodsConvertUnitDetailComponent } from './material-goods-convert-unit-detail.component';
import { MaterialGoodsConvertUnitUpdateComponent } from './material-goods-convert-unit-update.component';
import { MaterialGoodsConvertUnitDeletePopupComponent } from './material-goods-convert-unit-delete-dialog.component';
import { IMaterialGoodsConvertUnit } from 'app/shared/model/material-goods-convert-unit.model';

@Injectable({ providedIn: 'root' })
export class MaterialGoodsConvertUnitResolve implements Resolve<IMaterialGoodsConvertUnit> {
    constructor(private service: MaterialGoodsConvertUnitService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((materialGoodsConvertUnit: HttpResponse<MaterialGoodsConvertUnit>) => materialGoodsConvertUnit.body));
        }
        return of(new MaterialGoodsConvertUnit());
    }
}

export const materialGoodsConvertUnitRoute: Routes = [
    {
        path: 'material-goods-convert-unit',
        component: MaterialGoodsConvertUnitComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.materialGoodsConvertUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-goods-convert-unit/:id/view',
        component: MaterialGoodsConvertUnitDetailComponent,
        resolve: {
            materialGoodsConvertUnit: MaterialGoodsConvertUnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsConvertUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-goods-convert-unit/new',
        component: MaterialGoodsConvertUnitUpdateComponent,
        resolve: {
            materialGoodsConvertUnit: MaterialGoodsConvertUnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsConvertUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-goods-convert-unit/:id/edit',
        component: MaterialGoodsConvertUnitUpdateComponent,
        resolve: {
            materialGoodsConvertUnit: MaterialGoodsConvertUnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsConvertUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const materialGoodsConvertUnitPopupRoute: Routes = [
    {
        path: 'material-goods-convert-unit/:id/delete',
        component: MaterialGoodsConvertUnitDeletePopupComponent,
        resolve: {
            materialGoodsConvertUnit: MaterialGoodsConvertUnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsConvertUnit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
