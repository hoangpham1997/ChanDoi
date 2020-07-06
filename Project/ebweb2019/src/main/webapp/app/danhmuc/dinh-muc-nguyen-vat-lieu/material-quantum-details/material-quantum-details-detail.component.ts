import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterialQuantumDetails } from 'app/shared/model/material-quantum-details.model';

@Component({
    selector: 'eb-material-quantum-details-detail',
    templateUrl: './material-quantum-details-detail.component.html'
})
export class MaterialQuantumDetailsDetailComponent implements OnInit {
    materialQuantumDetails: IMaterialQuantumDetails;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialQuantumDetails }) => {
            this.materialQuantumDetails = materialQuantumDetails;
        });
    }

    previousState() {
        window.history.back();
    }
}
