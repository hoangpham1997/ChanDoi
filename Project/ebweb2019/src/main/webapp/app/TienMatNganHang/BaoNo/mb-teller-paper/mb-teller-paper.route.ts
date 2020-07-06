import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { MBTellerPaperService } from './mb-teller-paper.service';
import { MBTellerPaperComponent } from './mb-teller-paper.component';
import { MBTellerPaperDetailComponent } from './mb-teller-paper-detail.component';
import { MBTellerPaperUpdateComponent } from './mb-teller-paper-update.component';
import { MBTellerPaperDeletePopupComponent } from './mb-teller-paper-delete-dialog.component';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';
import { ROLE_ADMIN } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class MBTellerPaperResolve implements Resolve<IMBTellerPaper> {
    constructor(private service: MBTellerPaperService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((mBTellerPaper: HttpResponse<MBTellerPaper>) => mBTellerPaper.body));
        }
        return of(new MBTellerPaper());
    }
}

export const mBTellerPaperRoute: Routes = [
    {
        path: '',
        component: MBTellerPaperComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoNo_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.mBTellerPaper.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: MBTellerPaperDetailComponent,
        resolve: {
            mBTellerPaper: MBTellerPaperResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBTellerPaper.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: MBTellerPaperUpdateComponent,
        resolve: {
            mBTellerPaper: MBTellerPaperResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoNo_Them],
            pageTitle: 'ebwebApp.mBTellerPaper.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: MBTellerPaperUpdateComponent,
        resolve: {
            mBTellerPaper: MBTellerPaperResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoNo_Sua],
            pageTitle: 'ebwebApp.mBTellerPaper.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: MBTellerPaperUpdateComponent,
        resolve: {
            mBTellerPaper: MBTellerPaperResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoNo_Sua],
            pageTitle: 'ebwebApp.mBTellerPaper.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const mBTellerPaperPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MBTellerPaperDeletePopupComponent,
        resolve: {
            mBTellerPaper: MBTellerPaperResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoNo_Xoa],
            pageTitle: 'ebwebApp.mBTellerPaper.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
