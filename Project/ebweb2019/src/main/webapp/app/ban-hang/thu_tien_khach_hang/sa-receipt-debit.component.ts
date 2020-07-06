import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import * as moment from 'moment';
import { Moment } from 'moment';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { CurrencyService } from 'app/danhmuc/currency';
import { PPPayVendorService } from 'app/muahang/tra_tien_nha_cung_cap/pp-pay-vendor.service';
import { DATE_FORMAT } from 'app/shared';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { ISAReceiptDebit } from 'app/shared/model/sa-receipt-debit';
import { ISAReceiptDebitBill } from 'app/shared/model/sa-receipt-debit-bill';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { SO_LAM_VIEC, TypeID } from 'app/app.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-sa-receipt-debit',
    templateUrl: './sa-receipt-debit.component.html',
    styleUrls: ['./sa-receipt-debit.component.css']
})
export class SAReceiptDebitComponent extends BaseComponent implements OnInit {
    accountingObjects: IAccountingObject[];
    saReceiptDebits: ISAReceiptDebit[];
    saReceiptDebitBills: ISAReceiptDebitBill[];
    fromDate: Moment;
    toDate: Moment;
    postedDate: Moment;
    selectedRow: ISAReceiptDebit;
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
        public utilsService: UtilsService
    ) {
        super();
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
            this.typeLedger = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
            this.postedDate = this.utilsService.ngayHachToan(this.account);
            const date = this.postedDate.year();
            this.fromDate = moment('01/01/' + date);
            this.toDate = moment(Date());
            this.saReceiptDebitBills = [];
            this.accountingObjectService.query().subscribe(
                (res: HttpResponse<IAccountingObject[]>) => {
                    this.accountingObjects = res.body;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
            this.accountingObjectService
                .getSAReceiptDebits({
                    fromDate: this.fromDate != null && this.fromDate.isValid() ? this.fromDate.format(DATE_FORMAT) : null,
                    toDate: this.toDate != null && this.toDate.isValid() ? this.toDate.format(DATE_FORMAT) : null
                })
                .subscribe((res: HttpResponse<ISAReceiptDebit[]>) => {
                    this.saReceiptDebits = res.body.filter(x => x.soConPhaiThu !== 0);
                    this.objects = res.body;
                    if (this.saReceiptDebits.length > 0) {
                        this.onSelect(this.saReceiptDebits[0]);
                    } else {
                        this.saReceiptDebitBills = [];
                    }
                });
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

    onSelect(select: ISAReceiptDebit) {
        this.selectedRow = select;
        this.accountingObjectService
            .getSAReceiptDebitBills({
                fromDate: this.fromDate != null && this.fromDate.isValid() ? this.fromDate.format(DATE_FORMAT) : null,
                toDate: this.toDate != null && this.toDate.isValid() ? this.toDate.format(DATE_FORMAT) : null,
                accountObjectID: select.accountingObjectID
            })
            .subscribe((res: HttpResponse<ISAReceiptDebitBill[]>) => {
                this.saReceiptDebitBills = res.body;
            });
    }

    doubleClickRow(select: ISAReceiptDebit) {
        this.router.navigate([
            './sa-receipt-debit/sa-receipt-debit-detail',
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
                .getSAReceiptDebits({
                    fromDate: this.fromDate != null && this.fromDate.isValid() ? this.fromDate.format(DATE_FORMAT) : null,
                    toDate: this.toDate != null && this.toDate.isValid() ? this.toDate.format(DATE_FORMAT) : null
                })
                .subscribe((res: HttpResponse<ISAReceiptDebit[]>) => {
                    this.saReceiptDebits = res.body.filter(x => x.soConPhaiThu !== 0);
                    this.objects = res.body;
                    if (this.saReceiptDebits.length > 0) {
                        this.onSelect(this.saReceiptDebits[0]);
                    } else {
                        this.saReceiptDebitBills = [];
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
                .getSAReceiptDebits({
                    fromDate: this.fromDate != null && this.fromDate.isValid() ? this.fromDate.format(DATE_FORMAT) : null,
                    toDate: this.toDate != null && this.toDate.isValid() ? this.toDate.format(DATE_FORMAT) : null
                })
                .subscribe((res: HttpResponse<ISAReceiptDebit[]>) => {
                    this.saReceiptDebits = res.body.filter(x => x.soConPhaiThu !== 0);
                    this.objects = res.body;
                    if (this.saReceiptDebits.length > 0) {
                        this.onSelect(this.saReceiptDebits[0]);
                    } else {
                        this.saReceiptDebitBills = [];
                    }
                });
        }
    }

    viewVoucher(saReceiptDebitBill: ISAReceiptDebitBill) {
        let url = '';
        if (saReceiptDebitBill.typeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
            url = `/#/chung-tu-ban-hang/${saReceiptDebitBill.referenceID}/edit/from-ref`;
        } else if (saReceiptDebitBill.typeID === TypeID.CHUNG_TU_NGHIEP_VU_KHAC) {
            url = `/#/g-other-voucher/${saReceiptDebitBill.referenceID}/edit/from-ref`;
        }
        window.open(url, '_blank');
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }
}
