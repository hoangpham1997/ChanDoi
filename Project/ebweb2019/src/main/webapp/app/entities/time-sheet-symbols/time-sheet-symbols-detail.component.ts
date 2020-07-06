import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITimeSheetSymbols } from 'app/shared/model/time-sheet-symbols.model';

@Component({
    selector: 'eb-time-sheet-symbols-detail',
    templateUrl: './time-sheet-symbols-detail.component.html'
})
export class TimeSheetSymbolsDetailComponent implements OnInit {
    timeSheetSymbols: ITimeSheetSymbols;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ timeSheetSymbols }) => {
            this.timeSheetSymbols = timeSheetSymbols;
        });
    }

    previousState() {
        window.history.back();
    }
}
