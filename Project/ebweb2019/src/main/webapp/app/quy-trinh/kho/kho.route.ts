import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { QuyTrinhKhoComponent } from 'app/quy-trinh/kho/kho.component';

export const khoRoute: Routes = [
    {
        path: '',
        component: QuyTrinhKhoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.quyTrinh.repository'
        },
        canActivate: [UserRouteAccessService]
    }
];
