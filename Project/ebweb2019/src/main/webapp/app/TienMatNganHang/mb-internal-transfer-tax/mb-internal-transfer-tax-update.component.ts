import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IMBInternalTransferTax } from 'app/shared/model/mb-internal-transfer-tax.model';
import { MBInternalTransferTaxService } from './mb-internal-transfer-tax.service';
import { IMBInternalTransfer } from 'app/shared/model/mb-internal-transfer.model';
import { MBInternalTransferService } from 'app/TienMatNganHang/mb-internal-transfer';

@Component({
    selector: 'eb-mb-internal-transfer-tax-update',
    templateUrl: './mb-internal-transfer-tax-update.component.html'
})
export class MBInternalTransferTaxUpdateComponent implements OnInit {
    private _mBInternalTransferTax: IMBInternalTransferTax;
    isSaving: boolean;

    mbinternaltransfers: IMBInternalTransfer[];
    invoiceDateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private mBInternalTransferTaxService: MBInternalTransferTaxService,
        private mBInternalTransferService: MBInternalTransferService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ mBInternalTransferTax }) => {
            this.mBInternalTransferTax = mBInternalTransferTax;
        });
        this.mBInternalTransferService.query().subscribe(
            (res: HttpResponse<IMBInternalTransfer[]>) => {
                this.mbinternaltransfers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.mBInternalTransferTax.id !== undefined) {
            this.subscribeToSaveResponse(this.mBInternalTransferTaxService.update(this.mBInternalTransferTax));
        } else {
            this.subscribeToSaveResponse(this.mBInternalTransferTaxService.create(this.mBInternalTransferTax));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMBInternalTransferTax>>) {
        result.subscribe(
            (res: HttpResponse<IMBInternalTransferTax>) => this.onSaveSuccess(),
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

    trackMBInternalTransferById(index: number, item: IMBInternalTransfer) {
        return item.id;
    }
    get mBInternalTransferTax() {
        return this._mBInternalTransferTax;
    }

    set mBInternalTransferTax(mBInternalTransferTax: IMBInternalTransferTax) {
        this._mBInternalTransferTax = mBInternalTransferTax;
    }
}
