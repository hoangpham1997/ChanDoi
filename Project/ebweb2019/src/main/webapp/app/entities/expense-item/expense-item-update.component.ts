import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ExpenseItemService } from './expense-item.service';

@Component({
    selector: 'eb-expense-item-update',
    templateUrl: './expense-item-update.component.html'
})
export class ExpenseItemUpdateComponent implements OnInit {
    private _expenseItem: IExpenseItem;
    isSaving: boolean;

    constructor(private expenseItemService: ExpenseItemService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ expenseItem }) => {
            this.expenseItem = expenseItem;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.expenseItem.id !== undefined) {
            this.subscribeToSaveResponse(this.expenseItemService.update(this.expenseItem));
        } else {
            this.subscribeToSaveResponse(this.expenseItemService.create(this.expenseItem));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IExpenseItem>>) {
        result.subscribe((res: HttpResponse<IExpenseItem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get expenseItem() {
        return this._expenseItem;
    }

    set expenseItem(expenseItem: IExpenseItem) {
        this._expenseItem = expenseItem;
    }
}
