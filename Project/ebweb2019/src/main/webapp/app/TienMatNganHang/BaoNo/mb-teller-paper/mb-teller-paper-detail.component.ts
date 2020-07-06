import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';

@Component({
    selector: 'eb-mb-teller-paper-detail',
    templateUrl: './mb-teller-paper-detail.component.html'
})
export class MBTellerPaperDetailComponent implements OnInit {
    mBTellerPaper: IMBTellerPaper;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBTellerPaper }) => {
            this.mBTellerPaper = mBTellerPaper;
        });
    }

    previousState() {
        window.history.back();
    }
}
