import { AfterViewChecked, AfterViewInit, Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';

import { IPPDiscountReturn, PPDiscountReturn } from 'app/shared/model/pp-discount-return.model';
import { PPDiscountReturnService } from './pp-discount-return.service';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { CurrencyService } from 'app/danhmuc/currency';

import { ToastrService } from 'ngx-toastr';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { IPPDiscountReturnDetails, PPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { IMaterialGoodsInStock } from 'app/shared/model/material-goods.model';
import { RepositoryService } from 'app/danhmuc/repository';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { SaBill } from 'app/shared/model/sa-bill.model';
import { SaBillService } from 'app/ban-hang/xuat-hoa-don/sa-bill.service';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { Unit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import { SystemOptionService } from 'app/he-thong/system-option';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { ExpenseItemService } from 'app/entities/expense-item';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { CostSetService } from 'app/entities/cost-set';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { EMContractService } from 'app/entities/em-contract';
import { BudgetItemService } from 'app/entities/budget-item';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { PPDiscountReturnDetailsService } from 'app/entities/pp-discount-return-details';
import { AccountingObjectBankAccountService } from 'app/entities/accounting-object-bank-account';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { Principal } from 'app/core';
import { TranslateService } from '@ngx-translate/core';
import { ISaBillDetails } from 'app/shared/model/sa-bill-details.model';
import { IaPublishInvoiceDetails } from 'app/shared/model/ia-publish-invoice-details.model';
import { IaPublishInvoiceDetailsService } from 'app/ban-hang/xuat-hoa-don/ia-publish-invoice-details.service';
import { IRSInwardOutward } from 'app/shared/model/rs-inward-outward.model';
import {
    AccountType,
    CategoryName,
    DDSo_NgoaiTe,
    DDSo_TienVND,
    DDSo_TyGia,
    DDSo_TyLe,
    GROUP_TYPEID,
    HH_XUATQUASLTON,
    HMTL_KIEM_HOA_DON,
    HMTL_KIEM_PXUAT,
    REPORT,
    SD_SO_QUAN_TRI,
    SO_LAM_VIEC,
    TCKHAC_SDTichHopHDDT,
    TypeID
} from 'app/app.constants';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { PPInvoiceDetailsService } from 'app/entities/nhan-hoa-don/pp-invoice-details.service';
import * as moment from 'moment';
import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { ExportInvoiceModalService } from 'app/core/login/export-invoice-modal.service';
import { DiscountReturnModalService } from 'app/core/login/discount-return-modal.service';
import { IAccountAllList } from 'app/shared/model/account-all-list.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { XuatHoaDonService } from 'app/ban-hang/xuat-hoa-don/xuat-hoa-don.service';
import { PPInvoiceService } from 'app/muahang/mua_hang_qua_kho';
import { ROLE } from 'app/role.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { RSInwardOutwardService } from 'app/entities/rs-inward-outward/rs-inward-outward.service';
import { EbMaterialGoodsSpecificationsModalComponent } from 'app/shared/modal/material-goods-specifications/material-goods-specifications.component';

@Component({
    selector: 'eb-pp-discount-return-update',
    templateUrl: './pp-discount-return-update.component.html',
    styleUrls: ['discount-return-details.css']
})
export class PPDiscountReturnUpdateComponent extends BaseComponent implements OnInit, AfterViewInit, AfterViewChecked {
    @ViewChild('contentVoucher') private contentVoucherModal: TemplateRef<any>;
    @ViewChild('contentMinus') contentMinus: TemplateRef<any>;
    @ViewChild('deleteItem') deleteItemModal: TemplateRef<any>;
    @ViewChild('contentClose') contentCloseModal: TemplateRef<any>;
    @ViewChild('contentSave') contentSaveModal: TemplateRef<any>;
    @ViewChild('contentVAT') contentVATModal: TemplateRef<any>;
    @ViewChild('contentRecordSave') contentRecordSaveModal: TemplateRef<any>;
    private _pPDiscountReturn: IPPDiscountReturn;
    contextMenu: ContextMenu;
    isSaving: boolean;
    dateDp: any;
    postedDateDp: any;
    invoiceDateDp: any;
    public option1: any;
    public option2: any;
    // employee: IAccountingObject;
    employees: IAccountingObject[];
    accountingObjects: IAccountingObject[];
    accountingObject: IAccountingObject;
    searchVoucher: ISearchVoucher;
    // region tiáº¿n lÃ¹i chá»©ng tá»«
    rowNum: number;
    count: number;
    isRecord: boolean;
    date: any;
    checkNumber: any;
    planningDate: any;
    voucherDate: any;
    currencys: ICurrency[];
    currency: ICurrency;
    checkModalRef: NgbModalRef;
    timeLine: any;
    dtBeginDate: string;
    dtEndDate: string;
    listTimeLine: any[];
    isFormalityReadOnly: Boolean;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    fromDate: any;
    toDate: any;
    ppDiscountReturnDetails: IPPDiscountReturnDetails[];
    materialGoodss: any[];
    globalcheckbox = false;
    mgForPPOderItem: IMaterialGoodsInStock;
    repositoryPopup: any[];
    repository: any;
    creditAccountList: IAccountList[];
    creditAccountListItem: IAccountList;
    debitAccountList: IAccountList[];
    debitAccountItem: IAccountList;
    saBillList: any[];
    units: Unit[];
    checked: boolean;
    unit: Unit;
    mainUnitName: any;
    amountOriginal: any;
    numberAmountOriginal: string;
    vatAccountList: IAccountList[];
    IAccountListItem: IAccountList;
    deductionDebitAccountList: any;
    goodsServicePurchase: any;
    expenseItems: IExpenseItem[];
    costSetList: ICostSet[];
    emContractList: IEMContract[];
    budgetItems: IBudgetItem[];
    organizationUnits: any;
    statisticCodes: any;
    ppDiscountReturnDetailsa: any;
    contextmenu = { value: false };
    contextmenuX = { value: 0 };
    contextmenuY = { value: 0 };
    // selectedDetail = PPDiscountReturnDetails[];
    selectedDetailTax = null;
    isShowDetail = { value: false };
    isShowDetailTax = { value: false };
    isReadOnly: boolean;
    accountingObjectBankAccountList: any;
    accountingObjectBankAccount: any;
    authoritiesNoMBook: any;
    sessionWork: any;
    dataSession: IDataSessionStorage;
    itemsPerPage: number;
    pageCount: number;
    totalItems: number;
    searchData: string;
    predicate: any;
    accountingObjectName: string;
    reverse: number;
    page: number;
    checkData: boolean;
    checkReadOnly: boolean;
    account: any;
    isCreateUrl: boolean;
    isEdit: any;
    saBill: SaBill;
    templates: IaPublishInvoiceDetails[];
    template: IaPublishInvoiceDetails;
    rsInwardOutward: IRSInwardOutward;
    discountAndBillAndRSIDTO: any;
    sumQuantity: number;
    sumUnitPriceOriginal: number;
    sumUnitPrice: number;
    sumMainQuantity: number;
    sumMainUnitPrice: number;
    val: any;
    saBillDTL: ISaBillDetails;
    viewVouchersSelected: any[];
    modalRef: NgbModalRef;
    record_: Irecord;
    REPORT = REPORT;
    options = {
        precision: DDSo_TyLe
    };
    sumAmount: number;
    sumAmountOriginal: number;
    DDSo_NgoaiTe: any;
    DDSo_TienVND: any;
    DDSo_Tyle: any;
    DDSO_TYGia: any;
    tyGiaOptions: any;
    eventSubscriber: Subscription;
    searchDataSearch: any;
    idAll: any;
    noBookVoucher: any;
    noBookRSI: any;
    checkedVoucher: any;
    ppInvoiceDetailsConverts: any[];
    ppDiscountreturnCopy: any;
    checkBook: number;
    sabillIdList: any;
    accountingObjectVoucher: any;
    fromDateVoucher: any;
    toDateVoucher: any;
    timeLineVoucher: any;
    timeLineInvoice: any;
    fromDateInvoice: any;
    toDateInvoice: any;
    accountingObjectIncoice: any;
    previousPage: any;
    pageVoucher: any;
    pageCountVoucher: any;
    itemsPerPageVoucher: any;
    totalItemsVoucher: any;
    links: any;
    previousPageVoucher: number;
    totalItemsPPinvoice: any;
    itemsPerPagePPincoice: any;
    pagePPinvoice: number;
    previousPagePPinvoice: any;
    pageCountPPinvoice: any;
    checkModalVoucher: any;
    ppDiscountReturnDetailsCopy: any;
    rsInwardOutwardCopy: IRSInwardOutward;
    saBillCopy: SaBill;
    checkDetailType: boolean;
    saBillDetail: any;
    saBillConvertDTO: any;
    paymentMethod: any;
    printMessages: any[];
    isRequiredInvoiceNo: boolean;
    isCloseAll: boolean;
    columnList = [
        { column: AccountType.TK_CO, ppType: false },
        { column: AccountType.TK_NO, ppType: false },
        { column: AccountType.TK_THUE_GTGT, ppType: false },
        { column: AccountType.TKDU_THUE_GTGT, ppType: false }
    ];
    indexFocusDetailCol: any;
    listIDInputDeatilTax: any[] = [
        'materialGoodsID',
        'description',
        'repository',
        'debitAccountList',
        'creditAccount',
        'accountingObjectHT',
        'unit',
        'quantity',
        'unitPriceOriginal',
        'unitPrice',
        'mainUnitID',
        'mainConvertRate',
        'formula',
        'mainQuantity',
        'mainUnitPrice',
        'amountOriginal',
        'amount',
        'lotNo',
        'expiryDateDetail',
        'ppInvoiceID',
        'materialGoodsID1',
        'description1',
        'debitAccount2',
        'creditAccount2',
        'accountingObjectHT1',
        'vatRate1',
        'vatAmountOriginal',
        'vatAmount',
        'vatAccountList',
        'deductionDebitAccount',
        'goodsServicePurchase',
        'materialGoodsID2',
        'description2',
        'costSet1',
        'budgetItem',
        'organizationUnit',
        'StatisticsCode'
    ];
    indexFocusDetailRow: any;
    checkClose: Boolean;
    startUsing: any;
    statusPurchase: Boolean;
    statusHD: number;
    checkDataDetail: boolean;
    listVAT = [{ name: '0%', data: 0 }, { name: '5%', data: 1 }, { name: '10%', data: 2 }];
    templateDVT: boolean;
    isTichHopHDDT: boolean;
    typeDelete: number;
    isViewFromEInvoice: boolean; // add by Hautv
    typeAmount = true;
    typeAmountOriginal = true;
    checkChangeReason = false;
    checkRefID = new Map();
    MUA_HANG_TRA_LAI = TypeID.MUA_HANG_TRA_LAI;
    /*Xuất kho by Huypq*/
    isFromOutWard: boolean;
    rsDataSession: any;
    rsSearchData: any;
    rsTotalItems: any;
    rsRowNum: any;
    isViewRSInWardOutward: boolean;
    XUAT_KHO = TypeID.XUAT_KHO;
    XUAT_KHO_TU_DIEU_CHINH = TypeID.XUAT_KHO_TU_DIEU_CHINH;
    XUAT_KHO_TU_BAN_HANG = TypeID.XUAT_KHO_TU_BAN_HANG;
    XUAT_KHO_TU_HANG_MUA_TRA_LAI = TypeID.XUAT_KHO_TU_MUA_HANG;
    /*Xuất kho by Huypq*/
    ROLE_Them1 = ROLE.HangMuaTraLai_Them;
    ROLE_Them2 = ROLE.HangMuaGiamGia_Them;
    ROLE_Sua1 = ROLE.HangMuaTraLai_Sua;
    ROLE_Sua2 = ROLE.HangMuaGiamGia_Sua;
    ROLE_Xoa1 = ROLE.HangMuaTraLai_Xoa;
    ROLE_Xoa2 = ROLE.HangMuaGiamGia_Xoa;
    ROLE_GhiSo1 = ROLE.HangMuaTraLai_GhiSo;
    ROLE_GhiSo2 = ROLE.HangMuaGiamGia_GhiSo;
    ROLE_KetXuat2 = ROLE.HangMuaGiamGia_KetXuat;
    ROLE_KetXuat1 = ROLE.HangMuaTraLai_KetXuat;
    ROLE_In2 = ROLE.HangMuaGiamGia_In;
    ROLE_In1 = ROLE.HangMuaTraLai_In;

    checkSLT: boolean;
    mgForPPOderTextCode: any;
    recorded: boolean;
    checkOpenSave: boolean;
    creditAccountDefault: any;
    debitAccountDefault: any;
    vatAccountDefault: any;
    deductionDebitAccountDefault: any;
    checkSave: boolean;
    checkVATError: boolean;
    checkOpenModal: any = new Map();
    // checkOpenModal =  1 check modal tiền thuế != 0 và vat = 0
    // checkOpenModal =  2 check modal tiền thuế != 0 và vat = null
    // checkOpenModal =  3 check modal số lượng tủ chứng từ mua hàng lớn hơn số lượng gốc
    isShowHHDV: boolean;

    constructor(
        private iaPublishInvoiceDetailsService: IaPublishInvoiceDetailsService,
        private ppInvoiceDetailsService: PPInvoiceDetailsService,
        private parseLinks: JhiParseLinks,
        private viewVoucherService: ViewVoucherService,
        private rsInwardOutwardService: RSInwardOutwardService,
        private accountingObjectBankAccountService: AccountingObjectBankAccountService,
        private ppDiscountReturnDetailsService: PPDiscountReturnDetailsService,
        private statisticsCodeService: StatisticsCodeService,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        private budgetItemService: BudgetItemService,
        private emContractService: EMContractService,
        private xuatHoaDonService: XuatHoaDonService,
        private eventManager: JhiEventManager,
        private costSetService: CostSetService,
        private expenseItemService: ExpenseItemService,
        private ppInvoiceService: PPInvoiceService,
        private organizationUnitService: OrganizationUnitService,
        private systemOptionService: SystemOptionService,
        private saBillService: SaBillService,
        private pPDiscountReturnService: PPDiscountReturnService,
        private activatedRoute: ActivatedRoute,
        private accountingObjectService: AccountingObjectService,
        public utilService: UtilsService,
        private gLService: GeneralLedgerService,
        private currencyService: CurrencyService,
        private toasService: ToastrService,
        private modalService: NgbModal,
        private repositoryService: RepositoryService,
        private materialGoodsService: MaterialGoodsService,
        private accountListService: AccountListService,
        private unitService: UnitService,
        private router: Router,
        private principal: Principal,
        public translateService: TranslateService,
        private refModalService: RefModalService,
        private discountReturnModalService: DiscountReturnModalService,
        private exportInvoiceModalService: ExportInvoiceModalService,
        public utilsService: UtilsService
    ) {
        super();
        this.getSessionData();
        this.contextMenu = new ContextMenu();
        this.viewVouchersSelected = [];
        this.listTimeLine = this.utilsService.getCbbTimeLine();
    }

    ngOnInit() {
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);

            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = true;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilsService.setShowPopup(response.content);
        });
        this.templateDVT = true;
        this.checkDataDetail = false;
        this.checkClose = false;
        this.saBillDetail = [];
        this.afterDeleteOrAddRow();
        this.checkDetailType = false;
        this.checkModalVoucher = false;
        this.itemsPerPageVoucher = ITEMS_PER_PAGE;
        this.itemsPerPagePPincoice = ITEMS_PER_PAGE;
        this.pagePPinvoice = 1;
        this.pageVoucher = 1;
        this.accountingObjectIncoice = {};
        this.accountingObjectVoucher = {};
        this.timeLineVoucher = {};
        this.timeLineInvoice = {};
        this.val = 1;
        this.discountAndBillAndRSIDTO = {};
        this.rsInwardOutward = {};
        this.saBill = new SaBill();
        this.saBill.saBillDetails = [];
        this.templates = [];
        this.template = {};
        this.isEdit = true;
        // this.isEditUrl = window.location.href.includes('/edit') || window.location.href.includes('/view');
        this.ppDiscountReturn = {};
        this.ppDiscountReturnDetails = [];
        this.checked = false;
        this.accountingObjects = [];
        this.accountingObject = {};
        this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body;
        });
        this.isSaving = false;
        // this.employee = {};
        this.currency = {};
        this.option1 = false;
        this.checkNumber = false;
        this.isSaving = false;
        this.account = { organizationUnit: {} };
        this.activatedRoute.data.subscribe(data => {
            if (data.statusPurchase !== null && data.statusPurchase !== undefined) {
                this.statusPurchase = data.statusPurchase;
            } else {
                this.statusPurchase = false;
            }
            this.principal.identity().then(account => {
                this.ppDiscountReturn.typeLedger = 2;
                this.account = account;
                this.isShowHHDV = this.account.organizationUnit.taxCalculationMethod === 1;
                this.startUsing = this.account.organizationUnit.startDate;
                this.typeAmount = this.account.organizationUnit.currencyID && this.account.organizationUnit.currencyID === 'VND';
                this.typeAmountOriginal = this.currency.currencyCode && this.currency.currencyCode === 'VND';
                this.sessionWork = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data === '1';
                this.authoritiesNoMBook = this.account.systemOption.find(x => x.code === SD_SO_QUAN_TRI).data === '1';
                this.checkSLT = this.account.systemOption.find(x => x.code === HH_XUATQUASLTON).data === '1';
                this.isRequiredInvoiceNo = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
                this.isTichHopHDDT = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
                if (data.pPDiscountReturn && data.pPDiscountReturn.id) {
                    this.ppDiscountReturn = data.pPDiscountReturn;
                    if (data.pPDiscountReturn.deliveryVoucher) {
                        this.ppDiscountReturn.isDeliveryVoucher = data.pPDiscountReturn.deliveryVoucher;
                    }
                    if (data.pPDiscountReturn.bill) {
                        this.ppDiscountReturn.isBill = data.pPDiscountReturn.bill;
                    }
                    if (this.statusPurchase) {
                        this.ppDiscountReturn.isDeliveryVoucher = false;
                        this.ppDiscountReturn.isBill = true;
                        this.option1 = false;
                        this.option2 = true;
                    } else {
                        if (data.pPDiscountReturn.exportInvoice) {
                            this.ppDiscountReturn.isExportInvoice = data.pPDiscountReturn.exportInvoice;
                        }
                        if (this.ppDiscountReturn.isDeliveryVoucher) {
                            this.option1 = true;
                            this.rsInwardOutwardService.find(this.ppDiscountReturn.rsInwardOutwardID).subscribe(ref => {
                                this.rsInwardOutward = ref.body;
                                // if (this.checkData) {
                                this.noBookRSI = this.checkBook === 1 ? this.rsInwardOutward.noMBook : this.rsInwardOutward.noFBook;
                                // if (this.ppDiscountReturn.typeLedger === 0 || this.ppDiscountReturn.typeLedger === 2) {
                                //     if (this.ppDiscountReturn.isDeliveryVoucher) {
                                //         this.option1 = true;
                                //         this.noBookRSI = this.rsInwardOutward.noFBook;
                                //     }
                                // } else if (this.ppDiscountReturn.typeLedger === 1) {
                                //     this.noBookRSI = this.rsInwardOutward.noMBook;
                                // }
                                // }
                            });
                        } else {
                            this.option1 = false;
                        }
                        this.option2 = this.ppDiscountReturn.isBill;
                    }
                    this.ppDiscountReturnDetails =
                        data.pPDiscountReturn.ppDiscountReturnDetails === undefined
                            ? []
                            : data.pPDiscountReturn.ppDiscountReturnDetails.sort((a, b) => a.orderPriority - b.orderPriority);
                    this.checkData = true;
                    this.checkDataDetail = true;
                    this.checkReadOnly = true;
                    this.isEdit = false;
                    this.pPDiscountReturnService.getRefVouchersByPPdiscountReturnID(data.pPDiscountReturn.id).subscribe(ref => {
                        this.viewVouchersSelected = ref.body;
                    });
                    if (this.dataSession && this.dataSession.isEdit) {
                        this.edit();
                    }
                    if (this.rsDataSession && this.rsDataSession.isEdit) {
                        this.edit();
                    }
                } else {
                    this.checkData = false;
                    this.checkDataDetail = false;
                    this.ppDiscountReturn = {};
                    // this.ppDiscountReturn.reason = this.translateService.instant('ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason');
                    this.translateService.get(['ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason']).subscribe((res: any) => {
                        this.ppDiscountReturn.reason = res['ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason'];
                        this.saBill.reason = this.ppDiscountReturn.reason;
                    });
                    this.translateService.get(['ebwebApp.pPDiscountReturn.home.rsInwardOutwardReason']).subscribe((res: any) => {
                        this.rsInwardOutward.reason = res['ebwebApp.pPDiscountReturn.home.rsInwardOutwardReason'];
                    });
                    this.ppDiscountReturnDetails = [];
                    this.checkReadOnly = false;
                    this.ppDiscountReturn.typeLedger = this.authoritiesNoMBook ? 2 : 0;
                    if (this.statusPurchase) {
                        // this.ppDiscountReturn.reason = this.translateService.instant(
                        //     'ebwebApp.pPDiscountReturn.home.pPDiscountPurchaseReason'
                        // );

                        this.translateService.get(['ebwebApp.pPDiscountReturn.home.pPDiscountPurchaseReason']).subscribe((res: any) => {
                            this.ppDiscountReturn.reason = res['ebwebApp.pPDiscountReturn.home.pPDiscountPurchaseReason'];
                            this.saBill.reason = this.ppDiscountReturn.reason;
                        });
                        this.option2 = true;
                        this.option1 = false;
                    } else {
                        this.option1 = this.account.systemOption.find(x => x.code === HMTL_KIEM_PXUAT).data === '1';
                        this.option2 = this.account.systemOption.find(x => x.code === HMTL_KIEM_HOA_DON).data === '1';
                    }
                }
                if (this.authoritiesNoMBook) {
                    this.checkBook = this.sessionWork ? 1 : 0;
                } else {
                    this.checkBook = 0;
                }
                this.getData();
            });
        });
        /*Xuất kho by Huypq*/
        this.isViewRSInWardOutward = window.location.href.includes('/edit-rs-inward-outward');
        if (this.isViewRSInWardOutward) {
            this.getSessionDataRS();
        }
        /*Xuất kho by Huypq*/
        this.registerRef();
        /*from EInvoice*/
        this.isViewFromEInvoice = window.location.href.includes('/from-einvoice');
        /*from EInvoice*/
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
        });
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = true;
            this.registerComboboxSave(response);
        });
        this.registerSelectMaterialGoodsSpecification();
        this.registerMaterialGoodsSpecifications();
    }

    previousState() {
        window.history.back();
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.ppDiscountReturnDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.ppDiscountReturn;
    }

    addDataToDetail() {
        this.ppDiscountReturnDetails = this.details ? this.details : this.ppDiscountReturnDetails;
        this.ppDiscountReturn = this.parent ? this.parent : this.ppDiscountReturn;
    }

    saveAfter(check?: Boolean) {
        console.log(this.checkSave);
        this.checkOpenModal = new Map();
        this.isEdit = false;
        this.isSaving = true;
        if (this.ppDiscountReturnDetails.length > 0) {
            this.changeTypeLedger();
            this.convertSaBillAndRSI();
            if (this.option2) {
                if (!this.statusPurchase) {
                    this.saBill.saBillDetails = [];
                } else {
                    this.saBill = {};
                }
            }
            for (let i = 0; i < this.ppDiscountReturnDetails.length; i++) {
                this.saBillDTL = {};
                this.ppDiscountReturnDetails[i].orderPriority = i;
                if (this.option2) {
                    if (!this.statusPurchase) {
                        this.saBillDTL = {};
                        this.ppDiscountReturnDetails[i].orderPriority = i;
                        this.convertSaBill(this.saBillDTL, this.ppDiscountReturnDetails[i]);
                        this.saBill.saBillDetails.push(this.saBillDTL);
                    }
                }
            }
            // if (this.option2) {
            //     if (!this.statusPurchase) {
            //         // if (!this.saBill.saBillDetails) {
            //         this.saBill.saBillDetails = [];
            //         // }
            //         for (let i = 0; i < this.ppDiscountReturnDetails.length; i++) {
            //             this.saBillDTL = {};
            //             this.ppDiscountReturnDetails[i].orderPriority = i;
            //             this.convertSaBill(this.saBillDTL, this.ppDiscountReturnDetails[i]);
            //             this.saBill.saBillDetails.push(this.saBillDTL);
            //         }
            //     } else {
            //         this.saBill = {};
            //     }
            // }
            this.ppDiscountReturn.ppDiscountReturnDetails = this.ppDiscountReturnDetails;
            // this.ppDiscountReturn.id = check ? this.ppDiscountReturn.id : '';
            this.ppDiscountReturn.isBill = this.option2 ? this.option2 : false;
            this.ppDiscountReturn.isDeliveryVoucher = this.option1;
            this.discountAndBillAndRSIDTO = {
                ppDiscountReturn: this.ppDiscountReturn,
                rsInwardOutward: this.ppDiscountReturn.isDeliveryVoucher ? this.rsInwardOutward : {},
                saBill: this.saBill,
                viewVouchers: this.viewVouchersSelected,
                statusPurchase: this.statusPurchase ? this.statusPurchase : false,
                record: this.recorded
            };
            if (this.ppDiscountReturn.id) {
                this.pPDiscountReturnService.update(this.discountAndBillAndRSIDTO).subscribe(
                    res => {
                        // this.ppDiscountReturn.recorded = true;
                        this.isCloseAll = true;
                        this.toasService.success(
                            this.translateService.instant('ebwebApp.mBDeposit.editSuccess'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                        this.materialGoodsService.queryForComboboxGood().subscribe(ref => {
                            this.materialGoodss = ref.body;
                        });
                        this.ppDiscountReturn = res.body.ppDiscountReturn;
                        this.ppDiscountReturn.accountingObject = this.accountingObjects.find(
                            n => n.id === this.ppDiscountReturn.accountingObjectID
                        );
                        // this.ppDiscountReturnDetails = this.ppDiscountReturn.ppDiscountReturnDetails ?  this.ppDiscountReturn.ppDiscountReturnDetails : [];
                        this.saBill = res.body.saBill ? res.body.saBill : {};
                        this.rsInwardOutward = res.body.rsInwardOutward ? res.body.rsInwardOutward : {};
                        this.ppDiscountReturn.totalDiscountAmountOriginalReturn =
                            this.ppDiscountReturn.totalAmountOriginal -
                            (this.ppDiscountReturn.totalDiscountAmounOriginal ? this.ppDiscountReturn.totalDiscountAmounOriginal : 0) +
                            this.ppDiscountReturn.totalVATAmountOriginal;
                        this.ppDiscountReturn.totalDiscountAmountOriginalReturn = this.utilsService.round(
                            this.ppDiscountReturn.totalDiscountAmountOriginalReturn,
                            this.account.systemOption,
                            this.typeAmountOriginal ? 1 : 2
                        );
                        this.ppDiscountReturn.totalDiscountAmountReturn =
                            this.ppDiscountReturn.totalAmount -
                            (this.ppDiscountReturn.totalDiscountAmount ? this.ppDiscountReturn.totalDiscountAmount : 0) +
                            this.ppDiscountReturn.totalVATAmount;
                        this.ppDiscountReturn.totalDiscountAmountReturn = this.utilsService.round(
                            this.ppDiscountReturn.totalDiscountAmountReturn,
                            this.account.systemOption,
                            this.typeAmount ? 1 : 2
                        );
                        if (!this.checkSave) {
                            if (!this.statusPurchase) {
                                this.router.navigate(['hang-mua/tra-lai', res.body.id, 'edit', this.rowNum ? this.rowNum : 0]);
                            } else {
                                this.router.navigate(['hang-mua/giam-gia', res.body.id, 'edit', this.rowNum ? this.rowNum : 0]);
                            }
                            this.ngOnInit();
                        } else {
                            this.addNew(event);
                        }
                        if (this.modalRef) {
                            this.modalRef.close();
                        }
                        this.copy();
                    },
                    res => {
                        this.isEdit = true;
                        this.isCloseAll = false;
                        if (res.error.entityName === 'voucherExists') {
                            if (this.statusPurchase) {
                                this.toasService.error(
                                    this.translateService.instant('ebwebApp.pPDiscountReturn.error.discountreturnPurchase'),
                                    this.translateService.instant('ebwebApp.mBDeposit.message')
                                );
                                return;
                            }
                            this.toasService.error(
                                this.translateService.instant('ebwebApp.pPDiscountReturn.error.' + res.error.title),
                                this.translateService.instant('ebwebApp.mBDeposit.message')
                            );
                            return;
                        } else if (res.error.entityName === 'saBill') {
                            this.toasService.error(
                                this.translateService.instant('ebwebApp.saBill.error.' + res.error.errorKey),
                                this.translateService.instant('ebwebApp.mBDeposit.message')
                            );
                            return;
                        } else {
                            this.toasService.error(
                                this.translateService.instant('ebwebApp.pPDiscountReturn.error.unUpdate'),
                                this.translateService.instant('ebwebApp.mBDeposit.message')
                            );
                            return;
                        }
                    }
                );
            } else {
                this.pPDiscountReturnService.create(this.discountAndBillAndRSIDTO).subscribe(
                    res => {
                        // this.ppDiscountReturn.recorded = true;
                        this.isCloseAll = true;
                        this.ppDiscountReturn = res.body.ppDiscountReturn;
                        this.ppDiscountReturn.accountingObject = this.accountingObjects.find(
                            n => n.id === this.ppDiscountReturn.accountingObjectID
                        );
                        // this.ppDiscountReturnDetails = this.ppDiscountReturn.ppDiscountReturnDetails ?  this.ppDiscountReturn.ppDiscountReturnDetails : [];
                        this.saBill = res.body.saBill ? res.body.saBill : {};
                        this.rsInwardOutward = res.body.rsInwardOutward ? res.body.rsInwardOutward : {};
                        this.ppDiscountReturn.totalDiscountAmountOriginalReturn =
                            this.ppDiscountReturn.totalAmountOriginal -
                            (this.ppDiscountReturn.totalDiscountAmounOriginal ? this.ppDiscountReturn.totalDiscountAmounOriginal : 0) +
                            this.ppDiscountReturn.totalVATAmountOriginal;
                        this.ppDiscountReturn.totalDiscountAmountOriginalReturn = this.utilsService.round(
                            this.ppDiscountReturn.totalDiscountAmountOriginalReturn,
                            this.account.systemOption,
                            this.typeAmountOriginal ? 1 : 2
                        );
                        this.ppDiscountReturn.totalDiscountAmountReturn =
                            this.ppDiscountReturn.totalAmount -
                            (this.ppDiscountReturn.totalDiscountAmount ? this.ppDiscountReturn.totalDiscountAmount : 0) +
                            this.ppDiscountReturn.totalVATAmount;
                        this.ppDiscountReturn.totalDiscountAmountReturn = this.utilsService.round(
                            this.ppDiscountReturn.totalDiscountAmountReturn,
                            this.account.systemOption,
                            this.typeAmount ? 1 : 2
                        );
                        this.toasService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.insertSuccess'));
                        this.materialGoodsService.queryForComboboxGood().subscribe(ref => {
                            this.materialGoodss = ref.body;
                        });
                        if (!this.checkSave) {
                            if (!this.statusPurchase) {
                                this.router.navigate(['hang-mua/tra-lai', res.body.id, 'edit', this.rowNum ? this.rowNum : 0]);
                            } else {
                                this.router.navigate(['hang-mua/giam-gia', res.body.id, 'edit', this.rowNum ? this.rowNum : 0]);
                            }
                            this.ngOnInit();
                        } else {
                            this.addNew(event);
                        }
                        if (this.modalRef) {
                            this.modalRef.close();
                        }
                        this.copy();
                    },
                    res => {
                        this.isCloseAll = false;
                        this.isEdit = true;
                        if (res.error.entityName === 'voucherExists') {
                            if (this.statusPurchase) {
                                this.toasService.error(
                                    this.translateService.instant('ebwebApp.pPDiscountReturn.error.discountreturnPurchase'),
                                    this.translateService.instant('ebwebApp.mBDeposit.message')
                                );
                                return;
                            }
                            this.toasService.error(
                                this.translateService.instant('ebwebApp.pPDiscountReturn.error.' + res.error.title),
                                this.translateService.instant('ebwebApp.mBDeposit.message')
                            );
                            return;
                        } else if (res.error.entityName === 'saBill') {
                            this.toasService.error(
                                this.translateService.instant('ebwebApp.saBill.error.' + res.error.errorKey),
                                this.translateService.instant('ebwebApp.mBDeposit.message')
                            );
                            return;
                        } else {
                            this.toasService.error(
                                this.translateService.instant('ebwebApp.pPDiscountReturn.error.unUpdate'),
                                this.translateService.instant('ebwebApp.mBDeposit.message')
                            );
                            return;
                        }
                    }
                );
            }
        } else {
            this.isEdit = true;
            this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.notPPDiscountReturn'));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPPDiscountReturn>>) {
        result.subscribe((res: HttpResponse<IPPDiscountReturn>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    get ppDiscountReturn() {
        return this._pPDiscountReturn;
    }

    set ppDiscountReturn(pPDiscountReturn: IPPDiscountReturn) {
        this._pPDiscountReturn = pPDiscountReturn;
    }

    private onError(message: string) {}

    selectAccountingObject() {
        if (this.ppDiscountReturn.accountingObject) {
            // chứng từ
            this.ppDiscountReturn.accountingObjectID = this.ppDiscountReturn.accountingObject.id;
            this.ppDiscountReturn.accountingObjectName = this.ppDiscountReturn.accountingObject.accountingObjectName;
            this.ppDiscountReturn.accountingObjectAddress = this.ppDiscountReturn.accountingObject.accountingObjectAddress;
            this.ppDiscountReturn.companyTaxCode = this.ppDiscountReturn.accountingObject.taxCode;
            this.ppDiscountReturn.contactName = this.ppDiscountReturn.accountingObject.contactName;
            if (!this.checkChangeReason) {
                if (this.statusPurchase) {
                    this.ppDiscountReturn.reason = this.translateService.instant(
                        'ebwebApp.pPDiscountReturn.home.pPDiscountPurchaseReasonObject',
                        {
                            object: this.ppDiscountReturn.accountingObject.accountingObjectName
                        }
                    );
                    this.saBill.reason = this.ppDiscountReturn.reason;
                } else {
                    this.ppDiscountReturn.reason = this.translateService.instant(
                        'ebwebApp.pPDiscountReturn.home.pPDiscountReturnReasonObject',
                        {
                            object: this.ppDiscountReturn.accountingObject.accountingObjectName
                        }
                    );
                    this.saBill.reason = this.translateService.instant('ebwebApp.pPDiscountReturn.home.pPDiscountReturnReasonObject', {
                        object: this.ppDiscountReturn.accountingObject.accountingObjectName
                    });
                    this.rsInwardOutward.reason = this.translateService.instant(
                        'ebwebApp.pPDiscountReturn.home.rsInwardOutwardReasonObject',
                        {
                            object: this.ppDiscountReturn.accountingObject.accountingObjectName
                        }
                    );
                }
            }
            if (this.ppDiscountReturn.accountingObject) {
                this.accountingObjectBankAccountService
                    .getByAccountingObjectById({
                        accountingObjectID: this.ppDiscountReturn.accountingObject.id
                    })
                    .subscribe(ref => {
                        this.accountingObjectBankAccountList = ref.body;
                        if (this.checkData && this.ppDiscountReturn.isBill) {
                            this.accountingObjectBankAccount = this.accountingObjectBankAccountList.some(
                                i => i.bankAccount === this.ppDiscountReturn.accountingObjectBankAccount
                            );
                        }
                    });
            }
        } else {
            this.ppDiscountReturn.accountingObject = null;
            this.ppDiscountReturn.accountingObjectID = null;
            this.ppDiscountReturn.accountingObjectID = null;
            this.ppDiscountReturn.accountingObjectName = null;
            this.ppDiscountReturn.accountingObjectAddress = null;
            this.ppDiscountReturn.companyTaxCode = null;
            this.ppDiscountReturn.contactName = null;
            if (this.statusPurchase) {
                this.ppDiscountReturn.reason = this.translateService.instant('ebwebApp.pPDiscountReturn.home.pPDiscountReturnReasonTitle');
                this.saBill.reason = this.translateService.instant('ebwebApp.pPDiscountReturn.home.pPDiscountReturnReasonTitle');
                this.rsInwardOutward.reason = this.translateService.instant('ebwebApp.pPDiscountReturn.home.pPDiscountReturnReasonTitle');
            } else {
                this.ppDiscountReturn.reason = this.translateService.instant('ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason');
                this.saBill.reason = this.translateService.instant('ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason');
                this.rsInwardOutward.reason = this.translateService.instant('ebwebApp.pPDiscountReturn.home.rsInwardOutwardReason');
            }
        }
        this.ppDiscountReturnDetails.forEach(x => {
            x.accountingObject = this.ppDiscountReturn.accountingObject;
        });
    }

    private validatePurchaseGiveBack() {
        if (this.checkCloseBook(this.account, this.ppDiscountReturn.postedDate)) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.lockBook.err.checkForSave'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.ppDiscountReturn.date) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pPDiscountReturn.error.date', 'ebwebApp.pPDiscountReturn.error.error')
            );
            return false;
        }
        if (!this.ppDiscountReturn.postedDate) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pPDiscountReturn.error.postedDate', 'ebwebApp.pPDiscountReturn.error.error')
            );
            return false;
        }
        if (!this.checkPostedDateGreaterDate()) {
            return false;
        }
        if (this.ppDiscountReturn.totalAmount === null || this.ppDiscountReturn.totalAmount === undefined) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pPDiscountReturn.error.totalAmount', 'ebwebApp.pPDiscountReturn.error.error')
            );
            return false;
        }
        if (this.ppDiscountReturn.totalAmountOriginal === null || this.ppDiscountReturn.totalAmountOriginal === undefined) {
            this.toasService.error(
                this.translateService.instant(
                    'ebwebApp.pPDiscountReturn.error.totalAmountOriginal',
                    'ebwebApp.pPDiscountReturn.error.error'
                )
            );
            return false;
        }
        if (this.ppDiscountReturn.totalVATAmount === null || this.ppDiscountReturn.totalVATAmount === undefined) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pPDiscountReturn.error.totalVATAmount', 'ebwebApp.pPDiscountReturn.error.error')
            );
            return false;
        }
        if (this.ppDiscountReturn.totalVATAmountOriginal === null || this.ppDiscountReturn.totalVATAmountOriginal === undefined) {
            this.toasService.error(
                this.translateService.instant(
                    'ebwebApp.pPDiscountReturn.error.totalVATAmountOriginal',
                    'ebwebApp.pPDiscountReturn.error.error'
                )
            );
            return false;
        }
        const errorData = this.ppDiscountReturnDetails.filter(item => !item.materialGoodsID || !item.mgForPPOderItem);
        if (errorData && errorData.length) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pporder.error.required'),
                this.translateService.instant('ebwebApp.pPDiscountReturn.voucher')
            );
            return false;
        }
        const errorRepository = this.ppDiscountReturnDetails.filter(
            item =>
                !(item.mgForPPOderItem && (item.mgForPPOderItem.materialGoodsType === 2 || item.mgForPPOderItem.materialGoodsType === 4)) &&
                !item.repository &&
                this.option1
        );
        if (errorRepository && errorRepository.length) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pporder.error.required'),
                this.translateService.instant('ebwebApp.pPDiscountReturn.voucher')
            );
            return false;
        }
        if (this.ppDiscountReturn.isExportInvoice) {
            const errorExportInvoice = this.ppDiscountReturnDetails.some(item => item.saBillID !== null && item.saBillID !== undefined);
            if (!errorExportInvoice) {
                this.toasService.error(
                    this.translateService.instant('ebwebApp.pporder.error.required'),
                    this.translateService.instant('ebwebApp.pPDiscountReturn.voucher')
                );
                return;
            }
        }
        const vatAccountItem = this.ppDiscountReturnDetails.filter(
            item => item.vatRate !== null && item.vatRate !== undefined && !item.vatAccountItem
        );
        if (vatAccountItem && vatAccountItem.length) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pporder.error.required'),
                this.translateService.instant('ebwebApp.pPDiscountReturn.voucher')
            );
            return false;
        }
        const deductionDebitAccountItem = this.ppDiscountReturnDetails.filter(
            item => item.vatRate !== null && item.vatRate !== undefined && !item.deductionDebitAccountItem
        );
        if (deductionDebitAccountItem && deductionDebitAccountItem.length) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pporder.error.required'),
                this.translateService.instant('ebwebApp.pPDiscountReturn.voucher')
            );
            return false;
        }
        const detailType = this.ppDiscountReturnDetails.filter(
            item =>
                (item.creditAccountItem &&
                    item.creditAccountItem.detailType !== null &&
                    item.creditAccountItem.detailType !== undefined &&
                    item.creditAccountItem.detailType === '0') ||
                (item.debitAccountItem &&
                    item.debitAccountItem.detailType !== null &&
                    item.debitAccountItem.detailType !== undefined &&
                    item.debitAccountItem.detailType === '0')
        );
        if (detailType && detailType.length && !this.ppDiscountReturn.accountingObject) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pPDiscountReturn.error.detailType', {
                    accountNumber:
                        detailType[0].creditAccountItem.detailType === '0'
                            ? detailType[0].creditAccountItem.accountNumber
                            : detailType[0].debitAccountItem.accountNumber
                })
            );
            this.checkDetailType = true;
            return false;
        }

        const creditAccount = this.ppDiscountReturnDetails.filter(item => !item.creditAccountItem);
        if (creditAccount && creditAccount.length) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pporder.error.required'),
                this.translateService.instant('ebwebApp.pPDiscountReturn.voucher')
            );
            return false;
        }
        const debitAccount = this.ppDiscountReturnDetails.filter(item => !item.debitAccountItem);
        if (debitAccount && debitAccount.length) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pporder.error.required'),
                this.translateService.instant('ebwebApp.pPDiscountReturn.voucher')
            );
            return false;
        }
        // const unitPrice = this.ppDiscountReturnDetails.filter(item => !item.unitID);
        // if (unitPrice && unitPrice.length) {
        //     this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.unitPrice'));
        //     return false;
        // }
        // const unitPriceOriginal = this.ppDiscountReturnDetails.filter(item => !item.mainUnitName);
        // if (unitPriceOriginal && unitPriceOriginal.length) {
        //     this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.mainUnitName'));
        //     return false;
        // }
        const amountOriginal = this.ppDiscountReturnDetails.filter(
            item => item.amountOriginal === null && !isNaN(Number(item.amountOriginal.toString()))
        );
        if (amountOriginal && amountOriginal.length) {
            this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.amountOriginal'));
            return false;
        }
        const amount = this.ppDiscountReturnDetails.filter(item => item.amount === null && !isNaN(Number(item.amount.toString())));
        if (amount && amount.length) {
            this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.amount'));
            return false;
        }
        const VATAmount = this.ppDiscountReturnDetails.filter(item => item.vatAmount === null && !isNaN(Number(item.vatAmount.toString())));
        if (VATAmount && VATAmount.length) {
            this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.VATAmount'));
            return false;
        }
        const VATAmountOriginal = this.ppDiscountReturnDetails.filter(
            item => item.vatAmountOriginal === null && !isNaN(Number(item.vatAmountOriginal.toString()))
        );
        if (VATAmountOriginal && VATAmountOriginal.length) {
            this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.VATAmountOriginal'));
            return false;
        }
        if (this.ppDiscountReturn.companyTaxCode && !this.utilService.checkMST(this.ppDiscountReturn.companyTaxCode)) {
            this.toasService.error(this.translateService.instant('ebwebApp.saBill.error.mst'));
            return false;
        }
        if (!this.statusPurchase) {
            if (this.option2) {
                if (this.saBill.invoiceTemplate === null || this.saBill.invoiceTemplate === undefined) {
                    this.toasService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceTemplate'));
                    return false;
                }
                if (!this.saBill.invoiceSeries) {
                    this.toasService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceSeries'));
                    return false;
                }
                if (!(this.isRequiredInvoiceNo && this.saBill.invoiceForm === 0) && !this.saBill.invoiceNo) {
                    this.toasService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceNo'));
                    return false;
                }
                if (!this.saBill.invoiceDate) {
                    this.toasService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceDate'));
                    return false;
                }
                // if (this.saBill.invoiceDate.isBefore(this.template.startUsing)) {
                //     this.toasService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceDate2'));
                //     return false;
                // }
                // if (!this.saBill.accountingObject || !this.saBill.accountingObject.id) {
                //     this.toasService.error(this.translateService.instant('ebwebApp.saBill.error.accountingObject'));
                //     return false;
                // }
                if (!this.saBill.paymentMethod) {
                    this.toasService.error(this.translateService.instant('ebwebApp.saBill.error.paymentMethod'));
                    return false;
                }
            }
        }
        if (!this.noBookVoucher) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pporder.error.required'),
                this.translateService.instant('ebwebApp.pPDiscountReturn.voucher')
            );
            return false;
        }
        if (this.ppDiscountReturn.exchangeRate === null || this.ppDiscountReturn.exchangeRate === undefined) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pporder.error.required'),
                this.translateService.instant('ebwebApp.pPDiscountReturn.exchangeRate')
            );
            return false;
        }

        if (!this.currency || !this.ppDiscountReturn.currencyID) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pporder.error.required'),
                this.translateService.instant('ebwebApp.currency.home.title')
            );
            return false;
        }

        if (this.noBookVoucher && !this.utilsService.checkNoBook(this.noBookVoucher)) {
            this.toasService.error(
                this.translateService.instant('global.data.noVoucherInvalid'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (this.noBookRSI && this.ppDiscountReturn.isDeliveryVoucher && !this.utilsService.checkNoBook(this.noBookRSI)) {
            this.toasService.error(
                this.translateService.instant('global.data.noVoucherInvalid'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        let materialGoodsSpecificationsError = '';
        this.ppDiscountReturnDetails.forEach(detail => {
            if (
                !this.ppDiscountReturn.id &&
                detail.mgForPPOderItem.isFollow &&
                (!detail.materialGoodsSpecificationsLedgers || detail.materialGoodsSpecificationsLedgers.length === 0)
            ) {
                materialGoodsSpecificationsError = materialGoodsSpecificationsError + ', ' + detail.mgForPPOderItem.materialGoodsCode;
            }
        });
        if (materialGoodsSpecificationsError) {
            materialGoodsSpecificationsError = materialGoodsSpecificationsError.substring(2, materialGoodsSpecificationsError.length);
            this.toasService.error(
                'Hàng hóa ' +
                    materialGoodsSpecificationsError +
                    ' ' +
                    this.translateService.instant('ebwebApp.materialGoodsSpecifications.noMaterialGoodsSpecifications')
            );
            return false;
        }
        return true;
    }

    selectCurrencyObjects() {
        if (this.currency) {
            // if (!this.checkData) {
            this.ppDiscountReturn.currencyID = this.currency.currencyCode;
            this.ppDiscountReturn.exchangeRate = this.currency.exchangeRate;
            // }
            this.typeAmountOriginal = this.currency.currencyCode && this.currency.currencyCode === 'VND';
            for (let i = 0; i < this.ppDiscountReturnDetails.length; i++) {
                this.changeUnitPriceOriginal(this.ppDiscountReturnDetails[i]);
            }
        } else {
            this.ppDiscountReturn.currencyID = null;
            this.ppDiscountReturn.exchangeRate = null;
        }

        // tính lại thành tiền
        this.sumAllList(true);
        // this.copy();
    }

    // selectEmployee() {
    //     if (this.employee) {
    //         this.ppDiscountReturn.employeeID = this.employee.id;
    //     } else {
    //         this.ppDiscountReturn.employeeID = null;
    //         this.employee = null;
    //     }
    // }

    changeIsAttachList() {
        if (!this.ppDiscountReturn.isAttachList) {
            this.ppDiscountReturn.listNo = '';
            this.ppDiscountReturn.listDate = null;
            this.ppDiscountReturn.listCommonNameInventory = '';
            this.saBill.listNo = '';
            this.saBill.listDate = null;
            this.saBill.listCommonNameInventory = '';
        }
    }

    openModel() {
        if (this.checkData) {
            this.ppDiscountReturnDetails = [];
        }
        this.modalRef = this.exportInvoiceModalService.open(
            this.ppDiscountReturnDetails,
            this.currency.currencyCode,
            this.ppDiscountReturn.accountingObjectID,
            GROUP_TYPEID.GROUP_PPDISCOUNTRETURN
        );
    }

    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('giam-gia') ? ROLE.HangMuaTraLai_Xoa : ROLE.HangMuaGiamGia_Xoa])
    delete() {
        event.preventDefault();
        if (
            !(this.isEdit || (!(this.ppDiscountReturn && this.ppDiscountReturn.id) || this.ppDiscountReturn.recorded)) &&
            !this.utilsService.isShowPopup
        ) {
            if (this.ppDiscountReturn.isBill && this.saBill.invoiceForm === 0 && this.isTichHopHDDT) {
                if (this.saBill.invoiceNo) {
                    this.typeDelete = 2;
                } else {
                    this.typeDelete = 0;
                }
            } else if (this.ppDiscountReturn.isBill) {
                this.typeDelete = 1;
            } else {
                this.typeDelete = 0;
            }
            this.checkModalRef = this.modalService.open(this.deleteItemModal, { size: 'lg', backdrop: 'static' });
        }
    }

    toggleCheckBoxes() {
        if (this.globalcheckbox) {
            this.globalcheckbox = false;
        } else {
            this.globalcheckbox = true;
        }
    }

    addNewRow(index: number, checkCopy: boolean, checkControl: boolean, isRightClick?) {
        const inputs = document.getElementsByTagName('input');
        if (!checkCopy) {
            this.ppDiscountReturnDetails.push({});
            if (this.ppDiscountReturn.accountingObject) {
                this.ppDiscountReturnDetails[
                    this.ppDiscountReturnDetails.length - 1
                ].accountingObject = this.ppDiscountReturn.accountingObject;
                this.ppDiscountReturnDetails[
                    this.ppDiscountReturnDetails.length - 1
                ].accountingObjectID = this.ppDiscountReturn.accountingObject.id;
            }
            this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].vatRate = null;
            if (this.creditAccountDefault) {
                this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].creditAccountItem = this.creditAccountList.find(
                    x => x.accountNumber === this.creditAccountDefault
                );
                if (this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].creditAccountItem) {
                    this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].creditAccount = this.ppDiscountReturnDetails[
                        this.ppDiscountReturnDetails.length - 1
                    ].creditAccountItem.accountNumber;
                }
            }
            if (this.debitAccountDefault) {
                this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].debitAccountItem = this.debitAccountList.find(
                    x => x.accountNumber === this.debitAccountDefault
                );
                if (this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].debitAccountItem) {
                    this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].debitAccount = this.ppDiscountReturnDetails[
                        this.ppDiscountReturnDetails.length - 1
                    ].debitAccountItem.accountNumber;
                }
            }
            if (this.vatAccountDefault) {
                this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].vatAccountItem = this.vatAccountList.find(
                    x => x.accountNumber === this.vatAccountDefault
                );
                if (this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].vatAccountItem) {
                    this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].vatAccount = this.ppDiscountReturnDetails[
                        this.ppDiscountReturnDetails.length - 1
                    ].vatAccountItem.accountNumber;
                }
            }
            if (this.deductionDebitAccountDefault) {
                this.ppDiscountReturnDetails[
                    this.ppDiscountReturnDetails.length - 1
                ].deductionDebitAccountItem = this.deductionDebitAccountList.find(
                    x => x.accountNumber === this.deductionDebitAccountDefault
                );
                if (this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].deductionDebitAccountItem) {
                    this.ppDiscountReturnDetails[
                        this.ppDiscountReturnDetails.length - 1
                    ].deductionDebitAccount = this.ppDiscountReturnDetails[
                        this.ppDiscountReturnDetails.length - 1
                    ].deductionDebitAccountItem.accountNumber;
                }
            } else {
                if (this.ppDiscountReturnDetails && this.ppDiscountReturnDetails.length > 0) {
                    this.ppDiscountReturnDetails[
                        this.ppDiscountReturnDetails.length - 1
                    ].deductionDebitAccountItem = this.deductionDebitAccountList.find(x => x.accountNumber === this.debitAccountDefault);
                    if (this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].deductionDebitAccountItem) {
                        this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].debitAccount = this.ppDiscountReturnDetails[
                            this.ppDiscountReturnDetails.length - 1
                        ].deductionDebitAccountItem.accountNumber;
                    }
                }
            }
            this.contextmenu.value = false;
            this.sumAllList();
            this.checkDetailTypeObject();
        } else {
            const addRow = this.ppDiscountReturnDetails[index];
            addRow.id = null;
            this.ppDiscountReturnDetails.push(this.utilService.deepCopyObject(addRow));
        }
        if (isRightClick || checkControl) {
            const lst = this.listIDInputDeatilTax;
            const col = this.indexFocusDetailCol;
            let row: any;
            if (checkControl) {
                row = this.ppDiscountReturnDetails.length - 1;
            } else {
                row = this.indexFocusDetailRow + 1;
            }
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(lst[col] + row);
                if (element) {
                    element.focus();
                }
            }, 0);
        } else {
            const nameTag: string = event.srcElement.id;
            const idx: number = this.ppDiscountReturnDetails.length - 1;
            const nameTagIndex: string = nameTag + String(idx);
            console.log(nameTag);
            console.log(idx);
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(nameTagIndex);
                if (element) {
                    console.log(element);
                    element.focus();
                }
            }, 0);
        }
    }

    actionFocus(indexCol, indexRow) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
    }

    selectChangeDiscountReturnObjects(detail: IPPDiscountReturnDetails) {
        if (detail.mgForPPOderItem) {
            detail.materialGoodsID = detail.mgForPPOderItem.id;
            detail.materialGoodsCode = detail.mgForPPOderItem.materialGoodsCode;
            detail.description = detail.mgForPPOderItem.materialGoodsName;
            detail.vatDescription = 'Thuế GTGT ' + detail.mgForPPOderItem.materialGoodsCode;
            detail.quantity = 0;
            detail.unitPrice = 0;
            detail.unitPriceOriginal = 0;
            detail.vatRate = null;
            detail.repository = this.repositoryPopup.find(n => n.id === detail.mgForPPOderItem.repositoryID);
            if (this.creditAccountDefault) {
                detail.creditAccountItem = this.creditAccountList.find(x => x.accountNumber === this.creditAccountDefault);
                detail.creditAccount = detail.creditAccountItem.accountNumber;
            }
            if (this.debitAccountDefault) {
                detail.debitAccountItem = this.debitAccountList.find(x => x.accountNumber === this.debitAccountDefault);
                detail.debitAccount = detail.debitAccountItem.accountNumber;
            }
            if (this.vatAccountDefault) {
                detail.vatAccountItem = this.vatAccountList.find(x => x.accountNumber === this.vatAccountDefault);
                detail.vatAccount = detail.vatAccountItem.accountNumber;
            }
            if (this.deductionDebitAccountDefault) {
                detail.deductionDebitAccountItem = this.deductionDebitAccountList.find(
                    x => x.accountNumber === this.deductionDebitAccountDefault
                );
                detail.deductionDebitAccount = detail.deductionDebitAccountItem.accountNumber;
            }
            if (detail.repository && detail.repository.id) {
                this.selectRepository(detail);
            }
            // đơn vị tính
            this.unitService
                .convertRateForMaterialGoodsComboboxCustom({
                    materialGoodsId: detail.mgForPPOderItem.id
                })
                .subscribe(ref => {
                    detail.units = ref.body;
                });
            // đơn vị tính chính
            this.unitService
                .getMainUnitName({
                    materialGoodsId: detail.mgForPPOderItem.id
                })
                .subscribe(ref => {
                    detail.mainUnitName = ref.body.unitName;
                    detail.mainUnitID = ref.body.id;
                    detail.mainUnit = ref.body;
                    detail.unit = ref.body;
                    this.selectUnit(detail);
                    this.sumAllList();
                    // this.sumAllList(detail);
                });
            this.goodsServicePurchaseService.getPurchaseName().subscribe(ref => {
                detail.goodsServicePurchase = ref.body;
                this.selectGoodsServiceObjects(detail);
            });
            if (detail.mgForPPOderItem.expenseAccount) {
                detail.creditAccountItem = this.creditAccountList.find(n => n.accountNumber === detail.mgForPPOderItem.expenseAccount);
            }
        } else {
            detail.materialGoodsID = null;
            detail.description = null;
            detail.vatDescription = null;
        }
        // if (isRightClick) {
        //     const lst = this.listIDInputDeatilTax;
        //     const col = this.indexFocusDetailCol;
        //     const row = this.indexFocusDetailRow + 1;
        //     setTimeout(function() {
        //         const element: HTMLElement = document.getElementById(lst[col] + row);
        //         if (element) {
        //             element.focus();
        //         }
        //     }, 0);
        // } else {
        const nameTag: string = event.srcElement.id;
        const idx: number = this.ppDiscountReturnDetails.length - 1;
        const nameTagIndex: string = nameTag + String(idx);
        setTimeout(function() {
            const element: HTMLElement = document.getElementById(nameTagIndex);
            if (element) {
                element.focus();
            }
        }, 0);
        // }
    }

    sumAllList(checkExchangeRate?: Boolean) {
        this.ppDiscountReturn.totalAmount = 0;
        this.ppDiscountReturn.totalAmountOriginal = 0;
        this.ppDiscountReturn.totalVATAmount = 0;
        this.ppDiscountReturn.totalVATAmountOriginal = 0;
        this.ppDiscountReturn.totalDiscountAmountReturn = 0;
        this.ppDiscountReturn.totalDiscountAmountOriginalReturn = 0;
        this.sumQuantity = 0;
        this.sumUnitPriceOriginal = 0;
        this.sumUnitPrice = 0;
        this.sumMainQuantity = 0;
        this.sumMainUnitPrice = 0;
        this.sumAmount = 0;
        this.sumAmountOriginal = 0;
        if (this.ppDiscountReturnDetails && this.ppDiscountReturnDetails.length > 0) {
            this.ppDiscountReturnDetails.forEach(item => {
                // chọn loại tiền tính lại các quy đổi
                if (checkExchangeRate) {
                    // đơn giá quy đổi
                    item.unitPrice =
                        item.unitPriceOriginal && this.ppDiscountReturn.exchangeRate
                            ? item.unitPriceOriginal * this.ppDiscountReturn.exchangeRate
                            : 0;
                    // thành tiền quy đổi
                    if (item.amountOriginal && this.ppDiscountReturn.exchangeRate) {
                        item.amount = item.amountOriginal * this.ppDiscountReturn.exchangeRate;
                    }
                    // thuế gtgt quy đổi
                    if (item.vatAmountOriginal && this.ppDiscountReturn.exchangeRate) {
                        item.vatAmount = item.vatAmountOriginal * this.ppDiscountReturn.exchangeRate;
                    }
                }
                // tính tổng số lượng
                if (item.quantity) {
                    this.sumQuantity += item.quantity;
                }
                // tính tổng đơn giá
                if (item.unitPriceOriginal) {
                    this.sumUnitPriceOriginal += item.unitPriceOriginal;
                }
                // Tổng đơn giá quy đổi
                if (item.unitPrice) {
                    this.sumUnitPrice += item.unitPrice;
                }
                // số lượng theo đơn vị chính
                if (item.mainQuantity) {
                    this.sumMainQuantity += item.mainQuantity;
                }
                // tổng đơn giá theo dvc
                if (item.mainUnitPrice) {
                    this.sumMainUnitPrice += item.mainUnitPrice;
                }
                // tính tổng thành tiền quy đổi
                if (item.amount) {
                    this.sumAmount += item.amount;
                    this.ppDiscountReturn.totalAmount += item.amount;
                } else {
                    item.amount = 0;
                }
                // tính tổng thành tiền
                if (item.amountOriginal) {
                    this.sumAmountOriginal += item.amountOriginal;
                    this.ppDiscountReturn.totalAmountOriginal += item.amountOriginal;
                } else {
                    item.amountOriginal = 0;
                }
                // Tính tổng tiền thuế
                if (item.vatAmount) {
                    this.ppDiscountReturn.totalVATAmount += item.vatAmount;
                } else {
                    item.vatAmount = 0;
                }
                // tính tổng tiền thuế quy đổi
                if (item.vatAmountOriginal) {
                    this.ppDiscountReturn.totalVATAmountOriginal += item.vatAmountOriginal;
                } else {
                    item.vatAmountOriginal = 0;
                }
            });
            this.ppDiscountReturn.totalDiscountAmountOriginalReturn =
                this.ppDiscountReturn.totalAmountOriginal -
                (this.ppDiscountReturn.totalDiscountAmounOriginal ? this.ppDiscountReturn.totalDiscountAmounOriginal : 0) +
                this.ppDiscountReturn.totalVATAmountOriginal;
            this.ppDiscountReturn.totalDiscountAmountOriginalReturn = this.utilsService.round(
                this.ppDiscountReturn.totalDiscountAmountOriginalReturn,
                this.account.systemOption,
                this.typeAmountOriginal ? 1 : 2
            );
            this.ppDiscountReturn.totalDiscountAmountReturn =
                this.ppDiscountReturn.totalAmount -
                (this.ppDiscountReturn.totalDiscountAmount ? this.ppDiscountReturn.totalDiscountAmount : 0) +
                this.ppDiscountReturn.totalVATAmount;
            this.ppDiscountReturn.totalDiscountAmountReturn = this.utilsService.round(
                this.ppDiscountReturn.totalDiscountAmountReturn,
                this.account.systemOption,
                this.typeAmount ? 1 : 2
            );
        }
    }

    changeMainConvertRate(detail: IPPDiscountReturnDetails) {
        if (detail.mainConvertRate) {
            detail.mainQuantity =
                detail.formula === '*' ? detail.quantity * detail.mainConvertRate : detail.quantity / detail.mainConvertRate;
            detail.mainUnitPrice =
                detail.formula === '*'
                    ? detail.unitPriceOriginal / detail.mainConvertRate
                    : detail.unitPriceOriginal * detail.mainConvertRate;
        }
        this.sumAllList();
    }

    selectUnit(detail) {
        if (detail.unit) {
            detail.unitID = detail.unit.id;
            if (detail.mainUnitID === detail.unitID) {
                detail.mainConvertRate = 1;
                detail.formula = '*';
                detail.mainQuantity = detail.quantity;
                detail.mainUnitPrice = detail.unitPriceOriginal;
            } else {
                detail.mainConvertRate = detail.unit.convertRate ? detail.unit.convertRate : 1;
                detail.formula = detail.unit.formula;
                detail.mainQuantity =
                    detail.formula === '*' ? detail.quantity * detail.mainConvertRate : detail.quantity / detail.mainConvertRate;
                detail.mainUnitPrice =
                    detail.formula === '*'
                        ? detail.unitPriceOriginal / detail.mainConvertRate
                        : detail.unitPsavericeOriginal * detail.mainConvertRate;
            }
        } else {
            detail.unitID = null;
        }
    }

    changeAmountOriginal(detail: IPPDiscountReturnDetails) {
        if (detail.amountOriginal && detail.quantity) {
            detail.unitPriceOriginal = detail.amountOriginal / detail.quantity;
            detail.unitPriceOriginal = this.utilsService.round(
                detail.unitPriceOriginal,
                this.account.systemOption,
                this.typeAmountOriginal ? 1 : 2
            );
            // đơn giá quy đổi
            if (detail.unitPriceOriginal && this.ppDiscountReturn.exchangeRate) {
                detail.unitPrice =
                    this.currency.formula === '/'
                        ? detail.unitPriceOriginal / this.ppDiscountReturn.exchangeRate
                        : detail.unitPriceOriginal * this.ppDiscountReturn.exchangeRate;
            } else {
                detail.unitPrice = 0;
            }
        }
        // tính lại đơn giá
        if (detail.quantity) {
            detail.unitPriceOriginal = detail.amountOriginal / detail.quantity;
        }
        //         tính lại đơn giá theo đơn vị chính
        if (detail.formula && !this.statusPurchase) {
            if (detail.formula === '/' && detail.mainConvertRate) {
                detail.mainUnitPrice = detail.unitPriceOriginal * detail.mainConvertRate;
            } else if (detail.formula === '*' && detail.mainConvertRate) {
                detail.mainUnitPrice = detail.unitPriceOriginal / detail.mainConvertRate;
            }
        }
        this.changeAmount(detail);
        // this.selectUnit(detail);
        this.changeVatRate(detail);
        this.sumAllList();
    }

    changeAmount(detail: IPPDiscountReturnDetails) {
        // thành tiền QĐ
        if (detail.amountOriginal !== null && detail.amountOriginal !== undefined && this.ppDiscountReturn.exchangeRate) {
            detail.amount =
                this.currency.formula === '/'
                    ? detail.amountOriginal / this.ppDiscountReturn.exchangeRate
                    : detail.amountOriginal * this.ppDiscountReturn.exchangeRate;
            detail.amount = this.utilsService.round(detail.amount, this.account.systemOption, this.typeAmount ? 1 : 2);
        }
    }

    changeVatRate(detail: IPPDiscountReturnDetails) {
        if (detail.vatRate !== null && detail.vatRate !== undefined && detail.amountOriginal) {
            if (detail.vatRate === 0) {
                detail.vatAmountOriginal = 0;
            }
            if (detail.vatRate === 1) {
                detail.vatAmountOriginal = detail.amountOriginal * 5 / 100;
            } else if (detail.vatRate === 2) {
                detail.vatAmountOriginal = detail.amountOriginal * 10 / 100;
            }
            if (detail.vatAmountOriginal !== null && detail.vatAmountOriginal !== undefined && this.ppDiscountReturn.exchangeRate) {
                detail.vatAmount =
                    this.currency.formula === '/'
                        ? detail.vatAmountOriginal / this.ppDiscountReturn.exchangeRate
                        : detail.vatAmountOriginal * this.ppDiscountReturn.exchangeRate;
            }
        }
        detail.vatAmount = this.utilsService.round(detail.vatAmount, this.account.systemOption, this.typeAmount ? 1 : 2);
        if (detail.vatAmountOriginal) {
            detail.vatAmountOriginal = this.utilsService.round(
                detail.vatAmountOriginal,
                this.account.systemOption,
                this.typeAmountOriginal ? 1 : 2
            );
        }
        this.sumAllList();
    }

    changeVATAmountOriginal(detail: IPPDiscountReturnDetails) {
        if (detail.vatAmountOriginal && this.ppDiscountReturn.exchangeRate) {
            detail.vatAmount =
                this.currency.formula === '/'
                    ? detail.vatAmountOriginal / this.ppDiscountReturn.exchangeRate
                    : detail.vatAmountOriginal * this.ppDiscountReturn.exchangeRate;
        }
        detail.vatAmount = this.utilsService.round(detail.vatAmount, this.account.systemOption, this.typeAmount ? 1 : 2);
        if (detail.vatAmountOriginal) {
            detail.vatAmountOriginal = this.utilsService.round(
                detail.vatAmountOriginal,
                this.account.systemOption,
                this.typeAmountOriginal ? 1 : 2
            );
        }
        this.sumAllList();
    }
    selectGoodsServiceObjects(detail: IPPDiscountReturnDetails) {
        if (detail.goodsServicePurchase) {
            detail.goodsServicePurchaseID = detail.goodsServicePurchase.id;
        }
    }

    selectChangeExpenseItem(detail: IPPDiscountReturnDetails) {
        if (detail.expenseItem) {
            detail.expenseItemID = detail.expenseItem.id;
        }
    }

    selectChangeCostSet(detail: IPPDiscountReturnDetails) {
        if (detail.costSet) {
            detail.costSetID = detail.costSet.id;
        }
    }

    selectChangeEmContract(detail: IPPDiscountReturnDetails) {
        if (detail.emContract) {
            detail.contractID = detail.emContract.id;
        }
    }

    selectChangeBudgetItem(detail: IPPDiscountReturnDetails) {
        if (detail.budgetItem) {
            detail.budgetItemID = detail.budgetItem.id;
        }
    }

    selectChangeOrganizationUnit(detail: IPPDiscountReturnDetails) {
        if (detail.organizationUnit) {
            detail.departmentID = detail.organizationUnit.id;
        }
    }

    selectChangeStatisticsCode(detail: IPPDiscountReturnDetails) {
        if (detail.statisticsCode) {
            detail.statisticsCodeID = detail.statisticsCode.id;
        }
    }

    selectChangeAccountingObjectsHD(detail: IPPDiscountReturnDetails) {
        if (detail.accountingObject) {
            detail.accountingObjectID = detail.accountingObject.id;
        }
    }

    selectRepository(detail: IPPDiscountReturnDetails) {
        if (!(detail.mgForPPOderItem.materialGoodsType === 2 || detail.mgForPPOderItem.materialGoodsType === 4)) {
            if (detail.repository) {
                detail.repositoryCode = !detail.repository ? null : detail.repository.repositoryCode;
                detail.repositoryID = !detail.repository ? null : detail.repository.id;
            } else {
                detail.repositoryID = null;
                detail.repositoryCode = null;
            }
        } else {
            detail.repositoryID = null;
            detail.repositoryCode = null;
        }
    }

    editableForm() {
        this.isReadOnly = false;
    }

    disableContextMenu() {
        this.contextmenu.value = false;
    }

    KeyPress(value: number, select: number) {
        if (select === 0) {
            this.ppDiscountReturnDetails.splice(value, 1);
        }
    }

    selectAccountingObjectBankAccount() {
        if (this.accountingObjectBankAccount) {
            this.saBill.accountingObjectBankAccount = this.accountingObjectBankAccount.bankAccount;
            this.saBill.accountingObjectBankName = this.accountingObjectBankAccount.bankBranchName;
            this.ppDiscountReturn.accountingObjectBankAccount = this.accountingObjectBankAccount.bankAccount;
            this.ppDiscountReturn.accountingObjectBankName = this.accountingObjectBankAccount.bankBranchName;
        } else {
            this.saBill.accountingObjectBankAccount = null;
            this.saBill.accountingObjectBankName = null;
            this.ppDiscountReturn.accountingObjectBankAccount = null;
            this.ppDiscountReturn.accountingObjectBankName = null;
        }
    }

    selectDebitAccountItem(detail: IPPDiscountReturnDetails) {
        if (detail.debitAccountItem) {
            detail.debitAccount = detail.debitAccountItem.accountNumber;
        } else {
            detail.debitAccount = null;
        }
        const aVAT = this.deductionDebitAccountList.find(x => x.accountNumber === detail.debitAccount);
        if (aVAT) {
            this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].deductionDebitAccountItem = aVAT;
            this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].deductionDebitAccount = aVAT.accountNumber;
        }
        // if (this.deductionDebitAccountDefault) {
        //     this.ppDiscountReturnDetails[
        //     this.ppDiscountReturnDetails.length - 1
        //         ].deductionDebitAccountItem = this.deductionDebitAccountList.find(
        //         x => x.accountNumber === this.deductionDebitAccountDefault
        //     );
        //     if (this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].deductionDebitAccountItem) {
        //         this.ppDiscountReturnDetails[
        //         this.ppDiscountReturnDetails.length - 1
        //             ].deductionDebitAccount = this.ppDiscountReturnDetails[
        //         this.ppDiscountReturnDetails.length - 1
        //             ].deductionDebitAccountItem.accountNumber;
        //     }
        // } else {
        //     this.ppDiscountReturnDetails[
        //     this.ppDiscountReturnDetails.length - 1
        //         ].deductionDebitAccountItem = this.deductionDebitAccountList.find(x => x.accountNumber === this.debitAccountDefault);
        //     if (this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].deductionDebitAccountItem) {
        //         this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].debitAccount = this.ppDiscountReturnDetails[
        //         this.ppDiscountReturnDetails.length - 1
        //             ].deductionDebitAccountItem.accountNumber;
        //     }
        // }
        this.checkDetailTypeObject();
    }

    selectCreditAccountItem(detail: IPPDiscountReturnDetails) {
        if (detail.creditAccountItem) {
            detail.creditAccount = detail.creditAccountItem.accountNumber;
        } else {
            detail.creditAccount = null;
        }
        this.checkDetailTypeObject();
    }

    selectvatAccount(detail: IPPDiscountReturnDetails) {
        if (detail.vatAccountItem) {
            detail.vatAccount = detail.vatAccountItem.accountNumber;
        } else {
            detail.vatAccount = null;
        }
    }

    selectdeductionDebitAccount(detail: IPPDiscountReturnDetails) {
        if (detail.deductionDebitAccountItem) {
            detail.deductionDebitAccount = detail.deductionDebitAccountItem.accountNumber;
        } else {
            detail.deductionDebitAccount = null;
        }
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('dataSession'));
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

    getData() {
        // hình thức thanh toán

        this.paymentMethod = [];
        this.translateService
            .get(['global.paymentMethod.cash', 'global.paymentMethod.transfer', 'global.paymentMethod.both'])
            .subscribe(res => {
                this.paymentMethod.push({ data: res['global.paymentMethod.cash'] });
                this.paymentMethod.push({ data: res['global.paymentMethod.transfer'] });
                this.paymentMethod.push({ data: res['global.paymentMethod.both'] });
            });
        // lấy thông tin hóa đơn
        this.iaPublishInvoiceDetailsService.query().subscribe(
            res => {
                this.templates = res.body;
                this.templates.forEach(item => {
                    if (item.invoiceForm === 0) {
                        item.invoiceFormName = 'Hóa đơn điện tử';
                    } else if (item.invoiceForm === 1) {
                        item.invoiceFormName = 'Hóa đơn đặt in';
                    } else if (item.invoiceForm === 2) {
                        item.invoiceFormName = 'Hóa đơn tự in';
                    }
                });
                if (this.checkData) {
                    if (this.ppDiscountReturn.isBill) {
                        this.option2 = true;
                        this.saBillService.findById(this.ppDiscountReturn.ppDiscountReturnDetails[0].saBillID).subscribe(ref => {
                            this.saBill = ref.body.saBill;
                            if (this.saBill && this.saBill.templateID) {
                                this.template = this.templates.find(item => item.id === this.saBill.templateID);
                                this.selectTemplate();
                            }
                        });
                    }
                } else {
                    if (this.templates && this.templates.length === 1) {
                        this.saBill.paymentMethod = this.paymentMethod[2].data;
                        this.template = this.templates[0];
                        this.selectTemplate();
                    }
                }
            },
            ref => {
                if (this.ppDiscountReturn.ppDiscountReturnDetails[0].saBillID) {
                    this.toasService.error(this.translateService.instant('ebwebApp.common.error.infoBill'));
                }
            }
        );
        // tk có
        const typeIDVoucher = this.statusPurchase ? TypeID.MUA_HANG_GIAM_GIA : TypeID.MUA_HANG_TRA_LAI;
        const param = {
            typeID: typeIDVoucher,
            columnName: this.columnList
        };
        this.accountListService.getAccountTypeFour(param).subscribe(res => {
            const dataAccount = res.body;
            this.creditAccountList = dataAccount.creditAccount;
            this.creditAccountDefault = dataAccount.creditAccountDefault;
            this.debitAccountList = dataAccount.debitAccount;
            this.debitAccountDefault = dataAccount.debitAccountDefault;
            this.vatAccountList = dataAccount.vatAccount;
            this.vatAccountDefault = dataAccount.vatAccountDefault;
            this.deductionDebitAccountList = dataAccount.deductionDebitAccount;
            this.deductionDebitAccountDefault = dataAccount.deductionDebitAccountDefault;
            if (this.checkData) {
                this.ppDiscountReturnDetails.forEach(item => {
                    item.creditAccountItem = this.creditAccountList.find(n => n.accountNumber === item.creditAccount);
                    item.debitAccountItem = this.debitAccountList.find(n => n.accountNumber === item.debitAccount);
                    item.vatAccountItem = this.vatAccountList.find(n => n.accountNumber === item.vatAccount);
                    item.deductionDebitAccountItem = this.deductionDebitAccountList.find(
                        n => n.accountNumber === item.deductionDebitAccount
                    );
                });
            }
            this.checkClose = true;
            this.sumAllList();
            this.copy();
        });
        this.materialGoodsService.queryForComboboxGood().subscribe(res => {
            this.materialGoodss = res.body;
            if (this.checkData) {
                this.ppDiscountReturnDetails.forEach(item => {
                    item.mgForPPOderItem = this.materialGoodss.find(n => n.id === item.materialGoodsID);
                    // get đơn vị tính khi xem
                    if (item.materialGoodsID) {
                        this.unitService
                            .convertRateForMaterialGoodsComboboxCustom({
                                materialGoodsId: item.mgForPPOderItem.id
                            })
                            .subscribe(ref => {
                                item.units = ref.body;
                                item.unit = item.units.find(i => i.id === item.unitID);
                                const mainUnitItem = item.units.find(i => i.id === item.mainUnitID);
                                if (mainUnitItem) {
                                    item.mainUnitID = mainUnitItem.id;
                                    item.mainUnitName = mainUnitItem.unitName;
                                }
                            });
                    }
                });
            }
        });
        // lấy kho
        this.repositoryService.getRepositoryCombobox().subscribe(res => {
            this.repositoryPopup = res.body;
            if (this.checkData) {
                this.ppDiscountReturnDetails.forEach(item => {
                    item.repository = this.repositoryPopup.find(n => n.id === item.repositoryID);
                });
            }
        });
        // đối tượng
        this.accountingObjectService.getAccountingObjectActive().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjects = res.body;
            if (this.checkData) {
                this.ppDiscountReturn.accountingObject = this.accountingObjects.find(
                    n => n.id === this.ppDiscountReturn.accountingObjectID
                );
                if (this.ppDiscountReturn.accountingObject) {
                    this.accountingObjectBankAccountService
                        .getByAccountingObjectById({
                            accountingObjectID: this.ppDiscountReturn.accountingObject.id
                        })
                        .subscribe(ref => {
                            this.accountingObjectBankAccountList = ref.body;
                            if (this.checkData && this.ppDiscountReturn.isBill) {
                                this.accountingObjectBankAccount = this.accountingObjectBankAccountList.find(
                                    i => i.bankAccount === this.ppDiscountReturn.accountingObjectBankAccount
                                );
                            }
                        });
                }

                this.ppDiscountReturnDetails.forEach(item => {
                    item.accountingObject = this.accountingObjects.find(n => n.id === item.accountingObjectID);
                });
            }
        });

        // lấy thông tin nhân viên thực hiện
        this.accountingObjectService.getAccountingObjectEmployee().subscribe(
            (res: HttpResponse<IAccountingObject[]>) => {
                this.employees = res.body;
                // if (this.checkData) {
                //     this.employee = this.employees.find(n => n.id === this.ppDiscountReturn.employeeID);
                // }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.goodsServicePurchaseService.getGoodServicePurchases().subscribe(ref => {
            this.goodsServicePurchase = ref.body;
            if (this.checkData) {
                this.ppDiscountReturnDetails.forEach(item => {
                    item.goodsServicePurchase = this.goodsServicePurchase.find(n => n.id === item.goodsServicePurchaseID);
                    if (item.goodsServicePurchase) {
                        this.selectGoodsServiceObjects(item);
                    }
                });
            }
        });
        // Khoản mục CP
        this.expenseItemService.getExpenseItems().subscribe(ref => {
            this.expenseItems = ref.body;
            if (this.checkData) {
                this.ppDiscountReturnDetails.forEach(item => {
                    item.expenseItem = this.expenseItems.find(n => n.id === item.expenseItemID);
                });
            }
        });
        // cost set
        this.costSetService.getCostSets().subscribe(ref => {
            this.costSets = ref.body;
            if (this.checkData) {
                this.ppDiscountReturnDetails.forEach(item => {
                    item.costSet = this.costSets.find(n => n.id === item.costSetID);
                });
            }
        });
        // cost set
        this.emContractService.getEMContracts().subscribe(ref => {
            this.emContractList = ref.body;
            if (this.checkData) {
                this.ppDiscountReturnDetails.forEach(item => {
                    item.emContract = this.emContractList.find(n => n.id === item.contractID);
                });
            }
        });
        // mục thu/ chi
        this.budgetItemService.getBudgetItems().subscribe(ref => {
            this.budgetItems = ref.body;
            if (this.checkData) {
                this.ppDiscountReturnDetails.forEach(item => {
                    item.budgetItem = this.budgetItems.find(n => n.id.toString() === item.budgetItemID);
                });
            }
        });
        // phòng ban
        this.organizationUnitService.getOrganizationUnits().subscribe(ref => {
            this.organizationUnits = ref.body;
            if (this.checkData) {
                this.ppDiscountReturnDetails.forEach(item => {
                    item.organizationUnit = this.organizationUnits.find(n => n.id === item.departmentID);
                });
            }
        });
        // mã thống kê
        this.statisticsCodeService.getStatisticsCodes().subscribe(ref => {
            this.statisticCodes = ref.body;
            if (this.checkData) {
                this.ppDiscountReturnDetails.forEach(item => {
                    item.statisticsCode = this.statisticCodes.find(n => n.id === item.statisticsCodeID);
                });
            }
        });
        this.getTypeLedger();

        // lấy loại tiền
        this.setDefaultDataFromSystemOptions();
    }

    private genCodeVoucher(currencyBook: number) {
        if (this.checkData) {
            this.noBookVoucher = this.checkBook === 1 ? this.ppDiscountReturn.noMBook : this.ppDiscountReturn.noFBook;
            if (this.ppDiscountReturn.isDeliveryVoucher) {
                this.noBookRSI = this.checkBook === 1 ? this.rsInwardOutward.noMBook : this.rsInwardOutward.noFBook;
            }
            this.option1 = this.ppDiscountReturn.isDeliveryVoucher ? true : false;
            // if (this.ppDiscountReturn.typeLedger === 0 || this.ppDiscountReturn.typeLedger === 2) {
            //     if (this.ppDiscountReturn.isDeliveryVoucher) {
            //         this.option1 = true;
            //         this.noBookRSI = this.rsInwardOutward.noFBook;
            //     }
            // } else if (this.ppDiscountReturn.typeLedger === 1) {
            //     this.noBookRSI = this.rsInwardOutward.noMBook;
            //     this.noBookVoucher = this.ppDiscountReturn.noMBook;
            // }
        } else {
            const groupTypeIDVoucher = this.statusPurchase ? GROUP_TYPEID.GROUP_PPDISCOUNTPURCHASE : GROUP_TYPEID.GROUP_PPDISCOUNTRETURN;
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: groupTypeIDVoucher,
                    displayOnBook: currencyBook
                })
                .subscribe((res: HttpResponse<string>) => {
                    this.noBookVoucher = res.body;
                });
            if (!this.statusPurchase) {
                this.utilsService
                    .getGenCodeVoucher({
                        typeGroupID: GROUP_TYPEID.GROUP_RSOUTWARD,
                        displayOnBook: currencyBook
                    })
                    .subscribe((res: HttpResponse<string>) => {
                        this.noBookRSI = res.body;
                        this.rsInwardOutward.typeID = TypeID.XUAT_KHO_TU_MUA_HANG;
                    });
            }
        }
    }

    setDefaultDataFromSystemOptions() {
        if (this.account) {
            if (!this.checkData) {
                this.ppDiscountReturn.date = this.utilsService.ngayHachToan(this.account);
                this.ppDiscountReturn.postedDate = this.ppDiscountReturn.date;
                if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                    this.ppDiscountReturn.currencyID = this.account.organizationUnit.currencyID;
                }
            }
        }
        this.getActiveCurrencies();
    }

    getActiveCurrencies() {
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencys = res.body;
            if (this.ppDiscountReturn && this.ppDiscountReturn.currencyID) {
                this.currency = this.currencys.find(item => item.currencyCode === this.ppDiscountReturn.currencyID);
                if (!this.ppDiscountReturn.exchangeRate) {
                    this.ppDiscountReturn.exchangeRate = this.currency.exchangeRate;
                }
            } else if (this.ppDiscountReturn && !this.ppDiscountReturn.currencyID) {
                this.currencyService.findActiveDefault().subscribe(ref => {
                    this.currency = ref.body;
                    this.ppDiscountReturn.exchangeRate = this.currency.exchangeRate;
                });
            }
            // this.selectCurrencyObjects();
        });
    }

    /*Xuất kho by Huypq*/
    getSessionDataRS() {
        this.rsDataSession = JSON.parse(sessionStorage.getItem('xuatKhoDataSession'));
        sessionStorage.removeItem('xuatKhoDataSession');
        if (this.rsDataSession) {
            this.isFromOutWard = true;
            this.page = this.rsDataSession.page;
            this.rsTotalItems = this.rsDataSession.totalItems;
            this.rsRowNum = this.rsDataSession.rowNum;
            this.rsSearchData = this.rsDataSession.searchVoucher;
        } else {
            this.rsDataSession = null;
        }
    }
    /*Xuất kho by Huypq*/

    move(direction: number) {
        this.isCloseAll = true;
        if (this.isFromOutWard) {
            if ((direction === -1 && this.rsRowNum === 1) || (direction === 1 && this.rsRowNum === this.rsTotalItems)) {
                return;
            }
            this.rsRowNum += direction;
            const searchData = JSON.parse(this.rsSearchData);
            return this.rsInwardOutwardService
                .findReferenceTablesID({
                    fromDate: searchData.fromDate || '',
                    toDate: searchData.toDate || '',
                    noType: searchData.noType,
                    status: searchData.status,
                    accountingObject: searchData.accountingObject || '',
                    searchValue: searchData.searchValue || '',
                    rowNum: this.rsRowNum
                })
                .subscribe(
                    (res: HttpResponse<any>) => {
                        console.log(res.body);
                        const rsInwardOutward = res.body;
                        this.rsDataSession.rowNum = this.rsRowNum;
                        // console.log(sessionStorage.getItem('xuatKhoDataSession'));
                        sessionStorage.setItem('xuatKhoDataSession', JSON.stringify(this.rsDataSession));
                        if (rsInwardOutward.typeID === this.XUAT_KHO) {
                            this.router.navigate(['/xuat-kho', rsInwardOutward.id, 'edit', this.rsRowNum]);
                        } else if (rsInwardOutward.typeID === this.XUAT_KHO_TU_BAN_HANG) {
                            this.router.navigate(['/chung-tu-ban-hang', rsInwardOutward.refID, 'edit-rs-inward-outward']);
                        } else if (rsInwardOutward.typeID === this.XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
                            this.router.navigate(['/hang-mua/tra-lai', rsInwardOutward.refID, 'edit-rs-inward-outward']);
                        } else if (rsInwardOutward.typeID === this.XUAT_KHO_TU_DIEU_CHINH) {
                        }
                    },
                    (res: HttpErrorResponse) => {
                        this.handleError(res);
                        this.getSessionData();
                    }
                );
        } else {
            if (direction === -1) {
                if (this.page === 1) {
                    if (this.rowNum === 1) {
                        return;
                    }
                } else {
                    if (this.rowNum === (this.page - 1) * this.itemsPerPage + 1) {
                        this.page = this.page - 1;
                    }
                }
            } else {
                if (this.page === this.pageCount) {
                    if (this.rowNum === this.totalItems) {
                        return;
                    }
                } else {
                    if (this.rowNum === this.page * this.itemsPerPage) {
                        this.page++;
                    }
                }
            }
            this.rowNum += direction;
            const searchDataIndex = JSON.parse(this.searchData);
            this.searchDataSearch = {
                currency: searchDataIndex.currency ? searchDataIndex.currency : '',
                fromDate: searchDataIndex.fromDate ? searchDataIndex.fromDate : '',
                toDate: searchDataIndex.toDate ? searchDataIndex.toDate : '',
                status: searchDataIndex.status ? searchDataIndex.status : '',
                accountingObject:
                    searchDataIndex.accountingObject && searchDataIndex.accountingObject.id ? searchDataIndex.accountingObject.id : '',
                searchValue: searchDataIndex.searchValue ? searchDataIndex.searchValue : ''
            };
            // goi service get by row num
            return this.pPDiscountReturnService
                .findByRowNum({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    accountingObjectID: searchDataIndex.accountingObject ? searchDataIndex.accountingObject : '',
                    fromDate: searchDataIndex.fromDate ? searchDataIndex.fromDate : '',
                    toDate: searchDataIndex.toDate ? searchDataIndex.toDate : '',
                    status: searchDataIndex.status ? searchDataIndex.status : '',
                    currencyID: searchDataIndex.currency ? searchDataIndex.currency : '',
                    employee: searchDataIndex.employee ? searchDataIndex.employee : '',
                    keySearch: searchDataIndex.searchValue ? searchDataIndex.searchValue : '',
                    id: this.ppDiscountReturn.id,
                    rowNum: this.rowNum
                })
                .subscribe(
                    (res: HttpResponse<PPDiscountReturn>) => {
                        this.ppDiscountReturn = res.body;
                        this.dataSession.page = this.page;
                        this.dataSession.rowNum = this.rowNum;
                        sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
                        if (!this.statusPurchase) {
                            this.router.navigate(['hang-mua/tra-lai', this.ppDiscountReturn.id, 'edit', this.rowNum ? this.rowNum : 0]);
                        } else {
                            this.router.navigate(['hang-mua/giam-gia', this.ppDiscountReturn.id, 'edit', this.rowNum ? this.rowNum : 0]);
                        }

                        // this.getData();
                    },
                    (res: HttpErrorResponse) => {
                        this.handleError(res);
                        this.getSessionData();
                    }
                );
        }
    }

    handleError(err) {
        this.isSaving = false;
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('giam-gia') ? ROLE.HangMuaTraLai_Them : ROLE.HangMuaGiamGia_Them])
    addNew($event) {
        $event.preventDefault();
        // this.isEdit = true;
        // this.initValues();
        // this.genCodeVoucher(this.checkBook);
        // this.checkData = false;
        if (!this.isEdit && !this.utilsService.isShowPopup) {
            this.isCloseAll = true;
            this.checkData = false;
            this.checkDataDetail = false;
            this.saBill.saBillDetails = [];
            this.saBill = {};
            this.ppDiscountReturnDetails = [];
            this.ppDiscountReturn = {};
            if (!this.statusPurchase) {
                this.router.navigate(['hang-mua/tra-lai/new']);
            } else {
                this.router.navigate(['hang-mua/giam-gia/new']);
            }
            this.ngOnInit();
            this.checkSave = false;
        }
    }

    initValues() {
        this.accountingObject = {};
        // this.employee = {};
        this.template = {};
        this.ppDiscountReturn = {};
        this.ppDiscountReturn.reason = this.translateService.instant('ebwebApp.pPDiscountReturn.home.pPDiscountReturnReason');
        this.ppDiscountReturnDetails = [];
        this.isSaving = false;
        this.saBill = {};
        this.rsInwardOutward = {};
        this.rsInwardOutward.reason = this.translateService.instant('ebwebApp.pPDiscountReturn.home.rsInwardOutwardReason');
        this.setDefaultDataFromSystemOptions();
        this.getTypeLedger();
    }

    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('giam-gia') ? ROLE.HangMuaTraLai_Sua : ROLE.HangMuaGiamGia_Sua])
    edit() {
        event.preventDefault();
        if (
            !(this.isEdit || this.ppDiscountReturn.recorded) &&
            !this.checkCloseBook(this.account, this.ppDiscountReturn.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            this.isEdit = true;
            this.checkData = false;
            this.checkDataDetail = true;
            this.isCloseAll = false;
            if (this.dataSession) {
                this.checkData = true;
                this.dataSession.isEdit = false;
                sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
            }
            if (this.rsDataSession) {
                this.checkData = true;
                this.rsDataSession.isEdit = false;
                sessionStorage.setItem('xuatKhoDataSession', JSON.stringify(this.rsDataSession));
            }
            // this.sumAllList();
            this.copy();
        }
    }

    selectTemplate() {
        if (this.template) {
            this.saBill.templateID = this.template.id;
            this.saBill.invoiceTemplate = this.template.invoiceTemplate;
            this.saBill.invoiceSeries = this.template.invoiceSeries;
            this.saBill.invoiceForm = this.template.invoiceForm;
            this.saBill.invoiceTypeID = this.template.invoiceType.id;
            if (!this.checkData) {
                this.saBill.invoiceNo = null;
                this.saBill.invoiceDate = null;
            }
        } else {
            this.saBill.templateID = null;
            this.saBill.invoiceTemplate = null;
            this.saBill.invoiceSeries = null;
            this.saBill.invoiceForm = null;
            this.saBill.invoiceNo = null;
            this.saBill.invoiceDate = null;
            this.saBill.invoiceTypeID = null;
        }
    }

    changeReason() {
        if (this.ppDiscountReturn.reason) {
            this.checkChangeReason = true;
            this.saBill.reason = this.ppDiscountReturn.reason;
            this.rsInwardOutward.reason = this.ppDiscountReturn.reason;
        }
    }

    changeobjectname() {
        if (this.ppDiscountReturn.accountingObjectName) {
            this.saBill.accountingObjectName = this.ppDiscountReturn.accountingObjectName;
            this.rsInwardOutward.accountingObjectName = this.ppDiscountReturn.accountingObjectName;
        }
    }

    changeAdress() {
        if (this.ppDiscountReturn.accountingObjectAddress) {
            this.saBill.accountingObjectAddress = this.ppDiscountReturn.accountingObjectAddress;
            this.rsInwardOutward.accountingObjectAddress = this.ppDiscountReturn.accountingObjectAddress;
        }
    }

    changeTaxCode() {
        if (this.ppDiscountReturn.companyTaxCode) {
            this.saBill.companyTaxCode = this.ppDiscountReturn.companyTaxCode;
            // this.rsInwardOutward. = this.ppDiscountReturn.accountingObjectAddress;
        }
    }

    changeContactName() {
        if (this.ppDiscountReturn.contactName) {
            this.saBill.contactName = this.ppDiscountReturn.contactName;
            this.rsInwardOutward.contactName = this.ppDiscountReturn.contactName;
        }
    }

    changeListNo() {
        if (this.ppDiscountReturn.listNo) {
            this.saBill.listNo = this.ppDiscountReturn.listNo;
        }
    }

    changeLisDate() {
        if (this.ppDiscountReturn.listDate) {
            this.saBill.listDate = this.ppDiscountReturn.listDate;
        }
    }

    changeListCommonNameInventory() {
        if (this.ppDiscountReturn.listCommonNameInventory) {
            this.saBill.listCommonNameInventory = this.ppDiscountReturn.listCommonNameInventory;
        }
    }

    // changeDate() {
    //     if (this.ppDiscountReturn.date) {
    //         this.rsInwardOutward.date = this.ppDiscountReturn.date ? moment(this.ppDiscountReturn.date) : null;
    //     }
    // }

    // changePostedDate() {
    //     this.checkPostedDateGreaterDate();
    //     if (this.ppDiscountReturn.postedDate) {
    //         this.rsInwardOutward.postedDate = this.ppDiscountReturn.postedDate ? moment(this.ppDiscountReturn.postedDate) : null;
    //     }
    // }

    private convertSaBill(saBillDTL: ISaBillDetails, item: IPPDiscountReturnDetails) {
        saBillDTL.materialGoods = item.mgForPPOderItem;
        saBillDTL.description = item.description;
        saBillDTL.debitAccount = item.debitAccount;
        saBillDTL.creditAccount = item.creditAccount;
        saBillDTL.unit = item.unit;
        saBillDTL.unitID = item.unitID;
        saBillDTL.mainUnit = item.mainUnit;
        saBillDTL.mainUnitID = item.mainUnitID;
        saBillDTL.mainUnitPrice = item.mainUnitPrice;
        saBillDTL.quantity = item.quantity;
        saBillDTL.unitPrice = item.unitPrice;
        saBillDTL.unitPriceOriginal = item.unitPriceOriginal;
        saBillDTL.mainConvertRate = item.mainConvertRate;
        saBillDTL.mainQuantity = item.mainQuantity;
        saBillDTL.formula = item.formula;
        saBillDTL.amount = item.amount;
        saBillDTL.amountOriginal = item.amountOriginal;
        saBillDTL.vatRate = item.vatRate;
        saBillDTL.vatAmountOriginal = item.vatAmountOriginal;
        saBillDTL.vatAmount = item.vatAmount;
        saBillDTL.lotNo = item.lotNo;
        saBillDTL.expiryDate = item.expiryDate;
        saBillDTL.isPromotion = item.isPromotion;
        saBillDTL.orderPriority = item.orderPriority;
    }

    convertSaBillAndRSI() {
        if (this.ppDiscountReturn && this.option1) {
            this.rsInwardOutward.exported = false;
            this.rsInwardOutward.postedDate = this.ppDiscountReturn.postedDate;
            this.rsInwardOutward.date = this.ppDiscountReturn.date;
            this.rsInwardOutward.typeLedger = this.ppDiscountReturn.typeLedger;
            this.rsInwardOutward.accountingObjectID = this.ppDiscountReturn.accountingObjectID;
            this.rsInwardOutward.accountingObjectName = this.ppDiscountReturn.accountingObjectName;
            this.rsInwardOutward.accountingObjectAddress = this.ppDiscountReturn.accountingObjectAddress;
            this.rsInwardOutward.contactName = this.ppDiscountReturn.contactName;
            this.rsInwardOutward.currencyID = this.ppDiscountReturn.currencyID;
            this.rsInwardOutward.exchangeRate = this.ppDiscountReturn.exchangeRate;
            this.rsInwardOutward.employeeID = this.ppDiscountReturn.employeeID;
            this.rsInwardOutward.transportMethodID = this.ppDiscountReturn.transportMethodID;
            this.rsInwardOutward.totalAmount = this.ppDiscountReturn.totalAmount;
            this.rsInwardOutward.totalAmountOriginal = this.ppDiscountReturn.totalAmountOriginal;
            this.rsInwardOutward.templateID = this.ppDiscountReturn.templateID;
            this.rsInwardOutward.recorded = this.ppDiscountReturn.recorded;
        }
        if (this.ppDiscountReturn && this.option2) {
            this.saBill.typeLedger = this.ppDiscountReturn.typeLedger;
            this.saBill.accountingObject = this.accountingObject && this.accountingObject.id ? this.accountingObject : null;
            this.saBill.accountingObjectName = this.ppDiscountReturn.accountingObjectName;
            this.saBill.accountingObjectAddress = this.ppDiscountReturn.accountingObjectAddress;
            this.saBill.companyTaxCode = this.ppDiscountReturn.companyTaxCode;
            this.saBill.currencyID = this.ppDiscountReturn.currencyID;
            this.saBill.exchangeRate = this.ppDiscountReturn.exchangeRate;
            this.saBill.totalAmount = this.ppDiscountReturn.totalAmount;
            this.saBill.contactName = this.ppDiscountReturn.contactName;
            this.saBill.totalAmountOriginal = this.ppDiscountReturn.totalAmountOriginal;
            // this.saBill.totalDiscountAmount = this.ppDiscountReturn.totalDiscountAmount;
            // this.saBill.totalDiscountAmountOriginal = this.ppDiscountReturn.totalDiscountAmounOriginal;
            this.saBill.totalVATAmount = this.ppDiscountReturn.totalVATAmount;
            this.saBill.totalVATAmountOriginal = this.ppDiscountReturn.totalVATAmountOriginal;
            this.saBill.isAttachList = this.ppDiscountReturn.isAttachList;
            this.saBill.listNo = this.ppDiscountReturn.listNo;
            this.saBill.listDate = moment(this.ppDiscountReturn.listDate);
            this.saBill.listCommonNameInventory = this.ppDiscountReturn.listCommonNameInventory;
            if (!this.statusPurchase) {
                this.ppDiscountReturn.invoiceDate = moment(this.saBill.invoiceDate);
                this.ppDiscountReturn.invoiceTypeID = this.template.id;
                this.ppDiscountReturn.invoiceSeries = this.saBill.invoiceSeries;
                this.ppDiscountReturn.invoiceNo = this.saBill.invoiceNo;
                this.ppDiscountReturn.invoiceTemplate = this.saBill.invoiceTemplate;
                this.ppDiscountReturn.invoiceForm = this.saBill.invoiceForm;
                this.ppDiscountReturn.paymentMethod = this.saBill.paymentMethod;
            }
            if (this.isRequiredInvoiceNo) {
                this.saBill.statusInvoice = 1;
            } else {
                this.saBill.statusInvoice = 0;
            }
        }
    }

    checkvalidate() {
        if (this.ppDiscountReturn.listDate) {
            this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.listDate'));
            return;
        }
        if (this.ppDiscountReturn.date) {
            this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.date'));
            return;
        }
    }

    changeActive(number: number) {
        this.val = number;
    }

    // check xem có check vào kiêm phiếu xuất kho hay không
    changeTab(check?: Boolean) {
        if ((!this.option1 && this.val === 2) || (!this.option2 && this.val === 3)) {
            this.val = 1;
        }
        if (this.ppDiscountReturn.id && this.option1) {
            this.pPDiscountReturnService.countFromRSI(this.ppDiscountReturn.id).subscribe(res => {
                if (res.body > 0) {
                    this.toasService.warning(
                        this.translateService.instant('ebwebApp.pPDiscountReturn.error.attachRSI'),
                        this.translateService.instant('ebwebApp.mBDeposit.message')
                    );
                    this.option1 = false;
                    return;
                }
            });
        }
        if (this.option1 && !this.noBookRSI && check) {
            if (!this.statusPurchase) {
                this.utilsService
                    .getGenCodeVoucher({
                        typeGroupID: GROUP_TYPEID.GROUP_RSOUTWARD,
                        displayOnBook: this.checkBook
                    })
                    .subscribe((res: HttpResponse<string>) => {
                        this.noBookRSI = res.body;
                        this.rsInwardOutward.typeID = TypeID.XUAT_KHO_TU_MUA_HANG;
                    });
            }
        }
    }

    public beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            if (this.isEdit) {
                this.modalRef = this.refModalService.open(this.viewVouchersSelected);
            }
        } else if ($event.nextId === 'PPInvoice') {
            $event.preventDefault();
            // this.searchPPinVoid();
            if (this.isEdit) {
                if (this.statusPurchase) {
                    this.modalRef = this.discountReturnModalService.open(
                        this.ppDiscountReturnDetails,
                        this.currency.currencyCode,
                        this.ppDiscountReturn.accountingObjectID,
                        false,
                        this.statusPurchase ? GROUP_TYPEID.GROUP_PPDISCOUNTPURCHASE : GROUP_TYPEID.GROUP_PPDISCOUNTRETURN
                    );
                } else {
                    this.modalRef = this.discountReturnModalService.open(
                        this.ppDiscountReturnDetails,
                        this.currency.currencyCode,
                        this.ppDiscountReturn.accountingObjectID,
                        true,
                        this.statusPurchase ? GROUP_TYPEID.GROUP_PPDISCOUNTPURCHASE : GROUP_TYPEID.GROUP_PPDISCOUNTRETURN
                    );
                }
            }
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
        if (select === 0) {
            this.ppDiscountReturnDetails.splice(value, 1);
        } else if (select === 1) {
            this.ppDiscountReturnDetails.splice(value, 1);
        }
        const lst = this.listIDInputDeatilTax;
        const col = this.indexFocusDetailCol;
        const row = this.ppDiscountReturnDetails.length - 1;
        setTimeout(function() {
            const element: HTMLElement = document.getElementById(lst[col] + row);
            if (element) {
                element.focus();
            }
        }, 0);
        this.checkDetailTypeObject();
    }

    // ghi so
    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('giam-gia') ? ROLE.HangMuaTraLai_GhiSo : ROLE.HangMuaGiamGia_GhiSo])
    record() {
        event.preventDefault();
        if (!(this.isEdit || this.checkCloseBook(this.account, this.ppDiscountReturn.postedDate)) && !this.utilsService.isShowPopup) {
            this.utilService
                .checkQuantityExistsTest1(this.ppDiscountReturnDetails, this.account, this.ppDiscountReturn.postedDate)
                .then(data => {
                    if (data) {
                        this.mgForPPOderTextCode = data;
                        if (!this.checkSLT && this.mgForPPOderTextCode.quantityExists && this.option1) {
                            this.toasService.error(
                                this.translateService.instant('ebwebApp.pPDiscountReturnDetails.error.quantityExistsRecordErrorSave'),
                                this.translateService.instant('ebwebApp.mBDeposit.message')
                            );
                            return;
                        }
                        if (!this.ppDiscountReturn.recorded) {
                            this.record_ = {};
                            this.record_.id = this.ppDiscountReturn.id;
                            this.record_.typeID = this.ppDiscountReturn.typeID;
                            this.gLService.record(this.record_).subscribe(
                                (res: HttpResponse<Irecord>) => {
                                    if (res.body.success) {
                                        this.ppDiscountReturn.recorded = true;
                                        this.toasService.success(
                                            this.translateService.instant('ebwebApp.mBCreditCard.recordSuccess'),
                                            this.translateService.instant('ebwebApp.mBDeposit.message')
                                        );
                                        this.materialGoodsService.queryForComboboxGood().subscribe(ref => {
                                            this.materialGoodss = ref.body;
                                        });
                                    }
                                },
                                ref => {
                                    this.toasService.error(
                                        this.translateService.instant('global.data.recordFailed'),
                                        this.translateService.instant('ebwebApp.mBDeposit.message')
                                    );
                                }
                            );
                        }
                        // else {open
                        //     this.toasService.warning(
                        //         this.translateService.instant('global.data.warningRecorded'),
                        //         this.translateService.instant('ebwebApp.mBDeposit.message')
                        //     );
                        // }
                    }
                });
        }
    }

    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('giam-gia') ? ROLE.HangMuaTraLai_GhiSo : ROLE.HangMuaGiamGia_GhiSo])
    unrecord() {
        event.preventDefault();
        if (!(this.isEdit || this.checkCloseBook(this.account, this.ppDiscountReturn.postedDate)) && !this.utilsService.isShowPopup) {
            if (this.ppDiscountReturn.recorded) {
                this.record_ = {};
                this.record_.id = this.ppDiscountReturn.id;
                this.record_.typeID = this.ppDiscountReturn.typeID;
                this.record_.repositoryLedgerID = this.ppDiscountReturn.rsInwardOutwardID;
                this.gLService.unrecord(this.record_).subscribe(
                    (res: HttpResponse<Irecord>) => {
                        // if (res.body.success) {
                        this.ppDiscountReturn.recorded = false;
                        this.toasService.success(
                            this.translateService.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                        this.materialGoodsService.queryForComboboxGood().subscribe(ref => {
                            this.materialGoodss = ref.body;
                        });
                        // }
                    },
                    ref => {
                        this.toasService.error(
                            this.translateService.instant('global.data.unRecordFailed'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                );
            }
            // else {
            //     this.toasService.warning(
            //         this.translateService.instant('global.data.warningUnRecorded'),
            //         this.translateService.instant('ebwebApp.mBDeposit.message')
            //     );
            // }
        }
    }

    setUpDataForMaskInput() {
        this.account.systemOption.forEach(item => {
            if (item.code === DDSo_NgoaiTe && item.data) {
                this.DDSo_NgoaiTe = item.data;
            } else if (item.code === DDSo_TienVND && item.data) {
                this.DDSo_TienVND = item.data;
            } else if (item.code === DDSo_TyLe && item.data) {
                this.DDSo_Tyle = item.data;
            } else if (item.code === DDSo_TyGia && item.data) {
                this.DDSO_TYGia = item.data;
            }
        });
        this.options = {
            precision: this.DDSo_Tyle
        };
        this.tyGiaOptions = {
            precision: this.DDSO_TYGia
        };
    }

    checkInvoiceNo() {
        if (!this.statusPurchase) {
            this.saBill.invoiceNo = this.utilService.pad(this.saBill.invoiceNo, 7);
        }
    }

    closeForm() {
        event.preventDefault();
        // this.selectedRow = item;
        // this.rowNum = this.getRowNumberOfRecord(this.page, this.rowNum);
        if (this.checkClose) {
            this.isCloseAll = true;
            if (this.checkData) {
                if (!this.page) {
                    this.page = 0;
                }
                if (this.dataSession) {
                    this.dataSession.page = this.page;
                    this.dataSession.itemsPerPage = this.itemsPerPage;
                    this.dataSession.pageCount = this.pageCount;
                    this.dataSession.totalItems = this.totalItems;
                    this.dataSession.rowNum = this.rowNum;
                    this.dataSession.predicate = this.predicate;
                    // this.dataSession.keySearch = this.searchData;
                    this.dataSession.reverse = this.reverse;
                    // this.dataSession.searchVoucher = this.searchData;
                    sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
                }
                if (!this.checkValidateClose()) {
                    this.modalRef = this.modalService.open(this.contentCloseModal, { backdrop: 'static' });
                } else {
                    if (this.isFromOutWard) {
                        this.backToRS();
                    } else {
                        if (!this.statusPurchase) {
                            this.router.navigate(['hang-mua/tra-lai', this.ppDiscountReturn.id, 'new', this.rowNum ? this.rowNum : 0]);
                        } else {
                            this.router.navigate(['hang-mua/giam-gia', this.ppDiscountReturn.id, 'new', this.rowNum ? this.rowNum : 0]);
                        }
                    }
                }
                // if (JSON.stringify(this.ppDiscountreturnCopy) !== JSON.stringify(this.ppDiscountReturn)) {
                //     this.modalRef = this.modalService.open(contentClose, { backdrop: 'static' });
                // } else {
                // this.router.navigate(['hang-mua/tra-lai', this.ppDiscountReturn.id, 'new', this.rowNum]);
                // }
            } else {
                if (this.isEdit && !this.checkValidateClose()) {
                    this.modalRef = this.modalService.open(this.contentCloseModal, { backdrop: 'static' });
                } else {
                    if (this.isFromOutWard) {
                        this.backToRS();
                    } else {
                        if (!this.statusPurchase) {
                            this.router.navigate(['hang-mua/tra-lai']);
                        } else {
                            this.router.navigate(['hang-mua/giam-gia']);
                        }
                    }
                }
            }
        }
    }

    registerRef() {
        this.eventSubscriber = this.eventManager.subscribe('selectViewVoucher', response => {
            if (this.isEdit) {
                this.viewVouchersSelected = response.content;
            }
        });
        this.copy();
    }

    // getRowNumberOfRecord(page: number, index: number): number {
    //     if (page > 0 && index !== -1) {
    //         return (page - 1) * this.itemsPerPage + index + 1;
    //     }
    // }
    checknoFBook() {
        if (this.noBookRSI === this.noBookVoucher && this.ppDiscountReturn.isDeliveryVoucher) {
            this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.voucherError'));
        }
    }

    /*Xuất kho by Huypq*/
    backToRS() {
        sessionStorage.removeItem('xuatKhoDataSession');
        this.router.navigate(
            ['/xuat-kho'],
            this.rsDataSession
                ? {
                      queryParams: {
                          page: this.rsDataSession.page,
                          size: this.rsDataSession.itemsPerPage,
                          sort: this.rsDataSession.predicate + ',' + (this.rsDataSession.reverse ? 'asc' : 'desc')
                      }
                  }
                : null
        );
    }
    /*Xuất kho by Huypq*/

    deleteAfter() {
        if (this.checkData) {
            if (this.typeDelete === 2) {
                this.toasService.error(this.translateService.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
            } else {
                this.pPDiscountReturnService.delete(this.ppDiscountReturn.id).subscribe(
                    ref => {
                        this.checkData = true;
                        this.checkDataDetail = true;
                        this.toasService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
                        this.checkModalRef.close();
                        this.close(true);
                    },
                    ref => {
                        this.checkData = false;
                        this.checkDataDetail = false;
                        this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.error'));
                    }
                );
            }
        }
        /*Sang từ xuất kho thì quay lại xuất kho*/
        if (this.isFromOutWard) {
            this.backToRS();
        }
        // if (this.checkData) {
        //     this.pPDiscountReturnService.delete(this.ppDiscountReturn.id).subscribe(
        //         ref => {
        //             this.checkData = true;
        //             this.checkDataDetail = true;
        //             this.toasService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
        //             this.checkModalRef.close();
        //             this.close(true);
        //         },
        //         ref => {
        //             this.checkData = false;
        //             this.checkDataDetail = false;
        //             this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.error'));
        //         }
        //     );
        // }
    }

    changeTypeLedger() {
        if (Number(this.ppDiscountReturn.typeLedger) === 2) {
            if (this.checkBook) {
                this.ppDiscountReturn.noMBook = this.noBookVoucher;
                this.rsInwardOutward.noMBook = this.noBookRSI;
                if (!this.ppDiscountReturn.id) {
                    this.rsInwardOutward.noFBook = null;
                    this.ppDiscountReturn.noFBook = null;
                }
            } else {
                this.ppDiscountReturn.noFBook = this.noBookVoucher;
                this.rsInwardOutward.noFBook = this.noBookRSI;
                if (!this.ppDiscountReturn.id) {
                    this.rsInwardOutward.noMBook = null;
                    this.ppDiscountReturn.noMBook = null;
                }
            }
        } else if (Number(this.ppDiscountReturn.typeLedger) === 1) {
            this.ppDiscountReturn.noFBook = null;
            this.ppDiscountReturn.noMBook = this.noBookVoucher;
            this.rsInwardOutward.noFBook = null;
            this.rsInwardOutward.noMBook = this.noBookRSI;
        } else if (Number(this.ppDiscountReturn.typeLedger) === 0) {
            this.ppDiscountReturn.noFBook = this.noBookVoucher;
            this.ppDiscountReturn.noMBook = null;
            this.rsInwardOutward.noFBook = this.noBookRSI;
            this.rsInwardOutward.noMBook = null;
        }
    }

    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('giam-gia') ? ROLE.HangMuaTraLai_Them : ROLE.HangMuaGiamGia_Them])
    saveAndNew() {
        event.preventDefault();
        this.checkSave = true;
        this.save(true);
    }

    copyAndNew() {
        event.preventDefault();
        if (!(this.isEdit || (!this.ppDiscountReturn.id && this.ppDiscountReturn.recorded))) {
            this.ppDiscountReturn.id = null;
            this.ppDiscountReturn.recorded = false;
            if (this.ppDiscountReturnDetails && this.ppDiscountReturnDetails.length > 0) {
                this.ppDiscountReturnDetails.forEach(item => {
                    item.id = null;
                    item.ppInvoiceDetailID = null;
                    item.ppInvoiceID = null;
                    item.saBillID = null;
                    item.saBillDetailID = null;
                });
            }
            this.saBill.saBillDetails = [];
            if (this.option2) {
                this.saBill.invoiceNo = null;
                this.saBill.iDAdjustInv = null;
                this.saBill.iDReplaceInv = null;
                this.saBill.statusInvoice = 0;
            }
            this.saBill.id = null;
            this.rsInwardOutward.id = null;
            this.isEdit = true;
            this.checkData = false;
            this.checkDataDetail = false;

            this.genCodeVoucher(this.checkBook);
            this.toasService.success(
                this.translateService.instant('ebwebApp.pPDiscountReturn.copySuccessful'),
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
        }
    }

    checkPostedDateGreaterDate() {
        if (this.ppDiscountReturn.postedDate && this.ppDiscountReturn.date) {
            if (moment(this.ppDiscountReturn.postedDate) < moment(this.ppDiscountReturn.date)) {
                this.toasService.error(this.translateService.instant('ebwebApp.common.error.dateAndPostDate'));
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    changeQuantity(detail: PPDiscountReturnDetails) {
        if (
            detail.quantity !== null &&
            detail.quantity !== undefined &&
            detail.unitPriceOriginal !== null &&
            detail.unitPriceOriginal !== undefined
        ) {
            detail.amountOriginal = detail.quantity * detail.unitPriceOriginal;
        }
        this.changeAmount(detail);
        this.changeVatRate(detail);
        // detail.mainConvertRate = detail.unit.convertRate ? detail.unit.convertRate : 1;
        if (detail.formula && detail.mainConvertRate) {
            detail.mainQuantity =
                detail.formula === '*' ? detail.quantity * detail.mainConvertRate : detail.quantity / detail.mainConvertRate;
            detail.mainUnitPrice =
                detail.formula === '*'
                    ? detail.unitPriceOriginal / detail.mainConvertRate
                    : detail.unitPriceOriginal * detail.mainConvertRate;
        }
        // this.selectUnit(detail);
        this.sumAllList();
    }

    selectPPinVoid() {
        this.ppDiscountReturnDetails = [];
        let ppDiscountDetail1: PPDiscountReturnDetails;
        let dem = 0;
        this.ppInvoiceDetailsConverts.forEach(object => {
            if (object.ppInvoiceID === this.ppInvoiceDetailsConverts[0].ppInvoiceID) {
                dem++;
            }
            ppDiscountDetail1 = {};
            if (object.ppInvoiceID && !this.checkRefID.get(object.ppInvoiceID)) {
                this.viewVouchersSelected.push({});
                const index = this.viewVouchersSelected.length - 1;
                this.viewVouchersSelected[index].date = object.date;
                this.viewVouchersSelected[index].postedDate = object.postedDate;
                this.viewVouchersSelected[index].no = object.noMBook;
                this.viewVouchersSelected[index].checkRef = true;
                this.viewVouchersSelected[index].reason = object.reason;
                this.viewVouchersSelected[index].refID2 = object.ppInvoiceID;
                this.viewVouchersSelected[index].typeGroupID = object.typeGroupID;
                this.checkRefID.set(object.ppInvoiceID, object.ppInvoiceID);
            }
            this.convertItemSelect(object, ppDiscountDetail1, true);
            this.ppDiscountReturnDetails.push(ppDiscountDetail1);
            this.getDataPPdiscountReturnDetail(ppDiscountDetail1);
            // this.changeQuantity(object);
            this.changeUnitPriceOriginal(ppDiscountDetail1);
        });
        if (dem === this.ppInvoiceDetailsConverts.length && !this.ppDiscountReturn.id) {
            if (this.ppInvoiceDetailsConverts.length > 0) {
                this.ppInvoiceService.findPPInvoiceById({ id: this.ppInvoiceDetailsConverts[0].ppInvoiceID }).subscribe(res => {
                    if (res.body.id) {
                        res.body.id = null;
                    }
                    this.ppDiscountReturn.accountingObjectID = res.body.accountingObjectId;
                    this.ppDiscountReturn.accountingObjectName = res.body.accountingObjectName;
                    this.ppDiscountReturn.accountingObjectAddress = res.body.accountingObjectAddress;
                    this.ppDiscountReturn.companyTaxCode = res.body.companyTaxCode;
                    this.ppDiscountReturn.contactName = res.body.contactName;
                    this.ppDiscountReturn.isAttachList = res.body.isAttachList;
                    this.ppDiscountReturn.listNo = res.body.listNo;
                    this.ppDiscountReturn.listCommonNameInventory = res.body.listCommonNameInventory;
                    this.ppDiscountReturn.employeeID = res.body.employeeId;
                    // this.ppDiscountReturn.reason = reason;
                    this.ppDiscountReturn.accountingObject = this.accountingObjects.find(n => n.id === res.body.accountingObjectId);
                    // this.employee = this.employees.find(n => n.id === res.body.employeeId);
                });
            }
        }
        this.sumAllList();
    }

    convertItemSelect(item, ppDiscountReturnDetailItem, boolean: Boolean) {
        Object.assign(ppDiscountReturnDetailItem, item);
        // Phòng ban
        if (item.departmentId) {
            ppDiscountReturnDetailItem.organizationUnit = this.organizationUnits.find(n => n.id === item.departmentId);
        }
        if (item.statisticCodeID) {
            ppDiscountReturnDetailItem.statisticsCode = this.statisticCodes.find(n => n.id === item.statisticCodeID);
        }
        if (item.vatDescription) {
            ppDiscountReturnDetailItem.vatDescription = item.vatDescription;
        }
        if (item.id) {
            ppDiscountReturnDetailItem.saBillDetailID = item.id;
        }
        ppDiscountReturnDetailItem.ppInvoiceQuantity = item.quantity ? item.quantity : 0;

        if (item.ppInvoiceDetailID) {
            ppDiscountReturnDetailItem.ppInvoiceDetailID = item.ppInvoiceDetailID;
        }
        if (!this.statusPurchase) {
            if (item.quantityRollBack) {
                ppDiscountReturnDetailItem.quantity = item.quantityRollBack;
            }
        } else {
            if (item.quantity) {
                ppDiscountReturnDetailItem.quantity = item.quantity;
            }
        }
        if (item.quantity) {
            item.amountOriginal = item.amountOriginal ? item.amountOriginal : 0;
            item.discountAccount = item.discountAccount ? item.discountAccount : 0;
            ppDiscountReturnDetailItem.unitPriceOriginal = (item.amountOriginal - item.discountAccount) / item.quantity;
        } else {
            ppDiscountReturnDetailItem.unitPriceOriginal = 0;
        }
        if (boolean) {
            if (item.creditAccount && item.checked) {
                ppDiscountReturnDetailItem.debitAccount = item.creditAccount;
            } else if (!item.checked) {
                ppDiscountReturnDetailItem.debitAccount = item.debitAccount;
            }
            if (item.debitAccount && item.checked) {
                ppDiscountReturnDetailItem.creditAccount = item.debitAccount;
            } else if (!item.checked) {
                ppDiscountReturnDetailItem.creditAccount = item.creditAccount;
            }
        } else {
            if (item.debitAccount) {
                ppDiscountReturnDetailItem.debitAccount = item.debitAccount;
            } else if (item.debitAccountItem) {
                ppDiscountReturnDetailItem.debitAccount = item.debitAccountItem.accountNumber;
            }
            if (item.creditAccount) {
                ppDiscountReturnDetailItem.creditAccount = item.creditAccount;
            } else if (item.creditAccountItem) {
                ppDiscountReturnDetailItem.creditAccount = item.creditAccountItem.accountNumber;
            }
        }
        if (!this.checkDataDetail) {
            ppDiscountReturnDetailItem.id = null;
        }
        if (item.units) {
            ppDiscountReturnDetailItem.units = item.units;
            if (item.unitID) {
                ppDiscountReturnDetailItem.unit = item.units.find(n => n.id === item.unitID);
            }
            if (item.mainUnitID) {
                const mainUnitItem = item.units.find(i => i.id === item.mainUnitID);
                if (mainUnitItem) {
                    ppDiscountReturnDetailItem.mainUnitID = mainUnitItem.id;
                    ppDiscountReturnDetailItem.mainUnitName = mainUnitItem.unitName;
                }
            }
        }
        if (item.date) {
            ppDiscountReturnDetailItem.ppInVoiceDate = item.date;
        }
        if (item.noMBook) {
            ppDiscountReturnDetailItem.ppInVoiceNoBook = item.noMBook;
        }
        if (item.unitPriceOriginal) {
            ppDiscountReturnDetailItem.unitPriceOriginal = item.unitPriceOriginal;
        }
    }

    getDataPPdiscountReturnDetail(item: PPDiscountReturnDetails) {
        if (item.materialGoodsID) {
            item.mgForPPOderItem = this.materialGoodss.find(n => n.id === item.materialGoodsID);
        }
        if (item.repositoryID) {
            item.repository = this.repositoryPopup.find(n => n.id === item.repositoryID);
        }
        if (item.debitAccount) {
            const debitAccountItem = this.debitAccountList.find(n => n.accountNumber === item.debitAccount);
            if (debitAccountItem) {
                item.debitAccountItem = debitAccountItem;
            } else {
                item.debitAccount = null;
            }
        } else {
            item.debitAccount = null;
        }
        if (item.creditAccount) {
            const creditAccountItem = this.creditAccountList.find(n => n.accountNumber === item.creditAccount);
            if (creditAccountItem) {
                item.creditAccountItem = creditAccountItem;
            } else {
                item.creditAccount = null;
            }
        } else {
            item.creditAccount = null;
        }
        if (item.accountingObjectID) {
            item.accountingObject = this.accountingObjects.find(n => n.id === item.accountingObjectID);
        }
        if (item.departmentID) {
            item.organizationUnit = this.organizationUnits.find(n => n.id === item.departmentID);
        }
        if (item.vatAccount) {
            item.vatAccountItem = this.vatAccountList.find(n => n.accountNumber === item.vatAccount);
        }
        if (item.expenseItemID) {
            item.expenseItem = this.expenseItems.find(n => n.id === item.expenseItemID);
        }
        if (item.costSetID) {
            item.costSet = this.costSetList.find(n => n.id === item.costSetID);
        }
        if (item.contractID) {
            item.emContract = this.emContractList.find(n => n.id === item.contractID);
        }
        if (item.budgetItemID) {
            item.budgetItem = this.budgetItems.find(n => n.id.toString() === item.budgetItemID);
        }
        if (item.statisticsCodeID) {
            item.statisticsCode = this.statisticCodes.find(n => n.id === item.statisticsCodeID);
        }
        if (item.deductionDebitAccount) {
            item.deductionDebitAccountItem = this.deductionDebitAccountList.find(n => n.accountNumber === item.deductionDebitAccount);
        }
        if (item.goodsServicePurchaseID) {
            item.goodsServicePurchase = this.goodsServicePurchase.find(n => n.id === item.goodsServicePurchaseID);
        }
        if (item.goodsServicePurchase) {
            this.selectGoodsServiceObjects(item);
        }
    }

    selectSabill() {
        this.ppDiscountReturnDetails = [];
        let ppDiscountDetail: PPDiscountReturnDetails;
        this.saBillDetail.forEach(object => {
            ppDiscountDetail = {};
            if (object.saBillID && !this.checkRefID.get(object.saBillID)) {
                this.viewVouchersSelected.push({});
                const index = this.viewVouchersSelected.length - 1;
                this.viewVouchersSelected[index].date = object.invoiceDate.format(DATE_FORMAT);
                // this.viewVouchersSelected[index].postedDate = object.postedDate;
                this.viewVouchersSelected[index].no = object.invoiceNo;
                this.viewVouchersSelected[index].checkRefSa = true;
                this.viewVouchersSelected[index].reason = object.description;
                this.viewVouchersSelected[index].refID2 = object.saBillID;
                this.viewVouchersSelected[index].typeGroupID = object.typeGroupID;
                this.checkRefID.set(object.saBillID, object.saBillID);
            }
            this.convertItemSelect(object, ppDiscountDetail, false);
            this.ppDiscountReturnDetails.push(ppDiscountDetail);
            this.getDataPPdiscountReturnDetail(ppDiscountDetail);
            this.changeUnitPriceOriginal(ppDiscountDetail);
        });
        this.sumAllList();
    }

    selectedItemPerPage(boolean: Boolean) {
        if (boolean) {
            this.pageVoucher = 1;
        } else {
            this.pagePPinvoice = 1;
        }
    }

    // loadPage(page: number) {
    //     if (page !== this.previousPageVoucher) {
    //         this.pageVoucher = page;
    //         this.transition();
    //     }
    // }
    // transition() {
    //     this.searchDataSabill();
    // }
    // ROLE_Them1: ROLE.;

    closeModalPPinvoice() {
        this.modalRef.close();
    }

    copy() {
        this.ppDiscountreturnCopy = Object.assign({}, this.ppDiscountReturn);
        this.ppDiscountReturnDetailsCopy = this.ppDiscountReturnDetails.map(object => ({ ...object }));
        if (this.ppDiscountReturn.isDeliveryVoucher) {
            this.rsInwardOutwardCopy = Object.assign({}, this.rsInwardOutward);
        }
        if (this.ppDiscountReturn.isBill) {
            this.saBillCopy = Object.assign({}, this.saBill);
            // this.saBillCopy.saBillDetails = this.saBill.saBillDetails.map(object => ({ ...object }));
        }
    }

    close(statusDelete?: Boolean) {
        this.isCloseAll = true;
        if (this.isFromOutWard) {
            /*Xuất kho by huypq*/
            this.backToRS();
        } else {
            if (this.checkData) {
                this.dataSession.page = this.page;
                this.dataSession.itemsPerPage = this.itemsPerPage;
                this.dataSession.pageCount = this.pageCount;
                this.dataSession.totalItems = this.totalItems;
                this.dataSession.rowNum = this.rowNum;
                this.dataSession.predicate = this.predicate;
                // this.dataSession.keySearch = this.searchData;
                this.dataSession.reverse = this.reverse;
                if (statusDelete) {
                    this.dataSession.statusDelete = true;
                } else {
                    this.dataSession.statusDelete = false;
                }
                // this.dataSession.searchVoucher = this.searchData;
                sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
                if (!statusDelete) {
                    if (!this.statusPurchase) {
                        this.router.navigate(['hang-mua/tra-lai', this.ppDiscountReturn.id, 'new', this.rowNum ? this.rowNum : 0]);
                    } else {
                        this.router.navigate(['hang-mua/giam-gia', this.ppDiscountReturn.id, 'new', this.rowNum ? this.rowNum : 0]);
                    }
                }
                if (statusDelete) {
                    if (!this.statusPurchase) {
                        this.router.navigate(['hang-mua/tra-lai']);
                    } else {
                        this.router.navigate(['hang-mua/giam-gia']);
                    }
                }
            } else {
                if (!this.statusPurchase) {
                    this.router.navigate(['hang-mua/tra-lai']);
                } else {
                    this.router.navigate(['hang-mua/giam-gia']);
                }
            }
            if (this.modalRef) {
                this.modalRef.close();
            }
        }
        this.checkSave = false;
    }

    private getTypeLedger() {
        // lấy quyền phaan loại sổ
        this.genCodeVoucher(this.checkBook);
        // if (!this.checkData && this.sessionWork) {
        //     this.ppDiscountReturn.typeLedger = 2;
        // }
        // if (!this.authoritiesNoMBook) {
        //     this.ppDiscountReturn.typeLedger = this.checkBook;
        // } else {
        //     this.ppDiscountReturn.typeLedger = 2;
        // }
        // this.copy();
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    checkValidateClose() {
        if (!this.isEdit) {
            return true;
        }
        if (
            !this.ppDiscountreturnCopy ||
            (this.utilsService.isEquivalent(this.ppDiscountReturn, this.ppDiscountreturnCopy) &&
                this.utilsService.isEquivalentArray(this.ppDiscountReturnDetails, this.ppDiscountReturnDetailsCopy))
        ) {
            if (this.ppDiscountReturn.isBill) {
                if (!(!this.saBill || this.utilsService.isEquivalent(this.saBill, this.saBillCopy))) {
                    return false;
                }
            }
            if (this.ppDiscountReturn.isDeliveryVoucher) {
                if (!(this.rsInwardOutward || this.utilsService.isEquivalent(this.rsInwardOutward, this.rsInwardOutwardCopy))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private checkDetailTypeObject() {
        if (this.ppDiscountReturnDetails && this.ppDiscountReturnDetails.length > 0) {
            if (this.creditAccountDefault) {
                this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].creditAccountItem = this.creditAccountList.find(
                    x => x.accountNumber === this.creditAccountDefault
                );
            }
            if (this.debitAccountDefault) {
                this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].debitAccountItem = this.debitAccountList.find(
                    x => x.accountNumber === this.debitAccountDefault
                );
            }
            if (this.vatAccountDefault) {
                this.ppDiscountReturnDetails[this.ppDiscountReturnDetails.length - 1].vatAccountItem = this.vatAccountList.find(
                    x => x.accountNumber === this.vatAccountDefault
                );
            }
            if (this.deductionDebitAccountDefault) {
                this.ppDiscountReturnDetails[
                    this.ppDiscountReturnDetails.length - 1
                ].deductionDebitAccountItem = this.deductionDebitAccountList.find(
                    x => x.accountNumber === this.deductionDebitAccountDefault
                );
            }
            if (this.ppDiscountReturnDetails.length > 0) {
                const detailType1 = this.ppDiscountReturnDetails.filter(
                    item =>
                        (item.creditAccountItem &&
                            item.creditAccountItem.detailType !== null &&
                            item.creditAccountItem.detailType !== undefined &&
                            item.creditAccountItem.detailType === '0') ||
                        (item.debitAccountItem &&
                            item.debitAccountItem.detailType !== null &&
                            item.debitAccountItem.detailType !== undefined &&
                            item.debitAccountItem.detailType === '0')
                );
                if (detailType1 && detailType1.length && !this.accountingObject) {
                    this.checkDetailType = true;
                } else {
                    this.checkDetailType = false;
                }
            }
        }
        this.sumAllList();
    }

    afterDeleteOrAddRow() {
        this.eventManager.subscribe('afterDeleteRow', ref => {
            const lst = this.listIDInputDeatilTax;
            const col = this.indexFocusDetailCol;
            const row = this.indexFocusDetailRow - 1;
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(lst[col] + row);
                if (element) {
                    element.focus();
                }
            }, 0);
            // console.log(ref);
            const countItem = this.ppDiscountReturnDetails.filter(x => x.ppInvoiceID === ref.content.ppInvoiceID).length;
            const countItem1 = this.ppDiscountReturnDetails.filter(x => x.saBillID === ref.content.saBillID).length;
            if (countItem <= 0 || countItem1 <= 0) {
                for (let i = 0; i < this.viewVouchersSelected.length; i++) {
                    if (this.viewVouchersSelected[i].refID2 === ref.content.ppInvoiceID) {
                        this.viewVouchersSelected.splice(i, 1);
                        this.checkRefID.delete(ref.content.ppInvoiceID);
                        i--;
                    } else if (this.viewVouchersSelected[i].refID2 === ref.content.saBillID) {
                        this.viewVouchersSelected.splice(i, 1);
                        this.checkRefID.delete(ref.content.saBillID);
                        i--;
                    }
                }
            }
            this.checkDetailTypeObject();
        });
        this.eventManager.subscribe('afterAddNewRow', () => {
            const lst = this.listIDInputDeatilTax;
            const col = this.indexFocusDetailCol;
            const row = this.indexFocusDetailRow + 1;
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(lst[col] + row);
                if (element) {
                    element.focus();
                }
            }, 0);
            this.checkDetailTypeObject();
        });
        this.eventManager.subscribe('afterCopyRow', () => {
            const lst = this.listIDInputDeatilTax;
            const col = this.indexFocusDetailCol;
            const row = this.indexFocusDetailRow + 1;
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(lst[col] + row);
                if (element) {
                    element.focus();
                }
            }, 0);
            this.checkDetailTypeObject();
        });
        this.eventManager.subscribe('selectViewInvoice', ref => {
            this.saBillDetail = [];
            this.saBillDetail = ref.content;
            for (let i = 0; i < this.viewVouchersSelected.length; i++) {
                if (this.viewVouchersSelected[i] && this.viewVouchersSelected[i].checkRefSa) {
                    this.viewVouchersSelected.splice(i, 1);
                    i--;
                }
            }
            if (!this.checkRefID) {
                this.checkRefID = new Map();
            }
            this.selectSabill();
        });
        this.eventManager.subscribe('selectDiscountReturn', ref => {
            this.ppInvoiceDetailsConverts = [];
            this.ppInvoiceDetailsConverts = ref.content;
            if (this.viewVouchersSelected && this.viewVouchersSelected.length > 0) {
                for (let i = 0; i < this.viewVouchersSelected.length; i++) {
                    if (this.viewVouchersSelected[i] && this.viewVouchersSelected[i].checkRef) {
                        this.viewVouchersSelected.splice(i, 1);
                        i--;
                    }
                }
            } else {
                this.viewVouchersSelected = [];
            }
            for (let i = 0; i < this.viewVouchersSelected.length; i++) {
                if (this.viewVouchersSelected[i] && this.viewVouchersSelected[i].checkRef) {
                    this.viewVouchersSelected.splice(i, 1);
                    i--;
                }
            }
            if (!this.checkRefID) {
                this.checkRefID = new Map();
            }
            this.selectPPinVoid();
        });
    }

    changeUnitPriceOriginal(detail: IPPDiscountReturnDetails) {
        // đơn giá quy đổi
        detail.unitPriceOriginal = this.utilsService.round(
            detail.unitPriceOriginal,
            this.account.systemOption,
            this.typeAmountOriginal ? 1 : 2
        );
        if (this.currency.formula === '/') {
            detail.unitPrice =
                detail.unitPriceOriginal && this.ppDiscountReturn.exchangeRate
                    ? detail.unitPriceOriginal / this.ppDiscountReturn.exchangeRate
                    : 0;
        } else {
            detail.unitPrice =
                detail.unitPriceOriginal && this.ppDiscountReturn.exchangeRate
                    ? detail.unitPriceOriginal * this.ppDiscountReturn.exchangeRate
                    : 0;
        }
        if (detail.quantity && detail.unitPriceOriginal) {
            detail.amountOriginal = detail.quantity * detail.unitPriceOriginal;
        }
        if (detail.unit && detail.unit.formula) {
            detail.formula = detail.unit.formula;
        }
        detail.mainQuantity = detail.formula === '*' ? detail.quantity * detail.mainConvertRate : detail.quantity / detail.mainConvertRate;
        detail.mainUnitPrice =
            detail.formula === '*' ? detail.unitPriceOriginal / detail.mainConvertRate : detail.unitPriceOriginal * detail.mainConvertRate;
        this.changeQuantity(detail);
        // this.changeVatRate(detail);
        // this.sumAllList();
    }

    changeExchangeRate() {
        this.sumAllList(true);
    }

    canDeactive() {
        if (!this.isCloseAll) {
            return this.checkValidateClose();
        } else {
            return true;
        }
    }

    closes() {
        this.isCloseAll = false;
        this.modalRef.close();
    }
    getCurrentDate(): { year; month; day } {
        const _date = moment();
        return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
    }

    getFromToStr(date?: string, isMaxDate?: boolean): { year; month; day } {
        const _date = date && moment(date, 'YYYYMMDD').isValid() ? moment(date, 'YYYYMMDD') : isMaxDate ? null : moment();
        return _date ? { year: _date.year(), month: _date.month() + 1, day: _date.date() } : null;
    }

    changeTemplate(template: boolean) {
        this.templateDVT = template;
    }

    changeMainQuantity(detail: IPPDiscountReturnDetails) {
        if (detail.formula && !this.statusPurchase) {
            // tính lại đơn giá
            if (detail.formula === '/' && detail.mainConvertRate) {
                detail.quantity = detail.mainQuantity * detail.mainConvertRate;
            } else if (detail.formula === '*' && detail.mainConvertRate) {
                detail.quantity = detail.mainQuantity / detail.mainConvertRate;
            }
            // tính lại thành tiền
            if (detail.quantity && detail.unitPriceOriginal) {
                detail.amountOriginal = detail.quantity * detail.unitPriceOriginal;
            }
            this.changeAmount(detail);
            this.changeVatRate(detail);
        }
    }

    changeMainUnitPrice(detail: IPPDiscountReturnDetails) {
        if (detail.formula && !this.statusPurchase) {
            // tính lại đơn giá
            if (detail.formula === '/' && detail.mainConvertRate) {
                detail.unitPriceOriginal = detail.mainUnitPrice / detail.mainConvertRate;
            } else if (detail.formula === '*' && detail.mainConvertRate) {
                detail.unitPriceOriginal = detail.mainUnitPrice * detail.mainConvertRate;
            }
            detail.unitPrice =
                this.currency.formula === '/'
                    ? detail.unitPriceOriginal / this.ppDiscountReturn.exchangeRate
                    : detail.unitPriceOriginal * this.ppDiscountReturn.exchangeRate;
            // tính lại thành tiền
            if (detail.quantity && detail.unitPriceOriginal) {
                detail.amountOriginal = detail.quantity * detail.unitPriceOriginal;
            }
            this.changeAmount(detail);
            this.changeVatRate(detail);
        }
    }

    @ebAuth([
        'ROLE_ADMIN',
        !window.location.href.includes('giam-gia') ? ROLE.HangMuaTraLai_Sua : ROLE.HangMuaGiamGia_Sua,
        !window.location.href.includes('giam-gia') ? ROLE.HangMuaTraLai_Them : ROLE.HangMuaGiamGia_Them
    ])
    save(check?: Boolean) {
        event.preventDefault();
        if (this.isEdit && !this.utilsService.isShowPopup) {
            if (this.validatePurchaseGiveBack()) {
                this.checkOpenModal = new Map();
                // this.mgForPPOderTextCode = this.utilService.checkQuantityExistsTest1(this.ppDiscountReturnDetails, this.ppDiscountReturn.postedDate);
                this.utilService
                    .checkQuantityExistsTest1(this.ppDiscountReturnDetails, this.account, this.ppDiscountReturn.postedDate)
                    .then(data => {
                        if (data) {
                            this.mgForPPOderTextCode = data;
                            if (!this.checkSLT && this.mgForPPOderTextCode && this.mgForPPOderTextCode.quantityExists && this.option1) {
                                this.recorded = false;
                                this.checkOpenSave = true;
                                if (this.modalRef) {
                                    this.modalRef.close();
                                }
                                this.modalRef = this.modalService.open(this.contentSaveModal, { backdrop: 'static' });
                                return;
                            }
                            if (this.mgForPPOderTextCode && this.mgForPPOderTextCode.minimumStock && this.option1) {
                                this.checkOpenSave = false;
                                if (this.modalRef) {
                                    this.modalRef.close();
                                }
                                this.modalRef = this.modalService.open(this.contentSaveModal, { backdrop: 'static' });
                                return;
                            }
                            for (let i = 0; i < this.ppDiscountReturnDetails.length; i++) {
                                this.ppDiscountReturnDetails[i].orderPriority = i;
                                // if (this.ppDiscountReturnDetails[i].unit && this.ppDiscountReturnDetails[i].unit.id) {
                                //     this.ppDiscountReturnDetails[i].unit = null;
                                // }
                                if (this.ppDiscountReturnDetails[i].vatAmountOriginal && this.ppDiscountReturnDetails[i].vatRate === 0) {
                                    this.checkVATError = false;
                                    if (this.modalRef) {
                                        this.modalRef.close();
                                    }
                                    this.modalRef = this.modalService.open(this.contentVATModal, { backdrop: 'static' });
                                    this.checkOpenModal.set(1, 1);
                                    return;
                                }
                                if (
                                    this.ppDiscountReturnDetails[i].vatAmountOriginal &&
                                    (this.ppDiscountReturnDetails[i].vatRate === null ||
                                        this.ppDiscountReturnDetails[i].vatRate === undefined)
                                ) {
                                    this.checkVATError = true;
                                    this.checkOpenModal.set(2, 2);
                                    if (this.modalRef) {
                                        this.modalRef.close();
                                    }
                                    this.modalRef = this.modalService.open(this.contentVATModal, { backdrop: 'static' });
                                    return;
                                }
                            }
                            this.recorded = true;
                            const minusError = this.ppDiscountReturnDetails.filter(item => item.ppInvoiceQuantity < item.quantity);
                            if (minusError && minusError.length) {
                                this.checkOpenModal.set(3, 3);
                                if (this.modalRef) {
                                    this.modalRef.close();
                                }
                                this.modalRef = this.modalService.open(this.contentMinus, { backdrop: 'static' });
                                return;
                            } else {
                                this.saveAfter(check);
                            }
                        }
                    });
            } else {
                this.isEdit = true;
            }
        }
    }
    // checkQuantityExists(mgForPPOder: any[], details: any[]) {
    //     const mgForPPOderMap = new Map();
    //     let textCode: any = '';
    //     for (let i = 0; i < details.length; i++) {
    //         const item = mgForPPOder.find(
    //             x =>
    //                 x.id === details[i].materialGoodsID && x.materialGoodsInStock < 0);
    //         if (item) {
    //             if (mgForPPOderMap.size === 0 || mgForPPOderMap.get(item.materialGoodsCode) !== item.materialGoodsCode) {
    //                 mgForPPOderMap.set(item.materialGoodsCode, item.materialGoodsCode);
    //                 textCode += item.materialGoodsCode + ', ';
    //             }
    //         }
    //     }
    //     return textCode.slice(0, textCode.length - 2);
    // }
    checkOption2() {
        if (this.option2) {
            if (this.ppDiscountReturn.isBill && this.saBill.invoiceForm === 0 && this.isTichHopHDDT) {
                if (this.saBill.invoiceNo) {
                    this.option2 = true;
                    this.typeDelete = 2;
                    this.toasService.error(
                        this.translateService.instant('ebwebApp.pPDiscountReturn.error.optionSaBill'),
                        this.translateService.instant('ebwebApp.pPDiscountReturn.error.optionSaBillTitle')
                    );
                    return false;
                } else {
                    this.typeDelete = 0;
                }
            } else if (this.ppDiscountReturn.isBill) {
                this.typeDelete = 1;
            } else {
                this.typeDelete = 0;
            }
            return true;
        }
        return true;
    }

    changeStatusReason() {
        this.checkChangeReason = true;
    }

    CTKTExportPDF(isDownload, typeReports: any) {
        if (!this.isCreateUrl) {
            this.pPDiscountReturnService
                .getCustomerReport({
                    id: this.ppDiscountReturn.id,
                    typeID: this.statusPurchase ? TypeID.MUA_HANG_GIAM_GIA : TypeID.MUA_HANG_TRA_LAI,
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
                        const name = 'MUA_HANG_TRA_LAI.pdf';
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
            if (typeReports === REPORT.ChungTuKeToan) {
                this.toasService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.mBDeposit.financialPaper') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
            } else if (typeReports === REPORT.ChungTuKeToanQuyDoi) {
                this.toasService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.mBDeposit.financialPaperQD') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
            } else if (typeReports === REPORT.PhieuThu) {
                this.toasService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.mCReceipt.print.mCReceipt') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
            } else if (typeReports === REPORT.GiayBaoCo) {
                this.toasService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.mBDeposit.creditNote') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
            } else if (typeReports === REPORT.PhieuXuatKho) {
                this.toasService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.outWard.billOutWrad') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
            }
            if (typeReports === 10) {
                this.printMessages = [];
                this.translateService
                    .get(['ebwebApp.mBDeposit.printing', 'ebwebApp.sAInvoice.vatInvoice', 'ebwebApp.mBDeposit.message'])
                    .subscribe(res => {
                        this.toasService.success(
                            res['ebwebApp.mBDeposit.printing'] + res['ebwebApp.sAInvoice.vatInvoice'],
                            res['ebwebApp.mBDeposit.message']
                        );
                    });
            } else if (typeReports === 11) {
                this.printMessages = [];
                this.translateService
                    .get(['ebwebApp.mBDeposit.printing', 'ebwebApp.sAInvoice.saleInvoice', 'ebwebApp.mBDeposit.message'])
                    .subscribe(res => {
                        this.toasService.success(
                            res['ebwebApp.mBDeposit.printing'] + res['ebwebApp.sAInvoice.saleInvoice'],
                            res['ebwebApp.mBDeposit.message']
                        );
                    });
            }
        }
    }

    openModelSave() {
        if (!this.checkOpenModal || !this.checkOpenModal.get(3, 3)) {
            const minusError = this.ppDiscountReturnDetails.filter(item => item.ppInvoiceQuantity < item.quantity);
            if (minusError && minusError.length) {
                this.checkOpenModal.set(3, 3);
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(this.contentMinus, { backdrop: 'static' });
                return;
            }
        }
        if (!this.checkOpenModal || (!this.checkOpenModal.get(1, 1) && !this.checkOpenModal.get(2, 2))) {
            for (let i = 0; i < this.ppDiscountReturnDetails.length; i++) {
                // if (!this.ppDiscountReturnDetails[i].unit && !this.ppDiscountReturnDetails[i].unit.id) {
                //     this.ppDiscountReturnDetails[i].unit = null;
                // }
                if (!this.checkOpenModal || !this.checkOpenModal.get(1, 1)) {
                    if (this.ppDiscountReturnDetails[i].vatAmountOriginal && this.ppDiscountReturnDetails[i].vatRate === 0) {
                        this.checkVATError = false;
                        if (this.modalRef) {
                            this.modalRef.close();
                        }
                        this.modalRef = this.modalService.open(this.contentVATModal, { backdrop: 'static' });
                        this.checkOpenModal.set(1, 1);
                        return;
                    }
                }
                if (!this.checkOpenModal || !this.checkOpenModal.get(2, 2)) {
                    if (
                        this.ppDiscountReturnDetails[i].vatAmountOriginal &&
                        (this.ppDiscountReturnDetails[i].vatRate === null || this.ppDiscountReturnDetails[i].vatRate === undefined)
                    ) {
                        this.checkVATError = true;
                        this.checkOpenModal.set(2, 2);
                        if (this.modalRef) {
                            this.modalRef.close();
                        }
                        this.modalRef = this.modalService.open(this.contentVATModal, { backdrop: 'static' });
                        return;
                    }
                }
            }
        }
        this.saveAfter();
    }

    ngAfterViewInit(): void {
        const inputs = document.getElementsByTagName('input');
        if (!this.statusPurchase) {
            inputs[2].focus();
        } else {
            inputs[0].focus();
        }
        // if (this.dataSession && this.dataSession.isEdit) {
        //     this.isEdit = false;
        //     this.edit();
        // }
    }

    addIfLastInput(i) {
        if (i === this.ppDiscountReturnDetails.length - 1) {
            this.addNewRow(0, false, false);
        }
    }
    ngAfterViewChecked(): void {
        this.disableInput();
    }
    changeStatusCheckSave() {
        this.checkSave = false;
        this.checkOpenModal = undefined;
    }

    changeDate() {
        if (this.ppDiscountReturn.date) {
            this.ppDiscountReturn.postedDate = this.ppDiscountReturn.date;
        }
    }
    view(voucher) {
        // this.activeModal.dismiss(true);
        let url = '';
        console.log(voucher.typeGroupID);
        switch (voucher.typeGroupID) {
            // Hàng bán trả lại
            case 33:
                url = `/#/hang-ban/tra-lai/${voucher.refID2}/edit/from-ref`;
                break;
            // Giảm giá hàng bán
            case 34:
                url = `/#/hang-ban/giam-gia/${voucher.refID2}/edit/from-ref`;
                break;
            // Xuất hóa đơn
            case 35:
                url = `/#/xuat-hoa-don/${voucher.refID2}/edit/from-ref`;
                break;
            case 22:
                url = `/#/hang-mua/tra-lai/${voucher.refID2}/view`;
                break;
            case 23:
                url = `/#/hang-mua/giam-gia/${voucher.refID2}/view`;
                break;
            case 10:
                url = `/#/mc-receipt/${voucher.refID2}/edit/from-ref`;
                break;
            case 16:
                url = `/#/mb-deposit/${voucher.refID2}/edit/from-ref`;
                break;
            case 17:
                url = `/#/mb-credit-card/${voucher.refID2}/edit/from-ref`;
                break;
            case 70:
                url = `/#/g-other-voucher/${voucher.refID2}/edit/from-ref`;
                break;
            case 11:
                url = `/#/mc-payment/${voucher.refID2}/edit/from-ref`;
                break;
            case 31:
                url = `/#/sa-order/${voucher.refID2}/edit/from-ref`;
                break;
            case 24:
                url = `/#/mua-dich-vu/${voucher.refID2}/edit/from-ref`;
                break;
            case 40:
                url = `/#/nhap-kho/${voucher.refID2}/edit/from-ref`;
                break;
            case 20:
                url = `/#/don-mua-hang/${voucher.refID2}/edit/from-ref`;
                break;
            case 41:
                url = `/#/xuat-kho/${voucher.refID2}/edit/from-ref`;
                break;
            case 21:
                this.viewVoucherService.checkViaStockPPInvoice({ id: voucher.refID2 }).subscribe(
                    (res: HttpResponse<any>) => {
                        if (res.body.message === UpdateDataMessages.STOCK_TRUE) {
                            url = `/#/mua-hang/qua-kho-ref/${voucher.refID2}/edit/1`;
                            window.open(url, '_blank');
                        } else if (res.body.message === UpdateDataMessages.STOCK_FALSE) {
                            url = `/#/mua-hang/khong-qua-kho-ref/${voucher.refID2}/edit/1`;
                            window.open(url, '_blank');
                        } else {
                            this.toasService.error(this.translateService.instant('ebwebApp.pPInvoice.error.default'));
                        }
                        return;
                    },
                    (res: HttpErrorResponse) => {
                        this.toasService.error(this.translateService.instant('ebwebApp.pPInvoice.error.default'));
                    }
                );
                return;
            case 18:
                url = `/#/mc-audit/${voucher.refID2}/edit/from-ref`;
                break;
            case 32:
                url = `/#/chung-tu-ban-hang/${voucher.refID2}/edit/from-ref`;
                break;
            case 30:
                url = `/#/sa-quote/${voucher.refID2}/edit/from-ref`;
                break;
            case 12:
            case 13:
            case 14:
                url = `/#/mb-teller-paper/${voucher.refID2}/edit/from-ref`;
                break;
        }
        window.open(url, '_blank');
    }

    getMgForPPOderTextCode() {
        return this.mgForPPOderTextCode;
    }

    changeInvoiceDateQ() {
        if (this.saBill.invoiceDate) {
            this.saBill.refDateTime = this.saBill.invoiceDate;
        }
    }

    viewMaterialGoodsSpecification(detail) {
        if (!this.statusPurchase) {
            if (detail.mgForPPOderItem) {
                if (detail.mgForPPOderItem.isFollow) {
                    this.refModalService.open(
                        detail,
                        EbMaterialGoodsSpecificationsModalComponent,
                        detail.materialGoodsSpecificationsLedgers,
                        false,
                        2
                    );
                } else {
                    this.toasService.error(this.translateService.instant('ebwebApp.materialGoodsSpecifications.notFollow'));
                }
            } else {
                this.toasService.error(this.translateService.instant('ebwebApp.materialGoodsSpecifications.noMaterialGoods'));
            }
        }
    }

    registerSelectMaterialGoodsSpecification() {
        this.eventSubscriber = this.eventManager.subscribe('selectMaterialGoodsSpecification', response => {
            this.viewMaterialGoodsSpecification(this.contextMenu.selectedData);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerMaterialGoodsSpecifications() {
        this.eventManager.subscribe('materialGoodsSpecifications', ref => {
            const group = ref.content.reduce((g: any, materialGoodsSpecificationsLedger) => {
                g[materialGoodsSpecificationsLedger.iWRepositoryID] = g[materialGoodsSpecificationsLedger.iWRepositoryID] || [];
                g[materialGoodsSpecificationsLedger.iWRepositoryID].push(materialGoodsSpecificationsLedger);
                return g;
            }, {});
            const groupData = Object.keys(group).map(a => {
                return {
                    iWRepositoryID: a,
                    materialGoodsSpecificationsLedgers: group[a]
                };
            });
            for (let i = 0; i < groupData.length; i++) {
                const quantity = groupData[i].materialGoodsSpecificationsLedgers.reduce(function(prev, cur) {
                    return prev + cur.iWQuantity;
                }, 0);
                if (i === 0) {
                    this.ppDiscountReturnDetails[this.currentRow].quantity = quantity;
                    this.ppDiscountReturnDetails[this.currentRow].repositoryID = groupData[i].iWRepositoryID;
                    this.ppDiscountReturnDetails[this.currentRow].repository = this.repositoryPopup.find(
                        x => x.id === groupData[i].iWRepositoryID
                    );
                    this.ppDiscountReturnDetails[this.currentRow].materialGoodsSpecificationsLedgers =
                        groupData[i].materialGoodsSpecificationsLedgers;
                } else {
                    const ppDiscountReturnDetail = { ...this.ppDiscountReturnDetails[this.currentRow] };
                    ppDiscountReturnDetail.quantity = quantity;
                    ppDiscountReturnDetail.repositoryID = groupData[i].iWRepositoryID;
                    ppDiscountReturnDetail.repository = this.repositoryPopup.find(x => x.id === groupData[i].iWRepositoryID);
                    ppDiscountReturnDetail.materialGoodsSpecificationsLedgers = groupData[i].materialGoodsSpecificationsLedgers;
                    this.ppDiscountReturnDetails.push(ppDiscountReturnDetail);
                }
            }
        });
    }
}
