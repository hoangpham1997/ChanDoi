import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';
import { ITIAudit, TIAudit } from 'app/shared/model/ti-audit.model';
import { KiemKeCcdcService } from 'app/congcudungcu/kiem-ke-ccdc/kiem-ke-ccdc.service';
import { KiemKeCcdcComponent } from 'app/congcudungcu/kiem-ke-ccdc/kiem-ke-ccdc.component';
import { KiemKeCcdcDetailComponent } from 'app/congcudungcu/kiem-ke-ccdc/kiem-ke-ccdc-detail.component';
import { KiemKeCcdcUpdateComponent } from 'app/congcudungcu/kiem-ke-ccdc/kiem-ke-ccdc-update.component';
import { KiemKeCcdcDeletePopupComponent } from 'app/congcudungcu/kiem-ke-ccdc/kiem-ke-ccdc-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class ITIAuditResolve implements Resolve<ITIAudit> {
    constructor(private service: KiemKeCcdcService) {}
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((itiAudits: HttpResponse<ITIAudit>) => itiAudits.body));
        }
        return of(new TIAudit());
    }
}
export const KiemKeCcdcRoute: Routes = [
    {
        path: '',
        component: KiemKeCcdcComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.tIAudit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: KiemKeCcdcDetailComponent,
        resolve: {
            itiAudits: ITIAuditResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAudit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: KiemKeCcdcUpdateComponent,
        resolve: {
            itiAudits: ITIAuditResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAudit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'hasSearch/:isSearch',
        component: KiemKeCcdcComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.tIAudit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit/:rowNum',
        component: KiemKeCcdcUpdateComponent,
        resolve: {
            itiAudits: ITIAuditResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAudit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: KiemKeCcdcUpdateComponent,
        resolve: {
            itiAudits: ITIAuditResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAudit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-g-other-voucher',
        component: KiemKeCcdcUpdateComponent,
        resolve: {
            itiAudits: ITIAuditResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAudit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const KiemkeCcdcPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: KiemKeCcdcDeletePopupComponent,
        resolve: {
            itiAudits: ITIAuditResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xem],
            pageTitle: 'ebwebApp.tIAudit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
