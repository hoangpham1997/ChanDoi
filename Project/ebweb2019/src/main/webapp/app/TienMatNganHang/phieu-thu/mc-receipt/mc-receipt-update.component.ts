import { AfterViewChecked, AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IMCReceipt } from 'app/shared/model/mc-receipt.model';
import { MCReceiptService } from './mc-receipt.service';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { ITransportMethod } from 'app/shared/model/transport-method.model';
import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { IMCReceiptDetails } from 'app/shared/model/mc-receipt-details.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { CostSetService } from 'app/entities/cost-set';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IMCReceiptDetailTax } from 'app/shared/model/mc-receipt-detail-tax.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ExpenseItemService } from 'app/entities/expense-item';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { Irecord } from 'app/shared/model/record';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { EMContractService } from 'app/entities/em-contract';
import { MBTellerPaperDetails } from 'app/shared/model/mb-teller-paper-details.model';
import { MBTellerPaperDetailTax } from 'app/shared/model/mb-teller-paper-detail-tax.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
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
import * as moment from 'moment';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { DATE_FORMAT } from 'app/shared';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { IMCReceiptDetailCustomer } from 'app/shared/model/mc-receipt-detail-customer.model';
import { ROLE } from 'app/role.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { IAccountList } from 'app/shared/model/account-list.model';

@Component({
    selector: 'eb-mc-receipt-update',
    templateUrl: './mc-receipt-update.component.html',
    styleUrls: ['./mc-receipt-update.component.css']
})
export class MCReceiptUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewChecked, AfterViewInit {
    @ViewChild('content') public modalComponent: NgbModalRef;
    TYPE_MC_RECEIPT = 100;
    TYPE_GROUP_MC_RECEIPT = 10;
    TYPE_MC_RECEIPT_FROM_SAINVOICE = 102;
    TYPE_MC_RECEIPT_CUSTOM = 101;
    TCKHAC_GhiSo: boolean;
    account: any;
    private _mCReceipt: IMCReceipt;
    isSaving: boolean;

    accountingObjectData: IAccountingObject[]; // Đối tượng

    currencys: ICurrency[]; //
    autoPrinciples: IAutoPrinciple[];
    autoPrinciple: IAutoPrinciple;
    expenseItems: IExpenseItem[]; // Khoản mục chi phí
    dateDp: any;
    postedDateDp: any;
    isRecord: boolean;
    listcolumnString: string[];
    listHeadercolumnString: string[];
    eMContracts: IEMContract[];
    statusVoucher: number;
    currentRow: number;
    // --------
    mCReceiptDetails: IMCReceiptDetails[];
    mCReceiptDetailTaxs: IMCReceiptDetailTax[];
    //
    // region tiến lùi chứng từ
    rowNum: number;
    count: number;
    searchVoucher: ISearchVoucher;
    // endregion
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
    creditAccountList: IAccountList[];
    debitAccountList: IAccountList[];
    vatAccountList: any;
    no: string;
    mCReceiptCopy: IMCReceipt;
    mCReceiptDetailsCopy: IMCReceiptDetails[];
    mCReceiptDetailTaxsCopy: IMCReceiptDetailTax[];
    viewVouchersSelectedCopy: any;
    soDangLamViec: any;
    currencyCode: string;
    currency?: ICurrency;
    dateStr: string;
    postedDateStr: string;
    validateDate: boolean;
    validatePostedDate: boolean;
    isCloseAll: boolean;
    isSaveAndNew: boolean;
    contextMenu: ContextMenu;
    select: number;
    isViewFromRef: boolean;
    /*Phiếu thu khách hàng*/
    mcReceiptDetailCustomers: IMCReceiptDetailCustomer[];
    DDSo_NgoaiTe = DDSo_NgoaiTe;
    DDSo_TienVND = DDSo_TienVND;
    DDSo_TyLe = DDSo_TyLe;

    /*Phiếu thu khách hàng*/

    /*Phân quyền*/
    ROLE = ROLE;
    ROLE_Them = ROLE.PhieuThu_Them;
    ROLE_Sua = ROLE.PhieuThu_Sua;
    ROLE_Xoa = ROLE.PhieuThu_Xoa;
    ROLE_GhiSo = ROLE.PhieuThu_GhiSo;
    ROLE_KetXuat = ROLE.PhieuThu_KetXuat;

    // accountDefaut
    creditAccountDefault: string;
    debitAccountDefault: string;
    vatAccountDefault: string;

    reasonDefault = 'Thu tiền mặt';
    accountingObjectNameOld: string;
    accountingObjectIDOld: string;
    nameCategory: any;
    Report = REPORT;
    CategoryName = CategoryName;
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

    constructor(
        private jhiAlertService: JhiAlertService,
        private mCReceiptService: MCReceiptService,
        private accountingObjectService: AccountingObjectService,
        private activatedRoute: ActivatedRoute,
        private autoPrinciplellService: AutoPrincipleService,
        private currencyService: CurrencyService,
        private organizationUnitService: OrganizationUnitService,
        private costsetService: CostSetService,
        private expenseItemService: ExpenseItemService,
        private gLService: GeneralLedgerService,
        public utilsService: UtilsService,
        private statisticsCodeService: StatisticsCodeService,
        private eMContractService: EMContractService,
        private router: Router,
        private toastr: ToastrService,
        public translate: TranslateService,
        private bankAccountDetailsService: BankAccountDetailsService,
        private budgetItemService: BudgetItemService,
        private principal: Principal,
        private refModalService: RefModalService,
        private eventManager: JhiEventManager,
        private accountListService: AccountListService,
        private modalService: NgbModal
    ) {
        super();
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.searchVoucher = JSON.parse(sessionStorage.getItem('searchVoucherMCReceipt')); // Dùng cho tiến lùi chứng từ với danh sách tìm kiếm
        this.autoPrinciplellService
            .getByTypeAndCompanyID({ type: this.TYPE_MC_RECEIPT })
            .subscribe((res: HttpResponse<IAutoPrinciple[]>) => {
                this.autoPrinciples = res.body;
            });
        this.organizationUnitService.getOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body.sort((a, b) => a.organizationUnitCode.localeCompare(b.organizationUnitCode));
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
        this.getDataAccount();
        this.contextMenu = new ContextMenu();
        this.isViewFromRef = window.location.href.includes('/from-ref');
    }

    getDataAccount() {
        /*// tk có
        this.accountListService
            .getAccountType({
                typeID: 100,
                authoritiesCode: 'TCKHAC_HanCheTK',
                columnName: 'CreditAccount',
                valueCheck: '1',
                delimiter: ';',
                ppType: '0'
            })
            .subscribe(res => {
                this.creditAccountList = res.body;
            });
        // tk nợ
        this.accountListService
            .getAccountType({
                typeID: 100,
                authoritiesCode: 'TCKHAC_HanCheTK',
                columnName: 'DebitAccount',
                valueCheck: '1',
                delimiter: ';',
                ppType: '0'
            })
            .subscribe(res => {
                this.debitAccountList = res.body;
            });
        this.accountListService
            .getAccountType({
                typeID: 100,
                authoritiesCode: 'TCKHAC_HanCheTK',
                columnName: 'VATAccount',
                valueCheck: '1',
                delimiter: ';',
                ppType: '0'
            })
            .subscribe(res => {
                this.vatAccountList = res.body;
            });m
        // TK thuế GTGT*/

        const columnList = [
            { column: AccountType.TK_CO, ppType: false },
            { column: AccountType.TK_NO, ppType: false },
            { column: AccountType.TK_THUE_GTGT, ppType: false }
        ];
        const param = {
            typeID: this.TYPE_MC_RECEIPT,
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
        this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
            this.eMContracts = res.body.sort((a, b) =>
                (this.isSoTaiChinh ? a.noFBook : a.noMBook).localeCompare(this.isSoTaiChinh ? b.noFBook : b.noMBook)
            );
        });
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ mCReceipt }) => {
            this.mCReceipt = mCReceipt;
            this.viewVouchersSelected = mCReceipt.viewVouchers ? mCReceipt.viewVouchers : [];
            if (this.mCReceipt.id !== undefined) {
                if (this.isViewFromRef) {
                    this.mCReceiptService.find(this.mCReceipt.id).subscribe((res: HttpResponse<IMCReceipt>) => {
                        const sAInvoiceID = res.body.sAInvoiceID;
                        if (sAInvoiceID) {
                            this.router.navigate(['./chung-tu-ban-hang', sAInvoiceID, 'edit', 'from-mc-receipt', this.mCReceipt.id]);
                        }
                    });
                }
                /*this.changePostedDate();
                this.changeDate();*/
                if (this.account) {
                    this.showVaoSo = this.account.systemOption.some(x => x.code === SU_DUNG_SO_QUAN_TRI && x.data === '1');
                    this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                    if (this.isSoTaiChinh !== undefined) {
                        this.getHopDong();
                    }
                    this.no = this.isSoTaiChinh ? this.mCReceipt.noFBook : this.mCReceipt.noMBook;
                    this.soDangLamViec = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                    this.TCKHAC_GhiSo = this.account.systemOption.some(x => x.code === TCKHAC_GhiSo && x.data === '0');
                    this.currencyCode = this.account.organizationUnit.currencyID;
                }
                this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                    this.currencys = res.body.filter(n => n.isActive).sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                    this.currency = this.currencys.find(n => n.currencyCode === this.mCReceipt.currencyID);
                });
                this.statusVoucher = 1;
                this.accountingObjectNameOld = this.mCReceipt.accountingObjectName ? this.mCReceipt.accountingObjectName : '';
                this.accountingObjectIDOld = this.mCReceipt.accountingObject ? this.mCReceipt.accountingObject.id : null;
                this.isEdit = false; // Xem chứng từ
                this.accountingObjectService.getAllAccountingObjectDTO().subscribe(
                    (res: HttpResponse<IAccountingObject[]>) => {
                        this.accountingObjectData = res.body.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                        this.employees = res.body
                            .filter(n => n.isEmployee)
                            .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                        if (!this.mCReceipt.accountingObjectType && this.mCReceipt.accountingObjectType !== 0) {
                            this.accountingObjects = res.body;
                        } else {
                            this.selectAccountingObjectType();
                        }
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
                this.mCReceiptDetails =
                    this.mCReceipt.mcreceiptDetails === undefined
                        ? []
                        : this.mCReceipt.mcreceiptDetails.sort((a, b) => a.orderPriority - b.orderPriority);
                this.mcReceiptDetailCustomers =
                    this.mCReceipt.mcreceiptDetailCustomers === undefined
                        ? []
                        : this.mCReceipt.mcreceiptDetailCustomers.sort((a, b) => a.orderPriority - b.orderPriority);
                this.mCReceiptDetailTaxs =
                    this.mCReceipt.mcreceiptDetailTaxes === undefined
                        ? []
                        : this.mCReceipt.mcreceiptDetailTaxes.sort((a, b) => a.orderPriority - b.orderPriority);
                this.isRecord = this.mCReceipt.recorded === undefined ? false : this.mCReceipt.recorded;
                // region Tiến lùi chứng từ
                this.utilsService
                    .getIndexRow({
                        id: this.mCReceipt.id,
                        isNext: true,
                        typeID: this.TYPE_MC_RECEIPT,
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
                // endregion
            } else {
                this.no = null;
                this.mCReceipt.reason = this.reasonDefault;
                this.accountingObjectNameOld = this.mCReceipt.accountingObjectName ? this.mCReceipt.accountingObjectName : '';
                this.accountingObjectIDOld = this.mCReceipt.accountingObject ? this.mCReceipt.accountingObject.id : null;
                this.statusVoucher = 0;
                this.isEdit = true; // thêm mới chứng từ
                this.mCReceipt.totalAmount = 0;
                this.mCReceipt.totalAmountOriginal = 0;
                // Set default theo systemOption
                if (this.account) {
                    this.showVaoSo = this.account.systemOption.some(x => x.code === SU_DUNG_SO_QUAN_TRI && x.data === '1');
                    this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                    if (this.isSoTaiChinh !== undefined) {
                        this.getHopDong();
                    }
                    this.TCKHAC_GhiSo = this.account.systemOption.some(x => x.code === TCKHAC_GhiSo && x.data === '0');
                    this.currencyCode = this.account.organizationUnit.currencyID;
                    this.mCReceipt.date = this.utilsService.ngayHachToan(this.account);
                    this.mCReceipt.postedDate = this.mCReceipt.date;
                    /*this.changePostedDate();*/
                    this.changeDate();
                    this.soDangLamViec = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                    if (this.isSoTaiChinh) {
                        if (this.showVaoSo) {
                            this.mCReceipt.typeLedger = 2;
                        } else {
                            this.mCReceipt.typeLedger = 0;
                        }
                    } else {
                        this.mCReceipt.typeLedger = 2;
                    }
                    this.mCReceipt.accountingObjectType = 1;
                    if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                        this.mCReceipt.currencyID = this.account.organizationUnit.currencyID;
                        this.mCReceipt.exchangeRate = 1;
                        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                            this.currencys = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                            this.selectCurrency();
                        });
                    }
                    this.utilsService
                        .getGenCodeVoucher({
                            typeGroupID: this.TYPE_GROUP_MC_RECEIPT, // typeGroupID loại chứng từ
                            displayOnBook: this.soDangLamViec // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
                        })
                        .subscribe((res: HttpResponse<string>) => {
                            console.log(res.body);
                            this.no = res.body;
                            this.copy();
                        });
                }
                this.accountingObjectService.getAllDTO().subscribe(
                    (res: HttpResponse<IAccountingObject[]>) => {
                        this.accountingObjectData = res.body.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                        this.employees = res.body
                            .filter(n => n.isEmployee)
                            .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                        this.mCReceipt.accountingObjectType = 1;
                        this.selectAccountingObjectType();
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
                this.mCReceiptDetails = [];
                this.mCReceiptDetailTaxs = [];
                this.mcReceiptDetailCustomers = [];
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
        if (this.mCReceiptCopy && (this.statusVoucher === 0 || this.statusVoucher === 1)) {
            if (
                !this.utilsService.isEquivalent(this.mCReceipt, this.mCReceiptCopy) ||
                !this.utilsService.isEquivalentArray(this.mCReceiptDetails, this.mCReceiptDetailsCopy) ||
                !this.utilsService.isEquivalentArray(this.mCReceiptDetailTaxs, this.mCReceiptDetailTaxsCopy) ||
                !this.utilsService.isEquivalentArray(this.viewVouchersSelected, this.viewVouchersSelectedCopy)
            ) {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(this.modalComponent, { backdrop: 'static' });
            } else {
                this.closeAll();
            }
        } else {
            this.closeAll();
        }
    }

    closeAll() {
        if (this.searchVoucher) {
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

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Sua, ROLE.PhieuThu_Them])
    save() {
        event.preventDefault();
        if (this.statusVoucher === 0 && !this.utilsService.isShowPopup) {
            this.fillToSave();
            if (this.checkError()) {
                if (this.mCReceipt.id !== undefined) {
                    this.subscribeToSaveResponse(this.mCReceiptService.update(this.mCReceipt));
                } else {
                    this.subscribeToSaveResponse(this.mCReceiptService.create(this.mCReceipt));
                }
            }
        }
    }

    fillToSave() {
        this.mCReceipt.viewVouchers = this.viewVouchersSelected;
        if (this.mCReceipt.typeLedger.toString() === '0') {
            this.mCReceipt.noFBook = this.no;
            this.mCReceipt.noMBook = null;
        } else if (this.mCReceipt.typeLedger.toString() === '1') {
            this.mCReceipt.noMBook = this.no;
            this.mCReceipt.noFBook = null;
        } else {
            if (this.isSoTaiChinh) {
                this.mCReceipt.noFBook = this.no;
            } else {
                this.mCReceipt.noMBook = this.no;
            }
        }
        if (!this.mCReceipt.typeID) {
            this.mCReceipt.typeID = this.TYPE_MC_RECEIPT;
        }
        this.isSaving = true;
        this.mCReceipt.totalVATAmount = 0;
        this.mCReceipt.totalVATAmountOriginal = 0;
        this.mCReceipt.mcreceiptDetails = this.mCReceiptDetails;
        this.mCReceipt.mcreceiptDetailTaxes = this.mCReceiptDetailTaxs;
        for (let i = 0; i < this.mCReceipt.mcreceiptDetailTaxes.length; i++) {
            this.mCReceipt.mcreceiptDetailTaxes[i].orderPriority = i + 1;
            this.mCReceipt.totalVATAmount = this.mCReceipt.totalVATAmount + this.mCReceipt.mcreceiptDetailTaxes[i].vATAmount;
            this.mCReceipt.totalVATAmountOriginal =
                this.mCReceipt.totalVATAmountOriginal + this.mCReceipt.mcreceiptDetailTaxes[i].vATAmountOriginal;
        }
        for (let i = 0; i < this.mCReceipt.mcreceiptDetails.length; i++) {
            this.mCReceipt.mcreceiptDetails[i].orderPriority = i + 1;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Them])
    saveAndNew() {
        event.preventDefault();
        // this.save();
        if (this.statusVoucher === 0 && !this.utilsService.isShowPopup) {
            this.fillToSave();
            if (this.checkError()) {
                if (this.mCReceipt.id !== undefined) {
                    this.subscribeToSaveResponseAndContinue(this.mCReceiptService.update(this.mCReceipt));
                } else {
                    this.subscribeToSaveResponseAndContinue(this.mCReceiptService.create(this.mCReceipt));
                }
                this.statusVoucher = 0;
                this.isEdit = true;
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Them])
    copyAndNew() {
        event.preventDefault();
        if (this.statusVoucher === 1 && !this.utilsService.isShowPopup) {
            this.mCReceipt.id = undefined;
            this.mCReceipt.recorded = false;
            this.no = null;
            this.mCReceipt.noFBook = null;
            this.mCReceipt.noMBook = null;
            this.isRecord = false;
            for (let i = 0; i < this.mCReceiptDetails.length; i++) {
                this.mCReceiptDetails[i].id = undefined;
            }
            for (let i = 0; i < this.mCReceiptDetailTaxs.length; i++) {
                this.mCReceiptDetailTaxs[i].id = undefined;
            }
            for (let i = 0; i < this.viewVouchersSelected.length; i++) {
                this.viewVouchersSelected[i].id = undefined;
            }
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: this.TYPE_GROUP_MC_RECEIPT, // typeGroupID loại chứng từ
                    displayOnBook: this.soDangLamViec // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
                })
                .subscribe((res: HttpResponse<string>) => {
                    console.log(res.body);
                    this.no = res.body;
                });
            this.statusVoucher = 0;
            this.isEdit = true;
            this.mCReceiptCopy = {};
            // this.copy();
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Sua])
    edit() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.mCReceipt.postedDate) && !this.utilsService.isShowPopup) {
            if (this.statusVoucher === 1 && !this.mCReceipt.recorded) {
                this.statusVoucher = 0;
                this.isEdit = true;
                this.copy();
            }
        }
    }

    saveContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.fillToSave();
        if (this.checkError()) {
            if (this.mCReceipt.id !== undefined) {
                this.subscribeToSaveResponseWhenClose(this.mCReceiptService.update(this.mCReceipt));
            } else {
                this.subscribeToSaveResponseWhenClose(this.mCReceiptService.create(this.mCReceipt));
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
        this.mCReceiptCopy = Object.assign({}, this.mCReceipt);
        this.mCReceiptDetailsCopy = this.mCReceiptDetails.map(object => ({ ...object }));
        this.mCReceiptDetailTaxsCopy = this.mCReceiptDetailTaxs.map(object => ({ ...object }));
        this.viewVouchersSelectedCopy = this.viewVouchersSelected.map(object => ({ ...object }));
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Them])
    addNew($event = null) {
        event.preventDefault();
        if (!this.utilsService.isShowPopup) {
            this.router.navigate(['/mc-receipt', 'new']);
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.mCReceipt.id = res.body.mCReceipt.id;
                    this.mCReceipt.recorded = res.body.mCReceipt.recorded;
                    this.router.navigate(['/mc-receipt', res.body.mCReceipt.id, 'edit']);
                    this.mCReceiptCopy = null;
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                } else if (res.body.status === 2) {
                    if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.mCReceipt.id = res.body.mCReceipt.id;
                        this.mCReceipt.recorded = res.body.mCReceipt.recorded;
                        this.mCReceiptCopy = null;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyQT'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.mCReceipt.id = res.body.mCReceipt.id;
                        this.mCReceipt.recorded = res.body.mCReceipt.recorded;
                        this.mCReceiptCopy = null;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyTC'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.mCReceipt.id = res.body.mCReceipt.id;
                        this.mCReceipt.recorded = res.body.mCReceipt.recorded;
                        this.mCReceiptCopy = null;
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
                    this.mCReceipt.id = res.body.mCReceipt.id;
                    this.mCReceipt.recorded = res.body.mCReceipt.recorded;
                    this.router.navigate(['/mc-receipt', res.body.mCReceipt.id, 'edit']).then(() => {
                        this.router.navigate(['/mc-receipt', 'new']);
                        this.isSaveAndNew = false;
                    });
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                } else if (res.body.status === 2) {
                    if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.mCReceipt.id = res.body.mCReceipt.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyQT'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.router.navigate(['/mc-payment', res.body.mCReceipt.id, 'edit']).then(() => {
                            this.router.navigate(['/mc-payment', 'new']);
                            this.isSaveAndNew = false;
                        });
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.mCReceipt.id = res.body.mCReceipt.id;
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
                        this.mCReceipt.id = res.body.mCReceipt.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuy'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.router.navigate(['/mc-payment', res.body.mCReceipt.id, 'edit']).then(() => {
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

    private noVoucherExist() {
        this.toastr.error(
            this.translate.instant('global.data.noVocherAlreadyExist'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
    }

    private recordFailed() {
        this.toastr.error(this.translate.instant('global.data.recordFailed'), this.translate.instant('ebwebApp.mCReceipt.home.message'));
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
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

    get mCReceipt() {
        return this._mCReceipt;
    }

    set mCReceipt(mCReceipt: IMCReceipt) {
        this._mCReceipt = mCReceipt;
    }

    selectAccountingObject() {
        if (this.mCReceipt.accountingObject !== null && this.mCReceipt.accountingObject !== undefined) {
            this.mCReceipt.accountingObjectName = this.mCReceipt.accountingObject.accountingObjectName;
            this.mCReceipt.accountingObjectAddress = this.mCReceipt.accountingObject.accountingObjectAddress;
            this.mCReceipt.taxCode = this.mCReceipt.accountingObject.taxCode;
            this.mCReceipt.payers = this.mCReceipt.accountingObject.contactName;
            if (
                this.mCReceipt.reason === this.reasonDefault ||
                this.mCReceipt.reason === this.reasonDefault + ' từ ' + this.accountingObjectNameOld
            ) {
                this.mCReceipt.reason = this.reasonDefault + ' từ ' + this.mCReceipt.accountingObjectName;
                this.mCReceiptDetails.forEach(mc => {
                    if (!mc.accountingObject || this.accountingObjectIDOld === mc.accountingObject.id) {
                        mc.description = this.mCReceipt.reason;
                    }
                });
            }
            this.accountingObjectNameOld = this.mCReceipt.accountingObject.accountingObjectName;
            for (const dt of this.mCReceiptDetails) {
                if (!dt.accountingObject || this.accountingObjectIDOld === dt.accountingObject.id) {
                    dt.accountingObject = this.mCReceipt.accountingObject;
                }
            }
            for (const dtt of this.mCReceiptDetailTaxs) {
                dtt.accountingObject = this.mCReceipt.accountingObject;
            }
            this.accountingObjectIDOld = this.mCReceipt.accountingObject.id;
        } else {
            this.mCReceipt.accountingObjectAddress = null;
            this.mCReceipt.taxCode = null;
            this.mCReceipt.payers = null;
            this.mCReceipt.accountingObjectName = null;
            this.mCReceipt.reason = this.reasonDefault;
            this.mCReceiptDetails.forEach(n => {
                if (this.accountingObjectIDOld === n.accountingObject.id) {
                    n.accountingObject = null;
                }
            });
        }
        if (this.accountingObjects && this.accountingObjects.length > 0) {
            if (this.mCReceipt.accountingObject) {
                if (!this.accountingObjects.map(n => n.id).includes(this.mCReceipt.accountingObject.id)) {
                    this.mCReceipt.accountingObject = null;
                }
            }
        }
    }

    selectAutoPrinciple() {
        if (this.autoPrinciple && this.autoPrinciple.typeId !== 0) {
            this.mCReceipt.reason = this.autoPrinciple.autoPrincipleName;
            for (const dt of this.mCReceiptDetails) {
                dt.debitAccount = this.autoPrinciple.debitAccount;
                dt.creditAccount = this.autoPrinciple.creditAccount;
                dt.description = this.autoPrinciple.autoPrincipleName;
            }
        } else if (this.autoPrinciple && this.autoPrinciple.typeId === 0) {
            this.mCReceipt.reason = this.autoPrinciple.autoPrincipleName;
        }
    }

    selectCurrency() {
        if (this.mCReceipt.currencyID) {
            this.currency = this.currencys.find(n => n.currencyCode === this.mCReceipt.currencyID);
            this.mCReceipt.exchangeRate = this.currencys.find(n => n.currencyCode === this.mCReceipt.currencyID).exchangeRate;

            this.mCReceipt.totalAmount = 0;
            // this.mCReceipt.totalAmountOriginal = 0;
            for (let i = 0; i < this.mCReceiptDetails.length; i++) {
                this.mCReceiptDetails[i].amount = this.round(
                    this.mCReceiptDetails[i].amountOriginal *
                        (this.currency.formula.includes('*') ? this.mCReceipt.exchangeRate : 1 / this.mCReceipt.exchangeRate),
                    7
                );
            }
            this.updateMCReceipt();
        }
    }

    changeExchangeRate() {
        if (!this.mCReceipt.exchangeRate) {
            this.mCReceipt.exchangeRate = 0;
        }
        if (this.mCReceipt.currencyID) {
            this.mCReceipt.totalAmount = 0;
            for (let i = 0; i < this.mCReceiptDetails.length; i++) {
                this.mCReceiptDetails[i].amount = this.round(
                    this.mCReceiptDetails[i].amountOriginal *
                        (this.currency.formula.includes('*') ? this.mCReceipt.exchangeRate : 1 / this.mCReceipt.exchangeRate),
                    7
                );
            }
            this.updateMCReceipt();
        }
    }

    // Loại đối tượng kế toán
    selectAccountingObjectType() {
        if (this.mCReceipt.accountingObjectType || this.mCReceipt.accountingObjectType === 0) {
            if (this.mCReceipt.accountingObjectType === 0) {
                // Nhà cung cấp
                this.nameCategory = this.utilsService.NHA_CUNG_CAP;
                this.accountingObjects = this.accountingObjectData.filter(n => n.objectType === 0 || n.objectType === 2);
            } else if (this.mCReceipt.accountingObjectType === 1) {
                // Khách hàng
                this.nameCategory = this.utilsService.KHACH_HANG;
                this.accountingObjects = this.accountingObjectData.filter(n => n.objectType === 1 || n.objectType === 2);
            } else if (this.mCReceipt.accountingObjectType === 2) {
                // Nhân viên
                this.nameCategory = this.utilsService.NHAN_VIEN;
                this.accountingObjects = this.accountingObjectData.filter(n => n.isEmployee);
            } else if (this.mCReceipt.accountingObjectType === 3) {
                // Khác
                this.nameCategory = this.utilsService.KHACH_HANG;
                this.accountingObjects = this.accountingObjectData.filter(n => n.objectType === 3);
            } else {
                this.nameCategory = this.utilsService.KHACH_HANG;
                this.accountingObjects = this.accountingObjectData;
            }
        }
        if (this.accountingObjects && this.accountingObjects.length > 0) {
            if (this.mCReceipt.accountingObject) {
                if (!this.accountingObjects.map(n => n.id).includes(this.mCReceipt.accountingObject.id)) {
                    this.mCReceipt.accountingObject = null;
                    this.mCReceipt.accountingObjectName = null;
                    this.mCReceipt.accountingObjectAddress = null;
                    this.mCReceipt.taxCode = null;
                    this.mCReceipt.payers = null;
                }
            }
        } else {
            this.mCReceipt.accountingObject = null;
            this.mCReceipt.accountingObjectName = null;
            this.mCReceipt.accountingObjectAddress = null;
            this.mCReceipt.taxCode = null;
            this.mCReceipt.payers = null;
        }
    }

    AddnewRow(isRightClick?) {
        if (this.mCReceipt.totalAmount === undefined) {
            this.mCReceipt.totalAmount = 0;
        }
        if (this.mCReceipt.totalAmountOriginal === undefined) {
            this.mCReceipt.totalAmountOriginal = 0;
        }
        let lenght = 0;
        if (isRightClick) {
            this.mCReceiptDetails.splice(this.indexFocusDetailRow + 1, 0, {});
            lenght = this.indexFocusDetailRow + 2;
        } else {
            this.mCReceiptDetails.push({});
            lenght = this.mCReceiptDetails.length;
        }
        this.mCReceiptDetails[lenght - 1].amount = 0;
        this.mCReceiptDetails[lenght - 1].amountOriginal = 0;
        this.mCReceiptDetails[lenght - 1].cashOutExchangeRateFB = 0;
        this.mCReceiptDetails[lenght - 1].cashOutDifferAmountFB = 0;
        this.mCReceiptDetails[lenght - 1].cashOutAmountFB = 0;
        this.mCReceiptDetails[lenght - 1].cashOutExchangeRateMB = 0;
        this.mCReceiptDetails[lenght - 1].cashOutDifferAmountMB = 0;
        this.mCReceiptDetails[lenght - 1].cashOutAmountMB = 0;
        this.mCReceiptDetails[lenght - 1].description = this.mCReceipt.reason ? this.mCReceipt.reason : '';
        if (lenght > 1) {
            this.mCReceiptDetails[lenght - 1].debitAccount = this.mCReceiptDetails[lenght - 2].debitAccount;
            this.mCReceiptDetails[lenght - 1].creditAccount = this.mCReceiptDetails[lenght - 2].creditAccount;
            this.mCReceiptDetails[lenght - 1].accountingObject = this.mCReceiptDetails[lenght - 2].accountingObject;
            // this.mCReceiptDetails[lenght - 1].description = this.mCReceiptDetails[lenght - 2].description;
            this.mCReceiptDetails[lenght - 1].organizationUnit = this.mCReceiptDetails[lenght - 2].organizationUnit;
        } else {
            if (this.autoPrinciple && this.autoPrinciple.typeId !== 0) {
                this.mCReceiptDetails[lenght - 1].debitAccount = this.autoPrinciple.debitAccount;
                this.mCReceiptDetails[lenght - 1].creditAccount = this.autoPrinciple.creditAccount;
            } else {
                this.mCReceiptDetails[lenght - 1].debitAccount = this.debitAccountDefault ? this.debitAccountDefault : null;
                this.mCReceiptDetails[lenght - 1].creditAccount = this.creditAccountDefault ? this.creditAccountDefault : null;
            }
            if (this.mCReceipt.accountingObject) {
                this.mCReceiptDetails[lenght - 1].accountingObject = this.mCReceipt.accountingObject;
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
            const idx: number = this.mCReceiptDetails.length - 1;
            const nameTagIndex: string = nameTag + String(idx);
            setTimeout(() => {
                const element: HTMLElement = document.getElementById(nameTagIndex);
                if (element) {
                    element.focus();
                }
            }, 0);
        }
    }

    KeyPress(detail: object, key: string) {
        switch (key) {
            case 'ctr + delete':
                this.removeRow(detail);
                break;
            case 'ctr + c':
                this.copyRow(detail, true);
                break;
            case 'ctr + insert':
                this.AddnewRow(true);
                break;
        }
    }

    copyRow(detail, fromKeyDown?) {
        if (!this.getSelectionText() || !fromKeyDown) {
            const detailCopy: any = Object.assign({}, detail);
            detailCopy.id = null;
            this.mCReceiptDetails.push(detailCopy);
            this.updateMCReceipt();
            if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const lst = this.listIDInputDeatil;
                const col = this.indexFocusDetailCol;
                const row = this.mCReceiptDetails.length - 1;
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

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_GhiSo])
    record() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.mCReceipt.postedDate) && !this.utilsService.isShowPopup) {
            if (this.statusVoucher === 1 && !this.mCReceipt.recorded) {
                const record_: Irecord = {};
                record_.id = this.mCReceipt.id;
                record_.typeID = this.mCReceipt.typeID;
                this.gLService.record(record_).subscribe((res: HttpResponse<Irecord>) => {
                    console.log(JSON.stringify(res.body));
                    if (res.body.success) {
                        this.mCReceipt.recorded = true;
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

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_GhiSo])
    unrecord() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.mCReceipt.postedDate) && !this.utilsService.isShowPopup) {
            if (this.statusVoucher === 1 && this.mCReceipt.recorded) {
                const record_: Irecord = {};
                record_.id = this.mCReceipt.id;
                record_.typeID = this.mCReceipt.typeID;
                this.gLService.unrecord(record_).subscribe((res: HttpResponse<Irecord>) => {
                    console.log(JSON.stringify(res.body));
                    if (res.body.success) {
                        this.mCReceipt.recorded = false;
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

    exportPdf(isDownload, typeReports: number) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.mCReceipt.id,
                typeID: this.mCReceipt.typeID,
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
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.mCReceipt.home.title') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        }
    }

    changeAmountOriginal(detail: IMCReceiptDetails) {
        detail.amount = this.round(
            detail.amountOriginal * (this.currency.formula.includes('*') ? this.mCReceipt.exchangeRate : 1 / this.mCReceipt.exchangeRate),
            7
        );
        this.updateMCReceipt();
    }

    changeAmount(detail: IMCReceiptDetails) {
        this.updateMCReceipt();
    }

    updateMCReceipt() {
        this.mCReceipt.totalAmount = this.round(this.sumDetail('amount'), 7);
        this.mCReceipt.totalAmountOriginal = this.round(this.sumDetail('amountOriginal'), 8);
    }

    sumDetail(prop) {
        let total = 0;
        for (let i = 0; i < this.mCReceiptDetails.length; i++) {
            total += this.mCReceiptDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    // region Tiến lùi chứng từ
    // ham lui, tien
    previousEdit() {
        // goi service get by row num
        if (this.rowNum !== this.count) {
            this.utilsService
                .findByRowNum({
                    id: this.mCReceipt.id,
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

    nextEdit() {
        // goi service get by row num
        if (this.rowNum !== 1) {
            this.utilsService
                .findByRowNum({
                    id: this.mCReceipt.id,
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

    // endregion
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
    checkError(): boolean {
        if (this.mCReceipt.taxCode) {
            if (!this.utilsService.checkMST(this.mCReceipt.taxCode)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.taxCodeInvalid'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        if (this.checkCloseBook(this.account, this.mCReceipt.postedDate)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.lockBook.err.checkForSave'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (this.mCReceipt.employeeID) {
            if (!this.employees.find(n => n.id === this.mCReceipt.employeeID)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.employeeIDNotExist'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        if (this.mCReceipt.typeLedger === 0) {
            if (!this.mCReceipt.noFBook) {
                this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
                return false;
            } else {
                if (!this.utilsService.checkNoBook(this.mCReceipt.noFBook, this.account)) {
                    return false;
                }
            }
        } else if (this.mCReceipt.typeLedger === 1) {
            if (!this.mCReceipt.noMBook) {
                this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
                return false;
            } else {
                if (!this.utilsService.checkNoBook(this.mCReceipt.noMBook, this.account)) {
                    return false;
                }
            }
        } else {
            if (this.isSoTaiChinh) {
                if (!this.mCReceipt.noFBook) {
                    this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
                    return false;
                } else {
                    if (!this.utilsService.checkNoBook(this.mCReceipt.noFBook, this.account)) {
                        return false;
                    }
                }
            } else {
                if (!this.mCReceipt.noMBook) {
                    this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
                    return false;
                } else {
                    if (!this.utilsService.checkNoBook(this.mCReceipt.noMBook, this.account)) {
                        return false;
                    }
                }
            }
        }
        if (!this.mCReceipt.postedDate || !this.mCReceipt.date) {
            this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        if (this.mCReceipt.postedDate.format(DATE_FORMAT) < this.mCReceipt.date.format(DATE_FORMAT)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.postedDate'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.mCReceipt.currencyID) {
            this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }

        if (this.mCReceiptDetails.length === 0) {
            // Null phần chi tiết
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullDetail'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        const dt = this.mCReceiptDetails.find(
            n => n.creditAccount === null || n.creditAccount === undefined || n.debitAccount === undefined || n.debitAccount === null
        );
        const dtT = this.mCReceiptDetailTaxs.find(n => n.vATAccount === null || n.vATAccount === undefined);
        if (dt || dtT) {
            this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        const checkAcc = this.utilsService.checkAccoutWithDetailType(
            this.debitAccountList,
            this.creditAccountList,
            this.mCReceiptDetails,
            this.accountingObjectData,
            this.costSets,
            this.eMContracts,
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
        return true;
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
    getTotalVATAmount() {
        if (this.mCReceiptDetailTaxs && this.mCReceiptDetailTaxs.length > 0) {
            return this.mCReceiptDetailTaxs.map(n => n.vATAmount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    changeDate() {
        // this.dateStr = this.mCReceipt.date.format('DD/MM/YYYY');
        this.mCReceipt.postedDate = this.mCReceipt.date;
        // this.changePostedDate();
    }

    changeDateStr() {
        try {
            if (this.dateStr.length === 8) {
                const td = this.dateStr.replace(/^(.{2})/, '$1/').replace(/^(.{5})/, '$1/');
                if (!moment(td, 'DD/MM/YYYY').isValid()) {
                    this.validateDate = true;
                    this.mCReceipt.date = null;
                } else {
                    this.validateDate = false;
                    this.mCReceipt.date = moment(td, 'DD/MM/YYYY');
                    this.mCReceipt.postedDate = this.mCReceipt.date;
                    this.changePostedDate();
                }
            } else {
                this.mCReceipt.date = null;
                this.validateDate = false;
            }
        } catch (e) {
            this.mCReceipt.date = null;
            this.validateDate = false;
        }
    }

    changePostedDate() {
        this.postedDateStr = this.mCReceipt.postedDate.format('DD/MM/YYYY');
    }

    changePostedDateStr() {
        try {
            if (this.postedDateStr.length === 8) {
                const td = this.postedDateStr.replace(/^(.{2})/, '$1/').replace(/^(.{5})/, '$1/');
                if (!moment(td, 'DD/MM/YYYY').isValid()) {
                    this.validatePostedDate = true;
                    this.mCReceipt.postedDate = null;
                } else {
                    this.validatePostedDate = false;
                    this.mCReceipt.postedDate = moment(td, 'DD/MM/YYYY');
                }
            } else {
                this.mCReceipt.postedDate = null;
                this.validatePostedDate = false;
            }
        } catch (e) {
            this.mCReceipt.postedDate = null;
            this.validatePostedDate = false;
        }
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    changeNoVoucher() {
        if (this.mCReceipt.typeLedger === 0) {
            this.mCReceipt.noFBook = this.no;
        } else if (this.mCReceipt.typeLedger === 1) {
            this.mCReceipt.noMBook = this.no;
        } else {
            if (this.isSoTaiChinh) {
                this.mCReceipt.noFBook = this.no;
            } else {
                this.mCReceipt.noMBook = this.no;
            }
        }
    }

    /*ngOnDestroy(): void {
        if (this.eventSubscriber) {
            this.eventManager.destroy(this.eventSubscriber);
        }
    }*/

    /*
   * hàm ss du lieu 2 object và 2 mảng object
   * return true: neu tat ca giong nhau
   * return fale: neu 1 trong cac object ko giong nhau
   * */
    canDeactive(): boolean {
        if (this.statusVoucher === 0 && !this.isCloseAll && !this.isSaveAndNew) {
            return (
                this.utilsService.isEquivalent(this.mCReceipt, this.mCReceiptCopy) &&
                this.utilsService.isEquivalentArray(this.mCReceiptDetails, this.mCReceiptDetailsCopy) &&
                this.utilsService.isEquivalentArray(this.mCReceiptDetailTaxs, this.mCReceiptDetailTaxsCopy) &&
                this.utilsService.isEquivalentArray(this.viewVouchersSelected, this.viewVouchersSelectedCopy)
            );
        } else {
            return true;
        }
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
                this.mCReceiptDetails[this.currentRow].budgetItem = BudgetItem;
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    saveRow(i) {
        this.currentRow = i;
    }

    removeRow(detail: object) {
        if (this.select === 2) {
            this.viewVouchersSelected.splice(this.viewVouchersSelected.indexOf(detail), 1);
        } else {
            this.mCReceiptDetails.splice(this.mCReceiptDetails.indexOf(detail), 1);
            if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                // vì còn trường hợp = 0
                if (this.mCReceiptDetails.length > 0) {
                    let row = 0;
                    if (this.indexFocusDetailRow > this.mCReceiptDetails.length - 1) {
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
            this.updateMCReceipt();
        }
    }

    registerAddNewRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.AddnewRow(true);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            this.copyRow(this.contextMenu.selectedData);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            this.removeRow(this.contextMenu.selectedData);
        });
        this.eventSubscribers.push(this.eventSubscriber);
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

    @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Xoa])
    delete() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.mCReceipt.postedDate) && !this.utilsService.isShowPopup) {
            if (this.statusVoucher === 1 && !this.mCReceipt.recorded) {
                this.router.navigate(['/mc-receipt', { outlets: { popup: this.mCReceipt.id + '/delete' } }]);
            }
        }
    }

    navigate(imcReceipt: IMCReceipt) {
        switch (imcReceipt.typeID) {
            case this.TYPE_MC_RECEIPT:
            case this.TYPE_MC_RECEIPT_CUSTOM:
                this.router.navigate(['/mc-receipt', imcReceipt.id, 'edit']);
                break;
            case this.TYPE_MC_RECEIPT_FROM_SAINVOICE:
                this.mCReceiptService.find(imcReceipt.id).subscribe((res: HttpResponse<IMCReceipt>) => {
                    const sAInvoiceID = res.body.sAInvoiceID;
                    if (sAInvoiceID) {
                        this.router.navigate(['./ban-hang', 'thu-tien-ngay', sAInvoiceID, 'edit', 'from-mc-receipt', imcReceipt.id]);
                    }
                });
                break;
        }
    }

    isForeignCurrency() {
        return this.account && this.mCReceipt.currencyID !== this.account.organizationUnit.currencyID;
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

    /*Phiếu thu khách hàng*/
    sumMCReceiptDetailCustomers(prop) {
        let total = 0;
        for (let i = 0; i < this.mcReceiptDetailCustomers.length; i++) {
            total += this.mcReceiptDetailCustomers[i][prop];
        }
        return isNaN(total) ? 0 : total;
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

    viewVoucher(imcReceiptDetailCustomer: IMCReceiptDetailCustomer) {
        if ((this.isSoTaiChinh ? imcReceiptDetailCustomer.noFBook : imcReceiptDetailCustomer.noMBook) === 'OPN') {
            return;
        }
        let url = '';
        if (imcReceiptDetailCustomer.voucherTypeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
            url = `/#/ban-hang/chua-thu-tien/${imcReceiptDetailCustomer.saleInvoiceID}/edit/from-ref`;
        } else if (imcReceiptDetailCustomer.voucherTypeID === TypeID.CHUNG_TU_NGHIEP_VU_KHAC) {
            url = `/#/g-other-voucher/${imcReceiptDetailCustomer.saleInvoiceID}/edit/from-ref`;
        }
        window.open(url, '_blank');
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

    /*Phiếu thu khách hàng*/

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    sum(prop) {
        let total = 0;
        if (this.mCReceiptDetails) {
            for (let i = 0; i < this.mCReceiptDetails.length; i++) {
                total += this.mCReceiptDetails[i][prop];
            }
        }
        return isNaN(total) ? 0 : total;
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.mCReceiptDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.mCReceipt;
    }

    addDataToDetail() {
        this.mCReceiptDetails = this.details ? this.details : this.mCReceiptDetails;
        this.mCReceipt = this.parent ? this.parent : this.mCReceipt;
    }

    ngAfterViewInit(): void {
        if (window.location.href.includes('/mc-receipt/new')) {
            this.focusFirstInput();
        }
    }

    actionFocus(indexCol, indexRow) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
    }
}
