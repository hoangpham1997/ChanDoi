import { Component, ElementRef, OnInit, Renderer } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { LoginService } from 'app/core/login/login.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import * as moment from 'moment';
import { Moment } from 'moment';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { TypeGroupService } from 'app/shared/modal/ref/type-group.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { HttpResponse } from '@angular/common/http';
import { ISaBillCreated } from 'app/shared/model/sa-bill-created';
import { SaBillService } from 'app/ban-hang/xuat-hoa-don/sa-bill.service';
import { PPInvoiceDetailsService } from 'app/entities/nhan-hoa-don/pp-invoice-details.service';
import { PPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { equal } from 'assert';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { GROUP_TYPEID } from 'app/app.constants';

@Component({
    selector: 'eb-export-invoice-modal',
    templateUrl: './export-invoice.component.html',
    styleUrls: ['export-invoice.component.css']
})
export class EbExportIncoiceModalComponent implements OnInit {
    accountingObjects: any;
    currencyCode: any;
    accountingObject: any;
    typeID: any;
    typeHAngMuaTraLai: any;
    listTimeLine: any[];
    checked: boolean;
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
    fromDate: any;
    toDate: any;
    pageVoucher: any;
    pageCountVoucher: any;
    totalItemsVoucher: any;
    itemsPerPageVoucher: any;
    timeLineVoucher: any;
    links: any;
    previousPageVoucher: any;
    data: any[];
    viewVouchers: any[];
    saBillIdList: any[];
    newList: any[];
    dataViewVouchers: any[];
    currentAccount: any;
    invoiceNo: string;
    constructor(
        private eventManager: JhiEventManager,
        private parseLinks: JhiParseLinks,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private router: Router,
        private saBillService: SaBillService,
        private ppInvoiceDetailsService: PPInvoiceDetailsService,
        public utilsService: UtilsService,
        private activeModal: NgbActiveModal,
        private accountingObjectService: AccountingObjectService,
        private viewVoucherService: ViewVoucherService,
        private typeGroupService: TypeGroupService,
        private principal: Principal,
        private toastrService: ToastrService,
        private translateService: TranslateService
    ) {
        this.typeHAngMuaTraLai = GROUP_TYPEID.GROUP_PPDISCOUNTRETURN;
        this.listTimeLine = this.utilsService.getCbbTimeLine();
        this.timeLineVoucher = 4;
        this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
    }

    ngOnInit(): void {
        this.invoiceNo = '';
        this.timeLineVoucher = this.listTimeLine[4].value;
        this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
        this.newList = [];
        if (this.data) {
            this.dataViewVouchers = this.data;
        }
        this.itemsPerPageVoucher = ITEMS_PER_PAGE;
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.search();
        this.getAccountingObject();
    }
    getAccountingObject() {
        if (this.typeID === GROUP_TYPEID.GROUP_PPDISCOUNTRETURN) {
            this.accountingObjectService.getAccountingObjectActive().subscribe((res: HttpResponse<IAccountingObject[]>) => {
                this.accountingObjects = res.body;
            });
        } else {
            this.accountingObjectService.getAllDTO().subscribe((data: HttpResponse<IAccountingObject[]>) => {
                this.accountingObjects = data.body
                    .filter(n => n.isActive && (n.objectType === 1 || n.objectType === 2))
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            });
        }
    }

    apply() {
        const viewVouchersSelectedId = this.newList.filter(item => item.checked);
        this.saBillIdList = [];
        viewVouchersSelectedId.forEach(object => {
            if (object.checked) {
                this.saBillIdList.push(object.id);
                if (object.invoiceNo) {
                    this.invoiceNo += object.invoiceNo + ', ';
                }
            }
        });
        this.saBillService.getSabillCreatedDetail({ sabillIdList: this.saBillIdList }).subscribe(ref => {
            // @ts-ignore
            this.dataViewVouchers.push(...ref);
            for (let i = 0; i < this.dataViewVouchers.length; i++) {
                for (let j = 0; j < i; j++) {
                    if (this.dataViewVouchers[i].id === this.dataViewVouchers[j].id) {
                        this.dataViewVouchers.splice(i, 1);
                        i--;
                    }
                }
            }
            this.invoiceNo = this.invoiceNo.substring(0, this.invoiceNo.length - 2);
            let viewVouchersSelected: any[];
            viewVouchersSelected = this.dataViewVouchers;
            if (viewVouchersSelected) {
                this.eventManager.broadcast({
                    name: 'selectViewInvoice',
                    content: viewVouchersSelected,
                    invoiceNo: this.invoiceNo
                });
            }
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

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDate = moment(this.objTimeLine.dtBeginDate).format('YYYYMMDD');
            this.toDate = moment(this.objTimeLine.dtEndDate).format('YYYYMMDD');
        }
    }

    search() {
        this.pageVoucher = 1;
        this.searchDataSabill();
    }

    searchDataSabill() {
        if (!this.fromDate) {
            this.toastrService.error(
                this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.fromDateNull'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
            return;
        }
        if (!this.toDate) {
            this.toastrService.error(
                this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.toDateNull'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
            return;
        }
        if (this.fromDate > this.toDate) {
            this.toastrService.error(
                this.translateService.instant('ebwebApp.mCAudit.errorDate'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
            return;
        }
        this.saBillService
            .getSabillCreated({
                page: this.pageVoucher - 1,
                size: this.itemsPerPageVoucher,
                accountingObjectID: this.accountingObject ? this.accountingObject : '',
                formDate: this.fromDate,
                toDate: this.toDate,
                currencyCode: this.currencyCode ? this.currencyCode : ''
            })
            .subscribe((res: HttpResponse<ISaBillCreated[]>) => {
                this.links = this.parseLinks.parse(res.headers.get('link'));
                this.totalItemsVoucher = parseInt(res.headers.get('X-Total-Count'), 10);
                this.pageCountVoucher = Math.ceil(this.totalItemsVoucher / this.itemsPerPageVoucher);
                this.viewVouchers = res.body;
                this.previousPageVoucher = this.pageVoucher;
                if (this.data) {
                    this.viewVouchers.forEach(item => {
                        item.checked = this.data.some(dataItem => dataItem.saBillID === item.id);
                        if (item.checked) {
                            if (!this.data.some(newListItem => newListItem.id === item.id)) {
                                this.newList.push(item);
                            }
                        }
                    });
                }
                this.viewVouchers.forEach(item => {
                    item.checked = this.newList.some(dataItem => dataItem.id === item.id);
                });
            });
    }

    isCheckAll() {
        return this.viewVouchers && this.viewVouchers.every(item => item.checked) && this.viewVouchers.length;
    }

    checkAll() {
        const isCheck = this.viewVouchers.every(item => item.checked) && this.viewVouchers.length;
        this.viewVouchers.forEach(item => (item.checked = !isCheck));

        if (!isCheck) {
            for (let j = 0; j < this.viewVouchers.length; j++) {
                let isPush = true;
                for (let i = 0; i < this.newList.length; i++) {
                    if (this.newList[i].id === this.viewVouchers[j].id) {
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
                    if (this.newList[i].id === this.viewVouchers[j].id) {
                        this.newList.splice(i, 1);
                        i--;
                    }
                }
            }
        }
    }

    selectedItemPerPage() {
        this.pageVoucher = 1;
        this.searchDataSabill();
    }

    loadPage(page: number) {
        if (page !== this.previousPageVoucher) {
            this.pageVoucher = page;
            this.transition();
        }
    }

    transition() {
        this.searchDataSabill();
    }

    check(viewVoucher) {
        viewVoucher.checked = !viewVoucher.checked;
        if (viewVoucher.checked) {
            this.newList.push(viewVoucher);
        } else {
            for (let i = 0; i < this.newList.length; i++) {
                if (this.newList[i].id === viewVoucher.id) {
                    this.newList.splice(i, 1);
                    i--;
                }
            }
            for (let i = 0; i < this.dataViewVouchers.length; i++) {
                if (this.dataViewVouchers[i].saBillID === viewVoucher.id) {
                    this.dataViewVouchers.splice(i, 1);
                    i--;
                }
            }
        }
    }
}
