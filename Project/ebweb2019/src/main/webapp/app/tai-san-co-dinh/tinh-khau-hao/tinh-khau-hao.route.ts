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
import { GhiTangComponent } from 'app/tai-san-co-dinh/ghi-tang/ghi-tang.component';
import { GhiTangService } from 'app/tai-san-co-dinh/ghi-tang/ghi-tang.service';
import { GhiTangDetailComponent } from 'app/tai-san-co-dinh/ghi-tang/ghi-tang-detail.component';
import { GhiTangUpdateComponent } from 'app/tai-san-co-dinh/ghi-tang/ghi-tang-update.component';
import { GhiTangDeleteDialogComponent } from 'app/tai-san-co-dinh/ghi-tang/ghi-tang-delete-dialog.component';
import { FAIncrement } from 'app/shared/model/fa-increment.model';
import { TinhKhauHaoComponent } from 'app/tai-san-co-dinh/tinh-khau-hao/tinh-khau-hao.component';
import { TinhKhauHaoUpdateComponent } from 'app/tai-san-co-dinh/tinh-khau-hao/tinh-khau-hao-update.component';

@Injectable({ providedIn: 'root' })
export class TinhKhauHaoRoute implements Resolve<IMCReceipt> {
    constructor(private service: GhiTangService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            // const a = this.service.find(id).pipe(map((tiIncrement: HttpResponse<TIIncrement>) => tiIncrement.body));
            return this.service.find(id).pipe(map((faIncrement: HttpResponse<FAIncrement>) => faIncrement.body));
        }
        return of(new MCReceipt());
    }
}

export const tinhKhauHaoRoute: Routes = [
    {
        path: '',
        component: TinhKhauHaoComponent,
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
        path: 'new',
        component: TinhKhauHaoUpdateComponent,
        resolve: {
            faIncrement: TinhKhauHaoRoute
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
        component: TinhKhauHaoUpdateComponent,
        resolve: {
            faIncrement: TinhKhauHaoRoute
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
        component: TinhKhauHaoUpdateComponent,
        resolve: {
            faIncrement: TinhKhauHaoRoute
        },
        data: {
            authorities: ['ROLE_USER', ROLE.PhieuThu_Xem],
            pageTitle: 'ebwebApp.tIIncrement.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];
