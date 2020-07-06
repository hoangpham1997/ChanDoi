import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IGOtherVoucherDetailExpenseAllocation } from 'app/shared/model/g-other-voucher-detail-expense-allocation.model';
import { GOtherVoucherDetailExpenseAllocationService } from './g-other-voucher-detail-expense-allocation.service';

@Component({
    selector: 'eb-g-other-voucher-detail-expense-allocation-update',
    templateUrl: './g-other-voucher-detail-expense-allocation-update.component.html'
})
export class GOtherVoucherDetailExpenseAllocationUpdateComponent implements OnInit {
    private _gOtherVoucherDetailExpenseAllocation: IGOtherVoucherDetailExpenseAllocation;
    isSaving: boolean;

    constructor(
        private gOtherVoucherDetailExpenseAllocationService: GOtherVoucherDetailExpenseAllocationService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ gOtherVoucherDetailExpenseAllocation }) => {
            this.gOtherVoucherDetailExpenseAllocation = gOtherVoucherDetailExpenseAllocation;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.gOtherVoucherDetailExpenseAllocation.id !== undefined) {
            this.subscribeToSaveResponse(
                this.gOtherVoucherDetailExpenseAllocationService.update(this.gOtherVoucherDetailExpenseAllocation)
            );
        } else {
            this.subscribeToSaveResponse(
                this.gOtherVoucherDetailExpenseAllocationService.create(this.gOtherVoucherDetailExpenseAllocation)
            );
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IGOtherVoucherDetailExpenseAllocation>>) {
        result.subscribe(
            (res: HttpResponse<IGOtherVoucherDetailExpenseAllocation>) => this.onSaveSuccess(),
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
    get gOtherVoucherDetailExpenseAllocation() {
        return this._gOtherVoucherDetailExpenseAllocation;
    }

    set gOtherVoucherDetailExpenseAllocation(gOtherVoucherDetailExpenseAllocation: IGOtherVoucherDetailExpenseAllocation) {
        this._gOtherVoucherDetailExpenseAllocation = gOtherVoucherDetailExpenseAllocation;
    }
}
