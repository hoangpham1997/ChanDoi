import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { DanhLaiSoChungTuComponent } from 'app/tien-ich/danh-lai-so-chung-tu/danh-lai-so-chung-tu.component';

export const DanhLaiSoChungTuRoute: Routes = [
    {
        path: '',
        component: DanhLaiSoChungTuComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.tienIch.danhLaiSoChungTu.name'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const DanhLaiSoChungTuRoutePopupRoute: Routes = [];
