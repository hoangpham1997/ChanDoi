import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MaterialGoodsSpecialTaxGroup } from 'app/shared/model/material-goods-special-tax-group.model';
import { MaterialGoodsSpecialTaxGroupService } from './material-goods-special-tax-group.service';
import { MaterialGoodsSpecialTaxGroupComponent } from './material-goods-special-tax-group.component';
import { MaterialGoodsSpecialTaxGroupDetailComponent } from './material-goods-special-tax-group-detail.component';
import { MaterialGoodsSpecialTaxGroupUpdateComponent } from './material-goods-special-tax-group-update.component';
import { MaterialGoodsSpecialTaxGroupDeletePopupComponent } from './material-goods-special-tax-group-delete-dialog.component';
import { IMaterialGoodsSpecialTaxGroup } from 'app/shared/model/material-goods-special-tax-group.model';
import { ROLE } from 'app/role.constants';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';

@Injectable({ providedIn: 'root' })
export class MaterialGoodsSpecialTaxGroupResolve implements Resolve<IMaterialGoodsSpecialTaxGroup> {
    constructor(private service: MaterialGoodsSpecialTaxGroupService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((materialGoodsSpecialTaxGroup: HttpResponse<MaterialGoodsSpecialTaxGroup>) => materialGoodsSpecialTaxGroup.body));
        }
        return of(new MaterialGoodsSpecialTaxGroup());
    }
}

export const materialGoodsSpecialTaxGroupRoute: Routes = [
    {
        path: '',
        component: MaterialGoodsSpecialTaxGroupComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucHHDVChiuThueTTDB_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.materialGoodsSpecialTaxGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    // {
    //     path: ':id/view',
    //     component: MaterialGoodsSpecialTaxGroupDetailComponent,
    //     resolve: {
    //         materialGoodsSpecialTaxGroup: MaterialGoodsSpecialTaxGroupResolve
    //     },
    //     data: {
    //         authorities: ['ROLE_ADMIN', ROLE.DanhMucHHDVChiuThueTTDB_Xem],
    //         pageTitle: 'ebwebApp.materialGoodsSpecialTaxGroup.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // },
    {
        path: 'new',
        component: MaterialGoodsSpecialTaxGroupUpdateComponent,
        resolve: {
            materialGoodsSpecialTaxGroup: MaterialGoodsSpecialTaxGroupResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucHHDVChiuThueTTDB_Them],
            pageTitle: 'ebwebApp.materialGoodsSpecialTaxGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: MaterialGoodsSpecialTaxGroupUpdateComponent,
        resolve: {
            materialGoodsSpecialTaxGroup: MaterialGoodsSpecialTaxGroupResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucHHDVChiuThueTTDB_Sua],
            pageTitle: 'ebwebApp.materialGoodsSpecialTaxGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const materialGoodsSpecialTaxGroupPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MaterialGoodsSpecialTaxGroupDeletePopupComponent,
        resolve: {
            materialGoodsSpecialTaxGroup: MaterialGoodsSpecialTaxGroupResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucHHDVChiuThueTTDB_Xoa],
            pageTitle: 'ebwebApp.materialGoodsSpecialTaxGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
