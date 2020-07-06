import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMBInternalTransferTax } from 'app/shared/model/mb-internal-transfer-tax.model';

@Component({
    selector: 'eb-mb-internal-transfer-tax-detail',
    templateUrl: './mb-internal-transfer-tax-detail.component.html'
})
export class MBInternalTransferTaxDetailComponent implements OnInit {
    mBInternalTransferTax: IMBInternalTransferTax;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBInternalTransferTax }) => {
            this.mBInternalTransferTax = mBInternalTransferTax;
        });
    }

    previousState() {
        window.history.back();
    }
}
