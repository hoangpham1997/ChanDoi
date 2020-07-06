import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExpenseItem } from 'app/shared/model/expense-item.model';

@Component({
    selector: 'eb-expense-item-detail',
    templateUrl: './expense-item-detail.component.html'
})
export class ExpenseItemDetailComponent implements OnInit {
    expenseItem: IExpenseItem;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ expenseItem }) => {
            this.expenseItem = expenseItem;
        });
    }

    previousState() {
        window.history.back();
    }
}
