import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { OrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from './organization-unit.service';
import { OrganizationUnitComponent } from './organization-unit.component';
import { OrganizationUnitDetailComponent } from './organization-unit-detail.component';
import { OrganizationUnitUpdateComponent } from './organization-unit-update.component';
import { OrganizationUnitDeletePopupComponent } from './organization-unit-delete-dialog.component';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class OrganizationUnitResolve implements Resolve<IOrganizationUnit> {
    constructor(private service: OrganizationUnitService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((organizationUnit: HttpResponse<OrganizationUnit>) => organizationUnit.body));
        }
        return of(new OrganizationUnit());
    }
}

export const organizationUnitRoute: Routes = [
    {
        path: '',
        component: OrganizationUnitComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.CoCauToChuc_Xem, ROLE.ROLE_MGT],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.organizationUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: OrganizationUnitDetailComponent,
        resolve: {
            organizationUnit: OrganizationUnitResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.CoCauToChuc_Xem, ROLE.ROLE_MGT],
            pageTitle: 'ebwebApp.organizationUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: OrganizationUnitUpdateComponent,
        resolve: {
            organizationUnit: OrganizationUnitResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.CoCauToChuc_Them, ROLE.ROLE_MGT],
            pageTitle: 'ebwebApp.organizationUnit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: OrganizationUnitUpdateComponent,
        resolve: {
            organizationUnit: OrganizationUnitResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.CoCauToChuc_Sua, ROLE.ROLE_MGT],
            pageTitle: 'ebwebApp.organizationUnit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const organizationUnitPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: OrganizationUnitDeletePopupComponent,
        resolve: {
            organizationUnit: OrganizationUnitResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.CoCauToChuc_Xoa, ROLE.ROLE_MGT],
            pageTitle: 'ebwebApp.organizationUnit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
