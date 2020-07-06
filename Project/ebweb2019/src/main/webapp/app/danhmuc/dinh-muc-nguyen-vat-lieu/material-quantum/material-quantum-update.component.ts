import { AfterViewInit, Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';

import { IMaterialQuantum } from 'app/shared/model/material-quantum.model';
import { MaterialQuantumService } from './material-quantum.service';
import { IMaterialQuantumDetails } from 'app/shared/model/material-quantum-details.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { IMaterialGoods, IMaterialGoodsInStock } from 'app/shared/model/material-goods.model';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { Principal } from 'app/core';
import { IObjectsMaterialQuantum } from 'app/shared/model/objects-material-quantum.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { JhiEventManager } from 'ng-jhipster';
import { IMBDepositDetails } from 'app/shared/model/mb-deposit-details.model';

@Component({
    selector: 'eb-material-quantum-update',
    templateUrl: './material-quantum-update.component.html',
    styleUrls: ['./material-quantum-update.component.css']
})
export class MaterialQuantumUpdateComponent extends BaseComponent implements OnInit, AfterViewInit {
    @ViewChild('content') content: TemplateRef<any>;
    @ViewChild('confirmDelete') popupDelete: TemplateRef<any>;
    @ViewChild('confirmEdit') popupEdit: TemplateRef<any>;
    private _materialQuantum: IMaterialQuantum;
    isSaving: boolean;
    objectName: string;
    materialQuantumDetails: IMaterialQuantumDetails[];
    materialGoodsInStock: IMaterialGoodsInStock[];
    units: IUnit[];
    contextMenu: ContextMenu;
    select: number;
    currentRow: number;
    isCreateUrl: boolean;
    currentAccount: any;
    objectsMaterialQuantum: IObjectsMaterialQuantum[];
    listColumnsMaterialQuantum: string[];
    listHeaderColumnsMaterialQuantum: string[];
    isSaveAndCreate: boolean;
    eventSubscriber: Subscription;
    modalRef: NgbModalRef;
    materialQuantumCopy: IMaterialQuantum;
    materialQuantumDetailsCopy: IMaterialQuantumDetails[];
    ROLE_DinhMucNguyenVatLieu_Xem = ROLE.DinhMucNguyenVatLieu_Xem;
    ROLE_DinhMucNguyenVatLieu_Them = ROLE.DinhMucNguyenVatLieu_Them;
    ROLE_DinhMucNguyenVatLieu_Sua = ROLE.DinhMucNguyenVatLieu_Sua;
    ROLE_DinhMucNguyenVatLieu_Xoa = ROLE.DinhMucNguyenVatLieu_Xoa;

    buttonDeleteTranslate = 'ebwebApp.mBDeposit.toolTip.delete';
    buttonAddTranslate = 'ebwebApp.mBDeposit.toolTip.add';
    buttonEditTranslate = 'ebwebApp.mBDeposit.toolTip.edit';
    buttonSaveTranslate = 'ebwebApp.mBDeposit.toolTip.save';
    buttonRecordTranslate = 'ebwebApp.mBDeposit.toolTip.record';
    buttonUnRecordTranslate = 'ebwebApp.mBDeposit.toolTip.unrecord';
    buttonPrintTranslate = 'ebwebApp.mBDeposit.toolTip.print';
    buttonSaveAndNewTranslate = 'ebwebApp.mBDeposit.toolTip.saveAndNew';
    buttonCopyAndNewTranslate = 'ebwebApp.mBDeposit.toolTip.copyAndNew';
    buttonCloseFormTranslate = 'ebwebApp.mBDeposit.toolTip.closeForm';

    arrAuthorities: any[];
    isRoleSua: boolean;
    isRoleThem: boolean;
    isEditUrl: boolean;

    constructor(
        private materialQuantumService: MaterialQuantumService,
        private activatedRoute: ActivatedRoute,
        public utilsService: UtilsService,
        private materialGoodsService: MaterialGoodsService,
        private unitService: UnitService,
        private router: Router,
        private principal: Principal,
        private toastr: ToastrService,
        private translateService: TranslateService,
        private modalService: NgbModal,
        private eventManager: JhiEventManager
    ) {
        super();
        this.contextMenu = new ContextMenu();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.arrAuthorities = account.authorities;
            this.isEditUrl = window.location.href.includes('edit');
            this.isCreateUrl = window.location.href.includes('/new');
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_DinhMucNguyenVatLieu_Sua)
                : true;
            this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_DinhMucNguyenVatLieu_Them)
                : true;
        });
    }

    ngOnInit() {
        this.isSaveAndCreate = false;
        this.objectsMaterialQuantum = [];
        this.listColumnsMaterialQuantum = ['objectCode', 'objectName', 'objectTypeString'];
        this.listHeaderColumnsMaterialQuantum = ['Mã đối tượng', 'Tên đối tượng', 'Loại'];
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ materialQuantum }) => {
            this.materialQuantum = materialQuantum;
            this.materialQuantumDetails = this.materialQuantum.materialQuantumDetails ? this.materialQuantum.materialQuantumDetails : [];
        });
        this.materialGoodsService.queryForComboboxGood().subscribe((res: HttpResponse<IMaterialGoods[]>) => {
            this.materialGoodsInStock = res.body;
        });
        this.unitService.getUnits().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
        });
        this.materialQuantumService.getObject().subscribe((res: HttpResponse<IObjectsMaterialQuantum[]>) => {
            if (this.materialQuantum && this.materialQuantum.objectID) {
                this.objectsMaterialQuantum = res.body.filter(a => a.isActive || a.objectID === this.materialQuantum.objectID);
            } else {
                this.objectsMaterialQuantum = res.body.filter(a => a.isActive);
            }
            if (this.materialQuantum.objectID) {
                this.selectChangeAfterSelect();
            }
            this.copy();
        });
        this.sumAfterDeleteByContextMenu();
        this.afterAddRow();
        this.registerCopyRow();
    }

    closeForm() {
        event.preventDefault();
        if (this.materialQuantumCopy) {
            if (!this.utilsService.isEquivalent(this.materialQuantum, this.materialQuantumCopy)) {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
                return;
            } else {
                this.closeAll();
                return;
            }
        } else {
            this.copy();
            this.closeAll();
            return;
        }
    }

    saveAndNew() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = true;
        if (this.checkError()) {
            this.materialQuantum.materialQuantumDetails = this.materialQuantumDetails;
            this.materialQuantum.objectType = this.objectsMaterialQuantum.find(
                a => a.objectID === this.materialQuantum.objectID
            ).objectType;
            if (this.materialQuantum.id !== undefined) {
                this.subscribeToSaveResponse(this.materialQuantumService.update(this.materialQuantum));
            } else {
                this.subscribeToSaveResponse(this.materialQuantumService.create(this.materialQuantum));
            }
        }
    }

    save() {
        event.preventDefault();
        if (
            this.materialQuantum.id &&
            (!this.utilsService.isEquivalent(this.materialQuantum, this.materialQuantumCopy) ||
                !this.utilsService.isEquivalentArray(this.materialQuantumDetails, this.materialQuantumDetailsCopy))
        ) {
            if (this.materialQuantum.id) {
                this.modalRef = this.modalService.open(this.popupEdit, { backdrop: 'static' });
                this.materialQuantum.isChange = true;
            }
        } else {
            this.materialQuantum.isChange = false;
            this.funcSave();
        }
    }

    funcSave() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isSaving = true;
        this.isSaveAndCreate = false;
        if (this.checkError()) {
            this.materialQuantum.materialQuantumDetails = this.materialQuantumDetails;
            this.materialQuantum.objectType = this.objectsMaterialQuantum.find(
                a => a.objectID === this.materialQuantum.objectID
            ).objectType;
            if (this.materialQuantum.id !== undefined) {
                this.subscribeToSaveResponse(this.materialQuantumService.update(this.materialQuantum));
            } else {
                this.subscribeToSaveResponse(this.materialQuantumService.create(this.materialQuantum));
            }
        }
    }

    continueSave() {
        this.funcSave();
    }

    checkError(): boolean {
        if (this.materialQuantum.fromDate > this.materialQuantum.toDate) {
            this.toastr.error(
                this.translateService.instant('ebwebApp.materialQuantum.fromDateMustBeLessThanToDate'),
                this.translateService.instant('ebwebApp.materialQuantum.error')
            );
            return false;
        }
        if (!this.materialQuantum.objectID) {
            this.toastr.error(
                this.translateService.instant('ebwebApp.materialQuantum.objectIDIsNotBlank'),
                this.translateService.instant('ebwebApp.materialQuantum.error')
            );
            return false;
        }
        if (!this.objectName) {
            this.toastr.error(
                this.translateService.instant('ebwebApp.materialQuantum.objectNameIsNotBlank'),
                this.translateService.instant('ebwebApp.materialQuantum.error')
            );
            return false;
        }
        if (!this.materialQuantum.fromDate) {
            this.toastr.error(
                this.translateService.instant('ebwebApp.materialQuantum.fromDateIsNotBlank'),
                this.translateService.instant('ebwebApp.materialQuantum.error')
            );
            return false;
        }
        if (!this.materialQuantum.toDate) {
            this.toastr.error(
                this.translateService.instant('ebwebApp.materialQuantum.toDateIsNotBlank'),
                this.translateService.instant('ebwebApp.materialQuantum.error')
            );
            return false;
        }
        for (let i = 0; i < this.materialQuantumDetails.length; i++) {
            if (this.materialQuantum.objectID === this.materialQuantumDetails[i].materialGoodsID) {
                this.toastr.error(
                    this.translateService.instant('ebwebApp.materialQuantum.objectIDMustNotEqualsMaterialGoodsID'),
                    this.translateService.instant('ebwebApp.materialQuantum.error')
                );
                return false;
            }
        }
        return true;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialQuantum>>) {
        result.subscribe((res: HttpResponse<IMaterialQuantum>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError(res));
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.copy();
        this.toastr.success(
            this.translateService.instant('ebwebApp.materialQuantum.editSuccess'),
            this.translateService.instant('ebwebApp.materialQuantum.message')
        );
        if (this.isSaveAndCreate) {
            this.materialQuantum = {};
            this.objectName = null;
            this.materialQuantumDetails = [];
        } else {
            this.router.navigate(['/material-quantum']);
        }
    }

    private onSaveError(res) {
        if (res.error.errorKey === 'duplicate') {
            this.toastr.error(
                this.translateService.instant('ebwebApp.materialQuantum.objectIDAlreadyExist'),
                this.translateService.instant('ebwebApp.materialQuantum.error')
            );
        }
        this.isSaving = false;
    }

    get materialQuantum() {
        return this._materialQuantum;
    }

    set materialQuantum(materialQuantum: IMaterialQuantum) {
        this._materialQuantum = materialQuantum;
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    onRightClick($event, data, selectedData, isNew, isDelete, select, currentRow) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.isCopy = true;
        this.contextMenu.selectedData = selectedData;
        this.select = select;
        this.currentRow = currentRow;
    }

    AddnewRow(eventData: any, select: number) {
        if (select === 0) {
            this.materialQuantumDetails.push(Object.assign({}));
            this.materialQuantumDetails[this.materialQuantumDetails.length - 1].id = undefined;
            this.materialQuantumDetails[this.materialQuantumDetails.length - 1].amount = 0;
            this.materialQuantumDetails[this.materialQuantumDetails.length - 1].orderPriority = this.materialQuantumDetails.length - 1;
            // this.utilsService.autoPrinciple(this.autoPrinciple, this.materialQuantumDetails[this.materialQuantumDetails.length - 1]);
            const nameTag: string = event.srcElement.id;
            const index: number = this.materialQuantumDetails.length - 1;
            const nameTagIndex: string = nameTag + String(index);
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(nameTagIndex);
                if (element) {
                    element.focus();
                }
            }, 0);
        } else {
        }
    }

    afterAddRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.materialQuantumDetails.push({});
        });
    }

    keyControlDelete(value: number, select: number) {
        if (select === 0) {
            this.materialQuantumDetails.splice(value, 1);
            let totalAmount = 0;

            for (let i = 0; i < this.materialQuantumDetails.length; i++) {
                totalAmount += this.materialQuantumDetails[i].amount;
            }
        }
        // else if (select === 1) {
        //     this.mBDepositDetailTax.splice(value, 1);
        // }
    }

    keyControlC(value: number) {
        if (value !== null && value !== undefined) {
            const ob: IMaterialQuantumDetails = Object.assign({}, this.materialQuantumDetails[value]);
            ob.id = undefined;
            ob.orderPriority = undefined;
            this.materialQuantumDetails.push(ob);
        } else {
            this.materialQuantumDetails.push({});
        }
    }

    delete() {
        event.preventDefault();
        if (this.materialQuantum.id) {
            this.router.navigate(['/material-quantum', { outlets: { popup: this.materialQuantum.id + '/delete' } }]);
        }
    }

    selectChangeAfterSelectDetails(index) {
        if (this.materialQuantumDetails[index].materialGoodsID) {
            const currentMaterialGoods = this.materialGoodsInStock.find(a => a.id === this.materialQuantumDetails[index].materialGoodsID);
            this.materialQuantumDetails[index].unitID = currentMaterialGoods.unitID;
            this.materialQuantumDetails[index].description = currentMaterialGoods.materialGoodsName;
        }
    }

    selectChangeAfterSelect() {
        if (this.materialQuantum.objectID && this.materialQuantum.objectID) {
            this.objectName = this.objectsMaterialQuantum.find(a => a.objectID === this.materialQuantum.objectID).objectName;
        }
    }

    calTotalAmount(i) {
        if (this.materialQuantumDetails[i].quantity && this.materialQuantumDetails[i].unitPrice) {
            this.materialQuantumDetails[i].amount = this.materialQuantumDetails[i].quantity * this.materialQuantumDetails[i].unitPrice;
        } else {
            this.materialQuantumDetails[i].amount = 0;
        }
    }

    /*
    * hàm ss du lieu 2 object và 2 mảng object
    * return true: neu tat ca giong nhau
    * return false: neu 1 trong cac object ko giong nhau
    * */
    canDeactive(): boolean {
        return this.utilsService.isEquivalent(this.materialQuantum, this.materialQuantumCopy);
    }

    copy() {
        this.materialQuantumCopy = Object.assign({}, this.materialQuantum);
        this.materialQuantumDetailsCopy = this.materialQuantumDetails.map(object => ({ ...object }));
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    saveContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isSaving = true;
        this.isSaveAndCreate = false;
        if (this.checkError()) {
            this.materialQuantum.materialQuantumDetails = this.materialQuantumDetails;
            if (this.materialQuantum.id !== undefined) {
                this.subscribeToSaveResponse(this.materialQuantumService.update(this.materialQuantum));
            } else {
                this.subscribeToSaveResponse(this.materialQuantumService.create(this.materialQuantum));
            }
        }
    }

    exit() {
        if (this.modalRef) {
            this.modalRef.close();
            return;
        }
    }

    close() {
        this.modalRef.close();
        this.copy();
        this.router.navigate(['/material-quantum']);
    }

    closeAll() {
        this.router.navigate(['/material-quantum']);
    }

    ngAfterViewInit(): void {
        if ((!this.isRoleSua && this.isEditUrl) || (!this.isRoleThem && this.isCreateUrl)) {
            this.focusFirstInput();
        }
    }

    addIfLastInput(i) {
        if (i === this.materialQuantumDetails.length - 1) {
            this.keyControlC(null);
        }
    }

    showEditConstrainRSIInwardOutward(content) {
        this.modalRef = this.modalService.open(content, { backdrop: 'static' });
    }

    showDeleteConstrainRSIInwardOutward(content) {
        this.modalRef = this.modalService.open(content, { backdrop: 'static' });
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            const ob: IMaterialQuantumDetails = Object.assign({}, this.contextMenu.selectedData);
            ob.id = undefined;
            ob.orderPriority = undefined;
            this.materialQuantumDetails.push(ob);
        });
    }

    sumAfterDeleteByContextMenu() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            this.materialQuantumDetails.splice(this.currentRow, 1);
        });
    }
}
