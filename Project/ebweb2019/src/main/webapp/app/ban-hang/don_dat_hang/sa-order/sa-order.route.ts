import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SAOrder } from 'app/shared/model/sa-order.model';
import { SAOrderService } from './sa-order.service';
import { SAOrderComponent } from './sa-order.component';
import { SAOrderDetailComponent } from './sa-order-detail.component';
import { SAOrderUpdateComponent } from './sa-order-update.component';
import { SAOrderDeletePopupComponent } from './sa-order-delete-dialog.component';
import { ISAOrder } from 'app/shared/model/sa-order.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class SAOrderResolve implements Resolve<ISAOrder> {
    constructor(private service: SAOrderService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((sAOrder: HttpResponse<SAOrder>) => sAOrder.body));
        }
        return of(new SAOrder());
    }
}

export const sAOrderRoute: Routes = [
    {
        path: '',
        component: SAOrderComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DonDatHang_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.sAOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'hasSearch/:isSearch',
        component: SAOrderComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DonDatHang_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.sAOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SAOrderDetailComponent,
        resolve: {
            sAOrder: SAOrderResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DonDatHang_Xem],
            pageTitle: 'ebwebApp.sAOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: SAOrderUpdateComponent,
        resolve: {
            sAOrder: SAOrderResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DonDatHang_Xem],
            pageTitle: 'ebwebApp.sAOrder.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: SAOrderUpdateComponent,
        resolve: {
            sAOrder: SAOrderResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DonDatHang_Xem],
            pageTitle: 'ebwebApp.sAOrder.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: SAOrderUpdateComponent,
        resolve: {
            sAOrder: SAOrderResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DonDatHang_Xem],
            pageTitle: 'ebwebApp.sAOrder.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const sAOrderPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SAOrderDeletePopupComponent,
        resolve: {
            sAOrder: SAOrderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.sAOrder.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
