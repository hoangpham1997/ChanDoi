import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IPPInvoiceDetails } from 'app/shared/model/pp-invoice-details.model';
import { PPInvoiceDetailsService } from './pp-invoice-details.service';

@Component({
    selector: 'eb-pp-invoice-details-update',
    templateUrl: './pp-invoice-details-update.component.html'
})
export class PPInvoiceDetailsUpdateComponent implements OnInit {
    private _pPInvoiceDetails: IPPInvoiceDetails;
    isSaving: boolean;
    expiryDateDp: any;
    invoiceDateDp: any;

    constructor(private pPInvoiceDetailsService: PPInvoiceDetailsService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ pPInvoiceDetails }) => {
            this.pPInvoiceDetails = pPInvoiceDetails;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.pPInvoiceDetails.id !== undefined) {
            this.subscribeToSaveResponse(this.pPInvoiceDetailsService.update(this.pPInvoiceDetails));
        } else {
            this.subscribeToSaveResponse(this.pPInvoiceDetailsService.create(this.pPInvoiceDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPPInvoiceDetails>>) {
        result.subscribe((res: HttpResponse<IPPInvoiceDetails>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get pPInvoiceDetails() {
        return this._pPInvoiceDetails;
    }

    set pPInvoiceDetails(pPInvoiceDetails: IPPInvoiceDetails) {
        this._pPInvoiceDetails = pPInvoiceDetails;
    }
}
