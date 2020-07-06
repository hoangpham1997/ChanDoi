import { Component, ElementRef, OnInit, Renderer, ViewChild } from '@angular/core';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { TranslateService } from '@ngx-translate/core';
import { IPPOrderDto } from 'app/shared/modal/pp-order/pp-order-dto.model';
import * as moment from 'moment';
import { LoginService, Principal, StateStorageService } from 'app/core';
import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { SaReturnModalService } from 'app/shared/modal/sa-return/sa-return-modal.service';
import { Moment } from 'moment';
import { IViewSAOrderDTO } from 'app/shared/model/view-sa-order.model';
import { Router } from '@angular/router';
import { TypeGroupService } from 'app/shared/modal/ref/type-group.service';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { ToastrService } from 'ngx-toastr';
import { SAOrderService } from 'app/ban-hang/don_dat_hang/sa-order';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'eb-em-contract-modal',
    templateUrl: './em-contract-modal.component.html',
    styleUrls: ['./em-contract-modal.component.css']
})
export class EmContractModalComponent implements OnInit {
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
    newList: IViewSAOrderDTO[];
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    accountingObjects: any[];
    accountingObject: any;
    listTimeLine: any[];
    typeID;
    timeLineVoucher: any;
    currentAccount: any;
    ViewSAOrderDTOs: IViewSAOrderDTO[];
    fromDateStr: string;
    toDateStr: string;
    validateToDate: boolean;
    validateFromDate: boolean;

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
        private sAOrderService: SAOrderService,
        private toastr: ToastrService,
        public translate: TranslateService
    ) {
        this.typeSearch = 1;
        this.ViewSAOrderDTOs = [];
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
        this.search();
    }

    getAccountingObject() {
        this.accountingObjectService.getAllDTO({ isObjectType12: true }).subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjects = res.body.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
        });
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        // voucher?hóa đơn : chứng từ tham chiếu
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDate = moment(this.objTimeLine.dtBeginDate);
            this.toDate =
                moment(this.getCurrentDate()) <= moment(this.objTimeLine.dtEndDate)
                    ? moment(this.getCurrentDate())
                    : moment(this.objTimeLine.dtEndDate);
            this.changeToDate();
            this.changeFromDate();
        }
    }

    search() {
        if (this.checkErr()) {
            this.sAOrderService
                .getViewSAOrderDTO({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    accountingObjectID: this.accountingObject ? this.accountingObject.id : '',
                    fromDate: this.fromDate != null && this.fromDate.isValid() ? this.fromDate.format(DATE_FORMAT) : '',
                    toDate: this.toDate != null && this.toDate.isValid() ? this.toDate.format(DATE_FORMAT) : ''
                })
                .subscribe(res => {
                    this.ViewSAOrderDTOs = res.body;
                    this.ViewSAOrderDTOs.forEach(item => {
                        item.quantityOut = item.quantity - item.quantityDelivery;
                    });
                    if (this.newList) {
                        this.ViewSAOrderDTOs.forEach(item => {
                            item.checked = this.newList.some(data => data.sAOrderDetailID === item.sAOrderDetailID);
                            if (item.checked) {
                                item.quantityOut = this.newList.find(data => data.sAOrderDetailID === item.sAOrderDetailID).quantityOut;
                            }
                        });
                    }
                    this.links = this.parseLinks.parse(res.headers.get('link'));
                    this.totalItems = parseInt(res.headers.get('X-Total-Count'), 10);
                    this.queryCount = this.totalItems;
                });
        }
    }

    checkErr() {
        if (this.toDate && this.fromDate) {
            if (this.toDate.format(DATE_FORMAT) < this.fromDate.format(DATE_FORMAT)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.fromDateGreaterToDate'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        return true;
    }

    checkVal(item: IViewSAOrderDTO) {
        if (item.quantity - item.quantityDelivery < item.quantityOut) {
            this.toastr.error(this.translateService.instant('ebwebApp.sAInvoice.error.quantityOut'));
        }
    }

    apply() {
        const err = this.newList.filter(item => item.quantity - item.quantityDelivery < item.quantityOut);
        if (err && err.length) {
            this.toastr.error(this.translateService.instant('ebwebApp.sAInvoice.error.quantityOut'));
            return;
        }
        this.eventManager.broadcast({
            name: 'selectEMContract',
            content: this.newList
        });
        this.activeModal.dismiss(true);
    }

    close() {
        this.activeModal.dismiss(false);
        this.eventManager.broadcast({
            name: 'closeSelectEMContract'
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
        return this.ViewSAOrderDTOs.every(item => item.checked) && this.ViewSAOrderDTOs.length;
    }

    checkAll() {
        const isCheck = this.ViewSAOrderDTOs.every(item => item.checked) && this.ViewSAOrderDTOs.length;
        this.ViewSAOrderDTOs.forEach(item => (item.checked = !isCheck));

        if (!isCheck) {
            for (let j = 0; j < this.ViewSAOrderDTOs.length; j++) {
                let isPush = true;
                for (let i = 0; i < this.newList.length; i++) {
                    if (this.newList[i].sAOrderDetailID === this.ViewSAOrderDTOs[j].sAOrderDetailID) {
                        isPush = true;
                    }
                }
                if (isPush) {
                    this.newList.push(this.ViewSAOrderDTOs[j]);
                }
            }
        } else {
            for (let j = 0; j < this.ViewSAOrderDTOs.length; j++) {
                for (let i = 0; i < this.newList.length; i++) {
                    if (this.newList[i].sAOrderDetailID === this.ViewSAOrderDTOs[j].sAOrderDetailID) {
                        this.newList.splice(i, 1);
                        i--;
                    }
                }
            }
        }
    }

    check(viewSAOrder: IViewSAOrderDTO) {
        viewSAOrder.checked = !viewSAOrder.checked;
        if (viewSAOrder.checked) {
            this.newList.push(viewSAOrder);
        } else {
            for (let i = 0; i < this.newList.length; i++) {
                if (this.newList[i].sAOrderDetailID === viewSAOrder.sAOrderDetailID) {
                    this.newList.splice(i, 1);
                    i--;
                }
            }
        }
    }

    changeFromDate() {
        this.fromDateStr = this.fromDate.format('DD/MM/YYYY');
    }

    changeFromDateStr() {
        try {
            if (this.fromDateStr.length === 8) {
                const td = this.fromDateStr.replace(/^(.{2})/, '$1/').replace(/^(.{5})/, '$1/');
                if (!moment(td, 'DD/MM/YYYY').isValid()) {
                    this.validateFromDate = true;
                    this.fromDate = null;
                } else {
                    this.validateFromDate = false;
                    this.fromDate = moment(td, 'DD/MM/YYYY');
                }
            } else {
                this.fromDate = null;
                this.validateFromDate = false;
            }
        } catch (e) {
            this.fromDate = null;
            this.validateFromDate = false;
        }
    }

    changeToDate() {
        this.toDateStr = this.toDate.format('DD/MM/YYYY');
    }

    changeToDateStr() {
        try {
            if (this.toDateStr.length === 8) {
                const td = this.toDateStr.replace(/^(.{2})/, '$1/').replace(/^(.{5})/, '$1/');
                if (!moment(td, 'DD/MM/YYYY').isValid()) {
                    this.validateToDate = true;
                    this.toDate = null;
                } else {
                    this.validateToDate = false;
                    this.toDate = moment(td, 'DD/MM/YYYY');
                }
            } else {
                this.toDate = null;
                this.validateToDate = false;
            }
        } catch (e) {
            this.toDate = null;
            this.validateToDate = false;
        }
    }

    changeQuantity(index) {}
}
