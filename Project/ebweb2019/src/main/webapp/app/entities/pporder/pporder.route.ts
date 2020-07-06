import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IPporder, Pporder } from 'app/shared/model/pporder.model';
import { PporderService } from './pporder.service';
import { PporderComponent } from './pporder.component';
import { PporderDetailComponent } from './pporder-detail.component';
import { PporderUpdateComponent } from './pporder-update.component';

@Injectable({ providedIn: 'root' })
export class PporderResolve implements Resolve<IPporder> {
    constructor(private service: PporderService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.findById(id).pipe(map((pporder: HttpResponse<Pporder>) => pporder.body));
        }
        return of(new Pporder());
    }
}

export const pporderRoute: Routes = [
    {
        path: 'pporder',
        component: PporderComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.pporder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pporder/:id/view',
        component: PporderDetailComponent,
        resolve: {
            pporder: PporderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pporder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pporder/new',
        component: PporderUpdateComponent,
        resolve: {
            pporder: PporderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pporder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'pporder/:id/edit',
        component: PporderUpdateComponent,
        resolve: {
            pporder: PporderResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.pporder.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
