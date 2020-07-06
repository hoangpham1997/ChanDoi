import { AfterContentChecked, Component, HostListener, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IOpAccountModel, OpAccountModel } from 'app/shared/model/op-account.model';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SoDuDauKyService } from 'app/tien-ich/so-du-dau-ky/so-du-dau-ky.service';
import { Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { IAccountList } from 'app/shared/model/account-list.model';
import { TranslateService } from '@ngx-translate/core';
import { IMaterialGoods, MaterialGoods } from 'app/shared/model/material-goods.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { HttpResponse } from '@angular/common/http';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { CostSetService } from 'app/entities/cost-set';
import { ExpenseItemService } from 'app/entities/expense-item';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { BudgetItemService } from 'app/entities/budget-item';
import { Principal } from 'app/core';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { Subscription } from 'rxjs';
import { IOPMaterialGoods, OpMaterialGoodsModel } from 'app/shared/model/op-material-goods.model';
import {
    ACCOUNT_DETAIL_TYPE,
    ACCOUNTING_TYPE_ID,
    SO_LAM_VIEC,
    ACCOUNTING_TYPE,
    NGAY_HACH_TOAN,
    TOOLTIPS,
    ImportExcel
} from 'app/app.constants';
import { RepositoryService } from 'app/danhmuc/repository';
import { IRepository } from 'app/shared/model/repository.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { UnitService } from 'app/danhmuc/unit';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_FORMAT_SLASH } from 'app/shared';
import { ToastrService } from 'ngx-toastr';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { EbVirtualScrollerComponent } from 'app/virtual-scroller/virtual-scroller';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { Moment } from 'moment';
import { AccountListService } from 'app/danhmuc/account-list';
import { ROLE } from 'app/role.constants';
import { EbMaterialGoodsSpecificationsModalComponent } from 'app/shared/modal/material-goods-specifications/material-goods-specifications.component';

@Component({
    selector: 'eb-so-du-dau-ky-insert-update',
    templateUrl: './so-du-dau-ky-insert-update.component.html',
    styleUrls: ['./so-du-dau-ky-insert-update.component.css']
})
export class SoDuDauKyInsertUpdateComponent extends BaseComponent implements OnInit, AfterContentChecked {
    @ViewChild('content') content: any;
    @ViewChild('validHadReference') validHadReference: TemplateRef<any>;
    @ViewChild('chooseSheetModal') public chooseSheetModal: NgbModalRef;
    @ViewChild('scroll') scroll: any;
    ROLE = ROLE;
    TOOLTIPS = TOOLTIPS;
    isRoleEdit: boolean;
    accountObjects: IAccountingObject[];
    openingBalanceDetails: any[];
    openingBalance: any;
    accountingType: string[];
    modalRef: NgbModalRef;
    isForeignCurrency: boolean;
    accountList: IAccountList;
    bankAccountDetails: IBankAccountDetails[];
    materialGoods: MaterialGoods[];
    expenseItems: IExpenseItem[];
    costSets: ICostSet[];
    budgetItems: IBudgetItem[];
    statisticsCodes: IStatisticsCode[];
    // emContracts: IEMContract[];
    organizationUnits: IOrganizationUnit[];
    currentBook: string;
    account: any;
    isReadOnly: any;
    contextMenu: ContextMenu;
    eventSubscriber: Subscription;
    repositories: IRepository[];
    repository: string;
    currencies: ICurrency[];
    currency: any;
    deletedItems: any[];
    deletedItemsTemp: any[];
    ACCOUNTING_TYPE = ACCOUNTING_TYPE;
    ACCOUNTING_TYPE_ID = ACCOUNTING_TYPE_ID;
    postedDate: Moment;
    isLoading: boolean;
    balanceType: number;
    opsData: any;
    refModel: any;
    refModel2: any;
    refModel3: any;
    link: any;
    file: any;
    sessionData: any[];
    sessionDataOld: any[];
    cleanCanDeActive: boolean;
    havingData: boolean;
    totalDebitBalance: number;
    totalDebitBalanceOriginal: number;
    totalCreditBalance: number;
    totalCreditBalanceOriginal: number;
    totalAmountOriginal: number;
    totalAmount: number;
    isAccountingObject: boolean;
    sheetNames: string[];
    @ViewChild(EbVirtualScrollerComponent) private virtualScroller: EbVirtualScrollerComponent;
    // call this function whenever you have to focus on second item
    units: any[];
    dataUpdate: any[];

    constructor(
        private soDuDauKyService: SoDuDauKyService,
        private router: Router,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        private toastrService: ToastrService,
        public translateService: TranslateService,
        public utilsService: UtilsService,
        private unitService: UnitService,
        private bankAccountDetailService: BankAccountDetailsService,
        private statisticsCodeService: StatisticsCodeService,
        private costSetService: CostSetService,
        private expenseItemsService: ExpenseItemService,
        // private emContractService: EMContractService,
        private organizationUnitService: OrganizationUnitService,
        private budgetItemService: BudgetItemService,
        private principal: Principal,
        private repositoryService: RepositoryService,
        private accountingObjectService: AccountingObjectService,
        private materialGoodsService: MaterialGoodsService,
        private currencyService: CurrencyService,
        private refModalService: RefModalService,
        private accountListService: AccountListService
    ) {
        super();
    }

    ngOnInit(): void {
        this.toolbarClass = 'gr-toolbar';
        this.isEditPage = true;
        this.cleanCanDeActive = false;
        this.deletedItems = [];
        this.accountList = this.soDuDauKyService.getAccountList();
        this.accountingType = this.soDuDauKyService.getAccountingType();
        if (!this.accountList || !this.accountList.accountNumber) {
            this.router.navigate([`so-du-dau-ky`]);
        }
        this.contextMenu = new ContextMenu();
        this.contextMenu.isShow = false;
        this.principal.identity().then(account => {
            this.account = account;
            this.isRoleEdit = this.account.authorities.some(x => ['ROLE_ADMIN', this.ROLE.SoDuDauKy_Sua].includes(x.toString()));

            this.isEdit = this.isRoleEdit;
            if (!this.accountList.isForeignCurrency) {
                this.currency = this.account.organizationUnit.currencyID;
            }
            this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                this.currencies = res.body;
                if (!this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
                    this.sessionData = this.currencies.map(x => {
                        return { currencyId: x.id, currency: x.currencyCode, data: [] };
                    });
                    this.sessionDataOld = this.currencies.map(x => {
                        return { currencyId: x.id, currency: x.currencyCode, data: [] };
                    });
                }
                this.onUpdateCurrency();
            });
            if (this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
                this.balanceType = ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE;
                this.repositoryService.getRepositoryCombobox().subscribe(res => {
                    this.repositories = res.body.filter(rep => rep.isActive);
                    this.sessionData = this.repositories.map(x => {
                        return { repository: x.id, code: x.repositoryCode, data: [] };
                    });
                    this.sessionDataOld = this.repositories.map(x => {
                        return { repository: x.id, code: x.repositoryCode, data: [] };
                    });
                });
                this.materialGoodsService.getMaterialGoodsCustom().subscribe((res: HttpResponse<IMaterialGoods[]>) => {
                    this.materialGoods = res.body;
                    this.unitService
                        .convertRateForMaterialGoodsComboboxCustomList({
                            materialGoodsId: this.materialGoods.map(x => x.id)
                        })
                        .subscribe(ref => {
                            this.units = ref;
                        });
                });
            } else if (this.accountingType.includes(ACCOUNTING_TYPE.AO)) {
                this.balanceType = ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT;
            } else {
                this.balanceType = ACCOUNTING_TYPE_ID.NORMAL_ACCOUNT;
            }
        });
        this.onLoadData();
        this.registerRef();
        this.registerMaterialGoodsSpecifications();
    }

    disableParentNode() {
        const parentNode = document.getElementsByClassName('isParentNode');
        for (let i = 0; i < parentNode.length; i++) {
            const inputs = parentNode.item(i).getElementsByTagName('input');
            const buttons = parentNode.item(i).getElementsByTagName('button');
            for (let j = 0; j < inputs.length; j++) {
                inputs[j].disabled = true;
            }
            for (let j = 0; j < buttons.length; j++) {
                buttons[j].disabled = true;
            }
        }
    }

    setUpOpeningBalances() {
        if (!this.sessionData) {
            return;
        }
        const opA = this.sessionData.find(x => x.currency === this.currency);
        if (!opA) {
            return;
        }
        if (opA && opA.data && opA.data.length > 0) {
            this.onSelect(opA.data[0]);
            this.openingBalanceDetails = opA.data;
            this.virtualScroller.items = this.openingBalanceDetails;
            this.calculatorTotalBalance();
            if (this.accountList) {
                let indexSelected = this.openingBalanceDetails.findIndex(x => x.accountNumber === this.accountList.accountNumber);
                this.openingBalance = this.openingBalanceDetails[indexSelected];
                if (indexSelected > 10) {
                    indexSelected -= 10;
                } else {
                    indexSelected = 0;
                }

                this.virtualScroller.scrollInto(this.openingBalanceDetails[indexSelected]);
            }
            return;
        }
        this.accountListService.getAccountListsActiveAndOP({ currencyId: this.currency }).subscribe(res => {
            let accountObjects = res.body.sort((a, b) => a.accountNumber.localeCompare(b.accountNumber));
            for (let i = 0; i < accountObjects.length; i++) {
                if (accountObjects[i].children && accountObjects[i].children.length) {
                    for (let j = 0; j < accountObjects[i].children.length; j++) {
                        accountObjects[i].children[j].accountGroupKind = accountObjects[i].accountGroupKind;
                        accountObjects[i].children[j].typeId = ACCOUNTING_TYPE_ID.NORMAL_ACCOUNT;
                    }
                    accountObjects.splice(i + 1, 0, ...accountObjects[i].children);
                }
            }
            accountObjects = accountObjects
                .filter(x => {
                    // if (!x.isForeignCurrency) {
                    //     return false;
                    // }
                    if (x.detailType) {
                        const accLst = x.detailType.split(';');
                        if (
                            accLst.includes(ACCOUNT_DETAIL_TYPE.ACCOUNT_SUPPLIER) ||
                            accLst.includes(ACCOUNT_DETAIL_TYPE.ACCOUNT_CUSTOMER) ||
                            accLst.includes(ACCOUNT_DETAIL_TYPE.ACCOUNT_EMPLOYEE) ||
                            accLst.includes(ACCOUNT_DETAIL_TYPE.ACCOUNT_BANK)
                        ) {
                            return false;
                        }
                    }
                    return true;
                })
                .map((x, index) => {
                    x.count = index;
                    return x;
                });
            accountObjects =
                this.account.organizationUnit.currencyID === this.currency
                    ? accountObjects.filter(x => !x.isForeignCurrency)
                    : accountObjects.filter(x => x.isForeignCurrency);
            opA.data = [];
            opA.data.push(...accountObjects);
            this.openingBalanceDetails = opA.data;
            this.calculatorTotalBalance();
            this.onSelect(opA.data[0]);
            for (let i = 0; i < this.openingBalanceDetails.length; i++) {
                if (this.openingBalanceDetails[i].accountGroupKind === 1) {
                    this.openingBalanceDetails[i].debitAmountOriginal = 0;
                    this.openingBalanceDetails[i].debitAmount = 0;
                }
                if (this.openingBalanceDetails[i].accountGroupKind === 0) {
                    this.openingBalanceDetails[i].creditAmountOriginal = 0;
                    this.openingBalanceDetails[i].creditAmount = 0;
                }

                if (this.openingBalanceDetails[i].opAccountDTOList && this.openingBalanceDetails[i].opAccountDTOList.length > 0) {
                    for (let j = 0; j < this.openingBalanceDetails[i].opAccountDTOList.length; j++) {
                        const dataNode = this.openingBalanceDetails[i].opAccountDTOList[j];
                        dataNode.parentAccountID = this.openingBalanceDetails[i].parentAccountID;
                        dataNode.accountGroupKind = this.openingBalanceDetails[i].accountGroupKind;
                        dataNode.detailType = this.openingBalanceDetails[i].detailType;
                        this.openingBalanceDetails.splice(i + 1, 0, dataNode);
                    }
                    this.openingBalanceDetails.splice(i, 1);
                } else {
                    // this.openingBalanceDetails[i].id = null;
                }
            }
            for (let i = 0; i < this.openingBalanceDetails.length; i++) {
                if (this.currency && this.openingBalanceDetails[i].currencyId === this.currency) {
                    this.openingBalanceDetails[i].debitAmountOriginal = this.openingBalanceDetails[i].debitAmount;
                    this.openingBalanceDetails[i].creditAmountOriginal = this.openingBalanceDetails[i].creditAmount;
                }
            }

            this.openingBalanceDetails = this.openingBalanceDetails.sort(
                (a, b) => a.accountNumber.localeCompare(b.accountNumber) || a.orderPriority - b.orderPriority
            );
            this.calculatingParent(this.openingBalanceDetails);
            this.sessionDataOld.find(x => x.currency === this.currency).data = JSON.parse(JSON.stringify(this.openingBalanceDetails));
            this.virtualScroller.items = this.openingBalanceDetails;
            this.virtualScroller.refresh();
            if (this.accountList) {
                let indexSelected = this.openingBalanceDetails.findIndex(x => x.accountNumber === this.accountList.accountNumber);
                this.openingBalance = this.openingBalanceDetails[indexSelected];
                if (indexSelected > 10) {
                    indexSelected -= 10;
                } else {
                    indexSelected = 0;
                }

                this.virtualScroller.scrollInto(this.openingBalanceDetails[indexSelected]);
            }
        });
    }

    registerRef() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => this.onAddNewRow(response.content));

        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => this.onDeleteRow(response.content));

        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => this.onCopyRow(response.content));

        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${ACCOUNTING_TYPE_ID.NORMAL_ACCOUNT}`, () => {
            this.exportExcel();
        });
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE}`, () => {
            this.exportExcel();
        });
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT}`, () => {
            this.exportExcel();
        });
    }

    undo() {
        const indexUndo = this.openingBalanceDetails.findIndex(x => x === this.deletedItems[this.deletedItems.length - 1]);
        this.openingBalanceDetails.splice(indexUndo + 1, this.deletedItems[this.deletedItems.length]);
        this.deletedItems.pop();
    }

    onRightClick($event, data, selectedData, isNew, isDelete, isShow) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isCopy = true;
        this.contextMenu.isShow = isShow;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.selectedData = selectedData;
    }

    onLoadData() {
        this.bankAccountDetailService.getBankAccountDetailsCustom().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
            this.bankAccountDetails = res.body.sort((a, b) => a.bankAccount.localeCompare(b.bankAccount));
        });
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticsCodes = res.body.filter(statisticsCode => statisticsCode.isActive);
        });
        this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body.filter(costSet => costSet.isActive);
        });
        this.expenseItemsService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body.filter(expenseItem => expenseItem.isActive);
        });
        // this.emContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
        //     this.emContracts = res.body.filter(contract => contract.isActive);
        // });
        this.organizationUnitService.getOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body.filter(department => department.isActive && department.unitType === 4);
        });
        this.budgetItemService.getBudgetItems().subscribe((res: HttpResponse<IBudgetItem[]>) => {
            this.budgetItems = res.body.filter(budget => budget.isActive);
        });
        this.organizationUnitService.getPostedDate().subscribe(res => {
            this.postedDate = moment.utc(res.body, DATE_FORMAT);
        });
    }

    canDeactive() {
        if (this.cleanCanDeActive || !this.sessionData) {
            return true;
        }
        if (
            this.deletedItems &&
            this.deletedItems.filter(
                x => x.id !== null && (x.amountOriginal > 0 || x.creditAmountOriginal > 0 || x.debitAmountOriginal > 0)
            ).length > 0
        ) {
            return false;
        }
        const dataSave = [];

        const dataSaveOld = [];
        if (this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
            for (const repository of this.sessionData) {
                dataSave.push(...this.utilsService.deepCopyObject(repository.data.filter(x => x.amountOriginal > 0)));
                dataSave.forEach(d => {
                    delete d.unitPrices;
                    delete d.convertRates;
                    delete d.units;
                });
            }
            for (const repository of this.sessionDataOld) {
                dataSaveOld.push(...this.utilsService.deepCopyObject(repository.data.filter(x => x.amountOriginal > 0)));
                dataSaveOld.forEach(d => {
                    delete d.unitPrices;
                    delete d.convertRates;
                    delete d.units;
                });
            }
        } else {
            for (const repository of this.sessionData) {
                dataSave.push(
                    ...this.utilsService.deepCopyObject(
                        repository.data.filter(x => x.creditAmountOriginal > 0 || x.debitAmountOriginal > 0)
                    )
                );
            }
            for (const repository of this.sessionDataOld) {
                dataSaveOld.push(
                    ...this.utilsService.deepCopyObject(
                        repository.data.filter(x => x.creditAmountOriginal > 0 || x.debitAmountOriginal > 0)
                    )
                );
            }
        }

        return this.utilsService.isEquivalentArray(dataSave, dataSaveOld);
    }

    trackIdentity(index, item: any) {
        return item.receiptDate;
    }

    exportExcel() {
        const data = { accountNumber: this.accountList.accountNumber || '', repositoryId: this.repository || '' };
        if (this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
            this.soDuDauKyService.exportPdfMaterialGoods(data).subscribe(res => {
                this.refModalService.open(null, EbReportPdfPopupComponent, res, false, ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE);
            });
        } else if (this.accountingType.includes(ACCOUNTING_TYPE.AO)) {
            this.soDuDauKyService.exportPdfAccountingObject(data).subscribe(res => {
                this.refModalService.open(null, EbReportPdfPopupComponent, res, false, ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT);
            });
        } else {
            this.soDuDauKyService.exportPDFAccountNormal(data).subscribe(res => {
                this.refModalService.open(null, EbReportPdfPopupComponent, res, false, ACCOUNTING_TYPE_ID.NORMAL_ACCOUNT);
            });
        }
    }

    doubleClickRow(result: any) {}

    save() {
        this.onSave();
    }

    closeForm() {
        this.close(this.content);
    }

    close(content?: any) {
        if (!this.canDeactive()) {
            this.openModal(content);
        } else {
            this.closeModal();
        }
    }
    openModal(content) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(content, { backdrop: 'static' });
    }

    closeModal() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.currency = null;
        this.cleanCanDeActive = true;
        this.router.navigate([`so-du-dau-ky`]);
    }

    onSelect(result) {
        this.currentRow = this.openingBalanceDetails.indexOf(result);
        this.openingBalance = result;
        this.updateHavingData();
    }

    updateHavingData() {
        let count = 0;
        for (let i = 0; i < this.openingBalanceDetails.length; i++) {
            if (
                this.accountingType.includes(ACCOUNTING_TYPE.MT) &&
                this.openingBalanceDetails[i].materialGoodsCode === this.openingBalance.materialGoodsCode
            ) {
                count++;
            } else if (
                this.accountingType.includes(ACCOUNTING_TYPE.AO) &&
                this.openingBalanceDetails[i].accountingObjectCode === this.openingBalance.accountingObjectCode
            ) {
                count++;
            } else if (
                !this.accountingType.includes(ACCOUNTING_TYPE.MT) &&
                !this.accountingType.includes(ACCOUNTING_TYPE.AO) &&
                this.openingBalanceDetails[i].accountNumber === this.openingBalance.accountNumber
            ) {
                count++;
            }
            if (count > 1) {
                this.havingData = true;

                return;
            }
        }
        if (!this.openingBalance) {
            this.openingBalance = this.openingBalanceDetails[0];
        }
        if (this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
            this.havingData = this.openingBalance.amountOriginal > 0 || false;
            console.log('amount');
        } else {
            this.havingData = this.openingBalance.creditAmountOriginal > 0 || this.openingBalance.debitAmountOriginal > 0 || false;
        }
    }

    closeContextMenu() {
        if (this.contextMenu) {
            this.contextMenu.isShow = false;
        }
    }

    @HostListener('document:keydown.control.c')
    onCtrlC() {
        if (this.getSelectionText()) {
            return;
        }
        this.onCopyRow();
    }

    onCopyRow(detail?) {
        event.preventDefault();
        detail = detail || this.openingBalance;
        const index = this.openingBalanceDetails.findIndex(x => x === detail);
        if (index !== undefined && index !== null && index >= 0) {
            const account = this.utilsService.deepCopyObject(this.openingBalanceDetails[index]);
            const typeLedger = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
            account.typeLedger = parseInt(typeLedger, 0);
            account.typeId = this.accountingType.includes(ACCOUNTING_TYPE.MT)
                ? ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE
                : this.accountingType.includes(ACCOUNTING_TYPE.AO)
                    ? ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT
                    : ACCOUNTING_TYPE_ID.NORMAL_ACCOUNT;
            account.postedDate = this.postedDate;
            // // set null
            account.id = null;
            account.currencyId = this.currency;
            account.orderPriority++;
            if (this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
                for (let i = 0; i < this.openingBalanceDetails.length; i++) {
                    if (
                        this.openingBalanceDetails[i].materialGoodsId === account.materialGoodsId &&
                        this.openingBalanceDetails[i].orderPriority >= account.orderPriority
                    ) {
                        this.openingBalanceDetails[i].orderPriority++;
                    }
                }
            } else if (this.accountingType.includes(ACCOUNTING_TYPE.AO)) {
                for (let i = 0; i < this.openingBalanceDetails.length; i++) {
                    if (
                        this.openingBalanceDetails[i].accountingObjectId === account.accountingObjectId &&
                        this.openingBalanceDetails[i].orderPriority >= account.orderPriority
                    ) {
                        this.openingBalanceDetails[i].orderPriority++;
                    }
                }
            } else {
                for (let i = 0; i < this.openingBalanceDetails.length; i++) {
                    if (
                        this.openingBalanceDetails[i].accountNumber === account.accountNumber &&
                        this.openingBalanceDetails[i].orderPriority >= account.orderPriority
                    ) {
                        this.openingBalanceDetails[i].orderPriority++;
                    }
                }
            }
            this.openingBalanceDetails.splice(index + 1, 0, account);
            this.updateAmount();
        }
        this.updateHavingData();
    }

    @HostListener('document:keydown.control.insert')
    onAddNewRow(detail?) {
        detail = detail || this.openingBalance;
        const index = this.openingBalanceDetails.findIndex(x => x === detail);
        if (index !== undefined && index !== null && index >= 0) {
            const account = this.utilsService.deepCopyObject(this.openingBalanceDetails[index]);
            const typeLedger = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
            account.typeLedger = parseInt(typeLedger, 0);
            account.typeId = this.accountingType.includes(ACCOUNTING_TYPE.MT)
                ? ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE
                : this.accountingType.includes(ACCOUNTING_TYPE.AO)
                    ? ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT
                    : ACCOUNTING_TYPE_ID.NORMAL_ACCOUNT;
            account.postedDate = this.postedDate;
            // // set null
            account.id = null;
            account.currencyId = this.currency;
            account.orderPriority++;
            account.debitAmountOriginal = 0;
            account.debitAmount = 0;
            account.creditAmountOriginal = 0;
            account.creditAmount = 0;
            account.unitId = null;
            account.quantity = 0;
            account.unitPriceOriginal = 0;
            account.unitPrice = 0;
            account.amountOriginal = 0;
            account.amount = 0;
            account.lotNo = null;
            account.expiredDate = null;
            account.expiryDateStr = null;
            account.bankAccountDetailId = null;
            account.expenseItemId = null;
            account.costSetId = null;
            account.contractId = null;
            account.budgetItemId = null;
            account.departmentId = null;
            account.statisticsCodeId = null;
            if (this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
                for (let i = 0; i < this.openingBalanceDetails.length; i++) {
                    if (
                        this.openingBalanceDetails[i].materialGoodsId === account.materialGoodsId &&
                        this.openingBalanceDetails[i].orderPriority >= account.orderPriority
                    ) {
                        this.openingBalanceDetails[i].orderPriority++;
                    }
                }
            } else if (this.accountingType.includes(ACCOUNTING_TYPE.AO)) {
                for (let i = 0; i < this.openingBalanceDetails.length; i++) {
                    if (
                        this.openingBalanceDetails[i].accountingObjectId === account.accountingObjectId &&
                        this.openingBalanceDetails[i].orderPriority >= account.orderPriority
                    ) {
                        this.openingBalanceDetails[i].orderPriority++;
                    }
                }
            } else {
                for (let i = 0; i < this.openingBalanceDetails.length; i++) {
                    if (
                        this.openingBalanceDetails[i].accountNumber === account.accountNumber &&
                        this.openingBalanceDetails[i].orderPriority >= account.orderPriority
                    ) {
                        this.openingBalanceDetails[i].orderPriority++;
                    }
                }
            }
            this.openingBalanceDetails.splice(index + 1, 0, account);
            this.openingBalance = this.openingBalanceDetails[index + 1];
            let indexSelected = index + 1;
            if (indexSelected > 10) {
                indexSelected -= 10;
            } else {
                indexSelected = 0;
            }

            this.virtualScroller.scrollInto(this.openingBalanceDetails[indexSelected]);
        }
        this.updateHavingData();
    }
    onAddNewRowFromSelected() {
        this.onAddNewRow();
    }
    onRemoveRowFromSelected() {
        this.onDeleteRow();
    }

    onCreateMaterialsGoods() {
        this.soDuDauKyService.createOPMaterialGoods(this.dataUpdate).subscribe(res => {
            this.sessionData = this.repositories.map(x => {
                return { repository: x.id, code: x.repositoryCode, data: [] };
            });
            this.sessionDataOld = this.repositories.map(x => {
                return { repository: x.id, code: x.repositoryCode, data: [] };
            });
            this.onLoadOPMaterialGoods();
            this.toastrService.success(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.updateSuccess'));
            this.cleanCanDeActive = true;
            this.router.navigate([`so-du-dau-ky`]);
        });
        if ((this.deletedItems && this.deletedItems.length) || (this.deletedItemsTemp && this.deletedItemsTemp.length)) {
            const deleteItems = [];
            deleteItems.push(...this.deletedItems.map(x => x.id));
            deleteItems.push(...this.deletedItemsTemp.map(x => x.id));
            this.soDuDauKyService.deleteOPMaterialGoods({ uuids: deleteItems }).subscribe(res => {
                this.deletedItems = [];
            });
        }
    }

    onCreateOPAccount() {
        this.soDuDauKyService.createOPAccount(this.dataUpdate).subscribe(res => {
            this.sessionData = this.currencies.map(x => {
                return { currencyId: x.id, currency: x.currencyCode, data: [] };
            });
            this.sessionDataOld = this.currencies.map(x => {
                return { currencyId: x.id, currency: x.currencyCode, data: [] };
            });
            this.onUpdateCurrency();
            this.toastrService.success(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.updateSuccess'));
            this.cleanCanDeActive = true;
            this.router.navigate([`so-du-dau-ky`]);
        });
        if ((this.deletedItems && this.deletedItems.length) || (this.deletedItemsTemp && this.deletedItemsTemp.length)) {
            const deleteItems = [];
            deleteItems.push(...this.deletedItems.map(x => x.id));
            deleteItems.push(...this.deletedItemsTemp.map(x => x.id));
            this.soDuDauKyService.deleteOPAccount({ uuids: deleteItems }).subscribe(res => {
                this.deletedItems = [];
            });
        }
    }

    acceptDataReference() {
        if (this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
            this.onCreateMaterialsGoods();
        } else {
            this.onCreateOPAccount();
        }
    }

    onSave() {
        const deleteItemTemp = [];
        if (this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
            const opMaterials: IOPMaterialGoods[] = [];
            const dataOld = [];
            for (const repository of this.sessionDataOld) {
                dataOld.push(...this.utilsService.deepCopyObject(repository.data.filter(x => x.amountOriginal)));
            }
            const dataSave = [];
            for (const repository of this.sessionData) {
                dataSave.push(...this.utilsService.deepCopyObject(repository.data.filter(x => x.amountOriginal)));
            }
            deleteItemTemp.push(...dataOld.filter(x => !dataSave.some(y => y.id === x.id)));
            const dataUpdate = dataSave.filter(x => {
                for (let i = 0; i < dataOld.length; i++) {
                    if (x.id && dataOld[i].id && dataOld[i].id === x.id && this.utilsService.isEquivalent(x, dataOld[i])) {
                        return false;
                    }
                }
                return true;
            });
            for (const openingBalance of dataUpdate) {
                const opMaterial: IOPMaterialGoods = {};
                // if (this.accountingType.includes(ACCOUNTING_TYPE.NH) && !openingBalance.bankAccountDetailId) {
                //     return;
                // }
                Object.assign(opMaterial, openingBalance);
                if (!opMaterial.id) {
                    const typeLedger = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                    opMaterial.typeLedger = parseInt(typeLedger, 0);
                }
                if (opMaterial.expiryDateStr) {
                    opMaterial.expiryDate = moment.utc(opMaterial.expiryDateStr, DATE_FORMAT_SLASH);
                }
                opMaterials.push(opMaterial);
            }
            this.dataUpdate = opMaterials;

            let materialGoodsSpecificationsError = '';
            this.dataUpdate.filter(x => !x.id).forEach(detail => {
                const matetialGoods = this.materialGoods.find(x => x.id === detail.materialGoodsId);
                if (
                    matetialGoods.isFollow &&
                    (!detail.materialGoodsSpecificationsLedgers || detail.materialGoodsSpecificationsLedgers.length === 0)
                ) {
                    materialGoodsSpecificationsError = materialGoodsSpecificationsError + ', ' + detail.materialGoodsCode;
                }
            });
            if (materialGoodsSpecificationsError) {
                materialGoodsSpecificationsError = materialGoodsSpecificationsError.substring(2, materialGoodsSpecificationsError.length);
                this.toastrService.error(
                    'Hàng hóa ' +
                        materialGoodsSpecificationsError +
                        ' ' +
                        this.translateService.instant('ebwebApp.materialGoodsSpecifications.noMaterialGoodsSpecifications')
                );
                return false;
            }

            const dataUsing = this.dataUpdate.filter(x => x.id).map(x => x.id);
            if ((this.deletedItems && this.deletedItems.length) || (deleteItemTemp && deleteItemTemp.length)) {
                const deleteItems = [];
                deleteItems.push(...this.deletedItems.map(x => x.id));
                deleteItems.push(...deleteItemTemp.map(x => x.id));
                dataUsing.push(...deleteItems);
            }
            this.deletedItemsTemp = deleteItemTemp;
            this.soDuDauKyService.getCheckReferenceData(dataUsing).subscribe(res => {
                if (res.body.message === 'SUCCESS') {
                    this.onCreateMaterialsGoods();
                } else {
                    this.refModel3 = this.modalService.open(this.validHadReference, { size: 'lg', backdrop: 'static' });
                }
            });
        } else {
            const dataSave = [];
            const dataOld = [];
            for (const repository of this.sessionDataOld) {
                dataOld.push(
                    ...this.utilsService.deepCopyObject(
                        repository.data.filter(x => !x.parentNode && (x.debitAmountOriginal > 0 || x.creditAmountOriginal > 0))
                    )
                );
            }
            for (const opAccount of this.sessionData) {
                dataSave.push(...opAccount.data.filter(x => !x.parentNode && (x.debitAmountOriginal > 0 || x.creditAmountOriginal > 0)));
            }
            deleteItemTemp.push(...dataOld.filter(x => !dataSave.some(y => y.id === x.id)));
            const opAccounts: IOpAccountModel[] = [];
            const dataUpdate = dataSave.filter(x => {
                for (let i = 0; i < dataOld.length; i++) {
                    if (x.id && dataOld[i].id && dataOld[i].id === x.id && this.utilsService.isEquivalent(x, dataOld[i])) {
                        return false;
                    }
                }
                return true;
            });
            for (const openingBalance of dataUpdate) {
                if (!openingBalance) {
                    continue;
                }
                const opAccount: IOpAccountModel = {};
                if (openingBalance.detailType) {
                    const accLst = openingBalance.detailType.split(';');
                    if (accLst.includes(ACCOUNT_DETAIL_TYPE.ACCOUNT_DEBIT) && !openingBalance.bankAccountDetailId) {
                        this.toastrService.success(
                            this.translateService.instant('ebwebApp.tienIch.soDuDauKy.bankAccountNull', {
                                accountNumber: openingBalance.accountNumber,
                                currency: openingBalance.currencyId
                            })
                        );
                        return;
                    }
                }

                Object.assign(opAccount, openingBalance);
                if (!opAccount.id) {
                    opAccount.typeId = this.accountingType.includes(ACCOUNTING_TYPE.AO)
                        ? ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT
                        : ACCOUNTING_TYPE_ID.NORMAL_ACCOUNT;
                    const typeLedger = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                    opAccount.typeLedger = parseInt(typeLedger, 0);
                }
                opAccounts.push(opAccount);
            }
            this.dataUpdate = opAccounts;
            const dataUsing = this.dataUpdate.filter(x => x.id).map(x => x.id);
            if ((this.deletedItems && this.deletedItems.length) || (deleteItemTemp && deleteItemTemp.length)) {
                const deleteItems = [];
                deleteItems.push(...this.deletedItems.map(x => x.id));
                deleteItems.push(...deleteItemTemp.map(x => x.id));
                dataUsing.push(...deleteItems);
            }
            this.deletedItemsTemp = deleteItemTemp;
            this.soDuDauKyService.getCheckReferenceData(dataUsing).subscribe(res => {
                if (res.body.message === 'SUCCESS') {
                    this.onCreateOPAccount();
                } else {
                    this.refModel3 = this.modalService.open(this.validHadReference, { size: 'lg', backdrop: 'static' });
                }
            });
        }
    }

    @HostListener('document:keydown.control.delete')
    onKeyCtrlDelete() {
        if (this.getSelectionText()) {
            return;
        }
        this.onDeleteRow();
    }

    onDeleteRow(detail?) {
        event.preventDefault();
        detail = detail || this.openingBalance;
        const indexDelete = this.openingBalanceDetails.findIndex(x => x === detail);
        if (indexDelete < 0) {
            return;
        }
        // detail.prefixItem = this.openingBalanceDetails[indexDelete - 1];
        if (detail.id && !this.deletedItems.some(x => x.id === detail.id)) {
            this.deletedItems.push({ ...detail });
        }

        let count = 0;
        for (let i = 0; i < this.openingBalanceDetails.length; i++) {
            if (this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
                if (this.openingBalanceDetails[i].materialGoodsId === this.openingBalanceDetails[indexDelete].materialGoodsId) {
                    count++;
                }
            } else if (this.accountingType.includes(ACCOUNTING_TYPE.AO)) {
                if (this.openingBalanceDetails[i].accountingObjectId === this.openingBalanceDetails[indexDelete].accountingObjectId) {
                    count++;
                }
            } else {
                if (this.openingBalanceDetails[i].accountNumber === this.openingBalanceDetails[indexDelete].accountNumber) {
                    count++;
                }
            }
        }
        if (count <= 1) {
            if (this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
                const opMaterialGoodsModel = this.utilsService.deepCopyObject(this.openingBalanceDetails[indexDelete]);
                opMaterialGoodsModel.id = null;
                this.openingBalanceDetails[indexDelete] = opMaterialGoodsModel;
            } else {
                const typeLedger = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                this.openingBalanceDetails[indexDelete].typeLedger = parseInt(typeLedger, 0);
                this.openingBalanceDetails[indexDelete].typeId = this.accountingType.includes(ACCOUNTING_TYPE.MT)
                    ? ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE
                    : this.accountingType.includes(ACCOUNTING_TYPE.AO)
                        ? ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT
                        : ACCOUNTING_TYPE_ID.NORMAL_ACCOUNT;
                this.openingBalanceDetails[indexDelete].postedDate = this.postedDate;
                // // set null
                this.openingBalanceDetails[indexDelete].id = null;
                this.openingBalanceDetails[indexDelete].currencyId = this.currency;
            }
            this.openingBalanceDetails[indexDelete].debitAmountOriginal = 0;
            this.openingBalanceDetails[indexDelete].debitAmount = 0;
            this.openingBalanceDetails[indexDelete].creditAmountOriginal = 0;
            this.openingBalanceDetails[indexDelete].creditAmount = 0;
            this.openingBalanceDetails[indexDelete].exchangeRate = 0;
            this.openingBalanceDetails[indexDelete].unitId = null;
            this.openingBalanceDetails[indexDelete].quantity = 0;
            this.openingBalanceDetails[indexDelete].unitPriceOriginal = 0;
            this.openingBalanceDetails[indexDelete].unitPrice = 0;
            this.openingBalanceDetails[indexDelete].amountOriginal = 0;
            this.openingBalanceDetails[indexDelete].amount = 0;
            this.openingBalanceDetails[indexDelete].lotNo = null;
            this.openingBalanceDetails[indexDelete].expiredDate = null;
            this.openingBalanceDetails[indexDelete].expiryDateStr = null;
            this.openingBalanceDetails[indexDelete].bankAccountDetailId = null;
            this.openingBalanceDetails[indexDelete].expenseItemId = null;
            this.openingBalanceDetails[indexDelete].costSetId = null;
            this.openingBalanceDetails[indexDelete].contractId = null;
            this.openingBalanceDetails[indexDelete].budgetItemId = null;
            this.openingBalanceDetails[indexDelete].departmentId = null;
            this.openingBalanceDetails[indexDelete].statisticsCodeId = null;
            // this.onSelect(this.openingBalanceDetails[indexDelete]);
        } else {
            this.openingBalanceDetails.splice(indexDelete, 1);
            this.onSelect(this.openingBalanceDetails[indexDelete < this.openingBalanceDetails.length ? indexDelete : indexDelete - 1]);
        }
        this.updateHavingData();
        this.virtualScroller.refresh();
        this.updateAmount();
    }

    getCurrencyType(type?: number) {
        if (this.account.organizationUnit.currencyID === 'VND') {
            if (this.currency === 'VND') {
                return 1;
            } else {
                return type === 1 ? 2 : 1;
            }
        } else {
            if (this.currency === 'VND') {
                return type === 1 ? 1 : 2;
            } else {
                return 2;
            }
        }
    }

    getStatusAccount(detail, number: number): boolean {
        if (this.balanceType === ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT) {
            return ![number, 2, 3].includes(this.accountList.accountGroupKind);
        }
        return ![number, 2, 3].includes(detail.accountGroupKind);
    }

    onLoadAccountingObject() {
        const opA = this.sessionData.find(x => x.currency === this.currency);
        if (opA && opA.data && opA.data.length > 0) {
            this.onSelect(opA.data[0]);
            this.openingBalanceDetails = opA.data;
            this.calculatorTotalBalance();
            this.virtualScroller.items = this.openingBalanceDetails;
            if (this.accountList) {
                let indexSelected = this.openingBalanceDetails.findIndex(x => x.accountNumber === this.accountList.accountNumber);
                this.openingBalance = this.openingBalanceDetails[indexSelected];
                if (indexSelected > 15) {
                    indexSelected -= 5;
                }
                this.virtualScroller.scrollInto(this.openingBalanceDetails[indexSelected]);
            }
            return;
        }
        const accountObjects = this.accountObjects;
        this.calculatingParent(accountObjects);
        const opAccounts = accountObjects.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode)).map(x => {
            const opAccount: IOpAccountModel = new OpAccountModel();
            opAccount.accountingObjectId = x.id;
            opAccount.accountingObjectName = x.accountingObjectName;
            opAccount.accountingObjectCode = x.accountingObjectCode;
            opAccount.creditAmountOriginal = 0;
            opAccount.creditAmount = 0;
            opAccount.debitAmountOriginal = 0;
            opAccount.debitAmount = 0;
            opAccount.accountNumber = this.accountList.accountNumber;
            opAccount.currencyId = this.currency;
            const typeLedger = this.account.systemOption.find(soa => soa.code === SO_LAM_VIEC).data;
            opAccount.typeLedger = parseInt(typeLedger, 0);
            opAccount.postedDate = this.postedDate;
            return opAccount;
        });
        this.soDuDauKyService
            .findAllOpAccounts({ accountNumber: this.accountList.accountNumber, currencyId: this.currency })
            .subscribe(res => {
                for (let i = 0; i < opAccounts.length; i++) {
                    const resItem = res.body
                        .filter(x => x.accountingObjectId === opAccounts[i].accountingObjectId)
                        .sort((a, b) => a.orderPriority - b.orderPriority);
                    let iCount = 0;

                    for (let j = 0; j < resItem.length; j++) {
                        // this.cleanDetail(resItem[0]);
                        resItem[j].accountingObjectCode = opAccounts[i].accountingObjectCode;
                        resItem[j].accountingObjectName = opAccounts[i].accountingObjectName;
                        if (j === 0) {
                            opAccounts[i] = resItem[0];
                        } else {
                            opAccounts.splice(i + j, 0, resItem[j]);
                            iCount++;
                        }
                    }
                    i += iCount;
                }
                opA.data.push(
                    ...this.utilsService
                        .deepCopyObject(opAccounts)
                        .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode))
                );
                this.sessionDataOld.find(x => x.currency === this.currency).data = this.utilsService.deepCopyObject(opA.data);
                this.openingBalanceDetails = opA.data;
                this.virtualScroller.items = this.openingBalanceDetails;
                this.calculatorTotalBalance();
                this.onSelect(opA.data[0]);
                if (this.accountList) {
                    let indexSelected = this.openingBalanceDetails.findIndex(x => x.accountNumber === this.accountList.accountNumber);
                    this.openingBalance = this.openingBalanceDetails[indexSelected];
                    if (indexSelected > 15) {
                        indexSelected -= 5;
                    }
                    this.virtualScroller.scrollInto(this.openingBalanceDetails[indexSelected]);
                }
            });
    }

    onLoadOPMaterialGoods() {
        const opMG = this.sessionData.find(x => x.repository === this.repository);
        if (opMG && opMG.data && opMG.data.length > 0) {
            this.openingBalance = opMG.data[0];
            this.openingBalanceDetails = opMG.data;
            this.virtualScroller.items = this.openingBalanceDetails;
            this.calculatorTotalBalance();
            return;
        }
        this.soDuDauKyService
            .findAllOPMaterialGoods({ accountNumber: this.accountList.accountNumber, repositoryId: this.repository })
            .subscribe(resOPM => {
                const openingBalanceDetailsOld = resOPM.body.filter(x => x.amountOriginal > 0 || x.amount > 0);
                for (let i = 0; i < this.materialGoods.length; i++) {
                    let opMaterialGoods: OpMaterialGoodsModel = new OpMaterialGoodsModel();
                    if (openingBalanceDetailsOld.some(x => x.materialGoodsId === this.materialGoods[i].id)) {
                        openingBalanceDetailsOld.filter(x => x.materialGoodsId === this.materialGoods[i].id).forEach(obdo => {
                            opMaterialGoods = { ...obdo };
                            if (opMaterialGoods.expiryDate) {
                                opMaterialGoods.expiryDateStr = moment.utc(opMaterialGoods.expiryDate).format(DATE_FORMAT_SLASH);
                            }
                            this.getUnit(opMaterialGoods);
                            opMG.data.push(opMaterialGoods);
                        });
                    } else {
                        opMaterialGoods.accountNumber = this.accountList.accountNumber;
                        const typeLedger = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                        opMaterialGoods.typeLedger = parseInt(typeLedger, 0);
                        opMaterialGoods.postedDate = this.utilsService.ngayHachToan(this.account);
                        opMaterialGoods.materialGoodsId = this.materialGoods[i].id;
                        opMaterialGoods.materialGoodsCode = this.materialGoods[i].materialGoodsCode;
                        opMaterialGoods.materialGoodsName = this.materialGoods[i].materialGoodsName;
                        opMaterialGoods.unitName = this.materialGoods[i].unitName;
                        opMaterialGoods.unitId = this.materialGoods[i].unitID;
                        opMaterialGoods.repositoryId = this.repository;
                        opMaterialGoods.typeId = ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE;
                        opMaterialGoods.currencyId = this.currency;
                        // opMaterialGoods.expiryDate = moment();
                        this.getUnit(opMaterialGoods);
                        opMG.data.push(opMaterialGoods);
                    }
                }
                opMG.data = opMG.data.sort(
                    (a, b) => a.materialGoodsCode.localeCompare(b.materialGoodsCode) || b.orderPriority - a.orderPriority
                );
                this.openingBalance = opMG.data[0];
                // this.sessionData.find(x => x.repository === this.repository).data = opMG.data;
                this.sessionDataOld.find(x => x.repository === this.repository).data = this.utilsService.deepCopyObject(opMG.data);
                this.openingBalanceDetails = opMG.data;
                this.calculatorTotalBalance();
                this.virtualScroller.items = this.openingBalanceDetails;
                // this.materialGoodsService.getMaterialGoodsCustom().subscribe((res: HttpResponse<IMaterialGoods[]>) => {
                //     this.materialGoods = res.body.sort((a, b) => a.materialGoodsCode.localeCompare(b.materialGoodsCode));
                //
                // });
            });
    }

    calculatorTotalBalance() {
        this.totalCreditBalanceOriginal = 0;
        this.totalCreditBalance = 0;
        this.totalDebitBalanceOriginal = 0;
        this.totalDebitBalance = 0;
        this.totalAmountOriginal = 0;
        this.totalAmountOriginal = 0;
        for (let i = 0; i < this.openingBalanceDetails.length; i++) {
            if (!this.openingBalanceDetails[i].parentNode) {
                this.totalDebitBalanceOriginal += this.openingBalanceDetails[i].debitAmountOriginal || 0;
                this.totalDebitBalance += this.openingBalanceDetails[i].debitAmount || 0;
                this.totalCreditBalanceOriginal += this.openingBalanceDetails[i].creditAmountOriginal || 0;
                this.totalCreditBalance += this.openingBalanceDetails[i].creditAmount || 0;
                this.totalAmountOriginal += this.openingBalanceDetails[i].amountOriginal || 0;
                this.totalAmount += this.openingBalanceDetails[i].amount || 0;
            }
        }
    }
    // đơn vị tính
    getUnit(detail) {
        detail.units = this.units.filter(x => x.materialGoodsID === detail.materialGoodsId) || [];
    }

    getUnitPriceOriginal(detail) {
        detail.unitPrices = detail.unitPrices || [];
        detail.unitPrice = detail.unitPrice || 0;
        detail.unitPriceOriginal = detail.unitPriceOriginal || 0;
        if (!detail.unitId || !detail.currencyId || !detail.materialGoodsId) {
            return;
        }
        this.unitService
            .unitPriceOriginalForMaterialGoods({
                materialGoodsId: detail.materialGoodsId,
                unitId: detail.unitId,
                currencyCode: detail.currencyId
            })
            .subscribe(
                res => {
                    for (const item of res.body) {
                        detail.unitPrices.push({ data: item });
                    }
                    if (detail.unitPrices.length && detail.unitPrices[0]) {
                        detail.unitPriceOriginal = detail.unitPrices[0].data;
                    }
                    this.selectUnit(detail);
                },
                error => console.log(error)
            );
    }

    selectUnit(detail) {
        // if (detail.unitId === detail.mainUnitId) {
        //     detail.mainConvertRate = 1;
        //     detail.formula = '*';
        //     detail.mainQuantity = detail.quantity;
        //     detail.mainUnitPrice = detail.unitPriceOriginal;
        // } else {
        //     const unit = detail.units.find(x => x.id === detail.unitId);
        //     detail.mainConvertRate = detail.unit.convertRate;
        //     detail.formula = detail.unit.formula;
        //     detail.unitId = detail.unit.id;
        //     if (detail.formula === '/') {
        //         detail.mainQuantity = detail.unit.convertRate / detail.quantity;
        //         detail.mainUnitPrice = detail.unitPriceOriginal * detail.mainConvertRate;
        //     } else {
        //         detail.mainQuantity = detail.quantity * detail.unit.convertRate;
        //         detail.mainUnitPrice = detail.unitPriceOriginal / detail.mainConvertRate;
        //     }
        // }
        this.changeQuantity(detail);
        this.calculatingParent(this.openingBalanceDetails);
    }

    changeQuantity(detail) {
        if (detail.quantity && detail.unitPriceOriginal) {
            detail.amountOriginal = detail.quantity * detail.unitPriceOriginal;
            detail.amount = detail.amountOriginal;
        }
        this.updateHavingData();
        this.roundObjects();
        this.calculatingParent(this.openingBalanceDetails);
    }

    roundObjects() {
        this.utilsService.roundObjects(
            this.openingBalanceDetails.filter(x => !x.isAccountList),
            this.account.systemOption,
            !this.isForeignCurrency
        );
    }

    roundObject(detail) {
        this.utilsService.roundObject(detail, this.account.systemOption, !this.isForeignCurrency);
    }

    onUpdateCurrency() {
        if (!this.currency) {
            return;
        }
        this.openingBalanceDetails = [];
        this.currencies.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
        if (this.accountingType.includes(ACCOUNTING_TYPE.AO)) {
            this.balanceType = ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT;
            this.accountingObjectService
                .getAccountingObjectsActiveForRSInwardOutward()
                .subscribe((resAO: HttpResponse<IAccountingObject[]>) => {
                    const accLst = this.accountList.detailType.split(';');
                    this.accountObjects = resAO.body;

                    if (accLst.includes(ACCOUNT_DETAIL_TYPE.ACCOUNT_EMPLOYEE)) {
                        this.accountObjects = this.accountObjects.filter(x => x.isEmployee);
                    }
                    if (accLst.includes(ACCOUNT_DETAIL_TYPE.ACCOUNT_SUPPLIER)) {
                        this.accountObjects = this.accountObjects.filter(x => [0, 2].includes(x.objectType));
                    }
                    if (accLst.includes(ACCOUNT_DETAIL_TYPE.ACCOUNT_CUSTOMER)) {
                        this.accountObjects = this.accountObjects.filter(x => [1, 2].includes(x.objectType));
                    }

                    this.onLoadAccountingObject();
                });
        } else if (!this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
            this.balanceType = ACCOUNTING_TYPE_ID.NORMAL_ACCOUNT;
            this.setUpOpeningBalances();
        }

        if (this.account && this.currency && this.account.organizationUnit && this.account.organizationUnit.currencyID) {
            this.isForeignCurrency = this.account.organizationUnit.currencyID !== this.currency;
        }
    }

    calculatingParent(listOP: any[]) {
        listOP.forEach(item => {
            if (item.parentNode) {
                item.debitAmountOriginal = 0;
                item.debitAmount = 0;
                item.creditAmountOriginal = 0;
                item.creditAmount = 0;
                item.amountOriginal = 0;
                item.amount = 0;
            }
        });
        for (let i = listOP.length - 1; i >= 0; i--) {
            if (listOP[i].parentAccountID) {
                const parentMode = listOP.find(x => x.id === listOP[i].parentAccountID);
                if (parentMode) {
                    parentMode.debitAmountOriginal += listOP[i].debitAmountOriginal || 0;
                    parentMode.debitAmount += listOP[i].debitAmount || 0;
                    parentMode.creditAmountOriginal += listOP[i].creditAmountOriginal || 0;
                    parentMode.creditAmount += listOP[i].creditAmount || 0;
                    parentMode.amountOriginal += listOP[i].amountOriginal || 0;
                    parentMode.amount += listOP[i].amount || 0;
                }
            }
        }
        this.calculatorTotalBalance();
    }

    selectUnitPriceOriginal(detail: any) {
        this.changeQuantity(detail);
        this.changeAmountOriginal(detail);
    }

    selectUnitPrice(detail: any) {
        detail.amount = detail.quantity * detail.unitPrice;
        this.roundObject(detail);
        this.calculatingParent(this.openingBalanceDetails);
    }

    changeAmountOriginal(detail: any) {
        this.cleanDetailId(detail);
        // detail.unitPriceOriginal = detail.amountOriginal / detail.quantity;
        this.updateCurrencyDetail(detail);
        this.updateExchangeRate(detail);
        if (!this.isForeignCurrency) {
            detail.unitPrice = detail.unitPriceOriginal;
            detail.amount = detail.amountOriginal;
        }
        this.roundObject(detail);
        this.updateHavingData();
        this.calculatingParent(this.openingBalanceDetails);
    }

    changeAmount(detail: any) {
        // detail.unitPrice = detail.amount / detail.quantity;
        this.updateCurrencyDetail(detail);
        this.updateExchangeRate(detail);
        this.roundObject(detail);
        this.calculatingParent(this.openingBalanceDetails);
    }

    onChangeCreditAmountOriginal(detail: any) {
        this.cleanDetailId(detail);
        const formula = this.currencies.find(x => x.currencyCode === this.currency).formula;
        detail.creditAmount = this.isForeignCurrency
            ? formula === '*' ? detail.creditAmountOriginal * detail.exchangeRate : detail.creditAmountOriginal / detail.exchangeRate
            : detail.creditAmountOriginal;
        this.updateCurrencyDetail(detail);
        this.updateExchangeRate(detail);
        this.updateAmount();
        this.updateHavingData();
        this.roundObject(detail);
        this.calculatingParent(this.openingBalanceDetails);
    }

    onChangeDebitAmountOriginal(detail: any) {
        this.cleanDetailId(detail);
        const formula = this.currencies.find(x => x.currencyCode === this.currency).formula;
        detail.debitAmount = this.isForeignCurrency
            ? formula === '*' ? detail.debitAmountOriginal * detail.exchangeRate : detail.debitAmountOriginal / detail.exchangeRate
            : detail.debitAmountOriginal;
        this.updateCurrencyDetail(detail);
        this.updateExchangeRate(detail);
        this.updateAmount();
        this.updateHavingData();
        this.calculatingParent(this.openingBalanceDetails);
    }

    cleanDetailId(detail: any) {
        if (detail.orderPriority === undefined || detail.orderPriority === null) {
            detail.orderPriority = 0;
        }
        if (detail.id) {
            if (this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
                const oldDetail = this.sessionDataOld.find(x => x.repository === this.repository).data.find(x => x.id === detail.id);
                if (!oldDetail.amountOriginal && !oldDetail.amount) {
                    detail.id = null;
                }
            } else {
                const oldDetail = this.sessionDataOld.find(x => x.currency === this.currency).data.find(x => x.id === detail.id);
                if (
                    !oldDetail.debitAmount &&
                    !oldDetail.debitAmountOriginal &&
                    !oldDetail.creditAmount &&
                    !oldDetail.creditAmountOriginal
                ) {
                    detail.id = null;
                }
            }
        }
    }

    onChangeExchangeRate(detail: any) {
        const formula = this.currencies.find(x => x.currencyCode === this.currency).formula;
        if (this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
            detail.amount = formula === '*' ? detail.amountOriginal * detail.exchangeRate : detail.amountOriginal / detail.exchangeRate;
        } else {
            if (detail.debitAmountOriginal > 0) {
                detail.debitAmount =
                    formula === '*' ? detail.debitAmountOriginal * detail.exchangeRate : detail.debitAmountOriginal / detail.exchangeRate;
            }
            if (detail.creditAmountOriginal > 0) {
                detail.creditAmount =
                    formula === '*' ? detail.creditAmountOriginal * detail.exchangeRate : detail.creditAmountOriginal / detail.exchangeRate;
            }
        }
        this.calculatingParent(this.openingBalanceDetails);
    }

    updateCurrencyDetail(detail) {
        if (!detail.currencyId) {
            detail.currencyId = this.currency;
        }
    }

    updateExchangeRate(detail: any) {
        if (this.accountingType.includes(ACCOUNTING_TYPE.MT)) {
            if ((!detail.exchangeRate || detail.exchangeRate === '') && detail.amountOriginal > 0) {
                detail.exchangeRate = this.currencies.find(x => x.currencyCode === this.currency).exchangeRate;
            }
        } else {
            if (
                (!detail.exchangeRate || detail.exchangeRate === '') &&
                (detail.debitAmountOriginal > 0 || detail.creditAmountOriginal > 0)
            ) {
                detail.exchangeRate = this.currencies.find(x => x.currencyCode === this.currency).exchangeRate;
            }
        }
    }

    updateAmount() {
        for (let i = this.openingBalanceDetails.length - 1; i >= 0; i--) {
            if (this.openingBalanceDetails[i].parentNode) {
                this.openingBalanceDetails[i].creditAmountOriginal = 0;
                this.openingBalanceDetails[i].creditAmount = 0;
                this.openingBalanceDetails[i].debitAmountOriginal = 0;
                this.openingBalanceDetails[i].debitAmount = 0;
                const childNodes = this.openingBalanceDetails.filter(x => x.parentAccountID === this.openingBalanceDetails[i].id);
                for (let j = 0; j < childNodes.length; j++) {
                    this.openingBalanceDetails[i].creditAmountOriginal += childNodes[j].creditAmountOriginal || 0;
                    this.openingBalanceDetails[i].creditAmount += childNodes[j].creditAmount || 0;
                    this.openingBalanceDetails[i].debitAmountOriginal += childNodes[j].debitAmountOriginal || 0;
                    this.openingBalanceDetails[i].debitAmount += childNodes[j].debitAmount || 0;
                }
            }
        }
    }
    importFromExcel(detailModal) {
        this.refModel3 = this.modalService.open(detailModal, { size: 'lg', backdrop: 'static' });
    }
    downloadTem() {
        this.isLoading = true;
        this.soDuDauKyService.downloadTem({ type: this.balanceType }).subscribe(
            res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name =
                    this.balanceType === ACCOUNTING_TYPE_ID.NORMAL_ACCOUNT
                        ? 'SoDuDauKy_TKThongThuong.xlsx'
                        : this.balanceType === ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT
                            ? 'SoDuDauKy_TKChiTietDoiTuong.xlsx'
                            : 'SoDuDauKy_TKChiTietVTHH.xlsx';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
                this.isLoading = false;
            },
            () => {
                this.isLoading = false;
            }
        );
    }

    changeFile(event) {
        const file = event.target.files;
        this.file = null;
        if (file && file.length) {
            this.file = file[0];
        }
    }
    checkHaveData(download: any, updLoadForm: any) {
        if (this.balanceType === ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE) {
            this.soDuDauKyService.getCheckHaveDataMG().subscribe(res => {
                if (res.body) {
                    this.refModel2 = this.modalService.open(updLoadForm, { size: 'lg', backdrop: 'static' });
                } else {
                    this.acceptedData();
                }
            });
        } else {
            this.soDuDauKyService.getCheckHaveData({ typeId: this.balanceType }).subscribe(res => {
                if (res.body) {
                    this.refModel2 = this.modalService.open(updLoadForm, { size: 'lg', backdrop: 'static' });
                } else {
                    this.acceptedData();
                }
            });
        }
    }

    acceptedData() {
        if (this.balanceType === ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE) {
            this.soDuDauKyService.acceptedOPMaterialGoods(this.opsData).subscribe(() => this.onAcceptedDataSuccess());
        } else {
            this.soDuDauKyService.acceptedOPAccount(this.opsData, this.balanceType).subscribe(() => this.onAcceptedDataSuccess());
        }
    }

    onAcceptedDataSuccess() {
        if (this.refModel) {
            this.refModel.close();
        }
        if (this.refModel2) {
            this.refModel2.close();
        }
        if (this.refModel3) {
            this.refModel3.close();
        }
        this.toastrService.success(this.translateService.instant('ebwebApp.saBill.upload.success'));
        this.router.navigate([`so-du-dau-ky`]);
    }

    upload(download: any, updLoadForm) {
        this.isLoading = true;
        if (this.balanceType === ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE) {
            this.soDuDauKyService.uploadMaterialGoods(this.file).subscribe(
                res => this.onUpdateRes(res, download, updLoadForm),
                () => {
                    this.isLoading = false;
                }
            );
        } else if (this.balanceType === ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT) {
            this.soDuDauKyService.uploadAccountingObject(this.file).subscribe(
                res => this.onUpdateRes(res, download, updLoadForm),
                () => {
                    this.isLoading = false;
                }
            );
        } else {
            this.soDuDauKyService.uploadAccountNormal(this.file).subscribe(
                res => this.onUpdateRes(res, download, updLoadForm),
                () => {
                    this.isLoading = false;
                }
            );
        }
    }
    chooseSheet(name, download: any, updLoadForm) {
        this.isLoading = true;
        if (this.refModel2) {
            this.refModel2.close();
        }
        if (this.balanceType === ACCOUNTING_TYPE_ID.MATERIAL_GOODS_TYPE) {
            this.soDuDauKyService.uploadMaterialGoods(this.file, name).subscribe(
                res => this.onUpdateRes(res, download, updLoadForm),
                () => {
                    this.isLoading = false;
                }
            );
        } else if (this.balanceType === ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT) {
            this.soDuDauKyService.uploadAccountingObject(this.file, name).subscribe(
                res => this.onUpdateRes(res, download, updLoadForm),
                () => {
                    this.isLoading = false;
                }
            );
        } else {
            this.soDuDauKyService.uploadAccountNormal(this.file, name).subscribe(
                res => this.onUpdateRes(res, download, updLoadForm),
                () => {
                    this.isLoading = false;
                }
            );
        }
    }

    onUpdateRes(res, download, updLoadForm) {
        this.isEdit = this.isRoleEdit;
        if (res.headers.get('isError') === '1') {
            if (res.headers.get('message')) {
                if (res.headers.get('message') === ImportExcel.SELECT_SHEET) {
                    const blob = new Blob([res.body], { type: 'aapplication/json' });
                    const reader = new FileReader();
                    reader.onload = () => {
                        this.sheetNames = JSON.parse(reader.result);
                        this.refModel2 = this.modalService.open(this.chooseSheetModal, { size: 'lg', backdrop: 'static' });
                    };
                    reader.readAsBinaryString(blob);
                } else {
                    this.toastrService.error(this.translateService.instant('ebwebApp.saBill.upload.' + res.headers.get('message')));
                }
            } else {
                // this.refModel.close();
                this.refModel = this.modalService.open(download, { size: 'lg', backdrop: 'static' });
                const blob = new Blob([res.body], { type: 'application/pdf' });
                const fileURL = URL.createObjectURL(blob);

                this.link = document.createElement('a');
                document.body.appendChild(this.link);
                this.link.download = fileURL;
                this.link.setAttribute('style', 'display: none');
                const name =
                    this.balanceType === ACCOUNTING_TYPE_ID.NORMAL_ACCOUNT
                        ? 'SoDuDauKy_TKThongThuong_Loi.xlsx'
                        : this.balanceType === ACCOUNTING_TYPE_ID.ACCOUNTING_OBJECT
                            ? 'SoDuDauKy_TKChiTietDoiTuong_Loi.xlsx'
                            : 'SoDuDauKy_TKChiTietVTHH_Loi.xlsx';
                this.link.setAttribute('download', name);
                this.link.href = fileURL;
            }
        } else {
            this.opsData = res.body;
            this.checkHaveData(download, updLoadForm);
        }
    }
    download() {
        this.link.click();
    }

    viewMaterialGoodsSpecification(detail) {
        const matetialGoods = this.materialGoods.find(x => x.id === detail.materialGoodsId);
        if (matetialGoods.isFollow) {
            this.refModalService.open(
                detail,
                EbMaterialGoodsSpecificationsModalComponent,
                detail.materialGoodsSpecificationsLedgers,
                false,
                1
            );
        } else {
            this.toastrService.error(this.translateService.instant('ebwebApp.materialGoodsSpecifications.notFollow'));
        }
    }

    registerMaterialGoodsSpecifications() {
        this.eventManager.subscribe('materialGoodsSpecifications', ref => {
            this.openingBalanceDetails[this.currentRow].materialGoodsSpecificationsLedgers = ref.content;
            this.openingBalanceDetails[this.currentRow].quantity = ref.content.reduce(function(prev, cur) {
                return prev + cur.iWQuantity;
            }, 0);
        });
    }

    ngAfterContentChecked(): void {
        this.disableInput();
        this.disableParentNode();
    }
}
