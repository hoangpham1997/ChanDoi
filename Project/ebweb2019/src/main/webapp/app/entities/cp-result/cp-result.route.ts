import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CPResult } from 'app/shared/model/cp-result.model';
import { CPResultService } from './cp-result.service';
import { CPResultComponent } from './cp-result.component';
import { CPResultDetailComponent } from './cp-result-detail.component';
import { CPResultUpdateComponent } from './cp-result-update.component';
import { CPResultDeletePopupComponent } from './cp-result-delete-dialog.component';
import { ICPResult } from 'app/shared/model/cp-result.model';

@Injectable({ providedIn: 'root' })
export class CPResultResolve implements Resolve<ICPResult> {
    constructor(private service: CPResultService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((cPResult: HttpResponse<CPResult>) => cPResult.body));
        }
        return of(new CPResult());
    }
}

export const cPResultRoute: Routes = [
    {
        path: 'cp-result',
        component: CPResultComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.cPResult.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cp-result/:id/view',
        component: CPResultDetailComponent,
        resolve: {
            cPResult: CPResultResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPResult.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cp-result/new',
        component: CPResultUpdateComponent,
        resolve: {
            cPResult: CPResultResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPResult.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cp-result/:id/edit',
        component: CPResultUpdateComponent,
        resolve: {
            cPResult: CPResultResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPResult.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cPResultPopupRoute: Routes = [
    {
        path: 'cp-result/:id/delete',
        component: CPResultDeletePopupComponent,
        resolve: {
            cPResult: CPResultResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPResult.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
