import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEMContract } from 'app/shared/model/em-contract.model';

@Component({
    selector: 'eb-em-contract-detail',
    templateUrl: './em-contract-detail.component.html'
})
export class EMContractDetailComponent implements OnInit {
    eMContract: IEMContract;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ eMContract }) => {
            this.eMContract = eMContract;
        });
    }

    previousState() {
        window.history.back();
    }
}
