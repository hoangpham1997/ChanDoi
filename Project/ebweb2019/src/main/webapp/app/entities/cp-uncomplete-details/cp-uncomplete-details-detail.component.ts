import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICPUncompleteDetails } from 'app/shared/model/cp-uncomplete-details.model';

@Component({
    selector: 'eb-cp-uncomplete-details-detail',
    templateUrl: './cp-uncomplete-details-detail.component.html'
})
export class CPUncompleteDetailsDetailComponent implements OnInit {
    cPUncompleteDetails: ICPUncompleteDetails;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cPUncompleteDetails }) => {
            this.cPUncompleteDetails = cPUncompleteDetails;
        });
    }

    previousState() {
        window.history.back();
    }
}
