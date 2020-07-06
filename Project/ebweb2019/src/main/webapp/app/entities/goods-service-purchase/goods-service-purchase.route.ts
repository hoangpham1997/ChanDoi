import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { GoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from './goods-service-purchase.service';
import { GoodsServicePurchaseComponent } from './goods-service-purchase.component';
import { GoodsServicePurchaseDetailComponent } from './goods-service-purchase-detail.component';
import { GoodsServicePurchaseUpdateComponent } from './goods-service-purchase-update.component';
import { GoodsServicePurchaseDeletePopupComponent } from './goods-service-purchase-delete-dialog.component';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';

@Injectable({ providedIn: 'root' })
export class GoodsServicePurchaseResolve implements Resolve<IGoodsServicePurchase> {
    constructor(private service: GoodsServicePurchaseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((goodsServicePurchase: HttpResponse<GoodsServicePurchase>) => goodsServicePurchase.body));
        }
        return of(new GoodsServicePurchase());
    }
}

export const goodsServicePurchaseRoute: Routes = [
    {
        path: 'goods-service-purchase',
        component: GoodsServicePurchaseComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.goodsServicePurchase.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'goods-service-purchase/:id/view',
        component: GoodsServicePurchaseDetailComponent,
        resolve: {
            goodsServicePurchase: GoodsServicePurchaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.goodsServicePurchase.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'goods-service-purchase/new',
        component: GoodsServicePurchaseUpdateComponent,
        resolve: {
            goodsServicePurchase: GoodsServicePurchaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.goodsServicePurchase.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'goods-service-purchase/:id/edit',
        component: GoodsServicePurchaseUpdateComponent,
        resolve: {
            goodsServicePurchase: GoodsServicePurchaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.goodsServicePurchase.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const goodsServicePurchasePopupRoute: Routes = [
    {
        path: 'goods-service-purchase/:id/delete',
        component: GoodsServicePurchaseDeletePopupComponent,
        resolve: {
            goodsServicePurchase: GoodsServicePurchaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.goodsServicePurchase.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
