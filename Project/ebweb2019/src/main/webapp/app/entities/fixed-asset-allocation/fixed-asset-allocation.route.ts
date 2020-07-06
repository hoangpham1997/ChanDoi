import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { FixedAssetAllocation } from 'app/shared/model/fixed-asset-allocation.model';
import { FixedAssetAllocationService } from './fixed-asset-allocation.service';
import { FixedAssetAllocationComponent } from './fixed-asset-allocation.component';
import { FixedAssetAllocationDetailComponent } from './fixed-asset-allocation-detail.component';
import { FixedAssetAllocationUpdateComponent } from './fixed-asset-allocation-update.component';
import { FixedAssetAllocationDeletePopupComponent } from './fixed-asset-allocation-delete-dialog.component';
import { IFixedAssetAllocation } from 'app/shared/model/fixed-asset-allocation.model';

@Injectable({ providedIn: 'root' })
export class FixedAssetAllocationResolve implements Resolve<IFixedAssetAllocation> {
    constructor(private service: FixedAssetAllocationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((fixedAssetAllocation: HttpResponse<FixedAssetAllocation>) => fixedAssetAllocation.body));
        }
        return of(new FixedAssetAllocation());
    }
}

export const fixedAssetAllocationRoute: Routes = [
    {
        path: 'fixed-asset-allocation',
        component: FixedAssetAllocationComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.fixedAssetAllocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fixed-asset-allocation/:id/view',
        component: FixedAssetAllocationDetailComponent,
        resolve: {
            fixedAssetAllocation: FixedAssetAllocationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetAllocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fixed-asset-allocation/new',
        component: FixedAssetAllocationUpdateComponent,
        resolve: {
            fixedAssetAllocation: FixedAssetAllocationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetAllocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fixed-asset-allocation/:id/edit',
        component: FixedAssetAllocationUpdateComponent,
        resolve: {
            fixedAssetAllocation: FixedAssetAllocationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetAllocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const fixedAssetAllocationPopupRoute: Routes = [
    {
        path: 'fixed-asset-allocation/:id/delete',
        component: FixedAssetAllocationDeletePopupComponent,
        resolve: {
            fixedAssetAllocation: FixedAssetAllocationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.fixedAssetAllocation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
