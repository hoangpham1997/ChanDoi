import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICPResult } from 'app/shared/model/cp-result.model';
import { CPResultService } from './cp-result.service';

@Component({
    selector: 'eb-cp-result-update',
    templateUrl: './cp-result-update.component.html'
})
export class CPResultUpdateComponent implements OnInit {
    private _cPResult: ICPResult;
    isSaving: boolean;

    constructor(private cPResultService: CPResultService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ cPResult }) => {
            this.cPResult = cPResult;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.cPResult.id !== undefined) {
            this.subscribeToSaveResponse(this.cPResultService.update(this.cPResult));
        } else {
            this.subscribeToSaveResponse(this.cPResultService.create(this.cPResult));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICPResult>>) {
        result.subscribe((res: HttpResponse<ICPResult>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get cPResult() {
        return this._cPResult;
    }

    set cPResult(cPResult: ICPResult) {
        this._cPResult = cPResult;
    }
}
