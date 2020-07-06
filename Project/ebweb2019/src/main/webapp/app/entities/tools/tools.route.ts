import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Tools } from 'app/shared/model/tools.model';
import { ToolsService } from './tools.service';
import { ToolsComponent } from './tools.component';
import { ToolsDetailComponent } from './tools-detail.component';
import { ToolsUpdateComponent } from './tools-update.component';
import { ToolsDeletePopupComponent } from './tools-delete-dialog.component';
import { ITools } from 'app/shared/model/tools.model';

@Injectable({ providedIn: 'root' })
export class ToolsResolve implements Resolve<ITools> {
    constructor(private service: ToolsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((tools: HttpResponse<Tools>) => tools.body));
        }
        return of(new Tools());
    }
}

export const toolsRoute: Routes = [
    {
        path: 'tools',
        component: ToolsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.tools.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tools/:id/view',
        component: ToolsDetailComponent,
        resolve: {
            tools: ToolsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.tools.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tools/new',
        component: ToolsUpdateComponent,
        resolve: {
            tools: ToolsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.tools.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tools/:id/edit',
        component: ToolsUpdateComponent,
        resolve: {
            tools: ToolsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.tools.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const toolsPopupRoute: Routes = [
    {
        path: 'tools/:id/delete',
        component: ToolsDeletePopupComponent,
        resolve: {
            tools: ToolsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.tools.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
