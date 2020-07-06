import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { DangKySuDungComponent } from './dang-ky-su-dung.component';
import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { of } from 'rxjs';
import { DangKySuDungUpdateComponent } from 'app/quan-ly-hoa-don/dang-ky-su-dung/dang-ky-su-dung-update.component';
import { IARegisterInvoiceService } from 'app/quan-ly-hoa-don/dang-ky-su-dung/ia-register-invoice.service';
import { IARegisterInvoice } from 'app/shared/model/ia-register-invoice.model';
import { DangKySuDungDeletePopupComponent } from 'app/quan-ly-hoa-don/dang-ky-su-dung/dang-ky-su-dung-delete-dialog.component';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class IARegisterInvoiceResolve implements Resolve<IARegisterInvoice> {
    constructor(private service: IARegisterInvoiceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((mBTellerPaper: HttpResponse<IARegisterInvoice>) => mBTellerPaper.body));
        }
        return of(new IARegisterInvoice());
    }
}

export const DangKySuDungRoute: Routes = [
    {
        path: '',
        component: DangKySuDungComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DKSD_XEM],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.iARegisterInvoice.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DangKySuDungUpdateComponent,
        resolve: {
            iaRegisterInvoice: IARegisterInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DKSD_THEM],
            pageTitle: 'ebwebApp.iARegisterInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: DangKySuDungUpdateComponent,
        resolve: {
            iaRegisterInvoice: IARegisterInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DKSD_XEM],
            pageTitle: 'ebwebApp.iARegisterInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: DangKySuDungUpdateComponent,
        resolve: {
            iaRegisterInvoice: IARegisterInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DKSD_XEM],
            pageTitle: 'ebwebApp.iARegisterInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const DangKySuDungsPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DangKySuDungDeletePopupComponent,
        resolve: {
            iaRegisterInvoice: IARegisterInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DKSD_XEM],
            pageTitle: 'ebwebApp.iARegisterInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
