import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ContractState } from 'app/shared/model/contract-state.model';
import { ContractStateService } from './contract-state.service';
import { ContractStateComponent } from './contract-state.component';
import { ContractStateDetailComponent } from './contract-state-detail.component';
import { ContractStateUpdateComponent } from './contract-state-update.component';
import { ContractStateDeletePopupComponent } from './contract-state-delete-dialog.component';
import { IContractState } from 'app/shared/model/contract-state.model';

@Injectable({ providedIn: 'root' })
export class ContractStateResolve implements Resolve<IContractState> {
    constructor(private service: ContractStateService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((contractState: HttpResponse<ContractState>) => contractState.body));
        }
        return of(new ContractState());
    }
}

export const contractStateRoute: Routes = [
    {
        path: 'contract-state',
        component: ContractStateComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.contractState.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'contract-state/:id/view',
        component: ContractStateDetailComponent,
        resolve: {
            contractState: ContractStateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.contractState.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'contract-state/new',
        component: ContractStateUpdateComponent,
        resolve: {
            contractState: ContractStateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.contractState.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'contract-state/:id/edit',
        component: ContractStateUpdateComponent,
        resolve: {
            contractState: ContractStateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.contractState.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const contractStatePopupRoute: Routes = [
    {
        path: 'contract-state/:id/delete',
        component: ContractStateDeletePopupComponent,
        resolve: {
            contractState: ContractStateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.contractState.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
