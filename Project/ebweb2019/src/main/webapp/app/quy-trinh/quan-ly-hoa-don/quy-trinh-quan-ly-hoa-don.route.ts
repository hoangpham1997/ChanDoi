import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { QuyTrinhQuanLyHoaDonComponent } from 'app/quy-trinh/quan-ly-hoa-don/quy-trinh-quan-ly-hoa-don.component';

export const quyTrinhQuanLyHoaDonRoute: Routes = [
    {
        path: '',
        component: QuyTrinhQuanLyHoaDonComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.quyTrinh.mNInvoice'
        },
        canActivate: [UserRouteAccessService]
    }
];
