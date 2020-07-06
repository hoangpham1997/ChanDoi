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
import { Moment } from 'moment';
import { IPPPayVendorBill } from 'app/shared/model/pp-pay-vendor-bill';
import { IPPPayVendorAccounting } from 'app/shared/model/pp-pay-vendor-accounting';
import { BudgetItemService } from 'app/entities/budget-item';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { ICreditCard } from 'app/shared/model/credit-card.model';
import { CreditCardService } from 'app/danhmuc/credit-card';
import { IMCPayment } from 'app/shared/model/mc-payment.model';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { IMBCreditCard } from 'app/shared/model/mb-credit-card.model';
import { IMCPaymentDetailVendor } from 'app/shared/model/mc-payment-detail-vendor.model';
import { IMCPaymentDetails } from 'app/shared/model/mc-payment-details.model';
import { IMBCreditCardDetailVendor } from 'app/shared/model/mb-credit-card-detail-vendor.model';
import { IMBCreditCardDetails } from 'app/shared/model/mb-credit-card-details.model';
import { IMBTellerPaperDetailVendor } from 'app/shared/model/mb-teller-paper-detail-vendor.model';
import { IMBTellerPaperDetails } from 'app/shared/model/mb-teller-paper-details.model';
import { AccountType, CategoryName, MSGERROR, SO_LAM_VIEC, SU_DUNG_SO_QUAN_TRI, TypeID } from 'app/app.constants';
import { Principal } from 'app/core';
import { MCPaymentService } from 'app/TienMatNganHang/phieu-chi/mc-payment';
import { MBTellerPaperService } from 'app/TienMatNganHang/BaoNo/mb-teller-paper';
import { MBCreditCardService } from 'app/TienMatNganHang/TheTinDung/mb-credit-card';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Observable, Subscription } from 'rxjs';
import { AccountListService } from 'app/danhmuc/account-list';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { AccountingObjectBankAccountService } from 'app/entities/accounting-object-bank-account';
import { forEach } from '@angular/router/src/utils/collection';

@Component({
    selector: 'eb-pp-pay-vendor',
    templateUrl: './pp-pay-vendor-detail.component.html',
    styleUrls: ['./pp-pay-vendor-detail.component.css']
})
export class PPPayVendorDetailComponent extends BaseComponent implements OnInit {
    private TYPE_MC_PAYMENT = 116;
    private TYPE_MB_TELLER_PAPER_1 = 126;
    private TYPE_MB_TELLER_PAPER_2 = 134;
    private TYPE_MB_TELLER_PAPER_3 = 144;
    private TYPE_MB_CREDIT_CARD = 174;
    private TYPE_GROUP_MC_PAYMENT = 11;
    private TYPE_GROUP_MB_TELLER_PAPER_1 = 12;
    private TYPE_GROUP_MB_TELLER_PAPER_2 = 13;
    private TYPE_GROUP_MB_TELLER_PAPER_3 = 14;
    private TYPE_GROUP_MB_CREDIT_CARD = 17;
    private TYPE_PP_INVOICE = 210;
    private TYPE_PP_SERVICE = 240;
    private TYPE_PP_DISCOUNT_RETURN = 230;
    private TYPE_FA_INCREAMENT = 610;
    private TYPE_TI_INCREAMENT = 510;
    private TYPE_GO_VOUCHER = 707;
    TYPE_OPN = TypeID.OPN_DOI_TUONG;
    eventSubscriber: Subscription;
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
    bankAccountDetail: IBankAccountDetails;
    accountingObjectBankAccounts: IAccountingObjectBankAccount[];
    accountingObjectBankAccount?: IAccountingObjectBankAccount;
    creditcards: ICreditCard[];
    creditAccountList: any[];
    debitAccountList: any[];
    discountAccountList: any[];
    DDSo_TienVND: number;
    DDSo_NgoaiTe: number;
    typeID: number;
    record_: any;
    creditCardType: string;
    ownerCard: string;
    creditCard: any;
    accountingObjectID: string;
    accountingObjectCode: string;
    accountingObjectName: string;
    accountingObjectAddress: string;
    accountingObjectBankName: string;
    bankName: string;
    taxCode: string;
    receiver: string;
    reason: string;
    employeeID: string;
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
    pPPayVendorDetails: IPPPayVendorBill[];
    pPPayVendorBills: IPPPayVendorBill[];
    pPPayVendorAccountings: IPPPayVendorAccounting[];
    checkAll: boolean;
    fromDate: string;
    toDate: string;
    showVaoSo: boolean;
    isSoTaiChinh: boolean;
    modalRef: NgbModalRef;
    mCPayment: IMCPayment;
    mBCreditCard: IMBCreditCard;
    mBTellerPaper: IMBTellerPaper;
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
    accountCurrencyID: string;
    phepTinhTyGia: string;
    typeOPN: TypeID.OPN_DOI_TUONG;
    indexFocusDetailRow: any;
    indexFocusDetailCol: any;
    idIndex: any;
    constructor(
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private mCReceiptService: MCReceiptService,
        private mCPaymentService: MCPaymentService,
        private mBTellerPaperService: MBTellerPaperService,
        private mBCreditCardService: MBCreditCardService,
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
        private accountingObjectBankAccountService: AccountingObjectBankAccountService,
        private creditCardService: CreditCardService,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService,
        private principal: Principal,
        private modalService: NgbModal,
        private accountListService: AccountListService,
        private viewVoucherService: ViewVoucherService
    ) {
        super();
        this.autoPrinciplesAll = [];
        this.bankAccountDetailService.getBankAccountDetails().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
            this.bankAccountDetails = res.body.sort((a, b) => a.bankAccount.localeCompare(b.bankAccount));
        });
        this.creditCardService.getCreditCardsByCompanyID().subscribe((res: HttpResponse<ICreditCard[]>) => {
            this.creditcards = res.body.filter(n => n.isActive).sort((a, b) => a.creditCardNumber.localeCompare(b.creditCardNumber));
        });
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
        this.activatedRoute.data.subscribe(({ pPPayVendorBills }) => {
            this.pPPayVendorDetails = pPPayVendorBills;
        });
        for (let i = 0; i < this.pPPayVendorDetails.length; i++) {
            this.pPPayVendorDetails[i].amount = 0;
            this.pPPayVendorDetails[i].amountOriginal = 0;
            this.pPPayVendorDetails[i].differAmount = 0;
            this.pPPayVendorDetails[i].discountRate = 0;
            this.pPPayVendorDetails[i].discountAmount = 0;
            this.pPPayVendorDetails[i].discountAmountOriginal = 0;
        }
        this.pPPayVendorBills = [];
        this.paymentMethod = '0';
        if (this.activatedRoute.snapshot.paramMap.has('id')) {
            this.accountingObjectID = this.activatedRoute.snapshot.paramMap.get('id');
            this.fromDate = this.activatedRoute.snapshot.paramMap.get('fromDate');
            this.toDate = this.activatedRoute.snapshot.paramMap.get('toDate');
        }
        this.accountingObjectService.getAllDTO().subscribe(
            (res: HttpResponse<IAccountingObject[]>) => {
                this.accountingObjects = res.body
                    .filter(x => x.objectType === 0 || x.objectType === 2)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                this.employees = res.body
                    .filter(n => n.isEmployee && n.isActive)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                this.accountingObject = this.accountingObjects.find(x => x.id === this.accountingObjectID);
                this.accountingObjectCode = this.accountingObject.accountingObjectCode;
                this.accountingObjectName = this.accountingObject.accountingObjectName;
                this.accountingObjectAddress = this.accountingObject.accountingObjectAddress;
                this.taxCode = this.accountingObject.taxCode;
                this.receiver = this.accountingObject.contactName;
                this.accountingObjectBankAccountService
                    .getByAccountingObjectById({ accountingObjectID: this.accountingObjectID })
                    .subscribe((res1: HttpResponse<IAccountingObjectBankAccount[]>) => {
                        this.accountingObjectBankAccounts = res1.body;
                    });
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
                this.reason = this.translate.instant('ebwebApp.pPPayVendor.reason');
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
        this.mCPayment = {};
        this.mBTellerPaper = {};
        this.mBCreditCard = {};
        this.checkAll = false;
        this.taxCode = '';
        this.receiver = '';
        this.reason = '';
        this.employeeID = '';
        this.numberAttach = '';
        this.totalAmount = 0;
        this.totalAmountOriginal = 0;
        this.pPPayVendorAccountings = [];
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
        this.details = this.pPPayVendorAccountings;
    }

    saveParent() {
        this.currentRow = null;
    }

    addDataToDetail() {
        this.pPPayVendorAccountings = this.details ? this.details : this.pPPayVendorAccountings;
        this.employeeID = this.nhanVienID ? this.nhanVienID : this.employeeID;
        this.creditCard = this.theTinDung ? this.theTinDung : this.creditCard;
        this.bankAccountDetail = this.taiKhoanNganHang ? this.taiKhoanNganHang : this.bankAccountDetail;
    }

    changePaymentMethod() {
        if (this.paymentMethod === '0') {
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: this.TYPE_GROUP_MC_PAYMENT,
                    displayOnBook: this.typeLedger
                })
                .subscribe((res: HttpResponse<string>) => {
                    this.no = res.body;
                });
            this.typeID = this.TYPE_MC_PAYMENT;
            this.autoPrinciples = this.autoPrinciplesAll.filter(x => x.typeId === TypeID.PHIEU_CHI);
        } else if (this.paymentMethod === '1') {
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: this.TYPE_GROUP_MB_TELLER_PAPER_1,
                    displayOnBook: this.typeLedger
                })
                .subscribe((res: HttpResponse<string>) => {
                    this.no = res.body;
                });
            this.typeID = this.TYPE_MB_TELLER_PAPER_1;
            this.autoPrinciples = this.autoPrinciplesAll.filter(x => x.typeId === TypeID.UY_NHIEM_CHI);
        } else if (this.paymentMethod === '2') {
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: this.TYPE_GROUP_MB_TELLER_PAPER_2,
                    displayOnBook: this.typeLedger
                })
                .subscribe((res: HttpResponse<string>) => {
                    this.no = res.body;
                });
            this.typeID = this.TYPE_MB_TELLER_PAPER_2;
            this.autoPrinciples = this.autoPrinciplesAll.filter(x => x.typeId === TypeID.SEC_CHUYEN_KHOAN);
        } else if (this.paymentMethod === '3') {
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: this.TYPE_GROUP_MB_TELLER_PAPER_3,
                    displayOnBook: this.typeLedger
                })
                .subscribe((res: HttpResponse<string>) => {
                    this.no = res.body;
                });
            this.typeID = this.TYPE_MB_TELLER_PAPER_3;
            this.autoPrinciples = this.autoPrinciplesAll.filter(x => x.typeId === TypeID.SEC_TIEN_MAT);
        } else {
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: this.TYPE_GROUP_MB_CREDIT_CARD,
                    displayOnBook: this.typeLedger
                })
                .subscribe((res: HttpResponse<string>) => {
                    this.no = res.body;
                });
            this.typeID = this.TYPE_MB_CREDIT_CARD;
            this.autoPrinciples = this.autoPrinciplesAll.filter(x => x.typeId === TypeID.THE_TIN_DUNG);
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
            this.receiver = this.accountingObject.contactName;
            this.accountingObjectBankAccountService
                .getByAccountingObjectById({ accountingObjectID: this.accountingObjectID })
                .subscribe((res1: HttpResponse<IAccountingObjectBankAccount[]>) => {
                    this.accountingObjectBankAccounts = res1.body;
                });
            this.accountingObjectService
                .getPPPayVendorBills({
                    fromDate: this.fromDate,
                    toDate: this.toDate,
                    accountObjectID: this.accountingObject.id
                })
                .subscribe((res: HttpResponse<IPPPayVendorBill[]>) => {
                    this.pPPayVendorDetails = res.body.filter(x => x.debitAmountOriginal > 0);
                    this.pPPayVendorBills = this.pPPayVendorDetails.filter(x => x.currencyID === this.currencyID);
                    this.pPPayVendorAccountings = [];
                });
        }
    }

    selectAutoPrinciple() {
        if (this.autoPrinciple !== null && this.autoPrinciple !== undefined) {
            this.reason = this.autoPrinciple.autoPrincipleName;
        }
    }

    selectCurrentcy() {
        if (this.currencyID !== null) {
            this.exchangeRate = this.currencys.find(n => n.currencyCode === this.currencyID).exchangeRate;
            this.phepTinhTyGia = this.currencys.find(n => n.currencyCode === this.currencyID).formula;
            this.pPPayVendorBills = this.pPPayVendorDetails.filter(x => x.currencyID === this.currencyID);
            this.pPPayVendorAccountings = [];
            this.sumDebitAmount = this.pPPayVendorBills.reduce(function(prev, cur) {
                return prev + cur.debitAmount;
            }, 0);
            this.sumDebitAmountOriginal = this.pPPayVendorBills.reduce(function(prev, cur) {
                return prev + cur.debitAmountOriginal;
            }, 0);
            this.changeExchangeRate();
        }
    }

    selectChangeCreditCardNumber() {
        if (this.creditCard) {
            this.creditCardType = this.creditCard.creditCardType;
            this.ownerCard = this.creditCard.ownerCard;
        } else {
            this.creditCardType = '';
            this.ownerCard = '';
        }
    }

    selectedChangedAccountingObjectBankAccount() {
        this.accountingObjectBankName = this.accountingObjectBankAccount.bankName;
    }

    selectedChangedBankAccountDetails() {
        this.bankName = this.bankAccountDetail.bankName;
    }

    submit(question) {
        if (this.pPPayVendorBills.filter(x => x.check).length === 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.pPPayVendor.error.noSelectVoucher'),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
            return;
        }
        if (this.pPPayVendorBills.filter(x => x.check && x.discountRate > 100).length > 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
            return;
        }
        if (this.pPPayVendorBills.filter(x => x.check && x.amountOriginal > x.debitAmountOriginal).length > 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.pPPayVendor.error.checkAmount'),
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
            this.mCPayment = {};
            this.mCPayment.typeID = this.TYPE_MC_PAYMENT;
            this.mCPayment.accountingObjectID = this.accountingObject.id;
            this.mCPayment.employeeID = this.employeeID;
            this.mCPayment.date = this.date;
            this.mCPayment.postedDate = this.postedDate;
            this.mCPayment.typeLedger = Number.parseInt(this.typeLedger);
            if (this.mCPayment.typeLedger === 0) {
                this.mCPayment.noFBook = this.no;
                this.mCPayment.noMBook = null;
            } else if (this.mCPayment.typeLedger === 1) {
                this.mCPayment.noMBook = this.no;
                this.mCPayment.noFBook = null;
            } else {
                if (this.isSoTaiChinh) {
                    this.mCPayment.noFBook = this.no;
                } else {
                    this.mCPayment.noMBook = this.no;
                }
            }
            this.mCPayment.accountingObjectName = this.accountingObjectName;
            this.mCPayment.accountingObjectAddress = this.accountingObjectAddress;
            this.mCPayment.accountingObjectType = 0;
            this.mCPayment.reason = this.reason;
            this.mCPayment.numberAttach = this.numberAttach;
            this.mCPayment.receiver = this.receiver;
            this.mCPayment.currencyID = this.currencyID;
            this.mCPayment.exchangeRate = this.exchangeRate;
            this.mCPayment.totalAmount = this.totalAmount;
            this.mCPayment.totalAmountOriginal = this.totalAmountOriginal;
            this.mCPayment.totalVATAmount = 0;
            this.mCPayment.totalVATAmountOriginal = 0;
            this.mCPayment.recorded = false;
            this.mCPayment.mcpaymentDetailVendors = [];
            this.mCPayment.mcpaymentDetails = [];
            const listPPPayVendorBills0 = this.pPPayVendorBills.filter(x => x.check);
            for (let i = 0; i < listPPPayVendorBills0.length; i++) {
                const mCPaymentDetailVendor: IMCPaymentDetailVendor = {};
                mCPaymentDetailVendor.voucherTypeID = listPPPayVendorBills0[i].typeID;
                mCPaymentDetailVendor.debitAccount = listPPPayVendorBills0[i].account;
                mCPaymentDetailVendor.dueDate = listPPPayVendorBills0[i].dueDate;
                mCPaymentDetailVendor.payableAmount = listPPPayVendorBills0[i].totalDebit;
                mCPaymentDetailVendor.payableAmountOriginal = listPPPayVendorBills0[i].totalDebitOriginal;
                mCPaymentDetailVendor.remainingAmount = listPPPayVendorBills0[i].debitAmount;
                mCPaymentDetailVendor.remainingAmountOriginal = listPPPayVendorBills0[i].debitAmountOriginal;
                mCPaymentDetailVendor.amount = listPPPayVendorBills0[i].amount;
                mCPaymentDetailVendor.amountOriginal = listPPPayVendorBills0[i].amountOriginal;
                mCPaymentDetailVendor.discountRate = listPPPayVendorBills0[i].discountRate;
                mCPaymentDetailVendor.discountAccount = listPPPayVendorBills0[i].discountAccount;
                mCPaymentDetailVendor.differAmount = listPPPayVendorBills0[i].differAmount;
                mCPaymentDetailVendor.discountAmount = listPPPayVendorBills0[i].discountAmount;
                mCPaymentDetailVendor.discountAmountOriginal = listPPPayVendorBills0[i].discountAmountOriginal;
                mCPaymentDetailVendor.accountingObjectID = this.mCPayment.accountingObjectID;
                mCPaymentDetailVendor.pPInvoiceID = listPPPayVendorBills0[i].referenceID;
                mCPaymentDetailVendor.refVoucherExchangeRate = listPPPayVendorBills0[i].refVoucherExchangeRate;
                mCPaymentDetailVendor.differAmount = listPPPayVendorBills0[i].differAmount;
                mCPaymentDetailVendor.lastExchangeRate = listPPPayVendorBills0[i].lastExchangeRate;
                this.mCPayment.mcpaymentDetailVendors.push(mCPaymentDetailVendor);
            }
            for (let i = 0; i < this.pPPayVendorAccountings.length; i++) {
                const mCPaymentDetail: IMCPaymentDetails = {};
                mCPaymentDetail.description = this.pPPayVendorAccountings[i].description;
                mCPaymentDetail.debitAccount = this.pPPayVendorAccountings[i].debitAccount;
                mCPaymentDetail.creditAccount = this.pPPayVendorAccountings[i].creditAccount;
                mCPaymentDetail.amount = this.pPPayVendorAccountings[i].totalAmount;
                mCPaymentDetail.amountOriginal = this.pPPayVendorAccountings[i].totalAmountOriginal;
                mCPaymentDetail.budgetItem = this.pPPayVendorAccountings[i].budgetItem;
                mCPaymentDetail.costSet = this.pPPayVendorAccountings[i].costSet;
                mCPaymentDetail.contractID = this.pPPayVendorAccountings[i].emContract;
                mCPaymentDetail.statisticsCode = this.pPPayVendorAccountings[i].statisticsCode;
                mCPaymentDetail.expenseItem = this.pPPayVendorAccountings[i].expenseItem;
                mCPaymentDetail.organizationUnit = this.pPPayVendorAccountings[i].department;
                mCPaymentDetail.accountingObject = this.accountingObject;
                this.mCPayment.mcpaymentDetails.push(mCPaymentDetail);
            }
            this.subscribeToSaveMCPaymentResponse(this.mCPaymentService.create(this.mCPayment), question);
        } else if (this.paymentMethod === '4') {
            if (!this.creditCard) {
                this.toastr.error(this.translate.instant('ebwebApp.mBTellerPaper.error.bankAccountNotNullMessage'));
                return;
            }
            this.mBCreditCard = {};
            this.mBCreditCard.typeID = this.TYPE_MB_CREDIT_CARD;
            this.mBCreditCard.accountingObjectID = this.accountingObject.id;
            this.mBCreditCard.employeeID = this.employeeID;
            this.mBCreditCard.date = this.date;
            this.mBCreditCard.postedDate = this.postedDate;
            this.mBCreditCard.typeLedger = Number.parseInt(this.typeLedger);
            if (this.mBCreditCard.typeLedger === 0) {
                this.mBCreditCard.noFBook = this.no;
                this.mBCreditCard.noMBook = null;
            } else if (this.mBCreditCard.typeLedger === 1) {
                this.mBCreditCard.noMBook = this.no;
                this.mBCreditCard.noFBook = null;
            } else {
                if (this.isSoTaiChinh) {
                    this.mBCreditCard.noFBook = this.no;
                } else {
                    this.mBCreditCard.noMBook = this.no;
                }
            }
            this.mBCreditCard.accountingObjectName = this.accountingObjectName;
            this.mBCreditCard.accountingObjectAddress = this.accountingObjectAddress;
            if (this.accountingObjectBankAccount) {
                this.mBCreditCard.accountingObjectBankAccountDetailID = this.accountingObjectBankAccount.id;
                this.mBCreditCard.accountingObjectBankName = this.accountingObjectBankAccount.bankName;
            }
            this.mBCreditCard.creditCardNumber = this.creditCard.creditCardNumber;
            this.mBCreditCard.creditCardID = this.creditCard.id;
            this.mBCreditCard.accountingObjectType = 0;
            this.mBCreditCard.reason = this.reason;
            this.mBCreditCard.currencyID = this.currencyID;
            this.mBCreditCard.exchangeRate = this.exchangeRate;
            this.mBCreditCard.totalAmount = this.totalAmount;
            this.mBCreditCard.totalAmountOriginal = this.totalAmountOriginal;
            this.mBCreditCard.totalVATAmount = 0;
            this.mBCreditCard.totalVATAmountOriginal = 0;
            this.mBCreditCard.recorded = false;
            this.mBCreditCard.mBCreditCardDetailVendor = [];
            this.mBCreditCard.mBCreditCardDetails = [];
            const listPPPayVendorBills1 = this.pPPayVendorBills.filter(x => x.check);
            for (let i = 0; i < listPPPayVendorBills1.length; i++) {
                const mBCreditCardDetailVendor: IMBCreditCardDetailVendor = {};
                mBCreditCardDetailVendor.voucherTypeID = listPPPayVendorBills1[i].typeID;
                mBCreditCardDetailVendor.debitAccount = listPPPayVendorBills1[i].account;
                mBCreditCardDetailVendor.dueDate = listPPPayVendorBills1[i].dueDate;
                mBCreditCardDetailVendor.payableAmount = listPPPayVendorBills1[i].totalDebit;
                mBCreditCardDetailVendor.payableAmountOriginal = listPPPayVendorBills1[i].totalDebitOriginal;
                mBCreditCardDetailVendor.remainingAmount = listPPPayVendorBills1[i].debitAmount;
                mBCreditCardDetailVendor.remainingAmountOriginal = listPPPayVendorBills1[i].debitAmountOriginal;
                mBCreditCardDetailVendor.amount = listPPPayVendorBills1[i].amount;
                mBCreditCardDetailVendor.amountOriginal = listPPPayVendorBills1[i].amountOriginal;
                mBCreditCardDetailVendor.discountRate = listPPPayVendorBills1[i].discountRate;
                mBCreditCardDetailVendor.discountAccount = listPPPayVendorBills1[i].discountAccount;
                mBCreditCardDetailVendor.differAmount = listPPPayVendorBills1[i].differAmount;
                mBCreditCardDetailVendor.discountAmount = listPPPayVendorBills1[i].discountAmount;
                mBCreditCardDetailVendor.discountAmountOriginal = listPPPayVendorBills1[i].discountAmountOriginal;
                mBCreditCardDetailVendor.accountingObjectID = this.accountingObject.id;
                mBCreditCardDetailVendor.pPInvoiceID = listPPPayVendorBills1[i].referenceID;
                mBCreditCardDetailVendor.refVoucherExchangeRate = listPPPayVendorBills1[i].refVoucherExchangeRate;
                mBCreditCardDetailVendor.differAmount = listPPPayVendorBills1[i].differAmount;
                mBCreditCardDetailVendor.lastExchangeRate = listPPPayVendorBills1[i].lastExchangeRate;
                this.mBCreditCard.mBCreditCardDetailVendor.push(mBCreditCardDetailVendor);
            }
            for (let i = 0; i < this.pPPayVendorAccountings.length; i++) {
                const mBCreditCardDetail: IMBCreditCardDetails = {};
                mBCreditCardDetail.description = this.pPPayVendorAccountings[i].description;
                mBCreditCardDetail.debitAccount = this.pPPayVendorAccountings[i].debitAccount;
                mBCreditCardDetail.creditAccount = this.pPPayVendorAccountings[i].creditAccount;
                mBCreditCardDetail.amount = this.pPPayVendorAccountings[i].totalAmount;
                mBCreditCardDetail.amountOriginal = this.pPPayVendorAccountings[i].totalAmountOriginal;
                if (this.pPPayVendorAccountings[i].budgetItem) {
                    mBCreditCardDetail.budgetItemID = this.pPPayVendorAccountings[i].budgetItem.id;
                }
                if (this.pPPayVendorAccountings[i].costSet) {
                    mBCreditCardDetail.costSetID = this.pPPayVendorAccountings[i].costSet.id;
                }
                if (this.pPPayVendorAccountings[i].emContract) {
                    mBCreditCardDetail.contractID = this.pPPayVendorAccountings[i].emContract.id;
                }
                if (this.pPPayVendorAccountings[i].statisticsCode) {
                    mBCreditCardDetail.statisticsCodeID = this.pPPayVendorAccountings[i].statisticsCode.id;
                }
                if (this.pPPayVendorAccountings[i].expenseItem) {
                    mBCreditCardDetail.expenseItemID = this.pPPayVendorAccountings[i].expenseItem.id;
                }
                if (this.pPPayVendorAccountings[i].department) {
                    mBCreditCardDetail.departmentID = this.pPPayVendorAccountings[i].department.id;
                }
                mBCreditCardDetail.accountingObjectID = this.accountingObject.id;
                this.mBCreditCard.mBCreditCardDetails.push(mBCreditCardDetail);
            }
            this.subscribeToSaveMBCreditCardResponse(this.mBCreditCardService.create(this.mBCreditCard), question);
        } else {
            if (!this.bankAccountDetail) {
                this.toastr.error(this.translate.instant('ebwebApp.mBTellerPaper.error.bankAccountNotNullMessage'));
                return;
            }
            this.mBTellerPaper = {};
            if (this.paymentMethod === '1') {
                this.mBTellerPaper.typeId = this.TYPE_MB_TELLER_PAPER_1;
            } else if (this.paymentMethod === '2') {
                this.mBTellerPaper.typeId = this.TYPE_MB_TELLER_PAPER_2;
            } else {
                this.mBTellerPaper.typeId = this.TYPE_MB_TELLER_PAPER_3;
            }
            this.mBTellerPaper.accountingObjectID = this.accountingObject.id;
            this.mBTellerPaper.employeeID = this.employeeID;
            this.mBTellerPaper.date = this.date;
            this.mBTellerPaper.postedDate = this.postedDate;
            this.mBTellerPaper.typeLedger = Number.parseInt(this.typeLedger);
            if (this.mBTellerPaper.typeLedger === 0) {
                this.mBTellerPaper.noFBook = this.no;
                this.mBTellerPaper.noMBook = null;
            } else if (this.mBTellerPaper.typeLedger === 1) {
                this.mBTellerPaper.noMBook = this.no;
                this.mBTellerPaper.noFBook = null;
            } else {
                if (this.isSoTaiChinh) {
                    this.mBTellerPaper.noFBook = this.no;
                } else {
                    this.mBTellerPaper.noMBook = this.no;
                }
            }
            this.mBTellerPaper.accountingObjectName = this.accountingObjectName;
            this.mBTellerPaper.accountingObjectAddress = this.accountingObjectAddress;
            if (this.accountingObjectBankAccount) {
                this.mBTellerPaper.accountingObjectBankAccount = this.accountingObjectBankAccount.id;
                this.mBTellerPaper.accountingObjectBankName = this.accountingObjectBankAccount.bankName;
            }
            if (this.bankAccountDetail) {
                this.mBTellerPaper.bankAccountDetailID = this.bankAccountDetail.id;
                this.mBTellerPaper.bankName = this.bankAccountDetail.bankName;
            }
            this.mBTellerPaper.accountingObjectType = 0;
            this.mBTellerPaper.reason = this.reason;
            this.mBTellerPaper.currencyId = this.currencyID;
            this.mBTellerPaper.exchangeRate = this.exchangeRate;
            this.mBTellerPaper.totalAmount = this.totalAmount;
            this.mBTellerPaper.totalAmountOriginal = this.totalAmountOriginal;
            this.mBTellerPaper.totalVatAmount = 0;
            this.mBTellerPaper.totalVatAmountOriginal = 0;
            this.mBTellerPaper.recorded = false;
            this.mBTellerPaper.mBTellerPaperDetailVendor = [];
            this.mBTellerPaper.mBTellerPaperDetails = [];
            const listPPPayVendorBills2 = this.pPPayVendorBills.filter(x => x.check);
            for (let i = 0; i < listPPPayVendorBills2.length; i++) {
                const mBTellerPaperDetailVendor: IMBTellerPaperDetailVendor = {};
                mBTellerPaperDetailVendor.voucherTypeID = listPPPayVendorBills2[i].typeID;
                mBTellerPaperDetailVendor.debitAccount = listPPPayVendorBills2[i].account;
                mBTellerPaperDetailVendor.dueDate = listPPPayVendorBills2[i].dueDate;
                mBTellerPaperDetailVendor.payableAmount = listPPPayVendorBills2[i].totalDebit;
                mBTellerPaperDetailVendor.payableAmountOriginal = listPPPayVendorBills2[i].totalDebitOriginal;
                mBTellerPaperDetailVendor.remainingAmount = listPPPayVendorBills2[i].debitAmount;
                mBTellerPaperDetailVendor.remainingAmountOriginal = listPPPayVendorBills2[i].debitAmountOriginal;
                mBTellerPaperDetailVendor.amount = listPPPayVendorBills2[i].amount;
                mBTellerPaperDetailVendor.amountOriginal = listPPPayVendorBills2[i].amountOriginal;
                mBTellerPaperDetailVendor.discountRate = listPPPayVendorBills2[i].discountRate;
                mBTellerPaperDetailVendor.discountAccount = listPPPayVendorBills2[i].discountAccount;
                mBTellerPaperDetailVendor.differAmount = listPPPayVendorBills2[i].differAmount;
                mBTellerPaperDetailVendor.discountAmount = listPPPayVendorBills2[i].discountAmount;
                mBTellerPaperDetailVendor.discountAmountOriginal = listPPPayVendorBills2[i].discountAmountOriginal;
                mBTellerPaperDetailVendor.accountingObjectID = this.accountingObject.id;
                mBTellerPaperDetailVendor.pPInvoiceID = listPPPayVendorBills2[i].referenceID;
                mBTellerPaperDetailVendor.refVoucherExchangeRate = listPPPayVendorBills2[i].refVoucherExchangeRate;
                mBTellerPaperDetailVendor.differAmount = listPPPayVendorBills2[i].differAmount;
                mBTellerPaperDetailVendor.lastExchangeRate = listPPPayVendorBills2[i].lastExchangeRate;
                this.mBTellerPaper.mBTellerPaperDetailVendor.push(mBTellerPaperDetailVendor);
            }
            for (let i = 0; i < this.pPPayVendorAccountings.length; i++) {
                const mBTellerPaperDetails: IMBTellerPaperDetails = {};
                mBTellerPaperDetails.description = this.pPPayVendorAccountings[i].description;
                mBTellerPaperDetails.debitAccount = this.pPPayVendorAccountings[i].debitAccount;
                mBTellerPaperDetails.creditAccount = this.pPPayVendorAccountings[i].creditAccount;
                mBTellerPaperDetails.amount = this.pPPayVendorAccountings[i].totalAmount;
                mBTellerPaperDetails.amountOriginal = this.pPPayVendorAccountings[i].totalAmountOriginal;
                mBTellerPaperDetails.accountingObjectID = this.accountingObject.id;
                if (this.pPPayVendorAccountings[i].budgetItem) {
                    mBTellerPaperDetails.budgetItemID = this.pPPayVendorAccountings[i].budgetItem.id;
                }
                if (this.pPPayVendorAccountings[i].costSet) {
                    mBTellerPaperDetails.costSetID = this.pPPayVendorAccountings[i].costSet.id;
                }
                if (this.pPPayVendorAccountings[i].emContract) {
                    mBTellerPaperDetails.eMContractID = this.pPPayVendorAccountings[i].emContract.id;
                }
                if (this.pPPayVendorAccountings[i].statisticsCode) {
                    mBTellerPaperDetails.statisticsCodeID = this.pPPayVendorAccountings[i].statisticsCode.id;
                }
                if (this.pPPayVendorAccountings[i].expenseItem) {
                    mBTellerPaperDetails.expenseItemID = this.pPPayVendorAccountings[i].expenseItem.id;
                }
                if (this.pPPayVendorAccountings[i].department) {
                    mBTellerPaperDetails.departmentID = this.pPPayVendorAccountings[i].department.id;
                }
                this.mBTellerPaper.mBTellerPaperDetails.push(mBTellerPaperDetails);
            }
            this.subscribeToSaveMBTellerPaperResponse(this.mBTellerPaperService.create(this.mBTellerPaper), question);
        }
    }

    private subscribeToSaveMCPaymentResponse(result: Observable<HttpResponse<any>>, question) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.mCPayment.id = res.body.mCPayment.id;
                    this.mCPayment.recorded = res.body.mCPayment.recorded;
                    this.viewNo = res.body.mCPayment.noFBook ? res.body.mCPayment.noMBook : res.body.mCPayment.noFBook;
                    this.modalRef = this.modalService.open(question, { backdrop: 'static' });
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                } else if (res.body.status === 2) {
                    if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyQT'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyTC'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuy'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else {
                        this.recordFailed();
                    }
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private subscribeToSaveMBCreditCardResponse(result: Observable<HttpResponse<any>>, question) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.mBCreditCard.id = res.body.mbCreditCard.id;
                    this.mBCreditCard.recorded = res.body.mbCreditCard.recorded;
                    this.viewNo = res.body.mbCreditCard.noFBook ? res.body.mbCreditCard.noMBook : res.body.mbCreditCard.noFBook;
                    this.modalRef = this.modalService.open(question, { backdrop: 'static' });
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                } else if (res.body.status === 2) {
                    if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyQT'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyTC'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuy'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else {
                        this.recordFailed();
                    }
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private subscribeToSaveMBTellerPaperResponse(result: Observable<HttpResponse<any>>, question) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.mBTellerPaper.id = res.body.mBTellerPaper.id;
                    this.mBTellerPaper.recorded = res.body.mBTellerPaper.recorded;
                    this.viewNo = res.body.mBTellerPaper.noFBook ? res.body.mBTellerPaper.noMBook : res.body.mBTellerPaper.noFBook;
                    this.modalRef = this.modalService.open(question, { backdrop: 'static' });
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                } else if (res.body.status === 2) {
                    if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyQT'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyTC'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuy'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else {
                        this.recordFailed();
                    }
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

    viewVoucher(pPPayVendorBill: IPPPayVendorBill) {
        let url = '';
        if (pPPayVendorBill.typeID === this.TYPE_PP_INVOICE) {
            this.viewVoucherService.checkViaStockPPInvoice({ id: pPPayVendorBill.referenceID }).subscribe(
                (res: HttpResponse<any>) => {
                    if (res.body.message === UpdateDataMessages.STOCK_TRUE) {
                        url = `/#/mua-hang/qua-kho-ref/${pPPayVendorBill.referenceID}/edit/1`;
                        window.open(url, '_blank');
                    } else if (res.body.message === UpdateDataMessages.STOCK_FALSE) {
                        url = `/#/mua-hang/khong-qua-kho-ref/${pPPayVendorBill.referenceID}/edit/1`;
                        window.open(url, '_blank');
                    } else {
                        this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                    }
                    return;
                },
                (res: HttpErrorResponse) => {
                    this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                }
            );
        } else if (pPPayVendorBill.typeID === this.TYPE_PP_SERVICE) {
            url = `/#/mua-dich-vu/${pPPayVendorBill.referenceID}/edit/from-ref`;
        } else if (pPPayVendorBill.typeID === this.TYPE_PP_DISCOUNT_RETURN) {
            url = `/#/hang-mua/giam-gia/${pPPayVendorBill.referenceID}/view`;
        } else if (pPPayVendorBill.typeID === this.TYPE_FA_INCREAMENT) {
            url = `/#/fa-increament/${pPPayVendorBill.referenceID}/edit/from-ref`;
        } else if (pPPayVendorBill.typeID === this.TYPE_TI_INCREAMENT) {
            url = `/#/ti-increament/${pPPayVendorBill.referenceID}/edit/from-ref`;
        } else if (pPPayVendorBill.typeID === TypeID.CHUNG_TU_NGHIEP_VU_KHAC) {
            url = `/#/g-other-voucher/${pPPayVendorBill.referenceID}/edit/from-ref`;
        }
        if (url) {
            window.open(url, '_blank');
        }
    }

    calculateAmont(pPPayVendorBill: IPPPayVendorBill) {
        if (pPPayVendorBill.amountOriginal > pPPayVendorBill.debitAmountOriginal) {
            this.toastr.warning(
                this.translate.instant('ebwebApp.pPPayVendor.error.checkAmount'),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
        }
        pPPayVendorBill.amount = this.utilsService.round(
            this.phepTinhTyGia === '*'
                ? pPPayVendorBill.amountOriginal * this.exchangeRate
                : pPPayVendorBill.amountOriginal / this.exchangeRate,
            this.account.systemOption,
            7
        );
        if (pPPayVendorBill.amountOriginal > 0) {
            pPPayVendorBill.check = true;
        }
        this.calculateDiscount(pPPayVendorBill);
    }

    calculateDiscount(pPPayVendorBill: IPPPayVendorBill) {
        if (pPPayVendorBill.discountRate > 100) {
            this.toastr.warning(
                this.translate.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
        } else {
            if (!pPPayVendorBill.discountRate) {
                pPPayVendorBill.discountRate = 0;
            }
            if (!pPPayVendorBill.amountOriginal) {
                pPPayVendorBill.amountOriginal = 0;
            }
            pPPayVendorBill.discountAmount = pPPayVendorBill.amount * pPPayVendorBill.discountRate / 100;
            pPPayVendorBill.discountAmountOriginal = pPPayVendorBill.amountOriginal * pPPayVendorBill.discountRate / 100;
            this.calculateTotal();
            this.genAccountingByDiscounts();
        }
    }

    changeExchangeRate() {
        for (let i = 0; i < this.pPPayVendorBills.length; i++) {
            this.pPPayVendorBills[i].amount = this.utilsService.round(
                this.phepTinhTyGia === '*'
                    ? this.pPPayVendorBills[i].amountOriginal * this.exchangeRate
                    : this.pPPayVendorBills[i].amountOriginal / this.exchangeRate,
                this.account.systemOption,
                7
            );
            this.pPPayVendorBills[i].discountAmount = this.utilsService.round(
                this.phepTinhTyGia === '*'
                    ? this.pPPayVendorBills[i].discountAmountOriginal * this.exchangeRate
                    : this.pPPayVendorBills[i].discountAmountOriginal / this.exchangeRate,
                this.account.systemOption,
                7
            );
        }
        this.calculateTotal();
    }

    changeDiscountAmount(pPPayVendorBill: IPPPayVendorBill) {
        if (pPPayVendorBill.discountAmountOriginal) {
            pPPayVendorBill.discountRate = pPPayVendorBill.discountAmountOriginal * 100 / pPPayVendorBill.amountOriginal;
            pPPayVendorBill.discountAmount = this.utilsService.round(
                this.phepTinhTyGia === '*'
                    ? pPPayVendorBill.discountAmountOriginal * this.exchangeRate
                    : pPPayVendorBill.discountAmountOriginal / this.exchangeRate,
                this.account.systemOption,
                7
            );
            this.calculateTotal();
        }
    }

    calculateTotal() {
        const amount = this.pPPayVendorBills.filter(x => x.check).reduce(function(prev, cur) {
            return prev + cur.amount;
        }, 0);
        const amountOriginal = this.pPPayVendorBills.filter(x => x.check).reduce(function(prev, cur) {
            return prev + cur.amountOriginal;
        }, 0);
        const discountAmount = this.pPPayVendorBills.filter(x => x.check).reduce(function(prev, cur) {
            return prev + cur.discountAmount;
        }, 0);
        const discountAmountOriginal = this.pPPayVendorBills.filter(x => x.check).reduce(function(prev, cur) {
            return prev + cur.discountAmountOriginal;
        }, 0);
        this.totalAmount = amount - discountAmount;
        this.totalAmountOriginal = amountOriginal - discountAmountOriginal;
        this.sumAmountBills = 0;
        this.sumAmountOriginalBills = 0;
        this.sumDiffAmount = 0;
        this.sumDiscountAmount = 0;
        this.sumDiscountAmountOriginal = 0;
        this.pPPayVendorBills.forEach((item: IPPPayVendorBill) => {
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

    genAccountings(pPPayVendorBill: IPPPayVendorBill) {
        if (pPPayVendorBill.check) {
            pPPayVendorBill.amount = pPPayVendorBill.debitAmount;
            pPPayVendorBill.amountOriginal = pPPayVendorBill.debitAmountOriginal;
            pPPayVendorBill.discountRate = 0;
        } else {
            pPPayVendorBill.amount = 0;
            pPPayVendorBill.amountOriginal = 0;
            pPPayVendorBill.discountRate = 0;
        }
        this.calculateDiscount(pPPayVendorBill);
    }

    checkAllFunction() {
        if (this.pPPayVendorBills) {
            this.checkAll = !this.checkAll;
            for (let i = 0; i < this.pPPayVendorBills.length; i++) {
                this.pPPayVendorBills[i].check = this.checkAll;
                if (this.checkAll) {
                    this.pPPayVendorBills[i].amount = this.pPPayVendorBills[i].debitAmount;
                    this.pPPayVendorBills[i].amountOriginal = this.pPPayVendorBills[i].debitAmountOriginal;
                    this.pPPayVendorBills[i].discountRate = 0;
                } else {
                    this.pPPayVendorBills[i].amount = 0;
                    this.pPPayVendorBills[i].amountOriginal = 0;
                    this.pPPayVendorBills[i].discountRate = 0;
                }
                this.calculateDiscount(this.pPPayVendorBills[i]);
            }
        }
    }

    genAccountingByDiscounts() {
        this.pPPayVendorAccountings = [];
        const group = this.pPPayVendorBills.filter(x => x.check).reduce((g: any, ppPayVendorBill: IPPPayVendorBill) => {
            g[ppPayVendorBill.account] = g[ppPayVendorBill.account] || [];
            g[ppPayVendorBill.account].push(ppPayVendorBill);
            return g;
        }, {});
        const groupData = Object.keys(group).map(a => {
            return {
                account: a,
                ppPayVendorBills: group[a]
            };
        });
        for (let i = 0; i < groupData.length; i++) {
            const pPPayVendorAccounting: IPPPayVendorAccounting = {};
            pPPayVendorAccounting.debitAccount = groupData[i].account;
            pPPayVendorAccounting.creditAccount =
                this.paymentMethod === '0' ? (this.currencyID === 'VND' ? '1111' : '1112') : this.currencyID === 'VND' ? '1121' : '1122';
            pPPayVendorAccounting.accountingObjectCode = this.accountingObjectCode;
            pPPayVendorAccounting.description = 'Trả tiền nhà cung cấp ' + this.accountingObjectName;
            pPPayVendorAccounting.totalAmount = groupData[i].ppPayVendorBills.reduce(function(prev, cur) {
                return prev + cur.amount - cur.discountAmount;
            }, 0);
            pPPayVendorAccounting.totalAmountOriginal = groupData[i].ppPayVendorBills.reduce(function(prev, cur) {
                return prev + cur.amountOriginal - cur.discountAmountOriginal;
            }, 0);
            this.pPPayVendorAccountings.push(pPPayVendorAccounting);
            const ppPayVendorBillDiscounts: IPPPayVendorBill[] = groupData[i].ppPayVendorBills;
            const groupDiscount = ppPayVendorBillDiscounts
                .filter(x => x.discountAccount !== null && x.discountAccount !== undefined)
                .reduce((g: any, ppPayVendorBillDis: IPPPayVendorBill) => {
                    g[ppPayVendorBillDis.discountAccount] = g[ppPayVendorBillDis.discountAccount] || [];
                    g[ppPayVendorBillDis.discountAccount].push(ppPayVendorBillDis);
                    return g;
                }, {});
            const groupDataDiscount = Object.keys(groupDiscount).map(d => {
                return {
                    discountAccount: d,
                    ppPayVendorBillDiscounts: groupDiscount[d]
                };
            });
            for (let j = 0; j < groupDataDiscount.length; j++) {
                const pPPayVendorAccountingDis: IPPPayVendorAccounting = {};
                pPPayVendorAccountingDis.debitAccount = groupData[i].account;
                pPPayVendorAccountingDis.creditAccount = groupDataDiscount[j].discountAccount;
                pPPayVendorAccountingDis.accountingObjectCode = this.accountingObjectCode;
                pPPayVendorAccountingDis.description = 'Chiết khấu thanh toán được hưởng';
                pPPayVendorAccountingDis.totalAmount = groupDataDiscount[j].ppPayVendorBillDiscounts.reduce(function(prev, cur) {
                    return prev + cur.discountAmount;
                }, 0);
                pPPayVendorAccountingDis.totalAmountOriginal = groupDataDiscount[j].ppPayVendorBillDiscounts.reduce(function(prev, cur) {
                    return prev + cur.discountAmountOriginal;
                }, 0);
                this.pPPayVendorAccountings.push(pPPayVendorAccountingDis);
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
            this.router.navigate(['./mc-payment', this.mCPayment.id, 'edit']);
        } else if (this.paymentMethod === '4') {
            this.router.navigate(['./mb-credit-card', this.mBCreditCard.id, 'edit']);
        } else {
            this.router.navigate(['./mb-teller-paper', this.mBTellerPaper.id, 'edit']);
        }
    }

    closeQuestion() {
        if (this.modalRef) {
            this.modalRef.close();
            this.accountingObjectService
                .getPPPayVendorBills({
                    fromDate: this.fromDate != null ? this.fromDate : null,
                    toDate: this.date != null ? this.date.format('YYYY/MM/DD') : null,
                    accountObjectID: this.accountingObjectID
                })
                .subscribe((res: HttpResponse<IPPPayVendorBill[]>) => {
                    this.pPPayVendorDetails = res.body.filter(x => x.debitAmountOriginal > 0);
                    this.pPPayVendorBills = this.pPPayVendorDetails.filter(x => x.currencyID === this.currencyID);
                    this.pPPayVendorAccountings = [];
                    this.changePaymentMethod();
                    this.calculateTotal();
                });
        }
    }

    checkPostedDateGreaterDate() {
        this.postedDate = this.date;
        this.accountingObjectService
            .getPPPayVendorBills({
                fromDate: this.fromDate != null ? this.fromDate : null,
                toDate: this.date != null ? this.date.format('YYYY/MM/DD') : null,
                accountObjectID: this.accountingObjectID
            })
            .subscribe((res: HttpResponse<IPPPayVendorBill[]>) => {
                this.pPPayVendorDetails = res.body.filter(x => x.debitAmountOriginal > 0);
                this.pPPayVendorBills = this.pPPayVendorDetails.filter(x => x.currencyID === this.currencyID);
                this.pPPayVendorAccountings = [];
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
            if (this.pPPayVendorAccountings && this.pPPayVendorAccountings.length > 0) {
                this.pPPayVendorAccountings.forEach(item => {
                    item.creditAccount = this.creditAccountList[0].accountNumber;
                });
            }
        });
    }

    addNewRow(isRightClick?) {
        let length = 0;
        if (isRightClick) {
            this.pPPayVendorAccountings.splice(this.indexFocusDetailRow + 1, 0, {});
            length = this.indexFocusDetailRow + 2;
        } else {
            this.pPPayVendorAccountings.push({});
            length = this.pPPayVendorAccountings.length;
        }
        this.pPPayVendorAccountings[
            this.pPPayVendorAccountings.length - 1
        ].accountingObjectCode = this.accountingObject.accountingObjectCode;
        if (this.pPPayVendorAccountings.length > 0) {
            this.pPPayVendorAccountings[this.pPPayVendorAccountings.length - 1].accountingObjectCode = this.pPPayVendorAccountings[
                this.pPPayVendorAccountings.length - 2
            ].accountingObjectCode;
            this.pPPayVendorAccountings[this.pPPayVendorAccountings.length - 1].description = this.pPPayVendorAccountings[
                this.pPPayVendorAccountings.length - 2
            ].description;
            this.pPPayVendorAccountings[this.pPPayVendorAccountings.length - 1].creditAccount = this.pPPayVendorAccountings[
                this.pPPayVendorAccountings.length - 2
            ].creditAccount;
            this.pPPayVendorAccountings[this.pPPayVendorAccountings.length - 1].debitAccount = this.pPPayVendorAccountings[
                this.pPPayVendorAccountings.length - 2
            ].debitAccount;
        }
        this.pPPayVendorAccountings[this.pPPayVendorAccountings.length - 1].totalAmount = 0;
        this.pPPayVendorAccountings[this.pPPayVendorAccountings.length - 1].totalAmountOriginal = 0;
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
            const idx: number = this.pPPayVendorAccountings.length - 1;
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
        this.pPPayVendorAccountings.splice(this.pPPayVendorAccountings.indexOf(detail), 1);
        if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
            // vì còn trường hợp = 0
            if (this.pPPayVendorAccountings.length > 0) {
                let row = 0;
                if (this.indexFocusDetailRow > this.pPPayVendorAccountings.length - 1) {
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

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    addIfLastInput(i) {
        if (i === this.pPPayVendorAccountings.length - 1) {
            this.addNewRow();
        }
    }

    copyRow(detail, isRigthClick?) {
        if (!this.getSelectionText() || isRigthClick) {
            const detailCopy: any = Object.assign({}, detail);
            detailCopy.id = null;
            this.pPPayVendorAccountings.push(detailCopy);
            if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const id = this.idIndex;
                const row = this.pPPayVendorAccountings.length - 1;
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
}
