import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMCPayment } from 'app/shared/model/mc-payment.model';

@Component({
    selector: 'eb-mc-payment-detail',
    templateUrl: './mc-payment-detail.component.html'
})
export class MCPaymentDetailComponent implements OnInit {
    mCPayment: IMCPayment;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mCPayment }) => {
            this.mCPayment = mCPayment;
        });
    }

    previousState() {
        window.history.back();
    }
}
