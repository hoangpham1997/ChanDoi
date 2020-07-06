import { Component, ElementRef, OnInit, Renderer } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { LoginService } from 'app/core/login/login.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import * as moment from 'moment';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { TypeGroupService } from 'app/shared/modal/ref/type-group.service';
import { ITEMS_PER_PAGE } from 'app/shared';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';

@Component({
    selector: 'eb-ref-modal',
    templateUrl: './ref.component.html'
})
export class EbRefModalComponent implements OnInit {
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
    timeLineVoucher: any;
    listTimeLine: any[];
    objTimeLine: { dtBeginDate?: string; dtEndDate?: string };

    constructor(
        private eventManager: JhiEventManager,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private router: Router,
        private activeModal: NgbActiveModal,
        private viewVoucherService: ViewVoucherService,
        private typeGroupService: TypeGroupService,
        private parseLinks: JhiParseLinks,
        public utilsService: UtilsService,
        private toastrService: ToastrService,
        private translateService: TranslateService
    ) {
        this.typeSearch = 1;
        this.viewVouchers = [];
        this.listTimeLine = this.utilsService.getCbbTimeLine();
        this.timeLineVoucher = this.listTimeLine[4].value;
        this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
    }

    ngOnInit(): void {
        // this.search();
        this.typeGroupService.queryForPopup().subscribe(res => {
            this.typeGroups = res.body;
        });
        this.page = 1;
        if (this.data) {
            this.newList = [];
            this.newList.push(...this.data);
        }
    }

    search() {
        if (
            (!this.typeGroup && this.typeSearch === 1) ||
            (!this.no && this.typeSearch === 2) ||
            (!this.invoiceNo && this.typeSearch === 3)
        ) {
            this.toastrService.error(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.typeGroup'));
            return;
        }
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
        this.viewVoucherService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                typeSearch: this.typeSearch ? this.typeSearch : '',
                status: this.status !== null && this.status !== undefined ? this.status : '',
                fromDate: this.fromDate ? this.fromDate : '',
                toDate: this.toDate ? this.toDate : '',
                typeGroup: this.typeGroup && this.typeGroup.id ? this.typeGroup.id : '',
                recorded: this.recorded !== null && this.recorded !== undefined ? this.recorded : '',
                no: this.no ? this.no : '',
                invoiceNo: this.invoiceNo ? this.invoiceNo : ''
            })
            .subscribe(res => {
                this.viewVouchers = res.body;
                if (this.newList) {
                    this.viewVouchers.forEach(item => {
                        item.checked = this.newList.some(data => data.refID2 === item.refID2);
                    });
                }
                this.links = this.parseLinks.parse(res.headers.get('link'));
                this.totalItems = parseInt(res.headers.get('X-Total-Count'), 10);
                this.queryCount = this.totalItems;
            });
    }

    apply() {
        // const viewVouchersSelected = this.viewVouchers.filter(item => item.checked);
        this.eventManager.broadcast({
            name: 'selectViewVoucher',
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

    isCheckAll() {
        return this.viewVouchers.every(item => item.checked) && this.viewVouchers.length;
    }

    checkAll() {
        const isCheck = this.viewVouchers.every(item => item.checked) && this.viewVouchers.length;
        this.viewVouchers.forEach(item => (item.checked = !isCheck));

        if (!isCheck) {
            for (let j = 0; j < this.viewVouchers.length; j++) {
                let isPush = true;
                for (let i = 0; i < this.newList.length; i++) {
                    if (this.newList[i].refID2 === this.viewVouchers[j].refID2) {
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
                    if (this.newList[i].refID2 === this.viewVouchers[j].refID2) {
                        this.newList.splice(i, 1);
                        i--;
                    }
                }
            }
        }
    }

    check(viewVoucher) {
        viewVoucher.checked = !viewVoucher.checked;
        if (viewVoucher.checked) {
            this.changeTypeSearch(viewVoucher);
            this.newList.push(viewVoucher);
        } else {
            for (let i = 0; i < this.newList.length; i++) {
                if (this.newList[i].refID2 === viewVoucher.refID2) {
                    this.newList.splice(i, 1);
                    i--;
                }
            }
        }
    }

    // nếu là loại chứng từ thì chỉ cho chọn 1 giá trị có cùng type group, bỏ các giá trị khác
    changeTypeSearch(viewVoucher = null) {
        // if (this.viewVouchers && this.viewVouchers.length) {
        //     if (viewVoucher.typeGroupID !== this.viewVouchers[0].typeGroupID) {
        //         this.viewVouchers.forEach(item => {
        //             item.checked = !(!viewVoucher || item.no !== viewVoucher.no);
        //         });
        //     }
        // }

        if (this.newList && this.newList.length) {
            if (viewVoucher && viewVoucher.typeGroupID !== this.newList[0].typeGroupID) {
                this.newList = [];
                this.viewVouchers.forEach(item => {
                    item.checked = !(!viewVoucher || item.no !== viewVoucher.no);
                });
            }
        }
    }

    selectChangeBeginDateAndEndDate(intTimeLine: String) {
        if (intTimeLine) {
            this.objTimeLine = this.utilsService.getTimeLine(intTimeLine);
            this.fromDate = moment(this.objTimeLine.dtBeginDate).format('YYYYMMDD');
            this.toDate = moment(this.objTimeLine.dtEndDate).format('YYYYMMDD');
        }
    }

    view(voucher, $event) {
        $event.stopPropagation();
        let url = '';
        console.log(voucher.typeGroupID);
        switch (voucher.typeGroupID) {
            // Hàng bán trả lại
            case 33:
                url = `/#/hang-ban/tra-lai/${voucher.refID2}/edit/from-ref`;
                break;
            // Giảm giá hàng bán
            case 34:
                url = `/#/hang-ban/giam-gia/${voucher.refID2}/edit/from-ref`;
                break;
            // Xuất hóa đơn
            case 35:
                url = `/#/xuat-hoa-don/${voucher.refID2}/edit/from-ref`;
                break;
            case 22:
                url = `/#/hang-mua/tra-lai/${voucher.refID2}/view`;
                break;
            case 23:
                url = `/#/hang-mua/giam-gia/${voucher.refID2}/view`;
                break;
            case 10:
                url = `/#/mc-receipt/${voucher.refID2}/edit/from-ref`;
                break;
            case 16:
                url = `/#/mb-deposit/${voucher.refID2}/edit/from-ref`;
                break;
            case 17:
                url = `/#/mb-credit-card/${voucher.refID2}/edit/from-ref`;
                break;
            case 70:
                url = `/#/g-other-voucher/${voucher.refID2}/edit/from-ref`;
                break;
            case 11:
                url = `/#/mc-payment/${voucher.refID2}/edit/from-ref`;
                break;
            case 31:
                url = `/#/sa-order/${voucher.refID2}/edit/from-ref`;
                break;
            case 24:
                url = `/#/mua-dich-vu/${voucher.refID2}/edit/from-ref`;
                break;
            case 40:
                url = `/#/nhap-kho/${voucher.refID2}/edit/from-ref`;
                break;
            case 20:
                url = `/#/don-mua-hang/${voucher.refID2}/edit/from-ref`;
                break;
            case 41:
                url = `/#/xuat-kho/${voucher.refID2}/edit/from-ref`;
                break;
            case 21:
                this.viewVoucherService.checkViaStockPPInvoice({ id: voucher.refID2 }).subscribe(
                    (res: HttpResponse<any>) => {
                        if (res.body.message === UpdateDataMessages.STOCK_TRUE) {
                            url = `/#/mua-hang/qua-kho/${voucher.refID2}/edit/1`;
                            window.open(url, '_blank');
                        } else if (res.body.message === UpdateDataMessages.STOCK_FALSE) {
                            url = `/#/mua-hang/khong-qua-kho/${voucher.refID2}/edit/1`;
                            window.open(url, '_blank');
                        } else {
                            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.default'));
                        }
                        return;
                    },
                    (res: HttpErrorResponse) => {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.default'));
                    }
                );
                return;
            case 18:
                url = `/#/mc-audit/${voucher.refID2}/edit/from-ref`;
                break;
            case 32:
                url = `/#/chung-tu-ban-hang/${voucher.refID2}/edit/from-ref`;
                break;
            case 30:
                url = `/#/sa-quote/${voucher.refID2}/edit/from-ref`;
                break;
            case 12:
            case 13:
            case 14:
                url = `/#/mb-teller-paper/${voucher.refID2}/edit/from-ref`;
                break;
            case 42:
                url = `/#/chuyen-kho/${voucher.refID2}/edit/from-ref`;
                break;
        }
        window.open(url, '_blank');
    }
}
