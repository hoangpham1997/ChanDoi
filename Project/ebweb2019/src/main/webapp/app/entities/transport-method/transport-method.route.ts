import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { TransportMethod } from 'app/shared/model/transport-method.model';
import { TransportMethodService } from './transport-method.service';
import { TransportMethodComponent } from './transport-method.component';
import { TransportMethodDetailComponent } from './transport-method-detail.component';
import { TransportMethodUpdateComponent } from './transport-method-update.component';
import { TransportMethodDeletePopupComponent } from './transport-method-delete-dialog.component';
import { ITransportMethod } from 'app/shared/model/transport-method.model';

@Injectable({ providedIn: 'root' })
export class TransportMethodResolve implements Resolve<ITransportMethod> {
    constructor(private service: TransportMethodService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((transportMethod: HttpResponse<TransportMethod>) => transportMethod.body));
        }
        return of(new TransportMethod());
    }
}

export const transportMethodRoute: Routes = [
    {
        path: 'transport-method',
        component: TransportMethodComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.transportMethod.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'transport-method/:id/view',
        component: TransportMethodDetailComponent,
        resolve: {
            transportMethod: TransportMethodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.transportMethod.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'transport-method/new',
        component: TransportMethodUpdateComponent,
        resolve: {
            transportMethod: TransportMethodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.transportMethod.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'transport-method/:id/edit',
        component: TransportMethodUpdateComponent,
        resolve: {
            transportMethod: TransportMethodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.transportMethod.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const transportMethodPopupRoute: Routes = [
    {
        path: 'transport-method/:id/delete',
        component: TransportMethodDeletePopupComponent,
        resolve: {
            transportMethod: TransportMethodResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.transportMethod.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
