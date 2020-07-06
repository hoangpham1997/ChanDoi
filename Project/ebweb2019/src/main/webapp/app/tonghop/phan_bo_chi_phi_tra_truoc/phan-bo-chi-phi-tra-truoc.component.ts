import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { PhanBoChiPhiTraTruocService } from 'app/tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ChiPhiTraTruocService } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc.service';
import { DDSo_NgoaiTe, DDSo_TienVND, MAU_BO_GHI_SO, TypeID } from 'app/app.constants';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { Subscription } from 'rxjs';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { PrepaidExpenseService } from 'app/entities/prepaid-expense';
import * as moment from 'moment';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { GOtherVoucherService } from 'app/tonghop/chung_tu_nghiep_vu_khac';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-phan-bo-chi-phi-tra-truoc',
    templateUrl: './phan-bo-chi-phi-tra-truoc.component.html',
    styleUrls: ['./phan-bo-chi-phi-tra-truoc.component.css']
})
export class PhanBoChiPhiTraTruocComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('contentAddNew') public modalContentAddNew: NgbModalRef;
    @ViewChild('deleteModal') public deleteModal: NgbModalRef;
    @ViewChild('contentAddNew') public contentAddNewModal: NgbModalRef;
    @ViewChild('popUpMultiRecord') popUpMultiRecord: TemplateRef<any>;
    currentAccount: any;
    gOtherVouchers: IGOtherVoucher[];
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    links: any;
    previousPage: any;
    modalRef: NgbModalRef;
    // navigate update form
    dataSession: IDataSessionStorage = new class implements IDataSessionStorage {
        accountingObjectName: string;
        itemsPerPage: number;
        page: number;
        pageCount: number;
        predicate: number;
        reverse: number;
        rowNum: number;
        totalItems: number;
        isEdit: boolean;
    }();
    routeData: any;
    reverse: boolean;
    objSearch: ISearchVoucher;
    isShowSearch: boolean;
    selectedRow: any;
    selectedYear: any;
    selectedMonth: any;
    pageCount: number;
    index: number;
    gOtherVoucherDetails: any;
    rowNum: number;
    gOtherVoucherDetailExpenseAllocations: any[];
    gOtherVoucherDetailExpenses: any[];
    private prepaidExpenseCodeList: any[];
    record_: any;
    viewVouchersSelected: any;
    eventSubscriber: Subscription;
    sumAmount: number;
    sumRemainingAmount: number;
    sumAllocationAmount: number;
    sumAllocationAmountDetail: any;
    sumAmountDetail: number;
    sumAccountingAmountDetail: number;
    DDSo_NgoaiTe: any;
    DDSo_TienVND: any;
    ROLE_Xem = ROLE.PhanBoChiPhiTRaTruoc_Xem;
    ROLE_Them = ROLE.PhanBoChiPhiTRaTruoc_Them;
    ROLE_Sua = ROLE.PhanBoChiPhiTRaTruoc_Sua;
    ROLE_Xoa = ROLE.PhanBoChiPhiTRaTruoc_Xoa;
    ROLE_GhiSo = ROLE.PhanBoChiPhiTRaTruoc_GhiSo;
    ROLE_KetXuat = ROLE.PhanBoChiPhiTRaTruoc_KetXuat;
    monthArr: any[];
    typeMultiAction: number;

    constructor(
        private translate: TranslateService,
        private toastr: ToastrService,
        private prepaidExpenseService: PrepaidExpenseService,
        private eventManager: JhiEventManager,
        private phanBoChiPhiTraTruocService: PhanBoChiPhiTraTruocService,
        private gOtherVoucherServices: GOtherVoucherService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private gLService: GeneralLedgerService,
        private chiPhiTraTruocService: ChiPhiTraTruocService,
        public utilsService: UtilsService,
        private refModalService: RefModalService,
        private modalService: NgbModal
    ) {
        super();
        this.DDSo_NgoaiTe = DDSo_NgoaiTe;
        this.DDSo_TienVND = DDSo_TienVND;
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
            for (let i = 0; i < this.currentAccount.systemOption.length; i++) {
                if (!this.currentAccount.systemOption[i].data) {
                    this.currentAccount.systemOption[i].data = this.currentAccount.systemOption[i].defaultData;
                }
            }
            this.color = this.currentAccount.systemOption.find(item => item.code === MAU_BO_GHI_SO).data;
        });
    }

    ngOnInit() {
        this.monthArr = [];
        for (let i = 0; i < 12; i++) {
            this.monthArr.push({ month: i + 1 });
        }
        this.registerLockSuccess();
        this.registerUnlockSuccess();
        this.registerExport();
        this.clearSearch();
        this.getPrepaidExpenseCode();
        this.selectedYear = new Date().getFullYear();
        this.selectedMonth = new Date().getMonth() + 1;
        this.dataSession = new class implements IDataSessionStorage {
            creditCardType: string;
            itemsPerPage: number;
            ownerCard: string;
            page: number;
            pageCount: number;
            predicate: number;
            reverse: number;
            rowNum: number;
            searchVoucher: string;
            totalItems: number;
        }();
    }

    ngOnDestroy() {
        // this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IGOtherVoucher) {
        return item.id;
    }

    getPrepaidExpenseCode() {
        this.chiPhiTraTruocService.getPrepaidExpenseCode().subscribe(ref => {
            this.prepaidExpenseCodeList = ref.body;
        });
    }

    sort() {
        // const result = ['date' + ',' + (this.reverse ? 'desc' : 'asc')];
        // // result.push('postedDate' + ',' + (this.reverse ? 'asc' : 'desc'));
        // result.push('postedDate' + ',' + (this.reverse ? 'desc' : 'asc'));
        // if (this.isSoTaiChinh) {
        //     result.push('noFBook' + ',' + (this.reverse ? 'desc' : 'asc'));
        // } else {
        //     result.push('noMBook' + ',' + (this.reverse ? 'desc' : 'asc'));
        // }
        // // const result = ['date, desc', 'postedDate, desc'];
        // return result;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    search() {
        this.getSessionData();
        if (this.objSearch.fromDate && this.objSearch.toDate) {
            if (moment(this.objSearch.toDate) < moment(this.objSearch.fromDate)) {
                this.toastr.error(this.translate.instant('ebwebApp.mBDeposit.fromDateMustBeLessThanToDate'));
                return false;
            }
        }
        this.phanBoChiPhiTraTruocService
            .searchAllPB({
                page: this.page - 1,
                size: this.itemsPerPage,
                fromDate: this.objSearch.fromDate ? this.objSearch.fromDate : '',
                toDate: this.objSearch.toDate ? this.objSearch.toDate : '',
                textSearch: this.objSearch.textSearch ? this.objSearch.textSearch : ''
            })
            .subscribe(res => {
                this.links = this.parseLinks.parse(res.headers.get('link'));
                this.totalItems = parseInt(res.headers.get('X-Total-Count'), 10);
                this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
                this.previousPage = this.page;
                this.gOtherVouchers = res.body;
                this.objects = res.body;
                if (res.body.length > 0) {
                    if (JSON.parse(sessionStorage.getItem('itemGOtherVoucher'))) {
                        const itemID = JSON.parse(sessionStorage.getItem('itemGOtherVoucher'));
                        sessionStorage.removeItem('itemGOtherVoucher');
                        this.selectedRow = this.gOtherVouchers.find(item => item.id === itemID);
                        if (!this.selectedRow) {
                            // this.selectedRow = this.gOtherVouchers[0];
                            this.onSelect(this.gOtherVouchers[0]);
                        } else {
                            this.onSelect(this.selectedRow);
                        }
                    } else {
                        // this.selectedRow = this.gOtherVouchers[0];
                        this.onSelect(this.gOtherVouchers[0]);
                    }
                    // if (this.selectedRow) {
                    //     this.onSelect(this.selectedRow);
                    // } else {
                    //     this.gOtherVoucherDetailExpenses = [];
                    //     this.gOtherVoucherDetailExpenseAllocations = [];
                    //     this.gOtherVoucherDetails = [];
                    //     this.viewVouchersSelected = [];
                    // }
                } else {
                    this.gOtherVoucherDetailExpenses = [];
                    this.gOtherVoucherDetailExpenseAllocations = [];
                    this.gOtherVoucherDetails = [];
                    this.viewVouchersSelected = [];
                }
            });
        sessionStorage.setItem('dataSearchGOtherVoucher', JSON.stringify(this.objSearch));
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_KetXuat])
    export() {
        event.preventDefault();
        if (!(!this.gOtherVouchers || this.gOtherVouchers.length === 0 || !this.selectedRow)) {
            this.phanBoChiPhiTraTruocService
                .exportPDFPB({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    fromDate: this.objSearch.fromDate ? this.objSearch.fromDate : '',
                    toDate: this.objSearch.toDate ? this.objSearch.toDate : '',
                    textSearch: this.objSearch.textSearch ? this.objSearch.textSearch : ''
                })
                .subscribe(res => {
                    this.refModalService.open(null, EbReportPdfPopupComponent, res, false, TypeID.PHAN_BO_CHI_PHI_TRA_TRUOC);
                });
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_GhiSo])
    record() {
        event.preventDefault();
        if (
            !(
                !this.gOtherVouchers ||
                this.gOtherVouchers.length === 0 ||
                !this.selectedRow ||
                (this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate) && this.selectedRows.length <= 1)
            )
        ) {
            if (this.selectedRows && this.selectedRows.length > 1) {
                this.typeMultiAction = 0;
                this.modalRef = this.modalService.open(this.popUpMultiRecord, { backdrop: 'static' });
                return;
            } else {
                if (this.selectedRow && !this.selectedRow.recorded) {
                    if (this.selectedRow.id) {
                        this.record_ = {};
                        this.record_.id = this.selectedRow.id;
                        this.record_.typeID = TypeID.PHAN_BO_CHI_PHI_TRA_TRUOC;
                        if (!this.selectedRow.recorded) {
                            this.gLService.record(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                                if (res.body.success) {
                                    this.selectedRow.recorded = true;
                                    this.toastr.success(
                                        this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
                                        this.translate.instant('ebwebApp.mBCreditCard.message')
                                    );
                                }
                            });
                        }
                    }
                }
                // else {
                //     this.toastr.warning(
                //         this.translate.instant('global.data.warningRecorded'),
                //         this.translate.instant('ebwebApp.mBDeposit.message')
                //     );
                // }
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_GhiSo])
    unrecord() {
        event.preventDefault();
        if (
            !(
                !this.gOtherVouchers ||
                this.gOtherVouchers.length === 0 ||
                !this.selectedRow ||
                (this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate) && this.selectedRows.length <= 1)
            )
        ) {
            if (this.selectedRows && this.selectedRows.length > 1) {
                this.typeMultiAction = 1;
                this.modalRef = this.modalService.open(this.popUpMultiRecord, { backdrop: 'static' });
                return;
            } else {
                if (
                    !(this.gOtherVoucherDetails && this.gOtherVoucherDetails.length < 1) ||
                    this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)
                ) {
                    if (this.selectedRow.id) {
                        this.record_ = {};
                        this.record_.id = this.selectedRow.id;
                        this.record_.typeID = TypeID.PHAN_BO_CHI_PHI_TRA_TRUOC;
                        this.gLService.unrecord(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                            if (res.body.success) {
                                this.selectedRow.recorded = false;
                                this.toastr.success(
                                    this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                                    this.translate.instant('ebwebApp.mBCreditCard.message')
                                );
                            }
                        });
                    }
                }
                // else {
                //     this.toastr.warning(
                //         this.translate.instant('global.data.warningUnRecorded'),
                //         this.translate.instant('ebwebApp.mBDeposit.message')
                //     );
                // }
            }
        }
    }

    searchAfterChangeRecord() {}

    doubleClickRow(id: string, check?: boolean) {
        if (id) {
            if (!check) {
                this.rowNum = this.getRowNumberOfRecord(this.page, this.index);
            } else {
                this.getRowNumByID(id);
            }
            this.dataSession.page = this.page;
            this.dataSession.itemsPerPage = this.itemsPerPage;
            this.dataSession.pageCount = this.pageCount;
            this.dataSession.totalItems = this.totalItems;
            this.dataSession.rowNum = this.rowNum;
            this.dataSession.isShowSearch = this.isShowSearch;
            sessionStorage.setItem('objectSearch', JSON.stringify(this.objSearch));
            sessionStorage.setItem('itemGOtherVoucher', JSON.stringify(id));
            sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
            this.router.navigate(['./phan-bo-chi-phi-tra-truoc', id, 'edit']);
        }
    }

    deleteAfter() {
        if (this.selectedRows && this.selectedRows.length > 1) {
            this.phanBoChiPhiTraTruocService.multiDeletePB(this.selectedRows).subscribe(
                res => {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    this.selectedRows = [];
                    this.searchAfterChangeRecord();
                    this.search();
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
                        this.toastr.success(this.translate.instant('ebwebApp.pPDiscountReturn.delete.success'));
                    }
                },
                (res: HttpErrorResponse) => {
                    if (res.error.errorKey === 'errorDeleteList') {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.mBDeposit.errorDeleteVoucherNo'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                }
            );
        } else {
            if (this.selectedRow && this.selectedRow.id) {
                this.phanBoChiPhiTraTruocService.deletePB(this.selectedRow.id).subscribe(
                    res => {
                        this.toastr.success(this.translate.instant('ebwebApp.pPDiscountReturn.delete.success'));
                        if (this.modalRef) {
                            this.modalRef.close();
                        }
                        this.search();
                    },
                    res => {
                        this.toastr.error(this.translate.instant('ebwebApp.pPDiscountReturn.delete.error'));
                    }
                );
            }
        }
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${TypeID.PHAN_BO_CHI_PHI_TRA_TRUOC}`, () => {
            this.exportExcel();
        });
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Them])
    addNew($event) {
        $event.preventDefault();
        this.modalRef = this.modalService.open(this.contentAddNewModal, { backdrop: 'static' });
    }

    exit() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    add(contentDuplicate) {
        this.phanBoChiPhiTraTruocService
            .getPrepaidExpenseAllocationCount({
                month: this.selectedMonth,
                year: this.selectedYear
            })
            .subscribe(
                res => {
                    if (res.body > 0) {
                        this.modalRef = this.modalService.open(contentDuplicate, { backdrop: 'static' });
                        return;
                    } else {
                        const objectDate = {
                            month: this.selectedMonth,
                            year: this.selectedYear
                        };
                        sessionStorage.setItem('objectDate', JSON.stringify(objectDate));
                        this.router.navigate(['./phan-bo-chi-phi-tra-truoc/new']);
                    }
                },
                res => {
                    if (res.error.errorKey) {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.gOtherVoucher.error.' + res.error.errorKey),
                            this.translate.instant('ebwebApp.mBCreditCard.message')
                        );
                        return;
                    }
                }
            );
    }

    clearSearch() {
        this.objSearch = {};
        this.objSearch.fromDate = null;
        this.objSearch.toDate = null;
        this.objSearch.textSearch = null;
        this.page = 1;
        this.search();
    }

    onSelect(item: IGOtherVoucher) {
        console.log(this.selectedRows);
        this.selectedRow = item;
        this.index = this.gOtherVouchers.indexOf(this.selectedRow);
        this.rowNum = this.getRowNumberOfRecord(this.page, this.index);
        if (item.id) {
            this.phanBoChiPhiTraTruocService.findDetailViewPB(this.selectedRow.id).subscribe(res => {
                this.gOtherVoucherDetailExpenses = res.body.gOtherVoucherDetailExpenses;
                this.gOtherVoucherDetailExpenseAllocations = res.body.gOtherVoucherDetailExpenseAllocations;
                this.gOtherVoucherDetails = res.body.gOtherVoucherDetails;
                this.viewVouchersSelected = res.body.refVoucherDTOS;
                for (let i = 0; i < this.gOtherVoucherDetailExpenseAllocations.length; i++) {
                    this.gOtherVoucherDetailExpenseAllocations[i].objectCode = this.prepaidExpenseCodeList.find(
                        x => (x.id = this.gOtherVoucherDetailExpenseAllocations[i].objectID)
                    ).code;
                }
                this.sumAllList();
            });
        }
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

    transition() {
        this.router.navigate(['/phan-bo-chi-phi-tra-truoc'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.search();
    }

    getSessionData() {
        if (JSON.parse(sessionStorage.getItem('objectSearch'))) {
            this.objSearch = JSON.parse(sessionStorage.getItem('objectSearch'));
            sessionStorage.removeItem('objectSearch');
        }
        if (JSON.parse(sessionStorage.getItem('dataSession'))) {
            this.dataSession = JSON.parse(sessionStorage.getItem('dataSession'));
            sessionStorage.removeItem('dataSession');
            if (this.dataSession) {
                this.page = this.dataSession.page;
                this.itemsPerPage = this.dataSession.itemsPerPage;
                this.pageCount = this.dataSession.pageCount;
                this.totalItems = this.dataSession.totalItems;
                this.rowNum = this.dataSession.rowNum;
            }
        }
        if (JSON.parse(sessionStorage.getItem('checkNew'))) {
            this.modalRef = this.modalService.open(this.modalContentAddNew, { backdrop: 'static' });
            sessionStorage.removeItem('checkNew');
        }
    }

    exportExcel() {
        this.phanBoChiPhiTraTruocService
            .exportExcelPB({
                page: this.page - 1,
                size: this.itemsPerPage,
                fromDate: this.objSearch.fromDate ? this.objSearch.fromDate : '',
                toDate: this.objSearch.toDate ? this.objSearch.toDate : '',
                textSearch: this.objSearch.textSearch ? this.objSearch.textSearch : ''
            })
            .subscribe(
                res => {
                    const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                    const fileURL = URL.createObjectURL(blob);

                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = 'DS_PhanBoChiPhiTraTruoc.xls';
                    link.setAttribute('download', name);
                    link.href = fileURL;
                    link.click();
                },
                () => {}
            );
    }

    addDuplicate() {
        this.prepaidExpenseService
            .getPrepaidExpenseAllocationDuplicate({
                month: this.selectedMonth,
                year: this.selectedYear
            })
            .subscribe(res => {
                console.log(res.body);
                this.doubleClickRow(res.body.id, true);
            });
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xoa])
    delete() {
        event.preventDefault();
        if (
            !(
                !this.gOtherVouchers ||
                this.gOtherVouchers.length === 0 ||
                !this.selectedRow ||
                (this.selectedRow.recorded && this.selectedRows.length <= 1) ||
                (this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate) && this.selectedRows.length <= 1)
            )
        ) {
            if (this.selectedRows && this.selectedRows.length > 1) {
                let dem = 0;
                for (let i = 0; i < this.selectedRows.length; i++) {
                    if (this.selectedRows[i].recorded) {
                        dem++;
                    }
                }
                if (dem === this.selectedRows.length) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.mBDeposit.errorDeleteVoucherNo'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    return;
                }
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(this.deleteModal, { backdrop: 'static' });
            } else {
                // if (
                //     !(
                //         (this.gOtherVouchers && this.gOtherVouchers.length === 0) ||
                //         !this.selectedRow ||
                //         (this.selectedRow.recorded && this.selectedRows.length <= 1) ||
                //         this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)
                //     )
                // ) {
                if (this.selectedRow.id) {
                    this.phanBoChiPhiTraTruocService.getMaxMonth(this.selectedRow.id).subscribe(res => {
                        if (moment(this.selectedRow.postedDate).isSame(moment(res.body))) {
                            if (this.modalRef) {
                                this.modalRef.close();
                            }
                            this.modalRef = this.modalService.open(this.deleteModal, { backdrop: 'static' });
                        } else {
                            this.toastr.error(
                                this.translate.instant('ebwebApp.gOtherVoucher.error.checkDeletePB'),
                                this.translate.instant('ebwebApp.mBDeposit.message')
                            );
                            return;
                        }
                    });
                }
            }
            // }
        }
    }

    private getRowNumByID(idGO: any) {
        this.phanBoChiPhiTraTruocService
            .findRowNumPBByID({
                fromDate: this.objSearch.fromDate ? this.objSearch.fromDate : '',
                toDate: this.objSearch.toDate ? this.objSearch.toDate : '',
                textSearch: this.objSearch.textSearch ? this.objSearch.textSearch : '',
                id: idGO
            })
            .subscribe(res => {
                this.rowNum = res.body;
                console.log(this.rowNum);
            });
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Sua])
    edit() {
        event.preventDefault();
        if (this.selectedRow && !this.selectedRow.recorded) {
            this.dataSession.isEdit = true;
            this.doubleClickRow(this.selectedRow.id);
        } else {
            this.dataSession.isEdit = false;
            this.toastr.error(
                this.translate.instant('ebwebApp.gOtherVoucher.errorEditVoucherNo'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
    }

    sumAllList() {
        this.sumAmount = 0;
        this.sumRemainingAmount = 0;
        this.sumAllocationAmount = 0;
        this.sumAllocationAmountDetail = 0;
        this.sumAmountDetail = 0;
        this.sumAccountingAmountDetail = 0;
        if (this.gOtherVoucherDetailExpenses && this.gOtherVoucherDetailExpenses.length > 0) {
            for (let i = 0; i < this.gOtherVoucherDetailExpenses.length; i++) {
                if (this.gOtherVoucherDetailExpenses[i].amount) {
                    this.sumAmount += this.gOtherVoucherDetailExpenses[i].amount;
                }
                if (this.gOtherVoucherDetailExpenses[i].allocationAmount) {
                    this.sumAllocationAmount += this.gOtherVoucherDetailExpenses[i].allocationAmount;
                }
                if (this.gOtherVoucherDetailExpenses[i].remainingAmount) {
                    this.sumRemainingAmount += this.gOtherVoucherDetailExpenses[i].remainingAmount;
                }
            }
        }
        if (this.gOtherVoucherDetailExpenseAllocations && this.gOtherVoucherDetailExpenseAllocations.length > 0) {
            for (let i = 0; i < this.gOtherVoucherDetailExpenseAllocations.length; i++) {
                this.sumAllocationAmountDetail += this.gOtherVoucherDetailExpenseAllocations[i].allocationAmount;
                this.sumAmountDetail += this.gOtherVoucherDetailExpenseAllocations[i].amount;
                this.sumAccountingAmountDetail += this.gOtherVoucherDetails[i].amount;
            }
        }
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

    continueMultiAction() {
        if (this.typeMultiAction === 1) {
            this.gOtherVoucherServices.multiUnRecord(this.selectedRows).subscribe(
                (res: HttpResponse<any>) => {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    for (let i = 0; i < this.selectedRows.length; i++) {
                        const temp = res.body.listFail.some(r => r.refID === this.selectedRows[i].id);
                        if (!temp) {
                            this.selectedRows[i].recorded = false;
                        }
                    }
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
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    // this.activeModal.close();
                    // this.search();
                },
                (res: HttpErrorResponse) => {
                    this.toastr.error(
                        this.translate.instant('global.data.unRecordFailed'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                }
            );
        } else if (this.typeMultiAction === 0) {
            const listRecord: RequestRecordListDtoModel = {};
            listRecord.typeIDMain = TypeID.PHAN_BO_CHI_PHI_TRA_TRUOC;
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
                    for (let i = 0; i < this.selectedRows.length; i++) {
                        const temp = res.body.listFail.some(r => r.refID === this.selectedRows[i].id);
                        if (!temp) {
                            this.selectedRows[i].recorded = true;
                        }
                    }
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
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    // this.activeModal.close();
                    // this.search();
                },
                (res: HttpErrorResponse) => {
                    this.toastr.error(
                        this.translate.instant('global.data.recordFailed'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                }
            );
        }
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
