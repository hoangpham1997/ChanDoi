import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from './material-goods.service';
import { MaterialGoodsComponent } from './material-goods.component';
import { MaterialGoodsDetailComponent } from './material-goods-detail.component';
import { MaterialGoodsUpdateComponent } from './material-goods-update.component';
import { MaterialGoodsDeletePopupComponent } from './material-goods-delete-dialog.component';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class MaterialGoodsResolve implements Resolve<IMaterialGoods> {
    constructor(private service: MaterialGoodsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((materialGoods: HttpResponse<MaterialGoods>) => materialGoods.body));
        }
        return of(new MaterialGoods());
    }
}

export const materialGoodsRoute: Routes = [
    {
        path: '',
        component: MaterialGoodsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucVatTuHangHoa_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.materialGoods.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    // {
    //     path: ':id/view',
    //     component: MaterialGoodsDetailComponent,
    //     resolve: {
    //         materialGoods: MaterialGoodsResolve
    //     },
    //     data: {
    //         authorities: ['ROLE_ADMIN'],
    //         pageTitle: 'ebwebApp.materialGoods.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // },
    {
        path: 'new',
        component: MaterialGoodsUpdateComponent,
        resolve: {
            materialGoods: MaterialGoodsResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucVatTuHangHoa_Them],
            pageTitle: 'ebwebApp.materialGoods.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: MaterialGoodsUpdateComponent,
        resolve: {
            materialGoods: MaterialGoodsResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucVatTuHangHoa_Sua],
            pageTitle: 'ebwebApp.materialGoods.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const materialGoodsPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MaterialGoodsDeletePopupComponent,
        resolve: {
            materialGoods: MaterialGoodsResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucVatTuHangHoa_Xoa],
            pageTitle: 'ebwebApp.materialGoods.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
