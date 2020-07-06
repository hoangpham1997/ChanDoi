import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CareerGroup } from 'app/shared/model/career-group.model';
import { CareerGroupService } from './career-group.service';
import { CareerGroupComponent } from './career-group.component';
import { CareerGroupDetailComponent } from './career-group-detail.component';
import { CareerGroupUpdateComponent } from './career-group-update.component';
import { CareerGroupDeletePopupComponent } from './career-group-delete-dialog.component';
import { ICareerGroup } from 'app/shared/model/career-group.model';

@Injectable({ providedIn: 'root' })
export class CareerGroupResolve implements Resolve<ICareerGroup> {
    constructor(private service: CareerGroupService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((careerGroup: HttpResponse<CareerGroup>) => careerGroup.body));
        }
        return of(new CareerGroup());
    }
}

export const careerGroupRoute: Routes = [
    {
        path: 'career-group',
        component: CareerGroupComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.careerGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'career-group/:id/view',
        component: CareerGroupDetailComponent,
        resolve: {
            careerGroup: CareerGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.careerGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'career-group/new',
        component: CareerGroupUpdateComponent,
        resolve: {
            careerGroup: CareerGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.careerGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'career-group/:id/edit',
        component: CareerGroupUpdateComponent,
        resolve: {
            careerGroup: CareerGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.careerGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const careerGroupPopupRoute: Routes = [
    {
        path: 'career-group/:id/delete',
        component: CareerGroupDeletePopupComponent,
        resolve: {
            careerGroup: CareerGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.careerGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
