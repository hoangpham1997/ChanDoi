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
import { IMaterialGoods, IMaterialGoodsInStock, MaterialGoods } from 'app/shared/model/material-goods.model';
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
    TCKHAC_SDTichHopHDDT,
    RSTRANSFER_TYPE,
    TCKHAC_GhiSo
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
import { el } from '@angular/platform-browser/testing/src/browser_util';
import { EbSelectMaterialGoodsModalComponent } from 'app/shared/modal/select-material-goods/select-material-goods.component';
import { ROLE } from 'app/role.constants';
import { SAInvoice } from 'app/shared/model/sa-invoice.model';
import { ISAOrderDetails, SAOrderDetails } from 'app/shared/model/sa-order-details.model';
import { SAOrder } from 'app/shared/model/sa-order.model';
import { IMCPayment } from 'app/shared/model/mc-payment.model';

import { IRSTransfer, RSTransfer } from 'app/shared/model/rs-transfer.model';
import { IRSTransferDetails, RSTransferDetails } from 'app/shared/model/rs-transfer-details.model';
import { TransportMethodService } from 'app/danhmuc/transport-method';
import { TransportMethod } from 'app/shared/model/transport-method.model';
import { RsTransferService } from 'app/kho/chuyen-kho/rs-transfer.service';
import { IaPublishInvoiceDetails } from 'app/shared/model/ia-publish-invoice-details.model';
import { IaPublishInvoiceDetailsService } from 'app/ban-hang/xuat-hoa-don/ia-publish-invoice-details.service';
import { SaBill } from 'app/shared/model/sa-bill.model';
import index from '@angular/cli/lib/cli';
import { IMaterialGoodsConvertUnit } from 'app/shared/model/material-goods-convert-unit.model';
import { MaterialGoodsConvertUnitService } from 'app/entities/material-goods-convert-unit';
import { ITEMS_PER_PAGE } from 'app/shared';

@Component({
    selector: 'eb-chuyen-kho-update',
    templateUrl: './chuyen-kho-update.component.html',
    styleUrls: ['./chuyen-kho-update.component.css']
})
export class ChuyenKhoUpdateComponent extends BaseComponent implements OnInit, AfterViewInit, AfterViewChecked {
    @ViewChild('content') content: NgbModalRef;
    @ViewChild('deleteItem') deleteItem: NgbModalRef;
    @ViewChild('contentSave') contentSaveModal: TemplateRef<any>;
    isSaving: boolean;
    //     dateDp: any;
    //     accountDefaults: string[] = [];
    accountingObjects: AccountingObjectDTO[];
    employees: AccountingObjectDTO[];
    transportMethods: TransportMethod[];
    //     currencies: ICurrency[];
    //     totalvatamount: number;
    //     totalvatamountoriginal: number;
    isCreateUrl: boolean;
    isLoading: boolean;
    isCurrencyVND: boolean;
    page: number;
    sysRecord: any;
    itemsPerPage: number;
    pageCount: number;
    totalItems: number;
    rowNum: number;
    predicate: any;
    reverse: any;
    stockType: number;
    accountingObjectName: any;
    recorded: boolean;
    dataSession: IDataSessionStorage;
    rsTransferCopy: RSTransfer;
    rsTransferDetails: RSTransferDetails[];
    rsTransferDetailsCopy: RSTransferDetails[];
    isEditUrl: boolean;
    currency: ICurrency;
    deliverDate: any;
    isClosed: boolean;
    creditAccountDefault: string;
    debitAccountDefault: string;
    date: any;
    checkNDGVBT: boolean;
    indexDetail: number;
    materialGoodsInStock: IMaterialGoodsInStock[];
    isEdit: boolean;
    checkCopyAndNew: boolean;
    checkAddNew: boolean;
    searchData: string;
    rsTransfer: RSTransfer;
    account: any;
    checkSaveAndNew: boolean;
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
    private TYPE_GROUP_RS_TRANSFER = 42;
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
    materialGoodsConvertUnits: IMaterialGoodsConvertUnit[];
    units: IUnit[];
    creditAccountList: any[];
    debitAccountList: any[];
    isMoreForm: boolean;
    CHUYEN_KHO_KIEM_VAN_CHUYEN = TypeID.CHUYEN_KHO_KIEM_VAN_CHUYEN;
    CHUYEN_KHO_GUI_DAI_LY = TypeID.CHUYEN_KHO_GUI_DAI_LY;
    CHUYEN_KHO_NOI_BO = TypeID.CHUYEN_KHO_NOI_BO;
    isViewFromRef: boolean;
    //     itemUnSelected: any[];
    // checkNDGVBT: boolean;
    checkModalRef: NgbModalRef;
    isInputReason: boolean;
    ROLE_XEM = ROLE.ChuyenKho_XEM;
    ROLE_THEM = ROLE.ChuyenKho_THEM;
    ROLE_SUA = ROLE.ChuyenKho_SUA;
    ROLE_XOA = ROLE.ChuyenKho_XOA;
    ROLE_GHI_SO = ROLE.ChuyenKho_GHI_SO;
    ROLE_IN = ROLE.ChuyenKho_IN;
    ROLE_KETXUAT = ROLE.ChuyenKho_KET_XUAT;
    materialGoodsInStockTextCode: any;
    checkSave: boolean;
    checkPopupSLT: boolean;
    checkOpenModal: any = new Map();
    select: number;
    templates: IaPublishInvoiceDetails[];
    template: IaPublishInvoiceDetails;
    saBill: SaBill;
    isRequiredInvoiceNo: boolean;
    RSTRANSFER_TYPE = RSTRANSFER_TYPE;
    DDSo_DonGia = 1; // Đơn giá - 1
    DDSo_DonGiaNT = 2; // Đơn giá ngoại tệ - 2
    priceOriginalType = this.DDSo_DonGia;
    priceType = this.DDSo_DonGiaNT;
    checkStock: boolean;
    checkData: boolean;
    accountingObjectAlls: IAccountingObject[];

    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private currencyService: CurrencyService,
        private accountingObjectService: AccountingObjectService,
        private transportMethodService: TransportMethodService,
        private ppInvoiceDetailService: PPInvoiceDetailsService,
        private accountDefaultService: AccountDefaultService,
        private jhiAlertService: JhiAlertService,
        private materialGoodsService: MaterialGoodsService,
        private rsTransferService: RsTransferService,
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
        private gLService: GeneralLedgerService,
        private iaPublishInvoiceDetailsService: IaPublishInvoiceDetailsService,
        public materialGoodsConvertUnitService: MaterialGoodsConvertUnitService
    ) {
        super();
        this.getSessionData();
        this.contextMenu = new ContextMenu();
        this.isViewFromRef = window.location.href.includes('/from-ref');
    }

    ngOnInit() {
        this.initValues();
        this.isEdit = true;
        this.isEditUrl = window.location.href.includes('/edit') || window.location.href.includes('/view');
        this.isEdit = window.location.href.includes('new');
        this.isCreateUrl = window.location.href.includes('/chuyen-kho/new');
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.account = account;
                this.isRequiredInvoiceNo = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '0');
                this.checkSLT = this.account.systemOption.find(x => x.code === HH_XUATQUASLTON).data === '1';
                if (data.rsTransfer && data.rsTransfer.id) {
                    this.checkData = true;
                    this.rsTransfer = data && data.rsTransfer ? data.rsTransfer : {};
                    if (this.rsTransfer.invoiceDate) {
                        this.rsTransfer.invoiceDate = moment(this.rsTransfer.invoiceDate);
                    }
                    this.rsTransferDetails = this.rsTransfer.rsTransferDetails;
                    if (this.rsTransfer) {
                        if (this.rsTransfer.typeID === this.CHUYEN_KHO_KIEM_VAN_CHUYEN) {
                            this.stockType = 0;
                        } else if (this.rsTransfer.typeID === this.CHUYEN_KHO_GUI_DAI_LY) {
                            this.stockType = 1;
                        } else {
                            this.stockType = 2;
                        }
                    }
                    this.rsTransferService
                        .getRefVouchersByPPOrderID(this.rsTransfer.id)
                        .subscribe(res => (this.viewVouchersSelected = res.body));
                } else {
                    this.checkData = false;
                    let typeID: any;
                    if (this.stockType === 0) {
                        typeID = this.CHUYEN_KHO_KIEM_VAN_CHUYEN;
                    } else if (this.stockType === 1) {
                        typeID = this.CHUYEN_KHO_GUI_DAI_LY;
                    } else {
                        typeID = this.CHUYEN_KHO_NOI_BO;
                    }
                    this.rsTransferDetails = [];
                    this.rsTransfer.typeID = typeID;
                    this.setDefaultDataFromSystemOptions();
                }
                this.rsTransferDataSetup();
                if (this.dataSession && this.dataSession.isEdit) {
                    this.edit();
                }
            });
        });
        this.subcribeEvent();
        // Nhận event khi thêm nhanh
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilsService.setShowPopup(response.content);
        });
        this.registerIsShowPopup();
    }

    registerIsShowPopup() {
        this.utilsService.checkEvent.subscribe(res => {
            this.isShowPopup = res;
        });
    }

    getCurrencyType() {
        // this.ppOrder.currencyId : đồng tiền hiện tại
        // this.account.organizationUnit.currencyID : đồng tiền hoạch toán
        // priceType : đơn giá quy đổi
        // priceOriginalType : đơn giá
        if (this.rsTransfer.currencyID === this.account.organizationUnit.currencyID) {
            this.priceOriginalType = this.DDSo_DonGia;
        } else {
            this.priceOriginalType = this.DDSo_DonGiaNT;
            this.priceType = this.DDSo_DonGia;
        }
    }

    getAccount() {
        const columnList = [{ column: AccountType.TK_CO, ppType: false }, { column: AccountType.TK_NO, ppType: false }];
        let typeID: any;
        if (this.stockType === 0) {
            typeID = this.CHUYEN_KHO_KIEM_VAN_CHUYEN;
        } else if (this.stockType === 1) {
            typeID = this.CHUYEN_KHO_GUI_DAI_LY;
        } else {
            typeID = this.CHUYEN_KHO_NOI_BO;
        }
        const param = {
            typeID: typeID,
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
        this.rsTransferCopy = Object.assign({}, this.rsTransfer);
        this.rsTransferDetailsCopy = [...this.rsTransferDetails];
        this.viewVouchersSelectedCopy = [...this.viewVouchersSelected];
    }

    previousState(content) {
        if (this.rsTransferCopy && !this.utilsService.isShowPopup) {
            this.isClosing = true;
            if (
                !this.rsTransferCopy ||
                this.isMove ||
                (this.utilsService.isEquivalent(this.rsTransfer, this.rsTransferCopy) &&
                    this.utilsService.isEquivalentArray(this.rsTransferDetails, this.rsTransferDetailsCopy) &&
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
        if (this.isClosing || !this.rsTransferCopy || this.isClosed) {
            return true;
        }
        return (
            this.utilsService.isEquivalent(this.rsTransfer, this.rsTransferCopy) &&
            this.utilsService.isEquivalentArray(this.rsTransferDetails, this.rsTransferDetailsCopy) &&
            this.utilsService.isEquivalentArray(this.viewVouchersSelected, this.viewVouchersSelectedCopy)
        );
    }

    closeAll() {
        this.isClosed = true;
        this.dataSession = JSON.parse(sessionStorage.getItem('chuyenKhoSearchData'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
        sessionStorage.removeItem('chuyenKhoDataSession');
        this.router.navigate(
            ['/chuyen-kho'],
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

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('chuyenKhoDataSession'));
        if (!this.dataSession) {
            this.dataSession = JSON.parse(sessionStorage.getItem('chuyenKhoSearchData'));
        }
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.searchData = this.dataSession.searchVoucher;
            this.predicate = this.dataSession.predicate;
            // this.accountingObjectName = this.dataSession.accountingObjectName;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
    }

    rsTransferDataSetup() {
        this.sysRecord = this.account.systemOption.find(x => x.code === TCKHAC_GhiSo && x.data);
        this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
        this.checkNDGVBT = this.account.systemOption.some(x => x.code === VTHH_NHAP_DON_GIA_VON && x.data === '0');
        this.checkUseMoreNoMBook = this.account.systemOption.some(x => x.code === TCKHAC_SDSoQuanTri && x.data === '0');
        this.accountingObjects = [];
        if (this.rsTransfer.id) {
            this.isEdit = false;
            this.noBookVoucher = this.isSoTaiChinh ? this.rsTransfer.noFBook : this.rsTransfer.noMBook;
        } else {
            this.rsTransfer.typeLedger = 2;
        }
        if (!this.isEditUrl) {
            this.genNewVoucerCode();
        }
        if (this.rsTransfer.rsTransferDetails) {
            this.rsTransferDetails = this.rsTransfer.rsTransferDetails;
            this.updateSum();
        }
        this.materialGoodsService.queryForComboboxGood({ materialsGoodsType: [0, 1, 3] }).subscribe(
            (res: HttpResponse<any>) => {
                this.materialGoodsInStock = res.body;
            },
            (res: HttpErrorResponse) => console.log(res.message)
        );
        this.accountingObjectService.getAccountingObjectsRSTransfer().subscribe((res: HttpResponse<AccountingObjectDTO[]>) => {
            this.accountingObjectAlls = res.body;
            if (this.rsTransfer && this.rsTransfer.accountingObjectID) {
                this.rsTransfer.accountingObject = this.accountingObjects.find(item => item.id === this.rsTransfer.accountingObjectID);
            }
            if (this.checkData) {
                this.accountingObjects = this.accountingObjectAlls;
                this.rsTransfer.accountingObject = this.accountingObjects.find(n => n.id === this.rsTransfer.accountingObjectID);
            } else {
                this.accountingObjects = this.accountingObjectAlls
                    .filter(x => x.isActive)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            }
        });
        if (this.isEdit) {
            this.accountingObjectService.getAccountingObjectsForEmployee().subscribe((res: HttpResponse<AccountingObjectDTO[]>) => {
                this.employees = res.body;
                if (this.rsTransfer && this.rsTransfer.employeeID) {
                    this.rsTransfer.accountingObjectEmployee = this.employees.find(item => item.id === this.rsTransfer.employeeID);
                }
            });
            this.repositoryService.getRepositoryCombobox().subscribe(res => {
                this.repositories = res.body;
            });
        } else {
            this.accountingObjectService.getAllEmployee().subscribe((res: HttpResponse<AccountingObjectDTO[]>) => {
                this.employees = res.body;
                if (this.rsTransfer && this.rsTransfer.employeeID) {
                    this.rsTransfer.accountingObjectEmployee = this.employees.find(item => item.id === this.rsTransfer.employeeID);
                }
            });
            this.repositoryService.getRepositoryComboboxGetAll().subscribe(res => {
                this.repositories = res.body;
            });
        }
        this.transportMethodService.getTransportMethodCombobox().subscribe((res: HttpResponse<TransportMethod[]>) => {
            this.transportMethods = res.body;
        });
        this.unitService.convertRateForMaterialGoodsComboboxCustom().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
            this.materialGoodsConvertUnitService
                .getAllMaterialGoodsConvertUnits()
                .subscribe((resC: HttpResponse<IMaterialGoodsConvertUnit[]>) => {
                    this.materialGoodsConvertUnits = resC.body;
                    this.LoadDefaultEdit(this.rsTransferDetails);
                });
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
        this.iaPublishInvoiceDetailsService.getFollowTransferByCompany().subscribe(res => {
            this.templates = res.body;
            console.log(this.templates);
            this.templates.forEach(item => {
                if (item.invoiceForm === 0) {
                    item.invoiceFormName = 'Hóa đơn điện tử';
                } else if (item.invoiceForm === 1) {
                    item.invoiceFormName = 'Hóa đơn đặt in';
                } else if (item.invoiceForm === 2) {
                    item.invoiceFormName = 'Hóa đơn tự in';
                }
                if (this.stockType === 0) {
                    this.templates = this.templates.filter(x => x.invoiceTemplate.includes('03XKNB'));
                } else if (this.stockType === 1) {
                    this.templates = this.templates.filter(x => x.invoiceTemplate.includes('04HGDL'));
                }
            });
            if (this.rsTransfer && this.rsTransfer.invoiceTypeID) {
                this.template = this.templates.find(item => item.invoiceTypeID === this.rsTransfer.invoiceTypeID);
                this.template.invoiceForm = this.rsTransfer.invoiceForm;
            }
        });
    }

    LoadDefaultEdit(detail: RSTransferDetails[]) {
        for (let i = 0; i < detail.length; i++) {
            // detail[i].units = this.getUintFromMaterialgoodsByID(detail[i].materialGoods.id);
            if (detail[i].unit && detail[i].unit.id) {
                detail[i].unit = this.units.find(n => n.id === detail[i].unit.id);
                if (detail[i].unit.id !== detail[i].mainUnit.id) {
                    detail[i].unitPrices = this.getUnitPriceOriginalsWithUnitConvert(detail[i].unit.id, detail[i].materialGood);
                } else {
                    detail[i].unitPrices = this.getUnitPriceOriginals(detail[i].materialGood);
                }
            }
            // detail[i].saleDiscountPolicysDTO = this.saleDiscountPolicysDTO.filter(n => n.materialGoodsID === detail[i].materialGoods.id);
        }
    }

    getUnitPriceOriginals(mg: IMaterialGoodsInStock) {
        const lst = [];
        if (mg) {
            if (mg.fixedSalePrice) {
                lst.push({ unitPriceOriginal: mg.fixedSalePrice });
            }
            if (mg.salePrice1) {
                lst.push({ unitPriceOriginal: mg.salePrice1 });
            }
            if (mg.salePrice2) {
                lst.push({ unitPriceOriginal: mg.salePrice2 });
            }
            if (mg.salePrice3) {
                lst.push({ unitPriceOriginal: mg.salePrice3 });
            }
        }
        return lst;
    }

    getUnitPriceOriginalsWithUnitConvert(unitID, materalGoods: IMaterialGoodsInStock) {
        const lst = [];
        let mg;
        if (unitID === materalGoods.unitID) {
            mg = materalGoods;
        } else {
            mg = this.materialGoodsConvertUnits.find(n => n.materialGoodsID === materalGoods.id && n.unitID === unitID);
        }
        if (mg) {
            if (mg.fixedSalePrice) {
                lst.push({ unitPriceOriginal: mg.fixedSalePrice });
            }
            if (mg.salePrice1) {
                lst.push({ unitPriceOriginal: mg.salePrice1 });
            }
            if (mg.salePrice2) {
                lst.push({ unitPriceOriginal: mg.salePrice2 });
            }
            if (mg.salePrice3) {
                lst.push({ unitPriceOriginal: mg.salePrice3 });
            }
        }
        return lst;
    }

    onChangeStockType() {
        this.iaPublishInvoiceDetailsService.getFollowTransferByCompany().subscribe(res => {
            this.templates = res.body;
            if (this.templates.length === 0) {
                if (this.stockType === 0) {
                    this.templates = this.templates.filter(x => x.invoiceTemplate.includes('03XKNB'));
                    this.rsTransfer.invoiceSeries = null;
                    this.translateService.get(['ebwebApp.warehouseTransfer.home.title']).subscribe((response: any) => {
                        this.rsTransfer.reason = response['ebwebApp.warehouseTransfer.home.title'];
                    });
                    this.template = undefined;
                    this.rsTransfer.invoiceDate = null;
                    this.rsTransfer.invoiceNo = null;
                    this.checkAddNew = false;
                    this.checkStock = false;
                } else if (this.stockType === 1) {
                    this.templates = this.templates.filter(x => x.invoiceTemplate.includes('04HGDL'));
                    this.rsTransfer.invoiceSeries = null;
                    this.template = undefined;
                    this.translateService.get(['ebwebApp.warehouseTransfer.fillReason']).subscribe((response: any) => {
                        this.rsTransfer.reason = response['ebwebApp.warehouseTransfer.fillReason'];
                    });
                    this.rsTransfer.invoiceDate = null;
                    this.rsTransfer.invoiceNo = null;
                    this.checkAddNew = false;
                    this.checkStock = false;
                } else {
                    this.translateService.get(['ebwebApp.warehouseTransfer.home.title']).subscribe((response: any) => {
                        this.rsTransfer.reason = response['ebwebApp.warehouseTransfer.home.title'];
                    });
                    this.checkStock = true;
                }
            } else {
                this.templates.forEach(item => {
                    if (this.stockType === 0) {
                        this.templates = this.templates.filter(x => x.invoiceTemplate.includes('03XKNB'));
                        this.rsTransfer.invoiceSeries = null;
                        this.translateService.get(['ebwebApp.warehouseTransfer.home.title']).subscribe((response: any) => {
                            this.rsTransfer.reason = response['ebwebApp.warehouseTransfer.home.title'];
                        });
                        this.template = undefined;
                        this.rsTransfer.invoiceDate = null;
                        this.rsTransfer.invoiceNo = null;
                        this.checkAddNew = false;
                        this.checkStock = false;
                    } else if (this.stockType === 1) {
                        this.templates = this.templates.filter(x => x.invoiceTemplate.includes('04HGDL'));
                        this.rsTransfer.invoiceSeries = null;
                        this.template = undefined;
                        this.translateService.get(['ebwebApp.warehouseTransfer.fillReason']).subscribe((response: any) => {
                            this.rsTransfer.reason = response['ebwebApp.warehouseTransfer.fillReason'];
                        });
                        this.rsTransfer.invoiceDate = null;
                        this.rsTransfer.invoiceNo = null;
                        this.checkAddNew = false;
                        this.checkStock = false;
                    } else {
                        this.translateService.get(['ebwebApp.warehouseTransfer.home.title']).subscribe((response: any) => {
                            this.rsTransfer.reason = response['ebwebApp.warehouseTransfer.home.title'];
                        });
                        this.checkStock = true;
                    }
                    if (item.invoiceForm === 0) {
                        item.invoiceFormName = 'Hóa đơn điện tử';
                    } else if (item.invoiceForm === 1) {
                        item.invoiceFormName = 'Hóa đơn đặt in';
                    } else if (item.invoiceForm === 2) {
                        item.invoiceFormName = 'Hóa đơn tự in';
                    }
                });
            }
            if (this.rsTransfer && this.rsTransfer.invoiceTypeID && this.template) {
                // this.template = this.templates.find(item => item.invoiceTypeID === this.rsTransfer.invoiceTypeID);
                this.template.invoiceForm = this.rsTransfer.invoiceForm;
            }
        });
    }

    selectTemplate() {
        if (this.template) {
            this.rsTransfer.invoiceTemplate = this.template.invoiceTemplate;
            this.rsTransfer.invoiceSeries = this.template.invoiceSeries;
            this.rsTransfer.invoiceForm = this.template.invoiceForm;
            this.rsTransfer.invoiceTypeID = this.template.invoiceTypeID;
            this.rsTransfer.invoiceNo = null;
            // this.rsTransfer.invoiceDate = null;
            this.checkRequiredInvoiceNo(false);
        } else {
            this.rsTransfer.templateID = null;
            this.rsTransfer.invoiceTemplate = null;
            this.rsTransfer.invoiceSeries = null;
            this.rsTransfer.invoiceForm = null;
            this.rsTransfer.invoiceTypeID = null;
            this.checkRequiredInvoiceNo(true);
        }
    }

    checkRequiredInvoiceNo(check?: boolean) {
        if (check) {
            this.checkAddNew = false;
        } else {
            this.checkAddNew =
                (this.isRequiredInvoiceNo || (!this.isRequiredInvoiceNo && this.template && this.template.invoiceForm !== 0)) &&
                !this.rsTransfer.invoiceNo;
        }
    }

    checkSaveRequiredInvoiceNo() {
        return (
            (this.isRequiredInvoiceNo || (!this.isRequiredInvoiceNo && this.template && this.template.invoiceForm !== 0)) &&
            !this.rsTransfer.invoiceNo
        );
    }

    disableInvoiceNo() {
        return this.isEdit && !this.isRequiredInvoiceNo && this.template && this.template.invoiceForm === 0;
    }

    checkInvoiceNo() {
        if (this.rsTransfer.invoiceNo) {
            this.rsTransfer.invoiceNo = this.utilsService.pad(this.rsTransfer.invoiceNo, 7);
        }
    }

    genNewVoucerCode() {
        this.utilsService
            .getGenCodeVoucher({
                typeGroupID: this.TYPE_GROUP_RS_TRANSFER,
                displayOnBook: this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
            })
            .subscribe((res: HttpResponse<string>) => {
                this.noBookVoucher = res.body;
                if (!this.rsTransfer.id) {
                    this.copy();
                }
            });
    }

    getUnit() {
        if (this.rsTransferDetails && this.units) {
            this.rsTransferDetails.forEach(item => {
                item.convertRates = this.units.filter(data => data.materialGoodsID === item.materialGood.id);
            });
        }
    }

    initValues() {
        this.template = null;
        this.checkRequiredInvoiceNo(false);
        this.stockType = 0;
        this.viewVouchersSelected = [];
        const checkType = {
            typeID: null,
            typeLedger: 2
        };
        if (this.stockType === 0) {
            checkType.typeID = this.CHUYEN_KHO_KIEM_VAN_CHUYEN;
        } else if (this.stockType === 1) {
            checkType.typeID = this.CHUYEN_KHO_GUI_DAI_LY;
        } else {
            checkType.typeID = this.CHUYEN_KHO_NOI_BO;
        }
        this.rsTransfer = checkType;
        // this.rsTransfer = { typeID: this.CHUYEN_KHO, typeLedger: 2 };
        if (this.account && this.account.systemOption) {
            this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
        }
        this.translateService.get(['ebwebApp.warehouseTransfer.home.title']).subscribe((res: any) => {
            this.rsTransfer.reason = res['ebwebApp.warehouseTransfer.home.title'];
        });
        this.quantitySum = 0;
        this.mainQuantitySum = 0;
        this.rsTransfer.totalAmount = 0;
        this.rsTransfer.totalOWAmount = 0;
        this.rsTransfer.accountingObject = {};
        this.rsTransfer.accountingObjectEmployee = {};
        this.rsTransferDetails = [];
        // this.lotNoArray = [];
        this.isSaving = false;
        // this.isInputReason = false;
        this.setDefaultDataFromSystemOptions();
    }

    setDefaultDataFromSystemOptions() {
        if (this.account) {
            this.rsTransfer.date = this.utilsService.ngayHachToan(this.account);
            this.rsTransfer.postedDate = this.rsTransfer.date;
            if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                this.rsTransfer.currencyID = this.account.organizationUnit.currencyID;
                this.rsTransfer.exchangeRate = 1;
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
        if (this.rsTransfer.accountingObject) {
            this.rsTransfer.accountingObjectID = this.rsTransfer.accountingObject.id;
            this.rsTransfer.accountingObjectName = this.rsTransfer.accountingObject.accountingObjectName;
            this.rsTransfer.accountingObjectAddress = this.rsTransfer.accountingObject.accountingObjectAddress;
            this.rsTransfer.contactName = this.rsTransfer.accountingObject.contactName;
            this.rsTransfer.reason = this.translateService.instant('ebwebApp.warehouseTransfer.fillAutoReason', {
                name: this.rsTransfer.accountingObjectName
            });
        } else {
            this.rsTransfer.accountingObjectName = null;
            this.rsTransfer.accountingObjectAddress = null;
            this.rsTransfer.contactName = null;
            this.translateService.get(['ebwebApp.warehouseTransfer.fillReason']).subscribe((response: any) => {
                this.rsTransfer.reason = response['ebwebApp.warehouseTransfer.fillReason'];
            });
        }
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.rsTransferDetails.length; i++) {
            total += this.rsTransferDetails[i][prop];
        }
        return total ? total : 0;
    }

    selectUnitPriceOriginal(index: number) {
        if (this.rsTransferDetails[index].materialGood && this.rsTransferDetails[index].unit && this.rsTransferDetails[index].unit.id) {
            this.rsTransferDetails[index].unitPrices = this.getUnitPriceOriginalsWithUnitConvert(
                this.rsTransferDetails[index].unit.id,
                this.rsTransferDetails[index].materialGood
            );
        }
        this.rsTransferDetails[index].unitPrice = this.rsTransferDetails[index].unitPriceOriginal * this.rsTransfer.exchangeRate;
        this.changeQuantity(index);
        this.roundObject();
        this.updateSum();
    }

    changeLotNo(detail: IRSTransferDetails) {
        if (detail.lotNo) {
            const data = detail.lotNoArray.filter(x => x.lotNo === detail.lotNo);
            if (data && data.length > 0) {
                const selected = detail.lotNoArray.find(x => x.lotNo === detail.lotNo);
                detail.expiryDate = moment(selected.expiryDate);
            }
        }
    }

    onAmountChange(index) {
        this.rsTransferDetails[index].unitPriceOriginal = this.rsTransferDetails[index].amount / this.rsTransferDetails[index].quantity;
        if (this.rsTransferDetails[index].formula === '/') {
            this.rsTransferDetails[index].mainUnitPrice =
                this.rsTransferDetails[index].oWPrice * this.rsTransferDetails[index].mainConvertRate;
        } else {
            this.rsTransferDetails[index].mainUnitPrice =
                this.rsTransferDetails[index].oWPrice / this.rsTransferDetails[index].mainConvertRate;
        }
        this.roundObject();
        this.updateSum();
    }

    onRightClick($event, data, selectedData, isNew, isDelete, isCopy) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.isCopy = isCopy;
        this.contextMenu.selectedData = selectedData;
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    selectUnit(index: number) {
        if (this.rsTransferDetails[index].unit) {
            if (this.rsTransferDetails[index].unit.id === this.rsTransferDetails[index].mainUnit.id) {
                this.rsTransferDetails[index].mainConvertRate = 1;
                this.rsTransferDetails[index].formula = '*';
                this.rsTransferDetails[index].mainQuantity = this.rsTransferDetails[index].quantity;
                this.rsTransferDetails[index].mainUnitPrice = this.rsTransferDetails[index].oWPrice;
            } else {
                this.rsTransferDetails[index].mainConvertRate = this.rsTransferDetails[index].unit.convertRate;
                this.rsTransferDetails[index].formula = this.rsTransferDetails[index].unit.formula;
                this.rsTransferDetails[index].unitId = this.rsTransferDetails[index].unit.id;
                if (this.rsTransferDetails[index].formula === '*') {
                    this.rsTransferDetails[index].mainQuantity =
                        this.rsTransferDetails[index].unit.convertRate * this.rsTransferDetails[index].quantity;
                    this.rsTransferDetails[index].mainUnitPrice =
                        this.rsTransferDetails[index].oWPrice * this.rsTransferDetails[index].mainConvertRate;
                } else {
                    this.rsTransferDetails[index].mainQuantity =
                        this.rsTransferDetails[index].quantity / this.rsTransferDetails[index].unit.convertRate;
                    this.rsTransferDetails[index].mainUnitPrice =
                        this.rsTransferDetails[index].oWPrice / this.rsTransferDetails[index].mainConvertRate;
                }
            }
        }
        this.changeQuantity(index);
    }

    // event thay doi select option value
    selectEmployee() {
        if (this.rsTransfer.accountingObjectEmployee) {
            this.rsTransfer.employeeID = this.rsTransfer.accountingObjectEmployee.id;
            this.rsTransfer.employeeName = this.rsTransfer.accountingObjectEmployee.accountingObjectName;
        } else {
            this.rsTransfer.employeeID = null;
            this.rsTransfer.employeeName = null;
        }
    }

    // changeCurrency() {
    //     this.rsInwardOutward.exchangeRate = this.rsInwardOutward.currency ? this.rsInwardOutward.currency.exchangeRate : 1;
    //     this.rsInwardOutward.currencyID = this.rsInwardOutward.currency ? this.rsInwardOutward.currency.currencyCode : '';
    //     this.isCurrencyVND = this.rsInwardOutward.exchangeRate === 1;
    //     if (this.rsInwardOutwardDetails.length) {
    //         for (let i = 0; i < this.rsInwardOutwardDetails.length; i++) {
    //             this.getConvertRate(this.rsInwardOutwardDetails[i].materialGood.id, i);
    //         }
    //     }
    // }

    changeExchangeRate() {
        for (let index = 0; index < this.rsTransferDetails.length; index++) {
            this.selectUnitPriceOriginal(index);
        }
    }

    selectedMaterialGoods(index?: number) {
        if (this.rsTransferDetails[index].materialGood) {
            const currentMaterialGoodsId = this.rsTransferDetails[index].materialGood.id;
            this.rsTransferDetails[index].materialGoodsID = currentMaterialGoodsId;
            this.rsTransferDetails[index].description = this.rsTransferDetails[index].materialGood.materialGoodsName;
            this.rsTransferDetails[index].creditAccount = this.rsTransferDetails[index].materialGood.reponsitoryAccount;
            this.rsTransferDetails[index].oWPrice = 0;
            this.rsTransferDetails[index].oWAmount = 0;
            this.rsTransferDetails[index].mainConvertRate = 1;
            this.rsTransferDetails[index].formula = '*';
            if (this.rsTransferDetails[index].materialGood.repositoryID) {
                this.rsTransferDetails[index].fromRepositoryID = this.rsTransferDetails[index].materialGood.repositoryID;
            }
            this.getConvertRate(currentMaterialGoodsId, index);
            this.repositoryLedgerService.getLotNoArray(this.rsTransferDetails[index].materialGoodsID).subscribe(res => {
                this.rsTransferDetails[index].lotNoArray = res.body;
            });
        }
    }

    updateLotNoArray() {
        for (const item of this.rsTransferDetails) {
            this.repositoryLedgerService.getLotNoArray(item.materialGood.id).subscribe(res => {
                item.lotNoArray = res.body;
            });
        }
    }

    onChangeFromRepository(index?: number) {
        for (const detail of this.rsTransferDetails) {
            this.rsTransferDetails[index].creditAccount = this.repositories.find(x => x.id === detail.fromRepositoryID).defaultAccount;
        }
    }

    onChangeToRepository(index?: number) {
        for (const detail of this.rsTransferDetails) {
            this.rsTransferDetails[index].debitAccount = this.repositories.find(x => x.id === detail.toRepositoryID).defaultAccount;
        }
    }

    getConvertRate(currentMaterialGoodsId, index) {
        this.unitService.convertRateForMaterialGoodsComboboxCustom({ materialGoodsId: currentMaterialGoodsId }).subscribe(
            res => {
                this.rsTransferDetails[index].convertRates = res.body;
                this.rsTransferDetails[index].unit = this.rsTransferDetails[index].convertRates[0];
                if (this.rsTransferDetails[index].unit) {
                    this.rsTransferDetails[index].unitId = this.rsTransferDetails[index].unit.id;
                }
                this.rsTransferDetails[index].mainUnit = this.rsTransferDetails[index].convertRates[0];
                if (this.rsTransferDetails[index].mainUnit) {
                    this.rsTransferDetails[index].mainUnitId = this.rsTransferDetails[index].mainUnit.id;
                }
                this.updateSum();
                if (!this.rsTransfer.id) {
                    this.getUnitPriceOriginal(currentMaterialGoodsId, index);
                }
            },
            error => console.log(error)
        );
    }

    onChangeAutoPrinciple() {
        this.isInputReason = true;
        this.rsTransfer.reason = this.rsTransfer.autoPrinciple.autoPrincipleName;
        // this.rsInwardOutwardDetails.forEach(item => {
        //     item.debitAccount = this.rsInwardOutward.autoPrinciple.debitAccount;
        //     item.creditAccount = this.rsInwardOutward.autoPrinciple.creditAccount;
        // });

        for (let i = 0; i < this.rsTransferDetails.length; i++) {
            this.rsTransferDetails[i].debitAccount = this.rsTransfer.autoPrinciple.debitAccount;
            this.rsTransferDetails[i].creditAccount = this.rsTransfer.autoPrinciple.creditAccount;
        }
    }

    roundObject() {
        this.utilsService.roundObject(this.rsTransferDetails, this.account.systemOption);
        this.utilsService.roundObject(this.rsTransfer, this.account.systemOption);
    }

    getUnitPriceOriginal(currentMaterialGoodsId, index) {
        this.rsTransferDetails[index].unitPrices = [];
        this.rsTransferDetails[index].unitPrice = 0;
        this.rsTransferDetails[index].unitPriceOriginal = 0;

        this.materialGoodsConvertUnitService.getAllMaterialGoodsConvertUnits().subscribe(
            (res: HttpResponse<IMaterialGoodsConvertUnit[]>) => {
                this.materialGoodsConvertUnits = res.body;
                this.LoadDefaultEdit(this.rsTransferDetails);
                if (this.rsTransferDetails[index].unitPrices.length && this.rsTransferDetails[index].unitPrices[0]) {
                    this.rsTransferDetails[index].unitPriceOriginal = this.rsTransferDetails[index].unitPrices[0].unitPriceOriginal;
                    this.selectUnitPriceOriginal(index);
                }
                if (res.body.length <= 0) {
                    this.rsTransferDetails[index].mainConvertRate = 1;
                    this.rsTransferDetails[index].formula = '*';
                }
                this.selectUnit(index);
            },
            error => console.log(error)
        );
    }

    onUnitChange(index) {
        this.rsTransferDetails[index].unitPrices = [];
        this.rsTransferDetails[index].unitPrice = 0;
        this.rsTransferDetails[index].unitPriceOriginal = 0;
        this.materialGoodsConvertUnitService.getAllMaterialGoodsConvertUnits().subscribe(
            (res: HttpResponse<IMaterialGoodsConvertUnit[]>) => {
                this.materialGoodsConvertUnits = res.body;
                this.LoadDefaultEdit(this.rsTransferDetails);
                if (this.rsTransferDetails[index].unitPrices.length && this.rsTransferDetails[index].unitPrices[0]) {
                    this.rsTransferDetails[index].unitPriceOriginal = this.rsTransferDetails[index].unitPrices[0].unitPriceOriginal;
                }
                this.selectUnitPriceOriginal(index);
                this.selectUnit(index);
            },
            error => console.log(error)
        );
    }

    changeQuantity(index) {
        if (this.rsTransferDetails[index].formula === '*') {
            this.rsTransferDetails[index].mainQuantity =
                this.rsTransferDetails[index].quantity * this.rsTransferDetails[index].mainConvertRate;
        } else {
            this.rsTransferDetails[index].mainQuantity =
                this.rsTransferDetails[index].quantity / this.rsTransferDetails[index].mainConvertRate;
        }
        this.rsTransferDetails[index].amountOriginal =
            this.rsTransferDetails[index].quantity * this.rsTransferDetails[index].unitPriceOriginal;
        this.rsTransferDetails[index].amount = this.rsTransferDetails[index].amountOriginal;
        this.rsTransferDetails[index].oWAmount = this.rsTransferDetails[index].quantity * this.rsTransferDetails[index].oWPrice;
        this.roundObject();
        this.updateSum();
    }

    onChangeOWAmount(index) {
        this.rsTransferDetails[index].oWPrice = this.rsTransferDetails[index].oWAmount / this.rsTransferDetails[index].quantity;
        this.roundObject();
        this.updateSum();
    }

    onChangeOWPrice(index) {
        if (this.rsTransferDetails[index].unit && this.rsTransferDetails[index].unit.id === this.rsTransferDetails[index].mainUnit.id) {
            this.rsTransferDetails[index].mainUnitPrice = this.rsTransferDetails[index].oWPrice;
        } else {
            if (this.rsTransferDetails[index].formula === '*') {
                this.rsTransferDetails[index].mainUnitPrice =
                    this.rsTransferDetails[index].oWPrice / this.rsTransferDetails[index].mainConvertRate;
            } else {
                this.rsTransferDetails[index].mainUnitPrice =
                    this.rsTransferDetails[index].oWPrice * this.rsTransferDetails[index].mainConvertRate;
            }
        }
        this.changeQuantity(index);
        this.roundObject();
        this.updateSum();
    }

    updateSum() {
        this.updateRSTransfer();
    }

    convertRateChange(index) {
        if (this.rsTransferDetails[index].formula === '*') {
            this.rsTransferDetails[index].mainQuantity =
                this.rsTransferDetails[index].quantity * this.rsTransferDetails[index].mainConvertRate;
            this.rsTransferDetails[index].mainUnitPrice =
                this.rsTransferDetails[index].oWPrice / this.rsTransferDetails[index].mainConvertRate;
        } else {
            this.rsTransferDetails[index].mainQuantity =
                this.rsTransferDetails[index].quantity / this.rsTransferDetails[index].mainConvertRate;
            this.rsTransferDetails[index].mainUnitPrice =
                this.rsTransferDetails[index].oWPrice * this.rsTransferDetails[index].mainConvertRate;
        }
        this.roundObject();
        this.updateSum();
    }

    updateRSTransfer() {
        this.setDefaultValue();
        this.rsTransfer.totalAmount = this.sum('amount');
        this.rsTransfer.totalOWAmount = this.sum('oWAmount');
        this.quantitySum = this.sum('quantity');
        this.mainQuantitySum = this.sum('mainQuantity');
        this.setDefaultValue();
        // this.rsTransfer.totalAmountOriginal = 0;
    }

    mainQuantityChange(index) {
        if (this.rsTransferDetails[index].formula === '*') {
            this.rsTransferDetails[index].quantity =
                this.rsTransferDetails[index].mainQuantity / this.rsTransferDetails[index].mainConvertRate;
        } else {
            this.rsTransferDetails[index].quantity =
                this.rsTransferDetails[index].mainQuantity * this.rsTransferDetails[index].mainConvertRate;
        }
        this.rsTransferDetails[index].amount = this.rsTransferDetails[index].quantity * this.rsTransferDetails[index].unitPriceOriginal;
        this.roundObject();
        this.updateSum();
    }

    mainUnitPriceChange(index) {
        if (this.rsTransferDetails[index].formula === '*') {
            this.rsTransferDetails[index].oWPrice =
                this.rsTransferDetails[index].mainUnitPrice * this.rsTransferDetails[index].mainConvertRate;
        } else {
            this.rsTransferDetails[index].oWPrice =
                this.rsTransferDetails[index].mainUnitPrice / this.rsTransferDetails[index].mainConvertRate;
        }
        this.rsTransferDetails[index].amount = this.rsTransferDetails[index].quantity * this.rsTransferDetails[index].unitPriceOriginal;
        this.roundObject();
        this.updateSum();
    }

    setDefaultValue() {
        this.rsTransfer.totalAmount = this.rsTransfer.totalAmount || 0;
        this.rsTransfer.totalOWAmount = this.rsTransfer.totalOWAmount || 0;
        this.quantitySum = this.quantitySum || 0;
        this.mainQuantitySum = this.mainQuantitySum || 0;
    }

    addNewRow(select: number) {
        if (select === 0) {
            const checkAcount = {
                creditAccount: null,
                debitAccount: null
            };
            if (this.creditAccountDefault) {
                checkAcount.creditAccount = this.creditAccountDefault;
            } else if (this.rsTransfer && this.rsTransfer.autoPrinciple && this.rsTransfer.autoPrinciple.creditAccount) {
                checkAcount.creditAccount = this.rsTransfer.autoPrinciple.creditAccount;
            }
            if (this.debitAccountDefault) {
                checkAcount.debitAccount = this.debitAccountDefault;
            } else if (this.rsTransfer && this.rsTransfer.autoPrinciple && this.rsTransfer.autoPrinciple.debitAccount) {
                checkAcount.debitAccount = this.rsTransfer.autoPrinciple.debitAccount;
            }
            this.rsTransferDetails.push(checkAcount);
        }
    }

    onChangeDate() {
        this.rsTransfer.postedDate = this.rsTransfer.date;
    }

    removeRow(value: number, select: number) {
        if (select === 0) {
            this.rsTransferDetails.splice(value, 1);
        } else if (select === 1) {
            this.rsTransferDetails.splice(value, 1);
        }
        this.updateSum();
    }

    edit() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.rsTransfer.postedDate)) {
            if (!(this.isEdit || this.rsTransfer.recorded)) {
                this.accountingObjects = this.accountingObjectAlls
                    .filter(x => x.isActive)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                this.isEdit = true;
                this.isMove = false;
                if (this.stockType === 2) {
                    this.checkStock = true;
                }
                // this.itemUnSelected = [];
                for (const detail of this.rsTransferDetails) {
                    this.ppInvoiceDetailService.getLotNoArray(detail.materialGood.id).subscribe(res => {
                        detail.lotNoArray = res.body;
                    });
                }
                this.repositoryService.getRepositoryCombobox().subscribe(res => {
                    this.repositories = res.body;
                });
                this.copy();
                this.updateLotNoArray();
            }
        }
    }

    addNew($event) {
        $event.preventDefault();
        this.template = {};
        this.isEdit = true;
        this.checkAddNew = true;
        this.translateService.get(['ebwebApp.warehouseTransfer.home.title']).subscribe((res: any) => {
            this.rsTransfer.reason = res['ebwebApp.warehouseTransfer.home.title'];
        });
        this.checkRequiredInvoiceNo(true);
        this.initValues();
        this.genNewVoucerCode();
        this.focusFirstInput();
    }

    copyAndNew() {
        this.rsTransfer.id = null;
        this.rsTransfer.invoiceNo = null;
        this.rsTransferDetails.forEach(item => (item.id = null));
        this.isEdit = true;
        this.viewVouchersSelected = [];
        if (this.stockType === 2) {
            this.checkStock = true;
        }
        this.isRequiredInvoiceNo = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '0');
        this.onChangeStockType();
        this.genNewVoucerCode();
        this.disableInvoiceNo();
        this.checkRequiredInvoiceNo(false);
        if (!this.templates || this.templates.length === 0) {
            this.checkAddNew = false;
        }
        this.updateLotNoArray();
    }

    save(isNew = false) {
        event.preventDefault();
        this.checkSaveAndNew = isNew;
        if ((this.isCreateUrl && !this.utilsService.isShowPopup) || (this.isEditUrl && !this.utilsService.isShowPopup)) {
            this.rsTransfer.rsTransferDetails = this.rsTransferDetails;
            if (this.rsTransfer.typeLedger === 0 || this.isSoTaiChinh) {
                this.rsTransfer.noFBook = this.noBookVoucher;
                this.rsTransfer.noMBook = null;
            } else if (this.rsTransfer.typeLedger === 1 || !this.isSoTaiChinh) {
                this.rsTransfer.noMBook = this.noBookVoucher;
                this.rsTransfer.noFBook = null;
            }
            if (this.checkPostedDateGreaterDate()) {
                return;
            }
            this.isRequiredInvoiceNo = this.checkAddNew;
            if (!this.checkError()) {
                this.isClosing = false;
                if (this.modalRef) {
                    this.modalRef.close();
                }
                return;
            }
            // check khi lưu có đồng thời ghi sổ hay ko
            if (this.sysRecord.data === '0') {
                this.recorded = false;
                this.utilsService.checkQuantityExistsTest1(this.rsTransferDetails, this.account, this.rsTransfer.postedDate).then(data => {
                    if (data) {
                        this.materialGoodsInStockTextCode = data;
                        if (!this.checkSLT && this.materialGoodsInStockTextCode && this.materialGoodsInStockTextCode.quantityExists) {
                            this.recorded = true;
                            this.checkOpenSave = true;
                            if (this.modalRef) {
                                this.modalRef.close();
                            }
                            this.modalRef = this.modalService.open(this.contentSaveModal, { backdrop: 'static' });
                            return false;
                        }
                        if (this.materialGoodsInStockTextCode && this.materialGoodsInStockTextCode.minimumStock) {
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
            } else {
                this.recorded = true;
                this.rsTransfer.recorded = false;
                this.saveAfter(isNew);
            }
        }
    }

    saveAfter(isNew = false) {
        let typeID: any;
        if (this.stockType === 0) {
            typeID = this.CHUYEN_KHO_KIEM_VAN_CHUYEN;
        } else if (this.stockType === 1) {
            typeID = this.CHUYEN_KHO_GUI_DAI_LY;
        } else {
            typeID = this.CHUYEN_KHO_NOI_BO;
        }
        if (this.stockType === 2) {
            this.rsTransfer.invoiceForm = null;
            this.rsTransfer.invoiceTemplate = null;
            this.rsTransfer.invoiceNo = null;
            this.rsTransfer.invoiceSeries = null;
            this.rsTransfer.invoiceDate = null;
            this.rsTransfer.mobilizationOrderNo = null;
            this.rsTransfer.mobilizationOrderOf = null;
            this.rsTransfer.mobilizationOrderDate = null;
            this.rsTransfer.mobilizationOrderFor = null;
            this.rsTransfer.invoiceTypeID = null;
            this.rsTransfer.totalAmount = 0;
            this.rsTransfer.totalAmountOriginal = 0;
            for (const detail of this.rsTransferDetails) {
                detail.unitPriceOriginal = 0;
                detail.amount = 0;
                detail.amountOriginal = 0;
                detail.unitPrice = 0;
            }
        } else if (this.stockType === 1) {
            this.rsTransfer.mobilizationOrderFor = null;
        }
        this.rsTransfer.typeID = typeID;
        this.isSaving = true;
        this.rsTransfer.totalAmount = this.rsTransfer.totalOWAmount;
        this.rsTransfer.totalAmountOriginal = this.rsTransfer.totalAmount;
        const data = {
            rsTransfer: this.rsTransfer,
            viewVouchers: this.viewVouchersSelected,
            currentBook: this.isSoTaiChinh ? 0 : 1,
            checkRecordSLT: this.recorded
        };
        if (this.rsTransfer.id) {
            this.rsTransferService.update(data).subscribe(
                res => {
                    this.handleSuccessResponse(res, isNew);
                },
                error => {
                    this.handleError(error);
                }
            );
            // this.checkPopupSLT = false;
        } else {
            this.rsTransferService.create(data).subscribe(
                res => {
                    this.handleSuccessResponse(res, isNew);
                },
                error => {
                    this.handleError(error);
                }
            );
            // this.checkPopupSLT = false;
        }
    }

    checkPostedDateGreaterDate(): boolean {
        if (this.rsTransfer.postedDate < this.rsTransfer.date) {
            this.toastrService.error(this.translateService.instant('ebwebApp.common.error.dateAndPostDate'));
            return true;
        }
        return false;
    }

    checkError(): boolean {
        if (this.isSoTaiChinh) {
            if (!this.rsTransfer.noFBook) {
                this.toastrService.error(this.translateService.instant('global.data.null'));
                return;
            } else {
                if (!this.utilsService.checkNoBook(this.rsTransfer.noFBook)) {
                    return;
                }
            }
        } else {
            if (!this.rsTransfer.noMBook) {
                this.toastrService.error(this.translateService.instant('global.data.null'));
                return;
            } else {
                if (!this.utilsService.checkNoBook(this.rsTransfer.noMBook)) {
                    return;
                }
            }
        }

        if ((this.rsTransfer.invoiceNo || this.rsTransfer.invoiceDate) && !this.template && !this.checkStock) {
            this.toastrService.error(
                this.translateService.instant('ebwebApp.saBill.error.invoiceTemplate'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }

        if (!this.rsTransfer.invoiceNo && this.checkSaveRequiredInvoiceNo() && !this.checkStock) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceNo'));
            return;
        }

        if ((this.rsTransfer.invoiceNo || this.template) && !this.rsTransfer.invoiceDate && !this.checkStock) {
            this.toastrService.error(
                this.translateService.instant('ebwebApp.rSInwardOutward.error.errorInvoiceDate'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }

        if (
            this.template &&
            this.rsTransfer &&
            this.rsTransfer.invoiceDate &&
            this.rsTransfer.invoiceDate.isBefore(this.template.startUsing)
        ) {
            this.toastrService.error(this.translateService.instant('ebwebApp.rSInwardOutward.error.invalidInvoiceDate'));
            return;
        }

        if (this.checkCloseBook(this.account, this.rsTransfer.postedDate)) {
            this.toastrService.error(
                this.translateService.instant('ebwebApp.lockBook.err.checkForSave'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }

        if (!this.rsTransferDetails || !this.rsTransferDetails.length) {
            this.toastrService.error(
                this.translateService.instant('ebwebApp.pporder.error.nullDetail'),
                this.translateService.instant('ebwebApp.pporder.error.error')
            );
            return false;
        }

        if (!this.rsTransferDetails || !this.rsTransferDetails.length) {
            this.toastrService.error(this.translateService.instant('ebwebApp.rsInwardOutward.error.rsInwardOutwardDetails'));
            return;
        }

        let orderPriority = 0;
        for (const item of this.rsTransferDetails) {
            if (!item.materialGood) {
                this.toastrService.error(this.translateService.instant('ebwebApp.outWard.materialGoodsDoNotEmpty'));
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
            if (item.fromRepositoryID === item.toRepositoryID) {
                this.toastrService.error(this.translateService.instant('ebwebApp.warehouseTransfer.fromRepositoryDuplicateToRepository'));
                return false;
            }
            item.orderPriority = orderPriority;
            orderPriority++;
        }
        return true;
    }

    handleSuccessResponse(res, isNew) {
        this.isSaving = false;
        if (!this.rsTransfer.id) {
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
            this.rsTransfer.id = res.body.id;
            this.rsTransferDetails = res.body.rsTransferDetails;
            this.rsTransfer.recorded = res.body.recorded;
            const searchData = JSON.parse(this.searchData);
            this.rsTransferService
                .findRowNumOutWardByID({
                    fromDate: searchData.fromDate || '',
                    toDate: searchData.toDate || '',
                    noType: searchData.noType,
                    status: searchData.status,
                    accountingObject: searchData.accountingObject || '',
                    searchValue: searchData.searchValue || '',
                    id: this.rsTransfer.id
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
        // this.resetCreateFrom();
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
        return this.rsTransferService
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
                    const rsTransfer = res.body;
                    this.dataSession.rowNum = this.rowNum;
                    sessionStorage.setItem('chuyenKhoDataSession', JSON.stringify(this.dataSession));
                    if (
                        rsTransfer.typeID === this.CHUYEN_KHO_KIEM_VAN_CHUYEN ||
                        rsTransfer.typeID === this.CHUYEN_KHO_GUI_DAI_LY ||
                        rsTransfer.typeID === this.CHUYEN_KHO_NOI_BO
                    ) {
                        this.router.navigate(['/chuyen-kho', rsTransfer.id, 'edit', this.rowNum]);
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
        // this.eventManager.subscribe('selectedPPDisCountReturn', response => {
        //     if (this.isEdit) {
        //         this.createdFromPpDiscountReturn(response);
        //     }
        // });
        // this.eventManager.subscribe('closedPPDisCountReturn', async ref => {
        //     this.resetCreateFrom();
        //     this.isFromPPDiscountReturn = false;
        // });
        // this.eventManager.subscribe('selectViewSAOrder', response => {
        //     if (this.isEdit) {
        //         this.createdFromSaOrder(response);
        //     }
        // });
        // this.eventManager.subscribe('closeSelectViewSAOrder', async ref => {
        //     this.resetCreateFrom();
        //     this.isFromSaOrder = false;
        // });
        // this.eventManager.subscribe('selectedSAInvoiceOutWard', response => {
        //     if (this.isEdit) {
        //         this.createdFromSaInvoice(response);
        //     }
        // });
        // this.eventManager.subscribe('closedSelectedSAInvoiceOutWard', async ref => {
        //     this.resetCreateFrom();
        //     this.isFromSaInvoice = false;
        // });
        // this.eventManager.subscribe('selectedMaterialQuantum', response => {
        //     if (this.isEdit) {
        //         this.createdFromMaterialQuantum(response);
        //     }
        // });
        // this.eventManager.subscribe('closedSelectedMaterialQuantum', async ref => {
        //     this.resetCreateFrom();
        // });
        // this.eventManager.subscribe('afterDeleteRow', response => {
        //     if (response.content.ppOrderDetailId && response.content.quantityFromDB) {
        //         this.itemUnSelected.push(response.content);
        //     }
        //     this.updateSum();
        // });
    }

    record() {
        event.preventDefault();
        if (!this.isEdit) {
            if (!this.checkCloseBook(this.account, this.rsTransfer.postedDate)) {
                this.utilsService.checkQuantityExistsTest1(this.rsTransferDetails, this.account, this.rsTransfer.postedDate).then(data => {
                    if (data) {
                        this.materialGoodsInStockTextCode = data;
                        if (!this.checkSLT && this.materialGoodsInStockTextCode.quantityExists) {
                            this.toastrService.error(
                                this.translateService.instant('ebwebApp.pPDiscountReturnDetails.error.quantityExistsRecordErrorSave'),
                                this.translateService.instant('ebwebApp.mBDeposit.message')
                            );
                            return;
                        }
                        if (!this.rsTransfer.recorded) {
                            const record_: Irecord = {};
                            let typeID: any;
                            if (this.stockType === 0) {
                                typeID = this.CHUYEN_KHO_KIEM_VAN_CHUYEN;
                            } else if (this.stockType === 1) {
                                typeID = this.CHUYEN_KHO_GUI_DAI_LY;
                            } else {
                                typeID = this.CHUYEN_KHO_NOI_BO;
                            }
                            record_.id = this.rsTransfer.id;
                            record_.typeID = typeID;
                            record_.repositoryLedgerID = this.rsTransfer.id;
                            this.gLService.record(record_).subscribe((res: HttpResponse<Irecord>) => {
                                if (res.body.success) {
                                    this.rsTransfer.recorded = true;
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
        if (!this.checkCloseBook(this.account, this.rsTransfer.postedDate)) {
            const record_: Irecord = {};
            let typeID: any;
            if (this.stockType === 0) {
                typeID = this.CHUYEN_KHO_KIEM_VAN_CHUYEN;
            } else if (this.stockType === 1) {
                typeID = this.CHUYEN_KHO_GUI_DAI_LY;
            } else {
                typeID = this.CHUYEN_KHO_NOI_BO;
            }
            record_.id = this.rsTransfer.id;
            record_.typeID = typeID;
            record_.repositoryLedgerID = this.rsTransfer.id;
            this.gLService.unrecord(record_).subscribe((res: HttpResponse<Irecord>) => {
                if (res.body.success) {
                    this.rsTransfer.recorded = false;
                }
            });
        }
    }

    openModel(content) {
        this.checkModalRef = this.modalService.open(content, { size: 'lg', backdrop: 'static' });
    }

    delete() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.rsTransfer.postedDate)) {
            this.rsTransferService.delete(this.rsTransfer.id).subscribe(
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
        this.checkSaveAndNew = true;
        this.save(true);
    }

    closeForm() {
        this.previousState(this.content);
    }

    printTransfer(isDownload: boolean, typeReports: number) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.rsTransfer.id,
                typeID: TypeID.CHUYEN_KHO,
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
        if (i === this.rsTransferDetails.length - 1) {
            this.addNewRow(0);
        }
    }

    copyRow(index: number) {
        if (!this.getSelectionText()) {
            const addRow = this.utilsService.deepCopyObject(this.rsTransferDetails[index]);
            addRow.id = null;
            this.rsTransferDetails.push(addRow);
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
        // this.checkPopupSLT = true;
        this.modalRef.close();
        this.rsTransfer.recorded = false;
        // if (this.checkSaveAndNew) {
        //     this.saveAfter(true);
        // } else {
        this.saveAfter(this.checkSaveAndNew);
        // }
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.rsTransferDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.rsTransfer;
    }

    addDataToDetail() {
        this.rsTransferDetails = this.details ? this.details : this.rsTransferDetails;
        this.rsTransfer = this.parent ? this.parent : this.rsTransfer;
    }

    keyPress(detail: object, key: string) {
        switch (key) {
            case 'ctr + delete':
                this.removeRow(1, 0);
                break;
            case 'ctr + c':
                this.copyRow(0);
                break;
            case 'ctr + insert':
                this.addNewRow(0);
                break;
        }
    }

    printOutWard(isDownload: boolean, typeReports: number) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.rsTransfer.id,
                typeID: TypeID.CHUYEN_KHO,
                typeReport: typeReports
            },
            isDownload
        );
        switch (typeReports) {
            case RSTRANSFER_TYPE.TYPE_REPORT_ChungTuKeToan:
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.mBDeposit.financialPaper') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
                break;
            case RSTRANSFER_TYPE.TYPE_REPORT_XUAT_KHO:
            case RSTRANSFER_TYPE.TYPE_REPORT_XUAT_KHO_A5:
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.outWard.billOutWrad') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
                break;
            case RSTRANSFER_TYPE.TYPE_REPORT_PhieuNhapKho:
            case RSTRANSFER_TYPE.TYPE_REPORT_PhieuNhapKhoA5:
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.pPInvoice.phieuNhapKho') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
                break;
        }
    }
}
