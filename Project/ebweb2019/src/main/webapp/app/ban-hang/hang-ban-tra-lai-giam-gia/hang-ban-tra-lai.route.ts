import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { HangBanTraLaiComponent } from './hang-ban-tra-lai.component';
import { SaReturn } from 'app/shared/model/sa-return.model';
import { SaReturnService } from 'app/ban-hang/hang-ban-tra-lai-giam-gia/sa-return.service';
import { HangBanTraLaiUpdateComponent } from 'app/ban-hang/hang-ban-tra-lai-giam-gia/hang-ban-tra-lai-update.component';
import { HangBanTraLainDeletePopupComponent } from 'app/ban-hang/hang-ban-tra-lai-giam-gia/hang-ban-tra-lai-delete-dialog.component';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class HangBanTraLaiResolve implements Resolve<any> {
    constructor(private service: SaReturnService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        if (route.routeConfig.path === 'tra-lai/new' || route.routeConfig.path === 'giam-gia/new') {
            return of(new SaReturn());
        } else {
            const id = route.params['id'] ? route.params['id'] : null;
            return this.service.findById(id).pipe(map((saReturn: HttpResponse<any>) => saReturn.body));
        }
    }
}

export const hangBanTraLaiRoute: Routes = [
    {
        path: 'tra-lai',
        component: HangBanTraLaiComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.HangBanTraLai_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.saReturn.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'giam-gia',
        component: HangBanTraLaiComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.HangBanGiamGia_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.saReturn.home.title2'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tra-lai/new',
        component: HangBanTraLaiUpdateComponent,
        resolve: {
            saReturn: HangBanTraLaiResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.HangBanTraLai_Them],
            pageTitle: 'ebwebApp.saReturn.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'giam-gia/new',
        component: HangBanTraLaiUpdateComponent,
        resolve: {
            saReturn: HangBanTraLaiResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.HangBanGiamGia_Them],
            pageTitle: 'ebwebApp.saReturn.home.title2'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'tra-lai/:id/edit',
        component: HangBanTraLaiUpdateComponent,
        resolve: {
            saReturn: HangBanTraLaiResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.HangBanTraLai_Sua],
            pageTitle: 'ebwebApp.saReturn.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'tra-lai/:id/edit/:rowNum',
        component: HangBanTraLaiUpdateComponent,
        resolve: {
            saReturn: HangBanTraLaiResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.HangBanTraLai_Sua],
            pageTitle: 'ebwebApp.saReturn.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'tra-lai/:id/edit-rs-inward',
        component: HangBanTraLaiUpdateComponent,
        resolve: {
            saReturn: HangBanTraLaiResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.saReturn.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'giam-gia/:id/edit',
        component: HangBanTraLaiUpdateComponent,
        resolve: {
            saReturn: HangBanTraLaiResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.HangBanGiamGia_Sua],
            pageTitle: 'ebwebApp.saReturn.home.title2'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'giam-gia/:id/edit/:rowNum',
        component: HangBanTraLaiUpdateComponent,
        resolve: {
            saReturn: HangBanTraLaiResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.HangBanGiamGia_Sua],
            pageTitle: 'ebwebApp.saReturn.home.title2'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'giam-gia/:id/edit/from-ref',
        component: HangBanTraLaiUpdateComponent,
        resolve: {
            saReturn: HangBanTraLaiResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.saReturn.home.title2'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'tra-lai/:id/edit/from-ref',
        component: HangBanTraLaiUpdateComponent,
        resolve: {
            saReturn: HangBanTraLaiResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.saReturn.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: 'tra-lai/:id/edit/from-einvoice',
        component: HangBanTraLaiUpdateComponent,
        resolve: {
            saReturn: HangBanTraLaiResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.saReturn.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];
export const hangBanTraLaiPopupRoute: Routes = [
    {
        path: 'tra-lai/:id/delete',
        component: HangBanTraLainDeletePopupComponent,
        resolve: {
            deleteDTO: HangBanTraLaiResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.HangBanTraLai_Xoa],
            pageTitle: 'ebwebApp.saReturn.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'giam-gia/:id/delete',
        component: HangBanTraLainDeletePopupComponent,
        resolve: {
            deleteDTO: HangBanTraLaiResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.HangBanGiamGia_Xoa],
            pageTitle: 'ebwebApp.saReturn.home.title2'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
