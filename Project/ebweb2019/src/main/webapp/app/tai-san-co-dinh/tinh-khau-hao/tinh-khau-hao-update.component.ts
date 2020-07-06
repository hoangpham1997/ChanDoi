import { AfterViewChecked, AfterViewInit, Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IMCReceipt } from 'app/shared/model/mc-receipt.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { CostSetService } from 'app/entities/cost-set';
import { ICostSet } from 'app/shared/model/cost-set.model';
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
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { BudgetItemService } from 'app/entities/budget-item';
import { Principal } from 'app/core';
import {
    CategoryName,
    DDSo_NgoaiTe,
    DDSo_TienVND,
    DDSo_TyLe,
    GROUP_TYPEID,
    REPORT,
    SO_LAM_VIEC,
    SU_DUNG_SO_QUAN_TRI,
    TCKHAC_GhiSo,
    TypeID
} from 'app/app.constants';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { AccountListService } from 'app/danhmuc/account-list';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { IMCReceiptDetailCustomer } from 'app/shared/model/mc-receipt-detail-customer.model';
import { ROLE } from 'app/role.constants';
import { ToolsService } from 'app/entities/tools';
import { UnitService } from 'app/danhmuc/unit';
import { ExportPrepaidExpenseVoucherModalService } from 'app/core/login/prepaid-expense-voucher-modal.service';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { TIIncrementDetailRefVoucher } from 'app/shared/model/ti-increment-detail-ref-voucher.model';
import { IFAIncrement } from 'app/shared/model/fa-increment.model';
import { IFAIncrementDetails } from 'app/shared/model/fa-increment-details.model';
import { GhiTangService } from 'app/tai-san-co-dinh/ghi-tang/ghi-tang.service';
import { IFixedAsset } from 'app/shared/model/fixed-asset.model';

@Component({
    selector: 'eb-tinh-khau-hao-update',
    templateUrl: './tinh-khau-hao-update.component.html',
    styleUrls: ['./tinh-khau-hao-update.component.css']
})
export class TinhKhauHaoUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewChecked, AfterViewInit {
    @ViewChild('content') public modalComponent: NgbModalRef;
    @ViewChild('deleteItem') deleteItemModal: TemplateRef<any>;
    GROUP_ID = GROUP_TYPEID;
    TYPE_ID = TypeID;
    TCKHAC_GhiSo: boolean;
    account: any;
    private _FAIncrement: IFAIncrement;
    faIncrementDetail: IFAIncrementDetails[];
    isSaving: boolean;

    autoPrinciples: IAutoPrinciple[];
    autoPrinciple: IAutoPrinciple;
    expenseItems: IExpenseItem[]; // Khoản mục chi phí
    dateDp: any;
    postedDateDp: any;
    isRecord: boolean;
    eMContracts: IEMContract[];
    statusVoucher: number;
    currentRow: number;
    // region tiến lùi chứng từ
    rowNum: any;
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
    creditAccountList: any;
    debitAccountList: any;
    vatAccountList: any;
    no: string;
    viewVouchersSelectedCopy: any;
    soDangLamViec: any;

    isCloseAll: boolean;
    isSaveAndNew: boolean;
    contextMenu: ContextMenu;
    select: number;
    isViewFromRef: boolean;

    DDSo_NgoaiTe = DDSo_NgoaiTe;
    DDSo_TienVND = DDSo_TienVND;
    DDSo_TyLe = DDSo_TyLe;

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

    reasonDefault = 'Ghi tăng TSCD';
    nameCategory: any;
    Report = REPORT;
    CategoryName = CategoryName;
    indexFocusDetailCol: any;
    indexFocusDetailRow: any;
    listIDInputDeatil: any[] = [
        'fixedAssetCodeHT',
        'fixedAssetNameHT',
        'departmentHT',
        'amountHT',
        'fixedAssetCodeTK',
        'accountingObject',
        'expenseItems',
        'costSet',
        'contract',
        'budgetItem',
        'department',
        'statisticsCodes'
    ];
    tools: any[];
    faIncrementDetailsCopy: any[];
    faIncrementCopy: any;
    itemsPerPage: any;
    page: number;
    faIncrementDetailRefVoucher: any[];
    sumQuantity: number;
    sumUnitPrice: number;
    sumAllAmount: number;
    sumTotalAmountOriginal: any;
    tools2: any;
    fixedAssets: IFixedAsset[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private ghiTangService: GhiTangService,
        private toolsService: ToolsService,
        private viewVoucherService: ViewVoucherService,
        private accountingObjectService: AccountingObjectService,
        private activatedRoute: ActivatedRoute,
        private autoPrinciplellService: AutoPrincipleService,
        private currencyService: CurrencyService,
        private organizationUnitService: OrganizationUnitService,
        private costSetService: CostSetService,
        private expenseItemService: ExpenseItemService,
        private gLService: GeneralLedgerService,
        public utilsService: UtilsService,
        private statisticsCodeService: StatisticsCodeService,
        private eMContractService: EMContractService,
        private router: Router,
        private toast: ToastrService,
        public translate: TranslateService,
        private bankAccountDetailsService: BankAccountDetailsService,
        private budgetItemService: BudgetItemService,
        private principal: Principal,
        private refModalService: RefModalService,
        private eventManager: JhiEventManager,
        private exportPrepaidExpenseVoucherModalService: ExportPrepaidExpenseVoucherModalService,
        private accountListService: AccountListService,
        private unitService: UnitService,
        private modalService: NgbModal
    ) {
        super();
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.searchVoucher = JSON.parse(sessionStorage.getItem('searchVoucherGhiTangTSCD')); // Dùng cho tiến lùi chứng từ với danh sách tìm kiếm
        if (this.searchVoucher) {
            this.rowNum = this.searchVoucher.rowNum + 1;
        }
        this.autoPrinciplellService.getAutoPrinciples().subscribe((res: HttpResponse<IAutoPrinciple[]>) => {
            this.autoPrinciples = res.body.filter(n => n.typeId === this.TYPE_ID.TSCD_GHI_TANG || n.typeId === 0).sort((n1, n2) => {
                if (n1.typeId > n2.typeId) {
                    return 1;
                }
                if (n1.typeId < n2.typeId) {
                    return -1;
                }
                return 0;
            });
        });
        this.expenseItemService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body.sort((a, b) => a.expenseItemCode.localeCompare(b.expenseItemCode));
        });
        this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body.sort((a, b) => a.costSetCode.localeCompare(b.costSetCode));
        });
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticCodes = res.body.sort((a, b) => a.statisticsCode.localeCompare(b.statisticsCode));
        });
        this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
            this.eMContracts = res.body;
        });
        this.budgetItemService.getBudgetItems().subscribe((res: HttpResponse<IBudgetItem[]>) => {
            this.budgetItems = res.body.sort((a, b) => a.budgetItemCode.localeCompare(b.budgetItemCode));
        });
        this.accountingObjectService.getAccountingObjectsIsActive().subscribe(res => {
            this.accountingObjects = res.body;
        });
        this.contextMenu = new ContextMenu();
        this.isViewFromRef = window.location.href.includes('/from-ref');
    }

    getHopDong() {
        this.eMContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
            this.eMContracts = res.body.sort((a, b) =>
                (this.isSoTaiChinh ? a.noFBook : a.noMBook).localeCompare(this.isSoTaiChinh ? b.noFBook : b.noMBook)
            );
        });
    }

    ngOnInit() {
        // this.getDataPopup();
        this.faIncrementDetailRefVoucher = [];
        this.faIncrementDetail = [];
        this.isSaving = false;
        this.activatedRoute.data.subscribe(data => {
            this.faIncrement = data.faIncrement ? data.faIncrement : {};
            if (this.faIncrement.id !== undefined) {
                // this.sumAllList();
                this.faIncrementDetail = this.faIncrement.faIncrementDetails;
                this.faIncrementDetail.sort(x => x.orderPriority);
                this.ghiTangService.findDetailsByID(this.faIncrement.id).subscribe(res => {
                    this.faIncrementDetailRefVoucher =
                        res.body.faIncrementDetailRefVoucherConvertDTOS === undefined
                            ? []
                            : res.body.faIncrementDetailRefVoucherConvertDTOS;
                });

                if (this.account) {
                    this.showVaoSo = this.account.systemOption.some(x => x.code === SU_DUNG_SO_QUAN_TRI && x.data === '1');
                    this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                    if (this.isSoTaiChinh !== undefined) {
                        this.getHopDong();
                    }
                    this.no = this.isSoTaiChinh ? this.faIncrement.noFBook : this.faIncrement.noMBook;
                    this.soDangLamViec = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                    this.TCKHAC_GhiSo = this.account.systemOption.some(x => x.code === TCKHAC_GhiSo && x.data === '0');
                    // this.currencyCode = this.account.organizationUnit.currencyID;
                }

                this.statusVoucher = 1;
                // this.accountingObjectNameOld = this.faIncrement.accountingObjectName ? this.faIncrement.accountingObjectName : '';
                this.isEdit = false; // Xem chứng từ
                this.isRecord = this.faIncrement.recorded === undefined ? false : this.faIncrement.recorded;

                // this.sumAllList();
            } else {
                this.no = null;
                this.faIncrementDetail = [];
                this.faIncrement.reason = this.reasonDefault;
                this.faIncrement.totalAmount = 0;
                // this.accountingObjectNameOld = this.faIncrement.accountingObjectName ? this.faIncrement.accountingObjectName : '';
                this.statusVoucher = 0;
                this.isEdit = true; // thêm mới chứng từ
                this.faIncrement.totalAmount = 0;
                this.translate.get(['ebwebApp.tscd.home.ghiTang']).subscribe((ref: any) => {
                    this.reasonDefault = this.translate.instant('ebwebApp.tscd.home.ghiTang');
                    this.faIncrement.reason = this.translate.instant('ebwebApp.tscd.home.ghiTang');
                });
                // Set default theo systemOption
                if (this.account) {
                    this.showVaoSo = this.account.systemOption.some(x => x.code === SU_DUNG_SO_QUAN_TRI && x.data === '1');
                    this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                    if (this.isSoTaiChinh !== undefined) {
                        this.getHopDong();
                    }
                    this.TCKHAC_GhiSo = this.account.systemOption.some(x => x.code === TCKHAC_GhiSo && x.data === '0');
                    this.faIncrement.date = this.utilsService.ngayHachToan(this.account);
                    /*this.changePostedDate();*/
                    this.changeDate();
                    this.soDangLamViec = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                    if (this.isSoTaiChinh) {
                        if (this.showVaoSo) {
                            this.faIncrement.typeLedger = 2;
                        } else {
                            this.faIncrement.typeLedger = 0;
                        }
                    } else {
                        this.faIncrement.typeLedger = 2;
                    }
                    this.utilsService
                        .getGenCodeVoucher({
                            typeGroupID: this.GROUP_ID.GROUP_TSCD_GHI_TANG, // typeGroupID loại chứng từ
                            displayOnBook: this.soDangLamViec // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
                        })
                        .subscribe((res: HttpResponse<string>) => {
                            console.log(res.body);
                            this.no = res.body;
                            // this.copy();
                        });
                }
            }
            this.getTSCD();
            this.getOrganizationUnits();
        });

        this.getDataPopup();
        this.registerCombobox();
        this.registerAddNewRow();
        this.registerDeleteRow();
        this.registerCopyRow();
    }

    getDataPopup() {
        this.eventManager.subscribe('selectPrepaidExpenseVoucher', ref => {
            this.faIncrementDetailRefVoucher = [];
            this.faIncrementDetailRefVoucher = ref.content;
            this.convertRefVouchers();
        });
    }

    convertRefVouchers() {
        if (this.faIncrementDetailRefVoucher && this.faIncrementDetailRefVoucher.length > 0) {
            for (let j = 0; j < this.faIncrementDetailRefVoucher.length; j++) {
                this.faIncrementDetailRefVoucher[j].id = null;
                if (this.faIncrementDetailRefVoucher[j].refID1) {
                    this.faIncrementDetailRefVoucher[j].refVoucherID = this.faIncrementDetailRefVoucher[j].refID1;
                }
                this.faIncrementDetailRefVoucher[j].orderPriority = j;
            }
        }
    }

    closeForm() {
        if (this.faIncrementCopy && (this.statusVoucher === 0 || this.statusVoucher === 1)) {
            if (
                !this.utilsService.isEquivalent(this.faIncrement, this.faIncrementCopy) ||
                !this.utilsService.isEquivalentArray(this.faIncrementDetail, this.faIncrementDetailsCopy) ||
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
            if (sessionStorage.getItem('index_GhiTang-tscd')) {
                this.rowNum = sessionStorage.getItem('index_GhiTang-tscd');
            } else {
                this.router.navigate(['/ghi-tang-tscd', 'hasSearch', '1']);
            }
        } else {
            if (sessionStorage.getItem('page_GhiTang')) {
                this.router.navigate(['/ghi-tang-tscd'], {
                    queryParams: {
                        page: sessionStorage.getItem('page_GhiTang'),
                        size: sessionStorage.getItem('size_GhiTang'),
                        index: sessionStorage.getItem('index_GhiTang-tscd')
                    }
                });
            } else {
                this.router.navigate(['/ghi-tang-tscd']);
            }
        }
    }

    //  @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Sua, ROLE.PhieuThu_Them])
    save() {
        event.preventDefault();

        this.fillToSave();
        if (this.checkError()) {
            if (this.faIncrement.id !== undefined) {
                this.subscribeToSaveResponse(this.ghiTangService.update(this.faIncrement));
            } else {
                this.subscribeToSaveResponse(this.ghiTangService.create(this.faIncrement));
            }
        }
    }

    fillToSave() {
        this.faIncrement.faIncrementDetailRefVouchers = this.faIncrementDetailRefVoucher;
        if (this.faIncrement.typeLedger.toString() === '0') {
            this.faIncrement.noFBook = this.no;
            this.faIncrement.noMBook = null;
        } else if (this.faIncrement.typeLedger.toString() === '1') {
            this.faIncrement.noMBook = this.no;
            this.faIncrement.noFBook = null;
        } else {
            if (this.isSoTaiChinh) {
                this.faIncrement.noFBook = this.no;
            } else {
                this.faIncrement.noMBook = this.no;
            }
        }
        if (!this.faIncrement.typeID) {
            this.faIncrement.typeID = this.TYPE_ID.TSCD_GHI_TANG;
        }
        this.isSaving = true;
        // this.faIncrement.totalVATAmount = 0;
        // this.faIncrement.totalVATAmountOriginal = 0;
        this.faIncrement.faIncrementDetails = this.faIncrementDetail;
        if (this.faIncrement && this.faIncrement.faIncrementDetails.length > 0) {
            for (let i = 0; i < this.faIncrement.faIncrementDetails.length; i++) {
                this.faIncrement.faIncrementDetails[i].orderPriority = i;
                if (!this.faIncrement.faIncrementDetails[i].quantity) {
                    this.faIncrement.faIncrementDetails[i].quantity = 0;
                }
                if (!this.faIncrement.faIncrementDetails[i].unitPrice) {
                    this.faIncrement.faIncrementDetails[i].unitPrice = 0;
                }
                if (!this.faIncrement.faIncrementDetails[i].amount) {
                    this.faIncrement.faIncrementDetails[i].amount = 0;
                }
            }
        }
    }

    //  @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Them])
    saveAndNew() {
        event.preventDefault();
        // this.save();
        if (this.statusVoucher === 0 && !this.utilsService.isShowPopup) {
            this.fillToSave();
            if (this.checkError()) {
                this.fillToSave();
                if (this.faIncrement.id !== undefined) {
                    this.subscribeToSaveResponseAndContinue(this.ghiTangService.update(this.faIncrement));
                } else {
                    this.subscribeToSaveResponseAndContinue(this.ghiTangService.create(this.faIncrement));
                }
                this.statusVoucher = 0;
                this.isEdit = true;
            }
        }
    }

    //  @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Them])
    copyAndNew() {
        event.preventDefault();
        this.faIncrement.id = undefined;
        this.faIncrement.recorded = false;
        this.no = null;
        this.faIncrement.noFBook = null;
        this.faIncrement.noMBook = null;
        this.isRecord = false;
        for (let i = 0; i < this.faIncrementDetail.length; i++) {
            this.faIncrementDetail[i].id = undefined;
        }
        if (this.faIncrementDetailRefVoucher) {
            for (let i = 0; i < this.faIncrementDetailRefVoucher.length; i++) {
                this.faIncrementDetailRefVoucher[i].id = undefined;
            }
        }
        this.utilsService
            .getGenCodeVoucher({
                typeGroupID: this.GROUP_ID.GROUP_TSCD_GHI_TANG, // typeGroupID loại chứng từ
                displayOnBook: this.soDangLamViec // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
            })
            .subscribe((res: HttpResponse<string>) => {
                this.no = res.body;
            });
        this.statusVoucher = 0;
        this.isEdit = true;
        this.faIncrementCopy = {};

        // this.copy();
    }

    //  @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Sua])
    edit() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.faIncrement.date) && !this.utilsService.isShowPopup) {
            if (this.statusVoucher === 1 && !this.faIncrement.recorded) {
                this.statusVoucher = 0;
                this.tools = this.tools2;
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
            if (this.faIncrement.id !== undefined) {
                this.subscribeToSaveResponseWhenClose(this.ghiTangService.update(this.faIncrement));
            } else {
                this.subscribeToSaveResponseWhenClose(this.ghiTangService.create(this.faIncrement));
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
        this.faIncrementCopy = Object.assign({}, this.faIncrement);
        this.faIncrementDetailsCopy = this.faIncrementDetail.map(object => ({ ...object }));
        // this.viewVouchersSelectedCopy = this.t.map(object => ({ ...object }));
    }

    //  @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Them])
    addNew($event = null) {
        event.preventDefault();
        this.router.navigate(['/ghi-tang-tscd', 'new']);
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body) {
                    this.onSaveSuccess();
                    this.faIncrement.id = res.body.id;
                    this.faIncrement.recorded = res.body.recorded;
                    this.router.navigate(['/ghi-tang-tscd', res.body.id, 'edit']);
                    this.faIncrementCopy = null;
                }
                // else if (res.body.status === 1) {
                //     this.noVoucherExist();
                // } else if (res.body.status === 2) {
                //     this.recordFailed();
                // }
            },
            (res: HttpErrorResponse) => {
                if (res.error.title === 'RSIBadRequest') {
                    this.toast.error(
                        this.translate.instant('global.data.noVocherAlreadyExist'),
                        this.translate.instant('ebwebApp.mCReceipt.home.message')
                    );
                }
                this.onSaveError();
            }
        );
    }

    private subscribeToSaveResponseAndContinue(result: Observable<HttpResponse<any>>) {
        this.isSaveAndNew = true;
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body) {
                    this.onSaveSuccess();
                    // this.faIncrement.id = res.body.id;
                    // this.faIncrement.recorded = res.body.recorded;
                    // this.router.navigate(['/ghi-tang-tscd', res.body.id, 'edit']).then(() => {
                    //     this.router.navigate(['/ghi-tang-tscd', 'new']);
                    //     this.isSaveAndNew = false;
                    // });
                }
                // else if (res.body.status === 1) {
                //     this.noVoucherExist();
                // } else if (res.body.status === 2) {
                //     this.recordFailed();
                // }
                this.addNew();
            },
            (res: HttpErrorResponse) => {
                if (res.error.title === 'RSIBadRequest') {
                    this.toast.error(
                        this.translate.instant('global.data.noVocherAlreadyExist'),
                        this.translate.instant('ebwebApp.mCReceipt.home.message')
                    );
                }
                this.onSaveError();
            }
        );
    }

    private subscribeToSaveResponseWhenClose(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body) {
                    this.onSaveSuccess();
                    this.closeAll();
                }
                // else if (res.body.status === 1) {
                //     this.noVoucherExist();
                // } else if (res.body.status === 2) {
                //     this.recordFailed();
                // }
            },
            (res: HttpErrorResponse) => {
                if (res.error.title === 'RSIBadRequest') {
                    this.toast.error(
                        this.translate.instant('global.data.noVocherAlreadyExist'),
                        this.translate.instant('ebwebApp.mCReceipt.home.message')
                    );
                }
                this.onSaveError();
            }
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.statusVoucher = 1;
        this.isEdit = false;
        this.toast.success(
            this.translate.instant('ebwebApp.mCReceipt.home.saveSuccess'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
        // this.previousState();
    }

    private noVoucherExist() {
        this.toast.error(
            this.translate.instant('global.data.noVocherAlreadyExist'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
    }

    private recordFailed() {
        this.toast.error(this.translate.instant('global.data.recordFailed'), this.translate.instant('ebwebApp.mCReceipt.home.message'));
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    get faIncrement() {
        return this._FAIncrement;
    }

    set faIncrement(faIncrement: IFAIncrement) {
        this._FAIncrement = faIncrement;
    }

    AddNewRow(isRightClick?) {
        let detailItem: IFAIncrementDetails;
        detailItem = {};
        detailItem.quantity = 0;
        detailItem.unitPrice = 0;
        detailItem.amount = 0;
        // this.faIncrementDetail.push(detailItem);
        let lenght = 0;
        if (isRightClick) {
            this.faIncrementDetail.splice(this.indexFocusDetailRow + 1, 0, detailItem);
            lenght = this.indexFocusDetailRow + 2;
        } else {
            this.faIncrementDetail.push(detailItem);
            lenght = this.faIncrementDetail.length;
        }

        if (isRightClick && this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
            const lst = this.listIDInputDeatil;
            const col = this.indexFocusDetailCol;
            const row = this.indexFocusDetailRow + 1;
            this.indexFocusDetailRow = row;
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(lst[col] + row);
                if (element) {
                    element.focus();
                }
            }, 0);
        } else {
            const nameTag: string = event.srcElement.id;
            const idx: number = this.faIncrementDetail.length - 1;
            const nameTagIndex: string = nameTag + String(idx);
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(nameTagIndex);
                if (element) {
                    element.focus();
                }
            }, 0);
        }
    }

    // 0 màn chi tiết + phân bổ
    // 1 màn nguồn gốc hình thành
    KeyPress(detail: object, key: string) {
        switch (key) {
            case 'ctr + delete':
                this.removeRow(detail);
                break;
            case 'ctr + c':
                this.copyRow(detail);
                break;
            case 'ctr + insert':
                this.AddNewRow(true);
                break;
        }
    }

    copyRow(detail) {
        // if (!this.getSelectionText()) {
        const detailCopy: any = Object.assign({}, detail);
        detailCopy.id = null;
        this.faIncrementDetail.push(detailCopy);
        // this.updateMCReceipt();
        if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
            const lst = this.listIDInputDeatil;
            const col = this.indexFocusDetailCol;
            const row = this.faIncrementDetail.length - 1;
            this.indexFocusDetailRow = row;
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(lst[col] + row);
                if (element) {
                    element.focus();
                }
            }, 0);
        }
        // }
    }

    //  @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_GhiSo])
    record() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.faIncrement.date) && !this.utilsService.isShowPopup) {
            if (!this.faIncrement.recorded) {
                const record_: Irecord = {};
                record_.id = this.faIncrement.id;
                record_.typeID = this.TYPE_ID.TSCD_GHI_TANG;
                this.gLService.record(record_).subscribe((res: HttpResponse<Irecord>) => {
                    if (res.body.success) {
                        this.faIncrement.recorded = true;
                        this.toast.success(
                            this.translate.instant('ebwebApp.mBDeposit.recordSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                });
            }
        }
    }

    //  @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_GhiSo])
    unrecord() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.faIncrement.date) && !this.utilsService.isShowPopup) {
            this.isEdit = true;
            if (this.faIncrement.recorded) {
                const record_: Irecord = {};
                record_.id = this.faIncrement.id;
                record_.typeID = this.TYPE_ID.TSCD_GHI_TANG;
                this.gLService.unrecord(record_).subscribe((res: HttpResponse<Irecord>) => {
                    if (res.body.success) {
                        this.faIncrement.recorded = false;
                        this.toast.success(
                            this.translate.instant('ebwebApp.mBDeposit.unrecordSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
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
                id: this.faIncrement.id,
                typeID: this.faIncrement.typeID,
                typeReport: typeReports
            },
            isDownload
        );
        if (typeReports === 1) {
            this.toast.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.mBDeposit.financialPaper') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 2) {
            this.toast.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.mCReceipt.home.title') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        }
    }

    updateMCReceipt() {
        // this.faIncrement.totalAmount = this.round(this.sumDetail('amount'), 7);
        // this.faIncrement.totalAmountOriginal = this.round(this.sumDetail('amountOriginal'), 8);
    }

    move(direction: number) {
        if ((direction === -1 && this.rowNum === 1) || (direction === 1 && this.rowNum === this.count)) {
            return;
        }
        this.rowNum += direction;
        // goi service get by row num
        if (this.rowNum !== this.count) {
            this.ghiTangService
                .findByRowNum({
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    fromDate: this.searchVoucher && this.searchVoucher.fromDate ? this.searchVoucher.fromDate : '',
                    toDate: this.searchVoucher && this.searchVoucher.toDate ? this.searchVoucher.toDate : '',
                    keySearch: this.searchVoucher && this.searchVoucher.textSearch ? this.searchVoucher.textSearch : '',
                    rowNum: this.rowNum
                })
                .subscribe(
                    res => {
                        this.navigate(res.body);
                        // this.rowNum--;
                        this.searchVoucher.id = res.body.id;
                        this.searchVoucher.rowNum = this.rowNum;
                        sessionStorage.setItem('searchVoucherGhiTangTSCD', JSON.stringify(this.searchVoucher));
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

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

    //  //  end of right click
    //  /*Check Error*/
    checkError(): boolean {
        if (this.faIncrementDetail && this.faIncrementDetail.length <= 0) {
            // Null phần chi tiết
            this.toast.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullDetail'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        } else {
            for (let i = 0; i < this.faIncrementDetail.length; i++) {
                if (!this.faIncrementDetail[i].fixedAssetItem) {
                    this.toast.error(
                        this.translate.instant('ebwebApp.tools.error.nullTools'),
                        this.translate.instant('ebwebApp.mCReceipt.error.error')
                    );
                    return false;
                }
            }
        }

        return true;
    }
    //
    public beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.modalRef = this.exportPrepaidExpenseVoucherModalService.open(this.faIncrementDetailRefVoucher);
        }
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    changeNoVoucher() {
        if (this.faIncrement.typeLedger === 0) {
            this.faIncrement.noFBook = this.no;
        } else if (this.faIncrement.typeLedger === 1) {
            this.faIncrement.noMBook = this.no;
        } else {
            if (this.isSoTaiChinh) {
                this.faIncrement.noFBook = this.no;
            } else {
                this.faIncrement.noMBook = this.no;
            }
        }
    }

    //  /*
    // * hàm ss du lieu 2 object và 2 mảng object
    // * return true: neu tat ca giong nhau
    // * return fale: neu 1 trong cac object ko giong nhau
    // * *
    canDeactive(): boolean {
        if (this.statusVoucher === 0 && !this.isCloseAll && !this.isSaveAndNew) {
            return (
                this.utilsService.isEquivalent(this.faIncrement, this.faIncrementCopy) &&
                this.utilsService.isEquivalentArray(this.faIncrementDetail, this.faIncrementDetailsCopy)
            );
        } else {
            return true;
        }
    }

    registerCombobox() {}

    saveRow(i) {
        this.currentRow = i;
    }

    removeRow(detail: object) {
        if (detail instanceof TIIncrementDetailRefVoucher) {
            this.faIncrementDetailRefVoucher.splice(this.faIncrementDetailRefVoucher.indexOf(detail), 1);
        } else {
            this.faIncrementDetail.splice(this.faIncrementDetail.indexOf(detail), 1);
            if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                // vì còn trường hợp = 0
                if (this.faIncrementDetail.length > 0) {
                    let row = 0;
                    if (this.indexFocusDetailRow > this.faIncrementDetail.length - 1) {
                        row = this.indexFocusDetailRow - 1;
                    } else {
                        row = this.indexFocusDetailRow;
                    }
                    const lst = this.listIDInputDeatil;
                    const col = this.indexFocusDetailCol;
                    setTimeout(function() {
                        const element: HTMLElement = document.getElementById(lst[col] + row);
                        if (element) {
                            element.focus();
                        }
                    }, 0);
                }
            }
        }
        this.updateMCReceipt();
    }

    registerAddNewRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {});
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {});
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {});
        this.eventSubscribers.push(this.eventSubscriber);
    }

    onRightClick($event, data, selectedData, isNew, isDelete, isShow, isCopy, select) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = isShow;
        this.contextMenu.isCopy = isCopy;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.selectedData = selectedData;
    }
    //
    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    //  @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Xoa])
    delete() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.faIncrement.date) && !this.utilsService.isShowPopup) {
            if (!this.faIncrement.recorded) {
                this.modalRef = this.modalService.open(this.deleteItemModal, { backdrop: 'static' });
            }
        }
    }

    navigate(imcReceipt: IFAIncrement) {
        switch (imcReceipt.typeID) {
            case this.TYPE_ID.TSCD_GHI_TANG:
                this.router.navigate(['/ghi-tang-tscd', imcReceipt.id, 'edit']);
                break;
        }
    }
    //
    isForeignCurrency() {
        // return this.account && this.faIncrement.c !== this.account.organizationUnit.currencyID;
    }

    getAmountOriginalType() {
        return 7;
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    ngAfterViewInit(): void {
        if (window.location.href.includes('/ghi-tang-tscd/new')) {
            this.focusFirstInput();
        }
    }

    actionFocus(indexCol, indexRow) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
    }
    changeSelectReason(reason: string) {}

    changeReason(reason: string) {}

    changeDate() {}

    getTSCD() {
        this.ghiTangService.getTSCD().subscribe(res => {
            this.fixedAssets = res.body;
            if (this.faIncrement && this.faIncrement.id) {
                if (this.faIncrementDetail) {
                    this.faIncrementDetail.forEach(item => {
                        item.fixedAssetItem = this.fixedAssets.find(x => x.id === item.fixedAssetID);
                    });
                }
            }
        });
    }

    getOrganizationUnits() {
        this.organizationUnitService.getOrganizationUnits().subscribe(ref => {
            this.organizationUnits = ref.body;
        });
    }

    changeTSCD(detail: IFAIncrementDetails) {
        if (detail.fixedAssetItem) {
            detail.fixedAssetID = detail.fixedAssetItem.id;
            detail.fixedAssetName = detail.fixedAssetItem.fixedAssetName;
            detail.fixedAssetCode = detail.fixedAssetItem.fixedAssetCode;
            detail.departmentID = detail.fixedAssetItem.departmentID;
            detail.amount = detail.fixedAssetItem.originalPrice;
        } else {
            detail.fixedAssetID = null;
            detail.fixedAssetName = null;
            detail.fixedAssetCode = null;
            detail.departmentID = null;
            detail.amount = null;
        }
        this.changeAmount();
    }

    confirmDelete(id: string) {
        this.ghiTangService.delete(id).subscribe(
            response => {
                // this.afterDeleteSuccess();
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.toast.success(this.translate.instant('ebwebApp.pPDiscountReturn.delete.success'));
                this.closeAll();
            },
            res => {
                this.toast.error(this.translate.instant('ebwebApp.pPDiscountReturn.delete.error'));
            }
        );
    }

    // sumAmount(detail: IFAIncrementDetails) {
    //     detail.amount = (detail.quantity ? detail.quantity : 0) * (detail.unitPrice ? detail.unitPrice : 0);
    //     this.sumAllList();
    // }

    // getDataPopup() {
    //     this.eventManager.subscribe('selectPrepaidExpenseVoucher', ref => {
    //         this.faIncrementDetailRefVoucher = [];
    //         this.faIncrementDetailRefVoucher = ref.content;
    //         this.convertRefVouchers();
    //     });
    // }
    //
    // convertRefVouchers() {
    //     if (this.faIncrementDetailRefVoucher && this.faIncrementDetailRefVoucher.length > 0) {
    //         for (let j = 0; j < this.faIncrementDetailRefVoucher.length; j++) {
    //             this.faIncrementDetailRefVoucher[j].id = null;
    //             if (this.faIncrementDetailRefVoucher[j].refID1) {
    //                 this.faIncrementDetailRefVoucher[j].refVoucherID = this.faIncrementDetailRefVoucher[j].refID1;
    //             }
    //             this.faIncrementDetailRefVoucher[j].orderPriority = j;
    //         }
    //     }
    // }

    // sumAllList() {
    //     this.sumQuantity = 0;
    //     this.sumUnitPrice = 0;
    //     this.sumAllAmount = 0;
    //     const lengthDetail: number = this.faIncrementDetail.length;
    //     const lengthVoucher: number = this.faIncrementDetailRefVoucher.length;
    //     const length = lengthDetail > lengthVoucher ? lengthDetail : lengthVoucher;
    //     for (let i = 0; i < length; i++) {
    //         if (i < lengthDetail) {
    //             if (this.faIncrementDetail[i].quantity) {
    //                 this.sumQuantity += this.faIncrementDetail[i].quantity;
    //             }
    //             if (this.faIncrementDetail[i].unitPrice) {
    //                 this.sumUnitPrice += this.faIncrementDetail[i].unitPrice;
    //             }
    //             if (this.faIncrementDetail[i].amount) {
    //                 this.sumAllAmount += this.faIncrementDetail[i].amount;
    //             }
    //         }
    //         if (i < lengthVoucher) {
    //             if (this.faIncrementDetailRefVoucher[i].totalAmountOriginal) {
    //                 this.sumTotalAmountOriginal += this.faIncrementDetailRefVoucher[i].totalAmountOriginal;
    //             }
    //         }
    //     }
    //     this.faIncrement.totalAmount = this.sumAllAmount;
    // }

    changeAmount() {
        this.faIncrement.totalAmount = 0;
        if (this.faIncrementDetail && this.faIncrementDetail.length > 0) {
            this.faIncrementDetail.forEach(detail => {
                this.faIncrement.totalAmount += detail.amount ? detail.amount : 0;
            });
        }
    }
}
