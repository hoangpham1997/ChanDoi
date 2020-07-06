import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from './account-list.service';
import { AccountListComponent } from './account-list.component';
import { AccountListUpdateComponent } from './account-list-update.component';
import { AccountListDeletePopupComponent } from './account-list-delete-dialog.component';
import { IAccountList } from 'app/shared/model/account-list.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class AccountListResolve implements Resolve<IAccountList> {
    constructor(private service: AccountListService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((accountList: HttpResponse<AccountList>) => accountList.body));
        }
        return of(new AccountList());
    }
}

export const accountListRoute: Routes = [
    {
        path: '',
        component: AccountListComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.HeThongTaiKhoan_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.accountList.home.title'
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
        component: AccountListUpdateComponent,
        resolve: {
            accountList: AccountListResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.HeThongTaiKhoan_Them],
            pageTitle: 'ebwebApp.accountList.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: AccountListUpdateComponent,
        resolve: {
            accountList: AccountListResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.HeThongTaiKhoan_Sua],
            pageTitle: 'ebwebApp.accountList.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const accountListPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AccountListDeletePopupComponent,
        resolve: {
            accountList: AccountListResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.HeThongTaiKhoan_Xoa],
            pageTitle: 'ebwebApp.accountList.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
