import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { Irecord } from 'app/shared/model/record';
import { DataSessionStorage, IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { PporderService } from 'app/entities/pporder';
import { PporderdetailService } from 'app/entities/pporderdetail';
import { IPporder, Pporder } from 'app/shared/model/pporder.model';

import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { PPOrderDetail } from 'app/shared/model/pporderdetail.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { AccountingObjectDTO } from 'app/shared/model/accounting-object.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { UnitService } from 'app/danhmuc/unit';
import * as moment from 'moment';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { DatePipe } from '@angular/common';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { TypeID, REPORT } from 'app/app.constants';

@Component({
    selector: 'eb-don-mua-hang',
    templateUrl: './don-mua-hang.component.html',
    styleUrls: ['./don-mua-hang.component.css']
})
export class DonMuaHangComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('referencePPOrder') referencePPOrder: NgbModalRef;
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    currentAccount: any;
    accountingObjectEmployees: AccountingObjectDTO[];
    ppOrder: Pporder[];
    ppOrderDetailsLoadDataClick: PPOrderDetail[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    duplicateEventSubscriber: Subscription;
    routeData: any;
    searchValue: string;
    links: any;
    ppOrders: Pporder;
    ppOrderDetails: PPOrderDetail[];
    currency: ICurrency;
    currencies: ICurrency[];
    status: number;
    accountingObject: AccountingObjectDTO;
    accountingObjects: AccountingObjectDTO[];
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    currencyID: string;
    fromDate: any;
    toDate: any;
    recorded?: boolean;
    predicate: any;
    previousPage: any;
    reverse: any;
    accountingObjectEmployee: AccountingObjectDTO;
    public selectedRow: Pporder;
    isShowSearch: boolean;
    pageCount: any;
    record_: Irecord;
    rowNum: number;
    index: number;
    searchData: any;
    dataSession: IDataSessionStorage = new class implements IDataSessionStorage {
        accountingObjectName: string;
        itemsPerPage: number;
        page: number;
        pageCount: number;
        predicate: number;
        reverse: number;
        rowNum: number;
        totalItems: number;
        isEdit: boolean;
    }();
    refVoucher: any;

    fromDateHolder: any;
    fromDateHolderMask: any;
    toDateHolder: any;
    toDateHolderMask: any;
    isErrorInvalid: any;
    account: any;
    DON_MUA_HANG = 200;
    modalRef: NgbModalRef;

    // ROLE
    ROLE_XEM = ROLE.DonMuaHang_XEM;
    ROLE_THEM = ROLE.DonMuaHang_THEM;
    ROLE_SUA = ROLE.DonMuaHang_SUA;
    ROLE_XOA = ROLE.DonMuaHang_XOA;
    ROLE_IN = ROLE.DonMuaHang_IN;
    ROLE_KETXUAT = ROLE.DonMuaHang_KET_XUAT;
    sumTotalAmount: number;

    quantitySum: number;
    quantityReceiptSum: number;
    amountSum: number;
    amountOriginalSum: number;
    discountAmountOriginalSum: number;
    discountAmountSum: number;
    vatAmountOriginalSum: number;
    vatAmountSum: number;
    mainQuantitySum: number;

    countTotalVouchers: number;
    countSuccessVouchers: number;
    REPORT = REPORT;

    constructor(
        private currencyService: CurrencyService,
        public datepipe: DatePipe,
        private accountingObjectService: AccountingObjectService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private materialGoodsService: MaterialGoodsService,
        private unitService: UnitService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private ppOrderService: PporderService,
        private ppOrderDetailsService: PporderdetailService,
        private eventManager: JhiEventManager,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private modalService: NgbModal,
        private refModalService: RefModalService,
        private translate: TranslateService,
        public activeModal: NgbActiveModal,
        private pPOrderService: PporderService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
    }

    ngOnInit() {
        this.countTotalVouchers = 0;
        this.countSuccessVouchers = 0;
        this.status = -1;
        this.ppOrderDetailsLoadDataClick = [];
        this.refVoucher = [];
        this.accountingObjects = [];
        this.isShowSearch = false;
        this.sumTotalAmount = 0;
        this.ppOrders = {};
        this.ppOrderDetails = [];
        this.updateSum();
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.registerChangeInPPOrder();
            this.registerExport();
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
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('DMHSearchData'));
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
        sessionStorage.removeItem('DMHSearchData');
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
        this.status = -1;
        this.currency = {};
        this.accountingObjectEmployee = {};
        this.fromDate = '';
        this.toDate = '';
        this.selectedRow = null;
        this.searchValue = '';
        this.loadAllForSearch();
    }

    checkToDateGreaterFromDate(): boolean {
        if (
            this.fromDate &&
            this.toDate &&
            this.datepipe.transform(this.fromDate, 'yyyyMMdd') > this.datepipe.transform(this.toDate, 'yyyyMMdd')
        ) {
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

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition(true);
        }
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    onSelect(ppOrder: Pporder) {
        this.selectedRow = ppOrder;
        if (ppOrder) {
            this.ppOrderDetailsLoadDataClick = ppOrder.ppOrderDetails;
            if (ppOrder.id) {
                this.ppOrderService.getRefVouchersByPPOrderID(ppOrder.id).subscribe(res => (this.refVoucher = res.body));
            }
            this.updateSum();
        }
    }

    printf(isDownload: boolean, typeReports) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.selectedRow.id,
                typeID: TypeID.DON_MUA_HANG,
                typeReport: typeReports
            },
            isDownload
        );
        this.toastr.success(
            this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.pporder.financialPaper') + '...',
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    search() {
        const searchObject = {
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
            searchDTO: JSON.stringify({
                currency: this.currency && this.currency.currencyCode ? this.currency.currencyCode : '',
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: (this.status || this.status === 0) && this.status !== -1 ? this.status : '',
                accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
                employee: this.accountingObjectEmployee && this.accountingObjectEmployee.id ? this.accountingObjectEmployee.id : '',
                searchValue: this.searchValue || ''
            })
        };

        this.ppOrderService
            .searchAll(searchObject)
            .subscribe(
                (res: HttpResponse<Pporder[]>) => this.paginatePPOrder(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );

        this.ppOrderService
            .findTotalSum(searchObject)
            .subscribe(
                (res: HttpResponse<number>) => (this.sumTotalAmount = res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    transition(isTransition) {
        if (isTransition) {
            this.selectedRow = null;
        }
        this.router.navigate(['/don-mua-hang'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.search();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/don-mua-hang',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.search();
    }

    doubleClickRow(ppOrrder: Pporder, index?: number, isEdit?: boolean) {
        if (this.dataSession) {
            this.dataSession.isEdit = isEdit;
        }
        index = !index ? this.ppOrder.indexOf(ppOrrder) : index;
        this.saveSearchData(ppOrrder, index);
        this.router.navigate(['/don-mua-hang', ppOrrder.id, 'edit', this.rowNum]);
    }

    addNew($event = null) {
        this.saveSearchData(this.selectedRow, this.index - 1);
        this.router.navigate(['/don-mua-hang/new']);
    }

    edit() {
        event.preventDefault();
        if (!this.dataSession) {
            this.dataSession = new DataSessionStorage();
        }
        if (this.selectedRow.id) {
            this.doubleClickRow(this.selectedRow, this.ppOrder.indexOf(this.selectedRow), true);
        } else {
            this.dataSession.isEdit = false;
        }
    }

    saveSearchData(ppOrder: Pporder, index: number) {
        this.selectedRow = ppOrder;
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
        if (!this.dataSession) {
            this.dataSession = new DataSessionStorage();
        }
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.pageCount = this.pageCount;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.rowNum;
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        this.dataSession.isShowSearch = this.isShowSearch;
        this.dataSession.searchVoucher = JSON.stringify(this.searchData);
        sessionStorage.setItem('DMHSearchData', JSON.stringify(this.dataSession));
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
        // this.eventManager.destroy(this.duplicateEventSubscriber);
    }

    trackId(index: number, item: IPporder) {
        return item.id;
    }

    registerChangeInPPOrder() {
        this.eventSubscriber = this.eventManager.subscribe('PPOrderListModification', response => this.search());
        // this.duplicateEventSubscriber = this.eventManager.subscribe('duplicateRefPPOrder', () => {
        //     this.modalRef = this.modalService.open(this.referencePPOrder, { backdrop: 'static' });
        // });
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

    private paginatePPOrder(data: Pporder[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.ppOrder = data;
        this.objects = data;
        if (this.page > 1 && this.ppOrder && this.ppOrder.length === 0) {
            this.page = this.page - 1;
            this.loadAllForSearch();
            return;
        }
        console.table([this.rowNum, this.index]);
        if (this.rowNum && !this.index) {
            this.index = this.rowNum % this.itemsPerPage;
            this.index = this.index || this.itemsPerPage;
            this.selectedRow = this.ppOrder[this.index - 1];
        } else if (this.index) {
            this.selectedRow = this.ppOrder[this.index - 1];
        } else {
            this.selectedRow = this.ppOrder[0];
        }
        this.rowNum = this.getRowNumberOfRecord(this.page, 0);
        this.ppOrderDetailsLoadDataClick = [];
        if (!this.ppOrderDetailsLoadDataClick.length) {
            this.quantitySum = 0;
            this.quantityReceiptSum = 0;
            this.amountOriginalSum = 0;
            this.amountSum = 0;
            this.discountAmountOriginalSum = 0;
            this.discountAmountSum = 0;
            this.vatAmountOriginalSum = 0;
            this.vatAmountSum = 0;
            this.refVoucher = [];
        }
        this.onSelect(this.selectedRow || this.ppOrder[0] || null);
        this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    updateFromDateHolder() {
        this.fromDate = this.utilsService.formatStrDate(this.fromDateHolder);
    }

    updateToDateHolder() {
        this.toDate = this.utilsService.formatStrDate(this.toDateHolder);
    }

    isForeignCurrency() {
        // Check đồng tiền hoạch toán
        if (this.selectedRow && this.selectedRow.currencyId) {
            return this.account && this.selectedRow.currencyId !== this.account.organizationUnit.currencyID;
        }
    }

    updateSum() {
        this.mainQuantitySum = 0;
        this.amountOriginalSum = 0;
        this.amountSum = 0;
        this.quantityReceiptSum = 0;
        this.quantitySum = 0;
        this.discountAmountOriginalSum = 0;
        this.discountAmountSum = 0;
        this.vatAmountOriginalSum = 0;
        this.vatAmountSum = 0;
        if (this.ppOrderDetailsLoadDataClick) {
            if (this.ppOrderDetailsLoadDataClick.length) {
                for (const item of this.ppOrderDetailsLoadDataClick) {
                    this.quantitySum += item.quantity;
                    this.quantityReceiptSum += item.quantityReceipt;
                    this.amountSum += item.amount;
                    this.amountOriginalSum += item.amountOriginal;
                    this.discountAmountOriginalSum += item.discountAmountOriginal;
                    this.discountAmountSum += item.discountAmount;
                    this.vatAmountOriginalSum += item.vatAmountOriginal;
                    this.vatAmountSum += item.vatAmount;
                    this.mainQuantitySum += item.mainQuantity;
                }
            }
        }
    }

    // updatePPOrder() {
    //     this.ppOrders.totalAmount = this.sum('amount');
    //     this.ppOrders.totalVATAmount = this.sum('vatAmount');
    //     this.ppOrders.totalDiscountAmount = this.sum('discountAmount');
    //     this.ppOrders.total = this.ppOrders.totalAmount + this.ppOrders.totalVATAmount - this.ppOrders.totalDiscountAmount;
    //     this.ppOrders.totalAmountOriginal = this.sum('amountOriginal');
    //     this.ppOrders.totalVATAmountOriginal = this.sum('vatAmountOriginal');
    //     this.ppOrders.totalDiscountAmountOriginal = this.sum('discountAmountOriginal');
    //     this.ppOrders.totalOriginal =
    //         this.ppOrders.totalAmountOriginal + this.ppOrders.totalVATAmountOriginal - this.ppOrders.totalDiscountAmountOriginal;
    // }
    //
    // sum(prop) {
    //     let total = 0;
    //     for (let i = 0; i < this.ppOrderDetailsLoadDataClick.length; i++) {
    //         total += this.ppOrderDetailsLoadDataClick[i][prop];
    //     }
    //     return total ? total : 0;
    // }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    exportExcel() {
        this.ppOrderService
            .export('excel', {
                searchDTO: JSON.stringify({
                    currency: this.currency && this.currency.currencyCode ? this.currency.currencyCode : '',
                    fromDate: this.fromDate || '',
                    toDate: this.toDate || '',
                    status: (this.status || this.status === 0) && this.status !== -1 ? this.status : '',
                    accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
                    employee: this.accountingObjectEmployee && this.accountingObjectEmployee.id ? this.accountingObjectEmployee.id : '',
                    searchValue: this.searchValue || ''
                })
            })
            .subscribe(res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_DonMuaHang.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            });
    }

    exportPdf() {
        this.ppOrderService
            .export('pdf', {
                searchDTO: JSON.stringify({
                    currency: this.currency && this.currency.currencyCode ? this.currency.currencyCode : '',
                    fromDate: this.fromDate || '',
                    toDate: this.toDate || '',
                    status: (this.status || this.status === 0) && this.status !== -1 ? this.status : '',
                    accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
                    employee: this.accountingObjectEmployee && this.accountingObjectEmployee.id ? this.accountingObjectEmployee.id : '',
                    searchValue: this.searchValue || ''
                })
            })
            .subscribe(res => {
                this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.DON_MUA_HANG);
            });
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.DON_MUA_HANG}`, () => {
            this.exportExcel();
        });
    }

    deletePPOrderAndReferences(id: string) {
        this.ppOrderService.deletePPOrderAndReferences(id).subscribe(
            res => {
                if (res.body.message === 'success' && this.modalRef) {
                    this.modalRef.close();
                    this.search();
                }
            },
            error => {
                console.log(error);
            }
        );
    }

    export() {
        this.exportPdf();
    }

    delete() {
        event.preventDefault();
        this.pPOrderService.deletePporderCheckDuplicat(this.selectedRow.id).subscribe(response => {
            if (response.body.message === 'duplicate' && this.selectedRows.length === 1) {
                this.modalRef = this.modalService.open(this.referencePPOrder, { backdrop: 'static' });
                return;
            }
            if (this.selectedRows.length > 1) {
                this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
            } else {
                if (this.selectedRow && this.selectedRow.id) {
                    this.router.navigate(['/don-mua-hang', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
                }
            }
        });
    }

    getOriginalType() {
        return this.isForeignCurrency() ? 8 : 7;
    }

    closePopUpDelete() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    continueDelete() {
        this.ppOrderService.multiDelete(this.selectedRows).subscribe(
            (res: HttpResponse<any>) => {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.selectedRows = [];
                this.search();
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
    }
}
