import { AfterViewChecked, AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { BankService } from 'app/danhmuc/bank';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { CostSetService } from 'app/entities/cost-set';
import { ExpenseItemService } from 'app/entities/expense-item';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { CreditCardService } from 'app/danhmuc/credit-card';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { AutoPrinciple, IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { AccountDefaultService } from 'app/danhmuc/account-default';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { EMContractService } from 'app/entities/em-contract';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { BudgetItemService } from 'app/entities/budget-item';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import {
    AccountType,
    DDSo_NgoaiTe,
    DDSo_TienVND,
    DDSo_TyLe,
    REPORT,
    DDSo_TyLePBo,
    SD_SO_QUAN_TRI,
    SO_LAM_VIEC,
    TCKHAC_SDSoQuanTri,
    TypeID
} from 'app/app.constants';
import { Principal } from 'app/core';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { PhanBoChiPhiTraTruocService } from 'app/tonghop/phan_bo_chi_phi_tra_truoc/phan-bo-chi-phi-tra-truoc.service';
import { GOtherVoucherDetailExpense } from 'app/shared/model/g-other-voucher-detail-expense.model';
import { PrepaidExpenseService } from 'app/entities/prepaid-expense';
import { GOtherVoucherDetailExpenseAllocation } from 'app/shared/model/g-other-voucher-detail-expense-allocation.model';
import { ChiPhiTraTruocService } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc.service';
import { GOtherVoucherDetails } from 'app/shared/model/g-other-voucher-details.model';
import { IAccountAllList } from 'app/shared/model/account-all-list.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import * as moment from 'moment';
import { Subscription } from 'rxjs';
import { Irecord } from 'app/shared/model/record';
import { ROLE } from 'app/role.constants';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';

@Component({
    selector: 'eb-phan-bo-chi-phi-tra-truoc-update',
    templateUrl: './phan-bo-chi-phi-tra-truoc-update.component.html',
    styleUrls: ['./phan-bo-chi-phi-tra-truoc-update.component.css']
})
export class PhanBoChiPhiTraTruocUpdateComponent extends BaseComponent implements OnInit, AfterViewInit, AfterViewChecked {
    @ViewChild('test') public modalComponent: NgbModalRef;
    @ViewChild('checkAmount') public modalCheckAmount: NgbModalRef;
    @ViewChild('content') public modalContent: NgbModalRef;
    @ViewChild('deleteModal') public deleteModal: NgbModalRef;
    private _gOtherVoucher: IGOtherVoucher;
    isSaving: boolean;
    totalAmount: number;
    totalAmountOriginal: number;
    temp: Number;
    totalVatAmount: any;
    totalVatAmountOriginal: any;
    //  data storage provider
    page: number;
    itemsPerPage: number;
    pageCount: number;
    totalItems: number;
    rowNum: number;
    typeAmountOriginal = true;
    accounting: IAccountingObject[];
    options: any;
    TCKHAC_SDSoQuanTri: any;
    creditAccountList: IAccountList[];
    creditAccountListItem: IAccountList;
    debitAccountList: IAccountList[];
    debitAccountItem: IAccountList;
    vatAccountList: IAccountList[];
    iAutoPrinciple: IAutoPrinciple;
    gOtherVoucherCopy: IGOtherVoucher;
    statusVoucher: number;
    type: any;
    no: any;
    goodsServicePurchases: IGoodsServicePurchase[];
    goodsServicePurchaseID: any;
    isReadOnly: boolean;
    columnList = [{ column: AccountType.TK_CO, ppType: false }, { column: AccountType.TK_NO, ppType: false }];

    autoPrinciples: IAutoPrinciple[];
    contextMenu: ContextMenu;
    dataSession: any;
    currentAccount: any;
    gOtherVoucherDetailExpenses: GOtherVoucherDetailExpense[];
    gOtherVoucherDetailExpenseAllocations: any[];
    autoPrinciple: any;
    objectDate: any;
    prepaidExpenseCodeList: any[];
    costAccounts: any;
    gOtherVoucherDetail: GOtherVoucherDetails[];
    accountingObjects: IAccountingObject[];
    employees: IAccountingObject[];
    expenseItems: IExpenseItem[];
    organizationUnits: IOrganizationUnit[];
    costSets: ICostSet[];
    isSoTaiChinh: boolean;
    eMContracts: IEMContract[];
    statisticCodes: any;
    sysTypeLedger: any;
    isEdit: boolean;
    checkData: boolean;
    checkAllocation: any;
    modalRef: NgbModalRef;
    viewVouchersSelected: any[];
    eventSubscriber: Subscription;
    objectSearch: any;
    isCloseAll: boolean;
    gOtherVoucherDetailsCopy: GOtherVoucherDetails[];
    gOtherVoucherDetailExpenseAllocationsCopy: GOtherVoucherDetailExpenseAllocation[];
    gOtherVoucherDetailExpensesCopy: GOtherVoucherDetailExpense[];
    record_: any;
    DDSo_TyLe: any;
    DDSo_NgoaiTe: any;
    DDSo_TienVND: any;
    DDSo_TyLePB: any;
    checkSaveBoolean: Boolean;
    noCopy: any;
    REPORT = REPORT;
    sumAmount: number;
    sumRemainingAmount: number;
    sumAllocationAmount: number;
    sumAllocationAmountDetail: any;
    sumAmountDetail: number;
    sumAccountingAmountDetail: number;
    budgetItems: IBudgetItem[];
    indexFocusDetailCol: any;
    listIDInputDeatilTax: any[] = [
        'prepaidExpenseCode',
        'prepaidExpenseName',
        'amount',
        'remainingAmount',
        'allocationAmountGo',
        'prepaidExpenseID',
        'prepaidExpenseNameGo',
        'objectID',
        'allocationAmount',
        'allocationRate1',
        'amountGo',
        'costAccounts',
        'expenseItemCode',
        'description',
        'debitAccountList',
        'creditAccount',
        'amountGOtherVoucherDetail',
        'debitAccountingObject_statistical',
        'creditAccountingObject_statistical',
        'employee_statistical',
        'budgetItem_DT',
        'expenseItem_statistical',
        'organizationUnit_statistical',
        'costSet_statistical',
        'statisticsCode_statistical'
    ];
    indexFocusDetailRow: any;

    // add by namnh GOtherVoucher
    isGOtherVoucher: boolean;
    searchVoucher: ISearchVoucher;
    count: number;
    TYPE_CHUNG_TU_NGHIEP_VU_KHAC = 700;
    TYPE_KET_CHUYEN_LAI_LO = 702;
    TYPE_PHAN_BO_CHI_PHI_TRA_TRUOC = 709;
    // end add by namnh

    ROLE_Xem = ROLE.PhanBoChiPhiTRaTruoc_Xem;
    ROLE_Them = ROLE.PhanBoChiPhiTRaTruoc_Them;
    ROLE_Sua = ROLE.PhanBoChiPhiTRaTruoc_Sua;
    ROLE_Xoa = ROLE.PhanBoChiPhiTRaTruoc_Xoa;
    ROLE_GhiSo = ROLE.PhanBoChiPhiTRaTruoc_GhiSo;
    ROLE_In = ROLE.PhanBoChiPhiTRaTruoc_In;
    prepaidExpenseCodeList2: any;
    expenseItems2: IExpenseItem[];

    constructor(
        private router: Router,
        private phanBoChiPhiTraTruocService: PhanBoChiPhiTraTruocService,
        private expenseItemService: ExpenseItemService,
        private chiPhiTraTruocService: ChiPhiTraTruocService,
        private gOtherVoucherService: PhanBoChiPhiTraTruocService,
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
        private autoPrinciplellService: AutoPrincipleService,
        private prepaidExpenseService: PrepaidExpenseService
    ) {
        super();
        this.DDSo_NgoaiTe = DDSo_NgoaiTe;
        this.DDSo_TienVND = DDSo_TienVND;
        this.DDSo_TyLe = DDSo_TyLe;
        this.DDSo_TyLePB = DDSo_TyLePBo;
        this.contextMenu = new ContextMenu();
        this.dataSession = JSON.parse(sessionStorage.getItem('dataSession'));
        this.objectSearch = JSON.parse(sessionStorage.getItem('objectSearch'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
        } else {
            this.dataSession = null;
        }
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.typeAmountOriginal = this.currentAccount.currencyCode && this.currentAccount.currencyCode === 'VND';
            this.goodsServicePurchaseID = this.currentAccount.organizationUnit.goodsServicePurchaseID;
            this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
            this.TCKHAC_SDSoQuanTri = this.currentAccount.systemOption.find(x => x.code === TCKHAC_SDSoQuanTri).data;
            this.sysTypeLedger = this.isSoTaiChinh ? 0 : 1;
            this.DDSo_NgoaiTe = this.currentAccount.systemOption.find(x => x.code === DDSo_NgoaiTe && x.data).data;
            this.DDSo_TienVND = this.currentAccount.systemOption.find(x => x.code === DDSo_TienVND && x.data).data;
            this.activatedRoute.data.subscribe(data => {
                if (data.gOtherVoucher && data.gOtherVoucher.gOtherVoucher && data.gOtherVoucher.gOtherVoucher.id) {
                    this.checkData = true;
                    this.isEdit = false;
                    this.gOtherVoucher = data.gOtherVoucher.gOtherVoucher;
                    this.gOtherVoucher.date = moment(data.gOtherVoucher.gOtherVoucher.date);
                    this.gOtherVoucher.postedDate = moment(data.gOtherVoucher.gOtherVoucher.postedDate);
                    this.gOtherVoucherDetailExpenses = data.gOtherVoucher.gOtherVoucherDetailExpenses;
                    this.gOtherVoucherDetailExpenseAllocations = data.gOtherVoucher.gOtherVoucherDetailExpenseAllocations;
                    this.gOtherVoucherDetail = data.gOtherVoucher.gOtherVoucherDetails;
                    this.viewVouchersSelected = data.gOtherVoucher.refVoucherDTOS ? data.gOtherVoucher.refVoucherDTOS : [];
                    if (this.isSoTaiChinh) {
                        this.no = this.gOtherVoucher.noFBook;
                    } else if (this.sysTypeLedger === 1) {
                        this.no = this.gOtherVoucher.noMBook;
                    }
                    if (this.dataSession && this.dataSession.isEdit) {
                        this.edit();
                    }
                } else {
                    this.checkData = false;
                    this.viewVouchersSelected = [];
                    this.isEdit = true;
                    this.gOtherVoucher = {};
                    this.gOtherVoucherDetail = [];
                    this.gOtherVoucherDetailExpenseAllocations = [];
                    this.gOtherVoucherDetailExpenses = [];
                    // lấy dữ liệu khi thêm mới
                    this.getSessionData();
                }
            });
        });
    }

    ngOnInit() {
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
        this.isGOtherVoucher = false;
        this.gOtherVoucherDetailsCopy = [];
        this.gOtherVoucherDetailExpenseAllocationsCopy = [];
        this.gOtherVoucherDetailExpensesCopy = [];
        this.afterDeleteOrAddRow();
        this.registerRef();
        this.isSaving = false;
        this.getData();
        this.getAccount();
        this.sumAllList();
        // add by namnh
        if (window.location.href.includes('/from-g-other-voucher')) {
            this.isGOtherVoucher = true;
            this.searchVoucher = JSON.parse(sessionStorage.getItem('dataSearchGOtherVoucher'));
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
        }
        // end add by namnh
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
        });
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = true;
            this.registerComboboxSave(response);
        });
    }

    get gOtherVoucher() {
        return this._gOtherVoucher;
    }

    set gOtherVoucher(gOtherVoucher: IGOtherVoucher) {
        this._gOtherVoucher = gOtherVoucher;
    }

    getData() {
        // diễn giải
        this.autoPrinciplellService.getAllAutoPrinciples().subscribe((res: HttpResponse<IAutoPrinciple[]>) => {
            this.autoPrinciples = res.body;
            const gOtherVoucherReason: IAutoPrinciple = new AutoPrinciple();
            if (this.gOtherVoucher.id) {
                gOtherVoucherReason.autoPrincipleName = this.gOtherVoucher.reason;
                if (this.autoPrinciples && !this.autoPrinciples.some(x => x.autoPrincipleName === this.gOtherVoucher.reason)) {
                    this.autoPrinciples.push(gOtherVoucherReason);
                }
            } else {
                if (this.objectDate) {
                    this.translate.get(['ebwebApp.gOtherVoucher.reasonTitle']).subscribe((ref: any) => {
                        this.gOtherVoucher.reason = this.translate.instant('ebwebApp.gOtherVoucher.reasonTitle', {
                            month: this.objectDate.month,
                            year: this.objectDate.year
                        });
                        gOtherVoucherReason.autoPrincipleName = this.gOtherVoucher.reason;
                        if (this.autoPrinciples && !this.autoPrinciples.some(x => x.autoPrincipleName === this.gOtherVoucher.reason)) {
                            this.autoPrinciples.push(gOtherVoucherReason);
                        }
                    });
                }
            }
        });
        // khoản mục chi phi
        this.getExpenseItemService();
        // tk chi phi
        this.getCostAccounts();
        // đối tượng phân bổ
        this.getPrepaidExpenseCode();
        // đối tượng nợ, đối tượng có, nhân viên
        this.getObjectAcount();
        // lây mục thu chi
        this.getBudgetItem();
        // phong ban
        this.getOrganizationUnit();
        // dt tap hop chi phi
        this.getCostSet();
        // hợp đồng
        this.getEMContractService();
        // mã thống kê
        this.getStatisticsCode();
        // số chứng từ
        this.getNoBook();
    }

    canDeactive() {
        if (this.isCloseAll) {
            return true;
        } else {
            return (
                this.utilService.isEquivalent(this.gOtherVoucher, this.gOtherVoucherCopy) &&
                this.utilService.isEquivalentArray(this.gOtherVoucherDetail, this.gOtherVoucherDetailsCopy) &&
                this.utilService.isEquivalentArray(
                    this.gOtherVoucherDetailExpenseAllocations,
                    this.gOtherVoucherDetailExpenseAllocationsCopy
                ) &&
                this.utilService.isEquivalentArray(this.gOtherVoucherDetailExpenses, this.gOtherVoucherDetailExpensesCopy)
            );
        }
    }

    addNewRow(eventData: any, select: number) {
        if (select === 0) {
            this.gOtherVoucherDetailExpenseAllocations.push(Object.assign({}, {}));
            this.gOtherVoucherDetail.push(Object.assign({}, {}));
            this.gOtherVoucherDetailExpenseAllocations[this.gOtherVoucherDetailExpenseAllocations.length - 1].id = null;
            this.gOtherVoucherDetail[this.gOtherVoucherDetail.length - 1].id = null;
            this.gOtherVoucherDetail[this.gOtherVoucherDetail.length - 1].description = this.gOtherVoucher.reason;
            this.changeAllocationAmount();

            const nameTag: string = event.srcElement.id;
            const index: number = this.gOtherVoucherDetailExpenses.length - 1;
            const nameTagIndex: string = nameTag + String(index);
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(nameTagIndex);
                if (element) {
                    element.focus();
                }
            }, 0);
        }
    }

    onRightClick($event, data, selectedData, isNew, isDelete, isShow, checkAllocation, isCopy) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = isShow;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.isCopy = isCopy;
        this.contextMenu.selectedData = selectedData;
        this.checkAllocation = checkAllocation;
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    keyDownAddRow(checkCopy: boolean, index) {
        index = index;
        if (checkCopy) {
            this.gOtherVoucherDetailExpenseAllocations.splice(
                index,
                0,
                Object.assign({}, this.gOtherVoucherDetailExpenseAllocations[index])
            );
            this.gOtherVoucherDetail.splice(index, 0, Object.assign({}, this.gOtherVoucherDetail[index]));
            this.gOtherVoucherDetailExpenseAllocations[index].id = null;
            this.gOtherVoucherDetail[index].id = null;
        } else {
            this.gOtherVoucherDetailExpenseAllocations.splice(index, 0, Object.assign({}, {}));
            this.gOtherVoucherDetail.splice(index, 0, Object.assign({}, {}));
            this.gOtherVoucherDetailExpenseAllocations[index].id = null;
            this.gOtherVoucherDetail[index].id = null;
            // this.gOtherVoucherDetailExpenseAllocations.push({});
            // this.gOtherVoucherDetail.push({});
        }
        const lst = this.listIDInputDeatilTax;
        const col = this.indexFocusDetailCol;
        const row = this.indexFocusDetailRow + 1;
        setTimeout(function() {
            const element: HTMLElement = document.getElementById(lst[col] + row);
            if (element) {
                element.focus();
            }
        }, 0);
        this.changeAllocationAmount();
    }

    keyPress(index: number, checkAllocation) {
        if (index !== null && index >= 0) {
            if (checkAllocation === 2) {
                const idByIndex = this.gOtherVoucherDetailExpenses[index].prepaidExpenseID;
                this.gOtherVoucherDetailExpenses.splice(index, 1);
                for (let i = 0; i < this.gOtherVoucherDetailExpenseAllocations.length; i++) {
                    if (idByIndex === this.gOtherVoucherDetailExpenseAllocations[i].prepaidExpenseID) {
                        this.gOtherVoucherDetailExpenseAllocations.splice(i, 1);
                        this.gOtherVoucherDetail.splice(i, 1);
                        i--;
                    }
                }
            } else {
                this.gOtherVoucherDetail.splice(index, 1);
                this.gOtherVoucherDetailExpenseAllocations.splice(index, 1);
            }
        }
        const lst = this.listIDInputDeatilTax;
        const col = this.indexFocusDetailCol;
        const row = this.indexFocusDetailRow - 1;
        setTimeout(function() {
            const element: HTMLElement = document.getElementById(lst[col] + row);
            if (element) {
                element.focus();
            }
        }, 0);
        this.changeAllocationAmount();
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    new() {
        this.isEdit = false;
    }

    copy() {
        this.gOtherVoucherCopy = Object.assign({}, this.gOtherVoucher);
        this.noCopy = this.no;
        if (this.gOtherVoucherDetail) {
            this.gOtherVoucherDetailsCopy = this.gOtherVoucherDetail.map(object => ({ ...object }));
        }
        if (this.gOtherVoucherDetailExpenseAllocations) {
            this.gOtherVoucherDetailExpenseAllocationsCopy = this.gOtherVoucherDetailExpenseAllocations.map(object => ({ ...object }));
        }
        if (this.gOtherVoucherDetailExpenses) {
            this.gOtherVoucherDetailExpensesCopy = this.gOtherVoucherDetailExpenses.map(object => ({ ...object }));
        }
    }

    getPrepaidExpenseAllocation() {
        if (this.objectDate) {
            this.translate.get(['ebwebApp.gOtherVoucher.reasonTitle']).subscribe((ref: any) => {
                this.gOtherVoucher.reason = this.translate.instant('ebwebApp.gOtherVoucher.reasonTitle', {
                    month: this.objectDate.month,
                    year: this.objectDate.year
                });
                const gOtherVoucherReason: IAutoPrinciple = new AutoPrinciple();
                gOtherVoucherReason.autoPrincipleName = this.gOtherVoucher.reason;
                if (this.autoPrinciples && !this.autoPrinciples.some(x => x.autoPrincipleName === this.gOtherVoucher.reason)) {
                    this.autoPrinciples.push(gOtherVoucherReason);
                }
            });
            this.gOtherVoucher.date = moment(new Date(this.objectDate.year, this.objectDate.month, 0));
            this.gOtherVoucher.postedDate = moment(new Date(this.objectDate.year, this.objectDate.month, 0));
            this.prepaidExpenseService
                .getPrepaidExpenseAllocation({
                    month: this.objectDate.month,
                    year: this.objectDate.year
                })
                .subscribe(res => {
                    this.gOtherVoucherDetailExpenses = res.body.prepaidExpenseConvertDTOS;

                    this.gOtherVoucherDetailExpenseAllocations = [];
                    this.gOtherVoucherDetail = [];
                    for (let i = 0; i < res.body.prepaidExpenseAllocations.length; i++) {
                        this.gOtherVoucherDetailExpenseAllocations.push({});
                        this.gOtherVoucherDetailExpenseAllocations[i].prepaidExpenseID =
                            res.body.prepaidExpenseAllocations[i].prepaidExpenseID;
                        this.gOtherVoucherDetailExpenseAllocations[i].prepaidExpenseCode =
                            res.body.prepaidExpenseAllocations[i].prepaidExpenseCode;
                        this.gOtherVoucherDetailExpenseAllocations[i].prepaidExpenseName =
                            res.body.prepaidExpenseAllocations[i].prepaidExpenseName;
                        this.gOtherVoucherDetailExpenseAllocations[i].objectID = res.body.prepaidExpenseAllocations[i].allocationObjectID;
                        this.gOtherVoucherDetailExpenseAllocations[i].allocationAmount =
                            res.body.prepaidExpenseAllocations[i].allocationAmountGo;
                        this.gOtherVoucherDetailExpenseAllocations[i].allocationRate = res.body.prepaidExpenseAllocations[i].allocationRate;
                        this.gOtherVoucherDetailExpenseAllocations[i].amount =
                            this.gOtherVoucherDetailExpenseAllocations[i].allocationAmount *
                            res.body.prepaidExpenseAllocations[i].allocationRate /
                            100;
                        this.gOtherVoucherDetailExpenseAllocations[i].costAccount = res.body.prepaidExpenseAllocations[i].costAccount;
                        this.gOtherVoucherDetailExpenseAllocations[i].expenseItemID = res.body.prepaidExpenseAllocations[i].expenseItemID;
                        // lấy CHI TIẾT HẠCH TOÁN
                        this.gOtherVoucherDetail.push({});
                        if (res.body.prepaidExpenseAllocations[i].reason) {
                            this.gOtherVoucherDetail[i].description = res.body.prepaidExpenseAllocations[i].reason;
                        } else {
                            this.gOtherVoucherDetail[i].description = this.gOtherVoucher.reason;
                        }
                        this.gOtherVoucherDetail[i].debitAccount = res.body.prepaidExpenseAllocations[i].costAccount;
                        this.gOtherVoucherDetail[i].creditAccount = res.body.prepaidExpenseAllocations[i].allocationAccount;
                        this.gOtherVoucherDetail[i].amount = this.gOtherVoucherDetailExpenseAllocations[i].amount;
                        this.gOtherVoucherDetail[i].amountOriginal = this.gOtherVoucherDetailExpenseAllocations[i].amount;
                        this.gOtherVoucherDetail[i].expenseItemID = res.body.prepaidExpenseAllocations[i].expenseItemID;
                    }
                    this.sumAllList();
                });
        }
        this.changeAllocationAmount();
    }

    getSessionData() {
        this.objectDate = JSON.parse(sessionStorage.getItem('objectDate')) ? JSON.parse(sessionStorage.getItem('objectDate')) : null;
        // sessionStorage.removeItem('objectDate');
        // lấy chi tiết chi phí
        this.getPrepaidExpenseAllocation();
    }

    // getPrepaidExpenseCode() {
    //     if (this.gOtherVoucher && this.gOtherVoucher.id) {
    //         this.chiPhiTraTruocService.getPrepaidExpenseCodeCanActive().subscribe(ref => {
    //             this.prepaidExpenseCodeList = ref.body;
    //         });
    //     }
    //     this.chiPhiTraTruocService.getPrepaidExpenseCode().subscribe(ref => {
    //         if (!this.gOtherVoucher && !this.gOtherVoucher.id) {
    //             this.prepaidExpenseCodeList = ref.body;
    //         }
    //         this.prepaidExpenseCodeList2 = ref.body;
    //     });
    // }
    getPrepaidExpenseCode() {
        if (this.gOtherVoucher && this.gOtherVoucher.id) {
            this.chiPhiTraTruocService.getPrepaidExpenseCodeCanActive().subscribe(ref => {
                this.prepaidExpenseCodeList = ref.body;
            });
        }
        this.chiPhiTraTruocService.getPrepaidExpenseCode().subscribe(ref => {
            if (!this.gOtherVoucher || !this.gOtherVoucher.id) {
                this.prepaidExpenseCodeList = ref.body;
            }
            this.prepaidExpenseCodeList2 = ref.body;
        });
    }

    getExpenseItemService() {
        if (this.gOtherVoucher && this.gOtherVoucher.id) {
            this.expenseItemsService.getAllExpenseItems().subscribe(res => {
                this.expenseItems = res.body;
            });
        }
        this.expenseItemService.getExpenseItems().subscribe(ref => {
            if (!this.gOtherVoucher || !this.gOtherVoucher.id) {
                this.expenseItems = ref.body;
            }
            this.expenseItems2 = ref.body;
            this.copy();
        });
    }

    getAccount() {
        this.accountListService.getAccountTypeFromGOV().subscribe(res => {
            this.creditAccountList = res.body;
            this.debitAccountList = res.body;
            this.copy();
        });
    }

    getCostAccounts() {
        this.chiPhiTraTruocService.getCostAccounts().subscribe(ref => {
            this.costAccounts = ref.body;
            this.copy();
        });
    }

    getObjectAcount() {
        this.accService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accounting = res.body;
            this.accountingObjects = res.body
                .filter(n => n.isActive)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            this.employees = res.body.filter(e => e.isEmployee && e.isActive);
        });
    }

    getBudgetItem() {
        this.budgetItemService.getBudgetItems().subscribe((res: HttpResponse<IBudgetItem[]>) => {
            this.budgetItems = res.body;
        });
    }

    getOrganizationUnit() {
        this.organizationUnitService.getOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body;
        });
    }

    getCostSet() {
        this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body;
        });
    }

    getEMContractService() {
        this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
            this.eMContracts = res.body;
        });
    }

    getStatisticsCode() {
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticCodes = res.body;
        });
    }

    changeSelectReason(autoPrinciple: any) {
        if (this.gOtherVoucher.reason) {
            for (let i = 0; i < this.gOtherVoucherDetail.length; i++) {
                this.gOtherVoucherDetail[i].description = this.gOtherVoucher.reason;
            }
        }
    }

    changeDate() {
        if (this.gOtherVoucher.date) {
            this.gOtherVoucher.postedDate = this.gOtherVoucher.date;
        }
    }

    changeReason(reason: string) {
        if (reason) {
            for (let i = 0; i < this.gOtherVoucherDetail.length; i++) {
                this.gOtherVoucherDetail[i].description = reason;
            }
        }
    }

    getNoBook() {
        this.utilService
            .getGenCodeVoucher({
                typeGroupID: 70, // typeGroupID loại chứng từ
                companyID: '', // ID công ty
                branchID: '', // ID chi nhánh
                displayOnBook: this.sysTypeLedger // 0 - sổ tài chính, 1 - sổ quản trị
            })
            .subscribe((res: HttpResponse<string>) => {
                // this.mCReceipt.noFBook = (res.body.toString());
                if (!this.checkData) {
                    this.no = res.body;
                    if (this.isSoTaiChinh) {
                        this.gOtherVoucher.noFBook = this.no;
                    } else {
                        this.gOtherVoucher.noMBook = this.no;
                    }
                }
                this.copy();
            });
    }

    saveAfter(check?: Boolean) {
        for (let i = 0; i < this.gOtherVoucherDetailExpenseAllocations.length; i++) {
            this.gOtherVoucherDetailExpenseAllocations[i].orderPriority = i;
            this.gOtherVoucherDetail[i].orderPriority = i;
        }

        const data = {
            gOtherVoucher: this.gOtherVoucher,
            gOtherVoucherDetailExpenses: this.gOtherVoucherDetailExpenses,
            gOtherVoucherDetailExpenseAllocations: this.gOtherVoucherDetailExpenseAllocations,
            gOtherVoucherDetails: this.gOtherVoucherDetail,
            viewVouchers: this.viewVouchersSelected
        };
        if (this.isSoTaiChinh) {
            this.gOtherVoucher.noFBook = this.no;
        } else {
            this.gOtherVoucher.noMBook = this.no;
        }
        if (this.gOtherVoucher.id) {
            this.phanBoChiPhiTraTruocService.updatePB({ data }).subscribe(
                res => {
                    this.copy();
                    console.log(res.body);
                    this.isCloseAll = true;
                    this.gOtherVoucher.recorded = res.body.record;
                    this.toastr.success(
                        this.translate.instant('ebwebApp.mBDeposit.editSuccess'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    if (check) {
                        this.isCloseAll = true;
                        this.isEdit = false;
                        this.addNew(event);
                    } else {
                        this.isEdit = false;
                        // this.router.navigate(['./phan-bo-chi-phi-tra-truoc', res.body.gOtherVoucher.id, 'edit']);
                        this.ngOnInit();
                    }
                },
                res => {
                    if (res.error.errorKey === UpdateDataMessages.DUPLICATE_NO_BOOK_RS) {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.pPInvoice.error.duplicateNoBookRs'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                        return;
                    } else {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.pPDiscountReturn.error.unUpdate'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                        return;
                    }
                }
            );
        } else {
            this.phanBoChiPhiTraTruocService.createPB({ data }).subscribe(
                res => {
                    this.copy();
                    this.isCloseAll = true;
                    console.log(res.body);
                    this.gOtherVoucher.recorded = res.body.record;
                    this.toastr.success(
                        this.translate.instant('ebwebApp.mBDeposit.insertSuccess'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    if (check) {
                        this.isEdit = false;
                        this.addNew(event);
                    } else {
                        this.router.navigate(['./phan-bo-chi-phi-tra-truoc', res.body.gOtherVoucher.id, 'edit']);
                    }
                },
                res => {
                    if (res.error.entityName === UpdateDataMessages.DUPLICATE_NO_BOOK_RS) {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.pPInvoice.error.duplicateNoBookRs'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                        return;
                    } else {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.pPDiscountReturn.error.unInsertSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                        return;
                    }
                }
            );
        }
    }

    closeForm() {
        event.preventDefault();
        if (this.gOtherVoucherCopy) {
            if (
                this.no !== this.noCopy ||
                !this.utilService.isEquivalent(this.gOtherVoucher, this.gOtherVoucherCopy) ||
                !this.utilService.isEquivalent(this.gOtherVoucher, this.gOtherVoucherCopy) ||
                !this.utilService.isEquivalentArray(this.gOtherVoucherDetail, this.gOtherVoucherDetailsCopy) ||
                !this.utilService.isEquivalentArray(
                    this.gOtherVoucherDetailExpenseAllocations,
                    this.gOtherVoucherDetailExpenseAllocationsCopy
                ) ||
                !this.utilService.isEquivalentArray(this.gOtherVoucherDetailExpenses, this.gOtherVoucherDetailExpensesCopy)
            ) {
                if (!this.isEdit) {
                    this.closeAll();
                    return;
                }
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(this.modalContent, { backdrop: 'static' });
            } else {
                this.closeAll();
            }
        } else {
            // this.copy();
            this.closeAll();
        }
    }

    closeAll() {
        this.isCloseAll = true;
        if (sessionStorage.getItem('dataSourcePhanBoChiPhiTraTruoc')) {
            this.router.navigate(['/phan-bo-chi-phi-tra-truoc', 'hasSearch', '1']);
        } else if (window.location.href.includes('/from-g-other-voucher')) {
            if (JSON.parse(sessionStorage.getItem('dataSearchGOtherVoucher')) !== null) {
                this.router.navigate(['/g-other-voucher', 'hasSearch']);
            } else {
                this.router.navigate(['/g-other-voucher']);
            }
        } else {
            this.router.navigate(['/phan-bo-chi-phi-tra-truoc']);
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Them])
    addNew($event) {
        $event.preventDefault();
        if (!this.isEdit && !this.utilsService.isShowPopup) {
            this.isCloseAll = true;
            this.checkData = false;
            sessionStorage.setItem('checkNew', JSON.stringify(true));
            if (sessionStorage.getItem('dataSourcePhanBoChiPhiTraTruoc')) {
                this.router.navigate(['/phan-bo-chi-phi-tra-truoc', 'hasSearch', '1']);
            } else {
                this.router.navigate(['/phan-bo-chi-phi-tra-truoc']);
            }
        }
    }

    changeAmount(detail: any, i: number) {
        if (detail.amount) {
            detail.amountOriginal = detail.amount;
            detail.allocationRate = detail.amount / detail.allocationAmount * 100;
            detail.allocationRate = this.utilsService.round(detail.allocationRate, this.currentAccount.systemOption, 6);
            this.gOtherVoucherDetail[i].amount = detail.amount;
        }
        this.changeAllocationAmount();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Sua])
    edit() {
        event.preventDefault();
        if (
            !(this.isEdit || this.gOtherVoucher.recorded) &&
            !this.checkCloseBook(this.currentAccount, this.gOtherVoucher.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            this.prepaidExpenseCodeList = this.prepaidExpenseCodeList2;
            this.expenseItems = this.expenseItems2;
            this.isEdit = true;
            this.copy();
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_GhiSo])
    record() {
        event.preventDefault();
        if (
            !this.gOtherVoucher ||
            (!this.checkCloseBook(this.currentAccount, this.gOtherVoucher.postedDate) && !this.utilsService.isShowPopup)
        ) {
            if (!this.gOtherVoucher.recorded && !this.utilsService.isShowPopup) {
                if (this.gOtherVoucher.id) {
                    this.record_ = {};
                    this.record_.id = this.gOtherVoucher.id;
                    this.record_.typeID = TypeID.PHAN_BO_CHI_PHI_TRA_TRUOC;
                    if (!this.gOtherVoucher.recorded) {
                        this.gLService.record(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                            if (res.body.success) {
                                this.gOtherVoucher.recorded = true;
                                this.toastr.success(
                                    this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
                                    this.translate.instant('ebwebApp.mBCreditCard.message')
                                );
                            }
                        });
                    }
                }
            }
            // else {
            //     this.toastr.warning(
            //         this.translate.instant('global.data.warningRecorded'),
            //         this.translate.instant('ebwebApp.mBDeposit.message')
            //     );
            // }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_GhiSo])
    unrecord() {
        event.preventDefault();
        if (!(this.isEdit || this.checkCloseBook(this.currentAccount, this.gOtherVoucher.postedDate))) {
            if (this.gOtherVoucher.recorded) {
                if (this.gOtherVoucher.id) {
                    this.record_ = {};
                    this.record_.id = this.gOtherVoucher.id;
                    this.record_.typeID = TypeID.PHAN_BO_CHI_PHI_TRA_TRUOC;
                    this.gLService.unrecord(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                        if (res.body.success) {
                            this.gOtherVoucher.recorded = false;
                            this.toastr.success(
                                this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                                this.translate.instant('ebwebApp.mBCreditCard.message')
                            );
                        }
                    });
                }
            }
            // else {
            //     this.toastr.warning(
            //         this.translate.instant('global.data.warningUnRecorded'),
            //         this.translate.instant('ebwebApp.mBDeposit.message')
            //     );
            // }
        }
    }

    deleteAfter() {
        if (this.gOtherVoucher && this.gOtherVoucher.id) {
            this.gOtherVoucherService.deletePB(this.gOtherVoucher.id).subscribe(
                res => {
                    this.toastr.success(this.translate.instant('ebwebApp.pPDiscountReturn.delete.success'));
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    this.closeAll();
                },
                res => {
                    this.toastr.error(this.translate.instant('ebwebApp.pPDiscountReturn.delete.error'));
                }
            );
        }
        // this.previousState(content);
    }

    CTKTExportPDF(isDownload: boolean, typeReports: any) {
        if (!this.isEdit) {
            this.gOtherVoucherService
                .getCustomerReport({
                    id: this.gOtherVoucher.id,
                    typeID: TypeID.PHAN_BO_CHI_PHI_TRA_TRUOC,
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
                        const name = 'PHAN_BO_CHI_PHI_TRA_TRUOC.pdf';
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
            if (typeReports === REPORT.ChungTuKeToan) {
                this.toastr.success(
                    this.translate.instant('ebwebApp.mBDeposit.printing') +
                        this.translate.instant('ebwebApp.mBDeposit.financialPaper') +
                        '...',
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
            }
        }
    }

    afterDeleteOrAddRow() {
        // sau khi click vào nút thêm hoặc xóa
        this.eventManager.subscribe('afterAddNewRow', ref => {
            let index = 0;
            if (this.checkAllocation === 1) {
                index = this.gOtherVoucherDetailExpenseAllocations.indexOf(ref.content);
            } else if (this.checkAllocation === 0) {
                index = this.gOtherVoucherDetail.indexOf(ref.content);
            }
            index = index + 1;
            this.gOtherVoucherDetailExpenseAllocations.splice(index, 0, Object.assign({}, {}));
            this.gOtherVoucherDetail.splice(index, 0, Object.assign({}, {}));
            this.gOtherVoucherDetailExpenseAllocations[index].id = null;
            this.gOtherVoucherDetailExpenseAllocations[index].allocationRate = 100;
            this.gOtherVoucherDetail[index].id = null;
            this.gOtherVoucherDetail[index].description = this.gOtherVoucher.reason;
            this.changeAllocationAmount();
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
        this.eventManager.subscribe('afterDeleteRow', ref => {
            if (this.checkAllocation === 1) {
                this.deleteRow(this.gOtherVoucherDetailExpenseAllocations.indexOf(ref.content));
            } else if (this.checkAllocation === 0) {
                this.deleteRow(this.gOtherVoucherDetail.indexOf(ref.content));
            } else if (this.checkAllocation === 2) {
                // console.count("c:");
                for (let i = 0; i < this.gOtherVoucherDetailExpenseAllocations.length; i++) {
                    if (ref.content.prepaidExpenseID === this.gOtherVoucherDetailExpenseAllocations[i].prepaidExpenseID) {
                        this.gOtherVoucherDetailExpenseAllocations.splice(i, 1);
                        this.gOtherVoucherDetail.splice(i, 1);
                        i--;
                    }
                }
            }
            const lst = this.listIDInputDeatilTax;
            const col = this.indexFocusDetailCol;
            const row = this.indexFocusDetailRow - 1;
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(lst[col] + row);
                if (element) {
                    element.focus();
                }
            }, 0);
            // this.gOtherVoucherDetailExpenseAllocations = ref.content;
            this.changeAllocationAmount();
        });
        this.eventManager.subscribe('afterCopyRow', ref => {
            let index = 0;
            if (this.checkAllocation === 1) {
                index = this.gOtherVoucherDetailExpenseAllocations.indexOf(ref.content);
            } else if (this.checkAllocation === 0) {
                index = this.gOtherVoucherDetail.indexOf(ref.content);
            }
            this.keyDownAddRow(true, index);
        });
    }

    deleteRow(index) {
        if (index !== null && index >= 0) {
            // const countItem = this.gOtherVoucherDetailExpenseAllocations.filter(
            //     item => item.prepaidExpenseID === this.gOtherVoucherDetailExpenseAllocations[index].prepaidExpenseID
            // );
            // // nếu là dòng detail cuối cùng của mã chi phí trả trước thì sẽ xóa dòng chi phí pb , hạch toán và chi phí trả trước đó
            // if (countItem.length === 1) {
            //     for (let i = 0; i < this.gOtherVoucherDetailExpenses.length; i++) {
            //         if (this.gOtherVoucherDetailExpenses[i].prepaidExpenseID === this.gOtherVoucherDetailExpenseAllocations[index].prepaidExpenseID) {
            //             this.gOtherVoucherDetailExpenses.splice(i, 1);
            //             break;
            //         }
            //     }
            // }
            this.gOtherVoucherDetailExpenseAllocations.splice(index, 1);
            this.gOtherVoucherDetail.splice(index, 1);
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

    registerRef() {
        this.eventSubscriber = this.eventManager.subscribe('selectViewVoucher', response => {
            if (this.isEdit) {
                this.viewVouchersSelected = response.content;
                this.copy();
            }
        });
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Sua, ROLE.PhanBoChiPhiTRaTruoc_Them])
    save(check?: boolean) {
        event.preventDefault();
        if (this.isEdit) {
            if (moment(this.gOtherVoucher.postedDate) < moment(this.gOtherVoucher.date)) {
                this.toastr.error(this.translate.instant('ebwebApp.common.error.dateAndPostDate'));
                return false;
            }
            for (let i = 0; i < this.gOtherVoucherDetailExpenses.length; i++) {
                if (this.gOtherVoucherDetailExpenses[i].allocationAmount > this.gOtherVoucherDetailExpenses[i].remainingAmount) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.gOtherVoucher.error.remainingAmount'),
                        this.translate.instant('ebwebApp.mBCreditCard.message')
                    );
                    return;
                }
                this.gOtherVoucherDetailExpenses[i].orderPriority = i;
            }
            if (this.isEdit && !this.utilsService.isShowPopup) {
                this.checkSaveBoolean = check;
                if (!this.gOtherVoucherDetailExpenses || this.gOtherVoucherDetailExpenses.length <= 0) {
                    this.toastr.error(this.translate.instant('ebwebApp.pPDiscountReturn.notPPDiscountReturn'));
                    return;
                }
                if (this.checkCloseBook(this.currentAccount, this.gOtherVoucher.postedDate)) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.lockBook.error.checkForSave'),
                        this.translate.instant('ebwebApp.mCReceipt.error.error')
                    );
                    return false;
                }
                if (!this.gOtherVoucherDetailExpenseAllocations || this.gOtherVoucherDetailExpenseAllocations.length <= 0) {
                    this.toastr.error(this.translate.instant('ebwebApp.pPDiscountReturn.notPPDiscountReturn'));
                    return;
                }
                const creditAccount = this.gOtherVoucherDetail.filter(item => !item.creditAccount);
                if (creditAccount && creditAccount.length) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.pporder.error.required'),
                        this.translate.instant('ebwebApp.pPDiscountReturn.voucher')
                    );
                    return false;
                }
                const debitAccount = this.gOtherVoucherDetail.filter(item => !item.debitAccount);
                if (debitAccount && debitAccount.length) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.pporder.error.required'),
                        this.translate.instant('ebwebApp.pPDiscountReturn.voucher')
                    );
                    return false;
                }
                // check tỉ lệ phân bổ và số tiền
                const countSumTotal = new Map();
                const countAllocationRate = new Map();
                const countAllocationNameRate = new Map();
                this.gOtherVoucher.totalAmount = 0;
                this.gOtherVoucher.totalAmountOriginal = 0;
                // materialGoodListError = new Map(this.gOtherVoucherDetailExpenseAllocations.map(i => [i.prepaidExpenseID, i.prepaidExpenseCode]));
                // countAllocationNameRate = new Map(this.gOtherVoucherDetailExpenseAllocations.map(i => [i.prepaidExpenseID, i.prepaidExpenseCode]));
                this.gOtherVoucherDetailExpenses.forEach(item => {
                    countAllocationNameRate.set(item.prepaidExpenseID, item.prepaidExpenseCode);
                });
                for (let i = 0; i < this.gOtherVoucherDetailExpenseAllocations.length; i++) {
                    countAllocationRate.set(
                        this.gOtherVoucherDetailExpenseAllocations[i].prepaidExpenseID,
                        countAllocationRate.get(this.gOtherVoucherDetailExpenseAllocations[i].prepaidExpenseID)
                            ? Number(countAllocationRate.get(this.gOtherVoucherDetailExpenseAllocations[i].prepaidExpenseID)) +
                              this.gOtherVoucherDetailExpenseAllocations[i].allocationRate
                            : this.gOtherVoucherDetailExpenseAllocations[i].allocationRate
                    );
                    countSumTotal.set(
                        this.gOtherVoucherDetailExpenseAllocations[i].prepaidExpenseID,
                        countSumTotal.get(this.gOtherVoucherDetailExpenseAllocations[i].prepaidExpenseID)
                            ? Number(countSumTotal.get(this.gOtherVoucherDetailExpenseAllocations[i].prepaidExpenseID)) +
                              this.gOtherVoucherDetailExpenseAllocations[i].amount
                            : this.gOtherVoucherDetailExpenseAllocations[i].amount
                    );
                    this.gOtherVoucherDetail[i].amountOriginal = this.gOtherVoucherDetail[i].amount;
                    if (this.gOtherVoucherDetail[i].amount) {
                        this.gOtherVoucher.totalAmountOriginal += this.gOtherVoucherDetail[i].amount;
                        this.gOtherVoucher.totalAmount += this.gOtherVoucherDetail[i].amount;
                    }
                }
                let errorCountRate = '';
                for (const [key, value] of Array.from(countAllocationRate)) {
                    if (value && value > 100) {
                        errorCountRate += ' ' + countAllocationNameRate.get(key) + ',';
                    }
                }
                if (errorCountRate) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.gOtherVoucher.error.allocationRate', {
                            rate: errorCountRate.substring(1, errorCountRate.length - 1)
                        })
                    );
                    return;
                }
                errorCountRate = '';
                for (const [key, value] of Array.from(countAllocationRate)) {
                    if (value < 100) {
                        errorCountRate += ' ' + countAllocationNameRate.get(key) + ',';
                    }
                }
                if (errorCountRate) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.gOtherVoucher.error.allocationRateMin', {
                            rate: errorCountRate.substring(1, errorCountRate.length - 1)
                        })
                    );
                    return;
                }
                // if (countError > 0) {
                //
                // }
                // check số tiền
                // for (const item of Array.from(this.gOtherVoucherDetailExpenseAllocations)) {
                //     if (countSumTotal.get(item.prepaidExpenseCode) !== item.allocationAmount) {
                //         this.modalRef = this.modalService.open(this.modalCheckAmount, { backdrop: 'static' });
                //         return;
                //     }
                // }
                this.saveAfter(check);
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Them])
    saveAndNew() {
        event.preventDefault();
        if (this.isEdit) {
            this.save(true);
        }
    }

    move(direction: number) {
        if ((direction === -1 && this.rowNum === 1) || (direction === 1 && this.rowNum === this.totalItems)) {
            return;
        }
        this.isCloseAll = true;
        this.rowNum += direction;
        // goi service get by row num
        return this.gOtherVoucherService
            .findByRowNumPB({
                fromDate: this.objectSearch.fromDate || '',
                toDate: this.objectSearch.toDate || '',
                textSearch: this.objectSearch.searchValue || '',
                rowNum: this.rowNum
            })
            .subscribe(
                res => {
                    // this.ppOrder = res.body;
                    this.dataSession.rowNum = this.rowNum;
                    sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
                    sessionStorage.setItem('itemGOtherVoucher', JSON.stringify(res.body.id));
                    this.router.navigate(['./phan-bo-chi-phi-tra-truoc', res.body.id, 'edit']);
                    this.getData();
                    this.copy();
                    // this.ngOnInit();
                },
                (res: HttpErrorResponse) => {
                    // this.handleError(res);
                    this.getSessionData();
                }
            );
    }

    changeAllocationRate(detail: any, i: number) {
        if (detail.allocationRate) {
            detail.amount = detail.allocationAmount * detail.allocationRate / 100;
            this.gOtherVoucherDetail[i].amount = detail.amount;
        } else {
            detail.amount = 0;
            this.gOtherVoucherDetail[i].amount = 0;
        }
        this.changeAllocationAmount();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xoa])
    delete() {
        event.preventDefault();
        if (!(this.isEdit || this.gOtherVoucher.recorded) && !this.utilsService.isShowPopup) {
            if (this.gOtherVoucher.id) {
                this.gOtherVoucherService.getMaxMonth(this.gOtherVoucher.id).subscribe(res => {
                    if (moment(this.gOtherVoucher.postedDate).isSame(moment(res.body))) {
                        this.modalRef = this.modalService.open(this.deleteModal, { backdrop: 'static' });
                    } else {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.gOtherVoucher.error.checkDeletePB'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                        return;
                    }
                });
            }
        }
    }

    changePrepaidExpenseCode(detail: any) {
        if (detail.prepaidExpenseID) {
            const prepaidItem = this.gOtherVoucherDetailExpenses.find(i => i.prepaidExpenseID === detail.prepaidExpenseID);
            detail.prepaidExpenseName = prepaidItem.prepaidExpenseName;
            detail.allocationAmount = prepaidItem.allocationAmount;
            detail.objectID = null;
            detail.allocationRate = 100;
            detail.amount = detail.allocationAmount;
            detail.costAccount = null;
            detail.expenseItemID = null;
        }
        this.changeAllocationAmount();
    }

    changeAllocationAmount() {
        // check tỉ lệ phân bổ và số tiền
        const countSumTotal1 = new Map();
        const countAllocationRate1 = new Map();
        for (let i = 0; i < this.gOtherVoucherDetailExpenseAllocations.length; i++) {
            countAllocationRate1.set(
                this.gOtherVoucherDetailExpenseAllocations[i].prepaidExpenseID,
                countAllocationRate1.get(this.gOtherVoucherDetailExpenseAllocations[i].prepaidExpenseID)
                    ? Number(countAllocationRate1.get(this.gOtherVoucherDetailExpenseAllocations[i].prepaidExpenseID)) +
                      this.gOtherVoucherDetailExpenseAllocations[i].allocationRate
                    : this.gOtherVoucherDetailExpenseAllocations[i].allocationRate
            );
        }
        for (const item of this.gOtherVoucherDetailExpenseAllocations) {
            if (countAllocationRate1.get(item.prepaidExpenseID) !== 100) {
                item.checked = true;
            } else {
                item.checked = false;
            }
        }
    }

    ChangeAllocationAmount(detail: any) {
        if (detail.allocationAmount <= detail.remainingAmount) {
            for (let i = 0; i < this.gOtherVoucherDetailExpenseAllocations.length; i++) {
                if (this.gOtherVoucherDetailExpenseAllocations[i].prepaidExpenseID === detail.prepaidExpenseID) {
                    this.gOtherVoucherDetailExpenseAllocations[i].allocationAmount = detail.allocationAmount;
                    this.gOtherVoucherDetailExpenseAllocations[i].amount =
                        detail.allocationAmount * this.gOtherVoucherDetailExpenseAllocations[i].allocationRate / 100;
                    this.gOtherVoucherDetail[i].amount = this.gOtherVoucherDetailExpenseAllocations[i].amount;
                }
            }
            this.sumAllList();
        } else {
            this.toastr.error(
                this.translate.instant('ebwebApp.gOtherVoucher.error.remainingAmount'),
                this.translate.instant('ebwebApp.mBCreditCard.message')
            );
        }
    }

    sumAllList() {
        this.sumAmount = 0;
        this.sumRemainingAmount = 0;
        this.sumAllocationAmount = 0;
        this.sumAllocationAmountDetail = 0;
        this.sumAmountDetail = 0;
        this.sumAccountingAmountDetail = 0;
        for (let i = 0; i < this.gOtherVoucherDetailExpenses.length; i++) {
            if (this.gOtherVoucherDetailExpenses[i].amount) {
                this.sumAmount += this.gOtherVoucherDetailExpenses[i].amount;
            }
            if (this.gOtherVoucherDetailExpenses[i].allocationAmount) {
                this.sumAllocationAmount += this.gOtherVoucherDetailExpenses[i].allocationAmount;
            }
            if (this.gOtherVoucherDetailExpenses[i].remainingAmount) {
                this.sumRemainingAmount += this.gOtherVoucherDetailExpenses[i].remainingAmount;
            }
        }
        for (let i = 0; i < this.gOtherVoucherDetailExpenseAllocations.length; i++) {
            if (this.gOtherVoucherDetailExpenseAllocations[i].allocationAmount) {
                this.sumAllocationAmountDetail += this.gOtherVoucherDetailExpenseAllocations[i].allocationAmount;
            }
            if (this.gOtherVoucherDetailExpenseAllocations[i].amount) {
                this.sumAmountDetail += this.gOtherVoucherDetailExpenseAllocations[i].amount;
            }
            if (this.gOtherVoucherDetail[i].amount) {
                this.sumAccountingAmountDetail += this.gOtherVoucherDetail[i].amount;
            }
        }
    }

    ngAfterViewInit(): void {
        const inputs = document.getElementsByTagName('input');
        inputs[0].focus();
    }

    addIfLastInput(i) {
        if (i === this.gOtherVoucherDetailExpenseAllocations.length - 1) {
            this.keyDownAddRow(false, 0);
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

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.gOtherVoucherDetail;
    }

    addDataToDetail() {
        this.gOtherVoucherDetail = this.details ? this.details : this.gOtherVoucherDetail;
    }

    saveDetails2nd(i) {
        this.currentRow = i;
        this.details2nd = this.gOtherVoucherDetailExpenseAllocations;
    }

    addDataToDetail2nd() {
        this.gOtherVoucherDetailExpenseAllocations = this.details2nd ? this.details2nd : this.gOtherVoucherDetailExpenseAllocations;
    }

    actionFocus(indexCol, indexRow) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
    }

    changeExpenseItemCode(detail: any, i: number) {
        if (detail.expenseItemID) {
            this.gOtherVoucherDetail[i].expenseItemID = detail.expenseItemID;
            this.gOtherVoucherDetailExpenseAllocations[i].expenseItemID = detail.expenseItemID;
        }
    }
}
