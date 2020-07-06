import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import * as moment from 'moment';
import { Moment } from 'moment';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { CurrencyService } from 'app/danhmuc/currency';
import { IPPPayVendor } from 'app/shared/model/pp-pay-vendor';
import { IPPPayVendorBill } from 'app/shared/model/pp-pay-vendor-bill';
import { PPPayVendorService } from 'app/muahang/tra_tien_nha_cung_cap/pp-pay-vendor.service';
import { DATE_FORMAT } from 'app/shared';
import { Principal } from 'app/core';
import { SO_LAM_VIEC, TypeID } from 'app/app.constants';
import { ToastrService } from 'ngx-toastr';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-pp-pay-vendor',
    templateUrl: './pp-pay-vendor.component.html',
    styleUrls: ['./pp-pay-vendor.component.css']
})
export class PPPayVendorComponent extends BaseComponent implements OnInit {
    private TYPE_FA_INCREAMENT = 610;
    private TYPE_TI_INCREAMENT = 510;
    accountingObjects: IAccountingObject[];
    pPPayVendors: IPPPayVendor[];
    pPPayVendorDetails: IPPPayVendorBill[];
    fromDate: Moment;
    toDate: Moment;
    postedDate: Moment;
    selectedRow: IPPPayVendor;
    account: any;
    typeLedger: string;
    TYPE_OPN = TypeID.OPN_DOI_TUONG;
    constructor(
        private jhiAlertService: JhiAlertService,
        private activatedRoute: ActivatedRoute,
        private currencyIDService: CurrencyService,
        private accountingObjectService: AccountingObjectService,
        private pPPayVendorService: PPPayVendorService,
        private router: Router,
        private principal: Principal,
        private toastr: ToastrService,
        public utilsService: UtilsService,
        private viewVoucherService: ViewVoucherService,
        private translate: TranslateService
    ) {
        super();
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
            if (this.account) {
                this.typeLedger = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                this.postedDate = this.utilsService.ngayHachToan(this.account);
                const date = this.postedDate.year();
                this.fromDate = moment('01/01/' + date);
                this.toDate = moment(Date());
                this.pPPayVendorDetails = [];
                this.accountingObjectService.query().subscribe(
                    (res: HttpResponse<IAccountingObject[]>) => {
                        this.accountingObjects = res.body;
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
                this.accountingObjectService
                    .getPPPayVendors({
                        fromDate: this.fromDate != null && this.fromDate.isValid() ? this.fromDate.format(DATE_FORMAT) : null,
                        toDate: this.toDate != null && this.toDate.isValid() ? this.toDate.format(DATE_FORMAT) : null
                    })
                    .subscribe((res: HttpResponse<IPPPayVendor[]>) => {
                        this.pPPayVendors = res.body.filter(x => x.soConPhaiTra !== 0);
                        this.objects = res.body;
                        if (this.pPPayVendors.length > 0) {
                            this.onSelect(this.pPPayVendors[0]);
                        } else {
                            this.pPPayVendorDetails = [];
                        }
                    });
            }
        });
    }

    previousState() {
        window.history.back();
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackAccountingObjectById(index: number, item: IAccountingObject) {
        return item.id;
    }

    onSelect(select: IPPPayVendor) {
        this.selectedRow = select;
        this.accountingObjectService
            .getPPPayVendorBills({
                fromDate: this.fromDate != null && this.fromDate.isValid() ? this.fromDate.format(DATE_FORMAT) : null,
                toDate: this.toDate != null && this.toDate.isValid() ? this.toDate.format(DATE_FORMAT) : null,
                accountObjectID: select.accountingObjectID
            })
            .subscribe((res: HttpResponse<IPPPayVendorBill[]>) => {
                this.pPPayVendorDetails = res.body;
            });
    }

    doubleClickRow(select: IPPPayVendor) {
        this.router.navigate([
            './pp-pay-vendor/pp-pay-vendor-detail',
            select.accountingObjectID,
            this.fromDate.format(DATE_FORMAT),
            this.toDate.format(DATE_FORMAT)
        ]);
    }

    checkFromDate() {
        if (moment(this.toDate) < moment(this.fromDate)) {
            this.toastr.error('Từ ngày phải nhỏ hơn đến ngày!', 'Lỗi');
            return;
        } else {
            this.accountingObjectService
                .getPPPayVendors({
                    fromDate: this.fromDate != null && this.fromDate.isValid() ? this.fromDate.format(DATE_FORMAT) : null,
                    toDate: this.toDate != null && this.toDate.isValid() ? this.toDate.format(DATE_FORMAT) : null
                })
                .subscribe((res: HttpResponse<IPPPayVendor[]>) => {
                    this.pPPayVendors = res.body.filter(x => x.soConPhaiTra !== 0);
                    this.objects = res.body;
                    if (this.pPPayVendors.length > 0) {
                        this.onSelect(this.pPPayVendors[0]);
                    } else {
                        this.pPPayVendorDetails = [];
                    }
                });
        }
    }

    checkToDate() {
        if (moment(this.toDate) < moment(this.fromDate)) {
            this.toastr.error('Từ ngày phải nhỏ hơn đến ngày!', 'Lỗi');
            return;
        } else {
            this.accountingObjectService
                .getPPPayVendors({
                    fromDate: this.fromDate != null && this.fromDate.isValid() ? this.fromDate.format(DATE_FORMAT) : null,
                    toDate: this.toDate != null && this.toDate.isValid() ? this.toDate.format(DATE_FORMAT) : null
                })
                .subscribe((res: HttpResponse<IPPPayVendor[]>) => {
                    this.pPPayVendors = res.body.filter(x => x.soConPhaiTra !== 0);
                    this.objects = res.body;
                    if (this.pPPayVendors.length > 0) {
                        this.onSelect(this.pPPayVendors[0]);
                    } else {
                        this.pPPayVendorDetails = [];
                    }
                });
        }
    }

    viewVoucher(pPPayVendorBill: IPPPayVendorBill) {
        let url = '';
        if (pPPayVendorBill.typeID === TypeID.MUA_HANG) {
            this.viewVoucherService.checkViaStockPPInvoice({ id: pPPayVendorBill.referenceID }).subscribe(
                (res: HttpResponse<any>) => {
                    if (res.body.message === UpdateDataMessages.STOCK_TRUE) {
                        url = `/#/mua-hang/qua-kho-ref/${pPPayVendorBill.referenceID}/edit/1`;
                        window.open(url, '_blank');
                    } else if (res.body.message === UpdateDataMessages.STOCK_FALSE) {
                        url = `/#/mua-hang/khong-qua-kho-ref/${pPPayVendorBill.referenceID}/edit/1`;
                        window.open(url, '_blank');
                    } else {
                        this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                    }
                    return;
                },
                (res: HttpErrorResponse) => {
                    this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                }
            );
        } else if (pPPayVendorBill.typeID === TypeID.MUA_DICH_VU) {
            url = `/#/mua-dich-vu/${pPPayVendorBill.referenceID}/edit/from-ref`;
        } else if (pPPayVendorBill.typeID === TypeID.MUA_HANG_GIAM_GIA) {
            url = `/#/hang-mua/giam-gia/${pPPayVendorBill.referenceID}/view`;
        } else if (pPPayVendorBill.typeID === this.TYPE_FA_INCREAMENT) {
            url = `/#/fa-increament/${pPPayVendorBill.referenceID}/edit/from-ref`;
        } else if (pPPayVendorBill.typeID === this.TYPE_TI_INCREAMENT) {
            url = `/#/ti-increament/${pPPayVendorBill.referenceID}/edit/from-ref`;
        } else if (pPPayVendorBill.typeID === TypeID.CHUNG_TU_NGHIEP_VU_KHAC) {
            url = `/#/g-other-voucher/${pPPayVendorBill.referenceID}/edit/from-ref`;
        }
        if (url) {
            window.open(url, '_blank');
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }
}
