import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AutoPrinciple } from 'app/shared/model/auto-principle.model';
import { AutoPrincipleService } from './auto-principle.service';
import { AutoPrincipleComponent } from './auto-principle.component';
import { AutoPrincipleDetailComponent } from './auto-principle-detail.component';
import { AutoPrincipleUpdateComponent } from './auto-principle-update.component';
import { AutoPrincipleDeletePopupComponent } from './auto-principle-delete-dialog.component';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class AutoPrincipleResolve implements Resolve<IAutoPrinciple> {
    constructor(private service: AutoPrincipleService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((autoPrinciple: HttpResponse<AutoPrinciple>) => autoPrinciple.body));
        }
        return of(new AutoPrinciple());
    }
}

export const autoPrincipleRoute: Routes = [
    {
        path: '',
        component: AutoPrincipleComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucDinhKhoanTuDong_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.autoPrinciple.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    // {
    //     path: ':id/view',
    //     component: AutoPrincipleDetailComponent,
    //     resolve: {
    //         autoPrinciple: AutoPrincipleResolve
    //     },
    //     data: {
    //         authorities: ['ROLE_ADMIN', ROLE.DanhMucDinhKhoanTuDong_Xem],
    //         pageTitle: 'ebwebApp.autoPrinciple.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // },
    {
        path: 'new',
        component: AutoPrincipleUpdateComponent,
        resolve: {
            autoPrinciple: AutoPrincipleResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucDinhKhoanTuDong_Them],
            pageTitle: 'ebwebApp.autoPrinciple.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: AutoPrincipleUpdateComponent,
        resolve: {
            autoPrinciple: AutoPrincipleResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucDinhKhoanTuDong_Sua],
            pageTitle: 'ebwebApp.autoPrinciple.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const autoPrinciplePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: AutoPrincipleDeletePopupComponent,
        resolve: {
            autoPrinciple: AutoPrincipleResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucDinhKhoanTuDong_Xoa],
            pageTitle: 'ebwebApp.autoPrinciple.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
