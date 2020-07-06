import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IPrepaidExpenseVoucher } from 'app/shared/model/prepaid-expense-voucher.model';
import { PrepaidExpenseVoucherService } from './prepaid-expense-voucher.service';

@Component({
    selector: 'eb-prepaid-expense-voucher-update',
    templateUrl: './prepaid-expense-voucher-update.component.html'
})
export class PrepaidExpenseVoucherUpdateComponent implements OnInit {
    private _prepaidExpenseVoucher: IPrepaidExpenseVoucher;
    isSaving: boolean;
    dateDp: any;

    constructor(private prepaidExpenseVoucherService: PrepaidExpenseVoucherService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ prepaidExpenseVoucher }) => {
            this.prepaidExpenseVoucher = prepaidExpenseVoucher;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.prepaidExpenseVoucher.id !== undefined) {
            this.subscribeToSaveResponse(this.prepaidExpenseVoucherService.update(this.prepaidExpenseVoucher));
        } else {
            this.subscribeToSaveResponse(this.prepaidExpenseVoucherService.create(this.prepaidExpenseVoucher));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPrepaidExpenseVoucher>>) {
        result.subscribe(
            (res: HttpResponse<IPrepaidExpenseVoucher>) => this.onSaveSuccess(),
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
    get prepaidExpenseVoucher() {
        return this._prepaidExpenseVoucher;
    }

    set prepaidExpenseVoucher(prepaidExpenseVoucher: IPrepaidExpenseVoucher) {
        this._prepaidExpenseVoucher = prepaidExpenseVoucher;
    }
}
