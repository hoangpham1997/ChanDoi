import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { GOtherVoucherDetailExpense } from 'app/shared/model/g-other-voucher-detail-expense.model';
import { GOtherVoucherDetailExpenseService } from './g-other-voucher-detail-expense.service';
import { GOtherVoucherDetailExpenseComponent } from './g-other-voucher-detail-expense.component';
import { GOtherVoucherDetailExpenseDetailComponent } from './g-other-voucher-detail-expense-detail.component';
import { GOtherVoucherDetailExpenseUpdateComponent } from './g-other-voucher-detail-expense-update.component';
import { GOtherVoucherDetailExpenseDeletePopupComponent } from './g-other-voucher-detail-expense-delete-dialog.component';
import { IGOtherVoucherDetailExpense } from 'app/shared/model/g-other-voucher-detail-expense.model';

@Injectable({ providedIn: 'root' })
export class GOtherVoucherDetailExpenseResolve implements Resolve<IGOtherVoucherDetailExpense> {
    constructor(private service: GOtherVoucherDetailExpenseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((gOtherVoucherDetailExpense: HttpResponse<GOtherVoucherDetailExpense>) => gOtherVoucherDetailExpense.body));
        }
        return of(new GOtherVoucherDetailExpense());
    }
}

export const gOtherVoucherDetailExpenseRoute: Routes = [
    {
        path: 'g-other-voucher-detail-expense',
        component: GOtherVoucherDetailExpenseComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.gOtherVoucherDetailExpense.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'g-other-voucher-detail-expense/:id/view',
        component: GOtherVoucherDetailExpenseDetailComponent,
        resolve: {
            gOtherVoucherDetailExpense: GOtherVoucherDetailExpenseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.gOtherVoucherDetailExpense.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'g-other-voucher-detail-expense/new',
        component: GOtherVoucherDetailExpenseUpdateComponent,
        resolve: {
            gOtherVoucherDetailExpense: GOtherVoucherDetailExpenseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.gOtherVoucherDetailExpense.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'g-other-voucher-detail-expense/:id/edit',
        component: GOtherVoucherDetailExpenseUpdateComponent,
        resolve: {
            gOtherVoucherDetailExpense: GOtherVoucherDetailExpenseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.gOtherVoucherDetailExpense.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const gOtherVoucherDetailExpensePopupRoute: Routes = [
    {
        path: 'g-other-voucher-detail-expense/:id/delete',
        component: GOtherVoucherDetailExpenseDeletePopupComponent,
        resolve: {
            gOtherVoucherDetailExpense: GOtherVoucherDetailExpenseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.gOtherVoucherDetailExpense.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
