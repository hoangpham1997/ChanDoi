import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { PPINVOICE_COMPONENT_TYPE } from 'app/app.constants';
import { KetChuyenLaiLoService } from 'app/tonghop/ket_chuyen_lai_lo/ket-chuyen-lai-lo.service';
import { KetChuyenLaiLoComponent } from 'app/tonghop/ket_chuyen_lai_lo/ket-chuyen-lai-lo.component';
import { KetChuyenLaiLoUpdateComponent } from 'app/tonghop/ket_chuyen_lai_lo/ket-chuyen-lai-lo-update.component';
import { GOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class KetChuyenLaiLoResolve implements Resolve<any> {
    constructor(private service: KetChuyenLaiLoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.findById({ id }).pipe(map((gOtherVoucher: HttpResponse<any>) => gOtherVoucher.body));
        }
        return of(new GOtherVoucher());
    }
}

export const KetChuyenLaiLoRoute: Routes = [
    {
        path: '',
        component: KetChuyenLaiLoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.KetChuyenLaiLo_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.gOtherVoucher.home.titleKc'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: KetChuyenLaiLoUpdateComponent,
        resolve: {
            gOtherVoucher: KetChuyenLaiLoResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.KetChuyenLaiLo_Them],
            pageTitle: 'ebwebApp.gOtherVoucher.home.titleKc'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/:rowNum',
        component: KetChuyenLaiLoUpdateComponent,
        resolve: {
            gOtherVoucher: KetChuyenLaiLoResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.KetChuyenLaiLo_Xem],
            pageTitle: 'ebwebApp.gOtherVoucher.home.titleKc'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: KetChuyenLaiLoUpdateComponent,
        resolve: {
            gOtherVoucher: KetChuyenLaiLoResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.KetChuyenLaiLo_Xem],
            pageTitle: 'ebwebApp.gOtherVoucher.home.titleKc'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-g-other-voucher',
        component: KetChuyenLaiLoUpdateComponent,
        resolve: {
            gOtherVoucher: KetChuyenLaiLoResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.KetChuyenLaiLo_Xem],
            pageTitle: 'ebwebApp.gOtherVoucher.home.titleKc'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];
