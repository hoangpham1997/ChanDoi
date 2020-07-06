import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICPResult } from 'app/shared/model/cp-result.model';

@Component({
    selector: 'eb-cp-result-detail',
    templateUrl: './cp-result-detail.component.html'
})
export class CPResultDetailComponent implements OnInit {
    cPResult: ICPResult;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cPResult }) => {
            this.cPResult = cPResult;
        });
    }

    previousState() {
        window.history.back();
    }
}
