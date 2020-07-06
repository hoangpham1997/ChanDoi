import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterialGoodsResourceTaxGroup } from 'app/shared/model/material-goods-resource-tax-group.model';

@Component({
    selector: 'eb-material-goods-resource-tax-group-detail',
    templateUrl: './material-goods-resource-tax-group-detail.component.html'
})
export class MaterialGoodsResourceTaxGroupDetailComponent implements OnInit {
    materialGoodsResourceTaxGroup: IMaterialGoodsResourceTaxGroup;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialGoodsResourceTaxGroup }) => {
            this.materialGoodsResourceTaxGroup = materialGoodsResourceTaxGroup;
        });
    }

    previousState() {
        window.history.back();
    }
}
