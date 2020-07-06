import { AfterViewChecked, AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { ISAQuote } from 'app/shared/model/sa-quote.model';
import { SAQuoteService } from './sa-quote.service';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { PaymentClauseService } from 'app/entities/payment-clause';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { Irecord } from 'app/shared/model/record';
import { IDataSessionStorage } from 'app/shared/model/DataSessionStorage';
import { IType } from 'app/shared/model/type.model';
import { ISAQuoteDetails, SAQuoteDetails } from 'app/shared/model/sa-quote-details.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { ExpenseItemService } from 'app/entities/expense-item';
import { CostSetService } from 'app/entities/cost-set';
import { EMContractService } from 'app/entities/em-contract';
import { StatisticsCodeService } from 'app/entities/statistics-code';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { AccountDefaultService } from 'app/danhmuc/account-default';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { IMaterialGoods, IMaterialGoodsInStock } from 'app/shared/model/material-goods.model';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import * as moment from 'moment';
import { SO_LAM_VIEC, TypeID } from 'app/app.constants';
import { Principal } from 'app/core';
import { SaleDiscountPolicyService } from 'app/entities/sale-discount-policy';
import { ISaleDiscountPolicy, SaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { TranslateService } from '@ngx-translate/core';
import { ISearchVoucher } from 'app/shared/model/SearchVoucher';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { ROLE } from 'app/role.constants';
import { IMaterialQuantumDetails } from 'app/shared/model/material-quantum-details.model';
import { IMaterialGoodsConvertUnit } from 'app/shared/model/material-goods-convert-unit.model';
import { MaterialGoodsConvertUnitService } from 'app/entities/material-goods-convert-unit';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-sa-quote-update',
    templateUrl: './sa-quote-update.component.html',
    styleUrls: ['./sa-quote-update.component.css']
})
export class SAQuoteUpdateComponent extends BaseComponent implements OnInit, AfterViewInit, AfterViewChecked {
    TYPE_SA_QUOTE = 300;
    private _sAQuote: ISAQuote;
    isSaving: boolean;
    @ViewChild('contentUnRecord') public modalContentUnRecord: NgbModalRef;
    @ViewChild('content') public modalCloseComponent: NgbModalRef;
    @ViewChild('contentSave') contentSave;
    accountingObjects: IAccountingObject[];
    paymentClauses: IPaymentClause[];
    dateDp: any;
    finalDateDp: any;
    currencyCodes: string[];
    autoprinciples: IAutoPrinciple[];
    accountDefaults: string[] = [];
    autoPrinciple: IAutoPrinciple;
    employees: IAccountingObject[];
    accountingobjectbankaccounts: IAccountingObjectBankAccount[];
    expenseitems: IExpenseItem[];
    costsets: ICostSet[];
    organizationunits: IOrganizationUnit[];
    isRecord: boolean;
    emcontracts: IEMContract[];
    statisticsCodes: IStatisticsCode[];
    currencies: ICurrency[];
    isReadOnly: boolean;
    totalvatamount: number;
    totalvatamountoriginal: number;
    isCreateUrl: boolean;
    record_: Irecord;
    // right click declare
    contextmenu = { value: false };
    contextmenuX = { value: 0 };
    contextmenuY = { value: 0 };
    selectedDetail = { value: new SAQuoteDetails() };
    selectedDetailTax = { value: false };
    isShowDetail = { value: false };
    isShowDetailTax = { value: false };
    //  data storage provider
    page: number;
    itemsPerPage: number;
    pageCount: number;
    totalItems: number;
    rowNum: number;
    // sort
    predicate: any;
    reverse: any;
    searchVoucher: string;
    dataSession: IDataSessionStorage;
    // end sort
    statusVoucher: number;
    count: any;
    types: IType[];
    unitPriceOriginal: any[];
    totalMoney: any;
    totalMoneyOriginal: any;
    account: any;
    saleDiscountPolicys: SaleDiscountPolicy[];
    modalRef: NgbModalRef;
    viewVouchersSelected: any;
    eventSubscriber: Subscription;
    dataSearchVoucher: ISearchVoucher;
    isCheckCanDeactive: boolean;
    sAQuoteCopy: ISAQuote;
    sAQuoteDetailsCopy: ISAQuoteDetails[];
    currencyCode: any;
    typeReport: any;
    vatRates: any;
    currency: ICurrency;
    isMoreForm: boolean;
    isFromRef: boolean;
    materialGoodsConvertUnits: IMaterialGoodsConvertUnit[];
    // role
    ROLE_BaoGia = ROLE.BaoGia;
    ROLE_BaoGia_Xem = ROLE.BaoGia_Xem;
    ROLE_BaoGia_Them = ROLE.BaoGia_Them;
    ROLE_BaoGia_Sua = ROLE.BaoGia_Sua;
    ROLE_BaoGia_Xoa = ROLE.BaoGia_Xoa;
    ROLE_BaoGia_In = ROLE.BaoGia_In;
    reasonDefault = 'Bảng báo giá';
    isChangedReason: boolean;
    warningVatRate: number;
    isSaveAndAdd: boolean;
    isTGXKTT: boolean;
    newRow: any;
    template: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private sAQuoteService: SAQuoteService,
        private accountingObjectService: AccountingObjectService,
        private paymentClauseService: PaymentClauseService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private currencyService: CurrencyService,
        private bankAccountDetailService: BankAccountDetailsService,
        private autoPrincipleService: AutoPrincipleService,
        private expenseItemService: ExpenseItemService,
        private costSetService: CostSetService,
        private eMContractService: EMContractService,
        private statisticsCodeService: StatisticsCodeService,
        private organizationUnitService: OrganizationUnitService,
        private accountDefaultService: AccountDefaultService,
        private materialGoodsService: MaterialGoodsService,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private unitService: UnitService,
        private principal: Principal,
        private saleDiscountPolicyService: SaleDiscountPolicyService,
        private refModalService: RefModalService,
        private eventManager: JhiEventManager,
        private translate: TranslateService,
        private modalService: NgbModal,
        private materialGoodsConvertUnitService: MaterialGoodsConvertUnitService
    ) {
        super();
        this.dataSearchVoucher = JSON.parse(sessionStorage.getItem('sessionSearchVoucher'));
        this.searchVoucher = this.dataSearchVoucher ? sessionStorage.getItem('sessionSearchVoucher') : null;
        this.vatRates = [
            { value: 0, name: '0%' },
            { value: 1, name: '5%' },
            { value: 2, name: '10%' },
            { value: 3, name: 'KCT' },
            { value: 4, name: 'KTT' }
        ];
        this.getOnInit();
    }

    ngOnInit() {
        this.unitPriceOriginal = [];
        this.isMoreForm = false;
        this.isSaving = false;
        this.isChangedReason = false;
        this.newRow = false;
        this.paymentClauseService.getPaymentClauses().subscribe(
            (res: HttpResponse<IPaymentClause[]>) => {
                this.paymentClauses = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.materialGoodsService.queryForComboboxGood().subscribe(
            (res: HttpResponse<any>) => {
                this.materialGoodsInStock = res.body;
            },
            (res: HttpErrorResponse) => console.log(res.message)
        );
        this.registerRef();
        this.registerCategory();
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            if (response.content.size === undefined || response.content.size === null || response.content.size === 1) {
                this.registerComboboxSave(response);
            }
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = true;
            this.utilsService.setShowPopup(true);
            this.registerComboboxSave(response);
        });
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilsService.setShowPopup(false);
        });
        this.template = 1;
        // this.registerIsShowPopup();
    }

    // registerIsShowPopup() {
    //     this.utilsService.checkEvent.subscribe(res => {
    //         this.isShowPopup = res;
    //     });
    // }

    getOnInit() {
        this.activatedRoute.data.subscribe(({ sAQuote }) => {
            this.sAQuote = sAQuote;
            this.viewVouchersSelected = sAQuote.viewVouchers ? sAQuote.viewVouchers : [];
            this.sAQuoteDetails =
                this.sAQuote.sAQuoteDetails === undefined
                    ? []
                    : this.sAQuote.sAQuoteDetails.sort((a, b) => (a.orderPriority > b.orderPriority ? 1 : -1));
            // check url new Return True or false
            // if (this.sAQuoteDetails && this.sAQuoteDetails.length > 0) {
            //     for (let i = 0; i < this.sAQuoteDetails.length; i++) {
            //
            //         this.listUnits.push([]);
            //     }
            // }
            this.isCreateUrl = window.location.href.includes('/sa-quote/new');
            this.isFromRef = this.router.url.includes('/from-ref');
            // isReadOnly
            if (this.sAQuote.id && !this.isCreateUrl) {
                this.isReadOnly = true;
                this.isCheckCanDeactive = false;
            } else {
                this.isReadOnly = false;
                this.isCheckCanDeactive = true;
            }
            this.materialGoodsConvertUnitService
                .getAllMaterialGoodsConvertUnits()
                .subscribe((resData: HttpResponse<IMaterialGoodsConvertUnit[]>) => {
                    this.materialGoodsConvertUnits = resData.body;
                    this.unitService.convertRateForMaterialGoodsComboboxCustom().subscribe((res: HttpResponse<IUnit[]>) => {
                        this.units = res.body;
                        if (this.sAQuoteDetails.length > 0) {
                            for (let i = 0; i < this.sAQuoteDetails.length; i++) {
                                this.sAQuoteDetails[i].units = this.units.filter(
                                    n => n.materialGoodsID === this.sAQuoteDetails[i].materialGoods.id
                                );
                                this.sAQuoteDetails[i].unit = this.sAQuoteDetails[i].units.find(
                                    a => a.id === this.sAQuoteDetails[i].unit.id
                                );
                                const mainUnitItem = this.sAQuoteDetails[i].units.find(a => a.id === this.sAQuoteDetails[i].mainUnit.id);
                                if (mainUnitItem) {
                                    this.sAQuoteDetails[i].mainUnit.id = mainUnitItem.id;
                                    this.sAQuoteDetails[i].mainUnit = mainUnitItem;
                                }
                                if (this.sAQuoteDetails[i].unit !== this.sAQuoteDetails[i].mainUnit) {
                                    this.unitPriceOriginal.push([]);
                                    const materialGoodsConvertUnit = this.materialGoodsConvertUnits.find(
                                        x =>
                                            x.materialGoodsID === this.sAQuoteDetails[i].materialGoods.id &&
                                            x.unitID === this.sAQuoteDetails[i].unit.id
                                    );
                                    if (materialGoodsConvertUnit) {
                                        if (materialGoodsConvertUnit.fixedSalePrice) {
                                            this.unitPriceOriginal[i].push({ data: materialGoodsConvertUnit.fixedSalePrice });
                                        }
                                        if (materialGoodsConvertUnit.salePrice1) {
                                            this.unitPriceOriginal[i].push({ data: materialGoodsConvertUnit.salePrice1 });
                                        }
                                        if (materialGoodsConvertUnit.salePrice2) {
                                            this.unitPriceOriginal[i].push({ data: materialGoodsConvertUnit.salePrice2 });
                                        }
                                        if (materialGoodsConvertUnit.salePrice3) {
                                            this.unitPriceOriginal[i].push({ data: materialGoodsConvertUnit.salePrice3 });
                                        }
                                    }
                                }
                            }
                        }
                    });
                });
            this.accountingObjectService.getAllDTO().subscribe(
                (res: HttpResponse<IAccountingObject[]>) => {
                    this.employees = res.body
                        .filter(x => x.isEmployee)
                        .sort((a, b) => (a.accountingObjectCode > b.accountingObjectCode ? 1 : -1));
                    this.accountingObjects = res.body
                        .filter(x => x.objectType === 1 || x.objectType === 2)
                        .sort((a, b) => (a.accountingObjectCode > b.accountingObjectCode ? 1 : -1));
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
            if (this.sAQuote.id !== undefined) {
                this.principal.identity().then(account => {
                    this.account = account;
                    if (account && account.organizationUnit && account.organizationUnit.taxCalculationMethod === 1) {
                        this.isTGXKTT = false;
                    } else {
                        this.isTGXKTT = true;
                    }
                    if (this.account) {
                        this.currencyCode = this.account.organizationUnit.currencyID;
                        this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                            this.currencies = res.body.sort((a, b) => (a.currencyCode > b.currencyCode ? 1 : -1));
                            this.currency = this.currencies.find(n => n.currencyCode === this.sAQuote.currencyID);
                            // this.selectedChangedCurrency();
                        });
                        this.calculateTotal();
                    }
                    this.isEnter = this.account.systemOption.filter(n => n.code === 'TCKHAC_Enter')[0].data === '1';
                    this.eventManager.broadcast({
                        name: 'checkEnter',
                        content: this.isEnter
                    });
                });
                if (this.isCreateUrl) {
                    this.utilsService
                        .getGenCodeVoucher({
                            typeGroupID: 30,
                            displayOnBook: 2
                        })
                        .subscribe((res: HttpResponse<string>) => {
                            console.log(res.body);
                            this.sAQuote.no = res.body;
                        });
                } else {
                    this.copyObject();
                    this.sAQuoteService
                        .getIndexRow({
                            id: this.sAQuote.id,
                            searchVoucher: this.searchVoucher ? this.searchVoucher : null
                        })
                        .subscribe(
                            (res: any) => {
                                this.rowNum = res.body[0];
                                this.totalItems = res.body[1];
                            },
                            (res: HttpErrorResponse) => this.onError(res.message)
                        );
                }
            } else {
                this.principal.identity().then(account => {
                    // Set default theo systemOption
                    this.account = account;
                    if (this.account) {
                        if (account && account.organizationUnit && account.organizationUnit.taxCalculationMethod === 1) {
                            this.isTGXKTT = false;
                        } else {
                            this.isTGXKTT = true;
                        }
                        this.getReasonDefault();
                        this.sAQuote.date = this.utilsService.ngayHachToan(account);
                        this.currencyCode = this.account.organizationUnit.currencyID;
                        this.sAQuote.currencyID = this.currencyCode;
                        if (this.account.organizationUnit && this.account.organizationUnit.currencyID) {
                            this.currencyService.findAllActive().subscribe((res: HttpResponse<ICurrency[]>) => {
                                this.currencies = res.body.sort((a, b) => (a.currencyCode > b.currencyCode ? 1 : -1));
                                // this.currency = this.currencies.find(n => n.currencyCode === this.sAQuote.currencyID);
                                this.selectedChangedCurrency();
                            });
                        }
                        this.utilsService
                            .getGenCodeVoucher({
                                typeGroupID: 30,
                                displayOnBook: 2
                            })
                            .subscribe((res: HttpResponse<string>) => {
                                this.sAQuote.no = res.body;
                                this.valueDefault();
                                this.sAQuoteCopy = Object.assign({}, this.sAQuote);
                                this.sAQuoteDetailsCopy = this.sAQuoteDetails.map(object => ({ ...object }));
                            });
                    }
                    this.isEnter = this.account.systemOption.filter(n => n.code === 'TCKHAC_Enter')[0].data === '1';
                    this.eventManager.broadcast({
                        name: 'checkEnter',
                        content: this.isEnter
                    });
                });
            }
        });
    }

    getReasonDefault() {
        this.sAQuote.reason = this.sAQuote.reason ? this.sAQuote.reason : this.reasonDefault;
    }

    previousState() {
        window.history.back();
    }

    // ham lui, tien
    previousEdit() {
        if (this.rowNum === this.totalItems) {
            return;
        }
        this.rowNum++;
        this.sAQuoteService
            .findByRowNum({
                searchVoucher: this.searchVoucher ? this.searchVoucher : '',
                rowNum: this.rowNum
            })
            .subscribe(
                (res: HttpResponse<ISAQuote>) => {
                    this.sAQuote = res.body;
                    this.sAQuoteDetails = this.sAQuote.sAQuoteDetails;
                    this.router.navigate(['/sa-quote', this.sAQuote.id, 'edit']);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    nextEdit() {
        if (this.rowNum === 1) {
            return;
        }
        this.rowNum--;
        this.sAQuoteService
            .findByRowNum({
                searchVoucher: this.searchVoucher ? this.searchVoucher : '',
                rowNum: this.rowNum
            })
            .subscribe(
                (res: HttpResponse<ISAQuote>) => {
                    this.sAQuote = res.body;
                    this.sAQuoteDetails = this.sAQuote.sAQuoteDetails;
                    this.router.navigate(['/sa-quote', this.sAQuote.id, 'edit']);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    // end of lui tien
    // xu ly delete row (ctr +delete)
    keyPress(value: number, select: number) {
        if (select === 0) {
            this.sAQuoteDetails.splice(value, 1);
        } else if (select === 1) {
        }
    }

    copyRow(detail) {
        if (!this.getSelectionText()) {
            const detailCopy: any = Object.assign({}, detail);
            detailCopy.id = null;
            this.sAQuoteDetails.push(detailCopy);
            // this.sumAllList();
        }
    }

    addNewRow(event: any, select: number) {
        if (select === 0) {
            this.sAQuoteDetails.push(new SAQuoteDetails());
            this.unitPriceOriginal.push([]);
            const count = this.sAQuoteDetails.length;
            this.sAQuoteDetails[count - 1].vATAmount = 0;
            this.sAQuoteDetails[count - 1].unitPrice = 0;
            this.sAQuoteDetails[count - 1].unitPriceOriginal = 0;
            this.sAQuoteDetails[count - 1].mainUnitPrice = 0;
            this.sAQuoteDetails[count - 1].mainConvertRate = 1;
            this.sAQuoteDetails[count - 1].amount = 0;
            this.sAQuoteDetails[count - 1].discountAmount = 0;
            this.sAQuoteDetails[count - 1].materialGoods = null;
            const nameTag: string = event.srcElement.id;
            const idx: number = this.sAQuoteDetails.length - 1;
            const nameTagIndex: string = nameTag + String(idx);
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(nameTagIndex);
                if (element) {
                    element.focus();
                }
            }, 0);
            this.contextmenu.value = false;
        } else if (select === 1) {
        }
    }

    doubleClickRow(e: any, select: number) {
        if (select === this.sAQuoteDetails.length - 1) {
            const cellCurrent = e.path['0'].closest('td').cellIndex;
            const cellMax = document.getElementsByTagName('tbody')[0].children[select].childElementCount;
            if (cellCurrent === cellMax - 1) {
                this.newRow = true;
            }
        }
        if ((this.isEnter && select === this.sAQuoteDetails.length - 1) || this.newRow) {
            this.sAQuoteDetails.push(new SAQuoteDetails());
            this.unitPriceOriginal.push([]);
            const count = this.sAQuoteDetails.length;
            this.newRow = false;
            this.sAQuoteDetails[count - 1].vATAmount = 0;
            this.sAQuoteDetails[count - 1].unitPrice = 0;
            this.sAQuoteDetails[count - 1].unitPriceOriginal = 0;
            this.sAQuoteDetails[count - 1].mainUnitPrice = 0;
            this.sAQuoteDetails[count - 1].mainConvertRate = 1;
            this.sAQuoteDetails[count - 1].amount = 0;
            this.sAQuoteDetails[count - 1].discountAmount = 0;
            this.sAQuoteDetails[count - 1].materialGoods = null;
            let nameTag: string = 'materialGoodsID';
            select++;
            nameTag = nameTag + select;
            // const idx: number = this.sAQuoteDetails.length - 1;
            // const nameTagIndex: string = nameTag + String(idx);
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(nameTag);
                if (element) {
                    element.focus();
                }
            }, 0);
            this.contextmenu.value = false;
        }
        if (this.isEnter && select < this.sAQuoteDetails.length - 1) {
            const inputs = document.getElementsByTagName('input');
            const indexInput = 18 + 19 * (select + 1) + 1;
            for (let i = 0; i < inputs.length; i++) {
                if (inputs[i].tabIndex === indexInput) {
                    setTimeout(() => {
                        inputs[i].focus();
                    }, 0);
                    break;
                }
            }
        }
    }

    // right click event
    // activates the menu with the coordinates
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
        if (!this.isReadOnly) {
            this.utilsService.onrightClick(event, eventData, selectedObj, isShowTab1, isShowTab2, contextmenu, x, y, select);
            console.log(event);
        } else {
            return;
        }
    }

    // disables the menu
    disableContextMenu() {
        this.contextmenu.value = false;
    }

    //  end of right click
    @ebAuth(['ROLE_ADMIN', ROLE.BaoGia_Them, ROLE.BaoGia_Sua])
    save(isNew = false) {
        event.preventDefault();
        this.fillToSave();
        if (!this.utilsService.isShowPopup) {
            if (this.checkError()) {
                if (this.sAQuoteDetails.filter(x => !x.vATRate && x.vATRate !== 0 && x.vATAmountOriginal > 0).length > 0) {
                    this.warningVatRate = 1;
                } else if (this.sAQuoteDetails.filter(x => x.vATRate !== 1 && x.vATRate !== 2 && x.vATAmountOriginal > 0).length > 0) {
                    this.warningVatRate = 2;
                }
                if (this.warningVatRate === 1 || this.warningVatRate === 2) {
                    this.isSaveAndAdd = false;
                    this.modalRef = this.modalService.open(this.contentSave, { backdrop: 'static' });
                } else {
                    this.saveAll();
                }
            } else {
                this.closeModal();
            }
        }
    }

    saveAll() {
        if (this.sAQuote.id !== undefined) {
            this.subscribeToSaveResponse(this.sAQuoteService.update(this.sAQuote));
        } else {
            this.subscribeToSaveResponse(this.sAQuoteService.create(this.sAQuote));
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoGia_Them])
    saveAndNew() {
        event.preventDefault();
        this.fillToSave();
        if (!this.utilsService.isShowPopup) {
            if (this.checkError()) {
                if (this.sAQuote.id !== undefined) {
                    this.subscribeToSaveResponseAndContinue(this.sAQuoteService.update(this.sAQuote));
                } else {
                    this.subscribeToSaveResponseAndContinue(this.sAQuoteService.create(this.sAQuote));
                }
            } else {
                this.closeModal();
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoGia_Them])
    copyAndNew() {
        event.preventDefault();
        if (!this.utilsService.isShowPopup) {
            this.copyObject();
            this.router.navigate(['sa-quote/new', { id: this.sAQuote.id }]);
        }
    }

    copyObject() {
        this.sAQuoteDetails = this.sAQuote.id ? this.sAQuote.sAQuoteDetails : this.sAQuoteDetails;
        this.sAQuoteCopy = Object.assign({}, this.sAQuote);
        this.sAQuoteDetailsCopy = this.sAQuoteDetails.map(object => ({ ...object }));
    }

    fillToSave() {
        this.isSaving = true;

        this.sAQuote.typeId = 300;
        // this.sAQuote.totalDiscountAmountOriginal = 3111;
        // this.sAQuote.totalDiscountAmount = 2111;
        this.sAQuote.sAQuoteDetails = this.sAQuoteDetails;
        console.log('saDetails: ' + JSON.stringify(this.sAQuote.sAQuoteDetails));
        this.sAQuote.viewVouchers = this.viewVouchersSelected;
        // check is url new
        if (this.isCreateUrl && this.sAQuote.id) {
            this.sAQuote.id = undefined;
            for (let i = 0; i < this.sAQuoteDetails.length; i++) {
                this.sAQuoteDetails[i].id = undefined;
            }
        }
    }

    checkError(): boolean {
        if (this.sAQuoteDetails.length === 0) {
            // Null phần chi tiết
            this.toastr.error(
                this.translate.instant('ebwebApp.mCReceipt.error.nullDetail'),
                this.translate.instant('ebwebApp.mCReceipt.error.error')
            );
            return false;
        } else {
            for (let i = 0; i < this.sAQuoteDetails.length; i++) {
                if (!this.sAQuoteDetails[i].materialGoods) {
                    this.toastr.error(this.translate.instant('global.data.null'), this.translate.instant('ebwebApp.mCReceipt.error.error'));
                    return false;
                }
            }
        }
        if (!this.sAQuote.no) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.nullNo'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        } else if (!this.utilsService.checkNoBook(this.sAQuote.no, this.account)) {
            return false;
        } else if (!this.sAQuote.date) {
            this.toastr.error(
                this.translate.instant('ebwebApp.mBTellerPaper.error.nullDate'),
                this.translate.instant('ebwebApp.mBTellerPaper.error.error')
            );
            return false;
        } else if (!this.checkDiscountRateLessThan100()) {
            return false;
        } else if (!this.checkEmailValidate(this.sAQuote.contactEmail)) {
            return false;
        }
        return true;
    }

    checkEmailValidate(email: string): boolean {
        if (email && !email.includes('@')) {
            this.toastr.error(
                this.translate.instant('ebwebApp.sAQuote.error.emailNotValid'),
                this.translate.instant('ebwebApp.sAQuote.error.error')
            );
            return false;
        } else {
            return true;
        }
    }

    checkDiscountRateLessThan100() {
        const dt = this.sAQuoteDetails.find(n => n.discountRate > 100);
        if (dt) {
            this.toastr.error(
                this.translate.instant('ebwebApp.sAQuote.error.vatratemustlessthan100'),
                this.translate.instant('ebwebApp.sAQuote.error.error')
            );
            return false;
        }
        return true;
    }

    // typeReport:number;
    exportPdf(isDownload: boolean, typeReport: number) {
        this.typeReport = typeReport;
        this.sAQuoteService
            .getCustomerReport({
                //  id: this.sAQuotes[0].id,
                //  typeID: 0
                id: this.sAQuote.id,
                typeID: this.sAQuote.typeId,
                typeReport: this.typeReport
            })
            .subscribe(response => {
                //  this.showReport(response);
                const file = new Blob([response.body], { type: 'application/pdf' });
                const fileURL = window.URL.createObjectURL(file);

                if (isDownload) {
                    const link = document.createElement('a');
                    document.body.appendChild(link);
                    link.download = fileURL;
                    link.setAttribute('style', 'display: none');
                    const name = 'ten_bao_cao.pdf';
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

                    //  add a load listener to the window so that the title gets changed on page load
                    newWin.addEventListener('load', function() {
                        newWin.document.title = result.replace(/"/g, '');
                        //  this.router.navigate(['/report/buy']);
                    });
                }
            });
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoGia_Sua])
    edit() {
        event.preventDefault();
        if (!this.utilsService.isShowPopup) {
            this.sAQuoteService.checkRelateVoucher({ id: this.sAQuote.id }).subscribe((response: HttpResponse<boolean>) => {
                if (response.body) {
                    this.modalRef = this.modalService.open(this.modalContentUnRecord, { backdrop: 'static' });
                } else {
                    this.isEdit = true;
                    this.isReadOnly = false;
                    this.isCheckCanDeactive = true;
                    this.copyObject();
                }
            });
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoGia_Them])
    addNew(event) {
        event.preventDefault();
        if (!this.utilsService.isShowPopup) {
            this.router.navigate(['/sa-quote/new']);
        }
    }

    calculateTotal() {
        if (this.sAQuote.totalAmount) {
            this.sAQuote.total =
                this.sAQuote.totalAmount -
                (this.sAQuote.totalDiscountAmount ? this.sAQuote.totalDiscountAmount : 0) +
                (this.sAQuote.totalVATAmount ? this.sAQuote.totalVATAmount : 0);
        }
        if (this.sAQuote.totalAmountOriginal) {
            this.sAQuote.totalOriginal =
                this.sAQuote.totalAmountOriginal -
                (this.sAQuote.totalDiscountAmountOriginal ? this.sAQuote.totalDiscountAmountOriginal : 0) +
                (this.sAQuote.totalVATAmountOriginal ? this.sAQuote.totalVATAmountOriginal : 0);
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.sAQuote = this.convertDateFromServer(res.body.sAQuote);
                    this.isReadOnly = true;
                    this.isCheckCanDeactive = false;
                    this.closeModal();
                    this.calculateTotal();
                    this.isEdit = false;
                    this.router.navigate(['/sa-quote', res.body.sAQuote.id, 'edit']);
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                    this.closeModal();
                } else if (res.body.status === 2) {
                    this.updateGenCodeFailed();
                    this.closeModal();
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private subscribeToSaveResponseAndContinue(result: Observable<HttpResponse<ISAQuote>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                    this.sAQuote = this.convertDateFromServer(res.body.sAQuote);
                    this.isCheckCanDeactive = false;
                    this.router.navigate(['/sa-quote', res.body.sAQuote.id, 'edit']).then(() => {
                        this.isCheckCanDeactive = false;
                        this.router.navigate(['/sa-quote', 'new']);
                    });
                } else if (res.body.status === 1) {
                    this.noVoucherExist();
                    this.closeModal();
                } else if (res.body.status === 2) {
                    this.updateGenCodeFailed();
                    this.closeModal();
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private updateGenCodeFailed() {
        this.toastr.error(
            this.translate.instant('ebwebApp.mBTellerPaper.error.noGenCodeUpdatefailed'),
            this.translate.instant('ebwebApp.mBTellerPaper.error.error')
        );
    }

    private noVoucherExist() {
        this.toastr.error(
            this.translate.instant('global.data.noVocherAlreadyExist'),
            this.translate.instant('ebwebApp.mBTellerPaper.error.error')
        );
    }

    private noVoucherLimited() {
        this.toastr.error(
            this.translate.instant('ebwebApp.mBTellerPaper.error.noVoucherLimited'),
            this.translate.instant('ebwebApp.mBTellerPaper.error.error')
        );
    }

    private convertDateFromServer(res: any): any {
        res.date = res.date != null ? moment(res.date) : null;
        res.finalDate = res.finalDate != null ? moment(res.finalDate) : null;
        return res;
    }

    private onSaveSuccess() {
        this.isSaving = false;
        if (this.sAQuote.id) {
            this.toastr.success(
                this.translate.instant('ebwebApp.sAQuote.updated'),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
        } else {
            this.toastr.success(
                this.translate.instant('ebwebApp.sAQuote.created'),
                this.translate.instant('ebwebApp.mCReceipt.home.message')
            );
        }
    }

    valueDefault() {
        if (!this.sAQuote.totalAmount) {
            this.sAQuote.totalAmount = 0;
        }
        if (!this.sAQuote.totalVATAmount) {
            this.sAQuote.totalVATAmount = 0;
        }
        if (!this.sAQuote.totalDiscountAmount) {
            this.sAQuote.totalDiscountAmount = 0;
        }
        if (!this.sAQuote.total) {
            this.sAQuote.total = 0;
        }
        if (!this.sAQuote.totalAmountOriginal) {
            this.sAQuote.totalAmountOriginal = 0;
        }
        if (!this.sAQuote.totalVATAmountOriginal) {
            this.sAQuote.totalVATAmountOriginal = 0;
        }
        if (!this.sAQuote.totalDiscountAmountOriginal) {
            this.sAQuote.totalDiscountAmountOriginal = 0;
        }
        if (!this.sAQuote.totalOriginal) {
            this.sAQuote.totalOriginal = 0;
        }
    }

    private onSaveError() {
        this.isSaving = false;
        this.closeModal();
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackAccountingObjectById(index: number, item: IAccountingObject) {
        return item.id;
    }

    trackPaymentClauseById(index: number, item: IPaymentClause) {
        return item.id;
    }

    get sAQuote() {
        return this._sAQuote;
    }

    set sAQuote(sAQuote: ISAQuote) {
        this._sAQuote = sAQuote;
    }

    selectedChangedAccountingObject() {
        if (this.sAQuote.accountingObject === undefined || this.sAQuote.accountingObject === null) {
            this.sAQuote.accountingObjectName = null;
            this.sAQuote.accountingObjectAddress = null;
            this.sAQuote.companyTaxCode = null;
            this.sAQuote.contactName = null;
            this.sAQuote.reason = this.reasonDefault;
        } else {
            this.sAQuote.accountingObjectName = this.sAQuote.accountingObject.accountingObjectName;
            this.sAQuote.accountingObjectAddress = this.sAQuote.accountingObject.accountingObjectAddress;
            this.sAQuote.companyTaxCode = this.sAQuote.accountingObject.taxCode;
            this.sAQuote.contactName = this.sAQuote.accountingObject.contactName;
            if (!this.isChangedReason) {
                this.sAQuote.reason = this.reasonDefault + ' của ' + this.sAQuote.accountingObjectName;
            }
        }
    }

    selectedChangedCurrency() {
        if (this.sAQuote.currencyID) {
            this.currency = this.currencies.find(n => n.currencyCode === this.sAQuote.currencyID);
            this.sAQuote.exchangeRate = this.currency.exchangeRate;
        }
        this.changeExchangeRate();
    }

    setValueDefault(index) {
        this.sAQuoteDetails[index].quantity = 0;
        this.sAQuoteDetails[index].mainQuantity = 0;
        this.sAQuoteDetails[index].amountOriginal = 0;
        this.sAQuoteDetails[index].discountRate = 0;
        this.sAQuoteDetails[index].discountAmountOriginal = 0;
        this.sAQuoteDetails[index].vATAmountOriginal = 0;
    }

    selectMaterialGood(index) {
        this.saleDiscountPolicyService
            .findByMaterialGoodsID({
                id: this.sAQuoteDetails[index].materialGoods.id
            })
            .subscribe(res => {
                this.saleDiscountPolicys = res.body;
                this.sAQuoteDetails[index].materialGoods.saleDiscountPolicy = this.saleDiscountPolicys;
                this.setValueDefault(index);
                this.sAQuoteDetails[index].discountRate = this.sAQuoteDetails[index].materialGoods.saleDiscountRate
                    ? this.sAQuoteDetails[index].materialGoods.saleDiscountRate
                    : 0;
                this.checkSaleDiscountPolicy(index);
            });
        // this.sAQuoteDetails[index].materialGoods = this.materialGood;
        this.sAQuoteDetails[index].description = this.sAQuoteDetails[index].materialGoods.materialGoodsName;
        this.sAQuoteDetails[this.currentRow].vATRate = this.sAQuoteDetails[this.currentRow].materialGoods.vatTaxRate;
        this.unitPriceOriginal[index] = [];
        if (this.sAQuoteDetails[index].materialGoods.fixedSalePrice) {
            this.sAQuoteDetails[index].unitPriceOriginal = this.sAQuoteDetails[index].materialGoods.fixedSalePrice;
            this.sAQuoteDetails[index].unitPrice = this.sAQuoteDetails[index].materialGoods.fixedSalePrice;
            this.unitPriceOriginal[index].push({ data: this.sAQuoteDetails[index].materialGoods.fixedSalePrice });
        }
        if (this.sAQuoteDetails[index].materialGoods.salePrice1) {
            this.unitPriceOriginal[index].push({ data: this.sAQuoteDetails[index].materialGoods.salePrice1 });
        }
        if (this.sAQuoteDetails[index].materialGoods.salePrice2) {
            this.unitPriceOriginal[index].push({ data: this.sAQuoteDetails[index].materialGoods.salePrice2 });
        }
        if (this.sAQuoteDetails[index].materialGoods.salePrice3) {
            this.unitPriceOriginal[index].push({ data: this.sAQuoteDetails[index].materialGoods.salePrice3 });
        }
        this.unitService
            .convertRateForMaterialGoodsComboboxCustom({ materialGoodsId: this.sAQuoteDetails[index].materialGoods.id })
            .subscribe((res: HttpResponse<IUnit[]>) => {
                this.sAQuoteDetails[index].units = res.body;
                this.sAQuoteDetails[index].unit = this.sAQuoteDetails[index].units[0];
                this.sAQuoteDetails[index].mainUnit = this.sAQuoteDetails[index].units[0];
                this.selectUnit(index);
            });
    }

    checkSaleDiscountPolicy(index) {
        if (this.sAQuoteDetails[index].quantity && this.saleDiscountPolicys) {
            this.saleDiscountPolicys.forEach(saleDiscountPolicy => {
                if (
                    saleDiscountPolicy.quantityFrom <= this.sAQuoteDetails[index].quantity &&
                    this.sAQuoteDetails[index].quantity <= saleDiscountPolicy.quantityTo
                ) {
                    if (saleDiscountPolicy.discountType === 0) {
                        this.sAQuoteDetails[index].discountRate = saleDiscountPolicy.discountResult;
                        if (
                            (this.sAQuoteDetails[index].amountOriginal || this.sAQuoteDetails[index].amountOriginal === 0) &&
                            (this.sAQuoteDetails[index].discountRate || this.sAQuoteDetails[index].discountRate === 0)
                        ) {
                            this.sAQuoteDetails[index].discountAmountOriginal =
                                this.sAQuoteDetails[index].amountOriginal * this.sAQuoteDetails[index].discountRate / 100;
                            if (this.sAQuote.exchangeRate || this.sAQuote.exchangeRate === 0) {
                                this.sAQuoteDetails[index].discountAmount = this.round(
                                    this.sAQuoteDetails[index].discountAmountOriginal *
                                        (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                                    7
                                );
                            }
                        } else {
                            this.sAQuoteDetails[index].discountAmountOriginal = 0;
                            this.sAQuoteDetails[index].discountAmount = 0;
                        }
                    } else if (saleDiscountPolicy.discountType === 1) {
                        this.sAQuoteDetails[index].discountAmountOriginal = this.round(
                            saleDiscountPolicy.discountResult,
                            this.getAmountOriginalType()
                        );
                        this.sAQuoteDetails[index].discountAmount = this.round(
                            this.sAQuoteDetails[index].discountAmountOriginal *
                                (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                            7
                        );
                        this.sAQuoteDetails[index].discountRate = this.round(
                            this.sAQuoteDetails[index].discountAmountOriginal / this.sAQuoteDetails[index].amountOriginal,
                            5
                        );
                        console.log(this.sAQuoteDetails);
                    } else if (saleDiscountPolicy.discountType === 2) {
                        this.sAQuoteDetails[index].discountAmountOriginal = this.round(
                            saleDiscountPolicy.discountResult * this.sAQuoteDetails[index].quantity,
                            this.getAmountOriginalType()
                        );
                        this.sAQuoteDetails[index].discountAmount = this.round(
                            this.sAQuoteDetails[index].discountAmountOriginal *
                                (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                            7
                        );
                        this.sAQuoteDetails[index].discountRate = this.round(
                            this.sAQuoteDetails[index].discountAmountOriginal / this.sAQuoteDetails[index].amountOriginal,
                            5
                        );
                    }
                }
            });
        } else if (this.sAQuoteDetails[index].quantity) {
            this.sAQuoteDetails[index].discountAmountOriginal =
                this.sAQuoteDetails[index].amountOriginal * this.sAQuoteDetails[index].discountRate / 100;
            if (this.sAQuote.exchangeRate || this.sAQuote.exchangeRate === 0) {
                this.sAQuoteDetails[index].discountAmount = this.round(
                    this.sAQuoteDetails[index].discountAmountOriginal *
                        (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                    7
                );
            }
        }
    }

    selectUnit(index) {
        if (this.sAQuoteDetails[index].unit && this.sAQuoteDetails[index].mainUnit) {
            if (this.sAQuoteDetails[index].unit.id === this.sAQuoteDetails[index].mainUnit.id) {
                this.unitPriceOriginal[index] = [];
                if (this.sAQuoteDetails[index].materialGoods.fixedSalePrice) {
                    this.sAQuoteDetails[index].unitPriceOriginal = this.sAQuoteDetails[index].materialGoods.fixedSalePrice;
                    this.sAQuoteDetails[index].unitPrice = this.sAQuoteDetails[index].materialGoods.fixedSalePrice;
                    this.unitPriceOriginal[index].push({ data: this.sAQuoteDetails[index].materialGoods.fixedSalePrice });
                }
                if (this.sAQuoteDetails[index].materialGoods.salePrice1) {
                    this.unitPriceOriginal[index].push({ data: this.sAQuoteDetails[index].materialGoods.salePrice1 });
                }
                if (this.sAQuoteDetails[index].materialGoods.salePrice2) {
                    this.unitPriceOriginal[index].push({ data: this.sAQuoteDetails[index].materialGoods.salePrice2 });
                }
                if (this.sAQuoteDetails[index].materialGoods.salePrice3) {
                    this.unitPriceOriginal[index].push({ data: this.sAQuoteDetails[index].materialGoods.salePrice3 });
                }
                this.sAQuoteDetails[index].mainConvertRate = 1;
                this.sAQuoteDetails[index].formula = '*';
                this.sAQuoteDetails[index].mainQuantity = this.sAQuoteDetails[index].quantity;
                this.sAQuoteDetails[index].mainUnitPrice = this.sAQuoteDetails[index].unitPriceOriginal;
            } else {
                this.unitPriceOriginal[index] = [];
                const materialGoodsConvertUnit = this.materialGoodsConvertUnits.find(
                    x =>
                        x.materialGoodsID === this.sAQuoteDetails[index].materialGoods.id && x.unitID === this.sAQuoteDetails[index].unit.id
                );
                if (materialGoodsConvertUnit) {
                    this.sAQuoteDetails[index].mainConvertRate = materialGoodsConvertUnit.convertRate;
                    this.sAQuoteDetails[index].formula = materialGoodsConvertUnit.formula;
                    if (materialGoodsConvertUnit.formula === '*') {
                        this.sAQuoteDetails[index].mainQuantity =
                            materialGoodsConvertUnit.convertRate * this.sAQuoteDetails[index].quantity;
                        this.sAQuoteDetails[index].mainUnitPrice =
                            this.sAQuoteDetails[index].unitPriceOriginal / this.sAQuoteDetails[index].mainConvertRate;
                    } else if (materialGoodsConvertUnit.formula === '/') {
                        this.sAQuoteDetails[index].mainQuantity =
                            materialGoodsConvertUnit.convertRate / this.sAQuoteDetails[index].quantity;
                        this.sAQuoteDetails[index].mainUnitPrice =
                            this.sAQuoteDetails[index].unitPriceOriginal * this.sAQuoteDetails[index].mainConvertRate;
                    }
                    if (materialGoodsConvertUnit.fixedSalePrice) {
                        this.unitPriceOriginal[index].push({ data: materialGoodsConvertUnit.fixedSalePrice });
                    }
                    if (materialGoodsConvertUnit.salePrice1) {
                        this.unitPriceOriginal[index].push({ data: materialGoodsConvertUnit.salePrice1 });
                    }
                    if (materialGoodsConvertUnit.salePrice2) {
                        this.unitPriceOriginal[index].push({ data: materialGoodsConvertUnit.salePrice2 });
                    }
                    if (materialGoodsConvertUnit.salePrice3) {
                        this.unitPriceOriginal[index].push({ data: materialGoodsConvertUnit.salePrice3 });
                    }
                }
                this.sAQuoteDetails[index].unitPriceOriginal = this.unitPriceOriginal[index][0].data;
            }
        }
        this.changeQuantity(index);
    }

    selectUnitPriceOriginal(index) {
        if (this.sAQuote.exchangeRate && this.sAQuoteDetails[index].unitPriceOriginal && this.sAQuoteDetails[index].mainConvertRate) {
            if (this.sAQuoteDetails[index].formula === '*') {
                this.sAQuoteDetails[index].mainUnitPrice =
                    this.sAQuoteDetails[index].unitPriceOriginal / this.sAQuoteDetails[index].mainConvertRate;
            } else {
                this.sAQuoteDetails[index].mainUnitPrice =
                    this.sAQuoteDetails[index].unitPriceOriginal * this.sAQuoteDetails[index].mainConvertRate;
            }
            this.sAQuoteDetails[index].unitPrice = this.round(
                this.sAQuoteDetails[index].unitPriceOriginal *
                    (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                1
            );
            this.calculateAmountOriginal(index, true);
        }
        this.roundObject();
    }

    changeQuantity(index) {
        let isCalDiscount = false;
        this.saleDiscountPolicys = this.sAQuoteDetails[index].materialGoods.saleDiscountPolicy;
        if (this.saleDiscountPolicys) {
            this.sAQuoteDetails[index].discountRate = this.sAQuoteDetails[index].materialGoods.saleDiscountRate
                ? this.sAQuoteDetails[index].materialGoods.saleDiscountRate
                : 0;
            isCalDiscount = true;
        }
        this.checkSaleDiscountPolicy(index);
        if (this.sAQuoteDetails[index].unit) {
            if (this.sAQuoteDetails[index].formula === '*') {
                this.sAQuoteDetails[index].mainQuantity = this.sAQuoteDetails[index].quantity * this.sAQuoteDetails[index].mainConvertRate;
            } else {
                this.sAQuoteDetails[index].mainQuantity = this.sAQuoteDetails[index].quantity / this.sAQuoteDetails[index].mainConvertRate;
            }
        }
        this.calculateAmountOriginal(index, isCalDiscount);
    }

    calculateAmountOriginal(index, isCalDiscount) {
        if (
            (this.sAQuoteDetails[index].quantity || this.sAQuoteDetails[index].quantity === 0) &&
            (this.sAQuoteDetails[index].unitPriceOriginal || this.sAQuoteDetails[index].unitPriceOriginal === 0)
        ) {
            this.sAQuoteDetails[index].amountOriginal = this.sAQuoteDetails[index].quantity * this.sAQuoteDetails[index].unitPriceOriginal;
            this.roundObject();
            if (this.sAQuote.exchangeRate) {
                this.sAQuoteDetails[index].amount = this.round(
                    this.sAQuoteDetails[index].amountOriginal *
                        (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                    7
                );
            }
            this.roundObject();
            if (isCalDiscount) {
                this.changeDiscountRate(index);
            }
            this.changeVATRate(index);
        }
    }

    changeMainUnitPrice(index) {
        if (this.sAQuoteDetails[index].mainUnitPrice && this.sAQuoteDetails[index].mainConvertRate) {
            if (this.sAQuoteDetails[index].formula === '*') {
                this.sAQuoteDetails[index].unitPriceOriginal =
                    this.sAQuoteDetails[index].mainUnitPrice * this.sAQuoteDetails[index].mainConvertRate;
            } else {
                this.sAQuoteDetails[index].unitPriceOriginal =
                    this.sAQuoteDetails[index].mainUnitPrice / this.sAQuoteDetails[index].mainConvertRate;
            }
        }
        if (this.sAQuoteDetails[index].unitPriceOriginal) {
            this.sAQuoteDetails[index].amountOriginal = this.sAQuoteDetails[index].quantity * this.sAQuoteDetails[index].unitPriceOriginal;
        }
        if (this.sAQuote.exchangeRate) {
            this.sAQuoteDetails[index].unitPrice = this.round(
                this.sAQuoteDetails[index].unitPriceOriginal *
                    (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                1
            );
            this.sAQuoteDetails[index].amount = this.round(
                this.sAQuoteDetails[index].amountOriginal *
                    (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                7
            );
        }
        this.roundObject();
        this.changeDiscountRate(index);
        this.changeVATRate(index);
        // this.selectUnitPriceOriginal(index);
    }

    changeAmount(index) {
        if (!this.sAQuoteDetails[index].amountOriginal) {
            this.sAQuoteDetails[index].amountOriginal = 0;
        }
        if (this.sAQuoteDetails[index].formula === '*') {
            this.sAQuoteDetails[index].quantity = this.sAQuoteDetails[index].mainQuantity / this.sAQuoteDetails[index].mainConvertRate;
        } else {
            this.sAQuoteDetails[index].quantity = this.sAQuoteDetails[index].mainQuantity * this.sAQuoteDetails[index].mainConvertRate;
        }
        if (this.sAQuoteDetails[index].quantity !== 0) {
            this.sAQuoteDetails[index].unitPriceOriginal = this.sAQuoteDetails[index].amountOriginal / this.sAQuoteDetails[index].quantity;
            this.sAQuoteDetails[index].unitPrice = this.round(
                this.sAQuoteDetails[index].unitPriceOriginal *
                    (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                1
            );
        }
        if (this.sAQuote.exchangeRate) {
            this.sAQuoteDetails[index].amount = this.round(
                this.sAQuoteDetails[index].amountOriginal *
                    (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                7
            );
        }
        this.roundObject();
        this.changeDiscountRate(index);
        this.changeVATRate(index);
    }

    changeMainQuantity(index) {
        if (!this.sAQuoteDetails[index].amountOriginal) {
            this.sAQuoteDetails[index].amountOriginal = 0;
        }
        if (this.sAQuoteDetails[index].formula === '*') {
            this.sAQuoteDetails[index].quantity = this.sAQuoteDetails[index].mainQuantity / this.sAQuoteDetails[index].mainConvertRate;
        } else {
            this.sAQuoteDetails[index].quantity = this.sAQuoteDetails[index].mainQuantity * this.sAQuoteDetails[index].mainConvertRate;
        }
        if (this.sAQuoteDetails[index].unitPriceOriginal) {
            this.sAQuoteDetails[index].amountOriginal = this.sAQuoteDetails[index].quantity * this.sAQuoteDetails[index].unitPriceOriginal;
        }
        if (this.sAQuote.exchangeRate) {
            this.sAQuoteDetails[index].amount = this.round(
                this.sAQuoteDetails[index].amountOriginal *
                    (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                7
            );
        }
        this.roundObject();
        this.changeDiscountRate(index);
        this.changeVATRate(index);
    }

    changeDiscountAmount(index) {
        if (this.sAQuoteDetails[index].discountAmountOriginal || this.sAQuoteDetails[index].discountAmountOriginal === 0) {
            this.sAQuoteDetails[index].discountRate =
                this.sAQuoteDetails[index].discountAmountOriginal / this.sAQuoteDetails[index].amountOriginal * 100;
            if (this.sAQuote.exchangeRate) {
                this.sAQuoteDetails[index].discountAmount = this.round(
                    this.sAQuoteDetails[index].discountAmountOriginal *
                        (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                    7
                );
            }
        }
        this.changeVATRate(index);
    }

    changeDiscountRate(index) {
        if (this.sAQuoteDetails[index].discountRate > 100) {
            this.toastr.warning('Tỷ lệ chiết khấu không được lớn hơn 100%', this.translate.instant('ebwebApp.mCReceipt.home.message'));
        }
        if (this.sAQuoteDetails[index].amountOriginal && this.sAQuoteDetails[index].discountRate) {
            this.sAQuoteDetails[index].discountAmountOriginal =
                this.sAQuoteDetails[index].amountOriginal * this.sAQuoteDetails[index].discountRate / 100;
            if (this.sAQuote.exchangeRate) {
                this.sAQuoteDetails[index].discountAmount = this.round(
                    this.sAQuoteDetails[index].discountAmountOriginal *
                        (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                    7
                );
            }
        } else {
            this.sAQuoteDetails[index].discountAmountOriginal = 0;
            this.sAQuoteDetails[index].discountAmount = 0;
        }
        console.log(this.sAQuoteDetails[index].discountAmountOriginal + ' disc& ' + this.sAQuoteDetails[index].discountAmount);
        console.log('changeDiscountRateBefore : ' + JSON.stringify(this.sAQuoteDetails));
        // this.checkSaleDiscountPolicy(index);
        this.changeVATRate(index);
        this.updatesAQuote();
        console.log('changeDiscountRateAfter : ' + JSON.stringify(this.sAQuoteDetails));
    }

    changeVATRate(index) {
        if (
            (this.sAQuoteDetails[index].amountOriginal || this.sAQuoteDetails[index].amountOriginal === 0) &&
            (this.sAQuoteDetails[index].discountAmountOriginal || this.sAQuoteDetails[index].discountAmountOriginal === 0) &&
            (this.sAQuoteDetails[index].vATRate === 1 || this.sAQuoteDetails[index].vATRate === 2)
        ) {
            this.sAQuoteDetails[index].vATAmountOriginal =
                (this.sAQuoteDetails[index].amountOriginal - this.sAQuoteDetails[index].discountAmountOriginal) *
                (this.sAQuoteDetails[index].vATRate === 1 ? 0.05 : 0.1);
            if (this.sAQuote.exchangeRate) {
                this.sAQuoteDetails[index].vATAmount = this.round(
                    this.sAQuoteDetails[index].vATAmountOriginal *
                        (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                    7
                );
                this.updatesAQuote();
            }
        } else {
            this.sAQuoteDetails[index].vATAmountOriginal = 0;
            this.sAQuoteDetails[index].vATAmount = 0;
            this.updatesAQuote();
        }
    }

    changeMainConvertRate(index) {
        if (this.sAQuoteDetails[index].mainConvertRate) {
            this.sAQuoteDetails[index].mainQuantity =
                this.sAQuoteDetails[index].formula === '*'
                    ? this.sAQuoteDetails[index].quantity * this.sAQuoteDetails[index].mainConvertRate
                    : this.sAQuoteDetails[index].quantity / this.sAQuoteDetails[index].mainConvertRate;
            this.sAQuoteDetails[index].mainUnitPrice =
                this.sAQuoteDetails[index].formula === '*'
                    ? this.sAQuoteDetails[index].unitPrice / this.sAQuoteDetails[index].mainConvertRate
                    : this.sAQuoteDetails[index].unitPrice * this.sAQuoteDetails[index].mainConvertRate;
        }
        if (this.sAQuoteDetails[index].unitPriceOriginal) {
            this.sAQuoteDetails[index].amountOriginal = this.sAQuoteDetails[index].quantity * this.sAQuoteDetails[index].unitPriceOriginal;
        }
        if (this.sAQuote.exchangeRate) {
            this.sAQuoteDetails[index].amount = this.round(
                this.sAQuoteDetails[index].amountOriginal *
                    (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                7
            );
        }
        this.roundObject();
        this.changeDiscountRate(index);
        this.changeVATRate(index);
    }

    changeExchangeRate() {
        if (this.sAQuote.currencyID) {
            // this.sAQuote.exchangeRate = this.round(this.sAQuote.exchangeRate, 4);
            this.sAQuoteDetails.forEach(item => {
                if (item.unitPriceOriginal || item.unitPriceOriginal === 0) {
                    item.unitPrice = this.round(
                        item.unitPriceOriginal *
                            (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                        1
                    );
                }
                if (item.vATAmountOriginal || item.vATAmountOriginal === 0) {
                    item.vATAmount = this.round(
                        item.vATAmountOriginal *
                            (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                        7
                    );
                }
                if (item.amountOriginal || item.amountOriginal === 0) {
                    item.amount = this.round(
                        item.amountOriginal *
                            (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                        7
                    );
                }
                if (item.discountAmountOriginal || item.discountAmountOriginal === 0) {
                    item.discountAmount = this.round(
                        item.discountAmountOriginal *
                            (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                        7
                    );
                }
            });
            this.updatesAQuote();
        }
    }

    updatesAQuote() {
        this.sAQuote.totalAmount = this.round(this.sum('amount'), 7);
        this.sAQuote.totalVATAmount = this.round(this.sum('vATAmount'), 7);
        this.sAQuote.totalDiscountAmount = this.round(this.sum('discountAmount'), 7);
        this.sAQuote.total = this.round(this.sAQuote.totalAmount + this.sAQuote.totalVATAmount - this.sAQuote.totalDiscountAmount, 7);

        this.sAQuote.totalAmountOriginal = this.round(this.sum('amountOriginal'), this.getAmountOriginalType());
        this.sAQuote.totalVATAmountOriginal = this.round(this.sum('vATAmountOriginal'), this.getAmountOriginalType());
        this.sAQuote.totalDiscountAmountOriginal = this.round(this.sum('discountAmountOriginal'), this.getAmountOriginalType());
        this.sAQuote.totalOriginal = this.round(
            this.sAQuote.totalAmountOriginal + this.sAQuote.totalVATAmountOriginal - this.sAQuote.totalDiscountAmountOriginal,
            this.getAmountOriginalType()
        );
    }

    getAmountOriginalType() {
        if (this.isForeignCurrency()) {
            return 8;
        }
        return 7;
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.sAQuoteDetails.length; i++) {
            total += this.sAQuoteDetails[i][prop];
        }
        return total;
    }

    roundObject() {
        this.utilsService.roundObject(this.sAQuoteDetails, this.account.systemOption);
        this.utilsService.roundObject(this.sAQuote, this.account.systemOption);
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(0);
        }
    }

    public beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            if (!this.isReadOnly) {
                this.modalRef = this.refModalService.open(this.viewVouchersSelected);
            }
        }
    }

    registerRef() {
        this.eventSubscriber = this.eventManager.subscribe('selectViewVoucher', response => {
            if (!this.isReadOnly) {
                this.viewVouchersSelected = response.content;
            }
        });
    }

    registerCategory() {
        this.eventSubscriber = this.eventManager.subscribe('selectedEmployee', response => {
            this.sAQuote.employeeID = response.content.accountingObject.id;
            this.accountingObjectService.getAllDTO().subscribe(
                (res: HttpResponse<IAccountingObject[]>) => {
                    this.employees = res.body
                        .filter(x => x.isEmployee)
                        .sort((a, b) => (a.accountingObjectCode > b.accountingObjectCode ? 1 : -1));
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        });
    }

    /*
   * hàm ss du lieu 2 object và 2 mảng object
   * return true: neu tat ca giong nhau
   * return fale: neu 1 trong cac object ko giong nhau
   * */
    canDeactive(): boolean {
        if (this.isCheckCanDeactive) {
            return (
                this.utilsService.isEquivalent(this.sAQuote, this.sAQuoteCopy) &&
                this.utilsService.isEquivalentArray(this.sAQuoteDetails, this.sAQuoteDetailsCopy)
            );
        }
        return true;
    }

    closeForm() {
        event.preventDefault();
        if (!this.utilsService.isShowPopup) {
            if (this.isReadOnly) {
                this.isCheckCanDeactive = false;
                this.router.navigate(['/sa-quote']);
            } else {
                this.isCheckCanDeactive = true;
                if (!this.canDeactive()) {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    this.modalRef = this.modalService.open(this.modalCloseComponent, { backdrop: 'static' });
                } else {
                    this.noSaveCloseAndBackList();
                }
            }
        }
    }

    closeModal() {
        if (this.modalRef) {
            this.warningVatRate = 0;
            this.modalRef.close();
        }
    }

    noSaveCloseAndBackList() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isCheckCanDeactive = false;
        this.router.navigate(['/sa-quote']);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.BaoGia_Xoa])
    delete() {
        event.preventDefault();
        if (!this.utilsService.isShowPopup) {
            this.isCheckCanDeactive = false;
            this.router.navigate(['/sa-quote', { outlets: { popup: this.sAQuote.id + '/delete' } }]);
        }
    }

    getCurrencyType(type) {
        if (this.currencyCode === 'VND') {
            if (this.sAQuote.currencyID === 'VND') {
                return 1;
            } else {
                return type === 1 ? 2 : 1;
            }
        } else {
            if (this.sAQuote.currencyID === 'VND') {
                return type === 1 ? 1 : 2;
            } else {
                return 2;
            }
        }
    }

    getOriginalType() {
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

    isForeignCurrency() {
        return this.account && this.sAQuote.currencyID !== this.currencyCode;
    }

    changeVatAmountOriginal(index) {
        this.calculateVatAmount(index);
        this.updatesAQuote();
    }

    calculateVatAmount(index) {
        if (this.sAQuote.exchangeRate) {
            this.sAQuoteDetails[index].vATAmount = this.round(
                this.sAQuoteDetails[index].vATAmountOriginal *
                    (this.currency.formula.includes('*') ? this.sAQuote.exchangeRate : 1 / this.sAQuote.exchangeRate),
                7
            );
        }
    }

    round(value, type) {
        return this.utilsService.round(value, this.account.systemOption, type);
    }

    deleteRef() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isEdit = true;
        this.sAQuoteService.deleteRef(this.sAQuote.id).subscribe(
            (res: HttpResponse<boolean>) => {
                if (res) {
                    this.deleteRefSuccess();
                    this.isReadOnly = false;
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

    printPDF(isDownload, typeReports: number) {
        if (!this.isCreateUrl) {
            this.sAQuoteService
                .getCustomerReport({
                    id: this.sAQuote.id,
                    typeID: TypeID.BAO_GIA,
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
                        const name = 'Bao_gia.pdf';
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
            if (typeReports === 1) {
                this.toastr.success(
                    this.translate.instant('ebwebApp.mBDeposit.printing') +
                        this.translate.instant('ebwebApp.sAQuote.printBasicForm') +
                        '...',
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
            } else if (typeReports === 2) {
                this.toastr.success(
                    this.translate.instant('ebwebApp.mBDeposit.printing') +
                        this.translate.instant('ebwebApp.sAQuote.printAdvancedForm') +
                        '...',
                    this.translate.instant('ebwebApp.mBDeposit.message')
                );
            }
        }
    }

    setIsMoreForm(template) {
        this.isMoreForm = !this.isMoreForm;
        this.template = template;
    }

    changeReason() {
        this.isChangedReason = true;
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }

    addIfLastInput(i) {
        if (i === this.sAQuoteDetails.length - 1) {
            this.keyControlC(null);
        }
    }

    keyControlC(value: number) {
        if (value !== null && value !== undefined) {
            const ob: ISAQuoteDetails = Object.assign({}, this.sAQuoteDetails[value]);
            ob.id = undefined;
            ob.orderPriority = undefined;
            this.sAQuoteDetails.push(ob);
        } else {
            this.sAQuoteDetails.push({});
        }
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.sAQuoteDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.sAQuote;
    }

    // updateSaQuoteDetail() {
    //     this.sAQuoteDetails[this.currentRow].unitPriceOriginal = this.sAQuoteDetails[this.currentRow].materialGoods.fixedSalePrice;
    //     this.selectMaterialGood(this.currentRow);
    //     this.sAQuoteDetails[this.currentRow].vATRate = this.sAQuoteDetails[this.currentRow].materialGoods.vatTaxRate;
    // }

    addDataToDetail() {
        if (this.details !== undefined) {
            this.sAQuoteDetails = this.details;
            this.selectMaterialGood(this.currentRow);
            this.sAQuoteDetails[this.currentRow].materialGoods.materialGoodsConvertUnits.forEach(n => {
                n.materialGoodsID = this.sAQuoteDetails[this.currentRow].materialGoods.id;
            });
            this.materialGoodsConvertUnits.push(...this.sAQuoteDetails[this.currentRow].materialGoods.materialGoodsConvertUnits);
            if (!this.utilsService.isShowPopup) {
                this.details = undefined;
            }
        } else {
            this.sAQuote.accountingObject = this.parent.accountingObject;
            this.selectedChangedAccountingObject();
        }
    }

    checkRequired(data: any) {
        return data === undefined || data === null || data.length === 0;
    }

    ngAfterViewChecked(): void {
        if (window.location.href.includes('edit')) {
            this.disableInput();
        }
    }
}
