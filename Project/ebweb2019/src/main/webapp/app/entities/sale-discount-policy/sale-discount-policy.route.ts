import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';
import { SaleDiscountPolicyService } from './sale-discount-policy.service';
import { SaleDiscountPolicyComponent } from './sale-discount-policy.component';
import { SaleDiscountPolicyDetailComponent } from './sale-discount-policy-detail.component';
import { SaleDiscountPolicyUpdateComponent } from './sale-discount-policy-update.component';
import { SaleDiscountPolicyDeletePopupComponent } from './sale-discount-policy-delete-dialog.component';
import { ISaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';

@Injectable({ providedIn: 'root' })
export class SaleDiscountPolicyResolve implements Resolve<ISaleDiscountPolicy> {
    constructor(private service: SaleDiscountPolicyService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((saleDiscountPolicy: HttpResponse<SaleDiscountPolicy>) => saleDiscountPolicy.body));
        }
        return of(new SaleDiscountPolicy());
    }
}

export const saleDiscountPolicyRoute: Routes = [
    {
        path: 'sale-discount-policy',
        component: SaleDiscountPolicyComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.saleDiscountPolicy.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sale-discount-policy/:id/view',
        component: SaleDiscountPolicyDetailComponent,
        resolve: {
            saleDiscountPolicy: SaleDiscountPolicyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.saleDiscountPolicy.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sale-discount-policy/new',
        component: SaleDiscountPolicyUpdateComponent,
        resolve: {
            saleDiscountPolicy: SaleDiscountPolicyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.saleDiscountPolicy.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sale-discount-policy/:id/edit',
        component: SaleDiscountPolicyUpdateComponent,
        resolve: {
            saleDiscountPolicy: SaleDiscountPolicyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.saleDiscountPolicy.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const saleDiscountPolicyPopupRoute: Routes = [
    {
        path: 'sale-discount-policy/:id/delete',
        component: SaleDiscountPolicyDeletePopupComponent,
        resolve: {
            saleDiscountPolicy: SaleDiscountPolicyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.saleDiscountPolicy.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
