import { AfterViewInit, Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';

import { ICostSet } from 'app/shared/model/cost-set.model';
import { CostSetService } from './cost-set.service';
import { TranslateService } from '@ngx-translate/core';
import { IMaterialGoods, MaterialGoods } from 'app/shared/model/material-goods.model';
import { ICostSetMaterialGood } from 'app/shared/model/cost-set-material-good.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { ToastrService } from 'ngx-toastr';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { Principal } from 'app/core';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'eb-cost-set-update',
    styleUrls: ['./cost-set-update.component.css'],
    templateUrl: './cost-set-update.component.html'
})
export class CostSetUpdateComponent extends BaseComponent implements OnInit, AfterViewInit, OnDestroy {
    @ViewChild('content') content: TemplateRef<any>;
    isSaving: boolean;
    listType: any[];
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
    eventSubscriber: Subscription;
    eventSubscriber1: Subscription;
    eventSubscriber2: Subscription;
    materialGoodss: any[];
    isNew: boolean;

    isCreateUrl: boolean;
    currentAccount: any;
    arrAuthorities: any[];
    isRoleSua: boolean;
    isRoleThem: boolean;
    isEditUrl: boolean;
    isReadOnly: false;
    indexFocusDetailCol: any;
    indexFocusDetailRow: any;
    listIDInputDeatil: any[] = ['costSetMaterialGoodsCode', 'costSetMaterialGoodsName', 'costSetMaterialGoodsDes'];
    // role
    ROLE_Them = ROLE.DanhMucDTTHCP_Them;
    ROLE_Sua = ROLE.DanhMucDTTHCP_Sua;
    ROLE_Xem = ROLE.DanhMucDTTHCP_Xem;
    ROLE_Xoa = ROLE.DanhMucDTTHCP_Xoa;

    constructor(
        private costSetService: CostSetService,
        private activatedRoute: ActivatedRoute,
        public translate: TranslateService,
        private materialGoodsService: MaterialGoodsService,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private router: Router,
        private modalService: NgbModal,
        private principal: Principal,
        private eventManager: JhiEventManager
    ) {
        super();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.arrAuthorities = account.authorities;
            this.isEditUrl = window.location.href.includes('edit');
            this.isCreateUrl = window.location.href.includes('/new');
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_Sua) : true;
            this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_Them) : true;
            console.log(this.isRoleSua);
            console.log(this.isEditUrl);
        });
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
        this.contextMenu = new ContextMenu();
    }

    get costSet() {
        return this._costSet;
    }

    set costSet(costSet: ICostSet) {
        this._costSet = costSet;
    }

    ngOnInit() {
        if (this.eventSubscribers) {
            this.eventSubscribers.forEach(ev => {
                ev.unsubscribe();
            });
        }
        this.isSaving = false;
        this.materialGoodsService.queryForComboboxGood().subscribe((res: HttpResponse<IMaterialGoods[]>) => {
            this.materialGoodss = res.body.filter(x => x.materialGoodsType === 1 || x.materialGoodsType === 3);
            this.costSet.materialGoods = this.materialGoodss.find(x => x.materialGoodsCode === this.costSet.costSetCode);
            this.copy();
        });
        this.activatedRoute.data.subscribe(({ costSet }) => {
            this.costSet = costSet;
            if (this.costSet.id) {
                this.costSetMaterialGoods = this.costSet.costSetMaterialGoods
                    ? this.costSet.costSetMaterialGoods.sort((a, b) => a.orderPriority - b.orderPriority)
                    : [];
                this.isNew = false;
            } else {
                this.costSetMaterialGoods = [];
                this.isNew = true;
            }
        });
        this.registerAddNewRow();
        this.registerDeleteRow();
        this.registerCopyRow();
        if (this.utilsService.isShowPopup === undefined || this.utilsService.isShowPopup === null) {
            this.utilsService.isShowPopup = false;
        }
        this.eventSubscriber = this.eventManager.subscribe('saveSuccessDacnm', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        this.eventSubscriber1 = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = true;
            this.registerComboboxSave(response);
        });
        this.eventSubscribers.push(this.eventSubscriber1);
        this.eventSubscriber2 = this.eventManager.subscribe('closePopup', response => {
            this.utilsService.setShowPopup(response.content);
        });
        this.eventSubscribers.push(this.eventSubscriber2);
        this.registerIsShowPopup();
    }

    // su kien addRowToInsert
    addRowToInsert() {
        if (this.isReadOnly) {
            return;
        }
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

    /*KeyPress(detail: object, key: string) {
        switch (key) {
            case 'ctr + delete':
                this.removeRow(detail);
                break;
            case 'ctr + c':
                this.copyRow(detail);
                break;
            case 'ctr + insert':
                this.AddnewRow();
                break;
        }
    }*/

    keyPress(value: number, select: number) {
        if (select === 0) {
            this.costSetMaterialGoods.splice(value, 1);
            for (let i = 0; i < this.costSetMaterialGoods.length; i++) {}
        }
    }

    keyDownAddRow(value: number) {
        if (value !== null && value !== undefined) {
            const ob: ICostSetMaterialGood = Object.assign({}, this.costSetMaterialGoods[value]);
            ob.id = undefined;
            ob.orderPriority = undefined;
            this.costSetMaterialGoods.push(ob);
        } else {
            this.costSetMaterialGoods.push({});
        }
    }

    copyRow(detail, select) {
        /*if (!this.getSelectionText()) {
            const addRow = this.utilsService.deepCopyObject(this.costSetMaterialGoods[value]);
            addRow.id = null;
            this.costSetMaterialGoods.push(addRow);
        }*/
        if (select === 0) {
            const detailCopy: any = Object.assign({}, detail);
            detailCopy.id = null;
            this.costSetMaterialGoods.push(detailCopy);
            if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const lst = this.listIDInputDeatil;
                const col = this.indexFocusDetailCol;
                const row = this.costSetMaterialGoods.length - 1;
                this.indexFocusDetailRow = row;
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(lst[col] + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
        }
    }

    // addNewRow() {}

    save() {
        event.preventDefault();
        if (!this.utilsService.isShowPopup) {
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
    }

    registerIsShowPopup() {
        this.utilsService.checkEvent.subscribe(res => {
            this.isShowPopup = res;
        });
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
            (res: HttpErrorResponse) => this.onSaveError()
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
        const errorData = this.costSetMaterialGoods.filter(item => !item.materialGoods);
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

    onRightClick($event, data, selectedData, isNew, isDelete, select) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.isCopy = true;
        this.contextMenu.selectedData = selectedData;
        this.select = select;
    }

    saveRow(i) {
        this.currentRow = i;
    }

    removeRow(detail: object, select: number) {
        if (select === 0) {
            this.costSetMaterialGoods.splice(this.costSetMaterialGoods.indexOf(detail), 1);
            if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                // vì còn trường hợp = 0
                if (this.costSetMaterialGoods.length > 0) {
                    let row = 0;
                    if (this.indexFocusDetailRow > this.costSetMaterialGoods.length - 1) {
                        row = this.indexFocusDetailRow - 1;
                    } else {
                        row = this.indexFocusDetailRow;
                    }
                    const lst = this.listIDInputDeatil;
                    const col = this.indexFocusDetailCol;
                    setTimeout(function() {
                        const element: HTMLElement = document.getElementById(lst[col] + row);
                        if (element) {
                            element.focus();
                        }
                    }, 0);
                }
            }
        }
    }

    registerAddNewRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.AddnewRow(this.select, true);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            this.removeRow(this.contextMenu.selectedData, this.select);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            this.copyRow(this.contextMenu.selectedData, this.select);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isClose = true;
        this.router.navigate(['cost-set']);
    }

    closeContent() {
        this.isClose = false;
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    copy() {
        this.costSetObjectCopy = Object.assign({}, this.costSet);
        this.costSetMaterialGoodCopy = this.costSetMaterialGoods.map(object => ({ ...object }));
    }

    closeForm() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isClose = true;
        console.log(this.costSet);
        console.log(this.costSetObjectCopy);
        if (
            !this.costSetObjectCopy ||
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

    canDeactive(): boolean {
        if (!this.isClose && !this.isSaveAndCreate) {
            return (
                this.utilsService.isEquivalent(this.costSet, this.costSetObjectCopy) &&
                this.utilsService.isEquivalentArray(this.costSetMaterialGoods, this.costSetMaterialGoodCopy)
            );
        }
        return true;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.costSet.id = res.body.costSet.id;
                    this.router.navigate(['/cost-set', res.body.costSet.id, 'edit']);
                } else if (res.body.status === 1) {
                    this.duplicateCostSetCode();
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private subscribeToSaveAndNewResponse(result: Observable<HttpResponse<any>>) {
        this.isSaveAndCreate = true;
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveAndNewSuccess();
                    this.resetForm();
                } else if (res.body.status === 1) {
                    this.duplicateCostSetCode();
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveAndNewSuccess() {
        this.isSaving = false;
        this.toastr.success(
            this.translate.instant('ebwebApp.costSet.home.saveSuccess'),
            this.translate.instant('ebwebApp.costSet.home.message')
        );
        this.resetForm();
    }

    private onSaveSuccess() {
        this.isSaving = false;
        if (this.isEditUrl) {
            this.toastr.success(
                this.translate.instant('ebwebApp.costSet.home.saveSuccess'),
                this.translate.instant('ebwebApp.costSet.home.message')
            );
        } else {
            this.toastr.success(
                this.translate.instant('ebwebApp.costSet.home.saveSuccess'),
                this.translate.instant('ebwebApp.costSet.home.message')
            );
        }
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    /*delete() {
        event.preventDefault();
        if (this.costSet.id) {
            this.router.navigate(['/cost-set', { outlets: { popup: this.costSet.id + '/delete' } }]);
        }
    }*/

    delete() {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.router.navigate(['/cost-set', { outlets: { popup: this.costSet.id + '/delete' } }]);
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }

    addIfLastInput() {
        this.keyDownAddRow(null);
    }

    /*private AddnewRow() {
        this.cSetMaterialGoodsDetails.push({});
    }*/

    AddnewRow(select: number, isRightClick?) {
        if (select === 0) {
            let lenght = 0;
            if (isRightClick) {
                this.costSetMaterialGoods.splice(this.indexFocusDetailRow + 1, 0, {});
                lenght = this.indexFocusDetailRow + 2;
            } else {
                this.costSetMaterialGoods.push({});
                lenght = this.costSetMaterialGoods.length;
            }
            if (isRightClick && this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const lst = this.listIDInputDeatil;
                const col = this.indexFocusDetailCol;
                const row = this.indexFocusDetailRow + 1;
                this.indexFocusDetailRow = row;
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(lst[col] + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            } else {
                const nameTag: string = event.srcElement.id;
                const idx: number = this.costSetMaterialGoods.length - 1;
                const nameTagIndex: string = nameTag + String(idx);
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(nameTagIndex);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
        }
    }

    afterDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            this.costSetMaterialGoods.splice(this.currentRow, 1);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    afterCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            this.copyRow(this.contextMenu.selectedData, this.select);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    KeyPress(detail, key, select?: number) {
        switch (key) {
            case 'ctr + delete':
                this.removeRow(detail, select);
                break;
            case 'ctr + c':
                this.copyRow(detail, select);
                break;
            case 'ctr + insert':
                this.AddnewRow(select, true);
                break;
        }
    }

    selectOnChangeMaterialGoods() {
        this.costSet.costSetCode = this.costSet.materialGoods.materialGoodsCode;
        this.costSet.costSetName = this.costSet.materialGoods.materialGoodsName;
        this.costSet.materialGoodsID = this.costSet.materialGoods.id;
    }

    selectChangeMaterialGoods(detail: ICostSetMaterialGood) {
        if (detail.materialGoods) {
            detail.materialGoodsID = detail.materialGoods.id;
            detail.deScription = detail.materialGoods.materialGoodsName;
        }
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.costSet;
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.costSetMaterialGoods;
    }

    addDataToDetail() {
        this.costSetMaterialGoods = this.details ? this.details : this.costSetMaterialGoods;
        this.costSet = this.parent ? this.parent : this.costSet;
    }

    actionFocus(indexCol, indexRow) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
    }
}
