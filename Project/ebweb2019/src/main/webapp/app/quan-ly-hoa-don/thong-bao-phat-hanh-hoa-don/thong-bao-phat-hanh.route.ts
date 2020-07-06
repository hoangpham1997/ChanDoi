import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { ThongBaoPhatHanhComponent } from './thong-bao-phat-hanh.component';
import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { of } from 'rxjs';
import { CanDeactiveGuardService } from 'app/shared/can-deactive-guard/can-deactive-guard.service';
import { IaPublishInvoice } from 'app/shared/model/ia-publish-invoice.model';
import { IaPublishInvoiceService } from 'app/quan-ly-hoa-don/thong-bao-phat-hanh-hoa-don/ia-publish-invoice.service';
import { ThongBaoPhatHanhUpdateComponent } from 'app/quan-ly-hoa-don/thong-bao-phat-hanh-hoa-don/thong-bao-phat-hanh-update.component';
import { ThongBaoPhatHanhDeletePopupComponent } from 'app/quan-ly-hoa-don/thong-bao-phat-hanh-hoa-don/thong-bao-phat-hanh-delete-dialog.component';
import { ROLE } from 'app/role.constants';

@Injectable({ providedIn: 'root' })
export class IaPublishInvoiceResolve implements Resolve<IaPublishInvoice> {
    constructor(private service: IaPublishInvoiceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((res: HttpResponse<IaPublishInvoice>) => res.body));
        }
        return of(new IaPublishInvoice());
    }
}

export const ThongBaoPhatHanhRoute: Routes = [
    {
        path: '',
        component: ThongBaoPhatHanhComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.TBPH_XEM],
            defaultSort: 'id,asc',
            pageTitle: 'ebwebApp.iaPublishInvoice.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: ThongBaoPhatHanhUpdateComponent,
        resolve: {
            iaPublishInvoice: IaPublishInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.TBPH_THEM],
            pageTitle: 'ebwebApp.iaPublishInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    },
    {
        path: ':id/edit',
        component: ThongBaoPhatHanhUpdateComponent,
        resolve: {
            iaPublishInvoice: IaPublishInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.TBPH_XEM],
            pageTitle: 'ebwebApp.iaPublishInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [CanDeactiveGuardService]
    }
];

export const ThongBaoPhatHanhPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ThongBaoPhatHanhDeletePopupComponent,
        resolve: {
            iaPublishInvoice: IaPublishInvoiceResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', ROLE.TBPH_XEM],
            pageTitle: 'ebwebApp.iaPublishInvoice.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
