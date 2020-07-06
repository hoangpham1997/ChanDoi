import { Component, OnInit } from '@angular/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { TypeService } from 'app/entities/type';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { HH_XUATQUASLTON, SO_LAM_VIEC, TCKHAC_MauCTuChuaGS } from 'app/app.constants';
import { Principal } from 'app/core';
import { TypeGroupService } from 'app/shared/modal/ref/type-group.service';
import { ITypeGroup } from 'app/shared/model/type-group.model';
import { GenCodeService } from 'app/entities/gen-code';
import { IGenCode } from 'app/shared/model/gen-code.model';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { Moment } from 'moment';
import { TranslateService } from '@ngx-translate/core';
import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-tim-kiem-chung-tu',
    templateUrl: './tim-kiem-chung-tu.component.html',
    styleUrls: ['tim-kiem-chung-tu.component.css']
})
export class TimKiemChungTuComponent extends BaseComponent implements OnInit {
    typeGroups: ITypeGroup[];
    viewVouchers: ViewVoucherNo[];
    isSoTaiChinh: boolean;
    account: any;
    typeSearch: number;
    typeGroupID: number;
    typeSearchs = [
        { value: 1, name: 'Loại chứng từ' },
        { value: 2, name: 'Số chứng từ' },
        {
            value: 3,
            name: 'Số hóa đơn'
        },
        { value: 4, name: 'Ngày hạch toán' },
        { value: 5, name: 'Ngày hóa đơn' }
        /*{value: 6, name: 'Loại tiền'},
        {value: 7, name: 'TK Nợ'},
        {value: 8, name: 'TK Có'},
        {value: 9, name: 'TK Ngân hàng'},
        {value: 10, name: 'Đối tượng'},
        {value: 11, name: 'Vật tư hàng hóa'},
        {value: 12, name: 'Kho'},
        {value: 13, name: 'Đối tượng THCP'},
        {value: 14, name: 'Khoản mục CP'},
        {value: 15, name: 'Mã thống kê'},
        {value: 16, name: 'Mục thu - chi'}*/
    ];
    listRecord: any;
    listColumnsRecord: string[] = ['name'];
    listHeaderColumnsRecord: string[] = ['Trạng thái'];
    statusRecord: any;
    fromDate: Moment;
    toDate: Moment;
    textSearch: string;
    invoiceDate: Moment;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    routeData: any;
    selectedRow: ViewVoucherNo;
    genCodes: IGenCode[];
    color: any;
    onlyOneBook: boolean;

    constructor(
        public utilsService: UtilsService,
        private typeService: TypeService,
        private typeGroupService: TypeGroupService,
        private principal: Principal,
        private genCodeService: GenCodeService,
        private viewVoucherService: ViewVoucherService,
        public translate: TranslateService,
        private activatedRoute: ActivatedRoute,
        private toastr: ToastrService,
        private router: Router
    ) {
        super();
        this.viewVouchers = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.typeGroupService.queryForPopup().subscribe(res => {
            this.typeGroups = res.body.filter(n => n.id !== 35);
        });
        this.genCodeService.getGenCodes().subscribe(res => {
            this.genCodes = res.body;
        });
        this.translate.get(['ebwebApp.mBDeposit.home.recorded', 'ebwebApp.mBDeposit.home.unrecorded']).subscribe(res => {
            this.listRecord = [
                { value: 1, name: res['ebwebApp.mBDeposit.home.recorded'] },
                { value: 2, name: res['ebwebApp.mBDeposit.home.unrecorded'] }
            ];
        });
    }

    ngOnInit(): void {
        this.principal.identity().then(account => {
            this.account = account;
            this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
            this.color = this.account.systemOption.find(x => x.code === TCKHAC_MauCTuChuaGS && x.data).data;
        });
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    search(fromButton?): void {
        if (fromButton) {
            this.page = 1;
        }
        if (this.checkErrorForSearch()) {
            this.viewVoucherService
                .searchVoucher({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    typeSearch: this.typeSearch ? this.typeSearch : '',
                    typeGroupID: this.typeGroupID ? this.typeGroupID : '',
                    no: this.textSearch ? this.textSearch : '',
                    // invoiceDate: this.invoiceDate ? this.invoiceDate.format(DATE_FORMAT) : '',
                    fromDate: this.fromDate ? this.fromDate.format(DATE_FORMAT) : '',
                    toDate: this.toDate ? this.toDate.format(DATE_FORMAT) : '',
                    recorded: this.statusRecord === 1 ? true : this.statusRecord === 2 ? false : ''
                })
                .subscribe((res: HttpResponse<ViewVoucherNo[]>) => {
                    this.paginate(res.body, res.headers);
                });
        }
    }

    resetSearch() {
        this.page = 1;
        this.typeSearch = null;
        this.statusRecord = null;
        this.invoiceDate = null;
        this.fromDate = null;
        this.toDate = null;
        this.textSearch = null;
        this.typeGroupID = null;
        this.viewVouchers = null;
        this.selectedRow = null;
    }

    transition() {
        this.router.navigate(['/tim-kiem-chung-tu'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage
            }
        });
        this.search();
    }

    checkErrorForSearch() {
        if (!this.typeSearch) {
            this.toastr.error('Chưa chọn loại tìm kiếm', this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        } else {
            if (this.typeSearch === 1 && !this.typeGroupID) {
                this.toastr.error('Chưa chọn loại chứng từ', this.translate.instant('ebwebApp.mCReceipt.error.error'));
                return false;
            }
            if (this.typeSearch === 2 && !this.textSearch) {
                this.toastr.error('Chưa nhập số chứng từ', this.translate.instant('ebwebApp.mCReceipt.error.error'));
                return false;
            }
            if (this.typeSearch === 3 && !this.textSearch) {
                this.toastr.error('Chưa nhập số hóa đơn', this.translate.instant('ebwebApp.mCReceipt.error.error'));
                return false;
            }
            if (this.typeSearch === 4 || this.typeSearch === 5) {
                if (!this.fromDate && !this.toDate) {
                    this.toastr.error('Chưa nhập từ ngày hoặc đén ngày', this.translate.instant('ebwebApp.mCReceipt.error.error'));
                    return false;
                }
            }
        }
        if (this.toDate && this.fromDate) {
            if (this.toDate < this.fromDate) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.fromDateGreaterToDate'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        return true;
    }

    selectedItemPerPage() {
        this.search();
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    private paginate(data: ViewVoucherNo[], headers: HttpHeaders) {
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.viewVouchers = data;
        this.objects = data;
        if (this.viewVouchers.length > 0) {
            /*this.toastr.success(
                this.translate.instant('ebwebApp.pPInvoice.found') +
                this.totalItems +
                ' ' +
                this.translate.instant('ebwebApp.pPInvoice.record'),
                this.translate.instant('ebwebApp.pPInvoice.message')
            );*/
        } else {
            this.toastr.warning(
                this.translate.instant('ebwebApp.pPInvoice.notFoundRecord'),
                this.translate.instant('ebwebApp.pPInvoice.message')
            );
        }
    }

    selectRow(detail): void {
        this.selectedRow = detail;
    }

    changeTypeGroupID(): void {
        const gencode: IGenCode = this.genCodes.find(
            n => n.typeGroupID === this.typeGroupID && (n.displayOnBook === 2 || n.displayOnBook === (this.isSoTaiChinh ? 0 : 1))
        );
        if (gencode) {
            if (gencode.displayOnBook === 2) {
                this.onlyOneBook = true;
                this.statusRecord = null;
            } else {
                this.onlyOneBook = false;
            }
        }
    }

    getStyle(record?: boolean) {
        return !record
            ? {
                  input: { color: this.color },
                  color: this.color
              }
            : {};
    }

    doubleClickRow(detial) {
        this.utilsService.viewVoucherByIDAndTypeGroupID(detial);
    }
}
