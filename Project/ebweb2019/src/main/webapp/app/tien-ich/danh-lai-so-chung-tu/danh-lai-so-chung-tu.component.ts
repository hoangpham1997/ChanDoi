import { Component, OnInit, ViewChild } from '@angular/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { TypeService } from 'app/entities/type';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { SO_LAM_VIEC, TCKHAC_MauCTuChuaGS } from 'app/app.constants';
import { Principal } from 'app/core';
import { TypeGroupService } from 'app/shared/modal/ref/type-group.service';
import { ITypeGroup } from 'app/shared/model/type-group.model';
import { GenCodeService } from 'app/entities/gen-code';
import { IGenCode } from 'app/shared/model/gen-code.model';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { TranslateService } from '@ngx-translate/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { ToastrService } from 'ngx-toastr';
import { Moment } from 'moment';
import { IResponeSds } from 'app/shared/model/hoa-don-dien-tu/respone-sds';
import { RequestResetNoDtoModel } from 'app/tien-ich/danh-lai-so-chung-tu/reques-reset-no-dto.model';
import { IMCReceipt } from 'app/shared/model/mc-receipt.model';
import { isNumber } from 'util';
import { isNumeric } from 'rxjs/internal-compatibility';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'eb-danh-lai-so-chung-tu',
    templateUrl: './danh-lai-so-chung-tu.component.html',
    styleUrls: ['danh-lai-so-chung-tu.component.css']
})
export class DanhLaiSoChungTuComponent implements OnInit {
    @ViewChild('content') public modalComponent: NgbModalRef;
    // typeGroups: ITypeGroup[];
    genCodes: IGenCode[];
    viewVoucher: ViewVoucherNo[];
    viewVoucherChange: ViewVoucherNo[];
    isSoTaiChinh: boolean;
    account: any;
    typeGroupID: number;
    prefix: string;
    currentValue: string;
    suffix: string;
    totalItems: number;
    queryCount: any;
    itemsPerPage: number;
    page: number;
    predicate: any;
    previousPage: any;
    reverse: any;
    routeData: any;
    fromDate: Moment;
    toDate: Moment;
    selectedRow: ViewVoucherNo;
    hasChange: boolean;
    onlyOneBook: boolean;
    color: any;
    modalRef: NgbModalRef;

    constructor(
        public utilsService: UtilsService,
        private typeService: TypeService,
        private typeGroupService: TypeGroupService,
        private principal: Principal,
        private genCodeService: GenCodeService,
        private viewVoucherService: ViewVoucherService,
        public translate: TranslateService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private toastr: ToastrService,
        private modalService: NgbModal
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        /*this.typeGroupService.queryForPopup().subscribe(res => {
            this.typeGroups = res.body;
        });*/
        this.genCodeService.getGenCodes().subscribe(res => {
            this.genCodes = res.body.filter(n => n.typeGroupID !== 51 && n.typeGroupID !== 61);
        });
        this.viewVoucher = [];
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

    loadData(fromButton?, notSetNewNo?) {
        if (this.checkError()) {
            if (fromButton) {
                this.viewVoucherChange = [];
                this.selectedRow = null;
                this.page = 1;
            }
            this.viewVoucherService
                .getVoucherByTypeGroup({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    typeGroupID: this.typeGroupID ? this.typeGroupID : '',
                    fromDate: this.fromDate ? this.fromDate.format(DATE_FORMAT) : '',
                    toDate: this.toDate ? this.toDate.format(DATE_FORMAT) : ''
                })
                .subscribe((res: HttpResponse<ViewVoucherNo[]>) => {
                    this.paginate(res.body, res.headers, notSetNewNo);
                });
        }
    }

    resetDataSearch() {
        this.prefix = null;
        this.currentValue = null;
        this.suffix = null;
        this.viewVoucher = [];
        this.viewVoucherChange = [];
    }

    checkError() {
        if (this.toDate && this.fromDate) {
            if (this.toDate.isBefore(this.fromDate)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.fromDateGreaterToDate'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        if (!this.typeGroupID) {
            this.toastr.error('Chưa chọn loại chứng từ', this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        if (!this.currentValue) {
            this.toastr.error('Chưa nhập só thứ tự bắt đầu', this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        if (!isNumeric(this.currentValue)) {
            this.toastr.error('Số thứ tự bắt đầu không đúng định dạng', this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        return true;
    }

    changeTypeGroup(): void {
        if (!this.typeGroupID) {
            this.resetDataSearch();
            return;
        }
        const gencode: IGenCode = this.genCodes.find(
            n => n.typeGroupID === this.typeGroupID && (n.displayOnBook === 2 || n.displayOnBook === (this.isSoTaiChinh ? 0 : 1))
        );
        if (gencode) {
            this.prefix = gencode.prefix;
            this.suffix = gencode.suffix;
            this.currentValue = '0';
            if (gencode.displayOnBook === 2) {
                this.onlyOneBook = true;
            } else {
                this.onlyOneBook = false;
            }
        }
    }

    selectedItemPerPage() {
        this.loadData();
    }

    loadPage(page: number) {
        this.addListChange();
        this.selectedRow = null;
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/danh-lai-so-chung-tu'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage
            }
        });
        this.loadData();
    }

    private paginate(data: ViewVoucherNo[], headers: HttpHeaders, notSetNewNo?) {
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.viewVoucher = data;
        if (!notSetNewNo) {
            this.setNoNew();
        }
    }

    selectRow(detail) {
        this.selectedRow = detail;
    }

    resetNo(): void {
        if (this.checkError()) {
            if (this.viewVoucher.length === 0) {
                this.toastr.warning('Không có dữ liệu để xử lý', this.translate.instant('ebwebApp.mCReceipt.error.error'));
                return;
            } else {
                this.modalRef = this.modalService.open(this.modalComponent, { backdrop: 'static' });
            }
        }
    }

    moveUp(): void {
        if (this.checkErrMove()) {
            this.hasChange = true;
            const index = this.viewVoucher.indexOf(this.selectedRow);
            if (index === 0) {
                return;
            }
            const up = this.viewVoucher[index - 1];
            const nonew1 = up.noNew;
            const nonew2 = this.selectedRow.noNew;
            this.viewVoucher[index - 1] = this.selectedRow;
            this.viewVoucher[index] = up;
            this.viewVoucher[index - 1].noNew = nonew1;
            this.viewVoucher[index].noNew = nonew2;
        }
    }

    moveDown(): void {
        if (this.checkErrMove()) {
            this.hasChange = true;
            const index = this.viewVoucher.indexOf(this.selectedRow);
            if (index === this.totalItems - 1) {
                return;
            }
            const down = this.viewVoucher[index + 1];
            const nonew1 = down.noNew;
            const nonew2 = this.selectedRow.noNew;
            this.viewVoucher[index + 1] = this.selectedRow;
            this.viewVoucher[index] = down;
            this.viewVoucher[index + 1].noNew = nonew1;
            this.viewVoucher[index].noNew = nonew2;
        }
    }

    trackId(index: number, item: ViewVoucherNo) {
        return item.id;
    }

    checkValidate() {
        return true;
    }

    checkErrMove() {
        if (!this.selectedRow) {
            this.toastr.error('Chưa chọn chứng từ', this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        return true;
    }

    setNoNew(): void {
        const count = Number((this.page - 1) * this.itemsPerPage);
        const lenght = this.currentValue.length;
        let i = Number(this.currentValue);
        this.viewVoucher.forEach(n => {
            n.noNew =
                (this.prefix ? this.prefix : '') + this.utilsService.pad(String(i + count), lenght) + (this.suffix ? this.suffix : '');
            i = i + 1;
        });
        this.setNoFromList();
    }

    setNoFromList() {
        for (let i = 0; i < this.viewVoucher.length; i++) {
            const noChange = this.viewVoucherChange.find(m => m.noNew === this.viewVoucher[i].noNew);
            if (noChange) {
                this.viewVoucher[i] = noChange;
            }
        }
        /*this.viewVoucher.forEach(n => {
            const noChange = this.viewVoucherChange.find(m => m.noNew === n.noNew);
            if (noChange) {
                n = noChange;
            }
        });*/
    }

    addListChange() {
        if (this.hasChange) {
            this.viewVoucherChange = this.viewVoucherChange.filter(n => this.viewVoucher.some(m => m.id !== n.id));
            this.viewVoucherChange.push(...this.viewVoucher);
        }
        this.hasChange = false;
    }

    getStyle(record?: boolean) {
        return !record
            ? {
                  input: { color: this.color },
                  color: this.color
              }
            : {};
    }

    continue() {
        this.toastr.warning(
            'Quá trình đánh lại số chứng từ có thể nhanh hoặc chậm phụ thuộc vào độ lớn của dữ liệu. Để tránh sai sót vui lòng đợi cho đến khi tiến trình hoàn tất!'
        );
        this.addListChange();
        const rq: RequestResetNoDtoModel = new RequestResetNoDtoModel();
        rq.typeGroupID = this.typeGroupID;
        rq.prefix = this.prefix;
        rq.suffix = this.suffix;
        rq.fromDate = this.fromDate ? this.fromDate.format(DATE_FORMAT) : '';
        rq.toDate = this.toDate ? this.toDate.format(DATE_FORMAT) : '';
        rq.currentValue = this.currentValue;
        rq.voucher = this.viewVoucherChange;
        this.viewVoucherService.resetNo(rq).subscribe((res: HttpResponse<IResponeSds>) => {
            if (res.body.status === 2) {
                this.viewVoucherChange = [];
                this.loadData(true, true);
                this.toastr.success('Đánh lại số chứng từ thành công');
            } else {
                this.toastr.error(res.body.message, this.translate.instant('ebwebApp.mCReceipt.error.error'));
            }
        });
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }
}
