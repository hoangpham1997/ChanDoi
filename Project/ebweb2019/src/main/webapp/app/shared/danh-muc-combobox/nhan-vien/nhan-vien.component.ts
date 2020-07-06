import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { PaymentClauseService } from 'app/entities/payment-clause';
import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';
import { AccountingObjectGroupService } from 'app/danhmuc/accounting-object-group';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { AccountingObjectBankAccountService } from 'app/entities/accounting-object-bank-account';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { KhachHangNhaCungCapService } from 'app/shared/danh-muc-combobox/khach-hang-nha-cung-cap/khach-hang-nha-cung-cap.service';
import { ToastrService } from 'ngx-toastr';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { CategoryName } from 'app/app.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { AccountingObjectService } from 'app/danhmuc/employee';
import { Principal } from 'app/core';
import { BankService } from 'app/danhmuc/bank';
import { IBank } from 'app/shared/model/bank.model';

@Component({
    selector: 'eb-accounting-object-cobobox-nhan-vien',
    templateUrl: './nhan-vien.component.html',
    styleUrls: ['./nhan-vien.component.css']
})
export class EbEmployeeComboboxComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('content') content: TemplateRef<any>;
    private _accountingObject: IAccountingObject;
    isSaving: boolean;
    paymentclauses: IPaymentClause[];
    accountingobjectgroups: IAccountingObjectGroup[];
    accountingobjectbankaccounts: IAccountingObjectBankAccount[];
    organizationunits: IOrganizationUnit[];
    employeebirthdayDp: any;
    issuedateDp: any;
    typeObject: number;
    isSaveAndCreate: boolean;
    isEditUrl: boolean;
    isClose: boolean;
    modalRef: NgbModalRef;
    accountingObjectCoppy: IAccountingObject;
    data: IAccountingObject;
    currentAccount: any;
    contextMenu: ContextMenu;
    eventSubscriber: Subscription;
    currentRow: number;
    saveSuccess: boolean;
    accountingObjects: IAccountingObject[];
    isCheck: boolean;
    bank: IBank[];
    select: number;
    isReadOnly: false;
    indexFocusDetailCol: any;
    indexFocusDetailRow: any;
    listIDInputDeatil: any[] = ['bankAccount', 'bankName', 'bankBranchName', 'accountHolderName'];

    constructor(
        private jhiAlertService: JhiAlertService,
        private accountingObjectService: AccountingObjectService,
        private paymentClauseService: PaymentClauseService,
        private accountingObjectGroupService: AccountingObjectGroupService,
        private organizationUnitService: OrganizationUnitService,
        private accountingObjectBankAccountService: AccountingObjectBankAccountService,
        private activatedRoute: ActivatedRoute,
        private toastr: ToastrService,
        private router: Router,
        public translate: TranslateService,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private principal: Principal,
        private eventManager: JhiEventManager,
        private activeModal: NgbActiveModal,
        private bankService: BankService
    ) {
        super();
        this.contextMenu = new ContextMenu();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
    }

    ngOnInit() {
        this.isClose = false;
        this.isSaving = false;
        this.accountingObject = {};
        this.paymentClauseService.getPaymentClauses().subscribe(
            (res: HttpResponse<IPaymentClause[]>) => {
                this.paymentclauses = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.accountingObjectGroupService.getAllAccountingObjectGroup().subscribe(
            (res: HttpResponse<IAccountingObjectGroup[]>) => {
                this.accountingobjectgroups = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.organizationUnitService.getOrganizationUnits().subscribe(
            (res: HttpResponse<IOrganizationUnit[]>) => {
                this.organizationunits = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.bankService.getBanksByCompanyID().subscribe(
            (res: HttpResponse<IBank[]>) => {
                this.bank = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        // addbymran :lay list accountingObjectBankAccounts theo accountingObjectid
        if (this.accountingObject.id !== undefined) {
            this.accountingobjectbankaccounts =
                this.accountingObject.accountingObjectBankAccounts === undefined
                    ? []
                    : this.accountingObject.accountingObjectBankAccounts.sort((a, b) => (a.orderPriority > b.orderPriority ? 1 : -1));
        } else {
            this.accountingobjectbankaccounts = [];
        }
        if (!this.accountingObject.id) {
            this.accountingObject.objectType = null;
        }
        this.registerAddNewRow();
        this.registerDeleteRow();
        this.registerCopyRow();
        this.copy();
    }

    addIfLastInput() {
        this.keyDownAddRow(null);
    }

    keyDownAddRow(value: number) {
        if (value !== null && value !== undefined) {
            const ob: IAccountingObjectBankAccount = Object.assign({}, this.accountingobjectbankaccounts[value]);
            ob.id = undefined;
            ob.orderPriority = undefined;
            this.accountingobjectbankaccounts.push(ob);
        } else {
            this.accountingobjectbankaccounts.push({});
        }
    }

    // su kien addRowToInsert
    addRowToInsert() {
        this.accountingobjectbankaccounts.push({ accountHolderName: this.accountingObject.accountingObjectName });
    }

    previousState() {
        this.activeModal.close();
    }

    save() {
        event.preventDefault();
        this.accountingObject.isEmployee = true;
        this.isSaving = true;
        this.isSaveAndCreate = false;
        this.isCheck = false;
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.accountingObject.isEmployee === null || this.accountingObject.isEmployee === undefined) {
            this.accountingObject.isEmployee = false;
        }
        if (!this.accountingObject.accountingObjectCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObject.missAccountingObjectCodes'),
                this.translate.instant('ebwebApp.accountingObject.detail.notification')
            );
            return;
        }
        if (!this.accountingObject.accountingObjectName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObject.missAccountingObjectNames'),
                this.translate.instant('ebwebApp.accountingObject.detail.notification')
            );
            return;
        }
        if (!this.accountingObject.departmentId) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObject.missOrganizationUnit'),
                this.translate.instant('ebwebApp.accountingObject.detail.notification')
            );
            return;
        }
        if (this.accountingObject.taxCode) {
            if (!this.utilsService.checkMST(this.accountingObject.taxCode)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.taxCodeInvalid'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        if (!this.checkEmailValidate(this.accountingObject.email)) {
            return;
        }
        this.accountingObject.accountingObjectBankAccounts = this.accountingobjectbankaccounts;
        for (let i = 0; i < this.accountingObject.accountingObjectBankAccounts.length; i++) {
            this.accountingObject.accountingObjectBankAccounts[i].orderPriority = i + 1;
        }
        console.log(this.accountingObject);
        if (this.accountingObject.id !== undefined) {
            this.accountingObject.voucherRefCatalogDTOS = [];
            this.subscribeToSaveResponse(this.accountingObjectService.update(this.accountingObject));
        } else {
            this.accountingObject.isActive = true;
            this.subscribeToSaveResponse(this.accountingObjectService.create(this.accountingObject));
        }
    }

    saveAndNew() {
        event.preventDefault();
        this.accountingObject.isEmployee = true;
        this.isSaving = true;
        this.isSaveAndCreate = true;
        this.isCheck = true;
        if (!this.accountingObject.accountingObjectCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObject.missAccountingObjectCode'),
                this.translate.instant('ebwebApp.accountingObject.detail.notification')
            );
            return;
        }
        if (!this.accountingObject.accountingObjectName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObject.missAccountingObjectName'),
                this.translate.instant('ebwebApp.accountingObject.detail.notification')
            );
            return;
        }
        if (!this.accountingObject.departmentId) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObject.missOrganizationUnit'),
                this.translate.instant('ebwebApp.accountingObject.detail.notification')
            );
            return;
        }
        if (!this.checkEmailValidate(this.accountingObject.email)) {
            return;
        }
        if (this.accountingObject.taxCode) {
            if (!this.utilsService.checkMST(this.accountingObject.taxCode)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.taxCodeInvalid'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        this.accountingObject.accountingObjectBankAccounts = this.accountingobjectbankaccounts;
        for (let i = 0; i < this.accountingObject.accountingObjectBankAccounts.length; i++) {
            this.accountingObject.accountingObjectBankAccounts[i].orderPriority = i + 1;
        }
        console.log(this.accountingObject);
        if (this.accountingObject.id !== undefined) {
            this.accountingObject.voucherRefCatalogDTOS = [];
            this.subscribeToSaveResponse(this.accountingObjectService.update(this.accountingObject));
        } else {
            this.accountingObject.isActive = true;
            this.subscribeToSaveResponse(this.accountingObjectService.create(this.accountingObject));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.data = res.body.accountingObject;
                    this.emmit(res.body);
                    this.onSaveSuccess();
                } else {
                    this.toastr.error(this.translate.instant('ebwebApp.unit.existData'));
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    emmit(emp: any) {
        this.eventManager.broadcast({
            name: 'selectedEmployee',
            content: emp
        });
    }

    private onSaveSuccess() {
        this.isClose = true;
        this.saveSuccess = true;
        this.accountingobjectbankaccounts = [];
        if (this.isSaveAndCreate) {
            this.eventManager.broadcast({
                name: 'saveAndNewSuccess',
                content: { name: CategoryName.NHAN_VIEN, data: this.data, typeObject: this.typeObject }
            });
            this.accountingObject = {};
        } else {
            this.eventManager.broadcast({
                name: 'saveSuccess',
                content: { name: CategoryName.NHAN_VIEN, data: this.data, typeObject: this.typeObject }
            });
            this.isSaving = false;
            this.previousState();
        }
        if (this.isEditUrl) {
            this.toastr.success(
                this.translate.instant('ebwebApp.accountingObject.insertDataSuccess'),
                this.translate.instant('ebwebApp.accountingObject.detail.notification')
            );
        } else {
            this.toastr.success(
                this.translate.instant('ebwebApp.accountingObject.insertDataSuccess'),
                this.translate.instant('ebwebApp.accountingObject.detail.notification')
            );
        }
    }

    private onSaveError() {
        this.isSaving = false;
        if (this.isEditUrl) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObject.editDataFail'),
                this.translate.instant('ebwebApp.accountingObject.detail.notification')
            );
        } else {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObject.insertDataFail'),
                this.translate.instant('ebwebApp.accountingObject.detail.notification')
            );
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackPaymentClauseById(index: number, item: IPaymentClause) {
        return item.id;
    }

    trackAccountingObjectGroupById(index: number, item: IAccountingObjectGroup) {
        return item.id;
    }

    trackOrganizationUnitById(index: number, item: IOrganizationUnit) {
        return item.id;
    }

    trackAccountingObjectById(index: number, item: IAccountingObject) {
        return item.id;
    }

    get accountingObject() {
        return this._accountingObject;
    }

    set accountingObject(accountingObject: IAccountingObject) {
        this._accountingObject = accountingObject;
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    closeEdit(content) {
        if (!this.utilsService.isEquivalent(this.accountingObject, this.accountingObjectCoppy)) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else {
            this.close();
        }
    }

    copy() {
        this.accountingObjectCoppy = Object.assign({}, this.accountingObject);
    }

    canDeactive(): boolean {
        event.preventDefault();
        if (!this.isClose) {
            return this.utilsService.isEquivalent(this.accountingObject, this.accountingObjectCoppy);
        }
        return true;
    }

    checkEmailValidate(email: string): boolean {
        if (email && !email.includes('@')) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObject.emailNotValid'),
                this.translate.instant('ebwebApp.sAQuote.error.error')
            );
            return false;
        } else {
            return true;
        }
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    onRightClick($event, data, selectedData, isNew, isDelete, currentRow) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.selectedData = selectedData;
        this.currentRow = currentRow;
    }

    afterDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            this.accountingobjectbankaccounts.splice(this.currentRow, 1);
        });
    }

    afterCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            this.copyRow(this.contextMenu.selectedData);
        });
    }

    KeyPress(detail, key, select?: number) {
        switch (key) {
            case 'ctr + delete':
                this.removeRow(detail, select);
                break;
            case 'ctr + c':
                this.copyRow(detail);
                break;
            case 'ctr + insert':
                this.AddnewRow(select, true);
                break;
        }
    }

    actionFocus(indexCol, indexRow) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
    }

    registerAddNewRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.AddnewRow(this.select, true);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    AddnewRow(select: number, isRightClick?) {
        if (select === 0) {
            let lenght = 0;
            if (isRightClick) {
                this.accountingobjectbankaccounts.splice(this.indexFocusDetailRow + 1, 0, {});
                lenght = this.indexFocusDetailRow + 2;
            } else {
                this.accountingobjectbankaccounts.push({});
                lenght = this.accountingobjectbankaccounts.length;
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
                const idx: number = this.accountingobjectbankaccounts.length - 1;
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

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            this.copyRow(this.contextMenu.selectedData);
        });
    }

    copyRow(detail, isRigthClick?) {
        if (!this.getSelectionText() || isRigthClick) {
            const detailCopy: any = Object.assign({}, detail);
            detailCopy.id = null;
            this.accountingobjectbankaccounts.push(detailCopy);
            if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const lst = this.listIDInputDeatil;
                const col = this.indexFocusDetailCol;
                const row = this.accountingobjectbankaccounts.length - 1;
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
    registerDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            this.removeRow(this.contextMenu.selectedData, this.select);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    removeRow(detail: object, select: number) {
        if (select === 0) {
            this.accountingobjectbankaccounts.splice(this.accountingobjectbankaccounts.indexOf(detail), 1);
            if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                // vì còn trường hợp = 0
                if (this.accountingobjectbankaccounts.length > 0) {
                    let row = 0;
                    if (this.indexFocusDetailRow > this.accountingobjectbankaccounts.length - 1) {
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

    ngOnDestroy(): void {
        // !this.utilsService.isEquivalent(this.mCPayment, this.mCPaymentCopy) ||
        // !this.utilsService.isEquivalentArray(this.mCPaymentDetails, this.mCPaymentDetailsCopy) ||
    }

    close() {
        this.eventManager.broadcast({
            name: 'closePopup',
            content: null
        });
        this.activeModal.dismiss(false);
    }
}
