import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Bank } from 'app/shared/model/bank.model';
import { BankService } from './bank.service';
import { BankComponent } from './bank.component';
import { BankDetailComponent } from './bank-detail.component';
import { BankUpdateComponent } from './bank-update.component';
import { BankDeletePopupComponent } from './bank-delete-dialog.component';
import { IBank } from 'app/shared/model/bank.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class BankResolve implements Resolve<IBank> {
    constructor(private service: BankService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((bank: HttpResponse<Bank>) => bank.body));
        }
        return of(new Bank());
    }
}

export const bankRoute: Routes = [
    {
        path: '',
        component: BankComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucNganHang_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.bank.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    // {
    //     path: ':id/view',
    //     component: BankDetailComponent,
    //     resolve: {
    //         bank: BankResolve
    //     },
    //     data: {
    //         authorities: ['ROLE_USER', ROLE.DanhMucNganHang_Xem],
    //         pageTitle: 'ebwebApp.bank.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // },
    {
        path: 'new',
        component: BankUpdateComponent,
        resolve: {
            bank: BankResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucNganHang_Them],
            pageTitle: 'ebwebApp.bank.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: BankUpdateComponent,
        resolve: {
            bank: BankResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucNganHang_Sua],
            pageTitle: 'ebwebApp.bank.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const bankPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: BankDeletePopupComponent,
        resolve: {
            bank: BankResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucNganHang_Xoa],
            pageTitle: 'ebwebApp.bank.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
