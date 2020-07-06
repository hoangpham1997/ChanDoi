import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';

@Component({
    selector: 'eb-sale-discount-policy-detail',
    templateUrl: './sale-discount-policy-detail.component.html'
})
export class SaleDiscountPolicyDetailComponent implements OnInit {
    saleDiscountPolicy: ISaleDiscountPolicy;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ saleDiscountPolicy }) => {
            this.saleDiscountPolicy = saleDiscountPolicy;
        });
    }

    previousState() {
        window.history.back();
    }
}
