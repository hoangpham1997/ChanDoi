import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGOtherVoucherDetailExpense } from 'app/shared/model/g-other-voucher-detail-expense.model';

@Component({
    selector: 'eb-g-other-voucher-detail-expense-detail',
    templateUrl: './g-other-voucher-detail-expense-detail.component.html'
})
export class GOtherVoucherDetailExpenseDetailComponent implements OnInit {
    gOtherVoucherDetailExpense: IGOtherVoucherDetailExpense;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ gOtherVoucherDetailExpense }) => {
            this.gOtherVoucherDetailExpense = gOtherVoucherDetailExpense;
        });
    }

    previousState() {
        window.history.back();
    }
}
