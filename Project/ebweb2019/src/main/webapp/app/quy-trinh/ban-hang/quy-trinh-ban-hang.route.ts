import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { QuyTrinhBanHangComponent } from './quy-trinh-ban-hang.component';

export const quyTrinhBanHangRoute: Routes = [
    {
        path: '',
        component: QuyTrinhBanHangComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.quyTrinh.sell'
        },
        canActivate: [UserRouteAccessService]
    }
];
