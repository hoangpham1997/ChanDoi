import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PrepaidExpenseAllocation } from 'app/shared/model/prepaid-expense-allocation.model';
import { PrepaidExpenseAllocationService } from './prepaid-expense-allocation.service';
import { PrepaidExpenseAllocationComponent } from './prepaid-expense-allocation.component';
import { PrepaidExpenseAllocationDetailComponent } from './prepaid-expense-allocation-detail.component';
import { PrepaidExpenseAllocationUpdateComponent } from './prepaid-expense-allocation-update.component';
import { PrepaidExpenseAllocationDeletePopupComponent } from './prepaid-expense-allocation-delete-dialog.component';
import { IPrepaidExpenseAllocation } from 'app/shared/model/prepaid-expense-allocation.model';

@Injectable({ providedIn: 'root' })
export class PrepaidExpenseAllocationResolve implements Resolve<IPrepaidExpenseAllocation> {
    constructor(private service: PrepaidExpenseAllocationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((prepaidExpenseAllocation: HttpResponse<PrepaidExpenseAllocation>) => prepaidExpenseAllocation.body));
        }
        return of(new PrepaidExpenseAllocation());
    }
}

export const prepaidExpenseAllocationRoute: Routes = [
    {
        path: 'prepaid-expense-allocation',
        component: PrepaidExpenseAllocationComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.prepaidExpenseAllocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'prepaid-expense-allocation/:id/view',
        component: PrepaidExpenseAllocationDetailComponent,
        resolve: {
            prepaidExpenseAllocation: PrepaidExpenseAllocationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.prepaidExpenseAllocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'prepaid-expense-allocation/new',
        component: PrepaidExpenseAllocationUpdateComponent,
        resolve: {
            prepaidExpenseAllocation: PrepaidExpenseAllocationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.prepaidExpenseAllocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'prepaid-expense-allocation/:id/edit',
        component: PrepaidExpenseAllocationUpdateComponent,
        resolve: {
            prepaidExpenseAllocation: PrepaidExpenseAllocationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.prepaidExpenseAllocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const prepaidExpenseAllocationPopupRoute: Routes = [
    {
        path: 'prepaid-expense-allocation/:id/delete',
        component: PrepaidExpenseAllocationDeletePopupComponent,
        resolve: {
            prepaidExpenseAllocation: PrepaidExpenseAllocationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.prepaidExpenseAllocation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
