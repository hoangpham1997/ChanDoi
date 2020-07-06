import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { ISAInvoice } from 'app/shared/model/sa-invoice.model';
import { Principal } from 'app/core';
import { DATE_FORMAT, DATE_FORMAT_SEARCH, ITEMS_PER_PAGE } from 'app/shared';
import { SAInvoiceService } from './sa-invoice.service';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { CurrencyService } from 'app/danhmuc/currency';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { DatePipe } from '@angular/common';
import { ISAInvoiceDetails } from 'app/shared/model/sa-invoice-details.model';
import { DataSessionStorage, IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { SAInvoiceDetailsService } from 'app/ban-hang/ban_hang_chua_thu_tien/sa-invoice-details.service';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { ExpenseItemService } from 'app/entities/expense-item';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { EMContractService } from 'app/entities/em-contract';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { CostSetService } from 'app/entities/cost-set';
import { BudgetItemService } from 'app/entities/budget-item';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { IRepository } from 'app/shared/model/repository.model';
import { RepositoryService } from 'app/danhmuc/repository';
import { IType } from 'app/shared/model/type.model';
import { TypeService } from 'app/entities/type';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { GROUP_TYPEID, HH_XUATQUASLTON, MAU_BO_GHI_SO, MSGERROR, TCKHAC_SDTichHopHDDT, TypeID } from 'app/app.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { UnitService } from 'app/danhmuc/unit';
import { IUnit } from 'app/shared/model/unit.model';
import * as moment from 'moment';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { ICareerGroup } from 'app/shared/model/career-group.model';
import { CareerGroupService } from 'app/entities/career-group';
import { TypeGroup } from 'app/shared/model/type-group.model';

@Component({
    selector: 'eb-sa-invoice',
    templateUrl: './sa-invoice.component.html',
    styleUrls: ['./sa-invoice.css'],
    providers: [DatePipe]
})
export class SAInvoiceComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('contentUnRecord') contentUnRecord;
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    @ViewChild('deleteItem') deleteItem;
    sAInvoices: ISAInvoice[];
    sAInvoiceDetails: ISAInvoiceDetails[];
    currentAccount: any;
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
    recorded: null;
    typeID: number;
    index: number;
    public isShowSearch: any;
    currencies: ICurrency[];
    accountingObjectID: any;
    currencyID: any;
    fromDate: any;
    toDate: any;
    keySearch: any;
    listRecord: any;
    listColumnsRecord: string[] = ['name'];
    listHeaderColumnsRecord: string[] = ['Trạng thái'];
    currencyCode: string;
    public pageCount: any;
    accountingObjects: IAccountingObject[];
    materialGoods: IMaterialGoods[];
    repositories: IRepository[];
    costSets: ICostSet[];
    expenseItems: IExpenseItem[];
    organizationUnits: IOrganizationUnit[];
    eMContracts: IEMContract[];
    budgetItem: IBudgetItem[];
    statisticsCodes: IStatisticsCode[];
    units: IUnit[];
    types: IType[];
    typeNotify: number;
    checkSLT: boolean;
    mgForPPOderTextCode: any;
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
    selectedRow: ISAInvoice;
    rowNum: number;
    sAInvoice: ISAInvoice;
    record_: Irecord;
    searchData: any;
    refVoucher: any;
    hiddenVAT: boolean;
    modalRef: NgbModalRef;
    ROLE_Them = ROLE.ChungTuBanHang_Them;
    ROLE_GhiSo = ROLE.ChungTuBanHang_GhiSo;
    ROLE_Xoa = ROLE.ChungTuBanHang_Xoa;
    ROLE_KetXuat = ROLE.ChungTuBanHang_KetXuat;
    typeMultiAction: number;
    typeDelete: number;
    isRequiredInvoiceNo: boolean;
    listVAT = [
        { name: '0%', data: 0 },
        { name: '5%', data: 1 },
        { name: '10%', data: 2 },
        { name: 'KCT', data: 3 },
        { name: 'KTT', data: 4 }
    ];
    careerGroups: ICareerGroup[];

    constructor(
        private toasService: ToastrService,
        private modalService: NgbModal,
        private translateService: TranslateService,
        private gLService: GeneralLedgerService,
        private sAInvoiceService: SAInvoiceService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private typeService: TypeService,
        private router: Router,
        public translate: TranslateService,
        public utilService: UtilsService,
        private sAInvoiceDetailsService: SAInvoiceDetailsService,
        private eventManager: JhiEventManager,
        private accountingObjectService: AccountingObjectService,
        private currencyService: CurrencyService,
        private expenseItemsService: ExpenseItemService,
        private statisticsCodeService: StatisticsCodeService,
        private eMContractService: EMContractService,
        private organizationUnitService: OrganizationUnitService,
        private costSetService: CostSetService,
        private budgetItemService: BudgetItemService,
        private materialGoodsService: MaterialGoodsService,
        private repositoryService: RepositoryService,
        private refModalService: RefModalService,
        private unitService: UnitService,
        public activeModal: NgbActiveModal,
        private careerGroupService: CareerGroupService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.translateService.get(['ebwebApp.mBDeposit.home.recorded', 'ebwebApp.mBDeposit.home.unrecorded']).subscribe(res => {
            this.listRecord = [
                { value: 1, name: res['ebwebApp.mBDeposit.home.recorded'] },
                { value: 0, name: res['ebwebApp.mBDeposit.home.unrecorded'] }
            ];
        });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition(true);
        }
    }

    transition(isTransition) {
        if (isTransition) {
            this.selectedRow = {};
        }
        this.router.navigate(['chung-tu-ban-hang/'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.search();
    }

    ngOnInit() {
        this.accountingObjectService.getAllAccountingObjectDTO({ isObjectType12: true }).subscribe(res => {
            this.accountingObjects = res.body;
        });
        this.repositoryService.getRepositoryCombobox().subscribe(res => {
            this.repositories = res.body;
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
        this.unitService.getAllUnits().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
        });
        this.careerGroupService.getCareerGroups().subscribe((res: HttpResponse<ICareerGroup[]>) => {
            this.careerGroups = res.body;
        });
        this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body
                .filter(type => type.typeGroupID === GROUP_TYPEID.GROUP_SAINVOICE && type.id !== TypeID.BANG_KE_HANG_HOA_DICH_VU)
                .sort((a, b) => a.typeName.localeCompare(b.typeName));
        });
        this.sAInvoiceDetails = [];
        this.keySearch = '';
        this.recorded = null;
        this.currencyID = null;
        this.accountingObjectID = null;
        this.isShowSearch = false;
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencies = res.body;
        });

        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
                this.materialGoodsService
                    .getAllMaterialGoodsDTO({ companyID: this.currentAccount.organizationUnit.id })
                    .subscribe((res: HttpResponse<IMaterialGoods[]>) => {
                        this.materialGoods = res.body;
                    });
                this.currencyCode = account.organizationUnit.currencyID;
                this.page = data.pagingParams.page;
                this.previousPage = data.pagingParams.page;
                this.reverse = data.pagingParams.ascending;
                this.color = this.currentAccount.systemOption.find(item => item.code === MAU_BO_GHI_SO).data;
                this.checkSLT = this.currentAccount.systemOption.find(x => x.code === HH_XUATQUASLTON).data === '1';
                this.isRequiredInvoiceNo = this.currentAccount.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '0');
                this.hiddenVAT = this.currentAccount.organizationUnit.taxCalculationMethod === 1;
                this.registerChangeInSAInvoices();
                this.registerExport();
                this.getSessionData();
            });
        });
        this.registerLockSuccess();
        this.registerUnlockSuccess();
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('dataSearchSAInvoice'));

        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.searchData = JSON.parse(this.dataSession.searchVoucher);
            if (this.searchData) {
                this.currencyID = this.searchData.currency;
                this.fromDate = this.searchData.fromDate ? moment(this.searchData.fromDate, DATE_FORMAT) : '';
                this.toDate = this.searchData.toDate ? moment(this.searchData.toDate, DATE_FORMAT) : '';
                this.recorded = this.searchData.status;
                this.accountingObjectID = this.searchData.accountingObject;
                this.keySearch = this.searchData.keySearch;
                this.typeID = this.searchData.typeID;
            }
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
            this.isShowSearch = this.dataSession.isShowSearch;
        }
        sessionStorage.removeItem('dataSearchSAInvoice');
        this.transition(false);
    }

    clearSearch() {
        this.keySearch = null;
        this.recorded = null;
        this.currencyID = null;
        this.accountingObjectID = null;
        this.fromDate = null;
        this.toDate = null;
        this.typeID = null;
        this.search();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISAInvoice) {
        return item.id;
    }

    registerChangeInSAInvoices() {
        this.eventSubscriber = this.eventManager.subscribe('sAInvoiceListModification', response => this.search());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateSAInvoice(data: ISAInvoice[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.sAInvoices = data;
        this.objects = data;
        if (this.sAInvoices && this.sAInvoices.length > 0) {
            if (this.rowNum && !this.index) {
                this.index = this.rowNum % this.itemsPerPage;
                this.index = this.index || this.itemsPerPage;
                this.selectedRow = this.sAInvoices[this.index - 1] ? this.sAInvoices[this.index - 1] : {};
                this.selectedRows.push(this.selectedRow);
            } else if (this.index) {
                this.selectedRow = this.sAInvoices[this.index - 1] ? this.sAInvoices[this.index - 1] : {};
                this.selectedRows.push(this.selectedRow);
            } else {
                this.selectedRows.push(this.sAInvoices[0]);
                this.selectedRow = this.sAInvoices[0];
            }
            this.rowNum = this.getRowNumberOfRecord(this.page, 0);
            this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
        } else {
            this.sAInvoiceDetails = [];
            this.refVoucher = [];
            this.selectedRow = {};
        }
        if (this.pageCount > 0 && (!this.sAInvoices || this.sAInvoices.length === 0) && this.page > 1) {
            this.page = this.page - 1;
            this.loadPage(this.page);
        }
    }

    search(index?) {
        if (!this.checkErrorForSearch()) {
            return;
        }
        this.searchData = {
            currency: this.currencyID ? this.currencyID : '',
            fromDate: this.fromDate ? this.fromDate.format('YYYY-MM-DD') : '',
            toDate: this.toDate ? this.toDate.format('YYYY-MM-DD') : '',
            status: this.recorded === 1 || this.recorded === 0 ? this.recorded : '',
            accountingObject: this.accountingObjectID ? this.accountingObjectID : '',
            searchValue: this.keySearch ? this.keySearch : '',
            typeId: this.typeID ? this.typeID : ''
        };
        this.sAInvoiceService
            .searchSAInvoice({
                accountingObjectID: this.searchData.accountingObject,
                currencyID: this.searchData.currency,
                fromDate: this.searchData.fromDate,
                toDate: this.searchData.toDate,
                status: this.searchData.status,
                keySearch: this.searchData.searchValue,
                typeId: this.searchData.typeId,
                page: this.page - 1,
                size: this.itemsPerPage
            })
            .subscribe(res => {
                if (index >= 0) {
                    this.selectedRow = {};
                    this.selectedRows = [];
                }
                this.paginateSAInvoice(res.body, res.headers);
                if (!this.selectedRow && res.body) {
                    this.onSelect(res.body[0]);
                } else {
                    const select = res.body.find(n => n.id === this.selectedRow.id);
                    if (!select) {
                        this.onSelect(res.body[0]);
                    } else if (index) {
                        this.onSelect(res.body[index]);
                    } else {
                        this.onSelect(select);
                    }
                }
            });
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    checkErrorForSearch() {
        if (this.toDate && this.fromDate && moment(this.toDate, DATE_FORMAT_SEARCH) < moment(this.fromDate, DATE_FORMAT_SEARCH)) {
            this.toasService.error(
                this.translate.instant('ebwebApp.mCReceipt.error.fromDateGreaterToDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        return true;
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    // clickShowDetail(saInvoice: ISAInvoice, index: number) {
    //     this.selectedRow = saInvoice;
    //     this.index = index + 1;
    //     this.onSelect(saInvoice);
    // }

    selectedItemPerPage() {
        this.search();
    }

    onSelect(item) {
        this.selectedRow = item;
        this.sAInvoiceDetailsService.getDetailByID({ sAInvoiceID: this.selectedRow.id }).subscribe(ref => {
            this.sAInvoiceDetails = ref.body.sort((n1, n2) => {
                return n1.orderPriority - n2.orderPriority;
            });
        });
        this.sAInvoiceService.getRefVouchersBySAInvoiceID(this.selectedRow.id).subscribe(ref => {
            this.refVoucher = ref.body;
        });
    }

    doubleClickRow(item: ISAInvoice) {
        event.preventDefault();
        this.saveSearchData(item, this.sAInvoices.indexOf(item));
        this.router.navigate(['chung-tu-ban-hang/', item.id, 'edit', this.rowNum]);
    }

    saveSearchData(item: ISAInvoice, index: number) {
        this.selectedRow = item;
        this.searchData = {
            currency: this.currencyID ? this.currencyID : '',
            fromDate: this.fromDate ? this.fromDate.format('YYYY-MM-DD') : '',
            toDate: this.toDate ? this.toDate.format('YYYY-MM-DD') : '',
            status: this.recorded === 1 || this.recorded === 0 ? this.recorded : '',
            accountingObject: this.accountingObjectID ? this.accountingObjectID : '',
            searchValue: this.keySearch ? this.keySearch : '',
            typeId: this.typeID ? this.typeID : ''
        };
        this.rowNum = this.getRowNumberOfRecord(this.page, index);
        this.dataSession = new DataSessionStorage();
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.pageCount = this.pageCount;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.rowNum = this.rowNum;
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        this.dataSession.isShowSearch = this.isShowSearch;
        this.dataSession.searchVoucher = JSON.stringify(this.searchData);
        sessionStorage.setItem('dataSearchSAInvoice', JSON.stringify(this.dataSession));
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuBanHang_Them])
    addNew($event) {
        this.router.navigate(['chung-tu-ban-hang/new']);
    }

    edit() {
        event.preventDefault();
        this.doubleClickRow(this.selectedRow);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuBanHang_GhiSo])
    record() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 2;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (!this.selectedRow.recorded && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
                this.utilService
                    .checkQuantityExistsTest1(this.sAInvoiceDetails, this.currentAccount, this.selectedRow.postedDate)
                    .then(data => {
                        if (data) {
                            this.mgForPPOderTextCode = data;
                            if (!this.checkSLT && this.mgForPPOderTextCode.quantityExists && this.selectedRow.rsInwardOutwardID) {
                                this.toasService.error(
                                    this.translateService.instant('ebwebApp.pPDiscountReturnDetails.error.quantityExistsRecordErrorSave'),
                                    this.translateService.instant('ebwebApp.mBDeposit.message')
                                );
                                return;
                            }
                            this.record_ = {};
                            this.record_.id = this.selectedRow.id;
                            this.record_.typeID = this.selectedRow.typeID;
                            this.record_.repositoryLedgerID = this.selectedRow.id;
                            this.gLService.record(this.record_).subscribe(
                                (res: HttpResponse<Irecord>) => {
                                    if (res.body.success) {
                                        this.selectedRow.recorded = true;
                                        this.toasService.success(
                                            this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
                                            this.translate.instant('ebwebApp.mBDeposit.message')
                                        );
                                    } else if (res.body.msg === MSGERROR.XUAT_QUA_SO_TON) {
                                        this.toasService.error(
                                            this.translateService.instant('global.messages.error.checkTonSoLuong'),
                                            this.translateService.instant('ebwebApp.mCReceipt.error.error')
                                        );
                                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                                        this.toasService.error(
                                            this.translateService.instant('global.messages.error.checkTonQuy'),
                                            this.translateService.instant('ebwebApp.mCReceipt.error.error')
                                        );
                                    }
                                },
                                (res: HttpErrorResponse) =>
                                    this.toasService.error(
                                        this.translate.instant('global.data.recordFailed'),
                                        this.translate.instant('ebwebApp.mBDeposit.message')
                                    )
                            );
                        }
                    });
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuBanHang_GhiSo])
    unrecord() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 1;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { size: 'lg', backdrop: 'static' });
        } else {
            if (!this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
                this.sAInvoiceService
                    .checkRelateVoucher({
                        sAInvoice: this.selectedRow.id,
                        isCheckKPXK: false
                    })
                    .subscribe((response: HttpResponse<number>) => {
                        this.typeNotify = response.body;
                        if (this.typeNotify === 1 || this.typeNotify === 2 || this.typeNotify === 3) {
                            this.modalRef = this.modalService.open(this.contentUnRecord, { backdrop: 'static' });
                        } else {
                            this.record_ = {};
                            this.record_.id = this.selectedRow.id;
                            this.record_.typeID = this.selectedRow.typeID;
                            this.record_.repositoryLedgerID = this.selectedRow.rsInwardOutwardID;
                            this.gLService.unrecord(this.record_).subscribe(
                                (res: HttpResponse<Irecord>) => {
                                    if (res.body.success) {
                                        this.selectedRow.recorded = false;
                                        this.toasService.success(
                                            this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                                            this.translate.instant('ebwebApp.mBDeposit.message')
                                        );
                                    }
                                },
                                (res: HttpErrorResponse) =>
                                    this.toasService.error(
                                        this.translate.instant('global.data.unRecordFailed'),
                                        this.translate.instant('ebwebApp.mBDeposit.message')
                                    )
                            );
                        }
                    });
            }
        }
    }

    closes() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    unrecordForm() {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.selectedRow.recorded) {
            this.record_ = {};
            this.record_.id = this.selectedRow.id;
            this.record_.typeID = this.selectedRow.typeID;
            this.record_.repositoryLedgerID = this.selectedRow.rsInwardOutwardID;
            this.gLService.unrecord(this.record_).subscribe(
                (res: HttpResponse<Irecord>) => {
                    if (res.body.success) {
                        this.selectedRow.recorded = false;
                        this.toasService.success(
                            this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                },
                (res: HttpErrorResponse) =>
                    this.toasService.error(
                        this.translate.instant('global.data.unRecordFailed'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    )
            );
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuBanHang_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = 0;
            this.modalRef = this.modalService.open(this.popUpMultiDelete, { size: 'lg', backdrop: 'static' });
        } else {
            if (!this.selectedRow.recorded && !this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)) {
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
                this.modalRef = this.modalService.open(this.deleteItem, { backdrop: 'static' });
            }
        }
    }

    deleteForm() {
        if (this.selectedRow) {
            if (this.typeDelete === 2) {
                this.toasService.error(this.translateService.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
            } else {
                const index = this.sAInvoices.indexOf(this.selectedRow);
                this.sAInvoiceService.delete(this.selectedRow.id).subscribe(
                    ref => {
                        this.toasService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
                        this.search(index);
                        this.modalRef.close();
                    },
                    ref => {
                        this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.error'));
                    }
                );
            }
        }
    }

    continueMultiAction() {
        if (this.typeMultiAction === 0) {
            this.sAInvoiceService.multiDelete(this.selectedRows).subscribe(
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
                        this.toasService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
                    }
                    this.activeModal.close();
                    this.search();
                    // }
                },
                (res: HttpErrorResponse) => {
                    if (res.error.errorKey === 'errorDeleteList') {
                        this.toasService.error(
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
            this.sAInvoiceService.multiUnrecord(this.selectedRows).subscribe(
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
                        this.toasService.success(
                            this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    this.activeModal.close();
                    this.search();
                    // }
                },
                (res: HttpErrorResponse) => {
                    this.toasService.error(
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
            listRecord.typeIDMain = TypeID.BAN_HANG_CHUA_THU_TIEN;
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
                        this.toasService.success(
                            this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    this.activeModal.close();
                    this.search();
                    // }
                },
                (res: HttpErrorResponse) => {
                    this.toasService.error(
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

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    getRepositoryByID(id) {
        if (this.repositories && id) {
            const repository = this.repositories.find(n => n.id === id.toLowerCase());
            if (repository) {
                return repository.repositoryCode;
            }
        }
    }

    getUnitNameByID(id) {
        if (this.units && id) {
            const unit = this.units.find(n => n.id === id);
            if (unit) {
                return unit.unitName;
            }
        }
    }

    getAccountingObjectCodeByID(id) {
        if (this.accountingObjects && id) {
            const unit = this.accountingObjects.find(n => n.id === id);
            if (unit) {
                return unit.accountingObjectCode;
            }
        }
    }

    getMaterialGoodsByID(id) {
        if (this.materialGoods && id) {
            const materialGood = this.materialGoods.find(n => n.id === id);
            if (materialGood) {
                return materialGood.materialGoodsCode;
            }
        }
    }

    getEMContractsbyID(id) {
        if (this.eMContracts && id) {
            const eMC = this.eMContracts.find(n => n.id === id.toLowerCase());
            if (eMC) {
                return eMC.noMBook;
            }
        }
    }

    getCareerGroupID(id) {
        if (this.careerGroups && id) {
            const ca = this.careerGroups.find(n => n.id === id.toLowerCase());
            if (ca) {
                return ca.careerGroupCode;
            }
        }
    }

    getAccountingObjectbyID(id) {
        if (this.accountingObjects && id) {
            const acc = this.accountingObjects.find(n => n.id === id.toLowerCase());
            if (acc) {
                return acc.accountingObjectCode;
            }
        }
    }

    getStatisticsCodebyID(id) {
        if (this.statisticsCodes && id) {
            const statisticsCode = this.statisticsCodes.find(n => n.id === id.toLowerCase());
            if (statisticsCode) {
                return statisticsCode.statisticsCode;
            }
        }
    }

    getCostSetbyID(id) {
        if (this.costSets && id) {
            const costSet = this.costSets.find(n => n.id === id.toLowerCase());
            if (costSet) {
                return costSet.costSetCode;
            }
        }
    }

    getExpenseItembyID(id) {
        if (this.expenseItems && id) {
            const expenseItem = this.expenseItems.find(n => n.id === id.toLowerCase());
            if (expenseItem) {
                return expenseItem.expenseItemCode;
            }
        }
    }

    getOrganizationUnitbyID(id) {
        if (this.organizationUnits && id) {
            const organizationUnit = this.organizationUnits.find(n => n.id === id.toLowerCase());
            if (organizationUnit) {
                return organizationUnit.organizationUnitCode;
            }
        }
    }

    getBudgetItembyID(id) {
        if (this.budgetItem && id) {
            const iBudgetItem = this.budgetItem.find(n => n.id === id.toLowerCase());
            if (iBudgetItem) {
                return iBudgetItem.budgetItemCode;
            }
        }
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
        for (let i = 0; i < this.sAInvoiceDetails.length; i++) {
            total += this.sAInvoiceDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    exportPdf() {
        this.searchData = {
            currency: this.currencyID ? this.currencyID : '',
            fromDate: this.fromDate ? this.fromDate : '',
            toDate: this.toDate ? this.toDate : '',
            status: this.recorded === 1 || this.recorded === 0 ? this.recorded : '',
            accountingObject: this.accountingObjectID ? this.accountingObjectID : '',
            searchValue: this.keySearch ? this.keySearch : '',
            typeId: this.typeID ? this.typeID : ''
        };
        this.sAInvoiceService
            .export({
                accountingObjectID: this.searchData.accountingObject,
                currencyID: this.searchData.currency,
                fromDate: this.searchData.fromDate,
                toDate: this.searchData.toDate,
                status: this.searchData.status,
                keySearch: this.searchData.searchValue,
                typeId: this.searchData.typeId,
                page: this.page - 1,
                size: this.itemsPerPage
            })
            .subscribe(res => {
                this.refModalService.open(null, EbReportPdfPopupComponent, res, false, TypeID.BAN_HANG_CHUA_THU_TIEN);
            });
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${TypeID.BAN_HANG_CHUA_THU_TIEN}`, () => {
            this.exportExcel();
        });
    }

    exportExcel() {
        this.searchData = {
            currency: this.currencyID ? this.currencyID : '',
            fromDate: this.fromDate ? this.fromDate : '',
            toDate: this.toDate ? this.toDate : '',
            status: this.recorded === 1 || this.recorded === 0 ? this.recorded : '',
            accountingObject: this.accountingObjectID ? this.accountingObjectID : '',
            searchValue: this.keySearch ? this.keySearch : '',
            typeId: this.typeID ? this.typeID : ''
        };
        this.sAInvoiceService
            .exportExcel({
                accountingObjectID: this.searchData.accountingObject,
                currencyID: this.searchData.currency,
                fromDate: this.searchData.fromDate,
                toDate: this.searchData.toDate,
                status: this.searchData.status,
                keySearch: this.searchData.searchValue,
                typeId: this.searchData.typeId,
                page: this.page - 1,
                size: this.itemsPerPage
            })
            .subscribe(res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);
                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_ChungTuBanHang.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            });
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuBanHang_KetXuat])
    export() {
        event.preventDefault();
        this.exportPdf();
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
