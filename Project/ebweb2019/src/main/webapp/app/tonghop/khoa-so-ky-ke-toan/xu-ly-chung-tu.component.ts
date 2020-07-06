import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { JhiEventManager } from 'ng-jhipster';
import { ViewVoucherNo } from 'app/tonghop/khoa-so-ky-ke-toan/view-voucher-no.model';
import { Router } from '@angular/router';
import { IType } from 'app/shared/model/type.model';
import { TypeService } from 'app/entities/type';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { XuLyChungTuService } from 'app/tonghop/khoa-so-ky-ke-toan/xu-ly-chung-tu.service';
import * as moment from 'moment';
import { Moment } from 'moment';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ClOSE_BOOK, SO_LAM_VIEC } from 'app/app.constants';
import { CloseBook } from 'app/tonghop/khoa-so-ky-ke-toan/close-book-request.model';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';

@Component({
    selector: 'eb-xu-ly-chung-tu',
    templateUrl: 'xu-ly-chung-tu.component.html',
    styleUrls: ['./xu-ly-chung-tu.component.css']
})
export class XuLyChungTuComponent implements OnInit {
    @ViewChild('contentPostedDate') public modalContent: NgbModalRef;
    modalData: any;
    data: any;
    viewVoucher: ViewVoucherNo[];
    selectedRow: ViewVoucherNo;
    newList: ViewVoucherNo[];
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    types: IType[];
    listFunction: any[];
    selectFuntionDefault: number;
    postedDate: Moment;
    dataDiffPostedDate: ViewVoucherNo[];
    modalRef: NgbModalRef;
    modalRefMess: NgbModalRef;
    closeBook: CloseBook;
    DATE_FORMAT = DATE_FORMAT;
    account: any;
    isSoTaiChinh: boolean;
    lstBranch: any;

    constructor(
        public activeModal: NgbActiveModal,
        private principal: Principal,
        private toastr: ToastrService,
        public translate: TranslateService,
        private typeService: TypeService,
        public eventManager: JhiEventManager,
        private router: Router,
        private xuLyChungTuService: XuLyChungTuService,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private refModalService: RefModalService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        /*this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body;
        });*/
        this.principal.identity().then(account => {
            this.account = account;
            this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
        });
        this.listFunction = [
            { value: ClOSE_BOOK.RECORD, name: this.translate.instant('ebwebApp.lockBook.function.record') },
            {
                value: ClOSE_BOOK.CHANG_POSTEDDATE,
                name: this.translate.instant('ebwebApp.lockBook.function.changePostedDate')
            },
            { value: ClOSE_BOOK.DELETE, name: this.translate.instant('ebwebApp.lockBook.function.delete') }
        ];
    }

    ngOnInit(): void {
        this.selectFuntionDefault = ClOSE_BOOK.RECORD;
        this.modalData.body.forEach(n => {
            n.checked1 = true;
        });
        this.viewVoucher = this.modalData.body ? this.modalData.body : [];
        this.newList = [];
        this.dataDiffPostedDate = [];
        this.page = 1;
        this.previousPage = 1;
        this.selectedRow = this.modalData.body ? this.modalData.body[0] : {};
        this.totalItems = parseInt(this.modalData.headers.get('X-Total-Count'), 10);
        const dataCopy: Moment = moment(this.data.postedDate.format(DATE_FORMAT), DATE_FORMAT);
        this.postedDate = dataCopy.add(1, 'days');
    }

    loadAll() {
        this.xuLyChungTuService
            .getAllVoucherNotRecorded({
                page: this.page - 1,
                size: this.itemsPerPage,
                postedDate: this.data.postedDate.format(DATE_FORMAT)
            })
            .subscribe(
                (res: HttpResponse<ViewVoucherNo[]>) => {
                    this.paginateViewVoucher(res.body, res.headers);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    private paginateViewVoucher(data: ViewVoucherNo[], headers: HttpHeaders) {
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        data.forEach(n => {
            const viewNo = this.newList.find(m => m.refID === n.refID);
            const viewNoDate = this.dataDiffPostedDate.find(m => m.refID === n.refID);
            if (viewNoDate) {
                n.postedDateChange = viewNoDate.postedDateChange;
            } else {
                if (this.selectFuntionDefault === ClOSE_BOOK.CHANG_POSTEDDATE) {
                    if (!viewNo) {
                        n.postedDateChange = this.postedDate;
                    } else {
                        n.postedDateChange = null;
                    }
                } else {
                    if (viewNo) {
                        n.postedDateChange = viewNo.postedDateChange;
                    } else {
                        n.postedDateChange = null;
                    }
                }
            }
            if (viewNo) {
                n.checked1 = viewNo.checked1;
                n.checked2 = viewNo.checked2;
                n.checked3 = viewNo.checked3;
            } else {
                if (this.selectFuntionDefault === ClOSE_BOOK.RECORD) {
                    n.checked1 = true;
                } else if (this.selectFuntionDefault === ClOSE_BOOK.CHANG_POSTEDDATE) {
                    n.checked2 = true;
                } else {
                    n.checked3 = true;
                }
            }
        });
        this.viewVoucher = data;
        this.selectedRow = data ? data[0] : {};
    }

    private onError(errorMessage) {
        this.toastr.error(errorMessage, this.translate.instant('ebwebApp.mCReceipt.error.error'));
    }

    isCheckAll1(): boolean {
        return this.viewVoucher.every(item => item.checked1) && this.viewVoucher.length && this.newList.length === 0;
    }

    isCheckAll2(): boolean {
        return this.viewVoucher.every(item => item.checked2) && this.viewVoucher.length && this.newList.length === 0;
    }

    isCheckAll3(): boolean {
        return this.viewVoucher.every(item => item.checked3) && this.viewVoucher.length && this.newList.length === 0;
    }

    checkAll(type) {
        switch (type) {
            case ClOSE_BOOK.RECORD:
                this.selectFuntionDefault = ClOSE_BOOK.RECORD;
                this.dataDiffPostedDate = [];
                const isCheck1 = this.viewVoucher.every(item => item.checked1) && this.viewVoucher.length;
                this.viewVoucher.forEach(item => {
                    item.checked1 = !isCheck1;
                    item.checked2 = false;
                    item.checked3 = false;
                    item.postedDateChange = null;
                });

                if (!isCheck1) {
                    this.newList = [];
                } else {
                    for (let j = 0; j < this.viewVoucher.length; j++) {
                        for (let i = 0; i < this.newList.length; i++) {
                            if (this.newList[i].id === this.viewVoucher[j].id) {
                                this.newList.splice(i, 1);
                                i--;
                            }
                        }
                    }
                }
                break;
            case ClOSE_BOOK.CHANG_POSTEDDATE:
                this.selectFuntionDefault = ClOSE_BOOK.CHANG_POSTEDDATE;
                const isCheck2 = this.viewVoucher.every(item => item.checked2) && this.viewVoucher.length;
                this.viewVoucher.forEach(item => {
                    item.checked2 = !isCheck2;
                    item.checked1 = false;
                    item.checked3 = false;
                    item.postedDateChange = this.postedDate;
                });
                if (!isCheck2) {
                    this.newList = [];
                } else {
                    for (let j = 0; j < this.viewVoucher.length; j++) {
                        for (let i = 0; i < this.newList.length; i++) {
                            if (this.newList[i].id === this.viewVoucher[j].id) {
                                this.newList.splice(i, 1);
                                i--;
                            }
                        }
                    }
                }
                break;
            case ClOSE_BOOK.DELETE:
                this.dataDiffPostedDate = [];
                this.selectFuntionDefault = ClOSE_BOOK.DELETE;
                const isCheck3 = this.viewVoucher.every(item => item.checked3) && this.viewVoucher.length;
                this.viewVoucher.forEach(item => {
                    item.checked3 = !isCheck3;
                    item.checked1 = false;
                    item.checked2 = false;
                    item.postedDateChange = null;
                });

                if (!isCheck3) {
                    this.newList = [];
                } else {
                    for (let j = 0; j < this.viewVoucher.length; j++) {
                        for (let i = 0; i < this.newList.length; i++) {
                            if (this.newList[i].refID === this.viewVoucher[j].refID) {
                                this.newList.splice(i, 1);
                                i--;
                            }
                        }
                    }
                }
                break;
        }
    }

    doubleClickRow(id) {}

    onSelect(detial) {
        if (this.selectedRow.id === detial.id) {
            return;
        }
        this.selectedRow = detial;
    }

    check(viewVoucherNo: ViewVoucherNo, type) {
        this.remove(this.newList, viewVoucherNo);
        this.remove(this.dataDiffPostedDate, viewVoucherNo);
        switch (type) {
            case ClOSE_BOOK.RECORD:
                viewVoucherNo.checked1 = !viewVoucherNo.checked1;
                viewVoucherNo.checked2 = false;
                viewVoucherNo.checked3 = false;
                viewVoucherNo.postedDateChange = null;
                break;
            case ClOSE_BOOK.CHANG_POSTEDDATE:
                if (!viewVoucherNo.postedDateChange) {
                    viewVoucherNo.postedDateChange = this.postedDate;
                }
                viewVoucherNo.checked2 = !viewVoucherNo.checked2;
                viewVoucherNo.checked1 = false;
                viewVoucherNo.checked3 = false;
                break;
            case ClOSE_BOOK.DELETE:
                viewVoucherNo.checked3 = !viewVoucherNo.checked3;
                viewVoucherNo.checked1 = false;
                viewVoucherNo.checked2 = false;
                viewVoucherNo.postedDateChange = null;
                break;
        }
        switch (this.selectFuntionDefault) {
            case ClOSE_BOOK.RECORD:
                if (type === ClOSE_BOOK.RECORD) {
                    this.remove(this.newList, viewVoucherNo);
                } else if (type === ClOSE_BOOK.CHANG_POSTEDDATE) {
                    this.newList.push(viewVoucherNo);
                } else {
                    this.newList.push(viewVoucherNo);
                }
                break;
            case ClOSE_BOOK.CHANG_POSTEDDATE:
                if (type === ClOSE_BOOK.RECORD) {
                    this.newList.push(viewVoucherNo);
                } else if (type === ClOSE_BOOK.CHANG_POSTEDDATE) {
                    this.remove(this.newList, viewVoucherNo);
                } else {
                    this.newList.push(viewVoucherNo);
                }
                break;
            case ClOSE_BOOK.DELETE:
                if (type === ClOSE_BOOK.RECORD) {
                    this.newList.push(viewVoucherNo);
                } else if (type === ClOSE_BOOK.CHANG_POSTEDDATE) {
                    this.newList.push(viewVoucherNo);
                } else {
                    this.remove(this.newList, viewVoucherNo);
                }
                break;
        }
        if (this.newList.filter(n => n.checked1).length === this.totalItems) {
            this.newList = [];
            this.selectFuntionDefault = ClOSE_BOOK.RECORD;
        } else if (this.newList.filter(n => n.checked2).length === this.totalItems) {
            this.selectFuntionDefault = ClOSE_BOOK.CHANG_POSTEDDATE;
            this.dataDiffPostedDate = [];
            this.newList
                .filter(n => n.checked2 && n.postedDateChange.format(DATE_FORMAT) !== this.postedDate.format(DATE_FORMAT))
                .forEach(n => {
                    this.dataDiffPostedDate.push(n);
                });
            this.newList = [];
        } else if (this.newList.filter(n => n.checked3).length === this.totalItems) {
            this.newList = [];
            this.selectFuntionDefault = ClOSE_BOOK.DELETE;
        }
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.loadAll();
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    getTypeByTypeID(typeID: number) {
        if (this.types) {
            const type = this.types.find(n => n.id === typeID);
            if (type !== undefined && type !== null) {
                return type.typeName;
            } else {
                return '';
            }
        }
    }

    private onSuccess() {
        this.toastr.success(
            this.translate.instant('ebwebApp.lockBook.lockSuccess'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
        this.principal.identity(true).then(account => {
            this.eventManager.broadcast({
                name: 'lockSuccess'
            });
        });
        this.activeModal.close();
    }

    selectFuntion(type) {
        this.selectFuntionDefault = type;
        this.newList = [];
        this.viewVoucher.forEach(n => {
            n.choseFuntion = type;
        });
    }

    changeFuntion(detail: ViewVoucherNo) {
        if (this.newList.find(n => n.refID === detail.refID)) {
            this.newList.splice(this.newList.indexOf(detail), 1);
        }
        if (detail.choseFuntion !== this.selectFuntionDefault) {
            this.newList.push(detail);
        }
    }

    remove(list, item) {
        const deleteRef = list.find(n => n.refID === item.refID);
        if (deleteRef) {
            list.splice(list.indexOf(deleteRef), 1);
        }
    }

    count(type) {
        switch (type) {
            case ClOSE_BOOK.RECORD:
                if (this.selectFuntionDefault === ClOSE_BOOK.RECORD) {
                    return this.totalItems - this.newList.length;
                } else {
                    return this.newList.filter(n => n.checked1).length;
                }
            case ClOSE_BOOK.CHANG_POSTEDDATE:
                if (this.selectFuntionDefault === ClOSE_BOOK.CHANG_POSTEDDATE) {
                    return this.totalItems - this.newList.length;
                } else {
                    return this.newList.filter(n => n.checked2).length;
                }
            case ClOSE_BOOK.DELETE:
                if (this.selectFuntionDefault === ClOSE_BOOK.DELETE) {
                    return this.totalItems - this.newList.length;
                } else {
                    return this.newList.filter(n => n.checked3).length;
                }
        }
    }

    checkErr() {
        if (this.newList.find(n => n.checked2 && n.postedDateChange === null)) {
            this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        } else if (
            this.selectFuntionDefault === ClOSE_BOOK.CHANG_POSTEDDATE &&
            this.dataDiffPostedDate.find(n => n.checked2 && n.postedDateChange === null)
        ) {
            this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        } else if (
            this.newList.length > 0 &&
            this.newList
                .filter(n => n.postedDateChange)
                .some(n => this.getDateFromMoment(n.postedDateChange) <= this.getDateFromMoment(this.data.postedDate))
        ) {
            this.toastr.error(
                this.translate.instant('ebwebApp.lockBook.err.sameDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        } else if (
            this.dataDiffPostedDate.length > 0 &&
            this.dataDiffPostedDate
                .filter(n => n.postedDateChange)
                .some(n => this.getDateFromMoment(n.postedDateChange) <= this.getDateFromMoment(this.data.postedDate))
        ) {
            this.toastr.error(
                this.translate.instant('ebwebApp.lockBook.err.sameDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        return true;
    }

    getDateFromMoment(date: Moment) {
        return moment(date.format(DATE_FORMAT), DATE_FORMAT);
    }

    selectPostedDate() {
        if (!this.postedDate) {
            this.errMess(this.translate.instant('global.data.null'));
            return;
        }
        this.viewVoucher.forEach(n => {
            n.postedDateChange = this.postedDate;
        });
        this.modalRef.close();
    }

    errMess(mess) {
        this.toastr.error(mess, this.translate.instant('ebwebApp.mCReceipt.error.error'));
    }

    closeChosePostedDate() {
        this.modalRef.close();
        this.checkAll(ClOSE_BOOK.RECORD);
    }

    changePostedDate(detail: ViewVoucherNo) {
        this.remove(this.dataDiffPostedDate, detail);
        if (detail.postedDateChange && detail.postedDateChange.format(DATE_FORMAT) !== this.postedDate.format(DATE_FORMAT)) {
            this.dataDiffPostedDate.push(detail);
        }
    }

    accept() {
        if (this.checkErr()) {
            const closeBook: CloseBook = {};
            closeBook.postedDate = this.data.postedDate ? this.data.postedDate.format(DATE_FORMAT) : null;
            closeBook.postedDateNew = this.postedDate ? this.postedDate.format(DATE_FORMAT) : null;
            closeBook.listDataChangeDiff = this.newList;
            closeBook.listChangePostedDateDiff = this.dataDiffPostedDate;
            closeBook.choseFuntion = this.selectFuntionDefault;
            closeBook.lstBranch = this.lstBranch;
            this.xuLyChungTuService.closeBook(closeBook).subscribe((res: HttpResponse<HandlingResult>) => {
                if (res.body.countFailVouchers > 0) {
                    this.modalRefMess = this.refModalService.open(
                        res.body,
                        HandlingResultComponent,
                        null,
                        false,
                        null,
                        null,
                        null,
                        null,
                        null,
                        true
                    );
                    this.activeModal.close();
                } else {
                    this.onSuccess();
                }
            });
        }
    }
}
