import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { EMContract } from 'app/shared/model/em-contract.model';
import { EMContractService } from './em-contract.service';
import { EMContractComponent } from './em-contract.component';
import { EMContractDetailComponent } from './em-contract-detail.component';
import { EMContractUpdateComponent } from './em-contract-update.component';
import { EMContractDeletePopupComponent } from './em-contract-delete-dialog.component';
import { IEMContract } from 'app/shared/model/em-contract.model';

@Injectable({ providedIn: 'root' })
export class EMContractResolve implements Resolve<IEMContract> {
    constructor(private service: EMContractService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((eMContract: HttpResponse<EMContract>) => eMContract.body));
        }
        return of(new EMContract());
    }
}

export const eMContractRoute: Routes = [
    {
        path: 'em-contract',
        component: EMContractComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.eMContract.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'em-contract/:id/view',
        component: EMContractDetailComponent,
        resolve: {
            eMContract: EMContractResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.eMContract.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'em-contract/new',
        component: EMContractUpdateComponent,
        resolve: {
            eMContract: EMContractResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.eMContract.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'em-contract/:id/edit',
        component: EMContractUpdateComponent,
        resolve: {
            eMContract: EMContractResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.eMContract.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const eMContractPopupRoute: Routes = [
    {
        path: 'em-contract/:id/delete',
        component: EMContractDeletePopupComponent,
        resolve: {
            eMContract: EMContractResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.eMContract.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
