import { AfterViewInit, Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';

import { IMaterialGoods, MaterialGoods } from 'app/shared/model/material-goods.model';
import { MaterialGoodsService } from './material-goods.service';
import { IType } from 'app/shared/model/type.model';
import { IMaterialGoodsAssembly, MaterialGoodsAssembly } from 'app/shared/model/material-goods-assembly.model';
import { ISaleDiscountPolicy, SaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';
import { IMaterialGoodsPurchasePrice, MaterialGoodsPurchasePrice } from 'app/shared/model/material-goods-purchase-price.model';
import { IMaterialGoodsConvertUnit, MaterialGoodsConvertUnit } from 'app/shared/model/material-goods-convert-unit.model';
import { IMaterialGoodsSpecifications } from 'app/shared/model/material-goods-specifications.model';
import { AccountType, CategoryName, FORMULA, SU_DUNG_DM_KHO } from 'app/app.constants';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { IWarranty } from 'app/shared/model/warranty.model';
import { WarrantyService } from 'app/entities/warranty';
import { IRepository } from 'app/shared/model/repository.model';
import { RepositoryService } from 'app/danhmuc/repository';
import { AccountListService } from 'app/danhmuc/account-list';
import { MaterialGoodsCategoryService } from 'app/danhmuc/material-goods-category';
import { IMaterialGoodsSpecialTaxGroup } from 'app/shared/model/material-goods-special-tax-group.model';
import { MaterialGoodsSpecialTaxGroupService } from 'app/danhmuc/material-goods-special-tax-group';
import { ICareerGroup } from 'app/shared/model/career-group.model';
import { CareerGroupService } from 'app/entities/career-group';
import { ICurrency } from 'app/shared/model/currency.model';
import { Principal } from 'app/core';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { SaleDiscountPolicyService } from 'app/entities/sale-discount-policy';
import { MaterialGoodsConvertUnitService } from 'app/entities/material-goods-convert-unit';
import { CurrencyService } from 'app/danhmuc/currency';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { MaterialGoodsAssemblyService } from 'app/entities/material-goods-assembly';
import { MaterialGoodsPurchasePriceService } from 'app/entities/material-goods-purchase-price';
import { MaterialGoodsSpecificationsService } from 'app/entities/material-goods-specifications';
import { JhiEventManager } from 'ng-jhipster';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { IMBTellerPaperDetails } from 'app/shared/model/mb-teller-paper-details.model';
import { IMBTellerPaperDetailTax } from 'app/shared/model/mb-teller-paper-detail-tax.model';
import { AccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';

@Component({
    selector: 'eb-material-goods-update',
    templateUrl: './material-goods-update.component.html',
    styleUrls: ['./material-goods-update.component.css']
})
export class MaterialGoodsUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _materialGoods: IMaterialGoods;
    isSaving: boolean;
    listColumnsType: any;
    listHeaderColumnsType: any;
    isSaveAndCreate: boolean;
    materialGoodsType: IType[];
    selectedRow: IMaterialGoods;
    materialGood: IMaterialGoods[];
    materialGoodsAssemblys: IMaterialGoodsAssembly[];
    saleDiscountPolicy: ISaleDiscountPolicy[];
    saleDiscountPolicy1: ISaleDiscountPolicy[];
    materialGoodsPurchasePrice: IMaterialGoodsPurchasePrice[];
    materialGoodsConvertUnit: IMaterialGoodsConvertUnit[];
    materialGoodsSpecifications: IMaterialGoodsSpecifications[];
    contextMenu: ContextMenu;
    isCreateUrl: boolean;
    UnitIdArray: string[];
    val: any;
    isFollowAll: boolean;
    isAdd: boolean;
    materialGoodsTypeList = [
        { id: 0, name: 'Vật tư hàng hóa' },
        { id: 1, name: 'VTHH lắp ráp/tháo dỡ' },
        { id: 2, name: 'Dịch vụ' },
        { id: 3, name: 'Thành phẩm' },
        { id: 4, name: 'Khác' }
    ];
    vatTaxRates = [
        { id: 0, name: '0%' },
        { id: 1, name: '5%' },
        { id: 2, name: '10%' },
        { id: 3, name: 'KCT' },
        {
            id: 4,
            name: 'KTT'
        }
    ];
    SaleDiscountPolicys = [
        { id: 0, name: 'Theo %' },
        { id: 1, name: 'Theo số tiền' },
        { id: 2, name: 'Theo đơn giá(Số tiền CK/1 đơn vị SL)' }
    ];
    units: IUnit[];
    materialGoodsCategoryPopup: IMaterialGoodsCategory[];
    listColumnsUnitId: any;
    listHeaderColumnsUnitId: any;
    listColumnsmaterialGoodsCategory = ['materialGoodsCategoryCode', 'materialGoodsCategoryName'];
    listHeaderColumnsmaterialGoodsCategor = ['Mã loại', 'Tên loại'];
    materialGoodsCategories: IMaterialGoodsCategory[];
    warrantys: IWarranty[];
    listColumnsWarranty = ['warrantyName'];
    listHeaderColumnsWarranty = ['Thời gian'];
    repositoryPopup: IRepository[];
    listColumnsRepository = ['repositoryName', 'repositoryCode'];
    listHeaderColumnsRepository = ['Tên kho', 'Mã kho'];
    accounts: IAccountList[];
    accounts1: IAccountList[];
    listColumnsAccountList = ['accountNumber', 'accountName'];
    listHeaderColumnsAccountList = ['Số tài khoản', 'Tên tài khoản'];
    materialGoodsSpecialTaxGroup: IMaterialGoodsSpecialTaxGroup[];
    listColumnsMaterialGoodsSpecialTaxGroup = ['materialGoodsSpecialTaxGroupCode', 'materialGoodsSpecialTaxGroupName'];
    listHeaderColumnsMaterialGoodsSpecialTaxGroup = ['Mã HHDV', 'Tên HHDV'];
    careerGroup: ICareerGroup[];
    listColumnsCareerGroup = ['careerGroupName'];
    listHeaderColumnsCareerGroup = ['Tên nhóm ngành nghề'];
    isEdit: any;
    account: any;
    materialGoodss: MaterialGoods[];
    currencys: ICurrency[];
    isClose: boolean;
    materialGoodsCopy: IMaterialGoods;
    modalRef: NgbModalRef;
    materialGoodsName: string;
    currentAccount: any;
    taxCalculationMethod: any;
    eventSubscriber: Subscription;
    currentRow: number;
    page: number;
    itemsPerPage: any;
    details: any[];
    isGetAllCompany: any;
    ROLE_VatTuHangHoa_Them = ROLE.DanhMucVatTuHangHoa_Them;
    ROLE_VatTuHangHoa_Sua = ROLE.DanhMucVatTuHangHoa_Sua;
    ROLE_VatTuHangHoa_Xoa = ROLE.DanhMucVatTuHangHoa_Xoa;
    ROLE_VatTuHangHoa_Xem = ROLE.DanhMucVatTuHangHoa_Xem;
    arrAuthorities: any[];
    isRoleSua: boolean;
    isRoleThem: boolean;
    isEditUrl: boolean;
    select: number;
    warningMessage: any;
    newRow: any;

    constructor(
        private materialGoodsService: MaterialGoodsService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private unitService: UnitService,
        private materialGoodsCategoryService: MaterialGoodsCategoryService,
        private repositoryService: RepositoryService,
        private accountListService: AccountListService,
        private warrantyService: WarrantyService,
        private materialGoodsSpecialTaxGroupService: MaterialGoodsSpecialTaxGroupService,
        private careerGroupService: CareerGroupService,
        private principal: Principal,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private saleDiscountPolicyService: SaleDiscountPolicyService,
        private materialGoodsConvertUnitService: MaterialGoodsConvertUnitService,
        private currencyService: CurrencyService,
        private modalService: NgbModal,
        private materialGoodsAssemblyService: MaterialGoodsAssemblyService,
        private materialGoodsPurchasePriceService: MaterialGoodsPurchasePriceService,
        private materialGoodsSpecificationsService: MaterialGoodsSpecificationsService,
        private eventManager: JhiEventManager,
        public translateService: TranslateService,
        private toasService: ToastrService
    ) {
        super();
        this.contextMenu = new ContextMenu();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.arrAuthorities = account.authorities;
            this.isEditUrl = window.location.href.includes('edit');
            this.isCreateUrl = window.location.href.includes('/new');
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_VatTuHangHoa_Sua) : true;
            this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_VatTuHangHoa_Them)
                : true;
            this.isEnter = this.currentAccount.systemOption.filter(n => n.code === 'TCKHAC_Enter')[0].data === '1';
            this.eventManager.broadcast({
                name: 'checkEnter',
                content: this.isEnter
            });
        });
    }

    ngOnInit() {
        this.isSaving = false;
        this.warrantys = [];
        this.units = [];
        this.materialGoodsCategoryPopup = [];
        this.materialGoodsAssemblys = [];
        this.saleDiscountPolicy = [];
        this.saleDiscountPolicy1 = [];
        this.materialGoodsPurchasePrice = [];
        this.materialGoodsConvertUnit = [];
        this.materialGoodsSpecifications = [];
        this.materialGoodsCategories = [];
        this.materialGoodsSpecialTaxGroup = [];
        this.accounts = [];
        this.accounts1 = [];
        this.val = 1;
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.taxCalculationMethod = this.currentAccount.organizationUnit.taxCalculationMethod;
            this.isGetAllCompany = this.currentAccount.systemOption.find(x => x.code === SU_DUNG_DM_KHO).data === '0';
            this.getListRepositories();
            console.log('taxCalculationMethod: ' + this.taxCalculationMethod);
        });
        this.materialGoodsService.queryForComboboxGood().subscribe((res: HttpResponse<IMaterialGoods[]>) => {
            this.materialGoodss = res.body;
        });

        this.activatedRoute.data.subscribe(({ materialGoods }) => {
            this.materialGoods = materialGoods;
            this.principal.identity(true).then(account => {
                this.account = account;
            });
            if (this.materialGoods && this.materialGoods.id) {
                this.materialGoodsAssemblyService
                    .findByMaterialGoodsID({ id: this.materialGoods.id })
                    .subscribe((res: HttpResponse<IMaterialGoodsAssembly[]>) => {
                        this.materialGoodsAssemblys = res.body;
                        if (this.materialGoodsAssemblys.length > 0) {
                            for (let i = 0; i < this.materialGoodsAssemblys.length; i++) {
                                this.selectMaterialGood(this.materialGoodsAssemblys[i].materialAssemblyID, i);
                            }
                        }
                    });
                this.materialGoodsPurchasePriceService
                    .findByMaterialGoodsID({ id: this.materialGoods.id })
                    .subscribe((res: HttpResponse<IMaterialGoodsPurchasePrice[]>) => {
                        this.materialGoodsPurchasePrice = res.body;
                    });
                this.materialGoodsSpecificationsService
                    .findByMaterialGoodsID({ id: this.materialGoods.id })
                    .subscribe((res: HttpResponse<IMaterialGoodsSpecifications[]>) => {
                        this.materialGoodsSpecifications = res.body;
                    });
                this.saleDiscountPolicyService
                    .findByMaterialGoodsID({ id: this.materialGoods.id })
                    .subscribe((res: HttpResponse<ISaleDiscountPolicy[]>) => {
                        this.saleDiscountPolicy = res.body;
                    });
                this.materialGoodsConvertUnitService
                    .findByMaterialGoodsID({ id: this.materialGoods.id })
                    .subscribe((res: HttpResponse<IMaterialGoodsConvertUnit[]>) => {
                        this.materialGoodsConvertUnit = res.body;
                    });
            } else {
                this.materialGoods.materialGoodsType = 0;
            }
        });
        this.listColumnsUnitId = ['unitName'];
        this.listHeaderColumnsUnitId = ['Đơn vị tính'];
        this.unitService.getAllUnits().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
        });
        this.materialGoodsCategoryService.getMaterialGoodsCategory().subscribe((res: HttpResponse<IMaterialGoodsCategory[]>) => {
            this.materialGoodsCategoryPopup = res.body;
        });
        this.warrantyService.getWarranty().subscribe((res: HttpResponse<IWarranty[]>) => {
            this.warrantys = res.body;
        });

        this.accountListService.getAccountActive().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accounts = res.body;
        });
        this.accountListService.getAccountActive1().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accounts1 = res.body;
        });
        this.materialGoodsSpecialTaxGroupService
            .getMaterialGoodsSpecialTaxGroupByCompanyID()
            .subscribe((res: HttpResponse<IMaterialGoodsSpecialTaxGroup[]>) => {
                this.materialGoodsSpecialTaxGroup = res.body;
            });
        this.careerGroupService.query().subscribe((res: HttpResponse<ICareerGroup[]>) => {
            this.careerGroup = res.body;
        });
        this.saleDiscountPolicyService.query().subscribe((res: HttpResponse<ISaleDiscountPolicy[]>) => {
            this.saleDiscountPolicy1 = res.body;
        });
        this.currencyService.getCurrency().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencys = res.body;
        });
        // this.afterAddRow();
        //         // this.afterDeleteRow();
        //         // this.registerCopyRow();
        this.registerAddNewRow();
        this.registerDeleteRow();
        this.registerCopyRow();
        this.registerAddNewRow1();
        this.registerDeleteRow1();
        this.registerCopyRow1();
        this.registerAddNewRow2();
        this.registerDeleteRow2();
        this.registerCopyRow2();
        this.registerAddNewRow3();
        this.registerDeleteRow3();
        this.registerCopyRow3();
        this.afterAddRow4();
        this.afterDeleteRow4();
        this.isAdd = true;
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.utilsService.setShowPopup(false);
            this.registerComboboxSave(response);
        });
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = true;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('closePopupMaterialGood', response => {
            this.utilsService.setShowPopup(response.content);
        });
        this.isCreateUrl = window.location.href.includes('/material-goods/new');
        this.isEdit = this.isCreateUrl;

        if (this.materialGoods.id !== undefined && !this.isCreateUrl) {
            this.isCreateUrl = false;
        } else {
            this.isCreateUrl = true;
        }
        if (this.utilsService.isShowPopup === undefined || this.utilsService.isShowPopup === null) {
            this.utilsService.isShowPopup = false;
        }
        this.isEdit = this.isCreateUrl;
        this.loadSelectFormula();
        this.copy();
    }

    getListRepositories() {
        this.repositoryService
            .listRepositories({
                isGetAllCompany: this.isGetAllCompany
            })
            .subscribe(res => {
                this.repositoryPopup = res.body;
            });
    }

    previousState() {
        this.router.navigate(['/material-goods']);
    }

    copy() {
        this.materialGoodsCopy = Object.assign({}, this.materialGoods);
    }

    closeEdit(content) {
        if (!this.utilsService.isEquivalent(this.materialGoods, this.materialGoodsCopy)) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else {
            this.close();
        }
    }

    closeForm() {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.materialGoodsCopy) {
            if (!this.utilsService.isEquivalent(this.materialGoods, this.materialGoodsCopy)) {
                this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
                return;
            } else {
                this.closeAll();
                return;
            }
        } else {
            this.copy();
            this.closeAll();
            return;
        }
    }

    closeAll() {
        this.router.navigate(['/material-goods']);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucVatTuHangHoa_Them, ROLE.DanhMucVatTuHangHoa_Sua])
    save() {
        event.preventDefault();
        if (!this.utilsService.isShowPopup) {
            this.warningMessage = '';
            this.isSaving = true;
            if (this.modalRef) {
                this.modalRef.close();
            }
            if (this.materialGoods.isSecurity === null || this.materialGoods.isSecurity === undefined) {
                this.materialGoods.isSecurity = false;
            }
            this.isSaveAndCreate = false;
            if (!this.materialGoods.materialGoodsCode) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.materialGoods.materialGoodsCodeNotNull'),
                    this.translate.instant('ebwebApp.materialGoods.message')
                );
                return false;
            }
            if (!this.materialGoods.materialGoodsName) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.materialGoods.materialGoodsNameNotNull'),
                    this.translate.instant('ebwebApp.materialGoods.message')
                );
                return false;
            }
            if (this.materialGoods.saleDiscountRate > 100 || this.materialGoods.purchaseDiscountRate > 100) {
                this.toasService.warning(
                    this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
                return false;
            }
            if (this.saleDiscountPolicy.filter(t => t.discountType === 0 && t.discountResult > 100).length > 0) {
                this.toasService.warning(
                    this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
                return false;
            }
            const errorData = this.materialGoodsAssemblys.filter(item => !item.materialAssemblyID);
            if (errorData && errorData.length > 0) {
                this.toasService.error(
                    this.translateService.instant('ebwebApp.materialGoods.materialGoodsCodeNotNull', 'ebwebApp.sAInvoice.error.error')
                );
                return false;
            }
            if (this.materialGoods.isFollow === true && this.materialGoodsSpecifications.length === 0) {
                this.toasService.error(
                    this.translateService.instant('ebwebApp.materialGoods.materialGoodsSpecificationsCheck'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
                return false;
            }
            if (
                this.materialGoods.isFollow === true &&
                (this.materialGoodsSpecifications[0].materialGoodsSpecificationsName === null ||
                    this.materialGoodsSpecifications[0].materialGoodsSpecificationsName === undefined)
            ) {
                this.toasService.error(
                    this.translateService.instant('ebwebApp.materialGoods.materialGoodsSpecificationsCheck'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
                return false;
            }
            if (this.checkQuantity() && this.saleDiscountPolicy.length > 1) {
                return false;
            }
            if (this.checkError()) {
                return;
            }
            for (let i = 0; i < this.saleDiscountPolicy.length; i++) {
                if (this.saleDiscountPolicy[i].quantityFrom > this.saleDiscountPolicy[i].quantityTo) {
                    this.toastr.error(
                        this.translateService.instant('ebwebApp.materialGoods.quantityFromCheck'),
                        this.translateService.instant('ebwebApp.materialGoods.message')
                    );
                    return false;
                }
            }
            this.materialGoods.materialGoodsPurchasePrice = this.materialGoodsPurchasePrice;
            this.materialGoods.materialGoodsConvertUnits = this.materialGoodsConvertUnit;
            this.materialGoodsSpecifications.forEach(item => {
                item.isFollow = this.isFollowAll;
            });
            this.materialGoods.materialGoodsAssembly = this.materialGoodsAssemblys;
            this.materialGoods.saleDiscountPolicy = this.saleDiscountPolicy;
            this.materialGoods.materialGoodsSpecifications = this.materialGoodsSpecifications;
            if (this.materialGoods.id !== undefined) {
                this.subscribeToSaveResponse(this.materialGoodsService.update(this.materialGoods), false);
            } else {
                if (this.materialGoods.isFollow === null || this.materialGoods.isFollow === undefined) {
                    this.materialGoods.isFollow = false;
                }
                this.materialGoods.isActive = true;
                this.subscribeToSaveResponse(this.materialGoodsService.create(this.materialGoods), false);
            }
        }
        // TODO: Save
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucVatTuHangHoa_Them])
    saveAndNew() {
        event.preventDefault();
        if (!this.utilsService.isShowPopup) {
            this.warningMessage = '';
            this.isSaving = true;
            if (this.modalRef) {
                this.modalRef.close();
            }
            if (this.materialGoods.isSecurity === null || this.materialGoods.isSecurity === undefined) {
                this.materialGoods.isSecurity = false;
            }
            this.isSaveAndCreate = true;
            if (!this.materialGoods.materialGoodsCode) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.materialGoods.materialGoodsCodeNotNull'),
                    this.translate.instant('ebwebApp.materialGoods.message')
                );
                return false;
            }
            if (!this.materialGoods.materialGoodsName) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.materialGoods.materialGoodsNameNotNull'),
                    this.translate.instant('ebwebApp.materialGoods.message')
                );
                return false;
            }
            if (this.materialGoods.saleDiscountRate > 100 || this.materialGoods.purchaseDiscountRate > 100) {
                this.toasService.warning(
                    this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
                return false;
            }
            if (this.saleDiscountPolicy.filter(t => t.discountType === 0 && t.discountResult > 100).length > 0) {
                this.toasService.warning(
                    this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
                return false;
            }
            const errorData = this.materialGoodsAssemblys.filter(item => !item.materialAssemblyID);
            if (errorData && errorData.length > 0) {
                this.toasService.error(
                    this.translateService.instant('ebwebApp.materialGoods.materialGoodsCodeNotNull', 'ebwebApp.sAInvoice.error.error')
                );
                return false;
            }
            if (this.materialGoods.isFollow === true && this.materialGoodsSpecifications.length === 0) {
                this.toasService.error(
                    this.translateService.instant('ebwebApp.materialGoods.materialGoodsSpecificationsCheck'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
                return false;
            }
            if (
                this.materialGoods.isFollow === true &&
                (this.materialGoodsSpecifications[0].materialGoodsSpecificationsName === null ||
                    this.materialGoodsSpecifications[0].materialGoodsSpecificationsName === undefined)
            ) {
                this.toasService.error(
                    this.translateService.instant('ebwebApp.materialGoods.materialGoodsSpecificationsCheck'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
                return false;
            }
            if (this.checkQuantity()) {
                return false;
            }
            if (this.checkError()) {
                return;
            }
            for (let i = 0; i < this.saleDiscountPolicy.length; i++) {
                if (this.saleDiscountPolicy[i].quantityFrom > this.saleDiscountPolicy[i].quantityTo) {
                    this.toastr.error(
                        this.translateService.instant('ebwebApp.materialGoods.quantityFromCheck'),
                        this.translateService.instant('ebwebApp.materialGoods.message')
                    );
                    return false;
                }
            }
            this.materialGoods.materialGoodsPurchasePrice = this.materialGoodsPurchasePrice;
            this.materialGoods.materialGoodsAssembly = this.materialGoodsAssemblys;
            this.materialGoodsSpecifications.forEach(item => {
                item.isFollow = this.isFollowAll;
            });
            this.materialGoods.materialGoodsSpecifications = this.materialGoodsSpecifications;
            this.materialGoods.saleDiscountPolicy = this.saleDiscountPolicy;
            this.materialGoods.materialGoodsConvertUnits = this.materialGoodsConvertUnit;
            if (this.materialGoods.id !== undefined) {
                this.subscribeToSaveResponse(this.materialGoodsService.update(this.materialGoods), true);
            } else {
                if (this.materialGoods.isFollow === null || this.materialGoods.isFollow === undefined) {
                    this.materialGoods.isFollow = false;
                }
                this.materialGoods.isActive = true;
                this.subscribeToSaveResponse(this.materialGoodsService.create(this.materialGoods), true);
            }
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>, isNew: boolean) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess(isNew);
                } else {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.materialGoods.check'),
                        this.translate.instant('ebwebApp.materialGoods.message')
                    );
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess(isNew: boolean) {
        this.isClose = true;
        if (this.isSaveAndCreate) {
            this.materialGoods = {};
        } else {
            this.isSaving = false;
            this.previousState();
        }
        if (isNew) {
            this.materialGoods = {};
            this.materialGoodsConvertUnit = [];
            this.materialGoodsSpecialTaxGroup = [];
            this.saleDiscountPolicy = [];
            this.materialGoodsPurchasePrice = [];
            this.materialGoodsSpecifications = [];
            this.materialGoodsAssemblys = [];
            this.router.navigate(['/material-goods/new']);
            this.materialGoods.materialGoodsType = 0;
        }
        this.toastr.success(
            this.translate.instant('ebwebApp.materialGoods.saveDataSuccess'),
            this.translate.instant('ebwebApp.materialGoods.message')
        );
    }

    private onSaveError() {
        this.toastr.error(
            this.translate.instant('ebwebApp.materialGoods.saveDataFailed'),
            this.translate.instant('ebwebApp.materialGoods.message')
        );
        this.isSaving = false;
    }

    get materialGoods() {
        return this._materialGoods;
    }

    set materialGoods(materialGoods: IMaterialGoods) {
        this._materialGoods = materialGoods;
    }

    onSelect(select: IMaterialGoods) {
        this.selectedRow = select;
    }

    trackId(index: number, item: IMaterialGoods) {
        return item.id;
    }

    edit() {
        if (this.selectedRow.id) {
            this.router.navigate(['./material-goods', this.selectedRow.id, 'edit']);
        }
    }

    changeActive(number: number) {
        this.val = number;
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    changMaterialGoodsType(materialGoods: IMaterialGoods) {
        if (this.materialGoods.materialGoodsType === 1) {
            this.addNewRow();
        } else {
            this.materialGoodsAssemblys = [];
        }
    }

    changVatTaxRate(materialGoods: IMaterialGoods) {
        console.log(materialGoods.vatTaxRate);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucVatTuHangHoa_Xoa])
    delete() {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.router.navigate(['/material-goods', { outlets: { popup: this.materialGoods.id + '/delete' } }]);
    }

    sum(prop) {
        let total = 0;
        for (let i = 0; i < this.materialGoodsAssemblys.length; i++) {
            total += this.materialGoodsAssemblys[i][prop];
        }
        return isNaN(total) ? 0 : total;
    }

    addNewRow() {
        this.materialGoodsAssemblys.push({});
        this.materialGoodsAssemblys[this.materialGoodsAssemblys.length - 1].unitPrice = 0;
        this.materialGoodsAssemblys[this.materialGoodsAssemblys.length - 1].quantity = 0;
        this.materialGoodsAssemblys[this.materialGoodsAssemblys.length - 1].totalAmount = 0;
        this.materialGoodsAssemblys[this.materialGoodsAssemblys.length - 1].materialGoods = {};
    }

    addNewRow1() {
        this.saleDiscountPolicy.push({});
        this.saleDiscountPolicy[this.saleDiscountPolicy.length - 1].quantityFrom = 0;
        this.saleDiscountPolicy[this.saleDiscountPolicy.length - 1].quantityTo = 0;
        this.saleDiscountPolicy[this.saleDiscountPolicy.length - 1].discountResult = 0;
    }

    addNewRow2() {
        this.materialGoodsPurchasePrice.push({});
    }

    addNewRow3() {
        this.materialGoodsConvertUnit.push({});
        const order = this.materialGoodsConvertUnit.length;
        this.materialGoodsConvertUnit[this.materialGoodsConvertUnit.length - 1].convertRate = 0;
        this.materialGoodsConvertUnit[this.materialGoodsConvertUnit.length - 1].fixedSalePrice = 0;
        this.materialGoodsConvertUnit[this.materialGoodsConvertUnit.length - 1].salePrice1 = 0;
        this.materialGoodsConvertUnit[this.materialGoodsConvertUnit.length - 1].salePrice2 = 0;
        this.materialGoodsConvertUnit[this.materialGoodsConvertUnit.length - 1].salePrice3 = 0;
        this.materialGoodsConvertUnit[this.materialGoodsConvertUnit.length - 1].formula = '*';
        this.materialGoodsConvertUnit[this.materialGoodsConvertUnit.length - 1].orderNumber = order;
    }

    addNewRow4(event: any) {
        if (this.materialGoodsSpecifications.length < 5) {
            this.materialGoodsSpecifications.push({});
            const code = this.materialGoodsSpecifications.length.toString();
            this.materialGoodsSpecifications[this.materialGoodsSpecifications.length - 1].materialGoodsSpecificationsCode = code;
        } else {
            this.isAdd = false;
        }
    }

    onRightClick($event, data, selectedData, isNew, isDelete, currentRow) {
        this.contextMenu.isNew = isNew;
        this.contextMenu.isCopy = true;
        this.contextMenu.isDelete = isDelete;
        this.contextMenu.isShow = true;
        this.contextMenu.event = $event;
        this.contextMenu.data = data;
        this.contextMenu.selectedData = selectedData;
        this.currentRow = currentRow;
        if (data[currentRow].quantityFrom >= 0) {
            this.contextMenu.checkTag = 1;
        } else if (data[currentRow].orderNumber >= 0) {
            this.contextMenu.checkTag = 3;
        } else if (data[currentRow].quantity >= 0) {
            this.contextMenu.checkTag = 4;
        } else {
            this.contextMenu.checkTag = 2;
        }
    }

    AddnewRow() {
        this.materialGoodsAssemblys.push({});
    }

    registerAddNewRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.addNewRow();
        });
    }

    registerCopyRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            if (this.contextMenu.checkTag === 4) {
                this.copyRow(this.contextMenu.selectedData);
            }
        });
    }

    copyRow(detail) {
        if (!this.getSelectionText()) {
            const detailCopy: any = Object.assign({}, detail);
            detailCopy.id = null;
            this.materialGoodsAssemblys.push(detailCopy);
        }
    }

    registerDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            if (this.contextMenu.checkTag === 4) {
                this.removeRow(this.contextMenu.selectedData);
            }
        });
    }

    removeRow(detail: object) {
        this.materialGoodsAssemblys.splice(this.materialGoodsAssemblys.indexOf(detail), 1);
    }

    KeyPress(detail: object, key: string) {
        switch (key) {
            case 'ctr + delete':
                this.removeRow(detail);
                break;
            case 'ctr + c':
                this.copyRow(detail);
                break;
            case 'ctr + insert':
                this.addNewRow();
                break;
        }
    }

    registerAddNewRow1() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.addNewRow1();
        });
    }

    registerDeleteRow1() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            if (this.contextMenu.checkTag === 1) {
                this.removeRow1(this.contextMenu.selectedData);
            }
        });
    }

    registerCopyRow1() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            if (this.contextMenu.checkTag === 1) {
                this.copyRow1(this.contextMenu.selectedData);
            }
        });
    }

    copyRow1(detail) {
        if (!this.getSelectionText()) {
            const detailCopy: any = Object.assign({}, detail);
            detailCopy.id = null;
            this.saleDiscountPolicy.push(detailCopy);
        }
    }

    removeRow1(detail: object) {
        this.saleDiscountPolicy.splice(this.saleDiscountPolicy.indexOf(detail), 1);
    }

    KeyPress1(detail: object, key: string) {
        switch (key) {
            case 'ctr + delete':
                this.removeRow1(detail);
                break;
            case 'ctr + c':
                this.copyRow1(detail);
                break;
            case 'ctr + insert':
                this.addNewRow1();
                break;
        }
    }

    registerAddNewRow2() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.addNewRow2();
        });
    }

    registerDeleteRow2() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            if (this.contextMenu.checkTag === 2) {
                this.removeRow2(this.contextMenu.selectedData);
            }
        });
    }

    registerCopyRow2() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            if (this.contextMenu.checkTag === 2) {
                this.copyRow2(this.contextMenu.selectedData);
            }
        });
    }

    copyRow2(detail) {
        if (!this.getSelectionText()) {
            const detailCopy: any = Object.assign({}, detail);
            detailCopy.id = null;
            this.materialGoodsPurchasePrice.push(detailCopy);
        }
    }

    removeRow2(detail: object) {
        this.materialGoodsPurchasePrice.splice(this.materialGoodsPurchasePrice.indexOf(detail), 1);
    }

    KeyPress2(detail: object, key: string) {
        switch (key) {
            case 'ctr + delete':
                this.removeRow2(detail);
                break;
            case 'ctr + c':
                this.copyRow2(detail);
                break;
            case 'ctr + insert':
                this.addNewRow2();
                break;
        }
    }

    registerAddNewRow3() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            this.addNewRow3();
        });
    }

    registerDeleteRow3() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            if (this.contextMenu.checkTag === 3) {
                this.removeRow3(this.contextMenu.selectedData);
            }
        });
    }

    registerCopyRow3() {
        this.eventSubscriber = this.eventManager.subscribe('afterCopyRow', response => {
            if (this.contextMenu.checkTag === 3) {
                this.copyRow3(this.contextMenu.selectedData);
            }
        });
    }

    copyRow3(detail) {
        if (!this.getSelectionText()) {
            const detailCopy: any = Object.assign({}, detail);
            detailCopy.id = null;
            this.materialGoodsConvertUnit.push(detailCopy);
        }
    }

    removeRow3(detail: object) {
        this.materialGoodsConvertUnit.splice(this.materialGoodsConvertUnit.indexOf(detail), 1);
    }

    KeyPress3(detail: object, key: string) {
        switch (key) {
            case 'ctr + delete':
                this.removeRow3(detail);
                break;
            case 'ctr + c':
                this.copyRow3(detail);
                break;
            case 'ctr + insert':
                this.addNewRow3();
                break;
        }
    }

    updatePPOrder() {}

    updateSum() {
        this.updatePPOrder();
    }

    keyPress1(value: number, select: number) {
        if (select === 0) {
            this.saleDiscountPolicy.splice(value, 1);
        } else if (select === 1) {
            this.saleDiscountPolicy.splice(value, 1);
        }
        this.updateSum();
    }

    keyPress(value: number, select: number) {
        if (select === 0) {
            this.materialGoodsAssemblys.splice(value, 1);
        } else if (select === 1) {
            this.materialGoodsAssemblys.splice(value, 1);
        }
        this.updateSum();
    }

    keyPress2(value: number, select: number) {
        if (select === 0) {
            this.materialGoodsPurchasePrice.splice(value, 1);
        } else if (select === 1) {
            this.materialGoodsPurchasePrice.splice(value, 1);
        }
        this.updateSum();
    }

    keyPress3(value: number, select: number) {
        if (select === 0) {
            this.materialGoodsConvertUnit.splice(value, 1);
        } else if (select === 1) {
            this.materialGoodsConvertUnit.splice(value, 1);
        }
        this.updateSum();
    }

    keyPress4(value: number, select: number) {
        if (select === 0) {
            this.materialGoodsSpecifications.splice(value, 1);
        } else if (select === 1) {
            this.materialGoodsSpecifications.splice(value, 1);
        }
        this.updateSum();
    }

    selectMaterialGood(id, i) {
        const materialGoodsName1 = this.materialGoodss.find(materialGood => materialGood.id === id).materialGoodsName;
        this.materialGoodsAssemblys[i].materialAssemblyDescription = materialGoodsName1;
    }

    selectRepository(id) {
        const kho = this.repositoryPopup.find(x => x.id === id);
        if (kho) {
            this.materialGoods.reponsitoryAccount = kho.defaultAccount;
        }
    }

    selectUnit(id, i) {
        const unit = this.units.find(un => un.id === id).unitName;
    }

    getUnitPriceOriginals(mg: MaterialGoods) {
        const lst = [];
        if (mg) {
            if (mg.fixedSalePrice) {
                lst.push({ data: mg.fixedSalePrice });
            }
            if (mg.salePrice1) {
                lst.push({ data: mg.salePrice1 });
            }
            if (mg.salePrice2) {
                lst.push({ data: mg.salePrice2 });
            }
            if (mg.salePrice3) {
                lst.push({ data: mg.salePrice3 });
            }
        }
        return lst;
    }

    getUintFromMaterialgoodsByID(materialGoodsID: string) {
        const units_result: IUnit[] = [];
        const mg: IMaterialGoodsConvertUnit[] = this.materialGoodsConvertUnit.filter(n => n.materialGoodsID === materialGoodsID);
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

    selectChangeDescription(index) {
        if (
            this.materialGoods.unitID &&
            this.materialGoodsConvertUnit[index].unitID &&
            this.materialGoodsConvertUnit[index].convertRate &&
            this.materialGoodsConvertUnit[index].formula
        ) {
            const currentUnit = this.units.find(unit => unit.id === this.materialGoods.unitID);
            const currentUnit2 = this.units.find(unit => unit.id === this.materialGoodsConvertUnit[index].unitID);
            if (this.materialGoodsConvertUnit[index].unitID === this.materialGoods.unitID) {
                this.materialGoodsConvertUnit[index].description = '1 ' + currentUnit2.unitName + ' = 1 ' + currentUnit.unitName;
                return;
            }
            if (this.materialGoodsConvertUnit[index].formula === '/') {
                this.materialGoodsConvertUnit[index].description =
                    '1 ' + currentUnit2.unitName + ' = 1/' + this.materialGoodsConvertUnit[index].convertRate + ' ' + currentUnit.unitName;
            } else {
                this.materialGoodsConvertUnit[index].description =
                    '1 ' + currentUnit2.unitName + ' = ' + this.materialGoodsConvertUnit[index].convertRate + ' ' + currentUnit.unitName;
            }
        }
        this.selectChangeFixedSalePrice(index);
        this.selectChangeFixedSalePrice1(index);
        this.selectChangeFixedSalePrice2(index);
        this.selectChangeFixedSalePrice3(index);
    }

    selectChangeMainUnit() {
        for (let i = 0; i < this.materialGoodsConvertUnit.length; i++) {}
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isClose = true;
        this.router.navigate(['/material-goods']);
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    canDeactive(): boolean {
        if (!this.isClose) {
            return this.utilsService.isEquivalent(this.materialGoods, this.materialGoodsCopy);
        }
        return true;
    }

    closeContextMenu() {
        this.contextMenu.isShow = false;
    }

    afterAddRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            const ob: IMaterialGoodsAssembly = Object.assign({});
            ob.id = undefined;
            ob.materialGoods = {};
            this.materialGoodsAssemblys.push(ob);
        });
    }

    afterDeleteRow() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            if (this.contextMenu.checkTag === 4) {
                this.materialGoodsAssemblys.splice(this.currentRow, 1);
            }
        });
    }

    afterAddRow4() {
        this.eventSubscriber = this.eventManager.subscribe('afterAddNewRow', response => {
            if (this.materialGoodsSpecifications.length < 5) {
                const sb: IMaterialGoodsSpecifications = Object.assign({});
                sb.id = undefined;
                sb.materialGoodsSpecificationsCode = (this.materialGoodsSpecifications.length + 1).toString();
                this.materialGoodsSpecifications.push(sb);
            }
        });
    }

    afterDeleteRow4() {
        this.eventSubscriber = this.eventManager.subscribe('afterDeleteRow', response => {
            this.materialGoodsSpecifications.splice(this.currentRow, 1);
            this.deleteRow4();
            this.isAdd = true;
        });
    }

    deleteRow4() {
        for (let i = 0; i < this.materialGoodsSpecifications.length; i++) {
            this.materialGoodsSpecifications[i].materialGoodsSpecificationsCode = (i + 1).toString();
        }
    }

    changeQuantity(index) {
        if (
            (this.materialGoodsAssemblys[index].quantity || this.materialGoodsAssemblys[index].quantity === 0) &&
            (this.materialGoodsAssemblys[index].unitPrice || this.materialGoodsAssemblys[index].unitPrice === 0)
        ) {
            this.materialGoodsAssemblys[index].totalAmount =
                this.materialGoodsAssemblys[index].quantity * this.materialGoodsAssemblys[index].unitPrice;
        }
    }

    selectUnitPriceOriginal(index: number) {
        this.changeQuantity(index);
        this.materialGoodsAssemblys[index].totalAmount =
            this.materialGoodsAssemblys[index].quantity * this.materialGoodsAssemblys[index].unitPrice;
    }

    selectChangeFixedSalePrice(index) {
        if (
            this.materialGoods.fixedSalePrice &&
            this.materialGoodsConvertUnit[index].convertRate &&
            this.materialGoodsConvertUnit[index].formula
        ) {
            if (this.materialGoodsConvertUnit[index].formula === '*') {
                this.materialGoodsConvertUnit[index].fixedSalePrice =
                    this.materialGoods.fixedSalePrice * this.materialGoodsConvertUnit[index].convertRate;
            } else {
                this.materialGoodsConvertUnit[index].fixedSalePrice =
                    this.materialGoods.fixedSalePrice / this.materialGoodsConvertUnit[index].convertRate;
            }
        }
    }

    selectfixedSalePrice() {
        for (let i = 0; i < this.materialGoodsConvertUnit.length; i++) {
            this.selectChangeFixedSalePrice(i);
        }
    }

    selectChangeFixedSalePrice1(index) {
        if (
            this.materialGoods.salePrice1 &&
            this.materialGoodsConvertUnit[index].convertRate &&
            this.materialGoodsConvertUnit[index].formula
        ) {
            if (this.materialGoodsConvertUnit[index].formula === '*') {
                this.materialGoodsConvertUnit[index].salePrice1 =
                    this.materialGoods.salePrice1 * this.materialGoodsConvertUnit[index].convertRate;
            } else {
                this.materialGoodsConvertUnit[index].salePrice1 =
                    this.materialGoods.salePrice1 / this.materialGoodsConvertUnit[index].convertRate;
            }
        }
    }

    selectSalePrice1() {
        for (let i = 0; i < this.materialGoodsConvertUnit.length; i++) {
            this.selectChangeFixedSalePrice1(i);
        }
    }

    selectChangeFixedSalePrice2(index) {
        if (
            this.materialGoods.salePrice2 &&
            this.materialGoodsConvertUnit[index].convertRate &&
            this.materialGoodsConvertUnit[index].formula
        ) {
            if (this.materialGoodsConvertUnit[index].formula === '*') {
                this.materialGoodsConvertUnit[index].salePrice2 =
                    this.materialGoods.salePrice2 * this.materialGoodsConvertUnit[index].convertRate;
            } else {
                this.materialGoodsConvertUnit[index].salePrice2 =
                    this.materialGoods.salePrice2 / this.materialGoodsConvertUnit[index].convertRate;
            }
        }
    }

    selectSalePrice2() {
        for (let i = 0; i < this.materialGoodsConvertUnit.length; i++) {
            this.selectChangeFixedSalePrice2(i);
        }
    }

    selectChangeFixedSalePrice3(index) {
        if (
            this.materialGoods.salePrice3 &&
            this.materialGoodsConvertUnit[index].convertRate &&
            this.materialGoodsConvertUnit[index].formula
        ) {
            if (this.materialGoodsConvertUnit[index].formula === '*') {
                this.materialGoodsConvertUnit[index].salePrice3 =
                    this.materialGoods.salePrice3 * this.materialGoodsConvertUnit[index].convertRate;
            } else {
                this.materialGoodsConvertUnit[index].salePrice3 =
                    this.materialGoods.salePrice3 / this.materialGoodsConvertUnit[index].convertRate;
            }
        }
    }

    selectSalePrice3() {
        for (let i = 0; i < this.materialGoodsConvertUnit.length; i++) {
            this.selectChangeFixedSalePrice3(i);
        }
    }

    changeDiscountRate() {
        if (this.materialGoods.saleDiscountRate > 100 || this.materialGoods.purchaseDiscountRate > 100) {
            this.toasService.warning(
                this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'),
                this.translateService.instant('ebwebApp.mCReceipt.home.message')
            );
        }
    }

    changeUnitId(index) {
        if (this.materialGoods.unitID === null || this.materialGoods.unitID === undefined) {
            this.toasService.warning(
                this.translateService.instant('ebwebApp.materialGoods.unit2'),
                this.translateService.instant('ebwebApp.materialGoods.message')
            );
        }
        if (this.materialGoods.unitID && this.materialGoodsConvertUnit[index].unitID) {
            const currentUnit = this.units.find(x => x.id === this.materialGoods.unitID);
            const currentUnit2 = this.units.find(x => x.id === this.materialGoodsConvertUnit[index].unitID);
            if (currentUnit.unitName === currentUnit2.unitName) {
                this.toasService.warning(
                    this.translateService.instant('ebwebApp.materialGoods.unit'),
                    this.translateService.instant('ebwebApp.materialGoods.message')
                );
            }
            if (
                this.materialGoodsConvertUnit[index].unitID &&
                this.materialGoodsConvertUnit[index].unitID === this.materialGoodsConvertUnit[index - 1].unitID
            ) {
                this.toasService.warning(
                    this.translateService.instant('ebwebApp.materialGoods.unit1'),
                    this.translateService.instant('ebwebApp.materialGoods.message')
                );
            }
        }
    }

    changeDiscountRate1(detail1: ISaleDiscountPolicy) {
        if (detail1.discountType === 0) {
            if (detail1.discountResult > 100) {
                this.toasService.warning(
                    this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'),
                    this.translateService.instant('ebwebApp.mCReceipt.home.message')
                );
            }
        }
    }

    soSanh(index) {
        if (this.saleDiscountPolicy[index].quantityFrom > this.saleDiscountPolicy[index].quantityTo) {
            this.toasService.warning(
                this.translateService.instant('ebwebApp.materialGoods.quantityFromCheck'),
                this.translateService.instant('ebwebApp.materialGoods.message')
            );
        }
    }

    checkError(): boolean {
        this.UnitIdArray = this.materialGoodsConvertUnit.map(n => n.unitID);
        for (let i = 0; i < this.UnitIdArray.length; i++) {
            if ((this.materialGoods.unitID === undefined || this.materialGoods.unitID === null) && this.UnitIdArray[i] !== undefined) {
                this.toasService.error(
                    this.translateService.instant('ebwebApp.materialGoods.unit2'),
                    this.translateService.instant('ebwebApp.materialGoods.message')
                );
                return true;
            }
            if (this.materialGoods.unitID === this.UnitIdArray[i] && this.UnitIdArray[i] !== undefined) {
                this.toasService.error(
                    this.translateService.instant('ebwebApp.materialGoods.unit'),
                    this.translateService.instant('ebwebApp.materialGoods.message')
                );
                return true;
            }
            if (this.UnitIdArray[i] === this.UnitIdArray[i - 1] && this.UnitIdArray[i] !== undefined) {
                this.toasService.error(
                    this.translateService.instant('ebwebApp.materialGoods.unit1'),
                    this.translateService.instant('ebwebApp.materialGoods.message')
                );
                return true;
            }
            if (this.UnitIdArray[i] === undefined) {
                this.toasService.error(
                    this.translateService.instant('ebwebApp.materialGoods.unitNotUndefined'),
                    this.translateService.instant('ebwebApp.materialGoods.message')
                );
                return true;
            }
        }
        return false;
    }

    checkQuantity() {
        this.saleDiscountPolicy = this.saleDiscountPolicy.sort((a, b) => a.quantityFrom - b.quantityFrom);
        for (let i = 1; i < this.saleDiscountPolicy.length; i++) {
            if (this.saleDiscountPolicy[i].quantityFrom <= this.saleDiscountPolicy[i - 1].quantityTo) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.materialGoods.quantityCheck'),
                    this.translate.instant('ebwebApp.materialGoods.message')
                );
                return true;
            }
        }
        return false;
        // this.saleDiscountPolicy.sort((a, b) => a.quantityFrom - b.quantityFrom).forEach(n => {
        //     const index = this.saleDiscountPolicy.indexOf(n);
        //     if (index > 0) {
        //         if (n.quantityFrom < this.saleDiscountPolicy[index - 1].quantityTo) {
        //             this.toastr.error(
        //                 this.translate.instant('ebwebApp.materialGoods.quantityCheck'),
        //                 this.translate.instant('ebwebApp.materialGoods.message')
        //             );
        //             return true;
        //         }
        //         return false;
        //     }
        // });
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.materialGoods;
    }

    addDataToDetail() {
        this.materialGood = this.details ? this.details : this.materialGood;
        this.materialGoods = this.parent ? this.parent : this.materialGoods;
        if (this.materialGoods.repositoryID !== undefined) {
            this.repositoryService.find(this.materialGoods.repositoryID).subscribe(res => {
                this.materialGoods.reponsitoryAccount = res.body.defaultAccount;
            });
        }
    }

    private loadSelectFormula() {
        for (let i = 0; i < this.materialGoodsConvertUnit.length; i++) {
            this.materialGoodsConvertUnit[i].formula = this.materialGoodsConvertUnit[i].formula
                ? this.materialGoodsConvertUnit[i].formula
                : '*';
        }
    }

    ngOnDestroy(): void {
        if (!window.location.href.includes('/material-goods')) {
            sessionStorage.removeItem('pageCurrent');
            sessionStorage.removeItem('selectIndex');
            sessionStorage.removeItem('search');
        }
    }

    addIfLastInput1(i) {
        if (i === this.saleDiscountPolicy.length - 1) {
            this.addNewRow1();
        }
    }

    addIfLastInput(i) {
        if (i === this.materialGoodsAssemblys.length - 1) {
            this.addNewRow();
        }
    }

    addIfLastInput2(i) {
        if (i === this.materialGoodsPurchasePrice.length - 1) {
            this.addNewRow2();
        }
    }

    addIfLastInput3(i) {
        if (i === this.materialGoodsConvertUnit.length - 1) {
            this.addNewRow3();
        }
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }

    check(id, i) {
        const MTG = this.materialGoodss.find(x => x.id === id);
        if (MTG) {
            this.materialGoodsAssemblys[i].materialAssemblyDescription = MTG.materialGoodsName;
        }
    }

    manipulationEnter(e: any, select: any, value: any, nameTag: any, indexInput: any, Objects: any) {
        if (select === Objects.length - 1) {
            const cellCurrent = e.path['0'].closest('td').cellIndex;
            const cellMax = document.getElementsByTagName('tbody')[0].children[select].childElementCount;
            if (cellCurrent === cellMax - 1) {
                this.newRow = true;
            }
        }
        if ((this.isEnter && select === Objects.length - 1) || this.newRow) {
            if (value === 1) {
                Objects.push(new MaterialGoodsAssembly());
            } else if (value === 2) {
                Objects.push(new SaleDiscountPolicy());
            } else if (value === 3) {
                Objects.push(new MaterialGoodsPurchasePrice());
            } else if (value === 4) {
                const materialGoodConvertUnit = new MaterialGoodsConvertUnit();
                materialGoodConvertUnit.orderNumber = select + 2;
                Objects.push(materialGoodConvertUnit);
            }
            this.newRow = false;
            select++;
            nameTag = nameTag + select;
            setTimeout(function() {
                const element: HTMLElement = document.getElementById(nameTag);
                if (element) {
                    element.focus();
                }
            }, 0);
        }
        if (this.isEnter && select < Objects.length - 1) {
            const inputs = document.getElementsByTagName('input');
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

    doubleClickRow(e: any, data: any) {
        if (data.value === 1) {
            const nameTag = 'field_materialGoods';
            const indexInput = 20 + 6 * (data.select + 1) + 1;
            this.manipulationEnter(e, data.select, data.value, nameTag, indexInput, this.materialGoodsAssemblys);
        } else if (data.value === 2) {
            const nameTag = 'quantityFrom';
            const indexInput = 21 + 4 * (data.select + 1) + 1;
            this.manipulationEnter(e, data.select, data.value, nameTag, indexInput, this.saleDiscountPolicy);
        } else if (data.value === 3) {
            const nameTag = 'currencyCode';
            const indexInput = 22 + 3 * (data.select + 1) + 1;
            this.manipulationEnter(e, data.select, data.value, nameTag, indexInput, this.materialGoodsPurchasePrice);
        } else if (data.value === 4) {
            const nameTag = 'unitID3';
            const indexInput = 28 + 9 * (data.select + 1) + 2;
            this.manipulationEnter(e, data.select, data.value, nameTag, indexInput, this.materialGoodsConvertUnit);
        }
    }
}
