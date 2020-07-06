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
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Principal } from 'app/core';
import { SAQuoteService } from 'app/ban-hang/bao_gia/sa-quote';
import { IViewSAOrderDTO } from 'app/shared/model/view-sa-order.model';
import { SAOrderService } from 'app/ban-hang/don_dat_hang/sa-order';
import { RepositoryLedgerService } from 'app/entities/repository-ledger';
import { SO_LAM_VIEC } from 'app/app.constants';

@Component({
    selector: 'eb-sa-invoice-modal',
    templateUrl: './select-material-goods.component.html',
    styleUrls: ['./select-material-goods.component.css']
})
export class EbSelectMaterialGoodsModalComponent implements OnInit {
    data: any[];
    recorded: any;
    status: any;
    fromDate: Moment;
    toDate: Moment;
    totalItems: number;
    page: number;
    itemsPerPage = ITEMS_PER_PAGE;
    links: any;
    queryCount: any;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    accountingObjects: any[];
    accountingObject: any;
    listTimeLine: any[];
    typeID;
    timeLineVoucher: any;
    currentAccount: any;
    listVouchers: any[];
    currencyID: any;
    objectId: any;
    modalData: any;
    selected: any;
    accountingObjectId: any;
    sessionWork: boolean;

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
        private repositoryLedgerService: RepositoryLedgerService,
        private toastr: ToastrService,
        public translate: TranslateService
    ) {
        this.listVouchers = [];
        this.listTimeLine = this.utilsService.getCbbTimeLine();
        this.timeLineVoucher = 4;
        this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
    }

    ngOnInit(): void {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.sessionWork = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '1');
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
            this.repositoryLedgerService
                .getIWVoucher({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    fromDate: fromDateP ? fromDateP : '',
                    toDate: toDateP ? toDateP : '',
                    objectId: this.objectId ? this.objectId : ''
                })
                .subscribe(res => {
                    this.listVouchers = res.body.filter(x => x.quantity > 0);
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

    selectedVoucher(voucher) {
        this.selected = voucher;
    }

    apply() {
        this.eventManager.broadcast({
            name: 'selectIWVoucher',
            content: this.selected
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
}
