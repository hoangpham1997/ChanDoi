import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { EbPackage } from 'app/shared/model/eb-package.model';
import { EbPackageService } from './eb-package.service';
import { EbPackageComponent } from './eb-package.component';
import { EbPackageDetailComponent } from './eb-package-detail.component';
import { EbPackageUpdateComponent } from './eb-package-update.component';
import { EbPackageDeletePopupComponent } from './eb-package-delete-dialog.component';
import { IEbPackage } from 'app/shared/model/eb-package.model';

@Injectable({ providedIn: 'root' })
export class EbPackageResolve implements Resolve<IEbPackage> {
    constructor(private service: EbPackageService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((ebPackage: HttpResponse<EbPackage>) => ebPackage.body));
        }
        return of(new EbPackage());
    }
}

export const ebPackageRoute: Routes = [
    {
        path: 'eb-package',
        component: EbPackageComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.ebPackage.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'eb-package/:id/view',
        component: EbPackageDetailComponent,
        resolve: {
            ebPackage: EbPackageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.ebPackage.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'eb-package/new',
        component: EbPackageUpdateComponent,
        resolve: {
            ebPackage: EbPackageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.ebPackage.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'eb-package/:id/edit',
        component: EbPackageUpdateComponent,
        resolve: {
            ebPackage: EbPackageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.ebPackage.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ebPackagePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: EbPackageDeletePopupComponent,
        resolve: {
            ebPackage: EbPackageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.ebPackage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
