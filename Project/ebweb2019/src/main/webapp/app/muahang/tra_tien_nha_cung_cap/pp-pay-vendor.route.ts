import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { PPPayVendorComponent } from 'app/muahang/tra_tien_nha_cung_cap/pp-pay-vendor.component';
import { PPPayVendorDetailComponent } from 'app/muahang/tra_tien_nha_cung_cap/pp-pay-vendor-detail.component';
import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { IPPPayVendorBill, PPPayVendorBill } from 'app/shared/model/pp-pay-vendor-bill';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class PPPayVendorResolve implements Resolve<any> {
    constructor(private service: AccountingObjectService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const accountingObjectID = route.params['id'] ? route.params['id'] : null;
        const fDate = route.params['fromDate'] ? route.params['fromDate'] : null;
        const tDate = route.params['toDate'] ? route.params['toDate'] : null;
        if (accountingObjectID) {
            return this.service
                .getPPPayVendorBills({
                    fromDate: fDate,
                    toDate: tDate,
                    accountObjectID: accountingObjectID
                })
                .pipe(
                    map((pPPayVendorBills: HttpResponse<IPPPayVendorBill[]>) =>
                        pPPayVendorBills.body.filter(x => x.debitAmountOriginal > 0)
                    )
                );
        }
        return of(new PPPayVendorBill());
    }
}

export const pPPayVendorRoute: Routes = [
    {
        path: '',
        component: PPPayVendorComponent,
        data: {
            authorities: ['ROLE_USER', ROLE.TraTienNhaCungCap_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.pPPayVendor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pp-pay-vendor-detail/:id/:fromDate/:toDate',
        component: PPPayVendorDetailComponent,
        resolve: {
            pPPayVendorBills: PPPayVendorResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TraTienNhaCungCap_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.pPPayVendor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
