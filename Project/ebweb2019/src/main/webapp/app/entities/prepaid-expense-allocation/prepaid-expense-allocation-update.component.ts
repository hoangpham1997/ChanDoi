import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IPrepaidExpenseAllocation } from 'app/shared/model/prepaid-expense-allocation.model';
import { PrepaidExpenseAllocationService } from './prepaid-expense-allocation.service';

@Component({
    selector: 'eb-prepaid-expense-allocation-update',
    templateUrl: './prepaid-expense-allocation-update.component.html'
})
export class PrepaidExpenseAllocationUpdateComponent implements OnInit {
    private _prepaidExpenseAllocation: IPrepaidExpenseAllocation;
    isSaving: boolean;

    constructor(private prepaidExpenseAllocationService: PrepaidExpenseAllocationService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ prepaidExpenseAllocation }) => {
            this.prepaidExpenseAllocation = prepaidExpenseAllocation;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.prepaidExpenseAllocation.id !== undefined) {
            this.subscribeToSaveResponse(this.prepaidExpenseAllocationService.update(this.prepaidExpenseAllocation));
        } else {
            this.subscribeToSaveResponse(this.prepaidExpenseAllocationService.create(this.prepaidExpenseAllocation));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPrepaidExpenseAllocation>>) {
        result.subscribe(
            (res: HttpResponse<IPrepaidExpenseAllocation>) => this.onSaveSuccess(),
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
    get prepaidExpenseAllocation() {
        return this._prepaidExpenseAllocation;
    }

    set prepaidExpenseAllocation(prepaidExpenseAllocation: IPrepaidExpenseAllocation) {
        this._prepaidExpenseAllocation = prepaidExpenseAllocation;
    }
}
