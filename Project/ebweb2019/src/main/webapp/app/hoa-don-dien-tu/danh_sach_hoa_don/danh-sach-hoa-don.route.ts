import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { DanhSachHoaDonComponent } from 'app/hoa-don-dien-tu/danh_sach_hoa_don/danh-sach-hoa-don';
import { ROLE } from 'app/role.constants';

export const DanhSachHoaDonRoute: Routes = [
    {
        path: '',
        component: DanhSachHoaDonComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DSHD],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.eInvoice.home.title.listInvoice'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const DanhSachHoaDonPopupRoute: Routes = [];
