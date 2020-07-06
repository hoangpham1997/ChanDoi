import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterialGoods } from 'app/shared/model/material-goods.model';

@Component({
    selector: 'eb-material-goods-detail',
    templateUrl: './material-goods-detail.component.html'
})
export class MaterialGoodsDetailComponent implements OnInit {
    materialGoods: IMaterialGoods;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialGoods }) => {
            this.materialGoods = materialGoods;
        });
    }

    previousState() {
        window.history.back();
    }
}
