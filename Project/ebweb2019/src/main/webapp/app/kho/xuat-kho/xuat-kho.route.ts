import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { XuatKhoComponent } from './xuat-kho.component';
import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { of } from 'rxjs';
import { RSInwardOutward } from 'app/shared/model/rs-inward-outward.model';
import { XuatKhoUpdateComponent } from 'app/kho/xuat-kho/xuat-kho-update.component';
import { XuatKhoDeletePopupComponent } from 'app/kho/xuat-kho/xuat-kho-delete-dialog.component';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE_ADMIN } from 'app/app.constants';
import { ROLE } from 'app/role.constants';
import { RSInwardOutwardService } from 'app/entities/rs-inward-outward/rs-inward-outward.service';

@Injectable({ providedIn: 'root' })
export class RSInwardOutwardResolve implements Resolve<RSInwardOutward> {
    constructor(private service: RSInwardOutwardService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((rsInwardOutward: HttpResponse<RSInwardOutward>) => rsInwardOutward.body));
        }
        return of(new RSInwardOutward());
    }
}

export const XuatKhoRoute: Routes = [
    {
        path: '',
        component: XuatKhoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.XuatKho_XEM],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.outWard.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: XuatKhoUpdateComponent,
        data: {
            authorities: ['ROLE_ADMIN', ROLE.XuatKho_THEM],
            pageTitle: 'ebwebApp.outWard.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/:rowNum',
        component: XuatKhoUpdateComponent,
        resolve: {
            rsInwardOutward: RSInwardOutwardResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.XuatKho_XEM],
            pageTitle: 'ebwebApp.outWard.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'xuat-kho/:id/edit',
        component: XuatKhoUpdateComponent,
        resolve: {
            rsInwardOutward: RSInwardOutwardResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.XuatKho_XEM],
            pageTitle: 'ebwebApp.outWard.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: XuatKhoUpdateComponent,
        resolve: {
            rsInwardOutward: RSInwardOutwardResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.XuatKho_XEM],
            pageTitle: 'ebwebApp.outWard.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const XuatKhoPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: XuatKhoDeletePopupComponent,
        resolve: {
            rsInwardOutward: RSInwardOutwardResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.XuatKho_XEM],
            pageTitle: 'ebwebApp.mBTellerPaper.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
