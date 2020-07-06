import { Component, ElementRef, OnInit, Renderer } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { LoginService } from 'app/core/login/login.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import * as moment from 'moment';
import { Moment } from 'moment';
import { TypeGroupService } from 'app/shared/modal/ref/type-group.service';
import { DATE_FORMAT, DATE_FORMAT_SEARCH, ITEMS_PER_PAGE } from 'app/shared/index';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { HttpResponse } from '@angular/common/http';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { TypeID } from 'app/app.constants';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Principal } from 'app/core';
import { IViewSAQuoteDTO } from 'app/shared/model/view-sa-quote.model';
import { SAQuoteService } from 'app/ban-hang/bao_gia/sa-quote';

@Component({
    selector: 'eb-sa-invoice-modal',
    templateUrl: './sa-quote.component.html',
    styleUrls: ['./sa-quote.component.css']
})
export class EbSaQuoteModalComponent implements OnInit {
    data: any[];
    recorded: any;
    status: any;
    fromDate: Moment;
    toDate: Moment;
    typeGroups: any;
    typeGroup: any;
    typeSearch: number;
    no: any;
    invoiceNo: any;
    totalItems: number;
    page: number;
    itemsPerPage = ITEMS_PER_PAGE;
    links: any;
    queryCount: any;
    newList: IViewSAQuoteDTO[];
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    accountingObjects: any[];
    accountingObject: any;
    listTimeLine: any[];
    typeID;
    timeLineVoucher: any;
    currentAccount: any;
    ViewSAQuoteDTOs: IViewSAQuoteDTO[];
    fromDateStr: string;
    toDateStr: string;
    validateToDate: boolean;
    validateFromDate: boolean;
    currencyID: any;
    accountingObjectId: any;

    constructor(
        private eventManager: JhiEventManager,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private router: Router,
        private activeModal: NgbActiveModal,
        private typeGroupService: TypeGroupService,
        private parseLinks: JhiParseLinks,
        public utilsService: UtilsService,
        private accountingObjectService: AccountingObjectService,
        private toastrService: ToastrService,
        private translateService: TranslateService,
        private principal: Principal,
        private sAQuoteService: SAQuoteService,
        private toastr: ToastrService,
        public translate: TranslateService
    ) {
        this.typeSearch = 1;
        this.ViewSAQuoteDTOs = [];
        this.listTimeLine = this.utilsService.getCbbTimeLine();
        this.timeLineVoucher = 4;
        this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
    }

    ngOnInit(): void {
        this.typeGroupService.query().subscribe(res => {
            this.typeGroups = res.body;
        });
        this.page = 1;
        this.newList = [];
        if (this.data) {
            this.newList.push(...this.data);
        }
        this.getAccountingObject();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
    }

    getAccountingObject() {
        this.accountingObjectService.getAllDTO({ isObjectType12: true }).subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjects = res.body.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            this.accountingObject = this.accountingObjects.find(x => x.id === this.accountingObjectId);
            this.search();
        });
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        // voucher?hóa đơn : chứng từ tham chiếu
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDate = moment(this.objTimeLine.dtBeginDate);
            this.toDate = moment(this.objTimeLine.dtEndDate);
        }
    }

    search() {
        if (this.checkErr()) {
            const fromDateP = this.fromDate instanceof moment ? this.fromDate.format(DATE_FORMAT) : this.fromDate;
            const toDateP = this.toDate instanceof moment ? this.toDate.format(DATE_FORMAT) : this.toDate;
            this.sAQuoteService
                .getViewSAQuoteDTO({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    accountingObjectID: this.accountingObject ? this.accountingObject.id : '',
                    fromDate: fromDateP ? fromDateP : '',
                    toDate: toDateP ? toDateP : '',
                    currencyID: this.currencyID ? this.currencyID : ''
                })
                .subscribe(res => {
                    this.ViewSAQuoteDTOs = res.body;
                    if (this.newList) {
                        this.ViewSAQuoteDTOs.forEach(item => {
                            item.checked = this.newList.some(data => data.sAQuoteDetailID === item.sAQuoteDetailID);
                        });
                    }
                    this.links = this.parseLinks.parse(res.headers.get('link'));
                    this.totalItems = parseInt(res.headers.get('X-Total-Count'), 10);
                    this.queryCount = this.totalItems;
                });
        }
    }

    checkErr() {
        if (!this.fromDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullFromDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullToDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (this.toDate && this.fromDate) {
            if (moment(this.toDate, DATE_FORMAT_SEARCH) < moment(this.fromDate, DATE_FORMAT_SEARCH)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.fromDateGreaterToDate'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        return true;
    }

    apply() {
        // const viewVouchersSelected = this.viewVouchers.filter(item => item.checked);
        this.eventManager.broadcast({
            name: 'selectViewSAQuote',
            content: this.newList
        });
        this.activeModal.dismiss(true);
    }

    close() {
        this.activeModal.dismiss(false);
        this.eventManager.broadcast({
            name: 'closeSelectViewSAQuote'
        });
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
        return this.ViewSAQuoteDTOs.every(item => item.checked) && this.ViewSAQuoteDTOs.length;
    }

    checkAll() {
        const isCheck = this.ViewSAQuoteDTOs.every(item => item.checked) && this.ViewSAQuoteDTOs.length;
        this.ViewSAQuoteDTOs.forEach(item => (item.checked = !isCheck));

        if (!isCheck) {
            for (let j = 0; j < this.ViewSAQuoteDTOs.length; j++) {
                let isPush = true;
                for (let i = 0; i < this.newList.length; i++) {
                    if (this.newList[i].sAQuoteDetailID === this.ViewSAQuoteDTOs[j].sAQuoteDetailID) {
                        isPush = false;
                    }
                }
                if (isPush) {
                    this.newList.push(this.ViewSAQuoteDTOs[j]);
                }
            }
        } else {
            for (let j = 0; j < this.ViewSAQuoteDTOs.length; j++) {
                for (let i = 0; i < this.newList.length; i++) {
                    if (this.newList[i].sAQuoteDetailID === this.ViewSAQuoteDTOs[j].sAQuoteDetailID) {
                        this.newList.splice(i, 1);
                        i--;
                    }
                }
            }
        }
    }

    check(viewSAQuote: IViewSAQuoteDTO) {
        viewSAQuote.checked = !viewSAQuote.checked;
        if (viewSAQuote.checked) {
            this.newList.push(viewSAQuote);
        } else {
            for (let i = 0; i < this.newList.length; i++) {
                if (this.newList[i].sAQuoteDetailID === viewSAQuote.sAQuoteDetailID) {
                    this.newList.splice(i, 1);
                    i--;
                }
            }
        }
    }

    changeQuantity(index) {}
}
