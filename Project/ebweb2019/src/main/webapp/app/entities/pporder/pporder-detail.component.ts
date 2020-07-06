import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPporder } from 'app/shared/model/pporder.model';

@Component({
    selector: 'eb-pporder-detail',
    templateUrl: './pporder-detail.component.html'
})
export class PporderDetailComponent implements OnInit {
    pporder: IPporder;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pporder }) => {
            this.pporder = pporder;
        });
    }

    previousState() {
        window.history.back();
    }
}
