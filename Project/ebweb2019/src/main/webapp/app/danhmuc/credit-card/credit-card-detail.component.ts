import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICreditCard } from 'app/shared/model/credit-card.model';

@Component({
    selector: 'eb-credit-card-detail',
    templateUrl: './credit-card-detail.component.html'
})
export class CreditCardDetailComponent implements OnInit {
    creditCard: ICreditCard;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ creditCard }) => {
            this.creditCard = creditCard;
        });
    }

    previousState() {
        window.history.back();
    }
}
