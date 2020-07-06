import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { DatePipe } from '@angular/common';
import { getEmptyRow } from 'app/shared/util/row-util';
import { AccountingObject } from 'app/shared/model/accounting-object.model';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { ActivatedRoute, Router, RoutesRecognized } from '@angular/router';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { Principal } from 'app/core';
import { DomSanitizer } from '@angular/platform-browser';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { SaReturnService } from 'app/ban-hang/hang-ban-tra-lai-giam-gia/sa-return.service';
import { CurrencyService } from 'app/danhmuc/currency';
import { ICurrency } from 'app/shared/model/currency.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ISaReturn, SaReturn } from 'app/shared/model/sa-return.model';
import { SaReturnDetails } from 'app/shared/model/sa-return-details.model';
import { SaReturnDetailsService } from 'app/ban-hang/hang-ban-tra-lai-giam-gia/sa-return-details.service';
import { Irecord } from 'app/shared/model/record';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { MAU_BO_GHI_SO, MSGERROR, SO_LAM_VIEC, TCKHAC_SDTichHopHDDT, TypeID } from 'app/app.constants';
import { filter, pairwise } from 'rxjs/operators';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { ISAInvoice } from 'app/shared/model/sa-invoice.model';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { DataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';

@Component({
    selector: 'eb-hang-ban-tra-lai',
    templateUrl: './hang-ban-tra-lai.component.html',
    styleUrls: ['./hang-ban-tra-lai.css'],
    providers: [DatePipe]
})
export class HangBanTraLaiComponent extends BaseComponent implements OnInit {
    @ViewChild('unrecordContent') unrecordModal;
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    @ViewChild('deleteItem') deleteItem;
    hangBanTraLai: SaReturn[];
    hangBanTraLaiDetails: SaReturnDetails[];
    accountingObjects: AccountingObject[];
    series: any;
    searchVoucher: any;
    recorded: null;
    links: any;
    totalItems: any;
    queryCount: any;
    page: any;
    itemsPerPage = ITEMS_PER_PAGE;
    predicate: any;
    previousPage: any;
    reverse: any;
    routeData: any;
    dataSession: any;
    rowNum: number;
    index: number;
    selectedRow: any;
    eventSubscriber: Subscription;
    file: any;
    refVoucher: any;
    currencyCode: string;
    link: any;
    isSingleClick: any;
    currencies: ICurrency[];
    currentAccount: any;
    isLoading: boolean;
    isSoTaiChinh: boolean;
    pageCount: any;
    modalRef: NgbModalRef;
    HANG_BAN_GIAM_GIA = TypeID.HANG_GIAM_GIA;
    HANG_BAN_TRA_LAI = TypeID.HANG_BAN_TRA_LAI;
    typeMultiAction: number;
    listRecord: any;
    listColumnsRecord: string[] = ['name'];
    listHeaderColumnsRecord: string[] = ['Trạng thái'];
    typeDelete: number;
    isRequiredInvoiceNo: boolean;
    hiddenVAT: boolean;
    listVAT = [
        { name: '0%', data: 0 },
        { name: '5%', data: 1 },
        { name: '10%', data: 2 },
        { name: 'KCT', data: 3 },
        { name: 'KTT', data: 4 }
    ];

    ROLE_Them1 = ROLE.HangBanTraLai_Them;
    ROLE_GhiSo1 = ROLE.HangBanTraLai_GhiSo;
    ROLE_Xoa1 = ROLE.HangBanTraLai_Xoa;
    ROLE_KetXuat1 = ROLE.HangBanTraLai_KetXuat;

    ROLE_Them2 = ROLE.HangBanGiamGia_Them;
    ROLE_GhiSo2 = ROLE.HangBanGiamGia_GhiSo;
    ROLE_Xoa2 = ROLE.HangBanGiamGia_Xoa;
    ROLE_KetXuat2 = ROLE.HangBanGiamGia_KetXuat;

    constructor(
        private saReturnService: SaReturnService,
        private saReturnDetailService: SaReturnDetailsService,
        private activatedRoute: ActivatedRoute,
        private parseLinks: JhiParseLinks,
        private router: Router,
        private modalService: NgbModal,
        private eventManager: JhiEventManager,
        private sanitizer: DomSanitizer,
        private principal: Principal,
        private toastrService: ToastrService,
        private translateService: TranslateService,
        private currencyService: CurrencyService,
        private gLService: GeneralLedgerService,
        public utilsService: UtilsService,
        private refModalService: RefModalService,
        private accountingObjectService: AccountingObjectService,
        public activeModal: NgbActiveModal
    ) {
        super();
        this.selectedRow = {};
        this.searchVoucher = { page: 1, recorded: 2 };
        this.translateService.get(['ebwebApp.mBDeposit.home.recorded', 'ebwebApp.mBDeposit.home.unrecorded']).subscribe(res => {
            this.listRecord = [
                { value: 1, name: res['ebwebApp.mBDeposit.home.recorded'] },
                { value: 0, name: res['ebwebApp.mBDeposit.home.unrecorded'] }
            ];
        });
        this.router.events
            .pipe(filter((evt: any) => evt instanceof RoutesRecognized), pairwise())
            .subscribe((events: RoutesRecognized[]) => {
                if (events[0].urlAfterRedirects.includes('hang-ban')) {
                    this.searchVoucher = this.saReturnService.searchVoucher ? this.saReturnService.searchVoucher : this.searchVoucher;
                } else {
                    this.searchVoucher = { page: 1, recorded: 2 };
                }
            });
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.registerChangeInAccountDefaults();
    }

    toggleSearch($event) {
        $event.preventDefault();
        if (this.searchVoucher.typeID === 330) {
            this.searchVoucher.isShowSearch2 = !this.searchVoucher.isShowSearch2;
        } else {
            this.searchVoucher.isShowSearch1 = !this.searchVoucher.isShowSearch1;
        }
    }

    getEmptyRow(data) {
        return getEmptyRow(data);
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    ngOnInit(): void {
        this.searchVoucher.typeID = this.activatedRoute.routeConfig.path.includes('giam-gia')
            ? this.HANG_BAN_GIAM_GIA
            : this.HANG_BAN_TRA_LAI;
        this.accountingObjectService.getAllDTO({ isObjectType12: true }).subscribe(res => {
            this.accountingObjects = res.body;
        });

        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.currencyCode = account.organizationUnit.currencyID;
            this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
            this.color = this.currentAccount.systemOption.find(item => item.code === MAU_BO_GHI_SO).data;
            this.isRequiredInvoiceNo = this.currentAccount.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '0');
            this.hiddenVAT = this.currentAccount.organizationUnit.taxCalculationMethod === 1;
        });

        this.currencyService.findAllActive().subscribe(res => {
            this.currencies = res.body;
        });
        this.hangBanTraLaiDetails = [];
        this.hangBanTraLai = [];
        this.getSessionData();
        this.registerExport();
        this.registerLockSuccess();
        this.registerUnlockSuccess();
    }

    getSessionData() {
        if (this.searchVoucher.typeID === TypeID.HANG_BAN_TRA_LAI) {
            this.dataSession = JSON.parse(sessionStorage.getItem('dataSearchSaReturn1'));
        } else {
            this.dataSession = JSON.parse(sessionStorage.getItem('dataSearchSaReturn2'));
        }
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.totalItems = this.dataSession.totalItems;
            this.searchVoucher = JSON.parse(this.dataSession.searchVoucher);
            this.rowNum = this.dataSession.rowNum;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
        }
        if (this.searchVoucher.typeID === TypeID.HANG_BAN_TRA_LAI) {
            sessionStorage.removeItem('dataSearchSaReturn1');
        } else {
            sessionStorage.removeItem('dataSearchSaReturn2');
        }
        this.search();
    }

    doubleClickRow(item: any) {
        this.isSingleClick = false;
        this.saveSearchData(this.selectedRow);
        if (this.searchVoucher.typeID === TypeID.HANG_BAN_TRA_LAI) {
            this.router.navigate(['hang-ban/tra-lai', item.id, 'edit', this.rowNum]);
        } else {
            this.router.navigate(['hang-ban/giam-gia', item.id, 'edit', this.rowNum]);
        }
    }

    saveSearchData(item: any) {
        this.selectedRow = item;
        this.rowNum = item.rowIndex;
        this.dataSession = new DataSessionStorage();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.rowNum;
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        this.dataSession.searchVoucher = JSON.stringify(this.searchVoucher);
        if (this.searchVoucher.typeID === TypeID.HANG_BAN_TRA_LAI) {
            sessionStorage.setItem('dataSearchSaReturn1', JSON.stringify(this.dataSession));
        } else {
            sessionStorage.setItem('dataSearchSaReturn2', JSON.stringify(this.dataSession));
        }
    }

    edit() {
        event.preventDefault();
        this.doubleClickRow(this.selectedRow);
    }

    onSelect(object) {
        this.isSingleClick = true;
        setTimeout(() => {
            if (this.isSingleClick) {
                this.selectedRow = object;
                if (object) {
                    this.saReturnDetailService.find(object.id).subscribe(res => {
                        this.hangBanTraLaiDetails = res.body.saReturnDetailsViewDTOs;
                        this.refVoucher = res.body.refVoucherDTOS;
                    });
                }
            }
        }, 250);
    }

    search(index?) {
        this.isLoading = true;
        this.saReturnService.searchVoucher = this.searchVoucher;
        this.searchVoucher.recorded = this.recorded;
        this.saReturnService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                accountingObjectID: this.searchVoucher.accountingObjectID ? this.searchVoucher.accountingObjectID : '',
                fromDate: this.searchVoucher.fromDate ? this.searchVoucher.fromDate : '',
                toDate: this.searchVoucher.toDate ? this.searchVoucher.toDate : '',
                currencyID: this.searchVoucher.currencyID ? this.searchVoucher.currencyID : '',
                recorded: this.searchVoucher.recorded === 1 || this.searchVoucher.recorded === 0 ? this.searchVoucher.recorded : '',
                freeText: this.searchVoucher.freeText ? this.searchVoucher.freeText : '',
                typeID: this.searchVoucher.typeID
            })
            .subscribe(
                res => {
                    this.paginateSAReturn(res.body, res.headers);
                    this.saReturnService.total = res.body.length;
                    this.isLoading = false;
                    if (!this.selectedRow && res.body) {
                        this.selectMultiRow(this.hangBanTraLai[0], event, this.hangBanTraLai);
                    } else {
                        const select = res.body.find(n => n.id === this.selectedRow.id);
                        if (!select) {
                            this.selectMultiRow(this.hangBanTraLai[0], event, this.hangBanTraLai);
                        } else if (index) {
                            this.selectMultiRow(res.body[index], event, this.hangBanTraLai);
                        } else {
                            this.selectMultiRow(select, event, this.hangBanTraLai);
                        }
                    }
                },
                () => {
                    this.isLoading = false;
                }
            );
    }

    private paginateSAReturn(data: ISaReturn[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.hangBanTraLai = data;
        this.objects = data;
        this.hangBanTraLai.forEach(item => {
            if (this.isSoTaiChinh) {
                item.noMBook = '';
            } else {
                item.noFBook = '';
            }
        });
        if (this.hangBanTraLai && this.hangBanTraLai.length > 0) {
            if (this.rowNum && !this.index) {
                this.index = this.rowNum % this.itemsPerPage;
                this.index = this.index || this.itemsPerPage;
                this.selectedRow = this.hangBanTraLai[this.index - 1] ? this.hangBanTraLai[this.index - 1] : {};
                this.selectedRows.push(this.selectedRow);
            } else if (this.index) {
                this.selectedRow = this.hangBanTraLai[this.index - 1] ? this.hangBanTraLai[this.index - 1] : {};
                this.selectedRows.push(this.selectedRow);
            } else {
                this.selectedRows.push(this.hangBanTraLai[0]);
                this.selectedRow = this.hangBanTraLai[0];
            }
            this.rowNum = this.getRowNumberOfRecord(this.page, 0);
            this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
        } else {
            this.hangBanTraLaiDetails = [];
            this.refVoucher = [];
            this.selectedRow = {};
        }
        if (this.pageCount > 0 && (!this.hangBanTraLai || this.hangBanTraLai.length === 0) && this.page > 1) {
            this.page = this.page - 1;
            this.loadPage(this.page);
        }
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    loadPage(page) {
        if (this.page !== page) {
            this.page = page;
        } else {
            this.search();
        }
    }

    reset() {
        this.searchVoucher.freeText = '';
        this.searchVoucher.fromDate = '';
        this.searchVoucher.toDate = '';
        this.searchVoucher.accountingObject = '';
        this.searchVoucher.accountingObjectID = '';
        this.searchVoucher.recorded = null;
        this.searchVoucher.invoiceTemplate = '';
        this.searchVoucher.currencyID = null;
        this.searchVoucher.currencyID = '';
        this.searchVoucher.recorded = '';
        this.recorded = null;
        this.page = 1;
        this.search();
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    registerChangeInAccountDefaults() {
        this.eventSubscriber = this.eventManager.subscribe('hangBanTraLaiModification', response => {
            this.search();
        });
    }

    exportExcel() {
        this.isLoading = true;
        this.saReturnService.exportExcel().subscribe(
            res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                let name = 'DS_HangBanTraLai.xls';
                if (this.searchVoucher.typeID === TypeID.HANG_GIAM_GIA) {
                    name = 'DS_HangBanGiamGia.xls';
                }
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
                this.isLoading = false;
            },
            () => {
                this.isLoading = false;
            }
        );
    }

    @ebAuth(['ROLE_ADMIN', window.location.href.includes('tra-lai') ? ROLE.HangBanTraLai_GhiSo : ROLE.HangBanGiamGia_GhiSo])
    record() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 2;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (this.selectedRow && !this.selectedRow.recorded && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
                this.isLoading = true;
                const record_: Irecord = {};
                record_.id = this.selectedRow.id;
                record_.typeID = this.selectedRow.typeID;
                record_.repositoryLedgerID = this.selectedRow.rsInwardOutwardID;
                if (!this.selectedRow.recorded) {
                    this.gLService.record(record_).subscribe(
                        (res: HttpResponse<Irecord>) => {
                            this.isLoading = false;
                            if (res.body.success) {
                                this.selectedRow.recorded = true;
                                this.toastrService.success(
                                    this.translateService.instant('ebwebApp.mBTellerPaper.recordToast.done'),
                                    this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                                );
                            } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                                this.toastrService.error(
                                    this.translateService.instant('global.messages.error.checkTonQuyQT'),
                                    this.translateService.instant('ebwebApp.mCReceipt.error.error')
                                );
                            } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                                this.toastrService.error(
                                    this.translateService.instant('global.messages.error.checkTonQuyTC'),
                                    this.translateService.instant('ebwebApp.mCReceipt.error.error')
                                );
                            } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                                this.toastrService.error(
                                    this.translateService.instant('global.messages.error.checkTonQuy'),
                                    this.translateService.instant('ebwebApp.mCReceipt.error.error')
                                );
                            }
                        },
                        () => {
                            this.isLoading = false;
                        }
                    );
                }
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', window.location.href.includes('tra-lai') ? ROLE.HangBanTraLai_GhiSo : ROLE.HangBanGiamGia_GhiSo])
    unrecord() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 1;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (this.selectedRow && this.selectedRow.recorded && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
                const record_: Irecord = {};
                record_.id = this.selectedRow.id;
                record_.typeID = this.selectedRow.typeID;
                record_.repositoryLedgerID = this.selectedRow.rsInwardOutwardID;
                this.gLService.unrecord(record_).subscribe(
                    (res: HttpResponse<Irecord>) => {
                        if (res.body.success) {
                            this.selectedRow.recorded = false;
                            this.toastrService.success(
                                this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.done'),
                                this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                            );
                        }
                        this.isLoading = false;
                    },
                    error => {
                        if (error.error.errorKey === 'unrecord') {
                            this.modalRef = this.modalService.open(this.unrecordModal, { backdrop: 'static' });
                        }
                        this.isLoading = false;
                    }
                );
            }
        }
    }

    doUnrecord() {
        this.isLoading = true;
        const record_: Irecord = {};
        record_.id = this.selectedRow.id;
        record_.typeID = this.selectedRow.typeID;
        record_.repositoryLedgerID = this.selectedRow.rsInwardOutwardID;
        record_.answer = true;
        this.gLService.unrecord(record_).subscribe(
            (res: HttpResponse<Irecord>) => {
                if (res.body.success) {
                    this.selectedRow.recorded = false;
                    this.toastrService.success(
                        this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.done'),
                        this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                    );
                }
                this.modalRef.close();
                this.isLoading = false;
            },
            () => {
                this.isLoading = false;
            }
        );
    }

    exportPdf() {
        this.saReturnService.export().subscribe(res => {
            this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.searchVoucher.typeID);
        });
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.searchVoucher.typeID}`, () => {
            this.exportExcel();
        });
    }

    @ebAuth(['ROLE_ADMIN', window.location.href.includes('tra-lai') ? ROLE.HangBanTraLai_Them : ROLE.HangBanGiamGia_Them])
    addNew($event) {
        $event.preventDefault();
        if (this.searchVoucher.typeID === this.HANG_BAN_GIAM_GIA) {
            this.router.navigate(['/hang-ban/giam-gia/new']);
        } else if (this.searchVoucher.typeID === this.HANG_BAN_TRA_LAI) {
            this.router.navigate(['/hang-ban/tra-lai/new']);
        }
    }

    @ebAuth(['ROLE_ADMIN', window.location.href.includes('tra-lai') ? ROLE.HangBanTraLai_Xoa : ROLE.HangBanGiamGia_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 0;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (this.selectedRow && !this.selectedRow.recorded && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
                if (this.selectedRow.invoiceForm === 0 && !this.isRequiredInvoiceNo) {
                    if (this.selectedRow.invoiceNo) {
                        this.typeDelete = 2;
                    } else {
                        this.typeDelete = 0;
                    }
                } else if (this.selectedRow.invoiceForm) {
                    this.typeDelete = 1;
                } else {
                    this.typeDelete = 0;
                }
                this.modalRef = this.modalService.open(this.deleteItem, { size: 'lg', backdrop: 'static' });
            }
        }
    }

    deleteForm() {
        if (this.selectedRow) {
            if (this.typeDelete === 2) {
                this.toastrService.error(this.translateService.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
            } else {
                const index = this.hangBanTraLai.indexOf(this.selectedRow);
                this.saReturnService.delete(this.selectedRow.id).subscribe(
                    ref => {
                        this.toastrService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));

                        this.search(index);
                        this.modalRef.close();
                    },
                    ref => {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.error'));
                    }
                );
            }
        }
    }

    continueMultiAction() {
        this.selectedRows.forEach(item => {
            item.date = item.date.format(DATE_FORMAT);
            item.postedDate = item.postedDate.format(DATE_FORMAT);
        });
        if (this.typeMultiAction === 0) {
            this.saReturnService.multiDelete(this.selectedRows).subscribe(
                (res: HttpResponse<any>) => {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    this.selectedRows = [];
                    if (res.body.countTotalVouchers !== res.body.countSuccessVouchers) {
                        this.modalRef = this.refModalService.open(
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
                    } else {
                        this.toastrService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
                    }
                    this.activeModal.close();
                    this.search();
                },
                (res: HttpErrorResponse) => {
                    if (res.error.errorKey === 'errorDeleteList') {
                        this.toastrService.error(
                            this.translateService.instant('ebwebApp.mBDeposit.errorDeleteVoucherNo'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                }
            );
        } else if (this.typeMultiAction === 1) {
            this.saReturnService.multiUnrecord(this.selectedRows).subscribe(
                (res: HttpResponse<any>) => {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    this.selectedRows = [];
                    if (res.body.countTotalVouchers !== res.body.countSuccessVouchers) {
                        this.modalRef = this.refModalService.open(
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
                    } else {
                        this.toastrService.success(
                            this.translateService.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    this.activeModal.close();
                    this.search();
                },
                (res: HttpErrorResponse) => {
                    this.toastrService.error(
                        this.translateService.instant('global.data.unRecordFailed'),
                        this.translateService.instant('ebwebApp.mBDeposit.message')
                    );
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                }
            );
        } else if (this.typeMultiAction === 2) {
            const listRecord: RequestRecordListDtoModel = {};
            listRecord.typeIDMain = TypeID.HANG_BAN_TRA_LAI;
            listRecord.records = [];
            this.selectedRows.forEach(item => {
                listRecord.records.push({
                    id: item.id,
                    typeID: item.typeID
                });
            });
            this.gLService.recordList(listRecord).subscribe(
                (res: HttpResponse<HandlingResult>) => {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    this.selectedRows = [];
                    if (res.body.listFail && res.body.listFail.length > 0) {
                        this.modalRef = this.refModalService.open(
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
                    } else {
                        this.toastrService.success(
                            this.translateService.instant('ebwebApp.mBCreditCard.recordSuccess'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    this.activeModal.close();
                    this.search();
                },
                (res: HttpErrorResponse) => {
                    this.toastrService.error(
                        this.translateService.instant('global.data.recordFailed'),
                        this.translateService.instant('ebwebApp.mBDeposit.message')
                    );
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                }
            );
        }
    }

    closePopUp() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    @ebAuth(['ROLE_ADMIN', window.location.href.includes('tra-lai') ? ROLE.HangBanTraLai_KetXuat : ROLE.HangBanGiamGia_KetXuat])
    export() {
        event.preventDefault();
        this.exportPdf();
    }

    print($event) {
        $event.preventDefault();
        this.exportExcel();
    }

    getVATRate(vatRate) {
        if (this.listVAT && (vatRate || vatRate === 0)) {
            const acc = this.listVAT.find(n => n.data === vatRate);
            if (acc) {
                return acc.name;
            }
        }
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.hangBanTraLaiDetails.length; i++) {
            total += this.hangBanTraLaiDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    registerLockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('lockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
            this.search();
        });
    }

    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
            this.search();
        });
    }

    checkMultiButton(isRecord: boolean) {
        if (this.selectedRows) {
            for (let i = 0; i < this.selectedRows.length; i++) {
                if (this.selectedRows[i] && this.selectedRows[i].recorded === isRecord) {
                    return true;
                }
            }
        }
        return false;
    }
}
