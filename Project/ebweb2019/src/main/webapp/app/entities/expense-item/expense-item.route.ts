import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ExpenseItem } from 'app/shared/model/expense-item.model';
import { ExpenseItemService } from './expense-item.service';
import { ExpenseItemComponent } from './expense-item.component';
import { ExpenseItemDetailComponent } from './expense-item-detail.component';
import { ExpenseItemUpdateComponent } from './expense-item-update.component';
import { ExpenseItemDeletePopupComponent } from './expense-item-delete-dialog.component';
import { IExpenseItem } from 'app/shared/model/expense-item.model';

@Injectable({ providedIn: 'root' })
export class ExpenseItemResolve implements Resolve<IExpenseItem> {
    constructor(private service: ExpenseItemService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((expenseItem: HttpResponse<ExpenseItem>) => expenseItem.body));
        }
        return of(new ExpenseItem());
    }
}

export const expenseItemRoute: Routes = [
    {
        path: 'expense-item',
        component: ExpenseItemComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.expenseItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'expense-item/:id/view',
        component: ExpenseItemDetailComponent,
        resolve: {
            expenseItem: ExpenseItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.expenseItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'expense-item/new',
        component: ExpenseItemUpdateComponent,
        resolve: {
            expenseItem: ExpenseItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.expenseItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'expense-item/:id/edit',
        component: ExpenseItemUpdateComponent,
        resolve: {
            expenseItem: ExpenseItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.expenseItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const expenseItemPopupRoute: Routes = [
    {
        path: 'expense-item/:id/delete',
        component: ExpenseItemDeletePopupComponent,
        resolve: {
            expenseItem: ExpenseItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.expenseItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
