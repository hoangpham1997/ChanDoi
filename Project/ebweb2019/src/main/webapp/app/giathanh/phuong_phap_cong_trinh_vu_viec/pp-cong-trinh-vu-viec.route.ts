import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CPPeriod } from 'app/shared/model/cp-period.model';
import { PpCongTrinhVuViecService } from './pp-cong-trinh-vu-viec.service';
import { PpCongTrinhVuViecComponent } from './pp-cong-trinh-vu-viec.component';
import { PpCongTrinhVuViecUpdateComponent } from './pp-cong-trinh-vu-viec-update.component';
import { ICPPeriod } from 'app/shared/model/cp-period.model';

@Injectable({ providedIn: 'root' })
export class CPPeriodResolve implements Resolve<ICPPeriod> {
    constructor(private service: PpCongTrinhVuViecService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.getCPPeriodByID({ id }).pipe(map((cPPeriod: HttpResponse<CPPeriod>) => cPPeriod.body));
        }
        return of(new CPPeriod());
    }
}

export const ppCongTrinhVuViecRoute: Routes = [
    {
        path: 'cong-trinh-vu-viec',
        component: PpCongTrinhVuViecComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.cPPeriod.home.titleCTVV'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cong-trinh-vu-viec/new',
        component: PpCongTrinhVuViecUpdateComponent,
        resolve: {
            cPPeriod: CPPeriodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPPeriod.home.titleCTVV'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cong-trinh-vu-viec/:id/edit',
        component: PpCongTrinhVuViecUpdateComponent,
        resolve: {
            cPPeriod: CPPeriodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPPeriod.home.titleCTVV'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cong-trinh-vu-viec/:id/acceptance',
        component: PpCongTrinhVuViecUpdateComponent,
        resolve: {
            cPPeriod: CPPeriodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPPeriod.home.titleCTVV'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'don-hang',
        component: PpCongTrinhVuViecComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.cPPeriod.home.titleDonHang'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'don-hang/new',
        component: PpCongTrinhVuViecUpdateComponent,
        resolve: {
            cPPeriod: CPPeriodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPPeriod.home.titleDonHang'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'don-hang/:id/edit',
        component: PpCongTrinhVuViecUpdateComponent,
        resolve: {
            cPPeriod: CPPeriodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPPeriod.home.titleDonHang'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'don-hang/:id/acceptance',
        component: PpCongTrinhVuViecUpdateComponent,
        resolve: {
            cPPeriod: CPPeriodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPPeriod.home.titleDonHang'
        },
        canActivate: [UserRouteAccessService]
    }
];
