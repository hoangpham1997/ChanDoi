import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMCReceipt } from 'app/shared/model/mc-receipt.model';

@Component({
    selector: 'eb-mc-receipt-detail',
    templateUrl: './mc-receipt-detail.component.html'
})
export class MCReceiptDetailComponent implements OnInit {
    mCReceipt: IMCReceipt;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mCReceipt }) => {
            this.mCReceipt = mCReceipt;
        });
    }

    previousState() {
        window.history.back();
    }
}
