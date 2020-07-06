import { Component, ElementRef, OnInit, Renderer } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { LoginService } from 'app/core/login/login.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import * as moment from 'moment';
import { Moment } from 'moment';
import { TypeGroupService } from 'app/shared/modal/ref/type-group.service';
import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared/index';
import { SAInvoiceService } from 'app/ban-hang/ban_hang_chua_thu_tien';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { HttpResponse } from '@angular/common/http';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { TypeID } from 'app/app.constants';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Principal } from 'app/core';
import { ISAReceiptDebitBill } from 'app/shared/model/sa-receipt-debit-bill';

@Component({
    selector: 'eb-sa-invoice-modal',
    templateUrl: './sa-invoice.component.html'
})
export class EbSaInvoiceModalComponent implements OnInit {
    data: any[];
    recorded: any;
    status: any;
    fromDate;
    any;
    toDate: any;
    typeGroups: any;
    typeGroup: any;
    typeSearch: number;
    viewVouchers: any[];
    no: any;
    invoiceNo: any;
    totalItems: number;
    page: number;
    itemsPerPage = ITEMS_PER_PAGE;
    links: any;
    queryCount: any;
    newList: any[];
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    accountingObjects: IAccountingObject[];
    accountingObject: IAccountingObject;
    listTimeLine: any[];
    typeID: any;
    accountingObjectId: any;
    timeLineVoucher: any;
    XUAT_HOA_DON = TypeID.XUAT_HOA_DON;
    HANG_BAN_TRA_LAI = TypeID.HANG_BAN_TRA_LAI;
    HANG_GIAM_GIA = TypeID.HANG_GIAM_GIA;
    currentAccount: any;
    masterSelected: boolean;
    modalData: any;
    currencyID: string;
    objectId: string;

    constructor(
        private eventManager: JhiEventManager,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private router: Router,
        private activeModal: NgbActiveModal,
        private saInvoiceService: SAInvoiceService,
        private typeGroupService: TypeGroupService,
        private parseLinks: JhiParseLinks,
        public utilsService: UtilsService,
        private accountingObjectService: AccountingObjectService,
        private toastrService: ToastrService,
        private translateService: TranslateService,
        private principal: Principal
    ) {
        this.typeSearch = 1;
        this.viewVouchers = [];
        this.listTimeLine = this.utilsService.getCbbTimeLine();
        this.timeLineVoucher = this.listTimeLine[4].value;
        this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
    }

    ngOnInit(): void {
        this.getAccountingObject();
        this.typeGroupService.query().subscribe(res => {
            this.typeGroups = res.body;
        });
        this.page = 1;
        this.newList = [];
        if (this.data) {
            this.newList.push(...this.data);
        }
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
    }

    getAccountingObject() {
        this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjects = res.body.filter(x => x.isActive);
            this.accountingObject = this.accountingObjects.find(x => x.id === this.accountingObjectId);
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
        if (!this.fromDate) {
            this.toastrService.error(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.fromDateNull'));
            return;
        }
        if (!this.toDate) {
            this.toastrService.error(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.toDateNull'));
            return;
        }
        if (this.fromDate > this.toDate) {
            this.toastrService.error(this.translateService.instant('ebwebApp.mCAudit.errorDate'));
            return;
        }
        const fromDateP = this.fromDate instanceof moment ? this.fromDate.format('YYYYMMDD') : this.fromDate;
        const toDateP = this.toDate instanceof moment ? this.toDate.format('YYYYMMDD') : this.toDate;
        if (this.fromDate > this.toDate) {
            this.toastrService.error(this.translateService.instant('ebwebApp.mCAudit.errorDate'));
            return;
        }
        this.saInvoiceService
            .searchSAInvoicePopup({
                currencyID: this.currencyID,
                page: this.page - 1,
                size: this.itemsPerPage,
                accountingObjectID: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
                fromDate: fromDateP ? fromDateP : '',
                toDate: toDateP ? toDateP : '',
                typeID: this.typeID,
                objectID: this.objectId ? this.objectId : '',
                createForm: this.modalData
            })
            .subscribe(res => {
                this.viewVouchers = res.body;
                if (this.newList) {
                    this.viewVouchers.forEach(item => {
                        if (this.typeID === this.XUAT_HOA_DON) {
                            item.checked = this.newList.some(data => data.saInvoiceID === item.saInvoiceID);
                        } else {
                            item.checked = this.newList.some(data => data.id === item.id);
                        }
                    });
                }
                this.links = this.parseLinks.parse(res.headers.get('link'));
                this.totalItems = parseInt(res.headers.get('X-Total-Count'), 10);
                this.queryCount = this.totalItems;
            });
    }

    apply() {
        if (this.typeID === this.HANG_BAN_TRA_LAI) {
            for (let i = 0; i < this.viewVouchers.length; i++) {
                if (this.viewVouchers[i].returnQuantity < 0) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.common.error.returnQuantityLess'));
                    return;
                }
            }
        }
        const ids = this.newList.map(item => {
            if (this.typeID === this.XUAT_HOA_DON) {
                return {
                    saInvoiceID: item.saInvoiceID,
                    returnQuantity: item.returnQuantity,
                    quantity: item.quantity,
                    typeID: this.XUAT_HOA_DON,
                    refTypeID: this.modalData
                };
            } else {
                return { id: item.id, returnQuantity: item.returnQuantity, quantity: item.quantity, typeID: this.HANG_BAN_TRA_LAI };
            }
        });
        if (ids.length) {
            this.saInvoiceService.getSaInvoiceDetail(ids).subscribe(res => {
                this.eventManager.broadcast({
                    name: 'selectSaInvoice',
                    content: res.body
                });
                this.activeModal.dismiss(true);
            });
        } else {
            this.eventManager.broadcast({
                name: 'selectSaInvoice',
                content: []
            });
            this.activeModal.dismiss(true);
        }
    }

    close() {
        this.activeModal.dismiss(false);
    }

    getCurrentDate() {
        const _date = moment();
        return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
    }

    isCheckAll() {
        return this.viewVouchers.every(item => item.checked) && this.viewVouchers.length;
    }

    checkAll() {
        const isCheck = this.viewVouchers.length && this.viewVouchers.every(item => item.checked);
        this.viewVouchers.forEach(item => (item.checked = !isCheck));
        if (this.typeID === this.XUAT_HOA_DON) {
            if (!isCheck) {
                for (let j = 0; j < this.viewVouchers.length; j++) {
                    let isPush = false;
                    for (let i = 0; i < this.newList.length; i++) {
                        if (this.newList[i].saInvoiceID === this.viewVouchers[j].saInvoiceID) {
                            isPush = true;
                        }
                    }
                    if (!isPush) {
                        this.newList.push(this.viewVouchers[j]);
                    }
                }
            } else {
                for (let j = 0; j < this.viewVouchers.length; j++) {
                    for (let i = 0; i < this.newList.length; i++) {
                        if (this.newList[i].saInvoiceID === this.viewVouchers[j].saInvoiceID) {
                            this.newList.splice(i, 1);
                            i--;
                        }
                    }
                }
            }
        } else {
            if (!isCheck) {
                for (let j = 0; j < this.viewVouchers.length; j++) {
                    let isPush = false;
                    for (let i = 0; i < this.newList.length; i++) {
                        if (this.newList[i].id === this.viewVouchers[j].id) {
                            isPush = true;
                        }
                    }
                    if (!isPush) {
                        this.newList.push(this.viewVouchers[j]);
                    }
                }
            } else {
                for (let j = 0; j < this.viewVouchers.length; j++) {
                    for (let i = 0; i < this.newList.length; i++) {
                        if (this.newList[i].id === this.viewVouchers[j].id) {
                            this.newList.splice(i, 1);
                            i--;
                        }
                    }
                }
            }
        }
    }

    check(viewVoucher) {
        viewVoucher.checked = !viewVoucher.checked;
        if (viewVoucher.checked) {
            if (this.typeID === this.XUAT_HOA_DON) {
                const isCheck = this.newList.some(item => item.saInvoiceID === viewVoucher.saInvoiceID);
                if (!isCheck) {
                    this.newList.push(viewVoucher);
                }
            } else {
                const isCheck = this.newList.some(item => item.id === viewVoucher.id);
                if (!isCheck) {
                    this.newList.push(viewVoucher);
                }
            }
        } else {
            if (this.typeID === this.XUAT_HOA_DON) {
                for (let i = 0; i < this.newList.length; i++) {
                    if (this.newList[i].saInvoiceID === viewVoucher.saInvoiceID) {
                        this.newList.splice(i, 1);
                        i--;
                    }
                }
            } else {
                for (let i = 0; i < this.newList.length; i++) {
                    if (this.newList[i].id === viewVoucher.id) {
                        this.newList.splice(i, 1);
                        i--;
                    }
                }
            }
        }
    }

    checkQuantity(detail) {
        if (detail.quantity < detail.returnQuantity) {
            this.toastrService.error(this.translateService.instant('ebwebApp.common.error.returnQuantityGreater'));
            detail.returnQuantity = 0;
        }
    }

    viewVoucher(id) {
        let url = '';
        if (this.modalData === TypeID.MUA_HANG_TRA_LAI) {
            url = `/#/hang-mua/tra-lai/${id}/edit/from-ref`;
        } else if (this.modalData === TypeID.HANG_GIAM_GIA) {
            url = `/#/hang-ban/giam-gia/${id}/edit/from-ref`;
        } else {
            url = `/#/chung-tu-ban-hang/${id}/edit/from-ref`;
        }
        window.open(url, '_blank');
    }
}
