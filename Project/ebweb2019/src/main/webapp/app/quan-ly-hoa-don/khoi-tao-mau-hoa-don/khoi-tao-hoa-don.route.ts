import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { KhoiTaoHoaDonComponent } from './khoi-tao-hoa-don.component';
import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { of } from 'rxjs';
import { IAReport } from 'app/shared/model/ia-report.model';
import { KhoiTaoHoaDonUpdateComponent } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/khoi-tao-hoa-don-update.component';
import { KhoiTaoHoaDonDeletePopupComponent } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/khoi-tao-hoa-don-delete-dialog.component';
import { IAReportService } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/ia-report.service';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class IAReportResolve implements Resolve<IAReport> {
    constructor(private service: IAReportService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((res: HttpResponse<IAReport>) => res.body));
        }
        return of(new IAReport());
    }
}

export const KhoiTaoHoaDonRoute: Routes = [
    {
        path: '',
        component: KhoiTaoHoaDonComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.KTHD_XEM],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.iAReport.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: KhoiTaoHoaDonUpdateComponent,
        resolve: {
            iaReport: IAReportResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.KTHD_THEM],
            pageTitle: 'ebwebApp.iAReport.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: KhoiTaoHoaDonUpdateComponent,
        resolve: {
            iaReport: IAReportResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.KTHD_XEM],
            pageTitle: 'ebwebApp.iAReport.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: KhoiTaoHoaDonUpdateComponent,
        resolve: {
            iaReport: IAReportResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.KTHD_XEM],
            pageTitle: 'ebwebApp.iAReport.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const KhoiTaoHoaDonPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: KhoiTaoHoaDonDeletePopupComponent,
        resolve: {
            iaReport: IAReportResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.KTHD_XEM],
            pageTitle: 'ebwebApp.iAReport.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
