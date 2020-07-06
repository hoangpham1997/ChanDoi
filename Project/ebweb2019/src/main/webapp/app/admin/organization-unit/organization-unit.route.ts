import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { OrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitAdminService } from './organization-unit.service';
import { OrganizationUnitAdminComponent } from './organization-unit.component';
import { OrganizationUnitUpdateAdminComponent } from './organization-unit-update.component';
import { OrganizationUnitDeletePopupAdminComponent } from './organization-unit-delete-dialog.component';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';

@Injectable({ providedIn: 'root' })
export class OrganizationUnitAdminResolve implements Resolve<IOrganizationUnit> {
    constructor(private service: OrganizationUnitAdminService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((organizationUnit: HttpResponse<OrganizationUnit>) => organizationUnit.body));
        }
        return of(new OrganizationUnit());
    }
}

export const organizationUnitAdminRoute: Routes = [
    {
        path: 'organization-unit',
        component: OrganizationUnitAdminComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.organizationUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'organization-unit/new',
        component: OrganizationUnitUpdateAdminComponent,
        resolve: {
            organizationUnit: OrganizationUnitAdminResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ebwebApp.organizationUnit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'organization-unit/:id/edit',
        component: OrganizationUnitUpdateAdminComponent,
        resolve: {
            organizationUnit: OrganizationUnitAdminResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ebwebApp.organizationUnit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const organizationUnitPopupAdminRoute: Routes = [
    {
        path: 'organization-unit/:id/delete',
        component: OrganizationUnitDeletePopupAdminComponent,
        resolve: {
            organizationUnit: OrganizationUnitAdminResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ebwebApp.organizationUnit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
