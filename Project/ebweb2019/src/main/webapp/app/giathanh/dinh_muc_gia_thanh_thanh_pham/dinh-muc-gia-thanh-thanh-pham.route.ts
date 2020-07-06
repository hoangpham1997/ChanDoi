import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { DinhMucGiaThanhThanhPhamComponent } from './dinh-muc-gia-thanh-thanh-pham.component';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

export const dinhMucGiaThanhThanhPhamRoute: Routes = [
    {
        path: '',
        component: DinhMucGiaThanhThanhPhamComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DinhMucGiaThanhThanhPham_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.dinhMucGiaThanhThanhPham.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const DinhMucGiaThanhThanhPhamPopupRoute: Routes = [];
