import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { Principal } from 'app/core';

import { DATE_FORMAT, ITEMS_PER_PAGE } from 'app/shared';
import { ChiPhiTraTruocService } from './chi-phi-tra-truoc.service';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { ICurrency } from 'app/shared/model/currency.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IType } from 'app/shared/model/type.model';
import { IMultipleRecord } from 'app/shared/model/mutiple-record';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { CurrencyService } from 'app/danhmuc/currency';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { DatePipe } from '@angular/common';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { ToastrService } from 'ngx-toastr';
import { TypeService } from 'app/entities/type';
import { TranslateService } from '@ngx-translate/core';
import { ExpenseItemService } from 'app/entities/expense-item';
import { DDSo_NCachHangDVi, DDSo_NCachHangNghin, DDSo_NgoaiTe, DDSo_TienVND, SO_LAM_VIEC } from 'app/app.constants';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { IPrepaidExpense } from 'app/shared/model/prepaid-expense.model';
import { PrepaidExpenseAllocation } from 'app/shared/model/prepaid-expense-allocation.model';
import { PrepaidExpenseService } from 'app/entities/prepaid-expense';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { PrepaidExpenseVoucherService } from 'app/entities/prepaid-expense-voucher';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { PrepaidExpensesComponent } from 'app/shared/modal/prepaid-expenses/prepaid-expenses.component';

@Component({
    selector: 'eb-chi-phi-tra-truoc',
    templateUrl: './chi-phi-tra-truoc.component.html',
    styleUrls: ['./chi-phi-tra-truoc.component.css']
})
export class ChiPhiTraTruocComponent extends BaseComponent implements OnInit {
    @ViewChild('deleteModal') deleteModal: TemplateRef<any>;
    typeExpense: any;
    currentAccount: any;
    prepaidExpenses: any[];
    isSaving: boolean;
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    typeID: string;
    fromDate: any;
    toDate: any;
    currencyID: string;
    recorded: boolean;
    accountingObjectCode: string;
    objSearch: any;
    currencies: ICurrency[];
    accountingObject: IAccountingObject[];
    types: IType[];
    typeName: string;
    date: any;
    isRecord: Boolean;
    items: any;
    record_: any;
    selectedRow: IPrepaidExpense;
    rowNum: number;
    pageCount: any;
    accountingObjectName: string;
    isShowSearch: boolean;
    typeGOtherVoucher: number;
    translateTypeGOtherVoucher: string;
    searchValue: string;
    listRecord: ({ name: string | any; value: null } | { name: string | any; value: boolean } | { name: string | any; value: boolean })[];
    listColumnsRecord: string[] = ['name'];
    listHeaderColumnsRecord: string[] = ['Trạng thái'];
    reSearchWhenRecord: string;
    statusRecordAfterFind: Boolean;
    mutipleRowSelected: IGOtherVoucher[];
    selectedPersonArray = [];
    isKeyPressed: boolean;
    viewVouchers: any;
    options: any;
    DDSo_NCachHangNghin: any;
    DDSo_NCachHangDVi: any;
    DDSo_TienVND: any;
    DDSo_NgoaiTe: any;
    isRecorded: boolean;
    isUnRecorded: boolean;
    iMutipleRecord: IMultipleRecord;
    costSets: ICostSet[];
    expenseItems: IExpenseItem[];
    organizationUnits: IOrganizationUnit[];
    eMContracts: IEMContract[];
    budgetItem: IBudgetItem[];
    statisticsCodes: IStatisticsCode[];
    goodsServicePurchases: IGoodsServicePurchase[];
    isSoTaiChinh: boolean;
    isFocusCurrentVoucher: boolean;
    index: any;
    bankAccountDetails: IBankAccountDetails[];
    statusRecord: any;
    currentRowSelected: any;
    prepaidExpenseAllocation: any[];
    modalRef: NgbModalRef;
    // navigate update form
    dataSession: IDataSessionStorage = new class implements IDataSessionStorage {
        accountingObjectName: string;
        itemsPerPage: number;
        page: number;
        pageCount: number;
        predicate: number;
        reverse: number;
        rowNum: number;
        totalItems: number;
        isShowSearch: boolean;
    }();
    prepaidExpenseCodeList: any[];
    typeExpenseList = [{ id: 1, name: '' }, { id: 0, name: '' }];

    expenseItemList: IExpenseItem[];
    prepaidExpenseVoucher: any[];
    ROLE_Xem = ROLE.ChiPhiTRaTruoc_Xem;
    ROLE_Them = ROLE.ChiPhiTRaTruoc_Them;
    ROLE_Sua = ROLE.ChiPhiTRaTruoc_Sua;
    ROLE_Xoa = ROLE.ChiPhiTRaTruoc_Xoa;
    constructor(
        private modalService: NgbModal,
        private prepaidExpenseVoucherService: PrepaidExpenseVoucherService,
        private prepaidExpenseService: PrepaidExpenseService,
        private expenseItemService: ExpenseItemService,
        private chiPhiTraTruocService: ChiPhiTraTruocService,
        private refModalService: RefModalService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private accountingObjectService: AccountingObjectService,
        private currencyService: CurrencyService,
        public utilsService: UtilsService,
        private datepipe: DatePipe,
        private gLService: GeneralLedgerService,
        private toastr: ToastrService,
        private typeService: TypeService,
        public translate: TranslateService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        if (this.isSoTaiChinh) {
            this.predicate = 'date desc,postedDate desc,noFBook';
        } else {
            this.predicate = 'date desc,postedDate desc,noMBook';
        }
        this.isRecorded = true;
        this.isUnRecorded = true;
        this.typeGOtherVoucher = 700;
        this.translateTypeGOtherVoucher = this.translate.instant('ebwebApp.gOtherVoucher.home.title');
    }

    loadAll() {
        this.page = 1;
        this.search();
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/chi-phi-tra-truoc'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.search();
    }

    ngOnInit() {
        this.isShowSearch = false;
        this.objSearch = {};
        this.getSessionData();
        if (!this.page) {
            this.page = 1;
        }
        this.translate
            .get(['ebwebApp.prepaidExpense.radio.prepaidExpense', 'ebwebApp.prepaidExpense.radio.prepaidExpense2'])
            .subscribe(res => {
                this.typeExpenseList[0].name = this.translate.instant('ebwebApp.prepaidExpense.radio.prepaidExpense');
                this.typeExpenseList[1].name = this.translate.instant('ebwebApp.prepaidExpense.radio.prepaidExpense2');
            });
        this.dataSession = new class implements IDataSessionStorage {
            creditCardType: string;
            itemsPerPage: number;
            ownerCard: string;
            page: number;
            pageCount: number;
            predicate: number;
            reverse: number;
            rowNum: number;
            searchVoucher: string;
            totalItems: number;
        }();
        this.index = 0;
        this.isRecorded = false;
        this.isUnRecorded = false;
        this.iMutipleRecord = {};
        this.statusRecordAfterFind = null;
        this.reSearchWhenRecord = '{}';
        this.isFocusCurrentVoucher = true;
        this.recorded = null;
        this.mutipleRowSelected = [];
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (account) {
                this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
            }
            this.currencyService.findAllActive().subscribe(res => {
                this.currencies = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
            });
            this.DDSo_NCachHangNghin = this.currentAccount.systemOption.find(x => x.code === DDSo_NCachHangNghin && x.data).data;
            this.DDSo_NCachHangDVi = this.currentAccount.systemOption.find(x => x.code === DDSo_NCachHangDVi && x.data).data;
            this.DDSo_NgoaiTe = this.currentAccount.systemOption.find(x => x.code === DDSo_NgoaiTe && x.data).data;
            this.DDSo_TienVND = this.currentAccount.systemOption.find(x => x.code === DDSo_TienVND && x.data).data;
        });
        this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body.filter(type => type.typeGroupID === 70);
        });
        this.isKeyPressed = false;
        this.getPrepaidExpenseCode();
        this.getExpenseItemService();
        this.search();
        this.prepaidExpenseAllocation = [];
    }

    trackId(index: number, item: IGOtherVoucher) {
        return item.id;
    }

    sort() {
        const result = ['date' + ',' + (this.reverse ? 'desc' : 'asc')];
        // result.push('postedDate' + ',' + (this.reverse ? 'asc' : 'desc'));
        result.push('postedDate' + ',' + (this.reverse ? 'desc' : 'asc'));
        if (this.isSoTaiChinh) {
            result.push('noFBook' + ',' + (this.reverse ? 'desc' : 'asc'));
        } else {
            result.push('noMBook' + ',' + (this.reverse ? 'desc' : 'asc'));
        }
        // const result = ['date, desc', 'postedDate, desc'];
        return result;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    search() {
        if (this.objSearch.fromDate && this.objSearch.toDate) {
            if (this.objSearch.fromDate > this.objSearch.toDate) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mBDeposit.fromDateMustBeLessThanToDate'),
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
                return;
            }
        }
        this.chiPhiTraTruocService
            .getAll({
                typeExpense:
                    this.objSearch.typeExpense !== null && this.objSearch.typeExpense !== undefined ? this.objSearch.typeExpense : '',
                fromDate: this.objSearch.fromDate ? this.objSearch.fromDate : '',
                toDate: this.objSearch.toDate ? this.objSearch.toDate : '',
                textSearch: this.objSearch.textSearch ? this.objSearch.textSearch : '',
                page: this.page - 1,
                size: this.itemsPerPage
            })
            .subscribe(res => {
                this.links = this.parseLinks.parse(res.headers.get('link'));
                this.totalItems = parseInt(res.headers.get('X-Total-Count'), 10);
                this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
                this.previousPage = this.page;
                this.prepaidExpenses = res.body;
                this.objects = res.body;
                if (res.body.length > 0) {
                    if (this.selectedRow) {
                        this.selectedRow = this.prepaidExpenses.find(item => item.id === this.selectedRow.id);
                        if (!this.selectedRow) {
                            this.selectedRow = this.prepaidExpenses[0];
                        }
                    } else {
                        this.selectedRow = this.prepaidExpenses[0];
                    }
                    this.onSelect(this.selectedRow);
                } else {
                    this.prepaidExpenseAllocation = [];
                }
            });
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    clearSearch() {
        this.objSearch = {};
        this.objSearch.fromDate = null;
        this.objSearch.toDate = null;
        this.objSearch.typeExpense = null;
        this.statusRecord = null;
        this.page = 1;
        this.search();
    }

    selectedItemPerPage(itemsPerPage: any) {
        this.loadPage(itemsPerPage);
    }

    private paginateMBDepositsTemp(data: IGOtherVoucher[], headers: HttpHeaders) {
        if (this.totalItems > 0) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.found') +
                    this.totalItems +
                    ' ' +
                    this.translate.instant('ebwebApp.mBDeposit.record'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        } else {
            this.toastr.warning(
                this.translate.instant('ebwebApp.mBDeposit.notFoundRecord'),
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    onSelect(select: any) {
        if (select) {
            this.selectedRow = select;
            this.index = this.prepaidExpenses.indexOf(this.selectedRow);
            this.rowNum = this.getRowNumberOfRecord(this.page, this.index);
            this.prepaidExpenseService.getPrepaidExpenseAllocationByID(this.selectedRow.id).subscribe(res => {
                this.prepaidExpenseAllocation = res.body;
                for (const item of this.prepaidExpenseAllocation) {
                    item.allocationObjectCode = this.getPrepaidExpenseCodeItem(item);
                    item.expenseItemCode = this.getExpenseItemServiceCode(item);
                }
            });
            this.prepaidExpenseService.findPrepaidExpenseByID(this.selectedRow.id).subscribe(res => {
                this.prepaidExpenseVoucher = res.body;
            });
        }
    }

    doubleClickRow(id: string) {
        if (id) {
            this.dataSession.page = this.page;
            this.dataSession.itemsPerPage = this.itemsPerPage;
            this.dataSession.pageCount = this.pageCount;
            this.dataSession.totalItems = this.totalItems;
            this.dataSession.rowNum = this.rowNum;
            this.dataSession.isShowSearch = this.isShowSearch;
            // this.index = this.gOtherVouchers.indexOf(this.selectedRow);
            this.currentRowSelected = { page: this.page, index: this.index };
            sessionStorage.setItem('objectSearch', JSON.stringify(this.objSearch));
            sessionStorage.setItem('itemPrepaidExpenses', JSON.stringify(this.selectedRow));
            sessionStorage.setItem('dataSessionPrepaidExpenses', JSON.stringify(this.dataSession));
            this.router.navigate(['./chi-phi-tra-truoc', id, 'edit']);
        }
    }

    deleteAfter() {
        if (this.selectedRows && this.selectedRows.length > 1) {
            this.chiPhiTraTruocService.multiDelete(this.selectedRows).subscribe(
                res => {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    const listDelete = this.selectedRows;
                    const listDeletedFailRes = res.body.listFail;
                    const listDeletedFail = [];
                    for (let j = 0; j < this.selectedRows.length; j++) {
                        for (let i = 0; i < listDeletedFailRes.length; i++) {
                            if (listDeletedFailRes[i].id === this.selectedRows[j].id) {
                                listDeletedFail.push(this.selectedRows[j]);
                            }
                        }
                    }
                    this.selectedRows.filter(x => listDeletedFailRes.map(y => y.id).includes(x.id));
                    const lst = this.selectedRows.filter(x => listDeletedFailRes.map(y => y.id).includes(x.id));
                    this.search();
                    if (res.body.countTotalVouchers !== res.body.countSuccessVouchers) {
                        this.modalRef = this.refModalService.open(
                            res.body,
                            PrepaidExpensesComponent,
                            {
                                listDelete,
                                listDeletedFail,
                                messages: 'Không thể xóa vì phát sinh chứng từ phân bổ!'
                            },
                            false,
                            null,
                            null,
                            null,
                            null,
                            null,
                            true
                        );
                    } else {
                        this.toastr.success(this.translate.instant('ebwebApp.pPDiscountReturn.delete.success'));
                    }
                },
                (res: HttpErrorResponse) => {
                    if (res.error.errorKey === 'errorDeleteList') {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.mBDeposit.errorDeleteVoucherNo'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                }
            );
        } else {
            if (this.selectedRow && this.selectedRow.id) {
                this.prepaidExpenseService.delete(this.selectedRow.id).subscribe(
                    res => {
                        this.toastr.success(this.translate.instant('ebwebApp.pPDiscountReturn.delete.success'));
                        if (this.modalRef) {
                            this.modalRef.close();
                        }
                        this.search();
                    },
                    res => {
                        this.toastr.error(this.translate.instant('ebwebApp.pPDiscountReturn.delete.error'));
                    }
                );
            }
        }
    }

    transformAmount(value: number) {
        const amount = parseFloat(value.toString().replace(/,/g, ''))
            .toFixed(this.DDSo_NgoaiTe)
            .toString()
            .replace('.', this.DDSo_NCachHangDVi)
            .replace(/\B(?=(\d{3})+(?!\d))/g, this.DDSo_NCachHangNghin);
        return amount;
    }

    getEMContractsbyID(id) {
        if (this.eMContracts) {
            const eMC = this.eMContracts.find(n => n.id === id);
            if (eMC) {
                return eMC.noMBook;
            }
        }
    }

    cssMultipleRow(igOtherVoucher: IGOtherVoucher) {
        if (this.mutipleRowSelected) {
            if (this.mutipleRowSelected.length > 0) {
                for (let i = 0; i < this.mutipleRowSelected.length; i++) {
                    if (this.mutipleRowSelected[i] === igOtherVoucher) {
                        return this.mutipleRowSelected[i];
                    }
                }
            }
        }
    }

    private convertDateFromClient(searchVoucher: ISearchVoucher): ISearchVoucher {
        const copy: ISearchVoucher = Object.assign({}, searchVoucher, {
            fromDate:
                searchVoucher.fromDate != null && searchVoucher.fromDate.isValid() ? searchVoucher.fromDate.format(DATE_FORMAT) : null,
            toDate: searchVoucher.toDate != null && searchVoucher.toDate.isValid() ? searchVoucher.toDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    getPrepaidExpenseCode() {
        this.chiPhiTraTruocService.getPrepaidExpenseCodeCanActive().subscribe(ref => {
            this.prepaidExpenseCodeList = ref.body;
        });
    }

    private getPrepaidExpenseCodeItem(item: PrepaidExpenseAllocation) {
        if (item && item.allocationObjectID) {
            return this.prepaidExpenseCodeList.find(x => x.id === item.allocationObjectID).code;
        }
        return null;
    }

    // Khoan muc chi phi
    getExpenseItemService() {
        this.expenseItemService.getAllExpenseItems().subscribe(ref => {
            this.expenseItemList = ref.body;
        });
    }

    getExpenseItemServiceCode(item: PrepaidExpenseAllocation) {
        if (item) {
            const pre = this.expenseItemList.find(x => x.id === item.expenseItemID);
            return pre ? pre.expenseItemCode : null;
        }
    }

    delete() {
        event.preventDefault();
        if (this.selectedRows && this.selectedRows.length > 1) {
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.modalRef = this.modalService.open(this.deleteModal, { backdrop: 'static' });
        } else {
            if (this.selectedRow && this.selectedRow.id) {
                this.chiPhiTraTruocService.countByPrepaidExpenseID(this.selectedRow.id).subscribe(res => {
                    // nếu chưa phân bổ hoặc đã ngừng phân bổ thì mới được xóa
                    if (res.body > 0) {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.prepaidExpense.delete.check'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                        return;
                    } else {
                        if (this.modalRef) {
                            this.modalRef.close();
                        }
                        this.modalRef = this.modalService.open(this.deleteModal, { backdrop: 'static' });
                    }
                });
            } else {
                this.toastr.error(
                    this.translate.instant('ebwebApp.prepaidExpense.delete.selectCheck'),
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
                return;
            }
        }
    }
    getSessionData() {
        this.selectedRow = JSON.parse(sessionStorage.getItem('itemPrepaidExpenses'))
            ? JSON.parse(sessionStorage.getItem('itemPrepaidExpenses'))
            : null;
        this.dataSession = JSON.parse(sessionStorage.getItem('dataSessionPrepaidExpenses'))
            ? JSON.parse(sessionStorage.getItem('dataSessionPrepaidExpenses'))
            : {};
        const obj = JSON.parse(sessionStorage.getItem('objectSearch'));
        this.objSearch = obj ? obj : {};
        sessionStorage.removeItem('itemPrepaidExpenses');
        sessionStorage.removeItem('dataSessionPrepaidExpenses');
        sessionStorage.removeItem('objectSearch');
        if (this.dataSession) {
            if (this.dataSession.itemsPerPage) {
                this.itemsPerPage = this.dataSession.itemsPerPage;
            }
            if (this.dataSession.page) {
                this.page = this.dataSession.page;
            }
            this.isShowSearch = this.dataSession.isShowSearch;
        }
        this.search();
    }

    addNew($event) {
        $event.preventDefault();
        this.router.navigate(['/chi-phi-tra-truoc/new']);
    }

    edit() {
        event.preventDefault();
        this.doubleClickRow(this.selectedRow.id);
    }
}
