import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { SAReceiptDebitComponent } from 'app/ban-hang/thu_tien_khach_hang/sa-receipt-debit.component';
import { SAReceiptDebitDetailComponent } from 'app/ban-hang/thu_tien_khach_hang/sa-receipt-debit-detail.component';
import { ISAReceiptDebitBill, SAReceiptDebitBill } from 'app/shared/model/sa-receipt-debit-bill';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class SAReceiptDebitResolve implements Resolve<any> {
    constructor(private service: AccountingObjectService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const accountingObjectID = route.params['id'] ? route.params['id'] : null;
        const fDate = route.params['fromDate'] ? route.params['fromDate'] : null;
        const tDate = route.params['toDate'] ? route.params['toDate'] : null;
        if (accountingObjectID) {
            return this.service
                .getSAReceiptDebitBills({
                    fromDate: fDate,
                    toDate: tDate,
                    accountObjectID: accountingObjectID
                })
                .pipe(
                    map((saReceiptDebitBills: HttpResponse<ISAReceiptDebitBill[]>) =>
                        saReceiptDebitBills.body.filter(x => x.creditAmountOriginal > 0)
                    )
                );
        }
        return of(new SAReceiptDebitBill());
    }
}

export const sAReceiptDebitRoute: Routes = [
    {
        path: '',
        component: SAReceiptDebitComponent,
        data: {
            authorities: ['ROLE_USER', ROLE.ThuTienKhachHang_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.sAReceiptDebit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sa-receipt-debit-detail/:id/:fromDate/:toDate',
        component: SAReceiptDebitDetailComponent,
        resolve: {
            saReceiptDebitBills: SAReceiptDebitResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.ThuTienKhachHang_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.sAReceiptDebit.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
