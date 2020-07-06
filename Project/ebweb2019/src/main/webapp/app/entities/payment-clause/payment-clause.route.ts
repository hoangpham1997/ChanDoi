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
import { PaymentClauseDetailComponent } from './payment-clause-detail.component';
import { PaymentClauseUpdateComponent } from './payment-clause-update.component';
import { PaymentClauseDeletePopupComponent } from './payment-clause-delete-dialog.component';
import { IPaymentClause } from 'app/shared/model/payment-clause.model';

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
        path: 'payment-clause',
        component: PaymentClauseComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.paymentClause.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'payment-clause/:id/view',
        component: PaymentClauseDetailComponent,
        resolve: {
            paymentClause: PaymentClauseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.paymentClause.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'payment-clause/new',
        component: PaymentClauseUpdateComponent,
        resolve: {
            paymentClause: PaymentClauseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.paymentClause.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'payment-clause/:id/edit',
        component: PaymentClauseUpdateComponent,
        resolve: {
            paymentClause: PaymentClauseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.paymentClause.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const paymentClausePopupRoute: Routes = [
    {
        path: 'payment-clause/:id/delete',
        component: PaymentClauseDeletePopupComponent,
        resolve: {
            paymentClause: PaymentClauseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.paymentClause.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
