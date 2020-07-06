import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Warranty } from 'app/shared/model/warranty.model';
import { WarrantyService } from './warranty.service';
import { WarrantyComponent } from './warranty.component';
import { WarrantyDetailComponent } from './warranty-detail.component';
import { WarrantyUpdateComponent } from './warranty-update.component';
import { WarrantyDeletePopupComponent } from './warranty-delete-dialog.component';
import { IWarranty } from 'app/shared/model/warranty.model';

@Injectable({ providedIn: 'root' })
export class WarrantyResolve implements Resolve<IWarranty> {
    constructor(private service: WarrantyService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((warranty: HttpResponse<Warranty>) => warranty.body));
        }
        return of(new Warranty());
    }
}

export const warrantyRoute: Routes = [
    {
        path: 'warranty',
        component: WarrantyComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.warranty.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'warranty/:id/view',
        component: WarrantyDetailComponent,
        resolve: {
            warranty: WarrantyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.warranty.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'warranty/new',
        component: WarrantyUpdateComponent,
        resolve: {
            warranty: WarrantyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.warranty.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'warranty/:id/edit',
        component: WarrantyUpdateComponent,
        resolve: {
            warranty: WarrantyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.warranty.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const warrantyPopupRoute: Routes = [
    {
        path: 'warranty/:id/delete',
        component: WarrantyDeletePopupComponent,
        resolve: {
            warranty: WarrantyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.warranty.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
