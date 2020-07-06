import { AfterViewChecked, AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { AccountingObjectDTO, IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ActivatedRoute, Router } from '@angular/router';
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
import { AccountType, GROUP_TYPEID, PPINVOICE_TYPE, REPORT, SO_LAM_VIEC, TCKHAC_SDSoQuanTri, TypeID } from 'app/app.constants';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { RepositoryService } from 'app/danhmuc/repository';
import { AccountListService } from 'app/danhmuc/account-list';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { ExpenseItemService } from 'app/entities/expense-item';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { EMContractService } from 'app/entities/em-contract';
import { BudgetItemService } from 'app/entities/budget-item';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { AccountingObjectBankAccountService } from 'app/entities/accounting-object-bank-account';
import { ExportInvoiceModalService } from 'app/core/login/export-invoice-modal.service';
import { CostSetService } from 'app/entities/cost-set';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { RSInwardOutward } from 'app/shared/model/rs-inward-outward.model';
import { RSInwardOutWardDetails } from 'app/shared/model/rs-inward-out-ward-details.model';
import { Irecord } from 'app/shared/model/record';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { PpOrderModalComponent } from '../../shared/modal/pp-order/pp-order-modal.component';
import { ONBROADCASTEVENT } from '../../muahang/mua-dich-vu/mua-dich-vu-event-name.constant';
import { PporderdetailService } from '../../entities/pporderdetail';
import { SaReturnModalComponent } from '../../shared/modal/sa-return/sa-return-modal.component';
import { HangBanTraLaiService } from '../../ban-hang/hang-ban-tra-lai-giam-gia/hang-ban-tra-lai.service';
import { ROLE } from 'app/role.constants';
import { PPOrderDetail } from 'app/shared/model/pporderdetail.model';
import { IPPOrderDto } from 'app/shared/modal/pp-order/pp-order-dto.model';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_FORMAT_SLASH } from 'app/shared';
import { SystemOption } from 'app/shared/model/system-option.model';
import { IUnit } from 'app/shared/model/unit.model';
import { SAOrder } from 'app/shared/model/sa-order.model';
import { SaReturn } from 'app/shared/model/sa-return.model';
import { RSInwardOutwardService } from 'app/entities/rs-inward-outward/rs-inward-outward.service';
import { ITEMS_PER_PAGE } from 'app/shared';
import { EbMaterialGoodsSpecificationsModalComponent } from 'app/shared/modal/material-goods-specifications/material-goods-specifications.component';

@Component({
    selector: 'eb-nhap-kho-update',
    templateUrl: './nhap-kho-update.component.html',
    styleUrls: ['./nhap-kho-update.component.css']
})
export class NhapKhoUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewInit, AfterViewChecked {
    @ViewChild('refids') refids;
    @ViewChild('content') content: NgbModalRef;
    @ViewChild('quantityConfirm') quantityConfirm;
    isSaving: boolean;
    dateDp: any;
    accountingObjects: AccountingObjectDTO[];
    employees: AccountingObjectDTO[];
    isCreateUrl: boolean;
    page: number;
    itemsPerPage: number;
    pageCount: number;
    totalItems: number;
    rowNum: number;
    predicate: any;
    reverse: any;
    accountingObjectName: any;
    dataSession: IDataSessionStorage;
    rsInwardOutward: RSInwardOutward;
    rsInwardOutwardCopy: RSInwardOutward;
    isEditUrl: boolean;
    deliverDate: any;
    date: any;
    rsInwardOutwardDetails: RSInwardOutWardDetails[];
    rsInwardOutwardDetailsCopy: RSInwardOutWardDetails[];
    materialGoodsInStock: IMaterialGoodsInStock[];
    isEdit: boolean;
    select: number;
    searchData: string;
    account: any;
    contextMenu: ContextMenu;
    quantitySum: number;
    mainQuantitySum: number;
    amountSum: number;
    amountOriginalSum: number;
    checkUseMoreNoMBook: boolean;
    modalRef: NgbModalRef;
    viewVouchersSelected: any;
    viewVouchersSelectedCopy: any;
    eventSubscriber: Subscription;
    isClosing: boolean;
    requiredInput: boolean;
    isMove: boolean;
    isSoTaiChinh: boolean;
    private TYPE_GROUP_RS_IO = 40;
    noBookVoucher: string;
    autoPrinciples: IAutoPrinciple[];
    repositories: any[];
    creditAccountList: any[];
    debitAccountList: any[];
    isLoading: boolean;
    expenseItems: IExpenseItem[];
    organizationUnits: IOrganizationUnit[]; // Phòng ban
    costSets: ICostSet[];
    statisticCodes: IStatisticsCode[];
    eMContracts: IEMContract[];
    budgetItems: IBudgetItem[];
    createFrom: number;
    isClosed: boolean;
    template: number;
    NHAP_KHO = TypeID.NHAP_KHO;
    NHAP_KHO_TU_DIEU_CHINH = TypeID.NHAP_KHO_TU_DIEU_CHINH;
    NHAP_KHO_TU_MUA_HANG = TypeID.NHAP_KHO_TU_MUA_HANG;
    NHAP_KHO_TU_BAN_HANG_TRA_LAI = TypeID.NHAP_KHO_TU_BAN_HANG_TRA_LAI;
    itemUnSelected: any[];
    isViewFromRef: boolean;
    checkModalRef: NgbModalRef;
    private record_: Irecord;
    isInputReason: boolean;
    createFromList: any;
    creditAccountDefault: string;
    debitAccountDefault: string;
    recorded: boolean;
    isFromSAReturn: boolean;
    isFromPPOrder: boolean;
    isCheckPopup: boolean;

    ROLE_XEM = ROLE.NhapKho_XEM;
    ROLE_THEM = ROLE.NhapKho_THEM;
    ROLE_SUA = ROLE.NhapKho_SUA;
    ROLE_XOA = ROLE.NhapKho_XOA;
    ROLE_GHI_SO = ROLE.NhapKho_GHI_SO;
    ROLE_IN = ROLE.NhapKho_IN;
    ROLE_KETXUAT = ROLE.NhapKho_KET_XUAT;
    REPORT = REPORT;
    checkData: boolean;
    accountingObjectAlls: IAccountingObject[];

    indexFocusDetailCol: any;
    indexFocusDetailRow: any;
    autoPrinciple: IAutoPrinciple;
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
        'amount',
        'lotNo',
        'expiryDate'
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
        private accountingObjectService: AccountingObjectService,
        private accountDefaultService: AccountDefaultService,
        private jhiAlertService: JhiAlertService,
        private materialGoodsService: MaterialGoodsService,
        private rsInwardOutwardService: RSInwardOutwardService,
        private toastrService: ToastrService,
        public translateService: TranslateService,
        private repositoryService: RepositoryService,
        private accountListService: AccountListService,
        private unitService: UnitService,
        private eventManager: JhiEventManager,
        public utilsService: UtilsService,
        private refModalService: RefModalService,
        private saReturnService: HangBanTraLaiService,
        private principal: Principal,
        private pporderdetailService: PporderdetailService,
        private expenseItemService: ExpenseItemService,
        private statisticsCodeService: StatisticsCodeService,
        private eMContractService: EMContractService,
        private budgetItemService: BudgetItemService,
        private gLService: GeneralLedgerService,
        private accountingObjectBankAccountService: AccountingObjectBankAccountService,
        private exportInvoiceModalService: ExportInvoiceModalService,
        private costSetService: CostSetService,
        private autoPrincipleService: AutoPrincipleService,
        private organizationUnitService: OrganizationUnitService,
        private modalService: NgbModal
    ) {
        super();
        this.getSessionData();
        this.contextMenu = new ContextMenu();
    }

    ngOnInit() {
        this.isFromSAReturn = false;
        this.isFromPPOrder = false;
        this.isViewFromRef = window.location.href.includes('/from-ref');
        this.template = 2;
        this.initValues();
        this.isEdit = true;
        this.isEditUrl = window.location.href.includes('/edit') || window.location.href.includes('/view');
        this.isEdit = window.location.href.includes('new') || window.location.href.includes('edit');
        this.isCreateUrl = window.location.href.includes('/nhap-kho/new');
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.account = account;
                if (data.rsInwardOutward && data.rsInwardOutward.id) {
                    this.checkData = true;
                    this.rsInwardOutwardDetails = data.rsInwardOutward.rsInwardOutwardDetails;
                    for (let i = 0; i < this.rsInwardOutwardDetails.length; i++) {
                        if (this.isEdit) {
                            if (this.rsInwardOutwardDetails[i].saReturnID) {
                                this.createFrom = 1;
                                break;
                            }
                            if (this.rsInwardOutwardDetails[i].ppOrder && this.rsInwardOutwardDetails[i].ppOrder.id) {
                                this.createFrom = 2;
                                break;
                            }
                        }
                    }
                    if (this.rsInwardOutward) {
                        this.checkNoBookDetails();
                    }
                    this.rsInwardOutward = data && data.rsInwardOutward ? data.rsInwardOutward : {};
                    this.rsInwardOutwardService
                        .getRefVouchersByPPOrderID(this.rsInwardOutward.id)
                        .subscribe(res => (this.viewVouchersSelected = res.body));
                } else {
                    this.checkData = false;
                    this.rsInwardOutward.typeID = this.NHAP_KHO;
                    this.setDefaultDataFromSystemOptions();
                }
                this.rsInwardOutwardDataSetup();
                if (this.dataSession && this.dataSession.isEdit) {
                    this.edit();
                }
            });
        });
        this.subcribeEvent();
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

    ngOnDestroy(): void {
        this.eventManager.destroy(this.eventSubscriber);
    }

    resetCreateFrom() {
        this.createFrom = 0;
    }

    subcribeEvent() {
        this.eventManager.subscribe('selectViewVoucher', response => {
            if (this.isEdit) {
                this.viewVouchersSelected = response.content;
            }
        });
        this.eventManager.subscribe('afterDeleteRow', response => {
            if (response.content.ppOrderDetailId && response.content.quantityFromDB) {
                this.itemUnSelected.push(response.content);
            }
            this.updateSum();
        });
        this.eventSubscriber = this.eventManager.subscribe(ONBROADCASTEVENT.selectedPPOrder, response =>
            this.onSubscribeModalPPOrder(response)
        );
        this.eventManager.subscribe('closePPOrder', async ref => {
            this.resetCreateFrom();
            this.isFromPPOrder = false;
        });
        this.eventSubscriber = this.eventManager.subscribe('selectedSaReturn', response => this.createdFromSaReturn(response));
    }

    createdFromSaReturn(response) {
        this.isFromSAReturn = true;
        this.isFromPPOrder = false;
        this.rsInwardOutwardDetails = [];
        if (response.content && response.content.length) {
            for (const item of response.content) {
                const details: any = {};
                details.id = null;
                details.refID1 = null;
                details.refID2 = item.id;
                details.no = item.book;
                details.date = moment(item.date, DATE_FORMAT).format(DATE_FORMAT_SLASH);
                details.postedDate = moment(item.postedDate, DATE_FORMAT).format(DATE_FORMAT_SLASH);
                details.reason = item.reason;
                details.typeGroupID = GROUP_TYPEID.GROUP_SARETURN;
                this.viewVouchersSelected.push(details);
            }
            this.saReturnService
                .findAllDetailsById({
                    id: response.content.map(saReturn => saReturn.id)
                })
                .subscribe(res => {
                    for (const item of res.body.sort((a, b) => a.orderPriority - b.orderPriority)) {
                        const detail = new RSInwardOutWardDetails();
                        Object.assign(detail, item);
                        detail.id = null;
                        detail.saReturnDetails = item;
                        detail.saReturn = new SaReturn();
                        detail.saReturn.id = item.saReturnID;
                        detail.saReturnDetailsID = item.id;
                        detail.saReturnID = item.saReturnID;
                        detail.materialGoodsID = item.materialGoodsID;
                        detail.description = item.description;
                        detail.unitPrice = item.unitPrice;
                        detail.mainUnitPrice = item.mainUnitPrice;
                        detail.mainConvertRate = item.mainConvertRate;
                        detail.formula = item.formula;
                        detail.repository = this.repositories.find(index => index.id === detail.repositoryID);
                        detail.quantity = item.quantity;
                        detail.mainQuantity =
                            detail.formula === '*' ? detail.quantity * detail.mainConvertRate : detail.quantity / detail.mainConvertRate;
                        detail.debitAccount = item.repositoryAccount;
                        detail.creditAccount = item.costAccount;
                        detail.unitPriceOriginal = item.unitPriceOriginal;
                        this.rsInwardOutwardDetails.push(detail);

                        this.unitService.find(item.unitID || '').subscribe(unit => {
                            if (item.unitID) {
                                detail.unit = unit.body;
                            }
                        });

                        this.unitService.find(item.mainUnitID || '').subscribe(unit => {
                            if (item.mainUnitID) {
                                detail.mainUnit = unit.body;
                            }
                        });

                        this.materialGoodsService.find(detail.materialGoodsID || '').subscribe(materialGood => {
                            if (detail.materialGoodsID) {
                                detail.materialGood = materialGood.body;
                            }
                            this.unitService
                                .unitPriceOriginalForMaterialGoods({
                                    materialGoodsId: detail.materialGoodsID,
                                    unitId: detail.materialGood && detail.materialGood.unitID ? detail.materialGood.unitID : '',
                                    currencyCode: this.rsInwardOutward.currencyID
                                })
                                .subscribe(
                                    units => {
                                        detail.unitPrices = [];
                                        for (const unit of units.body) {
                                            detail.unitPrices.push({ data: unit });
                                        }
                                        this.roundObject();
                                        detail.amount = detail.unitPriceOriginal * detail.quantity;
                                        this.roundObject();
                                        this.updateSum();
                                    },
                                    error => console.log(error)
                                );
                        });
                    }
                });
        }
    }

    onSubscribeModalPPOrder(response) {
        this.isFromPPOrder = true;
        this.isFromSAReturn = false;
        this.rsInwardOutwardDetails = [];
        if (response.content) {
            for (const item of response.content) {
                const details: any = {};
                details.id = null;
                details.refID1 = null;
                details.refID2 = item.ppOrderId;
                details.no = item.orderNumber;
                details.date = item.orderDate;
                details.reason = item.reason;
                details.typeGroupID = GROUP_TYPEID.GROUP_PPORDER;
                this.viewVouchersSelected.push(details);
            }
            let dataHaving = response.content.map(x => x.id);
            for (let i = 0; i < response.content.length; i++) {
                this.pporderdetailService.find(response.content[i].id).subscribe(res => {
                    const haveInList = this.rsInwardOutwardDetails.some(x => x.ppOrderDetailId === response.content[i].id);
                    if (!haveInList) {
                        this.rsInwardOutwardDetails.push(this.onSetUpPPOrderDetail(res.body, response.content[i]));
                        dataHaving = dataHaving.filter(x => x === response.content[i].id);
                    } else {
                        for (let j = 0; j < this.rsInwardOutwardDetails.length; j++) {
                            if (this.rsInwardOutwardDetails[j].ppOrderDetailId === response.content[i].id) {
                                this.rsInwardOutwardDetails[j] = this.onSetUpPPOrderDetail(
                                    res.body,
                                    response.content[i],
                                    this.rsInwardOutwardDetails[j]
                                );
                                dataHaving = dataHaving.filter(x => x === response.content[i].id);
                            }
                        }
                    }
                });
            }
            const listRemove = [];
            for (let i = 0; i < this.rsInwardOutwardDetails.length; i++) {
                if (
                    this.rsInwardOutwardDetails[i].ppOrderDetailId &&
                    !dataHaving.includes(this.rsInwardOutwardDetails[i].ppOrderDetailId)
                ) {
                    listRemove.push(this.rsInwardOutwardDetails[i]);
                }
            }
            this.rsInwardOutwardDetails = this.rsInwardOutwardDetails.filter(x => !listRemove.includes(x));
        }
    }

    onSetUpPPOrderDetail(ppOrderDetail: PPOrderDetail, dto: IPPOrderDto, oldRSIO?: RSInwardOutWardDetails): RSInwardOutWardDetails {
        const detail = oldRSIO || new RSInwardOutWardDetails();
        detail.ppOrder = new SAOrder();
        detail.ppOrder.id = dto.ppOrderId;
        detail.ppOrderDetail = new PPOrderDetail();
        detail.ppOrderDetail.id = ppOrderDetail.id;
        detail.materialGood = ppOrderDetail.materialGood;
        detail.materialGoodsID = detail.materialGood.id;
        detail.description = ppOrderDetail.description;
        detail.unit = ppOrderDetail.unit;
        detail.repository = this.repositories.find(item => item.id === detail.materialGood.repositoryID);
        detail.quantity = dto.receivedQuantity;
        detail.quantityReceipt = dto.quantityReceipt;
        detail.unitPrice = ppOrderDetail.unitPrice;
        detail.mainUnit = ppOrderDetail.mainUnit;
        detail.formula = ppOrderDetail.formula;
        detail.mainConvertRate = ppOrderDetail.mainConvertRate;
        detail.mainQuantity = detail.formula === '*' ? detail.quantity * detail.mainConvertRate : detail.quantity / detail.mainConvertRate;
        detail.mainUnitPrice = ppOrderDetail.mainUnitPrice;
        detail.creditAccount = this.rsInwardOutward.autoPrinciple ? this.rsInwardOutward.autoPrinciple.creditAccount : undefined;
        detail.debitAccount = this.rsInwardOutward.autoPrinciple ? this.rsInwardOutward.autoPrinciple.debitAccount : undefined;
        detail.unitPriceOriginal = ppOrderDetail.unitPriceOriginal;
        detail.bookPPOrder = dto.orderNumber;
        this.unitService
            .unitPriceOriginalForMaterialGoods({
                materialGoodsId: detail.materialGoodsID,
                unitId: detail.materialGood && detail.materialGood.unitID ? detail.materialGood.unitID : '',
                currencyCode: this.rsInwardOutward.currencyID
            })
            .subscribe(
                units => {
                    detail.unitPrices = [];
                    for (const item of units.body) {
                        detail.unitPrices.push({ data: item });
                    }
                    this.roundObject();
                    detail.amount = detail.unitPriceOriginal * detail.quantity;
                    this.roundObject();
                    this.updateSum();
                },
                error => console.log(error)
            );
        return detail;
    }

    createFromPPOrder(response) {
        this.resetCreateFrom();
        this.rsInwardOutwardDetails = [];
        if (response.content) {
            for (let i = 0; i < response.content.length; i++) {
                this.pporderdetailService.find(response.content[i].id).subscribe(res => {
                    const detail = new RSInwardOutWardDetails();
                    detail.ppOrderDetailId = res.body.id;
                    detail.ppOrderID = response.content[i].ppOrderId;
                    detail.materialGood = res.body.materialGood;
                    detail.materialGoodsID = detail.materialGood.id;
                    detail.description = res.body.description;
                    detail.unit = res.body.unit;
                    detail.quantity = response.content[i].receivedQuantity;
                    detail.quantityReceipt = response.content[i].quantityReceipt;
                    detail.unitPrice = res.body.unitPrice;
                    detail.mainUnit = res.body.mainUnit;
                    detail.formula = res.body.formula;
                    detail.mainConvertRate = res.body.mainConvertRate;
                    detail.mainQuantity =
                        detail.formula === '*' ? detail.quantity * detail.mainConvertRate : detail.quantity / detail.mainConvertRate;
                    detail.mainUnitPrice = res.body.mainUnitPrice;
                    detail.creditAccount = this.rsInwardOutward.autoPrinciple
                        ? this.rsInwardOutward.autoPrinciple.creditAccount
                        : undefined;
                    detail.debitAccount = this.rsInwardOutward.autoPrinciple ? this.rsInwardOutward.autoPrinciple.debitAccount : undefined;
                    detail.unitPriceOriginal = res.body.unitPriceOriginal;
                    this.rsInwardOutwardDetails.push(detail);
                    this.unitService
                        .unitPriceOriginalForMaterialGoods({
                            materialGoodsId: detail.materialGoodsID,
                            unitId: detail.materialGood && detail.materialGood.unitID ? detail.materialGood.unitID : '',
                            currencyCode: this.rsInwardOutward.currencyID
                        })
                        .subscribe(
                            units => {
                                detail.unitPrices = [];
                                for (const item of units.body) {
                                    detail.unitPrices.push({ data: item });
                                }
                                this.roundObject();
                                detail.amount = detail.unitPriceOriginal * detail.quantity;
                                this.roundObject();
                                this.updateSum();
                            },
                            error => console.log(error)
                        );
                });
            }
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

    onChangeCreateFrom() {
        if (this.createFrom) {
            if (this.createFrom === 2) {
                this.accountingObjectService.getAccountingObjectsForProvider().subscribe(accountingObject => {
                    this.modalRef = this.refModalService.open(null, PpOrderModalComponent, {
                        accountObjects: accountingObject.body,
                        itemUnSelected: this.itemUnSelected,
                        itemsSelected: this.rsInwardOutwardDetails.filter(x => x.ppOrderDetailId)
                    });
                });
            } else if (this.createFrom === 1) {
                this.accountingObjectService.getAccountingObjectsActive().subscribe(accountingObject => {
                    const listObject = accountingObject.body.filter(item => [1, 2].includes(item.objectType));
                    this.modalRef = this.refModalService.open(null, SaReturnModalComponent, {
                        accountObjects: listObject,
                        itemsSelected: this.rsInwardOutwardDetails.filter(x => x.saReturnID)
                    });
                });
            }
        }
    }

    getAccount(accountType: AccountType) {
        const columnList = [{ column: AccountType.TK_CO, ppType: false }, { column: AccountType.TK_NO, ppType: false }];
        const param = {
            typeID: this.NHAP_KHO,
            columnName: columnList
        };

        this.accountListService.getAccountTypeFour(param).subscribe(res => {
            this.creditAccountList = res.body.creditAccount;
            this.creditAccountDefault = res.body.creditAccountDefault;
            this.debitAccountDefault = res.body.debitAccountDefault;
            this.debitAccountList = res.body.debitAccount;
        });
    }

    onChangeAutoPrinciple() {
        this.isInputReason = true;
        this.rsInwardOutward.reason = this.rsInwardOutward.autoPrinciple.autoPrincipleName;
        this.rsInwardOutwardDetails.forEach(item => {
            item.debitAccount = this.rsInwardOutward.autoPrinciple.debitAccount;
            item.creditAccount = this.rsInwardOutward.autoPrinciple.creditAccount;
        });
    }

    copy() {
        this.rsInwardOutwardCopy = this.utilsService.deepCopyObject(this.rsInwardOutward);
        this.rsInwardOutwardDetailsCopy = this.utilsService.deepCopy(this.rsInwardOutwardDetails);
        this.viewVouchersSelectedCopy = this.utilsService.deepCopy(this.viewVouchersSelected);
    }

    closeForm() {
        event.preventDefault();
        this.previousState(this.content);
    }

    previousState(content) {
        if (this.rsInwardOutwardCopy && !this.utilsService.isShowPopup) {
            this.isClosing = true;
            if (this.isCheckPopup) {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(content, { backdrop: 'static' });
            } else if (
                this.isMove ||
                !this.rsInwardOutwardCopy ||
                (this.utilsService.isEquivalent(this.rsInwardOutward, this.rsInwardOutwardCopy) &&
                    this.utilsService.isEquivalentArray(this.rsInwardOutwardDetails, this.rsInwardOutwardDetailsCopy) &&
                    this.utilsService.isEquivalentArray(this.viewVouchersSelected, this.viewVouchersSelectedCopy))
            ) {
                this.closeAll();
                return;
            } else if (this.isEdit) {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(content, { backdrop: 'static' });
            } else {
                this.closeAll();
                return;
            }
        } else if (!this.utilsService.isShowPopup) {
            this.copy();
            this.closeAll();
            return;
        }
    }

    closeAll() {
        this.isClosed = true;
        this.dataSession = JSON.parse(sessionStorage.getItem('nhapKhoSearchData'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
        sessionStorage.removeItem('nhapKhoDataSession');
        this.router.navigate(
            ['/nhap-kho'],
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
    //     } else {
    //         return regExp.test(noFBook);
    //     }
    // }

    checkPostedDateGreaterDate(): boolean {
        if (this.rsInwardOutward.postedDate < this.rsInwardOutward.date) {
            this.toastrService.error(this.translateService.instant('ebwebApp.common.error.dateAndPostDate'));
            return true;
        }
        return false;
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('nhapKhoDataSession'));
        if (!this.dataSession) {
            this.dataSession = JSON.parse(sessionStorage.getItem('nhapKhoSearchData'));
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

    updateQuantityFromDB() {
        if (this.rsInwardOutwardDetails && this.rsInwardOutwardDetails.length) {
            this.rsInwardOutwardDetails.forEach(item => {
                item.quantityFromDB = item.quantity;
            });
        }
    }

    rsInwardOutwardDataSetup() {
        this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
        this.checkUseMoreNoMBook = this.account.systemOption.some(x => x.code === TCKHAC_SDSoQuanTri && x.data === '0');
        this.accountingObjects = [];
        if (this.rsInwardOutward.id) {
            this.isEdit = false;
            this.noBookVoucher = this.isSoTaiChinh ? this.rsInwardOutward.noFBook : this.rsInwardOutward.noMBook;
        } else {
            this.rsInwardOutward.typeLedger = 2;
        }
        if (!this.isEditUrl) {
            this.genNewVoucerCode();
        }
        if (this.rsInwardOutward.rsInwardOutwardDetails) {
            this.rsInwardOutwardDetails = this.rsInwardOutward.rsInwardOutwardDetails;
            this.updateQuantityFromDB();
            this.updateSum();
        }
        this.materialGoodsService.queryForComboboxGood({ materialsGoodsType: [0, 1, 3] }).subscribe(
            (res: HttpResponse<any>) => {
                this.materialGoodsInStock = res.body;
            },
            (res: HttpErrorResponse) => console.log(res.message)
        );
        this.accountingObjectService.getAllAccountingObjectsForRS().subscribe((res: HttpResponse<IAccountingObject[]>) => {
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
            this.copy();
        });
        this.repositoryService.getRepositoryCombobox().subscribe(res => {
            this.repositories = res.body;
        });
        this.getAccount(AccountType.TK_NO);
        this.getAccount(AccountType.TK_CO);

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

    initValues() {
        this.translateService.get(['ebwebApp.saReturn.home.title', 'ebwebApp.purchaseOrder.home.title']).subscribe((res: any) => {
            this.createFromList = [
                { name: res['ebwebApp.saReturn.home.title'], value: 1 },
                { name: res['ebwebApp.purchaseOrder.home.title'], value: 2 }
            ];
        });
        this.rsInwardOutward = { typeID: this.NHAP_KHO, typeLedger: 2 };
        if (this.account && this.account.systemOption) {
            this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
        }
        this.rsInwardOutward.accountingObject = {};
        this.rsInwardOutward.accountingObjectEmployee = {};
        this.rsInwardOutwardDetails = [];
        this.isSaving = false;
        this.isInputReason = false;
        this.translateService.get(['ebwebApp.rSInwardOutward.inward.hometitle']).subscribe((res: any) => {
            this.rsInwardOutward.reason = res['ebwebApp.rSInwardOutward.inward.hometitle'];
        });
        this.viewVouchersSelected = [];
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
        return this.account && this.rsInwardOutward.currencyID !== this.account.organizationUnit.currencyID;
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
        this.rsInwardOutward.accountingObjectCode = this.rsInwardOutward.accountingObject.accountingObjectCode;
        this.rsInwardOutward.contactName = this.rsInwardOutward.accountingObject.contactName;
        if (!this.isInputReason) {
            this.rsInwardOutward.reason = this.translateService.instant('ebwebApp.rSInwardOutward.defaultReason', {
                name: this.rsInwardOutward.accountingObjectName
            });
        }
    }

    updatePPOrder() {
        this.setDefaultValue();
        this.rsInwardOutward.totalAmount = this.sum('amount');
        this.quantitySum = this.sum('quantity');
        this.mainQuantitySum = this.sum('mainQuantity');
        this.setDefaultValue();
        this.rsInwardOutward.totalAmountOriginal = this.rsInwardOutward.totalAmount;
    }

    setDefaultValue() {
        this.rsInwardOutward.totalAmount = this.rsInwardOutward.totalAmount || 0;
        this.quantitySum = this.quantitySum || 0;
        this.mainQuantitySum = this.mainQuantitySum || 0;
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.rsInwardOutwardDetails.length; i++) {
            total += this.rsInwardOutwardDetails[i][prop];
        }
        return total ? total : 0;
    }

    selectUnitPriceOriginal(index: number) {
        this.changeQuantity(index);
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

    onchangePostedDate() {
        this.rsInwardOutward.postedDate = this.rsInwardOutward.date;
    }

    onRightClick($event, data, selectedData, isNew, isDelete, select) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        // if (select === 2) {
        //     this.contextMenu.isCopy = false;
        // } else {
        //     this.contextMenu.isCopy = true;
        // } =>> Bằng với this.contextMenu.isCopy = select !== 2;
        this.contextMenu.isCopy = select !== 2;
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
                if (this.rsInwardOutwardDetails[index].formula === '/') {
                    this.rsInwardOutwardDetails[index].mainQuantity =
                        this.rsInwardOutwardDetails[index].unit.convertRate / this.rsInwardOutwardDetails[index].quantity;
                    this.rsInwardOutwardDetails[index].mainUnitPrice =
                        this.rsInwardOutwardDetails[index].unitPriceOriginal * this.rsInwardOutwardDetails[index].mainConvertRate;
                } else {
                    this.rsInwardOutwardDetails[index].mainQuantity =
                        this.rsInwardOutwardDetails[index].quantity * this.rsInwardOutwardDetails[index].unit.convertRate;
                    this.rsInwardOutwardDetails[index].mainUnitPrice =
                        this.rsInwardOutwardDetails[index].unitPriceOriginal / this.rsInwardOutwardDetails[index].mainConvertRate;
                }
            }
        }
        this.changeQuantity(index);
    }

    // event thay doi select option value
    selectEmployee() {
        this.rsInwardOutward.employeeID = this.rsInwardOutward.accountingObjectEmployee.id;
    }

    selectedMaterialGoods(index) {
        const currentMaterialGoodsId = this.rsInwardOutwardDetails[index].materialGood.id;
        this.rsInwardOutwardDetails[index].materialGoodsID = currentMaterialGoodsId;
        this.rsInwardOutwardDetails[index].description = this.rsInwardOutwardDetails[index].materialGood.materialGoodsName;
        this.rsInwardOutwardDetails[index].debitAccount = this.rsInwardOutwardDetails[index].materialGood.reponsitoryAccount;
        this.rsInwardOutwardDetails[index].amount = 0;
        this.rsInwardOutwardDetails[index].lotNo = null;
        this.rsInwardOutwardDetails[index].expiryDate = null;
        this.rsInwardOutwardDetails[index].mainConvertRate = 1;
        this.rsInwardOutwardDetails[index].formula = '*';
        this.rsInwardOutwardDetails[index].repository = this.repositories.find(
            item => item.id === this.rsInwardOutwardDetails[index].materialGood.repositoryID
        );
        this.getConvertRate(currentMaterialGoodsId, index);
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
        if (this.rsInwardOutwardDetails[index].materialGoodsSpecificationsLedgers) {
            const iWQuantity = this.rsInwardOutwardDetails[index].materialGoodsSpecificationsLedgers.reduce(function(prev, cur) {
                return prev + cur.iWQuantity;
            }, 0);
            if (this.rsInwardOutwardDetails[index].quantity !== iWQuantity) {
                this.toastrService.error(this.translateService.instant('ebwebApp.materialGoodsSpecifications.notEqualsQuantity'));
                return;
            }
        }
        if (this.rsInwardOutwardDetails[index].formula === '*') {
            this.rsInwardOutwardDetails[index].mainQuantity =
                this.rsInwardOutwardDetails[index].quantity * this.rsInwardOutwardDetails[index].mainConvertRate;
        } else {
            this.rsInwardOutwardDetails[index].mainQuantity =
                this.rsInwardOutwardDetails[index].quantity / this.rsInwardOutwardDetails[index].mainConvertRate;
        }
        if (this.rsInwardOutwardDetails[index].quantity && this.rsInwardOutwardDetails[index].unitPriceOriginal) {
            this.rsInwardOutwardDetails[index].amountOriginal =
                this.rsInwardOutwardDetails[index].quantity * this.rsInwardOutwardDetails[index].unitPriceOriginal;
            this.rsInwardOutwardDetails[index].amount = this.rsInwardOutwardDetails[index].amountOriginal;
        }
        this.roundObject();
        this.updateSum();
    }

    roundObject() {
        this.utilsService.roundObject(this.rsInwardOutwardDetails, this.account.systemOption);
        this.utilsService.roundObject(this.rsInwardOutward, this.account.systemOption);
    }

    updateSum() {
        this.updatePPOrder();
    }

    addNewRow(select: number, isRightClick?) {
        if (select === 0) {
            let lenght = 0;
            if (isRightClick) {
                this.rsInwardOutwardDetails.splice(this.indexFocusDetailRow + 1, 0, {});
                lenght = this.indexFocusDetailRow + 2;
            } else {
                this.rsInwardOutwardDetails.push({
                    creditAccount: this.rsInwardOutward.autoPrinciple
                        ? this.rsInwardOutward.autoPrinciple.creditAccount
                        : this.creditAccountDefault,
                    debitAccount: this.rsInwardOutward.autoPrinciple
                        ? this.rsInwardOutward.autoPrinciple.debitAccount
                        : this.debitAccountDefault
                });
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
                this.copy();
            }
        }
    }

    addNew($event = null) {
        this.isEdit = true;
        this.initValues();
        this.genNewVoucerCode();
    }

    copyAndNew() {
        this.isCheckPopup = true;
        this.rsInwardOutward.id = null;
        this.rsInwardOutwardDetails.forEach(item => (item.id = null));
        this.isEdit = true;
        this.viewVouchersSelected = [];
        this.genNewVoucerCode();
    }

    save(isNew = false, isIgnoreQuantity = false) {
        event.preventDefault();
        if ((this.isCreateUrl && !this.utilsService.isShowPopup) || (this.isEditUrl && !this.utilsService.isShowPopup)) {
            this.recorded = false;
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

            if (this.modalRef) {
                this.modalRef.close();
            }

            if (!this.checkError(isIgnoreQuantity)) {
                this.isClosing = false;
                return;
            }
            for (const item of this.rsInwardOutwardDetails) {
                if (item.lotNo === '') {
                    item.lotNo = null;
                }
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
            } else {
                this.rsInwardOutwardService.create(data).subscribe(
                    res => {
                        this.handleSuccessResponse(res, isNew);
                    },
                    error => {
                        this.handleError(error);
                    }
                );
            }
        }
    }

    checkError(isIgnoreQuantity): boolean {
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
            this.toastrService.error(this.translateService.instant('ebwebApp.rsInwardOutward.error.date'));
            return;
        }
        if (!this.rsInwardOutward.postedDate) {
            this.toastrService.error(this.translateService.instant('ebwebApp.rsInwardOutward.error.postedDate'));
            return;
        }

        if (!this.rsInwardOutward.currencyID) {
            this.toastrService.error(this.translateService.instant('ebwebApp.rsInwardOutward.error.currencyID'));
            return;
        }
        if (!this.rsInwardOutward.exchangeRate) {
            this.toastrService.error(this.translateService.instant('ebwebApp.rsInwardOutward.error.exchangeRate'));
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

    checkNoBookDetails() {
        for (let i = 0; i < this.rsInwardOutwardDetails.length; i++) {
            if (this.rsInwardOutwardDetails[i].saReturn !== null && this.rsInwardOutwardDetails[i].saReturn !== undefined) {
                this.isFromSAReturn = true;
                if (this.isSoTaiChinh) {
                    this.rsInwardOutwardDetails[i].bookSaReturn = this.rsInwardOutwardDetails[i].saReturn.noMBook;
                } else {
                    this.rsInwardOutwardDetails[i].bookSaReturn = this.rsInwardOutwardDetails[i].saReturn.noFBook;
                }
            }
            if (this.rsInwardOutwardDetails[i].ppOrder !== null && this.rsInwardOutwardDetails[i].ppOrder !== undefined) {
                this.isFromPPOrder = true;
                this.rsInwardOutwardDetails[i].bookPPOrder = this.rsInwardOutwardDetails[i].ppOrder.no;
            }
        }
    }

    handleSuccessResponse(res, isNew) {
        this.isSaving = false;
        this.resetCreateFrom();
        if (!this.rsInwardOutward.id) {
            this.toastrService.success(this.translateService.instant('ebwebApp.rSInwardOutward.created'));
        } else {
            this.toastrService.success(this.translateService.instant('ebwebApp.rSInwardOutward.updated'));
        }
        if (isNew) {
            this.initValues();
            this.genNewVoucerCode();
        } else {
            this.isEdit = false;
            this.rsInwardOutward.id = res.body.id;
            this.rsInwardOutwardDetails = res.body.rsInwardOutwardDetails;
            this.updateQuantityFromDB();
            this.checkNoBookDetails();
            this.rsInwardOutward.recorded = res.body.recorded;
            const searchData = JSON.parse(this.searchData);
            this.rsInwardOutwardService
                .findRowNumByID({
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
            .findReferenceTableID({
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
                    console.log(res.body);
                    const rsInwardOutward = res.body;
                    this.dataSession.rowNum = this.rowNum;
                    sessionStorage.setItem('nhapKhoDataSession', JSON.stringify(this.dataSession));
                    if (rsInwardOutward.typeID === this.NHAP_KHO) {
                        this.router.navigate(['/nhap-kho', rsInwardOutward.id, 'edit', this.rowNum]);
                    } else if (rsInwardOutward.typeID === this.NHAP_KHO_TU_DIEU_CHINH) {
                    } else if (rsInwardOutward.typeID === this.NHAP_KHO_TU_MUA_HANG) {
                        this.router.navigate(['/mua-hang/qua-kho', rsInwardOutward.refID, 'edit-rs-inward']);
                    } else if (rsInwardOutward.typeID === this.NHAP_KHO_TU_BAN_HANG_TRA_LAI) {
                        this.router.navigate(['/hang-ban/tra-lai', rsInwardOutward.refID, 'edit-rs-inward']);
                    }
                    // this.rsInwardOutward = res.body;
                    // this.dataSession.rowNum = this.rowNum;
                    // sessionStorage.setItem('nhapKhoDataSession', JSON.stringify(this.dataSession));
                    // this.isEditUrl = true;
                    // this.rsInwardOutwardDataSetup();
                    // this.isMove = true;
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

    record() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.rsInwardOutward.postedDate)) {
            this.isLoading = true;
            this.record_ = {};
            this.record_.id = this.rsInwardOutward.id;
            this.record_.typeID = this.NHAP_KHO;
            this.record_.repositoryLedgerID = this.rsInwardOutward.id;
            this.gLService.record(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                if (res.body.success) {
                    this.rsInwardOutward.recorded = true;
                }
                this.isLoading = false;
            }, error => (this.isLoading = false));
        }
    }

    unrecord() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.rsInwardOutward.postedDate)) {
            this.isLoading = true;
            this.record_ = {};
            this.record_.id = this.rsInwardOutward.id;
            this.record_.typeID = this.NHAP_KHO;
            this.record_.repositoryLedgerID = this.rsInwardOutward.id;
            this.gLService.unrecord(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                if (res.body.success) {
                    this.rsInwardOutward.recorded = false;
                }
                this.isLoading = false;
            }, error => (this.isLoading = false));
        }
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

    changeTemplate(template: number) {
        this.template = template;
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

    exportPdf(isDownload: boolean, typeReports: number) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.rsInwardOutward.id,
                typeID: TypeID.NHAP_KHO,
                typeReport: typeReports
            },
            isDownload
        );
        switch (typeReports) {
            case REPORT.ChungTuKeToan:
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.mBDeposit.financialPaper') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
                break;
            case REPORT.PhieuNhapKho:
            case REPORT.PhieuNhapKhoA5:
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.pPInvoice.phieuNhapKho') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
        }
    }

    copyRow(detail, select, checkCopy?) {
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

    ngAfterViewInit(): void {
        if (this.isEdit) {
            this.focusFirstInput();
        }
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

    viewMaterialGoodsSpecification(detail) {
        if (this.isEdit) {
            if (detail.materialGood) {
                if (detail.materialGood.isFollow) {
                    this.refModalService.open(
                        detail,
                        EbMaterialGoodsSpecificationsModalComponent,
                        detail.materialGoodsSpecificationsLedgers,
                        false,
                        1
                    );
                } else {
                    this.toastrService.error(this.translateService.instant('ebwebApp.materialGoodsSpecifications.notFollow'));
                }
            } else {
                this.toastrService.error(this.translateService.instant('ebwebApp.materialGoodsSpecifications.noMaterialGoods'));
            }
        }
    }

    registerMaterialGoodsSpecifications() {
        this.eventManager.subscribe('materialGoodsSpecifications', ref => {
            this.rsInwardOutwardDetails[this.currentRow].materialGoodsSpecificationsLedgers = ref.content;
            this.rsInwardOutwardDetails[this.currentRow].quantity = ref.content.reduce(function(prev, cur) {
                return prev + cur.iWQuantity;
            }, 0);
        });
    }
}
