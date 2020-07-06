import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { MBTellerPaperDetails } from 'app/shared/model/mb-teller-paper-details.model';
import { MBTellerPaperDetailsService } from './mb-teller-paper-details.service';
import { MBTellerPaperDetailsComponent } from './mb-teller-paper-details.component';
import { MBTellerPaperDetailsDetailComponent } from './mb-teller-paper-details-detail.component';
import { MBTellerPaperDetailsUpdateComponent } from './mb-teller-paper-details-update.component';
import { MBTellerPaperDetailsDeletePopupComponent } from './mb-teller-paper-details-delete-dialog.component';
import { IMBTellerPaperDetails } from 'app/shared/model/mb-teller-paper-details.model';

@Injectable({ providedIn: 'root' })
export class MBTellerPaperDetailsResolve implements Resolve<IMBTellerPaperDetails> {
    constructor(private service: MBTellerPaperDetailsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((mBTellerPaperDetails: HttpResponse<MBTellerPaperDetails>) => mBTellerPaperDetails.body));
        }
        return of(new MBTellerPaperDetails());
    }
}

export const mBTellerPaperDetailsRoute: Routes = [
    {
        path: '',
        component: MBTellerPaperDetailsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.mBTellerPaperDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: MBTellerPaperDetailsDetailComponent,
        resolve: {
            mBTellerPaperDetails: MBTellerPaperDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBTellerPaperDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: MBTellerPaperDetailsUpdateComponent,
        resolve: {
            mBTellerPaperDetails: MBTellerPaperDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBTellerPaperDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: MBTellerPaperDetailsUpdateComponent,
        resolve: {
            mBTellerPaperDetails: MBTellerPaperDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBTellerPaperDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const mBTellerPaperDetailsPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: MBTellerPaperDetailsDeletePopupComponent,
        resolve: {
            mBTellerPaperDetails: MBTellerPaperDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.mBTellerPaperDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
