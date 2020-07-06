import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { RepositoryLedger } from 'app/shared/model/repository-ledger.model';
import { RepositoryLedgerService } from './repository-ledger.service';
import { RepositoryLedgerComponent } from './repository-ledger.component';
import { RepositoryLedgerDetailComponent } from './repository-ledger-detail.component';
import { RepositoryLedgerUpdateComponent } from './repository-ledger-update.component';
import { RepositoryLedgerDeletePopupComponent } from './repository-ledger-delete-dialog.component';
import { IRepositoryLedger } from 'app/shared/model/repository-ledger.model';

@Injectable({ providedIn: 'root' })
export class RepositoryLedgerResolve implements Resolve<IRepositoryLedger> {
    constructor(private service: RepositoryLedgerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((repositoryLedger: HttpResponse<RepositoryLedger>) => repositoryLedger.body));
        }
        return of(new RepositoryLedger());
    }
}

export const repositoryLedgerRoute: Routes = [
    {
        path: 'repository-ledger',
        component: RepositoryLedgerComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.repositoryLedger.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'repository-ledger/:id/view',
        component: RepositoryLedgerDetailComponent,
        resolve: {
            repositoryLedger: RepositoryLedgerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.repositoryLedger.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'repository-ledger/new',
        component: RepositoryLedgerUpdateComponent,
        resolve: {
            repositoryLedger: RepositoryLedgerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.repositoryLedger.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'repository-ledger/:id/edit',
        component: RepositoryLedgerUpdateComponent,
        resolve: {
            repositoryLedger: RepositoryLedgerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.repositoryLedger.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const repositoryLedgerPopupRoute: Routes = [
    {
        path: 'repository-ledger/:id/delete',
        component: RepositoryLedgerDeletePopupComponent,
        resolve: {
            repositoryLedger: RepositoryLedgerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.repositoryLedger.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
