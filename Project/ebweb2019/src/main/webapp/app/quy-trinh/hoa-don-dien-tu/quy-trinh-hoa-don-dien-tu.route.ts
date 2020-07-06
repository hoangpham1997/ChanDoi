import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { QuyTrinhHoaDonDienTuComponent } from 'app/quy-trinh/hoa-don-dien-tu/quy-trinh-hoa-don-dien-tu.component';

export const quyTrinhHoaDonDienTuRoute: Routes = [
    {
        path: '',
        component: QuyTrinhHoaDonDienTuComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.quyTrinh.eInvoice'
        },
        canActivate: [UserRouteAccessService]
    }
];
