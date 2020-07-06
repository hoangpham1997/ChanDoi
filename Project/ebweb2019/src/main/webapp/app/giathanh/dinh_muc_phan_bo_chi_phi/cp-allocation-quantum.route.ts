import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CPAllocationQuantum } from 'app/shared/model/cp-allocation-quantum.model';
import { CPAllocationQuantumService } from './cp-allocation-quantum.service';
import { CPAllocationQuantumComponent } from './cp-allocation-quantum.component';
import { CPAllocationQuantumDetailComponent } from './cp-allocation-quantum-detail.component';
import { CPAllocationQuantumUpdateComponent } from './cp-allocation-quantum-update.component';
import { CPAllocationQuantumDeletePopupComponent } from './cp-allocation-quantum-delete-dialog.component';
import { ICPAllocationQuantum } from 'app/shared/model/cp-allocation-quantum.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class CPAllocationQuantumResolve implements Resolve<ICPAllocationQuantum> {
    constructor(private service: CPAllocationQuantumService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((cPAllocationQuantum: HttpResponse<CPAllocationQuantum>) => cPAllocationQuantum.body));
        }
        return of(new CPAllocationQuantum());
    }
}

export const cPAllocationQuantumRoute: Routes = [
    {
        path: '',
        component: CPAllocationQuantumComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.DinhMucPhanBoChiPhi_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.cPAllocationQuantum.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/view',
        component: CPAllocationQuantumDetailComponent,
        resolve: {
            cPAllocationQuantum: CPAllocationQuantumResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPAllocationQuantum.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: CPAllocationQuantumUpdateComponent,
        resolve: {
            cPAllocationQuantum: CPAllocationQuantumResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPAllocationQuantum.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: CPAllocationQuantumUpdateComponent,
        resolve: {
            cPAllocationQuantum: CPAllocationQuantumResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPAllocationQuantum.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cPAllocationQuantumPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: CPAllocationQuantumDeletePopupComponent,
        resolve: {
            cPAllocationQuantum: CPAllocationQuantumResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.cPAllocationQuantum.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
