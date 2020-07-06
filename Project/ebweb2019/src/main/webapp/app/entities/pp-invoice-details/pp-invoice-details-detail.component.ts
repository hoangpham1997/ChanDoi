import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPPInvoiceDetails } from 'app/shared/model/pp-invoice-details.model';

@Component({
    selector: 'eb-pp-invoice-details-detail',
    templateUrl: './pp-invoice-details-detail.component.html'
})
export class PPInvoiceDetailsDetailComponent implements OnInit {
    pPInvoiceDetails: IPPInvoiceDetails;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pPInvoiceDetails }) => {
            this.pPInvoiceDetails = pPInvoiceDetails;
        });
    }

    previousState() {
        window.history.back();
    }
}
