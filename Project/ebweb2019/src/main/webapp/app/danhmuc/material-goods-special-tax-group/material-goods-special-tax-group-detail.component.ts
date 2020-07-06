import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterialGoodsSpecialTaxGroup } from 'app/shared/model/material-goods-special-tax-group.model';

@Component({
    selector: 'eb-material-goods-special-tax-group-detail',
    templateUrl: './material-goods-special-tax-group-detail.component.html'
})
export class MaterialGoodsSpecialTaxGroupDetailComponent implements OnInit {
    materialGoodsSpecialTaxGroup: IMaterialGoodsSpecialTaxGroup;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialGoodsSpecialTaxGroup }) => {
            this.materialGoodsSpecialTaxGroup = materialGoodsSpecialTaxGroup;
        });
    }

    previousState() {
        window.history.back();
    }
}
