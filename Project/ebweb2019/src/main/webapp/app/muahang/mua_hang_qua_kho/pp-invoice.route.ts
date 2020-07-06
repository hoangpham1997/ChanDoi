import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PPInvoice } from 'app/shared/model/pp-invoice.model';
import { PPInvoiceService } from './pp-invoice.service';
import { PPInvoiceComponent } from './pp-invoice.component';
import { PPInvoiceDetailComponent } from './pp-invoice-detail.component';
import { PPInvoiceUpdateComponent } from './pp-invoice-update.component';
import { PPInvoiceDeletePopupComponent } from './pp-invoice-delete-dialog.component';
import { IPPInvoice } from 'app/shared/model/pp-invoice.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { PPINVOICE_COMPONENT_TYPE } from 'app/app.constants';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class PPInvoiceResolve implements Resolve<any> {
    constructor(private service: PPInvoiceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.findById({ id }).pipe(map((pPInvoice: HttpResponse<any>) => pPInvoice.body));
        }
        return of(new PPInvoice());
    }
}

export const pPInvoiceRoute: Routes = [
    {
        path: 'qua-kho',
        component: PPInvoiceComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangQuaKho_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.pPInvoice.home.titleRSI',
            componentType: PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'khong-qua-kho',
        component: PPInvoiceComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangKhongQuaKho_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.pPInvoice.home.title',
            componentType: PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: PPInvoiceDetailComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ebwebApp.pPInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'qua-kho/new',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangQuaKho_Them],
            pageTitle: 'ebwebApp.pPInvoice.home.titleRSI',
            componentType: PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'khong-qua-kho/new',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangKhongQuaKho_Them],
            pageTitle: 'ebwebApp.pPInvoice.home.title',
            componentType: PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'qua-kho/:id/edit/:rowNum',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangQuaKho_Xem],
            pageTitle: 'ebwebApp.pPInvoice.home.titleRSI',
            componentType: PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'qua-kho/:id/edit',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangQuaKho_Xem],
            pageTitle: 'ebwebApp.pPInvoice.home.titleRSI',
            componentType: PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'qua-kho-ref/:id/edit/:rowNum',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangQuaKho_Xem],
            pageTitle: 'ebwebApp.pPInvoice.home.titleRSI',
            componentType: PPINVOICE_COMPONENT_TYPE.REF_QUA_KHO
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'khong-qua-kho/:id/edit/:rowNum',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangKhongQuaKho_Xem],
            pageTitle: 'ebwebApp.pPInvoice.home.title',
            componentType: PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'khong-qua-kho/:id/edit',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangKhongQuaKho_Xem],
            pageTitle: 'ebwebApp.pPInvoice.home.title',
            componentType: PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'khong-qua-kho-ref/:id/edit/:rowNum',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangKhongQuaKho_Xem],
            pageTitle: 'ebwebApp.pPInvoice.home.title',
            componentType: PPINVOICE_COMPONENT_TYPE.REF_KHONG_QUA_KHO
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'qua-kho/:id/edit-rs-inward',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangQuaKho_Xem],
            pageTitle: 'ebwebApp.pPInvoice.home.titleRSI'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/receiveBill',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ebwebApp.pPInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'khong-qua-kho/:id/edit/from-mc-payment/:mcpaymentID',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangKhongQuaKho_Xem],
            pageTitle: 'ebwebApp.pPInvoice.home.title',
            componentType: PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'qua-kho/:id/edit/from-mc-payment/:mcpaymentID',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangQuaKho_Xem],
            pageTitle: 'ebwebApp.pPInvoice.home.titleRSI',
            componentType: PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'khong-qua-kho/:id/edit/from-mb-credit-card/:mBCreditCardID',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangKhongQuaKho_Xem],
            pageTitle: 'ebwebApp.pPInvoice.home.title',
            componentType: PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'qua-kho/:id/edit/from-mb-credit-card/:mBCreditCardID',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangQuaKho_Xem],
            pageTitle: 'ebwebApp.pPInvoice.home.titleRSI',
            componentType: PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'khong-qua-kho/:id/edit/from-mb-teller-paper/:mBTellerPaperID',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangKhongQuaKho_Xem],
            pageTitle: 'ebwebApp.pPInvoice.home.title',
            componentType: PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'qua-kho/:id/edit/from-mb-teller-paper/:mBTellerPaperID',
        component: PPInvoiceUpdateComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.MuaHangQuaKho_Xem],
            pageTitle: 'ebwebApp.pPInvoice.home.titleRSI',
            componentType: PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const pPInvoicePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: PPInvoiceDeletePopupComponent,
        resolve: {
            pPInvoice: PPInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ebwebApp.pPInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
