import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Repository } from 'app/shared/model/repository.model';
import { RepositoryService } from './repository.service';
import { RepositoryComponent } from './repository.component';
import { RepositoryDetailComponent } from './repository-detail.component';
import { RepositoryUpdateComponent } from './repository-update.component';
import { RepositoryDeletePopupComponent } from './repository-delete-dialog.component';
import { IRepository } from 'app/shared/model/repository.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class RepositoryResolve implements Resolve<IRepository> {
    constructor(private service: RepositoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((repository: HttpResponse<Repository>) => repository.body));
        }
        return of(new Repository());
    }
}

export const repositoryRoute: Routes = [
    {
        path: '',
        component: RepositoryComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucKho_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.repository.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: RepositoryUpdateComponent,
        resolve: {
            repository: RepositoryResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucKho_Them],
            pageTitle: 'ebwebApp.repository.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: RepositoryUpdateComponent,
        resolve: {
            repository: RepositoryResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucKho_Sua],
            pageTitle: 'ebwebApp.repository.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const repositoryPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: RepositoryDeletePopupComponent,
        resolve: {
            repository: RepositoryResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DanhMucKho_Xoa],
            pageTitle: 'ebwebApp.repository.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
