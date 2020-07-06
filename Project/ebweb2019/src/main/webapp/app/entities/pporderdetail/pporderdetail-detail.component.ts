import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPporderdetail } from 'app/shared/model/pporderdetail.model';

@Component({
    selector: 'eb-pporderdetail-detail',
    templateUrl: './pporderdetail-detail.component.html'
})
export class PporderdetailDetailComponent implements OnInit {
    pporderdetail: IPporderdetail;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pporderdetail }) => {
            this.pporderdetail = pporderdetail;
        });
    }

    previousState() {
        window.history.back();
    }
}
