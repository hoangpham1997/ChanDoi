import { AfterViewChecked, AfterViewInit, Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { MBDepositService } from './mb-deposit.service';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { BankService } from 'app/danhmuc/bank';
import { IBank } from 'app/shared/model/bank.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';

import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IMBDepositDetails } from 'app/shared/model/mb-deposit-details.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { CostSetService } from 'app/entities/cost-set';
import { ExpenseItemService } from 'app/entities/expense-item';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { AccountDefaultService } from 'app/danhmuc/account-default';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { EMContractService } from 'app/entities/em-contract';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { DatePipe } from '@angular/common';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { BudgetItemService } from 'app/entities/budget-item';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { Principal } from 'app/core';
import {
    AccountType,
    DDSo_NCachHangDVi,
    DDSo_NCachHangNghin,
    DDSo_NgoaiTe,
    DDSo_TienVND,
    SO_LAM_VIEC,
    TCKHAC_GhiSo,
    TCKHAC_SDSoQuanTri,
    TypeID
} from 'app/app.constants';
import * as moment from 'moment';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { IAccountAllList } from 'app/shared/model/account-all-list.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { IMBDepositDetailCustomer } from 'app/shared/model/mb-deposit-detail-customer.model';
import { ROLE } from 'app/role.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { DATE_FORMAT } from 'app/shared';

@Component({
    selector: 'eb-mb-deposit-update',
    templateUrl: './mb-deposit-update.component.html',
    styleUrls: ['./mb-deposit-update.component.css']
})
export class MBDepositUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewInit, AfterViewChecked {
    @ViewChild('content') content: TemplateRef<any>;
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    TYPE_MBDEPOSIT = 160;
    TYPE_MCDEPOSIT = 161;
    TYPE_MSDEPOSIT = 162;
    private _mBDeposit: IMBDeposit;
    private _accountingObject: IAccountingObject;
    isSaving: boolean;
    count: number;
    dateDp: any;
    postedDateDp: any;
    accountDefaults: any[];
    accountDefault: { value?: string };
    employees: IAccountingObject[];
    accountingObjects: IAccountingObject[];
    currencies: ICurrency[];
    banks: IBank[];
    // bankAccountDetails: IBankAccountDetails[];
    mBDepositDetails: IMBDepositDetails[];
    isRecord: Boolean;
    totalAmount: number;
    totalAmountOriginal: number;
    isThem: Boolean;
    temp: Number;
    totalVatAmount: any;
    totalVatAmountOriginal: any;
    accountList: IAccountList[];
    autoPrinciple: IAutoPrinciple[];
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
    dataSession: IDataSessionStorage;
    warningMessage: any;
    searchVoucher: string;
    accounting: IAccountingObject[];
    viewVouchersSelected: any;
    contextMenu: ContextMenu;
    eventSubscriber: Subscription;
    modalRef: NgbModalRef;
    currentAccount: any;
    isSoTaiChinh: boolean;
    currency: ICurrency;
    ngayHachToan: any;
    sysRecord: any;
    sysTypeLedger: any;
    options: any;
    DDSo_NCachHangNghin: any;
    DDSo_NCachHangDVi: any;
    DDSo_TienVND: any;
    DDSo_NgoaiTe: any;
    TCKHAC_SDSoQuanTri: any;
    isHideTypeLedger: Boolean;
    creditAccountList: IAccountList[];
    debitAccountList: IAccountList[];
    vatAccountList: IAccountList[];
    iAutoPrinciple: IAutoPrinciple;
    mBDepositCopy: IMBDeposit;
    mBDepositDetailsCopy: IMBDepositDetails[];
    viewVouchersSelectedCopy: any;
    statusVoucher: number;
    no: any;
    listTempEMContract: string[];
    listTempHeaderEMContract: string[];
    noEMContract: any;
    test: any;
    isReadOnly: boolean;
    columnList = [
        { column: AccountType.TK_CO, ppType: false },
        { column: AccountType.TK_NO, ppType: false },
        { column: AccountType.TK_THUE_GTGT, ppType: false },
        { column: AccountType.TKDU_THUE_GTGT, ppType: false }
    ];
    lstAccountingObjectTypes: any[];
    select: number;
    isRefVoucher: boolean;
    mBDepositDetailCustomers: IMBDepositDetailCustomer[];
    currencyID: string;
    isHide: boolean;
    currentCurrency?: ICurrency;
    nameCategory: any;
    backUpAccountingObjectID: string;

    ROLE_XEM = ROLE.BaoCo_Xem;
    ROLE_THEM = ROLE.BaoCo_Them;
    ROLE_SUA = ROLE.BaoCo_Sua;
    ROLE_XOA = ROLE.BaoCo_Xoa;
    ROLE_GHISO = ROLE.BaoCo_GhiSo;
    ROLE_IN = ROLE.BaoCo_In;
    ROLE_KETXUAT = ROLE.BaoCo_KetXuat;

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
        private mBDepositService: MBDepositService,
        private activatedRoute: ActivatedRoute,
        private accService: AccountingObjectService,
        public utilsService: UtilsService,
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
        private toastr: ToastrService,
        private translate: TranslateService,
        private datepipe: DatePipe,
        private budgetItemService: BudgetItemService,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        private refModalService: RefModalService,
        private principal: Principal
    ) {
        super();
        this.contextMenu = new ContextMenu();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
            this.sysRecord = this.currentAccount.systemOption.find(x => x.code === TCKHAC_GhiSo && x.data);
            this.sysTypeLedger = this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data;
            this.TCKHAC_SDSoQuanTri = this.currentAccount.systemOption.find(x => x.code === TCKHAC_SDSoQuanTri).data;
            // this.mBDeposit.date = this.utilsService.ngayHachToan(account);
            // this.mBDeposit.postedDate = this.mBDeposit.date;

            this.currencyService.findAllActive().subscribe(res => {
                this.currencies = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                this.currentCurrency = this.currencies.find(n => n.currencyCode === this.mBDeposit.currencyID);
                this.currency = this.currencies.find(cur => cur.currencyCode === this.currentAccount.organizationUnit.currencyID);
                if (!this.mBDeposit || !this.mBDeposit.currencyID) {
                    this.mBDeposit.currencyID = this.currency.currencyCode;
                    this.selectChangeCurrency();
                }
                this.currencyID = this.currency.currencyCode;
                this.DDSo_NCachHangNghin = this.currentAccount.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
                this.DDSo_NCachHangDVi = this.currentAccount.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;
                this.DDSo_NgoaiTe = this.currentAccount.systemOption.find(x => x.code === DDSo_NgoaiTe && x.data).data;
                this.DDSo_TienVND = this.currentAccount.systemOption.find(x => x.code === DDSo_TienVND && x.data).data;
                if (
                    (this.mBDeposit && (this.mBDeposit.typeID && this.mBDeposit.typeID === this.TYPE_MBDEPOSIT)) ||
                    !this.mBDeposit.typeID
                ) {
                    const param = {
                        typeID: this.TYPE_MBDEPOSIT,
                        columnName: this.columnList
                    };
                    this.accountListService.getAccountTypeFour(param).subscribe(res2 => {
                        const dataAccount: IAccountAllList = res2.body;
                        this.creditAccountList = dataAccount.creditAccount;
                        this.debitAccountList = dataAccount.debitAccount;
                        this.vatAccountList = dataAccount.vatAccount;
                        this.mBDepositDetails.forEach(item => {
                            item.creditAccountItem = this.creditAccountList.find(n => n.accountNumber === item.creditAccount);
                            item.debitAccountItem = this.debitAccountList.find(n => n.accountNumber === item.debitAccount);
                        });
                    });
                } else {
                    const param = {
                        typeID: this.TYPE_MCDEPOSIT,
                        columnName: this.columnList
                    };
                    this.accountListService.getAccountTypeFour(param).subscribe(res2 => {
                        const dataAccount: IAccountAllList = res2.body;
                        this.creditAccountList = dataAccount.creditAccount;
                        this.debitAccountList = dataAccount.debitAccount;
                        this.vatAccountList = dataAccount.vatAccount;
                        this.mBDepositDetails.forEach(item => {
                            item.creditAccountItem = this.creditAccountList.find(n => n.accountNumber === item.creditAccount);
                            item.debitAccountItem = this.debitAccountList.find(n => n.accountNumber === item.debitAccount);
                        });
                    });
                }
            });
            // lấy dữ liệu cho combobox
            this.accService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
                this.accounting = res.body;
                if (this.mBDeposit.accountingObjectType !== null || this.mBDeposit.accountingObjectType !== undefined) {
                    this.accountingObjects = this.getObjectType(this.mBDeposit.accountingObjectType);
                } else {
                    this.mBDeposit.accountingObjectType = 1;
                    this.accountingObjects = this.getObjectType(1);
                }
            });
        });
    }

    ngOnInit() {
        this.mBDepositDetails = [];
        this.mBDepositDetailCustomers = [];
        this.searchVoucher = sessionStorage.getItem('dataSearchMBDeposit') ? sessionStorage.getItem('dataSearchMBDeposit') : null;
        this.isSaving = false;
        this.isMainCurrency = false;
        this.isHideTypeLedger = false;
        this.isEditUrl = window.location.href.includes('/edit') || window.location.href.includes('/view');
        this.activatedRoute.data.subscribe(({ mBDeposit }) => {
            this.mBDeposit = mBDeposit;
            this.backUpAccountingObjectID = this.mBDeposit.accountingObjectID;
            this.viewVouchersSelected = mBDeposit.viewVouchers ? mBDeposit.viewVouchers : [];
            this.mBDepositDetailCustomers = mBDeposit.mBDepositDetailCustomers ? mBDeposit.mBDepositDetailCustomers : [];
            this.translate
                .get([
                    'ebwebApp.mBDeposit.supplier',
                    'ebwebApp.mBDeposit.customer',
                    'ebwebApp.mBDeposit.employee',
                    'ebwebApp.mBDeposit.other'
                ])
                .subscribe(res => {
                    this.lstAccountingObjectTypes = [
                        { value: 0, name: res['ebwebApp.mBDeposit.supplier'] },
                        { value: 1, name: res['ebwebApp.mBDeposit.customer'] },
                        { value: 2, name: res['ebwebApp.mBDeposit.employee'] },
                        { value: 3, name: res['ebwebApp.mBDeposit.other'] }
                    ];
                });
            if (!this.isEditUrl) {
                this.statusVoucher = 0;
                this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                this.TCKHAC_SDSoQuanTri = this.currentAccount.systemOption.find(x => x.code === TCKHAC_SDSoQuanTri).data;
                this.mBDeposit.typeID = this.TYPE_MBDEPOSIT;
                this.mBDeposit.accountingObjectID = null;
                this.mBDeposit.date = this.utilsService.ngayHachToan(this.currentAccount);
                this.mBDeposit.postedDate = this.mBDeposit.date;
                if (this.TCKHAC_SDSoQuanTri === '0') {
                    this.mBDeposit.typeLedger = 0;
                    this.sysTypeLedger = 0;
                    this.isHideTypeLedger = true;
                } else {
                    this.mBDeposit.typeLedger = 2;
                    this.sysTypeLedger = this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                }
                this.utilsService
                    .getGenCodeVoucher({
                        typeGroupID: 16, // typeGroupID loại chứng từ
                        companyID: '', // ID công ty
                        branchID: '', // ID chi nhánh
                        displayOnBook: this.sysTypeLedger // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
                    })
                    .subscribe((res: HttpResponse<string>) => {
                        // this.mCReceipt.noFBook = (res.body.toString());
                        this.no = res.body;
                        this.translate.get(['ebwebApp.mBDeposit.defaultReason']).subscribe(res2 => {
                            this.mBDeposit.reason = res2['ebwebApp.mBDeposit.defaultReason'];
                        });
                        this.copy();
                    });
                if (sessionStorage.getItem('saveAndLoad')) {
                } else {
                    this.mBDeposit.accountingObjectType = 1;
                }
                this.isReadOnly = false;
            } else {
                // region Tiến lùi chứng từ
                this.utilsService
                    .getIndexRow({
                        id: this.mBDeposit.id,
                        isNext: true,
                        typeID: this.TYPE_MBDEPOSIT,
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
                this.isReadOnly = true;
                if (this.TCKHAC_SDSoQuanTri === '0') {
                    this.no = this.mBDeposit.noFBook;
                    this.isHideTypeLedger = true;
                    this.mBDeposit.typeLedger = 0;
                } else {
                    this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                    this.no = this.isSoTaiChinh ? this.mBDeposit.noFBook : this.mBDeposit.noMBook;
                    this.statusVoucher = 1; // Xem chứng từ
                }
            }
        });
        this.id = this.mBDeposit.id;
        this.accService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.employees = res.body
                .filter(employees => employees.isEmployee && employees.isActive)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
        });
        this.bankService.getBanks().subscribe((res: HttpResponse<IBank[]>) => {
            this.banks = res.body;
        });
        this.bankAccountDetailsService.getBankAccountDetails().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
            this.bankAccountDetails = res.body;
        });
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticCodes = res.body;
        });
        this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body;
        });
        this.expenseItemsService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body;
        });
        // this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
        //     this.eMContracts = res.body;
        // });
        this.organizationUnitService.getOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body.sort((a, b) => a.organizationUnitCode.localeCompare(b.organizationUnitCode));
        });
        this.organizationUnitService.getOrganizationUnitsByCompanyID().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.allOrganizationUnits = res.body.sort((a, b) => a.organizationUnitCode.localeCompare(b.organizationUnitCode));
        });
        this.autoPrincipleService.getAutoPrinciples().subscribe((res: HttpResponse<IAutoPrinciple[]>) => {
            this.autoPrinciple = res.body
                .filter(aPrinciple => aPrinciple.typeId === this.TYPE_MBDEPOSIT || aPrinciple.typeId === 0)
                .sort((n1, n2) => {
                    if (n1.typeId > n2.typeId) {
                        return 1;
                    }
                    if (n1.typeId < n2.typeId) {
                        return -1;
                    }
                    return 0;
                });
        });
        this.budgetItemService.getBudgetItems().subscribe((res: HttpResponse<IBudgetItem[]>) => {
            this.budgetItems = res.body;
        });
        // Check ghi sổ
        if (this.mBDeposit.id) {
            this.mBDepositDetails = this.mBDeposit.mBDepositDetails === undefined ? [] : this.mBDeposit.mBDepositDetails;
            this.isRecord = this.mBDeposit.recorded === undefined ? false : this.mBDeposit.recorded;
        } else {
            this.mBDepositDetails = [];
            this.isRecord = false;
        }

        if (this.mBDeposit.id !== undefined) {
            this.id = this.mBDeposit.id;
        } else {
        }
        if (this.mBDeposit.recorded) {
            this.isRecord = false;
        } else {
            this.isRecord = true;
        }
        this.isCreateUrl = window.location.href.includes('/mb-deposit/new');
        this.isEdit = this.isCreateUrl;

        if (this.mBDeposit.id !== undefined && !this.isCreateUrl) {
            this.isCreateUrl = false;
        } else {
            this.isCreateUrl = true;
        }
        this.isEdit = this.isCreateUrl;
        this.disableAddButton = true;
        if (this.mBDeposit.id) {
            if (!this.mBDeposit.recorded && this.isEditUrl) {
                this.disableEditButton = false;
            } else {
                this.disableEditButton = true;
            }
        } else {
            this.disableEditButton = true;
        }
        if (sessionStorage.getItem('saveAndLoad')) {
            this.mBDeposit = JSON.parse(sessionStorage.getItem('saveAndLoad'));
            this.mBDepositDetails = this.mBDeposit.mBDepositDetails;
            this.mBDepositDetailCustomers = this.mBDeposit.mBDepositDetailCustomers;
            this.viewVouchersSelected = this.mBDeposit.viewVouchers ? this.viewVouchersSelected : [];
            this.TCKHAC_SDSoQuanTri = this.currentAccount.systemOption.find(x => x.code === TCKHAC_SDSoQuanTri).data;
            this.mBDeposit.date = moment(this.mBDeposit.date);
            this.mBDeposit.postedDate = moment(this.mBDeposit.postedDate);
            this.copy();
        }
        sessionStorage.removeItem('saveAndLoad');
        if (this.mBDeposit.typeID === this.TYPE_MBDEPOSIT) {
            this.isHide = false;
        } else if (this.mBDeposit.typeID === this.TYPE_MCDEPOSIT) {
            this.isHide = true;
        }
        this.sumAfterDeleteByContextMenu();
        this.registerRef();
        this.afterAddRow();
        this.registerCopyRow();
        this.isRefVoucher = window.location.href.includes('/edit/from-ref');
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.selectChangeAccountingObject();
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.selectChangeAccountingObject();
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilsService.setShowPopup(response.content);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        // this.registerIsShowPopup();
    }

    //
    // registerIsShowPopup() {
    //     this.utilsService.checkEvent.subscribe(res => {
    //         this.isShowPopup = res;
    //     });
    // }

    closeForm() {
        event.preventDefault();
        if (this.mBDepositCopy && (this.statusVoucher === 0 || this.statusVoucher === 1) && !this.utilsService.isShowPopup) {
            if (
                !this.utilsService.isEquivalent(this.mBDeposit, this.mBDepositCopy) ||
                !this.utilsService.isEquivalentArray(this.mBDepositDetails, this.mBDepositDetailsCopy) ||
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

    closeAll() {
        if (sessionStorage.getItem('dataSourceMBDeposit')) {
            this.router.navigate(['/mb-deposit', 'hasSearch', '1']);
        } else {
            this.router.navigate(['/mb-deposit']);
        }
    }

    copy() {
        if (this.mBDeposit.id) {
            this.mBDepositDetails = this.mBDeposit.mBDepositDetails ? this.mBDeposit.mBDepositDetails : [];
            this.viewVouchersSelected = this.mBDeposit.viewVouchers ? this.mBDeposit.viewVouchers : [];
        }
        this.mBDepositCopy = Object.assign({}, this.mBDeposit);
        if (this.mBDepositDetails) {
            this.mBDepositDetailsCopy = this.mBDepositDetails.map(object => ({ ...object }));
        }
        if (this.viewVouchersSelected) {
            this.viewVouchersSelectedCopy = this.viewVouchersSelected.map(object => ({ ...object }));
        }
    }

    copyAndNew() {
        event.preventDefault();
        if (!this.isCreateUrl && !this.utilsService.isShowPopup) {
            this.mBDeposit.id = undefined;
            this.mBDeposit.noMBook = undefined;
            this.mBDeposit.noFBook = undefined;
            for (let i = 0; i < this.mBDeposit.mBDepositDetails.length; i++) {
                this.mBDeposit.mBDepositDetails[i].id = undefined;
            }
            if (this.mBDeposit.viewVouchers) {
                for (let i = 0; i < this.mBDeposit.viewVouchers.length; i++) {
                    this.mBDeposit.viewVouchers[i].id = undefined;
                }
            }
            sessionStorage.setItem('saveAndLoad', JSON.stringify(this.mBDeposit));
            this.edit();
            this.isSaving = false;
            this.copy();
            this.router.navigate(['mb-deposit/new']);
            this.statusVoucher = 0;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoCo_Sua])
    edit() {
        event.preventDefault();
        if (
            !this.isCreateUrl &&
            this.isRecord &&
            !this.checkCloseBook(this.currentAccount, this.mBDeposit.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            this.isCreateUrl = !this.isCreateUrl;
            this.isEdit = this.isCreateUrl;
            this.disableAddButton = false;
            this.disableEditButton = true;
            this.statusVoucher = 0;
            this.isReadOnly = false;
            this.copy();
            this.focusFirstInput();
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoCo_Them])
    addNew($event) {
        event.preventDefault();
        if (!this.isCreateUrl && !this.utilsService.isShowPopup) {
            this.router.navigate(['mb-deposit/new']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoCo_Them, ROLE.BaoCo_Sua])
    save(isNew = false) {
        event.preventDefault();
        if (this.isCreateUrl && !this.utilsService.isShowPopup) {
            this.warningMessage = '';
            this.mBDeposit.viewVouchers = this.viewVouchersSelected;
            this.mBDeposit.mBDepositDetails = this.mBDepositDetails;
            if (this.checkError()) {
                for (let i = 0; i < this.mBDeposit.mBDepositDetails.length; i++) {
                    this.mBDeposit.mBDepositDetails[i].orderPriority = i + 1;
                }
                if (this.mBDeposit.typeLedger === 0) {
                    this.mBDeposit.noFBook = this.no;
                    this.mBDeposit.noMBook = null;
                } else if (this.mBDeposit.typeLedger === 1) {
                    this.mBDeposit.noFBook = null;
                    this.mBDeposit.noMBook = this.no;
                } else {
                    if (this.isSoTaiChinh) {
                        this.mBDeposit.noFBook = this.no;
                    } else {
                        this.mBDeposit.noMBook = this.no;
                    }
                }
                if (!this.mBDeposit.typeID) {
                    this.mBDeposit.typeID = this.TYPE_MBDEPOSIT;
                }
                this.mBDeposit.totalVATAmount = 0;
                this.mBDeposit.totalVATAmountOriginal = 0;
                this.mBDeposit.postedDate = moment(this.mBDeposit.postedDate);
                this.mBDeposit.date = moment(this.mBDeposit.date);
                if (this.sysRecord.data === '0') {
                    this.mBDeposit.recorded = true;
                } else {
                    this.mBDeposit.recorded = false;
                }
                this.isSaving = true;
                const data = { mBDeposit: this.mBDeposit, viewVouchers: this.viewVouchersSelected };
                //
                // for (this.i = 0; this.i < this.mBDeposit.mBDepositDetails.length; this.i++) {
                //     this.mBDeposit.mBDepositDetails[this.i].orderPriority = this.i + 1;
                // }
                // check is url new
                if (this.mBDeposit.id && this.isEditUrl) {
                    this.isCreateUrl = this.isEdit = false;
                }
                if (this.isCreateUrl && this.mBDeposit.id !== undefined) {
                    this.mBDeposit.id = undefined;
                    for (let i = 0; i < this.mBDeposit.mBDepositDetails.length; i++) {
                        this.mBDeposit.mBDepositDetails[i].id = undefined;
                    }
                }
                this.mBDeposit.exported = false;
                if (!this.isCreateUrl && !this.isEditUrl) {
                    this.mBDeposit.id = undefined;
                }
                if (this.mBDeposit.id !== undefined) {
                    this.subscribeToSaveResponse(this.mBDepositService.update(this.mBDeposit));
                } else {
                    this.subscribeToSaveResponse(this.mBDepositService.create(this.mBDeposit));
                }
            } else {
            }
        }
    }

    checkError() {
        if (!this.no) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBDeposit.voucherNumber') + ' ' + this.translate.instant('ebwebApp.mBDeposit.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBDeposit.message'));
            return false;
        }
        if (!this.mBDeposit.date) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBDeposit.date') + ' ' + this.translate.instant('ebwebApp.mBDeposit.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBDeposit.message'));
            return false;
        }
        if (!this.mBDeposit.postedDate) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBDeposit.postedDate') + ' ' + this.translate.instant('ebwebApp.mBDeposit.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBDeposit.message'));
            return false;
        }
        if (this.mBDeposit.postedDate.format(DATE_FORMAT) < this.mBDeposit.date.format(DATE_FORMAT)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBDeposit.errorPostedDateAndDate'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            return false;
        }
        if (this.checkCloseBook(this.currentAccount, this.mBDeposit.postedDate)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.lockBook.err.checkForSave'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        if (!this.mBDeposit.currencyID || this.mBDeposit.currencyID === '') {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBDeposit.detail.currencyType') +
                ' ' +
                this.translate.instant('ebwebApp.mBDeposit.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBDeposit.message'));
            return false;
        }
        if (!this.mBDeposit.exchangeRate) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBDeposit.exchangeRate') + ' ' + this.translate.instant('ebwebApp.mBDeposit.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBDeposit.message'));
            return false;
        }
        if (this.mBDeposit.typeLedger === undefined || this.mBDeposit === null) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBDeposit.typeLedger') + ' ' + this.translate.instant('ebwebApp.mBDeposit.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBDeposit.message'));
            return false;
        }
        if (this.mBDepositDetails.length === 0) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBDeposit.home.details') + ' ' + this.translate.instant('ebwebApp.mBDeposit.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBDeposit.message'));
            return false;
        }
        if (!this.utilsService.checkNoBook(this.no ? this.no : null, this.currentAccount)) {
            return false;
        }
        if (this.mBDepositDetails) {
            for (this.i = 0; this.i < this.mBDepositDetails.length; this.i++) {
                if (this.mBDepositDetails[this.i].debitAccount === undefined || this.mBDepositDetails[this.i].debitAccount === null) {
                    this.warningMessage =
                        this.translate.instant('ebwebApp.mBDeposit.debitAccount') +
                        ' ' +
                        this.translate.instant('ebwebApp.mBDeposit.isNotNull');
                    this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBDeposit.message'));
                    return false;
                }
                if (this.mBDepositDetails[this.i].creditAccount === undefined || this.mBDepositDetails[this.i].creditAccount === null) {
                    this.warningMessage =
                        this.translate.instant('ebwebApp.mBDeposit.creditAccount') +
                        ' ' +
                        this.translate.instant('ebwebApp.mBDeposit.isNotNull');
                    this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBDeposit.message'));
                    return false;
                }
            }
        }
        const checkAcc = this.utilsService.checkAccoutWithDetailType(
            this.debitAccountList,
            this.creditAccountList,
            this.mBDepositDetails,
            this.accountingObjects,
            this.costSets,
            null,
            [],
            null,
            this.organizationUnits,
            this.expenseItems,
            this.budgetItems,
            this.statisticCodes
        );
        if (checkAcc) {
            this.toastr.error(checkAcc, this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        if (this.mBDepositDetails.length > 0) {
            for (let i = 0; i < this.mBDepositDetails.length; i++) {
                const detailTypeCre = this.creditAccountList.find(a => a.accountNumber === this.mBDepositDetails[i].creditAccount)
                    .detailType;
                const detailTypeDeb = this.debitAccountList.find(a => a.accountNumber === this.mBDepositDetails[i].debitAccount).detailType;
                if (detailTypeCre === '6') {
                    if (!this.mBDeposit.bankAccountDetailID) {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.mBDeposit.account') +
                                this.mBDepositDetails[i].creditAccount +
                                this.translate.instant('ebwebApp.mBDeposit.isAccountDetailFromBankAccountDetail') +
                                this.translate.instant('ebwebApp.mBDeposit.bankAccountDetailID') +
                                ' ' +
                                this.translate.instant('ebwebApp.mBDeposit.isNotNull'),
                            this.translate.instant('ebwebApp.mBDeposit.error')
                        );
                        return false;
                    }
                }
                if (detailTypeDeb === '6') {
                    if (!this.mBDeposit.bankAccountDetailID) {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.mBDeposit.account') +
                                this.mBDepositDetails[i].debitAccount +
                                this.translate.instant('ebwebApp.mBDeposit.isAccountDetailFromBankAccountDetail') +
                                this.translate.instant('ebwebApp.mBDeposit.bankAccountDetailID') +
                                ' ' +
                                this.translate.instant('ebwebApp.mBDeposit.isNotNull'),
                            this.translate.instant('ebwebApp.mBDeposit.error')
                        );
                        return false;
                    }
                }
            }
        }
        return true;
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
                    this.isCreateUrl = this.isEdit = false;
                    if (res.body.mbDeposit.recorded) {
                        this.disableAddButton = false;
                    } else {
                        this.disableAddButton = true;
                    }
                    this.mBDeposit = res.body.mbDeposit;
                    this.mBDeposit.postedDate = moment(this.mBDeposit.postedDate);
                    this.mBDeposit.date = moment(this.mBDeposit.date);
                    this.viewVouchersSelected = this.mBDeposit.viewVouchers ? this.mBDeposit.viewVouchers : [];
                    this.onSaveSuccess();
                    this.isReadOnly = true;
                    this.router.navigate(['./mb-deposit', this.mBDeposit.id, 'edit']);
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                    return;
                }
            },
            (res: HttpErrorResponse) => {
                this.onSaveError();
                this.isCreateUrl = this.isEdit = true;
            }
        );
    }

    noVoucherExist() {
        this.toastr.error(
            this.translate.instant('global.data.noVocherAlreadyExist'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
        this.isCreateUrl = this.isEdit = true;
    }

    private onSaveSuccess() {
        this.copy();
        this.statusVoucher = 1;
        this.isSaving = false;
        // this.router.navigate(['./mb-deposit', this.mBDeposit.id, 'edit']);
        this.disableAddButton = true;
        this.disableEditButton = true;
        this.isRecord = false;
    }

    private onSaveError() {
        this.isSaving = false;
        this.isCreateUrl = !this.isCreateUrl;
        this.isCreateUrl = this.isEdit;
    }

    get mBDeposit() {
        return this._mBDeposit;
    }

    get accountingObject() {
        // @ts-ignore
        return this._accountingObject.isemployee(true);
    }

    set mBDeposit(mBDeposit: IMBDeposit) {
        this._mBDeposit = mBDeposit;
    }

    trackbyAccoutingObjectID(index: number, item: IAccountingObject) {
        return item.id;
    }

    selectChangeAccountingObject() {
        const iAccountingObject = this.accountingObjects.find(
            accountingObject => accountingObject.id === this.mBDeposit.accountingObjectID
        );
        if (iAccountingObject) {
            this.mBDeposit.accountingObjectName = iAccountingObject.accountingObjectName;
            this.mBDeposit.accountingObjectAddress = iAccountingObject.accountingObjectAddress;
        } else {
            this.mBDeposit.accountingObjectName = '';
            this.mBDeposit.accountingObjectAddress = '';
        }
        for (const dt of this.mBDepositDetails) {
            if (dt.accountingObjectID === this.backUpAccountingObjectID) {
                dt.accountingObjectID = this.mBDeposit.accountingObjectID;
                if (!this.autoPrincipleName) {
                    dt.description =
                        'Thu tiền bằng TGNH ' + (this.mBDeposit.accountingObjectName ? 'từ ' + this.mBDeposit.accountingObjectName : '');
                }
            }
        }
        if (this.isCreateUrl && !this.autoPrincipleName) {
            this.mBDeposit.reason =
                'Thu tiền bằng TGNH ' + (this.mBDeposit.accountingObjectName ? 'từ ' + this.mBDeposit.accountingObjectName : '');
        }
        this.backUpAccountingObjectID = this.mBDeposit.accountingObjectID;
        // for (const dtt of this.mBDepositDetailTax) {
        //     dtt.accountingObjectID = this.mBDeposit.accountingObjectID;
        // }
    }

    selectChangeReason() {
        this.mBDeposit.reason = this.autoPrincipleName;
        this.iAutoPrinciple = this.autoPrinciple.find(a => a.autoPrincipleName === this.autoPrincipleName);
        if (this.mBDeposit.mBDepositDetails) {
            if (this.mBDeposit.mBDepositDetails.length > 0) {
                this.mBDepositDetails[0].description = this.autoPrincipleName;
            }
        }
        const checkDebitAccount = this.debitAccountList.find(a => a.accountNumber === this.iAutoPrinciple.debitAccount);
        const checkCreditAccount = this.creditAccountList.find(a => a.accountNumber === this.iAutoPrinciple.creditAccount);
        for (const dt of this.mBDepositDetails) {
            if (checkDebitAccount) {
                dt.debitAccount = this.iAutoPrinciple.debitAccount;
            } else {
                dt.debitAccount = null;
            }
            if (checkCreditAccount) {
                dt.creditAccount = this.iAutoPrinciple.creditAccount;
            } else {
                dt.creditAccount = null;
            }
            dt.description = this.iAutoPrinciple.autoPrincipleName;
        }
    }

    selectChangeCurrency() {
        if (this.isCreateUrl) {
            let sum = 0;
            this.currentCurrency = this.currencies.find(n => n.currencyCode === this.mBDeposit.currencyID);
            if (this.currentCurrency) {
                this.mBDeposit.exchangeRate = this.currentCurrency.exchangeRate;
            } else {
                this.mBDeposit.exchangeRate = 0;
            }
            if (this.mBDepositDetails && this.currentCurrency && this.currentCurrency.formula) {
                for (let i = 0; i < this.mBDepositDetails.length; i++) {
                    // this.mBDepositDetails[i].amount = this.mBDeposit.exchangeRate * this.mBDepositDetails[i].amountOriginal;
                    this.mBDepositDetails[i].amount = this.round(
                        this.mBDepositDetails[i].amountOriginal *
                            (this.currentCurrency.formula.includes('*') ? this.mBDeposit.exchangeRate : 1 / this.mBDeposit.exchangeRate),
                        7
                    );
                    sum = sum + this.mBDepositDetails[i].amount;
                }
            }
            this.mBDeposit.totalAmount = sum;
        }
    }

    selectChangeBank(bankAccountDetailID: string) {
        const lstBankAccountDetail = this.bankAccountDetails.find(
            bankAccountDetail => bankAccountDetail.id === this.mBDeposit.bankAccountDetailID
        );
        if (lstBankAccountDetail) {
            this.mBDeposit.bankName = lstBankAccountDetail.bankName;
        } else {
            this.mBDeposit.bankName = '';
        }
    }

    AddnewRow(eventData: any, select: number) {
        if (this.isCreateUrl) {
            if (select === 0) {
                this.mBDepositDetails.push(Object.assign({}, this.mBDepositDetails[this.mBDepositDetails.length - 1]));
                if (this.mBDepositDetails.length === 1) {
                    this.mBDeposit.totalAmountOriginal = 0;
                    this.mBDeposit.totalAmount = 0;
                    if (this.mBDeposit.accountingObjectID) {
                        this.mBDepositDetails[0].accountingObjectID = this.mBDeposit.accountingObjectID;
                    }
                    if (this.mBDeposit.reason) {
                        this.mBDepositDetails[0].description = this.mBDeposit.reason;
                    }
                }
                if (this.iAutoPrinciple) {
                    if (this.debitAccountList.find(a => a.accountNumber === this.iAutoPrinciple.debitAccount)) {
                        this.mBDepositDetails[this.mBDepositDetails.length - 1].debitAccount = this.iAutoPrinciple.debitAccount;
                    }
                    if (this.creditAccountList.find(a => a.accountNumber === this.iAutoPrinciple.creditAccount)) {
                        this.mBDepositDetails[this.mBDepositDetails.length - 1].creditAccount = this.iAutoPrinciple.creditAccount;
                    }
                }
                this.mBDepositDetails[this.mBDepositDetails.length - 1].id = undefined;
                this.mBDepositDetails[this.mBDepositDetails.length - 1].amount = 0;
                this.mBDepositDetails[this.mBDepositDetails.length - 1].amountOriginal = 0;
                // this.mBDepositDetails[this.mBDepositDetails.length - 1].orderPriority = this.mBDepositDetails.length - 1;
                const nameTag: string = event.srcElement.id;
                const index: number = this.mBDepositDetails.length - 1;
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
            this.mBDepositDetails.splice(value, 1);
            let totalAmount = 0;
            let totalAmountOriginal = 0;

            for (let i = 0; i < this.mBDepositDetails.length; i++) {
                totalAmount += this.mBDepositDetails[i].amount;
                totalAmountOriginal += this.mBDepositDetails[i].amountOriginal;
                this.mBDepositDetails[i].amount = this.mBDepositDetails[i].amountOriginal * this.mBDeposit.exchangeRate;
            }
            this.mBDeposit.totalAmountOriginal = totalAmountOriginal;
            this.mBDeposit.totalAmount = totalAmount;
        }
    }

    changeObjectType() {
        if (!this.isThem) {
            const objectType = this.mBDeposit.accountingObjectType;
            this.accountingObjects = this.getObjectType(objectType);
        } else {
            const objectType = this.mBDeposit.accountingObjectType;
            this.accountingObjects = this.getObjectType(objectType);
        }
        this.mBDeposit.accountingObjectID = null;
        this.mBDeposit.accountingObjectName = null;
        this.mBDeposit.accountingObjectAddress = null;
        this.translate.get(['ebwebApp.mBDeposit.defaultReason']).subscribe(res2 => {
            this.mBDeposit.reason = res2['ebwebApp.mBDeposit.defaultReason'];
        });
        for (let i = 0; i < this.mBDepositDetails.length; i++) {
            this.mBDepositDetails[i].accountingObjectID = null;
            this.translate.get(['ebwebApp.mBDeposit.defaultReason']).subscribe(res2 => {
                this.mBDepositDetails[i].description = res2['ebwebApp.mBDeposit.defaultReason'];
            });
        }
    }

    getObjectType(a: any) {
        if (a === 0) {
            this.accountingObjects = this.accounting.filter(
                listAccountingObject =>
                    (listAccountingObject.objectType === 0 || listAccountingObject.objectType === 2) && listAccountingObject.isActive
            );
        } else if (a === 1) {
            this.accountingObjects = this.accounting.filter(
                listAccountingObject =>
                    (listAccountingObject.objectType === 1 || listAccountingObject.objectType === 2) && listAccountingObject.isActive
            );
        } else if (a === 2) {
            this.accountingObjects = this.accounting.filter(
                listAccountingObject => listAccountingObject.isActive && listAccountingObject.isEmployee
            );
        } else if (a === 3) {
            this.accountingObjects = this.accounting.filter(
                listAccountingObject => listAccountingObject.objectType === 3 && listAccountingObject.isActive
            );
        } else {
            // this.accService.query().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            //     this.accountingObjects = res.body.filter(accountingobject => accountingobject.isemployee === false);
            // });
        }
        this.selectAccountingObjectType();
        return this.accountingObjects;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoCo_GhiSo])
    record() {
        event.preventDefault();
        if (!this.isCreateUrl && !this.checkCloseBook(this.currentAccount, this.mBDeposit.postedDate) && !this.utilsService.isShowPopup) {
            if (this.mBDeposit.id) {
                if (this.mBDeposit.recorded) {
                    this.isRecord = false;
                } else {
                    this.isRecord = true;
                }
                this.record_ = {};
                this.record_.id = this.mBDeposit.id;
                this.record_.typeID = this.mBDeposit.typeID;
                if (!this.mBDeposit.recorded) {
                    this.gLService.record(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                        if (res.body.success) {
                            this.mBDeposit.recorded = true;
                            this.toastr.success(
                                this.translate.instant('ebwebApp.mBDeposit.recordSuccess'),
                                this.translate.instant('ebwebApp.mBDeposit.message')
                            );
                        }
                    });
                    this.disableEditButton = true;
                    this.isRecord = false;
                }
            } else {
                this.toastr.error('ebwebApp.mBDeposit.error', 'ebwebApp.mBDeposit.message');
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoCo_GhiSo])
    unrecord() {
        event.preventDefault();
        if (!this.isCreateUrl && !this.checkCloseBook(this.currentAccount, this.mBDeposit.postedDate) && !this.utilsService.isShowPopup) {
            if (this.mBDeposit.id) {
                if (this.mBDeposit.recorded) {
                    this.isRecord = false;
                } else {
                    this.isRecord = true;
                }
                this.record_ = {};
                this.record_.id = this.mBDeposit.id;
                this.record_.typeID = this.mBDeposit.typeID;
                //
                if (this.mBDeposit.recorded) {
                    this.gLService.unrecord(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                        if (res.body.success) {
                            this.mBDeposit.recorded = false;
                            this.toastr.success(
                                this.translate.instant('ebwebApp.mBDeposit.unrecordSuccess'),
                                this.translate.instant('ebwebApp.mBDeposit.message')
                            );
                        }
                    });
                    this.disableEditButton = false;
                    this.isRecord = true;
                }
            } else {
                this.toastr.error('ebwebApp.mBDeposit.error', 'ebwebApp.mBDeposit.message');
            }
        }
    }

    CTKTExportPDF(isDownload, typeReports: number) {
        if (!this.isCreateUrl) {
            this.mBDepositService
                .getCustomerReport({
                    id: this.mBDeposit.id,
                    typeID: this.TYPE_MBDEPOSIT,
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
            if (typeReports === 1) {
                this.toastr.success(
                    this.translate.instant('ebwebApp.mBDeposit.printing') +
                        this.translate.instant('ebwebApp.mBDeposit.financialPaper') +
                        '...',
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
            } else if (typeReports === 2) {
                this.toastr.success(
                    this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.mBDeposit.creditNote') + '...',
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
            }
        }
    }

    calcular(objcet1: object, typeID: number, name: string, objectParent: object, objectDT1: object[], objectDT2: object[]) {
        this.utilsService.calcular(
            objcet1,
            typeID,
            name,
            objectParent,
            objectDT1,
            objectDT2,
            this.currentAccount,
            this.getAmountOriginalType(),
            this.currentCurrency.formula.includes('*') ? this.mBDeposit.exchangeRate : 1 / this.mBDeposit.exchangeRate
        );
        this.getAmountOriginal();
        this.getAmount();
    }

    // region Tiến lùi chứng từ
    // ham lui, tien
    previousEdit() {
        // goi service get by row num
        if (this.rowNum !== this.count) {
            this.utilsService
                .findByRowNum({
                    id: this.mBDeposit.id,
                    isNext: false,
                    typeID: this.TYPE_MBDEPOSIT,
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

    nextEdit() {
        // goi service get by row num
        if (this.rowNum !== 1) {
            this.utilsService
                .findByRowNum({
                    id: this.mBDeposit.id,
                    isNext: true,
                    typeID: this.TYPE_MBDEPOSIT,
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

    // end of lui tien

    navigate(imbDeposit: IMBDeposit) {
        switch (imbDeposit.typeID) {
            case this.TYPE_MBDEPOSIT:
            case this.TYPE_MCDEPOSIT:
                this.router.navigate(['/mb-deposit', imbDeposit.id, 'edit']);
                break;
            case this.TYPE_MSDEPOSIT:
                this.mBDepositService.find(imbDeposit.id).subscribe((res: HttpResponse<IMBDeposit>) => {
                    const sAInvoiceID = res.body.sAInvoiceID;
                    if (sAInvoiceID) {
                        this.router.navigate(['./ban-hang', 'thu-tien-ngay', sAInvoiceID, 'edit', 'from-mb-deposit', imbDeposit.id]);
                    }
                });
                break;
        }
    }

    private onError(errorMessage: string) {
        this.toastr.error(this.translate.instant('ebwebApp.mBDeposit.error'), this.translate.instant('ebwebApp.mBDeposit.message'));
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoCo_Xoa])
    delete() {
        event.preventDefault();
        if (!this.isCreateUrl && !this.checkCloseBook(this.currentAccount, this.mBDeposit.postedDate) && !this.utilsService.isShowPopup) {
            if (this.mBDeposit.recorded) {
                return;
            } else {
                this.copy();
                this.modalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
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

    @ebAuth(['ROLE_ADMIN', ROLE.BaoCo_Them])
    saveAndNew() {
        event.preventDefault();
        if (this.isCreateUrl && !this.utilsService.isShowPopup) {
            this.warningMessage = '';
            if (this.checkError()) {
                this.mBDeposit.mBDepositDetails = this.mBDepositDetails;
                this.mBDeposit.viewVouchers = this.viewVouchersSelected;
                for (let i = 0; i < this.mBDeposit.mBDepositDetails.length; i++) {
                    this.mBDeposit.mBDepositDetails[i].orderPriority = i + 1;
                }
                if (this.mBDeposit.typeLedger === 0) {
                    this.mBDeposit.noFBook = this.no;
                    this.mBDeposit.noMBook = null;
                } else if (this.mBDeposit.typeLedger === 1) {
                    this.mBDeposit.noMBook = this.no;
                    this.mBDeposit.noFBook = null;
                } else {
                    if (this.isSoTaiChinh) {
                        this.mBDeposit.noFBook = this.no;
                    } else {
                        this.mBDeposit.noMBook = this.no;
                    }
                }
                if (!this.mBDeposit.typeID) {
                    this.mBDeposit.typeID = this.TYPE_MBDEPOSIT;
                }
                this.mBDeposit.totalVATAmount = 0;
                this.mBDeposit.totalVATAmountOriginal = 0;
                this.mBDeposit.exported = false;
                if (this.sysRecord.data === '0') {
                    this.mBDeposit.recorded = true;
                } else {
                    this.mBDeposit.recorded = false;
                }
                this.isSaving = true;
                // this.mBDeposit.mBDepositDetails = this.mBDepositDetails;
                // for (this.i = 0; this.i < this.mBDeposit.mBDepositDetails.length; this.i++) {
                //     this.mBDeposit.mBDepositDetails[this.i].orderPriority = this.i + 1;
                // }
                this.copy();
                if (this.mBDeposit.id !== undefined) {
                    this.subscribeToSaveResponseAndContinue(this.mBDepositService.update(this.mBDeposit));
                } else {
                    this.subscribeToSaveResponseAndContinue(this.mBDepositService.create(this.mBDeposit));
                }
            } else {
            }
        }
    }

    private subscribeToSaveResponseAndContinue(result: Observable<HttpResponse<any>>) {
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
                    this.mBDeposit = {};
                    if (!this.mBDeposit.typeID) {
                        this.mBDeposit.typeID = 160;
                    }
                    this.mBDepositDetails = [];
                    this.viewVouchersSelected = [];
                    this.translate.get(['ebwebApp.mBDeposit.defaultReason']).subscribe(res2 => {
                        this.mBDeposit.reason = res2['ebwebApp.mBDeposit.defaultReason'];
                    });
                    this.router.navigate(['/mb-deposit', 'new']);
                    this.mBDeposit.exported = false;
                    if (this.TCKHAC_SDSoQuanTri === '0') {
                        this.mBDeposit.typeLedger = 0;
                        this.sysTypeLedger = 0;
                        this.isHideTypeLedger = true;
                    } else {
                        if (this.isSoTaiChinh) {
                            this.mBDeposit.typeLedger = 2;
                            this.sysTypeLedger = 0;
                        } else {
                            this.mBDeposit.typeLedger = 2;
                            this.sysTypeLedger = 1;
                        }
                    }
                    this.mBDeposit.date = this.utilsService.ngayHachToan(this.currentAccount);
                    this.mBDeposit.postedDate = this.mBDeposit.date;
                    this.selectChangeExchangeRate();
                    this.copy();
                    this.utilsService
                        .getGenCodeVoucher({
                            typeGroupID: 16, // typeGroupID loại chứng từ
                            companyID: '', // ID công ty
                            branchID: '', // ID chi nhánh
                            displayOnBook: this.sysTypeLedger // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
                        })
                        .subscribe((res2: HttpResponse<string>) => {
                            // this.mCReceipt.noFBook = (res.body.toString());
                            this.mBDeposit.accountingObjectType = 1;
                            this.getObjectType(this.mBDeposit.accountingObjectType);
                            this.no = res2.body;
                            this.mBDeposit.currencyID = this.currency.currencyCode;
                            this.selectChangeCurrency();
                            this.copy();
                        });
                    this.statusVoucher = 0;
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                    return;
                }
            },
            (res: HttpErrorResponse) => {
                this.onSaveError();
                this.isCreateUrl = this.isEdit = true;
            }
        );
    }

    selectChangeExchangeRate() {
        if (this.isCreateUrl) {
            let sum = 0;
            // let sum = 0;
            if (this.currentCurrency) {
                if (this.mBDepositDetails) {
                    for (let i = 0; i < this.mBDepositDetails.length; i++) {
                        this.mBDepositDetails[i].amount = this.round(
                            this.mBDepositDetails[i].amountOriginal *
                                (this.currentCurrency.formula.includes('*')
                                    ? this.mBDeposit.exchangeRate
                                    : 1 / this.mBDeposit.exchangeRate),
                            7
                        );
                        sum = sum + this.mBDepositDetails[i].amount;
                    }
                }
                this.mBDeposit.totalAmount = sum;
            }
        }
    }

    selectChangeDescription() {
        if (this.mBDeposit.mBDepositDetails) {
            if (this.mBDeposit.mBDepositDetails.length > 0) {
                this.mBDepositDetails[0].description = this.mBDeposit.reason;
            }
        }
    }

    onRightClick($event, data, selectedData, isNew, isDelete, select, currentRow) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.selectedData = selectedData;
        if (select === 0) {
            this.contextMenu.isCopy = true;
        } else {
            this.contextMenu.isCopy = false;
        }
        this.select = select;
        this.currentRow = currentRow;
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    public beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            if (this.isCreateUrl) {
                this.modalRef = this.refModalService.open(this.viewVouchersSelected);
            }
        }
    }

    registerRef() {
        this.eventSubscriber = this.eventManager.subscribe('selectViewVoucher', response => {
            if (this.isCreateUrl) {
                this.viewVouchersSelected = response.content ? response.content : [];
            }
            console.log(this.viewVouchersSelected);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    sumAfterDeleteByContextMenu() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            if (this.isEdit) {
                if (this.select === 0) {
                    this.mBDepositDetails.splice(this.currentRow, 1);
                    let sumTotalAmountOriginal = 0;
                    let sumTotalAmount = 0;
                    for (let i = 0; i < this.mBDepositDetails.length; i++) {
                        sumTotalAmount += this.mBDepositDetails[i].amount;
                        sumTotalAmountOriginal += this.mBDepositDetails[i].amountOriginal;
                    }
                    this.mBDeposit.totalAmount = sumTotalAmount;
                    this.mBDeposit.totalAmountOriginal = sumTotalAmountOriginal;
                } else {
                    this.viewVouchersSelected.splice(this.currentRow, 1);
                    this.select = null;
                }
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    keyDownAddRow(value: number) {
        if (this.isCreateUrl && !this.getSelectionText()) {
            if (value !== null && value !== undefined) {
                const ob: IMBDepositDetails = Object.assign({}, this.mBDepositDetails[value]);
                ob.id = undefined;
                ob.orderPriority = undefined;
                this.mBDepositDetails.push(ob);
            } else {
                this.mBDepositDetails.push({});
            }
            this.selectChangeTotalAmount();
        }
    }

    afterAddRow() {
        if (this.isEditUrl) {
            this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
                this.mBDepositDetails.push({ description: this.mBDeposit.reason, amountOriginal: 0, amount: 0 });
                this.selectChangeTotalAmount();
            });
        }
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerCopyRow() {
        if (this.isEditUrl) {
            this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
                const ob: IMBDepositDetails = Object.assign({}, this.contextMenu.selectedData);
                ob.id = undefined;
                ob.orderPriority = undefined;
                this.mBDepositDetails.push(ob);
            });
            this.selectChangeTotalAmount();
            this.eventSubscribers.push(this.eventSubscriber);
        }
    }

    getAmountOriginal() {
        if (this.mBDepositDetails && this.mBDepositDetails.length > 0) {
            return this.mBDepositDetails.map(n => n.amountOriginal).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getAmount() {
        if (this.mBDepositDetails && this.mBDepositDetails.length > 0) {
            return this.mBDepositDetails.map(n => n.amount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    saveContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.save(false);
    }

    close() {
        this.copy();
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.closeAll();
    }

    exit() {
        if (this.modalRef) {
            this.modalRef.close();
            return;
        }
    }

    selectChangeTotalAmount() {
        if (this.isCreateUrl) {
            let totalAmount = 0;
            let totalAmountOriginal = 0;
            if (this.mBDeposit.currencyID && this.currencies) {
                if (this.currentCurrency && this.currentCurrency.formula) {
                    for (let i = 0; i < this.mBDepositDetails.length; i++) {
                        totalAmountOriginal += this.mBDepositDetails[i].amountOriginal;
                        this.mBDepositDetails[i].amount = this.round(
                            this.mBDepositDetails[i].amountOriginal *
                                (this.currentCurrency.formula.includes('*')
                                    ? this.mBDeposit.exchangeRate
                                    : 1 / this.mBDeposit.exchangeRate),
                            7
                        );
                        totalAmount += this.mBDepositDetails[i].amount;
                    }
                    this.mBDeposit.totalAmountOriginal = totalAmountOriginal;
                    this.mBDeposit.totalAmount = totalAmount;
                }
            }
        }
    }

    /*
  * hàm ss du lieu 2 object và 2 mảng object
  * return true: neu tat ca giong nhau
  * return fale: neu 1 trong cac object ko giong nhau
  * */
    canDeactive(): boolean {
        if (this.isReadOnly) {
            return true;
        } else {
            return (
                this.utilsService.isEquivalent(this.mBDeposit, this.mBDepositCopy) &&
                this.utilsService.isEquivalentArray(this.mBDepositDetails, this.mBDepositDetailsCopy)
            );
        }
    }

    /*Nộp tiền từ khách hàng*/
    sumMBDepositDetailCustomers(prop) {
        let total = 0;
        for (let i = 0; i < this.mBDepositDetailCustomers.length; i++) {
            total += this.mBDepositDetailCustomers[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    viewVoucher(imbDepositDetailCustomer: IMBDepositDetailCustomer) {
        let url = '';
        if (imbDepositDetailCustomer.voucherTypeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
            url = `/#/ban-hang/chua-thu-tien/${imbDepositDetailCustomer.saleInvoiceID}/edit/from-ref`;
        } else if (imbDepositDetailCustomer.voucherTypeID === TypeID.CHUNG_TU_NGHIEP_VU_KHAC) {
            url = `/#/g-other-voucher/${imbDepositDetailCustomer.saleInvoiceID}/edit/from-ref`;
        }
        window.open(url, '_blank');
    }

    getAmountOriginalType() {
        if (this.isForeignCurrency()) {
            return 8;
        }
        return 7;
    }

    getEmployeeByID(id) {
        if (this.accounting) {
            const epl = this.accounting.find(n => n.id === id);
            if (epl) {
                return epl.accountingObjectCode;
            } else {
                return '';
            }
        }
    }

    getEMContractsbyID(id) {
        // if (this.eMContracts) {
        //     const eMC = this.eMContracts.find(n => n.id === id);
        //     if (eMC) {
        //         if (this.isSoTaiChinh) {
        //             return eMC.noFBook;
        //         } else {
        //             return eMC.noMBook;
        //         }
        //     }
        // }
    }

    ngAfterViewInit(): void {
        if (this.isCreateUrl) {
            this.focusFirstInput();
        }
    }

    round(value, type) {
        if (type === 8) {
            if (this.isForeignCurrency()) {
                return this.utilsService.round(value, this.currentAccount.systemOption, type);
            } else {
                return this.utilsService.round(value, this.currentAccount.systemOption, 7);
            }
        } else if (type === 2) {
            if (this.isForeignCurrency()) {
                return this.utilsService.round(value, this.currentAccount.systemOption, type);
            } else {
                return this.utilsService.round(value, this.currentAccount.systemOption, 1);
            }
        } else {
            return this.utilsService.round(value, this.currentAccount.systemOption, type);
        }
    }

    isForeignCurrency() {
        return this.currentAccount && this.mBDeposit.currencyID !== this.currentAccount.organizationUnit.currencyID;
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    selectChangeDate() {
        if (this.isEdit) {
            if (this.mBDeposit.date) {
                this.mBDeposit.postedDate = this.mBDeposit.date;
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoCo_In])
    print($event?) {
        event.preventDefault();
    }

    // Loại đối tượng kế toán
    selectAccountingObjectType() {
        if (this.mBDeposit.accountingObjectType !== null && this.mBDeposit.accountingObjectType !== undefined && this.accounting) {
            if (this.mBDeposit.accountingObjectType === 0) {
                // Nhà cung cấp
                this.nameCategory = this.utilsService.NHA_CUNG_CAP;
                this.accountingObjects = this.accounting.filter(n => n.objectType === 0 || n.objectType === 2);
            } else if (this.mBDeposit.accountingObjectType === 1) {
                // Khách hàng
                this.nameCategory = this.utilsService.KHACH_HANG;
                this.accountingObjects = this.accounting.filter(n => n.objectType === 1 || n.objectType === 2);
            } else if (this.mBDeposit.accountingObjectType === 2) {
                // Nhân viên
                this.nameCategory = this.utilsService.NHAN_VIEN;
                this.accountingObjects = this.accounting.filter(n => n.isEmployee);
            } else if (this.mBDeposit.accountingObjectType === 3) {
                // Khác
                this.nameCategory = this.utilsService.KHACH_HANG;
                this.accountingObjects = this.accounting.filter(n => n.objectType === 3);
            } else {
                this.nameCategory = this.utilsService.KHACH_HANG;
                this.accountingObjects = this.accounting;
            }
        }
        if (this.accountingObjects && this.accountingObjects.length > 0) {
            if (this.mBDeposit.accountingObjectID) {
                if (!this.accountingObjects.map(n => n.id).includes(this.mBDeposit.accountingObjectID)) {
                    this.mBDeposit.accountingObjectID = null;
                    this.mBDeposit.accountingObjectName = null;
                    this.mBDeposit.accountingObjectAddress = null;
                }
            }
        }
    }

    saveDetails(i, isAccountingObject?: boolean) {
        this.currentRow = i;
        if (isAccountingObject) {
            const iAccountingObject = this.accounting.find(a => a.id === this.mBDepositDetails[i].accountingObjectID);
            if (iAccountingObject) {
                if (!this.autoPrincipleName) {
                    this.translate.get(['ebwebApp.mBDeposit.defaultReason']).subscribe(res2 => {
                        this.mBDepositDetails[i].description =
                            res2['ebwebApp.mBDeposit.defaultReason'] + ' từ ' + iAccountingObject.accountingObjectName;
                    });
                }
            }
        }
        this.details = this.mBDepositDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.mBDeposit;
    }

    addDataToDetail() {
        this.mBDepositDetails = this.details ? this.details : this.mBDepositDetails;
        console.log(this.mBDepositDetails);
        console.log(this.details);
        this.mBDeposit = this.parent ? this.parent : this.mBDeposit;
    }

    continueDelete() {
        this.mBDepositService.delete(this.mBDeposit.id).subscribe(response => {
            this.modalRef.close();
        });
        this.toastr.success(this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'));
        this.router.navigate(['mb-deposit']);
    }

    closePopUpDelete() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }
}
