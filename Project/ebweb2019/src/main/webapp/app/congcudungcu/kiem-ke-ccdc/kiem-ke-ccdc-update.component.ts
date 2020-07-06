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
    CategoryName,
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
import { GOtherVoucherDetailExpense } from 'app/shared/model/g-other-voucher-detail-expense.model';
import { GOtherVoucherDetailExpenseAllocation } from 'app/shared/model/g-other-voucher-detail-expense-allocation.model';
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
import { ITIAdjustmentDetails } from 'app/shared/model/ti-adjustment-details.model';
import { ToolsService } from 'app/entities/tools';
import { GhiTangService } from 'app/congcudungcu/ghi-tang-ccdc-khac';
import { KiemKeCcdcService } from 'app/congcudungcu/kiem-ke-ccdc/kiem-ke-ccdc.service';
import { ITIAudit } from 'app/shared/model/ti-audit.model';
import { ITIAuditDetails } from 'app/shared/model/ti-audit-details.model';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import { MCAuditDetailMember } from 'app/shared/model/mc-audit-detail-member.model';
import { it } from '@angular/core/testing/src/testing_internal';
import { DATE_FORMAT, DATE_FORMAT_SEARCH, DATE_FORMAT_SLASH } from 'app/shared';
import { ITIAuditMemberDetails } from 'app/shared/model/ti-audit-member-details.model';
import { PPDiscountReturnService } from 'app/muahang/hang_mua_tra_lai_giam_gia/pp-discount-return';

@Component({
    selector: 'eb-kiem-ke-ccdc-update',
    templateUrl: './kiem-ke-ccdc-update.component.html',
    styleUrls: ['./kiem-ke-ccdc-update.component.css']
})
export class KiemKeCcdcUpdateComponent extends BaseComponent implements OnInit, AfterViewInit, AfterViewChecked {
    @ViewChild('test') public modalComponent: NgbModalRef;
    @ViewChild('checkAmount') public modalCheckAmount: NgbModalRef;
    @ViewChild('content') public modalContent: NgbModalRef;
    @ViewChild('deleteModal') public deleteModal: NgbModalRef;
    @ViewChild('deleteModalRelationShip') public deleteModalRelationShip: NgbModalRef;
    @ViewChild('afterSave') public afterSaveModal: NgbModalRef;
    private _itiAudit: ITIAudit;
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
    tiAdjustmentDetail: ITIAdjustmentDetails[];
    // tiAllocationAllocateds: ITIAllocationAllocated[];
    autoPrinciple: any;
    objectDate: any;
    prepaidExpenseCodeList: any[];
    costAccounts: any;
    tiAllocationPosts: ITIAllocationPost[];
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
    // sumAmount: number;
    // sumRemainingAmount: number;
    // sumAllocationAmount: number;
    // sumAllocationAmountDetail: any;
    // sumAmountDetail: number;
    // sumAccountingAmountDetail: number;
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

    // tiAllocationAllocatedsCopy: any[];
    listTools: any[];
    tiAllocationPostsCopy: any[];
    CategoryName = CategoryName;
    itiAuditDetail: ITIAuditDetails[];
    itiAuditDetailCopy: any[];
    itiAuditCopy: any;
    tiAuditMemberDetails: any;
    recommendationList = [{ id: 0, name: 'Ghi tăng' }, { id: 1, name: 'Ghi giảm' }, { id: 2, name: 'Không xử lý' }];
    objectAfterSave: { noBookIncrement: string; tiDecrementID: any; tiIncrementID: string; noBookDecrement: string };
    checkRecommendation: boolean;
    sumQuantityOnBook: number;
    sumQuantityInventory: number;
    sumExecuteQuantity: number;
    sumDiffQuantity: number;

    constructor(
        private router: Router,
        private toolsService: ToolsService,
        private ghiTangService: GhiTangService,
        private kiemKeCcdcService: KiemKeCcdcService,
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
        private branchIDService: OrganizationUnitService,
        private expenseItemsService: ExpenseItemService,
        private statisticsCodeService: StatisticsCodeService,
        private eMContractService: EMContractService,
        private accountListService: AccountListService,
        private autoPrincipleService: AutoPrincipleService,
        private organizationUnitService: OrganizationUnitService,
        private costSetService: CostSetService,
        private pPDiscountReturnService: PPDiscountReturnService,
        private creditCardService: CreditCardService,
        private translate: TranslateService,
        private toastr: ToastrService,
        private budgetItemService: BudgetItemService,
        private eventManager: JhiEventManager,
        private refModalService: RefModalService,
        private principal: Principal,
        private modalService: NgbModal,
        private unitService: UnitService,
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
                if (data.itiAudits && data.itiAudits.id) {
                    this.checkData = true;
                    this.isEdit = false;
                    this.itiAudit = data.itiAudits;
                    this.itiAudit.date = moment(data.itiAudits.date);
                    this.itiAudit.inventoryDate = this.itiAudit.date;
                    // this.itiAudit.postedDate = moment(data.tiAllocation.postedDate);
                    this.itiAuditDetail = data.itiAudits.tiAuditDetails;
                    this.pPDiscountReturnService.getRefVouchersByPPdiscountReturnID(this.itiAudit.id).subscribe(ref => {
                        this.viewVouchersSelected = ref.body ? ref.body : [];
                    });
                    kiemKeCcdcService.getAllToolsByTiAuditID(this.itiAudit.id).subscribe(res => {
                        this.listTools = res.body;
                        for (let i = 0; i < this.itiAuditDetail.length; i++) {
                            this.itiAuditDetail[i].toolsItem = this.listTools.find(y => y.toolsID === this.itiAuditDetail[i].toolsID);
                            if (this.itiAuditDetail[i].toolsItem) {
                                this.itiAuditDetail[i].toolsName = this.itiAuditDetail[i].toolsItem.toolsName;
                            }
                        }
                        this.copy();
                    });
                    this.tiAuditMemberDetails = data.itiAudits.tiAuditMemberDetails;
                    if (this.isSoTaiChinh) {
                        this.no = this.itiAudit.noFBook;
                    } else if (this.sysTypeLedger === 1) {
                        this.no = this.itiAudit.noMBook;
                    }
                    if (this.dataSession && this.dataSession.isEdit) {
                        this.edit();
                    }
                } else {
                    this.checkData = false;
                    this.viewVouchersSelected = [];
                    this.isEdit = true;
                    this.itiAudit = {};
                    // this.itiAudit.postedDate = this.itiAudit.date;
                    this.itiAuditDetail = [];
                    // this.tiAllocationAllocateds = [];
                    this.itiAuditDetail = [];
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
        this.unitService.getUnits().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
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
        this.tiAllocationPostsCopy = [];
        // this.tiAllocationAllocatedsCopy = [];
        this.itiAuditDetailCopy = [];
        this.afterDeleteOrAddRow();
        this.registerRef();
        this.isSaving = false;
        this.getData();
        this.getAccount();
        this.sumAllList();
        // add by namnh
        if (window.location.href.includes('/from-g-other-voucher')) {
            this.isGOtherVoucher = true;
            this.searchVoucher = JSON.parse(sessionStorage.getItem('dataSearchKiemKe'));
            this.utilsService
                .getIndexRow({
                    id: this.itiAudit.id,
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

    get itiAudit() {
        return this._itiAudit;
    }

    set itiAudit(itiAudit: ITIAudit) {
        this._itiAudit = itiAudit;
    }

    getData() {
        // diễn giải
        this.autoPrinciplellService.getAllAutoPrinciples().subscribe((res: HttpResponse<IAutoPrinciple[]>) => {
            this.autoPrinciples = res.body;
            const gOtherVoucherReason: IAutoPrinciple = new AutoPrinciple();
            if (this.itiAudit.id) {
                gOtherVoucherReason.autoPrincipleName = this.itiAudit.description;
                if (this.autoPrinciples && !this.autoPrinciples.some(x => x.autoPrincipleName === this.itiAudit.description)) {
                    this.autoPrinciples.push(gOtherVoucherReason);
                }
            } else {
                if (this.objectDate) {
                    const dateItem = this.objectDate.date;
                    this.translate.get(['ebwebApp.tIAudit.titleDate']).subscribe((ref: any) => {
                        this.itiAudit.description = this.translate.instant('ebwebApp.tIAudit.titleDate', {
                            date: dateItem
                                ? dateItem instanceof moment
                                    ? dateItem.format(DATE_FORMAT_SLASH)
                                    : moment(dateItem).format(DATE_FORMAT_SLASH)
                                : ''
                        });
                    });
                }
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
                this.utilService.isEquivalent(this.itiAudit, this.itiAuditCopy) &&
                this.utilService.isEquivalentArray(this.itiAuditDetail, this.itiAuditDetailCopy)
            );
        }
    }

    addNewRow(eventData: any, checkAllocation: number) {
        if (checkAllocation === 1) {
            if (!this.tiAuditMemberDetails) {
                this.tiAuditMemberDetails = [];
            }
            this.tiAuditMemberDetails.push({});
        } else {
            if (!this.itiAuditDetail) {
                this.itiAuditDetail = [];
            }
            this.itiAuditDetail.push({});
        }
        this.changeAllocationAmount();

        const nameTag: string = event.srcElement.id;
        const index: number = checkAllocation === 1 ? this.tiAuditMemberDetails.length - 1 : this.itiAuditDetail.length - 1;
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
            // this.tiAllocationPosts.splice(index, 0, Object.assign({}, this.tiAllocationPosts[index]));
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

    // checkAllocation : 0 tab phân bổ 1 tab member
    keyPress(index: number, checkAllocation, action: string) {
        if (action === 'CTRL + DELETE') {
            if (index !== null && index >= 0) {
                if (checkAllocation === 1) {
                    const idByIndex = '';
                    this.tiAuditMemberDetails.splice(index, 1);
                } else if (checkAllocation === 0) {
                    this.itiAuditDetail.splice(index, 1);
                }
            }
        } else {
            if (index !== null && index >= 0) {
                if (checkAllocation === 1) {
                    const item = Object.assign({}, this.tiAuditMemberDetails[index]);
                    item.id = null;
                    this.tiAuditMemberDetails.splice(index, 0, item);
                } else if (checkAllocation === 0) {
                    const item = Object.assign({}, this.itiAuditDetail[index]);
                    item.id = null;
                    this.itiAuditDetail.splice(index, 0, item);
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
        this.itiAuditCopy = Object.assign({}, this.itiAudit);
        this.noCopy = this.no;
        if (this.tiAllocationPosts) {
            this.tiAllocationPostsCopy = this.tiAllocationPosts.map(object => ({ ...object }));
        }
        // if (this.tiAllocationAllocateds) {
        //     this.tiAllocationAllocatedsCopy = this.tiAllocationAllocateds.map(object => ({ ...object }));
        // }
        if (this.itiAuditDetail) {
            this.itiAuditDetailCopy = this.itiAuditDetail.map(object => ({ ...object }));
        }
    }

    getPrepaidExpenseAllocation() {
        if (this.objectDate) {
            this.itiAudit.inventoryDate = this.itiAudit.date;
            // this.itiAudit.postedDate = moment(new Date(this.objectDate.year, this.objectDate.month, 0));
            const dateSearch = this.itiAudit.date;
            this.kiemKeCcdcService
                .getTIAuditCount({
                    date: dateSearch
                        ? dateSearch instanceof moment
                            ? dateSearch.format(DATE_FORMAT_SEARCH)
                            : moment(dateSearch).format(DATE_FORMAT_SEARCH)
                        : ''
                })
                .subscribe(res => {
                    this.listTools = [];
                    // this.tiAllocationAllocateds = [];
                    this.tiAllocationPosts = [];
                    this.itiAuditDetail = res.body;
                    for (let i = 0; i < this.itiAuditDetail.length; i++) {
                        const item = {
                            id: this.itiAuditDetail[i].toolsID,
                            toolsCode: this.itiAuditDetail[i].toolsCode,
                            toolsName: this.itiAuditDetail[i].toolsName
                        };
                        this.listTools.push(item);
                        this.itiAuditDetail[i].toolsItem = item;
                        this.itiAuditDetail[i].toolsName = item.toolsName;
                        this.itiAuditDetail[i].quantityOnBook = this.itiAuditDetail[i].quantity;
                        this.itiAuditDetail[i].quantityInventory = this.itiAuditDetail[i].quantityOnBook;
                        this.itiAuditDetail[i].diffQuantity =
                            (this.itiAuditDetail[i].quantityInventory ? this.itiAuditDetail[i].quantityInventory : 0) -
                            (this.itiAuditDetail[i].quantityOnBook ? this.itiAuditDetail[i].quantityOnBook : 0);
                        this.itiAuditDetail[i].executeQuantity = this.itiAuditDetail[i].diffQuantity;
                        this.itiAuditDetail[i].recommendation =
                            this.itiAuditDetail[i].executeQuantity > 0
                                ? this.itiAuditDetail[i].diffQuantity < 0 ? 1 : this.itiAuditDetail[i].diffQuantity !== 0 ? 0 : 2
                                : 2;
                    }
                    this.translate.get(['ebwebApp.tIAudit.titleDate']).subscribe((ref: any) => {
                        this.itiAudit.description = this.translate.instant('ebwebApp.tIAudit.titleDate', {
                            date: this.itiAudit.date
                                ? this.itiAudit.date instanceof moment
                                    ? this.itiAudit.date.format(DATE_FORMAT_SLASH)
                                    : moment(this.itiAudit.date).format(DATE_FORMAT_SLASH)
                                : ''
                        });
                    });
                    this.sumAllList();
                    this.copy();
                });
        }
        this.changeAllocationAmount();
    }

    getSessionData() {
        this.objectDate = JSON.parse(sessionStorage.getItem('objectDateTIAudit'));
        // lấy chi tiết chi phí
        this.itiAudit.date = this.objectDate.date ? moment(this.objectDate.date) : '';
        this.getPrepaidExpenseAllocation();
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
            if (this.itiAudit && this.itiAudit.id && this.tiAuditMemberDetails && this.tiAuditMemberDetails.length) {
                for (let i = 0; i < this.tiAuditMemberDetails.length; i++) {
                    this.tiAuditMemberDetails[i].accountingObject = this.accountingObjects.find(
                        y => y.id === this.tiAuditMemberDetails[i].accountingObjectID
                    );
                }
            }
            console.log(this.employees);
        });
    }

    // selectEmployeeMC(mCAuditDetailMember: MCAuditDetailMember) {
    //     mCAuditDetailMember.accountingObjectName = mCAuditDetailMember.accountingObject.accountingObjectName;
    //     mCAuditDetailMember.accountingObjectTitle = mCAuditDetailMember.accountingObject.contactTitle;
    //     if (mCAuditDetailMember.accountingObject.departmentId !== null) {
    //         this.branchIDService
    //             .find(mCAuditDetailMember.accountingObject.departmentId)
    //             .subscribe((res: HttpResponse<IOrganizationUnit>) => {
    //                 mCAuditDetailMember.organizationUnit = res.body;
    //             });
    //     } else {
    //         mCAuditDetailMember.organizationUnit = null;
    //     }
    // }

    // kiểm kê công cụ dụng cụ
    selectEmployeeMC(detail: ITIAuditMemberDetails) {
        if (detail.accountingObject) {
            detail.accountObjectName = detail.accountingObject.accountingObjectName;
            detail.accountingObjectTitle = detail.accountingObject.contactTitle;
            detail.departmentID = detail.accountingObject.departmentId;
            detail.accountingObjectID = detail.accountingObject.id;
        }
        // if (detail.accountingObject.departmentId !== null) {
        //     this.branchIDService
        //         .find(detail.accountingObject.departmentId)
        //         .subscribe((res: HttpResponse<IOrganizationUnit>) => {
        //             detail.organizationUnit = res.body;
        //         });
        // } else {
        //     detail.organizationUnit = null;
        // }
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
        if (this.itiAudit.description) {
            for (let i = 0; i < this.tiAllocationPosts.length; i++) {
                this.tiAllocationPosts[i].description = this.itiAudit.description;
            }
        }
    }

    changeDate() {
        if (this.itiAudit.date) {
            // this.itiAudit.postedDate = this.itiAudit.date;
        }
    }

    changeReason(description: string) {
        // if (description) {
        //     for (let i = 0; i < this.tiAllocationPosts.length; i++) {
        //         this.tiAllocationPosts[i].description = description;
        //     }
        // }
    }

    getNoBook() {
        this.utilService
            .getGenCodeVoucher({
                typeGroupID: 57, // typeGroupID loại chứng từ
                companyID: '', // ID công ty
                branchID: '', // ID chi nhánh
                displayOnBook: this.sysTypeLedger // 0 - sổ tài chính, 1 - sổ quản trị
            })
            .subscribe((res: HttpResponse<string>) => {
                // this.mCReceipt.noFBook = (res.body.toString());
                if (!this.checkData) {
                    this.no = res.body;
                    if (this.isSoTaiChinh) {
                        this.itiAudit.noFBook = this.no;
                    } else {
                        this.itiAudit.noMBook = this.no;
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
        // this.itiAudit.tiAllocationAllocateds = this.tiAllocationAllocateds;
        // this.itiAudit.tiAllocationDetails = this.itiAuditDetail;
        // this.itiAudit.tiAllocationPosts = this.tiAllocationPosts;
        // this.itiAudit.tiAuditDetails = this.itiAuditDetail;
        // this.itiAudit.tiAuditMemberDetails = this.tiAuditMemberDetails;
        if (this.isSoTaiChinh) {
            this.itiAudit.noFBook = this.no;
        } else {
            this.itiAudit.noMBook = this.no;
        }
        this.tiAuditMemberDetails = this.tiAuditMemberDetails ? this.tiAuditMemberDetails : [];
        this.itiAuditDetail = this.itiAuditDetail ? this.itiAuditDetail : [];
        const lengthMax =
            this.tiAuditMemberDetails && this.tiAuditMemberDetails.length > this.tiAuditMemberDetails && this.itiAuditDetail.length
                ? this.tiAuditMemberDetails.length
                : this.itiAuditDetail.length;
        for (let i = 0; i < lengthMax; i++) {
            if (i < this.itiAuditDetail.length) {
                this.itiAuditDetail[i].orderPriority = i;
            }
            if (i < this.tiAuditMemberDetails.length) {
                this.tiAuditMemberDetails[i].orderPriority = i;
            }
        }
        this.itiAudit.tiAuditDetails = this.itiAuditDetail;
        this.itiAudit.tiAuditMemberDetails = this.tiAuditMemberDetails;
        this.itiAudit.viewVouchers = this.viewVouchersSelected;
        if (this.itiAudit.id) {
            this.kiemKeCcdcService.update(this.itiAudit).subscribe(
                res => {
                    this.copy();
                    this.isCloseAll = true;
                    this.toastr.success(
                        this.translate.instant('ebwebApp.mBDeposit.editSuccess'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    console.log(res.body);
                    if (check) {
                        this.isCloseAll = true;
                        this.isEdit = false;
                        this.addNew(event);
                    } else {
                        this.objectAfterSave = {
                            tiDecrementID: res.body.tiDecrementID,
                            tiIncrementID: res.body.tiIncrementID,
                            noBookDecrement: res.body.noBookDecrement,
                            noBookIncrement: res.body.noBookIncrement
                        };
                        if (this.modalRef) {
                            this.modalRef.close();
                        }
                        if (this.objectAfterSave.tiDecrementID || this.objectAfterSave.tiIncrementID) {
                            this.modalRef = this.modalService.open(this.afterSaveModal, { backdrop: 'static' });
                        }
                        this.isEdit = false;
                        this.router.navigate(['./kiem-ke-ccdc', res.body.id, 'edit']);
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
            this.kiemKeCcdcService.create(this.itiAudit).subscribe(
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
                        this.objectAfterSave = {
                            tiDecrementID: res.body.tiDecrementID,
                            tiIncrementID: res.body.tiIncrementID,
                            noBookDecrement: res.body.noBookDecrement,
                            noBookIncrement: res.body.noBookIncrement
                        };
                        if (this.modalRef) {
                            this.modalRef.close();
                        }
                        if (this.objectAfterSave.tiDecrementID || this.objectAfterSave.tiIncrementID) {
                            this.modalRef = this.modalService.open(this.afterSaveModal, { backdrop: 'static' });
                        }
                        this.router.navigate(['./kiem-ke-ccdc', res.body.id, 'edit']);
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
        if (this.itiAuditCopy) {
            if (
                this.no !== this.noCopy ||
                !this.utilService.isEquivalent(this.itiAudit, this.itiAuditCopy) ||
                !this.utilService.isEquivalent(this.itiAuditDetail, this.itiAuditDetailCopy)
                // ||
                // !this.utilService.isEquivalentArray(this.tiAllocationPosts, this.tiAllocationPostsCopy) ||
                // !this.utilService.isEquivalentArray(this.tiAllocationAllocateds, this.tiAllocationAllocatedsCopy) ||
                // !this.utilService.isEquivalentArray(this.itiAuditDetail, this.itiAuditDetailCopy)
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
            this.router.navigate(['/kiem-ke-ccdc', 'hasSearch', '1']);
        } else if (window.location.href.includes('/from-g-other-voucher')) {
            if (JSON.parse(sessionStorage.getItem('dataSearchKiemKe')) !== null) {
                this.router.navigate(['/g-other-voucher', 'hasSearch']);
            } else {
                this.router.navigate(['/g-other-voucher']);
            }
        } else {
            this.router.navigate(['/kiem-ke-ccdc']);
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
                this.router.navigate(['/kiem-ke-ccdc', 'hasSearch', '1']);
            } else {
                this.router.navigate(['/kiem-ke-ccdc']);
            }
        }
    }

    changeAmount(detail: GOtherVoucherDetails, i: number) {
        detail.amountOriginal = detail.amount;
        this.tiAllocationPosts[i].amount = detail.amount;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_Sua])
    edit() {
        event.preventDefault();
        if (!this.isEdit && !this.checkCloseBook(this.currentAccount, this.itiAudit.date) && !this.utilsService.isShowPopup) {
            this.isEdit = true;
            this.checkRecommendation = false;
            const count = this.itiAuditDetail.some(x => x.recommendation === 1 || x.recommendation === 0);
            if (count) {
                this.checkRecommendation = false;
                this.modalRef = this.modalService.open(this.deleteModalRelationShip, { backdrop: 'static' });
            }
            this.copy();
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.PhanBoChiPhiTRaTruoc_GhiSo])
    record() {
        event.preventDefault();
        if (!(this.itiAudit || this.checkCloseBook(this.currentAccount, this.itiAudit.date)) && !this.utilsService.isShowPopup) {
            if (!this.utilsService.isShowPopup) {
                if (this.itiAudit.id) {
                    this.record_ = {};
                    this.record_.id = this.itiAudit.id;
                    this.record_.typeID = TypeID.KIEM_KE_CCDC;
                    if (true) {
                        this.gLService.record(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                            if (res.body.success) {
                                // this.itiAudit.recorded = true;
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
        if (!(this.isEdit || this.checkCloseBook(this.currentAccount, this.itiAudit.date))) {
            if (true) {
                if (this.itiAudit.id) {
                    this.record_ = {};
                    this.record_.id = this.itiAudit.id;
                    this.record_.typeID = TypeID.KIEM_KE_CCDC;
                    this.gLService.unrecord(this.record_).subscribe((res: HttpResponse<Irecord>) => {
                        if (res.body.success) {
                            // this.itiAudit.recorded = false;
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
        if (this.itiAudit && this.itiAudit.id) {
            this.kiemKeCcdcService.delete(this.itiAudit.id).subscribe(
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
            this.kiemKeCcdcService
                .getCustomerReport({
                    id: this.itiAudit.id,
                    typeID: TypeID.KIEM_KE_CCDC,
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
                // index = this.tiAllocationAllocateds.indexOf(ref.content);
            } else if (this.checkAllocation === 0) {
                index = this.tiAllocationPosts.indexOf(ref.content);
            }
            index = index + 1;
            // this.tiAllocationAllocateds.splice(index, 0, Object.assign({}, {}));
            // this.tiAllocationPosts.splice(index, 0, Object.assign({}, {}));
            // this.tiAllocationAllocateds[index].id = null;
            this.tiAllocationPosts[index].id = null;
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
            //     for (let i = 0; i < this.itiAuditDetail.length; i++) {
            //         if (this.itiAuditDetail[i].prepaidExpenseID ===  this.tiAllocationAllocateds[index].prepaidExpenseID) {
            //             this.itiAuditDetail.splice(i, 1);
            //             break;
            //         }
            //     }
            // }
            // this.tiAllocationAllocateds.splice(index, 1);
            this.tiAllocationPosts.splice(index, 1);
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
            if (!this.itiAudit.date) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.postedDate'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
            // if (moment(this.itiAudit.postedDate) < moment(this.itiAudit.date)) {
            //     this.toastr.error(this.translate.instant('ebwebApp.common.error.dateAndPostDate'));
            //     return false;
            // }
            for (let i = 0; i < this.itiAuditDetail.length; i++) {
                const diffQuantityItem =
                    this.itiAuditDetail[i].diffQuantity < 0
                        ? this.itiAuditDetail[i].diffQuantity * -1
                        : this.itiAuditDetail[i].diffQuantity;
                if (this.itiAuditDetail[i].executeQuantity > diffQuantityItem) {
                    this.toastr.error(this.translate.instant('ebwebApp.tIAuditDetails.error.executeQuantity'));
                    return;
                }
                this.itiAuditDetail[i].orderPriority = i;
            }
            if (this.isEdit && !this.utilsService.isShowPopup) {
                this.checkSaveBoolean = check;
                if (!this.itiAuditDetail || this.itiAuditDetail.length <= 0) {
                    this.toastr.error(this.translate.instant('ebwebApp.pPDiscountReturn.notPPDiscountReturn'));
                    return;
                }
                if (this.checkCloseBook(this.currentAccount, this.itiAudit.date)) {
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
                // this.itiAudit.totalAmount = 0;
                // this.itiAudit.totalAmountOriginal = 0;
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
                //         // this.itiAudit.totalAmountOriginal +=  this.tiAllocationPosts[i].amount;
                //         this.itiAudit.totalAmount +=  this.tiAllocationPosts[i].amount;
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
        return this.kiemKeCcdcService
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
                    this.router.navigate(['./kiem-ke-ccdc', res.body.id, 'edit']);
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
        if (!this.isEdit && !this.utilsService.isShowPopup) {
            if (this.itiAudit.id) {
                this.checkRecommendation = false;
                const count = this.itiAuditDetail.some(x => x.recommendation === 1 || x.recommendation === 0);
                if (count) {
                    this.checkRecommendation = true;
                    this.modalRef = this.modalService.open(this.deleteModalRelationShip, { backdrop: 'static' });
                }
                // this.kiemKeCcdcService.getMaxMonth(this.itiAudit.id).subscribe(res => {
                //     if (moment(this.itiAudit.date).isSame(moment(res.body))) {
                //         this.modalRef = this.modalService.open(this.deleteModal, { backdrop: 'static' });
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
    deleteRelationShip() {
        if (this.itiAudit && this.itiAudit.id) {
            this.kiemKeCcdcService.deleteRelationShip(this.itiAudit.id).subscribe(
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
    }
    changeTools(detail: any) {
        if (detail.toolsItem) {
            // const prepaidItem = this.tiAuditDetails.find(i => i. === detail.prepaidExpenseID);
            // detail.prepaidExpenseName = prepaidItem.prepaidExpenseCode;
            // detail.allocationAmount = prepaidItem.allocationAmount;
            detail.ToolsID = detail.toolsItem.id;
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
        this.sumQuantityOnBook = 0;
        this.sumQuantityInventory = 0;
        this.sumDiffQuantity = 0;
        this.sumExecuteQuantity = 0;
        for (let i = 0; i < this.itiAuditDetail.length; i++) {
            if (this.itiAuditDetail[i].quantityOnBook) {
                this.sumQuantityOnBook += this.itiAuditDetail[i].quantityOnBook;
            }
            if (this.itiAuditDetail[i].quantityInventory) {
                this.sumQuantityInventory += this.itiAuditDetail[i].quantityInventory;
            }
            if (this.itiAuditDetail[i].diffQuantity) {
                this.sumDiffQuantity += this.itiAuditDetail[i].diffQuantity;
            }
            if (this.itiAuditDetail[i].executeQuantity) {
                this.sumExecuteQuantity += this.itiAuditDetail[i].executeQuantity;
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
                    id: this.itiAudit.id,
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
                    id: this.itiAudit.id,
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
                this.router.navigate(['./kiem-ke-ccdc', iGOtherVoucher.id, 'edit', 'from-g-other-voucher']);
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
        this.details = this.tiAllocationPosts;
    }

    addDataToDetail() {
        this.tiAllocationPosts = this.details ? this.details : this.tiAllocationPosts;
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

    getToolsActive() {
        // this.ghiTangService.getToolsActive().subscribe(res => {
        //     this.listTools = res.body;
        //     if (this.itiAudit && this.itiAudit.id) {
        //         if (this.itiAuditDetail) {
        //             this.itiAuditDetail.forEach(item => {
        //                 item.toolsItem = this.listTools.find(x => x.id === item.toolsID);
        //                 if (item.toolsItem) {
        //                     item.toolsName = item.toolsItem.toolsName;
        //                 }
        //             });
        //         }
        //     }
        // });
    }

    changeQuantity(detail: any) {
        detail.diffQuantity =
            (detail.quantityInventory ? detail.quantityInventory : 0) - (detail.quantityOnBook ? detail.quantityOnBook : 0);
        detail.recommendation = detail.executeQuantity > 0 ? (detail.diffQuantity < 0 ? 1 : detail.diffQuantity !== 0 ? 0 : 2) : 2;
        this.sumAllList();
    }

    changeDiffQuantity(detail: any) {
        detail.recommendation = detail.executeQuantity > 0 ? (detail.diffQuantity < 0 ? 1 : detail.diffQuantity !== 0 ? 0 : 2) : 2;
        this.sumAllList();
    }

    afterSaveClick(check: boolean) {
        let url = '';
        if (check) {
            url = `/#/ghi-tang-ccdc/${this.objectAfterSave.tiIncrementID}/edit`;
        } else {
            url = `/#/ghi-giam-ccdc/${this.objectAfterSave.tiDecrementID}/edit`;
        }
        window.open(url, '_blank');
    }
}
