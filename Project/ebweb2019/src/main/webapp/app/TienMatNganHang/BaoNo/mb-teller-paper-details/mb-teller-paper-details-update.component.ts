import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IMBTellerPaperDetails } from 'app/shared/model/mb-teller-paper-details.model';
import { MBTellerPaperDetailsService } from './mb-teller-paper-details.service';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { MBTellerPaperService } from 'app/TienMatNganHang/BaoNo/mb-teller-paper';

@Component({
    selector: 'eb-mb-teller-paper-details-update',
    templateUrl: './mb-teller-paper-details-update.component.html'
})
export class MBTellerPaperDetailsUpdateComponent implements OnInit {
    private _mBTellerPaperDetails: IMBTellerPaperDetails;
    isSaving: boolean;

    mbtellerpapers: IMBTellerPaper[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private mBTellerPaperDetailsService: MBTellerPaperDetailsService,
        private mBTellerPaperService: MBTellerPaperService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ mBTellerPaperDetails }) => {
            this.mBTellerPaperDetails = mBTellerPaperDetails;
        });
        this.mBTellerPaperService.query().subscribe(
            (res: HttpResponse<IMBTellerPaper[]>) => {
                this.mbtellerpapers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        console.log(this.mBTellerPaperDetails);
        if (this.mBTellerPaperDetails.id !== undefined) {
            this.subscribeToSaveResponse(this.mBTellerPaperDetailsService.update(this.mBTellerPaperDetails));
        } else {
            this.subscribeToSaveResponse(this.mBTellerPaperDetailsService.create(this.mBTellerPaperDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMBTellerPaperDetails>>) {
        result.subscribe(
            (res: HttpResponse<IMBTellerPaperDetails>) => this.onSaveSuccess(),
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

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackMBTellerPaperById(index: number, item: IMBTellerPaper) {
        return item.id;
    }
    get mBTellerPaperDetails() {
        return this._mBTellerPaperDetails;
    }

    set mBTellerPaperDetails(mBTellerPaperDetails: IMBTellerPaperDetails) {
        this._mBTellerPaperDetails = mBTellerPaperDetails;
    }
}
