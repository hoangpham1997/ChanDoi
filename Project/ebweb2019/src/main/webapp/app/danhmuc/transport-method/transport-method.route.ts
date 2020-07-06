import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';

import { TransportMethodService } from './transport-method.service';
import { TransportMethodComponent } from './transport-method.component';
import { TransportMethodUpdateComponent } from './transport-method-update.component';
import { TransportMethodDeletePopupComponent } from './transport-method-delete-dialog.component';

import { ITransportMethod } from 'app/shared/model/transport-method.model';
import { TransportMethod } from 'app/shared/model/transport-method.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class TransportMethodResolve implements Resolve<ITransportMethod> {
    constructor(private service: TransportMethodService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((transportMethod: HttpResponse<TransportMethod>) => transportMethod.body));
        }
        return of(new TransportMethod());
    }
}

export const transportMethodRoute: Routes = [
    {
        path: '',
        component: TransportMethodComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DanhMucPhuongThucVanChuyen_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.transportMethod.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: TransportMethodUpdateComponent,
        resolve: {
            transportMethod: TransportMethodResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DanhMucPhuongThucVanChuyen_Them],
            pageTitle: 'ebwebApp.transportMethod.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: TransportMethodUpdateComponent,
        resolve: {
            transportMethod: TransportMethodResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DanhMucPhuongThucVanChuyen_Xem],
            pageTitle: 'ebwebApp.transportMethod.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const transportMedthodPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: TransportMethodDeletePopupComponent,
        resolve: {
            transportMethod: TransportMethodResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DanhMucPhuongThucVanChuyen_Xoa],
            pageTitle: 'ebwebApp.transportMethod.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
