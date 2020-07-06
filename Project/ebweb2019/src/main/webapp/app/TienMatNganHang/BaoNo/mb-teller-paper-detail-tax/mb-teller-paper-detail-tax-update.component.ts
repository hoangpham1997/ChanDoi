import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IMBTellerPaperDetailTax } from 'app/shared/model/mb-teller-paper-detail-tax.model';
import { MBTellerPaperDetailTaxService } from './mb-teller-paper-detail-tax.service';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { MBTellerPaperService } from 'app/TienMatNganHang/BaoNo/mb-teller-paper';

@Component({
    selector: 'eb-mb-teller-paper-detail-tax-update',
    templateUrl: './mb-teller-paper-detail-tax-update.component.html'
})
export class MBTellerPaperDetailTaxUpdateComponent implements OnInit {
    private _mBTellerPaperDetailTax: IMBTellerPaperDetailTax;
    isSaving: boolean;

    mbtellerpapers: IMBTellerPaper[];
    invoicedateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private mBTellerPaperDetailTaxService: MBTellerPaperDetailTaxService,
        private mBTellerPaperService: MBTellerPaperService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ mBTellerPaperDetailTax }) => {
            this.mBTellerPaperDetailTax = mBTellerPaperDetailTax;
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
        if (this.mBTellerPaperDetailTax.id !== undefined) {
            this.subscribeToSaveResponse(this.mBTellerPaperDetailTaxService.update(this.mBTellerPaperDetailTax));
        } else {
            this.subscribeToSaveResponse(this.mBTellerPaperDetailTaxService.create(this.mBTellerPaperDetailTax));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMBTellerPaperDetailTax>>) {
        result.subscribe(
            (res: HttpResponse<IMBTellerPaperDetailTax>) => this.onSaveSuccess(),
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
    get mBTellerPaperDetailTax() {
        return this._mBTellerPaperDetailTax;
    }

    set mBTellerPaperDetailTax(mBTellerPaperDetailTax: IMBTellerPaperDetailTax) {
        this._mBTellerPaperDetailTax = mBTellerPaperDetailTax;
    }
}
