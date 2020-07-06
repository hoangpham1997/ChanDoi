import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGOtherVoucherDetailExpenseAllocation } from 'app/shared/model/g-other-voucher-detail-expense-allocation.model';

@Component({
    selector: 'eb-g-other-voucher-detail-expense-allocation-detail',
    templateUrl: './g-other-voucher-detail-expense-allocation-detail.component.html'
})
export class GOtherVoucherDetailExpenseAllocationDetailComponent implements OnInit {
    gOtherVoucherDetailExpenseAllocation: IGOtherVoucherDetailExpenseAllocation;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ gOtherVoucherDetailExpenseAllocation }) => {
            this.gOtherVoucherDetailExpenseAllocation = gOtherVoucherDetailExpenseAllocation;
        });
    }

    previousState() {
        window.history.back();
    }
}
