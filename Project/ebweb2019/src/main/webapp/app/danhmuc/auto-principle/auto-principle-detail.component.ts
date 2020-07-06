import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';

@Component({
    selector: 'eb-auto-principle-detail',
    templateUrl: './auto-principle-detail.component.html'
})
export class AutoPrincipleDetailComponent implements OnInit {
    autoPrinciple: IAutoPrinciple;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ autoPrinciple }) => {
            this.autoPrinciple = autoPrinciple;
        });
    }

    previousState() {
        window.history.back();
    }
}
