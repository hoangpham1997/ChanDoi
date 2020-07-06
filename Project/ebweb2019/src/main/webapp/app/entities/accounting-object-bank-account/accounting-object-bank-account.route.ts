import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { AccountingObjectBankAccountService } from './accounting-object-bank-account.service';
import { AccountingObjectBankAccountComponent } from './accounting-object-bank-account.component';
import { AccountingObjectBankAccountDetailComponent } from './accounting-object-bank-account-detail.component';
import { AccountingObjectBankAccountUpdateComponent } from './accounting-object-bank-account-update.component';
import { AccountingObjectBankAccountDeletePopupComponent } from './accounting-object-bank-account-delete-dialog.component';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';

@Injectable({ providedIn: 'root' })
export class AccountingObjectBankAccountResolve implements Resolve<IAccountingObjectBankAccount> {
    constructor(private service: AccountingObjectBankAccountService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((accountingObjectBankAccount: HttpResponse<AccountingObjectBankAccount>) => accountingObjectBankAccount.body));
        }
        return of(new AccountingObjectBankAccount());
    }
}

export const accountingObjectBankAccountRoute: Routes = [
    {
        path: 'accounting-object-bank-account',
        component: AccountingObjectBankAccountComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.accountingObjectBankAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'accounting-object-bank-account/:id/view',
        component: AccountingObjectBankAccountDetailComponent,
        resolve: {
            accountingObjectBankAccount: AccountingObjectBankAccountResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.accountingObjectBankAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'accounting-object-bank-account/new',
        component: AccountingObjectBankAccountUpdateComponent,
        resolve: {
            accountingObjectBankAccount: AccountingObjectBankAccountResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.accountingObjectBankAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'accounting-object-bank-account/:id/edit',
        component: AccountingObjectBankAccountUpdateComponent,
        resolve: {
            accountingObjectBankAccount: AccountingObjectBankAccountResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.accountingObjectBankAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const accountingObjectBankAccountPopupRoute: Routes = [
    {
        path: 'accounting-object-bank-account/:id/delete',
        component: AccountingObjectBankAccountDeletePopupComponent,
        resolve: {
            accountingObjectBankAccount: AccountingObjectBankAccountResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.accountingObjectBankAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
