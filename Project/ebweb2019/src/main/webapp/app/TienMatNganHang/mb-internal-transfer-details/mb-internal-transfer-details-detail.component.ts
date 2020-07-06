import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMBInternalTransferDetails } from 'app/shared/model/mb-internal-transfer-details.model';

@Component({
    selector: 'eb-mb-internal-transfer-details-detail',
    templateUrl: './mb-internal-transfer-details-detail.component.html'
})
export class MBInternalTransferDetailsDetailComponent implements OnInit {
    mBInternalTransferDetails: IMBInternalTransferDetails;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBInternalTransferDetails }) => {
            this.mBInternalTransferDetails = mBInternalTransferDetails;
        });
    }

    previousState() {
        window.history.back();
    }
}
