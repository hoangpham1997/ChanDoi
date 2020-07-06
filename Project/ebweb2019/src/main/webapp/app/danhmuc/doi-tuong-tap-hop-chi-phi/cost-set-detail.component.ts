import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICostSet } from 'app/shared/model/cost-set.model';

@Component({
    selector: 'eb-cost-set-detail',
    templateUrl: './cost-set-detail.component.html'
})
export class CostSetDetailComponent implements OnInit {
    costSet: ICostSet;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ costSet }) => {
            this.costSet = costSet;
        });
    }

    previousState() {
        window.history.back();
    }
}
