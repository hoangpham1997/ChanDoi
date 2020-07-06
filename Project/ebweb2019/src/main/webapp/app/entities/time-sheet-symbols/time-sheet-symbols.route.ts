import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { TimeSheetSymbols } from 'app/shared/model/time-sheet-symbols.model';
import { TimeSheetSymbolsService } from './time-sheet-symbols.service';
import { TimeSheetSymbolsComponent } from './time-sheet-symbols.component';
import { TimeSheetSymbolsDetailComponent } from './time-sheet-symbols-detail.component';
import { TimeSheetSymbolsUpdateComponent } from './time-sheet-symbols-update.component';
import { TimeSheetSymbolsDeletePopupComponent } from './time-sheet-symbols-delete-dialog.component';
import { ITimeSheetSymbols } from 'app/shared/model/time-sheet-symbols.model';

@Injectable({ providedIn: 'root' })
export class TimeSheetSymbolsResolve implements Resolve<ITimeSheetSymbols> {
    constructor(private service: TimeSheetSymbolsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((timeSheetSymbols: HttpResponse<TimeSheetSymbols>) => timeSheetSymbols.body));
        }
        return of(new TimeSheetSymbols());
    }
}

export const timeSheetSymbolsRoute: Routes = [
    {
        path: 'time-sheet-symbols',
        component: TimeSheetSymbolsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.timeSheetSymbols.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'time-sheet-symbols/:id/view',
        component: TimeSheetSymbolsDetailComponent,
        resolve: {
            timeSheetSymbols: TimeSheetSymbolsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.timeSheetSymbols.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'time-sheet-symbols/new',
        component: TimeSheetSymbolsUpdateComponent,
        resolve: {
            timeSheetSymbols: TimeSheetSymbolsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.timeSheetSymbols.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'time-sheet-symbols/:id/edit',
        component: TimeSheetSymbolsUpdateComponent,
        resolve: {
            timeSheetSymbols: TimeSheetSymbolsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.timeSheetSymbols.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const timeSheetSymbolsPopupRoute: Routes = [
    {
        path: 'time-sheet-symbols/:id/delete',
        component: TimeSheetSymbolsDeletePopupComponent,
        resolve: {
            timeSheetSymbols: TimeSheetSymbolsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.timeSheetSymbols.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
