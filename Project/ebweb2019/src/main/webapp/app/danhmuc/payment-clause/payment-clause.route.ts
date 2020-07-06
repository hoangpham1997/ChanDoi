import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PaymentClause } from 'app/shared/model/payment-clause.model';
import { PaymentClauseService } from './payment-clause.service';
import { PaymentClauseComponent } from './payment-clause.component';
import { PaymentClauseUpdateComponent } from './payment-clause-update.component';
import { PaymentClauseDeletePopupComponent } from './payment-clause-delete-dialog.component';
import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class PaymentClauseResolve implements Resolve<IPaymentClause> {
    constructor(private service: PaymentClauseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((paymentClause: HttpResponse<PaymentClause>) => paymentClause.body));
        }
        return of(new PaymentClause());
    }
}

export const paymentClauseRoute: Routes = [
    {
        path: '',
        component: PaymentClauseComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucDieuKhoanThanhToan_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.paymentClause.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: PaymentClauseUpdateComponent,
        resolve: {
            paymentClause: PaymentClauseResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucDieuKhoanThanhToan_Them],
            pageTitle: 'ebwebApp.paymentClause.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: PaymentClauseUpdateComponent,
        resolve: {
            paymentClause: PaymentClauseResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucDieuKhoanThanhToan_Sua],
            pageTitle: 'ebwebApp.paymentClause.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const paymentClausePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: PaymentClauseDeletePopupComponent,
        resolve: {
            paymentClause: PaymentClauseResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucDieuKhoanThanhToan_Xoa],
            pageTitle: 'ebwebApp.paymentClause.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
