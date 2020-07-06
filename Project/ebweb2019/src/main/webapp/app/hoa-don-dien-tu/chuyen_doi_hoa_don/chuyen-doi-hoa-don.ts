import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { Principal } from 'app/core';

import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IType } from 'app/shared/model/type.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { ISAOrder } from 'app/shared/model/sa-order.model';
import * as moment from 'moment';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { CurrencyService } from 'app/danhmuc/currency';
import { IEInvoice } from 'app/shared/model/hoa-don-dien-tu/e-invoice.model';
import { EInvoiceService } from 'app/hoa-don-dien-tu/danh_sach_hoa_don/danh-sach-hoa-don.service';
import { IEInvoiceDetails } from 'app/shared/model/hoa-don-dien-tu/e-invoice-details.model';
import { IResponeSds } from 'app/shared/model/hoa-don-dien-tu/respone-sds';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { EbViewPdfEInvocieComponent } from 'app/shared/modal/show-pdf-e-invocie/eb-view-pdf-e-invocie.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DANH_SACH_HOA_CHUYEN_DOI, EI_IDNhaCungCapDichVu, HINH_THUC_HOA_DON_HOA_DON_DIEN_TU, NCCDV_EINVOICE } from 'app/app.constants';
import { SaBillService } from 'app/ban-hang/xuat-hoa-don/sa-bill.service';
import { IaPublishInvoiceDetails } from 'app/shared/model/ia-publish-invoice-details.model';
import { ROLE } from 'app/role.constants';
import { IaPublishInvoiceDetailsService } from 'app/ban-hang/xuat-hoa-don/ia-publish-invoice-details.service';

@Component({
    selector: 'eb-chuyen-doi-hoa-don',
    templateUrl: './chuyen-doi-hoa-don.html',
    styleUrls: ['./chuyen-doi-hoa-don.css']
})
export class ChuyenDoiHoaDonComponent implements OnInit {
    @ViewChild('content') public modalComponent: NgbModalRef;
    @ViewChild('contentSIV') public modalcontentSIVComponent: NgbModalRef;
    currentAccount: any;
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
    /*Tìm kiếm hóa đơn*/
    search: ISearchVoucher;
    types: IType[];
    currencys: ICurrency[]; //
    accountingObjects: IAccountingObject[];
    templates: IaPublishInvoiceDetails[];
    series: any[];
    listStatusSendMail: any[];
    /*---*/
    isShowSearch: boolean; // Ẩn hiện thanh tìm kiếm
    selectedRow: IEInvoice; // Dòng được chọn
    eInvoiceDetails?: IEInvoiceDetails[];
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
    eInvoices: IEInvoice[];
    newList: IEInvoice[];
    modalRef: NgbModalRef;

    /*Phân quyền*/
    ROLE_ChuyenDoiCMNG = ROLE.CDHD_CHUNG_MINH_NGUON_GOC;
    ROLE_ChuyenDoiLuuTru = ROLE.CDHD_LUU_TRU;

    /*Phân quyền*/

    nameUser: string; // hóa đơn SIV
    NCCDV: string;
    NCCDV_EINVOICE = NCCDV_EINVOICE;
    listVAT: any[] = [
        { name: '0%', data: 0 },
        { name: '5%', data: 1 },
        { name: '10%', data: 2 },
        { name: 'KCT', data: 3 },
        { name: 'KTT', data: 4 }
    ];
    invoiceTemplate: any;

    constructor(
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
        private einvocieService: EInvoiceService,
        private refModalService: RefModalService,
        private modalService: NgbModal,
        private iaPublishInvoiceDetailsService: IaPublishInvoiceDetailsService
    ) {
        this.eInvoices = [];
        this.newList = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjects = res.body
                .filter(n => !n.isEmployee)
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
                'ebwebApp.eInvoice.StatusConvert.Converted',
                'ebwebApp.eInvoice.StatusConvert.Notconverted',
                'ebwebApp.eInvoice.SendMail.Sent',
                'ebwebApp.eInvoice.SendMail.Unsent'
            ])
            .subscribe(res => {
                this.listStatus = [
                    { value: 1, name: res['ebwebApp.eInvoice.StatusConvert.Converted'] },
                    { value: 0, name: res['ebwebApp.eInvoice.StatusConvert.Notconverted'] }
                ];

                this.listStatusSendMail = [
                    { value: 1, name: res['ebwebApp.eInvoice.SendMail.Sent'] },
                    { value: 0, name: res['ebwebApp.eInvoice.SendMail.Unsent'] }
                ];
            });

        this.iaPublishInvoiceDetailsService.getAllByCompany().subscribe(res => {
            this.templates = res.body.filter(n => n.invoiceForm === HINH_THUC_HOA_DON_HOA_DON_DIEN_TU);
            this.series = [];
            this.templates.map(n => n.invoiceSeries).forEach(n => {
                this.series.push({ name: n });
            });
        });
    }

    loadAll(search?, loadPage?) {
        if (this.activatedRoute.snapshot.paramMap.has('isSearch')) {
            const isSearch = this.activatedRoute.snapshot.paramMap.get('isSearch');
            if (isSearch === '1') {
            } else {
                sessionStorage.removeItem('searchVoucherEInvoice');
            }
        } else {
            if (!loadPage) {
                sessionStorage.removeItem('searchVoucherEInvoice');
            }
        }
        const _search = JSON.parse(sessionStorage.getItem('searchVoucherEInvoice'));
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
                this.newList = [];
            }
            this.einvocieService
                .getEInvoiceForConvert({
                    page: this.page - 1,
                    size: this.itemsPerPage
                })
                .subscribe(
                    (res: HttpResponse<IEInvoice[]>) => {
                        this.paginateEInvoices(res.body, res.headers);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.newList = [];
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
        this.search.statusSendMail = null;
    }

    transition() {
        this.router.navigate(['/chuyen-doi-hoa-don'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage
                // sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                // sort: this.sort()
            }
        });
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
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (this.currentAccount) {
                this.currencyCode = this.currentAccount.organizationUnit.currencyID;
                this.NCCDV = this.currentAccount.systemOption.find(x => x.code === EI_IDNhaCungCapDichVu).data;
            }
        });
        this.registerConnectEInvocie();
    }

    trackId(index: number, item: IEInvoice) {
        return item.id;
    }

    private paginateEInvoices(data: IEInvoice[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.eInvoices = data;
        if (this.newList) {
            this.eInvoices.forEach(item => {
                item.checked = this.newList.some(dataE => dataE.id === item.id);
            });
        }
        if (this.eInvoices.length > 0) {
            this.selectedRow = this.eInvoices[0];
            this.einvocieService.find(this.selectedRow.id).subscribe((resD: HttpResponse<IEInvoice>) => {
                this.eInvoiceDetails =
                    resD.body.eInvoiceDetails === undefined
                        ? []
                        : resD.body.eInvoiceDetails.sort((a, b) => a.orderPriority - b.orderPriority);
            });
        } else {
            this.eInvoiceDetails = [];
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    doubleClickRow(id: string) {
        const einvoice: IEInvoice = this.eInvoices.find(n => n.id === id);
        if (einvoice.statusConverted) {
            this.einvocieService
                .viewInvoiceConvertedOrigin({
                    id,
                    pattern: einvoice.invoiceTemplate
                })
                .subscribe(
                    (res: HttpResponse<IResponeSds>) => {
                        const resp = res.body;
                        if (resp.status === 0) {
                            this.toastr.warning(
                                resp.message ? resp.message : 'Có lỗi xảy ra khi download file',
                                this.translate.instant('ebwebApp.mCReceipt.error.error')
                            );
                        } else {
                            this.refModalService.open(
                                einvoice,
                                EbViewPdfEInvocieComponent,
                                res,
                                false,
                                einvoice.typeID,
                                'width-80 height-100'
                            );
                        }
                        // var blob = new Blob([byteArray], {type: 'application/pdf'});
                        // const data = new Blob(res.body, { type: 'application/pdf' });
                        /*const fileURL = URL.createObjectURL(res.body);
                    window.open(fileURL);*/
                    },
                    (res: any) => this.onError(res.title)
                );
        } else {
            /* this.toastr.warning(
                'Hóa đơn chưa được chuyển đổi chứng minh nguồn gốc',
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );*/

            // Vierw hóa đơn
            this.einvocieService
                .viewInvoicePdf({
                    id,
                    pattern: einvoice.invoiceTemplate
                })
                .subscribe(
                    (res: HttpResponse<IResponeSds>) => {
                        const resp = res.body;
                        if (resp.status === 0) {
                            this.toastr.warning(
                                resp.message ? resp.message : 'Có lỗi xảy ra khi download file',
                                this.translate.instant('ebwebApp.mCReceipt.error.error')
                            );
                        } else {
                            this.refModalService.open(
                                einvoice,
                                EbViewPdfEInvocieComponent,
                                res,
                                false,
                                einvoice.typeID,
                                'width-80 height-100'
                            );
                        }
                        // var blob = new Blob([byteArray], {type: 'application/pdf'});
                        // const data = new Blob(res.body, { type: 'application/pdf' });
                        /*const fileURL = URL.createObjectURL(res.body);
                    window.open(fileURL);*/
                    },
                    (res: any) => this.onError(res.title)
                );
        }
    }

    delete() {
        this.router.navigate(['/', { outlets: { popup: 'sa-order/' + this.selectedRow.id + '/delete' } }]);
    }

    // region Tìm kiếm hóa đơn add by Hautv
    searchVoucher(search?) {
        if (!this.checkErrorForSearch()) {
            return false;
        }
        sessionStorage.removeItem('searchVoucherEInvoice');
        let searchVC: ISearchVoucher = {};
        // searchVC.typeID = this.TYPE_SAORDER;
        searchVC.status = this.search.status;
        searchVC.statusSendMail = this.search.statusSendMail;
        searchVC.fromDate = this.search.fromDate !== undefined ? this.search.fromDate : null;
        searchVC.toDate = this.search.toDate !== undefined ? this.search.toDate : null;
        searchVC.accountingObjectID = this.search.accountingObjectID !== undefined ? this.search.accountingObjectID : null;
        searchVC.textSearch = this.search.textSearch ? this.search.textSearch : null;
        searchVC.invoiceTemplate = this.search.invoiceTemplate ? this.search.invoiceTemplate : null;
        searchVC.invoiceSeries = this.search.invoiceSeries ? this.search.invoiceSeries : null;
        searchVC = this.convertDateFromClient(searchVC);
        if (search) {
            this.newList = [];
            this.page = 1;
            this.previousPage = 1;
        }
        this.einvocieService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                searchVoucher: JSON.stringify(searchVC),
                typeEInvoice: DANH_SACH_HOA_CHUYEN_DOI
            })
            .subscribe(
                (res: HttpResponse<ISAOrder[]>) => this.paginateEInvoices(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        sessionStorage.setItem('searchVoucherEInvoice', JSON.stringify(searchVC));
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    resetSearch() {
        this.toDateStr = null;
        this.fromDateStr = null;
        this.invoiceTemplate = null;
        sessionStorage.removeItem('searchVoucherEInvoice');
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

    onSelect(select: IEInvoice) {
        if (this.selectedRow.id === select.id) {
            return;
        }
        this.selectedRow = select;
        this.einvocieService.find(select.id).subscribe((res: HttpResponse<IEInvoice>) => {
            this.eInvoiceDetails =
                res.body.eInvoiceDetails === undefined ? [] : res.body.eInvoiceDetails.sort((a, b) => a.orderPriority - b.orderPriority);
        });
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

    sum(prop) {
        let total = 0;
        if (this.eInvoiceDetails) {
            for (let i = 0; i < this.eInvoiceDetails.length; i++) {
                total += this.eInvoiceDetails[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    check(eInvoice: IEInvoice) {
        eInvoice.checked = !eInvoice.checked;
        if (eInvoice.checked) {
            this.newList.push(eInvoice);
        } else {
            for (let i = 0; i < this.newList.length; i++) {
                if (this.newList[i].id === eInvoice.id) {
                    this.newList.splice(i, 1);
                    i--;
                }
            }
        }
    }

    isCheckAll() {
        return this.eInvoices.every(item => item.checked) && this.eInvoices.length;
    }

    checkAll() {
        const isCheck = this.eInvoices.every(item => item.checked) && this.eInvoices.length;
        this.eInvoices.forEach(item => (item.checked = !isCheck));

        if (!isCheck) {
            this.newList = [];
            this.eInvoices.forEach(eInvoice => {
                this.newList.push(eInvoice);
            });
        } else {
            for (let j = 0; j < this.eInvoices.length; j++) {
                for (let i = 0; i < this.newList.length; i++) {
                    if (this.newList[i].id === this.eInvoices[j].id) {
                        this.newList.splice(i, 1);
                        i--;
                    }
                }
            }
        }
    }

    padLeft(text: string, padChar: string, size: number): string {
        return (String(padChar).repeat(size) + text).substr(size * -1, size);
    }

    registerConnectEInvocie() {
        this.eventSubscriber = this.eventManager.subscribe('connectEInvocieSuccess', response => {
            this.loadAll();
        });
    }

    convertedOrigin(content) {
        if (this.newList.length > 0) {
            if (this.newList.some(n => n.statusConverted)) {
                let mess = 'Hóa đơn số ';
                const lst = this.newList.filter(n => n.statusConverted);
                for (let i = 0; i < lst.length; i++) {
                    mess += lst[i].invoiceNo;
                    if (i < lst.length - 1) {
                        mess += ', ';
                    }
                }
                mess += ' đã chuyển đổi chứng minh nguồn gốc';
                this.toastr.warning(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
            } else {
                this.modalRef = this.modalService.open(content, { backdrop: 'static' });
            }
        } else {
            this.toastr.warning('Chưa chọn hóa đơn', this.translate.instant('ebwebApp.mCReceipt.error.error'));
        }
    }

    ConvertedStorage() {
        if (this.newList.length > 1) {
            this.toastr.warning('Chỉ được chọn 1 hóa đơn để chuyển đổi lưu trữ', this.translate.instant('ebwebApp.mCReceipt.error.error'));
        } else if (this.newList.length === 0) {
            this.toastr.warning('Chưa chọn hóa đơn', this.translate.instant('ebwebApp.mCReceipt.error.error'));
        } else {
            const einvoice: IEInvoice = this.eInvoices.find(n => n.id === this.newList[0]);
            this.einvocieService
                .convertedStorage({
                    id: this.newList[0].id,
                    pattern: this.newList[0].invoiceTemplate
                })
                .subscribe((res: HttpResponse<any>) => {
                    const resp = res.body;
                    if (resp.status === 0) {
                        this.toastr.warning(
                            resp.message ? resp.message : 'Có lỗi xảy ra khi download file',
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else {
                        this.refModalService.open(
                            null,
                            EbViewPdfEInvocieComponent,
                            res,
                            false,
                            this.newList[0].typeID,
                            'width-80 height-100'
                        );
                    }
                });
        }
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    continue() {
        this.modalRef.close();
        switch (this.NCCDV) {
            case NCCDV_EINVOICE.SDS:
                this.continueSDS();
                break;
            case NCCDV_EINVOICE.SIV:
                this.modalRef = this.modalService.open(this.modalcontentSIVComponent, { backdrop: 'static' });
                break;
            case NCCDV_EINVOICE.MIV:
                break;
            case NCCDV_EINVOICE.VNPT:
                break;
        }
    }

    continueSDS() {
        this.einvocieService
            .convertedOrigin({
                uuidList: this.newList.map(n => n.id),
                nameUser: this.nameUser
            })
            .subscribe((res: HttpResponse<IResponeSds>) => {
                if (res.body.status === 1) {
                    this.toastr.warning(res.body.message, this.translate.instant('ebwebApp.mCReceipt.home.message'));
                    return;
                }
                if (res.body.data.keyInvoiceMsgDTO.length > 0) {
                    let mess = '';
                    if (res.body.data.keyInvoiceMsgDTO.length === 1) {
                        mess += res.body.data.keyInvoiceMsgDTO[0].message;
                    } else {
                        for (let i = 0; i < res.body.data.keyInvoiceMsgDTO.length; i++) {
                            const e = this.newList.find(n => n.id === res.body.data.keyInvoiceMsgDTO[i].id);
                            if (e) {
                                mess += 'Số ' + e.invoiceNo + ': ' + res.body.data.keyInvoiceMsgDTO[i].message + '\n';
                            } else {
                                mess += res.body.data.keyInvoiceMsgDTO[i].id + ': ' + res.body.data.keyInvoiceMsgDTO[i].message + '\n';
                            }
                        }
                    }
                    this.toastr.error(mess, this.translate.instant('ebwebApp.mCReceipt.home.message'));
                    if (res.body.data.keyInvoiceNoDTO.length > 0) {
                        let messSucess = 'Số hóa đơn ';
                        for (let i = 0; i < res.body.data.keyInvoiceNoDTO.length; i++) {
                            const e = this.eInvoices.find(n => n.id === res.body.data.keyInvoiceNoDTO[i].id);
                            const eS = this.newList.find(n => n.id === res.body.data.keyInvoiceNoDTO[i].id);
                            if (e) {
                                e.statusConverted = true;
                            }
                            messSucess += eS.invoiceNo;
                            if (i < res.body.data.keyInvoiceNoDTO.length - 1) {
                                messSucess += ', ';
                            }
                        }
                        messSucess += ' chuyển đổi chứng minh nguồn gốc thành công';
                        this.newList = [];
                        this.eInvoices.forEach(n => (n.checked = false));
                        this.toastr.success(messSucess, this.translate.instant('ebwebApp.mCReceipt.home.message'));
                    }
                } else {
                    let mess = 'Số hóa đơn ';
                    for (let i = 0; i < res.body.data.keyInvoiceNoDTO.length; i++) {
                        const e = this.eInvoices.find(n => n.id === res.body.data.keyInvoiceNoDTO[i].id);
                        const eS = this.newList.find(n => n.id === res.body.data.keyInvoiceNoDTO[i].id);
                        if (e) {
                            e.statusConverted = true;
                        }
                        mess += eS.invoiceNo;
                        if (i < res.body.data.keyInvoiceNoDTO.length - 1) {
                            mess += ', ';
                        }
                    }
                    mess += ' chuyển đổi chứng minh nguồn gốc thành công';
                    this.newList = [];
                    this.eInvoices.forEach(n => (n.checked = false));
                    this.toastr.success(mess, this.translate.instant('ebwebApp.mCReceipt.home.message'));
                }
                if (this.modalRef) {
                    this.nameUser = null;
                    this.modalRef.close();
                }
            });
    }

    closeContentSIV() {
        this.modalRef.close();
        this.nameUser = null;
    }

    changeInvoiceTemplate() {
        if (this.invoiceTemplate) {
            this.search.invoiceTemplate = this.invoiceTemplate.invoiceTemplate;
            this.search.invoiceSeries = this.invoiceTemplate.invoiceSeries;
        }
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

    getVATRate(vat) {
        if (vat) {
            return this.listVAT.find(n => n.data === vat).name;
        }
    }
}
