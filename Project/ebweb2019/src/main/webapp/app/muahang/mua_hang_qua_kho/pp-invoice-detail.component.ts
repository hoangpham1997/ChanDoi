import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPPInvoice } from 'app/shared/model/pp-invoice.model';

@Component({
    selector: 'eb-pp-invoice-detail',
    templateUrl: './pp-invoice-detail.component.html'
})
export class PPInvoiceDetailComponent implements OnInit {
    pPInvoice: IPPInvoice;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pPInvoice }) => {
            this.pPInvoice = pPInvoice;
        });
    }

    previousState() {
        window.history.back();
    }
}
