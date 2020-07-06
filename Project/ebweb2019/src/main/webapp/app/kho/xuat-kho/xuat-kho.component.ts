import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Principal } from 'app/core';

import { DEFAULT_ROWS, ITEMS_PER_PAGE } from 'app/shared';
import { Irecord } from 'app/shared/model/record';
import { DataSessionStorage, IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { IPporder, Pporder } from 'app/shared/model/pporder.model';

import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { AccountingObjectDTO } from 'app/shared/model/accounting-object.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { UnitService } from 'app/danhmuc/unit';
import * as moment from 'moment';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { DatePipe } from '@angular/common';
import { RSInwardOutward } from 'app/shared/model/rs-inward-outward.model';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { RSInwardOutWardDetails } from 'app/shared/model/rs-inward-out-ward-details.model';
import {
    CURRENCY_ID,
    HH_XUATQUASLTON,
    MAU_BO_GHI_SO,
    PPINVOICE_TYPE,
    SO_LAM_VIEC,
    TCKHAC_SDTichHopHDDT,
    TypeID,
    RSOUTWARD_TYPE,
    REPORT
} from 'app/app.constants';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { SAInvoiceService } from 'app/ban-hang/ban_hang_chua_thu_tien';
import { PPDiscountReturnService } from 'app/muahang/hang_mua_tra_lai_giam_gia/pp-discount-return';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { ISAInvoice, SAInvoice } from 'app/shared/model/sa-invoice.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { IMaterialGoodsInStock } from 'app/shared/model/material-goods.model';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/handling-result/handling-result.model';
import { IType } from 'app/shared/model/type.model';
import { TypeService } from 'app/entities/type';
import { IPPDiscountReturn, PPDiscountReturn } from 'app/shared/model/pp-discount-return.model';
import { IPPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';
import { PPDiscountReturnDetailsService } from 'app/entities/pp-discount-return-details';
import { ISAInvoiceDetails } from 'app/shared/model/sa-invoice-details.model';
import { SAInvoiceDetailsService } from 'app/ban-hang/ban_hang_chua_thu_tien/sa-invoice-details.service';
import { ICareerGroup } from 'app/shared/model/career-group.model';
import { CareerGroupService } from 'app/entities/career-group';
import { RSInwardOutwardService } from 'app/entities/rs-inward-outward/rs-inward-outward.service';

@Component({
    selector: 'eb-xuat-kho',
    templateUrl: './xuat-kho.component.html',
    styleUrls: ['./xuat-kho.component.css']
})
export class XuatKhoComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('deleteItem') deleteItem;
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    @ViewChild('popUpMultiRecord') popUpMultiRecord: TemplateRef<any>;
    currentAccount: any;
    accountingObjectEmployees: AccountingObjectDTO[];
    rsInwardOutward: RSInwardOutward[];
    rsInwardOutwardDetailsLoadDataClick: RSInwardOutWardDetails[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    searchValue: string;
    links: any;
    currency: ICurrency;
    noType: number;
    currencies: ICurrency[];
    status: number;
    // rsInwardOutwardDetails: rsInwardOutwardDetail[];
    accountingObject: AccountingObjectDTO;
    accountingObjects: AccountingObjectDTO[];
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    currencyID: string;
    fromDate: any;
    toDate: any;
    recorded?: boolean;
    predicate: any;
    previousPage: any;
    isShowSearch: boolean;
    reverse: any;
    rsInwardOutwardDetails: RSInwardOutWardDetails[];
    accountingObjectEmployee: AccountingObjectDTO;
    selectedRow: RSInwardOutward;
    pageCount: any;
    accountingObjectName: string;
    record_: Irecord;
    rowNum: number;
    accountingObjectCode: string;
    ppDiscountReturns: PPDiscountReturn;
    saInvoice: SAInvoice;
    searchData: any;
    // dataSession: any;
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
    refVoucher: any;
    fromDateHolder: any;
    fromDateHolderMask: any;
    toDateHolder: any;
    toDateHolderMask: any;
    isErrorInvalid: any;
    account: any;
    isLoading: boolean;
    RSOUTWARD_TYPE = RSOUTWARD_TYPE;
    REPORT = REPORT;
    quantitySum: number;
    mainQuantitySum: number;
    totalAmount: number;
    index: number;
    isSoTaiChinh: number;
    // totalAmountSum: number;
    printMessages: any[];
    XUAT_KHO = TypeID.XUAT_KHO;
    XUAT_KHO_TU_DIEU_CHINH = TypeID.XUAT_KHO_TU_DIEU_CHINH;
    XUAT_KHO_TU_BAN_HANG = TypeID.XUAT_KHO_TU_BAN_HANG;
    XUAT_KHO_TU_HANG_MUA_TRA_LAI = TypeID.XUAT_KHO_TU_MUA_HANG;
    BAN_HANG_CHUA_THU_TIEN = TypeID.BAN_HANG_CHUA_THU_TIEN;
    BAN_HANG_THU_TIEN_NGAY_TM = TypeID.BAN_HANG_THU_TIEN_NGAY_TM;
    BAN_HANG_THU_TIEN_NGAY_CK = TypeID.BAN_HANG_THU_TIEN_NGAY_CK;
    checkModalRef: NgbModalRef;
    defaultRows: number;
    mgForPPOder: IMaterialGoodsInStock[];
    mgForPPOderTextCode: any;
    checkSLT: boolean;
    routerName: string;
    isTichHopHDDT: boolean;
    typeDelete: number;
    isFromPPDiscountReturn: boolean;
    isFromSaOrder: boolean;
    isFromSaInvoice: boolean;
    typeRecords: any[];
    statusRecords: any[];
    STATUS_RECORDED = 1;
    STATUS_NOT_RECORDED = 2;
    typeMultiAction: number;
    types: IType[];
    Type_group_outWard = 41;
    ppDiscountTax: boolean;
    saInvoiceTax: boolean;
    pPDiscountReturns: IPPDiscountReturn[];
    ppDiscountReturnDetails: IPPDiscountReturnDetails[];
    sumVATAmountOriginal: number;
    sumVATAmount: number;
    currencyCode: string;
    hiddenVAT: boolean;
    sAInvoiceDetails: ISAInvoiceDetails[];
    listVAT = [
        { name: '0%', data: 0 },
        { name: '5%', data: 1 },
        { name: '10%', data: 2 },
        { name: 'KCT', data: 3 },
        { name: 'KTT', data: 4 }
    ];
    careerGroups: ICareerGroup[];
    selectedRowTAX: ISAInvoice;
    disablePrint: boolean;

    /*Phân Quyền*/
    ROLE_XEM = ROLE.XuatKho_XEM;
    ROLE_THEM = ROLE.XuatKho_THEM;
    ROLE_SUA = ROLE.XuatKho_SUA;
    ROLE_XOA = ROLE.XuatKho_XOA;
    ROLE_GHI_SO = ROLE.XuatKho_GHI_SO;
    ROLE_IN = ROLE.XuatKho_IN;
    ROLE_KETXUAT = ROLE.XuatKho_KET_XUAT;

    constructor(
        private saInvoiceService: SAInvoiceService,
        private ppDiscountReturn: PPDiscountReturnService,
        private ppDiscountReturnDetailsService: PPDiscountReturnDetailsService,
        private currencyService: CurrencyService,
        public datepipe: DatePipe,
        private accountingObjectService: AccountingObjectService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private gLService: GeneralLedgerService,
        private materialGoodsService: MaterialGoodsService,
        private unitService: UnitService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private rsInwardOutwardService: RSInwardOutwardService,
        private eventManager: JhiEventManager,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private refModalService: RefModalService,
        private modalService: NgbModal,
        private translate: TranslateService,
        private sAInvoiceService: SAInvoiceService,
        public activeModal: NgbActiveModal,
        private typeService: TypeService,
        private sAInvoiceDetailsService: SAInvoiceDetailsService,
        private careerGroupService: CareerGroupService
    ) {
        super();
        this.defaultRows = DEFAULT_ROWS;
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
            this.predicate = 'date';
        });
        this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body
                .filter(type => type.typeGroupID === this.Type_group_outWard)
                .sort((a, b) => a.typeName.localeCompare(b.typeName));
        });
    }

    ngOnInit() {
        this.typeMultiAction = 1;
        this.rsInwardOutwardDetailsLoadDataClick = [];
        this.refVoucher = [];
        this.accountingObjects = [];
        this.isShowSearch = false;
        this.registerChangeInPPOrder();
        this.registerExport();
        this.registerChangeSession();
        this.accountingObjectService.getAccountingObjectsActive().subscribe((res: HttpResponse<AccountingObjectDTO[]>) => {
            this.accountingObjects = res.body;
            this.getSessionData();
        });
        this.getAccount();
        this.registerLockSuccess();
        this.registerUnlockSuccess();
        this.materialGoodsService.queryForComboboxGood({ materialsGoodsType: [0, 1, 3] }).subscribe(
            (res: HttpResponse<any>) => {
                this.mgForPPOder = res.body;
            },
            (res: HttpErrorResponse) => console.log(res.message)
        );

        this.translate
            .get([
                'ebwebApp.outWard.home.title',
                'ebwebApp.outWard.sell',
                'ebwebApp.outWard.returnGoods',
                'ebwebApp.outWard.adjusted',
                'ebwebApp.common.carrying',
                'ebwebApp.common.notCarrying'
            ])
            .subscribe(res => {
                this.typeRecords = [
                    { value: this.XUAT_KHO, name: res['ebwebApp.outWard.home.title'] },
                    { value: this.XUAT_KHO_TU_BAN_HANG, name: res['ebwebApp.outWard.sell'] },
                    { value: this.XUAT_KHO_TU_HANG_MUA_TRA_LAI, name: res['ebwebApp.outWard.returnGoods'] },
                    { value: this.XUAT_KHO_TU_DIEU_CHINH, name: res['ebwebApp.outWard.adjusted'] }
                ];
                this.statusRecords = [
                    { value: this.STATUS_RECORDED, name: res['ebwebApp.common.carrying'] },
                    { value: this.STATUS_NOT_RECORDED, name: res['ebwebApp.common.notCarrying'] }
                ];
            });
    }

    getAccount() {
        this.principal.identity().then(account => {
            this.account = account;
            this.checkSLT = this.account.systemOption.find(x => x.code === HH_XUATQUASLTON).data === '1';
            this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
            for (let i = 0; i < this.account.systemOption.length; i++) {
                if (!this.account.systemOption[i].data) {
                    this.account.systemOption[i].data = this.account.systemOption[i].defaultData;
                }
            }
            this.color = this.account.systemOption.find(item => item.code === MAU_BO_GHI_SO).data;
            this.currencyCode = account.organizationUnit.currencyID;
            this.hiddenVAT = this.account.organizationUnit.taxCalculationMethod === 1;
            this.careerGroupService.getCareerGroups().subscribe((res: HttpResponse<ICareerGroup[]>) => {
                this.careerGroups = res.body;
            });
        });
    }

    getMaterialGoodsByID(id) {
        if (this.mgForPPOder && id) {
            const materialGood = this.mgForPPOder.find(n => n.id === id);
            if (materialGood) {
                return materialGood.materialGoodsCode;
            }
        }
    }

    checkDisablePrint() {
        const nameAgeMapping = new Map();
        nameAgeMapping.set(this.selectedRows[0].typeID, this.selectedRows[0].typeID);
        for (let i = 0; i < this.selectedRows.length; i++) {
            if (!nameAgeMapping.has(this.selectedRows[i].typeID)) {
                this.disablePrint = true;
                break;
            }
        }
    }

    getAccountingObjectbyID(id) {
        if (this.accountingObjects && id) {
            const acc = this.accountingObjects.find(n => n.id === id.toLowerCase());
            if (acc) {
                return acc.accountingObjectCode;
            }
        }
    }

    getVATRate(vatRate) {
        if (this.listVAT && (vatRate || vatRate === 0)) {
            const acc = this.listVAT.find(n => n.data === vatRate);
            if (acc) {
                return acc.name;
            }
        }
    }

    getCareerGroupID(id) {
        if (this.careerGroups && id) {
            const ca = this.careerGroups.find(n => n.id === id.toLowerCase());
            if (ca) {
                return ca.careerGroupCode;
            }
        }
    }

    registerChangeSession() {
        this.eventManager.subscribe('changeSession', response => {
            this.getAccount();
            this.search();
        });
    }

    loadAllForSearch() {
        if (this.checkToDateGreaterFromDate()) {
            this.page = 1;
            this.search();
        }
    }

    resetSeach() {
        this.isErrorInvalid = false;
        this.accountingObject = {};
        this.status = null;
        this.noType = null;
        this.currency = {};
        this.accountingObjectEmployee = {};
        this.fromDate = '';
        this.toDate = '';
        this.searchValue = '';
        this.loadAllForSearch();
    }

    checkToDateGreaterFromDate(): boolean {
        if (this.fromDate && this.toDate && this.fromDate > this.toDate) {
            this.isErrorInvalid = true;
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.fromDateGreaterToDate'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        } else {
            this.isErrorInvalid = false;
            return true;
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition(true);
        }
    }

    getRowNumberOfRecord(page: number, index: number): number {
        if (page > 0 && index !== -1) {
            return (page - 1) * this.itemsPerPage + index + 1;
        }
    }

    printOutWard(isDownload: boolean, typeReports: number) {
        let reportTypeID: any;
        if (this.selectedRow.typeID === this.XUAT_KHO) {
            reportTypeID = this.XUAT_KHO;
        } else {
            if (this.selectedRow.refID) {
                if (this.selectedRow.typeID === this.XUAT_KHO_TU_BAN_HANG) {
                    reportTypeID = this.XUAT_KHO_TU_BAN_HANG;
                } else if (this.selectedRow.typeID === this.XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
                    reportTypeID = TypeID.MUA_HANG_TRA_LAI;
                } else if (this.selectedRow.typeID === this.XUAT_KHO_TU_DIEU_CHINH) {
                    // TODO: chuyển sang màn lập kho từ điều chỉnh
                    // this.router.navigate(['/hang-ban/tra-lai', res.body, 'rs-inward-outward']);
                }
            }
        }
        this.utilsService.getCustomerReportPDF(
            {
                id: this.selectedRow.id,
                typeID: reportTypeID,
                typeReport: typeReports
            },
            isDownload
        );
        switch (typeReports) {
            case RSOUTWARD_TYPE.TYPE_REPORT_ChungTuKeToan:
                this.toastr.success(
                    this.translate.instant('ebwebApp.mBDeposit.printing') +
                        this.translate.instant('ebwebApp.mBDeposit.financialPaper') +
                        '...',
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
                break;
            case RSOUTWARD_TYPE.TYPE_REPORT_XUAT_KHO:
            case RSOUTWARD_TYPE.TYPE_REPORT_XUAT_KHO_A5:
                this.toastr.success(
                    this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.outWard.billOutWrad') + '...',
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
                break;
        }
    }

    CTKTExportPDF(isDownload, typeReports: any) {
        this.ppDiscountReturn
            .getCustomerReport({
                id: this.ppDiscountReturns.id,
                typeID: TypeID.MUA_HANG_TRA_LAI,
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
                    const name = 'MUA_HANG_TRA_LAI.pdf';
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
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.mBDeposit.financialPaper') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === REPORT.ChungTuKeToanQuyDoi) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') +
                    this.translate.instant('ebwebApp.mBDeposit.financialPaperQD') +
                    '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === REPORT.PhieuThu) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') +
                    this.translate.instant('ebwebApp.mCReceipt.print.mCReceipt') +
                    '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === REPORT.GiayBaoCo) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.mBDeposit.creditNote') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === REPORT.PhieuXuatKho) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.outWard.billOutWrad') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        }
        if (typeReports === 10) {
            this.printMessages = [];
            this.translate
                .get(['ebwebApp.mBDeposit.printing', 'ebwebApp.sAInvoice.vatInvoice', 'ebwebApp.mBDeposit.message'])
                .subscribe(res => {
                    this.toastr.success(
                        res['ebwebApp.mBDeposit.printing'] + res['ebwebApp.sAInvoice.vatInvoice'],
                        res['ebwebApp.mBDeposit.message']
                    );
                });
        } else if (typeReports === 11) {
            this.printMessages = [];
            this.translate
                .get(['ebwebApp.mBDeposit.printing', 'ebwebApp.sAInvoice.saleInvoice', 'ebwebApp.mBDeposit.message'])
                .subscribe(res => {
                    this.toastr.success(
                        res['ebwebApp.mBDeposit.printing'] + res['ebwebApp.sAInvoice.saleInvoice'],
                        res['ebwebApp.mBDeposit.message']
                    );
                });
        }
    }

    printfSaInvoice(isDownload, typeReports: number) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.saInvoice.id,
                typeID: this.saInvoice.typeID,
                typeReport: typeReports
            },
            isDownload
        );
        this.toastr.success(this.translate.instant('ebwebApp.mBDeposit.printing'), this.translate.instant('ebwebApp.mBDeposit.message'));
    }

    onSelect(rsInwardOutward: RSInwardOutward) {
        if (!rsInwardOutward) {
            return;
        }
        this.selectedRow = rsInwardOutward;
        this.quantitySum = 0;
        this.mainQuantitySum = 0;
        this.totalAmount = 0;
        // this.totalAmountSum = 0;
        if (rsInwardOutward.id && rsInwardOutward.typeID) {
            this.rsInwardOutwardService.getDetailsOutWardById(rsInwardOutward.id, rsInwardOutward.typeID).subscribe(res => {
                this.rsInwardOutwardDetailsLoadDataClick = res.body.sort((a, b) => a.orderPriority - b.orderPriority);
                if (this.selectedRow) {
                    for (let i = 0; i < this.rsInwardOutwardDetailsLoadDataClick.length; i++) {
                        if (
                            this.rsInwardOutwardDetailsLoadDataClick[i].ppDiscountReturn !== null &&
                            this.rsInwardOutwardDetailsLoadDataClick[i].ppDiscountReturn !== undefined
                        ) {
                            this.isFromPPDiscountReturn = true;
                            this.isFromSaOrder = false;
                            this.isFromSaInvoice = false;
                            if (this.isSoTaiChinh) {
                                this.rsInwardOutwardDetailsLoadDataClick[
                                    i
                                ].noBookPPDiscountReturn = this.rsInwardOutwardDetailsLoadDataClick[i].ppDiscountReturn.noFBook;
                            } else {
                                this.rsInwardOutwardDetailsLoadDataClick[
                                    i
                                ].noBookPPDiscountReturn = this.rsInwardOutwardDetailsLoadDataClick[i].ppDiscountReturn.noMBook;
                            }
                        } else if (
                            this.rsInwardOutwardDetailsLoadDataClick[i].saInvoice !== null &&
                            this.rsInwardOutwardDetailsLoadDataClick[i].saInvoice !== undefined
                        ) {
                            this.isFromSaInvoice = true;
                            this.isFromPPDiscountReturn = false;
                            this.isFromSaOrder = false;
                            if (this.isSoTaiChinh) {
                                this.rsInwardOutwardDetailsLoadDataClick[i].noBookSaInvoice = this.rsInwardOutwardDetailsLoadDataClick[
                                    i
                                ].saInvoice.noFBook;
                            } else {
                                this.rsInwardOutwardDetailsLoadDataClick[i].noBookSaInvoice = this.rsInwardOutwardDetailsLoadDataClick[
                                    i
                                ].saInvoice.noMBook;
                            }
                        } else if (
                            this.rsInwardOutwardDetailsLoadDataClick[i].saOrder !== null &&
                            this.rsInwardOutwardDetailsLoadDataClick[i].saOrder !== undefined
                        ) {
                            this.isFromSaOrder = true;
                            this.isFromPPDiscountReturn = false;
                            this.isFromSaInvoice = false;
                            this.rsInwardOutwardDetailsLoadDataClick[i].noBookSaOrder = this.rsInwardOutwardDetailsLoadDataClick[
                                i
                            ].saOrder.no;
                        } else {
                            this.isFromPPDiscountReturn = false;
                            this.isFromSaOrder = false;
                            this.isFromSaInvoice = false;
                        }
                    }
                }
                if (rsInwardOutward.typeID === this.XUAT_KHO) {
                    this.saInvoiceTax = false;
                    this.ppDiscountTax = false;
                } else {
                    if (rsInwardOutward.refID) {
                        if (rsInwardOutward.typeID === this.XUAT_KHO_TU_BAN_HANG) {
                            this.sAInvoiceDetailsService.getDetailByID({ sAInvoiceID: rsInwardOutward.refID }).subscribe(ref => {
                                this.sAInvoiceDetails = ref.body.sort((n1, n2) => {
                                    return n1.orderPriority - n2.orderPriority;
                                });
                            });
                            this.saInvoiceService.find(rsInwardOutward.refID).subscribe(response => {
                                this.saInvoice = response.body;
                            });
                            this.saInvoiceTax = true;
                            this.ppDiscountTax = false;
                        } else if (rsInwardOutward.typeID === this.XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
                            this.ppDiscountReturnDetailsService
                                .getDetailByID({ ppDiscountReturnId: rsInwardOutward.refID })
                                .subscribe(response => {
                                    this.ppDiscountReturnDetails = response.body;
                                    this.sumVATAmount = 0;
                                    this.sumVATAmountOriginal = 0;
                                    for (const item of this.ppDiscountReturnDetails) {
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
                                    }
                                });
                            this.ppDiscountReturn.find(rsInwardOutward.refID).subscribe(response => {
                                this.ppDiscountReturns = response.body;
                            });
                            this.ppDiscountTax = true;
                            this.saInvoiceTax = false;
                        } else if (rsInwardOutward.typeID === this.XUAT_KHO_TU_DIEU_CHINH) {
                            // TODO: chuyển sang màn lập kho từ điều chỉnh
                            // this.router.navigate(['/hang-ban/tra-lai', res.body, 'rs-inward-outward']);
                        }
                    }
                }
                this.setSumOnClick();
            });
        }
        if (rsInwardOutward.id) {
            this.rsInwardOutwardService.getRefVouchersByPPOrderID(rsInwardOutward.id).subscribe(res => (this.refVoucher = res.body));
        }
        this.disablePrint = false;
        if (this.selectedRows.length > 1) {
            this.checkDisablePrint();
        }
        // this.updateSum();
    }

    isForeignCurrency() {
        return this.account.organizationUnit.currencyID !== CURRENCY_ID;
    }

    getOriginalType() {
        if (this.isForeignCurrency()) {
            return 8;
        }
        return 7;
    }

    setSumOnClick() {
        this.quantitySum = 0;
        this.mainQuantitySum = 0;
        this.totalAmount = 0;
        for (const item of this.rsInwardOutwardDetailsLoadDataClick) {
            this.quantitySum += item.quantity;
            this.mainQuantitySum += item.mainQuantity;
            this.totalAmount += item.amount;
        }
        this.quantitySum = this.quantitySum || 0;
        this.mainQuantitySum = this.mainQuantitySum || 0;
        this.totalAmount = this.totalAmount || 0;
    }

    toggleSearch($event) {
        $event.preventDefault();
        this.isShowSearch = !this.isShowSearch;
    }

    search() {
        this.rsInwardOutwardService
            .searchAllOutWard({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: this.status || '',
                noType: this.noType || '',
                accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
                employee: this.accountingObjectEmployee && this.accountingObjectEmployee.id ? this.accountingObjectEmployee.id : '',
                searchValue: this.searchValue || ''
            })
            .subscribe(
                (res: HttpResponse<RSInwardOutward[]>) => this.paginatePPOrder(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    transition(isTransition) {
        if (isTransition) {
            this.selectedRow = null;
        }
        /*this.router.navigate(['/xuat-kho'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });*/
        this.search();
    }

    clear() {
        this.page = 0;
        this.router.navigate([
            '/xuat-kho',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.search();
    }

    doubleClickRow(rsInwardOutward: RSInwardOutward, index?: number, isEdit?: boolean) {
        if (this.dataSession) {
            this.dataSession.isEdit = isEdit;
        }
        index = !index ? this.rsInwardOutward.indexOf(rsInwardOutward) : index;
        this.saveSearchData(rsInwardOutward, index);
        if (rsInwardOutward.typeID === this.XUAT_KHO) {
            this.router.navigate(['/xuat-kho', rsInwardOutward.id, 'edit', this.rowNum]);
        } else {
            if (rsInwardOutward.refID) {
                if (rsInwardOutward.typeID === this.XUAT_KHO_TU_BAN_HANG) {
                    this.router.navigate(['/chung-tu-ban-hang', rsInwardOutward.refID, 'edit-rs-inward-outward']);
                } else if (rsInwardOutward.typeID === this.XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
                    this.router.navigate(['/hang-mua/tra-lai', rsInwardOutward.refID, 'edit-rs-inward-outward']);
                } else if (rsInwardOutward.typeID === this.XUAT_KHO_TU_DIEU_CHINH) {
                    // TODO: chuyển sang màn lập kho từ điều chỉnh
                    // this.router.navigate(['/hang-ban/tra-lai', res.body, 'rs-inward-outward']);
                }
            }
        }
    }

    addNew($event = null) {
        this.saveSearchData(this.selectedRow, this.index - 1);
        this.router.navigate(['/xuat-kho/new']);
    }

    saveSearchData(rsInwardOutward: RSInwardOutward, index: number) {
        this.selectedRow = rsInwardOutward;
        this.searchData = {
            fromDate: this.fromDate || '',
            toDate: this.toDate || '',
            noType: this.noType ? this.noType : '',
            status: this.status === this.STATUS_RECORDED || this.status === this.STATUS_NOT_RECORDED ? this.status : '',
            accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
            searchValue: this.searchValue || ''
        };
        this.rowNum = this.getRowNumberOfRecord(this.page, index);
        if (!this.dataSession) {
            this.dataSession = new DataSessionStorage();
        }
        this.dataSession.page = this.page;
        this.dataSession.itemsPerPage = this.itemsPerPage;
        this.dataSession.pageCount = this.pageCount;
        this.dataSession.totalItems = this.totalItems;
        this.dataSession.isShowSearch = this.isShowSearch;
        this.dataSession.rowNum = this.rowNum;
        this.dataSession.predicate = this.predicate;
        this.dataSession.reverse = this.reverse;
        this.dataSession.searchVoucher = JSON.stringify(this.searchData);
        sessionStorage.setItem('xuatKhoSearchData', JSON.stringify(this.dataSession));
        sessionStorage.setItem('xuatKhoDataSession', JSON.stringify(this.dataSession));
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('xuatKhoSearchData'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.searchData = JSON.parse(this.dataSession.searchVoucher);
            if (this.searchData) {
                this.fromDate = this.searchData.fromDate;
                this.toDate = this.searchData.toDate;
                this.status = this.searchData.status;
                this.noType = this.searchData.noType;
                this.accountingObject = this.accountingObjects.find(x => x.id === this.searchData.accountingObject);
                this.searchValue = this.searchData.searchValue;
            }
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
            this.isShowSearch = this.dataSession.isShowSearch;
        }
        sessionStorage.removeItem('xuatKhoSearchData');
        this.transition(false);
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.rsInwardOutward.length; i++) {
            total += this.rsInwardOutward[i][prop];
        }
        return total ? total : 0;
    }

    sumSaInvoice(prop) {
        let total = 0;
        for (let i = 0; i < this.sAInvoiceDetails.length; i++) {
            total += this.sAInvoiceDetails[i][prop];
        }
        return total ? total : 0;
    }

    // updateSum() {
    //     this.totalAmountSum = this.totalAmountSum || 0;
    //     this.totalAmountSum = this.sum('totalAmount');
    // }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IPporder) {
        return item.id;
    }

    registerChangeInPPOrder() {
        this.eventSubscriber = this.eventManager.subscribe('RSInwardOutwardListModification', response => this.search());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    onSetUpHolderMask() {
        this.fromDateHolderMask = moment(this.fromDate, 'DD/MM/YYYY').format('DD/MM/YYYY');
        this.toDateHolderMask = moment(this.toDate, 'DD/MM/YYYY').format('DD/MM/YYYY');
    }

    selectedItemPerPage() {
        this.search();
    }

    edit() {
        event.preventDefault();
        if (!this.dataSession) {
            this.dataSession = new DataSessionStorage();
        }
        if (this.selectedRow.id) {
            if (!this.selectedRow.recorded) {
                this.doubleClickRow(this.selectedRow, this.rsInwardOutward.indexOf(this.selectedRow), true);
            } else {
                this.dataSession.isEdit = false;
            }
        } else {
            this.dataSession.isEdit = false;
        }
    }

    private paginatePPOrder(data: RSInwardOutward[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.rsInwardOutward = data;
        this.objects = data;
        if (this.page > 1 && this.rsInwardOutward && this.rsInwardOutward.length === 0) {
            this.page = this.page - 1;
            this.loadAllForSearch();
            return;
        }
        if (this.rowNum && !this.index) {
            this.index = this.rowNum % this.itemsPerPage;
            this.index = this.index || this.itemsPerPage;
            this.selectedRow = this.rsInwardOutward[this.index - 1];
        } else if (this.index) {
            this.selectedRow = this.rsInwardOutward[this.index - 1];
        } else {
            this.selectedRow = this.rsInwardOutward[0];
        }
        const lstSelect = this.selectedRows.map(object => ({ ...object }));
        this.selectedRows = [];
        this.selectedRows.push(...this.rsInwardOutward.filter(n => lstSelect.some(m => m.id === n.id)));
        this.rowNum = this.getRowNumberOfRecord(this.page, 0);
        this.rsInwardOutwardDetailsLoadDataClick = [];
        this.onSelect(this.selectedRow || this.rsInwardOutward[0] || null);
        this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
        // this.updateSum();
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    updateFromDateHolder() {
        this.fromDate = this.utilsService.formatStrDate(this.fromDateHolder);
    }

    updateToDateHolder() {
        this.toDate = this.utilsService.formatStrDate(this.toDateHolder);
    }

    record() {
        event.preventDefault();
        if (this.selectedRows && this.selectedRows.length > 1) {
            this.typeMultiAction = 0;
            this.checkModalRef = this.modalService.open(this.popUpMultiRecord, { backdrop: 'static' });
            return;
        } else {
            if (!this.checkCloseBook(this.account, this.selectedRow.postedDate)) {
                this.utilsService
                    .checkQuantityExistsTest1(this.rsInwardOutwardDetailsLoadDataClick, this.account, this.selectedRow.postedDate)
                    .then(data => {
                        if (data) {
                            this.mgForPPOderTextCode = data;
                            if (!this.checkSLT && this.mgForPPOderTextCode.quantityExists) {
                                this.toastr.error(
                                    this.translate.instant('ebwebApp.pPDiscountReturnDetails.error.quantityExistsRecordErrorSave'),
                                    this.translate.instant('ebwebApp.mBDeposit.message')
                                );
                                return;
                            }
                            this.isLoading = true;
                            if (!this.selectedRow.recorded) {
                                let record_: Irecord = {};
                                if (this.selectedRow.typeID === this.XUAT_KHO) {
                                    record_ = {
                                        id: this.selectedRow.id,
                                        typeID: this.selectedRow.typeID,
                                        repositoryLedgerID: this.selectedRow.id
                                    };
                                } else if (this.selectedRow.typeID === this.XUAT_KHO_TU_BAN_HANG) {
                                    record_ = { id: this.selectedRow.refID, typeID: TypeID.BAN_HANG_CHUA_THU_TIEN };
                                } else if (this.selectedRow.typeID === this.XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
                                    record_ = {
                                        id: this.selectedRow.refID,
                                        typeID: TypeID.MUA_HANG_TRA_LAI,
                                        repositoryLedgerID: this.selectedRow.id
                                    };
                                } else if (this.selectedRow.typeID === this.XUAT_KHO_TU_DIEU_CHINH) {
                                    // TODO: Xuat kho tu dieu chinh
                                }
                                record_.msg = 'XUAT_KHO';
                                if (!this.selectedRow.recorded) {
                                    this.gLService.record(record_).subscribe(
                                        (res: HttpResponse<Irecord>) => {
                                            if (res.body.success) {
                                                this.toastr.success(
                                                    this.translate.instant('ebwebApp.mBTellerPaper.recordToast.done'),
                                                    this.translate.instant('ebwebApp.mBTellerPaper.message.title')
                                                );
                                                this.selectedRow.recorded = true;
                                            } else {
                                                this.toastr.error(
                                                    this.translate.instant('ebwebApp.mBTellerPaper.recordToast.failed'),
                                                    this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                                                );
                                            }
                                            this.isLoading = false;
                                        },
                                        () => {
                                            this.isLoading = false;
                                        }
                                    );
                                }
                            }
                        }
                    });
            }
        }
    }

    unrecord() {
        event.preventDefault();
        if (this.selectedRows && this.selectedRows.length > 1) {
            this.typeMultiAction = 1;
            this.checkModalRef = this.modalService.open(this.popUpMultiRecord, { backdrop: 'static' });
            return;
        } else {
            if (!this.checkCloseBook(this.account, this.selectedRow.postedDate)) {
                this.isLoading = true;
                let record_: Irecord = {};
                if (this.selectedRow.typeID === this.XUAT_KHO) {
                    record_ = {
                        id: this.selectedRow.id,
                        typeID: this.selectedRow.typeID,
                        repositoryLedgerID: this.selectedRow.id
                    };
                } else if (this.selectedRow.typeID === this.XUAT_KHO_TU_BAN_HANG) {
                    record_ = {
                        id: this.selectedRow.refID,
                        typeID: TypeID.BAN_HANG_CHUA_THU_TIEN || TypeID.BAN_HANG_THU_TIEN_NGAY_CK || TypeID.BAN_HANG_THU_TIEN_NGAY_TM,
                        repositoryLedgerID: this.selectedRow.id
                    };
                } else if (this.selectedRow.typeID === this.XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
                    record_ = {
                        id: this.selectedRow.refID,
                        typeID: TypeID.MUA_HANG_TRA_LAI,
                        repositoryLedgerID: this.selectedRow.id
                    };
                } else if (this.selectedRow.typeID === this.XUAT_KHO_TU_DIEU_CHINH) {
                    // TODO: Xuat kho tu dieu chinh
                }
                record_.msg = 'XUAT_KHO';
                if (this.selectedRow.recorded) {
                    this.gLService.unrecord(record_).subscribe(
                        (res: HttpResponse<Irecord>) => {
                            if (res.body.success) {
                                this.toastr.success(
                                    this.translate.instant('ebwebApp.mBTellerPaper.unrecordToast.done'),
                                    this.translate.instant('ebwebApp.mBTellerPaper.message.title')
                                );
                                this.selectedRow.recorded = false;
                            } else {
                                this.toastr.error(
                                    this.translate.instant('ebwebApp.mBTellerPaper.unrecordToast.failed'),
                                    this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                                );
                            }
                            this.isLoading = false;
                        },
                        () => {
                            this.isLoading = false;
                        }
                    );
                }
            }
        }
    }

    exportExcel() {
        this.rsInwardOutwardService
            .exportOutWard('excel', {
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: this.status === this.STATUS_RECORDED || this.status === this.STATUS_NOT_RECORDED ? this.status : '',
                noType: this.noType || '',
                accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
                employee: this.accountingObjectEmployee && this.accountingObjectEmployee.id ? this.accountingObjectEmployee.id : '',
                searchValue: this.searchValue || ''
            })
            .subscribe(res => {
                const blob = new Blob([res.body], { type: 'application/vnd.ms-excel' });
                const fileURL = URL.createObjectURL(blob);

                const link = document.createElement('a');
                document.body.appendChild(link);
                link.download = fileURL;
                link.setAttribute('style', 'display: none');
                const name = 'DS_NhapKho.xls';
                link.setAttribute('download', name);
                link.href = fileURL;
                link.click();
            });
    }

    exportPdf() {
        this.rsInwardOutwardService
            .exportOutWard('pdf', {
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: this.status === this.STATUS_RECORDED || this.status === this.STATUS_NOT_RECORDED ? this.status : '',
                noType: this.noType || '',
                accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
                employee: this.accountingObjectEmployee && this.accountingObjectEmployee.id ? this.accountingObjectEmployee.id : '',
                searchValue: this.searchValue || ''
            })
            .subscribe(res => {
                this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.XUAT_KHO);
            });
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.XUAT_KHO}`, () => {
            this.exportExcel();
        });
    }

    delete() {
        event.preventDefault();
        if (this.selectedRows.length > 1) {
            this.checkModalRef = this.modalService.open(this.popUpMultiDelete, { backdrop: 'static' });
        } else {
            if (!this.checkCloseBook(this.account, this.selectedRow.postedDate)) {
                if (!this.selectedRow.recorded) {
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
                    }
                    this.checkModalRef = this.modalService.open(this.deleteItem, { size: 'lg', backdrop: 'static' });
                }
            }
        }
    }

    confirmDelete() {
        if (this.selectedRow && this.selectedRow.id) {
            /*Check chứng từ hiện tại đã phát hành hay hóa đơn hay chưa*/
            if (this.selectedRow.typeID === this.XUAT_KHO_TU_BAN_HANG || this.selectedRow.typeID === this.XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
                this.isTichHopHDDT = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
                if (this.selectedRow.refIsBill && this.selectedRow.refInvoiceForm === 0 && this.isTichHopHDDT) {
                    if (this.selectedRow.refInvoiceNo) {
                        this.typeDelete = 2;
                    } else {
                        this.typeDelete = 0;
                    }
                } else if (this.selectedRow.refIsBill) {
                    this.typeDelete = 1;
                } else {
                    this.typeDelete = 0;
                }
            }
            /*End*/
            if (this.selectedRow.typeID === this.XUAT_KHO_TU_BAN_HANG) {
                if (this.selectedRow.refTypeID && this.typeDelete !== 2) {
                    this.sAInvoiceService.delete(this.selectedRow.refID).subscribe(response => {
                        this.toastr.success(this.translate.instant('ebwebApp.sAInvoice.delete.success'));
                        this.checkModalRef.close();
                        this.search();
                    });
                } else {
                    this.toastr.error(this.translate.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
                    this.checkModalRef.dismiss(true);
                }
            } else if (this.selectedRow.typeID === this.XUAT_KHO_TU_HANG_MUA_TRA_LAI) {
                if (this.selectedRow.typeID && this.typeDelete !== 2) {
                    this.ppDiscountReturn.delete(this.selectedRow.refID).subscribe(response => {
                        this.toastr.success(this.translate.instant('ebwebApp.saReturn.deleted'));
                        this.checkModalRef.close();
                        this.search();
                    });
                } else {
                    this.toastr.error(this.translate.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
                    this.checkModalRef.dismiss(true);
                }
            } else if (this.selectedRow.typeID === this.XUAT_KHO_TU_DIEU_CHINH) {
                // TODO: nhap kho tu dieu chinh
            } else if (this.selectedRow.typeID === this.XUAT_KHO) {
                this.rsInwardOutwardService.delete(this.selectedRow.id).subscribe(response => {
                    this.toastr.success(this.translate.instant('ebwebApp.saReturn.deleted'));
                    this.checkModalRef.close();
                    this.search();
                });
            }
        }
    }

    export() {
        this.exportPdf();
    }

    registerLockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('lockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
                this.account = account;
            });
            this.loadAllForSearch();
        });
    }

    registerUnlockSuccess() {
        this.eventSubscriber = this.eventManager.subscribe('unlockSuccess', response => {
            this.principal.identity().then(account => {
                this.currentAccount = account;
                this.account = account;
            });
            this.loadAllForSearch();
        });
    }

    closePopUpDelete() {
        if (this.checkModalRef) {
            this.checkModalRef.close();
        }
    }

    continueDelete() {
        this.rsInwardOutwardService.multiDelete(this.selectedRows).subscribe(
            (res: HttpResponse<any>) => {
                if (this.checkModalRef) {
                    this.checkModalRef.close();
                }
                this.selectedRows = [];
                this.loadAllForSearch();
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
                    this.toastr.success(this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'));
                }
                this.activeModal.close();
                // }
            },
            (res: HttpErrorResponse) => {
                if (res.error.errorKey === 'errorDeleteList') {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.mBDeposit.errorDeleteVoucherNo'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                }
                if (this.checkModalRef) {
                    this.checkModalRef.close();
                }
            }
        );
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

    continueMultiAction() {
        if (this.typeMultiAction === 1) {
            this.rsInwardOutwardService.multiUnRecord(this.selectedRows).subscribe(
                (res: HttpResponse<any>) => {
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
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
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBCreditCard.unrecordSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    // this.activeModal.close();
                    this.search();
                },
                (res: HttpErrorResponse) => {
                    this.toastr.error(
                        this.translate.instant('global.data.unRecordFailed'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
                    }
                }
            );
        } else if (this.typeMultiAction === 0) {
            const listRecord: RequestRecordListDtoModel = {};
            listRecord.typeIDMain = TypeID.XUAT_KHO;
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
                    if (res.body.listFail && res.body.listFail.length > 0) {
                        if (res.body.countFailVouchers > 0) {
                            res.body.listFail.forEach(n => {
                                if (n.rSInwardOutwardID) {
                                    const rs = this.rsInwardOutward.find(r => r.id === n.rSInwardOutwardID);
                                    const type = this.types.find(t => t.id === rs.typeID);
                                    n.noFBook = rs.noFBook;
                                    n.noMBook = rs.noMBook;
                                    n.typeName = type.typeName;
                                } else {
                                    const type = this.types.find(t => t.id === n.typeID);
                                    n.typeName = type.typeName;
                                }
                            });
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
                        }
                    } else {
                        this.toastr.success(
                            this.translate.instant('ebwebApp.mBCreditCard.recordSuccess'),
                            this.translate.instant('ebwebApp.mBDeposit.message')
                        );
                    }
                    // this.activeModal.close();
                    this.search();
                },
                (res: HttpErrorResponse) => {
                    this.toastr.error(
                        this.translate.instant('global.data.recordFailed'),
                        this.translate.instant('ebwebApp.mBDeposit.message')
                    );
                    if (this.checkModalRef) {
                        this.checkModalRef.close();
                    }
                }
            );
        }
    }

    isRecord() {
        return this.selectedRows.some(n => n.recorded) && !this.selectedRows.some(n => !n.recorded);
    }
}
