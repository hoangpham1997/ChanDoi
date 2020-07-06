import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICPAllocationRate } from 'app/shared/model/cp-allocation-rate.model';

@Component({
    selector: 'eb-cp-allocation-rate-detail',
    templateUrl: './cp-allocation-rate-detail.component.html'
})
export class CPAllocationRateDetailComponent implements OnInit {
    cPAllocationRate: ICPAllocationRate;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cPAllocationRate }) => {
            this.cPAllocationRate = cPAllocationRate;
        });
    }

    previousState() {
        window.history.back();
    }
}
