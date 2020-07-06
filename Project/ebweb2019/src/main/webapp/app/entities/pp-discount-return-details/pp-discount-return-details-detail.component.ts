import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';

@Component({
    selector: 'eb-pp-discount-return-details-detail',
    templateUrl: './pp-discount-return-details-detail.component.html'
})
export class PPDiscountReturnDetailsDetailComponent implements OnInit {
    pPDiscountReturnDetails: IPPDiscountReturnDetails;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pPDiscountReturnDetails }) => {
            this.pPDiscountReturnDetails = pPDiscountReturnDetails;
        });
    }

    previousState() {
        window.history.back();
    }
}
