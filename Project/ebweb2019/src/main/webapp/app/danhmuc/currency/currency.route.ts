import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Currency } from 'app/shared/model/currency.model';
import { CurrencyService } from './currency.service';
import { CurrencyComponent } from './currency.component';
import { CurrencyDetailComponent } from './currency-detail.component';
import { CurrencyUpdateComponent } from './currency-update.component';
import { CurrencyDeletePopupComponent } from './currency-delete-dialog.component';
import { ICurrency } from 'app/shared/model/currency.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class CurrencyResolve implements Resolve<ICurrency> {
    constructor(private service: CurrencyService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((currency: HttpResponse<Currency>) => currency.body));
        }
        return of(new Currency());
    }
}

export const currencyRoute: Routes = [
    {
        path: '',
        component: CurrencyComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucLoaiTien_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.currency.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    // {
    //     path: ':id/view',
    //     component: CurrencyDetailComponent,
    //     resolve: {
    //         currency: CurrencyResolve
    //     },
    //     data: {
    //         authorities: ['ROLE_ADMIN', ROLE.DanhMucLoaiTien_Xem],
    //         pageTitle: 'ebwebApp.currency.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // },
    {
        path: 'new',
        component: CurrencyUpdateComponent,
        resolve: {
            currency: CurrencyResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucLoaiTien_Them],
            pageTitle: 'ebwebApp.currency.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: CurrencyUpdateComponent,
        resolve: {
            currency: CurrencyResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucLoaiTien_Sua],
            pageTitle: 'ebwebApp.currency.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const currencyPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: CurrencyDeletePopupComponent,
        resolve: {
            currency: CurrencyResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucLoaiTien_Xoa],
            pageTitle: 'ebwebApp.currency.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
