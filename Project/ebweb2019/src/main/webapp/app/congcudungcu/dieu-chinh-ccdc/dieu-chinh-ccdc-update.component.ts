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
    DDSo_TyLePBo,
    REPORT,
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
import { ChiPhiTraTruocService } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc.service';
import { GOtherVoucherDetails } from 'app/shared/model/g-other-voucher-details.model';
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
import { ITIAllocationAllocated } from 'app/shared/model/ti-allocation-allocated.model';
import { ITIAllocationPost } from 'app/shared/model/ti-allocation-post.model';
import { ITIAdjustment } from 'app/shared/model/ti-adjustment.model';
import { DieuChinhCcdcService } from 'app/congcudungcu/dieu-chinh-ccdc/dieu-chinh-ccdc.service';
import { ITIAdjustmentDetails, TIAdjustmentDetails } from 'app/shared/model/ti-adjustment-details.model';
import { ToolsService } from 'app/entities/tools';
import { GhiTangService } from 'app/congcudungcu/ghi-tang-ccdc-khac';
import { DieuChuyenCcdcService } from 'app/congcudungcu/dieu-chuyen-ccdc/dieu-chuyen-ccdc.service';
import { PPDiscountReturnService } from 'app/muahang/hang_mua_tra_lai_giam_gia/pp-discount-return';
import { DATE_FORMAT_SEARCH, DATE_FORMAT_SLASH } from 'app/shared';

@Component({
    selector: 'eb-dieu-chinh-ccdc-update',
    templateUrl: './dieu-chinh-ccdc-update.component.html',
    styleUrls: ['./dieu-chinh-ccdc-update.component.css']
})
export class DieuChinhCcdcUpdateComponent extends BaseComponent implements OnInit, AfterViewInit, AfterViewChecked {
    @ViewChild('test') public modalComponent: NgbModalRef;
    @ViewChild('checkAmount') public modalCheckAmount: NgbModalRef;
    @ViewChild('content') public modalContent: NgbModalRef;
    @ViewChild('deleteModal') public deleteModal: NgbModalRef;
    private _itiAdjustment: ITIAdjustment;
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
    // gOtherVoucherCopy: IGOtherVoucher;
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
    tiAdjustmentDetail: ITIAdjustmentDetails[];
    // tiAllocationAllocateds: ITIAllocationAllocated[];
    autoPrinciple: any;
    // objectDate: any;
    prepaidExpenseCodeList: any[];
    costAccounts: any;
    // tiAllocationPosts: ITIAllocationPost[];
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
    record_: any;
    DDSo_TyLe: any;
    DDSo_NgoaiTe: any;
    DDSo_TienVND: any;
    DDSo_TyLePB: any;
    checkSaveBoolean: Boolean;
    noCopy: any;
    REPORT = REPORT;
    budgetItems: IBudgetItem[];
    indexFocusDetailCol: any;
    listIDInputDeatilTax: any[] = [
        'toolsItem',
        'toolsName',
        'quantity',
        'allocationAccount',
        'remainingAmount',
        'newRemainingAmount',
        'diffRemainingAmount',
        'remainAllocationTimes',
        'newRemainingAllocationTime',
        'differAllocationTime',
        'allocatedAmount',
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
    tiAllocationDetailsCopy: any[];
    // tiAllocationAllocatedsCopy: any[];
    listTools: any[];
    // tiAllocationPostsCopy: any[];
    tiAdjustmentCopy: any;
    tiAdjustmentDetailCopy: any[];
    listTools2: any;
    sumQuantity: any;
    sumRemainingAmount: any;
    sumNewRemainingAmount: any;
    sumDiffRemainingAmount: any;
    sumRemainAllocationTimes: any;
    sumNewRemainingAllocationTime: any;
    sumDifferAllocationTime: any;
    sumAllocatedAmount: any;

    constructor(
        private router: Router,
        private toolsService: ToolsService,
        private ghiTangService: GhiTangService,
        private dieuChuyenCcdcService: DieuChuyenCcdcService,
        private dieuChinhCcdcService: DieuChinhCcdcService,
        private expenseItemService: ExpenseItemService,
        private chiPhiTraTruocService: ChiPhiTraTruocService,
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
        private pPDiscountReturnService: PPDiscountReturnService,
        private modalService: NgbModal,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        public utilsService: UtilsService,
        private autoPrinciplellService: AutoPrincipleService
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
                if (data.tiAdjustment && data.tiAdjustment.id) {
                    this.checkData = true;
                    this.isEdit = false;
                    this.tiAdjustment = data.tiAdjustment;
                    this.tiAdjustment.date = moment(data.tiAdjustment.date);
                    this.tiAdjustment.postedDate = moment(data.tiAdjustment.postedDate);
                    this.tiAdjustmentDetail = data.tiAdjustment.tiAdjustmentDetails;

                    // this.viewVouchersSelected = data.gOtherVoucher.refVoucherDTOS ? data.gOtherVoucher.refVoucherDTOS : [];
                    if (this.isSoTaiChinh) {
                        this.no = this.tiAdjustment.noFBook;
                    } else if (this.sysTypeLedger === 1) {
                        this.no = this.tiAdjustment.noMBook;
                    }
                    if (this.dataSession && this.dataSession.isEdit) {
                        this.edit();
                    }
                    this.pPDiscountReturnService.getRefVouchersByPPdiscountReturnID(this.tiAdjustment.id).subscribe(ref => {
                        this.viewVouchersSelected = ref.body ? ref.body : [];
                    });
                } else {
                    this.checkData = false;
                    this.viewVouchersSelected = [];
                    this.isEdit = true;
                    this.tiAdjustment = {};
                    this.tiAdjustment.date = this.utilsService.ngayHachToan(this.currentAccount);
                    this.tiAdjustment.postedDate = this.tiAdjustment.date;
                    this.tiAdjustmentDetail = [];
                    // lấy dữ liệu khi thêm mới
                    this.getSessionData();
                }
            });
        });
    }

    ngOnInit() {
        this.getToolsActive();
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
        // this.tiAllocationPostsCopy = [];
        // this.tiAllocationAllocatedsCopy = [];
        this.tiAdjustmentDetailCopy = [];
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
                    id: this.tiAdjustment.id,
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

    get tiAdjustment() {
        return this._itiAdjustment;
    }

    set tiAdjustment(iTiAdjustment: ITIAdjustment) {
        this._itiAdjustment = iTiAdjustment;
    }

    getData() {
        // diễn giải
        this.autoPrinciplellService.getAllAutoPrinciples().subscribe((res: HttpResponse<IAutoPrinciple[]>) => {
            this.autoPrinciples = res.body;
            const gOtherVoucherReason: IAutoPrinciple = new AutoPrinciple();
            if (this.tiAdjustment.id) {
                gOtherVoucherReason.autoPrincipleName = this.tiAdjustment.reason;
                if (this.autoPrinciples && !this.autoPrinciples.some(x => x.autoPrincipleName === this.tiAdjustment.reason)) {
                    this.autoPrinciples.push(gOtherVoucherReason);
                }
            } else {
                // if (this.objectDate) {
                const dateItem = this.tiAdjustment.date;
                this.translate.get(['ebwebApp.tITransfer.reason']).subscribe((ref: any) => {
                    this.tiAdjustment.reason = this.translate.instant('ebwebApp.tITransfer.reason', {
                        date: dateItem
                            ? dateItem instanceof moment ? dateItem.format(DATE_FORMAT_SLASH) : moment(dateItem).format(DATE_FORMAT_SLASH)
                            : ''
                    });
                    // gOtherVoucherReason.autoPrincipleName = this.tiAdjustment.reason;
                    // if (this.autoPrinciples && !this.autoPrinciples.some(x => x.autoPrincipleName === this.tiAdjustment.reason)) {
                    //     this.autoPrinciples.push(gOtherVoucherReason);
                    // }
                });
                // }
            }
        });
        // khoanr mucj chi phi
        this.getExpenseItemService();
        // tk chi phi
        this.getCostAccounts();
        // đối tượng phân bổ
        this.getPrepaidExpenseCode();
        // đối tượng nợ, đối tượng có, nhân viên
        this.getObjectAcount();
        // lây mục thu chi
        this.getBudgetItem();
        // lay khoan muc chi phi
        this.getExpenseItems();
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
                this.utilService.isEquivalent(this.tiAdjustment, this.tiAdjustmentCopy) &&
                this.utilService.isEquivalentArray(this.tiAdjustmentDetail, this.tiAdjustmentDetailCopy)
            );
        }
    }

    addNewRow(eventData: any, select: number) {
        // if (select === 0) {
        // const tiAllocationAllocatedItemCopy = this.tiAllocationAllocateds[this.tiAllocationAllocateds.length - 1];
        // tiAllocationAllocatedItemCopy.id = null;
        // this.tiAllocationAllocateds.push(Object.assign({}, tiAllocationAllocatedItemCopy));
        // const a = this.tiAllocationPosts[this.tiAllocationPosts.length - 1];
        // a.id = null;
        // this.tiAllocationPosts.push(Object.assign({}, a));
        // this.tiAllocationAllocateds[this.tiAllocationAllocateds.length - 1].id = null;
        // this.tiAllocationPosts[this.tiAllocationPosts.length - 1].id = null;
        const item = new TIAdjustmentDetails();
        // @ts-ignore
        this.tiAdjustmentDetail.push(item);
        this.changeAllocationAmount();

        const nameTag: string = event.srcElement.id;
        const index: number = this.tiAdjustmentDetail.length - 1;
        const nameTagIndex: string = nameTag + String(index);
        setTimeout(function() {
            const element: HTMLElement = document.getElementById(nameTagIndex);
            if (element) {
                element.focus();
            }
        }, 0);
        // }
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
        index = index + 1;
        if (checkCopy) {
            // this.tiAllocationAllocateds.splice(index, 0, Object.assign({}, this.tiAllocationAllocateds[index]));
            // this.tiAllocationPosts.splice(index, 0, Object.assign({}, this.tiAllocationPosts[index]));
            // this.tiAllocationAllocateds[index].id = null;
            // this.tiAllocationPosts[index].id = null;
        } else {
            // this.tiAllocationAllocateds.splice(index, 0, Object.assign({}, {}));
            // this.tiAllocationPosts.splice(index, 0, Object.assign({}, {}));
            // this.tiAllocationAllocateds[index].id = null;
            // this.tiAllocationPosts[index].id = null;
            //  this.tiAllocationAllocateds.push({});
            //  this.tiAllocationPosts.push({});
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

    keyPress(detail: any, index: number, key: string, screen: number) {
        if (index !== null && index >= 0) {
            if (screen === 0) {
                switch (key) {
                    case 'ctr + delete': {
                        const idByIndex = '';
                        this.tiAdjustmentDetail.splice(index, 1);
                        const lst = this.listIDInputDeatilTax;
                        const col = this.indexFocusDetailCol;
                        const row = this.indexFocusDetailRow - 1;
                        setTimeout(function() {
                            const element: HTMLElement = document.getElementById(lst[col] + row);
                            if (element) {
                                element.focus();
                            }
                        }, 0);
                        break;
                    }
                    case 'ctr + c': {
                        const detailCopy: any = Object.assign({}, detail);
                        detailCopy.id = null;
                        this.tiAdjustmentDetail.splice(index + 1, 0, detailCopy);
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
                        break;
                    }
                    case 'ctr + insert': {
                        this.tiAdjustmentDetail.push({});
                        const lst = this.listIDInputDeatilTax;
                        const col = this.indexFocusDetailCol;
                        const row = this.tiAdjustmentDetail.length - 1;
                        setTimeout(function() {
                            const element: HTMLElement = document.getElementById(lst[col] + row);
                            if (element) {
                                element.focus();
                            }
                        }, 0);
                        break;
                    }
                }
            } else if (screen === 1) {
                this.viewVouchersSelected.splice(index, 1);
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

    new() {
        this.isEdit = false;
    }

    copy() {
        this.tiAdjustmentCopy = Object.assign({}, this.tiAdjustment);
        this.noCopy = this.no;
        // if (this.tiAllocationPosts) {
        //     this.tiAllocationPostsCopy = this.tiAllocationPosts.map(object => ({ ...object }));
        // }
        // if (this.tiAllocationAllocateds) {
        //     this.tiAllocationAllocatedsCopy = this.tiAllocationAllocateds.map(object => ({ ...object }));
        // }
        if (this.tiAdjustmentDetail) {
            this.tiAdjustmentDetailCopy = this.tiAdjustmentDetail.map(object => ({ ...object }));
        }
    }

    // getPrepaidExpenseAllocation() {
    //     if (this.objectDate) {
    //         // this.translate.get(['ebwebApp.gOtherVoucher.reasonTitle']).subscribe((ref: any) => {
    //         //     this.tiAdjustment.reason = this.translate.instant('ebwebApp.gOtherVoucher.reasonTitle', {
    //         //         month: this.objectDate.month,
    //         //         year: this.objectDate.year
    //         //     });
    //         //     const gOtherVoucherReason: IAutoPrinciple = new AutoPrinciple();
    //         //     gOtherVoucherReason.autoPrincipleName = this.tiAdjustment.reason;
    //         //     if (this.autoPrinciples && !this.autoPrinciples.some(x => x.autoPrincipleName === this.tiAdjustment.reason)) {
    //         //         this.autoPrinciples.push(gOtherVoucherReason);
    //         //     }
    //         // });
    //         this.tiAdjustment.date = moment(new Date(this.objectDate.year, this.objectDate.month, 0));
    //         this.tiAdjustment.postedDate = moment(new Date(this.objectDate.year, this.objectDate.month, 0));
    //         this.dieuChinhCcdcService
    //             .getDetails({
    //                 month: this.objectDate.month,
    //                 year: this.objectDate.year
    //             })
    //             .subscribe(res => {
    //                 this.listTools = [];
    //                 // this.tiAllocationAllocateds = [];
    //                 // this.tiAllocationPosts = [];
    //                 this.tiAdjustmentDetail = res.body;
    //                 // this.tiAdjustmentDetail.forEach(x => {
    //                 //     const item = { id: x.toolsID, toolsCode: x.toolsCode, toolsName: x.toolsName };
    //                 //     this.listTools.push(item);
    //                 //     const tiAllocationAllocatedItem: ITIAllocationAllocated = {};
    //                 //     tiAllocationAllocatedItem.toolsItem = item;
    //                 //     tiAllocationAllocatedItem.toolsCode = item.toolsCode;
    //                 //     tiAllocationAllocatedItem.toolsName = item.toolsName;
    //                 //     tiAllocationAllocatedItem.toolsID = item.id;
    //                 //     tiAllocationAllocatedItem.totalAllocationAmount = x.totalAllocationAmount;
    //                 //     tiAllocationAllocatedItem.rate = 100;
    //                 //     tiAllocationAllocatedItem.allocationAmount = x.totalAllocationAmount;
    //                 //     this.tiAllocationAllocateds.push(tiAllocationAllocatedItem);
    //                 //     const itiAllocationPost: ITIAllocationPost = {};
    //                 //     itiAllocationPost.description = this.tiAdjustment.reason;
    //                 //     itiAllocationPost.creditAccount = null;
    //                 //     itiAllocationPost.amount = 10;
    //                 //     itiAllocationPost.debitAccount = null;
    //                 //     this.tiAllocationPosts.push(itiAllocationPost);
    //                 // });
    //                 // for (let i = 0; i < res.body.prepaidExpenseAllocations.length; i++) {
    //                 //      this.tiAllocationAllocateds.push({});
    //                 //      this.tiAllocationAllocateds[i].prepaidExpenseID =
    //                 //         res.body.prepaidExpenseAllocations[i].prepaidExpenseID;
    //                 //      this.tiAllocationAllocateds[i].prepaidExpenseCode =
    //                 //         res.body.prepaidExpenseAllocations[i].prepaidExpenseCode;
    //                 //      this.tiAllocationAllocateds[i].prepaidExpenseName =
    //                 //         res.body.prepaidExpenseAllocations[i].prepaidExpenseName;
    //                 //      this.tiAllocationAllocateds[i].objectID = res.body.prepaidExpenseAllocations[i].allocationObjectID;
    //                 //      this.tiAllocationAllocateds[i].allocationAmount =
    //                 //         res.body.prepaidExpenseAllocations[i].allocationAmountGo;
    //                 //      this.tiAllocationAllocateds[i].allocationRate = res.body.prepaidExpenseAllocations[i].allocationRate;
    //                 //      this.tiAllocationAllocateds[i].amount =
    //                 //          this.tiAllocationAllocateds[i].allocationAmount *
    //                 //         res.body.prepaidExpenseAllocations[i].allocationRate /
    //                 //         100;
    //                 //      this.tiAllocationAllocateds[i].costAccount = res.body.prepaidExpenseAllocations[i].costAccount;
    //                 //      this.tiAllocationAllocateds[i].expenseItemID = res.body.prepaidExpenseAllocations[i].expenseItemID;
    //                 //     // lấy CHI TIẾT HẠCH TOÁN
    //                 //      this.tiAllocationPosts.push({});
    //                 //     if (res.body.prepaidExpenseAllocations[i].reason) {
    //                 //          this.tiAllocationPosts[i].description = res.body.prepaidExpenseAllocations[i].reason;
    //                 //     } else {
    //                 //          this.tiAllocationPosts[i].description = this.tiAdjustment.reason;
    //                 //     }
    //                 //      this.tiAllocationPosts[i].debitAccount = res.body.prepaidExpenseAllocations[i].costAccount;
    //                 //      this.tiAllocationPosts[i].creditAccount = res.body.prepaidExpenseAllocations[i].allocationAccount;
    //                 //      this.tiAllocationPosts[i].amount =  this.tiAllocationAllocateds[i].amount;
    //                 //      this.tiAllocationPosts[i].amountOriginal =  this.tiAllocationAllocateds[i].amount;
    //                 //      this.tiAllocationPosts[i].expenseItemID = res.body.prepaidExpenseAllocations[i].expenseItemID;
    //                 // }
    //                 this.sumAllList();
    //             });
    //     }
    //     this.changeAllocationAmount();
    // }

    getSessionData() {
        // this.objectDate = JSON.parse(sessionStorage.getItem('objectDate')) ? JSON.parse(sessionStorage.getItem('objectDate')) : null;
        // sessionStorage.removeItem('objectDate');
        // lấy chi tiết chi phí
        // this.getPrepaidExpenseAllocation();
    }

    getPrepaidExpenseCode() {
        this.chiPhiTraTruocService.getPrepaidExpenseCode().subscribe(ref => {
            this.prepaidExpenseCodeList = ref.body;
        });
    }

    getExpenseItemService() {
        this.expenseItemService.getExpenseItems().subscribe(ref => {
            this.expenseItems = ref.body;
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

    getExpenseItems() {
        this.expenseItemsService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body;
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
        if (this.tiAdjustment.reason) {
            // for (let i = 0; i < this.tiAllocationPosts.length; i++) {
            //     this.tiAllocationPosts[i].description = this.tiAdjustment.reason;
            // }
        }
    }

    changeDate() {
        if (this.tiAdjustment.date) {
            this.tiAdjustment.postedDate = this.tiAdjustment.date;
        }
    }

    changeReason(reason: string) {
        if (reason) {
            // for (let i = 0; i < this.tiAllocationPosts.length; i++) {
            //     this.tiAllocationPosts[i].description = reason;
            // }
        }
    }

    getNoBook() {
        this.utilService
            .getGenCodeVoucher({
                typeGroupID: 55, // typeGroupID loại chứng từ
                companyID: '', // ID công ty
                branchID: '', // ID chi nhánh
                displayOnBook: this.sysTypeLedger // 0 - sổ tài chính, 1 - sổ quản trị
            })
            .subscribe((res: HttpResponse<string>) => {
                // this.mCReceipt.noFBook = (res.body.toString());
                if (!this.checkData) {
                    this.no = res.body;
                    if (this.isSoTaiChinh) {
                        this.tiAdjustment.noFBook = this.no;
                    } else {
                        this.tiAdjustment.noMBook = this.no;
                    }
                }
                this.copy();
            });
    }

    saveAfter(check?: Boolean) {
        // for (let i = 0; i < this.tiAllocationAllocateds.length; i++) {
        //     this.tiAllocationAllocateds[i].orderPriority = i;
        //     this.tiAllocationPosts[i].orderPriority = i;
        // }
        // this.tiAdjustment.tiAllocationAllocateds = this.tiAllocationAllocateds;
        // this.tiAdjustment.tiAllocationDetails = this.tiAdjustmentDetail;
        // this.tiAdjustment.tiAllocationPosts = this.tiAllocationPosts;
        this.tiAdjustment.tiAdjustmentDetails = this.tiAdjustmentDetail;
        this.tiAdjustment.viewVouchers = this.viewVouchersSelected;
        if (this.isSoTaiChinh) {
            this.tiAdjustment.noFBook = this.no;
        } else {
            this.tiAdjustment.noMBook = this.no;
        }
        if (this.tiAdjustment.id) {
            this.dieuChinhCcdcService.update(this.tiAdjustment).subscribe(
                res => {
                    this.copy();
                    this.isCloseAll = true;
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
                        // this.router.navigate(['./dieu-chinh-ccdc', res.body.gOtherVoucher.id, 'edit']);
                        this.ngOnInit();
                    }
                },
                res => {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.pPDiscountReturn.error.unUpdate'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                }
            );
        } else {
            this.dieuChinhCcdcService.create(this.tiAdjustment).subscribe(
                res => {
                    this.copy();
                    this.isCloseAll = true;
                    this.toastr.success(
                        this.translate.instant('ebwebApp.mBDeposit.insertSuccess'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    if (check) {
                        this.isEdit = false;
                        this.addNew(event);
                    } else {
                        this.router.navigate(['./dieu-chinh-ccdc', res.body.id, 'edit']);
                    }
                },
                res => {
                    if (res.error.entityName === 'noBookLimit') {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.pPDiscountReturn.error.error.noBookLimit'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    } else {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.pPDiscountReturn.error.unInsertSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                }
            );
        }
    }

    closeForm() {
        event.preventDefault();
        if (this.tiAdjustmentCopy) {
            if (
                this.no !== this.noCopy ||
                !this.utilService.isEquivalent(this.tiAdjustment, this.tiAdjustmentCopy) ||
                !this.utilService.isEquivalent(this.tiAdjustment, this.tiAdjustmentCopy)
                // ||
                // !this.utilService.isEquivalentArray(this.tiAllocationPosts, this.tiAllocationPostsCopy) ||
                // !this.utilService.isEquivalentArray(this.tiAllocationAllocateds, this.tiAllocationAllocatedsCopy) ||
                // !this.utilService.isEquivalentArray(this.tiAdjustmentDetail, this.tiAdjustmentDetailCopy)
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
            this.router.navigate(['/dieu-chinh-ccdc', 'hasSearch', '1']);
        } else if (window.location.href.includes('/from-g-other-voucher')) {
            if (JSON.parse(sessionStorage.getItem('dataSearchGOtherVoucher')) !== null) {
                this.router.navigate(['/g-other-voucher', 'hasSearch']);
            } else {
                this.router.navigate(['/g-other-voucher']);
            }
        } else {
            this.router.navigate(['/dieu-chinh-ccdc']);
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
                this.router.navigate(['/dieu-chinh-ccdc', 'hasSearch', '1']);
            } else {
                this.router.navigate(['/dieu-chinh-ccdc']);
            }
        }
    }

    changeAmount(detail: GOtherVoucherDetails, i: number) {
        detail.amountOriginal = detail.amount;
        // this.tiAllocationPosts[i].amount = detail.amount;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Sua])
    edit() {
        event.preventDefault();
        if (
            !(this.isEdit || this.tiAdjustment.recorded) &&
            !this.checkCloseBook(this.currentAccount, this.tiAdjustment.postedDate) &&
            !this.utilsService.isShowPopup
        ) {
            this.isEdit = true;
            this.copy();
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_GhiSo])
    record() {
        event.preventDefault();
        // if (
        //     !(this.tiAdjustment || this.checkCloseBook(this.currentAccount, this.tiAdjustment.postedDate)) &&
        //     !this.utilsService.isShowPopup
        // ) {
        //     if (!this.tiAdjustment.recorded && !this.utilsService.isShowPopup) {
        if (this.tiAdjustment.id) {
            this.record_ = {};
            this.record_.id = this.tiAdjustment.id;
            this.record_.typeID = TypeID.DIEU_CHINH_CCDC;
            if (!this.tiAdjustment.recorded) {
                this.gLService.record(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                    if (res.body.success) {
                        this.tiAdjustment.recorded = true;
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
                            this.translate.instant('ebwebApp.mBCreditCard.message')
                        );
                    }
                });
            }
            // }
            // }
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
        // if (!(this.isEdit || this.checkCloseBook(this.currentAccount, this.tiAdjustment.postedDate))) {
        //     if (this.tiAdjustment.recorded) {
        if (this.tiAdjustment.id) {
            this.record_ = {};
            this.record_.id = this.tiAdjustment.id;
            this.record_.typeID = TypeID.DIEU_CHINH_CCDC;
            this.gLService.unrecord(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                if (res.body.success) {
                    this.tiAdjustment.recorded = false;
                    this.toastr.success(
                        this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                        this.translate.instant('ebwebApp.mBCreditCard.message')
                    );
                }
            });
            // }
            // }
            // else {
            //     this.toastr.warning(
            //         this.translate.instant('global.data.warningUnRecorded'),
            //         this.translate.instant('ebwebApp.mBDeposit.message')
            //     );
            // }
        }
    }

    deleteAfter() {
        if (this.tiAdjustment && this.tiAdjustment.id) {
            this.dieuChinhCcdcService.delete(this.tiAdjustment.id).subscribe(
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
            this.dieuChinhCcdcService
                .getCustomerReport({
                    id: this.tiAdjustment.id,
                    typeID: TypeID.DIEU_CHINH_CCDC,
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
                        const name = 'Dieu_Chinh_CCDC.pdf';
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
                // index = this.tiAllocationAllocateds.indexOf(ref.content);
            } else if (this.checkAllocation === 0) {
                // index = this.tiAllocationPosts.indexOf(ref.content);
            }
            index = index + 1;
            // this.tiAllocationAllocateds.splice(index, 0, Object.assign({}, {}));
            // this.tiAllocationPosts.splice(index, 0, Object.assign({}, {}));
            // this.tiAllocationAllocateds[index].id = null;
            // this.tiAllocationPosts[index].id = null;
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
                // this.deleteRow(this.tiAllocationAllocateds.indexOf(ref.content));
            } else if (this.checkAllocation === 0) {
                // this.deleteRow(this.tiAllocationPosts.indexOf(ref.content));
            } else if (this.checkAllocation === 2) {
                // console.count("c:");
                // for (let i = 0; i < this.tiAllocationAllocateds.length; i++) {
                // if (ref.content.prepaidExpenseID ===  this.tiAllocationAllocateds[i].prepaidExpenseID) {
                //      this.tiAllocationAllocateds.splice(i, 1);
                //      this.tiAllocationPosts.splice(i, 1);
                //     i--;
                // }
                // }
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
            //  this.tiAllocationAllocateds = ref.content;
            this.changeAllocationAmount();
        });
        this.eventManager.subscribe('afterCopyRow', ref => {
            const index = 0;
            if (this.checkAllocation === 1) {
                // index = this.tiAllocationAllocateds.indexOf(ref.content);
            } else if (this.checkAllocation === 0) {
                // index = this.tiAllocationPosts.indexOf(ref.content);
            }
            this.keyDownAddRow(true, index);
        });
    }

    deleteRow(index) {
        if (index !== null && index >= 0) {
            // const countItem =  this.tiAllocationAllocateds.filter(
            //     item => item.prepaidExpenseID ===  this.tiAllocationAllocateds[index].prepaidExpenseID
            // );
            // // nếu là dòng detail cuối cùng của mã chi phí trả trước thì sẽ xóa dòng chi phí pb , hạch toán và chi phí trả trước đó
            // if (countItem.length === 1) {
            //     for (let i = 0; i < this.tiAdjustmentDetail.length; i++) {
            //         if (this.tiAdjustmentDetail[i].prepaidExpenseID ===  this.tiAllocationAllocateds[index].prepaidExpenseID) {
            //             this.tiAdjustmentDetail.splice(i, 1);
            //             break;
            //         }
            //     }
            // }
            // this.tiAllocationAllocateds.splice(index, 1);
            // this.tiAllocationPosts.splice(index, 1);
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
            if (!this.tiAdjustment.postedDate) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.postedDate'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
            // if (moment(this.tiAdjustment.postedDate) < moment(this.tiAdjustment.date)) {
            //     this.toastr.error(this.translate.instant('ebwebApp.common.error.dateAndPostDate'));
            //     return false;
            // }
            for (let i = 0; i < this.tiAdjustmentDetail.length; i++) {
                // if (this.tiAdjustmentDetail[i].allocationAmount > this.tiAdjustmentDetail[i].remainingAmount) {
                //     this.toastr.error(
                //         this.translate.instant('ebwebApp.gOtherVoucher.error.remainingAmount'),
                //         this.translate.instant('ebwebApp.mBCreditCard.message')
                //     );
                //     return;
                // }
                this.tiAdjustmentDetail[i].orderPriority = i;
            }
            if (this.isEdit && !this.utilsService.isShowPopup) {
                this.checkSaveBoolean = check;
                if (!this.tiAdjustmentDetail || this.tiAdjustmentDetail.length <= 0) {
                    this.toastr.error(this.translate.instant('ebwebApp.pPDiscountReturn.notPPDiscountReturn'));
                    return;
                }
                if (this.checkCloseBook(this.currentAccount, this.tiAdjustment.postedDate)) {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.lockBook.error.checkForSave'),
                        this.translate.instant('ebwebApp.mCReceipt.error.error')
                    );
                    return false;
                }
                // if (!this.tiAllocationAllocateds || this.tiAllocationAllocateds.length <= 0) {
                //     this.toastr.error(this.translate.instant('ebwebApp.pPDiscountReturn.notPPDiscountReturn'));
                //     return;
                // }
                // const creditAccount = this.tiAllocationPosts.filter(item => !item.creditAccount);
                // if (creditAccount && creditAccount.length) {
                //     this.toastr.error(
                //         this.translate.instant('ebwebApp.pporder.error.required'),
                //         this.translate.instant('ebwebApp.pPDiscountReturn.voucher')
                //     );
                //     return false;
                // }
                // const debitAccount = this.tiAllocationPosts.filter(item => !item.debitAccount);
                // if (debitAccount && debitAccount.length) {
                //     this.toastr.error(
                //         this.translate.instant('ebwebApp.pporder.error.required'),
                //         this.translate.instant('ebwebApp.pPDiscountReturn.voucher')
                //     );
                //     return false;
                // }
                // check tỉ lệ phân bổ và số tiền
                const countSumTotal = new Map();
                const countAllocationRate = new Map();
                this.tiAdjustment.totalAmount = 0;
                // this.tiAdjustment.totalAmountOriginal = 0;
                // for (let i = 0; i <  this.tiAllocationAllocateds.length; i++) {
                //     countAllocationRate.set(
                //          this.tiAllocationAllocateds[i].prepaidExpenseCode,
                //         countAllocationRate.get( this.tiAllocationAllocateds[i].prepaidExpenseCode)
                //             ? Number(countAllocationRate.get( this.tiAllocationAllocateds[i].prepaidExpenseCode)) +
                //                this.tiAllocationAllocateds[i].allocationRate
                //             :  this.tiAllocationAllocateds[i].allocationRate
                //     );
                //     countSumTotal.set(
                //          this.tiAllocationAllocateds[i].prepaidExpenseCode,
                //         countSumTotal.get( this.tiAllocationAllocateds[i].prepaidExpenseCode)
                //             ? Number(countSumTotal.get( this.tiAllocationAllocateds[i].prepaidExpenseCode)) +
                //                this.tiAllocationAllocateds[i].amount
                //             :  this.tiAllocationAllocateds[i].amount
                //     );
                //      this.tiAllocationPosts[i].amountOriginal =  this.tiAllocationPosts[i].amount;
                //     if ( this.tiAllocationPosts[i].amount) {
                //         // this.tiAdjustment.totalAmountOriginal +=  this.tiAllocationPosts[i].amount;
                //         this.tiAdjustment.totalAmount +=  this.tiAllocationPosts[i].amount;
                //     }
                // }
                // let errorCountRate = '';
                // for (const [key, value] of Array.from(countAllocationRate)) {
                //     if (value && value > 100) {
                //         errorCountRate += ' ' + key + ',';
                //     }
                // }
                // if (errorCountRate) {
                //     this.toastr.error(
                //         this.translate.instant('ebwebApp.gOtherVoucher.error.allocationRate', {
                //             rate: errorCountRate.substring(1, errorCountRate.length - 1)
                //         })
                //     );
                //     return;
                // }
                // errorCountRate = '';
                // for (const [key, value] of Array.from(countAllocationRate)) {
                //     if (value < 100) {
                //         errorCountRate += ' ' + key + ',';
                //     }
                // }
                // if (errorCountRate) {
                //     this.toastr.error(
                //         this.translate.instant('ebwebApp.gOtherVoucher.error.allocationRateMin', {
                //             rate: errorCountRate.substring(1, errorCountRate.length - 1)
                //         })
                //     );
                //     return;
                // }
                // if (countError > 0) {
                //
                // }
                // check số tiền
                // for (const item of Array.from(this.tiAllocationAllocateds)) {
                // if (countSumTotal.get(item.prepaidExpenseCode) !== item.allocationAmount) {
                //     this.modalRef = this.modalService.open(this.modalCheckAmount, { backdrop: 'static' });
                //     return;
                // }
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
        return this.dieuChinhCcdcService
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
                    this.router.navigate(['./dieu-chinh-ccdc', res.body.id, 'edit']);
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
        if (detail.rate) {
            detail.allocationAmount = detail.totalAllocationAmount * detail.rate / 100;
            //  this.tiAllocationPosts[i].amount = detail.amount;
        }
        this.changeAllocationAmount();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Xoa])
    delete() {
        event.preventDefault();
        if (!(this.isEdit || this.tiAdjustment.recorded) && !this.utilsService.isShowPopup) {
            if (this.tiAdjustment.id) {
                // this.dieuChinhCcdcService.getMaxMonth(this.tiAdjustment.id).subscribe(res => {
                //     if (moment(this.tiAdjustment.postedDate).isSame(moment(res.body))) {
                this.modalRef = this.modalService.open(this.deleteModal, { backdrop: 'static' });
                //     } else {
                //         this.toastr.error(
                //             this.translate.instant('ebwebApp.gOtherVoucher.error.checkDeletePB'),
                //             this.translate.instant('ebwebApp.mBDeposit.message')
                //         );
                //         return;
                //     }
                // });
            }
        }
    }

    changeTools(detail: any) {
        if (detail.toolsItem) {
            // const prepaidItem = this.tiAdjustmentDetails.find(i => i. === detail.prepaidExpenseID);
            // detail.prepaidExpenseName = prepaidItem.prepaidExpenseCode;
            // detail.allocationAmount = prepaidItem.allocationAmount;
            detail.toolsID = detail.toolsItem.id;
            detail.toolsName = detail.toolsItem.toolsName;
            detail.debitAccount = detail.toolsItem.allocationAwaitAccount;
        }
    }

    changeAllocationAmount() {
        // check tỉ lệ phân bổ và số tiền
        const countSumTotal1 = new Map();
        const countAllocationRate1 = new Map();
        // for (let i = 0; i <  this.tiAllocationAllocateds.length; i++) {
        //     countAllocationRate1.set(
        //          this.tiAllocationAllocateds[i].prepaidExpenseCode,
        //         countAllocationRate1.get( this.tiAllocationAllocateds[i].prepaidExpenseCode)
        //             ? Number(countAllocationRate1.get( this.tiAllocationAllocateds[i].prepaidExpenseCode)) +
        //                this.tiAllocationAllocateds[i].allocationRate
        //             :  this.tiAllocationAllocateds[i].allocationRate
        //     );
        // }
        // for (const item of  this.tiAllocationAllocateds) {
        //     if (countAllocationRate1.get(item.prepaidExpenseCode) !== 100) {
        //         item.checked = true;
        //     } else {
        //         item.checked = false;
        //     }
        // }
    }

    ChangeAllocationAmount(detail: any) {
        // if (detail.allocationAmount <= detail.remainingAmount) {
        //     for (let i = 0; i < this.tiAllocationAllocateds.length; i++) {
        //         // if ( this.tiAllocationAllocateds[i].prepaidExpenseID === detail.prepaidExpenseID) {
        //         //      this.tiAllocationAllocateds[i].allocationAmount = detail.allocationAmount;
        //         //      this.tiAllocationAllocateds[i].amount =
        //         //         detail.allocationAmount *  this.tiAllocationAllocateds[i].allocationRate / 100;
        //         //      this.tiAllocationPosts[i].amount =  this.tiAllocationAllocateds[i].amount;
        //         // }
        //     }
        //     this.sumAllList();
        // } else {
        //     this.toastr.error(
        //         this.translate.instant('ebwebApp.gOtherVoucher.error.remainingAmount'),
        //         this.translate.instant('ebwebApp.mBCreditCard.message')
        //     );
        // }
    }

    sumAllList() {
        this.sumQuantity = 0;
        this.sumRemainingAmount = 0;
        this.sumNewRemainingAmount = 0;
        this.sumDiffRemainingAmount = 0;
        this.sumRemainAllocationTimes = 0;
        this.sumNewRemainingAllocationTime = 0;
        this.sumDifferAllocationTime = 0;
        this.sumAllocatedAmount = 0;
        for (let i = 0; i < this.tiAdjustmentDetail.length; i++) {
            if (this.tiAdjustmentDetail[i].quantity) {
                this.sumQuantity += this.tiAdjustmentDetail[i].quantity;
            }
            if (this.tiAdjustmentDetail[i].remainingAmount) {
                this.sumRemainingAmount += this.tiAdjustmentDetail[i].remainingAmount;
            }
            if (this.tiAdjustmentDetail[i].newRemainingAmount) {
                this.sumNewRemainingAmount += this.tiAdjustmentDetail[i].newRemainingAmount;
            }
            if (this.tiAdjustmentDetail[i].diffRemainingAmount) {
                this.sumDiffRemainingAmount += this.tiAdjustmentDetail[i].diffRemainingAmount;
            }
            if (this.tiAdjustmentDetail[i].remainAllocationTimes) {
                this.sumRemainAllocationTimes += this.tiAdjustmentDetail[i].remainAllocationTimes;
            }
            if (this.tiAdjustmentDetail[i].newRemainingAllocationTime) {
                this.sumNewRemainingAllocationTime += this.tiAdjustmentDetail[i].newRemainingAllocationTime;
            }
            if (this.tiAdjustmentDetail[i].differAllocationTime) {
                this.sumDifferAllocationTime += this.tiAdjustmentDetail[i].differAllocationTime;
            }
            if (this.tiAdjustmentDetail[i].allocatedAmount) {
                this.sumAllocatedAmount += this.tiAdjustmentDetail[i].allocatedAmount;
            }
        }
    }

    ngAfterViewInit(): void {
        const inputs = document.getElementsByTagName('input');
        inputs[0].focus();
    }

    addIfLastInput(i) {
        // if (i === this.tiAllocationAllocateds.length - 1) {
        //     this.keyDownAddRow(false, 0);
        // }
    }

    // ham lui, tien
    previousEdit() {
        // goi service get by row num
        if (this.rowNum !== this.count) {
            this.utilsService
                .findByRowNum({
                    id: this.tiAdjustment.id,
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
                    id: this.tiAdjustment.id,
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
                this.router.navigate(['./dieu-chinh-ccdc', iGOtherVoucher.id, 'edit', 'from-g-other-voucher']);
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
        // this.details = this.tiAllocationPosts;
    }

    addDataToDetail() {
        // this.tiAllocationPosts = this.details ? this.details : this.tiAllocationPosts;
    }

    saveDetails2nd(i) {
        this.currentRow = i;
        // this.details2nd = this.tiAllocationAllocateds;
    }

    addDataToDetail2nd() {
        // this.tiAllocationAllocateds = this.details2nd ? this.details2nd : this.tiAllocationAllocateds;
    }

    actionFocus(indexCol, indexRow) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
    }

    // getToolsActive() {
    //     if (this.tiAdjustment && this.tiAdjustment.id) {
    //         this.ghiTangService.getToolsActive().subscribe(res => {
    //             this.listTools = res.body;
    //             if (this.tiAdjustmentDetail) {
    //                 this.tiAdjustmentDetail.forEach(item => {
    //                     item.toolsItem = this.listTools.find(x => x.id === item.toolsID);
    //                     if (item.toolsItem) {
    //                         item.toolsName = item.toolsItem.toolsName;
    //                     }
    //                 });
    //             }
    //         });
    //     }
    //     this.dieuChuyenCcdcService.getToolsActiveByTITransfer().subscribe(res => {
    //         if (!this.tiAdjustment || !this.tiAdjustment.id) {
    //             this.listTools = res.body;
    //         }
    //         this.listTools2 = res.body;
    //         if (this.tiAdjustment && this.tiAdjustment.id) {
    //             if (this.tiAdjustmentDetail) {
    //                 this.tiAdjustmentDetail.forEach(item => {
    //                     item.toolsItem = this.listTools.find(x => x.id === item.toolsID);
    //                     if (item.toolsItem) {
    //                         item.toolsName = item.toolsItem.toolsName;
    //                     }
    //                 });
    //             }
    //         }
    //     });
    // }

    getToolsActive() {
        if (this.tiAdjustment && this.tiAdjustment.id) {
            this.ghiTangService.getToolsActive().subscribe(res => {
                this.listTools = res.body;
                if (this.tiAdjustmentDetail) {
                    this.tiAdjustmentDetail.forEach(item => {
                        item.toolsItem = this.listTools.find(x => x.id === item.toolsID);
                        if (item.toolsItem) {
                            item.toolsName = item.toolsItem.toolsName;
                        }
                    });
                }
            });
        }
        this.ghiTangService
            .getToolsActiveByTIDecrement({
                date: this.tiAdjustment
                    ? this.tiAdjustment.date instanceof moment
                        ? this.tiAdjustment.date.format(DATE_FORMAT_SEARCH)
                        : moment(this.tiAdjustment.date).format(DATE_FORMAT_SEARCH)
                    : ''
            })
            .subscribe(res => {
                if (!this.tiAdjustment || !this.tiAdjustment.id) {
                    this.listTools = res.body;
                }
                this.listTools2 = res.body;
                if (this.tiAdjustment && this.tiAdjustment.id) {
                    if (this.tiAdjustmentDetail) {
                        this.tiAdjustmentDetail.forEach(item => {
                            item.toolsItem = this.listTools.find(x => x.id === item.toolsID);
                            if (item.toolsItem) {
                                item.toolsName = item.toolsItem.toolsName;
                            }
                        });
                    }
                }
            });
    }

    getDiffRemainingAmount(detail: any) {
        detail.diffRemainingAmount = detail.newRemainingAmount - detail.remainingAmount;
    }
}
