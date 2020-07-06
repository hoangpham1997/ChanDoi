import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SystemOption } from 'app/shared/model/system-option.model';
import { SystemOptionService } from './system-option.service';
import { SystemOptionComponent } from './system-option.component';
import { SystemOptionDetailComponent } from './system-option-detail.component';
import { SystemOptionUpdateComponent } from './system-option-update.component';
import { SystemOptionDeletePopupComponent } from './system-option-delete-dialog.component';
import { ISystemOption } from 'app/shared/model/system-option.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class SystemOptionResolve implements Resolve<ISystemOption> {
    constructor(private service: SystemOptionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((systemOption: HttpResponse<SystemOption>) => systemOption.body));
        }
        return of(new SystemOption());
    }
}

export const systemOptionRoute: Routes = [
    {
        path: '',
        component: SystemOptionComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.TuyChon_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.systemOption.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/view',
        component: SystemOptionDetailComponent,
        resolve: {
            systemOption: SystemOptionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.systemOption.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: SystemOptionUpdateComponent,
        resolve: {
            systemOption: SystemOptionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.systemOption.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SystemOptionUpdateComponent,
        resolve: {
            systemOption: SystemOptionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.systemOption.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const systemOptionPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SystemOptionDeletePopupComponent,
        resolve: {
            systemOption: SystemOptionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.systemOption.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
