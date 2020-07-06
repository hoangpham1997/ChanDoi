import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CreditCard } from 'app/shared/model/credit-card.model';
import { CreditCardService } from './credit-card.service';
import { CreditCardComponent } from './credit-card.component';
import { CreditCardDetailComponent } from './credit-card-detail.component';
import { CreditCardUpdateComponent } from './credit-card-update.component';
import { CreditCardDeletePopupComponent } from './credit-card-delete-dialog.component';
import { ICreditCard } from 'app/shared/model/credit-card.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class CreditCardResolve implements Resolve<ICreditCard> {
    constructor(private service: CreditCardService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((creditCard: HttpResponse<CreditCard>) => creditCard.body));
        }
        return of(new CreditCard());
    }
}

export const creditCardRoute: Routes = [
    {
        path: '',
        component: CreditCardComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucTheTinDung_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.creditCard.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    // {
    //     path: ':id/view',
    //     component: CreditCardDetailComponent,
    //     resolve: {
    //         creditCard: CreditCardResolve
    //     },
    //     data: {
    //         authorities: ['ROLE_USER', ROLE.DanhMucTheTinDung_Xem],
    //         pageTitle: 'ebwebApp.creditCard.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // },
    {
        path: 'new',
        component: CreditCardUpdateComponent,
        resolve: {
            creditCard: CreditCardResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucTheTinDung_Them],
            pageTitle: 'ebwebApp.creditCard.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: CreditCardUpdateComponent,
        resolve: {
            creditCard: CreditCardResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucTheTinDung_Sua],
            pageTitle: 'ebwebApp.creditCard.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const creditCardPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: CreditCardDeletePopupComponent,
        resolve: {
            creditCard: CreditCardResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucTheTinDung_Xoa],
            pageTitle: 'ebwebApp.creditCard.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
