import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SAQuoteDetails } from 'app/shared/model/sa-quote-details.model';
import { SAQuoteDetailsService } from './sa-quote-details.service';
import { SAQuoteDetailsComponent } from './sa-quote-details.component';
import { SAQuoteDetailsDetailComponent } from './sa-quote-details-detail.component';
import { SAQuoteDetailsUpdateComponent } from './sa-quote-details-update.component';
import { SAQuoteDetailsDeletePopupComponent } from './sa-quote-details-delete-dialog.component';
import { ISAQuoteDetails } from 'app/shared/model/sa-quote-details.model';

@Injectable({ providedIn: 'root' })
export class SAQuoteDetailsResolve implements Resolve<ISAQuoteDetails> {
    constructor(private service: SAQuoteDetailsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((sAQuoteDetails: HttpResponse<SAQuoteDetails>) => sAQuoteDetails.body));
        }
        return of(new SAQuoteDetails());
    }
}

export const sAQuoteDetailsRoute: Routes = [
    {
        path: '',
        component: SAQuoteDetailsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.sAQuoteDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SAQuoteDetailsDetailComponent,
        resolve: {
            sAQuoteDetails: SAQuoteDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.sAQuoteDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: SAQuoteDetailsUpdateComponent,
        resolve: {
            sAQuoteDetails: SAQuoteDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.sAQuoteDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SAQuoteDetailsUpdateComponent,
        resolve: {
            sAQuoteDetails: SAQuoteDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.sAQuoteDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sAQuoteDetailsPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SAQuoteDetailsDeletePopupComponent,
        resolve: {
            sAQuoteDetails: SAQuoteDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.sAQuoteDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
