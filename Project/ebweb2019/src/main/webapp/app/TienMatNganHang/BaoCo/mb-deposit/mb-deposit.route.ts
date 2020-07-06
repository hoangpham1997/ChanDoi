import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MBDeposit } from 'app/shared/model/mb-deposit.model';
import { MBDepositService } from './mb-deposit.service';
import { MBDepositComponent } from './mb-deposit.component';
import { MBDepositDetailComponent } from './mb-deposit-detail.component';
import { MBDepositUpdateComponent } from './mb-deposit-update.component';
import { MBDepositDeletePopupComponent } from './mb-deposit-delete-dialog.component';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { MCReceiptComponent } from 'app/TienMatNganHang/phieu-thu/mc-receipt';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class MBDepositResolve implements Resolve<IMBDeposit> {
    constructor(private service: MBDepositService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((mBDeposit: HttpResponse<MBDeposit>) => mBDeposit.body));
        }
        return of(new MBDeposit());
    }
}

export const mBDepositRoute: Routes = [
    {
        path: '',
        component: MBDepositComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoCo_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.mBDeposit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: MBDepositDetailComponent,
        resolve: {
            mBDeposit: MBDepositResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoCo_Xem],
            pageTitle: 'ebwebApp.mBDeposit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'hasSearch/:isSearch',
        component: MBDepositComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoCo_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.mBDeposit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: MBDepositUpdateComponent,
        resolve: {
            mBDeposit: MBDepositResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoCo_Them],
            pageTitle: 'ebwebApp.mBDeposit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/:rowNum',
        component: MBDepositUpdateComponent,
        resolve: {
            mBDeposit: MBDepositResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoCo_Sua],
            pageTitle: 'ebwebApp.mBDeposit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: MBDepositUpdateComponent,
        resolve: {
            mBDeposit: MBDepositResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoCo_Sua],
            pageTitle: 'ebwebApp.mBDeposit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: MBDepositUpdateComponent,
        resolve: {
            mCPayment: MBDepositResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoCo_Sua],
            pageTitle: 'ebwebApp.mBDeposit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const mBDepositPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MBDepositDeletePopupComponent,
        resolve: {
            mBDeposit: MBDepositResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoCo_Xoa],
            pageTitle: 'ebwebApp.mBDeposit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
