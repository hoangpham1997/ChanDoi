import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MaterialGoodsPurchasePrice } from 'app/shared/model/material-goods-purchase-price.model';
import { MaterialGoodsPurchasePriceService } from './material-goods-purchase-price.service';
import { MaterialGoodsPurchasePriceComponent } from './material-goods-purchase-price.component';
import { MaterialGoodsPurchasePriceDetailComponent } from './material-goods-purchase-price-detail.component';
import { MaterialGoodsPurchasePriceUpdateComponent } from './material-goods-purchase-price-update.component';
import { MaterialGoodsPurchasePriceDeletePopupComponent } from './material-goods-purchase-price-delete-dialog.component';
import { IMaterialGoodsPurchasePrice } from 'app/shared/model/material-goods-purchase-price.model';

@Injectable({ providedIn: 'root' })
export class MaterialGoodsPurchasePriceResolve implements Resolve<IMaterialGoodsPurchasePrice> {
    constructor(private service: MaterialGoodsPurchasePriceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((materialGoodsPurchasePrice: HttpResponse<MaterialGoodsPurchasePrice>) => materialGoodsPurchasePrice.body));
        }
        return of(new MaterialGoodsPurchasePrice());
    }
}

export const materialGoodsPurchasePriceRoute: Routes = [
    {
        path: 'material-goods-purchase-price',
        component: MaterialGoodsPurchasePriceComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.materialGoodsPurchasePrice.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-goods-purchase-price/:id/view',
        component: MaterialGoodsPurchasePriceDetailComponent,
        resolve: {
            materialGoodsPurchasePrice: MaterialGoodsPurchasePriceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsPurchasePrice.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-goods-purchase-price/new',
        component: MaterialGoodsPurchasePriceUpdateComponent,
        resolve: {
            materialGoodsPurchasePrice: MaterialGoodsPurchasePriceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsPurchasePrice.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-goods-purchase-price/:id/edit',
        component: MaterialGoodsPurchasePriceUpdateComponent,
        resolve: {
            materialGoodsPurchasePrice: MaterialGoodsPurchasePriceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsPurchasePrice.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const materialGoodsPurchasePricePopupRoute: Routes = [
    {
        path: 'material-goods-purchase-price/:id/delete',
        component: MaterialGoodsPurchasePriceDeletePopupComponent,
        resolve: {
            materialGoodsPurchasePrice: MaterialGoodsPurchasePriceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialGoodsPurchasePrice.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
