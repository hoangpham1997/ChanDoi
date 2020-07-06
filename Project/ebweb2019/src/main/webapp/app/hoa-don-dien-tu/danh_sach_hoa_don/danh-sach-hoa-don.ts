import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { Principal } from 'app/core';

import { DATE_FORMAT, DATE_FORMAT_SEARCH, ITEMS_PER_PAGE } from 'app/shared';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ICurrency } from 'app/shared/model/currency.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { ISAOrder } from 'app/shared/model/sa-order.model';
import * as moment from 'moment';
import { Moment } from 'moment';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { CurrencyService } from 'app/danhmuc/currency';
import { IEInvoice } from 'app/shared/model/hoa-don-dien-tu/e-invoice.model';
import { EInvoiceService } from 'app/hoa-don-dien-tu/danh_sach_hoa_don/danh-sach-hoa-don.service';
import { IEInvoiceDetails } from 'app/shared/model/hoa-don-dien-tu/e-invoice-details.model';
import { IResponeSds, ResponeSds } from 'app/shared/model/hoa-don-dien-tu/respone-sds';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { EbViewPdfEInvocieComponent } from 'app/shared/modal/show-pdf-e-invocie/eb-view-pdf-e-invocie.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import {
    CheckHDCD,
    DANH_SACH_HOA_DON,
    EI_IDNhaCungCapDichVu,
    EINVOICE,
    EINVOICE_RESPONE,
    EINVOICE_STATUS,
    HINH_THUC_HOA_DON_HOA_DON_DIEN_TU,
    NCCDV_EINVOICE,
    SignType,
    TCKHAC_SDTichHopHDDT
} from 'app/app.constants';
import { IaPublishInvoiceDetails } from 'app/shared/model/ia-publish-invoice-details.model';
import { SaBillService } from 'app/ban-hang/xuat-hoa-don/sa-bill.service';
import { SignaturesPopupComponent } from 'app/hoa-don-dien-tu/danh_sach_hoa_don/signatures-popup.component';
import { IRequestSDS } from 'app/shared/model/hoa-don-dien-tu/request-sds';
import { IDigestDataDTO } from 'app/shared/model/hoa-don-dien-tu/respone-sds-data';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { IaPublishInvoiceDetailsService } from 'app/ban-hang/xuat-hoa-don/ia-publish-invoice-details.service';

@Component({
    selector: 'eb-danh-sach-hoa-don',
    templateUrl: './danh-sach-hoa-don.html',
    styleUrls: ['./danh-sach-hoa-don.css']
})
export class DanhSachHoaDonComponent extends BaseComponent implements OnInit {
    @ViewChild('cancelIV') public modalCancelIVComponent: NgbModalRef;
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
    question: string;
    optionQuestion: number;
    modalRef: NgbModalRef;
    email: string;
    isTichHop: boolean;
    useInvoiceWait: boolean;
    useSignToken: boolean;
    /*Chứng thư*/
    certificate: any;
    certificateSerial: any;
    digiDocChrome = 'TokenSigning';
    plugin: any;
    option = { lang: 'en' };
    dataHash: any[];
    sigData: any[];
    USER_CANCEL = 'userCancel';
    INVALID_ARGUMENT = 'invalidArgument';
    TECHNICAL_ERROR = 'technicalError';
    NOT_ALLOWED = 'notAllowed';

    /*Chứng thư*/

    /*Phân quyền*/
    ROLE_PhatHanhHD = ROLE.DSHD_PHAT_HANH;
    ROLE_ThayThe = ROLE.DSHD_THAY_THE_HOA_DON;
    ROLE_DieuChinh = ROLE.DSHD_DIEU_CHINH_HOA_DON;
    ROLE_HuyBo = ROLE.DSHD_HUY_BO_HOA_DON;
    ROLE_GuiEmail = ROLE.DSHD_GUI_EMAIL;
    ROLE_TaoCho = ROLE.DSHD_TAO_CHO;

    /*Phân quyền*/

    NCCDV: string;
    NCCDV_EINVOICE = NCCDV_EINVOICE;
    nameDocument: string;
    note: string;
    dateDocument: Moment;

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
        private saBillService: SaBillService,
        private iaPublishInvoiceDetailsService: IaPublishInvoiceDetailsService
    ) {
        super();
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
                'ebwebApp.eInvoice.Status.New',
                'ebwebApp.eInvoice.Status.Signed',
                'ebwebApp.eInvoice.Status.Replaced',
                'ebwebApp.eInvoice.Status.Adjusted',
                'ebwebApp.eInvoice.Status.Destruction',
                'ebwebApp.eInvoice.Status.ReplacedNew',
                'ebwebApp.eInvoice.Status.AdjustNew',
                'ebwebApp.eInvoice.SendMail.Sent',
                'ebwebApp.eInvoice.SendMail.Unsent'
            ])
            .subscribe(res => {
                if (this.NCCDV === NCCDV_EINVOICE.MIV) {
                    this.listStatus = [
                        { value: 0, name: res['ebwebApp.eInvoice.Status.New'] },
                        { value: 1, name: res['ebwebApp.eInvoice.Status.Signed'] },
                        { value: 4, name: res['ebwebApp.eInvoice.Status.Adjusted'] },
                        { value: 5, name: res['ebwebApp.eInvoice.Status.Destruction'] },
                        { value: 8, name: res['ebwebApp.eInvoice.Status.AdjustNew'] }
                    ];
                } else {
                    this.listStatus = [
                        { value: 0, name: res['ebwebApp.eInvoice.Status.New'] },
                        { value: 1, name: res['ebwebApp.eInvoice.Status.Signed'] },
                        { value: 3, name: res['ebwebApp.eInvoice.Status.Replaced'] },
                        { value: 4, name: res['ebwebApp.eInvoice.Status.Adjusted'] },
                        { value: 5, name: res['ebwebApp.eInvoice.Status.Destruction'] },
                        { value: 7, name: res['ebwebApp.eInvoice.Status.ReplacedNew'] },
                        { value: 8, name: res['ebwebApp.eInvoice.Status.AdjustNew'] }
                    ];
                }
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
                // this.newList = [];
                this.page = 1;
                this.previousPage = 1;
            }
            this.einvocieService
                .query({
                    page: this.page - 1,
                    size: this.itemsPerPage
                    // sort: this.sort()
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
            // this.newList = [];
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
        this.router.navigate(['/danh-sach-hoa-don'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage
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
                this.isTichHop = this.currentAccount.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
                this.useInvoiceWait = this.currentAccount.systemOption.some(x => x.code === CheckHDCD && x.data === '1');
                this.useSignToken = this.currentAccount.systemOption.some(x => x.code === SignType && x.data === '1');
                this.NCCDV = this.currentAccount.systemOption.find(x => x.code === EI_IDNhaCungCapDichVu).data;
                /*if (this.NCCDV === NCCDV_EINVOICE.MIV) {
                    this.einvocieService.loadDataTokenMIV({}).subscribe((res: HttpResponse<ResponeSds>) => {});
                }*/
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
                item.checked = this.newList.some(dataS => dataS.id === item.id);
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

    private onError(errorMessage) {
        this.toastr.error(errorMessage, this.translate.instant('ebwebApp.mCReceipt.error.error'));
    }

    doubleClickRow(einvoice: IEInvoice) {
        switch (this.NCCDV) {
            case NCCDV_EINVOICE.SDS:
                this.getViewPDF(einvoice);
                break;
            case NCCDV_EINVOICE.SIV:
                if (
                    einvoice.statusInvoice === EINVOICE.HOA_DON_MOI_TAO_LAP_THAY_THE ||
                    einvoice.statusInvoice === EINVOICE.HOA_DON_MOI_TAO_LAP_DIEU_CHINH
                ) {
                    this.openRefVoucher(einvoice);
                } else {
                    this.getViewPDF(einvoice);
                }
                break;
            case NCCDV_EINVOICE.MIV:
                this.getViewPDF(einvoice);
                break;
            case NCCDV_EINVOICE.VNPT:
                this.getViewPDF(einvoice);
                break;
        }
    }

    getViewPDF(einvoice: IEInvoice) {
        this.einvocieService
            .viewInvoicePdf({
                id: einvoice.id,
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
                        this.refModalService.open(einvoice, EbViewPdfEInvocieComponent, res, false, einvoice.typeID, 'width-80 height-100');
                    }
                    // var blob = new Blob([byteArray], {type: 'application/pdf'});
                    // const data = new Blob(res.body, { type: 'application/pdf' });
                    /*const fileURL = URL.createObjectURL(res.body);
                window.open(fileURL);*/
                },
                (res: any) => this.onError(res.title)
            );
    }

    openRefVoucher(einvoice: IEInvoice) {
        window.open(`/#/xuat-hoa-don/${einvoice.id}/edit/from-ref`, '_blank');
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
            // this.newList = [];
            this.page = 1;
            this.previousPage = 1;
        }
        this.einvocieService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                searchVoucher: JSON.stringify(searchVC),
                typeEInvoice: DANH_SACH_HOA_DON
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
        if (this.toDateStr) {
            this.search.toDate = moment(this.toDateStr, DATE_FORMAT_SEARCH);
        }
    }

    changeToDate() {
        this.toDateStr = this.search.toDate.format('DD/MM/YYYY');
    }

    changeToDateStr() {
        if (this.fromDateStr) {
            this.search.fromDate = moment(this.fromDateStr, DATE_FORMAT_SEARCH);
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
            for (let j = 0; j < this.eInvoices.length; j++) {
                if (!this.newList.some(n => n.id === this.eInvoices[j].id)) {
                    this.newList.push(this.eInvoices[j]);
                }
            }
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

    getStatusInvoice(statusInvocie) {
        let result = '';
        switch (statusInvocie) {
            case -1:
                result = this.translate.instant('ebwebApp.eInvoice.Status.NoExist');
                break;
            case 0:
                result = this.translate.instant('ebwebApp.eInvoice.Status.New');
                break;
            case 1:
                result = this.translate.instant('ebwebApp.eInvoice.Status.Signed');
                break;
            case 2:
                result = this.translate.instant('ebwebApp.eInvoice.Status.ReportTax');
                break;
            case 3:
                result = this.translate.instant('ebwebApp.eInvoice.Status.Replaced');
                break;
            case 4:
                result = this.translate.instant('ebwebApp.eInvoice.Status.Adjusted');
                break;
            case 5:
                result = this.translate.instant('ebwebApp.eInvoice.Status.Destruction');
                break;
            case 6:
                result = this.translate.instant('ebwebApp.eInvoice.Status.Wait');
                break;
            case 7:
                result = this.translate.instant('ebwebApp.eInvoice.Status.ReplacedNew');
                break;
            case 8:
                result = this.translate.instant('ebwebApp.eInvoice.Status.AdjustNew');
                break;
        }
        return result;
    }

    publishInvocie() {
        if (this.newList.filter(n => n.checked).length > 0) {
            if (this.checkErrPL()) {
                if (this.useSignToken) {
                    this.loadPluginFor();
                } else {
                    this.einvocieService.publishInvoice(this.newList.map(n => n.id)).subscribe((res: HttpResponse<IResponeSds>) => {
                        if (res.body.status === 2) {
                            if (res.body.data.keyInvoiceMsgDTO && res.body.data.keyInvoiceMsgDTO.length > 0) {
                                this.toastr.error(
                                    res.body.data.keyInvoiceMsgDTO[0].message,
                                    this.translate.instant('ebwebApp.mCReceipt.home.message')
                                );
                            } else {
                                for (let i = 0; i < this.newList.length; i++) {
                                    const e = this.eInvoices.find(n => n.id === this.newList[i].id);
                                    if (e) {
                                        e.statusInvoice = 1;
                                        e.invoiceNo = this.padLeft(
                                            res.body.data.keyInvoiceNoDTO.find(n => n.id === this.newList[i].id).no,
                                            '0',
                                            7
                                        );
                                        if (e.iDReplaceInv) {
                                            const eReplace = this.eInvoices.find(n => n.id === e.iDReplaceInv);
                                            if (eReplace) {
                                                eReplace.statusInvoice = 3;
                                            }
                                        } else if (e.iDAdjustInv) {
                                            const eAdjust = this.eInvoices.find(n => n.id === e.iDAdjustInv);
                                            if (eAdjust) {
                                                eAdjust.statusInvoice = 4;
                                            }
                                        }
                                    }
                                }
                                this.toastr.success('Phát hành thành công', this.translate.instant('ebwebApp.mCReceipt.home.message'));
                            }
                            this.newList = [];
                            this.eInvoices.forEach(n => (n.checked = false));
                        } else {
                            this.toastr.error(res.body.message, this.translate.instant('ebwebApp.mCReceipt.home.message'));
                        }
                    });
                }
            }
        } else {
            this.toastr.warning('Chưa chọn hóa đơn', this.translate.instant('ebwebApp.mCReceipt.home.message'));
        }
    }

    createInvoiceWaitSign(content) {
        this.question = 'Doanh nghiệp sẽ chịu hoàn toàn trách nhiệm về hành động này ?';
        this.optionQuestion = 0;
        if (this.newList.filter(n => n.checked).length > 0) {
            if (this.checkErr()) {
                this.modalRef = this.modalService.open(content, { backdrop: 'static' });
            }
        } else {
            this.toastr.warning('Chưa chọn hóa đơn', this.translate.instant('ebwebApp.mCReceipt.error.error'));
        }
    }

    checkErrPL() {
        let result = false;
        if (this.newList.some(n => n.statusInvoice === 1)) {
            let mess = 'Hóa đơn số ';
            const lst = this.newList.filter(n => n.statusInvoice === 1);
            for (let i = 0; i < lst.length; i++) {
                mess += lst[i].invoiceNo;
                if (i < lst.length - 1) {
                    mess += ', ';
                }
            }
            mess += ' đã có chữ ký số';
            this.toastr.warning(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        } else if (this.newList.some(n => n.statusInvoice === 3)) {
            let mess = 'Hóa đơn số ';
            const lst = this.newList.filter(n => n.statusInvoice === 3);
            for (let i = 0; i < lst.length; i++) {
                mess += lst[i].invoiceNo;
                if (i < lst.length - 1) {
                    mess += ', ';
                }
            }
            mess += ' là hóa đơn bị thay thế';
            this.toastr.warning(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        } else if (this.newList.some(n => n.statusInvoice === 4)) {
            let mess = 'Hóa đơn số ';
            const lst = this.newList.filter(n => n.statusInvoice === 4);
            for (let i = 0; i < lst.length; i++) {
                mess += lst[i].invoiceNo;
                if (i < lst.length - 1) {
                    mess += ', ';
                }
            }
            mess += ' là hóa đơn bị điều chỉnh';
            this.toastr.warning(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        } else if (this.newList.some(n => n.statusInvoice === 5)) {
            let mess = 'Hóa đơn số ';
            const lst = this.newList.filter(n => n.statusInvoice === 5);
            for (let i = 0; i < lst.length; i++) {
                mess += lst[i].invoiceNo;
                if (i < lst.length - 1) {
                    mess += ', ';
                }
            }
            mess += ' đã bị hủy';
            this.toastr.warning(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        } else {
            result = true;
        }
        return result;
    }

    checkErr() {
        let result = false;
        if (this.newList.some(n => n.statusInvoice === 1)) {
            let mess = 'Hóa đơn số ';
            const lst = this.newList.filter(n => n.statusInvoice === 1);
            for (let i = 0; i < lst.length; i++) {
                mess += lst[i].invoiceNo;
                if (i < lst.length - 1) {
                    mess += ', ';
                }
            }
            mess += ' đã có chữ ký số';
            this.toastr.warning(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        } else if (this.newList.some(n => n.statusInvoice === 3)) {
            let mess = 'Hóa đơn số ';
            const lst = this.newList.filter(n => n.statusInvoice === 3);
            for (let i = 0; i < lst.length; i++) {
                mess += lst[i].invoiceNo;
                if (i < lst.length - 1) {
                    mess += ', ';
                }
            }
            mess += ' là hóa đơn bị thay thế';
            this.toastr.warning(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        } else if (this.newList.some(n => n.statusInvoice === 4)) {
            let mess = 'Hóa đơn số ';
            const lst = this.newList.filter(n => n.statusInvoice === 4);
            for (let i = 0; i < lst.length; i++) {
                mess += lst[i].invoiceNo;
                if (i < lst.length - 1) {
                    mess += ', ';
                }
            }
            mess += ' là hóa đơn bị điều chỉnh';
            this.toastr.warning(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        } else if (this.newList.some(n => n.statusInvoice === 7)) {
            let mess = 'Hóa đơn số ';
            const lst = this.newList.filter(n => n.statusInvoice === 7);
            for (let i = 0; i < lst.length; i++) {
                mess += lst[i].invoiceNo;
                if (i < lst.length - 1) {
                    mess += ', ';
                }
            }
            mess += ' là hóa đơn mới tạo lập(TT)';
            this.toastr.warning(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        } else if (this.newList.some(n => n.statusInvoice === 8)) {
            let mess = 'Hóa đơn số ';
            const lst = this.newList.filter(n => n.statusInvoice === 8);
            for (let i = 0; i < lst.length; i++) {
                mess += lst[i].invoiceNo;
                if (i < lst.length - 1) {
                    mess += ', ';
                }
            }
            mess += ' là hóa đơn mới tạo lập(ĐC)';
            this.toastr.warning(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        } else if (this.newList.some(n => n.statusInvoice === 5)) {
            let mess = 'Hóa đơn số ';
            const lst = this.newList.filter(n => n.statusInvoice === 5);
            for (let i = 0; i < lst.length; i++) {
                mess += lst[i].invoiceNo;
                if (i < lst.length - 1) {
                    mess += ', ';
                }
            }
            mess += ' đã bị hủy';
            this.toastr.warning(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        } else {
            result = true;
        }
        return result;
    }

    createIVWaitSign() {
        this.einvocieService.createInvoiceWaitSign(this.newList.map(n => n.id)).subscribe((res: HttpResponse<IResponeSds>) => {
            if (res.body.status === 2) {
                this.newList = [];
                this.eInvoices.forEach(n => (n.checked = false));
                this.loadAll();
                this.toastr.success('Tạo hóa đơn chờ thành công', this.translate.instant('ebwebApp.mCReceipt.home.message'));
            } else {
                if (res.body.data.keyInvoiceMsgDTO && res.body.data.keyInvoiceMsgDTO.length > 0) {
                    this.toastr.error(res.body.data.keyInvoiceMsgDTO[0].message, this.translate.instant('ebwebApp.mCReceipt.home.message'));
                } else {
                    this.toastr.error(res.body.message, this.translate.instant('ebwebApp.mCReceipt.home.message'));
                }
            }
        });
    }

    cancelInvoice(content) {
        this.question = 'Bạn muốn hủy hóa đơn này ?';
        this.optionQuestion = 1;
        if (this.NCCDV === NCCDV_EINVOICE.SIV) {
            if (this.newList.filter(n => n.checked).length > 1) {
                if (this.newList.length > 1) {
                    this.toastr.warning('Chỉ được chọn một hóa đơn để hủy', this.translate.instant('ebwebApp.mCReceipt.home.message'));
                    return false;
                }
            }
        }
        if (this.newList.filter(n => n.checked).length > 0) {
            if (this.newList.some(n => n.statusInvoice === 5)) {
                let mess = 'Hóa đơn số ';
                const lst = this.newList.filter(n => n.statusInvoice === 5);
                for (let i = 0; i < lst.length; i++) {
                    mess += lst[i].invoiceNo;
                    if (i < lst.length - 1) {
                        mess += ', ';
                    }
                }
                mess += ' đã bị hủy';
                this.toastr.warning(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
            } else if (this.newList.some(n => n.statusInvoice === 0)) {
                let mess = 'Hóa đơn số ';
                const lst = this.newList.filter(n => n.statusInvoice === 0);
                for (let i = 0; i < lst.length; i++) {
                    mess += lst[i].invoiceNo;
                    if (i < lst.length - 1) {
                        mess += ', ';
                    }
                }
                mess += ' mới tạo lập';
                this.toastr.warning(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
            } else if (this.newList.some(n => n.statusInvoice === 3)) {
                let mess = 'Hóa đơn số ';
                const lst = this.newList.filter(n => n.statusInvoice === 3);
                for (let i = 0; i < lst.length; i++) {
                    mess += lst[i].invoiceNo;
                    if (i < lst.length - 1) {
                        mess += ', ';
                    }
                }
                mess += ' ở trạng thái bị thay thế không được phép hủy';
                this.toastr.warning(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
            } else if (this.newList.some(n => n.statusInvoice === 4)) {
                let mess = 'Hóa đơn số ';
                const lst = this.newList.filter(n => n.statusInvoice === 4);
                for (let i = 0; i < lst.length; i++) {
                    mess += lst[i].invoiceNo;
                    if (i < lst.length - 1) {
                        mess += ', ';
                    }
                }
                mess += ' ở trạng thái bị điều chỉnh không được phép hủy';
                this.toastr.warning(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
            } else {
                this.modalRef = this.modalService.open(content, { backdrop: 'static' });
            }
        } else {
            this.toastr.warning('Chưa chọn hóa đơn', this.translate.instant('ebwebApp.mCReceipt.error.error'));
        }
    }

    cancelIV() {
        if (this.newList.filter(n => n.checked).length > 0) {
            switch (this.NCCDV) {
                case NCCDV_EINVOICE.SDS:
                    this.cancelInvoiceSDS();
                    break;
                case NCCDV_EINVOICE.SIV:
                    this.modalRef = this.modalService.open(this.modalCancelIVComponent, { backdrop: 'static' });
                    // this.cancelInvoiceSDS();
                    break;
                case NCCDV_EINVOICE.MIV:
                    this.modalRef = this.modalService.open(this.modalCancelIVComponent, { backdrop: 'static' });
                    break;
                case NCCDV_EINVOICE.VNPT:
                    break;
            }
        } else {
            this.toastr.warning('Chưa chọn hóa đơn', this.translate.instant('ebwebApp.mCReceipt.home.message'));
        }
    }

    cancelInvoiceSDS() {
        this.einvocieService
            .cancelInvoice({
                uuidList: this.newList.map(n => n.id),
                nameDocument: this.nameDocument,
                dateDocument: this.dateDocument ? this.dateDocument.format(DATE_FORMAT) : null,
                note: this.note
            })
            .subscribe((res: HttpResponse<IResponeSds>) => {
                if (res.body.status === EINVOICE_RESPONE.Success) {
                    if (res.body.data.keyInvoiceMsgDTO && res.body.data.keyInvoiceMsgDTO.length > 0) {
                        let mess = '';
                        for (let i = 0; i < res.body.data.keyInvoiceMsgDTO.length; i++) {
                            const e = this.newList.find(n => n.id === res.body.data.keyInvoiceMsgDTO[i].id);
                            if (e) {
                                mess += 'Số ' + e.invoiceNo + ': ' + res.body.data.keyInvoiceMsgDTO[i].message + '\n';
                            } else {
                                mess += res.body.data.keyInvoiceMsgDTO[i].id + ': ' + res.body.data.keyInvoiceMsgDTO[i].message + '\n';
                            }
                        }
                        this.toastr.error(mess, this.translate.instant('ebwebApp.mCReceipt.home.message'));
                        if (res.body.data.keyInvoiceNoDTO && res.body.data.keyInvoiceNoDTO.length > 0) {
                            let messSucess = 'Số hóa đơn ';
                            for (let i = 0; i < res.body.data.keyInvoiceNoDTO.length; i++) {
                                const e = this.eInvoices.find(n => n.id === res.body.data.keyInvoiceNoDTO[i].id);
                                const eS = this.newList.find(n => n.id === res.body.data.keyInvoiceNoDTO[i].id);
                                if (e) {
                                    e.statusInvoice = 5;
                                }
                                messSucess += eS.invoiceNo;
                                if (i < res.body.data.keyInvoiceNoDTO.length - 1) {
                                    messSucess += ', ';
                                }
                            }
                            messSucess += ' hủy thành công';
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
                                e.statusInvoice = 5;
                            }
                            mess += eS.invoiceNo;
                            if (i < res.body.data.keyInvoiceNoDTO.length - 1) {
                                mess += ', ';
                            }
                        }
                        mess += ' hủy thành công';
                        this.newList = [];
                        this.eInvoices.forEach(n => (n.checked = false));
                        this.toastr.success(mess, this.translate.instant('ebwebApp.mCReceipt.home.message'));
                    }
                } else {
                    this.toastr.error(res.body.message, this.translate.instant('ebwebApp.mCReceipt.home.message'));
                }
                this.nameDocument = null;
                this.dateDocument = null;
            });
    }

    padLeft(text: string, padChar: string, size: number): string {
        return (String(padChar).repeat(size) + text).substr(size * -1, size);
    }

    registerConnectEInvocie() {
        this.eventSubscriber = this.eventManager.subscribe('connectEInvocieSuccess', response => {
            this.principal.identity(true).then(account => {
                this.currentAccount = account;
                if (this.currentAccount) {
                    this.currencyCode = this.currentAccount.organizationUnit.currencyID;
                    this.isTichHop = this.currentAccount.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
                    this.useInvoiceWait = this.currentAccount.systemOption.some(x => x.code === CheckHDCD && x.data === '1');
                    this.useSignToken = this.currentAccount.systemOption.some(x => x.code === SignType && x.data === '1');
                }
            });
            this.loadAll();
        });
    }

    continute() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        switch (this.optionQuestion) {
            case 0: // tạo hóa đơn chờ
                this.createIVWaitSign();
                break;
            case 1: // hủy hóa đơn
                this.cancelIV();
                break;
        }
    }

    closeContent() {
        this.modalRef.close();
    }

    thayTheHoaDon() {
        if (this.checkErrorForReplace()) {
            this.xulyHoaDon(0);
        }
    }

    dieuChinhThongTin() {
        if (this.checkErrorForReplace()) {
            this.xulyHoaDon(4);
        }
    }

    dieuChinhGiam() {
        if (this.checkErrorForReplace()) {
            this.xulyHoaDon(3);
        }
    }

    dieuChinhTang() {
        if (this.checkErrorForReplace()) {
            this.xulyHoaDon(2);
        }
    }

    checkErrorForReplace() {
        if (this.newList.length === 0) {
            this.toastr.warning('Chưa chọn hóa đơn', this.translate.instant('ebwebApp.mCReceipt.home.message'));
            return false;
        } else if (this.newList.length > 1) {
            this.toastr.warning(
                'Chỉ được chọn một hóa đơn để điều chỉnh hoặc thay thế',
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
            return false;
        } else if (
            this.newList[0].statusInvoice === EINVOICE.HOA_DON_MOI_TAO_LAP ||
            this.newList[0].statusInvoice === EINVOICE.HOA_DON_MOI_TAO_LAP_THAY_THE ||
            this.newList[0].statusInvoice === EINVOICE.HOA_DON_MOI_TAO_LAP_DIEU_CHINH
        ) {
            this.toastr.warning(
                this.translate.instant('ebwebApp.eInvoice.Status.New'),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
            return false;
        } else if (this.newList[0].statusInvoice === EINVOICE.HOA_DON_BI_DIEU_CHINH) {
            this.toastr.warning(
                this.getStatusInvoice(EINVOICE.HOA_DON_BI_DIEU_CHINH),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
            return false;
        } else if (this.newList[0].statusInvoice === EINVOICE.HOA_DON_BI_THAY_THE) {
            this.toastr.warning(
                this.getStatusInvoice(EINVOICE.HOA_DON_BI_THAY_THE),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
            return false;
        }
        return true;
    }

    xulyHoaDon(type: number) {
        if (this.newList[0].id) {
            const url = `/#/xuat-hoa-don/${this.newList[0].id}/edit/invoice-processing/${type}`;
            window.open(url, '_blank');
        }
    }

    openPopupsendMail(senMail) {
        if (this.checkErrorSendMail()) {
            this.modalRef = this.modalService.open(senMail, { backdrop: 'static' });
        }
    }

    checkErrorSendMail() {
        if (this.newList.length === 0) {
            this.toastr.warning('Chưa chọn hóa đơn', this.translate.instant('ebwebApp.mCReceipt.home.message'));
            return false;
        }
        return true;
    }

    getDateSendMail(einvoice: IEInvoice) {
        if (einvoice) {
            return moment(einvoice.email.split(';')[einvoice.email.split(';').length - 1].split(',')[1], DATE_FORMAT).format('DD/MM/YYYY');
        } else {
            return '';
        }
    }

    continueSendMail() {
        const ikeyEmail: Map<string, string> = new Map<string, string>();
        this.newList.forEach(n => ikeyEmail.set(n.id, this.email));
        if (ikeyEmail) {
            const convMap = {};
            ikeyEmail.forEach((val: string, key: string) => {
                convMap[key] = val;
            });
            this.einvocieService.sendMail(convMap).subscribe((res: HttpResponse<IResponeSds>) => {
                if (res.body.status === 2) {
                    if (res.body.data.keyInvoiceMsgDTO.length > 0) {
                        let mess = 'Hóa đơn số ';
                        for (let i = 0; i < res.body.data.keyInvoiceMsgDTO.length; i++) {
                            const e = this.newList.find(n => n.id === res.body.data.keyInvoiceMsgDTO[i].id);
                            if (e) {
                                mess += e.invoiceNo + ': ' + res.body.data.keyInvoiceMsgDTO[i].message + '\n';
                            } else {
                                mess += res.body.data.keyInvoiceMsgDTO[i].id + ': ' + res.body.data.keyInvoiceMsgDTO[i].message + '\n';
                            }
                        }
                        this.toastr.error(mess, this.translate.instant('ebwebApp.mCReceipt.home.message'));
                        for (let i = 0; i < this.newList.length; i++) {
                            if (!res.body.data.keyInvoiceMsgDTO.some(n => n.id === this.newList[i].id)) {
                                const e = this.eInvoices.find(n => n.id === this.newList[i].id);
                                if (e) {
                                    e.statusSendMail = true;
                                    e.email = ikeyEmail.get(this.newList[i].id) + ',' + moment(new Date(), DATE_FORMAT).format(DATE_FORMAT);
                                }
                            }
                        }
                        this.newList = [];
                        this.eInvoices.forEach(n => (n.checked = false));
                    } else {
                        for (let i = 0; i < this.newList.length; i++) {
                            const e = this.eInvoices.find(n => n.id === this.newList[i].id);
                            if (e) {
                                e.statusSendMail = true;
                                e.email = ikeyEmail.get(this.newList[i].id) + ',' + moment(new Date(), DATE_FORMAT).format(DATE_FORMAT);
                            }
                        }
                        this.toastr.success('Gửi mail thành công', this.translate.instant('ebwebApp.mCReceipt.home.message'));
                        this.newList = [];
                        this.eInvoices.forEach(n => (n.checked = false));
                    }
                } else if (res.status === 1) {
                    this.toastr.warning(res.body.message, this.translate.instant('ebwebApp.mCReceipt.home.message'));
                } else {
                    this.toastr.error(res.body.message, this.translate.instant('ebwebApp.mCReceipt.home.message'));
                }
                this.modalRef.close();
            });
        }
    }

    changeInvoiceTemplate() {
        if (this.invoiceTemplate) {
            this.search.invoiceTemplate = this.invoiceTemplate.invoiceTemplate;
            this.search.invoiceSeries = this.invoiceTemplate.invoiceSeries;
        }
    }

    /*Chứng thư*/
    loadPluginFor = () => {
        // Load plugin của chrome
        // nếu client chưa cài đặt plugin
        // Thông báo lỗi và yêu cầu người dùng cài đặt plugin
        if (this.hasExtensionFor(this.digiDocChrome)) {
            this.plugin = new top.window[this.digiDocChrome]();
            this.selectCert();
        } else {
            this.opendPopupGuide();
        }
    };

    hasExtensionFor = cls => {
        if (typeof top.window[cls] === 'function') {
            return true;
        }
        return typeof window[cls] === 'function';
    };

    opendPopupGuide() {
        const modalRef = this.modalService.open(SignaturesPopupComponent);
        modalRef.result.then(result => {}, reason => {});
    }

    selectCert = () => {
        this.plugin.selectCertSerial(this.option).then(
            response => {
                this.certificate = response.value.cert;
                this.certificateSerial = response.value.serial;
                console.log(this.certificate);
                // Sau khi người dùng chọn chữ kí số, gửi lên server để đọc dữ liệu
                // hiển thị lên view
                if (this.certificate) {
                    if (this.NCCDV === NCCDV_EINVOICE.SDS) {
                        this.getDigestData(this.newList, this.certificate);
                    } else if (this.NCCDV === NCCDV_EINVOICE.SIV) {
                        this.getDigestData(this.newList, this.certificateSerial);
                    }
                }
            },
            error => {
                // Plugin không đọc được dữ liệu
                // thông báo lỗi
                this.code2err(this.plugin.errorCode);
            }
        );
    };

    getDigestData(newList, certificate) {
        const rq: IRequestSDS = {};
        rq.Ikeys = this.newList.map(n => n.id);
        rq.CertString = certificate;
        this.einvocieService.getDigestData(rq).subscribe((res: HttpResponse<IResponeSds>) => {
            if (res.body.status === EINVOICE_STATUS.SUCCESS) {
                if (res.body.data.digestDataDTO && res.body.data.digestDataDTO.length > 0) {
                    this.signHashData(res.body.data.digestDataDTO, certificate);
                }
            } else {
                this.onError(res.body.message);
            }
        });
    }

    async signHashData(digestDataDTO: IDigestDataDTO[], certificate) {
        // console.log('digestDataDTO : ' + JSON.stringify(digestDataDTO));
        if (this.certificate) {
            for (let i = 0; i < digestDataDTO.length; i++) {
                const promise = new Promise((resolve, reject) => {
                    const n = digestDataDTO[i];
                    this.plugin
                        .signHashData(
                            certificate,
                            {
                                type: 'xmlwithcert',
                                value: n.hashData
                            },
                            this.option
                        )
                        .then(
                            response => {
                                n.sigData = response.value.signature;
                                resolve(response.value.signature);
                            },
                            error => {
                                this.code2err(this.plugin.errorCode);
                            }
                        );
                });
                const result = await promise;
            }
            const rq: IRequestSDS = {};
            rq.Ikeys = this.newList.map(n => n.id);
            rq.Pattern = this.newList[0].invoiceTemplate;
            rq.Serial = this.newList[0].invoiceSeries;
            rq.signatureDTO = digestDataDTO;
            rq.CertString = certificate;
            this.publishWithToke(rq);
            // console.log('rq : ' + JSON.stringify(rq));
        } else {
            console.log(new Error(this.INVALID_ARGUMENT));
        }
    }

    publishWithToke(rq: IRequestSDS) {
        this.einvocieService.publishInvoiceWithCert(rq).subscribe((res: HttpResponse<IResponeSds>) => {
            if (res.body.status === 2) {
                for (let i = 0; i < this.newList.length; i++) {
                    const e = this.eInvoices.find(n => n.id === this.newList[i].id);
                    if (e) {
                        e.statusInvoice = 1;
                        e.invoiceNo = this.padLeft(res.body.data.keyInvoiceNoDTO.find(n => n.id === this.newList[i].id).no, '0', 7);
                        if (e.iDReplaceInv) {
                            const eReplace = this.eInvoices.find(n => n.id === e.iDReplaceInv);
                            if (eReplace) {
                                eReplace.statusInvoice = 3;
                            }
                        } else if (e.iDAdjustInv) {
                            const eAdjust = this.eInvoices.find(n => n.id === e.iDAdjustInv);
                            if (eAdjust) {
                                eAdjust.statusInvoice = 4;
                            }
                        }
                    }
                }
                this.toastr.success('Phát hành thành công', this.translate.instant('ebwebApp.mCReceipt.home.message'));
                this.newList = [];
                this.eInvoices.forEach(n => (n.checked = false));
            } else {
                this.toastr.error(res.body.message, this.translate.instant('ebwebApp.mCReceipt.home.message'));
            }
        });
    }

    code2str(err) {
        switch (parseInt(err, 10)) {
            case 1:
                return this.USER_CANCEL;
            case 2:
                return this.INVALID_ARGUMENT;
            case 17:
                return this.INVALID_ARGUMENT;
            case 19:
                return this.NOT_ALLOWED;
            default:
                return this.TECHNICAL_ERROR;
        }
    }

    code2err(err) {
        this.toastr.error(
            this.translate.instant('ebwebApp.eInvoice.cert.error.' + this.code2str(err)),
            this.translate.instant('ebwebApp.eInvoice.cert.error.title')
        );
    }

    /*Chứng thư*/

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

    checkErrorContinuteCancelInvoice() {
        if (!this.note && this.NCCDV === NCCDV_EINVOICE.MIV) {
            this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        if (!this.dateDocument || !this.nameDocument) {
            this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        } else {
            return true;
        }
    }

    continueCancel() {
        if (this.checkErrorContinuteCancelInvoice()) {
            this.modalRef.close();
            this.cancelInvoiceSDS();
        }
    }

    closeCancel() {
        this.modalRef.close();
        this.nameDocument = null;
        this.note = null;
        this.dateDocument = null;
    }

    loadDataMIV() {
        this.einvocieService.loadDataMIV({}).subscribe((res: HttpResponse<ResponeSds>) => {
            if (res.body.status === EINVOICE_RESPONE.Success) {
                this.toastr.success('Cập nhật dữ liệu thành công', this.translate.instant('ebwebApp.mCReceipt.error.error'));
                this.loadAll();
            } else {
                this.toastr.error(res.body.message, this.translate.instant('ebwebApp.mCReceipt.error.error'));
            }
        });
    }

    getVATRate(vat) {
        if (vat) {
            return this.listVAT.find(n => n.data === vat).name;
        }
    }
}
