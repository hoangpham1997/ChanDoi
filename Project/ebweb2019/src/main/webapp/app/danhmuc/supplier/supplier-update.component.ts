import { AfterViewInit, Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from './supplier.service';
import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { PaymentClauseService } from 'app/entities/payment-clause';
import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';
import { AccountingObjectGroupService } from 'app/danhmuc/accounting-object-group';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { AccountingObjectBankAccount, IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { AccountingObjectBankAccountService } from 'app/entities/accounting-object-bank-account';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IMBDepositDetails } from 'app/shared/model/mb-deposit-details.model';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IUnit } from 'app/shared/model/unit.model';
import { ROLE } from 'app/role.constants';
import { IBank } from 'app/shared/model/bank.model';
import { BankService } from 'app/danhmuc/bank';
import { Principal } from 'app/core';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-accounting-object-update',
    templateUrl: './supplier-update.component.html',
    styleUrls: ['./supplier-update.component.css']
})
export class AccountingObjectUpdateComponent extends BaseComponent implements OnInit, AfterViewInit, OnDestroy {
    @ViewChild('content') content: TemplateRef<any>;
    private _accountingObject: IAccountingObject;
    isSaving: boolean;
    paymentclauses: IPaymentClause[];
    accountingobjectgroups: IAccountingObjectGroup[];
    accountingobjectbankaccounts: IAccountingObjectBankAccount[];
    bank: IBank[];
    organizationunits: IOrganizationUnit[];
    employeebirthdayDp: any;
    issuedateDp: any;
    isSaveAndCreate: boolean;
    isEditUrl: boolean;
    isClose: boolean;
    modalRef: NgbModalRef;
    accountingObjectCoppy: IAccountingObject;
    listAccountingObjectGroups: string[];
    listHeaderColumnsAccountingObjectGroups: string[];
    contextMenu: ContextMenu;
    eventSubscriber: Subscription;
    currentRow: number;
    arrAuthorities: any[];
    isRoleSua: boolean;
    isRoleThem: boolean;
    currentAccountingObject: any;
    isCreateUrl: boolean;
    select: number;
    isReadOnly: false;
    indexFocusDetailCol: any;
    indexFocusDetailRow: any;
    listIDInputDeatil: any[] = ['bankAccount', 'bankName', 'bankBranchName', 'accountHolderName'];
    ROLE_DanhMucNhaCungCap_Them = ROLE.DanhMucNhaCungCap_Them;
    ROLE_DanhMucNhaCungCap_Sua = ROLE.DanhMucNhaCungCap_Sua;
    ROLE_DanhMucNhaCungCap_Xoa = ROLE.DanhMucNhaCungCap_Xoa;
    ROLE_DanhMucNhaCungCap_Xem = ROLE.DanhMucNhaCungCap_Xem;
    newRow: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private accountingObjectService: AccountingObjectService,
        private paymentClauseService: PaymentClauseService,
        private accountingObjectGroupService: AccountingObjectGroupService,
        private bankService: BankService,
        private organizationUnitService: OrganizationUnitService,
        private accountingObjectBankAccountService: AccountingObjectBankAccountService,
        private activatedRoute: ActivatedRoute,
        private toastr: ToastrService,
        private router: Router,
        public translate: TranslateService,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
        super();
        this.contextMenu = new ContextMenu();
        this.principal.identity().then(accountingobject => {
            this.currentAccountingObject = accountingobject;
            this.arrAuthorities = accountingobject.authorities;
            this.isEditUrl = window.location.href.includes('edit');
            this.isCreateUrl = window.location.href.includes('/new');
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_DanhMucNhaCungCap_Sua)
                : true;
            this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_DanhMucNhaCungCap_Them)
                : true;
            this.isEnter = this.currentAccountingObject.systemOption.filter(n => n.code === 'TCKHAC_Enter')[0].data === '1';
            this.eventManager.broadcast({
                name: 'checkEnter',
                content: this.isEnter
            });
        });
    }

    ngOnInit() {
        this.isClose = false;
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ accountingObject }) => {
            this.accountingObject = accountingObject;
        });
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
        this.organizationUnitService.query().subscribe(
            (res: HttpResponse<IOrganizationUnit[]>) => {
                this.organizationunits = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.bankService.getBanks().subscribe(
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
            this.accountingObject.scaleType = 0;
            this.accountingObject.objectType = 0;
        }
        this.listHeaderColumnsAccountingObjectGroups = ['Nhóm nhà cung cấp'];
        this.listAccountingObjectGroups = ['accountingObjectGroupName'];
        this.registerAddNewRow();
        this.registerDeleteRow();
        this.registerCopyRow();
        this.copy();
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }

    // su kien addRowToInsert
    //@ebAuth(['ROLE_ADMIN', ROLE.DanhMucNhaCungCap_Them || ROLE.DanhMucNhaCungCap_Sua])
    addRowToInsert() {
        this.accountingobjectbankaccounts.push({});
    }

    doubleClickRow(e: any, select: number) {
        if (select === this.accountingobjectbankaccounts.length - 1) {
            const cellCurrent = e.path['0'].closest('td').cellIndex;
            const cellMax = document.getElementsByTagName('tbody')[0].children[select].childElementCount;
            if (cellCurrent === cellMax - 1) {
                this.newRow = true;
            }
        }
        if ((this.isEnter && select === this.accountingobjectbankaccounts.length - 1) || this.newRow) {
            this.accountingobjectbankaccounts.push(new AccountingObjectBankAccount());
            this.newRow = false;
            let nameTag: string = 'invoiceTemplate_Tax';
            select++;
            nameTag = nameTag + select;
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(nameTag);
                if (element) {
                    element.focus();
                }
            }, 0);
        }
        if (this.isEnter && select < this.accountingobjectbankaccounts.length - 1) {
            const inputs = document.getElementsByTagName('input');
            const indexInput = 20 + 4 * (select + 1) + 1;
            for (let i = 0; i < inputs.length; i++) {
                if (inputs[i].tabIndex === indexInput) {
                    setTimeout(() => {
                        inputs[i].focus();
                    }, 0);
                    break;
                }
            }
        }
    }

    previousState() {
        this.router.navigate(['/supplier']);
    }

    save() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = false;
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.accountingObject.isEmployee === null || this.accountingObject.isEmployee === undefined) {
            this.accountingObject.isEmployee = false;
        }
        if (!this.accountingObject.accountingObjectCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObject.missAccountingObjectCoded'),
                this.translate.instant('ebwebApp.accountingObject.detail.notification')
            );
            return;
        }
        if (!this.accountingObject.accountingObjectName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObject.missAccountingObjectNamed'),
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
        if (!this.checkEmailValidate(this.accountingObject.contactEmail)) {
            return;
        }
        this.accountingObject.accountingObjectBankAccounts = this.accountingobjectbankaccounts;
        for (let i = 0; i < this.accountingObject.accountingObjectBankAccounts.length; i++) {
            this.accountingObject.accountingObjectBankAccounts[i].orderPriority = i + 1;
        }
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
        this.isSaving = true;
        this.isSaveAndCreate = true;
        if (this.accountingObject.isEmployee === null || this.accountingObject.isEmployee === undefined) {
            this.accountingObject.isEmployee = false;
        }
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
        if (!this.checkEmailValidate(this.accountingObject.email)) {
            return;
        }
        if (!this.checkEmailValidate(this.accountingObject.contactEmail)) {
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

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                } else {
                    this.toastr.error(this.translate.instant('ebwebApp.unit.existData'));
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.copy();
        this.accountingobjectbankaccounts = [];
        if (this.isSaveAndCreate) {
            this.accountingObject = {};
            this.accountingObject.scaleType = 0;
            this.accountingObject.objectType = 0;
        } else {
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

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucNhaCungCap_Xoa])
    delete() {
        if (this.accountingObject.id) {
            this.router.navigate(['/supplier', { outlets: { popup: this.accountingObject.id + '/delete' } }]);
        }
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isClose = true;
        this.router.navigate(['supplier']);
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

    onRightClick($event, data, selectedData, isNew, isDelete, select) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isCopy = true;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.selectedData = selectedData;
        this.select = select;
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

    addIfLastInput() {
        this.keyDownAddRow(null);
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

    closeForm() {
        event.preventDefault();
        if (this.accountingObjectCoppy) {
            if (!this.utilsService.isEquivalent(this.accountingObject, this.accountingObjectCoppy)) {
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

    closeAll() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.router.navigate(['/supplier']);
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
        this.selectChangeTotalAmount();
    }

    selectChangeTotalAmount() {}

    keyPress(value: number, select: number) {
        if (select === 0) {
            this.accountingobjectbankaccounts.splice(value, 1);
            for (let i = 0; i < this.accountingobjectbankaccounts.length; i++) {}
        }
    }

    ngOnDestroy(): void {
        if (!window.location.href.includes('supplier')) {
            sessionStorage.removeItem('pageCurrent');
            sessionStorage.removeItem('selectIndex');
            sessionStorage.removeItem('search');
        }
    }

    loadForTax() {
        if (!this.utilsService.checkMST(this.accountingObject.taxCode)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.taxCodeInvalid'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
        } else if (this.accountingObject.taxCode.length == 10 || this.accountingObject.taxCode.length == 14) {
            this.utilsService.getDataFromDkkD({ taxCode: this.accountingObject.taxCode }).subscribe(res => {
                this.accountingObject.accountingObjectName = res.body.result.accountingObjectName;
                this.accountingObject.accountingObjectAddress = res.body.result.accountingObjectAddress;
            });
        } else {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.taxCodeInvalid'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
        }
    }
}
