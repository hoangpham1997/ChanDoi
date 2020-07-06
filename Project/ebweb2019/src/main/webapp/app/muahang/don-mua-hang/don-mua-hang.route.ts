import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { DonMuaHangComponent } from './don-mua-hang.component';
import { DonMuaHangUpdateComponent } from 'app/muahang/don-mua-hang/don-mua-hang-update.component';
import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Pporder } from 'app/shared/model/pporder.model';
import { PporderService } from 'app/entities/pporder';
import { map } from 'rxjs/operators';
import { of } from 'rxjs';
import { DonMuaHangDeletePopupComponent } from 'app/muahang/don-mua-hang/don-mua-hang-delete-dialog.component';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class PPOrderResolve implements Resolve<Pporder> {
    constructor(private service: PporderService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.findById(id).pipe(map((mBTellerPaper: HttpResponse<Pporder>) => mBTellerPaper.body));
        }
        return of(new Pporder());
    }
}

export const DonMuaHangRoute: Routes = [
    {
        path: '',
        component: DonMuaHangComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DonMuaHang_XEM],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.purchaseOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DonMuaHangUpdateComponent,
        resolve: {
            ppOrder: PPOrderResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DonMuaHang_THEM],
            pageTitle: 'ebwebApp.purchaseOrder.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/:rowNum',
        component: DonMuaHangUpdateComponent,
        resolve: {
            ppOrder: PPOrderResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DonMuaHang_XEM],
            pageTitle: 'ebwebApp.purchaseOrder.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'don-mua-hang/:id/edit',
        component: DonMuaHangUpdateComponent,
        resolve: {
            ppOrder: PPOrderResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DonMuaHang_XEM],
            pageTitle: 'ebwebApp.purchaseOrder.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: DonMuaHangUpdateComponent,
        resolve: {
            ppOrder: PPOrderResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DonMuaHang_XEM],
            pageTitle: 'ebwebApp.purchaseOrder.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const DonMuaHangPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DonMuaHangDeletePopupComponent,
        resolve: {
            ppOrder: PPOrderResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DonMuaHang_XEM],
            pageTitle: 'ebwebApp.mBTellerPaper.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
