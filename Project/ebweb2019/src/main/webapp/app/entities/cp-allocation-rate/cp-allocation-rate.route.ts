import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CPAllocationRate } from 'app/shared/model/cp-allocation-rate.model';
import { CPAllocationRateService } from './cp-allocation-rate.service';
import { CPAllocationRateComponent } from './cp-allocation-rate.component';
import { CPAllocationRateDetailComponent } from './cp-allocation-rate-detail.component';
import { CPAllocationRateUpdateComponent } from './cp-allocation-rate-update.component';
import { CPAllocationRateDeletePopupComponent } from './cp-allocation-rate-delete-dialog.component';
import { ICPAllocationRate } from 'app/shared/model/cp-allocation-rate.model';

@Injectable({ providedIn: 'root' })
export class CPAllocationRateResolve implements Resolve<ICPAllocationRate> {
    constructor(private service: CPAllocationRateService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((cPAllocationRate: HttpResponse<CPAllocationRate>) => cPAllocationRate.body));
        }
        return of(new CPAllocationRate());
    }
}

export const cPAllocationRateRoute: Routes = [
    {
        path: 'cp-allocation-rate',
        component: CPAllocationRateComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.cPAllocationRate.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cp-allocation-rate/:id/view',
        component: CPAllocationRateDetailComponent,
        resolve: {
            cPAllocationRate: CPAllocationRateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPAllocationRate.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cp-allocation-rate/new',
        component: CPAllocationRateUpdateComponent,
        resolve: {
            cPAllocationRate: CPAllocationRateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPAllocationRate.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cp-allocation-rate/:id/edit',
        component: CPAllocationRateUpdateComponent,
        resolve: {
            cPAllocationRate: CPAllocationRateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPAllocationRate.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cPAllocationRatePopupRoute: Routes = [
    {
        path: 'cp-allocation-rate/:id/delete',
        component: CPAllocationRateDeletePopupComponent,
        resolve: {
            cPAllocationRate: CPAllocationRateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPAllocationRate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
