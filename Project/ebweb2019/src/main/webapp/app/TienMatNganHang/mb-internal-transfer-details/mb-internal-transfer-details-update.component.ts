import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, pipe } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IMBInternalTransferDetails } from 'app/shared/model/mb-internal-transfer-details.model';
import { MBInternalTransferDetailsService } from './mb-internal-transfer-details.service';
import { IMBInternalTransfer } from 'app/shared/model/mb-internal-transfer.model';
import { MBInternalTransferService } from 'app/TienMatNganHang/mb-internal-transfer';
import { AccountingObject, IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';

@Component({
    selector: 'eb-mb-internal-transfer-details-update',
    templateUrl: './mb-internal-transfer-details-update.component.html'
})
export class MBInternalTransferDetailsUpdateComponent implements OnInit {
    private _mBInternalTransferDetails: IMBInternalTransferDetails;
    private _mBInternalTransfers: IMBInternalTransfer;
    isSaving: boolean;

    mbinternaltransfers: IMBInternalTransfer[];
    matchDateDp: any;
    postedDateDp: any;
    accObjects: IAccountingObject[];
    constructor(
        private jhiAlertService: JhiAlertService,
        private mBInternalTransferDetailsService: MBInternalTransferDetailsService,
        private mBInternalTransferService: MBInternalTransferService,
        private activatedRoute: ActivatedRoute,
        private accountingObjectService: AccountingObjectService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ mBInternalTransferDetails }) => {
            this.mBInternalTransferDetails = mBInternalTransferDetails;
        });
        this.mBInternalTransferService.query().subscribe(
            (res: HttpResponse<IMBInternalTransfer[]>) => {
                this.mbinternaltransfers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.accountingObjectService.query().subscribe((r: HttpResponse<IAccountingObject[]>) => {
            this.accObjects = r.body.filter(n => n.isEmployee === true && n.isActive === true);
            console.log(JSON.stringify(r.body));
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.mBInternalTransferDetails.id !== undefined) {
            this.subscribeToSaveResponse(this.mBInternalTransferDetailsService.update(this.mBInternalTransferDetails));
        } else {
            this.subscribeToSaveResponse(this.mBInternalTransferDetailsService.create(this.mBInternalTransferDetails));
        }
    }

    rowSelectedChange(id: string) {
        return this;
    }
    private subscribeToSaveResponse(result: Observable<HttpResponse<IMBInternalTransferDetails>>) {
        result.subscribe(
            (res: HttpResponse<IMBInternalTransferDetails>) => this.onSaveSuccess(),
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
    get mBInternalTransferDetails() {
        return this._mBInternalTransferDetails;
    }

    set mBInternalTransferDetails(mBInternalTransferDetails: IMBInternalTransferDetails) {
        this._mBInternalTransferDetails = mBInternalTransferDetails;
    }

    get mBInternalTransfers() {
        return this._mBInternalTransfers;
    }

    set mBInternalTransfers(mBInternalTransfers: IMBInternalTransfer) {
        this._mBInternalTransfers = mBInternalTransfers;
    }

    // Set value with accountingobjec
    // selectChange(){
    //     var acc = this.accObjects.find(n => n.id === this.mBInternalTransferDetails.accountingObjectID);
    //     if(acc != null){
    //         this.
    //     }
    // }
}
