import { AfterViewChecked, AfterViewInit, Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';

import { IPPInvoice } from 'app/shared/model/pp-invoice.model';
import { PPInvoiceService } from './pp-invoice.service';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { IPPInvoiceDetails } from 'app/shared/model/pp-invoice-details.model';
import { RepositoryService } from 'app/danhmuc/repository';
import { IRepository } from 'app/shared/model/repository.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { Unit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import { Moment } from 'moment';
import * as moment from 'moment';
import { Principal } from 'app/core';
import { MaterialGoodsConvertUnitService } from 'app/entities/material-goods-convert-unit';
import { IMaterialGoodsConvertUnit } from 'app/shared/model/material-goods-convert-unit.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ExpenseItemService } from 'app/entities/expense-item';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { CostSetService } from 'app/entities/cost-set';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { EMContractService } from 'app/entities/em-contract';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { BudgetItemService } from 'app/entities/budget-item';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import {
    ACCOUNT_DETAIL_TYPE,
    AccountType,
    GROUP_TYPEID,
    MATERIAL_GOODS_TYPE,
    MSGERROR,
    PPINVOICE_COMPONENT_TYPE,
    PPINVOICE_TYPE,
    SO_LAM_VIEC,
    SU_DUNG_SO_QUAN_TRI,
    TypeID
} from 'app/app.constants';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BankAccountDetails, IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { AccountingObjectBankAccountService } from 'app/entities/accounting-object-bank-account';
import { CreditCard, ICreditCard } from 'app/shared/model/credit-card.model';
import { CreditCardService } from 'app/danhmuc/credit-card';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { DiscountReturnModalService } from 'app/core/login/discount-return-modal.service';
import { JhiEventManager } from 'ng-jhipster';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { IRefVoucher } from 'app/shared/model/ref-voucher.model';
import { PpOrderModalComponent } from 'app/shared/modal/pp-order/pp-order-modal.component';
import { DiscountAllocation, IDiscountAllocation } from 'app/shared/modal/discount-allocation/discount-allocation.model';
import { DiscountAllocationModalComponent } from 'app/shared/modal/discount-allocation/discount-allocation-modal.component';
import { ViewLiabilitiesComponent } from 'app/shared/modal/view-liabilities/view-liabilities.component';
import { PporderdetailService } from 'app/entities/pporderdetail';
import { PPOrderDetail } from 'app/shared/model/pporderdetail.model';
import { IPPOrderDto } from 'app/shared/modal/pp-order/pp-order-dto.model';
import { CostAllocation, ICostAllocation } from 'app/shared/modal/cost-allocation/cost-allocation.model';
import { CostAllocationModalComponent } from 'app/shared/modal/cost-allocation/cost-allocation-modal.component';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { IPPInvoiceDetailCost } from 'app/shared/model/pp-invoice-detail-cost.model';
import { ICostVouchersDTO } from 'app/shared/modal/cost-vouchers/cost-vouchers-dto.model';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { DatePipe } from '@angular/common';
import { PPOrderModalService } from 'app/shared/modal/pp-order/pp-order-modal.service';
import { IMCPayment } from 'app/shared/model/mc-payment.model';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { MCPaymentService } from 'app/TienMatNganHang/phieu-chi/mc-payment';
import { MBTellerPaperService } from 'app/TienMatNganHang/BaoNo/mb-teller-paper';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { ROLE } from 'app/role.constants';
import { el } from '@angular/platform-browser/testing/src/browser_util';
import { IMBCreditCard } from 'app/shared/model/mb-credit-card.model';
import { MBCreditCardService } from 'app/TienMatNganHang/TheTinDung/mb-credit-card';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { DATE_FORMAT_SLASH, ITEMS_PER_PAGE } from 'app/shared';
import { RSInwardOutwardService } from 'app/entities/rs-inward-outward/rs-inward-outward.service';
import { EbMaterialGoodsSpecificationsModalComponent } from 'app/shared/modal/material-goods-specifications/material-goods-specifications.component';

@Component({
    selector: 'eb-pp-invoice-update',
    templateUrl: './pp-invoice-update.component.html',
    styleUrls: ['pp-invoice-detail.css']
})
export class PPInvoiceUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewChecked, AfterViewInit {
    @ViewChild('quantityLargerModal') quantityLargerModal: any;
    @ViewChild('deleteItem') deleteItem: any;
    @ViewChild('unRecordModal') unRecordModal: any;
    @ViewChild('content') content: any;
    @ViewChild('contentVAT') contentVATModal: TemplateRef<any>;
    // pPInvoice: IPPInvoice;
    pPInvoice: any;
    dateDp: any;
    postedDateDp: any;
    isPay: any; // true: thanh toán, false: chưa thanh toán
    typePay: any;
    accountingObjects: IAccountingObject[];
    accountingObject: IAccountingObject;
    employees: IAccountingObject[];
    currencies: ICurrency[];
    currency: ICurrency;
    employee: IAccountingObject;
    materialGoodss: any[];
    repositorys: any[];
    debitAccountList: any;
    creditAccountList: any;
    units: Unit[];
    mainUnitName: string;
    active = 1;
    noFBook: string;
    noFBookRS: string;
    date: Moment;
    postedDate: Moment;
    exchangeRate: number;
    account: any;
    amount: any;
    amountOriginal: any;
    goodsServicePurchases: IGoodsServicePurchase[];
    goodsServicePurchase: IGoodsServicePurchase;
    expenseItemList: IExpenseItem[];
    costSetList: ICostSet[];
    emContractList: IEMContract[];
    budgetItemList: IBudgetItem[];
    organizationUnits: any;
    statisticCodes: any;
    importTaxAccountList: any[];
    specialConsumeTaxAccountList: any[];
    vatAccountList: any[];
    deductionDebitAccountList: any[];
    checkData = false;
    isReadOnly = false;
    bankAccountDetails: BankAccountDetails[];
    bankAccountDetailReceivers: IAccountingObjectBankAccount[];
    creditBankAccountDetailReceivers: IAccountingObjectBankAccount[];
    creditCards: CreditCard[];
    PPINVOICE_TYPE = PPINVOICE_TYPE;
    searchSnapShot: any;
    pPInvoices: IPPInvoice[];
    ppInvoiceDetails: IPPInvoiceDetails[];
    viewVouchersSelected: any[];
    modalRef: NgbModalRef;
    eventSubscriber: Subscription;
    invoiceTemplateDefault = '01/';
    contextMenu: ContextMenu;
    page: number;
    itemsPerPage: number;
    pageCount: number;
    totalItems: number;
    rowNum: number;
    predicate: any;
    reverse: any;
    accountingObjectName: any;
    searchData: string;
    dataSession: IDataSessionStorage;
    checkModalRef: NgbModalRef;
    unRecordCheck: NgbModalRef;
    sumQuantity: number;
    sumUnitPriceOriginal: number;
    sumUnitPrice: number;
    sumMainQuantity: number;
    sumMainUnitPrice: number;
    sumAmountOriginal: number;
    sumFreightAmountOriginal: number;
    displayBook: number;
    isClosing: boolean;
    isMove: boolean;
    isClosed: boolean;
    sumCustomUnitPrice: number;
    ppInvoiceCopy: any;
    ppInvoiceDetailsCopy: any[];
    isFromRSInward: boolean;
    componentType: number;
    routerLink: string;
    PPINVOICE_COMPONENT_TYPE = PPINVOICE_COMPONENT_TYPE;
    isFromRSInwardOutward: boolean;
    rsDataSession: any;
    rsSearchData: any;
    rsTotalItems: any;
    isViewRSInWardOutward: boolean;
    NHAP_KHO = TypeID.NHAP_KHO;
    NHAP_KHO_TU_DIEU_CHINH = TypeID.NHAP_KHO_TU_DIEU_CHINH;
    NHAP_KHO_TU_MUA_HANG = TypeID.NHAP_KHO_TU_MUA_HANG;
    NHAP_KHO_TU_BAN_HANG_TRA_LAI = TypeID.NHAP_KHO_TU_BAN_HANG_TRA_LAI;
    rsRowNum: any;
    isMoreForm: boolean;
    isLoading = false;
    ROLE = ROLE;
    isCreateUrl: boolean;
    isEditUrl: boolean;
    itemUnSelected: any[];

    /*Phiếu chi Add by Hautv*/
    fromMCPayment: boolean;
    rowNumberMCPayment: number;
    countMCPayment: number;
    searchVoucher: ISearchVoucher;
    mCPaymentID: string;
    TYPE_MC_PAYMENT = 110;
    TYPE_PPSERVICE = 114;
    TYPE_PPINVOICE_MHQK = 115;
    /*Phiếu chi Add by Hautv*/

    /*Thẻ tín dụng*/
    fromMBCreditCard: boolean;
    rowNumberMBCreditCard: number;
    countMBCreditCard: number;
    mBCreditCardID: string;
    TYPE_MB_CREDIT_CARD = 170;
    TYPE_MB_CREDIT_CARD_MUA_HANG = 171;
    TYPE_MB_CREDIT_CARD_MUA_DV = 173;
    /*Thẻ tín dụng*/

    /*Bao No Add by Anmt*/
    fromMBTellerPaper: boolean;
    rowNumberMBTellerPaper: number;
    countMBTellerPaper: number;
    mBTellerPaperID: string;
    TYPE_BAONO_UNC = 120;
    TYPE_BAONO_SCK = 130;
    TYPE_BAONO_STM = 140;
    TYPE_UNC_PPINVOICE_MHQK = 125;
    TYPE_SCK_PPINVOICE_MHQK = 131;
    TYPE_STM_PPINVOICE_MHQK = 141;
    TYPE_UNC_PPSERVICE = 124;
    TYPE_SCK_PPSERVICE = 133;
    TYPE_STM_PPSERVICE = 143;
    /*end Bao No*/

    typeAmountOriginal = true;
    isCheckPPOrderQuantity: boolean;
    isKho = true; // true: mua hàng qua kho, false: mua hàng không qua kho
    isChangeReason = false;
    isChangeReasonRs = false;
    isChangeOtherReason = false;
    isPlayVendor = false;
    isUsed = false;
    isEdit: boolean;

    dataVATRate = [{ name: '0%', data: 0 }, { name: '5%', data: 1 }, { name: '10%', data: 2 }];
    dataAccount: any;
    showVaoSo: boolean;

    checkVATError = 1;
    isCheckVat: boolean;
    isNew: boolean;
    lisTempPPOrder: any[];
    taxCalculationMethod: boolean; // phương pháp tính thuế
    MATERIAL_GOODS_TYPE = MATERIAL_GOODS_TYPE;
    listIDInputDetail: any[] = [
        'materialGoods',
        'description',
        'repository',
        'debitAccount',
        'creditAccount',
        'accountingObjectHT',
        'unit',
        'quantity',
        'unitPrice',
        'unitPriceQd',
        'mainUnitID',
        'mainConvertRate',
        '',
        'mainQuantity',
        'mainUnitPrice',
        'amountOriginal',
        'detailAmount',
        'discountRate',
        'discountAmountOriginal',
        'discountAmount',
        'freightAmount',
        'importTaxExpenseAmount',
        'inwardAmount',
        'lotNo',
        'expiryDate',
        '',
        // thuế 26
        'materialGoodTax',
        'vatDescription',
        'debitAccountTax',
        'creditAccountTax',
        'accountingObjectHTTax',
        'customUnitPrice',
        'importTaxRate',
        'importTaxAmount',
        'importTaxAccountItem',
        'specialConsumeTaxRate',
        'specialConsumeTaxAmount',
        'specialConsumeTaxAccountItem',
        'vatRate',
        'vatAmountOriginal',
        'vatAmount',
        'vatAccountItem',
        'deductionDebitAccountItem',
        'invoiceTemplate',
        'invoiceSeries',
        'invoiceNo',
        'invoiceDate',
        'goodsServicePurchase',
        // thống kê 48
        'materialGoodTK',
        'descriptionTK',
        'expenseItem',
        'costSetItem',
        'budgetItem',
        'organizationUnit',
        'statisticsCode'
    ];
    indexFocusDetailCol: any;
    indexFocusDetailRow: any;
    currencyCode: string;
    invoiceTypes: any[];
    GROUP_ID = GROUP_TYPEID;
    constructor(
        private pPInvoiceService: PPInvoiceService,
        private accountingObjectService: AccountingObjectService,
        public utilsService: UtilsService,
        private currencyService: CurrencyService,
        private materialGoodsService: MaterialGoodsService,
        private repositoryService: RepositoryService,
        private accountListService: AccountListService,
        private accountingObjectBankAccountService: AccountingObjectBankAccountService,
        private unitService: UnitService,
        private toastrService: ToastrService,
        public translateService: TranslateService,
        private discountReturnModalService: DiscountReturnModalService,
        private materialGoodsConvertUnitService: MaterialGoodsConvertUnitService,
        private principal: Principal,
        private router: Router,
        public datepipe: DatePipe,
        private pporderdetailService: PporderdetailService,
        private eventManager: JhiEventManager,
        private refModalService: RefModalService,
        private modalService: NgbModal,
        private generalLedgerService: GeneralLedgerService,
        private bankAccountDetailService: BankAccountDetailsService,
        private emContractService: EMContractService,
        private costSetService: CostSetService,
        private statisticsCodeService: StatisticsCodeService,
        private organizationUnitService: OrganizationUnitService,
        private budgetItemService: BudgetItemService,
        private creditCardService: CreditCardService,
        private expenseItemService: ExpenseItemService,
        private rsInwardOutwardService: RSInwardOutwardService,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        private activatedRoute: ActivatedRoute,
        private ppOrderModalService: PPOrderModalService,
        private mCPaymentService: MCPaymentService,
        private mBCreditCardService: MBCreditCardService,
        private mBTellerPaperService: MBTellerPaperService
    ) {
        super();
        this.contextMenu = new ContextMenu();
    }

    ngOnInit() {
        this.registerChangeSession();
        this.ppOrderModalService.cleanData();
        this.initValues();
        this.getAccount();
        this.getData();
        this.registerRef();
        /*Nhap kho by Huypq*/
        this.isViewRSInWardOutward = window.location.href.includes('/edit-rs-inward');
        if (this.isViewRSInWardOutward) {
            this.getSessionDataRS();
        }
        /*Nhap kho by Huypq*/
        this.resetSession();

        /*Phiếu chi Add by Hautv*/
        this.fromMCPayment = this.activatedRoute.snapshot.routeConfig.path.includes('from-mc-payment');
        if (this.fromMCPayment) {
            this.searchVoucher = JSON.parse(sessionStorage.getItem('searchVoucherMCPayment'));
            this.mCPaymentID = this.activatedRoute.snapshot.paramMap.get('mcpaymentID');
            this.utilsService
                .getIndexRow({
                    id: this.mCPaymentID,
                    isNext: true,
                    typeID: this.TYPE_MC_PAYMENT,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<any[]>) => {
                        this.rowNumberMCPayment = res.body[0];
                        if (res.body.length === 1) {
                            this.countMCPayment = 1;
                        } else {
                            this.countMCPayment = res.body[1];
                        }
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
        /*Phiếu chi Add by Hautv*/

        /*Thẻ tín dụng*/
        this.fromMBCreditCard = this.activatedRoute.snapshot.routeConfig.path.includes('from-mb-credit-card');
        if (this.fromMBCreditCard) {
            this.searchVoucher = JSON.parse(sessionStorage.getItem('dataSearchMBCreditCard'));
            this.mBCreditCardID = this.activatedRoute.snapshot.paramMap.get('mBCreditCardID');
            this.utilsService
                .getIndexRow({
                    id: this.mBCreditCardID,
                    isNext: true,
                    typeID: this.TYPE_MB_CREDIT_CARD,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<any[]>) => {
                        this.rowNumberMBCreditCard = res.body[0];
                        if (res.body.length === 1) {
                            this.countMBCreditCard = 1;
                        } else {
                            this.countMBCreditCard = res.body[1];
                        }
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
        /*Thẻ tín dụng*/

        /*Bao No Add by anmt*/
        this.fromMBTellerPaper = this.activatedRoute.snapshot.routeConfig.path.includes('from-mb-teller-paper');
        if (this.fromMBTellerPaper) {
            this.searchVoucher = JSON.parse(sessionStorage.getItem('sessionSearchVoucher'));
            this.mBTellerPaperID = this.activatedRoute.snapshot.paramMap.get('mBTellerPaperID');
            this.mBTellerPaperService
                .getIndexRow({
                    id: this.mBTellerPaperID,
                    searchVoucher: this.searchVoucher ? JSON.stringify(this.searchVoucher) : null
                })
                .subscribe(
                    (res: any) => {
                        this.rowNumberMBTellerPaper = res.body[0];
                        this.countMBTellerPaper = res.body[1];
                        console.log('mua hang: ' + res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
        /*Bao No Add by anmt*/

        // Nhận event khi thêm nhanh
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilsService.setShowPopup(response.content);
        });
        this.registerIsShowPopup();
        this.registerCopyRow();
        this.registerSelectMaterialGoodsSpecification();
        this.registerMaterialGoodsSpecifications();
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const lst = this.listIDInputDetail;
                const col = this.indexFocusDetailCol;
                const row = this.pPInvoice.ppInvoiceDetails.length - 1;
                this.indexFocusDetailRow = row;
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(lst[col] + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
            this.changeAmountPPInvoice();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerIsShowPopup() {
        this.utilsService.checkEvent.subscribe(res => {
            this.isShowPopup = res;
        });
    }

    registerLockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('lockSuccess', response => {
            this.isReadOnly = true;
            this.refreshData();
        });
    }

    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.isReadOnly = true;
            this.refreshData();
        });
    }

    ngAfterViewInit(): void {
        if (this.dataSession && this.dataSession.isEdit) {
            this.focusFirstInput();
        }
    }

    ngAfterViewChecked(): void {
        this.disableInput();
        if (this.isPay && this.checkData && this.isPlayVendor) {
            this.isPay = false;
            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.payVendorDuplicate'));
            return;
        }
    }

    registerChangeSession() {
        this.eventManager.subscribe('changeSession', response => {
            this.isClosed = true;
        });
    }

    resetSession() {
        this.isCheckPPOrderQuantity = false;
        this.getAccount();
    }

    getSessionDataRS() {
        this.rsDataSession = JSON.parse(sessionStorage.getItem('nhapKhoDataSession'));
        if (this.rsDataSession) {
            this.isFromRSInwardOutward = true;
            this.rsTotalItems = this.rsDataSession.totalItems;
            this.rsRowNum = this.rsDataSession.rowNum;
            this.rsSearchData = this.rsDataSession.searchVoucher;
        } else {
            this.rsDataSession = null;
        }
    }

    getData() {
        // this.getAccount();
        this.getAccountingObject();
        this.getEmployees();
        // this.getCurrency();
        // this.getSoChungTu(PPINVOICE_TYPE.GROUP_CHUNG_TU_MUA_HANG);
        // this.getSoChungTu(PPINVOICE_TYPE.GROUP_PHIEU_NHAP_KHO);
        this.getMaterialGoods();
        this.getRepository();
        // this.getDebitAccount();
        this.getGoodsServicePurchase();
        this.getExpense();
        this.getCostSet();
        this.getEmContract();
        this.getBudget();
        this.getOrganizationUni();
        this.getStatisticsCode();
        this.onGetAllBankAccountDetails();
        this.onGetAllCreditCards();
        this.getDefaultGoodsServicePurchase();
    }

    initValues() {
        this.getSessionData();
        this.lisTempPPOrder = [];
        this.isEdit = true;
        this.isMoreForm = false;
        this.viewVouchersSelected = [];
        this.pPInvoices = [];
        this.searchSnapShot = {};
        this.pPInvoice = { ppInvoiceDetails: [], refVouchers: [], ppInvoiceDetailCost: [] };
        this.account = { organizationUnit: {} };
        this.currency = {};
        this.isPay = false;
        this.isLoading = false;
        // this.rsInwardOutward = {};
        this.accountingObjects = [];
        this.accountingObject = {};
        this.employees = [];
        this.currencies = [];
        this.materialGoodss = [];
        this.isCreateUrl =
            window.location.href.includes('/mua-hang/qua-kho/new') || window.location.href.includes('/mua-hang/khong-qua-kho/new');
        this.isEditUrl = window.location.href.includes('/edit');
        this.setDefaultDataFromSystemOptions();
    }

    getAccount() {
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.componentType = data.componentType ? data.componentType : PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO;
                this.getSessionData();
                this.getRouterLink();
                this.account = account;
                if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                    this.currencyCode = this.account.organizationUnit.currencyID;
                }
                this.showVaoSo = this.account.systemOption.some(x => x.code === SU_DUNG_SO_QUAN_TRI && x.data === '1');

                if (this.account) {
                    if (data.pPInvoice && data.pPInvoice.id) {
                        this.pPInvoice = data.pPInvoice;
                        this.loadDataPPInvoice();
                        // this.pPInvoice.ppInvoiceDetails.forEach(item => {
                        //     item.ppOrderNo = item.ppOrderId;
                        // })

                        /*check edit tu nhap kho*/
                        if (this.rsDataSession && this.rsDataSession.isEdit) {
                            this.edit();
                        }
                    } else {
                        this.pPInvoice = {
                            ppInvoiceDetails: [],
                            billReceived: false,
                            isImportPurchase: false,
                            typeId: PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN,
                            typeGroup: PPINVOICE_TYPE.GROUP_CHUNG_TU_MUA_HANG,
                            typeLedger: 2,
                            refVouchers: [],
                            ppInvoiceDetailCost: []
                        };
                        if (!this.showVaoSo) {
                            this.pPInvoice.typeLedger = 0;
                        }
                        if (this.isKho) {
                            this.translateService.get(['ebwebApp.pPInvoice.reasonDefault.quaKho']).subscribe((res: any) => {
                                this.pPInvoice.reason = res['ebwebApp.pPInvoice.reasonDefault.quaKho'];
                            });
                            this.translateService.get(['ebwebApp.pPInvoice.reasonDefault.kho']).subscribe((res: any) => {
                                this.pPInvoice.reasonRs = res['ebwebApp.pPInvoice.reasonDefault.kho'];
                            });
                        } else {
                            this.translateService.get(['ebwebApp.pPInvoice.reasonDefault.khongQuaKho']).subscribe((res: any) => {
                                this.pPInvoice.reason = res['ebwebApp.pPInvoice.reasonDefault.khongQuaKho'];
                            });
                        }

                        this.isReadOnly = false;
                        this.isEdit = !this.isReadOnly;
                        this.checkData = false;
                        this.taxCalculationMethod = this.account.organizationUnit.taxCalculationMethod === 1;
                        this.pPInvoice.currentBook = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                        this.getSoChungTu(PPINVOICE_TYPE.GROUP_CHUNG_TU_MUA_HANG);
                        this.getSoChungTu(PPINVOICE_TYPE.GROUP_PHIEU_NHAP_KHO);
                        this.onLoadReceipt();
                    }

                    this.getAllAccount();
                    if (this.account.organizationUnit) {
                        this.setDefaultDataFromSystemOptions();
                    }
                }
            });
        });
    }

    checkPayVendor() {
        this.pPInvoiceService.checkPayVendor({ id: this.pPInvoice.id }).subscribe((res: HttpResponse<any>) => {
            if (res.body.message === UpdateDataMessages.SUCCESS) {
                this.isPlayVendor = false;
            } else if (res.body.message === UpdateDataMessages.DUPLICATE) {
                this.isPlayVendor = true;
            }
        });
    }

    changeReason() {
        if (this.pPInvoice.accountingObject && this.pPInvoice.accountingObjectName) {
            if (!this.isChangeReason) {
                if (this.isKho) {
                    this.translateService
                        .get(['ebwebApp.pPInvoice.reasonDefault.quaKhoOf'], {
                            name: this.pPInvoice.accountingObjectName
                        })
                        .subscribe((res: any) => {
                            this.pPInvoice.reason = res['ebwebApp.pPInvoice.reasonDefault.quaKhoOf'];
                        });
                } else {
                    this.translateService
                        .get(['ebwebApp.pPInvoice.reasonDefault.khongQuaKhoOf'], {
                            name: this.pPInvoice.accountingObjectName
                        })
                        .subscribe((res: any) => {
                            this.pPInvoice.reason = res['ebwebApp.pPInvoice.reasonDefault.khongQuaKhoOf'];
                        });
                }
            }
            if (this.isKho && !this.isChangeReasonRs) {
                this.translateService
                    .get(['ebwebApp.pPInvoice.reasonDefault.khoOf'], {
                        name: this.pPInvoice.accountingObjectName
                    })
                    .subscribe((res: any) => {
                        this.pPInvoice.reasonRs = res['ebwebApp.pPInvoice.reasonDefault.khoOf'];
                    });
            }
            if (this.pPInvoice.typeId !== PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN) {
                if (!this.isChangeOtherReason) {
                    if (this.pPInvoice.typeId === PPINVOICE_TYPE.TYPE_ID_TIEN_MAT) {
                        this.translateService
                            .get(['ebwebApp.pPInvoice.reasonDefault.phieuChiOf'], {
                                name: this.pPInvoice.accountingObjectName
                            })
                            .subscribe((res: any) => {
                                this.pPInvoice.otherReason = res['ebwebApp.pPInvoice.reasonDefault.phieuChiOf'];
                            });
                    } else {
                        this.translateService
                            .get(['ebwebApp.pPInvoice.reasonDefault.uyNhiemChiOf'], {
                                name: this.pPInvoice.accountingObjectName
                            })
                            .subscribe((res: any) => {
                                this.pPInvoice.otherReason = res['ebwebApp.pPInvoice.reasonDefault.uyNhiemChiOf'];
                            });
                    }
                }
            } else {
                this.pPInvoice.otherReason = null;
            }
        } else {
            if (this.pPInvoice.typeId !== PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN) {
                if (!this.isChangeOtherReason) {
                    if (this.pPInvoice.typeId === PPINVOICE_TYPE.TYPE_ID_TIEN_MAT) {
                        this.translateService.get(['ebwebApp.pPInvoice.reasonDefault.phieuChi']).subscribe((res: any) => {
                            this.pPInvoice.otherReason = res['ebwebApp.pPInvoice.reasonDefault.phieuChi'];
                        });
                    } else {
                        this.translateService.get(['ebwebApp.pPInvoice.reasonDefault.uyNhiemChi']).subscribe((res: any) => {
                            this.pPInvoice.otherReason = res['ebwebApp.pPInvoice.reasonDefault.uyNhiemChi'];
                        });
                    }
                }
            } else {
                this.pPInvoice.otherReason = null;
            }
        }
    }

    getRouterLink() {
        switch (this.componentType) {
            case PPINVOICE_COMPONENT_TYPE.REF_QUA_KHO:
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO:
                this.isKho = true;
                this.routerLink = '/mua-hang/qua-kho';
                break;
            case PPINVOICE_COMPONENT_TYPE.REF_KHONG_QUA_KHO:
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO:
                this.routerLink = '/mua-hang/khong-qua-kho';
                this.isKho = false;
                break;
            default:
                this.isKho = true;
                this.routerLink = '/mua-hang/qua-kho';
        }
    }

    loadDataPPInvoice() {
        this.checkPayVendor();
        this.isReadOnly = true;
        this.isEdit = !this.isReadOnly;
        this.checkData = true;
        this.pPInvoice.currentBook = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
        if (this.pPInvoice.typeId !== PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN) {
            this.isPay = true;
        } else {
            this.isPay = false;
        }

        if (this.pPInvoice.currentBook === '1') {
            this.pPInvoice.noFBook = this.pPInvoice.noMBook;
            this.pPInvoice.noFBookRs = this.pPInvoice.noMBookRs;
            this.pPInvoice.otherNoFBook = this.pPInvoice.otherNoMBook;
        }

        this.pPInvoice.isImportPurchase = !!this.pPInvoice.importPurchase;
        for (const item of this.pPInvoice.ppInvoiceDetails) {
            item.quantityFromDB = item.quantity;
        }
        this.copyPPInvoice();
        // this.sumAmount();
        this.onLoadBankAccountDetailReceiversByAccountingObjectId();
        this.getPPOrderNo();

        // tính tổng tiền thanh toán
        // nhập khẩu
        if (this.pPInvoice.isImportPurchase) {
            this.amount = this.pPInvoice.totalAmount - this.pPInvoice.totalDiscountAmount;
            this.amountOriginal = this.pPInvoice.totalAmountOriginal - this.pPInvoice.totalDiscountAmountOriginal;
        } else {
            // trong nước
            this.amount = this.pPInvoice.totalAmount - this.pPInvoice.totalDiscountAmount + this.pPInvoice.totalVATAmount;
            this.amountOriginal =
                this.pPInvoice.totalAmountOriginal - this.pPInvoice.totalDiscountAmountOriginal + this.pPInvoice.totalVATAmountOriginal;
        }
        this.amount = this.utilsService.round(this.amount, this.account.systemOption, 7);
        this.amountOriginal = this.utilsService.round(this.amountOriginal, this.account.systemOption, this.typeAmountOriginal ? 7 : 8);

        this.sumQuantity = 0;
        this.sumUnitPriceOriginal = 0;
        this.sumUnitPrice = 0;
        this.sumMainQuantity = 0;
        this.sumMainUnitPrice = 0;
        this.sumAmountOriginal = 0;
        this.sumFreightAmountOriginal = 0;

        // tổng giá tính thuế
        this.sumCustomUnitPrice = 0;

        for (const detail of this.pPInvoice.ppInvoiceDetails) {
            detail.quantity = detail.quantity ? detail.quantity : 0;
            detail.unitPriceOriginal = detail.unitPriceOriginal ? detail.unitPriceOriginal : 0;
            detail.unitPrice = detail.unitPrice ? detail.unitPrice : 0;
            detail.mainQuantity = detail.mainQuantity ? detail.mainQuantity : 0;
            detail.mainUnitPrice = detail.mainUnitPrice ? detail.mainUnitPrice : 0;
            detail.freightAmountOriginal = detail.freightAmountOriginal ? detail.freightAmountOriginal : 0;
            detail.customUnitPrice = detail.customUnitPrice ? detail.customUnitPrice : 0;

            this.sumQuantity += detail.quantity;
            this.sumUnitPriceOriginal += detail.unitPriceOriginal;
            this.sumUnitPrice += detail.unitPrice;
            this.sumMainQuantity += detail.mainQuantity;
            this.sumMainUnitPrice += detail.mainUnitPrice;
            // chi phí mua
            this.sumFreightAmountOriginal += detail.freightAmountOriginal;
            // giá tính thuế
            this.sumCustomUnitPrice += detail.customUnitPrice;
        }
        // todo check lại ctrl e để sửa
        // if (this.dataSession && this.dataSession.isEdit) {
        //     this.edit();
        // }
    }

    private getPPOrderNo() {
        if (this.pPInvoice.ppInvoiceDetails) {
            this.pPInvoice.ppInvoiceDetails.forEach(item => {
                if (item.ppOrderId) {
                    this.pPInvoiceService.getPPOrderNo({ id: item.ppOrderId }).subscribe((res: HttpResponse<any>) => {
                        item.ppOrderNo = res.body.result;
                    });
                }
            });
        }
    }

    getAllAccount() {
        // this.getAccountType(AccountType.TK_CO);
        // this.getAccountType(AccountType.TK_NO);
        // this.getAccountType(AccountType.TK_THUE_NK);
        // this.getAccountType(AccountType.TK_THUE_TTDB);
        // this.getAccountType(AccountType.TK_THUE_GTGT);
        // this.getAccountType(AccountType.TKDU_THUE_GTGT);
        this.getAccountType();
    }

    changeImportPurchase() {
        // this.changeAccount();
        this.pPInvoice.ppInvoiceDetails.forEach(detail => {
            detail.importTaxExpenseAmount = 0;
            if (this.pPInvoice.isImportPurchase && detail.materialGood && detail.materialGood.importTaxRate) {
                detail.importTaxRate = detail.materialGood.importTaxRate;
            } else {
                detail.importTaxRate = 0;
            }
            this.giaTinhThue(detail);
            this.tienThueNk(detail);
            this.tienThueTTDB(detail);
            this.tienThueGTGTQD(detail);
            this.tienThueGTGT(detail);
            this.giaNhapKho(detail);
        });
        this.changeAmountPPInvoice();
    }

    onLoadReceipt() {
        if (this.account) {
            this.pPInvoice.date = this.utilsService.ngayHachToan(this.account);
            this.pPInvoice.postedDate = this.pPInvoice.date;
            if (!this.checkData) {
                this.copyPPInvoice();
            }
        }
    }

    changeTypeId(isCopyAndNew?) {
        if (this.pPInvoice.typeId) {
            if (this.pPInvoice.typeId === PPINVOICE_TYPE.TYPE_ID_TIEN_MAT) {
                this.getSoChungTu(PPINVOICE_TYPE.GROUP_PHIEU_CHI);
            } else if (this.pPInvoice.typeId === PPINVOICE_TYPE.TYPE_ID_UY_NHIEM_CHI) {
                this.getSoChungTu(PPINVOICE_TYPE.GROUP_UY_NHIEM_CHI);
            } else if (this.pPInvoice.typeId === PPINVOICE_TYPE.TYPE_ID_SEC_CK) {
                this.getSoChungTu(PPINVOICE_TYPE.GROUP_SEC_CHUYEN_KHOAN);
            } else if (this.pPInvoice.typeId === PPINVOICE_TYPE.TYPE_ID_SEC_TIEN_MAT) {
                this.getSoChungTu(PPINVOICE_TYPE.GROUP_SEC_TIEN_MAT);
            } else if (this.pPInvoice.typeId === PPINVOICE_TYPE.TYPE_ID_THE_TIN_DUNG) {
                this.getSoChungTu(PPINVOICE_TYPE.GROUP_THE_TIN_DUNG);
            }
            this.changeReason();
            if (!isCopyAndNew) {
                // sử dụng chức năng copy thì ko thay đổi tk
                this.changeAccount();
            }
        }
    }

    getSessionData() {
        switch (this.componentType) {
            case PPINVOICE_COMPONENT_TYPE.REF_QUA_KHO:
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO:
                this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceRSIDataSession'));
                break;
            case PPINVOICE_COMPONENT_TYPE.REF_KHONG_QUA_KHO:
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO:
                this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceDataSession'));
                break;
            default:
                this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceRSIDataSession'));
        }
        // this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceDataSession'));
        if (!this.dataSession) {
            // this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceSearchData'));
            switch (this.componentType) {
                case PPINVOICE_COMPONENT_TYPE.REF_QUA_KHO:
                case PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO:
                    this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceRSISearchData'));
                    break;
                case PPINVOICE_COMPONENT_TYPE.REF_KHONG_QUA_KHO:
                case PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO:
                    this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceSearchData'));
                    break;
                default:
                    this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceRSISearchData'));
            }
        }

        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.searchData = this.dataSession.searchVoucher;
            this.predicate = this.dataSession.predicate;
            this.accountingObjectName = this.dataSession.accountingObjectName;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
    }

    setDefaultDataFromSystemOptions() {
        if (this.account) {
            // this.ppOrder.date = this.utilsService.ngayHachToan(this.account);
            if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                if (!this.checkData) {
                    this.pPInvoice.currencyId = this.account.organizationUnit.currencyID;
                    this.copyPPInvoice();
                }
                this.getActiveCurrencies();
            }
        }
    }

    // tham chiếu
    getRefVoucherByRefId() {
        this.pPInvoiceService.findRefVoucherByRefId({ refId: this.pPInvoice.id }).subscribe((res: HttpResponse<IRefVoucher[]>) => {
            this.pPInvoice.refVouchers = res.body;
        });
    }

    // phân bổ chi phí
    getPPInvoiceDetailCost() {
        this.pPInvoiceService
            .getPPInvoiceDetailCost({ refId: this.pPInvoice.id })
            .subscribe((res: HttpResponse<IPPInvoiceDetailCost[]>) => {
                this.pPInvoice.ppInvoiceDetailCost = res.body;
            });
    }

    //
    onGetAllCreditCards() {
        this.creditCardService.getCreditCardsActiveByCompanyID().subscribe((res: HttpResponse<ICreditCard[]>) => {
            this.creditCards = res.body.filter(n => n.isActive);
            if (this.checkData) {
                this.pPInvoice.creditCardItem = this.creditCards.find(n => n.creditCardNumber === this.pPInvoice.creditCardNumber);
                if (this.pPInvoice.creditCardItem) {
                    this.pPInvoice.creditCardType = this.pPInvoice.creditCardItem.creditCardType;
                    this.pPInvoice.ownerCreditCard = this.pPInvoice.creditCardItem.ownerCard;
                    this.pPInvoice.creditCardNumber = this.pPInvoice.creditCardItem.creditCardNumber;
                }
            }
        });
    }

    //
    private onLoadBankAccountDetailReceiversByAccountingObjectId() {
        if (this.pPInvoice.accountingObjectId) {
            this.accountingObjectBankAccountService
                .getByAccountingObjectById({
                    accountingObjectID: this.pPInvoice.accountingObjectId
                })
                .subscribe(ref => {
                    this.bankAccountDetailReceivers = ref.body;
                    this.creditBankAccountDetailReceivers = ref.body;
                    if (this.checkData) {
                        this.pPInvoice.accountReceiver = this.bankAccountDetailReceivers.find(
                            n => n.id === this.pPInvoice.accountReceiverId
                        );
                        this.pPInvoice.bankAccountReceiverItem = this.creditBankAccountDetailReceivers.find(
                            n => n.id === this.pPInvoice.bankAccountReceiverId
                        );
                        if (this.pPInvoice.accountReceiver) {
                            this.pPInvoice.accountReceiverId = this.pPInvoice.accountReceiver.id;
                            this.pPInvoice.accountReceiverName = this.pPInvoice.accountReceiver.bankName;
                        }
                    }
                });
        }
    }

    // tài khoản trả
    onGetAllBankAccountDetails() {
        this.bankAccountDetailService.getBankAccountDetails().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
            this.bankAccountDetails = res.body;
            if (this.checkData) {
                this.pPInvoice.accountPayment = this.bankAccountDetails.find(n => n.id === this.pPInvoice.accountPaymentId);
            }
        });
    }

    // mã thống kê
    getStatisticsCode() {
        this.statisticsCodeService.getStatisticsCodes().subscribe(ref => {
            this.statisticCodes = ref.body;
            if (this.checkData) {
                this.pPInvoice.ppInvoiceDetails.forEach(item => {
                    item.statisticsCodeItem = this.statisticCodes.find(n => n.id === item.statisticCodeId);
                });
            }
        });
    }

    // phòng ban
    getOrganizationUni() {
        this.organizationUnitService.getOrganizationUnits().subscribe(ref => {
            this.organizationUnits = ref.body.sort((a, b) => a.organizationUnitCode.localeCompare(b.organizationUnitCode));
            if (this.checkData) {
                this.pPInvoice.ppInvoiceDetails.forEach(item => {
                    item.organizationUnitItem = this.organizationUnits.find(n => n.id === item.departmentId);
                });
            }
        });
    }

    // mục thu/ chi
    getBudget() {
        this.budgetItemService.getBudgetItems().subscribe(ref => {
            this.budgetItemList = ref.body;
            if (this.checkData) {
                this.pPInvoice.ppInvoiceDetails.forEach(item => {
                    item.budgetItem = this.budgetItemList.find(n => n.id === item.budgetItemId);
                });
            }
        });
    }

    // hợp đồng
    getEmContract() {
        this.emContractService.getEMContracts().subscribe(ref => {
            this.emContractList = ref.body;
            if (this.checkData) {
                this.pPInvoice.ppInvoiceDetails.forEach(item => {
                    item.emContractItem = this.emContractList.find(n => n.id === item.contractId);
                });
            }
        });
    }

    // Đối tượng THCP
    getCostSet() {
        this.costSetService.getCostSets().subscribe(ref => {
            this.costSetList = ref.body;
            if (this.checkData) {
                this.pPInvoice.ppInvoiceDetails.forEach(item => {
                    item.costSetItem = this.costSetList.find(n => n.id === item.costSetId);
                });
            }
        });
    }

    // Khoản mục CP
    getExpense() {
        this.expenseItemService.getExpenseItems().subscribe(ref => {
            this.expenseItemList = ref.body;
            if (this.checkData) {
                this.pPInvoice.ppInvoiceDetails.forEach(item => {
                    item.expenseItem = this.expenseItemList.find(n => n.id === item.expenseItemId);
                });
            }
        });
    }

    // nhóm HHDV mua vào
    getGoodsServicePurchase() {
        this.goodsServicePurchaseService.getGoodServicePurchases().subscribe(ref => {
            this.goodsServicePurchases = ref.body;
            if (this.checkData) {
                this.pPInvoice.ppInvoiceDetails.forEach(detail => {
                    detail.goodsServicePurchase = this.goodsServicePurchases.find(n => n.id === detail.goodsServicePurchaseId);
                });
            }
        });
    }

    //
    getDefaultGoodsServicePurchase() {
        this.goodsServicePurchaseService.getPurchaseName().subscribe(ref => {
            this.goodsServicePurchase = ref.body;
        });
    }

    // tiền tệ
    getActiveCurrencies() {
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencies = res.body;
            if (this.pPInvoice && this.pPInvoice.currencyId) {
                this.currency = this.currencies.find(item => item.currencyCode === this.pPInvoice.currencyId);
            } else if (this.pPInvoice && !this.pPInvoice.currencyId) {
                this.currency = this.currencies[0];
            }
            if (!this.checkData) {
                this.copyPPInvoice();
            }
            this.selectCurrency();
        });
    }

    // đơn vị tính
    getUnit(detail: IPPInvoiceDetails, isInStock?: boolean) {
        this.unitService
            .convertRateForMaterialGoodsComboboxCustom({
                materialGoodsId: detail.materialGoodsId
            })
            .subscribe(
                ref => {
                    detail.units = ref.body;
                    if (isInStock) {
                        if (detail.unitId) {
                            detail.unit = detail.units.find(n => n.id === detail.unitId);
                        } else {
                            detail.mainConvertRate = 1;
                            detail.formula = '*';
                        }
                    }
                    this.getMainUnit(detail, isInStock);
                },
                error => {
                    detail.mainConvertRate = 1;
                    detail.formula = '*';
                }
            );
    }

    // đơn vị tính chính
    getMainUnit(detail: IPPInvoiceDetails, isInStock?: boolean) {
        this.unitService
            .getMainUnitName({
                materialGoodsId: detail.materialGoodsId
            })
            .subscribe(ref => {
                if (!isInStock) {
                    detail.unit = ref.body;
                    detail.unitId = detail.unit.id;
                }
                detail.mainUnitId = ref.body.id;
                detail.mainUnitName = ref.body.unitName;
                this.getUnitPriceOriginal(detail, isInStock);
            });
    }

    // kho
    getRepository() {
        this.repositoryService.findAllByCompanyID().subscribe(
            (res: HttpResponse<IRepository[]>) => {
                this.repositorys = res.body;
                if (this.checkData && this.isKho) {
                    this.pPInvoice.ppInvoiceDetails.forEach(item => {
                        item.repository = this.repositorys.find(n => n.id === item.repositoryId);
                    });
                }
            },
            (subRes: HttpErrorResponse) => this.onError(subRes.message)
        );
    }

    // lấy danh sách các loại tài khoản
    getAccountType() {
        const columnList = [
            { column: AccountType.TK_CO, ppType: this.pPInvoice.isImportPurchase },
            { column: AccountType.TK_NO, ppType: this.pPInvoice.isImportPurchase },
            { column: AccountType.TK_THUE_NK, ppType: this.pPInvoice.isImportPurchase },
            { column: AccountType.TK_THUE_TTDB, ppType: this.pPInvoice.isImportPurchase },
            { column: AccountType.TK_THUE_GTGT, ppType: this.pPInvoice.isImportPurchase },
            { column: AccountType.TKDU_THUE_GTGT, ppType: this.pPInvoice.isImportPurchase }
        ];
        const param = {
            typeID: this.pPInvoice.typeId,
            columnName: columnList
        };
        this.accountListService.getAccountTypeFour(param).subscribe(res => {
            this.dataAccount = res.body;
            this.creditAccountList = this.dataAccount.creditAccount;
            this.debitAccountList = this.dataAccount.debitAccount;
            this.importTaxAccountList = this.dataAccount.importTaxAccount;
            this.specialConsumeTaxAccountList = this.dataAccount.specialConsumeTaxAccount;
            this.vatAccountList = this.dataAccount.vatAccount;
            this.deductionDebitAccountList = this.dataAccount.deductionDebitAccount;

            if (this.checkData) {
                for (const item of this.pPInvoice.ppInvoiceDetails) {
                    item.creditAccountItem = this.creditAccountList.find(n => n.accountNumber === item.creditAccount);
                    item.debitAccountItem = this.debitAccountList.find(n => n.accountNumber === item.debitAccount);
                    item.importTaxAccountItem = this.importTaxAccountList.find(n => n.accountNumber === item.importTaxAccount);
                    item.specialConsumeTaxAccountItem = this.specialConsumeTaxAccountList.find(
                        n => n.accountNumber === item.specialConsumeTaxAccount
                    );
                    item.vatAccountItem = this.vatAccountList.find(n => n.accountNumber === item.vatAccount);
                    item.deductionDebitAccountItem = this.deductionDebitAccountList.find(
                        n => n.accountNumber === item.deductionDebitAccount
                    );
                }
            }

            if (!this.isReadOnly) {
                if (this.pPInvoice.ppInvoiceDetails && this.pPInvoice.ppInvoiceDetails.length) {
                    for (const item of this.pPInvoice.ppInvoiceDetails) {
                        this.changeCreditAccountWhenChangeTypeId(item);
                    }
                }
            }
        });
    }

    changeActive(active) {
        this.active = active;
    }

    changeAccount() {
        if (this.pPInvoice.ppInvoiceDetails && this.pPInvoice.ppInvoiceDetails.length) {
            this.pPInvoice.ppInvoiceDetails.forEach(item => {
                item.debitAccountItem = null;
                item.debitAccount = null;

                item.creditAccountItem = null;
                item.creditAccount = null;

                item.importTaxAccountItem = null;
                item.importTaxAccount = null;

                item.specialConsumeTaxAccountItem = null;
                item.specialConsumeTaxAccount = null;

                item.vatAccountItem = null;
                item.vatAccount = null;

                item.deductionDebitAccountItem = null;
                item.deductionDebitAccount = null;
            });
        }
        this.getAllAccount();
    }

    getSoChungTu(groupId) {
        this.utilsService
            .getGenCodeVoucher({
                typeGroupID: groupId,
                displayOnBook: this.pPInvoice.currentBook
            })
            .subscribe((res: HttpResponse<string>) => {
                // this.mCReceipt.noFBook = (res.body.toString());
                // console.log(res.body);
                if (groupId === PPINVOICE_TYPE.GROUP_CHUNG_TU_MUA_HANG) {
                    this.pPInvoice.noFBook = res.body;
                } else if (groupId === PPINVOICE_TYPE.GROUP_PHIEU_NHAP_KHO) {
                    this.pPInvoice.noFBookRs = res.body;
                } else {
                    this.pPInvoice.otherNoFBook = res.body;
                }
                if (!this.checkData) {
                    this.copyPPInvoice();
                }
            });
    }

    // mã hàng
    getMaterialGoods() {
        this.materialGoodsService.queryForComboboxGood().subscribe(
            (res: HttpResponse<any>) => {
                this.materialGoodss = res.body;
                if (this.checkData) {
                    this.pPInvoice.ppInvoiceDetails.forEach(item => {
                        item.materialGood = this.materialGoodss.find(n => n.id.toString() === item.materialGoodsId.toString());
                        if (item.materialGood) {
                            item.materialGoodsCode = item.materialGood.materialGoodsCode;
                            // get đơn vị tính khi xem
                            this.unitService
                                .convertRateForMaterialGoodsComboboxCustom({
                                    materialGoodsId: item.materialGood.id
                                })
                                .subscribe(ref => {
                                    item.units = ref.body;
                                    item.unit = item.units.find(i => i.id === item.unitId);
                                    const mainUnitItem = item.units.find(i => i.id === item.mainUnitId);
                                    if (mainUnitItem && mainUnitItem.unitName) {
                                        item.mainUnitName = mainUnitItem.unitName;
                                    } else {
                                        item.mainConvertRate = 1;
                                    }
                                });
                        }
                    });
                }
            },
            (subRes: HttpErrorResponse) => this.onError(subRes.message)
        );
    }

    // đối tượng
    getAccountingObject() {
        this.accountingObjectService.getAccountingObjectDTOByTaskMethodAll({ taskMethod: 0 }).subscribe(
            (res: HttpResponse<IAccountingObject[]>) => {
                this.accountingObjects = res.body.filter(n => n.isActive);
                this.accountingObjects.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));

                if (this.checkData) {
                    this.pPInvoice.accountingObject = this.accountingObjects.find(n => n.id === this.pPInvoice.accountingObjectId);
                    if (this.pPInvoice.accountingObject) {
                        this.pPInvoice.accountingObjectCode = this.pPInvoice.accountingObject.accountingObjectCode;
                    } else {
                        this.pPInvoice.accountingObject = res.body.find(n => n.id === this.pPInvoice.accountingObjectId);
                        if (this.pPInvoice.accountingObject) {
                            this.pPInvoice.accountingObjectCode = this.pPInvoice.accountingObject.accountingObjectCode;
                        }
                    }
                    this.pPInvoice.ppInvoiceDetails.forEach(item => {
                        item.accountingObjectHd = this.accountingObjects.find(n => n.id === item.accountingObjectId);
                        if (!item.accountingObjectHd) {
                            item.accountingObjectHd = res.body.find(n => n.id === item.accountingObjectId);
                        }
                    });
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    // nhân viên thực hiện
    getEmployees() {
        this.accountingObjectService.getAccountingObjectEmployee().subscribe(
            (res: HttpResponse<IAccountingObject[]>) => {
                this.employees = res.body;
                if (this.checkData) {
                    this.pPInvoice.employee = this.employees.find(n => n.id === this.pPInvoice.employeeId);
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    // tiền tệ
    getCurrency() {
        this.currencyService.getCurrency().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencies = res.body;
        });
    }

    changeIsPay() {
        if (this.checkData && this.isPlayVendor) {
            return;
        }
        if (this.isPay) {
            this.pPInvoice.typeId = PPINVOICE_TYPE.TYPE_ID_TIEN_MAT;
            this.getSoChungTu(PPINVOICE_TYPE.GROUP_PHIEU_CHI);
        } else {
            this.pPInvoice.typeId = PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN;
            if (this.pPInvoice.otherNoFBook) {
                this.pPInvoice.otherNoFBook = null;
            }
        }
        if (this.active !== 1 && this.active !== 2) {
            this.active = 1;
        }
        this.changeAccount();
        this.changeReason();
    }

    private onError(errorMessage: string) {}

    selectAccountingObjects() {
        this.accountingObjectService.getAccountingObjectDTOByTaskMethod({ taskMethod: 0 }).subscribe(
            (res: HttpResponse<IAccountingObject[]>) => {
                this.accountingObjects = res.body;
                this.accountingObjects.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                this.changeAccountingObject();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    changeAccountingObject() {
        if (this.pPInvoice.accountingObject) {
            if (this.pPInvoice.ppInvoiceDetails.length > 0) {
                this.pPInvoice.ppInvoiceDetails.forEach(detail => {
                    if (detail.accountingObjectId === this.pPInvoice.accountingObjectId) {
                        detail.accountingObjectHd = this.pPInvoice.accountingObject;
                        detail.accountingObjectId = this.pPInvoice.accountingObject.id;
                    }
                });
            }
            this.pPInvoice.accountingObjectId = this.pPInvoice.accountingObject.id;
            this.pPInvoice.accountingObjectName = this.pPInvoice.accountingObject.accountingObjectName;
            this.pPInvoice.accountingObjectAddress = this.pPInvoice.accountingObject.accountingObjectAddress;
            this.pPInvoice.accountingObjectCode = this.pPInvoice.accountingObject.accountingObjectCode;
            this.pPInvoice.contactName = this.pPInvoice.accountingObject.contactName;
            this.pPInvoice.accountReceiverFullName = this.pPInvoice.accountingObject.contactName;
            this.pPInvoice.companyTaxCode = this.pPInvoice.accountingObject.taxCode;

            this.onLoadBankAccountDetailReceiversByAccountingObjectId();
            this.changeReason();
        } else {
            if (this.pPInvoice.ppInvoiceDetails.length > 0) {
                this.pPInvoice.ppInvoiceDetails.forEach(detail => {
                    if (detail.accountingObjectId === this.pPInvoice.accountingObjectId) {
                        detail.accountingObjectHd = null;
                        detail.accountingObjectId = null;
                    }
                });
            }
            this.pPInvoice.accountingObject = null;
            this.pPInvoice.accountingObjectId = null;
            this.pPInvoice.accountingObjectName = null;
            this.pPInvoice.accountingObjectAddress = null;
            this.pPInvoice.accountingObjectCode = null;
            this.pPInvoice.contactName = null;
            this.pPInvoice.accountReceiverFullName = null;
            this.pPInvoice.companyTaxCode = null;
            this.translateService.get(['ebwebApp.pPInvoice.reasonDefault.quaKho']).subscribe((res: any) => {
                this.pPInvoice.reason = res['ebwebApp.pPInvoice.reasonDefault.quaKho'];
            });

            this.bankAccountDetailReceivers = null;
            this.creditBankAccountDetailReceivers = null;
        }
        this.pPInvoice.accountReceiver = null;
        this.pPInvoice.accountReceiverName = null;
    }

    selectEmployee() {
        if (this.pPInvoice.employee) {
            this.pPInvoice.employeeId = this.pPInvoice.employee.id;
        } else {
            this.pPInvoice.employeeId = null;
        }
    }

    selectCurrency() {
        if (this.currency) {
            this.pPInvoice.currencyId = this.currency.currencyCode;
            if (!this.isReadOnly) {
                if (this.currency.currencyCode === this.account.organizationUnit.currencyID) {
                    this.pPInvoice.exchangeRate = 1;
                } else {
                    this.pPInvoice.exchangeRate = this.currency.exchangeRate;
                }
                if (this.pPInvoice.ppInvoiceDetails && this.pPInvoice.ppInvoiceDetails.length > 0) {
                    for (const detail of this.pPInvoice.ppInvoiceDetails) {
                        detail.unitPriceOriginal = 0;
                        this.getUnit(detail, false);
                    }
                }
            }
            this.typeAmountOriginal =
                this.account.organizationUnit && this.account.organizationUnit.currencyID === this.currency.currencyCode;
        } else {
            // this.currency = {};
            this.pPInvoice.currencyId = null;
            this.pPInvoice.exchangeRate = 1;
        }
        if (!this.checkData) {
            this.copyPPInvoice();
        }
        if (!this.isReadOnly) {
            this.changeExchangeRate();
        }
    }

    changeExchangeRate() {
        if (this.pPInvoice.ppInvoiceDetails && this.pPInvoice.ppInvoiceDetails.length) {
            this.pPInvoice.ppInvoiceDetails.forEach(detail => {
                this.donGiaQD(detail);
                this.thanhTienQD(detail);
                this.tienChietKhauQD(detail);
                this.giaTinhThue(detail);
                this.tienThueNk(detail);
                this.tienThueTTDB(detail);
                this.tienThueGTGTQD(detail);
                this.tienThueGTGT(detail);
                this.giaNhapKho(detail);
                this.changeAmountPPInvoice();
            });
        }
    }

    selectChangeMaterialGoods(detail: IPPInvoiceDetails, isInStock?: boolean) {
        if (detail.materialGood) {
            if (!isInStock && detail.materialGood.vatTaxRate) {
                detail.vatRate = detail.materialGood.vatTaxRate;
            }
            detail.materialGoodsId = detail.materialGood.id;
            if (!isInStock) {
                detail.description = detail.materialGood.materialGoodsName;
            }
            detail.materialGoodsCode = detail.materialGood.materialGoodsCode;
            if (!this.taxCalculationMethod) {
                detail.vatDescription = 'Thuế GTGT ' + detail.materialGood.materialGoodsCode;
            } else {
                detail.vatDescription = detail.materialGood.materialGoodsName;
            }

            if (!isInStock) {
                detail.discountRate = detail.materialGood.purchaseDiscountRate;
            }

            if (this.pPInvoice.isImportPurchase && detail.materialGood.importTaxRate) {
                detail.importTaxRate = detail.materialGood.importTaxRate;
            } else {
                detail.importTaxRate = 0;
            }

            if (this.isKho) {
                if (
                    detail.materialGood.materialGoodsType === MATERIAL_GOODS_TYPE.SERVICE ||
                    detail.materialGood.materialGoodsType === MATERIAL_GOODS_TYPE.DIFF
                ) {
                    detail.repository = null;
                    detail.repositoryId = null;
                } else {
                    if (detail.materialGood.repositoryID) {
                        detail.repository = this.repositorys.find(n => n.id === detail.materialGood.repositoryID);
                        if (detail.repository && detail.repository.id) {
                            detail.repositoryId = detail.repository.id;
                            this.getDebitAccountByRepository(detail);
                        } else {
                            detail.repository = null;
                            detail.repositoryId = null;
                        }
                    } else {
                        detail.repository = null;
                        detail.repositoryId = null;
                    }
                }
            }
            this.getUnit(detail, isInStock);
        } else {
            detail.materialGoodsId = null;
            detail.description = null;
            detail.materialGoodsCode = null;
            detail.vatDescription = null;
            detail.repository = null;
            if (this.isKho) {
                detail.repositoryId = null;
            }
            detail.units = null;
            detail.unit = null;
            detail.unitId = null;
            detail.mainUnitId = null;
            detail.mainUnitName = null;
            detail.unitPrices = [];
            detail.unitPriceOriginal = 0;
            this.donGiaQD(detail);
            this.thanhTien(detail);
            this.thanhTienQD(detail);
            this.tienChietKhau(detail);
            this.tienChietKhauQD(detail);
            this.giaTinhThue(detail);
            this.tienThueNk(detail);
            this.tienThueTTDB(detail);
            this.tienThueGTGTQD(detail);
            this.tienThueGTGT(detail);
            this.giaNhapKho(detail);
            this.changeAmountPPInvoice();
        }
    }

    getUnitPriceOriginal(detail: IPPInvoiceDetails, isInStock?: boolean) {
        detail.unitPrices = [];
        if (!isInStock) {
            detail.unitPrice = 0;
            detail.unitPriceOriginal = 0;
        }
        if (detail.unitId) {
            this.unitService
                .unitPriceOriginalForMaterialGoods({
                    materialGoodsId: detail.materialGoodsId,
                    unitId: detail.unitId,
                    currencyCode: this.pPInvoice.currencyId
                })
                .subscribe(
                    res => {
                        for (const item of res.body) {
                            detail.unitPrices.push({ unitPriceOriginal: item });
                        }
                        if (detail.unitPrices.length && detail.unitPrices[0] && !isInStock) {
                            detail.unitPriceOriginal = detail.unitPrices[0].unitPriceOriginal;
                            this.donGiaQD(detail);
                            this.thanhTien(detail);
                            this.thanhTienQD(detail);
                            this.tienChietKhau(detail);
                            this.tienChietKhauQD(detail);
                            this.giaTinhThue(detail);
                            this.tienThueNk(detail);
                            this.tienThueTTDB(detail);
                            this.tienThueGTGTQD(detail);
                            this.tienThueGTGT(detail);
                            this.giaNhapKho(detail);
                            this.changeAmountPPInvoice();
                        }
                        this.selectUnit(detail, isInStock);
                    },
                    error => {
                        if (!isInStock) {
                            this.donGiaQD(detail);
                            this.thanhTien(detail);
                            this.thanhTienQD(detail);
                            this.tienChietKhau(detail);
                            this.tienChietKhauQD(detail);
                            this.giaTinhThue(detail);
                            this.tienThueNk(detail);
                            this.tienThueTTDB(detail);
                            this.tienThueGTGTQD(detail);
                            this.tienThueGTGT(detail);
                        }
                        this.giaNhapKho(detail);
                        this.changeAmountPPInvoice();
                        detail.mainConvertRate = 1;
                        detail.formula = '*';
                    }
                );
        } else {
            if (!isInStock) {
                this.donGiaQD(detail);
                this.thanhTien(detail);
                this.thanhTienQD(detail);
                this.tienChietKhau(detail);
                this.tienChietKhauQD(detail);
                this.giaTinhThue(detail);
                this.tienThueNk(detail);
                this.tienThueTTDB(detail);
                this.tienThueGTGTQD(detail);
                this.tienThueGTGT(detail);
            }
            this.giaNhapKho(detail);
            this.changeAmountPPInvoice();
            detail.mainConvertRate = 1;
            detail.formula = '*';
        }
    }

    getDebitAccountByRepository(detail: IPPInvoiceDetails) {
        detail.debitAccountItem = this.debitAccountList.find(n => n.accountNumber === detail.repository.defaultAccount);
        if (detail.debitAccountItem) {
            detail.debitAccount = detail.debitAccountItem.accountNumber;
        } else {
            detail.debitAccountItem = this.debitAccountList.find(n => n.accountNumber === detail.materialGood.reponsitoryAccount);
            if (detail.debitAccountItem) {
                detail.debitAccount = detail.debitAccountItem.accountNumber;
            }
        }
    }

    onSelectRepository(detail: IPPInvoiceDetails) {
        if (detail.repository) {
            detail.repositoryId = detail.repository.id;
            this.getDebitAccountByRepository(detail);
        } else {
            detail.repositoryId = null;
        }
    }

    selectChangeAccountingObjectsHD(detail: IPPInvoiceDetails) {
        if (detail.accountingObjectHd) {
            detail.accountingObjectId = detail.accountingObjectHd.id;
        } else {
            detail.accountingObjectId = null;
        }
    }

    changeGiaTinhThue(detail: IPPInvoiceDetails) {
        // this.giaTinhThue(detail);
        this.tienThueNk(detail);
        this.tienThueTTDB(detail);
        this.tienThueGTGTQD(detail);
        this.tienThueGTGT(detail);
        this.giaNhapKho(detail);
        this.changeAmountPPInvoice();
    }

    selectUnit(detail: IPPInvoiceDetails, isInStock?: boolean) {
        if (detail.unit) {
            detail.unitId = detail.unit.id;
            this.changeUnit(detail);

            // this.getUnitPriceOriginal(detail);

            this.unitService
                .unitPriceOriginalForMaterialGoods({
                    materialGoodsId: detail.materialGoodsId,
                    unitId: detail.unitId,
                    currencyCode: this.pPInvoice.currencyId
                })
                .subscribe(
                    res => {
                        detail.unitPrices = [];
                        for (const item of res.body) {
                            detail.unitPrices.push({ unitPriceOriginal: item });
                        }
                        if (!isInStock) {
                            if (detail.unitPrices.length && detail.unitPrices[0]) {
                                detail.unitPriceOriginal = detail.unitPrices[0].unitPriceOriginal;
                            } else {
                                detail.unitPriceOriginal = 0;
                            }
                        }

                        this.donGiaQD(detail);
                        this.thanhTien(detail);
                        this.thanhTienQD(detail);
                        this.tienChietKhau(detail);
                        this.tienChietKhauQD(detail);
                        this.giaTinhThue(detail);
                        this.tienThueNk(detail);
                        this.tienThueTTDB(detail);
                        this.tienThueGTGTQD(detail);
                        this.tienThueGTGT(detail);
                        this.giaNhapKho(detail);
                        this.changeAmountPPInvoice();
                    },
                    error => {}
                );
        } else {
            detail.unitPrices = [];
            detail.unitPriceOriginal = 0;
            this.donGiaQD(detail);
            this.thanhTien(detail);
            this.thanhTienQD(detail);
            this.tienChietKhau(detail);
            this.tienChietKhauQD(detail);
            this.giaTinhThue(detail);
            this.tienThueNk(detail);
            this.tienThueTTDB(detail);
            this.tienThueGTGTQD(detail);
            this.tienThueGTGT(detail);
            this.giaNhapKho(detail);
            this.changeAmountPPInvoice();
        }
    }

    changeAmount(detail: IPPInvoiceDetails) {
        // if (detail.amountOriginal && detail.quantity) {
        // detail.unitPriceOriginal = detail.amountOriginal / detail.quantity;
        // detail.unitPriceOriginal = this.utilsService.round(
        //     detail.unitPriceOriginal,
        //     this.account.systemOption,
        //     this.typeAmountOriginal ? 1 : 2
        // );
        // tính lại đơn giá theo đơn vị tính chính
        // if (detail.formula) {
        //     if (detail.formula === '*') {
        //         if (detail.unitPriceOriginal && detail.mainConvertRate) {
        //             detail.mainUnitPrice = detail.unitPriceOriginal / detail.mainConvertRate;
        //         }
        //     } else if (detail.formula === '/') {
        //         if (detail.unitPriceOriginal * detail.mainConvertRate) {
        //             detail.mainUnitPrice = detail.unitPriceOriginal * detail.mainConvertRate;
        //         }
        //     }
        //     detail.mainUnitPrice = this.utilsService.round(
        //         detail.mainUnitPrice,
        //         this.account.systemOption,
        //         this.typeAmountOriginal ? 1 : 2
        //     );
        // }
        // this.donGiaQD(detail);
        // this.thanhTien(detail);
        // }
        this.thanhTienQD(detail);
        this.tienChietKhau(detail);
        this.tienChietKhauQD(detail);
        this.giaTinhThue(detail);
        this.tienThueNk(detail);
        this.tienThueTTDB(detail);
        this.tienThueGTGTQD(detail);
        this.tienThueGTGT(detail);
        this.giaNhapKho(detail);
        this.changeAmountPPInvoice();
    }

    changeAmountOriginal(detail: IPPInvoiceDetails) {
        this.changeAmountPPInvoice();
    }

    changeDiscountRate(detail: IPPInvoiceDetails) {
        this.tienChietKhau(detail);
        this.tienChietKhauQD(detail);
        this.giaTinhThue(detail);
        this.tienThueNk(detail);
        this.tienThueTTDB(detail);
        this.tienThueGTGTQD(detail);
        this.tienThueGTGT(detail);
        this.giaNhapKho(detail);
        this.changeAmountPPInvoice();
    }

    changeDiscountAmountOriginal(detail: IPPInvoiceDetails) {
        detail.discountAmountOriginal = detail.discountAmountOriginal ? detail.discountAmountOriginal : 0;
        detail.amountOriginal = detail.amountOriginal ? detail.amountOriginal : 0;
        if (detail.amountOriginal) {
            detail.discountRate = detail.discountAmountOriginal / detail.amountOriginal * 100;
            detail.discountRate = this.utilsService.round(detail.discountRate, this.account.systemOption, 5);
        } else {
            detail.discountRate = 0;
        }

        this.tienChietKhauQD(detail);
        this.giaTinhThue(detail);
        this.tienThueNk(detail);
        this.tienThueTTDB(detail);
        this.tienThueGTGTQD(detail);
        this.tienThueGTGT(detail);
        this.giaNhapKho(detail);
        this.changeAmountPPInvoice();
    }

    changeDiscountAmount(detail: any) {
        this.giaTinhThue(detail);
        this.tienThueNk(detail);
        this.tienThueTTDB(detail);
        this.tienThueGTGTQD(detail);
        this.tienThueGTGT(detail);
        this.giaNhapKho(detail);
        this.changeAmountPPInvoice();
    }

    changeQuantity(detail: IPPInvoiceDetails) {
        if (detail.materialGoodsSpecificationsLedgers) {
            const iWQuantity = detail.materialGoodsSpecificationsLedgers.reduce(function(prev, cur) {
                return prev + cur.iWQuantity;
            }, 0);
            if (detail.quantity !== iWQuantity) {
                this.toastrService.error(this.translateService.instant('ebwebApp.materialGoodsSpecifications.notEqualsQuantity'));
                return;
            }
        }
        this.thanhTien(detail);
        this.thanhTienQD(detail);
        this.tienChietKhau(detail);
        this.tienChietKhauQD(detail);
        this.changeUnit(detail);
        this.giaTinhThue(detail);
        this.tienThueNk(detail);
        this.tienThueTTDB(detail);
        this.tienThueGTGTQD(detail);
        this.tienThueGTGT(detail);
        this.giaNhapKho(detail);
        this.changeAmountPPInvoice();
    }

    changeUnitPriceOriginal(detail: IPPInvoiceDetails) {}

    changeFreightAmount(detail: IPPInvoiceDetails) {
        detail.freightAmount = detail.freightAmount ? detail.freightAmount : 0;
        if (this.currency) {
            if (this.currency.formula === '/') {
                detail.freightAmountOriginal = detail.freightAmount * (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            } else {
                detail.freightAmountOriginal = detail.freightAmount / (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            }
            detail.freightAmountOriginal = this.utilsService.round(
                detail.freightAmountOriginal,
                this.account.systemOption,
                this.typeAmountOriginal ? 7 : 8
            );
        }
        this.giaNhapKho(detail);
        this.changeAmountPPInvoice();
    }

    changeImportTaxExpenseAmount(detail: IPPInvoiceDetails) {
        if (detail.importTaxExpenseAmount) {
            if (this.currency) {
                if (this.currency.formula === '/') {
                    detail.importTaxExpenseAmountOriginal =
                        detail.importTaxExpenseAmount * (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
                } else {
                    detail.importTaxExpenseAmountOriginal =
                        detail.importTaxExpenseAmount / (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
                }
                detail.importTaxExpenseAmountOriginal = this.utilsService.round(
                    detail.importTaxExpenseAmountOriginal,
                    this.account.systemOption,
                    this.typeAmountOriginal ? 7 : 8
                );
            }
            this.giaTinhThue(detail);
            this.tienThueNk(detail);
            this.tienThueTTDB(detail);
            this.tienThueGTGTQD(detail);
            this.tienThueGTGT(detail);
            this.giaNhapKho(detail);
            this.changeAmountPPInvoice();
        }
    }

    changeInwardAmount(detail: any) {
        this.changeAmountPPInvoice();
    }

    selectDebitAccountItem(detail: IPPInvoiceDetails) {
        if (detail.debitAccountItem) {
            detail.debitAccount = detail.debitAccountItem.accountNumber;
        } else {
            detail.debitAccount = null;
        }
    }

    selectCreditAccountItem(detail: IPPInvoiceDetails) {
        if (detail.creditAccountItem) {
            detail.creditAccount = detail.creditAccountItem.accountNumber;
            if (!this.pPInvoice.isImportPurchase && !this.dataAccount.deductionDebitAccountDefault) {
                detail.deductionDebitAccountItem = this.deductionDebitAccountList.find(n => n.accountNumber === detail.creditAccount);

                if (detail.deductionDebitAccountItem) {
                    detail.deductionDebitAccount = detail.deductionDebitAccountItem.accountNumber;
                }
            }
        } else {
            detail.creditAccount = null;
        }
    }

    selectImportTaxAccountItem(detail: IPPInvoiceDetails) {
        if (detail.importTaxAccountItem) {
            detail.importTaxAccount = detail.importTaxAccountItem.accountNumber;
        } else {
            detail.importTaxAccount = null;
        }
    }

    changeSpecialConsumeTaxRate(detail: IPPInvoiceDetails) {
        this.tienThueTTDB(detail);
        this.tienThueGTGTQD(detail);
        this.tienThueGTGT(detail);
        this.giaNhapKho(detail);
        this.changeAmountPPInvoice();
    }

    changeSpecialConsumeTaxAmount(detail: IPPInvoiceDetails) {
        detail.specialConsumeTaxAmount = detail.specialConsumeTaxAmount ? detail.specialConsumeTaxAmount : 0;
        if (this.currency) {
            if (this.currency.formula === '/') {
                detail.specialConsumeTaxAmountOriginal =
                    detail.specialConsumeTaxAmount * (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            } else {
                detail.specialConsumeTaxAmountOriginal =
                    detail.specialConsumeTaxAmount / (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            }
            detail.specialConsumeTaxAmountOriginal = this.utilsService.round(
                detail.specialConsumeTaxAmountOriginal,
                this.account.systemOption,
                this.typeAmountOriginal ? 7 : 8
            );
        }
        // this.giaTinhThue(detail);
        // this.tienThueTTDB(detail);
        this.tienThueGTGTQD(detail);
        this.tienThueGTGT(detail);
        this.giaNhapKho(detail);
        this.changeAmountPPInvoice();
    }

    changeImportTaxRate(detail: IPPInvoiceDetails) {
        this.tienThueNk(detail);
        this.tienThueTTDB(detail);
        this.tienThueGTGTQD(detail);
        this.tienThueGTGT(detail);
        this.giaNhapKho(detail);
        this.changeAmountPPInvoice();
    }

    changeImportTaxAmount(detail: IPPInvoiceDetails) {
        if (this.currency) {
            detail.importTaxAmount = detail.importTaxAmount ? detail.importTaxAmount : 0;
            if (this.currency.formula === '/') {
                detail.importTaxAmountOriginal = detail.importTaxAmount * (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            } else {
                detail.importTaxAmountOriginal = detail.importTaxAmount / (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            }
            detail.importTaxAmountOriginal = this.utilsService.round(
                detail.importTaxAmountOriginal,
                this.account.systemOption,
                this.typeAmountOriginal ? 7 : 8
            );
        }
        this.tienThueTTDB(detail);
        this.tienThueGTGTQD(detail);
        this.tienThueGTGT(detail);
        this.giaNhapKho(detail);
        this.changeAmountPPInvoice();
    }

    changeNumberAttach() {
        if (this.pPInvoice.numberAttach) {
            this.pPInvoice.numberAttachRs = this.pPInvoice.numberAttach;
            this.pPInvoice.otherNumberAttach = this.pPInvoice.numberAttach;
        }
    }

    selectSpecialConsumeTaxAccountItem(detail: IPPInvoiceDetails) {
        if (detail.specialConsumeTaxAccountItem) {
            detail.specialConsumeTaxAccount = detail.specialConsumeTaxAccountItem.accountNumber;
        } else {
            detail.specialConsumeTaxAccount = null;
        }
    }

    changeVatRate(detail: IPPInvoiceDetails) {
        this.tienThueGTGTQD(detail);
        this.tienThueGTGT(detail);
        this.changeAmountPPInvoice();
    }

    changeVatAmount(detail: IPPInvoiceDetails) {
        this.tienThueGTGT(detail);
        this.changeAmountPPInvoice();
    }

    changeVatAmountOriginal(detail: IPPInvoiceDetails) {
        if (this.currency.currencyCode === this.account.organizationUnit.currencyID) {
            detail.vatAmount = detail.vatAmountOriginal;
        }
        this.changeAmountPPInvoice();
    }

    selectVatAccountItem(detail: IPPInvoiceDetails) {
        if (detail.vatAccountItem) {
            detail.vatAccount = detail.vatAccountItem.accountNumber;
        } else {
            detail.vatAccount = null;
        }
    }

    selectDeductionDebitAccountItem(detail: IPPInvoiceDetails) {
        if (detail.deductionDebitAccountItem) {
            detail.deductionDebitAccount = detail.deductionDebitAccountItem.accountNumber;
        } else {
            detail.deductionDebitAccount = null;
        }
    }

    selectGoodsServiceObjects(detail: IPPInvoiceDetails) {
        if (detail.goodsServicePurchase) {
            detail.goodsServicePurchaseId = detail.goodsServicePurchase.id;
        } else {
            detail.goodsServicePurchaseId = null;
        }
    }

    selectChangeExpenseItem(detail: IPPInvoiceDetails) {
        if (detail.expenseItem) {
            detail.expenseItemId = detail.expenseItem.id;
        } else {
            detail.expenseItemId = null;
        }
    }

    selectChangeCostSetItem(detail: IPPInvoiceDetails) {
        if (detail.costSetItem) {
            detail.costSetId = detail.costSetItem.id;
        } else {
            detail.costSetId = null;
        }
    }

    selectChangeEmContractItem(detail: IPPInvoiceDetails) {
        if (detail.emContractItem) {
            detail.contractId = detail.emContractItem.id;
        } else {
            detail.contractId = null;
        }
    }

    selectChangeBudgetItem(detail: IPPInvoiceDetails) {
        if (detail.budgetItem) {
            detail.budgetItemId = detail.budgetItem.id;
        } else {
            detail.budgetItemId = null;
        }
    }

    selectChangeOrganizationUnitItem(detail: IPPInvoiceDetails) {
        if (detail.organizationUnitItem) {
            detail.departmentId = detail.organizationUnitItem.id;
        } else {
            detail.departmentId = null;
        }
    }

    selectChangeStatisticsCodeItem(detail: IPPInvoiceDetails) {
        if (detail.statisticsCodeItem) {
            detail.statisticCodeId = detail.statisticsCodeItem.id;
        } else {
            detail.statisticCodeId = null;
        }
    }

    onChangeAccountPayment() {
        if (this.pPInvoice.accountPayment) {
            this.pPInvoice.accountPaymentId = this.pPInvoice.accountPayment.id;
            this.pPInvoice.accountPaymentName = this.pPInvoice.accountPayment.bankName;
        } else {
            this.pPInvoice.accountPaymentId = null;
            this.pPInvoice.accountPaymentName = null;
        }
    }

    onSelectBankAccountDetailReceiver() {
        if (this.pPInvoice.accountReceiver) {
            this.pPInvoice.accountReceiverId = this.pPInvoice.accountReceiver.id;
            this.pPInvoice.accountReceiverName = this.pPInvoice.accountReceiver.bankName;
        } else {
            this.pPInvoice.accountReceiverId = null;
            this.pPInvoice.accountReceiverName = null;
        }
    }

    onChangeCreditCard() {
        if (this.pPInvoice.creditCardItem) {
            this.pPInvoice.creditCardId = this.pPInvoice.creditCardItem.id;
            this.pPInvoice.creditCardType = this.pPInvoice.creditCardItem.creditCardType;
            this.pPInvoice.ownerCreditCard = this.pPInvoice.creditCardItem.ownerCard;
            this.pPInvoice.creditCardNumber = this.pPInvoice.creditCardItem.creditCardNumber;
        } else {
            this.pPInvoice.creditCardId = null;
            this.pPInvoice.creditCardType = null;
            this.pPInvoice.ownerCreditCard = null;
            this.pPInvoice.creditCardNumber = null;
        }
    }

    onSelectBankAccountDetail() {
        if (this.pPInvoice.bankAccountReceiverItem) {
            this.pPInvoice.bankAccountReceiverId = this.pPInvoice.bankAccountReceiverItem.id;
            this.pPInvoice.bankAccountReceiverName = this.pPInvoice.bankAccountReceiverItem.bankName;
        } else {
            this.pPInvoice.bankAccountReceiverId = null;
            this.pPInvoice.bankAccountReceiverName = null;
        }
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    selectUnitPriceOriginal(detail: IPPInvoiceDetails) {
        if (!this.isReadOnly) {
            this.donGiaQD(detail);
            this.thanhTien(detail);
            this.thanhTienQD(detail);
            this.tienChietKhau(detail);
            this.tienChietKhauQD(detail);
            this.changeUnit(detail);
            this.giaTinhThue(detail);
            this.tienThueNk(detail);
            this.tienThueTTDB(detail);
            this.tienThueGTGTQD(detail);
            this.tienThueGTGT(detail);
            this.giaNhapKho(detail);
            this.changeAmountPPInvoice();
        }
    }

    changeUnitPrice(detail: IPPInvoiceDetails) {
        this.changeAmountPPInvoice();
    }

    changeUnit(detail: IPPInvoiceDetails) {
        if (detail.unitId && detail.mainUnitId) {
            if (detail.unitId === detail.mainUnitId) {
                // tỉ lệ chuyển đổi
                detail.mainConvertRate = 1;
                // phép tính
                detail.formula = '*';
                // số lượng theo đơn vị chính
                if (detail.quantity) {
                    detail.mainQuantity = detail.quantity;
                }
                // đơn giá theo đơn vị tính chính
                detail.mainUnitPrice = detail.unitPriceOriginal;
                this.changeAmountPPInvoice();
            } else {
                if (detail.materialGoodsId) {
                    if (detail.unitId) {
                        this.materialGoodsConvertUnitService
                            .getMaterialGoodsConvertUnitPPInvoice({
                                materialGoodsId: detail.materialGoodsId,
                                unitId: detail.unitId
                            })
                            .subscribe(
                                (res: HttpResponse<IMaterialGoodsConvertUnit>) => {
                                    // tỉ lệ chuyển đổi
                                    detail.mainConvertRate = res.body.convertRate;
                                    // phép tính
                                    detail.formula = res.body.formula;
                                    this.changeMainConvertRate(detail);
                                },
                                (subRes: HttpErrorResponse) => {
                                    detail.formula = '*';
                                    detail.mainConvertRate = 1;
                                    this.onError(subRes.message);
                                }
                            );
                    } else {
                        detail.formula = '*';
                        detail.mainConvertRate = 1;
                    }
                }
            }
        } else {
            detail.mainQuantity = detail.quantity;
            detail.mainUnitPrice = detail.unitPriceOriginal;
            detail.mainConvertRate = 1;
            detail.formula = '*';
        }
    }

    sumAmount() {
        // nhập khẩu
        if (this.pPInvoice.isImportPurchase) {
            this.amount = this.pPInvoice.totalAmount - this.pPInvoice.totalDiscountAmount;
            this.amountOriginal = this.pPInvoice.totalAmountOriginal - this.pPInvoice.totalDiscountAmountOriginal;
        } else {
            // trong nước
            this.amount = this.pPInvoice.totalAmount - this.pPInvoice.totalDiscountAmount + this.pPInvoice.totalVATAmount;
            this.amountOriginal =
                this.pPInvoice.totalAmountOriginal - this.pPInvoice.totalDiscountAmountOriginal + this.pPInvoice.totalVATAmountOriginal;
        }
        this.amount = this.utilsService.round(this.amount, this.account.systemOption, 7);
        this.amountOriginal = this.utilsService.round(this.amountOriginal, this.account.systemOption, this.typeAmountOriginal ? 7 : 8);
    }

    setCreditAccount(detail: IPPInvoiceDetails) {
        if (this.creditAccountList && this.creditAccountList.length > 0) {
            detail.creditAccountItem = this.creditAccountList.find(n => n.accountNumber === this.dataAccount.creditAccountDefault);
            if (detail.creditAccountItem) {
                detail.creditAccount = detail.creditAccountItem.accountNumber;
            }
        }
    }

    setVatAccount(detail: IPPInvoiceDetails) {
        if (this.vatAccountList && this.vatAccountList.length > 0) {
            detail.vatAccountItem = this.vatAccountList.find(n => n.accountNumber === this.dataAccount.vatAccountDefault);
            if (detail.vatAccountItem) {
                detail.vatAccount = detail.vatAccountItem.accountNumber;
            }
        }
    }

    setDeductionDebitAccount(detail: IPPInvoiceDetails) {
        if (this.deductionDebitAccountList && this.deductionDebitAccountList.length > 0) {
            // nếu có thiết lập mặc định thì ưu tiên trước
            if (this.dataAccount.deductionDebitAccountDefault) {
                detail.deductionDebitAccountItem = this.deductionDebitAccountList.find(
                    n => n.accountNumber === this.dataAccount.deductionDebitAccountDefault
                );
            } else {
                // nếu là trong nước thì tk đối ứng lấy theo tk có còn nhập khẩu mặc định là 1331
                if (this.pPInvoice.isImportPurchase) {
                    detail.deductionDebitAccountItem = this.deductionDebitAccountList.find(n => n.accountNumber === '1331');
                } else {
                    detail.deductionDebitAccountItem = this.deductionDebitAccountList.find(
                        n => n.accountNumber === this.dataAccount.creditAccountDefault
                    );
                }
            }

            if (detail.deductionDebitAccountItem) {
                detail.deductionDebitAccount = detail.deductionDebitAccountItem.accountNumber;
            }
        }
    }

    setDebitAccount(detail: IPPInvoiceDetails) {
        if (this.debitAccountList && this.debitAccountList.length > 0) {
            detail.debitAccountItem = this.debitAccountList.find(n => n.accountNumber === this.dataAccount.debitAccountDefault);
            if (detail.debitAccountItem) {
                detail.debitAccount = detail.debitAccountItem.accountNumber;
            }
        }
    }

    setSpecialConsumeTaxAccount(detail: IPPInvoiceDetails) {
        if (this.specialConsumeTaxAccountList && this.specialConsumeTaxAccountList.length > 0) {
            detail.specialConsumeTaxAccountItem = this.specialConsumeTaxAccountList.find(
                n => n.accountNumber === this.dataAccount.specialConsumeTaxAccountDefault
            );
            if (detail.specialConsumeTaxAccountItem) {
                detail.specialConsumeTaxAccount = detail.specialConsumeTaxAccountItem.accountNumber;
            }
        }
    }

    setImportTaxAccount(detail: IPPInvoiceDetails) {
        detail.importTaxAccountItem = this.importTaxAccountList.find(n => n.accountNumber === this.dataAccount.importTaxAccountDefault);
        if (detail.importTaxAccountItem) {
            detail.importTaxAccount = detail.importTaxAccountItem.accountNumber;
        }
    }

    changeCreditAccountWhenChangeTypeId(detail: IPPInvoiceDetails) {
        this.setDebitAccount(detail);
        this.setCreditAccount(detail);
        this.setDeductionDebitAccount(detail);
        this.setVatAccount(detail);
        this.setSpecialConsumeTaxAccount(detail);
        if (this.pPInvoice.isImportPurchase) {
            this.setImportTaxAccount(detail);
        }
    }

    addNewRow(number: number, isRightClick?) {
        if (this.isReadOnly) {
            return;
        }
        if (number === 1) {
            const detail: IPPInvoiceDetails = {
                amount: 0,
                amountOriginal: 0,
                unitPrice: 0,
                unitPriceOriginal: 0,
                mainQuantity: 0
            };

            if (!this.taxCalculationMethod) {
                detail.goodsServicePurchase = this.goodsServicePurchase ? this.goodsServicePurchase : {};
                detail.goodsServicePurchaseId =
                    this.goodsServicePurchase && this.goodsServicePurchase.id ? this.goodsServicePurchase.id : null;
            }

            if (this.pPInvoice.accountingObject) {
                detail.accountingObjectHd = this.pPInvoice.accountingObject;
                detail.accountingObjectId = this.pPInvoice.accountingObject.id;
            }

            if (this.pPInvoice.ppInvoiceDetails && this.pPInvoice.ppInvoiceDetails.length > 0 && this.pPInvoice.billReceived) {
                const detailOld: IPPInvoiceDetails = this.pPInvoice.ppInvoiceDetails[this.pPInvoice.ppInvoiceDetails.length - 1];
                if (detailOld.invoiceTemplate) {
                    detail.invoiceTemplate = detailOld.invoiceTemplate;
                }

                if (detailOld.invoiceSeries) {
                    detail.invoiceSeries = detailOld.invoiceSeries;
                }

                if (detailOld.invoiceNo) {
                    detail.invoiceNo = detailOld.invoiceNo;
                }

                if (detailOld.invoiceDate) {
                    detail.invoiceDate = detailOld.invoiceDate;
                }
            }
            if (isRightClick && this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const lst = this.listIDInputDetail;
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
                const idx: number = this.pPInvoice.ppInvoiceDetails.length;
                const nameTagIndex: string = nameTag + String(idx);
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(nameTagIndex);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }

            this.changeCreditAccountWhenChangeTypeId(detail);
            this.pPInvoice.ppInvoiceDetails.push(detail);
        } else {
            this.pPInvoice.ppInvoiceDetailCost.push({});
        }
    }

    onRightClick($event, data, selectedData, isNew, isDelete, isCopy) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.isCopy = isCopy;
        this.contextMenu.data = data;
        this.contextMenu.selectedData = selectedData;
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    keyPress(value: number, select: number) {
        if (select === 1) {
            this.pPInvoice.ppInvoiceDetails.splice(value, 1);
            this.changeAmountPPInvoice();
            if (this.pPInvoice.ppInvoiceDetails.length > 0) {
                let row = 0;
                if (this.indexFocusDetailRow > this.pPInvoice.ppInvoiceDetails.length - 1) {
                    row = this.indexFocusDetailRow - 1;
                } else {
                    row = this.indexFocusDetailRow;
                }
                const lst = this.listIDInputDetail;
                const col = this.indexFocusDetailCol;

                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(lst[col] + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
        } else if (select === 2) {
            this.pPInvoice.ppInvoiceDetailCost.splice(value, 1);
        }
    }

    validate() {
        if (this.checkCloseBook(this.account, this.pPInvoice.postedDate)) {
            this.toastrService.error(
                this.translateService.instant('ebwebApp.lockBook.err.checkForSave'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.checkPostedDateGreaterDate()) {
            return false;
        }

        if (!this.pPInvoice.ppInvoiceDetails || this.pPInvoice.ppInvoiceDetails.length === 0) {
            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.detailsInvalid'));
            return false;
        }

        // đối tượng tk nợ
        const detailTypeDebitAccount = this.pPInvoice.ppInvoiceDetails.filter(
            item => item.debitAccountItem && item.debitAccountItem.detailType === ACCOUNT_DETAIL_TYPE.ACCOUNT_DEBIT
        );
        if (detailTypeDebitAccount && detailTypeDebitAccount.length) {
            switch (this.pPInvoice.typeId) {
                case PPINVOICE_TYPE.TYPE_ID_UY_NHIEM_CHI:
                case PPINVOICE_TYPE.TYPE_ID_SEC_CK:
                case PPINVOICE_TYPE.TYPE_ID_SEC_TIEN_MAT:
                    if (!this.pPInvoice.accountPaymentId) {
                        this.toastrService.error(
                            this.translateService.instant('ebwebApp.pPInvoice.error.objectInvalidAccountPayment', {
                                account: detailTypeDebitAccount[0].debitAccountItem.accountNumber
                            })
                        );
                        return false;
                    }
                    break;
                case PPINVOICE_TYPE.TYPE_ID_THE_TIN_DUNG:
                    if (!this.pPInvoice.creditCardNumber) {
                        this.toastrService.error(
                            this.translateService.instant('ebwebApp.pPInvoice.error.objectInvalidAccountPayment', {
                                account: detailTypeDebitAccount[0].debitAccountItem.accountNumber
                            })
                        );
                        return false;
                    }
                    break;
            }
        }

        // đối tượng tk có
        const detailTypeCreditAccount = this.pPInvoice.ppInvoiceDetails.filter(
            item => item.creditAccountItem && item.creditAccountItem.detailType === ACCOUNT_DETAIL_TYPE.ACCOUNT_DEBIT
        );
        if (detailTypeCreditAccount && detailTypeCreditAccount.length) {
            switch (this.pPInvoice.typeId) {
                case PPINVOICE_TYPE.TYPE_ID_UY_NHIEM_CHI:
                case PPINVOICE_TYPE.TYPE_ID_SEC_CK:
                case PPINVOICE_TYPE.TYPE_ID_SEC_TIEN_MAT:
                    if (!this.pPInvoice.accountPaymentId) {
                        this.toastrService.error(
                            this.translateService.instant('ebwebApp.pPInvoice.error.objectInvalidAccountPayment', {
                                account: detailTypeCreditAccount[0].creditAccountItem.accountNumber
                            })
                        );
                        return false;
                    }
                    break;
                case PPINVOICE_TYPE.TYPE_ID_THE_TIN_DUNG:
                    if (!this.pPInvoice.creditCardNumber) {
                        this.toastrService.error(
                            this.translateService.instant('ebwebApp.pPInvoice.error.objectInvalidAccountPayment', {
                                account: detailTypeCreditAccount[0].creditAccountItem.accountNumber
                            })
                        );
                        return false;
                    }
                    break;
            }
        }

        // mã số thuế
        if (this.pPInvoice.companyTaxCode && !this.utilsService.checkMST(this.pPInvoice.companyTaxCode)) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.mst'));
            return false;
        }

        // số chứng từ
        if (!this.pPInvoice.noFBook || (this.isKho && !this.pPInvoice.noFBookRs)) {
            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookInvalid'));
            return false;
        }

        if (this.pPInvoice.typeId === PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN) {
            if (this.isKho && this.pPInvoice.noFBook === this.pPInvoice.noFBookRs) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.dupNoBookTab'));
                return false;
            }
        } else {
            if (!this.pPInvoice.otherNoFBook) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookInvalid'));
                return false;
            }
            if (
                this.pPInvoice.noFBook === this.pPInvoice.otherNoFBook ||
                (this.isKho && this.pPInvoice.noFBookRs === this.pPInvoice.otherNoFBook)
            ) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.dupNoBookTab'));
                return false;
            }
        }

        // ngày chứng từ
        if (!this.pPInvoice.date) {
            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.dateNoBookInvalid'));
            return false;
        }

        // ngày hạch toán
        if (!this.pPInvoice.postedDate) {
            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.postedDateInvalid'));
            return false;
        }

        // loại tiền tệ
        if (!this.pPInvoice.currencyId) {
            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.currencyInvalid'));
            return false;
        }

        // tỷ giá
        if (!this.pPInvoice.exchangeRate) {
            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.exchangeRateInvalid'));
            return false;
        }

        // // tiền hàng
        // if (!this.pPInvoice.totalAmount || !this.pPInvoice.totalAmountOriginal) {
        //     this.toastrService.error(this.translateService.instant('Tiền hàng không được để trống!'));
        // }
        //
        // // tiền thuế nhập khẩu
        // if (!this.pPInvoice.totalImportTaxAmount || !this.pPInvoice.totalImportTaxAmountOriginal) {
        //     this.toastrService.error(this.translateService.instant('Tiền thuế nhập khẩu không được để trống!'));
        // }
        //
        // // tiền thuế gtgt
        // if (!this.pPInvoice.totalVATAmount || !this.pPInvoice.totalVATAmountOriginal) {
        //     this.toastrService.error(this.translateService.instant('Tiền thuế giá trị gia tăng không được để trống!'));
        // }
        //
        // // tiền ck
        // if (!this.pPInvoice.totalDiscountAmount || !this.pPInvoice.totalDiscountAmountOriginal) {
        //     this.toastrService.error(this.translateService.instant('Tiền chiết khấu không được để trống!'));
        // }
        //
        // // giá nhập kho
        // if (!this.pPInvoice.totalInwardAmount || !this.pPInvoice.totalInwardAmountOriginal) {
        //     this.toastrService.error(this.translateService.instant('Giá nhập kho không được để trống!'));
        // }
        //
        // // tiền thuế ttđb
        // if (!this.pPInvoice.totalSpecialConsumeTaxAmount || !this.pPInvoice.totalSpecialConsumeTaxAmountOriginal) {
        //     this.toastrService.error(this.translateService.instant('Tiền thuế tiêu thụ đặc biệt không được để trống!'));
        // }

        const checkAcc = this.utilsService.checkAccoutWithDetailType(
            this.debitAccountList,
            this.creditAccountList,
            this.pPInvoice.ppInvoiceDetails,
            this.accountingObjects,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
        if (checkAcc) {
            this.toastrService.error(checkAcc, this.translateService.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }

        let materialGoodsSpecificationsError = '';
        //  phần chi tiết
        for (const detail of this.pPInvoice.ppInvoiceDetails) {
            // mã hàng
            if (!detail.materialGoodsId) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.materialGoodsInvalid'));
                return false;
            }

            // kho
            if (
                this.isKho &&
                detail.materialGood &&
                !(
                    detail.materialGood.materialGoodsType === MATERIAL_GOODS_TYPE.SERVICE ||
                    detail.materialGood.materialGoodsType === MATERIAL_GOODS_TYPE.DIFF
                ) &&
                !detail.repositoryId
            ) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.repositoryInvalid'));
                return false;
            }

            // tài khoản có
            if (!detail.creditAccount) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.creditAccountInvalid'));
                return false;
            }

            // tài khoản nợ
            if (!detail.debitAccount) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.debitAccountInvalid'));
                return false;
            }

            // tỷ lệ ck
            if (detail.discountRate && detail.discountRate > 100) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.discountRateMax'));
                return false;
            } else if (detail.discountRate && detail.discountRate < 0) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.discountRateMin'));
                return false;
            }

            // tài khoản thuế nhập khẩu
            if (detail.importTaxAmount && !detail.importTaxAccount) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.importTaxAccountInvalid'));
                return false;
            }

            // tài khoản thuế tiêu thụ đặc biệt
            if (detail.specialConsumeTaxAmount && !detail.specialConsumeTaxAccount) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.specialConsumeTaxAccountInvalid'));
                return false;
            }

            // tài khoản thuế giá trị gia tăng
            if (detail.vatAmountOriginal && !detail.vatAccount) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.vatAccountInvalid'));
                return false;
            }

            // tài khoản đối ứng thuế giá trị gia tăng
            if (detail.vatAmountOriginal && !detail.deductionDebitAccount) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.deductionDebitAccountInvalid'));
                return false;
            }
            if (this.pPInvoice.billReceived) {
                if (!detail.invoiceNo) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.soHd'));
                    return false;
                } else if (!detail.invoiceDate) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.ngayHd'));
                    return false;
                }
            }
            if (
                !this.pPInvoice.id &&
                detail.materialGood.isFollow &&
                (!detail.materialGoodsSpecificationsLedgers || detail.materialGoodsSpecificationsLedgers.length === 0)
            ) {
                materialGoodsSpecificationsError = materialGoodsSpecificationsError + ', ' + detail.materialGood.materialGoodsCode;
            }
        }
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

        return true;
    }

    checkPostedDateGreaterDate() {
        if (this.pPInvoice.postedDate && this.pPInvoice.date) {
            if (moment(this.pPInvoice.postedDate) < moment(this.pPInvoice.date)) {
                this.toastrService.error(this.translateService.instant('ebwebApp.common.error.dateAndPostDate'));
                return false;
            }
        }
        return true;
    }

    public beforeChange($event: NgbTabChangeEvent) {
        switch ($event.nextId) {
            case 'ppinvoice-tab-pp-reference':
                $event.preventDefault();
                if (!this.isReadOnly) {
                    this.modalRef = this.refModalService.open(this.pPInvoice.refVouchers);
                }
                break;
            case 'ppinvoice-tab-pp-order':
                $event.preventDefault();
                if (!this.isReadOnly) {
                    this.lisTempPPOrder = this.pPInvoice.ppInvoiceDetails.map(object => ({ ...object }));
                    this.modalRef = this.refModalService.open(this.pPInvoice.refVouchers, PpOrderModalComponent, {
                        accountObjects: this.accountingObjects,
                        accountObject: this.pPInvoice.accountingObject,
                        itemsSelected: this.pPInvoice.ppInvoiceDetails.filter(x => x.ppOrderDetailId),
                        itemUnSelected: this.itemUnSelected
                    });
                }
                break;
            case 'ppinvoice-tab-liabilities':
                $event.preventDefault();
                if (!this.isReadOnly) {
                    if (this.pPInvoice.postedDate && this.pPInvoice.accountingObjectId) {
                        this.modalRef = this.refModalService.open(
                            this.pPInvoice.refVouchers,
                            ViewLiabilitiesComponent,
                            {
                                postDate: this.pPInvoice.postedDate.format('DD/MM/YYYY'),
                                objectCode: this.pPInvoice.accountingObjectCode,
                                objectId: this.pPInvoice.accountingObjectId,
                                currencyID: this.pPInvoice.currencyId
                            },
                            true,
                            null,
                            'mini-dialog'
                        );
                    } else {
                        this.toastrService.error(
                            !this.pPInvoice.accountingObjectId
                                ? this.translateService.instant('global.commonInfo.nonSelectAccountingObject')
                                : this.translateService.instant('global.commonInfo.nonSelectPostDate')
                        );
                    }
                }
                break;
            case 'ppinvoice-tab-allocation':
                $event.preventDefault();
                break;
        }
    }

    registerRef() {
        this.registerLockSuccess();
        this.registerUnlockSuccess();
        this.eventSubscriber = this.eventManager.subscribe('selectViewVoucher', response => {
            if (!this.isReadOnly) {
                this.pPInvoice.refVouchers = this.pPInvoice.refVouchers.filter(x => x.attach === true);
                response.content.forEach(item => {
                    item.attach = false;
                    this.pPInvoice.refVouchers.push(item);
                });
            }
        });

        this.eventManager.subscribe('selectedPPOrder', response => {
            this.pPInvoice.refVouchers = this.pPInvoice.refVouchers.filter(x => !x.attach);
            if (response.content) {
                console.log(response.content);
                this.pPInvoice.ppInvoiceDetails = [];
                this.pPInvoice.ppInvoiceDetails = this.lisTempPPOrder.map(object => ({ ...object }));
                let dataHaving = response.content.map(x => x.id);
                let isOwnAccountingObject = true;
                if (response.content.length === 0) {
                    isOwnAccountingObject = false;
                }

                for (let i = 0; i < response.content.length; i++) {
                    if (
                        !isOwnAccountingObject ||
                        response.content[i].accountingObjectID === null ||
                        (i > 0 && response.content[i].accountingObjectID !== response.content[0].accountingObjectID)
                    ) {
                        isOwnAccountingObject = false;
                    }
                    this.pporderdetailService.find(response.content[i].id).subscribe(res => {
                        const haveInList = this.pPInvoice.ppInvoiceDetails.some(x => x.ppOrderDetailId === response.content[i].id);
                        if (!haveInList) {
                            this.pPInvoice.ppInvoiceDetails.push(this.onSubscribePPOrder(res.body, response.content[i]));
                            const index = this.pPInvoice.ppInvoiceDetails.length;
                            this.changeMaterialGoodsByPPOrder(this.pPInvoice.ppInvoiceDetails[index - 1]);
                            dataHaving = dataHaving.filter(x => x === response.content[i].id);
                        } else {
                            for (let j = 0; j < this.pPInvoice.ppInvoiceDetails.length; j++) {
                                if (this.pPInvoice.ppInvoiceDetails[j].ppOrderDetailId === response.content[i].id) {
                                    response.content[i].priority = 0;
                                    this.pPInvoice.ppInvoiceDetails[j] = this.onSubscribePPOrder(
                                        res.body,
                                        response.content[i],
                                        this.pPInvoice.ppInvoiceDetails[j]
                                    );
                                    this.changeMaterialGoodsByPPOrder(this.pPInvoice.ppInvoiceDetails[j]);
                                    // this.selectUnit(j);
                                    dataHaving = dataHaving.filter(x => x === response.content[i].id);
                                }
                            }
                        }
                        this.pPInvoice.ppInvoiceDetails = this.pPInvoice.ppInvoiceDetails.sort((a, b) => a.newPriority - b.newPriority);
                    });
                }
                if (isOwnAccountingObject) {
                    this.pPInvoice.accountingObject = this.accountingObjects.find(n => n.id === response.content[0].accountingObjectID);
                } else {
                    this.pPInvoice.accountingObject = null;
                }

                const listRemove = [];
                for (let i = 0; i < this.pPInvoice.ppInvoiceDetails.length; i++) {
                    if (
                        this.pPInvoice.ppInvoiceDetails[i].ppOrderDetailId &&
                        !dataHaving.includes(this.pPInvoice.ppInvoiceDetails[i].ppOrderDetailId)
                    ) {
                        listRemove.push(this.pPInvoice.ppInvoiceDetails[i]);
                    }
                }
                this.pPInvoice.ppInvoiceDetails = this.pPInvoice.ppInvoiceDetails.filter(x => !listRemove.includes(x));
                this.changeAccountingObject();
            }
        });

        this.eventSubscriber = this.eventManager.subscribe('updateDiscountAllocations', response => {
            const discountAllocations: IDiscountAllocation[] = response.content;
            for (let i = 0; i < discountAllocations.length; i++) {
                for (let j = 0; j < this.pPInvoice.ppInvoiceDetails.length; j++) {
                    if (discountAllocations[i].object === this.pPInvoice.ppInvoiceDetails[j]) {
                        this.pPInvoice.ppInvoiceDetails[j].discountAmountOriginal = discountAllocations[i].discountAmountOriginal;
                        this.changeDiscountAmountOriginal(this.pPInvoice.ppInvoiceDetails[j]);
                    }
                }
            }
        });

        this.eventSubscriber = this.eventManager.subscribe('updateCostAllocations', response => {
            const costAllocations: ICostAllocation[] = response.content.costAllocations;
            const costVouchers: ICostVouchersDTO[] = response.content.costVouchers;
            const isHaiquan: boolean = response.content.isHaiQuan ? response.content.isHaiQuan : false;
            for (let i = 0; i < costAllocations.length; i++) {
                for (let j = 0; j < this.pPInvoice.ppInvoiceDetails.length; j++) {
                    if (costAllocations[i].object === this.pPInvoice.ppInvoiceDetails[j]) {
                        if (isHaiquan) {
                            this.pPInvoice.ppInvoiceDetails[j].importTaxExpenseAmount = costAllocations[i].freightAmount;
                            if (this.currency) {
                                if (this.currency.formula === '/') {
                                    this.pPInvoice.ppInvoiceDetails[j].importTaxExpenseAmountOriginal =
                                        costAllocations[i].freightAmount * (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
                                } else {
                                    this.pPInvoice.ppInvoiceDetails[j].importTaxExpenseAmountOriginal =
                                        costAllocations[i].freightAmount / (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
                                }
                                this.pPInvoice.ppInvoiceDetails[j].importTaxExpenseAmountOriginal = this.utilsService.round(
                                    this.pPInvoice.ppInvoiceDetails[j].importTaxExpenseAmountOriginal,
                                    this.account.systemOption,
                                    this.typeAmountOriginal ? 7 : 8
                                );
                            }

                            // this.pPInvoice.ppInvoiceDetails[j].importTaxExpenseAmount = this.pPInvoice.ppInvoiceDetails[
                            //     j
                            // ].importTaxExpenseAmountOriginal;
                            this.giaTinhThue(this.pPInvoice.ppInvoiceDetails[j]);
                            this.tienThueNk(this.pPInvoice.ppInvoiceDetails[j]);
                            this.tienThueTTDB(this.pPInvoice.ppInvoiceDetails[j]);
                            this.tienThueGTGTQD(this.pPInvoice.ppInvoiceDetails[j]);
                            this.tienThueGTGT(this.pPInvoice.ppInvoiceDetails[j]);
                            this.giaNhapKho(this.pPInvoice.ppInvoiceDetails[j]);
                        } else {
                            this.pPInvoice.ppInvoiceDetails[j].freightAmount = costAllocations[i].freightAmount;
                            if (this.currency) {
                                if (this.currency.formula === '/') {
                                    this.pPInvoice.ppInvoiceDetails[j].freightAmountOriginal =
                                        costAllocations[i].freightAmount * (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
                                } else {
                                    this.pPInvoice.ppInvoiceDetails[j].freightAmountOriginal =
                                        costAllocations[i].freightAmount / (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
                                }
                                this.pPInvoice.ppInvoiceDetails[j].freightAmountOriginal = this.utilsService.round(
                                    this.pPInvoice.ppInvoiceDetails[j].freightAmountOriginal,
                                    this.account.systemOption,
                                    this.typeAmountOriginal ? 7 : 8
                                );
                            }
                            this.giaNhapKho(this.pPInvoice.ppInvoiceDetails[j]);
                        }
                        this.changeAmountPPInvoice();
                    }
                }
            }
            this.changeAmountPPInvoice();
            if (this.pPInvoice.ppInvoiceDetailCost && this.pPInvoice.ppInvoiceDetailCost.length) {
                for (let i = 0; i < this.pPInvoice.ppInvoiceDetailCost.length; i++) {
                    if (
                        costVouchers.some(
                            _item =>
                                _item.id === this.pPInvoice.ppInvoiceDetailCost[i].ppServiceID &&
                                this.pPInvoice.ppInvoiceDetailCost[i].costType === isHaiquan
                        )
                    ) {
                        this.pPInvoice.ppInvoiceDetailCost.slice(i);
                    }
                }
            }

            for (const item of costVouchers) {
                if (this.pPInvoice.ppInvoiceDetailCost.some(_item => _item.ppServiceID === item.id && _item.costType === isHaiquan)) {
                    const index = this.pPInvoice.ppInvoiceDetailCost.findIndex(_item => _item.ppServiceID === item.id);
                    this.pPInvoice.ppInvoiceDetailCost[index].totalFreightAmount = item.totalAmount;
                    this.pPInvoice.ppInvoiceDetailCost[index].totalFreightAmountOriginal = item.totalAmountOriginal;
                    this.pPInvoice.ppInvoiceDetailCost[index].amount = item.freightAmount;
                    if (this.currency) {
                        if (this.currency.formula === '/') {
                            this.pPInvoice.ppInvoiceDetailCost[index].amountOriginal =
                                item.freightAmount * (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
                        } else {
                            this.pPInvoice.ppInvoiceDetailCost[index].amountOriginal =
                                item.freightAmount / (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
                        }
                        this.pPInvoice.ppInvoiceDetailCost[index].amountOriginal = this.utilsService.round(
                            this.pPInvoice.ppInvoiceDetailCost[index].amountOriginal,
                            this.account.systemOption,
                            this.typeAmountOriginal ? 7 : 8
                        );
                    }

                    this.pPInvoice.ppInvoiceDetailCost[index].accumulatedAllocateAmount =
                        this.pPInvoice.ppInvoiceDetailCost[index].amount + item.accumulatedAllocateAmount;

                    this.pPInvoice.ppInvoiceDetailCost[index].accumulatedAllocateAmount = this.utilsService.round(
                        this.pPInvoice.ppInvoiceDetailCost[index].accumulatedAllocateAmount,
                        this.account.systemOption,
                        7
                    );

                    this.pPInvoice.ppInvoiceDetailCost[index].accumulatedAllocateAmountOriginal =
                        this.pPInvoice.ppInvoiceDetailCost[index].amountOriginal +
                        (isHaiquan ? item.accumulatedAllocateAmountOriginal : item.accumulatedAllocateAmount);

                    this.pPInvoice.ppInvoiceDetailCost[index].accumulatedAllocateAmountOriginal = this.utilsService.round(
                        this.pPInvoice.ppInvoiceDetailCost[index].accumulatedAllocateAmountOriginal,
                        this.account.systemOption,
                        this.typeAmountOriginal ? 7 : 8
                    );
                } else {
                    const ppInvoiceDetailCost: IPPInvoiceDetailCost = {};
                    ppInvoiceDetailCost.costType = isHaiquan;
                    ppInvoiceDetailCost.accountingObjectId = item.accountingObjectID;
                    ppInvoiceDetailCost.accountingObjectName = item.accountingObjectName;
                    ppInvoiceDetailCost.date = item.date;
                    ppInvoiceDetailCost.postedDate = item.postedDate;
                    ppInvoiceDetailCost.noFBook = item.noFBook;
                    ppInvoiceDetailCost.noMBook = item.noMBook;
                    ppInvoiceDetailCost.typeID = PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN;
                    ppInvoiceDetailCost.ppServiceID = item.id;
                    ppInvoiceDetailCost.totalFreightAmount = item.totalAmount;
                    ppInvoiceDetailCost.totalFreightAmountOriginal = item.totalAmountOriginal;
                    ppInvoiceDetailCost.amount = item.freightAmount;
                    if (this.currency) {
                        if (this.currency.formula === '/') {
                            ppInvoiceDetailCost.amountOriginal =
                                item.freightAmount * (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
                        } else {
                            ppInvoiceDetailCost.amountOriginal =
                                item.freightAmount / (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
                        }
                        ppInvoiceDetailCost.amountOriginal = this.utilsService.round(
                            ppInvoiceDetailCost.amountOriginal,
                            this.account.systemOption,
                            this.typeAmountOriginal ? 7 : 8
                        );
                    }

                    ppInvoiceDetailCost.accumulatedAllocateAmount = ppInvoiceDetailCost.amount + item.accumulatedAllocateAmount;

                    ppInvoiceDetailCost.accumulatedAllocateAmount = this.utilsService.round(
                        ppInvoiceDetailCost.accumulatedAllocateAmount,
                        this.account.systemOption,
                        7
                    );

                    ppInvoiceDetailCost.accumulatedAllocateAmountOriginal =
                        ppInvoiceDetailCost.amountOriginal +
                        (isHaiquan ? item.accumulatedAllocateAmountOriginal : item.accumulatedAllocateAmount);

                    ppInvoiceDetailCost.accumulatedAllocateAmountOriginal = this.utilsService.round(
                        ppInvoiceDetailCost.accumulatedAllocateAmountOriginal,
                        this.account.systemOption,
                        this.typeAmountOriginal ? 7 : 8
                    );
                    this.pPInvoice.ppInvoiceDetailCost.push(ppInvoiceDetailCost);
                    console.log('ppInvoiceDetailCost: ');
                    console.log(ppInvoiceDetailCost);
                }
            }
        });

        this.eventManager.subscribe('afterAddNewRow', () => {
            if (this.pPInvoice.accountingObject) {
                this.pPInvoice.ppInvoiceDetails[
                    this.pPInvoice.ppInvoiceDetails.length - 1
                ].accountingObjectHd = this.pPInvoice.accountingObject;
                this.pPInvoice.ppInvoiceDetails[
                    this.pPInvoice.ppInvoiceDetails.length - 1
                ].accountingObjectId = this.pPInvoice.accountingObject.id;

                this.pPInvoice.ppInvoiceDetails[this.pPInvoice.ppInvoiceDetails.length - 1].amount = 0;
                this.pPInvoice.ppInvoiceDetails[this.pPInvoice.ppInvoiceDetails.length - 1].amountOriginal = 0;
                this.pPInvoice.ppInvoiceDetails[this.pPInvoice.ppInvoiceDetails.length - 1].unitPrice = 0;
                this.pPInvoice.ppInvoiceDetails[this.pPInvoice.ppInvoiceDetails.length - 1].unitPriceOriginal = 0;
                if (!this.taxCalculationMethod) {
                    this.pPInvoice.ppInvoiceDetails[this.pPInvoice.ppInvoiceDetails.length - 1].goodsServicePurchase = this
                        .goodsServicePurchase
                        ? this.goodsServicePurchase
                        : {};
                    this.pPInvoice.ppInvoiceDetails[this.pPInvoice.ppInvoiceDetails.length - 1].goodsServicePurchaseId =
                        this.goodsServicePurchase && this.goodsServicePurchase.id ? this.goodsServicePurchase.id : null;
                }
            } else {
                this.pPInvoice.ppInvoiceDetails[this.pPInvoice.ppInvoiceDetails.length - 1].amount = 0;
                this.pPInvoice.ppInvoiceDetails[this.pPInvoice.ppInvoiceDetails.length - 1].amountOriginal = 0;
                this.pPInvoice.ppInvoiceDetails[this.pPInvoice.ppInvoiceDetails.length - 1].unitPrice = 0;
                this.pPInvoice.ppInvoiceDetails[this.pPInvoice.ppInvoiceDetails.length - 1].unitPriceOriginal = 0;
                if (!this.taxCalculationMethod) {
                    this.pPInvoice.ppInvoiceDetails[this.pPInvoice.ppInvoiceDetails.length - 1].goodsServicePurchase = this
                        .goodsServicePurchase
                        ? this.goodsServicePurchase
                        : {};
                    this.pPInvoice.ppInvoiceDetails[this.pPInvoice.ppInvoiceDetails.length - 1].goodsServicePurchaseId =
                        this.goodsServicePurchase && this.goodsServicePurchase.id ? this.goodsServicePurchase.id : null;
                }
            }
            if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const lst = this.listIDInputDetail;
                const col = this.indexFocusDetailCol;
                const row = this.indexFocusDetailRow + 1;
                this.indexFocusDetailRow = row;
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(lst[col] + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
            this.changeCreditAccountWhenChangeTypeId(this.pPInvoice.ppInvoiceDetails[this.pPInvoice.ppInvoiceDetails.length - 1]);
        });
        this.eventManager.subscribe('afterDeleteRow', response => {
            if (response.content.ppOrderDetailId && response.content.quantityFromDB) {
                this.itemUnSelected.push(response.content);
            }
            if (this.pPInvoice.ppInvoiceDetails.length > 0) {
                let row = 0;
                if (this.indexFocusDetailRow > this.pPInvoice.ppInvoiceDetails.length - 1) {
                    row = this.indexFocusDetailRow - 1;
                } else {
                    row = this.indexFocusDetailRow;
                }
                const lst = this.listIDInputDetail;
                const col = this.indexFocusDetailCol;

                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(lst[col] + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
            this.changeAmountPPInvoice();
        });
    }

    private changeMaterialGoodsByPPOrder(detail: IPPInvoiceDetails) {
        detail.materialGood = this.materialGoodss.find(n => n.id === detail.materialGoodsId);
        this.selectChangeMaterialGoods(detail, true);
    }

    onSubscribePPOrder(detail: PPOrderDetail, dto: IPPOrderDto, oldPPService?: IPPInvoiceDetails): IPPInvoiceDetails {
        const ppInvoiceDetail: IPPInvoiceDetails = oldPPService ? oldPPService : {};
        ppInvoiceDetail.ppOrderNo = dto.orderNumber;
        ppInvoiceDetail.newPriority = dto.priority;
        ppInvoiceDetail.quantity = dto.receivedQuantity;
        ppInvoiceDetail.materialGoodsId = detail.materialGood ? detail.materialGood.id : null;
        ppInvoiceDetail.unitId = detail.unit ? detail.unit.id : null;
        ppInvoiceDetail.unitPrice = detail.unitPrice;
        ppInvoiceDetail.unitPriceOriginal = detail.unitPriceOriginal;
        ppInvoiceDetail.amount = detail.amount;
        ppInvoiceDetail.amountOriginal = detail.amountOriginal;
        ppInvoiceDetail.discountRate = detail.discountRate;
        ppInvoiceDetail.discountAmount = detail.discountAmount;
        ppInvoiceDetail.discountAmountOriginal = detail.discountAmountOriginal;
        ppInvoiceDetail.vatRate = detail.vatRate;
        ppInvoiceDetail.vatAmount = detail.vatAmount;
        ppInvoiceDetail.vatAmountOriginal = detail.vatAmountOriginal;
        ppInvoiceDetail.ppOrderId = dto.ppOrderId;
        ppInvoiceDetail.ppOrderDetailId = dto.id;
        ppInvoiceDetail.description = detail.description;
        if (this.pPInvoice.accountingObject) {
            ppInvoiceDetail.accountingObjectHd = this.pPInvoice.accountingObject;
            this.selectChangeAccountingObjectsHD(ppInvoiceDetail);
        }
        // todo update goodsServicePurchase
        if (this.goodsServicePurchase && !this.taxCalculationMethod) {
            ppInvoiceDetail.goodsServicePurchase = this.goodsServicePurchase;
            ppInvoiceDetail.goodsServicePurchaseId = this.goodsServicePurchase.id;
        }
        this.changeCreditAccountWhenChangeTypeId(ppInvoiceDetail);
        if (this.pPInvoice.refVouchers.filter(x => x.refID2 === ppInvoiceDetail.ppOrderId).length === 0) {
            this.pPInvoice.refVouchers.push({
                id: null,
                refID1: null,
                refID2: ppInvoiceDetail.ppOrderId,
                no: dto.orderNumber,
                date: dto.orderDate,
                reason: dto.productName,
                typeID: TypeID.DON_MUA_HANG,
                typeGroupID: GROUP_TYPEID.GROUP_PPORDER,
                attach: true
            });
        }
        return ppInvoiceDetail;
    }

    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('khong') ? ROLE.MuaHangQuaKho_GhiSo : ROLE.MuaHangKhongQuaKho_GhiSo])
    record() {
        event.preventDefault();
        if (this.pPInvoice && this.pPInvoice.id && !this.pPInvoice.recorded && !this.isLoading && this.isReadOnly) {
            this.recordPPI();
        }
    }

    recordPPI() {
        const recordData = { id: this.pPInvoice.id, typeID: PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN };
        this.isLoading = true;
        this.generalLedgerService.record(recordData).subscribe((res: HttpResponse<Irecord>) => {
            if (res.body.success) {
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBTellerPaper.recordToast.done'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                );
                this.pPInvoice.recorded = true;
            } else {
                if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                    this.toastrService.error(
                        this.translateService.instant('global.messages.error.checkTonQuyQT'),
                        this.translateService.instant('ebwebApp.mCReceipt.error.error')
                    );
                } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                    this.toastrService.error(
                        this.translateService.instant('global.messages.error.checkTonQuyTC'),
                        this.translateService.instant('ebwebApp.mCReceipt.error.error')
                    );
                } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                    this.toastrService.error(
                        this.translateService.instant('global.messages.error.checkTonQuy'),
                        this.translateService.instant('ebwebApp.mCReceipt.error.error')
                    );
                } else {
                    this.toastrService.error(
                        this.translateService.instant('ebwebApp.mBTellerPaper.recordToast.failed'),
                        this.translateService.instant('ebwebApp.mBTellerPaper.error.error')
                    );
                }
            }
            this.isLoading = false;
        });
    }

    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('khong') ? ROLE.MuaHangQuaKho_GhiSo : ROLE.MuaHangKhongQuaKho_GhiSo])
    unrecord() {
        event.preventDefault();
        if (
            this.pPInvoice &&
            this.pPInvoice.id &&
            this.pPInvoice.recorded &&
            !this.isLoading &&
            !this.checkCloseBook(this.account, this.pPInvoice.postedDate)
        ) {
            this.checkUnRecord(this.unRecordModal);
        }
    }

    unRecordPPI() {
        this.isLoading = true;
        const recordData = {
            id: this.pPInvoice.id,
            typeID: PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN
        };
        this.generalLedgerService.unrecord(recordData).subscribe((res: HttpResponse<Irecord>) => {
            if (res.body.success) {
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.done'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                );
                this.pPInvoice.recorded = false;
                if (this.unRecordCheck) {
                    this.unRecordCheck.close();
                }
            } else {
                this.toastrService.error(
                    this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.failed'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.error.error')
                );
            }
            this.isLoading = false;
        });
    }
    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('khong') ? ROLE.MuaHangQuaKho_Xoa : ROLE.MuaHangKhongQuaKho_Xoa])
    delete() {
        event.preventDefault();
        if (this.isReadOnly && this.pPInvoice.id && !this.pPInvoice.recorded && !this.isLoading) {
            this.openModelDelete(this.deleteItem);
        }
    }

    deletePPI() {
        if (this.pPInvoice && this.pPInvoice.id) {
            this.isLoading = true;
            this.pPInvoiceService.deleteById({ id: this.pPInvoice.id }).subscribe(
                (res: HttpResponse<any>) => {
                    if (res.body.message === UpdateDataMessages.DELETE_SUCCESS) {
                        this.toastrService.success(this.translateService.instant('ebwebApp.pPInvoice.success.deleteSuccess'));
                        this.checkModalRef.close();
                        this.isClosing = true;

                        // Nếu sang từ nhập kho thì quay lại nhập kho
                        if (this.isFromRSInwardOutward) {
                            this.backToRS();
                        } else {
                            this.router.navigate([this.routerLink], {
                                queryParams: {
                                    page: this.page ? this.page : 0,
                                    size: this.itemsPerPage ? this.itemsPerPage : ITEMS_PER_PAGE,
                                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                                }
                            });
                        }
                    } else if (res.body.message === UpdateDataMessages.PPINVOICE_USED) {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.ppInvoiceUsed'));
                    } else if (res.body.message === UpdateDataMessages.PPINVOICE_NOT_FOUND) {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.notFound'));
                    } else {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.deleteError'));
                    }
                    this.isLoading = false;
                },
                (res: HttpErrorResponse) => {
                    this.isLoading = false;
                }
            );
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPPInvoice>>) {
        result.subscribe((res: HttpResponse<IPPInvoice>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.toastrService.success(this.translateService.instant('ebwebApp.pPInvoice.success.addSuccess'));
        this.isReadOnly = true;
        this.isEdit = !this.isReadOnly;
        this.isLoading = false;
        // this.previousState();
    }

    private onSaveError() {
        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.addError'));
        this.isLoading = false;
    }
    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('khong') ? ROLE.MuaHangQuaKho_Sua : ROLE.MuaHangKhongQuaKho_Sua])
    edit() {
        event.preventDefault();
        if (
            !this.pPInvoice.recorded &&
            this.isReadOnly &&
            !this.isLoading &&
            !this.checkCloseBook(this.account, this.pPInvoice.postedDate)
        ) {
            this.isMove = false;
            this.isReadOnly = false;
            this.isEdit = !this.isReadOnly;
            this.isLoading = false;
        }
        if (this.rsDataSession) {
            this.checkData = true;
            this.rsDataSession.isEdit = false;
            sessionStorage.setItem('nhapKhoDataSession', JSON.stringify(this.rsDataSession));
        }
    }
    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('khong') ? ROLE.MuaHangQuaKho_Them : ROLE.MuaHangKhongQuaKho_Them])
    copyAndNew() {
        event.preventDefault();
        if (this.isReadOnly && !this.isLoading) {
            this.copyAndNewPPI();
        }
    }

    copyAndNewPPI() {
        this.isCheckPPOrderQuantity = false;
        this.pPInvoice.id = null;
        this.pPInvoice.recorded = false;
        this.pPInvoice.refVouchers = null;
        this.pPInvoice.ppInvoiceDetails.forEach(item => {
            item.id = null;
            item.accountingObjectHd = this.pPInvoice.accountingObject;
            item.accountingObjectId = this.pPInvoice.accountingObject.id;
            item.invoiceNo = null;
            item.ppOrderId = null;
            item.ppOrderDetailId = null;
            item.ppOrderNo = null;
        });
        this.pPInvoice.rsInwardOutwardId = null;
        this.pPInvoice.paymentVoucherId = null;
        this.isReadOnly = false;
        this.isEdit = !this.isReadOnly;
        this.checkData = false;
        this.onLoadReceipt();
        this.getSoChungTu(PPINVOICE_TYPE.GROUP_CHUNG_TU_MUA_HANG);
        this.getSoChungTu(PPINVOICE_TYPE.GROUP_PHIEU_NHAP_KHO);
        this.changeTypeId(true);
        this.copyPPInvoice();
        this.toastrService.success(this.translateService.instant('ebwebApp.pPInvoice.success.copy'));
    }
    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('khong') ? ROLE.MuaHangQuaKho_Them : ROLE.MuaHangKhongQuaKho_Them])
    addNew($event = null) {
        $event.preventDefault();
        // this.router.navigate(['pp-invoice/new']);
        if (this.isReadOnly && !this.isLoading) {
            this.addNewPPI();
        }
    }

    addNewPPI() {
        this.getMaterialGoods();
        this.active = 1;
        this.isPay = false;
        this.router.navigate([this.routerLink + '/new']);
        this.resetSession();
    }

    move(direction: number) {
        if (this.isFromRSInwardOutward) {
            if ((direction === -1 && this.rsRowNum === 1) || (direction === 1 && this.rsRowNum === this.rsTotalItems)) {
                return;
            }
            this.rsRowNum += direction;
            const searchData = JSON.parse(this.rsSearchData);
            return this.rsInwardOutwardService
                .findReferenceTableID({
                    fromDate: searchData.fromDate || '',
                    toDate: searchData.toDate || '',
                    noType: searchData.noType,
                    status: searchData.status,
                    accountingObject: searchData.accountingObject || '',
                    searchValue: searchData.searchValue || '',
                    rowNum: this.rsRowNum
                })
                .subscribe((res: HttpResponse<any>) => {
                    console.log(res.body);
                    const rsInwardOutward = res.body;
                    this.rsDataSession.rowNum = this.rsRowNum;
                    sessionStorage.setItem('nhapKhoDataSession', JSON.stringify(this.rsDataSession));
                    if (rsInwardOutward.typeID === this.NHAP_KHO) {
                        this.router.navigate(['/nhap-kho', rsInwardOutward.id, 'edit', this.rsRowNum]);
                    } else if (rsInwardOutward.typeID === this.NHAP_KHO_TU_DIEU_CHINH && rsInwardOutward.refID) {
                    } else if (rsInwardOutward.typeID === this.NHAP_KHO_TU_MUA_HANG && rsInwardOutward.refID) {
                        this.router.navigate(['/mua-hang/qua-kho', rsInwardOutward.refID, 'edit-rs-inward']);
                    } else if (rsInwardOutward.typeID === this.NHAP_KHO_TU_BAN_HANG_TRA_LAI && rsInwardOutward.refID) {
                        this.router.navigate(['/hang-ban/tra-lai', rsInwardOutward.refID, 'edit-rs-inward']);
                    }
                });
        } else {
            if ((direction === -1 && this.rowNum === 1) || (direction === 1 && this.rowNum === this.totalItems)) {
                return;
            }
            this.rowNum += direction;
            const searchData = JSON.parse(this.searchData);
            // goi service get by row num
            return this.pPInvoiceService
                .findByRowNum({
                    currency: searchData.currency && searchData.currency.currencyCode ? searchData.currency.currencyCode : '',
                    fromDate: searchData.fromDate || '',
                    toDate: searchData.toDate || '',
                    status: searchData.status === 0 || searchData.status === 1 ? searchData.status : '',
                    accountingObject: searchData.accountingObject && searchData.accountingObject.id ? searchData.accountingObject.id : '',
                    employee:
                        searchData.accountingObjectEmployee && searchData.accountingObjectEmployee.id
                            ? searchData.accountingObjectEmployee.id
                            : '',
                    searchValue: searchData.searchValue || '',
                    rowNum: this.rowNum,
                    isRSI: this.isKho
                })
                .subscribe(
                    (res: HttpResponse<IPPInvoice>) => {
                        this.pPInvoice = res.body;
                        this.dataSession.rowNum = this.rowNum;
                        // sessionStorage.setItem('ppInvoiceDataSession', JSON.stringify(this.dataSession));
                        switch (this.componentType) {
                            case PPINVOICE_COMPONENT_TYPE.REF_QUA_KHO:
                            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO:
                                sessionStorage.setItem('ppInvoiceRSIDataSession', JSON.stringify(this.dataSession));
                                break;
                            case PPINVOICE_COMPONENT_TYPE.REF_KHONG_QUA_KHO:
                            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO:
                                sessionStorage.setItem('ppInvoiceDataSession', JSON.stringify(this.dataSession));
                                break;
                            default:
                                sessionStorage.setItem('ppInvoiceRSIDataSession', JSON.stringify(this.dataSession));
                        }
                        if (this.pPInvoice && this.pPInvoice.id) {
                            this.loadDataPPInvoice();
                            this.getData();
                            this.getAllAccount();
                            if (this.account.organizationUnit) {
                                this.setDefaultDataFromSystemOptions();
                            }
                            this.isMove = true;
                        }
                    },
                    (res: HttpErrorResponse) => {
                        this.handleError(res);
                        this.getSessionData();
                    }
                );
        }
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    openModelDelete(content) {
        this.checkModalRef = this.modalService.open(content, { backdrop: 'static' });
    }

    save(check?: Boolean) {
        event.preventDefault();
        if ((this.isCreateUrl || this.isEditUrl) && !this.utilsService.isShowPopup) {
            if (!this.isReadOnly && !this.isLoading) {
                this.checkBeforeSave(false);
            }
        }
    }
    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('khong') ? ROLE.MuaHangQuaKho_Them : ROLE.MuaHangKhongQuaKho_Them])
    saveAndNew() {
        event.preventDefault();
        if ((this.isCreateUrl || this.isEditUrl) && !this.utilsService.isShowPopup) {
            if (!this.isReadOnly && !this.isLoading) {
                this.checkBeforeSave(true);
            }
        }
    }

    savePPI(isNew = false, content?: any) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isReadOnly = true;
        this.isEdit = !this.isReadOnly;
        if (!this.validate()) {
            this.isReadOnly = false;
            this.isEdit = !this.isReadOnly;
            return;
        }
        this.pPInvoice.importPurchase = this.pPInvoice.isImportPurchase;
        this.isLoading = true;
        this.roundObject();
        if (this.pPInvoice.ppInvoiceDetailCost && this.pPInvoice.ppInvoiceDetailCost) {
            this.utilsService.roundObjectsWithAccount(this.pPInvoice.ppInvoiceDetailCost, this.account, this.currency.currencyCode);
        }
        let isHavePPOrderId = false;
        for (let i = 0; i < this.pPInvoice.ppInvoiceDetails.length; i++) {
            this.pPInvoice.ppInvoiceDetails[i].orderPriority = i + 1;
            if (this.pPInvoice.ppInvoiceDetails[i].ppOrderDetailId) {
                isHavePPOrderId = true;
            }
        }
        if (isHavePPOrderId && !this.isCheckPPOrderQuantity) {
            this.pPInvoice.checkPPOrderQuantity = true;
            this.isCheckPPOrderQuantity = true;
        }

        this.pPInvoiceService.createPPInvoice(this.isKho ? '/rsi-save' : '/save', this.pPInvoice).subscribe(
            res => {
                if (res.body.messages === UpdateDataMessages.DUPLICATE) {
                    let duplications = '';
                    for (let i = 0; i < res.body.errors.length; i++) {
                        if (i > 0) {
                            duplications += ', ';
                        }
                        duplications += this.translateService.instant(`ebwebApp.muaHang.muaDichVu.toastr.${res.body.errors[i]}`);
                    }
                    this.toastrService.error(
                        this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.duplications', {
                            duplications
                        })
                    );
                    this.isReadOnly = false;
                } else if (
                    res.body.messages === UpdateDataMessages.SAVE_SUCCESS ||
                    res.body.messages === UpdateDataMessages.UPDATE_SUCCESS
                ) {
                    this.pPInvoice.id = res.body.uuid;
                    if (res.body.messageRecord === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.toastrService.error(
                            this.translateService.instant('global.messages.error.checkTonQuyQT'),
                            this.translateService.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.messageRecord === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.toastrService.error(
                            this.translateService.instant('global.messages.error.checkTonQuyTC'),
                            this.translateService.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.messageRecord === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.toastrService.error(
                            this.translateService.instant('global.messages.error.checkTonQuy'),
                            this.translateService.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else {
                        this.toastrService.success(this.translateService.instant('ebwebApp.pPInvoice.success.addSuccess'));
                    }
                    if (isNew) {
                        this.active = 1;
                        this.isPay = false;
                        this.addNewPPI();
                    } else {
                        this.isReadOnly = true;
                        this.refreshData();
                    }
                } else if (res.body.message === UpdateDataMessages.PPINVOICE_NOT_FOUND) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.notFound'));
                } else if (res.body.messages === UpdateDataMessages.DUPLICATE_NO_BOOK) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.duplicateNoBook'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.DUPLICATE_NO_BOOK_RS) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.duplicateNoBookRs'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.CANNOT_UPDATE_NO_BOOK_RS) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.cancelNotUpdateBookRs'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.RSI_ERROR) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.notFoundRsInward'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.DUPLICATE_OTHER_NO_BOOK) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.duplicateNoBook'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.CANNOT_UPDATE_OTHER_NO_BOOK) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.cancelNotUpdateBook'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.CANNOT_UPDATE_NO_BOOK) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.cancelNotUpdateBook'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.CURRENT_USER_IS_NOT_PRESENT) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.accountNotAuthority'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.NO_BOOK_NULL) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookInvalid'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.NO_BOOK_RSI_NULL) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookRSIInvalid'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.NO_BOOK_OTHER_NULL) {
                    switch (this.pPInvoice.typeId) {
                        case PPINVOICE_TYPE.TYPE_ID_TIEN_MAT:
                            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookCashInvalid'));
                            break;
                        case PPINVOICE_TYPE.TYPE_ID_UY_NHIEM_CHI:
                            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookUyNhiemChiInvalid'));
                            break;
                        case PPINVOICE_TYPE.TYPE_ID_SEC_CK:
                            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookSecChuyenKhoanInvalid'));
                            break;
                        case PPINVOICE_TYPE.TYPE_ID_SEC_TIEN_MAT:
                            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookSecTienMatInvalid'));
                            break;
                        case PPINVOICE_TYPE.TYPE_ID_THE_TIN_DUNG:
                            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookTheTinDungInvalid'));
                            break;
                        default:
                            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookInvalid'));
                    }
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookOtherInvalid'));
                    this.isReadOnly = false;
                } else {
                    this.isReadOnly = false;
                    if (this.pPInvoice.id) {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.updateError'));
                    } else {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.addError'));
                    }
                }
                this.isEdit = !this.isReadOnly;
                this.isLoading = false;
            },
            (res: HttpErrorResponse) => {
                if (res.error.errorKey === UpdateDataMessages.DUPLICATE_NO_BOOK) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.' + res.error.entityName));
                } else if (res.error.errorKey === UpdateDataMessages.QUANTITY_RECEIPT_GREATER_THAN && content) {
                    this.modalRef = this.modalService.open(content, { backdrop: 'static' });
                } else if (res.error.errorKey === UpdateDataMessages.NO_VOUCHER_LIMITED) {
                    // da xu ly ham chung
                } else if (res.error.errorKey === UpdateDataMessages.DETAIL_CODE_INVALID) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.detailCodeInvalid'));
                } else {
                    if (this.pPInvoice.id) {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.updateError'));
                    } else {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.addError'));
                    }
                }

                this.isReadOnly = false;
                this.isEdit = !this.isReadOnly;
                this.isLoading = false;
            }
        );
    }

    private refreshData() {
        this.pPInvoiceService
            .findById({
                id: this.pPInvoice.id
            })
            .subscribe(
                (resPP: HttpResponse<IPPInvoice>) => {
                    this.pPInvoice = {};
                    this.pPInvoice = resPP.body;
                    if (this.pPInvoice && this.pPInvoice.id) {
                        this.loadDataPPInvoice();
                        this.getData();
                        this.getAllAccount();
                        if (this.account.organizationUnit) {
                            this.setDefaultDataFromSystemOptions();
                        }
                    }
                },
                (resPP: HttpErrorResponse) => {
                    this.getSessionData();
                }
            );
    }

    // tính lại các loại tổng tiền
    changeAmountPPInvoice() {
        // tiền hàng
        this.pPInvoice.totalAmount = 0;
        this.pPInvoice.totalAmountOriginal = 0;

        // tiền chiết khấu
        this.pPInvoice.totalDiscountAmount = 0;
        this.pPInvoice.totalDiscountAmountOriginal = 0;

        // thuế giá trị gia tăng
        this.pPInvoice.totalVATAmount = 0;
        this.pPInvoice.totalVATAmountOriginal = 0;

        // tiền thuế nhập khẩu
        this.pPInvoice.totalImportTaxAmount = 0;
        this.pPInvoice.totalImportTaxAmountOriginal = 0;

        // tiền thuế TTĐB
        this.pPInvoice.totalSpecialConsumeTaxAmount = 0;
        this.pPInvoice.totalSpecialConsumeTaxAmountOriginal = 0;

        // tổng tiền thanh toán
        this.amount = 0;
        this.amountOriginal = 0;

        // chi phí mua
        this.pPInvoice.totalFreightAmount = 0;
        this.pPInvoice.totalFreightAmountOriginal = 0;

        // chi phí trước hải quan
        this.pPInvoice.totalImportTaxExpenseAmount = 0;
        this.pPInvoice.totalImportTaxExpenseAmountOriginal = 0;

        // giá nhập kho
        this.pPInvoice.totalInwardAmount = 0;
        this.pPInvoice.totalInwardAmountOriginal = 0;

        this.sumQuantity = 0;
        this.sumUnitPriceOriginal = 0;
        this.sumUnitPrice = 0;
        this.sumMainQuantity = 0;
        this.sumMainUnitPrice = 0;
        this.sumAmountOriginal = 0;
        this.sumFreightAmountOriginal = 0;

        // tổng giá tính thuế
        this.sumCustomUnitPrice = 0;

        for (const detail of this.pPInvoice.ppInvoiceDetails) {
            detail.amount = detail.amount ? detail.amount : 0;
            detail.amountOriginal = detail.amountOriginal ? detail.amountOriginal : 0;
            detail.discountAmount = detail.discountAmount ? detail.discountAmount : 0;
            detail.discountAmountOriginal = detail.discountAmountOriginal ? detail.discountAmountOriginal : 0;
            detail.freightAmountOriginal = detail.freightAmountOriginal ? detail.freightAmountOriginal : 0;
            detail.vatAmount = detail.vatAmount ? detail.vatAmount : 0;
            detail.vatAmountOriginal = detail.vatAmountOriginal ? detail.vatAmountOriginal : 0;
            detail.importTaxAmount = detail.importTaxAmount ? detail.importTaxAmount : 0;
            detail.importTaxAmountOriginal = detail.importTaxAmountOriginal ? detail.importTaxAmountOriginal : 0;
            detail.specialConsumeTaxAmount = detail.specialConsumeTaxAmount ? detail.specialConsumeTaxAmount : 0;
            detail.specialConsumeTaxAmountOriginal = detail.specialConsumeTaxAmountOriginal ? detail.specialConsumeTaxAmountOriginal : 0;
            detail.freightAmount = detail.freightAmount ? detail.freightAmount : 0;
            detail.freightAmountOriginal = detail.freightAmountOriginal ? detail.freightAmountOriginal : 0;
            detail.importTaxExpenseAmount = detail.importTaxExpenseAmount ? detail.importTaxExpenseAmount : 0;
            detail.importTaxExpenseAmountOriginal = detail.importTaxExpenseAmountOriginal ? detail.importTaxExpenseAmountOriginal : 0;
            detail.inwardAmount = detail.inwardAmount ? detail.inwardAmount : 0;
            detail.inwardAmountOriginal = detail.inwardAmountOriginal ? detail.inwardAmountOriginal : 0;

            detail.quantity = detail.quantity ? detail.quantity : 0;
            detail.unitPriceOriginal = detail.unitPriceOriginal ? detail.unitPriceOriginal : 0;
            detail.unitPrice = detail.unitPrice ? detail.unitPrice : 0;
            detail.mainQuantity = detail.mainQuantity ? detail.mainQuantity : 0;
            detail.mainUnitPrice = detail.mainUnitPrice ? detail.mainUnitPrice : 0;
            detail.freightAmountOriginal = detail.freightAmountOriginal ? detail.freightAmountOriginal : 0;
            detail.customUnitPrice = detail.customUnitPrice ? detail.customUnitPrice : 0;

            this.sumQuantity += detail.quantity;
            this.sumUnitPriceOriginal += detail.unitPriceOriginal;
            this.sumUnitPrice += detail.unitPrice;
            this.sumMainQuantity += detail.mainQuantity;
            this.sumMainUnitPrice += detail.mainUnitPrice;

            // tiền hàng
            this.pPInvoice.totalAmount += detail.amount;
            this.pPInvoice.totalAmountOriginal += detail.amountOriginal;
            // tiền chiết khấu
            this.pPInvoice.totalDiscountAmount += detail.discountAmount;
            this.pPInvoice.totalDiscountAmountOriginal += detail.discountAmountOriginal;

            // thuế giá trị gia tăng
            this.pPInvoice.totalVATAmount += detail.vatAmount;
            this.pPInvoice.totalVATAmountOriginal += detail.vatAmountOriginal;

            // tiền thuế nhập khẩu
            this.pPInvoice.totalImportTaxAmount += detail.importTaxAmount;
            this.pPInvoice.totalImportTaxAmountOriginal += detail.importTaxAmountOriginal;

            // tiền thuế tài sản đảm bảo
            this.pPInvoice.totalSpecialConsumeTaxAmount += detail.specialConsumeTaxAmount;
            this.pPInvoice.totalSpecialConsumeTaxAmountOriginal += detail.specialConsumeTaxAmountOriginal;

            // chi phí mua
            this.pPInvoice.totalFreightAmount += detail.freightAmount;
            this.pPInvoice.totalFreightAmountOriginal += detail.freightAmountOriginal;

            // chi phí trước hải quan
            this.pPInvoice.totalImportTaxExpenseAmount += detail.importTaxExpenseAmount;
            this.pPInvoice.totalImportTaxExpenseAmountOriginal += detail.importTaxExpenseAmountOriginal;

            // giá nhập kho
            this.pPInvoice.totalInwardAmount += detail.inwardAmount;
            this.pPInvoice.totalInwardAmountOriginal += detail.inwardAmountOriginal;

            // chi phí mua
            this.sumFreightAmountOriginal += detail.freightAmountOriginal;
            // giá tính thuế
            this.sumCustomUnitPrice += detail.customUnitPrice;
        }
        if (this.currency) {
            if (this.currency.formula === '/') {
                this.pPInvoice.totalInwardAmountOriginal =
                    this.pPInvoice.totalInwardAmount * (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            } else {
                this.pPInvoice.totalInwardAmountOriginal =
                    this.pPInvoice.totalInwardAmount / (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            }
        }
        this.roundObject();

        // tổng tiền thanh toán
        this.sumAmount();
    }

    // tính đơn giá quy đổi = đơn giá * tỷ giá
    donGiaQD(detail: IPPInvoiceDetails) {
        detail.unitPrice = 0;
        detail.unitPriceOriginal = detail.unitPriceOriginal ? detail.unitPriceOriginal : 0;
        if (this.currency) {
            if (this.currency.formula === '/') {
                detail.unitPrice = detail.unitPriceOriginal / (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            } else {
                detail.unitPrice = detail.unitPriceOriginal * (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            }
            detail.unitPrice = this.utilsService.round(detail.unitPrice, this.account.systemOption, 1);
        }
    }

    // thành tiền = số lượng * đơn giá
    thanhTien(detail: IPPInvoiceDetails) {
        detail.amountOriginal = 0;
        detail.unitPriceOriginal = detail.unitPriceOriginal ? detail.unitPriceOriginal : 0;
        detail.quantity = detail.quantity ? detail.quantity : 0;

        detail.amountOriginal = detail.quantity * detail.unitPriceOriginal;

        detail.amountOriginal = this.utilsService.round(detail.amountOriginal, this.account.systemOption, this.typeAmountOriginal ? 7 : 8);
    }

    roleSave() {
        if (this.isKho) {
            return this.pPInvoice.id ? ROLE.MuaHangQuaKho_Sua : ROLE.MuaHangQuaKho_Them;
        } else {
            return this.pPInvoice.id ? ROLE.MuaHangKhongQuaKho_Sua : ROLE.MuaHangKhongQuaKho_Them;
        }
    }

    // thành tiền quy đổi = thanh tiền * tỷ giá
    thanhTienQD(detail: IPPInvoiceDetails) {
        detail.amount = 0;
        detail.amountOriginal = detail.amountOriginal ? detail.amountOriginal : 0;
        if (this.currency) {
            if (this.currency.formula === '/') {
                detail.amount = detail.amountOriginal / (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            } else {
                detail.amount = detail.amountOriginal * (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            }
            detail.amount = this.utilsService.round(detail.amount, this.account.systemOption, 7);
        }
    }

    // tiền chiết khấu = thành tiền * tỉ lệ chiết khấu / 100
    tienChietKhau(detail: IPPInvoiceDetails) {
        detail.discountAmountOriginal = 0;
        detail.amountOriginal = detail.amountOriginal ? detail.amountOriginal : 0;
        detail.discountAmountOriginal = detail.amountOriginal * detail.discountRate / 100;

        detail.discountAmountOriginal = this.utilsService.round(
            detail.discountAmountOriginal,
            this.account.systemOption,
            this.typeAmountOriginal ? 7 : 8
        );
    }

    // tiền chiết khấu quy đổi = tiền chiết khấu * tỷ giá
    tienChietKhauQD(detail: IPPInvoiceDetails) {
        detail.discountAmount = 0;
        detail.discountAmountOriginal = detail.discountAmountOriginal ? detail.discountAmountOriginal : 0;
        if (this.currency) {
            if (this.currency.formula === '/') {
                detail.discountAmount = detail.discountAmountOriginal / (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            } else {
                detail.discountAmount = detail.discountAmountOriginal * (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            }
            detail.discountAmount = this.utilsService.round(detail.discountAmount, this.account.systemOption, 7);
        }
    }

    // giá nhập kho
    giaNhapKho(detail: IPPInvoiceDetails) {
        detail.inwardAmount = 0;
        detail.discountAmount = detail.discountAmount ? detail.discountAmount : 0;
        detail.freightAmount = detail.freightAmount ? detail.freightAmount : 0;
        detail.specialConsumeTaxAmount = detail.specialConsumeTaxAmount ? detail.specialConsumeTaxAmount : 0;

        detail.importTaxExpenseAmount = detail.importTaxExpenseAmount ? detail.importTaxExpenseAmount : 0;
        detail.importTaxAmount = detail.importTaxAmount ? detail.importTaxAmount : 0;
        if (!this.pPInvoice.isImportPurchase) {
            detail.inwardAmount = detail.amount - detail.discountAmount + detail.freightAmount + detail.specialConsumeTaxAmount;
        } else {
            detail.inwardAmount =
                detail.amount -
                detail.discountAmount +
                detail.freightAmount +
                detail.specialConsumeTaxAmount +
                detail.importTaxExpenseAmount +
                detail.importTaxAmount;
        }
        detail.inwardAmount = this.utilsService.round(detail.inwardAmount, this.account.systemOption, 7);
    }

    // giá tính thuế = thành tiền quy đổi - tiền chiết khấu quy đổi + chi phí trước hải quan
    giaTinhThue(detail: IPPInvoiceDetails) {
        detail.customUnitPrice = 0;
        detail.amount = detail.amount ? detail.amount : 0;
        detail.discountAmount = detail.discountAmount ? detail.discountAmount : 0;
        detail.importTaxExpenseAmount = detail.importTaxExpenseAmount ? detail.importTaxExpenseAmount : 0;

        detail.customUnitPrice = detail.amount - detail.discountAmount + detail.importTaxExpenseAmount;

        detail.customUnitPrice = this.utilsService.round(detail.customUnitPrice, this.account.systemOption, 7);
    }

    // tiền thuế nhập khẩu = giá tính thuế * thuế suất nhập khẩu
    tienThueNk(detail: IPPInvoiceDetails) {
        detail.importTaxAmount = 0;
        detail.importTaxAmountOriginal = 0;
        detail.customUnitPrice = detail.customUnitPrice ? detail.customUnitPrice : 0;
        detail.importTaxRate = detail.importTaxRate ? detail.importTaxRate : 0;

        detail.importTaxAmount = detail.customUnitPrice * detail.importTaxRate / 100;

        detail.importTaxAmount = this.utilsService.round(detail.importTaxAmount, this.account.systemOption, 7);
        if (this.currency) {
            if (this.currency.formula === '/') {
                detail.importTaxAmountOriginal = detail.importTaxAmount * (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            } else {
                detail.importTaxAmountOriginal = detail.importTaxAmount / (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            }

            detail.importTaxAmountOriginal = this.utilsService.round(
                detail.importTaxAmountOriginal,
                this.account.systemOption,
                this.typeAmountOriginal ? 7 : 8
            );
        }
    }

    // tiền thuế tiêu thụ đặc biệt = (giá tình thuế + tiền thuế nhập khẩu) * thuế suất tiêu thụ đặc biệt
    tienThueTTDB(detail: IPPInvoiceDetails) {
        detail.specialConsumeTaxAmount = 0;
        detail.specialConsumeTaxAmountOriginal = 0;
        detail.customUnitPrice = detail.customUnitPrice ? detail.customUnitPrice : 0;
        detail.importTaxAmount = detail.importTaxAmount ? detail.importTaxAmount : 0;
        detail.specialConsumeTaxRate = detail.specialConsumeTaxRate ? detail.specialConsumeTaxRate : 0;

        detail.specialConsumeTaxAmount = (detail.customUnitPrice + detail.importTaxAmount) * detail.specialConsumeTaxRate / 100;

        detail.specialConsumeTaxAmount = this.utilsService.round(detail.specialConsumeTaxAmount, this.account.systemOption, 7);
        if (this.currency) {
            if (this.currency.formula === '/') {
                detail.specialConsumeTaxAmountOriginal =
                    detail.specialConsumeTaxAmount * (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            } else {
                detail.specialConsumeTaxAmountOriginal =
                    detail.specialConsumeTaxAmount / (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            }

            detail.specialConsumeTaxAmountOriginal = this.utilsService.round(
                detail.specialConsumeTaxAmountOriginal,
                this.account.systemOption,
                this.typeAmountOriginal ? 7 : 8
            );
        }
    }

    // tiền thuế giá trị gia tăng quy đổi = (giá tính thuế + tiền thuế nhập khẩu + tiền thuế tiêu thụ đặc biệt) * % thuế giá trị gia tăng
    tienThueGTGTQD(detail: IPPInvoiceDetails) {
        detail.vatAmount = 0;
        if (detail.vatRate === 1 || detail.vatRate === 2) {
            detail.vatAmount =
                (detail.customUnitPrice + detail.importTaxAmount + detail.specialConsumeTaxAmount) * (detail.vatRate === 1 ? 5 : 10) / 100;
            detail.vatAmount = this.utilsService.round(detail.vatAmount, this.account.systemOption, 7);
        }
    }

    // tiền thuế giá trị gia tăng = tiền thuế giá trị gia tăng quy đổi / tỷ giá
    tienThueGTGT(detail: IPPInvoiceDetails) {
        detail.vatAmountOriginal = 0;
        detail.vatAmount = detail.vatAmount ? detail.vatAmount : 0;

        if (this.currency) {
            if (this.currency.formula === '/') {
                detail.vatAmountOriginal = detail.vatAmount * (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            } else {
                detail.vatAmountOriginal = detail.vatAmount / (this.pPInvoice.exchangeRate ? this.pPInvoice.exchangeRate : 1);
            }

            detail.vatAmountOriginal = this.utilsService.round(
                detail.vatAmountOriginal,
                this.account.systemOption,
                this.typeAmountOriginal ? 7 : 8
            );
        }
    }

    changeBillReceive() {
        this.pPInvoice.ppInvoiceDetails.forEach(detail => {
            detail.invoiceTemplate = null;
            detail.invoiceSeries = null;
            detail.invoiceNo = null;
            detail.invoiceDate = null;
        });
    }

    changeMainConvertRate(detail: IPPInvoiceDetails) {
        detail.mainConvertRate = detail.mainConvertRate ? detail.mainConvertRate : 1;
        if (detail.formula) {
            if (detail.formula === '*') {
                // số lượng theo đơn vị chính
                if (detail.mainConvertRate) {
                    detail.mainQuantity = detail.quantity * detail.mainConvertRate;
                }
                // đơn giá theo đơn vị tính chính
                if (detail.unitPriceOriginal && detail.mainConvertRate) {
                    detail.mainUnitPrice = detail.unitPriceOriginal / detail.mainConvertRate;
                }
            } else if (detail.formula === '/') {
                // số lượng theo đơn vị chính
                if (detail.mainConvertRate) {
                    detail.mainQuantity = detail.quantity / detail.mainConvertRate;
                }
                // đơn giá theo đơn vị tính chính
                if (detail.unitPriceOriginal * detail.mainConvertRate) {
                    detail.mainUnitPrice = detail.unitPriceOriginal * detail.mainConvertRate;
                }
            }
            detail.mainQuantity = this.utilsService.round(detail.mainQuantity, this.account.systemOption, 3);
            detail.mainUnitPrice = this.utilsService.round(detail.mainUnitPrice, this.account.systemOption, 1);
        }
        this.changeAmountPPInvoice();
    }

    changeMainQuantity(detail: IPPInvoiceDetails) {
        detail.mainQuantity = detail.mainQuantity ? detail.mainQuantity : 0;
        detail.mainConvertRate = detail.mainConvertRate ? detail.mainConvertRate : 1;
        // tính lại số lượng
        if (detail.formula) {
            if (detail.formula === '*') {
                if (detail.mainConvertRate) {
                    detail.quantity = detail.mainQuantity / detail.mainConvertRate;
                    detail.quantity = this.utilsService.round(detail.quantity, this.account.systemOption, 3);
                }
            } else if (detail.formula === '/') {
                if (detail.mainConvertRate) {
                    detail.quantity = detail.mainQuantity * detail.mainConvertRate;
                    detail.quantity = this.utilsService.round(detail.quantity, this.account.systemOption, 3);
                }
            }
        }
        this.thanhTien(detail);
        this.thanhTienQD(detail);
        this.tienChietKhau(detail);
        this.tienChietKhauQD(detail);
        this.changeUnit(detail);
        this.giaTinhThue(detail);
        this.tienThueNk(detail);
        this.tienThueTTDB(detail);
        this.tienThueGTGTQD(detail);
        this.tienThueGTGT(detail);
        this.giaNhapKho(detail);
        this.changeAmountPPInvoice();
    }

    changeMainUnitPrice(detail: IPPInvoiceDetails) {
        this.changeAmountPPInvoice();
    }

    copyPPInvoice() {
        this.ppInvoiceCopy = Object.assign({}, this.pPInvoice);
        this.ppInvoiceDetailsCopy = this.pPInvoice.ppInvoiceDetails.map(object => ({ ...object }));
    }

    formatPPInvoiceDetailsTemp() {
        const tempPPInvoiceDetails = this.pPInvoice.ppInvoiceDetails.map(object => ({ ...object }));
        for (const detail of tempPPInvoiceDetails) {
            detail.statisticsCodeItem = null;
            detail.organizationUnitItem = null;
            detail.budgetItem = null;
            detail.emContractItem = null;
            detail.costSetItem = null;
            detail.expenseItem = null;
            detail.goodsServicePurchase = null;
            detail.unit = null;
            detail.repository = null;
            detail.debitAccountItem = null;
            detail.creditAccountItem = null;
            detail.importTaxAccountItem = null;
            detail.specialConsumeTaxAccountItem = null;
            detail.vatAccountItem = null;
            detail.deductionDebitAccountItem = null;
            detail.materialGood = null;
            detail.units = null;
            detail.accountingObjectHd = null;
            detail.unitPrices = null;
            detail.ppOrderNo = null;
            detail.materialGoodsCode = null;
            detail.mainUnitName = null;
        }
        return tempPPInvoiceDetails;
    }

    formatPPInvoiceDetailsCopyTemp() {
        const tempPPInvoiceDetailsCopy = this.ppInvoiceDetailsCopy.map(object => ({ ...object }));
        for (const detail of tempPPInvoiceDetailsCopy) {
            detail.statisticsCodeItem = null;
            detail.organizationUnitItem = null;
            detail.budgetItem = null;
            detail.emContractItem = null;
            detail.costSetItem = null;
            detail.expenseItem = null;
            detail.goodsServicePurchase = null;
            detail.unit = null;
            detail.repository = null;
            detail.debitAccountItem = null;
            detail.creditAccountItem = null;
            detail.importTaxAccountItem = null;
            detail.specialConsumeTaxAccountItem = null;
            detail.vatAccountItem = null;
            detail.deductionDebitAccountItem = null;
            detail.materialGood = null;
            detail.units = null;
            detail.accountingObjectHd = null;
            detail.unitPrices = null;
            detail.ppOrderNo = null;
            detail.materialGoodsCode = null;
            detail.mainUnitName = null;
        }
        return tempPPInvoiceDetailsCopy;
    }

    formatPPInvoiceTemp() {
        // todo loại bỏ những thứ thêm vào và so sánh
        const tempPPInvoice = Object.assign({}, this.pPInvoice);
        // const tempPPInvoice = {...this.pPInvoice};
        tempPPInvoice.creditCardItem = null;
        tempPPInvoice.accountReceiver = null;
        tempPPInvoice.bankAccountReceiverItem = null;
        tempPPInvoice.accountPayment = null;
        tempPPInvoice.accountingObject = null;
        tempPPInvoice.employee = null;
        tempPPInvoice.ppInvoiceDetails = null;

        tempPPInvoice.currentBook = null;
        tempPPInvoice.isImportPurchase = null;
        tempPPInvoice.accountingObjectCode = null;

        tempPPInvoice.totalAmount = null;
        tempPPInvoice.totalAmountOriginal = null;
        tempPPInvoice.totalDiscountAmount = null;
        tempPPInvoice.totalDiscountAmountOriginal = null;
        tempPPInvoice.totalVATAmount = null;
        tempPPInvoice.totalVATAmountOriginal = null;
        tempPPInvoice.totalImportTaxAmount = null;
        tempPPInvoice.totalImportTaxAmountOriginal = null;
        tempPPInvoice.totalSpecialConsumeTaxAmount = null;
        tempPPInvoice.totalSpecialConsumeTaxAmountOriginal = null;
        tempPPInvoice.totalFreightAmount = null;
        tempPPInvoice.totalFreightAmountOriginal = null;
        tempPPInvoice.totalImportTaxExpenseAmount = null;
        tempPPInvoice.totalImportTaxExpenseAmountOriginal = null;
        tempPPInvoice.totalInwardAmount = null;
        tempPPInvoice.totalInwardAmountOriginal = null;
        tempPPInvoice.creditCardItem = null;
        tempPPInvoice.bankAccountReceiverItem = null;

        return tempPPInvoice;
    }

    formatPPInvoiceCopyTemp() {
        // todo loại bỏ những thứ thêm vào và so sánh
        const tempPPInvoiceCopy = Object.assign({}, this.ppInvoiceCopy);
        tempPPInvoiceCopy.currentBook = null;
        tempPPInvoiceCopy.isImportPurchase = null;
        tempPPInvoiceCopy.accountingObject = null;
        tempPPInvoiceCopy.accountingObjectCode = null;
        tempPPInvoiceCopy.employee = null;
        tempPPInvoiceCopy.accountPayment = null;
        tempPPInvoiceCopy.creditCardItem = null;
        tempPPInvoiceCopy.accountReceiver = null;
        tempPPInvoiceCopy.bankAccountReceiverItem = null;
        tempPPInvoiceCopy.totalAmount = null;
        tempPPInvoiceCopy.totalAmountOriginal = null;
        tempPPInvoiceCopy.totalDiscountAmount = null;
        tempPPInvoiceCopy.totalDiscountAmountOriginal = null;
        tempPPInvoiceCopy.totalVATAmount = null;
        tempPPInvoiceCopy.totalVATAmountOriginal = null;
        tempPPInvoiceCopy.totalImportTaxAmount = null;
        tempPPInvoiceCopy.totalImportTaxAmountOriginal = null;
        tempPPInvoiceCopy.totalSpecialConsumeTaxAmount = null;
        tempPPInvoiceCopy.totalSpecialConsumeTaxAmountOriginal = null;
        tempPPInvoiceCopy.totalFreightAmount = null;
        tempPPInvoiceCopy.totalFreightAmountOriginal = null;
        tempPPInvoiceCopy.totalImportTaxExpenseAmount = null;
        tempPPInvoiceCopy.totalImportTaxExpenseAmountOriginal = null;
        tempPPInvoiceCopy.totalInwardAmount = null;
        tempPPInvoiceCopy.totalInwardAmountOriginal = null;
        tempPPInvoiceCopy.ppInvoiceDetails = null;
        return tempPPInvoiceCopy;
    }

    checkChangePPInvoice() {
        if (this.pPInvoice.ppInvoiceDetails && this.pPInvoice.ppInvoiceDetails.some(item => !item.id)) {
            return true;
        }

        const tempPPInvoice = this.formatPPInvoiceTemp();
        const tempPPInvoiceCopy = this.formatPPInvoiceCopyTemp();

        if (this.datepipe.transform(tempPPInvoice.dueDate, 'yyyyMMdd') !== this.datepipe.transform(tempPPInvoiceCopy.dueDate, 'yyyyMMdd')) {
            return true;
        }

        if (this.datepipe.transform(tempPPInvoice.date, 'yyyyMMdd') !== this.datepipe.transform(tempPPInvoiceCopy.date, 'yyyyMMdd')) {
            return true;
        }

        if (
            this.datepipe.transform(tempPPInvoice.postedDate, 'yyyyMMdd') !==
            this.datepipe.transform(tempPPInvoiceCopy.postedDate, 'yyyyMMdd')
        ) {
            return true;
        }

        const tempPPInvoiceDetails = this.formatPPInvoiceDetailsTemp() ? this.formatPPInvoiceDetailsTemp() : [];
        const tempPPInvoiceDetailsCopy = this.formatPPInvoiceDetailsCopyTemp() ? this.formatPPInvoiceDetailsCopyTemp() : [];

        if (tempPPInvoiceDetails.length !== tempPPInvoiceDetailsCopy.length) {
            return true;
        }
        if (tempPPInvoiceDetails && tempPPInvoiceDetails.length) {
            for (let i = 0; i < tempPPInvoiceDetails.length; i++) {
                if (
                    this.datepipe.transform(tempPPInvoiceDetails[i].expiryDate, 'yyyyMMdd') !==
                    this.datepipe.transform(tempPPInvoiceDetailsCopy[i].expiryDate, 'yyyyMMdd')
                ) {
                    return true;
                }
                if (
                    this.datepipe.transform(tempPPInvoiceDetails[i].invoiceDate, 'yyyyMMdd') !==
                    this.datepipe.transform(tempPPInvoiceDetailsCopy[i].invoiceDate, 'yyyyMMdd')
                ) {
                    return true;
                }
            }
        }
        // bỏ date ra khỏi phần kiểm tra bên dưới vì đã kiểm tra bên trên r
        tempPPInvoice.dueDate = null;
        tempPPInvoice.date = null;
        tempPPInvoice.postedDate = null;

        tempPPInvoiceCopy.dueDate = null;
        tempPPInvoiceCopy.date = null;
        tempPPInvoiceCopy.postedDate = null;

        if (tempPPInvoiceDetails && tempPPInvoiceDetails.length) {
            for (let i = 0; i < tempPPInvoiceDetails.length; i++) {
                tempPPInvoiceDetails[i].expiryDate = null;
                tempPPInvoiceDetailsCopy[i].expiryDate = null;

                tempPPInvoiceDetails[i].invoiceDate = null;
                tempPPInvoiceDetailsCopy[i].invoiceDate = null;
            }
        }

        return (
            !this.utilsService.isEquivalent(tempPPInvoice, tempPPInvoiceCopy) ||
            !this.utilsService.isEquivalentArray(tempPPInvoiceDetails, tempPPInvoiceDetailsCopy)
        );
    }

    closeForm() {
        event.preventDefault();
        if (!this.isLoading) {
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.previousState(this.content);
        }
    }

    previousState(content) {
        if (!this.utilsService.isShowPopup) {
            this.isClosing = true;

            if (!this.ppInvoiceCopy || this.isMove || this.isReadOnly) {
                this.closeAll();
                return;
            }
            if (this.checkChangePPInvoice()) {
                this.modalRef = this.modalService.open(content, { backdrop: 'static' });
                return;
            }
            this.closeAll();
        }
    }

    canDeactive() {
        if (this.isClosing || !this.ppInvoiceCopy || this.isClosed || this.isReadOnly) {
            return true;
        }

        return !this.checkChangePPInvoice();
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.isClosing) {
            this.closeAll();
        }
        this.isClosing = false;
    }

    closePopup() {
        if (this.modalRef) {
            this.modalRef.close();
            if (this.ppInvoiceCopy) {
                this.ppInvoiceCopy.date = this.ppInvoiceCopy.date != null ? moment(this.ppInvoiceCopy.date) : null;
                this.ppInvoiceCopy.postedDate = this.ppInvoiceCopy.postedDate != null ? moment(this.ppInvoiceCopy.postedDate) : null;
            }
        }
    }

    closeAll() {
        if (
            this.componentType === PPINVOICE_COMPONENT_TYPE.REF_QUA_KHO ||
            this.componentType === PPINVOICE_COMPONENT_TYPE.REF_KHONG_QUA_KHO
        ) {
            this.router.navigate([this.routerLink]);
            return;
        }
        this.isClosed = true;
        // this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceSearchData'));
        switch (this.componentType) {
            case PPINVOICE_COMPONENT_TYPE.REF_QUA_KHO:
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO:
                this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceRSISearchData'));
                break;
            case PPINVOICE_COMPONENT_TYPE.REF_KHONG_QUA_KHO:
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO:
                this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceSearchData'));
                break;
            default:
                this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceRSISearchData'));
        }
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
        // sessionStorage.removeItem('ppInvoiceDataSession');
        switch (this.componentType) {
            case PPINVOICE_COMPONENT_TYPE.REF_QUA_KHO:
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO:
                sessionStorage.removeItem('ppInvoiceRSIDataSession');
                break;
            case PPINVOICE_COMPONENT_TYPE.REF_KHONG_QUA_KHO:
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO:
                sessionStorage.removeItem('ppInvoiceDataSession');
                break;
            default:
                sessionStorage.removeItem('ppInvoiceRSIDataSession');
        }

        /**
         * Trường hợp sang từ nhập kho
         */
        if (this.isFromRSInwardOutward) {
            this.backToRS();
        } else if (this.fromMCPayment) {
            // Add by Hautv
            this.closeAllFromMCPayment();
        } else if (this.fromMBTellerPaper) {
            // Add by anmt
            this.closeAllFromMBTellerPaper();
        } else if (this.fromMBCreditCard) {
            this.closeAllFromMBCreditCard();
        } else {
            // nếu đi từ màn khác ppinvoice thì quay về lại màn đó
            const linkreceiveBill = window.location.href.includes('/edit/receiveBill');
            if (linkreceiveBill) {
                window.history.back();
            } else {
                this.router.navigate([this.routerLink], {
                    queryParams: {
                        page: this.page ? this.page : 0,
                        size: this.itemsPerPage ? this.itemsPerPage : ITEMS_PER_PAGE,
                        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                    }
                });
            }
        }
    }

    backToRS() {
        sessionStorage.removeItem('nhapKhoDataSession');
        this.router.navigate(['/nhap-kho'], {
            queryParams: {
                page: this.rsDataSession.page ? this.rsDataSession.page : 0,
                size: this.rsDataSession.itemsPerPage ? this.rsDataSession.itemsPerPage : ITEMS_PER_PAGE,
                sort: this.rsDataSession.predicate + ',' + (this.rsDataSession.reverse ? 'asc' : 'desc')
            }
        });
    }

    private handleError(err) {
        this.isLoading = false;
        this.isClosing = false;
        this.close();
        this.toastrService.error(this.translateService.instant(`ebwebApp.pporder.${err.error.message}`));
    }

    checkUnRecord(content) {
        this.isLoading = true;
        this.pPInvoiceService.checkUnRecord({ id: this.pPInvoice.id }).subscribe(
            (res: HttpResponse<any>) => {
                this.isLoading = false;
                if (res.body.message === UpdateDataMessages.NOTHING) {
                    if (this.isPlayVendor) {
                        this.unRecordCheck = this.modalService.open(content, { size: 'lg', backdrop: 'static' });
                    } else {
                        this.unRecordPPI();
                    }
                } else if (res.body.message === UpdateDataMessages.PPINVOICE_USED) {
                    this.isUsed = true;
                    this.unRecordCheck = this.modalService.open(content, { size: 'lg', backdrop: 'static' });
                } else {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.default'));
                }
            },
            (res: HttpErrorResponse) => {
                this.isLoading = false;
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.default'));
            }
        );
    }

    /*Phiếu chi*/
    previousEditMCPayment() {
        // goi service get by row num
        if (this.rowNumberMCPayment !== this.countMCPayment) {
            this.utilsService
                .findByRowNum({
                    id: this.mCPaymentID,
                    isNext: false,
                    typeID: this.TYPE_MC_PAYMENT,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMCPayment>) => {
                        this.navigate(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    nextEditMCPayment() {
        // goi service get by row num
        if (this.rowNumberMCPayment !== 1) {
            this.utilsService
                .findByRowNum({
                    id: this.mCPaymentID,
                    isNext: true,
                    typeID: this.TYPE_MC_PAYMENT,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMCPayment>) => {
                        this.navigate(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    navigate(imcPayment: IMCPayment) {
        switch (imcPayment.typeID) {
            case this.TYPE_MC_PAYMENT:
                this.router.navigate(['/mc-payment', imcPayment.id, 'edit']);
                break;
            case this.TYPE_PPSERVICE:
                this.mCPaymentService.find(imcPayment.id).subscribe((res: HttpResponse<IMCPayment>) => {
                    const ppServiceID = res.body.ppServiceID;
                    if (ppServiceID) {
                        this.router.navigate(['./mua-dich-vu']).then(() => {
                            this.router.navigate(['./mua-dich-vu', ppServiceID, 'edit', 'from-mc-payment', imcPayment.id]);
                        });
                    }
                });
                break;
            case this.TYPE_PPINVOICE_MHQK:
                this.mCPaymentService.find(imcPayment.id).subscribe((res: HttpResponse<IMCPayment>) => {
                    const ppInvocieID = res.body.ppInvocieID;
                    if (ppInvocieID) {
                        if (res.body.storedInRepository) {
                            this.router.navigate(['./mua-hang', 'qua-kho', 'new']).then(() => {
                                this.router.navigate(['./mua-hang', 'qua-kho', ppInvocieID, 'edit', 'from-mc-payment', imcPayment.id]);
                            });
                        } else {
                            this.router.navigate(['./mua-hang', 'khong-qua-kho', 'new']).then(() => {
                                this.router.navigate([
                                    './mua-hang',
                                    'khong-qua-kho',
                                    ppInvocieID,
                                    'edit',
                                    'from-mc-payment',
                                    imcPayment.id
                                ]);
                            });
                        }
                    }
                });
                break;
        }
    }

    /*Thẻ tín dụng*/
    previousEditMBCreditCard() {
        // goi service get by row num
        if (this.rowNumberMBCreditCard !== this.countMBCreditCard) {
            this.utilsService
                .findByRowNum({
                    id: this.mBCreditCardID,
                    isNext: false,
                    typeID: this.TYPE_MB_CREDIT_CARD,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMBCreditCard>) => {
                        this.navigateMBCreditCard(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    nextEditMBCreditCard() {
        // goi service get by row num
        if (this.rowNumberMBCreditCard !== 1) {
            this.utilsService
                .findByRowNum({
                    id: this.mBCreditCardID,
                    isNext: true,
                    typeID: this.TYPE_MB_CREDIT_CARD,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMBCreditCard>) => {
                        this.navigateMBCreditCard(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    navigateMBCreditCard(imbCreditCard: IMBCreditCard) {
        switch (imbCreditCard.typeID) {
            case this.TYPE_MB_CREDIT_CARD:
                this.router.navigate(['/mb-credit-card', imbCreditCard.id, 'edit']);
                break;
            case this.TYPE_MB_CREDIT_CARD_MUA_DV:
                this.mBCreditCardService.find(imbCreditCard.id).subscribe((res: HttpResponse<IMBCreditCard>) => {
                    const ppServiceID = res.body.ppServiceID;
                    if (ppServiceID) {
                        this.router.navigate(['./mua-dich-vu']).then(() => {
                            this.router.navigate(['./mua-dich-vu', ppServiceID, 'edit', 'from-mb-credit-card', imbCreditCard.id]);
                        });
                    }
                });
                break;
            case this.TYPE_MB_CREDIT_CARD_MUA_HANG:
                this.mBCreditCardService.find(imbCreditCard.id).subscribe((res: HttpResponse<any>) => {
                    const ppInvocieID = res.body.ppInvocieID;
                    if (ppInvocieID) {
                        if (res.body.storedInRepository) {
                            this.router.navigate(['./mua-hang', 'qua-kho', 'new']).then(() => {
                                this.router.navigate([
                                    './mua-hang',
                                    'qua-kho',
                                    ppInvocieID,
                                    'edit',
                                    'from-mb-credit-card',
                                    imbCreditCard.id
                                ]);
                            });
                        } else {
                            this.router.navigate(['./mua-hang', 'khong-qua-kho', 'new']).then(() => {
                                this.router.navigate([
                                    './mua-hang',
                                    'khong-qua-kho',
                                    ppInvocieID,
                                    'edit',
                                    'from-mb-credit-card',
                                    imbCreditCard.id
                                ]);
                            });
                        }
                    }
                });
                break;
        }
    }

    closeAllFromMCPayment() {
        if (this.searchVoucher) {
            if (sessionStorage.getItem('page_MCPayment')) {
                this.router.navigate(['/mc-payment', 'hasSearch', '1'], {
                    queryParams: {
                        page: sessionStorage.getItem('page_MCPayment'),
                        size: sessionStorage.getItem('size_MCPayment'),
                        index: sessionStorage.getItem('index_MCPayment')
                    }
                });
            } else {
                this.router.navigate(['/mc-payment', 'hasSearch', '1']);
            }
        } else {
            if (sessionStorage.getItem('page_MCPayment')) {
                this.router.navigate(['/mc-payment'], {
                    queryParams: {
                        page: sessionStorage.getItem('page_MCPayment'),
                        size: sessionStorage.getItem('size_MCPayment'),
                        index: sessionStorage.getItem('index_MCPayment')
                    }
                });
            } else {
                this.router.navigate(['/mc-payment']);
            }
        }
    }

    /*Bao No*/
    previousEditMBTellerPaper() {
        if (this.rowNumberMBTellerPaper === this.countMBTellerPaper) {
            return;
        }
        this.rowNumberMBTellerPaper++;
        this.mBTellerPaperService
            .findByRowNum({
                searchVoucher: this.searchVoucher ? JSON.stringify(this.searchVoucher) : null,
                rowNum: this.rowNumberMBTellerPaper
            })
            .subscribe(
                (res: HttpResponse<IMBTellerPaper>) => {
                    this.navigateMBTP(res.body);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    nextEditMBTellerPaper() {
        if (this.rowNumberMBTellerPaper === 1) {
            return;
        }
        this.rowNum--;
        this.mBTellerPaperService
            .findByRowNum({
                searchVoucher: this.searchVoucher ? JSON.stringify(this.searchVoucher) : null,
                rowNum: this.rowNumberMBTellerPaper
            })
            .subscribe(
                (res: HttpResponse<IMBTellerPaper>) => {
                    this.navigateMBTP(res.body);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    navigateMBTP(imcPayment: IMBTellerPaper) {
        switch (imcPayment.typeId) {
            case this.TYPE_BAONO_UNC:
            case this.TYPE_BAONO_SCK:
            case this.TYPE_BAONO_STM:
                this.router.navigate(['/mb-teller-paper', imcPayment.id, 'edit']);
                break;
            case this.TYPE_UNC_PPSERVICE:
            case this.TYPE_SCK_PPSERVICE:
            case this.TYPE_STM_PPSERVICE:
                this.mBTellerPaperService.find(imcPayment.id).subscribe((res: HttpResponse<IMBTellerPaper>) => {
                    const ppServiceID = res.body.ppServiceID;
                    if (ppServiceID) {
                        this.router.navigate(['./mua-dich-vu']).then(() => {
                            this.router.navigate(['./mua-dich-vu', ppServiceID, 'edit', 'from-mb-teller-paper', imcPayment.id]);
                        });
                    }
                });
                break;
            case this.TYPE_UNC_PPINVOICE_MHQK:
            case this.TYPE_SCK_PPINVOICE_MHQK:
            case this.TYPE_STM_PPINVOICE_MHQK:
                this.mBTellerPaperService.find(imcPayment.id).subscribe((res: HttpResponse<IMBTellerPaper>) => {
                    const ppInvocieID = res.body.ppInvocieID;
                    if (ppInvocieID) {
                        if (res.body.storedInRepository) {
                            this.router.navigate(['./mua-hang', 'qua-kho', 'new']).then(() => {
                                this.router.navigate(['./mua-hang', 'qua-kho', ppInvocieID, 'edit', 'from-mb-teller-paper', imcPayment.id]);
                            });
                        } else {
                            this.router.navigate(['./mua-hang', 'khong-qua-kho', 'new']).then(() => {
                                this.router.navigate([
                                    './mua-hang',
                                    'khong-qua-kho',
                                    ppInvocieID,
                                    'edit',
                                    'from-mb-teller-paper',
                                    imcPayment.id
                                ]);
                            });
                        }
                    }
                });
                break;
        }
    }

    closeAllFromMBTellerPaper() {
        this.router.navigate(['/mb-teller-paper']);
    }

    closeAllFromMBCreditCard() {
        this.router.navigate(['/mb-credit-card']);
    }

    /*End Bao No*/

    roundObject() {
        if (this.currency) {
            this.utilsService.roundObjectsWithAccount(this.pPInvoice.ppInvoiceDetails, this.account, this.currency.currencyCode);
            this.utilsService.roundObjectWithAccount(this.pPInvoice, this.account, this.currency.currencyCode);
        }
    }

    ngOnDestroy(): void {
        this.eventManager.destroy(this.eventSubscriber);
    }

    saveByQuantityLargerModal() {
        this.pPInvoice.checkPPOrderQuantity = false;
        this.savePPI(null, null);
    }

    closeModal() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isClosing = false;
    }

    discountAllocation() {
        if (!this.isReadOnly) {
            const discountAllocations: IDiscountAllocation[] = [];
            for (let i = 0; i < this.pPInvoice.ppInvoiceDetails.length; i++) {
                const discountAllocation: IDiscountAllocation = new DiscountAllocation();
                discountAllocation.productCode = this.pPInvoice.ppInvoiceDetails[i].materialGoodsCode;
                discountAllocation.reason = this.pPInvoice.ppInvoiceDetails[i].description;
                discountAllocation.quantity = this.pPInvoice.ppInvoiceDetails[i].quantity;
                discountAllocation.amountOriginal = this.pPInvoice.ppInvoiceDetails[i].amountOriginal;
                discountAllocation.object = this.pPInvoice.ppInvoiceDetails[i];
                discountAllocations.push(discountAllocation);
            }
            this.modalRef = this.refModalService.open(this.pPInvoice.refVouchers, DiscountAllocationModalComponent, {
                discountAllocations,
                currencyCode: this.pPInvoice.currencyId
            });
        }
    }

    costAllocation(isHq: boolean) {
        if (!this.isReadOnly) {
            const costAllocations: ICostAllocation[] = [];
            for (let i = 0; i < this.pPInvoice.ppInvoiceDetails.length; i++) {
                const costAllocation: ICostAllocation = new CostAllocation();
                costAllocation.productCode = this.pPInvoice.ppInvoiceDetails[i].materialGoodsCode;
                costAllocation.reason = this.pPInvoice.ppInvoiceDetails[i].description;
                costAllocation.quantity = this.pPInvoice.ppInvoiceDetails[i].quantity;
                costAllocation.amount = this.pPInvoice.ppInvoiceDetails[i].amount;
                costAllocation.object = this.pPInvoice.ppInvoiceDetails[i];
                costAllocations.push(costAllocation);
            }
            this.modalRef = this.refModalService.open(this.pPInvoice.refVouchers, CostAllocationModalComponent, {
                costAllocations,
                ppInvoiceId: this.pPInvoice.id,
                ppInvoiceDetailCost: this.pPInvoice.ppInvoiceDetailCost,
                isHaiQuan: isHq,
                pPInvoice: this.pPInvoice
            });
        }
    }

    print($event?) {}

    printPPI(isDownload, typeReports: number) {
        if (this.isReadOnly) {
            this.pPInvoiceService
                .getCustomerReport({
                    id: this.pPInvoice.id,
                    typeID: PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN,
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
            if (typeReports === PPINVOICE_TYPE.TYPE_REPORT_NHAP_KHO || typeReports === PPINVOICE_TYPE.TYPE_REPORT_NHAP_KHO_A5) {
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.pPInvoice.print.nhapKho') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
            } else if (typeReports === PPINVOICE_TYPE.TYPE_REPORT_CHUNG_TU) {
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.pPInvoice.print.chungTuKeToan') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
            } else if (typeReports === PPINVOICE_TYPE.TYPE_REPORT_CHUNG_TU_QD) {
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.pPInvoice.print.chungTuKeToanQD') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
            } else if (typeReports === PPINVOICE_TYPE.TYPE_REPORT_BAO_NO) {
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.pPInvoice.print.baoNo') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
            } else if (typeReports === PPINVOICE_TYPE.TYPE_REPORT_PHIEU_CHI) {
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.pPInvoice.print.phieuChi') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
            }
        }
    }

    changeDate() {
        if (this.pPInvoice.date) {
            this.pPInvoice.postedDate = this.pPInvoice.date;
        }
    }

    copyRow(detail: any, number: number) {
        if (!this.getSelectionText()) {
            const detailCopy: any = Object.assign({}, detail);
            detailCopy.id = null;
            if (number === 1) {
                this.pPInvoice.ppInvoiceDetails.push(detailCopy);
                this.changeAmountPPInvoice();
            } else {
                this.pPInvoice.ppInvoiceDetailCost.push(detailCopy);
            }
            if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const lst = this.listIDInputDetail;
                const col = this.indexFocusDetailCol;
                const row = this.pPInvoice.ppInvoiceDetails.length - 1;
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

    getSelectionText() {
        let selectedText = '';
        if (window.getSelection) {
            selectedText = window.getSelection().toString();
        } else if (document.getSelection) {
            selectedText = document.getSelection().toString();
        }
        return selectedText;
    }

    addLastInput(i: number) {
        if (i === this.pPInvoice.ppInvoiceDetails.length - 1) {
            this.addNewRow(1);
        }
    }

    checkVatNull(isNew) {
        for (const item of this.pPInvoice.ppInvoiceDetails) {
            if (item.vatAmountOriginal && (item.vatRate === null || item.vatRate === undefined)) {
                this.checkVATError = 2;
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(this.contentVATModal, { backdrop: 'static' });
                return;
            }
        }
        this.checkVatZero();
    }

    checkVatZero() {
        for (const item of this.pPInvoice.ppInvoiceDetails) {
            if (item.vatAmountOriginal && item.vatRate === 0) {
                this.checkVATError = 3;
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(this.contentVATModal, { backdrop: 'static' });
                return;
            }
        }
        this.savePPI(this.isNew, this.quantityLargerModal);
    }

    changeCheckVATError() {
        this.checkVATError = 1;
        this.isNew = false;
    }

    checkVat() {
        if (this.pPInvoice.ppInvoiceDetails && this.pPInvoice.ppInvoiceDetails.length > 0) {
            if (this.checkVATError === 1) {
                this.checkVatNull(this.isNew);
            } else if (this.checkVATError === 2) {
                this.checkVatZero();
            } else {
                this.checkVATError = 1;
                this.savePPI(this.isNew, this.quantityLargerModal);
            }
        } else {
            this.savePPI(this.isNew, this.quantityLargerModal);
        }
    }

    checkBeforeSave(isNew: boolean) {
        this.isNew = isNew;
        this.checkVat();
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.pPInvoice.ppInvoiceDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.pPInvoice;
    }

    addDataToDetail() {
        this.pPInvoice.ppInvoiceDetails = this.details ? this.details : this.pPInvoice.ppInvoiceDetails;
        this.pPInvoice = this.parent ? this.parent : this.pPInvoice;
    }

    changInvoiceSeries(detail: IPPInvoiceDetails) {
        if (detail.invoiceSeries) {
            detail.invoiceSeries = detail.invoiceSeries.toUpperCase();
        }
    }

    changInvoiceInvoiceTemplate(detail: IPPInvoiceDetails) {
        if (detail.invoiceTemplate === '01/ hoặc 02/') {
            detail.invoiceTemplate = this.invoiceTemplateDefault;
        }
    }

    changeDescription(detail: IPPInvoiceDetails) {
        if (this.taxCalculationMethod) {
            detail.vatDescription = detail.description;
        }
    }

    actionFocus(indexCol, indexRow) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
    }

    registerSelectMaterialGoodsSpecification() {
        this.eventSubscriber = this.eventManager.subscribe('selectMaterialGoodsSpecification', response => {
            this.viewMaterialGoodsSpecification(this.contextMenu.selectedData);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    viewMaterialGoodsSpecification(detail) {
        if (this.isEdit) {
            if (detail.materialGood) {
                if (detail.materialGood.isFollow) {
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
            } else {
                this.toastrService.error(this.translateService.instant('ebwebApp.materialGoodsSpecifications.noMaterialGoods'));
            }
        }
    }

    registerMaterialGoodsSpecifications() {
        this.eventManager.subscribe('materialGoodsSpecifications', ref => {
            this.pPInvoice.ppInvoiceDetails[this.currentRow].materialGoodsSpecificationsLedgers = ref.content;
            this.pPInvoice.ppInvoiceDetails[this.currentRow].quantity = ref.content.reduce(function(prev, cur) {
                return prev + cur.iWQuantity;
            }, 0);
        });
    }
}
