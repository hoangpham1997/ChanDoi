import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { IMCAudit } from 'app/shared/model/mc-audit.model';
import { Principal } from 'app/core';
import { IMCAuditDetails } from 'app/shared/model/mc-audit-details.model';
import { IMCAuditDetailMember } from 'app/shared/model/mc-audit-detail-member.model';
import { MCAuditDetailsService } from 'app/TienMatNganHang/kiem_ke_quy/mc-audit-details.service';
import { DataSessionStorage, IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { MCAuditService } from 'app/TienMatNganHang/kiem_ke_quy/mc-audit.service';
import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { MCReceipt } from 'app/shared/model/mc-receipt.model';
import { MCPayment } from 'app/shared/model/mc-payment.model';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import * as moment from 'moment';

@Component({
    selector: 'eb-mc-audit',
    templateUrl: './mc-audit.component.html',
    styleUrls: ['./mc-audit.component.css']
})
export class MCAuditComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('info') info;
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    private TYPE_MC_AUDIT = 180;
    currentAccount: any;
    mCAudits: IMCAudit[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    pageCount: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    rowNum: number;
    isShowSearch: boolean;
    selectedRow: IMCAudit;
    selectedRowDetails: IMCAuditDetails[];
    selectedRowDetailMember: IMCAuditDetailMember[];
    searchData: ISearchVoucher;
    currencys: ICurrency[];
    viewVouchersSelected: any;
    modalRef: NgbModalRef;
    mCPayment: MCPayment;
    mCReceipt: MCReceipt;
    index: number;
    currencyAccount: string;
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
    ROLE_Them = ROLE.KiemKeQuy_Them;
    ROLE_Xoa = ROLE.KiemKeQuy_Xoa;
    ROLE_KetXuat = ROLE.KiemKeQuy_KetXuat;

    constructor(
        private mCAuditService: MCAuditService,
        private mCAuditDetailsService: MCAuditDetailsService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private currencyService: CurrencyService,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        public translate: TranslateService,
        private modalService: NgbModal,
        private refModalService: RefModalService,
        public activeModal: NgbActiveModal
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition(true);
        }
    }

    transition(isTransition) {
        if (isTransition) {
            this.selectedRow = {};
        }
        this.router.navigate(['/mc-audit'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.searchVoucher();
    }

    ngOnInit() {
        this.isShowSearch = false;
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencys = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
        });
        this.selectedRow = {};
        this.selectedRowDetails = [];
        this.selectedRowDetailMember = [];
        this.searchData = {};
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
                this.currencyAccount = account.organizationUnit.currencyID;
                this.page = data.pagingParams.page;
                this.previousPage = data.pagingParams.page;
                this.reverse = data.pagingParams.ascending;
                this.registerChangeInMCAudits();
                this.registerExport();
                this.getSessionData();
            });
        });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('dataSearchMCAudit'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.searchData = JSON.parse(this.dataSession.searchVoucher);
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
            this.isShowSearch = this.dataSession.isShowSearch;
        }
        sessionStorage.removeItem('dataSearchMCAudit');
        this.transition(false);
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IMCAudit) {
        return item.id;
    }

    trackDetailId(index: number, item: IMCAuditDetails) {
        return item.id;
    }

    trackDetailMemberId(index: number, item: IMCAuditDetailMember) {
        return item.id;
    }

    registerChangeInMCAudits() {
        this.eventSubscriber = this.eventManager.subscribe('mCAuditListModification', response => this.searchVoucher());
    }

    sort() {
        const result = ['date' + ',' + (this.reverse ? 'asc' : 'desc')];
        result.push('auditDate' + ',' + (this.reverse ? 'asc' : 'desc'));
        result.push('no' + ',' + (this.reverse ? 'asc' : 'desc'));
        return result;
    }

    private paginateMCAudits(data: IMCAudit[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.mCAudits = data;
        this.objects = data;
        if (this.mCAudits && this.mCAudits.length > 0) {
            if (this.rowNum && !this.index) {
                this.index = this.rowNum % this.itemsPerPage;
                this.index = this.index || this.itemsPerPage;
                this.selectedRow = this.mCAudits[this.index - 1] ? this.mCAudits[this.index - 1] : {};
                this.selectedRows.push(this.selectedRow);
            } else if (this.index) {
                this.selectedRow = this.mCAudits[this.index - 1] ? this.mCAudits[this.index - 1] : {};
                this.selectedRows.push(this.selectedRow);
            } else {
                this.selectedRows.push(this.mCAudits[0]);
                this.selectedRow = this.mCAudits[0];
            }
            this.rowNum = this.getRowNumberOfRecord(this.page, 0);
            this.clickShowDetail(this.selectedRow && this.selectedRow.id ? this.selectedRow : this.mCAudits[0], this.index - 1);
            this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
        } else {
            this.selectedRowDetails = [];
            this.selectedRowDetailMember = [];
            this.viewVouchersSelected = [];
            this.selectedRow = {};
        }
    }

    clickShowDetail(mcAudit: IMCAudit, index: number) {
        this.selectedRow = mcAudit;
        this.index = index + 1;
        this.getDetailByID(mcAudit);
    }

    onSelect(select: IMCAudit) {
        this.selectedRow = select;
        this.mCAuditService.getDetailByID({ mCAuditID: this.selectedRow.id }).subscribe(ref => {
            this.selectedRowDetails = ref.body.mcAuditDetails;
            this.selectedRowDetailMember = ref.body.mcAuditDetailMembers;
            this.viewVouchersSelected = ref.body.refVouchers;
        });

        const index = this.mCAudits.indexOf(this.selectedRow);
        this.rowNum = this.getRowNumberOfRecord(this.page, index);
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    edit() {
        event.preventDefault();
        this.saveSearchData(this.selectedRow, this.mCAudits.indexOf(this.selectedRow));
        this.router.navigate(['./mc-audit', this.selectedRow.id, 'edit', this.rowNum]);
    }

    doubleClickRow(any?, any2?) {
        this.edit();
    }

    saveSearchData(item: IMCAudit, index: number) {
        this.selectedRow = item;
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
        sessionStorage.setItem('dataSearchMCAudit', JSON.stringify(this.dataSession));
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KiemKeQuy_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            if (this.selectedRows.filter(x => x.differAmount !== 0)) {
                this.modalRef = this.modalService.open(this.info, { backdrop: 'static' });
            } else {
                this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
            }
        } else {
            if (this.selectedRow.differAmount > 0) {
                this.mCAuditService
                    .findMCReceiptByMCAuditID({
                        ID: this.selectedRow.id
                    })
                    .subscribe((res: HttpResponse<MCReceipt>) => {
                        console.log(res.body);
                        this.mCReceipt = res.body;
                        if (this.mCReceipt !== undefined && this.mCReceipt !== null) {
                            this.modalRef = this.modalService.open(this.info, { backdrop: 'static' });
                        } else {
                            this.router.navigate(['/mc-audit', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
                        }
                    });
            } else if (this.selectedRow.differAmount < 0) {
                this.mCAuditService
                    .findMCPaymentByMCAuditID({
                        ID: this.selectedRow.id
                    })
                    .subscribe((res: HttpResponse<MCPayment>) => {
                        console.log(res.body);
                        this.mCPayment = res.body;
                        if (this.mCPayment !== undefined && this.mCPayment !== null) {
                            this.modalRef = this.modalService.open(this.info, { backdrop: 'static' });
                        } else {
                            this.router.navigate(['/mc-audit', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
                        }
                    });
            } else {
                this.router.navigate(['/mc-audit', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
            }
        }
    }

    deleteVoucher() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.selectedRows.length > 1) {
            this.mCAuditService.multiDelete(this.selectedRows).subscribe(
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
                        this.toastr.success(this.translate.instant('ebwebApp.pPDiscountReturn.delete.success'));
                    }
                    this.activeModal.close();
                    this.searchVoucher();
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
        } else {
            this.mCAuditService.delete(this.selectedRow.id).subscribe(response => {
                this.toastr.success(
                    this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'),
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
                this.searchVoucher();
            });
        }
    }

    closePopUpDelete() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KiemKeQuy_Them])
    addNew(isAdd = false) {
        event.preventDefault();
        this.router.navigate(['/mc-audit/new']);
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    selectedItemPerPage() {
        this.searchVoucher();
    }

    resetSearch() {
        this.searchData = {};
        this.searchVoucher();
    }

    searchVoucher() {
        if (!this.checkErrorForSearch()) {
            return;
        }
        this.mCAuditService
            .searchMCAudit({
                currencyID: this.searchData.currencyID ? this.searchData.currencyID : '',
                fromDate: this.searchData.fromDate ? this.searchData.fromDate.format('YYYY-MM-DD') : '',
                toDate: this.searchData.toDate ? this.searchData.toDate.format('YYYY-MM-DD') : '',
                keySearch: this.searchData.textSearch ? this.searchData.textSearch : '',
                page: this.page - 1,
                size: this.itemsPerPage
            })
            .subscribe(res => {
                this.paginateMCAudits(res.body, res.headers);
                if (!this.selectedRow && res.body) {
                    this.getDetailByID(res.body[0]);
                } else {
                    const select = res.body.find(n => n.id === this.selectedRow.id);
                    if (!select) {
                        this.getDetailByID(res.body[0]);
                    } else {
                        this.getDetailByID(select);
                    }
                }
            });
    }

    getDetailByID(item: IMCAudit) {
        this.selectedRow = item;
        this.mCAuditService.getDetailByID({ mCAuditID: this.selectedRow.id }).subscribe(ref => {
            this.selectedRowDetails = ref.body.mcAuditDetails;
            this.selectedRowDetailMember = ref.body.mcAuditDetailMembers;
            this.viewVouchersSelected = ref.body.refVouchers;
        });
    }

    private convertDateFromClient(seachVoucher: ISearchVoucher): ISearchVoucher {
        const copy: ISearchVoucher = Object.assign({}, seachVoucher, {
            fromDate: seachVoucher.fromDate != null && seachVoucher.fromDate.isValid() ? seachVoucher.fromDate.format(DATE_FORMAT) : null,
            toDate: seachVoucher.toDate != null && seachVoucher.toDate.isValid() ? seachVoucher.toDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    checkErrorForSearch() {
        if (this.searchData.fromDate && this.searchData.toDate && moment(this.searchData.toDate) < moment(this.searchData.fromDate)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.fromDateGreaterToDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
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

    private paginateUnits(data: IMCAudit[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.mCAudits = data;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    exportPdf() {
        this.mCAuditService
            .export({
                page: this.page - 1,
                size: this.itemsPerPage,
                currencyID: this.searchData.currencyID ? this.searchData.currencyID : '',
                fromDate:
                    this.searchData.fromDate != null && this.searchData.fromDate.isValid()
                        ? this.searchData.fromDate.format(DATE_FORMAT)
                        : '',
                toDate:
                    this.searchData.toDate != null && this.searchData.toDate.isValid() ? this.searchData.toDate.format(DATE_FORMAT) : '',
                textSearch: this.searchData.textSearch ? this.searchData.textSearch : ''
            })
            .subscribe(res => {
                this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.TYPE_MC_AUDIT);
            });
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.TYPE_MC_AUDIT}`, () => {
            this.exportExcel();
        });
    }

    exportExcel() {
        this.mCAuditService
            .exportExcel({
                page: this.page - 1,
                size: this.itemsPerPage,
                currencyID: this.searchData.currencyID ? this.searchData.currencyID : '',
                fromDate:
                    this.searchData.fromDate != null && this.searchData.fromDate.isValid()
                        ? this.searchData.fromDate.format(DATE_FORMAT)
                        : '',
                toDate:
                    this.searchData.toDate != null && this.searchData.toDate.isValid() ? this.searchData.toDate.format(DATE_FORMAT) : '',
                textSearch: this.searchData.textSearch ? this.searchData.textSearch : ''
            })
            .subscribe(res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);
                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_KiemKeQuy.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            });
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.selectedRowDetails.length; i++) {
            total += this.selectedRowDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KiemKeQuy_KetXuat])
    export() {
        this.exportPdf();
    }
}
