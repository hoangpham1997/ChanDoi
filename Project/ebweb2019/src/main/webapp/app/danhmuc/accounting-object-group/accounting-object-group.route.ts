import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';
import { AccountingObjectGroupService } from './accounting-object-group.service';
import { AccountingObjectGroupComponent } from './accounting-object-group.component';
import { AccountingObjectGroupDetailComponent } from './accounting-object-group-detail.component';
import { AccountingObjectGroupUpdateComponent } from './accounting-object-group-update.component';
import { AccountingObjectGroupDeletePopupComponent } from './accounting-object-group-delete-dialog.component';
import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class AccountingObjectGroupResolve implements Resolve<IAccountingObjectGroup> {
    constructor(private service: AccountingObjectGroupService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((accountingObjectGroup: HttpResponse<AccountingObjectGroup>) => accountingObjectGroup.body));
        }
        return of(new AccountingObjectGroup());
    }
}

export const accountingObjectGroupRoute: Routes = [
    {
        path: '',
        component: AccountingObjectGroupComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucKhachHangVaNhaCungCap_Xoa],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.accountingObjectGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AccountingObjectGroupUpdateComponent,
        resolve: {
            accountingObjectGroup: AccountingObjectGroupResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucKhachHangVaNhaCungCap_Them],
            pageTitle: 'ebwebApp.accountingObjectGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: AccountingObjectGroupUpdateComponent,
        resolve: {
            accountingObjectGroup: AccountingObjectGroupResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucKhachHangVaNhaCungCap_Sua],
            pageTitle: 'ebwebApp.accountingObjectGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const accountingObjectGroupPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AccountingObjectGroupDeletePopupComponent,
        resolve: {
            accountingObjectGroup: AccountingObjectGroupResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucKhachHangVaNhaCungCap_Xoa],
            pageTitle: 'ebwebApp.accountingObjectGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
