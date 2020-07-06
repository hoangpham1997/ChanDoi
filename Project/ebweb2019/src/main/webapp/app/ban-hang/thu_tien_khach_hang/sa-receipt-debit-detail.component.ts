import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { MCReceiptService } from 'app/TienMatNganHang/phieu-thu/mc-receipt';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { PaymentClauseService } from 'app/entities/payment-clause';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { CurrencyService } from 'app/danhmuc/currency';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { CostSetService } from 'app/entities/cost-set';
import { AccountDefaultService } from 'app/danhmuc/account-default';
import { ExpenseItemService } from 'app/entities/expense-item';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { EMContractService } from 'app/entities/em-contract';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IEMContract } from 'app/shared/model/em-contract.model';
import * as moment from 'moment';
import { Moment } from 'moment';
import { BudgetItemService } from 'app/entities/budget-item';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { ACCOUNTING_TYPE_ID, AccountType, CategoryName, GROUP_TYPEID, SO_LAM_VIEC, SU_DUNG_SO_QUAN_TRI, TypeID } from 'app/app.constants';
import { Principal } from 'app/core';
import { MBTellerPaperService } from 'app/TienMatNganHang/BaoNo/mb-teller-paper';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Observable, Subscription } from 'rxjs';
import { AccountListService } from 'app/danhmuc/account-list';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { CreditCardService } from 'app/danhmuc/credit-card';
import { ISAReceiptDebitBill } from 'app/shared/model/sa-receipt-debit-bill';
import { ISAReceiptDebitAccounting } from 'app/shared/model/sa-receipt-debit-accounting';
import { IMCReceipt } from 'app/shared/model/mc-receipt.model';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { IMCReceiptDetails } from 'app/shared/model/mc-receipt-details.model';
import { IMCReceiptDetailCustomer } from 'app/shared/model/mc-receipt-detail-customer.model';
import { IMBDepositDetails } from 'app/shared/model/mb-deposit-details.model';
import { IMBDepositDetailCustomer } from 'app/shared/model/mb-deposit-detail-customer.model';
import { MBDepositService } from 'app/TienMatNganHang/BaoCo/mb-deposit';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-sa-receipt-debit',
    templateUrl: './sa-receipt-debit-detail.component.html',
    styleUrls: ['./sa-receipt-debit-detail.component.css']
})
export class SAReceiptDebitDetailComponent extends BaseComponent implements OnInit {
    accountingObjects: IAccountingObject[];
    employees: IAccountingObject[];
    organizationUnits: IOrganizationUnit[];
    costSets: ICostSet[];
    paymentClauses: IPaymentClause[];
    currencys: ICurrency[];
    autoPrinciples: IAutoPrinciple[];
    autoPrinciplesAll: IAutoPrinciple[];
    autoPrinciple: IAutoPrinciple;
    accountDefault: { value?: string };
    expenseItems: IExpenseItem[];
    statisticCodes: IStatisticsCode[];
    eMContracts: IEMContract[];
    accountingObject: IAccountingObject;
    budgetItems: IBudgetItem[];
    bankAccountDetails: IBankAccountDetails[];
    bankAccountDetailID: string;
    creditAccountList: any[];
    debitAccountList: any[];
    discountAccountList: any[];
    typeID: number;
    record_: any;
    creditCardType: string;
    ownerCard: string;
    creditCardNumber: string;
    accountingObjectID: string;
    accountingObjectCode: string;
    accountingObjectName: string;
    accountingObjectAddress: string;
    accountingObjectBankName: string;
    bankName: string;
    taxCode: string;
    payers: string;
    reason: string;
    employeeID: string;
    employee: IAccountingObject;
    numberAttach: string;
    no: string;
    date: Moment;
    postedDate: Moment;
    currencyID: string;
    exchangeRate: number;
    typeLedger: string;
    totalAmount: number;
    totalAmountOriginal: number;
    paymentMethod: string;
    saReceiptDebitDetails: ISAReceiptDebitBill[];
    saReceiptDebitBills: ISAReceiptDebitBill[];
    saReceiptDebitAccountings: ISAReceiptDebitAccounting[];
    checkAll: boolean;
    fromDate: string;
    toDate: string;
    showVaoSo: boolean;
    isSoTaiChinh: boolean;
    modalRef: NgbModalRef;
    mCReceipt: IMCReceipt;
    mBDeposit: IMBDeposit;
    sumAmountBills: number;
    sumAmountOriginalBills: number;
    sumDebitAmount: number;
    sumDebitAmountOriginal: number;
    sumDiffAmount: number;
    sumDiscountAmount: number;
    sumDiscountAmountOriginal: number;
    account: any;
    viewNo: string;
    contextMenu: ContextMenu;
    contextmenu = { value: false };
    contextmenuX = { value: 0 };
    contextmenuY = { value: 0 };
    eventSubscriber: Subscription;
    accountCurrencyID: string;
    phepTinhTyGia: string;
    TYPE_OPN = TypeID.OPN_DOI_TUONG;
    indexFocusDetailRow: any;
    indexFocusDetailCol: any;
    idIndex: any;
    constructor(
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private mCReceiptService: MCReceiptService,
        private mBTellerPaperService: MBTellerPaperService,
        private mBDepositService: MBDepositService,
        private accountingObjectService: AccountingObjectService,
        private paymentClauseService: PaymentClauseService,
        private activatedRoute: ActivatedRoute,
        private autoPrinciplellService: AutoPrincipleService,
        private currencyService: CurrencyService,
        private organizationUnitService: OrganizationUnitService,
        private costsetService: CostSetService,
        private accountDefaultService: AccountDefaultService,
        private expenseItemService: ExpenseItemService,
        private gLService: GeneralLedgerService,
        public utilsService: UtilsService,
        private statisticsCodeService: StatisticsCodeService,
        private eMContractService: EMContractService,
        private budgetItemService: BudgetItemService,
        private bankAccountDetailService: BankAccountDetailsService,
        private creditCardService: CreditCardService,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService,
        private principal: Principal,
        private modalService: NgbModal,
        private accountListService: AccountListService
    ) {
        super();
        this.autoPrinciplesAll = [];
        this.bankAccountDetailService.getBankAccountDetails().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
            this.bankAccountDetails = res.body.sort((a, b) => a.bankAccount.localeCompare(b.bankAccount));
        });
        this.paymentClauseService.query().subscribe(
            (res: HttpResponse<IPaymentClause[]>) => {
                this.paymentClauses = res.body
                    .filter(n => n.isActive)
                    .sort((a, b) => a.paymentClauseCode.localeCompare(b.paymentClauseCode));
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.autoPrinciplellService.getAutoPrinciplesByCompanyID().subscribe((res: HttpResponse<IAutoPrinciple[]>) => {
            this.autoPrinciplesAll = res.body;
        });
        this.organizationUnitService.getOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body
                .filter(n => n.isActive)
                .sort((a, b) => a.organizationUnitCode.localeCompare(b.organizationUnitCode));
        });
        this.expenseItemService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body.filter(n => n.isActive).sort((a, b) => a.expenseItemCode.localeCompare(b.expenseItemCode));
        });
        this.costsetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body.filter(n => n.isActive).sort((a, b) => a.costSetCode.localeCompare(b.costSetCode));
        });
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticCodes = res.body.filter(n => n.isActive).sort((a, b) => a.statisticsCode.localeCompare(b.statisticsCode));
        });
        this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
            this.eMContracts = res.body.filter(n => n.isActive).sort((a, b) => a.name.localeCompare(b.name));
        });
        this.budgetItemService.getBudgetItems().subscribe((res: HttpResponse<IBudgetItem[]>) => {
            this.budgetItems = res.body.filter(n => n.isActive).sort((a, b) => a.budgetItemCode.localeCompare(b.budgetItemCode));
        });
        this.contextMenu = new ContextMenu();
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ saReceiptDebitBills }) => {
            this.saReceiptDebitDetails = saReceiptDebitBills;
        });
        for (let i = 0; i < this.saReceiptDebitDetails.length; i++) {
            this.saReceiptDebitDetails[i].amount = 0;
            this.saReceiptDebitDetails[i].amountOriginal = 0;
            this.saReceiptDebitDetails[i].differAmount = 0;
            this.saReceiptDebitDetails[i].discountRate = 0;
            this.saReceiptDebitDetails[i].discountAmount = 0;
            this.saReceiptDebitDetails[i].discountAmountOriginal = 0;
        }
        this.saReceiptDebitBills = [];
        this.paymentMethod = '0';
        if (this.activatedRoute.snapshot.paramMap.has('id')) {
            this.accountingObjectID = this.activatedRoute.snapshot.paramMap.get('id');
            this.fromDate = this.activatedRoute.snapshot.paramMap.get('fromDate');
            this.toDate = this.activatedRoute.snapshot.paramMap.get('toDate');
        }
        this.accountingObjectService.getAllDTO().subscribe(
            (res: HttpResponse<IAccountingObject[]>) => {
                this.accountingObjects = res.body
                    .filter(x => x.objectType === 1 || x.objectType === 2)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                this.employees = res.body
                    .filter(n => n.isEmployee && n.isActive)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                this.accountingObject = this.accountingObjects.find(x => x.id === this.accountingObjectID);
                this.accountingObjectCode = this.accountingObject.accountingObjectCode;
                this.accountingObjectName = this.accountingObject.accountingObjectName;
                this.accountingObjectAddress = this.accountingObject.accountingObjectAddress;
                this.taxCode = this.accountingObject.taxCode;
                this.payers = this.accountingObject.contactName;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.principal.identity().then(account => {
            this.account = account;
            if (this.account) {
                this.accountCurrencyID = this.account.organizationUnit.currencyID;
                this.showVaoSo = this.account.systemOption.some(x => x.code === SU_DUNG_SO_QUAN_TRI && x.data === '1');
                this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                this.typeLedger = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                this.date = this.utilsService.ngayHachToan(account);
                this.postedDate = this.date;
                this.reason = this.translate.instant('ebwebApp.sAReceiptDebit.reason');
                this.changePaymentMethod();
                if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                    this.currencyID = this.account.organizationUnit.currencyID;
                    this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                        this.currencys = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                        this.selectCurrentcy();
                    });
                }
            }
        });
        this.checkAll = false;
        this.taxCode = '';
        this.payers = '';
        this.reason = '';
        this.employeeID = '';
        this.numberAttach = '';
        this.totalAmount = 0;
        this.totalAmountOriginal = 0;
        this.saReceiptDebitAccountings = [];
        this.viewNo = '';
        this.registerAddNewRow();
        this.registerDeleteRow();
        this.registerCopyRow();
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
        });
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = true;
            this.registerComboboxSave(response);
        });
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.saReceiptDebitAccountings;
    }

    saveParent() {
        this.currentRow = null;
    }

    addDataToDetail() {
        this.saReceiptDebitAccountings = this.details ? this.details : this.saReceiptDebitAccountings;
        this.employeeID = this.nhanVienID ? this.nhanVienID : this.employeeID;
        this.bankAccountDetailID = this.taiKhoanNganHangID ? this.taiKhoanNganHangID : this.bankAccountDetailID;
    }

    changePaymentMethod() {
        if (this.paymentMethod === '0') {
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: GROUP_TYPEID.GROUP_MCRECEIPT,
                    displayOnBook: this.typeLedger
                })
                .subscribe((res: HttpResponse<string>) => {
                    this.no = res.body;
                });
            this.typeID = TypeID.PHIEU_THU_TIEN_KHACH_HANG;
            this.autoPrinciples = this.autoPrinciplesAll.filter(x => x.typeId === TypeID.PHIEU_THU);
        } else if (this.paymentMethod === '1') {
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: GROUP_TYPEID.GROUP_MBDEPOSIT,
                    displayOnBook: this.typeLedger
                })
                .subscribe((res: HttpResponse<string>) => {
                    this.no = res.body;
                });
            this.typeID = TypeID.NOP_TIEN_TU_KHACH_HANG;
            this.autoPrinciples = this.autoPrinciplesAll.filter(x => x.typeId === TypeID.NOP_TIEN_TK);
        }

        this.getAccount();
    }

    selectAccountingObject() {
        if (this.accountingObject) {
            this.accountingObjectID = this.accountingObject.id;
            this.accountingObjectCode = this.accountingObject.accountingObjectCode;
            this.accountingObjectName = this.accountingObject.accountingObjectName;
            this.accountingObjectAddress = this.accountingObject.accountingObjectAddress;
            this.taxCode = this.accountingObject.taxCode;
            this.payers = this.accountingObject.contactName;
            this.accountingObjectService
                .getPPPayVendorBills({
                    fromDate: this.fromDate,
                    toDate: this.toDate,
                    accountObjectID: this.accountingObject.id
                })
                .subscribe((res: HttpResponse<ISAReceiptDebitBill[]>) => {
                    this.saReceiptDebitDetails = res.body.filter(x => x.creditAmountOriginal > 0);
                    this.saReceiptDebitBills = this.saReceiptDebitDetails.filter(x => x.currencyID === this.currencyID);
                    this.saReceiptDebitAccountings = [];
                });
        }
    }

    selectAutoPrinciple() {
        if (this.autoPrinciple !== null && this.autoPrinciple !== undefined) {
            this.reason = this.autoPrinciple.autoPrincipleName;
        }
    }

    selectChangeBank(bankAccountDetailID: string) {
        const bankAccountDetail = this.bankAccountDetails.find(x => x.id === bankAccountDetailID);
        if (bankAccountDetail) {
            this.bankName = bankAccountDetail.bankName;
        } else {
            this.bankName = '';
        }
    }

    selectCurrentcy() {
        if (this.currencyID !== null) {
            this.exchangeRate = this.currencys.find(n => n.currencyCode === this.currencyID).exchangeRate;
            this.phepTinhTyGia = this.currencys.find(n => n.currencyCode === this.currencyID).formula;
            this.saReceiptDebitBills = this.saReceiptDebitDetails.filter(x => x.currencyID === this.currencyID);
            this.saReceiptDebitAccountings = [];
            this.sumDebitAmount = this.saReceiptDebitBills.reduce(function(prev, cur) {
                return prev + cur.creditAmount;
            }, 0);
            this.sumDebitAmountOriginal = this.saReceiptDebitBills.reduce(function(prev, cur) {
                return prev + cur.creditAmountOriginal;
            }, 0);
            this.changeExchangeRate();
        }
    }

    submit(question) {
        if (this.saReceiptDebitBills.filter(x => x.check).length === 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.pPPayVendor.error.noSelectVoucher'),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
            return;
        }
        if (this.saReceiptDebitBills.filter(x => x.check && x.discountRate > 100).length > 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
            return;
        }
        if (this.saReceiptDebitBills.filter(x => x.check && x.amountOriginal > x.creditAmountOriginal).length > 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.sAReceiptDebit.error.checkAmount'),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
            return;
        }
        if (this.checkCloseBook(this.account, this.postedDate)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.lockBook.err.checkForSave'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (this.paymentMethod === '0') {
            this.mCReceipt = {};
            this.mCReceipt.typeID = TypeID.PHIEU_THU_TIEN_KHACH_HANG;
            this.mCReceipt.accountingObject = this.accountingObject;
            this.mCReceipt.employeeID = this.employeeID;
            this.mCReceipt.date = this.date;
            this.mCReceipt.postedDate = this.postedDate;
            this.mCReceipt.typeLedger = Number.parseInt(this.typeLedger);
            if (this.mCReceipt.typeLedger === 0) {
                this.mCReceipt.noFBook = this.no;
                this.mCReceipt.noMBook = null;
            } else if (this.mCReceipt.typeLedger === 1) {
                this.mCReceipt.noMBook = this.no;
                this.mCReceipt.noFBook = null;
            } else {
                if (this.isSoTaiChinh) {
                    this.mCReceipt.noFBook = this.no;
                } else {
                    this.mCReceipt.noMBook = this.no;
                }
            }
            this.mCReceipt.accountingObjectName = this.accountingObjectName;
            this.mCReceipt.accountingObjectAddress = this.accountingObjectAddress;
            this.mCReceipt.accountingObjectType = 1;
            this.mCReceipt.reason = this.reason;
            this.mCReceipt.numberAttach = this.numberAttach;
            this.mCReceipt.payers = this.payers;
            this.mCReceipt.currencyID = this.currencyID;
            this.mCReceipt.exchangeRate = this.exchangeRate;
            this.mCReceipt.totalAmount = this.totalAmount;
            this.mCReceipt.totalAmountOriginal = this.totalAmountOriginal;
            this.mCReceipt.totalVATAmount = 0;
            this.mCReceipt.totalVATAmountOriginal = 0;
            this.mCReceipt.recorded = false;
            this.mCReceipt.exported = false;
            this.mCReceipt.mcreceiptDetailCustomers = [];
            this.mCReceipt.mcreceiptDetails = [];
            const listSAReceiptBills0 = this.saReceiptDebitBills.filter(x => x.check);
            for (let i = 0; i < listSAReceiptBills0.length; i++) {
                const mcReceiptDetailCustomer: IMCReceiptDetailCustomer = {};
                mcReceiptDetailCustomer.voucherTypeID = listSAReceiptBills0[i].typeID;
                mcReceiptDetailCustomer.creditAccount = listSAReceiptBills0[i].account;
                mcReceiptDetailCustomer.receipableAmount = listSAReceiptBills0[i].totalCredit;
                mcReceiptDetailCustomer.receipableAmountOriginal = listSAReceiptBills0[i].totalCreditOriginal;
                mcReceiptDetailCustomer.remainingAmount = listSAReceiptBills0[i].creditAmount;
                mcReceiptDetailCustomer.remainingAmountOriginal = listSAReceiptBills0[i].creditAmountOriginal;
                mcReceiptDetailCustomer.amount = listSAReceiptBills0[i].amount;
                mcReceiptDetailCustomer.amountOriginal = listSAReceiptBills0[i].amountOriginal;
                mcReceiptDetailCustomer.discountRate = listSAReceiptBills0[i].discountRate;
                mcReceiptDetailCustomer.discountAccount = listSAReceiptBills0[i].discountAccount;
                mcReceiptDetailCustomer.differAmount = listSAReceiptBills0[i].differAmount;
                mcReceiptDetailCustomer.discountAmount = listSAReceiptBills0[i].discountAmount;
                mcReceiptDetailCustomer.discountAmountOriginal = listSAReceiptBills0[i].discountAmountOriginal;
                mcReceiptDetailCustomer.accountingObject = this.mCReceipt.accountingObject;
                mcReceiptDetailCustomer.saleInvoiceID = listSAReceiptBills0[i].referenceID;
                mcReceiptDetailCustomer.refVoucherExchangeRate = listSAReceiptBills0[i].refVoucherExchangeRate;
                mcReceiptDetailCustomer.differAmount = listSAReceiptBills0[i].differAmount;
                mcReceiptDetailCustomer.lastExchangeRate = listSAReceiptBills0[i].lastExchangeRate;
                this.mCReceipt.mcreceiptDetailCustomers.push(mcReceiptDetailCustomer);
            }
            for (let i = 0; i < this.saReceiptDebitAccountings.length; i++) {
                const mCReceiptDetail: IMCReceiptDetails = {};
                mCReceiptDetail.description = this.saReceiptDebitAccountings[i].description;
                mCReceiptDetail.debitAccount = this.saReceiptDebitAccountings[i].debitAccount;
                mCReceiptDetail.creditAccount = this.saReceiptDebitAccountings[i].creditAccount;
                mCReceiptDetail.amount = this.saReceiptDebitAccountings[i].totalAmount;
                mCReceiptDetail.amountOriginal = this.saReceiptDebitAccountings[i].totalAmountOriginal;
                mCReceiptDetail.budgetItem = this.saReceiptDebitAccountings[i].budgetItem;
                mCReceiptDetail.costSet = this.saReceiptDebitAccountings[i].costSet;
                mCReceiptDetail.contractID = this.saReceiptDebitAccountings[i].emContract;
                mCReceiptDetail.statisticsCode = this.saReceiptDebitAccountings[i].statisticsCode;
                mCReceiptDetail.expenseItem = this.saReceiptDebitAccountings[i].expenseItem;
                mCReceiptDetail.organizationUnit = this.saReceiptDebitAccountings[i].department;
                mCReceiptDetail.accountingObject = this.accountingObject;
                this.mCReceipt.mcreceiptDetails.push(mCReceiptDetail);
            }
            this.subscribeToSaveMCReceiptResponse(this.mCReceiptService.create(this.mCReceipt), question);
        } else if (this.paymentMethod === '1') {
            if (!this.bankAccountDetailID && this.saReceiptDebitAccountings.filter(x => x.debitAccount.startsWith('112')).length > 0) {
                this.toastr.error(this.translate.instant('ebwebApp.mBTellerPaper.error.bankAccountNullMessage'));
                return;
            }
            this.mBDeposit = {};
            this.mBDeposit.typeID = TypeID.NOP_TIEN_TU_KHACH_HANG;
            this.mBDeposit.accountingObjectID = this.accountingObject.id;
            this.mBDeposit.employeeID = this.employeeID;
            this.mBDeposit.date = this.date;
            this.mBDeposit.postedDate = this.postedDate;
            this.mBDeposit.typeLedger = Number.parseInt(this.typeLedger);
            if (this.mBDeposit.typeLedger === 0) {
                this.mBDeposit.noFBook = this.no;
                this.mBDeposit.noMBook = null;
            } else if (this.mBDeposit.typeLedger === 1) {
                this.mBDeposit.noMBook = this.no;
                this.mBDeposit.noFBook = null;
            } else {
                if (this.isSoTaiChinh) {
                    this.mBDeposit.noFBook = this.no;
                } else {
                    this.mBDeposit.noMBook = this.no;
                }
            }
            this.mBDeposit.accountingObjectName = this.accountingObjectName;
            this.mBDeposit.accountingObjectAddress = this.accountingObjectAddress;
            this.mBDeposit.bankAccountDetailID = this.bankAccountDetailID;
            this.mBDeposit.bankName = this.bankName;
            this.mBDeposit.accountingObjectType = 1;
            this.mBDeposit.reason = this.reason;
            this.mBDeposit.currencyID = this.currencyID;
            this.mBDeposit.exchangeRate = this.exchangeRate;
            this.mBDeposit.totalAmount = this.totalAmount;
            this.mBDeposit.totalAmountOriginal = this.totalAmountOriginal;
            this.mBDeposit.totalVATAmount = 0;
            this.mBDeposit.totalVATAmountOriginal = 0;
            this.mBDeposit.recorded = false;
            this.mBDeposit.exported = false;
            this.mBDeposit.mBDepositDetailCustomers = [];
            this.mBDeposit.mBDepositDetails = [];
            const listSAReceiptBills1 = this.saReceiptDebitBills.filter(x => x.check);
            for (let i = 0; i < listSAReceiptBills1.length; i++) {
                const mbDepositDetailCustomer: IMBDepositDetailCustomer = {};
                mbDepositDetailCustomer.voucherTypeID = listSAReceiptBills1[i].typeID;
                mbDepositDetailCustomer.creditAccount = listSAReceiptBills1[i].account;
                mbDepositDetailCustomer.receipableAmount = listSAReceiptBills1[i].totalCredit;
                mbDepositDetailCustomer.receipableAmountOriginal = listSAReceiptBills1[i].totalCreditOriginal;
                mbDepositDetailCustomer.remainingAmount = listSAReceiptBills1[i].creditAmount;
                mbDepositDetailCustomer.remainingAmountOriginal = listSAReceiptBills1[i].creditAmountOriginal;
                mbDepositDetailCustomer.amount = listSAReceiptBills1[i].amount;
                mbDepositDetailCustomer.amountOriginal = listSAReceiptBills1[i].amountOriginal;
                mbDepositDetailCustomer.discountRate = listSAReceiptBills1[i].discountRate;
                mbDepositDetailCustomer.discountAccount = listSAReceiptBills1[i].discountAccount;
                mbDepositDetailCustomer.differAmount = listSAReceiptBills1[i].differAmount;
                mbDepositDetailCustomer.discountAmount = listSAReceiptBills1[i].discountAmount;
                mbDepositDetailCustomer.discountAmountOriginal = listSAReceiptBills1[i].discountAmountOriginal;
                mbDepositDetailCustomer.accountingObject = this.accountingObject;
                mbDepositDetailCustomer.saleInvoiceID = listSAReceiptBills1[i].referenceID;
                mbDepositDetailCustomer.refVoucherExchangeRate = listSAReceiptBills1[i].refVoucherExchangeRate;
                mbDepositDetailCustomer.differAmount = listSAReceiptBills1[i].differAmount;
                mbDepositDetailCustomer.lastExchangeRate = listSAReceiptBills1[i].lastExchangeRate;
                this.mBDeposit.mBDepositDetailCustomers.push(mbDepositDetailCustomer);
            }
            for (let i = 0; i < this.saReceiptDebitAccountings.length; i++) {
                const mBDepositDetail: IMBDepositDetails = {};
                mBDepositDetail.description = this.saReceiptDebitAccountings[i].description;
                mBDepositDetail.debitAccount = this.saReceiptDebitAccountings[i].debitAccount;
                mBDepositDetail.creditAccount = this.saReceiptDebitAccountings[i].creditAccount;
                mBDepositDetail.amount = this.saReceiptDebitAccountings[i].totalAmount;
                mBDepositDetail.amountOriginal = this.saReceiptDebitAccountings[i].totalAmountOriginal;
                if (this.saReceiptDebitAccountings[i].budgetItem) {
                    mBDepositDetail.budgetItemID = this.saReceiptDebitAccountings[i].budgetItem.id;
                }
                if (this.saReceiptDebitAccountings[i].costSet) {
                    mBDepositDetail.costSetID = this.saReceiptDebitAccountings[i].costSet.id;
                }
                if (this.saReceiptDebitAccountings[i].emContract) {
                    mBDepositDetail.contractID = this.saReceiptDebitAccountings[i].emContract.id;
                }
                if (this.saReceiptDebitAccountings[i].statisticsCode) {
                    mBDepositDetail.statisticsCodeID = this.saReceiptDebitAccountings[i].statisticsCode.id;
                }
                if (this.saReceiptDebitAccountings[i].expenseItem) {
                    mBDepositDetail.expenseItemID = this.saReceiptDebitAccountings[i].expenseItem.id;
                }
                if (this.saReceiptDebitAccountings[i].department) {
                    mBDepositDetail.departmentID = this.saReceiptDebitAccountings[i].department.id;
                }
                mBDepositDetail.accountingObjectID = this.accountingObject.id;
                this.mBDeposit.mBDepositDetails.push(mBDepositDetail);
            }
            this.subscribeToSaveMBDepositResponse(this.mBDepositService.create(this.mBDeposit), question);
        }
    }

    private subscribeToSaveMCReceiptResponse(result: Observable<HttpResponse<any>>, question) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.mCReceipt.id = res.body.mCReceipt.id;
                    this.mCReceipt.recorded = res.body.mCReceipt.recorded;
                    this.viewNo = res.body.mCReceipt.noFBook == null ? res.body.mCReceipt.noMBook : res.body.mCReceipt.noFBook;
                    this.modalRef = this.modalService.open(question, { backdrop: 'static' });
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                } else if (res.body.status === 2) {
                    this.recordFailed();
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private subscribeToSaveMBDepositResponse(result: Observable<HttpResponse<any>>, question) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.mBDeposit.id = res.body.mbDeposit.id;
                    this.mBDeposit.recorded = res.body.mbDeposit.recorded;
                    this.viewNo = res.body.mbDeposit.noFBook == null ? res.body.mbDeposit.noMBook : res.body.mbDeposit.noFBook;
                    this.modalRef = this.modalService.open(question, { backdrop: 'static' });
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                } else if (res.body.status === 2) {
                    this.recordFailed();
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private noVoucherExist() {
        this.toastr.error(
            this.translate.instant('global.data.noVocherAlreadyExist'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
    }

    private recordFailed() {
        this.toastr.error(this.translate.instant('global.data.recordFailed'), this.translate.instant('ebwebApp.mCReceipt.home.message'));
    }

    private onSaveSuccess() {
        this.toastr.success(
            this.translate.instant('ebwebApp.mCReceipt.home.saveSuccess'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
    }

    private onSaveError() {}

    viewVoucher(saReceiptDebitBill: ISAReceiptDebitBill) {
        let url = '';
        if (saReceiptDebitBill.typeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
            url = `/#/chung-tu-ban-hang/${saReceiptDebitBill.referenceID}/edit/from-ref`;
        } else if (saReceiptDebitBill.typeID === TypeID.CHUNG_TU_NGHIEP_VU_KHAC) {
            url = `/#/g-other-voucher/${saReceiptDebitBill.referenceID}/edit/from-ref`;
        }
        window.open(url, '_blank');
    }

    calculateAmont(saReceiptDebitBill: ISAReceiptDebitBill) {
        if (saReceiptDebitBill.amountOriginal > saReceiptDebitBill.creditAmountOriginal) {
            this.toastr.warning(
                this.translate.instant('ebwebApp.sAReceiptDebit.error.checkAmount'),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
        }
        saReceiptDebitBill.amount = this.utilsService.round(
            this.phepTinhTyGia === '*'
                ? saReceiptDebitBill.amountOriginal * this.exchangeRate
                : saReceiptDebitBill.amountOriginal / this.exchangeRate,
            this.account.systemOption,
            7
        );
        if (saReceiptDebitBill.amountOriginal > 0) {
            saReceiptDebitBill.check = true;
        }
        this.calculateDiscount(saReceiptDebitBill);
    }

    calculateDiscount(saReceiptDebitBill: ISAReceiptDebitBill) {
        if (saReceiptDebitBill.discountRate > 100) {
            this.toastr.warning(
                this.translate.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
        } else {
            if (!saReceiptDebitBill.discountRate) {
                saReceiptDebitBill.discountRate = 0;
            }
            if (!saReceiptDebitBill.amountOriginal) {
                saReceiptDebitBill.amountOriginal = 0;
            }
            saReceiptDebitBill.discountAmount = saReceiptDebitBill.amount * saReceiptDebitBill.discountRate / 100;
            saReceiptDebitBill.discountAmountOriginal = saReceiptDebitBill.amountOriginal * saReceiptDebitBill.discountRate / 100;
            this.calculateTotal();
            this.genAccountingByDiscounts();
        }
    }

    changeDiscountAmount(saReceiptDebitBill: ISAReceiptDebitBill) {
        if (saReceiptDebitBill.discountAmountOriginal) {
            saReceiptDebitBill.discountRate = saReceiptDebitBill.discountAmountOriginal * 100 / saReceiptDebitBill.amountOriginal;
            saReceiptDebitBill.discountAmount = this.utilsService.round(
                this.phepTinhTyGia === '*'
                    ? saReceiptDebitBill.discountAmountOriginal * this.exchangeRate
                    : saReceiptDebitBill.discountAmountOriginal / this.exchangeRate,
                this.account.systemOption,
                7
            );
            this.calculateTotal();
        }
    }

    changeExchangeRate() {
        for (let i = 0; i < this.saReceiptDebitBills.length; i++) {
            this.saReceiptDebitBills[i].amount = this.utilsService.round(
                this.phepTinhTyGia === '*'
                    ? this.saReceiptDebitBills[i].amountOriginal * this.exchangeRate
                    : this.saReceiptDebitBills[i].amountOriginal / this.exchangeRate,
                this.account.systemOption,
                7
            );
            this.saReceiptDebitBills[i].discountAmount = this.utilsService.round(
                this.phepTinhTyGia === '*'
                    ? this.saReceiptDebitBills[i].discountAmountOriginal * this.exchangeRate
                    : this.saReceiptDebitBills[i].discountAmountOriginal / this.exchangeRate,
                this.account.systemOption,
                7
            );
        }
        this.calculateTotal();
    }

    calculateTotal() {
        const amount = this.saReceiptDebitBills.filter(x => x.check).reduce(function(prev, cur) {
            return prev + cur.amount;
        }, 0);
        const amountOriginal = this.saReceiptDebitBills.filter(x => x.check).reduce(function(prev, cur) {
            return prev + cur.amountOriginal;
        }, 0);
        const discountAmount = this.saReceiptDebitBills.filter(x => x.check).reduce(function(prev, cur) {
            return prev + cur.discountAmount;
        }, 0);
        const discountAmountOriginal = this.saReceiptDebitBills.filter(x => x.check).reduce(function(prev, cur) {
            return prev + cur.discountAmountOriginal;
        }, 0);
        this.totalAmount = amount - discountAmount;
        this.totalAmountOriginal = amountOriginal - discountAmountOriginal;
        this.sumAmountBills = 0;
        this.sumAmountOriginalBills = 0;
        this.sumDiffAmount = 0;
        this.sumDiscountAmount = 0;
        this.sumDiscountAmountOriginal = 0;
        this.saReceiptDebitBills.forEach((item: ISAReceiptDebitBill) => {
            if (item.amount) {
                this.sumAmountBills += item.amount;
            }
            if (item.amountOriginal) {
                this.sumAmountOriginalBills += item.amountOriginal;
            }
            if (item.differAmount) {
                this.sumDiffAmount += item.differAmount;
            }
            if (item.discountAmount) {
                this.sumDiscountAmount += item.discountAmount;
            }
            if (item.discountAmountOriginal) {
                this.sumDiscountAmountOriginal += item.discountAmountOriginal;
            }
        });
    }

    genAccountings(saReceiptDebitBill: ISAReceiptDebitBill) {
        if (saReceiptDebitBill.check) {
            saReceiptDebitBill.amount = saReceiptDebitBill.creditAmount;
            saReceiptDebitBill.amountOriginal = saReceiptDebitBill.creditAmountOriginal;
            saReceiptDebitBill.discountRate = 0;
        } else {
            saReceiptDebitBill.amount = 0;
            saReceiptDebitBill.amountOriginal = 0;
            saReceiptDebitBill.discountRate = 0;
        }
        this.calculateDiscount(saReceiptDebitBill);
    }

    checkAllFunction() {
        if (this.saReceiptDebitBills) {
            this.checkAll = !this.checkAll;
            for (let i = 0; i < this.saReceiptDebitBills.length; i++) {
                this.saReceiptDebitBills[i].check = this.checkAll;
                if (this.checkAll) {
                    this.saReceiptDebitBills[i].amount = this.saReceiptDebitBills[i].creditAmount;
                    this.saReceiptDebitBills[i].amountOriginal = this.saReceiptDebitBills[i].creditAmountOriginal;
                    this.saReceiptDebitBills[i].discountRate = 0;
                } else {
                    this.saReceiptDebitBills[i].amount = 0;
                    this.saReceiptDebitBills[i].amountOriginal = 0;
                    this.saReceiptDebitBills[i].discountRate = 0;
                }
                this.calculateDiscount(this.saReceiptDebitBills[i]);
            }
        }
    }

    genAccountingByDiscounts() {
        this.saReceiptDebitAccountings = [];
        const group = this.saReceiptDebitBills.filter(x => x.check).reduce((g: any, saReceiptDebitBill: ISAReceiptDebitBill) => {
            g[saReceiptDebitBill.account] = g[saReceiptDebitBill.account] || [];
            g[saReceiptDebitBill.account].push(saReceiptDebitBill);
            return g;
        }, {});
        const groupData = Object.keys(group).map(a => {
            return {
                account: a,
                saReceiptDebitBills: group[a]
            };
        });
        for (let i = 0; i < groupData.length; i++) {
            const saReceiptDebitAccounting: ISAReceiptDebitAccounting = {};
            saReceiptDebitAccounting.creditAccount = groupData[i].account;
            saReceiptDebitAccounting.debitAccount =
                this.paymentMethod === '0' ? (this.currencyID === 'VND' ? '1111' : '1112') : this.currencyID === 'VND' ? '1121' : '1122';
            saReceiptDebitAccounting.accountingObjectCode = this.accountingObjectCode;
            saReceiptDebitAccounting.description = 'Thu tiền khách hàng ' + this.accountingObjectName;
            saReceiptDebitAccounting.totalAmount = groupData[i].saReceiptDebitBills.reduce(function(prev, cur) {
                return prev + (cur.amount - cur.discountAmount);
            }, 0);
            saReceiptDebitAccounting.totalAmountOriginal = groupData[i].saReceiptDebitBills.reduce(function(prev, cur) {
                return prev + (cur.amountOriginal - cur.discountAmountOriginal);
            }, 0);
            this.saReceiptDebitAccountings.push(saReceiptDebitAccounting);
            const saReceiptDebitBillDiscounts: ISAReceiptDebitBill[] = groupData[i].saReceiptDebitBills;
            const groupDiscount = saReceiptDebitBillDiscounts
                .filter(x => x.discountAccount !== null && x.discountAccount !== undefined)
                .reduce((g: any, saReceiptDebitBillDis: ISAReceiptDebitBill) => {
                    g[saReceiptDebitBillDis.discountAccount] = g[saReceiptDebitBillDis.discountAccount] || [];
                    g[saReceiptDebitBillDis.discountAccount].push(saReceiptDebitBillDis);
                    return g;
                }, {});
            const groupDataDiscount = Object.keys(groupDiscount).map(d => {
                return {
                    discountAccount: d,
                    saReceiptDebitBillDiscounts: groupDiscount[d]
                };
            });
            for (let j = 0; j < groupDataDiscount.length; j++) {
                const saReceiptDebitAccountingDis: ISAReceiptDebitAccounting = {};
                saReceiptDebitAccountingDis.creditAccount = groupData[i].account;
                saReceiptDebitAccountingDis.debitAccount = groupDataDiscount[j].discountAccount;
                saReceiptDebitAccountingDis.accountingObjectCode = this.accountingObjectCode;
                saReceiptDebitAccountingDis.description = 'Chiết khấu thanh toán được hưởng';
                saReceiptDebitAccountingDis.totalAmount = groupDataDiscount[j].saReceiptDebitBillDiscounts.reduce(function(prev, cur) {
                    return prev + cur.discountAmount;
                }, 0);
                saReceiptDebitAccountingDis.totalAmountOriginal = groupDataDiscount[j].saReceiptDebitBillDiscounts.reduce(function(
                    prev,
                    cur
                ) {
                    return prev + cur.discountAmountOriginal;
                },
                0);
                this.saReceiptDebitAccountings.push(saReceiptDebitAccountingDis);
            }
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    viewRelateVoucher() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.paymentMethod === '0') {
            this.router.navigate(['./mc-receipt', this.mCReceipt.id, 'edit']);
        } else if (this.paymentMethod === '1') {
            this.router.navigate(['./mb-deposit', this.mBDeposit.id, 'edit']);
        }
    }

    closeQuestion() {
        if (this.modalRef) {
            this.modalRef.close();
            this.accountingObjectService
                .getSAReceiptDebitBills({
                    fromDate: this.fromDate != null ? this.fromDate : null,
                    toDate: this.date != null ? this.date.format('YYYY/MM/DD') : null,
                    accountObjectID: this.accountingObjectID
                })
                .subscribe((res: HttpResponse<ISAReceiptDebitBill[]>) => {
                    this.saReceiptDebitDetails = res.body.filter(x => x.creditAmountOriginal > 0);
                    this.saReceiptDebitBills = this.saReceiptDebitDetails.filter(x => x.currencyID === this.currencyID);
                    this.saReceiptDebitAccountings = [];
                    this.changePaymentMethod();
                    this.calculateTotal();
                });
        }
    }

    checkPostedDateGreaterDate() {
        this.postedDate = this.date;
        this.accountingObjectService
            .getSAReceiptDebitBills({
                fromDate: this.fromDate != null ? this.fromDate : null,
                toDate: this.date != null ? this.date.format('YYYY/MM/DD') : null,
                accountObjectID: this.accountingObjectID
            })
            .subscribe((res: HttpResponse<ISAReceiptDebitBill[]>) => {
                this.saReceiptDebitDetails = res.body.filter(x => x.creditAmountOriginal > 0);
                this.saReceiptDebitBills = this.saReceiptDebitDetails.filter(x => x.currencyID === this.currencyID);
                this.saReceiptDebitAccountings = [];
                this.changePaymentMethod();
                this.calculateTotal();
            });
    }

    getAccount() {
        const columnList = [
            { column: AccountType.TK_CO, ppType: false },
            { column: AccountType.TK_NO, ppType: false },
            { column: AccountType.TK_CHIET_KHAU, ppType: false }
        ];
        const param = {
            typeID: this.typeID,
            columnName: columnList
        };
        this.accountListService.getAccountTypeFour(param).subscribe(res => {
            this.creditAccountList = res.body.creditAccount;
            this.debitAccountList = res.body.debitAccount;
            this.discountAccountList = res.body.discountAccount;
            if (this.saReceiptDebitAccountings && this.saReceiptDebitAccountings.length > 0) {
                this.saReceiptDebitAccountings.forEach(item => {
                    item.debitAccount = this.debitAccountList[0].accountNumber;
                });
            }
        });
    }

    addNewRow(isRightClick?) {
        let length = 0;
        if (isRightClick) {
            this.saReceiptDebitAccountings.splice(this.indexFocusDetailRow + 1, 0, {});
            length = this.indexFocusDetailRow + 2;
        } else {
            this.saReceiptDebitAccountings.push({});
            length = this.saReceiptDebitAccountings.length;
        }
        this.saReceiptDebitAccountings[
            this.saReceiptDebitAccountings.length - 1
        ].accountingObjectCode = this.accountingObject.accountingObjectCode;
        if (this.saReceiptDebitAccountings.length > 0) {
            this.saReceiptDebitAccountings[this.saReceiptDebitAccountings.length - 1].accountingObjectCode = this.saReceiptDebitAccountings[
                this.saReceiptDebitAccountings.length - 2
            ].accountingObjectCode;
            this.saReceiptDebitAccountings[this.saReceiptDebitAccountings.length - 1].description = this.saReceiptDebitAccountings[
                this.saReceiptDebitAccountings.length - 2
            ].description;
            this.saReceiptDebitAccountings[this.saReceiptDebitAccountings.length - 1].creditAccount = this.saReceiptDebitAccountings[
                this.saReceiptDebitAccountings.length - 2
            ].creditAccount;
            this.saReceiptDebitAccountings[this.saReceiptDebitAccountings.length - 1].debitAccount = this.saReceiptDebitAccountings[
                this.saReceiptDebitAccountings.length - 2
            ].debitAccount;
        }
        this.saReceiptDebitAccountings[this.saReceiptDebitAccountings.length - 1].totalAmount = 0;
        this.saReceiptDebitAccountings[this.saReceiptDebitAccountings.length - 1].totalAmountOriginal = 0;
        this.contextmenu.value = false;
        if (isRightClick && this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
            const id = this.idIndex;
            const row = this.indexFocusDetailRow + 1;
            this.indexFocusDetailRow = row;
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(id + row);
                if (element) {
                    element.focus();
                }
            }, 0);
        } else {
            const nameTag: string = event.srcElement.id;
            const idx: number = this.saReceiptDebitAccountings.length - 1;
            const nameTagIndex: string = nameTag + String(idx);
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(nameTagIndex);
                if (element) {
                    element.focus();
                }
            }, 0);
        }
    }

    onrightClick($event, data, selectedData, isNew, isDelete, isCopy) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isCopy = isCopy;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.selectedData = selectedData;
    }

    disableContextMenu() {
        this.contextMenu.isShow = false;
    }

    keyPress(detail) {
        this.saReceiptDebitAccountings.splice(this.saReceiptDebitAccountings.indexOf(detail), 1);
        if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
            // vì còn trường hợp = 0
            if (this.saReceiptDebitAccountings.length > 0) {
                let row = 0;
                if (this.indexFocusDetailRow > this.saReceiptDebitAccountings.length - 1) {
                    row = this.indexFocusDetailRow - 1;
                } else {
                    row = this.indexFocusDetailRow;
                }
                const id = this.idIndex;
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(id + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
        }
    }

    registerAddNewRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.addNewRow(true);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            this.keyPress(this.contextMenu.selectedData);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            this.copyRow(this.contextMenu.selectedData, true);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    previousState() {
        window.history.back();
    }

    addIfLastInput(i) {
        if (i === this.saReceiptDebitAccountings.length - 1) {
            this.addNewRow();
        }
    }

    copyRow(detail, isRigthClick?) {
        if (!this.getSelectionText() || isRigthClick) {
            const detailCopy: any = Object.assign({}, detail);
            detailCopy.id = null;
            this.saReceiptDebitAccountings.push(detailCopy);
            if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const id = this.idIndex;
                const row = this.saReceiptDebitAccountings.length - 1;
                this.indexFocusDetailRow = row;
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(id + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
        }
    }

    actionFocus(indexCol, indexRow, id) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
        this.idIndex = id;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
