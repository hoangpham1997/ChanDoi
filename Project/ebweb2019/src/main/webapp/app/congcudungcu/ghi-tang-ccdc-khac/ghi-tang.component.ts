import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IMCReceipt } from 'app/shared/model/mc-receipt.model';
import { Principal } from 'app/core';

import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';

import { IMCReceiptDetails } from 'app/shared/model/mc-receipt-details.model';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ICurrency } from 'app/shared/model/currency.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { CurrencyService } from 'app/danhmuc/currency';
import { IType } from 'app/shared/model/type.model';
import { TypeService } from 'app/entities/type';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { EMContractService } from 'app/entities/em-contract';
import * as moment from 'moment';
import { HH_XUATQUASLTON, MultiAction, SO_LAM_VIEC, TCKHAC_MauCTuChuaGS, TypeID } from 'app/app.constants';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { ISAInvoiceDetails } from 'app/shared/model/sa-invoice-details.model';
import { IRepository } from 'app/shared/model/repository.model';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { RepositoryService } from 'app/danhmuc/repository';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ExpenseItemService } from 'app/entities/expense-item';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { CostSetService } from 'app/entities/cost-set';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { BudgetItemService } from 'app/entities/budget-item';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { SAInvoiceDetailsService } from 'app/ban-hang/ban_hang_chua_thu_tien/sa-invoice-details.service';
import { SAInvoiceService } from 'app/ban-hang/ban_hang_chua_thu_tien';
import { IMCReceiptDetailCustomer } from 'app/shared/model/mc-receipt-detail-customer.model';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import { GhiTangService } from 'app/congcudungcu/ghi-tang-ccdc-khac/ghi-tang.service';
import { ITIIncrement } from 'app/shared/model/ti-increment.model';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';

@Component({
    selector: 'eb-ghi-tang',
    templateUrl: './ghi-tang.component.html',
    styleUrls: ['./ghi-tang.component.css']
})
export class GhiTangComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('contentUnRecord') public modalContentUnRecord: NgbModalRef;
    @ViewChild('popUpMultiAction') popUpMultiAction: TemplateRef<any>;
    @ViewChild('deleteItem') deleteItemModal: TemplateRef<any>;
    TYPE_GROUP_GHI_TANG = 52;
    TYPEID_GROUP_GHI_TANG = 520;
    DDSo_NgoaiTe = 8;
    DDSo_TienVND = 7;
    DDSo_TyLe = 5;
    currentAccount: any;
    itiIncrements: any[];
    itiIncrementsDetails: any[];
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
    // isShowSearch: boolean;
    mCReceiptDetails: IMCReceiptDetails[];
    selectedRow: any;
    record_: Irecord;
    pageCount: any;
    typeID: string;
    fromDate: any;
    fromDateStr: string;
    toDate: any;
    toDateStr: string;
    validateToDate: boolean;
    validateFromDate: boolean;
    currencyID: string;
    recorded: boolean;
    accountingObjectCode: string;
    searchValue: string;
    search: ISearchVoucher;
    currencys: ICurrency[]; //
    accountingobjects: IAccountingObject[];
    types: IType[];
    eMContracts: IEMContract[];
    /*Trạng thái*/
    listRecord: any;
    listColumnsRecord: string[] = ['name'];
    listHeaderColumnsRecord: string[] = ['Trạng thái'];
    statusRecord: any;
    /**/
    viewVouchersSelected: any;
    account: any;
    isSoTaiChinh: boolean;
    currencyCode: string;

    /*Phiếu thu bán hàng*/
    sAInvoiceDetails: ISAInvoiceDetails[];
    repositories: IRepository[];
    materialGoods: IMaterialGoods[];
    refVoucher: any;
    expenseItems: IExpenseItem[];
    costSets: ICostSet[];
    budgetItem: IBudgetItem[];
    organizationUnits: IOrganizationUnit[];
    statisticsCodes: IStatisticsCode[];
    /*Phiếu thu tiền khách hàng*/

    /*Phiếu thu tiền khách hàng*/
    modalRef: NgbModalRef;
    sAInvoiceID: string;
    rSInwardOutwardID: string;
    typeIDSAInvoice: number;

    /*Phân quyền*/
    ROLE_Them = ROLE.PhieuThu_Them;
    ROLE_Sua = ROLE.PhieuThu_Sua;
    ROLE_Xoa = ROLE.PhieuThu_Xoa;
    ROLE_GhiSo = ROLE.PhieuThu_GhiSo;
    ROLE_KetXuat = ROLE.PhieuThu_KetXuat;

    isSingleClick: any;
    checkSLT: any;
    index: number;
    typeMultiAction: string;
    MultiAction = MultiAction;
    keySearch: any;
    rowNum: number;
    tiIncrementDetailRefVoucher: any;
    sumQuantity: number;
    sumUnitPrice: number;
    sumAllAmount: number;
    sumTotalAmountOriginal: any;

    constructor(
        private ghiTangService: GhiTangService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private gLService: GeneralLedgerService,
        private accountingObjectService: AccountingObjectService,
        private currencyService: CurrencyService,
        public utilsService: UtilsService,
        private typeService: TypeService,
        private toastr: ToastrService,
        public translate: TranslateService,
        private eMContractService: EMContractService,
        private refModalService: RefModalService,
        private materialGoodsService: MaterialGoodsService,
        private viewVoucherService: ViewVoucherService,
        private repositoryService: RepositoryService,
        private expenseItemsService: ExpenseItemService,
        private costSetService: CostSetService,
        private budgetItemService: BudgetItemService,
        private organizationUnitService: OrganizationUnitService,
        private statisticsCodeService: StatisticsCodeService,
        private sAInvoiceDetailsService: SAInvoiceDetailsService,
        private sAInvoiceService: SAInvoiceService,
        private modalService: NgbModal,
        private unitService: UnitService
    ) {
        super();
        this.search = {};
        this.selectedRow = {};
        this.principal.identity().then(account => {
            this.account = account;
            this.currentAccount = account;
            if (account) {
                this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                this.currencyCode = this.account.organizationUnit.currencyID;
                this.color = this.account.systemOption.find(x => x.code === TCKHAC_MauCTuChuaGS && x.data).data;
                this.checkSLT = this.account.systemOption.find(x => x.code === HH_XUATQUASLTON).data === '1';
            }
        });
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencys = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
        });
        this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingobjects = res.body
                .filter(n => n.isActive)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
        });
        this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body.filter(type => type.typeGroupID === 10).sort((a, b) => a.typeName.localeCompare(b.typeName));
        });
        this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
            this.eMContracts = res.body.filter(n => n.isActive);
        });
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });

        this.translate.get(['ebwebApp.mBDeposit.home.recorded', 'ebwebApp.mBDeposit.home.unrecorded']).subscribe(res => {
            this.listRecord = [
                { value: 1, name: res['ebwebApp.mBDeposit.home.recorded'] },
                { value: 2, name: res['ebwebApp.mBDeposit.home.unrecorded'] }
            ];
        });

        /*Phiếu thu bán hàng*/
        this.refVoucher = [];
        this.sAInvoiceDetails = [];
        this.materialGoodsService.queryForComboboxGood().subscribe((res: HttpResponse<IMaterialGoods[]>) => {
            this.materialGoods = res.body;
        });
        this.repositoryService.getRepositoryCombobox().subscribe(res => {
            this.repositories = res.body;
        });
        this.expenseItemsService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body;
        });
        this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body;
        });
        this.budgetItemService.getBudgetItems().subscribe((res: HttpResponse<IBudgetItem[]>) => {
            this.budgetItem = res.body;
        });
        this.organizationUnitService.getOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body;
        });
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticsCodes = res.body;
        });
        this.unitService.getAllUnits().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
        });
        /*Phiếu thu bán hàng*/
    }

    selectedItemPerPage() {
        this.loadAll(false, true);
    }

    deleteIndexSS(): void {
        sessionStorage.removeItem('page_MCReceipt');
        sessionStorage.removeItem('size_MCReceipt');
        sessionStorage.removeItem('index_MCReceipt');
    }

    loadAll(search?, loadPage?, isMultiAciton?) {
        this.deleteIndexSS();
        // const pr = this.activatedRoute.snapshot.queryParams;
        // if (pr && !loadPage && !search && !isMultiAciton) {
        //     if (pr.page) {
        //         this.page = Number(pr.page);
        //         this.previousPage = Number(pr.page);
        //     }
        //     if (pr.size) {
        //         this.itemsPerPage = Number(pr.size);
        //     }
        //     if (pr.index) {
        //         this.index = Number(pr.index);
        //     }
        // } else {
        //     if (isMultiAciton) {
        //         this.index = this.itiIncrements.indexOf(this.selectedRows[0]);
        //     } else {
        //         this.index = Number(pr.index);
        //     }
        // }
        // if (this.activatedRoute.snapshot.paramMap.has('isSearch')) {
        //     const isSearch = this.activatedRoute.snapshot.paramMap.get('isSearch');
        //     if (isSearch === '1') {
        //     } else {
        //         sessionStorage.removeItem('searchVoucherGhiTang');
        //     }
        // } else {
        //     if (!loadPage) {
        //         sessionStorage.removeItem('searchVoucherGhiTang');
        //     }
        // }
        // sessionStorage.removeItem('searchVoucherGhiTang');
        const _search = JSON.parse(sessionStorage.getItem('searchVoucherGhiTang'));
        sessionStorage.removeItem('searchVoucherGhiTang');
        this.search = _search;
        if (_search) {
            this.search.isShowSearch = _search.isShowSearch;
            if (_search.fromDate) {
                this.search.fromDate = moment(_search.fromDate, DATE_FORMAT);
                this.fromDateStr = this.search.fromDate.format('DD/MM/YYYY');
            }
            if (_search.toDate) {
                this.search.toDate = moment(_search.toDate, DATE_FORMAT);
                this.toDateStr = this.search.toDate.format('DD/MM/YYYY');
            }
        }
        if (this.search) {
            this.statusRecord = this.search.statusRecorded ? 1 : this.search.statusRecorded === false ? 2 : null;
        } else {
            this.clearSearch();
            if (search) {
                this.page = 1;
                this.previousPage = 1;
            }
        }
        this.ghiTangService
            .loadAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                fromDate: this.search.fromDate ? this.search.fromDate : '',
                toDate: this.search.toDate ? this.search.toDate : '',
                keySearch: this.search.textSearch ? this.search.textSearch : ''
            })
            .subscribe(
                (res: HttpResponse<IMCReceipt[]>) => {
                    this.paginateMCReceipts(res.body, res.headers);
                },
                (res: HttpErrorResponse) => {
                    this.page = this.page > 0 ? this.page - 1 : 0;
                    this.loadAll();
                }
            );
        // }
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.selectedRows = [];
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        if (this.activatedRoute.snapshot.paramMap.has('isSearch')) {
            this.router.navigate(['/ghi-tang-ccdc', 'hasSearch', '1'], {
                queryParams: {
                    page: this.page,
                    size: this.itemsPerPage,
                    index: this.itiIncrements.indexOf(this.selectedRow)
                }
            });
        } else {
            this.router.navigate(['/ghi-tang-ccdc'], {
                queryParams: {
                    page: this.page,
                    size: this.itemsPerPage,
                    index: this.itiIncrements.indexOf(this.selectedRow)
                }
            });
        }
        this.loadAll(false, true);
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/ghi-tang-ccdc',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.search.isShowSearch = !this.search.isShowSearch;
    }

    ngOnInit() {
        this.itiIncrementsDetails = [];
        this.search.isShowSearch = false;
        const searchV = JSON.parse(sessionStorage.getItem('searchVoucherGhiTang'));
        this.search = searchV ? JSON.parse(sessionStorage.getItem('searchVoucherGhiTang')) : {};

        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        // this.registerChangeInMCReceipts();
        this.itiIncrementsDetails = [];
        this.registerChangeSession();
        this.registerExport();
        this.registerLockSuccess();
        this.registerUnlockSuccess();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IMCReceipt) {
        return item.id;
    }

    // registerChangeInMCReceipts() {
    //     this.eventSubscriber = this.eventManager.subscribe('mCReceiptListModification', response => this.loadAll());
    //     this.eventSubscribers.push(this.eventSubscriber);
    // }

    sort() {
        /*const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }*/
        const result = ['date' + ',' + (this.reverse ? 'asc' : 'desc')];
        result.push('postedDate' + ',' + (this.reverse ? 'asc' : 'desc'));
        result.push('noFBook' + ',' + (this.reverse ? 'asc' : 'desc'));
        return result;
    }

    private paginateMCReceipts(data: IMCReceipt[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.itiIncrements = data ? data : [];
        this.objects = data ? data : [];
        if (this.itiIncrements && this.itiIncrements.length > 0) {
            const tiItem = this.itiIncrements.find(x => x.id === this.search.id);
            if (tiItem) {
                this.selectedRow = tiItem;
            } else {
                this.selectedRow = this.itiIncrements[0];
            }
            const lstSelect = this.selectedRows.map(object => ({ ...object }));
            this.selectedRows = [];
            this.selectedRows.push(...this.itiIncrements.filter(n => lstSelect.some(m => m.id === n.id)));
            if (!this.selectedRows.find(n => n.id === this.selectedRow.id)) {
                this.selectedRows.push(this.selectedRow);
            }
            this.onSelect(this.selectedRow);
        } else {
            this.itiIncrements = [];
            this.itiIncrementsDetails = [];
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    clearSearch() {
        this.search = {};
        this.search.currencyID = null;
        this.search.accountingObjectID = null;
        this.search.statusRecorded = null;
        this.search.typeID = null;
        this.statusRecord = undefined;
    }

    onSelect(select: any) {
        this.isSingleClick = true;
        setTimeout(() => {
            if (this.isSingleClick) {
                this.index = this.itiIncrements.indexOf(select);
                this.selectedRow = select;
                this.ghiTangService.findDetailsByID(select.id).subscribe(res => {
                    this.itiIncrementsDetails =
                        res.body.tiIncrementDetailsConvertDTOS === undefined ? [] : res.body.tiIncrementDetailsConvertDTOS;
                    this.tiIncrementDetailRefVoucher =
                        res.body.tiIncrementDetailRefVoucherConvertDTOS === undefined
                            ? []
                            : res.body.tiIncrementDetailRefVoucherConvertDTOS;
                    this.sumAllList();
                });
            }
        }, 250);
    }

    sumAllList() {
        this.sumQuantity = 0;
        this.sumUnitPrice = 0;
        this.sumAllAmount = 0;
        const lengthDetail: number = this.itiIncrementsDetails.length;
        const lengthVoucher: number = this.tiIncrementDetailRefVoucher.length;
        const length = lengthDetail > lengthVoucher ? lengthDetail : lengthVoucher;
        for (let i = 0; i < length; i++) {
            if (i < lengthDetail) {
                if (this.itiIncrementsDetails[i].quantity) {
                    this.sumQuantity += this.itiIncrementsDetails[i].quantity;
                }
                if (this.itiIncrementsDetails[i].unitPrice) {
                    this.sumUnitPrice += this.itiIncrementsDetails[i].unitPrice;
                }
                if (this.itiIncrementsDetails[i].amount) {
                    this.sumAllAmount += this.itiIncrementsDetails[i].amount;
                }
            }
            if (i < lengthVoucher) {
                if (this.tiIncrementDetailRefVoucher[i].totalAmountOriginal) {
                    this.sumTotalAmountOriginal += this.tiIncrementDetailRefVoucher[i].totalAmountOriginal;
                }
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_GhiSo])
    record() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = MultiAction.GHI_SO;
            this.modalRef = this.modalService.open(this.popUpMultiAction, { backdrop: 'static' });
        } else {
            if (!this.checkCloseBook(this.account, this.selectedRow.date)) {
                if (!this.selectedRow.recorded) {
                    const record_: Irecord = {};
                    record_.id = this.selectedRow.id;
                    record_.typeID = this.TYPEID_GROUP_GHI_TANG;
                    this.gLService.record(record_).subscribe((res: HttpResponse<Irecord>) => {
                        if (res.body.success) {
                            this.selectedRow.recorded = true;
                            this.toastr.success(
                                this.translate.instant('ebwebApp.mBDeposit.recordSuccess'),
                                this.translate.instant('ebwebApp.mBDeposit.message')
                            );
                        }
                    });
                }
            }
        }
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

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_GhiSo])
    unrecord() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.typeMultiAction = MultiAction.BO_GHI_SO;
            this.modalRef = this.modalService.open(this.popUpMultiAction, { backdrop: 'static' });
        } else {
            if (!this.checkCloseBook(this.account, this.selectedRow.date)) {
                if (this.selectedRow.recorded) {
                    const record_: Irecord = {};
                    record_.id = this.selectedRow.id;
                    record_.typeID = this.TYPEID_GROUP_GHI_TANG;
                    this.gLService.unrecord(record_).subscribe((res: HttpResponse<Irecord>) => {
                        if (res.body.success) {
                            this.selectedRow.recorded = false;
                            this.toastr.success(
                                this.translate.instant('ebwebApp.mBDeposit.unrecordSuccess'),
                                this.translate.instant('ebwebApp.mBDeposit.message')
                            );
                        }
                    });
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

    doubleClickRow(detial, index) {
        this.isSingleClick = false;
        sessionStorage.removeItem('searchVoucherGhiTang');
        let searchVC: ISearchVoucher = {};
        searchVC.fromDate = this.search.fromDate !== undefined ? this.search.fromDate : null;
        searchVC.toDate = this.search.toDate !== undefined ? this.search.toDate : null;
        searchVC.rowNum = this.getRowNumberOfRecord(this.page, 0);
        searchVC.textSearch = this.search.textSearch ? this.search.textSearch : null;
        searchVC.isShowSearch = this.search.isShowSearch ? this.search.isShowSearch : false;
        searchVC.id = this.selectedRow ? this.selectedRow.id : '';
        searchVC = this.convertDateFromClient(searchVC);
        sessionStorage.setItem('searchVoucherGhiTang', JSON.stringify(searchVC));

        this.router.navigate(['./ghi-tang-ccdc', detial.id, 'edit']);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Xoa])
    delete() {
        event.preventDefault();
        if (this.selectedRows && this.selectedRows.length > 1) {
            this.typeMultiAction = MultiAction.XOA;
            this.modalRef = this.modalService.open(this.popUpMultiAction, { backdrop: 'static' });
        } else {
            if (!this.checkCloseBook(this.account, this.selectedRow.date) && !this.utilsService.isShowPopup) {
                if (!this.selectedRow.recorded) {
                    this.modalRef = this.modalService.open(this.deleteItemModal, { backdrop: 'static' });
                }
            }
        }
    }

    // region Tìm kiếm chứng từ add by Hautv
    searchVoucher(search?) {
        if (!this.checkErrorForSearch()) {
            return false;
        }
        sessionStorage.removeItem('searchVoucherGhiTang');
        let searchVC: ISearchVoucher = {};
        searchVC.fromDate = this.search.fromDate !== undefined ? this.search.fromDate : null;
        searchVC.toDate = this.search.toDate !== undefined ? this.search.toDate : null;
        searchVC.rowNum = this.getRowNumberOfRecord(this.page, 0);
        searchVC.textSearch = this.search.textSearch ? this.search.textSearch : null;
        searchVC = this.convertDateFromClient(searchVC);
        if (search) {
            this.page = 1;
            this.previousPage = 1;
            this.index = 0;
        }
        this.ghiTangService
            .loadAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                fromDate: this.search.fromDate ? this.search.fromDate : '',
                toDate: this.search.toDate ? this.search.toDate : '',
                keySearch: this.search.textSearch ? this.search.textSearch : ''
            })
            .subscribe(
                (res: HttpResponse<IMCReceipt[]>) => {
                    this.paginateMCReceipts(res.body, res.headers);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        // sessionStorage.setItem('searchVoucherGhiTang', JSON.stringify(searchVC));
    }

    resetSearch() {
        this.toDateStr = null;
        this.fromDateStr = null;
        this.selectedRows = [];
        sessionStorage.removeItem('searchVoucherGhiTang');
        this.loadAll(true);
    }

    // endregion
    transformAmountv(value: number) {
        const amount = parseFloat(value.toString().replace(/,/g, ''))
            .toFixed(2)
            .toString()
            .replace('.', ',')
            .replace(/\B(?=(\d{3})+(?!\d))/g, '.');
        return amount;
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

    getEMContractsbyID(id) {
        if (this.eMContracts) {
            const eMC = this.eMContracts.find(n => n.id === id);
            if (eMC) {
                if (this.isSoTaiChinh) {
                    return eMC.noFBook;
                } else {
                    return eMC.noMBook;
                }
            }
        }
    }

    private convertDateFromClient(seachVoucher: ISearchVoucher): ISearchVoucher {
        const copy: ISearchVoucher = Object.assign({}, seachVoucher, {
            fromDate:
                seachVoucher.fromDate != null
                    ? seachVoucher.fromDate instanceof moment ? seachVoucher.fromDate.format(DATE_FORMAT) : seachVoucher.fromDate
                    : null,
            toDate:
                seachVoucher.toDate != null
                    ? seachVoucher.toDate instanceof moment ? seachVoucher.toDate.format(DATE_FORMAT) : seachVoucher.toDate
                    : null
        });
        return copy;
    }

    checkErrorForSearch() {
        if (this.search && this.search.toDate && this.search.fromDate) {
            if (this.search.toDate < this.search.fromDate) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.fromDateGreaterToDate'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        return true;
    }

    changeFromDate() {
        this.fromDateStr = this.search.fromDate.format('DD/MM/YYYY');
    }

    changeFromDateStr() {
        try {
            if (this.fromDateStr.length === 8) {
                const td = this.fromDateStr.replace(/^(.{2})/, '$1/').replace(/^(.{5})/, '$1/');
                if (!moment(td, 'DD/MM/YYYY').isValid()) {
                    this.validateFromDate = true;
                    this.search.fromDate = null;
                } else {
                    this.validateFromDate = false;
                    this.search.fromDate = moment(td, 'DD/MM/YYYY');
                }
            } else {
                this.search.fromDate = null;
                this.validateFromDate = false;
            }
        } catch (e) {
            this.search.fromDate = null;
            this.validateFromDate = false;
        }
    }

    changeToDate() {
        this.toDateStr = this.search.toDate.format('DD/MM/YYYY');
    }

    changeToDateStr() {
        try {
            if (this.toDateStr.length === 8) {
                const td = this.toDateStr.replace(/^(.{2})/, '$1/').replace(/^(.{5})/, '$1/');
                if (!moment(td, 'DD/MM/YYYY').isValid()) {
                    this.validateToDate = true;
                    this.search.toDate = null;
                } else {
                    this.validateToDate = false;
                    this.search.toDate = moment(td, 'DD/MM/YYYY');
                }
            } else {
                this.search.toDate = null;
                this.validateToDate = false;
            }
        } catch (e) {
            this.search.toDate = null;
            this.validateToDate = false;
        }
    }

    sumDT(prop) {
        let total = 0;
        for (let i = 0; i < this.itiIncrementsDetails.length; i++) {
            total += this.itiIncrementsDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    registerChangeSession() {
        this.eventSubscriber = this.eventManager.subscribe('changeSession', response => {
            this.principal.identity(true).then(account => {
                this.account = account;
                if (account) {
                    this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                    this.currencyCode = this.account.organizationUnit.currencyID;
                    this.loadAll();
                }
            });
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_KetXuat])
    export() {
        let searchVC: ISearchVoucher = {};
        searchVC.typeID = this.search.typeID !== undefined ? this.search.typeID : null;
        searchVC.status = this.search.status || this.search.status === 0 ? this.search.status : null;
        searchVC.currencyID = this.search.currencyID !== undefined ? this.search.currencyID : null;
        searchVC.fromDate = this.search.fromDate !== undefined ? this.search.fromDate : null;
        searchVC.toDate = this.search.toDate !== undefined ? this.search.toDate : null;
        searchVC.accountingObjectID = this.search.accountingObjectID !== undefined ? this.search.accountingObjectID : null;
        searchVC.employeeID = this.search.employeeID !== undefined ? this.search.employeeID : null;
        searchVC.textSearch = this.search.textSearch ? this.search.textSearch : null;
        searchVC = this.convertDateFromClient(searchVC);
        this.utilsService
            .exportPDF({
                typeID: this.TYPEID_GROUP_GHI_TANG,
                searchVoucher: JSON.stringify(searchVC)
            })
            .subscribe(res => {
                this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.TYPEID_GROUP_GHI_TANG);
                // (res: HttpErrorResponse) => this.onError(res.message);
            });
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.TYPEID_GROUP_GHI_TANG}`, () => {
            this.exportExcel();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    exportExcel() {
        let searchVC: ISearchVoucher = {};
        searchVC.typeID = this.search.typeID !== undefined ? this.search.typeID : null;
        searchVC.status = this.search.status || this.search.status === 0 ? this.search.status : null;
        searchVC.currencyID = this.search.currencyID !== undefined ? this.search.currencyID : null;
        searchVC.fromDate = this.search.fromDate !== undefined ? this.search.fromDate : null;
        searchVC.toDate = this.search.toDate !== undefined ? this.search.toDate : null;
        searchVC.accountingObjectID = this.search.accountingObjectID !== undefined ? this.search.accountingObjectID : null;
        searchVC.employeeID = this.search.employeeID !== undefined ? this.search.employeeID : null;
        searchVC.textSearch = this.search.textSearch ? this.search.textSearch : null;
        searchVC = this.convertDateFromClient(searchVC);
        this.utilsService
            .exportExcel({
                typeID: this.TYPEID_GROUP_GHI_TANG,
                searchVoucher: JSON.stringify(searchVC)
            })
            .subscribe(res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_PHIEUTHU.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            });
    }

    isForeignCurrency() {
        if (this.selectedRow) {
            return this.account && this.selectedRow.currencyID !== this.account.organizationUnit.currencyID;
        }
    }

    getAmountOriginalType() {
        if (this.isForeignCurrency()) {
            return 8;
        }
        return 7;
    }

    getUnitPriceOriginalType() {
        if (this.isForeignCurrency()) {
            return 2;
        }
        return 1;
    }

    round(value, type) {
        if (type === 8) {
            if (this.isForeignCurrency()) {
                return this.utilsService.round(value, this.account.systemOption, type);
            } else {
                return this.utilsService.round(value, this.account.systemOption, 7);
            }
        } else if (type === 2) {
            if (this.isForeignCurrency()) {
                return this.utilsService.round(value, this.account.systemOption, type);
            } else {
                return this.utilsService.round(value, this.account.systemOption, 1);
            }
        } else {
            return this.utilsService.round(value, this.account.systemOption, type);
        }
    }

    /*Phiếu thu bán hàng*/
    getRepositoryByID(id) {
        if (this.repositories && id) {
            const repository = this.repositories.find(n => n.id === id.toLowerCase());
            if (repository) {
                return repository.repositoryCode;
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

    getAccountingObjectbyID(id) {
        if (this.accountingobjects && id) {
            const acc = this.accountingobjects.find(n => n.id === id.toLowerCase());
            if (acc) {
                return acc.accountingObjectCode;
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

    sumMCR(prop) {
        let total = 0;
        for (let i = 0; i < this.itiIncrementsDetails.length; i++) {
            total += this.itiIncrementsDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    getExpenseItembyID(id) {
        if (this.expenseItems && id) {
            const expenseItem = this.expenseItems.find(n => n.id === id.toLowerCase());
            if (expenseItem) {
                return expenseItem.expenseItemCode;
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

    getBudgetItembyID(id) {
        if (this.budgetItem && id) {
            const iBudgetItem = this.budgetItem.find(n => n.id === id.toLowerCase());
            if (iBudgetItem) {
                return iBudgetItem.budgetItemCode;
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

    getStatisticsCodebyID(id) {
        if (this.statisticsCodes && id) {
            const statisticsCode = this.statisticsCodes.find(n => n.id === id.toLowerCase());
            if (statisticsCode) {
                return statisticsCode.statisticsCode;
            }
        }
    }

    /*Phiếu thu bán hàng*/

    /*Phiếu thu khách hàng*/
    sumMCReceiptDetailCustomers(prop) {
        const total = 0;
        return isNaN(total) ? 0 : total;
    }

    getEmployeeByID(id) {
        if (this.accountingobjects) {
            const epl = this.accountingobjects.find(n => n.id === id);
            if (epl) {
                return epl.accountingObjectCode;
            } else {
                return '';
            }
        }
    }

    viewVoucher(imcReceiptDetailCustomer: IMCReceiptDetailCustomer) {
        let url = '';
        if (imcReceiptDetailCustomer.voucherTypeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
            url = `/#/chung-tu-ban-hang/${imcReceiptDetailCustomer.saleInvoiceID}/edit/from-ref`;
        } else if (imcReceiptDetailCustomer.voucherTypeID === TypeID.CHUNG_TU_NGHIEP_VU_KHAC) {
            url = `/#/g-other-voucher/${imcReceiptDetailCustomer.saleInvoiceID}/edit/from-ref`;
        }
        window.open(url, '_blank');
    }

    /*Phiếu thu khách hàng*/
    @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Them])
    addNew($event) {
        event.preventDefault();
        this.router.navigate(['ghi-tang-ccdc/new']);
    }

    edit() {
        event.preventDefault();
        if (this.selectedRow) {
            this.doubleClickRow(this.selectedRow, this.itiIncrements.indexOf(this.selectedRow.id));
        }
    }

    registerLockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('lockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
                this.account = account;
            });
            this.loadAll();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
                this.account = account;
            });
            this.loadAll();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    closePop() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    multiRecord() {
        const rq: RequestRecordListDtoModel = {};
        rq.records = this.selectedRows.map(n => {
            return { id: n.id, typeID: n.typeID };
        });
        rq.typeIDMain = this.TYPEID_GROUP_GHI_TANG;
        this.gLService.recordList(rq).subscribe((res: HttpResponse<HandlingResult>) => {
            /*this.selectedRows.forEach(slr => {
                const update = this.mCReceipts.find(
                    n => n.id === slr.id && !res.body.listFail.some(m => m.id === slr.id || m.mCReceiptID === slr.id)
                );
                if (update) {
                    update.recorded = true;
                }
            });*/
            this.loadAll(false, false, true);
            if (res.body.countFailVouchers > 0) {
                res.body.listFail.forEach(n => {
                    if (n.mCReceiptID) {
                        const mc = this.itiIncrements.find(m => m.id === n.mCReceiptID);
                        const type = this.types.find(t => t.id === mc.typeID);
                        n.noFBook = mc.noFBook;
                        n.noMBook = mc.noMBook;
                        n.typeName = type.typeName;
                    } else {
                        const type = this.types.find(t => t.id === n.typeID);
                        n.typeName = type.typeName;
                    }
                });
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
                this.messageRecord();
            }
        });
    }

    multiUnRecord() {
        this.ghiTangService.multiUnrecord(this.selectedRows).subscribe(
            (res: HttpResponse<any>) => {
                this.typeMultiAction = undefined;
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
                this.loadAll(false, false, true);
                // }
            },
            (res: HttpErrorResponse) => {
                this.toastr.error(
                    this.translate.instant('global.data.unRecordFailed'),
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
            }
        );
    }

    multiDelete() {
        this.ghiTangService.multiDelete(this.selectedRows).subscribe(
            (res: HttpResponse<any>) => {
                this.typeMultiAction = undefined;
                this.selectedRows = [];
                this.loadAll();
                // this.searchAfterChangeRecord();
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
                // }
            },
            (res: HttpErrorResponse) => {
                if (res.error.errorKey === 'errorDeleteList') {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.mBDeposit.errorDeleteVoucherNo'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                }
            }
        );
    }

    continue() {
        switch (this.typeMultiAction) {
            case MultiAction.GHI_SO:
                this.multiRecord();
                break;
            case MultiAction.BO_GHI_SO:
                this.multiUnRecord();
                break;
            case MultiAction.XOA:
                this.multiDelete();
                break;
        }
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    isRecord() {
        return this.selectedRows.some(n => n.recorded) && !this.selectedRows.some(n => !n.recorded);
    }

    isUnrecord() {
        return !this.selectedRows.some(n => n.recorded) && this.selectedRows.some(n => !n.recorded);
    }

    checkSelect(itiIncrement: ITIIncrement) {
        return this.selectedRows.some(n => n.id === itiIncrement.id);
    }

    getUnitNameByID(id) {
        if (this.units && id) {
            const unit = this.units.find(n => n.id === id);
            if (unit) {
                return unit.unitName;
            }
        }
    }

    confirmDelete(id: string) {
        this.ghiTangService.delete(id).subscribe(
            response => {
                // this.afterDeleteSuccess();
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.toastr.success(this.translate.instant('ebwebApp.pPDiscountReturn.delete.success'));
                this.loadAll();
            },
            res => {
                this.toastr.error(this.translate.instant('ebwebApp.pPDiscountReturn.delete.error'));
            }
        );
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    view(voucher) {
        // this.activeModal.dismiss(true);
        let url = '';
        switch (voucher.typeGroupID) {
            // Hàng bán trả lại
            case 33:
                url = `/#/hang-ban/tra-lai/${voucher.refVoucherID}/edit/from-ref`;
                break;
            // Giảm giá hàng bán
            case 34:
                url = `/#/hang-ban/giam-gia/${voucher.refVoucherID}/edit/from-ref`;
                break;
            // Xuất hóa đơn
            case 35:
                url = `/#/xuat-hoa-don/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 22:
                url = `/#/hang-mua/tra-lai/${voucher.refVoucherID}/view`;
                break;
            case 23:
                url = `/#/hang-mua/giam-gia/${voucher.refVoucherID}/view`;
                break;
            case 10:
                url = `/#/mc-receipt/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 16:
                url = `/#/mb-deposit/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 17:
                url = `/#/mb-credit-card/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 70:
                url = `/#/g-other-voucher/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 11:
                url = `/#/mc-payment/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 31:
                url = `/#/sa-order/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 24:
                url = `/#/mua-dich-vu/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 40:
                url = `/#/nhap-kho/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 20:
                url = `/#/don-mua-hang/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 41:
                url = `/#/xuat-kho/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 21:
                this.viewVoucherService.checkViaStockPPInvoice({ id: voucher.refVoucherID }).subscribe(
                    (res: HttpResponse<any>) => {
                        if (res.body.message === UpdateDataMessages.STOCK_TRUE) {
                            url = `/#/mua-hang/qua-kho-ref/${voucher.refVoucherID}/edit/1`;
                            window.open(url, '_blank');
                        } else if (res.body.message === UpdateDataMessages.STOCK_FALSE) {
                            url = `/#/mua-hang/khong-qua-kho-ref/${voucher.refVoucherID}/edit/1`;
                            window.open(url, '_blank');
                        } else {
                            this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                        }
                        return;
                    },
                    (res: HttpErrorResponse) => {
                        this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                    }
                );
                return;
            case 18:
                url = `/#/mc-audit/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 32:
                url = `/#/chung-tu-ban-hang/${voucher.refID2}/edit/from-ref`;
                break;
            case 30:
                url = `/#/sa-quote/${voucher.refID2}/edit/from-ref`;
                break;
            case 12:
            case 13:
            case 14:
                url = `/#/mb-teller-paper/${voucher.refID2}/edit/from-ref`;
                break;
        }
        window.open(url, '_blank');
    }
}
