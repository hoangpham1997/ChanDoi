import { AfterViewChecked, AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Principal } from 'app/core';
import { Currency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { AccountingObject, IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import {
    AccountType,
    CANH_BAO_NHAN_VIEN,
    GROUP_TYPEID,
    KIEM_PHIEU_NHAP_KHO,
    LAP_KEM_HOA_DON,
    MATERIAL_GOODS_TYPE,
    MSGERROR,
    NHAP_DON_GIA,
    SD_SO_QUAN_TRI,
    SO_LAM_VIEC,
    TCKHAC_SDTichHopHDDT,
    TypeID
} from 'app/app.constants';
import { IaPublishInvoiceDetailsService } from 'app/ban-hang/xuat-hoa-don/ia-publish-invoice-details.service';
import { IaPublishInvoiceDetails } from 'app/shared/model/ia-publish-invoice-details.model';
import { IMaterialGoods, MaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { IUnit, Unit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { SaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';
import { SaleDiscountPolicyService } from 'app/entities/sale-discount-policy';
import { ISaReturn, SaReturn } from 'app/shared/model/sa-return.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { SaReturnService } from 'app/ban-hang/hang-ban-tra-lai-giam-gia/sa-return.service';
import { RepositoryService } from 'app/danhmuc/repository';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { CostSetService } from 'app/entities/cost-set';
import { ExpenseItemService } from 'app/entities/expense-item';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { EMContractService } from 'app/entities/em-contract';
import { BudgetItemService } from 'app/entities/budget-item';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { AccountingObjectBankAccountService } from 'app/entities/accounting-object-bank-account';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ExportInvoiceModalService } from 'app/core/login/export-invoice-modal.service';
import { EbSaInvoiceModalComponent } from 'app/shared/modal/sa-invoice/sa-invoice.component';
import { SaReturnDetails } from 'app/shared/model/sa-return-details.model';
import * as moment from 'moment';
import { RepositoryLedgerService } from 'app/entities/repository-ledger';
import { ROLE } from 'app/role.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { ICareerGroup } from 'app/shared/model/career-group.model';
import { CareerGroupService } from 'app/entities/career-group';
import { ISAInvoiceDetails } from 'app/shared/model/sa-invoice-details.model';
import { DATE_FORMAT_SLASH, DATE_TIME_SECOND_FORMAT } from 'app/shared';
import { InvoiceType } from 'app/shared/model/invoice-type.model';
import { IAReportService } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/ia-report.service';
import { Moment } from 'moment';
import { RSInwardOutwardService } from 'app/entities/rs-inward-outward/rs-inward-outward.service';
import { EbMaterialGoodsSpecificationsModalComponent } from 'app/shared/modal/material-goods-specifications/material-goods-specifications.component';

@Component({
    selector: 'eb-hang-ban-tra-lai-update',
    templateUrl: './hang-ban-tra-lai-update.component.html',
    styleUrls: ['./hang-ban-tra-lai.css']
})
export class HangBanTraLaiUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewChecked {
    @ViewChild('content') popup;
    @ViewChild('content2') popup2;
    @ViewChild('unrecordContent') unrecordModal;
    @ViewChild('deleteItem') deleteItem;
    @ViewChild('contentSave') contentSave;
    private TYPE_HANG_BAN_TRA_LAI = 33;
    private TYPE_HANG_BAN_GIAM_GIA = 34;
    private TYPE_GROUP_RS_IO = 40;
    private _sAReturn: ISaReturn;
    isSaving: boolean;
    currentAccount: any;
    saReturn: SaReturn;
    saReturnDetails: SaReturnDetails[];
    saReturnCopy: SaReturn;
    saReturnDetailsCopy: SaReturnDetails[];
    book: any;
    isSoTaiChinh: boolean;
    accountingObjects: AccountingObject[];
    accountingObjectAlls: AccountingObject[];
    employee: AccountingObject[];
    employeeAll: AccountingObject[];
    accountingObject: AccountingObject;
    currencies: Currency[];
    rowNum: any;
    count: any;
    dataSession: any;
    itemsPerPage: number;
    totalItems: number;
    searchData: string;
    predicate: any;
    reverse: number;
    page: number;
    currency: Currency;
    listColumnHeader: string[];
    templates: IaPublishInvoiceDetails[];
    template: IaPublishInvoiceDetails;
    invoiceTypes: InvoiceType[];
    materialGoodss: MaterialGoods[];
    units: Unit[];
    unitPriceOriginal: any[];
    isEdit: boolean;
    isSaveAndAdd: boolean;
    eventSubscriber: Subscription;
    eventSubscriber2: Subscription;
    eventSubscriber3: Subscription;
    eventSubscriber4: Subscription;
    modalRef: NgbModalRef;
    viewVouchersSelected: any;
    isEditReasonFirst: boolean;
    isEditReasonNKFirst: boolean;
    contextMenu: ContextMenu;
    saleDiscountPolicys: SaleDiscountPolicy[];
    currencyCode: string;
    isEditInvoice: boolean;
    isLoading: boolean;
    typeDelete: number;
    isRequiredInvoiceNo: boolean;
    isShow: boolean;
    paymentMethod: any[];
    listVAT: any[];
    index: number;
    isLapKemHoaDon: boolean;
    isKiemPhieuNhapKho: boolean;
    creditAccountList: any[];
    debitAccountList: any[];
    vatAccountList: any[];
    deductionDebitAccountList: any[];
    discountAccountList: any[];
    noBookVoucher: string;
    noBookInventory: string;
    repositories: any[];
    costAccountList: any[];
    repositoryAccountList: any[];
    isDonGia: boolean;
    expenseItems: IExpenseItem[];
    organizationUnits: IOrganizationUnit[]; // Phòng ban
    costSets: ICostSet[];
    statisticCodes: IStatisticsCode[];
    eMContracts: IEMContract[];
    budgetItems: IBudgetItem[];
    careerGroups: ICareerGroup[];
    reason: string;
    accountingObjectBankAccount: any;
    accountingObjectBankAccounts: any[];
    HANG_GIAM_GIA = 340;
    HANG_BAN_TRA_LAI = 330;
    saBillList: any;
    isSelfChange: boolean;
    isMoreForm: boolean;
    discountAccountDefault: string;
    costAccountDefault: string;
    repositoryAccountDefault: string;
    creditAccountDefault: string;
    debitAccountDefault: string;
    vatAccountDefault: string;
    deductionDebitAccountDefault: string;
    indexFocusDetailRow: any;
    indexFocusDetailCol: any;
    idIndex: any;
    select: number;
    refDateTime: Moment;
    /*Nhập kho*/
    isFromRSInwardOutward: boolean;
    NHAP_KHO = TypeID.NHAP_KHO;
    NHAP_KHO_TU_DIEU_CHINH = TypeID.NHAP_KHO_TU_DIEU_CHINH;
    NHAP_KHO_TU_MUA_HANG = TypeID.NHAP_KHO_TU_MUA_HANG;
    NHAP_KHO_TU_BAN_HANG_TRA_LAI = TypeID.NHAP_KHO_TU_BAN_HANG_TRA_LAI;
    rsRowNum: any;
    rsDataSession: any;
    isViewRSInWardOutward: boolean;
    /*Nhập kho*/

    isViewFromRef: boolean;
    isEditKPNK: boolean;
    hiddenVAT: boolean;
    defaultCareerGroupID: string;

    ROLE_HangBan1 = ROLE.HangBanTraLai_Xem;
    ROLE_Them1 = ROLE.HangBanTraLai_Them;
    ROLE_Sua1 = ROLE.HangBanTraLai_Sua;
    ROLE_Xoa1 = ROLE.HangBanTraLai_Xoa;
    ROLE_GhiSo1 = ROLE.HangBanTraLai_GhiSo;
    ROLE_In1 = ROLE.HangBanTraLai_In;

    ROLE_HangBan2 = ROLE.HangBanGiamGia_Xem;
    ROLE_Them2 = ROLE.HangBanGiamGia_Them;
    ROLE_Sua2 = ROLE.HangBanGiamGia_Sua;
    ROLE_Xoa2 = ROLE.HangBanGiamGia_Xoa;
    ROLE_GhiSo2 = ROLE.HangBanGiamGia_GhiSo;
    ROLE_In2 = ROLE.HangBanGiamGia_In;
    warningEmployee: boolean;
    warningVatRate: number;
    checkBook: number;
    VAT_TU_DV = MATERIAL_GOODS_TYPE.SERVICE;
    VAT_TU_KHAC = MATERIAL_GOODS_TYPE.DIFF;

    constructor(
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private router: Router,
        private currencyService: CurrencyService,
        private accountingObjectService: AccountingObjectService,
        private activatedRoute: ActivatedRoute,
        private iaPublishInvoiceDetailsService: IaPublishInvoiceDetailsService,
        private materialGoodsService: MaterialGoodsService,
        private unitService: UnitService,
        private saReturnService: SaReturnService,
        private toastrService: ToastrService,
        private translateService: TranslateService,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        public utilService: UtilsService,
        private refModalService: RefModalService,
        private saleDiscountPolicyService: SaleDiscountPolicyService,
        public utilsService: UtilsService,
        private accountListService: AccountListService,
        private repositoryService: RepositoryService,
        private organizationUnitService: OrganizationUnitService,
        private costSetService: CostSetService,
        private expenseItemService: ExpenseItemService,
        private statisticsCodeService: StatisticsCodeService,
        private eMContractService: EMContractService,
        private budgetItemService: BudgetItemService,
        private gLService: GeneralLedgerService,
        private rsInwardOutwardService: RSInwardOutwardService,
        private accountingObjectBankAccountService: AccountingObjectBankAccountService,
        private exportInvoiceModalService: ExportInvoiceModalService,
        private repositoryLedgerService: RepositoryLedgerService,
        private careerGroupService: CareerGroupService,
        private iAReportService: IAReportService
    ) {
        super();
        this.saReturn = {
            typeID: this.activatedRoute.routeConfig.path.includes('giam-gia') ? this.HANG_GIAM_GIA : this.HANG_BAN_TRA_LAI,
            autoOWAmountCal: 0
        };
        this.saReturnDetails = [];
        this.currency = {};
        this.contextMenu = new ContextMenu();
        this.listColumnHeader = ['accountingObjectCode', 'accountingObjectName'];
        this.viewVouchersSelected = [];
        this.index = 1;
        this.isViewFromRef = window.location.href.includes('/from-ref');
    }

    ngOnInit() {
        this.isLoading = true;
        this.isSaving = false;
        this.isEdit = true;
        this.warningEmployee = false;
        this.warningVatRate = 0;
        this.isEditInvoice = true;
        this.isEditKPNK = false;
        /*Nhập kho*/
        this.isViewRSInWardOutward = window.location.href.includes('/edit-rs-inward');
        /*Nhập kho*/
        this.count = this.saReturnService.total;
        this.materialGoodsService.queryForComboboxGood().subscribe((res: HttpResponse<IMaterialGoods[]>) => {
            this.materialGoodss = res.body;
            this.getMaterialGoods();
        });
        this.paymentMethod = [];
        this.translateService
            .get(['global.paymentMethod.cash', 'global.paymentMethod.transfer', 'global.paymentMethod.both'])
            .subscribe(res => {
                this.paymentMethod.push({ data: res['global.paymentMethod.cash'] });
                this.paymentMethod.push({ data: res['global.paymentMethod.transfer'] });
                this.paymentMethod.push({ data: res['global.paymentMethod.both'] });
            });
        this.listVAT = [
            { name: '0%', data: 0 },
            { name: '5%', data: 1 },
            { name: '10%', data: 2 },
            { name: 'KCT', data: 3 },
            { name: 'KTT', data: 4 }
        ];
        const columnList = [
            { column: AccountType.TK_CO, ppType: false },
            { column: AccountType.TK_NO, ppType: false },
            { column: AccountType.TK_THUE_GTGT, ppType: false },
            { column: AccountType.TKDU_THUE_GTGT, ppType: false },
            { column: AccountType.TK_CHIET_KHAU, ppType: false },
            { column: AccountType.TK_DON_GIA, ppType: false },
            { column: AccountType.TK_KHO, ppType: false }
        ];
        const param = {
            typeID: this.saReturn.typeID,
            columnName: columnList
        };
        this.accountListService.getAccountTypeFour(param).subscribe(res => {
            this.creditAccountList = res.body.creditAccount;
            this.debitAccountList = res.body.debitAccount;
            this.vatAccountList = res.body.vatAccount;
            this.deductionDebitAccountList = res.body.deductionDebitAccount;
            this.discountAccountList = res.body.discountAccount;
            this.costAccountList = res.body.costAccount;
            this.repositoryAccountList = res.body.repositoryAccount;
            this.discountAccountDefault = res.body.discountAccountDefault;
            this.costAccountDefault = res.body.costAccountDefault;
            this.repositoryAccountDefault = res.body.repositoryAccountDefault;
            this.creditAccountDefault = res.body.creditAccountDefault;
            this.debitAccountDefault = res.body.debitAccountDefault;
            this.vatAccountDefault = res.body.vatAccountDefault;
            this.deductionDebitAccountDefault = res.body.deductionDebitAccountDefault;
        });

        this.getListUnits();
        this.organizationUnitService.getOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body
                .filter(n => n.unitType === 4)
                .sort((a, b) => a.organizationUnitCode.localeCompare(b.organizationUnitCode));
        });
        this.saleDiscountPolicyService.findAllSaleDiscountPolicyDTO().subscribe(res => {
            this.saleDiscountPolicys = res.body;
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
        this.careerGroupService.getCareerGroups().subscribe((res: HttpResponse<ICareerGroup[]>) => {
            this.careerGroups = res.body;
        });
        this.accountingObjectService.getAllAccountingObjectDTO().subscribe((data: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjectAlls = data.body
                .filter(n => n.objectType === 1 || n.objectType === 2)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            this.employeeAll = data.body
                .filter(n => n.isEmployee)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            if (this.isEdit) {
                this.accountingObjects = this.accountingObjectAlls
                    .filter(n => n.isActive)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                this.employee = this.employeeAll
                    .filter(n => n.isActive)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            } else {
                this.accountingObjects = this.accountingObjectAlls;
                this.employee = this.employeeAll;
            }

            this.activatedRoute.data.subscribe(({ saReturn }) => {
                /*Nhập kho*/
                if (this.isViewRSInWardOutward) {
                    this.getSessionDataRS();
                }
                /*Nhập kho*/
                this.getSessionData();
                if (saReturn) {
                    this.saReturn = saReturn.saReturn ? saReturn.saReturn : this.saReturn;
                    this.count = saReturn.totalRow;
                    this.viewVouchersSelected = saReturn.viewVouchers ? saReturn.viewVouchers : [];
                    this.saReturnDetails = saReturn.saReturnDetails ? saReturn.saReturnDetails : [];
                    this.utilService.revertUUID(this.saReturnDetails);
                    if (this.saReturnDetails) {
                        this.saReturnDetails.sort((a, b) => {
                            return a.orderPriority - b.orderPriority;
                        });
                    }
                    if (saReturn.refDateTime) {
                        this.refDateTime = moment(saReturn.refDateTime);
                    }
                }
                if (this.saReturn) {
                    if (this.saReturnDetails) {
                        this.getUnit();
                        this.getMaterialGoods();
                        this.convertItemSelectBack();
                    }
                    if (this.saReturn.id) {
                        this.isEdit = false;
                    } else {
                        this.saReturn.isDeliveryVoucher = false;
                        this.isEditReasonFirst = true;
                        this.isEditReasonNKFirst = true;
                    }
                }
                this.iAReportService.queryInvoiceType().subscribe(res => {
                    this.invoiceTypes = res.body;
                });
                this.iaPublishInvoiceDetailsService.getAllByCompany().subscribe(res => {
                    this.templates = res.body;
                    if (this.saReturn && this.saReturn.invoiceTypeID) {
                        this.template = this.templates.find(item => item.invoiceTypeID === this.saReturn.invoiceTypeID);
                    }
                    this.templates.forEach(item => {
                        if (item.invoiceForm === 0) {
                            item.invoiceFormName = 'Hóa đơn điện tử';
                        } else if (item.invoiceForm === 1) {
                            item.invoiceFormName = 'Hóa đơn đặt in';
                        } else if (item.invoiceForm === 2) {
                            item.invoiceFormName = 'Hóa đơn tự in';
                        }
                    });
                    if (!this.saReturn.paymentMethod && this.saReturn.isBill) {
                        this.saReturn.paymentMethod = this.paymentMethod[0].data;
                    }
                    if (this.templates.length === 1 && this.saReturn.isBill && !this.template) {
                        this.template = this.templates[0];
                        this.selectTemplate();
                    }
                });
                this.principal.identity().then(account => {
                    this.currentAccount = account;
                    this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                    this.checkBook = Number.parseInt(this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data);
                    this.isShow = this.currentAccount.systemOption.some(x => x.code === SD_SO_QUAN_TRI && x.data === '1');
                    this.isDonGia = this.currentAccount.systemOption.some(x => x.code === NHAP_DON_GIA && x.data === '1');
                    this.isLapKemHoaDon = this.currentAccount.systemOption.some(x => x.code === LAP_KEM_HOA_DON && x.data === '1');
                    this.hiddenVAT = this.currentAccount.organizationUnit.taxCalculationMethod === 1;
                    this.defaultCareerGroupID = this.currentAccount.organizationUnit.careerGroupID;
                    if (this.saReturn.typeID === this.HANG_GIAM_GIA && this.isLapKemHoaDon && !this.saReturn.id) {
                        this.saReturn.isBill = true;
                    }
                    this.isKiemPhieuNhapKho = this.currentAccount.systemOption.some(x => x.code === KIEM_PHIEU_NHAP_KHO && x.data === '1');
                    if (this.saReturn.typeID === this.HANG_BAN_TRA_LAI && this.isKiemPhieuNhapKho && !this.saReturn.id) {
                        this.saReturn.isDeliveryVoucher = true;
                        this.checkInventoryVoucher(true);
                    }
                    this.isRequiredInvoiceNo = this.currentAccount.systemOption.some(
                        x => x.code === TCKHAC_SDTichHopHDDT && x.data === '0'
                    );
                    if (this.saReturn.invoiceForm === 0 && this.saReturn.invoiceNo && !this.isRequiredInvoiceNo) {
                        this.isEditInvoice = false;
                    }
                    this.noBookVoucher = this.isSoTaiChinh ? this.saReturn.noFBook : this.saReturn.noMBook;
                    if (saReturn) {
                        this.noBookInventory = this.isSoTaiChinh ? saReturn.noFBook : saReturn.noMBook;
                        this.reason = saReturn.reason;
                    }

                    this.currencyCode = this.currentAccount.organizationUnit.currencyID;
                    if (!this.saReturn.id) {
                        if (!this.isShow) {
                            this.saReturn.typeLedger = this.checkBook;
                        } else {
                            this.saReturn.typeLedger = 2;
                        }
                        if (this.saReturn.typeID === TypeID.HANG_BAN_TRA_LAI) {
                            this.translateService.get(['ebwebApp.saReturn.reasonTL']).subscribe((res: any) => {
                                this.saReturn.reason = res['ebwebApp.saReturn.reasonTL'];
                            });
                        } else {
                            this.translateService.get(['ebwebApp.saReturn.reasonGG']).subscribe((res: any) => {
                                this.saReturn.reason = res['ebwebApp.saReturn.reasonGG'];
                            });
                        }
                        this.translateService.get(['ebwebApp.saReturn.reasonNK']).subscribe((res: any) => {
                            this.reason = res['ebwebApp.saReturn.reasonNK'];
                        });
                    }
                    this.currencyService.findAllActive().subscribe(res => {
                        this.currencies = res.body;
                        if (this.saReturn && this.saReturn.currencyID) {
                            this.currency = this.currencies.find(cur => cur.currencyCode === this.saReturn.currencyID);
                        } else if (this.currentAccount.organizationUnit && this.currentAccount.organizationUnit.currencyID) {
                            this.currency = this.currencies.find(
                                cur => cur.currencyCode === this.currentAccount.organizationUnit.currencyID
                            );
                        }
                        this.changeExchangeRate(this.saReturn.id);
                        this.isLoading = false;
                        this.copy();
                    });
                    if (this.isEdit && !this.saReturn.id) {
                        this.saReturn.date = this.utilsService.ngayHachToan(account);
                        this.saReturn.postedDate = this.saReturn.date;
                        this.getNoBookVoucher();
                    }
                });
            });
        });
        this.repositoryService.getRepositoryCombobox().subscribe(res => {
            this.repositories = res.body;
        });
        this.registerChangeInAccountDefaults();
        this.registerRef();
        this.registerBill();
        this.registerSaInvoice();
        this.registerAddNewRow();
        this.registerDeleteRow();
        this.registerCopyRow();
        this.registerSelectMaterialGoodsSpecification();
        this.registerMaterialGoodsSpecifications();
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
        this.registerIsShowPopup();

        /*check edit tu nhap kho*/
        if (this.rsDataSession && this.rsDataSession.isEdit) {
            this.edit();
        }
    }

    registerIsShowPopup() {
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilsService.setShowPopup(response.content);
        });
    }

    getListUnits(quickAdd?) {
        this.unitService.convertRateForMaterialGoodsComboboxCustom().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
            this.getUnit();
            if (quickAdd) {
                if (this.saReturnDetails[this.currentRow].units && this.saReturnDetails[this.currentRow].units.length) {
                    this.saReturnDetails[this.currentRow].unit = this.saReturnDetails[this.currentRow].units[0];
                    this.saReturnDetails[this.currentRow].unitID = this.saReturnDetails[this.currentRow].units[0].id;
                    this.saReturnDetails[this.currentRow].mainUnit = this.saReturnDetails[this.currentRow].units[0];
                    this.saReturnDetails[this.currentRow].mainUnitID = this.saReturnDetails[this.currentRow].units[0].id;
                    this.selectUnit(this.currentRow);
                }
            }
        });
    }

    getNoBookVoucher() {
        this.utilsService
            .getGenCodeVoucher({
                typeGroupID: this.saReturn.typeID === this.HANG_BAN_TRA_LAI ? this.TYPE_HANG_BAN_TRA_LAI : this.TYPE_HANG_BAN_GIAM_GIA, // typeGroupID loại chứng từ
                displayOnBook: this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
            })
            .subscribe((res: HttpResponse<string>) => {
                this.noBookVoucher = res.body;
                this.copy();
            });
    }

    previousState() {
        /**
         * Trường hợp sang từ nhập kho
         */
        if (this.isFromRSInwardOutward) {
            sessionStorage.removeItem('nhapKhoDataSession');
            this.router.navigate(
                ['/nhap-kho'],
                this.rsDataSession
                    ? {
                          queryParams: {
                              page: this.rsDataSession.page,
                              size: this.rsDataSession.itemsPerPage,
                              sort: this.rsDataSession.predicate + ',' + (this.rsDataSession.reverse ? 'asc' : 'desc')
                          }
                      }
                    : null
            );
        } else {
            window.history.back();
        }
    }

    closeForm() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        /**
         * Trường hợp sang từ nhập kho
         */
        if (this.isFromRSInwardOutward && !this.utilsService.isShowPopup) {
            sessionStorage.removeItem('nhapKhoDataSession');
            this.router.navigate(
                ['/nhap-kho'],
                this.rsDataSession
                    ? {
                          queryParams: {
                              page: this.rsDataSession.page,
                              size: this.rsDataSession.itemsPerPage,
                              sort: this.rsDataSession.predicate + ',' + (this.rsDataSession.reverse ? 'asc' : 'desc')
                          }
                      }
                    : null
            );
        } else if (!this.utilsService.isShowPopup) {
            if (this.saReturn.typeID === this.HANG_BAN_TRA_LAI) {
                this.router.navigate(['/hang-ban/tra-lai']);
            } else {
                this.router.navigate(['/hang-ban/giam-gia']);
            }
        }
    }

    getUnit() {
        if (this.saReturnDetails && this.units) {
            this.saReturnDetails.forEach(item => {
                item.units = this.units.filter(data => data.materialGoodsID === item.materialGoodsID);
            });
        }
    }

    getMaterialGoods() {
        if (this.saReturnDetails && this.materialGoodss) {
            this.saReturnDetails.forEach(item => {
                item.materialGoods = this.materialGoodss.find(data => data.id === item.materialGoodsID);
            });
        }
    }

    saveAll(isNew) {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.saReturn.typeLedger === 0 || this.isSoTaiChinh) {
            this.saReturn.noFBook = this.noBookVoucher;
            this.saReturn.noFBookInventory = this.noBookInventory;
            this.saReturn.noMBook = '';
            this.saReturn.noMBook = '';
        } else if (this.saReturn.typeLedger === 1 || !this.isSoTaiChinh) {
            this.saReturn.noMBook = this.noBookVoucher;
            this.saReturn.noMBookInventory = this.noBookInventory;
            this.saReturn.noFBook = '';
            this.saReturn.noFBook = '';
        } else if (this.saReturn.typeLedger === 2) {
            this.saReturn.noMBook = this.noBookVoucher;
            this.saReturn.noMBookInventory = this.noBookInventory;
            this.saReturn.noFBook = this.noBookVoucher;
            this.saReturn.noFBookInventory = this.noBookInventory;
        }

        // Check mã đối tượng
        if (this.checkRequiredAccountingObject(true)) {
            return;
        }
        // check mst
        if (this.saReturn.companyTaxCode && !this.utilService.checkMST(this.saReturn.companyTaxCode)) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.mst'));
            return;
        }

        if (this.isSoTaiChinh) {
            if (
                !this.saReturn.noFBook ||
                (!this.saReturn.noFBookInventory && this.saReturn.isDeliveryVoucher && this.saReturn.typeID === this.HANG_BAN_TRA_LAI)
            ) {
                this.toastrService.error(this.translateService.instant('global.data.null'));
                return;
            } else {
                if (!this.utilsService.checkNoBook(this.saReturn.noFBook, this.currentAccount)) {
                    return;
                }
            }
        } else {
            if (!this.saReturn.noMBook) {
                this.toastrService.error(this.translateService.instant('global.data.null'));
                return;
            } else {
                if (!this.utilsService.checkNoBook(this.saReturn.noMBook, this.currentAccount)) {
                    return;
                }
            }
        }
        if (this.saReturn.noFBookInventory && !this.utilsService.checkNoBook(this.saReturn.noFBookInventory, this.currentAccount)) {
            return;
        }
        if (this.saReturn.noMBookInventory && !this.utilsService.checkNoBook(this.saReturn.noMBookInventory, this.currentAccount)) {
            return;
        }

        if (!this.saReturn.date) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saReturn.error.date'));
            return;
        }
        if (!this.saReturn.postedDate) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saReturn.error.postedDate'));
            return;
        }
        if (this.checkCloseBook(this.currentAccount, this.saReturn.postedDate)) {
            this.toastrService.error(
                this.translateService.instant('ebwebApp.lockBook.err.checkForSave'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.saReturn.currencyID) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saReturn.error.currencyID'));
            return;
        }
        if (!this.saReturn.exchangeRate) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saReturn.error.exchangeRate'));
            return;
        }

        if (this.saReturn.postedDate < this.saReturn.date) {
            this.toastrService.error(this.translateService.instant('ebwebApp.mBDeposit.errorPostedDateAndDate'));
            return;
        }

        if (!this.saReturnDetails || !this.saReturnDetails.length) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saReturn.error.saReturnDetails'));
            return;
        }
        if (this.saReturn.isBill) {
            if (!this.saReturn.invoiceTemplate) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceTemplate'));
                return;
            }
            if (!this.saReturn.invoiceSeries) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceSeries'));
                return;
            }
            if (this.checkRequiredInvoiceNo()) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceNo'));
                return;
            }
            if (!this.saReturn.invoiceDate) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceDate'));
                return;
            }
            if (this.saReturn.invoiceDate.isBefore(this.template.startUsing)) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceDate2'));
                return;
            }
        }

        this.saReturnDetails = this.utilService.roundObjects(this.saReturnDetails, this.currentAccount.systemOption);
        let isAsk = false;
        let materialGoodsSpecificationsError = '';
        for (let i = 0; i < this.saReturnDetails.length; i++) {
            const item = this.saReturnDetails[i];
            this.saReturnDetails[i].orderPriority = i;
            if (!item.materialGoodsID) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.materialGoods'));
                return;
            } else if (
                this.saReturn.isDeliveryVoucher &&
                item.materialGoods &&
                item.materialGoods.materialGoodsType !== 2 &&
                item.materialGoods.materialGoodsType !== 4 &&
                !item.repositoryID
            ) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saReturn.error.repository'));
                return;
            } else if (!item.debitAccount) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saReturn.error.debitAccount'));
                return;
            } else if (!item.creditAccount) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saReturn.error.creditAccount'));
                return;
            }
            if (this.saReturn.isDeliveryVoucher) {
                if (
                    !item.costAccount &&
                    item.materialGoods &&
                    item.materialGoods.materialGoodsType !== this.VAT_TU_DV &&
                    item.materialGoods.materialGoodsType !== this.VAT_TU_KHAC
                ) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.saReturn.error.costAccount'));
                    return;
                }
                if (
                    !item.repositoryAccount &&
                    item.materialGoods &&
                    item.materialGoods.materialGoodsType !== this.VAT_TU_DV &&
                    item.materialGoods.materialGoodsType !== this.VAT_TU_KHAC
                ) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.saReturn.error.repositoryAccount'));
                    return;
                }
            }

            if (this.saReturnDetails[i].discountRate > 100) {
                this.toastrService.error(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'));
                return;
            }

            // check tk chiết khấu
            if (
                this.saReturnDetails[i].discountAmountOriginal &&
                this.saReturnDetails[i].discountAmountOriginal > 0 &&
                !this.saReturnDetails[i].discountAccount
            ) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saReturn.error.discountAccount'));
                return;
            }

            // check tk thue
            if (this.saReturnDetails[i].vatAmountOriginal && this.saReturnDetails[i].vatAmountOriginal > 0) {
                if (!this.saReturnDetails[i].vatAccount) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.saReturn.error.vatAccount'));
                    return;
                } else if (!this.saReturnDetails[i].deductionDebitAccount) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.saReturn.error.deductionDebitAccount'));
                    return;
                }
            }
            if (
                this.saReturnDetails[i].saBillDetailID &&
                this.saReturnDetails[i].totalQuantity &&
                this.saReturnDetails[i].quantity > this.saReturnDetails[i].totalQuantity
            ) {
                isAsk = true;
            }
            if (
                !this.saReturn.id &&
                this.saReturnDetails[i].materialGoods.isFollow &&
                (!this.saReturnDetails[i].materialGoodsSpecificationsLedgers ||
                    this.saReturnDetails[i].materialGoodsSpecificationsLedgers.length === 0)
            ) {
                materialGoodsSpecificationsError =
                    materialGoodsSpecificationsError + ', ' + this.saReturnDetails[i].materialGoods.materialGoodsCode;
            }
        }
        if (materialGoodsSpecificationsError) {
            materialGoodsSpecificationsError = materialGoodsSpecificationsError.substring(2, materialGoodsSpecificationsError.length);
            this.toastrService.error(
                'Hàng hóa ' +
                    materialGoodsSpecificationsError +
                    ' ' +
                    this.translateService.instant('ebwebApp.materialGoodsSpecifications.noMaterialGoodsSpecifications')
            );
            return;
        }
        if (this.saReturn.typeID === this.HANG_GIAM_GIA && !this.saReturn.isBill) {
            this.saReturn.invoiceNo = null;
            this.saReturn.invoiceTypeID = null;
            this.saReturn.invoiceDate = null;
            this.saReturn.invoiceSeries = null;
            this.saReturn.invoiceForm = null;
        }

        this.saReturn.saReturnDetails = this.saReturnDetails;
        if (this.isRequiredInvoiceNo) {
            this.saReturn.statusInvoice = 1;
        } else {
            this.saReturn.statusInvoice = 0;
        }
        if (isAsk) {
            this.modalRef = this.modalService.open(this.popup2, { backdrop: 'static' });
        } else {
            this.doSave(isNew);
        }
    }

    doSave(isNew) {
        const data = {
            saReturn: this.saReturn,
            viewVouchers: this.viewVouchersSelected,
            reason: this.reason,
            noFBook: null,
            noMBook: null,
            refDateTime: this.refDateTime ? this.refDateTime.format(DATE_TIME_SECOND_FORMAT) : null
        };

        if (this.saReturn.typeLedger === 0 || this.isSoTaiChinh) {
            data.noFBook = this.noBookInventory;
            data.noMBook = null;
        } else if (this.saReturn.typeLedger === 1 || !this.isSoTaiChinh) {
            data.noMBook = this.noBookInventory;
            data.noFBook = null;
        }
        this.saReturnService.create(data).subscribe(
            res => {
                if (this.saReturn.id) {
                    this.toastrService.success(this.translateService.instant('ebwebApp.saReturn.updated'));
                } else {
                    this.toastrService.success(this.translateService.instant('ebwebApp.saReturn.created'));
                }
                if (res.body.recordMsg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                    this.toastrService.error(
                        this.translateService.instant('global.messages.error.checkTonQuyQT'),
                        this.translateService.instant('ebwebApp.mCReceipt.error.error')
                    );
                } else if (res.body.recordMsg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                    this.toastrService.error(
                        this.translateService.instant('global.messages.error.checkTonQuyTC'),
                        this.translateService.instant('ebwebApp.mCReceipt.error.error')
                    );
                } else if (res.body.recordMsg === MSGERROR.XUAT_QUA_TON_QUY) {
                    this.toastrService.error(
                        this.translateService.instant('global.messages.error.checkTonQuy'),
                        this.translateService.instant('ebwebApp.mCReceipt.error.error')
                    );
                }
                if (this.saReturn.recorded) {
                    this.materialGoodsService.queryForComboboxGood().subscribe((mate: HttpResponse<IMaterialGoods[]>) => {
                        this.materialGoodss = mate.body;
                    });
                }
                if (isNew) {
                    this.isEdit = false;
                    this.addNew(false);
                } else {
                    this.isEdit = false;
                    this.saReturn.id = res.body.saReturn.id;
                    this.saReturn.rsInwardOutwardID = res.body.saReturn.rsInwardOutwardID;
                    this.saReturn.recorded = res.body.saReturn.recorded;
                    for (let i = 0; i < this.saReturnDetails.length; i++) {
                        this.saReturnDetails[i].expiryDate =
                            res.body.saReturn.saReturnDetails[i].expiryDate != null
                                ? moment(res.body.saReturn.saReturnDetails[i].expiryDate)
                                : null;
                    }
                }
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.saReturnService.total = this.saReturnService.total + 1;
            },
            error1 => {
                if (error1.error.errorKey !== 'noVoucherLimited') {
                    this.toastrService.error(this.translateService.instant('ebwebApp.saReturn.error.' + error1.error.errorKey));
                }
            }
        );
    }

    registerAddNewRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.addNewRow(true);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            this.keyPress(this.contextMenu.selectedData);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            this.copyRow(this.contextMenu.selectedData, true);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    addNewRow(isRightClick?) {
        if (this.isEdit) {
            let length = 0;
            if (isRightClick) {
                this.saReturnDetails.splice(this.indexFocusDetailRow + 1, 0, {});
                length = this.indexFocusDetailRow + 2;
            } else {
                this.saReturnDetails.push({
                    accountingObjectID: this.saReturn.accountingObject ? this.saReturn.accountingObject.id : null,
                    quantity: 0,
                    amountOriginal: 0,
                    unitPriceOriginal: 0,
                    unitPrice: 0,
                    amount: 0,
                    vatAmount: 0,
                    vatAmountOriginal: 0,
                    discountAmount: 0,
                    discountAmountOriginal: 0,
                    careerGroupID: this.defaultCareerGroupID,
                    creditAccount: this.creditAccountDefault,
                    debitAccount: this.debitAccountDefault,
                    discountAccount: this.discountAccountDefault,
                    costAccount: this.costAccountDefault,
                    repositoryAccount: this.repositoryAccountDefault,
                    vatAccount: this.vatAccountDefault,
                    deductionDebitAccount: this.deductionDebitAccountDefault
                });
                length = this.saReturnDetails.length;
            }
            if (isRightClick && this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const id = this.idIndex;
                const row = this.indexFocusDetailRow + 1;
                this.indexFocusDetailRow = row;
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(id + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            } else {
                const nameTag: string = event.srcElement.id;
                const idx: number = this.saReturnDetails.length - 1;
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

    keyPress(detail, type?) {
        if (this.isEdit) {
            if (type || this.select) {
                this.viewVouchersSelected.splice(this.viewVouchersSelected.indexOf(detail), 1);
            } else {
                this.saReturnDetails.splice(this.saReturnDetails.indexOf(detail), 1);
                if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                    // vì còn trường hợp = 0
                    if (this.saReturnDetails.length > 0) {
                        let row = 0;
                        if (this.indexFocusDetailRow > this.saReturnDetails.length - 1) {
                            row = this.indexFocusDetailRow - 1;
                        } else {
                            row = this.indexFocusDetailRow;
                        }
                        const id = this.idIndex;
                        setTimeout(function() {
                            const element: HTMLElement = document.getElementById(id + row);
                            if (element) {
                                element.focus();
                            }
                        }, 0);
                    }
                }
            }
        }
    }

    actionFocus(indexCol, indexRow, id) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
        this.idIndex = id;
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(1);
        }
    }

    onRightClick($event, data, selectedData, isNew, isDelete, isCopy, select?) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isCopy = isCopy;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.selectedData = selectedData;
        this.select = select;
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    move(index) {
        if (this.isFromRSInwardOutward) {
            if ((index === -1 && this.rsRowNum === 1) || (index === 1 && this.rsRowNum === this.totalItems)) {
                return;
            }
            this.rsRowNum += index;
            const searchData = JSON.parse(this.searchData);
            return this.rsInwardOutwardService
                .findReferenceTableID({
                    fromDate: searchData.fromDate || '',
                    toDate: searchData.toDate || '',
                    noType: searchData.noType,
                    status: searchData.status,
                    accountingObject: searchData.accountingObject || '',
                    searchValue: searchData.searchValue || '',
                    rowNum: this.rsRowNum
                })
                .subscribe((res: HttpResponse<any>) => {
                    console.log(res.body);
                    const rsInwardOutward = res.body;
                    this.rsDataSession.rowNum = this.rsRowNum;
                    sessionStorage.setItem('nhapKhoDataSession', JSON.stringify(this.rsDataSession));
                    if (rsInwardOutward.typeID === this.NHAP_KHO) {
                        this.router.navigate(['/nhap-kho', rsInwardOutward.id, 'edit', this.rsRowNum]);
                    } else if (rsInwardOutward.typeID === this.NHAP_KHO_TU_DIEU_CHINH && rsInwardOutward.refID) {
                    } else if (rsInwardOutward.typeID === this.NHAP_KHO_TU_MUA_HANG && rsInwardOutward.refID) {
                        this.router.navigate(['/mua-hang/qua-kho', rsInwardOutward.refID, 'edit-rs-inward']);
                    } else if (rsInwardOutward.typeID === this.NHAP_KHO_TU_BAN_HANG_TRA_LAI && rsInwardOutward.refID) {
                        this.router.navigate(['/hang-ban/tra-lai', rsInwardOutward.refID, 'edit-rs-inward']);
                    }
                });
        } else {
            // goi service get by row num
            this.isLoading = true;
            if ((index === -1 && this.rowNum === 1) || (index === 1 && this.rowNum === this.totalItems)) {
                return;
            }
            this.rowNum += index;
            const searchDataIndex = JSON.parse(this.searchData);
            this.saReturnService
                .find({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort(),
                    accountingObjectID: searchDataIndex.accountingObjectID ? searchDataIndex.accountingObjectID : '',
                    currencyID: searchDataIndex.currencyID ? searchDataIndex.currencyID : '',
                    fromDate: searchDataIndex.fromDate ? searchDataIndex.fromDate : '',
                    toDate: searchDataIndex.toDate ? searchDataIndex.toDate : '',
                    recorded: searchDataIndex.recorded === 1 || searchDataIndex.recorded === 0 ? searchDataIndex.status : '',
                    freeText: searchDataIndex.freeText ? searchDataIndex.freeText : '',
                    rowIndex: this.rowNum,
                    id: this.saReturn.id,
                    typeId: this.saReturn.typeID ? this.saReturn.typeID : ''
                })
                .subscribe(
                    res => {
                        if (res.body) {
                            this.saReturn = res.body.saReturn;
                            this.saReturnDetails = this.saReturn.saReturnDetails;
                            this.viewVouchersSelected = res.body.viewVouchers;
                            this.count = res.body.totalRow;
                            this.noBookVoucher = this.isSoTaiChinh ? this.saReturn.noFBook : this.saReturn.noMBook;
                        }
                        this.isLoading = false;
                    },
                    () => {
                        this.getSessionData();
                        this.isLoading = false;
                    }
                );
        }
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    getSessionData() {
        if (this.saReturn.typeID === this.HANG_BAN_TRA_LAI) {
            this.dataSession = JSON.parse(sessionStorage.getItem('dataSessionSaReturn1'));
        } else {
            this.dataSession = JSON.parse(sessionStorage.getItem('dataSessionSaReturn2'));
        }
        if (!this.dataSession) {
            if (this.saReturn.typeID === this.HANG_BAN_TRA_LAI) {
                this.dataSession = JSON.parse(sessionStorage.getItem('dataSearchSaReturn1'));
            } else {
                this.dataSession = JSON.parse(sessionStorage.getItem('dataSearchSaReturn2'));
            }
        }
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.searchData = this.dataSession.searchVoucher;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
    }

    getSessionDataRS() {
        this.rsDataSession = JSON.parse(sessionStorage.getItem('nhapKhoDataSession'));
        if (this.rsDataSession) {
            this.isFromRSInwardOutward = true;
            this.totalItems = this.rsDataSession.totalItems;
            this.rsRowNum = this.rsDataSession.rowNum;
            this.searchData = this.rsDataSession.searchVoucher;
        } else {
            this.rsDataSession = null;
        }
    }

    selectAccountingObject() {
        if (this.saReturn.accountingObject) {
            this.saReturnDetails.forEach(item => {
                if (!item.accountingObjectID || this.saReturn.accountingObjectID === item.accountingObjectID) {
                    item.accountingObjectID = this.saReturn.accountingObject.id;
                }
            });
            this.saReturn.accountingObjectID = this.saReturn.accountingObject.id;
            this.saReturn.accountingObjectName = this.saReturn.accountingObject.accountingObjectName;
            this.saReturn.accountingObjectAddress = this.saReturn.accountingObject.accountingObjectAddress;
            this.saReturn.companyTaxCode = this.saReturn.accountingObject.taxCode;
            this.saReturn.contactName = this.saReturn.accountingObject.contactName;
            if (this.isEditReasonFirst) {
                if (this.saReturn.typeID === TypeID.HANG_BAN_TRA_LAI) {
                    this.translateService.get(['ebwebApp.saReturn.reasonTL1']).subscribe((res: any) => {
                        this.saReturn.reason = res['ebwebApp.saReturn.reasonTL1'] + ' ' + this.saReturn.accountingObjectName;
                    });
                } else {
                    this.translateService.get(['ebwebApp.saReturn.reasonGG1']).subscribe((res: any) => {
                        this.saReturn.reason = res['ebwebApp.saReturn.reasonGG1'] + ' ' + this.saReturn.accountingObjectName;
                    });
                }
            }
            if (this.isEditReasonNKFirst) {
                this.translateService.get(['ebwebApp.saReturn.reasonNK1']).subscribe((res: any) => {
                    this.reason = res['ebwebApp.saReturn.reasonNK1'] + ' ' + this.saReturn.accountingObjectName;
                });
            }
            if (this.saReturn.typeID === this.HANG_GIAM_GIA) {
                this.accountingObjectBankAccountService
                    .getByAccountingObjectById({ accountingObjectID: this.saReturn.accountingObject.id })
                    .subscribe(res => {
                        this.accountingObjectBankAccounts = res.body;
                    });
            }
        } else {
            this.saReturn.accountingObjectName = null;
            this.saReturn.accountingObjectAddress = null;
            this.saReturn.companyTaxCode = null;
            this.saReturn.contactName = null;
            if (
                this.saReturn &&
                this.saReturnDetails.length ===
                    this.saReturnDetails.filter(x => x.accountingObjectID === this.saReturnDetails[0].accountingObjectID).length
            ) {
                this.saReturnDetails.forEach(saReturnDetail => {
                    saReturnDetail.accountingObjectID = null;
                });
            }
        }
    }

    selectTemplate() {
        if (this.template) {
            this.saReturn.invoiceTemplate = this.template.invoiceTemplate;
            this.saReturn.invoiceSeries = this.template.invoiceSeries;
            this.saReturn.invoiceForm = this.template.invoiceForm;
            this.saReturn.invoiceTypeID = this.template.invoiceTypeID;
            this.saReturn.invoiceNo = '';
            this.saReturn.invoiceDate = null;
        } else {
            this.saReturn.templateID = null;
            this.saReturn.invoiceTemplate = null;
            this.saReturn.invoiceSeries = null;
            this.saReturn.invoiceForm = null;
            this.saReturn.invoiceTypeID = null;
        }
    }

    upperCase() {
        if (this.saReturn.typeID === this.HANG_BAN_TRA_LAI) {
            if (this.saReturn.invoiceTemplate) {
                if (this.saReturn.invoiceTemplate === '01/ hoặc 02/') {
                    this.saReturn.invoiceTemplate = '01/';
                }
                this.saReturn.invoiceTemplate = this.saReturn.invoiceTemplate.toUpperCase();
            }
            if (this.saReturn.invoiceSeries) {
                this.saReturn.invoiceSeries = this.saReturn.invoiceSeries.toUpperCase();
            }
        }
    }

    checkRequiredInvoiceNo() {
        return (
            (this.isRequiredInvoiceNo || (!this.isRequiredInvoiceNo && this.template && this.template.invoiceForm !== 0)) &&
            !this.saReturn.invoiceNo &&
            this.saReturn.typeID === this.HANG_GIAM_GIA
        );
    }

    changeCreditAccount(detail) {
        if (!detail.deductionDebitAccount && !this.deductionDebitAccountDefault) {
            detail.deductionDebitAccount = detail.creditAccount;
        }
    }

    selectedMaterialGoods(index) {
        this.saReturnDetails[index].materialGoods = this.materialGoodss.find(
            item => item.id === this.saReturnDetails[index].materialGoodsID
        );
        if (this.saReturnDetails[index].materialGoods) {
            this.saReturnDetails[index].vatDescription = this.saReturnDetails[index].materialGoods.materialGoodsCode;
            if (!this.hiddenVAT) {
                this.saReturnDetails[index].vatRate = this.saReturnDetails[index].materialGoods.vatTaxRate;
                this.translateService.get('ebwebApp.saBill.vat').subscribe(res => {
                    this.saReturnDetails[index].vatDescription = res + ' ' + this.saReturnDetails[index].materialGoods.materialGoodsCode;
                });
            }
            this.saReturnDetails[index].discountRate = this.saReturnDetails[index].materialGoods.saleDiscountRate;
            this.saReturnDetails[index].saleDiscountPolicys = this.saleDiscountPolicys.filter(
                x => x.materialGoodsID === this.saReturnDetails[index].materialGoodsID
            );
            this.checkSaleDiscountPolicy(index);
            this.saReturnDetails[index].description = this.saReturnDetails[index].materialGoods.materialGoodsName;
            this.saReturnDetails[index].repository = this.repositories.find(
                item => item.id === this.saReturnDetails[index].materialGoods.repositoryID
            );
            if (this.saReturnDetails[index].repository) {
                this.saReturnDetails[index].repositoryID = this.saReturnDetails[index].repository.id;
            }
            if (this.saReturnDetails[index].materialGoods.careerGroupID) {
                this.saReturnDetails[index].careerGroupID = this.saReturnDetails[index].materialGoods.careerGroupID;
            }
            this.saReturnDetails[index].repositoryAccount = this.saReturnDetails[index].materialGoods.reponsitoryAccount;
            this.saReturnDetails[index].unit = {};
            this.saReturnDetails[index].unitID = null;
            this.saReturnDetails[index].mainUnit = {};
            this.saReturnDetails[index].mainUnitID = null;
            this.saReturnDetails[index].units = this.units.filter(
                item => item.materialGoodsID === this.saReturnDetails[index].materialGoods.id
            );
            if (this.saReturnDetails[index].units && this.saReturnDetails[index].units.length) {
                this.saReturnDetails[index].unit = this.saReturnDetails[index].units[0];
                this.saReturnDetails[index].unitID = this.saReturnDetails[index].units[0].id;
                this.saReturnDetails[index].mainUnit = this.saReturnDetails[index].units[0];
                this.saReturnDetails[index].mainUnitID = this.saReturnDetails[index].units[0].id;
                this.selectUnit(index);
            } else {
                this.saReturnDetails[index].mainConvertRate = 1;
                this.saReturnDetails[index].formula = '*';
                this.saReturnDetails[index].mainQuantity = this.saReturnDetails[index].quantity;
                this.saReturnDetails[index].mainUnitPrice = this.saReturnDetails[index].unitPriceOriginal;
            }
            if (!this.saReturnDetails[index].materialGoods.lotNos) {
                this.repositoryLedgerService
                    .getListLotNoByMaterialGoodsID({
                        materialGoodsID: this.saReturnDetails[index].materialGoodsID
                    })
                    .subscribe(ref => {
                        this.saReturnDetails[index].materialGoods.lotNos = ref.body;
                    });
            }
        }
        this.updateSaBill();
    }

    selectUnitPriceOriginal(index) {
        this.calculateUnitPrice(index);
        this.calculateMainUnitPrice(index);
        this.changeAmountOriginal(index);
    }

    selectUnit(index) {
        this.saReturnDetails[index].unit = this.units.find(item => item.id === this.saReturnDetails[index].unitID);
        if (this.saReturnDetails[index].unit && this.saReturnDetails[index].mainUnit) {
            if (this.saReturnDetails[index].unit.id === this.saReturnDetails[index].mainUnit.id) {
                this.saReturnDetails[index].mainConvertRate = 1;
                this.saReturnDetails[index].formula = '*';
                this.saReturnDetails[index].mainQuantity = this.saReturnDetails[index].quantity;
                this.saReturnDetails[index].mainUnitPrice = this.saReturnDetails[index].unitPriceOriginal;
            } else {
                this.saReturnDetails[index].mainConvertRate = this.saReturnDetails[index].unit.convertRate;
                this.saReturnDetails[index].formula = this.saReturnDetails[index].unit.formula;
                if (this.saReturnDetails[index].formula === '*' && this.saReturnDetails[index].quantity) {
                    this.saReturnDetails[index].mainQuantity =
                        this.saReturnDetails[index].unit.convertRate / this.saReturnDetails[index].quantity;
                    this.saReturnDetails[index].mainUnitPrice =
                        this.saReturnDetails[index].unitPriceOriginal / this.saReturnDetails[index].mainConvertRate;
                } else if (this.saReturnDetails[index].formula === '/') {
                    this.saReturnDetails[index].mainQuantity =
                        this.saReturnDetails[index].unit.convertRate * this.saReturnDetails[index].quantity;
                    this.saReturnDetails[index].mainUnitPrice =
                        this.saReturnDetails[index].unitPriceOriginal * this.saReturnDetails[index].mainConvertRate;
                }
            }
            this.changeQuantity(index);
        }
    }

    changeExchangeRate(isExchange) {
        if (this.currency) {
            this.saReturn.currencyID = this.currency.currencyCode;
            if (!isExchange) {
                this.saReturn.exchangeRate = this.currency.exchangeRate;
            }
            this.saReturnDetails.forEach(item => {
                if (this.currency.formula === '*') {
                    if (item.unitPriceOriginal || item.unitPriceOriginal === 0) {
                        item.unitPrice = item.unitPriceOriginal * this.saReturn.exchangeRate;
                    }
                    if (item.amountOriginal || item.amountOriginal === 0) {
                        item.amount = item.amountOriginal * this.saReturn.exchangeRate;
                    }
                    if (item.discountAmountOriginal || item.discountAmountOriginal === 0) {
                        item.discountAmount = item.discountAmountOriginal * this.saReturn.exchangeRate;
                    }
                    if (item.vatAmountOriginal || item.vatAmountOriginal === 0) {
                        item.vatAmount = item.vatAmountOriginal * this.saReturn.exchangeRate;
                    }
                } else {
                    if (item.unitPriceOriginal || item.unitPriceOriginal === 0) {
                        item.unitPrice = item.unitPriceOriginal / this.saReturn.exchangeRate;
                    }
                    if (item.amountOriginal || item.amountOriginal === 0) {
                        item.amount = item.amountOriginal / this.saReturn.exchangeRate;
                    }
                    if (item.discountAmountOriginal || item.discountAmountOriginal === 0) {
                        item.discountAmount = item.discountAmountOriginal / this.saReturn.exchangeRate;
                    }
                    if (item.vatAmountOriginal || item.vatAmountOriginal === 0) {
                        item.vatAmount = item.vatAmountOriginal / this.saReturn.exchangeRate;
                    }
                }
                this.utilService.round(item.unitPrice, this.currentAccount.systemOption, 2);
                this.utilService.round(item.amount, this.currentAccount.systemOption, 8);
                this.utilService.round(item.discountAmount, this.currentAccount.systemOption, 8);
                this.utilService.round(item.vatAmount, this.currentAccount.systemOption, 8);
            });
            this.updateSaBill();
        }
    }

    changeQuantity(index) {
        if (this.saReturnDetails[index].materialGoodsSpecificationsLedgers) {
            const iWQuantity = this.saReturnDetails[index].materialGoodsSpecificationsLedgers.reduce(function(prev, cur) {
                return prev + cur.iWQuantity;
            }, 0);
            if (this.saReturnDetails[index].quantity !== iWQuantity) {
                this.toastrService.error(this.translateService.instant('ebwebApp.materialGoodsSpecifications.notEqualsQuantity'));
                return;
            }
        }
        this.calculateMainQuantity(index);
        this.changeAmountOriginal(index);
        this.checkSaleDiscountPolicy(index);
    }

    changeAmountOriginal(index, isSelfChange = false) {
        if (!isSelfChange) {
            this.calculateAmountOriginal(index);
        } else {
            this.calculateUnitPriceOriginalByAmountOriginal(index);
            this.calculateUnitPrice(index);
        }
        this.calculateAmount(index);
        this.calculateDiscountAmountOriginal(index);
        this.calculateDiscountAmount(index);
        this.checkSaleDiscountPolicy(index);
        this.calculateVatAmountOriginal(index);
        this.calculateVatAmount(index);
        this.changeOWPrice(index);
        this.updateSaBill();
    }

    calculateAmountOriginal(index) {
        if (
            (this.saReturnDetails[index].quantity || this.saReturnDetails[index].quantity === 0) &&
            (this.saReturnDetails[index].unitPriceOriginal || this.saReturnDetails[index].unitPriceOriginal === 0)
        ) {
            this.saReturnDetails[index].amountOriginal = this.utilService.round(
                this.saReturnDetails[index].quantity * this.saReturnDetails[index].unitPriceOriginal,
                this.currentAccount.systemOption,
                this.saReturn.currencyID === this.currencyCode ? 7 : 8
            );
        }
    }

    calculateAmount(index) {
        if (this.saReturn.exchangeRate && this.currency) {
            if (this.currency.formula === '*') {
                this.saReturnDetails[index].amount = this.utilService.round(
                    this.saReturnDetails[index].amountOriginal * this.saReturn.exchangeRate,
                    this.currentAccount.systemOption,
                    7
                );
            } else if (this.currency.formula === '/' && this.saReturn.exchangeRate !== 0) {
                this.saReturnDetails[index].amount = this.utilService.round(
                    this.saReturnDetails[index].amountOriginal / this.saReturn.exchangeRate,
                    this.currentAccount.systemOption,
                    7
                );
            }
        }
    }

    changeDiscountRate(index) {
        this.calculateDiscountAmountOriginal(index);
        this.calculateDiscountAmount(index);
        this.checkSaleDiscountPolicy(index);
        this.calculateVatAmountOriginal(index);
        this.calculateVatAmount(index);
        this.updateSaBill();
    }

    calculateDiscountAmountOriginal(index) {
        if (
            (this.saReturnDetails[index].amountOriginal || this.saReturnDetails[index].amountOriginal === 0) &&
            (this.saReturnDetails[index].discountRate || this.saReturnDetails[index].discountRate === 0)
        ) {
            this.saReturnDetails[index].discountAmountOriginal = this.utilService.round(
                this.saReturnDetails[index].amountOriginal * this.saReturnDetails[index].discountRate / 100,
                this.currentAccount.systemOption,
                this.saReturn.currencyID === this.currencyCode ? 7 : 8
            );
        } else {
            this.saReturnDetails[index].discountAmountOriginal = 0;
            this.saReturnDetails[index].discountAmount = 0;
        }
    }

    changeDiscountAmount(index) {
        if (this.saReturnDetails[index].discountAmountOriginal || this.saReturnDetails[index].discountAmountOriginal === 0) {
            this.calculateDiscountRate(index);
            this.calculateDiscountAmount(index);
        }
        this.changeVATRate(index);
    }

    calculateDiscountRate(index) {
        if (
            (this.saReturnDetails[index].discountAmountOriginal || this.saReturnDetails[index].discountAmountOriginal === 0) &&
            (this.saReturnDetails[index].amountOriginal || this.saReturnDetails[index].amountOriginal === 0)
        ) {
            this.saReturnDetails[index].discountRate =
                this.saReturnDetails[index].discountAmountOriginal / this.saReturnDetails[index].amountOriginal * 100;
            this.utilService.round(this.saReturnDetails[index].discountRate, this.currentAccount.systemOption, 5);
        }
    }

    calculateDiscountAmount(index) {
        if (
            (this.saReturnDetails[index].discountAmountOriginal || this.saReturnDetails[index].discountAmountOriginal === 0) &&
            (this.saReturn.exchangeRate || this.saReturn.exchangeRate === 0)
        ) {
            if (this.saReturn.exchangeRate && this.currency) {
                if (this.currency.formula === '*') {
                    this.saReturnDetails[index].discountAmount = this.utilService.round(
                        this.saReturnDetails[index].discountAmountOriginal * this.saReturn.exchangeRate,
                        this.currentAccount.systemOption,
                        7
                    );
                } else if (this.currency.formula === '/' && this.saReturn.exchangeRate !== 0) {
                    this.saReturnDetails[index].discountAmount = this.utilService.round(
                        this.saReturnDetails[index].discountAmountOriginal / this.saReturn.exchangeRate,
                        this.currentAccount.systemOption,
                        7
                    );
                }
            }
        }
    }

    changeVATRate(index) {
        this.calculateVatAmountOriginal(index);
        this.calculateVatAmount(index);
        this.updateSaBill();
    }

    calculateVatAmountOriginal(index) {
        if (
            (this.saReturnDetails[index].amountOriginal ||
                this.saReturnDetails[index].amountOriginal === 0 ||
                this.saReturnDetails[index].discountAmountOriginal ||
                this.saReturnDetails[index].discountAmountOriginal === 0) &&
            (this.saReturnDetails[index].vatRate === 1 || this.saReturnDetails[index].vatRate === 2)
        ) {
            if (!this.saReturnDetails[index].amountOriginal) {
                this.saReturnDetails[index].amountOriginal = 0;
            }
            if (!this.saReturnDetails[index].discountAmountOriginal) {
                this.saReturnDetails[index].discountAmountOriginal = 0;
            }
            this.saReturnDetails[index].vatAmountOriginal = this.utilService.round(
                (this.saReturnDetails[index].amountOriginal - this.saReturnDetails[index].discountAmountOriginal) *
                    (this.saReturnDetails[index].vatRate === 1 ? 0.05 : 0.1),
                this.currentAccount.systemOption,
                this.saReturn.currencyID === this.currencyCode ? 7 : 8
            );
            if (this.saReturn.exchangeRate) {
                if (this.currency.formula === '*') {
                    this.saReturnDetails[index].vatAmount = this.utilService.round(
                        this.saReturnDetails[index].vatAmountOriginal * this.saReturn.exchangeRate,
                        this.currentAccount.systemOption,
                        7
                    );
                } else if (this.currency.formula === '/' && this.saReturn.exchangeRate !== 0) {
                    this.saReturnDetails[index].vatAmount = this.utilService.round(
                        this.saReturnDetails[index].vatAmountOriginal / this.saReturn.exchangeRate,
                        this.currentAccount.systemOption,
                        7
                    );
                }
            }
        } else {
            this.saReturnDetails[index].vatAmountOriginal = 0;
            this.saReturnDetails[index].vatAmount = 0;
        }
    }

    calculateVatAmount(index) {
        if (this.saReturn.exchangeRate && this.currency) {
            if (this.currency.formula === '*') {
                this.saReturnDetails[index].vatAmount = this.utilService.round(
                    this.saReturnDetails[index].vatAmountOriginal * this.saReturn.exchangeRate,
                    this.currentAccount.systemOption,
                    7
                );
            } else if (this.currency.formula === '/' && this.saReturn.exchangeRate !== 0) {
                this.saReturnDetails[index].vatAmount = this.utilService.round(
                    this.saReturnDetails[index].vatAmountOriginal / this.saReturn.exchangeRate,
                    this.currentAccount.systemOption,
                    7
                );
            }
            this.updateSaBill();
        }
    }

    @ebAuth(['ROLE_ADMIN', window.location.href.includes('tra-lai') ? ROLE.HangBanTraLai_Sua : ROLE.HangBanGiamGia_Sua])
    edit() {
        event.preventDefault();
        if (!this.utilsService.isShowPopup) {
            this.isEdit = true;
            this.accountingObjects = this.accountingObjectAlls
                .filter(n => n.isActive)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            this.employee = this.employeeAll
                .filter(n => n.isActive)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            this.copy();
        }
        if (this.rsDataSession) {
            this.rsDataSession.isEdit = false;
            sessionStorage.setItem('nhapKhoDataSession', JSON.stringify(this.rsDataSession));
            this.copy();
        }
    }

    copy() {
        this.saReturnCopy = Object.assign({}, this.saReturn);
        this.saReturnDetailsCopy = this.saReturnDetails.map(object => ({ ...object }));
    }

    closeEdit(content) {
        if (!this.canDeactive()) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else {
            this.closeForm();
        }
    }

    justClose() {
        this.modalRef.close();
    }

    closeWarning() {
        if (this.modalRef) {
            this.modalRef.close();
            this.warningVatRate = 0;
            this.warningEmployee = false;
        }
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
            this.closeForm();
        }
        this.isEdit = false;
    }

    @ebAuth(['ROLE_ADMIN', window.location.href.includes('tra-lai') ? ROLE.HangBanTraLai_Them : ROLE.HangBanGiamGia_Them])
    addNew(isAdd = false) {
        event.preventDefault();
        if (this.isEdit && this.utilsService.isShowPopup) {
            return;
        }
        this.isEdit = true;
        this.saReturn = {
            autoOWAmountCal: 0,
            typeID: this.activatedRoute.routeConfig.path.includes('giam-gia') ? this.HANG_GIAM_GIA : this.HANG_BAN_TRA_LAI,
            isDeliveryVoucher: this.saReturn.typeID === this.HANG_BAN_TRA_LAI && this.isKiemPhieuNhapKho && !this.saReturn.id,
            isBill: this.saReturn.typeID === this.HANG_GIAM_GIA && this.isLapKemHoaDon && !this.saReturn.id
        };
        this.isEditReasonFirst = true;
        this.isEditReasonNKFirst = true;
        this.saReturn.date = this.utilsService.ngayHachToan(this.currentAccount);
        this.saReturn.postedDate = this.saReturn.date;
        this.template = null;
        this.saReturnDetails = [];
        this.template = {};
        this.saReturn.typeLedger = 2;
        this.currency = this.currencies.find(cur => cur.currencyCode === this.currentAccount.organizationUnit.currencyID);
        this.saReturn.currencyID = this.currency.currencyCode;
        this.saReturn.exchangeRate = this.currency.exchangeRate;
        this.saBillList = [];
        if (!this.saReturn.id) {
            if (this.saReturn.typeID === TypeID.HANG_BAN_TRA_LAI) {
                this.translateService.get(['ebwebApp.saReturn.reasonTL']).subscribe((res: any) => {
                    this.saReturn.reason = res['ebwebApp.saReturn.reasonTL'];
                });
            } else {
                this.translateService.get(['ebwebApp.saReturn.reasonGG']).subscribe((res: any) => {
                    this.saReturn.reason = res['ebwebApp.saReturn.reasonGG'];
                });
            }
            this.translateService.get(['ebwebApp.saReturn.reasonNK']).subscribe((res: any) => {
                this.reason = res['ebwebApp.saReturn.reasonNK'];
            });
        }
        if (this.saReturn.typeID === this.HANG_GIAM_GIA && this.isLapKemHoaDon) {
            this.saReturn.isBill = true;
        }
        if (this.saReturn.typeID === this.HANG_BAN_TRA_LAI && this.isKiemPhieuNhapKho) {
            this.saReturn.isDeliveryVoucher = true;
        }
        this.checkInventoryVoucher(true);
        this.getNoBookVoucher();
    }

    @ebAuth(['ROLE_ADMIN', window.location.href.includes('tra-lai') ? ROLE.HangBanTraLai_Them : ROLE.HangBanGiamGia_Them])
    copyAndNew() {
        event.preventDefault();
        if (!this.isEdit && this.saReturn && this.saReturn.id && !this.utilsService.isShowPopup) {
            this.saReturn.id = null;
            this.saReturn.rsInwardOutwardID = null;
            this.saReturn.invoiceNo = '';
            this.saReturn.isExportInvoice = false;
            this.saReturnDetails.forEach(item => {
                item.id = null;
                item.saInvoiceDetailID = null;
                item.saInvoiceID = null;
                item.saBillDetailID = null;
                item.saBillID = null;
                item.noFBook = null;
                item.date = null;
            });
            this.viewVouchersSelected = [];
            this.isEdit = true;
            this.checkInventoryVoucher(true);
            this.getNoBookVoucher();
        }
    }

    registerChangeInAccountDefaults() {
        this.eventSubscriber = this.eventManager.subscribe('hangBanTraLaiModification', response => {
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.previousState();
        });
    }

    registerRef() {
        this.eventSubscriber4 = this.eventManager.subscribe('selectViewVoucher', response => {
            if (this.isEdit) {
                this.viewVouchersSelected = this.viewVouchersSelected.filter(x => x.attach === true);
                response.content.forEach(item => {
                    item.attach = false;
                    this.viewVouchersSelected.push(item);
                });
            }
        });
    }

    registerSaInvoice() {
        this.eventSubscriber2 = this.eventManager.subscribe('selectSaInvoice', response => {
            for (let i = 0; i < this.saReturnDetails.length; i++) {
                if (this.saReturnDetails[i].saInvoiceDetailID) {
                    this.saReturnDetails.splice(i, 1);
                    i--;
                }
            }
            this.saBillList = response.content;
            this.viewVouchersSelected = this.viewVouchersSelected.filter(x => !x.attach);
            if (this.saBillList && this.saBillList.length) {
                this.saBillList.forEach(object => {
                    const detail = this.convertItemSelect(object, true);
                    detail.debitAccount = this.debitAccountDefault;
                    this.saReturnDetails.push(detail);
                    this.changeQuantity(this.saReturnDetails.indexOf(detail));
                    if (this.viewVouchersSelected.filter(x => x.refID2 === object.saInvoiceID).length === 0) {
                        this.viewVouchersSelected.push({
                            id: null,
                            refID1: null,
                            refID2: object.saInvoiceID,
                            no: object.noMBook + object.noFBook,
                            date: moment(object.date).format(DATE_FORMAT_SLASH),
                            postedDate: moment(object.date).format(DATE_FORMAT_SLASH),
                            reason: object.reason,
                            typeID: object.typeID,
                            typeGroupID: GROUP_TYPEID.GROUP_SAINVOICE,
                            attach: true
                        });
                    }
                });
                const isTheSame = this.saBillList.every(item => item.accountingObjectID2 === this.saBillList[0].accountingObjectID2);
                if (isTheSame) {
                    this.saReturn.accountingObject = this.accountingObjects.find(
                        item => item.id === this.saBillList[0].accountingObjectID2
                    );
                    this.selectAccountingObject();
                }
            }
        });
    }

    registerBill() {
        this.eventManager.subscribe('selectViewInvoice', ref => {
            for (let i = 0; i < this.saReturnDetails.length; i++) {
                if (this.saReturnDetails[i].saBillDetailID) {
                    this.saReturnDetails.splice(i, 1);
                    i--;
                }
            }
            this.saBillList = ref.content;
            this.saBillList.forEach(object => {
                this.saReturnDetails.push(this.convertItemSelect(object, false));
            });
        });
    }

    convertItemSelect(item, isMore?: boolean) {
        const target = new SaReturnDetails();
        Object.assign(target, item);
        // target.debitAccount = item.creditAccount;
        target.creditAccount = item.debitAccount;
        target.debitAccount = null;
        if (isMore) {
            target.saInvoiceDetailID = item.id;
            target.saInvoiceID = item.saInvoiceID;
        } else {
            target.saBillDetailID = item.id;
            target.saBillID = item.saBillID;
        }
        target.isPromotion = item.promotion;
        if (item.returnQuantity) {
            target.quantity = target.mainQuantity = item.returnQuantity;
        }

        target.totalQuantity = target.totalQuantity = item.quantity;
        target.materialGoods = this.materialGoodss.find(n => n.id === item.materialGoodsID);
        const unit = this.units.find(data => data.id === item.unitID);

        if (unit) {
            target.units = this.units.filter(data => data.materialGoodsID === unit.materialGoodsID);
        }

        if (target.units) {
            if (unit) {
                target.unit = unit;
            }
            if (item.mainUnitID) {
                target.mainUnit = target.units.find(i => i.id === item.mainUnitID);
            }
        }
        target.isLotNoReadOnly = true;
        return target;
    }

    convertItemSelectBack() {
        this.saBillList = [];
        this.saReturnDetails.forEach(item => {
            if (item.saInvoiceDetailID) {
                this.saBillList.push({ id: item.saInvoiceDetailID, saInvoiceID: item.saInvoiceID, returnQuantity: item.quantity });
            }
        });
    }

    updateSaBill() {
        this.saReturn.totalAmount = this.utilService.round(this.sum('amount'), this.currentAccount.systemOption, 7);
        this.saReturn.totalVatAmount = this.utilService.round(this.sum('vatAmount'), this.currentAccount.systemOption, 7);
        this.saReturn.totalDiscountAmount = this.utilService.round(this.sum('discountAmount'), this.currentAccount.systemOption, 7);
        this.saReturn.totalPaymentAmount = this.utilService.round(
            this.saReturn.totalAmount - this.saReturn.totalDiscountAmount + this.saReturn.totalVatAmount,
            this.currentAccount.systemOption,
            8
        );

        this.saReturn.totalAmountOriginal = this.utilService.round(
            this.sum('amountOriginal'),
            this.currentAccount.systemOption,
            this.saReturn.currencyID === this.currencyCode ? 7 : 8
        );
        this.saReturn.totalVatAmountOriginal = this.utilService.round(
            this.sum('vatAmountOriginal'),
            this.currentAccount.systemOption,
            this.saReturn.currencyID === this.currencyCode ? 7 : 8
        );
        this.saReturn.totalDiscountAmountOriginal = this.utilService.round(
            this.sum('discountAmountOriginal'),
            this.currentAccount.systemOption,
            this.saReturn.currencyID === this.currencyCode ? 7 : 8
        );
        this.saReturn.totalPaymentAmountOriginal = this.utilService.round(
            this.saReturn.totalAmountOriginal - this.saReturn.totalDiscountAmountOriginal + this.saReturn.totalVatAmountOriginal,
            this.currentAccount.systemOption,
            this.saReturn.currencyID === this.currencyCode ? 7 : 8
        );
        this.utilService.roundObjects(
            this.saReturnDetails,
            this.currentAccount.systemOption,
            this.saReturn.currencyID === this.currencyCode
        );
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.saReturnDetails.length; i++) {
            total += isNaN(this.saReturnDetails[i][prop]) ? 0 : this.saReturnDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    ngOnDestroy(): void {
        if (this.eventSubscriber) {
            this.eventManager.destroy(this.eventSubscriber);
        }
        if (this.eventSubscriber2) {
            this.eventManager.destroy(this.eventSubscriber2);
        }
        if (this.eventSubscriber3) {
            this.eventManager.destroy(this.eventSubscriber3);
        }
        if (this.eventSubscriber4) {
            this.eventManager.destroy(this.eventSubscriber4);
        }
    }

    public beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            this.modalRef = this.refModalService.open(this.viewVouchersSelected);
        } else if ($event.nextId === 'sell-voucher') {
            $event.preventDefault();
            if (this.saReturn.id) {
                let index = 0;
                this.saReturnDetails.forEach(item => {
                    if (item.saInvoiceID) {
                        this.saBillList.push(Object.assign({}));
                        this.saBillList[index].id = item.saInvoiceDetailID;
                        this.saBillList[index].saInvoiceID = item.saInvoiceID;
                        index++;
                    }
                });
            }
            this.modalRef = this.refModalService.open(
                this.saBillList,
                EbSaInvoiceModalComponent,
                100,
                false,
                this.saReturn.typeID,
                null,
                this.saReturn.currencyID,
                this.saReturn.id ? this.saReturn.id : null,
                this.saReturn.accountingObject ? this.saReturn.accountingObject.id : null
            );
        }
        this.checkInventoryVoucher();
    }

    checkInvoiceNo() {
        this.saReturn.invoiceNo = this.utilService.pad(this.saReturn.invoiceNo, 7);
    }

    checkEditKPNK() {
        if (this.saReturn.id) {
            this.saReturnService
                .checkRelateVoucher({
                    saReturnID: this.saReturn.id
                })
                .subscribe(res => {
                    if (res.body) {
                        this.isEditKPNK = false;
                        this.toastrService.error(
                            this.translateService.instant('global.messages.error.dontEditKPXK'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                    } else {
                        this.isEditKPNK = true;
                    }
                });
        } else {
            this.isEditKPNK = true;
        }
    }

    checkInventoryVoucher(force = false) {
        if (this.index === 2) {
            this.index = 1;
        }
        if (!this.noBookInventory || force) {
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: this.TYPE_GROUP_RS_IO, // typeGroupID loại chứng từ
                    displayOnBook: this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
                })
                .subscribe((res: HttpResponse<string>) => {
                    this.noBookInventory = res.body;
                });
            if (this.saReturn.isDeliveryVoucher) {
                this.reason = this.translateService.instant('ebwebApp.saReturn.reasonNK');
            }
        }
        if (!this.saReturn.isDeliveryVoucher) {
            this.saReturnDetails.forEach(item => {
                item.owAmount = null;
                item.owPrice = null;
                item.repositoryAccount = null;
                item.costAccount = null;
            });
        }
    }

    exportPdf(isDownload, typeReports: number) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.saReturn.id,
                typeID: this.saReturn.typeID,
                typeReport: typeReports
            },
            isDownload
        );
        this.toastrService.success(
            this.translateService.instant('ebwebApp.mBDeposit.printing'),
            this.translateService.instant('ebwebApp.mBDeposit.message')
        );
    }

    @ebAuth(['ROLE_ADMIN', window.location.href.includes('tra-lai') ? ROLE.HangBanTraLai_GhiSo : ROLE.HangBanGiamGia_GhiSo])
    record() {
        event.preventDefault();
        if (
            !this.isEdit &&
            !this.saReturn.recorded &&
            !this.checkCloseBook(this.currentAccount, this.saReturn.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            const record_: Irecord = {};
            record_.id = this.saReturn.id;
            record_.typeID = this.saReturn.typeID;
            record_.repositoryLedgerID = this.saReturn.rsInwardOutwardID;
            if (!this.saReturn.recorded) {
                this.gLService.record(record_).subscribe((res: HttpResponse<Irecord>) => {
                    if (res.body.success) {
                        this.saReturn.recorded = true;
                        this.materialGoodsService.queryForComboboxGood().subscribe((mate: HttpResponse<IMaterialGoods[]>) => {
                            this.materialGoodss = mate.body;
                        });
                        this.toastrService.success(
                            this.translateService.instant('ebwebApp.mBTellerPaper.recordToast.done'),
                            this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.toastrService.error(
                            this.translateService.instant('global.messages.error.checkTonQuyQT'),
                            this.translateService.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.toastrService.error(
                            this.translateService.instant('global.messages.error.checkTonQuyTC'),
                            this.translateService.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.toastrService.error(
                            this.translateService.instant('global.messages.error.checkTonQuy'),
                            this.translateService.instant('ebwebApp.mCReceipt.error.error')
                        );
                    }
                });
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', window.location.href.includes('tra-lai') ? ROLE.HangBanTraLai_GhiSo : ROLE.HangBanGiamGia_GhiSo])
    unrecord() {
        event.preventDefault();
        if (
            !this.isEdit &&
            this.saReturn.recorded &&
            !this.checkCloseBook(this.currentAccount, this.saReturn.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            const record_: Irecord = {};
            record_.id = this.saReturn.id;
            record_.typeID = this.saReturn.typeID;
            record_.repositoryLedgerID = this.saReturn.rsInwardOutwardID;
            this.gLService.unrecord(record_).subscribe(
                (res: HttpResponse<Irecord>) => {
                    if (res.body.success) {
                        this.saReturn.recorded = false;
                        this.materialGoodsService.queryForComboboxGood().subscribe((mate: HttpResponse<IMaterialGoods[]>) => {
                            this.materialGoodss = mate.body;
                        });
                        this.toastrService.success(
                            this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.done'),
                            this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                        );
                    }
                },
                error => {
                    if (error.error.errorKey === 'unrecord') {
                        this.modalRef = this.modalService.open(this.unrecordModal, { backdrop: 'static' });
                    }
                }
            );
        }
    }

    doUnrecord() {
        const record_: Irecord = {};
        record_.id = this.saReturn.id;
        record_.typeID = this.saReturn.typeID;
        record_.repositoryLedgerID = this.saReturn.rsInwardOutwardID;
        record_.answer = true;
        this.gLService.unrecord(record_).subscribe((res: HttpResponse<Irecord>) => {
            if (res.body.success) {
                this.saReturn.recorded = false;
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.done'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                );
            }
            if (this.modalRef) {
                this.modalRef.close();
            }
        });
    }

    changeOWPrice(index) {
        if (
            this.saReturnDetails[index].quantity ||
            this.saReturnDetails[index].quantity === 0 ||
            this.saReturnDetails[index].owPrice ||
            this.saReturnDetails[index].owPrice === 0
        ) {
            this.saReturnDetails[index].owAmount = this.utilService.round(
                this.saReturnDetails[index].quantity * this.saReturnDetails[index].owPrice,
                this.currentAccount.systemOption,
                7
            );
        }
    }
    changeOWAmount(index) {
        if (this.saReturnDetails[index].owAmount || this.saReturnDetails[index].owAmount === 0 || this.saReturnDetails[index].quantity) {
            this.saReturnDetails[index].owPrice = this.saReturnDetails[index].owAmount / this.saReturnDetails[index].quantity;
        }
    }

    checkSaleDiscountPolicy(index) {
        if (this.isSelfChange) {
            return;
        }
        if (
            (this.saReturnDetails[index].quantity || this.saReturnDetails[index].quantity === 0) &&
            this.saReturnDetails[index].saleDiscountPolicys
        ) {
            let goto = true;
            this.saReturnDetails[index].saleDiscountPolicys.forEach(saleDiscountPolicy => {
                if (
                    saleDiscountPolicy.quantityFrom <= this.saReturnDetails[index].quantity &&
                    this.saReturnDetails[index].quantity <= saleDiscountPolicy.quantityTo
                ) {
                    goto = false;
                    if (saleDiscountPolicy.discountType === 0) {
                        this.saReturnDetails[index].discountRate = saleDiscountPolicy.discountResult;
                        if (
                            (this.saReturnDetails[index].amountOriginal || this.saReturnDetails[index].amountOriginal === 0) &&
                            (this.saReturnDetails[index].discountRate || this.saReturnDetails[index].discountRate === 0)
                        ) {
                            this.saReturnDetails[index].discountAmountOriginal =
                                this.saReturnDetails[index].amountOriginal * this.saReturnDetails[index].discountRate / 100;
                            this.calculateDiscountAmount(index);
                        } else {
                            this.saReturnDetails[index].discountAmountOriginal = 0;
                            this.saReturnDetails[index].discountAmount = 0;
                        }
                    } else if (saleDiscountPolicy.discountType === 1) {
                        this.saReturnDetails[index].discountAmountOriginal = saleDiscountPolicy.discountResult;
                        this.calculateDiscountAmount(index);
                        this.saReturnDetails[index].discountRate = null;
                        this.calculateDiscountRate(index);
                    } else if (saleDiscountPolicy.discountType === 2) {
                        this.saReturnDetails[index].discountAmountOriginal =
                            saleDiscountPolicy.discountResult * this.saReturnDetails[index].quantity;
                        this.calculateDiscountAmount(index);
                        this.saReturnDetails[index].discountRate = null;
                        this.calculateDiscountRate(index);
                    }
                }
                if (goto) {
                    this.saReturnDetails[index].discountRate = this.saReturnDetails[index].materialGoods.saleDiscountRate;
                }
            });
        }
    }

    selectAccountingObjectBankAccount() {
        this.saReturn.accountingObjectBankAccount = this.accountingObjectBankAccount.bankAccount;
        this.saReturn.accountingObjectBankName = this.accountingObjectBankAccount.bankName;
    }

    canDeactive() {
        // console.log(this.saReturn);
        // console.log(this.saReturnCopy);
        // console.log(this.utilService.isEquivalent(this.saReturn, this.saReturnCopy));
        if (!this.isEdit) {
            return true;
        } else {
            return (
                this.utilService.isEquivalent(this.saReturn, this.saReturnCopy) &&
                this.utilService.isEquivalentArray(this.saReturnDetails, this.saReturnDetailsCopy)
            );
        }
    }

    openModel() {
        // lấy hóa đơn
        this.modalRef = this.exportInvoiceModalService.open(
            this.saBillList,
            this.currency.currencyCode,
            this.saReturn.accountingObject ? this.saReturn.accountingObject.id : null,
            this.saReturn.typeID === this.HANG_BAN_TRA_LAI ? this.TYPE_HANG_BAN_TRA_LAI : this.TYPE_HANG_BAN_GIAM_GIA
        );
    }

    changeReason() {
        if (this.isEditReasonFirst) {
            this.isEditReasonFirst = false;
        }
    }

    changeReasonNK() {
        if (this.isEditReasonNKFirst) {
            this.isEditReasonNKFirst = false;
        }
    }

    checkRequiredAccountingObject(isAlert) {
        let isError = false;
        if (!this.saReturn.accountingObject && this.isEdit) {
            for (let i = 0; i < this.saReturnDetails.length; i++) {
                if (this.debitAccountList) {
                    isError = this.debitAccountList.some(
                        item2 => this.saReturnDetails[i].debitAccount === item2.accountNumber && item2.detailType === '1'
                    );
                }
                if (isError && isAlert) {
                    this.toastrService.error(
                        this.translateService.instant('ebwebApp.saReturn.error.accountingObjectDebitAccount', {
                            param: this.saReturnDetails[i].debitAccount
                        })
                    );
                }
                if (isError) {
                    break;
                }
                if (this.creditAccountList) {
                    isError = this.creditAccountList.some(
                        item2 => this.saReturnDetails[i].creditAccount === item2.accountNumber && item2.detailType === '1'
                    );
                }
                if (isError && isAlert) {
                    this.toastrService.error(
                        this.translateService.instant('ebwebApp.saReturn.error.accountingObjectDebitAccount', {
                            param: this.saReturnDetails[i].creditAccount
                        })
                    );
                }
                if (isError) {
                    break;
                }
            }
        }
        return isError;
    }

    changeAccountingName() {
        if (this.isEditReasonFirst) {
            if (this.saReturn.typeID === TypeID.HANG_BAN_TRA_LAI) {
                this.translateService.get(['ebwebApp.saReturn.reasonTL1']).subscribe((res: any) => {
                    this.saReturn.reason = res['ebwebApp.saReturn.reasonTL1'] + ' ' + this.saReturn.accountingObjectName;
                });
            } else {
                this.translateService.get(['ebwebApp.saReturn.reasonGG1']).subscribe((res: any) => {
                    this.saReturn.reason = res['ebwebApp.saReturn.reasonGG1'] + ' ' + this.saReturn.accountingObjectName;
                });
            }
        }
        if (this.isEditReasonNKFirst) {
            this.translateService.get(['ebwebApp.saReturn.reasonNK1']).subscribe((res: any) => {
                this.reason = res['ebwebApp.saReturn.reasonNK1'] + ' ' + this.saReturn.accountingObjectName;
            });
        }
    }

    checkRequiredRepository(index) {
        return (
            this.saReturn.isDeliveryVoucher &&
            this.saReturnDetails[index].materialGoods &&
            this.saReturnDetails[index].materialGoods.materialGoodsType !== 2 &&
            this.saReturnDetails[index].materialGoods.materialGoodsType !== 4
        );
    }

    disableRepository(index) {
        return (
            this.saReturn.typeID !== this.HANG_GIAM_GIA &&
            (!this.saReturn.isDeliveryVoucher ||
                !this.saReturnDetails[index].materialGoodsID ||
                (this.saReturnDetails[index].materialGoods && this.saReturnDetails[index].materialGoods.materialGoodsType === 2) ||
                (this.saReturnDetails[index].materialGoods && this.saReturnDetails[index].materialGoods.materialGoodsType === 4))
        );
    }

    changeMainConvertRate(index) {
        this.calculateMainQuantity(index);
        this.calculateMainUnitPrice(index);
    }

    changeMainQuantity(index) {
        this.calculateQuantity(index);
        this.changeAmountOriginal(index);
    }

    changeMainUnitPrice(index) {
        this.calculateUnitPriceOriginal(index);
        this.calculateUnitPrice(index);
        this.changeAmountOriginal(index);
    }

    calculateQuantity(index) {
        if (this.saReturnDetails[index].mainQuantity) {
            this.saReturnDetails[index].quantity =
                this.saReturnDetails[index].formula === '*'
                    ? this.saReturnDetails[index].mainQuantity / this.saReturnDetails[index].mainConvertRate
                    : this.saReturnDetails[index].mainQuantity * this.saReturnDetails[index].mainConvertRate;
            this.utilService.round(this.saReturnDetails[index].quantity, this.currentAccount.systemOption, 3);
        }
    }

    calculateMainQuantity(index) {
        if (this.saReturnDetails[index].mainConvertRate) {
            this.saReturnDetails[index].mainQuantity =
                this.saReturnDetails[index].formula === '*'
                    ? this.saReturnDetails[index].quantity * this.saReturnDetails[index].mainConvertRate
                    : this.saReturnDetails[index].quantity / this.saReturnDetails[index].mainConvertRate;
            this.utilService.round(this.saReturnDetails[index].mainQuantity, this.currentAccount.systemOption, 3);
        } else {
            this.saReturnDetails[index].mainQuantity = null;
        }
    }

    calculateMainUnitPrice(index) {
        if (this.saReturnDetails[index].formula === '*' && this.saReturnDetails[index].mainConvertRate) {
            this.saReturnDetails[index].mainUnitPrice =
                this.saReturnDetails[index].unitPriceOriginal / this.saReturnDetails[index].mainConvertRate;
        } else {
            this.saReturnDetails[index].mainUnitPrice =
                this.saReturnDetails[index].unitPriceOriginal * this.saReturnDetails[index].mainConvertRate;
        }
        this.utilService.round(this.saReturnDetails[index].mainUnitPrice, this.currentAccount.systemOption, 1);
    }

    calculateUnitPriceOriginal(index) {
        if (this.saReturnDetails[index].mainConvertRate && this.saReturnDetails[index].mainUnitPrice) {
            this.saReturnDetails[index].unitPriceOriginal =
                this.saReturnDetails[index].formula === '*'
                    ? this.saReturnDetails[index].mainUnitPrice * this.saReturnDetails[index].mainConvertRate
                    : this.saReturnDetails[index].mainUnitPrice / this.saReturnDetails[index].mainConvertRate;
        } else {
            this.saReturnDetails[index].unitPriceOriginal = 0;
        }
        this.utilService.round(
            this.saReturnDetails[index].unitPriceOriginal,
            this.currentAccount.systemOption,
            this.saReturn.currencyID !== this.currencyCode ? 2 : 1
        );
    }

    calculateUnitPriceOriginalByAmountOriginal(index) {
        if (this.saReturnDetails[index].amountOriginal && this.saReturnDetails[index].quantity) {
            this.saReturnDetails[index].unitPriceOriginal =
                this.saReturnDetails[index].amountOriginal / this.saReturnDetails[index].quantity;
        } else {
            this.saReturnDetails[index].unitPriceOriginal = 0;
        }
        this.utilService.round(
            this.saReturnDetails[index].unitPriceOriginal,
            this.currentAccount.systemOption,
            this.saReturn.currencyID !== this.currencyCode ? 2 : 1
        );
    }

    calculateUnitPrice(index) {
        if (this.saReturn.exchangeRate && this.currency) {
            if (this.currency.formula === '*') {
                this.saReturnDetails[index].unitPrice = this.saReturnDetails[index].unitPriceOriginal * this.saReturn.exchangeRate;
            } else if (this.currency.formula === '/' && this.saReturn.exchangeRate !== 0) {
                this.saReturnDetails[index].unitPrice = this.saReturnDetails[index].unitPriceOriginal / this.saReturn.exchangeRate;
            }
            this.utilService.round(this.saReturnDetails[index].unitPrice, this.currentAccount.systemOption, 1);
        }
    }

    changeAutoOWAmountCal() {
        if (this.saReturn.autoOWAmountCal === 0) {
            for (let i = 0; i < this.saReturnDetails.length; i++) {
                this.saReturnDetails[i].owPrice = 0;
                this.saReturnDetails[i].owAmount = 0;
            }
        }
    }

    resetInvoice() {
        if (!this.saReturn.isBill) {
            this.index = 1;
            this.saReturn.invoiceForm = null;
            this.saReturn.invoiceDate = null;
            this.saReturn.invoiceSeries = '';
            this.saReturn.invoiceNo = '';
            this.saReturn.invoiceTemplate = '';
            this.saReturn.invoiceTypeID = null;
            this.template = null;
            this.saReturn.paymentMethod = null;
        } else {
            if (!this.saReturn.paymentMethod && this.saReturn.isBill) {
                this.saReturn.paymentMethod = this.paymentMethod[0].data;
            }
            if (this.templates.length === 1 && this.saReturn.isBill && !this.template) {
                this.template = this.templates[0];
                this.selectTemplate();
            }
        }
    }

    checkEditIsBill() {
        if (
            this.isEdit &&
            this.saReturn.isBill &&
            this.saReturn.invoiceForm === 0 &&
            this.saReturn.invoiceNo &&
            !this.isRequiredInvoiceNo
        ) {
            this.toastrService.error(
                this.translateService.instant('global.messages.error.dontEditIsBill'),
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
        }
    }

    changeLotNo(index) {
        if (this.saReturnDetails[index].lotNo) {
            const lotNoObject = this.saReturnDetails[index].materialGoods.lotNos.find(
                item => item.lotNo === this.saReturnDetails[index].lotNo
            );
            if (lotNoObject) {
                this.saReturnDetails[index].expiryDate = moment(lotNoObject.expiryDate);
            }
        }
    }

    disableInvoiceNo() {
        return this.isEdit && !this.isRequiredInvoiceNo && this.template && this.template.invoiceForm === 0;
    }

    @ebAuth(['ROLE_ADMIN', window.location.href.includes('tra-lai') ? ROLE.HangBanTraLai_Xoa : ROLE.HangBanGiamGia_Xoa])
    delete() {
        if (
            !this.isEdit &&
            !this.saReturn.recorded &&
            !this.checkCloseBook(this.currentAccount, this.saReturn.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            if (this.saReturn.isBill && this.saReturn.invoiceForm === 0 && !this.isRequiredInvoiceNo) {
                if (this.saReturn.invoiceNo) {
                    this.typeDelete = 2;
                } else {
                    this.typeDelete = 0;
                }
            } else if (this.saReturn.isBill) {
                this.typeDelete = 1;
            } else {
                this.typeDelete = 0;
            }
            this.modalRef = this.modalService.open(this.deleteItem, { size: 'lg', backdrop: 'static' });
        }
    }

    deleteVoucher() {
        if (this.saReturn.id) {
            if (this.typeDelete === 2) {
                this.toastrService.error(this.translateService.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
            } else {
                this.saReturnService.delete(this.saReturn.id).subscribe(
                    ref => {
                        this.toastrService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
                        if (this.modalRef) {
                            this.modalRef.close();
                        }
                        this.close();
                    },
                    ref => {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.error'));
                    }
                );
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', window.location.href.includes('tra-lai') ? ROLE.HangBanTraLai_Them : ROLE.HangBanGiamGia_Them])
    saveAndNew() {
        event.preventDefault();
        if (this.isEdit && !this.utilsService.isShowPopup) {
            if (this.currentAccount.systemOption.some(x => x.code === CANH_BAO_NHAN_VIEN && x.data === '1') && !this.saReturn.employee) {
                this.warningEmployee = true;
            }
            if (this.saReturnDetails.filter(x => !x.vatRate && x.vatRate !== 0 && x.vatAmountOriginal > 0).length > 0) {
                this.warningVatRate = 1;
            } else if (this.saReturnDetails.filter(x => x.vatRate !== 1 && x.vatRate !== 2 && x.vatAmountOriginal > 0).length > 0) {
                this.warningVatRate = 2;
            }
            if (this.warningEmployee || this.warningVatRate === 1 || this.warningVatRate === 2) {
                this.isSaveAndAdd = false;
                this.modalRef = this.modalService.open(this.contentSave, { backdrop: 'static' });
            } else {
                this.saveAll(true);
            }
        }
    }

    @ebAuth([
        'ROLE_ADMIN',
        window.location.href.includes('tra-lai') ? ROLE.HangBanTraLai_Them : ROLE.HangBanGiamGia_Them,
        window.location.href.includes('tra-lai') ? ROLE.HangBanTraLai_Sua : ROLE.HangBanGiamGia_Sua
    ])
    save(isSave = false) {
        if (this.isEdit && !this.utilsService.isShowPopup) {
            event.preventDefault();
            if (this.currentAccount.systemOption.some(x => x.code === CANH_BAO_NHAN_VIEN && x.data === '1') && !this.saReturn.employee) {
                this.warningEmployee = true;
            }
            if (this.saReturnDetails.filter(x => !x.vatRate && x.vatRate !== 0 && x.vatAmountOriginal > 0).length > 0) {
                this.warningVatRate = 1;
            } else if (this.saReturnDetails.filter(x => x.vatRate !== 1 && x.vatRate !== 2 && x.vatAmountOriginal > 0).length > 0) {
                this.warningVatRate = 2;
            }
            if (this.warningEmployee || this.warningVatRate === 1 || this.warningVatRate === 2) {
                this.isSaveAndAdd = false;
                this.modalRef = this.modalService.open(this.contentSave, { backdrop: 'static' });
            } else {
                this.saveAll(false);
            }
        }
    }

    copyRow(detail, isRigthClick?) {
        if (!this.getSelectionText() || isRigthClick) {
            const detailCopy: any = Object.assign({}, detail);
            detailCopy.id = null;
            this.saReturnDetails.push(detailCopy);
            this.updateSaBill();
            if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const id = this.idIndex;
                const row = this.saReturnDetails.length - 1;
                this.indexFocusDetailRow = row;
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(id + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
        }
    }

    addIfLastInput(i) {
        if (i === this.saReturnDetails.length - 1) {
            this.addNewRow();
        }
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    changeDate() {
        this.saReturn.postedDate = this.saReturn.date;
    }

    changeRefDateTime() {
        this.saReturn.invoiceDate = this.refDateTime;
    }

    changeIsAttachList() {
        if (!this.saReturn.isAttachList) {
            this.saReturn.listNo = '';
            this.saReturn.listDate = null;
            this.saReturn.listCommonNameInventory = '';
        }
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.saReturnDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.saReturn;
    }

    addDataToDetail() {
        this.saReturnDetails = this.details ? this.details : this.saReturnDetails;
        this.saReturn = this.parent ? this.parent : this.saReturn;
    }

    registerSelectMaterialGoodsSpecification() {
        this.eventSubscriber = this.eventManager.subscribe('selectMaterialGoodsSpecification', response => {
            this.viewMaterialGoodsSpecification(this.contextMenu.selectedData);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    viewMaterialGoodsSpecification(detail) {
        if (this.isEdit) {
            if (this.saReturn.typeID === this.HANG_BAN_TRA_LAI) {
                if (detail.materialGoods) {
                    if (detail.materialGoods.isFollow) {
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
    }

    registerMaterialGoodsSpecifications() {
        this.eventManager.subscribe('materialGoodsSpecifications', ref => {
            this.saReturnDetails[this.currentRow].materialGoodsSpecificationsLedgers = ref.content;
            this.saReturnDetails[this.currentRow].quantity = ref.content.reduce(function(prev, cur) {
                return prev + cur.iWQuantity;
            }, 0);
        });
    }
}
