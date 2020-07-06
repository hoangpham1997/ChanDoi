import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';

import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { MBTellerPaperService } from './mb-teller-paper.service';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { IMBTellerPaperDetails, MBTellerPaperDetails } from 'app/shared/model/mb-teller-paper-details.model';
import { IMBTellerPaperDetailTax, MBTellerPaperDetailTax } from 'app/shared/model/mb-teller-paper-detail-tax.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { AccountingObjectBankAccountService } from 'app/entities/accounting-object-bank-account';
import { MBTellerPaperDetailsService } from 'app/TienMatNganHang/BaoNo/mb-teller-paper-details';
import { MBTellerPaperDetailTaxService } from 'app/TienMatNganHang/BaoNo/mb-teller-paper-detail-tax';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ExpenseItemService } from 'app/entities/expense-item';
import { CostSetService } from 'app/entities/cost-set';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IAccountDefault } from 'app/shared/model/account-default.model';
import { AccountDefaultService } from 'app/danhmuc/account-default';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { EMContractService } from 'app/entities/em-contract';
import { ToastrService } from 'ngx-toastr';
import { TypeService } from 'app/entities/type';
import { IType } from 'app/shared/model/type.model';
import { BudgetItemService } from 'app/entities/budget-item';
import { Principal } from 'app/core';
import {
    AccountType,
    CategoryName,
    DDSo_NgoaiTe,
    DDSo_TienVND,
    DDSo_TyGia,
    DDSo_TyLe,
    MSGERROR,
    REPORT,
    SO_LAM_VIEC,
    TCKHAC_SDSoQuanTri,
    TypeID
} from 'app/app.constants';
import { ROLE } from 'app/role.constants';
import { TranslateService } from '@ngx-translate/core';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { AccountListService } from 'app/danhmuc/account-list';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { IAccountAllList } from 'app/shared/model/account-all-list.model';
import { IRefVoucher } from 'app/shared/model/ref-voucher.model';
import { PPInvoiceService } from 'app/muahang/mua_hang_qua_kho';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { IMBTellerPaperDetailVendor } from 'app/shared/model/mb-teller-paper-detail-vendor.model';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { IMCPaymentDetailTax } from 'app/shared/model/mc-payment-detail-tax.model';
import { IMaterialQuantumDetails } from 'app/shared/model/material-quantum-details.model';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { IMBDepositDetails } from 'app/shared/model/mb-deposit-details.model';
import { InvoiceType } from 'app/shared/model/invoice-type.model';
import { IAReportService } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/ia-report.service';

@Component({
    selector: 'eb-mb-teller-paper-update',
    templateUrl: './mb-teller-paper-update.component.html',
    styleUrls: ['./mb-teller-paper-update.component.css']
})
export class MBTellerPaperUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewInit {
    @ViewChild('content1') public modalComponent: NgbModalRef;
    @ViewChild('content') public modalCloseComponent: NgbModalRef;
    private _mBTellerPaper: IMBTellerPaper;
    isSaving: boolean;
    dateDp: any;
    posteddateDp: any;
    issuedateDp: any;
    matchdateDp: any;
    select: number;
    contextMenu: ContextMenu;
    mBTellerPaperDetails: IMBTellerPaperDetails[];
    mBTellerPaperDetailTaxs: IMBTellerPaperDetailTax[];
    autoPrinciples: IAutoPrinciple[];
    accountDefault: { value?: string };
    accountDefaults: any[];
    autoPrinciple: IAutoPrinciple;
    goodsServicePurchases: IGoodsServicePurchase[];
    accountingObjectBankAccounts: IAccountingObjectBankAccount[];
    isRecord: boolean;
    eMContracts: IEMContract[];
    currencies: ICurrency[];
    isReadOnly: boolean;
    totalvatamount: number;
    totalvatamountoriginal: number;
    isCreateUrl: boolean;
    record_: Irecord;
    isCurrencyVND: boolean;
    // right click declare
    // contextmenuX = {value: 0};
    // contextmenuY = {value: 0};
    // selectedDetail = {value: new MBTellerPaperDetails()};
    // selectedDetailTax = {value: new MBTellerPaperDetailTax()};
    // isShowDetail = {value: false};
    // isShowDetailTax = {value: false};
    //  data storage provider
    page: number;
    itemsPerPage: number;
    pageCount: number;
    totalItems: number;
    rowNum: number;
    // sort
    predicate: any;
    reverse: any;
    searchVoucher: string;
    dataSession: IDataSessionStorage;
    // end sort
    count: any;
    types: IType[];
    strTypeName: string;
    typeGroupID: number;
    currentAccount: any;
    isShowTypeLedger: boolean;
    isShowOption1: boolean;
    isShowOption2: boolean;
    viewVouchersSelected: any;
    modalRef: NgbModalRef;
    eventSubscriber: Subscription;
    mBTellerPaperCopy: IMBTellerPaper;
    mBTellerPaperDetailsCopy: IMBTellerPaperDetails[];
    mBTellerPaperDetailTaxsCopy: IMBTellerPaperDetailTax[];
    dataTCKHAC_SDSoQuanTri: number;
    dataSO_LAM_VIEC: number;
    displayBook: number;
    creditAccountList: any;
    debitAccountList: any;
    vatAccountList: any;
    DDSo_NgoaiTe = DDSo_NgoaiTe;
    DDSo_TienVND = DDSo_TienVND;
    DDSo_TyLe = DDSo_TyLe;
    DDSo_TyGia = DDSo_TyGia;
    returnValue: Boolean;
    typeReport: any;
    isCheckCanDeactive: boolean;
    isCheckTotal: boolean;
    accountingObjectData: IAccountingObject[];
    dataSearchVoucher: ISearchVoucher;
    columnList = [
        { column: AccountType.TK_CO, ppType: false },
        { column: AccountType.TK_NO, ppType: false },
        { column: AccountType.TK_THUE_GTGT, ppType: false },
        { column: AccountType.TKDU_THUE_GTGT, ppType: false }
    ];
    currencyCode: any;
    ppInvoiceDetails: any;
    ppInvoice: any;
    vatRates: any;
    isFromRef: boolean;
    nameCategory: any;
    accounting: IAccountingObject[];
    iAutoPrinciple: IAutoPrinciple;
    invoiceTypes: InvoiceType[];
    // role
    ROLE_BaoNo = ROLE.BaoNo;
    ROLE_BaoNo_Xem = ROLE.BaoNo_Xem;
    ROLE_BaoNo_Them = ROLE.BaoNo_Them;
    ROLE_BaoNo_Sua = ROLE.BaoNo_Sua;
    ROLE_BaoNo_Xoa = ROLE.BaoNo_Xoa;
    ROLE_BaoNo_GhiSo = ROLE.BaoNo_GhiSo;
    ROLE_BaoNo_In = ROLE.BaoNo_In;
    currency: ICurrency;
    TYPE_BAONO_UNC = 120;
    TYPE_BAONO_SCK = 130;
    TYPE_BAONO_STM = 140;
    TYPE_UNC_PPINVOICE_MHQK = 125;
    TYPE_SCK_PPINVOICE_MHQK = 131;
    TYPE_STM_PPINVOICE_MHQK = 141;
    TYPE_UNC_PPSERVICE = 124;
    TYPE_SCK_PPSERVICE = 133;
    TYPE_STM_PPSERVICE = 143;
    TYPE_UNC_TRA_TIEN_NCC = 126;
    TYPE_SCK_TRA_TIEN_NCC = 134;
    TYPE_STM_TRA_TIEN_NCC = 144;
    TYPE_PP_DISCOUNT_RETURN = 230;
    TYPE_FA_INCREAMENT = 610;
    TYPE_TI_INCREAMENT = 510;
    TYPE_PP_INVOICE = 210;
    TYPE_PP_SERVICE = 240;
    mBTellerPaperDetailVendors: IMBTellerPaperDetailVendor[];
    isSoTaiChinh: boolean;
    reasonDefault = 'Chi tiền từ TKNH';
    isChangedReason: boolean;
    REPORT = REPORT;
    currentRow: number;
    isShowGoodsServicePurchase: boolean;
    backUpAccountingObjectID: string;

    constructor(
        private mBTellerPaperService: MBTellerPaperService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private currencyService: CurrencyService,
        private bankAccountDetailService: BankAccountDetailsService,
        private autoPrincipleService: AutoPrincipleService,
        private accountingObjectService: AccountingObjectService,
        private accountingObjectBankAccountService: AccountingObjectBankAccountService,
        private mBTellerPaperDetailsService: MBTellerPaperDetailsService,
        private mBTellerPaperDetailTaxService: MBTellerPaperDetailTaxService,
        private expenseItemService: ExpenseItemService,
        private costSetService: CostSetService,
        private eMContractService: EMContractService,
        private statisticsCodeService: StatisticsCodeService,
        private organizationUnitService: OrganizationUnitService,
        private accountDefaultService: AccountDefaultService,
        private gLService: GeneralLedgerService,
        private jhiAlertService: JhiAlertService,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private typeService: TypeService,
        private budgetItemService: BudgetItemService,
        private principal: Principal,
        private refModalService: RefModalService,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        private accountListService: AccountListService,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        private pPInvoiceService: PPInvoiceService,
        private viewVoucherService: ViewVoucherService,
        private iaReportService: IAReportService
    ) {
        super();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (account.organizationUnit.taxCalculationMethod === 0) {
                this.isShowGoodsServicePurchase = true;
            } else {
                this.isShowGoodsServicePurchase = false;
            }
            this.getData(account);
            this.dataTCKHAC_SDSoQuanTri = +this.currentAccount.systemOption.find(x => x.code === TCKHAC_SDSoQuanTri).data;
            this.dataSO_LAM_VIEC = +this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data;
            this.displayBook = this.dataTCKHAC_SDSoQuanTri === 0 ? 0 : this.dataSO_LAM_VIEC;
            this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
            this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                this.currencies = res.body.sort((a, b) => (a.currencyCode > b.currencyCode ? 1 : -1));
                this.currencyCode = this.currentAccount.organizationUnit.currencyID;
                this.currency = this.currencies.find(n => n.currencyCode === this.currencyCode);
                // this.selectedChangedCurrency();
            });
        });
        this.iaReportService.queryInvoiceType().subscribe(res => {
            this.invoiceTypes = res.body;
        });
        this.contextMenu = new ContextMenu();
        this.dataSearchVoucher = JSON.parse(sessionStorage.getItem('sessionSearchVoucher'));
        this.searchVoucher = this.dataSearchVoucher ? sessionStorage.getItem('sessionSearchVoucher') : null;
        this.vatRates = [
            { value: 0, name: '0%' },
            { value: 1, name: '5%' },
            { value: 2, name: '10%' },
            { value: 3, name: 'Không chịu thuế' },
            { value: 4, name: 'Không tính thuế' }
        ];
    }

    ngOnInit() {
        this.isSaving = false;
        this.isCheckTotal = true;
        this.isChangedReason = false;
        this.activatedRoute.data.subscribe(({ mBTellerPaper }) => {
            this.mBTellerPaper = mBTellerPaper;
            this.backUpAccountingObjectID = this.mBTellerPaper.accountingObjectID;
            this.viewVouchersSelected = mBTellerPaper.viewVouchers ? mBTellerPaper.viewVouchers : [];
            this.mBTellerPaperDetails = this.mBTellerPaper.mBTellerPaperDetails
                ? this.mBTellerPaper.mBTellerPaperDetails.sort((a, b) => (a.orderPriority > b.orderPriority ? 1 : -1))
                : [];
            this.mBTellerPaperDetailTaxs = this.mBTellerPaper.mBTellerPaperDetailTaxs
                ? this.mBTellerPaper.mBTellerPaperDetailTaxs.sort((a, b) => (a.orderPriority > b.orderPriority ? 1 : -1))
                : [];
            this.isRecord = this.mBTellerPaper.recorded ? this.mBTellerPaper.recorded : false;
            // check url new Return True or false
            this.isCreateUrl = window.location.href.includes('/mb-teller-paper/new');
            this.isFromRef = this.router.url.includes('/from-ref');
            // isReadOnly
            if (this.mBTellerPaper.id && !this.isCreateUrl) {
                this.isReadOnly = true;
                this.isCheckCanDeactive = false;
            } else {
                this.isReadOnly = false;
                this.mBTellerPaper.recorded = false;
                this.isCheckCanDeactive = true;
            }
            if (this.mBTellerPaper.id !== undefined) {
                if (this.dataTCKHAC_SDSoQuanTri === 0) {
                    this.isShowTypeLedger = false;
                    this.mBTellerPaper.typeLedger = 0;
                } else {
                    if (this.dataSO_LAM_VIEC === 0) {
                        this.isShowOption1 = true;
                        this.isShowOption2 = false;
                    } else {
                        this.isShowOption1 = false;
                        this.isShowOption2 = true;
                    }
                    this.isShowTypeLedger = true;
                }
                if (this.isCreateUrl) {
                    if (this.mBTellerPaper.typeId === 120) {
                        this.typeGroupID = 12;
                    } else if (this.mBTellerPaper.typeId === 130) {
                        this.typeGroupID = 13;
                    } else if (this.mBTellerPaper.typeId === 140) {
                        this.typeGroupID = 14;
                    }
                    this.utilsService
                        .getGenCodeVoucher({
                            typeGroupID: this.typeGroupID,
                            displayOnBook: this.displayBook
                        })
                        .subscribe((res: HttpResponse<string>) => {
                            console.log(res.body);
                            if (+this.mBTellerPaper.typeLedger !== 2) {
                                // khong Su dung ca 2 SO
                                if (this.displayBook === 0) {
                                    this.mBTellerPaper.noFBook = res.body;
                                    this.mBTellerPaper.noMBook = null;
                                } else {
                                    this.mBTellerPaper.noFBook = null;
                                    this.mBTellerPaper.noMBook = res.body;
                                }
                            } else {
                                // su dung ca 2 SO
                                if (this.dataSO_LAM_VIEC === 0) {
                                    this.mBTellerPaper.noFBook = res.body;
                                    this.mBTellerPaper.noMBook = null;
                                } else {
                                    this.mBTellerPaper.noMBook = res.body;
                                    this.mBTellerPaper.noFBook = null;
                                }
                            }
                        });
                } else {
                    // this.loadDetailsIsPPInvoice();
                    this.mBTellerPaperDetailVendors =
                        this.mBTellerPaper.mBTellerPaperDetailVendor === undefined
                            ? []
                            : this.mBTellerPaper.mBTellerPaperDetailVendor.sort((a, b) => a.orderPriority - b.orderPriority);
                    // convert date from server
                    this.mBTellerPaper.mBTellerPaperDetailTaxs.forEach((mBTellerPaperDetailTax: IMBTellerPaperDetailTax) => {
                        mBTellerPaperDetailTax.invoiceDate =
                            mBTellerPaperDetailTax.invoiceDate != null ? moment(mBTellerPaperDetailTax.invoiceDate) : null;
                    });
                    this.copyObject();
                    console.log('detailTaxs: ' + JSON.stringify(this.mBTellerPaperDetailTaxs));
                    this.mBTellerPaperService
                        .getIndexRow({
                            id: this.mBTellerPaper.id,
                            searchVoucher: this.searchVoucher ? this.searchVoucher : null
                        })
                        .subscribe(
                            (res: any) => {
                                this.rowNum = res.body[0];
                                this.totalItems = res.body[1];
                            },
                            (res: HttpErrorResponse) => this.onError(res.message)
                        );
                }
            } else {
                if (this.currentAccount) {
                    this.mBTellerPaper.date = this.utilsService.ngayHachToan(this.currentAccount);
                    this.mBTellerPaper.postedDate = this.mBTellerPaper.date;
                    this.mBTellerPaper.typeId = this.TYPE_BAONO_UNC;
                    this.getReasonDefault();
                    if (this.dataTCKHAC_SDSoQuanTri === 0) {
                        this.isShowTypeLedger = false;
                        this.mBTellerPaper.typeLedger = 0;
                    } else {
                        this.mBTellerPaper.typeLedger = 2;
                        if (this.dataSO_LAM_VIEC === 0) {
                            this.isShowOption1 = true;
                            this.isShowOption2 = false;
                        } else {
                            this.isShowOption1 = false;
                            this.isShowOption2 = true;
                        }
                        this.isShowTypeLedger = true;
                    }
                    this.currencyCode = this.currentAccount.organizationUnit.currencyID;
                    if (this.currentAccount.organizationUnit && this.currentAccount.organizationUnit.currencyID) {
                        this.mBTellerPaper.currencyId = this.currencyCode;
                        this.mBTellerPaper.exchangeRate = 1;
                    }
                    this.mBTellerPaperDetailsCopy = this.mBTellerPaperDetails.map(object => ({ ...object }));
                    this.mBTellerPaperDetailTaxsCopy = this.mBTellerPaperDetailTaxs.map(object => ({ ...object }));
                    this.isRecord = false;
                    this.mBTellerPaperDetailVendors = [];
                    this.utilsService
                        .getGenCodeVoucher({
                            typeGroupID: 12,
                            displayOnBook: this.displayBook
                        })
                        .subscribe((res: HttpResponse<string>) => {
                            console.log(res.body);
                            if (+this.mBTellerPaper.typeLedger !== 2) {
                                // khong Su dung ca 2 SO
                                if (this.displayBook === 0) {
                                    this.mBTellerPaper.noFBook = res.body;
                                } else {
                                    this.mBTellerPaper.noMBook = res.body;
                                }
                            } else {
                                // su dung ca 2 SO
                                if (this.dataSO_LAM_VIEC === 0) {
                                    this.mBTellerPaper.noFBook = res.body;
                                } else {
                                    this.mBTellerPaper.noMBook = res.body;
                                }
                            }
                            this.mBTellerPaperCopy = Object.assign({}, this.mBTellerPaper);
                        });
                }
            }
            this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
                this.types = res.body.filter(
                    x => x.id === this.TYPE_BAONO_UNC || x.id === this.TYPE_BAONO_SCK || x.id === this.TYPE_BAONO_STM
                );
                if (this.mBTellerPaper) {
                    const typeSelected: IType = this.types.find(x => x.id === this.mBTellerPaper.typeId);
                    if (typeSelected) {
                        this.strTypeName = typeSelected.typeName;
                    }
                } else {
                    this.strTypeName = 'Ủy nhiệm chi';
                    this.mBTellerPaper.typeId = this.TYPE_BAONO_UNC;
                }
                // load autoprinciple theo TypeID
                this.autoPrincipleService.getAutoPrinciples().subscribe((res2: HttpResponse<IAutoPrinciple[]>) => {
                    this.autoPrinciples = res2.body
                        .filter(aPrinciple => aPrinciple.typeId === this.TYPE_BAONO_UNC || aPrinciple.typeId === 0)
                        .sort((n1, n2) => {
                            if (n1.typeId > n2.typeId) {
                                return 1;
                            }
                            if (n1.typeId < n2.typeId) {
                                return -1;
                            }
                            return 0;
                        });
                });
            });
            this.mBTellerPaper.accountingObjectType = this.mBTellerPaper.accountingObjectType ? this.mBTellerPaper.accountingObjectType : 0;
            this.selectedChangedAccountingObjectType();
            // load accountingObject theo objecttype & isActive =1
            this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
                this.employees = res.body
                    .filter(x => x.isEmployee)
                    .sort((a, b) => (a.accountingObjectCode > b.accountingObjectCode ? 1 : -1));
                this.accounting = res.body;
                // nha cung cap = 0
                if (+this.mBTellerPaper.accountingObjectType === 0) {
                    this.accountingObjects = res.body
                        .filter(x => x.objectType === 0 || x.objectType === 2)
                        .sort((a, b) => (a.accountingObjectCode > b.accountingObjectCode ? 1 : -1));
                } else if (+this.mBTellerPaper.accountingObjectType === 1) {
                    // khach hang = 1
                    this.accountingObjects = res.body
                        .filter(x => x.objectType === 1 || x.objectType === 2)
                        .sort((a, b) => (a.accountingObjectCode > b.accountingObjectCode ? 1 : -1));
                } else if (+this.mBTellerPaper.accountingObjectType === 2) {
                    // nhan vien = 2,
                    this.accountingObjects = res.body
                        .filter(x => x.isEmployee)
                        .sort((a, b) => (a.accountingObjectCode > b.accountingObjectCode ? 1 : -1));
                } else if (+this.mBTellerPaper.accountingObjectType === 3) {
                    //  khac = 3
                    this.accountingObjects = res.body
                        .filter(x => x.objectType === 3)
                        .sort((a, b) => (a.accountingObjectCode > b.accountingObjectCode ? 1 : -1));
                }
                // neu co id accountingObject moi load accountingObjectBankAccount
                if (this.mBTellerPaper.accountingObjectID) {
                    this.accountingObjectBankAccountService
                        .getByAccountingObjectById({ accountingObjectID: this.mBTellerPaper.accountingObjectID })
                        .subscribe((res2: HttpResponse<IAccountingObjectBankAccount[]>) => {
                            this.accountingObjectBankAccounts = res2.body;
                        });
                } else {
                    this.accountingObjectBankAccounts = [];
                }
            });
        });
        // load bankaccountdetail theo isActive=== true
        this.bankAccountDetailService.getBankAccountDetailsNotCreditCard().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
            if (this.isCreateUrl) {
                this.bankAccountDetails = res.body.filter(a => a.isActive);
            } else {
                this.bankAccountDetails = res.body;
            }
            this.selectedChangedBankAccountDetails();
        });
        this.budgetItemService.getBudgetItems().subscribe((res: HttpResponse<IBudgetItem[]>) => {
            this.budgetItems = res.body;
        });
        this.expenseItemService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body;
        });
        this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body;
        });
        this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
            this.eMContracts = res.body;
        });
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticCodes = res.body;
        });
        this.organizationUnitService.getOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body.sort((a, b) => a.organizationUnitCode.localeCompare(b.organizationUnitCode));
        });
        this.goodsServicePurchaseService.getGoodServicePurchases().subscribe((res: HttpResponse<IGoodsServicePurchase[]>) => {
            this.goodsServicePurchases = res.body;
        });
        this.accountDefaults = [];
        this.accountDefaultService.getAllAccountDefaults().subscribe((res: HttpResponse<IAccountDefault[]>) => {
            // this.accountDefaults = res.body;
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

        // this.mBTellerPaper.noMBook = this.mBTellerPaper.noFBook;
        this.mBTellerPaper.typeId = this.mBTellerPaper.typeId ? this.mBTellerPaper.typeId : 120;
        // this.mBTellerPaper.typeLedger = this.mBTellerPaper.typeLedger ? this.mBTellerPaper.typeLedger : 2;
        this.strTypeName = this.strTypeName ? this.strTypeName : 'Ủy nhiệm chi';

        this.autoPrinciple = this.autoPrinciple ? this.autoPrinciple : null;
        this.registerRef();
        this.getDataAccount();
        this.sumAfterDeleteByContextMenu();
        this.afterAddRow();
        this.registerCopyRow();
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.selectedChangedAccountingObject();
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.selectedChangedAccountingObject();
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilsService.setShowPopup(response.content);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        // this.registerIsShowPopup();
        // console.log(this.nameCategory);
    }

    // registerIsShowPopup() {
    //     this.utilsService.checkEvent.subscribe(res => {
    //         this.isShowPopup = res;
    //     });
    // }

    getReasonDefault() {
        if (
            this.mBTellerPaper.typeId === this.TYPE_BAONO_UNC ||
            this.mBTellerPaper.typeId === this.TYPE_BAONO_SCK ||
            this.mBTellerPaper.typeId === this.TYPE_BAONO_STM
        ) {
            if (this.mBTellerPaper.accountingObjectID) {
                const name = this.accounting.find(a => a.id === this.mBTellerPaper.accountingObjectID);
                this.translate.get(['ebwebApp.mBTellerPaper.defaultReason']).subscribe(res2 => {
                    this.mBTellerPaper.reason = res2['ebwebApp.mBTellerPaper.defaultReason'] + ' cho ' + name.accountingObjectName;
                });
            } else {
                if (this.isCreateUrl) {
                    this.translate.get(['ebwebApp.mBTellerPaper.defaultReason']).subscribe(res2 => {
                        this.mBTellerPaper.reason = res2['ebwebApp.mBTellerPaper.defaultReason'];
                    });
                }
            }
        } else {
            this.mBTellerPaper.reason = '';
        }
    }

    getData(account) {
        this.DDSo_TyGia = account.systemOption.find(x => x.code === DDSo_TyGia).data;
        this.DDSo_TienVND = account.systemOption.find(x => x.code === DDSo_TienVND).data;
        this.DDSo_NgoaiTe = account.systemOption.find(x => x.code === DDSo_NgoaiTe).data;
    }

    getDataAccount() {
        // let type;
        // if(this.mBTellerPaper.typeId === this.TYPE_UNC_TRA_TIEN_NCC){
        //     type = this.TYPE_UNC_TRA_TIEN_NCC;
        // }
        // else if(this.mBTellerPaper.typeId === this.TYPE_SCK_TRA_TIEN_NCC){
        //     type = this.TYPE_SCK_TRA_TIEN_NCC;
        // }
        // else if(this.mBTellerPaper.typeId === this.TYPE_STM_TRA_TIEN_NCC){
        //     type = this.TYPE_STM_TRA_TIEN_NCC
        // }
        // if (this.mBTellerPaper && this.mBTellerPaper.id && (this.mBTellerPaper.typeId === this.TYPE_UNC_TRA_TIEN_NCC ||
        //     this.mBTellerPaper.typeId === this.TYPE_SCK_TRA_TIEN_NCC || this.mBTellerPaper.typeId === this.TYPE_STM_TRA_TIEN_NCC)) {
        const param = {
            typeID: this.mBTellerPaper.typeId,
            columnName: this.columnList
        };
        this.accountListService.getAccountTypeFour(param).subscribe(res => {
            const dataAccount: IAccountAllList = res.body;
            this.creditAccountList = dataAccount.creditAccount;
            this.debitAccountList = dataAccount.debitAccount;
            this.vatAccountList = dataAccount.vatAccount;
            this.mBTellerPaperDetails.forEach(item => {
                item.creditAccountItem = this.creditAccountList.find(n => n.accountNumber === item.creditAccount);
                item.debitAccountItem = this.debitAccountList.find(n => n.accountNumber === item.debitAccount);
            });
        });
        // }
    }

    registerRef() {
        this.eventSubscriber = this.eventManager.subscribe('selectViewVoucher', response => {
            if (!this.isReadOnly) {
                this.viewVouchersSelected = response.content;
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    previousState() {
        if (this.isCreateUrl) {
            this.router.navigate(['mb-teller-paper']);
        } else {
            window.history.back();
        }
    }

    previousEdit() {
        if (this.rowNum === this.totalItems) {
            return;
        }
        this.rowNum++;
        this.mBTellerPaperService
            .findByRowNum({
                searchVoucher: this.searchVoucher ? this.searchVoucher : null,
                rowNum: this.rowNum
            })
            .subscribe(
                (res: HttpResponse<IMBTellerPaper>) => {
                    this.mBTellerPaper = res.body;
                    this.mBTellerPaperDetails = this.mBTellerPaper.mBTellerPaperDetails;
                    this.mBTellerPaperDetailTaxs = this.mBTellerPaper.mBTellerPaperDetailTaxs;
                    this.mBTellerPaperService.find(this.mBTellerPaper.id).subscribe((resD: HttpResponse<IMBTellerPaper>) => {
                        this.viewVouchersSelected = resD.body.viewVouchers;
                    });
                    this.copyObject();
                    this.router.navigate(['/mb-teller-paper', this.mBTellerPaper.id, 'edit']);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    nextEdit() {
        if (this.rowNum === 1) {
            return;
        }
        this.rowNum--;
        this.mBTellerPaperService
            .findByRowNum({
                searchVoucher: this.searchVoucher ? this.searchVoucher : null,
                rowNum: this.rowNum
            })
            .subscribe(
                (res: HttpResponse<IMBTellerPaper>) => {
                    this.mBTellerPaper = res.body;
                    this.mBTellerPaperDetails = this.mBTellerPaper.mBTellerPaperDetails;
                    this.mBTellerPaperDetailTaxs = this.mBTellerPaper.mBTellerPaperDetailTaxs;
                    this.mBTellerPaperService.find(this.mBTellerPaper.id).subscribe((resD: HttpResponse<IMBTellerPaper>) => {
                        this.viewVouchersSelected = resD.body.viewVouchers;
                    });
                    this.copyObject();
                    this.router.navigate(['/mb-teller-paper', this.mBTellerPaper.id, 'edit']);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoNo_Them, ROLE.BaoNo_Sua])
    save(isNew = false) {
        event.preventDefault();
        if (!this.mBTellerPaper.recorded && !this.isReadOnly && !this.utilsService.isShowPopup) {
            this.fillToSave();
            if (this.checkError()) {
                if (!this.checkTotalAmountEqualsTotalVATAmount() && this.isCheckTotal) {
                    this.modalRef = this.modalService.open(this.modalComponent, { backdrop: 'static' });
                    this.isCheckTotal = false;
                    return;
                }
                if (this.mBTellerPaper.id !== undefined) {
                    this.subscribeToSaveResponse(this.mBTellerPaperService.update(this.mBTellerPaper));
                } else {
                    this.subscribeToSaveResponse(this.mBTellerPaperService.create(this.mBTellerPaper));
                }
            } else {
                this.closeModal();
            }
        } else {
            // this.toastr.warning(
            //     this.translate.instant('ebwebApp.mBTellerPaper.warning.content'),
            //     this.translate.instant('ebwebApp.mBTellerPaper.warning.title')
            // );
        }
    }

    checkTotalAmountEqualsTotalVATAmount(): boolean {
        if (this.mBTellerPaperDetailTaxs.length > 0) {
            return this.mBTellerPaper.totalVatAmount === this.mBTellerPaper.totalAmount;
        }
        return true;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoNo_Them])
    saveAndNew() {
        event.preventDefault();
        if (!this.mBTellerPaper.recorded && !this.isReadOnly && !this.utilsService.isShowPopup) {
            this.fillToSave();
            if (this.checkError()) {
                if (this.mBTellerPaper.id !== undefined) {
                    this.subscribeToSaveResponseAndContinue(this.mBTellerPaperService.update(this.mBTellerPaper));
                } else {
                    this.subscribeToSaveResponseAndContinue(this.mBTellerPaperService.create(this.mBTellerPaper));
                }
            } else {
                this.closeModal();
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoNo_Them])
    copyAndNew() {
        event.preventDefault();
        if (!this.isCreateUrl && this.isReadOnly && !this.utilsService.isShowPopup) {
            this.copyObject();
            this.router.navigate(['mb-teller-paper/new', { id: this.mBTellerPaper.id }]);
        }
    }

    fillToSave() {
        this.isSaving = true;
        this.mBTellerPaper.totalAmountOriginal = this.getTotalAmountOriginal();
        this.mBTellerPaper.totalAmount = this.getTotalAmount();
        this.mBTellerPaper.totalVatAmountOriginal = this.getTotalAmountOriginal();
        this.mBTellerPaper.totalVatAmount = this.getTotalVATAmount();
        this.mBTellerPaper.mBTellerPaperDetails = this.mBTellerPaperDetails;
        this.mBTellerPaper.mBTellerPaperDetailTaxs = this.mBTellerPaperDetailTaxs;
        this.mBTellerPaper.viewVouchers = this.viewVouchersSelected;
        console.log('before save: ' + JSON.stringify(this.mBTellerPaper.mBTellerPaperDetailTaxs));

        for (let i = 0; i < this.mBTellerPaper.mBTellerPaperDetails.length; i++) {
            this.mBTellerPaper.mBTellerPaperDetails[i].orderPriority = i + 1;
        }
        for (let i = 0; i < this.mBTellerPaper.mBTellerPaperDetailTaxs.length; i++) {
            this.mBTellerPaper.mBTellerPaperDetailTaxs[i].orderPriority = i + 1;
        }
        // for (let i = 0; i < this.mBTellerPaper.mBTellerPaperDetailTaxs.length; i++) {
        //     this.mBTellerPaper.mBTellerPaperDetailTaxs[i].vATAmountOriginal = 99999;
        // }
        // check is url new
        if (this.isCreateUrl) {
            this.mBTellerPaper.id = undefined;
        }
        // if (this.mBTellerPaper.id) {
        //     // convert date from client to server
        //     for (let i = 0; i < this.mBTellerPaper.mBTellerPaperDetailTaxs.length; i++) {
        //         this.mBTellerPaper.mBTellerPaperDetailTaxs[i] = this.convertDateFromClientDTax(this.mBTellerPaperDetailTaxs[i]);
        //     }
        // }
    }

    // convert date from client
    private convertDateFromClientDTax(mBTellerPaperDetailTax: IMBTellerPaperDetailTax): IMBTellerPaperDetailTax {
        const copy: IMBTellerPaperDetailTax = Object.assign({}, mBTellerPaperDetailTax, {
            invoiceDate:
                mBTellerPaperDetailTax.invoiceDate != null && mBTellerPaperDetailTax.invoiceDate.isValid()
                    ? mBTellerPaperDetailTax.invoiceDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    checkNoFMBookRequired(): boolean {
        const noBook = this.displayBook === 0 ? this.mBTellerPaper.noFBook : this.mBTellerPaper.noMBook;
        if (!noBook) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.nullNo'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        } else {
            return true;
        }
    }

    checkError(): boolean {
        if (this.mBTellerPaperDetails.length === 0) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.nullDetail'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        } else if (this.displayBook === 0 && this.mBTellerPaper.noFBook === '') {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.nullBook'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        } else if (this.displayBook === 1 && this.mBTellerPaper.noMBook === '') {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.nullBook'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        } else if (this.checkCloseBook(this.currentAccount, this.mBTellerPaper.postedDate)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.lockBook.err.checkForSave'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        } else if (this.mBTellerPaper.date === undefined || this.mBTellerPaper.date === null) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.nullDate'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        } else if (this.mBTellerPaper.postedDate === undefined || this.mBTellerPaper.postedDate === null) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.nullPostedDate'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        } else if (!this.mBTellerPaper.currencyId || this.mBTellerPaper.currencyId === '') {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBDeposit.detail.currencyType') +
                    ' ' +
                    this.translate.instant('ebwebApp.mBDeposit.isNotNull'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        } else if (this.mBTellerPaper.totalAmount === undefined || this.mBTellerPaper.totalVatAmount === null) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.nullTotalAmount'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        } else if (this.mBTellerPaper.postedDate < this.mBTellerPaper.date) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.dateGreaterPostedDate'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        } else if (this.mBTellerPaper.taxCode) {
            if (!this.utilsService.checkMST(this.mBTellerPaper.taxCode)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mBTellerPaper.error.taxCodeInvalid'),
                    this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                );
                return false;
            }
        } else if (
            !this.utilsService.checkNoBook(
                this.mBTellerPaper.noFBook ? this.mBTellerPaper.noFBook : this.mBTellerPaper.noMBook,
                this.currentAccount
            )
        ) {
            return false;
        }
        // const dt = this.mBTellerPaperDetails.find(
        //     n => n.creditAccount === null || n.creditAccount === undefined || n.debitAccount === undefined || n.debitAccount === null
        // );
        // const dtT = this.mBTellerPaperDetailTaxs.find(n => n.vATAccount === null || n.vATAccount === undefined);
        // if (dt || dtT) {
        //     this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
        //     return false;
        // }
        const checkAcc = this.utilsService.checkAccoutWithDetailType(
            this.debitAccountList,
            this.creditAccountList,
            this.mBTellerPaperDetails,
            this.accountingObjects,
            this.costSets,
            this.eMContracts,
            null,
            null,
            this.organizationUnits,
            this.expenseItems,
            this.budgetItems,
            this.statisticCodes
        );
        if (checkAcc) {
            this.toastr.error(checkAcc, this.translate.instant('ebwebApp.mBTellerPaper.error.error'));
            return false;
        }
        if (this.mBTellerPaperDetails.length > 0) {
            if (this.checkDebitAccount()) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mBTellerPaper.error.debitAccountRequired'),
                    this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                );
                return false;
            }
            if (this.checkCreditAccount()) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mBTellerPaper.error.creditAccountRequired'),
                    this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                );
                return false;
            }
            for (let i = 0; i < this.mBTellerPaperDetails.length; i++) {
                const detailTypeCre = this.creditAccountList.find(a => a.accountNumber === this.mBTellerPaperDetails[i].creditAccount)
                    .detailType;
                const detailTypeDeb = this.debitAccountList.find(a => a.accountNumber === this.mBTellerPaperDetails[i].debitAccount)
                    .detailType;
                if (detailTypeCre === '6') {
                    if (!this.mBTellerPaper.bankAccountDetailID) {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.mBTellerPaper.error.bankAccountNotNullMessage') +
                                this.translate.instant('ebwebApp.mBTellerPaper.error.bankAccount') +
                                this.mBTellerPaperDetails[i].creditAccount +
                                this.translate.instant('ebwebApp.mBTellerPaper.error.isBankAccount'),
                            this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                        );
                        return;
                    }
                }
                if (detailTypeDeb === '6') {
                    if (!this.mBTellerPaper.bankAccountDetailID) {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.mBTellerPaper.error.bankAccountNotNullMessage') +
                                this.translate.instant('ebwebApp.mBTellerPaper.error.bankAccount') +
                                this.mBTellerPaperDetails[i].debitAccount +
                                this.translate.instant('ebwebApp.mBTellerPaper.error.isBankAccount'),
                            this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                        );
                        return false;
                    }
                }
            }
        }
        if (this.checkTax()) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.nullvATAccountTax'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        }
        return true;
    }

    checkTax() {
        if (this.mBTellerPaperDetailTaxs.length > 0) {
            return this.mBTellerPaperDetailTaxs.some(n => n.vATAccount === undefined || n.vATAccount === null);
        }
        return false;
    }

    checkDebitAccount(): Boolean {
        return this.mBTellerPaperDetails.some(n => n.debitAccount === undefined || n.debitAccount === null);
    }

    checkCreditAccount(): Boolean {
        return this.mBTellerPaperDetails.some(n => n.creditAccount === undefined || n.creditAccount === null);
    }

    private noVoucherExist() {
        this.toastr.error(
            this.translate.instant('global.data.noVocherAlreadyExist'),
            this.translate.instant('ebwebApp.mBTellerPaper.error.error')
        );
    }

    copyObject() {
        this.mBTellerPaperDetails = this.mBTellerPaper.id ? this.mBTellerPaper.mBTellerPaperDetails : this.mBTellerPaperDetails;
        this.mBTellerPaperDetailTaxs = this.mBTellerPaper.id ? this.mBTellerPaper.mBTellerPaperDetailTaxs : this.mBTellerPaperDetailTaxs;
        this.mBTellerPaperCopy = Object.assign({}, this.mBTellerPaper);
        this.mBTellerPaperDetailsCopy = this.mBTellerPaperDetails.map(object => ({ ...object }));
        this.mBTellerPaperDetailTaxsCopy = this.mBTellerPaperDetailTaxs.map(object => ({ ...object }));
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMBTellerPaper>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.mBTellerPaper = this.convertDateFromServer(res.body.mBTellerPaper);
                    console.log('after save: ' + JSON.stringify(this.mBTellerPaper.mBTellerPaperDetailTaxs));
                    this.isReadOnly = true;
                    this.isCheckCanDeactive = false;
                    this.closeModal();
                    this.router.navigate(['/mb-teller-paper', res.body.mBTellerPaper.id, 'edit']);
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                    this.closeModal();
                } else if (res.body.status === 2) {
                    if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.isSaving = false;
                        this.mBTellerPaper.id = res.body.mBTellerPaper.id;
                        this.mBTellerPaper.recorded = res.body.recorded;
                        this.copyObject();
                        this.isReadOnly = true;
                        this.router.navigate(['./mb-teller-paper', this.mBTellerPaper.id, 'edit']);
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyQT'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.isSaving = false;
                        this.mBTellerPaper.id = res.body.mBTellerPaper.id;
                        this.mBTellerPaper.recorded = res.body.recorded;
                        this.copyObject();
                        this.isReadOnly = true;
                        this.router.navigate(['./mb-teller-paper', this.mBTellerPaper.id, 'edit']);
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyTC'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.isSaving = false;
                        this.mBTellerPaper.id = res.body.mBTellerPaper.id;
                        this.mBTellerPaper.recorded = res.body.recorded;
                        this.copyObject();
                        this.isReadOnly = true;
                        this.router.navigate(['./mb-teller-paper', this.mBTellerPaper.id, 'edit']);
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuy'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        // this.recordFailed();
                    } else if (res.body.status === 3) {
                        this.updateGenCodeFailed();
                        this.closeModal();
                    }
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private recordFailed() {
        this.toastr.error(this.translate.instant('global.data.recordFailed'), this.translate.instant('ebwebApp.mBTellerPaper.error.error'));
    }

    private updateGenCodeFailed() {
        this.toastr.error(
            this.translate.instant('ebwebApp.mBTellerPaper.error.noGenCodeUpdatefailed'),
            this.translate.instant('ebwebApp.mBTellerPaper.error.error')
        );
    }

    private subscribeToSaveResponseAndContinue(result: Observable<HttpResponse<IMBTellerPaper>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.mBTellerPaper = this.convertDateFromServer(res.body.mBTellerPaper);
                    this.isCheckCanDeactive = false;
                    this.router.navigate(['/mb-teller-paper', res.body.mBTellerPaper.id, 'edit']).then(() => {
                        this.isCheckCanDeactive = false;
                        this.router.navigate(['/mb-teller-paper', 'new']);
                    });
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                } else if (res.body.status === 2) {
                    if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.isSaving = false;
                        this.mBTellerPaper.id = res.body.mBTellerPaper.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyQT'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.copyObject();
                        this.router.navigate(['/mb-teller-paper', res.body.mBTellerPaper.id, 'edit']).then(() => {
                            this.router.navigate(['/mb-teller-paper', 'new']);
                        });
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.isSaving = false;
                        this.mBTellerPaper.id = res.body.mBTellerPaper.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyTC'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.copyObject();
                        this.router.navigate(['/mb-teller-paper', res.body.mBTellerPaper.id, 'edit']).then(() => {
                            this.router.navigate(['/mb-teller-paper', 'new']);
                        });
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.isSaving = false;
                        this.mBTellerPaper.id = res.body.mBTellerPaper.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuy'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.copyObject();
                        this.router.navigate(['/mb-teller-paper', res.body.mBTellerPaper.id, 'edit']).then(() => {
                            this.router.navigate(['/mb-teller-paper', 'new']);
                        });
                    } else {
                        this.recordFailed();
                    }
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private convertDateFromServer(res: any): any {
        res.date = res.date != null ? moment(res.date) : null;
        res.postedDate = res.postedDate != null ? moment(res.postedDate) : null;
        res.issueDate = res.issueDate != null ? moment(res.issueDate) : null;
        res.matchDate = res.matchDate != null ? moment(res.matchDate) : null;
        return res;
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.isReadOnly = true;
        if (this.mBTellerPaper.id) {
            this.toastr.success(
                this.translate.instant('ebwebApp.sAQuote.updated'),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
        } else {
            this.toastr.success(
                this.translate.instant('ebwebApp.sAQuote.created'),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
        }
    }

    private onSaveError() {
        this.isSaving = false;
    }

    // event thay doi select option value
    selectedChangedBankAccountDetails() {
        const bankAcc: IBankAccountDetails = this.bankAccountDetails.find(x => x.id === this.mBTellerPaper.bankAccountDetailID);
        if (bankAcc) {
            this.mBTellerPaper.bankName = bankAcc.bankName;
        }
        // console.log(this.mBTellerPaper.bankaccountdetails);
    }

    selectedChangedAutoPrinciple() {
        if (this.autoPrinciple) {
            this.iAutoPrinciple = this.autoPrinciples.find(a => a === this.autoPrinciple);
            const checkDebitAccount = this.debitAccountList.find(a => a.accountNumber === this.iAutoPrinciple.debitAccount);
            const checkCreditAccount = this.creditAccountList.find(a => a.accountNumber === this.iAutoPrinciple.creditAccount);
            this.mBTellerPaper.reason = this.autoPrinciple.autoPrincipleName;
            for (const dt of this.mBTellerPaperDetails) {
                dt.description = this.autoPrinciple.autoPrincipleName;
                if (checkDebitAccount) {
                    dt.debitAccount = this.iAutoPrinciple.debitAccount;
                } else {
                    dt.debitAccount = null;
                }
                if (checkCreditAccount) {
                    dt.creditAccount = this.iAutoPrinciple.creditAccount;
                } else {
                    dt.creditAccount = null;
                }
                dt.description = this.iAutoPrinciple.autoPrincipleName;
            }
        }
    }

    selectedChangedAccountingObject() {
        if (this.mBTellerPaper.accountingObjectID) {
            const currentAccountingObject = this.accounting.find(a => a.id === this.mBTellerPaper.accountingObjectID);
            this.mBTellerPaper.accountingObjectName = currentAccountingObject ? currentAccountingObject.accountingObjectName : '';
            this.mBTellerPaper.accountingObjectAddress = currentAccountingObject ? currentAccountingObject.accountingObjectAddress : '';
            this.mBTellerPaper.receiver = currentAccountingObject ? currentAccountingObject.contactName : '';
            this.mBTellerPaper.identificationNo = currentAccountingObject ? currentAccountingObject.identificationNo : '';
            this.mBTellerPaper.issueDate = currentAccountingObject ? moment(currentAccountingObject.issueDate) : null;
            this.mBTellerPaper.issueBy = currentAccountingObject ? currentAccountingObject.issueBy : null;
            if (this.isCreateUrl && !this.autoPrinciple && this.mBTellerPaper.accountingObjectName) {
                this.getReasonDefault();
            }
            // neu co id accountingObject moi load accountingObjectBankAccount
            if (this.mBTellerPaper.accountingObjectID) {
                this.accountingObjectBankAccountService
                    .getByAccountingObjectById({ accountingObjectID: this.mBTellerPaper.accountingObjectID })
                    .subscribe((res: HttpResponse<IAccountingObjectBankAccount[]>) => {
                        this.accountingObjectBankAccounts = res.body;
                    });
            } else {
                this.accountingObjectBankAccounts = [];
            }
            // load accountingObject cho mbtellerpaperdetail
            for (const mBTellerPaperDetail of this.mBTellerPaperDetails) {
                if (mBTellerPaperDetail.accountingObjectID === this.backUpAccountingObjectID) {
                    mBTellerPaperDetail.accountingObjectID = this.mBTellerPaper.accountingObjectID;
                    if (!this.autoPrinciple) {
                        mBTellerPaperDetail.description =
                            'Thu tiền bằng TGNH ' +
                            (this.mBTellerPaper.accountingObjectName ? 'từ ' + this.mBTellerPaper.accountingObjectName : '');
                    }
                }
            }
            const acc = this.accountingObjects.find(a => a.id === this.mBTellerPaper.accountingObjectID);
            for (const mBTellerPaperDetailTax of this.mBTellerPaperDetailTaxs) {
                mBTellerPaperDetailTax.accountingObjectID = acc.id;
                mBTellerPaperDetailTax.accountingObjectName = acc.accountingObjectName;
                mBTellerPaperDetailTax.taxCode = acc.taxCode;
            }
            this.mBTellerPaper.accountingObjectBankAccount = null;
            this.mBTellerPaper.accountingObjectBankName = null;
        }
        this.backUpAccountingObjectID = this.mBTellerPaper.accountingObjectID;
    }

    selectedChangedAccountingObjectBankAccount() {
        if (this.accountingObjectBankAccounts && this.mBTellerPaper.accountingObjectBankAccount) {
            const bankName = this.accountingObjectBankAccounts.find(a => a.id === this.mBTellerPaper.accountingObjectBankAccount).bankName;
            this.mBTellerPaper.accountingObjectBankName = bankName;
        }
    }

    selectedChangedCurrency() {
        this.currency = this.currencies.find(x => x.currencyCode === this.mBTellerPaper.currencyId);
        if (this.currency) {
            this.mBTellerPaper.exchangeRate = this.currency.exchangeRate;
            this.mBTellerPaper.totalAmount = 0;
            this.mBTellerPaper.totalAmountOriginal = 0;
            // this.mBTellerPaper.totalAmountOriginal = 0;
            for (let i = 0; i < this.mBTellerPaperDetails.length; i++) {
                if (this.mBTellerPaper.currencyId === this.currencyCode) {
                    this.mBTellerPaperDetails[i].amount = this.mBTellerPaperDetails[i].amountOriginal;
                } else {
                    this.mBTellerPaperDetails[i].amount = this.round(
                        this.mBTellerPaperDetails[i].amountOriginal *
                            (this.currency.formula.includes('*') ? this.mBTellerPaper.exchangeRate : 1 / this.mBTellerPaper.exchangeRate),
                        7
                    );
                }
                if (this.isCreateUrl) {
                    this.utilsService.calcular(
                        this.mBTellerPaperDetails[i],
                        this.mBTellerPaper.typeId,
                        'amountOriginal',
                        this.mBTellerPaper,
                        this.mBTellerPaperDetails,
                        this.mBTellerPaperDetailTaxs,
                        this.currentAccount,
                        this.getAmountOriginalType(),
                        this.currency.formula.includes('*') ? this.mBTellerPaper.exchangeRate : 1 / this.mBTellerPaper.exchangeRate
                    );
                }
                this.mBTellerPaper.totalAmount = this.mBTellerPaper.totalAmount + this.mBTellerPaperDetails[i].amount;
                this.mBTellerPaper.totalAmountOriginal =
                    this.mBTellerPaper.totalAmountOriginal + this.mBTellerPaperDetails[i].amountOriginal;
            }
            for (let i = 0; i < this.mBTellerPaperDetailTaxs.length; i++) {
                if (this.mBTellerPaper.currencyId === this.currencyCode) {
                    this.mBTellerPaperDetailTaxs[i].vATAmount = this.mBTellerPaperDetailTaxs[i].vATAmountOriginal;
                    this.mBTellerPaperDetailTaxs[i].pretaxAmount = this.mBTellerPaperDetailTaxs[i].pretaxAmountOriginal;
                } else {
                    this.mBTellerPaperDetailTaxs[i].vATAmount = this.round(
                        this.mBTellerPaperDetailTaxs[i].vATAmountOriginal *
                            (this.currency.formula.includes('*') ? this.mBTellerPaper.exchangeRate : 1 / this.mBTellerPaper.exchangeRate),
                        7
                    );
                    this.mBTellerPaperDetailTaxs[i].pretaxAmount = this.round(
                        this.mBTellerPaperDetailTaxs[i].pretaxAmountOriginal *
                            (this.currency.formula.includes('*') ? this.mBTellerPaper.exchangeRate : 1 / this.mBTellerPaper.exchangeRate),
                        7
                    );
                }
            }
        }
        this.updateMBTellerPaper();
    }

    changeExchangeRate() {
        if (!this.mBTellerPaper.exchangeRate) {
            this.mBTellerPaper.exchangeRate = 0;
        }
        if (this.mBTellerPaper.currencyId) {
            const cr: ICurrency = this.currencies.find(x => x.currencyCode === this.mBTellerPaper.currencyId);
            this.mBTellerPaper.totalAmount = 0;
            this.mBTellerPaper.totalAmountOriginal = 0;
            for (let i = 0; i < this.mBTellerPaperDetails.length; i++) {
                this.mBTellerPaperDetails[i].amount = this.round(
                    this.mBTellerPaperDetails[i].amountOriginal *
                        (cr.formula.includes('*') ? this.mBTellerPaper.exchangeRate : 1 / this.mBTellerPaper.exchangeRate),
                    7
                );
                this.utilsService.calcular(
                    this.mBTellerPaperDetails[i],
                    this.mBTellerPaper.typeId,
                    'amountOriginal',
                    this.mBTellerPaper,
                    this.mBTellerPaperDetails,
                    this.mBTellerPaperDetailTaxs,
                    this.currentAccount,
                    this.getAmountOriginalType(),
                    this.currency.formula.includes('*') ? this.mBTellerPaper.exchangeRate : 1 / this.mBTellerPaper.exchangeRate
                );
                this.mBTellerPaper.totalAmount = this.mBTellerPaper.totalAmount + this.mBTellerPaperDetails[i].amount;
                this.mBTellerPaper.totalAmountOriginal =
                    this.mBTellerPaper.totalAmountOriginal + this.mBTellerPaperDetails[i].amountOriginal;
            }
            for (let i = 0; i < this.mBTellerPaperDetailTaxs.length; i++) {
                this.mBTellerPaperDetailTaxs[i].vATAmountOriginal = this.round(
                    this.mBTellerPaperDetailTaxs[i].vATAmount /
                        (cr.formula.includes('*') ? this.mBTellerPaper.exchangeRate : 1 / this.mBTellerPaper.exchangeRate),
                    7
                );
                this.mBTellerPaperDetailTaxs[i].pretaxAmountOriginal = this.round(
                    this.mBTellerPaperDetailTaxs[i].pretaxAmount /
                        (cr.formula.includes('*') ? this.mBTellerPaper.exchangeRate : 1 / this.mBTellerPaper.exchangeRate),
                    7
                );
            }
        }
    }

    calcular(objcet1: object, typeID: number, name: string, objectParent: object, objectDT1: object[], objectDT2: object[]) {
        const count = this.mBTellerPaperDetailTaxs.length;
        this.utilsService.calcular(
            objcet1,
            typeID,
            name,
            objectParent,
            objectDT1,
            objectDT2,
            this.currentAccount,
            this.getAmountOriginalType(),
            this.currency.formula.includes('*') ? this.mBTellerPaper.exchangeRate : 1 / this.mBTellerPaper.exchangeRate
        );
        if (count < objectDT2.length) {
            if (this.mBTellerPaper.accountingObjectID) {
                const accDTT = this.accountingObjects.find(n => n.id === this.mBTellerPaper.accountingObjectID);
                this.mBTellerPaperDetailTaxs[count].accountingObjectID = accDTT.id;
                this.mBTellerPaperDetailTaxs[count].accountingObjectName = accDTT.accountingObjectName;
                this.mBTellerPaperDetailTaxs[count].taxCode = accDTT.taxCode;
            }
            if (this.isShowGoodsServicePurchase && this.currentAccount) {
                this.mBTellerPaperDetailTaxs[count].goodsServicePurchaseId = this.currentAccount.organizationUnit.goodsServicePurchaseID;
            }
            if (this.currentAccount) {
                this.mBTellerPaperDetailTaxs[count].goodsServicePurchaseId = this.currentAccount.organizationUnit.goodsServicePurchaseID;
            }
            this.updateAmountTaxWithAccount();
            this.updateMBTellerPaper();
        }
    }

    //  tinh tab detail
    calTotalAmountAndTotalAmountOriginal() {
        // tinh totalAmountOriginal cua mBTellerPaper
        this.mBTellerPaper.totalAmountOriginal = this.mBTellerPaperDetails
            .map(x => +x.amountOriginal)
            .reduce((previousValue, currentValue) => previousValue + currentValue);
        console.log('totalamountoriginal= ' + this.mBTellerPaper.totalAmountOriginal);
        // tinh totalamount cua mBTellerPaper
        // this.mBTellerPaper.totalamount = this.mBTellerPaper.totalamountoriginal * this.mBTellerPaper.exchangerate;
        this.mBTellerPaper.totalAmount = this.mBTellerPaperDetails
            .map(x => +x.amount)
            .reduce((previousValue, currentValue) => previousValue + currentValue);
        console.log('totalamount= ' + this.mBTellerPaper.totalAmount);
    }

    getAmountOriginalType() {
        if (this.isForeignCurrency()) {
            return 8;
        }
        return 7;
    }

    isForeignCurrency() {
        return this.currentAccount && this.mBTellerPaper.currencyId !== this.currentAccount.organizationUnit.currencyID;
    }

    //  tính tab thuế
    calVATAmount(mBTellerPaperDetails: IMBTellerPaperDetails, mBTellerPaperDetailTax: IMBTellerPaperDetailTax) {
        // tam thoi gan pretaxamountoriginal
        mBTellerPaperDetailTax.pretaxAmountOriginal = 1;
        // tinh vatamount tax
        if (mBTellerPaperDetails) {
            if (mBTellerPaperDetailTax.vATRate === 4 || mBTellerPaperDetailTax.vATRate === 3 || mBTellerPaperDetailTax.vATRate === 0) {
                mBTellerPaperDetailTax.vATAmount = 0;
                // tam thoi gan =0 xem lai nghiep vu
                mBTellerPaperDetailTax.pretaxAmount = 0;
            } else if (mBTellerPaperDetailTax.vATRate === 1) {
                mBTellerPaperDetailTax.vATAmount = mBTellerPaperDetails.amount * 5 / 100;
                mBTellerPaperDetailTax.pretaxAmount = mBTellerPaperDetailTax.vATAmount / mBTellerPaperDetailTax.vATRate * 100;
            } else if (mBTellerPaperDetailTax.vATRate === 2) {
                mBTellerPaperDetailTax.vATAmount = mBTellerPaperDetails.amount * 10 / 100;
                mBTellerPaperDetailTax.pretaxAmount = mBTellerPaperDetailTax.vATAmount / mBTellerPaperDetailTax.vATRate * 100;
            }
        }
    }

    sumAfterDeleteByContextMenu() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            if (this.select === 0) {
                this.mBTellerPaperDetails.splice(this.currentRow, 1);
                let sumTotalAmountOriginal = 0;
                let sumTotalAmount = 0;
                for (let i = 0; i < this.mBTellerPaperDetails.length; i++) {
                    sumTotalAmount += this.mBTellerPaperDetails[i].amount;
                    sumTotalAmountOriginal += this.mBTellerPaperDetails[i].amountOriginal;
                    this.utilsService.calcular(
                        this.mBTellerPaperDetails[i],
                        this.mBTellerPaper.typeId,
                        'amountOriginal',
                        this.mBTellerPaper,
                        this.mBTellerPaperDetails,
                        this.mBTellerPaperDetailTaxs,
                        this.currentAccount,
                        this.getAmountOriginalType(),
                        this.currency.formula.includes('*') ? this.mBTellerPaper.exchangeRate : 1 / this.mBTellerPaper.exchangeRate
                    );
                }
                this.mBTellerPaper.totalAmount = sumTotalAmount;
                this.mBTellerPaper.totalAmountOriginal = sumTotalAmountOriginal;
            } else if (this.select === 1) {
                this.mBTellerPaperDetailTaxs.splice(this.currentRow, 1);
                let sumTotalVATAmountOriginal = 0;
                let sumTotalVATAmount = 0;
                for (let i = 0; i < this.mBTellerPaperDetailTaxs.length; i++) {
                    sumTotalVATAmount += this.mBTellerPaperDetailTaxs[i].vATAmount;
                    sumTotalVATAmountOriginal += this.mBTellerPaperDetailTaxs[i].vATAmountOriginal;
                    // this.utilsService.calcular(
                    //     this.mBTellerPaperDetails[i],
                    //     this.mBTellerPaper.typeId,
                    //     'amountOriginal',
                    //     this.mBTellerPaper,
                    //     this.mBTellerPaperDetails,
                    //     this.mBTellerPaperDetailTaxs,
                    //     this.currentAccount,
                    //     this.getAmountOriginalType(),
                    //     this.currency.formula.includes('*') ? this.mBTellerPaper.exchangeRate : 1 / this.mBTellerPaper.exchangeRate
                    // );
                }
                this.mBTellerPaper.totalVatAmount = sumTotalVATAmount;
                this.mBTellerPaper.totalVatAmountOriginal = sumTotalVATAmountOriginal;
            } else {
                this.viewVouchersSelected.splice(this.currentRow, 1);
                this.select = null;
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    // xu ly delete row (ctr +delete)
    // keyPress(value: number, select: number) {
    //     if (select === 0) {
    //         this.mBTellerPaperDetails.splice(value, 1);
    //     } else if (select === 1) {
    //         this.mBTellerPaperDetailTaxs.splice(value, 1);
    //     }
    //     // sau khi xoa 1 dong
    //     this.eventManager.broadcast({
    //         name: 'afterDeleteRow'
    //     });
    // }

    keyPress(detail, key, select?: number) {
        switch (key) {
            case 'ctr + delete':
                this.removeRow(detail, select);
                break;
            case 'ctr + c':
                this.copyRow(detail, select);
                break;
            case 'ctr + insert':
                this.addNewRow(event, select);
                break;
        }
    }

    copyRow(detail, select) {
        if (!this.getSelectionText()) {
            if (select === 0) {
                const detailCopy: any = Object.assign({}, detail);
                detailCopy.id = null;
                this.mBTellerPaperDetails.push(detailCopy);
                this.calcular(
                    detailCopy,
                    this.mBTellerPaper.typeId,
                    'amountOriginal',
                    this.mBTellerPaper,
                    this.mBTellerPaperDetails,
                    this.mBTellerPaperDetailTaxs
                );
            } else {
                const detailCopy: any = Object.assign({}, detail);
                this.mBTellerPaperDetailTaxs.push(detailCopy);
                this.calcular(
                    detailCopy,
                    this.mBTellerPaper.typeId,
                    'vATAmount',
                    this.mBTellerPaper,
                    this.mBTellerPaperDetails,
                    this.mBTellerPaperDetailTaxs
                );
            }
        }
    }

    addNewRow(event: any, select: number) {
        this.mBTellerPaper.totalAmountOriginal = this.mBTellerPaper.totalAmountOriginal ? this.mBTellerPaper.totalAmountOriginal : 0;
        this.mBTellerPaper.totalAmount = this.mBTellerPaper.totalAmount ? this.mBTellerPaper.totalAmount : 0;
        this.mBTellerPaper.totalVatAmountOriginal = this.mBTellerPaper.totalVatAmountOriginal
            ? this.mBTellerPaper.totalVatAmountOriginal
            : 0;
        this.mBTellerPaper.totalVatAmount = this.mBTellerPaper.totalVatAmount ? this.mBTellerPaper.totalVatAmount : 0;
        if (select === 0) {
            this.mBTellerPaperDetails.push({});
            const count = this.mBTellerPaperDetails.length;
            this.mBTellerPaperDetails[count - 1].amount = 0;
            this.mBTellerPaperDetails[count - 1].amountOriginal = 0;
            this.mBTellerPaperDetails[count - 1].cashOutExchangeRateFb = 0;
            this.mBTellerPaperDetails[count - 1].cashOutDifferAmountFb = 0;
            this.mBTellerPaperDetails[count - 1].cashOutAmountFb = 0;
            this.mBTellerPaperDetails[count - 1].cashOutExchangeRateFb = 0;
            this.mBTellerPaperDetails[count - 1].cashOutDifferAmountFb = 0;
            this.mBTellerPaperDetails[count - 1].cashOutAmountFb = 0;
            if (count > 1) {
                this.mBTellerPaperDetails[count - 1].description = this.mBTellerPaperDetails[count - 2].description;
                this.mBTellerPaperDetails[count - 1].debitAccount = this.mBTellerPaperDetails[count - 2].debitAccount;
                this.mBTellerPaperDetails[count - 1].creditAccount = this.mBTellerPaperDetails[count - 2].creditAccount;
                this.mBTellerPaperDetails[count - 1].accountingObjectID = this.mBTellerPaperDetails[count - 2].accountingObjectID;
            } else {
                this.mBTellerPaperDetails[count - 1].description = this.mBTellerPaper.reason ? this.mBTellerPaper.reason : null;
                if (this.iAutoPrinciple) {
                    if (this.debitAccountList.find(a => a.accountNumber === this.iAutoPrinciple.debitAccount)) {
                        this.mBTellerPaperDetails[this.mBTellerPaperDetails.length - 1].debitAccount = this.iAutoPrinciple.debitAccount;
                    }
                    if (this.creditAccountList.find(a => a.accountNumber === this.iAutoPrinciple.creditAccount)) {
                        this.mBTellerPaperDetails[this.mBTellerPaperDetails.length - 1].creditAccount = this.iAutoPrinciple.creditAccount;
                    }
                }
                this.mBTellerPaperDetails[count - 1].accountingObjectID = this.mBTellerPaper.accountingObjectID;
            }
            const nameTag: string = event.srcElement.id;
            const idx: number = this.mBTellerPaperDetails.length - 1;
            const nameTagIndex: string = nameTag + String(idx);
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(nameTagIndex);
                if (element) {
                    element.focus();
                }
            }, 0);
            // this.contextmenu.value = false;
        } else if (select === 1) {
            this.mBTellerPaperDetailTaxs.push({});
            const count = this.mBTellerPaperDetailTaxs.length;
            this.mBTellerPaperDetailTaxs[count - 1].description = 'Thuế GTGT';
            this.mBTellerPaperDetailTaxs[count - 1].vATRate = 0;
            this.mBTellerPaperDetailTaxs[count - 1].vATAmount = 0;
            this.mBTellerPaperDetailTaxs[count - 1].vATAmountOriginal = 0;
            this.mBTellerPaperDetailTaxs[count - 1].pretaxAmount = 0;
            this.mBTellerPaperDetailTaxs[count - 1].pretaxAmountOriginal = 0;
            if (count > 1) {
                this.mBTellerPaperDetailTaxs[count - 1].vATAccount = this.mBTellerPaperDetailTaxs[count - 2].vATAccount;
                this.mBTellerPaperDetailTaxs[count - 1].vATRate = this.mBTellerPaperDetailTaxs[count - 2].vATRate;
                this.mBTellerPaperDetailTaxs[count - 1].description = this.mBTellerPaperDetailTaxs[count - 2].description;
                this.mBTellerPaperDetailTaxs[count - 1].accountingObjectID = this.mBTellerPaperDetailTaxs[count - 2].accountingObjectID;
                this.mBTellerPaperDetailTaxs[count - 1].accountingObjectName = this.mBTellerPaperDetailTaxs[count - 2].accountingObjectName;
                this.mBTellerPaperDetailTaxs[count - 1].taxCode = this.mBTellerPaperDetailTaxs[count - 2].taxCode;
                if (this.isShowGoodsServicePurchase) {
                    this.mBTellerPaperDetailTaxs[count - 1].goodsServicePurchaseId = this.mBTellerPaperDetailTaxs[
                        count - 2
                    ].goodsServicePurchaseId;
                }
            } else {
                if (this.mBTellerPaper.accountingObjectID) {
                    const acc = this.accountingObjects.find(n => n.id === this.mBTellerPaper.accountingObjectID);
                    this.mBTellerPaperDetailTaxs[count - 1].accountingObjectID = acc.id;
                    this.mBTellerPaperDetailTaxs[count - 1].accountingObjectName = acc.accountingObjectName;
                    this.mBTellerPaperDetailTaxs[count - 1].taxCode = acc.taxCode;
                }
                if (this.isShowGoodsServicePurchase && this.currentAccount) {
                    this.mBTellerPaperDetailTaxs[
                        count - 1
                    ].goodsServicePurchaseId = this.currentAccount.organizationUnit.goodsServicePurchaseID;
                }
            }
            const nameTag: string = event.srcElement.id;
            const idx: number = this.mBTellerPaperDetailTaxs.length - 1;
            const nameTagIndex: string = nameTag + String(idx);
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(nameTagIndex);
                if (element) {
                    element.focus();
                }
            }, 0);
            // this.contextmenu.value = false;
        }
    }

    // right click event
    // activates the menu with the coordinates
    // onrightClick(
    //     event,
    //     eventData: any,
    //     selectedObj: any,
    //     isShowTab1: any,
    //     isShowTab2: any,
    //     contextmenu: any,
    //     x: any,
    //     y: any,
    //     select: number
    // ) {
    //     if (!this.isReadOnly) {
    //         this.utilsService.onrightClick(event, eventData, selectedObj, isShowTab1, isShowTab2, contextmenu, x, y, select);
    //         console.log(event);
    //     } else {
    //         return;
    //     }
    // }

    // // disables the menu
    // disableContextMenu() {
    //     this.contextmenu.value = false;
    // }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    //  end of right click

    onRightClick($event, data, selectedData, isNew, isDelete, select, currentRow) {
        if (!this.isReadOnly) {
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

    // ghi so
    @ebAuth(['ROLE_ADMIN', ROLE.BaoNo_GhiSo])
    record() {
        event.preventDefault();
        if (!this.checkCloseBook(this.currentAccount, this.mBTellerPaper.postedDate) && !this.utilsService.isShowPopup) {
            this.record_ = {};
            this.record_.id = this.mBTellerPaper.id;
            this.record_.typeID = this.mBTellerPaper.typeId;
            const loading = this.toastr.success(
                this.translate.instant('ebwebApp.mBTellerPaper.recordToast.recording'),
                this.translate.instant('ebwebApp.mBTellerPaper.message.title')
            );
            this.gLService.record(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                console.log(JSON.stringify(res.body));
                this.toastr.remove(loading.toastId);
                if (res.body.success === true) {
                    this.mBTellerPaper.recorded = true;
                    this.toastr.success(
                        this.translate.instant('ebwebApp.mBTellerPaper.recordToast.done'),
                        this.translate.instant('ebwebApp.mBTellerPaper.message.title')
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
                } else {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.mBTellerPaper.recordToast.failed'),
                        this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                    );
                }
            });
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoNo_GhiSo])
    unrecord() {
        event.preventDefault();
        if (!this.checkCloseBook(this.currentAccount, this.mBTellerPaper.postedDate) && !this.utilsService.isShowPopup) {
            this.record_ = {};
            this.record_.id = this.mBTellerPaper.id;
            this.record_.typeID = this.mBTellerPaper.typeId;
            const loading = this.toastr.success(
                this.translate.instant('ebwebApp.mBTellerPaper.unrecordToast.unrecording'),
                this.translate.instant('ebwebApp.mBTellerPaper.message.title')
            );
            this.gLService.unrecord(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                console.log(JSON.stringify(res.body));
                if (res.body.success === true) {
                    this.toastr.remove(loading.toastId);
                    this.mBTellerPaper.recorded = false;
                    this.toastr.success(
                        this.translate.instant('ebwebApp.mBTellerPaper.unrecordToast.done'),
                        this.translate.instant('ebwebApp.mBTellerPaper.message.title')
                    );
                } else {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.mBTellerPaper.unrecordToast.failed'),
                        this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                    );
                }
            });
        }
    }

    // typeReport:number;
    @ebAuth(['ROLE_ADMIN', ROLE.BaoNo_Sua])
    exportPdf(isDownload: boolean, typeReports: number) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.mBTellerPaper.id,
                typeID: this.mBTellerPaper.typeId,
                typeReport: typeReports
            },
            isDownload
        );
        if (typeReports === 1) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.mBDeposit.financialPaper') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 2) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.mCPayment.home.title') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        }
    }

    // function trackId ngFor
    trackBankAccountDetailsById(idx: number, item: IBankAccountDetails) {
        return item.id;
    }

    trackAutoPrincipleById(idx: number, item: IAutoPrinciple) {
        return item.id;
    }

    trackAccountingObjectById(idx: number, item: IAccountingObject) {
        return item.id;
    }

    trackAccountingObjectBankAccountById(idx: number, item: IAccountingObjectBankAccount) {
        return item.id;
    }

    trackExpenseItemById(idx: number, item: IExpenseItem) {
        return item.id;
    }

    trackCostSetById(idx: number, item: ICostSet) {
        return item.id;
    }

    trackOrganizationUnitById(idx: number, item: IOrganizationUnit) {
        return item.id;
    }

    get mBTellerPaper() {
        return this._mBTellerPaper;
    }

    set mBTellerPaper(mBTellerPaper: IMBTellerPaper) {
        this._mBTellerPaper = mBTellerPaper;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackEMContractById(idx: number, item: IEMContract) {
        return item.id;
    }

    trackStatisticsCodeById(idx: number, item: IStatisticsCode) {
        return item.id;
    }

    selectedChangedType() {
        if (this.isCreateUrl && !this.mBTellerPaper.id) {
            this.getReasonDefault();
        }
        const typeSelected: IType = this.types.find(x => x.id === this.mBTellerPaper.typeId);
        this.strTypeName = typeSelected.typeName;
        this.utilsService
            .getGenCodeVoucher({
                typeGroupID: this.mBTellerPaper.typeId ? typeSelected.typeGroupID : 12,
                displayOnBook: this.displayBook
            })
            .subscribe((res: HttpResponse<string>) => {
                if (+this.mBTellerPaper.typeLedger !== 2) {
                    // khong Su dung ca 2 SO
                    if (this.displayBook === 0) {
                        this.mBTellerPaper.noFBook = res.body;
                        this.mBTellerPaper.noMBook = '';
                    } else {
                        this.mBTellerPaper.noFBook = '';
                        this.mBTellerPaper.noMBook = res.body;
                    }
                } else {
                    // su dung ca 2 SO
                    if (this.dataSO_LAM_VIEC === 0) {
                        this.mBTellerPaper.noFBook = res.body;
                    } else {
                        this.mBTellerPaper.noMBook = res.body;
                    }
                }
            });
        this.getDataAccount();
        // load autoprinciple theo TypeID
        this.autoPrincipleService.getAutoPrinciples().subscribe((res: HttpResponse<IAutoPrinciple[]>) => {
            this.autoPrinciples = res.body.filter(x => x.typeId === this.mBTellerPaper.typeId);
        });
    }

    selectedChangedAccountingObjectType() {
        this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accounting = res.body;
            // nha cung cap = 0,
            if (+this.mBTellerPaper.accountingObjectType === 0) {
                this.accountingObjects = res.body
                    .filter(x => x.objectType === 0 || x.objectType === 2)
                    .sort((a, b) => (a.accountingObjectCode > b.accountingObjectCode ? 1 : -1));
            } else if (+this.mBTellerPaper.accountingObjectType === 1) {
                // khach hang = 1
                this.accountingObjects = res.body
                    .filter(x => x.objectType === 1 || x.objectType === 2)
                    .sort((a, b) => (a.accountingObjectCode > b.accountingObjectCode ? 1 : -1));
            } else if (+this.mBTellerPaper.accountingObjectType === 2) {
                // nhan vien = 2
                this.accountingObjects = res.body
                    .filter(x => x.isEmployee)
                    .sort((a, b) => (a.accountingObjectCode > b.accountingObjectCode ? 1 : -1));
            } else if (+this.mBTellerPaper.accountingObjectType === 3) {
                // khac = 3
                this.accountingObjects = res.body
                    .filter(x => x.objectType === 3)
                    .sort((a, b) => (a.accountingObjectCode > b.accountingObjectCode ? 1 : -1));
            }
            this.translate.get(['ebwebApp.mBTellerPaper.defaultReason']).subscribe(res2 => {
                this.mBTellerPaper.reason = res2['ebwebApp.mBTellerPaper.defaultReason'];
            });
            for (let i = 0; i < this.mBTellerPaperDetails.length; i++) {
                this.mBTellerPaperDetails[i].accountingObjectID = null;
                this.translate.get(['ebwebApp.mBTellerPaper.defaultReason']).subscribe(res2 => {
                    this.mBTellerPaperDetails[i].description = res2['ebwebApp.mBTellerPaper.defaultReason'];
                });
            }
            this.selectAccountingObjectType();
        });
    }

    checkPostedDateGreaterDate(): boolean {
        if (
            this.mBTellerPaper.date &&
            this.mBTellerPaper.postedDate &&
            moment(this.mBTellerPaper.postedDate) < moment(this.mBTellerPaper.date)
        ) {
            this.toastr.error('Ngày hạch toán không được nhỏ hơn ngày chứng từ !', 'Lỗi dữ liệu');
            return false;
        } else {
            return true;
        }
    }

    public beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            if (!this.isReadOnly) {
                this.modalRef = this.refModalService.open(this.viewVouchersSelected);
            }
        }
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoNo_Them])
    addNew(event) {
        event.preventDefault();
        if (!this.utilsService.isShowPopup) {
            this.router.navigate(['/mb-teller-paper/new']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoNo_Sua])
    edit() {
        event.preventDefault();
        if (
            !this.checkCloseBook(this.currentAccount, this.mBTellerPaper.postedDate) &&
            !this.utilsService.isShowPopup &&
            !this.mBTellerPaper.recorded
        ) {
            this.isReadOnly = false;
            this.isCheckCanDeactive = true;
            this.mBTellerPaperCopy = Object.assign({}, this.mBTellerPaper);
            this.mBTellerPaperDetailsCopy = this.mBTellerPaperDetails.map(object => ({ ...object }));
            this.mBTellerPaperDetailTaxsCopy = this.mBTellerPaperDetailTaxs.map(object => ({ ...object }));
        }
    }

    /*
    * hàm ss du lieu 2 object và 2 mảng object
    * return true: neu tat ca giong nhau
    * return fale: neu 1 trong cac object ko giong nhau
    * */
    canDeactive(): boolean {
        if (this.isCheckCanDeactive) {
            return (
                this.utilsService.isEquivalent(this.mBTellerPaper, this.mBTellerPaperCopy) &&
                this.utilsService.isEquivalentArray(this.mBTellerPaperDetails, this.mBTellerPaperDetailsCopy) &&
                this.utilsService.isEquivalentArray(this.mBTellerPaperDetailTaxs, this.mBTellerPaperDetailTaxsCopy)
            );
        }
        return true;
    }

    closeForm() {
        event.preventDefault();
        if (this.isReadOnly && !this.utilsService.isShowPopup) {
            this.isCheckCanDeactive = false;
            this.router.navigate(['/mb-teller-paper']);
        } else if (!this.utilsService.isShowPopup) {
            this.isCheckCanDeactive = true;
            if (!this.canDeactive()) {
                this.modalRef = this.modalService.open(this.modalCloseComponent, { backdrop: 'static' });
            } else {
                this.noSaveCloseAndBackList();
            }
        }
    }

    closeModal() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isCheckTotal = true;
    }

    noSaveCloseAndBackList() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isCheckCanDeactive = false;
        this.router.navigate(['/mb-teller-paper']);
    }

    loadDetailsIsPPInvoice() {
        if (this.mBTellerPaper.typeId === 125 || this.mBTellerPaper.typeId === 131 || this.mBTellerPaper.typeId === 141) {
            this.pPInvoiceService.findPPInvoiceByPaymentVoucherId({ id: this.mBTellerPaper.id }).subscribe(res => {
                this.ppInvoice = res.body;
                if (this.ppInvoice) {
                    this.pPInvoiceService.findDetailById({ id: this.ppInvoice.id }).subscribe(resD => {
                        this.ppInvoiceDetails = resD.body;
                        console.log(this.ppInvoiceDetails);
                        this.fillDetailAndDetailTax();
                    });
                    this.pPInvoiceService
                        .findRefVoucherByRefId({ refId: this.ppInvoice.id })
                        .subscribe((resV: HttpResponse<IRefVoucher[]>) => {
                            this.viewVouchersSelected = resV.body;
                            console.log(this.viewVouchersSelected);
                        });
                }
            });
        }
    }

    round(value, type) {
        return this.utilsService.round(value, this.currentAccount.systemOption, type);
    }

    fillDetailAndDetailTax() {
        this.mBTellerPaperDetails = [];
        this.mBTellerPaperDetailTaxs = [];
        for (let u = 0; u < this.ppInvoiceDetails.length; u++) {
            if (this.ppInvoiceDetails[u].amountOriginal) {
                this.mBTellerPaperDetails.push({});
            }
            if (this.ppInvoiceDetails[u].vatAmountOriginal) {
                this.mBTellerPaperDetailTaxs.push({});
            }
        }
        console.log('ppinvoicedetails.LENGTH = ' + this.ppInvoiceDetails.length);
        console.log('ppinvoicedetails = ' + JSON.stringify(this.ppInvoiceDetails));
        for (let i = 0; i < this.ppInvoiceDetails.length; i++) {
            for (let j = 0; j < this.mBTellerPaperDetails.length; j++) {
                this.mBTellerPaperDetails[j].description = this.ppInvoiceDetails[i].description;
                this.mBTellerPaperDetails[j].debitAccount = this.ppInvoiceDetails[i].debitAccount;
                this.mBTellerPaperDetails[j].creditAccount = this.ppInvoiceDetails[i].creditAccount;
                this.mBTellerPaperDetails[j].amount = this.ppInvoiceDetails[i].amount;
                this.mBTellerPaperDetails[j].amountOriginal = this.ppInvoiceDetails[i].amountOriginal;
                this.mBTellerPaperDetails[j].budgetItemID = this.ppInvoiceDetails[i].budgetItemId;
                this.mBTellerPaperDetails[j].costSetID = this.ppInvoiceDetails[i].costSetId;
                this.mBTellerPaperDetails[j].eMContractID = this.ppInvoiceDetails[i].contractId;
                this.mBTellerPaperDetails[j].accountingObjectID = this.ppInvoiceDetails[i].accountingObjectId;
                this.mBTellerPaperDetails[j].statisticsCodeID = this.ppInvoiceDetails[i].statisticCodeId;
                this.mBTellerPaperDetails[j].departmentID = this.ppInvoiceDetails[i].departmentId;
                this.mBTellerPaperDetails[j].expenseItemID = this.ppInvoiceDetails[i].expenseItemId;
            }
            for (let k = 0; k < this.mBTellerPaperDetailTaxs.length; k++) {
                this.mBTellerPaperDetailTaxs[k].description = this.ppInvoiceDetails[i].vatDescription;
                this.mBTellerPaperDetailTaxs[k].vATAmount = this.ppInvoiceDetails[i].vatAmount;
                this.mBTellerPaperDetailTaxs[k].vATAmountOriginal = this.ppInvoiceDetails[i].vatAmountOriginal;
                this.mBTellerPaperDetailTaxs[k].vATRate = this.ppInvoiceDetails[i].vatRate;
                this.mBTellerPaperDetailTaxs[k].vATAccount = this.ppInvoiceDetails[i].vatAccount;
                this.mBTellerPaperDetailTaxs[k].invoiceDate = this.ppInvoiceDetails[i].invoiceDate;
                this.mBTellerPaperDetailTaxs[k].invoiceNo = this.ppInvoiceDetails[i].invoiceNo;
                this.mBTellerPaperDetailTaxs[k].invoiceSeries = this.ppInvoiceDetails[i].invoiceSeries;
                if (this.isShowGoodsServicePurchase) {
                    this.mBTellerPaperDetailTaxs[k].goodsServicePurchaseId = this.ppInvoiceDetails[i].goodsServicePurchaseId;
                }
                this.mBTellerPaperDetailTaxs[k].accountingObjectID = this.ppInvoiceDetails[i].accountingObjectId;
            }
        }
    }

    getTotalAmountOriginal() {
        if (this.mBTellerPaperDetails && this.mBTellerPaperDetails.length > 0) {
            return this.mBTellerPaperDetails.map(n => n.amountOriginal).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getTotalAmount() {
        if (this.mBTellerPaperDetails && this.mBTellerPaperDetails.length > 0) {
            return this.mBTellerPaperDetails.map(n => n.amount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getTotalVATAmount() {
        if (this.mBTellerPaperDetailTaxs && this.mBTellerPaperDetailTaxs.length > 0) {
            return this.mBTellerPaperDetailTaxs.map(n => n.vATAmount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getTotalPretaxAmount() {
        if (this.mBTellerPaperDetailTaxs && this.mBTellerPaperDetailTaxs.length > 0) {
            return this.mBTellerPaperDetailTaxs.map(n => n.pretaxAmount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoNo_Xoa])
    delete() {
        event.preventDefault();
        if (!this.checkCloseBook(this.currentAccount, this.mBTellerPaper.postedDate) && !this.utilsService.isShowPopup) {
            this.isCheckCanDeactive = false;
            this.router.navigate(['/mb-teller-paper', { outlets: { popup: this.mBTellerPaper.id + '/delete' } }]);
        }
    }

    summCPaymentDetailVendors(prop) {
        let total = 0;
        if (this.mBTellerPaperDetailVendors) {
            for (let i = 0; i < this.mBTellerPaperDetailVendors.length; i++) {
                total += this.mBTellerPaperDetailVendors[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    viewVoucher(imcPaymentDetailVendor: IMBTellerPaperDetailVendor) {
        let url = '';
        if (imcPaymentDetailVendor.voucherTypeID === this.TYPE_PP_INVOICE) {
            this.viewVoucherService.checkViaStockPPInvoice({ id: imcPaymentDetailVendor.pPInvoiceID }).subscribe(
                (res: HttpResponse<any>) => {
                    if (res.body.message === UpdateDataMessages.STOCK_TRUE) {
                        url = `/#/mua-hang/qua-kho-ref/${imcPaymentDetailVendor.pPInvoiceID}/edit/1`;
                        window.open(url, '_blank');
                    } else if (res.body.message === UpdateDataMessages.STOCK_FALSE) {
                        url = `/#/mua-hang/khong-qua-kho-ref/${imcPaymentDetailVendor.pPInvoiceID}/edit/1`;
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
        } else if (imcPaymentDetailVendor.voucherTypeID === this.TYPE_PP_SERVICE) {
            url = `/#/pp-service/${imcPaymentDetailVendor.pPInvoiceID}/edit/from-ref`;
        } else if (imcPaymentDetailVendor.voucherTypeID === this.TYPE_PP_DISCOUNT_RETURN) {
            url = `/#/pp-discount-return/${imcPaymentDetailVendor.pPInvoiceID}/edit/from-ref`;
        } else if (imcPaymentDetailVendor.voucherTypeID === this.TYPE_FA_INCREAMENT) {
            url = `/#/fa-increament/${imcPaymentDetailVendor.pPInvoiceID}/edit/from-ref`;
        } else if (imcPaymentDetailVendor.voucherTypeID === this.TYPE_TI_INCREAMENT) {
            url = `/#/ti-increament/${imcPaymentDetailVendor.pPInvoiceID}/edit/from-ref`;
        } else if (imcPaymentDetailVendor.voucherTypeID === TypeID.CHUNG_TU_NGHIEP_VU_KHAC) {
            url = `/#/g-other-voucher/${imcPaymentDetailVendor.pPInvoiceID}/edit/from-ref`;
        }
        if (url) {
            window.open(url, '_blank');
        }
    }

    getEmployeeByID(id) {
        if (this.accountingObjectData) {
            const epl = this.accountingObjectData.find(n => n.id === id);
            if (epl) {
                return epl.accountingObjectCode;
            } else {
                return '';
            }
        }
    }

    trackMBTellerPaperDetailsById(idx: number, item: IMBTellerPaperDetails) {
        return item.id;
    }

    getAccountingObjectCode(detail: any): string {
        const acc = this.accountingObjects.find(x => x.id === detail.accountingObjectID);
        if (acc) {
            return acc ? acc.accountingObjectCode : '';
        } else {
            return '';
        }
    }

    getExpenseItemCode(detail: any): string {
        const expenseItem = this.expenseItems.find(x => x.id === detail.expenseItemID);
        if (expenseItem) {
            return expenseItem ? expenseItem.expenseItemCode : '';
        } else {
            return '';
        }
    }

    getDepartmentCode(detail: any): string {
        const orgUnit = this.organizationUnits.find(x => x.id === detail.departmentID);
        if (orgUnit) {
            return orgUnit ? orgUnit.organizationUnitCode : '';
        } else {
            return '';
        }
    }

    getCostSetCode(detail: any): string {
        const costSet = this.costSets.find(x => x.id === detail.costSetID);
        if (costSet) {
            return costSet ? costSet.costSetCode : '';
        } else {
            return '';
        }
    }

    getEMContractName(detail: any): string {
        const eMContract = this.eMContracts.find(x => x.id === detail.eMContractID);
        if (eMContract) {
            return this.isSoTaiChinh ? eMContract.noFBook : eMContract.noMBook;
        }
        return '';
    }

    getStatisticsCodeName(detail: any): string {
        const statisticsCode = this.statisticCodes.find(x => x.id === detail.statisticsCodeID);
        if (statisticsCode) {
            return statisticsCode ? statisticsCode.statisticsCodeName : '';
        } else {
            return '';
        }
    }

    updateAmountTaxWithAccount() {
        for (let i = 0; i < this.mBTellerPaperDetailTaxs.length; i++) {
            if (this.mBTellerPaperDetailTaxs[i].vATAccount && this.mBTellerPaperDetailTaxs[i].vATAccount.startsWith('133', 0)) {
                if (
                    this.mBTellerPaperDetails.length > 0 &&
                    !this.mBTellerPaperDetails.find(n => n.debitAccount === this.mBTellerPaperDetailTaxs[i].vATAccount)
                ) {
                    this.mBTellerPaperDetailTaxs[i].pretaxAmount = 0;
                    this.mBTellerPaperDetailTaxs[i].vATAmount = 0;
                    this.mBTellerPaperDetailTaxs[i].vATAmountOriginal = 0;
                }
            }
        }
    }

    removeRow(detail: object, select: number) {
        if (select === 0) {
            const dtt: IMCPaymentDetailTax = this.mBTellerPaperDetailTaxs.find(
                n => n.vATAccount === detail['debitAccount'] && n.vATAccount !== '' && n.vATAccount !== null && n.vATAccount !== undefined
            );
            if (dtt) {
                if (dtt.vATAccount.startsWith('133', 0)) {
                    if (this.mBTellerPaperDetails.filter(n => n.debitAccount === dtt.vATAccount).length > 1) {
                        dtt.vATAmount -= detail['amount'];
                        dtt.vATAmountOriginal -= detail['amountOriginal'];
                        if (dtt.vATRate === 1) {
                            dtt.pretaxAmount = this.round(parseFloat(dtt.vATAmount.toString()) / 0.05, 7);
                            dtt.pretaxAmountOriginal = this.round(
                                parseFloat(dtt.pretaxAmount.toString()) /
                                    parseFloat(
                                        (this.currency.formula.includes('*')
                                            ? this.mBTellerPaper.exchangeRate
                                            : 1 / this.mBTellerPaper.exchangeRate
                                        ).toString()
                                    ),
                                8
                            );
                        } else if (dtt.vATRate === 2) {
                            dtt.pretaxAmount = this.round(parseFloat(dtt.vATAmount.toString()) / 0.1, 7);
                            dtt.pretaxAmountOriginal = this.round(parseFloat(dtt.vATAmount.toString()) / 0.1, 8);
                        } else {
                            dtt.pretaxAmount = 0;
                            dtt.pretaxAmountOriginal = 0;
                        }
                    } else {
                        dtt.vATAmount = 0;
                        dtt.vATAmountOriginal = 0;
                    }
                }
            }
            this.mBTellerPaperDetails.splice(this.mBTellerPaperDetails.indexOf(detail), 1);
        } else if (select === 1) {
            this.mBTellerPaperDetailTaxs.splice(this.mBTellerPaperDetails.indexOf(detail), 1);
        }
        this.updateMBTellerPaper();
    }

    updateMBTellerPaper() {
        this.mBTellerPaper.totalAmount = this.round(this.sumDT('amount'), 7);
        this.mBTellerPaper.totalAmountOriginal = this.round(this.sumDT('amountOriginal'), 8);
        this.mBTellerPaper.totalVatAmount = this.round(this.sumDTTax('vATAmount'), 7);
        this.mBTellerPaper.totalVatAmountOriginal = this.round(this.sumDTTax('vATAmountOriginal'), 8);
    }

    sumDT(prop) {
        let total = 0;
        for (let i = 0; i < this.mBTellerPaperDetails.length; i++) {
            total += this.mBTellerPaperDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    sumDTTax(prop) {
        let total = 0;
        for (let i = 0; i < this.mBTellerPaperDetailTaxs.length; i++) {
            total += this.mBTellerPaperDetailTaxs[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    changeReason() {
        this.isChangedReason = true;
    }

    getCheckNCC(): boolean {
        return (
            this.mBTellerPaper.typeId === this.TYPE_UNC_TRA_TIEN_NCC ||
            this.mBTellerPaper.typeId === this.TYPE_SCK_TRA_TIEN_NCC ||
            this.mBTellerPaper.typeId === this.TYPE_STM_TRA_TIEN_NCC
        );
    }

    ngAfterViewInit(): void {
        if (!this.isReadOnly) {
            this.focusFirstInput();
        }
    }

    addIfLastInput(i, num) {
        if (num === 0) {
            if (i === this.mBTellerPaperDetails.length - 1) {
                this.keyControlC(null, num);
            }
        } else {
            if (i === this.mBTellerPaperDetailTaxs.length - 1) {
                this.keyControlC(null, num);
            }
        }
    }

    keyControlC(value: number, num: number) {
        if (num === 0) {
            if (value !== null && value !== undefined) {
                const ob: IMBTellerPaperDetails = Object.assign({}, this.mBTellerPaperDetails[value]);
                ob.id = undefined;
                ob.orderPriority = undefined;
                this.mBTellerPaperDetails.push(ob);
            } else {
                this.mBTellerPaperDetails.push({});
            }
        } else {
            if (value !== null && value !== undefined) {
                const ob: IMBTellerPaperDetailTax = Object.assign({}, this.mBTellerPaperDetailTaxs[value]);
                ob.id = undefined;
                ob.orderPriority = undefined;
                this.mBTellerPaperDetailTaxs.push(ob);
            } else {
                this.mBTellerPaperDetailTaxs.push({});
            }
        }
    }

    selectChangeAccountingObjectTax(i) {
        if (this.mBTellerPaperDetailTaxs[i].accountingObjectID && this.accountingObjects) {
            const currentAccountingObject = this.accountingObjects.find(a => a.id === this.mBTellerPaperDetailTaxs[i].accountingObjectID);
            this.mBTellerPaperDetailTaxs[i].accountingObjectName = currentAccountingObject.accountingObjectName;
            this.mBTellerPaperDetailTaxs[i].taxCode = currentAccountingObject.taxCode;
        }
    }

    // Loại đối tượng kế toán
    selectAccountingObjectType() {
        if (this.mBTellerPaper.accountingObjectType !== null && this.mBTellerPaper.accountingObjectType !== undefined && this.accounting) {
            if (this.mBTellerPaper.accountingObjectType === 0) {
                // Nhà cung cấp
                this.nameCategory = this.utilsService.NHA_CUNG_CAP;
                this.accountingObjects = this.accounting.filter(n => n.objectType === 0 || n.objectType === 2);
            } else if (this.mBTellerPaper.accountingObjectType === 1) {
                // Khách hàng
                this.nameCategory = this.utilsService.KHACH_HANG;
                this.accountingObjects = this.accounting.filter(n => n.objectType === 1 || n.objectType === 2);
            } else if (this.mBTellerPaper.accountingObjectType === 2) {
                // Nhân viên
                this.nameCategory = this.utilsService.NHAN_VIEN;
                this.accountingObjects = this.accounting.filter(n => n.isEmployee);
            } else if (this.mBTellerPaper.accountingObjectType === 3) {
                // Khác
                this.nameCategory = this.utilsService.KHACH_HANG;
                this.accountingObjects = this.accounting.filter(n => n.objectType === 3);
            } else {
                this.nameCategory = this.utilsService.KHACH_HANG;
                this.accountingObjects = this.accounting;
            }
        }
        if (this.accountingObjects && this.accountingObjects.length > 0) {
            if (this.mBTellerPaper.accountingObjectID) {
                if (!this.accountingObjects.map(n => n.id).includes(this.mBTellerPaper.accountingObjectID)) {
                    this.mBTellerPaper.accountingObjectID = null;
                    this.mBTellerPaper.accountingObjectName = null;
                    this.mBTellerPaper.accountingObjectAddress = null;
                }
            }
        }
    }

    selectChangeDate() {
        if (!this.isReadOnly) {
            if (this.mBTellerPaper.date) {
                this.mBTellerPaper.postedDate = this.mBTellerPaper.date;
            }
        }
    }

    saveDetails(i, isAccountingObject?: boolean) {
        this.currentRow = i;
        if (isAccountingObject) {
            const iAccountingObject = this.accounting.find(a => a.id === this.mBTellerPaperDetails[i].accountingObjectID);
            if (iAccountingObject) {
                if (!this.autoPrinciple) {
                    this.translate.get(['ebwebApp.mBTellerPaper.defaultReason']).subscribe(res2 => {
                        this.mBTellerPaperDetails[i].description =
                            res2['ebwebApp.mBTellerPaper.defaultReason'] + ' từ ' + iAccountingObject.accountingObjectName;
                    });
                }
            }
        }
        this.details = this.mBTellerPaperDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.mBTellerPaper;
    }

    addDataToDetail() {
        this.mBTellerPaperDetails = this.details ? this.details : this.mBTellerPaperDetails;
        this.mBTellerPaper = this.parent ? this.parent : this.mBTellerPaper;
    }

    afterAddRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            if (this.select === 0) {
                this.mBTellerPaperDetails.push({ description: this.mBTellerPaper.reason, amount: 0, amountOriginal: 0 });
            } else if (this.select === 1) {
                this.mBTellerPaperDetailTaxs.push({ description: 'Thuế GTGT', vATAmount: 0 });
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            if (this.select === 0) {
                const ob: IMBTellerPaperDetails = Object.assign({}, this.contextMenu.selectedData);
                ob.id = undefined;
                ob.orderPriority = undefined;
                this.mBTellerPaperDetails.push(ob);
            } else if (this.select === 1) {
                const ob: IMBTellerPaperDetailTax = Object.assign({}, this.contextMenu.selectedData);
                ob.id = undefined;
                ob.orderPriority = undefined;
                this.mBTellerPaperDetailTaxs.push(ob);
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
        // this.selectChangeTotalAmount();
    }
}
