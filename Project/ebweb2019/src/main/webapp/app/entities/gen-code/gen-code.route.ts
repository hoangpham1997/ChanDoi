import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { GenCode } from 'app/shared/model/gen-code.model';
import { GenCodeService } from './gen-code.service';
import { GenCodeComponent } from './gen-code.component';
import { GenCodeDetailComponent } from './gen-code-detail.component';
import { GenCodeUpdateComponent } from './gen-code-update.component';
import { GenCodeDeletePopupComponent } from './gen-code-delete-dialog.component';
import { IGenCode } from 'app/shared/model/gen-code.model';

@Injectable({ providedIn: 'root' })
export class GenCodeResolve implements Resolve<IGenCode> {
    constructor(private service: GenCodeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((genCode: HttpResponse<GenCode>) => genCode.body));
        }
        return of(new GenCode());
    }
}

export const genCodeRoute: Routes = [
    {
        path: 'gen-code',
        component: GenCodeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.genCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'gen-code/:id/view',
        component: GenCodeDetailComponent,
        resolve: {
            genCode: GenCodeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.genCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'gen-code/new',
        component: GenCodeUpdateComponent,
        resolve: {
            genCode: GenCodeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.genCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'gen-code/:id/edit',
        component: GenCodeUpdateComponent,
        resolve: {
            genCode: GenCodeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.genCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const genCodePopupRoute: Routes = [
    {
        path: 'gen-code/:id/delete',
        component: GenCodeDeletePopupComponent,
        resolve: {
            genCode: GenCodeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.genCode.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
