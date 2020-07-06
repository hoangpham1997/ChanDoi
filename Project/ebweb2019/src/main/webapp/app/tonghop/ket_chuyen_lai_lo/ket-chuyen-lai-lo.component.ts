import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { IPPInvoice } from 'app/shared/model/pp-invoice.model';
import { Principal } from 'app/core';
import { TranslateService } from '@ngx-translate/core';
import { ITEMS_PER_PAGE } from 'app/shared';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CurrencyService } from 'app/danhmuc/currency';
import { DataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { IRefVoucher } from 'app/shared/model/ref-voucher.model';
import { MAU_BO_GHI_SO, MSGERROR, SO_LAM_VIEC, TypeID } from 'app/app.constants';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { ToastrService } from 'ngx-toastr';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { DatePipe } from '@angular/common';
import * as moment from 'moment';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { KetChuyenLaiLoService } from 'app/tonghop/ket_chuyen_lai_lo/ket-chuyen-lai-lo.service';
import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-ket-chuyen-lai-lo',
    templateUrl: './ket-chuyen-lai-lo.component.html',
    styleUrls: ['ket-chuyen-lai-lo.css']
})
export class KetChuyenLaiLoComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('deleteItem') deleteItem: any;
    currentAccount: any;
    gOtherVouchers: IGOtherVoucher[];
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
    keySearch: any;
    pageCount: any;
    gOtherVoucherDetails: any[];
    selectedRow: IPPInvoice;
    checkModalRef: NgbModalRef;
    isShowSearch = false;
    rowNum: number;
    searchData: any;
    refVouchers?: IRefVoucher[];
    dataSession: any;
    status: number;
    searchValue: string;
    account: any;
    currentBook: string;
    isErrorInvalid: any;
    fromDateHolder: any;
    fromDateHolderMask: any;
    toDateHolder: any;
    toDateHolderMask: any;
    index: number;
    isLoading = false;
    ROLE = ROLE;

    constructor(
        private ketChuyenLaiLoService: KetChuyenLaiLoService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private modalService: NgbModal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        public datepipe: DatePipe,
        private toastrService: ToastrService,
        public translateService: TranslateService,
        private generalLedgerService: GeneralLedgerService,
        private currencyService: CurrencyService,
        private accountingObjectService: AccountingObjectService,
        private eventManager: JhiEventManager,
        private refModalService: RefModalService,
        public utilService: UtilsService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.selectedRow = {};
        this.gOtherVouchers = [];
    }

    ngOnInit() {
        this.init();
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.registerExport();
            this.registerChangeInPPInvoices();

            this.onSetUpHolderMask();
            this.getAccount();
            this.getSessionData();
        });
        this.registerLockSuccess();
        this.registerUnlockSuccess();
    }

    registerLockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('lockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
            this.getAccount();
            this.getSessionData();
        });
    }

    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
            this.getAccount();
            this.getSessionData();
        });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('ketChuyenSearchData'));

        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.searchData = JSON.parse(this.dataSession.searchVoucher);
            if (this.searchData) {
                this.fromDate = this.searchData.fromDate;
                this.toDate = this.searchData.toDate;
                this.status = this.searchData.status;
                this.searchValue = this.searchData.searchValue;
            }
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
            this.isShowSearch = this.dataSession.isShowSearch;
        }
        sessionStorage.removeItem('ketChuyenSearchData');
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
        this.status = null;
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

    search() {
        this.ketChuyenLaiLoService
            .searchGOtherVoucher({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: this.status || this.status === 0 ? this.status : '',
                keySearch: this.searchValue || ''
            })
            .subscribe(res => {
                this.paginatePPInvoices(res.body, res.headers);
            });
    }

    transition(isTransition) {
        if (isTransition) {
            this.selectedRow = {};
        }
        this.router.navigate(['/ket-chuyen-lai-lo'], {
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
            '/ket-chuyen-lai-lo',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.search();
    }

    doubleClickRowGO(item: IPPInvoice, index: number, isEdit) {
        this.saveSearchData(item, index, isEdit);
        this.router.navigate(['/ket-chuyen-lai-lo', item.id, 'edit', this.rowNum]);
    }
    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenLaiLo_Them])
    addNew($event = null) {
        this.saveSearchData(this.selectedRow, this.index - 1, true);
        this.router.navigate(['/ket-chuyen-lai-lo/new']);
    }

    saveSearchData(item: IPPInvoice, index: number, isEdit: boolean) {
        this.selectedRow = item;
        this.searchData = {
            fromDate: this.fromDate || '',
            toDate: this.toDate || '',
            status: this.status,
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
        this.dataSession.isEdit = isEdit;
        this.dataSession.searchVoucher = JSON.stringify(this.searchData);
        sessionStorage.setItem('ketChuyenSearchData', JSON.stringify(this.dataSession));
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

    private paginatePPInvoices(data: IGOtherVoucher[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.gOtherVouchers = data;
        this.objects = data;
        if (this.gOtherVouchers && this.gOtherVouchers.length > 0) {
            // console.table([this.rowNum, this.index]);
            if (this.rowNum && !this.index) {
                this.index = this.rowNum % this.itemsPerPage;
                this.index = this.index || this.itemsPerPage;
                this.selectedRow = this.gOtherVouchers[this.index - 1] ? this.gOtherVouchers[this.index - 1] : {};
            } else if (this.index) {
                this.selectedRow = this.gOtherVouchers[this.index - 1] ? this.gOtherVouchers[this.index - 1] : {};
            }
            this.rowNum = this.getRowNumberOfRecord(this.page, 0);
            this.clickShowDetail(this.selectedRow && this.selectedRow.id ? this.selectedRow : this.gOtherVouchers[0], this.index - 1);
            this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
        } else {
            this.gOtherVoucherDetails = [];
            this.refVouchers = [];
            this.selectedRow = {};
        }
    }

    getAccount() {
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.account = account;
                if (this.account) {
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
        this.gOtherVouchers = [];
        this.gOtherVoucherDetails = [];
        this.status = null;
        this.selectedRow = {};
        this.account = { organizationUnit: {} };
    }

    registerChangeInPPInvoices() {
        this.eventSubscriber = this.eventManager.subscribe('pPInvoiceListModification', response => this.search());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    clearSearch() {
        this.keySearch = '';
    }

    getDetailByID(item: IGOtherVoucher) {
        // this.ppInvoiceDetails = item.ppInvoiceDetails;
        this.selectedRow = item;
        this.ketChuyenLaiLoService.findDetailById({ id: item.id }).subscribe(res => {
            this.gOtherVoucherDetails = res.body;
        });

        // tham chiếu
        this.ketChuyenLaiLoService.findRefVoucherByRefId({ refId: item.id }).subscribe((res: HttpResponse<IRefVoucher[]>) => {
            this.refVouchers = res.body;
        });
    }
    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenLaiLo_Xoa])
    delete() {
        event.preventDefault();
        if (this.gOtherVouchers && this.gOtherVouchers.length > 0 && this.selectedRow && !this.selectedRow.recorded && !this.isLoading) {
            this.openModel1(this.deleteItem);
        }
    }

    openModel1(content) {
        this.checkModalRef = this.modalService.open(content, { size: 'lg', backdrop: 'static' });
    }
    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenLaiLo_GhiSo])
    record() {
        event.preventDefault();
        if (
            this.gOtherVouchers &&
            this.gOtherVouchers.length > 0 &&
            this.selectedRow &&
            this.selectedRow.id &&
            !this.selectedRow.recorded &&
            !this.isLoading
        ) {
            this.recordGO();
        }
    }

    recordGO() {
        const recordData = { id: this.selectedRow.id, typeID: TypeID.KET_CHUYEN_LAI_LO };
        this.isLoading = true;
        this.generalLedgerService.record(recordData).subscribe((res: HttpResponse<Irecord>) => {
            if (res.body.success) {
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBTellerPaper.recordToast.done'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                );
                this.selectedRow.recorded = true;
            } else {
                if (res.body.msg === MSGERROR.KC_DU_LIEU_AM) {
                    this.toastrService.error(
                        this.translateService.instant('global.messages.error.kcDataError'),
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
    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenLaiLo_GhiSo])
    unrecord() {
        event.preventDefault();
        if (
            this.gOtherVouchers &&
            this.gOtherVouchers.length > 0 &&
            this.selectedRow &&
            this.selectedRow.id &&
            this.selectedRow.recorded &&
            !this.isLoading &&
            !this.checkCloseBook(this.account, this.selectedRow.postedDate)
        ) {
            this.unRecordGO();
        }
    }

    unRecordGO() {
        const recordData = {
            id: this.selectedRow.id,
            typeID: TypeID.KET_CHUYEN_LAI_LO
        };
        this.isLoading = true;
        this.generalLedgerService.unrecord(recordData).subscribe((res: HttpResponse<Irecord>) => {
            if (res.body.success) {
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.done'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                );
                this.selectedRow.recorded = false;
            } else {
                this.toastrService.error(
                    this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.failed'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.error.error')
                );
            }
            this.isLoading = false;
        });
    }

    deleteGO() {
        if (this.selectedRow && this.selectedRow.id) {
            this.isLoading = true;
            this.ketChuyenLaiLoService.deleteById({ id: this.selectedRow.id }).subscribe(
                (res: HttpResponse<any>) => {
                    if (res.body.message === UpdateDataMessages.DELETE_SUCCESS) {
                        this.toastrService.success(this.translateService.instant('ebwebApp.pPInvoice.success.deleteSuccess'));
                        this.search();
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

    exportExcel() {
        this.ketChuyenLaiLoService
            .export('excel', {
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: this.status || this.status === 0 ? this.status : '',
                searchValue: this.searchValue || ''
            })
            .subscribe(res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_KetChuyenLaiLo.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            });
    }
    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenLaiLo_KetXuat])
    export() {
        event.preventDefault();
        if (this.gOtherVouchers && this.gOtherVouchers.length > 0 && !this.isLoading) {
            this.exportPdf();
        }
    }

    exportPdf() {
        this.ketChuyenLaiLoService
            .export('pdf', {
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: this.status || this.status === 0 ? this.status : '',
                searchValue: this.searchValue || ''
            })
            .subscribe(res => {
                this.refModalService.open(null, EbReportPdfPopupComponent, res, false, TypeID.KET_CHUYEN_LAI_LO);
            });
    }

    registerExport() {
        this.eventManager.subscribe(`export-excel-${TypeID.KET_CHUYEN_LAI_LO}`, () => {
            this.exportExcel();
        });
    }

    changeAmountPPInvoice(ppInvoiceDetails) {
        for (const detail of ppInvoiceDetails) {
        }
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    doubleClickRow(any?) {
        event.preventDefault();
        if (this.index) {
            this.index = this.index - 1;
        }
        this.doubleClickRowGO(this.selectedRow, this.index, false);
    }
    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenLaiLo_Sua])
    edit() {
        event.preventDefault();
        if (!this.selectedRow.recorded) {
            this.doubleClickRowGO(this.selectedRow, this.index, true);
        }
    }

    onSelect() {
        this.clickShowDetail(this.selectedRow, this.index);
    }
}
