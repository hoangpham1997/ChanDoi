import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMBInternalTransfer } from 'app/shared/model/mb-internal-transfer.model';

@Component({
    selector: 'eb-mb-internal-transfer-detail',
    templateUrl: './mb-internal-transfer-detail.component.html'
})
export class MBInternalTransferDetailComponent implements OnInit {
    mBInternalTransfer: IMBInternalTransfer;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBInternalTransfer }) => {
            this.mBInternalTransfer = mBInternalTransfer;
        });
    }

    previousState() {
        window.history.back();
    }
}
