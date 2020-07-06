import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICPAllocationRate } from 'app/shared/model/cp-allocation-rate.model';
import { CPAllocationRateService } from './cp-allocation-rate.service';

@Component({
    selector: 'eb-cp-allocation-rate-update',
    templateUrl: './cp-allocation-rate-update.component.html'
})
export class CPAllocationRateUpdateComponent implements OnInit {
    private _cPAllocationRate: ICPAllocationRate;
    isSaving: boolean;

    constructor(private cPAllocationRateService: CPAllocationRateService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ cPAllocationRate }) => {
            this.cPAllocationRate = cPAllocationRate;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.cPAllocationRate.id !== undefined) {
            this.subscribeToSaveResponse(this.cPAllocationRateService.update(this.cPAllocationRate));
        } else {
            this.subscribeToSaveResponse(this.cPAllocationRateService.create(this.cPAllocationRate));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICPAllocationRate>>) {
        result.subscribe((res: HttpResponse<ICPAllocationRate>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get cPAllocationRate() {
        return this._cPAllocationRate;
    }

    set cPAllocationRate(cPAllocationRate: ICPAllocationRate) {
        this._cPAllocationRate = cPAllocationRate;
    }
}
