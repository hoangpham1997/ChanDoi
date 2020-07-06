import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStatisticsCode } from 'app/shared/model/statistics-code.model';

@Component({
    selector: 'eb-statistics-code-detail',
    templateUrl: './statistics-code-detail.component.html'
})
export class StatisticsCodeDetailComponent implements OnInit {
    statisticsCode: IStatisticsCode;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ statisticsCode }) => {
            this.statisticsCode = statisticsCode;
        });
    }

    previousState() {
        window.history.back();
    }
}
