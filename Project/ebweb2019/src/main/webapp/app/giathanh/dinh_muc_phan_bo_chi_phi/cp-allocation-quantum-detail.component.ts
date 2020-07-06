import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICPAllocationQuantum } from 'app/shared/model/cp-allocation-quantum.model';

@Component({
    selector: 'eb-cp-allocation-quantum-detail',
    templateUrl: './cp-allocation-quantum-detail.component.html'
})
export class CPAllocationQuantumDetailComponent implements OnInit {
    cPAllocationQuantum: ICPAllocationQuantum;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cPAllocationQuantum }) => {
            this.cPAllocationQuantum = cPAllocationQuantum;
        });
    }

    previousState() {
        window.history.back();
    }
}
