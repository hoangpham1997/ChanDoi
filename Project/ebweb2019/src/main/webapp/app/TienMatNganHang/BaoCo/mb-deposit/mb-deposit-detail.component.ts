import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMBDeposit } from 'app/shared/model/mb-deposit.model';

@Component({
    selector: 'eb-mb-deposit-detail',
    templateUrl: './mb-deposit-detail.component.html'
})
export class MBDepositDetailComponent implements OnInit {
    mBDeposit: IMBDeposit;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBDeposit }) => {
            this.mBDeposit = mBDeposit;
        });
    }

    previousState() {
        window.history.back();
    }
}
