import { AfterViewChecked, AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { RepositoryService } from 'app/danhmuc/repository';
import { AccountListService } from 'app/danhmuc/account-list';
import { UnitService } from 'app/danhmuc/unit';
import { Moment } from 'moment';
import * as moment from 'moment';
import { Principal } from 'app/core';
import { MaterialGoodsConvertUnitService } from 'app/entities/material-goods-convert-unit';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { ExpenseItemService } from 'app/entities/expense-item';
import { CostSetService } from 'app/entities/cost-set';
import { EMContractService } from 'app/entities/em-contract';
import { BudgetItemService } from 'app/entities/budget-item';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { AccountType, GROUP_TYPEID, SO_LAM_VIEC, TypeID, KET_CHUYEN, MSGERROR } from 'app/app.constants';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { AccountingObjectBankAccountService } from 'app/entities/accounting-object-bank-account';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { DiscountReturnModalService } from 'app/core/login/discount-return-modal.service';
import { JhiEventManager } from 'ng-jhipster';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { PporderdetailService } from 'app/entities/pporderdetail';
import { IAccountAllList } from 'app/shared/model/account-all-list.model';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { DatePipe } from '@angular/common';
import { PPOrderModalService } from 'app/shared/modal/pp-order/pp-order-modal.service';
import { KetChuyenLaiLoService } from 'app/tonghop/ket_chuyen_lai_lo/ket-chuyen-lai-lo.service';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { IGOtherVoucherDetails } from 'app/shared/model/g-other-voucher-details.model';
import { DATE_FORMAT_SEARCH, DATE_FORMAT_SLASH } from 'app/shared';
import { REPORT } from 'app/app.constants';
import { ROLE } from 'app/role.constants';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { RSInwardOutwardService } from 'app/entities/rs-inward-outward/rs-inward-outward.service';

@Component({
    selector: 'eb-ket-chuyen-lai-lo-update',
    templateUrl: './ket-chuyen-lai-lo-update.component.html',
    styleUrls: ['ket-chuyen-lai-lo-detail.css']
})
export class KetChuyenLaiLoUpdateComponent extends BaseComponent implements OnInit, AfterViewChecked, AfterViewInit {
    @ViewChild('deleteItem') deleteItem: any;
    @ViewChild('content') content: any;
    @ViewChild('contentDataError') contentDataError: any;
    REPORT = REPORT;
    dateDp: any;
    postedDateDp: any;
    currencies: ICurrency[];
    currency: ICurrency;
    debitAccountList: any;
    creditAccountList: any;
    noFBook: string;
    date: Moment;
    postedDate: Moment;
    exchangeRate: number;
    account: any;
    amount: any;
    amountOriginal: any;
    checkData = false;
    isReadOnly = false;
    viewVouchersSelected: any[];
    modalRef: NgbModalRef;
    eventSubscriber: Subscription;
    contextMenu: ContextMenu;
    page: number;
    itemsPerPage: number;
    pageCount: number;
    totalItems: number;
    rowNum: number;
    predicate: any;
    reverse: any;
    dataSession: IDataSessionStorage;
    checkModalRef: NgbModalRef;
    itemUnSelected: any[];
    isClosing: boolean;
    isMove: boolean;
    isClosed: boolean;
    isMoreForm: boolean;
    isLoading = false;
    searchData: string;
    gOtherVoucher: IGOtherVoucher;
    gOtherVoucherCopy: any;
    gOtherVoucherDetailsCopy: any[];
    isEdit: boolean;
    ROLE = ROLE;

    // add by namnh GOtherVoucher
    isGOtherVoucher: boolean;
    searchVoucher: ISearchVoucher;
    count: number;
    TYPE_CHUNG_TU_NGHIEP_VU_KHAC = 700;
    TYPE_KET_CHUYEN_LAI_LO = 702;
    TYPE_PHAN_BO_CHI_PHI_TRA_TRUOC = 709;

    // end add by namnh

    constructor(
        private ketChuyenLaiLoService: KetChuyenLaiLoService,
        private accountingObjectService: AccountingObjectService,
        public utilsService: UtilsService,
        private currencyService: CurrencyService,
        private materialGoodsService: MaterialGoodsService,
        private repositoryService: RepositoryService,
        private accountListService: AccountListService,
        private accountingObjectBankAccountService: AccountingObjectBankAccountService,
        private unitService: UnitService,
        private toastrService: ToastrService,
        private translateService: TranslateService,
        private discountReturnModalService: DiscountReturnModalService,
        private materialGoodsConvertUnitService: MaterialGoodsConvertUnitService,
        private principal: Principal,
        private router: Router,
        public datepipe: DatePipe,
        private pporderdetailService: PporderdetailService,
        private eventManager: JhiEventManager,
        private refModalService: RefModalService,
        private modalService: NgbModal,
        private generalLedgerService: GeneralLedgerService,
        private bankAccountDetailService: BankAccountDetailsService,
        private emContractService: EMContractService,
        private costSetService: CostSetService,
        private statisticsCodeService: StatisticsCodeService,
        private organizationUnitService: OrganizationUnitService,
        private budgetItemService: BudgetItemService,
        private expenseItemService: ExpenseItemService,
        private rsInwardOutwardService: RSInwardOutwardService,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        private activatedRoute: ActivatedRoute,
        private ppOrderModalService: PPOrderModalService
    ) {
        super();
        this.contextMenu = new ContextMenu();
    }

    ngOnInit() {
        this.isGOtherVoucher = false;
        this.ppOrderModalService.cleanData();
        this.initValues();
        this.getAccount();
        this.registerRef();
    }

    getDataAccountSpecial() {
        this.ketChuyenLaiLoService
            .getDataAccountSpecial({ postDate: this.gOtherVoucher.postedDate.format(DATE_FORMAT_SEARCH) })
            .subscribe((res: HttpResponse<any[]>) => {
                if (res.body && res.body.length > 0) {
                    this.gOtherVoucher.gOtherVoucherDetails = [];
                    for (const item of res.body) {
                        if (item.amount > 0) {
                            this.gOtherVoucher.gOtherVoucherDetails.push(item);
                        }
                    }
                    this.getData();
                }
            });
    }

    getIndexFromGOtherVoucher() {
        // add by namnh
        if (window.location.href.includes('/from-g-other-voucher')) {
            this.searchVoucher = JSON.parse(sessionStorage.getItem('dataSearchGOtherVoucher'));
            this.isGOtherVoucher = true;
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
    }

    getData() {
        this.ketChuyenLaiLoService
            .getDataKc({ postDate: this.gOtherVoucher.postedDate.format(DATE_FORMAT_SEARCH) })
            .subscribe((res: HttpResponse<any[]>) => {
                if (res.body && res.body.length > 0) {
                    for (const item of res.body) {
                        if (
                            !item.debitAccount.startsWith(KET_CHUYEN.ACCOUNT_SPECIAL) &&
                            !item.creditAccount.startsWith(KET_CHUYEN.ACCOUNT_SPECIAL)
                        ) {
                            switch (item.fromAccountData) {
                                case KET_CHUYEN.NO:
                                    let accountNo = '';
                                    if (!item.debit) {
                                        accountNo = item.debitAccount;
                                    } else {
                                        accountNo = item.creditAccount;
                                    }
                                    const sumDebit = this.gOtherVoucher.gOtherVoucherDetails
                                        .filter(_item => _item.debitAccount === accountNo)
                                        .reduce((sum, __item) => sum + (__item.amount ? __item.amount : 0), 0);
                                    item.amount += sumDebit ? sumDebit : 0;
                                    break;
                                case KET_CHUYEN.CO:
                                    let accountCo = '';
                                    if (!item.debit) {
                                        accountCo = item.debitAccount;
                                    } else {
                                        accountCo = item.creditAccount;
                                    }
                                    const sumCreditCo = this.gOtherVoucher.gOtherVoucherDetails
                                        .filter(_item => _item.creditAccount === accountCo)
                                        .reduce((sum, __item) => sum + (__item.amount ? __item.amount : 0), 0);
                                    item.amount += sumCreditCo ? sumCreditCo : 0;
                                    break;
                                case KET_CHUYEN.NO_CO:
                                    let accountNoCo = '';
                                    if (!item.debit) {
                                        accountNoCo = item.debitAccount;
                                    } else {
                                        accountNoCo = item.creditAccount;
                                    }
                                    const sumDebitNoCo = this.gOtherVoucher.gOtherVoucherDetails
                                        .filter(_item => _item.debitAccount === accountNoCo)
                                        .reduce((sum, __item) => sum + (__item.amount ? __item.amount : 0), 0);

                                    const sumCreditNoCo = this.gOtherVoucher.gOtherVoucherDetails
                                        .filter(_item => _item.creditAccount === accountNoCo)
                                        .reduce((sum, __item) => sum + (__item.amount ? __item.amount : 0), 0);
                                    item.amount += (sumDebitNoCo ? sumDebitNoCo : 0) - (sumCreditNoCo ? sumCreditNoCo : 0);

                                    break;
                                case KET_CHUYEN.CO_NO:
                                    let accountCoNo = '';
                                    if (!item.debit) {
                                        accountCoNo = item.debitAccount;
                                    } else {
                                        accountCoNo = item.creditAccount;
                                    }
                                    const sumDebitCoNo = this.gOtherVoucher.gOtherVoucherDetails
                                        .filter(_item => _item.debitAccount === accountCoNo)
                                        .reduce((sum, __item) => sum + (__item.amount ? __item.amount : 0), 0);

                                    const sumCreditCoNo = this.gOtherVoucher.gOtherVoucherDetails
                                        .filter(_item => _item.creditAccount === accountCoNo)
                                        .reduce((sum, __item) => sum + (__item.amount ? __item.amount : 0), 0);
                                    item.amount += (sumCreditCoNo ? sumCreditCoNo : 0) - (sumDebitCoNo ? sumDebitCoNo : 0);
                                    break;
                            }
                            item.amount = this.utilsService.round(item.amount, this.account.systemOption, 7);
                        }
                        if (item.amount > 0) {
                            this.gOtherVoucher.gOtherVoucherDetails.push(item);
                        }
                    }
                    this.getDataDiff();
                }
            });
    }

    // cặp tài khoản lãi lỗ
    getDataDiff() {
        this.ketChuyenLaiLoService
            .getDataKcDiff({ postDate: this.gOtherVoucher.postedDate.format(DATE_FORMAT_SEARCH) })
            .subscribe((res: HttpResponse<any[]>) => {
                if (this.gOtherVoucher.gOtherVoucherDetails && this.gOtherVoucher.gOtherVoucherDetails.length > 0) {
                    let sumDebit = this.gOtherVoucher.gOtherVoucherDetails
                        .filter(item => item.debitAccount === KET_CHUYEN.ACCOUNT_911)
                        .reduce(
                            (sum, _item) => sum + (_item.amount ? this.utilsService.round(_item.amount, this.account.systemOption, 7) : 0),
                            0
                        );
                    sumDebit = this.utilsService.round(sumDebit, this.account.systemOption, 7);
                    let sumCredit = this.gOtherVoucher.gOtherVoucherDetails
                        .filter(item => item.creditAccount === KET_CHUYEN.ACCOUNT_911)
                        .reduce(
                            (sum, _item) => sum + (_item.amount ? this.utilsService.round(_item.amount, this.account.systemOption, 7) : 0),
                            0
                        );
                    sumCredit = this.utilsService.round(sumCredit, this.account.systemOption, 7);
                    this.utilsService.round(sumCredit, this.account.systemOption, 7);
                    let hieu = (sumDebit ? sumDebit : 0) - (sumCredit ? sumCredit : 0);
                    hieu = this.utilsService.round(hieu, this.account.systemOption, 7);
                    if (res.body && res.body.length > 0) {
                        hieu =
                            (hieu ? hieu : 0) +
                            (res.body[0].amount ? this.utilsService.round(res.body[0].amount, this.account.systemOption, 7) : 0);
                        hieu = this.utilsService.round(hieu, this.account.systemOption, 7);
                        // nếu nợ 911 lơn hơn có 911 thì lấy lỗ (fromAccountData = 0) nợ 911 nhỏ hơn có 911 thì lấy lãi (fromAccountData = 1)
                        if (hieu > 0) {
                            const item = res.body.find(a => a.fromAccountData !== 1);
                            item.amount = hieu;
                            item.amount = this.utilsService.round(item.amount, this.account.systemOption, 7);
                            this.gOtherVoucher.gOtherVoucherDetails.push(item);
                        } else if (hieu < 0) {
                            const item = res.body.find(a => a.fromAccountData === 1);
                            item.amount = hieu * -1;
                            item.amount = this.utilsService.round(item.amount, this.account.systemOption, 7);
                            this.gOtherVoucher.gOtherVoucherDetails.push(item);
                        }
                    }
                    for (const item of this.gOtherVoucher.gOtherVoucherDetails) {
                        if (this.currency.formula === '/') {
                            item.amountOriginal =
                                (item.amount ? item.amount : 0) * (this.gOtherVoucher.exchangeRate ? this.gOtherVoucher.exchangeRate : 1);
                        } else {
                            item.amountOriginal =
                                (item.amount ? item.amount : 0) / (this.gOtherVoucher.exchangeRate ? this.gOtherVoucher.exchangeRate : 1);
                        }
                        item.amountOriginal = this.utilsService.round(item.amountOriginal, this.account.systemOption, 7);
                    }
                }
                this.copyGOtherVoucher();
                this.getAllAccount();
                this.changeAmountGOtherVoucher();
            });
    }

    resetSession() {
        this.getAccount();
    }

    initValues() {
        this.getSessionData();
        this.isEdit = true;
        this.gOtherVoucher = { gOtherVoucherDetails: [] };
        this.isMoreForm = false;
        this.account = { organizationUnit: {} };
        this.currency = {};
        this.isLoading = false;
        this.currencies = [];
        this.currency = {};
        this.setDefaultDataFromSystemOptions();
    }

    getAccount() {
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.getSessionData();
                this.account = account;
                if (this.account) {
                    if (data.gOtherVoucher && data.gOtherVoucher.id) {
                        this.gOtherVoucher = data.gOtherVoucher;
                        this.loadDataGOtherVoucher();
                        this.getIndexFromGOtherVoucher();
                    } else {
                        this.gOtherVoucher = {
                            gOtherVoucherDetails: [],
                            typeID: TypeID.KET_CHUYEN_LAI_LO,
                            typeGroup: GROUP_TYPEID.GROUP_GOTHERVOUCHER,
                            refVouchers: []
                        };
                        this.gOtherVoucher.currentBook = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
                        this.isReadOnly = false;
                        this.isEdit = !this.isReadOnly;
                        this.checkData = false;
                        this.getSoChungTu(GROUP_TYPEID.GROUP_GOTHERVOUCHER);
                        this.onLoadReceipt();
                    }

                    if (this.account.organizationUnit) {
                        this.setDefaultDataFromSystemOptions();
                    }
                }
            });
        });
    }

    loadDataGOtherVoucher() {
        this.isReadOnly = true;
        this.isEdit = !this.isReadOnly;
        this.checkData = true;
        this.gOtherVoucher.currentBook = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
        if (this.gOtherVoucher.currentBook === '1') {
            this.gOtherVoucher.noFBook = this.gOtherVoucher.noMBook;
        }
        this.getAllAccount();
        this.copyGOtherVoucher();
        // todo check lại ctrl e để sửa
        // if (this.dataSession && this.dataSession.isEdit) {
        //     this.edit();
        // }
    }

    getAllAccount() {
        this.getAccountType();
    }

    onLoadReceipt() {
        if (this.account) {
            this.gOtherVoucher.date = this.utilsService.ngayHachToan(this.account);
            this.gOtherVoucher.postedDate = this.gOtherVoucher.date;
            this.changeReason();
            this.getDataAccountSpecial();
            if (!this.checkData) {
                this.copyGOtherVoucher();
            }
        }
    }

    changeReason() {
        this.translateService
            .get(['ebwebApp.gOtherVoucher.reasonKc'], {
                date: this.gOtherVoucher.postedDate ? this.gOtherVoucher.postedDate.format(DATE_FORMAT_SLASH) : ''
            })
            .subscribe((res: any) => {
                this.gOtherVoucher.reason = res['ebwebApp.gOtherVoucher.reasonKc'];
            });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('ketChuyenDataSession'));
        if (!this.dataSession) {
            this.dataSession = JSON.parse(sessionStorage.getItem('ketChuyenSearchData'));
        }

        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.searchData = this.dataSession.searchVoucher;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
    }

    setDefaultDataFromSystemOptions() {
        if (this.account) {
            if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                if (!this.checkData) {
                    this.gOtherVoucher.currencyID = this.account.organizationUnit.currencyID;
                    this.copyGOtherVoucher();
                }
                this.getActiveCurrencies();
            }
        }
    }

    // tiền tệ
    getActiveCurrencies() {
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencies = res.body;
            if (this.gOtherVoucher && this.gOtherVoucher.currencyID) {
                this.currency = this.currencies.find(item => item.currencyCode === this.gOtherVoucher.currencyID);
            } else if (this.gOtherVoucher && !this.gOtherVoucher.currencyID) {
                this.currency = this.currencies[0];
            }
            if (!this.checkData) {
                this.copyGOtherVoucher();
            }
            this.selectCurrency();
        });
    }

    // lấy danh sách các loại tài khoản
    getAccountType() {
        const columnList = [{ column: AccountType.TK_CO, ppType: false }, { column: AccountType.TK_NO, ppType: false }];
        const param = {
            typeID: this.gOtherVoucher.typeID,
            columnName: columnList
        };
        this.accountListService.getAccountTypeFour(param).subscribe(res => {
            const dataAccount: IAccountAllList = res.body;
            this.creditAccountList = dataAccount.creditAccount;
            this.debitAccountList = dataAccount.debitAccount;
            this.gOtherVoucher.gOtherVoucherDetails.forEach(item => {
                item.creditAccountItem = this.creditAccountList.find(n => n.accountNumber === item.creditAccount);
                item.debitAccountItem = this.debitAccountList.find(n => n.accountNumber === item.debitAccount);
            });
        });
    }

    getSoChungTu(groupId) {
        this.utilsService
            .getGenCodeVoucher({
                typeGroupID: groupId,
                displayOnBook: this.gOtherVoucher.currentBook
            })
            .subscribe((res: HttpResponse<string>) => {
                this.gOtherVoucher.noFBook = res.body;
                if (!this.checkData) {
                    this.copyGOtherVoucher();
                }
            });
    }

    private onError(errorMessage: string) {}

    selectCurrency() {
        if (this.currency) {
            this.gOtherVoucher.currencyID = this.currency.currencyCode;
            this.gOtherVoucher.exchangeRate = 1;
        } else {
            this.currency = {};
            this.gOtherVoucher.currencyID = null;
            this.gOtherVoucher.exchangeRate = 1;
        }
        if (!this.checkData) {
            this.copyGOtherVoucher();
        }
        if (!this.isReadOnly) {
            this.changeExchangeRate();
        }
    }

    changeExchangeRate() {
        if (this.gOtherVoucher.gOtherVoucherDetails && this.gOtherVoucher.gOtherVoucherDetails.length) {
            // this.gOtherVoucher.gOtherVoucherDetails.forEach(detail => {
            //
            // });
            this.changeAmountGOtherVoucher();
        }
    }

    selectDebitAccountItem(detail: IGOtherVoucherDetails) {
        if (detail.debitAccountItem) {
            detail.debitAccount = detail.debitAccountItem.accountNumber;
        } else {
            detail.debitAccount = null;
        }
    }

    selectCreditAccountItem(detail: IGOtherVoucherDetails) {
        if (detail.creditAccountItem) {
            detail.creditAccount = detail.creditAccountItem.accountNumber;
        } else {
            detail.creditAccount = null;
        }
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    addNewRow(number: number) {
        if (this.isReadOnly) {
            return;
        }
        this.gOtherVoucher.gOtherVoucherDetails.push({
            amount: 0,
            amountOriginal: 0
        });
    }

    onRightClick($event, data, selectedData, isNew, isDelete) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.selectedData = selectedData;
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    keyPress(value: number, select: number) {
        if (select === 0) {
            this.gOtherVoucher.gOtherVoucherDetails.splice(value, 1);
        } else if (select === 1) {
            this.gOtherVoucher.gOtherVoucherDetails.splice(value, 1);
        }
        this.changeAmountGOtherVoucher();
    }

    validate() {
        if (this.checkCloseBook(this.account, this.gOtherVoucher.postedDate)) {
            this.toastrService.error(
                this.translateService.instant('ebwebApp.lockBook.err.checkForSave'),
                this.translateService.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        }

        if (!this.checkPostedDateGreaterDate()) {
            return false;
        }
        // số chứng từ
        if (!this.gOtherVoucher.noFBook) {
            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookInvalid'));
            return false;
        }

        if (!this.gOtherVoucher.gOtherVoucherDetails || this.gOtherVoucher.gOtherVoucherDetails.length === 0) {
            this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.detailsInvalid'));
            return false;
        }

        //  phần chi tiết
        for (const detail of this.gOtherVoucher.gOtherVoucherDetails) {
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
        }
        return true;
    }

    checkPostedDateGreaterDate() {
        if (this.gOtherVoucher.postedDate && this.gOtherVoucher.date) {
            if (moment(this.gOtherVoucher.postedDate) < moment(this.gOtherVoucher.date)) {
                this.toastrService.error(this.translateService.instant('ebwebApp.common.error.dateAndPostDate'));
                return false;
            }
        }
        return true;
    }

    public beforeChange($event: NgbTabChangeEvent) {
        switch ($event.nextId) {
            case 'ppinvoice-tab-pp-reference':
                $event.preventDefault();
                if (!this.isReadOnly) {
                    this.modalRef = this.refModalService.open(this.gOtherVoucher.refVouchers);
                }
                break;
        }
    }

    registerRef() {
        this.registerLockSuccess();
        this.registerUnlockSuccess();
        this.eventSubscriber = this.eventManager.subscribe('selectViewVoucher', response => {
            this.gOtherVoucher.refVouchers = response.content;
        });

        this.eventManager.subscribe('afterDeleteRow', response => {
            if (response.content.ppOrderDetailId && response.content.quantityFromDB) {
                this.itemUnSelected.push(response.content);
            }
            this.changeAmountGOtherVoucher();
        });
    }
    registerLockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('lockSuccess', response => {
            this.isReadOnly = true;
            this.refreshData();
        });
    }

    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.isReadOnly = true;
            this.refreshData();
        });
    }

    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenLaiLo_GhiSo])
    record() {
        event.preventDefault();
        if (this.gOtherVoucher && this.gOtherVoucher.id && !this.gOtherVoucher.recorded && !this.isLoading && this.isReadOnly) {
            this.recordGO();
        }
    }

    recordGO() {
        const recordData = { id: this.gOtherVoucher.id, typeID: TypeID.KET_CHUYEN_LAI_LO };
        this.isLoading = true;
        this.generalLedgerService.record(recordData).subscribe((res: HttpResponse<Irecord>) => {
            if (res.body.success) {
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBTellerPaper.recordToast.done'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                );
                this.gOtherVoucher.recorded = true;
            } else {
                if (res.body.msg === MSGERROR.KC_DU_LIEU_AM) {
                    this.toastrService.error(
                        this.translateService.instant('global.messages.error.kcDataError'),
                        this.translateService.instant('ebwebApp.mCReceipt.error.error')
                    );
                } else {
                    this.toastrService.error(
                        this.translateService.instant('ebwebApp.mBTellerPaper.recordToast.failed'),
                        this.translateService.instant('ebwebApp.mBTellerPaper.error.error')
                    );
                }
            }
            this.isLoading = false;
        });
    }
    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenLaiLo_GhiSo])
    unrecord() {
        event.preventDefault();
        if (
            this.gOtherVoucher &&
            this.gOtherVoucher.id &&
            this.gOtherVoucher.recorded &&
            !this.isLoading &&
            !this.checkCloseBook(this.account, this.gOtherVoucher.postedDate)
        ) {
            this.unRecordGO();
        }
    }

    unRecordGO() {
        this.isLoading = true;
        const recordData = {
            id: this.gOtherVoucher.id,
            typeID: TypeID.KET_CHUYEN_LAI_LO
        };
        this.generalLedgerService.unrecord(recordData).subscribe((res: HttpResponse<Irecord>) => {
            if (res.body.success) {
                this.toastrService.success(
                    this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.done'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.message.title')
                );
                this.gOtherVoucher.recorded = false;
            } else {
                this.toastrService.error(
                    this.translateService.instant('ebwebApp.mBTellerPaper.unrecordToast.failed'),
                    this.translateService.instant('ebwebApp.mBTellerPaper.error.error')
                );
            }
            this.isLoading = false;
        });
    }
    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenLaiLo_Xoa])
    delete() {
        event.preventDefault();
        if (!this.isReadOnly || ((!(this.gOtherVoucher && this.gOtherVoucher.id) || this.gOtherVoucher.recorded) && !this.isLoading)) {
            this.openModelDelete(this.deleteItem);
        }
    }

    deleteGO() {
        if (this.gOtherVoucher && this.gOtherVoucher.id) {
            this.isLoading = true;
            this.ketChuyenLaiLoService.deleteById({ id: this.gOtherVoucher.id }).subscribe(
                (res: HttpResponse<any>) => {
                    if (res.body.message === UpdateDataMessages.DELETE_SUCCESS) {
                        this.toastrService.success(this.translateService.instant('ebwebApp.pPInvoice.success.deleteSuccess'));
                        this.checkModalRef.close();
                        this.isClosing = true;
                        this.router.navigate(
                            ['/ket-chuyen-lai-lo'],
                            this.dataSession
                                ? {
                                      queryParams: {
                                          page: this.page,
                                          size: this.itemsPerPage,
                                          sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                                      }
                                  }
                                : null
                        );
                    } else if (res.body.message === UpdateDataMessages.NOT_FOUND) {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.notFound'));
                    } else {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.deleteError'));
                    }
                    this.isLoading = false;
                },
                (res: HttpErrorResponse) => {
                    this.isLoading = false;
                }
            );
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IGOtherVoucher>>) {
        result.subscribe((res: HttpResponse<IGOtherVoucher>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.toastrService.success(this.translateService.instant('ebwebApp.pPInvoice.success.addSuccess'));
        this.isReadOnly = true;
        this.isEdit = !this.isReadOnly;
        this.isLoading = false;
    }

    private onSaveError() {
        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.addError'));
        this.isLoading = false;
    }
    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenLaiLo_Sua])
    edit() {
        event.preventDefault();
        if (
            !this.gOtherVoucher.recorded &&
            this.isReadOnly &&
            !this.isLoading &&
            !this.checkCloseBook(this.account, this.gOtherVoucher.postedDate)
        ) {
            this.isMove = false;
            this.isReadOnly = false;
            this.isEdit = !this.isReadOnly;
            this.isLoading = false;
        }
    }
    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenLaiLo_Them])
    copyAndNew() {
        event.preventDefault();
        if (this.isReadOnly && !this.isLoading) {
            this.copyAndNewGO();
        }
    }

    copyAndNewGO() {
        this.gOtherVoucher.id = null;
        this.gOtherVoucher.recorded = false;
        this.gOtherVoucher.gOtherVoucherDetails.forEach(item => (item.id = null));
        this.isReadOnly = false;
        this.isEdit = !this.isReadOnly;
        this.checkData = false;
        this.onLoadReceipt();
        this.getSoChungTu(GROUP_TYPEID.GROUP_GOTHERVOUCHER);

        this.copyGOtherVoucher();
        this.toastrService.success(this.translateService.instant('ebwebApp.pPInvoice.success.copy'));
    }
    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenLaiLo_Them])
    addNew($event = null) {
        $event.preventDefault();
        if (this.isReadOnly && !this.isLoading) {
            this.addNewGO();
        }
    }

    addNewGO() {
        this.router.navigate(['/ket-chuyen-lai-lo/new']);
        this.resetSession();
    }

    move(direction: number) {
        if ((direction === -1 && this.rowNum === 1) || (direction === 1 && this.rowNum === this.totalItems)) {
            return;
        }
        this.rowNum += direction;
        const searchData = JSON.parse(this.searchData);
        // goi service get by row num
        return this.ketChuyenLaiLoService
            .findByRowNum({
                fromDate: searchData.fromDate || '',
                toDate: searchData.toDate || '',
                status: searchData.status === 0 || searchData.status === 1 ? searchData.status : '',
                searchValue: searchData.searchValue || '',
                rowNum: this.rowNum
            })
            .subscribe(
                (res: HttpResponse<IGOtherVoucher>) => {
                    this.gOtherVoucher = res.body;
                    this.dataSession.rowNum = this.rowNum;
                    sessionStorage.setItem('ketChuyenDataSession', JSON.stringify(this.dataSession));
                    if (this.gOtherVoucher && this.gOtherVoucher.id) {
                        this.loadDataGOtherVoucher();
                        this.getAllAccount();
                        if (this.account.organizationUnit) {
                            this.setDefaultDataFromSystemOptions();
                        }
                        this.isMove = true;
                    }
                },
                (res: HttpErrorResponse) => {
                    this.handleError(res);
                    this.getSessionData();
                }
            );
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    openModelDelete(content) {
        this.checkModalRef = this.modalService.open(content, { size: 'lg', backdrop: 'static' });
    }

    save(check?: Boolean) {
        event.preventDefault();
        if (!this.isReadOnly && !this.isLoading) {
            this.checkBeforeSave(false);
        }
    }
    @ebAuth(['ROLE_ADMIN', ROLE.KetChuyenLaiLo_Them])
    saveAndNew() {
        event.preventDefault();
        if (!this.isReadOnly && !this.isLoading) {
            this.checkBeforeSave(true);
        }
    }

    saveGO(isNew = false) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isReadOnly = true;
        this.isEdit = !this.isReadOnly;
        if (!this.validate()) {
            this.isReadOnly = false;
            this.isEdit = !this.isReadOnly;
            return;
        }
        this.isLoading = true;
        let orderPriority = 0;
        for (const item of this.gOtherVoucher.gOtherVoucherDetails) {
            item.orderPriority = orderPriority;
            orderPriority++;
        }
        this.roundObject();
        this.ketChuyenLaiLoService.createGOtherVoucher(this.gOtherVoucher).subscribe(
            res => {
                if (res.body.messages === UpdateDataMessages.SAVE_SUCCESS) {
                    this.gOtherVoucher.id = res.body.uuid;
                    this.toastrService.success(this.translateService.instant('ebwebApp.pPInvoice.success.addSuccess'));
                    if (isNew) {
                        this.addNewGO();
                    } else {
                        this.isReadOnly = true;
                        this.refreshData();
                    }
                } else if (res.body.messages === UpdateDataMessages.UPDATE_SUCCESS) {
                    this.gOtherVoucher.id = res.body.uuid;
                    this.toastrService.success(this.translateService.instant('ebwebApp.pPInvoice.success.updateSuccess'));
                    if (isNew) {
                        this.addNewGO();
                    } else {
                        this.isReadOnly = true;
                        this.refreshData();
                    }
                } else if (res.body.messages === UpdateDataMessages.DUPLICATE_NO_BOOK) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.duplicateNoBook'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.DUPLICATE_NO_BOOK_RS) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.duplicateNoBookRs'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.CANNOT_UPDATE_NO_BOOK_RS) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.cancelNotUpdateBookRs'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.RSI_ERROR) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.notFoundRsInward'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.DUPLICATE_OTHER_NO_BOOK) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.duplicateNoBook'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.CANNOT_UPDATE_OTHER_NO_BOOK) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.cancelNotUpdateBook'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.CANNOT_UPDATE_NO_BOOK) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.cancelNotUpdateBook'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.CURRENT_USER_IS_NOT_PRESENT) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.accountNotAuthority'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.NO_BOOK_NULL) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookInvalid'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.NO_BOOK_RSI_NULL) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookRSIInvalid'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.NO_BOOK_OTHER_NULL) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.noBookOtherInvalid'));
                    this.isReadOnly = false;
                } else if (res.body.messages === UpdateDataMessages.POSTDATE_INVALID) {
                    this.toastrService.error(
                        this.translateService.instant('ebwebApp.pPInvoice.error.postdateInvalid', {
                            date: res.body.result ? res.body.result : ''
                        })
                    );
                    this.isReadOnly = false;
                } else {
                    this.isReadOnly = false;
                    if (this.gOtherVoucher.id) {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.updateError'));
                    } else {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.addError'));
                    }
                }
                this.isEdit = !this.isReadOnly;
                this.isLoading = false;
            },
            (res: HttpErrorResponse) => {
                if (res.error.errorKey === UpdateDataMessages.DUPLICATE_NO_BOOK) {
                    this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.' + res.error.entityName));
                } else {
                    if (this.gOtherVoucher.id) {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.updateError'));
                    } else {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPInvoice.error.addError'));
                    }
                }
                this.isReadOnly = false;
                this.isEdit = !this.isReadOnly;
                this.isLoading = false;
            }
        );
    }

    private refreshData() {
        this.ketChuyenLaiLoService
            .findById({
                id: this.gOtherVoucher.id
            })
            .subscribe(
                (resPP: HttpResponse<IGOtherVoucher>) => {
                    this.gOtherVoucher = {};
                    this.gOtherVoucher = resPP.body;
                    if (this.gOtherVoucher && this.gOtherVoucher.id) {
                        this.loadDataGOtherVoucher();
                        this.getAllAccount();
                        if (this.account.organizationUnit) {
                            this.setDefaultDataFromSystemOptions();
                        }
                    }
                },
                (resPP: HttpErrorResponse) => {
                    this.getSessionData();
                }
            );
    }

    // tính lại các loại tổng tiền
    changeAmountGOtherVoucher() {
        this.gOtherVoucher.totalAmount = 0;
        this.gOtherVoucher.totalAmountOriginal = 0;
        for (const detail of this.gOtherVoucher.gOtherVoucherDetails) {
            detail.amount = detail.amount ? detail.amount : 0;
            detail.amountOriginal = detail.amountOriginal ? detail.amountOriginal : 0;

            this.gOtherVoucher.totalAmount += detail.amount;
            this.gOtherVoucher.totalAmountOriginal += detail.amountOriginal;
            this.gOtherVoucher.totalAmount = this.utilsService.round(this.gOtherVoucher.totalAmount, this.account.systemOption, 7);
            this.gOtherVoucher.totalAmountOriginal = this.utilsService.round(
                this.gOtherVoucher.totalAmountOriginal,
                this.account.systemOption,
                7
            );
        }
    }

    copyGOtherVoucher() {
        this.gOtherVoucherCopy = Object.assign({}, this.gOtherVoucher);
        this.gOtherVoucherDetailsCopy = this.gOtherVoucher.gOtherVoucherDetails.map(object => ({ ...object }));
    }

    closeForm() {
        event.preventDefault();
        if (!this.isLoading) {
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.previousState(this.content);
        }
    }

    previousState(content) {
        this.isClosing = true;

        if (!this.gOtherVoucher || this.isMove || this.isReadOnly) {
            this.closeAll();
            return;
        }
        if (this.checkChangeGOtherVoucher()) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
            return;
        }
        this.closeAll();
    }

    private checkChangeGOtherVoucher() {
        const tempGOtherVoucher = this.formatGOtherVoucherTemp();
        const tempGOtherVoucherCopy = this.formatGOtherVoucherCopyTemp();

        if (
            this.datepipe.transform(tempGOtherVoucher.date, 'yyyyMMdd') !== this.datepipe.transform(tempGOtherVoucherCopy.date, 'yyyyMMdd')
        ) {
            return true;
        }

        if (
            this.datepipe.transform(tempGOtherVoucher.postedDate, 'yyyyMMdd') !==
            this.datepipe.transform(tempGOtherVoucherCopy.postedDate, 'yyyyMMdd')
        ) {
            return true;
        }

        const tempGOtherVoucherDetails = this.formatGOtherVoucherDetailsTemp() ? this.formatGOtherVoucherDetailsTemp() : [];
        const tempGOtherVoucherDetailsCopy = this.formatGOtherVoucherDetailsCopyTemp() ? this.formatGOtherVoucherDetailsCopyTemp() : [];

        if (tempGOtherVoucherDetails.length !== tempGOtherVoucherDetailsCopy.length) {
            return true;
        }

        tempGOtherVoucher.date = null;
        tempGOtherVoucher.postedDate = null;

        tempGOtherVoucherCopy.date = null;
        tempGOtherVoucherCopy.postedDate = null;

        return (
            !this.utilsService.isEquivalent(tempGOtherVoucher, tempGOtherVoucherCopy) ||
            !this.utilsService.isEquivalentArray(tempGOtherVoucherDetails, tempGOtherVoucherDetailsCopy)
        );
    }

    formatGOtherVoucherDetailsTemp() {
        const tempGOtherVoucherDetails = this.gOtherVoucher.gOtherVoucherDetails.map(object => ({ ...object }));
        for (const detail of tempGOtherVoucherDetails) {
            detail.debitAccountItem = null;
            detail.creditAccountItem = null;
        }
        return tempGOtherVoucherDetails;
    }

    formatGOtherVoucherDetailsCopyTemp() {
        const tempGOtherVoucherDetailsCopy = this.gOtherVoucherDetailsCopy.map(object => ({ ...object }));
        for (const detail of tempGOtherVoucherDetailsCopy) {
            detail.debitAccountItem = null;
            detail.creditAccountItem = null;
        }
        return tempGOtherVoucherDetailsCopy;
    }

    formatGOtherVoucherTemp() {
        // todo loại bỏ những thứ thêm vào và so sánh
        const tempGOtherVoucher = Object.assign({}, this.gOtherVoucher);
        tempGOtherVoucher.currentBook = null;
        tempGOtherVoucher.totalAmount = null;
        tempGOtherVoucher.totalAmountOriginal = null;
        return tempGOtherVoucher;
    }

    formatGOtherVoucherCopyTemp() {
        const tempGOtherVoucherCopy = Object.assign({}, this.gOtherVoucherCopy);
        tempGOtherVoucherCopy.currentBook = null;
        tempGOtherVoucherCopy.totalAmount = null;
        tempGOtherVoucherCopy.totalAmountOriginal = null;
        return tempGOtherVoucherCopy;
    }

    canDeactive() {
        if (this.isClosing || !this.gOtherVoucherCopy || this.isClosed || this.isReadOnly) {
            return true;
        }
        return !this.checkChangeGOtherVoucher();
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.isClosing) {
            this.closeAll();
        }
        this.isClosing = false;
    }

    closeModal() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isClosing = false;
    }

    closeAll() {
        this.isClosed = true;
        this.dataSession = JSON.parse(sessionStorage.getItem('ketChuyenSearchData'));

        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
        sessionStorage.removeItem('ketChuyenDataSession');
        if (window.location.href.includes('/from-g-other-voucher')) {
            window.history.back();
        } else {
            this.router.navigate(
                ['/ket-chuyen-lai-lo'],
                this.dataSession
                    ? {
                          queryParams: {
                              page: this.page,
                              size: this.itemsPerPage,
                              sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                          }
                      }
                    : null
            );
        }
    }

    private handleError(err) {
        this.isLoading = false;
        this.isClosing = false;
        this.close();
        this.toastrService.error(this.translateService.instant(`ebwebApp.pporder.${err.error.message}`));
    }

    changeAmount(detail: IGOtherVoucherDetails) {
        if (this.currency.formula === '/') {
            detail.amountOriginal = detail.amount * (this.gOtherVoucher.exchangeRate ? this.gOtherVoucher.exchangeRate : 1);
        } else {
            detail.amountOriginal = detail.amount / (this.gOtherVoucher.exchangeRate ? this.gOtherVoucher.exchangeRate : 1);
        }
        detail.amountOriginal = this.utilsService.round(detail.amountOriginal, this.account.systemOption, 7);
        this.changeAmountGOtherVoucher();
    }

    changeDate() {
        if (!this.isReadOnly && this.gOtherVoucher.date) {
            this.gOtherVoucher.postedDate = this.gOtherVoucher.date;
            this.changeReason();
            this.changDataWhenChangePostDate();
        }
    }

    changePostDate() {
        if (!this.isReadOnly) {
            this.changeReason();
            this.changDataWhenChangePostDate();
        }
    }

    changDataWhenChangePostDate() {
        if (this.gOtherVoucher.postedDate) {
            this.gOtherVoucher.gOtherVoucherDetails = [];
            this.getDataAccountSpecial();
        }
    }

    exportPdf(isDownload: boolean, typeReports) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.gOtherVoucher.id,
                typeID: TypeID.KET_CHUYEN_LAI_LO,
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
        }
    }

    roundObject() {
        this.utilsService.roundObjectsWithAccount(this.gOtherVoucher.gOtherVoucherDetails, this.account, this.currency.currencyCode);
        this.utilsService.roundObjectWithAccount(this.gOtherVoucher, this.account, this.currency.currencyCode);
    }

    ngAfterViewInit(): void {
        if (this.dataSession && this.dataSession.isEdit) {
            this.focusFirstInput();
        }
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    addLastInput(i: number) {
        if (i === this.gOtherVoucher.gOtherVoucherDetails.length - 1) {
            this.addNewRow(i);
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
    checkBeforeSave(isNew: boolean) {
        for (const item of this.gOtherVoucher.gOtherVoucherDetails) {
            if (item.amount < 0) {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(this.contentDataError, { backdrop: 'static' });
                return;
            }
        }
        this.saveGO(isNew);
    }
}
