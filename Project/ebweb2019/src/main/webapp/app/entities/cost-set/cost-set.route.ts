import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CostSet } from 'app/shared/model/cost-set.model';
import { CostSetService } from './cost-set.service';
import { CostSetComponent } from './cost-set.component';
import { CostSetDetailComponent } from './cost-set-detail.component';
import { CostSetUpdateComponent } from './cost-set-update.component';
import { CostSetDeletePopupComponent } from './cost-set-delete-dialog.component';
import { ICostSet } from 'app/shared/model/cost-set.model';

@Injectable({ providedIn: 'root' })
export class CostSetResolve implements Resolve<ICostSet> {
    constructor(private service: CostSetService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((costSet: HttpResponse<CostSet>) => costSet.body));
        }
        return of(new CostSet());
    }
}

export const costSetRoute: Routes = [
    {
        path: 'cost-set',
        component: CostSetComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.costSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cost-set/:id/view',
        component: CostSetDetailComponent,
        resolve: {
            costSet: CostSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.costSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cost-set/new',
        component: CostSetUpdateComponent,
        resolve: {
            costSet: CostSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.costSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cost-set/:id/edit',
        component: CostSetUpdateComponent,
        resolve: {
            costSet: CostSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.costSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const costSetPopupRoute: Routes = [
    {
        path: 'cost-set/:id/delete',
        component: CostSetDeletePopupComponent,
        resolve: {
            costSet: CostSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.costSet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
