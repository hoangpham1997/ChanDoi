import { Component, ElementRef, OnInit, Renderer, ViewChild } from '@angular/core';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { TranslateService } from '@ngx-translate/core';
import { IPPOrderDto } from 'app/shared/modal/pp-order/pp-order-dto.model';
import * as moment from 'moment';
import { LoginService, Principal, StateStorageService } from 'app/core';
import { DATE_FORMAT, DATE_FORMAT_SEARCH, ITEMS_PER_PAGE } from 'app/shared';
import { SaReturnModalService } from 'app/shared/modal/sa-return/sa-return-modal.service';
import { Moment } from 'moment';
import { IViewSAOrderDTO } from 'app/shared/model/view-sa-order.model';
import { Router } from '@angular/router';
import { TypeGroupService } from 'app/shared/modal/ref/type-group.service';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { ToastrService } from 'ngx-toastr';
import { SAOrderService } from 'app/ban-hang/don_dat_hang/sa-order';
import { HttpResponse } from '@angular/common/http';
import { IPPInvoiceDTO } from 'app/shared/modal/pp-invoice/pp-invoice-dto.model';
import { PPInvoiceService } from 'app/muahang/mua_hang_qua_kho';

@Component({
    selector: 'eb-pp-invoice-modal',
    templateUrl: './pp-invoice-modal.component.html',
    styleUrls: ['./pp-invoice-modal.component.css']
})
export class PpInvoiceModalComponent implements OnInit {
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
    newList: IPPInvoiceDTO[];
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    accountingObjects: any[];
    accountingObject: any;
    listTimeLine: any[];
    typeID;
    timeLineVoucher: any;
    currentAccount: any;
    PPInvoiceDTOs: IPPInvoiceDTO[];
    fromDateStr: string;
    toDateStr: string;
    validateToDate: boolean;
    validateFromDate: boolean;
    currencyID: any;
    accountingObjectId: string;

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
        private ppInvoiceService: PPInvoiceService,
        private toastr: ToastrService,
        public translate: TranslateService
    ) {
        this.typeSearch = 1;
        this.PPInvoiceDTOs = [];
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
        this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjects = res.body
                .filter(x => x.objectType === 0 || x.objectType === 2)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
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
            this.ppInvoiceService
                .getViewPPInvoiceDTO({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    accountingObjectID: this.accountingObject ? this.accountingObject.id : '',
                    fromDate: fromDateP ? fromDateP : '',
                    toDate: toDateP ? toDateP : '',
                    currencyID: this.currencyID ? this.currencyID : ''
                })
                .subscribe(res => {
                    this.PPInvoiceDTOs = res.body;
                    if (this.newList) {
                        this.PPInvoiceDTOs.forEach(item => {
                            item.checked = this.newList.some(data => data.pPInvoiceDetailID === item.pPInvoiceDetailID);
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
        this.eventManager.broadcast({
            name: 'selectPPInvoice',
            content: this.newList
        });
        this.activeModal.dismiss(true);
    }

    close() {
        this.activeModal.dismiss(false);
        this.eventManager.broadcast({
            name: 'closeSelectPPInvoice'
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
        return this.PPInvoiceDTOs.every(item => item.checked) && this.PPInvoiceDTOs.length;
    }

    checkAll() {
        const isCheck = this.PPInvoiceDTOs.every(item => item.checked) && this.PPInvoiceDTOs.length;
        this.PPInvoiceDTOs.forEach(item => (item.checked = !isCheck));

        if (!isCheck) {
            for (let j = 0; j < this.PPInvoiceDTOs.length; j++) {
                let isPush = true;
                for (let i = 0; i < this.newList.length; i++) {
                    if (this.newList[i].pPInvoiceDetailID === this.PPInvoiceDTOs[j].pPInvoiceDetailID) {
                        isPush = true;
                    }
                }
                if (isPush) {
                    this.newList.push(this.PPInvoiceDTOs[j]);
                }
            }
        } else {
            for (let j = 0; j < this.PPInvoiceDTOs.length; j++) {
                for (let i = 0; i < this.newList.length; i++) {
                    if (this.newList[i].pPInvoiceDetailID === this.PPInvoiceDTOs[j].pPInvoiceDetailID) {
                        this.newList.splice(i, 1);
                        i--;
                    }
                }
            }
        }
    }

    check(ppInvoiceDTO: IPPInvoiceDTO) {
        ppInvoiceDTO.checked = !ppInvoiceDTO.checked;
        if (ppInvoiceDTO.checked) {
            this.newList.push(ppInvoiceDTO);
        } else {
            for (let i = 0; i < this.newList.length; i++) {
                if (this.newList[i].pPInvoiceDetailID === ppInvoiceDTO.pPInvoiceDetailID) {
                    this.newList.splice(i, 1);
                    i--;
                }
            }
        }
    }
}
