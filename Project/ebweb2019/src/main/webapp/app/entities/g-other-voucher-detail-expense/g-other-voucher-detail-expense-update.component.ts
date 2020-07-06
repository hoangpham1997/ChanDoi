import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IGOtherVoucherDetailExpense } from 'app/shared/model/g-other-voucher-detail-expense.model';
import { GOtherVoucherDetailExpenseService } from './g-other-voucher-detail-expense.service';

@Component({
    selector: 'eb-g-other-voucher-detail-expense-update',
    templateUrl: './g-other-voucher-detail-expense-update.component.html'
})
export class GOtherVoucherDetailExpenseUpdateComponent implements OnInit {
    private _gOtherVoucherDetailExpense: IGOtherVoucherDetailExpense;
    isSaving: boolean;

    constructor(private gOtherVoucherDetailExpenseService: GOtherVoucherDetailExpenseService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ gOtherVoucherDetailExpense }) => {
            this.gOtherVoucherDetailExpense = gOtherVoucherDetailExpense;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.gOtherVoucherDetailExpense.id !== undefined) {
            this.subscribeToSaveResponse(this.gOtherVoucherDetailExpenseService.update(this.gOtherVoucherDetailExpense));
        } else {
            this.subscribeToSaveResponse(this.gOtherVoucherDetailExpenseService.create(this.gOtherVoucherDetailExpense));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IGOtherVoucherDetailExpense>>) {
        result.subscribe(
            (res: HttpResponse<IGOtherVoucherDetailExpense>) => this.onSaveSuccess(),
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
    get gOtherVoucherDetailExpense() {
        return this._gOtherVoucherDetailExpense;
    }

    set gOtherVoucherDetailExpense(gOtherVoucherDetailExpense: IGOtherVoucherDetailExpense) {
        this._gOtherVoucherDetailExpense = gOtherVoucherDetailExpense;
    }
}
