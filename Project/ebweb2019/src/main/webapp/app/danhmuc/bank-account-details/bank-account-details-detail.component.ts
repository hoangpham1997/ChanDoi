import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';

@Component({
    selector: 'eb-bank-account-details-detail',
    templateUrl: './bank-account-details-detail.component.html'
})
export class BankAccountDetailsDetailComponent implements OnInit {
    bankAccountDetails: IBankAccountDetails;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ bankAccountDetails }) => {
            this.bankAccountDetails = bankAccountDetails;
        });
    }

    previousState() {
        window.history.back();
    }
}
