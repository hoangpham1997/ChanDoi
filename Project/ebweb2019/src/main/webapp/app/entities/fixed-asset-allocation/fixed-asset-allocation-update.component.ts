import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IFixedAssetAllocation } from 'app/shared/model/fixed-asset-allocation.model';
import { FixedAssetAllocationService } from './fixed-asset-allocation.service';
import { IFixedAsset } from 'app/shared/model/fixed-asset.model';
import { FixedAssetService } from 'app/entities/fixed-asset';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ExpenseItemService } from 'app/entities/expense-item';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { StatisticsCodeService } from 'app/entities/statistics-code';

@Component({
    selector: 'eb-fixed-asset-allocation-update',
    templateUrl: './fixed-asset-allocation-update.component.html'
})
export class FixedAssetAllocationUpdateComponent implements OnInit {
    private _fixedAssetAllocation: IFixedAssetAllocation;
    isSaving: boolean;

    fixedassets: IFixedAsset[];

    expenseitems: IExpenseItem[];

    accountlists: IAccountList[];

    statisticscodes: IStatisticsCode[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private fixedAssetAllocationService: FixedAssetAllocationService,
        private fixedAssetService: FixedAssetService,
        private expenseItemService: ExpenseItemService,
        private accountListService: AccountListService,
        private statisticsCodeService: StatisticsCodeService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ fixedAssetAllocation }) => {
            this.fixedAssetAllocation = fixedAssetAllocation;
        });
        this.fixedAssetService.query().subscribe(
            (res: HttpResponse<IFixedAsset[]>) => {
                this.fixedassets = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.expenseItemService.query().subscribe(
            (res: HttpResponse<IExpenseItem[]>) => {
                this.expenseitems = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.accountListService.query().subscribe(
            (res: HttpResponse<IAccountList[]>) => {
                this.accountlists = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.statisticsCodeService.query().subscribe(
            (res: HttpResponse<IStatisticsCode[]>) => {
                this.statisticscodes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.fixedAssetAllocation.id !== undefined) {
            this.subscribeToSaveResponse(this.fixedAssetAllocationService.update(this.fixedAssetAllocation));
        } else {
            this.subscribeToSaveResponse(this.fixedAssetAllocationService.create(this.fixedAssetAllocation));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFixedAssetAllocation>>) {
        result.subscribe(
            (res: HttpResponse<IFixedAssetAllocation>) => this.onSaveSuccess(),
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

    trackFixedAssetById(index: number, item: IFixedAsset) {
        return item.id;
    }

    trackExpenseItemById(index: number, item: IExpenseItem) {
        return item.id;
    }

    trackAccountListById(index: number, item: IAccountList) {
        return item.id;
    }

    trackStatisticsCodeById(index: number, item: IStatisticsCode) {
        return item.id;
    }

    get fixedAssetAllocation() {
        return this._fixedAssetAllocation;
    }

    set fixedAssetAllocation(fixedAssetAllocation: IFixedAssetAllocation) {
        this._fixedAssetAllocation = fixedAssetAllocation;
    }
}
