import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';

@Component({
    selector: 'eb-material-goods-category-detail',
    templateUrl: './material-goods-category-detail.component.html'
})
export class MaterialGoodsCategoryDetailComponent implements OnInit {
    materialGoodsCategory: IMaterialGoodsCategory;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialGoodsCategory }) => {
            this.materialGoodsCategory = materialGoodsCategory;
        });
    }

    previousState() {
        window.history.back();
    }
}
