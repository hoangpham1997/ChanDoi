import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrepaidExpenseVoucher } from 'app/shared/model/prepaid-expense-voucher.model';

@Component({
    selector: 'eb-prepaid-expense-voucher-detail',
    templateUrl: './prepaid-expense-voucher-detail.component.html'
})
export class PrepaidExpenseVoucherDetailComponent implements OnInit {
    prepaidExpenseVoucher: IPrepaidExpenseVoucher;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ prepaidExpenseVoucher }) => {
            this.prepaidExpenseVoucher = prepaidExpenseVoucher;
        });
    }

    previousState() {
        window.history.back();
    }
}
