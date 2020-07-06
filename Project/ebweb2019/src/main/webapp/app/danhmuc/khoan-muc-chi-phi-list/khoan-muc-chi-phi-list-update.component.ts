import { Component, OnDestroy, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IAccountList } from 'app/shared/model/account-list.model';
import { KhoanMucChiPhiListService } from './khoan-muc-chi-phi-list.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ExpenseItem, IExpenseItem } from 'app/shared/model/expense-item.model';
import { EXPENSE_ITEM } from 'app/app.constants';
import { ExpenseItemService } from 'app/entities/expense-item';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { ROLE } from 'app/role.constants';
import { isBoolean } from 'util';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { Principal } from 'app/core';
import { ITreeAccountList } from 'app/shared/model/account-list-tree.model';
import { TreeGridComponent } from 'app/shared/tree-grid/tree-grid.component';

@Component({
    selector: 'eb-expense-list-update',
    templateUrl: './khoan-muc-chi-phi-list-update.component.html',
    styleUrls: ['./khoan-muc-chi-phi-list-update.component.css']
})
export class KhoanMucChiPhiListUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewInit {
    expenseItem: IExpenseItem;
    expenseItemCopy: IExpenseItem;
    isSaving: boolean;
    listCheckbox: [{ value?: string; isChecked?: boolean }];
    expenseItems: ExpenseItem[];
    typeAccountingObject: string;
    isSaveAndCreate: boolean;
    modalRef: NgbModalRef;
    account: any;
    arrAuthorities: any[];
    isEditUrl: boolean;
    saveAndAdd = false;
    isClose: boolean;
    isCheck: boolean;
    isNew: boolean;
    roleSua: boolean;
    roleThem: boolean;
    @ViewChild('content') content: any;
    dataList: (
        | { name: any; value: EXPENSE_ITEM }
        | { name: any; value: EXPENSE_ITEM }
        | { name: any; value: EXPENSE_ITEM }
        | { name: any; value: EXPENSE_ITEM }
        | { name: any; value: EXPENSE_ITEM }
        | { name: any; value: EXPENSE_ITEM }
        | { name: any; value: EXPENSE_ITEM }
        | { name: any; value: EXPENSE_ITEM }
        | { name: any; value: EXPENSE_ITEM }
        | { name: any; value: EXPENSE_ITEM }
        | { name: any; value: EXPENSE_ITEM }
        | { name: any; value: EXPENSE_ITEM }
        | { name: any; value: EXPENSE_ITEM }
        | { name: any; value: EXPENSE_ITEM }
        | { name: any; value: EXPENSE_ITEM })[];
    ROLE_IExpenseItem_Them = ROLE.DanhMucKhoanMucChiPhi_Them;
    ROLE_IExpenseItem_Sua = ROLE.DanhMucKhoanMucChiPhi_Sua;
    ROLE_IExpenseItem_Xoa = ROLE.DanhMucKhoanMucChiPhi_Xoa;
    ROLE_IExpenseItem_Xem = ROLE.DanhMucKhoanMucChiPhi_Xem;

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

    constructor(
        private jhiAlertService: JhiAlertService,
        private exepenseItemService: KhoanMucChiPhiListService,
        private activatedRoute: ActivatedRoute,
        public utilsService: UtilsService,
        private router: Router,
        private toastr: ToastrService,
        public translate: TranslateService,
        private modalService: NgbModal,
        private activeRoute: ActivatedRoute,
        private principal: Principal
    ) {
        super();
        this.translate
            .get([
                'ebwebApp.expenseItem.rawMaterialsDirectly',
                'ebwebApp.expenseItem.directLabor',
                'ebwebApp.expenseItem.laborCosts',
                'ebwebApp.expenseItem.costOfRawMaterials',
                'ebwebApp.expenseItem.costOfProductionTools',
                'ebwebApp.expenseItem.machineDepreciationExpense',
                'ebwebApp.expenseItem.costOfHiredServices',
                'ebwebApp.expenseItem.costsInOtherCurrencies',
                'ebwebApp.expenseItem.generalFactoryStaffCosts',
                'ebwebApp.expenseItem.generalMaterialCosts',
                'ebwebApp.expenseItem.generalProductionEquipmentCosts',
                'ebwebApp.expenseItem.generalDepreciationExpense',
                'ebwebApp.expenseItem.expensesForServicesPurchasedFromOutside',
                'ebwebApp.expenseItem.costsInOtherGeneralCurrency'
            ])
            .subscribe(res => {
                this.dataList = [
                    {
                        value: EXPENSE_ITEM.NGUYEN_VAT_LIEU_TRUC_TIEP,
                        name: res['ebwebApp.expenseItem.rawMaterialsDirectly']
                    },
                    { value: EXPENSE_ITEM.NHAN_CONG_TRUC_TIEP, name: res['ebwebApp.expenseItem.directLabor'] },
                    { value: EXPENSE_ITEM.CHI_PHI_NHAN_CONG, name: res['ebwebApp.expenseItem.laborCosts'] },
                    { value: EXPENSE_ITEM.CHI_PHI_NGUYEN_VAT_LIEU, name: res['ebwebApp.expenseItem.costOfRawMaterials'] },
                    {
                        value: EXPENSE_ITEM.CHI_PHI_DUNG_CU_SAN_XUAT,
                        name: res['ebwebApp.expenseItem.costOfProductionTools']
                    },
                    {
                        value: EXPENSE_ITEM.CHI_PHI_KHAU_HAO_MAY,
                        name: res['ebwebApp.expenseItem.machineDepreciationExpense']
                    },
                    {
                        value: EXPENSE_ITEM.CHI_PHI_DICH_VU_MUA_NGOAI,
                        name: res['ebwebApp.expenseItem.costOfHiredServices']
                    },
                    {
                        value: EXPENSE_ITEM.CHI_PHI_BANG_TIEN_KHAC,
                        name: res['ebwebApp.expenseItem.costsInOtherCurrencies']
                    },
                    {
                        value: EXPENSE_ITEM.PHI_PHI_NHAN_VIEN_PHAN_XUONG,
                        name: res['ebwebApp.expenseItem.generalFactoryStaffCosts']
                    },
                    {
                        value: EXPENSE_ITEM.CHI_PHI_NGUYEN_VAT_LIEU_CHUNG,
                        name: res['ebwebApp.expenseItem.generalMaterialCosts']
                    },
                    {
                        value: EXPENSE_ITEM.CHI_PHI_DUNG_CU_SAN_XUAT_CHUNG,
                        name: res['ebwebApp.expenseItem.generalProductionEquipmentCosts']
                    },
                    {
                        value: EXPENSE_ITEM.CHI_PHI_KHAU_HAO_MAY_CHUNG,
                        name: res['ebwebApp.expenseItem.generalDepreciationExpense']
                    },
                    {
                        value: EXPENSE_ITEM.CHI_PHI_DICH_VU_MUA_NGOAI_CHUNG,
                        name: res['ebwebApp.expenseItem.expensesForServicesPurchasedFromOutside']
                    },
                    {
                        value: EXPENSE_ITEM.CHI_PHI_BANG_TIEN_MAT_KHAC,
                        name: res['ebwebApp.expenseItem.costsInOtherGeneralCurrency']
                    }
                ];
            });
    }

    ngOnInit() {
        this.listCheckbox = [{}];
        this.expenseItems = [];
        this.expenseItem = {};
        this.expenseItem.grade = 1;
        this.isEditUrl = window.location.href.includes('/edit');
        this.expenseItem.isActive = true;
        this.expenseItem.isParentNode = false;
        this.expenseItem.isSecurity = true;
        this.isNew = window.location.href.includes('/new');
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.account = account;
                this.arrAuthorities = account.authorities;
                this.roleSua = !this.arrAuthorities.includes('ROLE_ADMIN')
                    ? this.arrAuthorities.includes(this.ROLE_IExpenseItem_Sua)
                    : true;
                this.roleThem = !this.arrAuthorities.includes('ROLE_ADMIN')
                    ? this.arrAuthorities.includes(this.ROLE_IExpenseItem_Them)
                    : true;
            });
        });
        this.exepenseItemService.getAllExpenseItems().subscribe(response => {
            this.expenseItems = response.body;
            this.activeRoute.data.subscribe(expenseItem => {
                if (expenseItem.expenseItem.id) {
                    this.expenseItem = expenseItem.expenseItem;
                    if (this.expenseItem.isParentNode) {
                        this.expenseItems = this.expenseItems.filter(expense => !this.checkIsChiDent(expense));
                    }
                    this.expenseItems = this.expenseItems.filter(expense => expense.id !== this.expenseItem.id);
                    this.expenseItem.parent = this.expenseItems.filter(expense => expense.id === this.expenseItem.parentID)[0];
                }
                this.expenseItems = this.expenseItems.filter(expense => expense.isActive);
                this.copy();
            });
        });
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ expenseItem }) => {
            if (expenseItem.id) {
                this.expenseItem = expenseItem;
            }
        });
        this.copy();
    }

    checkIsChiDent(expenseItem: ExpenseItem) {
        if (expenseItem.parentID === null) {
            return false;
        }
        if (expenseItem.parentID === this.expenseItem.id) {
            return true;
        }
        const expense = this.expenseItems.filter(a => a.id === expenseItem.parentID)[0];
        return this.checkIsChiDent(expense);
    }

    copy() {
        this.expenseItemCopy = this.utilsService.deepCopy([this.expenseItem])[0];
    }

    reload() {
        this.expenseItem = new ExpenseItem();
        this.saveAndAdd = false;
        console.log(this.expenseItems);
        this.exepenseItemService.getAllExpenseItems().subscribe(response => {
            this.expenseItems = response.body;
        });
        this.copy();
    }

    previousState() {
        this.router.navigate(['khoan-muc-chi-phi-list']);
    }

    changeExpense() {
        console.log(this.expenseItem);
    }

    checkError() {
        if (!this.expenseItem.expenseItemCode) {
            this.toastr.error(this.translate.instant('ebwebApp.expenseItem.errorExpenseItemCode'));
            return false;
        } else if (!this.expenseItem.expenseItemName) {
            this.toastr.error(this.translate.instant('ebwebApp.expenseItem.errorExpenseItemName'));
            return false;
        }
        return true;
    }

    save() {
        event.preventDefault();
        if (this.checkError()) {
            if (this.expenseItem.id !== undefined) {
                this.exepenseItemService
                    .update(this.expenseItem)
                    .subscribe((res: HttpResponse<any>) => this.onSaveSuccess(), httpErrorResponse => this.onSaveError(httpErrorResponse));
            } else {
                this.expenseItem.isActive = true;
                this.exepenseItemService
                    .create(this.expenseItem)
                    .subscribe((res: HttpResponse<any>) => this.onSaveSuccess(), httpErrorResponse => this.onSaveError(httpErrorResponse));
            }
        } else {
            this.router.navigate(['khoan-muc-chi-phi-list/new']);
        }
        this.closeContent();
    }

    saveAndNew() {
        event.preventDefault();
        this.saveAndAdd = true;
        // this.expenseItem.isActive = true;
        event.preventDefault();
        if (this.checkError()) {
            if (this.expenseItem.id !== undefined) {
                this.exepenseItemService
                    .update(this.expenseItem)
                    .subscribe((res: HttpResponse<any>) => this.onSaveSuccess(), httpErrorResponse => this.onSaveError(httpErrorResponse));
            } else {
                this.expenseItem.isActive = true;
                this.exepenseItemService
                    .create(this.expenseItem)
                    .subscribe((res: HttpResponse<any>) => this.onSaveSuccess(), httpErrorResponse => this.onSaveError(httpErrorResponse));
            }
        } else {
            this.router.navigate(['khoan-muc-chi-phi-list/new']);
            // this.ngOnInit();
        }
        this.closeContent();
    }

    private onSaveError(response) {
        this.toastr.error(this.translate.instant(`ebwebApp.expenseItem.${response.error.errorKey}`));
    }

    delete() {
        event.preventDefault();
        if (this.expenseItem.isParentNode) {
            this.toastr.error(this.translate.instant('ebwebApp.expenseItem.errorParent'));
        } else {
            if (this.expenseItem.id) {
                this.router.navigate(['/khoan-muc-chi-phi-list', { outlets: { popup: this.expenseItem.id + '/delete' } }]);
            }
        }
    }

    onSaveSuccess() {
        this.copy();
        this.toastr.success(
            this.translate.instant('ebwebApp.expenseItem.successful'),
            this.translate.instant('ebwebApp.expenseItem.message')
        );
        if (!this.saveAndAdd) {
            this.router.navigate(['/khoan-muc-chi-phi-list']);
        } else {
            this.reload();
        }
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    close() {
        this.modalRef.close();
        this.copy();
        this.router.navigate(['/khoan-muc-chi-phi-list']);
    }

    closeAll() {
        this.router.navigate(['/khoan-muc-chi-phi-list']);
    }

    closeForm() {
        if (this.expenseItemCopy) {
            if (!this.utilsService.isEquivalent(this.expenseItem, this.expenseItemCopy)) {
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
            this.previousState();
            this.closeAll();
        }
    }

    saveContent() {
        this.isSaveAndCreate = true;
        this.close();
        this.save();
    }

    edit() {
        event.preventDefault();
    }

    canDeactive() {
        if (!this.isClose) {
            return this.utilsService.isEquivalent(this.expenseItem, this.expenseItemCopy);
        }
        return true;
    }

    ngOnDestroy(): void {}

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }
}
