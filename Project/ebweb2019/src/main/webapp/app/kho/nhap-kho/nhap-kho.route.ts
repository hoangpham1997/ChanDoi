import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { NhapKhoComponent } from './nhap-kho.component';
import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Pporder } from 'app/shared/model/pporder.model';
import { PporderService } from 'app/entities/pporder';
import { map } from 'rxjs/operators';
import { of } from 'rxjs';
import { NhapKhoUpdateComponent } from 'app/kho/nhap-kho/nhap-kho-update.component';
import { NhapKhoDeletePopupComponent } from 'app/kho/nhap-kho/nhap-kho-delete-dialog.component';
import { RSInwardOutward } from 'app/shared/model/rs-inward-outward.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';
import { RSInwardOutwardService } from 'app/entities/rs-inward-outward/rs-inward-outward.service';

@Injectable({ providedIn: 'root' })
export class PPOrderResolve implements Resolve<Pporder> {
    constructor(private service: RSInwardOutwardService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((mBTellerPaper: HttpResponse<RSInwardOutward>) => mBTellerPaper.body));
        }
        return of(new RSInwardOutward());
    }
}

export const NhapKhoRoute: Routes = [
    {
        path: '',
        component: NhapKhoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.NhapKho_XEM],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.rSInwardOutward.inward.hometitle'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: NhapKhoUpdateComponent,
        data: {
            authorities: ['ROLE_ADMIN', ROLE.NhapKho_THEM],
            pageTitle: 'ebwebApp.rSInwardOutward.inward.hometitle'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/:rowNum',
        component: NhapKhoUpdateComponent,
        resolve: {
            rsInwardOutward: PPOrderResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.NhapKho_XEM],
            pageTitle: 'ebwebApp.rSInwardOutward.inward.hometitle'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'nhap-kho/:id/edit',
        component: NhapKhoUpdateComponent,
        resolve: {
            rsInwardOutward: PPOrderResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.NhapKho_XEM],
            pageTitle: 'ebwebApp.rSInwardOutward.inward.hometitle'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: NhapKhoUpdateComponent,
        resolve: {
            rsInwardOutward: PPOrderResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.NhapKho_XEM],
            pageTitle: 'ebwebApp.rSInwardOutward.inward.hometitle'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const NhapKhoPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: NhapKhoDeletePopupComponent,
        resolve: {
            ppOrder: PPOrderResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.NhapKho_XEM],
            pageTitle: 'ebwebApp.rSInwardOutward.inward.hometitle'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
