import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ChiPhiTraTruocComponent } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc.component';
import { ChiPhiTraTruocDetailComponent } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc-detail.component';
import { ChiPhiTraTruocUpdateComponent } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc-update.component';
import { ChiPhiTraTruocDeleteDialogComponent } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc-delete-dialog.component';
import { ChiPhiTraTruocService } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc.service';
import { IPrepaidExpense, PrepaidExpense } from 'app/shared/model/prepaid-expense.model';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class ChiPhiTraTruocResolve implements Resolve<IPrepaidExpense> {
    constructor(private service: ChiPhiTraTruocService) {}
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((prepaidExpense: HttpResponse<IPrepaidExpense>) => prepaidExpense.body));
        }
        return of(new PrepaidExpense());
    }
}

export const chiPhiTraTruocRoute: Routes = [
    {
        path: '',
        component: ChiPhiTraTruocComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChiPhiTRaTruoc_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.prepaidExpense.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: ChiPhiTraTruocDetailComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams,
            prepaidExpense: ChiPhiTraTruocResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.prepaidExpense.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: ChiPhiTraTruocUpdateComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams,
            prepaidExpense: ChiPhiTraTruocResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChiPhiTRaTruoc_Them],
            pageTitle: 'ebwebApp.prepaidExpense.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'hasSearch/:isSearch',
        component: ChiPhiTraTruocComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChiPhiTRaTruoc_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.prepaidExpense.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit/:rowNum',
        component: ChiPhiTraTruocUpdateComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams,
            prepaidExpense: ChiPhiTraTruocResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.prepaidExpense.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: ChiPhiTraTruocUpdateComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams,
            prepaidExpense: ChiPhiTraTruocResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChiPhiTRaTruoc_Sua],
            pageTitle: 'ebwebApp.prepaidExpense.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const chiPhiTraTruocPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ChiPhiTraTruocDeleteDialogComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams,
            prepaidExpense: ChiPhiTraTruocResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ChiPhiTRaTruoc_Xoa],
            pageTitle: 'ebwebApp.prepaidExpense.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
