import { AfterViewChecked, AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';

import { IMCPayment } from 'app/shared/model/mc-payment.model';
import { MCPaymentService } from './mc-payment.service';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { ITransportMethod } from 'app/shared/model/transport-method.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { TransportMethodService } from 'app/entities/transport-method';
import { PaymentClauseService } from 'app/entities/payment-clause';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { CurrencyService } from 'app/danhmuc/currency';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { CostSetService } from 'app/entities/cost-set';
import { ExpenseItemService } from 'app/entities/expense-item';
import { IMCPaymentDetails } from 'app/shared/model/mc-payment-details.model';
import { IMCPaymentDetailTax } from 'app/shared/model/mc-payment-detail-tax.model';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { EMContractService } from 'app/entities/em-contract';
import { MBTellerPaperDetails } from 'app/shared/model/mb-teller-paper-details.model';
import { MBTellerPaperDetailTax } from 'app/shared/model/mb-teller-paper-detail-tax.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { BudgetItemService } from 'app/entities/budget-item';
import { Principal } from 'app/core';
import {
    AccountType,
    CategoryName,
    DDSo_NgoaiTe,
    DDSo_TienVND,
    DDSo_TyLe,
    MSGERROR,
    REPORT,
    SO_LAM_VIEC,
    SU_DUNG_SO_QUAN_TRI,
    TCKHAC_GhiSo,
    TypeID
} from 'app/app.constants';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { AccountListService } from 'app/danhmuc/account-list';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { DATE_FORMAT } from 'app/shared';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { IMCPaymentDetailVendor } from 'app/shared/model/mc-payment-detail-vendor.model';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { ROLE } from 'app/role.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { InvoiceType } from 'app/shared/model/invoice-type.model';
import { IAReportService } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/ia-report.service';

@Component({
    selector: 'eb-mc-payment-update',
    templateUrl: './mc-payment-update.component.html',
    styleUrls: ['./mc-payment-update.component.css']
})
export class MCPaymentUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewChecked, AfterViewInit {
    @ViewChild('content') public modalComponent: NgbModalRef;
    @ViewChild('contentAmount') public modalComponentAmount: NgbModalRef;
    private TYPE_MC_PAYMENT = 110;
    public TYPE_MC_PAYMENT_NCC = 116;
    private TYPE_GROUP_MC_PAYMENT = 11;
    private TYPE_PPSERVICE = 114;
    private TYPE_PPINVOICE_MHQK = 115;
    private TYPE_PP_INVOICE = 210;
    private TYPE_PP_SERVICE = 240;
    private TYPE_PP_DISCOUNT_RETURN = 230;
    private TYPE_FA_INCREAMENT = 610;
    private TYPE_TI_INCREAMENT = 510;
    DDSo_NgoaiTe = DDSo_NgoaiTe;
    DDSo_TienVND = DDSo_TienVND;
    DDSo_TyLe = DDSo_TyLe;
    TCKHAC_GhiSo: boolean;
    account: any;
    reasonDefault = 'Chi tiền mặt';
    accountingObjectNameOld: string;
    accountingObjectIDOld: string;
    private _mCPayment: IMCPayment;
    isSaving: boolean;
    dateDp: any;
    postedDateDp: any;
    // region Tiến lùi chứng từ
    rowNum: number;
    count: number;
    searchVoucher: ISearchVoucher;
    // endregion
    accountingObjectData: IAccountingObject[]; // Đối tượng
    currencys: ICurrency[]; //
    autoPrinciples: IAutoPrinciple[];
    autoPrinciple: IAutoPrinciple;
    isRecord: boolean;
    statusVoucher: number;
    // --------
    mCPaymentDetails: IMCPaymentDetails[];
    mCPaymentDetailTaxs: IMCPaymentDetailTax[];
    // right click declare
    contextmenu = { value: false };
    contextmenuX = { value: 0 };
    contextmenuY = { value: 0 };
    selectedDetail = { value: new MBTellerPaperDetails() };
    selectedDetailTax = { value: new MBTellerPaperDetailTax() };
    isShowDetail = { value: false };
    isShowDetailTax = { value: false };
    lstAccountingObjectTypes = [
        { value: 0, name: 'Nhà cung cấp' },
        { value: 1, name: 'Khách hàng' },
        { value: 2, name: 'Nhân viên' },
        { value: 3, name: 'Khác' }
    ];
    showVaoSo: boolean;
    isSoTaiChinh: boolean;
    modalRef: NgbModalRef;
    viewVouchersSelected: any;
    eventSubscriber: Subscription;
    creditAccountList: any;
    debitAccountList: any;
    vatAccountList: any;
    no: string; // số chứng từ
    mCPaymentCopy: IMCPayment;
    mCPaymentDetailsCopy: IMCPaymentDetails[];
    mCPaymentDetailTaxsCopy: IMCPaymentDetailTax[];
    goodsServicePurchases: IGoodsServicePurchase[];
    viewVouchersSelectedCopy: any;
    soDangLamViec: any;
    currencyCode: string;
    contextMenu: ContextMenu;
    select: number;
    dateStr: string;
    postedDateStr: string;
    validateDate: boolean;
    validatePostedDate: boolean;
    isCloseAll: boolean;
    isSaveAndNew: boolean;
    isViewFromRef: boolean;
    fromCloseForm: boolean;
    saveAndNewForCheckError: boolean;
    currency?: ICurrency;
    currentRow: number;

    /*Phiếu chi Nhà cung cấp*/
    mCPaymentDetailVendors: IMCPaymentDetailVendor[];

    /*Phiếu chi Nhà cung cấp*/

    /*Phân quyền*/
    ROLE = ROLE;
    ROLE_Them = ROLE.PhieuChi_Them;
    ROLE_Sua = ROLE.PhieuChi_Sua;
    ROLE_Xoa = ROLE.PhieuChi_Xoa;
    ROLE_GhiSo = ROLE.PhieuChi_GhiSo;
    ROLE_KetXuat = ROLE.PhieuChi_KetXuat;

    // accountDefaut
    creditAccountDefault: string;
    debitAccountDefault: string;
    vatAccountDefault: string;
    nameCategory: any;
    CategoryName = CategoryName;
    REPORT = REPORT;
    taxCalculationMethod: any;
    indexFocusDetailCol: any;
    indexFocusDetailRow: any;
    listIDInputDeatil: any[] = [
        'description_DT',
        'debitAccountList',
        'creditAccount',
        'amountOriginal_DT',
        'amount_DT',
        'comboboxaccountingObject_DT',
        'comboboxBankAccountDetailID',
        'comboboxexpenseItem_DT',
        'budgetItem_DT',
        'comboboxorganizationUnit_DT',
        'comboboxcostSet_DT',
        'comboboxstatisticsCode_DT'
    ];
    listIDInputDeatilTax: any[] = [
        'description_Tax',
        'vATAccount_DTT',
        'vATRate_DTax',
        'vATAmountOriginal_Tax',
        'vATAmount_Tax',
        'pretaxAmountOriginal_Tax',
        'pretaxAmount_Tax',
        'comboboxaccountingObject_DTTax',
        'accountingObjectName',
        'taxCode',
        'invoiceTemplate_Tax',
        'invoiceSeries_Tax',
        'invoiceNo_Tax',
        'invoiceDate_Tax',
        'goodsServicePurchaseId'
    ];
    invoiceTypes: InvoiceType[];

    constructor(
        private mCPaymentService: MCPaymentService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private accountingObjectService: AccountingObjectService,
        private transportMethodService: TransportMethodService,
        private paymentClauseService: PaymentClauseService,
        private autoPrinciplellService: AutoPrincipleService,
        private currencyService: CurrencyService,
        private organizationUnitService: OrganizationUnitService,
        private costsetService: CostSetService,
        private expenseItemService: ExpenseItemService,
        private gLService: GeneralLedgerService,
        public utilsService: UtilsService,
        private statisticsCodeService: StatisticsCodeService,
        private jhiAlertService: JhiAlertService,
        private eMContractService: EMContractService,
        private toastr: ToastrService,
        public translate: TranslateService,
        private bankAccountDetailsService: BankAccountDetailsService,
        private budgetItemService: BudgetItemService,
        private principal: Principal,
        private refModalService: RefModalService,
        private eventManager: JhiEventManager,
        private accountListService: AccountListService,
        private modalService: NgbModal,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        private viewVoucherService: ViewVoucherService,
        private iaReportService: IAReportService
    ) {
        super();
        this.principal.identity().then(account => {
            this.account = account;
            this.taxCalculationMethod = this.account.organizationUnit.taxCalculationMethod;
        });
        this.searchVoucher = JSON.parse(sessionStorage.getItem('searchVoucherMCPayment'));
        this.autoPrinciplellService.getAutoPrinciples().subscribe((res: HttpResponse<IAutoPrinciple[]>) => {
            this.autoPrinciples = res.body.filter(n => n.typeId === this.TYPE_MC_PAYMENT || n.typeId === 0).sort((n1, n2) => {
                if (n1.typeId > n2.typeId) {
                    return 1;
                }
                if (n1.typeId < n2.typeId) {
                    return -1;
                }
                return 0;
            });
        });
        this.organizationUnitService.getOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body
                .filter(n => n.unitType === 4)
                .sort((a, b) => a.organizationUnitCode.localeCompare(b.organizationUnitCode));
        });
        this.expenseItemService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body.sort((a, b) => a.expenseItemCode.localeCompare(b.expenseItemCode));
        });
        this.costsetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body.sort((a, b) => a.costSetCode.localeCompare(b.costSetCode));
        });
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticCodes = res.body.sort((a, b) => a.statisticsCode.localeCompare(b.statisticsCode));
        });
        this.bankAccountDetailsService.getBankAccountDetails().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
            this.bankAccountDetails = res.body.sort((a, b) => a.bankAccount.localeCompare(b.bankAccount));
        });
        this.budgetItemService.getBudgetItems().subscribe((res: HttpResponse<IBudgetItem[]>) => {
            this.budgetItems = res.body.sort((a, b) => a.budgetItemCode.localeCompare(b.budgetItemCode));
        });
        this.goodsServicePurchaseService.getGoodServicePurchases().subscribe((res: HttpResponse<IGoodsServicePurchase[]>) => {
            this.goodsServicePurchases = res.body;
        });
        this.iaReportService.queryInvoiceType().subscribe(res => {
            this.invoiceTypes = res.body;
        });
        this.getDataAccount();
        this.contextMenu = new ContextMenu();
        this.isViewFromRef = window.location.href.includes('/from-ref');
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    getDataAccount() {
        /*// tk có
        this.accountListService
            .getAccountType({
                typeID: 110,
                authoritiesCode: 'TCKHAC_HanCheTK',
                columnName: 'CreditAccount',
                valueCheck: '1',
                delimiter: ';',
                ppType: ''
            })
            .subscribe(res => {
                this.creditAccountList = res.body;
            });
        // tk nợ
        this.accountListService
            .getAccountType({
                typeID: 110,
                authoritiesCode: 'TCKHAC_HanCheTK',
                columnName: 'DebitAccount',
                valueCheck: '1',
                delimiter: ';',
                ppType: ''
            })
            .subscribe(res => {
                this.debitAccountList = res.body;
            });
        this.accountListService
            .getAccountType({
                typeID: 110,
                authoritiesCode: 'TCKHAC_HanCheTK',
                columnName: 'VATAccount',
                valueCheck: '1',
                delimiter: ';',
                ppType: '0'
            })
            .subscribe(res => {
                this.vatAccountList = res.body;
            });
        // TK thuế GTGT*/
        const columnList = [
            { column: AccountType.TK_CO, ppType: false },
            { column: AccountType.TK_NO, ppType: false },
            { column: AccountType.TK_THUE_GTGT, ppType: false }
        ];
        const param = {
            typeID: this.TYPE_MC_PAYMENT,
            columnName: columnList
        };
        this.accountListService.getAccountTypeFour(param).subscribe(res => {
            this.creditAccountList = res.body.creditAccount;
            this.debitAccountList = res.body.debitAccount;
            this.vatAccountList = res.body.vatAccount;
            this.creditAccountDefault = res.body.creditAccountDefault;
            this.debitAccountDefault = res.body.debitAccountDefault;
            this.vatAccountDefault = res.body.vatAccountDefault;
            const crdAcc = this.creditAccountList.find(n => n.accountNumber === this.creditAccountDefault);
            if (crdAcc) {
                if (crdAcc.isParentNode) {
                    this.creditAccountDefault = null;
                }
            }
            const dbAcc = this.debitAccountList.find(n => n.accountNumber === this.debitAccountDefault);
            if (dbAcc) {
                if (dbAcc.isParentNode) {
                    this.debitAccountDefault = null;
                }
            }
        });
    }

    getHopDong() {
        /*this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
            this.eMContracts = res.body.sort((a, b) =>
                (this.isSoTaiChinh ? a.noFBook : a.noMBook).localeCompare(this.isSoTaiChinh ? b.noFBook : b.noMBook)
            );
        });*/
    }

    ngOnInit() {
        this.isSaving = false;
        this.mCPaymentDetails = [];
        this.mCPaymentDetailTaxs = [];
        this.activatedRoute.data.subscribe(({ mCPayment }) => {
            this.mCPayment = mCPayment;
            this.viewVouchersSelected = mCPayment.viewVouchers ? mCPayment.viewVouchers : [];
            if (this.mCPayment.id !== undefined) {
                if (this.mCPayment.typeID === this.TYPE_PP_SERVICE) {
                    this.mCPaymentService.find(mCPayment.id).subscribe((res: HttpResponse<IMCPayment>) => {
                        const ppServiceID = res.body.ppServiceID;
                        if (ppServiceID) {
                            this.router.navigate(['./mua-dich-vu', ppServiceID, 'edit', 'from-mc-payment', mCPayment.id]);
                        }
                    });
                } else if (this.mCPayment.typeID === this.TYPE_PPINVOICE_MHQK) {
                    this.mCPaymentService.find(mCPayment.id).subscribe((res: HttpResponse<IMCPayment>) => {
                        const ppInvocieID = res.body.ppInvocieID;
                        if (res.body.storedInRepository) {
                            this.router.navigate(['./mua-hang', 'qua-kho', ppInvocieID, 'edit', 'from-mc-payment', mCPayment.id]);
                        } else {
                            this.router.navigate(['./mua-hang', 'khong-qua-kho', ppInvocieID, 'edit', 'from-mc-payment', mCPayment.id]);
                        }
                    });
                }
                if (this.account) {
                    this.showVaoSo = this.account.systemOption.some(x => x.code === SU_DUNG_SO_QUAN_TRI && x.data === '1');
                    this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                    if (this.isSoTaiChinh !== undefined) {
                        this.getHopDong();
                    }
                    this.no = this.isSoTaiChinh ? this.mCPayment.noFBook : this.mCPayment.noMBook;
                    this.soDangLamViec = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                    this.TCKHAC_GhiSo = this.account.systemOption.some(x => x.code === TCKHAC_GhiSo && x.data === '0');
                    this.currencyCode = this.account.organizationUnit.currencyID;
                }
                this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                    this.currencys = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                    this.currency = this.currencys.find(n => n.currencyCode === this.mCPayment.currencyID);
                });
                this.statusVoucher = 1;
                this.accountingObjectNameOld = this.mCPayment.accountingObjectName ? this.mCPayment.accountingObjectName : '';
                this.accountingObjectIDOld = this.mCPayment.accountingObjectID ? this.mCPayment.accountingObjectID : null;
                this.isEdit = false; // Xem chứng từ
                this.accountingObjectService.getAllAccountingObjectDTO().subscribe(
                    (res: HttpResponse<IAccountingObject[]>) => {
                        this.accountingObjectData = res.body.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                        this.employees = res.body
                            .filter(n => n.isEmployee)
                            .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                        if (!this.mCPayment.accountingObjectType && this.mCPayment.accountingObjectType !== 0) {
                            this.accountingObjects = res.body;
                        } else {
                            this.selectAccountingObjectType();
                        }
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
                this.mCPaymentDetails =
                    this.mCPayment.mcpaymentDetails === undefined
                        ? []
                        : this.mCPayment.mcpaymentDetails.sort((a, b) => a.orderPriority - b.orderPriority);
                this.mCPaymentDetailTaxs =
                    this.mCPayment.mcpaymentDetailTaxes === undefined
                        ? []
                        : this.mCPayment.mcpaymentDetailTaxes.sort((a, b) => a.orderPriority - b.orderPriority);
                this.mCPaymentDetailVendors =
                    this.mCPayment.mcpaymentDetailVendors === undefined
                        ? []
                        : this.mCPayment.mcpaymentDetailVendors.sort((a, b) => a.orderPriority - b.orderPriority);
                this.isRecord = this.mCPayment.recorded === undefined ? false : this.mCPayment.recorded;
                this.utilsService
                    .getIndexRow({
                        id: this.mCPayment.id,
                        isNext: true,
                        typeID: this.TYPE_MC_PAYMENT,
                        searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                    })
                    .subscribe(
                        (res: HttpResponse<any[]>) => {
                            this.rowNum = res.body[0];
                            if (res.body.length === 1) {
                                this.count = 1;
                            } else {
                                this.count = res.body[1];
                            }
                        },
                        (res: HttpErrorResponse) => this.onError(res.message)
                    );
            } else {
                this.no = null;
                this.statusVoucher = 0;
                this.isEdit = true; // thêm mới chứng từ
                this.mCPayment.reason = this.reasonDefault;
                this.accountingObjectNameOld = this.mCPayment.accountingObjectName ? this.mCPayment.accountingObjectName : '';
                this.accountingObjectIDOld = this.mCPayment.accountingObjectID ? this.mCPayment.accountingObjectID : null;
                this.mCPayment.totalAmount = 0;
                this.mCPayment.totalAmountOriginal = 0;
                // Set default theo systemOption
                if (this.account) {
                    this.mCPayment.date = this.utilsService.ngayHachToan(this.account);
                    this.showVaoSo = this.account.systemOption.some(x => x.code === SU_DUNG_SO_QUAN_TRI && x.data === '1');
                    this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                    if (this.isSoTaiChinh !== undefined) {
                        this.getHopDong();
                    }
                    this.TCKHAC_GhiSo = this.account.systemOption.some(x => x.code === TCKHAC_GhiSo && x.data === '0');
                    this.currencyCode = this.account.organizationUnit.currencyID;
                    this.mCPayment.postedDate = this.mCPayment.date;
                    this.changeDate();
                    this.soDangLamViec = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                    if (this.isSoTaiChinh) {
                        if (this.showVaoSo) {
                            this.mCPayment.typeLedger = 2;
                        } else {
                            this.mCPayment.typeLedger = 0;
                        }
                    } else {
                        this.mCPayment.typeLedger = 2;
                    }
                    this.mCPayment.accountingObjectType = 0;
                    if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                        this.mCPayment.currencyID = this.account.organizationUnit.currencyID;
                        this.mCPayment.exchangeRate = 1;
                        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                            this.currencys = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                            this.selectCurrency();
                        });
                    }
                    this.utilsService
                        .getGenCodeVoucher({
                            typeGroupID: this.TYPE_GROUP_MC_PAYMENT,
                            displayOnBook: this.soDangLamViec
                        })
                        .subscribe((res: HttpResponse<string>) => {
                            // this.mCReceipt.noFBook = (res.body.toString());
                            console.log(res.body);
                            this.no = res.body;
                            this.copy();
                        });
                }
                this.accountingObjectService.getAllDTO().subscribe(
                    (res: HttpResponse<IAccountingObject[]>) => {
                        this.accountingObjectData = res.body
                            .filter(n => n.isActive)
                            .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                        this.employees = res.body
                            .filter(n => n.isEmployee && n.isActive)
                            .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                        this.mCPayment.accountingObjectType = 0;
                        this.selectAccountingObjectType();
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
                this.mCPaymentDetails = [];
                this.mCPaymentDetailTaxs = [];
                this.mCPaymentDetailVendors = [];
                this.isRecord = false;
            }
        });
        this.registerRef();
        this.registerCombobox();
        this.registerAddNewRow();
        this.registerDeleteRow();
        this.registerCopyRow();
    }

    registerRef() {
        this.eventSubscriber = this.eventManager.subscribe('selectViewVoucher', response => {
            // if (this.statusVoucher === 1) {
            this.viewVouchersSelected = response.content;
            // }
        });
        this.eventSubscribers.push(this.eventSubscriber);
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = true;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilsService.setShowPopup(response.content);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    closeForm() {
        event.preventDefault();
        // window.history.back();
        if (this.mCPaymentCopy && (this.statusVoucher === 0 || this.statusVoucher === 1)) {
            if (
                !this.utilsService.isEquivalent(this.mCPayment, this.mCPaymentCopy) ||
                !this.utilsService.isEquivalentArray(this.mCPaymentDetails, this.mCPaymentDetailsCopy) ||
                !this.utilsService.isEquivalentArray(this.mCPaymentDetailTaxs, this.mCPaymentDetailTaxsCopy) ||
                !this.utilsService.isEquivalentArray(this.viewVouchersSelected, this.viewVouchersSelectedCopy)
            ) {
                this.fromCloseForm = true;
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(this.modalComponent, { backdrop: 'static' });
            } else {
                this.fromCloseForm = false;
                this.closeAll();
            }
        } else {
            this.fromCloseForm = false;
            this.closeAll();
        }
    }

    saveContent(noCheckNhapTien?) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.fillToSave();
        if (this.checkError(noCheckNhapTien)) {
            if (this.mCPayment.id !== undefined) {
                this.subscribeToSaveResponseWhenClose(this.mCPaymentService.update(this.mCPayment));
            } else {
                this.subscribeToSaveResponseWhenClose(this.mCPaymentService.create(this.mCPayment));
            }
        }
    }

    closeAll() {
        if (this.searchVoucher) {
            if (sessionStorage.getItem('page_MCPayment')) {
                this.router.navigate(['/mc-payment', 'hasSearch', '1'], {
                    queryParams: {
                        page: sessionStorage.getItem('page_MCPayment'),
                        size: sessionStorage.getItem('size_MCPayment'),
                        index: sessionStorage.getItem('index_MCPayment')
                    }
                });
            } else {
                this.router.navigate(['/mc-payment', 'hasSearch', '1']);
            }
        } else {
            if (sessionStorage.getItem('page_MCPayment')) {
                this.router.navigate(['/mc-payment'], {
                    queryParams: {
                        page: sessionStorage.getItem('page_MCPayment'),
                        size: sessionStorage.getItem('size_MCPayment'),
                        index: sessionStorage.getItem('index_MCPayment')
                    }
                });
            } else {
                this.router.navigate(['/mc-payment']);
            }
        }
    }

    close() {
        this.isCloseAll = true;
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.closeAll();
    }

    copy() {
        this.mCPaymentCopy = Object.assign({}, this.mCPayment);
        this.mCPaymentDetailsCopy = this.mCPaymentDetails.map(object => ({ ...object }));
        this.mCPaymentDetailTaxsCopy = this.mCPaymentDetailTaxs.map(object => ({ ...object }));
        this.viewVouchersSelectedCopy = this.viewVouchersSelected.map(object => ({ ...object }));
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuChi_Sua, ROLE.PhieuChi_Them])
    save(noCheckNhapTien?) {
        event.preventDefault();
        if (this.statusVoucher === 0 && !this.utilsService.isShowPopup) {
            this.fillToSave();
            if (this.checkError(noCheckNhapTien)) {
                if (this.mCPayment.id !== undefined) {
                    this.subscribeToSaveResponse(this.mCPaymentService.update(this.mCPayment));
                } else {
                    this.subscribeToSaveResponse(this.mCPaymentService.create(this.mCPayment));
                }
            }
        }
    }

    fillToSave() {
        this.mCPayment.viewVouchers = this.viewVouchersSelected;
        if (this.mCPayment.typeLedger.toString() === '0') {
            this.mCPayment.noFBook = this.no;
            this.mCPayment.noMBook = null;
        } else if (this.mCPayment.typeLedger.toString() === '1') {
            this.mCPayment.noMBook = this.no;
            this.mCPayment.noFBook = null;
        } else {
            if (this.isSoTaiChinh) {
                this.mCPayment.noFBook = this.no;
            } else {
                this.mCPayment.noMBook = this.no;
            }
        }
        if (!this.mCPayment.typeID) {
            this.mCPayment.typeID = this.TYPE_MC_PAYMENT;
        }
        this.isSaving = true;
        this.mCPayment.totalVATAmount = 0;
        this.mCPayment.totalVATAmountOriginal = 0;
        this.mCPayment.mcpaymentDetails = this.mCPaymentDetails;
        this.mCPayment.mcpaymentDetailTaxes = this.mCPaymentDetailTaxs;
        for (let i = 0; i < this.mCPayment.mcpaymentDetailTaxes.length; i++) {
            this.mCPayment.mcpaymentDetailTaxes[i].orderPriority = i + 1;
            this.mCPayment.mcpaymentDetailTaxes[i].vATAmountOriginal = this.round(
                this.mCPayment.mcpaymentDetailTaxes[i].vATAmount /
                    (this.currency.formula.includes('*') ? this.mCPayment.exchangeRate : 1 / this.mCPayment.exchangeRate),
                8
            );
            this.mCPayment.mcpaymentDetailTaxes[i].pretaxAmountOriginal = this.round(
                this.mCPayment.mcpaymentDetailTaxes[i].pretaxAmount /
                    (this.currency.formula.includes('*') ? this.mCPayment.exchangeRate : 1 / this.mCPayment.exchangeRate),
                8
            );
            this.mCPayment.totalVATAmount = this.mCPayment.totalVATAmount + this.mCPayment.mcpaymentDetailTaxes[i].vATAmount;
            this.mCPayment.totalVATAmountOriginal += this.mCPayment.mcpaymentDetailTaxes[i].vATAmountOriginal;
        }
        for (let i = 0; i < this.mCPayment.mcpaymentDetails.length; i++) {
            this.mCPayment.mcpaymentDetails[i].orderPriority = i + 1;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuChi_Them])
    saveAndNew(noCheckNhapTien?) {
        event.preventDefault();
        if (this.statusVoucher === 0 && !this.utilsService.isShowPopup) {
            this.fillToSave();
            if (this.checkError(noCheckNhapTien)) {
                if (this.mCPayment.id !== undefined) {
                    this.subscribeToSaveResponseAndContinue(this.mCPaymentService.update(this.mCPayment));
                } else {
                    this.subscribeToSaveResponseAndContinue(this.mCPaymentService.create(this.mCPayment));
                }
                this.statusVoucher = 0;
                this.isEdit = true;
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuChi_Them])
    copyAndNew() {
        event.preventDefault();
        if (this.statusVoucher === 1 && !this.utilsService.isShowPopup) {
            this.mCPayment.id = undefined;
            this.mCPayment.recorded = false;
            this.no = null;
            this.mCPayment.noMBook = null;
            this.mCPayment.noFBook = null;
            this.isRecord = false;
            for (let i = 0; i < this.mCPaymentDetails.length; i++) {
                this.mCPaymentDetails[i].id = undefined;
            }
            for (let i = 0; i < this.mCPaymentDetailTaxs.length; i++) {
                this.mCPaymentDetailTaxs[i].id = undefined;
            }
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: this.TYPE_GROUP_MC_PAYMENT,
                    displayOnBook: this.soDangLamViec
                })
                .subscribe((res: HttpResponse<string>) => {
                    // this.mCReceipt.noFBook = (res.body.toString());
                    console.log(res.body);
                    this.no = res.body;
                });
            this.statusVoucher = 0;
            this.isEdit = true;
            this.mCPaymentCopy = {};
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuChi_Sua])
    edit() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.mCPayment.postedDate) && !this.utilsService.isShowPopup) {
            if (this.statusVoucher === 1 && !this.mCPayment.recorded) {
                this.statusVoucher = 0;
                this.isEdit = true;
                this.copy();
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuChi_Them])
    addNew($event = null) {
        if (!this.utilsService.isShowPopup) {
            this.router.navigate(['/mc-payment', 'new']);
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.mCPayment.id = res.body.mCPayment.id;
                    this.mCPayment.recorded = res.body.mCPayment.recorded;
                    this.mCPaymentCopy = null;
                    this.router.navigate(['/mc-payment', res.body.mCPayment.id, 'edit']);
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                } else if (res.body.status === 2) {
                    if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.mCPayment.id = res.body.mCPayment.id;
                        this.mCPayment.recorded = res.body.mCPayment.recorded;
                        this.mCPaymentCopy = null;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyQT'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.mCPayment.id = res.body.mCPayment.id;
                        this.mCPayment.recorded = res.body.mCPayment.recorded;
                        this.mCPaymentCopy = null;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyTC'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.mCPayment.id = res.body.mCPayment.id;
                        this.mCPayment.recorded = res.body.mCPayment.recorded;
                        this.mCPaymentCopy = null;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuy'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else {
                        this.recordFailed();
                    }
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private subscribeToSaveResponseWhenClose(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.closeAll();
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                } else if (res.body.status === 2) {
                    if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.closeAll();
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyQT'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.closeAll();
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyTC'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.closeAll();
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuy'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else {
                        this.recordFailed();
                    }
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private subscribeToSaveResponseAndContinue(result: Observable<HttpResponse<any>>) {
        this.isSaveAndNew = true;
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.mCPayment.id = res.body.mCPayment.id;
                    this.mCPayment.recorded = res.body.mCPayment.recorded;
                    this.router.navigate(['/mc-payment', res.body.mCPayment.id, 'edit']).then(() => {
                        this.router.navigate(['/mc-payment', 'new']);
                        this.isSaveAndNew = false;
                    });
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                } else if (res.body.status === 2) {
                    if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.mCPayment.id = res.body.mCPayment.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyQT'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.router.navigate(['/mc-payment', res.body.mCPayment.id, 'edit']).then(() => {
                            this.router.navigate(['/mc-payment', 'new']);
                            this.isSaveAndNew = false;
                        });
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.mCPayment.id = res.body.mCPayment.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyTC'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.router.navigate(['/mc-payment', res.body.mCPayment.id, 'edit']).then(() => {
                            this.router.navigate(['/mc-payment', 'new']);
                            this.isSaveAndNew = false;
                        });
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.mCPayment.id = res.body.mCPayment.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuy'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.router.navigate(['/mc-payment', res.body.mCPayment.id, 'edit']).then(() => {
                            this.router.navigate(['/mc-payment', 'new']);
                            this.isSaveAndNew = false;
                        });
                    } else {
                        this.recordFailed();
                    }
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private noVoucherExist() {
        this.toastr.error(
            this.translate.instant('global.data.noVocherAlreadyExist'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
    }

    private recordFailed() {
        this.toastr.error(this.translate.instant('global.data.recordFailed'), this.translate.instant('ebwebApp.mCReceipt.home.message'));
    }

    trackAccountingObjectById(index: number, item: IAccountingObject) {
        return item.id;
    }

    trackExpenseItemById(index: number, item: IExpenseItem) {
        return item.id;
    }

    trackOrganizationUnitById(index: number, item: IOrganizationUnit) {
        return item.id;
    }

    trackCostSetById(index: number, item: ICostSet) {
        return item.id;
    }

    trackTransportMethodById(index: number, item: ITransportMethod) {
        return item.id;
    }

    trackPaymentClauseById(index: number, item: IPaymentClause) {
        return item.id;
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.statusVoucher = 1;
        this.isEdit = false;
        this.toastr.success(
            this.translate.instant('ebwebApp.mCReceipt.home.saveSuccess'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
        // this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    get mCPayment() {
        return this._mCPayment;
    }

    set mCPayment(mCPayment: IMCPayment) {
        this._mCPayment = mCPayment;
    }

    selectAccountingObject() {
        if (this.mCPayment.accountingObjectID !== null && this.mCPayment.accountingObjectID !== undefined) {
            const acc: IAccountingObject = this.accountingObjects.find(n => n.id === this.mCPayment.accountingObjectID);
            this.mCPayment.accountingObjectName = acc.accountingObjectName;
            if (
                this.mCPayment.reason === this.reasonDefault ||
                this.mCPayment.reason === this.reasonDefault + ' cho ' + this.accountingObjectNameOld
            ) {
                this.mCPayment.reason = this.reasonDefault + ' cho ' + this.mCPayment.accountingObjectName;
                this.mCPaymentDetails.forEach(mc => {
                    mc.description = this.mCPayment.reason;
                });
            }
            this.accountingObjectNameOld = acc.accountingObjectName;
            this.mCPayment.accountingObjectAddress = acc.accountingObjectAddress;
            this.mCPayment.taxCode = acc.taxCode;
            this.mCPayment.receiver = acc.contactName;
            for (const dt of this.mCPaymentDetails) {
                if (!dt.accountingObject || this.accountingObjectIDOld === dt.accountingObject.id) {
                    dt.accountingObject = acc;
                }
            }
            for (const dtt of this.mCPaymentDetailTaxs) {
                if (!dtt.accountingObject || this.accountingObjectIDOld === dtt.accountingObject.id) {
                    dtt.accountingObject = acc;
                    dtt.accountingObjectName = acc.accountingObjectName;
                    dtt.taxCode = acc.taxCode;
                }
            }
            this.accountingObjectIDOld = this.mCPayment.accountingObjectID;
        } else {
            this.mCPayment.accountingObjectAddress = null;
            this.mCPayment.taxCode = null;
            this.mCPayment.receiver = null;
            this.mCPayment.accountingObjectName = null;
            this.mCPayment.reason = this.reasonDefault;
            this.mCPaymentDetails.forEach(n => {
                if (this.accountingObjectIDOld === n.accountingObject.id) {
                    n.accountingObject = null;
                }
            });
            this.mCPaymentDetailTaxs.forEach(n => {
                if (this.accountingObjectIDOld === n.accountingObject.id) {
                    n.accountingObject = null;
                }
            });
        }
    }

    selectAutoPrinciple() {
        if (this.autoPrinciple && this.autoPrinciple.typeId !== 0) {
            this.mCPayment.reason = this.autoPrinciple.autoPrincipleName;
            for (const dt of this.mCPaymentDetails) {
                dt.debitAccount = this.autoPrinciple.debitAccount;
                dt.creditAccount = this.autoPrinciple.creditAccount;
                dt.description = this.autoPrinciple.autoPrincipleName;
            }
        } else if (this.autoPrinciple && this.autoPrinciple.typeId === 0) {
            this.mCPayment.reason = this.autoPrinciple.autoPrincipleName;
        }
    }

    selectCurrency() {
        if (this.mCPayment.currencyID) {
            this.currency = this.currencys.find(n => n.currencyCode === this.mCPayment.currencyID);
            this.mCPayment.exchangeRate = this.currencys.find(n => n.currencyCode === this.mCPayment.currencyID).exchangeRate;
            this.mCPayment.totalAmount = 0;
            // this.mCReceipt.totalAmountOriginal = 0;
            for (let i = 0; i < this.mCPaymentDetails.length; i++) {
                if (this.mCPayment.currencyID === this.currencyCode) {
                    this.mCPaymentDetails[i].amount = this.mCPaymentDetails[i].amountOriginal;
                } else {
                    this.mCPaymentDetails[i].amount = this.round(
                        this.mCPaymentDetails[i].amountOriginal *
                            (this.currency.formula.includes('*') ? this.mCPayment.exchangeRate : 1 / this.mCPayment.exchangeRate),
                        7
                    );
                }
                // this.mCPayment.totalAmount = this.mCPayment.totalAmount + this.mCPaymentDetails[i].amount;
            }
            for (let i = 0; i < this.mCPaymentDetailTaxs.length; i++) {
                if (this.mCPayment.currencyID === this.currencyCode) {
                    this.mCPaymentDetailTaxs[i].vATAmount = this.mCPaymentDetailTaxs[i].vATAmountOriginal;
                    this.mCPaymentDetailTaxs[i].pretaxAmount = this.mCPaymentDetailTaxs[i].pretaxAmountOriginal;
                } else {
                    this.mCPaymentDetailTaxs[i].vATAmount = this.round(
                        this.mCPaymentDetailTaxs[i].vATAmount *
                            (this.currency.formula.includes('*') ? this.mCPayment.exchangeRate : 1 / this.mCPayment.exchangeRate),
                        7
                    );
                    this.mCPaymentDetailTaxs[i].pretaxAmount = this.round(
                        this.mCPaymentDetailTaxs[i].pretaxAmount *
                            (this.currency.formula.includes('*') ? this.mCPayment.exchangeRate : 1 / this.mCPayment.exchangeRate),
                        7
                    );
                    // this.mCPaymentDetailTaxs[i].vATAmountOriginal =
                    // this.round(this.mCPaymentDetailTaxs[i].vATAmount / this.mCPayment.exchangeRate, this.account, 8);
                    // this.mCPaymentDetailTaxs[i].pretaxAmountOriginal =
                    // this.round(this.mCPaymentDetailTaxs[i].pretaxAmount / this.mCPayment.exchangeRate, this.account, 8);
                }
            }
            this.updateMCPayment();
        }
    }

    changeExchangeRate() {
        if (!this.mCPayment.exchangeRate) {
            this.mCPayment.exchangeRate = 0;
        }
        if (this.mCPayment.currencyID) {
            this.mCPayment.totalAmount = 0;
            for (let i = 0; i < this.mCPaymentDetails.length; i++) {
                this.mCPaymentDetails[i].amount = this.round(
                    this.mCPaymentDetails[i].amountOriginal *
                        (this.currency.formula.includes('*') ? this.mCPayment.exchangeRate : 1 / this.mCPayment.exchangeRate),
                    7
                );
                this.mCPayment.totalAmount = this.round(this.mCPayment.totalAmount + this.mCPaymentDetails[i].amount, 7);
            }
            for (let i = 0; i < this.mCPaymentDetailTaxs.length; i++) {
                this.mCPaymentDetailTaxs[i].vATAmountOriginal = this.round(
                    this.mCPaymentDetailTaxs[i].vATAmount /
                        (this.currency.formula.includes('*') ? this.mCPayment.exchangeRate : 1 / this.mCPayment.exchangeRate),
                    this.getAmountOriginalType()
                );
                this.mCPaymentDetailTaxs[i].pretaxAmountOriginal = this.round(
                    this.mCPaymentDetailTaxs[i].pretaxAmount /
                        (this.currency.formula.includes('*') ? this.mCPayment.exchangeRate : 1 / this.mCPayment.exchangeRate),
                    this.getAmountOriginalType()
                );
            }
        }
    }

    // Loại đối tượng kế toán
    selectAccountingObjectType() {
        if (this.mCPayment.accountingObjectType || this.mCPayment.accountingObjectType === 0) {
            if (this.mCPayment.accountingObjectType === 0) {
                this.nameCategory = this.utilsService.NHA_CUNG_CAP;
                // Nhà cung cấp
                this.accountingObjects = this.accountingObjectData.filter(n => n.objectType === 0 || n.objectType === 2);
            } else if (this.mCPayment.accountingObjectType === 1) {
                this.nameCategory = this.utilsService.KHACH_HANG;
                // Khách hàng
                this.accountingObjects = this.accountingObjectData.filter(n => n.objectType === 1 || n.objectType === 2);
            } else if (this.mCPayment.accountingObjectType === 2) {
                this.nameCategory = this.utilsService.NHAN_VIEN;
                // Nhân viên
                this.accountingObjects = this.accountingObjectData.filter(n => n.isEmployee);
            } else if (this.mCPayment.accountingObjectType === 3) {
                this.nameCategory = this.utilsService.KHACH_HANG;
                // Khác
                this.accountingObjects = this.accountingObjectData.filter(n => n.objectType === 3);
            } else {
                this.nameCategory = this.utilsService.KHACH_HANG;
                this.accountingObjects = this.accountingObjectData;
            }
        }
        if (this.accountingObjects && this.accountingObjects.length > 0) {
            if (this.mCPayment.accountingObjectID && !this.accountingObjects.map(n => n.id).includes(this.mCPayment.accountingObjectID)) {
                this.mCPayment.accountingObjectID = null;
                this.mCPayment.accountingObjectName = null;
                this.mCPayment.accountingObjectAddress = null;
                this.mCPayment.taxCode = null;
                this.mCPayment.receiver = null;
            }
        } else {
            this.mCPayment.accountingObjectID = null;
            this.mCPayment.accountingObjectName = null;
            this.mCPayment.accountingObjectAddress = null;
            this.mCPayment.taxCode = null;
            this.mCPayment.receiver = null;
        }
    }

    AddnewRow(select: number, isRightClick?) {
        if (this.mCPayment.totalAmount === undefined) {
            this.mCPayment.totalAmount = 0;
        }
        if (this.mCPayment.totalAmountOriginal === undefined) {
            this.mCPayment.totalAmountOriginal = 0;
        }
        if (select === 0) {
            let lenght = 0;
            if (isRightClick) {
                this.mCPaymentDetails.splice(this.indexFocusDetailRow + 1, 0, {});
                lenght = this.indexFocusDetailRow + 2;
            } else {
                this.mCPaymentDetails.push({});
                lenght = this.mCPaymentDetails.length;
            }
            this.mCPaymentDetails[lenght - 1].amount = 0;
            this.mCPaymentDetails[lenght - 1].amountOriginal = 0;
            this.mCPaymentDetails[lenght - 1].cashOutExchangeRateFB = 0;
            this.mCPaymentDetails[lenght - 1].cashOutDifferAmountFB = 0;
            this.mCPaymentDetails[lenght - 1].cashOutAmountFB = 0;
            this.mCPaymentDetails[lenght - 1].cashOutExchangeRateMB = 0;
            this.mCPaymentDetails[lenght - 1].cashOutDifferAmountMB = 0;
            this.mCPaymentDetails[lenght - 1].cashOutAmountMB = 0;
            this.mCPaymentDetails[lenght - 1].description = this.mCPayment.reason ? this.mCPayment.reason : '';
            if (lenght > 1) {
                this.mCPaymentDetails[lenght - 1].debitAccount = this.mCPaymentDetails[lenght - 2].debitAccount;
                this.mCPaymentDetails[lenght - 1].creditAccount = this.mCPaymentDetails[lenght - 2].creditAccount;
                this.mCPaymentDetails[lenght - 1].accountingObject = this.mCPaymentDetails[lenght - 2].accountingObject;
                // this.mCPaymentDetails[lenght - 1].description = this.mCPaymentDetails[lenght - 2].description;
                this.mCPaymentDetails[lenght - 1].organizationUnit = this.mCPaymentDetails[lenght - 2].organizationUnit;
            } else {
                if (this.autoPrinciple && this.autoPrinciple.typeId !== 0) {
                    this.mCPaymentDetails[lenght - 1].debitAccount = this.autoPrinciple.debitAccount;
                    this.mCPaymentDetails[lenght - 1].creditAccount = this.autoPrinciple.creditAccount;
                } else {
                    this.mCPaymentDetails[lenght - 1].debitAccount = this.debitAccountDefault ? this.debitAccountDefault : null;
                    this.mCPaymentDetails[lenght - 1].creditAccount = this.creditAccountDefault ? this.creditAccountDefault : null;
                }
                if (this.mCPayment.accountingObjectID !== undefined && this.mCPayment.accountingObjectID !== null) {
                    this.mCPaymentDetails[lenght - 1].accountingObject = this.accountingObjects.find(
                        n => n.id === this.mCPayment.accountingObjectID
                    );
                }
            }
            if (isRightClick && this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const lst = this.listIDInputDeatil;
                const col = this.indexFocusDetailCol;
                const row = this.indexFocusDetailRow + 1;
                this.indexFocusDetailRow = row;
                setTimeout(() => {
                    const element: HTMLElement = document.getElementById(lst[col] + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            } else {
                const nameTag: string = event.srcElement.id;
                const idx: number = this.mCPaymentDetails.length - 1;
                const nameTagIndex: string = nameTag + String(idx);
                setTimeout(() => {
                    const element: HTMLElement = document.getElementById(nameTagIndex);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
        } else if (select === 1) {
            let lenght = 0;
            if (isRightClick) {
                this.mCPaymentDetailTaxs.splice(this.indexFocusDetailRow + 1, 0, {});
                lenght = this.indexFocusDetailRow + 2;
            } else {
                this.mCPaymentDetailTaxs.push({});
                lenght = this.mCPaymentDetailTaxs.length;
            }
            this.mCPaymentDetailTaxs[lenght - 1].vATAmount = 0;
            this.mCPaymentDetailTaxs[lenght - 1].vATAmountOriginal = 0;
            this.mCPaymentDetailTaxs[lenght - 1].vATAmount = 0;
            this.mCPaymentDetailTaxs[lenght - 1].vATAmountOriginal = 0;
            this.mCPaymentDetailTaxs[lenght - 1].pretaxAmount = 0;
            this.mCPaymentDetailTaxs[lenght - 1].pretaxAmountOriginal = 0;
            this.mCPaymentDetailTaxs[lenght - 1].vATAccount = this.vatAccountDefault ? this.vatAccountDefault : null;
            this.mCPaymentDetailTaxs[lenght - 1].description = 'Thuế GTGT';
            if (lenght > 1) {
                this.mCPaymentDetailTaxs[lenght - 1].vATAccount = this.mCPaymentDetailTaxs[lenght - 2].vATAccount;
                this.mCPaymentDetailTaxs[lenght - 1].vATRate = this.mCPaymentDetailTaxs[lenght - 2].vATRate;
                this.mCPaymentDetailTaxs[lenght - 1].description = this.mCPaymentDetailTaxs[lenght - 2].description;
                this.mCPaymentDetailTaxs[lenght - 1].accountingObject = this.mCPaymentDetailTaxs[lenght - 2].accountingObject;
                this.mCPaymentDetailTaxs[lenght - 1].accountingObjectName = this.mCPaymentDetailTaxs[lenght - 2].accountingObjectName;
                this.mCPaymentDetailTaxs[lenght - 1].taxCode = this.mCPaymentDetailTaxs[lenght - 2].taxCode;
                this.mCPaymentDetailTaxs[lenght - 1].goodsServicePurchaseID = this.mCPaymentDetailTaxs[lenght - 2].goodsServicePurchaseID;
            } else {
                if (this.mCPayment.accountingObjectID !== undefined && this.mCPayment.accountingObjectID !== null) {
                    const acc = this.accountingObjects.find(n => n.id === this.mCPayment.accountingObjectID);
                    this.mCPaymentDetailTaxs[lenght - 1].accountingObject = acc;
                    this.mCPaymentDetailTaxs[lenght - 1].accountingObjectName = acc.accountingObjectName;
                    this.mCPaymentDetailTaxs[lenght - 1].taxCode = acc.taxCode;
                }
                if (this.account) {
                    this.mCPaymentDetailTaxs[lenght - 1].goodsServicePurchaseID = this.account.organizationUnit.goodsServicePurchaseID;
                }
            }
            if (isRightClick) {
                const lst = this.listIDInputDeatilTax;
                const col = this.indexFocusDetailCol;
                const row = this.indexFocusDetailRow + 1;
                setTimeout(() => {
                    const element: HTMLElement = document.getElementById(lst[col] + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            } else {
                const nameTag: string = event.srcElement.id;
                const idx: number = this.mCPaymentDetailTaxs.length - 1;
                const nameTagIndex: string = nameTag + String(idx);
                setTimeout(() => {
                    const element: HTMLElement = document.getElementById(nameTagIndex);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
        } else {
        }
    }

    updateAmountTaxWithAccount() {
        /*for (let i = 0; i < this.mCPaymentDetailTaxs.length; i++) {
            if (this.mCPaymentDetailTaxs[i].vATAccount && this.mCPaymentDetailTaxs[i].vATAccount.startsWith('133', 0)) {
                if (
                    this.mCPaymentDetails.length > 0 &&
                    !this.mCPaymentDetails.find(n => n.debitAccount === this.mCPaymentDetailTaxs[i].vATAccount)
                ) {
                    this.mCPaymentDetailTaxs[i].pretaxAmount = 0;
                    this.mCPaymentDetailTaxs[i].vATAmount = 0;
                    this.mCPaymentDetailTaxs[i].vATAmountOriginal = 0;
                }
            }
        }*/
    }

    removeRow(detail: object, select: number) {
        if (select === 0) {
            const dtt: IMCPaymentDetailTax = this.mCPaymentDetailTaxs.find(
                n => n.vATAccount === detail['debitAccount'] && n.vATAccount !== '' && n.vATAccount !== null && n.vATAccount !== undefined
            );
            if (dtt) {
                if (dtt.vATAccount.startsWith('133', 0)) {
                    if (this.mCPaymentDetails.filter(n => n.debitAccount === dtt.vATAccount).length > 1) {
                        dtt.vATAmount -= detail['amount'];
                        dtt.vATAmountOriginal -= detail['amountOriginal'];
                        if (dtt.vATRate === 1) {
                            dtt.pretaxAmount = this.round(parseFloat(dtt.vATAmount.toString()) / 0.05, 7);
                            dtt.pretaxAmountOriginal = this.round(
                                parseFloat(dtt.pretaxAmount.toString()) /
                                    parseFloat(
                                        (this.currency.formula.includes('*')
                                            ? this.mCPayment.exchangeRate
                                            : 1 / this.mCPayment.exchangeRate
                                        ).toString()
                                    ),
                                8
                            );
                        } else if (dtt.vATRate === 2) {
                            dtt.pretaxAmount = this.round(parseFloat(dtt.vATAmount.toString()) / 0.1, 7);
                            dtt.pretaxAmountOriginal = this.round(parseFloat(dtt.vATAmount.toString()) / 0.1, 8);
                        } else {
                            dtt.pretaxAmount = 0;
                            dtt.pretaxAmountOriginal = 0;
                        }
                    } else {
                        dtt.vATAmount = 0;
                        dtt.vATAmountOriginal = 0;
                    }
                }
            }
            this.mCPaymentDetails.splice(this.mCPaymentDetails.indexOf(detail), 1);

            if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                // vì còn trường hợp = 0
                if (this.mCPaymentDetails.length > 0) {
                    let row = 0;
                    if (this.indexFocusDetailRow > this.mCPaymentDetails.length - 1) {
                        row = this.indexFocusDetailRow - 1;
                    } else {
                        row = this.indexFocusDetailRow;
                    }
                    const lst = this.listIDInputDeatil;
                    const col = this.indexFocusDetailCol;
                    setTimeout(() => {
                        const element: HTMLElement = document.getElementById(lst[col] + row);
                        if (element) {
                            element.focus();
                        }
                    }, 0);
                }
            }
        } else if (select === 1) {
            this.mCPaymentDetailTaxs.splice(this.mCPaymentDetailTaxs.indexOf(detail), 1);
            if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                // vì còn trường hợp = 0
                if (this.mCPaymentDetailTaxs.length > 0) {
                    let row = 0;
                    if (this.indexFocusDetailRow > this.mCPaymentDetailTaxs.length - 1) {
                        row = this.indexFocusDetailRow - 1;
                    } else {
                        row = this.indexFocusDetailRow;
                    }
                    const lst = this.listIDInputDeatilTax;
                    const col = this.indexFocusDetailCol;
                    setTimeout(() => {
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
        this.updateMCPayment();
    }

    updateMCPayment() {
        this.mCPayment.totalAmount = this.round(this.sumDT('amount'), 7);
        this.mCPayment.totalAmountOriginal = this.round(this.sumDT('amountOriginal'), 8);
        this.mCPayment.totalVATAmount = this.round(this.sumDTTax('vATAmount'), 7);
        this.mCPayment.totalVATAmountOriginal = this.round(this.sumDTTax('vATAmountOriginal'), 8);
    }

    sumDT(prop) {
        let total = 0;
        for (let i = 0; i < this.mCPaymentDetails.length; i++) {
            total += this.mCPaymentDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    sumDTTax(prop) {
        let total = 0;
        for (let i = 0; i < this.mCPaymentDetails.length; i++) {
            total += this.mCPaymentDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    KeyPress(detail, key, select?: number) {
        switch (key) {
            case 'ctr + delete':
                this.removeRow(detail, select);
                break;
            case 'ctr + c':
                this.copyRow(detail, select, true);
                break;
            case 'ctr + insert':
                this.AddnewRow(select, true);
                break;
        }
    }

    copyRow(detail, select, fromKeyDown?) {
        if (!this.getSelectionText() || !fromKeyDown) {
            if (select === 0) {
                const detailCopy: any = Object.assign({}, detail);
                detailCopy.id = null;
                this.mCPaymentDetails.push(detailCopy);
                this.calcular(
                    detailCopy,
                    this.TYPE_MC_PAYMENT,
                    'amountOriginal',
                    this.mCPayment,
                    this.mCPaymentDetails,
                    this.mCPaymentDetailTaxs
                );
                if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                    const lst = this.listIDInputDeatil;
                    const col = this.indexFocusDetailCol;
                    const row = this.mCPaymentDetails.length - 1;
                    this.indexFocusDetailRow = row;
                    setTimeout(() => {
                        const element: HTMLElement = document.getElementById(lst[col] + row);
                        if (element) {
                            element.focus();
                        }
                    }, 0);
                }
            } else {
                const detailCopy: any = Object.assign({}, detail);
                detailCopy.id = null;
                this.mCPaymentDetailTaxs.push(detailCopy);
                this.calcular(
                    detailCopy,
                    this.TYPE_MC_PAYMENT,
                    'vATAmount',
                    this.mCPayment,
                    this.mCPaymentDetails,
                    this.mCPaymentDetailTaxs
                );
                if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                    const lst = this.listIDInputDeatil;
                    const col = this.indexFocusDetailCol;
                    const row = this.mCPaymentDetailTaxs.length - 1;
                    this.indexFocusDetailRow = row;
                    setTimeout(() => {
                        const element: HTMLElement = document.getElementById(lst[col] + row);
                        if (element) {
                            element.focus();
                        }
                    }, 0);
                }
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuChi_GhiSo])
    record() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.mCPayment.postedDate) && !this.utilsService.isShowPopup) {
            if (this.statusVoucher === 1 && !this.mCPayment.recorded) {
                const record_: Irecord = {};
                record_.id = this.mCPayment.id;
                record_.typeID = this.mCPayment.typeID;
                this.gLService.record(record_).subscribe((res: HttpResponse<Irecord>) => {
                    console.log(JSON.stringify(res.body));
                    if (res.body.success) {
                        this.mCPayment.recorded = true;
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
                });
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuChi_GhiSo])
    unrecord() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.mCPayment.postedDate) && !this.utilsService.isShowPopup) {
            if (this.statusVoucher === 1 && this.mCPayment.recorded) {
                const record_: Irecord = {};
                record_.id = this.mCPayment.id;
                record_.typeID = this.mCPayment.typeID;
                this.gLService.unrecord(record_).subscribe((res: HttpResponse<Irecord>) => {
                    console.log(JSON.stringify(res.body));
                    if (res.body.success) {
                        this.mCPayment.recorded = false;
                    }
                });
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

    // Truyền giá trị thuế từ tab chi tiết sang tab thuế
    calcular(objcet1: object, typeID: number, name: string, objectParent: object, objectDT1: object[], objectDT2: object[]) {
        const count = this.mCPaymentDetailTaxs.length;
        this.utilsService.calcular(
            objcet1,
            typeID,
            name,
            objectParent,
            objectDT1,
            objectDT2,
            this.account,
            this.getAmountOriginalType(),
            this.currency.formula.includes('*') ? this.mCPayment.exchangeRate : 1 / this.mCPayment.exchangeRate
        );
        console.log(objectDT2.length);
        if (count < objectDT2.length) {
            if (this.mCPayment.accountingObjectID) {
                this.mCPaymentDetailTaxs[count].accountingObject = this.accountingObjects.find(
                    n => n.id === this.mCPayment.accountingObjectID
                );
            }
            if (this.account) {
                if (this.taxCalculationMethod !== 1) {
                    this.mCPaymentDetailTaxs[count].goodsServicePurchaseID = this.account.organizationUnit.goodsServicePurchaseID;
                }
            }
        }
        this.updateAmountTaxWithAccount();
        this.updateMCPayment();
    }

    exportPdf(isDownload, typeReports) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.mCPayment.id,
                typeID: this.mCPayment.typeID,
                typeReport: typeReports
            },
            isDownload
        );
        if (typeReports === 1) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.mBDeposit.financialPaper') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 2) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.mCPayment.home.title') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        }
    }

    // region Tiến lùi chứng từ
    // ham lui, tien
    previousEdit() {
        // goi service get by row num
        if (this.rowNum !== this.count) {
            this.utilsService
                .findByRowNum({
                    id: this.mCPayment.id,
                    isNext: false,
                    typeID: this.TYPE_MC_PAYMENT,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMCPayment>) => {
                        // this.OnInit(res.body);
                        // this.router.navigate(['/mc-payment', res.body.id, 'edit']);
                        // this.router.navigateByUrl('/', {skipLocationChange: true}).then(()=>
                        //     this.router.navigate(['/mc-payment', this.mCPayment.id, 'edit']));
                        // this.router.navigateByUrl('/mc-payment/' + this.mCPayment.id + '/edit');
                        this.navigate(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    nextEdit() {
        // goi service get by row num
        if (this.rowNum !== 1) {
            this.utilsService
                .findByRowNum({
                    id: this.mCPayment.id,
                    isNext: true,
                    typeID: this.TYPE_MC_PAYMENT,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMCPayment>) => {
                        // this.router.navigate(['/mc-payment', res.body.id, 'edit']);
                        this.navigate(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    navigate(imcPayment: IMCPayment) {
        switch (imcPayment.typeID) {
            case this.TYPE_MC_PAYMENT:
                this.router.navigate(['/mc-payment', imcPayment.id, 'edit']);
                break;
            case this.TYPE_PPSERVICE:
                this.mCPaymentService.find(imcPayment.id).subscribe((res: HttpResponse<IMCPayment>) => {
                    const ppServiceID = res.body.ppServiceID;
                    if (ppServiceID) {
                        this.router.navigate(['./mua-dich-vu', ppServiceID, 'edit', 'from-mc-payment', imcPayment.id]);
                    }
                });
                break;
            case this.TYPE_PPINVOICE_MHQK:
                this.mCPaymentService.find(imcPayment.id).subscribe((res: HttpResponse<IMCPayment>) => {
                    const ppInvocieID = res.body.ppInvocieID;
                    if (ppInvocieID) {
                        if (res.body.storedInRepository) {
                            this.router.navigate(['./mua-hang', 'qua-kho', ppInvocieID, 'edit', 'from-mc-payment', imcPayment.id]);
                        } else {
                            this.router.navigate(['./mua-hang', 'khong-qua-kho', ppInvocieID, 'edit', 'from-mc-payment', imcPayment.id]);
                        }
                    }
                });
                break;
        }
    }

    // endregion
    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    // right click event
    // activates the menu with the coordinates
    onrightClick(
        event,
        eventData: any,
        selectedObj: any,
        isShowTab1: any,
        isShowTab2: any,
        contextmenu: any,
        x: any,
        y: any,
        select: number
    ) {
        this.utilsService.onrightClick(event, eventData, selectedObj, isShowTab1, isShowTab2, contextmenu, x, y, select);
        console.log(event);
    }

    // disables the menu
    disableContextMenu() {
        this.contextmenu.value = false;
    }

    //  end of right click
    /*Check Error*/
    checkError(noCheckNhapTien?): boolean {
        // this.fromCloseForm = false;
        // this.saveAndNewForCheckError = false;
        if (this.mCPayment.taxCode) {
            if (!this.utilsService.checkMST(this.mCPayment.taxCode)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.taxCodeInvalid'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        if (this.checkCloseBook(this.account, this.mCPayment.postedDate)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.lockBook.err.checkForSave'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (this.mCPayment.employeeID) {
            if (!this.employees.find(n => n.id === this.mCPayment.employeeID)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.employeeIDNotExist'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        if (this.mCPayment.typeLedger === 0) {
            if (!this.mCPayment.noFBook) {
                this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
                return false;
            } else {
                if (!this.utilsService.checkNoBook(this.mCPayment.noFBook, this.account)) {
                    /*this.toastr.error(
                        this.translate.instant('global.data.noVoucherInvalid'),
                        this.translate.instant('ebwebApp.mCReceipt.error.error')
                    );*/
                    return false;
                }
            }
        } else if (this.mCPayment.typeLedger === 1) {
            if (!this.mCPayment.noMBook) {
                this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
                return false;
            } else {
                if (!this.utilsService.checkNoBook(this.mCPayment.noMBook, this.account)) {
                    return false;
                }
            }
        } else {
            if (this.isSoTaiChinh) {
                if (!this.mCPayment.noFBook) {
                    this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
                    return false;
                } else {
                    if (!this.utilsService.checkNoBook(this.mCPayment.noFBook, this.account)) {
                        return false;
                    }
                }
            } else {
                if (!this.mCPayment.noMBook) {
                    this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
                    return false;
                } else {
                    if (!this.utilsService.checkNoBook(this.mCPayment.noMBook, this.account)) {
                        return false;
                    }
                }
            }
        }
        if (!this.mCPayment.postedDate || !this.mCPayment.date) {
            this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        if (this.mCPayment.postedDate.format(DATE_FORMAT) < this.mCPayment.date.format(DATE_FORMAT)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.postedDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.mCPayment.currencyID) {
            this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }

        if (this.mCPaymentDetails.length === 0) {
            // Null phần chi tiết
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullDetail'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        const dt = this.mCPaymentDetails.find(
            n => n.creditAccount === null || n.creditAccount === undefined || n.debitAccount === undefined || n.debitAccount === null
        );
        const dtT = this.mCPaymentDetailTaxs.find(n => n.vATAccount === null || n.vATAccount === undefined);
        if (dt || dtT) {
            this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        const checkAcc = this.utilsService.checkAccoutWithDetailType(
            this.debitAccountList,
            this.creditAccountList,
            this.mCPaymentDetails,
            this.accountingObjectData,
            this.costSets,
            null,
            null,
            this.bankAccountDetails,
            this.organizationUnits,
            this.expenseItems,
            this.budgetItems,
            this.statisticCodes
        );
        if (checkAcc) {
            this.toastr.error(checkAcc, this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        if (!noCheckNhapTien) {
            if (!this.checkNhapTien()) {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(this.modalComponentAmount, { backdrop: 'static' });
                return false;
            }
        }
        return true;
    }

    closeModalComponentAmount() {
        if (this.fromCloseForm) {
            this.close();
        } else {
            this.modalRef.close();
        }
    }

    saveModalComponentAmount() {
        this.modalRef.close();
        if (this.fromCloseForm) {
            this.saveContent(true);
        } else if (this.saveAndNewForCheckError) {
            this.saveAndNew(true);
        } else {
            this.save(true);
        }
    }

    checkNhapTien() {
        /*Check nhập tiền giữa 2 tab*/
        let result = true;
        let keepGoing = true;
        // có bên hàng tiền mà không có bên thuế
        this.mCPaymentDetails.filter(n => n.debitAccount.startsWith('133')).forEach(n => {
            if (keepGoing) {
                if (this.mCPaymentDetailTaxs.filter(m => m.vATAccount === n.debitAccount).length === 0) {
                    result = false;
                    keepGoing = false;
                }
            }
        });
        // Có bên tab thuế mà k có bên hàng tiền
        this.mCPaymentDetailTaxs.filter(n => n.vATAccount.startsWith('133')).forEach(n => {
            if (keepGoing) {
                if (this.mCPaymentDetails.filter(m => m.debitAccount === n.vATAccount).length === 0) {
                    result = false;
                    keepGoing = false;
                }
            }
        });
        // fill tiền không hợp lệ
        this.mCPaymentDetails.forEach(n => {
            if (keepGoing) {
                const tax: IMCPaymentDetailTax[] = this.mCPaymentDetailTaxs.filter(
                    m => m.vATAccount === n.debitAccount && m.vATAccount !== null && m.vATAccount !== undefined && m.vATAccount !== ''
                );
                if (tax) {
                    if (tax.length > 0 && tax[0].vATAccount.startsWith('133', 0)) {
                        if (!n.amountOriginal && !tax.some(m => !m.vATAmount)) {
                            // this.toastr.error('Lỗi nhập tiền', this.translate.instant('ebwebApp.mCReceipt.error.error'));
                            result = false;
                            keepGoing = false;
                        }
                        if (result) {
                            const total133 = this.mCPaymentDetails
                                .filter(t => t.debitAccount === tax[0].vATAccount)
                                .map(t => t.amount)
                                .reduce((a, b) => a + b);
                            const total133Tax = tax.map(t => t.vATAmount).reduce((a, b) => a + b);
                            if (total133 !== total133Tax) {
                                result = false;
                                keepGoing = false;
                            }
                        }
                    }
                }
            }
        });
        return result;
    }

    public beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            if (this.statusVoucher === 0) {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.refModalService.open(this.viewVouchersSelected);
            }
        }
    }

    /*-----*/
    selectvatAccount(detail: IMCPaymentDetailTax) {
        detail.vATAccount = detail.vatAccountItem.accountNumber;
    }

    changeDate() {
        this.mCPayment.postedDate = this.mCPayment.date;
    }

    closeContent() {
        this.fromCloseForm = false;
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    changeNoVoucher() {
        if (this.mCPayment.typeLedger === 0) {
            this.mCPayment.noFBook = this.no;
        } else if (this.mCPayment.typeLedger === 1) {
            this.mCPayment.noMBook = this.no;
        } else {
            if (this.isSoTaiChinh) {
                this.mCPayment.noFBook = this.no;
            } else {
                this.mCPayment.noMBook = this.no;
            }
        }
    }

    /*ngOnDestroy(): void {
        if (this.eventSubscriber) {
            this.eventManager.destroy(this.eventSubscriber);
        }
    }*/

    getTotalVATAmount() {
        if (this.mCPaymentDetailTaxs && this.mCPaymentDetailTaxs.length > 0) {
            return this.mCPaymentDetailTaxs.map(n => n.vATAmount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getTotalPretaxAmount() {
        if (this.mCPaymentDetailTaxs && this.mCPaymentDetailTaxs.length > 0) {
            return this.mCPaymentDetailTaxs.map(n => n.pretaxAmount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    /*
    * hàm ss du lieu 2 object và 2 mảng object
    * return true: neu tat ca giong nhau
    * return fale: neu 1 trong cac object ko giong nhau
    * */
    canDeactive(): boolean {
        if (this.statusVoucher === 0 && !this.isCloseAll && !this.isSaveAndNew) {
            return (
                this.utilsService.isEquivalent(this.mCPayment, this.mCPaymentCopy) &&
                this.utilsService.isEquivalentArray(this.mCPaymentDetails, this.mCPaymentDetailsCopy) &&
                this.utilsService.isEquivalentArray(this.mCPaymentDetailTaxs, this.mCPaymentDetailTaxsCopy) &&
                this.utilsService.isEquivalentArray(this.viewVouchersSelected, this.viewVouchersSelectedCopy)
            );
        } else {
            return true;
        }
    }

    registerAddNewRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.AddnewRow(this.select, true);
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
            this.copyRow(this.contextMenu.selectedData, this.select);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerCombobox() {
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.accountingObjectService.getAllDTO().subscribe(
                (res: HttpResponse<IAccountingObject[]>) => {
                    this.accountingObjectData = res.body
                        .filter(n => n.isActive)
                        .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                    this.employees = res.body
                        .filter(n => n.isEmployee && n.isActive)
                        .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                    this.selectAccountingObjectType();
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
            if (response.content.name === CategoryName.MUC_THU_CHI) {
                const BudgetItem = response.content.data;
                this.budgetItems.push(BudgetItem);
                this.budgetItems.sort((a, b) => a.budgetItemCode.localeCompare(b.budgetItemCode));
                this.mCPaymentDetails[this.currentRow].budgetItem = BudgetItem;
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    saveRow(i) {
        this.currentRow = i;
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

    vATAccountChange() {
        this.updateAmountTaxWithAccount();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuChi_Xoa])
    delete() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.mCPayment.postedDate) && !this.utilsService.isShowPopup) {
            if (this.statusVoucher === 1 && !this.mCPayment.recorded) {
                this.router.navigate(['/mc-payment', { outlets: { popup: this.mCPayment.id + '/delete' } }]);
            }
        }
    }

    summCPaymentDetailVendors(prop) {
        let total = 0;
        for (let i = 0; i < this.mCPaymentDetailVendors.length; i++) {
            total += this.mCPaymentDetailVendors[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    getEMContractsbyID(id) {
        /*if (this.eMContracts) {
            const eMC = this.eMContracts.find(n => n.id === id);
            if (eMC) {
                if (this.isSoTaiChinh) {
                    return eMC.noFBook;
                } else {
                    return eMC.noMBook;
                }
            }
        }*/
    }

    getEmployeeByID(id) {
        if (this.accountingObjectData) {
            const epl = this.accountingObjectData.find(n => n.id === id);
            if (epl) {
                return epl.accountingObjectCode;
            } else {
                return '';
            }
        }
    }

    isForeignCurrency() {
        return this.account && this.mCPayment.currencyID !== this.account.organizationUnit.currencyID;
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

    viewVoucher(imcPaymentDetailVendor: IMCPaymentDetailVendor) {
        if ((this.isSoTaiChinh ? imcPaymentDetailVendor.noFBook : imcPaymentDetailVendor.noMBook) === 'OPN') {
            return;
        }
        let url = '';
        if (imcPaymentDetailVendor.voucherTypeID === this.TYPE_PP_INVOICE) {
            this.viewVoucherService.checkViaStockPPInvoice({ id: imcPaymentDetailVendor.pPInvoiceID }).subscribe(
                (res: HttpResponse<any>) => {
                    if (res.body.message === UpdateDataMessages.STOCK_TRUE) {
                        url = `/#/mua-hang/qua-kho-ref/${imcPaymentDetailVendor.pPInvoiceID}/edit/1`;
                        window.open(url, '_blank');
                    } else if (res.body.message === UpdateDataMessages.STOCK_FALSE) {
                        url = `/#/mua-hang/khong-qua-kho-ref/${imcPaymentDetailVendor.pPInvoiceID}/edit/1`;
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
        } else if (imcPaymentDetailVendor.voucherTypeID === this.TYPE_PP_SERVICE) {
            url = `/#/pp-service/${imcPaymentDetailVendor.pPInvoiceID}/edit/from-ref`;
        } else if (imcPaymentDetailVendor.voucherTypeID === this.TYPE_PP_DISCOUNT_RETURN) {
            url = `/#/pp-discount-return/${imcPaymentDetailVendor.pPInvoiceID}/edit/from-ref`;
        } else if (imcPaymentDetailVendor.voucherTypeID === this.TYPE_FA_INCREAMENT) {
            url = `/#/fa-increament/${imcPaymentDetailVendor.pPInvoiceID}/edit/from-ref`;
        } else if (imcPaymentDetailVendor.voucherTypeID === this.TYPE_TI_INCREAMENT) {
            url = `/#/ti-increament/${imcPaymentDetailVendor.pPInvoiceID}/edit/from-ref`;
        } else if (imcPaymentDetailVendor.voucherTypeID === TypeID.CHUNG_TU_NGHIEP_VU_KHAC) {
            url = `/#/g-other-voucher/${imcPaymentDetailVendor.pPInvoiceID}/edit/from-ref`;
        }
        if (url) {
            window.open(url, '_blank');
        }
    }

    selectChangeAccountingObjectTax(i) {
        if (this.mCPaymentDetailTaxs[i].accountingObject) {
            this.mCPaymentDetailTaxs[i].accountingObjectName = this.mCPaymentDetailTaxs[i].accountingObject.accountingObjectName;
            this.mCPaymentDetailTaxs[i].taxCode = this.mCPaymentDetailTaxs[i].accountingObject.taxCode;
        }
    }

    sum(prop) {
        let total = 0;
        if (this.mCPaymentDetails) {
            for (let i = 0; i < this.mCPaymentDetails.length; i++) {
                total += this.mCPaymentDetails[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.mCPaymentDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.mCPayment;
    }

    addDataToDetail() {
        this.mCPaymentDetails = this.details ? this.details : this.mCPaymentDetails;
        this.mCPayment = this.parent ? this.parent : this.mCPayment;
    }

    ngAfterViewInit(): void {
        if (window.location.href.includes('/mc-payment/new')) {
            this.focusFirstInput();
        }
    }

    actionFocus(indexCol, indexRow) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
    }
}
