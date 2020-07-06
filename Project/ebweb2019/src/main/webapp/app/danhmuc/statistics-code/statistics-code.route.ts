import { IStatisticsCode, StatisticsCode } from 'app/shared/model/statistics-code.model';
import { Injectable } from '@angular/core';
import { UserRouteAccessService } from 'app/core';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { StatisticsCodeUpdateComponent } from 'app/danhmuc/statistics-code/statistics-code-update.component';
import { StatisticsCodeService } from 'app/danhmuc/statistics-code/statistics-code.service';
import { StatisticsCodeDeletePopupComponent } from 'app/danhmuc/statistics-code/statistics-code-delete-dialog.component';
import { StatisticsCodeComponent } from 'app/danhmuc/statistics-code/statistics-code.component';
import { map } from 'rxjs/operators';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class StatisticsCodeResolve implements Resolve<IStatisticsCode> {
    constructor(private service: StatisticsCodeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((statisticsCode: HttpResponse<StatisticsCode>) => statisticsCode.body));
        }
        return new StatisticsCode();
    }
}

export const statisticsCodeRoute: Routes = [
    {
        path: '',
        component: StatisticsCodeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucMaThongKe_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.statisticsCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: StatisticsCodeUpdateComponent,
        resolve: {
            statisticsCode: StatisticsCodeResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucMaThongKe_Them],
            pageTitle: 'ebwebApp.statisticsCode.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: StatisticsCodeUpdateComponent,
        resolve: {
            statisticsCode: StatisticsCodeResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucMaThongKe_Sua],
            pageTitle: 'ebwebApp.statisticsCode.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const statisticsCodePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: StatisticsCodeDeletePopupComponent,
        resolve: {
            statisticsCode: StatisticsCodeResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucMaThongKe_Xoa],
            pageTitle: 'ebwebApp.statisticsCode.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
