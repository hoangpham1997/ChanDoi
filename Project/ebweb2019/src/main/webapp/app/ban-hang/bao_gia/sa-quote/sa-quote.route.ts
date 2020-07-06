import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SAQuote } from 'app/shared/model/sa-quote.model';
import { SAQuoteService } from './sa-quote.service';
import { SAQuoteComponent } from './sa-quote.component';
import { SAQuoteDetailComponent } from './sa-quote-detail.component';
import { SAQuoteUpdateComponent } from './sa-quote-update.component';
import { SAQuoteDeletePopupComponent } from './sa-quote-delete-dialog.component';
import { ISAQuote } from 'app/shared/model/sa-quote.model';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class SAQuoteResolve implements Resolve<ISAQuote> {
    constructor(private service: SAQuoteService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((sAQuote: HttpResponse<SAQuote>) => sAQuote.body));
        }
        return of(new SAQuote());
    }
}

export const sAQuoteRoute: Routes = [
    {
        path: '',
        component: SAQuoteComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoGia_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.sAQuote.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SAQuoteDetailComponent,
        resolve: {
            sAQuote: SAQuoteResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.sAQuote.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: SAQuoteUpdateComponent,
        resolve: {
            sAQuote: SAQuoteResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoGia_Them],
            pageTitle: 'ebwebApp.sAQuote.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: SAQuoteUpdateComponent,
        resolve: {
            sAQuote: SAQuoteResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoNo_Sua],
            pageTitle: 'ebwebApp.sAQuote.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: SAQuoteUpdateComponent,
        resolve: {
            sAQuote: SAQuoteResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoNo_Sua],
            pageTitle: 'ebwebApp.sAQuote.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const sAQuotePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SAQuoteDeletePopupComponent,
        resolve: {
            sAQuote: SAQuoteResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.BaoNo_Xoa],
            pageTitle: 'ebwebApp.sAQuote.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
