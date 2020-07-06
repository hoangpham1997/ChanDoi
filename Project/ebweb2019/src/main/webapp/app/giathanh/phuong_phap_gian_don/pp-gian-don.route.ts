import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CPPeriod } from 'app/shared/model/cp-period.model';
import { PpGianDonService } from './pp-gian-don.service';
import { PpGianDonComponent } from './pp-gian-don.component';
import { PpGianDonUpdateComponent } from './pp-gian-don-update.component';
import { ICPPeriod } from 'app/shared/model/cp-period.model';

@Injectable({ providedIn: 'root' })
export class CPPeriodResolve implements Resolve<ICPPeriod> {
    constructor(private service: PpGianDonService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.getCPPeriodByID({ id }).pipe(map((cPPeriod: HttpResponse<CPPeriod>) => cPPeriod.body));
        }
        return of(new CPPeriod());
    }
}

export const ppGianDonRoute: Routes = [
    {
        path: 'gian-don',
        component: PpGianDonComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.cPPeriod.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'gian-don/new',
        component: PpGianDonUpdateComponent,
        resolve: {
            cPPeriod: CPPeriodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPPeriod.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'gian-don/:id/edit',
        component: PpGianDonUpdateComponent,
        resolve: {
            cPPeriod: CPPeriodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPPeriod.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
