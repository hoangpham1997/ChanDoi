import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { EbGroup } from 'app/core/eb-group/eb-group.model';
import { EbGroupService } from 'app/phan-quyen/eb-group';
import { PermissionUserComponent } from 'app/phan-quyen/permission-user/permission-user.component';

@Injectable({ providedIn: 'root' })
export class PermissionUserResolve implements Resolve<any> {
    constructor(private service: EbGroupService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id);
        }
        return new EbGroup();
    }
}

export const permissionUserRoute: Routes = [
    {
        path: '',
        component: PermissionUserComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            pageTitle: 'permissionUser.home.title',
            defaultSort: 'id,asc'
        }
    }
];
