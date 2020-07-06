import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PrepaidExpense } from 'app/shared/model/prepaid-expense.model';
import { PrepaidExpenseService } from './prepaid-expense.service';
import { PrepaidExpenseComponent } from './prepaid-expense.component';
import { PrepaidExpenseDetailComponent } from './prepaid-expense-detail.component';
import { PrepaidExpenseUpdateComponent } from './prepaid-expense-update.component';
import { PrepaidExpenseDeletePopupComponent } from './prepaid-expense-delete-dialog.component';
import { IPrepaidExpense } from 'app/shared/model/prepaid-expense.model';

@Injectable({ providedIn: 'root' })
export class PrepaidExpenseResolve implements Resolve<IPrepaidExpense> {
    constructor(private service: PrepaidExpenseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((prepaidExpense: HttpResponse<PrepaidExpense>) => prepaidExpense.body));
        }
        return of(new PrepaidExpense());
    }
}

export const prepaidExpenseRoute: Routes = [
    {
        path: 'prepaid-expense',
        component: PrepaidExpenseComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.prepaidExpense.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'prepaid-expense/:id/view',
        component: PrepaidExpenseDetailComponent,
        resolve: {
            prepaidExpense: PrepaidExpenseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.prepaidExpense.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'prepaid-expense/new',
        component: PrepaidExpenseUpdateComponent,
        resolve: {
            prepaidExpense: PrepaidExpenseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.prepaidExpense.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'prepaid-expense/:id/edit',
        component: PrepaidExpenseUpdateComponent,
        resolve: {
            prepaidExpense: PrepaidExpenseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.prepaidExpense.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const prepaidExpensePopupRoute: Routes = [
    {
        path: 'prepaid-expense/:id/delete',
        component: PrepaidExpenseDeletePopupComponent,
        resolve: {
            prepaidExpense: PrepaidExpenseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.prepaidExpense.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
