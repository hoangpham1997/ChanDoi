import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICostSet } from 'app/shared/model/cost-set.model';
import { CostSetService } from './cost-set.service';

@Component({
    selector: 'eb-cost-set-update',
    templateUrl: './cost-set-update.component.html'
})
export class CostSetUpdateComponent implements OnInit {
    private _costSet: ICostSet;
    isSaving: boolean;

    constructor(private costSetService: CostSetService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ costSet }) => {
            this.costSet = costSet;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.costSet.id !== undefined) {
            this.subscribeToSaveResponse(this.costSetService.update(this.costSet));
        } else {
            this.subscribeToSaveResponse(this.costSetService.create(this.costSet));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICostSet>>) {
        result.subscribe((res: HttpResponse<ICostSet>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get costSet() {
        return this._costSet;
    }

    set costSet(costSet: ICostSet) {
        this._costSet = costSet;
    }
}
