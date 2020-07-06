import { Injectable } from '@angular/core';
import { Routes } from '@angular/router';
import { BankCompareComponent } from 'app/TienMatNganHang/doi_chieu_ngan_hang/bank-compare.component';
import { UserRouteAccessService } from 'app/core';
import { UnBankCompareComponent } from 'app/TienMatNganHang/doi_chieu_ngan_hang/un-bank-compare.component';
import { ViewBankCompareComponent } from 'app/TienMatNganHang/doi_chieu_ngan_hang/view-bank-compare.component';

@Injectable({ providedIn: 'root' })
export class BankCompareResolve {
    constructor() {}
}

export const bankCompareRoute: Routes = [
    {
        path: 'bank-compare',
        component: BankCompareComponent,
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.bankCompare.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'un-bank-compare',
        component: UnBankCompareComponent,
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.bankCompare.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'view-bank-compare',
        component: ViewBankCompareComponent,
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.bankCompare.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
