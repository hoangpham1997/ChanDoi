import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { Principal } from 'app/core';

import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { CPExpenseTranferService } from './cp-expense-tranfer.service';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { ICPExpenseTranferDetails } from 'app/shared/model/cp-expense-tranfer-details.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { IType } from 'app/shared/model/type.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ROLE } from 'app/role.constants';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { CurrencyService } from 'app/danhmuc/currency';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { DatePipe } from '@angular/common';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { ToastrService } from 'ngx-toastr';
import { TypeService } from 'app/entities/type';
import { TranslateService } from '@ngx-translate/core';
import { ExpenseItemService } from 'app/entities/expense-item';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { EMContractService } from 'app/entities/em-contract';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { CostSetService } from 'app/entities/cost-set';
import { BudgetItemService } from 'app/entities/budget-item';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { SAInvoiceDetailsService } from 'app/ban-hang/ban_hang_chua_thu_tien/sa-invoice-details.service';
import { SAInvoiceService } from 'app/ban-hang/ban_hang_chua_thu_tien';
import { RepositoryService } from 'app/danhmuc/repository';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { UnitService } from 'app/danhmuc/unit';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { SO_LAM_VIEC, TCKHAC_MauCTuChuaGS, TCKHAC_SDSOQUANTRI, TypeID } from 'app/app.constants';
import * as moment from 'moment';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { Irecord } from 'app/shared/model/record';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { ICPExpenseTranfer } from 'app/shared/model/cp-expense-tranfer.model';
import { ICPPeriod } from 'app/shared/model/cp-period.model';
import { PpGianDonService } from 'app/giathanh/phuong_phap_gian_don';

@Component({
    selector: 'eb-cp-expense-tranfer',
    templateUrl: './cp-expense-tranfer.component.html',
    styleUrls: ['./cp-expense-tranfer.component.css']
})
export class CPExpenseTranferComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('popUpShowError') public modalComponent: NgbModalRef;
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    @ViewChild('contentUnRecord') public modalContentUnRecord: NgbModalRef;
    @ViewChild('contentAddNew') public popUpAddNew: NgbModalRef;
    @ViewChild('checkExist') public checkExist: NgbModalRef;
    TYPE_CPEXPENSETRANFER = 701;
    currentAccount: any;
    isSaving: boolean;
    cPExpenseTranfers: ICPExpenseTranfer[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    typeID: string;
    fromDate: any;
    toDate: any;
    currencyID: string;
    recorded: boolean;
    accountingObjectCode: string;
    objSearch: ISearchVoucher;
    currencies: ICurrency[];
    cPExpenseTranferDetails: ICPExpenseTranferDetails[];
    types: IType[];
    typeName: string;
    date: any;
    isRecord: Boolean;
    items: any;
    record_: any;
    selectedRow: any;
    rowNum: number;
    pageCount: any;
    accountingObjectName: string;
    isShowSearch: boolean;
    searchValue: string;
    listRecord: any;
    listColumnsRecord: string[] = ['name'];
    listHeaderColumnsRecord: string[] = ['Trạng thái'];
    reSearchWhenRecord: string;
    statusRecordAfterFind: Boolean;
    isKeyPressed: boolean;
    viewVouchers: any;
    options: any;
    isRecorded: boolean;
    isUnRecorded: boolean;
    isSoTaiChinh: boolean;
    isFocusCurrentVoucher: boolean;
    index: any;
    statusRecord: any;
    TCKHAC_SDSoQuanTri: any;
    currentRow: any;
    currency: ICurrency;
    accountingObjects: IAccountingObject[];
    typeMultiAction: number;
    isHasCloseBook: boolean;
    modalRef: NgbModalRef;
    modalRefMess: NgbModalRef;
    cPPeriodList: ICPPeriod[];
    cPPeriodID: string;

    buttonDeleteTranslate = 'ebwebApp.mBDeposit.toolTip.delete';
    buttonAddTranslate = 'ebwebApp.mBDeposit.toolTip.add';
    buttonEditTranslate = 'ebwebApp.mBDeposit.toolTip.edit';
    buttonRecordTranslate = 'ebwebApp.mBDeposit.toolTip.record';
    buttonUnRecordTranslate = 'ebwebApp.mBDeposit.toolTip.unrecord';
    buttonExportTranslate = 'ebwebApp.mBDeposit.toolTip.export';
    buttonSearchTranslate = 'ebwebApp.mBDeposit.toolTip.search';

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
    }();
    ROLE_XEM = ROLE.KetChuyenChiPhi_Xem;
    ROLE_THEM = ROLE.KetChuyenChiPhi_Them;
    ROLE_SUA = ROLE.KetChuyenChiPhi_Sua;
    ROLE_XOA = ROLE.KetChuyenChiPhi_Xoa;
    ROLE_GHISO = ROLE.KetChuyenChiPhi_GhiSo;
    ROLE_IN = ROLE.KetChuyenChiPhi_In;
    ROLE_KETXUAT = ROLE.KetChuyenChiPhi_KetXuat;

    constructor(
        private cPExpenseTranferService: CPExpenseTranferService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private accountingObjectService: AccountingObjectService,
        private currencyService: CurrencyService,
        public utilsService: UtilsService,
        private datepipe: DatePipe,
        private gLService: GeneralLedgerService,
        private toastr: ToastrService,
        private typeService: TypeService,
        public translate: TranslateService,
        private expenseItemsService: ExpenseItemService,
        private statisticsCodeService: StatisticsCodeService,
        private eMContractService: EMContractService,
        private organizationUnitService: OrganizationUnitService,
        private costSetService: CostSetService,
        private budgetItemService: BudgetItemService,
        private refModalService: RefModalService,
        private sAInvoiceDetailsService: SAInvoiceDetailsService,
        private sAInvoiceService: SAInvoiceService,
        private repositoryService: RepositoryService,
        private materialGoodsService: MaterialGoodsService,
        private unitService: UnitService,
        private modalService: NgbModal,
        public activeModal: NgbActiveModal,
        private cPPeriodService: PpGianDonService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.TCKHAC_SDSoQuanTri = this.currentAccount.systemOption.find(x => x.code === TCKHAC_SDSOQUANTRI).data;
            this.color = this.currentAccount.systemOption.find(x => x.code === TCKHAC_MauCTuChuaGS && x.data).data;
            if (account) {
                if (this.TCKHAC_SDSoQuanTri === '1') {
                    this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                } else {
                    this.isSoTaiChinh = true;
                }
            }
            this.currencyService.findAllActive().subscribe(res => {
                this.currencies = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                this.currency = this.currencies.find(cur => cur.currencyCode === this.currentAccount.organizationUnit.currencyID);
                this.currencyID = this.currency.currencyCode !== null ? this.currency.currencyCode : '';
            });
        });
        this.isRecorded = true;
        this.isUnRecorded = true;
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    search() {
        if (this.objSearch.fromDate > this.objSearch.toDate) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBDeposit.fromDateMustBeLessThanToDate'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
        this.objSearch.statusRecorded = this.statusRecord;
        this.cPExpenseTranferService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                searchVoucher: JSON.stringify(this.saveSearchVoucher())
            })
            .subscribe(
                (res: HttpResponse<ICPExpenseTranfer[]>) => this.paginateCPExpenseTranfer(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.reSearchWhenRecord = JSON.stringify(this.saveSearchVoucher());
        sessionStorage.setItem('dataSearchCPExpenseTranfer', this.reSearchWhenRecord);
        this.page = 1;
        this.loadPage(this.page);
    }

    loadAll() {
        if (sessionStorage.getItem('dataSession')) {
            this.dataSession = JSON.parse(sessionStorage.getItem('dataSession'));
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.isShowSearch = this.dataSession.isShowSearch;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
            this.previousPage = this.dataSession.page;
            this.objSearch = JSON.parse(this.dataSession.searchVoucher);
            this.statusRecord = this.objSearch.statusRecorded;
            sessionStorage.removeItem('dataSession');
        }
        if (this.activatedRoute.snapshot.paramMap.has('isSearch')) {
            this.cPExpenseTranferService
                .searchAll({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    searchVoucher: this.dataSession.searchVoucher
                })
                .subscribe(
                    (res: HttpResponse<ICPExpenseTranfer[]>) => this.paginateCPExpenseTranfer(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        } else {
            this.router.navigate(['/ket-chuyen-chi-phi']);
        }
        this.cPExpenseTranferService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                searchVoucher: JSON.stringify(this.saveSearchVoucher())
            })
            .subscribe(
                (res: HttpResponse<ICPExpenseTranfer[]>) => this.paginateCPExpenseTranfer(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    transition() {
        this.dataSession.rowNum = 0;
        // this.router.navigate(['/ket-chuyen-chi-phi'], {
        //     queryParams: {
        //         page: this.page,
        //         size: this.itemsPerPage,
        //         sort: this.predicate + ',' + (this.reverse ? 'desc' : 'asc')
        //     }
        // });
        this.router.navigate(['/ket-chuyen-chi-phi']);
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/ket-chuyen-chi-phi',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'desc' : 'asc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.isHasCloseBook = true;
        this.objSearch = new class implements ISearchVoucher {
            accountingObjectID: string;
            currencyID: string;
            fromDate: moment.Moment;
            statusRecorded: boolean;
            textSearch: string;
            toDate: moment.Moment;
            typeID: number;
        }();
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
        this.loadAll();
        this.cPPeriodService.getCPPeriod().subscribe((res: HttpResponse<ICPPeriod[]>) => {
            this.cPPeriodList = res.body;
        });
        if (sessionStorage.getItem('checkNewKCCP')) {
            this.addNew(event);
            sessionStorage.removeItem('checkNewKCCP');
        }
        this.cPPeriodList = [];
        this.index = 0;
        this.isRecorded = false;
        this.isUnRecorded = false;
        this.statusRecordAfterFind = null;
        this.reSearchWhenRecord = '{}';
        this.isFocusCurrentVoucher = true;
        this.isShowSearch = false;
        this.recorded = null;
        this.registerChangeInCPExpenseTranfers();
        if (this.isSoTaiChinh) {
            this.predicate = 'date desc,postedDate desc,noFBook';
        } else {
            this.predicate = 'date desc,postedDate desc,noMBook';
        }
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticCodes = res.body;
        });
        this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body;
        });
        this.expenseItemsService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body;
        });
        this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body.filter(type => type.typeGroupID === 70);
        });
        this.isKeyPressed = false;
        this.registerExport();
        this.registerLockSuccess();
        this.registerUnlockSuccess();
    }

    clearSearch() {
        this.objSearch = {};
        this.objSearch.fromDate = null;
        this.objSearch.toDate = null;
        this.statusRecord = null;
        this.cPExpenseTranferService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                searchVoucher: JSON.stringify(this.saveSearchVoucher())
            })
            .subscribe(
                (res: HttpResponse<ICPExpenseTranfer[]>) => this.paginateCPExpenseTranfer(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICPExpenseTranfer) {
        return item.id;
    }

    registerChangeInCPExpenseTranfers() {
        this.eventSubscriber = this.eventManager.subscribe('cPExpenseTranferListModification', response => {
            this.selectedRows = [];
            this.searchAfterChangeRecord();
        });
    }

    sort() {
        const result = ['date' + ',' + (this.reverse ? 'desc' : 'asc')];
        // result.push('postedDate' + ',' + (this.reverse ? 'asc' : 'desc'));
        result.push('postedDate' + ',' + (this.reverse ? 'desc' : 'asc'));
        if (this.isSoTaiChinh) {
            result.push('noFBook' + ',' + (this.reverse ? 'desc' : 'asc'));
        } else {
            result.push('noMBook' + ',' + (this.reverse ? 'desc' : 'asc'));
        }
        // const result = ['date, desc', 'postedDate, desc'];
        return result;
    }

    private paginateCPExpenseTranfer(data: ICPExpenseTranfer[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.cPExpenseTranfers = data;
        this.objects = data;
        console.log(this.selectedRows);
        if (this.objects && this.objects.length > 0) {
            if (this.dataSession && this.dataSession.rowNum) {
                this.selectedRows.push(this.cPExpenseTranfers[this.dataSession.rowNum]);
                this.selectedRow = this.cPExpenseTranfers[this.dataSession.rowNum];
            } else {
                this.selectedRows.push(this.cPExpenseTranfers[0]);
                this.selectedRow = this.cPExpenseTranfers[0];
            }
        } else {
            this.selectedRow = null;
            this.cPExpenseTranferDetails = null;
            this.viewVouchers = null;
        }
        this.onSelect(this.selectedRow);
        this.checkMultiButton(false);
        //  display pageCount
        this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
        sessionStorage.removeItem('dataSearchCPExpenseTranfer');
    }

    private onError(errorMessage: string) {
        this.toastr.error(this.translate.instant('ebwebApp.mBDeposit.error'), this.translate.instant('ebwebApp.mBDeposit.message'));
    }

    onSelect(select: ICPExpenseTranfer, data = null) {
        this.selectedRow = select;
        this.index = this.cPExpenseTranfers.indexOf(this.selectedRow);
        this.rowNum = this.getRowNumberOfRecord(this.page, this.index);
        this.checkMultiCloseBook();
        if (select && select.id) {
            this.cPExpenseTranferService.find(select.id).subscribe((res: HttpResponse<ICPExpenseTranfer>) => {
                this.cPExpenseTranferDetails =
                    res.body.cPExpenseTranferDetails === undefined
                        ? []
                        : res.body.cPExpenseTranferDetails.sort((a, b) => a.orderPriority - b.orderPriority);
                this.viewVouchers = res.body.viewVouchers ? res.body.viewVouchers : [];
            });
        }
    }

    get mBDeposit() {
        return this.selectedRow;
    }

    set mBDeposit(mBDeposit: ICPExpenseTranfer) {
        this.selectedRow = mBDeposit;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICPExpenseTranfer>>) {
        result.subscribe((res: HttpResponse<ICPExpenseTranfer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
    }

    private onSaveError() {
        this.isSaving = false;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenChiPhi_GhiSo])
    record() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 2;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (this.selectedRow && !this.selectedRow.recorded) {
                this.dataSession.rowNum = this.cPExpenseTranfers.indexOf(this.selectedRow);
                this.isUnRecorded = true;
                this.isRecorded = false;
                this._record();
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenChiPhi_GhiSo])
    unrecord() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 1;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (this.selectedRow && this.selectedRow.recorded && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
                this.dataSession.rowNum = this.cPExpenseTranfers.indexOf(this.selectedRow);
                this.isRecorded = true;
                this.isUnRecorded = false;
                this._record();
            }
        }
    }

    _record() {
        this.record_ = {};
        this.record_.id = this.selectedRow.id;
        this.record_.typeID = this.selectedRow.typeID;
        if (this.selectedRow.recorded !== true) {
            switch (this.selectedRow.typeID) {
                case this.TYPE_CPEXPENSETRANFER:
                    this.gLService.record(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                        console.log(JSON.stringify(res.body));
                        if (res.body.success) {
                            this.selectedRow.recorded = true;
                            this.toastr.success(
                                this.translate.instant('ebwebApp.mBDeposit.recordSuccess'),
                                this.translate.instant('ebwebApp.mBDeposit.message')
                            );
                            this.searchAfterChangeRecord();
                        }
                    });
                    break;
            }
        } else if (this.selectedRow.recorded) {
            switch (this.selectedRow.typeID) {
                case this.TYPE_CPEXPENSETRANFER:
                    this.gLService.unrecord(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                        console.log(JSON.stringify(res.body));
                        if (res.body.success) {
                            this.selectedRow.recorded = false;
                            this.toastr.success(
                                this.translate.instant('ebwebApp.mBDeposit.unrecordSuccess'),
                                this.translate.instant('ebwebApp.mBDeposit.message')
                            );
                            this.searchAfterChangeRecord();
                        }
                    });
                    break;
            }
        } else {
            this.toastr.error('ebwebApp.mBDeposit.error', 'ebwebApp.mBDeposit.message');
        }
        this.selectedRows = [];
    }

    unRecordSAInvoice() {
        this.record_ = {};
        this.gLService.unrecord(this.record_).subscribe(
            (resURC: HttpResponse<Irecord>) => {
                if (resURC.body.success) {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    this.selectedRow.recorded = false;
                    this.messageUnrecord();
                }
            },
            (resURC: HttpErrorResponse) => this.messageUnrecordFailed()
        );
    }

    messageRecord() {
        this.toastr.success(
            this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
    }

    messageUnrecord() {
        this.toastr.success(
            this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
    }

    messageUnrecordFailed() {
        this.toastr.error(this.translate.instant('global.data.unRecordFailed'), this.translate.instant('ebwebApp.mBDeposit.message'));
    }

    messageRecordFailed() {
        this.toastr.error(this.translate.instant('global.data.recordFailed'), this.translate.instant('ebwebApp.mBDeposit.message'));
    }

    searchAfterChangeRecord() {
        if (this.reSearchWhenRecord) {
            this.cPExpenseTranferService
                .searchAll({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    searchVoucher: this.reSearchWhenRecord
                })
                .subscribe(
                    (res: HttpResponse<ICPExpenseTranfer[]>) => this.paginateCPExpenseTranfer(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
        this.loadPage(this.page);
    }

    doubleClickRow(any?) {
        this.edit();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenChiPhi_Xem])
    edit() {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.pageCount = this.pageCount;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.cPExpenseTranfers.indexOf(this.selectedRow);
        this.dataSession.isShowSearch = this.isShowSearch;
        this.dataSession.searchVoucher = JSON.stringify(this.saveSearchVoucher());
        // sort
        this.dataSession.predicate = this.predicate;
        this.dataSession.accountingObjectName = this.accountingObjectName;
        this.dataSession.reverse = this.reverse;
        sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
        switch (this.selectedRow.typeID) {
            case this.TYPE_CPEXPENSETRANFER:
                this.router.navigate(['./ket-chuyen-chi-phi', this.selectedRow.id, 'edit']);
                break;
        }
        // }
    }

    showPopupAddNew() {
        event.preventDefault();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.pageCount = this.pageCount;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.rowNum;
        // sort
        this.dataSession.predicate = this.predicate;
        this.dataSession.searchVoucher = JSON.stringify(this.saveSearchVoucher());
        this.dataSession.accountingObjectName = this.accountingObjectName;
        this.dataSession.reverse = this.reverse;
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.cPExpenseTranferService.findByCPPeriodID({ cPPeriodID: this.cPPeriodID }).subscribe(
            res => {
                if (this.cPPeriodID) {
                    sessionStorage.setItem('cPPeriodID', JSON.stringify(this.cPPeriodID));
                    const cPPeriodName = this.cPPeriodList.find(a => a.id === this.cPPeriodID);
                    if (cPPeriodName) {
                        sessionStorage.setItem('cPPeriodName', JSON.stringify(cPPeriodName.name));
                    }
                    this.router.navigate(['ket-chuyen-chi-phi/', 'new']);
                }
            },
            (res: HttpErrorResponse) => {
                if (res.error && res.error.errorKey === 'existCPPeriod') {
                    this.modalRef = this.modalService.open(this.checkExist, { backdrop: 'static' });
                }
            }
        );
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenChiPhi_Them])
    addNew($event) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        event.preventDefault();
        this.modalRef = this.modalService.open(this.popUpAddNew, { backdrop: 'static' });
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenChiPhi_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRow) {
            if (this.selectedRows.length > 1) {
                this.typeMultiAction = 0;
                this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
            } else if (
                this.selectedRows &&
                this.selectedRows.length === 1 &&
                !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)
            ) {
                if (this.selectedRow && !this.selectedRow.recorded) {
                    this.typeMultiAction = undefined;
                    this.router.navigate(['/ket-chuyen-chi-phi', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
                }
            }
        }
    }

    continueDelete() {
        if (this.typeMultiAction === 0) {
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.cPExpenseTranferService.multiDelete(this.selectedRows).subscribe(
                (res: HttpResponse<any>) => {
                    this.typeMultiAction = undefined;
                    this.selectedRows = [];
                    this.searchAfterChangeRecord();
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
                        this.toastr.success(this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'));
                    }
                    this.activeModal.close();
                    // }
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
        } else if (this.typeMultiAction === 1) {
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.cPExpenseTranferService.multiUnrecord(this.selectedRows).subscribe(
                (res: HttpResponse<any>) => {
                    this.typeMultiAction = undefined;
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
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    this.activeModal.close();
                    this.loadAll();
                    // }
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
        } else if (this.typeMultiAction === 2) {
            if (this.modalRef) {
                this.modalRef.close();
            }
            const rq: RequestRecordListDtoModel = {};
            rq.records = this.selectedRows.map(n => {
                return { id: n.id, typeID: n.typeID };
            });
            rq.typeIDMain = this.TYPE_CPEXPENSETRANFER;
            this.gLService.recordList(rq).subscribe((res: HttpResponse<HandlingResult>) => {
                this.selectedRows.forEach(slr => {
                    const update = this.cPExpenseTranfers.find(
                        n => n.id === slr.id && !res.body.listFail.some(m => n.id === slr.id || m.mBDepositID === slr.id)
                    );
                    if (update) {
                        update.recorded = true;
                    }
                    this.typeMultiAction = undefined;
                    this.loadAll();
                });
                if (res.body.countFailVouchers > 0) {
                    res.body.listFail.forEach(n => {
                        if (n.mBDepositID) {
                            const mc = this.cPExpenseTranfers.find(m => m.id === n.mBDepositID);
                            const type = this.types.find(t => t.id === mc.typeID);
                            n.noFBook = mc.noFBook;
                            n.noMBook = mc.noMBook;
                            n.typeName = type.typeName;
                        } else {
                            const type = this.types.find(t => t.id === n.typeID);
                            n.typeName = type.typeName;
                        }
                    });
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
                } else {
                    this.messageRecord();
                }
            });
        } else if (this.selectedRows.length === 1 && !this.typeMultiAction) {
            this.cPExpenseTranferService.delete(this.selectedRow.id).subscribe(response => {
                this.eventManager.broadcast({
                    name: 'cPExpenseTranferListModification',
                    content: 'Deleted an mBDeposit'
                });
                this.modalRef.close();
            });
            this.toastr.success(this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'));
            this.router.navigate(['mb-deposit']);
        }
        this.typeMultiAction = undefined;
    }

    closePopUpDelete() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    saveSearchVoucher(): ISearchVoucher {
        const searchObj: ISearchVoucher = {};
        searchObj.typeID = this.objSearch.typeID !== undefined ? this.objSearch.typeID : null;
        searchObj.statusRecorded = this.objSearch.statusRecorded !== undefined ? this.objSearch.statusRecorded : null;
        searchObj.currencyID = this.objSearch.currencyID !== undefined ? this.objSearch.currencyID : null;
        searchObj.fromDate = this.objSearch.fromDate !== undefined ? this.objSearch.fromDate : null;
        searchObj.toDate = this.objSearch.toDate !== undefined ? this.objSearch.toDate : null;
        searchObj.accountingObjectID = this.objSearch.accountingObjectID !== undefined ? this.objSearch.accountingObjectID : null;
        searchObj.textSearch = this.objSearch.textSearch !== undefined ? this.objSearch.textSearch : null;
        return this.convertDateFromClient(searchObj);
    }

    getStatisticsCodebyID(id) {
        if (this.statisticCodes) {
            const statisticsCode = this.statisticCodes.find(n => n.id === id);
            if (statisticsCode) {
                return statisticsCode.statisticsCode;
            }
        }
    }

    getCostSetbyID(id) {
        if (this.costSets) {
            const costSet = this.costSets.find(n => n.id === id);
            if (costSet) {
                return costSet.costSetCode;
            }
        }
    }

    getCostSetName(id) {
        if (this.costSets) {
            const costSet = this.costSets.find(n => n.id === id);
            if (costSet) {
                return costSet.costSetName;
            }
        }
    }

    getExpenseItembyID(id) {
        if (this.expenseItems) {
            const expenseItem = this.expenseItems.find(n => n.id === id);
            if (expenseItem) {
                return expenseItem.expenseItemCode;
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenChiPhi_KetXuat])
    export() {
        event.preventDefault();
        if (this.cPExpenseTranfers.length > 0) {
            this.cPExpenseTranferService
                .exportPDF({
                    searchVoucher: this.reSearchWhenRecord
                })
                .subscribe(res => {
                    this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.TYPE_CPEXPENSETRANFER);
                });
        }
    }

    convertDateFromClient(searchVoucher: ISearchVoucher): ISearchVoucher {
        const copy: ISearchVoucher = Object.assign({}, searchVoucher, {
            fromDate:
                searchVoucher.fromDate != null && searchVoucher.fromDate.isValid() ? searchVoucher.fromDate.format(DATE_FORMAT) : null,
            toDate: searchVoucher.toDate != null && searchVoucher.toDate.isValid() ? searchVoucher.toDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.TYPE_CPEXPENSETRANFER}`, () => {
            this.exportExcel();
        });
    }

    exportExcel() {
        this.cPExpenseTranferService.exportExcel({ searchVoucher: this.reSearchWhenRecord }).subscribe(
            res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_Ket_Chuyen_Chi_Phi.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            },
            () => {}
        );
    }

    getTypeName(typeID) {
        if (typeID && this.types) {
            const currentType = this.types.find(type => type.id === typeID);
            if (currentType) {
                return currentType.typeName;
            } else {
                return '';
            }
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    isForeignCurrency() {
        if (this.selectedRow) {
            return this.currentAccount && this.selectedRow.currencyID !== this.currentAccount.organizationUnit.currencyID;
        }
    }

    getAmountOriginalType() {
        if (this.isForeignCurrency()) {
            return 8;
        }
        return 7;
    }

    getAmountOriginal() {
        if (this.cPExpenseTranferDetails && this.cPExpenseTranferDetails.length > 0) {
            return this.cPExpenseTranferDetails.map(n => n.amountOriginal).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getAmount() {
        if (this.cPExpenseTranferDetails && this.cPExpenseTranferDetails.length > 0) {
            return this.cPExpenseTranferDetails.map(n => n.amount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    sumCPExpenseTranferAmountOriginal() {
        let sum = 0;
        if (this.cPExpenseTranferDetails) {
            for (let i = 0; i < this.cPExpenseTranferDetails.length; i++) {
                sum += this.cPExpenseTranferDetails[i].amountOriginal;
            }
        }
        return sum;
    }

    sumCPExpenseTranferAmount() {
        let sum = 0;
        if (this.cPExpenseTranferDetails) {
            for (let i = 0; i < this.cPExpenseTranferDetails.length; i++) {
                sum += this.cPExpenseTranferDetails[i].amount;
            }
        }
        return sum;
    }

    registerLockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('lockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
            this.loadAll();
        });
    }

    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
            this.loadAll();
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

    checkMultiCloseBook() {
        if (this.selectedRows && this.selectedRows.length > 1) {
            for (let i = 0; i < this.selectedRows.length; i++) {
                if (!this.checkCloseBook(this.currentAccount, this.selectedRows[i].postedDate)) {
                    this.isHasCloseBook = true;
                    return;
                }
            }
            this.isHasCloseBook = false;
        }
    }

    getCPPeriodName(id) {
        if (this.cPPeriodList && this.cPPeriodList.length > 0) {
            const cPPeriodName = this.cPPeriodList.find(a => a.id === id);
            if (cPPeriodName) {
                return cPPeriodName.name;
            }
        }
    }
}
