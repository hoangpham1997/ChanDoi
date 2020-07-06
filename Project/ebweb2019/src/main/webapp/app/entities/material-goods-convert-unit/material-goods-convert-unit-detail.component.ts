import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterialGoodsConvertUnit } from 'app/shared/model/material-goods-convert-unit.model';

@Component({
    selector: 'eb-material-goods-convert-unit-detail',
    templateUrl: './material-goods-convert-unit-detail.component.html'
})
export class MaterialGoodsConvertUnitDetailComponent implements OnInit {
    materialGoodsConvertUnit: IMaterialGoodsConvertUnit;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialGoodsConvertUnit }) => {
            this.materialGoodsConvertUnit = materialGoodsConvertUnit;
        });
    }

    previousState() {
        window.history.back();
    }
}
