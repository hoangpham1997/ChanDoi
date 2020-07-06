import { AfterViewChecked, AfterViewInit, Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { BankService } from 'app/danhmuc/bank';
import { IBank } from 'app/shared/model/bank.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { Currency, ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IGOtherVoucherDetails } from 'app/shared/model/g-other-voucher-details.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { CostSetService } from 'app/entities/cost-set';
import { ExpenseItemService } from 'app/entities/expense-item';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { IGOtherVoucherDetailTax } from 'app/shared/model/g-other-voucher-detail-tax.model';
import { ICreditCard } from 'app/shared/model/credit-card.model';
import { CreditCardService } from 'app/danhmuc/credit-card';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { IAccountDefault } from 'app/shared/model/account-default.model';
import { AccountDefaultService } from 'app/danhmuc/account-default';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { EMContractService } from 'app/entities/em-contract';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { BudgetItemService } from 'app/entities/budget-item';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import {
    AccountType,
    CategoryName,
    DDSo_NCachHangDVi,
    DDSo_NCachHangNghin,
    DDSo_NgoaiTe,
    DDSo_TienVND,
    MSGERROR,
    SO_LAM_VIEC,
    TCKHAC_GhiSo,
    TCKHAC_SDSoQuanTri
} from 'app/app.constants';
import { Principal } from 'app/core';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { GOtherVoucherService } from './g-other-voucher.service';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IAccountAllList } from 'app/shared/model/account-all-list.model';
import * as moment from 'moment';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { ROLE } from 'app/role.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { DATE_FORMAT } from 'app/shared';
import { InvoiceType } from 'app/shared/model/invoice-type.model';
import { IAReportService } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/ia-report.service';

@Component({
    selector: 'eb-g-other-voucher-update',
    templateUrl: './g-other-voucher-update.component.html',
    styleUrls: ['./g-other-voucher-update.component.css']
})
export class GOtherVoucherUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewInit, AfterViewChecked {
    @ViewChild('test') public modalComponent: NgbModalRef;
    @ViewChild('content') content: TemplateRef<any>;
    @ViewChild('contentAmount') public modalComponentAmount: NgbModalRef;
    private _gOtherVoucher: IGOtherVoucher;
    isSaving: boolean;
    dateDp: any;
    postedDateDp: any;
    accountDefault: { value?: string };
    currencies: ICurrency[];
    gOtherVoucherDetails: IGOtherVoucherDetails[];
    gOtherVoucherDetailTax: IGOtherVoucherDetailTax[];
    eMContracts: IEMContract[];
    creditCards: ICreditCard[];
    isRecord: Boolean;
    totalAmount: number;
    totalAmountOriginal: number;
    isThem: Boolean;
    temp: Number;
    totalVatAmount: any;
    totalVatAmountOriginal: any;
    banks: IBank[];
    accountList: IAccountList[];
    autoPrinciple: IAutoPrinciple[];
    accountingObjectBankAccount: IAccountingObjectBankAccount[];
    employeeName: string;
    autoPrincipleName: string;
    items: any;
    isMainCurrency: boolean;
    id: string;
    i: number;
    recorded: boolean;
    //  data storage provider
    page: number;
    itemsPerPage: number;
    pageCount: number;
    totalItems: number;
    rowNum: number;
    // sort
    predicate: any;
    accountingObjectName: any;
    reverse: any;
    record_: any;
    isCreateUrl: boolean;
    isEditUrl: boolean;
    disableAddButton: boolean;
    disableEditButton: boolean;
    accounting: IAccountingObject[];
    searchVoucher: string;
    dataSession: IDataSessionStorage;
    creditCardType: string;
    ownerCard: string;
    warningMessage: any;
    viewVouchersSelected: any;
    contextMenu: ContextMenu;
    select: number;
    eventSubscriber: Subscription;
    modalRef: NgbModalRef;
    currentAccount: any;
    isSoTaiChinh: boolean;
    currency: Currency;
    ngayHachToan: any;
    sysRecord: any;
    sysTypeLedger: any;
    options: any;
    DDSo_NCachHangNghin: any;
    DDSo_NCachHangDVi: any;
    DDSo_TienVND: any;
    DDSo_NgoaiTe: any;
    isHideTypeLedger: Boolean;
    TCKHAC_SDSoQuanTri: any;
    creditAccountList: IAccountList[];
    creditAccountListItem: IAccountList;
    debitAccountList: IAccountList[];
    debitAccountItem: IAccountList;
    vatAccountList: IAccountList[];
    iAutoPrinciple: IAutoPrinciple;
    gOtherVoucherCopy: IGOtherVoucher;
    gOtherVoucherDetailsCopy: IGOtherVoucherDetails[];
    gOtherVoucherDetailTaxCopy: IGOtherVoucherDetailTax[];
    statusVoucher: number;
    viewVouchersSelectedCopy: any;
    type: any;
    no: any;
    goodsServicePurchases: IGoodsServicePurchase[];
    goodsServicePurchaseID: any;
    isReadOnly: boolean;
    columnList = [
        { column: AccountType.TK_CO, ppType: false },
        { column: AccountType.TK_NO, ppType: false },
        { column: AccountType.TK_THUE_GTGT, ppType: false },
        { column: AccountType.TKDU_THUE_GTGT, ppType: false }
    ];
    currentRow: number;
    isRefVoucher: boolean;
    currentCurrency?: ICurrency;
    TYPE_CHUNG_TU_NGHIEP_VU_KHAC = 700;
    TYPE_KET_CHUYEN_LAI_LO = 702;
    TYPE_PHAN_BO_CHI_PHI_TRA_TRUOC = 709;
    TYPE_NGHIEM_THU_CONG_TRINH_DON_HANG_HOP_DONG = 703;
    count: number;
    listVAT: any[];
    isShowGoodsServicePurchase: boolean;
    invoiceTypes: InvoiceType[];

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
    buttonSaveTranslate = 'ebwebApp.mBDeposit.toolTip.save';
    buttonRecordTranslate = 'ebwebApp.mBDeposit.toolTip.record';
    buttonUnRecordTranslate = 'ebwebApp.mBDeposit.toolTip.unrecord';
    buttonPrintTranslate = 'ebwebApp.mBDeposit.toolTip.print';
    buttonSaveAndNewTranslate = 'ebwebApp.mBDeposit.toolTip.saveAndNew';
    buttonCopyAndNewTranslate = 'ebwebApp.mBDeposit.toolTip.copyAndNew';
    buttonCloseFormTranslate = 'ebwebApp.mBDeposit.toolTip.closeForm';

    constructor(
        private router: Router,
        private gOtherVoucherService: GOtherVoucherService,
        private activatedRoute: ActivatedRoute,
        private accService: AccountingObjectService,
        public utilService: UtilsService,
        private accountDefaultsService: AccountDefaultService,
        private jhiAlertService: JhiAlertService,
        private gLService: GeneralLedgerService,
        private bankAccountDetailsService: BankAccountDetailsService,
        private currencyService: CurrencyService,
        private bankService: BankService,
        private expenseItemsService: ExpenseItemService,
        private statisticsCodeService: StatisticsCodeService,
        private eMContractService: EMContractService,
        private accountListService: AccountListService,
        private autoPrincipleService: AutoPrincipleService,
        private organizationUnitService: OrganizationUnitService,
        private costSetService: CostSetService,
        private creditCardService: CreditCardService,
        private translate: TranslateService,
        private toastr: ToastrService,
        private budgetItemService: BudgetItemService,
        private eventManager: JhiEventManager,
        private refModalService: RefModalService,
        private principal: Principal,
        private modalService: NgbModal,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        public utilsService: UtilsService,
        private iaReportService: IAReportService
    ) {
        super();
        this.searchVoucher = JSON.parse(sessionStorage.getItem('dataSearchGOtherVoucher'));
        this.dataSession = JSON.parse(sessionStorage.getItem('dataSession'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
        this.contextMenu = new ContextMenu();

        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (account.organizationUnit.taxCalculationMethod === 0) {
                this.isShowGoodsServicePurchase = true;
                this.goodsServicePurchaseID = this.currentAccount.organizationUnit.goodsServicePurchaseID;
            } else {
                this.isShowGoodsServicePurchase = false;
            }
            this.sysRecord = this.currentAccount.systemOption.find(x => x.code === TCKHAC_GhiSo && x.data);
            this.sysTypeLedger = this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data;
            this.TCKHAC_SDSoQuanTri = this.currentAccount.systemOption.find(x => x.code === TCKHAC_SDSoQuanTri).data;
            this.currencyService.findAllActive().subscribe(res => {
                this.currencies = res.body;
                // if (this.mBCreditCard && this.mBCreditCard.currencyID) {
                //     this.currency = this.currencies.find(cur => cur.currencyCode === this.mBCreditCard.currencyID);
                // } else if (this.currentAccount.organizationUnit && this.currentAccount.organizationUnit.currencyID) {
                this.currency = this.currencies.find(cur => cur.currencyCode === this.currentAccount.organizationUnit.currencyID);
                this.currentCurrency = this.currencies.find(cur => cur.currencyCode === this.gOtherVoucher.currencyID);
                // }
                if (!this.gOtherVoucher || !this.gOtherVoucher.currencyID) {
                    this.gOtherVoucher.currencyID = this.currency.currencyCode;
                    this.selectChangeCurrency();
                }
                this.DDSo_NCachHangNghin = this.currentAccount.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
                this.DDSo_NCachHangDVi = this.currentAccount.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;
                this.DDSo_NgoaiTe = this.currentAccount.systemOption.find(x => x.code === DDSo_NgoaiTe && x.data).data;
                this.DDSo_TienVND = this.currentAccount.systemOption.find(x => x.code === DDSo_TienVND && x.data).data;
            });
            const param = {
                typeID: 700,
                columnName: this.columnList
            };
            this.accountListService.getAccountTypeFour(param).subscribe(res => {
                const dataAccount: IAccountAllList = res.body;
                this.creditAccountList = dataAccount.creditAccount;
                this.debitAccountList = dataAccount.debitAccount;
                this.vatAccountList = dataAccount.vatAccount;
            });
            if (this.gOtherVoucher) {
                if (this.gOtherVoucher.currencyID) {
                    if (this.gOtherVoucher.currencyID === this.currency.currencyCode) {
                        this.type = 7;
                        this.isMainCurrency = true;
                    } else {
                        this.type = 8;
                        this.isMainCurrency = false;
                    }
                } else {
                    this.type = 7;
                    this.isMainCurrency = true;
                }
            }
            this.accService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
                this.accounting = res.body;
                this.accountingObjects = res.body
                    .filter(n => n.isActive)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                this.employees = res.body.filter(e => e.isEmployee && e.isActive);
            });
        });
        this.iaReportService.queryInvoiceType().subscribe(res => {
            this.invoiceTypes = res.body;
        });
    }

    ngOnInit() {
        this.isSaving = false;
        this.isMainCurrency = false;
        this.listVAT = [
            { name: '0%', data: 0 },
            { name: '5%', data: 1 },
            { name: '10%', data: 2 },
            { name: 'KCT', data: 3 },
            { name: 'KTT', data: 4 }
        ];
        this.isEditUrl = window.location.href.includes('/edit');
        this.isHideTypeLedger = false;
        this.activatedRoute.data.subscribe(({ gOtherVoucher }) => {
            this.gOtherVoucher = gOtherVoucher;
            this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
            if (!this.isEditUrl) {
                this.statusVoucher = 0;
                this.gOtherVoucher.date = this.utilService.ngayHachToan(this.currentAccount);
                this.gOtherVoucher.postedDate = this.gOtherVoucher.date;
                if (this.TCKHAC_SDSoQuanTri === '0') {
                    this.gOtherVoucher.typeLedger = 0;
                    this.sysTypeLedger = 0;
                    this.isHideTypeLedger = true;
                } else {
                    this.gOtherVoucher.typeLedger = 2;
                    this.sysTypeLedger = this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                }
                this.utilService
                    .getGenCodeVoucher({
                        typeGroupID: 70, // typeGroupID loại chứng từ
                        companyID: '', // ID công ty
                        branchID: '', // ID chi nhánh
                        displayOnBook: this.sysTypeLedger // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
                    })
                    .subscribe((res: HttpResponse<string>) => {
                        // this.mCReceipt.noFBook = (res.body.toString());
                        this.no = res.body;
                        // sao chép và thêm mới
                        if (sessionStorage.getItem('saveAndLoad')) {
                            this.gOtherVoucher = JSON.parse(sessionStorage.getItem('saveAndLoad'));
                            this.autoPrincipleName = JSON.parse(sessionStorage.getItem('autoPrincipleName'));
                            this.gOtherVoucherDetails = this.gOtherVoucher.gOtherVoucherDetails;
                            this.gOtherVoucherDetailTax = this.gOtherVoucher.gOtherVoucherDetailTax;
                            this.viewVouchersSelected = this.gOtherVoucher.viewVouchers;
                            this.gOtherVoucher.date = moment(this.gOtherVoucher.date);
                            this.gOtherVoucher.postedDate = this.gOtherVoucher.date;
                            sessionStorage.removeItem('saveAndLoad');
                            sessionStorage.removeItem('autoPrincipleName');
                            this.copy();
                        } else {
                            this.translate.get(['ebwebApp.gOtherVoucher.defaultReason']).subscribe(res2 => {
                                this.gOtherVoucher.reason = res2['ebwebApp.gOtherVoucher.defaultReason'];
                            });
                        }
                        this.copy();
                    });
                this.isReadOnly = false;
            } else {
                this.utilsService
                    .getIndexRow({
                        id: this.gOtherVoucher.id,
                        isNext: true,
                        typeID: this.TYPE_CHUNG_TU_NGHIEP_VU_KHAC,
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
                this.viewVouchersSelected = gOtherVoucher.viewVouchers ? gOtherVoucher.viewVouchers : [];
                this.gOtherVoucherDetails = gOtherVoucher.gOtherVoucherDetails ? gOtherVoucher.gOtherVoucherDetails : [];
                this.gOtherVoucherDetailTax = gOtherVoucher.gOtherVoucherDetailTax ? gOtherVoucher.gOtherVoucherDetailTax : [];
                this.no = this.isSoTaiChinh ? gOtherVoucher.noFBook : gOtherVoucher.noMBook;
                if (this.TCKHAC_SDSoQuanTri === '0') {
                    this.isHideTypeLedger = true;
                }
                this.statusVoucher = 1;
                if (this.gOtherVoucher.gOtherVoucherDetailTax) {
                    for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetailTax.length; i++) {
                        this.gOtherVoucher.gOtherVoucherDetailTax[i].invoiceDate = moment(
                            this.gOtherVoucher.gOtherVoucherDetailTax[i].invoiceDate
                        );
                    }
                }
                this.isReadOnly = true;
                this.copy();
            }
        });
        // lấy dữ liệu cho combobox
        this.accService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.employees = res.body
                .filter(listEmployee => listEmployee.isEmployee && listEmployee.isActive)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
        });
        this.bankService.getBanks().subscribe((res: HttpResponse<IBank[]>) => {
            this.banks = res.body;
        });
        this.bankAccountDetailsService.getBankAccountDetails().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
            this.bankAccountDetails = res.body;
        });
        this.statisticsCodeService.getAllStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            if (this.gOtherVoucher.id) {
                this.statisticCodes = res.body.filter(a => a.isActive);
                if (this.gOtherVoucher.gOtherVoucherDetails.length > 0) {
                    for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetails.length; i++) {
                        if (this.gOtherVoucher.gOtherVoucherDetails[i].statisticCodeID) {
                            const findSC = this.statisticCodes.find(
                                a => a.id === this.gOtherVoucher.gOtherVoucherDetails[i].statisticCodeID
                            );
                            if (!findSC) {
                                const addSC = res.body.find(a => a.id === this.gOtherVoucher.gOtherVoucherDetails[i].statisticCodeID);
                                if (addSC) {
                                    this.statisticCodes.push(addSC);
                                }
                            }
                        }
                    }
                }
            } else {
                this.statisticCodes = res.body.filter(a => a.isActive);
            }
        });
        this.costSetService.getAllCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            if (this.gOtherVoucher.id) {
                this.costSets = res.body;
            } else {
                this.costSets = res.body.filter(a => a.isActive);
            }
        });
        this.accountListService.findByGOtherVoucher().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountList = res.body;
        });
        this.expenseItemsService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body;
        });
        this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
            this.eMContracts = res.body;
        });
        this.organizationUnitService.getOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body.sort((a, b) => a.organizationUnitCode.localeCompare(b.organizationUnitCode));
        });
        this.autoPrincipleService.getAutoPrinciples().subscribe((res: HttpResponse<IAutoPrinciple[]>) => {
            this.autoPrinciple = res.body.filter(aPrinciple => aPrinciple.typeId === 700 || aPrinciple.typeId === 0).sort((n1, n2) => {
                if (n1.typeId > n2.typeId) {
                    return 1;
                }
                if (n1.typeId < n2.typeId) {
                    return -1;
                }
                return 0;
            });
            if (this.gOtherVoucher.reason && this.gOtherVoucher.id) {
                if (this.autoPrinciple.find(a => a.autoPrincipleName === this.gOtherVoucher.reason)) {
                    this.autoPrincipleName = this.autoPrinciple.find(
                        a => a.autoPrincipleName === this.gOtherVoucher.reason
                    ).autoPrincipleName;
                }
            }
        });
        this.budgetItemService.getBudgetItems().subscribe((res: HttpResponse<IBudgetItem[]>) => {
            this.budgetItems = res.body;
        });
        this.goodsServicePurchaseService.getGoodServicePurchases().subscribe((res: HttpResponse<IGoodsServicePurchase[]>) => {
            this.goodsServicePurchases = res.body;
        });
        // Check ghi sổ, đếm số dòng
        if (this.gOtherVoucher.id) {
            this.gOtherVoucherDetails =
                this.gOtherVoucher.gOtherVoucherDetails === undefined ? [] : this.gOtherVoucher.gOtherVoucherDetails;
            this.gOtherVoucherDetailTax =
                this.gOtherVoucher.gOtherVoucherDetailTax === undefined ? [] : this.gOtherVoucher.gOtherVoucherDetailTax;
            // this.isRecord = this.gOtherVoucher.recorded === undefined ? false : this.gOtherVoucher.recorded;
        } else {
            this.gOtherVoucherDetails = [];
            this.gOtherVoucherDetailTax = [];
            this.viewVouchersSelected = [];
            this.isRecord = false;
        }
        if (this.gOtherVoucher.id) {
            this.id = this.gOtherVoucher.id;
        } else {
            // ; this.hideButtonPrevious = false
        }
        // if (this.gOtherVoucher.recorded) {
        //     this.isRecord = false;
        // } else {
        //     this.isRecord = true;
        // }
        this.isCreateUrl = window.location.href.includes('/g-other-voucher/new');
        if (this.gOtherVoucher.id && !this.isCreateUrl) {
            this.isEdit = this.isCreateUrl = false;
        } else {
            this.isEdit = this.isCreateUrl = true;
        }
        this.disableAddButton = true;
        if (this.gOtherVoucher.id) {
            if (!this.gOtherVoucher.recorded && this.isEditUrl) {
                this.disableEditButton = false;
            } else {
                this.disableEditButton = true;
            }
        } else {
            this.disableEditButton = true;
        }
        this.isRefVoucher = window.location.href.includes('/edit/from-ref');
        this.registerRef();
        this.copy();
        this.afterCopyRow();
        this.afterAddRow();
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilService.setShowPopup(response.content);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        this.sumAfterDeleteByContextMenu();
        // this.registerIsShowPopup();
    }

    // registerIsShowPopup() {
    //     this.utilService.checkEvent.subscribe(res => {
    //         this.isShowPopup = res;
    //     });
    // }

    closeForm() {
        event.preventDefault();
        if (this.gOtherVoucherCopy && (this.statusVoucher === 0 || this.statusVoucher === 1) && !this.utilsService.isShowPopup) {
            if (
                !this.utilsService.isEquivalent(this.gOtherVoucher, this.gOtherVoucherCopy) ||
                !this.utilsService.isEquivalentArray(this.gOtherVoucherDetails, this.gOtherVoucherDetailsCopy) ||
                !this.utilsService.isEquivalentArray(this.gOtherVoucherDetailTax, this.gOtherVoucherDetailTaxCopy) ||
                !this.utilsService.isEquivalentArray(this.viewVouchersSelected, this.viewVouchersSelectedCopy)
            ) {
                if (this.isReadOnly) {
                    this.closeAll();
                    return;
                }
                // this.copy();
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
                return;
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

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuNghiepVuKhac_Them])
    copyAndNew() {
        event.preventDefault();
        if (!this.isCreateUrl && !this.utilsService.isShowPopup) {
            this.gOtherVoucher.id = undefined;
            for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetails.length; i++) {
                this.gOtherVoucher.gOtherVoucherDetails[i].id = undefined;
            }
            for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetailTax.length; i++) {
                this.gOtherVoucher.gOtherVoucherDetailTax[i].id = undefined;
            }
            for (let i = 0; i < this.gOtherVoucher.viewVouchers.length; i++) {
                this.gOtherVoucher.viewVouchers[i].id = undefined;
            }
            this.gOtherVoucher.noMBook = null;
            this.gOtherVoucher.noFBook = null;
            sessionStorage.setItem('saveAndLoad', JSON.stringify(this.gOtherVoucher));
            sessionStorage.setItem('autoPrincipleName', JSON.stringify(this.autoPrincipleName ? this.autoPrincipleName : ''));
            this.edit();
            this.isSaving = false;
            this.copy();
            this.router.navigate(['g-other-voucher/new']);
            this.statusVoucher = 0;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuNghiepVuKhac_Sua])
    edit() {
        event.preventDefault();
        // && this.isRecord
        if (
            !this.isCreateUrl &&
            !this.checkCloseBook(this.currentAccount, this.gOtherVoucher.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            this.isCreateUrl = !this.isCreateUrl;
            this.isEdit = this.isCreateUrl;
            this.disableAddButton = false;
            this.disableEditButton = true;
            this.statusVoucher = 0;
            this.isReadOnly = false;
            this.focusFirstInput();
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuNghiepVuKhac_Them])
    addNew($event) {
        event.preventDefault();
        if (!this.isCreateUrl && !this.utilsService.isShowPopup) {
            this.router.navigate(['g-other-voucher/new']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuNghiepVuKhac_Them, ROLE.ChungTuNghiepVuKhac_Sua])
    save(isNew = false) {
        event.preventDefault();
        this.warningMessage = '';
        this.gOtherVoucher.gOtherVoucherDetails = this.gOtherVoucherDetails;
        this.gOtherVoucher.gOtherVoucherDetailTax = this.gOtherVoucherDetailTax;
        this.gOtherVoucher.viewVouchers = this.viewVouchersSelected;
        if (this.isCreateUrl && !this.isShowPopup) {
            if (this.checkError()) {
                let totalVatAmount = 0,
                    totalVatAmountOriginal = 0;
                for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetailTax.length; i++) {
                    this.gOtherVoucher.gOtherVoucherDetailTax[i].pretaxAmountOriginal = this.round(
                        this.gOtherVoucher.gOtherVoucherDetailTax[i].pretaxAmount /
                            (this.currentCurrency.formula.includes('*')
                                ? this.gOtherVoucher.exchangeRate
                                : 1 / this.gOtherVoucher.exchangeRate),
                        8
                    );
                    this.gOtherVoucher.gOtherVoucherDetailTax[i].vATAmountOriginal = this.round(
                        this.gOtherVoucher.gOtherVoucherDetailTax[i].vATAmount /
                            (this.currentCurrency.formula.includes('*')
                                ? this.gOtherVoucher.exchangeRate
                                : 1 / this.gOtherVoucher.exchangeRate),
                        8
                    );
                    this.gOtherVoucherDetailTax[i].orderPriority = i + 1;
                    // this.gOtherVoucher.gOtherVoucherDetailTax[i].orderPriority = i + 1;
                    totalVatAmount += this.gOtherVoucherDetailTax[i].vATAmount;
                    totalVatAmountOriginal += this.gOtherVoucherDetailTax[i].vATAmountOriginal;
                }
                if (this.gOtherVoucher && this.gOtherVoucher.gOtherVoucherDetails && this.gOtherVoucherDetails.length > 0) {
                    for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetails.length; i++) {
                        this.gOtherVoucher.gOtherVoucherDetails[i].orderPriority = i + 1;
                    }
                }
                // if (this.gOtherVoucherDetailTax.length > 0) {
                //     if (this.isMainCurrency) {
                //         if (totalVatAmount === this.gOtherVoucher.totalAmount) {
                //         } else {
                //             this.modalRef = this.modalService.open(this.modalComponent, { backdrop: 'static' });
                //             return;
                //         }
                //     } else {
                //         if (totalVatAmountOriginal === this.gOtherVoucher.totalAmountOriginal) {
                //         } else {
                //             this.modalRef = this.modalService.open(this.modalComponent, { backdrop: 'static' });
                //             return;
                //         }
                //     }
                // }
                this.isCreateUrl = !this.isCreateUrl;
                this.isEdit = this.isCreateUrl;
                this.disableAddButton = true;
                if (this.gOtherVoucher.typeLedger === 0) {
                    this.gOtherVoucher.noFBook = this.no;
                    this.gOtherVoucher.noMBook = null;
                } else if (this.gOtherVoucher.typeLedger === 1) {
                    this.gOtherVoucher.noFBook = null;
                    this.gOtherVoucher.noMBook = this.no;
                } else {
                    if (this.isSoTaiChinh) {
                        this.gOtherVoucher.noFBook = this.no;
                    } else {
                        this.gOtherVoucher.noMBook = this.no;
                    }
                }
                this.gOtherVoucher.typeID = 700;
                // if (this.sysRecord.data === '0') {
                //     this.gOtherVoucher.recorded = true;
                // } else {
                //     this.gOtherVoucher.recorded = false;
                // }
                this.isSaving = true;
                // for (this.i = 0; this.i < this.gOtherVoucher.gOtherVoucherDetailTax.length; this.i++) {
                //     this.gOtherVoucher.gOtherVoucherDetailTax[this.i].pretaxAmountOriginal = this.gOtherVoucher.gOtherVoucherDetailTax[
                //         this.i
                //         ].pretaxAmount;
                //     this.gOtherVoucher.gOtherVoucherDetailTax[this.i].vATAmountOriginal = this.gOtherVoucher.gOtherVoucherDetailTax[
                //         this.i
                //         ].vATAmount;
                //     this.gOtherVoucher.gOtherVoucherDetailTax[this.i].orderPriority = this.i + 1;
                // }

                // check is url new
                if (this.isCreateUrl && this.gOtherVoucher.id) {
                    this.gOtherVoucher.id = undefined;
                }
                if (!this.isCreateUrl && !this.isEditUrl) {
                    this.gOtherVoucher.id = undefined;
                    for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetails.length; i++) {
                        this.gOtherVoucher.gOtherVoucherDetails[i].id = undefined;
                    }
                    for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetailTax.length; i++) {
                        this.gOtherVoucher.gOtherVoucherDetailTax[i].id = undefined;
                    }
                }
                if (!this.isCreateUrl && !this.isEditUrl) {
                    this.gOtherVoucher.id = undefined;
                }
                if (this.gOtherVoucher.id) {
                    this.subscribeToSaveResponse(this.gOtherVoucherService.update(this.gOtherVoucher));
                } else {
                    this.subscribeToSaveResponse(this.gOtherVoucherService.create(this.gOtherVoucher));
                }
            } else {
            }
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    if (this.isEditUrl) {
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBDeposit.editSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    } else {
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBDeposit.insertSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    this.isEdit = this.isCreateUrl = false;
                    if (res.body.gOtherVoucher.recorded) {
                        this.disableAddButton = false;
                    } else {
                        this.disableAddButton = true;
                    }
                    this.gOtherVoucher.id = res.body.gOtherVoucher.id;
                    this.gOtherVoucher.recorded = res.body.gOtherVoucher.recorded;
                    this.onSaveSuccess();
                    this.isReadOnly = true;
                    this.router.navigate(['./g-other-voucher', this.gOtherVoucher.id, 'edit']);
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                    this.isEdit = this.isCreateUrl = true;
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    return;
                } else if (res.body.status === 2) {
                    if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.disableEditButton = false;
                        this.disableAddButton = true;
                        this.gOtherVoucher.id = res.body.gOtherVoucher.id;
                        this.copy();
                        this.router.navigate(['./g-other-voucher', this.gOtherVoucher.id, 'edit']);
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyQT'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.disableEditButton = false;
                        this.disableAddButton = true;
                        this.gOtherVoucher.id = res.body.gOtherVoucher.id;
                        this.copy();
                        this.router.navigate(['./g-other-voucher', this.gOtherVoucher.id, 'edit']);
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyTC'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.disableEditButton = false;
                        this.disableAddButton = true;
                        this.gOtherVoucher.id = res.body.gOtherVoucher.id;
                        this.copy();
                        this.router.navigate(['./g-other-voucher', this.gOtherVoucher.id, 'edit']);
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuy'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else {
                        this.recordFailed();
                    }
                }
            },
            (res: HttpErrorResponse) => {
                this.onSaveError();
                this.isEdit = this.isCreateUrl = true;
            }
        );
    }

    private recordFailed() {
        this.toastr.error(this.translate.instant('global.data.recordFailed'), this.translate.instant('ebwebApp.mCReceipt.home.message'));
    }

    private onSaveSuccess() {
        this.copy();
        this.statusVoucher = 1;
        this.isSaving = false;
        this.disableAddButton = true;
        this.disableEditButton = true;
        this.isRecord = false;
    }

    private onSaveError() {
        this.isSaving = false;
        this.isCreateUrl = !this.isCreateUrl;
        this.isEdit = this.isCreateUrl;
    }

    get gOtherVoucher() {
        return this._gOtherVoucher;
    }

    set gOtherVoucher(gOtherVoucher: IGOtherVoucher) {
        this._gOtherVoucher = gOtherVoucher;
    }

    // selectChangeAccountingObject() {
    //     const iAccountingObject = this.accountingObjects.find(
    //         accountingObject => accountingObject.id === this.gOtherVoucher.accountingObjectID
    //     );
    //     if (iAccountingObject) {
    //         this.gOtherVoucher.accountingObjectName = iAccountingObject.accountingObjectName;
    //         this.gOtherVoucher.accountingObjectAddress = iAccountingObject.accountingObjectAddress;
    //     } else {
    //         this.gOtherVoucher.accountingObjectName = '';
    //         this.gOtherVoucher.accountingObjectAddress = '';
    //     }
    //     for (const dt of this.gOtherVoucherDetails) {
    //         dt.accountingObjectID = this.gOtherVoucher.accountingObjectID;
    //     }
    //     for (const dtt of this.gOtherVoucherDetailTax) {
    //         dtt.accountingObjectID = this.gOtherVoucher.accountingObjectID;
    //     }
    // }

    selectChangeReason() {
        this.gOtherVoucher.reason = this.autoPrincipleName;
        if (this.autoPrinciple) {
            this.iAutoPrinciple = this.autoPrinciple.find(a => a.autoPrincipleName === this.autoPrincipleName);
            if (this.gOtherVoucher.gOtherVoucherDetails) {
                if (this.gOtherVoucher.gOtherVoucherDetails.length > 0) {
                    this.gOtherVoucherDetails[0].description = this.autoPrincipleName;
                }
            }
        }
        for (const dt of this.gOtherVoucherDetails) {
            dt.debitAccount = this.iAutoPrinciple.debitAccount;
            dt.creditAccount = this.iAutoPrinciple.creditAccount;
            dt.description = this.iAutoPrinciple.autoPrincipleName;
        }
    }

    selectChangeDescription() {
        if (this.gOtherVoucher.gOtherVoucherDetails) {
            if (this.gOtherVoucher.gOtherVoucherDetails.length > 0) {
                this.gOtherVoucherDetails[0].description = this.gOtherVoucher.reason;
            }
        }
    }

    selectChangeCurrency() {
        let sum = 0;
        this.currentCurrency = this.currencies.find(currency => currency.currencyCode === this.gOtherVoucher.currencyID);
        if (this.currentCurrency) {
            this.gOtherVoucher.exchangeRate = this.currentCurrency.exchangeRate;
            if (this.isCreateUrl && !sessionStorage.getItem('saveAndLoad')) {
                for (let i = 0; i < this.gOtherVoucherDetails.length; i++) {
                    this.utilService.calcular(
                        this.gOtherVoucherDetails[i],
                        700,
                        'amountOriginal',
                        this.gOtherVoucher,
                        this.gOtherVoucherDetails,
                        this.gOtherVoucherDetailTax,
                        this.currentAccount,
                        this.getAmountOriginalType(),
                        this.currentCurrency.formula.includes('*') ? this.gOtherVoucher.exchangeRate : 1 / this.gOtherVoucher.exchangeRate
                    );
                }
            }
        }
        if (this.gOtherVoucherDetails && this.currentCurrency && this.currentCurrency.formula) {
            for (let i = 0; i < this.gOtherVoucherDetails.length; i++) {
                this.gOtherVoucherDetails[i].amount = this.round(
                    this.gOtherVoucherDetails[i].amountOriginal *
                        (this.currentCurrency.formula.includes('*')
                            ? this.gOtherVoucher.exchangeRate
                            : 1 / this.gOtherVoucher.exchangeRate),
                    7
                );
                sum = sum + this.gOtherVoucherDetails[i].amount;
            }
        }
        this.gOtherVoucher.totalAmount = sum;
    }

    selectChangeExchangeRate() {
        let sum = 0;
        if (this.gOtherVoucherDetails && this.currentCurrency && this.currentCurrency.formula) {
            for (let i = 0; i < this.gOtherVoucherDetails.length; i++) {
                this.gOtherVoucherDetails[i].amount = this.round(
                    this.gOtherVoucherDetails[i].amountOriginal *
                        (this.currentCurrency.formula.includes('*')
                            ? this.gOtherVoucher.exchangeRate
                            : 1 / this.gOtherVoucher.exchangeRate),
                    7
                );
                sum = sum + this.gOtherVoucherDetails[i].amount;
            }
        }
        this.gOtherVoucher.totalAmount = sum;
        if (this.isCreateUrl) {
            for (let i = 0; i < this.gOtherVoucherDetails.length; i++) {
                this.utilService.calcular(
                    this.gOtherVoucherDetails[i],
                    700,
                    'amountOriginal',
                    this.gOtherVoucher,
                    this.gOtherVoucherDetails,
                    this.gOtherVoucherDetailTax,
                    this.currentAccount,
                    this.getAmountOriginalType(),
                    this.currentCurrency.formula.includes('*') ? this.gOtherVoucher.exchangeRate : 1 / this.gOtherVoucher.exchangeRate
                );
            }
        }
    }

    AddnewRow(eventData: any, select: number) {
        if (this.isCreateUrl) {
            if (select === 0) {
                this.gOtherVoucherDetails.push(Object.assign({}, this.gOtherVoucherDetails[this.gOtherVoucherDetails.length - 1]));
                if (this.gOtherVoucherDetails.length === 1) {
                    this.gOtherVoucher.totalAmountOriginal = 0;
                    this.gOtherVoucher.totalAmount = 0;
                    // if (this.gOtherVoucher.accountingObjectID) {
                    //     this.gOtherVoucherDetails[0].accountingObjectID = this.gOtherVoucher.accountingObjectID;
                    // }
                    if (this.gOtherVoucher.reason) {
                        this.gOtherVoucherDetails[0].description = this.gOtherVoucher.reason;
                    }
                }
                this.gOtherVoucherDetails[this.gOtherVoucherDetails.length - 1].id = undefined;
                if (this.iAutoPrinciple) {
                    this.gOtherVoucherDetails[this.gOtherVoucherDetails.length - 1].debitAccount = this.iAutoPrinciple.debitAccount;
                    this.gOtherVoucherDetails[this.gOtherVoucherDetails.length - 1].creditAccount = this.iAutoPrinciple.creditAccount;
                }
                this.gOtherVoucherDetails[this.gOtherVoucherDetails.length - 1].amount = 0;
                this.gOtherVoucherDetails[this.gOtherVoucherDetails.length - 1].amountOriginal = 0;
                // this.gOtherVoucherDetails[this.gOtherVoucherDetails.length - 1].orderPriority = this.gOtherVoucherDetails.length - 1;
                const nameTag: string = event.srcElement.id;
                const index: number = this.gOtherVoucherDetails.length - 1;
                const nameTagIndex: string = nameTag + String(index);
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(nameTagIndex);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            } else if (select === 1) {
                this.gOtherVoucherDetailTax.push(Object.assign({}, this.gOtherVoucherDetailTax[this.gOtherVoucherDetailTax.length - 1]));
                // if (this.gOtherVoucherDetailTax.length === 1) {
                //     if (this.gOtherVoucher.accountingObjectID) {
                //         this.gOtherVoucherDetailTax[0].accountingObjectID = this.gOtherVoucher.accountingObjectID;
                //     }
                // }
                this.gOtherVoucherDetailTax[this.gOtherVoucherDetailTax.length - 1].description = 'Thuế GTGT';
                this.gOtherVoucherDetailTax[this.gOtherVoucherDetailTax.length - 1].id = undefined;
                this.gOtherVoucherDetailTax[this.gOtherVoucherDetailTax.length - 1].vATAmount = 0;
                this.gOtherVoucherDetailTax[this.gOtherVoucherDetailTax.length - 1].vATAmountOriginal = 0;
                this.gOtherVoucherDetailTax[this.gOtherVoucherDetailTax.length - 1].pretaxAmount = 0;
                this.gOtherVoucherDetailTax[this.gOtherVoucherDetailTax.length - 1].pretaxAmountOriginal = 0;
                if (this.isShowGoodsServicePurchase) {
                    this.gOtherVoucherDetailTax[
                        this.gOtherVoucherDetailTax.length - 1
                    ].goodsServicePurchaseID = this.goodsServicePurchaseID;
                }
                // this.gOtherVoucherDetailTax[this.gOtherVoucherDetailTax.length - 1].orderPriority = this.gOtherVoucherDetailTax.length - 1;
                const nameTag: string = event.srcElement.id;
                const index: number = this.gOtherVoucherDetailTax.length - 1;
                const nameTagIndex: string = nameTag + String(index);
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(nameTagIndex);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            } else {
            }
        }
    }

    KeyPress(value: number, select: number) {
        if (select === 0) {
            this.gOtherVoucherDetails.splice(value, 1);
        } else if (select === 1) {
            this.gOtherVoucherDetailTax.splice(value, 1);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuNghiepVuKhac_GhiSo])
    record() {
        event.preventDefault();
        if (
            !this.gOtherVoucher.recorded &&
            !this.isCreateUrl &&
            !this.checkCloseBook(this.currentAccount, this.gOtherVoucher.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            if (this.gOtherVoucher.id) {
                // if (this.gOtherVoucher.recorded) {
                //     this.isRecord = false;
                // } else {
                //     this.isRecord = true;
                // }
                this.record_ = {};
                this.record_.id = this.gOtherVoucher.id;
                this.record_.typeID = this.gOtherVoucher.typeID;
                if (!this.gOtherVoucher.recorded) {
                    this.gLService.record(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                        if (res.body.success) {
                            this.gOtherVoucher.recorded = true;
                            this.toastr.success(
                                this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
                                this.translate.instant('ebwebApp.mBCreditCard.message')
                            );
                            this.disableEditButton = true;
                            this.isRecord = false;
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
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuNghiepVuKhac_GhiSo])
    unrecord() {
        event.preventDefault();
        if (
            this.gOtherVoucher.recorded &&
            !this.isCreateUrl &&
            !this.checkCloseBook(this.currentAccount, this.gOtherVoucher.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            if (this.gOtherVoucher.id) {
                // if (this.gOtherVoucher.recorded) {
                //     this.isRecord = false;
                // } else {
                //     this.isRecord = true;
                // }
                this.record_ = {};
                this.record_.id = this.gOtherVoucher.id;
                this.record_.typeID = this.gOtherVoucher.typeID;
                if (this.gOtherVoucher.recorded) {
                    this.gLService.unrecord(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                        if (res.body.success) {
                            this.gOtherVoucher.recorded = false;
                            this.toastr.success(
                                this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                                this.translate.instant('ebwebApp.mBCreditCard.message')
                            );
                        }
                    });
                    this.disableEditButton = false;
                    this.isRecord = true;
                }
            }
        }
    }

    CTKTExportPDF(isDownload, typeReports: number) {
        if (!this.isCreateUrl) {
            this.gOtherVoucherService
                .getCustomerReport({
                    id: this.gOtherVoucher.id,
                    typeID: 700,
                    typeReport: typeReports
                })
                .subscribe(response => {
                    // this.showReport(response);
                    const file = new Blob([response.body], { type: 'application/pdf' });
                    const fileURL = window.URL.createObjectURL(file);

                    if (isDownload) {
                        const link = document.createElement('a');
                        document.body.appendChild(link);
                        link.download = fileURL;
                        link.setAttribute('style', 'display: none');
                        const name = 'Bao_co.pdf';
                        link.setAttribute('download', name);
                        link.href = fileURL;
                        link.click();
                    } else {
                        const contentDispositionHeader = response.headers.get('Content-Disposition');
                        const result = contentDispositionHeader
                            .split(';')[1]
                            .trim()
                            .split('=')[1];
                        const newWin = window.open(fileURL, '_blank');

                        // add a load listener to the window so that the title gets changed on page load
                        newWin.addEventListener('load', function() {
                            newWin.document.title = result.replace(/"/g, '');
                            // this.router.navigate(['/report/buy']);
                        });
                    }
                });
            this.toastr.success(
                this.translate.instant('ebwebApp.mBCreditCard.printing') + this.translate.instant('ebwebApp.mBCreditCard.financialPaper'),
                this.translate.instant('ebwebApp.mBCreditCard.message')
            );
        }
    }

    calcular(objcet1: object, typeID: number, name: string, objectParent: object, objectDT1: object[], objectDT2: object[]) {
        if (this.isCreateUrl) {
            const count = this.gOtherVoucherDetailTax.length;
            this.utilService.calcular(
                objcet1,
                typeID,
                name,
                objectParent,
                objectDT1,
                objectDT2,
                this.currentAccount,
                this.getAmountOriginalType(),
                this.currentCurrency.formula.includes('*') ? this.gOtherVoucher.exchangeRate : 1 / this.gOtherVoucher.exchangeRate
            );
            console.log(objectDT2.length);
            if (count < objectDT2.length) {
                if (this.isShowGoodsServicePurchase && this.currentAccount) {
                    this.gOtherVoucherDetailTax[count].goodsServicePurchaseID = this.currentAccount.organizationUnit.goodsServicePurchaseID;
                }
            }
        }
    }

    // ham lui, tien
    previousEdit() {
        // goi service get by row num
        if (this.rowNum !== this.count) {
            this.utilsService
                .findByRowNum({
                    id: this.gOtherVoucher.id,
                    isNext: false,
                    typeID: this.TYPE_CHUNG_TU_NGHIEP_VU_KHAC,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IGOtherVoucher>) => {
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
                    id: this.gOtherVoucher.id,
                    isNext: true,
                    typeID: this.TYPE_CHUNG_TU_NGHIEP_VU_KHAC,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IGOtherVoucher>) => {
                        // this.router.navigate(['/mc-payment', res.body.id, 'edit']);
                        this.navigate(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    navigate(iGOtherVoucher: IGOtherVoucher) {
        switch (iGOtherVoucher.typeID) {
            case this.TYPE_CHUNG_TU_NGHIEP_VU_KHAC:
                this.router.navigate(['/g-other-voucher', iGOtherVoucher.id, 'edit']);
                break;
            case this.TYPE_KET_CHUYEN_LAI_LO:
                this.router.navigate(['./ket-chuyen-lai-lo', iGOtherVoucher.id, 'edit', 'from-g-other-voucher']);
                break;
            case this.TYPE_PHAN_BO_CHI_PHI_TRA_TRUOC:
                this.router.navigate(['./phan-bo-chi-phi-tra-truoc', iGOtherVoucher.id, 'edit', 'from-g-other-voucher']);
                break;
        }
    }

    // end of lui tien
    onError(errorMessage: string) {
        this.toastr.error(this.translate.instant('ebwebApp.mBCreditCard.error'), this.translate.instant('ebwebApp.mBCreditCard.message'));
    }

    onRightClick($event, data, selectedData, isNew, isDelete, select, currentRow) {
        if (this.isCreateUrl) {
            this.contextMenu.isNew = isNew;
            this.contextMenu.isDelete = isDelete;
            this.contextMenu.isShow = true;
            this.contextMenu.event = $event;
            this.contextMenu.data = data;
            this.contextMenu.selectedData = selectedData;
            if (select === 0 || select === 1 || select === 2) {
                this.contextMenu.isCopy = true;
            } else {
                this.contextMenu.isCopy = false;
            }
            this.select = select;
            this.currentRow = currentRow;
        }
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChungTuNghiepVuKhac_Xoa])
    delete() {
        event.preventDefault();
        if (
            !this.isCreateUrl &&
            !this.checkCloseBook(this.currentAccount, this.gOtherVoucher.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            if (this.gOtherVoucher.recorded) {
                // this.toastr.error(
                //     this.translate.instant('ebwebApp.mBCreditCard.errorDeleteVoucherNo'),
                //     this.translate.instant('ebwebApp.mBCreditCard.message')
                // );
                return;
            } else {
                this.router.navigate(['/g-other-voucher', { outlets: { popup: this.gOtherVoucher.id + '/delete' } }]);
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
    saveAndNew() {
        event.preventDefault();
        this.warningMessage = '';
        this.gOtherVoucher.gOtherVoucherDetails = this.gOtherVoucherDetails;
        this.gOtherVoucher.gOtherVoucherDetailTax = this.gOtherVoucherDetailTax;
        if (this.isCreateUrl && !this.utilsService.isShowPopup) {
            if (this.checkError()) {
                let totalVatAmount = 0,
                    totalVatAmountOriginal = 0;
                for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetailTax.length; i++) {
                    this.gOtherVoucher.gOtherVoucherDetailTax[i].pretaxAmountOriginal = this.round(
                        this.gOtherVoucher.gOtherVoucherDetailTax[i].pretaxAmount /
                            (this.currentCurrency.formula.includes('*')
                                ? this.gOtherVoucher.exchangeRate
                                : 1 / this.gOtherVoucher.exchangeRate),
                        8
                    );
                    this.gOtherVoucher.gOtherVoucherDetailTax[i].vATAmountOriginal = this.round(
                        this.gOtherVoucher.gOtherVoucherDetailTax[i].vATAmount /
                            (this.currentCurrency.formula.includes('*')
                                ? this.gOtherVoucher.exchangeRate
                                : 1 / this.gOtherVoucher.exchangeRate),
                        8
                    );
                    this.gOtherVoucher.gOtherVoucherDetailTax[i].orderPriority = i + 1;
                    totalVatAmount += this.gOtherVoucherDetailTax[i].vATAmount;
                    totalVatAmountOriginal += this.gOtherVoucherDetailTax[i].vATAmountOriginal;
                }
                if (this.gOtherVoucher && this.gOtherVoucher.gOtherVoucherDetails && this.gOtherVoucherDetails.length > 0) {
                    for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetails.length; i++) {
                        this.gOtherVoucher.gOtherVoucherDetails[i].orderPriority = i + 1;
                    }
                }
                this.isCreateUrl = !this.isCreateUrl;
                this.isEdit = this.isCreateUrl;
                this.disableAddButton = true;
                if (this.gOtherVoucher.typeLedger === 0) {
                    this.gOtherVoucher.noFBook = this.no;
                    this.gOtherVoucher.noMBook = null;
                } else if (this.gOtherVoucher.typeLedger === 1) {
                    this.gOtherVoucher.noFBook = null;
                    this.gOtherVoucher.noMBook = this.no;
                } else {
                    if (this.isSoTaiChinh) {
                        this.gOtherVoucher.noFBook = this.no;
                    } else {
                        this.gOtherVoucher.noMBook = this.no;
                    }
                }
                this.gOtherVoucher.typeID = 700;
                this.isSaving = true;
                // check is url new
                if (this.isCreateUrl && this.gOtherVoucher.id) {
                    this.gOtherVoucher.id = undefined;
                }
                if (!this.isCreateUrl && !this.isEditUrl) {
                    this.gOtherVoucher.id = undefined;
                    for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetails.length; i++) {
                        this.gOtherVoucher.gOtherVoucherDetails[i].id = undefined;
                    }
                    for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetailTax.length; i++) {
                        this.gOtherVoucher.gOtherVoucherDetailTax[i].id = undefined;
                    }
                }
                if (!this.isCreateUrl && !this.isEditUrl) {
                    this.gOtherVoucher.id = undefined;
                }
                if (this.gOtherVoucher.id !== undefined) {
                    this.subscribeToSaveResponseAndContinue(this.gOtherVoucherService.update(this.gOtherVoucher));
                } else {
                    this.subscribeToSaveResponseAndContinue(this.gOtherVoucherService.create(this.gOtherVoucher));
                }
            } else {
            }
        }
    }

    subscribeToSaveResponseAndContinue(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    if (this.isEditUrl) {
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBDeposit.editSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    } else {
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBDeposit.insertSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    this.onSaveSuccess();
                    this.gOtherVoucher = {};
                    this.gOtherVoucher.recorded = false;
                    this.gOtherVoucherDetails = [];
                    this.gOtherVoucherDetailTax = [];
                    this.viewVouchersSelected = [];
                    this.copy();
                    this.router.navigate(['g-other-voucher/new']);
                    this.isEdit = this.isCreateUrl = true;
                    this.gOtherVoucher.date = this.utilService.ngayHachToan(this.currentAccount);
                    this.gOtherVoucher.postedDate = this.gOtherVoucher.date;
                    if (this.TCKHAC_SDSoQuanTri === '0') {
                        this.gOtherVoucher.typeLedger = 0;
                        this.sysTypeLedger = 0;
                        this.isHideTypeLedger = true;
                    } else {
                        if (this.isSoTaiChinh) {
                            this.gOtherVoucher.typeLedger = 2;
                            this.sysTypeLedger = 0;
                        } else {
                            this.gOtherVoucher.typeLedger = 2;
                            this.sysTypeLedger = 1;
                        }
                    }
                    this.selectChangeExchangeRate();
                    this.utilService
                        .getGenCodeVoucher({
                            typeGroupID: 70, // typeGroupID loại chứng từ
                            companyID: '', // ID công ty
                            branchID: '', // ID chi nhánh
                            displayOnBook: this.sysTypeLedger // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
                        })
                        .subscribe((res2: HttpResponse<string>) => {
                            // this.mCReceipt.noFBook = (res.body.toString());
                            this.no = res2.body;
                            this.gOtherVoucher.currencyID = this.currency.currencyCode;
                            this.translate.get(['ebwebApp.gOtherVoucher.defaultReason']).subscribe(res => {
                                this.gOtherVoucher.reason = res['ebwebApp.gOtherVoucher.defaultReason'];
                            });
                            if (this.isCreateUrl) {
                                this.selectChangeCurrency();
                            }
                            this.copy();
                        });
                    this.copy();
                    this.isReadOnly = true;
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                    return;
                } else if (res.body.status === 2) {
                    if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.gOtherVoucher.id = res.body.gOtherVoucher.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyQT'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.copy();
                        this.router.navigate(['/g-other-voucher', res.body.gOtherVoucher.id, 'edit']).then(() => {
                            this.isReadOnly = true;
                            this.router.navigate(['/g-other-voucher', 'new']);
                        });
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.gOtherVoucher.id = res.body.gOtherVoucher.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyTC'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.copy();
                        this.router.navigate(['/g-other-voucher', res.body.gOtherVoucher.id, 'edit']).then(() => {
                            this.isReadOnly = true;
                            this.router.navigate(['/g-other-voucher', 'new']);
                        });
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.gOtherVoucher.id = res.body.gOtherVoucher.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuy'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.copy();
                        this.router.navigate(['/g-other-voucher', res.body.gOtherVoucher.id, 'edit']).then(() => {
                            this.isReadOnly = true;
                            this.router.navigate(['/g-other-voucher', 'new']);
                        });
                    } else {
                        this.recordFailed();
                    }
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    registerRef() {
        this.eventSubscriber = this.eventManager.subscribe('selectViewVoucher', response => {
            if (this.isCreateUrl) {
                this.viewVouchersSelected = response.content;
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            if (this.isCreateUrl) {
                this.modalRef = this.refModalService.open(this.viewVouchersSelected);
            }
        }
    }

    getTotalVATAmount() {
        if (this.gOtherVoucherDetailTax && this.gOtherVoucherDetailTax.length > 0) {
            return this.gOtherVoucherDetailTax.map(n => n.vATAmount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getAmountOriginal() {
        if (this.gOtherVoucherDetails && this.gOtherVoucherDetails.length > 0) {
            return this.gOtherVoucherDetails.map(n => n.amountOriginal).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getAmount() {
        if (this.gOtherVoucherDetails && this.gOtherVoucherDetails.length > 0) {
            return this.gOtherVoucherDetails.map(n => n.amount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getVATAmount() {
        if (this.gOtherVoucherDetailTax && this.gOtherVoucherDetailTax.length > 0) {
            return this.gOtherVoucherDetailTax.map(n => n.vATAmount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getPretaxAmount() {
        if (this.gOtherVoucherDetailTax && this.gOtherVoucherDetailTax.length > 0) {
            return this.gOtherVoucherDetailTax.map(n => n.pretaxAmount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    closeAll() {
        this.copy();
        if (sessionStorage.getItem('dataSearchGOtherVoucher')) {
            this.router.navigate(['/g-other-voucher', 'hasSearch', '1']);
        } else {
            this.router.navigate(['/g-other-voucher']);
        }
    }

    saveContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.save();
    }

    close() {
        this.copy();
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.closeAll();
    }

    khoiTaoGiaTri() {
        this.gOtherVoucher = {};
        this.gOtherVoucherDetails = [];
        this.gOtherVoucherDetailTax = [];
        this.viewVouchersSelected = [];
        this.gOtherVoucherCopy = Object.assign({});
        this.gOtherVoucherDetailsCopy = this.gOtherVoucherDetails.map(object => ({ ...object }));
        this.gOtherVoucherDetailTaxCopy = this.gOtherVoucherDetailTax.map(object => ({ ...object }));
        this.viewVouchersSelectedCopy = this.viewVouchersSelected.map(object => ({ ...object }));
    }

    copy() {
        this.gOtherVoucherCopy = Object.assign({}, this.gOtherVoucher);
        this.gOtherVoucherDetailsCopy = this.gOtherVoucherDetails.map(object => ({ ...object }));
        this.gOtherVoucherDetailTaxCopy = this.gOtherVoucherDetailTax.map(object => ({ ...object }));
        this.viewVouchersSelectedCopy = this.viewVouchersSelected.map(object => ({ ...object }));
    }

    noVoucherExist() {
        this.toastr.error(
            this.translate.instant('global.data.noVocherAlreadyExist'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
        this.isCreateUrl = !this.isCreateUrl;
        this.isEdit = this.isCreateUrl;
    }

    exit() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    /*
    * hàm ss du lieu 2 object và 2 mảng object
    * return true: neu tat ca giong nhau
    * return fale: neu 1 trong cac object ko giong nshau
    * */
    canDeactive(): boolean {
        if (this.isReadOnly || this.isReadOnly === undefined) {
            return true;
        } else {
            return (
                this.utilService.isEquivalent(this.gOtherVoucher, this.gOtherVoucherCopy) &&
                this.utilService.isEquivalentArray(this.gOtherVoucherDetails, this.gOtherVoucherDetailsCopy) &&
                this.utilService.isEquivalentArray(this.gOtherVoucherDetailTax, this.gOtherVoucherDetailTaxCopy) &&
                this.utilsService.isEquivalentArray(this.viewVouchersSelected, this.viewVouchersSelectedCopy)
            );
        }
    }

    checkError(): boolean {
        if (!this.no) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.voucherNumber') +
                ' ' +
                this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        if (this.checkCloseBook(this.currentAccount, this.gOtherVoucher.postedDate)) {
            this.toastr.error(this.translate.instant('ebwebApp.mBCreditCard.checkCloseBook'));
            return false;
        }
        if (!this.gOtherVoucher.date) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.date') + ' ' + this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        if (!this.gOtherVoucher.postedDate) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.postedDate') +
                ' ' +
                this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        if (this.gOtherVoucher.postedDate.format(DATE_FORMAT) < this.gOtherVoucher.date.format(DATE_FORMAT)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBCreditCard.errorPostedDateAndDate'),
                this.translate.instant('ebwebApp.mBCreditCard.message')
            );
            return false;
        }
        if (!this.gOtherVoucher.currencyID || this.gOtherVoucher.currencyID === '') {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.detail.currencyType') +
                ' ' +
                this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        if (!this.gOtherVoucher.exchangeRate) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.exchangeRate') +
                ' ' +
                this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        if (this.gOtherVoucher.typeLedger === undefined || this.gOtherVoucher.typeLedger === null) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.typeLedger') +
                ' ' +
                this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        if (!this.utilService.checkNoBook(this.no ? this.no : null, this.currentAccount)) {
            return false;
        }
        if (this.gOtherVoucher.gOtherVoucherDetails.length === 0) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.home.details') +
                ' ' +
                this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        for (this.i = 0; this.i < this.gOtherVoucher.gOtherVoucherDetails.length; this.i++) {
            if (
                !this.gOtherVoucher.gOtherVoucherDetails[this.i].debitAccount ||
                this.gOtherVoucher.gOtherVoucherDetails[this.i].debitAccount === ''
            ) {
                this.warningMessage =
                    this.translate.instant('ebwebApp.mBCreditCard.debitAccount') +
                    ' ' +
                    this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
                this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
                return false;
            }
            if (
                !this.gOtherVoucher.gOtherVoucherDetails[this.i].creditAccount ||
                this.gOtherVoucher.gOtherVoucherDetails[this.i].creditAccount === ''
            ) {
                this.warningMessage =
                    this.translate.instant('ebwebApp.mBCreditCard.creditAccount') +
                    ' ' +
                    this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
                this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
                return false;
            }
        }
        const checkAcc = this.utilsService.checkAccoutWithDetailType(
            this.debitAccountList,
            this.creditAccountList,
            this.gOtherVoucherDetails,
            this.accountingObjects,
            this.costSets,
            this.eMContracts,
            null,
            this.bankAccountDetails,
            this.organizationUnits,
            this.expenseItems,
            this.budgetItems,
            this.statisticCodes,
            null,
            true
        );
        if (this.gOtherVoucherDetailTax && this.gOtherVoucherDetailTax.length > 0) {
            for (let i = 0; i < this.gOtherVoucherDetailTax.length; i++) {
                if (!this.gOtherVoucherDetailTax[i].vATAccount) {
                    this.toastr.error(this.translate.instant('ebwebApp.gOtherVoucher.notBeBlank'));
                    return false;
                }
                if (this.gOtherVoucherDetailTax[i].taxCode) {
                    if (!this.utilService.checkMST(this.gOtherVoucherDetailTax[i].taxCode)) {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.mCReceipt.error.taxCodeInvalid'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        return false;
                    }
                }
            }
        }
        if (checkAcc) {
            this.toastr.error(checkAcc, this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        if (!this.checkNhapTien()) {
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.modalRef = this.modalService.open(this.modalComponentAmount, { backdrop: 'static' });
            return false;
        }
        return true;
    }

    checkNhapTien() {
        /*Check nhập tiền giữa 2 tab*/
        let result = true;
        let keepGoing = true;
        // có bên hàng tiền mà không có bên thuế
        this.gOtherVoucherDetails.filter(n => n.debitAccount.startsWith('133')).forEach(n => {
            if (keepGoing) {
                if (this.gOtherVoucherDetailTax.filter(m => m.vATAccount === n.debitAccount).length === 0) {
                    result = false;
                    keepGoing = false;
                }
            }
        });
        // Có bên tab thuế mà k có bên hàng tiền
        this.gOtherVoucherDetailTax.filter(n => n.vATAccount.startsWith('133')).forEach(n => {
            if (keepGoing) {
                if (this.gOtherVoucherDetails.filter(m => m.debitAccount === n.vATAccount).length === 0) {
                    result = false;
                    keepGoing = false;
                }
            }
        });
        // fill tiền không hợp lệ
        this.gOtherVoucherDetails.forEach(n => {
            if (keepGoing) {
                const tax = this.gOtherVoucherDetailTax.find(
                    m => m.vATAccount === n.debitAccount && m.vATAccount !== null && m.vATAccount !== undefined && m.vATAccount !== ''
                );
                if (tax) {
                    if (tax.vATAccount.startsWith('133', 0)) {
                        if (!n.amountOriginal && tax.vATAmount) {
                            // this.toastr.error('Lỗi nhập tiền', this.translate.instant('ebwebApp.mCReceipt.error.error'));
                            result = false;
                            keepGoing = false;
                        }
                        if (result) {
                            const total133 = this.gOtherVoucherDetails
                                .filter(t => t.debitAccount === tax.vATAccount)
                                .map(t => t.amount)
                                .reduce((a, b) => a + b);
                            if (total133 !== tax.vATAmount) {
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

    // continueSave() {
    //     let totalVatAmount = 0,
    //         totalVatAmountOriginal = 0;
    //     for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetailTax.length; i++) {
    //         this.gOtherVoucher.gOtherVoucherDetailTax[i].pretaxAmountOriginal = this.round(
    //             this.gOtherVoucher.gOtherVoucherDetailTax[i].pretaxAmount /
    //                 (this.currentCurrency.formula.includes('*') ? this.gOtherVoucher.exchangeRate : 1 / this.gOtherVoucher.exchangeRate),
    //             8
    //         );
    //         this.gOtherVoucher.gOtherVoucherDetailTax[i].vATAmountOriginal = this.round(
    //             this.gOtherVoucher.gOtherVoucherDetailTax[i].vATAmount /
    //                 (this.currentCurrency.formula.includes('*') ? this.gOtherVoucher.exchangeRate : 1 / this.gOtherVoucher.exchangeRate),
    //             8
    //         );
    //         this.gOtherVoucher.gOtherVoucherDetailTax[i].orderPriority = i + 1;
    //         totalVatAmount += this.gOtherVoucherDetailTax[i].vATAmount;
    //         totalVatAmountOriginal += this.gOtherVoucherDetailTax[i].vATAmountOriginal;
    //     }
    //     for (let i = 0; i < this.gOtherVoucherDetails.length; i++) {
    //         this.gOtherVoucher.gOtherVoucherDetails[i].orderPriority = i + 1;
    //     }
    //     this.isCreateUrl = !this.isCreateUrl;
    //     this.isEdit = this.isCreateUrl;
    //     this.disableAddButton = true;
    //     if (this.gOtherVoucher.typeLedger === 0) {
    //         this.gOtherVoucher.noFBook = this.no;
    //         this.gOtherVoucher.noMBook = null;
    //     } else if (this.gOtherVoucher.typeLedger === 1) {
    //         this.gOtherVoucher.noFBook = null;
    //         this.gOtherVoucher.noMBook = this.no;
    //     } else {
    //         if (this.isSoTaiChinh) {
    //             this.gOtherVoucher.noFBook = this.no;
    //         } else {
    //             this.gOtherVoucher.noMBook = this.no;
    //         }
    //     }
    //     this.gOtherVoucher.typeID = 700;
    //     if (this.sysRecord.data === '0') {
    //         this.gOtherVoucher.recorded = true;
    //     } else {
    //         this.gOtherVoucher.recorded = false;
    //     }
    //     this.isSaving = true;
    //     this.gOtherVoucher.gOtherVoucherDetails = this.gOtherVoucherDetails;
    //     this.gOtherVoucher.gOtherVoucherDetailTax = this.gOtherVoucherDetailTax;
    //     for (this.i = 0; this.i < this.gOtherVoucher.gOtherVoucherDetails.length; this.i++) {
    //         // this.gOtherVoucher.gOtherVoucherDetails[this.i].orderPriority = this.i + 1;
    //     }
    //     // for (this.i = 0; this.i < this.gOtherVoucher.gOtherVoucherDetailTax.length; this.i++) {
    //     //     this.gOtherVoucher.gOtherVoucherDetailTax[this.i].pretaxAmountOriginal = this.gOtherVoucher.gOtherVoucherDetailTax[
    //     //         this.i
    //     //         ].pretaxAmount;
    //     //     this.gOtherVoucher.gOtherVoucherDetailTax[this.i].vATAmountOriginal = this.gOtherVoucher.gOtherVoucherDetailTax[
    //     //         this.i
    //     //         ].vATAmount;
    //     //     this.gOtherVoucher.gOtherVoucherDetailTax[this.i].orderPriority = this.i + 1;
    //     // }
    //
    //     // check is url new
    //     if (this.isCreateUrl && this.gOtherVoucher.id) {
    //         this.gOtherVoucher.id = undefined;
    //     }
    //     if (!this.isCreateUrl && !this.isEditUrl) {
    //         this.gOtherVoucher.id = undefined;
    //         for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetails.length; i++) {
    //             this.gOtherVoucher.gOtherVoucherDetails[i].id = undefined;
    //         }
    //         for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetailTax.length; i++) {
    //             this.gOtherVoucher.gOtherVoucherDetailTax[i].id = undefined;
    //         }
    //     }
    //     if (!this.isCreateUrl && !this.isEditUrl) {
    //         this.gOtherVoucher.id = undefined;
    //     }
    //     if (this.gOtherVoucher.id) {
    //         this.subscribeToSaveResponse(this.gOtherVoucherService.update(this.gOtherVoucher));
    //     } else {
    //         this.subscribeToSaveResponse(this.gOtherVoucherService.create(this.gOtherVoucher));
    //     }
    // }

    continueModalSaveComponent() {
        this.isCreateUrl = !this.isCreateUrl;
        this.isEdit = this.isCreateUrl;
        this.disableAddButton = true;
        if (this.gOtherVoucher.typeLedger === 0) {
            this.gOtherVoucher.noFBook = this.no;
            this.gOtherVoucher.noMBook = null;
        } else if (this.gOtherVoucher.typeLedger === 1) {
            this.gOtherVoucher.noFBook = null;
            this.gOtherVoucher.noMBook = this.no;
        } else {
            if (this.isSoTaiChinh) {
                this.gOtherVoucher.noFBook = this.no;
            } else {
                this.gOtherVoucher.noMBook = this.no;
            }
        }
        this.gOtherVoucher.typeID = 700;
        // if (this.sysRecord.data === '0') {
        //     this.gOtherVoucher.recorded = true;
        // } else {
        //     this.gOtherVoucher.recorded = false;
        // }
        this.isSaving = true;
        // check is url new
        if (this.isCreateUrl && this.gOtherVoucher.id) {
            this.gOtherVoucher.id = undefined;
        }
        if (!this.isCreateUrl && !this.isEditUrl) {
            this.gOtherVoucher.id = undefined;
            for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetails.length; i++) {
                this.gOtherVoucher.gOtherVoucherDetails[i].id = undefined;
            }
            for (let i = 0; i < this.gOtherVoucher.gOtherVoucherDetailTax.length; i++) {
                this.gOtherVoucher.gOtherVoucherDetailTax[i].id = undefined;
            }
        }
        if (!this.isCreateUrl && !this.isEditUrl) {
            this.gOtherVoucher.id = undefined;
        }
        if (this.gOtherVoucher.id) {
            this.subscribeToSaveResponse(this.gOtherVoucherService.update(this.gOtherVoucher));
        } else {
            this.subscribeToSaveResponse(this.gOtherVoucherService.create(this.gOtherVoucher));
        }
    }

    closeModalSaveComponent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    keyDownAddRow(value: number, select: number) {
        if (!this.getSelectionText()) {
            if (select === 0) {
                if (value !== null && value !== undefined) {
                    const ob: IGOtherVoucherDetails = Object.assign({}, this.gOtherVoucherDetails[value]);
                    ob.id = undefined;
                    ob.orderPriority = undefined;
                    this.gOtherVoucherDetails.push(ob);
                } else {
                    this.gOtherVoucherDetails.push({});
                }
            } else if (select === 1) {
                if (value !== null && value !== undefined) {
                    const ob: IGOtherVoucherDetailTax = Object.assign({}, this.gOtherVoucherDetailTax[value]);
                    ob.id = undefined;
                    ob.orderPriority = undefined;
                    this.gOtherVoucherDetailTax.push(ob);
                } else {
                    this.gOtherVoucherDetailTax.push({});
                }
            }
            this.selectChangeTotalAmount();
        }
    }

    afterCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            if (this.select === 0 || this.select === 3) {
                const ob: IGOtherVoucherDetails = Object.assign({}, this.contextMenu.selectedData);
                ob.id = undefined;
                ob.orderPriority = undefined;
                this.gOtherVoucherDetails.push(ob);
                this.selectChangeTotalAmount();
            } else if (this.select === 1) {
                const ob: IGOtherVoucherDetailTax = Object.assign({}, this.contextMenu.selectedData);
                ob.id = undefined;
                ob.orderPriority = undefined;
                this.gOtherVoucherDetailTax.push(ob);
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    afterAddRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            if (this.select === 0 || this.select === 3) {
                this.gOtherVoucherDetails.push({ description: this.gOtherVoucher.reason, amount: 0, amountOriginal: 0 });
            } else if (this.select === 1) {
                this.gOtherVoucherDetailTax.push({ description: 'Thuế GTGT', vATAmount: 0, vATAmountOriginal: 0 });
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    selectChangeTotalAmount() {
        let totalAmount = 0;
        let totalAmountOriginal = 0;
        for (let i = 0; i < this.gOtherVoucherDetails.length; i++) {
            totalAmountOriginal += this.gOtherVoucherDetails[i].amountOriginal;
            this.gOtherVoucherDetails[i].amount = this.gOtherVoucherDetails[i].amountOriginal * this.gOtherVoucher.exchangeRate;
            totalAmount += this.gOtherVoucherDetails[i].amount;
        }
        this.gOtherVoucher.totalAmountOriginal = totalAmountOriginal;
        this.gOtherVoucher.totalAmount = totalAmount;
    }

    vATAccountChange() {
        this.updateAmountTaxWithAccount();
    }

    updateAmountTaxWithAccount() {
        for (let i = 0; i < this.gOtherVoucherDetailTax.length; i++) {
            if (
                this.gOtherVoucherDetailTax[i].vATAccount &&
                (this.gOtherVoucherDetailTax[i].vATAccount.startsWith('133', 0) ||
                    this.gOtherVoucherDetailTax[i].vATAccount.startsWith('3331', 0))
            ) {
                if (!this.gOtherVoucherDetails.find(n => n.debitAccount === this.gOtherVoucherDetailTax[i].vATAccount)) {
                    this.gOtherVoucherDetailTax[i].pretaxAmount = 0;
                    this.gOtherVoucherDetailTax[i].vATAmount = 0;
                    this.gOtherVoucherDetailTax[i].vATAmountOriginal = 0;
                } else if (!this.gOtherVoucherDetails.find(n => n.creditAccount === this.gOtherVoucherDetailTax[i].vATAccount)) {
                    this.gOtherVoucherDetailTax[i].pretaxAmount = 0;
                    this.gOtherVoucherDetailTax[i].vATAmount = 0;
                    this.gOtherVoucherDetailTax[i].vATAmountOriginal = 0;
                } else {
                    if (this.isCreateUrl) {
                        this.utilService.calcular(
                            this.gOtherVoucherDetails[i],
                            700,
                            'amountOriginal',
                            this.gOtherVoucher,
                            this.gOtherVoucherDetails,
                            this.gOtherVoucherDetailTax,
                            this.currentAccount,
                            this.getAmountOriginalType(),
                            this.currentCurrency.formula.includes('*')
                                ? this.gOtherVoucher.exchangeRate
                                : 1 / this.gOtherVoucher.exchangeRate
                        );
                    }
                }
            }
        }
    }

    changeAmount(index) {
        if (this.isCreateUrl) {
            let amount = 0;
            let vATRate = 0;
            let totalAmount = 0;
            let totalAmountOriginal = 0;
            for (let i = 0; i < this.gOtherVoucherDetailTax.length; i++) {
                amount = 0;
                if (this.gOtherVoucherDetailTax[i].vATAccount) {
                    if (this.gOtherVoucherDetailTax[i].vATAccount.startsWith('133')) {
                        for (let j = 0; j < this.gOtherVoucherDetails.length; j++) {
                            if (
                                this.gOtherVoucherDetails[j].debitAccount === this.gOtherVoucherDetailTax[i].vATAccount &&
                                !this.gOtherVoucherDetails[j].creditAccount.startsWith('33311')
                            ) {
                                amount += this.gOtherVoucherDetails[j].amount;
                            }
                        }
                        this.gOtherVoucherDetailTax[i].vATAmount = amount;
                        if (this.gOtherVoucherDetailTax[i].vATRate) {
                            if (
                                this.gOtherVoucherDetailTax[i].vATRate === 0 ||
                                this.gOtherVoucherDetailTax[i].vATRate === 3 ||
                                this.gOtherVoucherDetailTax[i].vATRate === 4
                            ) {
                                vATRate = 0;
                            } else if (this.gOtherVoucherDetailTax[i].vATRate === 1) {
                                vATRate = 0.05;
                            } else if (this.gOtherVoucherDetailTax[i].vATRate === 2) {
                                vATRate = 0.1;
                            }
                        } else {
                            vATRate = 0;
                        }
                        this.gOtherVoucherDetailTax[i].pretaxAmount = amount / vATRate;
                    } else if (this.gOtherVoucherDetailTax[i].vATAccount.startsWith('33311')) {
                        for (let j = 0; j < this.gOtherVoucherDetails.length; j++) {
                            if (
                                this.gOtherVoucherDetails[j].creditAccount === this.gOtherVoucherDetailTax[i].vATAccount &&
                                !this.gOtherVoucherDetails[j].debitAccount.startsWith('133')
                            ) {
                                amount += this.gOtherVoucherDetails[j].amount;
                            }
                            this.gOtherVoucherDetailTax[i].vATAmount = amount;
                            if (this.gOtherVoucherDetailTax[i].vATRate) {
                                if (
                                    this.gOtherVoucherDetailTax[i].vATRate === 0 ||
                                    this.gOtherVoucherDetailTax[i].vATRate === 3 ||
                                    this.gOtherVoucherDetailTax[i].vATRate === 4
                                ) {
                                    vATRate = 0;
                                } else if (this.gOtherVoucherDetailTax[i].vATRate === 1) {
                                    vATRate = 0.05;
                                } else if (this.gOtherVoucherDetailTax[i].vATRate === 2) {
                                    vATRate = 0.1;
                                }
                            } else {
                                vATRate = 0;
                            }
                            this.gOtherVoucherDetailTax[i].pretaxAmount = amount / vATRate;
                        }
                    }
                }
            }
            for (let i = 0; i < this.gOtherVoucherDetails.length; i++) {
                totalAmount += this.gOtherVoucherDetails[i].amount;
                totalAmountOriginal += this.gOtherVoucherDetails[i].amountOriginal;
            }
            this.gOtherVoucher.totalAmount = totalAmount;
            this.gOtherVoucher.totalAmountOriginal = totalAmountOriginal;
        }
    }

    print($event) {
        this.CTKTExportPDF(false, 0);
    }

    ngAfterViewInit(): void {
        if (this.isCreateUrl) {
            this.focusFirstInput();
        }
    }

    getAmountOriginalType() {
        if (this.isForeignCurrency()) {
            return 8;
        }
        return 7;
    }

    round(value, type) {
        if (type === 8) {
            if (this.isForeignCurrency()) {
                return this.utilService.round(value, this.currentAccount.systemOption, type);
            } else {
                return this.utilService.round(value, this.currentAccount.systemOption, 7);
            }
        } else if (type === 2) {
            if (this.isForeignCurrency()) {
                return this.utilService.round(value, this.currentAccount.systemOption, type);
            } else {
                return this.utilService.round(value, this.currentAccount.systemOption, 1);
            }
        } else {
            return this.utilService.round(value, this.currentAccount.systemOption, type);
        }
    }

    isForeignCurrency() {
        return this.currentAccount && this.gOtherVoucher.currencyID !== this.currentAccount.organizationUnit.currencyID;
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    selectChangeAccountingObjectTax(i) {
        if (this.gOtherVoucherDetailTax[i].accountingObjectID && this.accountingObjects) {
            const currentAccountingObject = this.accounting.find(a => a.id === this.gOtherVoucherDetailTax[i].accountingObjectID);
            this.gOtherVoucherDetailTax[i].accountingObjectName = currentAccountingObject.accountingObjectName;
            this.gOtherVoucherDetailTax[i].taxCode = currentAccountingObject.taxCode;
        }
    }

    selectChangeDate() {
        if (this.isEdit) {
            if (this.gOtherVoucher.date) {
                this.gOtherVoucher.postedDate = this.gOtherVoucher.date;
            }
        }
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.gOtherVoucherDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.gOtherVoucher;
    }

    addDataToDetail() {
        this.gOtherVoucherDetails = this.details ? this.details : this.gOtherVoucherDetails;
        this.gOtherVoucher = this.parent ? this.parent : this.gOtherVoucher;
    }

    sumAfterDeleteByContextMenu() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            if (this.isCreateUrl) {
                if (this.select === 0 || this.select === 3) {
                    if (this.gOtherVoucherDetails.length > 1) {
                        for (let j = 0; j < this.gOtherVoucherDetailTax.length; j++) {
                            if (this.gOtherVoucherDetailTax[j].vATAccount === this.gOtherVoucherDetails[this.currentRow].debitAccount) {
                                this.gOtherVoucherDetailTax.splice(j, 1);
                            }
                        }
                    }
                    this.gOtherVoucherDetails.splice(this.currentRow, 1);
                } else if (this.select === 1) {
                    this.gOtherVoucherDetailTax.splice(this.currentRow, 1);
                } else {
                    this.viewVouchersSelected.splice(this.currentRow, 1);
                    this.select = null;
                }
                let sumTotalAmountOriginal = 0;
                let sumTotalAmount = 0;
                for (let i = 0; i < this.gOtherVoucherDetails.length; i++) {
                    sumTotalAmount += this.gOtherVoucherDetails[i].amount;
                    sumTotalAmountOriginal += this.gOtherVoucherDetails[i].amountOriginal;
                    this.utilService.calcular(
                        this.gOtherVoucherDetails[i],
                        170,
                        'amountOriginal',
                        this.gOtherVoucher,
                        this.gOtherVoucherDetails,
                        this.gOtherVoucherDetailTax,
                        this.currentAccount,
                        this.getAmountOriginalType(),
                        this.currency.formula.includes('*') ? this.gOtherVoucher.exchangeRate : 1 / this.gOtherVoucher.exchangeRate
                    );
                }
                this.gOtherVoucher.totalAmount = sumTotalAmount;
                this.gOtherVoucher.totalAmountOriginal = sumTotalAmountOriginal;
            }
            this.selectChangeTotalAmount();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }
}
