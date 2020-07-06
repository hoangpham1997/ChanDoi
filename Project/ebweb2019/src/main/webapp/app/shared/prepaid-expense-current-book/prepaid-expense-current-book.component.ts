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
import { PrepaidExpense } from 'app/shared/model/prepaid-expense.model';
import { PrepaidExpenseService } from 'app/entities/prepaid-expense';

@Component({
    selector: 'eb-ref-modal',
    templateUrl: './prepaid-expense-current-book.component.html',
    styles: ['div#table-scroll-ref {' + '    z-index: 0;' + '}'],
    styleUrls: ['./prepaid-expense-current-book.component.css']
})
export class EbPrepaidExpenseCurrentBookModalComponent implements OnInit {
    data: any;
    recorded: any;
    fromDate;
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
    accountingObject: any;
    page: number;
    itemsPerPage: any;
    links: any;
    totalItems: number;
    pageCount: number;
    previousPage: any;
    newList: any;
    account: any;
    status: boolean;
    typeGroupID: any;
    routeData: any;

    constructor(
        private eventManager: JhiEventManager,
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
        private prepaidExpenseService: PrepaidExpenseService
    ) {
        this.typeSearch = 1;
        this.viewVouchers = [];
        this.listTimeLine = this.utilsService.getCbbTimeLine();
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            // this.reverse = data.pagingParams.ascending;
            // this.predicate = data.pagingParams.predicate;
        });
    }

    ngOnInit(): void {
        this.timeLineVoucher = this.listTimeLine[4].value;
        this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
        this.newList = {};
        this.itemsPerPage = ITEMS_PER_PAGE;
        if (this.data) {
            this.newList = Object.assign({}, this.data);
        }
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
        this.search();
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        // voucher?hóa đơn : chứng từ tham chiếu
        if (intTimeLine) {
            if (intTimeLine) {
                this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
                this.fromDate = moment(this.objTimeLine.dtBeginDate).format('YYYYMMDD');
                this.toDate = moment(this.objTimeLine.dtEndDate).format('YYYYMMDD');
            }
        }
        this.search();
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
            name: 'selectPrepaidExpenseCurrentBook',
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

    searchViewVoucher() {
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
        this.prepaidExpenseService
            .getPrepaidExpenseByCurrentBookToModal({
                page: this.page - 1,
                size: this.itemsPerPage,
                fromDate: this.fromDate,
                toDate: this.toDate,
                typeExpense: this.data.typeExpense
            })
            .subscribe(ref => {
                this.links = this.parseLinks.parse(ref.headers.get('link'));
                this.totalItems = parseInt(ref.headers.get('X-Total-Count'), 10);
                this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
                this.previousPage = this.page;
                this.viewVouchers = ref.body;
                if (this.newList) {
                    if (this.viewVouchers && this.viewVouchers.length > 0) {
                        this.viewVouchers.forEach(item => {
                            if (item.prepaidExpenseCode === this.newList.prepaidExpenseCode) {
                                item.checked = true;
                            }
                        });
                    }
                }
            });
    }

    loadPage(page: number) {
        // if (page !== this.previousPage) {
        this.page = page;
        this.searchViewVoucher();
        // }
    }

    selectedItemPerPage(itemsPerPage: any) {
        if (itemsPerPage !== this.page) {
            this.page = itemsPerPage;
            this.searchViewVoucher();
        }
    }
    search() {
        this.page = 1;
        this.searchViewVoucher();
    }

    // checkAll() {
    //     const isCheck = this.viewVouchers.every(item => item.checked) && this.viewVouchers.length;
    //     this.viewVouchers.forEach(item => (item.checked = !isCheck));
    //
    //     if (!isCheck) {
    //         for (let j = 0; j < this.viewVouchers.length; j++) {
    //             let isPush = true;
    //             for (let i = 0; i < this.newList.length; i++) {
    //                 if (this.newList[i].refID1 === this.viewVouchers[j].refID1) {
    //                     isPush = true;
    //                 }
    //             }
    //             if (isPush) {
    //                 this.newList.push(this.viewVouchers[j]);
    //             }
    //         }
    //     } else {
    //         for (let j = 0; j < this.viewVouchers.length; j++) {
    //             for (let i = 0; i < this.newList.length; i++) {
    //                 if (this.newList[i].refID1 === this.viewVouchers[j].refID1) {
    //                     this.newList.splice(i, 1);
    //                     i--;
    //                 }
    //             }
    //         }
    //     }
    // }

    check(viewVoucher) {
        viewVoucher.checked = !viewVoucher.checked;
        if (viewVoucher.checked) {
            this.newList = viewVoucher;
        }
        for (let j = 0; j < this.viewVouchers.length; j++) {
            if (this.viewVouchers[j].prepaidExpenseCode !== viewVoucher.prepaidExpenseCode) {
                this.viewVouchers[j].checked = false;
            }
        }
    }
}
