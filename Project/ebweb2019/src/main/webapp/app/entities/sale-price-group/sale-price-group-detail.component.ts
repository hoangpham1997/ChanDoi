import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISalePriceGroup } from 'app/shared/model/sale-price-group.model';

@Component({
    selector: 'eb-sale-price-group-detail',
    templateUrl: './sale-price-group-detail.component.html'
})
export class SalePriceGroupDetailComponent implements OnInit {
    salePriceGroup: ISalePriceGroup;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ salePriceGroup }) => {
            this.salePriceGroup = salePriceGroup;
        });
    }

    previousState() {
        window.history.back();
    }
}
