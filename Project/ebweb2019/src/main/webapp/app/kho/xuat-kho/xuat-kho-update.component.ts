import { AfterViewChecked, AfterViewInit, Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { AccountingObjectDTO, IAccountingObject } from 'app/shared/model/accounting-object.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ActivatedRoute, Router } from '@angular/router';
import { CurrencyService } from 'app/danhmuc/currency';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { AccountDefaultService } from 'app/danhmuc/account-default';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IMaterialGoodsInStock } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { UnitService } from 'app/danhmuc/unit';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Principal } from 'app/core';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { Subscription } from 'rxjs';
import * as moment from 'moment';
import { IRSInwardOutward, RSInwardOutward } from 'app/shared/model/rs-inward-outward.model';
import { IRSInwardOutWardDetails, RSInwardOutWardDetails } from 'app/shared/model/rs-inward-out-ward-details.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { CostSetService } from 'app/entities/cost-set';
import { ExpenseItemService } from 'app/entities/expense-item';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { EMContractService } from 'app/entities/em-contract';
import { BudgetItemService } from 'app/entities/budget-item';
import {
    AccountType,
    CURRENCY,
    CURRENCY_ID,
    SO_LAM_VIEC,
    TypeID,
    VTHH_NHAP_DON_GIA_VON,
    RSOUTWARD_TYPE,
    PP_TINH_GIA_XUAT_KHO,
    CALCULATE_OW,
    TCKHAC_SDSoQuanTri,
    HH_XUATQUASLTON,
    GROUP_TYPEID
} from 'app/app.constants';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { RepositoryService } from 'app/danhmuc/repository';
import { AccountListService } from 'app/danhmuc/account-list';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { PPInvoiceDetailsService } from 'app/entities/pp-invoice-details';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { EbSaOrderModalComponent } from 'app/shared/modal/sa-order/sa-order.component';
import { RepositoryLedgerService } from 'app/entities/repository-ledger';
import { PpDiscountReturnModalComponent } from 'app/shared/modal/pp-discount-return/pp-discount-return-modal.component';
import { MaterialQuantumModalComponent } from 'app/shared/modal/material-quantum/material-quantum-modal.component';
import { SAOrderService } from 'app/ban-hang/don_dat_hang/sa-order';
import { PPDiscountReturnDetailsService } from 'app/entities/pp-discount-return-details';
import { SAInvoiceDetailsService } from 'app/ban-hang/ban_hang_chua_thu_tien/sa-invoice-details.service';
import { MaterialQuantumDetailsService } from 'app/danhmuc/dinh-muc-nguyen-vat-lieu/material-quantum-details';
import { IUnit } from 'app/shared/model/unit.model';
import { SaInvoiceOutwardModalComponent } from 'app/shared/modal/sa-invoice-outward/sa-invoice-outward-modal.component';
import { IViewSAOrderDTO } from 'app/shared/model/view-sa-order.model';
import { PPDiscountReturn } from 'app/shared/model/pp-discount-return.model';
import { EbSelectMaterialGoodsModalComponent } from 'app/shared/modal/select-material-goods/select-material-goods.component';
import { ROLE } from 'app/role.constants';
import { SAInvoice } from 'app/shared/model/sa-invoice.model';
import { SAOrderDetails } from 'app/shared/model/sa-order-details.model';
import { SAOrder } from 'app/shared/model/sa-order.model';
import { DATE_FORMAT, DATE_FORMAT_SLASH, ITEMS_PER_PAGE } from 'app/shared';
import { RSInwardOutwardService } from 'app/entities/rs-inward-outward/rs-inward-outward.service';
import { EbMaterialGoodsSpecificationsModalComponent } from 'app/shared/modal/material-goods-specifications/material-goods-specifications.component';

@Component({
    selector: 'eb-xuat-kho-update',
    templateUrl: './xuat-kho-update.component.html',
    styleUrls: ['./xuat-kho-update.component.css']
})
export class XuatKhoUpdateComponent extends BaseComponent implements OnInit, AfterViewInit, AfterViewChecked {
    @ViewChild('content') content: NgbModalRef;
    @ViewChild('deleteItem') deleteItem: NgbModalRef;
    @ViewChild('contentSave') contentSaveModal: TemplateRef<any>;
    isSaving: boolean;
    dateDp: any;
    RSOUTWARD_TYPE = RSOUTWARD_TYPE;
    accountDefaults: string[] = [];
    accountingObjects: AccountingObjectDTO[];
    employees: AccountingObjectDTO[];
    currencies: ICurrency[];
    totalvatamount: number;
    totalvatamountoriginal: number;
    isCreateUrl: boolean;
    isLoading: boolean;
    isCurrencyVND: boolean;
    page: number;
    itemsPerPage: number;
    pageCount: number;
    totalItems: number;
    rowNum: number;
    predicate: any;
    reverse: any;
    createFrom = null;
    accountingObjectName: any;
    recorded: boolean;
    dataSession: IDataSessionStorage;
    rsInwardOutward: RSInwardOutward;
    rsInwardOutwardCopy: RSInwardOutward;
    rsInwardOutwardDetails: RSInwardOutWardDetails[];
    rsInwardOutwardDetailsCopy: RSInwardOutWardDetails[];
    isEditUrl: boolean;
    currency: ICurrency;
    deliverDate: any;
    isClosed: boolean;
    creditAccountDefault: string;
    debitAccountDefault: string;
    date: any;
    indexDetail: number;
    materialGoodsInStock: IMaterialGoodsInStock[];
    isEdit: boolean;
    searchData: string;
    account: any;
    checkSLT: boolean;
    checkOpenSave: boolean;
    contextMenu: ContextMenu;
    quantitySum: number;
    quantityReceiptSum: number;
    amountSum: number;
    amountOriginalSum: number;
    discountAmountOriginal: number;
    discountAmount: number;
    vatAmountOriginal: number;
    vatAmount: number;
    private TYPE_GROUP_RS_IO = 41;
    modalRef: NgbModalRef;
    viewVouchersSelected: any;
    viewVouchersSelectedCopy: any;
    eventSubscriber: Subscription;
    mainQuantitySum: number;
    isClosing: boolean;
    isMove: boolean;
    expenseItems: IExpenseItem[];
    costSets: ICostSet[];
    eMContracts: IEMContract[];
    viewSAOrder: IViewSAOrderDTO[];
    budgetItems: IBudgetItem[];
    isSoTaiChinh: boolean;
    checkUseMoreNoMBook: boolean;
    organizationUnits: IOrganizationUnit[];
    statisticCodes: IStatisticsCode[];
    noBookVoucher: string;
    autoPrinciples: IAutoPrinciple[];
    repositories: any[];
    units: IUnit[];
    creditAccountList: any[];
    debitAccountList: any[];
    isMoreForm: boolean;
    isFromPPDiscountReturn: boolean;
    isFromSaOrder: boolean;
    isFromSaInvoice: boolean;
    XUAT_KHO = TypeID.XUAT_KHO;
    XUAT_KHO_TU_DIEU_CHINH = TypeID.XUAT_KHO_TU_DIEU_CHINH;
    XUAT_KHO_TU_BAN_HANG = TypeID.XUAT_KHO_TU_BAN_HANG;
    XUAT_KHO_TU_HANG_MUA_TRA_LAI = TypeID.XUAT_KHO_TU_MUA_HANG;
    isViewFromRef: boolean;
    itemUnSelected: any[];
    checkNDGVBT: boolean;
    checkModalRef: NgbModalRef;
    isInputReason: boolean;
    ROLE_XEM = ROLE.XuatKho_XEM;
    ROLE_THEM = ROLE.XuatKho_THEM;
    ROLE_SUA = ROLE.XuatKho_SUA;
    ROLE_XOA = ROLE.XuatKho_XOA;
    ROLE_GHI_SO = ROLE.XuatKho_GHI_SO;
    ROLE_IN = ROLE.XuatKho_IN;
    ROLE_KETXUAT = ROLE.XuatKho_KET_XUAT;
    materialGoodsInStockTextCode: any;
    checkSave: boolean;
    checkPopupSLT: boolean;
    checkOpenModal: any = new Map();
    select: number;
    checkData: boolean;
    indexFocusDetailCol: any;
    indexFocusDetailRow: any;
    autoPrinciple: IAutoPrinciple;
    accountingObjectAlls: IAccountingObject[];
    listIDInputDeatil: any[] = [
        'productCode',
        'productName',
        'repository',
        'debitAccountList',
        'creditAccount',
        'unit',
        'quantity',
        'unitPrice',
        'mainConvertRate',
        'mainQuantity',
        'mainUnitPrice',
        'amounts',
        'lotNo',
        'expiryDate',
        'NoBookPPDiscountReturn',
        'NoBookSaOrder',
        'NoBookSaInvoice'
    ];
    listIDInputDeatilStatisTical: any[] = [
        'productCodes',
        'productNames',
        'expenseItems',
        'costSet',
        'contract',
        'budgetItem',
        'department',
        'statisticsCodes'
    ];

    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private currencyService: CurrencyService,
        private accountingObjectService: AccountingObjectService,
        private ppInvoiceDetailService: PPInvoiceDetailsService,
        private accountDefaultService: AccountDefaultService,
        private jhiAlertService: JhiAlertService,
        private materialGoodsService: MaterialGoodsService,
        private rsInwardOutwardService: RSInwardOutwardService,
        private toastrService: ToastrService,
        public translateService: TranslateService,
        private ppDiscountReturnDetailsService: PPDiscountReturnDetailsService,
        private saOrderDetailsService: SAOrderService,
        private saInvoiceDetailsService: SAInvoiceDetailsService,
        private materialQuantumDetailsService: MaterialQuantumDetailsService,
        private unitService: UnitService,
        private eventManager: JhiEventManager,
        private repositoryLedgerService: RepositoryLedgerService,
        public utilsService: UtilsService,
        private refModalService: RefModalService,
        private principal: Principal,
        private modalService: NgbModal,
        private costSetService: CostSetService,
        private expenseItemService: ExpenseItemService,
        private statisticsCodeService: StatisticsCodeService,
        private eMContractService: EMContractService,
        private budgetItemService: BudgetItemService,
        private autoPrincipleService: AutoPrincipleService,
        private organizationUnitService: OrganizationUnitService,
        private repositoryService: RepositoryService,
        private accountListService: AccountListService,
        private gLService: GeneralLedgerService
    ) {
        super();
        this.getSessionData();
        this.contextMenu = new ContextMenu();
        this.isViewFromRef = window.location.href.includes('/from-ref');
    }

    ngOnInit() {
        this.initValues();
        this.isEdit = true;
        this.translateService.get(['ebwebApp.outWard.home.title']).subscribe((res: any) => {
            this.rsInwardOutward.reason = res['ebwebApp.outWard.home.title'];
        });
        this.isEditUrl = window.location.href.includes('/edit') || window.location.href.includes('/view');
        this.isEdit = window.location.href.includes('new') || window.location.href.includes('edit');
        this.isCreateUrl = window.location.href.includes('/xuat-kho/new');
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.account = account;
                this.checkSLT = this.account.systemOption.find(x => x.code === HH_XUATQUASLTON).data === '1';
                if (data.rsInwardOutward && data.rsInwardOutward.id) {
                    this.rsInwardOutward = data && data.rsInwardOutward ? data.rsInwardOutward : {};
                    this.rsInwardOutwardDetails = this.rsInwardOutward.rsInwardOutwardDetails;
                    this.checkData = true;
                    for (let i = 0; i < this.rsInwardOutwardDetails.length; i++) {
                        if (this.isEdit) {
                            if (this.rsInwardOutwardDetails[i].ppDiscountReturn && this.rsInwardOutwardDetails[i].ppDiscountReturn.id) {
                                this.createFrom = 1;
                                break;
                            }
                            if (this.rsInwardOutwardDetails[i].saOrder && this.rsInwardOutwardDetails[i].saOrder.id) {
                                this.createFrom = 2;
                                break;
                            }
                            if (this.rsInwardOutwardDetails[i].saInvoice && this.rsInwardOutwardDetails[i].saInvoice.id) {
                                this.createFrom = 3;
                                break;
                            }
                            if (this.rsInwardOutwardDetails[i].materialQuantumID) {
                                this.createFrom = 7;
                                break;
                            }
                        }
                    }
                    if (this.rsInwardOutward) {
                        for (let i = 0; i < this.rsInwardOutwardDetails.length; i++) {
                            if (
                                this.rsInwardOutwardDetails[i].ppDiscountReturn !== null &&
                                this.rsInwardOutwardDetails[i].ppDiscountReturn !== undefined
                            ) {
                                this.isFromPPDiscountReturn = true;
                                if (this.isSoTaiChinh) {
                                    this.rsInwardOutwardDetails[i].noBookPPDiscountReturn = this.rsInwardOutwardDetails[
                                        i
                                    ].ppDiscountReturn.noMBook;
                                } else {
                                    this.rsInwardOutwardDetails[i].noBookPPDiscountReturn = this.rsInwardOutwardDetails[
                                        i
                                    ].ppDiscountReturn.noFBook;
                                }
                            }
                            if (
                                this.rsInwardOutwardDetails[i].saInvoice !== null &&
                                this.rsInwardOutwardDetails[i].saInvoice !== undefined
                            ) {
                                this.isFromSaInvoice = true;
                                if (this.isSoTaiChinh) {
                                    this.rsInwardOutwardDetails[i].noBookSaInvoice = this.rsInwardOutwardDetails[i].saInvoice.noMBook;
                                } else {
                                    this.rsInwardOutwardDetails[i].noBookSaInvoice = this.rsInwardOutwardDetails[i].saInvoice.noFBook;
                                }
                            }
                            if (this.rsInwardOutwardDetails[i].saOrder !== null && this.rsInwardOutwardDetails[i].saOrder !== undefined) {
                                this.isFromSaOrder = true;
                                this.rsInwardOutwardDetails[i].noBookSaOrder = this.rsInwardOutwardDetails[i].saOrder.no;
                            }
                        }
                    }
                    this.rsInwardOutwardService
                        .getRefVouchersByPPOrderID(this.rsInwardOutward.id)
                        .subscribe(res => (this.viewVouchersSelected = res.body));
                } else {
                    this.checkData = false;
                    this.rsInwardOutward.typeID = this.XUAT_KHO;
                    this.setDefaultDataFromSystemOptions();
                }
                this.rsInwardOutwardDataSetup();
                if (this.dataSession && this.dataSession.isEdit) {
                    this.edit();
                }
            });
        });
        this.afterSelectedIWVoucher();
        this.subcribeEvent();
        // Nhận event khi thêm nhanh
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = true;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilsService.setShowPopup(response.content);
        });
        this.registerIsShowPopup();
        this.registerAddNewRow();
        this.registerDeleteRow();
        this.registerCopyRow();
        this.registerSelectMaterialGoodsSpecification();
        this.registerMaterialGoodsSpecifications();
    }

    registerIsShowPopup() {
        this.utilsService.checkEvent.subscribe(res => {
            this.isShowPopup = res;
        });
    }

    resetCreateFrom() {
        this.createFrom = null;
    }

    createdFromPpDiscountReturn(response) {
        this.isFromPPDiscountReturn = true;
        this.isFromSaOrder = false;
        this.isFromSaInvoice = false;
        this.rsInwardOutwardDetails = [];
        for (const item of response.content) {
            const detail: any = {};
            detail.id = null;
            detail.refID1 = null;
            detail.refID2 = item.id;
            detail.no = item.book;
            detail.postedDate = moment(item.postedDate, DATE_FORMAT).format(DATE_FORMAT_SLASH);
            detail.date = moment(item.date, DATE_FORMAT).format(DATE_FORMAT_SLASH);
            detail.reason = item.reason;
            detail.typeGroupID = GROUP_TYPEID.GROUP_PPDISCOUNTRETURN;
            this.viewVouchersSelected.push(detail);
        }
        if (response.content && response.content.length) {
            this.ppDiscountReturnDetailsService
                .findAllDetailsById({
                    id: response.content.map(ppDiscountReturn => ppDiscountReturn.id)
                })
                .subscribe(res => {
                    for (const item of res.body.sort((a, b) => a.orderPriority - b.orderPriority)) {
                        const detail = new RSInwardOutWardDetails();
                        Object.assign(detail, item);
                        detail.id = null;
                        detail.ppDiscountReturnDetail = item;
                        detail.ppDiscountReturn = new PPDiscountReturn();
                        detail.ppDiscountReturn.id = item.ppDiscountReturnID;
                        detail.repository = this.repositories.find(data => data.id === detail.repositoryID);
                        detail.materialGood = this.materialGoodsInStock.find(data => data.id === detail.materialGoodsID);
                        detail.unit = this.units.find(data => data.id === detail.unitID);
                        detail.mainUnit = this.units.find(data => data.id === detail.mainUnitID);
                        detail.mainQuantity =
                            detail.formula === '*' ? detail.quantity * detail.mainConvertRate : detail.quantity / detail.mainConvertRate;
                        detail.expiryDate = moment(item.expiryDate);
                        this.rsInwardOutwardDetails.push(detail);
                        detail.amount = null;
                        detail.unitPriceOriginal = null;
                        this.getUnit();
                        this.updateSum();
                    }
                });
        }
    }

    createdFromSaOrder(response) {
        this.isFromSaOrder = true;
        this.isFromSaInvoice = false;
        this.isFromPPDiscountReturn = false;
        this.rsInwardOutwardDetails = [];
        for (const item of response.content) {
            const detail: any = {};
            detail.id = null;
            detail.refID1 = null;
            detail.refID2 = item.id;
            detail.no = item.no;
            detail.date = moment(item.date, DATE_FORMAT).format(DATE_FORMAT_SLASH);
            detail.reason = item.reason;
            detail.typeGroupID = GROUP_TYPEID.GROUP_SAORDER;
            this.viewVouchersSelected.push(detail);
        }
        if (response.content && response.content.length) {
            for (const item of response.content) {
                const detail = new RSInwardOutWardDetails();
                Object.assign(detail, item);
                if (response.content.find(data => data.sAOrderDetailID === detail.sAOrderDetailID)) {
                    detail.quantity = response.content.find(data => data.sAOrderDetailID === detail.sAOrderDetailID).quantityOut;
                }
                detail.id = null;
                detail.materialGood = this.materialGoodsInStock.find(data => data.id === detail.materialGoodsID);
                detail.saOrder = new SAOrder();
                detail.saOrder.id = item.id;
                detail.saOrderDetail = new SAOrderDetails();
                detail.saOrderDetail.id = item.sAOrderDetailID;
                detail.repository = this.repositories.find(data => data.id === detail.repositoryID);
                detail.unit = this.units.find(data => data.id === detail.unitID);
                detail.mainUnit = this.units.find(data => data.id === detail.mainUnitID);
                detail.mainQuantity =
                    detail.formula === '*' ? detail.quantity * detail.mainConvertRate : detail.quantity / detail.mainConvertRate;
                detail.unitPriceOriginal = null;
                detail.amount = null;
                detail.noBookSaOrder = item.no;
                this.rsInwardOutwardDetails.push(detail);
                this.getUnit();
                this.updateSum();
            }
        }
    }

    createdFromSaInvoice(response) {
        this.isFromSaInvoice = true;
        this.isFromSaOrder = false;
        this.isFromPPDiscountReturn = false;
        this.rsInwardOutwardDetails = [];
        for (const item of response.content) {
            const detail: any = {};
            detail.id = null;
            detail.refID1 = null;
            detail.refID2 = item.id;
            detail.no = item.book;
            detail.postedDate = moment(item.postedDate, DATE_FORMAT).format(DATE_FORMAT_SLASH);
            detail.date = moment(item.date, DATE_FORMAT).format(DATE_FORMAT_SLASH);
            detail.reason = item.reason;
            detail.typeGroupID = GROUP_TYPEID.GROUP_SAINVOICE;
            this.viewVouchersSelected.push(detail);
        }
        if (response.content && response.content.length) {
            this.saInvoiceDetailsService
                .findAllDetailsById({
                    id: response.content.map(saInvoice => saInvoice.id)
                })
                .subscribe(res => {
                    for (const item of res.body.sort((a, b) => a.orderPriority - b.orderPriority)) {
                        const detail = new RSInwardOutWardDetails();
                        Object.assign(detail, item);
                        detail.id = null;
                        detail.saInvoiceDetail = item;
                        detail.saInvoice = new SAInvoice();
                        detail.saInvoice.id = item.sAInvoiceId;
                        detail.materialGood = this.materialGoodsInStock.find(data => data.id === detail.materialGoodsID);
                        detail.repository = this.repositories.find(data => data.id === detail.repositoryID);
                        detail.unit = this.units.find(data => data.id === detail.unitID);
                        detail.mainUnit = this.units.find(data => data.id === detail.mainUnitID);
                        detail.mainQuantity =
                            detail.formula === '*' ? detail.quantity * detail.mainConvertRate : detail.quantity / detail.mainConvertRate;
                        detail.unitPriceOriginal = null;
                        detail.amount = null;
                        detail.expiryDate = moment(item.expiryDate);
                        this.rsInwardOutwardDetails.push(detail);
                        this.getUnit();
                        this.updateSum();
                    }
                });
        }
    }

    createdFromMaterialQuantum(response) {
        this.rsInwardOutwardDetails = [];
        if (response.content && response.content.length) {
            this.materialQuantumDetailsService
                .findAllDetailsById({
                    id: response.content.map(materialQuantum => materialQuantum.id)
                })
                .subscribe(res => {
                    // item is detail of materialQuantumDetails
                    for (const item of res.body) {
                        const detail = new RSInwardOutWardDetails();
                        Object.assign(detail, item);
                        detail.id = null;
                        detail.materialQuantumDetailID = item.id;
                        detail.materialQuantumID = item.materialQuantumID;
                        detail.materialGood = this.materialGoodsInStock.find(data => data.id === detail.materialGoodsID);
                        detail.unitPriceOriginal = item.unitPrice;
                        detail.mainUnitPrice = detail.unitPriceOriginal;
                        detail.unit = this.units.find(data => data.id === detail.unitID);
                        detail.repository = this.repositories.find(data => data.id === detail.repositoryID);
                        if (response.content.find(data => data.id === item.materialQuantumID)) {
                            detail.quantity = item.quantity * response.content.find(data => data.id === item.materialQuantumID).quantity;
                            detail.amount = detail.quantity * detail.unitPriceOriginal;
                        }
                        detail.debitAccount = this.debitAccountDefault;
                        detail.creditAccount = this.creditAccountDefault;
                        detail.mainConvertRate = 1;
                        detail.formula = '*';
                        detail.mainUnit = this.units.find(data => data.id === detail.unitID);
                        detail.mainQuantity =
                            detail.formula === '*' ? detail.quantity * detail.mainConvertRate : detail.quantity / detail.mainConvertRate;
                        this.rsInwardOutwardDetails.push(detail);
                        this.getUnit();
                        this.updateSum();
                    }
                });
        }
    }

    onChangeCreateFrom() {
        if (this.createFrom) {
            if (this.createFrom === 1) {
                this.accountingObjectService.getAccountingObjectsForProvider().subscribe(accountingObject => {
                    this.modalRef = this.refModalService.open(null, PpDiscountReturnModalComponent, {
                        accountObjects: accountingObject.body,
                        accountingObject: this.rsInwardOutward.accountingObject,
                        itemUnSelected: this.itemUnSelected,
                        itemsSelected: this.rsInwardOutwardDetails.filter(x => x.ppDiscountReturn)
                    });
                });
            } else if (this.createFrom === 2) {
                this.accountingObjectService.getAccountingObjectsActive().subscribe(accountingObject => {
                    const listObject = accountingObject.body.filter(item => [1, 2].includes(item.objectType));
                    this.modalRef = this.refModalService.open(null, EbSaOrderModalComponent, {
                        accountObjects: listObject,
                        accountingObject: this.rsInwardOutward.accountingObject,
                        itemsSelected: this.rsInwardOutwardDetails.filter(x => x.sAOrderDetailID),
                        fromScene: 'xuatKho'
                    });
                });
            } else if (this.createFrom === 3) {
                this.accountingObjectService.getAccountingObjectsActive().subscribe(accountingObject => {
                    this.modalRef = this.refModalService.open(null, SaInvoiceOutwardModalComponent, {
                        accountObjects: accountingObject.body,
                        accountingObject: this.rsInwardOutward.accountingObject,
                        itemsSelected: this.rsInwardOutwardDetails.filter(x => x.saInvoice)
                    });
                });
            } else if (this.createFrom === 4) {
            } else if (this.createFrom === 5) {
            } else if (this.createFrom === 6) {
            } else if (this.createFrom === 7) {
                this.modalRef = this.refModalService.open(null, MaterialQuantumModalComponent, {
                    itemsSelected: this.rsInwardOutwardDetails.filter(x => x.materialQuantumID)
                });
            }
        }
    }

    getAccount() {
        const columnList = [{ column: AccountType.TK_CO, ppType: false }, { column: AccountType.TK_NO, ppType: false }];
        const param = {
            typeID: this.XUAT_KHO,
            columnName: columnList
        };

        this.accountListService.getAccountTypeFour(param).subscribe(res => {
            this.creditAccountList = res.body.creditAccount;
            this.creditAccountDefault = res.body.creditAccountDefault;
            this.debitAccountDefault = res.body.debitAccountDefault;
            this.debitAccountList = res.body.debitAccount;
        });
    }

    copy() {
        this.rsInwardOutwardCopy = Object.assign({}, this.rsInwardOutward);
        this.rsInwardOutwardDetailsCopy = [...this.rsInwardOutwardDetails];
        this.viewVouchersSelectedCopy = [...this.viewVouchersSelected];
    }

    previousState(content) {
        if (this.rsInwardOutwardCopy && !this.utilsService.isShowPopup) {
            this.isClosing = true;
            if (
                !this.rsInwardOutwardCopy ||
                this.isMove ||
                (this.utilsService.isEquivalent(this.rsInwardOutward, this.rsInwardOutwardCopy) &&
                    this.utilsService.isEquivalentArray(this.rsInwardOutwardDetails, this.rsInwardOutwardDetailsCopy) &&
                    this.utilsService.isEquivalentArray(this.viewVouchersSelected, this.viewVouchersSelectedCopy))
            ) {
                this.closeAll();
                return;
            }
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else if (!this.utilsService.isShowPopup) {
            this.copy();
            this.closeAll();
            return;
        }
    }

    canDeactive() {
        if (this.isClosing || !this.rsInwardOutwardCopy || this.isClosed) {
            return true;
        }
        return (
            this.utilsService.isEquivalent(this.rsInwardOutward, this.rsInwardOutwardCopy) &&
            this.utilsService.isEquivalentArray(this.rsInwardOutwardDetails, this.rsInwardOutwardDetailsCopy) &&
            this.utilsService.isEquivalentArray(this.viewVouchersSelected, this.viewVouchersSelectedCopy)
        );
    }

    closeAll() {
        this.isClosed = true;
        this.dataSession = JSON.parse(sessionStorage.getItem('xuatKhoSearchData'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
        sessionStorage.removeItem('xuatKhoDataSession');
        this.router.navigate(
            ['/xuat-kho'],
            this.dataSession
                ? {
                      queryParams: {
                          page: this.page,
                          size: this.itemsPerPage,
                          sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                      }
                  }
                : {
                      queryParams: {
                          page: 0,
                          size: ITEMS_PER_PAGE,
                          sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                      }
                  }
        );
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.isClosing) {
            this.closeAll();
        }
        this.isClosing = false;
    }

    // checkNoBook(noFBook: string): boolean {
    //     const regExp = /^\D*\d{3,}.*$/;
    //     if (noFBook.length > 25) {
    //         return false;
    //     } else if (!regExp.test(noFBook)) {
    //         return false;
    //     } else {
    //         return regExp.test(noFBook);
    //     }
    // }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('xuatKhoDataSession'));
        if (!this.dataSession) {
            this.dataSession = JSON.parse(sessionStorage.getItem('xuatKhoSearchData'));
        }
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.searchData = this.dataSession.searchVoucher;
            this.predicate = this.dataSession.predicate;
            this.accountingObjectName = this.dataSession.accountingObjectName;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
    }

    rsInwardOutwardDataSetup() {
        this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
        this.checkNDGVBT = this.account.systemOption.some(x => x.code === VTHH_NHAP_DON_GIA_VON && x.data === '0');
        this.checkUseMoreNoMBook = this.account.systemOption.some(x => x.code === TCKHAC_SDSoQuanTri && x.data === '0');
        this.accountingObjects = [];
        if (this.rsInwardOutward.id) {
            this.isEdit = false;
            this.checkNDGVBT = true;
            this.noBookVoucher = this.isSoTaiChinh ? this.rsInwardOutward.noFBook : this.rsInwardOutward.noMBook;
        } else {
            this.rsInwardOutward.typeLedger = 2;
        }
        if (!this.isEditUrl) {
            this.genNewVoucerCode();
        }
        if (this.rsInwardOutward.rsInwardOutwardDetails) {
            this.rsInwardOutwardDetails = this.rsInwardOutward.rsInwardOutwardDetails;
            this.updateSum();
        }
        this.materialGoodsService.queryForComboboxGood({ materialsGoodsType: [0, 1, 3] }).subscribe(
            (res: HttpResponse<any>) => {
                this.materialGoodsInStock = res.body;
            },
            (res: HttpErrorResponse) => console.log(res.message)
        );
        this.accountingObjectService.getAllAccountingObjectsRSOutward().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjectAlls = res.body;
            if (this.checkData) {
                this.accountingObjects = this.accountingObjectAlls;
                this.rsInwardOutward.accountingObject = this.accountingObjects.find(n => n.id === this.rsInwardOutward.accountingObjectID);
            } else {
                this.accountingObjects = this.accountingObjectAlls
                    .filter(x => x.isActive)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            }
        });
        this.accountingObjectService.getAccountingObjectsForEmployee().subscribe((res: HttpResponse<AccountingObjectDTO[]>) => {
            this.employees = res.body;
            if (this.rsInwardOutward && this.rsInwardOutward.employeeID) {
                this.rsInwardOutward.accountingObjectEmployee = this.employees.find(item => item.id === this.rsInwardOutward.employeeID);
            }
        });

        this.autoPrincipleService.getByTypeAndCompanyID({ type: this.rsInwardOutward.typeID }).subscribe(res => {
            this.autoPrinciples = res.body;
            this.rsInwardOutward.autoPrinciple = this.autoPrinciples.find(item => item.autoPrincipleName === this.rsInwardOutward.reason);
        });
        this.repositoryService.getRepositoryCombobox().subscribe(res => {
            this.repositories = res.body;
        });
        this.unitService.convertRateForMaterialGoodsComboboxCustom().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
            this.getUnit();
        });
        this.getAccount();

        this.organizationUnitService.getOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body
                .filter(n => n.unitType === 4)
                .sort((a, b) => a.organizationUnitCode.localeCompare(b.organizationUnitCode));
        });

        this.expenseItemService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body.sort((a, b) => a.expenseItemCode.localeCompare(b.expenseItemCode));
        });
        this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body.sort((a, b) => a.costSetCode.localeCompare(b.costSetCode));
        });
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticCodes = res.body.sort((a, b) => a.statisticsCode.localeCompare(b.statisticsCode));
        });
        this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
            this.eMContracts = res.body;
        });
        this.budgetItemService.getBudgetItems().subscribe((res: HttpResponse<IBudgetItem[]>) => {
            this.budgetItems = res.body.sort((a, b) => a.budgetItemCode.localeCompare(b.budgetItemCode));
        });
    }

    genNewVoucerCode() {
        this.utilsService
            .getGenCodeVoucher({
                typeGroupID: this.TYPE_GROUP_RS_IO,
                displayOnBook: this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
            })
            .subscribe((res: HttpResponse<string>) => {
                this.noBookVoucher = res.body;
                if (!this.rsInwardOutward.id) {
                    this.copy();
                }
            });
    }

    getUnit() {
        if (this.rsInwardOutwardDetails && this.units) {
            this.rsInwardOutwardDetails.forEach(item => {
                item.convertRates = this.units.filter(data => data.materialGoodsID === item.materialGood.id);
            });
        }
    }

    initValues() {
        this.createFrom = null;
        this.isFromSaInvoice = false;
        this.isFromSaOrder = false;
        this.isFromPPDiscountReturn = false;
        this.viewVouchersSelected = [];
        this.rsInwardOutward = { typeID: this.XUAT_KHO, typeLedger: 2 };
        if (this.account && this.account.systemOption) {
            this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
        }
        this.quantitySum = 0;
        this.mainQuantitySum = 0;
        this.rsInwardOutward.totalAmount = 0;
        this.rsInwardOutward.accountingObject = {};
        this.rsInwardOutward.accountingObjectEmployee = {};
        this.rsInwardOutwardDetails = [];
        // this.lotNoArray = [];
        this.isSaving = false;
        this.isInputReason = false;
        this.setDefaultDataFromSystemOptions();
    }

    setDefaultDataFromSystemOptions() {
        if (this.account) {
            this.rsInwardOutward.date = this.utilsService.ngayHachToan(this.account);
            this.rsInwardOutward.postedDate = this.rsInwardOutward.date;
            if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                this.rsInwardOutward.currencyID = this.account.organizationUnit.currencyID;
                this.rsInwardOutward.exchangeRate = 1;
            }
            this.copy();
        }
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

    selectAccountingObjects() {
        this.rsInwardOutward.accountingObjectID = this.rsInwardOutward.accountingObject.id;
        this.rsInwardOutward.accountingObjectName = this.rsInwardOutward.accountingObject.accountingObjectName;
        this.rsInwardOutward.accountingObjectAddress = this.rsInwardOutward.accountingObject.accountingObjectAddress;
        this.rsInwardOutward.contactName = this.rsInwardOutward.accountingObject.contactName;
        if (!this.isInputReason) {
            this.rsInwardOutward.reason = this.translateService.instant('ebwebApp.rSInwardOutward.fillAutoReason', {
                name: this.rsInwardOutward.accountingObjectName
            });
        }
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.rsInwardOutwardDetails.length; i++) {
            total += this.rsInwardOutwardDetails[i][prop];
        }
        return total ? total : 0;
    }

    selectUnitPriceOriginal(index: number) {
        this.rsInwardOutwardDetails[index].unitPrice =
            this.rsInwardOutwardDetails[index].unitPriceOriginal * this.rsInwardOutward.exchangeRate;
        this.changeQuantity(index);
        if (
            this.rsInwardOutwardDetails[index].unit &&
            this.rsInwardOutwardDetails[index].unit.id === this.rsInwardOutwardDetails[index].mainUnit.id
        ) {
            this.rsInwardOutwardDetails[index].mainUnitPrice = this.rsInwardOutwardDetails[index].unitPriceOriginal;
        } else {
            if (this.rsInwardOutwardDetails[index].formula === '*') {
                this.rsInwardOutwardDetails[index].mainUnitPrice =
                    this.rsInwardOutwardDetails[index].unitPriceOriginal / this.rsInwardOutwardDetails[index].mainConvertRate;
            } else {
                this.rsInwardOutwardDetails[index].mainUnitPrice =
                    this.rsInwardOutwardDetails[index].unitPriceOriginal * this.rsInwardOutwardDetails[index].mainConvertRate;
            }
        }
        this.roundObject();
        this.updateSum();
    }

    changeLotNo(detail: IRSInwardOutWardDetails) {
        if (detail.lotNo) {
            const data = detail.lotNoArray.filter(x => x.lotNo === detail.lotNo);
            if (data && data.length > 0) {
                const selected = detail.lotNoArray.find(x => x.lotNo === detail.lotNo);
                detail.expiryDate = moment(selected.expiryDate);
            }
        }
    }

    onAmountChange(index) {
        this.rsInwardOutwardDetails[index].unitPriceOriginal =
            this.rsInwardOutwardDetails[index].amount / this.rsInwardOutwardDetails[index].quantity;
        if (this.rsInwardOutwardDetails[index].formula === '/') {
            this.rsInwardOutwardDetails[index].mainUnitPrice =
                this.rsInwardOutwardDetails[index].unitPriceOriginal * this.rsInwardOutwardDetails[index].mainConvertRate;
        } else {
            this.rsInwardOutwardDetails[index].mainUnitPrice =
                this.rsInwardOutwardDetails[index].unitPriceOriginal / this.rsInwardOutwardDetails[index].mainConvertRate;
        }
        this.roundObject();
        this.updateSum();
    }

    onRightClick($event, data, selectedData, isNew, isDelete, select) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        if (select === 2) {
            this.contextMenu.isCopy = false;
        } else {
            this.contextMenu.isCopy = true;
        }
        this.contextMenu.selectedData = selectedData;
        this.select = select;
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    selectUnit(index: number) {
        if (this.rsInwardOutwardDetails[index].unit) {
            if (this.rsInwardOutwardDetails[index].unit.id === this.rsInwardOutwardDetails[index].mainUnit.id) {
                this.rsInwardOutwardDetails[index].mainConvertRate = 1;
                this.rsInwardOutwardDetails[index].formula = '*';
                this.rsInwardOutwardDetails[index].mainQuantity = this.rsInwardOutwardDetails[index].quantity;
                this.rsInwardOutwardDetails[index].mainUnitPrice = this.rsInwardOutwardDetails[index].unitPriceOriginal;
            } else {
                this.rsInwardOutwardDetails[index].mainConvertRate = this.rsInwardOutwardDetails[index].unit.convertRate;
                this.rsInwardOutwardDetails[index].formula = this.rsInwardOutwardDetails[index].unit.formula;
                this.rsInwardOutwardDetails[index].unitId = this.rsInwardOutwardDetails[index].unit.id;
                if (this.rsInwardOutwardDetails[index].formula === '*') {
                    this.rsInwardOutwardDetails[index].mainQuantity =
                        this.rsInwardOutwardDetails[index].unit.convertRate * this.rsInwardOutwardDetails[index].quantity;
                    this.rsInwardOutwardDetails[index].mainUnitPrice =
                        this.rsInwardOutwardDetails[index].unitPriceOriginal * this.rsInwardOutwardDetails[index].mainConvertRate;
                } else {
                    this.rsInwardOutwardDetails[index].mainQuantity =
                        this.rsInwardOutwardDetails[index].quantity / this.rsInwardOutwardDetails[index].unit.convertRate;
                    this.rsInwardOutwardDetails[index].mainUnitPrice =
                        this.rsInwardOutwardDetails[index].unitPriceOriginal / this.rsInwardOutwardDetails[index].mainConvertRate;
                }
            }
        }
        this.changeQuantity(index);
    }

    // event thay doi select option value
    selectEmployee() {
        if (this.rsInwardOutward.accountingObjectEmployee) {
            this.rsInwardOutward.employeeID = this.rsInwardOutward.accountingObjectEmployee.id;
        } else {
            this.rsInwardOutward.employeeID = null;
        }
    }

    changeCurrency() {
        this.rsInwardOutward.exchangeRate = this.rsInwardOutward.currency ? this.rsInwardOutward.currency.exchangeRate : 1;
        this.rsInwardOutward.currencyID = this.rsInwardOutward.currency ? this.rsInwardOutward.currency.currencyCode : '';
        this.isCurrencyVND = this.rsInwardOutward.exchangeRate === 1;
        if (this.rsInwardOutwardDetails.length) {
            for (let i = 0; i < this.rsInwardOutwardDetails.length; i++) {
                this.getConvertRate(this.rsInwardOutwardDetails[i].materialGood.id, i);
            }
        }
    }

    changeExchangeRate() {
        for (let index = 0; index < this.rsInwardOutwardDetails.length; index++) {
            this.selectUnitPriceOriginal(index);
        }
    }

    selectedMaterialGoods(index?: number) {
        if (this.rsInwardOutwardDetails[index].materialGood) {
            const currentMaterialGoodsId = this.rsInwardOutwardDetails[index].materialGood.id;
            this.rsInwardOutwardDetails[index].materialGoodsID = currentMaterialGoodsId;
            this.rsInwardOutwardDetails[index].description = this.rsInwardOutwardDetails[index].materialGood.materialGoodsName;
            this.rsInwardOutwardDetails[index].creditAccount = this.rsInwardOutwardDetails[index].materialGood.reponsitoryAccount;
            this.rsInwardOutwardDetails[index].amount = 0;
            this.rsInwardOutwardDetails[index].lotNo = null;
            this.rsInwardOutwardDetails[index].expiryDate = null;
            this.rsInwardOutwardDetails[index].mainConvertRate = 1;
            this.rsInwardOutwardDetails[index].formula = '*';
            this.rsInwardOutwardDetails[index].repository = this.repositories.find(
                item => item.id === this.rsInwardOutwardDetails[index].materialGood.repositoryID
            );
            this.getConvertRate(currentMaterialGoodsId, index);
            this.repositoryLedgerService.getLotNoArray(this.rsInwardOutwardDetails[index].materialGoodsID).subscribe(res => {
                this.rsInwardOutwardDetails[index].lotNoArray = res.body;
            });
        }
        if (this.account.systemOption.some(x => x.code === PP_TINH_GIA_XUAT_KHO && x.data === CALCULATE_OW.DICH_DANH_CODE)) {
            this.indexDetail = index;
            this.modalRef = this.refModalService.open(
                null,
                EbSelectMaterialGoodsModalComponent,
                null,
                false,
                this.rsInwardOutward.typeID,
                null,
                this.rsInwardOutward.currencyID,
                this.rsInwardOutwardDetails[index].materialGoodsID ? this.rsInwardOutwardDetails[index].materialGoodsID : null
            );
        }
    }

    afterSelectedIWVoucher() {
        this.eventManager.subscribe('selectIWVoucher', ref => {
            const iWVoucher = ref.content;
            if (iWVoucher) {
                this.rsInwardOutwardDetails[this.indexDetail].unitPrice = iWVoucher.unitPrice;
                this.rsInwardOutwardDetails[this.indexDetail].unitPriceOriginal = iWVoucher.unitPrice;
                this.rsInwardOutwardDetails[this.indexDetail].amount = this.utilsService.round(
                    this.rsInwardOutwardDetails[this.indexDetail].quantity * iWVoucher.unitPrice,
                    this.account.systemOption,
                    7
                );
                this.rsInwardOutwardDetails[this.indexDetail].amountOriginal = this.utilsService.round(
                    this.rsInwardOutwardDetails[this.indexDetail].quantity *
                        this.rsInwardOutwardDetails[this.indexDetail].unitPriceOriginal,
                    this.account.systemOption,
                    7
                );
                this.rsInwardOutwardDetails[this.indexDetail].confrontID = iWVoucher.id;
                this.rsInwardOutwardDetails[this.indexDetail].confrontDetailID = iWVoucher.detailID;
                this.updateSum();
            }
        });
    }

    getConvertRate(currentMaterialGoodsId, index) {
        this.unitService.convertRateForMaterialGoodsComboboxCustom({ materialGoodsId: currentMaterialGoodsId }).subscribe(
            res => {
                this.rsInwardOutwardDetails[index].convertRates = res.body;
                this.rsInwardOutwardDetails[index].unit = this.rsInwardOutwardDetails[index].convertRates[0];
                if (this.rsInwardOutwardDetails[index].unit) {
                    this.rsInwardOutwardDetails[index].unitId = this.rsInwardOutwardDetails[index].unit.id;
                }
                this.rsInwardOutwardDetails[index].mainUnit = this.rsInwardOutwardDetails[index].convertRates[0];
                if (this.rsInwardOutwardDetails[index].mainUnit) {
                    this.rsInwardOutwardDetails[index].mainUnitId = this.rsInwardOutwardDetails[index].mainUnit.id;
                }
                this.updateSum();
                if (!this.rsInwardOutward.id) {
                    this.getUnitPriceOriginal(currentMaterialGoodsId, index);
                }
            },
            error => console.log(error)
        );
    }

    onChangeAutoPrinciple() {
        this.isInputReason = true;
        this.rsInwardOutward.reason = this.rsInwardOutward.autoPrinciple.autoPrincipleName;
        // this.rsInwardOutwardDetails.forEach(item => {
        //     item.debitAccount = this.rsInwardOutward.autoPrinciple.debitAccount;
        //     item.creditAccount = this.rsInwardOutward.autoPrinciple.creditAccount;
        // });

        for (let i = 0; i < this.rsInwardOutwardDetails.length; i++) {
            this.rsInwardOutwardDetails[i].debitAccount = this.rsInwardOutward.autoPrinciple.debitAccount;
            this.rsInwardOutwardDetails[i].creditAccount = this.rsInwardOutward.autoPrinciple.creditAccount;
        }
    }

    roundObject() {
        this.utilsService.roundObject(this.rsInwardOutwardDetails, this.account.systemOption);
        this.utilsService.roundObject(this.rsInwardOutward, this.account.systemOption);
    }

    getUnitPriceOriginal(currentMaterialGoodsId, index) {
        this.rsInwardOutwardDetails[index].unitPrices = [];
        this.rsInwardOutwardDetails[index].unitPrice = 0;
        this.rsInwardOutwardDetails[index].unitPriceOriginal = 0;

        this.unitService
            .unitPriceOriginalForMaterialGoods({
                materialGoodsId: currentMaterialGoodsId,
                unitId: this.rsInwardOutwardDetails[index].materialGood.unitID || '',
                currencyCode: this.rsInwardOutward.currencyID
            })
            .subscribe(
                res => {
                    for (const item of res.body) {
                        this.rsInwardOutwardDetails[index].unitPrices.push({ data: item });
                    }
                    // if (this.rsInwardOutwardDetails[index].unitPrices.length && this.rsInwardOutwardDetails[index].unitPrices[0]) {
                    //     this.rsInwardOutwardDetails[index].unitPriceOriginal = this.rsInwardOutwardDetails[index].unitPrices[0].data;
                    // }
                    if (res.body.length <= 0) {
                        this.rsInwardOutwardDetails[index].mainConvertRate = 1;
                        this.rsInwardOutwardDetails[index].formula = '*';
                    }
                    this.selectUnit(index);
                },
                error => console.log(error)
            );
    }

    onUnitChange(index) {
        this.rsInwardOutwardDetails[index].unitPrices = [];
        this.rsInwardOutwardDetails[index].unitPrice = 0;
        this.rsInwardOutwardDetails[index].unitPriceOriginal = 0;
        this.unitService
            .unitPriceOriginalForMaterialGoods({
                materialGoodsId: this.rsInwardOutwardDetails[index].materialGood.id,
                unitId: this.rsInwardOutwardDetails[index].unit.id || '',
                currencyCode: this.rsInwardOutward.currencyID
            })
            .subscribe(
                res => {
                    for (const item of res.body) {
                        this.rsInwardOutwardDetails[index].unitPrices.push({ data: item });
                    }
                    // if (this.rsInwardOutwardDetails[index].unitPrices.length && this.rsInwardOutwardDetails[index].unitPrices[0]) {
                    //     this.rsInwardOutwardDetails[index].unitPriceOriginal = this.rsInwardOutwardDetails[index].unitPrices[0].data;
                    // }
                    this.selectUnit(index);
                },
                error => console.log(error)
            );
    }

    changeQuantity(index) {
        if (this.rsInwardOutwardDetails[index].formula === '*') {
            this.rsInwardOutwardDetails[index].mainQuantity =
                this.rsInwardOutwardDetails[index].quantity * this.rsInwardOutwardDetails[index].mainConvertRate;
        } else {
            this.rsInwardOutwardDetails[index].mainQuantity =
                this.rsInwardOutwardDetails[index].quantity / this.rsInwardOutwardDetails[index].mainConvertRate;
        }
        // if (this.rsInwardOutwardDetails[index].quantity && this.rsInwardOutwardDetails[index].unitPriceOriginal) {
        this.rsInwardOutwardDetails[index].amountOriginal =
            this.rsInwardOutwardDetails[index].quantity * this.rsInwardOutwardDetails[index].unitPriceOriginal;
        this.rsInwardOutwardDetails[index].amount = this.rsInwardOutwardDetails[index].amountOriginal;
        // }
        this.roundObject();
        this.updateSum();
    }

    updateSum() {
        this.updateRSInWardOutWard();
    }

    convertRateChange(index) {
        if (this.rsInwardOutwardDetails[index].formula === '*') {
            this.rsInwardOutwardDetails[index].mainQuantity =
                this.rsInwardOutwardDetails[index].quantity * this.rsInwardOutwardDetails[index].mainConvertRate;
            this.rsInwardOutwardDetails[index].mainUnitPrice =
                this.rsInwardOutwardDetails[index].unitPriceOriginal / this.rsInwardOutwardDetails[index].mainConvertRate;
        } else {
            this.rsInwardOutwardDetails[index].mainQuantity =
                this.rsInwardOutwardDetails[index].quantity / this.rsInwardOutwardDetails[index].mainConvertRate;
            this.rsInwardOutwardDetails[index].mainUnitPrice =
                this.rsInwardOutwardDetails[index].unitPriceOriginal * this.rsInwardOutwardDetails[index].mainConvertRate;
        }
        this.roundObject();
        this.updateSum();
    }

    updateRSInWardOutWard() {
        this.setDefaultValue();
        this.rsInwardOutward.totalAmount = this.sum('amount');
        this.quantitySum = this.sum('quantity');
        this.mainQuantitySum = this.sum('mainQuantity');
        this.setDefaultValue();
        this.rsInwardOutward.totalAmountOriginal = this.rsInwardOutward.totalAmount;
    }

    mainQuantityChange(index) {
        if (this.rsInwardOutwardDetails[index].formula === '*') {
            this.rsInwardOutwardDetails[index].quantity =
                this.rsInwardOutwardDetails[index].mainQuantity / this.rsInwardOutwardDetails[index].mainConvertRate;
        } else {
            this.rsInwardOutwardDetails[index].quantity =
                this.rsInwardOutwardDetails[index].mainQuantity * this.rsInwardOutwardDetails[index].mainConvertRate;
        }
        this.rsInwardOutwardDetails[index].amount =
            this.rsInwardOutwardDetails[index].quantity * this.rsInwardOutwardDetails[index].unitPriceOriginal;
        this.roundObject();
        this.updateSum();
    }

    mainUnitPriceChange(index) {
        if (this.rsInwardOutwardDetails[index].formula === '*') {
            this.rsInwardOutwardDetails[index].unitPriceOriginal =
                this.rsInwardOutwardDetails[index].mainUnitPrice * this.rsInwardOutwardDetails[index].mainConvertRate;
        } else {
            this.rsInwardOutwardDetails[index].unitPriceOriginal =
                this.rsInwardOutwardDetails[index].mainUnitPrice / this.rsInwardOutwardDetails[index].mainConvertRate;
        }
        this.rsInwardOutwardDetails[index].amount =
            this.rsInwardOutwardDetails[index].quantity * this.rsInwardOutwardDetails[index].unitPriceOriginal;
        this.roundObject();
        this.updateSum();
    }

    setDefaultValue() {
        this.rsInwardOutward.totalAmount = this.rsInwardOutward.totalAmount || 0;
        this.quantitySum = this.quantitySum || 0;
        this.mainQuantitySum = this.mainQuantitySum || 0;
    }

    addNewRow(select: number, isRightClick?) {
        if (select === 0) {
            let lenght = 0;
            if (isRightClick) {
                this.rsInwardOutwardDetails.splice(this.indexFocusDetailRow + 1, 0, {});
                lenght = this.indexFocusDetailRow + 2;
            } else {
                const checkAcount = {
                    creditAccount: null,
                    debitAccount: null
                };
                if (this.creditAccountDefault) {
                    checkAcount.creditAccount = this.creditAccountDefault;
                } else if (this.rsInwardOutward && this.rsInwardOutward.autoPrinciple && this.rsInwardOutward.autoPrinciple.creditAccount) {
                    checkAcount.creditAccount = this.rsInwardOutward.autoPrinciple.creditAccount;
                }
                if (this.debitAccountDefault) {
                    checkAcount.debitAccount = this.debitAccountDefault;
                } else if (this.rsInwardOutward && this.rsInwardOutward.autoPrinciple && this.rsInwardOutward.autoPrinciple.debitAccount) {
                    checkAcount.debitAccount = this.rsInwardOutward.autoPrinciple.debitAccount;
                }
                this.rsInwardOutwardDetails.push(checkAcount);
                lenght = this.rsInwardOutwardDetails.length;
            }
            this.rsInwardOutwardDetails[lenght - 1].amountOriginal = 0;
            this.rsInwardOutwardDetails[lenght - 1].amount = 0;
            this.rsInwardOutwardDetails[lenght - 1].unitPrice = 0;
            this.rsInwardOutwardDetails[lenght - 1].unitPriceOriginal = 0;
            if (isRightClick && this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const lst = this.listIDInputDeatil;
                const col = this.indexFocusDetailCol;
                const row = this.indexFocusDetailRow + 1;
                this.indexFocusDetailRow = row;
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(lst[col] + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            } else {
                const nameTag: string = event.srcElement.id;
                const idx: number = this.rsInwardOutwardDetails.length - 1;
                const nameTagIndex: string = nameTag + String(idx);
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(nameTagIndex);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
        } else if (select === 1) {
            let lenght = 0;
            if (isRightClick) {
                this.rsInwardOutwardDetails.splice(this.indexFocusDetailRow + 1, 0, {});
                lenght = this.indexFocusDetailRow + 2;
            } else {
                this.rsInwardOutwardDetails.push({});
                lenght = this.rsInwardOutwardDetails.length;
            }
            this.rsInwardOutwardDetails[lenght - 1].amountOriginal = 0;
            this.rsInwardOutwardDetails[lenght - 1].amount = 0;
            this.rsInwardOutwardDetails[lenght - 1].unitPrice = 0;
            this.rsInwardOutwardDetails[lenght - 1].unitPriceOriginal = 0;
            if (isRightClick) {
                const lst = this.listIDInputDeatilStatisTical;
                const col = this.indexFocusDetailCol;
                const row = this.indexFocusDetailRow + 1;
                this.indexFocusDetailRow = row;
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(lst[col] + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            } else {
                const nameTag: string = event.srcElement.id;
                const idx: number = this.rsInwardOutwardDetails.length - 1;
                const nameTagIndex: string = nameTag + String(idx);
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(nameTagIndex);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
        }
    }

    onchangePostedDate() {
        this.rsInwardOutward.postedDate = this.rsInwardOutward.date;
    }

    removeRow(detail: object, select: number) {
        if (select === 0) {
            this.rsInwardOutwardDetails.splice(this.rsInwardOutwardDetails.indexOf(detail), 1);
            if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                // vì còn trường hợp = 0
                if (this.rsInwardOutwardDetails.length > 0) {
                    let row = 0;
                    if (this.indexFocusDetailRow > this.rsInwardOutwardDetails.length - 1) {
                        row = this.indexFocusDetailRow - 1;
                    } else {
                        row = this.indexFocusDetailRow;
                    }
                    const lst = this.listIDInputDeatil;
                    const col = this.indexFocusDetailCol;
                    setTimeout(function() {
                        const element: HTMLElement = document.getElementById(lst[col] + row);
                        if (element) {
                            element.focus();
                        }
                    }, 0);
                }
            }
        } else if (select === 1) {
            this.rsInwardOutwardDetails.splice(this.rsInwardOutwardDetails.indexOf(detail), 1);
            if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                // vì còn trường hợp = 0
                if (this.rsInwardOutwardDetails.length > 0) {
                    let row = 0;
                    if (this.indexFocusDetailRow > this.rsInwardOutwardDetails.length - 1) {
                        row = this.indexFocusDetailRow - 1;
                    } else {
                        row = this.indexFocusDetailRow;
                    }
                    const lst = this.listIDInputDeatilStatisTical;
                    const col = this.indexFocusDetailCol;
                    setTimeout(function() {
                        const element: HTMLElement = document.getElementById(lst[col] + row);
                        if (element) {
                            element.focus();
                        }
                    }, 0);
                }
            }
        } else if (select === 2) {
            this.viewVouchersSelected.splice(this.viewVouchersSelected.indexOf(detail), 1);
        }
        if (!this.rsInwardOutwardDetails.length) {
            this.createFrom = null;
        }
        this.updateSum();
    }

    edit() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.rsInwardOutward.postedDate)) {
            if (!(this.isEdit || this.rsInwardOutward.recorded)) {
                this.accountingObjects = this.accountingObjectAlls
                    .filter(x => x.isActive)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                this.isEdit = true;
                this.isMove = false;
                this.itemUnSelected = [];
                this.checkNDGVBT = this.account.systemOption.some(x => x.code === VTHH_NHAP_DON_GIA_VON && x.data === '0');
                this.copy();
                this.updateLotNoArray();
            }
        }
    }

    updateLotNoArray() {
        for (const item of this.rsInwardOutwardDetails) {
            this.repositoryLedgerService.getLotNoArray(item.materialGood.id).subscribe(res => {
                item.lotNoArray = res.body;
            });
        }
    }

    addNew($event) {
        $event.preventDefault();
        this.isEdit = true;
        this.initValues();
        this.genNewVoucerCode();
        this.isFromPPDiscountReturn = false;
        this.isFromSaOrder = false;
        this.isFromSaInvoice = false;
        this.focusFirstInput();
    }

    copyAndNew() {
        this.rsInwardOutward.id = null;
        this.rsInwardOutwardDetails.forEach(item => (item.id = null));
        this.isEdit = true;
        this.genNewVoucerCode();
        this.updateLotNoArray();
    }

    save(isNew = false) {
        event.preventDefault();
        if ((this.isCreateUrl && !this.utilsService.isShowPopup) || (this.isEditUrl && !this.utilsService.isShowPopup)) {
            this.recorded = false;
            this.utilsService
                .checkQuantityExistsTest1(this.rsInwardOutwardDetails, this.account, this.rsInwardOutward.postedDate)
                .then(data => {
                    if (data) {
                        this.materialGoodsInStockTextCode = data;
                        if (
                            !this.checkSLT &&
                            this.materialGoodsInStockTextCode &&
                            this.materialGoodsInStockTextCode.quantityExists &&
                            !this.checkPopupSLT
                        ) {
                            this.recorded = true;
                            this.checkOpenSave = true;
                            if (this.modalRef) {
                                this.modalRef.close();
                            }
                            this.modalRef = this.modalService.open(this.contentSaveModal, { backdrop: 'static' });
                            return false;
                        }
                        if (this.materialGoodsInStockTextCode && this.materialGoodsInStockTextCode.minimumStock && !this.checkPopupSLT) {
                            this.checkOpenSave = false;
                            if (this.modalRef) {
                                this.modalRef.close();
                            }
                            this.modalRef = this.modalService.open(this.contentSaveModal, { backdrop: 'static' });
                            return false;
                        }
                        this.saveAfter(isNew);
                    }
                });
        }
    }

    saveAfter(isNew = false) {
        this.rsInwardOutward.rsInwardOutwardDetails = this.rsInwardOutwardDetails;
        if (this.rsInwardOutward.typeLedger === 0 || this.isSoTaiChinh) {
            this.rsInwardOutward.noFBook = this.noBookVoucher;
            this.rsInwardOutward.noMBook = null;
        } else if (this.rsInwardOutward.typeLedger === 1 || !this.isSoTaiChinh) {
            this.rsInwardOutward.noMBook = this.noBookVoucher;
            this.rsInwardOutward.noFBook = null;
        }

        if (this.checkPostedDateGreaterDate()) {
            return;
        }

        if (!this.checkError()) {
            this.isClosing = false;
            if (this.modalRef) {
                this.modalRef.close();
            }
            return;
        }

        this.isSaving = true;
        const data = {
            rsInwardOutward: this.rsInwardOutward,
            viewVouchers: this.viewVouchersSelected,
            currentBook: this.isSoTaiChinh ? 0 : 1,
            checkRecordSLT: this.recorded
        };
        if (this.rsInwardOutward.id) {
            this.rsInwardOutwardService.update(data).subscribe(
                res => {
                    this.handleSuccessResponse(res, isNew);
                },
                error => {
                    this.handleError(error);
                }
            );
            this.checkPopupSLT = false;
        } else {
            this.rsInwardOutwardService.create(data).subscribe(
                res => {
                    this.handleSuccessResponse(res, isNew);
                },
                error => {
                    this.handleError(error);
                }
            );
            this.checkPopupSLT = false;
        }
    }

    checkPostedDateGreaterDate(): boolean {
        if (this.rsInwardOutward.postedDate < this.rsInwardOutward.date) {
            this.toastrService.error(this.translateService.instant('ebwebApp.common.error.dateAndPostDate'));
            return true;
        }
        return false;
    }

    checkError(): boolean {
        if (this.isSoTaiChinh) {
            if (!this.rsInwardOutward.noFBook) {
                this.toastrService.error(this.translateService.instant('global.data.null'));
                return;
            } else {
                if (!this.utilsService.checkNoBook(this.rsInwardOutward.noFBook)) {
                    return;
                }
            }
        } else {
            if (!this.rsInwardOutward.noMBook) {
                this.toastrService.error(this.translateService.instant('global.data.null'));
                return;
            } else {
                if (!this.utilsService.checkNoBook(this.rsInwardOutward.noMBook)) {
                    return;
                }
            }
        }

        if (this.checkCloseBook(this.account, this.rsInwardOutward.postedDate)) {
            this.toastrService.error(
                this.translateService.instant('ebwebApp.lockBook.err.checkForSave'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }

        if (!this.rsInwardOutwardDetails || !this.rsInwardOutwardDetails.length) {
            this.toastrService.error(
                this.translateService.instant('ebwebApp.pporder.error.nullDetail'),
                this.translateService.instant('ebwebApp.pporder.error.error')
            );
            return false;
        }

        if (!this.rsInwardOutward.date) {
            this.toastrService.warning(
                this.translateService.instant('global.combobox.dataNotExist'),
                this.translateService.instant('global.combobox.error')
            );
            return;
        }
        if (!this.rsInwardOutward.postedDate) {
            this.toastrService.warning(
                this.translateService.instant('global.combobox.dataNotExist'),
                this.translateService.instant('global.combobox.error')
            );
            return;
        }

        if (!this.rsInwardOutward.currencyID) {
            this.toastrService.warning(
                this.translateService.instant('global.combobox.dataNotExist'),
                this.translateService.instant('global.combobox.error')
            );
            return;
        }

        if (!this.rsInwardOutwardDetails || !this.rsInwardOutwardDetails.length) {
            this.toastrService.error(this.translateService.instant('ebwebApp.rsInwardOutward.error.rsInwardOutwardDetails'));
            return;
        }

        let orderPriority = 0;
        let materialGoodsSpecificationsError = '';
        for (const item of this.rsInwardOutwardDetails) {
            if (!item.materialGood) {
                this.toastrService.error(this.translateService.instant('ebwebApp.outWard.materialGoodsDoNotEmpty'));
                return false;
            } else if (
                !this.rsInwardOutward.id &&
                item.materialGood.isFollow &&
                (!item.materialGoodsSpecificationsLedgers || item.materialGoodsSpecificationsLedgers.length === 0)
            ) {
                materialGoodsSpecificationsError = materialGoodsSpecificationsError + ', ' + item.materialGood.materialGoodsCode;
            }
            if (!item.repository) {
                this.toastrService.error(this.translateService.instant('ebwebApp.outWard.repositoryDoNotEmpty'));
                return false;
            }
            if (!item.debitAccount) {
                this.toastrService.error(this.translateService.instant('ebwebApp.outWard.debitAccountListDoNotEmpty'));
                return false;
            }
            if (!item.creditAccount) {
                this.toastrService.error(this.translateService.instant('ebwebApp.outWard.creditAccountDoNotEmpty'));
                return false;
            }
            item.orderPriority = orderPriority;
            orderPriority++;
        }
        if (materialGoodsSpecificationsError) {
            materialGoodsSpecificationsError = materialGoodsSpecificationsError.substring(2, materialGoodsSpecificationsError.length);
            this.toastrService.error(
                'Hàng hóa ' +
                    materialGoodsSpecificationsError +
                    ' ' +
                    this.translateService.instant('ebwebApp.materialGoodsSpecifications.noMaterialGoodsSpecifications')
            );
            return false;
        }
        return true;
    }

    handleSuccessResponse(res, isNew) {
        this.isSaving = false;
        if (!this.rsInwardOutward.id) {
            this.toastrService.success(this.translateService.instant('ebwebApp.rSInwardOutward.created'));
        } else {
            this.toastrService.success(this.translateService.instant('ebwebApp.rSInwardOutward.updated'));
        }
        this.materialGoodsService.queryForComboboxGood({ materialsGoodsType: [0, 1, 3] }).subscribe(
            (response: HttpResponse<any>) => {
                this.materialGoodsInStock = response.body;
            },
            (response: HttpErrorResponse) => console.log(response.message)
        );
        if (isNew) {
            this.initValues();
            this.genNewVoucerCode();
        } else {
            this.isEdit = false;
            this.rsInwardOutward.id = res.body.id;
            this.rsInwardOutwardDetails = res.body.rsInwardOutwardDetails;
            for (let i = 0; i < this.rsInwardOutwardDetails.length; i++) {
                if (
                    this.rsInwardOutwardDetails[i].ppDiscountReturn !== null &&
                    this.rsInwardOutwardDetails[i].ppDiscountReturn !== undefined
                ) {
                    if (this.isSoTaiChinh) {
                        this.rsInwardOutwardDetails[i].noBookPPDiscountReturn = this.rsInwardOutwardDetails[i].ppDiscountReturn.noFBook;
                    } else {
                        this.rsInwardOutwardDetails[i].noBookPPDiscountReturn = this.rsInwardOutwardDetails[i].ppDiscountReturn.noMBook;
                    }
                }
                if (this.rsInwardOutwardDetails[i].saInvoice !== null && this.rsInwardOutwardDetails[i].saInvoice !== undefined) {
                    if (this.isSoTaiChinh) {
                        this.rsInwardOutwardDetails[i].noBookSaInvoice = this.rsInwardOutwardDetails[i].saInvoice.noFBook;
                    } else {
                        this.rsInwardOutwardDetails[i].noBookSaInvoice = this.rsInwardOutwardDetails[i].saInvoice.noMBook;
                    }
                }
                if (this.rsInwardOutwardDetails[i].saOrder !== null && this.rsInwardOutwardDetails[i].saOrder !== undefined) {
                    this.rsInwardOutwardDetails[i].noBookSaOrder = this.rsInwardOutwardDetails[i].saOrder.no;
                }
            }
            this.rsInwardOutward.recorded = res.body.recorded;
            const searchData = JSON.parse(this.searchData);
            this.rsInwardOutwardService
                .findRowNumOutWardByID({
                    fromDate: searchData.fromDate || '',
                    toDate: searchData.toDate || '',
                    noType: searchData.noType,
                    status: searchData.status,
                    accountingObject: searchData.accountingObject || '',
                    searchValue: searchData.searchValue || '',
                    id: this.rsInwardOutward.id
                })
                .subscribe((res2: HttpResponse<any>) => {
                    if (res2.body && res2.body !== -1) {
                        this.rowNum = res2.body;
                        this.totalItems = this.totalItems > this.rowNum ? this.totalItems : this.rowNum;
                    }
                });
            this.eventManager.broadcast({ name: 'RSInwardOutwardListModification' });
            this.copy();
            this.close();
        }
    }

    handleError(err) {
        this.isSaving = false;
        this.isClosing = false;
        this.resetCreateFrom();
        this.close();
        if (err.error.message !== 'error.noVoucherLimited') {
            this.toastrService.error(this.translateService.instant(`ebwebApp.rSInwardOutward.${err.error.message}`));
        }
    }

    move(direction: number) {
        if ((direction === -1 && this.rowNum === 1) || (direction === 1 && this.rowNum === this.totalItems)) {
            return;
        }
        this.rowNum += direction;
        const searchData = JSON.parse(this.searchData);
        return this.rsInwardOutwardService
            .findReferenceTablesID({
                fromDate: searchData.fromDate || '',
                toDate: searchData.toDate || '',
                noType: searchData.noType,
                status: searchData.status,
                accountingObject: searchData.accountingObject || '',
                searchValue: searchData.searchValue || '',
                rowNum: this.rowNum
            })
            .subscribe(
                (res: HttpResponse<any>) => {
                    const rsInwardOutward = res.body;
                    this.dataSession.rowNum = this.rowNum;
                    sessionStorage.setItem('xuatKhoDataSession', JSON.stringify(this.dataSession));
                    if (rsInwardOutward.typeID === this.XUAT_KHO) {
                        this.router.navigate(['/xuat-kho', rsInwardOutward.id, 'edit', this.rowNum]);
                    } else if (rsInwardOutward.typeID === this.XUAT_KHO_TU_BAN_HANG) {
                        this.router.navigate(['/chung-tu-ban-hang', rsInwardOutward.refID, 'edit-rs-inward-outward']);
                    } else if (rsInwardOutward.typeID === this.XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
                        this.router.navigate(['/hang-mua/tra-lai', rsInwardOutward.refID, 'edit-rs-inward-outward']);
                    } else if (rsInwardOutward.typeID === this.XUAT_KHO_TU_DIEU_CHINH) {
                    }
                },
                (res: HttpErrorResponse) => {
                    this.handleError(res);
                    this.getSessionData();
                }
            );
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    public beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            if (this.isEdit) {
                this.modalRef = this.refModalService.open(this.viewVouchersSelected);
            }
        }
    }

    subcribeEvent() {
        this.eventManager.subscribe('selectViewVoucher', response => {
            if (this.isEdit) {
                this.viewVouchersSelected = response.content;
            }
        });
        this.eventManager.subscribe('selectedPPDisCountReturn', response => {
            if (this.isEdit) {
                this.createdFromPpDiscountReturn(response);
            }
        });
        this.eventManager.subscribe('closedPPDisCountReturn', async ref => {
            this.resetCreateFrom();
            this.isFromPPDiscountReturn = false;
        });
        this.eventManager.subscribe('selectViewSAOrder', response => {
            if (this.isEdit) {
                this.createdFromSaOrder(response);
            }
        });
        this.eventManager.subscribe('closeSelectViewSAOrder', async ref => {
            this.resetCreateFrom();
            this.isFromSaOrder = false;
        });
        this.eventManager.subscribe('selectedSAInvoiceOutWard', response => {
            if (this.isEdit) {
                this.createdFromSaInvoice(response);
            }
        });
        this.eventManager.subscribe('closedSelectedSAInvoiceOutWard', async ref => {
            this.resetCreateFrom();
            this.isFromSaInvoice = false;
        });
        this.eventManager.subscribe('selectedMaterialQuantum', response => {
            if (this.isEdit) {
                this.createdFromMaterialQuantum(response);
            }
        });
        this.eventManager.subscribe('closedSelectedMaterialQuantum', async ref => {
            this.resetCreateFrom();
        });
        this.eventManager.subscribe('afterDeleteRow', response => {
            if (response.content.ppOrderDetailId && response.content.quantityFromDB) {
                this.itemUnSelected.push(response.content);
            }
            this.updateSum();
        });
    }

    record() {
        event.preventDefault();
        if (!this.isEdit) {
            if (!this.checkCloseBook(this.account, this.rsInwardOutward.postedDate)) {
                this.utilsService
                    .checkQuantityExistsTest1(this.rsInwardOutwardDetails, this.account, this.rsInwardOutward.postedDate)
                    .then(data => {
                        if (data) {
                            this.materialGoodsInStockTextCode = data;
                            if (!this.checkSLT && this.materialGoodsInStockTextCode.quantityExists) {
                                this.toastrService.error(
                                    this.translateService.instant('ebwebApp.pPDiscountReturnDetails.error.quantityExistsRecordErrorSave'),
                                    this.translateService.instant('ebwebApp.mBDeposit.message')
                                );
                                return;
                            }
                            if (!this.rsInwardOutward.recorded) {
                                const record_: Irecord = {};
                                record_.id = this.rsInwardOutward.id;
                                record_.typeID = this.XUAT_KHO;
                                record_.repositoryLedgerID = this.rsInwardOutward.id;
                                record_.msg = 'XUAT_KHO';
                                this.gLService.record(record_).subscribe((res: HttpResponse<Irecord>) => {
                                    if (res.body.success) {
                                        this.rsInwardOutward.recorded = true;
                                    }
                                });
                            }
                        }
                    });
            }
        }
    }

    unrecord() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.rsInwardOutward.postedDate)) {
            const record_: Irecord = {};
            record_.id = this.rsInwardOutward.id;
            record_.typeID = this.XUAT_KHO;
            record_.repositoryLedgerID = this.rsInwardOutward.id;
            record_.msg = 'XUAT_KHO';
            this.gLService.unrecord(record_).subscribe((res: HttpResponse<Irecord>) => {
                if (res.body.success) {
                    this.rsInwardOutward.recorded = false;
                }
            });
        }
    }

    openModel(content) {
        this.checkModalRef = this.modalService.open(content, { size: 'lg', backdrop: 'static' });
    }

    delete() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.rsInwardOutward.postedDate)) {
            this.rsInwardOutwardService.delete(this.rsInwardOutward.id).subscribe(
                response => {
                    this.toastrService.success(this.translateService.instant('ebwebApp.saReturn.deleted'));
                    this.checkModalRef.close();
                    this.closeAll();
                },
                error => {
                    this.checkModalRef.close();
                    console.log(error);
                }
            );
        }
    }

    saveAndNew() {
        event.preventDefault();
        this.save(true);
    }

    closeForm() {
        this.previousState(this.content);
    }

    printOutWard(isDownload: boolean, typeReports: number) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.rsInwardOutward.id,
                typeID: TypeID.XUAT_KHO,
                typeReport: typeReports
            },
            isDownload
        );
        switch (typeReports) {
            case RSOUTWARD_TYPE.TYPE_REPORT_ChungTuKeToan:
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.mBDeposit.financialPaper') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
                break;
            case RSOUTWARD_TYPE.TYPE_REPORT_XUAT_KHO:
            case RSOUTWARD_TYPE.TYPE_REPORT_XUAT_KHO_A5:
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.outWard.billOutWrad') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
                break;
        }
    }

    ngAfterViewInit(): void {
        if (this.isEdit) {
            this.focusFirstInput();
        }
    }

    addIfLastInput(i) {
        if (i === this.rsInwardOutwardDetails.length - 1) {
            this.addNewRow(0);
        }
    }

    copyRow(detail, select, checkCopy) {
        if (select === 0 || select === 1) {
            if (checkCopy) {
                if (!this.getSelectionText()) {
                    const addRow: any = Object.assign({}, detail);
                    addRow.id = null;
                    this.rsInwardOutwardDetails.push(addRow);
                    this.updateSum();
                    if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                        const lst = this.listIDInputDeatil;
                        const col = this.indexFocusDetailCol;
                        const row = this.rsInwardOutwardDetails.length - 1;
                        this.indexFocusDetailRow = row;
                        setTimeout(function() {
                            const element: HTMLElement = document.getElementById(lst[col] + row);
                            if (element) {
                                element.focus();
                            }
                        }, 0);
                    }
                }
            } else {
                if (this.contextMenu.isCopy) {
                    const addRow: any = Object.assign({}, detail);
                    addRow.id = null;
                    this.rsInwardOutwardDetails.push(addRow);
                    this.updateSum();
                    if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                        const lst = this.listIDInputDeatil;
                        const col = this.indexFocusDetailCol;
                        const row = this.rsInwardOutwardDetails.length - 1;
                        this.indexFocusDetailRow = row;
                        setTimeout(function() {
                            const element: HTMLElement = document.getElementById(lst[col] + row);
                            if (element) {
                                element.focus();
                            }
                        }, 0);
                    }
                }
            }
        }
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    changeStatusCheckSave() {
        this.checkSave = false;
        this.checkOpenModal = undefined;
    }

    openModelSave() {
        this.checkPopupSLT = true;
        this.modalRef.close();
        this.rsInwardOutward.recorded = false;
        this.saveAfter(false);
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.rsInwardOutwardDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.rsInwardOutward;
    }

    addDataToDetail() {
        this.rsInwardOutwardDetails = this.details ? this.details : this.rsInwardOutwardDetails;
        this.rsInwardOutward = this.parent ? this.parent : this.rsInwardOutward;
    }

    keyPress(detail, key, select?: number, checkCopy?) {
        switch (key) {
            case 'ctr + delete':
                this.removeRow(detail, select);
                break;
            case 'ctr + c':
                this.copyRow(detail, select, checkCopy);
                break;
            case 'ctr + insert':
                this.addNewRow(select, true);
                break;
        }
    }

    actionFocus(indexCol, indexRow) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
    }

    registerAddNewRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.addNewRow(this.select, true);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            this.removeRow(this.contextMenu.selectedData, this.select);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            this.copyRow(this.contextMenu.selectedData, this.select, false);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerSelectMaterialGoodsSpecification() {
        this.eventSubscriber = this.eventManager.subscribe('selectMaterialGoodsSpecification', response => {
            this.viewMaterialGoodsSpecification(this.contextMenu.selectedData);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerMaterialGoodsSpecifications() {
        this.eventManager.subscribe('materialGoodsSpecifications', ref => {
            const group = ref.content.reduce((g: any, materialGoodsSpecificationsLedger) => {
                g[materialGoodsSpecificationsLedger.iWRepositoryID] = g[materialGoodsSpecificationsLedger.iWRepositoryID] || [];
                g[materialGoodsSpecificationsLedger.iWRepositoryID].push(materialGoodsSpecificationsLedger);
                return g;
            }, {});
            const groupData = Object.keys(group).map(a => {
                return {
                    iWRepositoryID: a,
                    materialGoodsSpecificationsLedgers: group[a]
                };
            });
            for (let i = 0; i < groupData.length; i++) {
                const quantity = groupData[i].materialGoodsSpecificationsLedgers.reduce(function(prev, cur) {
                    return prev + cur.iWQuantity;
                }, 0);
                if (i === 0) {
                    this.rsInwardOutwardDetails[this.currentRow].quantity = quantity;
                    this.rsInwardOutwardDetails[this.currentRow].repositoryID = groupData[i].iWRepositoryID;
                    this.rsInwardOutwardDetails[this.currentRow].repository = this.repositories.find(
                        x => x.id === groupData[i].iWRepositoryID
                    );
                    this.rsInwardOutwardDetails[this.currentRow].materialGoodsSpecificationsLedgers =
                        groupData[i].materialGoodsSpecificationsLedgers;
                } else {
                    const rsInwardOutwardDetail = { ...this.rsInwardOutwardDetails[this.currentRow] };
                    rsInwardOutwardDetail.quantity = quantity;
                    rsInwardOutwardDetail.repositoryID = groupData[i].iWRepositoryID;
                    rsInwardOutwardDetail.repository = this.repositories.find(x => x.id === groupData[i].iWRepositoryID);
                    rsInwardOutwardDetail.materialGoodsSpecificationsLedgers = groupData[i].materialGoodsSpecificationsLedgers;
                    this.rsInwardOutwardDetails.push(rsInwardOutwardDetail);
                }
            }
        });
    }

    viewMaterialGoodsSpecification(detail) {
        if (detail.materialGood) {
            if (detail.materialGood.isFollow) {
                this.refModalService.open(
                    detail,
                    EbMaterialGoodsSpecificationsModalComponent,
                    detail.materialGoodsSpecificationsLedgers,
                    false,
                    2
                );
            } else {
                this.toastrService.error(this.translateService.instant('ebwebApp.materialGoodsSpecifications.notFollow'));
            }
        } else {
            this.toastrService.error(this.translateService.instant('ebwebApp.materialGoodsSpecifications.noMaterialGoods'));
        }
    }
}
