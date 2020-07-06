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
    REPORT,
    SO_LAM_VIEC,
    SU_DUNG_SO_QUAN_TRI,
    TCKHAC_GhiSo
} from 'app/app.constants';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { AccountListService } from 'app/danhmuc/account-list';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { IMCReceiptDetailCustomer } from 'app/shared/model/mc-receipt-detail-customer.model';
import { ROLE } from 'app/role.constants';
import { GhiTangService } from 'app/congcudungcu/ghi-tang-ccdc-khac/ghi-tang.service';
import { ITIIncrement } from 'app/shared/model/ti-increment.model';
import { ITIIncrementDetails, TIIncrementDetails } from 'app/shared/model/ti-increment-details.model';
import { ToolsService } from 'app/entities/tools';
import { UnitService } from 'app/danhmuc/unit';
import { ExportPrepaidExpenseVoucherModalService } from 'app/core/login/prepaid-expense-voucher-modal.service';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';
import { d } from '@angular/core/src/render3';
import { TIIncrementDetailRefVoucher } from 'app/shared/model/ti-increment-detail-ref-voucher.model';
import * as moment from 'moment';
import { DATE_FORMAT_SEARCH } from 'app/shared';

@Component({
    selector: 'eb-ghi-tang-update',
    templateUrl: './ghi-tang-update.component.html',
    styleUrls: ['./ghi-tang-update.component.css']
})
export class GhiTangUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewChecked, AfterViewInit {
    @ViewChild('content') public modalComponent: NgbModalRef;
    @ViewChild('equalQuantity') public modalEqualQuantity: NgbModalRef;
    @ViewChild('deleteItem') deleteItemModal: TemplateRef<any>;
    TYPE_GROUP_GHI_TANG = 52;
    TYPEID_GROUP_GHI_TANG = 520;
    TCKHAC_GhiSo: boolean;
    account: any;
    private _TIIncrement: ITIIncrement;
    tiIncrementDetail: ITIIncrementDetails[];
    isSaving: boolean;

    accountingObjectData: IAccountingObject[]; // Đối tượng
    checkOpenModal: any = new Map();
    currencys: ICurrency[]; //
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
    mCReceiptCopy: IMCReceipt;
    viewVouchersSelectedCopy: any;
    soDangLamViec: any;
    // currencyCode: string;
    // currency?: ICurrency;
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

    reasonDefault = '';
    accountingObjectNameOld: string;
    nameCategory: any;
    Report = REPORT;
    CategoryName = CategoryName;
    indexFocusDetailCol: any;
    indexFocusDetailRow: any;
    listIDInputDeatil: any[] = [
        'toolsITemCode',
        'toolsITemName',
        'unit',
        'creditAccount',
        'quantity',
        'unitPrice1q',
        'amount1q',
        'toolsITemCode1q',
        'toolsITemName1q',
        'accountingObject',
        'expenseItems',
        'costSet',
        'contract',
        'budgetItem',
        'department',
        'statisticsCodes'
    ];
    tools: any;
    tiIncrementDetailsCopy: any[];
    tiIncrementCopy: any;
    itemsPerPage: any;
    page: number;
    tiIncrementDetailRefVoucher: any[];
    sumQuantity: number;
    sumUnitPrice: number;
    sumAllAmount: number;
    sumTotalAmountOriginal: any;
    tools2: any;
    tiIncrementDetailRefVoucherCopy: any[];

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
        this.searchVoucher = JSON.parse(sessionStorage.getItem('searchVoucherGhiTang')); // Dùng cho tiến lùi chứng từ với danh sách tìm kiếm
        if (this.searchVoucher) {
            this.rowNum = this.searchVoucher.rowNum + 1;
        }
        this.autoPrinciplellService.getAutoPrinciples().subscribe((res: HttpResponse<IAutoPrinciple[]>) => {
            this.autoPrinciples = res.body.filter(n => n.typeId === this.TYPEID_GROUP_GHI_TANG || n.typeId === 0).sort((n1, n2) => {
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
        this.getDataPopup();
        this.registerCombobox();
        this.registerAddNewRow();
        this.registerDeleteRow();
        this.registerCopyRow();
        this.tiIncrementDetailRefVoucher = [];
        this.tiIncrementDetail = [];
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ tiIncrement }) => {
            this.tiIncrement = tiIncrement ? tiIncrement : {};
            if (this.tiIncrement.id !== undefined) {
                this.sumAllList();
                this.tiIncrementDetail = this.tiIncrement.tiIncrementDetails;
                this.tiIncrementDetail.sort(x => x.orderPriority);
                this.ghiTangService.findDetailsByID(tiIncrement.id).subscribe(res => {
                    this.tiIncrementDetailRefVoucher =
                        res.body.tiIncrementDetailRefVoucherConvertDTOS === undefined
                            ? []
                            : res.body.tiIncrementDetailRefVoucherConvertDTOS;
                    this.copy();
                });
                /*this.changePostedDate();
                 this.changeDate();*/
                if (this.account) {
                    this.showVaoSo = this.account.systemOption.some(x => x.code === SU_DUNG_SO_QUAN_TRI && x.data === '1');
                    this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                    if (this.isSoTaiChinh !== undefined) {
                        this.getHopDong();
                    }
                    this.no = this.isSoTaiChinh ? this.tiIncrement.noFBook : this.tiIncrement.noMBook;
                    this.soDangLamViec = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                    this.TCKHAC_GhiSo = this.account.systemOption.some(x => x.code === TCKHAC_GhiSo && x.data === '0');
                    // this.currencyCode = this.account.organizationUnit.currencyID;
                }
                // this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                //     this.currencys = res.body.filter(n => n.isActive).sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                //     this.currency = this.currencys.find(n => n.currencyCode === this.tiIncrement.currencyID);
                // });
                this.statusVoucher = 1;
                // this.accountingObjectNameOld = this.tiIncrement.accountingObjectName ? this.tiIncrement.accountingObjectName : '';
                this.isEdit = false; // Xem chứng từ
                this.isRecord = this.tiIncrement.recorded === undefined ? false : this.tiIncrement.recorded;
                // region Tiến lùi chứng từ
                // this.utilsService
                //     .getIndexRow({
                //         id: this.tiIncrement.id,
                //         isNext: true,
                //         typeID: this.TYPEID_GROUP_GHI_TANG,
                //         searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                //     })
                //     .subscribe(
                //         (res: HttpResponse<any[]>) => {
                //             this.rowNum = res.body[0];
                //             if (res.body.length === 1) {
                //                 this.count = 1;
                //             } else {
                //                 this.count = res.body[1];
                //             }
                //         },
                //         (res: HttpErrorResponse) => this.onError(res.message)
                //     );
                // endregion
                this.sumAllList();
                this.copy();
            } else {
                this.no = null;
                this.tiIncrementDetail = [];
                this.tiIncrement.totalAmount = 0;
                // this.accountingObjectNameOld = this.tiIncrement.accountingObjectName ? this.tiIncrement.accountingObjectName : '';
                this.statusVoucher = 0;
                this.isEdit = true; // thêm mới chứng từ
                this.tiIncrement.totalAmount = 0;
                this.translate.get(['ebwebApp.tIIncrement.home.title']).subscribe((ref: any) => {
                    this.reasonDefault = this.translate.instant('ebwebApp.tIIncrement.reason');
                    this.tiIncrement.reason = this.translate.instant('ebwebApp.tIIncrement.reason');
                });
                // Set default theo systemOption
                if (this.account) {
                    this.showVaoSo = this.account.systemOption.some(x => x.code === SU_DUNG_SO_QUAN_TRI && x.data === '1');
                    this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                    if (this.isSoTaiChinh !== undefined) {
                        this.getHopDong();
                    }
                    this.TCKHAC_GhiSo = this.account.systemOption.some(x => x.code === TCKHAC_GhiSo && x.data === '0');
                    this.tiIncrement.date = this.utilsService.ngayHachToan(this.account);
                    /*this.changePostedDate();*/
                    this.changeDate();
                    this.soDangLamViec = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                    if (this.isSoTaiChinh) {
                        if (this.showVaoSo) {
                            this.tiIncrement.typeLedger = 2;
                        } else {
                            this.tiIncrement.typeLedger = 0;
                        }
                    } else {
                        this.tiIncrement.typeLedger = 2;
                    }
                    this.utilsService
                        .getGenCodeVoucher({
                            typeGroupID: this.TYPE_GROUP_GHI_TANG, // typeGroupID loại chứng từ
                            displayOnBook: this.soDangLamViec // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
                        })
                        .subscribe((res: HttpResponse<string>) => {
                            console.log(res.body);
                            this.no = res.body;
                            // this.copy();
                        });
                }
            }
            this.getToolsActive();
            this.getOrganizationUnits();
            this.getUnit();
            this.copy();
        });

        // this.registerRef();
    }

    //  registerRef() {
    //      this.eventSubscriber = this.eventManager.subscribe('selectViewVoucher', response => {
    //          // if (this.statusVoucher === 1) {
    //          this.viewVouchersSelected = response.content;
    //          // }
    //      });
    //      this.eventSubscribers.push(this.eventSubscriber);
    //      this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
    //          this.isCbbSaveAndNew = false;
    //          this.registerComboboxSave(response);
    //          this.utilsService.setShowPopup(false);
    //      });
    //      this.eventSubscribers.push(this.eventSubscriber);
    //      this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
    //          this.isCbbSaveAndNew = true;
    //          this.registerComboboxSave(response);
    //          this.utilsService.setShowPopup(false);
    //      });
    //      this.eventSubscribers.push(this.eventSubscriber);
    //      this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
    //          this.utilsService.setShowPopup(response.content);
    //      });
    //      this.eventSubscribers.push(this.eventSubscriber);
    //  }
    //
    closeForm() {
        if (this.tiIncrementCopy) {
            if (
                !this.utilsService.isEquivalent(this.tiIncrement, this.tiIncrementCopy) ||
                !this.utilsService.isEquivalentArray(this.tiIncrementDetail, this.tiIncrementDetailsCopy) ||
                !this.utilsService.isEquivalentArray(this.tiIncrementDetailRefVoucher, this.tiIncrementDetailRefVoucher)
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
            if (sessionStorage.getItem('index_GhiTang')) {
                this.rowNum = sessionStorage.getItem('index_GhiTang');
                // this.router.navigate(['/ghi-tang-ccdc', 'hasSearch', '1'], {
                //     queryParams: {
                //         page: sessionStorage.getItem('page_GhiTang'),
                //         size: sessionStorage.getItem('size_GhiTang'),
                //         index: sessionStorage.getItem('index_GhiTang')
                //     }
                // });
            } else {
                this.router.navigate(['/ghi-tang-ccdc', 'hasSearch', '1']);
            }
        } else {
            if (sessionStorage.getItem('page_GhiTang')) {
                this.router.navigate(['/ghi-tang-ccdc'], {
                    queryParams: {
                        page: sessionStorage.getItem('page_GhiTang'),
                        size: sessionStorage.getItem('size_GhiTang'),
                        index: sessionStorage.getItem('index_GhiTang')
                    }
                });
            } else {
                this.router.navigate(['/ghi-tang-ccdc']);
            }
        }
    }

    //  @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Sua, ROLE.PhieuThu_Them])
    save() {
        event.preventDefault();
        this.fillToSave();

        if (this.checkError()) {
            this.checkOpenModal = new Map();
            this.saveAfter(false);
        }
    }

    // check = true lưu và thêm <> false lưu
    saveAfter(check: boolean) {
        if (this.sumTotalAmountOriginal !== this.sumAllAmount && !this.checkOpenModal.has(1)) {
            this.checkOpenModal.set(1, 1);
            this.modalRef = this.modalService.open(this.modalEqualQuantity, { backdrop: 'static' });
            return;
        }
        if (!check) {
            if (this.tiIncrement.id !== undefined) {
                this.subscribeToSaveResponse(this.ghiTangService.update(this.tiIncrement));
            } else {
                this.subscribeToSaveResponse(this.ghiTangService.create(this.tiIncrement));
            }
        } else {
            if (this.tiIncrement.id !== undefined) {
                this.subscribeToSaveResponseAndContinue(this.ghiTangService.update(this.tiIncrement));
            } else {
                this.subscribeToSaveResponseAndContinue(this.ghiTangService.create(this.tiIncrement));
            }
        }
    }
    fillToSave() {
        this.tiIncrement.tiIncrementDetailRefVouchers = this.tiIncrementDetailRefVoucher;
        if (this.tiIncrement.typeLedger.toString() === '0') {
            this.tiIncrement.noFBook = this.no;
            this.tiIncrement.noMBook = null;
        } else if (this.tiIncrement.typeLedger.toString() === '1') {
            this.tiIncrement.noMBook = this.no;
            this.tiIncrement.noFBook = null;
        } else {
            if (this.isSoTaiChinh) {
                this.tiIncrement.noFBook = this.no;
            } else {
                this.tiIncrement.noMBook = this.no;
            }
        }
        if (!this.tiIncrement.typeID) {
            this.tiIncrement.typeID = this.TYPEID_GROUP_GHI_TANG;
        }
        this.isSaving = true;
        // this.tiIncrement.totalVATAmount = 0;
        // this.tiIncrement.totalVATAmountOriginal = 0;
        this.tiIncrement.tiIncrementDetails = this.tiIncrementDetail;
        if (this.tiIncrement && this.tiIncrement.tiIncrementDetails.length > 0) {
            for (let i = 0; i < this.tiIncrement.tiIncrementDetails.length; i++) {
                this.tiIncrement.tiIncrementDetails[i].orderPriority = i;
                if (!this.tiIncrement.tiIncrementDetails[i].quantity) {
                    this.tiIncrement.tiIncrementDetails[i].quantity = 0;
                }
                if (!this.tiIncrement.tiIncrementDetails[i].unitPrice) {
                    this.tiIncrement.tiIncrementDetails[i].unitPrice = 0;
                }
                if (!this.tiIncrement.tiIncrementDetails[i].amount) {
                    this.tiIncrement.tiIncrementDetails[i].amount = 0;
                }
            }
        }
        // this.tiIncrement.mcreceiptDetailTaxes = this.mCReceiptDetailTaxs;
        // for (let i = 0; i < this.tiIncrement.mcreceiptDetailTaxes.length; i++) {
        //     this.tiIncrement.mcreceiptDetailTaxes[i].orderPriority = i + 1;
        //     this.tiIncrement.totalVATAmount = this.tiIncrement.totalVATAmount + this.tiIncrement.mcreceiptDetailTaxes[i].vATAmount;
        //     this.tiIncrement.totalVATAmountOriginal =
        //         this.tiIncrement.totalVATAmountOriginal + this.tiIncrement.mcreceiptDetailTaxes[i].vATAmountOriginal;
        // }
        // for (let i = 0; i < this.tiIncrement.mcreceiptDetails.length; i++) {
        //     this.tiIncrement.mcreceiptDetails[i].orderPriority = i + 1;
        // }
    }

    //  @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Them])
    saveAndNew() {
        event.preventDefault();
        // this.save();
        if (this.statusVoucher === 0 && !this.utilsService.isShowPopup) {
            this.fillToSave();
            if (this.checkError()) {
                this.statusVoucher = 0;
                this.isEdit = true;
                this.saveAfter(true);
            }
        }
    }

    //  @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Them])
    copyAndNew() {
        event.preventDefault();
        this.tiIncrement.id = undefined;
        this.tiIncrement.recorded = false;
        this.no = null;
        this.tiIncrement.noFBook = null;
        this.tiIncrement.noMBook = null;
        this.isRecord = false;
        for (let i = 0; i < this.tiIncrementDetail.length; i++) {
            this.tiIncrementDetail[i].id = undefined;
        }
        if (this.tiIncrementDetailRefVoucher) {
            for (let i = 0; i < this.tiIncrementDetailRefVoucher.length; i++) {
                this.tiIncrementDetailRefVoucher[i].id = undefined;
            }
        }
        this.utilsService
            .getGenCodeVoucher({
                typeGroupID: this.TYPE_GROUP_GHI_TANG, // typeGroupID loại chứng từ
                displayOnBook: this.soDangLamViec // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
            })
            .subscribe((res: HttpResponse<string>) => {
                this.no = res.body;
            });
        this.statusVoucher = 0;
        this.isEdit = true;
        this.tiIncrementCopy = {};

        // this.copy();
    }

    //  @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Sua])
    edit() {
        event.preventDefault();
        if (!this.checkCloseBook(this.account, this.tiIncrement.date) && !this.utilsService.isShowPopup) {
            if (this.statusVoucher === 1 && !this.tiIncrement.recorded) {
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
            if (this.tiIncrement.id !== undefined) {
                this.subscribeToSaveResponseWhenClose(this.ghiTangService.update(this.tiIncrement));
            } else {
                this.subscribeToSaveResponseWhenClose(this.ghiTangService.create(this.tiIncrement));
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
        this.tiIncrementCopy = Object.assign({}, this.tiIncrement);
        this.tiIncrementDetailsCopy = this.tiIncrementDetail.map(object => ({ ...object }));
        this.tiIncrementDetailRefVoucherCopy = this.tiIncrementDetailRefVoucher.map(object => ({ ...object }));
        // this.viewVouchersSelectedCopy = this.t.map(object => ({ ...object }));
    }

    //  @ebAuth(['ROLE_ADMIN', ROLE.PhieuThu_Them])
    addNew($event = null) {
        event.preventDefault();
        if (!this.utilsService.isShowPopup) {
            this.router.navigate(['/ghi-tang-ccdc/new']);
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body) {
                    this.onSaveSuccess();
                    this.tiIncrement.id = res.body.id;
                    this.tiIncrement.recorded = res.body.recorded;
                    this.router.navigate(['/ghi-tang-ccdc', res.body.id, 'edit']);
                    this.tiIncrementCopy = null;
                }
                // else if (res.body.status === 1) {
                //     this.noVoucherExist();
                // } else if (res.body.status === 2) {
                //     this.recordFailed();
                // }
                // }
            },
            (res: HttpErrorResponse) => {
                if (res.error.title === 'RSIBadRequest') {
                    this.toast.error(
                        this.translate.instant('global.data.noVocherAlreadyExist'),
                        this.translate.instant('ebwebApp.mCReceipt.home.message')
                    );
                    return;
                }
                if (res.error.title === 'maxQuantity') {
                    this.toast.error(
                        this.translate.instant('ebwebApp.tIIncrement.error.quantityRest', { code: res.error.entityName }),
                        this.translate.instant('ebwebApp.mCReceipt.error.error')
                    );
                    return;
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
                    this.tiIncrement.id = res.body.id;
                    this.tiIncrement.recorded = res.body.recorded;
                    // this.addNew();
                    // this.router.navigate(['/ghi-tang-ccdc', 'new']);
                    this.router.navigate(['/ghi-tang-ccdc', res.body.id, 'edit']).then(() => {
                        this.router.navigate(['/ghi-tang-ccdc', 'new']);
                        this.isSaveAndNew = false;
                    });
                }
                // else if (res.body.status === 1) {
                //     this.noVoucherExist();
                // } else if (res.body.status === 2) {
                //     this.recordFailed();
                // }
                // this.addNew();
            },
            (res: HttpErrorResponse) => {
                if (res.error.title === 'RSIBadRequest') {
                    this.toast.error(
                        this.translate.instant('global.data.noVocherAlreadyExist'),
                        this.translate.instant('ebwebApp.mCReceipt.home.message')
                    );
                    return;
                }
                if (res.error.title === 'maxQuantity') {
                    this.toast.error(
                        this.translate.instant('ebwebApp.tIIncrement.error.quantityRest', { code: res.error.entityName }),
                        this.translate.instant('ebwebApp.mCReceipt.error.error')
                    );
                    return;
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
                    return;
                }
                if (res.error.title === 'maxQuantity') {
                    this.toast.error(
                        this.translate.instant('ebwebApp.tIIncrement.error.quantityRest', { code: res.error.entityName }),
                        this.translate.instant('ebwebApp.mCReceipt.error.error')
                    );
                    return;
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
    //
    //  trackAccountingObjectById(index: number, item: IAccountingObject) {
    //      return item.id;
    //  }
    //
    //  trackExpenseItemById(index: number, item: IExpenseItem) {
    //      return item.id;
    //  }
    //
    //  trackOrganizationUnitById(index: number, item: IOrganizationUnit) {
    //      return item.id;
    //  }
    //
    //  trackCostSetById(index: number, item: ICostSet) {
    //      return item.id;
    //  }
    //
    //  trackTransportMethodById(index: number, item: ITransportMethod) {
    //      return item.id;
    //  }
    //
    //  trackPaymentClauseById(index: number, item: IPaymentClause) {
    //      return item.id;
    //  }
    //
    get tiIncrement() {
        return this._TIIncrement;
    }

    set tiIncrement(tiIncrement: ITIIncrement) {
        this._TIIncrement = tiIncrement;
    }
    //
    //  selectAccountingObject() {
    //      if (this.tiIncrement.accountingObject !== null && this.tiIncrement.accountingObject !== undefined) {
    //          this.tiIncrement.accountingObjectName = this.tiIncrement.accountingObject.accountingObjectName;
    //          this.tiIncrement.accountingObjectAddress = this.tiIncrement.accountingObject.accountingObjectAddress;
    //          this.tiIncrement.taxCode = this.tiIncrement.accountingObject.taxCode;
    //          this.tiIncrement.payers = this.tiIncrement.accountingObject.contactName;
    //          if (
    //              this.tiIncrement.reason === this.reasonDefault ||
    //              this.tiIncrement.reason === this.reasonDefault + ' của ' + this.accountingObjectNameOld
    //          ) {
    //              this.tiIncrement.reason = this.reasonDefault + ' của ' + this.tiIncrement.accountingObjectName;
    //              this.tiIncrementDetails.forEach(mc => {
    //                  mc.description = this.tiIncrement.reason;
    //              });
    //          }
    //          this.accountingObjectNameOld = this.tiIncrement.accountingObject.accountingObjectName;
    //          for (const dt of this.tiIncrementDetail) {
    //              dt.accountingObject = this.tiIncrement.accountingObject;
    //          }
    //          for (const dtt of this.mCReceiptDetailTaxs) {
    //              dtt.accountingObject = this.tiIncrement.accountingObject;
    //          }
    //      }
    //      if (this.accountingObjects && this.accountingObjects.length > 0) {
    //          if (this.tiIncrement.accountingObject) {
    //              if (!this.accountingObjects.map(n => n.id).includes(this.tiIncrement.accountingObject.id)) {
    //                  this.tiIncrement.accountingObject = null;
    //              }
    //          }
    //      }
    //  }
    //
    //  selectAutoPrinciple() {
    //      if (this.autoPrinciple && this.autoPrinciple.typeId !== 0) {
    //          this.tiIncrement.reason = this.autoPrinciple.autoPrincipleName;
    //          for (const dt of this.tiIncrementDetail) {
    //              dt.debitAccount = this.autoPrinciple.debitAccount;
    //              dt.creditAccount = this.autoPrinciple.creditAccount;
    //              dt.description = this.autoPrinciple.autoPrincipleName;
    //          }
    //      } else if (this.autoPrinciple && this.autoPrinciple.typeId === 0) {
    //          this.tiIncrement.reason = this.autoPrinciple.autoPrincipleName;
    //      }
    //  }
    //
    //  selectCurrency() {
    //      if (this.tiIncrement.currencyID) {
    //          this.currency = this.currencys.find(n => n.currencyCode === this.tiIncrement.currencyID);
    //          this.tiIncrement.exchangeRate = this.currencys.find(n => n.currencyCode === this.tiIncrement.currencyID).exchangeRate;
    //
    //          this.tiIncrement.totalAmount = 0;
    //          // this.tiIncrement.totalAmountOriginal = 0;
    //          for (let i = 0; i < this.tiIncrementDetail.length; i++) {
    //              this.tiIncrementDetail[i].amount = this.round(
    //                  this.tiIncrementDetail[i].amountOriginal *
    //                      (this.currency.formula.includes('*') ? this.tiIncrement.exchangeRate : 1 / this.tiIncrement.exchangeRate),
    //                  7
    //              );
    //          }
    //          this.updateMCReceipt();
    //      }
    //  }
    //
    //  changeExchangeRate() {
    //      if (!this.tiIncrement.exchangeRate) {
    //          this.tiIncrement.exchangeRate = 0;
    //      }
    //      if (this.tiIncrement.currencyID) {
    //          this.tiIncrement.totalAmount = 0;
    //          for (let i = 0; i < this.tiIncrementDetail.length; i++) {
    //              this.tiIncrementDetail[i].amount = this.round(
    //                  this.tiIncrementDetail[i].amountOriginal *
    //                      (this.currency.formula.includes('*') ? this.tiIncrement.exchangeRate : 1 / this.tiIncrement.exchangeRate),
    //                  7
    //              );
    //          }
    //          this.updateMCReceipt();
    //      }
    //  }
    //
    //  // Loại đối tượng kế toán
    //  selectAccountingObjectType() {
    //      if (this.tiIncrement.accountingObjectType || this.tiIncrement.accountingObjectType === 0) {
    //          if (this.tiIncrement.accountingObjectType === 0) {
    //              // Nhà cung cấp
    //              this.nameCategory = this.utilsService.NHA_CUNG_CAP;
    //              this.accountingObjects = this.accountingObjectData.filter(n => n.objectType === 0 || n.objectType === 2);
    //          } else if (this.tiIncrement.accountingObjectType === 1) {
    //              // Khách hàng
    //              this.nameCategory = this.utilsService.KHACH_HANG;
    //              this.accountingObjects = this.accountingObjectData.filter(n => n.objectType === 1 || n.objectType === 2);
    //          } else if (this.tiIncrement.accountingObjectType === 2) {
    //              // Nhân viên
    //              this.nameCategory = this.utilsService.NHAN_VIEN;
    //              this.accountingObjects = this.accountingObjectData.filter(n => n.isEmployee);
    //          } else if (this.tiIncrement.accountingObjectType === 3) {
    //              // Khác
    //              this.nameCategory = this.utilsService.KHACH_HANG;
    //              this.accountingObjects = this.accountingObjectData.filter(n => n.objectType === 3);
    //          } else {
    //              this.nameCategory = this.utilsService.KHACH_HANG;
    //              this.accountingObjects = this.accountingObjectData;
    //          }
    //      }
    //      if (this.accountingObjects && this.accountingObjects.length > 0) {
    //          if (this.tiIncrement.accountingObject) {
    //              if (!this.accountingObjects.map(n => n.id).includes(this.tiIncrement.accountingObject.id)) {
    //                  this.tiIncrement.accountingObject = null;
    //                  this.tiIncrement.accountingObjectName = null;
    //                  this.tiIncrement.accountingObjectAddress = null;
    //                  this.tiIncrement.taxCode = null;
    //                  this.tiIncrement.payers = null;
    //              }
    //          }
    //      }
    //  }
    //
    AddNewRow(isRightClick?) {
        let detailItem: TIIncrementDetails;
        detailItem = {};
        detailItem.quantity = 0;
        detailItem.unitPrice = 0;
        detailItem.amount = 0;
        // this.tiIncrementDetail.push(detailItem);
        let lenght = 0;
        if (isRightClick) {
            this.tiIncrementDetail.splice(this.indexFocusDetailRow + 1, 0, detailItem);
            lenght = this.indexFocusDetailRow + 2;
        } else {
            this.tiIncrementDetail.push({});
            this.tiIncrementDetail[this.tiIncrementDetail.length - 1].organizationUnits = this.organizationUnits;
            lenght = this.tiIncrementDetail.length;
        }
        // this.tiIncrementDetail[lenght - 1].amount = 0;
        // this.tiIncrementDetail[lenght - 1].quantity = 0;
        // this.tiIncrementDetail[lenght - 1].unitPrice = 0;
        // this.tiIncrementDetail[lenght - 1].description = this.tiIncrement.reason ? this.tiIncrement.reason : '';
        // if (lenght > 1) {
        //     this.tiIncrementDetail[lenght - 1].quantity = this.tiIncrementDetail[lenght - 2].quantity;
        //     this.tiIncrementDetail[lenght - 1].amount = this.tiIncrementDetail[lenght - 2].amount;
        //     this.tiIncrementDetail[lenght - 1].unitPrice = this.tiIncrementDetail[lenght - 2].unitPrice;
        //     this.tiIncrementDetail[lenght - 1].description = this.tiIncrementDetail[lenght - 2].description;
        //     this.tiIncrementDetail[lenght - 1].organizationUnit = this.tiIncrementDetail[lenght - 2].organizationUnit;
        // } else {
        //     if (this.autoPrinciple && this.autoPrinciple.typeId !== 0) {
        //         this.tiIncrementDetail[lenght - 1].debitAccount = this.autoPrinciple.debitAccount;
        //         this.tiIncrementDetail[lenght - 1].creditAccount = this.autoPrinciple.creditAccount;
        //     } else {
        //         this.tiIncrementDetail[lenght - 1].debitAccount = this.debitAccountDefault ? this.debitAccountDefault : null;
        //         this.tiIncrementDetail[lenght - 1].creditAccount = this.creditAccountDefault ? this.creditAccountDefault : null;
        //     }
        //     if (this.tiIncrement.accountingObject) {
        //         this.tiIncrementDetail[lenght - 1].accountingObject = this.tiIncrement.accountingObject;
        //     }
        // }
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
            const idx: number = this.tiIncrementDetail.length - 1;
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
                this.sumAllList();
                break;
            case 'ctr + c':
                this.copyRow(detail);
                this.sumAllList();
                break;
            case 'ctr + insert':
                this.AddNewRow(true);
                this.sumAllList();
                break;
        }
    }

    copyRow(detail) {
        // if (!this.getSelectionText()) {
        const detailCopy: any = Object.assign({}, detail);
        detailCopy.id = null;
        this.tiIncrementDetail.push(detailCopy);
        // this.updateMCReceipt();
        if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
            const lst = this.listIDInputDeatil;
            const col = this.indexFocusDetailCol;
            const row = this.tiIncrementDetail.length - 1;
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
        if (!this.checkCloseBook(this.account, this.tiIncrement.date) && !this.utilsService.isShowPopup) {
            if (!this.tiIncrement.recorded) {
                const record_: Irecord = {};
                record_.id = this.tiIncrement.id;
                record_.typeID = this.TYPEID_GROUP_GHI_TANG;
                this.gLService.record(record_).subscribe((res: HttpResponse<Irecord>) => {
                    if (res.body.success) {
                        this.tiIncrement.recorded = true;
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
        if (!this.checkCloseBook(this.account, this.tiIncrement.date) && !this.utilsService.isShowPopup) {
            this.isEdit = true;
            if (this.tiIncrement.recorded) {
                const record_: Irecord = {};
                record_.id = this.tiIncrement.id;
                record_.typeID = this.TYPEID_GROUP_GHI_TANG;
                this.gLService.unrecord(record_).subscribe((res: HttpResponse<Irecord>) => {
                    if (res.body.success) {
                        this.tiIncrement.recorded = false;
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
                id: this.tiIncrement.id,
                typeID: this.tiIncrement.typeID,
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
    //
    //  changeAmountOriginal(detail: IMCReceiptDetails) {
    //      detail.amount = this.round(
    //          detail.amountOriginal * (this.currency.formula.includes('*') ? this.tiIncrement.exchangeRate : 1 / this.tiIncrement.exchangeRate),
    //          7
    //      );
    //      this.updateMCReceipt();
    //  }
    //
    //  changeAmount(detail: IMCReceiptDetails) {
    //      this.updateMCReceipt();
    //  }

    updateMCReceipt() {
        // this.tiIncrement.totalAmount = this.round(this.sumDetail('amount'), 7);
        // this.tiIncrement.totalAmountOriginal = this.round(this.sumDetail('amountOriginal'), 8);
    }

    //  sumDetail(prop) {
    //      let total = 0;
    //      for (let i = 0; i < this.tiIncrementDetail.length; i++) {
    //          total += this.tiIncrementDetail[i][prop];
    //      }
    //      return isNaN(total) ? 0 : total;
    //  }
    //
    //  // region Tiến lùi chứng từ
    //  // ham lui, tien
    // previousEdit() {
    //     // goi service get by row num
    //     if (this.rowNum !== this.count) {
    //         this.ghiTangService
    //             .findByRowNum({
    //                 id: this.tiIncrement.id,
    //                 page: this.page - 1,
    //                 size: this.itemsPerPage,
    //                 fromDate: this.searchVoucher.fromDate ? this.searchVoucher.fromDate : '',
    //                 toDate: this.searchVoucher.toDate ? this.searchVoucher.toDate : '',
    //                 keySearch: this.searchVoucher.textSearch ? this.searchVoucher.textSearch : '',
    //                 rowNum: this.rowNum
    //             })
    //             .subscribe(
    //                 res => {
    //                     this.navigate(res.body);
    //                     this.rowNum--;
    //                     this.searchVoucher.rowNum = this.rowNum;
    //                     sessionStorage.setItem('searchVoucherGhiTang', JSON.stringify(this.searchVoucher));
    //                 },
    //                 (res: HttpErrorResponse) => this.onError(res.message)
    //             );
    //     }
    // }

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
                        sessionStorage.setItem('searchVoucherGhiTang', JSON.stringify(this.searchVoucher));
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }
    //
    // nextEdit() {
    //     // goi service get by row num
    //     if (this.rowNum !== 1) {
    //         this.ghiTangService
    //             .findByRowNum({
    //                 id: this.tiIncrement.id,
    //                 page: this.page - 1,
    //                 size: this.itemsPerPage,
    //                 fromDate: this.searchVoucher.fromDate ? this.searchVoucher.fromDate : '',
    //                 toDate: this.searchVoucher.toDate ? this.searchVoucher.toDate : '',
    //                 keySearch: this.searchVoucher.textSearch ? this.searchVoucher.textSearch : '',
    //                 rowNum: this.searchVoucher.rowNum ? this.searchVoucher.rowNum : 0
    //             })
    //             .subscribe(
    //                 res => {
    //                     this.navigate(res.body);
    //                     this.searchVoucher.rowNum = this.searchVoucher.rowNum++;
    //                     sessionStorage.setItem('searchVoucherGhiTang', JSON.stringify(this.searchVoucher));
    //                 },
    //                 (res: HttpErrorResponse) => this.onError(res.message)
    //             );
    //     }
    // }
    //
    //  // endregion
    //  // right click event
    //  // activates the menu with the coordinates
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
    //
    //  // disables the menu
    //  disableContextMenu() {
    //      this.contextmenu.value = false;
    //  }
    //
    //  //  end of right click
    //  /*Check Error*/
    checkError(): boolean {
        if (this.tiIncrementDetail && this.tiIncrementDetail.length <= 0) {
            // Null phần chi tiết
            this.toast.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullDetail'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        } else {
            // let textError = '';
            // const checkToolsMap = new Map();
            // const checkToolsCodeAndQuantityMap = new Map();

            if (this.tiIncrementDetailRefVoucher) {
                for (let i = 0; i < this.tiIncrementDetailRefVoucher.length; i++) {}
            }
            for (let i = 0; i < this.tiIncrementDetail.length; i++) {
                // const name = this.translate.instant('ebwebApp.tIIncrement.error.quantityRestItem', {
                //     code: textError.slice(0, textError.length - 2)
                // });
                // if (checkToolsCodeAndQuantityMap.has(name)) {
                // }

                // check tools null
                if (!this.tiIncrementDetail[i].toolsItem) {
                    this.toast.error(
                        this.translate.instant('ebwebApp.tools.error.nullTools'),
                        this.translate.instant('ebwebApp.mCReceipt.error.error')
                    );
                    return false;
                }

                // check tổng số lượng tồn
                //     if (!checkToolsMap.has(this.tiIncrementDetail[i].toolsID)) {
                //         checkToolsMap.set(this.tiIncrementDetail[i].toolsID, this.tiIncrementDetail[i].quantity);
                //     } else {
                //         checkToolsMap.set(
                //             this.tiIncrementDetail[i].toolsID,
                //             checkToolsMap.get(this.tiIncrementDetail[i].toolsID) + this.tiIncrementDetail[i].quantity
                //         );
                //     }
                //     let quantity = 0;
                //     if (!this.tiIncrementDetail[i].toolsItem.quantityRest) {
                //         if (this.tiIncrementDetail[i].organizationUnits) {
                //             quantity = this.tiIncrementDetail[i].organizationUnits.find(x => (x.id = this.tiIncrementDetail[i].toolsID))
                //                 .quantityRest;
                //         }
                //     } else {
                //         quantity = this.tiIncrementDetail[i].toolsItem.quantityRest;
                //     }
                //     if (quantity > 0 && quantity < checkToolsMap.get(this.tiIncrementDetail[i].toolsID)) {
                //         textError += this.tiIncrementDetail[i].toolsName + ', ';
                //     }
                // }
                // if (textError.trim().length > 0) {
                //     this.toast.error(
                //         this.translate.instant('ebwebApp.tIIncrement.error.quantityRest', { code: textError.slice(0, textError.length - 2) }),
                //         this.translate.instant('ebwebApp.mCReceipt.error.error')
                //     );
                //     return false;
            }
        }

        // const dt = this.tiIncrementDetail.find(
        //     n => n.creditAccount === null || n.creditAccount === undefined || n.debitAccount === undefined || n.debitAccount === null
        // );
        // const dtT = this.mCReceiptDetailTaxs.find(n => n.vATAccount === null || n.vATAccount === undefined);
        // if (dt || dtT) {
        //     this.toast.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
        //     return false;
        // }
        // const checkAcc = this.utilsService.checkAccoutWithDetailType(
        //     this.debitAccountList,
        //     this.creditAccountList,
        //     this.tiIncrementDetail,
        //     this.accountingObjectData,
        //     this.costSets,
        //     this.eMContracts,
        //     null,
        //     this.bankAccountDetails,
        //     this.organizationUnits,
        //     this.expenseItems,
        //     this.budgetItems,
        //     this.statisticCodes
        // );
        // if (checkAcc) {
        //     this.toast.error(checkAcc, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        //     return false;
        // }
        return true;
    }
    //
    public beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.modalRef = this.exportPrepaidExpenseVoucherModalService.open(this.tiIncrementDetailRefVoucher);
        }
    }
    //
    //  /*-----*/
    //  getTotalVATAmount() {
    //      if (this.mCReceiptDetailTaxs && this.mCReceiptDetailTaxs.length > 0) {
    //          return this.mCReceiptDetailTaxs.map(n => n.vATAmount).reduce((a, b) => a + b);
    //      } else {
    //          return 0;
    //      }
    //  }
    //
    //  changeDate() {
    //      // this.dateStr = this.tiIncrement.date.format('DD/MM/YYYY');
    //      this.tiIncrement.postedDate = this.tiIncrement.date;
    //      // this.changePostedDate();
    //  }
    //
    //  changeDateStr() {
    //      try {
    //          if (this.dateStr.length === 8) {
    //              const td = this.dateStr.replace(/^(.{2})/, '$1/').replace(/^(.{5})/, '$1/');
    //              if (!moment(td, 'DD/MM/YYYY').isValid()) {
    //                  this.validateDate = true;
    //                  this.tiIncrement.date = null;
    //              } else {
    //                  this.validateDate = false;
    //                  this.tiIncrement.date = moment(td, 'DD/MM/YYYY');
    //                  this.tiIncrement.postedDate = this.tiIncrement.date;
    //                  this.changePostedDate();
    //              }
    //          } else {
    //              this.tiIncrement.date = null;
    //              this.validateDate = false;
    //          }
    //      } catch (e) {
    //          this.tiIncrement.date = null;
    //          this.validateDate = false;
    //      }
    //  }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    changeNoVoucher() {
        if (this.tiIncrement.typeLedger === 0) {
            this.tiIncrement.noFBook = this.no;
        } else if (this.tiIncrement.typeLedger === 1) {
            this.tiIncrement.noMBook = this.no;
        } else {
            if (this.isSoTaiChinh) {
                this.tiIncrement.noFBook = this.no;
            } else {
                this.tiIncrement.noMBook = this.no;
            }
        }
    }

    /*ngOnDestroy(): void {
         if (this.eventSubscriber) {
             this.eventManager.destroy(this.eventSubscriber);
         }
     }*/

    //  /*
    // * hàm ss du lieu 2 object và 2 mảng object
    // * return true: neu tat ca giong nhau
    // * return fale: neu 1 trong cac object ko giong nhau
    // * *
    canDeactive(): boolean {
        if (this.statusVoucher === 0 && !this.isCloseAll && !this.isSaveAndNew) {
            return (
                this.utilsService.isEquivalent(this.tiIncrement, this.tiIncrementCopy) &&
                this.utilsService.isEquivalentArray(this.tiIncrementDetail, this.tiIncrementDetailsCopy)
            );
        } else {
            return true;
        }
    }

    registerCombobox() {
        // this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
        //     this.accountingObjectService.getAllDTO().subscribe(
        //         (res: HttpResponse<IAccountingObject[]>) => {
        //             this.accountingObjectData = res.body
        //                 .filter(n => n.isActive)
        //                 .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
        //             this.employees = res.body
        //                 .filter(n => n.isEmployee && n.isActive)
        //                 .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
        //             this.selectAccountingObjectType();
        //         },
        //         (res: HttpErrorResponse) => this.onError(res.message)
        //     );
        //     if (response.content.name === CategoryName.MUC_THU_CHI) {
        //         const BudgetItem = response.content.data;
        //         this.budgetItems.push(BudgetItem);
        //         this.budgetItems.sort((a, b) => a.budgetItemCode.localeCompare(b.budgetItemCode));
        //         this.tiIncrementDetail[this.currentRow].budgetItem = BudgetItem;
        //     }
        // });
        // this.eventSubscribers.push(this.eventSubscriber);
    }

    saveRow(i) {
        this.currentRow = i;
    }

    removeRow(detail: object) {
        if (detail instanceof TIIncrementDetailRefVoucher) {
            this.tiIncrementDetailRefVoucher.splice(this.tiIncrementDetailRefVoucher.indexOf(detail), 1);
        } else {
            this.tiIncrementDetail.splice(this.tiIncrementDetail.indexOf(detail), 1);
            if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                // vì còn trường hợp = 0
                if (this.tiIncrementDetail.length > 0) {
                    let row = 0;
                    if (this.indexFocusDetailRow > this.tiIncrementDetail.length - 1) {
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
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            const lst = this.listIDInputDeatil;
            const col = this.indexFocusDetailCol;
            const row = this.tiIncrementDetail.length - 1;
            this.sumAllList();
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(lst[col] + row);
                if (element) {
                    element.focus();
                }
            }, 0);
        });
        // this.eventSubscribers.push(this.eventSubscriber);
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            const lst = this.listIDInputDeatil;
            const col = this.indexFocusDetailCol;
            const row = this.indexFocusDetailRow + 1;
            this.sumAllList();
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(lst[col] + row);
                if (element) {
                    element.focus();
                }
            }, 0);
            // this.changeAllocationAmount();
        });
        // this.eventSubscribers.push(this.eventSubscriber);
    }

    registerDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            const lst = this.listIDInputDeatil;
            const col = this.indexFocusDetailCol;
            const row = this.indexFocusDetailRow - 1;
            this.sumAllList();
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(lst[col] + row);
                if (element) {
                    element.focus();
                }
            }, 0);
        });
        // this.eventSubscribers.push(this.eventSubscriber);
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
        if (!this.checkCloseBook(this.account, this.tiIncrement.date) && !this.utilsService.isShowPopup) {
            if (!this.tiIncrement.recorded) {
                this.modalRef = this.modalService.open(this.deleteItemModal, { backdrop: 'static' });
            }
        }
    }

    navigate(imcReceipt: ITIIncrement) {
        switch (imcReceipt.typeID) {
            case this.TYPEID_GROUP_GHI_TANG:
                this.router.navigate(['/ghi-tang-ccdc', imcReceipt.id, 'edit']);
                break;
        }
    }
    //
    isForeignCurrency() {
        // return this.account && this.tiIncrement.c !== this.account.organizationUnit.currencyID;
    }

    getAmountOriginalType() {
        return 7;
    }

    //  getUnitPriceOriginalType() {
    //      if (this.isForeignCurrency()) {
    //          return 2;
    //      }
    //      return 1;
    //  }
    //
    //  round(value, type) {
    //      if (type === 8) {
    //          if (this.isForeignCurrency()) {
    //              return this.utilsService.round(value, this.account.systemOption, type);
    //          } else {
    //              return this.utilsService.round(value, this.account.systemOption, 7);
    //          }
    //      } else if (type === 2) {
    //          if (this.isForeignCurrency()) {
    //              return this.utilsService.round(value, this.account.systemOption, type);
    //          } else {
    //              return this.utilsService.round(value, this.account.systemOption, 1);
    //          }
    //      } else {
    //          return this.utilsService.round(value, this.account.systemOption, type);
    //      }
    //  }
    //
    //  /*Phiếu thu khách hàng*/
    //  sumMCReceiptDetailCustomers(prop) {
    //      let total = 0;
    //      for (let i = 0; i < this.mcReceiptDetailCustomers.length; i++) {
    //          total += this.mcReceiptDetailCustomers[i][pa
    //      return isNaN(total) ? 0 : total;
    //  }
    //
    //  getEmployeeByID(id) {
    //      if (this.accountingObjectData) {
    //          const epl = this.accountingObjectData.find(n => n.id === id);
    //          if (epl) {
    //              return epl.accountingObjectCode;
    //          } else {
    //              return '';
    //          }
    //      }
    //  }
    //
    //  viewVoucher(imcReceiptDetailCustomer: IMCReceiptDetailCustomer) {
    //      if ((this.isSoTaiChinh ? imcReceiptDetailCustomer.noFBook : imcReceiptDetailCustomer.noMBook) === 'OPN') {
    //          return;
    //      }
    //      let url = '';
    //      if (imcReceiptDetailCustomer.voucherTypeID === TypeID.BAN_HANG_CHUA_THU_TIEN) {
    //          url = `/#/ban-hang/chua-thu-tien/${imcReceiptDetailCustomer.saleInvoiceID}/edit/from-ref`;
    //      } else if (imcReceiptDetailCustomer.voucherTypeID === TypeID.CHUNG_TU_NGHIEP_VU_KHAC) {
    //          url = `/#/g-other-voucher/${imcReceiptDetailCustomer.saleInvoiceID}/edit/from-ref`;
    //      }
    //      window.open(url, '_blank');
    //  }
    //
    //  getEMContractsbyID(id) {
    //      if (this.eMContracts) {
    //          const eMC = this.eMContracts.find(n => n.id === id);
    //          if (eMC) {
    //              if (this.isSoTaiChinh) {
    //                  return eMC.noFBook;
    //              } else {
    //                  return eMC.noMBook;
    //              }
    //          }
    //      }
    //  }
    //
    //  /*Phiếu thu khách hàng*/
    //
    ngAfterViewChecked(): void {
        this.disableInput();
    }

    //
    //  saveDetails(i) {
    //      this.currentRow = i;
    //      this.details = this.tiIncrementDetail;
    //  }
    //
    //  saveParent() {
    //      this.currentRow = null;
    //      this.parent = this.mCReceipt;
    //  }
    //
    //  addDataToDetail() {
    //      this.tiIncrementDetail = this.details ? this.details : this.tiIncrementDetail;
    //      this.mCReceipt = this.parent ? this.parent : this.mCReceipt;
    //  }

    ngAfterViewInit(): void {
        if (window.location.href.includes('/ghi-tang-ccdc/new')) {
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

    getToolsActive() {
        if (this.tiIncrement && this.tiIncrement.id) {
            this.ghiTangService.getToolsActive().subscribe(res => {
                this.tools = res.body;
                if (this.tiIncrementDetail) {
                    this.tiIncrementDetail.forEach(item => {
                        item.toolsItem = this.tools.find(x => x.id === item.toolsID);
                        if (item.toolsItem) {
                            item.toolsName = item.toolsItem.toolsName;
                        }
                    });
                }
            });
            this.copy();
        }
        this.ghiTangService
            .getToolsActiveByIncrements({
                date: this.tiIncrement
                    ? this.tiIncrement.date instanceof moment
                        ? this.tiIncrement.date.format(DATE_FORMAT_SEARCH)
                        : moment(this.tiIncrement.date).format(DATE_FORMAT_SEARCH)
                    : ''
            })
            .subscribe(ref => {
                if (!this.tiIncrement || !this.tiIncrement.id) {
                    this.tools = ref.body;
                }
                this.tools2 = ref.body;
                this.copy();
            });
    }

    getOrganizationUnits() {
        this.organizationUnitService.getOrganizationUnits().subscribe(ref => {
            this.organizationUnits = ref.body;
            this.copy();
        });
    }

    changTools(detail: ITIIncrementDetails) {
        if (detail.toolsItem) {
            detail.toolsID = detail.toolsItem.id;
            detail.toolsName = detail.toolsItem.toolsName;
            detail.unitID = detail.toolsItem.unitID;
            detail.quantity = detail.toolsItem.quantity;
            detail.unitPrice = detail.toolsItem.unitPrice;
            detail.unitID = detail.toolsItem.unitID;
            this.sumAmount(detail);
            this.getOrganizationUnitByToolsID(detail.toolsID, detail);
        } else {
            detail.toolsID = '';
            detail.toolsName = '';
        }
    }

    // đơn vị tính
    getUnit() {
        this.unitService.getAllUnits().subscribe(ref => {
            this.units = ref.body;
            this.copy();
        });
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

    sumAmount(detail: ITIIncrementDetails) {
        detail.amount = (detail.quantity ? detail.quantity : 0) * (detail.unitPrice ? detail.unitPrice : 0);
        this.sumAllList();
    }

    getDataPopup() {
        this.eventManager.subscribe('selectPrepaidExpenseVoucher', ref => {
            this.tiIncrementDetailRefVoucher = [];
            this.tiIncrementDetailRefVoucher = ref.content;
            this.convertRefVouchers();
        });
    }

    convertRefVouchers() {
        if (this.tiIncrementDetailRefVoucher && this.tiIncrementDetailRefVoucher.length > 0) {
            for (let j = 0; j < this.tiIncrementDetailRefVoucher.length; j++) {
                this.tiIncrementDetailRefVoucher[j].id = null;
                if (this.tiIncrementDetailRefVoucher[j].refID1) {
                    this.tiIncrementDetailRefVoucher[j].refVoucherID = this.tiIncrementDetailRefVoucher[j].refID1;
                    this.tiIncrementDetailRefVoucher[j].refID2 = this.tiIncrementDetailRefVoucher[j].refID1;
                }
                this.tiIncrementDetailRefVoucher[j].orderPriority = j;
            }
        }
    }

    view(voucher) {
        // this.activeModal.dismiss(true);
        let url = '';
        switch (voucher.typeGroupID) {
            // Hàng bán trả lại
            case 33:
                url = `/#/hang-ban/tra-lai/${voucher.refVoucherID}/edit/from-ref`;
                break;
            // Giảm giá hàng bán
            case 34:
                url = `/#/hang-ban/giam-gia/${voucher.refVoucherID}/edit/from-ref`;
                break;
            // Xuất hóa đơn
            case 35:
                url = `/#/xuat-hoa-don/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 22:
                url = `/#/hang-mua/tra-lai/${voucher.refVoucherID}/view`;
                break;
            case 23:
                url = `/#/hang-mua/giam-gia/${voucher.refVoucherID}/view`;
                break;
            case 10:
                url = `/#/mc-receipt/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 16:
                url = `/#/mb-deposit/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 17:
                url = `/#/mb-credit-card/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 70:
                url = `/#/g-other-voucher/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 11:
                url = `/#/mc-payment/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 31:
                url = `/#/sa-order/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 24:
                url = `/#/mua-dich-vu/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 40:
                url = `/#/nhap-kho/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 20:
                url = `/#/don-mua-hang/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 41:
                url = `/#/xuat-kho/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 21:
                this.viewVoucherService.checkViaStockPPInvoice({ id: voucher.refVoucherID }).subscribe(
                    (res: HttpResponse<any>) => {
                        if (res.body.message === UpdateDataMessages.STOCK_TRUE) {
                            url = `/#/mua-hang/qua-kho-ref/${voucher.refVoucherID}/edit/1`;
                            window.open(url, '_blank');
                        } else if (res.body.message === UpdateDataMessages.STOCK_FALSE) {
                            url = `/#/mua-hang/khong-qua-kho-ref/${voucher.refVoucherID}/edit/1`;
                            window.open(url, '_blank');
                        } else {
                            this.toast.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                        }
                        return;
                    },
                    (res: HttpErrorResponse) => {
                        this.toast.error(this.translate.instant('ebwebApp.pPInvoice.error.default'));
                    }
                );
                return;
            case 18:
                url = `/#/mc-audit/${voucher.refVoucherID}/edit/from-ref`;
                break;
            case 32:
                url = `/#/chung-tu-ban-hang/${voucher.refID2}/edit/from-ref`;
                break;
            case 30:
                url = `/#/sa-quote/${voucher.refID2}/edit/from-ref`;
                break;
            case 12:
            case 13:
            case 14:
                url = `/#/mb-teller-paper/${voucher.refID2}/edit/from-ref`;
                break;
        }
        window.open(url, '_blank');
    }

    sumAllList() {
        this.sumQuantity = 0;
        this.sumUnitPrice = 0;
        this.sumAllAmount = 0;
        this.sumTotalAmountOriginal = 0;
        const lengthDetail: number = this.tiIncrementDetail.length;
        const lengthVoucher: number = this.tiIncrementDetailRefVoucher.length;
        const length = lengthDetail > lengthVoucher ? lengthDetail : lengthVoucher;
        for (let i = 0; i < length; i++) {
            if (i < lengthDetail) {
                if (this.tiIncrementDetail[i].quantity) {
                    this.sumQuantity += this.tiIncrementDetail[i].quantity;
                }
                if (this.tiIncrementDetail[i].unitPrice) {
                    this.sumUnitPrice += this.tiIncrementDetail[i].unitPrice;
                }
                if (this.tiIncrementDetail[i].amount) {
                    this.sumAllAmount += this.tiIncrementDetail[i].amount;
                }
            }
            if (i < lengthVoucher) {
                if (this.tiIncrementDetailRefVoucher[i].totalAmountOriginal) {
                    this.sumTotalAmountOriginal += this.tiIncrementDetailRefVoucher[i].totalAmountOriginal;
                }
            }
        }
        this.tiIncrement.totalAmount = this.sumAllAmount;
    }

    changeAmount(detail: ITIIncrementDetails) {
        if (detail.amount) {
            if (detail.quantity) {
                detail.unitPrice = detail.amount / detail.quantity;
            } else {
                detail.unitPrice = 0;
            }
        } else {
            detail.unitPrice = 0;
        }
        this.sumAllList();
    }

    getOrganizationUnitByToolsID(id: string, detail: ITIIncrementDetails) {
        this.ghiTangService
            .getOrganizationUnitByToolsIDTIIncrement({
                id,
                date: this.tiIncrement
                    ? this.tiIncrement.date instanceof moment
                        ? this.tiIncrement.date.format(DATE_FORMAT_SEARCH)
                        : moment(this.tiIncrement.date).format(DATE_FORMAT_SEARCH)
                    : ''
            })
            .subscribe(res => {
                detail.organizationUnits = res.body;
            });
    }

    changeOrganizationUnits(detail: ITIIncrementDetails, i: number) {
        const item = detail.organizationUnits.find(x => x.id === detail.departmentID);
        if (item) {
            detail.quantity = item.quantityRest;
        }
        this.sumAmount(detail);
    }
}
