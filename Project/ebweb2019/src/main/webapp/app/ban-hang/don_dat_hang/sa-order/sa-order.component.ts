import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { Principal } from 'app/core';

import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { SAOrderService } from './sa-order.service';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IType } from 'app/shared/model/type.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { ISAOrder } from 'app/shared/model/sa-order.model';
import { ISAOrderDetails } from 'app/shared/model/sa-order-details.model';
import * as moment from 'moment';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { CurrencyService } from 'app/danhmuc/currency';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ROLE } from 'app/role.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-sa-order',
    templateUrl: './sa-order.component.html',
    styleUrls: ['./sa-order.component.css']
})
export class SAOrderComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('deletePopup') deletePopup;
    private TYPE_SAORDER = 310;
    currentAccount: any;
    sAOrders: ISAOrder[];
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
    /*Tìm kiếm chứng từ*/
    search: ISearchVoucher;
    types: IType[];
    currencys: ICurrency[]; //
    accountingObjects: IAccountingObject[];
    /*---*/
    isShowSearch: boolean; // Ẩn hiện thanh tìm kiếm
    selectedRow: ISAOrder; // Dòng được chọn
    saOrderDetails?: ISAOrderDetails[];
    viewVouchersSelected: any;
    currencyCode: string;

    fromDate: any;
    fromDateStr: string;
    toDate: any;
    toDateStr: string;
    validateToDate: boolean;
    validateFromDate: boolean;
    listStatus: any[];
    listColumnsStatus: string[] = ['name'];
    listHeaderColumnsStatus: string[] = ['Trạng thái'];
    employees: IAccountingObject[];
    modalRef: NgbModalRef;
    modalRefMess: NgbModalRef;

    /*Phân quyền*/
    ROLE_Them = ROLE.DonDatHang_Them;
    ROLE_Sua = ROLE.DonDatHang_Sua;
    ROLE_Xoa = ROLE.DonDatHang_Xoa;
    ROLE_KetXuat = ROLE.DonDatHang_KetXuat;

    isSingleClick: any;
    listVAT: any[] = [
        { name: '0%', data: 0 },
        { name: '5%', data: 1 },
        { name: '10%', data: 2 },
        { name: 'KCT', data: 3 },
        { name: 'KTT', data: 4 }
    ];
    index: number;

    constructor(
        private sAOrderService: SAOrderService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private accountingObjectService: AccountingObjectService,
        private currencyService: CurrencyService,
        private refModalService: RefModalService,
        private modalService: NgbModal
    ) {
        super();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (this.currentAccount) {
                this.currencyCode = this.currentAccount.organizationUnit.currencyID;
            }
        });
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjects = res.body
                .filter(n => n.objectType === 1 || n.objectType === 2)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            this.employees = res.body
                .filter(n => n.isEmployee)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
        });
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencys = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
        });

        this.translate
            .get([
                'ebwebApp.sAOrder.stt.unfulfilled',
                'ebwebApp.sAOrder.stt.processing',
                'ebwebApp.sAOrder.stt.accomplished',
                'ebwebApp.sAOrder.stt.canceled'
            ])
            .subscribe(res => {
                this.listStatus = [
                    { value: 0, name: res['ebwebApp.sAOrder.stt.unfulfilled'] },
                    { value: 1, name: res['ebwebApp.sAOrder.stt.processing'] },
                    { value: 2, name: res['ebwebApp.sAOrder.stt.accomplished'] },
                    { value: 3, name: res['ebwebApp.sAOrder.stt.canceled'] }
                ];
            });
    }

    deleteIndexSS(): void {
        sessionStorage.removeItem('page_saorder');
        sessionStorage.removeItem('size_saorder');
        sessionStorage.removeItem('index_saorder');
    }

    loadAll(search?, loadPage?): void {
        this.deleteIndexSS();
        const pr = this.activatedRoute.snapshot.queryParams;
        if (pr && !loadPage && !search) {
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
            this.index = null;
        }
        if (this.activatedRoute.snapshot.paramMap.has('isSearch')) {
            const isSearch = this.activatedRoute.snapshot.paramMap.get('isSearch');
            if (isSearch === '1') {
            } else {
                sessionStorage.removeItem('searchVoucherSAOrder');
            }
        } else {
            if (!loadPage) {
                sessionStorage.removeItem('searchVoucherSAOrder');
            }
        }
        const _search = JSON.parse(sessionStorage.getItem('searchVoucherSAOrder'));
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
            // this.statusRecord = this.search.statusRecorded ? 1 : this.search.statusRecorded === false ? 2 : null;
            this.searchVoucher();
            this.isShowSearch = true;
        } else {
            this.clearSearch();
            if (search) {
                this.page = 1;
                this.previousPage = 1;
            }
            this.sAOrderService
                .query({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<ISAOrder[]>) => {
                        this.paginateSAOrders(res.body, res.headers);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.selectedRows = [];
            this.previousPage = page;
            this.transition();
        }
    }

    clearSearch() {
        this.search = {};
        this.search.currencyID = null;
        this.search.accountingObjectID = null;
        this.search.statusRecorded = null;
        this.search.typeID = null;
        this.search.status = null;
    }

    transition() {
        if (this.activatedRoute.snapshot.paramMap.has('isSearch')) {
            this.router.navigate(['/sa-order', 'hasSearch', '1'], {
                queryParams: {
                    page: this.page,
                    size: this.itemsPerPage,
                    index: this.sAOrders.indexOf(this.selectedRow)
                }
            });
        } else {
            this.router.navigate(['/sa-order'], {
                queryParams: {
                    page: this.page,
                    size: this.itemsPerPage,
                    index: this.sAOrders.indexOf(this.selectedRow)
                }
            });
        }
        this.loadAll(false, true);
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/sa-order',
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
        this.registerChangeInSAOrders();
        this.registerExport();
    }

    /*ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }*/

    trackId(index: number, item: ISAOrder) {
        return item.id;
    }

    registerChangeInSAOrders() {
        this.eventSubscriber = this.eventManager.subscribe('sAOrderListModification', response => this.loadAll());
        this.eventSubscribers.push(this.eventSubscriber);
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateSAOrders(data: ISAOrder[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.sAOrders = data;
        this.objects = data;
        if (this.sAOrders.length > 0) {
            if (this.index) {
                const indexSAorder = this.sAOrders[this.index];
                if (indexSAorder) {
                    this.selectedRow = indexSAorder;
                } else {
                    this.selectedRow = this.sAOrders[0];
                }
            } else {
                this.selectedRow = this.sAOrders[0];
            }
            if (!this.selectedRows.find(n => n.id === this.selectedRow.id)) {
                this.selectedRows.push(this.selectedRow);
            }
            this.onSelect(this.selectedRow);
        } else {
            this.saOrderDetails = [];
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    doubleClickRow(detial) {
        this.isSingleClick = false;
        sessionStorage.setItem('page_saorder', this.page);
        sessionStorage.setItem('size_saorder', this.itemsPerPage);
        sessionStorage.setItem('index_saorder', String(this.sAOrders.indexOf(this.selectedRow)));
        this.router.navigate(['./sa-order', detial.id, 'edit']);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DonDatHang_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.modalRef = this.modalService.open(this.deletePopup, { size: 'lg', backdrop: 'static' });
        } else {
            this.router.navigate(['/sa-order', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
        }
    }

    confirmDeleteList() {
        this.sAOrderService.deleteByListID(this.selectedRows.map(n => n.id)).subscribe((res: HttpResponse<HandlingResult>) => {
            if (res.body.countSuccessVouchers > 0) {
                this.loadAll();
            }
            if (res.body.countFailVouchers === 0) {
                this.toastr.success(this.translate.instant('ebwebApp.sAOrder.deleted'));
            } else {
                const lstFailView = [];
                res.body.listIDFail.forEach(n => {
                    const sa = this.sAOrders.find(m => m.id === n);
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

    // region Tìm kiếm chứng từ add by Hautv
    searchVoucher(search?) {
        if (!this.checkErrorForSearch()) {
            return false;
        }
        sessionStorage.removeItem('searchVoucherSAOrder');
        let searchVC: ISearchVoucher = {};
        searchVC.typeID = this.TYPE_SAORDER;
        searchVC.status = this.search.status || this.search.status === 0 ? this.search.status : null;
        searchVC.currencyID = this.search.currencyID !== undefined ? this.search.currencyID : null;
        searchVC.fromDate = this.search.fromDate !== undefined ? this.search.fromDate : null;
        searchVC.toDate = this.search.toDate !== undefined ? this.search.toDate : null;
        searchVC.accountingObjectID = this.search.accountingObjectID !== undefined ? this.search.accountingObjectID : null;
        searchVC.employeeID = this.search.employeeID !== undefined ? this.search.employeeID : null;
        searchVC.textSearch = this.search.textSearch ? this.search.textSearch : null;
        searchVC = this.convertDateFromClient(searchVC);
        if (search) {
            this.page = 1;
            this.previousPage = 1;
            this.index = 0;
        }
        this.utilsService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                typeID: this.TYPE_SAORDER,
                searchVoucher: JSON.stringify(searchVC)
            })
            .subscribe(
                (res: HttpResponse<ISAOrder[]>) => this.paginateSAOrders(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        sessionStorage.setItem('searchVoucherSAOrder', JSON.stringify(searchVC));
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    resetSearch() {
        this.toDateStr = null;
        this.fromDateStr = null;
        this.selectedRows = [];
        sessionStorage.removeItem('searchVoucherSAOrder');
        this.loadAll(true);
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

    onSelect(select: ISAOrder) {
        this.isSingleClick = true;
        setTimeout(() => {
            if (this.isSingleClick) {
                this.index = this.sAOrders.indexOf(select);
                this.selectedRow = select;
                this.sAOrderService.find(select.id).subscribe((res: HttpResponse<ISAOrder>) => {
                    this.saOrderDetails =
                        res.body.saOrderDetails === undefined
                            ? []
                            : res.body.saOrderDetails.sort((a, b) => a.orderPriority - b.orderPriority);
                    this.viewVouchersSelected = res.body.viewVouchers ? res.body.viewVouchers : [];
                });
            }
        }, 250);
    }

    selectedItemPerPage() {
        this.loadAll(false, true);
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

    getNameStatus(status) {
        if (status === 0) {
            return this.translate.instant('ebwebApp.sAOrder.stt.unfulfilled');
        } else if (status === 1) {
            return this.translate.instant('ebwebApp.sAOrder.stt.processing');
        } else if (status === 2) {
            return this.translate.instant('ebwebApp.sAOrder.stt.accomplished');
        } else if (status === 3) {
            return this.translate.instant('ebwebApp.sAOrder.stt.canceled');
        }
    }

    sum(prop) {
        let total = 0;
        if (this.saOrderDetails) {
            for (let i = 0; i < this.saOrderDetails.length; i++) {
                total += this.saOrderDetails[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DonDatHang_KetXuat])
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
                typeID: this.TYPE_SAORDER,
                searchVoucher: JSON.stringify(searchVC)
            })
            .subscribe(
                res => {
                    this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.TYPE_SAORDER);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.TYPE_SAORDER}`, () => {
            this.exportExcel();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    exportExcel() {
        let searchVC: ISearchVoucher = {};
        searchVC.typeID = this.TYPE_SAORDER;
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
                typeID: this.TYPE_SAORDER,
                searchVoucher: JSON.stringify(searchVC)
            })
            .subscribe(res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_DONDATHANG.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            });
    }

    isForeignCurrency() {
        if (this.selectedRow) {
            return this.currentAccount && this.selectedRow.currencyID !== this.currentAccount.organizationUnit.currencyID;
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

    @ebAuth(['ROLE_ADMIN', ROLE.DonDatHang_Them])
    addNew($event) {
        this.router.navigate(['sa-order/new']);
    }

    edit() {
        event.preventDefault();
        if (this.selectedRow) {
            this.doubleClickRow(this.selectedRow);
        }
    }

    getVATRate(vat) {
        if (vat) {
            return this.listVAT.find(n => n.data === vat).name;
        }
    }

    checkSelect(saorder: ISAOrder) {
        return this.selectedRows.some(n => n.id === saorder.id);
    }
}
