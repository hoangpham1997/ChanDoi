import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MaterialGoodsAssembly } from 'app/shared/model/material-goods-assembly.model';
import { MaterialGoodsAssemblyService } from './material-goods-assembly.service';
import { MaterialGoodsAssemblyComponent } from './material-goods-assembly.component';
import { MaterialGoodsAssemblyDetailComponent } from './material-goods-assembly-detail.component';
import { MaterialGoodsAssemblyUpdateComponent } from './material-goods-assembly-update.component';
import { MaterialGoodsAssemblyDeletePopupComponent } from './material-goods-assembly-delete-dialog.component';
import { IMaterialGoodsAssembly } from 'app/shared/model/material-goods-assembly.model';

@Injectable({ providedIn: 'root' })
export class MaterialGoodsAssemblyResolve implements Resolve<IMaterialGoodsAssembly> {
    constructor(private service: MaterialGoodsAssemblyService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((materialGoodsAssembly: HttpResponse<MaterialGoodsAssembly>) => materialGoodsAssembly.body));
        }
        return of(new MaterialGoodsAssembly());
    }
}

export const materialGoodsAssemblyRoute: Routes = [
    {
        path: 'material-goods-assembly',
        component: MaterialGoodsAssemblyComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.materialGoodsAssembly.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-goods-assembly/:id/view',
        component: MaterialGoodsAssemblyDetailComponent,
        resolve: {
            materialGoodsAssembly: MaterialGoodsAssemblyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsAssembly.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-goods-assembly/new',
        component: MaterialGoodsAssemblyUpdateComponent,
        resolve: {
            materialGoodsAssembly: MaterialGoodsAssemblyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsAssembly.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-goods-assembly/:id/edit',
        component: MaterialGoodsAssemblyUpdateComponent,
        resolve: {
            materialGoodsAssembly: MaterialGoodsAssemblyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsAssembly.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const materialGoodsAssemblyPopupRoute: Routes = [
    {
        path: 'material-goods-assembly/:id/delete',
        component: MaterialGoodsAssemblyDeletePopupComponent,
        resolve: {
            materialGoodsAssembly: MaterialGoodsAssemblyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsAssembly.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
