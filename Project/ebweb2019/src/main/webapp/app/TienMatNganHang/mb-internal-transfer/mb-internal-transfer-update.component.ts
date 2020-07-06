import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IMBInternalTransfer } from 'app/shared/model/mb-internal-transfer.model';
import { MBInternalTransferService } from './mb-internal-transfer.service';

@Component({
    selector: 'eb-mb-internal-transfer-update',
    templateUrl: './mb-internal-transfer-update.component.html'
})
export class MBInternalTransferUpdateComponent implements OnInit {
    private _mBInternalTransfer: IMBInternalTransfer;
    isSaving: boolean;
    dateDp: any;
    postedDateDp: any;

    constructor(private mBInternalTransferService: MBInternalTransferService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ mBInternalTransfer }) => {
            this.mBInternalTransfer = mBInternalTransfer;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.mBInternalTransfer.id !== undefined) {
            this.subscribeToSaveResponse(this.mBInternalTransferService.update(this.mBInternalTransfer));
        } else {
            this.subscribeToSaveResponse(this.mBInternalTransferService.create(this.mBInternalTransfer));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMBInternalTransfer>>) {
        result.subscribe((res: HttpResponse<IMBInternalTransfer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get mBInternalTransfer() {
        return this._mBInternalTransfer;
    }

    set mBInternalTransfer(mBInternalTransfer: IMBInternalTransfer) {
        this._mBInternalTransfer = mBInternalTransfer;
    }
}
