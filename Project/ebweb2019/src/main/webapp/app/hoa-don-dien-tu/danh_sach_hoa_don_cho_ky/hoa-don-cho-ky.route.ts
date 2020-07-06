import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { HoaDonChoKyComponent } from 'app/hoa-don-dien-tu/danh_sach_hoa_don_cho_ky/hoa-don-cho-ky';
import { ROLE } from 'app/role.constants';

export const HoaDonChoKyRoute: Routes = [
    {
        path: '',
        component: HoaDonChoKyComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DSHD_CHO_KY],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.eInvoice.home.title.listInvoice'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const HoaDonChoKyPopupRoute: Routes = [];
