import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SalePriceGroup } from 'app/shared/model/sale-price-group.model';
import { SalePriceGroupService } from './sale-price-group.service';
import { SalePriceGroupComponent } from './sale-price-group.component';
import { SalePriceGroupDetailComponent } from './sale-price-group-detail.component';
import { SalePriceGroupUpdateComponent } from './sale-price-group-update.component';
import { SalePriceGroupDeletePopupComponent } from './sale-price-group-delete-dialog.component';
import { ISalePriceGroup } from 'app/shared/model/sale-price-group.model';

@Injectable({ providedIn: 'root' })
export class SalePriceGroupResolve implements Resolve<ISalePriceGroup> {
    constructor(private service: SalePriceGroupService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((salePriceGroup: HttpResponse<SalePriceGroup>) => salePriceGroup.body));
        }
        return of(new SalePriceGroup());
    }
}

export const salePriceGroupRoute: Routes = [
    {
        path: 'sale-price-group',
        component: SalePriceGroupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.salePriceGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sale-price-group/:id/view',
        component: SalePriceGroupDetailComponent,
        resolve: {
            salePriceGroup: SalePriceGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.salePriceGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sale-price-group/new',
        component: SalePriceGroupUpdateComponent,
        resolve: {
            salePriceGroup: SalePriceGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.salePriceGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sale-price-group/:id/edit',
        component: SalePriceGroupUpdateComponent,
        resolve: {
            salePriceGroup: SalePriceGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.salePriceGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const salePriceGroupPopupRoute: Routes = [
    {
        path: 'sale-price-group/:id/delete',
        component: SalePriceGroupDeletePopupComponent,
        resolve: {
            salePriceGroup: SalePriceGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.salePriceGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
