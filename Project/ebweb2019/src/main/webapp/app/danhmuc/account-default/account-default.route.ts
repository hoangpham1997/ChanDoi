import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AccountDefault } from 'app/shared/model/account-default.model';
import { AccountDefaultService } from './account-default.service';
import { AccountDefaultComponent } from './account-default.component';
import { AccountDefaultDetailComponent } from './account-default-detail.component';
import { AccountDefaultUpdateComponent } from './account-default-update.component';
import { AccountDefaultDeletePopupComponent } from './account-default-delete-dialog.component';
import { IAccountDefault } from 'app/shared/model/account-default.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class AccountDefaultResolve implements Resolve<IAccountDefault> {
    constructor(private service: AccountDefaultService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        // if (id) {
        //     return this.service.find(id).pipe(map((accountDefault: HttpResponse<AccountDefault>) => accountDefault.body));
        // }
        return of(new AccountDefault());
    }
}

export const accountDefaultRoute: Routes = [
    {
        path: '',
        component: AccountDefaultComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TaiKhoanNgamDinh_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.accountDefault.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: AccountDefaultUpdateComponent,
        resolve: {
            accountDefault: AccountDefaultResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.accountDefault.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: AccountDefaultUpdateComponent,
        resolve: {
            accountDefault: AccountDefaultResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TaiKhoanNgamDinh_Sua],
            pageTitle: 'ebwebApp.accountDefault.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':typeID/edit',
        component: AccountDefaultUpdateComponent,
        resolve: {
            accountDefault: AccountDefaultResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TaiKhoanNgamDinh_Sua],
            pageTitle: 'ebwebApp.accountDefault.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const accountDefaultPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AccountDefaultDeletePopupComponent,
        resolve: {
            accountDefault: AccountDefaultResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.accountDefault.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
