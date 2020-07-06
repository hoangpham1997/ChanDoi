import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAccountTransfer } from 'app/shared/model/account-transfer.model';

@Component({
    selector: 'eb-account-transfer-detail',
    templateUrl: './account-transfer-detail.component.html'
})
export class AccountTransferDetailComponent implements OnInit {
    accountTransfer: IAccountTransfer;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ accountTransfer }) => {
            this.accountTransfer = accountTransfer;
        });
    }

    previousState() {
        window.history.back();
    }
}
