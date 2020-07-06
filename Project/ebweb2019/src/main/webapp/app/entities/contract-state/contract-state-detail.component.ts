import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContractState } from 'app/shared/model/contract-state.model';

@Component({
    selector: 'eb-contract-state-detail',
    templateUrl: './contract-state-detail.component.html'
})
export class ContractStateDetailComponent implements OnInit {
    contractState: IContractState;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ contractState }) => {
            this.contractState = contractState;
        });
    }

    previousState() {
        window.history.back();
    }
}
