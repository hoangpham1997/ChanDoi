import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrepaidExpenseAllocation } from 'app/shared/model/prepaid-expense-allocation.model';

@Component({
    selector: 'eb-prepaid-expense-allocation-detail',
    templateUrl: './prepaid-expense-allocation-detail.component.html'
})
export class PrepaidExpenseAllocationDetailComponent implements OnInit {
    prepaidExpenseAllocation: IPrepaidExpenseAllocation;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ prepaidExpenseAllocation }) => {
            this.prepaidExpenseAllocation = prepaidExpenseAllocation;
        });
    }

    previousState() {
        window.history.back();
    }
}
