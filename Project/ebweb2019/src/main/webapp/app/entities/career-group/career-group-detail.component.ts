import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICareerGroup } from 'app/shared/model/career-group.model';

@Component({
    selector: 'eb-career-group-detail',
    templateUrl: './career-group-detail.component.html'
})
export class CareerGroupDetailComponent implements OnInit {
    careerGroup: ICareerGroup;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ careerGroup }) => {
            this.careerGroup = careerGroup;
        });
    }

    previousState() {
        window.history.back();
    }
}
