import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { IAccountList } from 'app/shared/model/account-list.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ExpenseItem, IExpenseItem } from 'app/shared/model/expense-item.model';
import { CategoryName, EXPENSE_ITEM } from 'app/app.constants';
import { ExpenseItemService } from 'app/entities/expense-item';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { ROLE } from 'app/role.constants';
import { isBoolean } from 'util';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { KhoanMucChiPhiListService } from 'app/danhmuc/khoan-muc-chi-phi-list';
import { ICreditCard } from 'app/shared/model/credit-card.model';
import { UnitService } from 'app/danhmuc/unit';

@Component({
    selector: 'eb-expense-list-update',
    templateUrl: './khoan-muc-chi-phi-list-update.component.html',
    styleUrls: ['./khoan-muc-chi-phi-list-update.component.css']
})
export class KhoanMucChiPhiListComboboxComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewInit {
    expenseItem: IExpenseItem;
    expenseItemCopy: IExpenseItem;
    isSaving: boolean;
    listCheckbox: [{ value?: string; isChecked?: boolean }];
    expenseItems: ExpenseItem[];
    typeAccountingObject: string;
    isSaveAndCreate: boolean;
    isDuplicateAccountNumber: boolean;
    modalRef: NgbModalRef;
    saveAndAdd = false;
    isClose: boolean;
    data: IExpenseItem;
    @ViewChild('content') content: any;
    saveSuccess: boolean;
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
    isClosing: boolean;
    isEditUrl: boolean;

    constructor(
        private jhiAlertService: JhiAlertService,
        private exepenseItemService: KhoanMucChiPhiListService,
        private activatedRoute: ActivatedRoute,
        public utilsService: UtilsService,
        private router: Router,
        private toastr: ToastrService,
        public translate: TranslateService,
        private modalService: NgbModal,
        private activeModal: NgbActiveModal,
        private activeRoute: ActivatedRoute,
        private eventManager: JhiEventManager,
        private toastrService: ToastrService,
        private translateService: TranslateService
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
        this.expenseItem.isActive = true;
        this.expenseItem.isParentNode = false;
        this.expenseItem.isSecurity = true;
        this.exepenseItemService.getAllExpenseItems().subscribe(response => {
            this.expenseItems = response.body;
            this.activeRoute.data.subscribe(expenseItem => {
                this.expenseItems = this.expenseItems.filter(expense => expense.isActive);
                this.copy();
            });
        });
        this.isSaving = false;
        this.copy();
    }

    copy() {
        this.expenseItemCopy = this.utilsService.deepCopy([this.expenseItem])[0];
    }

    reload() {
        this.expenseItem = new ExpenseItem();
        this.saveAndAdd = false;
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
        this.expenseItem.isSecurity = false;
        this.expenseItem.isActive = true;
        this.saveAndAdd = false;
        this.isSaveAndCreate = false;
        this.isSaving = true;
        if (this.checkError()) {
            if (this.expenseItem.id) {
                this.exepenseItemService.update(this.expenseItem).subscribe(
                    (res: HttpResponse<any>) => {
                        this.data = res.body;
                        this.onSaveSuccess();
                    },
                    httpErrorResponse => this.onSaveError(httpErrorResponse)
                );
            } else {
                this.exepenseItemService.create(this.expenseItem).subscribe(
                    (res: HttpResponse<any>) => {
                        this.data = res.body;
                        this.onSaveSuccess();
                    },
                    httpErrorResponse => this.onSaveError(httpErrorResponse)
                );
            }
        }
        // this.close();
    }

    saveAndNew() {
        event.preventDefault();
        this.expenseItem.isSecurity = false;
        this.expenseItem.isActive = true;
        this.saveAndAdd = true;
        this.isSaveAndCreate = true;
        this.isSaving = true;
        if (this.checkError()) {
            if (this.expenseItem.id) {
                this.exepenseItemService.update(this.expenseItem).subscribe(
                    (res: HttpResponse<any>) => {
                        this.data = res.body;
                        this.onSaveSuccess();
                    },
                    httpErrorResponse => this.onSaveError(httpErrorResponse)
                );
            } else {
                this.exepenseItemService.create(this.expenseItem).subscribe(
                    (res: HttpResponse<any>) => {
                        this.data = res.body;
                        this.onSaveSuccess();
                    },
                    httpErrorResponse => this.onSaveError(httpErrorResponse)
                );
            }
        }
        // this.close();
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
        event.preventDefault();
        this.copy();
        this.toastr.success(
            this.translate.instant('ebwebApp.expenseItem.successful'),
            this.translate.instant('ebwebApp.expenseItem.message')
        );
        if (!this.saveAndAdd) {
            this.eventManager.broadcast({
                name: 'saveSuccess',
                content: { name: CategoryName.KHOAN_MUC_CHI_PHI, data: this.data }
            });
            this.activeModal.dismiss(false);
        } else {
            this.eventManager.broadcast({
                name: 'saveAndNewSuccess',
                content: { name: CategoryName.KHOAN_MUC_CHI_PHI, data: this.data }
            });
            this.reload();
        }
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    close() {
        this.eventManager.broadcast({
            name: 'closePopup',
            content: null
        });
        this.activeModal.dismiss(false);
    }

    closeForm() {
        if (!this.utilsService.isEquivalent(this.expenseItem, this.expenseItemCopy)) {
            if (!this.isClose) {
                this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
                this.isClose = true;
            }
        } else {
            this.copy();
            this.previousState();
            this.close();
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
