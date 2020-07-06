import { Component, ElementRef, OnInit, Renderer } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { LoginService } from 'app/core/login/login.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import * as moment from 'moment';
import { Moment } from 'moment';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { TypeGroupService } from 'app/shared/modal/ref/type-group.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { HttpResponse } from '@angular/common/http';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { PPInvoiceDetailsService } from 'app/entities/nhan-hoa-don/pp-invoice-details.service';
import { GlobalConfig, IndividualConfig, ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Principal } from 'app/core';
import { utf8Encode } from '@angular/compiler/src/util';
import { GROUP_TYPEID } from 'app/app.constants';

@Component({
    selector: 'eb-ref-modal',
    templateUrl: './discount-return.component.html',
    styles: ['div#table-scroll-ref {' + '    z-index: 0;' + '}']
})
export class EbDiscountReturnModalComponent implements OnInit {
    data: any[];
    currencyCode: any;
    recorded: any;
    accountingObjectID: any;
    fromDate;
    typeID: any;
    any;
    toDate: any;
    typeGroups: any;
    typeGroup: any;
    typeSearch: number;
    viewVouchers: any[];
    no: any;
    invoiceNo: any;
    listTimeLine: any;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    timeLineVoucher: any;
    accountingObjects: IAccountingObject[];
    accountingObject: any;
    page: number;
    itemsPerPage: any;
    links: any;
    totalItems: number;
    pageCount: number;
    previousPage: any;
    newList: any[];
    account: any;
    status: boolean;
    listID: any[];

    constructor(
        private eventManager: JhiEventManager,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private toastr: ToastrService,
        public translate: TranslateService,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private router: Router,
        private ppInvoiceDetailsService: PPInvoiceDetailsService,
        private activeModal: NgbActiveModal,
        private accountingObjectService: AccountingObjectService,
        public utilsService: UtilsService,
        private viewVoucherService: ViewVoucherService,
        private typeGroupService: TypeGroupService
    ) {
        this.typeSearch = 1;
        this.viewVouchers = [];
        this.listTimeLine = this.utilsService.getCbbTimeLine();
    }

    ngOnInit(): void {
        this.timeLineVoucher = this.listTimeLine[4].value;
        this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
        this.newList = [];
        this.itemsPerPage = ITEMS_PER_PAGE;

        if (this.data) {
            this.newList = [];
            this.newList.push(...this.data);
        }
        this.typeGroupService.query().subscribe(res => {
            this.typeGroups = res.body;
        });
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
        this.getAccountingObject();
        this.search();
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        // voucher? hóa đơn : chứng từ tham chiếu
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDate = moment(this.objTimeLine.dtBeginDate).format('YYYYMMDD');
            this.toDate = moment(this.objTimeLine.dtEndDate).format('YYYYMMDD');
        }
    }

    getAccountingObject() {
        if (this.typeID === GROUP_TYPEID.GROUP_PPDISCOUNTPURCHASE || this.typeID === GROUP_TYPEID.GROUP_PPDISCOUNTRETURN) {
            this.accountingObjectService.getAccountingObjectActive().subscribe((res: HttpResponse<IAccountingObject[]>) => {
                this.accountingObjects = res.body;
            });
        } else {
            this.accountingObjectService.getAccountingObjectActive().subscribe((res: HttpResponse<IAccountingObject[]>) => {
                this.accountingObjects = res.body;
            });
        }
    }

    apply() {
        // for (let i = 0; i < this.newList.length; i++) {
        //     if (this.newList[i].quantity < this.newList[i].quantityRollBack) {
        //         this.toastr.error(
        //             this.translate.instant('ebwebApp.pPDiscountReturnDetails.error.minusError'),
        //             this.translate.instant('ebwebApp.mCReceipt.home.message')
        //         );
        //     }
        // }
        this.eventManager.broadcast({
            name: 'selectDiscountReturn',
            content: this.newList
        });
        this.activeModal.dismiss(true);
    }

    close() {
        this.activeModal.dismiss(false);
    }

    getCurrentDate() {
        const _date = moment();
        return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
    }

    getFromToMoment(date?: Moment, isMaxDate?: boolean) {
        const _date = date && moment(date).isValid() ? date : isMaxDate ? null : moment();
        return _date ? { year: _date.year(), month: _date.month() + 1, day: _date.date() } : null;
    }

    isCheckAll() {
        return this.viewVouchers.every(item => item.checked) && this.viewVouchers.length;
    }

    // checkAll() {
    //     const isCheck = this.viewVouchers.every(item => item.checked) && this.viewVouchers.length;
    //     this.viewVouchers.forEach(item => (item.checked = !isCheck));
    // }

    // check(viewVoucher) {
    //     viewVoucher.checked = !viewVoucher.checked;
    // }
    searchPPinVoid() {
        if (!this.fromDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.muaHang.muaDichVu.toastr.fromDateNull'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return;
        }
        if (!this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.muaHang.muaDichVu.toastr.toDateNull'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return;
        }
        if (this.fromDate > this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCAudit.errorDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return;
        }
        this.listID = [];
        for (let i = 0; i < this.newList.length; i++) {
            if (this.newList[i].ppInvoiceDetailID) {
                this.listID.push(this.newList[i].ppInvoiceDetailID);
            }
        }
        this.ppInvoiceDetailsService
            .getPPInvoiceDetailsGetLicense({
                page: this.page - 1,
                size: this.itemsPerPage,
                accountingObjectID: this.accountingObject ? this.accountingObject : '',
                formDate: this.fromDate ? this.fromDate : '',
                toDate: this.toDate ? this.toDate.toString() : '',
                currencyCode: this.currencyCode ? this.currencyCode : '',
                listID: this.listID && this.listID.length > 0 ? this.listID : ''
            })
            .subscribe(ref => {
                this.links = this.parseLinks.parse(ref.headers.get('link'));
                this.totalItems = parseInt(ref.headers.get('X-Total-Count'), 10);
                this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
                this.previousPage = this.page;
                this.viewVouchers = ref.body;
                // this.viewVouchers.forEach(item => {
                //     item.quantityRollBack = item.quantity;
                // });
                if (this.newList) {
                    this.viewVouchers.forEach(item => {
                        const newListItem = this.newList.find(data => data.ppInvoiceDetailID === item.ppInvoiceDetailID);
                        if (newListItem) {
                            item.checked = true;
                            if (!newListItem.quantity) {
                                item.quantityRollBack = item.quantity;
                            } else {
                                item.quantityRollBack = newListItem.quantity;
                            }
                        } else {
                            item.quantityRollBack = item.quantity;
                            item.checked = false;
                        }
                    });
                }
                // if (this.data) {
                //     this.viewVouchers.forEach(item => {
                //         item.checked = this.data.some(data => data.ppInvoiceDetailID === item.ppInvoiceDetailID);
                //     });
                // }
            });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.page = page;
            this.searchPPinVoid();
        }
    }

    selectedItemPerPage() {
        this.page = 1;
        this.searchPPinVoid();
    }
    search() {
        this.page = 1;
        this.checkPostedDateGreaterDate();
        this.searchPPinVoid();
    }

    checkAll() {
        const isCheck = this.viewVouchers.every(item => item.checked) && this.viewVouchers.length;
        this.viewVouchers.forEach(item => (item.checked = !isCheck));

        if (!isCheck) {
            for (let j = 0; j < this.viewVouchers.length; j++) {
                let isPush = true;
                for (let i = 0; i < this.newList.length; i++) {
                    if (this.newList[i].ppInvoiceDetailID === this.viewVouchers[j].ppInvoiceDetailID) {
                        isPush = true;
                    }
                }
                if (isPush) {
                    this.newList.push(this.viewVouchers[j]);
                }
            }
        } else {
            for (let j = 0; j < this.viewVouchers.length; j++) {
                for (let i = 0; i < this.newList.length; i++) {
                    if (this.newList[i].ppInvoiceDetailID === this.viewVouchers[j].ppInvoiceDetailID) {
                        this.newList.splice(i, 1);
                        i--;
                    }
                }
            }
        }
    }

    check(viewVoucher) {
        viewVoucher.checked = !viewVoucher.checked;
        if (viewVoucher.checked) {
            this.newList.push(viewVoucher);
        } else {
            for (let i = 0; i < this.newList.length; i++) {
                if (this.newList[i].ppInvoiceDetailID === viewVoucher.ppInvoiceDetailID) {
                    this.newList.splice(i, 1);
                    i--;
                }
            }
        }
    }

    changeQuantityRollBack(viewVoucher: any) {
        if (viewVoucher.quantity < viewVoucher.quantityRollBack && viewVoucher.checked) {
            this.toastr.warning(
                this.translate.instant('ebwebApp.pPDiscountReturnDetails.error.minusError'),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
            return;
        }
        for (let i = 0; i < this.newList.length; i++) {
            if (this.newList[i].ppInvoiceDetailID === viewVoucher.ppInvoiceDetailID) {
                this.newList.splice(i, 1);
                this.newList.push(viewVoucher);
                break;
            }
        }
    }

    checkPostedDateGreaterDate() {
        if (this.toDate && this.fromDate) {
            if (moment(this.toDate) < moment(this.fromDate)) {
                this.toastr.error(this.translate.instant('ebwebApp.mBDeposit.fromDateMustBeLessThanToDate'));
                return false;
            } else {
                return true;
            }
        }
        return true;
    }
}
