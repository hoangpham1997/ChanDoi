import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CPOPN } from 'app/shared/model/cpopn.model';
import { CPOPNService } from './cpopn.service';
import { CPOPNComponent } from './cpopn.component';
import { CPOPNDetailComponent } from './cpopn-detail.component';
import { CPOPNUpdateComponent } from './cpopn-update.component';
import { CPOPNDeletePopupComponent } from './cpopn-delete-dialog.component';
import { ICPOPN } from 'app/shared/model/cpopn.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class CPOPNResolve implements Resolve<ICPOPN> {
    constructor(private service: CPOPNService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((cPOPN: HttpResponse<CPOPN>) => cPOPN.body));
        }
        return of(new CPOPN());
    }
}

export const cPOPNRoute: Routes = [
    {
        path: '',
        component: CPOPNComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.ChiPhiDoDangDauKy_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.cPOPN.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/view',
        component: CPOPNDetailComponent,
        resolve: {
            cPOPN: CPOPNResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPOPN.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: CPOPNUpdateComponent,
        resolve: {
            cPOPN: CPOPNResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPOPN.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: CPOPNUpdateComponent,
        resolve: {
            cPOPN: CPOPNResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.ChiPhiDoDangDauKy_Sua],
            pageTitle: 'ebwebApp.cPOPN.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cPOPNPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: CPOPNDeletePopupComponent,
        resolve: {
            cPOPN: CPOPNResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPOPN.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
