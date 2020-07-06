import { Component, OnInit, ViewChild } from '@angular/core';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { TranslateService } from '@ngx-translate/core';
import * as moment from 'moment';
import { Principal } from 'app/core';
import { CostVouchersModalService } from 'app/shared/modal/cost-vouchers/cost-vouchers-modal.service';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { CostAllocationModalComponent } from 'app/shared/modal/cost-allocation/cost-allocation-modal.component';
import { ICostAllocation } from 'app/shared/modal/cost-allocation/cost-allocation.model';
import { ICostVouchersDTO } from 'app/shared/modal/cost-vouchers/cost-vouchers-dto.model';
import { SO_LAM_VIEC } from 'app/app.constants';
import { IPPInvoice } from 'app/shared/model/pp-invoice.model';
import { ITEMS_PER_PAGE } from 'app/shared';
import { IPPInvoiceDetailCost } from 'app/shared/model/pp-invoice-detail-cost.model';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'eb-cost-vouchers-modal',
    templateUrl: './cost-vouchers-modal.component.html',
    styleUrls: ['./cost-vouchers-modal.component.css']
})
export class CostVouchersModalComponent implements OnInit {
    @ViewChild('fromDateTemp') fromDateTemp: any;
    @ViewChild('toDateTemp') toDateTemp: any;
    accountObjects: IAccountingObject[];
    account: any;
    toDateByPeriod: string;
    fromDateByPeriod: string;
    recorded: any;
    searchValue: any;
    accountingObject: any;
    period: any;
    itemsSelected: any[];
    modalData: any;
    modalRef: NgbModalRef;
    costAllocations: ICostAllocation[];
    currentBook: string;
    totalItems: number;
    page: number;
    itemsPerPage = ITEMS_PER_PAGE;
    links: any;
    queryCount: any;
    periods: any[];
    costVouchers: ICostVouchersDTO[];
    newList: ICostVouchersDTO[];
    ppInvoiceId: string;
    ppInvoiceDetailCost: IPPInvoiceDetailCost[]; // list chứng từ chi phí
    isHaiQuan: boolean;
    pPInvoice: IPPInvoice;
    sumTotalAmount: number;
    sumAmount: number;
    sumFreightAmount: number;
    toDate;
    fromDate;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    listTimeLine: any[];
    timeLineVoucher: any;

    constructor(
        private eventManager: JhiEventManager,
        private activeModal: NgbActiveModal,
        public utilsService: UtilsService,
        private translate: TranslateService,
        private principal: Principal,
        private toastService: ToastrService,
        private translateService: TranslateService,
        private refModalService: NgbModal,
        private accountingObjectService: AccountingObjectService,
        private costVouchersModalService: CostVouchersModalService,
        private parseLinks: JhiParseLinks
    ) {
        this.costVouchers = [];
        this.newList = [];
        this.account = { organizationUnit: {} };
        this.listTimeLine = this.utilsService.getCbbTimeLine();
        this.timeLineVoucher = this.listTimeLine[4].value;
        this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
    }

    ngOnInit(): void {
        this.page = 1;
        this.principal.identity().then(account => {
            this.account = account;
            this.currentBook = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
            if (this.modalData) {
                this.isHaiQuan = this.modalData.isHaiQuan;
            }

            this.getAccountingObject();
        });
    }

    changeSum() {
        if (this.costVouchers && this.costVouchers.length) {
            this.sumTotalAmount = 0;
            this.sumFreightAmount = 0;
            this.sumAmount = 0;
            this.costVouchers.forEach(item => {
                this.sumTotalAmount += item.totalAmount ? item.totalAmount : 0;
                this.sumFreightAmount += item.freightAmount ? item.freightAmount : 0;
                this.sumAmount += item.amount ? item.amount : 0;
            });
        } else {
            this.sumTotalAmount = 0;
            this.sumFreightAmount = 0;
            this.sumAmount = 0;
        }
    }

    // đối tượng
    getAccountingObject() {
        this.accountingObjectService.getAccountingObjectsForProvider().subscribe(
            (res: HttpResponse<IAccountingObject[]>) => {
                this.accountObjects = res.body;
                if (this.pPInvoice && this.pPInvoice.accountingObjectId) {
                    this.accountingObject = this.accountObjects.find(n => n.id === this.pPInvoice.accountingObjectId);
                }
                this.queryData();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    queryData() {
        const fromDateP = this.fromDate instanceof moment ? this.fromDate.format('YYYYMMDD') : this.fromDate;
        const toDateP = this.toDate instanceof moment ? this.toDate.format('YYYYMMDD') : this.toDate;
        this.costVouchersModalService
            .findCostVouchers({
                ppInvoiceId: this.ppInvoiceId ? this.ppInvoiceId : '',
                isHaiQuan: this.isHaiQuan,
                accountingObject: this.accountingObject ? this.accountingObject.id : '',
                fromDate: fromDateP ? fromDateP : '',
                toDate: toDateP ? toDateP : '',
                page: this.page - 1,
                size: this.itemsPerPage
            })
            .subscribe(res => {
                this.paginate(res.body, res.headers);
            });
    }

    private paginate(data: ICostVouchersDTO[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.costVouchers = data;
        if (this.costVouchers) {
            this.costVouchers.forEach(item => {
                item.freightAmount =
                    (item.totalAmount ? item.totalAmount : 0) -
                    (item.totalDiscountAmount ? item.totalDiscountAmount : 0) -
                    // (item.totalVATAmount ? item.totalVATAmount : 0) -
                    (item.sumAmount ? item.sumAmount : 0) - // sumAmount: tổng giá trị đã phân bổ ở các lần trước
                    (this.ppInvoiceDetailCost.some(_item => _item.ppServiceID === item.id && _item.costType !== this.isHaiQuan)
                        ? this.ppInvoiceDetailCost.find(_item => _item.ppServiceID === item.id && _item.costType !== this.isHaiQuan).amount
                        : 0);

                item.amount = item.freightAmount;

                item.accumulatedAllocateAmount =
                    (item.sumAmount ? item.sumAmount : 0) +
                    (this.ppInvoiceDetailCost.some(_item => _item.ppServiceID === item.id && _item.costType !== this.isHaiQuan)
                        ? this.ppInvoiceDetailCost.find(_item => _item.ppServiceID === item.id && _item.costType !== this.isHaiQuan).amount
                        : 0);

                item.accumulatedAllocateAmountOriginal =
                    ((item.sumAmount ? item.sumAmount : 0) +
                        (this.ppInvoiceDetailCost.some(_item => _item.ppServiceID === item.id && _item.costType !== this.isHaiQuan)
                            ? this.ppInvoiceDetailCost.find(_item => _item.ppServiceID === item.id && _item.costType !== this.isHaiQuan)
                                  .amount
                            : 0)) /
                    item.exchangeRate;
                item.totalAmount = this.utilsService.round(item.totalAmount, this.account.systemOption, 7);
                item.amount = this.utilsService.round(item.amount, this.account.systemOption, 7);
                item.freightAmount = this.utilsService.round(item.freightAmount, this.account.systemOption, 7);
            });
        }

        // lấy lại dữ liệu khi update
        if (this.costVouchers && this.costVouchers.length) {
            this.costVouchers = this.costVouchers.filter(item => item.amount > 0);
            this.costVouchers.forEach(item => {
                item.checked = this.ppInvoiceDetailCost.some(_item => _item.ppServiceID === item.id && _item.costType === this.isHaiQuan);
                if (item.checked) {
                    item.freightAmount = this.ppInvoiceDetailCost.find(
                        _item => _item.ppServiceID === item.id && _item.costType === this.isHaiQuan
                    ).amount;
                }
                this.changeSum();
            });
        }
        this.changeSum();
    }

    close() {
        this.activeModal.dismiss(false);
        const modalRef = this.refModalService.open(CostAllocationModalComponent, {
            size: 'lg',
            windowClass: 'modal-xl1',
            backdrop: 'static'
        });
        modalRef.componentInstance.costAllocations = this.costAllocations;
        modalRef.componentInstance.ppInvoiceId = this.ppInvoiceId;
        modalRef.componentInstance.isHaiQuan = this.isHaiQuan;
        modalRef.componentInstance.ppInvoiceDetailCost = this.ppInvoiceDetailCost;
        modalRef.componentInstance.pPInvoice = this.pPInvoice;
        modalRef.result.then(result => {}, reason => {});
    }

    apply() {
        this.newList = this.costVouchers.filter(item => item.checked);

        if (!this.newList || this.newList.length === 0) {
            return;
        }

        for (const item of this.newList) {
            if (item.freightAmount > item.amount) {
                this.toastService.error(this.translateService.instant('ebwebApp.pPInvoice.error.allocationError'));
                return;
            }
        }
        this.activeModal.dismiss(false);
        const modalRef = this.refModalService.open(CostAllocationModalComponent, {
            size: 'lg',
            windowClass: 'modal-xl1',
            backdrop: 'static'
        });
        modalRef.componentInstance.costAllocations = this.costAllocations;
        modalRef.componentInstance.ppInvoiceId = this.ppInvoiceId;
        modalRef.componentInstance.isHaiQuan = this.isHaiQuan;
        modalRef.componentInstance.ppInvoiceDetailCost = this.ppInvoiceDetailCost;
        modalRef.componentInstance.pPInvoice = this.pPInvoice;
        modalRef.componentInstance.costVouchers = this.newList;
        modalRef.result.then(result => {}, reason => {});
    }

    check(costVoucher: ICostVouchersDTO) {
        costVoucher.checked = !costVoucher.checked;
    }

    isCheckAll() {
        if (this.costVouchers && this.costVouchers.length > 0) {
            for (let i = 0; i < this.costVouchers.length; i++) {
                if (!this.costVouchers[i].checked) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    checkAll() {
        if (this.costVouchers) {
            const isCheckAll = this.isCheckAll();
            for (let i = 0; i < this.costVouchers.length; i++) {
                this.costVouchers[i].checked = !isCheckAll;
                // if (!this.newList.some(x => x.id === this.costVouchers[i].id)) {
                //     this.newList.push(this.costVouchers[i]);
                // }
            }
        }
    }

    private onError(message: string) {}

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDate = moment(this.objTimeLine.dtBeginDate);
            this.toDate = moment(this.objTimeLine.dtEndDate);
        }
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
}
