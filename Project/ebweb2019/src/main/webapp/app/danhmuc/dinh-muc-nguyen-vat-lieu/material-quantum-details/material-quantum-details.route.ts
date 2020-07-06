import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MaterialQuantumDetails } from 'app/shared/model/material-quantum-details.model';
import { MaterialQuantumDetailsService } from './material-quantum-details.service';
import { MaterialQuantumDetailsComponent } from './material-quantum-details.component';
import { MaterialQuantumDetailsDetailComponent } from './material-quantum-details-detail.component';
import { MaterialQuantumDetailsUpdateComponent } from './material-quantum-details-update.component';
import { MaterialQuantumDetailsDeletePopupComponent } from './material-quantum-details-delete-dialog.component';
import { IMaterialQuantumDetails } from 'app/shared/model/material-quantum-details.model';

@Injectable({ providedIn: 'root' })
export class MaterialQuantumDetailsResolve implements Resolve<IMaterialQuantumDetails> {
    constructor(private service: MaterialQuantumDetailsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .find(id)
                .pipe(map((materialQuantumDetails: HttpResponse<MaterialQuantumDetails>) => materialQuantumDetails.body));
        }
        return of(new MaterialQuantumDetails());
    }
}

export const materialQuantumDetailsRoute: Routes = [
    {
        path: 'material-quantum-details',
        component: MaterialQuantumDetailsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.materialQuantumDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-quantum-details/:id/view',
        component: MaterialQuantumDetailsDetailComponent,
        resolve: {
            materialQuantumDetails: MaterialQuantumDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialQuantumDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-quantum-details/new',
        component: MaterialQuantumDetailsUpdateComponent,
        resolve: {
            materialQuantumDetails: MaterialQuantumDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialQuantumDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'material-quantum-details/:id/edit',
        component: MaterialQuantumDetailsUpdateComponent,
        resolve: {
            materialQuantumDetails: MaterialQuantumDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialQuantumDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const materialQuantumDetailsPopupRoute: Routes = [
    {
        path: 'material-quantum-details/:id/delete',
        component: MaterialQuantumDetailsDeletePopupComponent,
        resolve: {
            materialQuantumDetails: MaterialQuantumDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.materialQuantumDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
