import { AfterViewChecked, AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { AccountingObjectDTO } from 'app/shared/model/accounting-object.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { ActivatedRoute, Router } from '@angular/router';
import { CurrencyService } from 'app/danhmuc/currency';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { AccountDefaultService } from 'app/danhmuc/account-default';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IPporder, Pporder } from 'app/shared/model/pporder.model';
import { IMaterialGoodsInStock } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { UnitService } from 'app/danhmuc/unit';
import { PporderService } from 'app/entities/pporder';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { PPOrderDetail } from 'app/shared/model/pporderdetail.model';
import { Principal } from 'app/core';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { Subscription } from 'rxjs';
import * as moment from 'moment';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ISAInvoice } from 'app/shared/model/sa-invoice.model';
import { CURRENCY, FORMULA, REPORT, TypeID } from 'app/app.constants';
import { ROLE } from 'app/role.constants';
import { el } from '@angular/platform-browser/testing/src/browser_util';
import { ITEMS_PER_PAGE } from 'app/shared';

@Component({
    selector: 'eb-don-mua-hang-update',
    templateUrl: './don-mua-hang-update.component.html',
    styleUrls: ['./don-mua-hang-update.component.css']
})
export class DonMuaHangUpdateComponent extends BaseComponent implements OnInit, AfterViewInit, AfterViewChecked {
    @ViewChild('referencePPOrder') referencePPOrder: NgbModalRef;
    @ViewChild('content') content: NgbModalRef;
    @ViewChild('deleteItem') deleteItem: NgbModalRef;
    @ViewChild('vatValidModal') vatValidModal: any;
    isSaving: boolean;
    dateDp: any;
    accountDefaults: string[] = [];
    accountingObjects: AccountingObjectDTO[];
    employees: AccountingObjectDTO[];
    currencies: ICurrency[];
    totalvatamount: number;
    totalvatamountoriginal: number;
    isCreateUrl: boolean;
    isCurrencyVND: boolean;
    page: number;
    itemsPerPage: number;
    pageCount: number;
    totalItems: number;
    rowNum: number;
    predicate: any;
    reverse: any;
    accountingObjectName: any;
    dataSession: IDataSessionStorage;
    ppOrder: Pporder;
    ppOrderCopy: Pporder;
    isEditUrl: boolean;
    currency: ICurrency;
    deliverDate: any;
    date: any;
    ppOrderDetails: PPOrderDetail[];
    ppOrderDetailsCopy: PPOrderDetail[];
    materialGoodsInStock: IMaterialGoodsInStock[];
    isEdit: boolean;
    isCheckPopup: boolean;
    vatValid: boolean;
    vatValidNUllCase: boolean;
    searchData: string;
    account: any;
    listVAT = [{ name: '0%', data: 0 }, { name: '5%', data: 1 }, { name: '10%', data: 2 }];
    contextMenu: ContextMenu;
    quantitySum: number;
    quantityReceiptSum: number;
    amountSum: number;
    amountOriginalSum: number;
    discountAmountOriginal: number;
    discountAmount: number;
    vatAmountOriginal: number;
    vatAmount: number;
    modalRef: NgbModalRef;
    viewVouchersSelected: any;
    viewVouchersSelectedCopy: any;
    eventSubscriber: Subscription;
    discountAmountOriginalSum: number;
    discountAmountSum: number;
    vatAmountOriginalSum: number;
    vatAmountSum: number;
    mainQuantitySum: number;
    receivedDateHolder: any;
    receivedDateHolderMask: any;
    dateHolder: any;
    dateHolderMask: any;
    isClosing: boolean;
    requiredInput: boolean;
    isMove: boolean;
    isClosed: boolean;
    template: number;
    isViewFromRef: boolean;
    isSaved: boolean;
    currencyFormula: string;
    isInputReason: boolean;
    DDSo_DonGia = 1; // Đơn giá - 1
    DDSo_DonGiaNT = 2; // Đơn giá ngoại tệ - 2
    priceOriginalType = this.DDSo_DonGia;
    priceType = this.DDSo_DonGiaNT;

    // ROLE
    ROLE_XEM = ROLE.DonMuaHang_XEM;
    ROLE_THEM = ROLE.DonMuaHang_THEM;
    ROLE_SUA = ROLE.DonMuaHang_SUA;
    ROLE_XOA = ROLE.DonMuaHang_XOA;
    ROLE_IN = ROLE.DonMuaHang_IN;
    ROLE_KETXUAT = ROLE.DonMuaHang_KET_XUAT;
    REPORT = REPORT;
    select: any;
    indexFocusDetailCol: any;
    indexFocusDetailRow: any;
    listIDInputDeatil: any[] = [
        'productCode',
        'productName',
        'unit',
        'amount',
        'amountReceived',
        'unitPrice',
        'unitPrice',
        'mainConvertRate',
        'mainQuantity',
        'mainUnitPrice',
        'amountOriginal',
        'amount',
        'discountRate',
        'discountAmountOriginal',
        'discountAmount',
        'vatRate',
        'vatAmountOriginal',
        'vatAmount'
    ];

    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private currencyService: CurrencyService,
        private accountingObjectService: AccountingObjectService,
        private accountDefaultService: AccountDefaultService,
        private jhiAlertService: JhiAlertService,
        private materialGoodsService: MaterialGoodsService,
        private pporderService: PporderService,
        private toastrService: ToastrService,
        private translateService: TranslateService,
        private unitService: UnitService,
        private eventManager: JhiEventManager,
        public utilsService: UtilsService,
        private refModalService: RefModalService,
        private principal: Principal,
        private modalService: NgbModal
    ) {
        super();
        this.getSessionData();
        this.contextMenu = new ContextMenu();
        this.viewVouchersSelected = [];
    }

    ngOnInit() {
        this.isViewFromRef = window.location.href.includes('/from-ref');
        this.template = 2;
        this.initValues();
        this.isEdit = true;
        this.isEditUrl = window.location.href.includes('/edit') || window.location.href.includes('/view');
        this.isCreateUrl = window.location.href.includes('/don-mua-hang/new');
        this.activatedRoute.data.subscribe(data => {
            this.principal.identity().then(account => {
                this.account = account;
                if (data.ppOrder && data.ppOrder.id) {
                    this.ppOrder = data && data.ppOrder ? data.ppOrder : {};
                    this.pporderService.getRefVouchersByPPOrderID(this.ppOrder.id).subscribe(res => (this.viewVouchersSelected = res.body));
                } else {
                    this.setDefaultDataFromSystemOptions();
                }
                this.ppOrderDataSetup();
                if (this.dataSession && this.dataSession.isEdit) {
                    this.edit();
                    this.dataSession.isEdit = false;
                }
            });
        });
        this.subcribeEvent();
        this.onSetUpHolderMask();
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilsService.setShowPopup(response.content);
        });
        this.registerIsShowPopup();
        this.registerAddNewRow();
        this.registerDeleteRow();
        this.registerCopyRow();
    }

    registerIsShowPopup() {
        this.utilsService.checkEvent.subscribe(res => {
            this.isShowPopup = res;
        });
    }

    copy() {
        this.ppOrderCopy = this.utilsService.deepCopyObject(this.ppOrder);
        this.ppOrderDetailsCopy = this.utilsService.deepCopy(this.ppOrderDetails);
        this.viewVouchersSelectedCopy = this.utilsService.deepCopy(this.viewVouchersSelected);
    }

    previousState(content) {
        if (!this.utilsService.isShowPopup) {
            this.isClosing = true;
            if (this.isCheckPopup) {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(content, { backdrop: 'static' });
            } else if (
                this.isSaved ||
                !this.ppOrderCopy ||
                this.isMove ||
                (this.utilsService.isEquivalent(this.ppOrder, this.ppOrderCopy) &&
                    this.utilsService.isEquivalentArray(this.ppOrderDetails, this.ppOrderDetailsCopy) &&
                    this.utilsService.isEquivalentArray(this.viewVouchersSelected, this.viewVouchersSelectedCopy))
            ) {
                this.closeAll();
                return;
            } else if (!this.utilsService.isShowPopup) {
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.modalRef = this.modalService.open(content, { backdrop: 'static' });
                // this.copy();
                // this.closeAll();
                // return;
            }
        }
    }

    canDeactive() {
        if (this.isClosing || !this.ppOrderCopy || this.isClosed) {
            return true;
        }
        return (
            this.utilsService.isEquivalent(this.ppOrder, this.ppOrderCopy) &&
            this.utilsService.isEquivalentArray(this.ppOrderDetails, this.ppOrderDetailsCopy) &&
            this.utilsService.isEquivalentArray(this.viewVouchersSelected, this.viewVouchersSelectedCopy)
        );
    }

    closeAll() {
        this.isClosed = true;
        this.dataSession = JSON.parse(sessionStorage.getItem('DMHSearchData'));
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
        sessionStorage.removeItem('DMHDataSession');
        this.router.navigate(
            ['/don-mua-hang'],
            this.dataSession
                ? {
                      queryParams: {
                          page: this.page,
                          size: this.itemsPerPage,
                          sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                      }
                  }
                : {
                      queryParams: {
                          page: 0,
                          size: ITEMS_PER_PAGE,
                          sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                      }
                  }
        );
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

    checkVATValid() {
        this.vatValid = true;
        this.modalRef.close();
        this.save(false);
    }

    checkRequiredInput() {
        this.requiredInput = !this.utilsService.checkNoBook(this.ppOrder.no);
    }

    // checkNoBook(noFBook: string): boolean {
    //     const regExp = /^\D*\d{3,}.*$/;
    //     if (noFBook.length > 25) {
    //         return false;
    //     } else if (!regExp.test(noFBook)) {
    //         return false;
    //     } else {
    //         return true;
    //     }
    // }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('DMHDataSession'));
        if (!this.dataSession) {
            this.dataSession = JSON.parse(sessionStorage.getItem('DMHSearchData'));
        }

        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.pageCount = this.dataSession.pageCount;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.searchData = this.dataSession.searchVoucher;
            this.predicate = this.dataSession.predicate;
            this.accountingObjectName = this.dataSession.accountingObjectName;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
    }

    ppOrderDataSetup() {
        this.accountingObjects = [];
        if (this.ppOrder.id) {
            this.isEdit = false;
        }
        if (!this.isEditUrl) {
            this.genNewVoucerCode();
        }
        if (this.ppOrder.ppOrderDetails) {
            this.ppOrderDetails = this.ppOrder.ppOrderDetails;
            this.updateSum();
        }
        this.materialGoodsService.queryForComboboxGood().subscribe(
            (res: HttpResponse<any>) => {
                this.materialGoodsInStock = res.body;
            },
            (res: HttpErrorResponse) => console.log(res.message)
        );
        this.accountingObjectService.getAccountingObjectsForProvider().subscribe((res: HttpResponse<AccountingObjectDTO[]>) => {
            this.accountingObjects = res.body;
            if (this.ppOrder && this.ppOrder.accountingObjectId) {
                this.ppOrder.accountingObject = this.accountingObjects.find(item => item.id === this.ppOrder.accountingObjectId);
            }
        });
        this.accountingObjectService.getAccountingObjectsForEmployee().subscribe((res: HttpResponse<AccountingObjectDTO[]>) => {
            this.employees = res.body;
            if (this.ppOrder && this.ppOrder.employeeId) {
                this.ppOrder.accountingObjectEmployee = this.employees.find(item => item.id === this.ppOrder.employeeId);
            }
        });

        this.getActiveCurrencies();

        if (this.ppOrder.totalAmount) {
            this.ppOrder.total = this.ppOrder.totalAmount + (this.ppOrder.totalVATAmount ? this.ppOrder.totalVATAmount : 0);
        }
        if (this.ppOrder.totalAmountOriginal) {
            this.ppOrder.totalOriginal =
                this.ppOrder.totalAmountOriginal + (this.ppOrder.totalVATAmountOriginal ? this.ppOrder.totalVATAmountOriginal : 0);
        }
    }

    genNewVoucerCode() {
        this.utilsService
            .getGenCodeVoucher({
                typeGroupID: 20,
                displayOnBook: 2
            })
            .subscribe((res: HttpResponse<string>) => {
                this.ppOrder.no = res.body;
                if (!this.ppOrder.id) {
                    this.copy();
                }
            });
    }

    initValues() {
        this.mainQuantitySum = 0;
        this.amountOriginalSum = 0;
        this.amountSum = 0;
        this.quantityReceiptSum = 0;
        this.quantitySum = 0;
        this.discountAmountOriginalSum = 0;
        this.discountAmountSum = 0;
        this.vatAmountOriginalSum = 0;
        this.vatAmountSum = 0;
        this.ppOrder = {};
        this.ppOrder.status = 0;
        this.ppOrder.accountingObject = {};
        this.ppOrder.accountingObjectEmployee = {};
        this.translateService.get(['ebwebApp.purchaseOrder.home.title']).subscribe((res: any) => {
            this.ppOrder.reason = res['ebwebApp.purchaseOrder.home.title'];
        });
        this.ppOrderDetails = [];
        this.isSaving = false;
        this.isInputReason = false;
        this.setDefaultDataFromSystemOptions();
    }

    setDefaultDataFromSystemOptions() {
        if (this.account) {
            this.ppOrder.date = this.utilsService.ngayHachToan(this.account);
            this.onSetUpHolderMask();
            if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                this.ppOrder.currencyId = this.account.organizationUnit.currencyID;
                this.getActiveCurrencies();
            }
        }
    }

    isForeignCurrency() {
        // Check đồng tiền hoạch toán
        // this.account.organizationUnit.currencyID : đồng tiền hoạch toán
        return this.account && this.ppOrder.currencyId !== this.account.organizationUnit.currencyID;
    }

    /*Nếu loại tiền = tiền hoạch toán thì ẩn*/
    // isHiddenCurrency() {
    //     if (!this.isEditUrl) {
    //         if (this.ppOrder.currency) {
    //             return this.ppOrder.currency.currencyCode === this.account.organizationUnit.currencyID;
    //         }
    //     } else {
    //         return false;
    //     }
    // }

    getCurrencyType() {
        // this.ppOrder.currencyId : đồng tiền hiện tại
        // this.account.organizationUnit.currencyID : đồng tiền hoạch toán
        // priceType : đơn giá quy đổi
        // priceOriginalType : đơn giá
        if (this.ppOrder.currencyId === this.account.organizationUnit.currencyID) {
            this.priceOriginalType = this.DDSo_DonGia;
        } else {
            this.priceOriginalType = this.DDSo_DonGiaNT;
            this.priceType = this.DDSo_DonGia;
        }
    }

    getOriginalType() {
        return this.isForeignCurrency() ? 8 : 7;
    }

    getActiveCurrencies() {
        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencies = res.body;
            if (this.ppOrder && this.ppOrder.currencyId) {
                this.ppOrder.currency = this.currencies.find(item => item.currencyCode === this.ppOrder.currencyId);
            } else if (this.ppOrder && !this.ppOrder.currencyId) {
                this.ppOrder.currency = this.currencies[0];
            }
            this.changeCurrency();
            if (!this.ppOrder.id) {
                this.copy();
            }
        });
    }

    selectAccountingObjects() {
        if (this.ppOrder.accountingObject) {
            this.ppOrder.accountingObjectId = this.ppOrder.accountingObject.id;
            this.ppOrder.accountingObjectName = this.ppOrder.accountingObject.accountingObjectName;
            this.ppOrder.accountingObjectAddress = this.ppOrder.accountingObject.accountingObjectAddress;
            this.ppOrder.companyTaxCode = this.ppOrder.accountingObject.taxCode;
            this.ppOrder.contactName = this.ppOrder.accountingObject.contactName;
            if (!this.isInputReason) {
                this.ppOrder.reason = this.translateService.instant('ebwebApp.pporder.defaultReason', {
                    name: this.ppOrder.accountingObjectName
                });
            }
        } else {
            this.ppOrder.accountingObjectName = null;
            this.ppOrder.accountingObjectAddress = null;
            this.ppOrder.companyTaxCode = null;
            this.ppOrder.contactName = null;
            this.translateService.get(['ebwebApp.purchaseOrder.home.title']).subscribe((res: any) => {
                this.ppOrder.reason = res['ebwebApp.purchaseOrder.home.title'];
            });
        }
    }

    changeReason() {
        this.ppOrder.reason = this.translateService.instant('ebwebApp.pporder.defaultReason', {
            name: this.ppOrder.accountingObjectName
        });
    }

    changeDiscountRate(index) {
        if (this.ppOrderDetails[index].amountOriginal && this.ppOrderDetails[index].discountRate) {
            this.ppOrderDetails[index].discountAmountOriginal =
                this.ppOrderDetails[index].amountOriginal * this.ppOrderDetails[index].discountRate / 100;
            if (this.ppOrder.exchangeRate) {
                this.updateDiscountAmount(index);
            }
        } else {
            this.ppOrderDetails[index].discountAmountOriginal = 0;
            this.ppOrderDetails[index].discountAmount = 0;
        }
        this.changeVATRate(index);
    }

    changeVATRate(index) {
        if (
            this.ppOrderDetails[index].amountOriginal &&
            (this.ppOrderDetails[index].vatRate === 1 || this.ppOrderDetails[index].vatRate === 2)
        ) {
            this.ppOrderDetails[index].vatAmountOriginal =
                (this.ppOrderDetails[index].amountOriginal -
                    (this.ppOrderDetails[index].discountAmountOriginal ? this.ppOrderDetails[index].discountAmountOriginal : 0)) *
                (this.ppOrderDetails[index].vatRate === 1 ? 0.05 : 0.1);
            if (this.ppOrder.exchangeRate) {
                this.vatAmountOriginalChange(index);
            }
        } else {
            this.ppOrderDetails[index].vatAmountOriginal = 0;
            this.ppOrderDetails[index].vatAmount = 0;
        }
        this.updateSum();
    }

    updatePPOrder() {
        this.ppOrder.totalAmount = this.sum('amount');
        this.ppOrder.totalVATAmount = this.sum('vatAmount');
        this.ppOrder.totalDiscountAmount = this.sum('discountAmount');
        this.roundObject();
        this.ppOrder.total = this.ppOrder.totalAmount + this.ppOrder.totalVATAmount - this.ppOrder.totalDiscountAmount;
        this.ppOrder.totalAmountOriginal = this.sum('amountOriginal');
        this.ppOrder.totalVATAmountOriginal = this.sum('vatAmountOriginal');
        this.ppOrder.totalDiscountAmountOriginal = this.sum('discountAmountOriginal');
        this.roundObject();
        this.ppOrder.totalOriginal =
            this.ppOrder.totalAmountOriginal + this.ppOrder.totalVATAmountOriginal - this.ppOrder.totalDiscountAmountOriginal;
    }

    roundObject() {
        this.utilsService.roundObject(this.ppOrderDetails, this.account.systemOption);
        this.utilsService.roundObject(this.ppOrder, this.account.systemOption);
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.ppOrderDetails.length; i++) {
            total += this.ppOrderDetails[i][prop];
        }
        return total ? total : 0;
    }

    onAmountChange(index) {
        this.ppOrderDetails[index].unitPriceOriginal = this.ppOrderDetails[index].amountOriginal / this.ppOrderDetails[index].quantity;
        this.roundObject();
        this.selectUnitPriceOriginal(index);
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

    selectUnit(index: number) {
        if (
            this.ppOrderDetails[index].unit &&
            this.ppOrderDetails[index].mainUnit &&
            this.ppOrderDetails[index].unit.id === this.ppOrderDetails[index].mainUnit.id
        ) {
            this.ppOrderDetails[index].mainConvertRate = 1;
            this.ppOrderDetails[index].formula = '*';
            this.ppOrderDetails[index].mainQuantity = this.ppOrderDetails[index].quantity;
            this.ppOrderDetails[index].mainUnitPrice = this.ppOrderDetails[index].unitPriceOriginal;
        } else {
            if (this.ppOrderDetails[index].unit) {
                this.ppOrderDetails[index].mainConvertRate = this.ppOrderDetails[index].unit.convertRate;
                this.ppOrderDetails[index].formula = this.ppOrderDetails[index].unit.formula;
                this.ppOrderDetails[index].unitId = this.ppOrderDetails[index].unit.id;
                if (this.ppOrderDetails[index].formula === '*') {
                    this.ppOrderDetails[index].mainQuantity =
                        this.ppOrderDetails[index].unit.convertRate * this.ppOrderDetails[index].quantity;
                    this.ppOrderDetails[index].mainUnitPrice =
                        this.ppOrderDetails[index].unitPriceOriginal / this.ppOrderDetails[index].mainConvertRate;
                } else {
                    this.ppOrderDetails[index].mainQuantity =
                        this.ppOrderDetails[index].quantity / this.ppOrderDetails[index].unit.convertRate;
                    this.ppOrderDetails[index].mainUnitPrice =
                        this.ppOrderDetails[index].unitPriceOriginal * this.ppOrderDetails[index].mainConvertRate;
                }
            }
        }
        this.changeQuantity(index);
    }

    // event thay doi select option value
    selectEmployee() {
        this.ppOrder.employeeId = this.ppOrder.accountingObjectEmployee.id;
    }

    changeCurrency() {
        if (!this.isEditUrl || this.isEdit) {
            this.ppOrder.exchangeRate = this.ppOrder.currency ? this.ppOrder.currency.exchangeRate : 1;
        }
        this.ppOrder.currencyId = this.ppOrder.currency ? this.ppOrder.currency.currencyCode : '';
        this.isCurrencyVND = this.account.organizationUnit.currencyID === this.ppOrder.currencyId;
        this.currencyFormula = this.ppOrder.currency && this.ppOrder.currency.formula ? this.ppOrder.currency.formula : FORMULA.MULTIPLY;
        this.getCurrencyType();
        if (this.ppOrderDetails.length) {
            for (let i = 0; i < this.ppOrderDetails.length; i++) {
                if (this.ppOrderDetails[i].materialGood) {
                    this.getConvertRate(this.ppOrderDetails[i].materialGood.id, i);
                }
            }
        }
    }

    changeExchangeRate() {
        for (let index = 0; index < this.ppOrderDetails.length; index++) {
            this.selectUnitPriceOriginal(index);
            this.changeDiscountRate(index);
            this.changeVATRate(index);
        }
    }

    selectedMaterialGoods(index) {
        if (this.ppOrderDetails && this.ppOrderDetails[index].materialGood) {
            const currentMaterialGoodsId = this.ppOrderDetails[index].materialGood.id;
            this.ppOrderDetails[index].materialGoodsId = currentMaterialGoodsId;
            this.ppOrderDetails[index].description = this.ppOrderDetails[index].materialGood.materialGoodsName;
            this.ppOrderDetails[index].vatRate = this.ppOrderDetails[index].materialGood.vatTaxRate;
            this.ppOrderDetails[index].discountRate = this.ppOrderDetails[index].materialGood.purchaseDiscountRate;
            this.getConvertRate(currentMaterialGoodsId, index);
        }
    }

    getConvertRate(currentMaterialGoodsId, index) {
        this.unitService.convertRateForMaterialGoodsComboboxCustom({ materialGoodsId: currentMaterialGoodsId }).subscribe(
            res => {
                this.ppOrderDetails[index].convertRates = res.body;
                if (this.isEdit) {
                    this.ppOrderDetails[index].unit = this.ppOrderDetails[index].convertRates[0];
                    if (this.ppOrderDetails[index].unit) {
                        this.ppOrderDetails[index].unitId = this.ppOrderDetails[index].unit.id;
                    }
                    this.ppOrderDetails[index].mainUnit = this.ppOrderDetails[index].convertRates[0];
                    if (this.ppOrderDetails[index].mainUnit) {
                        this.ppOrderDetails[index].mainUnitId = this.ppOrderDetails[index].mainUnit.id;
                    }
                }
                this.updateSum();
                if (!this.ppOrder.id) {
                    this.getUnitPriceOriginal(currentMaterialGoodsId, index);
                }
            },
            error => console.log(error)
        );
    }

    getUnitPriceOriginal(currentMaterialGoodsId, index) {
        this.ppOrderDetails[index].unitPrices = [];
        this.ppOrderDetails[index].unitPrice = 0;
        this.ppOrderDetails[index].unitPriceOriginal = 0;

        this.unitService
            .unitPriceOriginalForMaterialGoods({
                materialGoodsId: currentMaterialGoodsId,
                unitId: this.ppOrderDetails[index].materialGood.unitID || '',
                currencyCode: this.ppOrder.currency.currencyCode
            })
            .subscribe(
                res => {
                    for (const item of res.body) {
                        this.ppOrderDetails[index].unitPrices.push({ data: item });
                    }
                    if (this.ppOrderDetails[index].unitPrices.length && this.ppOrderDetails[index].unitPrices[0]) {
                        this.ppOrderDetails[index].unitPriceOriginal = this.ppOrderDetails[index].unitPrices[0].data;
                        this.selectUnitPriceOriginal(index);
                    }
                    this.selectUnitPriceOriginal(index);
                    this.selectUnit(index);
                },
                error => console.log(error)
            );
    }

    onUnitChange(index) {
        this.ppOrderDetails[index].unitPrices = [];
        this.ppOrderDetails[index].unitPrice = 0;
        this.ppOrderDetails[index].unitPriceOriginal = 0;
        this.unitService
            .unitPriceOriginalForMaterialGoods({
                materialGoodsId: this.ppOrderDetails[index].materialGood.id,
                unitId: this.ppOrderDetails[index].unit.id,
                currencyCode: this.ppOrder.currency.currencyCode
            })
            .subscribe(
                res => {
                    for (const item of res.body) {
                        this.ppOrderDetails[index].unitPrices.push({ data: item });
                    }
                    if (this.ppOrderDetails[index].unitPrices.length && this.ppOrderDetails[index].unitPrices[0]) {
                        this.ppOrderDetails[index].unitPriceOriginal = this.ppOrderDetails[index].unitPrices[0].data;
                    }
                    this.selectUnitPriceOriginal(index);
                    this.selectUnit(index);
                },
                error => console.log(error)
            );
    }

    changeQuantity(index) {
        if (this.ppOrderDetails[index].formula === '*') {
            this.ppOrderDetails[index].mainQuantity = this.ppOrderDetails[index].mainConvertRate * this.ppOrderDetails[index].quantity;
        } else {
            this.ppOrderDetails[index].mainQuantity = this.ppOrderDetails[index].quantity / this.ppOrderDetails[index].mainConvertRate;
        }
        if (
            (this.ppOrderDetails[index].quantity || this.ppOrderDetails[index].quantity === 0) &&
            (this.ppOrderDetails[index].unitPriceOriginal || this.ppOrderDetails[index].unitPriceOriginal === 0)
        ) {
            this.ppOrderDetails[index].amountOriginal = this.ppOrderDetails[index].quantity * this.ppOrderDetails[index].unitPriceOriginal;
            this.roundObject();
            if (this.ppOrder.exchangeRate) {
                this.updateAmount(index);
            }
        }
        this.roundObject();
        this.changeDiscountRate(index);
    }

    updateSum() {
        this.mainQuantitySum = 0;
        this.amountOriginalSum = 0;
        this.amountSum = 0;
        this.quantityReceiptSum = 0;
        this.quantitySum = 0;
        this.discountAmountOriginalSum = 0;
        this.discountAmountSum = 0;
        this.vatAmountOriginalSum = 0;
        this.vatAmountSum = 0;
        if (this.ppOrderDetails.length) {
            for (const item of this.ppOrderDetails) {
                this.quantitySum += item.quantity;
                this.quantityReceiptSum += item.quantityReceipt;
                this.amountSum += item.amount;
                this.amountOriginalSum += item.amountOriginal;
                this.discountAmountOriginalSum += item.discountAmountOriginal;
                this.discountAmountSum += item.discountAmount;
                this.vatAmountOriginalSum += item.vatAmountOriginal;
                this.vatAmountSum += item.vatAmount;
                this.mainQuantitySum += item.mainQuantity;
            }
        }
        this.updatePPOrder();
    }

    addNewRow(select: number, isRightClick?) {
        if (select === 0) {
            let lenght = 0;
            if (isRightClick) {
                this.ppOrderDetails.splice(this.indexFocusDetailRow + 1, 0, {});
                lenght = this.indexFocusDetailRow + 2;
            } else {
                this.ppOrderDetails.push({
                    discountRate: 0
                });
                this.changeDiscountRate(this.ppOrderDetails.length - 1);
                lenght = this.ppOrderDetails.length;
            }
            this.ppOrderDetails[lenght - 1].amountOriginal = 0;
            this.ppOrderDetails[lenght - 1].amount = 0;
            this.ppOrderDetails[lenght - 1].unitPrice = 0;
            this.ppOrderDetails[lenght - 1].unitPriceOriginal = 0;
            this.ppOrderDetails[lenght - 1].amount = 0;
            this.ppOrderDetails[lenght - 1].discountAmount = 0;
            this.ppOrderDetails[lenght - 1].discountAmountOriginal = 0;
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
                const idx: number = this.ppOrderDetails.length - 1;
                const nameTagIndex: string = nameTag + String(idx);
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(nameTagIndex);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
        } else if (select === 1) {
        }
    }

    copyRow(detail, checkCopy?) {
        if (checkCopy) {
            if (!this.getSelectionText()) {
                const addRow: any = Object.assign({}, detail);
                addRow.id = null;
                this.ppOrderDetails.push(addRow);
                this.updateSum();
                if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                    const lst = this.listIDInputDeatil;
                    const col = this.indexFocusDetailCol;
                    const row = this.ppOrderDetails.length - 1;
                    this.indexFocusDetailRow = row;
                    setTimeout(function() {
                        const element: HTMLElement = document.getElementById(lst[col] + row);
                        if (element) {
                            element.focus();
                        }
                    }, 0);
                }
            }
        } else {
            if (this.contextMenu.isCopy) {
                const addRow: any = Object.assign({}, detail);
                addRow.id = null;
                this.ppOrderDetails.push(addRow);
                this.updateSum();
                if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                    const lst = this.listIDInputDeatil;
                    const col = this.indexFocusDetailCol;
                    const row = this.ppOrderDetails.length - 1;
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
    }

    removeRow(detail: object) {
        if (this.select === 1) {
            this.viewVouchersSelected.splice(this.viewVouchersSelected.indexOf(detail), 1);
        } else {
            this.ppOrderDetails.splice(this.ppOrderDetails.indexOf(detail), 1);
            if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                // vì còn trường hợp = 0
                if (this.ppOrderDetails.length > 0) {
                    let row = 0;
                    if (this.indexFocusDetailRow > this.ppOrderDetails.length - 1) {
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
        this.updateSum();
    }

    edit() {
        event.preventDefault();
        this.pporderService.checkReferencesCount(this.ppOrder.id).subscribe(response => {
            if (!this.isEdit) {
                this.isEdit = true;
                this.isMove = false;
            }
            this.copy();
            if (response.body.message !== 'success') {
                this.modalRef = this.modalService.open(this.referencePPOrder, { backdrop: 'static' });
            }
        });
    }

    addNew($event = null) {
        this.isEdit = true;
        this.initValues();
        this.genNewVoucerCode();
    }

    copyAndNew() {
        this.viewVouchersSelected = [];
        this.isCheckPopup = true;
        this.ppOrder.id = null;
        this.ppOrder.status = 0;
        for (const item of this.ppOrderDetails) {
            item.quantityReceipt = 0;
        }
        this.ppOrderDetails.forEach(item => (item.id = null));
        this.isEdit = true;
        this.genNewVoucerCode();
    }

    print($event) {
        $event.preventDefault();
        this.exportPdf(false, REPORT.BieuMau);
    }

    openModal(content) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(content, { backdrop: 'static' });
    }

    save(isNew = false) {
        event.preventDefault();
        if ((this.isCreateUrl && !this.utilsService.isShowPopup) || (this.isEditUrl && !this.utilsService.isShowPopup)) {
            this.ppOrder.ppOrderDetails = this.ppOrderDetails;
            for (const details of this.ppOrderDetails) {
                // Check % thue = null va co tien thue
                if (!details.vatRate && details.vatAmountOriginal && !this.vatValid) {
                    this.vatValidNUllCase = true;
                    // Check % thue = 0 va co tien thue
                    if (details.vatRate === 0) {
                        this.vatValidNUllCase = false;
                    }
                    this.openModal(this.vatValidModal);
                    return false;
                }
            }
            this.vatValid = false;
            if (!this.checkError()) {
                this.isClosing = false;
                if (this.modalRef) {
                    this.modalRef.close();
                }
                return;
            }
            this.isSaving = true;
            const data = { ppOrder: this.ppOrder, viewVouchers: this.viewVouchersSelected };
            if (this.ppOrder.id) {
                this.pporderService.update(data).subscribe(
                    res => {
                        this.handleSuccessResponse(res, isNew);
                    },
                    error => {
                        this.handleError(error);
                    }
                );
            } else {
                this.pporderService.create(data).subscribe(
                    res => {
                        this.handleSuccessResponse(res, isNew);
                    },
                    error => {
                        this.handleError(error);
                    }
                );
            }
        }
    }

    checkError(): boolean {
        if (!this.ppOrder.no || !this.ppOrder.date) {
            this.toastrService.error(this.translateService.instant('ebwebApp.pporder.error.required'));
            return false;
        }
        if (!this.utilsService.checkNoBook(this.ppOrder.no)) {
            return false;
        }
        if (this.ppOrder.companyTaxCode) {
            if (!this.utilsService.checkMST(this.ppOrder.companyTaxCode)) {
                this.toastrService.error(
                    this.translateService.instant('ebwebApp.mCReceipt.error.taxCodeInvalid'),
                    this.translateService.instant('ebwebApp.mCReceipt.error.error')
                );
                return false;
            }
        }
        if (!this.ppOrderDetails || !this.ppOrderDetails.length) {
            this.toastrService.error(
                this.translateService.instant('ebwebApp.pporder.error.nullDetail'),
                this.translateService.instant('ebwebApp.pporder.error.error')
            );
            return false;
        }
        let orderPriority = 0;
        for (const item of this.ppOrderDetails) {
            if (item.discountRate && item.discountRate > 100) {
                this.toastrService.error(this.translateService.instant('ebwebApp.pporder.error.discountRate100'));
                return false;
            }
            item.orderPriority = orderPriority;
            orderPriority++;
        }
        return true;
    }

    handleSuccessResponse(res, isNew) {
        this.isCheckPopup = false;
        this.isSaving = false;
        if (!this.ppOrder.id) {
            this.toastrService.success(this.translateService.instant('ebwebApp.pporder.created'));
        } else {
            this.toastrService.success(this.translateService.instant('ebwebApp.pporder.updated'));
        }
        if (isNew) {
            this.initValues();
            this.genNewVoucerCode();
        } else {
            this.isEdit = false;
            this.isSaved = true;
            this.ppOrder.id = res.body.ppOrder.id;
            const searchData = JSON.parse(this.searchData);
            this.pporderService
                .findByRowNumByID({
                    searchDTO: JSON.stringify({
                        currency: searchData.currency && searchData.currency.currencyCode ? searchData.currency.currencyCode : '',
                        fromDate: searchData.fromDate || '',
                        toDate: searchData.toDate || '',
                        status: searchData.status !== 0 ? searchData.status : '',
                        accountingObject:
                            searchData.accountingObject && searchData.accountingObject.id ? searchData.accountingObject.id : '',
                        employee:
                            searchData.accountingObjectEmployee && searchData.accountingObjectEmployee.id
                                ? searchData.accountingObjectEmployee.id
                                : '',
                        searchValue: searchData.searchValue || ''
                    }),
                    id: this.ppOrder.id
                })
                .subscribe((res2: HttpResponse<any>) => {
                    if (res2.body && res2.body !== -1) {
                        this.rowNum = res2.body;
                        this.totalItems = this.totalItems > this.rowNum ? this.totalItems : this.rowNum;
                    }
                });
            this.eventManager.broadcast({ name: 'PPOrderListModification' });
            this.copy();
            this.close();
        }
    }

    handleError(err) {
        this.isSaving = false;
        this.isClosing = false;
        this.close();
        if (err && err.error) {
            this.toastrService.error(this.translateService.instant(`ebwebApp.pporder.${err.error.message}`));
        }
    }

    move(direction: number) {
        if ((direction === -1 && this.rowNum === 1) || (direction === 1 && this.rowNum === this.totalItems)) {
            return;
        }
        this.rowNum += direction;
        const searchData = JSON.parse(this.searchData);
        // goi service get by row num
        return this.pporderService
            .findByRowNum({
                searchDTO: JSON.stringify({
                    currency: searchData.currency && searchData.currency.currencyCode ? searchData.currency.currencyCode : '',
                    fromDate: searchData.fromDate || '',
                    toDate: searchData.toDate || '',
                    status: searchData.status !== 0 ? searchData.status : '',
                    accountingObject: searchData.accountingObject && searchData.accountingObject.id ? searchData.accountingObject.id : '',
                    employee:
                        searchData.accountingObjectEmployee && searchData.accountingObjectEmployee.id
                            ? searchData.accountingObjectEmployee.id
                            : '',
                    searchValue: searchData.searchValue || ''
                }),
                rowNum: this.rowNum
            })
            .subscribe(
                (res: HttpResponse<Pporder>) => {
                    this.ppOrder = res.body;
                    this.dataSession.rowNum = this.rowNum;
                    sessionStorage.setItem('DMHDataSession', JSON.stringify(this.dataSession));
                    this.isEditUrl = true;
                    this.ppOrderDataSetup();
                    this.isMove = true;
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

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
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

    subcribeEvent() {
        this.eventManager.subscribe('selectViewVoucher', response => {
            if (this.isEdit) {
                this.viewVouchersSelected = response.content;
            }
        });
        this.eventManager.subscribe('afterAddNewRow', () => {
            this.ppOrderDetails[this.ppOrderDetails.length - 1].discountRate = 0;
            this.changeDiscountRate(this.ppOrderDetails.length - 1);
        });
        this.eventManager.subscribe('afterDeleteRow', () => this.updateSum());
    }

    onSetUpHolderMask() {
        this.receivedDateHolderMask = moment(this.ppOrder.deliverDate, 'DD/MM/YYYY').format('DD/MM/YYYY');
        this.dateHolderMask = moment(this.ppOrder.date, 'DD/MM/YYYY').format('DD/MM/YYYY');
    }

    updateReceivedDateHolder() {
        this.ppOrder.deliverDate = this.utilsService.formatStrDate(this.receivedDateHolder);
    }

    updateDateHolder() {
        this.ppOrder.date = this.utilsService.formatStrDate(this.dateHolder);
    }

    convertRateChange(index) {
        if (this.ppOrderDetails[index].formula === '*') {
            this.ppOrderDetails[index].mainQuantity = this.ppOrderDetails[index].quantity * this.ppOrderDetails[index].mainConvertRate;
            this.ppOrderDetails[index].mainUnitPrice =
                this.ppOrderDetails[index].unitPriceOriginal / this.ppOrderDetails[index].mainConvertRate;
        } else {
            this.ppOrderDetails[index].mainQuantity = this.ppOrderDetails[index].quantity / this.ppOrderDetails[index].mainConvertRate;
            this.ppOrderDetails[index].mainUnitPrice =
                this.ppOrderDetails[index].unitPriceOriginal * this.ppOrderDetails[index].mainConvertRate;
        }
        this.roundObject();
        this.updateSum();
    }

    mainQuantityChange(index) {
        if (this.ppOrderDetails[index].formula === '*') {
            this.ppOrderDetails[index].quantity = this.ppOrderDetails[index].mainQuantity / this.ppOrderDetails[index].mainConvertRate;
        } else {
            this.ppOrderDetails[index].quantity = this.ppOrderDetails[index].mainQuantity * this.ppOrderDetails[index].mainConvertRate;
        }
        this.ppOrderDetails[index].amountOriginal = this.ppOrderDetails[index].quantity * this.ppOrderDetails[index].unitPriceOriginal;
        this.ppOrderDetails[index].discountAmountOriginal =
            this.ppOrderDetails[index].amountOriginal * this.ppOrderDetails[index].discountRate / 100;
        this.changeVATRate(index);
    }

    mainUnitPriceChange(index) {
        if (this.ppOrderDetails[index].formula === '*') {
            this.ppOrderDetails[index].unitPriceOriginal =
                this.ppOrderDetails[index].mainUnitPrice * this.ppOrderDetails[index].mainConvertRate;
        } else {
            this.ppOrderDetails[index].unitPriceOriginal =
                this.ppOrderDetails[index].mainUnitPrice / this.ppOrderDetails[index].mainConvertRate;
        }
        this.ppOrderDetails[index].amountOriginal = this.ppOrderDetails[index].quantity * this.ppOrderDetails[index].unitPriceOriginal;
        this.ppOrderDetails[index].discountAmountOriginal =
            this.ppOrderDetails[index].amountOriginal * this.ppOrderDetails[index].discountRate / 100;
        this.changeVATRate(index);
    }

    selectUnitPriceOriginal(index: number) {
        if (this.currencyFormula === FORMULA.DIVIDE) {
            this.ppOrderDetails[index].unitPrice = this.ppOrderDetails[index].unitPriceOriginal / this.ppOrder.exchangeRate;
        } else {
            this.ppOrderDetails[index].unitPrice = this.ppOrderDetails[index].unitPriceOriginal * this.ppOrder.exchangeRate;
        }
        this.changeQuantity(index);
        if (this.ppOrderDetails[index].formula === FORMULA.DIVIDE) {
            this.ppOrderDetails[index].mainUnitPrice =
                this.ppOrderDetails[index].unitPriceOriginal * this.ppOrderDetails[index].mainConvertRate;
        } else {
            this.ppOrderDetails[index].mainUnitPrice =
                this.ppOrderDetails[index].unitPriceOriginal / this.ppOrderDetails[index].mainConvertRate;
        }
        this.roundObject();
    }

    discountAmountOriginalChange(index) {
        this.updateDiscountAmount(index);
        this.ppOrderDetails[index].discountRate =
            (this.ppOrderDetails[index].discountAmountOriginal ? this.ppOrderDetails[index].discountAmountOriginal : 0) /
            this.ppOrderDetails[index].amountOriginal *
            100;
        this.changeVATRate(index);
        this.updatePPOrder();
    }

    updateDiscountAmount(index: number) {
        if (this.currencyFormula === FORMULA.DIVIDE) {
            this.ppOrderDetails[index].discountAmount = this.ppOrderDetails[index].discountAmountOriginal / this.ppOrder.exchangeRate;
        } else {
            this.ppOrderDetails[index].discountAmount = this.ppOrderDetails[index].discountAmountOriginal * this.ppOrder.exchangeRate;
        }
    }

    vatAmountOriginalChange(index) {
        if (this.currencyFormula === FORMULA.DIVIDE) {
            this.ppOrderDetails[index].vatAmount = this.ppOrderDetails[index].vatAmountOriginal / this.ppOrder.exchangeRate;
        } else {
            this.ppOrderDetails[index].vatAmount = this.ppOrderDetails[index].vatAmountOriginal * this.ppOrder.exchangeRate;
        }
        for (const item of this.ppOrderDetails) {
            this.vatAmountOriginalSum = item.vatAmountOriginal;
            this.ppOrder.totalVATAmountOriginal = item.vatAmountOriginal;
            this.ppOrder.totalVATAmount = item.vatAmountOriginal;
        }
        this.updatePPOrder();
    }

    updateAmount(index) {
        if (this.currencyFormula === FORMULA.DIVIDE) {
            this.ppOrderDetails[index].amount = this.ppOrderDetails[index].amountOriginal / this.ppOrder.exchangeRate;
        } else {
            this.ppOrderDetails[index].amount = this.ppOrderDetails[index].amountOriginal * this.ppOrder.exchangeRate;
        }
    }

    changeTemplate(template: number) {
        this.template = template;
    }

    deletePPOrderAndReferences(id: string) {
        if (this.isEdit) {
            this.pporderService.deleteReferences(id).subscribe(
                res => {
                    if (res.body.message === 'success' && this.modalRef) {
                        this.modalRef.close();
                        for (const item of this.ppOrderDetails) {
                            item.quantityReceipt = 0;
                        }
                        this.quantityReceiptSum = 0;
                    }
                },
                error => {
                    console.log(error);
                }
            );
        } else {
            this.pporderService.deletePPOrderAndReferences(id).subscribe(
                res => {
                    if (res.body.message === 'success' && this.modalRef) {
                        this.modalRef.close();
                        this.closeAll();
                    }
                },
                error => {
                    console.log(error);
                }
            );
        }
    }

    openModel(content) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(content, { size: 'lg', backdrop: 'static' });
    }

    confirmDelete() {
        this.pporderService.deleteValidate(this.ppOrder.id).subscribe(
            response => {
                if (response.body.message === 'success') {
                    this.eventManager.broadcast({
                        name: 'PPOrderListModification',
                        content: 'Deleted an PPOrder'
                    });
                    this.modalRef.close();
                    this.toastrService.success(this.translateService.instant('ebwebApp.pporder.deleted'));
                    this.closeAll();
                } else {
                    this.modalRef.close();
                    this.modalRef = this.modalService.open(this.referencePPOrder, { backdrop: 'static' });
                }
            },
            error => {
                if (this.modalRef) {
                    this.modalRef.close();
                }
            }
        );
    }

    closeForm() {
        this.previousState(this.content);
    }

    saveAndNew() {
        event.preventDefault();
        this.save(true);
    }

    delete() {
        event.preventDefault();
        if (this.ppOrder && this.ppOrder.id) {
            this.openModel(this.deleteItem);
        }
    }

    // print($event) {
    //     $event.preventDefault();
    //     this.exportPdf(false, REPORT.BieuMau);
    // }

    exportPdf(isDownload: boolean, typeReports) {
        this.utilsService.getCustomerReportPDF(
            {
                id: this.ppOrder.id,
                typeID: TypeID.DON_MUA_HANG,
                typeReport: typeReports
            },
            isDownload
        );
        this.toastrService.success(
            this.translateService.instant('ebwebApp.mBDeposit.printing') +
                this.translateService.instant('ebwebApp.pporder.financialPaper') +
                '...',
            this.translateService.instant('ebwebApp.mBDeposit.message')
        );
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.ppOrderDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.ppOrder;
    }

    addDataToDetail() {
        this.ppOrderDetails = this.details ? this.details : this.ppOrderDetails;
        this.ppOrder = this.parent ? this.parent : this.ppOrder;
    }

    actionFocus(indexCol, indexRow) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
    }

    registerAddNewRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.addNewRow(this.select, true);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            this.removeRow(this.contextMenu.selectedData);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            this.copyRow(this.contextMenu.selectedData);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }
}
