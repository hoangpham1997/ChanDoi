import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { Principal } from 'app/core';

import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { MBCreditCardService } from './mb-credit-card.service';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { CurrencyService } from 'app/danhmuc/currency';
import { DatePipe } from '@angular/common';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { IMBCreditCard } from 'app/shared/model/mb-credit-card.model';
import { IMBCreditCardDetails } from 'app/shared/model/mb-credit-card-details.model';
import { IMBCreditCardDetailTax } from 'app/shared/model/mb-credit-card-detail-tax.model';
import { ICreditCard } from 'app/shared/model/credit-card.model';
import { CreditCardService } from 'app/danhmuc/credit-card';
import { IType } from 'app/shared/model/type.model';
import { TypeService } from 'app/entities/type';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import * as moment from 'moment';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import {
    DDSo_NCachHangDVi,
    DDSo_NCachHangNghin,
    DDSo_NgoaiTe,
    DDSo_TienVND,
    MSGERROR,
    PPINVOICE_COMPONENT_TYPE,
    SO_LAM_VIEC,
    TCKHAC_MauCTuChuaGS,
    TCKHAC_SDSOQUANTRI,
    TypeID
} from 'app/app.constants';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { ExpenseItemService } from 'app/entities/expense-item';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { EMContractService } from 'app/entities/em-contract';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { CostSetService } from 'app/entities/cost-set';
import { BudgetItemService } from 'app/entities/budget-item';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { IMCPayment } from 'app/shared/model/mc-payment.model';
import { IMBCreditCardDetailVendor } from 'app/shared/model/mb-credit-card-detail-vendor.model';
import { IMCPaymentDetailVendor } from 'app/shared/model/mc-payment-detail-vendor.model';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { IRefVoucher } from 'app/shared/model/ref-voucher.model';
import { IPPInvoiceDetailCost } from 'app/shared/model/pp-invoice-detail-cost.model';
import { PPInvoiceService } from 'app/muahang/mua_hang_qua_kho';
import { MuaDichVuService } from 'app/muahang/mua-dich-vu/mua-dich-vu.service';
import { IMuaDichVuResult, MuaDichVuResult } from 'app/muahang/mua-dich-vu/model/mua-dich-vu-result.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { IPPInvoice } from 'app/shared/model/pp-invoice.model';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-mb-credit-card',
    templateUrl: './mb-credit-card.component.html',
    styleUrls: ['./mb-credit-card.component.css'],
    providers: [DatePipe]
})
export class MBCreditCardComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    @ViewChild('unRecordModalPPService') unRecordModalPPService: any;
    @ViewChild('unRecordModalPPInvoice') unRecordModalPPInvoice: any;
    @ViewChild('checkHadPaidModal') checkHadPaidModal: any;
    @ViewChild('checkHadReferencePaidModal') checkHadReferencePaidModal: any;
    currentAccount: any;
    isSaving: boolean;
    mBCreditCards: IMBCreditCard[];
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
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    typeID: string;
    fromDate: any;
    toDate: any;
    currencyID: string;
    recorded: boolean;
    accountingObjectCode: string;
    searchValue: string;
    creditCardDetails: IMBCreditCardDetails[];
    currencies: ICurrency[];
    accountingObjects: IAccountingObject[];
    mBCreditCardDetails: IMBCreditCardDetails[];
    mBCreditCardDetailTax: IMBCreditCardDetailTax[];
    date: any;
    strRecorded: string;
    items: any;
    record_: any;
    selectedRow: IMBCreditCard;
    selectDetails: IMBCreditCardDetails[];
    objSearch: ISearchVoucher;
    rowNum: number;
    pageCount: any;
    accountingObjectName: string;
    isShowSearch: boolean;
    creditcards: ICreditCard[];
    typeName: string;
    types: IType[];
    isRecord: Boolean;
    typeMBCreditCard: Number;
    reSearchWhenRecord: string;
    listRecord: any;
    listColumnsRecord: string[] = ['name'];
    listHeaderColumnsRecord: string[] = ['Trạng thái'];
    statusRecordAfterFind: Boolean;
    viewVouchers: any;
    DDSo_NCachHangNghin: any;
    DDSo_NCachHangDVi: any;
    DDSo_TienVND: any;
    DDSo_NgoaiTe: any;
    costSets: ICostSet[];
    expenseItems: IExpenseItem[];
    organizationUnits: IOrganizationUnit[];
    eMContracts: IEMContract[];
    budgetItem: IBudgetItem[];
    statisticsCodes: IStatisticsCode[];
    goodsServicePurchases: IGoodsServicePurchase[];
    isSoTaiChinh: boolean;
    index: any;
    statusRecord: any;
    TCKHAC_SDSoQuanTri: any;
    currentRowSelected: any;
    lastRowSelected: any;
    currency: ICurrency;
    currentBook: any;
    color: string;
    modalRef: NgbModalRef;
    typeMultiAction: number;
    ppServiceID: string;
    ppInvoiceID: string;
    ppInvoice: IPPInvoice;
    allTypes: IType[];
    isShowGoodsServicePurchase: boolean;
    isRoleMuaDichVu: boolean;
    isRoleMuaHangQuaKho: boolean;
    isRoleMuaHangKhongQuaKho: boolean;
    ROLE_MUA_DICH_VU_XEM = ROLE.MuaDichVu_Xem;
    ROLE_MUA_HANG_QUA_KHO_XEM = ROLE.MuaHangQuaKho_Xem;
    ROLE_MUA_HANG_KHONG_QUA_KHO_XEM = ROLE.MuaHangKhongQuaKho_Xem;

    // trả tiền nhà cung cấp
    mBCreditCardDetailVendors: IMBCreditCardDetailVendor[];

    /*Mua hàng qua kho*/
    ppInvoiceDetails: any[];
    refVouchers?: IRefVoucher[];
    PPINVOICE_COMPONENT_TYPE = PPINVOICE_COMPONENT_TYPE;
    componentType: number;
    ppInvoiceDetailCost?: any[];
    sumQuantity: number;
    sumUnitPriceOriginal: number;
    sumUnitPrice: number;
    sumMainQuantity: number;
    sumMainUnitPrice: number;
    sumAmountOriginal: number;
    sumFreightAmountOriginal: number;
    totalAmount: number;
    totalAmountOriginal: number;
    totalDiscountAmount: number;
    totalDiscountAmountOriginal: number;
    totalVATAmount: number;
    totalVATAmountOriginal: number;
    totalImportTaxAmount: number;
    totalImportTaxAmountOriginal: number;
    totalSpecialConsumeTaxAmount: number;
    totalSpecialConsumeTaxAmountOriginal: number;
    totalFreightAmount: number;
    totalFreightAmountOriginal: number;
    totalImportTaxExpenseAmount: number;
    totalImportTaxExpenseAmountOriginal: number;
    totalInwardAmount: number;
    totalInwardAmountOriginal: number;
    sumCustomUnitPrice: number;
    modalRefMess: NgbModalRef;
    pPInvoiceID: string;
    pPServiceID: string;

    /*Mua hàng qua kho*/

    /*Mua dịch vụ*/
    muaDichVuResult: MuaDichVuResult;
    /*****/

    ROLE_TTD_XEM = ROLE.TheTinDung_Xem;
    ROLE_TTD_THEM = ROLE.TheTinDung_Them;
    ROLE_TTD_SUA = ROLE.TheTinDung_Sua;
    ROLE_TTD_XOA = ROLE.TheTinDung_Xoa;
    ROLE_TTD_GHISO = ROLE.TheTinDung_GhiSo;
    ROLE_TTD_IN = ROLE.TheTinDung_In;
    ROLE_TTD_KETXUAT = ROLE.TheTinDung_KetXuat;

    // navigate update form
    dataSession: IDataSessionStorage = new class implements IDataSessionStorage {
        accountingObjectName: string;
        itemsPerPage: number;
        page: number;
        pageCount: number;
        predicate: number;
        reverse: number;
        rowNum: number;
        totalItems: number;
    }();

    buttonDeleteTranslate = 'ebwebApp.mBDeposit.toolTip.delete';
    buttonAddTranslate = 'ebwebApp.mBDeposit.toolTip.add';
    buttonEditTranslate = 'ebwebApp.mBDeposit.toolTip.edit';
    buttonRecordTranslate = 'ebwebApp.mBDeposit.toolTip.record';
    buttonUnRecordTranslate = 'ebwebApp.mBDeposit.toolTip.unrecord';
    buttonExportTranslate = 'ebwebApp.mBDeposit.toolTip.export';
    buttonSearchTranslate = 'ebwebApp.mBDeposit.toolTip.search';

    constructor(
        private mBCreditCardService: MBCreditCardService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private accountingObjectService: AccountingObjectService,
        private currencyService: CurrencyService,
        private datepipe: DatePipe,
        private gLService: GeneralLedgerService,
        public utilsService: UtilsService,
        private creditCardService: CreditCardService,
        private typeService: TypeService,
        public translate: TranslateService,
        private toastr: ToastrService,
        private expenseItemsService: ExpenseItemService,
        private statisticsCodeService: StatisticsCodeService,
        private eMContractService: EMContractService,
        private organizationUnitService: OrganizationUnitService,
        private costSetService: CostSetService,
        private budgetItemService: BudgetItemService,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        private refModalService: RefModalService,
        private viewVoucherService: ViewVoucherService,
        private pPInvoiceService: PPInvoiceService,
        private muaDichVuService: MuaDichVuService,
        private modalService: NgbModal,
        public activeModal: NgbActiveModal,
        private ppServiceService: MuaDichVuService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
            this.translate.get(['ebwebApp.mBCreditCard.home.recorded', 'ebwebApp.mBCreditCard.home.unrecorded']).subscribe(res => {
                this.listRecord = [
                    { value: 1, name: res['ebwebApp.mBCreditCard.home.recorded'] },
                    { value: 0, name: res['ebwebApp.mBCreditCard.home.unrecorded'] }
                ];
            });
        });
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    search() {
        if (this.objSearch.fromDate > this.objSearch.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBCreditCard.fromDateMustBeLessThanToDate'),
                this.translate.instant('ebwebApp.mBCreditCard.message')
            );
            return;
        }
        this.statusRecordAfterFind = this.objSearch.statusRecorded;
        this.objSearch.statusRecorded = this.statusRecord;
        sessionStorage.setItem('dataSearchMBCreditCard', JSON.stringify(this.saveSearchVoucher()));
        this.mBCreditCardService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                searchVoucher: JSON.stringify(this.saveSearchVoucher())
            })
            .subscribe(
                (res: HttpResponse<IMBCreditCard[]>) => this.paginateMBCreditCards(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        if (this.objSearch.statusRecorded) {
            this.isRecord = false;
        } else {
            this.isRecord = true;
        }
        // this.isFind = true;
        this.reSearchWhenRecord = JSON.stringify(this.saveSearchVoucher());
        this.page = 1;
        this.loadPage(this.page);
    }

    loadAll() {
        if (sessionStorage.getItem('dataSession')) {
            this.dataSession = JSON.parse(sessionStorage.getItem('dataSession'));
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.isShowSearch = this.dataSession.isShowSearch;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
            this.previousPage = this.dataSession.page;
            this.objSearch = JSON.parse(this.dataSession.searchVoucher);
            this.statusRecord = this.objSearch.statusRecorded;
            sessionStorage.removeItem('dataSession');
        }
        if (this.activatedRoute.snapshot.paramMap.has('isSearch')) {
            this.mBCreditCardService
                .searchAll({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    searchVoucher: this.dataSession.searchVoucher
                })
                .subscribe(
                    (res: HttpResponse<IMBCreditCard[]>) => this.paginateMBCreditCards(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            this.isShowSearch = true;
            return;
        } else {
            // this.router.navigate(['/mb-credit-card'], {
            //     queryParams: {
            //         page: this.page,
            //         size: this.itemsPerPage,
            //         sort: this.predicate + ',' + (this.reverse ? 'desc' : 'asc')
            //     }
            // });
            this.router.navigate(['/mb-credit-card']);
        }
        this.mBCreditCardService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                searchVoucher: JSON.stringify(this.saveSearchVoucher())
            })
            .subscribe(
                (res: HttpResponse<IMBCreditCard[]>) => this.paginateMBCreditCards(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    transition() {
        this.dataSession.rowNum = 0;
        // this.router.navigate(['/mb-credit-card'], {
        //     queryParams: {
        //         page: this.page,
        //         size: this.itemsPerPage,
        //         sort: this.predicate + ',' + (this.reverse ? 'desc' : 'asc')
        //     }
        // });
        this.router.navigate(['/mb-credit-card']);
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/mb-credit-card',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'desc' : 'asc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.objSearch = new class implements ISearchVoucher {
            accountingObjectID: string;
            currencyID: string;
            fromDate: moment.Moment;
            statusRecorded: boolean;
            textSearch: string;
            toDate: moment.Moment;
            typeID: number;
        }();
        this.dataSession = new class implements IDataSessionStorage {
            creditCardType: string;
            itemsPerPage: number;
            ownerCard: string;
            page: number;
            pageCount: number;
            predicate: number;
            reverse: number;
            rowNum: number;
            searchVoucher: string;
            totalItems: number;
        }();
        this.index = 0;
        this.reSearchWhenRecord = '{}';
        this.statusRecordAfterFind = null;
        this.isShowSearch = false;
        this.loadAll();
        this.registerChangeInMBCreditCards();
        this.recorded = false;
        this.isRecord = true;
        this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjects = res.body.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
        });
        this.creditCardService.query().subscribe((res: HttpResponse<ICreditCard[]>) => {
            this.creditcards = res.body;
        });
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticsCodes = res.body;
        });
        this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body;
        });
        this.expenseItemsService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body;
        });
        this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
            this.eMContracts = res.body;
        });
        this.organizationUnitService.getOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body;
        });
        this.budgetItemService.getBudgetItems().subscribe((res: HttpResponse<IBudgetItem[]>) => {
            this.budgetItem = res.body;
        });
        this.goodsServicePurchaseService.getGoodServicePurchases().subscribe((res: HttpResponse<IGoodsServicePurchase[]>) => {
            this.goodsServicePurchases = res.body;
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.isRoleMuaDichVu = this.currentAccount.authorities.includes(this.ROLE_MUA_DICH_VU_XEM);
            this.isRoleMuaHangQuaKho = this.currentAccount.authorities.includes(this.ROLE_MUA_HANG_QUA_KHO_XEM);
            this.isRoleMuaHangKhongQuaKho = this.currentAccount.authorities.includes(this.ROLE_MUA_HANG_KHONG_QUA_KHO_XEM);
            this.TCKHAC_SDSoQuanTri = this.currentAccount.systemOption.find(x => x.code === TCKHAC_SDSOQUANTRI).data;
            this.color = this.currentAccount.systemOption.find(x => x.code === TCKHAC_MauCTuChuaGS && x.data).data;
            if (account) {
                if (account.organizationUnit.taxCalculationMethod === 0) {
                    this.isShowGoodsServicePurchase = true;
                } else {
                    this.isShowGoodsServicePurchase = false;
                }
                if (this.TCKHAC_SDSoQuanTri === '1') {
                    this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                } else {
                    this.isSoTaiChinh = true;
                }
            }
            this.currentBook = this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data;
            this.currencyService.findAllActive().subscribe(res => {
                this.currencies = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                this.currency = this.currencies.find(cur => cur.currencyCode === this.currentAccount.organizationUnit.currencyID);
            });
            if (this.isSoTaiChinh) {
                this.predicate = 'date desc,postedDate desc,noFBook';
            } else {
                this.predicate = 'date desc,postedDate desc,noMBook';
            }
            this.DDSo_NCachHangNghin = this.currentAccount.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
            this.DDSo_NCachHangDVi = this.currentAccount.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;
            this.DDSo_NgoaiTe = this.currentAccount.systemOption.find(x => x.code === DDSo_NgoaiTe && x.data).data;
            this.DDSo_TienVND = this.currentAccount.systemOption.find(x => x.code === DDSo_TienVND && x.data).data;
        });
        this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body.filter(type => type.typeGroupID === 17);
            this.allTypes = res.body;
        });
        this.typeMBCreditCard = 170;
        this.registerExport();
        // Trả tiền nhà cung cấp
        this.mBCreditCardDetailVendors = [];
        // Mua hàng qua kho
        this.ppInvoiceDetails = [];
        // this.isFind = null;
        // Mua dich vu
        this.muaDichVuResult = {};
        this.registerLockSuccess();
        this.registerUnlockSuccess();
    }

    clearSearch() {
        this.objSearch = {};
        this.statusRecord = null;
        this.objSearch.fromDate = null;
        this.objSearch.toDate = null;
        this.mBCreditCardService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                searchVoucher: JSON.stringify(this.saveSearchVoucher())
            })
            .subscribe(
                (res: HttpResponse<IMBCreditCard[]>) => this.paginateMBCreditCards(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TheTinDung_Them])
    addNew(isNew = false) {
        event.preventDefault();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.pageCount = this.pageCount;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.rowNum;
        // sort
        this.dataSession.predicate = this.predicate;
        this.dataSession.searchVoucher = JSON.stringify(this.saveSearchVoucher());
        this.dataSession.accountingObjectName = this.accountingObjectName;
        this.dataSession.reverse = this.reverse;
        sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
        this.router.navigate(['mb-credit-card/', 'new']);
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    /*ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }*/

    trackId(index: number, item: IMBCreditCard) {
        return item.id;
    }

    registerChangeInMBCreditCards() {
        this.eventSubscriber = this.eventManager.subscribe('mBCreditCardListModification', response => {
            this.selectedRows = [];
            this.searchAfterChangeRecord();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    sort() {
        const result = ['date' + ',' + (this.reverse ? 'desc' : 'asc')];
        result.push('postedDate' + ',' + (this.reverse ? 'desc' : 'asc'));
        if (this.isSoTaiChinh) {
            result.push('noFBook' + ',' + (this.reverse ? 'desc' : 'asc'));
        } else {
            result.push('noMBook' + ',' + (this.reverse ? 'desc' : 'asc'));
        }
        // const result = ['date, desc', 'postedDate, desc'];
        return result;
    }

    private paginateMBCreditCards(data: IMBCreditCard[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.mBCreditCards = data;
        this.objects = data;
        //  load first element
        if (this.dataSession && this.dataSession.rowNum) {
            this.selectedRows.push(this.mBCreditCards[this.dataSession.rowNum]);
            this.selectedRow = this.mBCreditCards[this.dataSession.rowNum];
        } else {
            this.selectedRows.push(this.mBCreditCards[0]);
            this.selectedRow = this.mBCreditCards[0];
        }
        // console.log(this.selectedRow);
        if (this.selectedRow) {
            this.onSelect(this.selectedRow);
        } else {
            this.mBCreditCardDetails = null;
            this.mBCreditCardDetailTax = null;
            this.viewVouchers = null;
        }
        //  display pageCount
        this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
        if (this.selectedRow) {
            if (this.selectedRow.typeID === this.TYPE_MB_CREDIT_CARD_MUA_HANG) {
                this.findMuaHangQuaKho(this.selectedRow.id);
            } else {
                this.findMuaDichVu(this.selectedRow.id);
            }
        }
        sessionStorage.removeItem('dataSearchMBCreditCard');
    }

    private onError(errorMessage: string) {
        this.toastr.error(this.translate.instant('ebwebApp.mBCreditCard.error'), this.translate.instant('ebwebApp.mBCreditCard.message'));
    }

    onSelect(select: IMBCreditCard) {
        this.selectedRow = select;
        switch (this.mBCreditCard.typeID) {
            case this.TYPE_MB_CREDIT_CARD:
            case this.TYPE_MB_CREDIT_CARD_NCC:
                this.index = this.mBCreditCards.indexOf(this.selectedRow);
                this.rowNum = this.getRowNumberOfRecord(this.page, this.index);
                this.mBCreditCardService.find(this.selectedRow.id).subscribe((res: HttpResponse<IMBCreditCard>) => {
                    this.mBCreditCardDetails =
                        res.body.mBCreditCardDetails === undefined
                            ? []
                            : res.body.mBCreditCardDetails.sort((a, b) => a.orderPriority - b.orderPriority);
                    this.mBCreditCardDetailTax =
                        res.body.mbCreditCardDetailTax === undefined
                            ? []
                            : res.body.mbCreditCardDetailTax.sort((a, b) => a.orderPriority - b.orderPriority);
                    this.mBCreditCardDetailVendors =
                        res.body.mBCreditCardDetailVendor === undefined
                            ? []
                            : res.body.mBCreditCardDetailVendor.sort((a, b) => a.orderPriority - b.orderPriority);
                    this.viewVouchers = res.body.viewVouchers;
                });
                if (this.selectedRow.recorded) {
                    this.isRecord = false;
                } else {
                    this.isRecord = true;
                }
                break;
            case this.TYPE_MB_CREDIT_CARD_MUA_DV:
                this.findMuaDichVu(this.selectedRow.id);
                break;
            case this.TYPE_MB_CREDIT_CARD_MUA_HANG:
                this.findMuaHangQuaKho(this.selectedRow.id);
                break;
        }

        // console.log(this.mBTellerPaperDetails);
        // console.log(this.mBTellerPaperDetailTaxs);
    }

    trackbyCurrencyID(index: number, item: ICurrency) {
        return item.id;
    }

    get mBCreditCard() {
        return this.selectedRow;
    }

    set mBCreditCard(mBCreditCard: IMBCreditCard) {
        this.selectedRow = mBCreditCard;
    }

    trackbyAccountingObjectID(index: number, item: IAccountingObject) {
        return item.id;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMBCreditCard>>) {
        result.subscribe((res: HttpResponse<IMBCreditCard>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
    }

    private onSaveError() {
        this.isSaving = false;
    }

    exportPdf(isDownload) {
        this.mBCreditCardService.getCustomerReport({}).subscribe(response => {
            // this.showReport(response);
            const file = new Blob([response.body], { type: 'application/pdf' });
            const fileURL = window.URL.createObjectURL(file);

            if (isDownload) {
                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'ten_bao_cao.pdf';
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
    }

    record() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 2;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            event.preventDefault();
            if (this.selectedRow && !this.selectedRow.recorded && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
                this.dataSession.rowNum = this.mBCreditCards.indexOf(this.selectedRow);
                this.record_ = {};
                this.record_.id = this.selectedRow.id;
                this.record_.typeID = this.selectedRow.typeID;
                if (!this.selectedRow.recorded) {
                    this.selectedRows = [];
                    switch (this.selectedRow.typeID) {
                        case this.TYPE_MB_CREDIT_CARD:
                        case this.TYPE_MB_CREDIT_CARD_NCC:
                            this.gLService.record(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                                console.log(JSON.stringify(res.body));
                                if (res.body.success) {
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
                                this.searchAfterChangeRecord();
                            });
                            if (this.objSearch.statusRecorded === null || this.objSearch.statusRecorded === undefined) {
                                this.isRecord = false;
                            } else {
                                this.isRecord = !this.objSearch.statusRecorded;
                            }
                            break;
                        case this.TYPE_MB_CREDIT_CARD_MUA_DV:
                            this.mBCreditCardService.find(this.selectedRow.id).subscribe((res: HttpResponse<IMBCreditCard>) => {
                                const ppServiceID = res.body.ppServiceID;
                                if (ppServiceID) {
                                    this.record_.id = ppServiceID;
                                    this.record_.typeID = this.TYPE_PP_SERVICE;
                                    this.gLService.record(this.record_).subscribe((resRc: HttpResponse<Irecord>) => {
                                        console.log(JSON.stringify(res.body));
                                        if (resRc.body.success) {
                                            this.toastr.success(
                                                this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
                                                this.translate.instant('ebwebApp.mBCreditCard.message')
                                            );
                                            this.searchAfterChangeRecord();
                                        }
                                    });
                                }
                            });
                            break;
                        case this.TYPE_MB_CREDIT_CARD_MUA_HANG:
                            this.mBCreditCardService.find(this.selectedRow.id).subscribe((res: HttpResponse<any>) => {
                                const ppInvocieID = res.body.ppInvocieID;
                                if (ppInvocieID) {
                                    this.record_.id = ppInvocieID;
                                    this.record_.typeID = this.TYPE_PP_INVOICE;
                                    this.gLService.record(this.record_).subscribe((resRc: HttpResponse<Irecord>) => {
                                        console.log(JSON.stringify(res.body));
                                        if (resRc.body.success) {
                                            this.toastr.success(
                                                this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
                                                this.translate.instant('ebwebApp.mBCreditCard.message')
                                            );
                                            this.searchAfterChangeRecord();
                                        }
                                    });
                                }
                            });
                            break;
                    }
                }
            }
        }
    }

    unrecord() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 1;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (this.selectedRow && this.selectedRow.recorded && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
                this.dataSession.rowNum = this.mBCreditCards.indexOf(this.selectedRow);
                this.record_ = {};
                this.record_.id = this.selectedRow.id;
                this.record_.typeID = this.selectedRow.typeID;
                if (this.selectedRow.recorded) {
                    this.selectedRows = [];
                    switch (this.selectedRow.typeID) {
                        case this.TYPE_MB_CREDIT_CARD:
                        case this.TYPE_MB_CREDIT_CARD_NCC:
                            this.gLService.unrecord(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                                console.log(JSON.stringify(res.body));
                                if (res.body.success) {
                                    this.toastr.success(
                                        this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                                        this.translate.instant('ebwebApp.mBCreditCard.message')
                                    );
                                }
                                this.searchAfterChangeRecord();
                            });
                            if (this.objSearch.statusRecorded === null || this.objSearch.statusRecorded === undefined) {
                                this.isRecord = true;
                            } else {
                                this.isRecord = !this.objSearch.statusRecorded;
                            }
                            break;
                        case this.TYPE_MB_CREDIT_CARD_MUA_DV:
                            this.mBCreditCardService.find(this.selectedRow.id).subscribe((res: HttpResponse<IMBCreditCard>) => {
                                this.ppServiceID = res.body.ppServiceID;
                                this.ppServiceService.checkHadReference({ ppServiceId: this.ppServiceID }).subscribe(res2 => {
                                    if (res2.body.messages === UpdateDataMessages.HAD_REFERENCE_AND_PAID) {
                                        this.openModal(this.checkHadReferencePaidModal);
                                    } else if (res2.body.messages === UpdateDataMessages.HAD_REFERENCE) {
                                        this.openModal(this.unRecordModalPPService);
                                    } else if (res2.body.messages === UpdateDataMessages.HAD_PAID) {
                                        this.openModal(this.checkHadPaidModal);
                                    } else {
                                        this.unRecordPPService();
                                    }
                                });
                            });
                            break;
                        case this.TYPE_MB_CREDIT_CARD_MUA_HANG:
                            this.mBCreditCardService.find(this.selectedRow.id).subscribe((res: HttpResponse<any>) => {
                                const ppInvocieID = res.body.ppInvocieID;
                                if (ppInvocieID) {
                                    this.pPInvoiceService.findById({ id: ppInvocieID }).subscribe((res2: HttpResponse<any>) => {
                                        this.ppInvoice = res2.body;
                                        this.pPInvoiceService.checkUnRecord({ id: ppInvocieID }).subscribe(
                                            (res3: HttpResponse<any>) => {
                                                if (res3.body.message === UpdateDataMessages.NOTHING) {
                                                    this.checkPayVendor(this.unRecordModalPPInvoice);
                                                } else if (res3.body.message === UpdateDataMessages.PPINVOICE_USED) {
                                                    this.ppInvoice.isUsed = true;
                                                    this.checkPayVendor(this.unRecordModalPPInvoice);
                                                    // this.unRecordCheck = this.modalService.open(content, { size: 'lg', backdrop: 'static' });
                                                } else {
                                                    this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                                                }
                                            },
                                            (res3: HttpErrorResponse) => {
                                                this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                                            }
                                        );
                                    });
                                }
                            });
                            break;
                    }
                }
            }
        }
    }

    checkPayVendor(content) {
        this.pPInvoiceService.checkPayVendor({ id: this.selectedRow.id }).subscribe((res: HttpResponse<any>) => {
            if (res.body.message === UpdateDataMessages.SUCCESS) {
                this.ppInvoice.isPlayVendor = false;
                if (this.ppInvoice.isUsed) {
                    this.modalRef = this.modalService.open(content, { size: 'lg', backdrop: 'static' });
                } else {
                    this.unRecordPPInvoice();
                }
            } else if (res.body.message === UpdateDataMessages.DUPLICATE) {
                this.ppInvoice.isPlayVendor = true;
                this.modalRef = this.modalService.open(content, { size: 'lg', backdrop: 'static' });
            }
        });
    }

    unRecordPPInvoice() {
        this.record_.id = this.ppInvoice.id;
        this.record_.typeID = this.TYPE_PP_INVOICE;
        this.gLService.unrecord(this.record_).subscribe((resURc: HttpResponse<Irecord>) => {
            if (resURc.body.success) {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.toastr.success(
                    this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                    this.translate.instant('ebwebApp.mBCreditCard.message')
                );
                this.searchAfterChangeRecord();
            }
        });
    }

    openModal(content) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(content, { backdrop: 'static' });
    }

    unRecordPPService() {
        if (this.ppServiceID) {
            this.record_.id = this.ppServiceID;
            this.record_.typeID = this.TYPE_PP_SERVICE;
            this.gLService.unrecord(this.record_).subscribe((resURc: HttpResponse<Irecord>) => {
                if (resURc.body.success) {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    this.toastr.success(
                        this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                        this.translate.instant('ebwebApp.mBCreditCard.message')
                    );
                    this.searchAfterChangeRecord();
                }
            });
        }
    }

    doubleClickRow(any?) {
        this.edit();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TheTinDung_Xem])
    edit() {
        event.preventDefault();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.pageCount = this.pageCount;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.mBCreditCards.indexOf(this.selectedRow);
        this.dataSession.isShowSearch = this.isShowSearch;
        this.dataSession.searchVoucher = JSON.stringify(this.saveSearchVoucher());
        this.dataSession.predicate = this.predicate;
        this.dataSession.accountingObjectName = this.accountingObjectName;
        this.dataSession.reverse = this.reverse;
        sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
        switch (this.selectedRow.typeID) {
            case this.TYPE_MB_CREDIT_CARD:
            case this.TYPE_MB_CREDIT_CARD_NCC:
                this.router.navigate(['./mb-credit-card', this.selectedRow.id, 'edit']);
                break;
            case this.TYPE_MB_CREDIT_CARD_MUA_DV:
                if (this.isRoleMuaDichVu || this.currentAccount.authorities.includes('ROLE_ADMIN')) {
                    this.mBCreditCardService.find(this.selectedRow.id).subscribe((res: HttpResponse<IMBCreditCard>) => {
                        const ppServiceID = res.body.ppServiceID;
                        if (ppServiceID) {
                            this.router.navigate(['./mua-dich-vu', ppServiceID, 'edit', 'from-mb-credit-card', this.selectedRow.id]);
                        }
                    });
                } else {
                    this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
                    return false;
                }
                break;
            case this.TYPE_MB_CREDIT_CARD_MUA_HANG:
                this.mBCreditCardService.find(this.selectedRow.id).subscribe((res: HttpResponse<any>) => {
                    const ppInvoiceID = res.body.ppInvocieID;
                    if (
                        res.body.storedInRepository &&
                        (this.isRoleMuaHangQuaKho || this.currentAccount.authorities.includes('ROLE_ADMIN'))
                    ) {
                        this.router.navigate(['./mua-hang', 'qua-kho', ppInvoiceID, 'edit', 'from-mb-credit-card', this.selectedRow.id]);
                    } else if (
                        !res.body.storedInRepository &&
                        (this.isRoleMuaHangKhongQuaKho || this.currentAccount.authorities.includes('ROLE_ADMIN'))
                    ) {
                        this.router.navigate([
                            './mua-hang',
                            'khong-qua-kho',
                            ppInvoiceID,
                            'edit',
                            'from-mb-credit-card',
                            this.selectedRow.id
                        ]);
                    } else {
                        this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
                        return false;
                    }
                });
                break;
            default:
                this.router.navigate(['./mb-credit-card', this.selectedRow.id, 'edit']);
                break;
        }
        // }
    }

    delete() {
        event.preventDefault();
        const index = 0;
        if (this.selectedRows.length > 1 && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.typeMultiAction = 0;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else if (this.selectedRows.length === 1) {
            if (this.selectedRow && !this.selectedRow.recorded) {
                this.typeMultiAction = undefined;
                this.router.navigate(['/mb-credit-card', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
            }
        }
    }

    continueDelete() {
        if (this.typeMultiAction === 0) {
            this.mBCreditCardService.multiDelete(this.selectedRows).subscribe(
                (res: HttpResponse<any>) => {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    this.selectedRows = [];
                    this.searchAfterChangeRecord();
                    if (res.body.countTotalVouchers !== res.body.countSuccessVouchers) {
                        this.modalRef = this.refModalService.open(
                            res.body,
                            HandlingResultComponent,
                            null,
                            false,
                            null,
                            null,
                            null,
                            null,
                            null,
                            true
                        );
                    } else {
                        this.toastr.success(this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'));
                    }
                    this.activeModal.close();
                    // }
                },
                (res: HttpErrorResponse) => {
                    if (res.error.errorKey === 'errorDeleteList') {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.mBDeposit.errorDeleteVoucherNo'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                }
            );
        } else if (this.typeMultiAction === 1) {
            this.mBCreditCardService.multiUnrecord(this.selectedRows).subscribe(
                (res: HttpResponse<any>) => {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    this.selectedRows = [];
                    if (res.body.countTotalVouchers !== res.body.countSuccessVouchers) {
                        this.modalRef = this.refModalService.open(
                            res.body,
                            HandlingResultComponent,
                            null,
                            false,
                            null,
                            null,
                            null,
                            null,
                            null,
                            true
                        );
                    } else {
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    this.activeModal.close();
                    this.search();
                    // }
                },
                (res: HttpErrorResponse) => {
                    this.toastr.error(
                        this.translate.instant('global.data.unRecordFailed'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                }
            );
        } else if (this.typeMultiAction === 2) {
            const rq: RequestRecordListDtoModel = {};
            if (this.modalRef) {
                this.modalRef.close();
            }
            rq.records = this.selectedRows.map(n => {
                return { id: n.id, typeID: n.typeID };
            });
            rq.typeIDMain = this.TYPE_MB_CREDIT_CARD;
            this.gLService.recordList(rq).subscribe((res: HttpResponse<HandlingResult>) => {
                this.selectedRows.forEach(slr => {
                    const update = this.mBCreditCards.find(
                        n => n.id === slr.id && !res.body.listFail.some(m => m.id === slr.id || m.mBCreditCardID === slr.id)
                    );
                    if (update) {
                        update.recorded = true;
                    }
                });
                if (res.body.countFailVouchers > 0) {
                    res.body.listFail.forEach(n => {
                        if (n.mBCreditCardID) {
                            const mc = this.mBCreditCards.find(m => m.id === n.mBCreditCardID);
                            const type = this.types.find(t => t.id === mc.typeID);
                            n.noFBook = mc.noFBook;
                            n.noMBook = mc.noMBook;
                            n.typeName = type.typeName;
                        } else {
                            const type = this.allTypes.find(t => t.id === n.typeID);
                            n.typeName = type.typeName;
                        }
                    });
                    this.modalRef = this.refModalService.open(
                        res.body,
                        HandlingResultComponent,
                        null,
                        false,
                        null,
                        null,
                        null,
                        null,
                        null,
                        true
                    );
                    this.loadAll();
                } else {
                    this.messageRecord();
                }
            });
        } else if (!this.typeMultiAction && this.selectedRows.length === 1) {
            this.mBCreditCardService.delete(this.selectedRow.id).subscribe(response => {
                this.eventManager.broadcast({
                    name: 'mBCreditCardListModification',
                    content: 'Deleted an mBCreditCard'
                });
                this.modalRef.close();
            });
            this.toastr.success(this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'));
            this.router.navigate(['mb-credit-card']);
        }
        this.typeMultiAction = undefined;
    }

    messageRecord() {
        this.toastr.success(
            this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
    }

    closePopUpDelete() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    saveSearchVoucher(): ISearchVoucher {
        const searchObj: ISearchVoucher = {};
        searchObj.typeID = this.objSearch.typeID !== undefined ? this.objSearch.typeID : null;
        searchObj.statusRecorded = this.objSearch.statusRecorded !== undefined ? this.objSearch.statusRecorded : null;
        searchObj.currencyID = this.objSearch.currencyID !== undefined ? this.objSearch.currencyID : null;
        searchObj.fromDate = this.objSearch.fromDate !== undefined ? this.objSearch.fromDate : null;
        searchObj.toDate = this.objSearch.toDate !== undefined ? this.objSearch.toDate : null;
        searchObj.accountingObjectID = this.objSearch.accountingObjectID !== undefined ? this.objSearch.accountingObjectID : null;
        searchObj.textSearch = this.objSearch.textSearch !== undefined ? this.objSearch.textSearch : null;
        return this.convertDateFromClient(searchObj);
    }

    searchAfterChangeRecord() {
        if (this.reSearchWhenRecord) {
            this.mBCreditCardService
                .searchAll({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    searchVoucher: this.reSearchWhenRecord
                })
                .subscribe(
                    (res: HttpResponse<IMBDeposit[]>) => this.paginateMBCreditCards(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            this.page = 1;
            this.loadPage(this.page);
        }
    }

    getEMContractsbyID(id) {
        if (this.eMContracts) {
            const eMC = this.eMContracts.find(n => n.id === id);
            if (eMC) {
                return eMC.noMBook;
            }
        }
    }

    getAccountingObjectbyID(id) {
        if (this.accountingObjects) {
            const acc = this.accountingObjects.find(n => n.id === id);
            if (acc) {
                return acc.accountingObjectCode;
            }
        }
    }

    getStatisticsCodebyID(id) {
        if (this.statisticsCodes) {
            const statisticsCode = this.statisticsCodes.find(n => n.id === id);
            if (statisticsCode) {
                return statisticsCode.statisticsCode;
            }
        }
    }

    getCostSetbyID(id) {
        if (this.costSets) {
            const costSet = this.costSets.find(n => n.id === id);
            if (costSet) {
                return costSet.costSetCode;
            }
        }
    }

    getExpenseItembyID(id) {
        if (this.expenseItems) {
            const expenseItem = this.expenseItems.find(n => n.id === id);
            if (expenseItem) {
                return expenseItem.expenseItemCode;
            }
        }
    }

    getOrganizationUnitbyID(id) {
        if (this.organizationUnits) {
            const organizationUnit = this.organizationUnits.find(n => n.id === id);
            if (organizationUnit) {
                return organizationUnit.organizationUnitCode;
            }
        }
    }

    getBudgetItembyID(id) {
        if (this.budgetItem) {
            const iBudgetItem = this.budgetItem.find(n => n.id === id);
            if (iBudgetItem) {
                return iBudgetItem.budgetItemCode;
            }
        }
    }

    getGoodsServicePurchaseIDbyID(id) {
        if (this.goodsServicePurchases) {
            const goodsServicePurchase = this.goodsServicePurchases.find(n => n.id === id);
            if (goodsServicePurchase) {
                return goodsServicePurchase.goodsServicePurchaseCode;
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TheTinDung_KetXuat])
    export() {
        event.preventDefault();
        this.mBCreditCardService
            .exportPDF({
                searchVoucher: this.reSearchWhenRecord
            })
            .subscribe(res => {
                this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.TYPE_MB_CREDIT_CARD);
            });
    }

    private convertDateFromClient(searchVoucher: ISearchVoucher): ISearchVoucher {
        const copy: ISearchVoucher = Object.assign({}, searchVoucher, {
            fromDate:
                searchVoucher.fromDate != null && searchVoucher.fromDate.isValid() ? searchVoucher.fromDate.format(DATE_FORMAT) : null,
            toDate: searchVoucher.toDate != null && searchVoucher.toDate.isValid() ? searchVoucher.toDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.TYPE_MB_CREDIT_CARD}`, () => {
            this.exportExcel();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    exportExcel() {
        this.mBCreditCardService.exportExcel({ searchVoucher: this.reSearchWhenRecord }).subscribe(
            res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_TheTinDung.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            },
            () => {}
        );
    }

    getTypeName(typeID) {
        if (typeID && this.types) {
            const currentType = this.types.find(type => type.id === typeID);
            return currentType.typeName;
        }
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
        if (this.accountingObjects) {
            const epl = this.accountingObjects.find(n => n.id === id);
            if (epl) {
                return epl.accountingObjectCode;
            } else {
                return '';
            }
        }
    }

    getAmountOriginalType() {
        if (this.isForeignCurrency()) {
            return 8;
        }
        return 7;
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    /*Trả tiền nhà cung cấp*/
    sumMBCreditCardDetailVendors(prop) {
        let total = 0;
        for (let i = 0; i < this.mBCreditCardDetailVendors.length; i++) {
            total += this.mBCreditCardDetailVendors[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    /*Mua hàng qua kho*/
    changeAmountPPInvoice(ppInvoiceDetails) {
        // tiền hàng
        this.totalAmount = 0;
        this.totalAmountOriginal = 0;

        // tiền chiết khấu
        this.totalDiscountAmount = 0;
        this.totalDiscountAmountOriginal = 0;

        // thuế giá trị gia tăng
        this.totalVATAmount = 0;
        this.totalVATAmountOriginal = 0;

        // tiền thuế nhập khẩu
        this.totalImportTaxAmount = 0;
        this.totalImportTaxAmountOriginal = 0;

        // tiền thuế tài sản đảm bảo
        this.totalSpecialConsumeTaxAmount = 0;
        this.totalSpecialConsumeTaxAmountOriginal = 0;

        // tổng tiền thanh toán
        // this.amount = 0;
        // this.amountOriginal = 0;

        // chi phí mua
        this.totalFreightAmount = 0;
        this.totalFreightAmountOriginal = 0;

        // chi phí trước hải quan
        this.totalImportTaxExpenseAmount = 0;
        this.totalImportTaxExpenseAmountOriginal = 0;

        // giá nhập kho
        this.totalInwardAmount = 0;
        this.totalInwardAmountOriginal = 0;

        this.sumQuantity = 0;
        this.sumUnitPriceOriginal = 0;
        this.sumUnitPrice = 0;
        this.sumMainQuantity = 0;
        this.sumMainUnitPrice = 0;
        this.sumAmountOriginal = 0;
        this.sumFreightAmountOriginal = 0;

        // tổng giá tính thuế
        this.sumCustomUnitPrice = 0;

        for (const detail of ppInvoiceDetails) {
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
            this.totalAmount += detail.amount;
            this.totalAmountOriginal += detail.amountOriginal;
            // tiền chiết khấu
            this.totalDiscountAmount += detail.discountAmount;
            this.totalDiscountAmountOriginal += detail.discountAmountOriginal;
            // thuế giá trị gia tăng
            this.totalVATAmount += detail.vatAmount;
            this.totalVATAmountOriginal += detail.vatAmountOriginal;
            // tiền thuế nhập khẩu
            this.totalImportTaxAmount += detail.importTaxAmount;
            this.totalImportTaxAmountOriginal += detail.importTaxAmountOriginal;
            // tiền thuế tài sản đảm bảo
            this.totalSpecialConsumeTaxAmount += detail.specialConsumeTaxAmount;
            this.totalSpecialConsumeTaxAmountOriginal += detail.specialConsumeTaxAmountOriginal;
            // chi phí mua
            this.totalFreightAmount += detail.freightAmount;
            this.totalFreightAmountOriginal += detail.freightAmountOriginal;
            // chi phí trước hải quan
            this.totalImportTaxExpenseAmount += detail.importTaxExpenseAmount;
            this.totalImportTaxExpenseAmountOriginal += detail.importTaxExpenseAmountOriginal;
            // giá nhập kho
            this.totalInwardAmount += detail.inwardAmount;
            // this.totalInwardAmountOriginal += detail.inwardAmountOriginal;
            this.totalInwardAmountOriginal += detail.inwardAmountOriginal;

            // chi phí mua
            this.sumFreightAmountOriginal += detail.freightAmountOriginal;
            // giá tính thuế
            this.sumCustomUnitPrice += detail.customUnitPrice;
        }
        // this.totalInwardAmountOriginal = this.totalInwardAmount / this.selectedRow.exchangeRate;

        // tổng tiền thanh toán
        // // nhập khẩu
        // if (this.selectedRow.isImportPurchase) {
        //     this.amount = this.totalAmount - this.totalDiscountAmount;
        //     this.amountOriginal = this.totalAmountOriginal - this.totalDiscountAmountOriginal;
        // } else {
        //     // trong nước
        //     this.amount = this.totalAmount - this.totalDiscountAmount + this.totalVATAmount;
        //     this.amountOriginal =
        //         this.totalAmountOriginal - this.totalDiscountAmountOriginal + this.totalVATAmountOriginal;
        // }
    }

    findMuaHangQuaKho(id) {
        // this.ppInvoiceDetails = item.ppInvoiceDetails;
        this.pPInvoiceService.findDetailByPaymentVoucherID({ paymentVoucherID: id }).subscribe(res => {
            this.ppInvoiceDetails = res.body;
            this.changeAmountPPInvoice(this.ppInvoiceDetails);
        });

        // tham chiếu
        this.muaDichVuService
            .findRefVoucherByByPaymentVoucherID({
                typeID: this.TYPE_MB_CREDIT_CARD_MUA_HANG,
                paymentVoucherID: id,
                currentBook: this.currentBook
            })
            .subscribe((res: HttpResponse<IRefVoucher[]>) => {
                this.refVouchers = res.body;
            });
        // phân bổ chi phí
        this.pPInvoiceService
            .getPPInvoiceDetailCostByPaymentVoucherID({ paymentvoucherID: id })
            .subscribe((res: HttpResponse<IPPInvoiceDetailCost[]>) => {
                this.ppInvoiceDetailCost = res.body;
            });
    }

    getUnitPriceOriginalType() {
        if (this.isForeignCurrency()) {
            return 2;
        }
        return 1;
    }

    isForeignCurrency() {
        if (this.selectedRow) {
            return this.currentAccount && this.selectedRow.currencyID !== this.currentAccount.organizationUnit.currencyID;
        }
    }

    /*Mua hàng qua kho*/

    /*Mua dịch vụ*/
    trackIdentity(index, item: IMuaDichVuResult) {
        return item.receiptDate;
    }

    findMuaDichVu(id) {
        this.muaDichVuService.findAllPPServiceDetailsByPaymentVoucherID({ paymentVoucherID: id }).subscribe(resDetail => {
            this.muaDichVuResult.ppServiceDetailDTOS = resDetail.body ? resDetail.body : [];
            //
            this.muaDichVuResult.ppServiceDetailDTOS.forEach(detail => (detail.quantityFromDB = detail.quantity));
        });
        this.muaDichVuService
            .findRefVoucherByByPaymentVoucherID({
                typeID: this.TYPE_MB_CREDIT_CARD_MUA_DV,
                paymentVoucherID: id,
                currentBook: this.currentBook
            })
            .subscribe(resDetail => (this.muaDichVuResult.refVouchers = resDetail.body ? resDetail.body : []));
    }

    sumPPServiceDetailDTOS(prop) {
        let total = 0;
        if (this.muaDichVuResult && this.muaDichVuResult.ppServiceDetailDTOS) {
            for (let i = 0; i < this.muaDichVuResult.ppServiceDetailDTOS.length; i++) {
                total += this.muaDichVuResult.ppServiceDetailDTOS[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    getVATRateValue(vatRate: number) {
        const vatRateValue = vatRate ? (vatRate === 0 ? 0 : vatRate === 1 ? 5 : 10) : null;
        if (vatRate != null) {
            return vatRateValue + '%';
        }
        return '';
    }

    getSumDetails(prop) {
        let total = 0;
        if (this.mBCreditCardDetails) {
            for (let i = 0; i < this.mBCreditCardDetails.length; i++) {
                total += this.mBCreditCardDetails[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    getSumDetailTaxs(prop) {
        let total = 0;
        if (this.mBCreditCardDetailTax) {
            for (let i = 0; i < this.mBCreditCardDetailTax.length; i++) {
                total += this.mBCreditCardDetailTax[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    getVATRate(vAT) {
        if (vAT === 0) {
            return '0%';
        } else if (vAT === 1) {
            return '5%';
        } else if (vAT === 2) {
            return '10%';
        } else if (vAT === 3) {
            return 'Không chịu thuế';
        } else if (vAT === 4) {
            return 'Không tính thuế';
        } else {
            return '';
        }
    }

    registerLockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('lockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
            this.loadAll();
        });
    }

    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
            this.loadAll();
        });
    }

    checkMultiButton(isRecord: boolean) {
        for (let i = 0; i < this.selectedRows.length; i++) {
            if (this.selectedRows[i] && this.selectedRows[i].recorded === isRecord) {
                return true;
            }
        }
        return false;
    }
}
