import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Principal } from 'app/core';
import { DEFAULT_ROWS, ITEMS_PER_PAGE } from 'app/shared';
import { Irecord } from 'app/shared/model/record';
import { DataSessionStorage, IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { IPporder, Pporder } from 'app/shared/model/pporder.model';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { AccountingObjectDTO } from 'app/shared/model/accounting-object.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { UnitService } from 'app/danhmuc/unit';
import * as moment from 'moment';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { DatePipe } from '@angular/common';
import { RSInwardOutward } from 'app/shared/model/rs-inward-outward.model';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { RSInwardOutWardDetails } from 'app/shared/model/rs-inward-out-ward-details.model';
import { CURRENCY_ID, HH_XUATQUASLTON, MAU_BO_GHI_SO, PPINVOICE_TYPE, SO_LAM_VIEC, TCKHAC_SDTichHopHDDT, TypeID } from 'app/app.constants';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { SAInvoiceService } from 'app/ban-hang/ban_hang_chua_thu_tien';
import { PPDiscountReturnService } from 'app/muahang/hang_mua_tra_lai_giam_gia/pp-discount-return';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { ISAInvoice } from 'app/shared/model/sa-invoice.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { IMaterialGoodsInStock } from 'app/shared/model/material-goods.model';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { IType } from 'app/shared/model/type.model';
import { TypeService } from 'app/entities/type';
import { RSTransfer } from 'app/shared/model/rs-transfer.model';
import { RSTransferDetails } from 'app/shared/model/rs-transfer-details.model';
import { RsTransferService } from 'app/kho/chuyen-kho/rs-transfer.service';
import { RepositoryService } from 'app/danhmuc/repository';
import { IRepository, Repository } from 'app/shared/model/repository.model';

@Component({
    selector: 'eb-chuyen-kho',
    templateUrl: './chuyen-kho.component.html',
    styleUrls: ['./chuyen-kho.component.css']
})
export class ChuyenKhoComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('deleteItem') deleteItem;
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    @ViewChild('popUpMultiRecord') popUpMultiRecord: TemplateRef<any>;
    currentAccount: any;
    accountingObjectEmployees: AccountingObjectDTO[];
    rsTransfer: RSTransfer[];
    rsTransferDetailsLoadDataClick: RSTransferDetails[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    searchValue: string;
    links: any;
    currency: ICurrency;
    noType: number;
    currencies: ICurrency[];
    status: number;
    // rsInwardOutwardDetails: rsInwardOutwardDetail[];
    accountingObject: AccountingObjectDTO;
    accountingObjects: AccountingObjectDTO[];
    repositorys: Repository[];
    repository: Repository;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    currencyID: string;
    fromDate: any;
    toDate: any;
    recorded?: boolean;
    predicate: any;
    previousPage: any;
    isShowSearch: boolean;
    reverse: any;
    // rsInwardOutwardDetails: RSInwardOutWardDetails[];
    accountingObjectEmployee: AccountingObjectDTO;
    selectedRow: RSTransfer;
    pageCount: any;
    accountingObjectName: string;
    record_: Irecord;
    rowNum: number;
    accountingObjectCode: string;
    searchData: any;
    // dataSession: any;
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
    refVoucher: any;
    fromDateHolder: any;
    fromDateHolderMask: any;
    toDateHolder: any;
    toDateHolderMask: any;
    isErrorInvalid: any;
    account: any;
    isLoading: boolean;
    checkHidden: boolean;
    quantitySum: number;
    mainQuantitySum: number;
    totalAmount: number;
    totalOwAmount: number;
    index: number;
    isSoTaiChinh: number;
    // totalAmountSum: number;
    CHUYEN_KHO_KIEM_VAN_CHUYEN = TypeID.CHUYEN_KHO_KIEM_VAN_CHUYEN;
    CHUYEN_KHO_GUI_DAI_LY = TypeID.CHUYEN_KHO_GUI_DAI_LY;
    CHUYEN_KHO_NOI_BO = TypeID.CHUYEN_KHO_NOI_BO;
    checkModalRef: NgbModalRef;
    defaultRows: number;
    mgForPPOder: IMaterialGoodsInStock[];
    mgForPPOderTextCode: any;
    checkSLT: boolean;
    routerName: string;
    isTichHopHDDT: boolean;
    typeDelete: number;
    isFromPPDiscountReturn: boolean;
    isFromSaOrder: boolean;
    isFromSaInvoice: boolean;
    typeMultiAction: number;
    typeRecords: any[];
    statusRecords: any[];
    STATUS_RECORDED = 1;
    STATUS_NOT_RECORDED = 2;
    types: IType[];
    Type_group_rsTransfer = 42;

    /*Phân Quyền*/
    ROLE_XEM = ROLE.ChuyenKho_XEM;
    ROLE_THEM = ROLE.ChuyenKho_THEM;
    ROLE_SUA = ROLE.ChuyenKho_SUA;
    ROLE_XOA = ROLE.ChuyenKho_XOA;
    ROLE_GHI_SO = ROLE.ChuyenKho_GHI_SO;
    ROLE_IN = ROLE.ChuyenKho_IN;
    ROLE_KETXUAT = ROLE.ChuyenKho_KET_XUAT;

    constructor(
        private saInvoiceService: SAInvoiceService,
        private ppDiscountReturn: PPDiscountReturnService,
        private currencyService: CurrencyService,
        public datepipe: DatePipe,
        private accountingObjectService: AccountingObjectService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private gLService: GeneralLedgerService,
        private materialGoodsService: MaterialGoodsService,
        private unitService: UnitService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private rsTransferService: RsTransferService,
        private eventManager: JhiEventManager,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private refModalService: RefModalService,
        private modalService: NgbModal,
        private translate: TranslateService,
        private sAInvoiceService: SAInvoiceService,
        public activeModal: NgbActiveModal,
        private typeService: TypeService,
        private repositoryService: RepositoryService
    ) {
        super();
        this.defaultRows = DEFAULT_ROWS;
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
            this.predicate = 'date';
        });
        this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body
                .filter(type => type.typeGroupID === this.Type_group_rsTransfer)
                .sort((a, b) => a.typeName.localeCompare(b.typeName));
        });
    }

    ngOnInit() {
        this.typeMultiAction = 1;
        this.rsTransferDetailsLoadDataClick = [];
        this.refVoucher = [];
        this.accountingObjects = [];
        this.repositorys = [];
        this.isShowSearch = false;
        this.registerChangeInPPOrder();
        this.registerExport();
        this.registerChangeSession();
        this.accountingObjectService.getAccountingObjectsIsActive().subscribe((res: HttpResponse<AccountingObjectDTO[]>) => {
            this.accountingObjects = res.body;
            this.getSessionData();
        });
        this.getRepository();
        this.getAccount();
        this.registerLockSuccess();
        this.registerUnlockSuccess();
        this.materialGoodsService.queryForComboboxGood({ materialsGoodsType: [0, 1, 3] }).subscribe(
            (res: HttpResponse<any>) => {
                this.mgForPPOder = res.body;
            },
            (res: HttpErrorResponse) => console.log(res.message)
        );

        this.translate
            .get([
                'ebwebApp.warehouseTransfer.internalWarehous',
                'ebwebApp.warehouseTransfer.exportAgent',
                'ebwebApp.warehouseTransfer.internalExport',
                'ebwebApp.common.carrying',
                'ebwebApp.common.notCarrying'
            ])
            .subscribe(res => {
                this.typeRecords = [
                    { value: this.CHUYEN_KHO_KIEM_VAN_CHUYEN, name: res['ebwebApp.warehouseTransfer.internalWarehous'] },
                    { value: this.CHUYEN_KHO_GUI_DAI_LY, name: res['ebwebApp.warehouseTransfer.exportAgent'] },
                    { value: this.CHUYEN_KHO_NOI_BO, name: res['ebwebApp.warehouseTransfer.internalExport'] }
                ];
                this.statusRecords = [
                    { value: this.STATUS_RECORDED, name: res['ebwebApp.common.carrying'] },
                    { value: this.STATUS_NOT_RECORDED, name: res['ebwebApp.common.notCarrying'] }
                ];
            });
    }

    // kho
    getRepository() {
        this.repositoryService.findAllByCompanyID().subscribe(
            (res: HttpResponse<IRepository[]>) => {
                this.repositorys = res.body;
            },
            (subRes: HttpErrorResponse) => this.onError(subRes.message)
        );
    }

    getAccount() {
        this.principal.identity().then(account => {
            this.account = account;
            this.checkSLT = this.account.systemOption.find(x => x.code === HH_XUATQUASLTON).data === '1';
            this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
            for (let i = 0; i < this.account.systemOption.length; i++) {
                if (!this.account.systemOption[i].data) {
                    this.account.systemOption[i].data = this.account.systemOption[i].defaultData;
                }
            }
            this.color = this.account.systemOption.find(item => item.code === MAU_BO_GHI_SO).data;
        });
    }

    registerChangeSession() {
        this.eventManager.subscribe('changeSession', response => {
            this.getAccount();
            this.search();
        });
    }

    loadAllForSearch() {
        if (this.checkToDateGreaterFromDate()) {
            this.page = 1;
            this.search();
        }
    }

    resetSeach() {
        this.isErrorInvalid = false;
        this.accountingObject = {};
        this.repository = {};
        this.status = null;
        this.noType = null;
        this.currency = {};
        this.accountingObjectEmployee = {};
        this.fromDate = '';
        this.toDate = '';
        this.searchValue = '';
        this.loadAllForSearch();
    }

    checkToDateGreaterFromDate(): boolean {
        if (this.fromDate && this.toDate && this.fromDate > this.toDate) {
            this.isErrorInvalid = true;
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.fromDateGreaterToDate'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        } else {
            this.isErrorInvalid = false;
            return true;
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition(true);
        }
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    onSelect(rsTransfer: RSTransfer) {
        if (!rsTransfer) {
            return;
        }
        this.selectedRow = rsTransfer;
        this.quantitySum = 0;
        this.mainQuantitySum = 0;
        this.totalAmount = 0;
        this.totalOwAmount = 0;
        // this.totalAmountSum = 0;
        if (rsTransfer.id && rsTransfer.typeID) {
            this.rsTransferService.getDetailsTransferById(rsTransfer.id, rsTransfer.typeID).subscribe(res => {
                this.rsTransferDetailsLoadDataClick = res.body.sort((a, b) => a.orderPriority - b.orderPriority);
                if (rsTransfer.typeID === this.CHUYEN_KHO_NOI_BO) {
                    this.checkHidden = true;
                } else {
                    this.checkHidden = false;
                }
                this.setSumOnClick();
            });
        }
        if (rsTransfer.id) {
            this.rsTransferService.getRefVouchersByPPOrderID(rsTransfer.id).subscribe(res => (this.refVoucher = res.body));
        }
        // this.updateSum();
    }

    isForeignCurrency() {
        return this.account.organizationUnit.currencyID !== CURRENCY_ID;
    }

    getOriginalType() {
        if (this.isForeignCurrency()) {
            return 8;
        }
        return 7;
    }

    setSumOnClick() {
        for (const item of this.rsTransferDetailsLoadDataClick) {
            this.quantitySum += item.quantity;
            this.mainQuantitySum += item.mainQuantity;
            this.totalAmount += item.amount;
            this.totalOwAmount += item.oWAmount;
        }
        this.quantitySum = this.quantitySum || 0;
        this.mainQuantitySum = this.mainQuantitySum || 0;
        this.totalAmount = this.totalAmount || 0;
        this.totalOwAmount = this.totalOwAmount || 0;
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    search() {
        this.rsTransferService
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: this.status || '',
                noType: this.noType || '',
                accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
                employee: this.accountingObjectEmployee && this.accountingObjectEmployee.id ? this.accountingObjectEmployee.id : '',
                repository: this.repository && this.repository.id ? this.repository.id : '',
                searchValue: this.searchValue || ''
            })
            .subscribe(
                (res: HttpResponse<RSTransfer[]>) => this.paginatePPOrder(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    transition(isTransition) {
        if (isTransition) {
            this.selectedRow = null;
        }
        // this.router.navigate(['/chuyen-kho'], {
        //     queryParams: {
        //         page: this.page,
        //         size: this.itemsPerPage,
        //         sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        //     }
        // });
        this.search();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/chuyen-kho',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.search();
    }

    doubleClickRow(rsTransfer: RSTransfer, index?: number, isEdit?: boolean) {
        if (this.dataSession) {
            this.dataSession.isEdit = isEdit;
        }
        index = !index ? this.rsTransfer.indexOf(rsTransfer) : index;
        this.saveSearchData(rsTransfer, index);
        if (
            rsTransfer.typeID === this.CHUYEN_KHO_KIEM_VAN_CHUYEN ||
            rsTransfer.typeID === this.CHUYEN_KHO_GUI_DAI_LY ||
            rsTransfer.typeID === this.CHUYEN_KHO_NOI_BO
        ) {
            this.router.navigate(['/chuyen-kho', rsTransfer.id, 'edit', this.rowNum]);
        }
    }

    addNew($event = null) {
        this.saveSearchData(this.selectedRow, this.index - 1);
        this.router.navigate(['/chuyen-kho/new']);
    }

    saveSearchData(rsTransfer: RSTransfer, index: number) {
        this.selectedRow = rsTransfer;
        this.searchData = {
            fromDate: this.fromDate || '',
            toDate: this.toDate || '',
            noType: this.noType ? this.noType : '',
            status: this.status === this.STATUS_RECORDED || this.status === this.STATUS_NOT_RECORDED ? this.status : '',
            accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
            searchValue: this.searchValue || ''
        };
        this.rowNum = this.getRowNumberOfRecord(this.page, index);
        if (!this.dataSession) {
            this.dataSession = new DataSessionStorage();
        }
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.pageCount = this.pageCount;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.isShowSearch = this.isShowSearch;
        this.dataSession.rowNum = this.rowNum;
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        this.dataSession.searchVoucher = JSON.stringify(this.searchData);
        sessionStorage.setItem('chuyenKhoSearchData', JSON.stringify(this.dataSession));
        sessionStorage.setItem('chuyenKhoDataSession', JSON.stringify(this.dataSession));
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('chuyenKhoSearchData'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.searchData = JSON.parse(this.dataSession.searchVoucher);
            if (this.searchData) {
                this.fromDate = this.searchData.fromDate;
                this.toDate = this.searchData.toDate;
                this.status = this.searchData.status;
                this.noType = this.searchData.noType;
                this.accountingObject = this.accountingObjects.find(x => x.id === this.searchData.accountingObject);
                this.searchValue = this.searchData.searchValue;
            }
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
            this.isShowSearch = this.dataSession.isShowSearch;
        }
        sessionStorage.removeItem('chuyenKhoSearchData');
        this.transition(false);
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.rsTransfer.length; i++) {
            total += this.rsTransfer[i][prop];
        }
        return total ? total : 0;
    }

    // updateSum() {
    //     this.totalAmountSum = this.totalAmountSum || 0;
    //     this.totalAmountSum = this.sum('totalAmount');
    // }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IPporder) {
        return item.id;
    }

    registerChangeInPPOrder() {
        this.eventSubscriber = this.eventManager.subscribe('RSInwardOutwardListModification', response => this.search());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    onSetUpHolderMask() {
        this.fromDateHolderMask = moment(this.fromDate, 'DD/MM/YYYY').format('DD/MM/YYYY');
        this.toDateHolderMask = moment(this.toDate, 'DD/MM/YYYY').format('DD/MM/YYYY');
    }

    selectedItemPerPage() {
        this.search();
    }

    edit() {
        event.preventDefault();
        if (!this.dataSession) {
            this.dataSession = new DataSessionStorage();
        }
        if (this.selectedRow.id) {
            if (!this.selectedRow.recorded) {
                this.doubleClickRow(this.selectedRow, this.rsTransfer.indexOf(this.selectedRow), true);
            } else {
                this.dataSession.isEdit = false;
            }
        } else {
            this.dataSession.isEdit = false;
        }
    }

    private paginatePPOrder(data: RSTransfer[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.rsTransfer = data;
        this.objects = data;
        if (this.page > 1 && this.rsTransfer && this.rsTransfer.length === 0) {
            this.page = this.page - 1;
            this.loadAllForSearch();
            return;
        }
        if (this.rowNum && !this.index) {
            this.index = this.rowNum % this.itemsPerPage;
            this.index = this.index || this.itemsPerPage;
            this.selectedRow = this.rsTransfer[this.index - 1];
        } else if (this.index) {
            this.selectedRow = this.rsTransfer[this.index - 1];
        } else {
            this.selectedRow = this.rsTransfer[0];
        }
        const lstSelect = this.selectedRows.map(object => ({ ...object }));
        this.selectedRows = [];
        this.selectedRows.push(...this.rsTransfer.filter(n => lstSelect.some(m => m.id === n.id)));
        this.rowNum = this.getRowNumberOfRecord(this.page, 0);
        this.rsTransferDetailsLoadDataClick = [];
        this.onSelect(this.selectedRow || this.rsTransfer[0] || null);
        this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
        sessionStorage.removeItem('chuyenKhoSearchData');
        // this.updateSum();
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    //     updateFromDateHolder() {
    //         this.fromDate = this.utilsService.formatStrDate(this.fromDateHolder);
    //     }
    //
    //     updateToDateHolder() {
    //         this.toDate = this.utilsService.formatStrDate(this.toDateHolder);
    //     }

    record() {
        event.preventDefault();
        if (this.selectedRows && this.selectedRows.length > 1) {
            this.typeMultiAction = 0;
            this.checkModalRef = this.modalService.open(this.popUpMultiRecord, { backdrop: 'static' });
            return;
        } else {
            if (!this.checkCloseBook(this.account, this.selectedRow.postedDate)) {
                this.utilsService
                    .checkQuantityExistsTest1(this.rsTransferDetailsLoadDataClick, this.account, this.selectedRow.postedDate)
                    .then(data => {
                        if (data) {
                            this.mgForPPOderTextCode = data;
                            if (!this.checkSLT && this.mgForPPOderTextCode.quantityExists) {
                                this.toastr.error(
                                    this.translate.instant('ebwebApp.pPDiscountReturnDetails.error.quantityExistsRecordErrorSave'),
                                    this.translate.instant('ebwebApp.mBDeposit.message')
                                );
                                return;
                            }
                            this.isLoading = true;
                            if (!this.selectedRow.recorded) {
                                let record_: Irecord = {};
                                if (
                                    this.selectedRow.typeID === this.CHUYEN_KHO_KIEM_VAN_CHUYEN ||
                                    this.selectedRow.typeID === this.CHUYEN_KHO_GUI_DAI_LY ||
                                    this.selectedRow.typeID === this.CHUYEN_KHO_NOI_BO
                                ) {
                                    record_ = {
                                        id: this.selectedRow.id,
                                        typeID: this.selectedRow.typeID,
                                        repositoryLedgerID: this.selectedRow.id
                                    };
                                }
                                if (!this.selectedRow.recorded) {
                                    this.gLService.record(record_).subscribe(
                                        (res: HttpResponse<Irecord>) => {
                                            if (res.body.success) {
                                                this.toastr.success(
                                                    this.translate.instant('ebwebApp.mBTellerPaper.recordToast.done'),
                                                    this.translate.instant('ebwebApp.mBTellerPaper.message.title')
                                                );
                                                this.selectedRow.recorded = true;
                                            } else {
                                                this.toastr.error(
                                                    this.translate.instant('ebwebApp.mBTellerPaper.recordToast.failed'),
                                                    this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                                                );
                                            }
                                            this.isLoading = false;
                                        },
                                        () => {
                                            this.isLoading = false;
                                        }
                                    );
                                }
                            }
                        }
                    });
            }
        }
    }

    unrecord() {
        event.preventDefault();
        if (this.selectedRows && this.selectedRows.length > 1) {
            this.typeMultiAction = 1;
            this.checkModalRef = this.modalService.open(this.popUpMultiRecord, { backdrop: 'static' });
            return;
        } else {
            if (!this.checkCloseBook(this.account, this.selectedRow.postedDate)) {
                this.isLoading = true;
                let record_: Irecord = {};
                if (
                    this.selectedRow.typeID === this.CHUYEN_KHO_KIEM_VAN_CHUYEN ||
                    this.selectedRow.typeID === this.CHUYEN_KHO_GUI_DAI_LY ||
                    this.selectedRow.typeID === this.CHUYEN_KHO_NOI_BO
                ) {
                    record_ = {
                        id: this.selectedRow.id,
                        typeID: this.selectedRow.typeID,
                        repositoryLedgerID: this.selectedRow.id
                    };
                }
                if (this.selectedRow.recorded) {
                    this.gLService.unrecord(record_).subscribe(
                        (res: HttpResponse<Irecord>) => {
                            if (res.body.success) {
                                this.toastr.success(
                                    this.translate.instant('ebwebApp.mBTellerPaper.unrecordToast.done'),
                                    this.translate.instant('ebwebApp.mBTellerPaper.message.title')
                                );
                                this.selectedRow.recorded = false;
                            } else {
                                this.toastr.error(
                                    this.translate.instant('ebwebApp.mBTellerPaper.unrecordToast.failed'),
                                    this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                                );
                            }
                            this.isLoading = false;
                        },
                        () => {
                            this.isLoading = false;
                        }
                    );
                }
            }
        }
    }

    exportExcel() {
        this.rsTransferService
            .exportTranfer('excel', {
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: this.status === this.STATUS_RECORDED || this.status === this.STATUS_NOT_RECORDED ? this.status : '',
                noType: this.noType || '',
                accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
                employee: this.accountingObjectEmployee && this.accountingObjectEmployee.id ? this.accountingObjectEmployee.id : '',
                searchValue: this.searchValue || ''
            })
            .subscribe(res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_ChuyenKho.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            });
    }

    exportPdf() {
        this.rsTransferService
            .exportTranfer('pdf', {
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: this.status === this.STATUS_RECORDED || this.status === this.STATUS_NOT_RECORDED ? this.status : '',
                noType: this.noType || '',
                accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
                employee: this.accountingObjectEmployee && this.accountingObjectEmployee.id ? this.accountingObjectEmployee.id : '',
                searchValue: this.searchValue || ''
            })
            .subscribe(res => {
                this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.CHUYEN_KHO_KIEM_VAN_CHUYEN);
            });
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.CHUYEN_KHO_KIEM_VAN_CHUYEN}`, () => {
            this.exportExcel();
        });
    }

    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.checkModalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (!this.checkCloseBook(this.account, this.selectedRow.postedDate)) {
                if (!this.selectedRow.recorded) {
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
                    }
                    this.checkModalRef = this.modalService.open(this.deleteItem, { size: 'lg', backdrop: 'static' });
                }
            }
        }
    }

    confirmDelete() {
        if (this.selectedRow && this.selectedRow.id) {
            // /*Check chứng từ hiện tại đã phát hành hóa đơn hay chưa*/
            // if (this.selectedRow.typeID === this.XUAT_KHO_TU_BAN_HANG || this.selectedRow.typeID === this.XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
            //     this.isTichHopHDDT = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
            //     if (this.selectedRow.refIsBill && this.selectedRow.refInvoiceForm === 0 && this.isTichHopHDDT) {
            //         if (this.selectedRow.refInvoiceNo) {
            //             this.typeDelete = 2;
            //         } else {
            //             this.typeDelete = 0;
            //         }
            //     } else if (this.selectedRow.refIsBill) {
            //         this.typeDelete = 1;
            //     } else {
            //         this.typeDelete = 0;
            //     }
            // }
            // /*End*/
            // if (this.selectedRow.typeID === this.XUAT_KHO_TU_BAN_HANG) {
            //     if (this.selectedRow.refTypeID && this.typeDelete !== 2) {
            //         this.sAInvoiceService.delete(this.selectedRow.refID).subscribe(response => {
            //             this.toastr.success(this.translate.instant('ebwebApp.sAInvoice.delete.success'));
            //             this.checkModalRef.close();
            //             this.search();
            //         });
            //     } else {
            //         this.toastr.error(this.translate.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
            //         this.checkModalRef.dismiss(true);
            //     }
            // } else if (this.selectedRow.typeID === this.XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
            //     if (this.selectedRow.typeID && this.typeDelete !== 2) {
            //         this.ppDiscountReturn.delete(this.selectedRow.refID).subscribe(response => {
            //             this.toastr.success(this.translate.instant('ebwebApp.saReturn.deleted'));
            //             this.checkModalRef.close();
            //             this.search();
            //         });
            //     } else {
            //         this.toastr.error(this.translate.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
            //         this.checkModalRef.dismiss(true);
            //     }
            // } else if (this.selectedRow.typeID === this.XUAT_KHO_TU_DIEU_CHINH) {
            //     // TODO: nhap kho tu dieu chinh
            // } else
            if (
                this.selectedRow.typeID === this.CHUYEN_KHO_KIEM_VAN_CHUYEN ||
                this.selectedRow.typeID === this.CHUYEN_KHO_GUI_DAI_LY ||
                this.selectedRow.typeID === this.CHUYEN_KHO_NOI_BO
            ) {
                this.rsTransferService.delete(this.selectedRow.id).subscribe(response => {
                    this.toastr.success(this.translate.instant('ebwebApp.saReturn.deleted'));
                    this.checkModalRef.close();
                    this.search();
                });
            }
        }
    }

    export() {
        this.exportPdf();
    }

    registerLockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('lockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
                this.account = account;
            });
            this.loadAllForSearch();
        });
    }

    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
                this.account = account;
            });
            this.loadAllForSearch();
        });
    }

    closePopUpDelete() {
        if (this.checkModalRef) {
            this.checkModalRef.close();
        }
    }

    continueDelete() {
        this.rsTransferService.multiDelete(this.selectedRows).subscribe(
            (res: HttpResponse<any>) => {
                if (this.checkModalRef) {
                    this.checkModalRef.close();
                }
                this.selectedRows = [];
                this.loadAllForSearch();
                if (res.body.countTotalVouchers !== res.body.countSuccessVouchers) {
                    this.checkModalRef = this.refModalService.open(
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
                if (this.checkModalRef) {
                    this.checkModalRef.close();
                }
            }
        );
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

    continueMultiAction() {
        if (this.typeMultiAction === 1) {
            this.rsTransferService.multiUnRecord(this.selectedRows).subscribe(
                (res: HttpResponse<any>) => {
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
                    }
                    // this.selectedRows = [];
                    if (res.body.countTotalVouchers !== res.body.countSuccessVouchers) {
                        this.checkModalRef = this.refModalService.open(
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
                    this.search();
                },
                (res: HttpErrorResponse) => {
                    this.toastr.error(
                        this.translate.instant('global.data.unRecordFailed'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
                    }
                }
            );
        } else if (this.typeMultiAction === 0) {
            const listRecord: RequestRecordListDtoModel = {};
            listRecord.typeIDMain = TypeID.CHUYEN_KHO;
            listRecord.records = [];
            this.selectedRows.forEach(item => {
                listRecord.records.push({
                    id: item.id,
                    typeID: item.typeID
                });
            });
            this.gLService.recordList(listRecord).subscribe(
                (res: HttpResponse<HandlingResult>) => {
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
                    }
                    if (res.body.listFail && res.body.listFail.length > 0) {
                        if (res.body.countFailVouchers > 0) {
                            res.body.listFail.forEach(n => {
                                if (n.rSTransferID) {
                                    const rs = this.rsTransfer.find(p => p.id === n.rSTransferID);
                                    const type = this.types.find(t => t.id === rs.typeID);
                                    n.noFBook = rs.noFBook;
                                    n.noMBook = rs.noMBook;
                                    n.typeName = type.typeName;
                                } else {
                                    const type = this.types.find(t => t.id === n.typeID);
                                    n.typeName = type.typeName;
                                }
                            });
                            this.checkModalRef = this.refModalService.open(
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
                        }
                    } else {
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    // this.activeModal.close();
                    this.search();
                },
                (res: HttpErrorResponse) => {
                    this.toastr.error(
                        this.translate.instant('global.data.recordFailed'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
                    }
                }
            );
        }
    }

    isRecord() {
        return this.selectedRows.some(n => n.recorded) && !this.selectedRows.some(n => !n.recorded);
    }
}
