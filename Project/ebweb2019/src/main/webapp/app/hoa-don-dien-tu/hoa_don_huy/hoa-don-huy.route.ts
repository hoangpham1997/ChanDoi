import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { HoaDonHuyComponent } from 'app/hoa-don-dien-tu/hoa_don_huy/hoa-don-huy';

export const HoaDonHuyRoute: Routes = [
    {
        path: '',
        component: HoaDonHuyComponent,
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

export const HoaDonHuyPopupRoute: Routes = [];
