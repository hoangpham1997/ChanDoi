import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { StatisticsCode } from 'app/shared/model/statistics-code.model';
import { StatisticsCodeService } from './statistics-code.service';
import { StatisticsCodeComponent } from './statistics-code.component';
import { StatisticsCodeDetailComponent } from './statistics-code-detail.component';
import { StatisticsCodeUpdateComponent } from './statistics-code-update.component';
import { StatisticsCodeDeletePopupComponent } from './statistics-code-delete-dialog.component';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';

@Injectable({ providedIn: 'root' })
export class StatisticsCodeResolve implements Resolve<IStatisticsCode> {
    constructor(private service: StatisticsCodeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((statisticsCode: HttpResponse<StatisticsCode>) => statisticsCode.body));
        }
        return of(new StatisticsCode());
    }
}

export const statisticsCodeRoute: Routes = [
    {
        path: 'statistics-code',
        component: StatisticsCodeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.statisticsCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'statistics-code/:id/view',
        component: StatisticsCodeDetailComponent,
        resolve: {
            statisticsCode: StatisticsCodeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.statisticsCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'statistics-code/new',
        component: StatisticsCodeUpdateComponent,
        resolve: {
            statisticsCode: StatisticsCodeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.statisticsCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'statistics-code/:id/edit',
        component: StatisticsCodeUpdateComponent,
        resolve: {
            statisticsCode: StatisticsCodeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.statisticsCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const statisticsCodePopupRoute: Routes = [
    {
        path: 'statistics-code/:id/delete',
        component: StatisticsCodeDeletePopupComponent,
        resolve: {
            statisticsCode: StatisticsCodeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.statisticsCode.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
