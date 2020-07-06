import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IPrepaidExpense } from 'app/shared/model/prepaid-expense.model';
import { PrepaidExpenseService } from './prepaid-expense.service';

@Component({
    selector: 'eb-prepaid-expense-update',
    templateUrl: './prepaid-expense-update.component.html'
})
export class PrepaidExpenseUpdateComponent implements OnInit {
    private _prepaidExpense: IPrepaidExpense;
    isSaving: boolean;
    dateDp: any;

    constructor(private prepaidExpenseService: PrepaidExpenseService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ prepaidExpense }) => {
            this.prepaidExpense = prepaidExpense;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.prepaidExpense.id !== undefined) {
            this.subscribeToSaveResponse(this.prepaidExpenseService.update(this.prepaidExpense));
        } else {
            this.subscribeToSaveResponse(this.prepaidExpenseService.create(this.prepaidExpense));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPrepaidExpense>>) {
        result.subscribe((res: HttpResponse<IPrepaidExpense>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get prepaidExpense() {
        return this._prepaidExpense;
    }

    set prepaidExpense(prepaidExpense: IPrepaidExpense) {
        this._prepaidExpense = prepaidExpense;
    }
}
