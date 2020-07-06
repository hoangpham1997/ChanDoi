import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ITimeSheetSymbols } from 'app/shared/model/time-sheet-symbols.model';
import { TimeSheetSymbolsService } from './time-sheet-symbols.service';

@Component({
    selector: 'eb-time-sheet-symbols-update',
    templateUrl: './time-sheet-symbols-update.component.html'
})
export class TimeSheetSymbolsUpdateComponent implements OnInit {
    private _timeSheetSymbols: ITimeSheetSymbols;
    isSaving: boolean;

    constructor(private timeSheetSymbolsService: TimeSheetSymbolsService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ timeSheetSymbols }) => {
            this.timeSheetSymbols = timeSheetSymbols;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.timeSheetSymbols.id !== undefined) {
            this.subscribeToSaveResponse(this.timeSheetSymbolsService.update(this.timeSheetSymbols));
        } else {
            this.subscribeToSaveResponse(this.timeSheetSymbolsService.create(this.timeSheetSymbols));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITimeSheetSymbols>>) {
        result.subscribe((res: HttpResponse<ITimeSheetSymbols>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get timeSheetSymbols() {
        return this._timeSheetSymbols;
    }

    set timeSheetSymbols(timeSheetSymbols: ITimeSheetSymbols) {
        this._timeSheetSymbols = timeSheetSymbols;
    }
}
