import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PPInvoice } from 'app/shared/model/pp-invoice.model';
import { PPInvoiceService } from './pp-invoice.service';
import { IPPInvoice } from 'app/shared/model/pp-invoice.model';

@Injectable({ providedIn: 'root' })
export class PPInvoiceResolve implements Resolve<IPPInvoice> {
    constructor(private service: PPInvoiceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((pPInvoice: HttpResponse<PPInvoice>) => pPInvoice.body));
        }
        return of(new PPInvoice());
    }
}

export const pPInvoiceRoute: Routes = [];

export const pPInvoicePopupRoute: Routes = [];
