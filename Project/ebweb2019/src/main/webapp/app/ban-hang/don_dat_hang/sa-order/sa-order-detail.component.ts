import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISAOrder } from 'app/shared/model/sa-order.model';

@Component({
    selector: 'eb-sa-order-detail',
    templateUrl: './sa-order-detail.component.html'
})
export class SAOrderDetailComponent implements OnInit {
    sAOrder: ISAOrder;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sAOrder }) => {
            this.sAOrder = sAOrder;
        });
    }

    previousState() {
        window.history.back();
    }
}
