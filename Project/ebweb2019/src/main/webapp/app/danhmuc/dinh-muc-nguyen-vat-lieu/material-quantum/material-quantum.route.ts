import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MaterialQuantum } from 'app/shared/model/material-quantum.model';
import { MaterialQuantumService } from './material-quantum.service';
import { MaterialQuantumComponent } from './material-quantum.component';
import { MaterialQuantumDetailComponent } from './material-quantum-detail.component';
import { MaterialQuantumUpdateComponent } from './material-quantum-update.component';
import { MaterialQuantumDeletePopupComponent } from './material-quantum-delete-dialog.component';
import { IMaterialQuantum } from 'app/shared/model/material-quantum.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class MaterialQuantumResolve implements Resolve<IMaterialQuantum> {
    constructor(private service: MaterialQuantumService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((materialQuantum: HttpResponse<MaterialQuantum>) => materialQuantum.body));
        }
        return of(new MaterialQuantum());
    }
}

export const materialQuantumRoute: Routes = [
    {
        path: '',
        component: MaterialQuantumComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DinhMucNguyenVatLieu_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.materialQuantum.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: MaterialQuantumDetailComponent,
        resolve: {
            materialQuantum: MaterialQuantumResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DinhMucNguyenVatLieu_Xem],
            pageTitle: 'ebwebApp.materialQuantum.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: MaterialQuantumUpdateComponent,
        resolve: {
            materialQuantum: MaterialQuantumResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DinhMucNguyenVatLieu_Them],
            pageTitle: 'ebwebApp.materialQuantum.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: MaterialQuantumUpdateComponent,
        resolve: {
            materialQuantum: MaterialQuantumResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DinhMucNguyenVatLieu_Sua],
            pageTitle: 'ebwebApp.materialQuantum.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const materialQuantumPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MaterialQuantumDeletePopupComponent,
        resolve: {
            materialQuantum: MaterialQuantumResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.DinhMucNguyenVatLieu_Xoa],
            pageTitle: 'ebwebApp.materialQuantum.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
