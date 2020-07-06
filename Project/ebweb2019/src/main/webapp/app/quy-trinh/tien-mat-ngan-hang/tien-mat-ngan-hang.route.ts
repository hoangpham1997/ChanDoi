import { Injectable } from '@angular/core';
import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';

import { ROLE } from 'app/role.constants';
import { QuyTrinhTienMatNganHangComponent } from 'app/quy-trinh/tien-mat-ngan-hang/tien-mat-ngan-hang.component';

export const tienMatNganHangRoute: Routes = [
    {
        path: '',
        component: QuyTrinhTienMatNganHangComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.quyTrinh.cashAndBank'
        },
        canActivate: [UserRouteAccessService]
    }
];
