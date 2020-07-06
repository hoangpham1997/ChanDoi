import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { Principal } from 'app/core';
import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { MBDepositService } from './mb-deposit.service';
import { IMBDepositDetails } from 'app/shared/model/mb-deposit-details.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { CurrencyService } from 'app/danhmuc/currency';
import { DatePipe } from '@angular/common';
// import { IMBDepositDetailTax } from 'app/shared/model/mb-deposit-detail-tax.model';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ToastrService } from 'ngx-toastr';
import { IType } from 'app/shared/model/type.model';
import { TypeService } from 'app/entities/type';
import { TranslateService } from '@ngx-translate/core';
import * as moment from 'moment';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import {
    DBDateClosed,
    DDSo_NCachHangDVi,
    DDSo_NCachHangNghin,
    DDSo_NgoaiTe,
    DDSo_TienVND,
    SO_LAM_VIEC,
    TCKHAC_MauCTuChuaGS,
    TCKHAC_SDSOQUANTRI,
    TypeID
} from 'app/app.constants';
import { IMultipleRecord } from 'app/shared/model/mutiple-record';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { ExpenseItemService } from 'app/entities/expense-item';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { EMContractService } from 'app/entities/em-contract';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { CostSetService } from 'app/entities/cost-set';
import { BudgetItemService } from 'app/entities/budget-item';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { IMCPayment } from 'app/shared/model/mc-payment.model';
import { IMCReceiptDetailCustomer } from 'app/shared/model/mc-receipt-detail-customer.model';
import { IMBDepositDetailCustomer } from 'app/shared/model/mb-deposit-detail-customer.model';
import { ISAInvoiceDetails } from 'app/shared/model/sa-invoice-details.model';
import { IRepository, Repository } from 'app/shared/model/repository.model';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { SAInvoiceDetailsService } from 'app/ban-hang/ban_hang_chua_thu_tien/sa-invoice-details.service';
import { SAInvoiceService } from 'app/ban-hang/ban_hang_chua_thu_tien';
import { IMCReceipt } from 'app/shared/model/mc-receipt.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IMultiDelete } from 'app/shared/model/multi-delete';
import { ROLE } from 'app/role.constants';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { RepositoryService } from 'app/danhmuc/repository';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { Moment } from 'moment';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-mb-deposit',
    templateUrl: './mb-deposit.component.html',
    styleUrls: ['./mb-deposit.component.css'],
    providers: [DatePipe]
})
export class MBDepositComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('popUpShowError') public modalComponent: NgbModalRef;
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    @ViewChild('contentUnRecord') public modalContentUnRecord: NgbModalRef;
    TYPE_MBDEPOSIT = 160;
    TYPE_MCDEPOSIT = 161;
    TYPE_MSDEPOSIT = 162;
    private TYPE_BAN_HANG_CHUA_THU_TIEN = 320;
    private TYPE_BAN_HANG_THU_TIEN_NGAY_TM = 321;
    private TYPE_BAN_HANG_THU_TIEN_NGAY_CK = 322;
    currentAccount: any;
    isSaving: boolean;
    mBDeposits: IMBDeposit[];
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
    depositDetails: IMBDepositDetails[];
    currencies: ICurrency[];
    mBDepositDetails: IMBDepositDetails[];
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
    typeMBDeposit: number;
    translateTypeMBDeposit: string;
    searchValue: string;
    listRecord: any;
    listColumnsRecord: string[] = ['name'];
    listHeaderColumnsRecord: string[] = ['Trạng thái'];
    reSearchWhenRecord: string;
    statusRecordAfterFind: Boolean;
    // mutipleRowSelected: IMBDeposit[];
    isKeyPressed: boolean;
    viewVouchers: any;
    options: any;
    isRecorded: boolean;
    isUnRecorded: boolean;
    iMutipleRecord: IMultipleRecord;
    costSets: ICostSet[];
    expenseItems: IExpenseItem[];
    organizationUnits: IOrganizationUnit[];
    eMContracts: IEMContract[];
    budgetItem: IBudgetItem[];
    statisticsCodes: IStatisticsCode[];
    isSoTaiChinh: boolean;
    isFocusCurrentVoucher: boolean;
    index: any;
    statusRecord: any;
    TCKHAC_SDSoQuanTri: any;
    currentRow: any;
    currency: ICurrency;
    isMainCurrency: boolean;
    typeDDSO_TienVND: any;
    typeDDSO_NgoaiTe: any;
    BAO_CO = 160;
    mBDepositDetailCustomers: IMBDepositDetailCustomer[];
    accountingObjects: IAccountingObject[];
    /*Nộp tiền từ bán hàng*/
    sAInvoiceDetails: ISAInvoiceDetails[];
    repositories: IRepository[];
    materialGoods: IMaterialGoods[];
    units: IUnit[];
    refVoucher: any;
    color: string;
    modalRef: NgbModalRef;
    modalRefMess: NgbModalRef;
    listIDRecorded: string[];
    listIDUnrecorded: string[];
    countTotalVouchers: number;
    countSuccessVouchers: number;
    countFailVouchers: number;
    listDeleteFail: IMultiDelete[];
    isHasCloseBook: boolean;

    /*Phiếu thu tiền khách hàng*/
    sAInvoiceID: string;
    rSInwardOutwardID: string;
    typeIDSAInvoice: number;
    typeMultiAction: number;
    isRoleBanHangThuTienNgay: boolean;
    isRoleBanHangChuaThuTien: boolean;

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
    ROLE_XEM = ROLE.BaoCo_Xem;
    ROLE_THEM = ROLE.BaoCo_Them;
    ROLE_SUA = ROLE.BaoCo_Sua;
    ROLE_XOA = ROLE.BaoCo_Xoa;
    ROLE_GHISO = ROLE.BaoCo_GhiSo;
    ROLE_IN = ROLE.BaoCo_In;
    ROLE_KETXUAT = ROLE.BaoCo_KetXuat;
    ROLE_BAN_HANG_XEM = ROLE.ChungTuBanHang_Xem;

    constructor(
        private mBDepositService: MBDepositService,
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
        public activeModal: NgbActiveModal
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.isRecorded = true;
        this.isUnRecorded = true;
        this.typeMBDeposit = 160;
        this.translate
            .get(['ebwebApp.mBDeposit.home.recorded', 'ebwebApp.mBDeposit.home.unrecorded', 'ebwebApp.mBDeposit.moneyBankDeposit'])
            .subscribe(res => {
                this.listRecord = [
                    { value: 1, name: res['ebwebApp.mBDeposit.home.recorded'] },
                    { value: 0, name: res['ebwebApp.mBDeposit.home.unrecorded'] }
                ];
                this.translateTypeMBDeposit = res['ebwebApp.mBDeposit.moneyBankDeposit'];
            });
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
        this.mBDepositService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                searchVoucher: JSON.stringify(this.saveSearchVoucher())
            })
            .subscribe(
                (res: HttpResponse<IMBDeposit[]>) => this.paginateMBDeposits(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.reSearchWhenRecord = JSON.stringify(this.saveSearchVoucher());
        sessionStorage.setItem('dataSearchMBDeposit', this.reSearchWhenRecord);
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
            this.mBDepositService
                .searchAll({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    searchVoucher: this.dataSession.searchVoucher
                })
                .subscribe(
                    (res: HttpResponse<IMBDeposit[]>) => this.paginateMBDeposits(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        } else {
            this.router.navigate(['/mb-deposit']);
        }
        this.mBDepositService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                searchVoucher: JSON.stringify(this.saveSearchVoucher())
            })
            .subscribe(
                (res: HttpResponse<IMBDeposit[]>) => this.paginateMBDeposits(res.body, res.headers),
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
        this.router.navigate(['/mb-deposit']);
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/mb-deposit',
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
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.isRoleBanHangThuTienNgay = this.currentAccount.authorities.includes(this.ROLE_BAN_HANG_XEM);
            this.isRoleBanHangChuaThuTien = this.currentAccount.authorities.includes(this.ROLE_BAN_HANG_XEM);
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
        this.countTotalVouchers = 0;
        this.countSuccessVouchers = 0;
        this.countFailVouchers = 0;
        this.listIDRecorded = [];
        this.listIDUnrecorded = [];
        this.listDeleteFail = [];
        this.index = 0;
        this.isRecorded = false;
        this.isUnRecorded = false;
        this.iMutipleRecord = {};
        this.statusRecordAfterFind = null;
        this.reSearchWhenRecord = '{}';
        this.isFocusCurrentVoucher = true;
        this.isShowSearch = false;
        this.recorded = null;
        this.sAInvoiceDetails = [];
        this.registerChangeInMBDeposits();
        if (this.isSoTaiChinh) {
            this.predicate = 'date desc,postedDate desc,noFBook';
        } else {
            this.predicate = 'date desc,postedDate desc,noMBook';
        }
        this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjects = res.body.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
        });
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticsCodes = res.body;
        });
        this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body;
        });
        this.expenseItemsService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body;
        });
        this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
            this.eMContracts = res.body;
        });
        this.organizationUnitService.getOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body;
        });
        this.budgetItemService.getBudgetItems().subscribe((res: HttpResponse<IBudgetItem[]>) => {
            this.budgetItem = res.body;
        });
        this.repositoryService.getRepositoryCombobox().subscribe((res: HttpResponse<IRepository[]>) => {
            this.repositories = res.body;
        });
        this.materialGoodsService.getMaterialGoods().subscribe((res: HttpResponse<IMaterialGoods[]>) => {
            this.materialGoods = res.body;
        });
        this.unitService.getUnits().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
        });
        this.loadAll();
        this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body.filter(type => type.typeGroupID === 16);
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
        this.mBDepositService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                searchVoucher: JSON.stringify(this.saveSearchVoucher())
            })
            .subscribe(
                (res: HttpResponse<IMBDeposit[]>) => this.paginateMBDeposits(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    // ngOnDestroy() {
    //     this.eventManager.destroy(this.eventSubscriber);
    // }

    trackId(index: number, item: IMBDeposit) {
        return item.id;
    }

    registerChangeInMBDeposits() {
        this.eventSubscriber = this.eventManager.subscribe('mBDepositListModification', response => {
            this.selectedRows = [];
            this.searchAfterChangeRecord();
        });
        this.eventSubscribers.push(this.eventSubscriber);
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

    private paginateMBDeposits(data: IMBDeposit[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.mBDeposits = data;
        this.objects = data;
        if (this.page > 1 && this.mBDeposits && this.mBDeposits.length === 0) {
            this.page = this.page - 1;
            this.loadAll();
            return;
        }
        console.log(this.selectedRows);
        if (this.objects && this.objects.length > 0) {
            if (this.dataSession && this.dataSession.rowNum) {
                this.selectedRows.push(this.mBDeposits[this.dataSession.rowNum]);
                this.selectedRow = this.mBDeposits[this.dataSession.rowNum];
            } else {
                this.selectedRows.push(this.mBDeposits[0]);
                this.selectedRow = this.mBDeposits[0];
            }
        } else {
            this.selectedRow = null;
            this.mBDepositDetails = null;
            this.viewVouchers = null;
        }
        this.onSelect(this.selectedRow);
        this.checkMultiButton(false);
        //  display pageCount
        this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
        sessionStorage.removeItem('dataSearchMBDeposit');
    }

    private onError(errorMessage: string) {
        this.toastr.error(this.translate.instant('ebwebApp.mBDeposit.error'), this.translate.instant('ebwebApp.mBDeposit.message'));
    }

    onSelect(select: IMBDeposit, data = null) {
        this.selectedRow = select;
        this.index = this.mBDeposits.indexOf(this.selectedRow);
        this.rowNum = this.getRowNumberOfRecord(this.page, this.index);
        this.checkMultiCloseBook();
        if (select && select.typeID) {
            switch (select.typeID) {
                case this.TYPE_MBDEPOSIT:
                case this.TYPE_MCDEPOSIT:
                    this.mBDepositService.find(select.id).subscribe((res: HttpResponse<IMBDeposit>) => {
                        this.mBDepositDetails =
                            res.body.mBDepositDetails === undefined
                                ? []
                                : res.body.mBDepositDetails.sort((a, b) => a.orderPriority - b.orderPriority);
                        this.viewVouchers = res.body.viewVouchers ? res.body.viewVouchers : [];
                        this.mBDepositDetailCustomers = res.body.mBDepositDetailCustomers;
                    });
                    break;
                case this.TYPE_MSDEPOSIT:
                    this.sAInvoiceDetailsService.getDetailByMBDepositID({ mBDepositID: select.id }).subscribe(ref => {
                        this.sAInvoiceDetails = ref.body;
                    });
                    this.sAInvoiceService.getRefVouchersByMBDepositID(select.id).subscribe(ref => {
                        this.refVoucher = ref.body;
                    });
                    break;
            }
        }
    }

    get mBDeposit() {
        return this.selectedRow;
    }

    set mBDeposit(mBDeposit: IMBDeposit) {
        this.selectedRow = mBDeposit;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMBDeposit>>) {
        result.subscribe((res: HttpResponse<IMBDeposit>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
    }

    private onSaveError() {
        this.isSaving = false;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoCo_GhiSo])
    record() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 2;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (this.selectedRow && !this.selectedRow.recorded) {
                this.dataSession.rowNum = this.mBDeposits.indexOf(this.selectedRow);
                this.isUnRecorded = true;
                this.isRecorded = false;
                this._record();
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoCo_GhiSo])
    unrecord() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 1;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (this.selectedRow && this.selectedRow.recorded && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
                this.dataSession.rowNum = this.mBDeposits.indexOf(this.selectedRow);
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
                case this.TYPE_MBDEPOSIT:
                case this.TYPE_MCDEPOSIT:
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
                case this.TYPE_MSDEPOSIT:
                    this.mBDepositService.find(this.selectedRow.id).subscribe((res: HttpResponse<any>) => {
                        const sAInvoiceID = res.body.sAInvoiceID;
                        if (sAInvoiceID) {
                            this.record_.id = sAInvoiceID;
                            this.record_.typeID = res.body.typeIDSAInvoice;
                            this.record_.repositoryLedgerID = sAInvoiceID;
                            this.gLService.record(this.record_).subscribe(
                                (resRC: HttpResponse<Irecord>) => {
                                    if (resRC.body.success) {
                                        this.selectedRow.recorded = true;
                                        this.messageRecord();
                                    }
                                },
                                (resRC: HttpErrorResponse) => this.messageRecordFailed()
                            );
                        }
                        this.searchAfterChangeRecord();
                    });
                    break;
            }
        } else if (this.selectedRow.recorded) {
            switch (this.selectedRow.typeID) {
                case this.TYPE_MBDEPOSIT:
                case this.TYPE_MCDEPOSIT:
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
                case this.TYPE_MSDEPOSIT:
                    this.mBDepositService.find(this.selectedRow.id).subscribe((res: HttpResponse<any>) => {
                        const sAInvoiceID = res.body.sAInvoiceID;
                        this.sAInvoiceID = sAInvoiceID;
                        this.typeIDSAInvoice = res.body.typeIDSAInvoice;
                        this.rSInwardOutwardID = res.body.rSInwardOutwardID;
                        if (sAInvoiceID) {
                            this.sAInvoiceService
                                .checkRelateVoucher({
                                    sAInvoice: sAInvoiceID
                                })
                                .subscribe((response: HttpResponse<boolean>) => {
                                    if (response.body) {
                                        this.modalRef = this.modalService.open(this.modalContentUnRecord, { backdrop: 'static' });
                                    } else {
                                        this.unRecordSAInvoice();
                                    }
                                });
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
        this.record_.id = this.sAInvoiceID;
        this.record_.typeID = this.typeIDSAInvoice;
        this.record_.repositoryLedgerID = this.rSInwardOutwardID;
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
            this.mBDepositService
                .searchAll({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    searchVoucher: this.reSearchWhenRecord
                })
                .subscribe(
                    (res: HttpResponse<IMBDeposit[]>) => this.paginateMBDeposits(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
        this.loadPage(this.page);
    }

    doubleClickRow(any?) {
        this.edit();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoCo_Xem])
    edit() {
        event.preventDefault();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.pageCount = this.pageCount;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.mBDeposits.indexOf(this.selectedRow);
        this.dataSession.isShowSearch = this.isShowSearch;
        this.dataSession.searchVoucher = JSON.stringify(this.saveSearchVoucher());
        // sort
        this.dataSession.predicate = this.predicate;
        this.dataSession.accountingObjectName = this.accountingObjectName;
        this.dataSession.reverse = this.reverse;
        sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
        switch (this.selectedRow.typeID) {
            case this.TYPE_MBDEPOSIT:
            case this.TYPE_MCDEPOSIT:
                this.router.navigate(['./mb-deposit', this.selectedRow.id, 'edit']);
                break;
            case this.TYPE_MSDEPOSIT:
                this.mBDepositService.find(this.selectedRow.id).subscribe((res: HttpResponse<any>) => {
                    const sAInvoiceID = res.body.sAInvoiceID;
                    if (
                        sAInvoiceID &&
                        ((this.isRoleBanHangThuTienNgay &&
                            (res.body.typeIDSAInvoice === this.TYPE_BAN_HANG_THU_TIEN_NGAY_TM ||
                                res.body.typeIDSAInvoice === this.TYPE_BAN_HANG_THU_TIEN_NGAY_CK)) ||
                            (this.isRoleBanHangChuaThuTien && res.body.typeIDSAInvoice === this.TYPE_BAN_HANG_CHUA_THU_TIEN) ||
                            this.currentAccount.authorities.includes('ROLE_ADMIN'))
                    ) {
                        this.router.navigate(['./chung-tu-ban-hang', sAInvoiceID, 'edit', 'from-mb-deposit', this.selectedRow.id]);
                    } else {
                        this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
                        return false;
                    }
                });
                break;
        }
        // }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoCo_Them])
    addNew(isNew = false) {
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
        this.router.navigate(['mb-deposit/', 'new']);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoCo_Xoa])
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
                    this.router.navigate(['/mb-deposit', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
                }
            }
        }
    }

    continueDelete() {
        if (this.typeMultiAction === 0) {
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.mBDepositService.multiDelete(this.selectedRows).subscribe(
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
            this.mBDepositService.multiUnrecord(this.selectedRows).subscribe(
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
            rq.typeIDMain = this.TYPE_MBDEPOSIT;
            this.gLService.recordList(rq).subscribe((res: HttpResponse<HandlingResult>) => {
                this.selectedRows.forEach(slr => {
                    const update = this.mBDeposits.find(
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
                            const mc = this.mBDeposits.find(m => m.id === n.mBDepositID);
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
            this.mBDepositService.delete(this.selectedRow.id).subscribe(response => {
                this.eventManager.broadcast({
                    name: 'mBDepositListModification',
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

    toggleItemInArr(arr, item) {
        const index = arr.indexOf(item);
        index === -1 ? arr.push(item) : arr.splice(index, 1);
    }

    getEMContractsbyID(id) {
        if (this.eMContracts) {
            const eMC = this.eMContracts.find(n => n.id === id);
            if (eMC) {
                return eMC.noMBook;
            }
        }
    }

    getAccountingObjectbyID(id) {
        if (this.accountingObjects) {
            const acc = this.accountingObjects.find(n => n.id === id);
            if (acc) {
                return acc.accountingObjectCode;
            }
        }
    }

    getStatisticsCodebyID(id) {
        if (this.statisticsCodes) {
            const statisticsCode = this.statisticsCodes.find(n => n.id === id);
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

    getExpenseItembyID(id) {
        if (this.expenseItems) {
            const expenseItem = this.expenseItems.find(n => n.id === id);
            if (expenseItem) {
                return expenseItem.expenseItemCode;
            }
        }
    }

    getOrganizationUnitbyID(id) {
        if (this.organizationUnits) {
            const organizationUnit = this.organizationUnits.find(n => n.id === id);
            if (organizationUnit) {
                return organizationUnit.organizationUnitCode;
            }
        }
    }

    getBudgetItembyID(id) {
        if (this.budgetItem) {
            const iBudgetItem = this.budgetItem.find(n => n.id === id);
            if (iBudgetItem) {
                return iBudgetItem.budgetItemCode;
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoCo_KetXuat])
    export() {
        event.preventDefault();
        if (this.mBDeposits.length > 0) {
            this.mBDepositService
                .exportPDF({
                    searchVoucher: this.reSearchWhenRecord
                })
                .subscribe(res => {
                    this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.BAO_CO);
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
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.BAO_CO}`, () => {
            this.exportExcel();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    exportExcel() {
        this.mBDepositService.exportExcel({ searchVoucher: this.reSearchWhenRecord }).subscribe(
            res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_BaoCo.xls';
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

    viewVoucher(imbDepositDetailCustomer: IMBDepositDetailCustomer) {
        let url = '';
        if (imbDepositDetailCustomer.voucherTypeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
            url = `/#/chung-tu-ban-hang/${imbDepositDetailCustomer.saleInvoiceID}/edit/from-ref`;
        } else if (imbDepositDetailCustomer.voucherTypeID === TypeID.CHUNG_TU_NGHIEP_VU_KHAC) {
            url = `/#/g-other-voucher/${imbDepositDetailCustomer.saleInvoiceID}/edit/from-ref`;
        }
        window.open(url, '_blank');
    }

    getEmployeeByID(id) {
        if (this.accountingObjects) {
            const epl = this.accountingObjects.find(n => n.id === id);
            if (epl) {
                return epl.accountingObjectCode;
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

    /*Nộp tiền từ khách hàng*/
    sumMBDepositDetailCustomers(prop) {
        let total = 0;
        if (this.mBDepositDetailCustomers) {
            for (let i = 0; i < this.mBDepositDetailCustomers.length; i++) {
                total += this.mBDepositDetailCustomers[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    getMaterialGoodsByID(id) {
        if (this.materialGoods && id) {
            const materialGood = this.materialGoods.find(n => n.id === id);
            if (materialGood) {
                return materialGood.materialGoodsCode;
            }
        }
    }

    getUnitByID(id) {
        if (this.units && id) {
            const unit = this.units.find(n => n.id === id);
            if (unit) {
                return unit.unitName;
            }
        }
    }

    /*Nộp tiền từ bán hàng*/
    getRepositoryByID(id) {
        if (this.repositories && id) {
            const repository = this.repositories.find(n => n.id === id.toLowerCase());
            if (repository) {
                return repository.repositoryCode;
            }
        }
    }

    getUnitPriceOriginalType() {
        if (this.isForeignCurrency()) {
            return 2;
        }
        return 1;
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

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.sAInvoiceDetails.length; i++) {
            total += this.sAInvoiceDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    getAmountOriginal() {
        if (this.mBDepositDetails && this.mBDepositDetails.length > 0) {
            return this.mBDepositDetails.map(n => n.amountOriginal).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getAmount() {
        if (this.mBDepositDetails && this.mBDepositDetails.length > 0) {
            return this.mBDepositDetails.map(n => n.amount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    showPopUp() {
        this.modalRef = this.modalService.open(this.modalComponent, {
            size: 'lg',
            windowClass: 'height-30',
            backdrop: 'static'
        });
    }

    sumMBDepositAmountOriginal() {
        let sum = 0;
        if (this.mBDepositDetails) {
            for (let i = 0; i < this.mBDepositDetails.length; i++) {
                sum += this.mBDepositDetails[i].amountOriginal;
            }
        }
        return sum;
    }

    sumMBDepositAmount() {
        let sum = 0;
        if (this.mBDepositDetails) {
            for (let i = 0; i < this.mBDepositDetails.length; i++) {
                sum += this.mBDepositDetails[i].amount;
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
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
            this.loadAll();
        });
        this.eventSubscribers.push(this.eventSubscriber);
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
}
