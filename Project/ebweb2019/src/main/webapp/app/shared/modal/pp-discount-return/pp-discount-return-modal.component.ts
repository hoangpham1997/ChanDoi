import { Component, OnInit, ViewChild } from '@angular/core';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AccountingObjectDTO, IAccountingObject } from 'app/shared/model/accounting-object.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { TranslateService } from '@ngx-translate/core';
import { IPPOrderDto } from 'app/shared/modal/pp-order/pp-order-dto.model';
import * as moment from 'moment';
import { Principal } from 'app/core';
import { ITEMS_PER_PAGE } from 'app/shared';
import { SaReturnModalService } from 'app/shared/modal/sa-return/sa-return-modal.service';
import { ToastrService } from 'ngx-toastr';
import { PPDiscountReturnService } from 'app/muahang/hang_mua_tra_lai_giam_gia/pp-discount-return';
import { PpDiscountReturnModalService } from 'app/shared/modal/pp-discount-return/pp-discount-return-modal.service';

@Component({
    selector: 'eb-pp-order-modal',
    templateUrl: './pp-discount-return-modal.component.html',
    styleUrls: ['./pp-discount-return-modal.component.css']
})
export class PpDiscountReturnModalComponent implements OnInit {
    @ViewChild('fromDateTemp') fromDateTemp: any;
    @ViewChild('toDateTemp') toDateTemp: any;
    accountObjects: IAccountingObject[];
    account: any;
    ppDiscountReturn: any[];
    toDate: any;
    toDateByPeriod: string;
    fromDate: any;
    fromDateByPeriod: string;
    recorded: any;
    searchValue: any;
    accountingObjectID: any;
    period: any;
    periods: any[];
    itemsSelected: any[];
    modalData: any;
    typeMG: number;
    totalItems: any;
    links: any;
    queryCount: any;
    itemsPerPage = ITEMS_PER_PAGE;
    page: any;
    newList: any[];
    listTimeLine: any[];
    timeLineVoucher: any;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };

    constructor(
        private eventManager: JhiEventManager,
        private activeModal: NgbActiveModal,
        public utilsService: UtilsService,
        public translate: TranslateService,
        private toastr: ToastrService,
        private principal: Principal,
        private ppDiscountReturnService: PpDiscountReturnModalService,
        private parseLinks: JhiParseLinks
    ) {}

    ngOnInit(): void {
        this.listTimeLine = this.utilsService.getCbbTimeLine();
        this.timeLineVoucher = this.listTimeLine[4].value;
        this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);

        this.page = 1;
        this.getPeriods();
        this.newList = [];
        this.principal.identity().then(account => {
            this.account = account;
            if (this.modalData) {
                this.accountObjects = this.modalData.accountObjects;
                this.accountingObjectID = this.modalData.accountingObject || null;
                this.itemsSelected = this.modalData.itemsSelected; //
                this.typeMG = this.modalData.typeMG ? this.modalData.typeMG : null; // material Goods Type: null : All
            }
            this.queryData();
        });
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDate = moment(this.objTimeLine.dtBeginDate);
            this.toDate = moment(this.objTimeLine.dtEndDate);
        }
    }

    queryData() {
        const fromDateP = this.fromDate instanceof moment ? this.fromDate.format('YYYYMMDD') : this.fromDate;
        const toDateP = this.toDate instanceof moment ? this.toDate.format('YYYYMMDD') : this.toDate;
        if (!fromDateP) {
            this.toastr.error(
                this.translate.instant('ebwebApp.muaHang.muaDichVu.toastr.fromDateNull'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return;
        }
        if (!toDateP) {
            this.toastr.error(
                this.translate.instant('ebwebApp.muaHang.muaDichVu.toastr.toDateNull'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return;
        }
        if (this.fromDate > this.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.fromDateGreaterToDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return;
        }
        this.ppDiscountReturnService
            .find({
                page: this.page - 1,
                size: this.itemsPerPage,
                accountingObject: this.accountingObjectID && this.accountingObjectID.id ? this.accountingObjectID.id : '',
                fromDate: fromDateP ? fromDateP : '',
                toDate: toDateP ? toDateP : ''
            })
            .subscribe(res => {
                this.ppDiscountReturn = res.body.content;
                this.ppDiscountReturn.forEach(item => {
                    if (this.itemsSelected.some(x => x.ppDiscountReturnID === item.id)) {
                        item.checked = true;
                        this.newList.push(item);
                    }
                });
                this.ppDiscountReturn.sort((a, b) => a.orderPriority - b.orderPriority);
                this.links = this.parseLinks.parse(res.headers.get('link'));
                this.totalItems = parseInt(res.headers.get('X-Total-Count'), 10);
                this.queryCount = this.totalItems;
            });
    }

    close() {
        this.activeModal.dismiss(false);
        this.eventManager.broadcast({
            name: 'closedPPDisCountReturn'
        });
    }

    apply() {
        this.eventManager.broadcast({
            name: 'selectedPPDisCountReturn',
            content: this.newList.filter(item => item.checked)
        });
        this.activeModal.dismiss(true);
    }

    check(orderDto) {
        orderDto.checked = !orderDto.checked;
        if (orderDto.checked) {
            this.newList.push(orderDto);
        } else {
            this.newList = this.newList.filter(x => x.id !== orderDto.id);
        }
    }

    isCheckAll() {
        if (this.ppDiscountReturn) {
            for (let i = 0; i < this.ppDiscountReturn.length; i++) {
                if (!this.ppDiscountReturn[i].checked) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    checkAll() {
        if (this.ppDiscountReturn) {
            const isCheckAll = this.isCheckAll();
            for (let i = 0; i < this.ppDiscountReturn.length; i++) {
                this.ppDiscountReturn[i].checked = !isCheckAll;
                if (!isCheckAll && !this.newList.some(x => x.id === this.ppDiscountReturn[i].id)) {
                    this.newList.push(this.ppDiscountReturn[i]);
                }
            }
        }
    }

    getPeriods() {
        let result = [
            {
                name: '',
                value: -3
            },
            {
                name: this.translate.instant('ebwebApp.comboBox.periods.currentYear'),
                value: -2
            },
            {
                name: this.translate.instant('ebwebApp.comboBox.periods.currentMonth'),
                value: -1
            }
        ];
        const currentMonth = new Date();
        for (let i = 1; i < currentMonth.getMonth() + 2; i++) {
            result = [
                ...result,
                {
                    name: this.translate.instant(`ebwebApp.comboBox.periods.${i}`),
                    value: i
                }
            ];
        }
        this.periods = result;
        this.period = -1;
        this.onSelectPeriod();
    }

    onSelectPeriod() {
        const newDate = new Date();
        const currentDate = newDate.getDate();
        const _currentMonth = newDate.getMonth() + 1;
        const currentMonth = _currentMonth < 10 ? `0${_currentMonth}` : _currentMonth;
        const currentFullYear = newDate.getFullYear();
        const currentFullDate = `${currentFullYear}${currentMonth}${currentDate}`;
        if (this.period) {
            const thisMonth = this.period < 10 ? `0${this.period}` : this.period;
            switch (this.period) {
                case -2:
                    this.fromDateByPeriod = `${currentFullYear}0101`;
                    this.toDateByPeriod = currentFullDate;
                    break;
                case -1:
                    this.fromDateByPeriod = `${currentFullYear}${currentMonth}01`;
                    this.toDateByPeriod = currentFullDate;
                    break;
                default:
                    this.fromDateByPeriod = `${currentFullYear}${thisMonth}01`;
                    this.toDateByPeriod = `${currentFullYear}${thisMonth}${this.getDaysInMonth(this.period, currentFullYear)}`;
                    break;
            }
        }
        this.fromDate = this.fromDateByPeriod;
        this.toDate = this.toDateByPeriod;
    }

    getDaysInMonth(m: number, y: number): number {
        switch (m) {
            case 2:
                return (y % 4 === 0 && y % 100) || y % 400 === 0 ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
    }

    getCurrentDate(): string {
        const _date = moment();
        return `${_date.year()}-${_date.month() + 1}-${_date.date()}`;
    }

    getFromToStr(date?: string, isMaxDate?: boolean): { year; month; day } {
        const _date = date && moment(date, 'YYYYMMDD').isValid() ? moment(date, 'YYYYMMDD') : isMaxDate ? null : moment();
        return _date ? { year: _date.year(), month: _date.month() + 1, day: _date.date() } : null;
    }

    onChangeFromDate() {
        const toDateYear = parseInt(this.toDate.substr(0, 4), 0);
        const toDateMonth = parseInt(this.toDate.substr(4, 2), 0);
        const lastDateOfMonth = this.getDaysInMonth(toDateMonth, toDateYear) + '';
        if (this.fromDate.substr(6, 2) !== '01' || this.toDate.substr(6, 2) !== lastDateOfMonth) {
            this.period = -3;
        }
    }
}
