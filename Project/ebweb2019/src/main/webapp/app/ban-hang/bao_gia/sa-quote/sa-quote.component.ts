import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { ISAQuote } from 'app/shared/model/sa-quote.model';
import { Principal } from 'app/core';

import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { SAQuoteService } from './sa-quote.service';
import { ICurrency } from 'app/shared/model/currency.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { CurrencyService } from 'app/danhmuc/currency';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ISAQuoteDetails } from 'app/shared/model/sa-quote-details.model';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { ToastrService } from 'ngx-toastr';
import * as moment from 'moment';
import { TranslateService } from '@ngx-translate/core';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { ROLE } from 'app/role.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-sa-quote',
    templateUrl: './sa-quote.component.html',
    styleUrls: ['./sa-quote.component.css']
})
export class SAQuoteComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('deletePopup') deletePopup;
    currentAccount: any;
    sAQuotes: ISAQuote[];
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
    selectedRow: ISAQuote;
    isShowSearch: boolean;
    search: ISearchVoucher;
    currencys: ICurrency[];
    accountingObjects: IAccountingObject[];
    dataSession: IDataSessionStorage;
    pageCount: number;
    rowNum: number;
    sAQuoteDetails: ISAQuoteDetails[];
    employees: IAccountingObject[];
    isErrorInvalid: boolean;
    viewVouchersSelected: any;
    currencyCode: string;
    BAO_GIA = 300;
    isTGXKTT: boolean;
    // role
    ROLE_BaoGia_Xem = ROLE.BaoGia_Xem;
    ROLE_BaoGia_Them = ROLE.BaoGia_Them;
    ROLE_BaoGia_Xoa = ROLE.BaoGia_Xoa;
    ROLE_BaoGia_KetXuat = ROLE.BaoGia_KetXuat;
    isMoreForm: any;
    modalRef: NgbModalRef;
    modalRefMess: NgbModalRef;

    constructor(
        private sAQuoteService: SAQuoteService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private accountingObjectService: AccountingObjectService,
        private currencyService: CurrencyService,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private refModalService: RefModalService,
        private modalService: NgbModal
    ) {
        super();
        this.getUserAccount();
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencys = res.body.sort((a, b) => (a.currencyCode > b.currencyCode ? 1 : -1));
        });
        this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjects = res.body
                .filter(x => (x.isActive === true && x.objectType === 1) || (x.isActive === true && x.objectType === 2))
                .sort((a, b) => (a.accountingObjectCode > b.accountingObjectCode ? 1 : -1));
            this.employees = res.body
                .filter(x => x.isActive && x.isEmployee)
                .sort((a, b) => (a.accountingObjectCode > b.accountingObjectCode ? 1 : -1));
        });
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            // this.predicate = data.pagingParams.predicate;
            // add by mran
            this.predicate = 'date';
        });
        this.isMoreForm = false;
    }

    loadAllForSearch() {
        if (this.checkToDateGreaterFromDate()) {
            this.page = 1;
            this.loadAllSearchData();
        }
    }

    saveSearchVoucher(): ISearchVoucher {
        const searchObj: ISearchVoucher = {};
        searchObj.accountingObjectID = this.search.accountingObjectID !== undefined ? this.search.accountingObjectID : null;
        searchObj.employeeID = this.search.employeeID !== undefined ? this.search.employeeID : null;
        searchObj.currencyID = this.search.currencyID !== undefined ? this.search.currencyID : null;
        searchObj.fromDate = this.search.fromDate !== undefined ? this.search.fromDate : null;
        searchObj.toDate = this.search.toDate !== undefined ? this.search.toDate : null;
        searchObj.textSearch = this.search.textSearch !== undefined ? this.search.textSearch : null;
        return this.convertDateFromClient(searchObj);
    }

    private convertDateFromClient(searchVoucher: ISearchVoucher): ISearchVoucher {
        const copy: ISearchVoucher = Object.assign({}, searchVoucher, {
            fromDate:
                searchVoucher.fromDate != null && searchVoucher.fromDate.isValid() ? searchVoucher.fromDate.format(DATE_FORMAT) : null,
            toDate: searchVoucher.toDate != null && searchVoucher.toDate.isValid() ? searchVoucher.toDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    isCheckingSearchVoucher(): boolean {
        const obj = this.saveSearchVoucher();
        const objNull: ISearchVoucher = {
            accountingObjectID: null,
            currencyID: null,
            employeeID: null,
            fromDate: null,
            textSearch: null,
            toDate: null
        };
        return !this.utilsService.isEquivalent(obj, objNull);
    }

    resetSearch() {
        this.isErrorInvalid = false;
        this.search = {};
        sessionStorage.removeItem('sessionSearchVoucher');
        this.loadAllData();
    }

    loadAllSearchData() {
        this.sAQuoteService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                searchVoucher: JSON.stringify(this.saveSearchVoucher())
            })
            .subscribe(
                (res: HttpResponse<IMBTellerPaper[]>) => this.paginateSAQuotes(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadAllData() {
        this.sAQuoteService
            .getAllData({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IMBTellerPaper[]>) => this.paginateSAQuotes(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/sa-quote'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        if (this.isCheckingSearchVoucher()) {
            this.loadAllSearchData();
        } else {
            this.loadAllData();
        }
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/sa-quote',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAllSearchData();
    }

    doubleClickRow(id) {
        event.preventDefault();
        sessionStorage.setItem('sessionSearchVoucher', JSON.stringify(this.saveSearchVoucher()));
        this.router.navigate(['./sa-quote', id.id, 'edit']);
    }

    edit() {
        event.preventDefault();
        this.router.navigate(['./sa-quote', this.selectedRow.id, 'edit']);
    }

    getUserAccount() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (account && account.organizationUnit && account.organizationUnit.taxCalculationMethod === 1) {
                this.isTGXKTT = false;
            } else {
                this.isTGXKTT = true;
            }
            if (this.currentAccount) {
                this.currencyCode = this.currentAccount.organizationUnit.currencyID;
            }
        });
    }

    ngOnInit() {
        this.search = {};
        this.dataSession = {};
        this.isErrorInvalid = false;
        this.isShowSearch = false;
        if (this.isCheckingSearchVoucher()) {
            this.loadAllSearchData();
        } else {
            this.loadAllData();
        }
        this.registerChangeInSAQuotes();
        this.registerExport();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoGia_Them])
    addNew(event) {
        event.preventDefault();
        this.router.navigate(['/sa-quote/new']);
    }

    onSelect(select: ISAQuote) {
        this.selectedRow = select;
        this.sAQuoteService.find(this.selectedRow.id).subscribe((resD: HttpResponse<ISAQuote>) => {
            this.sAQuoteDetails = resD.body.sAQuoteDetails
                ? resD.body.sAQuoteDetails.sort((a, b) => a.orderPriority - b.orderPriority)
                : [];
            this.viewVouchersSelected = resD.body.viewVouchers;
        });
    }

    selectedItemPerPage() {
        if (this.isCheckingSearchVoucher()) {
            this.loadAllSearchData();
        } else {
            this.loadAllData();
        }
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISAQuote) {
        return item.id;
    }

    trackISAQuoteDetailsById(index: number, item: ISAQuoteDetails) {
        return item.id;
    }

    registerChangeInSAQuotes() {
        this.eventSubscriber = this.eventManager.subscribe('sAQuoteListModification', response => this.loadAllSearchData());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateSAQuotes(data: ISAQuote[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.sAQuotes = data;
        this.objects = data;
        //  load first element
        this.selectedRow = this.sAQuotes[0];
        this.selectedRows.push(this.sAQuotes[0]);
        // console.log(this.selectedRow);
        if (this.selectedRow) {
            this.sAQuoteService.find(this.selectedRow.id).subscribe((resD: HttpResponse<ISAQuote>) => {
                this.sAQuoteDetails = resD.body.sAQuoteDetails
                    ? resD.body.sAQuoteDetails.sort((a, b) => a.orderPriority - b.orderPriority)
                    : [];
                this.viewVouchersSelected = resD.body.viewVouchers;
            });
        } else {
            this.sAQuoteDetails = null;
            this.viewVouchersSelected = null;
        }
        //  display pageCount
        this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
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

    getTotalAmount(sAQuote: ISAQuote): number {
        sAQuote.totalAmount = sAQuote.totalAmount ? sAQuote.totalAmount : 0;
        sAQuote.totalDiscountAmount = sAQuote.totalDiscountAmount ? sAQuote.totalDiscountAmount : 0;
        sAQuote.totalVATAmount = sAQuote.totalVATAmount ? sAQuote.totalVATAmount : 0;
        return sAQuote.totalAmount - sAQuote.totalDiscountAmount + sAQuote.totalVATAmount;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoGia_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.modalRef = this.modalService.open(this.deletePopup, { size: 'lg', backdrop: 'static' });
        } else {
            this.router.navigate(['/sa-quote', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
        }
    }

    confirmDeleteList() {
        this.sAQuoteService.deleteByListID(this.selectedRows.map(n => n.id)).subscribe((res: HttpResponse<HandlingResult>) => {
            if (res.body.countSuccessVouchers > 0) {
                if (this.isCheckingSearchVoucher()) {
                    this.loadAllSearchData();
                } else {
                    this.loadAllData();
                }
            }
            if (res.body.countFailVouchers === 0) {
                this.toastr.success(this.translate.instant('ebwebApp.sAOrder.deleted'));
            } else {
                const lstFailView = [];
                res.body.listIDFail.forEach(n => {
                    const sa = this.sAQuotes.find(m => m.id === n);
                    if (sa) {
                        const viewNo: ViewVoucherNo = {};
                        viewNo.date = sa.date;
                        viewNo.noFBook = sa.no;
                        viewNo.noMBook = sa.no;
                        viewNo.reasonFail = this.translate.instant('ebwebApp.sAOrder.delete.hasRef');
                        lstFailView.push(viewNo);
                    }
                });
                res.body.listFail = lstFailView;
                const lstHideColumn = ['postedDate', 'typeID'];
                this.modalRefMess = this.refModalService.open(
                    res.body,
                    HandlingResultComponent,
                    lstHideColumn,
                    false,
                    null,
                    null,
                    null,
                    null,
                    null,
                    true
                );
            }
        });
        this.modalRef.close();
    }

    getVATRate(detail: ISAQuoteDetails): string {
        if (detail.vATRate === 0) {
            return '0%';
        } else if (detail.vATRate === 1) {
            return '5%';
        } else if (detail.vATRate === 2) {
            return '10%';
        } else if (detail.vATRate === 3) {
            return 'KCT';
        } else if (detail.vATRate === 4) {
            return 'KTT';
        } else {
            return '';
        }
    }

    getAmountOriginalType(detail: ISAQuote) {
        if (this.isForeignCurrency(detail)) {
            return 8;
        }
        return 7;
    }

    isForeignCurrency(detail: ISAQuote) {
        return this.currentAccount && detail.currencyID !== this.currentAccount.organizationUnit.currencyID;
    }

    sum(prop) {
        let total = 0;
        if (this.sAQuoteDetails) {
            for (let i = 0; i < this.sAQuoteDetails.length; i++) {
                total += this.sAQuoteDetails[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    exportExcel() {
        this.sAQuoteService
            .exportExcel({
                searchVoucher: JSON.stringify(this.saveSearchVoucher())
            })
            .subscribe(res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);
                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_BaoGia.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            });
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoGia_KetXuat])
    export() {
        event.preventDefault();
        if (this.sAQuotes && this.sAQuotes.length > 0) {
            const loading = this.toastr.success(this.translate.instant('ebwebApp.mBTellerPaper.exportToast.exporting'));
            this.sAQuoteService
                .export({
                    searchVoucher: JSON.stringify(this.saveSearchVoucher())
                })
                .subscribe(res => {
                    this.toastr.remove(loading.toastId);
                    this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.BAO_GIA);
                });
        }
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.BAO_GIA}`, () => {
            this.exportExcel();
        });
    }
}
