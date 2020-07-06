import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CPExpenseTranfer } from 'app/shared/model/cp-expense-tranfer.model';
import { CPExpenseTranferService } from './cp-expense-tranfer.service';
import { CPExpenseTranferComponent } from './cp-expense-tranfer.component';
import { CPExpenseTranferUpdateComponent } from './cp-expense-tranfer-update.component';
import { CPExpenseTranferDeletePopupComponent } from './cp-expense-tranfer-delete-dialog.component';
import { ICPExpenseTranfer } from 'app/shared/model/cp-expense-tranfer.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';

@Injectable({ providedIn: 'root' })
export class CPExpenseTranferResolve implements Resolve<ICPExpenseTranfer> {
    constructor(private service: CPExpenseTranferService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((cPExpenseTranfer: HttpResponse<CPExpenseTranfer>) => cPExpenseTranfer.body));
        }
        return of(new CPExpenseTranfer());
    }
}

export const cPExpenseTranferRoute: Routes = [
    {
        path: '',
        component: CPExpenseTranferComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.cPExpenseTranfer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: CPExpenseTranferUpdateComponent,
        resolve: {
            cPExpenseTranfer: CPExpenseTranferResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPExpenseTranfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: CPExpenseTranferUpdateComponent,
        resolve: {
            cPExpenseTranfer: CPExpenseTranferResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPExpenseTranfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const cPExpenseTranferPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: CPExpenseTranferDeletePopupComponent,
        resolve: {
            cPExpenseTranfer: CPExpenseTranferResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPExpenseTranfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
