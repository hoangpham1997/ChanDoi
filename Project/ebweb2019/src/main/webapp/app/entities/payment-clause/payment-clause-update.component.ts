import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { PaymentClauseService } from './payment-clause.service';

@Component({
    selector: 'eb-payment-clause-update',
    templateUrl: './payment-clause-update.component.html'
})
export class PaymentClauseUpdateComponent implements OnInit {
    private _paymentClause: IPaymentClause;
    isSaving: boolean;

    constructor(private paymentClauseService: PaymentClauseService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ paymentClause }) => {
            this.paymentClause = paymentClause;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.paymentClause.id !== undefined) {
            this.subscribeToSaveResponse(this.paymentClauseService.update(this.paymentClause));
        } else {
            this.subscribeToSaveResponse(this.paymentClauseService.create(this.paymentClause));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentClause>>) {
        result.subscribe((res: HttpResponse<IPaymentClause>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get paymentClause() {
        return this._paymentClause;
    }

    set paymentClause(paymentClause: IPaymentClause) {
        this._paymentClause = paymentClause;
    }
}
