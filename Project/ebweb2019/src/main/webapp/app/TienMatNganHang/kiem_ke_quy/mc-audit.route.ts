import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IMCAudit, MCAudit } from 'app/shared/model/mc-audit.model';
import { MCAuditService } from './mc-audit.service';
import { MCAuditComponent } from './mc-audit.component';
import { MCAuditUpdateComponent } from './mc-audit-update.component';
import { MCAuditDeletePopupComponent } from './mc-audit-delete-dialog.component';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class MCAuditResolve implements Resolve<IMCAudit> {
    constructor(private service: MCAuditService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((mCAudit: HttpResponse<MCAudit>) => mCAudit.body));
        }
        return of(new MCAudit());
    }
}

export const mCAuditRoute: Routes = [
    {
        path: '',
        component: MCAuditComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.KiemKeQuy_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.mCAudit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: MCAuditUpdateComponent,
        resolve: {
            mCAudit: MCAuditResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mCAudit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: MCAuditUpdateComponent,
        resolve: {
            mCAudit: MCAuditResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.KiemKeQuy_Them],
            pageTitle: 'ebwebApp.mCAudit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/:rowNum',
        component: MCAuditUpdateComponent,
        resolve: {
            mCAudit: MCAuditResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mCAudit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: MCAuditUpdateComponent,
        resolve: {
            mCAudit: MCAuditResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.KiemKeQuy_Sua],
            pageTitle: 'ebwebApp.mCAudit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: MCAuditUpdateComponent,
        resolve: {
            mCAudit: MCAuditResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mCAudit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const mCAuditPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MCAuditDeletePopupComponent,
        resolve: {
            mCAudit: MCAuditResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.KiemKeQuy_Xoa],
            pageTitle: 'ebwebApp.mCAudit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
