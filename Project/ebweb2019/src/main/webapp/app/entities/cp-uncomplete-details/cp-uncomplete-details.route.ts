import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CPUncompleteDetails } from 'app/shared/model/cp-uncomplete-details.model';
import { CPUncompleteDetailsService } from './cp-uncomplete-details.service';
import { CPUncompleteDetailsComponent } from './cp-uncomplete-details.component';
import { CPUncompleteDetailsDetailComponent } from './cp-uncomplete-details-detail.component';
import { CPUncompleteDetailsUpdateComponent } from './cp-uncomplete-details-update.component';
import { CPUncompleteDetailsDeletePopupComponent } from './cp-uncomplete-details-delete-dialog.component';
import { ICPUncompleteDetails } from 'app/shared/model/cp-uncomplete-details.model';

@Injectable({ providedIn: 'root' })
export class CPUncompleteDetailsResolve implements Resolve<ICPUncompleteDetails> {
    constructor(private service: CPUncompleteDetailsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((cPUncompleteDetails: HttpResponse<CPUncompleteDetails>) => cPUncompleteDetails.body));
        }
        return of(new CPUncompleteDetails());
    }
}

export const cPUncompleteDetailsRoute: Routes = [
    {
        path: 'cp-uncomplete-details',
        component: CPUncompleteDetailsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.cPUncompleteDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cp-uncomplete-details/:id/view',
        component: CPUncompleteDetailsDetailComponent,
        resolve: {
            cPUncompleteDetails: CPUncompleteDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPUncompleteDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cp-uncomplete-details/new',
        component: CPUncompleteDetailsUpdateComponent,
        resolve: {
            cPUncompleteDetails: CPUncompleteDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPUncompleteDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cp-uncomplete-details/:id/edit',
        component: CPUncompleteDetailsUpdateComponent,
        resolve: {
            cPUncompleteDetails: CPUncompleteDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPUncompleteDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cPUncompleteDetailsPopupRoute: Routes = [
    {
        path: 'cp-uncomplete-details/:id/delete',
        component: CPUncompleteDetailsDeletePopupComponent,
        resolve: {
            cPUncompleteDetails: CPUncompleteDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPUncompleteDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
