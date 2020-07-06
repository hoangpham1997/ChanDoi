import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMBCreditCard } from 'app/shared/model/mb-credit-card.model';

@Component({
    selector: 'eb-mb-credit-card-detail',
    templateUrl: './mb-credit-card-detail.component.html'
})
export class MBCreditCardDetailComponent implements OnInit {
    mBCreditCard: IMBCreditCard;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBCreditCard }) => {
            this.mBCreditCard = mBCreditCard;
        });
    }

    previousState() {
        window.history.back();
    }
}
