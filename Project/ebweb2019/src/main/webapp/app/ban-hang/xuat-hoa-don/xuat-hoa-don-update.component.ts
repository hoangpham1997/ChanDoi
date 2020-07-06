import { AfterViewChecked, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { XuatHoaDonService } from 'app/ban-hang/xuat-hoa-don/xuat-hoa-don.service';
import { Principal } from 'app/core';
import { ISaBillDetails, SaBillDetails } from 'app/shared/model/sa-bill-details.model';
import { SaBill } from 'app/shared/model/sa-bill.model';
import { Currency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { AccountingObject, IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { AccountingObjectBankAccountService } from 'app/entities/accounting-object-bank-account';
import { AccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { EI_IDNhaCungCapDichVu, NCCDV_EINVOICE, SD_SO_QUAN_TRI, SO_LAM_VIEC, TCKHAC_SDTichHopHDDT, TypeID } from 'app/app.constants';
import { IaPublishInvoiceDetailsService } from 'app/ban-hang/xuat-hoa-don/ia-publish-invoice-details.service';
import { IaPublishInvoiceDetails } from 'app/shared/model/ia-publish-invoice-details.model';
import { IMaterialGoods, MaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from 'app/danhmuc/material-goods';
import { IUnit, Unit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import { SaBillService } from 'app/ban-hang/xuat-hoa-don/sa-bill.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { NgbModal, NgbModalRef, NgbTabChangeEvent } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { SaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';
import { SaleDiscountPolicyService } from 'app/entities/sale-discount-policy';
import { EbSaInvoiceModalComponent } from 'app/shared/modal/sa-invoice/sa-invoice.component';
import { BaseComponent } from 'app/shared/base-component/base.component';
import * as moment from 'moment';
import { RepositoryLedgerService } from 'app/entities/repository-ledger';
import { ROLE } from 'app/role.constants';
import { SAInvoiceService } from 'app/ban-hang/ban_hang_chua_thu_tien';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { MaterialGoodsConvertUnitService } from 'app/entities/material-goods-convert-unit';
import { IMaterialGoodsConvertUnit } from 'app/shared/model/material-goods-convert-unit.model';
import { ICareerGroup } from 'app/shared/model/career-group.model';
import { CareerGroupService } from 'app/entities/career-group';
import { DATE_FORMAT_SLASH } from 'app/shared';

@Component({
    selector: 'eb-xuat-hoa-don-update',
    templateUrl: './xuat-hoa-don-update.component.html',
    styleUrls: ['./xuat-hoa-don.update.css']
})
export class XuatHoaDonUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewChecked {
    @ViewChild('content') content;
    @ViewChild('deleteItem') deleteItem;
    @ViewChild('contentSave') contentSave;
    isSaving: boolean;
    currentAccount: any;
    saBill: SaBill;
    saBillDetails: SaBillDetails[];

    saBillCopy: SaBill;
    saBillDetailsCopy: SaBillDetails[];

    book: any;
    isSoTaiChinh: boolean;
    accountingObjects: AccountingObject[];
    accountingObjectAlls: AccountingObject[];
    accountingObject: AccountingObject;
    currencies: Currency[];
    rowNum: any;
    count: any;
    dataSession: any;
    itemsPerPage: number;
    totalItems: number;
    searchData: string;
    predicate: any;
    reverse: number;
    page: number;
    currency: Currency;
    listColumnHeader: string[];
    accountingObjectBankAccounts: AccountingObjectBankAccount[];
    accountingObjectBankAccount: AccountingObjectBankAccount;
    templates: IaPublishInvoiceDetails[];
    template: IaPublishInvoiceDetails;
    materialGoodss: MaterialGoods[];
    units: Unit[];
    materialGoodsConvertUnits: IMaterialGoodsConvertUnit[];
    careerGroups: ICareerGroup[];
    isEdit: boolean;
    eventSubscriber: Subscription;
    eventSubscriber2: Subscription;
    eventSubscriber3: Subscription;
    modalRef: NgbModalRef;
    viewVouchersSelected: any[];
    warningVatRate: number;
    isSaveAndAdd: boolean;
    contextMenu: ContextMenu;
    saleDiscountPolicys: SaleDiscountPolicy[];
    currencyCode: string;
    isEditReasonFirst: boolean;
    isLoading: boolean;
    typeDelete: number;
    isRequiredInvoiceNo: boolean;
    isShow: boolean;
    paymentMethod: any[];
    isAsk: boolean;
    saBillList: any[];
    listVAT: any[];
    isSelfChange: boolean;
    isMoreForm: boolean;
    isViewFromRef: boolean;
    hiddenVAT: boolean;
    defaultCareerGroupID: string;
    createFrom = 0;
    indexFocusDetailRow: any;
    indexFocusDetailCol: any;
    idIndex: any;
    select: number;
    /*Hóa đơn điện tử*/
    isHasType: boolean;
    typeEInvoiceString: string;
    warningDelete: boolean;
    TYPE_BAN_HANG = TypeID.BAN_HANG_CHUA_THU_TIEN;
    TYPE_HANG_BAN_GG = TypeID.HANG_GIAM_GIA;
    TYPE_MUA_HANG_TL = TypeID.MUA_HANG_TRA_LAI;
    /****************/

    ROLE_XuatHoaDon = ROLE.XuatHoaDon_Xem;
    ROLE_Them = ROLE.XuatHoaDon_Them;
    ROLE_Sua = ROLE.XuatHoaDon_Sua;
    ROLE_Xoa = ROLE.XuatHoaDon_Xoa;
    ROLE_In = ROLE.XuatHoaDon_In;

    NCCDV: string;
    NCCDV_EINVOICE = NCCDV_EINVOICE;

    constructor(
        private jhiAlertService: JhiAlertService,
        private xuatHoaDonService: XuatHoaDonService,
        private principal: Principal,
        private router: Router,
        private currencyService: CurrencyService,
        private accountingObjectService: AccountingObjectService,
        private accountingObjectBankAccountService: AccountingObjectBankAccountService,
        private activatedRoute: ActivatedRoute,
        private iaPublishInvoiceDetailsService: IaPublishInvoiceDetailsService,
        private materialGoodsService: MaterialGoodsService,
        private unitService: UnitService,
        private materialGoodsConvertUnitService: MaterialGoodsConvertUnitService,
        private saBillService: SaBillService,
        private saInvoiceService: SAInvoiceService,
        private toastrService: ToastrService,
        public translateService: TranslateService,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        public utilService: UtilsService,
        private refModalService: RefModalService,
        private saleDiscountPolicyService: SaleDiscountPolicyService,
        private repositoryLedgerService: RepositoryLedgerService,
        private careerGroupService: CareerGroupService
    ) {
        super();
        this.saBill = { typeID: TypeID.XUAT_HOA_DON };
        this.saBillDetails = [];
        this.currency = {};
        this.contextMenu = new ContextMenu();
        this.listColumnHeader = ['accountingObjectCode', 'accountingObjectName'];
        this.viewVouchersSelected = [];
        this.isViewFromRef = window.location.href.includes('/from-ref');
    }

    ngOnInit() {
        this.isLoading = true;
        this.isSaving = false;
        this.isEdit = true;
        this.warningVatRate = 0;
        this.count = this.saBillService.total;
        this.paymentMethod = [];
        this.translateService
            .get(['global.paymentMethod.cash', 'global.paymentMethod.transfer', 'global.paymentMethod.both'])
            .subscribe(res => {
                this.paymentMethod.push({ data: res['global.paymentMethod.cash'] });
                this.paymentMethod.push({ data: res['global.paymentMethod.transfer'] });
                this.paymentMethod.push({ data: res['global.paymentMethod.both'] });
            });
        this.listVAT = [
            { name: '0%', data: 0 },
            { name: '5%', data: 1 },
            { name: '10%', data: 2 },
            { name: 'KCT', data: 3 },
            { name: 'KTT', data: 4 }
        ];
        this.materialGoodsService.queryForComboboxGood().subscribe((res: HttpResponse<IMaterialGoods[]>) => {
            this.materialGoodss = res.body;
            // this.getMaterialGoods();
        });
        this.saleDiscountPolicyService.findAllSaleDiscountPolicyDTO().subscribe(res => {
            this.saleDiscountPolicys = res.body;
        });
        this.careerGroupService.getCareerGroups().subscribe((res: HttpResponse<ICareerGroup[]>) => {
            this.careerGroups = res.body;
        });
        this.unitService.convertRateForMaterialGoodsComboboxCustom().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
            this.getUnit();
        });
        this.materialGoodsConvertUnitService
            .getAllMaterialGoodsConvertUnits()
            .subscribe((res: HttpResponse<IMaterialGoodsConvertUnit[]>) => {
                this.materialGoodsConvertUnits = res.body;
            });
        this.rowNum = parseInt(this.activatedRoute.snapshot.paramMap.get('id'), 0);
        this.accountingObjectService.getAllAccountingObjectDTO().subscribe((data: HttpResponse<IAccountingObject[]>) => {
            this.accountingObjectAlls = data.body;
            if (!this.isEdit) {
                this.accountingObjects = this.accountingObjectAlls;
            } else {
                this.accountingObjects = this.accountingObjectAlls
                    .filter(x => x.isActive)
                    .sort((a, b) => a.accountingObjectCode.localeCompare(b.accountingObjectCode));
            }
            this.activatedRoute.data.subscribe(({ saBill }) => {
                this.getSessionData();
                this.saBill = saBill && saBill.saBill ? saBill.saBill : this.saBill;
                this.count = saBill && saBill.totalRow;
                this.viewVouchersSelected = saBill && saBill.viewVouchers ? saBill.viewVouchers : [];
                if (this.saBill) {
                    if (this.saBill.saBillDetails) {
                        this.saBillDetails = this.saBill.saBillDetails;
                        if (this.saBillDetails) {
                            this.saBillDetails.sort((a, b) => {
                                return a.orderPriority - b.orderPriority;
                            });
                            this.saBillDetails.forEach(item => {
                                if (item.ppDiscountReturnDetailID) {
                                    this.createFrom = TypeID.MUA_HANG_TRA_LAI;
                                } else if (item.saReturnDetailID) {
                                    this.createFrom = TypeID.HANG_GIAM_GIA;
                                } else if (item.saInvoiceDetailID) {
                                    this.createFrom = TypeID.BAN_HANG_CHUA_THU_TIEN;
                                }
                            });
                        }
                        this.getUnit();
                        this.getMaterialGoods();
                    }
                    if (this.saBill.id) {
                        this.isEdit = false;
                    } else {
                        this.isEditReasonFirst = true;
                        this.translateService.get(['ebwebApp.saBill.reasonXHD']).subscribe((res: any) => {
                            this.saBill.reason = res['ebwebApp.saBill.reasonXHD'];
                        });
                        if (!this.saBill.paymentMethod) {
                            this.saBill.paymentMethod = this.paymentMethod[0].data;
                        }
                    }
                    this.accountingObjectBankAccount = {
                        bankAccount: this.saBill.accountingObjectBankAccount,
                        bankName: this.saBill.accountingObjectBankName
                    };
                    if (this.saBill.totalAmount) {
                        this.saBill.total =
                            this.saBill.totalAmount -
                            (this.saBill.totalDiscountAmount ? this.saBill.totalDiscountAmount : 0) +
                            (this.saBill.totalVATAmount ? this.saBill.totalVATAmount : 0);
                    }
                    if (this.saBill.totalAmountOriginal) {
                        this.saBill.totalOriginal =
                            this.saBill.totalAmountOriginal -
                            (this.saBill.totalDiscountAmountOriginal ? this.saBill.totalDiscountAmountOriginal : 0) +
                            (this.saBill.totalVATAmountOriginal ? this.saBill.totalVATAmountOriginal : 0);
                    }
                    /*Add by Hautv - Hóa đơn điện tử*/
                    this.isHasType = this.activatedRoute.snapshot.paramMap.has('type');
                    if (this.isHasType) {
                        const type: number = Number(this.activatedRoute.snapshot.paramMap.get('type'));
                        if (type !== null && type !== undefined) {
                            this.setDataWithType(type);
                        }
                    } else {
                        if (
                            this.saBill.type !== null &&
                            this.saBill.type !== undefined &&
                            (!this.saBill.iDAdjustInv || !this.saBill.iDReplaceInv)
                        ) {
                            this.getTypeStringWhenEdit();
                        }
                    }
                    /*******************************/
                }
                this.iaPublishInvoiceDetailsService.getAllByCompany().subscribe(res => {
                    this.templates = res.body;
                    this.templates.forEach(item => {
                        if (item.invoiceForm === 0) {
                            item.invoiceFormName = 'Hóa đơn điện tử';
                        } else if (item.invoiceForm === 1) {
                            item.invoiceFormName = 'Hóa đơn đặt in';
                        } else if (item.invoiceForm === 2) {
                            item.invoiceFormName = 'Hóa đơn tự in';
                        }
                    });
                    if (this.saBill && this.saBill.invoiceTypeID) {
                        this.template = this.templates.find(item => item.invoiceTypeID === this.saBill.invoiceTypeID);
                    } else if (this.templates.length === 1 && !this.template) {
                        this.template = this.templates[0];
                        this.selectTemplate();
                    }
                });
                this.principal.identity().then(account => {
                    this.currentAccount = account;
                    this.NCCDV = this.currentAccount.systemOption.find(x => x.code === EI_IDNhaCungCapDichVu).data;
                    this.isSoTaiChinh = this.currentAccount.systemOption.some(x => x.code === SO_LAM_VIEC && x.data === '0');
                    this.hiddenVAT = this.currentAccount.organizationUnit.taxCalculationMethod === 1;
                    this.defaultCareerGroupID = this.currentAccount.organizationUnit.careerGroupID;
                    if (!this.saBill.typeLedger && this.saBill.typeLedger !== 0) {
                        this.saBill.typeLedger = 2;
                    }
                    this.isShow = this.currentAccount.systemOption.some(x => x.code === SD_SO_QUAN_TRI && x.data === '1');
                    this.isRequiredInvoiceNo = this.currentAccount.systemOption.some(
                        x => x.code === TCKHAC_SDTichHopHDDT && x.data === '0'
                    );

                    this.currencyCode = this.currentAccount.organizationUnit.currencyID;
                    this.currencyService.findAllActive().subscribe(res => {
                        this.currencies = res.body;
                        if (this.saBill && this.saBill.currencyID) {
                            this.currency = this.currencies.find(cur => cur.currencyCode === this.saBill.currencyID);
                        } else if (this.currentAccount.organizationUnit && this.currentAccount.organizationUnit.currencyID) {
                            this.currency = this.currencies.find(
                                cur => cur.currencyCode === this.currentAccount.organizationUnit.currencyID
                            );
                        }
                        this.changeExchangeRate(this.saBill.exchangeRate !== null && this.saBill.exchangeRate !== undefined);
                        this.isLoading = false;
                    });
                });
            });
        });
        this.registerChangeInAccountDefaults();
        this.registerRef();
        this.registerSaInvoice();
        this.registerAddNewRow();
        this.registerDeleteRow();
        this.registerCopyRow();
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilService.setShowPopup(false);
        });
        this.registerIsShowPopup();
    }

    registerIsShowPopup() {
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilService.setShowPopup(response.content);
        });
    }

    getSessionData() {
        this.dataSession = JSON.parse(sessionStorage.getItem('dataSessionSaBill'));
        if (!this.dataSession) {
            this.dataSession = JSON.parse(sessionStorage.getItem('dataSearchSaBill'));
        }
        if (this.dataSession) {
            this.page = this.dataSession.page;
            this.itemsPerPage = this.dataSession.itemsPerPage;
            this.totalItems = this.dataSession.totalItems;
            this.rowNum = this.dataSession.rowNum;
            this.searchData = this.dataSession.searchVoucher;
            this.predicate = this.dataSession.predicate;
            this.reverse = this.dataSession.reverse;
        } else {
            this.dataSession = null;
        }
    }

    /*Hóa đơn điện tử*/
    setDataWithType(type: number) {
        this.isLoading = true;
        this.isSaving = false;
        this.isEdit = true;
        this.saBill.type = type;
        if (type === 0) {
            this.saBill.iDReplaceInv = this.saBill.id;
            this.saBill.iDAdjustInv = null;
        } else {
            this.saBill.iDAdjustInv = this.saBill.id;
            this.saBill.iDReplaceInv = null;
        }
        this.saBill.id = undefined;
        this.saBill.saBillDetails.forEach(n => {
            n.id = undefined;
        });
        switch (type) {
            case 0:
                this.typeEInvoiceString =
                    `Thay thế cho hóa đơn số ${this.saBill.invoiceNo}, ` +
                    `ký hiệu: ${this.saBill.invoiceSeries}, ` +
                    `ngày hóa đơn ${this.saBill.invoiceDate.format('DD/MM/YYYY')}`;
                this.saBill.statusInvoice = 7;
                break;
            case 2:
                this.typeEInvoiceString =
                    `Điều chỉnh tăng cho hóa đơn số ${this.saBill.invoiceNo}, ` +
                    `ký hiệu: ${this.saBill.invoiceSeries}, ` +
                    `ngày hóa đơn ${this.saBill.invoiceDate.format('DD/MM/YYYY')}`;
                this.saBill.statusInvoice = 8;
                break;
            case 3:
                this.typeEInvoiceString =
                    `Điều chỉnh giảm cho hóa đơn số ${this.saBill.invoiceNo}, ` +
                    `ký hiệu: ${this.saBill.invoiceSeries}, ` +
                    `ngày hóa đơn ${this.saBill.invoiceDate.format('DD/MM/YYYY')}`;
                this.saBill.statusInvoice = 8;
                break;
            case 4:
                this.typeEInvoiceString =
                    `Điều chỉnh thông tin cho hóa đơn số ${this.saBill.invoiceNo}, ` +
                    `ký hiệu: ${this.saBill.invoiceSeries}, ` +
                    `ngày hóa đơn ${this.saBill.invoiceDate.format('DD/MM/YYYY')}`;
                this.saBill.statusInvoice = 8;
                break;
        }
        this.saBill.invoiceNo = null;
        this.saBill.invoiceDate = null;
        this.saBill.refDateTime = null;
        this.viewVouchersSelected = [];
    }

    getTypeStringWhenEdit() {
        this.saBillService.find(this.saBill.iDAdjustInv ? this.saBill.iDAdjustInv : this.saBill.iDReplaceInv).subscribe(res => {
            if (res.body) {
                const saBill: SaBill = res.body.saBill;
                switch (this.saBill.type) {
                    case 0:
                        this.typeEInvoiceString =
                            `Thay thế cho hóa đơn số ${saBill.invoiceNo}, ` +
                            `ký hiệu: ${saBill.invoiceSeries}, ` +
                            `ngày hóa đơn ${saBill.invoiceDate.format('DD/MM/YYYY')}`;
                        break;
                    case 2:
                        this.typeEInvoiceString =
                            `Điều chỉnh tăng cho hóa đơn số ${saBill.invoiceNo}, ` +
                            `ký hiệu: ${saBill.invoiceSeries}, ` +
                            `ngày hóa đơn ${saBill.invoiceDate.format('DD/MM/YYYY')}`;
                        break;
                    case 3:
                        this.typeEInvoiceString =
                            `Điều chỉnh giảm cho hóa đơn số ${saBill.invoiceNo}, ` +
                            `ký hiệu: ${saBill.invoiceSeries}, ` +
                            `ngày hóa đơn ${saBill.invoiceDate.format('DD/MM/YYYY')}`;
                        break;
                    case 4:
                        this.typeEInvoiceString =
                            `Điều chỉnh thông tin cho hóa đơn số ${saBill.invoiceNo}, ` +
                            `ký hiệu: ${saBill.invoiceSeries}, ` +
                            `ngày hóa đơn ${saBill.invoiceDate.format('DD/MM/YYYY')}`;
                        break;
                }
            }
        });
    }

    /****************/

    setInvoiceDate() {
        if (this.saBill.refDateTime) {
            this.saBill.invoiceDate = this.saBill.refDateTime;
        }
    }

    getUnit() {
        if (this.saBillDetails && this.units) {
            this.saBillDetails.forEach(item => {
                item.units = this.units.filter(data => data.materialGoodsID === item.materialGoods.id);
                item.unit = item.units.find(x => x.id === item.unitID);
                item.mainUnit = item.units.find(x => x.id === item.mainUnitID);
            });
        }
    }

    getMaterialGoods() {
        if (this.saBillDetails) {
            this.saBillDetails.forEach((item, index) => {
                this.saBillDetails[index].unitPriceOriginals = [];
                if (item.unitID && item.mainUnitID && item.unitID !== item.mainUnitID) {
                    const materialGoodsConvertUnit = this.materialGoodsConvertUnits.find(
                        x =>
                            x.materialGoodsID === this.saBillDetails[index].materialGoods.id &&
                            x.unitID === this.saBillDetails[index].unitID
                    );
                    if (materialGoodsConvertUnit) {
                        if (materialGoodsConvertUnit.fixedSalePrice) {
                            this.saBillDetails[index].unitPriceOriginals.push({
                                unitPriceOriginal: materialGoodsConvertUnit.fixedSalePrice
                            });
                        }
                        if (materialGoodsConvertUnit.salePrice1) {
                            this.saBillDetails[index].unitPriceOriginals.push({ unitPriceOriginal: materialGoodsConvertUnit.salePrice1 });
                        }
                        if (materialGoodsConvertUnit.salePrice2) {
                            this.saBillDetails[index].unitPriceOriginals.push({ unitPriceOriginal: materialGoodsConvertUnit.salePrice2 });
                        }
                        if (materialGoodsConvertUnit.salePrice3) {
                            this.saBillDetails[index].unitPriceOriginals.push({ unitPriceOriginal: materialGoodsConvertUnit.salePrice3 });
                        }
                    }
                } else {
                    if (this.saBillDetails[index].materialGoods.fixedSalePrice) {
                        this.saBillDetails[index].unitPriceOriginals.push({
                            unitPriceOriginal: this.saBillDetails[index].materialGoods.fixedSalePrice
                        });
                    }
                    if (this.saBillDetails[index].materialGoods.salePrice1) {
                        this.saBillDetails[index].unitPriceOriginals.push({
                            unitPriceOriginal: this.saBillDetails[index].materialGoods.salePrice1
                        });
                    }
                    if (this.saBillDetails[index].materialGoods.salePrice2) {
                        this.saBillDetails[index].unitPriceOriginals.push({
                            unitPriceOriginal: this.saBillDetails[index].materialGoods.salePrice2
                        });
                    }
                    if (this.saBillDetails[index].materialGoods.salePrice3) {
                        this.saBillDetails[index].unitPriceOriginals.push({
                            unitPriceOriginal: this.saBillDetails[index].materialGoods.salePrice3
                        });
                    }
                }
            });
        }
    }

    previousState() {
        window.history.back();
        if (this.activatedRoute.snapshot.paramMap.has('id')) {
            const type: number = Number(this.activatedRoute.snapshot.paramMap.get('id'));
            if (isNaN(type)) {
                this.router.navigate(['/xuat-hoa-don']);
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.XuatHoaDon_Them, ROLE.XuatHoaDon_Sua])
    save(isNew = false) {
        event.preventDefault();
        if (this.isEdit && !this.utilService.isShowPopup) {
            if (this.saBillDetails.filter(x => !x.vatRate && x.vatRate !== 0 && x.vatAmountOriginal > 0).length > 0) {
                this.warningVatRate = 1;
            } else if (this.saBillDetails.filter(x => x.vatRate !== 1 && x.vatRate !== 2 && x.vatAmountOriginal > 0).length > 0) {
                this.warningVatRate = 2;
            }
            if (this.warningVatRate === 1 || this.warningVatRate === 2) {
                this.isSaveAndAdd = false;
                this.modalRef = this.modalService.open(this.contentSave, { backdrop: 'static' });
            } else {
                this.saveAll(false);
            }
        }
    }

    saveAll(isNew = false) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.saBill.companyTaxCode && !this.utilService.checkMST(this.saBill.companyTaxCode)) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.mst'));
            return;
        }
        if (!this.saBill.invoiceTemplate) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceTemplate'));
            return;
        }
        if (!this.saBill.invoiceSeries) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceSeries'));
            return;
        }
        if (!this.saBill.invoiceNo && this.checkRequiredInvoiceNo()) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceNo'));
            return;
        }
        if (!this.saBill.invoiceDate) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceDate'));
            return;
        }
        if (this.saBill.invoiceDate.isBefore(this.template.startUsing)) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.invoiceDate2'));
            return;
        }
        if (!this.saBill.paymentMethod) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.paymentMethod'));
            return;
        }
        if (!this.saBillDetails || !this.saBillDetails.length) {
            this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.saBillDetails'));
            return;
        }

        if (this.NCCDV === NCCDV_EINVOICE.SIV && (this.saBill.type || this.saBill.type === 0)) {
            if (!this.saBill.documentNo) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.documentName'));
                return;
            } else if (!this.saBill.documentDate) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.documentDate'));
                return;
            }
        }
        if (this.NCCDV === NCCDV_EINVOICE.MIV && (this.saBill.type || this.saBill.type === 0)) {
            if (!this.saBill.documentNo) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.documentName'));
                return;
            } else if (!this.saBill.documentDate) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.documentDate'));
                return;
            } else if (!this.saBill.documentNote) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.documentNote'));
                return;
            }
        }

        this.saBillDetails = this.utilService.roundObjects(this.saBillDetails, this.currentAccount.systemOption);
        for (let i = 0; i < this.saBillDetails.length; i++) {
            this.saBillDetails[i].orderPriority = i;
            if (this.saBillDetails[i].discountRate > 100) {
                // this.saBillDetails[index].discountRate = 100;
                this.toastrService.error(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'));
                return;
            }
            if (!this.saBillDetails[i].materialGoods || !this.saBillDetails[i].materialGoods.id) {
                this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.materialGoods'));
                return;
            }
        }

        this.saBill.saBillDetails = this.saBillDetails;
        if (this.isRequiredInvoiceNo) {
            this.saBill.statusInvoice = 1;
        } else {
            if (!this.saBill.statusInvoice) {
                this.saBill.statusInvoice = 0;
            }
        }
        const data = { saBill: this.saBill, viewVouchers: this.viewVouchersSelected };
        this.saBillService.create(data).subscribe(
            res => {
                if (this.saBill.id) {
                    this.toastrService.success(this.translateService.instant('ebwebApp.saBill.updated'));
                } else {
                    this.toastrService.success(this.translateService.instant('ebwebApp.saBill.created'));
                }
                if (isNew) {
                    this.addNew();
                } else {
                    this.isEdit = false;
                    this.saBill.id = res.body.saBill.id;
                    this.saBill.invoiceNo = res.body.saBill.invoiceNo;
                }
                if (this.modalRef) {
                    this.modalRef.close();
                }
                this.saBillService.total = this.saBillService.total + 1;
                if (this.isAsk) {
                    this.previousState();
                }
            },
            error1 => {
                this.toastrService.error(this.translateService.instant('ebwebApp.saBill.error.' + error1.error.errorKey));
            }
        );
    }

    registerAddNewRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.addNewRow(true);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            this.keyPress(this.contextMenu.selectedData);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            this.copyRow(this.contextMenu.selectedData, true);
        });
        this.eventSubscribers.push(this.eventSubscriber);
    }

    addNewRow(isRightClick?) {
        if (this.isEdit) {
            let length = 0;
            if (isRightClick) {
                this.saBillDetails.splice(this.indexFocusDetailRow + 1, 0, {});
                length = this.indexFocusDetailRow + 2;
            } else {
                this.saBillDetails.push({
                    careerGroupID: null
                });
                length = this.saBillDetails.length;
            }
            this.saBillDetails[length - 1].vatRate = null;
            this.saBillDetails[length - 1].careerGroupID = this.defaultCareerGroupID;
            if (isRightClick && this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const id = this.idIndex;
                const row = this.indexFocusDetailRow + 1;
                this.indexFocusDetailRow = row;
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(id + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            } else {
                const nameTag: string = event.srcElement.id;
                const idx: number = this.saBillDetails.length - 1;
                const nameTagIndex: string = nameTag + String(idx);
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(nameTagIndex);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
        }
    }

    keyPress(detail, type?) {
        if (this.isEdit) {
            if (type || this.select) {
                this.viewVouchersSelected.splice(this.viewVouchersSelected.indexOf(detail), 1);
            } else {
                this.saBillDetails.splice(this.saBillDetails.indexOf(detail), 1);
                if (this.indexFocusDetailCol !== undefined && this.indexFocusDetailCol !== null) {
                    // vì còn trường hợp = 0
                    if (this.saBillDetails.length > 0) {
                        let row = 0;
                        if (this.indexFocusDetailRow > this.saBillDetails.length - 1) {
                            row = this.indexFocusDetailRow - 1;
                        } else {
                            row = this.indexFocusDetailRow;
                        }
                        const id = this.idIndex;
                        setTimeout(function() {
                            const element: HTMLElement = document.getElementById(id + row);
                            if (element) {
                                element.focus();
                            }
                        }, 0);
                    }
                }
            }
        }
    }

    actionFocus(indexCol, indexRow, id) {
        this.indexFocusDetailCol = indexCol;
        this.indexFocusDetailRow = indexRow;
        this.idIndex = id;
    }

    newArr(length: number): any[] {
        if (length > 0) {
            return new Array(length);
        } else {
            return new Array(1);
        }
    }

    onRightClick($event, data, selectedData, isNew, isDelete, isCopy, select?) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isCopy = isCopy;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.selectedData = selectedData;
        this.select = select;
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    move(index) {
        // goi service get by row num
        this.isLoading = true;
        this.saBillService.moveVoucher = index;
        this.rowNum = this.saBillService.searchVoucher.rowIndex;
        this.saBillService.find().subscribe(
            res => {
                if (res.body) {
                    this.saBill = res.body.saBill;
                    this.saBillDetails = this.saBill.saBillDetails;
                    this.viewVouchersSelected = res.body.viewVouchers;
                    this.count = res.body.totalRow;
                }
                this.isLoading = false;
            },
            () => {
                this.saBillService.moveVoucher = -index;
                this.rowNum = this.saBillService.searchVoucher.rowIndex;
                this.isLoading = false;
            }
        );
    }

    selectAccountingObject() {
        if (this.saBill.accountingObject) {
            this.saBill.accountingObjectName = this.saBill.accountingObject.accountingObjectName;
            this.saBill.accountingObjectAddress = this.saBill.accountingObject.accountingObjectAddress;
            this.saBill.companyTaxCode = this.saBill.accountingObject.taxCode;
            this.saBill.contactName = this.saBill.accountingObject.contactName;
            if (this.isEditReasonFirst) {
                this.translateService.get(['ebwebApp.saBill.reasonXHD1']).subscribe((res: any) => {
                    this.saBill.reason = res['ebwebApp.saBill.reasonXHD1'] + ' ' + this.saBill.accountingObjectName;
                });
            }
            this.accountingObjectBankAccountService
                .getByAccountingObjectById({
                    accountingObjectID: this.saBill.accountingObject.id
                })
                .subscribe(ref => {
                    this.accountingObjectBankAccounts = ref.body;
                });
        } else {
            this.saBill.accountingObjectName = null;
            this.saBill.accountingObjectAddress = null;
            this.saBill.companyTaxCode = null;
            this.saBill.contactName = null;
            this.accountingObjectBankAccounts = [];
            this.accountingObjectBankAccount = null;
            this.saBill.accountingObjectBankName = null;
            if (this.isEditReasonFirst) {
                this.translateService.get(['ebwebApp.saBill.reasonXHD']).subscribe((res: any) => {
                    this.saBill.reason = res['ebwebApp.saBill.reasonXHD'];
                });
            }
        }
    }

    changeAccountingName() {
        if (this.isEditReasonFirst) {
            this.translateService.get(['ebwebApp.saBill.reasonXHD1']).subscribe((res: any) => {
                this.saBill.reason = res['ebwebApp.saBill.reasonXHD1'] + ' ' + this.saBill.accountingObjectName;
            });
        }
    }

    selectAccountingObjectBankAccount() {
        this.saBill.accountingObjectBankAccount = this.accountingObjectBankAccount.bankAccount;
        this.saBill.accountingObjectBankName = this.accountingObjectBankAccount.bankName;
    }

    selectTemplate() {
        if (this.template) {
            this.saBill.invoiceTemplate = this.template.invoiceTemplate;
            this.saBill.invoiceSeries = this.template.invoiceSeries;
            this.saBill.invoiceForm = this.template.invoiceForm;
            this.saBill.invoiceTypeID = this.template.invoiceTypeID;
            this.saBill.invoiceNo = '';
            this.saBill.invoiceDate = null;
        } else {
            this.saBill.templateID = null;
            this.saBill.invoiceTemplate = null;
            this.saBill.invoiceSeries = null;
            this.saBill.invoiceForm = null;
            this.saBill.invoiceTypeID = null;
        }
    }

    selectedMaterialGoods(index) {
        if (!this.hiddenVAT) {
            this.saBillDetails[index].vatRate = this.saBillDetails[index].materialGoods.vatTaxRate;
        }
        this.saBillDetails[index].discountRate = this.saBillDetails[index].materialGoods.saleDiscountRate;
        this.saBillDetails[index].saleDiscountPolicys = this.saleDiscountPolicys.filter(
            x => x.materialGoodsID === this.saBillDetails[index].materialGoods.id
        );
        this.checkSaleDiscountPolicy(index);
        if (this.saBillDetails[index].materialGoods.careerGroupID) {
            this.saBillDetails[index].careerGroupID = this.saBillDetails[index].materialGoods.careerGroupID;
        }
        this.saBillDetails[index].unitPriceOriginals = [];
        this.saBillDetails[index].description = this.saBillDetails[index].materialGoods.materialGoodsName;
        if (this.saBillDetails[index].materialGoods.fixedSalePrice) {
            this.saBillDetails[index].unitPriceOriginal = this.saBillDetails[index].materialGoods.fixedSalePrice;
            this.calculateUnitPrice(index);
            this.saBillDetails[index].unitPriceOriginals.push({
                unitPriceOriginal: this.saBillDetails[index].materialGoods.fixedSalePrice
            });
        }
        if (this.saBillDetails[index].materialGoods.salePrice1) {
            this.saBillDetails[index].unitPriceOriginals.push({ unitPriceOriginal: this.saBillDetails[index].materialGoods.salePrice1 });
        }
        if (this.saBillDetails[index].materialGoods.salePrice2) {
            this.saBillDetails[index].unitPriceOriginals.push({ unitPriceOriginal: this.saBillDetails[index].materialGoods.salePrice2 });
        }
        if (this.saBillDetails[index].materialGoods.salePrice3) {
            this.saBillDetails[index].unitPriceOriginals.push({ unitPriceOriginal: this.saBillDetails[index].materialGoods.salePrice3 });
        }
        this.saBillDetails[index].units = this.units.filter(item => item.materialGoodsID === this.saBillDetails[index].materialGoods.id);
        if (this.saBillDetails[index].units && this.saBillDetails[index].units.length) {
            this.saBillDetails[index].unit = this.saBillDetails[index].units[0];
            this.saBillDetails[index].mainUnit = this.saBillDetails[index].units[0];
            this.selectUnit(index);
        } else {
            this.saBillDetails[index].mainConvertRate = 1;
            this.saBillDetails[index].formula = '*';
            this.saBillDetails[index].mainQuantity = this.saBillDetails[index].quantity;
            this.saBillDetails[index].mainUnitPrice = this.saBillDetails[index].unitPriceOriginal;
        }
        this.saBillDetails[index].lotNo = null;
        this.saBillDetails[index].expiryDate = null;
        this.repositoryLedgerService
            .getListLotNoByMaterialGoodsID({
                materialGoodsID: this.saBillDetails[index].materialGoods.id
            })
            .subscribe(ref => {
                this.saBillDetails[index].lotNos = ref.body;
            });
    }

    selectUnitPriceOriginal(index) {
        this.calculateUnitPrice(index);
        this.calculateMainUnitPrice(index);
        this.changeAmountOriginal(index);
    }

    selectUnit(index) {
        if (this.saBillDetails[index].unit && this.saBillDetails[index].mainUnit) {
            if (this.saBillDetails[index].unit.id === this.saBillDetails[index].mainUnit.id) {
                this.saBillDetails[index].mainConvertRate = 1;
                this.saBillDetails[index].formula = '*';
                this.saBillDetails[index].mainQuantity = this.saBillDetails[index].quantity;
                this.saBillDetails[index].mainUnitPrice = this.saBillDetails[index].unitPriceOriginal;
                this.saBillDetails[index].unitPriceOriginals = [];
                if (this.saBillDetails[index].materialGoods.fixedSalePrice) {
                    this.saBillDetails[index].unitPriceOriginal = this.saBillDetails[index].materialGoods.fixedSalePrice;
                    this.calculateUnitPrice(index);
                    this.saBillDetails[index].unitPriceOriginals.push({
                        unitPriceOriginal: this.saBillDetails[index].materialGoods.fixedSalePrice
                    });
                }
                if (this.saBillDetails[index].materialGoods.salePrice1) {
                    this.saBillDetails[index].unitPriceOriginals.push({
                        unitPriceOriginal: this.saBillDetails[index].materialGoods.salePrice1
                    });
                }
                if (this.saBillDetails[index].materialGoods.salePrice2) {
                    this.saBillDetails[index].unitPriceOriginals.push({
                        unitPriceOriginal: this.saBillDetails[index].materialGoods.salePrice2
                    });
                }
                if (this.saBillDetails[index].materialGoods.salePrice3) {
                    this.saBillDetails[index].unitPriceOriginals.push({
                        unitPriceOriginal: this.saBillDetails[index].materialGoods.salePrice3
                    });
                }
            } else {
                this.saBillDetails[index].mainConvertRate = this.saBillDetails[index].unit.convertRate;
                this.saBillDetails[index].formula = this.saBillDetails[index].unit.formula;
                this.saBillDetails[index].unitPriceOriginals = [];
                const materialGoodsConvertUnit = this.materialGoodsConvertUnits.find(
                    x => x.materialGoodsID === this.saBillDetails[index].materialGoods.id && x.unitID === this.saBillDetails[index].unit.id
                );
                if (materialGoodsConvertUnit) {
                    if (materialGoodsConvertUnit.fixedSalePrice) {
                        this.saBillDetails[index].unitPriceOriginal = materialGoodsConvertUnit.fixedSalePrice;
                        this.calculateUnitPrice(index);
                        this.saBillDetails[index].unitPriceOriginals.push({ unitPriceOriginal: materialGoodsConvertUnit.fixedSalePrice });
                    }
                    if (materialGoodsConvertUnit.salePrice1) {
                        this.saBillDetails[index].unitPriceOriginals.push({ unitPriceOriginal: materialGoodsConvertUnit.salePrice1 });
                    }
                    if (materialGoodsConvertUnit.salePrice2) {
                        this.saBillDetails[index].unitPriceOriginals.push({ unitPriceOriginal: materialGoodsConvertUnit.salePrice2 });
                    }
                    if (materialGoodsConvertUnit.salePrice3) {
                        this.saBillDetails[index].unitPriceOriginals.push({ unitPriceOriginal: materialGoodsConvertUnit.salePrice3 });
                    }
                }
                if (this.saBillDetails[index].formula === '*' && this.saBillDetails[index].quantity) {
                    this.saBillDetails[index].mainQuantity =
                        this.saBillDetails[index].unit.convertRate * this.saBillDetails[index].quantity;
                    this.saBillDetails[index].mainUnitPrice =
                        this.saBillDetails[index].unitPriceOriginal / this.saBillDetails[index].mainConvertRate;
                } else if (this.saBillDetails[index].formula === '/') {
                    this.saBillDetails[index].mainQuantity =
                        this.saBillDetails[index].unit.convertRate * this.saBillDetails[index].quantity;
                    this.saBillDetails[index].mainUnitPrice =
                        this.saBillDetails[index].unitPriceOriginal * this.saBillDetails[index].mainConvertRate;
                }
            }
            this.changeQuantity(index);
        }
    }

    changeExchangeRate(isExchange) {
        if (this.currency) {
            this.saBill.currencyID = this.currency.currencyCode;
            if (!isExchange) {
                this.saBill.exchangeRate = this.currency.exchangeRate;
            }
            this.saBillDetails.forEach(item => {
                if (item.unitPriceOriginal || item.unitPriceOriginal === 0) {
                    item.unitPrice = item.unitPriceOriginal * this.saBill.exchangeRate;
                }
                if (item.amountOriginal || item.amountOriginal === 0) {
                    item.amount = item.amountOriginal * this.saBill.exchangeRate;
                }
                if (item.discountAmountOriginal || item.discountAmountOriginal === 0) {
                    item.discountAmount = item.discountAmountOriginal * this.saBill.exchangeRate;
                }
                if (item.vatAmountOriginal || item.vatAmountOriginal === 0) {
                    item.vatAmount = item.vatAmountOriginal * this.saBill.exchangeRate;
                }
            });
            this.updateSaBill();
        }
    }

    changeQuantity(index) {
        this.calculateMainQuantity(index);
        this.changeAmountOriginal(index);
        this.checkSaleDiscountPolicy(index);
    }

    calculateAmountOriginal(index) {
        if (
            (this.saBillDetails[index].quantity || this.saBillDetails[index].quantity === 0) &&
            (this.saBillDetails[index].unitPriceOriginal || this.saBillDetails[index].unitPriceOriginal === 0)
        ) {
            this.saBillDetails[index].amountOriginal = this.saBillDetails[index].quantity * this.saBillDetails[index].unitPriceOriginal;

            this.utilService.round(
                this.saBillDetails[index].amountOriginal,
                this.currentAccount.systemOption,
                this.saBill.currencyID !== this.currencyCode ? 8 : 7
            );
        }
    }

    calculateAmount(index) {
        if (this.saBill.exchangeRate && this.currency) {
            if (this.currency.formula === '*') {
                this.saBillDetails[index].amount = this.saBillDetails[index].amountOriginal * this.saBill.exchangeRate;
            } else if (this.currency.formula === '/' && this.saBill.exchangeRate !== 0) {
                this.saBillDetails[index].amount = this.saBillDetails[index].amountOriginal / this.saBill.exchangeRate;
            }
            this.utilService.round(this.saBillDetails[index].amount, this.currentAccount.systemOption, 7);
        }
    }

    changeAmountOriginal(index, isSelfChange = false) {
        if (!isSelfChange) {
            this.calculateAmountOriginal(index);
        } else {
            this.calculateUnitPriceOriginalByAmountOriginal(index);
            this.calculateUnitPrice(index);
            this.calculateMainUnitPrice(index);
        }
        this.calculateUnitPriceOriginal(index);
        this.calculateUnitPrice(index);
        this.calculateAmount(index);
        this.calculateDiscountAmountOriginal(index);
        this.calculateDiscountAmount(index);
        this.calculateVatAmountOriginal(index);
        this.calculateVatAmount(index);
        this.updateSaBill();
    }

    changeVatAmountOriginal(index) {
        this.calculateVatAmount(index);
    }

    calculateDiscountAmount(index) {
        if (
            (this.saBillDetails[index].discountAmountOriginal || this.saBillDetails[index].discountAmountOriginal === 0) &&
            (this.saBill.exchangeRate || this.saBill.exchangeRate === 0)
        ) {
            if (this.saBill.exchangeRate && this.currency) {
                if (this.currency.formula === '*') {
                    this.saBillDetails[index].discountAmount = this.saBillDetails[index].discountAmountOriginal * this.saBill.exchangeRate;
                } else if (this.currency.formula === '/' && this.saBill.exchangeRate !== 0) {
                    this.saBillDetails[index].discountAmount = this.saBillDetails[index].discountAmountOriginal / this.saBill.exchangeRate;
                }
                this.utilService.round(this.saBillDetails[index].discountAmount, this.currentAccount.systemOption, 7);
            }
        }
    }

    calculateDiscountAmountOriginal(index) {
        if (this.saBillDetails[index].amountOriginal && this.saBillDetails[index].discountRate) {
            this.saBillDetails[index].discountAmountOriginal =
                this.saBillDetails[index].amountOriginal * this.saBillDetails[index].discountRate / 100;
            this.utilService.round(
                this.saBillDetails[index].discountAmountOriginal,
                this.currentAccount.systemOption,
                this.saBill.currencyID !== this.currencyCode ? 8 : 7
            );
        } else {
            this.saBillDetails[index].discountAmountOriginal = 0;
            this.saBillDetails[index].discountAmount = 0;
        }
    }

    changeDiscountRate(index) {
        if (this.saBillDetails[index].amountOriginal && this.saBillDetails[index].discountRate) {
            this.saBillDetails[index].discountAmountOriginal =
                this.saBillDetails[index].amountOriginal * this.saBillDetails[index].discountRate / 100;
            this.utilService.round(
                this.saBillDetails[index].discountAmountOriginal,
                this.currentAccount.systemOption,
                this.saBill.currencyID !== this.currencyCode ? 8 : 7
            );

            this.calculateDiscountAmount(index);
        } else {
            this.saBillDetails[index].discountAmountOriginal = 0;
            this.saBillDetails[index].discountAmount = 0;
        }
        this.checkSaleDiscountPolicy(index);
        this.changeVATRate(index);
        this.updateSaBill();
    }

    changeDiscountAmountOriginal(index) {
        if (this.saBillDetails[index].discountAmountOriginal || this.saBillDetails[index].discountAmountOriginal === 0) {
            this.saBillDetails[index].discountRate =
                this.saBillDetails[index].discountAmountOriginal / this.saBillDetails[index].amountOriginal * 100;
            this.calculateDiscountAmount(index);
        }
        this.changeVATRate(index);
    }

    changeVATRate(index) {
        this.calculateVatAmountOriginal(index);
        this.calculateVatAmount(index);
        this.updateSaBill();
    }

    calculateVatAmountOriginal(index) {
        if (
            (this.saBillDetails[index].amountOriginal ||
                this.saBillDetails[index].amountOriginal === 0 ||
                this.saBillDetails[index].discountAmountOriginal ||
                this.saBillDetails[index].discountAmountOriginal === 0) &&
            (this.saBillDetails[index].vatRate === 1 || this.saBillDetails[index].vatRate === 2)
        ) {
            if (!this.saBillDetails[index].amountOriginal) {
                this.saBillDetails[index].amountOriginal = 0;
            }
            if (!this.saBillDetails[index].discountAmountOriginal) {
                this.saBillDetails[index].discountAmountOriginal = 0;
            }
            this.saBillDetails[index].vatAmountOriginal =
                (this.saBillDetails[index].amountOriginal - this.saBillDetails[index].discountAmountOriginal) *
                (this.saBillDetails[index].vatRate === 1 ? 0.05 : 0.1);
            this.utilService.round(
                this.saBillDetails[index].vatAmountOriginal,
                this.currentAccount.systemOption,
                this.saBill.currencyID !== this.currencyCode ? 8 : 7
            );
        } else {
            this.saBillDetails[index].vatAmountOriginal = 0;
            this.saBillDetails[index].vatAmount = 0;
        }
    }

    calculateVatAmount(index) {
        if (this.saBill.exchangeRate && this.currency) {
            if (this.currency.formula === '*') {
                this.saBillDetails[index].vatAmount = this.saBillDetails[index].vatAmountOriginal * this.saBill.exchangeRate;
            } else if (this.currency.formula === '/' && this.saBill.exchangeRate !== 0) {
                this.saBillDetails[index].vatAmount = this.saBillDetails[index].vatAmountOriginal / this.saBill.exchangeRate;
            }
            this.utilService.round(this.saBillDetails[index].vatAmount, this.currentAccount.systemOption, 7);
            this.updateSaBill();
        }
    }

    changeLotNo(detail: ISaBillDetails) {
        if (detail.lotNo) {
            const data = detail.lotNos.filter(x => x.lotNo === detail.lotNo);
            if (data && data.length > 0) {
                const selected = detail.lotNos.find(x => x.lotNo === detail.lotNo);
                detail.expiryDate = moment(selected.expiryDate);
            }
        }
    }

    changeReason() {
        if (this.isEditReasonFirst) {
            this.isEditReasonFirst = false;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.XuatHoaDon_Sua])
    edit() {
        event.preventDefault();
        if (!this.utilService.isShowPopup) {
            this.isEdit = true;
            this.saBillCopy = Object.assign({}, this.saBill);
            this.saBillDetailsCopy = this.saBillDetails.map(object => ({ ...object }));
        }
    }

    closeForm() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (!this.canDeactive() && !this.utilService.isShowPopup) {
            this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
        } else if (!this.utilService.isShowPopup) {
            this.close();
        }
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.previousState();
        this.isEdit = false;
        this.saBill = Object.assign({}, this.saBillCopy);
        this.saBillDetails = this.saBillDetailsCopy ? this.saBillDetailsCopy.map(object => ({ ...object })) : [];
    }

    @ebAuth(['ROLE_ADMIN', ROLE.XuatHoaDon_Them])
    addNew($event = null) {
        if ($event) {
            $event.preventDefault();
        }
        if (!this.utilService.isShowPopup) {
            this.isEdit = true;
            this.saBill.invoiceDate = null;
            this.saBill = { typeID: TypeID.XUAT_HOA_DON };
            this.template = null;
            this.saBillDetails = [];
            this.accountingObjectBankAccount = {};
            this.template = {};
            this.saBill.typeLedger = 2;
            this.saBill.currencyID = this.currentAccount.organizationUnit.currencyID;
            this.currency = this.currencies.find(cur => cur.currencyCode === this.currentAccount.organizationUnit.currencyID);
            this.saBill.exchangeRate = this.currency.exchangeRate;
            this.viewVouchersSelected = [];
            this.createFrom = 0;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.XuatHoaDon_Them])
    copyAndNew() {
        if (this.saBill && this.saBill.id && !this.utilService.isShowPopup) {
            this.saBill.id = null;
            this.saBill.statusInvoice = null;
            this.saBill.iDReplaceInv = null;
            this.saBill.iDAdjustInv = null;
            this.saBillDetails.forEach(item => {
                item.id = null;
                item.saInvoiceID = null;
                item.saInvoiceDetailID = null;
                item.saReturnDetailID = null;
                item.ppDiscountReturnDetailID = null;
            });
            this.isEdit = true;
            this.saBill.invoiceNo = '';
            this.createFrom = 0;
        }
    }

    registerChangeInAccountDefaults() {
        this.eventSubscriber = this.eventManager.subscribe('xuatHoaDonModification', response => {
            if (this.modalRef) {
                this.modalRef.close();
            }
            this.previousState();
        });
    }

    registerRef() {
        this.eventSubscriber = this.eventManager.subscribe('selectViewVoucher', response => {
            if (this.isEdit) {
                this.viewVouchersSelected = this.viewVouchersSelected.filter(x => x.attach === true);
                response.content.forEach(item => {
                    item.attach = false;
                    this.viewVouchersSelected.push(item);
                });
            }
        });
    }

    updateSaBill() {
        this.saBill.totalAmount = this.utilService.round(this.sum('amount'), this.currentAccount.systemOption, 7);
        this.saBill.totalVATAmount = this.utilService.round(this.sum('vatAmount'), this.currentAccount.systemOption, 7);
        this.saBill.totalDiscountAmount = this.utilService.round(this.sum('discountAmount'), this.currentAccount.systemOption, 7);
        this.saBill.total = this.utilService.round(
            this.saBill.totalAmount - this.saBill.totalDiscountAmount + this.saBill.totalVATAmount,
            this.currentAccount.systemOption,
            7
        );

        this.saBill.totalAmountOriginal = this.utilService.round(
            this.sum('amountOriginal'),
            this.currentAccount.systemOption,
            this.saBill.currencyID !== this.currencyCode ? 8 : 7
        );
        this.saBill.totalVATAmountOriginal = this.utilService.round(
            this.sum('vatAmountOriginal'),
            this.currentAccount.systemOption,
            this.saBill.currencyID !== this.currencyCode ? 8 : 7
        );
        this.saBill.totalDiscountAmountOriginal = this.utilService.round(
            this.sum('discountAmountOriginal'),
            this.currentAccount.systemOption,
            this.saBill.currencyID !== this.currencyCode ? 8 : 7
        );
        this.saBill.totalOriginal = this.utilService.round(
            this.saBill.totalAmountOriginal - this.saBill.totalDiscountAmountOriginal + this.saBill.totalVATAmountOriginal,
            this.currentAccount.systemOption,
            this.saBill.currencyID !== this.currencyCode ? 8 : 7
        );
        this.utilService.roundObjects(this.saBillDetails, this.currentAccount.systemOption);
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.saBillDetails.length; i++) {
            total += isNaN(this.saBillDetails[i][prop]) ? 0 : this.saBillDetails[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    ngOnDestroy(): void {
        if (this.eventSubscriber) {
            this.eventManager.destroy(this.eventSubscriber);
        }
        if (this.eventSubscriber2) {
            this.eventManager.destroy(this.eventSubscriber2);
        }
        if (this.eventSubscriber3) {
            this.eventManager.destroy(this.eventSubscriber3);
        }
    }

    onChangeCreateFrom() {
        if (this.createFrom) {
            if (this.saBill.id) {
                this.saBillList = [];
                this.saInvoiceService
                    .getSAInvoiceBySABillID({
                        saBillID: this.saBill.id
                    })
                    .subscribe(res => {
                        if (res.body) {
                            for (let i = 0; i < res.body.length; i++) {
                                this.saBillList.push(Object.assign({}));
                                this.saBillList[i].id = res.body[i];
                                this.saBillList[i].saInvoiceID = res.body[i];
                            }
                        }
                        this.modalRef = this.refModalService.open(
                            this.saBillList,
                            EbSaInvoiceModalComponent,
                            this.createFrom,
                            false,
                            this.saBill.typeID,
                            null,
                            this.saBill.currencyID,
                            this.saBill.id ? this.saBill.id : null,
                            this.saBill.accountingObject ? this.saBill.accountingObject.id : null
                        );
                    });
            } else {
                this.modalRef = this.refModalService.open(
                    this.saBillList,
                    EbSaInvoiceModalComponent,
                    this.createFrom,
                    false,
                    this.saBill.typeID,
                    null,
                    this.saBill.currencyID,
                    this.saBill.id ? this.saBill.id : null,
                    this.saBill.accountingObject ? this.saBill.accountingObject.id : null
                );
            }
        }
    }
    public beforeChange($event: NgbTabChangeEvent) {
        if ($event.nextId === 'reference') {
            $event.preventDefault();
            if (this.isEdit && !this.isLoading) {
                this.modalRef = this.refModalService.open(this.viewVouchersSelected);
            }
        }
    }

    checkInvoiceNo() {
        if (this.saBill.invoiceNo) {
            this.saBill.invoiceNo = this.utilService.pad(this.saBill.invoiceNo, 7);
        }
    }

    numberOnly() {
        if (this.saBill.invoiceNo) {
            try {
                const last = this.saBill.invoiceNo.charAt(this.saBill.invoiceNo.length - 1);
                const pat = parseInt(last, 10);
                if (last === 'e' || isNaN(pat)) {
                    this.saBill.invoiceNo = this.saBill.invoiceNo.substr(0, 0);
                }
            } catch (e) {
                this.saBill.invoiceNo = this.saBill.invoiceNo.substr(0, 0);
            }
        }
    }

    checkSaleDiscountPolicy(index) {
        if (this.isSelfChange) {
            return;
        }
        if (this.saBillDetails[index].quantity && this.saBillDetails[index].saleDiscountPolicys) {
            let goto = true;
            this.saBillDetails[index].saleDiscountPolicys.forEach(saleDiscountPolicy => {
                if (
                    saleDiscountPolicy.quantityFrom <= this.saBillDetails[index].quantity &&
                    this.saBillDetails[index].quantity <= saleDiscountPolicy.quantityTo
                ) {
                    goto = false;
                    if (saleDiscountPolicy.discountType === 0) {
                        this.saBillDetails[index].discountRate = saleDiscountPolicy.discountResult;
                        if (
                            (this.saBillDetails[index].amountOriginal || this.saBillDetails[index].amountOriginal === 0) &&
                            (this.saBillDetails[index].discountRate || this.saBillDetails[index].discountRate === 0)
                        ) {
                            this.saBillDetails[index].discountAmountOriginal =
                                this.saBillDetails[index].amountOriginal * this.saBillDetails[index].discountRate / 100;
                            if (this.saBill.exchangeRate || this.saBill.exchangeRate === 0) {
                                this.saBillDetails[index].discountAmount =
                                    this.saBillDetails[index].discountAmountOriginal * this.saBill.exchangeRate;
                            }
                        } else {
                            this.saBillDetails[index].discountAmountOriginal = 0;
                            this.saBillDetails[index].discountAmount = 0;
                        }
                    } else if (saleDiscountPolicy.discountType === 1) {
                        this.saBillDetails[index].discountAmountOriginal = saleDiscountPolicy.discountResult;
                        this.saBillDetails[index].discountAmount =
                            this.saBillDetails[index].discountAmountOriginal * this.saBill.exchangeRate;
                        this.saBillDetails[index].discountRate = null;
                    } else if (saleDiscountPolicy.discountType === 2) {
                        this.saBillDetails[index].discountAmountOriginal =
                            saleDiscountPolicy.discountResult * this.saBillDetails[index].quantity;
                        this.saBillDetails[index].discountAmount =
                            this.saBillDetails[index].discountAmountOriginal * this.saBill.exchangeRate;
                        this.saBillDetails[index].discountRate = null;
                    }
                }
                if (goto) {
                    this.saBillDetails[index].discountRate = this.saBillDetails[index].materialGoods.saleDiscountRate;
                }
            });
        }
    }

    canDeactive() {
        if (!this.isEdit) {
            return true;
        } else {
            return (
                this.utilService.isEquivalent(this.saBill, this.saBillCopy) &&
                this.utilService.isEquivalentArray(this.saBillDetails, this.saBillDetailsCopy)
            );
        }
    }

    exportPdf(isDownload, typeReports: number) {
        this.utilService.getCustomerReportPDF({
            id: this.saBill.id,
            typeID: this.saBill.typeID,
            typeReport: typeReports
        });
        if (typeReports === 11) {
            this.toastrService.success(
                this.translateService.instant('ebwebApp.mBDeposit.printing') +
                    this.translateService.instant('ebwebApp.sAInvoice.saleInvoice') +
                    '...',
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
        } else if (typeReports === 10) {
            this.toastrService.success(
                this.translateService.instant('ebwebApp.mBDeposit.printing') +
                    this.translateService.instant('ebwebApp.sAInvoice.vatInvoice') +
                    '...',
                this.translateService.instant('ebwebApp.mBDeposit.message')
            );
        }
    }

    registerSaInvoice() {
        this.eventSubscriber2 = this.eventManager.subscribe('selectSaInvoice', response => {
            for (let i = 0; i < this.saBillDetails.length; i++) {
                if (
                    this.saBillDetails[i].saInvoiceDetailID ||
                    this.saBillDetails[i].saReturnDetailID ||
                    this.saBillDetails[i].ppDiscountReturnDetailID
                ) {
                    this.saBillDetails.splice(i, 1);
                    i--;
                }
            }
            this.saBillList = response.content;
            if (
                response.content &&
                response.content.length ===
                    response.content.filter(x => x.accountingObjectID === response.content[0].accountingObjectID).length
            ) {
                if (this.accountingObjects.some(x => x.id === response.content[0].accountingObjectID)) {
                    this.saBill.accountingObject = this.accountingObjects.find(x => x.id === response.content[0].accountingObjectID);
                    this.selectAccountingObject();
                }
            }
            this.viewVouchersSelected = this.viewVouchersSelected.filter(x => !x.attach);
            this.saBillList.forEach(object => {
                this.saBillDetails.push(this.convertItemSelect(object));
                const groupType = parseInt(object.typeID.toString().substring(0, 2), 10);
                if (this.viewVouchersSelected.filter(x => x.refID2 === object.saInvoiceID).length === 0) {
                    this.viewVouchersSelected.push({
                        id: null,
                        refID1: null,
                        refID2: object.saInvoiceID,
                        no: object.noMBook + object.noFBook,
                        date: moment(object.date).format(DATE_FORMAT_SLASH),
                        postedDate: moment(object.date).format(DATE_FORMAT_SLASH),
                        reason: object.reason,
                        typeID: object.typeID,
                        typeGroupID: groupType,
                        attach: true
                    });
                }
            });
            this.updateSaBill();
        });
    }

    convertItemSelect(item) {
        const target = new SaBillDetails();
        Object.assign(target, item);
        target.debitAccount = item.creditAccount;
        target.creditAccount = item.debitAccount;
        if (this.createFrom === TypeID.BAN_HANG_CHUA_THU_TIEN) {
            target.saInvoiceDetailID = item.id;
            target.saInvoiceID = item.saInvoiceID;
        } else if (this.createFrom === TypeID.HANG_GIAM_GIA) {
            target.saReturnDetailID = item.id;
            target.saInvoiceID = item.saInvoiceID;
        } else if (this.createFrom === TypeID.MUA_HANG_TRA_LAI) {
            target.ppDiscountReturnDetailID = item.id;
            target.saInvoiceID = item.saInvoiceID;
        }
        if (item.units) {
            target.units = item.units;
            if (item.unitID) {
                target.unit = item.units.find(n => n.id === item.unitID);
            }
            if (item.mainUnitID) {
                target.mainUnit = item.units.find(i => i.id === item.mainUnitID);
            }
        }
        target.isPromotion = item.promotion;
        target.materialGoods = this.materialGoodss.find(n => n.id === item.materialGoodsID);
        target.unit = this.units.find(n => n.id === item.unitID);
        target.mainUnit = this.units.find(n => n.id === item.mainUnitID);
        return target;
    }

    checkRequiredInvoiceNo() {
        return (
            (this.isRequiredInvoiceNo || (!this.isRequiredInvoiceNo && this.template && this.template.invoiceForm !== 0)) &&
            !this.saBill.invoiceNo
        );
    }

    disableInvoiceNo() {
        return this.isEdit && !this.isRequiredInvoiceNo && this.template && this.template.invoiceForm === 0;
    }

    changeMainConvertRate(index) {
        this.calculateMainQuantity(index);
        this.calculateMainUnitPrice(index);
    }

    changeMainQuantity(index) {
        this.calculateQuantity(index);
        this.changeAmountOriginal(index);
    }

    changeMainUnitPrice(index) {
        this.calculateUnitPriceOriginal(index);
        this.calculateUnitPrice(index);
        this.changeAmountOriginal(index);
    }

    calculateQuantity(index) {
        if (this.saBillDetails[index].mainQuantity) {
            this.saBillDetails[index].quantity =
                this.saBillDetails[index].formula === '*'
                    ? this.saBillDetails[index].mainQuantity / this.saBillDetails[index].mainConvertRate
                    : this.saBillDetails[index].mainQuantity * this.saBillDetails[index].mainConvertRate;

            this.utilService.round(this.saBillDetails[index].quantity, this.currentAccount.systemOption, 3);
        }
    }

    calculateMainQuantity(index) {
        if (this.saBillDetails[index].mainConvertRate) {
            this.saBillDetails[index].mainQuantity =
                this.saBillDetails[index].formula === '*'
                    ? this.saBillDetails[index].quantity * this.saBillDetails[index].mainConvertRate
                    : this.saBillDetails[index].quantity / this.saBillDetails[index].mainConvertRate;
            this.utilService.round(this.saBillDetails[index].mainQuantity, this.currentAccount.systemOption, 3);
        } else {
            this.saBillDetails[index].mainQuantity = null;
        }
    }

    calculateMainUnitPrice(index) {
        if (this.saBillDetails[index].formula === '*' && this.saBillDetails[index].mainConvertRate) {
            this.saBillDetails[index].mainUnitPrice =
                this.saBillDetails[index].unitPriceOriginal / this.saBillDetails[index].mainConvertRate;
        } else {
            this.saBillDetails[index].mainUnitPrice =
                this.saBillDetails[index].unitPriceOriginal * this.saBillDetails[index].mainConvertRate;
        }
        this.utilService.round(this.saBillDetails[index].mainUnitPrice, this.currentAccount.systemOption, 1);
    }

    calculateUnitPriceOriginal(index) {
        if (this.saBillDetails[index].mainConvertRate && this.saBillDetails[index].mainUnitPrice) {
            this.saBillDetails[index].unitPriceOriginal =
                this.saBillDetails[index].formula === '*'
                    ? this.saBillDetails[index].mainUnitPrice * this.saBillDetails[index].mainConvertRate
                    : this.saBillDetails[index].mainUnitPrice / this.saBillDetails[index].mainConvertRate;
        }
        this.utilService.round(this.saBillDetails[index].unitPriceOriginal, this.currentAccount.systemOption, 1);
    }

    calculateUnitPrice(index) {
        if (this.saBill.exchangeRate && this.currency) {
            if (this.currency.formula === '*') {
                this.saBillDetails[index].unitPrice = this.saBillDetails[index].unitPriceOriginal * this.saBill.exchangeRate;
            } else if (this.currency.formula === '/' && this.saBill.exchangeRate !== 0) {
                this.saBillDetails[index].unitPrice = this.saBillDetails[index].unitPriceOriginal / this.saBill.exchangeRate;
            }
            this.utilService.round(this.saBillDetails[index].discountAmount, this.currentAccount.systemOption, 7);
        }
    }

    calculateUnitPriceOriginalByAmountOriginal(index) {
        if (this.saBillDetails[index].amountOriginal && this.saBillDetails[index].quantity) {
            this.saBillDetails[index].unitPriceOriginal = this.saBillDetails[index].amountOriginal / this.saBillDetails[index].quantity;
        } else {
            this.saBillDetails[index].unitPriceOriginal = 0;
        }
        this.utilService.round(
            this.saBillDetails[index].unitPriceOriginal,
            this.currentAccount.systemOption,
            this.saBill.currencyID !== this.currencyCode ? 2 : 1
        );
    }

    @ebAuth(['ROLE_ADMIN', ROLE.XuatHoaDon_Xoa])
    delete() {
        if (!this.isEdit && !this.utilService.isShowPopup) {
            this.saBillService
                .checkRelateVoucher({
                    sABillID: this.saBill.id
                })
                .subscribe((res: HttpResponse<boolean>) => {
                    this.warningDelete = res.body;
                    if (this.saBill.invoiceForm === 0 && !this.isRequiredInvoiceNo) {
                        if (this.saBill.invoiceNo) {
                            this.typeDelete = 3;
                        } else {
                            this.typeDelete = 0;
                        }
                    } else {
                        if (this.saBill.invoiceNo) {
                            this.typeDelete = 1;
                        } else {
                            this.typeDelete = 0;
                        }
                    }
                    this.modalRef = this.modalService.open(this.deleteItem, { size: 'lg', backdrop: 'static' });
                });
        }
    }

    deleteBill() {
        if (this.saBill.id) {
            if (this.typeDelete === 3) {
                this.toastrService.error(this.translateService.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
            } else {
                this.saBillService.delete(this.saBill.id).subscribe(
                    ref => {
                        this.toastrService.success(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.success'));
                        if (this.modalRef) {
                            this.modalRef.close();
                        }
                        this.close();
                    },
                    ref => {
                        this.toastrService.error(this.translateService.instant('ebwebApp.pPDiscountReturn.delete.error'));
                    }
                );
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.XuatHoaDon_Them])
    saveAndNew() {
        event.preventDefault();
        if (this.isEdit && !this.utilService.isShowPopup) {
            if (this.saBillDetails.filter(x => !x.vatRate && x.vatRate !== 0 && x.vatAmountOriginal > 0).length > 0) {
                this.warningVatRate = 1;
            } else if (this.saBillDetails.filter(x => x.vatRate !== 1 && x.vatRate !== 2 && x.vatAmountOriginal > 0).length > 0) {
                this.warningVatRate = 2;
            }
            if (this.warningVatRate === 1 || this.warningVatRate === 2) {
                this.isSaveAndAdd = true;
                this.modalRef = this.modalService.open(this.contentSave, { backdrop: 'static' });
            } else {
                this.saveAll(true);
            }
        }
    }

    copyRow(detail, isRigthClick?) {
        if (!this.getSelectionText() || isRigthClick) {
            const detailCopy: any = Object.assign({}, detail);
            detailCopy.id = null;
            this.saBillDetails.push(detailCopy);
            this.updateSaBill();
            if (this.indexFocusDetailCol !== null && this.indexFocusDetailCol !== undefined) {
                const id = this.idIndex;
                const row = this.saBillDetails.length - 1;
                this.indexFocusDetailRow = row;
                setTimeout(function() {
                    const element: HTMLElement = document.getElementById(id + row);
                    if (element) {
                        element.focus();
                    }
                }, 0);
            }
        }
    }

    addIfLastInput(i) {
        if (i === this.saBillDetails.length - 1) {
            this.addNewRow();
        }
    }

    ngAfterViewChecked(): void {
        this.disableInput();
    }

    changeIsAttachList() {
        if (!this.saBill.isAttachList) {
            this.saBill.listNo = '';
            this.saBill.listDate = null;
            this.saBill.listCommonNameInventory = '';
        }
    }

    closes() {
        if (this.modalRef) {
            this.warningVatRate = 0;
            this.modalRef.close();
        }
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.saBillDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.saBill;
    }

    addDataToDetail() {
        this.saBillDetails = this.details ? this.details : this.saBillDetails;
        this.saBill = this.parent ? this.parent : this.saBill;
    }
}
