import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICostSetMaterialGood } from 'app/shared/model/cost-set-material-good.model';

@Component({
    selector: 'eb-cost-set-material-good-detail',
    templateUrl: './cost-set-material-good-detail.component.html'
})
export class CostSetMaterialGoodDetailComponent implements OnInit {
    costSetMaterialGood: ICostSetMaterialGood;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ costSetMaterialGood }) => {
            this.costSetMaterialGood = costSetMaterialGood;
        });
    }

    previousState() {
        window.history.back();
    }
}
