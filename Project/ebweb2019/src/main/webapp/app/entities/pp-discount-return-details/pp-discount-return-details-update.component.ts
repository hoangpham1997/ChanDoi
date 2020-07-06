import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IPPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';
import { PPDiscountReturnDetailsService } from './pp-discount-return-details.service';

@Component({
    selector: 'eb-pp-discount-return-details-update',
    templateUrl: './pp-discount-return-details-update.component.html'
})
export class PPDiscountReturnDetailsUpdateComponent implements OnInit {
    private _pPDiscountReturnDetails: IPPDiscountReturnDetails;
    isSaving: boolean;
    expiryDateDp: any;
    matchDateDp: any;

    constructor(private pPDiscountReturnDetailsService: PPDiscountReturnDetailsService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ pPDiscountReturnDetails }) => {
            this.pPDiscountReturnDetails = pPDiscountReturnDetails;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.pPDiscountReturnDetails.id !== undefined) {
            this.subscribeToSaveResponse(this.pPDiscountReturnDetailsService.update(this.pPDiscountReturnDetails));
        } else {
            this.subscribeToSaveResponse(this.pPDiscountReturnDetailsService.create(this.pPDiscountReturnDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPPDiscountReturnDetails>>) {
        result.subscribe(
            (res: HttpResponse<IPPDiscountReturnDetails>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get pPDiscountReturnDetails() {
        return this._pPDiscountReturnDetails;
    }

    set pPDiscountReturnDetails(pPDiscountReturnDetails: IPPDiscountReturnDetails) {
        this._pPDiscountReturnDetails = pPDiscountReturnDetails;
    }
}
