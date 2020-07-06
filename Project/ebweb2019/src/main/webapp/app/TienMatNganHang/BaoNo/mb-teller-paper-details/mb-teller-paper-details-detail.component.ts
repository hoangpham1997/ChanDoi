import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMBTellerPaperDetails } from 'app/shared/model/mb-teller-paper-details.model';

@Component({
    selector: 'eb-mb-teller-paper-details-detail',
    templateUrl: './mb-teller-paper-details-detail.component.html'
})
export class MBTellerPaperDetailsDetailComponent implements OnInit {
    mBTellerPaperDetails: IMBTellerPaperDetails;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBTellerPaperDetails }) => {
            this.mBTellerPaperDetails = mBTellerPaperDetails;
        });
    }

    previousState() {
        window.history.back();
    }
}
