import { AfterViewInit, Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { CostSet, ICostSet } from 'app/shared/model/cost-set.model';
import { TranslateService } from '@ngx-translate/core';
import { IMaterialGoods, MaterialGoods } from 'app/shared/model/material-goods.model';
import { ICostSetMaterialGood } from 'app/shared/model/cost-set-material-good.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { ToastrService } from 'ngx-toastr';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IMBDepositDetails } from 'app/shared/model/mb-deposit-details.model';
import { ROLE } from 'app/role.constants';
import { CostSetService } from 'app/entities/cost-set';
import { JhiEventManager } from 'ng-jhipster';
import { CategoryName } from 'app/app.constants';

@Component({
    selector: 'eb-cost-set-update-popup',
    styleUrls: ['./cost-set-update.component.css'],
    templateUrl: './cost-set-update.component.html'
})
export class CostSetComboboxComponent extends BaseComponent implements OnInit, AfterViewInit {
    @ViewChild('content') content: TemplateRef<any>;

    isSaving: boolean;
    listType: any[];
    xcc: any;
    listColumnsType: string[] = ['name'];
    listHeaderColumnsStatus: string[] = ['Loại'];
    materialGoodObjects: IMaterialGoods[];
    costSetMaterialGoods: ICostSetMaterialGood[];
    cSetMaterialGoodsDetails: IMaterialGoodsCategory[];
    contextMenu: ContextMenu;
    select: number;
    isSaveAndCreate: boolean;
    modalRef: NgbModalRef;
    isClose: boolean;
    costSetObjectCopy: ICostSet;
    costSetMaterialGoodCopy: any;
    private _costSet: ICostSet;
    currentRow: number;
    // role
    ROLE_Them = ROLE.DanhMucDTTHCP_Them;
    ROLE_Sua = ROLE.DanhMucDTTHCP_Sua;
    ROLE_Xem = ROLE.DanhMucDTTHCP_Xem;
    ROLE_Xoa = ROLE.DanhMucDTTHCP_Xoa;
    data: ICostSet;

    constructor(
        private costSetService: CostSetService,
        private activatedRoute: ActivatedRoute,
        public translate: TranslateService,
        private materialGoodsService: MaterialGoodsService,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private router: Router,
        private modalService: NgbModal,
        private activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
        super();
        this.translate
            .get([
                'ebwebApp.costSet.Type.Order',
                'ebwebApp.costSet.Type.Construction',
                'ebwebApp.costSet.Type.Factory',
                'ebwebApp.costSet.Type.Manufacturing technology',
                'ebwebApp.costSet.Type.Product',
                'ebwebApp.costSet.Type.Others'
            ])
            .subscribe(res => {
                this.listType = [
                    { value: 0, name: this.translate.instant('ebwebApp.costSet.Type.Order') },
                    { value: 1, name: this.translate.instant('ebwebApp.costSet.Type.Construction') },
                    { value: 2, name: this.translate.instant('ebwebApp.costSet.Type.Factory') },
                    { value: 3, name: this.translate.instant('ebwebApp.costSet.Type.Manufacturing technology') },
                    { value: 4, name: this.translate.instant('ebwebApp.costSet.Type.Product') },
                    { value: 5, name: this.translate.instant('ebwebApp.costSet.Type.Others') }
                ];
            });
        this.materialGoodsService.queryForComboboxGood().subscribe((res: HttpResponse<IMaterialGoods[]>) => {
            this.materialGoodObjects = res.body;
        });
        this.contextMenu = new ContextMenu();
    }

    get costSet() {
        return this._costSet;
    }

    set costSet(costSet: ICostSet) {
        this._costSet = costSet;
    }

    ngOnInit() {
        this.isSaving = false;
        this.costSet = new CostSet();
        if (this.costSet.id) {
            this.costSetMaterialGoods = this.costSet.costSetMaterialGoods
                ? this.costSet.costSetMaterialGoods.sort((a, b) => a.orderPriority - b.orderPriority)
                : [];
        } else {
            this.costSetMaterialGoods = [];
        }
        this.copy();
        this.cSetMaterialGoodsDetails = [];
    }

    // su kien addRowToInsert
    addRowToInsert() {
        this.costSetMaterialGoods.push({});
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    previousState() {
        this.isClose = true;
        window.history.back();
    }

    KeyPress(value: number, select: number) {
        if (select === 0) {
            this.costSetMaterialGoods.splice(value, 1);
        }
    }

    keyDownAddRow(value: number) {
        if (value !== null && value !== undefined) {
            const ob: IMaterialGoods = Object.assign({}, this.costSetMaterialGoods[value]);
            ob.id = undefined;
            this.costSetMaterialGoods.push(ob);
        } else {
            this.costSetMaterialGoods.push({});
        }
    }

    removeRow(detail: object) {
        this.cSetMaterialGoodsDetails.splice(this.cSetMaterialGoodsDetails.indexOf(detail), 1);
    }

    copyRow(detail) {
        const detailCopy: any = Object.assign({}, detail);
        this.cSetMaterialGoodsDetails.push(detailCopy);
    }

    addNewRow() {}

    save() {
        event.preventDefault();
        this.isSaving = true;
        this.fillToSave();
        if (this.checkError()) {
            if (this.costSet.id !== undefined) {
                this.subscribeToSaveResponse(this.costSetService.update(this.costSet));
            } else {
                this.subscribeToSaveResponse(this.costSetService.create(this.costSet));
            }
        }
    }

    saveContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.fillToSave();
        if (this.checkError()) {
            if (this.costSet.id !== undefined) {
                this.subscribeToSaveResponseWhenClose(this.costSetService.update(this.costSet));
            } else {
                this.subscribeToSaveResponseWhenClose(this.costSetService.create(this.costSet));
            }
        }
    }

    private subscribeToSaveResponseWhenClose(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.close();
                } else if (res.body.status === 1) {
                    this.duplicateCostSetCode();
                }
            },
            (res: HttpErrorResponse) => this.onSaveError(res)
        );
    }

    private duplicateCostSetCode() {
        this.toastr.error(
            this.translate.instant('global.data.duplicateCostSetCode'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
    }

    saveAndNew() {
        event.preventDefault();
        this.isSaving = false;
        this.isSaveAndCreate = true;
        if (!this.costSet.costSetType && this.costSet.costSetType !== 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.costSet.missingCostSetType'),
                this.translate.instant('ebwebApp.accountingObject.detail.notification')
            );
            return;
        }
        if (!this.costSet.costSetCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.costSet.missingCostSetCode'),
                this.translate.instant('ebwebApp.accountingObject.detail.notification')
            );
            return;
        }
        if (!this.costSet.costSetName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.costSet.missingCostSetName'),
                this.translate.instant('ebwebApp.costSet.detail.notification')
            );
            return;
        }
        if (this.costSet.id !== undefined) {
            this.subscribeToSaveAndNewResponse(this.costSetService.update(this.costSet));
        } else {
            this.costSet.isActive = true;
            this.subscribeToSaveAndNewResponse(this.costSetService.create(this.costSet));
        }
    }

    resetForm() {
        this.costSet = {};
        this.costSetMaterialGoods = [];
        this.copy();
    }

    checkError() {
        if (!this.costSet.costSetCode) {
            this.toastr.error(this.translate.instant('ebwebApp.costSet.error.costSetCodeNull'));
            return false;
        } else if (!this.costSet.costSetName) {
            this.toastr.error(this.translate.instant('ebwebApp.costSet.error.costSetNameNull'));
            return false;
        } else if (!this.costSet.costSetType && this.costSet.costSetType !== 0) {
            this.toastr.error(this.translate.instant('ebwebApp.costSet.error.costSetTypeNull'));
            return false;
        }
        return true;
    }

    fillToSave() {
        for (let i = 0; i < this.costSetMaterialGoods.length; i++) {
            this.costSetMaterialGoods[i].orderPriority = i;
        }
        this.costSet.costSetMaterialGoods = this.costSetMaterialGoods;
    }

    onRightClick($event, data, selectedData, isNew, isDelete, select, currentRow) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.selectedData = selectedData;
        this.select = select;
        this.currentRow = currentRow;
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    close() {
        this.eventManager.broadcast({
            name: 'closePopup',
            content: null
        });
        this.activeModal.dismiss(false);
    }

    closeContent() {
        this.isClose = false;
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    closeForm() {
        this.isClose = true;
        if (
            !this.utilsService.isEquivalent(this.costSet, this.costSetObjectCopy) ||
            !this.utilsService.isEquivalentArray(this.costSetMaterialGoods, this.costSetMaterialGoodCopy)
        ) {
            this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
        } else {
            this.close();
        }
    }

    closeAll() {
        this.router.navigate(['/cost-set']);
    }

    copy() {
        this.costSetObjectCopy = Object.assign({}, this.costSet);
        this.costSetMaterialGoodCopy = this.costSetMaterialGoods.map(object => ({ ...object }));
    }

    canDeactive(): boolean {
        if (!this.isClose && !this.isSaveAndCreate) {
            return (
                this.utilsService.isEquivalent(this.costSet, this.costSetObjectCopy) &&
                this.utilsService.isEquivalentArray(this.costSetMaterialGoods, this.costSetMaterialGoodCopy)
            );
        }
        return true;
    }

    consoleLog() {
        console.log(this.costSet.costSetType);
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.eventManager.broadcast({
                        name: 'saveSuccess',
                        content: { name: CategoryName.DOI_TUONG_TAP_HOP_CHI_PHI, data: res.body.costSet }
                    });
                    this.onSaveSuccess();
                } else if (res.body.status === 1) {
                    this.duplicateCostSetCode();
                }
            },
            (res: HttpErrorResponse) => this.onSaveError(res)
        );
    }

    private subscribeToSaveAndNewResponse(result: Observable<HttpResponse<any>>) {
        this.isSaveAndCreate = true;
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.eventManager.broadcast({
                        name: 'saveAndNewSuccess',
                        content: { name: CategoryName.DOI_TUONG_TAP_HOP_CHI_PHI, data: res.body.costSet }
                    });
                    this.onSaveAndNewSuccess();
                    this.resetForm();
                } else if (res.body.status === 1) {
                    this.duplicateCostSetCode();
                }
            },
            (res: HttpErrorResponse) => this.onSaveError(res)
        );
    }

    private onSaveAndNewSuccess() {
        this.isSaving = false;
        this.resetForm();
    }

    private onSaveSuccess() {
        this.isSaving = false;
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.toastr.success(
            this.translate.instant('ebwebApp.costSet.home.saveSuccess'),
            this.translate.instant('ebwebApp.costSet.home.message')
        );
        // this.previousState();
        if (!this.isSaveAndCreate) {
            this.close();
        } else {
            this.reload();
        }
    }

    reload() {
        this.ngOnInit();
    }

    private onSaveError(err) {
        this.isSaving = false;
        if (err && err.error) {
            this.toastr.error(this.translate.instant(`ebwebApp.ebPackage.${err.error.message}`));
        }
    }

    /*delete() {
        event.preventDefault();
        if (this.costSet.id) {
            this.router.navigate(['/cost-set', { outlets: { popup: this.costSet.id + '/delete' } }]);
        }
    }*/

    delete() {
        event.preventDefault();
        this.router.navigate(['/cost-set', { outlets: { popup: this.costSet.id + '/delete' } }]);
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }

    addIfLastInput() {
        this.keyDownAddRow(null);
    }
}
