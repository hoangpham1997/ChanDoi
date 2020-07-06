import { Component, OnInit } from '@angular/core';
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
import {
    CheckHDCD,
    DANH_SACH_HOA_DON_CHO_KY,
    EINVOICE_STATUS,
    HINH_THUC_HOA_DON_HOA_DON_DIEN_TU,
    SignType,
    TCKHAC_SDTichHopHDDT
} from 'app/app.constants';
import { IaPublishInvoiceDetails } from 'app/shared/model/ia-publish-invoice-details.model';
import { SaBillService } from 'app/ban-hang/xuat-hoa-don/sa-bill.service';
import { SignaturesPopupComponent } from 'app/hoa-don-dien-tu/danh_sach_hoa_don/signatures-popup.component';
import { IRequestSDS } from 'app/shared/model/hoa-don-dien-tu/request-sds';
import { IDigestDataDTO } from 'app/shared/model/hoa-don-dien-tu/respone-sds-data';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ROLE } from 'app/role.constants';
import { IaPublishInvoiceDetailsService } from 'app/ban-hang/xuat-hoa-don/ia-publish-invoice-details.service';

@Component({
    selector: 'eb-hoa-don-cho-ky',
    templateUrl: './hoa-don-cho-ky.html',
    styleUrls: ['./hoa-don-cho-ky.css']
})
export class HoaDonChoKyComponent implements OnInit {
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
    useSignToken: boolean;

    /*Chứng thư*/
    certificate: any;
    digiDocChrome = 'TokenSigning';
    plugin: any;
    option = { lang: 'en' };
    USER_CANCEL = 'userCancel';
    INVALID_ARGUMENT = 'invalidArgument';
    TECHNICAL_ERROR = 'technicalError';
    NOT_ALLOWED = 'notAllowed';

    /*Chứng thư*/

    /*Phân quyền*/
    ROLE_PhatHanh = ROLE.DSHD_CHO_KY_PHAT_HANH;
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
        private saBillService: SaBillService,
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
                this.listStatus = [
                    { value: 0, name: res['ebwebApp.eInvoice.Status.New'] },
                    { value: 1, name: res['ebwebApp.eInvoice.Status.Signed'] },
                    { value: 3, name: res['ebwebApp.eInvoice.Status.Replaced'] },
                    { value: 4, name: res['ebwebApp.eInvoice.Status.Adjusted'] },
                    { value: 5, name: res['ebwebApp.eInvoice.Status.Destruction'] },
                    { value: 7, name: res['ebwebApp.eInvoice.Status.ReplacedNew'] },
                    { value: 8, name: res['ebwebApp.eInvoice.Status.AdjustNew'] }
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
                // this.newList = [];
                this.page = 1;
                this.previousPage = 1;
            }
            this.einvocieService
                .getEInvoiceWaitSign({
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
        this.router.navigate(['/hoa-don-cho-ky'], {
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
                this.useSignToken = this.currentAccount.systemOption.some(x => x.code === SignType && x.data === '1');
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

    delete() {
        this.router.navigate(['/', { outlets: { popup: 'sa-order/' + this.selectedRow.id + '/delete' } }]);
    }

    // region Tìm kiếm hóa đơn
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
                typeEInvoice: DANH_SACH_HOA_DON_CHO_KY
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

    publishInvocie() {
        if (this.newList.filter(n => n.checked).length > 0) {
            if (this.useSignToken) {
                this.loadPluginFor();
            } else {
                this.einvocieService.publishInvoice(this.newList.map(n => n.id)).subscribe((res: HttpResponse<IResponeSds>) => {
                    if (res.body.status === 2) {
                        this.newList = [];
                        this.eInvoices.forEach(n => (n.checked = false));
                        this.toastr.success('Phát hành thành công', this.translate.instant('ebwebApp.mCReceipt.home.message'));
                        this.loadAll();
                    } else {
                        this.toastr.error(res.body.message, this.translate.instant('ebwebApp.mCReceipt.home.message'));
                    }
                });
            }
        } else {
            this.toastr.warning('Chưa chọn hóa đơn', this.translate.instant('ebwebApp.mCReceipt.home.message'));
        }
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
                    this.useSignToken = this.currentAccount.systemOption.some(x => x.code === SignType && x.data === '1');
                }
            });
            this.loadAll();
        });
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
                console.log(this.certificate);
                // Sau khi người dùng chọn chữ kí số, gửi lên server để đọc dữ liệu
                // hiển thị lên view
                if (this.certificate) {
                    this.getDigestData(this.newList, this.certificate);
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
        console.log('digestDataDTO : ' + JSON.stringify(digestDataDTO));
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
            console.log('rq : ' + JSON.stringify(rq));
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
                this.loadAll();
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

    getVATRate(vat) {
        if (vat) {
            return this.listVAT.find(n => n.data === vat).name;
        }
    }
}
