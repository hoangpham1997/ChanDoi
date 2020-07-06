import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';

import { BudgetItemService } from './budget-item.service';
import { BudgetItemComponent } from './budget-item.component';
import { BudgetItemUpdateComponent } from './budget-item-update.component';
import { BudgetItemDeletePopupComponent } from './budget-item-delete-dialog.component';

import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { BudgetItem } from 'app/shared/model/budget-item.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class BudgetItemResolve implements Resolve<IBudgetItem> {
    constructor(private service: BudgetItemService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((budgetItem: HttpResponse<BudgetItem>) => budgetItem.body));
        }
        return of(new BudgetItem());
    }
}

export const budgetItemRoute: Routes = [
    {
        path: '',
        component: BudgetItemComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DanhMucThuChi_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.budgetItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: BudgetItemUpdateComponent,
        resolve: {
            budgetItem: BudgetItemResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DanhMucThuChi_Them],
            pageTitle: 'ebwebApp.budgetItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: BudgetItemUpdateComponent,
        resolve: {
            budgetItem: BudgetItemResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DanhMucThuChi_Xem],
            pageTitle: 'ebwebApp.budgetItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const budgetItemPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: BudgetItemDeletePopupComponent,
        resolve: {
            budgetItem: BudgetItemResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DanhMucThuChi_Xoa],
            pageTitle: 'ebwebApp.budgetItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
