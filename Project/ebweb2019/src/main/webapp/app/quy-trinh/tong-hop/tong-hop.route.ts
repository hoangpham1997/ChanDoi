import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { QuyTrinhTongHopComponent } from 'app/quy-trinh/tong-hop/tong-hop.component';

export const tongHopRoute: Routes = [
    {
        path: '',
        component: QuyTrinhTongHopComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.quyTrinh.general'
        },
        canActivate: [UserRouteAccessService]
    }
];
