import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { QuyTrinhKhoComponent } from 'app/quy-trinh/kho/kho.component';
import { QuyTrinhGiaThanhComponent } from './quy-trinh-gia-thanh.component';

export const quyTrinhGiaThanhRoute: Routes = [
    {
        path: '',
        component: QuyTrinhGiaThanhComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.quyTrinh.price'
        },
        canActivate: [UserRouteAccessService]
    }
];
