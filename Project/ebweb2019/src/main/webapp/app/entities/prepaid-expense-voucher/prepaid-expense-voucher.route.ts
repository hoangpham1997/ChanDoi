import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PrepaidExpenseVoucher } from 'app/shared/model/prepaid-expense-voucher.model';
import { PrepaidExpenseVoucherService } from './prepaid-expense-voucher.service';
import { PrepaidExpenseVoucherComponent } from './prepaid-expense-voucher.component';
import { PrepaidExpenseVoucherDetailComponent } from './prepaid-expense-voucher-detail.component';
import { PrepaidExpenseVoucherUpdateComponent } from './prepaid-expense-voucher-update.component';
import { PrepaidExpenseVoucherDeletePopupComponent } from './prepaid-expense-voucher-delete-dialog.component';
import { IPrepaidExpenseVoucher } from 'app/shared/model/prepaid-expense-voucher.model';

@Injectable({ providedIn: 'root' })
export class PrepaidExpenseVoucherResolve implements Resolve<IPrepaidExpenseVoucher> {
    constructor(private service: PrepaidExpenseVoucherService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((prepaidExpenseVoucher: HttpResponse<PrepaidExpenseVoucher>) => prepaidExpenseVoucher.body));
        }
        return of(new PrepaidExpenseVoucher());
    }
}

export const prepaidExpenseVoucherRoute: Routes = [
    {
        path: 'prepaid-expense-voucher',
        component: PrepaidExpenseVoucherComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.prepaidExpenseVoucher.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'prepaid-expense-voucher/:id/view',
        component: PrepaidExpenseVoucherDetailComponent,
        resolve: {
            prepaidExpenseVoucher: PrepaidExpenseVoucherResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.prepaidExpenseVoucher.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'prepaid-expense-voucher/new',
        component: PrepaidExpenseVoucherUpdateComponent,
        resolve: {
            prepaidExpenseVoucher: PrepaidExpenseVoucherResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.prepaidExpenseVoucher.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'prepaid-expense-voucher/:id/edit',
        component: PrepaidExpenseVoucherUpdateComponent,
        resolve: {
            prepaidExpenseVoucher: PrepaidExpenseVoucherResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.prepaidExpenseVoucher.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const prepaidExpenseVoucherPopupRoute: Routes = [
    {
        path: 'prepaid-expense-voucher/:id/delete',
        component: PrepaidExpenseVoucherDeletePopupComponent,
        resolve: {
            prepaidExpenseVoucher: PrepaidExpenseVoucherResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.prepaidExpenseVoucher.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
