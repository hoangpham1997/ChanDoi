import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { StatisticsCodeService } from './statistics-code.service';

@Component({
    selector: 'eb-statistics-code-update',
    templateUrl: './statistics-code-update.component.html'
})
export class StatisticsCodeUpdateComponent implements OnInit {
    private _statisticsCode: IStatisticsCode;
    isSaving: boolean;

    constructor(private statisticsCodeService: StatisticsCodeService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ statisticsCode }) => {
            this.statisticsCode = statisticsCode;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.statisticsCode.id !== undefined) {
            this.subscribeToSaveResponse(this.statisticsCodeService.update(this.statisticsCode));
        } else {
            this.subscribeToSaveResponse(this.statisticsCodeService.create(this.statisticsCode));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IStatisticsCode>>) {
        result.subscribe((res: HttpResponse<IStatisticsCode>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get statisticsCode() {
        return this._statisticsCode;
    }

    set statisticsCode(statisticsCode: IStatisticsCode) {
        this._statisticsCode = statisticsCode;
    }
}
