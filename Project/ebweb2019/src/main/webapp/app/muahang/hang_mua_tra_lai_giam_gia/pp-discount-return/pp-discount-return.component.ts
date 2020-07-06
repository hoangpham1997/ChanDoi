import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IPPDiscountReturn, PPDiscountReturn } from 'app/shared/model/pp-discount-return.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { PPDiscountReturnService } from './pp-discount-return.service';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { CurrencyService } from 'app/danhmuc/currency';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IPPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';
import { PPDiscountReturnDetailsService } from 'app/entities/pp-discount-return-details';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ToastrService } from 'ngx-toastr';
import { Irecord } from 'app/shared/model/record';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import * as moment from 'moment';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { HH_XUATQUASLTON, MAU_BO_GHI_SO, SD_SO_QUAN_TRI, SO_LAM_VIEC, TCKHAC_SDTichHopHDDT, TypeID } from 'app/app.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { ViewVoucherService } from 'app/shared/modal/ref/view-voucher.service';

@Component({
    selector: 'eb-pp-discount-return',
    templateUrl: './pp-discount-return.component.html',
    styleUrls: ['discount-return.css']
})
export class PPDiscountReturnComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('deleteItem') deleteItemModal: TemplateRef<any>;
    @ViewChild('deleteItemCheck') deleteItemCheckModal: TemplateRef<any>;
    @ViewChild('popUpMultiRecord') popUpMultiRecord: TemplateRef<any>;
    currentAccount: any;
    pPDiscountReturns: IPPDiscountReturn[];
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
    recorded: null;
    isShowSearch: any;
    currencies: ICurrency[];
    accountingObject: any;
    currency: any;
    trackbyCurrencyID: any;
    fromDate: any;
    toDate: any;
    keySearch: any;
    public pageCount: any;
    accountingObjects: IAccountingObject[];
    ppDiscountReturnDetails: IPPDiscountReturnDetails[];
    dataSession: IDataSessionStorage = new class implements IDataSessionStorage {
        accountingObjectName: string;
        itemsPerPage: number;
        page: number;
        pageCount: number;
        predicate: number;
        reverse: number;
        rowNum: number;
        totalItems: number;
        isEdit: boolean;
    }();
    selectedRow: IPPDiscountReturn;
    rowNum: number;
    ppDiscountReturn: PPDiscountReturn;
    record_: Irecord;
    searchData: any;
    searchDataSearch: any;
    checkModalRef: NgbModalRef;
    refVoucher: any;
    statusPurchase: boolean;
    MUA_HANG_GIAM_GIA = TypeID.MUA_HANG_GIAM_GIA;
    MUA_HANG_TRA_LAI = TypeID.MUA_HANG_TRA_LAI;
    authoritiesNoMBook: Boolean;
    checkBook: Boolean;
    typeDelete: number;
    account: any;
    isTichHopHDDT: boolean;
    multipleRowSelected: IPPDiscountReturn[];
    isRecorded: boolean;
    isUnRecorded: boolean;
    listID: any[];
    contTypeDelete: number;
    ROLE_Them1 = ROLE.HangMuaTraLai_Them;
    ROLE_Them2 = ROLE.HangMuaGiamGia_Them;
    ROLE_Sua1 = ROLE.HangMuaTraLai_Sua;
    ROLE_Sua2 = ROLE.HangMuaGiamGia_Sua;
    ROLE_Xoa1 = ROLE.HangMuaTraLai_Xoa;
    ROLE_Xoa2 = ROLE.HangMuaGiamGia_Xoa;
    ROLE_GhiSo1 = ROLE.HangMuaTraLai_GhiSo;
    ROLE_GhiSo2 = ROLE.HangMuaGiamGia_GhiSo;
    ROLE_KetXuat2 = ROLE.HangMuaGiamGia_KetXuat;
    ROLE_KetXuat1 = ROLE.HangMuaTraLai_KetXuat;
    typeAmount = true;
    typeAmountOriginal = true;
    sumQuantity: number;
    sumUnitPriceOriginal: number;
    sumUnitPrice: number;
    sumMainQuantity: number;
    sumMainUnitPrice: number;
    sumAmount: number;
    sumAmountOriginal: number;
    sumVATAmount: number;
    sumVATAmountOriginal: number;
    mgForPPOderTextCode: any;
    checkSLT: boolean;
    mgForPPOder: any[];
    countRSI: number;
    checkOpenModalMap = new Map();
    typeMultiAction: number;
    isShowHHDV: boolean;

    constructor(
        private toastService: ToastrService,
        private viewVoucherService: ViewVoucherService,
        private refModalService: RefModalService,
        private modalService: NgbModal,
        private translateService: TranslateService,
        private gLService: GeneralLedgerService,
        private pPDiscountReturnService: PPDiscountReturnService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        public translate: TranslateService,
        public utilService: UtilsService,
        private pPDiscountReturnDetailsService: PPDiscountReturnDetailsService,
        private eventManager: JhiEventManager,
        private accountingObjectService: AccountingObjectService,
        private materialGoodsService: MaterialGoodsService,
        private currencyService: CurrencyService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            if (data.pagingParams && data.pagingParams.page) {
                this.page = data.pagingParams.page;
            }
            // this.previousPage = data.pagingParams.page;
            if (data.pagingParams && data.pagingParams.ascending) {
                this.reverse = data.pagingParams.ascending;
            }
            if (data.pagingParams && data.pagingParams.predicate) {
                this.predicate = data.pagingParams.predicate;
            }
            if (data.statusPurchase !== null && data.statusPurchase !== undefined) {
                this.statusPurchase = data.statusPurchase;
            } else {
                this.statusPurchase = false;
            }
        });
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
        // this.router.navigate(['/giam-gia'], {
        //     queryParams: {
        //         page: this.page,
        //         size: this.itemsPerPage,
        //         sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        //     }
        // });
        this.search();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            'hang-mua/tra-lai',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
                this.isShowHHDV = this.currentAccount.organizationUnit.taxCalculationMethod === 1;
                this.typeAmount =
                    this.currentAccount.organizationUnit.currencyID && this.currentAccount.organizationUnit.currencyID === 'VND';
                this.typeAmountOriginal = this.selectedRow.currencyID && this.selectedRow.currencyID === 'VND';
                this.authoritiesNoMBook = this.currentAccount.systemOption.find(x => x.code === SD_SO_QUAN_TRI).data === '1';
                this.color = this.currentAccount.systemOption.find(item => item.code === MAU_BO_GHI_SO).data;
                this.checkSLT = this.currentAccount.systemOption.find(x => x.code === HH_XUATQUASLTON).data === '1';
                if (this.currentAccount.systemOption.find(x => x.code === SO_LAM_VIEC).data === '1') {
                    this.checkBook = this.authoritiesNoMBook ? true : false;
                } else {
                    this.checkBook = false;
                }
                this.authoritiesNoMBook = this.currentAccount.systemOption.find(x => x.code === SD_SO_QUAN_TRI).data === '1';
                if (data.pPDiscountReturn && data.pPDiscountReturn.id) {
                    this.ppDiscountReturn = data.pPDiscountReturn;
                    this.selectedRow = data.pPDiscountReturn;
                    this.getSessionData();
                    this.convertSessionData();
                    // this.search();
                } else {
                    this.getSessionData();
                    if (this.dataSession && this.dataSession.statusDelete) {
                        this.convertSessionData();
                    } else {
                        this.search();
                    }
                }
            });
        });
        this.typeMultiAction = 1;
        this.currentAccount = { organizationUnit: {} };
        this.registerExport();
        this.registerChangeInPPDiscountReturns();
        this.keySearch = '';
        this.recorded = null;
        this.currency = null;
        this.accountingObject = null;
        this.isShowSearch = false;
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencies = res.body;
        });
        this.accountingObjectService.getAccountingObjectActive().subscribe((res: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjects = res.body;
        });
        this.registerLockSuccess();
        this.registerUnlockSuccess();
        this.materialGoodsService.queryForComboboxGood().subscribe(res => {
            this.mgForPPOder = res.body;
        });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('dataSession')) ? JSON.parse(sessionStorage.getItem('dataSession')) : null;
        console.log(JSON.stringify(this.dataSession));
        sessionStorage.removeItem('dataSession');
    }
    convertSessionData() {
        if (this.dataSession) {
            const searchDataIndex = JSON.parse(this.dataSession.searchVoucher) ? JSON.parse(this.dataSession.searchVoucher) : {};
            this.fromDate = searchDataIndex.fromDate ? moment(searchDataIndex.fromDate) : '';
            this.toDate = searchDataIndex.toDate ? moment(searchDataIndex.toDate) : '';
            this.recorded = searchDataIndex.status !== null && searchDataIndex.status !== null ? searchDataIndex.status : '';
            this.isShowSearch = searchDataIndex.isShowSearch;
            this.keySearch = searchDataIndex.searchValue ? searchDataIndex.searchValue : '';
            this.page = this.dataSession.page ? this.dataSession.page : 0;
            this.previousPage = this.dataSession.page ? this.dataSession.page : 0;
            this.itemsPerPage = this.dataSession.itemsPerPage ? this.dataSession.itemsPerPage : ITEMS_PER_PAGE;
            this.pageCount = this.dataSession.pageCount ? this.dataSession.pageCount : 0;
            this.totalItems = this.dataSession.totalItems ? this.dataSession.totalItems : 0;
            this.rowNum = this.dataSession.rowNum ? this.dataSession.rowNum : 0;
            this.predicate = this.dataSession.predicate;
            // this.selectedRow.id = searchDataIndex.id;
            this.reverse = this.dataSession.reverse;
            if (!this.currency) {
                this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                    this.currencies = res.body;
                    this.currency = searchDataIndex.currency && searchDataIndex.currency ? searchDataIndex.currency : '';
                    if (searchDataIndex.accountingObject) {
                        if (!this.accountingObjects) {
                            this.accountingObjectService.getAccountingObjectActive().subscribe((ref: HttpResponse<IAccountingObject[]>) => {
                                this.accountingObjects = ref.body;
                                this.accountingObject = this.accountingObjects.find(item => item.id === searchDataIndex.accountingObject);
                                this.search();
                            });
                        } else {
                            this.accountingObject = this.accountingObjects.find(item => item.id === searchDataIndex.accountingObject);
                            this.search();
                        }
                    } else {
                        this.search();
                    }
                });
            } else {
                this.search();
            }
        } else {
            this.dataSession = {};
            this.search();
        }
    }
    clearSearch() {
        this.keySearch = '';
        this.recorded = null;
        this.currency = null;
        this.fromDate = null;
        this.toDate = null;
        this.accountingObject = null;
        this.loadAll();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IPPDiscountReturn) {
        return item.id;
    }

    registerChangeInPPDiscountReturns() {
        this.eventSubscriber = this.eventManager.subscribe('pPDiscountReturnListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginatePPDiscountReturns(data: IPPDiscountReturn[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.pPDiscountReturns = data ? data : [];
        this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
        if (this.ppDiscountReturn && this.ppDiscountReturn.id) {
            this.selectedRow = this.pPDiscountReturns.find(n => n.id === this.ppDiscountReturn.id);
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    search() {
        if (this.checkPostedDateGreaterDate()) {
            this.searchData = {
                currency: this.currency ? this.currency : '',
                fromDate: this.fromDate ? this.fromDate : '',
                toDate: this.toDate ? this.toDate : '',
                status: this.recorded === 1 || this.recorded === 0 ? this.recorded : '',
                isShowSearch: this.isShowSearch,
                accountingObject: this.accountingObject ? this.accountingObject : {},
                searchValue: this.keySearch ? this.keySearch : ''
            };
            this.pPDiscountReturnService
                .searchPPDiscountReturn({
                    accountingObjectID: this.searchData.accountingObject.id ? this.searchData.accountingObject.id : '',
                    currencyID: this.searchData.currency,
                    fromDate: this.searchData.fromDate,
                    toDate: this.searchData.toDate,
                    status: this.searchData.status,
                    keySearch: this.searchData.searchValue,
                    statusPurchase: this.statusPurchase !== null && this.statusPurchase !== undefined ? this.statusPurchase : false,
                    page: this.page ? this.page - 1 : 0,
                    size: this.itemsPerPage
                    // sort: this.sort()
                })
                .subscribe(
                    res => {
                        this.previousPage = this.page;
                        this.objects = res.body;
                        this.paginatePPDiscountReturns(res.body, res.headers);
                        if (res.body.length > 0) {
                            if (!this.selectedRow) {
                                if (res.body.length > 0) {
                                    this.onSelect(res.body[0], event);
                                }
                            } else {
                                const select = res.body.find(n => n.id === this.selectedRow.id);
                                if (!select) {
                                    if (res.body.length > 0) {
                                        this.onSelect(res.body[0], event);
                                    }
                                } else {
                                    if (res.body.length > 0) {
                                        this.onSelect(select, event);
                                    }
                                }
                            }
                            // nếu tìm kiếm ra rỗng
                            if (!this.selectedRow) {
                                this.ppDiscountReturnDetails = [];
                            }
                        } else {
                            this.ppDiscountReturnDetails = [];
                        }
                        this.sumAllList();
                    },
                    resizeTo => {
                        this.page = 1;
                        this.search();
                    }
                );
        }
    }

    selectedItemPerPage() {
        this.page = 1;
        this.search();
    }

    sendData(item: IPPDiscountReturn) {
        this.ppDiscountReturnDetails = item.ppDiscountReturnDetails;
        console.log(item);
    }

    onSelect(item: IPPDiscountReturn, event) {
        this.selectedRow = item;
        if (this.selectedRow) {
            this.typeAmountOriginal = this.selectedRow.currencyID && this.selectedRow.currencyID === 'VND';
            if (item.id) {
                this.pPDiscountReturnDetailsService.getDetailByID({ ppDiscountReturnId: item.id }).subscribe(res => {
                    this.ppDiscountReturnDetails = res.body;
                    this.sumAllList();
                });
            }
            this.pPDiscountReturnService.getRefVouchersByPPdiscountReturnID(this.selectedRow.id).subscribe(ref => {
                this.refVoucher = ref.body;
            });
        } else {
            this.ppDiscountReturnDetails = [];
        }
        // if (event.ctrlKey || event.shiftKey) {
        //     if (item.recorded) {
        //         this.isRecorded = true;
        //     } else {
        //         this.isUnRecorded = true;
        //     }
        //     this.toggleItemInArr(this.multipleRowSelected, item);
        // } else {
        //     this.isRecorded = false;
        //     this.isUnRecorded = false;
        //     this.multipleRowSelected = [];
        // }
    }

    toggleItemInArr(arr, item) {
        const index = arr.indexOf(item);
        index === -1 ? arr.push(item) : arr.splice(index, 1);
    }
    doubleClickRowItem(item: IPPDiscountReturn, index: number) {
        this.searchData = {
            currency: this.currency && this.currency ? this.currency : '',
            fromDate: this.fromDate ? this.fromDate : '',
            toDate: this.toDate ? this.toDate : '',
            status: this.recorded === 1 || this.recorded === 0 ? this.recorded : '',
            isShowSearch: this.isShowSearch,
            accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
            searchValue: this.keySearch ? this.keySearch : ''
        };
        if (!this.dataSession) {
            this.dataSession = {};
        }
        this.selectedRow = item;
        this.rowNum = this.getRowNumberOfRecord(this.page, index);
        this.dataSession.page = this.page ? this.page : 0;
        this.dataSession.itemsPerPage = this.itemsPerPage ? this.itemsPerPage : ITEMS_PER_PAGE;
        this.dataSession.pageCount = this.pageCount ? this.pageCount : 0;
        this.dataSession.totalItems = this.totalItems ? this.totalItems : 0;
        this.dataSession.rowNum = this.rowNum ? this.rowNum : 0;
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        this.dataSession.searchVoucher = JSON.stringify(this.searchData);
        sessionStorage.setItem('dataSession', JSON.stringify(this.dataSession));
        if (!this.statusPurchase) {
            this.router.navigate(['hang-mua/tra-lai', item.id, 'edit', this.rowNum]);
        } else {
            this.router.navigate(['hang-mua/giam-gia', item.id, 'edit', this.rowNum]);
        }
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('giam-gia') ? ROLE.HangMuaTraLai_GhiSo : ROLE.HangMuaGiamGia_GhiSo])
    record() {
        event.preventDefault();
        if (
            !(
                !this.pPDiscountReturns ||
                this.pPDiscountReturns.length === 0 ||
                !this.selectedRow ||
                (this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate) && this.selectedRows.length === 1)
            )
        ) {
            if (this.selectedRows && this.selectedRows.length > 1) {
                this.typeMultiAction = 0;
                this.checkModalRef = this.modalService.open(this.popUpMultiRecord, { backdrop: 'static' });
                return;
            } else {
                // if (!(!this.pPDiscountReturns || this.pPDiscountReturns.length === 0 ||
                // !this.selectedRow || this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate))) {
                this.utilService
                    .checkQuantityExistsTest1(this.ppDiscountReturnDetails, this.currentAccount, this.selectedRow.postedDate)
                    .then(data => {
                        if (data) {
                            this.mgForPPOderTextCode = data;
                            if (!this.checkSLT && this.mgForPPOderTextCode.quantityExists && this.selectedRow.isDeliveryVoucher) {
                                this.toastService.error(
                                    this.translateService.instant('ebwebApp.pPDiscountReturnDetails.error.quantityExistsRecordErrorSave'),
                                    this.translateService.instant('ebwebApp.mBDeposit.message')
                                );
                                return;
                            }
                            if (!this.selectedRow.recorded) {
                                this.record_ = {};
                                this.record_.id = this.selectedRow.id;
                                this.record_.typeID = this.selectedRow.typeID;
                                this.record_.repositoryLedgerID = this.selectedRow.rsInwardOutwardID;
                                this.gLService.record(this.record_).subscribe(
                                    (res: HttpResponse<Irecord>) => {
                                        if (res.body.success) {
                                            this.selectedRow.recorded = true;
                                            this.toastService.success(
                                                this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
                                                this.translate.instant('ebwebApp.mBDeposit.message')
                                            );
                                        }
                                    },
                                    (res: HttpErrorResponse) =>
                                        this.toastService.error(
                                            this.translate.instant('global.data.recordFailed'),
                                            this.translate.instant('ebwebApp.mBDeposit.message')
                                        )
                                );
                                // }
                            }
                        }

                        // else {
                        //     this.toastService.warning(
                        //         this.translate.instant('global.data.warningRecorded'),
                        //         this.translate.instant('ebwebApp.mBDeposit.message')
                        //     );
                        // }
                    });
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('giam-gia') ? ROLE.HangMuaTraLai_GhiSo : ROLE.HangMuaGiamGia_GhiSo])
    unrecord() {
        event.preventDefault();
        if (
            !(
                !this.pPDiscountReturns ||
                this.pPDiscountReturns.length === 0 ||
                !this.selectedRow ||
                (this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate) && this.selectedRows.length === 1)
            )
        ) {
            if (this.selectedRows && this.selectedRows.length > 1) {
                this.typeMultiAction = 1;
                this.checkModalRef = this.modalService.open(this.popUpMultiRecord, { backdrop: 'static' });
                return;
            } else {
                // if (!(!this.pPDiscountReturns || this.pPDiscountReturns.length === 0 || !this.selectedRow ||
                // (this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate) && this.selectedRows.length === 1))) {
                this.record_ = {};
                this.record_.id = this.selectedRow.id;
                this.record_.typeID = this.selectedRow.typeID;
                this.gLService.unrecord(this.record_).subscribe(
                    (res: HttpResponse<Irecord>) => {
                        if (res.body.success) {
                            this.selectedRow.recorded = false;
                            this.toastService.success(
                                this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                                this.translate.instant('ebwebApp.mBDeposit.message')
                            );
                        }
                    },
                    (res: HttpErrorResponse) =>
                        this.toastService.error(
                            this.translate.instant('global.data.unRecordFailed'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        )
                );
                // }
            }
        }
        // else {
        //     this.toastService.warning(
        //         this.translate.instant('global.data.warningUnRecorded'),
        //         this.translate.instant('ebwebApp.mBDeposit.message')
        //     );
        // }
    }

    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('giam-gia') ? ROLE.HangMuaTraLai_Xoa : ROLE.HangMuaGiamGia_Xoa])
    delete() {
        event.preventDefault();
        if (
            !(
                (this.ppDiscountReturnDetails && this.ppDiscountReturnDetails.length === 0) ||
                !this.selectedRow ||
                (this.selectedRow && this.selectedRow.recorded && this.selectedRows.length <= 1) ||
                (this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate) && this.selectedRows.length === 1)
            )
        ) {
            this.contTypeDelete = 0;
            if (this.selectedRows && this.selectedRows.length > 1) {
                let dem = 0;
                for (let i = 0; i < this.selectedRows.length; i++) {
                    if (this.selectedRows[i].recorded) {
                        dem++;
                    }
                }
                if (dem === this.selectedRows.length) {
                    this.toastService.error(
                        this.translate.instant('ebwebApp.mBDeposit.errorDeleteVoucherNo'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    return;
                }
                if (this.checkModalRef) {
                    this.checkModalRef.close();
                }
                this.checkModalRef = this.modalService.open(this.deleteItemModal, { backdrop: 'static' });
            } else {
                if (
                    !(
                        (this.pPDiscountReturns && this.pPDiscountReturns.length === 0) ||
                        !this.selectedRow ||
                        (this.selectedRow && this.selectedRow.recorded && this.selectedRows.length === 1) ||
                        this.checkCloseBook(this.currentAccount, this.selectedRow.postedDate)
                    )
                ) {
                    this.isTichHopHDDT = this.currentAccount.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
                    if (this.selectedRow.isBill && this.selectedRow.invoiceForm === 0 && this.isTichHopHDDT) {
                        if (this.selectedRow.invoiceNo) {
                            this.typeDelete = 2;
                        } else {
                            this.typeDelete = 0;
                        }
                    } else if (this.selectedRow.isBill) {
                        this.typeDelete = 1;
                    } else {
                        this.typeDelete = 0;
                    }
                    this.checkModalRef = this.modalService.open(this.deleteItemModal, { size: 'lg', backdrop: 'static' });
                }
            }
        }
    }
    checkValidateDelete() {
        if (!this.checkOpenModalMap.get(1)) {
            this.pPDiscountReturnService.countFromRSI(this.selectedRow.id).subscribe(res => {
                if (res.body > 0) {
                    this.countRSI = res.body;
                    this.checkOpenModalMap.set(1, 1);
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
                    }
                    this.checkModalRef = this.modalService.open(this.deleteItemCheckModal, { size: 'lg', backdrop: 'static' });
                    return;
                } else {
                    this.countRSI = 0;
                }
                this.deleteAfter();
            });
        }
    }
    deleteAfter() {
        // if (this.typeDelete === 2) {
        //     this.toastService.error(this.translateService.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
        //     return;
        // }
        // event.preventDefault();
        // if (!this.listID || this.listID.length <= 0) {
        //     this.listID = [];
        //     if (this.selectedRow && this.selectedRow.id) {
        //         this.listID.push(this.selectedRow.id);
        //     }
        // }
        // if (this.listID && this.listID.length > 0) {
        //     this.pPDiscountReturnService.multipleDelete(this.listID).subscribe(
        //         ref => {
        //             this.toastService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
        //             this.checkModalRef.close();
        //             this.ppDiscountReturnDetails = [];
        //             this.search();
        //         },
        //         ref => {
        //             this.toastService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.error'));
        //         }
        //     );
        // } else {
        //     this.toastService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.notItem'));
        // }

        this.listID = [];
        if (this.selectedRows && this.selectedRows.length > 1) {
            for (let i = 0; i < this.selectedRows.length; i++) {
                this.listID.push(this.selectedRows[i].id);
            }
            this.pPDiscountReturnService.multipleDelete(this.listID).subscribe(
                res => {
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
                    }
                    this.selectedRows = [];
                    this.search();
                    if (res.body.countTotalVouchers !== res.body.countSuccessVouchers) {
                        this.checkModalRef = this.refModalService.open(
                            res.body,
                            HandlingResultComponent,
                            null,
                            false,
                            null,
                            null,
                            null,
                            null,
                            null,
                            true
                        );
                    } else {
                        this.toastService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
                    }
                },
                (res: HttpErrorResponse) => {
                    if (res.error.errorKey === 'errorDeleteList') {
                        this.toastService.error(
                            this.translate.instant('ebwebApp.mBDeposit.errorDeleteVoucherNo'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
                    }
                }
            );
        } else {
            if (this.selectedRow && this.selectedRow.id) {
                if (this.typeDelete === 2) {
                    this.toastService.error(this.translateService.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
                } else {
                    this.checkOpenModalMap = new Map();
                    this.pPDiscountReturnService.delete(this.selectedRow.id).subscribe(
                        ref => {
                            this.toastService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
                            this.checkModalRef.close();
                            this.search();
                        },
                        ref => {
                            this.toastService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.error'));
                        }
                    );
                }
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

    getCurrentDate(): { year; month; day } {
        const _date = moment();
        return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
    }

    getFromToStr(date?: string, isMaxDate?: boolean): { year; month; day } {
        const _date = date && moment(date, 'YYYYMMDD').isValid() ? moment(date, 'YYYYMMDD') : isMaxDate ? null : moment();
        return _date ? { year: _date.year(), month: _date.month() + 1, day: _date.date() } : null;
    }

    checkPostedDateGreaterDate() {
        if (this.toDate && this.fromDate) {
            if (moment(this.toDate) < moment(this.fromDate)) {
                this.toastService.error(this.translateService.instant('ebwebApp.mBDeposit.fromDateMustBeLessThanToDate'));
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('giam-gia') ? ROLE.HangMuaTraLai_KetXuat : ROLE.HangMuaGiamGia_KetXuat])
    export() {
        event.preventDefault();
        this.searchData = {
            currency: this.currency ? this.currency : '',
            fromDate: this.fromDate ? this.fromDate : '',
            toDate: this.toDate ? this.toDate : '',
            status: this.recorded === 1 || this.recorded === 0 ? this.recorded : '',
            isShowSearch: this.isShowSearch,
            accountingObject: this.accountingObject ? this.accountingObject : {},
            searchValue: this.keySearch ? this.keySearch : ''
        };
        this.pPDiscountReturnService
            .export({
                accountingObjectID: this.searchData.accountingObject.id ? this.searchData.accountingObject.id : '',
                currencyID: this.searchData.currency,
                fromDate: this.searchData.fromDate,
                toDate: this.searchData.toDate,
                status: this.searchData.status,
                keySearch: this.searchData.searchValue,
                statusPurchase: this.statusPurchase !== null && this.statusPurchase !== undefined ? this.statusPurchase : false,
                page: this.page - 1,
                size: this.itemsPerPage
            })
            .subscribe(res => {
                this.refModalService.open(
                    null,
                    EbReportPdfPopupComponent,
                    res,
                    false,
                    !this.statusPurchase ? this.MUA_HANG_TRA_LAI : this.MUA_HANG_GIAM_GIA
                );
            });
    }
    exportExcel() {
        this.searchData = {
            currency: this.currency ? this.currency : '',
            fromDate: this.fromDate ? this.fromDate : '',
            toDate: this.toDate ? this.toDate : '',
            status: this.recorded === 1 || this.recorded === 0 ? this.recorded : '',
            isShowSearch: this.isShowSearch,
            accountingObject: this.accountingObject ? this.accountingObject : {},
            searchValue: this.keySearch ? this.keySearch : ''
        };
        this.pPDiscountReturnService
            .exportExcel({
                accountingObjectID: this.searchData.accountingObject.id ? this.searchData.accountingObject.id : '',
                currencyID: this.searchData.currency,
                fromDate: this.searchData.fromDate,
                toDate: this.searchData.toDate,
                status: this.searchData.status,
                keySearch: this.searchData.searchValue,
                statusPurchase: this.statusPurchase !== null && this.statusPurchase !== undefined ? this.statusPurchase : false,
                page: this.page - 1,
                size: this.itemsPerPage
            })
            .subscribe(res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = !this.statusPurchase ? 'DS_MuaHangTraLai.xls' : 'DS_MuaHangGiamGia.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            });
    }
    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(
            `export-excel-${!this.statusPurchase ? this.MUA_HANG_TRA_LAI : this.MUA_HANG_GIAM_GIA}`,
            response => {
                this.exportExcel();
            }
        );
    }

    getCurrencyType(type) {
        if (this.currentAccount.organizationUnit.currencyID === 'VND') {
            if (this.selectedRow.currencyID === 'VND') {
                return 1;
            } else {
                return type === 1 ? 2 : 1;
            }
        } else {
            if (this.selectedRow.currencyID === 'VND') {
                return type === 1 ? 1 : 2;
            } else {
                return 2;
            }
        }
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('giam-gia') ? ROLE.HangMuaTraLai_Them : ROLE.HangMuaGiamGia_Them])
    addNew($event) {
        $event.preventDefault();
        if (!this.statusPurchase) {
            this.router.navigate(['/hang-mua/tra-lai/new']);
        } else {
            this.router.navigate(['/hang-mua/giam-gia/new']);
        }
    }

    @ebAuth(['ROLE_ADMIN', !window.location.href.includes('giam-gia') ? ROLE.HangMuaTraLai_Sua : ROLE.HangMuaGiamGia_Sua])
    edit() {
        event.preventDefault();
        if (this.selectedRow.id) {
            if (!this.selectedRow.recorded) {
                this.dataSession.isEdit = true;
                this.doubleClickRowItem(this.selectedRow, this.pPDiscountReturns.indexOf(this.selectedRow));
            }
        }
        // else {
        //     this.dataSession.isEdit = false;
        //     this.toastService.error(
        //         this.translate.instant('ebwebApp.pPDiscountReturn.error.selectedItem'),
        //         this.translate.instant('ebwebApp.mBDeposit.message')
        //     );
        // }
    }

    sumAllList(checkExchangeRate?: Boolean) {
        this.sumQuantity = 0;
        this.sumUnitPriceOriginal = 0;
        this.sumUnitPrice = 0;
        this.sumMainQuantity = 0;
        this.sumMainUnitPrice = 0;
        this.sumAmount = 0;
        this.sumAmountOriginal = 0;
        this.sumVATAmount = 0;
        this.sumVATAmountOriginal = 0;
        if (this.ppDiscountReturnDetails && this.ppDiscountReturnDetails.length > 0) {
            this.ppDiscountReturnDetails.forEach(item => {
                // chọn loại tiền tính lại các quy đổi
                if (checkExchangeRate) {
                    // đơn giá quy đổi
                    item.unitPrice =
                        item.unitPriceOriginal && this.ppDiscountReturn.exchangeRate
                            ? item.unitPriceOriginal * this.ppDiscountReturn.exchangeRate
                            : 0;
                    // thành tiền quy đổi
                    if (item.amountOriginal && this.ppDiscountReturn.exchangeRate) {
                        item.amount = item.amountOriginal * this.ppDiscountReturn.exchangeRate;
                    }
                    // thuế gtgt quy đổi
                    if (item.vatAmountOriginal && this.ppDiscountReturn.exchangeRate) {
                        item.vatAmount = item.vatAmountOriginal * this.ppDiscountReturn.exchangeRate;
                    }
                }
                // tính tổng số lượng
                if (item.quantity) {
                    this.sumQuantity += item.quantity;
                }
                // tính tổng đơn giá
                if (item.unitPriceOriginal) {
                    this.sumUnitPriceOriginal += item.unitPriceOriginal;
                }
                // Tổng đơn giá quy đổi
                if (item.unitPrice) {
                    this.sumUnitPrice += item.unitPrice;
                }
                // số lượng theo đơn vị chính
                if (item.mainQuantity) {
                    this.sumMainQuantity += item.mainQuantity;
                }
                // tổng đơn giá theo dvc
                if (item.mainUnitPrice) {
                    this.sumMainUnitPrice += item.mainUnitPrice;
                }
                // tính tổng thành tiền quy đổi
                if (item.amount) {
                    this.sumAmount += item.amount;
                } else {
                    item.amount = 0;
                }
                // tính tổng thành tiền
                if (item.amountOriginal) {
                    this.sumAmountOriginal += item.amountOriginal;
                } else {
                    item.amountOriginal = 0;
                }
                // Tính tổng tiền thuế
                if (item.vatAmountOriginal) {
                    this.sumVATAmount += item.vatAmount;
                } else {
                    item.vatAmountOriginal = 0;
                }
                // tính tổng tiền thuế quy đổi
                if (item.vatAmount) {
                    this.sumVATAmountOriginal += item.vatAmountOriginal;
                } else {
                    item.vatAmount = 0;
                }
            });
        }
    }

    doubleClickRow(any?) {
        event.preventDefault();
        this.doubleClickRowItem(this.selectedRow, this.pPDiscountReturns.indexOf(this.selectedRow));
    }

    registerLockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('lockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
            this.loadAll();
        });
    }

    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
            });
            this.loadAll();
        });
    }

    continueMultiAction() {
        if (this.typeMultiAction === 1) {
            this.pPDiscountReturnService.multiUnRecord(this.selectedRows).subscribe(
                (res: HttpResponse<any>) => {
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
                    }
                    for (let i = 0; i < this.selectedRows.length; i++) {
                        const temp = res.body.listFail.some(r => r.refID === this.selectedRows[i].id);
                        if (!temp) {
                            this.selectedRows[i].recorded = false;
                        }
                    }
                    if (res.body.countTotalVouchers !== res.body.countSuccessVouchers) {
                        this.checkModalRef = this.refModalService.open(
                            res.body,
                            HandlingResultComponent,
                            null,
                            false,
                            null,
                            null,
                            null,
                            null,
                            null,
                            true
                        );
                    } else {
                        this.toastService.success(
                            this.translateService.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    // this.activeModal.close();
                    // this.search();
                },
                (res: HttpErrorResponse) => {
                    this.toastService.error(
                        this.translateService.instant('global.data.unRecordFailed'),
                        this.translateService.instant('ebwebApp.mBDeposit.message')
                    );
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
                    }
                }
            );
        } else if (this.typeMultiAction === 0) {
            const listRecord: RequestRecordListDtoModel = {};
            listRecord.typeIDMain = this.statusPurchase ? TypeID.MUA_HANG_GIAM_GIA : TypeID.MUA_HANG_TRA_LAI;
            listRecord.records = [];
            this.selectedRows.forEach(item => {
                listRecord.records.push({
                    id: item.id,
                    typeID: item.typeID
                });
            });
            this.gLService.recordList(listRecord).subscribe(
                (res: HttpResponse<HandlingResult>) => {
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
                    }
                    for (let i = 0; i < this.selectedRows.length; i++) {
                        const temp = res.body.listFail.some(r => r.refID === this.selectedRows[i].id);
                        if (!temp) {
                            this.selectedRows[i].recorded = true;
                        }
                    }
                    if (res.body.listFail && res.body.listFail.length > 0) {
                        this.checkModalRef = this.refModalService.open(
                            res.body,
                            HandlingResultComponent,
                            null,
                            false,
                            null,
                            null,
                            null,
                            null,
                            null,
                            true
                        );
                    } else {
                        this.toastService.success(
                            this.translateService.instant('ebwebApp.mBCreditCard.recordSuccess'),
                            this.translateService.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    // this.activeModal.close();
                    // this.search();
                },
                (res: HttpErrorResponse) => {
                    this.toastService.error(
                        this.translateService.instant('global.data.recordFailed'),
                        this.translateService.instant('ebwebApp.mBDeposit.message')
                    );
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
                    }
                }
            );
        }
    }

    checkMultiButton(isRecord: boolean) {
        if (this.selectedRows) {
            for (let i = 0; i < this.selectedRows.length; i++) {
                if (this.selectedRows[i] && this.selectedRows[i].recorded === isRecord) {
                    return true;
                }
            }
        }
        return false;
    }

    view(voucher) {
        // this.activeModal.dismiss(true);
        let url = '';
        console.log(voucher.typeGroupID);
        switch (voucher.typeGroupID) {
            // Hàng bán trả lại
            case 33:
                url = `/#/hang-ban/tra-lai/${voucher.refID2}/edit/from-ref`;
                break;
            // Giảm giá hàng bán
            case 34:
                url = `/#/hang-ban/giam-gia/${voucher.refID2}/edit/from-ref`;
                break;
            // Xuất hóa đơn
            case 35:
                url = `/#/xuat-hoa-don/${voucher.refID2}/edit/from-ref`;
                break;
            case 22:
                url = `/#/hang-mua/tra-lai/${voucher.refID2}/view`;
                break;
            case 23:
                url = `/#/hang-mua/giam-gia/${voucher.refID2}/view`;
                break;
            case 10:
                url = `/#/mc-receipt/${voucher.refID2}/edit/from-ref`;
                break;
            case 16:
                url = `/#/mb-deposit/${voucher.refID2}/edit/from-ref`;
                break;
            case 17:
                url = `/#/mb-credit-card/${voucher.refID2}/edit/from-ref`;
                break;
            case 70:
                url = `/#/g-other-voucher/${voucher.refID2}/edit/from-ref`;
                break;
            case 11:
                url = `/#/mc-payment/${voucher.refID2}/edit/from-ref`;
                break;
            case 31:
                url = `/#/sa-order/${voucher.refID2}/edit/from-ref`;
                break;
            case 24:
                url = `/#/mua-dich-vu/${voucher.refID2}/edit/from-ref`;
                break;
            case 40:
                url = `/#/nhap-kho/${voucher.refID2}/edit/from-ref`;
                break;
            case 20:
                url = `/#/don-mua-hang/${voucher.refID2}/edit/from-ref`;
                break;
            case 41:
                url = `/#/xuat-kho/${voucher.refID2}/edit/from-ref`;
                break;
            case 21:
                this.viewVoucherService.checkViaStockPPInvoice({ id: voucher.refID2 }).subscribe(
                    (res: HttpResponse<any>) => {
                        if (res.body.message === UpdateDataMessages.STOCK_TRUE) {
                            url = `/#/mua-hang/qua-kho-ref/${voucher.refID2}/edit/1`;
                            window.open(url, '_blank');
                        } else if (res.body.message === UpdateDataMessages.STOCK_FALSE) {
                            url = `/#/mua-hang/khong-qua-kho-ref/${voucher.refID2}/edit/1`;
                            window.open(url, '_blank');
                        } else {
                            this.toastService.error(this.translateService.instant('ebwebApp.pPInvoice.error.default'));
                        }
                        return;
                    },
                    (res: HttpErrorResponse) => {
                        this.toastService.error(this.translateService.instant('ebwebApp.pPInvoice.error.default'));
                    }
                );
                return;
            case 18:
                url = `/#/mc-audit/${voucher.refID2}/edit/from-ref`;
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
}
