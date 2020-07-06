import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';

import { Principal, User, UserRouteAccessService, UserService } from 'app/core';
import { EbGroup } from 'app/core/eb-group/eb-group.model';
import { EbGroupService } from 'app/phan-quyen/eb-group/eb-group.service';
import { EbGroupComponent } from 'app/phan-quyen/eb-group/eb-group.component';
import { EbGroupUpdateComponent } from 'app/phan-quyen/eb-group/eb-group-update.component';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class EbGroupResolve implements Resolve<any> {
    constructor(private service: EbGroupService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id);
        }
        return new EbGroup();
    }
}

export const ebGroupRoute: Routes = [
    {
        path: '',
        component: EbGroupComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.ROLE_PERMISSION],
            pageTitle: 'ebGroup.home.title',
            defaultSort: 'id,asc'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: EbGroupUpdateComponent,
        resolve: {
            ebGroup: EbGroupResolve
        },
        data: {
            pageTitle: 'ebGroup.home.title'
        }
    },
    {
        path: 'new',
        component: EbGroupUpdateComponent,
        resolve: {
            ebGroup: EbGroupResolve
        },
        data: {
            pageTitle: 'ebGroup.home.title'
        }
    },
    {
        path: ':id/edit',
        component: EbGroupUpdateComponent,
        resolve: {
            ebGroup: EbGroupResolve
        },
        data: {
            pageTitle: 'ebGroup.home.title'
        }
    }
];
