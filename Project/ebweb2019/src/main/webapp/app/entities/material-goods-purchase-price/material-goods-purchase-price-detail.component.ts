import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterialGoodsPurchasePrice } from 'app/shared/model/material-goods-purchase-price.model';

@Component({
    selector: 'eb-material-goods-purchase-price-detail',
    templateUrl: './material-goods-purchase-price-detail.component.html'
})
export class MaterialGoodsPurchasePriceDetailComponent implements OnInit {
    materialGoodsPurchasePrice: IMaterialGoodsPurchasePrice;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialGoodsPurchasePrice }) => {
            this.materialGoodsPurchasePrice = materialGoodsPurchasePrice;
        });
    }

    previousState() {
        window.history.back();
    }
}
