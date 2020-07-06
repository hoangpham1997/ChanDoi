import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAccountDefault } from 'app/shared/model/account-default.model';

@Component({
    selector: 'eb-account-default-detail',
    templateUrl: './account-default-detail.component.html'
})
export class AccountDefaultDetailComponent implements OnInit {
    accountDefault: IAccountDefault;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ accountDefault }) => {
            this.accountDefault = accountDefault;
        });
    }

    previousState() {
        window.history.back();
    }
}
