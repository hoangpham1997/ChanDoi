import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AccountTransfer } from 'app/shared/model/account-transfer.model';
import { AccountTransferService } from './account-transfer.service';
import { AccountTransferComponent } from './account-transfer.component';
import { AccountTransferDetailComponent } from './account-transfer-detail.component';
import { AccountTransferUpdateComponent } from './account-transfer-update.component';
import { AccountTransferDeletePopupComponent } from './account-transfer-delete-dialog.component';
import { IAccountTransfer } from 'app/shared/model/account-transfer.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class AccountTransferResolve implements Resolve<IAccountTransfer> {
    constructor(private service: AccountTransferService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((accountTransfer: HttpResponse<AccountTransfer>) => accountTransfer.body));
        }
        return of(new AccountTransfer());
    }
}

export const accountTransferRoute: Routes = [
    {
        path: '',
        component: AccountTransferComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TaiKhoanKetChuyen_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.accountTransfer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: AccountTransferDetailComponent,
        resolve: {
            accountTransfer: AccountTransferResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TaiKhoanKetChuyen_Xem],
            pageTitle: 'ebwebApp.accountTransfer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AccountTransferUpdateComponent,
        resolve: {
            accountTransfer: AccountTransferResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TaiKhoanKetChuyen_Them],
            pageTitle: 'ebwebApp.accountTransfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: AccountTransferUpdateComponent,
        resolve: {
            accountTransfer: AccountTransferResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TaiKhoanKetChuyen_Sua],
            pageTitle: 'ebwebApp.accountTransfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const accountTransferPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AccountTransferDeletePopupComponent,
        resolve: {
            accountTransfer: AccountTransferResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TaiKhoanKetChuyen_Xoa],
            pageTitle: 'ebwebApp.accountTransfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
