import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';

@Component({
    selector: 'eb-accounting-object-bank-account-detail',
    templateUrl: './accounting-object-bank-account-detail.component.html'
})
export class AccountingObjectBankAccountDetailComponent implements OnInit {
    accountingObjectBankAccount: IAccountingObjectBankAccount;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ accountingObjectBankAccount }) => {
            this.accountingObjectBankAccount = accountingObjectBankAccount;
        });
    }

    previousState() {
        window.history.back();
    }
}
