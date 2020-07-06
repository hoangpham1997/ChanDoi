import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { MaterialGoodsCategoryService } from './material-goods-category.service';
import { MaterialGoodsCategoryComponent } from './material-goods-category.component';
import { MaterialGoodsCategoryDetailComponent } from './material-goods-category-detail.component';
import { MaterialGoodsCategoryUpdateComponent } from './material-goods-category-update.component';
import { MaterialGoodsCategoryDeletePopupComponent } from './material-goods-category-delete-dialog.component';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class MaterialGoodsCategoryResolve implements Resolve<IMaterialGoodsCategory> {
    constructor(private service: MaterialGoodsCategoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((materialGoodsCategory: HttpResponse<MaterialGoodsCategory>) => materialGoodsCategory.body));
        }
        return of(new MaterialGoodsCategory());
    }
}

export const materialGoodsCategoryRoute: Routes = [
    {
        path: '',
        component: MaterialGoodsCategoryComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucLoaiVatTuHangHoa_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.materialGoodsCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: MaterialGoodsCategoryUpdateComponent,
        resolve: {
            materialGoodsCategory: MaterialGoodsCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucLoaiVatTuHangHoa_Them],
            pageTitle: 'ebwebApp.materialGoodsCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: MaterialGoodsCategoryUpdateComponent,
        resolve: {
            materialGoodsCategory: MaterialGoodsCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucLoaiVatTuHangHoa_Sua],
            pageTitle: 'ebwebApp.materialGoodsCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const materialGoodsCategoryPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MaterialGoodsCategoryDeletePopupComponent,
        resolve: {
            materialGoodsCategory: MaterialGoodsCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucLoaiVatTuHangHoa_Xoa],
            pageTitle: 'ebwebApp.materialGoodsCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
