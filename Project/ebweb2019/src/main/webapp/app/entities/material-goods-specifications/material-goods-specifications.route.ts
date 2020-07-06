import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MaterialGoodsSpecifications } from 'app/shared/model/material-goods-specifications.model';
import { MaterialGoodsSpecificationsService } from './material-goods-specifications.service';
import { MaterialGoodsSpecificationsComponent } from './material-goods-specifications.component';
import { MaterialGoodsSpecificationsDetailComponent } from './material-goods-specifications-detail.component';
import { MaterialGoodsSpecificationsUpdateComponent } from './material-goods-specifications-update.component';
import { MaterialGoodsSpecificationsDeletePopupComponent } from './material-goods-specifications-delete-dialog.component';
import { IMaterialGoodsSpecifications } from 'app/shared/model/material-goods-specifications.model';

@Injectable({ providedIn: 'root' })
export class MaterialGoodsSpecificationsResolve implements Resolve<IMaterialGoodsSpecifications> {
    constructor(private service: MaterialGoodsSpecificationsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((materialGoodsSpecifications: HttpResponse<MaterialGoodsSpecifications>) => materialGoodsSpecifications.body));
        }
        return of(new MaterialGoodsSpecifications());
    }
}

export const materialGoodsSpecificationsRoute: Routes = [
    {
        path: 'material-goods-specifications',
        component: MaterialGoodsSpecificationsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.materialGoodsSpecifications.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-goods-specifications/:id/view',
        component: MaterialGoodsSpecificationsDetailComponent,
        resolve: {
            materialGoodsSpecifications: MaterialGoodsSpecificationsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsSpecifications.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-goods-specifications/new',
        component: MaterialGoodsSpecificationsUpdateComponent,
        resolve: {
            materialGoodsSpecifications: MaterialGoodsSpecificationsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsSpecifications.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-goods-specifications/:id/edit',
        component: MaterialGoodsSpecificationsUpdateComponent,
        resolve: {
            materialGoodsSpecifications: MaterialGoodsSpecificationsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsSpecifications.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const materialGoodsSpecificationsPopupRoute: Routes = [
    {
        path: 'material-goods-specifications/:id/delete',
        component: MaterialGoodsSpecificationsDeletePopupComponent,
        resolve: {
            materialGoodsSpecifications: MaterialGoodsSpecificationsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsSpecifications.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
