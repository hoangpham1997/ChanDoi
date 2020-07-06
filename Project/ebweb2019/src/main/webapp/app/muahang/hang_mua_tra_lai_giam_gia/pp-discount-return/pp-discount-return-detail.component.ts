import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPPDiscountReturn } from 'app/shared/model/pp-discount-return.model';

@Component({
    selector: 'eb-pp-discount-return-detail',
    templateUrl: './pp-discount-return-detail.component.html'
})
export class PPDiscountReturnDetailComponent implements OnInit {
    pPDiscountReturn: IPPDiscountReturn;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pPDiscountReturn }) => {
            this.pPDiscountReturn = pPDiscountReturn;
        });
    }

    previousState() {
        window.history.back();
    }
}
