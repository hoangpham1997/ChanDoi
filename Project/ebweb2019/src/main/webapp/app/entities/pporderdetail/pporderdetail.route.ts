import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IPporderdetail, PPOrderDetail } from 'app/shared/model/pporderdetail.model';
import { PporderdetailService } from './pporderdetail.service';
import { PporderdetailComponent } from './pporderdetail.component';
import { PporderdetailDetailComponent } from './pporderdetail-detail.component';
import { PporderdetailUpdateComponent } from './pporderdetail-update.component';
import { PporderdetailDeletePopupComponent } from './pporderdetail-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class PporderdetailResolve implements Resolve<IPporderdetail> {
    constructor(private service: PporderdetailService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((pporderdetail: HttpResponse<PPOrderDetail>) => pporderdetail.body));
        }
        return of(new PPOrderDetail());
    }
}

export const pporderdetailRoute: Routes = [
    {
        path: 'pporderdetail',
        component: PporderdetailComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.pporderdetail.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pporderdetail/:id/view',
        component: PporderdetailDetailComponent,
        resolve: {
            pporderdetail: PporderdetailResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pporderdetail.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pporderdetail/new',
        component: PporderdetailUpdateComponent,
        resolve: {
            pporderdetail: PporderdetailResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pporderdetail.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pporderdetail/:id/edit',
        component: PporderdetailUpdateComponent,
        resolve: {
            pporderdetail: PporderdetailResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pporderdetail.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pporderdetailPopupRoute: Routes = [
    {
        path: 'pporderdetail/:id/delete',
        component: PporderdetailDeletePopupComponent,
        resolve: {
            pporderdetail: PporderdetailResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pporderdetail.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
