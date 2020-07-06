import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';

@Component({
    selector: 'eb-goods-service-purchase-detail',
    templateUrl: './goods-service-purchase-detail.component.html'
})
export class GoodsServicePurchaseDetailComponent implements OnInit {
    goodsServicePurchase: IGoodsServicePurchase;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ goodsServicePurchase }) => {
            this.goodsServicePurchase = goodsServicePurchase;
        });
    }

    previousState() {
        window.history.back();
    }
}
