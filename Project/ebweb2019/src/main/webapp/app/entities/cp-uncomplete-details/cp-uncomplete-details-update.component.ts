import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICPUncompleteDetails } from 'app/shared/model/cp-uncomplete-details.model';
import { CPUncompleteDetailsService } from './cp-uncomplete-details.service';

@Component({
    selector: 'eb-cp-uncomplete-details-update',
    templateUrl: './cp-uncomplete-details-update.component.html'
})
export class CPUncompleteDetailsUpdateComponent implements OnInit {
    private _cPUncompleteDetails: ICPUncompleteDetails;
    isSaving: boolean;

    constructor(private cPUncompleteDetailsService: CPUncompleteDetailsService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ cPUncompleteDetails }) => {
            this.cPUncompleteDetails = cPUncompleteDetails;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.cPUncompleteDetails.id !== undefined) {
            this.subscribeToSaveResponse(this.cPUncompleteDetailsService.update(this.cPUncompleteDetails));
        } else {
            this.subscribeToSaveResponse(this.cPUncompleteDetailsService.create(this.cPUncompleteDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICPUncompleteDetails>>) {
        result.subscribe((res: HttpResponse<ICPUncompleteDetails>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get cPUncompleteDetails() {
        return this._cPUncompleteDetails;
    }

    set cPUncompleteDetails(cPUncompleteDetails: ICPUncompleteDetails) {
        this._cPUncompleteDetails = cPUncompleteDetails;
    }
}
