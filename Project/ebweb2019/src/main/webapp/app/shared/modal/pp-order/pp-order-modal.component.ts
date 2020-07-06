import { Component, OnInit, ViewChild } from '@angular/core';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { TranslateService } from '@ngx-translate/core';
import { PPOrderModalService } from 'app/shared/modal/pp-order/pp-order-modal.service';
import { IPPOrderDto } from 'app/shared/modal/pp-order/pp-order-dto.model';
import * as moment from 'moment';
import { Principal } from 'app/core';
import { ITEMS_PER_PAGE } from 'app/shared';
import { Moment } from 'moment';
import { ToastrService } from 'ngx-toastr';
import { GROUP_TYPEID } from 'app/app.constants';

/**
 * @author phuonghv.
 * @description
 *
 * @requires accountObjects.
 * @requires itemsSelected. Khi load detail từ db về thì tạo ra thêm 1 property quantityFromDB = quantity
 * @example
 * @link TabInsertUpdateReceiptMuaDichVuComponent.beforeChange with case: 'muaDichVu-tab-pp-order'.
 * this.modalRef = // NgbModalRef
 *  this.refModalService.open(null, PpOrderModalComponent, { // app/core/login/ref-modal.service
 *              accountObjects: this.accountObjects, //List AccountingObject with ObjectType = 0 or 2 and IsActive = 1
 *               itemsSelected: this.ppService.ppServiceDetailDTOS.filter(x => x.ppOrderDetailId), // List details have ppOrderDetailId
 *              typeMG: 2 // Material Goods Type, if null load All,
 *              currency: // currencyId, if null load All
 *          });
 * @return
 *  {@link TabInsertUpdateReceiptMuaDichVuComponent.registerRef eventSubscriber: 'ONBROADCASTEVENT.selectedPPOrder'}.
 */
@Component({
    selector: 'eb-pp-order-modal',
    templateUrl: './pp-order-modal.component.html',
    styleUrls: ['./pp-order-modal.component.css']
})
export class PpOrderModalComponent implements OnInit {
    @ViewChild('fromDateTemp') fromDateTemp: any;
    @ViewChild('toDateTemp') toDateTemp: any;
    accountObjects: IAccountingObject[];
    account: any;
    ppOrders: any[];
    toDate;
    fromDate;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
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
    accountingObject: any;
    page: any;
    newList: IPPOrderDto[];
    listTimeLine: any[];
    timeLineVoucher: any;
    itemUnSelected: any[];
    currency: string;
    constructor(
        private eventManager: JhiEventManager,
        private activeModal: NgbActiveModal,
        public utilsService: UtilsService,
        public translate: TranslateService,
        private toastr: ToastrService,
        private principal: Principal,
        private ppOrderModalService: PPOrderModalService,
        private parseLinks: JhiParseLinks
    ) {
        this.listTimeLine = this.utilsService.getCbbTimeLine();
        this.timeLineVoucher = this.listTimeLine[4].value;
        this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
    }

    ngOnInit(): void {
        this.itemUnSelected = this.ppOrderModalService.getItemHadUnSelected();
        this.page = 1;
        this.newList = [];
        this.principal.identity().then(account => {
            this.account = account;
            if (this.modalData) {
                this.accountObjects = this.modalData.accountObjects;
                this.accountingObject = this.modalData.accountObject;
                this.itemsSelected = this.modalData.itemsSelected; //
                this.typeMG = this.modalData.typeMG ? this.modalData.typeMG : null; // material Goods Type: null : All
                if (this.modalData.itemUnSelected) {
                    this.itemUnSelected.push(...this.modalData.itemUnSelected);
                }
                this.currency = this.modalData.currency ? this.modalData.currency : '';
            }
            this.search();
        });
    }
    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDate = moment(this.objTimeLine.dtBeginDate);
            this.toDate = moment(this.objTimeLine.dtEndDate);
        }
    }

    search() {
        const fromDateP = this.fromDate instanceof moment ? this.fromDate.format('YYYYMMDD') : this.fromDate;
        const toDateP = this.toDate instanceof moment ? this.toDate.format('YYYYMMDD') : this.toDate;
        if (!fromDateP || fromDateP === '') {
            this.toastr.error(
                this.translate.instant('ebwebApp.muaHang.muaDichVu.toastr.fromDateNull'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return;
        }
        if (!toDateP || toDateP === '') {
            this.toastr.error(
                this.translate.instant('ebwebApp.muaHang.muaDichVu.toastr.toDateNull'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return;
        }
        if (parseInt(fromDateP, 0) > parseInt(toDateP, 0)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.fromDateGreaterToDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return;
        }
        this.ppOrderModalService
            .find({
                page: this.page - 1,
                size: this.itemsPerPage,
                accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
                fromDate: fromDateP ? fromDateP : '',
                toDate: toDateP ? toDateP : '',
                typeId: this.typeMG ? this.typeMG : '',
                itemsSelected: this.itemsSelected ? this.itemsSelected.map(x => x.ppOrderDetailId) : '',
                currency: this.currency ? this.currency : ''
            })
            .subscribe(res => {
                this.ppOrders = res.body.content;
                if (this.ppOrders.length === 0) {
                    this.toastr.warning(
                        this.translate.instant('ebwebApp.pPInvoice.notFoundRecord'),
                        this.translate.instant('ebwebApp.pPInvoice.message')
                    );
                }
                this.ppOrders.forEach(item => {
                    // todo Set lại  this.itemUnSelected đang bị trùng nhau nhiều lần
                    if (this.itemUnSelected && this.itemUnSelected.some(x => x.id === item.id)) {
                        item.quantityReceipt += this.itemUnSelected.find(x => x.id === item.id).receivedQuantity;
                    }
                    item.receivedQuantity = item.quantityReceipt;
                    if (this.itemsSelected.some(x => x.ppOrderDetailId === item.id)) {
                        item.checked = true;
                        const itemSelect = this.itemsSelected.find(x => x.ppOrderDetailId === item.id);
                        item.receivedQuantity = itemSelect.quantity;

                        if (itemSelect.id && itemSelect.quantityFromDB) {
                            item.quantityReceipt += itemSelect.quantityFromDB;
                        }
                        if (!this.newList.map(x => x.id).includes(item.id)) {
                            this.newList.push(item);
                        }
                    }
                    if (this.newList.some(x => x.id === item.id)) {
                        item.checked = true;
                        item.receivedQuantity = this.newList.find(x => x.id === item.id).receivedQuantity;
                    }
                });
                this.links = this.parseLinks.parse(res.headers.get('link'));
                this.totalItems = parseInt(res.headers.get('X-Total-Count'), 10);
                this.queryCount = this.totalItems;
            });
    }

    close() {
        this.activeModal.dismiss(false);
        this.eventManager.broadcast({
            name: 'closePPOrder'
        });
    }

    apply() {
        for (let i = 0; i < this.newList.length; i++) {
            if (!this.utilsService.changeReceivedQuantity(this.newList[i].receivedQuantity, this.newList[i].quantityReceipt)) {
                return;
            }
        }

        this.eventManager.broadcast({
            name: 'selectedPPOrder',
            content: this.newList.filter(item => item.checked).sort((a, b) => a.priority - b.priority)
        });
        this.activeModal.dismiss(true);
    }

    check(orderDto: IPPOrderDto) {
        orderDto.checked = !orderDto.checked;
        if (orderDto.checked) {
            this.newList.push(orderDto);
        } else {
            if (this.itemsSelected.some(x => x.ppOrderDetailId === orderDto.id)) {
                this.itemUnSelected.push(orderDto);
            }
            this.ppOrderModalService.setItemUnSelected(this.itemUnSelected);
            this.newList = this.newList.filter(x => x.id !== orderDto.id);
        }
    }

    isCheckAll() {
        if (this.ppOrders) {
            for (let i = 0; i < this.ppOrders.length; i++) {
                if (!this.ppOrders[i].checked) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    checkAll() {
        if (this.ppOrders) {
            const isCheckAll = this.isCheckAll();
            for (let i = 0; i < this.ppOrders.length; i++) {
                this.ppOrders[i].checked = !isCheckAll;
                if (!isCheckAll && !this.newList.some(x => x.id === this.ppOrders[i].id)) {
                    this.newList.push(this.ppOrders[i]);
                }
            }
        }
    }

    getCurrentDate(): string {
        const _date = moment();
        return `${_date.year()}-${_date.month() + 1}-${_date.date()}`;
    }
    getFromToMoment(date?: Moment, isMaxDate?: boolean): { year; month; day } {
        const _date = date && moment(date).isValid() ? date : isMaxDate ? null : moment();
        return _date ? { year: _date.year(), month: _date.month() + 1, day: _date.date() } : null;
    }
    changeReceivedQuantity(ppOrder: IPPOrderDto) {
        ppOrder.checked = true;
        if (this.newList.some(x => x.id === ppOrder.id)) {
            this.newList[this.newList.findIndex(x => x.id === ppOrder.id)] = ppOrder;
        } else {
            this.newList.push(ppOrder);
        }

        // this.utilsService.changeReceivedQuantity(ppOrder.receivedQuantity, ppOrder.quantityReceipt);
    }
}
