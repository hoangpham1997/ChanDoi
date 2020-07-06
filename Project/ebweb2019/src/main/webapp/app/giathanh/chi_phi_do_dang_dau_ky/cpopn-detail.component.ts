import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICPOPN } from 'app/shared/model/cpopn.model';

@Component({
    selector: 'eb-cpopn-detail',
    templateUrl: './cpopn-detail.component.html'
})
export class CPOPNDetailComponent implements OnInit {
    cPOPN: ICPOPN;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cPOPN }) => {
            this.cPOPN = cPOPN;
        });
    }

    previousState() {
        window.history.back();
    }
}
