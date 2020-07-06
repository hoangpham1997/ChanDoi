import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRepositoryLedger } from 'app/shared/model/repository-ledger.model';

@Component({
    selector: 'eb-repository-ledger-detail',
    templateUrl: './repository-ledger-detail.component.html'
})
export class RepositoryLedgerDetailComponent implements OnInit {
    repositoryLedger: IRepositoryLedger;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ repositoryLedger }) => {
            this.repositoryLedger = repositoryLedger;
        });
    }

    previousState() {
        window.history.back();
    }
}
