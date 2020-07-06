import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWarranty } from 'app/shared/model/warranty.model';

@Component({
    selector: 'eb-warranty-detail',
    templateUrl: './warranty-detail.component.html'
})
export class WarrantyDetailComponent implements OnInit {
    warranty: IWarranty;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ warranty }) => {
            this.warranty = warranty;
        });
    }

    previousState() {
        window.history.back();
    }
}
