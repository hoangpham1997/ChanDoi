import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { Principal } from 'app/core';

import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { MBTellerPaperService } from './mb-teller-paper.service';
import { IMBTellerPaperDetails } from 'app/shared/model/mb-teller-paper-details.model';
import { IMBTellerPaperDetailTax } from 'app/shared/model/mb-teller-paper-detail-tax.model';
import { MBTellerPaperDetailsService } from 'app/TienMatNganHang/BaoNo/mb-teller-paper-details';
import { MBTellerPaperDetailTaxService } from 'app/TienMatNganHang/BaoNo/mb-teller-paper-detail-tax';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { ICurrency } from 'app/shared/model/currency.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IType } from 'app/shared/model/type.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { CurrencyService } from 'app/danhmuc/currency';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { TypeService } from 'app/entities/type';
import * as moment from 'moment';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import {
    DDSo_NgoaiTe,
    DDSo_TienVND,
    DDSo_TyLe,
    MSGERROR,
    PPINVOICE_COMPONENT_TYPE,
    SO_LAM_VIEC,
    TCKHAC_MauCTuChuaGS,
    TypeID
} from 'app/app.constants';
import { ROLE } from 'app/role.constants';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { ExpenseItemService } from 'app/entities/expense-item';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { EMContractService } from 'app/entities/em-contract';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { CostSetService } from 'app/entities/cost-set';
import { BudgetItemService } from 'app/entities/budget-item';
import { IRefVoucher } from 'app/shared/model/ref-voucher.model';
import { PPInvoiceService } from 'app/muahang/mua_hang_qua_kho';
import { IPPInvoice } from 'app/shared/model/pp-invoice.model';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { IPPInvoiceDetailCost } from 'app/shared/model/pp-invoice-detail-cost.model';
import { MuaDichVuService } from 'app/muahang/mua-dich-vu/mua-dich-vu.service';
import { IMuaDichVuResult, MuaDichVuResult } from 'app/muahang/mua-dich-vu/model/mua-dich-vu-result.model';
import { IPPPayVendorBill } from 'app/shared/model/pp-pay-vendor-bill';
import { IMBTellerPaperDetailVendor } from 'app/shared/model/mb-teller-paper-detail-vendor.model';
import { IMCPaymentDetailVendor } from 'app/shared/model/mc-payment-detail-vendor.model';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IMBCreditCard } from 'app/shared/model/mb-credit-card.model';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { GoodsServicePurchase, IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';

@Component({
    selector: 'eb-mb-teller-paper',
    templateUrl: './mb-teller-paper.component.html',
    styleUrls: ['./mb-teller-paper.component.css']
})
export class MBTellerPaperComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    @ViewChild('unRecordModalPPService') unRecordModalPPService: any;
    @ViewChild('unRecordModalPPInvoice') unRecordModalPPInvoice: any;
    @ViewChild('checkHadPaidModal') checkHadPaidModal: any;
    @ViewChild('checkHadReferencePaidModal') checkHadReferencePaidModal: any;
    currentAccount: any;
    mBTellerPapers: IMBTellerPaper[];
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
    selectedRow: IMBTellerPaper;
    isShowSearch: boolean;
    mBTellerPaperDetails: IMBTellerPaperDetails[];
    mBTellerPaperDetailTaxs: IMBTellerPaperDetailTax[];
    pageCount: any;
    accountingObjectName: string;
    record_: Irecord;
    rowNum: number;
    search: ISearchVoucher;
    currencys: ICurrency[]; //
    accountingObjects: IAccountingObject[];
    types: IType[];
    allTypes: IType[];
    dataSession: IDataSessionStorage;
    isErrorInvalid: any;
    viewVouchersSelected: any;
    statisticsCodes: IStatisticsCode[];
    costSets: ICostSet[];
    expenseItems: IExpenseItem[];
    eMContracts: IEMContract[];
    organizationUnits: IOrganizationUnit[];
    budgetItems: IBudgetItem[];
    listRecord: any[];
    currencyCode: string;
    ppInvoice: IPPInvoice;
    isSoTaiChinh: boolean;
    modalRef: NgbModalRef;
    typeMultiAction: number;
    isShowGoodsServicePurchase: boolean;
    goodsServicePurchases: IGoodsServicePurchase[];

    BAO_NO = 1234;
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
    muaDichVuResult: MuaDichVuResult;
    currentBook: any;
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
    /*Mua hàng qua kho*/
    // Trả tiền NCC
    mBTellerPaperDetailVendors: IMBTellerPaperDetailVendor[];
    DDSo_NgoaiTe = DDSo_NgoaiTe;
    DDSo_TienVND = DDSo_TienVND;
    DDSo_TyLe = DDSo_TyLe;
    pPInvoiceID: string;
    pPServiceID: string;
    // role
    ROLE_BaoNo_Xem = ROLE.BaoNo_Xem;
    ROLE_BaoNo_Them = ROLE.BaoNo_Them;
    ROLE_BaoNo_Xoa = ROLE.BaoNo_Xoa;
    ROLE_BaoNo_GhiSo = ROLE.BaoNo_GhiSo;
    ROLE_BaoNo_KetXuat = ROLE.BaoNo_KetXuat;

    isRoleMuaDichVu: boolean;
    isRoleMuaHangQuaKho: boolean;
    isRoleMuaHangKhongQuaKho: boolean;
    ROLE_MUA_DICH_VU_XEM = ROLE.MuaDichVu_Xem;
    ROLE_MUA_HANG_QUA_KHO_XEM = ROLE.MuaHangQuaKho_Xem;
    ROLE_MUA_HANG_KHONG_QUA_KHO_XEM = ROLE.MuaHangKhongQuaKho_Xem;

    constructor(
        private mBTellerPaperService: MBTellerPaperService,
        private mBTellerPaperDetailsService: MBTellerPaperDetailsService,
        private mBTellerPaperDetailTaxService: MBTellerPaperDetailTaxService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private gLService: GeneralLedgerService,
        private accountingObjectService: AccountingObjectService,
        private currencyService: CurrencyService,
        public utilsService: UtilsService,
        private typeService: TypeService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private expenseItemsService: ExpenseItemService,
        private statisticsCodeService: StatisticsCodeService,
        private eMContractService: EMContractService,
        private organizationUnitService: OrganizationUnitService,
        private costSetService: CostSetService,
        private budgetItemService: BudgetItemService,
        private refModalService: RefModalService,
        private muaDichVuService: MuaDichVuService,
        private pPInvoiceService: PPInvoiceService,
        private viewVoucherService: ViewVoucherService,
        private modalService: NgbModal,
        public activeModal: NgbActiveModal,
        private ppServiceService: MuaDichVuService,
        private goodsServicePurchaseService: GoodsServicePurchaseService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            // this.predicate = datathis.translateService.instant.pagingParams.predicate;
            // add by mran
            this.predicate = 'date';
        });
        this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.allTypes = res.body;
            this.types = res.body
                .filter(a => a.typeGroupID === 12 || a.typeGroupID === 13 || a.typeGroupID === 14)
                .sort((a, b) => (a.typeName > b.typeName ? 1 : -1));
        });
        this.translate.get(['ebwebApp.mBTellerPaper.searchForm.record', 'ebwebApp.mBTellerPaper.searchForm.unrecord']).subscribe(res => {
            this.listRecord = [
                { value: true, name: res['ebwebApp.mBTellerPaper.searchForm.record'] },
                { value: false, name: res['ebwebApp.mBTellerPaper.searchForm.unrecord'] }
            ];
        });
        this.selectedRow = {};
        this.muaDichVuResult = {};
        /*mua hang qua kho*/
        this.ppInvoiceDetails = [];
        this.ppInvoiceDetailCost = [];
        this.refVouchers = [];
        /*trả tiền nhà cung cấp*/
        this.mBTellerPaperDetailVendors = [];
        /*trả tiền nhà cung cấp*/
    }

    saveSearchVoucher(): void {
        const searchObj: ISearchVoucher = {};
        searchObj.typeID = this.search.typeID !== undefined ? this.search.typeID : null;
        searchObj.statusRecorded = this.search.statusRecorded !== undefined ? this.search.statusRecorded : null;
        searchObj.currencyID = this.search.currencyID !== undefined ? this.search.currencyID : null;
        searchObj.accountingObjectID = this.search.accountingObjectID !== undefined ? this.search.accountingObjectID : null;
        searchObj.fromDate = this.search.fromDate !== undefined ? this.search.fromDate : null;
        searchObj.toDate = this.search.toDate !== undefined ? this.search.toDate : null;
        searchObj.textSearch = this.search.textSearch !== undefined ? this.search.textSearch : null;
    }

    isCheckingSearchVoucher(): boolean {
        this.saveSearchVoucher();
        const objNull: ISearchVoucher = {
            accountingObjectID: null,
            statusRecorded: null,
            typeID: null,
            currencyID: null,
            fromDate: null,
            textSearch: null,
            toDate: null
        };
        return !this.utilsService.isEquivalent(this.search, objNull);
    }

    private convertDateFromClient(searchVoucher: ISearchVoucher): ISearchVoucher {
        const copy: ISearchVoucher = Object.assign({}, searchVoucher, {
            fromDate:
                searchVoucher.fromDate != null && searchVoucher.fromDate.isValid() ? searchVoucher.fromDate.format(DATE_FORMAT) : null,
            toDate: searchVoucher.toDate != null && searchVoucher.toDate.isValid() ? searchVoucher.toDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    loadAllForSearch() {
        if (this.checkToDateGreaterFromDate()) {
            this.dataSession.searchVoucher = JSON.stringify(this.search);
            this.page = 1;
            this.searchAll();
        }
    }

    resetSearch() {
        this.search = new class implements ISearchVoucher {
            accountingObjectID: string;
            currencyID: string;
            fromDate: moment.Moment;
            statusRecorded: boolean;
            textSearch: string;
            toDate: moment.Moment;
            typeID: number;
        }();
        this.search.accountingObjectID = null;
        this.search.statusRecorded = null;
        this.search.typeID = null;
        this.search.currencyID = null;
        this.search.fromDate = null;
        this.search.textSearch = null;
        this.search.toDate = null;
        this.loadAll();
    }

    searchAll() {
        if (sessionStorage.getItem('dataSession')) {
            this.dataSession = JSON.parse(sessionStorage.getItem('dataSession'));
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.isShowSearch = this.dataSession.isShowSearch;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
            this.previousPage = this.dataSession.page;
            this.search = this.dataSession.searchVoucher ? JSON.parse(this.dataSession.searchVoucher) : null;
            sessionStorage.removeItem('dataSession');
        }
        this.mBTellerPaperService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                searchVoucher: JSON.stringify(this.search)
            })
            .subscribe(
                (res: HttpResponse<IMBTellerPaper[]>) => {
                    if (this.isSoTaiChinh) {
                        this.paginateMBTellerPapers(res.body.filter(x => x.typeLedger === 0 || x.typeLedger === 2), res.headers);
                    } else {
                        this.paginateMBTellerPapers(res.body.filter(x => x.typeLedger === 1 || x.typeLedger === 2), res.headers);
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
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
            this.search = JSON.parse(this.dataSession.searchVoucher);
            sessionStorage.removeItem('dataSession');
        }
        this.mBTellerPaperService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                searchVoucher: this.dataSession.searchVoucher
            })
            .subscribe(
                (res: HttpResponse<IMBTellerPaper[]>) => {
                    if (this.isSoTaiChinh) {
                        this.paginateMBTellerPapers(res.body.filter(x => x.typeLedger === 0 || x.typeLedger === 2), res.headers);
                    } else {
                        this.paginateMBTellerPapers(res.body.filter(x => x.typeLedger === 1 || x.typeLedger === 2), res.headers);
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.search = new class implements ISearchVoucher {
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
        // this.dataSession = {};
        this.isShowSearch = false;
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (this.currentAccount) {
                this.isRoleMuaDichVu = this.currentAccount.authorities.includes(this.ROLE_MUA_DICH_VU_XEM);
                this.isRoleMuaHangQuaKho = this.currentAccount.authorities.includes(this.ROLE_MUA_HANG_QUA_KHO_XEM);
                this.isRoleMuaHangKhongQuaKho = this.currentAccount.authorities.includes(this.ROLE_MUA_HANG_KHONG_QUA_KHO_XEM);
                if (account.organizationUnit.taxCalculationMethod === 0) {
                    this.isShowGoodsServicePurchase = true;
                } else {
                    this.isShowGoodsServicePurchase = false;
                }
                this.currencyCode = this.currentAccount.organizationUnit.currencyID;
                this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                this.currentBook = this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                this.color = this.currentAccount.systemOption.find(x => x.code === TCKHAC_MauCTuChuaGS && x.data).data;
                if (this.isCheckingSearchVoucher()) {
                    this.searchAll();
                } else {
                    this.loadAll();
                }
            }
        });
        this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjects = res.body
                .filter(a => a.isActive)
                .sort((a, b) => (a.accountingObjectCode > b.accountingObjectCode ? 1 : -1));
        });
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencys = res.body.filter(x => x.isActive).sort((a, b) => (a.currencyCode > b.currencyCode ? 1 : -1));
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
            this.budgetItems = res.body;
        });
        this.goodsServicePurchaseService.getGoodServicePurchases().subscribe((res: HttpResponse<IGoodsServicePurchase[]>) => {
            this.goodsServicePurchases = res.body;
        });
        this.registerChangeInMBTellerPapers();
        this.registerChangeSession();
        this.registerExport();
        this.registerLockSuccess();
        this.registerUnlockSuccess();
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

    doubleClickRow(id: string) {
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.pageCount = this.pageCount;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.mBTellerPapers.indexOf(this.selectedRow);
        this.dataSession.isShowSearch = this.isShowSearch;
        this.dataSession.searchVoucher = JSON.stringify(this.search);
        // sort
        this.dataSession.predicate = this.predicate;
        this.dataSession.accountingObjectName = this.accountingObjectName;
        this.dataSession.reverse = this.reverse;
        sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
        // this.router.navigate(['./mb-teller-paper', id, 'edit']);
        switch (this.selectedRow.typeId) {
            case this.TYPE_BAONO_UNC:
            case this.TYPE_BAONO_SCK:
            case this.TYPE_BAONO_STM:
            case this.TYPE_UNC_TRA_TIEN_NCC:
            case this.TYPE_SCK_TRA_TIEN_NCC:
            case this.TYPE_STM_TRA_TIEN_NCC:
                this.router.navigate(['./mb-teller-paper', this.selectedRow.id, 'edit']);
                break;
            case this.TYPE_UNC_PPSERVICE:
            case this.TYPE_SCK_PPSERVICE:
            case this.TYPE_STM_PPSERVICE:
                if (this.isRoleMuaDichVu || this.currentAccount.authorities.includes('ROLE_ADMIN')) {
                    this.mBTellerPaperService.find(this.selectedRow.id).subscribe((res: HttpResponse<IMBTellerPaper>) => {
                        const ppServiceID = res.body.ppServiceID;
                        if (ppServiceID) {
                            this.router.navigate(['./mua-dich-vu', ppServiceID, 'edit', 'from-mb-teller-paper', this.selectedRow.id]);
                        }
                    });
                } else {
                    this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
                    return false;
                }
                break;
            case this.TYPE_UNC_PPINVOICE_MHQK:
            case this.TYPE_SCK_PPINVOICE_MHQK:
            case this.TYPE_STM_PPINVOICE_MHQK:
                this.mBTellerPaperService.find(this.selectedRow.id).subscribe((res: HttpResponse<IMBTellerPaper>) => {
                    const ppInvocieID = res.body.ppInvocieID;
                    if (
                        res.body.storedInRepository &&
                        (this.isRoleMuaHangQuaKho || this.currentAccount.authorities.includes('ROLE_ADMIN'))
                    ) {
                        this.router.navigate(['./mua-hang', 'qua-kho', ppInvocieID, 'edit', 'from-mb-teller-paper', this.selectedRow.id]);
                    } else if (
                        !res.body.storedInRepository &&
                        (this.isRoleMuaHangKhongQuaKho || this.currentAccount.authorities.includes('ROLE_ADMIN'))
                    ) {
                        this.router.navigate([
                            './mua-hang',
                            'khong-qua-kho',
                            ppInvocieID,
                            'edit',
                            'from-mb-teller-paper',
                            this.selectedRow.id
                        ]);
                    } else {
                        this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
                        return false;
                    }
                });
                break;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoNo_Them])
    addNew(event) {
        event.preventDefault();
        this.router.navigate(['/mb-teller-paper/new']);
    }

    onSelect(select: IMBTellerPaper) {
        this.selectedRow = select;
        this.loadDetails();
    }

    loadDetails() {
        switch (this.selectedRow.typeId) {
            case this.TYPE_BAONO_UNC:
            case this.TYPE_BAONO_SCK:
            case this.TYPE_BAONO_STM:
            case this.TYPE_UNC_TRA_TIEN_NCC:
            case this.TYPE_SCK_TRA_TIEN_NCC:
            case this.TYPE_STM_TRA_TIEN_NCC:
                this.mBTellerPaperService.find(this.selectedRow.id).subscribe((res: HttpResponse<IMBTellerPaper>) => {
                    this.mBTellerPaperDetails =
                        res.body.mBTellerPaperDetails === undefined
                            ? []
                            : res.body.mBTellerPaperDetails.sort((a, b) => a.orderPriority - b.orderPriority);
                    this.mBTellerPaperDetailTaxs =
                        res.body.mBTellerPaperDetailTaxs === undefined
                            ? []
                            : res.body.mBTellerPaperDetailTaxs.sort((a, b) => a.orderPriority - b.orderPriority);
                    this.viewVouchersSelected = res.body.viewVouchers;
                    this.mBTellerPaperDetailVendors =
                        res.body.mBTellerPaperDetailVendor === undefined
                            ? []
                            : res.body.mBTellerPaperDetailVendor.sort((a, b) => a.orderPriority - b.orderPriority);
                    this.viewVouchersSelected = res.body.viewVouchers === undefined ? [] : res.body.viewVouchers;
                });
                break;
            case this.TYPE_UNC_PPSERVICE:
            case this.TYPE_SCK_PPSERVICE:
            case this.TYPE_STM_PPSERVICE:
                this.findMuaDichVu(this.selectedRow.id);
                break;
            case this.TYPE_UNC_PPINVOICE_MHQK:
            case this.TYPE_SCK_PPINVOICE_MHQK:
            case this.TYPE_STM_PPINVOICE_MHQK:
                this.findMuaHangQuaKho(this.selectedRow.id);
                break;
        }
    }

    findMuaDichVu(id) {
        this.muaDichVuService.findAllPPServiceDetailsByPaymentVoucherID({ paymentVoucherID: id }).subscribe(resDetail => {
            this.muaDichVuResult.ppServiceDetailDTOS = resDetail.body ? resDetail.body : [];
            //
            this.muaDichVuResult.ppServiceDetailDTOS.forEach(detail => (detail.quantityFromDB = detail.quantity));
        });
        this.muaDichVuService
            .findRefVoucherByByPaymentVoucherID({
                typeID: this.selectedRow.typeId,
                paymentVoucherID: id,
                currentBook: this.currentBook
            })
            .subscribe(resDetail => (this.muaDichVuResult.refVouchers = resDetail.body ? resDetail.body : []));
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
                typeID: this.selectedRow.typeId,
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

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    transition() {
        // this.router.navigate(['/mb-teller-paper'], {
        //     queryParams: {
        //         page: this.page,
        //         size: this.itemsPerPage,
        //         sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        //     }
        // });
        this.router.navigate(['/mb-teller-paper']);
        this.dataSession.rowNum = 0;
        if (this.dataSession.searchVoucher) {
            this.searchAll();
        } else {
            this.loadAll();
        }
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/mb-teller-paper',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    // ngOnDestroy() {
    //     this.eventManager.destroy(this.eventSubscriber);
    // }

    // ghi so
    @ebAuth(['ROLE_ADMIN', ROLE.BaoNo_GhiSo])
    record() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 2;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (!this.selectedRow.recorded && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
                this.record_ = {};
                this.record_.id = this.selectedRow.id;
                this.record_.typeID = this.selectedRow.typeId;
                // let loading;
                switch (this.selectedRow.typeId) {
                    case this.TYPE_BAONO_UNC:
                    case this.TYPE_BAONO_SCK:
                    case this.TYPE_BAONO_STM:
                    case this.TYPE_UNC_TRA_TIEN_NCC:
                    case this.TYPE_SCK_TRA_TIEN_NCC:
                    case this.TYPE_STM_TRA_TIEN_NCC:
                        this.gLService.record(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                            // console.log(JSON.stringify(res.body));
                            // this.toastr.remove(loading.toastId);
                            if (res.body.success === true) {
                                this.selectedRow.recorded = true;
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
                        break;
                    case this.TYPE_UNC_PPSERVICE:
                    case this.TYPE_SCK_PPSERVICE:
                    case this.TYPE_STM_PPSERVICE:
                        this.mBTellerPaperService.find(this.selectedRow.id).subscribe((res: HttpResponse<any>) => {
                            const ppServiceID = res.body.ppServiceID;
                            if (ppServiceID) {
                                this.record_.id = ppServiceID;
                                this.record_.typeID = this.TYPE_PP_SERVICE;
                                this.gLService.record(this.record_).subscribe((res2: HttpResponse<Irecord>) => {
                                    // console.log(JSON.stringify(res.body));
                                    // this.toastr.remove(loading.toastId);
                                    if (res2.body.success === true) {
                                        this.selectedRow.recorded = true;
                                        this.toastr.success(
                                            this.translate.instant('ebwebApp.mBTellerPaper.recordToast.done'),
                                            this.translate.instant('ebwebApp.mBTellerPaper.message.title')
                                        );
                                    } else {
                                        this.toastr.error(
                                            this.translate.instant('ebwebApp.mBTellerPaper.recordToast.failed'),
                                            this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                                        );
                                    }
                                });
                            }
                        });
                        break;
                    case this.TYPE_UNC_PPINVOICE_MHQK:
                    case this.TYPE_SCK_PPINVOICE_MHQK:
                    case this.TYPE_STM_PPINVOICE_MHQK:
                        this.mBTellerPaperService.find(this.selectedRow.id).subscribe((res: HttpResponse<any>) => {
                            const ppInvocieID = res.body.ppInvocieID;
                            if (ppInvocieID) {
                                this.record_.id = ppInvocieID;
                                this.record_.typeID = this.TYPE_PP_INVOICE;
                                this.gLService.record(this.record_).subscribe((resRc: HttpResponse<Irecord>) => {
                                    console.log(JSON.stringify(res.body));
                                    if (resRc.body.success) {
                                        this.selectedRow.recorded = true;
                                        this.toastr.success(
                                            this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
                                            this.translate.instant('ebwebApp.mBCreditCard.message')
                                        );
                                    }
                                });
                            }
                        });
                        break;
                }
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoNo_GhiSo])
    unrecord() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 1;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (this.selectedRow.recorded && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
                this.record_ = {};
                this.record_.id = this.selectedRow.id;
                this.record_.typeID = this.selectedRow.typeId;
                // const loading = this.toastr.success(
                //     this.translate.instant('ebwebApp.mBTellerPaper.unrecordToast.unrecording'),
                //     this.translate.instant('ebwebApp.mBTellerPaper.message.title')
                // );
                switch (this.selectedRow.typeId) {
                    case this.TYPE_BAONO_UNC:
                    case this.TYPE_BAONO_SCK:
                    case this.TYPE_BAONO_STM:
                    case this.TYPE_UNC_TRA_TIEN_NCC:
                    case this.TYPE_SCK_TRA_TIEN_NCC:
                    case this.TYPE_STM_TRA_TIEN_NCC:
                        this.gLService.unrecord(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                            // console.log(JSON.stringify(res.body));
                            // this.toastr.remove(loading.toastId);
                            if (res.body.success === true) {
                                this.selectedRow.recorded = false;
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
                        break;
                    case this.TYPE_UNC_PPSERVICE:
                    case this.TYPE_SCK_PPSERVICE:
                    case this.TYPE_STM_PPSERVICE:
                        this.mBTellerPaperService.find(this.selectedRow.id).subscribe((res: HttpResponse<any>) => {
                            const ppServiceID = res.body.ppServiceID;
                            if (ppServiceID) {
                                this.pPServiceID = ppServiceID;
                                this.ppServiceService.checkHadReference({ ppServiceId: this.pPServiceID }).subscribe(res2 => {
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
                            }
                        });
                        break;
                    case this.TYPE_UNC_PPINVOICE_MHQK:
                    case this.TYPE_SCK_PPINVOICE_MHQK:
                    case this.TYPE_STM_PPINVOICE_MHQK:
                        this.mBTellerPaperService.find(this.selectedRow.id).subscribe((res: HttpResponse<any>) => {
                            const ppInvocieID = res.body.ppInvocieID;
                            if (ppInvocieID) {
                                this.pPInvoiceID = ppInvocieID;
                                this.pPInvoiceService.findById({ id: this.pPInvoiceID }).subscribe((res2: HttpResponse<any>) => {
                                    this.ppInvoice = res2.body;
                                    this.pPInvoiceService.checkUnRecord({ id: this.pPInvoiceID }).subscribe(
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
        this.record_.id = this.pPInvoiceID;
        this.record_.typeID = this.TYPE_PP_INVOICE;
        this.gLService.unrecord(this.record_).subscribe((resURc: HttpResponse<Irecord>) => {
            if (resURc.body.success) {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.selectedRows = [];
                this.toastr.success(
                    this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                    this.translate.instant('ebwebApp.mBCreditCard.message')
                );
                this.loadAll();
            }
        });
    }

    unRecordPPService() {
        if (this.pPServiceID) {
            this.record_.id = this.pPServiceID;
            this.record_.typeID = this.TYPE_PP_SERVICE;
            this.gLService.unrecord(this.record_).subscribe((resURc: HttpResponse<Irecord>) => {
                if (resURc.body.success) {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    this.selectedRows = [];
                    this.toastr.success(
                        this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                        this.translate.instant('ebwebApp.mBCreditCard.message')
                    );
                    this.loadAll();
                }
            });
        }
    }

    openModal(content) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(content, { backdrop: 'static' });
    }

    unRecordOld() {
        //     this.record_.id = ppServiceID;
        //     this.record_.typeID = this.TYPE_PP_SERVICE;
        //     this.gLService.unrecord(this.record_).subscribe((res2: HttpResponse<Irecord>) => {
        //     // console.log(JSON.stringify(res.body));
        //     this.toastr.remove(loading.toastId);
        //     if (res2.body.success === true) {
        //     this.selectedRow.recorded = false;
        //     this.toastr.success(
        //         this.translate.instant('ebwebApp.mBTellerPaper.unrecordToast.done'),
        //     this.translate.instant('ebwebApp.mBTellerPaper.message.title')
        // );
        // } else {
        //     this.toastr.error(
        //         this.translate.instant('ebwebApp.mBTellerPaper.unrecordToast.failed'),
        //         this.translate.instant('ebwebApp.mBTellerPaper.error.error')
        //     );
        // }
        // });
    }

    trackId(index: number, item: IMBTellerPaper) {
        return item.id;
    }

    trackMBTellerPaperDetailsById(index: number, item: IMBTellerPaperDetails) {
        return item.id;
    }

    trackMBTellerPaperDetailTaxById(index: number, item: IMBTellerPaperDetailTax) {
        return item.id;
    }

    registerChangeInMBTellerPapers() {
        this.eventSubscriber = this.eventManager.subscribe('mBTellerPaperListModification', response => this.loadAll());
        this.eventSubscribers.push(this.eventSubscriber);
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    selectedItemPerPage() {
        if (this.isCheckingSearchVoucher()) {
            this.searchAll();
        } else {
            this.loadAll();
        }
    }

    getTypeByTypeID(typeID: number): string {
        const type = this.types.find(n => n.id === typeID);
        if (type !== undefined && type !== null) {
            return type.typeName;
        } else {
            return '';
        }
    }

    private paginateMBTellerPapers(data: IMBTellerPaper[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        if (data && data.length === 0 && this.page > 1) {
            this.page = this.page - 1;
            this.loadAll();
            this.router.navigate(['/mb-teller-paper'], {
                queryParams: {
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
            });
        }
        this.mBTellerPapers = data;
        this.objects = data;
        //  load first element
        if (this.dataSession && this.dataSession.rowNum) {
            this.selectedRows.push(this.mBTellerPapers[this.dataSession.rowNum]);
            this.selectedRow = this.mBTellerPapers[this.dataSession.rowNum];
        } else {
            this.selectedRows.push(this.mBTellerPapers[0]);
            this.selectedRow = this.mBTellerPapers[0];
        }
        // console.log(this.selectedRow);
        if (this.selectedRow) {
            this.loadDetails();
        } else {
            this.mBTellerPaperDetails = null;
            this.mBTellerPaperDetailTaxs = null;
            this.viewVouchersSelected = null;
        }
        //  display pageCount
        // this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    transformAmount(value: number) {
        const amount = parseFloat(value.toString().replace(/,/g, ''))
            .toFixed(2)
            .toString()
            .replace('.', ',')
            .replace(/\B(?=(\d{3})+(?!\d))/g, '.');
        return amount;
    }

    checkToDateGreaterFromDate(): boolean {
        if (this.search.fromDate && this.search.toDate && this.search.toDate < this.search.fromDate) {
            this.isErrorInvalid = true;
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.fromDateGreaterToDate'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        } else {
            this.isErrorInvalid = false;
            return true;
        }
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
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
        if (this.organizationUnits) {
            const orgUnit = this.organizationUnits.find(x => x.id === detail.departmentID);
            if (orgUnit) {
                return orgUnit ? orgUnit.organizationUnitCode : '';
            } else {
                return '';
            }
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
        const statisticsCode = this.statisticsCodes.find(x => x.id === detail.statisticsCodeID);
        if (statisticsCode) {
            return statisticsCode ? statisticsCode.statisticsCodeName : '';
        } else {
            return '';
        }
    }

    getTotalAmountOriginal() {
        return this.selectedRow ? this.selectedRow.totalAmountOriginal : 0;
    }

    getTotalAmount() {
        return this.selectedRow ? this.selectedRow.totalAmount : 0;
    }

    getVATRate(detail: IMBTellerPaperDetailTax): string {
        if (detail.vATRate === 0) {
            return '0%';
        } else if (detail.vATRate === 1) {
            return '5%';
        } else if (detail.vATRate === 2) {
            return '10%';
        } else if (detail.vATRate === -1) {
            return 'Không chịu thuế';
        } else if (detail.vATRate === -2) {
            return 'Không tính thuế';
        } else {
            return '';
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoNo_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1 && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
            this.typeMultiAction = 0;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (this.selectedRow && !this.selectedRow.recorded) {
                this.router.navigate(['/mb-teller-paper', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
            }
        }
    }

    /*Mua dịch vụ*/
    trackIdentity(index, item: IMuaDichVuResult) {
        return item.receiptDate;
    }

    getVATRateValue(vatRate: number) {
        if (vatRate === 0) {
            return '0%';
        } else if (vatRate === 1) {
            return '5%';
        } else if (vatRate === 2) {
            return '10%';
        } else if (vatRate === -1) {
            return 'Không chịu thuế';
        } else if (vatRate === -2) {
            return 'Không tính thuế';
        } else {
            return '';
        }
    }

    getCurrencyType(type) {
        if (this.currentAccount.organizationUnit.currencyID === 'VND') {
            if (this.selectedRow.currencyId === 'VND') {
                return 1;
            } else {
                return type === 1 ? 2 : 1;
            }
        } else {
            if (this.selectedRow.currencyId === 'VND') {
                return type === 1 ? 1 : 2;
            } else {
                return 2;
            }
        }
    }

    getAmountOriginalType(detail: any) {
        if (this.isForeignCurrency(detail)) {
            return 8;
        }
        return 7;
    }

    isForeignCurrency(detail) {
        return this.currentAccount && detail.currencyId !== this.currentAccount.organizationUnit.currencyID;
    }

    registerChangeSession() {
        this.eventManager.subscribe('changeSession', response => {
            this.principal.identity(true).then(account => {
                this.currentAccount = account;
                if (account) {
                    this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                    this.currencyCode = this.currentAccount.organizationUnit.currencyID;
                    if (this.isCheckingSearchVoucher()) {
                        this.searchAll();
                    } else {
                        this.loadAll();
                    }
                }
            });
        });
    }

    exportExcel() {
        this.mBTellerPaperService
            .exportExcel({
                searchVoucher: JSON.stringify(this.search)
            })
            .subscribe(res => {
                const blob = new Blob([res.body], { type: 'application/pdf' });
                const fileURL = URL.createObjectURL(blob);
                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_BaoNo.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            });
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoNo_KetXuat])
    export() {
        event.preventDefault();
        if (this.mBTellerPapers && this.mBTellerPapers.length > 0) {
            const loading = this.toastr.success(this.translate.instant('ebwebApp.mBTellerPaper.exportToast.exporting'));
            this.mBTellerPaperService
                .export({
                    searchVoucher: JSON.stringify(this.search)
                })
                .subscribe(res => {
                    this.toastr.remove(loading.toastId);
                    this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.BAO_NO);
                });
        }
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.BAO_NO}`, () => {
            this.exportExcel();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    /*Trả tiền nhà cung cấp*/
    summCPaymentDetailVendors(prop) {
        let total = 0;
        for (let i = 0; i < this.mBTellerPaperDetailVendors.length; i++) {
            total += this.mBTellerPaperDetailVendors[i][prop];
        }
        return isNaN(total) ? 0 : total;
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

    /*Trả tiền nhà cung cấp*/
    getUnitPriceOriginalType() {
        if (this.isForeignCurrencyFunc()) {
            return 2;
        }
        return 1;
    }

    isForeignCurrencyFunc() {
        if (this.selectedRow) {
            return this.currentAccount && this.selectedRow.currencyId !== this.currentAccount.organizationUnit.currencyID;
        }
    }

    sum(prop) {
        let total = 0;
        if (this.muaDichVuResult && this.muaDichVuResult.ppServiceDetailDTOS) {
            for (let i = 0; i < this.muaDichVuResult.ppServiceDetailDTOS.length; i++) {
                total += this.muaDichVuResult.ppServiceDetailDTOS[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoNo_Xem])
    edit() {
        event.preventDefault();
        if (!this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
            if (this.selectedRow) {
                this.doubleClickRow(this.selectedRow.id);
            }
        }
    }

    registerLockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('lockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
            this.loadAll();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
            this.loadAll();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    checkMultiButton(isRecord: boolean) {
        if (this.selectedRows) {
            for (let i = 0; i < this.selectedRows.length; i++) {
                if (this.selectedRows[i] && this.selectedRows[i].recorded === isRecord) {
                    return true;
                }
            }
            return false;
        }
    }

    continueDelete() {
        if (this.typeMultiAction === 0) {
            this.mBTellerPaperService.multiDelete(this.selectedRows).subscribe(
                (res: HttpResponse<any>) => {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    this.selectedRows = [];
                    this.loadAll();
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
            this.mBTellerPaperService.multiUnrecord(this.selectedRows).subscribe(
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
                    this.loadAll();
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
                return { id: n.id, typeID: n.typeId };
            });
            rq.typeIDMain = this.TYPE_BAONO_UNC;
            this.gLService.recordList(rq).subscribe((res: HttpResponse<HandlingResult>) => {
                this.selectedRows.forEach(slr => {
                    const update = this.mBTellerPapers.find(
                        n => n.id === slr.id && !res.body.listFail.some(m => m.id === slr.id || m.mBTellerPaperID === slr.id)
                    );
                    if (update) {
                        update.recorded = true;
                    }
                });
                if (res.body.countFailVouchers > 0) {
                    res.body.listFail.forEach(n => {
                        if (n.mBTellerPaperID) {
                            const mc = this.mBTellerPapers.find(m => m.id === n.mBTellerPaperID);
                            const type = this.types.find(t => t.id === mc.typeId);
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
        }
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

    getGoodsServicePurchaseIDbyID(id) {
        if (this.goodsServicePurchases) {
            const goodsServicePurchase = this.goodsServicePurchases.find(n => n.id === id);
            if (goodsServicePurchase) {
                return goodsServicePurchase.goodsServicePurchaseCode;
            }
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

    getSumDetailTaxs(prop) {
        let total = 0;
        if (this.mBTellerPaperDetailTaxs) {
            for (let i = 0; i < this.mBTellerPaperDetailTaxs.length; i++) {
                total += this.mBTellerPaperDetailTaxs[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }
}
