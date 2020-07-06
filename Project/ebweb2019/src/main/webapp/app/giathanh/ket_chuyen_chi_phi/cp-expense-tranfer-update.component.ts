import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';

import { CPExpenseTranferService } from './cp-expense-tranfer.service';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { Currency, ICurrency } from 'app/shared/model/currency.model';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { AccountType, MSGERROR, SO_LAM_VIEC, TCKHAC_GhiSo, TCKHAC_SDSoQuanTri } from 'app/app.constants';
import { ROLE } from 'app/role.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { AccountDefaultService } from 'app/danhmuc/account-default';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { CurrencyService } from 'app/danhmuc/currency';
import { BankService } from 'app/danhmuc/bank';
import { ExpenseItemService } from 'app/entities/expense-item';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { EMContractService } from 'app/entities/em-contract';
import { AccountListService } from 'app/danhmuc/account-list';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { CostSetService } from 'app/entities/cost-set';
import { CreditCardService } from 'app/danhmuc/credit-card';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { BudgetItemService } from 'app/entities/budget-item';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { Principal } from 'app/core';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { IAccountAllList } from 'app/shared/model/account-all-list.model';
import * as moment from 'moment';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { Irecord } from 'app/shared/model/record';
import { DATE_FORMAT } from 'app/shared';
import { ICPExpenseTranfer } from 'app/shared/model/cp-expense-tranfer.model';
import { ICPExpenseTranferDetails } from 'app/shared/model/cp-expense-tranfer-details.model';
import { PpGianDonService } from 'app/giathanh/phuong_phap_gian_don';
import { ICPPeriod } from 'app/shared/model/cp-period.model';

@Component({
    selector: 'eb-cp-expense-tranfer-update',
    templateUrl: './cp-expense-tranfer-update.component.html',
    styleUrls: ['./cp-expense-tranfer-update.component.css']
})
export class CPExpenseTranferUpdateComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content: TemplateRef<any>;
    @ViewChild('contentAmount') public modalComponentAmount: NgbModalRef;
    private _cPExpenseTranfer: ICPExpenseTranfer;
    isSaving: boolean;
    postedDateDp: any;
    dateDp: any;
    accountDefaults: any[];
    accountDefault: { value?: string };
    currencies: ICurrency[];
    cPExpenseTranferDetails: ICPExpenseTranferDetails[];
    isRecord: Boolean;
    totalAmount: number;
    totalAmountOriginal: number;
    isThem: Boolean;
    temp: Number;
    totalVatAmount: any;
    totalVatAmountOriginal: any;
    accountList: IAccountList[];
    autoPrinciple: IAutoPrinciple[];
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
    reverse: any;
    record_: any;
    isCreateUrl: boolean;
    isEditUrl: boolean;
    disableAddButton: boolean;
    disableEditButton: boolean;
    accounting: IAccountingObject[];
    searchVoucher: string;
    dataSession: IDataSessionStorage;
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
    isHideTypeLedger: Boolean;
    TCKHAC_SDSoQuanTri: any;
    creditAccountList: IAccountList[];
    creditAccountListItem: IAccountList;
    debitAccountList: IAccountList[];
    debitAccountItem: IAccountList;
    vatAccountList: IAccountList[];
    iAutoPrinciple: IAutoPrinciple;
    cPExpenseTranferCopy: ICPExpenseTranfer;
    cPExpenseTranferDetailsCopy: ICPExpenseTranferDetails[];
    statusVoucher: number;
    viewVouchersSelectedCopy: any;
    type: any;
    no: any;
    isReadOnly: boolean;
    warningMessage: string;
    cPPeriodID: string;
    cPPeriodName: string;
    columnList = [
        { column: AccountType.TK_CO, ppType: false },
        { column: AccountType.TK_NO, ppType: false },
        { column: AccountType.TK_THUE_GTGT, ppType: false },
        { column: AccountType.TKDU_THUE_GTGT, ppType: false }
    ];
    currentRow: number;
    isRefVoucher: boolean;
    currentCurrency?: ICurrency;
    TYPE_KET_CHUYEN_CHI_PHI = 700;
    count: number;

    ROLE_KCCP_XEM = ROLE.KetChuyenChiPhi_Xem;
    ROLE_KCCP_THEM = ROLE.KetChuyenChiPhi_Them;
    ROLE_KCCP_SUA = ROLE.KetChuyenChiPhi_Sua;
    ROLE_KCCP_XOA = ROLE.KetChuyenChiPhi_Xoa;
    ROLE_KCCP_GHISO = ROLE.KetChuyenChiPhi_GhiSo;
    ROLE_KCCP_IN = ROLE.KetChuyenChiPhi_In;
    ROLE_KCCP_KETXUAT = ROLE.KetChuyenChiPhi_KetXuat;

    buttonDeleteTranslate = 'ebwebApp.mBDeposit.toolTip.delete';
    buttonAddTranslate = 'ebwebApp.mBDeposit.toolTip.add';
    buttonEditTranslate = 'ebwebApp.mBDeposit.toolTip.edit';
    buttonSaveTranslate = 'ebwebApp.mBDeposit.toolTip.save';
    buttonRecordTranslate = 'ebwebApp.mBDeposit.toolTip.record';
    buttonUnRecordTranslate = 'ebwebApp.mBDeposit.toolTip.unrecord';
    buttonPrintTranslate = 'ebwebApp.mBDeposit.toolTip.print';
    buttonSaveAndNewTranslate = 'ebwebApp.mBDeposit.toolTip.saveAndNew';
    // buttonCopyAndNewTranslate = 'ebwebApp.mBDeposit.toolTip.copyAndNew';
    buttonCloseFormTranslate = 'ebwebApp.mBDeposit.toolTip.closeForm';

    constructor(
        private cPExpenseTranferService: CPExpenseTranferService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
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
        private cPPeriodService: PpGianDonService
    ) {
        super();
        this.searchVoucher = JSON.parse(sessionStorage.getItem('dataSearchCPExpenseTranfer'));
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
            this.sysRecord = this.currentAccount.systemOption.find(x => x.code === TCKHAC_GhiSo && x.data);
            this.sysTypeLedger = this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data;
            this.TCKHAC_SDSoQuanTri = this.currentAccount.systemOption.find(x => x.code === TCKHAC_SDSoQuanTri).data;
            // tk có
            // this.accountListService
            //     .getAccountType({
            //         typeID: 700,
            //         columnName: AccountType.TK_CO,
            //         ppType: false
            //     })
            //     .subscribe(res => {
            //         this.creditAccountList = res.body;
            //         this.cPExpenseTranferDetails.forEach(item => {
            //             item.creditAccountItem = this.creditAccountList.find(n => n.accountNumber === item.creditAccount);
            //         });
            //     });
            // tk nợ
            // this.accountListService
            //     .getAccountType({
            //         typeID: 700,
            //         columnName: AccountType.TK_NO,
            //         ppType: false
            //     })
            //     .subscribe(res => {
            //         this.debitAccountList = res.body;
            //         this.cPExpenseTranferDetails.forEach(item => {
            //             item.debitAccountItem = this.debitAccountList.find(n => n.accountNumber === item.debitAccount);
            //         });
            //     });
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
            // this.accountListService
            //     .getAccountType({
            //         typeID: 700,
            //         columnName: AccountType.TK_THUE_GTGT,
            //         ppType: false
            //     })
            //     .subscribe(res => {
            //         this.vatAccountList = res.body;
            //         this.cPExpenseTranferDetailTax.forEach(item => {
            //             item.vatAccountItem = this.vatAccountList.find(n => n.accountNumber === item.vATAccount);
            //         });
            //     });
            this.accService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
                this.accounting = res.body;
                this.accountingObjects = res.body
                    .filter(n => n.isActive)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                this.employees = res.body.filter(e => e.isEmployee && e.isActive);
            });
        });
    }

    ngOnInit() {
        this.isSaving = false;
        this.isMainCurrency = false;
        this.isEditUrl = window.location.href.includes('/edit');
        this.isHideTypeLedger = false;
        this.activatedRoute.data.subscribe(({ cPExpenseTranfer }) => {
            this.cPExpenseTranfer = cPExpenseTranfer;
            this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
            if (!this.cPExpenseTranfer.id) {
                if (sessionStorage.getItem('cPPeriodID')) {
                    this.cPPeriodID = JSON.parse(sessionStorage.getItem('cPPeriodID'));
                    this.cPExpenseTranfer.cPPeriodID = this.cPPeriodID;
                    sessionStorage.removeItem('cPPeriodID');
                } else {
                    this.router.navigate(['ket-chuyen-chi-phi']);
                }
                if (sessionStorage.getItem('cPPeriodName')) {
                    this.cPPeriodName = JSON.parse(sessionStorage.getItem('cPPeriodName'));
                    sessionStorage.removeItem('cPPeriodName');
                }
                this.cPExpenseTranferService
                    .getCPExpenseTransferDetails({ cPPeriodID: this.cPPeriodID ? this.cPPeriodID : null })
                    .subscribe((res: HttpResponse<any[]>) => {
                        this.cPExpenseTranferDetails = res.body;
                        if (this.cPExpenseTranferDetails && this.cPExpenseTranferDetails.length > 0) {
                            for (let i = 0; i < this.cPExpenseTranferDetails.length; i++) {
                                this.cPExpenseTranferDetails[i].description =
                                    'Kết chuyển chi phí tài khoản ' +
                                    this.cPExpenseTranferDetails[i].creditAccount +
                                    ' của đối tượng ' +
                                    this.cPExpenseTranferDetails[i].costSetName;
                            }
                        }
                        for (let i = 0; i < this.cPExpenseTranferDetails.length; i++) {
                            this.cPExpenseTranferDetails[i].amountOriginal = this.cPExpenseTranferDetails[i].amount;
                        }
                        this.cPExpenseTranfer.totalAmount = this.getAmount();
                        this.cPExpenseTranfer.totalAmountOriginal = this.getAmountOriginal();
                    });
            }
            if (!this.isEditUrl) {
                this.statusVoucher = 0;
                this.cPExpenseTranfer.date = this.utilService.ngayHachToan(this.currentAccount);
                this.cPExpenseTranfer.postedDate = this.cPExpenseTranfer.date;
                if (this.TCKHAC_SDSoQuanTri === '0') {
                    this.cPExpenseTranfer.typeLedger = 0;
                    this.sysTypeLedger = 0;
                    this.isHideTypeLedger = true;
                } else {
                    this.cPExpenseTranfer.typeLedger = 2;
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
                        this.translate.get(['ebwebApp.cPExpenseTranfer.defaultReason']).subscribe(res2 => {
                            this.cPExpenseTranfer.reason =
                                res2['ebwebApp.cPExpenseTranfer.defaultReason'] + (this.cPPeriodName ? this.cPPeriodName : '');
                        });
                    });
                this.copy();
                this.isReadOnly = false;
            } else {
                this.cPPeriodService.find(this.cPExpenseTranfer.cPPeriodID).subscribe((res: HttpResponse<ICPPeriod>) => {
                    this.cPPeriodName = res.body.name;
                });
                this.utilsService
                    .getIndexRow({
                        id: this.cPExpenseTranfer.id,
                        isNext: true,
                        typeID: this.TYPE_KET_CHUYEN_CHI_PHI,
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
                this.cPExpenseTranferDetails = cPExpenseTranfer.cPExpenseTranferDetails ? cPExpenseTranfer.cPExpenseTranferDetails : [];
                this.viewVouchersSelected = cPExpenseTranfer.viewVouchers ? cPExpenseTranfer.viewVouchers : [];
                this.no = this.isSoTaiChinh ? cPExpenseTranfer.noFBook : cPExpenseTranfer.noMBook;
                if (this.TCKHAC_SDSoQuanTri === '0') {
                    this.isHideTypeLedger = true;
                }
                this.statusVoucher = 1;
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
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticCodes = res.body;
        });
        this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body;
            if (this.cPExpenseTranferDetails) {
                for (let i = 0; i < this.cPExpenseTranferDetails.length; i++) {
                    this.selectChangeCostSet(i);
                }
            }
        });
        this.accountListService.findByGOtherVoucher().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountList = res.body;
        });
        this.expenseItemsService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body;
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
        });
        // Check ghi sổ, đếm số dòng
        if (this.cPExpenseTranfer.id) {
            this.cPExpenseTranferDetails =
                this.cPExpenseTranfer.cPExpenseTranferDetails === undefined ? [] : this.cPExpenseTranfer.cPExpenseTranferDetails;
        } else {
            this.cPExpenseTranferDetails = [];
            this.viewVouchersSelected = [];
            this.isRecord = false;
        }
        if (this.cPExpenseTranfer.id) {
            this.id = this.cPExpenseTranfer.id;
        } else {
        }
        this.isCreateUrl = window.location.href.includes('/ket-chuyen-chi-phi/new');
        if (this.cPExpenseTranfer.id && !this.isCreateUrl) {
            this.isEdit = this.isCreateUrl = false;
        } else {
            this.isEdit = this.isCreateUrl = true;
        }
        this.disableAddButton = true;
        if (this.cPExpenseTranfer.id) {
            if (!this.cPExpenseTranfer.recorded && this.isEditUrl) {
                this.disableEditButton = false;
            } else {
                this.disableEditButton = true;
            }
        } else {
            this.disableEditButton = true;
        }
        // // sao chép và thêm mới
        // if (sessionStorage.getItem('saveAndLoad')) {
        //     this.cPExpenseTranfer = JSON.parse(sessionStorage.getItem('saveAndLoad'));
        //     this.cPExpenseTranferDetails = this.cPExpenseTranfer.cPExpenseTranferDetails;
        //     this.viewVouchersSelected = this.cPExpenseTranfer.viewVouchers;
        //     this.cPExpenseTranfer.date = moment(this.cPExpenseTranfer.date);
        //     this.cPExpenseTranfer.postedDate = this.cPExpenseTranfer.date;
        //     this.copy();
        //     sessionStorage.removeItem('saveAndLoad');
        // }
        this.isRefVoucher = window.location.href.includes('/edit/from-ref');
        this.registerRef();
        this.afterCopyRow();
        this.afterAddRow();
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
            this.utilService.setShowPopup(response.content);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        this.sumAfterDeleteByContextMenu();
    }

    closeForm() {
        event.preventDefault();
        if (this.cPExpenseTranferCopy && (this.statusVoucher === 0 || this.statusVoucher === 1) && !this.utilsService.isShowPopup) {
            if (
                !this.utilsService.isEquivalent(this.cPExpenseTranfer, this.cPExpenseTranferCopy) ||
                !this.utilsService.isEquivalentArray(this.cPExpenseTranferDetails, this.cPExpenseTranferDetailsCopy) ||
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
    //
    // @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenChiPhi_Them])
    // copyAndNew() {
    //     event.preventDefault();
    //     if (!this.isCreateUrl && !this.utilsService.isShowPopup) {
    //         this.cPExpenseTranfer.id = undefined;
    //         for (let i = 0; i < this.cPExpenseTranfer.cPExpenseTranferDetails.length; i++) {
    //             this.cPExpenseTranfer.cPExpenseTranferDetails[i].id = undefined;
    //         }
    //         for (let i = 0; i < this.cPExpenseTranfer.viewVouchers.length; i++) {
    //             this.cPExpenseTranfer.viewVouchers[i].id = undefined;
    //         }
    //         this.cPExpenseTranfer.noMBook = null;
    //         this.cPExpenseTranfer.noFBook = null;
    //         sessionStorage.setItem('saveAndLoad', JSON.stringify(this.cPExpenseTranfer));
    //
    //         this.edit();
    //         this.isSaving = false;
    //         this.copy();
    //         this.router.navigate(['ket-chuyen-chi-phi/new']);
    //         this.statusVoucher = 0;
    //     }
    // }

    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenChiPhi_Sua])
    edit() {
        event.preventDefault();
        // && this.isRecord
        if (
            !this.isCreateUrl &&
            !this.checkCloseBook(this.currentAccount, this.cPExpenseTranfer.postedDate) &&
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

    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenChiPhi_Them])
    addNew($event) {
        event.preventDefault();
        if (!this.isCreateUrl && !this.utilsService.isShowPopup) {
            sessionStorage.setItem('checkNewKCCP', JSON.stringify(true));
            this.router.navigate(['ket-chuyen-chi-phi']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenChiPhi_Them, ROLE.KetChuyenChiPhi_Sua])
    save(isNew = false) {
        event.preventDefault();
        this.cPExpenseTranfer.cPExpenseTranferDetails = this.cPExpenseTranferDetails;
        this.cPExpenseTranfer.viewVouchers = this.viewVouchersSelected;
        if (this.isCreateUrl && !this.isShowPopup) {
            if (this.checkError()) {
                if (this.cPExpenseTranfer && this.cPExpenseTranfer.cPExpenseTranferDetails && this.cPExpenseTranferDetails.length > 0) {
                    for (let i = 0; i < this.cPExpenseTranfer.cPExpenseTranferDetails.length; i++) {
                        this.cPExpenseTranfer.cPExpenseTranferDetails[i].orderPriority = i + 1;
                    }
                }
                this.isCreateUrl = !this.isCreateUrl;
                this.isEdit = this.isCreateUrl;
                this.disableAddButton = true;
                if (this.cPExpenseTranfer.typeLedger === 0) {
                    this.cPExpenseTranfer.noFBook = this.no;
                    this.cPExpenseTranfer.noMBook = null;
                } else if (this.cPExpenseTranfer.typeLedger === 1) {
                    this.cPExpenseTranfer.noFBook = null;
                    this.cPExpenseTranfer.noMBook = this.no;
                } else {
                    if (this.isSoTaiChinh) {
                        this.cPExpenseTranfer.noFBook = this.no;
                    } else {
                        this.cPExpenseTranfer.noMBook = this.no;
                    }
                }
                this.cPExpenseTranfer.typeID = 701;
                this.isSaving = true;
                // check is url new
                if (this.isCreateUrl && this.cPExpenseTranfer.id) {
                    this.cPExpenseTranfer.id = undefined;
                }
                if (!this.isCreateUrl && !this.isEditUrl) {
                    this.cPExpenseTranfer.id = undefined;
                    for (let i = 0; i < this.cPExpenseTranfer.cPExpenseTranferDetails.length; i++) {
                        this.cPExpenseTranfer.cPExpenseTranferDetails[i].id = undefined;
                    }
                }
                if (!this.isCreateUrl && !this.isEditUrl) {
                    this.cPExpenseTranfer.id = undefined;
                }
                if (this.cPExpenseTranfer.id) {
                    this.subscribeToSaveResponse(this.cPExpenseTranferService.update(this.cPExpenseTranfer));
                } else {
                    this.subscribeToSaveResponse(this.cPExpenseTranferService.create(this.cPExpenseTranfer));
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
                    if (res.body.cpExpenseTranfer.recorded) {
                        this.disableEditButton = true;
                        this.disableAddButton = false;
                    } else {
                        this.disableEditButton = false;
                        this.disableAddButton = true;
                    }
                    this.cPExpenseTranfer.id = res.body.cpExpenseTranfer.id;
                    this.cPExpenseTranfer.recorded = res.body.cpExpenseTranfer.recorded;
                    this.onSaveSuccess();
                    this.isReadOnly = true;
                    this.router.navigate(['./ket-chuyen-chi-phi', this.cPExpenseTranfer.id, 'edit']);
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
                        this.cPExpenseTranfer.id = res.body.cpExpenseTranfer.id;
                        this.copy();
                        this.router.navigate(['./ket-chuyen-chi-phi', this.cPExpenseTranfer.id, 'edit']);
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
                        this.cPExpenseTranfer.id = res.body.cpExpenseTranfer.id;
                        this.copy();
                        this.router.navigate(['./ket-chuyen-chi-phi', this.cPExpenseTranfer.id, 'edit']);
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
                        this.cPExpenseTranfer.id = res.body.cpExpenseTranfer.id;
                        this.copy();
                        this.router.navigate(['./ket-chuyen-chi-phi', this.cPExpenseTranfer.id, 'edit']);
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
        // this.disableEditButton = true;
        this.isRecord = false;
    }

    private onSaveError() {
        this.isSaving = false;
        this.isCreateUrl = !this.isCreateUrl;
        this.isEdit = this.isCreateUrl;
    }

    selectChangeReason() {
        this.cPExpenseTranfer.reason = this.autoPrincipleName;
        this.iAutoPrinciple = this.autoPrinciple.find(a => a.autoPrincipleName === this.autoPrincipleName);
        if (this.cPExpenseTranfer.cPExpenseTranferDetails) {
            if (this.cPExpenseTranfer.cPExpenseTranferDetails.length > 0) {
                this.cPExpenseTranferDetails[0].description = this.autoPrincipleName;
            }
        }
        for (const dt of this.cPExpenseTranferDetails) {
            dt.debitAccount = this.iAutoPrinciple.debitAccount;
            dt.creditAccount = this.iAutoPrinciple.creditAccount;
            dt.description = this.iAutoPrinciple.autoPrincipleName;
        }
    }

    selectChangeDescription() {
        if (this.cPExpenseTranfer.cPExpenseTranferDetails) {
            if (this.cPExpenseTranfer.cPExpenseTranferDetails.length > 0) {
                this.cPExpenseTranferDetails[0].description = this.cPExpenseTranfer.reason;
            }
        }
    }

    keyPress(value: number, select: number) {
        if (select === 0) {
            this.cPExpenseTranferDetails.splice(value, 1);
        } else {
            this.viewVouchersSelected.splice(value, 1);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenChiPhi_GhiSo])
    record() {
        event.preventDefault();
        if (
            !this.cPExpenseTranfer.recorded &&
            !this.isCreateUrl &&
            !this.checkCloseBook(this.currentAccount, this.cPExpenseTranfer.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            if (this.cPExpenseTranfer.id) {
                this.record_ = {};
                this.record_.id = this.cPExpenseTranfer.id;
                this.record_.typeID = this.cPExpenseTranfer.typeID;
                if (!this.cPExpenseTranfer.recorded) {
                    this.gLService.record(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                        if (res.body.success) {
                            this.cPExpenseTranfer.recorded = true;
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

    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenChiPhi_GhiSo])
    unrecord() {
        event.preventDefault();
        if (
            this.cPExpenseTranfer.recorded &&
            !this.isCreateUrl &&
            !this.checkCloseBook(this.currentAccount, this.cPExpenseTranfer.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            if (this.cPExpenseTranfer.id) {
                this.record_ = {};
                this.record_.id = this.cPExpenseTranfer.id;
                this.record_.typeID = this.cPExpenseTranfer.typeID;
                if (this.cPExpenseTranfer.recorded) {
                    this.gLService.unrecord(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                        if (res.body.success) {
                            this.cPExpenseTranfer.recorded = false;
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
            this.cPExpenseTranferService
                .getCustomerReport({
                    id: this.cPExpenseTranfer.id,
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

    // ham lui, tien
    previousEdit() {
        // goi service get by row num
        if (this.rowNum !== this.count) {
            this.utilsService
                .findByRowNum({
                    id: this.cPExpenseTranfer.id,
                    isNext: false,
                    typeID: this.TYPE_KET_CHUYEN_CHI_PHI,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<ICPExpenseTranfer>) => {
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
                    id: this.cPExpenseTranfer.id,
                    isNext: true,
                    typeID: this.TYPE_KET_CHUYEN_CHI_PHI,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<ICPExpenseTranfer>) => {
                        // this.router.navigate(['/mc-payment', res.body.id, 'edit']);
                        this.navigate(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    navigate(iCPExpenseTranfer: ICPExpenseTranfer) {
        this.router.navigate(['/ket-chuyen-chi-phi', iCPExpenseTranfer.id, 'edit']);
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

    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenChiPhi_Xoa])
    delete() {
        event.preventDefault();
        if (
            !this.isCreateUrl &&
            !this.checkCloseBook(this.currentAccount, this.cPExpenseTranfer.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            if (this.cPExpenseTranfer.recorded) {
                return;
            } else {
                this.router.navigate(['/ket-chuyen-chi-phi', { outlets: { popup: this.cPExpenseTranfer.id + '/delete' } }]);
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

    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenChiPhi_Them])
    saveAndNew() {
        event.preventDefault();
        this.cPExpenseTranfer.cPExpenseTranferDetails = this.cPExpenseTranferDetails;
        if (this.isCreateUrl && !this.utilsService.isShowPopup) {
            if (this.checkError()) {
                this.isCreateUrl = !this.isCreateUrl;
                this.isEdit = this.isCreateUrl;
                this.disableAddButton = true;
                if (this.cPExpenseTranfer.typeLedger === 0) {
                    this.cPExpenseTranfer.noFBook = this.no;
                    this.cPExpenseTranfer.noMBook = null;
                } else if (this.cPExpenseTranfer.typeLedger === 1) {
                    this.cPExpenseTranfer.noFBook = null;
                    this.cPExpenseTranfer.noMBook = this.no;
                } else {
                    if (this.isSoTaiChinh) {
                        this.cPExpenseTranfer.noFBook = this.no;
                    } else {
                        this.cPExpenseTranfer.noMBook = this.no;
                    }
                }
                this.cPExpenseTranfer.typeID = 701;
                this.isSaving = true;
                // check is url new
                if (this.isCreateUrl && this.cPExpenseTranfer.id) {
                    this.cPExpenseTranfer.id = undefined;
                }
                if (!this.isCreateUrl && !this.isEditUrl) {
                    this.cPExpenseTranfer.id = undefined;
                    for (let i = 0; i < this.cPExpenseTranfer.cPExpenseTranferDetails.length; i++) {
                        this.cPExpenseTranfer.cPExpenseTranferDetails[i].id = undefined;
                    }
                }
                if (!this.isCreateUrl && !this.isEditUrl) {
                    this.cPExpenseTranfer.id = undefined;
                }
                if (this.cPExpenseTranfer.id !== undefined) {
                    this.subscribeToSaveResponseAndContinue(this.cPExpenseTranferService.update(this.cPExpenseTranfer));
                } else {
                    this.subscribeToSaveResponseAndContinue(this.cPExpenseTranferService.create(this.cPExpenseTranfer));
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
                    this.copy();
                    sessionStorage.setItem('checkNewKCCP', JSON.stringify(true));
                    this.router.navigate(['ket-chuyen-chi-phi']);
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                    return;
                } else if (res.body.status === 2) {
                    if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.cPExpenseTranfer.id = res.body.cpExpenseTranfer.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyQT'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.copy();
                        this.router.navigate(['/ket-chuyen-chi-phi', res.body.cpExpenseTranfer.id, 'edit']).then(() => {
                            this.isReadOnly = true;
                            sessionStorage.setItem('checkNewKCCP', JSON.stringify(true));
                            this.router.navigate(['ket-chuyen-chi-phi']);
                        });
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.cPExpenseTranfer.id = res.body.cpExpenseTranfer.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuyTC'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.copy();
                        this.router.navigate(['/ket-chuyen-chi-phi', res.body.cpExpenseTranfer.id, 'edit']).then(() => {
                            this.isReadOnly = true;
                            sessionStorage.setItem('checkNewKCCP', JSON.stringify(true));
                            this.router.navigate(['ket-chuyen-chi-phi']);
                        });
                    } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                        this.isSaving = false;
                        this.statusVoucher = 1;
                        this.isEdit = false;
                        this.cPExpenseTranfer.id = res.body.cpExpenseTranfer.id;
                        this.toastr.error(
                            this.translate.instant('global.messages.error.checkTonQuy'),
                            this.translate.instant('ebwebApp.mCReceipt.error.error')
                        );
                        this.copy();
                        this.router.navigate(['/ket-chuyen-chi-phi', res.body.cpExpenseTranfer.id, 'edit']).then(() => {
                            this.isReadOnly = true;
                            sessionStorage.setItem('checkNewKCCP', JSON.stringify(true));
                            this.router.navigate(['ket-chuyen-chi-phi']);
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
                this.viewVouchersSelected = this.viewVouchersSelected.filter(x => x.attach === true);
                response.content.forEach(item => {
                    item.attach = false;
                    this.viewVouchersSelected.push(item);
                });
            }
            this.eventSubscribers.push(this.eventSubscriber);
        });
    }

    beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            if (this.isCreateUrl) {
                this.modalRef = this.refModalService.open(this.viewVouchersSelected);
            }
        }
    }

    getAmountOriginal() {
        if (this.cPExpenseTranferDetails && this.cPExpenseTranferDetails.length > 0) {
            return this.cPExpenseTranferDetails.map(n => n.amount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    getAmount() {
        if (this.cPExpenseTranferDetails && this.cPExpenseTranferDetails.length > 0) {
            return this.cPExpenseTranferDetails.map(n => n.amount).reduce((a, b) => a + b);
        } else {
            return 0;
        }
    }

    closeAll() {
        this.copy();
        if (sessionStorage.getItem('dataSearchCPExpenseTranfer')) {
            this.router.navigate(['/ket-chuyen-chi-phi', 'hasSearch', '1']);
        } else {
            this.router.navigate(['/ket-chuyen-chi-phi']);
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

    copy() {
        this.cPExpenseTranferCopy = Object.assign({}, this.cPExpenseTranfer);
        this.cPExpenseTranferDetailsCopy = this.cPExpenseTranferDetails.map(object => ({ ...object }));
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
                this.utilService.isEquivalent(this.cPExpenseTranfer, this.cPExpenseTranferCopy) &&
                this.utilService.isEquivalentArray(this.cPExpenseTranferDetails, this.cPExpenseTranferDetailsCopy) &&
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
        if (this.checkCloseBook(this.currentAccount, this.cPExpenseTranfer.postedDate)) {
            this.toastr.error(this.translate.instant('ebwebApp.mBCreditCard.checkCloseBook'));
            return false;
        }
        if (!this.cPExpenseTranfer.date) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.date') + ' ' + this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        if (!this.cPExpenseTranfer.postedDate) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.postedDate') +
                ' ' +
                this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        if (this.cPExpenseTranfer.postedDate.format(DATE_FORMAT) < this.cPExpenseTranfer.date.format(DATE_FORMAT)) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBCreditCard.errorPostedDateAndDate'),
                this.translate.instant('ebwebApp.mBCreditCard.message')
            );
            return false;
        }
        if (this.cPExpenseTranfer.typeLedger === undefined || this.cPExpenseTranfer.typeLedger === null) {
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
        if (this.cPExpenseTranfer.cPExpenseTranferDetails.length === 0) {
            this.warningMessage =
                this.translate.instant('ebwebApp.mBCreditCard.home.details') +
                ' ' +
                this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
            this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
            return false;
        }
        for (this.i = 0; this.i < this.cPExpenseTranfer.cPExpenseTranferDetails.length; this.i++) {
            if (
                !this.cPExpenseTranfer.cPExpenseTranferDetails[this.i].debitAccount ||
                this.cPExpenseTranfer.cPExpenseTranferDetails[this.i].debitAccount === ''
            ) {
                this.warningMessage =
                    this.translate.instant('ebwebApp.mBCreditCard.debitAccount') +
                    ' ' +
                    this.translate.instant('ebwebApp.mBCreditCard.isNotNull');
                this.toastr.error(this.warningMessage.toString(), this.translate.instant('ebwebApp.mBCreditCard.message'));
                return false;
            }
            if (
                !this.cPExpenseTranfer.cPExpenseTranferDetails[this.i].creditAccount ||
                this.cPExpenseTranfer.cPExpenseTranferDetails[this.i].creditAccount === ''
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
            this.cPExpenseTranferDetails,
            null,
            this.costSets,
            null,
            null,
            null,
            null,
            this.expenseItems,
            null,
            this.statisticCodes,
            null,
            true
        );
        if (checkAcc) {
            this.toastr.error(checkAcc, this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        return true;
    }

    continueModalSaveComponent() {
        this.isCreateUrl = !this.isCreateUrl;
        this.isEdit = this.isCreateUrl;
        this.disableAddButton = true;
        if (this.cPExpenseTranfer.typeLedger === 0) {
            this.cPExpenseTranfer.noFBook = this.no;
            this.cPExpenseTranfer.noMBook = null;
        } else if (this.cPExpenseTranfer.typeLedger === 1) {
            this.cPExpenseTranfer.noFBook = null;
            this.cPExpenseTranfer.noMBook = this.no;
        } else {
            if (this.isSoTaiChinh) {
                this.cPExpenseTranfer.noFBook = this.no;
            } else {
                this.cPExpenseTranfer.noMBook = this.no;
            }
        }
        this.cPExpenseTranfer.typeID = 701;
        // if (this.sysRecord.data === '0') {
        //     this.cPExpenseTranfer.recorded = true;
        // } else {
        //     this.cPExpenseTranfer.recorded = false;
        // }
        this.isSaving = true;
        // check is url new
        if (this.isCreateUrl && this.cPExpenseTranfer.id) {
            this.cPExpenseTranfer.id = undefined;
        }
        if (!this.isCreateUrl && !this.isEditUrl) {
            this.cPExpenseTranfer.id = undefined;
            for (let i = 0; i < this.cPExpenseTranfer.cPExpenseTranferDetails.length; i++) {
                this.cPExpenseTranfer.cPExpenseTranferDetails[i].id = undefined;
            }
        }
        if (!this.isCreateUrl && !this.isEditUrl) {
            this.cPExpenseTranfer.id = undefined;
        }
        if (this.cPExpenseTranfer.id) {
            this.subscribeToSaveResponse(this.cPExpenseTranferService.update(this.cPExpenseTranfer));
        } else {
            this.subscribeToSaveResponse(this.cPExpenseTranferService.create(this.cPExpenseTranfer));
        }
    }

    closeModalSaveComponent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    afterCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            if (this.select === 0 || this.select === 3) {
                const ob: ICPExpenseTranferDetails = Object.assign({}, this.contextMenu.selectedData);
                ob.id = undefined;
                ob.orderPriority = undefined;
                this.cPExpenseTranferDetails.push(ob);
                this.selectChangeTotalAmount();
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    afterAddRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            if (this.select === 0 || this.select === 3) {
                this.cPExpenseTranferDetails.push({
                    description: this.cPExpenseTranfer.reason,
                    amount: 0,
                    amountOriginal: 0
                });
            }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    selectChangeTotalAmount() {
        let totalAmount = 0;
        let totalAmountOriginal = 0;
        for (let i = 0; i < this.cPExpenseTranferDetails.length; i++) {
            totalAmountOriginal += this.cPExpenseTranferDetails[i].amountOriginal;
            this.cPExpenseTranferDetails[i].amount = this.cPExpenseTranferDetails[i].amountOriginal;
            totalAmount += this.cPExpenseTranferDetails[i].amount;
        }
        this.cPExpenseTranfer.totalAmountOriginal = totalAmountOriginal;
        this.cPExpenseTranfer.totalAmount = totalAmount;
    }

    changeAmount(index) {
        if (this.isCreateUrl) {
            let totalAmount = 0;
            let totalAmountOriginal = 0;
            for (let i = 0; i < this.cPExpenseTranferDetails.length; i++) {
                totalAmount += this.cPExpenseTranferDetails[i].amount;
                totalAmountOriginal += this.cPExpenseTranferDetails[i].amountOriginal;
            }
            this.cPExpenseTranfer.totalAmount = totalAmount;
            this.cPExpenseTranfer.totalAmountOriginal = totalAmountOriginal;
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

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    selectChangeDate() {
        if (this.isEdit) {
            if (this.cPExpenseTranfer.date) {
                this.cPExpenseTranfer.postedDate = this.cPExpenseTranfer.date;
            }
        }
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.cPExpenseTranferDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.cPExpenseTranfer;
    }

    addDataToDetail() {
        this.cPExpenseTranferDetails = this.details ? this.details : this.cPExpenseTranferDetails;
        this.cPExpenseTranfer = this.parent ? this.parent : this.cPExpenseTranfer;
    }

    sumAfterDeleteByContextMenu() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            if (this.isCreateUrl) {
                if (this.select === 0) {
                    this.cPExpenseTranferDetails.splice(this.currentRow, 1);
                } else {
                    this.viewVouchersSelected.splice(this.currentRow, 1);
                    this.select = null;
                }
                let sumTotalAmountOriginal = 0;
                let sumTotalAmount = 0;
                for (let i = 0; i < this.cPExpenseTranferDetails.length; i++) {
                    sumTotalAmount += this.cPExpenseTranferDetails[i].amount;
                    sumTotalAmountOriginal += this.cPExpenseTranferDetails[i].amountOriginal;
                }
                this.cPExpenseTranfer.totalAmount = sumTotalAmount;
                this.cPExpenseTranfer.totalAmountOriginal = sumTotalAmountOriginal;
            }
            this.selectChangeTotalAmount();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    get cPExpenseTranfer() {
        return this._cPExpenseTranfer;
    }

    set cPExpenseTranfer(cPExpenseTranfer: ICPExpenseTranfer) {
        this._cPExpenseTranfer = cPExpenseTranfer;
    }

    selectChangeCostSet(index) {
        if (this.cPExpenseTranferDetails && this.cPExpenseTranferDetails.length > 0 && this.cPExpenseTranferDetails[index].costSetID) {
            const costSetName = this.costSets.find(a => a.id === this.cPExpenseTranferDetails[index].costSetID).costSetName;
            if (costSetName) {
                this.cPExpenseTranferDetails[index].costSetName = costSetName;
            }
        }
    }

    selectChangeAmount() {
        let totalAmount = 0;
        if (this.cPExpenseTranferDetails && this.cPExpenseTranferDetails.length > 0) {
            for (let i = 0; i < this.cPExpenseTranferDetails.length; i++) {
                totalAmount += this.cPExpenseTranferDetails[i].amount;
                this.cPExpenseTranferDetails[i].amountOriginal = this.cPExpenseTranferDetails[i].amount;
            }
            this.cPExpenseTranfer.totalAmountOriginal = totalAmount;
            this.cPExpenseTranfer.totalAmount = totalAmount;
        }
    }

    getCostSetName(id) {
        if (this.costSets) {
            const costSet = this.costSets.find(n => n.id === id);
            if (costSet) {
                return costSet.costSetName;
            }
        }
    }
}
