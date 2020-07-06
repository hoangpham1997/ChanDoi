import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CostSetMaterialGood } from 'app/shared/model/cost-set-material-good.model';
import { CostSetMaterialGoodService } from './cost-set-material-good.service';
import { CostSetMaterialGoodComponent } from './cost-set-material-good.component';
import { CostSetMaterialGoodDetailComponent } from './cost-set-material-good-detail.component';
import { CostSetMaterialGoodUpdateComponent } from './cost-set-material-good-update.component';
import { CostSetMaterialGoodDeletePopupComponent } from './cost-set-material-good-delete-dialog.component';
import { ICostSetMaterialGood } from 'app/shared/model/cost-set-material-good.model';

@Injectable({ providedIn: 'root' })
export class CostSetMaterialGoodResolve implements Resolve<ICostSetMaterialGood> {
    constructor(private service: CostSetMaterialGoodService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((costSetMaterialGood: HttpResponse<CostSetMaterialGood>) => costSetMaterialGood.body));
        }
        return of(new CostSetMaterialGood());
    }
}

export const costSetMaterialGoodRoute: Routes = [
    {
        path: 'cost-set-material-good',
        component: CostSetMaterialGoodComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.costSetMaterialGood.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cost-set-material-good/:id/view',
        component: CostSetMaterialGoodDetailComponent,
        resolve: {
            costSetMaterialGood: CostSetMaterialGoodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.costSetMaterialGood.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cost-set-material-good/new',
        component: CostSetMaterialGoodUpdateComponent,
        resolve: {
            costSetMaterialGood: CostSetMaterialGoodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.costSetMaterialGood.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cost-set-material-good/:id/edit',
        component: CostSetMaterialGoodUpdateComponent,
        resolve: {
            costSetMaterialGood: CostSetMaterialGoodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.costSetMaterialGood.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const costSetMaterialGoodPopupRoute: Routes = [
    {
        path: 'cost-set-material-good/:id/delete',
        component: CostSetMaterialGoodDeletePopupComponent,
        resolve: {
            costSetMaterialGood: CostSetMaterialGoodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.costSetMaterialGood.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
