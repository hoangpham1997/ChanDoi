import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SAInvoice } from 'app/shared/model/sa-invoice.model';
import { SAInvoiceService } from './sa-invoice.service';
import { SAInvoiceComponent } from './sa-invoice.component';
import { SAInvoiceUpdateComponent } from './sa-invoice-update.component';
import { SAInvoiceDeletePopupComponent } from './sa-invoice-delete-dialog.component';
import { ISAInvoice } from 'app/shared/model/sa-invoice.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class SAInvoiceResolve implements Resolve<ISAInvoice> {
    constructor(private service: SAInvoiceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((sAInvoice: HttpResponse<SAInvoice>) => sAInvoice.body));
        }
        return of(new SAInvoice());
    }
}

export const sAInvoiceRoute: Routes = [
    {
        path: '',
        component: SAInvoiceComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChungTuBanHang_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.sAInvoice.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit-rs-inward-outward',
        component: SAInvoiceUpdateComponent,
        resolve: {
            sAInvoice: SAInvoiceResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChungTuBanHang_Xem],
            pageTitle: 'ebwebApp.sAInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'new',
        component: SAInvoiceUpdateComponent,
        resolve: {
            sAInvoice: SAInvoiceResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChungTuBanHang_Them],
            pageTitle: 'ebwebApp.sAInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: SAInvoiceUpdateComponent,
        resolve: {
            sAInvoice: SAInvoiceResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChungTuBanHang_Sua],
            pageTitle: 'ebwebApp.sAInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/:rowNum',
        component: SAInvoiceUpdateComponent,
        resolve: {
            sAInvoice: SAInvoiceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.sAInvoice.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit/from-ref',
        component: SAInvoiceUpdateComponent,
        resolve: {
            sAInvoice: SAInvoiceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.sAInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-mc-receipt/:mCReceiptID',
        component: SAInvoiceUpdateComponent,
        resolve: {
            sAInvoice: SAInvoiceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.sAInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-mb-deposit/:mBDepositID',
        component: SAInvoiceUpdateComponent,
        resolve: {
            sAInvoice: SAInvoiceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.sAInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-einvoice',
        component: SAInvoiceUpdateComponent,
        resolve: {
            sAInvoice: SAInvoiceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.sAInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-einvoice',
        component: SAInvoiceUpdateComponent,
        resolve: {
            sAInvoice: SAInvoiceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.sAInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const sAInvoicePopupRoute: Routes = [
    {
        path: '/:id/delete',
        component: SAInvoiceDeletePopupComponent,
        resolve: {
            sAInvoice: SAInvoiceResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChungTuBanHang_Xoa],
            pageTitle: 'ebwebApp.sAInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
