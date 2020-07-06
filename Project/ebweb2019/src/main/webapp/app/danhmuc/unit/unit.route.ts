import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Unit } from 'app/shared/model/unit.model';
import { UnitService } from './unit.service';
import { UnitComponent } from './unit.component';
import { UnitDetailComponent } from './unit-detail.component';
import { UnitUpdateComponent } from './unit-update.component';
import { UnitDeletePopupComponent } from './unit-delete-dialog.component';
import { IUnit } from 'app/shared/model/unit.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class UnitResolve implements Resolve<IUnit> {
    constructor(private service: UnitService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((unit: HttpResponse<Unit>) => unit.body));
        }
        return of(new Unit());
    }
}

export const unitRoute: Routes = [
    {
        path: '',
        component: UnitComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucDonViTinh_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.unit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: UnitUpdateComponent,
        resolve: {
            unit: UnitResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucDonViTinh_Them],
            pageTitle: 'ebwebApp.unit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: UnitUpdateComponent,
        resolve: {
            unit: UnitResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucDonViTinh_Sua],
            pageTitle: 'ebwebApp.unit.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const unitPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: UnitDeletePopupComponent,
        resolve: {
            unit: UnitResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucDonViTinh_Xoa],
            pageTitle: 'ebwebApp.unit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
