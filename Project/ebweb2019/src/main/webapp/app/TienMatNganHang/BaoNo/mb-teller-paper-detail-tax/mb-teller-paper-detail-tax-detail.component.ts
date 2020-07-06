import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMBTellerPaperDetailTax } from 'app/shared/model/mb-teller-paper-detail-tax.model';

@Component({
    selector: 'eb-mb-teller-paper-detail-tax-detail',
    templateUrl: './mb-teller-paper-detail-tax-detail.component.html'
})
export class MBTellerPaperDetailTaxDetailComponent implements OnInit {
    mBTellerPaperDetailTax: IMBTellerPaperDetailTax;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBTellerPaperDetailTax }) => {
            this.mBTellerPaperDetailTax = mBTellerPaperDetailTax;
        });
    }

    previousState() {
        window.history.back();
    }
}
