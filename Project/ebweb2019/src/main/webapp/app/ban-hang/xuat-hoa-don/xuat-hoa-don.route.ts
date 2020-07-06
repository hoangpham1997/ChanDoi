import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { XuatHoaDonComponent } from './xuat-hoa-don.component';
import { XuatHoaDonUpdateComponent } from 'app/ban-hang/xuat-hoa-don/xuat-hoa-don-update.component';
import { SaBillService } from 'app/ban-hang/xuat-hoa-don/sa-bill.service';
import { SaBill } from 'app/shared/model/sa-bill.model';
import { XuatHoaDonDeletePopupComponent } from 'app/ban-hang/xuat-hoa-don/xuat-hoa-don-delete-dialog.component';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { MCAuditResolve, MCAuditUpdateComponent } from 'app/TienMatNganHang/kiem_ke_quy';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class XuatHoaDonResolve implements Resolve<any> {
    constructor(private service: SaBillService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        if (route.routeConfig.path === 'new') {
            return of(new SaBill());
        } else if (route.routeConfig.path.includes('delete')) {
            const id = route.params['id'] ? route.params['id'] : null;
            return this.service.findById(id).pipe(map((saBill: HttpResponse<any>) => saBill.body));
        } else {
            const id = route.params['id'] ? route.params['id'] : null;
            return this.service.find(id).pipe(map((saBill: HttpResponse<any>) => saBill.body));
        }
    }
}

export const xuatHoaDon: Routes = [
    {
        path: '',
        component: XuatHoaDonComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', ROLE.XuatHoaDon_Xem],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.saBill.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: XuatHoaDonUpdateComponent,
        resolve: {
            saBill: XuatHoaDonResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.XuatHoaDon_Them],
            pageTitle: 'ebwebApp.saBill.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: XuatHoaDonUpdateComponent,
        resolve: {
            saBill: XuatHoaDonResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.XuatHoaDon_Sua],
            pageTitle: 'ebwebApp.saBill.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/invoice-processing/:type',
        component: XuatHoaDonUpdateComponent,
        resolve: {
            saBill: XuatHoaDonResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.saBill.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit/from-ref',
        component: XuatHoaDonUpdateComponent,
        resolve: {
            saBill: XuatHoaDonResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ebwebApp.saBill.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];
export const xuatHoaDonPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: XuatHoaDonDeletePopupComponent,
        resolve: {
            xuatHoaDon: XuatHoaDonResolve
        },
        data: {
            authorities: ['ROLE_USER', ROLE.XuatHoaDon_Xoa],
            pageTitle: 'ebwebApp.saBill.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
