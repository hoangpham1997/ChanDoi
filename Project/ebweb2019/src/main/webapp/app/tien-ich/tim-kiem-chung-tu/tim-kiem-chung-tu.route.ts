import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { TimKiemChungTuComponent } from 'app/tien-ich/tim-kiem-chung-tu/tim-kiem-chung-tu.component';

export const TimKiemChungTuRoute: Routes = [
    {
        path: '',
        component: TimKiemChungTuComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.tienIch.timKiemChungTu.name'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const TimKiemChungTuRoutePopupRoute: Routes = [];
