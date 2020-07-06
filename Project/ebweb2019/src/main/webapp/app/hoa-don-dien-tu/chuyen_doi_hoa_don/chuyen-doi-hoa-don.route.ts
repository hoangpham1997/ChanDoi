import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { ChuyenDoiHoaDonComponent } from 'app/hoa-don-dien-tu/chuyen_doi_hoa_don/chuyen-doi-hoa-don';
import { ROLE } from 'app/role.constants';

export const ChuyenDoiHoaDonRoute: Routes = [
    {
        path: '',
        component: ChuyenDoiHoaDonComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.CDHD],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.eInvoice.home.title.listInvoice'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ChuyenDoiHoaDonPopupRoute: Routes = [];
