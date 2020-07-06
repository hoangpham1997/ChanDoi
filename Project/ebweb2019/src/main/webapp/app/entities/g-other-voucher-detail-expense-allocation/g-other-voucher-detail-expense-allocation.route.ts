import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { GOtherVoucherDetailExpenseAllocation } from 'app/shared/model/g-other-voucher-detail-expense-allocation.model';
import { GOtherVoucherDetailExpenseAllocationService } from './g-other-voucher-detail-expense-allocation.service';
import { GOtherVoucherDetailExpenseAllocationComponent } from './g-other-voucher-detail-expense-allocation.component';
import { GOtherVoucherDetailExpenseAllocationDetailComponent } from './g-other-voucher-detail-expense-allocation-detail.component';
import { GOtherVoucherDetailExpenseAllocationUpdateComponent } from './g-other-voucher-detail-expense-allocation-update.component';
import { GOtherVoucherDetailExpenseAllocationDeletePopupComponent } from './g-other-voucher-detail-expense-allocation-delete-dialog.component';
import { IGOtherVoucherDetailExpenseAllocation } from 'app/shared/model/g-other-voucher-detail-expense-allocation.model';

@Injectable({ providedIn: 'root' })
export class GOtherVoucherDetailExpenseAllocationResolve implements Resolve<IGOtherVoucherDetailExpenseAllocation> {
    constructor(private service: GOtherVoucherDetailExpenseAllocationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(
                    map(
                        (gOtherVoucherDetailExpenseAllocation: HttpResponse<GOtherVoucherDetailExpenseAllocation>) =>
                            gOtherVoucherDetailExpenseAllocation.body
                    )
                );
        }
        return of(new GOtherVoucherDetailExpenseAllocation());
    }
}

export const gOtherVoucherDetailExpenseAllocationRoute: Routes = [
    {
        path: 'g-other-voucher-detail-expense-allocation',
        component: GOtherVoucherDetailExpenseAllocationComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.gOtherVoucherDetailExpenseAllocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'g-other-voucher-detail-expense-allocation/:id/view',
        component: GOtherVoucherDetailExpenseAllocationDetailComponent,
        resolve: {
            gOtherVoucherDetailExpenseAllocation: GOtherVoucherDetailExpenseAllocationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.gOtherVoucherDetailExpenseAllocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'g-other-voucher-detail-expense-allocation/new',
        component: GOtherVoucherDetailExpenseAllocationUpdateComponent,
        resolve: {
            gOtherVoucherDetailExpenseAllocation: GOtherVoucherDetailExpenseAllocationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.gOtherVoucherDetailExpenseAllocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'g-other-voucher-detail-expense-allocation/:id/edit',
        component: GOtherVoucherDetailExpenseAllocationUpdateComponent,
        resolve: {
            gOtherVoucherDetailExpenseAllocation: GOtherVoucherDetailExpenseAllocationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.gOtherVoucherDetailExpenseAllocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const gOtherVoucherDetailExpenseAllocationPopupRoute: Routes = [
    {
        path: 'g-other-voucher-detail-expense-allocation/:id/delete',
        component: GOtherVoucherDetailExpenseAllocationDeletePopupComponent,
        resolve: {
            gOtherVoucherDetailExpenseAllocation: GOtherVoucherDetailExpenseAllocationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.gOtherVoucherDetailExpenseAllocation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
