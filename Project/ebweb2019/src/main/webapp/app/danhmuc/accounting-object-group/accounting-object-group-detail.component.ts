import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';

@Component({
    selector: 'eb-accounting-object-group-detail',
    templateUrl: './accounting-object-group-detail.component.html'
})
export class AccountingObjectGroupDetailComponent implements OnInit {
    accountingObjectGroup: IAccountingObjectGroup;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ accountingObjectGroup }) => {
            this.accountingObjectGroup = accountingObjectGroup;
        });
    }

    previousState() {
        window.history.back();
    }
}
