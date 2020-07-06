import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMCReceipt } from 'app/shared/model/mc-receipt.model';

@Component({
    selector: 'eb-ghi-tang-detail',
    templateUrl: './ghi-tang-detail.component.html'
})
export class GhiTangDetailComponent implements OnInit {
    mCReceipt: IMCReceipt;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mCReceipt }) => {
            this.mCReceipt = mCReceipt;
        });
    }

    previousState() {
        window.history.back();
    }
}
