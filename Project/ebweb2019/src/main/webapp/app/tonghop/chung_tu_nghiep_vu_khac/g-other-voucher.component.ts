import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { Principal } from 'app/core';

import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { GOtherVoucherService } from './g-other-voucher.service';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { ICurrency } from 'app/shared/model/currency.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IType } from 'app/shared/model/type.model';
import { IMultipleRecord } from 'app/shared/model/mutiple-record';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
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
import * as moment from 'moment';
import {
    DDSo_NCachHangDVi,
    DDSo_NCachHangNghin,
    DDSo_NgoaiTe,
    DDSo_TienVND,
    MSGERROR,
    SO_LAM_VIEC,
    TCKHAC_MauCTuChuaGS,
    TypeID
} from 'app/app.constants';
import { Irecord } from 'app/shared/model/record';
import { IGOtherVoucherDetails } from 'app/shared/model/g-other-voucher-details.model';
import { IGOtherVoucherDetailTax } from 'app/shared/model/g-other-voucher-detail-tax.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { type } from 'os';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { IGOtherVoucherDetailExpense } from 'app/shared/model/g-other-voucher-detail-expense.model';
import { IGOtherVoucherDetailExpenseAllocation } from 'app/shared/model/g-other-voucher-detail-expense-allocation.model';
import { IPrepaidExpense } from 'app/shared/model/prepaid-expense.model';
import { PrepaidExpenseService } from 'app/entities/prepaid-expense';
import { ChiPhiTraTruocService } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc.service';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { PhanBoChiPhiTraTruocService } from 'app/tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc.service';

@Component({
    selector: 'eb-g-other-voucher',
    templateUrl: './g-other-voucher.component.html',
    styleUrls: ['./g-other-voucher.component.css']
})
export class GOtherVoucherComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    currentAccount: any;
    gOtherVouchers: IGOtherVoucher[];
    isSaving: boolean;
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
    accountingObject: IAccountingObject[];
    gOtherVoucherDetails: IGOtherVoucherDetails[];
    gOtherVoucherDetailTax: IGOtherVoucherDetailTax[];
    prepaidExpenses: IPrepaidExpense[];
    types: IType[];
    typeName: string;
    date: any;
    isRecord: Boolean;
    items: any;
    record_: any;
    selectedRow: IGOtherVoucher;
    rowNum: number;
    pageCount: any;
    accountingObjectName: string;
    isShowSearch: boolean;
    typeGOtherVoucher: number;
    translateTypeGOtherVoucher: string;
    searchValue: string;
    listRecord: any;
    listColumnsRecord: string[] = ['name'];
    listHeaderColumnsRecord: string[] = ['Trạng thái'];
    reSearchWhenRecord: string;
    statusRecordAfterFind: Boolean;
    mutipleRowSelected: IGOtherVoucher[];
    selectedPersonArray = [];
    isKeyPressed: boolean;
    viewVouchers: any;
    options: any;
    DDSo_NCachHangNghin: any;
    DDSo_NCachHangDVi: any;
    DDSo_TienVND: any;
    DDSo_NgoaiTe: any;
    isRecorded: boolean;
    isUnRecorded: boolean;
    iMutipleRecord: IMultipleRecord;
    costSets: ICostSet[];
    expenseItems: IExpenseItem[];
    organizationUnits: IOrganizationUnit[];
    eMContracts: IEMContract[];
    budgetItem: IBudgetItem[];
    statisticsCodes: IStatisticsCode[];
    goodsServicePurchases: IGoodsServicePurchase[];
    prepaidExpenseCodeList: any[];
    isSoTaiChinh: boolean;
    isFocusCurrentVoucher: boolean;
    index: any;
    bankAccountDetails: IBankAccountDetails[];
    statusRecord: any;
    currentRow: any;
    TYPE_CHUNG_TU_NGHIEP_VU_KHAC = 700;
    TYPE_KET_CHUYEN_LAI_LO = 702;
    TYPE_PHAN_BO_CHI_PHI_TRA_TRUOC = 709;
    TYPE_NGHIEM_THU_CONG_TRINH_DON_HANG_HOP_DONG = 703;
    color: string;
    currency: ICurrency;
    isNavigateForm: boolean;
    modalRef: NgbModalRef;
    typeMultiAction: number;
    isShowGoodsServicePurchase: boolean;
    ROLE_KET_CHUYEN_LAI_LO = ROLE.KetChuyenLaiLo_Xem;
    ROLE_PHAN_BO_CHI_PHI_TRA_TRUOC = ROLE.PhanBoChiPhiTRaTruoc_Xem;
    isRoleKetChuyenLaiLo: boolean;
    isRolePhanBoChiPhiTraTruoc: boolean;

    gOtherVoucherDetailExpenses: IGOtherVoucherDetailExpense[];
    gOtherVoucherDetailExpenseAllocations: IGOtherVoucherDetailExpenseAllocation[];

    ROLE_CTNVK_XEM = ROLE.ChungTuNghiepVuKhac_Xem;
    ROLE_CTNVK_THEM = ROLE.ChungTuNghiepVuKhac_Them;
    ROLE_CTNVK_SUA = ROLE.ChungTuNghiepVuKhac_Sua;
    ROLE_CTNVK_XOA = ROLE.ChungTuNghiepVuKhac_Xoa;
    ROLE_CTNVK_GHISO = ROLE.ChungTuNghiepVuKhac_GhiSo;
    ROLE_CTNVK_IN = ROLE.ChungTuNghiepVuKhac_In;
    ROLE_CTNVK_KETXUAT = ROLE.ChungTuNghiepVuKhac_KetXuat;

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

    constructor(
        private gOtherVoucherService: GOtherVoucherService,
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
        private bankAccountDetailsService: BankAccountDetailsService,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        private refModalService: RefModalService,
        private prepaidExpenseService: PrepaidExpenseService,
        private chiPhiTraTruocService: ChiPhiTraTruocService,
        private modalService: NgbModal,
        public activeModal: NgbActiveModal,
        private phanBoChiPhiTraTruocService: PhanBoChiPhiTraTruocService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
            this.translate.get(['ebwebApp.mBCreditCard.home.recorded', 'ebwebApp.mBCreditCard.home.unrecorded']).subscribe(res => {
                this.listRecord = [
                    { value: 1, name: res['ebwebApp.mBCreditCard.home.recorded'] },
                    { value: 0, name: res['ebwebApp.mBCreditCard.home.unrecorded'] }
                ];
            });
        });
        if (this.isSoTaiChinh) {
            this.predicate = 'date desc,postedDate desc,noFBook';
        } else {
            this.predicate = 'date desc,postedDate desc,noMBook';
        }
        this.isRecorded = true;
        this.isUnRecorded = true;
        this.typeGOtherVoucher = 700;
        this.translateTypeGOtherVoucher = this.translate.instant('ebwebApp.gOtherVoucher.home.title');
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
            this.objSearch = this.dataSession.searchVoucher != null ? JSON.parse(this.dataSession.searchVoucher) : {};
            this.statusRecord = this.objSearch.statusRecorded;
            sessionStorage.removeItem('dataSession');
        }
        if (this.activatedRoute.snapshot.paramMap.has('isSearch')) {
            this.objSearch.statusRecorded = this.statusRecord;
            this.gOtherVoucherService
                .searchAll({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    searchVoucher: this.dataSession.searchVoucher
                })
                .subscribe(
                    (res: HttpResponse<IGOtherVoucher[]>) => this.paginateGOtherVouchers(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            this.isShowSearch = true;
            return;
        } else {
            // this.router.navigate(['/g-other-voucher'], {
            //     queryParams: {
            //         page: this.page,
            //         size: this.itemsPerPage,
            //         sort: this.predicate + ',' + (this.reverse ? 'desc' : 'asc')
            //     }
            // });
            this.router.navigate(['/g-other-voucher']);
        }
        this.gOtherVoucherService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                searchVoucher: JSON.stringify(this.saveSearchVoucher())
            })
            .subscribe(
                (res: HttpResponse<IGOtherVoucher[]>) => this.paginateGOtherVouchers(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.dataSession.rowNum = 0;
        this.router.navigate(['/g-other-voucher']);
        // this.router.navigate(['/g-other-voucher'], {
        //     queryParams: {
        //         page: this.page,
        //         size: this.itemsPerPage,
        //         sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        //     }
        // });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/g-other-voucher',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'desc' : 'asc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.isShowSearch = false;
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
        this.isNavigateForm = true;
        this.index = 0;
        this.isRecorded = false;
        this.isUnRecorded = false;
        this.iMutipleRecord = {};
        this.statusRecordAfterFind = null;
        this.reSearchWhenRecord = '{}';
        this.isFocusCurrentVoucher = true;
        this.gOtherVoucherDetailExpenses = [];
        this.gOtherVoucherDetailExpenseAllocations = [];
        this.loadAll();
        this.recorded = null;
        this.mutipleRowSelected = [];
        this.registerChangeInGOtherVouchers();
        this.prepaidExpenseService.getPrepaidExpenses().subscribe((res: HttpResponse<IPrepaidExpense[]>) => {
            this.prepaidExpenses = res.body;
        });
        this.chiPhiTraTruocService.getPrepaidExpenseCode().subscribe(ref => {
            this.prepaidExpenseCodeList = ref.body;
        });
        this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObject = res.body.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
        });
        this.statisticsCodeService.getAllStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticsCodes = res.body;
        });
        this.bankAccountDetailsService.getBankAccountDetails().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
            this.bankAccountDetails = res.body;
        });
        this.costSetService.getAllCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
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
        this.goodsServicePurchaseService.getGoodServicePurchases().subscribe((res: HttpResponse<IGoodsServicePurchase[]>) => {
            this.goodsServicePurchases = res.body;
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (account) {
                this.isRolePhanBoChiPhiTraTruoc = this.currentAccount.authorities.includes(this.ROLE_PHAN_BO_CHI_PHI_TRA_TRUOC);
                this.isRoleKetChuyenLaiLo = this.currentAccount.authorities.includes(this.ROLE_KET_CHUYEN_LAI_LO);
                if (account.organizationUnit.taxCalculationMethod === 0) {
                    this.isShowGoodsServicePurchase = true;
                } else {
                    this.isShowGoodsServicePurchase = false;
                }
                this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                this.color = this.currentAccount.systemOption.find(x => x.code === TCKHAC_MauCTuChuaGS && x.data).data;
            }
            this.currencyService.findAllActive().subscribe(res => {
                this.currencies = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                this.currency = this.currencies.find(cur => cur.currencyCode === this.currentAccount.organizationUnit.currencyID);
            });
            this.DDSo_NCachHangNghin = this.currentAccount.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
            this.DDSo_NCachHangDVi = this.currentAccount.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;
            this.DDSo_NgoaiTe = this.currentAccount.systemOption.find(x => x.code === DDSo_NgoaiTe && x.data).data;
            this.DDSo_TienVND = this.currentAccount.systemOption.find(x => x.code === DDSo_TienVND && x.data).data;
        });
        this.typeService.getAllTypes().subscribe((res2: HttpResponse<IType[]>) => {
            this.types = res2.body.filter(iType => iType.typeGroupID === 70);
        });
        this.isKeyPressed = false;
        this.registerExport();
        this.registerLockSuccess();
        this.registerUnlockSuccess();
    }
    //
    // ngOnDestroy() {
    //     this.eventManager.destroy(this.eventSubscriber);
    // }

    trackId(index: number, item: IGOtherVoucher) {
        return item.id;
    }

    registerChangeInGOtherVouchers() {
        this.eventSubscriber = this.eventManager.subscribe('gOtherVoucherListModification', response => {
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

    private paginateGOtherVouchers(data: IGOtherVoucher[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        console.log(headers);
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.gOtherVouchers = data;
        this.objects = data;
        if (data && data.length === 0 && this.page > 1) {
            this.page = this.page - 1;
            this.loadAll();
            this.router.navigate(['/g-other-voucher'], {
                queryParams: {
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
            });
        }
        //  load first element
        if (this.objects && this.objects.length > 0) {
            if (this.dataSession && this.dataSession.rowNum) {
                this.selectedRows.push(this.gOtherVouchers[this.dataSession.rowNum]);
                this.selectedRow = this.gOtherVouchers[this.dataSession.rowNum];
            } else {
                this.selectedRows.push(this.gOtherVouchers[0]);
                this.selectedRow = this.gOtherVouchers[0];
            }
        } else {
            this.selectedRow = null;
            this.gOtherVoucherDetails = null;
            this.gOtherVoucherDetailTax = null;
            this.viewVouchers = null;
        }
        this.onSelect(this.selectedRow);
        this.checkMultiButton(false);
        //  display pageCount
        sessionStorage.removeItem('dataSearchGOtherVoucher');
        this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
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
        this.statusRecordAfterFind = this.objSearch.statusRecorded;
        sessionStorage.setItem('dataSearchGOtherVoucher', JSON.stringify(this.saveSearchVoucher()));
        this.gOtherVoucherService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                searchVoucher: JSON.stringify(this.saveSearchVoucher())
            })
            .subscribe(
                (res: HttpResponse<IGOtherVoucher[]>) => this.paginateGOtherVouchers(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        if (this.objSearch.statusRecorded) {
            this.isRecorded = true;
        } else {
            this.isUnRecorded = true;
        }
        this.reSearchWhenRecord = JSON.stringify(this.saveSearchVoucher());
        this.page = 1;
        this.loadPage(this.page);
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    clearSearch() {
        this.objSearch = {};
        this.objSearch.fromDate = null;
        this.objSearch.toDate = null;
        this.statusRecord = null;
        this.gOtherVoucherService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                searchVoucher: JSON.stringify(this.saveSearchVoucher())
            })
            .subscribe(
                (res: HttpResponse<IGOtherVoucher[]>) => this.paginateGOtherVouchers(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    selectedItemPerPage() {
        this.loadAll();
    }

    onSelect(select: IGOtherVoucher) {
        event.preventDefault();
        this.selectedRow = select;
        this.index = this.gOtherVouchers.indexOf(this.selectedRow);
        console.log(this.index);
        this.rowNum = this.getRowNumberOfRecord(this.page, this.index);
        switch (this.selectedRow.typeID) {
            case this.TYPE_CHUNG_TU_NGHIEP_VU_KHAC:
            case this.TYPE_KET_CHUYEN_LAI_LO:
            case this.TYPE_NGHIEM_THU_CONG_TRINH_DON_HANG_HOP_DONG:
                this.gOtherVoucherService.find(this.selectedRow.id).subscribe((res: HttpResponse<IGOtherVoucher>) => {
                    this.gOtherVoucherDetails =
                        res.body.gOtherVoucherDetails === undefined
                            ? []
                            : res.body.gOtherVoucherDetails.sort((a, b) => a.orderPriority - b.orderPriority);
                    this.gOtherVoucherDetailTax =
                        res.body.gOtherVoucherDetailTax === undefined
                            ? []
                            : res.body.gOtherVoucherDetailTax.sort((a, b) => a.orderPriority - b.orderPriority);
                    this.viewVouchers = res.body.viewVouchers;
                });
                break;
            case this.TYPE_PHAN_BO_CHI_PHI_TRA_TRUOC:
                this.gOtherVoucherService.find(this.selectedRow.id).subscribe((res: HttpResponse<IGOtherVoucher>) => {
                    this.gOtherVoucherDetails =
                        res.body.gOtherVoucherDetails === undefined
                            ? []
                            : res.body.gOtherVoucherDetails.sort((a, b) => a.orderPriority - b.orderPriority);
                    this.gOtherVoucherDetailExpenses =
                        res.body.gOtherVoucherDetailExpenses === undefined
                            ? []
                            : res.body.gOtherVoucherDetailExpenses.sort((a, b) => a.orderPriority - b.orderPriority);
                    this.gOtherVoucherDetailExpenseAllocations =
                        res.body.gOtherVoucherDetailExpenseAllocations === undefined
                            ? []
                            : res.body.gOtherVoucherDetailExpenseAllocations.sort((a, b) => a.orderPriority - b.orderPriority);
                    this.viewVouchers = res.body.viewVouchers;
                });
                break;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuNghiepVuKhac_KetXuat])
    export() {
        event.preventDefault();
        if (this.gOtherVouchers && this.gOtherVouchers.length > 0) {
            this.gOtherVoucherService
                .exportPDF({
                    searchVoucher: this.reSearchWhenRecord
                })
                .subscribe(res => {
                    this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.TYPE_CHUNG_TU_NGHIEP_VU_KHAC);
                });
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuNghiepVuKhac_GhiSo])
    record() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 2;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (this.selectedRow && !this.selectedRow.recorded && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
                this.dataSession.rowNum = this.gOtherVouchers.indexOf(this.selectedRow);
                this.isUnRecorded = true;
                this.isRecorded = false;
                this._record();
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuNghiepVuKhac_GhiSo])
    unrecord() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 1;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (this.selectedRow && this.selectedRow.recorded && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
                this.dataSession.rowNum = this.gOtherVouchers.indexOf(this.selectedRow);
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
            this.gLService.record(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                console.log(JSON.stringify(res.body));
                if (res.body.success) {
                    this.selectedRow.recorded = true;
                    this.toastr.success(
                        this.translate.instant('ebwebApp.mBDeposit.recordSuccess'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                    this.toastr.error(
                        this.translate.instant('global.messages.error.checkTonQuyQT'),
                        this.translate.instant('ebwebApp.mCReceipt.error.error')
                    );
                } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                    this.toastr.error(
                        this.translate.instant('global.messages.error.checkTonQuyTC'),
                        this.translate.instant('ebwebApp.mCReceipt.error.error')
                    );
                } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                    this.toastr.error(
                        this.translate.instant('global.messages.error.checkTonQuy'),
                        this.translate.instant('ebwebApp.mCReceipt.error.error')
                    );
                }
                this.searchAfterChangeRecord();
            });
            if (this.statusRecordAfterFind === null || this.statusRecordAfterFind === undefined) {
                this.isRecorded = false;
                this.isUnRecorded = true;
            } else {
                this.isRecorded = !this.statusRecordAfterFind;
            }
        } else if (this.selectedRow.recorded) {
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
            if (this.statusRecordAfterFind === null || this.statusRecordAfterFind === undefined) {
                this.isUnRecorded = true;
                this.isRecorded = false;
            } else {
                this.isUnRecorded = !this.statusRecordAfterFind;
            }
        } else {
            this.toastr.error('ebwebApp.mBDeposit.error', 'ebwebApp.mBDeposit.message');
        }
        this.selectedRows = [];
    }

    searchAfterChangeRecord() {
        if (this.reSearchWhenRecord) {
            this.gOtherVoucherService
                .searchAll({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    searchVoucher: this.reSearchWhenRecord
                })
                .subscribe(
                    (res: HttpResponse<IGOtherVoucher[]>) => this.paginateGOtherVouchers(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    doubleClickRow(any?) {
        this.edit();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuNghiepVuKhac_Sua])
    edit() {
        event.preventDefault();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.pageCount = this.pageCount;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.gOtherVouchers.indexOf(this.selectedRow);
        this.dataSession.isShowSearch = this.isShowSearch;
        this.dataSession.searchVoucher = JSON.stringify(this.saveSearchVoucher());
        this.dataSession.predicate = this.predicate;
        this.dataSession.accountingObjectName = this.accountingObjectName;
        this.dataSession.reverse = this.reverse;
        sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
        if (this.isNavigateForm) {
            switch (this.selectedRow.typeID) {
                case this.TYPE_CHUNG_TU_NGHIEP_VU_KHAC:
                case this.TYPE_NGHIEM_THU_CONG_TRINH_DON_HANG_HOP_DONG:
                    this.router.navigate(['./g-other-voucher', this.selectedRow.id, 'edit']);
                    break;
                case this.TYPE_KET_CHUYEN_LAI_LO:
                    if (this.isRoleKetChuyenLaiLo || this.currentAccount.authorities.includes('ROLE_ADMIN')) {
                        this.router.navigate(['./ket-chuyen-lai-lo', this.selectedRow.id, 'edit', 'from-g-other-voucher']);
                    } else {
                        this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
                    }
                    break;
                case this.TYPE_PHAN_BO_CHI_PHI_TRA_TRUOC:
                    if (this.isRolePhanBoChiPhiTraTruoc || this.currentAccount.authorities.includes('ROLE_ADMIN')) {
                        this.router.navigate(['./phan-bo-chi-phi-tra-truoc', this.selectedRow.id, 'edit', 'from-g-other-voucher']);
                    } else {
                        this.toastr.error(this.translate.instant('ebwebApp.quyTrinh.notHasAccess'));
                    }
                    break;
            }
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
        return searchObj;
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
        if (this.accountingObject) {
            const acc = this.accountingObject.find(n => n.id === id);
            if (acc) {
                return acc.accountingObjectCode;
            }
        }
    }

    getBankAccountDetailbyID(id) {
        if (this.bankAccountDetails) {
            const ibank = this.bankAccountDetails.find(n => n.id === id);
            if (ibank) {
                return ibank.bankAccount;
            }
        }
    }

    getEmployeeByID(id) {
        if (this.accountingObject) {
            const acc = this.accountingObject.find(n => n.id === id);
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

    getGoodsServicePurchasebyID(id) {
        if (this.goodsServicePurchases) {
            const iGoodsServicePurchase = this.goodsServicePurchases.find(n => n.id === id);
            if (iGoodsServicePurchase) {
                return iGoodsServicePurchase.goodsServicePurchaseCode;
            }
        }
    }

    cssMultipleRow(igOtherVoucher: IGOtherVoucher) {
        if (this.mutipleRowSelected) {
            if (this.mutipleRowSelected.length > 0) {
                for (let i = 0; i < this.mutipleRowSelected.length; i++) {
                    if (this.mutipleRowSelected[i] === igOtherVoucher) {
                        return this.mutipleRowSelected[i];
                    }
                }
            }
        }
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.TYPE_CHUNG_TU_NGHIEP_VU_KHAC}`, () => {
            this.exportExcel();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    exportExcel() {
        this.gOtherVoucherService.exportExcel({ searchVoucher: this.reSearchWhenRecord }).subscribe(
            res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_ChungTuNghiepVuKhac.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            },
            () => {}
        );
    }

    getTypeName(typeID) {
        if (typeID && this.types) {
            const currentType = this.types.find(iType => iType.id === typeID);
            {
                if (currentType) {
                    return currentType.typeName;
                }
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

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuNghiepVuKhac_Them])
    addNew(isNew = false) {
        event.preventDefault();
        this.router.navigate(['g-other-voucher/', 'new']);
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

    getSumDetails(prop) {
        let sum = 0;
        if (this.gOtherVoucherDetails) {
            for (let i = 0; i < this.gOtherVoucherDetails.length; i++) {
                sum += this.gOtherVoucherDetails[i][prop];
            }
        }
        return sum;
    }

    getVATRate(vAT) {
        if (vAT === 0) {
            return '0%';
        } else if (vAT === 1) {
            return '5%';
        } else if (vAT === 2) {
            return '10%';
        } else if (vAT === -1) {
            return 'Không chịu thuế';
        } else if (vAT === -2) {
            return 'Không tính thuế';
        } else {
            return '';
        }
    }

    getSumDetailTax(prop) {
        let sum = 0;
        if (this.gOtherVoucherDetailTax) {
            for (let i = 0; i < this.gOtherVoucherDetailTax.length; i++) {
                sum += this.gOtherVoucherDetailTax[i][prop];
            }
        }
        return sum;
    }

    getPrepaidExpenseCode(id) {
        if (id && this.prepaidExpenses) {
            const prepaidExpense = this.prepaidExpenses.find(a => a.id === id);
            if (prepaidExpense) {
                return prepaidExpense.prepaidExpenseCode;
            }
        } else {
            return '';
        }
    }

    getPrepaidExpenseName(id) {
        if (id && this.prepaidExpenses) {
            const prepaidExpense = this.prepaidExpenses.find(a => a.id === id);
            if (prepaidExpense) {
                return prepaidExpense.prepaidExpenseName;
            }
        } else {
            return '';
        }
    }

    getObjectCode(id) {
        if (id && this.prepaidExpenseCodeList) {
            const prepaidExpense = this.prepaidExpenseCodeList.find(a => a.id === id);
            if (prepaidExpense) {
                return prepaidExpense.code;
            }
        } else {
            return '';
        }
    }

    getSumDetailExpenses(prop) {
        let sum = 0;
        if (this.gOtherVoucherDetailExpenses) {
            for (let i = 0; i < this.gOtherVoucherDetailExpenses.length; i++) {
                sum += this.gOtherVoucherDetailExpenses[i][prop];
            }
        }
        return sum;
    }

    getSumDetailAllocationExpenses(prop) {
        let sum = 0;
        if (this.gOtherVoucherDetailExpenseAllocations) {
            for (let i = 0; i < this.gOtherVoucherDetailExpenseAllocations.length; i++) {
                sum += this.gOtherVoucherDetailExpenseAllocations[i][prop];
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
        if (this.selectedRows.length > 0) {
            for (let i = 0; i < this.selectedRows.length; i++) {
                if (this.selectedRows && this.selectedRows[i] && this.selectedRows[i].recorded === isRecord) {
                    return true;
                }
            }
        }
        return false;
    }

    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1 && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
            this.typeMultiAction = 0;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (this.selectedRow && !this.selectedRow.recorded) {
                this.dataSession.rowNum = this.gOtherVouchers.indexOf(this.selectedRow);
                if (this.selectedRow.typeID === 709) {
                    this.phanBoChiPhiTraTruocService.getMaxMonth(this.selectedRow.id).subscribe(res => {
                        if (moment(this.selectedRow.postedDate).isSame(moment(res.body))) {
                            if (this.modalRef) {
                                this.modalRef.close();
                            }
                            this.router.navigate(['/g-other-voucher', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
                        } else {
                            this.toastr.error(
                                this.translate.instant('ebwebApp.gOtherVoucher.error.checkDeletePB'),
                                this.translate.instant('ebwebApp.mBDeposit.message')
                            );
                            return;
                        }
                    });
                } else {
                    this.router.navigate(['/g-other-voucher', { outlets: { popup: this.selectedRow.id + '/delete' } }]);
                }
            }
        }
    }

    continueDelete() {
        if (this.typeMultiAction === 0) {
            this.gOtherVoucherService.multiDelete(this.selectedRows).subscribe(
                (res: HttpResponse<any>) => {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
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
            this.gOtherVoucherService.multiUnRecord(this.selectedRows).subscribe(
                (res: HttpResponse<any>) => {
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
            const listRecord: RequestRecordListDtoModel = {};
            listRecord.typeIDMain = TypeID.CHUNG_TU_NGHIEP_VU_KHAC;
            listRecord.records = [];
            this.selectedRows.forEach(item => {
                listRecord.records.push({
                    id: item.id,
                    typeID: item.typeID
                });
            });
            this.gLService.recordList(listRecord).subscribe(
                (res: HttpResponse<any>) => {
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
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    this.activeModal.close();
                    this.loadAll();
                    // }
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

    closePopUpDelete() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }
}
