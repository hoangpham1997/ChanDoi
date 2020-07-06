import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaymentClause } from 'app/shared/model/payment-clause.model';

@Component({
    selector: 'eb-payment-clause-detail',
    templateUrl: './payment-clause-detail.component.html'
})
export class PaymentClauseDetailComponent implements OnInit {
    paymentClause: IPaymentClause;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ paymentClause }) => {
            this.paymentClause = paymentClause;
        });
    }

    previousState() {
        window.history.back();
    }
}
