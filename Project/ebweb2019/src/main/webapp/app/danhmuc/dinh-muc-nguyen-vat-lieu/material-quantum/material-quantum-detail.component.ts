import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterialQuantum } from 'app/shared/model/material-quantum.model';

@Component({
    selector: 'eb-material-quantum-detail',
    templateUrl: './material-quantum-detail.component.html'
})
export class MaterialQuantumDetailComponent implements OnInit {
    materialQuantum: IMaterialQuantum;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialQuantum }) => {
            this.materialQuantum = materialQuantum;
        });
    }

    previousState() {
        window.history.back();
    }
}
