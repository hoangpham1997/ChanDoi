import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AccountList } from 'app/shared/model/account-list.model';
import { KhoanMucChiPhiListService } from './khoan-muc-chi-phi-list.service';
import { KhoanMucChiPhiListComponent } from './khoan-muc-chi-phi-list.component';
import { KhoanMucChiPhiListUpdateComponent } from './khoan-muc-chi-phi-list-update.component';
import { KhoanMucChiPhiListDeletePopupComponent } from './khoan-muc-chi-phi-list-delete-dialog.component';
import { IAccountList } from 'app/shared/model/account-list.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ExpenseItem, IExpenseItem } from 'app/shared/model/expense-item.model';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class ExpenseItemResolve implements Resolve<IExpenseItem> {
    constructor(private service: KhoanMucChiPhiListService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((iExpenseItem: HttpResponse<IExpenseItem>) => iExpenseItem.body));
        }
        return of(new ExpenseItem());
    }
}

export const khoanMucChiPhiListRoute: Routes = [
    {
        path: '',
        component: KhoanMucChiPhiListComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DanhMucKhoanMucChiPhi_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.expenseItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    // {
    //     path: ':id/view',
    //     component: AccountListDetailComponent,
    //     resolve: {
    //         accountList: AccountListResolve
    //     },
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'ebwebApp.accountList.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // },
    {
        path: 'new',
        component: KhoanMucChiPhiListUpdateComponent,
        resolve: {
            expenseItem: ExpenseItemResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DanhMucKhoanMucChiPhi_Them],
            pageTitle: 'ebwebApp.expenseItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: KhoanMucChiPhiListUpdateComponent,
        resolve: {
            expenseItem: ExpenseItemResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DanhMucKhoanMucChiPhi_Sua],
            pageTitle: 'ebwebApp.expenseItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const expenseItemPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: KhoanMucChiPhiListDeletePopupComponent,
        resolve: {
            accountList: ExpenseItemResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DanhMucKhoanMucChiPhi_Xoa],
            pageTitle: 'ebwebApp.expenseItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
