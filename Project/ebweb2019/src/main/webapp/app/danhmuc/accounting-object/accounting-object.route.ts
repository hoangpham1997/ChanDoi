import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from './accounting-object.service';
import { AccountingObjectComponent } from './accounting-object.component';
import { AccountingObjectUpdateComponent } from './accounting-object-update.component';
import { AccountingObjectDeletePopupComponent } from './accounting-object-delete-dialog.component';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class AccountingObjectResolve implements Resolve<IAccountingObject> {
    constructor(private service: AccountingObjectService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((accountingObject: HttpResponse<AccountingObject>) => accountingObject.body));
        }
        return of(new AccountingObject());
    }
}

export const accountingObjectRoute: Routes = [
    {
        path: '',
        component: AccountingObjectComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucKhachHang_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.accountingObject.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AccountingObjectUpdateComponent,
        resolve: {
            accountingObject: AccountingObjectResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucKhachHang_Them],
            pageTitle: 'ebwebApp.accountingObject.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: AccountingObjectUpdateComponent,
        resolve: {
            accountingObject: AccountingObjectResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucKhachHang_Sua],
            pageTitle: 'ebwebApp.accountingObject.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const accountingObjectPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AccountingObjectDeletePopupComponent,
        resolve: {
            accountingObject: AccountingObjectResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucKhachHang_Xoa],
            pageTitle: 'ebwebApp.accountingObject.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
