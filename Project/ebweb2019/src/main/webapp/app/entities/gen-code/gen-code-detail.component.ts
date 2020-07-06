import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGenCode } from 'app/shared/model/gen-code.model';

@Component({
    selector: 'eb-gen-code-detail',
    templateUrl: './gen-code-detail.component.html'
})
export class GenCodeDetailComponent implements OnInit {
    genCode: IGenCode;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ genCode }) => {
            this.genCode = genCode;
        });
    }

    previousState() {
        window.history.back();
    }
}
