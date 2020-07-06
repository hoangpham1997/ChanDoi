import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { BankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { BankAccountDetailsService } from './bank-account-details.service';
import { BankAccountDetailsComponent } from './bank-account-details.component';
import { BankAccountDetailsDetailComponent } from './bank-account-details-detail.component';
import { BankAccountDetailsUpdateComponent } from './bank-account-details-update.component';
import { BankAccountDetailsDeletePopupComponent } from './bank-account-details-delete-dialog.component';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class BankAccountDetailsResolve implements Resolve<IBankAccountDetails> {
    constructor(private service: BankAccountDetailsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((bankAccountDetails: HttpResponse<BankAccountDetails>) => bankAccountDetails.body));
        }
        return of(new BankAccountDetails());
    }
}

export const bankAccountDetailsRoute: Routes = [
    {
        path: '',
        component: BankAccountDetailsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucTaiKhoanNganHang_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.bankAccountDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: BankAccountDetailsUpdateComponent,
        resolve: {
            bankAccountDetails: BankAccountDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucTaiKhoanNganHang_Them],
            pageTitle: 'ebwebApp.bankAccountDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: BankAccountDetailsUpdateComponent,
        resolve: {
            bankAccountDetails: BankAccountDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucTaiKhoanNganHang_Sua],
            pageTitle: 'ebwebApp.bankAccountDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const bankAccountDetailsPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: BankAccountDetailsDeletePopupComponent,
        resolve: {
            bankAccountDetails: BankAccountDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucTaiKhoanNganHang_Xoa],
            pageTitle: 'ebwebApp.bankAccountDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
