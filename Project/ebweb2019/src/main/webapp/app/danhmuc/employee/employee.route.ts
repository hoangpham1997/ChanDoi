import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from './employee.service';
import { AccountingObjectComponent } from './employee.component';
import { AccountingObjectUpdateComponent } from './employee-update.component';
import { AccountingObjectDeletePopupComponent } from './employee-delete-dialog.component';
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
            authorities: ['ROLE_USER', ROLE.DanhMucNhanVien_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.accountingObject.home.employee'
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
            authorities: ['ROLE_USER', ROLE.DanhMucNhanVien_Them],
            pageTitle: 'ebwebApp.accountingObject.home.employee'
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
            authorities: ['ROLE_USER', ROLE.DanhMucNhanVien_Sua],
            pageTitle: 'ebwebApp.accountingObject.home.employee'
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
            authorities: ['ROLE_USER', ROLE.DanhMucNhanVien_Xoa],
            pageTitle: 'ebwebApp.accountingObject.home.employee'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
