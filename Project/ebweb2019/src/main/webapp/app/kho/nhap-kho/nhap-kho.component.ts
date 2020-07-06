import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Principal } from 'app/core';

import { DEFAULT_ROWS, ITEMS_PER_PAGE } from 'app/shared';
import { Irecord } from 'app/shared/model/record';
import { DataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { IPporder } from 'app/shared/model/pporder.model';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { AccountingObjectDTO } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { UnitService } from 'app/danhmuc/unit';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { DatePipe } from '@angular/common';
import { RSInwardOutward } from 'app/shared/model/rs-inward-outward.model';
import { RSInwardOutWardDetails } from 'app/shared/model/rs-inward-out-ward-details.model';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { CURRENCY_ID, MAU_BO_GHI_SO, MSGERROR, PPINVOICE_TYPE, SO_LAM_VIEC, TypeID, REPORT } from 'app/app.constants';
import { EbReportPdfPopupComponent } from 'app/shared/modal/show-pdf/eb-report-pdf-popup.component';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { PPInvoiceService } from 'app/muahang/mua_hang_qua_kho';
import { SaReturnService } from 'app/ban-hang/hang-ban-tra-lai-giam-gia/sa-return.service';
import { ROLE } from 'app/role.constants';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { HandlingResultComponent } from 'app/shared/modal/handling-result/handling-result.component';
import { RequestRecordListDtoModel } from 'app/shared/model/request-record-list-dto.model';
import { HandlingResult } from 'app/shared/modal/bank/bank-result.model';
import { IType } from 'app/shared/model/type.model';
import { TypeService } from 'app/entities/type';
import { IPPInvoiceDetailCost } from 'app/shared/model/pp-invoice-detail-cost.model';
import { SaReturnDetailsService } from 'app/ban-hang/hang-ban-tra-lai-giam-gia/sa-return-details.service';
import { PPInvoice } from 'app/shared/model/pp-invoice.model';
import { SaReturn } from 'app/shared/model/sa-return.model';
import { RSInwardOutwardService } from 'app/entities/rs-inward-outward/rs-inward-outward.service';

@Component({
    selector: 'eb-nhap-kho',
    templateUrl: './nhap-kho.component.html',
    styleUrls: ['./nhap-kho.component.css']
})
export class NhapKhoComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('deleteItem') deleteItem;
    @ViewChild('popUpMultiRecord') popUpMultiRecord: TemplateRef<any>;
    @ViewChild('popUpMultiDelete') popUpMultiDelete: TemplateRef<any>;
    currentAccount: any;
    ppOrder: RSInwardOutward[];
    ppOrderDetailsLoadDataClick: RSInwardOutWardDetails[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    searchValue: string;
    links: any;
    status: number;
    noType: number;
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
    reverse: any;
    accountingObjectEmployee: AccountingObjectDTO;
    selectedRow: any;
    isShowSearch: boolean;
    pageCount: any;
    accountingObjectName: string;
    rowNum: number;
    accountingObjectCode: string;
    searchData: any;
    dataSession: any;
    refVoucher: any;
    isErrorInvalid: any;
    account: any;
    isLoading: boolean;
    quantitySum: number;
    mainQuantitySum: number;
    totalAmount: number;
    index: number;
    isSoTaiChinh: number;
    NHAP_KHO = TypeID.NHAP_KHO;
    NHAP_KHO_TU_DIEU_CHINH = TypeID.NHAP_KHO_TU_DIEU_CHINH;
    NHAP_KHO_TU_MUA_HANG = TypeID.NHAP_KHO_TU_MUA_HANG;
    NHAP_KHO_TU_BAN_HANG_TRA_LAI = TypeID.NHAP_KHO_TU_BAN_HANG_TRA_LAI;
    checkModalRef: NgbModalRef;
    @ViewChild('refids') refids;
    private record_: Irecord;
    typeRecords: any[];
    statusRecords: any[];
    STATUS_RECORDED = 1;
    STATUS_NOT_RECORDED = 2;
    statusRecordHeader: any[];
    statusRecordHeaderValue = 3;
    noTypeHeaderValue = 4;
    noTypeHeader: any[];
    types: IType[];
    Type_group_inward = 40;
    ppInvoiceDetails: any[];
    ppInvoiceTax: boolean;
    saReturnTax: boolean;
    sumQuantity: number;
    sumUnitPriceOriginal: number;
    sumUnitPrice: number;
    sumMainQuantity: number;
    sumMainUnitPrice: number;
    sumAmountOriginal: number;
    sumFreightAmountOriginal: number;
    totalAmountOriginal: number;
    totalDiscountAmount: number;
    totalDiscountAmountOriginal: number;
    totalVATAmount: number;
    totalVATAmountOriginal: number;
    totalImportTaxAmount: number;
    totalImportTaxAmountOriginal: number;
    totalSpecialConsumeTaxAmount: number;
    totalSpecialConsumeTaxAmountOriginal: number;
    totalFreightAmount: number;
    totalFreightAmountOriginal: number;
    totalImportTaxExpenseAmount: number;
    totalImportTaxExpenseAmountOriginal: number;
    totalInwardAmount: number;
    totalInwardAmountOriginal: number;
    sumCustomUnitPrice: number;
    ppInvoiceDetailCost?: any[];
    currentBook: string;
    hiddenVAT: boolean;
    currencyCode: string;
    hangBanTraLaiDetails: any[];
    listVAT = [
        { name: '0%', data: 0 },
        { name: '5%', data: 1 },
        { name: '10%', data: 2 },
        { name: 'KCT', data: 3 },
        { name: 'KTT', data: 4 }
    ];

    ROLE_XEM = ROLE.NhapKho_XEM;
    ROLE_THEM = ROLE.NhapKho_THEM;
    ROLE_SUA = ROLE.NhapKho_SUA;
    ROLE_XOA = ROLE.NhapKho_XOA;
    ROLE_GHI_SO = ROLE.NhapKho_GHI_SO;
    ROLE_IN = ROLE.NhapKho_IN;
    ROLE_KETXUAT = ROLE.NhapKho_KET_XUAT;
    defaultRows: number;
    typeMultiAction: number;
    isFromSAReturn: boolean;
    isFromPPOrder: boolean;
    REPORT = REPORT;
    PPINVOICE_TYPE = PPINVOICE_TYPE;
    ppInvoice: PPInvoice;
    saReturn: any;
    disablePrint: boolean;

    constructor(
        public datepipe: DatePipe,
        private pPInvoiceService: PPInvoiceService,
        private accountingObjectService: AccountingObjectService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private materialGoodsService: MaterialGoodsService,
        private unitService: UnitService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private modalService: NgbModal,
        private saReturnService: SaReturnService,
        private gLService: GeneralLedgerService,
        private rsInwardOutwardService: RSInwardOutwardService,
        private eventManager: JhiEventManager,
        public utilsService: UtilsService,
        private refModalService: RefModalService,
        private toastr: ToastrService,
        private translate: TranslateService,
        public activeModal: NgbActiveModal,
        private typeService: TypeService,
        private saReturnDetailService: SaReturnDetailsService
    ) {
        super();
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.defaultRows = DEFAULT_ROWS;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body
                .filter(type => type.typeGroupID === this.Type_group_inward)
                .sort((a, b) => a.typeName.localeCompare(b.typeName));
        });
    }

    ngOnInit() {
        this.hangBanTraLaiDetails = [];
        this.ppInvoiceDetails = [];
        this.ppInvoiceDetailCost = [];
        this.typeMultiAction = 1;
        this.ppOrderDetailsLoadDataClick = [];
        this.refVoucher = [];
        this.accountingObjects = [];
        this.isShowSearch = false;
        this.registerChangeInPPOrder();
        this.registerExport();
        this.registerChangeSession();
        this.registerLockSuccess();
        this.registerUnlockSuccess();

        this.accountingObjectService.getAccountingObjectsActive().subscribe((res: HttpResponse<AccountingObjectDTO[]>) => {
            this.accountingObjects = res.body;
            this.getSessionData();
        });

        this.principal.identity().then(account => {
            this.account = account;
            this.currencyCode = account.organizationUnit.currencyID;
            this.color = this.account.systemOption.find(item => item.code === MAU_BO_GHI_SO).data;
            this.isSoTaiChinh = this.account.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
            this.currentBook = this.account.systemOption.find(x => x.code === SO_LAM_VIEC).data;
            this.hiddenVAT = this.account.organizationUnit.taxCalculationMethod === 1;
        });

        this.translate
            .get([
                'ebwebApp.rSInwardOutward.noType400',
                'ebwebApp.rSInwardOutward.noType401',
                'ebwebApp.rSInwardOutward.noType402',
                'ebwebApp.rSInwardOutward.noType403',
                'ebwebApp.rSInwardOutward.statusRecorded',
                'ebwebApp.rSInwardOutward.statusNotRecorded',
                'ebwebApp.purchaseOrder.status',
                'ebwebApp.rSInwardOutward.noType'
            ])
            .subscribe(res => {
                this.typeRecords = [
                    { value: this.NHAP_KHO, name: res['ebwebApp.rSInwardOutward.noType400'] },
                    { value: this.NHAP_KHO_TU_DIEU_CHINH, name: res['ebwebApp.rSInwardOutward.noType401'] },
                    { value: this.NHAP_KHO_TU_MUA_HANG, name: res['ebwebApp.rSInwardOutward.noType402'] },
                    { value: this.NHAP_KHO_TU_BAN_HANG_TRA_LAI, name: res['ebwebApp.rSInwardOutward.noType403'] }
                ];
                this.statusRecords = [
                    { value: this.STATUS_RECORDED, name: res['ebwebApp.rSInwardOutward.statusRecorded'] },
                    { value: this.STATUS_NOT_RECORDED, name: res['ebwebApp.rSInwardOutward.statusNotRecorded'] }
                ];
                this.statusRecordHeader = [{ value: this.statusRecordHeaderValue, name: res['ebwebApp.purchaseOrder.status'] }];
                this.noTypeHeader = [{ value: this.noTypeHeaderValue, name: res['ebwebApp.rSInwardOutward.noType'] }];
            });
    }

    loadAllForSearch() {
        if (this.checkToDateGreaterFromDate()) {
            this.page = 1;
            this.search();
        }
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

    registerChangeSession() {
        this.eventManager.subscribe('changeSession', response => {
            this.search();
        });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('nhapKhoSearchData'));
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
                this.accountingObject = this.searchData.accountingObject;
                this.searchValue = this.searchData.searchValue;
            }
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
            this.isShowSearch = this.dataSession.isShowSearch;
        }
        sessionStorage.removeItem('nhapKhoSearchData');
        this.transition(false);
    }

    resetSeach() {
        this.isErrorInvalid = false;
        this.accountingObject = {};
        this.status = null;
        this.noType = null;
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

    getVATRateValue(vatRate: number) {
        const vatRateValue = vatRate ? (vatRate === 0 ? 0 : vatRate === 1 ? 5 : 10) : null;
        if (vatRate != null) {
            return vatRateValue + '%';
        }
        return '';
    }

    changeAmountPPInvoice(ppInvoiceDetails) {
        // tiền hàng
        this.totalAmount = 0;
        this.totalAmountOriginal = 0;

        // tiền chiết khấu
        this.totalDiscountAmount = 0;
        this.totalDiscountAmountOriginal = 0;

        // thuế giá trị gia tăng
        this.totalVATAmount = 0;
        this.totalVATAmountOriginal = 0;

        // tiền thuế nhập khẩu
        this.totalImportTaxAmount = 0;
        this.totalImportTaxAmountOriginal = 0;

        // tiền thuế tài sản đảm bảo
        this.totalSpecialConsumeTaxAmount = 0;
        this.totalSpecialConsumeTaxAmountOriginal = 0;

        // tổng tiền thanh toán
        // this.amount = 0;
        // this.amountOriginal = 0;

        // chi phí mua
        this.totalFreightAmount = 0;
        this.totalFreightAmountOriginal = 0;

        // chi phí trước hải quan
        this.totalImportTaxExpenseAmount = 0;
        this.totalImportTaxExpenseAmountOriginal = 0;

        // giá nhập kho
        this.totalInwardAmount = 0;
        this.totalInwardAmountOriginal = 0;

        this.sumQuantity = 0;
        this.sumUnitPriceOriginal = 0;
        this.sumUnitPrice = 0;
        this.sumMainQuantity = 0;
        this.sumMainUnitPrice = 0;
        this.sumAmountOriginal = 0;
        this.sumFreightAmountOriginal = 0;

        // tổng giá tính thuế
        this.sumCustomUnitPrice = 0;

        for (const detail of ppInvoiceDetails) {
            detail.amount = detail.amount ? detail.amount : 0;
            detail.amountOriginal = detail.amountOriginal ? detail.amountOriginal : 0;
            detail.discountAmount = detail.discountAmount ? detail.discountAmount : 0;
            detail.discountAmountOriginal = detail.discountAmountOriginal ? detail.discountAmountOriginal : 0;
            detail.freightAmountOriginal = detail.freightAmountOriginal ? detail.freightAmountOriginal : 0;
            detail.vatAmount = detail.vatAmount ? detail.vatAmount : 0;
            detail.vatAmountOriginal = detail.vatAmountOriginal ? detail.vatAmountOriginal : 0;
            detail.importTaxAmount = detail.importTaxAmount ? detail.importTaxAmount : 0;
            detail.importTaxAmountOriginal = detail.importTaxAmountOriginal ? detail.importTaxAmountOriginal : 0;
            detail.specialConsumeTaxAmount = detail.specialConsumeTaxAmount ? detail.specialConsumeTaxAmount : 0;
            detail.specialConsumeTaxAmountOriginal = detail.specialConsumeTaxAmountOriginal ? detail.specialConsumeTaxAmountOriginal : 0;
            detail.freightAmount = detail.freightAmount ? detail.freightAmount : 0;
            detail.freightAmountOriginal = detail.freightAmountOriginal ? detail.freightAmountOriginal : 0;
            detail.importTaxExpenseAmount = detail.importTaxExpenseAmount ? detail.importTaxExpenseAmount : 0;
            detail.importTaxExpenseAmountOriginal = detail.importTaxExpenseAmountOriginal ? detail.importTaxExpenseAmountOriginal : 0;
            detail.inwardAmount = detail.inwardAmount ? detail.inwardAmount : 0;
            detail.inwardAmountOriginal = detail.inwardAmountOriginal ? detail.inwardAmountOriginal : 0;

            detail.quantity = detail.quantity ? detail.quantity : 0;
            detail.unitPriceOriginal = detail.unitPriceOriginal ? detail.unitPriceOriginal : 0;
            detail.unitPrice = detail.unitPrice ? detail.unitPrice : 0;
            detail.mainQuantity = detail.mainQuantity ? detail.mainQuantity : 0;
            detail.mainUnitPrice = detail.mainUnitPrice ? detail.mainUnitPrice : 0;
            detail.freightAmountOriginal = detail.freightAmountOriginal ? detail.freightAmountOriginal : 0;
            detail.customUnitPrice = detail.customUnitPrice ? detail.customUnitPrice : 0;

            this.sumQuantity += detail.quantity;
            this.sumUnitPriceOriginal += detail.unitPriceOriginal;
            this.sumUnitPrice += detail.unitPrice;
            this.sumMainQuantity += detail.mainQuantity;
            this.sumMainUnitPrice += detail.mainUnitPrice;

            // tiền hàng
            this.totalAmount += detail.amount;
            this.totalAmountOriginal += detail.amountOriginal;
            // tiền chiết khấu
            this.totalDiscountAmount += detail.discountAmount;
            this.totalDiscountAmountOriginal += detail.discountAmountOriginal;
            // thuế giá trị gia tăng
            this.totalVATAmount += detail.vatAmount;
            this.totalVATAmountOriginal += detail.vatAmountOriginal;
            // tiền thuế nhập khẩu
            this.totalImportTaxAmount += detail.importTaxAmount;
            this.totalImportTaxAmountOriginal += detail.importTaxAmountOriginal;
            // tiền thuế tài sản đảm bảo
            this.totalSpecialConsumeTaxAmount += detail.specialConsumeTaxAmount;
            this.totalSpecialConsumeTaxAmountOriginal += detail.specialConsumeTaxAmountOriginal;
            // chi phí mua
            this.totalFreightAmount += detail.freightAmount;
            this.totalFreightAmountOriginal += detail.freightAmountOriginal;
            // chi phí trước hải quan
            this.totalImportTaxExpenseAmount += detail.importTaxExpenseAmount;
            this.totalImportTaxExpenseAmountOriginal += detail.importTaxExpenseAmountOriginal;
            // giá nhập kho
            this.totalInwardAmount += detail.inwardAmount;
            // this.totalInwardAmountOriginal += detail.inwardAmountOriginal;
            this.totalInwardAmountOriginal += detail.inwardAmountOriginal;

            // chi phí mua
            this.sumFreightAmountOriginal += detail.freightAmountOriginal;
            // giá tính thuế
            this.sumCustomUnitPrice += detail.customUnitPrice;
        }
        // this.roundObject();
    }

    // roundObject() {
    //     this.utilsService.roundObjectsWithAccount(this.ppInvoiceDetails, this.account, this.currency.currencyCode);
    //     this.utilsService.roundObjectsWithAccount(this.ppInvoiceDetailCost, this.account, this.currency.currencyCode);
    //
    //     this.utilsService.roundObjectWithAccount(this.selectedRow, this.account, this.currency.currencyCode);
    // }

    getVATRate(vatRate) {
        if (this.listVAT && vatRate) {
            const acc = this.listVAT.find(n => n.data === vatRate);
            if (acc) {
                return acc.name;
            }
        }
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.hangBanTraLaiDetails.length; i++) {
            total += this.hangBanTraLaiDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    checkDisablePrint() {
        let nameAgeMapping = new Map();
        nameAgeMapping.set(this.selectedRows[0].typeID, this.selectedRows[0].typeID);
        for (let i = 0; i < this.selectedRows.length; i++) {
            if (!nameAgeMapping.has(this.selectedRows[i].typeID)) {
                this.disablePrint = true;
                break;
            }
        }
    }

    exportPdf(isDownload: boolean, typeReports: number) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.selectedRow.id,
                typeID: TypeID.NHAP_KHO,
                typeReport: typeReports
            },
            isDownload
        );
        switch (typeReports) {
            case REPORT.ChungTuKeToan:
                this.toastr.success(
                    this.translate.instant('ebwebApp.mBDeposit.printing') +
                        this.translate.instant('ebwebApp.mBDeposit.financialPaper') +
                        '...',
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
                break;
            case REPORT.PhieuNhapKho:
            case REPORT.PhieuNhapKhoA5:
                this.toastr.success(
                    this.translate.instant('ebwebApp.mBDeposit.printing') +
                        this.translate.instant('ebwebApp.pPInvoice.phieuNhapKho') +
                        '...',
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
        }
    }

    printPPI(isDownload, typeReports: number) {
        this.pPInvoiceService
            .getCustomerReport({
                id: this.ppInvoice.id,
                typeID: PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN,
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
                    const name = 'Bao_co.pdf';
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
        if (typeReports === PPINVOICE_TYPE.TYPE_REPORT_NHAP_KHO || typeReports === PPINVOICE_TYPE.TYPE_REPORT_NHAP_KHO_A5) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.pPInvoice.print.nhapKho') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === PPINVOICE_TYPE.TYPE_REPORT_CHUNG_TU) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') +
                    this.translate.instant('ebwebApp.pPInvoice.print.chungTuKeToan') +
                    '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === PPINVOICE_TYPE.TYPE_REPORT_CHUNG_TU_QD) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') +
                    this.translate.instant('ebwebApp.pPInvoice.print.chungTuKeToanQD') +
                    '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === PPINVOICE_TYPE.TYPE_REPORT_BAO_NO) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.pPInvoice.print.baoNo') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === PPINVOICE_TYPE.TYPE_REPORT_PHIEU_CHI) {
            this.toastr.success(
                this.translate.instant('ebwebApp.mBDeposit.printing') + this.translate.instant('ebwebApp.pPInvoice.print.phieuChi') + '...',
                this.translate.instant('ebwebApp.mBDeposit.message')
            );
        }
    }

    printfSaReturn(isDownload, typeReports: number) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.saReturn.saReturn.id,
                typeID: this.saReturn.saReturn.typeID,
                typeReport: typeReports
            },
            isDownload
        );
        this.toastr.success(this.translate.instant('ebwebApp.mBDeposit.printing'), this.translate.instant('ebwebApp.mBDeposit.message'));
    }

    onSelect(ppOrder) {
        this.selectedRow = ppOrder;
        this.quantitySum = 0;
        this.mainQuantitySum = 0;
        this.totalAmount = 0;
        for (let i = 0; i < this.ppOrder.length; i++) {
            if (this.ppOrder && this.ppOrder[i].id) {
                this.rsInwardOutwardService.getDetailsById(ppOrder.id, ppOrder.typeID).subscribe(res => {
                    this.ppOrderDetailsLoadDataClick = res.body.sort((a, b) => a.orderPriority - b.orderPriority);
                    if (this.selectedRow) {
                        for (const item of this.ppOrderDetailsLoadDataClick) {
                            if (item.saReturn != null) {
                                this.isFromSAReturn = true;
                                this.isFromPPOrder = false;
                                if (this.isSoTaiChinh) {
                                    item.bookSaReturn = item.saReturn.noFBook;
                                } else {
                                    item.bookSaReturn = item.saReturn.noMBook;
                                }
                            } else if (item.ppOrder != null) {
                                this.isFromSAReturn = false;
                                this.isFromPPOrder = true;
                                item.bookPPOrder = item.ppOrder.no;
                            } else {
                                this.isFromSAReturn = false;
                                this.isFromPPOrder = false;
                            }
                        }
                    }
                    if (ppOrder.typeID === this.NHAP_KHO) {
                        this.ppInvoiceTax = false;
                        this.saReturnTax = false;
                    } else {
                        if (ppOrder.refID) {
                            if (ppOrder.typeID === this.NHAP_KHO_TU_MUA_HANG) {
                                this.pPInvoiceService.findDetailById({ id: ppOrder.refID }).subscribe(response => {
                                    this.ppInvoiceDetails = response.body;
                                });
                                this.changeAmountPPInvoice(this.ppInvoiceDetails);
                                // phân bổ chi phí
                                this.pPInvoiceService
                                    .getPPInvoiceDetailCost({ refId: ppOrder.refID })
                                    .subscribe((response: HttpResponse<IPPInvoiceDetailCost[]>) => {
                                        this.ppInvoiceDetailCost = response.body;
                                    });
                                this.pPInvoiceService.find(ppOrder.refID).subscribe(response => {
                                    this.ppInvoice = response.body;
                                });
                                this.ppInvoiceTax = true;
                                this.saReturnTax = false;
                            } else if (ppOrder.typeID === this.NHAP_KHO_TU_BAN_HANG_TRA_LAI) {
                                this.saReturnDetailService.find(ppOrder.refID).subscribe(response => {
                                    this.hangBanTraLaiDetails = response.body.saReturnDetailsViewDTOs;
                                });
                                this.saReturnService.findById(ppOrder.refID).subscribe(response => {
                                    this.saReturn = response.body;
                                });
                                this.ppInvoiceTax = false;
                                this.saReturnTax = true;
                            } else if (ppOrder.typeID === this.NHAP_KHO_TU_DIEU_CHINH) {
                                // TODO: chuyển sang màn lập kho từ điều chỉnh
                                // this.router.navigate(['/hang-ban/tra-lai', res.body, 'rs-inward-outward']);
                            }
                        }
                    }
                    this.setSumOnClick();
                });
                this.rsInwardOutwardService.getRefVouchersByPPOrderID(ppOrder.id).subscribe(res => (this.refVoucher = res.body));
            }
        }
        this.disablePrint = false;
        if (this.selectedRows.length > 1) {
            this.checkDisablePrint();
        }
    }

    setSumOnClick() {
        this.quantitySum = 0;
        this.mainQuantitySum = 0;
        this.totalAmount = 0;
        for (const item of this.ppOrderDetailsLoadDataClick) {
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
            .searchAll({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: this.status || '',
                noType: this.noType || '',
                accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
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
        /*this.router.navigate(['/nhap-kho'], {
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
            '/nhap-kho',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.search();
    }

    doubleClickRow(ppOrder: RSInwardOutward, index?: number, isEdit?: boolean) {
        if (this.dataSession) {
            this.dataSession.isEdit = isEdit;
        }
        index = !index ? this.ppOrder.indexOf(ppOrder) : index;
        this.saveSearchData(ppOrder, index);
        if (ppOrder.typeID === this.NHAP_KHO) {
            this.router.navigate(['/nhap-kho', ppOrder.id, 'edit', this.rowNum]);
        } else {
            if (ppOrder.refID) {
                if (ppOrder.typeID === this.NHAP_KHO_TU_MUA_HANG) {
                    this.router.navigate(['/mua-hang/qua-kho', ppOrder.refID, 'edit-rs-inward']);
                } else if (ppOrder.typeID === this.NHAP_KHO_TU_BAN_HANG_TRA_LAI) {
                    this.router.navigate(['/hang-ban/tra-lai', ppOrder.refID, 'edit-rs-inward']);
                } else if (ppOrder.typeID === this.NHAP_KHO_TU_DIEU_CHINH) {
                    // TODO: chuyển sang màn lập kho từ điều chỉnh
                    // this.router.navigate(['/hang-ban/tra-lai', res.body, 'rs-inward-outward']);
                }
            }
        }
    }

    addNew($event = null) {
        this.saveSearchData(this.selectedRow, this.index - 1);
        this.router.navigate(['/nhap-kho/new']);
    }

    saveSearchData(ppOrrder: RSInwardOutward, index: number) {
        this.selectedRow = ppOrrder;
        this.searchData = {
            fromDate: this.fromDate || '',
            toDate: this.toDate || '',
            noType: this.noType ? this.noType : '',
            status: this.status === this.STATUS_RECORDED || this.status === this.STATUS_NOT_RECORDED ? this.status : '',
            accountingObject: this.accountingObject || '',
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
        sessionStorage.setItem('nhapKhoSearchData', JSON.stringify(this.dataSession));
        sessionStorage.setItem('nhapKhoDataSession', JSON.stringify(this.dataSession));
    }

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

    selectedItemPerPage() {
        this.search();
    }

    private paginatePPOrder(data: RSInwardOutward[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.ppOrder = data;
        this.objects = data;
        if (this.page > 1 && this.ppOrder && this.ppOrder.length === 0) {
            this.page = this.page - 1;
            this.loadAllForSearch();
            return;
        }
        if (this.rowNum && !this.index) {
            this.index = this.rowNum % this.itemsPerPage;
            this.index = this.index || this.itemsPerPage;
            this.selectedRow = this.ppOrder[this.index - 1];
        } else if (this.index) {
            this.selectedRow = this.ppOrder[this.index - 1];
        } else {
            this.selectedRow = this.ppOrder[0];
        }
        const lstSelect = this.selectedRows.map(object => ({ ...object }));
        this.selectedRows = [];
        this.selectedRows.push(...this.ppOrder.filter(n => lstSelect.some(m => m.id === n.id)));
        this.rowNum = this.getRowNumberOfRecord(this.page, 0);
        this.ppOrderDetailsLoadDataClick = [];
        this.onSelect(this.selectedRow || this.ppOrder[0] || null);
        this.pageCount = Math.ceil(this.totalItems / this.itemsPerPage);
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    record() {
        event.preventDefault();
        if (this.selectedRows && this.selectedRows.length > 1) {
            this.typeMultiAction = 0;
            this.checkModalRef = this.modalService.open(this.popUpMultiRecord, { backdrop: 'static' });
            return;
        } else {
            if (!this.checkCloseBook(this.account, this.selectedRow.postedDate)) {
                this.isLoading = true;
                this.record_ = {};
                if (this.selectedRow.typeID === this.NHAP_KHO) {
                    this.record_ = {
                        id: this.selectedRow.id,
                        typeID: this.selectedRow.typeID,
                        repositoryLedgerID: this.selectedRow.id
                    };
                } else if (this.selectedRow.typeID === this.NHAP_KHO_TU_MUA_HANG) {
                    this.record_ = { id: this.selectedRow.refID, typeID: PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN };
                } else if (this.selectedRow.typeID === this.NHAP_KHO_TU_BAN_HANG_TRA_LAI) {
                    this.record_ = {
                        id: this.selectedRow.refID,
                        typeID: TypeID.HANG_BAN_TRA_LAI,
                        repositoryLedgerID: this.selectedRow.id
                    };
                } else if (this.selectedRow.typeID === this.NHAP_KHO_TU_DIEU_CHINH) {
                    // TODO: nhap kho tu dieu chinh
                }
                this.gLService.record(this.record_).subscribe(
                    (res: HttpResponse<Irecord>) => {
                        if (res.body.success) {
                            this.toastr.success(
                                this.translate.instant('ebwebApp.mBTellerPaper.recordToast.done'),
                                this.translate.instant('ebwebApp.mBTellerPaper.message.title')
                            );
                            this.selectedRow.recorded = true;
                        } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_QT) {
                            this.toastr.error(
                                this.translate.instant('global.messages.error.checkTonQuyQT'),
                                this.translate.instant('ebwebApp.mCReceipt.error.error')
                            );
                        } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY_TC) {
                            this.toastr.error(
                                this.translate.instant('global.messages.error.checkTonQuyTC'),
                                this.translate.instant('ebwebApp.mCReceipt.error.error')
                            );
                        } else if (res.body.msg === MSGERROR.XUAT_QUA_TON_QUY) {
                            this.toastr.error(
                                this.translate.instant('global.messages.error.checkTonQuy'),
                                this.translate.instant('ebwebApp.mCReceipt.error.error')
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

    unrecord() {
        event.preventDefault();
        if (this.selectedRows && this.selectedRows.length > 1) {
            this.typeMultiAction = 1;
            this.checkModalRef = this.modalService.open(this.popUpMultiRecord, { backdrop: 'static' });
            return;
        } else {
            if (!this.checkCloseBook(this.account, this.selectedRow.postedDate)) {
                this.isLoading = true;
                this.record_ = {};
                if (this.selectedRow.typeID === this.NHAP_KHO) {
                    this.record_ = {
                        id: this.selectedRow.id,
                        typeID: this.selectedRow.typeID,
                        repositoryLedgerID: this.selectedRow.id
                    };
                } else if (this.selectedRow.typeID === this.NHAP_KHO_TU_MUA_HANG) {
                    this.record_ = { id: this.selectedRow.refID, typeID: PPINVOICE_TYPE.TYPE_ID_CHUA_THANH_TOAN };
                } else if (this.selectedRow.typeID === this.NHAP_KHO_TU_BAN_HANG_TRA_LAI) {
                    this.record_ = {
                        id: this.selectedRow.refID,
                        typeID: TypeID.HANG_BAN_TRA_LAI,
                        repositoryLedgerID: this.selectedRow.id
                    };
                } else if (this.selectedRow.typeID === this.NHAP_KHO_TU_DIEU_CHINH) {
                    // TODO: nhap kho tu dieu chinh
                }
                this.gLService.unrecord(this.record_).subscribe((res: HttpResponse<Irecord>) => {
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
                }, error => (this.isLoading = false));
            }
        }
    }

    exportExcel() {
        this.rsInwardOutwardService
            .export('excel', {
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: this.status === this.STATUS_RECORDED || this.status === this.STATUS_NOT_RECORDED ? this.status : '',
                noType: this.noType || '',
                accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
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

    export() {
        this.rsInwardOutwardService
            .export('pdf', {
                fromDate: this.fromDate || '',
                toDate: this.toDate || '',
                status: this.status === this.STATUS_RECORDED || this.status === this.STATUS_NOT_RECORDED ? this.status : '',
                noType: this.noType || '',
                accountingObject: this.accountingObject && this.accountingObject.id ? this.accountingObject.id : '',
                searchValue: this.searchValue || ''
            })
            .subscribe(res => {
                this.refModalService.open(null, EbReportPdfPopupComponent, res, false, this.NHAP_KHO);
            });
    }

    registerExport() {
        this.eventSubscriber = this.eventManager.subscribe(`export-excel-${this.NHAP_KHO}`, () => {
            this.exportExcel();
        });
    }

    edit() {
        event.preventDefault();
        if (!this.dataSession) {
            this.dataSession = new DataSessionStorage();
        }
        if (this.selectedRow.id) {
            if (!this.selectedRow.recorded) {
                this.doubleClickRow(this.selectedRow, this.ppOrder.indexOf(this.selectedRow), true);
            } else {
                this.dataSession.isEdit = false;
            }
        } else {
            this.dataSession.isEdit = false;
        }
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
            if (this.selectedRow.typeID === this.NHAP_KHO_TU_MUA_HANG) {
                this.pPInvoiceService.deleteById({ id: this.selectedRow.refID }).subscribe(
                    (res: HttpResponse<any>) => {
                        if (res.body.message === UpdateDataMessages.DELETE_SUCCESS) {
                            this.toastr.success(this.translate.instant('ebwebApp.pPInvoice.success.deleteSuccess'));
                            this.checkModalRef.close();
                            this.search();
                        } else if (res.body.message === UpdateDataMessages.PPINVOICE_USED) {
                            this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.ppInvoiceUsed'));
                        } else if (res.body.message === UpdateDataMessages.PPINVOICE_NOT_FOUND) {
                            this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.notFound'));
                        } else {
                            this.toastr.error(this.translate.instant('ebwebApp.pPInvoice.error.deleteError'));
                        }
                    },
                    (res: HttpErrorResponse) => {}
                );
            } else if (this.selectedRow.typeID === this.NHAP_KHO_TU_BAN_HANG_TRA_LAI) {
                this.saReturnService.delete(this.selectedRow.refID).subscribe(response => {
                    this.toastr.success(this.translate.instant('ebwebApp.saReturn.deleted'));
                    this.checkModalRef.close();
                    this.search();
                });
            } else if (this.selectedRow.typeID === this.NHAP_KHO) {
                this.rsInwardOutwardService.delete(this.selectedRow.id).subscribe(response => {
                    this.toastr.success(this.translate.instant('ebwebApp.saReturn.deleted'));
                    this.checkModalRef.close();
                    this.search();
                });
            } else if (this.selectedRow.typeID === this.NHAP_KHO_TU_DIEU_CHINH) {
                // TODO: nhap kho tu dieu chinh
            }
        }
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
            listRecord.typeIDMain = TypeID.NHAP_KHO;
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
                                    const rs = this.ppOrder.find(r => r.id === n.rSInwardOutwardID);
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
