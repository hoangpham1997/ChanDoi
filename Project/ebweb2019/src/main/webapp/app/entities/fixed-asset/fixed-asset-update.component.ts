import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IFixedAsset } from 'app/shared/model/fixed-asset.model';
import { FixedAssetService } from './fixed-asset.service';
import { IFixedAssetCategory } from 'app/shared/model/fixed-asset-category.model';
import { FixedAssetCategoryService } from 'app/entities/fixed-asset-category';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { IFixedAssetDetails } from 'app/shared/model/fixed-asset-details.model';
import { IFixedAssetAllocation } from 'app/shared/model/fixed-asset-allocation.model';
import { FixedAssetAccessories, IFixedAssetAccessories } from 'app/shared/model/fixed-asset-accessories.model';
import { IWarranty } from 'app/shared/model/warranty.model';
import { WarrantyService } from 'app/entities/warranty';
import { FixedAssetAllocationService } from 'app/entities/fixed-asset-allocation';
import { FixedAssetDetailsService } from 'app/entities/fixed-asset-details';
import { FixedAssetAccessoriesService } from 'app/entities/fixed-asset-accessories';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ExpenseItemService } from 'app/entities/expense-item';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { CostSetService } from 'app/entities/cost-set';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

@Component({
    selector: 'eb-fixed-asset-update',
    templateUrl: './fixed-asset-update.component.html',
    styleUrls: ['./fixed-asset-update.component.css']
})
export class FixedAssetUpdateComponent implements OnInit {
    private _fixedAsset: IFixedAsset;
    isSaving: boolean;
    fixedassetcategories: IFixedAssetCategory[];
    organizationunits: IOrganizationUnit[];
    accountingobjects: IAccountingObject[];
    suppliers: IAccountingObject[];

    accountlists: IAccountList[];
    fixedAssetDetails: IFixedAssetDetails[];
    fixedAssetAllocation: IFixedAssetAllocation[];
    fixedAssetCategory: IFixedAssetCategory[];
    fixedAssetAccessories: IFixedAssetAccessories[];
    warranty: IWarranty[];
    costAccount: IAccountList[];
    unit: IUnit[];
    expenseItem: IExpenseItem[];
    objects: ICostSet[];
    deliveryRecordDateDp: any;
    purchasedDateDp: any;
    incrementDateDp: any;
    depreciationDateDp: any;
    usedDateDp: any;
    i: number;

    constructor(
        private jhiAlertService: JhiAlertService,
        private fixedAssetService: FixedAssetService,
        private fixedAssetCategoryService: FixedAssetCategoryService,
        private organizationUnitService: OrganizationUnitService,
        private accountingObjectService: AccountingObjectService,
        private accountListService: AccountListService,
        private activatedRoute: ActivatedRoute,
        private warrantyService: WarrantyService,
        private fixedAssetAllocationService: FixedAssetAllocationService,
        private fixedAssetDetailsSerivce: FixedAssetDetailsService,
        private fixedAssetAccessoriesService: FixedAssetAccessoriesService,
        private unitService: UnitService,
        private accountListSerivce: AccountListService,
        private expenseItemService: ExpenseItemService,
        private costSetService: CostSetService,
        public utilsService: UtilsService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ fixedAsset }) => {
            this.fixedAsset = fixedAsset;
        });
        this.fixedAssetCategoryService.query().subscribe(
            (res: HttpResponse<IFixedAssetCategory[]>) => {
                this.fixedassetcategories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.organizationUnitService.query().subscribe(
            (res: HttpResponse<IOrganizationUnit[]>) => {
                this.organizationunits = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.accountingObjectService.query().subscribe(
            (res: HttpResponse<IAccountingObject[]>) => {
                this.accountingobjects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.accountListService.query().subscribe(
            (res: HttpResponse<IAccountList[]>) => {
                this.accountlists = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.warrantyService.query().subscribe(
            (res: HttpResponse<IWarranty[]>) => {
                this.warranty = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.fixedAssetAllocationService.query().subscribe(
            (res: HttpResponse<IFixedAssetAllocation[]>) => {
                this.fixedAssetAllocation = res.body.filter(n => n.fixedassetID === this.fixedAsset.id);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.costSetService.query().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.objects = res.body;
        });
        // this.fixedAssetAllocation = [];
        this.fixedAssetDetailsSerivce.query().subscribe(
            (res: HttpResponse<IFixedAssetDetails[]>) => {
                this.fixedAssetDetails = res.body.filter(n => n.fixedassetID === this.fixedAsset.id);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.fixedAssetAccessoriesService.query().subscribe(
            (res: HttpResponse<IFixedAssetAccessories[]>) => {
                this.fixedAssetAccessories = res.body.filter(n => n.fixedassetID === this.fixedAsset.id);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.accountListSerivce.query().subscribe(
            (res: HttpResponse<IAccountList[]>) => {
                this.costAccount = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.unitService.query().subscribe(
            (res: HttpResponse<IUnit[]>) => {
                this.unit = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.expenseItemService.query().subscribe(
            (res: HttpResponse<IExpenseItem[]>) => {
                this.expenseItem = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.accountingObjectService.query().subscribe(
            (res: HttpResponse<IAccountingObject[]>) => {
                this.suppliers = res.body.filter(supplier => supplier.objectType === 2 || supplier.objectType === 0);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    AddnewRow(select: number) {
        if (select === 0) {
            this.fixedAssetAllocation.push({});
        } else if (select === 1) {
            this.fixedAssetDetails.push({});
        } else if (select === 2) {
            this.fixedAssetAccessories.push({});
        }
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.fixedAsset.id !== undefined) {
            this.fixedAsset.fixedAssetAccessories = this.fixedAssetAccessories;
            this.fixedAsset.fixedAssetAllocation = this.fixedAssetAllocation;
            this.fixedAsset.fixedAssetDetails = this.fixedAssetDetails;
            for (this.i = 0; this.i < this.fixedAsset.fixedAssetAllocation.length; this.i++) {
                this.fixedAsset.fixedAssetAllocation[this.i].orderPriority = 0;
            }
            for (this.i = 0; this.i < this.fixedAsset.fixedAssetDetails.length; this.i++) {
                this.fixedAsset.fixedAssetDetails[this.i].orderPriority = 0;
            }
            this.subscribeToSaveResponse(this.fixedAssetService.update(this.fixedAsset));
        } else {
            this.fixedAsset.fixedAssetAccessories = this.fixedAssetAccessories;
            this.fixedAsset.fixedAssetAllocation = this.fixedAssetAllocation;
            this.fixedAsset.fixedAssetDetails = this.fixedAssetDetails;
            for (this.i = 0; this.i < this.fixedAsset.fixedAssetAllocation.length; this.i++) {
                this.fixedAsset.fixedAssetAllocation[this.i].orderPriority = 0;
            }
            for (this.i = 0; this.i < this.fixedAsset.fixedAssetDetails.length; this.i++) {
                this.fixedAsset.fixedAssetDetails[this.i].orderPriority = 0;
            }
            this.subscribeToSaveResponse(this.fixedAssetService.create(this.fixedAsset));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFixedAsset>>) {
        result.subscribe((res: HttpResponse<IFixedAsset>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackFixedAssetCategoryById(index: number, item: IFixedAssetCategory) {
        return item.id;
    }

    trackFixedAssetDetailsByID(index: number, item: IFixedAssetDetails) {
        return item.id;
    }

    trackOrganizationUnitById(index: number, item: IOrganizationUnit) {
        return item.id;
    }

    trackWarrantyById(index: number, item: IWarranty) {
        return item.id;
    }

    trackAccountingObjectById(index: number, item: IAccountingObject) {
        return item.id;
    }

    trackAccountListById(index: number, item: IAccountList) {
        return item.id;
    }

    get fixedAsset() {
        return this._fixedAsset;
    }

    set fixedAsset(fixedAsset: IFixedAsset) {
        this._fixedAsset = fixedAsset;
    }

    clearValue() {}

    saveAndCreate() {
        this.isSaving = true;
        if (this.fixedAsset.id !== undefined) {
            this.isSaving = false;
            this.subscribeToSaveResponse(this.fixedAssetService.update(this.fixedAsset));
        } else {
            this.isSaving = false;
            this.subscribeToSaveResponse(this.fixedAssetService.create(this.fixedAsset));
        }
        this.clearValue();
    }

    KeyPress(value: number, select: number) {
        if (select === 0) {
            this.fixedAssetAllocation.splice(value, 1);
        } else if (select === 1) {
            this.fixedAssetDetails.splice(value, 1);
        } else if (select === 2) {
            this.fixedAssetAccessories.splice(value, 1);
        }
    }
}
