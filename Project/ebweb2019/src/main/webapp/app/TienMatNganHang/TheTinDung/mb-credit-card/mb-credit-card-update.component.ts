import { AfterViewChecked, AfterViewInit, Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { IMBCreditCard } from 'app/shared/model/mb-credit-card.model';
import { MBCreditCardService } from './mb-credit-card.service';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { BankService } from 'app/danhmuc/bank';
import { IBank } from 'app/shared/model/bank.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { Currency, ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IMBCreditCardDetails } from 'app/shared/model/mb-credit-card-details.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { CostSetService } from 'app/entities/cost-set';
import { ExpenseItemService } from 'app/entities/expense-item';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { IMBCreditCardDetailTax } from 'app/shared/model/mb-credit-card-detail-tax.model';
import { ICreditCard } from 'app/shared/model/credit-card.model';
import { CreditCardService } from 'app/danhmuc/credit-card';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { IAccountDefault } from 'app/shared/model/account-default.model';
import { AccountDefaultService } from 'app/danhmuc/account-default';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { EMContractService } from 'app/entities/em-contract';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { BudgetItemService } from 'app/entities/budget-item';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import {
    AccountType,
    CategoryName,
    DDSo_NCachHangDVi,
    DDSo_NCachHangNghin,
    DDSo_NgoaiTe,
    DDSo_TienVND,
    MSGERROR,
    NGAY_HACH_TOAN,
    SO_LAM_VIEC,
    TCKHAC_GhiSo,
    TCKHAC_SDSoQuanTri,
    TypeID
} from 'app/app.constants';
import { Principal } from 'app/core';
import * as moment from 'moment';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { AccountingObjectBankAccountService } from 'app/entities/accounting-object-bank-account';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IAccountAllList } from 'app/shared/model/account-all-list.model';
import { IMBDepositDetails } from 'app/shared/model/mb-deposit-details.model';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { IMBTellerPaperDetailTax } from 'app/shared/model/mb-teller-paper-detail-tax.model';
import { DATE_FORMAT } from 'app/shared';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { IMBCreditCardDetailVendor } from 'app/shared/model/mb-credit-card-detail-vendor.model';
import { IMCPaymentDetailVendor } from 'app/shared/model/mc-payment-detail-vendor.model';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { ROLE } from 'app/role.constants';
import { IMCPayment } from 'app/shared/model/mc-payment.model';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { IType } from 'app/shared/model/type.model';
import { InvoiceType } from 'app/shared/model/invoice-type.model';
import { IAReportService } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/ia-report.service';

@Component({
    selector: 'eb-mb-credit-card-update',
    templateUrl: './mb-credit-card-update.component.html',
    styleUrls: ['./mb-credit-card-update.component.css']
})
export class MBCreditCardUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewInit, AfterViewChecked {
    @ViewChild('test') public modalComponent: NgbModalRef;
    @ViewChild('content') content: TemplateRef<any>;
    @ViewChild('contentAmount') public modalComponentAmount: NgbModalRef;
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    private _mBCreditCard: IMBCreditCard;
    private _accountingObject: IAccountingObject;
    TYPE_MB_CREDIT_CARD = 170;
    TYPE_MB_CREDIT_CARD_MUA_HANG = 171;
    TYPE_MB_CREDIT_CARD_MUA_TSCD = 172;
    TYPE_MB_CREDIT_CARD_MUA_DV = 173;
    TYPE_MB_CREDIT_CARD_NCC = 174;
    TYPE_MB_CREDIT_CARD_MUA_CCDC = 175;
    private TYPE_PP_INVOICE = 210;
    private TYPE_PP_SERVICE = 240;
    private TYPE_PP_DISCOUNT_RETURN = 230;
    private TYPE_FA_INCREAMENT = 610;
    private TYPE_TI_INCREAMENT = 510;
    isSaving: boolean;
    dateDp: any;
    postedDateDp: any;
    accountDefaults: any[];
    accountDefault: { value?: string };
    currencies: ICurrency[];
    mBCreditCardDetail: IMBCreditCardDetails[];
    mBCreditCardDetailTax: IMBCreditCardDetailTax[];
    eMContracts: IEMContract[];
    isRecord: Boolean;
    totalAmount: number;
    totalAmountOriginal: number;
    isThem: Boolean;
    temp: Number;
    totalVatAmount: any;
    totalVatAmountOriginal: any;
    banks: IBank[];
    accountList: IAccountList[];
    autoPrinciple: IAutoPrinciple[];
    accountingObjectBankAccounts: IAccountingObjectBankAccount[];
    employeeName: string;
    autoPrincipleName: string;
    items: any;
    id: string;
    i: number;
    recorded: boolean;
    //  data storage provider
    page: number;
    itemsPerPage: number;
    pageCount: number;
    totalItems: number;
    rowNum: number;
    // sort
    predicate: any;
    accountingObjectName: any;
    reverse: any;
    record_: any;
    isCreateUrl: boolean;
    isEditUrl: boolean;
    disableAddButton: boolean;
    disableEditButton: boolean;
    accounting: IAccountingObject[];
    searchVoucher: string;
    dataSession: IDataSessionStorage;
    creditCardType: string;
    ownerCard: string;
    warningMessage: any;
    viewVouchersSelected: any;
    contextMenu: ContextMenu;
    eventSubscriber: Subscription;
    modalRef: NgbModalRef;
    currentAccount: any;
    isSoTaiChinh: boolean;
    currency: Currency;
    ngayHachToan: any;
    sysRecord: any;
    sysTypeLedger: any;
    options: any;
    DDSo_NCachHangNghin: any;
    DDSo_NCachHangDVi: any;
    DDSo_TienVND: any;
    DDSo_NgoaiTe: any;
    isHideTypeLedger: Boolean;
    TCKHAC_SDSoQuanTri: any;
    creditAccountList: IAccountList[];
    creditAccountListItem: IAccountList;
    debitAccountList: IAccountList[];
    debitAccountItem: IAccountList;
    vatAccountList: IAccountList[];
    iAutoPrinciple: IAutoPrinciple;
    mBCreditCardCopy: IMBCreditCard;
    mBCreditCardDetailsCopy: IMBCreditCardDetails[];
    mBCreditCardDetailTaxsCopy: IMBCreditCardDetailTax[];
    statusVoucher: number;
    viewVouchersSelectedCopy: any;
    no: any;
    listTempEMContract: string[];
    listTempHeaderEMContract: string[];
    noEMContract: any;
    goodsServicePurchases: IGoodsServicePurchase[];
    goodsServicePurchaseID: any;
    isReadOnly: any;
    isShowGoodsServicePurchase: boolean;
    columnList = [
        { column: AccountType.TK_CO, ppType: false },
        { column: AccountType.TK_NO, ppType: false },
        { column: AccountType.TK_THUE_GTGT, ppType: false },
        { column: AccountType.TKDU_THUE_GTGT, ppType: false }
    ];
    lstAccountingObjectTypes: any[];
    select: number;
    currentRow: number;
    listVAT: any[];
    isRefVoucher: boolean;
    currencyID: string;
    mBCreditCardDetailVendors: IMBCreditCardDetailVendor[];
    currentCurrency?: ICurrency;
    nameCategory?: any;
    backUpAccountingObjectID: string;
    accountingObjectBankAccountDetailIDCopy: string;
    invoiceTypes: InvoiceType[];

    ROLE_TTD_XEM = ROLE.TheTinDung_Xem;
    ROLE_TTD_THEM = ROLE.TheTinDung_Them;
    ROLE_TTD_SUA = ROLE.TheTinDung_Sua;
    ROLE_TTD_XOA = ROLE.TheTinDung_Xoa;
    ROLE_TTD_GHISO = ROLE.TheTinDung_GhiSo;

    ROLE_TTD_IN = ROLE.TheTinDung_In;
    ROLE_TTD_KETXUAT = ROLE.TheTinDung_KetXuat;

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
        private router: Router,
        private mBCreditCardService: MBCreditCardService,
        private activatedRoute: ActivatedRoute,
        private accService: AccountingObjectService,
        public utilService: UtilsService,
        private accountDefaultsService: AccountDefaultService,
        private jhiAlertService: JhiAlertService,
        private gLService: GeneralLedgerService,
        private bankAccountDetailsService: BankAccountDetailsService,
        private currencyService: CurrencyService,
        private bankService: BankService,
        private expenseItemsService: ExpenseItemService,
        private statisticsCodeService: StatisticsCodeService,
        private eMContractService: EMContractService,
        private accountListService: AccountListService,
        private autoPrincipleService: AutoPrincipleService,
        private organizationUnitService: OrganizationUnitService,
        private costSetService: CostSetService,
        private creditCardService: CreditCardService,
        private translate: TranslateService,
        private toastr: ToastrService,
        private budgetItemService: BudgetItemService,
        private eventManager: JhiEventManager,
        private refModalService: RefModalService,
        private principal: Principal,
        private modalService: NgbModal,
        private accountingObjectBankAccountService: AccountingObjectBankAccountService,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        private viewVoucherService: ViewVoucherService,
        private iaReportService: IAReportService
    ) {
        super();
        this.dataSession = JSON.parse(sessionStorage.getItem('dataSession'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.predicate = this.dataSession.predicate;
            this.searchVoucher = this.dataSession.searchVoucher;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
        this.accounting = [];
        this.contextMenu = new ContextMenu();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (account.organizationUnit.taxCalculationMethod === 0) {
                this.isShowGoodsServicePurchase = true;
            } else {
                this.isShowGoodsServicePurchase = false;
            }
            this.sysRecord = this.currentAccount.systemOption.find(x => x.code === TCKHAC_GhiSo && x.data);
            this.sysTypeLedger = this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data;
            this.TCKHAC_SDSoQuanTri = this.currentAccount.systemOption.find(x => x.code === TCKHAC_SDSoQuanTri).data;
            this.currencyService.findAllActive().subscribe(res => {
                this.currencies = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                this.currency = this.currencies.find(cur => cur.currencyCode === this.currentAccount.organizationUnit.currencyID);
                this.currentCurrency = this.currencies.find(cur => cur.currencyCode === this.mBCreditCard.currencyID);
                if (!this.mBCreditCard || !this.mBCreditCard.currencyID) {
                    this.mBCreditCard.currencyID = this.currency.currencyCode;
                    this.selectChangeCurrency();
                }
                this.currencyID = this.currency.currencyCode;
                this.DDSo_NCachHangNghin = this.currentAccount.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
                this.DDSo_NCachHangDVi = this.currentAccount.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;
                this.DDSo_NgoaiTe = this.currentAccount.systemOption.find(x => x.code === DDSo_NgoaiTe && x.data).data;
                this.DDSo_TienVND = this.currentAccount.systemOption.find(x => x.code === DDSo_TienVND && x.data).data;
            });
            if (this.mBCreditCard && this.mBCreditCard.id && this.mBCreditCard.typeID === this.TYPE_MB_CREDIT_CARD_NCC) {
                const param = {
                    typeID: this.TYPE_MB_CREDIT_CARD_NCC,
                    columnName: this.columnList
                };
                this.accountListService.getAccountTypeFour(param).subscribe(res => {
                    const dataAccount: IAccountAllList = res.body;
                    this.creditAccountList = dataAccount.creditAccount;
                    this.debitAccountList = dataAccount.debitAccount;
                    this.vatAccountList = dataAccount.vatAccount;
                    this.mBCreditCardDetail.forEach(item => {
                        item.creditAccountItem = this.creditAccountList.find(n => n.accountNumber === item.creditAccount);
                        item.debitAccountItem = this.debitAccountList.find(n => n.accountNumber === item.debitAccount);
                    });
                    this.mBCreditCardDetailTax.forEach(item => {
                        item.vatAccountItem = this.vatAccountList.find(n => n.accountNumber === item.vATAccount);
                    });
                });
            } else {
                const param = {
                    typeID: this.TYPE_MB_CREDIT_CARD,
                    columnName: this.columnList
                };
                this.accountListService.getAccountTypeFour(param).subscribe(res => {
                    const dataAccount: IAccountAllList = res.body;
                    this.creditAccountList = dataAccount.creditAccount;
                    this.debitAccountList = dataAccount.debitAccount;
                    this.vatAccountList = dataAccount.vatAccount;
                    this.mBCreditCardDetail.forEach(item => {
                        item.creditAccountItem = this.creditAccountList.find(n => n.accountNumber === item.creditAccount);
                        item.debitAccountItem = this.debitAccountList.find(n => n.accountNumber === item.debitAccount);
                    });
                    this.mBCreditCardDetailTax.forEach(item => {
                        item.vatAccountItem = this.vatAccountList.find(n => n.accountNumber === item.vATAccount);
                    });
                });
            }
            // this.accountListService
            //     .getAccountTypeThird({
            //         typeID: 170,
            //         columnName: JSON.stringify(this.columnList)
            //     })
            //     .subscribe(res => {
            //         const dataAccount: IAccountAllList = res.body;
            //         this.creditAccountList = dataAccount.creditAccount;
            //         this.debitAccountList = dataAccount.debitAccount;
            //         this.vatAccountList = dataAccount.vatAccount;
            //         this.mBCreditCardDetail.forEach(item => {
            //             item.creditAccountItem = this.creditAccountList.find(n => n.accountNumber === item.creditAccount);
            //             item.debitAccountItem = this.debitAccountList.find(n => n.accountNumber === item.debitAccount);
            //         });
            //         this.mBCreditCardDetailTax.forEach(item => {
            //             item.vatAccountItem = this.vatAccountList.find(n => n.accountNumber === item.vATAccount);
            //         });
            //     });
            // lấy dữ liệu cho combobox
            this.accService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
                this.accounting = res.body;
                if (this.mBCreditCard.accountingObjectType !== null && this.mBCreditCard.accountingObjectType !== undefined) {
                    this.accountingObjects = this.getObjectType(this.mBCreditCard.accountingObjectType);
                } else {
                    this.mBCreditCard.accountingObjectType = 1;
                    this.accountingObjects = this.getObjectType(1);
                }
            });
        });
        this.iaReportService.queryInvoiceType().subscribe(res => {
            this.invoiceTypes = res.body;
        });
    }

    ngOnInit() {
        this.searchVoucher = sessionStorage.getItem('dataSearchMBCreditCard') ? sessionStorage.getItem('dataSearchMBCreditCard') : null;
        this.isSaving = false;
        this.isEditUrl = window.location.href.includes('/edit');
        this.isHideTypeLedger = false;
        this.activatedRoute.data.subscribe(({ mBCreditCard }) => {
            this.mBCreditCard = mBCreditCard;
            this.backUpAccountingObjectID = this.mBCreditCard.accountingObjectID;
            this.viewVouchersSelected = mBCreditCard.viewVouchers ? mBCreditCard.viewVouchers : [];
            this.listVAT = [
                { name: '0%', data: 0 },
                { name: '5%', data: 1 },
                { name: '10%', data: 2 },
                { name: 'KCT', data: 3 },
                { name: 'KTT', data: 4 }
            ];
            this.translate
                .get([
                    'ebwebApp.mBDeposit.supplier',
                    'ebwebApp.mBDeposit.customer',
                    'ebwebApp.mBDeposit.employee',
                    'ebwebApp.mBDeposit.other'
                ])
                .subscribe(res => {
                    this.lstAccountingObjectTypes = [
                        { value: 0, name: res['ebwebApp.mBDeposit.supplier'] },
                        { value: 1, name: res['ebwebApp.mBDeposit.customer'] },
                        { value: 2, name: res['ebwebApp.mBDeposit.employee'] },
                        { value: 3, name: res['ebwebApp.mBDeposit.other'] }
                    ];
                });
            if (!this.isEditUrl) {
                this.statusVoucher = 0;
                this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                this.mBCreditCard.typeID = this.TYPE_MB_CREDIT_CARD;
                this.mBCreditCard.date = this.utilService.ngayHachToan(this.currentAccount);
                this.mBCreditCard.postedDate = this.mBCreditCard.date;
                if (this.TCKHAC_SDSoQuanTri === '0') {
                    this.mBCreditCard.typeLedger = 0;
                    this.sysTypeLedger = 0;
                    this.isHideTypeLedger = true;
                } else {
                    this.mBCreditCard.typeLedger = 2;
                    this.sysTypeLedger = this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                }
                this.utilService
                    .getGenCodeVoucher({
                        typeGroupID: 17, // typeGroupID loại chứng từ
                        companyID: '', // ID công ty
                        branchID: '', // ID chi nhánh
                        displayOnBook: this.sysTypeLedger // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
                    })
                    .subscribe((res: HttpResponse<string>) => {
                        this.no = res.body;
                        this.translate.get(['ebwebApp.mBCreditCard.defaultReason']).subscribe(res2 => {
                            this.mBCreditCard.reason = res2['ebwebApp.mBCreditCard.defaultReason'];
                        });
                        this.copy();
                    });
                this.isReadOnly = false;
            } else {
                this.utilService
                    .getIndexRow({
                        id: this.mBCreditCard.id,
                        isNext: true,
                        typeID: this.TYPE_MB_CREDIT_CARD,
                        searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                    })
                    .subscribe(
                        (res: HttpResponse<any[]>) => {
                            this.rowNum = res.body[0];
                            if (res.body.length === 1) {
                                this.totalItems = 1;
                            } else {
                                this.totalItems = res.body[1];
                            }
                        },
                        (res: HttpErrorResponse) => this.onError(res.message)
                    );
                if (this.TCKHAC_SDSoQuanTri === '0') {
                    this.no = this.mBCreditCard.noFBook;
                    this.isHideTypeLedger = true;
                    this.sysTypeLedger = 0;
                    this.mBCreditCard.typeLedger = 0;
                } else {
                    this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                    this.no = this.isSoTaiChinh ? this.mBCreditCard.noFBook : this.mBCreditCard.noMBook;
                    this.statusVoucher = 1; // Xem chứng từ
                }
                // if (this.mBCreditCard.mbCreditCardDetailTax) {
                //     for (let i = 0; i < this.mBCreditCard.mbCreditCardDetailTax.length; i++) {
                //         this.mBCreditCard.mbCreditCardDetailTax[i].invoiceDate = moment(this.mBCreditCard.mbCreditCardDetailTax[i].invoiceDate);
                //     }
                // }
                this.isReadOnly = true;
            }
        });
        this.accService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.employees = res.body
                .filter(listEmployee => listEmployee.isEmployee && listEmployee.isActive)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
        });
        this.bankService.getBanks().subscribe((res: HttpResponse<IBank[]>) => {
            this.banks = res.body;
        });
        this.bankAccountDetailsService.getBankAccountDetails().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
            this.bankAccountDetails = res.body;
        });
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticCodes = res.body;
        });
        this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body;
        });
        this.expenseItemsService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body.sort((a, b) => a.expenseItemCode.localeCompare(b.expenseItemCode));
        });
        this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
            this.eMContracts = res.body;
        });
        this.organizationUnitService.getOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body.sort((a, b) => a.organizationUnitCode.localeCompare(b.organizationUnitCode));
        });
        this.autoPrincipleService.getAutoPrinciples().subscribe((res: HttpResponse<IAutoPrinciple[]>) => {
            this.autoPrinciple = res.body.filter(aPrinciple => aPrinciple.typeId === 170 || aPrinciple.typeId === 0).sort((n1, n2) => {
                if (n1.typeId > n2.typeId) {
                    return 1;
                }
                if (n1.typeId < n2.typeId) {
                    return -1;
                }
                return 0;
            });
        });
        this.budgetItemService.getBudgetItems().subscribe((res: HttpResponse<IBudgetItem[]>) => {
            this.budgetItems = res.body;
        });
        this.goodsServicePurchaseService.getGoodServicePurchases().subscribe((res: HttpResponse<IGoodsServicePurchase[]>) => {
            this.goodsServicePurchases = res.body;
        });
        if (this.mBCreditCard) {
            if (this.mBCreditCard.accountingObjectID) {
                this.accountingObjectBankAccountService
                    .getByAccountingObjectById({ accountingObjectID: this.mBCreditCard.accountingObjectID })
                    .subscribe((res: HttpResponse<IAccountingObjectBankAccount[]>) => {
                        this.accountingObjectBankAccounts = res.body;
                        if (sessionStorage.getItem('accountingObjectBankAccountDetailID')) {
                            this.mBCreditCard.accountingObjectBankAccountDetailID = JSON.parse(
                                sessionStorage.getItem('accountingObjectBankAccountID')
                            );
                            sessionStorage.removeItem('accountingObjectBankAccountDetailID');
                        }
                    });
            }
        }
        if (this.TCKHAC_SDSoQuanTri === '1') {
            if (this.isSoTaiChinh) {
                this.noEMContract = 'noFBook';
                this.listTempEMContract = [this.noEMContract, 'name'];
                this.listTempHeaderEMContract = ['Số hợp đồng', 'Trích yếu'];
            } else {
                this.noEMContract = 'noMBook';
                this.listTempEMContract = [this.noEMContract, 'name'];
                this.listTempHeaderEMContract = ['Số hợp đồng', 'Trích yếu'];
            }
        } else {
            this.noEMContract = 'noFBook';
            this.listTempEMContract = [this.noEMContract, 'name'];
            this.listTempHeaderEMContract = ['Số hợp đồng', 'Trích yếu'];
        }
        this.accountDefaults = [];
        this.accountDefaultsService.getAllAccountDefaults().subscribe((res: HttpResponse<IAccountDefault[]>) => {
            for (let i = 0; i < res.body.length; i++) {
                if (res.body[i].filterAccount !== null && res.body[i].filterAccount !== undefined) {
                    const lstacc = res.body[i].filterAccount.split(';');
                    for (let j = 0; j < lstacc.length; j++) {
                        this.accountDefault = {};
                        this.accountDefault.value = lstacc[j];
                        this.accountDefaults.push(this.accountDefault);
                    }
                }
            }
        });
        // Check ghi sổ, đếm số dòng
        if (this.mBCreditCard.id) {
            this.mBCreditCardDetail = this.mBCreditCard.mBCreditCardDetails === undefined ? [] : this.mBCreditCard.mBCreditCardDetails;
            this.mBCreditCardDetailTax =
                this.mBCreditCard.mbCreditCardDetailTax === undefined ? [] : this.mBCreditCard.mbCreditCardDetailTax;
            this.mBCreditCardDetailVendors =
                this.mBCreditCard.mBCreditCardDetailVendor === undefined ? [] : this.mBCreditCard.mBCreditCardDetailVendor;
            this.isRecord = this.mBCreditCard.recorded === undefined ? false : this.mBCreditCard.recorded;
        } else {
            this.mBCreditCardDetail = [];
            this.mBCreditCardDetailTax = [];
            this.mBCreditCardDetailVendors = [];
            this.isRecord = false;
        }
        if (this.mBCreditCard.id) {
            this.id = this.mBCreditCard.id;
        } else {
            // ; this.hideButtonPrevious = false
        }
        if (this.mBCreditCard.recorded) {
            this.isRecord = false;
        } else {
            this.isRecord = true;
        }
        this.isCreateUrl = window.location.href.includes('/mb-credit-card/new');
        this.isEdit = this.isCreateUrl;
        if (this.mBCreditCard.id && !this.isCreateUrl) {
            this.isEdit = this.isCreateUrl;
        } else {
            this.isEdit = this.isCreateUrl;
        }
        this.disableAddButton = true;
        if (this.mBCreditCard.id) {
            if (!this.mBCreditCard.recorded && this.isEditUrl) {
                this.disableEditButton = false;
            } else {
                this.disableEditButton = true;
            }
        } else {
            this.disableEditButton = true;
        }
        this.creditCardService.getCreditCardsByCompanyID().subscribe((res: HttpResponse<ICreditCard[]>) => {
            if (this.mBCreditCard && this.mBCreditCard.creditCardID) {
                this.creditCards = res.body.filter(a => a.isActive || a.id === this.mBCreditCard.creditCardID);
            } else {
                this.creditCards = res.body.filter(a => a.isActive);
            }
        });
        this.creditCardService
            .findByCreditCardNumber({ creditCardNumber: this.mBCreditCard.creditCardNumber })
            .subscribe((res: HttpResponse<ICreditCard>) => {
                this.ownerCard = res.body.ownerCard;
                this.creditCardType = res.body.creditCardType;
            });
        if (sessionStorage.getItem('saveAndLoad')) {
            this.mBCreditCard = JSON.parse(sessionStorage.getItem('saveAndLoad'));
            this.mBCreditCardDetail = this.mBCreditCard.mBCreditCardDetails;
            this.mBCreditCardDetailTax = this.mBCreditCard.mbCreditCardDetailTax;
            this.viewVouchersSelected = this.mBCreditCard.viewVouchers;
            this.mBCreditCard.date = moment(this.mBCreditCard.date);
            this.mBCreditCard.postedDate = this.mBCreditCard.date;
            this.getObjectType(this.mBCreditCard.accountingObjectType);
            this.creditCardService
                .findByCreditCardNumber({ creditCardNumber: this.mBCreditCard.creditCardNumber })
                .subscribe((res: HttpResponse<ICreditCard>) => {
                    this.ownerCard = res.body.ownerCard;
                    this.creditCardType = res.body.creditCardType;
                    this.selectChangeAccountingObject();
                });
            this.copy();
        } else if (!sessionStorage.getItem('saveAndLoad') && !this.isEditUrl) {
            this.mBCreditCard.accountingObjectType = 1;
            this.getObjectType(this.mBCreditCard.accountingObjectType);
        }
        sessionStorage.removeItem('saveAndLoad');
        this.registerRef();
        this.sumAfterDeleteByContextMenu();
        this.isRefVoucher = window.location.href.includes('/edit/from-ref');
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.selectChangeAccountingObject();
            this.utilService.setShowPopup(false);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.selectChangeAccountingObject();
            this.utilService.setShowPopup(false);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilService.setShowPopup(response.content);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        this.afterAddRow();
        this.afterCopyRow();
        // this.registerIsShowPopup();
    }

    // registerIsShowPopup() {
    //     this.utilService.checkEvent.subscribe(res => {
    //         this.isShowPopup = res;
    //     });
    // }

    closeForm() {
        event.preventDefault();
        if (this.mBCreditCardCopy && (this.statusVoucher === 0 || this.statusVoucher === 1) && !this.utilService.isShowPopup) {
            if (
                !this.utilService.isEquivalent(this.mBCreditCard, this.mBCreditCardCopy) ||
                !this.utilService.isEquivalentArray(this.mBCreditCardDetail, this.mBCreditCardDetailsCopy) ||
                !this.utilService.isEquivalentArray(this.mBCreditCardDetailTax, this.mBCreditCardDetailTaxsCopy) ||
                !this.utilService.isEquivalentArray(this.viewVouchersSelected, this.viewVouchersSelectedCopy)
            ) {
                if (this.isReadOnly) {
                    this.closeAll();
                    return;
                }
                // this.copy();
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
            } else {
                this.closeAll();
            }
        } else if (!this.utilService.isShowPopup) {
            this.copy();
            this.closeAll();
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TheTinDung_Them])
    copyAndNew() {
        event.preventDefault();
        if (!this.isCreateUrl && !this.utilService.isShowPopup) {
            this.mBCreditCard.id = undefined;
            this.mBCreditCard.noMBook = undefined;
            this.mBCreditCard.noFBook = undefined;
            for (let i = 0; i < this.mBCreditCard.mBCreditCardDetails.length; i++) {
                this.mBCreditCard.mBCreditCardDetails[i].id = undefined;
            }
            for (let i = 0; i < this.mBCreditCard.mbCreditCardDetailTax.length; i++) {
                this.mBCreditCard.mbCreditCardDetailTax[i].id = undefined;
            }
            for (let i = 0; i < this.mBCreditCard.viewVouchers.length; i++) {
                this.mBCreditCard.viewVouchers[i].id = undefined;
            }
            sessionStorage.setItem('saveAndLoad', JSON.stringify(this.mBCreditCard));
            sessionStorage.setItem(
                'accountingObjectBankAccountDetailID',
                JSON.stringify(this.mBCreditCard.accountingObjectBankAccountDetailID)
            );
            this.selectChangeAccountingObject();
            this.edit();
            this.isSaving = false;
            this.router.navigate(['mb-credit-card/new']);
            this.statusVoucher = 0;
            this.mBCreditCard.id = undefined;
            this.copy();
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TheTinDung_Sua])
    edit() {
        event.preventDefault();
        if (
            !this.isCreateUrl &&
            this.isRecord &&
            !this.checkCloseBook(this.currentAccount, this.mBCreditCard.postedDate) &&
            !this.utilService.isShowPopup
        ) {
            this.isCreateUrl = !this.isCreateUrl;
            this.isEdit = this.isCreateUrl;
            this.statusVoucher = 0;
            this.focusFirstInput();
            this.copy();
            this.disableAddButton = false;
            this.disableEditButton = true;
            this.isReadOnly = false;
        }
    }

    addNew(isNew = false) {
        event.preventDefault();
        if (!this.isCreateUrl && !this.utilService.isShowPopup) {
            this.router.navigate(['mb-credit-card/new']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TheTinDung_Them, ROLE.TheTinDung_Sua])
    save() {
        event.preventDefault();
        if (this.isCreateUrl && !this.utilService.isShowPopup) {
            this.warningMessage = '';
            this.mBCreditCard.mBCreditCardDetails = this.mBCreditCardDetail;
            this.mBCreditCard.mbCreditCardDetailTax = this.mBCreditCardDetailTax;
            this.mBCreditCard.viewVouchers = this.viewVouchersSelected;
            if (this.isCreateUrl && !this.isShowPopup) {
                sessionStorage.removeItem('saveAndLoad');
                if (this.checkError()) {
                    this.fillToSave();
                    if (this.mBCreditCard.id) {
                        this.subscribeToSaveResponse(this.mBCreditCardService.update(this.mBCreditCard));
                    } else {
                        this.subscribeToSaveResponse(this.mBCreditCardService.create(this.mBCreditCard));
                    }
                } else {
                }
            }
        }
    }

    continueModalSaveComponent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.fillToSave();
        if (this.mBCreditCard.id) {
            this.subscribeToSaveResponse(this.mBCreditCardService.update(this.mBCreditCard));
        } else {
            this.subscribeToSaveResponse(this.mBCreditCardService.create(this.mBCreditCard));
        }
    }

    checkError(): boolean {
        if (!this.no) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.voucherNumber') +
                ' ' +
                this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        if (!this.mBCreditCard.date) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.date') + ' ' + this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        if (!this.mBCreditCard.postedDate) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.postedDate') +
                ' ' +
                this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        if (!this.mBCreditCard.currencyID) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.detail.currencyType') +
                ' ' +
                this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        if (this.mBCreditCard.postedDate.format(DATE_FORMAT) < this.mBCreditCard.date.format(DATE_FORMAT)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBDeposit.errorPostedDateAndDate'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (this.checkCloseBook(this.currentAccount, this.mBCreditCard.postedDate)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.lockBook.err.checkForSave'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.mBCreditCard.exchangeRate) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.exchangeRate') +
                ' ' +
                this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        if (this.mBCreditCard.typeLedger === undefined || this.mBCreditCard.typeLedger === null) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.typeLedger') +
                ' ' +
                this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        if (!this.utilService.checkNoBook(this.no ? this.no : null, this.currentAccount)) {
            return false;
        }
        if (this.mBCreditCard.mBCreditCardDetails.length === 0) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.home.details') +
                ' ' +
                this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        for (this.i = 0; this.i < this.mBCreditCard.mBCreditCardDetails.length; this.i++) {
            if (!this.mBCreditCard.mBCreditCardDetails[this.i].debitAccount) {
                this.warningMessage =
                    this.translate.instant('ebwebApp.mBCreditCard.debitAccount') +
                    ' ' +
                    this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
                this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
                return false;
            }
            if (!this.mBCreditCard.mBCreditCardDetails[this.i].creditAccount) {
                this.warningMessage =
                    this.translate.instant('ebwebApp.mBCreditCard.creditAccount') +
                    ' ' +
                    this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
                this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
                return false;
            }
        }
        if (this.mBCreditCardDetailTax && this.mBCreditCardDetailTax.length > 0) {
            for (let i = 0; i < this.mBCreditCardDetailTax.length; i++) {
                if (this.mBCreditCardDetailTax[i].taxCode) {
                    if (!this.utilService.checkMST(this.mBCreditCardDetailTax[i].taxCode)) {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.mCReceipt.error.taxCodeInvalid'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        return false;
                    }
                }
            }
        }
        const checkAcc = this.utilService.checkAccoutWithDetailType(
            this.debitAccountList,
            this.creditAccountList,
            this.mBCreditCardDetail,
            this.accountingObjects,
            this.costSets,
            this.eMContracts,
            [],
            null,
            this.organizationUnits,
            this.expenseItems,
            this.budgetItems,
            this.statisticCodes
        );
        if (checkAcc) {
            this.toastr.error(checkAcc, this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        if (this.mBCreditCardDetail.length > 0) {
            for (let i = 0; i < this.mBCreditCardDetail.length; i++) {
                const detailTypeCre = this.creditAccountList.find(a => a.accountNumber === this.mBCreditCardDetail[i].creditAccount)
                    .detailType;
                const detailTypeDeb = this.debitAccountList.find(a => a.accountNumber === this.mBCreditCardDetail[i].debitAccount)
                    .detailType;
                if (detailTypeCre === '6') {
                    if (!this.mBCreditCard.creditCardNumber) {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.mBDeposit.account') +
                                this.mBCreditCardDetail[i].creditAccount +
                                this.translate.instant('ebwebApp.mBDeposit.isAccountDetailFromBankAccountDetail') +
                                this.translate.instant('ebwebApp.mBCreditCard.accountPay') +
                                ' ' +
                                this.translate.instant('ebwebApp.mBDeposit.isNotNull'),
                            this.translate.instant('ebwebApp.mBDeposit.error')
                        );
                        return false;
                    }
                }
                if (detailTypeDeb === '6') {
                    if (!this.mBCreditCard.creditCardNumber) {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.mBDeposit.account') +
                                this.mBCreditCardDetail[i].debitAccount +
                                this.translate.instant('ebwebApp.mBDeposit.isAccountDetailFromBankAccountDetail') +
                                this.translate.instant('ebwebApp.mBCreditCard.accountPay') +
                                ' ' +
                                this.translate.instant('ebwebApp.mBDeposit.isNotNull'),
                            this.translate.instant('ebwebApp.mBDeposit.error')
                        );
                        return false;
                    }
                }
            }
        }
        if (!this.checkNhapTien()) {
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.modalRef = this.modalService.open(this.modalComponentAmount, { backdrop: 'static' });
            return false;
        }
        return true;
    }

    checkNhapTien() {
        /*Check nhập tiền giữa 2 tab*/
        let result = true;
        let keepGoing = true;
        // có bên hàng tiền mà không có bên thuế
        this.mBCreditCardDetail.filter(n => n.debitAccount.startsWith('133')).forEach(n => {
            if (keepGoing) {
                if (this.mBCreditCardDetailTax.filter(m => m.vATAccount === n.debitAccount).length === 0) {
                    result = false;
                    keepGoing = false;
                }
            }
        });
        // Có bên tab thuế mà k có bên hàng tiền
        this.mBCreditCardDetailTax.filter(n => n.vATAccount.startsWith('133')).forEach(n => {
            if (keepGoing) {
                if (this.mBCreditCardDetail.filter(m => m.debitAccount === n.vATAccount).length === 0) {
                    result = false;
                    keepGoing = false;
                }
            }
        });
        // fill tiền không hợp lệ
        this.mBCreditCardDetail.forEach(n => {
            if (keepGoing) {
                const tax = this.mBCreditCardDetailTax.find(
                    m => m.vATAccount === n.debitAccount && m.vATAccount !== null && m.vATAccount !== undefined && m.vATAccount !== ''
                );
                if (tax) {
                    if (tax.vATAccount.startsWith('133', 0)) {
                        if (!n.amountOriginal && tax.vATAmount) {
                            // this.toastr.error('Lỗi nhập tiền', this.translate.instant('ebwebApp.mCReceipt.error.error'));
                            result = false;
                            keepGoing = false;
                        }
                        if (result) {
                            const total133 = this.mBCreditCardDetail
                                .filter(t => t.debitAccount === tax.vATAccount)
                                .map(t => t.amount)
                                .reduce((a, b) => a + b);
                            if (total133 !== tax.vATAmount) {
                                result = false;
                                keepGoing = false;
                            }
                        }
                    }
                }
            }
        });
        return result;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    if (this.isEditUrl) {
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBDeposit.editSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    } else {
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBDeposit.insertSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    this.isEdit = this.isCreateUrl = false;
                    if (res.body.mbCreditCard.recorded) {
                        this.disableAddButton = false;
                    } else {
                        this.disableAddButton = true;
                    }
                    this.mBCreditCard.id = res.body.mbCreditCard.id;
                    this.mBCreditCard.recorded = res.body.mbCreditCard.recorded;
                    this.onSaveSuccess();
                    this.isReadOnly = true;
                    this.router.navigate(['./mb-credit-card', this.mBCreditCard.id, 'edit']);
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                    this.isEdit = this.isCreateUrl = true;
                    return;
                } else if (res.body.status === 2) {
                    if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.disableEditButton = false;
                        this.disableAddButton = true;
                        this.mBCreditCard.id = res.body.mbCreditCard.id;
                        this.copy();
                        this.router.navigate(['./mb-credit-card', this.mBCreditCard.id, 'edit']);
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyQT'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.disableEditButton = false;
                        this.disableAddButton = true;
                        this.mBCreditCard.id = res.body.mbCreditCard.id;
                        this.copy();
                        this.router.navigate(['./mb-credit-card', this.mBCreditCard.id, 'edit']);
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyTC'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.disableEditButton = false;
                        this.disableAddButton = true;
                        this.mBCreditCard.id = res.body.mbCreditCard.id;
                        this.copy();
                        this.router.navigate(['./mb-credit-card', this.mBCreditCard.id, 'edit']);
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuy'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else {
                        this.recordFailed();
                    }
                }
            },
            (res: HttpErrorResponse) => {
                this.onSaveError();
                this.isEditUrl = this.isCreateUrl = true;
            }
        );
    }

    private recordFailed() {
        this.toastr.error(this.translate.instant('global.data.recordFailed'), this.translate.instant('ebwebApp.mCReceipt.home.message'));
    }

    private onSaveSuccess() {
        this.copy();
        this.statusVoucher = 1;
        this.isSaving = false;
        this.disableAddButton = true;
        this.disableEditButton = true;
        this.isRecord = false;
    }

    private onSaveError() {
        this.isSaving = false;
    }

    get mBCreditCard() {
        return this._mBCreditCard;
    }

    get accountingObject() {
        // @ts-ignore
        return this._accountingObject.isemployee(true);
    }

    set mBCreditCard(mBCreditCard: IMBCreditCard) {
        this._mBCreditCard = mBCreditCard;
    }

    selectChangeAccountingObject() {
        let iAccountingObject;
        if (this.mBCreditCard.accountingObjectID) {
            iAccountingObject = this.accountingObjects.find(
                accountingObject => accountingObject.id === this.mBCreditCard.accountingObjectID
            );
        }
        if (iAccountingObject) {
            this.mBCreditCard.accountingObjectName = iAccountingObject.accountingObjectName;
            this.mBCreditCard.accountingObjectAddress = iAccountingObject.accountingObjectAddress;
            this.mBCreditCard.taxCode = iAccountingObject.taxCode;
        } else {
            this.mBCreditCard.accountingObjectName = '';
            this.mBCreditCard.accountingObjectAddress = '';
        }
        if (this.mBCreditCard.accountingObjectID) {
            this.accountingObjectBankAccountService
                .getByAccountingObjectById({ accountingObjectID: this.mBCreditCard.accountingObjectID })
                .subscribe((res: HttpResponse<IAccountingObjectBankAccount[]>) => {
                    this.accountingObjectBankAccounts = res.body;
                });
        } else {
            this.accountingObjectBankAccounts = [];
        }
        for (const dt of this.mBCreditCardDetail) {
            if (dt.accountingObjectID === this.backUpAccountingObjectID) {
                dt.accountingObjectID = this.mBCreditCard.accountingObjectID;
                if (!this.autoPrincipleName) {
                    this.translate.get(['ebwebApp.mBTellerPaper.defaultReason']).subscribe(res2 => {
                        dt.description = res2['ebwebApp.mBTellerPaper.defaultReason'] + ' từ ' + this.mBCreditCard.accountingObjectName;
                    });
                }
            }
        }
        for (const dtt of this.mBCreditCardDetailTax) {
            dtt.accountingObjectID = this.mBCreditCard.accountingObjectID;
            dtt.accountingObjectName = iAccountingObject.accountingObjectName;
            dtt.taxCode = iAccountingObject.taxCode;
        }
        if (this.isCreateUrl && !this.autoPrincipleName) {
            this.mBCreditCard.reason =
                'Chi tiền bằng thẻ tín dụng ' +
                (this.mBCreditCard.accountingObjectName ? 'cho ' + this.mBCreditCard.accountingObjectName : '');
        }
        if (!sessionStorage.getItem('accountingObjectBankAccountDetailID') && !this.mBCreditCard.id) {
            this.mBCreditCard.accountingObjectBankAccountDetailID = null;
        }
        this.mBCreditCard.accountingObjectBankName = '';
        this.backUpAccountingObjectID = this.mBCreditCard.accountingObjectID;
    }

    selectChangeReason() {
        this.mBCreditCard.reason = this.autoPrincipleName;
        this.iAutoPrinciple = this.autoPrinciple.find(a => a.autoPrincipleName === this.autoPrincipleName);
        if (this.mBCreditCard.mBCreditCardDetails) {
            if (this.mBCreditCard.mBCreditCardDetails.length > 0) {
                this.mBCreditCardDetail[0].description = this.autoPrincipleName;
            }
        }
        for (const dt of this.mBCreditCardDetail) {
            dt.debitAccount = this.iAutoPrinciple.debitAccount;
            dt.creditAccount = this.iAutoPrinciple.creditAccount;
            dt.description = this.iAutoPrinciple.autoPrincipleName;
        }
    }

    selectChangeDescription() {
        if (this.mBCreditCard.mBCreditCardDetails) {
            if (this.mBCreditCard.mBCreditCardDetails.length > 0) {
                this.mBCreditCardDetail[0].description = this.mBCreditCard.reason;
            }
        }
    }

    selectChangeCurrency() {
        let sum = 0;
        this.currentCurrency = this.currencies.find(cur => cur.currencyCode === this.mBCreditCard.currencyID);
        if (this.currentCurrency) {
            this.mBCreditCard.exchangeRate = this.currentCurrency.exchangeRate;
            if (this.isCreateUrl) {
                for (let i = 0; i < this.mBCreditCardDetail.length; i++) {
                    this.utilService.calcular(
                        this.mBCreditCardDetail[i],
                        170,
                        'amountOriginal',
                        this.mBCreditCard,
                        this.mBCreditCardDetail,
                        this.mBCreditCardDetailTax,
                        this.currentAccount,
                        this.getAmountOriginalType(),
                        this.currentCurrency.formula.includes('*') ? this.mBCreditCard.exchangeRate : 1 / this.mBCreditCard.exchangeRate
                    );
                }
            }
        }
        if (this.mBCreditCardDetail && this.currentCurrency && this.currentCurrency.formula) {
            for (let i = 0; i < this.mBCreditCardDetail.length; i++) {
                this.mBCreditCardDetail[i].amount = this.round(
                    this.mBCreditCardDetail[i].amountOriginal *
                        (this.currentCurrency.formula.includes('*') ? this.mBCreditCard.exchangeRate : 1 / this.mBCreditCard.exchangeRate),
                    7
                );
                sum = sum + this.mBCreditCardDetail[i].amount;
            }
        }
        this.mBCreditCard.totalAmount = sum;
    }

    getAmountOriginalType() {
        if (this.isForeignCurrency()) {
            return 8;
        }
        return 7;
    }

    selectChangeExchangeRate() {
        let sum = 0;
        if (this.currentCurrency) {
            if (this.mBCreditCardDetail) {
                for (let i = 0; i < this.mBCreditCardDetail.length; i++) {
                    this.mBCreditCardDetail[i].amount = this.round(
                        this.mBCreditCardDetail[i].amountOriginal *
                            (this.currentCurrency.formula.includes('*')
                                ? this.mBCreditCard.exchangeRate
                                : 1 / this.mBCreditCard.exchangeRate),
                        8
                    );
                    sum = sum + this.mBCreditCardDetail[i].amount;
                }
            }
            this.mBCreditCard.totalAmount = sum;
        }
        if (this.isCreateUrl) {
            for (let i = 0; i < this.mBCreditCardDetail.length; i++) {
                this.utilService.calcular(
                    this.mBCreditCardDetail[i],
                    170,
                    'amountOriginal',
                    this.mBCreditCard,
                    this.mBCreditCardDetail,
                    this.mBCreditCardDetailTax,
                    this.currentAccount,
                    this.getAmountOriginalType(),
                    this.currency.formula.includes('*') ? this.mBCreditCard.exchangeRate : 1 / this.mBCreditCard.exchangeRate
                );
            }
        }
    }

    AddnewRow(eventData: any, select: number) {
        let findGoodsServicePurchase;
        if (this.isShowGoodsServicePurchase) {
            if (this.currentAccount.organizationUnit.goodsServicePurchaseID) {
                findGoodsServicePurchase = this.goodsServicePurchases.find(
                    goodsID => goodsID.id === this.currentAccount.organizationUnit.goodsServicePurchaseID
                );
            }
        }
        if (this.isCreateUrl) {
            if (select === 0) {
                this.mBCreditCardDetail.push(Object.assign({}, this.mBCreditCardDetail[this.mBCreditCardDetail.length - 1]));
                if (this.mBCreditCardDetail.length === 1) {
                    this.mBCreditCard.totalAmountOriginal = 0;
                    this.mBCreditCard.totalAmount = 0;
                    if (this.mBCreditCard.accountingObjectID) {
                        this.mBCreditCardDetail[0].accountingObjectID = this.mBCreditCard.accountingObjectID;
                    }
                    if (this.mBCreditCard.reason) {
                        this.mBCreditCardDetail[0].description = this.mBCreditCard.reason;
                    }
                }
                this.mBCreditCardDetail[this.mBCreditCardDetail.length - 1].id = undefined;
                if (this.mBCreditCard.reason) {
                    this.mBCreditCardDetail[0].description = this.mBCreditCard.reason;
                }
                if (this.iAutoPrinciple) {
                    this.mBCreditCardDetail[this.mBCreditCardDetail.length - 1].debitAccount = this.iAutoPrinciple.debitAccount;
                    this.mBCreditCardDetail[this.mBCreditCardDetail.length - 1].creditAccount = this.iAutoPrinciple.creditAccount;
                }
                this.mBCreditCardDetail[this.mBCreditCardDetail.length - 1].amount = 0;
                this.mBCreditCardDetail[this.mBCreditCardDetail.length - 1].amountOriginal = 0;
                // this.mBCreditCardDetail[this.mBCreditCardDetail.length - 1].orderPriority = this.mBCreditCardDetail.length - 1;
                const nameTag: string = event.srcElement.id;
                const index: number = this.mBCreditCardDetail.length - 1;
                const nameTagIndex: string = nameTag + String(index);
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(nameTagIndex);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            } else if (select === 1) {
                this.mBCreditCardDetailTax.push(Object.assign({}, this.mBCreditCardDetailTax[this.mBCreditCardDetailTax.length - 1]));
                if (this.mBCreditCardDetailTax.length === 1) {
                    if (this.mBCreditCard.accountingObjectID) {
                        const acc = this.accountingObjects.find(a => a.id === this.mBCreditCard.accountingObjectID);
                        this.mBCreditCardDetailTax[0].accountingObjectID = acc.id;
                        this.mBCreditCardDetailTax[0].accountingObjectName = acc.accountingObjectName;
                        this.mBCreditCardDetailTax[0].taxCode = acc.taxCode;
                    }
                }
                this.mBCreditCardDetailTax[this.mBCreditCardDetailTax.length - 1].description = 'Thuế GTGT';
                this.mBCreditCardDetailTax[this.mBCreditCardDetailTax.length - 1].id = undefined;
                this.mBCreditCardDetailTax[this.mBCreditCardDetailTax.length - 1].vATAmount = 0;
                this.mBCreditCardDetailTax[this.mBCreditCardDetailTax.length - 1].vATAmountOriginal = 0;
                this.mBCreditCardDetailTax[this.mBCreditCardDetailTax.length - 1].vATAmountOriginal = 0;
                this.mBCreditCardDetailTax[this.mBCreditCardDetailTax.length - 1].pretaxAmount = 0;
                this.mBCreditCardDetailTax[this.mBCreditCardDetailTax.length - 1].pretaxAmountOriginal = 0;
                if (this.isShowGoodsServicePurchase && findGoodsServicePurchase) {
                    this.mBCreditCardDetailTax[this.mBCreditCardDetailTax.length - 1].goodsServicePurchaseID = findGoodsServicePurchase.id;
                }
                // this.mBCreditCardDetailTax[this.mBCreditCardDetailTax.length - 1].orderPriority = this.mBCreditCardDetailTax.length - 1;
                const nameTag: string = event.srcElement.id;
                const index: number = this.mBCreditCardDetailTax.length - 1;
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
    }

    keyPress(value: number, select: number) {
        if (select === 0) {
            this.mBCreditCardDetail.splice(value, 1);
        } else if (select === 1) {
            this.mBCreditCardDetailTax.splice(value, 1);
        }
    }

    changeObjectType() {
        if (!this.isThem) {
            const objectType = this.mBCreditCard.accountingObjectType;
            this.accountingObjects = this.getObjectType(objectType);
        } else {
            const objectType = this.mBCreditCard.accountingObjectType;
            this.accountingObjects = this.getObjectType(objectType);
        }
        this.mBCreditCard.accountingObjectID = null;
        this.mBCreditCard.accountingObjectName = null;
        this.mBCreditCard.accountingObjectAddress = null;
        this.translate.get(['ebwebApp.mBCreditCard.defaultReason']).subscribe(res2 => {
            this.mBCreditCard.reason = res2['ebwebApp.mBCreditCard.defaultReason'];
        });
        this.mBCreditCard.accountingObjectBankAccountDetailID = null;
        this.mBCreditCard.accountingObjectBankName = null;
        for (let i = 0; i < this.mBCreditCardDetail.length; i++) {
            this.mBCreditCardDetail[i].accountingObjectID = null;
            this.translate.get(['ebwebApp.mBCreditCard.defaultReason']).subscribe(res2 => {
                this.mBCreditCardDetail[i].description = res2['ebwebApp.mBCreditCard.defaultReason'];
            });
        }
        for (let i = 0; i < this.mBCreditCardDetailTax.length; i++) {
            this.mBCreditCardDetailTax[i].accountingObjectID = null;
        }
    }

    getObjectType(a: any) {
        if (a === 0) {
            this.accountingObjects = this.accounting.filter(
                listAccountingObject =>
                    (listAccountingObject.objectType === 0 || listAccountingObject.objectType === 2) && listAccountingObject.isActive
            );
        } else if (a === 1) {
            this.accountingObjects = this.accounting.filter(
                listAccountingObject =>
                    (listAccountingObject.objectType === 1 || listAccountingObject.objectType === 2) && listAccountingObject.isActive
            );
        } else if (a === 2) {
            this.accountingObjects = this.accounting.filter(
                listAccountingObject => listAccountingObject.isActive && listAccountingObject.isEmployee
            );
        } else if (a === 3) {
            this.accountingObjects = this.accounting.filter(
                listAccountingObject => listAccountingObject.objectType === 3 && listAccountingObject.isActive
            );
        } else {
        }
        this.selectAccountingObjectType();
        return this.accountingObjects;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TheTinDung_GhiSo])
    record() {
        event.preventDefault();
        if (!this.isCreateUrl && !this.checkCloseBook(this.currentAccount, this.mBCreditCard.postedDate) && !this.utilService.isShowPopup) {
            if (this.mBCreditCard.id) {
                this.record_ = {};
                this.record_.id = this.mBCreditCard.id;
                this.record_.typeID = this.mBCreditCard.typeID;
                if (!this.mBCreditCard.recorded) {
                    this.gLService.record(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                        if (res.body.success) {
                            if (this.mBCreditCard.recorded) {
                                this.isRecord = false;
                            } else {
                                this.isRecord = true;
                            }
                            this.disableEditButton = true;
                            this.isRecord = false;
                            this.mBCreditCard.recorded = true;
                            this.toastr.success(
                                this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
                                this.translate.instant('ebwebApp.mBCreditCard.message')
                            );
                        } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
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
                        }
                    });
                }
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TheTinDung_GhiSo])
    unrecord() {
        event.preventDefault();
        if (!this.isCreateUrl && !this.checkCloseBook(this.currentAccount, this.mBCreditCard.postedDate) && !this.utilService.isShowPopup) {
            if (this.mBCreditCard.id) {
                if (this.mBCreditCard.recorded) {
                    this.isRecord = false;
                } else {
                    this.isRecord = true;
                }
                this.record_ = {};
                this.record_.id = this.mBCreditCard.id;
                this.record_.typeID = this.mBCreditCard.typeID;
                if (this.mBCreditCard.recorded) {
                    this.gLService.unrecord(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                        if (res.body.success) {
                            this.mBCreditCard.recorded = false;
                            this.toastr.success(
                                this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                                this.translate.instant('ebwebApp.mBCreditCard.message')
                            );
                        }
                    });
                    this.disableEditButton = false;
                    this.isRecord = true;
                }
            }
        }
    }

    print($event) {
        event.preventDefault();
        this.CTKTExportPDF(false, 0);
    }

    CTKTExportPDF(isDownload, typeReports: number) {
        if (!this.isCreateUrl) {
            this.mBCreditCardService
                .getCustomerReport({
                    id: this.mBCreditCard.id,
                    typeID: 170,
                    typeReport: typeReports
                })
                .subscribe(response => {
                    // this.showReport(response);
                    const file = new Blob([response.body], { type: 'application/pdf' });
                    const fileURL = window.URL.createObjectURL(file);

                    if (isDownload) {
                        const link = document.createElement('a');
                        document.body.appendChild(link);
                        link.download = fileURL;
                        link.setAttribute('style', 'display: none');
                        const name = 'Bao_co.pdf';
                        link.setAttribute('download', name);
                        link.href = fileURL;
                        link.click();
                    } else {
                        const contentDispositionHeader = response.headers.get('Content-Disposition');
                        const result = contentDispositionHeader
                            .split(';')[1]
                            .trim()
                            .split('=')[1];
                        const newWin = window.open(fileURL, '_blank');
                        // add a load listener to the window so that the title gets changed on page load
                        newWin.addEventListener('load', function() {
                            newWin.document.title = result.replace(/"/g, '');
                            // this.router.navigate(['/report/buy']);
                        });
                    }
                });
            this.toastr.success(
                this.translate.instant('ebwebApp.mBCreditCard.printing') + this.translate.instant('ebwebApp.mBCreditCard.financialPaper'),
                this.translate.instant('ebwebApp.mBCreditCard.message')
            );
        }
    }

    calcular(objcet1: object, typeID: number, name: string, objectParent: object, objectDT1: object[], objectDT2: object[]) {
        if (this.isCreateUrl) {
            const count = this.mBCreditCardDetailTax.length;
            this.utilService.calcular(
                objcet1,
                typeID,
                name,
                objectParent,
                objectDT1,
                objectDT2,
                this.currentAccount,
                this.getAmountOriginalType(),
                this.currency.formula.includes('*') ? this.mBCreditCard.exchangeRate : 1 / this.mBCreditCard.exchangeRate
            );
            console.log(objectDT2.length);
            if (count < objectDT2.length) {
                if (this.currentAccount) {
                    this.mBCreditCardDetailTax[count].goodsServicePurchaseID = this.currentAccount.organizationUnit.goodsServicePurchaseID;
                }
            }
        }
        if (name === 'amountOriginal') {
            this.selectChangeTotalAmount();
        }
    }

    // region Tiến lùi chứng từ
    // ham lui, tien
    previousEdit() {
        // goi service get by row num
        if (this.rowNum !== this.totalItems) {
            this.utilService
                .findByRowNum({
                    id: this.mBCreditCard.id,
                    isNext: false,
                    typeID: this.TYPE_MB_CREDIT_CARD,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMBCreditCard>) => {
                        // this.OnInit(res.body);
                        // this.router.navigate(['/mc-payment', res.body.id, 'edit']);
                        // this.router.navigateByUrl('/', {skipLocationChange: true}).then(()=>
                        //     this.router.navigate(['/mc-payment', this.mCPayment.id, 'edit']));
                        // this.router.navigateByUrl('/mc-payment/' + this.mCPayment.id + '/edit');
                        this.navigate(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    nextEdit() {
        // goi service get by row num
        if (this.rowNum !== 1) {
            this.utilService
                .findByRowNum({
                    id: this.mBCreditCard.id,
                    isNext: true,
                    typeID: this.TYPE_MB_CREDIT_CARD,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMBCreditCard>) => {
                        // this.router.navigate(['/mc-payment', res.body.id, 'edit']);
                        this.navigate(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    navigate(imbCreditCard: IMBCreditCard) {
        switch (imbCreditCard.typeID) {
            case this.TYPE_MB_CREDIT_CARD:
            case this.TYPE_MB_CREDIT_CARD_NCC:
                this.router.navigate(['/mb-credit-card', imbCreditCard.id, 'edit']);
                break;
            case this.TYPE_MB_CREDIT_CARD_MUA_DV:
                this.mBCreditCardService.find(imbCreditCard.id).subscribe((res: HttpResponse<any>) => {
                    const ppServiceID = res.body.ppServiceID;
                    if (ppServiceID) {
                        this.router.navigate(['./mua-dich-vu', ppServiceID, 'edit', 'from-mb-credit-card', imbCreditCard.id]);
                    }
                });
                break;
            case this.TYPE_MB_CREDIT_CARD_MUA_HANG:
                this.mBCreditCardService.find(imbCreditCard.id).subscribe((res: HttpResponse<any>) => {
                    const ppInvocieID = res.body.ppInvocieID;
                    if (ppInvocieID) {
                        if (res.body.storedInRepository) {
                            this.router.navigate(['./mua-hang', 'qua-kho', ppInvocieID, 'edit', 'from-mb-credit-card', imbCreditCard.id]);
                        } else {
                            this.router.navigate([
                                './mua-hang',
                                'khong-qua-kho',
                                ppInvocieID,
                                'edit',
                                'from-mb-credit-card',
                                imbCreditCard.id
                            ]);
                        }
                    }
                });
                break;
        }
    }

    private onError(errorMessage: string) {
        this.toastr.error(this.translate.instant('ebwebApp.mBCreditCard.error'), this.translate.instant('ebwebApp.mBCreditCard.message'));
    }

    onRightClick($event, data, selectedData, isNew, isDelete, select, currentRow) {
        if (this.isCreateUrl) {
            this.contextMenu.isNew = isNew;
            this.contextMenu.isDelete = isDelete;
            this.contextMenu.isShow = true;
            this.contextMenu.event = $event;
            this.contextMenu.data = data;
            this.contextMenu.selectedData = selectedData;
            if (select === 0 || select === 1) {
                this.contextMenu.isCopy = true;
            } else {
                this.contextMenu.isCopy = false;
            }
            this.select = select;
            this.currentRow = currentRow;
        }
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    selectChangeCreditCardNumber() {
        const iCreditCard = this.creditCards.find(creditCard => creditCard.id === this.mBCreditCard.creditCardID);
        if (iCreditCard) {
            this.mBCreditCard.creditCardNumber = iCreditCard.creditCardNumber;
            this.creditCardType = iCreditCard.creditCardType;
            this.ownerCard = iCreditCard.ownerCard;
        } else {
            this.creditCardType = '';
            this.ownerCard = '';
        }
    }

    selectChangeAccountingObjectBankAccount() {
        if (this.accountingObjectBankAccounts && this.accountingObjectBankAccounts.length > 0) {
            const iaccountingObjectBankAccount = this.accountingObjectBankAccounts.find(
                accountingObjectBankAccount => accountingObjectBankAccount.id === this.mBCreditCard.accountingObjectBankAccountDetailID
            );
            if (iaccountingObjectBankAccount) {
                this.mBCreditCard.accountingObjectBankName = iaccountingObjectBankAccount.bankName;
            } else {
                this.mBCreditCard.accountingObjectBankName = '';
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TheTinDung_Xoa])
    delete() {
        if (!this.isCreateUrl && !this.checkCloseBook(this.currentAccount, this.mBCreditCard.postedDate) && !this.utilService.isShowPopup) {
            if (this.mBCreditCard.recorded) {
                // this.toastr.error(
                //     this.translate.instant('ebwebApp.mBCreditCard.errorDeleteVoucherNo'),
                //     this.translate.instant('ebwebApp.mBCreditCard.message')
                // );
                return;
            } else {
                this.copy();
                this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
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

    fillToSave() {
        let totalVatAmount = 0,
            totalVatAmountOriginal = 0;
        for (let i = 0; i < this.mBCreditCard.mbCreditCardDetailTax.length; i++) {
            this.mBCreditCard.mbCreditCardDetailTax[i].pretaxAmountOriginal = this.round(
                this.mBCreditCard.mbCreditCardDetailTax[i].pretaxAmount /
                    (this.currentCurrency.formula.includes('*') ? this.mBCreditCard.exchangeRate : 1 / this.mBCreditCard.exchangeRate),
                8
            );
            this.mBCreditCard.mbCreditCardDetailTax[i].vATAmountOriginal = this.round(
                this.mBCreditCard.mbCreditCardDetailTax[i].vATAmount /
                    (this.currentCurrency.formula.includes('*') ? this.mBCreditCard.exchangeRate : 1 / this.mBCreditCard.exchangeRate),
                8
            );
            this.mBCreditCard.mbCreditCardDetailTax[i].orderPriority = this.i + 1;
        }
        for (let i = 0; i < this.mBCreditCardDetailTax.length; i++) {
            totalVatAmount += this.mBCreditCardDetailTax[i].vATAmount;
            totalVatAmountOriginal += this.mBCreditCardDetailTax[i].vATAmountOriginal;
        }
        if (this.mBCreditCard.typeLedger === 0) {
            this.mBCreditCard.noMBook = null;
            this.mBCreditCard.noFBook = this.no;
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
        // check is url new
        // if (this.isCreateUrl && this.mBCreditCard.id) {
        //     this.mBCreditCard.id = undefined;
        // }
        // if (!this.isCreateUrl && !this.isEditUrl) {
        //     this.mBCreditCard.id = undefined;
        //     for (let i = 0; i < this.mBCreditCard.mBCreditCardDetails.length; i++) {
        //         this.mBCreditCard.mBCreditCardDetails[i].id = undefined;
        //     }
        //     for (let i = 0; i < this.mBCreditCard.mbCreditCardDetailTax.length; i++) {
        //         this.mBCreditCard.mbCreditCardDetailTax[i].id = undefined;
        //     }
        // }
        // if (!this.isCreateUrl && !this.isEditUrl) {
        //     this.mBCreditCard.id = undefined;
        // }
        if (!this.mBCreditCard.typeID) {
            this.mBCreditCard.typeID = 170;
        }
        this.mBCreditCard.totalVATAmount = totalVatAmount;
        this.mBCreditCard.totalVATAmountOriginal = totalVatAmountOriginal;
        this.mBCreditCard.mBCreditCardDetails = this.mBCreditCardDetail;
        this.mBCreditCard.mbCreditCardDetailTax = this.mBCreditCardDetailTax;
        this.isSaving = true;
        // if (this.mBCreditCardDetailTax.length > 0) {
        //     if (this.isForeignCurrency()) {
        //         if (totalVatAmount === this.mBCreditCard.totalAmount) {
        //         } else {
        //             this.modalRef = this.modalService.open(this.modalComponent, {backdrop: 'static'});
        //             return;
        //         }
        //     } else {
        //         if (totalVatAmountOriginal === this.mBCreditCard.totalAmountOriginal) {
        //         } else {
        //             this.modalRef = this.modalService.open(this.modalComponent, {backdrop: 'static'});
        //             return;
        //         }
        //     }
        // }
        this.isCreateUrl = !this.isCreateUrl;
        this.isEdit = this.isCreateUrl;
        this.disableAddButton = true;
        // if (this.sysRecord.data === '0') {
        //     this.mBCreditCard.recorded = true;
        // } else {
        //     this.mBCreditCard.recorded = false;
        // }
        // if (this.mBCreditCardDetailTax) {
        //     for (let i = 0; i < this.mBCreditCardDetailTax.length; i++) {
        //         this.mBCreditCardDetailTax[i].invoiceDate = moment(this.mBCreditCardDetailTax[i].invoiceDate);
        //     }
        // }
        for (this.i = 0; this.i < this.mBCreditCard.mBCreditCardDetails.length; this.i++) {
            this.mBCreditCard.mBCreditCardDetails[this.i].orderPriority = this.i + 1;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TheTinDung_Them])
    saveAndNew() {
        event.preventDefault();
        if (this.isCreateUrl && !this.utilService.isShowPopup) {
            this.warningMessage = '';
            this.mBCreditCard.mBCreditCardDetails = this.mBCreditCardDetail;
            this.mBCreditCard.mbCreditCardDetailTax = this.mBCreditCardDetailTax;
            if (this.isCreateUrl && !this.utilService.isShowPopup) {
                sessionStorage.removeItem('saveAndLoad');
                if (this.checkError()) {
                    this.fillToSave();
                    this.copy();
                    if (this.mBCreditCard.id !== undefined) {
                        this.subscribeToSaveResponseAndContinue(this.mBCreditCardService.update(this.mBCreditCard));
                    } else {
                        this.subscribeToSaveResponseAndContinue(this.mBCreditCardService.create(this.mBCreditCard));
                    }
                } else {
                }
            }
        }
    }

    private subscribeToSaveResponseAndContinue(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    if (this.isEditUrl) {
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBDeposit.editSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    } else {
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBDeposit.insertSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    this.onSaveSuccess();
                    this.isEdit = this.isCreateUrl = true;
                    // this.mBCreditCard = {};
                    // this.mBCreditCard.typeID = 170;
                    // this.mBCreditCardDetail = [];
                    // this.mBCreditCardDetailTax = [];
                    // this.viewVouchersSelected = [];
                    // this.autoPrincipleName = null;
                    // this.creditCardType = null;
                    // this.ownerCard = null;
                    // this.isReadOnly = true;
                    // this.translate
                    //     .get([
                    //         'ebwebApp.mBCreditCard.defaultReason',
                    //         'ebwebApp.mBDeposit.supplier',
                    //         'ebwebApp.mBDeposit.customer',
                    //         'ebwebApp.mBDeposit.employee',
                    //         'ebwebApp.mBDeposit.other'
                    //     ])
                    //     .subscribe(res => {
                    //         this.mBCreditCard.reason = res['ebwebApp.mBCreditCard.defaultReason'];
                    //         this.lstAccountingObjectTypes = [
                    //             {value: 0, name: res['ebwebApp.mBDeposit.supplier']},
                    //             {value: 1, name: res['ebwebApp.mBDeposit.customer']},
                    //             {value: 2, name: res['ebwebApp.mBDeposit.employee']},
                    //             {value: 3, name: res['ebwebApp.mBDeposit.other']}
                    //         ];
                    //     });
                    this.copy();
                    this.router.navigate(['/mb-credit-card', res.body.mbCreditCard.id, 'edit']).then(() => {
                        this.router.navigate(['/mb-credit-card', 'new']);
                    });
                    // if (this.TCKHAC_SDSoQuanTri === '0') {
                    //     this.mBCreditCard.typeLedger = 0;
                    //     this.sysTypeLedger = 0;
                    //     this.isHideTypeLedger = true;
                    // } else {
                    //     if (this.isSoTaiChinh) {
                    //         this.mBCreditCard.typeLedger = 2;
                    //         this.sysTypeLedger = 0;
                    //     } else {
                    //         this.mBCreditCard.typeLedger = 2;
                    //         this.sysTypeLedger = 1;
                    //     }
                    // }
                    // this.mBCreditCard.date = this.utilService.ngayHachToan(this.currentAccount);
                    // this.mBCreditCard.postedDate = this.mBCreditCard.date;
                    // this.mBCreditCard.accountingObjectType = 1;
                    // this.getObjectType(1);
                    // this.copy();
                    // this.selectChangeExchangeRate();
                    // this.utilService
                    //     .getGenCodeVoucher({
                    //         typeGroupID: 17, // typeGroupID loại chứng từ
                    //         companyID: '', // ID công ty
                    //         branchID: '', // ID chi nhánh
                    //         displayOnBook: this.sysTypeLedger // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
                    //     })
                    //     .subscribe((res2: HttpResponse<string>) => {
                    //         // this.mCReceipt.noFBook = (res.body.toString());
                    //         this.no = res2.body;
                    //         this.mBCreditCard.currencyID = this.currency.currencyCode;
                    //         this.selectChangeCurrency();
                    //         this.copy();
                    //     });
                    // this.statusVoucher = 0;
                } else if (res.body.status === 1) {
                    this.noVoucherExist();

                    return;
                } else if (res.body.status === 2) {
                    if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.mBCreditCard.id = res.body.mbCreditCard.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyQT'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.copy();
                        this.router.navigate(['/mb-credit-card', res.body.mbCreditCard.id, 'edit']).then(() => {
                            this.router.navigate(['/mb-credit-card', 'new']);
                        });
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.mBCreditCard.id = res.body.mbCreditCard.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyTC'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.copy();
                        this.router.navigate(['/mb-credit-card', res.body.mbCreditCard.id, 'edit']).then(() => {
                            this.router.navigate(['/mb-credit-card', 'new']);
                        });
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.mBCreditCard.id = res.body.mbCreditCard.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuy'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.copy();
                        this.router.navigate(['/mb-credit-card', res.body.mbCreditCard.id, 'edit']).then(() => {
                            this.router.navigate(['/mb-credit-card', 'new']);
                        });
                    } else {
                        this.recordFailed();
                    }
                }
            },
            (res: HttpErrorResponse) => {
                this.onSaveError();
                this.isEdit = this.isCreateUrl = true;
            }
        );
    }

    registerRef() {
        this.eventSubscriber = this.eventManager.subscribe('selectViewVoucher', response => {
            if (this.isCreateUrl) {
                this.viewVouchersSelected = response.content;
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    sumAfterDeleteByContextMenu() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            if (this.isCreateUrl) {
                if (this.select === 0) {
                    if (this.mBCreditCardDetail.length > 1) {
                        for (let i = 0; i < this.mBCreditCardDetail.length; i++) {
                            if (i !== this.currentRow) {
                                if (this.mBCreditCardDetail[i].debitAccount === this.mBCreditCardDetail[this.currentRow].debitAccount) {
                                    this.mBCreditCardDetail.splice(this.currentRow, 1);
                                    return;
                                }
                            }
                        }
                        for (let j = 0; j < this.mBCreditCardDetailTax.length; j++) {
                            if (this.mBCreditCardDetailTax[j].vATAccount === this.mBCreditCardDetail[this.currentRow].debitAccount) {
                                this.mBCreditCardDetailTax.splice(j, 1);
                            }
                        }
                    }
                    this.mBCreditCardDetail.splice(this.currentRow, 1);
                } else if (this.select === 1) {
                    this.mBCreditCardDetailTax.splice(this.currentRow, 1);
                } else {
                    this.viewVouchersSelected.splice(this.currentRow, 1);
                    this.select = null;
                }
                let sumTotalAmountOriginal = 0;
                let sumTotalAmount = 0;
                for (let i = 0; i < this.mBCreditCardDetail.length; i++) {
                    sumTotalAmount += this.mBCreditCardDetail[i].amount;
                    sumTotalAmountOriginal += this.mBCreditCardDetail[i].amountOriginal;
                    this.utilService.calcular(
                        this.mBCreditCardDetail[i],
                        170,
                        'amountOriginal',
                        this.mBCreditCard,
                        this.mBCreditCardDetail,
                        this.mBCreditCardDetailTax,
                        this.currentAccount,
                        this.getAmountOriginalType(),
                        this.currency.formula.includes('*') ? this.mBCreditCard.exchangeRate : 1 / this.mBCreditCard.exchangeRate
                    );
                }
                this.mBCreditCard.totalAmount = sumTotalAmount;
                this.mBCreditCard.totalAmountOriginal = sumTotalAmountOriginal;
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    public beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            if (this.isCreateUrl) {
                this.modalRef = this.refModalService.open(this.viewVouchersSelected);
            }
        }
    }

    getTotalVATAmount() {
        if (this.mBCreditCardDetailTax && this.mBCreditCardDetailTax.length > 0) {
            return this.mBCreditCardDetailTax.map(n => n.vATAmount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getAmountOriginal() {
        if (this.mBCreditCardDetail && this.mBCreditCardDetail.length > 0) {
            return this.mBCreditCardDetail.map(n => n.amountOriginal).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getAmount() {
        if (this.mBCreditCardDetail && this.mBCreditCardDetail.length > 0) {
            return this.mBCreditCardDetail.map(n => n.amount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getVATAmount() {
        if (this.mBCreditCardDetailTax && this.mBCreditCardDetailTax.length > 0) {
            return this.mBCreditCardDetailTax.map(n => n.vATAmount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getPretaxAmount() {
        if (this.mBCreditCardDetailTax && this.mBCreditCardDetailTax.length > 0) {
            return this.mBCreditCardDetailTax.map(n => n.pretaxAmount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    closeAll() {
        if (sessionStorage.getItem('dataSourceMBCreditCard')) {
            this.router.navigate(['/mb-credit-card', 'hasSearch', '1']);
        } else {
            this.router.navigate(['/mb-credit-card']);
        }
    }

    saveContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.save();
        // this.closeAll();
    }

    close() {
        this.copy();
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.closeAll();
    }

    closeModalSaveComponent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    copy() {
        this.mBCreditCardCopy = Object.assign({}, this.mBCreditCard);
        if (this.mBCreditCardDetail) {
            this.mBCreditCardDetailsCopy = this.mBCreditCardDetail.map(object => ({ ...object }));
        } else {
            this.mBCreditCardDetailsCopy = [];
        }
        if (this.mBCreditCardDetailTax) {
            this.mBCreditCardDetailTaxsCopy = this.mBCreditCardDetailTax.map(object => ({ ...object }));
        } else {
            this.mBCreditCardDetailTaxsCopy = [];
        }
        if (this.viewVouchersSelected) {
            this.viewVouchersSelectedCopy = this.viewVouchersSelected.map(object => ({ ...object }));
        } else {
            this.viewVouchersSelectedCopy = [];
        }
    }

    noVoucherExist() {
        this.toastr.error(
            this.translate.instant('global.data.noVocherAlreadyExist'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
    }

    exit() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    continueSave() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.fillToSave();
        if (this.mBCreditCard.id) {
            this.subscribeToSaveResponse(this.mBCreditCardService.update(this.mBCreditCard));
        } else {
            this.subscribeToSaveResponse(this.mBCreditCardService.create(this.mBCreditCard));
        }
        this.modalRef.close();
    }

    /*
* hàm ss du lieu 2 object và 2 mảng object
* return true: neu tat ca giong nhau
* return fale: neu 1 trong cac object ko giong nhau
* */
    canDeactive(): boolean {
        if (this.isReadOnly || this.isReadOnly === undefined) {
            return true;
        } else {
            return (
                this.utilService.isEquivalent(this.mBCreditCard, this.mBCreditCardCopy) &&
                this.utilService.isEquivalentArray(this.mBCreditCardDetail, this.mBCreditCardDetailsCopy) &&
                this.utilService.isEquivalentArray(this.mBCreditCardDetailTax, this.mBCreditCardDetailTaxsCopy)
            );
        }
    }

    keyDownAddRow(value: number, select: number) {
        if (!this.getSelectionText()) {
            if (select === 0) {
                if (value !== null && value !== undefined) {
                    const ob: IMBCreditCardDetails = Object.assign({}, this.mBCreditCardDetail[value]);
                    ob.id = undefined;
                    ob.orderPriority = undefined;
                    this.mBCreditCardDetail.push(ob);
                } else {
                    this.mBCreditCardDetail.push({});
                }
            } else if (select === 1) {
                if (value !== null && value !== undefined) {
                    const ob: IMBCreditCardDetailTax = Object.assign({}, this.mBCreditCardDetailTax[value]);
                    ob.id = undefined;
                    ob.orderPriority = undefined;
                    this.mBCreditCardDetailTax.push(ob);
                } else {
                    this.mBCreditCardDetailTax.push({});
                }
            }
            this.selectChangeTotalAmount();
        }
    }

    afterCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            if (this.select === 0) {
                const ob: IMBCreditCardDetails = Object.assign({}, this.contextMenu.selectedData);
                ob.id = undefined;
                ob.orderPriority = undefined;
                this.mBCreditCardDetail.push(ob);
                this.selectChangeTotalAmount();
            } else if (this.select === 1) {
                const ob: IMBCreditCardDetailTax = Object.assign({}, this.contextMenu.selectedData);
                ob.id = undefined;
                ob.orderPriority = undefined;
                this.mBCreditCardDetailTax.push(ob);
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    afterAddRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            if (this.select === 0) {
                this.mBCreditCardDetail.push({ description: this.mBCreditCard.reason, amount: 0, amountOriginal: 0 });
            } else if (this.select === 1) {
                this.mBCreditCardDetailTax.push({ description: 'Thuế GTGT', vATAmount: 0 });
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    selectChangeTotalAmount() {
        if (this.isCreateUrl) {
            let totalAmount = 0;
            let totalAmountOriginal = 0;
            if (this.mBCreditCard.currencyID) {
                const currentCurrency = this.currencies.find(iCurrency => iCurrency.currencyCode === this.mBCreditCard.currencyID);
                if (currentCurrency) {
                    for (let i = 0; i < this.mBCreditCardDetail.length; i++) {
                        totalAmountOriginal += this.mBCreditCardDetail[i].amountOriginal;
                        if (currentCurrency.formula === '*') {
                            this.mBCreditCardDetail[i].amount = this.utilService.round(
                                this.mBCreditCardDetail[i].amountOriginal * this.mBCreditCard.exchangeRate,
                                this.currentAccount.systemOption,
                                7
                            );
                        } else {
                            this.mBCreditCardDetail[i].amount = this.utilService.round(
                                this.mBCreditCardDetail[i].amountOriginal / this.mBCreditCard.exchangeRate,
                                this.currentAccount.systemOption,
                                7
                            );
                        }
                        totalAmount += this.mBCreditCardDetail[i].amount;
                    }
                    this.mBCreditCard.totalAmountOriginal = totalAmountOriginal;
                    this.mBCreditCard.totalAmount = totalAmount;
                }
            }
        }
    }

    vATAccountChange() {
        this.updateAmountTaxWithAccount();
    }

    updateAmountTaxWithAccount() {
        for (let i = 0; i < this.mBCreditCardDetailTax.length; i++) {
            if (this.mBCreditCardDetailTax[i].vATAccount && this.mBCreditCardDetailTax[i].vATAccount.startsWith('133', 0)) {
                if (!this.mBCreditCardDetail.find(n => n.debitAccount === this.mBCreditCardDetailTax[i].vATAccount)) {
                    this.mBCreditCardDetailTax[i].pretaxAmount = 0;
                    this.mBCreditCardDetailTax[i].vATAmount = 0;
                    this.mBCreditCardDetailTax[i].vATAmountOriginal = 0;
                } else {
                    this.utilService.calcular(
                        this.mBCreditCardDetail[i],
                        170,
                        'amountOriginal',
                        this.mBCreditCard,
                        this.mBCreditCardDetail,
                        this.mBCreditCardDetailTax,
                        this.currentAccount,
                        this.getAmountOriginalType(),
                        this.currency.formula.includes('*') ? this.mBCreditCard.exchangeRate : 1 / this.mBCreditCard.exchangeRate
                    );
                }
            }
        }
    }

    sumMBCreditCardDetailVendors(prop) {
        let total = 0;
        for (let i = 0; i < this.mBCreditCardDetailVendors.length; i++) {
            total += this.mBCreditCardDetailVendors[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    viewVoucher(imbCreditCardDetailVendor: IMBCreditCardDetailVendor) {
        let url = '';
        if (imbCreditCardDetailVendor.voucherTypeID === this.TYPE_PP_INVOICE) {
            this.viewVoucherService.checkViaStockPPInvoice({ id: imbCreditCardDetailVendor.pPInvoiceID }).subscribe(
                (res: HttpResponse<any>) => {
                    if (res.body.message === UpdateDataMessages.STOCK_TRUE) {
                        url = `/#/mua-hang/qua-kho-ref/${imbCreditCardDetailVendor.pPInvoiceID}/edit/1`;
                        window.open(url, '_blank');
                    } else if (res.body.message === UpdateDataMessages.STOCK_FALSE) {
                        url = `/#/mua-hang/khong-qua-kho-ref/${imbCreditCardDetailVendor.pPInvoiceID}/edit/1`;
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
        } else if (imbCreditCardDetailVendor.voucherTypeID === this.TYPE_PP_SERVICE) {
            url = `/#/pp-service/${imbCreditCardDetailVendor.pPInvoiceID}/edit/from-ref`;
        } else if (imbCreditCardDetailVendor.voucherTypeID === this.TYPE_PP_DISCOUNT_RETURN) {
            url = `/#/pp-discount-return/${imbCreditCardDetailVendor.pPInvoiceID}/edit/from-ref`;
        } else if (imbCreditCardDetailVendor.voucherTypeID === this.TYPE_FA_INCREAMENT) {
            url = `/#/fa-increament/${imbCreditCardDetailVendor.pPInvoiceID}/edit/from-ref`;
        } else if (imbCreditCardDetailVendor.voucherTypeID === this.TYPE_TI_INCREAMENT) {
            url = `/#/ti-increament/${imbCreditCardDetailVendor.pPInvoiceID}/edit/from-ref`;
        } else if (imbCreditCardDetailVendor.voucherTypeID === TypeID.CHUNG_TU_NGHIEP_VU_KHAC) {
            url = `/#/g-other-voucher/${imbCreditCardDetailVendor.pPInvoiceID}/edit/from-ref`;
        }
        if (url) {
            window.open(url, '_blank');
        }
    }

    getEmployeeByID(id) {
        if (this.accounting) {
            const epl = this.accounting.find(n => n.id === id);
            if (epl) {
                return epl.accountingObjectCode;
            } else {
                return '';
            }
        }
    }

    ngAfterViewInit(): void {
        if (this.isCreateUrl) {
            this.focusFirstInput();
        }
    }

    round(value, type) {
        if (type === 8) {
            if (this.isForeignCurrency()) {
                return this.utilService.round(value, this.currentAccount.systemOption, type);
            } else {
                return this.utilService.round(value, this.currentAccount.systemOption, 7);
            }
        } else if (type === 2) {
            if (this.isForeignCurrency()) {
                return this.utilService.round(value, this.currentAccount.systemOption, type);
            } else {
                return this.utilService.round(value, this.currentAccount.systemOption, 1);
            }
        } else {
            return this.utilService.round(value, this.currentAccount.systemOption, type);
        }
    }

    isForeignCurrency() {
        return this.currentAccount && this.mBCreditCard.currencyID !== this.currentAccount.organizationUnit.currencyID;
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    selectChangeAccountingObjectTax(i) {
        if (this.mBCreditCardDetailTax[i].accountingObjectID && this.accountingObjects) {
            const currentAccountingObject = this.accounting.find(a => a.id === this.mBCreditCardDetailTax[i].accountingObjectID);
            this.mBCreditCardDetailTax[i].accountingObjectName = currentAccountingObject.accountingObjectName;
            this.mBCreditCardDetailTax[i].taxCode = currentAccountingObject.taxCode;
        }
    }

    // Loại đối tượng kế toán
    selectAccountingObjectType() {
        if (this.mBCreditCard.accountingObjectType !== null && this.mBCreditCard.accountingObjectType !== undefined && this.accounting) {
            if (this.mBCreditCard.accountingObjectType === 0) {
                // Nhà cung cấp
                this.nameCategory = this.utilService.NHA_CUNG_CAP;
                this.accountingObjects = this.accounting.filter(n => n.objectType === 0 || n.objectType === 2);
            } else if (this.mBCreditCard.accountingObjectType === 1) {
                // Khách hàng
                this.nameCategory = this.utilService.KHACH_HANG;
                this.accountingObjects = this.accounting.filter(n => n.objectType === 1 || n.objectType === 2);
            } else if (this.mBCreditCard.accountingObjectType === 2) {
                // Nhân viên
                this.nameCategory = this.utilService.NHAN_VIEN;
                this.accountingObjects = this.accounting.filter(n => n.isEmployee);
            } else if (this.mBCreditCard.accountingObjectType === 3) {
                // Khác
                this.nameCategory = this.utilService.KHACH_HANG;
                this.accountingObjects = this.accounting.filter(n => n.objectType === 3);
            } else {
                this.nameCategory = this.utilService.KHACH_HANG;
                this.accountingObjects = this.accounting;
            }
        }
        if (this.accountingObjects && this.accountingObjects.length > 0) {
            if (this.mBCreditCard.accountingObjectID) {
                if (!this.accountingObjects.map(n => n.id).includes(this.mBCreditCard.accountingObjectID)) {
                    this.mBCreditCard.accountingObjectID = null;
                    this.mBCreditCard.accountingObjectName = null;
                    this.mBCreditCard.accountingObjectAddress = null;
                }
            }
        }
    }

    saveDetails(i, isAccountingObject?: boolean) {
        this.currentRow = i;
        if (isAccountingObject) {
            const iAccountingObject = this.accounting.find(a => a.id === this.mBCreditCardDetail[i].accountingObjectID);
            if (iAccountingObject) {
                if (!this.autoPrinciple) {
                    this.translate.get(['ebwebApp.mBTellerPaper.defaultReason']).subscribe(res2 => {
                        this.mBCreditCardDetail[i].description =
                            res2['ebwebApp.mBTellerPaper.defaultReason'] + ' từ ' + iAccountingObject.accountingObjectName;
                    });
                }
            }
        }
        this.details = this.mBCreditCardDetail;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.mBCreditCard;
    }

    addDataToDetail() {
        this.mBCreditCardDetail = this.details ? this.details : this.mBCreditCardDetail;
        this.mBCreditCard = this.parent ? this.parent : this.mBCreditCard;
    }

    selectChangeDate() {
        if (!this.isReadOnly) {
            if (this.mBCreditCard.date) {
                this.mBCreditCard.postedDate = this.mBCreditCard.date;
            }
        }
    }

    continueDelete() {
        this.mBCreditCardService.delete(this.mBCreditCard.id).subscribe(response => {
            this.modalRef.close();
        });
        this.toastr.success(this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'));
        this.router.navigate(['mb-credit-card']);
    }

    closePopUpDelete() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }
}
