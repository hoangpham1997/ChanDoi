import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { BudgetItemService } from './budget-item.service';

@Component({
    selector: 'eb-budget-item-update',
    templateUrl: './budget-item-update.component.html'
})
export class BudgetItemUpdateComponent implements OnInit {
    private _budgetItem: IBudgetItem;
    isSaving: boolean;

    budgetitems: IBudgetItem[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private budgetItemService: BudgetItemService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ budgetItem }) => {
            this.budgetItem = budgetItem;
        });
        this.budgetItemService.query().subscribe(
            (res: HttpResponse<IBudgetItem[]>) => {
                this.budgetitems = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.budgetItem.id !== undefined) {
            this.subscribeToSaveResponse(this.budgetItemService.update(this.budgetItem));
        } else {
            this.subscribeToSaveResponse(this.budgetItemService.create(this.budgetItem));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBudgetItem>>) {
        result.subscribe((res: HttpResponse<IBudgetItem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackBudgetItemById(index: number, item: IBudgetItem) {
        return item.id;
    }
    get budgetItem() {
        return this._budgetItem;
    }

    set budgetItem(budgetItem: IBudgetItem) {
        this._budgetItem = budgetItem;
    }
}
