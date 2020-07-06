import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { IMCPayment } from 'app/shared/model/mc-payment.model';
import { Principal } from 'app/core';
import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { MCPaymentService } from './mc-payment.service';
import { IMCPaymentDetails } from 'app/shared/model/mc-payment-details.model';
import { IMCPaymentDetailTax } from 'app/shared/model/mc-payment-detail-tax.model';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { CurrencyService } from 'app/danhmuc/currency';
import { ICurrency } from 'app/shared/model/currency.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IType } from 'app/shared/model/type.model';
import { TypeService } from 'app/entities/type';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { EMContractService } from 'app/entities/em-contract';
import * as moment from 'moment';
import {
    DDSo_NgoaiTe,
    DDSo_TienVND,
    DDSo_TyLe,
    MSGERROR,
    MultiAction,
    PPINVOICE_COMPONENT_TYPE,
    SO_LAM_VIEC,
    TCKHAC_MauCTuChuaGS,
    TypeID
} from 'app/app.constants';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { IMuaDichVuResult, MuaDichVuResult } from 'app/muahang/mua-dich-vu/model/mua-dich-vu-result.model';
import { MuaDichVuService } from 'app/muahang/mua-dich-vu/mua-dich-vu.service';
import { IRefVoucher } from 'app/shared/model/ref-voucher.model';
import { PPInvoiceService } from 'app/muahang/mua_hang_qua_kho';
import { IPPInvoiceDetailCost } from 'app/shared/model/pp-invoice-detail-cost.model';
import { IMCPaymentDetailVendor } from 'app/shared/model/mc-payment-detail-vendor.model';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { ISAOrder } from 'app/shared/model/sa-order.model';

@Component({
    selector: 'eb-mc-payment',
    templateUrl: './mc-payment.component.html',
    styleUrls: ['./mc-payment.component.css']
})
export class MCPaymentComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('unRecordModal') unRecordModal: any;
    @ViewChild('popUpMultiAction') popUpMultiAction: TemplateRef<any>;
    TYPE_MC_PAYMENT = 110;
    TYPE_MC_PAYMENT_NCC = 116;
    TYPE_MC_PAYMENT_PPSERVICE = 114;
    TYPE_MC_PAYMENT_PPINVOICE_MHQK = 115;
    private TYPE_PP_INVOICE = 210;
    private TYPE_PP_SERVICE = 240;
    private TYPE_PP_DISCOUNT_RETURN = 230;
    private TYPE_FA_INCREAMENT = 610;
    private TYPE_TI_INCREAMENT = 510;
    DDSo_NgoaiTe = DDSo_NgoaiTe;
    DDSo_TienVND = DDSo_TienVND;
    DDSo_TyLe = DDSo_TyLe;
    currentAccount: any;
    mCPayments: IMCPayment[];
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
    isShowSearch: boolean;
    hide: string;
    pageCount: any;
    search: ISearchVoucher;
    currencys: ICurrency[]; //
    accountingobjects: IAccountingObject[]; // Ä‘á»‘i tÆ°á»£ng
    mCPaymentDetails: IMCPaymentDetails[];
    mCPaymentDetailTaxs: IMCPaymentDetailTax[];
    goodsServicePurchases: IGoodsServicePurchase[];
    selectedRow: IMCPayment;
    record_: Irecord;
    types: IType[];
    eMContracts: IEMContract[];
    /*Trạng thái*/
    listRecord: any;
    listColumnsRecord: string[] = ['name'];
    listHeaderColumnsRecord: string[] = ['Trạng thái'];
    statusRecord: any;
    /**/
    viewVouchersSelected: any;
    account: any;
    isSoTaiChinh: boolean;
    currencyCode: string;

    fromDateStr: string;
    toDateStr: string;
    validateToDate: boolean;
    validateFromDate: boolean;
    currentBook: any;

    /*Mua dịch vụ*/
    muaDichVuResult: MuaDichVuResult;
    /*****/

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
    /*trả tiền nhà cung cấp*/
    mCPaymentDetailVendors: IMCPaymentDetailVendor[];

    /*trả tiền nhà cung cấp*/

    /*Phân quyền*/
    ROLE_Them = ROLE.PhieuChi_Them;
    ROLE_Sua = ROLE.PhieuChi_Sua;
    ROLE_Xoa = ROLE.PhieuChi_Xoa;
    ROLE_GhiSo = ROLE.PhieuChi_GhiSo;
    ROLE_KetXuat = ROLE.PhieuChi_KetXuat;

    isSingleClick: any;
    modalRef: NgbModalRef;
    index: number;
    idPPService: string;
    typeMultiAction: string;
    MultiAction = MultiAction;
    taxCalculationMethod: any;

    constructor(
        private mCPaymentService: MCPaymentService,
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
        private eMContractService: EMContractService,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        private refModalService: RefModalService,
        private muaDichVuService: MuaDichVuService,
        private pPInvoiceService: PPInvoiceService,
        private viewVoucherService: ViewVoucherService,
        private modalService: NgbModal
    ) {
        super();
        this.principal.identity().then(account => {
            this.account = account;
            if (account) {
                this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                this.currencyCode = this.account.organizationUnit.currencyID;
                this.currentBook = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                this.color = this.account.systemOption.find(x => x.code === TCKHAC_MauCTuChuaGS && x.data).data;
                this.taxCalculationMethod = this.account.organizationUnit.taxCalculationMethod;
            }
        });
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencys = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
        });
        this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingobjects = res.body.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
        });
        this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body.filter(type => type.typeGroupID === 11).sort((a, b) => a.typeName.localeCompare(b.typeName));
        });
        this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
            this.eMContracts = res.body.filter(n => n.isActive);
        });
        this.goodsServicePurchaseService.getGoodServicePurchases().subscribe((res: HttpResponse<IGoodsServicePurchase[]>) => {
            this.goodsServicePurchases = res.body;
        });
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.reverse = false;
            // this.predicate = data.pagingParams.predicate;

            this.predicate = 'date';
        });
        this.translate.get(['ebwebApp.mBDeposit.home.recorded', 'ebwebApp.mBDeposit.home.unrecorded']).subscribe(res => {
            this.listRecord = [
                { value: 1, name: res['ebwebApp.mBDeposit.home.recorded'] },
                { value: 2, name: res['ebwebApp.mBDeposit.home.unrecorded'] }
            ];
        });
        this.selectedRow = {};
        this.muaDichVuResult = {};
        /*mua hang qua kho*/
        this.ppInvoiceDetails = [];
        this.ppInvoiceDetailCost = [];
        this.refVouchers = [];
        /*mua hang qua kho*/
        /*trả tiền nhà cung cấp*/
        this.mCPaymentDetailVendors = [];
        /*trả tiền nhà cung cấp*/
    }

    selectedItemPerPage() {
        this.loadAll(false, true);
    }

    deleteIndexSS(): void {
        sessionStorage.removeItem('page_MCPayment');
        sessionStorage.removeItem('size_MCPayment');
        sessionStorage.removeItem('index_MCPayment');
    }

    loadAll(search?, loadPage?, isMultiAciton?) {
        this.deleteIndexSS();
        const pr = this.activatedRoute.snapshot.queryParams;
        if (pr && !loadPage && !search && !isMultiAciton) {
            if (pr.page) {
                this.page = Number(pr.page);
                this.previousPage = Number(pr.page);
            }
            if (pr.size) {
                this.itemsPerPage = Number(pr.size);
            }
            if (pr.index) {
                this.index = Number(pr.index);
            }
        } else {
            if (isMultiAciton) {
                this.index = this.mCPayments.indexOf(this.selectedRows[0]);
            } else {
                this.index = Number(pr.index);
            }
        }
        if (this.activatedRoute.snapshot.paramMap.has('isSearch')) {
            const isSearch = this.activatedRoute.snapshot.paramMap.get('isSearch');
            if (isSearch === '1') {
            } else {
                sessionStorage.removeItem('searchVoucherMCPayment');
            }
        } else {
            if (!loadPage) {
                sessionStorage.removeItem('searchVoucherMCPayment');
            }
        }
        const _search = JSON.parse(sessionStorage.getItem('searchVoucherMCPayment'));
        this.search = _search;
        if (_search) {
            if (_search.fromDate) {
                this.search.fromDate = moment(_search.fromDate);
                this.fromDateStr = this.search.fromDate.format('DD/MM/YYYY');
            }
            if (_search.toDate) {
                this.search.toDate = moment(_search.toDate);
                this.toDateStr = this.search.toDate.format('DD/MM/YYYY');
            }
        }
        if (this.search) {
            this.statusRecord = this.search.statusRecorded ? 1 : this.search.statusRecorded === false ? 2 : null;
            this.searchVoucher();
            this.isShowSearch = true;
        } else {
            this.clearSearch();
            if (search) {
                this.page = 1;
                this.previousPage = 1;
            }
            this.mCPaymentService
                .query({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<IMCPayment[]>) => {
                        this.paginateMCPayments(res.body, res.headers);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    clearSearch() {
        this.search = {};
        this.search.currencyID = null;
        this.search.accountingObjectID = null;
        this.search.statusRecorded = null;
        this.search.typeID = null;
        this.statusRecord = undefined;
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.selectedRows = [];
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        if (this.activatedRoute.snapshot.paramMap.has('isSearch')) {
            this.router.navigate(['/mc-payment', 'hasSearch', '1'], {
                queryParams: {
                    page: this.page,
                    size: this.itemsPerPage,
                    index: this.mCPayments.indexOf(this.selectedRow)
                }
            });
        } else {
            this.router.navigate(['/mc-payment'], {
                queryParams: {
                    page: this.page,
                    size: this.itemsPerPage,
                    index: this.mCPayments.indexOf(this.selectedRow)
                }
            });
        }
        this.loadAll(false, true);
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/mc-payment',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.isShowSearch = false;
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInMCPayments();
        this.registerChangeSession();
        this.registerExport();
        this.registerLockSuccess();
        this.registerUnlockSuccess();
        // this.mCPaymentDetails = [];
        // this.mCPaymentDetailTaxs = [];
    }

    /*ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }*/

    trackId(index: number, item: IMCPayment) {
        return item.id;
    }

    registerChangeInMCPayments() {
        this.eventSubscriber = this.eventManager.subscribe('mCPaymentListModification', response => this.loadAll());
        this.eventSubscribers.push(this.eventSubscriber);
    }

    sort() {
        /*const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }*/
        const result = ['date' + ',' + (this.reverse ? 'asc' : 'desc')];
        result.push('postedDate' + ',' + (this.reverse ? 'asc' : 'desc'));
        result.push('noFBook' + ',' + (this.reverse ? 'asc' : 'desc'));
        // const result = ['date, desc', 'postedDate, desc'];

        return result;
    }

    private paginateMCPayments(data: IMCPayment[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.mCPayments = data;
        this.objects = data;

        this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
        if (this.mCPayments.length > 0) {
            if (this.index) {
                const indexSAorder = this.mCPayments[this.index];
                if (indexSAorder) {
                    this.selectedRow = indexSAorder;
                } else {
                    this.selectedRow = this.mCPayments[0];
                }
            } else {
                this.selectedRow = this.mCPayments[0];
            }
            const lstSelect = this.selectedRows.map(object => ({ ...object }));
            this.selectedRows = [];
            this.selectedRows.push(...this.mCPayments.filter(n => lstSelect.some(m => m.id === n.id)));
            if (!this.selectedRows.find(n => n.id === this.selectedRow.id)) {
                this.selectedRows.push(this.selectedRow);
            }
            this.onSelect(this.selectedRow);
        } else {
            this.mCPaymentDetails = [];
            this.mCPaymentDetailTaxs = [];
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    onSelect(select: IMCPayment) {
        this.isSingleClick = true;
        setTimeout(() => {
            if (this.isSingleClick) {
                this.index = this.mCPayments.indexOf(select);
                this.selectedRow = select;
                switch (this.selectedRow.typeID) {
                    case this.TYPE_MC_PAYMENT:
                    case this.TYPE_MC_PAYMENT_NCC:
                        this.mCPaymentService.find(select.id).subscribe((res: HttpResponse<IMCPayment>) => {
                            this.mCPaymentDetails =
                                res.body.mcpaymentDetails === undefined
                                    ? []
                                    : res.body.mcpaymentDetails.sort((a, b) => a.orderPriority - b.orderPriority);
                            this.mCPaymentDetailTaxs =
                                res.body.mcpaymentDetailTaxes === undefined
                                    ? []
                                    : res.body.mcpaymentDetailTaxes.sort((a, b) => a.orderPriority - b.orderPriority);
                            this.mCPaymentDetailVendors =
                                res.body.mcpaymentDetailVendors === undefined
                                    ? []
                                    : res.body.mcpaymentDetailVendors.sort((a, b) => a.orderPriority - b.orderPriority);
                            this.viewVouchersSelected = res.body.viewVouchers;
                        });
                        break;
                    case this.TYPE_MC_PAYMENT_PPSERVICE:
                        this.findMuaDichVu(this.selectedRow.id);
                        break;
                    case this.TYPE_MC_PAYMENT_PPINVOICE_MHQK:
                        this.findMuaHangQuaKho(this.selectedRow.id);
                        break;
                }
            }
        }, 250);

        // console.log(`selectedTest1 = ${JSON.stringify(this.selectedRow)}`);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuChi_GhiSo])
    record() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = MultiAction.GHI_SO;
            this.modalRef = this.modalService.open(this.popUpMultiAction, { backdrop: 'static' });
        } else {
            if (!this.checkCloseBook(this.account, this.selectedRow.postedDate)) {
                this.record_ = {};
                switch (this.selectedRow.typeID) {
                    case this.TYPE_MC_PAYMENT:
                    case this.TYPE_MC_PAYMENT_NCC:
                        this.record_.id = this.selectedRow.id;
                        this.record_.typeID = this.selectedRow.typeID;
                        this.gLService.record(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                            console.log(JSON.stringify(res.body));
                            if (res.body.success) {
                                this.selectedRow.recorded = true;
                                this.messageRecord();
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
                        break;
                    case this.TYPE_MC_PAYMENT_PPSERVICE:
                        this.mCPaymentService.find(this.selectedRow.id).subscribe((res: HttpResponse<IMCPayment>) => {
                            const ppServiceID = res.body.ppServiceID;
                            if (ppServiceID) {
                                this.record_.id = ppServiceID;
                                this.record_.typeID = this.TYPE_PP_SERVICE;
                                this.gLService.record(this.record_).subscribe((resRc: HttpResponse<Irecord>) => {
                                    console.log(JSON.stringify(res.body));
                                    if (resRc.body.success) {
                                        this.selectedRow.recorded = true;
                                        this.messageRecord();
                                    }
                                });
                            }
                        });
                        break;
                    case this.TYPE_MC_PAYMENT_PPINVOICE_MHQK:
                        this.mCPaymentService.find(this.selectedRow.id).subscribe((res: HttpResponse<IMCPayment>) => {
                            const ppInvocieID = res.body.ppInvocieID;
                            if (ppInvocieID) {
                                this.record_.id = ppInvocieID;
                                this.record_.typeID = this.TYPE_PP_INVOICE;
                                this.gLService.record(this.record_).subscribe((resRc: HttpResponse<Irecord>) => {
                                    console.log(JSON.stringify(res.body));
                                    if (resRc.body.success) {
                                        this.selectedRow.recorded = true;
                                        this.messageRecord();
                                    }
                                });
                            }
                        });
                        break;
                }
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuChi_GhiSo])
    unrecord() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = MultiAction.BO_GHI_SO;
            this.modalRef = this.modalService.open(this.popUpMultiAction, { backdrop: 'static' });
        } else {
            if (!this.checkCloseBook(this.account, this.selectedRow.postedDate)) {
                this.record_ = {};
                switch (this.selectedRow.typeID) {
                    case this.TYPE_MC_PAYMENT:
                    case this.TYPE_MC_PAYMENT_NCC:
                        this.record_.id = this.selectedRow.id;
                        this.record_.typeID = this.selectedRow.typeID;
                        this.gLService.unrecord(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                            console.log(JSON.stringify(res.body));
                            if (res.body.success) {
                                this.selectedRow.recorded = false;
                                this.messageUnrecord();
                            }
                        });
                        break;
                    case this.TYPE_MC_PAYMENT_PPSERVICE:
                        this.mCPaymentService.find(this.selectedRow.id).subscribe((res: HttpResponse<IMCPayment>) => {
                            const ppServiceID = res.body.ppServiceID;
                            if (ppServiceID) {
                                this.record_.id = ppServiceID;
                                this.record_.typeID = this.TYPE_PP_SERVICE;
                                this.idPPService = ppServiceID;
                                this.muaDichVuService.checkHadReference({ ppServiceId: ppServiceID }).subscribe(rescheck => {
                                    if (rescheck.body.messages === UpdateDataMessages.HAD_REFERENCE) {
                                        this.openModal(this.unRecordModal);
                                    } else {
                                        this.gLService.unrecord(this.record_).subscribe((resURc: HttpResponse<Irecord>) => {
                                            console.log(JSON.stringify(res.body));
                                            if (resURc.body.success) {
                                                this.selectedRow.recorded = false;
                                                this.messageUnrecord();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                        break;
                    case this.TYPE_MC_PAYMENT_PPINVOICE_MHQK:
                        this.mCPaymentService.find(this.selectedRow.id).subscribe((res: HttpResponse<IMCPayment>) => {
                            const ppInvocieID = res.body.ppInvocieID;
                            if (ppInvocieID) {
                                this.record_.id = ppInvocieID;
                                this.record_.typeID = this.TYPE_PP_INVOICE;
                                this.gLService.unrecord(this.record_).subscribe((resURc: HttpResponse<Irecord>) => {
                                    console.log(JSON.stringify(res.body));
                                    if (resURc.body.success) {
                                        this.selectedRow.recorded = false;
                                        this.messageUnrecord();
                                    }
                                });
                            }
                        });
                        break;
                }
            }
        }
    }

    unRecording() {
        const recordData = { id: this.idPPService, typeID: this.TYPE_PP_SERVICE };
        this.gLService.unrecord(recordData).subscribe((res: HttpResponse<Irecord>) => {
            if (res.body.success === true) {
                this.toastr.success(
                    this.translate.instant('ebwebApp.mBTellerPaper.unrecordToast.done'),
                    this.translate.instant('ebwebApp.mBTellerPaper.message.title')
                );
                this.selectedRow.recorded = false;
            } else {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mBTellerPaper.unrecordToast.fail'),
                    this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                );
            }
            if (this.modalRef) {
                this.modalRef.close();
            }
        });
    }

    openModal(content) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(content, { backdrop: 'static' });
    }

    messageRecord() {
        this.toastr.success(
            this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
    }

    messageUnrecord() {
        this.toastr.success(
            this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
    }

    doubleClickRow(detial) {
        this.isSingleClick = false;
        sessionStorage.setItem('page_MCPayment', this.page);
        sessionStorage.setItem('size_MCPayment', this.itemsPerPage);
        sessionStorage.setItem('index_MCPayment', String(this.mCPayments.indexOf(this.selectedRow)));
        if (
            !this.search.typeID &&
            !this.search.fromDate &&
            !this.search.toDate &&
            (this.statusRecord === null || undefined) &&
            !this.search.textSearch &&
            !this.search.accountingObjectID &&
            !this.search.currencyID
        ) {
            sessionStorage.removeItem('searchVoucherMCReceipt');
        }
        switch (this.selectedRow.typeID) {
            case this.TYPE_MC_PAYMENT:
            case this.TYPE_MC_PAYMENT_NCC:
                this.router.navigate(['./mc-payment', detial.id, 'edit']);
                break;
            case this.TYPE_MC_PAYMENT_PPSERVICE:
                this.mCPaymentService.find(detial.id).subscribe((res: HttpResponse<IMCPayment>) => {
                    const ppServiceID = res.body.ppServiceID;
                    if (ppServiceID) {
                        this.router.navigate(['./mua-dich-vu', ppServiceID, 'edit', 'from-mc-payment', detial.id]);
                    }
                });
                break;
            case this.TYPE_MC_PAYMENT_PPINVOICE_MHQK:
                this.mCPaymentService.find(detial.id).subscribe((res: HttpResponse<IMCPayment>) => {
                    const ppInvocieID = res.body.ppInvocieID;
                    if (res.body.storedInRepository) {
                        this.router.navigate(['./mua-hang', 'qua-kho', ppInvocieID, 'edit', 'from-mc-payment', detial.id]);
                    } else {
                        this.router.navigate(['./mua-hang', 'khong-qua-kho', ppInvocieID, 'edit', 'from-mc-payment', detial.id]);
                    }
                });
                break;
            default:
                this.router.navigate(['./mc-payment', detial.id, 'edit']);
                break;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuChi_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows && this.selectedRows.length > 1) {
            this.typeMultiAction = MultiAction.XOA;
            this.modalRef = this.modalService.open(this.popUpMultiAction, { backdrop: 'static' });
        } else {
            if (!this.checkCloseBook(this.account, this.selectedRow.postedDate) && this.selectedRow && !this.selectedRow.recorded) {
                this.router.navigate(['/mc-payment', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
            }
        }
    }

    // region Tìm kiếm chứng từ add by Hautv
    searchVoucher(search?) {
        if (!this.checkErrorForSearch()) {
            return false;
        }
        sessionStorage.removeItem('searchVoucherMCPayment');
        let searchVC: ISearchVoucher = {};
        searchVC.typeID = this.search.typeID !== undefined ? this.search.typeID : null;
        searchVC.statusRecorded = this.statusRecord ? (this.statusRecord === 1 ? true : false) : null;
        searchVC.currencyID = this.search.currencyID !== undefined ? this.search.currencyID : null;
        searchVC.fromDate = this.search.fromDate !== undefined ? this.search.fromDate : null;
        searchVC.toDate = this.search.toDate !== undefined ? this.search.toDate : null;
        searchVC.accountingObjectID = this.search.accountingObjectID !== undefined ? this.search.accountingObjectID : null;
        searchVC.textSearch = this.search.textSearch ? this.search.textSearch : null;
        searchVC = this.convertDateFromClient(searchVC);
        if (search) {
            this.page = 1;
            this.previousPage = 1;
            this.index = 0;
            this.selectedRows = [];
        }
        this.utilsService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                typeID: this.TYPE_MC_PAYMENT,
                searchVoucher: JSON.stringify(searchVC)
            })
            .subscribe(
                (res: HttpResponse<IMCPayment[]>) => this.paginateMCPayments(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        sessionStorage.setItem('searchVoucherMCPayment', JSON.stringify(searchVC));
    }

    resetSearch() {
        this.toDateStr = null;
        this.fromDateStr = null;
        this.selectedRows = [];
        sessionStorage.removeItem('searchVoucherMCPayment');
        this.loadAll(true);
    }

    // endregion

    transformAmountv(value: number) {
        const amount = parseFloat(value.toString().replace(/,/g, ''))
            .toFixed(2)
            .toString()
            .replace('.', ',')
            .replace(/\B(?=(\d{3})+(?!\d))/g, '.');
        return amount;
    }

    getTypeByTypeID(typeID: number) {
        if (this.types) {
            const type = this.types.find(n => n.id === typeID);
            if (type !== undefined && type !== null) {
                return type.typeName;
            } else {
                return '';
            }
        }
    }

    getEMContractsbyID(id) {
        if (this.eMContracts) {
            const eMC = this.eMContracts.find(n => n.id === id);
            if (eMC) {
                if (this.isSoTaiChinh) {
                    return eMC.noFBook;
                } else {
                    return eMC.noMBook;
                }
            }
        }
    }

    private convertDateFromClient(seachVoucher: ISearchVoucher): ISearchVoucher {
        const copy: ISearchVoucher = Object.assign({}, seachVoucher, {
            fromDate: seachVoucher.fromDate != null && seachVoucher.fromDate.isValid() ? seachVoucher.fromDate.format(DATE_FORMAT) : null,
            toDate: seachVoucher.toDate != null && seachVoucher.toDate.isValid() ? seachVoucher.toDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    checkErrorForSearch() {
        if (this.search.toDate && this.search.fromDate) {
            if (this.search.toDate < this.search.fromDate) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.fromDateGreaterToDate'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        return true;
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    changeFromDate() {
        this.fromDateStr = this.search.fromDate.format('DD/MM/YYYY');
    }

    changeFromDateStr() {
        try {
            if (this.fromDateStr.length === 8) {
                const td = this.fromDateStr.replace(/^(.{2})/, '$1/').replace(/^(.{5})/, '$1/');
                if (!moment(td, 'DD/MM/YYYY').isValid()) {
                    this.validateFromDate = true;
                    this.search.fromDate = null;
                } else {
                    this.validateFromDate = false;
                    this.search.fromDate = moment(td, 'DD/MM/YYYY');
                }
            } else {
                this.search.fromDate = null;
                this.validateFromDate = false;
            }
        } catch (e) {
            this.search.fromDate = null;
            this.validateFromDate = false;
        }
    }

    changeToDate() {
        this.toDateStr = this.search.toDate.format('DD/MM/YYYY');
    }

    changeToDateStr() {
        try {
            if (this.toDateStr.length === 8) {
                const td = this.toDateStr.replace(/^(.{2})/, '$1/').replace(/^(.{5})/, '$1/');
                if (!moment(td, 'DD/MM/YYYY').isValid()) {
                    this.validateToDate = true;
                    this.search.toDate = null;
                } else {
                    this.validateToDate = false;
                    this.search.toDate = moment(td, 'DD/MM/YYYY');
                }
            } else {
                this.search.toDate = null;
                this.validateToDate = false;
            }
        } catch (e) {
            this.search.toDate = null;
            this.validateToDate = false;
        }
    }

    getTotalVATAmount() {
        if (this.mCPaymentDetailTaxs && this.mCPaymentDetailTaxs.length > 0) {
            return this.mCPaymentDetailTaxs.map(n => n.vATAmount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getTotalPretaxAmount() {
        if (this.mCPaymentDetailTaxs && this.mCPaymentDetailTaxs.length > 0) {
            return this.mCPaymentDetailTaxs.map(n => n.pretaxAmount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getGoodsServicePurchase(id) {
        const gs = this.goodsServicePurchases.find(n => n.id === id);
        if (gs) {
            return gs.goodsServicePurchaseCode;
        } else {
            return '';
        }
    }

    registerChangeSession() {
        this.eventSubscriber = this.eventManager.subscribe('changeSession', response => {
            this.principal.identity(true).then(account => {
                this.account = account;
                if (account) {
                    this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                    this.currencyCode = this.account.organizationUnit.currencyID;
                    this.loadAll();
                }
            });
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuChi_KetXuat])
    export() {
        let searchVC: ISearchVoucher = {};
        searchVC.typeID = this.search.typeID !== undefined ? this.search.typeID : null;
        searchVC.status = this.search.status || this.search.status === 0 ? this.search.status : null;
        searchVC.currencyID = this.search.currencyID !== undefined ? this.search.currencyID : null;
        searchVC.fromDate = this.search.fromDate !== undefined ? this.search.fromDate : null;
        searchVC.toDate = this.search.toDate !== undefined ? this.search.toDate : null;
        searchVC.accountingObjectID = this.search.accountingObjectID !== undefined ? this.search.accountingObjectID : null;
        searchVC.employeeID = this.search.employeeID !== undefined ? this.search.employeeID : null;
        searchVC.textSearch = this.search.textSearch ? this.search.textSearch : null;
        searchVC = this.convertDateFromClient(searchVC);
        this.utilsService
            .exportPDF({
                typeID: this.TYPE_MC_PAYMENT,
                searchVoucher: JSON.stringify(searchVC)
            })
            .subscribe(
                res => {
                    this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.TYPE_MC_PAYMENT);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.TYPE_MC_PAYMENT}`, () => {
            this.exportExcel();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    exportExcel() {
        let searchVC: ISearchVoucher = {};
        searchVC.typeID = this.search.typeID !== undefined ? this.search.typeID : null;
        searchVC.status = this.search.status || this.search.status === 0 ? this.search.status : null;
        searchVC.currencyID = this.search.currencyID !== undefined ? this.search.currencyID : null;
        searchVC.fromDate = this.search.fromDate !== undefined ? this.search.fromDate : null;
        searchVC.toDate = this.search.toDate !== undefined ? this.search.toDate : null;
        searchVC.accountingObjectID = this.search.accountingObjectID !== undefined ? this.search.accountingObjectID : null;
        searchVC.employeeID = this.search.employeeID !== undefined ? this.search.employeeID : null;
        searchVC.textSearch = this.search.textSearch ? this.search.textSearch : null;
        searchVC = this.convertDateFromClient(searchVC);
        this.utilsService
            .exportExcel({
                typeID: this.TYPE_MC_PAYMENT,
                searchVoucher: JSON.stringify(searchVC)
            })
            .subscribe(res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_PHIEUCHI.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            });
    }

    isForeignCurrency() {
        if (this.selectedRow) {
            return this.account && this.selectedRow.currencyID !== this.account.organizationUnit.currencyID;
        }
    }

    getAmountOriginalType() {
        if (this.isForeignCurrency()) {
            return 8;
        }
        return 7;
    }

    getUnitPriceOriginalType() {
        if (this.isForeignCurrency()) {
            return 2;
        }
        return 1;
    }

    round(value, type) {
        if (type === 8) {
            if (this.isForeignCurrency()) {
                return this.utilsService.round(value, this.account.systemOption, type);
            } else {
                return this.utilsService.round(value, this.account.systemOption, 7);
            }
        } else if (type === 2) {
            if (this.isForeignCurrency()) {
                return this.utilsService.round(value, this.account.systemOption, type);
            } else {
                return this.utilsService.round(value, this.account.systemOption, 1);
            }
        } else {
            return this.utilsService.round(value, this.account.systemOption, type);
        }
    }

    /*Mua dịch vụ*/
    trackIdentity(index, item: IMuaDichVuResult) {
        return item.receiptDate;
    }

    getVATRateValue(vatRate: number) {
        const vatRateValue = vatRate ? (vatRate === 0 ? 0 : vatRate === 1 ? 5 : 10) : null;
        if (vatRate != null) {
            return vatRateValue + '%';
        }
        return '';
    }

    findMuaDichVu(id) {
        this.muaDichVuService.findAllPPServiceDetailsByPaymentVoucherID({ paymentVoucherID: id }).subscribe(resDetail => {
            this.muaDichVuResult.ppServiceDetailDTOS = resDetail.body ? resDetail.body : [];
            //
            this.muaDichVuResult.ppServiceDetailDTOS.forEach(detail => (detail.quantityFromDB = detail.quantity));
        });
        this.muaDichVuService
            .findRefVoucherByByPaymentVoucherID({
                typeID: this.TYPE_MC_PAYMENT_PPSERVICE,
                paymentVoucherID: id,
                currentBook: this.currentBook
            })
            .subscribe(resDetail => (this.muaDichVuResult.refVouchers = resDetail.body ? resDetail.body : []));
    }

    summPPServiceDetailDTOS(prop) {
        let total = 0;
        if (this.muaDichVuResult && this.muaDichVuResult.ppServiceDetailDTOS) {
            for (let i = 0; i < this.muaDichVuResult.ppServiceDetailDTOS.length; i++) {
                total += this.muaDichVuResult.ppServiceDetailDTOS[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    /*Mua dịch vụ*/

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
                typeID: this.TYPE_MC_PAYMENT_PPINVOICE_MHQK,
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

    /*Mua hàng qua kho*/

    /*Trả tiền nhà cung cấp*/
    summCPaymentDetailVendors(prop) {
        let total = 0;
        for (let i = 0; i < this.mCPaymentDetailVendors.length; i++) {
            total += this.mCPaymentDetailVendors[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    getEmployeeByID(id) {
        if (this.accountingobjects) {
            const epl = this.accountingobjects.find(n => n.id === id);
            if (epl) {
                return epl.accountingObjectCode;
            } else {
                return '';
            }
        }
    }

    viewVoucher(imcPaymentDetailVendor: IMCPaymentDetailVendor) {
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

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuChi_Them])
    addNew($event) {
        event.preventDefault();
        this.router.navigate(['mc-payment/new']);
    }

    edit() {
        event.preventDefault();
        if (this.selectedRow) {
            this.doubleClickRow(this.selectedRow);
        }
    }

    registerLockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('lockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
                this.account = account;
            });
            this.loadAll();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
                this.account = account;
            });
            this.loadAll();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    sum(prop) {
        let total = 0;
        if (this.mCPaymentDetails) {
            for (let i = 0; i < this.mCPaymentDetails.length; i++) {
                total += this.mCPaymentDetails[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    multiRecord() {
        const rq: RequestRecordListDtoModel = {};
        rq.records = this.selectedRows.map(n => {
            return { id: n.id, typeID: n.typeID };
        });
        rq.typeIDMain = this.TYPE_MC_PAYMENT;
        this.gLService.recordList(rq).subscribe((res: HttpResponse<HandlingResult>) => {
            /*this.selectedRows.forEach(slr => {
                const update = this.mCPayments.find(
                    n => n.id === slr.id && !res.body.listFail.some(m => m.id === slr.id || m.paymentVoucherID === slr.id)
                );
                if (update) {
                    update.recorded = true;
                }
            });*/
            this.loadAll(false, false, true);
            if (res.body.countFailVouchers > 0) {
                res.body.listFail.forEach(n => {
                    if (n.paymentVoucherID) {
                        const mc = this.mCPayments.find(m => m.id === n.paymentVoucherID);
                        const type = this.types.find(t => t.id === mc.typeID);
                        n.noFBook = mc.noFBook;
                        n.noMBook = mc.noMBook;
                        n.typeName = type.typeName;
                    } else {
                        const type = this.types.find(t => t.id === n.typeID);
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
            } else {
                this.messageRecord();
            }
        });
    }

    multiUnRecord() {
        this.mCPaymentService.multiUnrecord(this.selectedRows).subscribe(
            (res: HttpResponse<any>) => {
                this.typeMultiAction = undefined;
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
                // this.activeModal.close();
                this.loadAll(false, false, true);
                // }
            },
            (res: HttpErrorResponse) => {
                this.toastr.error(
                    this.translate.instant('global.data.unRecordFailed'),
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
            }
        );
    }

    multiDelete() {
        this.mCPaymentService.multiDelete(this.selectedRows).subscribe(
            (res: HttpResponse<any>) => {
                this.typeMultiAction = undefined;
                this.selectedRows = [];
                this.index = 0;
                this.loadAll();
                // this.searchAfterChangeRecord();
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
                // }
            },
            (res: HttpErrorResponse) => {
                if (res.error.errorKey === 'errorDeleteList') {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.mBDeposit.errorDeleteVoucherNo'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                }
            }
        );
    }

    closePop() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    continue() {
        switch (this.typeMultiAction) {
            case MultiAction.GHI_SO:
                this.multiRecord();
                break;
            case MultiAction.BO_GHI_SO:
                this.multiUnRecord();
                break;
            case MultiAction.XOA:
                this.multiDelete();
                break;
        }
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    isRecord() {
        return this.selectedRows.some(n => n.recorded) && !this.selectedRows.some(n => !n.recorded);
    }

    isUnrecord() {
        return !this.selectedRows.some(n => n.recorded) && this.selectedRows.some(n => !n.recorded);
    }

    checkSelect(saorder: ISAOrder) {
        return this.selectedRows.some(n => n.id === saorder.id);
    }
}
