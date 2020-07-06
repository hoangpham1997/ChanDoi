import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IMCReceipt, MCReceipt } from 'app/shared/model/mc-receipt.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';
import { GhiTangDeleteDialogComponent } from 'app/congcudungcu/ghi-tang-ccdc-khac/ghi-tang-delete-dialog.component';
import { GhiTangService } from 'app/congcudungcu/ghi-tang-ccdc-khac/ghi-tang.service';
import { GhiTangComponent } from 'app/congcudungcu/ghi-tang-ccdc-khac/ghi-tang.component';
import { GhiTangDetailComponent } from 'app/congcudungcu/ghi-tang-ccdc-khac/ghi-tang-detail.component';
import { GhiTangUpdateComponent } from 'app/congcudungcu/ghi-tang-ccdc-khac/ghi-tang-update.component';
import { TIIncrement } from 'app/shared/model/ti-increment.model';

@Injectable({ providedIn: 'root' })
export class GhiTangResolve implements Resolve<IMCReceipt> {
    constructor(private service: GhiTangService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            const a = this.service.find(id).pipe(map((tiIncrement: HttpResponse<TIIncrement>) => tiIncrement.body));
            return this.service.find(id).pipe(map((tiIncrement: HttpResponse<TIIncrement>) => tiIncrement.body));
        }
        return of(new MCReceipt());
    }
}

export const ghiTangRoute: Routes = [
    {
        path: '',
        component: GhiTangComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuThu_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.tIIncrement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'hasSearch/:isSearch',
        component: GhiTangComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuThu_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.tIIncrement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: GhiTangDetailComponent,
        resolve: {
            tiIncrement: GhiTangResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuThu_Xem],
            pageTitle: 'ebwebApp.tIIncrement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: GhiTangUpdateComponent,
        resolve: {
            tiIncrement: GhiTangResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuThu_Xem],
            pageTitle: 'ebwebApp.tIIncrement.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: GhiTangUpdateComponent,
        resolve: {
            tiIncrement: GhiTangResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuThu_Xem],
            pageTitle: 'ebwebApp.tIIncrement.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: GhiTangUpdateComponent,
        resolve: {
            tiIncrement: GhiTangResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuThu_Xem],
            pageTitle: 'ebwebApp.tIIncrement.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const mCReceiptPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: GhiTangDeleteDialogComponent,
        resolve: {
            tiIncrement: GhiTangResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.tIIncrement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
