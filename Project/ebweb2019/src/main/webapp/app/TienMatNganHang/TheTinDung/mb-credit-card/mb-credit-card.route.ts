import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MBCreditCard } from 'app/shared/model/mb-credit-card.model';
import { MBCreditCardService } from './mb-credit-card.service';
import { MBCreditCardComponent } from './mb-credit-card.component';
import { MBCreditCardDetailComponent } from './mb-credit-card-detail.component';
import { MBCreditCardUpdateComponent } from './mb-credit-card-update.component';
import { MBCreditCardDeletePopupComponent } from './mb-credit-card-delete-dialog.component';
import { IMBCreditCard } from 'app/shared/model/mb-credit-card.model';
import { MBDepositComponent, MBDepositResolve, MBDepositUpdateComponent } from 'app/TienMatNganHang/BaoCo/mb-deposit';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class MBCreditCardResolve implements Resolve<IMBCreditCard> {
    constructor(private service: MBCreditCardService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((mBCreditCard: HttpResponse<MBCreditCard>) => mBCreditCard.body));
        }
        return of(new MBCreditCard());
    }
}

export const mBCreditCardRoute: Routes = [
    {
        path: '',
        component: MBCreditCardComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TheTinDung_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.mBCreditCard.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: MBCreditCardDetailComponent,
        resolve: {
            mBCreditCard: MBCreditCardResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TheTinDung_Xem],
            pageTitle: 'ebwebApp.mBCreditCard.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: MBCreditCardUpdateComponent,
        resolve: {
            mBCreditCard: MBCreditCardResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TheTinDung_Them],
            pageTitle: 'ebwebApp.mBCreditCard.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'hasSearch/:isSearch',
        component: MBCreditCardComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TheTinDung_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.mBCreditCard.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit/:rowNum',
        component: MBCreditCardUpdateComponent,
        resolve: {
            mBCreditCard: MBCreditCardResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TheTinDung_Sua],
            pageTitle: 'ebwebApp.mBCreditCard.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: MBCreditCardUpdateComponent,
        resolve: {
            mBCreditCard: MBCreditCardResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TheTinDung_Sua],
            pageTitle: 'ebwebApp.mBCreditCard.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: MBCreditCardUpdateComponent,
        resolve: {
            mCPayment: MBCreditCardResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TheTinDung_Sua],
            pageTitle: 'ebwebApp.mBCreditCard.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const mBCreditCardPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MBCreditCardDeletePopupComponent,
        resolve: {
            mBCreditCard: MBCreditCardResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TheTinDung_Xoa],
            pageTitle: 'ebwebApp.mBCreditCard.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
