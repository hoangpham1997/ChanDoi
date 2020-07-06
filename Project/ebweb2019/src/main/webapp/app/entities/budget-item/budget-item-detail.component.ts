import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBudgetItem } from 'app/shared/model/budget-item.model';

@Component({
    selector: 'eb-budget-item-detail',
    templateUrl: './budget-item-detail.component.html'
})
export class BudgetItemDetailComponent implements OnInit {
    budgetItem: IBudgetItem;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ budgetItem }) => {
            this.budgetItem = budgetItem;
        });
    }

    previousState() {
        window.history.back();
    }
}
