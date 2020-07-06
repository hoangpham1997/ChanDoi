import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { NhanHoaDonComponent } from './nhan-hoa-don.component';
import { MBDepositResolve, MBDepositUpdateComponent } from 'app/TienMatNganHang/BaoCo/mb-deposit';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { PPInvoiceResolve, PPInvoiceUpdateComponent } from 'app/muahang/mua_hang_qua_kho';
import { ROLE } from 'app/role.constants';

export const nhanHoaDonRoute: Routes = [
    {
        path: '',
        component: NhanHoaDonComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.NhanHoaDon_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.receiveBill.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const NhanHoaDonPopupRoute: Routes = [];
