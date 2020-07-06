import { Component, OnInit, OnDestroy, ViewChild, AfterViewChecked, TemplateRef } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { IPPInvoice } from 'app/shared/model/pp-invoice.model';
import { Principal } from 'app/core';
import { TranslateService } from '@ngx-translate/core';
import { DATE_FORMAT, DATE_FORMAT_SLASH, ITEMS_PER_PAGE } from 'app/shared';
import { PPInvoiceService } from './pp-invoice.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { DataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { IRefVoucher } from 'app/shared/model/ref-voucher.model';
import {
    DBDateClosed,
    GROUP_TYPEID,
    MAU_BO_GHI_SO,
    MSGERROR,
    PPINVOICE_COMPONENT_TYPE,
    PPINVOICE_TYPE,
    SO_LAM_VIEC,
    TypeID
} from 'app/app.constants';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { ToastrService } from 'ngx-toastr';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { IPPInvoiceDetailCost } from 'app/shared/model/pp-invoice-detail-cost.model';
import { DatePipe } from '@angular/common';
import { AccountingObjectDTO, IAccountingObject } from 'app/shared/model/accounting-object.model';
import * as moment from 'moment';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { SystemOptionService } from 'app/he-thong/system-option';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';

@Component({
    selector: 'eb-pp-invoice',
    templateUrl: './pp-invoice.component.html',
    styleUrls: ['pp-invoice.css']
})
export class PPInvoiceComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    @ViewChild('unRecordModal') unRecordModal: any;
    @ViewChild('deleteItem') deleteItem: any;
    pPInvoices: IPPInvoice[];
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
    fromDate: any;
    toDate: any;
    currencyID: any;
    currency: any;
    accountingObjectCode: any;
    accountingObjects: any[];
    recorded = null;
    keySearch: any;
    pageCount: any;
    ppInvoiceDetails: any[];
    selectedRow: IPPInvoice;
    checkModalRef: NgbModalRef;
    unRecordCheck: NgbModalRef;
    isShowSearch = false;
    accountingObject: any;
    currencies: ICurrency[];
    rowNum: number;
    searchData: any;
    refVouchers?: IRefVoucher[];
    dataSession: any;
    status: number;
    accountingObjectEmployee: any;
    searchValue: string;
    account: any;
    currentBook: string;
    PPINVOICE_TYPE = PPINVOICE_TYPE;
    ppInvoiceDetailCost?: any[];
    accountingObjectEmployees: any[];
    isErrorInvalid: any;
    fromDateHolder: any;
    fromDateHolderMask: any;
    toDateHolder: any;
    toDateHolderMask: any;
    index: number;
    MUA_HANG_QUA_KHO = 210;
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
    componentType: number;
    routerLink: string;
    PPINVOICE_COMPONENT_TYPE = PPINVOICE_COMPONENT_TYPE;
    isLoading = false;
    isMau: boolean;
    ROLE = ROLE;
    isKho = true; // true: mua hàng qua kho, false: mua hàng không qua kho
    isData = false;
    typeMultiAction: number;
    modalRef: NgbModalRef;
    isShowBtnRecord = true;
    isShowBtnUnRecord = true;
    currencyCode: string;
    taxCalculationMethod: boolean;
    GROUP_ID = GROUP_TYPEID;

    constructor(
        private systemOptionService: SystemOptionService,
        private pPInvoiceService: PPInvoiceService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private modalService: NgbModal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        public datepipe: DatePipe,
        public utilsService: UtilsService,
        private toastrService: ToastrService,
        public translateService: TranslateService,
        private generalLedgerService: GeneralLedgerService,
        private currencyService: CurrencyService,
        private accountingObjectService: AccountingObjectService,
        private eventManager: JhiEventManager,
        private refModalService: RefModalService,
        public utilService: UtilsService,
        public activeModal: NgbActiveModal
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.selectedRow = {};
        this.pPInvoices = [];
        // this.routeData = this.activatedRoute.data.subscribe(data => {
        //     this.page = data.pagingParams.page;
        //     this.previousPage = data.pagingParams.page;
        //     this.reverse = data.pagingParams.ascending;
        //     this.predicate = data.pagingParams.predicate;
        // });
    }

    ngOnInit() {
        this.init();
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.componentType = data.componentType ? data.componentType : PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO;
            this.getRouterLink();
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            // this.registerChangeInPPOrder();
            this.registerExport();
            this.registerChangeInPPInvoices();
            this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                this.currencies = res.body;
                this.accountingObjectService.getAccountingObjectsForProvider().subscribe((res2: HttpResponse<AccountingObjectDTO[]>) => {
                    this.accountingObjects = res2.body;
                    this.accountingObjectService
                        .getAccountingObjectsForEmployee()
                        .subscribe((res3: HttpResponse<AccountingObjectDTO[]>) => {
                            this.accountingObjectEmployees = res3.body;
                            this.getSessionData();
                        });
                });
            });
            this.onSetUpHolderMask();
        });
        this.reset();
        this.registerLockSuccess();
        this.registerUnlockSuccess();
    }

    registerLockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('lockSuccess', response => {
            this.principal.identity().then(account => {
                this.account = account;
            });
            this.reset();
        });
    }

    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.principal.identity().then(account => {
                this.account = account;
            });
            this.reset();
        });
    }

    reset() {
        this.getAccount();
        this.getSessionData();
    }

    getRouterLink() {
        switch (this.componentType) {
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO:
                this.routerLink = '/mua-hang/qua-kho';
                this.isKho = true;
                break;
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO:
                this.routerLink = '/mua-hang/khong-qua-kho';
                this.isKho = false;
                break;
            default:
                this.routerLink = '/mua-hang/qua-kho';
                this.isKho = true;
        }
    }

    getSessionData() {
        // this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceSearchData'));
        switch (this.componentType) {
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO:
                this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceRSISearchData'));
                break;
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO:
                this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceSearchData'));
                break;
            default:
                this.dataSession = JSON.parse(sessionStorage.getItem('ppInvoiceRSISearchData'));
        }

        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.searchData = JSON.parse(this.dataSession.searchVoucher);
            if (this.searchData) {
                this.currency = this.searchData.currency;
                this.fromDate = this.searchData.fromDate;
                this.toDate = this.searchData.toDate;
                this.status = this.searchData.status;
                this.accountingObject = this.searchData.accountingObject;
                this.accountingObjectEmployee = this.searchData.accountingObjectEmployee;
                this.searchValue = this.searchData.searchValue;
            }
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
            this.isShowSearch = this.dataSession.isShowSearch;
        }
        // sessionStorage.removeItem('ppInvoiceSearchData');
        switch (this.componentType) {
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO:
                sessionStorage.removeItem('ppInvoiceRSISearchData');
                break;
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO:
                sessionStorage.removeItem('ppInvoiceSearchData');
                break;
            default:
                sessionStorage.removeItem('ppInvoiceRSISearchData');
        }
        this.transition(false);
    }

    loadAllForSearch() {
        if (this.checkToDateGreaterFromDate()) {
            this.page = 1;
            this.search();
        }
    }

    resetSeach() {
        this.isErrorInvalid = false;
        this.accountingObject = {};
        this.status = null;
        this.currency = {};
        this.accountingObjectEmployee = {};
        this.fromDate = '';
        this.toDate = '';
        this.selectedRow = {};
        this.searchValue = '';
        this.loadAllForSearch();
    }

    checkToDateGreaterFromDate(): boolean {
        if (this.fromDate && this.toDate && moment(this.toDate) < moment(this.fromDate)) {
            this.isErrorInvalid = true;
            this.toastrService.error(
                this.translateService.instant('ebwebApp.mBTellerPaper.error.fromDateGreaterToDate'),
                this.translateService.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        } else {
            this.isErrorInvalid = false;
            return true;
        }
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.index = null;
            this.previousPage = page;
            this.transition(true);
        }
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    clickShowDetail(ppInVoice: IPPInvoice, index: number) {
        this.selectedRow = ppInVoice;
        this.index = index + 1;
        this.getDetailByID(ppInVoice);
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    search(index?) {
        this.pPInvoiceService
            .searchPPInvoice({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                currencyId: this.currency && this.currency.currencyCode ? this.currency.currencyCode : '',
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: this.status || this.status === 0 ? this.status : '',
                accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
                employee: this.accountingObjectEmployee && this.accountingObjectEmployee.id ? this.accountingObjectEmployee.id : '',
                keySearch: this.searchValue || '',
                isRSI: this.componentType === PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO
            })
            .subscribe(res => {
                this.paginatePPInvoices(res.body, res.headers);
            });
    }

    transition(isTransition) {
        if (isTransition) {
            this.selectedRow = {};
        }
        /*this.router.navigate([this.routerLink], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });*/
        this.search();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            this.routerLink,
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.search();
    }

    doubleClickRowPPI(item: IPPInvoice, index: number, isEdit) {
        this.saveSearchData(item, index, isEdit);
        this.router.navigate([this.routerLink, item.id, 'edit', this.rowNum]);
    }
    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('khong') ? ROLE.MuaHangQuaKho_Them : ROLE.MuaHangKhongQuaKho_Them])
    addNew($event = null) {
        this.saveSearchData(this.selectedRow, this.index - 1, true);
        this.router.navigate([this.routerLink + '/new']);
    }

    saveSearchData(item: IPPInvoice, index: number, isEdit) {
        this.selectedRow = item;
        this.searchData = {
            currency: this.currency,
            fromDate: this.fromDate || '',
            toDate: this.toDate || '',
            status: this.status,
            accountingObject: this.accountingObject,
            employee: this.accountingObjectEmployee,
            searchValue: this.searchValue || ''
        };
        this.rowNum = this.getRowNumberOfRecord(this.page, index);
        this.dataSession = new DataSessionStorage();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.pageCount = this.pageCount;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.rowNum;
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        this.dataSession.isShowSearch = this.isShowSearch;
        this.dataSession.searchVoucher = JSON.stringify(this.searchData);
        this.dataSession.isEdit = isEdit;
        // sessionStorage.setItem('ppInvoiceSearchData', JSON.stringify(this.dataSession));
        switch (this.componentType) {
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO:
                sessionStorage.setItem('ppInvoiceRSISearchData', JSON.stringify(this.dataSession));
                break;
            case PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO:
                sessionStorage.setItem('ppInvoiceSearchData', JSON.stringify(this.dataSession));
                break;
            default:
                sessionStorage.setItem('ppInvoiceRSISearchData', JSON.stringify(this.dataSession));
        }
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IPPInvoice) {
        return item.id;
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    onSetUpHolderMask() {
        this.fromDateHolderMask = moment(this.fromDate, 'DD/MM/YYYY').format('DD/MM/YYYY');
        this.toDateHolderMask = moment(this.toDate, 'DD/MM/YYYY').format('DD/MM/YYYY');
    }

    selectedItemPerPage() {
        this.search();
    }

    private paginatePPInvoices(data: IPPInvoice[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.pPInvoices = data;
        this.objects = data;
        if (this.pPInvoices && this.pPInvoices.length > 0) {
            this.isData = true;
            const map = new Map();
            for (let i = 0; i < this.selectedRows.length; i++) {
                map.set(this.selectedRows[i].id, this.selectedRows[i].id);
            }
            const listNew = [];
            for (let i = 0; i < this.pPInvoices.length; i++) {
                if (map.has(this.pPInvoices[i].id)) {
                    listNew.push(this.pPInvoices[i]);
                }
            }
            this.selectedRows = listNew;
            if (this.selectedRows.length > 0) {
                this.selectedRow = this.selectedRows[0];
            } else {
                if (this.rowNum && !this.index) {
                    this.index = this.rowNum % this.itemsPerPage;
                    this.index = this.index || this.itemsPerPage;
                    this.selectedRow = this.pPInvoices[this.index - 1] ? this.pPInvoices[this.index - 1] : {};
                    if (!this.selectedRows.some(item => item.id === this.selectedRow.id)) {
                        this.selectedRows.push(this.selectedRow);
                    }
                } else if (this.index) {
                    this.selectedRow = this.pPInvoices[this.index - 1] ? this.pPInvoices[this.index - 1] : {};
                    if (!this.selectedRows.some(item => item.id === this.selectedRow.id)) {
                        this.selectedRows.push(this.selectedRow);
                    }
                } else {
                    if (!this.selectedRows.some(item => item.id === this.pPInvoices[0].id)) {
                        this.selectedRows.push(this.pPInvoices[0]);
                    }
                    this.selectedRow = this.pPInvoices[0];
                }
            }

            this.rowNum = this.getRowNumberOfRecord(this.page, 0);
            this.clickShowDetail(this.selectedRow && this.selectedRow.id ? this.selectedRow : this.pPInvoices[0], this.index - 1);
            this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
        } else {
            this.isData = false;
            this.ppInvoiceDetails = [];
            this.refVouchers = [];
            this.ppInvoiceDetailCost = [];
            this.selectedRow = {};
            this.changeAmountPPInvoice(this.ppInvoiceDetails);
        }
    }

    getAccount() {
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.account = account;
                if (this.account) {
                    this.taxCalculationMethod = this.account.organizationUnit.taxCalculationMethod === 1;
                    if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                        this.currencyCode = this.account.organizationUnit.currencyID;
                    }
                    this.currentBook = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                    for (let i = 0; i < this.account.systemOption.length; i++) {
                        if (!this.account.systemOption[i].data) {
                            this.account.systemOption[i].data = this.account.systemOption[i].defaultData;
                        }
                    }
                    this.color = this.account.systemOption.find(item => item.code === MAU_BO_GHI_SO).data;
                }
            });
        });
    }

    private init() {
        this.ppInvoiceDetailCost = [];
        this.ppInvoiceDetails = [];
        this.status = null;
        this.accountingObjects = [];
        this.accountingObjectEmployees = [];
        this.selectedRow = {};
        this.account = { organizationUnit: {} };
        this.accountingObject = {};
        this.currency = {};
        this.isMau = false;
        this.isKho = true;
    }

    getData() {
        this.getAccountingObject();
        this.getEmployees();
        this.getCurrency();
    }

    registerChangeInPPInvoices() {
        this.eventSubscriber = this.eventManager.subscribe('pPInvoiceListModification', response => this.search());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    clearSearch() {
        this.keySearch = '';
        this.recorded = null;
        this.currency = null;
        this.accountingObject = null;
    }

    getDetailByID(item: IPPInvoice) {
        // this.ppInvoiceDetails = item.ppInvoiceDetails;
        this.selectedRow = item;
        this.pPInvoiceService.findDetailById({ id: item.id }).subscribe(res => {
            this.ppInvoiceDetails = res.body;
            this.changeAmountPPInvoice(this.ppInvoiceDetails);
        });

        // tham chiếu
        this.pPInvoiceService.findRefVoucherByRefId({ refId: item.id }).subscribe((res: HttpResponse<IRefVoucher[]>) => {
            this.refVouchers = res.body;
        });
        // phân bổ chi phí
        this.pPInvoiceService.getPPInvoiceDetailCost({ refId: item.id }).subscribe((res: HttpResponse<IPPInvoiceDetailCost[]>) => {
            this.ppInvoiceDetailCost = res.body;
        });
    }

    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('khong') ? ROLE.MuaHangQuaKho_Xoa : ROLE.MuaHangKhongQuaKho_Xoa])
    delete() {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.checkModalRef) {
            this.checkModalRef.close();
        }
        if (this.pPInvoices && this.pPInvoices.length > 0 && this.selectedRow && !this.selectedRow.recorded && !this.isLoading) {
            this.openModel1(this.deleteItem);
        }
    }

    openModel1(content) {
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 0;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            this.checkModalRef = this.modalService.open(this.deleteItem, { backdrop: 'static' });
        }
    }

    record() {
        event.preventDefault();
        if (
            this.pPInvoices &&
            this.pPInvoices.length > 0 &&
            this.selectedRow &&
            this.selectedRow.id &&
            !this.selectedRow.recorded &&
            !this.isLoading
        ) {
            this.recordPPI();
        }
    }
    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('khong') ? ROLE.MuaHangQuaKho_GhiSo : ROLE.MuaHangKhongQuaKho_GhiSo])
    recordPPI() {
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 2;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            const recordData = { id: this.selectedRow.id, typeID: PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN };
            this.isLoading = true;
            this.generalLedgerService.record(recordData).subscribe((res: HttpResponse<Irecord>) => {
                if (res.body.success) {
                    this.toastrService.success(
                        this.translateService.instant('ebwebApp.mBTellerPaper.recordToast.done'),
                        this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                    );
                    this.selectedRow.recorded = true;
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
                            this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.failed'),
                            this.translateService.instant('ebwebApp.mBTellerPaper.error.error')
                        );
                    }
                }
                this.isLoading = false;
            });
        }
    }

    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('khong') ? ROLE.MuaHangQuaKho_GhiSo : ROLE.MuaHangKhongQuaKho_GhiSo])
    unrecord() {
        event.preventDefault();
        if (
            this.pPInvoices &&
            this.pPInvoices.length > 0 &&
            this.selectedRow &&
            this.selectedRow.id &&
            this.selectedRow.recorded &&
            !this.isLoading &&
            !this.checkCloseBook(this.account, this.selectedRow.postedDate)
        ) {
            this.checkUnRecord(this.unRecordModal);
        }
    }
    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('khong') ? ROLE.MuaHangQuaKho_GhiSo : ROLE.MuaHangKhongQuaKho_GhiSo])
    unRecordPPI() {
        const recordData = {
            id: this.selectedRow.id,
            typeID: PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN
        };
        this.isLoading = true;
        this.generalLedgerService.unrecord(recordData).subscribe((res: HttpResponse<Irecord>) => {
            if (res.body.success) {
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.done'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                );
                this.selectedRow.recorded = false;
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

    // lấy ds đối tượng
    getAccountingObject() {
        this.accountingObjectService.getAccountingObjectActive().subscribe(res => {
            this.accountingObjects = res.body;
        });
    }

    getCurrency() {
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencies = res.body;
        });
    }

    deletePPI() {
        if (this.selectedRow && this.selectedRow.id) {
            this.isLoading = true;
            const index = this.pPInvoices.indexOf(this.selectedRow);
            this.pPInvoiceService.deleteById({ id: this.selectedRow.id }).subscribe(
                (res: HttpResponse<any>) => {
                    if (res.body.message === UpdateDataMessages.DELETE_SUCCESS) {
                        this.toastrService.success(this.translateService.instant('ebwebApp.pPInvoice.success.deleteSuccess'));
                        this.search(index);
                        this.checkModalRef.close();
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
    // nhân viên thực hiện
    getEmployees() {
        this.accountingObjectService.getAccountingObjectEmployee().subscribe(
            (res: HttpResponse<IAccountingObject[]>) => {
                this.accountingObjectEmployees = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    exportExcel() {
        this.pPInvoiceService
            .export('excel', {
                currency: this.currency && this.currency.currencyCode ? this.currency.currencyCode : '',
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: this.status || this.status === 0 ? this.status : '',
                accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
                employee: this.accountingObjectEmployee && this.accountingObjectEmployee.id ? this.accountingObjectEmployee.id : '',
                searchValue: this.searchValue || '',
                isRSI: this.componentType === PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO
            })
            .subscribe(res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                // const name = 'DS_MuaHangQuaKho.xls';
                let name = '';
                switch (this.componentType) {
                    case PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO:
                        name = 'DS_MuaHangQuaKho.xls';
                        break;
                    case PPINVOICE_COMPONENT_TYPE.MUA_HANG_KHONG_QUA_KHO:
                        name = 'DS_MuaHangKhongQuaKho.xls';
                        break;
                    default:
                        name = 'DS_MuaHangQuaKho.xls';
                }
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            });
    }
    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('khong') ? ROLE.MuaHangQuaKho_KetXuat : ROLE.MuaHangKhongQuaKho_KetXuat])
    export() {
        event.preventDefault();
        if (this.pPInvoices && this.pPInvoices.length > 0 && !this.isLoading) {
            this.exportPdf();
        }
    }

    exportPdf() {
        this.pPInvoiceService
            .export('pdf', {
                currency: this.currency && this.currency.currencyCode ? this.currency.currencyCode : '',
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: this.status || this.status === 0 ? this.status : '',
                accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
                employee: this.accountingObjectEmployee && this.accountingObjectEmployee.id ? this.accountingObjectEmployee.id : '',
                searchValue: this.searchValue || '',
                isRSI: this.componentType === PPINVOICE_COMPONENT_TYPE.MUA_HANG_QUA_KHO
            })
            .subscribe(res => {
                this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.MUA_HANG_QUA_KHO);
            });
    }

    registerExport() {
        this.eventManager.subscribe(`export-excel-${this.MUA_HANG_QUA_KHO}`, () => {
            this.exportExcel();
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
        this.roundObject();
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

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    checkPayVendor(content) {
        this.pPInvoiceService.checkPayVendor({ id: this.selectedRow.id }).subscribe((res: HttpResponse<any>) => {
            if (res.body.message === UpdateDataMessages.SUCCESS) {
                this.selectedRow.isPlayVendor = false;
                if (this.selectedRow.isUsed) {
                    // this.unRecordPPI();
                    this.unRecordCheck = this.modalService.open(content, { size: 'lg', backdrop: 'static' });
                } else {
                    this.unRecordPPI();
                }
            } else if (res.body.message === UpdateDataMessages.DUPLICATE) {
                this.selectedRow.isPlayVendor = true;
                // this.unRecordPPI();
                this.unRecordCheck = this.modalService.open(content, { size: 'lg', backdrop: 'static' });
            }
        });
    }

    checkUnRecord(content) {
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 1;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            this.isLoading = true;
            this.pPInvoiceService.checkUnRecord({ id: this.selectedRow.id }).subscribe(
                (res: HttpResponse<any>) => {
                    if (res.body.message === UpdateDataMessages.NOTHING) {
                        this.checkPayVendor(content);
                    } else if (res.body.message === UpdateDataMessages.PPINVOICE_USED) {
                        this.selectedRow.isUsed = true;
                        this.checkPayVendor(content);
                        // this.unRecordCheck = this.modalService.open(content, { size: 'lg', backdrop: 'static' });
                    } else {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.default'));
                    }
                    this.isLoading = false;
                },
                (res: HttpErrorResponse) => {
                    this.isLoading = false;
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.default'));
                }
            );
        }
    }

    roundObject() {
        this.utilsService.roundObjectsWithAccount(this.ppInvoiceDetails, this.account, this.currency.currencyCode);
        this.utilsService.roundObjectsWithAccount(this.ppInvoiceDetailCost, this.account, this.currency.currencyCode);

        this.utilsService.roundObjectWithAccount(this.selectedRow, this.account, this.currency.currencyCode);
    }

    getVATRateValue(vatRate: number) {
        const vatRateValue = vatRate ? (vatRate === 1 ? 5 : 10) : vatRate === 0 ? 0 : null;
        if (vatRate != null) {
            return vatRateValue + '%';
        }
        return '';
    }

    doubleClickRow(any?) {
        event.preventDefault();
        if (this.index) {
            this.index = this.index - 1;
        }
        this.doubleClickRowPPI(this.selectedRow, this.index, false);
    }
    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('khong') ? ROLE.MuaHangQuaKho_Sua : ROLE.MuaHangKhongQuaKho_Sua])
    edit() {
        event.preventDefault();
        if (!this.selectedRow.recorded) {
            this.doubleClickRowPPI(this.selectedRow, this.index, true);
        }
    }

    onSelect(item) {
        this.selectedRow = item;
        this.clickShowDetail(this.selectedRow, this.index);
    }

    checkMultiButton(isRecord: boolean) {
        if (this.selectedRows) {
            for (let i = 0; i < this.selectedRows.length; i++) {
                if (this.selectedRows[i] && this.selectedRows[i].recorded === isRecord) {
                    return true;
                }
            }
        }
        return false;
    }

    closePopUp() {
        if (this.modalRef) {
            this.modalRef.close();
            this.modalRef = null;
        }
    }

    continueMultiAction() {
        this.isLoading = true;
        if (this.typeMultiAction === 0) {
            this.pPInvoiceService.multiDelete({ ppInvoices: this.selectedRows, kho: this.isKho }).subscribe(
                (res: HttpResponse<any>) => {
                    this.isLoading = false;
                    if (this.modalRef) {
                        this.modalRef.close();
                        this.modalRef = null;
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
                        this.toastrService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
                    }
                    this.activeModal.close();
                    this.search();
                    this.index = 1;
                },
                (res: HttpErrorResponse) => {
                    this.isLoading = false;
                    if (res.error.errorKey === 'errorDeleteList') {
                        this.toastrService.error(
                            this.translateService.instant('ebwebApp.mBDeposit.errorDeleteVoucherNo'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    if (this.modalRef) {
                        this.modalRef.close();
                        this.modalRef = null;
                    }
                }
            );
        } else if (this.typeMultiAction === 1) {
            this.pPInvoiceService.multiUnrecord({ ppInvoices: this.selectedRows, kho: this.isKho }).subscribe(
                (res: HttpResponse<any>) => {
                    this.isLoading = false;
                    if (this.modalRef) {
                        this.modalRef.close();
                        this.modalRef = null;
                    }
                    // this.selectedRows = [];
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
                        this.toastrService.success(
                            this.translateService.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    this.activeModal.close();
                    this.search();
                    this.index = 1;
                },
                (res: HttpErrorResponse) => {
                    this.isLoading = false;
                    this.toastrService.error(
                        this.translateService.instant('global.data.unRecordFailed'),
                        this.translateService.instant('ebwebApp.mBDeposit.message')
                    );
                    if (this.modalRef) {
                        this.modalRef.close();
                        this.modalRef = null;
                    }
                }
            );
        } else if (this.typeMultiAction === 2) {
            const listRecord: RequestRecordListDtoModel = {};
            listRecord.typeIDMain = PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN;
            listRecord.kho = this.isKho;
            listRecord.records = [];
            this.selectedRows.forEach(item => {
                listRecord.records.push({
                    id: item.id,
                    typeID: item.typeId
                });
            });
            this.generalLedgerService.recordList(listRecord).subscribe(
                (res: HttpResponse<HandlingResult>) => {
                    this.isLoading = false;
                    if (this.modalRef) {
                        this.modalRef.close();
                        this.modalRef = null;
                    }
                    // this.selectedRows = [];
                    if (res.body.listFail && res.body.listFail.length > 0) {
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
                        this.toastrService.success(
                            this.translateService.instant('ebwebApp.mBCreditCard.recordSuccess'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    this.activeModal.close();
                    this.search();
                    this.index = 1;
                },
                (res: HttpErrorResponse) => {
                    this.isLoading = false;
                    this.toastrService.error(
                        this.translateService.instant('global.data.recordFailed'),
                        this.translateService.instant('ebwebApp.mBDeposit.message')
                    );
                    if (this.modalRef) {
                        this.modalRef.close();
                        this.modalRef = null;
                    }
                }
            );
        }
    }

    checkDisable(isDelete) {
        if (this.selectedRows && this.selectedRows.length > 0) {
            if (isDelete) {
                let check = false;
                for (const item of this.selectedRows) {
                    if (!item.recorded) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    return true;
                }
            }
            const dbDateClosed = this.account.systemOption.find(x => x.code === DBDateClosed).data;
            if (dbDateClosed) {
                const dateClose = dbDateClosed ? moment(dbDateClosed, DATE_FORMAT) : null;
                if (dateClose) {
                    for (const item of this.selectedRows) {
                        if (item.recorded) {
                            const postedDate = moment(item.postedDate, DATE_FORMAT);
                            if (dateClose.isBefore(postedDate)) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    closeModalDeleteItem() {
        if (this.checkModalRef) {
            this.checkModalRef.close();
            //  this.checkModalRef = null;
        }
    }

    checkSelected(item: IPPInvoice) {
        return this.selectedRows.some(_item => _item.id === item.id);
    }
}
