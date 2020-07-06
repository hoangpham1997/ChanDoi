import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrepaidExpense } from 'app/shared/model/prepaid-expense.model';

@Component({
    selector: 'eb-prepaid-expense-detail',
    templateUrl: './prepaid-expense-detail.component.html'
})
export class PrepaidExpenseDetailComponent implements OnInit {
    prepaidExpense: IPrepaidExpense;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ prepaidExpense }) => {
            this.prepaidExpense = prepaidExpense;
        });
    }

    previousState() {
        window.history.back();
    }
}
