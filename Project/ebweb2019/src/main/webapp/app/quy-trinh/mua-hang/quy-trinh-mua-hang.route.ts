import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { QuyTrinhKhoComponent } from 'app/quy-trinh/kho/kho.component';
import { QuyTrinhMuaHangComponent } from 'app/quy-trinh/mua-hang/quy-trinh-mua-hang.component';

export const quyTrinhMuaHangRoute: Routes = [
    {
        path: '',
        component: QuyTrinhMuaHangComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.quyTrinh.purchase'
        },
        canActivate: [UserRouteAccessService]
    }
];
