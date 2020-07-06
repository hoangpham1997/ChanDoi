import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IFixedAssetCategory } from 'app/shared/model/fixed-asset-category.model';
import { FixedAssetCategoryService } from './fixed-asset-category.service';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IBank } from 'app/shared/model/bank.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

@Component({
    selector: 'eb-fixed-asset-category-update',
    templateUrl: './fixed-asset-category-update.component.html'
})
export class FixedAssetCategoryUpdateComponent implements OnInit {
    private _fixedAssetCategory: IFixedAssetCategory;
    isSaving: boolean;
    vFixedAssetCategoryCode: any;
    vFixedAssetCategoryName: any;
    vDescription: any;
    vUsedTime: any;
    vDepreciationRate: any;
    vOriginalPriceAccount: any;
    vDepreciationAccount: any;
    vExpenditureAccount: any;
    vIsActive: any;
    selectedRow: IFixedAssetCategory;
    accountLists: IAccountList[];

    fixedassetcategories: IFixedAssetCategory[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private fixedAssetCategoryService: FixedAssetCategoryService,
        private accountListService: AccountListService,
        private activatedRoute: ActivatedRoute,
        public utilsService: UtilsService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ fixedAssetCategory }) => {
            this.fixedAssetCategory = fixedAssetCategory;
        });
        this.fixedAssetCategoryService.query().subscribe(
            (res: HttpResponse<IFixedAssetCategory[]>) => {
                this.fixedassetcategories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.accountListService.query().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountLists = res.body;
        });
    }

    previousState() {
        window.history.back();
    }

    clearValue() {
        this.fixedAssetCategory.fixedAssetCategoryCode = null;
        this.fixedAssetCategory.fixedAssetCategoryName = null;
        this.fixedAssetCategory.isActive = false;
        this.fixedAssetCategory.depreciationAccount = null;
        this.fixedAssetCategory.depreciationRate = null;
        this.fixedAssetCategory.description = null;
        this.fixedAssetCategory.expenditureAccount = null;
        this.fixedAssetCategory.originalPriceAccount = null;
        this.fixedAssetCategory.usedTime = null;
    }

    save() {
        if (this.fixedAssetCategory.id !== undefined) {
            this.isSaving = false;
            this.subscribeToSaveResponse(this.fixedAssetCategoryService.update(this.fixedAssetCategory));
        } else {
            this.isSaving = true;
            this.subscribeToSaveResponse(this.fixedAssetCategoryService.create(this.fixedAssetCategory));
        }
    }

    saveAndCreate() {
        this.isSaving = true;
        if (this.fixedAssetCategory.id !== undefined) {
            this.isSaving = false;
            this.subscribeToSaveAndCreateResponse(this.fixedAssetCategoryService.update(this.fixedAssetCategory));
        } else {
            this.isSaving = true;
            this.subscribeToSaveAndCreateResponse(this.fixedAssetCategoryService.create(this.fixedAssetCategory));
        }
        this.clearValue();
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFixedAssetCategory>>) {
        result.subscribe((res: HttpResponse<IFixedAssetCategory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private subscribeToSaveAndCreateResponse(result: Observable<HttpResponse<IFixedAssetCategory>>) {
        result.subscribe(
            (res: HttpResponse<IFixedAssetCategory>) => this.onSaveAndCreateSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveAndCreateSuccess() {
        this.isSaving = false;
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackFixedAssetCategoryById(index: number, item: IFixedAssetCategory) {
        return item.id;
    }

    trackAccountById(index: number, item: IAccountList) {
        return item.id;
    }

    get fixedAssetCategory() {
        return this._fixedAssetCategory;
    }

    set fixedAssetCategory(fixedAssetCategory: IFixedAssetCategory) {
        this._fixedAssetCategory = fixedAssetCategory;
    }

    onSelect(select: IFixedAssetCategory) {
        this.selectedRow = select;
        console.log(`selectedTest1 = ${JSON.stringify(this.selectedRow)}`);
        // alert(`selectedTest1 = ${JSON.stringify(this.selectedTest1)}`);
    }
}
