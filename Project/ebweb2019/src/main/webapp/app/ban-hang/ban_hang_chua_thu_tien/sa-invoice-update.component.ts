import { AfterViewChecked, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { ISAInvoice } from 'app/shared/model/sa-invoice.model';
import { SAInvoiceService } from './sa-invoice.service';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { ICurrency } from 'app/shared/model/currency.model';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { IMaterialGoods, MaterialGoods } from 'app/shared/model/material-goods.model';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IUnit, Unit } from 'app/shared/model/unit.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { SaBill } from 'app/shared/model/sa-bill.model';
import { IaPublishInvoiceDetails } from 'app/shared/model/ia-publish-invoice-details.model';
import { IRSInwardOutward } from 'app/shared/model/rs-inward-outward.model';
import { ISaBillDetails } from 'app/shared/model/sa-bill-details.model';
import { Irecord } from 'app/shared/model/record';
import {
    AccountType,
    BH_KIEM_HD,
    BH_KIEM_PX,
    CALCULATE_OW,
    CANH_BAO_NHAN_VIEN,
    GROUP_TYPEID,
    HH_XUATQUASLTON,
    MATERIAL_GOODS_TYPE,
    MSGERROR,
    NHAP_DON_GIA_VON,
    PP_TINH_GIA_XUAT_KHO,
    PPINVOICE_TYPE,
    SO_LAM_VIEC,
    TCKHAC_SDSOQUANTRI,
    TCKHAC_SDTichHopHDDT,
    TypeID
} from 'app/app.constants';
import { IaPublishInvoiceDetailsService } from 'app/ban-hang/xuat-hoa-don/ia-publish-invoice-details.service';
import { PPInvoiceDetailsService } from 'app/entities/nhan-hoa-don/pp-invoice-details.service';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { AccountingObjectBankAccountService } from 'app/entities/accounting-object-bank-account';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { BudgetItemService } from 'app/entities/budget-item';
import { EMContractService } from 'app/entities/em-contract';
import { CostSetService } from 'app/entities/cost-set';
import { ExpenseItemService } from 'app/entities/expense-item';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { SystemOptionService } from 'app/he-thong/system-option';
import { SaBillService } from 'app/ban-hang/xuat-hoa-don/sa-bill.service';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { CurrencyService } from 'app/danhmuc/currency';
import { ToastrService } from 'ngx-toastr';
import { RepositoryService } from 'app/danhmuc/repository';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { AccountListService } from 'app/danhmuc/account-list';
import { UnitService } from 'app/danhmuc/unit';
import { Principal } from 'app/core';
import { TranslateService } from '@ngx-translate/core';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { DiscountReturnModalService } from 'app/core/login/discount-return-modal.service';
import { ExportInvoiceModalService } from 'app/core/login/export-invoice-modal.service';
import { DATE_FORMAT, DATE_FORMAT_SLASH, ITEMS_PER_PAGE } from 'app/shared';
import * as moment from 'moment';
import { ISAInvoiceDetails } from 'app/shared/model/sa-invoice-details.model';
import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { PaymentClauseService } from 'app/entities/payment-clause';
import { IMCReceipt } from 'app/shared/model/mc-receipt.model';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { MCReceiptService } from 'app/TienMatNganHang/phieu-thu/mc-receipt';
import { MBDepositService } from 'app/TienMatNganHang/BaoCo/mb-deposit';
import { EbSaQuoteModalComponent } from 'app/shared/modal/sa-quote/sa-quote.component';
import { IViewSAQuoteDTO } from 'app/shared/model/view-sa-quote.model';
import { AccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { SaleDiscountPolicyService } from 'app/entities/sale-discount-policy';
import { SaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';
import { DiscountAllocation, IDiscountAllocation } from 'app/shared/modal/discount-allocation/discount-allocation.model';
import { DiscountAllocationModalComponent } from 'app/shared/modal/discount-allocation/discount-allocation-modal.component';
import { RepositoryLedgerService } from 'app/entities/repository-ledger';
import { IViewSAOrderDTO } from 'app/shared/model/view-sa-order.model';
import { EbSaOrderModalComponent } from 'app/shared/modal/sa-order/sa-order.component';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { EmContractModalComponent } from 'app/shared/modal/em-contract/em-contract-modal.component';
import { PpInvoiceModalComponent } from 'app/shared/modal/pp-invoice/pp-invoice-modal.component';
import { RsOutwardModalComponent } from 'app/shared/modal/rs-outward/rs-outward-modal.component';
import { RsTranfersModalComponent } from 'app/shared/modal/rs-tranfers/rs-tranfers-modal.component';
import { IPPInvoiceDTO } from 'app/shared/modal/pp-invoice/pp-invoice-dto.model';
import { IRSOutWardDTO } from 'app/shared/modal/rs-outward/rs-outward-dto.model';
import { IRSTranferDTO } from 'app/shared/modal/rs-tranfers/rs-tranfers-dto.model';
import { SAInvoiceDetailsService } from 'app/ban-hang/ban_hang_chua_thu_tien/sa-invoice-details.service';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { EbSelectMaterialGoodsModalComponent } from 'app/shared/modal/select-material-goods/select-material-goods.component';
import { Repository } from 'app/shared/model/repository.model';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { IMaterialGoodsConvertUnit } from 'app/shared/model/material-goods-convert-unit.model';
import { MaterialGoodsConvertUnitService } from 'app/entities/material-goods-convert-unit';
import { CareerGroupService } from 'app/entities/career-group';
import { ICareerGroup } from 'app/shared/model/career-group.model';
import { RSInwardOutwardService } from 'app/entities/rs-inward-outward/rs-inward-outward.service';
import { EbMaterialGoodsSpecificationsModalComponent } from 'app/shared/modal/material-goods-specifications/material-goods-specifications.component';

@Component({
    selector: 'eb-sa-invoice-update',
    templateUrl: './sa-invoice-update.component.html',
    styleUrls: ['sa-invoice-details.css']
})
export class SAInvoiceUpdateComponent extends BaseComponent implements OnInit, AfterViewChecked {
    @ViewChild('contentUnRecord') contentUnRecord;
    @ViewChild('contentSave') contentSave;
    @ViewChild('deleteItem') deleteItem;
    @ViewChild('contentClose') contentClose;
    @ViewChild('contentCheckSave') contentCheckSave;
    private _sAInvoice: ISAInvoice;
    contextMenu: ContextMenu;
    isSaving: boolean;
    dateDp: any;
    postedDateDp: any;
    public option1: any;
    public option2: any;
    employee: IAccountingObject;
    employees: IAccountingObject[];
    employeeAlls: IAccountingObject[];
    accountingObjects: IAccountingObject[];
    accountingObjectAlls: IAccountingObject[];
    searchVoucher: ISearchVoucher;
    paymentMethod: any[];
    listVAT: any[];
    rowNum: number;
    count: number;
    isRecord: boolean;
    date: any;
    currencys: ICurrency[];
    listTimeLine: any[];
    fromDate: any;
    toDate: any;
    sAInvoiceDetails: ISAInvoiceDetails[];
    materialGoodss: MaterialGoods[];
    repositories: Repository[];
    repository: any;
    debitAccountItem: IAccountList;
    materialGoodsConvertUnits: IMaterialGoodsConvertUnit[];
    units: Unit[];
    unit: Unit;
    mainUnitName: any;
    amountOriginal: any;
    discountAccountList: IAccountList[];
    costAccountList: IAccountList[];
    repositoryAccountList: IAccountList[];
    creditAccountList: IAccountList[];
    debitAccountList: IAccountList[];
    vatAccountList: IAccountList[];
    exportTaxAmountAccountList: IAccountList[];
    exportTaxAccountCorrespondingList: IAccountList[];
    deductionDebitAccountList: IAccountList[];
    // accountDefaut
    discountAccountDefault: string;
    costAccountDefault: string;
    repositoryAccountDefault: string;
    creditAccountDefault: string;
    debitAccountDefault: string;
    vatAccountDefault: string;
    exportTaxAmountAccountDefault: string;
    deductionDebitAccountDefault: string;

    saleDiscountPolicys: SaleDiscountPolicy[];
    goodsServicePurchase: any;
    expenseItems: IExpenseItem[];
    costSets: ICostSet[];
    emContractList: IEMContract[];
    budgetItems: IBudgetItem[];
    paymentClauses: IPaymentClause[];
    paymentClause: IPaymentClause;
    viewSAQuote: IViewSAQuoteDTO[];
    viewSAOrder: IViewSAOrderDTO[];
    viewPPInvoice: IPPInvoiceDTO[];
    viewRSOutward: IRSOutWardDTO[];
    viewRSTranfer: IRSTranferDTO[];
    organizationUnits: any;
    statisticCodes: any;
    contextmenu = { value: false };
    contextmenuX = { value: 0 };
    contextmenuY = { value: 0 };
    selectedDetailTax = null;
    isShowDetail = { value: false };
    isShowDetailTax = { value: false };
    isReadOnly: boolean;
    accountingObjectBankAccountList: AccountingObjectBankAccount[];
    accountingObjectBankAccount: AccountingObjectBankAccount;
    authoritiesNoMBook: any;
    sessionWork: boolean;
    dataSession: IDataSessionStorage;
    itemsPerPage: number;
    pageCount: number;
    totalItems: number;
    searchData: string;
    isEditOWAmount: boolean;
    predicate: any;
    accountingObjectName: string;
    reverse: number;
    page: number;
    isRequiredInvoiceNo: boolean;
    checkData: boolean;
    checkReadOnly: boolean;
    isCloseAll: boolean;
    account: any;
    isCreateUrl: boolean;
    isMoreForm: boolean;
    isEdit: any;
    isEditInvoice: boolean;
    isEditKPXK: boolean;
    saBill: SaBill;
    templates: IaPublishInvoiceDetails[];
    template: IaPublishInvoiceDetails;
    rsInwardOutward: IRSInwardOutward;
    mCReceipt: IMCReceipt;
    mBDeposit: IMBDeposit;
    val: any;
    saBillDTL: ISaBillDetails;
    viewVouchersSelected: any[];
    modalRef: NgbModalRef;
    record_: Irecord;
    eventSubscriber: Subscription;
    searchDataSearch: any;
    noBookVoucher: any;
    noBookRSI: any;
    noMBDeposit: string;
    noMCReceipt: string;
    saInvoiceCopy: any;
    saInvoiceCopyDetailsCopy: any;
    checkBook: number;
    previousPage: any;
    pageVoucher: any;
    itemsPerPageVoucher: any;
    links: any;
    checkModalVoucher: any;
    rsInwardOutwardCopy: IRSInwardOutward;
    saBillCopy: SaBill;
    mCReceiptCopy: IMCReceipt;
    mBDepositCopy: IMBDeposit;
    saBillDetail: ISaBillDetails[];
    currencyCode: string;
    bankAccountDetails: IBankAccountDetails[];
    careerGroups: ICareerGroup[];
    autoPrinciples: IAutoPrinciple[];
    autoPrinciple: IAutoPrinciple;
    createFrom = 0;
    phepTinhTyGia: string;
    typeDelete: number;
    indexDetail: number;
    isEditReasonFirst: boolean;
    isEditReasonFirstBill: boolean;
    isEditReasonFirstXK: boolean;
    isEditReasonFirstBC: boolean;
    isEditReasonFirstPT: boolean;
    isViewFromRef: boolean;
    isSaveAndAdd: boolean;
    checkSLT: boolean;
    mgForPPOderTextCode: any;
    recorded: boolean;
    checkOpenSave: boolean;
    hiddenVAT: boolean;
    defaultCareerGroupID: string;
    indexFocusDetailRow: any;
    indexFocusDetailCol: any;
    idIndex: any;
    select: number;
    /*Phiếu thu Add by Hautv*/
    fromMCReceipt: boolean;
    rowNumberMCReceipt: number;
    countMCReceipt: number;
    searchVoucherMCReceipt: ISearchVoucher;
    mCReceiptID: string;
    TYPE_MC_RECEIPT = 100;
    TYPE_MC_RECEIPT_FROM_SAINVOICE = 102;
    TYPE_MC_RECEIPT_CUSTOM = 101;
    /*Phiếu thu Add by Hautv*/

    /*Báo có Add by Namnh*/
    fromMBDeposit: boolean;
    rowNumberMBDeposit: number;
    countMBDeposit: number;
    searchVoucherMBDeposit: ISearchVoucher;
    mBDepositID: string;
    TYPE_MB_DEPOSIT = 160;
    TYPE_MB_DEPOSIT_FROM_SAINVOICE = 162;
    TYPE_MB_DEPOSIT_CUSTOM = 161;
    /*Báo có Add by Namnh*/
    isViewFromEInvoice: boolean; // add by Hautv

    /*Xuất kho by Huypq*/
    isFromOutWard: boolean;
    rsDataSession: any;
    rsSearchData: any;
    rsTotalItems: any;
    rsRowNum: any;
    isViewRSInWardOutward: boolean;
    XUAT_KHO = TypeID.XUAT_KHO;
    XUAT_KHO_TU_DIEU_CHINH = TypeID.XUAT_KHO_TU_DIEU_CHINH;
    XUAT_KHO_TU_BAN_HANG = TypeID.XUAT_KHO_TU_BAN_HANG;
    XUAT_KHO_TU_HANG_MUA_TRA_LAI = TypeID.XUAT_KHO_TU_MUA_HANG;
    /*Xuất kho by Huypq*/

    ROLE_BanHang = ROLE.ChungTuBanHang_Xem;
    ROLE_Them = ROLE.ChungTuBanHang_Them;
    ROLE_Sua = ROLE.ChungTuBanHang_Sua;
    ROLE_Xoa = ROLE.ChungTuBanHang_Xoa;
    ROLE_GhiSo = ROLE.ChungTuBanHang_GhiSo;
    ROLE_In = ROLE.ChungTuBanHang_In;

    typeNotify: number;
    warningEmployee: boolean;
    warningVatRate: number;
    VAT_TU_DV = MATERIAL_GOODS_TYPE.SERVICE;
    VAT_TU_KHAC = MATERIAL_GOODS_TYPE.DIFF;

    constructor(
        private iaPublishInvoiceDetailsService: IaPublishInvoiceDetailsService,
        private ppInvoiceDetailsService: PPInvoiceDetailsService,
        private parseLinks: JhiParseLinks,
        private rsInwardOutwardService: RSInwardOutwardService,
        private mcReceiptService: MCReceiptService,
        private mbDepositService: MBDepositService,
        private accountingObjectBankAccountService: AccountingObjectBankAccountService,
        private bankAccountDetailsService: BankAccountDetailsService,
        private autoPrincipleService: AutoPrincipleService,
        private statisticsCodeService: StatisticsCodeService,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        private budgetItemService: BudgetItemService,
        private emContractService: EMContractService,
        private eventManager: JhiEventManager,
        private costSetService: CostSetService,
        private expenseItemService: ExpenseItemService,
        private organizationUnitService: OrganizationUnitService,
        private systemOptionService: SystemOptionService,
        private saBillService: SaBillService,
        private activatedRoute: ActivatedRoute,
        private accountingObjectService: AccountingObjectService,
        private paymentClauseService: PaymentClauseService,
        private gLService: GeneralLedgerService,
        private currencyService: CurrencyService,
        private toasService: ToastrService,
        private modalService: NgbModal,
        private repositoryService: RepositoryService,
        private materialGoodsService: MaterialGoodsService,
        private accountListService: AccountListService,
        private unitService: UnitService,
        private repositoryLedgerService: RepositoryLedgerService,
        private saleDiscountPolicyService: SaleDiscountPolicyService,
        private router: Router,
        private principal: Principal,
        public translateService: TranslateService,
        private refModalService: RefModalService,
        private discountReturnModalService: DiscountReturnModalService,
        public utilsService: UtilsService,
        public sAInvoiceService: SAInvoiceService,
        private exportInvoiceModalService: ExportInvoiceModalService,
        private sAInvoiceDetailsService: SAInvoiceDetailsService,
        private materialGoodsConvertUnitService: MaterialGoodsConvertUnitService,
        private careerGroupService: CareerGroupService
    ) {
        super();
        this.contextMenu = new ContextMenu();
        this.viewVouchersSelected = [];
        this.listTimeLine = this.utilsService.getCbbTimeLine();
        this.isViewFromRef = window.location.href.includes('/from-ref');
    }

    ngOnInit() {
        this.saBillDetail = [];
        this.checkModalVoucher = false;
        this.itemsPerPageVoucher = ITEMS_PER_PAGE;
        this.pageVoucher = 1;
        this.val = 1;
        this.warningEmployee = false;
        this.warningVatRate = 0;
        this.rsInwardOutward = {};
        this.mBDeposit = {};
        this.mCReceipt = {};
        this.saBill = new SaBill();
        this.saBill.saBillDetails = [];
        this.templates = [];
        this.isEdit = true;
        this.isEditInvoice = true;
        this.isEditKPXK = false;
        this.sAInvoice = {};
        this.sAInvoiceDetails = [];
        this.accountingObjects = [];
        this.isSaving = false;
        this.employee = {};
        this.option1 = false;
        this.option2 = false;
        this.viewSAQuote = [];
        this.viewSAOrder = [];
        this.viewPPInvoice = [];
        this.viewRSOutward = [];
        this.viewRSTranfer = [];
        this.phepTinhTyGia = '*';
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.account = account;
                this.getSessionData();
                this.sessionWork = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '1');
                this.isEditOWAmount = this.account.systemOption.some(x => x.code === NHAP_DON_GIA_VON && x.data === '0');
                this.checkBook = Number.parseInt(this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data);
                this.authoritiesNoMBook = this.account.systemOption.find(x => x.code === TCKHAC_SDSOQUANTRI && x.data === '1');
                this.hiddenVAT = this.account.organizationUnit.taxCalculationMethod === 1;
                this.defaultCareerGroupID = this.account.organizationUnit.careerGroupID;
                this.isRequiredInvoiceNo = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '0');
                this.checkSLT = this.account.systemOption.find(x => x.code === HH_XUATQUASLTON).data === '1';
                if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                    this.currencyCode = this.account.organizationUnit.currencyID;
                }
                if (data.sAInvoice && data.sAInvoice.id) {
                    this.sAInvoice = data.sAInvoice;
                    this.sAInvoice.date = this.sAInvoice.date != null ? moment(this.sAInvoice.date) : null;
                    this.sAInvoice.postedDate = this.sAInvoice.postedDate != null ? moment(this.sAInvoice.postedDate) : null;
                    this.sAInvoiceDetails = data.sAInvoice.sAInvoiceDetails.sort((n1, n2) => {
                        return n1.orderPriority - n2.orderPriority;
                    });
                    this.sAInvoiceDetails.forEach(detail => {
                        detail.expiryDate = detail.expiryDate != null ? moment(detail.expiryDate) : null;
                        if (detail.sAQuoteDetailID) {
                            this.createFrom = TypeID.BAO_GIA;
                        } else if (detail.sAOrderDetailID) {
                            this.createFrom = TypeID.DON_DAT_HANG;
                        } else if (detail.pPInvoiceDetailID) {
                            this.createFrom = TypeID.MUA_HANG;
                        } else if (detail.rSInwardOutwardDetailID) {
                            this.createFrom = TypeID.XUAT_KHO;
                        } else if (detail.rSTransferDetailID) {
                            this.createFrom = TypeID.CHUYEN_KHO;
                        }
                    });
                    this.checkData = true;
                    this.checkReadOnly = true;
                    this.isEdit = false;
                    this.isEditReasonFirst = false;
                    this.isEditReasonFirstBill = false;
                    this.isEditReasonFirstXK = false;
                    this.isEditReasonFirstPT = false;
                    this.isEditReasonFirstBC = false;
                    if (this.sAInvoice.invoiceForm === 0 && this.sAInvoice.invoiceNo && !this.isRequiredInvoiceNo) {
                        this.isEditInvoice = false;
                    }
                    this.sAInvoiceService.getRefVouchersBySAInvoiceID(data.sAInvoice.id).subscribe(ref => {
                        this.viewVouchersSelected = ref.body;
                        this.sAInvoiceDetails.forEach(detail => {
                            const sAOrder = this.viewVouchersSelected.find(x => x.refID2 === detail.sAOrderID);
                            if (sAOrder) {
                                detail.sAOrderNo = sAOrder.no;
                            }
                        });
                    });
                    if (this.rsDataSession && this.rsDataSession.isEdit) {
                        this.edit();
                    }
                } else {
                    this.checkData = false;
                    this.sAInvoice = {};
                    this.sAInvoiceDetails = [];
                    this.checkReadOnly = false;
                    this.isEditReasonFirst = true;
                    this.isEditReasonFirstBill = true;
                    this.isEditReasonFirstXK = true;
                    this.isEditReasonFirstPT = true;
                    this.isEditReasonFirstBC = true;
                    this.sAInvoice.date = this.utilsService.ngayHachToan(this.account);
                    this.sAInvoice.isDeliveryVoucher = this.account.systemOption.find(x => x.code === BH_KIEM_PX).data === '1';
                    this.option1 = this.sAInvoice.isDeliveryVoucher;
                    this.sAInvoice.isBill = this.account.systemOption.find(x => x.code === BH_KIEM_HD).data === '1';
                    this.option2 = this.sAInvoice.isBill;
                    this.sAInvoice.postedDate = this.sAInvoice.date;
                    this.sAInvoice.exported = false;
                    this.sAInvoice.typeID = TypeID.BAN_HANG_CHUA_THU_TIEN;
                    this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHCTT']).subscribe((res: any) => {
                        this.sAInvoice.reason = res['ebwebApp.sAInvoice.reasonSA.reasonBHCTT'];
                    });
                    this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonPKX']).subscribe((res: any) => {
                        this.rsInwardOutward.reason = res['ebwebApp.sAInvoice.reasonSA.reasonPKX'];
                    });
                    this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHCTT']).subscribe((res: any) => {
                        this.saBill.reason = res['ebwebApp.sAInvoice.reasonSA.reasonBHCTT'];
                    });
                    // this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHTTN']).subscribe((res: any) => {
                    //     this.sAInvoice.reason = res['ebwebApp.sAInvoice.reasonSA.reasonBHTTN'];
                    // });
                    // this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonPKX']).subscribe((res: any) => {
                    //     this.rsInwardOutward.reason = res['ebwebApp.sAInvoice.reasonSA.reasonPKX'];
                    // });
                    // this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHTTN']).subscribe((res: any) => {
                    //     this.saBill.reason = res['ebwebApp.sAInvoice.reasonSA.reasonBHTTN'];
                    // });
                    // this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonPT']).subscribe((res: any) => {
                    //     this.mCReceipt.reason = res['ebwebApp.sAInvoice.reasonSA.reasonPT'];
                    // });
                    // this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBC']).subscribe((res: any) => {
                    //     this.mBDeposit.reason = res['ebwebApp.sAInvoice.reasonSA.reasonBC'];
                    // });
                    this.rsInwardOutward.date = this.sAInvoice.date;
                    this.rsInwardOutward.postedDate = this.sAInvoice.date;
                    this.sAInvoice.currencyID = this.currencyCode;
                    if (!this.authoritiesNoMBook) {
                        this.sAInvoice.typeLedger = this.checkBook;
                    } else {
                        this.sAInvoice.typeLedger = 2;
                    }
                }
                this.getData();
                this.copy();
            });
        });
        this.afterDeleteOrAddRow();
        this.afterSelectedIWVoucher();
        this.registerAddNewRow();
        this.registerDeleteRow();
        this.registerCopyRow();
        this.registerSelectMaterialGoodsSpecification();
        this.registerMaterialGoodsSpecifications();
        /*Xuất kho by Huypq*/
        this.isViewRSInWardOutward = window.location.href.includes('/edit-rs-inward-outward');
        if (this.isViewRSInWardOutward) {
            this.getSessionDataRS();
        }
        /*Xuất kho by Huypq*/

        this.registerRef();
        /*from EInvoice*/
        this.isViewFromEInvoice = window.location.href.includes('/from-einvoice');
        /*from EInvoice*/
        /*Phiếu thu Add by Hautv*/
        this.fromMCReceipt = this.activatedRoute.snapshot.routeConfig.path.includes('from-mc-receipt');
        if (this.fromMCReceipt) {
            this.searchVoucherMCReceipt = JSON.parse(sessionStorage.getItem('searchVoucherMCReceipt'));
            this.mCReceiptID = this.activatedRoute.snapshot.paramMap.get('mCReceiptID');
            // this.isEdit
            this.utilsService
                .getIndexRow({
                    id: this.mCReceiptID,
                    isNext: true,
                    typeID: this.TYPE_MC_RECEIPT,
                    searchVoucher: this.searchVoucherMCReceipt === undefined ? null : JSON.stringify(this.searchVoucherMCReceipt)
                })
                .subscribe(
                    (res: HttpResponse<any[]>) => {
                        this.rowNumberMCReceipt = res.body[0];
                        if (res.body.length === 1) {
                            this.countMCReceipt = 1;
                        } else {
                            this.countMCReceipt = res.body[1];
                        }
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
        /*Phiếu thu Add by Hautv*/
        /*Báo có*/
        this.fromMBDeposit = this.activatedRoute.snapshot.routeConfig.path.includes('from-mb-deposit');
        if (this.fromMBDeposit) {
            this.searchVoucherMBDeposit = JSON.parse(sessionStorage.getItem('dataSearchMBDeposit'));
            this.mBDepositID = this.activatedRoute.snapshot.paramMap.get('mBDepositID');
            // this.isEdit
            this.utilsService
                .getIndexRow({
                    id: this.mBDepositID,
                    isNext: true,
                    typeID: this.TYPE_MB_DEPOSIT,
                    searchVoucher: this.searchVoucherMBDeposit === undefined ? null : JSON.stringify(this.searchVoucherMBDeposit)
                })
                .subscribe(
                    (res: HttpResponse<any[]>) => {
                        this.rowNumberMBDeposit = res.body[0];
                        if (res.body.length === 1) {
                            this.countMBDeposit = 1;
                        } else {
                            this.countMBDeposit = res.body[1];
                        }
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
        /*Báo có*/
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
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuBanHang_Them])
    saveAndNew() {
        event.preventDefault();
        if (this.isEdit && !this.utilsService.isShowPopup) {
            this.mgForPPOderTextCode = this.utilsService.checkQuantityExistsTest(this.materialGoodss, this.sAInvoiceDetails);
            if (!this.checkSLT && this.mgForPPOderTextCode.quantityExists && this.option1) {
                this.sAInvoice.checkRecord = false;
                this.checkOpenSave = true;
                this.modalRef = this.modalService.open(this.contentCheckSave, { backdrop: 'static' });
                return;
            }
            if (this.mgForPPOderTextCode.minimumStock && this.option1) {
                this.checkOpenSave = false;
                this.sAInvoice.checkRecord = true;
                this.modalRef = this.modalService.open(this.contentCheckSave, { backdrop: 'static' });
                return;
            } else {
                this.sAInvoice.checkRecord = true;
                this.saveAndNewCheckEmployee();
            }
        }
    }

    saveAndNewCheckEmployee() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.isEdit) {
            if (this.account.systemOption.some(x => x.code === CANH_BAO_NHAN_VIEN && x.data === '1') && !this.sAInvoice.employeeID) {
                this.warningEmployee = true;
            }
            if (this.sAInvoiceDetails.filter(x => !x.vATRate && x.vATAmountOriginal > 0).length > 0) {
                this.warningVatRate = 1;
            }
            if (this.sAInvoiceDetails.filter(x => x.vATRate !== 1 && x.vATRate !== 2 && x.vATAmountOriginal > 0).length > 0) {
                this.warningVatRate = 2;
            }
            if (this.warningEmployee || this.warningVatRate === 1 || this.warningVatRate === 2) {
                this.isSaveAndAdd = true;
                this.modalRef = this.modalService.open(this.contentSave, { backdrop: 'static' });
            } else {
                this.saveAll(false);
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuBanHang_Them, ROLE.ChungTuBanHang_Sua])
    save(isSave = false) {
        event.preventDefault();
        if (this.isEdit && !this.utilsService.isShowPopup) {
            this.utilsService.checkQuantityExistsTest1(this.sAInvoiceDetails, this.account, this.sAInvoice.postedDate).then(data => {
                if (data) {
                    this.mgForPPOderTextCode = data;
                    if (!this.checkSLT && this.mgForPPOderTextCode && this.mgForPPOderTextCode.quantityExists && this.option1) {
                        this.checkOpenSave = true;
                        this.sAInvoice.checkRecord = false;
                        this.modalRef = this.modalService.open(this.contentCheckSave, { backdrop: 'static' });
                        return;
                    }
                    if (this.mgForPPOderTextCode && this.mgForPPOderTextCode.minimumStock && this.option1) {
                        this.checkOpenSave = false;
                        this.sAInvoice.checkRecord = true;
                        this.modalRef = this.modalService.open(this.contentCheckSave, { backdrop: 'static' });
                        return;
                    } else {
                        this.sAInvoice.checkRecord = true;
                        this.saveCheckEmployee();
                    }
                }
            });
        }
    }

    saveCheckEmployee() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.isEdit) {
            if (this.account.systemOption.some(x => x.code === CANH_BAO_NHAN_VIEN && x.data === '1') && !this.sAInvoice.employeeID) {
                this.warningEmployee = true;
            }
            if (this.sAInvoiceDetails.filter(x => !x.vATRate && x.vATRate !== 0 && x.vATAmountOriginal > 0).length > 0) {
                this.warningVatRate = 1;
            } else if (this.sAInvoiceDetails.filter(x => x.vATRate !== 1 && x.vATRate !== 2 && x.vATAmountOriginal > 0).length > 0) {
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

    saveAll(check?: Boolean) {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.warningEmployee = false;
        this.warningVatRate = 0;
        if (this.validatePurchaseGiveBack()) {
            this.isSaving = true;
            if (this.sAInvoiceDetails.length > 0) {
                this.changeTypeLedger();
                this.sAInvoice.sAInvoiceDetails = this.sAInvoiceDetails;
                this.sAInvoiceDetails.forEach(detail => {
                    detail.expiryDate = detail.expiryDate ? detail.expiryDate.format(DATE_FORMAT) : null;
                });
                this.sAInvoice.viewVouchers = this.viewVouchersSelected;
                this.sAInvoice.isBill = this.option2;
                this.sAInvoice.isDeliveryVoucher = this.option1;
                if (this.option1) {
                    this.convertRSOutWard(this.sAInvoice);
                    this.sAInvoice.rsInwardOutward = this.rsInwardOutward;
                }
                if (this.option2) {
                    this.convertSaBill(this.sAInvoice);
                    this.saBill.saBillDetails = [];
                    for (let i = 0; i < this.sAInvoiceDetails.length; i++) {
                        this.saBillDTL = {};
                        this.convertSaBillDetail(this.saBillDTL, this.sAInvoiceDetails[i]);
                        this.saBillDTL.orderPriority = i;
                        this.saBill.saBillDetails.push(this.saBillDTL);
                    }
                    this.sAInvoice.saBill = this.saBill;
                }
                if (this.sAInvoice.typeID === TypeID.BAN_HANG_THU_TIEN_NGAY_TM) {
                    this.convertMCReceipt(this.sAInvoice);
                    this.sAInvoice.mcReceipt = this.mCReceipt;
                } else if (this.sAInvoice.typeID === TypeID.BAN_HANG_THU_TIEN_NGAY_CK) {
                    this.convertMBDeposit(this.sAInvoice);
                    this.sAInvoice.mbDeposit = this.mBDeposit;
                }
                if (this.sAInvoice.id) {
                    this.subscribeToSaveResponse(this.sAInvoiceService.update(this.sAInvoice), check);
                } else {
                    this.sAInvoice.recorded = false;
                    this.subscribeToSaveResponse(this.sAInvoiceService.create(this.sAInvoice), check);
                }
            } else {
                this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.notPPDiscountReturn'));
            }
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>, check) {
        result.subscribe((res: HttpResponse<any>) => {
            for (let i = 0; i < this.sAInvoiceDetails.length; i++) {
                this.sAInvoiceDetails[i].expiryDate =
                    res.body.saInvoice.sAInvoiceDetails[i].expiryDate != null
                        ? moment(res.body.saInvoice.sAInvoiceDetails[i].expiryDate)
                        : null;
            }
            if (res.body.status === 0 || res.body.status === 7 || res.body.status === 8 || res.body.status === 9) {
                this.sAInvoice = res.body.saInvoice;
                this.sAInvoice.date = this.sAInvoice.date != null ? moment(this.sAInvoice.date) : null;
                this.sAInvoice.postedDate = this.sAInvoice.postedDate != null ? moment(this.sAInvoice.postedDate) : null;
                this.sAInvoice.invoiceDate = this.sAInvoice.date != null ? moment(this.sAInvoice.invoiceDate) : null;
                this.isCloseAll = true;
                this.toasService.success(
                    this.translateService.instant('ebwebApp.mCReceipt.home.saveSuccess'),
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
                if (res.body.status === 7) {
                    this.toasService.error(
                        this.translateService.instant('global.data.recordFailed'),
                        this.translateService.instant('ebwebApp.mBDeposit.message')
                    );
                } else if (res.body.status === 8) {
                    this.toasService.error(
                        this.translateService.instant('global.messages.error.checkTonSoLuong'),
                        this.translateService.instant('ebwebApp.mCReceipt.error.error')
                    );
                } else if (res.body.status === 9) {
                    this.toasService.error(
                        this.translateService.instant('global.messages.error.checkTonQuy'),
                        this.translateService.instant('ebwebApp.mCReceipt.error.error')
                    );
                }
                if (this.sAInvoice.recorded) {
                    this.materialGoodsService.queryForComboboxGood().subscribe((mate: HttpResponse<IMaterialGoods[]>) => {
                        this.materialGoodss = mate.body;
                    });
                }
                if (check) {
                    this.isEdit = false;
                    this.router.navigate(['chung-tu-ban-hang/', res.body.saInvoice.id, 'edit']);
                } else {
                    this.isEdit = true;
                    this.router.navigate(['chung-tu-ban-hang/', res.body.saInvoice.id, 'edit']).then(() => {
                        this.router.navigate(['chung-tu-ban-hang/', 'new']);
                    });
                }
            } else if (res.body.status === 1) {
                this.isCloseAll = false;
                this.isSaving = false;
                this.toasService.error(
                    this.translateService.instant('global.data.noVocherAlreadyExist'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
            } else if (res.body.status === 2) {
                this.isCloseAll = false;
                this.isSaving = false;
                this.toasService.error(
                    this.translateService.instant('global.data.noOutwardAlreadyExist'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
            } else if (res.body.status === 3) {
                this.isCloseAll = false;
                this.isSaving = false;
                this.toasService.error(
                    this.translateService.instant('global.data.noMCRecieptAlreadyExist'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
            } else if (res.body.status === 4) {
                this.isCloseAll = false;
                this.isSaving = false;
                this.toasService.error(
                    this.translateService.instant('global.data.noMBDepositAlreadyExist'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
            } else if (res.body.status === 5) {
                this.isCloseAll = false;
                this.isSaving = false;
                this.toasService.error(
                    this.translateService.instant('global.data.invoiceNoAlreadyExist'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
            } else if (res.body.status === 6) {
                this.isCloseAll = false;
                this.isSaving = false;
                this.toasService.error(
                    this.translateService.instant('global.data.invoiceNoNotRelease'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
            } else if (res.body.status === 10) {
                this.isCloseAll = false;
                this.isSaving = false;
                this.toasService.error(
                    this.translateService.instant('ebwebApp.saBill.error.errorInvoiceDate'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
            }
        });
    }

    private onSaveError() {
        this.isCloseAll = false;
        this.isSaving = false;
    }

    checkInvoiceNo() {
        if (this.sAInvoice.invoiceNo) {
            this.sAInvoice.invoiceNo = this.utilsService.pad(this.sAInvoice.invoiceNo, 7);
        }
    }

    private onError(message: string) {}

    selectAutoPrinciple() {
        if (this.autoPrinciple !== null && this.autoPrinciple !== undefined) {
            if (this.val === 5) {
                this.mBDeposit.reason = this.autoPrinciple.autoPrincipleName;
            } else {
                this.mCReceipt.reason = this.autoPrinciple.autoPrincipleName;
            }
            for (const dt of this.sAInvoiceDetails) {
                dt.debitAccount = this.autoPrinciple.debitAccount;
                dt.creditAccount = this.autoPrinciple.creditAccount;
                dt.description = this.autoPrinciple.autoPrincipleName;
            }
        }
    }

    selectAccountingObjects() {
        if (this.sAInvoice.accountingObject) {
            // phiếu xuất
            this.sAInvoiceDetails.forEach(sAInvoiceDetail => {
                if (!sAInvoiceDetail.accountingObject || this.sAInvoice.accountingObjectID === sAInvoiceDetail.accountingObject.id) {
                    sAInvoiceDetail.accountingObject = this.sAInvoice.accountingObject;
                    sAInvoiceDetail.accountingObjectID = this.sAInvoice.accountingObject.id;
                }
            });

            this.rsInwardOutward.accountingObjectID = this.sAInvoice.accountingObject.id;
            this.rsInwardOutward.accountingObjectName = this.sAInvoice.accountingObject.accountingObjectName;
            this.rsInwardOutward.accountingObjectAddress = this.sAInvoice.accountingObject.accountingObjectAddress;
            this.rsInwardOutward.contactName = this.sAInvoice.accountingObject.contactName;
            // hoa don
            this.saBill.accountingObject = this.sAInvoice.accountingObject;
            this.saBill.accountingObjectName = this.sAInvoice.accountingObject.accountingObjectName;
            this.saBill.accountingObjectAddress = this.sAInvoice.accountingObject.accountingObjectAddress;
            this.saBill.companyTaxCode = this.sAInvoice.accountingObject.taxCode;
            this.saBill.contactName = this.sAInvoice.accountingObject.contactName;
            // chứng từ
            this.sAInvoice.accountingObjectID = this.sAInvoice.accountingObject.id;
            this.sAInvoice.accountingObjectName = this.sAInvoice.accountingObject.accountingObjectName;
            this.sAInvoice.accountingObjectAddress = this.sAInvoice.accountingObject.accountingObjectAddress;
            this.sAInvoice.companyTaxCode = this.sAInvoice.accountingObject.taxCode;
            this.sAInvoice.contactName = this.sAInvoice.accountingObject.contactName;
            if (this.isEditReasonFirst) {
                if (this.sAInvoice.typeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
                    this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHCTT1']).subscribe((res: any) => {
                        this.sAInvoice.reason =
                            res['ebwebApp.sAInvoice.reasonSA.reasonBHCTT1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                    });
                } else {
                    this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHTTN1']).subscribe((res: any) => {
                        this.sAInvoice.reason =
                            res['ebwebApp.sAInvoice.reasonSA.reasonBHTTN1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                    });
                }
            }
            if (this.isEditReasonFirstBill) {
                if (this.sAInvoice.typeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
                    this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHCTT1']).subscribe((res: any) => {
                        this.saBill.reason =
                            res['ebwebApp.sAInvoice.reasonSA.reasonBHCTT1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                    });
                } else {
                    this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHTTN1']).subscribe((res: any) => {
                        this.saBill.reason =
                            res['ebwebApp.sAInvoice.reasonSA.reasonBHTTN1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                    });
                }
            }
            if (this.isEditReasonFirstXK) {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonPKX1']).subscribe((res: any) => {
                    this.rsInwardOutward.reason =
                        res['ebwebApp.sAInvoice.reasonSA.reasonPKX1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                });
            }
            if (this.isEditReasonFirstPT && this.sAInvoice.typeID !== TypeID.BAN_HANG_CHUA_THU_TIEN) {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonPT1']).subscribe((res: any) => {
                    this.mCReceipt.reason =
                        res['ebwebApp.sAInvoice.reasonSA.reasonPT1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                });
            }
            if (this.isEditReasonFirstBC && this.sAInvoice.typeID !== TypeID.BAN_HANG_CHUA_THU_TIEN) {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBC1']).subscribe((res: any) => {
                    this.mBDeposit.reason =
                        res['ebwebApp.sAInvoice.reasonSA.reasonBC1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                });
            }
            this.accountingObjectBankAccountService
                .getByAccountingObjectById({
                    accountingObjectID: this.sAInvoice.accountingObject.id
                })
                .subscribe(ref => {
                    this.accountingObjectBankAccountList = ref.body;
                });
            // if (
            //     this.sAInvoiceDetails &&
            //     this.sAInvoiceDetails.length ===
            //         this.sAInvoiceDetails.filter(x => x.accountingObjectID === this.sAInvoiceDetails[0].accountingObjectID).length
            // ) {
            //     this.sAInvoiceDetails.forEach(sAInvoiceDetail => {
            //         sAInvoiceDetail.accountingObject = this.sAInvoice.accountingObject;
            //         sAInvoiceDetail.accountingObjectID = this.sAInvoice.accountingObject.id;
            //     });
            // }
        } else {
            this.rsInwardOutward.accountingObjectID = null;
            this.rsInwardOutward.accountingObjectName = null;
            this.rsInwardOutward.accountingObjectAddress = null;
            this.rsInwardOutward.contactName = null;
            // hoa don
            this.saBill.accountingObject = null;
            this.saBill.accountingObjectName = null;
            this.saBill.accountingObjectAddress = null;
            this.saBill.companyTaxCode = null;
            this.saBill.contactName = null;
            // chứng từ
            this.sAInvoice.accountingObjectID = null;
            this.sAInvoice.accountingObjectName = null;
            this.sAInvoice.accountingObjectAddress = null;
            this.sAInvoice.companyTaxCode = null;
            this.sAInvoice.contactName = null;

            if (this.sAInvoice.typeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHCTT']).subscribe((res: any) => {
                    this.sAInvoice.reason = res['ebwebApp.sAInvoice.reasonSA.reasonBHCTT'];
                });
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonPKX']).subscribe((res: any) => {
                    this.rsInwardOutward.reason = res['ebwebApp.sAInvoice.reasonSA.reasonPKX'];
                });
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHCTT']).subscribe((res: any) => {
                    this.saBill.reason = res['ebwebApp.sAInvoice.reasonSA.reasonBHCTT'];
                });
            } else {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHTTN']).subscribe((res: any) => {
                    this.sAInvoice.reason = res['ebwebApp.sAInvoice.reasonSA.reasonBHTTN'];
                });
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonPKX']).subscribe((res: any) => {
                    this.rsInwardOutward.reason = res['ebwebApp.sAInvoice.reasonSA.reasonPKX'];
                });
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHTTN']).subscribe((res: any) => {
                    this.saBill.reason = res['ebwebApp.sAInvoice.reasonSA.reasonBHTTN'];
                });
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonPT']).subscribe((res: any) => {
                    this.mCReceipt.reason = res['ebwebApp.sAInvoice.reasonSA.reasonPT'];
                });
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBC']).subscribe((res: any) => {
                    this.mBDeposit.reason = res['ebwebApp.sAInvoice.reasonSA.reasonBC'];
                });
            }
            this.accountingObjectBankAccountList = [];
            if (
                this.sAInvoiceDetails &&
                this.sAInvoiceDetails.length ===
                    this.sAInvoiceDetails.filter(x => x.accountingObjectID === this.sAInvoiceDetails[0].accountingObjectID).length
            ) {
                this.sAInvoiceDetails.forEach(sAInvoiceDetail => {
                    sAInvoiceDetail.accountingObject = null;
                    sAInvoiceDetail.accountingObjectID = null;
                });
            }
        }
    }

    changeAccountingName() {
        if (this.isEditReasonFirst) {
            if (this.sAInvoice.typeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHCTT1']).subscribe((res: any) => {
                    this.sAInvoice.reason =
                        res['ebwebApp.sAInvoice.reasonSA.reasonBHCTT1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                });
            } else {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHTTN1']).subscribe((res: any) => {
                    this.sAInvoice.reason =
                        res['ebwebApp.sAInvoice.reasonSA.reasonBHTTN1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                });
            }
        }
        if (this.isEditReasonFirstBill) {
            if (this.sAInvoice.typeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHCTT1']).subscribe((res: any) => {
                    this.saBill.reason =
                        res['ebwebApp.sAInvoice.reasonSA.reasonBHCTT1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                });
            } else {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHTTN1']).subscribe((res: any) => {
                    this.saBill.reason =
                        res['ebwebApp.sAInvoice.reasonSA.reasonBHTTN1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                });
            }
        }
        if (this.isEditReasonFirstXK) {
            this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonPKX1']).subscribe((res: any) => {
                this.rsInwardOutward.reason =
                    res['ebwebApp.sAInvoice.reasonSA.reasonPKX1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
            });
        }
        if (this.isEditReasonFirstPT && this.sAInvoice.typeID !== TypeID.BAN_HANG_CHUA_THU_TIEN) {
            this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonPT1']).subscribe((res: any) => {
                this.mCReceipt.reason =
                    res['ebwebApp.sAInvoice.reasonSA.reasonPT1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
            });
        }
        if (this.isEditReasonFirstBC && this.sAInvoice.typeID !== TypeID.BAN_HANG_CHUA_THU_TIEN) {
            this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBC1']).subscribe((res: any) => {
                this.mBDeposit.reason =
                    res['ebwebApp.sAInvoice.reasonSA.reasonBC1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
            });
        }
    }

    private validatePurchaseGiveBack() {
        if (!this.sAInvoice.date) {
            this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.date', 'ebwebApp.sAInvoice.error.error'));
            return false;
        }
        if (!this.sAInvoice.postedDate) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pPDiscountReturn.error.postedDate', 'ebwebApp.sAInvoice.error.error')
            );
            return false;
        }
        if (this.checkCloseBook(this.account, this.sAInvoice.postedDate)) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.lockBook.err.checkForSave'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.sAInvoice.currencyID) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.sAInvoice.error.nullCurrencyID', 'ebwebApp.sAInvoice.error.error')
            );
            return false;
        }
        if (this.sAInvoice.postedDate && this.sAInvoice.date && moment(this.sAInvoice.postedDate) < moment(this.sAInvoice.date)) {
            this.toasService.error(this.translateService.instant('ebwebApp.common.error.dateAndPostDate'));
            return false;
        }
        const errorData = this.sAInvoiceDetails.filter(item => !item.materialGoodsID);
        if (errorData && errorData.length > 0) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pPDiscountReturn.error.materialGoodsID', 'ebwebApp.sAInvoice.error.error')
            );
            return false;
        }
        const repositoryID = this.sAInvoiceDetails.filter(
            item =>
                item.materialGoods.materialGoodsType !== this.VAT_TU_DV &&
                item.materialGoods.materialGoodsType !== this.VAT_TU_KHAC &&
                this.sAInvoice.isDeliveryVoucher &&
                !item.repositoryID
        );
        if (repositoryID && repositoryID.length) {
            this.toasService.error(this.translateService.instant('ebwebApp.sAInvoice.error.repositoryID'));
            return false;
        }
        const creditAccount = this.sAInvoiceDetails.filter(item => !item.creditAccount);
        if (creditAccount && creditAccount.length) {
            this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.creditAccount'));
            return false;
        }
        const debitAccount = this.sAInvoiceDetails.filter(item => !item.debitAccount);
        if (debitAccount && debitAccount.length > 0) {
            this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.debitAccount'));
            return false;
        }
        const amountOriginal = this.sAInvoiceDetails.filter(
            item => item.amountOriginal === null && !isNaN(Number(item.amountOriginal.toString()))
        );
        if (amountOriginal && amountOriginal.length > 0) {
            this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.amountOriginal'));
            return false;
        }
        const amount = this.sAInvoiceDetails.filter(item => item.amount === null && !isNaN(Number(item.amount.toString())));
        if (amount && amount.length > 0) {
            this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.amount'));
            return false;
        }
        const VATAmount = this.sAInvoiceDetails.filter(item => item.vATAmount === null && !isNaN(Number(item.vATAmount.toString())));
        if (VATAmount && VATAmount.length > 0) {
            this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.VATAmount'));
            return false;
        }
        const VATAmountOriginal = this.sAInvoiceDetails.filter(
            item => item.vATAmountOriginal === null && !isNaN(Number(item.vATAmountOriginal.toString()))
        );
        if (VATAmountOriginal && VATAmountOriginal.length > 0) {
            this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.error.VATAmountOriginal'));
            return false;
        }
        if (this.sAInvoiceDetails.filter(x => x.discountRate > 100).length > 0) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'),
                this.translateService.instant('ebwebApp.mCReceipt.home.message')
            );
            return false;
        }
        const discountAccount = this.sAInvoiceDetails.filter(item => item.discountAmountOriginal > 0 && !item.discountAccount);
        if (discountAccount && discountAccount.length) {
            this.toasService.error(this.translateService.instant('ebwebApp.sAInvoice.error.discountAccount'));
            return false;
        }
        const vatAccount = this.sAInvoiceDetails.filter(item => item.vATAmountOriginal !== 0 && !item.vATAccount);
        if (vatAccount && vatAccount.length) {
            this.toasService.error(this.translateService.instant('ebwebApp.sAInvoice.error.vatAccount'));
            return false;
        }
        const deductionDebitAccount = this.sAInvoiceDetails.filter(item => item.vATAmountOriginal !== 0 && !item.deductionDebitAccount);
        if (deductionDebitAccount && deductionDebitAccount.length) {
            this.toasService.error(this.translateService.instant('ebwebApp.sAInvoice.error.deductionDebitAccount'));
            return false;
        }
        if (this.sAInvoice.exported) {
            const exportTaxAccount = this.sAInvoiceDetails.filter(item => item.exportTaxAmount !== 0 && !item.exportTaxAmountAccount);
            if (exportTaxAccount && exportTaxAccount.length) {
                this.toasService.error(this.translateService.instant('ebwebApp.sAInvoice.error.exportTaxAccount'));
                return false;
            }
            const exportTaxAccountCorresponding = this.sAInvoiceDetails.filter(
                item => item.exportTaxAmount !== 0 && !item.exportTaxAccountCorresponding
            );
            if (exportTaxAccountCorresponding && exportTaxAccountCorresponding.length) {
                this.toasService.error(this.translateService.instant('ebwebApp.sAInvoice.error.exportTaxAccountCorresponding'));
                return false;
            }
        }
        if (this.sAInvoice.isDeliveryVoucher) {
            const repositoryAccount = this.sAInvoiceDetails.filter(
                item =>
                    item.materialGoods.materialGoodsType !== this.VAT_TU_DV &&
                    item.materialGoods.materialGoodsType !== this.VAT_TU_KHAC &&
                    !item.repositoryAccount
            );
            if (repositoryAccount && repositoryAccount.length) {
                this.toasService.error(this.translateService.instant('ebwebApp.sAInvoice.error.repositoryAccount'));
                return false;
            }
            const costAccount = this.sAInvoiceDetails.filter(
                item =>
                    item.materialGoods.materialGoodsType !== this.VAT_TU_DV &&
                    item.materialGoods.materialGoodsType !== this.VAT_TU_KHAC &&
                    !item.costAccount
            );
            if (costAccount && costAccount.length) {
                this.toasService.error(this.translateService.instant('ebwebApp.sAInvoice.error.costAccount'));
                return false;
            }
        }
        if (this.sAInvoice.companyTaxCode && !this.utilsService.checkMST(this.sAInvoice.companyTaxCode)) {
            this.toasService.error(this.translateService.instant('ebwebApp.saBill.error.mst'));
            return false;
        }
        const checkAcc = this.utilsService.checkAccoutWithDetailType(
            this.debitAccountList,
            this.creditAccountList,
            this.sAInvoiceDetails,
            this.accountingObjects,
            this.costSets,
            this.emContractList,
            this.materialGoodss,
            this.bankAccountDetails,
            this.organizationUnits,
            this.expenseItems,
            this.budgetItems,
            this.statisticCodes,
            this.sAInvoice.typeID === TypeID.BAN_HANG_THU_TIEN_NGAY_CK ? this.mBDeposit : null
        );
        if (checkAcc) {
            this.toasService.error(checkAcc, this.translateService.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        if (this.option2) {
            if (!this.sAInvoice.invoiceTemplate) {
                this.toasService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceTemplate'));
                return false;
            }
            if (!this.sAInvoice.invoiceSeries) {
                this.toasService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceSeries'));
                return false;
            }
            if (this.checkRequiredInvoiceNo()) {
                this.toasService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceNo'));
                return false;
            }
            if (!this.sAInvoice.invoiceDate) {
                this.toasService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceDate'));
                return false;
            }
            if (this.sAInvoice.invoiceDate.isBefore(this.template.startUsing)) {
                this.toasService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceDate2'));
                return;
            }
            if (!this.sAInvoice.paymentMethod) {
                this.toasService.error(this.translateService.instant('ebwebApp.saBill.error.paymentMethod'));
                return false;
            }
        }
        if (!this.noBookVoucher) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pporder.error.required'),
                this.translateService.instant('ebwebApp.pPDiscountReturn.voucher')
            );
            return false;
        }
        if (this.sAInvoice.typeID === TypeID.BAN_HANG_THU_TIEN_NGAY_CK && !this.noMBDeposit) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pporder.error.required'),
                this.translateService.instant('ebwebApp.pPDiscountReturn.voucher')
            );
            return false;
        }
        if (this.sAInvoice.typeID === TypeID.BAN_HANG_THU_TIEN_NGAY_TM && !this.noMCReceipt) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pporder.error.required'),
                this.translateService.instant('ebwebApp.pPDiscountReturn.voucher')
            );
            return false;
        }
        if (!this.sAInvoice.exchangeRate) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pporder.error.required'),
                this.translateService.instant('ebwebApp.pPDiscountReturn.exchangeRate')
            );
            return false;
        }

        if (!this.sAInvoice.currencyID) {
            this.toasService.error(
                this.translateService.instant('ebwebApp.pporder.error.required'),
                this.translateService.instant('ebwebApp.currency.home.title')
            );
            return false;
        }

        if (!this.utilsService.checkNoBook(this.noBookVoucher, this.account)) {
            return false;
        }
        if (this.sAInvoice.typeID === TypeID.BAN_HANG_THU_TIEN_NGAY_CK && !this.utilsService.checkNoBook(this.noMBDeposit, this.account)) {
            return false;
        }
        if (this.sAInvoice.typeID === TypeID.BAN_HANG_THU_TIEN_NGAY_TM && !this.utilsService.checkNoBook(this.noMCReceipt, this.account)) {
            return false;
        }
        if (this.sAInvoice.isDeliveryVoucher && !this.utilsService.checkNoBook(this.noBookRSI, this.account)) {
            return false;
        }
        let materialGoodsSpecificationsError = '';
        this.sAInvoiceDetails.forEach(detail => {
            if (
                !this.sAInvoice.id &&
                detail.materialGoods.isFollow &&
                (!detail.materialGoodsSpecificationsLedgers || detail.materialGoodsSpecificationsLedgers.length === 0)
            ) {
                materialGoodsSpecificationsError = materialGoodsSpecificationsError + ', ' + detail.materialGoods.materialGoodsCode;
            }
        });
        if (materialGoodsSpecificationsError) {
            materialGoodsSpecificationsError = materialGoodsSpecificationsError.substring(2, materialGoodsSpecificationsError.length);
            this.toasService.error(
                'Hàng hóa ' +
                    materialGoodsSpecificationsError +
                    ' ' +
                    this.translateService.instant('ebwebApp.materialGoodsSpecifications.noMaterialGoodsSpecifications')
            );
            return false;
        }
        return true;
    }

    selectCurrencyObjects() {
        if (this.sAInvoice.currencyID && this.isEdit) {
            this.sAInvoice.exchangeRate = this.currencys.find(n => n.currencyCode === this.sAInvoice.currencyID).exchangeRate;
            this.phepTinhTyGia = this.currencys.find(n => n.currencyCode === this.sAInvoice.currencyID).formula;
            this.changeExchangeRate();
        }
    }

    selectChangePaymentClause() {
        if (this.paymentClause) {
            this.sAInvoice.paymentClauseID = this.paymentClause.id;
        } else {
            this.sAInvoice.paymentClauseID = '';
        }
    }

    changeIsAttachList() {
        if (!this.saBill.isAttachList) {
            this.saBill.listNo = '';
            this.saBill.listDate = null;
            this.saBill.listCommonNameInventory = '';
        }
    }

    setInvoiceDate() {
        if (this.saBill.refDateTime) {
            this.saBill.invoiceDate = this.saBill.refDateTime;
            this.sAInvoice.invoiceDate = this.saBill.refDateTime;
        }
    }

    addNewRow(isRightClick?) {
        if (this.isEdit) {
            let length = 0;
            if (isRightClick) {
                this.sAInvoiceDetails.splice(this.indexFocusDetailRow + 1, 0, {});
                length = this.indexFocusDetailRow + 2;
            } else {
                this.sAInvoiceDetails.push({
                    accountingObject: null,
                    accountingObjectID: null,
                    careerGroupID: null,
                    discountAccount: null,
                    costAccount: null,
                    repositoryAccount: null,
                    creditAccount: null,
                    debitAccount: null,
                    vATAccount: null,
                    exportTaxAmountAccount: null,
                    exportTaxAccountCorresponding: null,
                    deductionDebitAccount: null
                });
                length = this.sAInvoiceDetails.length;
            }
            if (this.sAInvoice.accountingObject) {
                this.sAInvoiceDetails[length - 1].accountingObject = this.sAInvoice.accountingObject;
                this.sAInvoiceDetails[length - 1].accountingObjectID = this.sAInvoice.accountingObject.id;
            }
            if (this.defaultCareerGroupID) {
                this.sAInvoiceDetails[length - 1].careerGroupID = this.defaultCareerGroupID;
            }
            this.sAInvoiceDetails[length - 1].amountOriginal = 0;
            this.sAInvoiceDetails[length - 1].amount = 0;
            this.sAInvoiceDetails[length - 1].discountRate = 0;
            this.sAInvoiceDetails[length - 1].discountAmount = 0;
            this.sAInvoiceDetails[length - 1].discountAmountOriginal = 0;
            this.sAInvoiceDetails[length - 1].exportTaxPrice = 0;
            this.sAInvoiceDetails[length - 1].exportTaxTaxRate = 0;
            this.sAInvoiceDetails[length - 1].exportTaxAmount = 0;
            this.sAInvoiceDetails[length - 1].vATAmountOriginal = 0;
            this.sAInvoiceDetails[length - 1].vATAmount = 0;
            this.sAInvoiceDetails[length - 1].oWPrice = 0;
            this.sAInvoiceDetails[length - 1].oWAmount = 0;
            if (this.discountAccountList.some(x => x.accountNumber === this.discountAccountDefault)) {
                this.sAInvoiceDetails[length - 1].discountAccount = this.discountAccountDefault;
            }
            if (this.costAccountList.some(x => x.accountNumber === this.costAccountDefault)) {
                this.sAInvoiceDetails[length - 1].costAccount = this.costAccountDefault;
            }
            if (this.repositoryAccountList.some(x => x.accountNumber === this.repositoryAccountDefault)) {
                this.sAInvoiceDetails[length - 1].repositoryAccount = this.repositoryAccountDefault;
            }
            if (this.creditAccountList.some(x => x.accountNumber === this.creditAccountDefault)) {
                this.sAInvoiceDetails[length - 1].creditAccount = this.creditAccountDefault;
            }
            if (this.debitAccountList.some(x => x.accountNumber === this.debitAccountDefault)) {
                this.sAInvoiceDetails[length - 1].debitAccount = this.debitAccountDefault;
                this.sAInvoiceDetails[length - 1].exportTaxAccountCorresponding = this.debitAccountDefault;
            }
            if (this.vatAccountList.some(x => x.accountNumber === this.vatAccountDefault)) {
                this.sAInvoiceDetails[length - 1].vATAccount = this.vatAccountDefault;
            }
            if (this.exportTaxAmountAccountList.some(x => x.accountNumber === this.exportTaxAmountAccountDefault)) {
                this.sAInvoiceDetails[length - 1].exportTaxAmountAccount = this.exportTaxAmountAccountDefault;
            }
            if (this.deductionDebitAccountList.some(x => x.accountNumber === this.deductionDebitAccountDefault)) {
                this.sAInvoiceDetails[length - 1].deductionDebitAccount = this.deductionDebitAccountDefault;
            }
            this.contextmenu.value = false;
            this.sumAllList();
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
                const idx: number = this.sAInvoiceDetails.length - 1;
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

    changeDebitAccount(detail: ISAInvoiceDetails) {
        if (!detail.deductionDebitAccount && !this.deductionDebitAccountDefault) {
            detail.deductionDebitAccount = detail.debitAccount;
        }
        if (!detail.exportTaxAccountCorresponding && this.sAInvoice.exported) {
            detail.exportTaxAccountCorresponding = detail.debitAccount;
        }
    }

    getListUnits() {
        this.unitService.convertRateForMaterialGoodsComboboxCustom().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
            this.sAInvoiceDetails[this.currentRow].units = this.units.filter(
                x => x.materialGoodsID === this.sAInvoiceDetails[this.currentRow].materialGoodsID
            );
            this.sAInvoiceDetails[this.currentRow].unit = this.sAInvoiceDetails[this.currentRow].units[0];
            this.sAInvoiceDetails[this.currentRow].unitID = this.sAInvoiceDetails[this.currentRow].units[0].id;
            this.sAInvoiceDetails[this.currentRow].mainUnit = this.sAInvoiceDetails[this.currentRow].units[0];
            this.sAInvoiceDetails[this.currentRow].mainUnitID = this.sAInvoiceDetails[this.currentRow].units[0].id;
            this.selectUnit(this.currentRow);
        });
    }

    selectChangeMaterialGoods(detail: ISAInvoiceDetails) {
        if (detail.materialGoods) {
            detail.materialGoodsID = detail.materialGoods.id;
            detail.description = detail.materialGoods.materialGoodsName;
            detail.vATDescription = detail.materialGoods.materialGoodsCode;
            if (detail.materialGoods.careerGroupID) {
                detail.careerGroupID = detail.materialGoods.careerGroupID;
            }
            detail.quantity = 0;
            detail.unitPrice = 0;
            detail.unitPriceOriginal = 0;
            this.changeUnitPriceOriginal(detail);
            if (this.repositories.some(x => x.id === detail.materialGoods.repositoryID)) {
                detail.repositoryID = detail.materialGoods.repositoryID;
            }
            if (this.creditAccountList.some(x => x.accountNumber === detail.materialGoods.revenueAccount)) {
                detail.creditAccount = detail.materialGoods.revenueAccount;
            }
            detail.repositoryAccount = detail.materialGoods.reponsitoryAccount;
            detail.description = detail.materialGoods.materialGoodsName;
            if (!this.hiddenVAT) {
                if (!this.sAInvoice.exported) {
                    detail.vATRate = detail.materialGoods.vatTaxRate;
                } else {
                    detail.vATRate = 0;
                }
                this.translateService.get('ebwebApp.saBill.vat').subscribe(res => {
                    detail.vATDescription = res + ' ' + detail.materialGoods.materialGoodsCode;
                });
            }
            detail.discountRate = detail.materialGoods.saleDiscountRate;
            detail.exportTaxTaxRate = detail.materialGoods.exportTaxRate;
            detail.unitPriceOriginals = [];
            if (detail.materialGoods.fixedSalePrice) {
                detail.unitPriceOriginal = detail.materialGoods.fixedSalePrice;
                detail.unitPrice =
                    this.phepTinhTyGia === '*'
                        ? detail.materialGoods.fixedSalePrice * this.sAInvoice.exchangeRate
                        : detail.materialGoods.fixedSalePrice / this.sAInvoice.exchangeRate;
                detail.unitPriceOriginals.push({ unitPriceOriginal: detail.materialGoods.fixedSalePrice });
                this.changeUnitPriceOriginal(detail);
            }
            if (detail.materialGoods.salePrice1) {
                detail.unitPriceOriginals.push({ unitPriceOriginal: detail.materialGoods.salePrice1 });
            }
            if (detail.materialGoods.salePrice2) {
                detail.unitPriceOriginals.push({ unitPriceOriginal: detail.materialGoods.salePrice2 });
            }
            if (detail.materialGoods.salePrice3) {
                detail.unitPriceOriginals.push({ unitPriceOriginal: detail.materialGoods.salePrice3 });
            }
            // đơn vị tính
            detail.unit = {};
            detail.unitID = null;
            detail.mainUnit = {};
            detail.mainUnitID = null;
            detail.units = this.units.filter(item => item.materialGoodsID === detail.materialGoodsID);
            if (detail.units && detail.units.length) {
                detail.unit = detail.units[0];
                detail.unitID = detail.units[0].id;
                detail.mainUnit = detail.units[0];
                detail.mainUnitID = detail.units[0].id;
                this.selectUnit(detail);
            } else {
                detail.mainConvertRate = 1;
                detail.formula = '*';
                detail.mainQuantity = detail.quantity;
                detail.mainUnitPrice = detail.unitPrice;
            }
            detail.lotNo = null;
            detail.expiryDate = null;
            this.repositoryLedgerService
                .getListLotNoByMaterialGoodsID({
                    materialGoodsID: detail.materialGoods.id
                })
                .subscribe(ref => {
                    detail.lotNos = ref.body;
                });
            if (
                this.account.systemOption.some(x => x.code === PP_TINH_GIA_XUAT_KHO && x.data === CALCULATE_OW.DICH_DANH_CODE) &&
                this.sAInvoice.isDeliveryVoucher
            ) {
                this.indexDetail = this.sAInvoiceDetails.indexOf(detail);
                this.modalRef = this.refModalService.open(
                    null,
                    EbSelectMaterialGoodsModalComponent,
                    null,
                    false,
                    this.sAInvoice.typeID,
                    null,
                    this.sAInvoice.currencyID,
                    detail.materialGoodsID ? detail.materialGoodsID : null,
                    this.sAInvoice.accountingObject ? this.sAInvoice.accountingObject.id : null
                );
            }
        }
    }

    onSelectRepository(detail: ISAInvoiceDetails) {
        if (detail.repositoryID) {
            detail.repositoryAccount = this.repositories.find(x => x.id === detail.repositoryID).defaultAccount;
        }
    }

    checkSaleDiscountPolicy(detail: ISAInvoiceDetails) {
        let goto = true;
        detail.saleDiscountPolicys.forEach(saleDiscountPolicy => {
            if (saleDiscountPolicy.quantityFrom <= detail.quantity && detail.quantity <= saleDiscountPolicy.quantityTo) {
                goto = false;
                if (saleDiscountPolicy.discountType === 0) {
                    detail.discountRate = saleDiscountPolicy.discountResult;
                    if (detail.amountOriginal && detail.discountRate) {
                        detail.discountAmountOriginal = detail.amountOriginal * detail.discountRate / 100;
                        if (this.sAInvoice.exchangeRate) {
                            detail.discountAmount =
                                this.phepTinhTyGia === '*'
                                    ? detail.discountAmountOriginal * this.sAInvoice.exchangeRate
                                    : detail.discountAmountOriginal / this.sAInvoice.exchangeRate;
                        }
                    } else {
                        detail.discountAmountOriginal = 0;
                        detail.discountAmount = 0;
                    }
                } else if (saleDiscountPolicy.discountType === 1) {
                    detail.discountAmountOriginal = saleDiscountPolicy.discountResult;
                    detail.discountAmount =
                        this.phepTinhTyGia === '*'
                            ? detail.discountAmountOriginal * this.sAInvoice.exchangeRate
                            : detail.discountAmountOriginal / this.sAInvoice.exchangeRate;
                    detail.discountRate = detail.discountAmountOriginal / detail.amountOriginal * 100;
                } else if (saleDiscountPolicy.discountType === 2) {
                    detail.discountAmountOriginal = saleDiscountPolicy.discountResult * detail.quantity;
                    detail.discountAmount =
                        this.phepTinhTyGia === '*'
                            ? detail.discountAmountOriginal * this.sAInvoice.exchangeRate
                            : detail.discountAmountOriginal / this.sAInvoice.exchangeRate;
                    detail.discountRate = detail.discountAmountOriginal / detail.amountOriginal * 100;
                }
            }
            if (goto) {
                detail.discountRate = detail.materialGoods.saleDiscountRate;
                this.changeDiscountRate(detail);
            }
        });
        if (this.sAInvoice.exported) {
            detail.exportTaxPrice = detail.amount - detail.discountAmount;
        }
        this.changeVatRate(detail);
    }

    sumAllList() {
        if (this.isEdit) {
            this.sAInvoice.totalAmount = 0;
            this.sAInvoice.totalAmountOriginal = 0;
            this.sAInvoice.totalVATAmount = 0;
            this.sAInvoice.totalVATAmountOriginal = 0;
            this.sAInvoice.totalDiscountAmount = 0;
            this.sAInvoice.totalDiscountAmountOriginal = 0;
            this.sAInvoice.totalCapitalAmount = 0;
            this.sAInvoiceDetails.forEach(item => {
                if (item.amount) {
                    this.sAInvoice.totalAmount += item.amount;
                }
                if (item.amountOriginal) {
                    this.sAInvoice.totalAmountOriginal += item.amountOriginal;
                }
                if (item.discountAmount) {
                    this.sAInvoice.totalDiscountAmount += item.discountAmount;
                }
                if (item.discountAmountOriginal) {
                    this.sAInvoice.totalDiscountAmountOriginal += item.discountAmountOriginal;
                }
                if (item.vATAmount) {
                    this.sAInvoice.totalVATAmount += item.vATAmount;
                }
                if (item.vATAmountOriginal) {
                    this.sAInvoice.totalVATAmountOriginal += item.vATAmountOriginal;
                }
                if (item.oWAmount) {
                    this.sAInvoice.totalCapitalAmount += item.oWAmount;
                }
            });
        }
    }

    changeMainConvertRate(detail: ISAInvoiceDetails) {
        if (detail.mainConvertRate) {
            detail.mainQuantity =
                detail.formula === '*' ? detail.quantity * detail.mainConvertRate : detail.quantity / detail.mainConvertRate;
            detail.mainUnitPrice =
                detail.formula === '*' ? detail.unitPrice / detail.mainConvertRate : detail.unitPrice * detail.mainConvertRate;
        }
    }

    changeMainQuantity(detail: ISAInvoiceDetails) {
        if (detail.mainQuantity) {
            detail.quantity =
                detail.formula === '*' ? detail.mainQuantity / detail.mainConvertRate : detail.mainQuantity * detail.mainConvertRate;
            this.changeQuantity(detail);
        }
    }

    changeTypeSell() {
        if (this.sAInvoice.exported === true) {
            this.listVAT = [{ name: '0%', data: 0 }, { name: 'KCT', data: 3 }, { name: 'KTT', data: 4 }];
            this.sAInvoiceDetails.forEach(item => {
                item.exportTaxPrice = item.amount - item.discountAmount;
                if (item.vATRate === 1 || item.vATRate === 2) {
                    item.vATRate = null;
                    this.changeVatRate(item);
                }
                this.changeExportTaxTaxRate(item);
            });
        } else {
            this.sAInvoiceDetails.forEach(item => {
                item.exportTaxPrice = 0;
                this.changeExportTaxTaxRate(item);
            });
            this.listVAT = [
                { name: '0%', data: 0 },
                { name: '5%', data: 1 },
                { name: '10%', data: 2 },
                { name: 'KCT', data: 3 },
                { name: 'KTT', data: 4 }
            ];
        }
    }

    selectUnit(detail, isNotChangeUnitPrice?) {
        if (detail.unit) {
            detail.unitID = detail.unit.id;
            if (detail.mainUnitID === detail.unitID) {
                detail.mainConvertRate = 1;
                detail.formula = '*';
                detail.mainQuantity = detail.quantity;
                detail.mainUnitPrice = detail.unitPrice;
                detail.unitPriceOriginals = [];
                if (detail.materialGoods.fixedSalePrice) {
                    if (!isNotChangeUnitPrice) {
                        detail.unitPriceOriginal = detail.materialGoods.fixedSalePrice;
                        detail.unitPrice =
                            this.phepTinhTyGia === '*'
                                ? detail.materialGoods.fixedSalePrice * this.sAInvoice.exchangeRate
                                : detail.materialGoods.fixedSalePrice / this.sAInvoice.exchangeRate;
                        this.changeUnitPriceOriginal(detail);
                    }
                    detail.unitPriceOriginals.push({ unitPriceOriginal: detail.materialGoods.fixedSalePrice });
                }
                if (detail.materialGoods.salePrice1) {
                    detail.unitPriceOriginals.push({ unitPriceOriginal: detail.materialGoods.salePrice1 });
                }
                if (detail.materialGoods.salePrice2) {
                    detail.unitPriceOriginals.push({ unitPriceOriginal: detail.materialGoods.salePrice2 });
                }
                if (detail.materialGoods.salePrice3) {
                    detail.unitPriceOriginals.push({ unitPriceOriginal: detail.materialGoods.salePrice3 });
                }
            } else {
                detail.unitPriceOriginals = [];
                const materialGoodsConvertUnit = this.materialGoodsConvertUnits.find(
                    x => x.materialGoodsID === detail.materialGoods.id && x.unitID === detail.unit.id
                );
                if (materialGoodsConvertUnit) {
                    if (materialGoodsConvertUnit.fixedSalePrice) {
                        if (!isNotChangeUnitPrice) {
                            detail.unitPriceOriginal = materialGoodsConvertUnit.fixedSalePrice;
                            detail.unitPrice =
                                this.phepTinhTyGia === '*'
                                    ? materialGoodsConvertUnit.fixedSalePrice * this.sAInvoice.exchangeRate
                                    : materialGoodsConvertUnit.fixedSalePrice / this.sAInvoice.exchangeRate;
                            this.changeUnitPriceOriginal(detail);
                        }
                        detail.unitPriceOriginals.push({ unitPriceOriginal: materialGoodsConvertUnit.fixedSalePrice });
                    }
                    if (materialGoodsConvertUnit.salePrice1) {
                        detail.unitPriceOriginals.push({ unitPriceOriginal: materialGoodsConvertUnit.salePrice1 });
                    }
                    if (materialGoodsConvertUnit.salePrice2) {
                        detail.unitPriceOriginals.push({ unitPriceOriginal: materialGoodsConvertUnit.salePrice2 });
                    }
                    if (materialGoodsConvertUnit.salePrice3) {
                        detail.unitPriceOriginals.push({ unitPriceOriginal: materialGoodsConvertUnit.salePrice3 });
                    }
                }
                detail.mainConvertRate = detail.unit.convertRate;
                detail.formula = detail.unit.formula;
                detail.mainQuantity =
                    detail.formula === '*' ? detail.quantity * detail.mainConvertRate : detail.quantity / detail.mainConvertRate;
                detail.mainUnitPrice =
                    detail.formula === '*' ? detail.unitPrice / detail.mainConvertRate : detail.unitPrice * detail.mainConvertRate;
            }
        } else {
            detail.mainConvertRate = 1;
            detail.formula = '*';
            detail.mainQuantity = detail.quantity;
            detail.mainUnitPrice = detail.unitPrice;
        }
    }

    changeAmountOriginal(detail: ISAInvoiceDetails) {
        if (!detail.amountOriginal) {
            detail.amountOriginal = 0;
        }
        detail.amount =
            this.phepTinhTyGia === '*'
                ? detail.amountOriginal * this.sAInvoice.exchangeRate
                : detail.amountOriginal / this.sAInvoice.exchangeRate;
        if (detail.quantity && detail.unitPriceOriginal) {
            detail.unitPriceOriginal = detail.amountOriginal / detail.quantity;
            detail.unitPrice =
                this.phepTinhTyGia === '*'
                    ? detail.unitPriceOriginal * this.sAInvoice.exchangeRate
                    : detail.unitPriceOriginal / this.sAInvoice.exchangeRate;
            detail.mainUnitPrice =
                detail.formula === '*' ? detail.unitPrice / detail.mainConvertRate : detail.unitPrice * detail.mainConvertRate;
        }
        this.selectUnit(detail, true);
        detail.saleDiscountPolicys = this.saleDiscountPolicys.filter(x => x.materialGoodsID === detail.materialGoodsID);
        if (detail.saleDiscountPolicys && detail.saleDiscountPolicys.length > 0) {
            this.checkSaleDiscountPolicy(detail);
        } else {
            this.changeDiscountRate(detail);
        }
    }

    changeDiscountRate(detail: ISAInvoiceDetails) {
        if (detail.discountRate > 100) {
            this.toasService.error(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'));
        } else {
            if (detail.amountOriginal && detail.discountRate) {
                detail.discountAmountOriginal = this.utilsService.round(
                    detail.amountOriginal * detail.discountRate / 100,
                    this.account.systemOption,
                    this.sAInvoice.currencyID !== this.currencyCode ? 8 : 7
                );
                detail.discountAmount = this.utilsService.round(
                    this.phepTinhTyGia === '*'
                        ? detail.discountAmountOriginal * this.sAInvoice.exchangeRate
                        : detail.discountAmountOriginal / this.sAInvoice.exchangeRate,
                    this.account.systemOption,
                    7
                );
            } else {
                detail.discountAmountOriginal = 0;
                detail.discountAmount = 0;
            }
            if (this.sAInvoice.exported) {
                detail.exportTaxPrice = detail.amount - detail.discountAmount;
                detail.exportTaxAmount = this.utilsService.round(
                    detail.exportTaxTaxRate * detail.exportTaxPrice / 100,
                    this.account.systemOption,
                    7
                );
            }
            this.changeVatRate(detail);
        }
    }

    changeDiscountAmountOriginal(detail: ISAInvoiceDetails) {
        if (!detail.discountAmountOriginal) {
            detail.discountAmountOriginal = 0;
        }
        detail.discountRate = detail.discountAmountOriginal * 100 / detail.amountOriginal;
        detail.discountAmount = this.utilsService.round(
            this.phepTinhTyGia === '*'
                ? detail.discountAmountOriginal * this.sAInvoice.exchangeRate
                : detail.discountAmountOriginal / this.sAInvoice.exchangeRate,
            this.account.systemOption,
            7
        );
        this.changeVatRate(detail);
    }

    changeExportTaxTaxRate(detail: ISAInvoiceDetails) {
        if (!detail.exportTaxPrice) {
            detail.exportTaxPrice = 0;
        }
        detail.exportTaxAmount = this.utilsService.round(
            detail.exportTaxTaxRate * detail.exportTaxPrice / 100,
            this.account.systemOption,
            7
        );
    }

    changeVatRate(detail: ISAInvoiceDetails) {
        if (detail.vATRate !== null && detail.vATRate !== undefined && detail.amountOriginal) {
            if (detail.vATRate === 0 || detail.vATRate === 3 || detail.vATRate === 4) {
                detail.vATAmountOriginal = 0;
            } else if (detail.vATRate === 1) {
                detail.vATAmountOriginal = this.utilsService.round(
                    (detail.amountOriginal - detail.discountAmountOriginal) * 5 / 100,
                    this.account.systemOption,
                    this.sAInvoice.currencyID !== this.currencyCode ? 8 : 7
                );
            } else if (detail.vATRate === 2) {
                detail.vATAmountOriginal = this.utilsService.round(
                    (detail.amountOriginal - detail.discountAmountOriginal) * 10 / 100,
                    this.account.systemOption,
                    this.sAInvoice.currencyID !== this.currencyCode ? 8 : 7
                );
            }
            if (this.sAInvoice.exchangeRate) {
                detail.vATAmount = this.utilsService.round(
                    this.phepTinhTyGia === '*'
                        ? detail.vATAmountOriginal * this.sAInvoice.exchangeRate
                        : detail.vATAmountOriginal / this.sAInvoice.exchangeRate,
                    this.account.systemOption,
                    7
                );
            }
        } else {
            detail.vATAmountOriginal = 0;
            detail.vATAmount = 0;
        }
        this.sumAllList();
    }

    changeVATAmountOriginal(detail: ISAInvoiceDetails) {
        if (!detail.vATAmountOriginal) {
            detail.vATAmountOriginal = 0;
        }
        detail.vATAmount = this.utilsService.round(
            this.phepTinhTyGia === '*'
                ? detail.vATAmountOriginal * this.sAInvoice.exchangeRate
                : detail.vATAmountOriginal / this.sAInvoice.exchangeRate,
            this.account.systemOption,
            7
        );
        this.sumAllList();
    }

    changeQuantity(detail: ISAInvoiceDetails) {
        if (!detail.quantity) {
            detail.quantity = 0;
        }
        if (detail.mainConvertRate) {
            detail.mainQuantity =
                detail.formula === '*' ? detail.quantity * detail.mainConvertRate : detail.quantity / detail.mainConvertRate;
            detail.mainUnitPrice =
                detail.formula === '*' ? detail.unitPrice / detail.mainConvertRate : detail.unitPrice * detail.mainConvertRate;
        }
        if (!detail.unitPriceOriginal) {
            detail.unitPriceOriginal = 0;
        }
        detail.amountOriginal = this.utilsService.round(
            detail.quantity * detail.unitPriceOriginal,
            this.account.systemOption,
            this.sAInvoice.currencyID !== this.currencyCode ? 8 : 7
        );
        if (!detail.oWPrice) {
            detail.oWPrice = 0;
        }
        detail.oWAmount = this.utilsService.round(detail.quantity * detail.oWPrice, this.account.systemOption, 7);
        detail.amount = this.utilsService.round(
            this.phepTinhTyGia === '*'
                ? detail.amountOriginal * this.sAInvoice.exchangeRate
                : detail.amountOriginal / this.sAInvoice.exchangeRate,
            this.account.systemOption,
            7
        );
        detail.saleDiscountPolicys = this.saleDiscountPolicys.filter(x => x.materialGoodsID === detail.materialGoodsID);
        if (detail.saleDiscountPolicys && detail.saleDiscountPolicys.length > 0) {
            this.checkSaleDiscountPolicy(detail);
        } else {
            this.changeDiscountRate(detail);
        }
    }

    changeUnitPriceOriginal(detail: ISAInvoiceDetails) {
        // đơn giá quy đổi
        detail.unitPrice = this.utilsService.round(
            this.phepTinhTyGia === '*'
                ? detail.unitPriceOriginal * this.sAInvoice.exchangeRate
                : detail.unitPriceOriginal / this.sAInvoice.exchangeRate,
            this.account.systemOption,
            2
        );
        detail.mainUnitPrice =
            detail.formula === '*' ? detail.unitPrice / detail.mainConvertRate : detail.unitPrice * detail.mainConvertRate;
        this.changeQuantity(detail);
    }

    changeExchangeRate() {
        if (this.isEdit) {
            for (let i = 0; i < this.sAInvoiceDetails.length; i++) {
                this.sAInvoiceDetails[i].unitPrice = this.utilsService.round(
                    this.phepTinhTyGia === '*'
                        ? this.sAInvoiceDetails[i].unitPriceOriginal * this.sAInvoice.exchangeRate
                        : this.sAInvoiceDetails[i].unitPriceOriginal / this.sAInvoice.exchangeRate,
                    this.account.systemOption,
                    2
                );
                this.changeQuantity(this.sAInvoiceDetails[i]);
            }
        }
    }

    selectChangeAccountingObjectsHD(detail: ISAInvoiceDetails) {
        if (detail.accountingObject) {
            detail.accountingObjectID = detail.accountingObject.id;
        }
    }

    changeOWPrice(detail: ISAInvoiceDetails) {
        if (!detail.oWPrice) {
            detail.oWPrice = 0;
        }
        detail.oWAmount = this.utilsService.round(detail.oWPrice * detail.quantity, this.account.systemOption, 2);
    }

    changeOWAmount(detail: ISAInvoiceDetails) {
        if (!detail.oWAmount) {
            detail.oWAmount = 0;
        }
        detail.oWPrice = this.utilsService.round(detail.quantity ? detail.oWAmount / detail.quantity : 0, this.account.systemOption, 7);
    }

    changeLotNo(detail: ISAInvoiceDetails) {
        if (detail.lotNo) {
            const data = detail.lotNos.filter(x => x.lotNo === detail.lotNo);
            if (data && data.length > 0) {
                const selected = detail.lotNos.find(x => x.lotNo === detail.lotNo);
                detail.expiryDate = moment(selected.expiryDate);
            }
        }
    }

    changeReason() {
        if (this.isEditReasonFirst) {
            this.isEditReasonFirst = false;
        }
    }

    changeReasonXK() {
        if (this.isEditReasonFirstXK) {
            this.isEditReasonFirstXK = false;
        }
    }

    changeReasonPT() {
        if (this.isEditReasonFirstPT) {
            this.isEditReasonFirstPT = false;
        }
    }

    changeReasonBC() {
        if (this.isEditReasonFirstBC) {
            this.isEditReasonFirstBC = false;
        }
    }

    changeReasonBill() {
        if (this.isEditReasonFirstBill) {
            this.isEditReasonFirstBill = false;
        }
    }

    onrightClick($event, data, selectedData, isNew, isDelete, isCopy) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isCopy = isCopy;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.selectedData = selectedData;
    }

    disableContextMenu() {
        this.contextmenu.value = false;
        this.contextMenu.isShow = false;
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

    keyPress(detail, type?) {
        if (this.isEdit) {
            if (type || this.select) {
                this.viewVouchersSelected.splice(this.viewVouchersSelected.indexOf(detail), 1);
            } else {
                this.sAInvoiceDetails.splice(this.sAInvoiceDetails.indexOf(detail), 1);
                if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                    // vì còn trường hợp = 0
                    if (this.sAInvoiceDetails.length > 0) {
                        let row = 0;
                        if (this.indexFocusDetailRow > this.sAInvoiceDetails.length - 1) {
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

    copyRow(detail, isRigthClick?) {
        if (this.isEdit) {
            if (!this.getSelectionText() || isRigthClick) {
                const detailCopy: any = Object.assign({}, detail);
                detailCopy.id = null;
                this.sAInvoiceDetails.push(detailCopy);
                this.sumAllList();
                if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                    const id = this.idIndex;
                    const row = this.sAInvoiceDetails.length - 1;
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
    }

    actionFocus(indexCol, indexRow, id) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
        this.idIndex = id;
    }

    selectAccountingObjectBankAccount() {
        if (this.accountingObjectBankAccount) {
            this.saBill.accountingObjectBankAccount = this.accountingObjectBankAccount.bankAccount;
            this.saBill.accountingObjectBankName = this.accountingObjectBankAccount.bankName;
        }
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('dataSearchSAInvoice'));
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

    getData() {
        this.paymentMethod = [];
        this.translateService
            .get(['global.paymentMethod.cash', 'global.paymentMethod.transfer', 'global.paymentMethod.both'])
            .subscribe(res => {
                this.paymentMethod.push({ data: res['global.paymentMethod.cash'] });
                this.paymentMethod.push({ data: res['global.paymentMethod.transfer'] });
                this.paymentMethod.push({ data: res['global.paymentMethod.both'] });
            });
        if (!this.sAInvoice.paymentMethod && this.sAInvoice.isBill) {
            this.sAInvoice.paymentMethod = this.paymentMethod[0].data;
        }
        if (this.checkData && this.sAInvoice.exported) {
            this.listVAT = [{ name: '0%', data: 0 }, { name: 'KCT', data: 3 }, { name: 'KTT', data: 4 }];
        } else {
            this.listVAT = [
                { name: '0%', data: 0 },
                { name: '5%', data: 1 },
                { name: '10%', data: 2 },
                { name: 'KCT', data: 3 },
                { name: 'KTT', data: 4 }
            ];
        }
        // lấy thông tin hóa đơn
        this.iaPublishInvoiceDetailsService.getAllByCompany().subscribe(res => {
            this.templates = res.body;
            this.templates.forEach(item => {
                if (item.invoiceForm === 0) {
                    item.invoiceFormName = 'Hóa đơn điện tử';
                } else if (item.invoiceForm === 1) {
                    item.invoiceFormName = 'Hóa đơn đặt in';
                } else if (item.invoiceForm === 2) {
                    item.invoiceFormName = 'Hóa đơn tự in';
                }
            });
            if (this.checkData) {
                if (this.sAInvoice && this.sAInvoice.invoiceTypeID) {
                    this.template = this.templates.find(item => item.invoiceTypeID === this.sAInvoice.invoiceTypeID);
                }
            } else if (this.templates.length === 1 && this.sAInvoice.isBill && !this.template) {
                this.template = this.templates[0];
                this.selectTemplate();
            }
        });
        if (this.checkData) {
            if (this.sAInvoice.isBill) {
                this.option2 = true;
                this.saBillService.findById(this.sAInvoice.sAInvoiceDetails[0].sABillID).subscribe(ref => {
                    this.saBill = ref.body.saBill;
                    this.accountingObjectBankAccount = {
                        bankAccount: this.saBill.accountingObjectBankAccount,
                        bankName: this.saBill.accountingObjectBankName
                    };
                });
            } else {
                this.option2 = false;
            }
            if (this.sAInvoice.isDeliveryVoucher) {
                this.option1 = true;
                this.rsInwardOutwardService.find(this.sAInvoice.rsInwardOutwardID).subscribe(ref => {
                    this.rsInwardOutward = ref.body;
                    if (this.sessionWork) {
                        this.noBookRSI = this.rsInwardOutward.noMBook;
                    } else {
                        this.noBookRSI = this.rsInwardOutward.noFBook;
                    }
                });
            } else {
                this.option1 = false;
            }
            if (this.sAInvoice.typeID === 321) {
                this.mcReceiptService.find(this.sAInvoice.mcReceiptID).subscribe(ref => {
                    this.mCReceipt = ref.body;
                    if (this.sessionWork) {
                        this.noMCReceipt = this.mCReceipt.noMBook;
                    } else {
                        this.noMCReceipt = this.mCReceipt.noFBook;
                    }
                });
            }
            if (this.sAInvoice.typeID === 322) {
                this.mbDepositService.find(this.sAInvoice.mbDepositID).subscribe(ref => {
                    this.mBDeposit = ref.body;
                    if (this.sessionWork) {
                        this.noMBDeposit = this.mBDeposit.noMBook;
                    } else {
                        this.noMBDeposit = this.mBDeposit.noFBook;
                    }
                });
            }
        }
        const columnList = [
            { column: AccountType.TK_CO, ppType: false },
            { column: AccountType.TK_NO, ppType: false },
            { column: AccountType.TK_THUE_GTGT, ppType: false },
            { column: AccountType.TKDU_THUE_GTGT, ppType: false },
            { column: AccountType.TK_CHIET_KHAU, ppType: false },
            { column: AccountType.TK_DON_GIA, ppType: false },
            { column: AccountType.TK_KHO, ppType: false },
            { column: AccountType.TK_THUE_XK, ppType: false }
        ];
        const param = {
            typeID: this.sAInvoice.typeID,
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
            this.exportTaxAmountAccountList = res.body.exportTaxAccount;
            this.discountAccountDefault = res.body.discountAccountDefault;
            this.costAccountDefault = res.body.costAccountDefault;
            this.repositoryAccountDefault = res.body.repositoryAccountDefault;
            this.creditAccountDefault = res.body.creditAccountDefault;
            this.debitAccountDefault = res.body.debitAccountDefault;
            this.vatAccountDefault = res.body.vatAccountDefault;
            this.exportTaxAmountAccountDefault = res.body.exportTaxAccountDefault;
            this.deductionDebitAccountDefault = res.body.deductionDebitAccountDefault;
        });
        this.accountListService.getAccountLists().subscribe(res => {
            this.exportTaxAccountCorrespondingList = res.body;
        });
        // lấy mã hàng
        this.materialGoodsService.queryForComboboxGood().subscribe((res: HttpResponse<IMaterialGoods[]>) => {
            this.materialGoodss = res.body;
            if (this.checkData) {
                this.sAInvoiceDetails.forEach(item => {
                    item.materialGoods = this.materialGoodss.find(n => n.id === item.materialGoodsID);
                    if (item.mainUnitID === item.unitID) {
                        item.unitPriceOriginals = [];
                        if (item.materialGoods.fixedSalePrice) {
                            item.unitPriceOriginals.push({ unitPriceOriginal: item.materialGoods.fixedSalePrice });
                        }
                        if (item.materialGoods.salePrice1) {
                            item.unitPriceOriginals.push({ unitPriceOriginal: item.materialGoods.salePrice1 });
                        }
                        if (item.materialGoods.salePrice2) {
                            item.unitPriceOriginals.push({ unitPriceOriginal: item.materialGoods.salePrice2 });
                        }
                        if (item.materialGoods.salePrice3) {
                            item.unitPriceOriginals.push({ unitPriceOriginal: item.materialGoods.salePrice3 });
                        }
                    }
                });
            }
        });
        this.saleDiscountPolicyService.findAllSaleDiscountPolicyDTO().subscribe(res => {
            this.saleDiscountPolicys = res.body;
        });
        this.materialGoodsConvertUnitService
            .getAllMaterialGoodsConvertUnits()
            .subscribe((res: HttpResponse<IMaterialGoodsConvertUnit[]>) => {
                this.materialGoodsConvertUnits = res.body;
            });
        this.unitService.convertRateForMaterialGoodsComboboxCustom().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
            if (this.checkData) {
                this.sAInvoiceDetails.forEach(item => {
                    item.units = this.units.filter(n => n.materialGoodsID === item.materialGoodsID);
                    item.unit = item.units.find(i => i.id === item.unitID);
                    const mainUnitItem = item.units.find(i => i.id === item.mainUnitID);
                    if (mainUnitItem) {
                        item.mainUnitID = mainUnitItem.id;
                        item.mainUnit = mainUnitItem;
                    }
                    if (item.unitID !== item.mainUnitID) {
                        item.unitPriceOriginals = [];
                        const materialGoodsConvertUnit = this.materialGoodsConvertUnits.find(
                            x => x.materialGoodsID === item.materialGoodsID && x.unitID === item.unitID
                        );
                        if (materialGoodsConvertUnit) {
                            if (materialGoodsConvertUnit.fixedSalePrice) {
                                item.unitPriceOriginals.push({ unitPriceOriginal: materialGoodsConvertUnit.fixedSalePrice });
                            }
                            if (materialGoodsConvertUnit.salePrice1) {
                                item.unitPriceOriginals.push({ unitPriceOriginal: materialGoodsConvertUnit.salePrice1 });
                            }
                            if (materialGoodsConvertUnit.salePrice2) {
                                item.unitPriceOriginals.push({ unitPriceOriginal: materialGoodsConvertUnit.salePrice2 });
                            }
                            if (materialGoodsConvertUnit.salePrice3) {
                                item.unitPriceOriginals.push({ unitPriceOriginal: materialGoodsConvertUnit.salePrice3 });
                            }
                        }
                    }
                });
            }
        });
        // lấy kho
        this.repositoryService.getRepositoryCombobox().subscribe(res => {
            this.repositories = res.body;
        });
        // đối tượng
        this.accountingObjectService.getAllAccountingObjectDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjectAlls = res.body
                .filter(x => x.objectType === 1 || x.objectType === 2)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            this.employeeAlls = res.body
                .filter(n => n.isEmployee)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            if (this.checkData) {
                this.accountingObjects = this.accountingObjectAlls;
                this.employees = this.employeeAlls;
                this.sAInvoice.accountingObject = this.accountingObjects.find(n => n.id === this.sAInvoice.accountingObjectID);
                this.sAInvoiceDetails.forEach(item => {
                    item.accountingObject = this.accountingObjects.find(n => n.id === item.accountingObjectID);
                });
                this.employee = this.employees.find(n => n.id === this.sAInvoice.employeeID);
            } else {
                this.accountingObjects = this.accountingObjectAlls
                    .filter(x => x.isActive)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                this.employees = this.employeeAlls
                    .filter(x => x.isActive)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            }
        });
        this.bankAccountDetailsService.getBankAccountDetails().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
            this.bankAccountDetails = res.body;
        });
        this.careerGroupService.getCareerGroups().subscribe((res: HttpResponse<ICareerGroup[]>) => {
            this.careerGroups = res.body;
        });
        // điều khoản thanh toán
        this.paymentClauseService.getAllPaymentClauses().subscribe((res: HttpResponse<IPaymentClause[]>) => {
            this.paymentClauses = res.body;
            if (this.checkData) {
                this.paymentClause = this.paymentClauses.find(n => n.id === this.sAInvoice.paymentClauseID);
            }
        });
        // Khoản mục CP
        this.expenseItemService.getExpenseItems().subscribe(ref => {
            this.expenseItems = ref.body;
        });
        // cost set
        this.costSetService.getCostSets().subscribe(ref => {
            this.costSets = ref.body;
        });
        // cost set
        this.emContractService.getEMContracts().subscribe(ref => {
            this.emContractList = ref.body;
        });
        // mục thu/ chi
        this.budgetItemService.getBudgetItems().subscribe(ref => {
            this.budgetItems = ref.body;
        });
        // phòng ban
        this.organizationUnitService.getOrganizationUnits().subscribe(ref => {
            this.organizationUnits = ref.body.sort((a, b) => a.organizationUnitCode.localeCompare(b.organizationUnitCode));
        });
        // mã thống kê
        this.statisticsCodeService.getStatisticsCodes().subscribe(ref => {
            this.statisticCodes = ref.body;
        });
        this.autoPrincipleService.getAutoPrinciples().subscribe((res: HttpResponse<IAutoPrinciple[]>) => {
            this.autoPrinciples = res.body.filter(aPrinciple => aPrinciple.typeId === 160 || aPrinciple.typeId === 0).sort((n1, n2) => {
                if (n1.typeId > n2.typeId) {
                    return -1;
                }
                if (n1.typeId < n2.typeId) {
                    return 1;
                }
                return 0;
            });
        });
        this.getTypeLedger();
        this.getActiveCurrencies();
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.sAInvoiceDetails.length; i++) {
            total += this.sAInvoiceDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    private genCodeVoucher(currencyBook: number) {
        if (this.checkData) {
            if (this.sessionWork) {
                this.noBookVoucher = this.sAInvoice.noMBook;
            } else {
                this.noBookVoucher = this.sAInvoice.noFBook;
            }
            if (!this.sAInvoice.rsInwardOutwardID) {
                this.utilsService
                    .getGenCodeVoucher({
                        typeGroupID: GROUP_TYPEID.GROUP_RSOUTWARD,
                        displayOnBook: currencyBook
                    })
                    .subscribe((res: HttpResponse<string>) => {
                        this.noBookRSI = res.body;
                        this.rsInwardOutward.typeID = TypeID.XUAT_KHO_TU_BAN_HANG;
                    });
            }
            if (this.sAInvoice.typeID !== TypeID.BAN_HANG_THU_TIEN_NGAY_CK) {
                this.utilsService
                    .getGenCodeVoucher({
                        typeGroupID: GROUP_TYPEID.GROUP_MBDEPOSIT,
                        displayOnBook: currencyBook
                    })
                    .subscribe((res: HttpResponse<string>) => {
                        this.noMBDeposit = res.body;
                        this.mBDeposit.typeID = TypeID.NOP_TIEN_TU_BAN_HANG;
                    });
            }
            if (this.sAInvoice.typeID !== TypeID.BAN_HANG_THU_TIEN_NGAY_TM) {
                this.utilsService
                    .getGenCodeVoucher({
                        typeGroupID: GROUP_TYPEID.GROUP_MCRECEIPT,
                        displayOnBook: currencyBook
                    })
                    .subscribe((res: HttpResponse<string>) => {
                        this.noMCReceipt = res.body;
                        this.mCReceipt.typeID = TypeID.PHIEU_THU_TU_BAN_HANG;
                    });
            }
        } else {
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: GROUP_TYPEID.GROUP_SAINVOICE,
                    displayOnBook: currencyBook
                })
                .subscribe((res: HttpResponse<string>) => {
                    this.noBookVoucher = res.body;
                });
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: GROUP_TYPEID.GROUP_RSOUTWARD,
                    displayOnBook: currencyBook
                })
                .subscribe((res: HttpResponse<string>) => {
                    this.noBookRSI = res.body;
                    this.rsInwardOutward.typeID = TypeID.XUAT_KHO_TU_BAN_HANG;
                });
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: GROUP_TYPEID.GROUP_MCRECEIPT,
                    displayOnBook: currencyBook
                })
                .subscribe((res: HttpResponse<string>) => {
                    this.noMCReceipt = res.body;
                    this.mCReceipt.typeID = TypeID.PHIEU_THU_TU_BAN_HANG;
                });
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: GROUP_TYPEID.GROUP_MBDEPOSIT,
                    displayOnBook: currencyBook
                })
                .subscribe((res: HttpResponse<string>) => {
                    this.noMBDeposit = res.body;
                    this.mBDeposit.typeID = TypeID.NOP_TIEN_TU_BAN_HANG;
                });
        }
    }

    getActiveCurrencies() {
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencys = res.body;
            this.selectCurrencyObjects();
        });
    }

    /*Xuất kho by Huypq*/
    getSessionDataRS() {
        this.rsDataSession = JSON.parse(sessionStorage.getItem('xuatKhoDataSession'));
        sessionStorage.removeItem('xuatKhoDataSession');
        if (this.rsDataSession) {
            this.isFromOutWard = true;
            this.page = this.rsDataSession.page;
            this.rsTotalItems = this.rsDataSession.totalItems;
            this.rsRowNum = this.rsDataSession.rowNum;
            this.rsSearchData = this.rsDataSession.searchVoucher;
        } else {
            this.rsDataSession = null;
        }
    }

    /*Xuất kho by Huypq*/

    move(direction: number) {
        this.isCloseAll = true;
        /*Xuất kho by huypq*/
        if (this.isFromOutWard) {
            if ((direction === -1 && this.rsRowNum === 1) || (direction === 1 && this.rsRowNum === this.rsTotalItems)) {
                return;
            }
            this.rsRowNum += direction;
            const searchData = JSON.parse(this.rsSearchData);
            return this.rsInwardOutwardService
                .findReferenceTablesID({
                    fromDate: searchData.fromDate || '',
                    toDate: searchData.toDate || '',
                    noType: searchData.noType,
                    status: searchData.status,
                    accountingObject: searchData.accountingObject || '',
                    searchValue: searchData.searchValue || '',
                    rowNum: this.rsRowNum
                })
                .subscribe(
                    (res: HttpResponse<any>) => {
                        console.log(res.body);
                        const rsInwardOutward = res.body;
                        this.rsDataSession.rowNum = this.rsRowNum;
                        sessionStorage.setItem('xuatKhoDataSession', JSON.stringify(this.rsDataSession));
                        if (rsInwardOutward.typeID === this.XUAT_KHO) {
                            this.router.navigate(['/xuat-kho', rsInwardOutward.id, 'edit', this.rsRowNum]);
                        } else if (rsInwardOutward.typeID === this.XUAT_KHO_TU_BAN_HANG) {
                            this.router.navigate(['/chung-tu-ban-hang', rsInwardOutward.refID, 'edit-rs-inward-outward']);
                        } else if (rsInwardOutward.typeID === this.XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
                            this.router.navigate(['/hang-mua/tra-lai', rsInwardOutward.refID, 'edit-rs-inward-outward']);
                        } else if (rsInwardOutward.typeID === this.XUAT_KHO_TU_DIEU_CHINH) {
                            // TODO: Xuat kho tu dieu chinh
                        }
                    },
                    (res: HttpErrorResponse) => {
                        this.handleError(res);
                        this.getSessionData();
                    }
                );
            /*Xuất kho by huypq*/
        } else {
            if ((direction === -1 && this.rowNum === 1) || (direction === 1 && this.rowNum === this.totalItems)) {
                return;
            }
            this.rowNum += direction;
            const searchDataIndex = JSON.parse(this.searchData);
            this.searchDataSearch = {
                currency: searchDataIndex.currency ? searchDataIndex.currency : '',
                fromDate: searchDataIndex.fromDate ? searchDataIndex.fromDate : '',
                toDate: searchDataIndex.toDate ? searchDataIndex.toDate : '',
                status: searchDataIndex.status ? searchDataIndex.status : '',
                accountingObject: searchDataIndex.accountingObject ? searchDataIndex.accountingObject : '',
                searchValue: searchDataIndex.searchValue ? searchDataIndex.searchValue : '',
                typeId: searchDataIndex.typeId ? searchDataIndex.typeId : '',
                isBanHangChuaThuTien: searchDataIndex.isBanHangChuaThuTien ? searchDataIndex.isBanHangChuaThuTien : ''
            };
            // goi service get by row num
            return this.sAInvoiceService
                .findByRowNum({
                    accountingObjectID: searchDataIndex.accountingObject ? searchDataIndex.accountingObject : '',
                    fromDate: searchDataIndex.fromDate ? searchDataIndex.fromDate : '',
                    toDate: searchDataIndex.toDate ? searchDataIndex.toDate : '',
                    status: searchDataIndex.status ? searchDataIndex.status : '',
                    currencyID: searchDataIndex.currency ? searchDataIndex.currency : '',
                    keySearch: searchDataIndex.searchValue ? searchDataIndex.searchValue : '',
                    id: this.sAInvoice.id,
                    rowNum: this.rowNum,
                    typeId: searchDataIndex.typeId ? searchDataIndex.typeId : '',
                    isBanHangChuaThuTien: searchDataIndex.isBanHangChuaThuTien ? searchDataIndex.isBanHangChuaThuTien : ''
                })
                .subscribe(
                    (res: HttpResponse<ISAInvoice>) => {
                        this.sAInvoice = res.body;
                        this.getDetailByID(this.sAInvoice);
                        this.dataSession.page = this.page;
                        this.dataSession.rowNum = this.rowNum;
                        sessionStorage.setItem('dataSearchSAInvoice', JSON.stringify(this.dataSession));
                        this.getData();
                    },
                    (res: HttpErrorResponse) => {
                        this.handleError(res);
                        this.getSessionData();
                    }
                );
        }
    }

    getDetailByID(item: ISAInvoice) {
        this.sAInvoiceDetailsService.getDetailByID({ sAInvoiceID: item.id }).subscribe(ref => {
            this.sAInvoiceDetails = ref.body.sort((n1, n2) => {
                return n1.orderPriority - n2.orderPriority;
            });
        });
        this.sAInvoiceService.getRefVouchersBySAInvoiceID(item.id).subscribe(ref => {
            this.viewVouchersSelected = ref.body;
        });
    }

    handleError(err) {
        this.isSaving = false;
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuBanHang_Them])
    addNew($event) {
        if (!this.utilsService.isShowPopup) {
            this.isCloseAll = true;
            this.router.navigate(['chung-tu-ban-hang/new']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuBanHang_Sua])
    edit() {
        event.preventDefault();
        if (!this.isEdit && !this.sAInvoice.recorded && !this.utilsService.isShowPopup) {
            this.accountingObjects = this.accountingObjectAlls
                .filter(x => x.isActive)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            this.employees = this.employeeAlls
                .filter(x => x.isActive)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            this.isCloseAll = false;
            this.isEdit = true;
            // this.checkData = false;
            this.copy();
        }
        if (this.rsDataSession) {
            this.checkData = true;
            this.rsDataSession.isEdit = false;
            sessionStorage.setItem('xuatKhoDataSession', JSON.stringify(this.rsDataSession));
            this.copy();
        }
    }

    selectTemplate() {
        if (this.template) {
            this.sAInvoice.invoiceTypeID = this.template.invoiceTypeID;
            this.sAInvoice.invoiceTemplate = this.template.invoiceTemplate;
            this.sAInvoice.invoiceSeries = this.template.invoiceSeries;
            this.sAInvoice.invoiceForm = this.template.invoiceForm;
            if (!this.isRequiredInvoiceNo && this.sAInvoice.invoiceForm === 0) {
                this.sAInvoice.invoiceNo = null;
            }
        } else {
            this.sAInvoice.invoiceTypeID = null;
            this.sAInvoice.invoiceTemplate = null;
            this.sAInvoice.invoiceSeries = null;
            this.sAInvoice.invoiceForm = null;
        }
    }

    checkRequiredInvoiceNo() {
        return (
            (this.isRequiredInvoiceNo || (!this.isRequiredInvoiceNo && this.template && this.template.invoiceForm !== 0)) &&
            !this.sAInvoice.invoiceNo
        );
    }

    private convertRSOutWard(item: ISAInvoice) {
        if (this.sAInvoice.id) {
            this.rsInwardOutward.id = item.rsInwardOutwardID;
        }
        this.rsInwardOutward.typeID = TypeID.XUAT_KHO_TU_BAN_HANG;
        this.rsInwardOutward.typeLedger = item.typeLedger;
        this.rsInwardOutward.postedDate = item.postedDate;
        this.rsInwardOutward.date = item.date;
        this.rsInwardOutward.accountingObjectID = item.accountingObjectID;
        this.rsInwardOutward.employeeID = item.employeeID;
        this.rsInwardOutward.exported = item.exported;
        this.rsInwardOutward.transportMethodID = item.transportMethodID;
        this.rsInwardOutward.currencyID = item.currencyID;
        this.rsInwardOutward.exchangeRate = item.exchangeRate;
        this.rsInwardOutward.typeLedger = item.typeLedger;
        this.rsInwardOutward.accountingObjectName = item.accountingObjectName;
        this.rsInwardOutward.accountingObjectAddress = item.accountingObjectAddress;
        this.rsInwardOutward.contactName = item.contactName;
        this.rsInwardOutward.totalAmount = item.totalCapitalAmount;
        this.rsInwardOutward.totalAmountOriginal = item.totalCapitalAmount;
    }

    private convertMCReceipt(item: ISAInvoice) {
        if (this.sAInvoice.id) {
            this.mCReceipt.id = item.mcReceiptID;
        }
        this.mCReceipt.typeID = TypeID.PHIEU_THU_TU_BAN_HANG;
        this.mCReceipt.typeLedger = item.typeLedger;
        this.mCReceipt.postedDate = item.postedDate;
        this.mCReceipt.date = item.date;
        this.mCReceipt.accountingObject = item.accountingObject;
        this.mCReceipt.taxCode = item.companyTaxCode;
        this.mCReceipt.employeeID = item.employeeID;
        this.mCReceipt.exported = item.exported;
        this.mCReceipt.currencyID = item.currencyID;
        this.mCReceipt.exchangeRate = item.exchangeRate;
        this.mCReceipt.typeLedger = item.typeLedger;
        this.mCReceipt.accountingObjectName = item.accountingObjectName;
        this.mCReceipt.accountingObjectAddress = item.accountingObjectAddress;
        this.mCReceipt.totalAmount = item.totalAmount - item.totalDiscountAmount + item.totalVATAmount;
        this.mCReceipt.totalAmountOriginal = item.totalAmountOriginal - item.totalDiscountAmountOriginal + item.totalVATAmountOriginal;
        this.mCReceipt.recorded = false;
        this.mCReceipt.totalVATAmount = 0;
        this.mCReceipt.totalVATAmountOriginal = 0;
    }

    private convertMBDeposit(item: ISAInvoice) {
        if (this.sAInvoice.id) {
            this.mBDeposit.id = item.mbDepositID;
        }
        this.mBDeposit.typeID = TypeID.NOP_TIEN_TU_BAN_HANG;
        this.mBDeposit.typeLedger = item.typeLedger;
        this.mBDeposit.postedDate = item.postedDate;
        this.mBDeposit.date = item.date;
        this.mBDeposit.accountingObjectID = item.accountingObjectID;
        this.mBDeposit.employeeID = item.employeeID;
        this.mBDeposit.exported = item.exported;
        this.mBDeposit.currencyID = item.currencyID;
        this.mBDeposit.exchangeRate = item.exchangeRate;
        this.mBDeposit.typeLedger = item.typeLedger;
        this.mBDeposit.accountingObjectName = item.accountingObjectName;
        this.mBDeposit.accountingObjectAddress = item.accountingObjectAddress;
        this.mBDeposit.totalAmount = item.totalAmount - item.totalDiscountAmount + item.totalVATAmount;
        this.mBDeposit.totalAmountOriginal = item.totalAmountOriginal - item.totalDiscountAmountOriginal + item.totalVATAmountOriginal;
        this.mBDeposit.recorded = false;
        this.mBDeposit.totalVATAmount = 0;
        this.mBDeposit.totalVATAmountOriginal = 0;
    }

    private convertSaBill(item: ISAInvoice) {
        if (this.sAInvoice.id) {
            this.saBill.id = item.sAInvoiceDetails[0].sABillID;
        }
        this.saBill.typeID = TypeID.XUAT_HOA_DON_BH;
        this.saBill.currencyID = item.currencyID;
        this.saBill.exchangeRate = item.exchangeRate;
        this.saBill.typeLedger = item.typeLedger;
        this.saBill.reason = item.reason;
        this.saBill.accountingObjectName = item.accountingObjectName;
        this.saBill.accountingObjectAddress = item.accountingObjectAddress;
        this.saBill.companyTaxCode = item.companyTaxCode;
        this.saBill.contactName = item.contactName;
        this.saBill.isAttachList = item.isAttachList;
        this.saBill.listNo = item.listNo;
        this.saBill.listDate = item.listDate;
        this.saBill.listCommonNameInventory = item.listCommonNameInventory;
        this.saBill.totalAmount = item.totalAmount;
        this.saBill.totalAmountOriginal = item.totalAmountOriginal;
        this.saBill.totalDiscountAmount = item.totalDiscountAmount;
        this.saBill.totalDiscountAmountOriginal = item.totalDiscountAmountOriginal;
        this.saBill.totalVATAmount = item.totalVATAmount;
        this.saBill.totalVATAmountOriginal = item.totalVATAmountOriginal;
        this.saBill.invoiceForm = item.invoiceForm;
        this.saBill.invoiceTemplate = item.invoiceTemplate;
        this.saBill.invoiceTypeID = this.sAInvoice.invoiceTypeID;
        this.saBill.invoiceSeries = item.invoiceSeries;
        this.saBill.invoiceNo = item.invoiceNo;
        this.saBill.invoiceDate = item.invoiceDate;
        this.saBill.paymentMethod = item.paymentMethod;
        this.saBill.accountingObject = item.accountingObject;
    }

    private convertSaBillDetail(saBillDTL: ISaBillDetails, item: ISAInvoiceDetails) {
        if (this.sAInvoice.id) {
            saBillDTL.id = item.sABillDetailID;
            saBillDTL.saBillId = item.sABillID;
        }
        saBillDTL.materialGoods = item.materialGoods;
        saBillDTL.description = item.description;
        saBillDTL.debitAccount = item.debitAccount;
        saBillDTL.creditAccount = item.creditAccount;
        saBillDTL.unit = item.unit;
        saBillDTL.mainUnit = item.mainUnit;
        saBillDTL.unitID = item.unitID;
        saBillDTL.mainUnitID = item.mainUnitID;
        saBillDTL.quantity = item.quantity;
        saBillDTL.unitPrice = item.unitPrice;
        saBillDTL.unitPriceOriginal = item.unitPriceOriginal;
        saBillDTL.mainConvertRate = item.mainConvertRate;
        saBillDTL.mainQuantity = item.mainQuantity;
        saBillDTL.mainUnitPrice = item.mainUnitPrice;
        saBillDTL.formula = item.formula;
        saBillDTL.amount = item.amount;
        saBillDTL.amountOriginal = item.amountOriginal;
        saBillDTL.discountRate = item.discountRate;
        saBillDTL.discountAmount = item.discountAmount;
        saBillDTL.discountAmountOriginal = item.discountAmountOriginal;
        saBillDTL.vatRate = item.vATRate;
        saBillDTL.vatAmount = item.vATAmount;
        saBillDTL.vatAmountOriginal = item.vATAmountOriginal;
        saBillDTL.lotNo = item.lotNo;
        saBillDTL.expiryDate = item.expiryDate;
        saBillDTL.isPromotion = item.isPromotion;
        saBillDTL.orderPriority = item.orderPriority;
    }

    changeActive(number: number) {
        this.val = number;
    }

    changeTabXK() {
        if ((!this.option1 && this.val === 2) || (!this.option2 && this.val === 3)) {
            this.val = 1;
        }
        this.sAInvoice.isDeliveryVoucher = this.option1;
    }

    checkEditKPXK() {
        if (this.isEdit && this.checkData) {
            this.sAInvoiceService
                .checkRelateVoucher({
                    sAInvoice: this.sAInvoice.id,
                    isCheckKPXK: true
                })
                .subscribe(res => {
                    if (res.body) {
                        this.isEditKPXK = false;
                        this.toasService.error(
                            this.translateService.instant('global.messages.error.dontEditKPXK'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                    } else {
                        this.isEditKPXK = true;
                    }
                });
        } else {
            this.isEditKPXK = true;
        }
    }

    checkEditIsBill() {
        if (
            this.isEdit &&
            this.checkData &&
            this.sAInvoice.isBill &&
            this.sAInvoice.invoiceForm === 0 &&
            this.sAInvoice.invoiceNo &&
            !this.isRequiredInvoiceNo
        ) {
            this.toasService.error(
                this.translateService.instant('global.messages.error.dontEditIsBill'),
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
            return;
        }
    }

    changeTabHD() {
        if ((!this.option1 && this.val === 2) || (!this.option2 && this.val === 3)) {
            this.val = 1;
        }
        this.sAInvoice.isBill = this.option2;
        if (!this.option2) {
            this.sAInvoice.invoiceNo = null;
            this.sAInvoice.invoiceTypeID = null;
            this.sAInvoice.invoiceTemplate = null;
            this.sAInvoice.invoiceForm = null;
            this.sAInvoice.invoiceSeries = null;
            this.sAInvoice.invoiceDate = null;
            this.template = null;
        } else {
            if (!this.sAInvoice.paymentMethod && this.sAInvoice.isBill) {
                this.sAInvoice.paymentMethod = this.paymentMethod[0].data;
            }
            if (this.templates.length === 1 && this.sAInvoice.isBill && !this.template) {
                this.template = this.templates[0];
                this.selectTemplate();
            }
        }
    }

    changeDate() {
        this.sAInvoice.postedDate = this.sAInvoice.date;
    }

    changeTypePayment() {
        this.val = 1;
        if (this.sAInvoice.typeID === TypeID.BAN_HANG_THU_TIEN_NGAY_TM) {
            this.val = 4;
        } else if (this.sAInvoice.typeID === TypeID.BAN_HANG_THU_TIEN_NGAY_CK) {
            this.val = 5;
        }
        if (this.sAInvoice.accountingObject) {
            if (this.isEditReasonFirst) {
                if (this.sAInvoice.typeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
                    this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHCTT1']).subscribe((res: any) => {
                        this.sAInvoice.reason =
                            res['ebwebApp.sAInvoice.reasonSA.reasonBHCTT1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                    });
                } else {
                    this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHTTN1']).subscribe((res: any) => {
                        this.sAInvoice.reason =
                            res['ebwebApp.sAInvoice.reasonSA.reasonBHTTN1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                    });
                }
            }
            if (this.isEditReasonFirstBill) {
                if (this.sAInvoice.typeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
                    this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHCTT1']).subscribe((res: any) => {
                        this.saBill.reason =
                            res['ebwebApp.sAInvoice.reasonSA.reasonBHCTT1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                    });
                } else {
                    this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHTTN1']).subscribe((res: any) => {
                        this.saBill.reason =
                            res['ebwebApp.sAInvoice.reasonSA.reasonBHTTN1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                    });
                }
            }
            if (this.isEditReasonFirstXK) {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonPKX1']).subscribe((res: any) => {
                    this.rsInwardOutward.reason =
                        res['ebwebApp.sAInvoice.reasonSA.reasonPKX1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                });
            }
            if (this.isEditReasonFirstPT && this.sAInvoice.typeID !== TypeID.BAN_HANG_CHUA_THU_TIEN) {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonPT1']).subscribe((res: any) => {
                    this.mCReceipt.reason =
                        res['ebwebApp.sAInvoice.reasonSA.reasonPT1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                });
            }
            if (this.isEditReasonFirstBC && this.sAInvoice.typeID !== TypeID.BAN_HANG_CHUA_THU_TIEN) {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBC1']).subscribe((res: any) => {
                    this.mBDeposit.reason =
                        res['ebwebApp.sAInvoice.reasonSA.reasonBC1'] + ' ' + this.sAInvoice.accountingObject.accountingObjectName;
                });
            }
        } else {
            if (this.isEditReasonFirst && this.sAInvoice.typeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHCTT']).subscribe((res: any) => {
                    this.sAInvoice.reason = res['ebwebApp.sAInvoice.reasonSA.reasonBHCTT'];
                });
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHCTT']).subscribe((res: any) => {
                    this.saBill.reason = res['ebwebApp.sAInvoice.reasonSA.reasonBHCTT'];
                });
            }
            if (this.isEditReasonFirstXK && this.sAInvoice.typeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonPKX']).subscribe((res: any) => {
                    this.rsInwardOutward.reason = res['ebwebApp.sAInvoice.reasonSA.reasonPKX'];
                });
            }
            if (this.isEditReasonFirst && this.sAInvoice.typeID !== TypeID.BAN_HANG_CHUA_THU_TIEN) {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHTTN']).subscribe((res: any) => {
                    this.sAInvoice.reason = res['ebwebApp.sAInvoice.reasonSA.reasonBHTTN'];
                });
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBHTTN']).subscribe((res: any) => {
                    this.saBill.reason = res['ebwebApp.sAInvoice.reasonSA.reasonBHTTN'];
                });
            }
            if (this.isEditReasonFirstXK && this.sAInvoice.typeID !== TypeID.BAN_HANG_CHUA_THU_TIEN) {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonPKX']).subscribe((res: any) => {
                    this.rsInwardOutward.reason = res['ebwebApp.sAInvoice.reasonSA.reasonPKX'];
                });
            }
            if (this.isEditReasonFirstPT && this.sAInvoice.typeID !== TypeID.BAN_HANG_CHUA_THU_TIEN) {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonPT']).subscribe((res: any) => {
                    this.mCReceipt.reason = res['ebwebApp.sAInvoice.reasonSA.reasonPT'];
                });
            }
            if (this.isEditReasonFirstBC && this.sAInvoice.typeID !== TypeID.BAN_HANG_CHUA_THU_TIEN) {
                this.translateService.get(['ebwebApp.sAInvoice.reasonSA.reasonBC']).subscribe((res: any) => {
                    this.mBDeposit.reason = res['ebwebApp.sAInvoice.reasonSA.reasonBC'];
                });
            }
        }

        const columnList = [
            { column: AccountType.TK_CO, ppType: false },
            { column: AccountType.TK_NO, ppType: false },
            { column: AccountType.TK_THUE_GTGT, ppType: false },
            { column: AccountType.TKDU_THUE_GTGT, ppType: false },
            { column: AccountType.TK_CHIET_KHAU, ppType: false },
            { column: AccountType.TK_DON_GIA, ppType: false },
            { column: AccountType.TK_KHO, ppType: false },
            { column: AccountType.TK_THUE_XK, ppType: false }
        ];
        const param = {
            typeID: this.sAInvoice.typeID,
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
            this.exportTaxAmountAccountList = res.body.exportTaxAccount;
            this.exportTaxAccountCorrespondingList = res.body.exportTaxAccount;
            this.discountAccountDefault = res.body.discountAccountDefault;
            this.costAccountDefault = res.body.costAccountDefault;
            this.repositoryAccountDefault = res.body.repositoryAccountDefault;
            this.creditAccountDefault = res.body.creditAccountDefault;
            this.debitAccountDefault = res.body.debitAccountDefault;
            this.vatAccountDefault = res.body.vatAccountDefault;
            this.deductionDebitAccountDefault = res.body.deductionDebitAccountDefault;
            this.exportTaxAmountAccountDefault = res.body.exportTaxAmountAccountDefault;
            this.sAInvoiceDetails.forEach(detail => {
                if (this.discountAccountList.some(x => x.accountNumber === this.discountAccountDefault)) {
                    detail.discountAccount = this.discountAccountDefault;
                } else if (!this.discountAccountList.some(x => x.accountNumber === detail.discountAccount)) {
                    detail.discountAccount = '';
                }
                if (this.costAccountList.some(x => x.accountNumber === this.costAccountDefault)) {
                    detail.costAccount = this.costAccountDefault;
                } else if (!this.costAccountList.some(x => x.accountNumber === detail.costAccount)) {
                    detail.costAccount = '';
                }
                if (this.repositoryAccountList.some(x => x.accountNumber === this.repositoryAccountDefault)) {
                    detail.repositoryAccount = this.repositoryAccountDefault;
                } else if (!this.repositoryAccountList.some(x => x.accountNumber === detail.repositoryAccount)) {
                    detail.repositoryAccount = '';
                }
                if (this.debitAccountList.some(x => x.accountNumber === this.debitAccountDefault)) {
                    detail.debitAccount = this.debitAccountDefault;
                    detail.exportTaxAccountCorresponding = this.debitAccountDefault;
                } else if (!this.debitAccountList.some(x => x.accountNumber === detail.debitAccount)) {
                    detail.debitAccount = '';
                    detail.exportTaxAccountCorresponding = '';
                }
                if (this.vatAccountList.some(x => x.accountNumber === this.vatAccountDefault)) {
                    detail.vATAccount = this.vatAccountDefault;
                } else if (!this.vatAccountList.some(x => x.accountNumber === detail.vATAccount)) {
                    detail.vATAccount = '';
                }
                if (this.exportTaxAmountAccountList.some(x => x.accountNumber === this.exportTaxAmountAccountDefault)) {
                    detail.exportTaxAmountAccount = this.exportTaxAmountAccountDefault;
                } else if (!this.exportTaxAmountAccountList.some(x => x.accountNumber === detail.exportTaxAmountAccount)) {
                    detail.exportTaxAmountAccount = '';
                }
                if (this.deductionDebitAccountList.some(x => x.accountNumber === this.deductionDebitAccountDefault)) {
                    detail.deductionDebitAccount = this.deductionDebitAccountDefault;
                } else if (!this.deductionDebitAccountList.some(x => x.accountNumber === detail.deductionDebitAccount)) {
                    detail.deductionDebitAccount = '';
                }
            });
        });
    }

    public beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            this.modalRef = this.refModalService.open(this.viewVouchersSelected);
        } else if ($event.nextId === 'allocationdiscount') {
            $event.preventDefault();
            if (!this.isReadOnly) {
                const discountAllocations: IDiscountAllocation[] = [];
                for (let i = 0; i < this.sAInvoiceDetails.length; i++) {
                    const discountAllocation: IDiscountAllocation = new DiscountAllocation();
                    discountAllocation.productCode = this.sAInvoiceDetails[i].materialGoods
                        ? this.sAInvoiceDetails[i].materialGoods.materialGoodsCode
                        : '';
                    discountAllocation.reason = this.sAInvoiceDetails[i].description;
                    discountAllocation.quantity = this.sAInvoiceDetails[i].quantity;
                    discountAllocation.amountOriginal = this.sAInvoiceDetails[i].amountOriginal;
                    discountAllocation.object = this.sAInvoiceDetails[i];
                    discountAllocations.push(discountAllocation);
                }
                this.modalRef = this.refModalService.open(this.sAInvoice, DiscountAllocationModalComponent, {
                    discountAllocations
                });
            }
        }
    }

    onChangeCreateFrom() {
        if (this.createFrom) {
            if (this.createFrom === TypeID.BAO_GIA) {
                if (this.checkData) {
                    this.sAInvoiceDetails.forEach(item => {
                        if (item.sAQuoteID) {
                            this.viewSAQuote.push({
                                vATAmount: item.vATAmount,
                                vATAmountOriginal: item.vATAmountOriginal,
                                vATRate: item.vATRate,
                                amount: item.amount,
                                amountOriginal: item.amountOriginal,
                                discountRate: item.discountRate,
                                discountAmount: item.discountAmount,
                                discountAmountOriginal: item.discountAmountOriginal,
                                quantity: item.quantity,
                                mainUnitPrice: item.mainUnitPrice,
                                unitPrice: item.unitPrice,
                                unitPriceOriginal: item.unitPriceOriginal,
                                formula: item.formula,
                                description: item.description,
                                mainConvertRate: item.mainConvertRate,
                                id: item.sAQuoteID,
                                sAQuoteDetailID: item.sAQuoteDetailID,
                                vATDescription: item.vATDescription,
                                mainQuantity: item.mainQuantity,
                                unitID: item.unitID,
                                materialGoodsID: item.materialGoodsID,
                                materialGoodsCode: item.description
                            });
                        }
                    });
                }
                this.modalRef = this.refModalService.open(
                    this.viewSAQuote,
                    EbSaQuoteModalComponent,
                    null,
                    false,
                    this.sAInvoice.typeID,
                    null,
                    this.sAInvoice.currencyID,
                    null,
                    this.sAInvoice.accountingObject ? this.sAInvoice.accountingObject.id : null
                );
            } else if (this.createFrom === TypeID.DON_DAT_HANG) {
                if (this.checkData) {
                    this.sAInvoiceDetails.forEach(item => {
                        if (item.sAOrderID) {
                            this.viewSAOrder.push({
                                vATRate: item.vATRate,
                                discountRate: item.discountRate,
                                quantityOut: item.quantity,
                                mainUnitPrice: item.mainUnitPrice,
                                unitPrice: item.unitPrice,
                                unitPriceOriginal: item.unitPriceOriginal,
                                formula: item.formula,
                                materialGoodsCode: item.description,
                                mainConvertRate: item.mainConvertRate,
                                id: item.sAOrderID,
                                sAOrderDetailID: item.sAOrderDetailID,
                                no: item.sAOrderNo,
                                vATDescription: item.vATDescription,
                                mainQuantity: item.mainQuantity,
                                unitID: item.unitID,
                                materialGoodsID: item.materialGoodsID
                            });
                        }
                    });
                }
                this.modalRef = this.refModalService.open(
                    this.viewSAOrder,
                    EbSaOrderModalComponent,
                    null,
                    false,
                    this.sAInvoice.typeID,
                    null,
                    this.sAInvoice.currencyID,
                    this.sAInvoice.id,
                    this.sAInvoice.accountingObject ? this.sAInvoice.accountingObject.id : null
                );
            } else if (this.createFrom === PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN) {
                if (this.checkData) {
                    this.sAInvoiceDetails.forEach(item => {
                        if (item.pPInvoiceID) {
                            this.viewPPInvoice.push({
                                vATRate: item.vATRate,
                                discountRate: item.discountRate,
                                quantity: item.quantity,
                                mainUnitPrice: item.mainUnitPrice,
                                unitPrice: item.unitPrice,
                                unitPriceOriginal: item.unitPriceOriginal,
                                formula: item.formula,
                                mainConvertRate: item.mainConvertRate,
                                id: item.pPInvoiceID,
                                pPInvoiceDetailID: item.pPInvoiceDetailID,
                                vATDescription: item.vATDescription,
                                mainQuantity: item.mainQuantity,
                                unitID: item.unitID,
                                materialGoodsID: item.materialGoodsID,
                                materialGoodsCode: item.description,
                                lotNo: item.lotNo,
                                expiryDate: item.expiryDate
                            });
                        }
                    });
                }
                this.modalRef = this.refModalService.open(
                    this.viewPPInvoice,
                    PpInvoiceModalComponent,
                    null,
                    false,
                    this.sAInvoice.typeID,
                    null,
                    this.sAInvoice.currencyID,
                    null,
                    this.sAInvoice.accountingObject ? this.sAInvoice.accountingObject.id : null
                );
            } else if (this.createFrom === TypeID.XUAT_KHO) {
                if (this.checkData) {
                    this.sAInvoiceDetails.forEach(item => {
                        if (item.rSInwardOutwardID) {
                            this.viewRSOutward.push({
                                materialGoodsID: item.materialGoodsID,
                                quantity: item.quantity,
                                mainUnitPrice: item.mainUnitPrice,
                                formula: item.formula,
                                description: item.description,
                                mainConvertRate: item.mainConvertRate,
                                id: item.rSInwardOutwardID,
                                rSInwardOutwardDetailID: item.rSInwardOutwardDetailID,
                                mainQuantity: item.mainQuantity,
                                creditAccount: item.creditAccount,
                                debitAccount: item.debitAccount
                            });
                        }
                    });
                }
                this.modalRef = this.refModalService.open(
                    this.viewRSOutward,
                    RsOutwardModalComponent,
                    null,
                    false,
                    this.sAInvoice.typeID,
                    null,
                    this.sAInvoice.currencyID,
                    null,
                    this.sAInvoice.accountingObject ? this.sAInvoice.accountingObject.id : null
                );
            } else if (this.createFrom === TypeID.CHUYEN_KHO) {
                this.modalRef = this.refModalService.open(
                    this.viewRSTranfer,
                    RsTranfersModalComponent,
                    null,
                    false,
                    this.sAInvoice.typeID,
                    null,
                    this.sAInvoice.currencyID,
                    null,
                    this.sAInvoice.accountingObject ? this.sAInvoice.accountingObject.id : null
                );
            } else if (this.createFrom === TypeID.HOP_DONG_BAN) {
                this.modalRef = this.refModalService.open(
                    this.viewSAOrder,
                    EmContractModalComponent,
                    null,
                    false,
                    this.sAInvoice.typeID,
                    null,
                    this.sAInvoice.currencyID,
                    null,
                    this.sAInvoice.accountingObject ? this.sAInvoice.accountingObject.id : null
                );
            }
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

    // ghi so
    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuBanHang_GhiSo])
    record() {
        event.preventDefault();
        if (
            !this.isEdit &&
            !this.sAInvoice.recorded &&
            !this.checkCloseBook(this.account, this.sAInvoice.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            this.utilsService.checkQuantityExistsTest1(this.sAInvoiceDetails, this.account, this.sAInvoice.postedDate).then(data => {
                if (data) {
                    this.mgForPPOderTextCode = data;
                    if (!this.checkSLT && this.mgForPPOderTextCode.quantityExists && this.sAInvoice.isDeliveryVoucher) {
                        this.toasService.error(
                            this.translateService.instant('ebwebApp.pPDiscountReturnDetails.error.quantityExistsRecordErrorSave'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                        return;
                    }
                    if (!this.sAInvoice.recorded) {
                        this.record_ = {};
                        this.record_.id = this.sAInvoice.id;
                        this.record_.typeID = this.sAInvoice.typeID;
                        this.gLService.record(this.record_).subscribe(
                            (res: HttpResponse<Irecord>) => {
                                console.log(JSON.stringify(res.body));
                                if (res.body.success === true) {
                                    this.sAInvoice.recorded = true;
                                    this.materialGoodsService.queryForComboboxGood().subscribe((mate: HttpResponse<IMaterialGoods[]>) => {
                                        this.materialGoodss = mate.body;
                                    });
                                    this.toasService.success(
                                        this.translateService.instant('ebwebApp.mBCreditCard.recordSuccess'),
                                        this.translateService.instant('ebwebApp.mBDeposit.message')
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
                            ref => {
                                this.toasService.error(
                                    this.translateService.instant('global.data.recordFailed'),
                                    this.translateService.instant('ebwebApp.mBDeposit.message')
                                );
                            }
                        );
                    }
                }
            });
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuBanHang_GhiSo])
    unrecord() {
        event.preventDefault();
        if (
            !this.isEdit &&
            this.sAInvoice.recorded &&
            !this.checkCloseBook(this.account, this.sAInvoice.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            this.sAInvoiceService
                .checkRelateVoucher({
                    sAInvoice: this.sAInvoice.id,
                    isCheckKPXK: false
                })
                .subscribe((response: HttpResponse<number>) => {
                    this.typeNotify = response.body;
                    if (this.typeNotify === 1 || this.typeNotify === 2 || this.typeNotify === 3) {
                        this.modalRef = this.modalService.open(this.contentUnRecord, { backdrop: 'static' });
                    } else {
                        this.record_ = {};
                        this.record_.id = this.sAInvoice.id;
                        this.record_.typeID = this.sAInvoice.typeID;
                        this.record_.repositoryLedgerID = this.sAInvoice.rsInwardOutwardID;
                        this.gLService.unrecord(this.record_).subscribe(
                            (res: HttpResponse<Irecord>) => {
                                if (res.body.success) {
                                    this.sAInvoice.recorded = false;
                                    this.materialGoodsService.queryForComboboxGood().subscribe((mate: HttpResponse<IMaterialGoods[]>) => {
                                        this.materialGoodss = mate.body;
                                    });
                                    this.toasService.success(
                                        this.translateService.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                                        this.translateService.instant('ebwebApp.mBDeposit.message')
                                    );
                                }
                            },
                            ref => {
                                this.toasService.error(
                                    this.translateService.instant('global.data.unRecordFailed'),
                                    this.translateService.instant('ebwebApp.mBDeposit.message')
                                );
                            }
                        );
                    }
                });
        }
    }

    unrecordForm() {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.sAInvoice.recorded) {
            this.record_ = {};
            this.record_.id = this.sAInvoice.id;
            this.record_.typeID = this.sAInvoice.typeID;
            this.record_.repositoryLedgerID = this.sAInvoice.rsInwardOutwardID;
            this.gLService.unrecord(this.record_).subscribe(
                (res: HttpResponse<Irecord>) => {
                    if (res.body.success) {
                        this.sAInvoice.recorded = false;
                        this.toasService.success(
                            this.translateService.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                },
                ref => {
                    this.toasService.error(
                        this.translateService.instant('global.data.unRecordFailed'),
                        this.translateService.instant('ebwebApp.mBDeposit.message')
                    );
                }
            );
        }
    }

    closeForm() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.checkData && this.dataSession && !this.utilsService.isShowPopup) {
            if (!this.page) {
                window.history.back();
            }
            this.dataSession.page = this.page;
            this.dataSession.itemsPerPage = this.itemsPerPage;
            this.dataSession.pageCount = this.pageCount;
            this.dataSession.totalItems = this.totalItems;
            this.dataSession.rowNum = this.rowNum;
            this.dataSession.predicate = this.predicate;
            this.dataSession.reverse = this.reverse;
            sessionStorage.setItem('dataSessionSAInvoice', JSON.stringify(this.dataSession));
            if (this.isEdit && !this.checkValidateClose()) {
                this.modalRef = this.modalService.open(this.contentClose, { backdrop: 'static' });
            } else {
                if (this.fromMCReceipt) {
                    this.closeAllFromMCReceipt();
                } else if (this.fromMBDeposit) {
                    this.closeAllFromMBDeposit();
                } else if (this.isFromOutWard) {
                    this.backToRS();
                } else if (this.fromMBDeposit) {
                    this.router.navigate(['/mb-deposit']);
                } else {
                    this.isCloseAll = true;
                    this.router.navigate(['chung-tu-ban-hang']);
                }
            }
        } else if (!this.utilsService.isShowPopup) {
            if (this.isEdit && !this.checkValidateClose()) {
                this.modalRef = this.modalService.open(this.contentClose, { backdrop: 'static' });
            } else {
                if (this.fromMCReceipt) {
                    this.closeAllFromMCReceipt();
                } else if (this.fromMBDeposit) {
                    this.closeAllFromMBDeposit();
                } else if (this.isFromOutWard) {
                    this.backToRS();
                } else if (this.fromMBDeposit) {
                    this.router.navigate(['/mb-deposit']);
                } else {
                    this.isCloseAll = true;
                    this.router.navigate(['chung-tu-ban-hang']);
                }
            }
        }
    }

    /*Xuất kho by Huypq*/
    backToRS() {
        sessionStorage.removeItem('xuatKhoDataSession');
        this.router.navigate(
            ['/xuat-kho'],
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
    }

    /*Xuất kho by Huypq*/

    registerRef() {
        this.eventSubscriber = this.eventManager.subscribe('selectViewVoucher', response => {
            if (this.isEdit) {
                this.viewVouchersSelected = this.viewVouchersSelected.filter(x => x.attach === true);
                response.content.forEach(item => {
                    item.attach = false;
                    this.viewVouchersSelected.push(item);
                });
            }
        });
    }

    checknoFBook() {
        if (this.noBookRSI === this.noBookVoucher && this.sAInvoice.isDeliveryVoucher) {
            this.toasService.error(this.translateService.instant('ebwebApp.sAInvoice.error.voucherError'));
        }
    }

    print($event) {
        event.preventDefault();
    }

    deleteForm() {
        if (this.checkData) {
            if (this.typeDelete === 2) {
                this.toasService.error(this.translateService.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
            } else {
                this.sAInvoiceService.delete(this.sAInvoice.id).subscribe(
                    ref => {
                        this.checkData = true;
                        this.toasService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
                        this.modalRef.close();
                        this.close();
                    },
                    ref => {
                        this.checkData = false;
                        this.toasService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.error'));
                    }
                );
            }
            if (this.isFromOutWard) {
                this.backToRS();
            } else {
                this.router.navigate(['/', { outlets: { popup: '' + this.sAInvoice.id + '/delete' } }]);
            }
        }
    }

    changeTypeLedger() {
        if (Number(this.sAInvoice.typeLedger) === 1) {
            this.sAInvoice.noFBook = null;
            this.sAInvoice.noMBook = this.noBookVoucher;
            this.rsInwardOutward.noFBook = null;
            this.rsInwardOutward.noMBook = this.noBookRSI;
            this.mBDeposit.noFBook = null;
            this.mBDeposit.noMBook = this.noMBDeposit;
            this.mCReceipt.noFBook = null;
            this.mCReceipt.noMBook = this.noMCReceipt;
        } else if (Number(this.sAInvoice.typeLedger) === 0) {
            this.sAInvoice.noFBook = this.noBookVoucher;
            this.sAInvoice.noMBook = null;
            this.rsInwardOutward.noFBook = this.noBookRSI;
            this.rsInwardOutward.noMBook = null;
            this.mBDeposit.noFBook = this.noMBDeposit;
            this.mBDeposit.noMBook = null;
            this.mCReceipt.noFBook = this.noMCReceipt;
            this.mCReceipt.noMBook = null;
        } else {
            if (Number(this.checkBook) === 0) {
                this.sAInvoice.noFBook = this.noBookVoucher;
                this.rsInwardOutward.noFBook = this.noBookRSI;
                this.mBDeposit.noFBook = this.noMBDeposit;
                this.mCReceipt.noFBook = this.noMCReceipt;
            } else {
                this.sAInvoice.noMBook = this.noBookVoucher;
                this.rsInwardOutward.noMBook = this.noBookRSI;
                this.mBDeposit.noMBook = this.noMBDeposit;
                this.mCReceipt.noMBook = this.noMCReceipt;
            }
        }
    }

    selectChangeBank() {
        const lstBankAccountDetail = this.bankAccountDetails.find(
            bankAccountDetail => bankAccountDetail.id === this.mBDeposit.bankAccountDetailID
        );
        if (lstBankAccountDetail) {
            this.mBDeposit.bankName = lstBankAccountDetail.bankName;
        } else {
            this.mBDeposit.bankName = '';
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuBanHang_Them])
    copyAndNew() {
        event.preventDefault();
        if (!this.isEdit && !this.utilsService.isShowPopup) {
            this.sAInvoice.id = null;
            this.sAInvoice.recorded = false;
            this.sAInvoice.invoiceNo = null;
            this.sAInvoice.noMBook = null;
            this.sAInvoice.noFBook = null;
            this.sAInvoice.rsInwardOutwardID = null;
            this.sAInvoice.sAInvoiceDetails = [];
            this.sAInvoiceDetails.forEach(
                item => (
                    (item.id = null),
                    (item.sAOrderDetailID = null),
                    (item.sAOrderID = null),
                    (item.sAQuoteDetailID = null),
                    (item.sAQuoteID = null),
                    (item.sABillID = null),
                    (item.sABillDetailID = null),
                    (item.pPInvoiceID = null),
                    (item.pPInvoiceDetailID = null),
                    (item.rSTransferDetailID = null),
                    (item.rSTransferID = null),
                    (item.rSInwardOutwardDetailID = null),
                    (item.rSInwardOutwardID = null),
                    (item.sAOrderNo = null)
                )
            );
            this.saBill.saBillDetails.forEach(item => (item.id = null));
            this.saBill.id = null;
            this.saBill.statusInvoice = 0;
            this.saBill.invoiceNo = null;
            this.mCReceipt.id = null;
            this.mCReceipt.noMBook = null;
            this.mCReceipt.noFBook = null;
            this.mBDeposit.id = null;
            this.mBDeposit.noMBook = null;
            this.mBDeposit.noFBook = null;
            this.rsInwardOutward.id = null;
            this.rsInwardOutward.noMBook = null;
            this.rsInwardOutward.noFBook = null;
            this.viewVouchersSelected = [];
            this.isEdit = true;
            this.checkData = false;
            this.createFrom = 0;
            this.genCodeVoucher(this.checkBook);
            this.copy();
        }
    }

    convertItemSelect(item, sAInvoiceDetailItem) {
        Object.assign(sAInvoiceDetailItem, item);
        sAInvoiceDetailItem.id = null;
        if (item.units) {
            sAInvoiceDetailItem.units = item.units;
            if (item.unitID) {
                sAInvoiceDetailItem.unit = item.units.find(n => n.id === item.unitID);
            }
            if (item.mainUnitID) {
                const mainUnitItem = item.units.find(i => i.id === item.mainUnitID);
                sAInvoiceDetailItem.mainUnitID = mainUnitItem.id;
                sAInvoiceDetailItem.mainUnitName = mainUnitItem.unitName;
            }
        }
        sAInvoiceDetailItem.vATRate = item.vatRate;
        sAInvoiceDetailItem.vATAmountOriginal = item.vatAmountOriginal;
        sAInvoiceDetailItem.vATAmount = item.vatAmount;
    }

    selectSabill() {
        this.sAInvoiceDetails = [];
        let sAInvoiceDetail: ISAInvoiceDetails;
        const invoiceNo = '';
        this.saBillDetail.forEach(object => {
            sAInvoiceDetail = {};
            this.convertItemSelect(object, sAInvoiceDetail);
            sAInvoiceDetail.materialGoods = this.materialGoodss.find(n => n.id === sAInvoiceDetail.materialGoodsID);
            sAInvoiceDetail.discountAccount = this.discountAccountDefault;
            sAInvoiceDetail.costAccount = this.costAccountDefault;
            sAInvoiceDetail.repositoryAccount = this.repositoryAccountDefault;
            sAInvoiceDetail.creditAccount = this.creditAccountDefault;
            sAInvoiceDetail.debitAccount = this.debitAccountDefault;
            sAInvoiceDetail.vATAccount = this.vatAccountDefault;
            sAInvoiceDetail.exportTaxAmountAccount = this.exportTaxAmountAccountDefault;
            sAInvoiceDetail.exportTaxAccountCorresponding = this.debitAccountDefault;
            sAInvoiceDetail.deductionDebitAccount = this.deductionDebitAccountDefault;
            this.sAInvoiceDetails.push(sAInvoiceDetail);
        });
    }

    selectedItemPerPage(boolean: Boolean) {
        if (boolean) {
            this.pageVoucher = 1;
        }
    }

    copy() {
        this.saInvoiceCopy = Object.assign({}, this.sAInvoice);
        this.saInvoiceCopyDetailsCopy = this.sAInvoiceDetails.map(object => ({ ...object }));
        if (this.sAInvoice.isDeliveryVoucher) {
            this.rsInwardOutwardCopy = Object.assign({}, this.rsInwardOutward);
        }
        if (this.sAInvoice.isBill) {
            this.saBillCopy = Object.assign({}, this.saBill);
            this.saBillCopy.saBillDetails = this.saBill.saBillDetails.map(object => ({ ...object }));
        }
        if (this.sAInvoice.typeID === TypeID.BAN_HANG_THU_TIEN_NGAY_TM) {
            this.mCReceiptCopy = Object.assign({}, this.mCReceipt);
        }
        if (this.sAInvoice.typeID === TypeID.BAN_HANG_THU_TIEN_NGAY_CK) {
            this.mBDepositCopy = Object.assign({}, this.mBDeposit);
        }
    }

    close() {
        this.modalRef.close();
        this.isCloseAll = true;
        if (this.isFromOutWard) {
            /*Xuất kho by huypq*/
            this.backToRS();
        } else if (this.fromMCReceipt) {
            this.closeAllFromMCReceipt();
        } else if (this.fromMBDeposit) {
            this.closeAllFromMBDeposit();
        } else {
            this.router.navigate(['chung-tu-ban-hang']);
            this.dataSession = JSON.parse(sessionStorage.getItem('dataSearchSAInvoice'));
            if (this.dataSession) {
                this.page = this.dataSession.page;
                this.itemsPerPage = this.dataSession.itemsPerPage;
                this.predicate = this.dataSession.predicate;
                this.reverse = this.dataSession.reverse;
            } else {
                this.dataSession = null;
            }
        }
    }

    private getTypeLedger() {
        this.genCodeVoucher(this.checkBook);
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    checkValidateClose() {
        if (
            !this.saInvoiceCopy ||
            (this.utilsService.isEquivalent(this.sAInvoice, this.saInvoiceCopy) &&
                this.utilsService.isEquivalentArray(this.sAInvoiceDetails, this.saInvoiceCopyDetailsCopy))
        ) {
            if (this.sAInvoice.isBill) {
                if (!this.saBill || this.utilsService.isEquivalent(this.saBill, this.saBillCopy)) {
                    return true;
                }
            }
            if (this.sAInvoice.isDeliveryVoucher) {
                if (this.rsInwardOutward || this.utilsService.isEquivalent(this.rsInwardOutward, this.rsInwardOutwardCopy)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    afterSelectedIWVoucher() {
        this.eventManager.subscribe('selectIWVoucher', ref => {
            const iWVoucher = ref.content;
            this.sAInvoiceDetails[this.indexDetail].oWPrice = iWVoucher.unitPrice;
            this.sAInvoiceDetails[this.indexDetail].oWAmount = this.utilsService.round(
                this.sAInvoiceDetails[this.indexDetail].quantity * iWVoucher.unitPrice,
                this.account.systemOption,
                7
            );
            this.sAInvoiceDetails[this.indexDetail].confrontID = iWVoucher.id;
            this.sAInvoiceDetails[this.indexDetail].confrontDetailID = iWVoucher.detailID;
            this.sumAllList();
        });
    }

    afterDeleteOrAddRow() {
        this.eventManager.subscribe('selectViewInvoice', ref => {
            this.saBillDetail = ref.content;
            if (
                ref.content &&
                ref.content.length === ref.content.filter(x => x.accountingObjectID === ref.content[0].accountingObjectID).length
            ) {
                if (this.accountingObjects.some(x => x.id === ref.content[0].accountingObjectID)) {
                    this.sAInvoice.accountingObjectID = ref.content[0].accountingObjectID;
                    this.sAInvoice.accountingObjectName = ref.content[0].accountingObjectName;
                    this.sAInvoice.accountingObjectAddress = ref.content[0].accountingObjectAddress;
                    this.sAInvoice.contactName = ref.content[0].contactName;
                    this.sAInvoice.companyTaxCode = ref.content[0].companyTaxCode;
                    this.sAInvoice.accountingObject = this.accountingObjects.find(x => x.id === ref.content[0].accountingObjectID);
                    this.changeAccountingName();
                }
            }
            this.selectSabill();
            this.sumAllList();
        });
        this.eventManager.subscribe('selectViewSAQuote', async ref => {
            this.viewSAQuote = ref.content;
            this.sAInvoiceDetails = [];
            this.viewVouchersSelected = this.viewVouchersSelected.filter(x => !x.attach);
            if (
                this.viewSAQuote &&
                this.viewSAQuote.length ===
                    this.viewSAQuote.filter(x => x.accountingObjectID === this.viewSAQuote[0].accountingObjectID).length
            ) {
                if (this.accountingObjects.some(x => x.id === this.viewSAQuote[0].accountingObjectID)) {
                    this.sAInvoice.accountingObjectID = this.viewSAQuote[0].accountingObjectID;
                    this.sAInvoice.accountingObjectName = this.viewSAQuote[0].accountingObjectName;
                    this.sAInvoice.accountingObjectAddress = this.viewSAQuote[0].accountingObjectAddress;
                    this.sAInvoice.contactName = this.viewSAQuote[0].contactName;
                    this.sAInvoice.companyTaxCode = this.viewSAQuote[0].companyTaxCode;
                    this.sAInvoice.employeeID = this.viewSAQuote[0].employeeID;
                    this.sAInvoice.accountingObject = this.accountingObjects.find(x => x.id === this.viewSAQuote[0].accountingObjectID);
                    this.changeAccountingName();
                }
            }
            for (let i = 0; i < this.viewSAQuote.length; i++) {
                const detail: ISAInvoiceDetails = {};
                detail.vATAmount = this.viewSAQuote[i].vATAmount;
                detail.vATAmountOriginal = this.viewSAQuote[i].vATAmountOriginal;
                detail.vATRate = this.viewSAQuote[i].vATRate;
                detail.amount = this.viewSAQuote[i].amount;
                detail.amountOriginal = this.viewSAQuote[i].amountOriginal;
                detail.discountRate = this.viewSAQuote[i].discountRate;
                detail.discountAmount = this.viewSAQuote[i].discountAmount;
                detail.discountAmountOriginal = this.viewSAQuote[i].discountAmountOriginal;
                detail.quantity = this.viewSAQuote[i].quantity;
                detail.mainUnitPrice = this.viewSAQuote[i].mainUnitPrice;
                detail.unitPrice = this.viewSAQuote[i].unitPrice;
                detail.unitPriceOriginal = this.viewSAQuote[i].unitPriceOriginal;
                detail.formula = this.viewSAQuote[i].formula;
                detail.description = this.viewSAQuote[i].description;
                detail.mainConvertRate = this.viewSAQuote[i].mainConvertRate;
                detail.sAQuoteID = this.viewSAQuote[i].id;
                detail.sAQuoteDetailID = this.viewSAQuote[i].sAQuoteDetailID;
                detail.mainQuantity = this.viewSAQuote[i].mainQuantity;
                detail.accountingObjectID = this.viewSAQuote[i].accountingObjectID;
                detail.accountingObject = this.accountingObjects.find(x => x.id === detail.accountingObjectID);
                detail.materialGoodsID = this.viewSAQuote[i].materialGoodsID;
                detail.description = this.viewSAQuote[i].materialGoodsCode;
                detail.materialGoods = this.materialGoodss.find(n => n.id === this.viewSAQuote[i].materialGoodsID);
                detail.vATDescription = detail.materialGoods.materialGoodsCode;
                if (!this.hiddenVAT) {
                    this.translateService.get(['ebwebApp.purchaseOrder.vatTax']).subscribe((res: any) => {
                        detail.vATDescription = res['ebwebApp.purchaseOrder.vatTax'] + ' ' + detail.materialGoods.materialGoodsCode;
                    });
                }
                if (detail.materialGoods.careerGroupID) {
                    detail.careerGroupID = detail.materialGoods.careerGroupID;
                }
                if (this.discountAccountList.some(x => x.accountNumber === this.discountAccountDefault)) {
                    detail.discountAccount = this.discountAccountDefault;
                }
                if (this.costAccountList.some(x => x.accountNumber === this.costAccountDefault)) {
                    detail.costAccount = this.costAccountDefault;
                }
                if (this.repositoryAccountList.some(x => x.accountNumber === this.repositoryAccountDefault)) {
                    detail.repositoryAccount = this.repositoryAccountDefault;
                }
                if (this.creditAccountList.some(x => x.accountNumber === this.creditAccountDefault)) {
                    detail.creditAccount = this.creditAccountDefault;
                }
                if (this.debitAccountList.some(x => x.accountNumber === this.debitAccountDefault)) {
                    detail.debitAccount = this.debitAccountDefault;
                    detail.exportTaxAccountCorresponding = this.debitAccountDefault;
                }
                if (this.vatAccountList.some(x => x.accountNumber === this.vatAccountDefault)) {
                    detail.vATAccount = this.vatAccountDefault;
                }
                if (this.exportTaxAmountAccountList.some(x => x.accountNumber === this.exportTaxAmountAccountDefault)) {
                    detail.exportTaxAmountAccount = this.exportTaxAmountAccountDefault;
                }
                if (this.deductionDebitAccountList.some(x => x.accountNumber === this.deductionDebitAccountDefault)) {
                    detail.deductionDebitAccount = this.deductionDebitAccountDefault;
                }
                detail.repositoryID = detail.materialGoods.repositoryID;
                detail.creditAccount = detail.materialGoods.revenueAccount;
                detail.repositoryAccount = detail.materialGoods.reponsitoryAccount;
                detail.units = this.units.filter(item => item.materialGoodsID === detail.materialGoodsID);
                if (this.viewSAQuote[i].unitID) {
                    detail.unit = detail.units.find(x => x.id === this.viewSAQuote[i].unitID);
                    detail.mainUnit = detail.units.find(x => x.id === this.viewSAQuote[i].mainUnitID);
                    this.selectUnit(detail, true);
                }
                this.sAInvoiceDetails.push(detail);
                if (this.viewVouchersSelected.filter(x => x.refID2 === detail.sAQuoteID).length === 0) {
                    this.viewVouchersSelected.push({
                        id: null,
                        refID1: null,
                        refID2: detail.sAQuoteID,
                        no: this.viewSAQuote[i].no,
                        date: this.viewSAQuote[i].date.format(DATE_FORMAT_SLASH),
                        reason: this.viewSAQuote[i].descriptionParent,
                        typeID: TypeID.BAO_GIA,
                        typeGroupID: GROUP_TYPEID.GROUP_SAQUOTE,
                        attach: true
                    });
                }
            }
            this.sumAllList();
        });
        this.eventManager.subscribe('selectViewSAOrder', async ref => {
            this.viewSAOrder = ref.content;
            this.sAInvoiceDetails = [];
            this.viewVouchersSelected = this.viewVouchersSelected.filter(x => !x.attach);
            if (
                this.viewSAOrder &&
                this.viewSAOrder.length ===
                    this.viewSAOrder.filter(x => x.accountingObjectID === this.viewSAOrder[0].accountingObjectID).length
            ) {
                if (this.accountingObjects.some(x => x.id === this.viewSAOrder[0].accountingObjectID)) {
                    this.sAInvoice.accountingObjectID = this.viewSAOrder[0].accountingObjectID;
                    this.sAInvoice.accountingObjectName = this.viewSAOrder[0].accountingObjectName;
                    this.sAInvoice.accountingObjectAddress = this.viewSAOrder[0].accountingObjectAddress;
                    this.sAInvoice.contactName = this.viewSAOrder[0].contactName;
                    this.sAInvoice.companyTaxCode = this.viewSAOrder[0].companyTaxCode;
                    this.sAInvoice.employeeID = this.viewSAOrder[0].employeeID;
                    this.sAInvoice.accountingObject = this.accountingObjects.find(x => x.id === this.viewSAOrder[0].accountingObjectID);
                    this.changeAccountingName();
                }
            }
            for (let i = 0; i < this.viewSAOrder.length; i++) {
                const detail: ISAInvoiceDetails = {};
                detail.vATRate = this.viewSAOrder[i].vATRate;
                detail.discountRate = this.viewSAOrder[i].discountRate;
                detail.quantity = this.viewSAOrder[i].quantityOut;
                detail.mainUnitPrice = this.viewSAOrder[i].mainUnitPrice;
                detail.unitPrice = this.viewSAOrder[i].unitPrice;
                detail.unitPriceOriginal = this.viewSAOrder[i].unitPriceOriginal;
                detail.formula = this.viewSAOrder[i].formula;
                detail.mainConvertRate = this.viewSAOrder[i].mainConvertRate;
                detail.sAOrderID = this.viewSAOrder[i].id;
                detail.sAOrderDetailID = this.viewSAOrder[i].sAOrderDetailID;
                detail.sAOrderNo = this.viewSAOrder[i].no;
                detail.mainQuantity = this.viewSAOrder[i].mainQuantity;
                detail.accountingObjectID = this.viewSAOrder[i].accountingObjectID;
                detail.accountingObject = this.accountingObjects.find(x => x.id === detail.accountingObjectID);
                detail.materialGoodsID = this.viewSAOrder[i].materialGoodsID;
                detail.description = this.viewSAOrder[i].description;
                detail.materialGoods = this.materialGoodss.find(n => n.id === this.viewSAOrder[i].materialGoodsID);
                detail.vATDescription = detail.materialGoods.materialGoodsCode;
                if (!this.hiddenVAT) {
                    this.translateService.get(['ebwebApp.purchaseOrder.vatTax']).subscribe((res: any) => {
                        detail.vATDescription = res['ebwebApp.purchaseOrder.vatTax'] + ' ' + detail.materialGoods.materialGoodsCode;
                    });
                }
                if (detail.materialGoods.careerGroupID) {
                    detail.careerGroupID = detail.materialGoods.careerGroupID;
                }
                if (this.discountAccountList.some(x => x.accountNumber === this.discountAccountDefault)) {
                    detail.discountAccount = this.discountAccountDefault;
                }
                if (this.costAccountList.some(x => x.accountNumber === this.costAccountDefault)) {
                    detail.costAccount = this.costAccountDefault;
                }
                if (this.repositoryAccountList.some(x => x.accountNumber === this.repositoryAccountDefault)) {
                    detail.repositoryAccount = this.repositoryAccountDefault;
                }
                if (this.creditAccountList.some(x => x.accountNumber === this.creditAccountDefault)) {
                    detail.creditAccount = this.creditAccountDefault;
                }
                if (this.debitAccountList.some(x => x.accountNumber === this.debitAccountDefault)) {
                    detail.debitAccount = this.debitAccountDefault;
                    detail.exportTaxAccountCorresponding = this.debitAccountDefault;
                }
                if (this.vatAccountList.some(x => x.accountNumber === this.vatAccountDefault)) {
                    detail.vATAccount = this.vatAccountDefault;
                }
                if (this.exportTaxAmountAccountList.some(x => x.accountNumber === this.exportTaxAmountAccountDefault)) {
                    detail.exportTaxAmountAccount = this.exportTaxAmountAccountDefault;
                }
                if (this.deductionDebitAccountList.some(x => x.accountNumber === this.deductionDebitAccountDefault)) {
                    detail.deductionDebitAccount = this.deductionDebitAccountDefault;
                }
                detail.repositoryID = detail.materialGoods.repositoryID;
                detail.creditAccount = detail.materialGoods.revenueAccount;
                detail.repositoryAccount = detail.materialGoods.reponsitoryAccount;
                if (detail.repositoryID) {
                    detail.repositoryAccount = this.repositories.find(x => x.id === detail.repositoryID).defaultAccount;
                }
                detail.units = this.units.filter(item => item.materialGoodsID === detail.materialGoodsID);
                if (this.viewSAOrder[i].unitID) {
                    detail.unit = detail.units.find(x => x.id === this.viewSAOrder[i].unitID);
                    detail.mainUnit = detail.units.find(x => x.id === this.viewSAOrder[i].mainUnitID);
                    this.selectUnit(detail, true);
                }
                this.changeQuantity(detail);
                this.sAInvoiceDetails.push(detail);
                if (this.viewVouchersSelected.filter(x => x.refID2 === detail.sAOrderID).length === 0) {
                    this.viewVouchersSelected.push({
                        id: null,
                        refID1: null,
                        refID2: detail.sAOrderID,
                        no: this.viewSAOrder[i].no,
                        date: this.viewSAOrder[i].date.format(DATE_FORMAT_SLASH),
                        reason: this.viewSAOrder[i].reason,
                        typeID: TypeID.DON_DAT_HANG,
                        typeGroupID: GROUP_TYPEID.GROUP_SAORDER,
                        attach: true
                    });
                }
            }
            this.sumAllList();
        });
        this.eventManager.subscribe('selectPPInvoice', async ref => {
            this.viewPPInvoice = ref.content;
            this.sAInvoiceDetails = [];
            this.viewVouchersSelected = this.viewVouchersSelected.filter(x => !x.attach);
            for (let i = 0; i < this.viewPPInvoice.length; i++) {
                const detail: ISAInvoiceDetails = {};
                detail.vATRate = this.viewPPInvoice[i].vATRate;
                detail.discountRate = this.viewPPInvoice[i].discountRate;
                detail.quantity = this.viewPPInvoice[i].quantity;
                detail.mainUnitPrice = this.viewPPInvoice[i].mainUnitPrice;
                detail.unitPrice = this.viewPPInvoice[i].unitPrice;
                detail.unitPriceOriginal = this.viewPPInvoice[i].unitPriceOriginal;
                detail.formula = this.viewPPInvoice[i].formula;
                detail.description = this.viewPPInvoice[i].reason;
                detail.mainConvertRate = this.viewPPInvoice[i].mainConvertRate;
                detail.pPInvoiceID = this.viewPPInvoice[i].id;
                detail.pPInvoiceDetailID = this.viewPPInvoice[i].pPInvoiceDetailID;
                detail.mainQuantity = this.viewPPInvoice[i].mainQuantity;
                detail.materialGoodsID = this.viewPPInvoice[i].materialGoodsID;
                detail.description = this.viewPPInvoice[i].materialGoodsCode;
                detail.lotNo = this.viewPPInvoice[i].lotNo;
                detail.expiryDate = this.viewPPInvoice[i].expiryDate;
                detail.materialGoods = this.materialGoodss.find(n => n.id === this.viewPPInvoice[i].materialGoodsID);
                detail.repositoryID = detail.materialGoods.repositoryID;
                this.translateService.get(['ebwebApp.purchaseOrder.vatTax']).subscribe((res: any) => {
                    detail.vATDescription = res['ebwebApp.purchaseOrder.vatTax'] + ' ' + detail.materialGoods.materialGoodsCode;
                });
                if (this.account.systemOption.some(x => x.code === PP_TINH_GIA_XUAT_KHO && x.data === CALCULATE_OW.DICH_DANH_CODE)) {
                    detail.oWPrice = this.viewPPInvoice[i].unitPrice;
                    detail.confrontID = this.viewPPInvoice[i].id;
                    detail.confrontDetailID = this.viewPPInvoice[i].pPInvoiceDetailID;
                }
                if (this.discountAccountList.some(x => x.accountNumber === this.discountAccountDefault)) {
                    detail.discountAccount = this.discountAccountDefault;
                }
                if (this.costAccountList.some(x => x.accountNumber === this.costAccountDefault)) {
                    detail.costAccount = this.costAccountDefault;
                }
                if (this.repositoryAccountList.some(x => x.accountNumber === this.repositoryAccountDefault)) {
                    detail.repositoryAccount = this.repositoryAccountDefault;
                }
                if (this.creditAccountList.some(x => x.accountNumber === this.creditAccountDefault)) {
                    detail.creditAccount = this.creditAccountDefault;
                }
                if (this.debitAccountList.some(x => x.accountNumber === this.debitAccountDefault)) {
                    detail.debitAccount = this.debitAccountDefault;
                    detail.exportTaxAccountCorresponding = this.debitAccountDefault;
                }
                if (this.vatAccountList.some(x => x.accountNumber === this.vatAccountDefault)) {
                    detail.vATAccount = this.vatAccountDefault;
                }
                if (this.exportTaxAmountAccountList.some(x => x.accountNumber === this.exportTaxAmountAccountDefault)) {
                    detail.exportTaxAmountAccount = this.exportTaxAmountAccountDefault;
                }
                if (this.deductionDebitAccountList.some(x => x.accountNumber === this.deductionDebitAccountDefault)) {
                    detail.deductionDebitAccount = this.deductionDebitAccountDefault;
                }
                detail.units = this.units.filter(item => item.materialGoodsID === detail.materialGoodsID);
                if (this.viewPPInvoice[i].unitID) {
                    detail.unit = detail.units.find(x => x.id === this.viewPPInvoice[i].unitID);
                    detail.mainUnit = detail.units.find(x => x.id === this.viewPPInvoice[i].mainUnitID);
                    this.selectUnit(detail, true);
                    this.changeQuantity(detail);
                }
                this.sAInvoiceDetails.push(detail);
                if (this.viewVouchersSelected.filter(x => x.refID2 === detail.pPInvoiceID).length === 0) {
                    this.viewVouchersSelected.push({
                        id: null,
                        refID1: null,
                        refID2: detail.pPInvoiceID,
                        no: this.viewPPInvoice[i].no,
                        date: this.viewPPInvoice[i].date.format(DATE_FORMAT_SLASH),
                        postedDate: this.viewPPInvoice[i].date.format(DATE_FORMAT_SLASH),
                        reason: this.viewPPInvoice[i].reason,
                        typeID: TypeID.MUA_HANG,
                        typeGroupID: GROUP_TYPEID.GROUP_PPINVOICE,
                        attach: true
                    });
                }
            }
            this.sumAllList();
        });
        this.eventManager.subscribe('selectRSOutward', async ref => {
            this.viewRSOutward = ref.content;
            this.sAInvoiceDetails = [];
            this.viewVouchersSelected = this.viewVouchersSelected.filter(x => !x.attach);
            if (
                this.viewRSOutward &&
                this.viewRSOutward.length ===
                    this.viewRSOutward.filter(x => x.accountingObjectID === this.viewRSOutward[0].accountingObjectID).length
            ) {
                if (this.accountingObjects.some(x => x.id === this.viewRSOutward[0].accountingObjectID)) {
                    this.sAInvoice.accountingObjectID = this.viewRSOutward[0].accountingObjectID;
                    this.sAInvoice.accountingObjectName = this.viewRSOutward[0].accountingObjectName;
                    this.sAInvoice.accountingObjectAddress = this.viewRSOutward[0].accountingObjectAddress;
                    this.sAInvoice.contactName = this.viewRSOutward[0].contactName;
                    this.sAInvoice.employeeID = this.viewRSOutward[0].employeeID;
                    this.sAInvoice.accountingObject = this.accountingObjects.find(x => x.id === this.viewRSOutward[0].accountingObjectID);
                    this.changeAccountingName();
                }
            } else {
                this.sAInvoice.accountingObjectID = null;
                this.sAInvoice.accountingObjectName = null;
                this.sAInvoice.accountingObjectAddress = null;
                this.sAInvoice.contactName = null;
                this.sAInvoice.employeeID = null;
                this.sAInvoice.accountingObject = null;
            }
            for (let i = 0; i < this.viewRSOutward.length; i++) {
                const detail: ISAInvoiceDetails = {};
                detail.accountingObjectID = this.viewRSOutward[i].accountingObjectID;
                detail.accountingObject = this.accountingObjects.find(x => x.id === detail.accountingObjectID);
                detail.materialGoods = this.materialGoodss.find(n => n.id === this.viewRSOutward[i].materialGoodsID);
                detail.repositoryID = detail.materialGoods.repositoryID;
                detail.creditAccount = this.viewRSOutward[i].creditAccount;
                detail.debitAccount = this.viewRSOutward[i].debitAccount;
                detail.discountAccount = this.discountAccountDefault;
                detail.costAccount = this.costAccountDefault;
                detail.repositoryAccount = this.repositoryAccountDefault;
                detail.vATAccount = this.vatAccountDefault;
                detail.exportTaxAmountAccount = this.exportTaxAmountAccountDefault;
                detail.exportTaxAccountCorresponding = this.debitAccountDefault;
                detail.deductionDebitAccount = this.deductionDebitAccountDefault;
                this.selectChangeMaterialGoods(detail);
                detail.quantity = this.viewRSOutward[i].quantity;
                detail.mainUnitPrice = this.viewRSOutward[i].mainUnitPrice;
                detail.formula = this.viewRSOutward[i].formula;
                detail.description = this.viewRSOutward[i].description;
                detail.mainConvertRate = this.viewRSOutward[i].mainConvertRate;
                detail.rSInwardOutwardID = this.viewRSOutward[i].id;
                detail.rSInwardOutwardDetailID = this.viewRSOutward[i].rSInwardOutwardDetailID;
                detail.mainQuantity = this.viewRSOutward[i].mainQuantity;
                this.changeQuantity(detail);
                this.sAInvoiceDetails.push(detail);
                if (this.viewVouchersSelected.filter(x => x.refID2 === detail.rSInwardOutwardID).length === 0) {
                    this.viewVouchersSelected.push({
                        id: null,
                        refID1: null,
                        refID2: detail.rSInwardOutwardID,
                        no: this.viewRSOutward[i].no,
                        date: this.viewRSOutward[i].date.format(DATE_FORMAT_SLASH),
                        postedDate: this.viewRSOutward[i].date.format(DATE_FORMAT_SLASH),
                        reason: this.viewRSOutward[i].reason,
                        typeID: TypeID.NHAP_KHO,
                        typeGroupID: GROUP_TYPEID.GROUP_RSINWARD,
                        attach: true
                    });
                }
            }
            this.sumAllList();
        });
        this.eventManager.subscribe('selectRSTranfer', async ref => {
            this.viewRSTranfer = ref.content;
            this.sAInvoiceDetails = [];
            this.viewVouchersSelected = this.viewVouchersSelected.filter(x => !x.attach);
            for (let i = 0; i < this.viewRSTranfer.length; i++) {
                const detail: ISAInvoiceDetails = {};
                detail.quantity = this.viewRSTranfer[i].quantity;
                detail.mainUnitPrice = this.viewRSTranfer[i].mainUnitPrice;
                detail.unitPrice = this.viewRSTranfer[i].unitPrice;
                detail.unitPriceOriginal = this.viewRSTranfer[i].unitPriceOriginal;
                detail.formula = this.viewRSTranfer[i].formula;
                detail.description = this.viewRSTranfer[i].description;
                detail.mainConvertRate = this.viewRSTranfer[i].mainConvertRate;
                detail.rSTransferID = this.viewRSTranfer[i].id;
                detail.rSTransferDetailID = this.viewRSTranfer[i].rSTransferDetailID;
                detail.vATDescription = this.viewRSTranfer[i].vATDescription;
                detail.mainQuantity = this.viewRSTranfer[i].mainQuantity;
                detail.materialGoodsID = this.viewRSTranfer[i].materialGoodsID;
                detail.description = this.viewRSTranfer[i].materialGoodsCode;
                detail.materialGoods = this.materialGoodss.find(n => n.id === this.viewRSTranfer[i].materialGoodsID);
                detail.repositoryID = detail.materialGoods.repositoryID;
                if (this.discountAccountList.some(x => x.accountNumber === this.discountAccountDefault)) {
                    detail.discountAccount = this.discountAccountDefault;
                }
                if (this.costAccountList.some(x => x.accountNumber === this.costAccountDefault)) {
                    detail.costAccount = this.costAccountDefault;
                }
                if (this.repositoryAccountList.some(x => x.accountNumber === this.repositoryAccountDefault)) {
                    detail.repositoryAccount = this.repositoryAccountDefault;
                }
                if (this.creditAccountList.some(x => x.accountNumber === this.creditAccountDefault)) {
                    detail.creditAccount = this.creditAccountDefault;
                }
                if (this.debitAccountList.some(x => x.accountNumber === this.debitAccountDefault)) {
                    detail.debitAccount = this.debitAccountDefault;
                    detail.exportTaxAccountCorresponding = this.debitAccountDefault;
                }
                if (this.vatAccountList.some(x => x.accountNumber === this.vatAccountDefault)) {
                    detail.vATAccount = this.vatAccountDefault;
                }
                if (this.exportTaxAmountAccountList.some(x => x.accountNumber === this.exportTaxAmountAccountDefault)) {
                    detail.exportTaxAmountAccount = this.exportTaxAmountAccountDefault;
                }
                if (this.deductionDebitAccountList.some(x => x.accountNumber === this.deductionDebitAccountDefault)) {
                    detail.deductionDebitAccount = this.deductionDebitAccountDefault;
                }
                detail.units = this.units.filter(item => item.materialGoodsID === detail.materialGoodsID);
                if (this.viewRSTranfer[i].unitID) {
                    detail.unit = detail.units.find(x => x.id === this.viewRSTranfer[i].unitID);
                    detail.mainUnit = detail.units.find(x => x.id === this.viewRSTranfer[i].mainUnitID);
                    this.selectUnit(detail, true);
                    this.changeQuantity(detail);
                }
                this.sAInvoiceDetails.push(detail);
                if (this.viewVouchersSelected.filter(x => x.refID2 === detail.rSTransferID).length === 0) {
                    this.viewVouchersSelected.push({
                        id: null,
                        refID1: null,
                        refID2: detail.rSTransferID,
                        no: this.viewRSTranfer[i].no,
                        date: this.viewRSTranfer[i].date.format(DATE_FORMAT_SLASH),
                        postedDate: this.viewRSTranfer[i].date.format(DATE_FORMAT_SLASH),
                        reason: this.viewRSTranfer[i].reason,
                        typeID: TypeID.CHUYEN_KHO,
                        typeGroupID: GROUP_TYPEID.GROUP_RSTRANFER,
                        attach: true
                    });
                }
            }
            this.sumAllList();
        });
        this.eventSubscriber = this.eventManager.subscribe('updateDiscountAllocations', response => {
            const discountAllocations: IDiscountAllocation[] = response.content;
            for (let i = 0; i < discountAllocations.length; i++) {
                for (let j = 0; j < this.sAInvoiceDetails.length; j++) {
                    if (discountAllocations[i].object === this.sAInvoiceDetails[j]) {
                        this.sAInvoiceDetails[j].discountAmountOriginal = discountAllocations[i].discountAmountOriginal;
                        this.sAInvoiceDetails[j].discountRate =
                            100 * this.sAInvoiceDetails[j].discountAmountOriginal / this.sAInvoiceDetails[j].amountOriginal;
                        this.changeDiscountAmountOriginal(this.sAInvoiceDetails[j]);
                    }
                }
            }
        });
    }

    deleteInvoiceNo() {
        if (!this.sAInvoice.isExportInvoice) {
            this.sAInvoice.invoiceNo = null;
        }
    }

    openModel() {
        this.modalRef = this.exportInvoiceModalService.open(
            this.sAInvoiceDetails,
            this.sAInvoice.currencyID,
            this.sAInvoice.accountingObjectID,
            GROUP_TYPEID.GROUP_RSOUTWARD
        );
    }

    canDeactive() {
        if (!this.isCloseAll && this.isEdit) {
            return this.checkValidateClose();
        } else {
            return true;
        }
    }

    closes() {
        this.warningEmployee = false;
        this.warningVatRate = 0;
        this.isCloseAll = false;
        this.modalRef.close();
    }

    delete() {
        event.preventDefault();
        if (
            !this.isEdit &&
            !this.sAInvoice.recorded &&
            !this.checkCloseBook(this.account, this.sAInvoice.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            if (this.sAInvoice.isBill && this.saBill.invoiceForm === 0 && !this.isRequiredInvoiceNo) {
                if (this.saBill.invoiceNo) {
                    this.typeDelete = 2;
                } else {
                    this.typeDelete = 0;
                }
            } else if (this.sAInvoice.isBill) {
                this.typeDelete = 1;
            } else {
                this.typeDelete = 0;
            }
            this.modalRef = this.modalService.open(this.deleteItem, { size: 'lg', backdrop: 'static' });
        }
    }

    get sAInvoice() {
        return this._sAInvoice;
    }

    set sAInvoice(sAInvoice: ISAInvoice) {
        this._sAInvoice = sAInvoice;
    }

    /*Phiếu chi*/
    previousEditMCReceipt() {
        // goi service get by row num
        if (this.rowNumberMCReceipt !== this.countMCReceipt) {
            this.utilsService
                .findByRowNum({
                    id: this.mCReceiptID,
                    isNext: false,
                    typeID: this.TYPE_MC_RECEIPT,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMCReceipt>) => {
                        this.navigate(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    nextEditMCReceipt() {
        // goi service get by row num
        if (this.rowNumberMCReceipt !== 1) {
            this.utilsService
                .findByRowNum({
                    id: this.mCReceiptID,
                    isNext: true,
                    typeID: this.TYPE_MC_RECEIPT,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMCReceipt>) => {
                        this.navigate(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    navigate(imcReceipt: IMCReceipt) {
        switch (imcReceipt.typeID) {
            case this.TYPE_MC_RECEIPT:
            case this.TYPE_MC_RECEIPT_CUSTOM:
                this.router.navigate(['/mc-receipt', imcReceipt.id, 'edit']);
                break;
            case this.TYPE_MC_RECEIPT_FROM_SAINVOICE:
                this.mcReceiptService.find(imcReceipt.id).subscribe((res: HttpResponse<IMCReceipt>) => {
                    const sAInvoiceID = res.body.sAInvoiceID;
                    if (sAInvoiceID) {
                        this.router.navigate(['./ban-hang', 'thu-tien-ngay', sAInvoiceID, 'edit', 'from-mc-receipt', sAInvoiceID]);
                    }
                });
                break;
        }
    }

    /*Báo có*/
    previousEditMBDeposit() {
        // goi service get by row num
        if (this.rowNumberMBDeposit !== this.countMBDeposit) {
            this.utilsService
                .findByRowNum({
                    id: this.mBDepositID,
                    isNext: false,
                    typeID: this.TYPE_MB_DEPOSIT,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMBDeposit>) => {
                        this.navigateMBDeposit(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    nextEditMBDeposit() {
        // goi service get by row num
        if (this.rowNumberMBDeposit !== 1) {
            this.utilsService
                .findByRowNum({
                    id: this.mBDepositID,
                    isNext: true,
                    typeID: this.TYPE_MB_DEPOSIT,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMBDeposit>) => {
                        this.navigate(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    navigateMBDeposit(imbDeposit: IMBDeposit) {
        switch (imbDeposit.typeID) {
            case this.TYPE_MB_DEPOSIT:
            case this.TYPE_MB_DEPOSIT_CUSTOM:
                this.router.navigate(['/mb-deposit', imbDeposit.id, 'edit']);
                break;
            case this.TYPE_MB_DEPOSIT_FROM_SAINVOICE:
                this.mbDepositService.find(imbDeposit.id).subscribe((res: HttpResponse<IMBDeposit>) => {
                    const sAInvoiceID = res.body.sAInvoiceID;
                    if (sAInvoiceID) {
                        this.router.navigate(['./ban-hang', 'thu-tien-ngay', sAInvoiceID, 'edit', 'from-mb-deposit', sAInvoiceID]);
                    }
                });
                break;
        }
    }

    closeAllFromMCReceipt() {
        if (this.searchVoucherMCReceipt) {
            if (sessionStorage.getItem('page_MCReceipt')) {
                this.router.navigate(['/mc-receipt', 'hasSearch', '1'], {
                    queryParams: {
                        page: sessionStorage.getItem('page_MCReceipt'),
                        size: sessionStorage.getItem('size_MCReceipt'),
                        index: sessionStorage.getItem('index_MCReceipt')
                    }
                });
            } else {
                this.router.navigate(['/mc-receipt', 'hasSearch', '1']);
            }
        } else {
            if (sessionStorage.getItem('page_MCReceipt')) {
                this.router.navigate(['/mc-receipt'], {
                    queryParams: {
                        page: sessionStorage.getItem('page_MCReceipt'),
                        size: sessionStorage.getItem('size_MCReceipt'),
                        index: sessionStorage.getItem('index_MCReceipt')
                    }
                });
            } else {
                this.router.navigate(['/mc-receipt']);
            }
        }
    }

    closeAllFromMBDeposit() {
        if (this.searchVoucherMCReceipt) {
            this.router.navigate(['/mb-deposit', 'hasSearch', '1']);
        } else {
            this.router.navigate(['/mb-deposit']);
        }
    }

    // Phiếu thu

    exportPdf(isDownload, typeReports: number) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.sAInvoice.id,
                typeID: this.sAInvoice.typeID,
                typeReport: typeReports
            },
            isDownload
        );
        if (typeReports === 0) {
            this.toasService.success(
                this.translateService.instant('ebwebApp.mBDeposit.printing') +
                    this.translateService.instant('global.reportName.sAInvoicePXK') +
                    '...',
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 1) {
            this.toasService.success(
                this.translateService.instant('ebwebApp.mBDeposit.printing') +
                    this.translateService.instant('global.reportName.chungTuKeToan') +
                    '...',
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 3) {
            this.toasService.success(
                this.translateService.instant('ebwebApp.mBDeposit.printing') +
                    this.translateService.instant('global.reportName.chungTuKeToanQD') +
                    '...',
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 4) {
            this.toasService.success(
                this.translateService.instant('ebwebApp.mBDeposit.printing') +
                    this.translateService.instant('ebwebApp.mBDeposit.creditNote') +
                    '...',
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 10) {
            this.toasService.success(
                this.translateService.instant('ebwebApp.mBDeposit.printing') +
                    this.translateService.instant('ebwebApp.sAInvoice.vatInvoice') +
                    '...',
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 11) {
            this.toasService.success(
                this.translateService.instant('ebwebApp.mBDeposit.printing') +
                    this.translateService.instant('ebwebApp.sAInvoice.saleInvoice') +
                    '...',
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 12) {
            this.toasService.success(
                this.translateService.instant('ebwebApp.mBDeposit.printing') +
                    this.translateService.instant('ebwebApp.outWard.billOutWrad') +
                    '...',
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 13) {
            this.toasService.success(
                this.translateService.instant('ebwebApp.mBDeposit.printing') +
                    this.translateService.instant('ebwebApp.mCReceipt.print.mCReceipt') +
                    '...',
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 14) {
            this.toasService.success(
                this.translateService.instant('ebwebApp.mBDeposit.printing') +
                    this.translateService.instant('ebwebApp.mCReceipt.print.mCReceiptA5') +
                    '...',
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 15) {
            this.toasService.success(
                this.translateService.instant('ebwebApp.mBDeposit.printing') +
                    this.translateService.instant('ebwebApp.sAInvoice.GoodsAndService') +
                    '...',
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 16) {
            this.toasService.success(
                this.translateService.instant('ebwebApp.mBDeposit.printing') +
                    this.translateService.instant('ebwebApp.mCReceipt.print.billOutWradA5') +
                    '...',
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
        }
    }

    addIfLastInput(i) {
        if (i === this.sAInvoiceDetails.length - 1) {
            this.addNewRow();
        }
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.sAInvoiceDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.sAInvoice;
    }

    addDataToDetail() {
        this.sAInvoiceDetails = this.details ? this.details : this.sAInvoiceDetails;
        this.sAInvoice = this.parent ? this.parent : this.sAInvoice;
        if (this.sAInvoice.bankAccountDetailID) {
            this.mBDeposit.bankAccountDetailID = this.sAInvoice.bankAccountDetailID;
            this.mBDeposit.bankName = this.sAInvoice.bankName;
        }
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
                    this.sAInvoiceDetails[this.currentRow].quantity = quantity;
                    this.sAInvoiceDetails[this.currentRow].repositoryID = groupData[i].iWRepositoryID;
                    this.sAInvoiceDetails[this.currentRow].materialGoodsSpecificationsLedgers =
                        groupData[i].materialGoodsSpecificationsLedgers;
                } else {
                    const sAInvoiceDetail = { ...this.sAInvoiceDetails[this.currentRow] };
                    sAInvoiceDetail.quantity = quantity;
                    sAInvoiceDetail.repositoryID = groupData[i].iWRepositoryID;
                    sAInvoiceDetail.materialGoodsSpecificationsLedgers = groupData[i].materialGoodsSpecificationsLedgers;
                    this.sAInvoiceDetails.push(sAInvoiceDetail);
                }
            }
        });
    }

    viewMaterialGoodsSpecification(detail) {
        if (detail.materialGoods) {
            if (detail.materialGoods.isFollow) {
                this.refModalService.open(
                    detail,
                    EbMaterialGoodsSpecificationsModalComponent,
                    detail.materialGoodsSpecificationsLedgers,
                    false,
                    2
                );
            } else {
                this.toasService.error(this.translateService.instant('ebwebApp.materialGoodsSpecifications.notFollow'));
            }
        } else {
            this.toasService.error(this.translateService.instant('ebwebApp.materialGoodsSpecifications.noMaterialGoods'));
        }
    }
}
