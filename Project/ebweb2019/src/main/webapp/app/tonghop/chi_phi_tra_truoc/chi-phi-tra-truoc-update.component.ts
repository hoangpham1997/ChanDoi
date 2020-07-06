import { AfterViewChecked, AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { BankService } from 'app/danhmuc/bank';
import { IBank } from 'app/shared/model/bank.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { Currency, ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { CostSetService } from 'app/entities/cost-set';
import { ExpenseItemService } from 'app/entities/expense-item';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { ICreditCard } from 'app/shared/model/credit-card.model';
import { CreditCardService } from 'app/danhmuc/credit-card';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { AccountDefaultService } from 'app/danhmuc/account-default';
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
import {
    DDSo_NCachHangDVi,
    DDSo_NCachHangNghin,
    DDSo_NgoaiTe,
    DDSo_TienVND,
    SD_SO_QUAN_TRI,
    SO_LAM_VIEC,
    TCKHAC_GhiSo,
    TCKHAC_SDSoQuanTri
} from 'app/app.constants';
import { Principal } from 'app/core';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import * as moment from 'moment';
import { Moment } from 'moment';
import { ChiPhiTraTruocService } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc.service';
import { AccountListService } from 'app/danhmuc/account-list';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { IPrepaidExpense } from 'app/shared/model/prepaid-expense.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IPrepaidExpenseAllocation, PrepaidExpenseAllocation } from 'app/shared/model/prepaid-expense-allocation.model';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { ExportPrepaidExpenseVoucherModalService } from 'app/core/login/prepaid-expense-voucher-modal.service';
import { XuatHoaDonService } from 'app/ban-hang/xuat-hoa-don/xuat-hoa-don.service';
import { PrepaidExpenseService } from 'app/entities/prepaid-expense';
import { ExportPrepaidExpenseCurrentBookModalService } from 'app/core/login/prepaid-expense-current-book-modal.service';
import { ROLE } from 'app/role.constants';
import { forEach } from '@angular/router/src/utils/collection';
import { DATE_FORMAT_YMD } from 'app/shared';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-chi-phi-tra-truoc-update',
    templateUrl: './chi-phi-tra-truoc-update.component.html',
    styleUrls: ['./chi-phi-tra-truoc-update.component.css']
})
export class ChiPhiTraTruocUpdateComponent extends BaseComponent implements OnInit, AfterViewInit {
    @ViewChild('checkAmount') public modalCheckAmount: NgbModalRef;
    @ViewChild('checkPB') public modalCheckPB: NgbModalRef;
    @ViewChild('content') public modalContent: NgbModalRef;
    @ViewChild('deleteModal') public modalDeleteModal: NgbModalRef;
    private _gOtherVoucher: IGOtherVoucher;
    prepaidExpense: IPrepaidExpense;
    prepaidExpenseCopy: IPrepaidExpense;
    costAccounts: any[];
    expenseItemList: any[];
    prepaidExpenseCodeList: any[];
    prepaidExpenseAllocations: IPrepaidExpenseAllocation[];
    prepaidExpenseAllocationsCopy: IPrepaidExpenseAllocation[];
    prepaidExpenseCode: any;
    isSaving: boolean;
    dateDp: any;
    postedDateDp: any;
    accountDefaults: any[];
    accountDefault: { value?: string };
    employees: IAccountingObject[];
    accountingObjects: IAccountingObject[];
    currencies: ICurrency[];
    bankAccountDetails: IBankAccountDetails[];
    statisticsCodes: IStatisticsCode[];
    costSets: ICostSet[];
    expenseItems: IExpenseItem[];
    organizationUnits: IOrganizationUnit[];
    eMContracts: IEMContract[];
    creditCards: ICreditCard[];
    isRecord: Boolean;
    totalAmount: number;
    totalAmountOriginal: number;
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
    budgetItem: IBudgetItem[];
    viewVouchersSelected: any;
    contextMenu: ContextMenu;
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
    debitAccountList: IAccountList[];
    debitAccountItem: IAccountList;
    vatAccountList: IAccountList[];
    statusVoucher: number;
    viewVouchersSelectedCopy: any;
    type: any;
    no: any;
    goodsServicePurchases: IGoodsServicePurchase[];
    goodsServicePurchaseID: any;
    isReadOnly: boolean;
    typeDDSO_TienVND: any;
    typeDDSO_NgoaiTe: any;
    sessionWork: boolean;
    checkData: boolean;
    isActive: boolean;
    checkIsActive: boolean;
    prepaidExpenseVouchers: any[];
    prepaidExpenseVouchersSave: any[];
    startUsing: Moment;
    checkSave: Boolean;
    prepaidExpenseDTO: any;
    isCloseAll: boolean;
    countPB: number;
    authoritiesNoMBook: boolean;
    ROLE_Xem = ROLE.ChiPhiTRaTruoc_Xem;
    ROLE_Them = ROLE.ChiPhiTRaTruoc_Them;
    ROLE_Sua = ROLE.ChiPhiTRaTruoc_Sua;
    ROLE_Xoa = ROLE.ChiPhiTRaTruoc_Xoa;
    checkEditRoleSua: boolean;
    checkEditRoleThem: boolean;
    arrAuthorities: any[];
    isNew: boolean;
    isEditAu: boolean;
    indexFocusDetailCol: any;
    listIDInputDeatilTax: any[] = ['debitAccount_DT', 'allocationObjectName', 'allocationRate', 'costAccounts', 'unitPrice'];
    indexFocusDetailRow: any;
    prepaidExpenseCodeList2: any;
    expenseItemList2: any[];

    constructor(
        private expenseCurrentBookModalService: ExportPrepaidExpenseCurrentBookModalService,
        private prepaidExpenseService: PrepaidExpenseService,
        private router: Router,
        private chiPhiTraTruocService: ChiPhiTraTruocService,
        private expenseItemService: ExpenseItemService,
        private gOtherVoucherService: ChiPhiTraTruocService,
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
        private exportPrepaidExpenseVoucherModalService: ExportPrepaidExpenseVoucherModalService,
        private principal: Principal,
        private modalService: NgbModal,
        private xuatHoaDonService: XuatHoaDonService,
        private goodsServicePurchaseService: GoodsServicePurchaseService
    ) {
        super();
        this.dataSession = JSON.parse(sessionStorage.getItem('dataSession'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.predicate = this.dataSession.predicate;
            this.searchVoucher = this.dataSession.searchVoucher;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
        this.contextMenu = new ContextMenu();

        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.startUsing = this.currentAccount.organizationUnit.startDate;
            this.arrAuthorities = this.currentAccount.authorities;
            this.isNew = window.location.href.includes('new');
            this.isEditAu = window.location.href.includes('edit');
            this.checkEditRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_Sua) : true;
            this.checkEditRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_Them) : true;
            this.sessionWork = this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data === '1';
            this.goodsServicePurchaseID = this.currentAccount.organizationUnit.goodsServicePurchaseID;
            this.sysRecord = this.currentAccount.systemOption.find(x => x.code === TCKHAC_GhiSo && x.data);
            this.sysTypeLedger = this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data;
            this.TCKHAC_SDSoQuanTri = this.currentAccount.systemOption.find(x => x.code === TCKHAC_SDSoQuanTri).data;
            this.authoritiesNoMBook = this.currentAccount.systemOption.find(x => x.code === SD_SO_QUAN_TRI).data === '1';
            this.currencyService.findAllActive().subscribe(res => {
                this.currencies = res.body;
                this.currency = this.currencies.find(cur => cur.currencyCode === this.currentAccount.organizationUnit.currencyID);
                this.DDSo_NCachHangNghin = this.currentAccount.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
                this.DDSo_NCachHangDVi = this.currentAccount.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;
                this.DDSo_NgoaiTe = this.currentAccount.systemOption.find(x => x.code === DDSo_NgoaiTe && x.data).data;
                this.DDSo_TienVND = this.currentAccount.systemOption.find(x => x.code === DDSo_TienVND && x.data).data;
            });
            this.accService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
                this.accounting = res.body;
                this.accountingObjects = res.body;
                this.employees = res.body.filter(e => e.isEmployee && e.isActive);
            });
        });
    }

    ngOnInit() {
        this.getDataView();
        this.prepaidExpenseAllocations = [];
        this.prepaidExpense = {};
        this.isSaving = false;
        this.isMainCurrency = false;
        this.isEditUrl = window.location.href.includes('/edit');
        this.isHideTypeLedger = false;
        this.activatedRoute.data.subscribe(data => {
            if (data.prepaidExpense && data.prepaidExpense.id) {
                this.prepaidExpense = data.prepaidExpense;
                this.isActive = !this.prepaidExpense.isActive;
                this.checkIsActive = this.isActive;
                this.prepaidExpense.typeExpense = data.prepaidExpense.typeExpense;
                if (data.prepaidExpense.prepaidExpenseAllocation && data.prepaidExpense.prepaidExpenseAllocation.length > 0) {
                    this.prepaidExpenseAllocations = data.prepaidExpense.prepaidExpenseAllocation
                        ? data.prepaidExpense.prepaidExpenseAllocation
                        : [];
                }
                this.prepaidExpenseService.findPrepaidExpenseByID(this.prepaidExpense.id).subscribe(res => {
                    this.prepaidExpenseVouchers = res.body ? res.body : [];
                });
                this.checkData = true;
                if (this.prepaidExpense.id) {
                    if (this.prepaidExpense && this.prepaidExpense.id) {
                        this.chiPhiTraTruocService.countByPrepaidExpenseID(this.prepaidExpense.id).subscribe(res => {
                            // nếu chưa phân bổ hoặc đã ngừng phân bổ thì mới được xóa
                            this.countPB = res.body;
                        });
                    }
                }
                this.copy();
            } else {
                this.checkData = false;
                this.prepaidExpenseVouchers = [];
                this.prepaidExpenseAllocations = [];
                this.prepaidExpense = {};
                this.prepaidExpense.typeLedger = 2;
            }
        });
        if (!this.isEditUrl) {
            this.statusVoucher = 0;
            this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
        }
        this.getExpenseItemService();
        this.getPrepaidExpenseCode();
        this.getData();
        this.copy();
    }

    closeForm() {
        event.preventDefault();
        this.isCloseAll = true;
        if (this.prepaidExpenseCopy) {
            if (
                !this.utilService.isEquivalent(this.prepaidExpense, this.prepaidExpenseCopy) ||
                !this.utilService.isEquivalentArray(this.prepaidExpenseAllocations, this.prepaidExpenseAllocationsCopy)
            ) {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(this.modalContent, { backdrop: 'static' });
                return;
            } else {
                this.closeAll();
            }
        } else {
            this.copy();
            this.closeAll();
        }
    }

    saveAndLoad() {
        if (!this.isCreateUrl) {
            this.edit();
            this.isSaving = false;
            this.copy();
            this.router.navigate(['chi-phi-tra-truoc/new']);
            this.statusVoucher = 0;
        }
    }

    edit() {
        if (!this.isCreateUrl && this.isRecord) {
            this.isCreateUrl = !this.isCreateUrl;
            this.statusVoucher = 0;
            this.copy();
            this.disableAddButton = false;
            this.disableEditButton = true;
            this.isReadOnly = false;
        }
    }

    new() {
        if (!this.isCreateUrl) {
            this.router.navigate(['chi-phi-tra-truoc/new']);
        }
    }

    // check : Có lưu và thêm hay không
    saveAfter(check?: Boolean) {
        this.isCloseAll = true;
        if (this.prepaidExpense && this.prepaidExpense.id) {
            this.prepaidExpense.isActive = !this.isActive;
        } else {
            this.prepaidExpense.isActive = true;
        }
        // this.prepaidExpense.active = !this.isActive;
        for (let i = 0; i < this.prepaidExpenseAllocations.length; i++) {
            this.prepaidExpenseAllocations[i].orderPriority = i;
        }
        this.prepaidExpense.prepaidExpenseAllocation = this.prepaidExpenseAllocations;
        this.prepaidExpense.prepaidExpenseVouchers = this.prepaidExpenseVouchersSave;
        if (this.prepaidExpense.id) {
            this.chiPhiTraTruocService.update(this.prepaidExpense).subscribe(
                res => {
                    this.toastr.success(
                        this.translate.instant('ebwebApp.accountingObjectGroup.insertDataSuccess'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    this.prepaidExpense = res.body;
                    this.prepaidExpenseAllocations = this.prepaidExpense.prepaidExpenseAllocation;
                    this.prepaidExpenseVouchers = this.prepaidExpense.prepaidExpenseVouchers;
                    if (check) {
                        this.isActive = true;
                        this.checkData = false;
                        this.prepaidExpense = {};
                        this.prepaidExpenseAllocations = [];
                        this.router.navigate(['/chi-phi-tra-truoc/new']);
                        this.ngOnInit();
                    } else {
                        this.closeAll();
                    }
                },
                res => {
                    this.copy();
                    if (res.error.title === 'prepaidExpense') {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.prepaidExpense.error.' + res.error.params),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                        return;
                    }
                    this.toastr.error(
                        this.translate.instant('ebwebApp.pPDiscountReturn.error.unUpdate'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                }
            );
        } else {
            this.chiPhiTraTruocService.create(this.prepaidExpense).subscribe(
                res => {
                    this.toastr.success(
                        this.translate.instant('ebwebApp.accountingObjectGroup.insertDataSuccess'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    this.prepaidExpense = res.body;
                    this.prepaidExpenseAllocations = this.prepaidExpense.prepaidExpenseAllocation;
                    this.prepaidExpenseVouchers = this.prepaidExpense.prepaidExpenseVouchers;
                    if (check) {
                        this.isActive = true;
                        this.checkData = false;
                        this.prepaidExpense = {};
                        this.prepaidExpenseAllocations = [];
                        this.router.navigate(['/chi-phi-tra-truoc/new']);
                        this.ngOnInit();
                    } else {
                        this.closeAll();
                    }
                },
                res => {
                    this.copy();
                    if (res.error.title === 'prepaidExpense') {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.prepaidExpense.error.' + res.error.params),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                        return;
                    }
                    this.toastr.error(
                        this.translate.instant('ebwebApp.pPDiscountReturn.error.unInsertSuccess'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                }
            );
        }
    }

    addNewRow(eventData: any, select: number, checkControl: boolean) {
        this.prepaidExpenseAllocations = this.prepaidExpenseAllocations ? this.prepaidExpenseAllocations : [];
        const prepaidExpenseAllocationItem: IPrepaidExpenseAllocation = new PrepaidExpenseAllocation();
        prepaidExpenseAllocationItem.allocationRate = 100;
        this.prepaidExpenseAllocations.push(prepaidExpenseAllocationItem);
        if (checkControl) {
            const lst = this.listIDInputDeatilTax;
            const col = this.indexFocusDetailCol;
            const row = this.prepaidExpenseAllocations.length - 1;
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(lst[col] + row);
                if (element) {
                    element.focus();
                }
            }, 0);
        }
        const nameTag: string = event.srcElement.id;
        const index: number = this.prepaidExpenseAllocations.length - 1;
        const nameTagIndex: string = nameTag + String(index);
        setTimeout(function() {
            const element: HTMLElement = document.getElementById(nameTagIndex);
            if (element) {
                element.focus();
            }
        }, 0);
    }

    keyPress(value: number, select: number) {
        if (select === 0) {
            this.prepaidExpenseAllocations.splice(value, 1);
        } else if (select === 1) {
            this.prepaidExpenseAllocations.splice(value, 1);
        }
        const lst = this.listIDInputDeatilTax;
        const col = this.indexFocusDetailCol;
        const row = this.prepaidExpenseAllocations.length - 1;
        setTimeout(function() {
            const element: HTMLElement = document.getElementById(lst[col] + row);
            if (element) {
                element.focus();
            }
        }, 0);
    }
    // end of lui tien
    private onError(errorMessage: string) {
        this.toastr.error(this.translate.instant('ebwebApp.mBCreditCard.error'), this.translate.instant('ebwebApp.mBCreditCard.message'));
    }

    onRightClick($event, data, selectedData, isNew, isDelete, isCopy) {
        event.preventDefault();
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

    deletePrepaidExpense(content) {
        if (this.prepaidExpense && this.prepaidExpense.id) {
            this.prepaidExpenseService.delete(this.prepaidExpense.id).subscribe(
                res => {
                    this.toastr.success(this.translate.instant('ebwebApp.pPDiscountReturn.delete.success'));
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                },
                res => {
                    this.toastr.error(this.translate.instant('ebwebApp.pPDiscountReturn.delete.error'));
                }
            );
        }
        this.closeForm();
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    saveAndCreate() {}

    public beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            this.modalRef = this.exportPrepaidExpenseVoucherModalService.open(this.prepaidExpenseVouchers);
        }
        // if ($event.nextId === 'currentBook') {
        //     this.openModalCurrentBook();
        // }
    }

    closeAll() {
        if (sessionStorage.getItem('dataSourceGOtherVoucher')) {
            this.router.navigate(['/chi-phi-tra-truoc', 'hasSearch', '1']);
        } else {
            this.router.navigate(['/chi-phi-tra-truoc']);
        }
    }

    saveContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.saveAfter();
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.closeAll();
    }

    copy() {
        if (this.prepaidExpense) {
            this.prepaidExpenseCopy = Object.assign({}, this.prepaidExpense);
        } else {
            this.prepaidExpenseCopy = {};
        }
        if (this.prepaidExpenseAllocations && this.prepaidExpenseAllocations.length > 0) {
            this.prepaidExpenseAllocationsCopy = this.prepaidExpenseAllocations.map(object => ({ ...object }));
        } else {
            this.prepaidExpenseAllocationsCopy = [];
        }
    }

    noVoucherExist() {
        this.toastr.error(
            this.translate.instant('global.data.noVocherAlreadyExist'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
        this.isCreateUrl = !this.isCreateUrl;
    }

    exit() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    /*
* hàm ss du lieu 2 object và 2 mảng object
* return true: neu tat ca giong nhau
* return fale: neu 1 trong cac object ko giong nhau
* */
    canDeactive(): boolean {
        if (this.isCloseAll) {
            return true;
        } else {
            return (
                this.utilService.isEquivalent(this.prepaidExpense, this.prepaidExpenseCopy) &&
                this.utilService.isEquivalentArray(this.prepaidExpenseAllocations, this.prepaidExpenseAllocationsCopy)
            );
        }
    }

    keyDownAddRow(value: number, select: number) {
        if (select === 0) {
            if (value !== null && value !== undefined) {
                const ob: PrepaidExpenseAllocation = Object.assign({}, this.prepaidExpenseAllocations[value]);
                ob.id = undefined;
                this.prepaidExpenseAllocations.push(ob);
            } else {
                this.prepaidExpenseAllocations.push({});
            }
        }
        const lst = this.listIDInputDeatilTax;
        const col = this.indexFocusDetailCol;
        const row = this.prepaidExpenseAllocations.length - 1;
        setTimeout(function() {
            const element: HTMLElement = document.getElementById(lst[col] + row);
            if (element) {
                element.focus();
            }
        }, 0);

        this.prepaidExpenseVouchers[this.prepaidExpenseVouchers.length - 1].allocationRate = 100;
    }

    getData() {
        if (!this.checkData) {
            this.prepaidExpense.typeExpense = 1;
            this.prepaidExpense.typeLedger = 2;
        }
        this.getExpenseItemService();
        this.getCostAccounts();
    }

    getExpenseItemService() {
        if (this.prepaidExpense && this.prepaidExpense.id) {
            this.expenseItemService.getAllExpenseItems().subscribe(ref => {
                this.expenseItemList = ref.body;
                this.copy();
            });
        }
        this.expenseItemService.getExpenseItems().subscribe(ref => {
            if (!this.prepaidExpense || !this.prepaidExpense.id) {
                this.expenseItemList = ref.body;
            }
            this.expenseItemList2 = ref.body;
            this.copy();
        });
    }

    // getPrepaidExpenseCode() {
    //     this.chiPhiTraTruocService.getPrepaidExpenseCode().subscribe(ref => {
    //         this.prepaidExpenseCodeList = ref.body;
    //         if (this.checkData) {
    //             this.prepaidExpenseAllocations.forEach(item => {
    //                 item.prepaidExpenseCodeItem = this.prepaidExpenseCodeList.find(x => x.id === item.allocationObjectID);
    //             });
    //             this.copy();
    //         }
    //     });
    // }
    getPrepaidExpenseCode() {
        if (this.prepaidExpense && this.prepaidExpense.id) {
            this.chiPhiTraTruocService.getPrepaidExpenseCodeCanActive().subscribe(ref => {
                this.prepaidExpenseCodeList = ref.body;
                this.prepaidExpenseAllocations.forEach(item => {
                    item.prepaidExpenseCodeItem = this.prepaidExpenseCodeList.find(x => x.id === item.allocationObjectID);
                });
            });
        }
        this.chiPhiTraTruocService.getPrepaidExpenseCode().subscribe(ref => {
            if (!this.prepaidExpense || !this.prepaidExpense.id) {
                this.prepaidExpenseCodeList = ref.body;
            }
            this.prepaidExpenseCodeList2 = ref.body;
        });
    }
    getCostAccounts() {
        this.chiPhiTraTruocService.getCostAccounts().subscribe(ref => {
            this.costAccounts = ref.body;
            this.copy();
        });
    }

    changePrepaidExpenseCodeList(detail: IPrepaidExpenseAllocation) {
        if (detail.prepaidExpenseCodeItem) {
            detail.allocationObjectID = detail.prepaidExpenseCodeItem.id;
            detail.allocationObjectName = detail.prepaidExpenseCodeItem.name;
            detail.allocationObjectType = detail.prepaidExpenseCodeItem.type;
        } else {
            detail.allocationObjectID = null;
            detail.allocationObjectName = null;
            detail.allocationObjectType = null;
        }
    }

    changeAmount(prepaidExpense: IPrepaidExpense) {
        if (prepaidExpense.allocationTime && prepaidExpense.amount) {
            prepaidExpense.allocatedAmount = prepaidExpense.amount / prepaidExpense.allocationTime;
            if (prepaidExpense.allocatedPeriod && !prepaidExpense.typeExpense) {
                prepaidExpense.allocationAmount = prepaidExpense.allocatedAmount * prepaidExpense.allocatedPeriod;
            } else {
                prepaidExpense.allocationAmount = 0;
            }
        } else {
            prepaidExpense.allocationAmount = 0;
            prepaidExpense.allocatedAmount = 0;
        }
    }

    getDataView() {
        this.eventManager.subscribe('selectPrepaidExpenseVoucher', ref => {
            this.prepaidExpenseVouchers = [];
            this.prepaidExpenseVouchers = ref.content;
            this.convertPrepaidExpenseVouchers();
        });
        this.eventManager.subscribe('afterAddNewRow', ref => {
            const lst = this.listIDInputDeatilTax;
            const col = this.indexFocusDetailCol;
            const row = this.indexFocusDetailRow + 1;
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(lst[col] + row);
                if (element) {
                    element.focus();
                }
            }, 0);

            this.prepaidExpenseVouchers[this.prepaidExpenseVouchers.length - 1].allocationRate = 100;
        });

        this.eventManager.subscribe('afterDeleteRow', () => {
            const lst = this.listIDInputDeatilTax;
            const col = this.indexFocusDetailCol;
            const row = this.indexFocusDetailRow - 1;
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(lst[col] + row);
                if (element) {
                    element.focus();
                }
            }, 0);
        });
        this.eventManager.subscribe('afterCopyRow', () => {
            const lst = this.listIDInputDeatilTax;
            const col = this.indexFocusDetailCol;
            const row = this.indexFocusDetailRow + 1;
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(lst[col] + row);
                if (element) {
                    element.focus();
                }
            }, 0);
        });

        this.eventManager.subscribe('selectPrepaidExpenseCurrentBook', response => {
            const type = this.prepaidExpense.typeLedger;
            this.prepaidExpense = response.content;
            this.prepaidExpense.id = null;
            this.prepaidExpense.typeLedger = type;
            this.prepaidExpenseAllocations = response.content.prepaidExpenseAllocation;
            this.prepaidExpenseAllocations.forEach(item => {
                item.prepaidExpenseCodeItem = this.prepaidExpenseCodeList.find(x => x.id === item.allocationObjectID);
                item.id = null;
                item.prepaidExpenseID = null;
            });
            this.prepaidExpenseService.findPrepaidExpenseByID(this.prepaidExpense.id).subscribe(res => {
                this.prepaidExpenseVouchers = res.body;
                this.prepaidExpenseVouchers.forEach(x => {
                    x.prepaidExpenseID = null;
                });
            });
        });
        this.copy();
    }

    private selectPrepaidExpenseVoucher() {}

    changeDate(prepaidExpense: IPrepaidExpense) {
        if (this.prepaidExpense.typeExpense === 0 && moment(prepaidExpense.date) > moment(this.startUsing)) {
            this.toastr.error(this.translate.instant('ebwebApp.prepaidExpense.error.date'));
            return false;
        } else {
            return true;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChiPhiTRaTruoc_Them, ROLE.ChiPhiTRaTruoc_Sua])
    save(check?: Boolean) {
        event.preventDefault();
        // if (this.isEditAu) {
        this.isCloseAll = true;
        // ngừng phân bổ
        if (this.prepaidExpense.id) {
            if (this.countPB > 0) {
                if (this.checkIsActive !== this.isActive) {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    if (
                        this.utilService.isEquivalent(this.prepaidExpense, this.prepaidExpenseCopy) &&
                        this.utilService.isEquivalentArray(this.prepaidExpenseAllocations, this.prepaidExpenseAllocationsCopy)
                    ) {
                        this.updateStatusAllocation();
                        return;
                    } else {
                        this.modalRef = this.modalService.open(this.modalCheckPB, { backdrop: 'static' });
                        return;
                    }
                } else if (
                    !this.utilService.isEquivalent(this.prepaidExpense, this.prepaidExpenseCopy) ||
                    !this.utilService.isEquivalentArray(this.prepaidExpenseAllocations, this.prepaidExpenseAllocationsCopy)
                ) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.prepaidExpense.update.check'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    return;
                }
            }
        }
        if (this.prepaidExpenseAllocations && this.prepaidExpenseAllocations.length > 0) {
            // bắt lỗi tk chi phí
            const costAccountsDetail = this.prepaidExpenseAllocations.filter(item => !item.costAccount);
            if (costAccountsDetail && costAccountsDetail.length) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.pporder.error.required'),
                    this.translate.instant('ebwebApp.pPDiscountReturn.voucher')
                );
                return false;
            }
            if (!this.prepaidExpense.prepaidExpenseCode || this.prepaidExpense.prepaidExpenseCode.trim().length <= 0) {
                this.prepaidExpense.prepaidExpenseCode = '';
                this.toastr.error(
                    this.translate.instant('ebwebApp.prepaidExpense.error.nullPrepaidExpenseCode2'),
                    this.translate.instant('ebwebApp.pPDiscountReturn.voucher')
                );
                return false;
            }
            if (this.prepaidExpense.typeExpense === 0) {
                if (this.prepaidExpense.allocatedPeriod > this.prepaidExpense.allocationTime) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.prepaidExpense.allocatedPeriodCheck'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    return;
                }
            } else {
                this.prepaidExpense.allocatedPeriod = 0;
            }
            // bắt lỗi chưa nhập ngày ghi nhận
            if (!this.prepaidExpense.date) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.pporder.error.required'),
                    this.translate.instant('ebwebApp.pPDiscountReturn.voucher')
                );
                return;
            }
            // bắt lỗi chi phí phan bổ không được vựot quá 100%
            let count = 0;
            for (let j = 0; j < this.prepaidExpenseAllocations.length; j++) {
                count += this.prepaidExpenseAllocations[j].allocationRate;
            }
            if (count > 100) {
                this.toastr.error(this.translate.instant('ebwebApp.prepaidExpenseAllocation.error.allocationRate'));
                return false;
            }
            if (count < 100) {
                this.toastr.error(this.translate.instant('ebwebApp.prepaidExpenseAllocation.error.allocationRateMin'));
                return false;
            }
            // SỐ TIỀN PHÂN BỔ KHÔNG ĐƯỢC LƠN HƠN SỐ TIỀN
            if (this.prepaidExpense.allocatedAmount > this.prepaidExpense.amount) {
                this.toastr.error(this.translate.instant('ebwebApp.prepaidExpense.error.checkAmount'));
                return false;
            }
            // SỐ TIỀN đã  PHÂN BỔ KHÔNG ĐƯỢC LƠN HƠN tổng SỐ TIỀN
            if (this.prepaidExpense.allocationAmount > this.prepaidExpense.amount) {
                this.toastr.error(this.translate.instant('ebwebApp.prepaidExpense.error.checkAmountAllocation'));
                return false;
            }
            // bắt lỗi tài khoản phân bổ
            if (!this.prepaidExpense.allocationAccount) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.pporder.error.required'),
                    this.translate.instant('ebwebApp.pPDiscountReturn.voucher')
                );
                return false;
            }
            // bắt lỗi date
            if (!this.changeDate(this.prepaidExpense)) {
                return false;
            }

            // checdk toongr tiền
            if (this.prepaidExpenseVouchers && this.prepaidExpenseVouchers.length > 0) {
                let sumAmount = 0;
                for (let j = 0; j < this.prepaidExpenseVouchers.length; j++) {
                    sumAmount += this.prepaidExpenseVouchers[j].totalAmountOriginal;
                }
                if (this.prepaidExpense.amount !== sumAmount) {
                    this.checkSave = check ? true : false;
                    this.modalRef = this.modalService.open(this.modalCheckAmount, { backdrop: 'static' });
                } else {
                    this.saveAfter(check);
                }
            } else {
                if (this.prepaidExpense.amount !== 0) {
                    this.checkSave = check ? true : false;
                    this.modalRef = this.modalService.open(this.modalCheckAmount, { backdrop: 'static' });
                    return;
                } else {
                    this.saveAfter(check);
                }
            }
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.pPDiscountReturn.notPPDiscountReturn'));
            return;
        }
        // }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChiPhiTRaTruoc_Xoa])
    delete() {
        event.preventDefault();
        if (this.prepaidExpense.id) {
            if (this.prepaidExpense && this.prepaidExpense.id) {
                this.chiPhiTraTruocService.countByPrepaidExpenseID(this.prepaidExpense.id).subscribe(res => {
                    // nếu chưa phân bổ hoặc đã ngừng phân bổ thì mới được xóa
                    if (res.body > 0) {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.prepaidExpense.delete.check'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                        return;
                    } else {
                        this.modalRef = this.modalService.open(this.modalDeleteModal, { backdrop: 'static' });
                    }
                });
            }
        }
    }

    convertPrepaidExpenseVouchers() {
        if (this.prepaidExpenseVouchers && this.prepaidExpenseVouchers.length > 0) {
            this.prepaidExpenseVouchersSave = [];
            for (let j = 0; j < this.prepaidExpenseVouchers.length; j++) {
                this.prepaidExpenseVouchersSave.push({});
                this.prepaidExpenseVouchersSave[j].id = null;
                if (this.prepaidExpenseVouchers[j].refID1) {
                    this.prepaidExpenseVouchersSave[j].refID = this.prepaidExpenseVouchers[j].refID1;
                }
                if (this.prepaidExpenseVouchers[j].typeID) {
                    this.prepaidExpenseVouchersSave[j].typeID = this.prepaidExpenseVouchers[j].typeID;
                }
                if (this.prepaidExpenseVouchers[j].date) {
                    this.prepaidExpenseVouchersSave[j].date = this.prepaidExpenseVouchers[j].date
                        ? moment(this.prepaidExpenseVouchers[j].date, DATE_FORMAT_YMD)
                        : null;
                }
                if (this.prepaidExpenseVouchers[j].postedDate) {
                    this.prepaidExpenseVouchersSave[j].postedDate = this.prepaidExpenseVouchers[j].postedDate
                        ? moment(this.prepaidExpenseVouchers[j].postedDate, DATE_FORMAT_YMD)
                        : null;
                }
                // this.prepaidExpenseVouchersSave[j].refID = this.prepaidExpenseVouchers[j].refID;
                // if (this.prepaidExpenseVouchers[j].totalAmountOriginal) {
                //     this.prepaidExpenseVouchers[j].amount = this.prepaidExpenseVouchers[j].totalAmountOriginal;
                // }
                // this.prepaidExpenseVouchersSave[j].amount = this.prepaidExpenseVouchers[j].amount;
                // this.prepaidExpenseVouchersSave[j].date = this.prepaidExpenseVouchers[j].date;
                // this.prepaidExpenseVouchersSave[j].postedDate = this.prepaidExpenseVouchers[j].postedDate;
                // this.prepaidExpenseVouchersSave[j].no = this.prepaidExpenseVouchers[j].no;
                // this.prepaidExpenseVouchersSave[j].reason = this.prepaidExpenseVouchers[j].reason;
            }
        }
        console.log(this.prepaidExpenseVouchers);
    }

    openModalCurrentBook() {
        this.modalRef = this.expenseCurrentBookModalService.open(this.prepaidExpense);
    }

    changTypeledger() {
        console.log(this.prepaidExpense.typeLedger);
    }

    checkShowModalCurrentBook() {
        return Number(this.prepaidExpense.typeLedger) !== 2;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ChiPhiTRaTruoc_Them])
    saveAndNew() {
        event.preventDefault();
        this.save(true);
    }

    ngAfterViewInit(): void {
        const inputs = document.getElementsByTagName('input');
        inputs[2].focus();
    }

    addIfLastInput(i) {
        if (i === this.prepaidExpenseAllocations.length - 1) {
            this.addNewRow(event, 0, false);
        }
    }

    actionFocus(indexCol, indexRow) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
    }

    updateStatusAllocation() {
        this.chiPhiTraTruocService.updateIsActive(this.prepaidExpense.id).subscribe(res => {
            this.toastr.success(
                this.translate.instant('ebwebApp.accountingObjectGroup.insertDataSuccess'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
            this.closeAll();
            return;
        });
    }

    clickPrepaidExpenseCodeList() {
        this.prepaidExpenseCodeList = this.prepaidExpenseCodeList2;
        this.expenseItemList = this.expenseItemList2;
    }
}
