import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CPPeriod } from 'app/shared/model/cp-period.model';
import { PpHeSoService } from './pp-he-so.service';
import { PpHeSoComponent } from './pp-he-so.component';
import { PpHeSoUpdateComponent } from './pp-he-so-update.component';
import { ICPPeriod } from 'app/shared/model/cp-period.model';

@Injectable({ providedIn: 'root' })
export class CPPeriodResolve implements Resolve<ICPPeriod> {
    constructor(private service: PpHeSoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.getCPPeriodByID({ id }).pipe(map((cPPeriod: HttpResponse<CPPeriod>) => cPPeriod.body));
        }
        return of(new CPPeriod());
    }
}

export const ppHeSoRoute: Routes = [
    {
        path: 'he-so',
        component: PpHeSoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.cPPeriod.home.titleHeSo'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'he-so/new',
        component: PpHeSoUpdateComponent,
        resolve: {
            cPPeriod: CPPeriodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPPeriod.home.titleHeSo'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'he-so/:id/edit',
        component: PpHeSoUpdateComponent,
        resolve: {
            cPPeriod: CPPeriodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPPeriod.home.titleHeSo'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'ty-le',
        component: PpHeSoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.cPPeriod.home.titleTyLe'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'ty-le/new',
        component: PpHeSoUpdateComponent,
        resolve: {
            cPPeriod: CPPeriodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPPeriod.home.titleTyLe'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'ty-le/:id/edit',
        component: PpHeSoUpdateComponent,
        resolve: {
            cPPeriod: CPPeriodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPPeriod.home.titleTyLe'
        },
        canActivate: [UserRouteAccessService]
    }
];
