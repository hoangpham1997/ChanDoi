import { AfterViewChecked, AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { AccountingObject, IAccountingObject } from 'app/shared/model/accounting-object.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { ActivatedRoute } from '@angular/router';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { MuaDichVuResult } from '../model/mua-dich-vu-result.model';
import { IMuaDichVuDetailResult, MuaDichVuDetailResult } from 'app/muahang/mua-dich-vu/model/mua-dich-vu-detail-result.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { MaterialGoods } from 'app/shared/model/material-goods.model';
import { AccountDefaultService } from 'app/danhmuc/account-default';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import {
    ACCOUNT_DETAIL_TYPE,
    AccountType,
    CURRENCY_ID,
    REPORT,
    SO_LAM_VIEC,
    TCKHAC_HANCHETK,
    TypeID,
    TOOLTIPS,
    SD_SO_QUAN_TRI,
    MSGERROR
} from 'app/app.constants';
import { Principal } from 'app/core';
import { getEmptyRow } from 'app/shared/util/row-util';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { UnitService } from 'app/danhmuc/unit';
import { AccountListService } from 'app/danhmuc/account-list';
import { IAccountList } from 'app/shared/model/account-list.model';
import { PpOrderModalComponent } from 'app/shared/modal/pp-order/pp-order-modal.component';
import { ACCOUNT_TYPE, GEN_CODE_TYPE_GROUP, PP_SERVICE_TYPE, UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { BankAccountDetails, IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { CreditCard, ICreditCard } from 'app/shared/model/credit-card.model';
import { CreditCardService } from 'app/danhmuc/credit-card';
import { AccountingObjectBankAccountService } from 'app/entities/accounting-object-bank-account';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { ExpenseItemService } from 'app/entities/expense-item';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { EMContractService } from 'app/entities/em-contract';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { CostSetService } from 'app/entities/cost-set';
import { BudgetItemService } from 'app/entities/budget-item';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { ONBROADCASTEVENT } from '../mua-dich-vu-event-name.constant';
import { MuaDichVuService } from '../mua-dich-vu.service';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { Irecord } from 'app/shared/model/record';
import { IRefVoucher, RefVoucher } from 'app/shared/model/ref-voucher.model';
import { IPPOrderDto } from 'app/shared/modal/pp-order/pp-order-dto.model';
import { PporderdetailService } from 'app/entities/pporderdetail';
import { IPporderdetail, PPOrderDetail } from 'app/shared/model/pporderdetail.model';
import { Subscription } from 'rxjs';
import { DiscountAllocationModalComponent } from 'app/shared/modal/discount-allocation/discount-allocation-modal.component';
import { ViewLiabilitiesComponent } from 'app/shared/modal/view-liabilities/view-liabilities.component';
import { DiscountAllocation, IDiscountAllocation } from 'app/shared/modal/discount-allocation/discount-allocation.model';
import { DialogDeleteComponent } from 'app/shared/modal/dialog-delete/dialog-delete.component';
import * as moment from 'moment';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { IAccountAllList } from 'app/shared/model/account-all-list.model';
import { Router } from '@angular/router';
import { PPOrderModalService } from 'app/shared/modal/pp-order/pp-order-modal.service';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { IMCPayment } from 'app/shared/model/mc-payment.model';
import { MCPaymentService } from 'app/TienMatNganHang/phieu-chi/mc-payment';
import { MBTellerPaperService } from 'app/TienMatNganHang/BaoNo/mb-teller-paper';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { IMBCreditCard } from 'app/shared/model/mb-credit-card.model';
import { MBCreditCardService } from 'app/TienMatNganHang/TheTinDung/mb-credit-card';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { Location } from '@angular/common';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { PporderService } from 'app/entities/pporder';
import { DATE_FORMAT_SLASH, DATE_FORMAT_TYPE1 } from 'app/shared';
import { IPPInvoiceDetails } from 'app/shared/model/pp-invoice-details.model';

@Component({
    selector: 'eb-tab-insert-update-receipt-mua-dich-vu',
    templateUrl: './tab-insert-update-receipt-mua-dich-vu.component.html',
    styleUrls: ['./tab-insert-update-receipt-mua-dich-vu.component.css']
})
export class TabInsertUpdateReceiptMuaDichVuComponent extends BaseComponent implements OnInit, AfterViewChecked, AfterViewInit {
    @ViewChild('quantityLargerModal') quantityLargerModal: any;
    @ViewChild('unRecordModal') unRecordModal: any;
    @ViewChild('content') content: any;
    @ViewChild('checkHadPaidModal') checkHadPaidModal: any;
    @ViewChild('checkHadReferencePaidModal') checkHadReferencePaidModal: any;
    @ViewChild('vatValidModal') vatValidModal: any;
    ROLE = ROLE;
    REPORT = REPORT;
    TOOLTIPS = TOOLTIPS;
    accountingObjects: IAccountingObject[];
    isRequiredAccountObjects: boolean;
    isRequiredAccountPayment: boolean;
    employees: IAccountingObject[];
    ppService: MuaDichVuResult;
    statusVoucher: number;
    currencies: ICurrency[];
    isForeignCurrency: boolean;
    // tabs detail
    materialGoodss: MaterialGoods[];
    accountDebit: IAccountList[];
    accountCredit: IAccountList[];
    accountDefault: { value?: string };
    contextMenu: ContextMenu;
    ppServiceTypePaid: string;
    eventSubscriber: Subscription;
    account: any;
    selectedRow: MuaDichVuResult;

    tabSelected: string;
    lastPaidType: string;
    // UNC
    PP_SERVICE_TYPE = PP_SERVICE_TYPE;
    GEN_CODE_TYPE_GROUP = GEN_CODE_TYPE_GROUP;
    bankAccountDetails: BankAccountDetails[];
    bankAccountDetailReceivers: IAccountingObjectBankAccount[];
    creditCards: CreditCard[];

    // valid
    taxCodeValid: boolean;

    goodsServicePurchases: IGoodsServicePurchase[];
    goodsServicePurchase: IGoodsServicePurchase;
    costSets: ICostSet[];
    expenseItems: IExpenseItem[];
    budgetItems: IBudgetItem[];
    statisticCodes: IStatisticsCode[];
    // emContracts: IEMContract[];
    organizationUnits: IOrganizationUnit[];

    // department
    modalRef: NgbModalRef;
    vatAccountList: IAccountList[];
    deductionDebitAccountList: any;
    isReadOnly: boolean;
    isFistItemSelected: boolean;
    isLastItemSelected: boolean;
    isRecording: boolean;
    currentBook: string;
    ppServiceOld: MuaDichVuResult;
    isFillReasonByAccountingObject: boolean;
    isFillOtherReasonByAccountingObject: boolean;
    isLoadNumberAttach: boolean;
    isFromOtherScene: boolean;
    isCheckPPOrderQuantity: boolean;
    itemUnSelected: any[];
    usingManagerBook: boolean;
    /*Phiếu chi Add by Hautv*/
    fromMCPayment: boolean;
    rowNum: number;
    count: number;
    searchVoucher: ISearchVoucher;
    mCPaymentID: string;
    private TYPE_MC_PAYMENT = 110;
    private TYPE_PPSERVICE = 114;
    private TYPE_PPINVOICE_MHQK = 115;
    /*Phiếu chi Add by Hautv*/

    /*Bao No Add by anmt*/
    fromMBTellerPaper: boolean;
    rowNumberMBTellerPaper: number;
    countMBTellerPaper: number;
    mBTellerPaperID: string;
    TYPE_BAONO_UNC = 120;
    TYPE_BAONO_SCK = 130;
    TYPE_BAONO_STM = 140;
    TYPE_UNC_PPINVOICE_MHQK = 125;
    TYPE_SCK_PPINVOICE_MHQK = 131;
    TYPE_STM_PPINVOICE_MHQK = 141;
    TYPE_UNC_PPSERVICE = 124;
    TYPE_SCK_PPSERVICE = 133;
    TYPE_STM_PPSERVICE = 143;
    /*Bao No Add by anmt*/

    /*Thẻ tín dụng*/
    fromMBCreditCard: boolean;
    mBCreditCardID: string;
    private TYPE_MB_CREDIT_CARD = 170;
    private TYPE_MDV_MB_CREDIT_CARD = 173;
    private TYPE_MHQK_MB_CREDIT_CARD = 171;
    dataVATRate = [{ name: '', value: null }, { name: '0%', value: 0 }, { name: '5%', value: 1 }, { name: '10%', value: 2 }];
    isLoading: boolean;
    vatValid: boolean;
    vatValidNUllCase: boolean;
    otherActionOnSuccess: any;
    saveContentValid: any;
    detailVATCheck: any;
    isCareerGroup: boolean;
    creditAccountDefault: any;
    debitAccountDefault: any;
    vatAccountDefault: any;
    deductionDebitAccountDefault: any;
    isShowPaid: any;

    constructor(
        public utilsService: UtilsService,
        private accountingObjectService: AccountingObjectService,
        private activatedRoute: ActivatedRoute,
        private jhiAlertService: JhiAlertService,
        private currencyService: CurrencyService,
        private accountDefaultService: AccountDefaultService,
        private toastrService: ToastrService,
        public translateService: TranslateService,
        private materialGoodsService: MaterialGoodsService,
        private principal: Principal,
        private unitService: UnitService,
        private accountListService: AccountListService,
        private creditCardService: CreditCardService,
        private bankAccountDetailService: BankAccountDetailsService,
        private accountingObjectBankAccountService: AccountingObjectBankAccountService,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        private expenseItemsService: ExpenseItemService,
        private statisticsCodeService: StatisticsCodeService,
        // private emContractService: EMContractService,
        private organizationUnitService: OrganizationUnitService,
        private costSetService: CostSetService,
        private budgetItemService: BudgetItemService,
        private refModalService: RefModalService,
        private eventManager: JhiEventManager,
        private ppServiceService: MuaDichVuService,
        private generalLedgerService: GeneralLedgerService,
        private pporderService: PporderService,
        private ppOrderDetailService: PporderdetailService,
        private modalService: NgbModal,
        private ppOrderModalService: PPOrderModalService,
        private router: Router,
        private mCPaymentService: MCPaymentService,
        private mBCreditCardService: MBCreditCardService,
        private mBTellerPaperService: MBTellerPaperService,
        private location: Location
    ) {
        super();
    }

    ngOnInit(): void {
        this.contextMenu = new ContextMenu();
        this.contextMenu.isShow = false;
        this.isFromOtherScene = this.ppServiceService.getIsFromOtherScene();
        this.ppOrderModalService.cleanData();
        this.onGetData();
        this.registerRef();
        this.tabSelected = this.PP_SERVICE_TYPE.PP_SERVICE_UNPAID;
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    registerLockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('lockSuccess', response => {
            this.ngOnInit();
        });
    }

    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.ngOnInit();
        });
    }

    onInitPPService(unLoadReadonly?: boolean) {
        /* Phiếu chi Add by Hautv */
        this.fromMCPayment = this.activatedRoute.snapshot.routeConfig.path.includes('from-mc-payment');
        if (this.fromMCPayment) {
            this.searchVoucher = JSON.parse(sessionStorage.getItem('searchVoucherMCPayment'));
            this.mCPaymentID = this.activatedRoute.snapshot.paramMap.get('mcpaymentID');
            this.utilsService
                .getIndexRow({
                    id: this.mCPaymentID,
                    isNext: true,
                    typeID: this.TYPE_MC_PAYMENT,
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
        /* Phiếu chi Add by Hautv */
        /* Bao No Add by anmt */
        this.fromMBTellerPaper = this.activatedRoute.snapshot.routeConfig.path.includes('from-mb-teller-paper');
        if (this.fromMBTellerPaper) {
            this.searchVoucher = JSON.parse(sessionStorage.getItem('sessionSearchVoucher'));
            this.mBTellerPaperID = this.activatedRoute.snapshot.paramMap.get('mBTellerPaperID');
            this.mBTellerPaperService
                .getIndexRow({
                    id: this.mBTellerPaperID,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: any) => {
                        this.rowNumberMBTellerPaper = res.body[0];
                        this.countMBTellerPaper = res.body[1];
                        console.log('dich vu: ' + res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
        /* Bao No Add by anmt */
        /* Thẻ tín dụng add by namnh */
        this.fromMBCreditCard = this.activatedRoute.snapshot.routeConfig.path.includes('from-mb-credit-card');
        if (this.fromMBCreditCard) {
            this.searchVoucher = JSON.parse(sessionStorage.getItem('dataSearchMBCreditCard'));
            this.mBCreditCardID = this.activatedRoute.snapshot.paramMap.get('mBCreditCardID');
            this.utilsService
                .getIndexRow({
                    id: this.mBCreditCardID,
                    isNext: true,
                    typeID: this.TYPE_MB_CREDIT_CARD,
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
        /* Thẻ tín dụng add by namnh*/
        /*  From view PPService*/
        if (this.activatedRoute.snapshot.routeConfig.path.includes('pp-service')) {
            this.isFromOtherScene = false;
        }
        if (this.isFromOtherScene || !this.ppServiceService.getPPServiceSelected().id) {
            this.setupPPService({
                body: {
                    result: this.ppServiceService.getPPServiceSelected() || new MuaDichVuResult(),
                    ...this.ppServiceService.getDataResult()
                }
            });
        } else {
            this.ppServiceService
                .findPPServiceByLocationItem({
                    action: 0,
                    ppServiceId: this.ppServiceService.getPPServiceSelected().id,
                    ...this.ppServiceService.getMuaDichVuSearchSnapShot()
                })
                .subscribe(res => this.setupPPService(res, unLoadReadonly));
        }
    }

    setupPPService(res, unLoadReadonly?) {
        this.ppService = res.body.result;
        const arrType = [
            this.PP_SERVICE_TYPE.PP_SERVICE_CASH,
            this.PP_SERVICE_TYPE.PP_SERVICE_PAYMENT_ORDER,
            this.PP_SERVICE_TYPE.PP_SERVICE_TRANSFER_SEC,
            this.PP_SERVICE_TYPE.PP_SERVICE_CASH_SEC,
            this.PP_SERVICE_TYPE.PP_SERVICE_CREDIT_CARD
        ];
        this.isShowPaid = arrType.includes(this.ppService.typeId + '');
        this.changeAmountPPService();
        this.ppServiceService.setPPServiceSelected(this.ppService);
        this.isFistItemSelected = false;
        this.isLastItemSelected = false;
        if (res.body.messages === UpdateDataMessages.IS_FIST_ITEM || res.body.rowNum === 1) {
            this.isFistItemSelected = true;
        } else if (res.body.messages === UpdateDataMessages.IS_LAST_ITEM || res.body.rowNum === res.body.totalRows) {
            this.isLastItemSelected = true;
        }
        if (this.account && this.account.organizationUnit && this.account.organizationUnit.currencyID) {
            this.ppService.currencyID = this.ppService.currencyID || this.account.organizationUnit.currencyID;
            this.isForeignCurrency = this.account.organizationUnit.currencyID !== this.ppService.currencyID;
        }
        if (this.ppService && this.ppService.rowNum) {
            if (this.ppService.rowNum > 0) {
                this.isLastItemSelected = true;
            } else {
                this.isFistItemSelected = true;
            }
        }
        this.currentBook = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
        this.usingManagerBook = this.account.systemOption.some(x => x.code === SD_SO_QUAN_TRI && x.data === '1');
        this.ppService.currentBook = this.currentBook;
        if (this.ppService.typeLedger === null || this.ppService.typeLedger === undefined) {
            if (this.usingManagerBook) {
                this.ppService.typeLedger = 2;
            } else {
                this.ppService.typeLedger = 0;
            }
        }

        if (!unLoadReadonly) {
            this.isReadOnly = this.ppServiceService.getReadOnly();
            this.isEdit = !this.isReadOnly;
        }
        this.ppService.ppServiceDetailDTOS = this.ppService.ppServiceDetailDTOS ? this.ppService.ppServiceDetailDTOS : [];
        for (let i = 0; i < this.ppService.ppServiceDetailDTOS.length; i++) {
            this.onSelectMaterialGoods(this.ppService.ppServiceDetailDTOS[i]);
            this.onLoadUnits(this.ppService.ppServiceDetailDTOS[i], false);
            this.ppService.ppServiceDetailDTOS[i].quantityFromDB = this.ppService.ppServiceDetailDTOS[i].quantity;
        }
        this.ppService.refVouchers = this.ppService.refVouchers ? this.ppService.refVouchers : [];
        this.ppServiceOld = this.utilsService.deepCopyObject(this.ppService);
        this.onGetEmployees();
        this.onGetAccountObjects(true);
        // this.onGetCurrencies();

        this.getAccountType();
        this.onGetAllCreditCards();
        this.onGetAllBankAccountDetails();

        this.onGetAllStatiscalData();
        this.onGetVATAcountList();
        this.onGetDeductionDebitAccountList();
        this.onLoadReceipt();
        this.onGetDetail();
        this.onLoadRefVoucher();
        this.onUpdateContextMenu();
        this.onLoadMaterialGoods();
        this.onSetUpPPServiceType();
        this.changeAmountPPService();
        this.getAccountType();
        if (this.ppServiceService.getEdit()) {
            this.edit();
        }
    }

    onUpdateContextMenu() {
        if (this.contextMenu) {
            // this.contextMenu.isShow = !this.isReadOnly;
        }
    }

    onSetUpPPServiceType() {
        this.ppServiceTypePaid =
            this.ppService && this.ppService.typeId && this.ppService.typeId.toString() !== this.PP_SERVICE_TYPE.PP_SERVICE_UNPAID
                ? this.ppService.typeId.toString()
                : null;
        this.ppService.receiptType =
            this.ppService && this.ppService.typeId ? this.ppService.typeId.toString() : this.PP_SERVICE_TYPE.PP_SERVICE_UNPAID;
    }

    onGetData() {
        this.onGetCurrencies();
        this.principal.identity().then(account => {
            this.account = account;
            this.isCareerGroup = true;
            if (this.account.organizationUnit.taxCalculationMethod === 1) {
                this.isCareerGroup = false;
            } else {
                this.onGetGoodsServicePurchases();
            }
            this.isReadOnly = true;
            this.onInitPPService();
        });
    }

    onGetDetail() {
        if (this.ppService.ppServiceDetailDTOS && this.ppService.ppServiceDetailDTOS.length === 0) {
            this.ppServiceService
                .findAllPPServiceDetails({ ppServiceId: this.ppService.id })
                .subscribe((res: HttpResponse<IMuaDichVuDetailResult[]>) => {
                    this.ppService.ppServiceDetailDTOS = res.body;
                });
        }
    }

    onLoadRefVoucher() {
        if (this.ppService.refVouchers && this.ppService.refVouchers.length === 0 && this.ppService.id) {
            this.ppServiceService
                .findRefVoucherByRefId({ refId: this.ppService.id, currentBook: this.ppService.currentBook })
                .subscribe((res: HttpResponse<IRefVoucher[]>) => {
                    this.ppService.refVouchers = res.body;
                });
        }
    }

    onGetDeductionDebitAccountList() {
        this.accountListService
            .getAccountType({
                typeID: parseInt(this.PP_SERVICE_TYPE.PP_SERVICE_UNPAID, 0),
                authoritiesCode: TCKHAC_HANCHETK,
                columnName: AccountType.TKDU_THUE_GTGT,
                valueCheck: '1',
                delimiter: ';',
                ppType: false
            })
            .subscribe(res => {
                this.deductionDebitAccountList = res.body;
            });
    }

    onGetVATAcountList() {
        this.accountListService
            .getAccountType({
                typeID: parseInt(this.PP_SERVICE_TYPE.PP_SERVICE_UNPAID, 0),
                authoritiesCode: TCKHAC_HANCHETK,
                columnName: AccountType.TK_THUE_GTGT,
                valueCheck: '1',
                delimiter: ';',
                ppType: false
            })
            .subscribe(res => {
                this.vatAccountList = res.body;
            });
    }

    onGetAllStatiscalData() {
        this.statisticsCodeService.getStatisticsCodes().subscribe((res: HttpResponse<IStatisticsCode[]>) => {
            this.statisticCodes = res.body.filter(statisticsCode => statisticsCode.isActive);
        });
        this.costSetService.getCostSets().subscribe((res: HttpResponse<ICostSet[]>) => {
            this.costSets = res.body.filter(costSet => costSet.isActive);
        });
        this.expenseItemsService.getExpenseItems().subscribe((res: HttpResponse<IExpenseItem[]>) => {
            this.expenseItems = res.body.filter(expenseItem => expenseItem.isActive);
        });
        // this.emContractService.getEMContracts().subscribe((res: HttpResponse<IEMContract[]>) => {
        //     this.emContracts = res.body.filter(contract => contract.isActive);
        // });
        this.organizationUnitService.getOrganizationUnits().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body.filter(department => department.isActive && department.unitType === 4);
        });
        this.budgetItemService.getBudgetItems().subscribe((res: HttpResponse<IBudgetItem[]>) => {
            this.budgetItems = res.body.filter(budget => budget.isActive);
        });
    }

    // todo: fill this to form
    onGetDefaultGoodsServicePurchase() {
        this.goodsServicePurchaseService.getPurchaseName().subscribe(ref => {
            this.goodsServicePurchase = ref.body;
            if (this.ppService.ppServiceDetailDTOS) {
                this.ppService.ppServiceDetailDTOS.forEach(detail => {
                    // if (!detail.goodsServicePurchaseId) {
                    //     detail.goodsServicePurchaseId = this.goodsServicePurchase.id;
                    // }
                });
            }
        });
    }

    onGetGoodsServicePurchases() {
        this.goodsServicePurchaseService.getGoodServicePurchases().subscribe((res: HttpResponse<IGoodsServicePurchase[]>) => {
            this.goodsServicePurchases = res.body;
            this.goodsServicePurchases.sort((a, b) => a.goodsServicePurchaseCode.localeCompare(b.goodsServicePurchaseCode));
            this.onGetDefaultGoodsServicePurchase();
        });
    }

    onLoadBankAccountDetailReceiversByAccountingObjectId() {
        if (this.ppService.accountingObjectID) {
            this.accountingObjectBankAccountService
                .getByAccountingObjectById({
                    accountingObjectID: this.ppService.accountingObjectID
                })
                .subscribe(ref => {
                    this.bankAccountDetailReceivers = ref.body;
                    this.bankAccountDetailReceivers.sort((a, b) => a.bankAccount.localeCompare(b.bankAccount));
                    this.onSelectBankAccountDetailReceiver();
                });
        }
    }

    onGetAllCreditCards() {
        this.creditCardService.getCreditCardsByCompanyID().subscribe((res: HttpResponse<ICreditCard[]>) => {
            this.creditCards = res.body.filter(n => n.isActive);
            this.creditCards.sort((a, b) => a.creditCardNumber.localeCompare(b.creditCardNumber));
            if (this.ppService.creditCardNumber) {
                this.onChangeCreditCard();
            }
        });
    }

    onGetAllBankAccountDetails() {
        this.bankAccountDetailService.getBankAccountDetails().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
            this.bankAccountDetails = res.body;
            this.bankAccountDetails.sort((a, b) => a.bankAccount.localeCompare(b.bankAccount));
        });
    }

    getGenCodeTypeGroup() {
        switch (this.ppService.receiptType) {
            case this.PP_SERVICE_TYPE.PP_SERVICE_UNPAID:
                return this.GEN_CODE_TYPE_GROUP.PP_SERVICE_LICENSE;
            case this.PP_SERVICE_TYPE.PP_SERVICE_CREDIT_CARD:
                return this.GEN_CODE_TYPE_GROUP.CREDIT_CARD;
            case this.PP_SERVICE_TYPE.PP_SERVICE_PAYMENT_ORDER:
                return this.GEN_CODE_TYPE_GROUP.PAYMENT_ORDER;
            case this.PP_SERVICE_TYPE.PP_SERVICE_TRANSFER_SEC:
                return this.GEN_CODE_TYPE_GROUP.TRANSFER_SEC;
            case this.PP_SERVICE_TYPE.PP_SERVICE_CASH:
                return this.GEN_CODE_TYPE_GROUP.MC_PAYMENT;
            case this.PP_SERVICE_TYPE.PP_SERVICE_CASH_SEC:
                return this.GEN_CODE_TYPE_GROUP.CASH_SEC;
            default:
                return null;
        }
    }

    onLoadReceipt(isReGen?: boolean, notRenderReceiptDate?: boolean) {
        if (this.account && (!this.ppService.noBook || isReGen)) {
            if (!notRenderReceiptDate) {
                this.ppService.receiptDate = this.utilsService.ngayHachToan(this.account).format('DD/MM/YYYY');
                this.ppService.postedDate = this.ppService.receiptDate;
            }

            if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                this.ppService.currencyID = this.ppService.currencyID || this.account.organizationUnit.currencyID;
                this.ppServiceOld = this.utilsService.deepCopyObject(this.ppService);
                this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                    this.currencies = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                    if (!this.ppService.exchangeRate) {
                        this.selectCurrency(false);
                    }
                });
            }
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: this.GEN_CODE_TYPE_GROUP.PP_SERVICE_LICENSE, // typeGroupID loại chứng từ
                    displayOnBook: this.ppService.currentBook // 0 - sổ tài chính, 1 - sổ quản trị, 2-cả hai sổ
                })
                .subscribe((res: HttpResponse<string>) => {
                    this.ppService.noBook = res.body;
                });
        }
    }

    onLoadMaterialGoods() {
        this.materialGoodsService.queryForComboboxGood({ materialsGoodsType: [2] }).subscribe(
            (res: HttpResponse<any>) => {
                this.materialGoodss = res.body;
                for (let i = 0; i < this.ppService.ppServiceDetailDTOS.length; i++) {
                    this.onSelectMaterialGoods(this.ppService.ppServiceDetailDTOS[i]);
                }
                this.materialGoodss.sort((a, b) => a.materialGoodsCode.localeCompare(b.materialGoodsCode));
            },
            (res: HttpErrorResponse) => console.log(res.message)
        );
    }

    getAccountType() {
        const typeID: number = this.ppService.typeId;
        const columnList = [
            { column: AccountType.TK_CO, ppType: false },
            { column: AccountType.TK_NO, ppType: false },
            { column: AccountType.TK_THUE_GTGT, ppType: false },
            { column: AccountType.TKDU_THUE_GTGT, ppType: false }
        ];
        const param = {
            typeID: typeID,
            columnName: columnList
        };
        this.accountListService.getAccountTypeFour(param).subscribe(res => {
            const dataAccount: IAccountAllList = res.body;
            this.accountCredit = dataAccount.creditAccount;
            this.accountDebit = dataAccount.debitAccount;
            this.vatAccountList = dataAccount.vatAccount;
            this.deductionDebitAccountList = dataAccount.deductionDebitAccount;
            this.creditAccountDefault = res.body.creditAccountDefault;
            this.debitAccountDefault = res.body.debitAccountDefault;
            this.vatAccountDefault = res.body.vatAccountDefault;
            this.deductionDebitAccountDefault = res.body.deductionDebitAccountDefault;
        });
    }

    onGetCurrencies() {
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencies = res.body;
            this.currencies.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
        });
    }

    //
    onGetAccountObjects(isInit?: boolean) {
        this.accountingObjectService
            .getAccountingObjectDTOByTaskMethod({ taskMethod: 0 })
            .subscribe((res: HttpResponse<IAccountingObject[]>) => {
                this.accountingObjects = res.body;
                this.accountingObjects.sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                this.onSelectAccountingObject(isInit ? isInit : false);
            });
    }

    onGetEmployees() {
        this.accountingObjectService.getAllDTO().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.employees = res.body
                .filter(n => n.isEmployee)
                .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
        });
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onSelectAccountingObject(isInit?: boolean) {
        if (!isInit && this.ppService.accountingObjectID) {
            const acc: IAccountingObject = this.accountingObjects.find(n => n.id === this.ppService.accountingObjectID);
            this.ppService.accountingObjectName = acc.accountingObjectName;
            this.ppService.accountingObjectCode = acc.accountingObjectCode;
            this.ppService.accountingObjectAddress = acc.accountingObjectAddress;
            this.ppService.companyTaxCode = acc.taxCode;
            this.ppService.contactName = acc.contactName;

            for (let i = 0; i < this.ppService.ppServiceDetailDTOS.length; i++) {
                this.ppService.ppServiceDetailDTOS[i].postedObjectId = this.ppService.accountingObjectID;
                this.ppService.ppServiceDetailDTOS[i].postedObjectCode = this.ppService.accountingObjectCode;
            }
            if (!this.isFillReasonByAccountingObject) {
                this.ppService.reason = this.getReason();
            }
            if (!this.isFillOtherReasonByAccountingObject) {
                if (this.ppService.receiptType === this.PP_SERVICE_TYPE.PP_SERVICE_CASH) {
                    this.ppService.otherReason = this.getCashTypeReason();
                } else {
                    this.ppService.otherReason = this.getOtherReason();
                }
            }
        }
        this.onLoadBankAccountDetailReceiversByAccountingObjectId();
        this.ppService.accountReceiverId = null;
        this.ppService.accountReceiverName = null;
        if (!this.ppService.accountingObjectID) {
            this.ppService.accountingObjectName = null;
            this.ppService.accountingObjectCode = null;
            this.ppService.accountingObjectAddress = null;
            this.ppService.companyTaxCode = null;
            this.ppService.contactName = null;
            this.ppService.reason = this.translateService.instant('ebwebApp.muaHang.muaDichVu.title.receiptReason');
        }
    }

    getReason(name?) {
        const accountName = name || this.ppService.accountingObjectName;
        return this.translateService.instant('ebwebApp.muaHang.muaDichVu.title.receiptReason') + ' của ' + accountName;
    }

    getCashTypeReason(name?) {
        const accountName = name || this.ppService.accountingObjectName;
        return this.translateService.instant('ebwebApp.muaHang.muaDichVu.title.cashTypeReason') + ' của ' + accountName;
    }

    getOtherReason(name?) {
        const accountName = name || this.ppService.accountingObjectName;
        return this.translateService.instant('ebwebApp.muaHang.muaDichVu.title.otherReason') + ' của ' + accountName;
    }

    selectEmployee() {
        if (this.ppService.employeeID) {
            const acc: IAccountingObject = this.accountingObjects.find(n => n.id === this.ppService.employeeID);
            if (acc) {
                this.ppService.employeeName = acc.accountingObjectName;
            }
        }
    }

    selectCurrency(isNew) {
        const currency = this.currencies.find(n => n.currencyCode === this.ppService.currencyID);
        if (currency) {
            this.ppService.exchangeRate = currency.exchangeRate;
            this.ppService.totalAmount = 0;
            this.ppService.totalAmountOriginal = 0;
            this.ppService.totalVATAmount = 0;
            this.ppService.totalVATAmountOriginal = 0;
            this.ppService.totalAmount = 0;
            if (this.account && this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                this.isForeignCurrency = this.account.organizationUnit.currencyID !== this.ppService.currencyID;
            }
            for (const detail of this.ppService.ppServiceDetailDTOS) {
                this.onLoadUnits(detail, isNew);
            }
            this.onUpdateExChangeRate();
            this.changeAmountPPService();
        }
    }

    onUpdateExChangeRate() {
        for (let i = 0; i < this.ppService.ppServiceDetailDTOS.length; i++) {
            const detail = this.ppService.ppServiceDetailDTOS[i];
            this.donGiaQD(detail);
            // this.thanhTien(detail);
            this.thanhTienQD(detail);
            // this.tienChietKhau(detail);
            this.tienChietKhauQD(detail);
            // this.tienThueGTGT(detail);
            this.tienThueGTGTQD(detail);
            this.changeAmountPPService();
        }
    }

    selectChangeMaterialGoods(detail: MuaDichVuDetailResult) {
        this.onSelectMaterialGoods(detail, true);
    }

    onSelectMaterialGoods(detail: MuaDichVuDetailResult, isNew?, notChangeQuantity?) {
        if (!this.materialGoodss || !detail) {
            return;
        }
        const materialTemp = this.materialGoodss.find(n => n.id === detail.materialGoodsId);
        if (materialTemp) {
            detail.materialGoodsCode = materialTemp.materialGoodsCode;
            detail.materialGoodsName = materialTemp.materialGoodsName;
            detail.materialGoodsId = materialTemp.id;
            if (this.currencies.find(x => x.currencyCode === this.ppService.currencyID).formula === '/') {
                detail.unitPrice = detail.unitPriceOriginal / this.ppService.exchangeRate;
            } else {
                detail.unitPrice = detail.unitPriceOriginal * this.ppService.exchangeRate;
            }

            detail.vatDescription = 'Thuế GTGT ' + detail.materialGoodsCode;
            detail.unitId = materialTemp.unitID;
            if (isNew) {
                detail.vatRate = materialTemp.vatTaxRate;
                if (!notChangeQuantity && materialTemp.minimumStock) {
                    detail.quantity = materialTemp.minimumStock;
                }

                detail.discountRate = materialTemp.purchaseDiscountRate;
                if (this.isCareerGroup && this.goodsServicePurchase && this.goodsServicePurchase.id) {
                    detail.goodsServicePurchaseId = this.goodsServicePurchase.id;
                }
            }
        }
        this.onLoadUnits(detail, isNew);
    }

    onLoadUnits(detail: MuaDichVuDetailResult, isNew) {
        const materialGoodsId = detail.materialGoodsId;
        // đơn vị tính
        this.unitService
            .convertRateForMaterialGoodsComboboxCustom({
                materialGoodsId
            })
            .subscribe(ref => {
                detail.units = ref.body;
                // đơn vị tính chính
                this.unitService
                    .getMainUnitName({
                        materialGoodsId: detail.materialGoodsId
                    })
                    .subscribe(ref2 => {
                        detail.unitName = ref2.body.unitName;
                        this.unitService
                            .unitPriceOriginalForMaterialGoods({
                                materialGoodsId: detail.materialGoodsId,
                                unitId: detail.unitId || '',
                                currencyCode: this.ppService.currencyID
                            })
                            .subscribe(
                                res => {
                                    detail.unitPrices = [];
                                    for (const item of res.body) {
                                        detail.unitPrices.push({ data: item });
                                    }
                                    if (detail.unitPrices.length && detail.unitPrices[0] && isNew) {
                                        detail.unitPriceOriginal = detail.unitPrices[0].data;
                                    }
                                    // this.donGiaQD(detail);
                                    // this.thanhTien(detail);
                                    // this.thanhTienQD(detail);
                                    // this.tienChietKhau(detail);
                                    // this.tienChietKhauQD(detail);
                                    // this.tienThueGTGT(detail);
                                    // this.tienThueGTGTQD(detail);
                                    this.changeAmountPPService();
                                },
                                error => console.log(error)
                            );
                    });
            });
    }

    onSelectPostObject(i: number) {
        const postedObject: AccountingObject = this.accountingObjects.find(
            n => n.id === this.ppService.ppServiceDetailDTOS[i].postedObjectId
        );
        this.ppService.ppServiceDetailDTOS[i].postedObjectCode = postedObject.accountingObjectCode;
    }

    addNewRow(select: number) {
        if (this.isReadOnly) {
            return;
        }
        const ppServiceDetail: MuaDichVuDetailResult = new MuaDichVuDetailResult();
        ppServiceDetail.postedObjectId = this.ppService.accountingObjectID;
        ppServiceDetail.postedObjectCode = this.ppService.accountingObjectCode;
        if (this.isCareerGroup && this.goodsServicePurchase && this.goodsServicePurchase.id) {
            ppServiceDetail.goodsServicePurchaseId = this.goodsServicePurchase.id;
        }
        ppServiceDetail.debitAccount = this.debitAccountDefault ? this.debitAccountDefault : null;
        ppServiceDetail.creditAccount = this.creditAccountDefault ? this.creditAccountDefault : null;
        ppServiceDetail.vatAccount = this.vatAccountDefault ? this.vatAccountDefault : null;
        ppServiceDetail.deductionDebitAccount = this.deductionDebitAccountDefault ? this.deductionDebitAccountDefault : null;
        this.setPPServiceDetailValueDefault(ppServiceDetail);
        this.ppService.ppServiceDetailDTOS.push(ppServiceDetail);
        this.focusInputWhenAddNewRow(this.ppService.ppServiceDetailDTOS.length - 1);
    }

    setPPServiceDetailValueDefault(ppServiceDetail: MuaDichVuDetailResult) {
        ppServiceDetail.amount = 0;
        ppServiceDetail.amountOriginal = 0;
        ppServiceDetail.discountAmountOriginal = 0;
        ppServiceDetail.discountAmount = 0;
        ppServiceDetail.unitPrice = 0;
        ppServiceDetail.unitPriceOriginal = 0;
    }

    keyPress(value: number, select: number, isVoucher?: boolean) {
        if (this.isReadOnly) {
            return;
        }
        if (isVoucher) {
            this.ppService.refVouchers.splice(value, 1);
        } else {
            if (this.ppService.ppServiceDetailDTOS[value].ppOrderId) {
                if (
                    this.ppService.ppServiceDetailDTOS.filter(x => x.ppOrderId === this.ppService.ppServiceDetailDTOS[value].ppOrderId)
                        .length === 1
                ) {
                    this.ppService.refVouchers = this.ppService.refVouchers.filter(
                        x => x.refID2 !== this.ppService.ppServiceDetailDTOS[value].ppOrderId
                    );
                }
            }

            this.ppService.ppServiceDetailDTOS.splice(value, 1);
        }
    }

    getEmptyRow(data: MuaDichVuDetailResult[]) {
        return getEmptyRow(data, 10);
    }

    onRightClick($event, data, selectedData, isNew, isDelete, isCopy) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.isCopy = isCopy;
        this.contextMenu.selectedData = selectedData;
    }

    closeContextMenu() {
        if (this.contextMenu) {
            this.contextMenu.isShow = false;
        }
    }

    selectUnit(detail: MuaDichVuDetailResult) {
        this.onSelectMaterialGoods(detail, true);
    }

    selectDebitAccount(detail: MuaDichVuDetailResult) {
        this.checkRequire();
    }

    selectCreditAccount(detail: MuaDichVuDetailResult) {
        this.checkRequire();
    }

    checkRequire() {
        this.isRequiredAccountObjects = false;
        this.isRequiredAccountPayment = false;
        for (const detail of this.ppService.ppServiceDetailDTOS) {
            const creditAccount = this.accountCredit.find(x => x.accountNumber === detail.creditAccount);
            const debitAccount = this.accountDebit.find(x => x.accountNumber === detail.debitAccount);
            if (creditAccount && creditAccount.detailType === ACCOUNT_DETAIL_TYPE.ACCOUNT_SUPPLIER && !this.ppService.accountingObjectID) {
                this.isRequiredAccountObjects = true;
            }
            if (debitAccount && debitAccount.detailType === ACCOUNT_DETAIL_TYPE.ACCOUNT_SUPPLIER && !this.ppService.accountingObjectID) {
                this.isRequiredAccountObjects = true;
            }
            if (
                creditAccount &&
                creditAccount.detailType === ACCOUNT_DETAIL_TYPE.ACCOUNT_DEBIT &&
                !this.ppService.accountPaymentId &&
                (this.PP_SERVICE_TYPE.PP_SERVICE_PAYMENT_ORDER === this.ppService.receiptType ||
                    this.PP_SERVICE_TYPE.PP_SERVICE_TRANSFER_SEC === this.ppService.receiptType ||
                    this.PP_SERVICE_TYPE.PP_SERVICE_CASH_SEC === this.ppService.receiptType)
            ) {
                this.isRequiredAccountPayment = true;
            }
            if (
                debitAccount &&
                debitAccount.detailType === ACCOUNT_DETAIL_TYPE.ACCOUNT_DEBIT &&
                !this.ppService.accountPaymentId &&
                (this.PP_SERVICE_TYPE.PP_SERVICE_PAYMENT_ORDER === this.ppService.receiptType ||
                    this.PP_SERVICE_TYPE.PP_SERVICE_TRANSFER_SEC === this.ppService.receiptType ||
                    this.PP_SERVICE_TYPE.PP_SERVICE_CASH_SEC === this.ppService.receiptType)
            ) {
                this.isRequiredAccountPayment = true;
            }
        }
    }

    onChangePPServiceTypePaid() {
        if (!this.ppServiceTypePaid) {
            this.ppServiceTypePaid = this.lastPaidType || this.PP_SERVICE_TYPE.PP_SERVICE_CASH;
        }
        if (this.tabSelected !== this.PP_SERVICE_TYPE.PP_SERVICE_UNPAID) {
            this.tabSelected = this.ppServiceTypePaid;
        }
        this.lastPaidType = this.ppServiceTypePaid;
        this.ppService.receiptType = this.ppServiceTypePaid;
        this.ppService.typeId = parseInt(this.ppService.receiptType, 0);
        this.onLoadOtherCodeVoucher();
        this.getAccountType();
        this.ppService.ppServiceDetailDTOS.forEach(item => {
            item.debitAccount = null;
            item.creditAccount = null;
            item.vatAccount = null;
            item.deductionDebitAccount = null;
        });
        if (this.ppService.receiptType === this.PP_SERVICE_TYPE.PP_SERVICE_CASH) {
            this.ppService.otherReason = this.translateService.instant('ebwebApp.muaHang.muaDichVu.title.cashTypeReason');
        } else {
            this.ppService.otherReason = this.translateService.instant('ebwebApp.muaHang.muaDichVu.title.otherReason');
        }

        this.isFillOtherReasonByAccountingObject = false;
        this.checkRequire();
    }

    onLoadOtherCodeVoucher() {
        if (this.ppService.typeId.toString() === PP_SERVICE_TYPE.PP_SERVICE_UNPAID) {
            return;
        }
        this.utilsService
            .getGenCodeVoucher({
                typeGroupID: this.getGenCodeTypeGroup(), // typeGroupID loại chứng từ
                displayOnBook: parseInt(this.ppService.currentBook, 0) // 0 - sổ tài chính, 1 - sổ quản trị,
            })
            .subscribe((res: HttpResponse<string>) => {
                this.ppService.otherNoBook = res.body;
            });
    }

    onSelectBankAccountDetailReceiver() {
        const accountReceiver = this.bankAccountDetailReceivers.find(b => b.id === this.ppService.accountReceiverId);
        if (accountReceiver) {
            this.ppService.accountReceiverName = accountReceiver.bankName;
        }
    }

    onChangeTaxCode() {
        if (this.utilsService.checkMST(this.ppService.companyTaxCode)) {
            this.taxCodeValid = true;
        } else {
            this.taxCodeValid = false;
        }
    }

    public beforeChange($event: NgbTabChangeEvent) {
        switch ($event.nextId) {
            case 'muaDichVu-tab-pp-reference':
                $event.preventDefault();
                if (!this.isReadOnly) {
                    this.modalRef = this.refModalService.open(this.ppService.refVouchers);
                }
                break;
            case 'muaDichVu-tab-pp-order':
                $event.preventDefault();
                if (!this.isReadOnly) {
                    this.modalRef = this.refModalService.open(this.ppService.refVouchers, PpOrderModalComponent, {
                        accountObjects: this.accountingObjects,
                        accountObject: this.accountingObjects.find(x => x.id === this.ppService.accountingObjectID),
                        itemsSelected: this.ppService.ppServiceDetailDTOS.filter(x => x.ppOrderDetailId),
                        typeMG: 2,
                        itemUnSelected: this.itemUnSelected,
                        currency: this.ppService.currencyID
                    });
                }

                break;
            case 'muaDichVu-tab-discount-allocation':
                $event.preventDefault();
                if (!this.isReadOnly) {
                    const discountAllocations: IDiscountAllocation[] = [];
                    for (let i = 0; i < this.ppService.ppServiceDetailDTOS.length; i++) {
                        const discountAllocation: IDiscountAllocation = new DiscountAllocation();
                        discountAllocation.productCode = this.ppService.ppServiceDetailDTOS[i].materialGoodsCode;
                        discountAllocation.reason = this.ppService.ppServiceDetailDTOS[i].materialGoodsName;
                        discountAllocation.quantity = this.ppService.ppServiceDetailDTOS[i].quantity;
                        discountAllocation.amountOriginal = this.ppService.ppServiceDetailDTOS[i].amount;
                        discountAllocation.object = this.ppService.ppServiceDetailDTOS[i];
                        discountAllocations.push(discountAllocation);
                    }
                    this.modalRef = this.refModalService.open(null, DiscountAllocationModalComponent, {
                        discountAllocations
                    });
                }

                break;
            case 'muaDichVu-tab-liabilities':
                $event.preventDefault();
                if (!this.isReadOnly) {
                    if (this.ppService.postedDate && this.ppService.accountingObjectID) {
                        this.modalRef = this.refModalService.open(
                            this.ppService.refVouchers,
                            ViewLiabilitiesComponent,
                            {
                                postDate: this.ppService.postedDate,
                                objectCode: this.ppService.accountingObjectCode,
                                objectId: this.ppService.accountingObjectID,
                                currencyID: this.ppService.currencyID
                            },
                            true,
                            null,
                            'mini-dialog'
                        );
                    } else {
                        this.toastrService.error(
                            !this.ppService.accountingObjectID
                                ? this.translateService.instant('global.commonInfo.nonSelectAccountingObject')
                                : this.translateService.instant('global.commonInfo.nonSelectPostDate')
                        );
                    }
                }
                break;
        }
    }

    registerRef() {
        this.registerLockSuccess();
        this.registerUnlockSuccess();
        this.eventSubscriber = this.eventManager.subscribe(ONBROADCASTEVENT.afterDeleteRow, response => {
            if (response.content.ppOrderDetailId && response.content.quantityFromDB) {
                this.itemUnSelected.push(response.content);
            }
            for (let i = 0; i < this.ppService.ppServiceDetailDTOS.length; i++) {
                this.onSelectMaterialGoods(this.ppService.ppServiceDetailDTOS[i]);
            }
        });

        this.eventSubscriber = this.eventManager.subscribe(ONBROADCASTEVENT.selectViewVoucher, response => {
            this.ppService.refVouchers = response.content;
        });

        this.eventSubscriber = this.eventManager.subscribe(ONBROADCASTEVENT.selectedPPOrder, response => {
            if (response.content) {
                if (response.content.length === 0) {
                    this.onRemovePPServiceDetails([]);
                }
                const dataHaving = response.content.map(x => x.id);
                let countSubscriber = 0;
                for (let i = 0; i < response.content.length; i++) {
                    this.ppOrderDetailService.find(response.content[i].id).subscribe(res => {
                        this.pporderService.findById(response.content[i].ppOrderId).subscribe(resPPOrder => {
                            const refVoucher: IRefVoucher = new RefVoucher();
                            refVoucher.refID2 = resPPOrder.body.id;
                            refVoucher.no = resPPOrder.body.no;
                            refVoucher.reason = resPPOrder.body.reason;
                            refVoucher.date = moment(resPPOrder.body.date).format(DATE_FORMAT_SLASH);
                            if (this.ppService.refVouchers.findIndex(x => x.refID2 === refVoucher.refID2) < 0) {
                                this.ppService.refVouchers.push(refVoucher);
                            }
                        });
                        const haveInList = this.ppService.ppServiceDetailDTOS.some(x => x.ppOrderDetailId === response.content[i].id);
                        if (!haveInList) {
                            this.ppService.ppServiceDetailDTOS.push(this.onSubscribePPOrder(res.body, response.content[i]));
                            const index = this.ppService.ppServiceDetailDTOS.length - 1;
                            this.onSelectMaterialGoods(this.ppService.ppServiceDetailDTOS[index], true);
                            // dataHaving = dataHaving.filter(x => x === response.content[i].id);
                            countSubscriber++;
                            if (countSubscriber === response.content.length) {
                                this.onRemovePPServiceDetails(dataHaving);
                            }
                        } else {
                            for (let j = 0; j < this.ppService.ppServiceDetailDTOS.length; j++) {
                                response.content[i].priority = 0;
                                if (this.ppService.ppServiceDetailDTOS[j].ppOrderDetailId === response.content[i].id) {
                                    this.ppService.ppServiceDetailDTOS[j] = this.onSubscribePPOrder(
                                        res.body,
                                        response.content[i],
                                        this.ppService.ppServiceDetailDTOS[j]
                                    );
                                    this.onSelectMaterialGoods(this.ppService.ppServiceDetailDTOS[j], false);
                                    // dataHaving = dataHaving.filter(x => x === response.content[i].id);
                                    countSubscriber++;
                                    if (countSubscriber === response.content.length) {
                                        this.onRemovePPServiceDetails(dataHaving);
                                    }
                                }
                            }
                        }
                        this.ppService.ppServiceDetailDTOS = this.ppService.ppServiceDetailDTOS.sort(
                            (a, b) => a.newPriority - b.newPriority
                        );
                    });
                }
            }
        });

        this.eventSubscriber = this.eventManager.subscribe(ONBROADCASTEVENT.updateDiscountAllocations, response => {
            const discountAllocations: IDiscountAllocation[] = response.content;
            let totalDisCountOriginal = 0;
            let totalDisCount = 0;
            for (let i = 0; i < discountAllocations.length; i++) {
                for (let j = 0; j < this.ppService.ppServiceDetailDTOS.length; j++) {
                    if (discountAllocations[i].object === this.ppService.ppServiceDetailDTOS[j]) {
                        this.ppService.ppServiceDetailDTOS[j].discountAmountOriginal = discountAllocations[i].discountAmountOriginal;
                        if (this.currencies.find(x => x.currencyCode === this.ppService.currencyID).formula === '*') {
                            this.ppService.ppServiceDetailDTOS[j].discountAmount =
                                this.ppService.ppServiceDetailDTOS[j].discountAmountOriginal * this.ppService.exchangeRate;
                        } else {
                            this.ppService.ppServiceDetailDTOS[j].discountAmount =
                                this.ppService.ppServiceDetailDTOS[j].discountAmountOriginal / this.ppService.exchangeRate;
                        }
                        this.ppService.ppServiceDetailDTOS[j].discountRate =
                            100 *
                            this.ppService.ppServiceDetailDTOS[j].discountAmountOriginal /
                            this.ppService.ppServiceDetailDTOS[j].amountOriginal;
                        totalDisCountOriginal += this.ppService.ppServiceDetailDTOS[j].discountAmountOriginal;
                        totalDisCount += this.ppService.ppServiceDetailDTOS[j].discountAmount;
                    }
                }
            }
            // for (let i = 0; i < this.ppService.ppServiceDetailDTOS.length; i++) {
            //     this.onSelectMaterialGoods(this.ppService.ppServiceDetailDTOS[i], true);
            // }
            this.ppService.totalDiscountAmountOriginal = totalDisCountOriginal;
            this.ppService.totalDiscountAmount = totalDisCount;
        });
        this.registerCombobox();
        this.registerCopyRow();
        this.registerDeleteRow();
        this.registerAddNewRow();
        // this.registerIsShowPopup();
    }

    onRemovePPServiceDetails(dataHaving) {
        const listRemove = [];
        for (let i = 0; i < this.ppService.ppServiceDetailDTOS.length; i++) {
            if (
                this.ppService.ppServiceDetailDTOS[i].ppOrderNo &&
                !dataHaving.includes(this.ppService.ppServiceDetailDTOS[i].ppOrderDetailId)
            ) {
                listRemove.push(this.ppService.ppServiceDetailDTOS[i]);
            }
        }
        this.ppService.ppServiceDetailDTOS = this.ppService.ppServiceDetailDTOS.filter(x => !listRemove.includes(x));
    }

    registerAddNewRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.addNewRow(this.ppService.ppServiceDetailDTOS.findIndex(x => x === response.content));
        });
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            this.copyRow(null, this.contextMenu.selectedData);
        });
    }

    registerDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            const isVoucher = !this.contextMenu.isNew && !this.contextMenu.isCopy;
            this.keyPress(
                isVoucher
                    ? this.ppService.refVouchers.findIndex(x => x === this.contextMenu.selectedData)
                    : this.ppService.ppServiceDetailDTOS.findIndex(x => x === this.contextMenu.selectedData),
                0,
                isVoucher
            );
        });
    }

    registerCombobox() {
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
    }

    focusInputWhenAddNewRow(i: number) {
        setTimeout(function() {
            const element1: HTMLElement = document.getElementById('ppsd-detail-material-goods-1' + i);
            const element2: HTMLElement = document.getElementById('ppsd-detail-material-goods-2' + i);
            const element3: HTMLElement = document.getElementById('ppsd-detail-material-goods-3' + i);
            if (element1) {
                element1.focus();
            }
            if (element2) {
                element2.focus();
            }
            if (element3) {
                element3.focus();
            }
        }, 0);
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.ppService.ppServiceDetailDTOS;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.ppService;
    }

    addDataToDetail() {
        this.ppService.ppServiceDetailDTOS = this.details ? this.details : this.ppService.ppServiceDetailDTOS;
        this.ppService = this.parent ? this.parent : this.ppService;
    }

    onSubscribePPOrder(detail: PPOrderDetail, dto: IPPOrderDto, oldPPService?: MuaDichVuDetailResult): MuaDichVuDetailResult {
        const newPPService: MuaDichVuDetailResult = oldPPService ? oldPPService : new MuaDichVuDetailResult();
        newPPService.ppOrderNo = dto.orderNumber;
        newPPService.newPriority = dto.priority;
        newPPService.quantity = dto.receivedQuantity;
        newPPService.quantityReceipt = dto.quantityReceipt;
        newPPService.materialGoodsId = detail.materialGood ? detail.materialGood.id : null;

        if (!oldPPService) {
            const materialTemp = this.materialGoodss.find(n => n.id === newPPService.materialGoodsId);
            newPPService.materialGoodsCode = materialTemp.materialGoodsCode;
            if (this.ppService.accountingObjectID) {
                newPPService.postedObjectId = this.ppService.accountingObjectID;
                newPPService.postedObjectCode = this.ppService.accountingObjectCode;
                this.onSelectMaterialGoods(newPPService, !!oldPPService, true);
            }
            newPPService.materialGoodsName = materialTemp.materialGoodsName;
            newPPService.unitId = detail.unit ? detail.unit.id : null;
            newPPService.unitPrice = detail.unitPrice;
            newPPService.unitPriceOriginal = detail.unitPriceOriginal;
            newPPService.amount = detail.amount;
            newPPService.amountOriginal = detail.amountOriginal;
            newPPService.discountRate = detail.discountRate;
            newPPService.discountAmount = detail.discountAmount;
            newPPService.discountAmountOriginal = detail.discountAmountOriginal;
            newPPService.vatRate = detail.vatRate;
            newPPService.ppOrderId = dto.ppOrderId;
            newPPService.ppOrderDetailId = dto.id;
            if (this.isCareerGroup && this.goodsServicePurchase) {
                newPPService.goodsServicePurchaseId = this.goodsServicePurchase.id;
            }
        }
        return newPPService;
    }

    onChangeAccountPayment(isEdit?: boolean) {
        const accountPayment = this.bankAccountDetails.find(a => a.id === this.ppService.accountPaymentId);
        if (accountPayment) {
            this.ppService.accountPaymentName = this.bankAccountDetails.find(a => a.id === this.ppService.accountPaymentId).bankName;
        }
    }

    onChangeCreditCard() {
        const creditCardTerm = this.creditCards.find(x => x.creditCardNumber === this.ppService.creditCardNumber);
        this.ppService.creditCardId = creditCardTerm.id;
        this.ppService.creditCardType = creditCardTerm.creditCardType;
        this.ppService.ownerCreditCard = creditCardTerm.ownerCard;
    }

    previousEdit() {
        if (!this.isReadOnly) {
            return;
        }
        this.changeLocationItems(1);
    }

    nextEdit() {
        if (!this.isReadOnly) {
            return;
        }
        this.changeLocationItems(-1);
    }

    private changeLocationItems(action: number) {
        this.ppServiceService
            .findPPServiceByLocationItem({
                action,
                ppServiceId: this.ppService.id,
                ...this.ppServiceService.getMuaDichVuSearchSnapShot()
            })
            .subscribe(res => {
                this.setupPPService(res, true);
                this.location.go(`mua-dich-vu/${res.body.uuid}/edit/pp-service`);
            });
    }

    @ebAuth(['ROLE_ADMIN', ROLE.MuaDichVu_Xem])
    addNew($event = null) {
        event.preventDefault();
        if (this.isEdit || this.utilsService.isShowPopup) {
            return;
        }
        const ppService = new MuaDichVuResult();
        ppService.reason = this.translateService.instant('ebwebApp.muaHang.muaDichVu.title.name');
        ppService.otherReason = ppService.reason;
        this.isFillReasonByAccountingObject = false;
        this.isFillOtherReasonByAccountingObject = false;
        this.ppServiceService.setPPServiceSelected(ppService);
        this.ppServiceService.setReadOnly(false);
        this.isEdit = true;
        this.ngOnInit();
        this.location.go(`mua-dich-vu/new`);
    }

    exportPdf(isDownload: boolean, typeReports) {
        if (this.utilsService.isShowPopup) {
            return;
        }
        this.utilsService.getCustomerReportPDF(
            {
                id: this.ppService.id,
                typeID: TypeID.MUA_DICH_VU,
                typeReport: typeReports
            },
            isDownload
        );
        switch (typeReports) {
            case REPORT.ChungTuKeToan:
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.mBDeposit.financialPaper') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
                break;
            case REPORT.ChungTuKeToanQuyDoi:
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBDeposit.printing') +
                        this.translateService.instant('ebwebApp.mCPayment.home.title') +
                        '...',
                    this.translateService.instant('ebwebApp.mBDeposit.message')
                );
        }
    }

    copyAndNew() {
        event.preventDefault();
        if (this.utilsService.isShowPopup) {
            return;
        }
        this.ppService.id = null;
        this.isFillReasonByAccountingObject = false;
        this.isFillOtherReasonByAccountingObject = false;
        this.onChangeReason();
        this.onChangeOtherReason();
        if (!this.ppService.currentBook) {
            this.currentBook = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
            this.ppService.currentBook = this.currentBook;
        }
        this.usingManagerBook = this.account.systemOption.some(x => x.code === SD_SO_QUAN_TRI && x.data === '1');
        if (this.usingManagerBook) {
            // this.ppService.typeLedger = 0;
        } else {
            // this.ppService.typeLedger = parseInt(this.ppService.currentBook, 0);
        }
        this.ppService.paymentVoucherID = null;
        this.ppService.otherNoBook = null;
        for (let i = 0; i < this.ppService.ppServiceDetailDTOS.length; i++) {
            this.ppService.ppServiceDetailDTOS[i].id = null;
            this.ppService.ppServiceDetailDTOS[i].invoiceDate = null;
            this.ppService.ppServiceDetailDTOS[i].invoiceNo = null;
            this.ppService.ppServiceDetailDTOS[i].invoiceSeries = null;
            this.ppService.ppServiceDetailDTOS[i].invoiceTemplate = null;
            this.ppService.ppServiceDetailDTOS[i].ppOrderDetailId = null;
            this.ppService.ppServiceDetailDTOS[i].ppOrderId = null;
            this.ppService.ppServiceDetailDTOS[i].ppOrderNo = null;
        }
        this.ppService.refVouchers = [];
        this.ppServiceOld = Object.assign({}, this.ppService);
        this.onLoadReceipt(true, true);
        this.onLoadOtherCodeVoucher();
        this.location.go(`mua-dich-vu/new`);
        this.edit();
    }

    save(otherActionOnSuccess?: any, content?: any) {
        event.preventDefault();
        if (this.utilsService.isShowPopup) {
            return;
        }
        this.isReadOnly = true;
        this.isLoading = true;
        let isHavePPOrderId = false;
        if (this.validAllFields()) {
            this.vatValid = false;
            for (let i = 0; i < this.ppService.ppServiceDetailDTOS.length; i++) {
                this.ppService.ppServiceDetailDTOS[i].orderPriority = i + 1;
                if (this.ppService.ppServiceDetailDTOS[i].ppOrderDetailId) {
                    isHavePPOrderId = true;
                }
            }
            if (isHavePPOrderId && !this.isCheckPPOrderQuantity) {
                this.ppService.checkPPOrderQuantity = true;
                this.isCheckPPOrderQuantity = true;
            }
            this.ppServiceService.create(this.ppService).subscribe(
                res => {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    if (res.body.messages === UpdateDataMessages.DUPLICATE) {
                        let duplications = '';
                        for (let i = 0; i < res.body.errors.length; i++) {
                            if (i > 0) {
                                duplications += ', ';
                            }
                            duplications += this.translateService.instant(`ebwebApp.muaHang.muaDichVu.toastr.${res.body.errors[i]}`);
                        }
                        this.toastrService.error(
                            this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.duplications', {
                                duplications
                            })
                        );
                        this.isReadOnly = false;
                        this.isLoading = false;
                        return;
                    } else if (res.body.messages === UpdateDataMessages.SAVE_SUCCESS) {
                        this.ppService.id = res.body.uuid;
                        this.toastrService.success(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.saveSuccess'));
                        this.isLoading = false;
                        this.isEdit = false;
                        this.onCheckMessagesRecord(res.body.messageRecord);
                        if (otherActionOnSuccess) {
                            otherActionOnSuccess();
                        } else {
                            this.reloadPPService(this.ppService.id, true);
                        }
                    } else if (res.body.messages === UpdateDataMessages.UPDATE_SUCCESS) {
                        this.ppService.id = res.body.uuid;
                        this.toastrService.success(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.updateSuccess'));
                        this.isLoading = false;
                        this.isEdit = false;
                        this.onCheckMessagesRecord(res.body.messageRecord);
                        if (otherActionOnSuccess) {
                            otherActionOnSuccess();
                        } else {
                            this.reloadPPService(this.ppService.id, true);
                        }
                    } else if (res.body.messages === UpdateDataMessages.EXPENSIVE_ITEM_HAS_CHILD) {
                        this.toastrService.error(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.expensiveItemHasChild'));
                        this.isReadOnly = false;
                        this.isLoading = false;
                        return;
                    }
                },
                res => {
                    if (res.error.errorKey === UpdateDataMessages.QUANTITY_RECEIPT_GREATER_THAN && content) {
                        this.openModal(content);
                    }
                    this.isLoading = false;
                    this.isReadOnly = false;
                }
            );
        } else {
            this.isLoading = false;
            this.isReadOnly = false;
        }
    }

    onCheckMessagesRecord(message) {
        if (message === MSGERROR.XUAT_QUA_TON_QUY_QT) {
            this.toastrService.error(
                this.translateService.instant('global.messages.error.checkTonQuyQT'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
        } else if (message === MSGERROR.XUAT_QUA_TON_QUY_TC) {
            this.toastrService.error(
                this.translateService.instant('global.messages.error.checkTonQuyTC'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
        } else if (message === MSGERROR.XUAT_QUA_TON_QUY) {
            this.toastrService.error(
                this.translateService.instant('global.messages.error.checkTonQuy'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
        }
    }

    savePPService() {
        this.ppService.checkPPOrderQuantity = false;
        this.save(null, null);
    }

    checkVATValid() {
        this.vatValid = true;
        this.modalRef.close();
        this.save(this.otherActionOnSuccess, this.saveContentValid);
    }

    reloadPPService(id: string, unLoadReadonly?: boolean) {
        this.ppOrderModalService.cleanData();
        this.isCheckPPOrderQuantity = false;
        this.location.go(`mua-dich-vu/${id}/edit/pp-service`);
        this.ppServiceService
            .findPPServiceByLocationItem({
                ppServiceId: id,
                currentBook: parseInt(this.ppService.currentBook, 0),
                ...this.ppServiceService.getMuaDichVuSearchSnapShot()
            })
            .subscribe(res => this.setupPPService(res, unLoadReadonly));
    }

    validAllFields() {
        if (this.ppService.companyTaxCode) {
            if (!this.utilsService.checkMST(this.ppService.companyTaxCode)) {
                this.toastrService.error(
                    this.translateService.instant('ebwebApp.mCReceipt.error.taxCodeInvalid'),
                    this.translateService.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        if (!this.ppService.ppServiceDetailDTOS || this.ppService.ppServiceDetailDTOS.length === 0) {
            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.detailsInvalid'));
            return false;
        }
        if (this.checkCloseBook(this.account, this.ppService.postedDate)) {
            this.toastrService.error(
                this.translateService.instant('ebwebApp.lockBook.err.checkForSave'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }
        // số chứng từ
        if (!this.ppService.noBook) {
            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookInvalid'));
            return false;
        }

        if (this.ppService.receiptType !== PP_SERVICE_TYPE.PP_SERVICE_UNPAID) {
            if (!this.ppService.otherNoBook) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookInvalid'));
                return false;
            }
            if (this.ppService.noBook === this.ppService.otherNoBook) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.dupNoBookTab'));
                return false;
            }
        }

        // ngày chứng từ
        if (!this.ppService.receiptDate) {
            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.dateNoBookInvalid'));
            return false;
        }

        // ngày hạch toán
        if (!this.ppService.postedDate) {
            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.postedDateInvalid'));
            return false;
        }
        const receiptDateFormat = moment(this.ppService.receiptDate, 'DD/MM/YYYY').format('YYYYMMDD');
        const postedDateFormat = moment(this.ppService.postedDate, 'DD/MM/YYYY').format('YYYYMMDD');
        if (receiptDateFormat > postedDateFormat) {
            this.toastrService.error(this.translateService.instant('global.messages.validate.postedDateFormatLarger'));
            return false;
        }
        // loại tiền tệ
        if (!this.ppService.currencyID) {
            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.currencyInvalid'));
            return false;
        }

        // tỷ giá
        if (!this.ppService.exchangeRate) {
            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.exchangeRateInvalid'));
            return false;
        }
        //  phần chi tiết
        for (const detail of this.ppService.ppServiceDetailDTOS) {
            if (this.ppService.billReceived) {
                if (!detail.invoiceNo || detail.invoiceNo === '') {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.invoiceNoInvalid'));
                    return false;
                }
                if (!detail.invoiceDate || detail.invoiceDate === '') {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.invoiceDateInvalid'));
                    return false;
                }
            }
            const creditAccount = this.accountCredit.find(x => x.accountNumber === detail.creditAccount);
            const debitAccount = this.accountDebit.find(x => x.accountNumber === detail.debitAccount);

            if (creditAccount && creditAccount.detailType === ACCOUNT_DETAIL_TYPE.ACCOUNT_SUPPLIER && !this.ppService.accountingObjectID) {
                this.toastrService.error(
                    this.translateService.instant('global.messages.validate.objectInvalid0', {
                        account: detail.creditAccount
                    })
                );
                return false;
            }
            if (debitAccount && debitAccount.detailType === ACCOUNT_DETAIL_TYPE.ACCOUNT_SUPPLIER && !this.ppService.accountingObjectID) {
                this.toastrService.error(
                    this.translateService.instant('global.messages.validate.objectInvalid0', {
                        account: detail.debitAccount
                    })
                );
                return false;
            }

            if (creditAccount && creditAccount.detailType === ACCOUNT_DETAIL_TYPE.ACCOUNT_DEBIT) {
                if (
                    this.PP_SERVICE_TYPE.PP_SERVICE_PAYMENT_ORDER === this.ppService.receiptType ||
                    this.PP_SERVICE_TYPE.PP_SERVICE_TRANSFER_SEC === this.ppService.receiptType ||
                    this.PP_SERVICE_TYPE.PP_SERVICE_CASH_SEC === this.ppService.receiptType
                ) {
                    if (!this.ppService.accountPaymentId) {
                        this.toastrService.error(
                            this.translateService.instant('global.messages.validate.objectInvalid6', {
                                account: detail.creditAccount
                            })
                        );
                        return false;
                    }
                }
                if (this.PP_SERVICE_TYPE.PP_SERVICE_CREDIT_CARD === this.ppService.receiptType) {
                    if (!this.ppService.creditCardNumber) {
                        this.toastrService.error(
                            this.translateService.instant('global.messages.validate.objectInvalid6', {
                                account: detail.creditAccount
                            })
                        );
                        return false;
                    }
                }
            }
            if (debitAccount && debitAccount.detailType === ACCOUNT_DETAIL_TYPE.ACCOUNT_DEBIT) {
                switch (this.ppService.receiptType) {
                    case this.PP_SERVICE_TYPE.PP_SERVICE_PAYMENT_ORDER:
                    case this.PP_SERVICE_TYPE.PP_SERVICE_TRANSFER_SEC:
                    case this.PP_SERVICE_TYPE.PP_SERVICE_CASH_SEC:
                        if (!this.ppService.accountPaymentId) {
                            this.toastrService.error(
                                this.translateService.instant('ebwebApp.pPInvoice.error.objectInvalidAccountPayment', {
                                    account: detail.debitAccount
                                })
                            );
                            return false;
                        }
                        break;
                    case PP_SERVICE_TYPE.PP_SERVICE_CREDIT_CARD:
                        if (!this.ppService.creditCardNumber) {
                            this.toastrService.error(
                                this.translateService.instant('ebwebApp.pPInvoice.error.objectInvalidAccountPayment', {
                                    account: detail.debitAccount
                                })
                            );
                            return false;
                        }
                        break;
                }
                return false;
            }
            // TH Tiền thuế GTGT > 0 --> Bắt buộc nhập TK thuế GTGT và TKĐƯ thuế GTGT
            if (detail.vatRate === 1 || detail.vatRate === 2 || detail.isRequiredVATAccount) {
                if (!detail.vatAccount) {
                    this.toastrService.error(this.translateService.instant('global.messages.validate.vatAccountNull'));
                    return false;
                }
                if (!detail.deductionDebitAccount) {
                    this.toastrService.error(this.translateService.instant('global.messages.validate.deductionDebitAccountNull'));
                    return false;
                }
            }

            // mã hàng
            if (!detail.materialGoodsId) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.materialGoodsInvalid'));
                return false;
            }

            // kho
            if (detail.materialGoodsId) {
                const materialGoods = this.materialGoodss.find(x => x.id === detail.materialGoodsId);
                if (!(materialGoods.materialGoodsType === 2 || materialGoods.materialGoodsType === 4)) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.repositoryInvalid'));
                    return false;
                }
            }

            // tài khoản có
            if (!detail.creditAccount) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.creditAccountInvalid'));
                return false;
            }

            // tài khoản nợ
            if (!detail.debitAccount) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.debitAccountInvalid'));
                return false;
            }

            // tỷ lệ ck
            if (detail.discountRate && detail.discountRate > 100) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.discountRateMax'));
                return false;
            } else if (detail.discountRate && detail.discountRate < 0) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.discountRateMin'));
                return false;
            }
            if (!detail.vatRate && (detail.vatAmountOriginal || detail.vatAmount) && !this.vatValid) {
                this.detailVATCheck = detail;
                this.vatValidNUllCase = true;
                if (detail.vatRate === 0) {
                    this.vatValidNUllCase = false;
                }
                this.openModal(this.vatValidModal);
                return false;
            }
        }
        return true;
    }

    record() {
        event.preventDefault();
        if (this.ppService.recorded) {
            return;
        }
        const recordData = { id: this.ppService.id, typeID: parseInt(PP_SERVICE_TYPE.PP_SERVICE_UNPAID, 0) };
        this.isRecording = true;
        this.generalLedgerService.record(recordData).subscribe((res: HttpResponse<Irecord>) => {
            if (res.body.success) {
                this.selectedRow.recorded = true;
                this.ppService.recorded = true;
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBTellerPaper.recordToast.done'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                );
            } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                this.toastrService.error(
                    this.translateService.instant('global.messages.error.checkTonQuyQT'),
                    this.translateService.instant('ebwebApp.mCReceipt.error.error')
                );
            } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                this.toastrService.error(
                    this.translateService.instant('global.messages.error.checkTonQuyTC'),
                    this.translateService.instant('ebwebApp.mCReceipt.error.error')
                );
            } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                this.toastrService.error(
                    this.translateService.instant('global.messages.error.checkTonQuy'),
                    this.translateService.instant('ebwebApp.mCReceipt.error.error')
                );
            }
            this.isRecording = false;
        });
    }

    unRecord() {
        if (!this.ppService || !this.ppService.recorded || this.checkCloseBook(this.account, this.selectedRow.postedDate)) {
            return;
        }
        this.ppServiceService.checkHadReference({ ppServiceId: this.ppService.id }).subscribe(res => {
            if (res.body.messages === UpdateDataMessages.HAD_REFERENCE_AND_PAID) {
                this.openModal(this.checkHadReferencePaidModal);
            } else if (res.body.messages === UpdateDataMessages.HAD_REFERENCE) {
                this.openModal(this.unRecordModal);
            } else if (res.body.messages === UpdateDataMessages.HAD_PAID) {
                this.openModal(this.checkHadPaidModal);
            } else {
                this.unRecording();
            }
        });
    }

    unRecording() {
        const recordData = { id: this.ppService.id, typeID: parseInt(PP_SERVICE_TYPE.PP_SERVICE_UNPAID, 0) };
        this.isRecording = true;
        this.generalLedgerService.unrecord(recordData).subscribe((res: HttpResponse<Irecord>) => {
            if (res.body.success === true) {
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.done'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                );
                this.ppService.recorded = false;
            } else {
                this.toastrService.error(
                    this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.fail'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.error.error')
                );
            }
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.isRecording = false;
        });
    }

    @ebAuth(['ROLE_ADMIN', ROLE.MuaDichVu_Sua])
    edit() {
        event.preventDefault();
        if (this.utilsService.isShowPopup) {
            return;
        }
        if (this.ppService.id && (this.checkCloseBook(this.account, this.ppService.postedDate) || this.ppService.recorded)) {
            return;
        }
        this.isReadOnly = false;
        this.isEdit = true;
        this.ppServiceService.setReadOnly(false);
        this.itemUnSelected = [];
        this.focusFirstInput();
        this.onUpdateContextMenu();
        this.getAccountType();
    }

    onUpdateViewType(content: any) {
        if (this.utilsService.isShowPopup) {
            return;
        }
        if (!this.canDeactive()) {
            this.openModal(content);
        } else {
            this.close();
        }
    }

    openModal(content) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(content, { backdrop: 'static' });
    }

    canDeactive() {
        if (this.isReadOnly || !this.ppService || !this.ppServiceOld) {
            return true;
        } else {
            const ppServiceTemp = this.utilsService.deepCopyObject(this.ppService);
            const ppServiceOldTemp = this.utilsService.deepCopyObject(this.ppServiceOld);
            this.cleanPPService(ppServiceTemp);
            this.cleanPPService(ppServiceOldTemp);
            return (
                this.utilsService.isEquivalent(ppServiceTemp, ppServiceOldTemp) &&
                this.utilsService.isEquivalentArray(
                    ppServiceTemp.ppServiceDetailDTOS ? ppServiceTemp.ppServiceDetailDTOS : [],
                    ppServiceOldTemp.ppServiceDetailDTOS ? ppServiceOldTemp.ppServiceDetailDTOS : []
                ) &&
                this.utilsService.isEquivalentArray(
                    ppServiceTemp.refVouchers ? ppServiceTemp.refVouchers : [],
                    ppServiceOldTemp.refVouchers ? ppServiceOldTemp.refVouchers : []
                )
            );
        }
    }

    cleanPPService(ppService: MuaDichVuResult) {
        ppService.postedDate = null;
        ppService.receiptDate = null;
        ppService.receiptType = null;
        ppService.resultAmountOriginal = null;
        ppService.noBook = null;
        ppService.otherNoBook = null;
        ppService.totalAmount = null;
        ppService.resultAmount = null;

        if (ppService.ppServiceDetailDTOS && ppService.ppServiceDetailDTOS.length === 0) {
            ppService.ppServiceDetailDTOS = null;
        } else {
            ppService.ppServiceDetailDTOS.forEach(x => {
                x.units = null;
                x.unitPrices = null;
            });
        }

        if (ppService.refVouchers && ppService.refVouchers.length === 0) {
            ppService.refVouchers = null;
        }
    }

    removePPService() {
        if (this.utilsService.isShowPopup) {
            return;
        }
        if (this.checkCloseBook(this.account, this.selectedRow.postedDate)) {
            return;
        }
        if (this.ppService.id) {
            this.modalRef = this.refModalService.open(
                null,
                DialogDeleteComponent,
                {
                    id: this.ppService.id,
                    confirmDelete: id => {
                        this.ppServiceService.deletePPServiceById({ id }).subscribe(response => {
                            if (response.body.messages === UpdateDataMessages.DELETE_SUCCESS) {
                                this.eventManager.broadcast({
                                    name: ONBROADCASTEVENT.DELETE_PP_SERVICE_SUCCESS,
                                    content: id
                                });
                                this.eventManager.broadcast({
                                    name: ONBROADCASTEVENT.UPDATE_VIEW_TYPE_MUA_DICH_VU,
                                    content: 0
                                });
                            }
                            // else if (response.body.messages === UpdateDataMessages.HAD_REFERENCE) {
                            //     this.toastrService.error(
                            //         this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.hasReferenceSuccess')
                            //     );
                            // }
                        });
                    }
                },
                null,
                null,
                true
            );
        }
    }

    close() {
        this.ppService = null;
        this.ppServiceOld = null;
        if (this.modalRef) {
            this.modalRef.close();
        }
        const onClose = this.ppServiceService.getOnClose();
        if (onClose) {
            onClose();
        } else {
            this.eventManager.broadcast({
                name: ONBROADCASTEVENT.UPDATE_VIEW_TYPE_MUA_DICH_VU,
                content: 0
            });
            this.location.go('mua-dich-vu');
        }
    }

    saveAndNew() {
        if (this.utilsService.isShowPopup) {
            return;
        }
        this.save(() => {
            this.addNew();
        });
    }

    // Nhập lại Số lượng --> Tính lại Thành tiền (QĐ), Tiền CK (QĐ), Tiền thuế GTGT (QĐ)
    validReceivedQuantity(detailResult: MuaDichVuDetailResult) {
        this.thanhTien(detailResult);
        this.thanhTienQD(detailResult);
        this.tienChietKhau(detailResult);
        this.tienChietKhauQD(detailResult);
        this.tienThueGTGT(detailResult);
        this.tienThueGTGTQD(detailResult);
        this.changeAmountPPService();
    }

    onSelectUnpaidType() {
        this.tabSelected = this.PP_SERVICE_TYPE.PP_SERVICE_UNPAID;
        this.ppServiceTypePaid = null;
        this.getAccountType();
        this.isShowPaid = false;
    }

    onSelectPaidType() {
        if (this.ppService.id) {
            this.ppServiceService.checkHadReference({ ppServiceId: this.ppService.id }).subscribe(res => {
                if (res.body.messages === UpdateDataMessages.HAD_PAID) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.hasPaid'));
                    this.ppService.receiptType = this.PP_SERVICE_TYPE.PP_SERVICE_UNPAID;
                } else {
                    this.selectedPaidType();
                }
            });
        } else {
            this.selectedPaidType();
        }
        this.isShowPaid = true;
    }

    selectedPaidType() {
        this.ppServiceTypePaid = this.lastPaidType || this.PP_SERVICE_TYPE.PP_SERVICE_CASH;
        this.ppService.receiptType = this.ppServiceTypePaid;
        this.onChangePPServiceTypePaid();
    }

    changeVatRate(detail: MuaDichVuDetailResult) {
        if (detail.vatRate === 0) {
            detail.vatAmountOriginal = 0;
        }
        if (detail.vatRate === null || detail.vatRate === undefined) {
            detail.vatAmountOriginal = 0;
        }
        this.tienThueGTGT(detail);
        this.tienThueGTGTQD(detail);

        this.changeAmountPPService();
    }

    onChangeReason() {
        if (!this.isFillReasonByAccountingObject && this.ppService.reason !== this.getReason()) {
            this.isFillReasonByAccountingObject = true;
        }
    }

    onChangeOtherReason() {
        if (!this.isFillOtherReasonByAccountingObject) {
            if (this.ppService.receiptType === this.PP_SERVICE_TYPE.PP_SERVICE_CASH) {
                if (this.ppService.otherReason !== this.getCashTypeReason()) {
                    this.isFillOtherReasonByAccountingObject = true;
                }
            } else {
                if (this.ppService.otherReason !== this.getOtherReason()) {
                    this.isFillOtherReasonByAccountingObject = true;
                }
            }
        }
    }

    onChangeNumberAttach() {
        if (!this.isLoadNumberAttach) {
            this.ppService.otherNumberAttach = this.ppService.numberAttach;
            this.isLoadNumberAttach = true;
        }
    }

    changeAmountPPService() {
        this.roundObjects();
        this.ppService.totalquantity = 0;

        this.ppService.totalAmount = 0;
        this.ppService.totalAmountOriginal = 0;

        this.ppService.totalDiscountAmount = 0;
        this.ppService.totalDiscountAmountOriginal = 0;

        this.ppService.totalVATAmount = 0;
        this.ppService.totalVATAmountOriginal = 0;

        for (let index = 0; index < this.ppService.ppServiceDetailDTOS.length; index++) {
            this.ppService.totalquantity += this.ppService.ppServiceDetailDTOS[index].quantity || 0;

            if (this.ppService.currencyID === this.account.organizationUnit.currencyID) {
                this.ppService.ppServiceDetailDTOS[index].amount = this.ppService.ppServiceDetailDTOS[index].amountOriginal || 0;
                this.ppService.ppServiceDetailDTOS[index].discountAmount =
                    this.ppService.ppServiceDetailDTOS[index].discountAmountOriginal || 0;
                this.ppService.ppServiceDetailDTOS[index].vatAmount = this.ppService.ppServiceDetailDTOS[index].vatAmountOriginal || 0;
            }
            this.ppService.totalAmount += this.ppService.ppServiceDetailDTOS[index].amount || 0;
            this.ppService.totalAmountOriginal += this.ppService.ppServiceDetailDTOS[index].amountOriginal || 0;

            this.ppService.totalDiscountAmount += this.ppService.ppServiceDetailDTOS[index].discountAmount || 0;
            this.ppService.totalDiscountAmountOriginal += this.ppService.ppServiceDetailDTOS[index].discountAmountOriginal || 0;

            this.ppService.totalVATAmount += this.ppService.ppServiceDetailDTOS[index].vatAmount || 0;
            this.ppService.totalVATAmountOriginal += this.ppService.ppServiceDetailDTOS[index].vatAmountOriginal || 0;
        }
        this.ppService.resultAmount = this.ppService.totalAmount - this.ppService.totalDiscountAmount + this.ppService.totalVATAmount || 0;
        this.ppService.resultAmountOriginal =
            this.ppService.totalAmountOriginal - this.ppService.totalDiscountAmountOriginal + this.ppService.totalVATAmountOriginal || 0;
    }

    roundObjects() {
        this.utilsService.roundObject(this.ppService, this.account.systemOption, !this.isForeignCurrency);
        this.utilsService.roundObjects(this.ppService.ppServiceDetailDTOS, this.account.systemOption, !this.isForeignCurrency);
    }

    donGia(detail: MuaDichVuDetailResult) {
        detail.unitPriceOriginal = detail.unitPriceOriginal ? detail.unitPriceOriginal : 0;
    }

    // tính đơn giá quy đổi = đơn giá * tỷ giá
    donGiaQD(detail: MuaDichVuDetailResult) {
        detail.unitPrice = 0;
        this.donGia(detail);
        if (this.currencies.find(x => x.currencyCode === this.ppService.currencyID).formula === '/') {
            detail.unitPrice = detail.unitPriceOriginal / this.ppService.exchangeRate;
        } else {
            detail.unitPrice = detail.unitPriceOriginal * this.ppService.exchangeRate;
        }
    }

    // thành tiền = số lượng * đơn giá
    thanhTien(detail: MuaDichVuDetailResult) {
        // detail.amountOriginal = 0;
        detail.unitPriceOriginal = detail.unitPriceOriginal ? detail.unitPriceOriginal : 0;
        detail.quantity = detail.quantity ? detail.quantity : 0;
        // if (detail.amountOriginal > 0 && (detail.unitPriceOriginal === 0 || detail.quantity === 0)) {
        //     return;
        // }
        detail.amountOriginal = detail.quantity * detail.unitPriceOriginal;
    }

    // thành tiền quy đổi = thanh tiền * tỷ giá
    thanhTienQD(detail: MuaDichVuDetailResult) {
        detail.amount = 0;
        detail.amountOriginal = detail.amountOriginal ? detail.amountOriginal : 0;
        if (this.currencies.find(x => x.currencyCode === this.ppService.currencyID).formula === '/') {
            detail.amount = detail.amountOriginal / this.ppService.exchangeRate;
        } else {
            detail.amount = detail.amountOriginal * this.ppService.exchangeRate;
        }
    }

    // tiền chiết khấu = thành tiền * tỉ lệ chiết khấu / 1000
    tienChietKhau(detail: MuaDichVuDetailResult) {
        detail.discountAmountOriginal = 0;
        detail.amountOriginal = detail.amountOriginal ? detail.amountOriginal : 0;
        detail.discountAmountOriginal = detail.amountOriginal * detail.discountRate / 100;
    }

    // tiền chiết khấu quy đổi = tiền chiết khấu * tỷ giá
    tienChietKhauQD(detail: MuaDichVuDetailResult) {
        detail.discountAmount = 0;
        detail.discountAmountOriginal = detail.discountAmountOriginal ? detail.discountAmountOriginal : 0;
        if (this.currencies.find(x => x.currencyCode === this.ppService.currencyID).formula === '/') {
            detail.discountAmount = detail.discountAmountOriginal / this.ppService.exchangeRate;
        } else {
            detail.discountAmount = detail.discountAmountOriginal * this.ppService.exchangeRate;
        }
    }

    // tiền thuế giá trị gia tăng quy đổi = tiền thuế giá trị gia tăng nguyên tệ * % thuế giá trị gia tăng
    tienThueGTGT(detail: MuaDichVuDetailResult) {
        if ([0, 1, 2].includes(detail.vatRate)) {
            if (detail.vatRate !== 0) {
                detail.vatAmountOriginal = (detail.amountOriginal - detail.discountAmountOriginal) * (detail.vatRate === 1 ? 5 : 10) / 100;
            }
        } else {
            // detail.vatAmountOriginal = 0;
        }
        this.tienThueGTGTQD(detail);
    }

    // tiền thuế giá trị gia tăng = tiền thuế giá trị gia tăng quy đổi / tỷ giá
    tienThueGTGTQD(detail: MuaDichVuDetailResult) {
        if (this.currencies.find(x => x.currencyCode === this.ppService.currencyID).formula === '/') {
            detail.vatAmount = detail.vatAmountOriginal / this.ppService.exchangeRate;
        } else {
            detail.vatAmount = detail.vatAmountOriginal * this.ppService.exchangeRate;
        }
    }

    // Nhập lại Đơn giá --> Tính lại Thành tiền (QĐ), Tiền CK (QĐ), Tiền thuế GTGT (QĐ)
    selectUnitPriceOriginal(detail: MuaDichVuDetailResult) {
        if (this.currencies.find(x => x.currencyCode === this.ppService.currencyID).formula === '/') {
            detail.unitPrice = detail.unitPriceOriginal / this.ppService.exchangeRate;
        } else {
            detail.unitPrice = detail.unitPriceOriginal * this.ppService.exchangeRate;
        }
        this.donGiaQD(detail);
        this.thanhTien(detail);
        this.thanhTienQD(detail);
        this.tienChietKhau(detail);
        this.tienChietKhauQD(detail);
        this.tienThueGTGT(detail);
        this.tienThueGTGTQD(detail);

        this.changeAmountPPService();
    }

    changeUnitPrice(detail: MuaDichVuDetailResult) {
        this.donGiaQD(detail);
        this.thanhTien(detail);
        this.thanhTienQD(detail);
        this.tienChietKhau(detail);
        this.tienChietKhauQD(detail);
        this.tienThueGTGT(detail);
        this.tienThueGTGTQD(detail);
        this.changeAmountPPService();
    }

    changeAmount(detail: MuaDichVuDetailResult) {
        this.changeAmountPPService();
    }

    // Nhập lại Thành tiền --> Tính lại Đơn giá, Thành tiền QĐ, Tiền CK (QĐ), Tiền thuế GTGT (QĐ)
    changeAmountOriginal(detail: MuaDichVuDetailResult) {
        this.donGia(detail);
        this.donGiaQD(detail);
        // this.thanhTien(detail);
        this.thanhTienQD(detail);
        this.tienChietKhau(detail);
        this.tienChietKhauQD(detail);
        this.tienThueGTGT(detail);
        this.tienThueGTGTQD(detail);
        if (detail.amountOriginal && detail.quantity) {
            detail.unitPriceOriginal = detail.amountOriginal / detail.quantity;
        }
        this.changeAmountPPService();
    }

    // Nhập lại Tỷ lệ CK --> Tính lại Tiền CK (QĐ), Tiền thuế GTGT (QĐ)
    changeDiscountRate(detail: MuaDichVuDetailResult) {
        this.tienChietKhau(detail);
        this.tienChietKhauQD(detail);
        this.tienThueGTGT(detail);
        this.tienThueGTGTQD(detail);
        this.changeAmountPPService();
    }

    // Nhập lại Tiền CK --> Tính lại Tỷ lệ CK, Tiền thuế GTGT (QĐ)
    changeDiscountAmountOriginal(detail: MuaDichVuDetailResult) {
        detail.discountRate = detail.discountAmountOriginal / detail.amountOriginal * 100;
        this.tienChietKhauQD(detail);
        this.tienThueGTGT(detail);
        this.tienThueGTGTQD(detail);
        this.changeAmountPPService();
    }

    changeDiscountAmount(detail: any) {
        // this.tienThueGTGTQD(detail);
        // this.tienThueGTGT(detail);
        this.changeAmountPPService();
    }

    changeVatAmount(detail: MuaDichVuDetailResult) {
        this.changeAmountPPService();
    }

    changeVatAmountOriginal(detail: MuaDichVuDetailResult) {
        detail.isRequiredVATAccount = detail.vatAmountOriginal > 0;
        // this.tienThueGTGT(detail);
        this.tienThueGTGTQD(detail);
        this.changeAmountPPService();
    }

    changeVatAccount(detail: MuaDichVuDetailResult) {}

    changeDeductDebitAccount(detail: MuaDichVuDetailResult) {}

    changeGoodsServicePurchaseId(detail: MuaDichVuDetailResult) {}

    changeExpenseItem(detail: MuaDichVuDetailResult) {}

    changeCostSet(detail: MuaDichVuDetailResult) {}

    changeEMContract(detail: MuaDichVuDetailResult) {}

    changeBudgetItem(detail: MuaDichVuDetailResult) {}

    changeOrganizationUnit(detail: MuaDichVuDetailResult) {}

    changeStatisticCode(detail: MuaDichVuDetailResult) {}

    /*Phiếu chi*/
    previousEditMCPayment() {
        // goi service get by row num
        if (this.rowNum !== this.count) {
            this.utilsService
                .findByRowNum({
                    id: this.mCPaymentID,
                    isNext: false,
                    typeID: this.TYPE_MC_PAYMENT,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMCPayment>) => {
                        this.navigate(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    nextEditMCPayment() {
        // goi service get by row num
        if (this.rowNum !== 1) {
            this.utilsService
                .findByRowNum({
                    id: this.mCPaymentID,
                    isNext: true,
                    typeID: this.TYPE_MC_PAYMENT,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMCPayment>) => {
                        this.navigate(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    /*Thẻ tín dụng*/
    previousEditMBCreditCard() {
        // goi service get by row num
        if (this.rowNum !== this.count) {
            this.utilsService
                .findByRowNum({
                    id: this.mBCreditCardID,
                    isNext: false,
                    typeID: this.TYPE_MB_CREDIT_CARD,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMBCreditCard>) => {
                        this.navigateMBCreditCard(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    nextEditMBCreditCard() {
        // goi service get by row num
        if (this.rowNum !== 1) {
            this.utilsService
                .findByRowNum({
                    id: this.mBCreditCardID,
                    isNext: true,
                    typeID: this.TYPE_MB_CREDIT_CARD,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMBCreditCard>) => {
                        this.navigateMBCreditCard(res.body);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    /* Thẻ tín dụng */

    navigate(imcPayment: IMCPayment) {
        switch (imcPayment.typeID) {
            case this.TYPE_MC_PAYMENT:
                this.router.navigate(['/mc-payment', imcPayment.id, 'edit']);
                break;
            case this.TYPE_PPSERVICE:
                this.mCPaymentService.find(imcPayment.id).subscribe((res: HttpResponse<IMCPayment>) => {
                    const ppServiceID = res.body.ppServiceID;
                    if (ppServiceID) {
                        this.router.navigate(['./mua-dich-vu', ppServiceID, 'edit', 'from-mc-payment', imcPayment.id]).then(() => {
                            this.reloadPPService(ppServiceID);
                        });
                    }
                });
                break;
            case this.TYPE_PPINVOICE_MHQK:
                this.mCPaymentService.find(imcPayment.id).subscribe((res: HttpResponse<IMCPayment>) => {
                    const ppInvocieID = res.body.ppInvocieID;
                    if (ppInvocieID) {
                        if (res.body.storedInRepository) {
                            this.router.navigate(['./mua-hang', 'qua-kho', ppInvocieID, 'edit', 'from-mc-payment', imcPayment.id]);
                        } else {
                            this.router.navigate(['./mua-hang', 'khong-qua-kho', ppInvocieID, 'edit', 'from-mc-payment', imcPayment.id]);
                        }
                    }
                });
                break;
        }
    }

    navigateMBCreditCard(imbCreditCard: IMBCreditCard) {
        switch (imbCreditCard.typeID) {
            case this.TYPE_MB_CREDIT_CARD:
                this.router.navigate(['/mb-credit-card', imbCreditCard.id, 'edit']);
                break;
            case this.TYPE_MDV_MB_CREDIT_CARD:
                this.mBCreditCardService.find(imbCreditCard.id).subscribe((res: HttpResponse<IMBCreditCard>) => {
                    const ppServiceID = res.body.ppServiceID;
                    if (ppServiceID) {
                        this.router.navigate(['./mua-dich-vu', ppServiceID, 'edit', 'from-mb-credit-card', imbCreditCard.id]).then(() => {
                            this.reloadPPService(ppServiceID);
                        });
                    }
                });
                break;
            case this.TYPE_MHQK_MB_CREDIT_CARD:
                this.mBCreditCardService.find(imbCreditCard.id).subscribe((res: HttpResponse<any>) => {
                    const ppInvocieID = res.body.ppInvocieID;
                    if (ppInvocieID) {
                        if (res.body.storedInRepository) {
                            this.router.navigate(['./mua-hang', 'qua-kho', ppInvocieID, 'edit', 'from-mb-credit-card', imbCreditCard.id]);
                        } else {
                            this.router.navigate([
                                './mua-hang',
                                'khong-qua-kho',
                                ppInvocieID,
                                'edit',
                                'from-mb-credit-card',
                                imbCreditCard.id
                            ]);
                        }
                    }
                });
                break;
        }
    }

    closeAllFromMCPayment() {
        if (this.searchVoucher) {
            this.router.navigate(['/mc-payment', 'hasSearch', '1']);
        } else {
            this.router.navigate(['/mc-payment']);
        }
    }

    /*Phiếu chi*/
    /*Bao No*/

    /*Bao No*/
    previousEditMBTellerPaper() {
        if (this.rowNumberMBTellerPaper === this.countMBTellerPaper) {
            return;
        }
        this.rowNumberMBTellerPaper++;
        this.mBTellerPaperService
            .findByRowNum({
                searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher),
                rowNum: this.rowNumberMBTellerPaper
            })
            .subscribe(
                (res: HttpResponse<IMBTellerPaper>) => {
                    this.navigateMBTP(res.body);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    nextEditMBTellerPaper() {
        if (this.rowNumberMBTellerPaper === 1) {
            return;
        }
        this.rowNum--;
        this.mBTellerPaperService
            .findByRowNum({
                searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher),
                rowNum: this.rowNumberMBTellerPaper
            })
            .subscribe(
                (res: HttpResponse<IMBTellerPaper>) => {
                    this.navigateMBTP(res.body);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    navigateMBTP(imcPayment: IMBTellerPaper) {
        switch (imcPayment.typeId) {
            case this.TYPE_BAONO_UNC:
            case this.TYPE_BAONO_SCK:
            case this.TYPE_BAONO_STM:
                this.router.navigate(['/mb-teller-paper', imcPayment.id, 'edit']);
                break;
            case this.TYPE_UNC_PPSERVICE:
            case this.TYPE_SCK_PPSERVICE:
            case this.TYPE_STM_PPSERVICE:
                this.mBTellerPaperService.find(imcPayment.id).subscribe((res: HttpResponse<IMBTellerPaper>) => {
                    const ppServiceID = res.body.ppServiceID;
                    if (ppServiceID) {
                        this.router.navigate(['./mua-dich-vu']).then(() => {
                            this.router.navigate(['./mua-dich-vu', ppServiceID, 'edit', 'from-mb-teller-paper', imcPayment.id]);
                        });
                    }
                });
                break;
            case this.TYPE_UNC_PPINVOICE_MHQK:
            case this.TYPE_SCK_PPINVOICE_MHQK:
            case this.TYPE_STM_PPINVOICE_MHQK:
                this.mBTellerPaperService.find(imcPayment.id).subscribe((res: HttpResponse<IMBTellerPaper>) => {
                    const ppInvocieID = res.body.ppInvocieID;
                    if (ppInvocieID) {
                        if (res.body.storedInRepository) {
                            this.router.navigate(['./mua-hang', 'qua-kho', 'new']).then(() => {
                                this.router.navigate(['./mua-hang', 'qua-kho', ppInvocieID, 'edit', 'from-mb-teller-paper', imcPayment.id]);
                            });
                        } else {
                            this.router.navigate(['./mua-hang', 'khong-qua-kho', 'new']).then(() => {
                                this.router.navigate([
                                    './mua-hang',
                                    'khong-qua-kho',
                                    ppInvocieID,
                                    'edit',
                                    'from-mb-teller-paper',
                                    imcPayment.id
                                ]);
                            });
                        }
                    }
                });
                break;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.MuaDichVu_GhiSo])
    unrecord() {
        event.preventDefault();
        if (!this.ppService || !this.ppService.recorded) {
            return;
        }
        this.unRecord();
    }

    @ebAuth(['ROLE_ADMIN', ROLE.MuaDichVu_Xoa])
    delete() {
        event.preventDefault();
        if (!this.ppService || !this.ppService.recorded) {
            return;
        }
        this.removePPService();
    }

    baseExport() {
        // this.exportPdf(false);
    }

    closeForm() {
        event.preventDefault();
        this.onUpdateViewType(this.content);
    }

    ngAfterViewInit(): void {
        if (this.ppService) {
            this.focusFirstInput();
        }
    }

    addIfLastInput(i) {
        if (i === this.ppService.ppServiceDetailDTOS.length - 1) {
            this.addNewRow(0);
            // this.keyControlC
        }
    }

    onChangeReceiptDate() {
        this.ppService.postedDate = this.ppService.receiptDate;
    }

    onChangeAccountingObjectName() {
        if (!this.isFillReasonByAccountingObject) {
            this.ppService.reason = this.getReason(this.ppService.accountingObjectName);
        }
        if (!this.isFillOtherReasonByAccountingObject) {
            if (this.ppService.receiptType === this.PP_SERVICE_TYPE.PP_SERVICE_CASH) {
                this.ppService.otherReason = this.getCashTypeReason(this.ppService.accountingObjectName);
            } else {
                this.ppService.otherReason = this.getOtherReason(this.ppService.accountingObjectName);
            }
        }
    }

    copyRow(index, detail?) {
        // event.preventDefault();
        if (!detail) {
            detail = this.ppService.ppServiceDetailDTOS[index];
            if (this.getSelectionText()) {
                return;
            }
        } else {
            index = this.ppService.ppServiceDetailDTOS.findIndex(x => x === detail);
        }
        if (this.ppService.ppServiceDetailDTOS.length > index) {
            detail.id = null;
            this.ppService.ppServiceDetailDTOS.splice(index + 1, 0, this.utilsService.deepCopyObject(detail));
        } else {
            this.ppService.ppServiceDetailDTOS.push(this.utilsService.deepCopyObject(detail));
        }
    }

    updateBillReveived() {
        if (!this.ppService.billReceived) {
            this.ppService.ppServiceDetailDTOS.forEach(value => {
                value.invoiceTemplate = '';
                value.invoiceSeries = '';
                value.invoiceNo = '';
                value.invoiceDate = null;
            });
        }
    }

    changInvoiceInvoiceTemplate(detail: MuaDichVuDetailResult) {
        if (detail.invoiceTemplate) {
            detail.invoiceTemplate = detail.invoiceTemplate.toUpperCase();
        }
    }
}
