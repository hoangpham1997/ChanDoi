import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PPInvoiceDetails } from 'app/shared/model/pp-invoice-details.model';
import { PPInvoiceDetailsService } from './pp-invoice-details.service';
import { IPPInvoiceDetails } from 'app/shared/model/pp-invoice-details.model';

@Injectable({ providedIn: 'root' })
export class PPInvoiceDetailsResolve implements Resolve<IPPInvoiceDetails> {
    constructor(private service: PPInvoiceDetailsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((pPInvoiceDetails: HttpResponse<PPInvoiceDetails>) => pPInvoiceDetails.body));
        }
        return of(new PPInvoiceDetails());
    }
}

export const pPInvoiceDetailsRoute: Routes = [];

export const pPInvoiceDetailsPopupRoute: Routes = [];
