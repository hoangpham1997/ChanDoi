import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { HoaDonDieuChinhComponent } from 'app/hoa-don-dien-tu/hoa_don_dieu_chinh/hoa-don-dieu-chinh';

export const HoaDonDieuChinhRoute: Routes = [
    {
        path: '',
        component: HoaDonDieuChinhComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.eInvoice.home.title.listInvoice'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const HoaDonDieuChinhPopupRoute: Routes = [];
