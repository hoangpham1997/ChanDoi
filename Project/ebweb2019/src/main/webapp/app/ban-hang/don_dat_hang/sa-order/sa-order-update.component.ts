import { AfterViewChecked, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';

import { ISAOrder } from 'app/shared/model/sa-order.model';
import { SAOrderService } from './sa-order.service';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { ISAOrderDetails } from 'app/shared/model/sa-order-details.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { ICurrency } from 'app/shared/model/currency.model';
import { IMaterialGoods, MaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { CurrencyService } from 'app/danhmuc/currency';
import { Principal } from 'app/core';
import { IUnit } from 'app/shared/model/unit.model';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { SaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { SaleDiscountPolicyService } from 'app/entities/sale-discount-policy';
import { UnitService } from 'app/danhmuc/unit';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import * as moment from 'moment';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IMCPayment } from 'app/shared/model/mc-payment.model';
import { EbSaQuoteModalComponent } from 'app/shared/modal/sa-quote/sa-quote.component';
import { IViewSAQuoteDTO } from 'app/shared/model/view-sa-quote.model';
import { IMaterialGoodsConvertUnit } from 'app/shared/model/material-goods-convert-unit.model';
import { MaterialGoodsConvertUnitService } from 'app/entities/material-goods-convert-unit';
import { ROLE } from 'app/role.constants';
import { CategoryName, REPORT } from 'app/app.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-sa-order-update',
    templateUrl: './sa-order-update.component.html',
    styleUrls: ['./sa-order-update.component.css']
})
export class SAOrderUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewChecked {
    @ViewChild('content') public modalComponent: NgbModalRef;
    @ViewChild('contentUnRecord') public modalContentUnRecord: NgbModalRef;
    private TYPE_SA_ODER = 310;
    private TYPE_GROUPP_SA_ODER = 31;
    private _sAOrder: ISAOrder;
    isSaving: boolean;
    currentAccount: any;
    dateDp: any;
    deliverDateDp: any;
    statusVoucher: number;
    // region Tiến lùi chứng từ
    rowNum: number;
    count: number;
    searchVoucher: ISearchVoucher;
    // endregion
    accountingObjects: IAccountingObject[]; // Đối tượng
    employees: IAccountingObject[]; // Nhân viên
    saOrderDetails?: ISAOrderDetails[];
    currencies: ICurrency[]; //
    materialGoods: MaterialGoods[];
    currencyCode: string;
    currency?: ICurrency;
    saleDiscountPolicysDTO: SaleDiscountPolicy[];
    eventSubscriber: Subscription;
    modalRef: NgbModalRef;
    viewVouchersSelected: any;
    viewSAQuote: IViewSAQuoteDTO[];
    contextMenu: ContextMenu;

    sAOrderCopy: ISAOrder;
    saOrderDetailsCopy?: ISAOrderDetails[];
    viewVouchersSelectedCopy: any;
    isCloseAll: boolean;
    validateDate: boolean;
    dateStr: string;
    validateDeliverDate: boolean;
    deliverDateStr: string;
    isSaveAndNew: boolean;
    units: IUnit[];
    materialGoodsConvertUnits: IMaterialGoodsConvertUnit[];
    isViewFromRef: boolean;
    checkDiscount: boolean;
    listVAT: any[];
    reasonDefault = 'Đơn đặt hàng';
    accountingObjectNameOld: string;
    isMoreForm: boolean;
    /*Phân quyền*/
    ROLE_Them = ROLE.DonDatHang_Them;
    ROLE_Sua = ROLE.DonDatHang_Sua;
    ROLE_Xoa = ROLE.DonDatHang_Xoa;
    ROLE_KetXuat = ROLE.DonDatHang_KetXuat;
    REPORT = REPORT;
    ROLE = ROLE;

    CategoryName = CategoryName;
    taxCalculationMethod: any;
    select: any;
    indexFocusDetailCol: any;
    indexFocusDetailRow: any;
    listIDInputDeatil: any[] = [
        'materialGoods',
        'description',
        'unit',
        'quantity',
        'quantityDelivery',
        'unitPriceOriginal',
        'unitPrice',
        'mainConvertRate',
        'mainQuantity',
        'mainUnitPrice',
        'amountOriginal',
        'amount',
        'discountRate',
        'discountAmountOriginal',
        'discountAmount',
        'vATRate',
        'vATAmountOriginal',
        'vATAmount'
    ];

    constructor(
        private sAOrderService: SAOrderService,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        public utilsService: UtilsService,
        private router: Router,
        private jhiAlertService: JhiAlertService,
        private accountingObjectService: AccountingObjectService,
        private urrencyService: CurrencyService,
        private materialGoodsService: MaterialGoodsService,
        private toastrService: ToastrService,
        private translateService: TranslateService,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        private refModalService: RefModalService,
        private saleDiscountPolicyService: SaleDiscountPolicyService,
        private unitService: UnitService,
        private toastr: ToastrService,
        public translate: TranslateService,
        public materialGoodsConvertUnitService: MaterialGoodsConvertUnitService
    ) {
        super();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.taxCalculationMethod = account.organizationUnit.taxCalculationMethod;
        });
        this.searchVoucher = JSON.parse(sessionStorage.getItem('searchVoucherSAOrder'));
        this.accountingObjectService.getAllDTO().subscribe(
            (res: HttpResponse<IAccountingObject[]>) => {
                this.accountingObjects = res.body
                    .filter(n => n.objectType === 1 || n.objectType === 2)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
                this.employees = res.body
                    .filter(n => n.isEmployee)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.materialGoodsService.queryForComboboxGood().subscribe((res: HttpResponse<IMaterialGoods[]>) => {
            this.materialGoodss = res.body;
        });
        this.saleDiscountPolicyService.findAllSaleDiscountPolicyDTO().subscribe(res => {
            this.saleDiscountPolicysDTO = res.body;
        });
        this.unitService.convertRateForMaterialGoodsComboboxCustom().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
            this.materialGoodsConvertUnitService
                .getAllMaterialGoodsConvertUnits()
                .subscribe((resC: HttpResponse<IMaterialGoodsConvertUnit[]>) => {
                    this.materialGoodsConvertUnits = resC.body;
                    this.LoadDefaultEdit(this.saOrderDetails);
                });
        });
        this.viewVouchersSelected = [];
        this.contextMenu = new ContextMenu();
        this.isViewFromRef = window.location.href.includes('/from-ref');
        this.listVAT = [
            { name: '0%', data: 0 },
            { name: '5%', data: 1 },
            { name: '10%', data: 2 },
            { name: 'KCT', data: 3 },
            { name: 'KTT', data: 4 }
        ];
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ sAOrder }) => {
            this.sAOrder = sAOrder;
            this.viewVouchersSelected = sAOrder.viewVouchers ? sAOrder.viewVouchers : [];
            this.saOrderDetails = this.sAOrder.saOrderDetails
                ? this.sAOrder.saOrderDetails.sort((a, b) => a.orderPriority - b.orderPriority)
                : [];
            if (this.sAOrder.id) {
                this.statusVoucher = 1;
                this.accountingObjectNameOld = this.sAOrder.accountingObjectName ? this.sAOrder.accountingObjectName : '';
                this.isEdit = false;
                this.changeDate();
                this.changeDeliverDate();
                if (this.currentAccount) {
                    this.currencyCode = this.currentAccount.organizationUnit.currencyID;
                }
                this.urrencyService.findAllActive().subscribe(res => {
                    this.currencies = res.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                    this.currency = this.currencies.find(n => n.currencyCode === this.sAOrder.currencyID);
                });
                if (this.sAOrder.totalAmount) {
                    this.sAOrder.total = this.sAOrder.totalAmount + (this.sAOrder.totalVATAmount ? this.sAOrder.totalVATAmount : 0);
                }
                if (this.sAOrder.totalAmountOriginal) {
                    this.sAOrder.totalOriginal =
                        this.sAOrder.totalAmountOriginal + (this.sAOrder.totalVATAmountOriginal ? this.sAOrder.totalVATAmountOriginal : 0);
                }
                this.utilsService
                    .getIndexRow({
                        id: this.sAOrder.id,
                        isNext: true,
                        typeID: this.TYPE_SA_ODER,
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
            } else {
                this.statusVoucher = 0;
                this.isEdit = true;
                this.viewSAQuote = [];
                this.sAOrder.reason = this.reasonDefault;
                this.accountingObjectNameOld = this.sAOrder.accountingObjectName ? this.sAOrder.accountingObjectName : '';
                if (this.currentAccount) {
                    this.sAOrder.date = this.utilsService.ngayHachToan(this.currentAccount);
                    this.changeDate();
                    this.currencyCode = this.currentAccount.organizationUnit.currencyID;
                    this.sAOrder.currencyID = this.currencyCode;
                }
                this.utilsService
                    .getGenCodeVoucher({
                        typeGroupID: this.TYPE_GROUPP_SA_ODER,
                        displayOnBook: 2
                    })
                    .subscribe((res: HttpResponse<string>) => {
                        // this.mCReceipt.noFBook = (res.body.toString());
                        console.log(res.body);
                        this.sAOrder.no = res.body;
                        this.saOrderDetails = [];
                        this.valueDefaut();
                        this.sAOrder.status = 0;
                        this.urrencyService.findAllActive().subscribe(resC => {
                            this.currencies = resC.body.sort((a, b) => a.currencyCode.localeCompare(b.currencyCode));
                            this.selectCurrency();
                            this.copy();
                        });
                    });
            }
        });
        this.registerRef();
        this.registerAddNewRow();
        this.registerDeleteRow();
        this.registerCopyRow();
        this.registerViewSAQuote();
        this.registerCombobox();
    }

    closeForm() {
        if (this.sAOrderCopy && (this.statusVoucher === 0 || this.statusVoucher === 1)) {
            if (
                !this.utilsService.isEquivalent(this.sAOrder, this.sAOrderCopy) ||
                !this.utilsService.isEquivalentArray(this.saOrderDetails, this.saOrderDetailsCopy) ||
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
            if (sessionStorage.getItem('page_saorder')) {
                this.router.navigate(['/sa-order', 'hasSearch', '1'], {
                    queryParams: {
                        page: sessionStorage.getItem('page_saorder'),
                        size: sessionStorage.getItem('size_saorder'),
                        index: sessionStorage.getItem('index_saorder')
                    }
                });
            } else {
                this.router.navigate(['/sa-order', 'hasSearch', '1']);
            }
        } else {
            if (sessionStorage.getItem('page_saorder')) {
                this.router.navigate(['/sa-order'], {
                    queryParams: {
                        page: sessionStorage.getItem('page_saorder'),
                        size: sessionStorage.getItem('size_saorder'),
                        index: sessionStorage.getItem('index_saorder')
                    }
                });
            } else {
                this.router.navigate(['/sa-order']);
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DonDatHang_Them, ROLE.DonDatHang_Sua])
    save() {
        event.preventDefault();
        this.fillToSave();
        if (this.checkError() && !this.utilsService.isShowPopup) {
            this.isSaving = true;
            if (this.sAOrder.id !== undefined) {
                this.subscribeToSaveResponse(this.sAOrderService.update(this.sAOrder));
            } else {
                this.subscribeToSaveResponse(this.sAOrderService.create(this.sAOrder));
            }
        }
    }

    fillToSave() {
        this.sAOrder.typeID = this.TYPE_SA_ODER;
        this.sAOrder.saOrderDetails = this.saOrderDetails;
        this.sAOrder.viewVouchers = this.viewVouchersSelected;
        this.isSaving = true;
        for (let i = 0; i < this.sAOrder.saOrderDetails.length; i++) {
            this.sAOrder.saOrderDetails[i].orderPriority = i + 1;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DonDatHang_Them])
    saveAndNew() {
        event.preventDefault();
        this.fillToSave();
        if (this.checkError() && !this.utilsService.isShowPopup) {
            if (this.sAOrder.id !== undefined) {
                this.subscribeToSaveResponseAndContinue(this.sAOrderService.update(this.sAOrder));
            } else {
                this.subscribeToSaveResponseAndContinue(this.sAOrderService.create(this.sAOrder));
            }
            this.statusVoucher = 0;
            this.isEdit = true;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DonDatHang_Them])
    copyAndNew() {
        if (!this.utilsService.isShowPopup) {
            this.sAOrder.id = undefined;
            for (let i = 0; i < this.saOrderDetails.length; i++) {
                this.saOrderDetails[i].id = undefined;
            }
            this.utilsService
                .getGenCodeVoucher({
                    typeGroupID: this.TYPE_GROUPP_SA_ODER,
                    displayOnBook: 2
                })
                .subscribe((res: HttpResponse<string>) => {
                    console.log(res.body);
                    this.sAOrder.no = res.body;
                });
            this.statusVoucher = 0;
            this.isEdit = true;
            this.sAOrderCopy = {};
        }
    }

    checkError() {
        if (this.sAOrder.companyTaxCode) {
            if (!this.utilsService.checkMST(this.sAOrder.companyTaxCode)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.taxCodeInvalid'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        if (this.sAOrder.employeeID) {
            if (!this.employees.find(n => n.id === this.sAOrder.employeeID)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mCReceipt.error.employeeIDNotExist'),
                    this.translate.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        if (this.sAOrder.no) {
            if (!this.utilsService.checkNoBook(this.sAOrder.no, this.currentAccount)) {
                return false;
            }
        } else {
            this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        if (!this.sAOrder.date) {
            this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }
        if (!this.sAOrder.currencyID) {
            this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
            return false;
        }

        if (this.saOrderDetails.length === 0) {
            // Null phần chi tiết
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullDetail'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        } else {
            for (let i = 0; i < this.saOrderDetails.length; i++) {
                if (!this.saOrderDetails[i].materialGoods) {
                    this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
                    return false;
                }
                if (this.saOrderDetails[i].discountRate > 100) {
                    this.toastr.error('Tỷ lệ chiết khấu không được lớn hơn 100%', this.translate.instant('ebwebApp.mCReceipt.error.error'));
                    return false;
                }
            }
        }
        return true;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DonDatHang_Sua])
    edit() {
        event.preventDefault();
        if (!this.utilsService.isShowPopup) {
            this.sAOrderService.checkRelateVoucher({ id: this.sAOrder.id }).subscribe((response: HttpResponse<boolean>) => {
                if (response.body) {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    this.modalRef = this.modalService.open(this.modalContentUnRecord, { backdrop: 'static' });
                } else {
                    this.statusVoucher = 0;
                    this.isEdit = true;
                    this.copy();
                }
            });
        }
    }

    deleteRef() {
        if (this.modalRef) {
            this.modalRef.close();
        }

        this.sAOrderService.deleteRef(this.sAOrder.id).subscribe(
            (res: HttpResponse<boolean>) => {
                if (res) {
                    this.deleteRefSuccess();
                } else {
                    this.deleteRefFail();
                }
            },
            (res: HttpErrorResponse) => {
                this.toastr.error(res.message);
            }
        );
    }

    deleteRefSuccess() {
        this.toastr.success(
            this.translate.instant('ebwebApp.sAOrder.delete.deletedRef'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
    }

    deleteRefFail() {
        this.toastr.error(
            this.translate.instant('ebwebApp.sAOrder.delete.deletedRefFail'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DonDatHang_Them])
    addNew($event = null) {
        if (!this.utilsService.isShowPopup) {
            this.router.navigate(['/sa-order', 'new']);
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISAOrder>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.sAOrder.id = res.body.sAOrder.id;
                    this.sAOrderCopy = null;
                    this.router.navigate(['/sa-order', res.body.sAOrder.id, 'edit']);
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private subscribeToSaveResponseAndContinue(result: Observable<HttpResponse<any>>) {
        this.isSaveAndNew = true;
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.sAOrder.id = res.body.sAOrder.id;
                    this.router.navigate(['/sa-order', res.body.sAOrder.id, 'edit']).then(() => {
                        this.router.navigate(['/sa-order', 'new']);
                        this.isSaveAndNew = false;
                    });
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private subscribeToSaveResponseWhenClose(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.closeAll();
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private noVoucherExist() {
        this.toastr.error(
            this.translate.instant('global.data.noVocherAlreadyExist'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.statusVoucher = 1;
        this.isEdit = false;
        this.toastr.success(
            this.translate.instant('ebwebApp.mCReceipt.home.saveSuccess'),
            this.translate.instant('ebwebApp.mCReceipt.home.message')
        );
    }

    private onSaveError() {
        this.isSaving = false;
    }

    get sAOrder() {
        return this._sAOrder;
    }

    set sAOrder(sAOrder: ISAOrder) {
        this._sAOrder = sAOrder;
    }

    // region Tiến lùi chứng từ
    // ham lui, tien
    previousEdit() {
        // goi service get by row num
        if (this.rowNum !== this.count) {
            this.utilsService
                .findByRowNum({
                    id: this.sAOrder.id,
                    isNext: false,
                    typeID: this.TYPE_SA_ODER,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMCPayment>) => {
                        this.router.navigate(['/sa-order', res.body.id, 'edit']);
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
                    id: this.sAOrder.id,
                    isNext: true,
                    typeID: this.TYPE_SA_ODER,
                    searchVoucher: this.searchVoucher === undefined ? null : JSON.stringify(this.searchVoucher)
                })
                .subscribe(
                    (res: HttpResponse<IMCPayment>) => {
                        this.router.navigate(['/sa-order', res.body.id, 'edit']);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    // endregion

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    exportPdf(isDownload, typeReport) {
        this.utilsService.getCustomerReportPDF({
            id: this.sAOrder.id,
            typeID: this.sAOrder.typeID,
            typeReport
        });
    }

    getUnitPriceOriginalsByID(id) {
        const lst = [];
        if (this.materialGoodss) {
            const mg: MaterialGoods = this.materialGoodss.find(n => n.id === id);
            if (mg) {
                if (mg.fixedSalePrice) {
                    lst.push({ unitPriceOriginal: mg.fixedSalePrice });
                }
                if (mg.salePrice1) {
                    lst.push({ unitPriceOriginal: mg.salePrice1 });
                }
                if (mg.salePrice2) {
                    lst.push({ unitPriceOriginal: mg.salePrice2 });
                }
                if (mg.salePrice3) {
                    lst.push({ unitPriceOriginal: mg.salePrice3 });
                }
            }
        }
        return lst;
    }

    getUnitPriceOriginals(mg: MaterialGoods) {
        const lst = [];
        if (mg) {
            if (mg.fixedSalePrice) {
                lst.push({ unitPriceOriginal: mg.fixedSalePrice });
            }
            if (mg.salePrice1) {
                lst.push({ unitPriceOriginal: mg.salePrice1 });
            }
            if (mg.salePrice2) {
                lst.push({ unitPriceOriginal: mg.salePrice2 });
            }
            if (mg.salePrice3) {
                lst.push({ unitPriceOriginal: mg.salePrice3 });
            }
        }
        return lst;
    }

    getUnitPriceOriginalsWithUnitConvert(unitID, materalGoods: IMaterialGoods) {
        const lst = [];
        let mg;
        if (unitID === materalGoods.unitID) {
            mg = materalGoods;
        } else {
            mg = this.materialGoodsConvertUnits.find(n => n.materialGoodsID === materalGoods.id && n.unitID === unitID);
        }
        if (mg) {
            if (mg.fixedSalePrice) {
                lst.push({ unitPriceOriginal: mg.fixedSalePrice });
            }
            if (mg.salePrice1) {
                lst.push({ unitPriceOriginal: mg.salePrice1 });
            }
            if (mg.salePrice2) {
                lst.push({ unitPriceOriginal: mg.salePrice2 });
            }
            if (mg.salePrice3) {
                lst.push({ unitPriceOriginal: mg.salePrice3 });
            }
        }
        return lst;
    }

    selectAccountingObject() {
        if (this.sAOrder.accountingObjectID !== null && this.sAOrder.accountingObjectID !== undefined) {
            const acc: IAccountingObject = this.accountingObjects.find(n => n.id === this.sAOrder.accountingObjectID);
            this.sAOrder.accountingObjectName = acc.accountingObjectName;
            if (
                this.sAOrder.reason === this.reasonDefault ||
                this.sAOrder.reason === this.reasonDefault + ' của ' + this.accountingObjectNameOld
            ) {
                this.sAOrder.reason = this.reasonDefault + ' của ' + this.sAOrder.accountingObjectName;
            }
            this.accountingObjectNameOld = acc.accountingObjectName;
            this.sAOrder.accountingObjectAddress = acc.accountingObjectAddress;
            this.sAOrder.companyTaxCode = acc.taxCode;
            this.sAOrder.contactName = acc.contactName;
        } else {
            this.sAOrder.accountingObjectAddress = null;
            this.sAOrder.companyTaxCode = null;
            this.sAOrder.contactName = null;
            this.sAOrder.accountingObjectName = null;
            this.sAOrder.reason = this.reasonDefault;
        }
    }

    selectCurrency() {
        if (this.sAOrder.currencyID) {
            this.currency = this.currencies.find(n => n.currencyCode === this.sAOrder.currencyID);
            this.sAOrder.exchangeRate = this.currency.exchangeRate;
        }
        this.changeExchangeRate();
    }

    KeyPress(value: number, select: number) {
        if (select === 0) {
            this.saOrderDetails.splice(value, 1);
        } else if (select === 1) {
        }
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    AddnewRow(select: number, isRightClick?) {
        if (this.sAOrder.totalAmount === undefined) {
            this.sAOrder.totalAmount = 0;
        }
        if (this.sAOrder.totalAmountOriginal === undefined) {
            this.sAOrder.totalAmountOriginal = 0;
        }
        if (select === 0) {
            let lenght = 0;
            if (isRightClick) {
                this.saOrderDetails.splice(this.indexFocusDetailRow + 1, 0, {});
                lenght = this.indexFocusDetailRow + 2;
            } else {
                this.saOrderDetails.push({});
                lenght = this.saOrderDetails.length;
            }
            this.saOrderDetails[lenght - 1].amountOriginal = 0;
            this.saOrderDetails[lenght - 1].amount = 0;
            this.saOrderDetails[lenght - 1].unitPrice = 0;
            this.saOrderDetails[lenght - 1].unitPriceOriginal = 0;
            this.saOrderDetails[lenght - 1].amount = 0;
            this.saOrderDetails[lenght - 1].discountAmount = 0;
            this.saOrderDetails[lenght - 1].discountAmountOriginal = 0;
            this.saOrderDetails[lenght - 1].vATAmountOriginal = 0;
            this.saOrderDetails[lenght - 1].vATAmount = 0;
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
                const idx: number = this.saOrderDetails.length - 1;
                const nameTagIndex: string = nameTag + String(idx);
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(nameTagIndex);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
        } else if (select === 1) {
        } else {
        }
    }

    LoadDefaultEdit(detail: ISAOrderDetails[]) {
        for (let i = 0; i < detail.length; i++) {
            detail[i].units = this.getUintFromMaterialgoodsByID(detail[i].materialGoods.id);
            if (detail[i].unit && detail[i].unit.id) {
                detail[i].unit = this.units.find(n => n.id === detail[i].unit.id);
            }

            if (detail[i].unit.id !== detail[i].mainUnit.id) {
                detail[i].unitPriceOriginals = this.getUnitPriceOriginalsWithUnitConvert(detail[i].unit.id, detail[i].materialGoods);
            } else {
                detail[i].unitPriceOriginals = this.getUnitPriceOriginals(detail[i].materialGoods);
            }
            detail[i].saleDiscountPolicysDTO = this.saleDiscountPolicysDTO.filter(n => n.materialGoodsID === detail[i].materialGoods.id);
        }
    }

    selectedMaterialGoods(index) {
        /*this.saleDiscountPolicyService.findByMaterialGoodsID(this.saOrderDetails[index].materialGoods.id).subscribe(res => {
            this.saleDiscountPolicys = res.body;
            this.checkSaleDiscountPolicy(index);
        });*/

        if (!this.saOrderDetails[index].materialGoods) {
            return;
        }
        this.saOrderDetails[index].saleDiscountPolicysDTO = this.saleDiscountPolicysDTO.filter(
            n => n.materialGoodsID === this.saOrderDetails[index].materialGoods.id
        );
        this.checkSaleDiscountPolicyDTO(index);
        // this.saOrderDetails[index].materialGoods = this.materialGood;
        this.saOrderDetails[index].description = this.saOrderDetails[index].materialGoods.materialGoodsName;
        // this.unitPriceOriginal = [];
        if (this.saOrderDetails[index].materialGoods.fixedSalePrice) {
            this.saOrderDetails[index].unitPriceOriginal = this.round(
                this.saOrderDetails[index].materialGoods.fixedSalePrice,
                this.getUnitPriceOriginalType()
            );
            this.saOrderDetails[index].unitPrice = this.round(
                this.saOrderDetails[index].materialGoods.fixedSalePrice *
                    (this.currency.formula.includes('*') ? this.sAOrder.exchangeRate : 1 / this.sAOrder.exchangeRate),
                1
            );
        }
        if (this.taxCalculationMethod !== 1) {
            if (this.saOrderDetails[index].materialGoods.vatTaxRate) {
                this.saOrderDetails[index].vATRate = this.saOrderDetails[index].materialGoods.vatTaxRate;
            }
        }
        /* this.unitService
             .convertRateForMaterialGoodsComboboxCustom({materialGoodsId: this.saOrderDetails[index].materialGoods.id})
             .subscribe((res: HttpResponse<IUnit[]>) => {
                 this.saOrderDetails[index].units = res.body;
                 this.saOrderDetails[index].unit = this.saOrderDetails[index].units[0];
                 this.saOrderDetails[index].mainUnit = this.saOrderDetails[index].units[0];
                 this.selectUnit(index);
             });*/
        this.saOrderDetails[index].unitPriceOriginals = this.getUnitPriceOriginals(this.saOrderDetails[index].materialGoods);
        this.saOrderDetails[index].units = this.getUintFromMaterialgoodsByID(this.saOrderDetails[index].materialGoods.id);
        const unitM = this.saOrderDetails[index].units.find(n => n.id === this.saOrderDetails[index].materialGoods.unitID);
        if (unitM) {
            this.saOrderDetails[index].unit = unitM;
            this.saOrderDetails[index].mainUnit = unitM;
            this.selectUnit(index);
        } else {
            this.saOrderDetails[index].mainConvertRate = 1;
            this.saOrderDetails[index].formula = '*';
            this.saOrderDetails[index].mainQuantity = this.saOrderDetails[index].quantity;
            this.saOrderDetails[index].mainUnitPrice = this.saOrderDetails[index].unitPriceOriginal;
        }
    }

    selectUnitPriceOriginal(index) {
        this.calculateUnitPrice(index);
        this.calculateMainUnitPrice(index);
        this.changeAmountOriginal(index, false, true);
    }

    selectUnit(index) {
        if (this.saOrderDetails[index].unit && this.saOrderDetails[index].mainUnit) {
            this.saOrderDetails[index].unitPriceOriginals = this.getUnitPriceOriginalsWithUnitConvert(
                this.saOrderDetails[index].unit.id,
                this.saOrderDetails[index].materialGoods
            );
            if (this.saOrderDetails[index].unitPriceOriginals.length > 0) {
                this.saOrderDetails[index].unitPriceOriginal = this.round(
                    this.saOrderDetails[index].unitPriceOriginals[0].unitPriceOriginal,
                    this.getUnitPriceOriginalType()
                );
                this.saOrderDetails[index].unitPrice = this.round(
                    this.saOrderDetails[index].unitPriceOriginals[0].unitPriceOriginal *
                        (this.currency.formula.includes('*') ? this.sAOrder.exchangeRate : 1 / this.sAOrder.exchangeRate),
                    1
                );
            }
            if (this.saOrderDetails[index].unit.id === this.saOrderDetails[index].mainUnit.id) {
                this.saOrderDetails[index].mainConvertRate = 1;
                this.saOrderDetails[index].formula = '*';
                this.saOrderDetails[index].mainQuantity = this.round(this.saOrderDetails[index].quantity, 3);
                this.saOrderDetails[index].mainUnitPrice = this.round(this.saOrderDetails[index].unitPriceOriginal, 1);
            } else {
                this.saOrderDetails[index].mainConvertRate = this.round(this.saOrderDetails[index].unit.convertRate, 5);
                this.saOrderDetails[index].formula = this.saOrderDetails[index].unit.formula;
                if (this.saOrderDetails[index].formula === '*' && this.saOrderDetails[index].quantity) {
                    this.saOrderDetails[index].mainQuantity = this.round(
                        this.saOrderDetails[index].unit.convertRate * this.saOrderDetails[index].quantity,
                        3
                    );
                    this.saOrderDetails[index].mainUnitPrice = this.round(
                        this.saOrderDetails[index].unitPriceOriginal / this.saOrderDetails[index].mainConvertRate,
                        1
                    );
                } else if (this.saOrderDetails[index].formula === '/') {
                    this.saOrderDetails[index].mainQuantity = this.round(
                        this.saOrderDetails[index].unit.convertRate * this.saOrderDetails[index].quantity,
                        3
                    );
                    this.saOrderDetails[index].mainUnitPrice = this.round(
                        this.saOrderDetails[index].unitPriceOriginal * this.saOrderDetails[index].mainConvertRate,
                        1
                    );
                }
            }
            // this.changeQuantity(index);
            this.selectUnitPriceOriginal(index);
        }
    }

    changeExchangeRate() {
        if (this.sAOrder.currencyID) {
            // this.sAOrder.currencyID = this.sAOrder.currencyCode;
            this.sAOrder.exchangeRate = this.round(this.sAOrder.exchangeRate, 4);
            this.saOrderDetails.forEach(item => {
                if (item.unitPriceOriginal || item.unitPriceOriginal === 0) {
                    item.unitPrice = this.round(
                        item.unitPriceOriginal *
                            (this.currency.formula.includes('*') ? this.sAOrder.exchangeRate : 1 / this.sAOrder.exchangeRate),
                        1
                    );
                }
                if (item.amountOriginal || item.amountOriginal === 0) {
                    item.amount = this.round(
                        item.amountOriginal *
                            (this.currency.formula.includes('*') ? this.sAOrder.exchangeRate : 1 / this.sAOrder.exchangeRate),
                        7
                    );
                }
                if (item.discountAmountOriginal || item.discountAmountOriginal === 0) {
                    item.discountAmount = this.round(
                        item.discountAmountOriginal *
                            (this.currency.formula.includes('*') ? this.sAOrder.exchangeRate : 1 / this.sAOrder.exchangeRate),
                        7
                    );
                }
                if (item.vATAmountOriginal || item.vATAmountOriginal === 0) {
                    item.vATAmount = this.round(
                        item.vATAmountOriginal *
                            (this.currency.formula.includes('*') ? this.sAOrder.exchangeRate : 1 / this.sAOrder.exchangeRate),
                        7
                    );
                }
            });
            this.updatesAOrder();
        }
    }

    changeQuantity(index) {
        this.calculateMainQuantity(index);
        this.changeAmountOriginal(index);
        this.checkSaleDiscountPolicyDTO(index);
    }

    calculateAmount(index) {
        if (this.sAOrder.exchangeRate) {
            this.saOrderDetails[index].amount = this.round(
                this.saOrderDetails[index].amountOriginal *
                    (this.currency.formula.includes('*') ? this.sAOrder.exchangeRate : 1 / this.sAOrder.exchangeRate),
                7
            );
        }
    }

    calculateAmountOriginal(index) {
        if (
            (this.saOrderDetails[index].quantity || this.saOrderDetails[index].quantity === 0) &&
            (this.saOrderDetails[index].unitPriceOriginal || this.saOrderDetails[index].unitPriceOriginal === 0)
        ) {
            this.saOrderDetails[index].amountOriginal = this.round(
                this.saOrderDetails[index].quantity * this.saOrderDetails[index].unitPriceOriginal,
                this.getAmountOriginalType()
            );
        }
    }

    changeAmountOriginal(index, isSelfChange = false, isUnitPriceChange = false) {
        if (!isSelfChange) {
            this.calculateAmountOriginal(index);
        } else {
            this.calculateUnitPriceOriginalByAmountOriginal(index);
            this.calculateUnitPrice(index);
            this.calculateMainUnitPrice(index);
        }
        if (!isUnitPriceChange) {
            this.calculateUnitPriceOriginal(index);
        }
        this.calculateUnitPrice(index);
        this.calculateAmount(index);
        this.calculateDiscountAmountOriginal(index);
        this.calculateDiscountAmount(index);
        this.calculateVatAmountOriginal(index);
        this.calculateVatAmount(index);
        this.updatesAOrder();
    }

    changeVatAmountOriginal(index) {
        this.calculateVatAmount(index);
    }

    calculateDiscountAmount(index) {
        if (
            (this.saOrderDetails[index].discountAmountOriginal || this.saOrderDetails[index].discountAmountOriginal === 0) &&
            (this.sAOrder.exchangeRate || this.sAOrder.exchangeRate === 0)
        ) {
            this.saOrderDetails[index].discountAmount = this.round(
                this.saOrderDetails[index].discountAmountOriginal *
                    (this.currency.formula.includes('*') ? this.sAOrder.exchangeRate : 1 / this.sAOrder.exchangeRate),
                7
            );
        }
    }

    calculateDiscountAmountOriginal(index) {
        if (
            (this.saOrderDetails[index].amountOriginal || this.saOrderDetails[index].amountOriginal === 0) &&
            (this.saOrderDetails[index].discountRate || this.saOrderDetails[index].discountRate === 0)
        ) {
            this.saOrderDetails[index].discountAmountOriginal = this.round(
                this.saOrderDetails[index].amountOriginal * this.saOrderDetails[index].discountRate / 100,
                this.getAmountOriginalType()
            );
        } else {
            this.saOrderDetails[index].discountAmountOriginal = 0;
            this.saOrderDetails[index].discountAmount = 0;
        }
    }

    changeDiscountRate(index, noCheck?) {
        if (this.saOrderDetails[index].discountRate > 100) {
            this.checkDiscount = true;
            if (this.checkDiscount) {
                this.toastr.warning('Tỷ lệ chiết khấu không được lớn hơn 100%', this.translate.instant('ebwebApp.mCReceipt.home.message'));
            }
        } else {
            this.checkDiscount = false;
        }
        if (
            (this.saOrderDetails[index].amountOriginal || this.saOrderDetails[index].amountOriginal === 0) &&
            (this.saOrderDetails[index].discountRate || this.saOrderDetails[index].discountRate === 0)
        ) {
            this.saOrderDetails[index].discountAmountOriginal = this.round(
                this.saOrderDetails[index].amountOriginal * this.saOrderDetails[index].discountRate / 100,
                this.getAmountOriginalType()
            );
            if (this.sAOrder.exchangeRate || this.sAOrder.exchangeRate === 0) {
                this.saOrderDetails[index].discountAmount = this.round(
                    this.saOrderDetails[index].discountAmountOriginal *
                        (this.currency.formula.includes('*') ? this.sAOrder.exchangeRate : 1 / this.sAOrder.exchangeRate),
                    7
                );
            }
        } else {
            this.saOrderDetails[index].discountAmountOriginal = 0;
            this.saOrderDetails[index].discountAmount = 0;
        }
        if (!noCheck) {
            this.checkSaleDiscountPolicyDTO(index);
        }
        this.changeVATRate(index);
        this.updatesAOrder();
    }

    changeDiscountAmount(index) {
        if (this.saOrderDetails[index].discountAmountOriginal || this.saOrderDetails[index].discountAmountOriginal === 0) {
            this.saOrderDetails[index].discountRate = this.round(
                this.saOrderDetails[index].discountAmountOriginal / this.saOrderDetails[index].amountOriginal * 100,
                5
            );
            if (this.sAOrder.exchangeRate) {
                this.saOrderDetails[index].discountAmount = this.round(
                    this.saOrderDetails[index].discountAmountOriginal *
                        (this.currency.formula.includes('*') ? this.sAOrder.exchangeRate : 1 / this.sAOrder.exchangeRate),
                    7
                );
            }
        }
        this.changeVATRate(index);
        this.updatesAOrder();
    }

    changeVATRate(index) {
        this.calculateVatAmountOriginal(index);
        this.calculateVatAmount(index);
        this.updatesAOrder();
    }

    calculateVatAmountOriginal(index) {
        if (
            (this.saOrderDetails[index].amountOriginal ||
                this.saOrderDetails[index].amountOriginal === 0 ||
                this.saOrderDetails[index].discountAmountOriginal ||
                this.saOrderDetails[index].discountAmountOriginal === 0) &&
            (this.saOrderDetails[index].vATRate === 1 || this.saOrderDetails[index].vATRate === 2)
        ) {
            if (!this.saOrderDetails[index].amountOriginal) {
                this.saOrderDetails[index].amountOriginal = 0;
            }
            if (!this.saOrderDetails[index].discountAmountOriginal) {
                this.saOrderDetails[index].discountAmountOriginal = 0;
            }
            this.saOrderDetails[index].vATAmountOriginal = this.round(
                (this.saOrderDetails[index].amountOriginal - this.saOrderDetails[index].discountAmountOriginal) *
                    (this.saOrderDetails[index].vATRate === 1 ? 0.05 : 0.1),
                this.getAmountOriginalType()
            );
            if (this.sAOrder.exchangeRate) {
                this.saOrderDetails[index].vATAmount = this.round(
                    this.saOrderDetails[index].vATAmountOriginal *
                        (this.currency.formula.includes('*') ? this.sAOrder.exchangeRate : 1 / this.sAOrder.exchangeRate),
                    7
                );
            }
        } else {
            this.saOrderDetails[index].vATAmountOriginal = 0;
            this.saOrderDetails[index].vATAmount = 0;
        }
    }

    calculateVatAmount(index) {
        if (this.sAOrder.exchangeRate) {
            this.saOrderDetails[index].vATAmount = this.round(
                this.saOrderDetails[index].vATAmountOriginal *
                    (this.currency.formula.includes('*') ? this.sAOrder.exchangeRate : 1 / this.sAOrder.exchangeRate),
                7
            );
        }
        this.updatesAOrder();
    }

    changeMainConvertRate(index) {
        this.calculateMainQuantity(index);
        this.calculateMainUnitPrice(index);
    }

    changeMainQuantity(index) {
        this.calculateQuantity(index);
        this.changeAmountOriginal(index);
    }

    /*changeMainUnitPrice(index) {
        this.calculateUnitPriceOriginal(index);
        this.calculateUnitPrice(index);
        this.changeAmountOriginal(index);
    }*/

    calculateQuantity(index) {
        if (this.saOrderDetails[index].mainQuantity) {
            this.saOrderDetails[index].quantity = this.round(
                this.saOrderDetails[index].formula === '*'
                    ? this.saOrderDetails[index].mainQuantity / this.saOrderDetails[index].mainConvertRate
                    : this.saOrderDetails[index].mainQuantity * this.saOrderDetails[index].mainConvertRate,
                3
            );
        }
    }

    calculateMainQuantity(index) {
        if (this.saOrderDetails[index].mainConvertRate) {
            this.saOrderDetails[index].mainQuantity = this.round(
                this.saOrderDetails[index].formula === '*'
                    ? this.saOrderDetails[index].quantity * this.saOrderDetails[index].mainConvertRate
                    : this.saOrderDetails[index].quantity / this.saOrderDetails[index].mainConvertRate,
                3
            );
        } else {
            this.saOrderDetails[index].mainQuantity = null;
        }
    }

    calculateMainUnitPrice(index) {
        // if (
        //     this.saOrderDetails[index].unit &&
        //     this.saOrderDetails[index].mainUnit &&
        //     this.saOrderDetails[index].unit.id === this.saOrderDetails[index].mainUnit.id
        // ) {
        //     this.saOrderDetails[index].mainUnitPrice = this.saOrderDetails[index].unitPriceOriginal;
        // } else {
        //     if (this.saOrderDetails[index].formula === '*' && this.saOrderDetails[index].mainConvertRate) {
        //         this.saOrderDetails[index].mainUnitPrice =
        //             this.saOrderDetails[index].unitPriceOriginal / this.saOrderDetails[index].mainConvertRate;
        //     } else {
        //         this.saOrderDetails[index].mainUnitPrice =
        //             this.saOrderDetails[index].unitPriceOriginal * this.saOrderDetails[index].mainConvertRate;
        //     }
        // }

        if (this.saOrderDetails[index].formula === '*' && this.saOrderDetails[index].mainConvertRate) {
            this.saOrderDetails[index].mainUnitPrice = this.round(
                this.saOrderDetails[index].unitPriceOriginal / this.saOrderDetails[index].mainConvertRate,
                1
            );
        } else {
            this.saOrderDetails[index].mainUnitPrice = this.round(
                this.saOrderDetails[index].unitPriceOriginal * this.saOrderDetails[index].mainConvertRate,
                1
            );
        }
    }

    calculateUnitPriceOriginal(index) {
        if (this.saOrderDetails[index].mainConvertRate && this.saOrderDetails[index].mainUnitPrice) {
            this.saOrderDetails[index].unitPriceOriginal = this.round(
                this.saOrderDetails[index].formula === '*'
                    ? this.saOrderDetails[index].mainUnitPrice * this.saOrderDetails[index].mainConvertRate
                    : this.saOrderDetails[index].mainUnitPrice / this.saOrderDetails[index].mainConvertRate,
                2
            );
        } else {
            // this.saOrderDetails[index].unitPriceOriginal = 0;
        }
    }

    calculateUnitPrice(index) {
        if (this.sAOrder.exchangeRate) {
            this.saOrderDetails[index].unitPrice = this.round(
                this.saOrderDetails[index].unitPriceOriginal *
                    (this.currency.formula.includes('*') ? this.sAOrder.exchangeRate : 1 / this.sAOrder.exchangeRate),
                1
            );
        }
    }

    calculateUnitPriceOriginalByAmountOriginal(index) {
        if (this.saOrderDetails[index].amountOriginal && this.saOrderDetails[index].quantity) {
            this.saOrderDetails[index].unitPriceOriginal = this.round(
                this.saOrderDetails[index].amountOriginal / this.saOrderDetails[index].quantity,
                this.getUnitPriceOriginalType()
            );
        } else {
            this.saOrderDetails[index].unitPriceOriginal = 0;
        }
    }

    updatesAOrder() {
        this.sAOrder.totalAmount = this.round(this.sum('amount'), 7);
        this.sAOrder.totalVATAmount = this.round(this.sum('vATAmount'), 7);
        this.sAOrder.totalDiscountAmount = this.round(this.sum('discountAmount'), 7);
        this.sAOrder.total = this.round(this.sAOrder.totalAmount + this.sAOrder.totalVATAmount - this.sAOrder.totalDiscountAmount, 7);

        this.sAOrder.totalAmountOriginal = this.round(this.sum('amountOriginal'), this.getAmountOriginalType());
        this.sAOrder.totalVATAmountOriginal = this.round(this.sum('vATAmountOriginal'), this.getAmountOriginalType());
        this.sAOrder.totalDiscountAmountOriginal = this.round(this.sum('discountAmountOriginal'), this.getAmountOriginalType());
        this.sAOrder.totalOriginal = this.round(
            this.sAOrder.totalAmountOriginal + this.sAOrder.totalVATAmountOriginal - this.sAOrder.totalDiscountAmountOriginal,
            this.getAmountOriginalType()
        );
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.saOrderDetails.length; i++) {
            total += this.saOrderDetails[i][prop] ? this.saOrderDetails[i][prop] : 0;
        }
        return isNaN(total) ? 0 : total;
    }

    public beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.modalRef = this.refModalService.open(this.viewVouchersSelected);
        } else if ($event.nextId === 'sa-quote') {
            $event.preventDefault();
            /* const saQView = this.saOrderDetails.filter(n => n.sAQuoteDetailID);
            this.viewSAQuote = [];
            for (let i = 0; i < saQView.length; i++) {
                this.viewSAQuote.push({});
                this.viewSAQuote[i].sAQuoteDetailID = saQView[i].sAQuoteDetailID;
            }*/
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.modalRef = this.refModalService.open(
                this.viewSAQuote,
                EbSaQuoteModalComponent,
                null,
                false,
                this.sAOrder.typeID,
                null,
                this.sAOrder.currencyID,
                null,
                this.sAOrder.accountingObjectID
            );
        }
    }

    /*checkInvoiceNo() {
        this.sAOrder.invoiceNo = this.utilsService.pad(this.sAOrder.invoiceNo, 7);
    }*/

    saveContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.fillToSave();
        if (this.checkError()) {
            if (this.sAOrder.id !== undefined) {
                this.subscribeToSaveResponseWhenClose(this.sAOrderService.update(this.sAOrder));
            } else {
                this.subscribeToSaveResponseWhenClose(this.sAOrderService.create(this.sAOrder));
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

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    checkSaleDiscountPolicyDTO(index) {
        if (!this.saOrderDetails[index].quantity) {
            this.saOrderDetails[index].quantity = 0;
        }
        if (this.saOrderDetails[index].saleDiscountPolicysDTO) {
            let brForeach = false;
            this.saOrderDetails[index].saleDiscountPolicysDTO.forEach(saleDiscountPolicy => {
                if (!brForeach) {
                    if (
                        saleDiscountPolicy.quantityFrom <= this.saOrderDetails[index].quantity &&
                        this.saOrderDetails[index].quantity <= saleDiscountPolicy.quantityTo
                    ) {
                        if (saleDiscountPolicy.discountType === 0) {
                            this.saOrderDetails[index].discountRate = this.round(saleDiscountPolicy.discountResult, 5);
                            if (
                                (this.saOrderDetails[index].amountOriginal || this.saOrderDetails[index].amountOriginal === 0) &&
                                (this.saOrderDetails[index].discountRate || this.saOrderDetails[index].discountRate === 0)
                            ) {
                                this.saOrderDetails[index].discountAmountOriginal = this.round(
                                    this.saOrderDetails[index].amountOriginal * this.saOrderDetails[index].discountRate / 100,
                                    this.getAmountOriginalType()
                                );
                                if (this.sAOrder.exchangeRate || this.sAOrder.exchangeRate === 0) {
                                    this.saOrderDetails[index].discountAmount = this.round(
                                        this.saOrderDetails[index].discountAmountOriginal *
                                            (this.currency.formula.includes('*')
                                                ? this.sAOrder.exchangeRate
                                                : 1 / this.sAOrder.exchangeRate),
                                        7
                                    );
                                }
                            } else {
                                this.saOrderDetails[index].discountAmountOriginal = 0;
                                this.saOrderDetails[index].discountAmount = 0;
                            }
                        } else if (saleDiscountPolicy.discountType === 1) {
                            this.saOrderDetails[index].discountAmountOriginal = this.round(
                                saleDiscountPolicy.discountResult,
                                this.getAmountOriginalType()
                            );
                            this.saOrderDetails[index].discountAmount = this.round(
                                this.saOrderDetails[index].discountAmountOriginal *
                                    (this.currency.formula.includes('*') ? this.sAOrder.exchangeRate : 1 / this.sAOrder.exchangeRate),
                                7
                            );
                            this.saOrderDetails[index].discountRate = this.round(
                                this.saOrderDetails[index].discountAmountOriginal / this.saOrderDetails[index].amountOriginal,
                                5
                            );
                        } else if (saleDiscountPolicy.discountType === 2) {
                            this.saOrderDetails[index].discountAmountOriginal = this.round(
                                saleDiscountPolicy.discountResult * this.saOrderDetails[index].quantity,
                                this.getAmountOriginalType()
                            );
                            this.saOrderDetails[index].discountAmount = this.round(
                                this.saOrderDetails[index].discountAmountOriginal *
                                    (this.currency.formula.includes('*') ? this.sAOrder.exchangeRate : 1 / this.sAOrder.exchangeRate),
                                7
                            );
                            this.saOrderDetails[index].discountRate = this.round(
                                this.saOrderDetails[index].discountAmountOriginal / this.saOrderDetails[index].amountOriginal,
                                5
                            );
                        }
                        brForeach = true;
                    } else {
                        this.saOrderDetails[index].discountRate = this.round(this.saOrderDetails[index].materialGoods.saleDiscountRate, 5);
                    }
                }
            });
        } else {
            this.saOrderDetails[index].discountRate = this.round(this.saOrderDetails[index].materialGoods.saleDiscountRate, 5);
        }
    }

    /*ngOnDestroy(): void {
        if (this.eventSubscriber) {
            this.eventManager.destroy(this.eventSubscriber);
        }
    }*/

    copy() {
        this.sAOrderCopy = Object.assign({}, this.sAOrder);
        this.saOrderDetailsCopy = this.saOrderDetails.map(object => ({ ...object }));
        this.viewVouchersSelectedCopy = this.viewVouchersSelected.map(object => ({ ...object }));
    }

    registerRef() {
        this.eventSubscriber = this.eventManager.subscribe('selectViewVoucher', response => {
            // if (this.statusVoucher === 1) {
            this.viewVouchersSelected = response.content;
            // }
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    changeDate() {
        this.dateStr = this.sAOrder.date.format('DD/MM/YYYY');
    }

    changeDateStr() {
        try {
            if (this.dateStr.length === 8) {
                const td = this.dateStr.replace(/^(.{2})/, '$1/').replace(/^(.{5})/, '$1/');
                if (!moment(td, 'DD/MM/YYYY').isValid()) {
                    this.validateDate = true;
                    this.sAOrder.date = null;
                } else {
                    this.validateDate = false;
                    this.sAOrder.date = moment(td, 'DD/MM/YYYY');
                }
            } else {
                this.sAOrder.date = null;
                this.validateDate = false;
            }
        } catch (e) {
            this.sAOrder.date = null;
            this.validateDate = false;
        }
    }

    changeDeliverDate() {
        this.deliverDateStr = this.sAOrder.deliverDate ? this.sAOrder.deliverDate.format('DD/MM/YYYY') : null;
    }

    changeDeliverDateStr() {
        try {
            if (this.deliverDateStr.length === 8) {
                const td = this.deliverDateStr.replace(/^(.{2})/, '$1/').replace(/^(.{5})/, '$1/');
                if (!moment(td, 'DD/MM/YYYY').isValid()) {
                    this.validateDeliverDate = true;
                    this.sAOrder.deliverDate = null;
                    this.deliverDateStr = null;
                } else {
                    this.validateDeliverDate = false;
                    this.sAOrder.deliverDate = moment(td, 'DD/MM/YYYY');
                }
            } else {
                this.sAOrder.deliverDate = null;
                this.validateDeliverDate = false;
                this.deliverDateStr = null;
            }
        } catch (e) {
            this.sAOrder.deliverDate = null;
            this.validateDeliverDate = false;
            this.deliverDateStr = null;
        }
    }

    onRightClick($event, data, selectedData, isNew, isDelete, select) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        if (select === 2) {
            this.contextMenu.isCopy = false;
        } else {
            this.contextMenu.isCopy = true;
        }
        this.contextMenu.selectedData = selectedData;
        this.select = select;
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    copyRow(detail, fromKeyDown?) {
        if (!this.getSelectionText() || !fromKeyDown) {
            const detailCopy: any = Object.assign({}, detail);
            detailCopy.id = null;
            this.saOrderDetails.push(detailCopy);
            this.updatesAOrder();
            if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const lst = this.listIDInputDeatil;
                const col = this.indexFocusDetailCol;
                const row = this.saOrderDetails.length - 1;
                this.indexFocusDetailRow = row;
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(lst[col] + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
        }
    }

    valueDefaut() {
        if (!this.sAOrder.totalAmount) {
            this.sAOrder.totalAmount = 0;
        }
        if (!this.sAOrder.totalVATAmount) {
            this.sAOrder.totalVATAmount = 0;
        }
        if (!this.sAOrder.totalDiscountAmount) {
            this.sAOrder.totalDiscountAmount = 0;
        }
        if (!this.sAOrder.total) {
            this.sAOrder.total = 0;
        }
        if (!this.sAOrder.totalAmountOriginal) {
            this.sAOrder.totalAmountOriginal = 0;
        }
        if (!this.sAOrder.totalVATAmountOriginal) {
            this.sAOrder.totalVATAmountOriginal = 0;
        }
        if (!this.sAOrder.totalDiscountAmountOriginal) {
            this.sAOrder.totalDiscountAmountOriginal = 0;
        }
        if (!this.sAOrder.totalOriginal) {
            this.sAOrder.totalOriginal = 0;
        }
    }

    /*
   * hàm ss du lieu 2 object và 2 mảng object
   * return true: neu tat ca giong nhau
   * return fale: neu 1 trong cac object ko giong nhau
   * */
    canDeactive(): boolean {
        if (this.statusVoucher === 0 && !this.isCloseAll && !this.isSaveAndNew) {
            return (
                this.utilsService.isEquivalent(this.sAOrder, this.sAOrderCopy) &&
                this.utilsService.isEquivalentArray(this.saOrderDetails, this.saOrderDetailsCopy) &&
                this.utilsService.isEquivalentArray(this.viewVouchersSelected, this.viewVouchersSelectedCopy)
            );
        } else {
            return true;
        }
    }

    registerCombobox() {
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = true;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscribers.push(this.eventSubscriber);
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilsService.setShowPopup(response.content);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerAddNewRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.AddnewRow(0, true);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            this.removeRow(this.contextMenu.selectedData);
            this.updatesAOrder();
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            this.copyRow(this.contextMenu.selectedData);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    removeRow(detail: object) {
        if (this.select === 2) {
            this.viewVouchersSelected.splice(this.viewVouchersSelected.indexOf(detail), 1);
        } else {
            this.saOrderDetails.splice(this.saOrderDetails.indexOf(detail), 1);
            if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                // vì còn trường hợp = 0
                if (this.saOrderDetails.length > 0) {
                    let row = 0;
                    if (this.indexFocusDetailRow > this.saOrderDetails.length - 1) {
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
    }

    registerViewSAQuote() {
        this.eventSubscriber = this.eventManager.subscribe('selectViewSAQuote', async response => {
            this.viewSAQuote = response.content;
            if (this.checkToFillDataParent(this.viewSAQuote)) {
                this.sAOrder.accountingObjectID = this.viewSAQuote[0].accountingObjectID;
                this.sAOrder.accountingObjectName = this.viewSAQuote[0].accountingObjectName;
                this.sAOrder.accountingObjectAddress = this.viewSAQuote[0].accountingObjectAddress;
                this.sAOrder.companyTaxCode = this.viewSAQuote[0].companyTaxCode;
                this.sAOrder.contactName = this.viewSAQuote[0].contactName;
                // this.sAOrder.deliverDate = this.viewSAQuote[0].deliveryTime;
                this.sAOrder.employeeID = this.viewSAQuote[0].employeeID;
                this.sAOrder.reason = this.viewSAQuote[0].descriptionParent;
            }
            // this.deleteSAQuote(this.viewSAQuote);
            this.saOrderDetails = [];
            let listMTGCV: IMaterialGoodsConvertUnit[];
            this.materialGoodsConvertUnitService
                .getAllMaterialGoodsConvertUnits()
                .subscribe((res: HttpResponse<IMaterialGoodsConvertUnit[]>) => {
                    listMTGCV = res.body;
                    for (let i = 0; i < this.viewSAQuote.length; i++) {
                        const detail: ISAOrderDetails = {};
                        detail.vATAmount = this.viewSAQuote[i].vATAmount;
                        detail.vATAmountOriginal = this.viewSAQuote[i].vATAmountOriginal;
                        detail.vATRate = this.viewSAQuote[i].vATRate;
                        detail.amount = this.viewSAQuote[i].amount;
                        detail.amountOriginal = this.viewSAQuote[i].amountOriginal;
                        detail.discountRate = this.viewSAQuote[i].discountRate;
                        detail.discountAmount = this.viewSAQuote[i].discountAmount;
                        detail.discountAmountOriginal = this.viewSAQuote[i].discountAmountOriginal;
                        detail.quantity = this.viewSAQuote[i].quantity;
                        // detail.quantityDelivery = this.viewSAQuote[i].mainQuantity;
                        detail.mainUnitPrice = this.viewSAQuote[i].mainUnitPrice;
                        detail.unitPrice = this.viewSAQuote[i].unitPrice;
                        detail.unitPriceOriginal = this.viewSAQuote[i].unitPriceOriginal;
                        detail.formula = this.viewSAQuote[i].formula;
                        detail.description = this.viewSAQuote[i].description;
                        detail.mainConvertRate = this.viewSAQuote[i].mainConvertRate;
                        detail.sAQuoteID = this.viewSAQuote[i].id;
                        detail.sAQuoteDetailID = this.viewSAQuote[i].sAQuoteDetailID;
                        detail.vATDescription = this.viewSAQuote[i].vATDescription;
                        detail.mainQuantity = this.viewSAQuote[i].mainQuantity;
                        detail.units = this.getUintFromMaterialgoods(this.units, listMTGCV, this.viewSAQuote[i].materialGoodsID);
                        detail.saleDiscountPolicysDTO = this.saleDiscountPolicysDTO.filter(
                            n => n.materialGoodsID === this.viewSAQuote[i].materialGoodsID
                        );
                        detail.unit = this.units.find(n => n.id === this.viewSAQuote[i].unitID);
                        detail.mainUnit = this.units.find(n => n.id === this.viewSAQuote[i].mainUnitID);
                        detail.materialGoods = this.materialGoodss.find(n => n.id === this.viewSAQuote[i].materialGoodsID);
                        if (detail.unit && detail.mainUnit && detail.unit.id === detail.mainUnit.id) {
                            detail.unitPriceOriginals = this.getUnitPriceOriginals(detail.materialGoods);
                        } else {
                            detail.unitPriceOriginals = this.getUnitPriceOriginalsWithUnitConvert(detail.unit.id, detail.materialGoods);
                        }
                        this.saOrderDetails.push(detail);
                    }
                    this.updatesAOrder();
                });
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    checkToFillDataParent(viewSAQuote: IViewSAQuoteDTO[]) {
        if (viewSAQuote.length === 1) {
            return true;
        } else if (viewSAQuote.length > 1) {
            return !(
                viewSAQuote.some(n => n.accountingObjectID !== viewSAQuote[0].accountingObjectID) ||
                viewSAQuote.some(n => n.accountingObjectAddress !== viewSAQuote[0].accountingObjectAddress) ||
                viewSAQuote.some(n => n.accountingObjectName !== viewSAQuote[0].accountingObjectName) ||
                viewSAQuote.some(n => n.companyTaxCode !== viewSAQuote[0].companyTaxCode) ||
                viewSAQuote.some(n => n.contactName !== viewSAQuote[0].contactName) ||
                viewSAQuote.some(n => n.descriptionParent !== viewSAQuote[0].descriptionParent) ||
                viewSAQuote.some(n => n.employeeID !== viewSAQuote[0].employeeID) ||
                viewSAQuote.some(n => n.deliveryTime !== viewSAQuote[0].deliveryTime)
            );
        } else {
            return false;
        }
    }

    deleteSAQuote(viewSAQuote: IViewSAQuoteDTO[]) {
        const saDTQ = this.saOrderDetails.filter(n => n.sAQuoteDetailID);
        for (let i = 0; i < saDTQ.length; i++) {
            if (!viewSAQuote.find(n => n.sAQuoteDetailID === saDTQ[i].sAQuoteDetailID)) {
                this.saOrderDetails.splice(this.saOrderDetails.indexOf(saDTQ[i]), 1);
            }
        }
    }

    getUintFromMaterialgoods(units: IUnit[], materialGoodsConvertUnits: IMaterialGoodsConvertUnit[], materialGoodsID: string) {
        const units_result: IUnit[] = [];
        const mg: IMaterialGoodsConvertUnit[] = materialGoodsConvertUnits.filter(n => n.materialGoodsID === materialGoodsID);
        for (let i = 0; i < mg.length; i++) {
            const unit = units.find(n => n.id === mg[i].unitID);
            if (unit) {
                units_result.push(unit);
            }
        }
        const mgF = this.materialGoodss.find(n => n.id === materialGoodsID);
        if (mgF) {
            for (let i = 0; i < units.length; i++) {
                if (units[i].id === mgF.unitID) {
                    if (!units_result.find(n => n.id === units[i].id)) {
                        units[i].formula = '*';
                        units[i].convertRate = 1;
                        units_result.push(units[i]);
                    }
                }
            }
        }
        return units_result;
    }

    getUintFromMaterialgoodsByID(materialGoodsID: string) {
        const units_result: IUnit[] = [];
        const mg: IMaterialGoodsConvertUnit[] = this.materialGoodsConvertUnits.filter(n => n.materialGoodsID === materialGoodsID);
        for (let i = 0; i < mg.length; i++) {
            const unit = this.units.find(n => n.id === mg[i].unitID);
            if (unit) {
                units_result.push(unit);
            }
        }
        const mgF = this.materialGoodss.find(n => n.id === materialGoodsID);
        if (mgF) {
            for (let i = 0; i < this.units.length; i++) {
                if (this.units[i].id === mgF.unitID) {
                    if (!units_result.find(n => n.id === this.units[i].id)) {
                        this.units[i].formula = '*';
                        this.units[i].convertRate = 1;
                        units_result.push(this.units[i]);
                    }
                }
            }
        }
        return units_result;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DonDatHang_Xoa])
    delete() {
        event.preventDefault();
        if (!this.utilsService.isShowPopup) {
            this.router.navigate(['/sa-order', { outlets: { popup: this.sAOrder.id + '/delete' } }]);
        }
    }

    round(value, type) {
        if (type === 8) {
            if (this.isForeignCurrency()) {
                return this.utilsService.round(value, this.currentAccount.systemOption, type);
            } else {
                return this.utilsService.round(value, this.currentAccount.systemOption, 7);
            }
        } else if (type === 2) {
            if (this.isForeignCurrency()) {
                return this.utilsService.round(value, this.currentAccount.systemOption, type);
            } else {
                return this.utilsService.round(value, this.currentAccount.systemOption, 1);
            }
        } else {
            return this.utilsService.round(value, this.currentAccount.systemOption, type);
        }
    }

    isForeignCurrency() {
        return this.currentAccount && this.sAOrder.currencyID !== this.currentAccount.organizationUnit.currencyID;
    }

    getAmountOriginalType() {
        if (this.isForeignCurrency()) {
            return 8;
        }
        return 7;
    }

    getUnitPriceOriginalType() {
        if (this.isForeignCurrency()) {
            return 2;
        }
        return 1;
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.saOrderDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.sAOrder;
    }

    addDataToDetail() {
        this.saOrderDetails = this.details ? this.details : this.saOrderDetails;
        this.sAOrder = this.parent ? this.parent : this.sAOrder;
    }

    actionFocus(indexCol, indexRow) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
    }
}
