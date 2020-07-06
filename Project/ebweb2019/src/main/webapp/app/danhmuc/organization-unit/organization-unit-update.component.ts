import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';

import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from './organization-unit.service';
import { AccountListService } from 'app/danhmuc/account-list';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IOrganizationUnitOptionReport, OrganizationUnitOptionReport } from 'app/shared/model/organization-unit-option-report.model';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { OrganizationUnitOptionReportService } from 'app/entities/organization-unit-option-report';
import { DatePipe } from '@angular/common';
import * as moment from 'moment';
import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UnitTypeByOrganizationUnit } from 'app/app.constants';
import { Principal } from 'app/core';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { EbPackageService } from 'app/admin';
import { EbPackage } from 'app/app.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { ICareerGroup } from 'app/shared/model/career-group.model';
import { CareerGroupService } from 'app/entities/career-group';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'eb-organization-unit-update',
    templateUrl: './organization-unit-update.component.html',
    styleUrls: ['./organization-unit-update.component.css']
})
export class OrganizationUnitUpdateComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content: TemplateRef<any>;
    @ViewChild('deleteForm') deleteForm: TemplateRef<any>;
    @ViewChild('popupMethod') popupMethod: TemplateRef<any>;
    @ViewChild('popupChangeCurrency') popupChangeCurrency: TemplateRef<any>;
    @ViewChild('deleteUser') deleteUser: TemplateRef<any>;
    private _organizationUnit: IOrganizationUnit;
    organizationUnitBackUp: IOrganizationUnit;
    isSaving: boolean;
    issueDateDp: any;
    accounts: IAccountList[];
    organizationUnits: IOrganizationUnit[];
    allOrganizationUnits: IOrganizationUnit[];
    organizationUnitOptionReport: IOrganizationUnitOptionReport = {};
    goodsServicePurchases: IGoodsServicePurchase[];
    careersGroups: ICareerGroup[];
    currencies: ICurrency[];
    accountLists: IAccountList[];
    isSaveAndCreate: boolean;
    organizationUnitCopy: IOrganizationUnit;
    organizationUnitOptionReportCopy: IOrganizationUnitOptionReport;
    modalRef: NgbModalRef;
    listColumnsOrganizationUnit: string[];
    listHeaderColumnsOrganizationUnit: string[];
    currentAccount: any;
    unitType: number;
    isComType: boolean;
    isCreateOrg: boolean;
    eventSubscriber: Subscription;
    comType: number;
    currentUserUnitType: any;
    skipExistUserOrgID: boolean = false;
    unitTypeAfterSave: number;

    ROLE_CoCauToChuc_Xem = ROLE.CoCauToChuc_Xem;
    ROLE_CoCauToChuc_Them = ROLE.CoCauToChuc_Them;
    ROLE_CoCauToChuc_Sua = ROLE.CoCauToChuc_Sua;
    ROLE_CoCauToChuc_Xoa = ROLE.CoCauToChuc_Xoa;
    ROLE_MGT = ROLE.ROLE_MGT;

    buttonDeleteTranslate = 'ebwebApp.mBDeposit.toolTip.delete';
    buttonAddTranslate = 'ebwebApp.mBDeposit.toolTip.add';
    buttonEditTranslate = 'ebwebApp.mBDeposit.toolTip.edit';
    buttonSaveTranslate = 'ebwebApp.mBDeposit.toolTip.save';
    buttonRecordTranslate = 'ebwebApp.mBDeposit.toolTip.record';
    buttonUnRecordTranslate = 'ebwebApp.mBDeposit.toolTip.unrecord';
    buttonPrintTranslate = 'ebwebApp.mBDeposit.toolTip.print';
    buttonSaveAndNewTranslate = 'ebwebApp.mBDeposit.toolTip.saveAndNew';
    buttonCopyAndNewTranslate = 'ebwebApp.mBDeposit.toolTip.copyAndNew';
    buttonCloseFormTranslate = 'ebwebApp.mBDeposit.toolTip.closeForm';
    isAdminWithTotalPackage: boolean;
    arrAuthorities: any[];
    isRoleSua: boolean;
    isRoleThem: boolean;
    isCreateUrl: boolean;
    isEditUrl: boolean;
    mainCurrency: string;
    isUserID: string;
    backupUnitType: number;

    constructor(
        private organizationUnitService: OrganizationUnitService,
        private activatedRoute: ActivatedRoute,
        private accountService: AccountListService,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        private currencyService: CurrencyService,
        private accountListService: AccountListService,
        public utilsService: UtilsService,
        private organizationUnitOptionReportService: OrganizationUnitOptionReportService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private datePipe: DatePipe,
        private router: Router,
        private modalService: NgbModal,
        private principal: Principal,
        private ebPackageService: EbPackageService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        public bankAccountDetailService: BankAccountDetailsService,
        private careerGroupService: CareerGroupService
    ) {
        super();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.currentUserUnitType = account.organizationUnit.unitType;
            this.isUserID = account.organizationUnit.userID;
            this.comType = account.ebPackage.comType;
            this.mainCurrency = this.currentAccount.organizationUnit.currencyID;
            // Set mặc định lúc tạo là hạch toán phụ thuộc
            this.arrAuthorities = account.authorities;
            this.isEditUrl = window.location.href.includes('edit');
            this.isCreateUrl = window.location.href.includes('/new');
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_CoCauToChuc_Sua) : true;
            this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_CoCauToChuc_Them) : true;
            this.isAdminWithTotalPackage = account.ebPackage.isTotalPackage;
        });
    }

    ngOnInit() {
        this.isSaving = false;
        this.unitType = this.currentAccount.organizationUnit.unitType;
        this.listColumnsOrganizationUnit = ['organizationUnitCode', 'organizationUnitName'];
        this.listHeaderColumnsOrganizationUnit = ['Mã tổ chức/đơn vị', 'Tên tổ chức/đơn vị'];
        this.isCreateOrg = window.location.href.includes('/new');
        this.activatedRoute.data.subscribe(({ organizationUnit }) => {
            this.organizationUnit = organizationUnit;
            this.organizationUnitBackUp = Object.assign({}, this.organizationUnit);
            if (this.organizationUnit.id) {
                this.organizationUnitOptionReportService
                    .findByOrganizationUnitID({
                        orgID: this.organizationUnit.id
                    })
                    .subscribe(
                        (res: HttpResponse<IOrganizationUnitOptionReport>) => {
                            this.organizationUnitOptionReport = res.body ? res.body : {};
                            this.backupUnitType = this.organizationUnit.unitType;
                            this.copy();
                        },
                        (res: HttpErrorResponse) => this.onError(res.message)
                    );
            } else {
                this.organizationUnit.accType =
                    this.currentAccount.organizationUnit.accType != null ? this.currentAccount.organizationUnit.accType : 0;
                this.organizationUnit.organizationUnitOptionReport = {};
                if (sessionStorage.getItem('unitTypeAfterSave')) {
                    let unitTypeAfterSave = JSON.parse(sessionStorage.getItem('unitTypeAfterSave'));
                    if (
                        unitTypeAfterSave != null &&
                        unitTypeAfterSave !== undefined &&
                        (unitTypeAfterSave >= this.currentAccount.organizationUnit.unitType + 1 || !this.isUserID)
                    ) {
                        this.organizationUnit.unitType = unitTypeAfterSave;
                    } else {
                        this.organizationUnit.unitType = this.currentAccount.organizationUnit.unitType + 1;
                    }
                    sessionStorage.removeItem('unitTypeAfterSave');
                } else {
                    if (this.unitType === 0 && this.isAdminWithTotalPackage) {
                        this.organizationUnit.unitType = 0;
                    } else if (this.unitTypeAfterSave && this.unitTypeAfterSave >= this.currentAccount.organization.unitType + 1) {
                        this.organizationUnit.unitType = this.unitTypeAfterSave;
                    } else if (this.unitType === 0 && !this.isAdminWithTotalPackage) {
                        if (this.comType === 2) {
                            if (this.currentUserUnitType === 0) {
                                this.organizationUnit.unitType = 1;
                            } else {
                                this.organizationUnit.unitType = 2;
                            }
                        } else if (this.comType === 3) {
                            if (this.isUserID === null && !this.currentAccount.organizationUnit.parentID) {
                                this.organizationUnit.unitType = 0;
                            } else {
                                this.organizationUnit.unitType = this.unitType + 1;
                            }
                        } else if (this.comType === 1) {
                            this.organizationUnit.unitType = 2;
                        } else {
                            this.organizationUnit.unitType = this.unitType + 1;
                        }
                    } else {
                        if (this.comType === 2) {
                            if (this.currentUserUnitType === 0) {
                                this.organizationUnit.unitType = 1;
                            } else {
                                this.organizationUnit.unitType = 2;
                            }
                        } else if (this.comType === 3) {
                            if (this.isUserID === null && !this.currentAccount.organizationUnit.parentID) {
                                this.organizationUnit.unitType = 0;
                            } else {
                                this.organizationUnit.unitType = this.unitType + 1;
                            }
                        } else if (this.comType === 1) {
                            this.organizationUnit.unitType = 2;
                        } else {
                            this.organizationUnit.unitType = this.unitType + 1;
                        }
                    }
                }
                this.organizationUnit.accountingType =
                    this.currentAccount.organizationUnit.accountingType != null ? this.currentAccount.organizationUnit.accountingType : 0;
                this.organizationUnit.taxCalculationMethod =
                    this.currentAccount.organizationUnit.taxCalculationMethod != null
                        ? this.currentAccount.organizationUnit.taxCalculationMethod
                        : 0;
                if (
                    this.organizationUnit.unitType === 0 &&
                    this.goodsServicePurchases &&
                    this.goodsServicePurchases.length > 0 &&
                    this.careersGroups &&
                    this.careersGroups.length > 0
                ) {
                    if (this.organizationUnit.taxCalculationMethod === 0) {
                        this.organizationUnit.goodsServicePurchaseID = this.goodsServicePurchases[0].id;
                    } else {
                        this.organizationUnit.careerGroupID = this.careersGroups[0].id;
                    }
                }
                this.organizationUnit.financialYear =
                    this.currentAccount.organizationUnit.financialYear != null
                        ? this.currentAccount.organizationUnit.financialYear
                        : new Date().getFullYear();
                this.organizationUnit.fromDate =
                    this.currentAccount.organizationUnit.fromDate != null
                        ? moment(this.currentAccount.organizationUnit.fromDate)
                        : moment('01/01/' + this.organizationUnit.financialYear.toString());
                this.organizationUnit.toDate =
                    this.currentAccount.organizationUnit.toDate != null
                        ? moment(this.currentAccount.organizationUnit.toDate)
                        : moment('12/31/' + this.organizationUnit.financialYear.toString());
                this.organizationUnit.startDate =
                    this.currentAccount.organizationUnit.startDate != null
                        ? moment(this.currentAccount.organizationUnit.startDate)
                        : this.organizationUnit.fromDate;
                this.organizationUnit.currencyID = this.mainCurrency != null ? this.mainCurrency : 'VND';
                console.log(this.mainCurrency);
                this.organizationUnitOptionReport = {};
                this.organizationUnitOptionReport.isDisplayAccount = true;
                this.organizationUnitOptionReport.isDisplayNameInReport = true;
                this.organizationUnitOptionReport.headerSetting = 0;
                this.copy();
            }
            if (this.comType === 3) {
                this.isComType = false;
            } else if (this.comType === 2) {
                if (this.organizationUnit.unitType > 0) {
                    this.isComType = false;
                } else {
                    this.isComType = true;
                }
            } else {
                this.isComType = true;
            }
            // this.organizationUnit.fromDate = moment(this.datePipe.transform(this.organizationUnit.fromDate,'yyyy-MM-dd'));
            // this.organizationUnit.toDate = moment(this.datePipe.transform(this.organizationUnit.toDate,'yyyy-MM-dd'));
            // this.organizationUnit.startDate = moment(this.datePipe.transform(this.organizationUnit.startDate,'yyyy-MM-dd'));
        });
        // cbb tài khoản
        this.accountService.query().subscribe(
            (res: HttpResponse<IAccountList[]>) => {
                this.accounts = res.body;
            }
            // (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.bankAccountDetailService.getBankAccountDetails().subscribe((res: HttpResponse<IBankAccountDetails[]>) => {
            this.bankAccountDetails = res.body;
        });
        this.careerGroupService.getCareerGroups().subscribe((res: HttpResponse<ICareerGroup[]>) => {
            this.careersGroups = res.body;
        });
        // cbb thuộc đơn vị
        if (this.isAdminWithTotalPackage) {
            this.organizationUnitService.getAllOrganizationUnitsIsCompany().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
                this.organizationUnits = res.body;
                this.allOrganizationUnits = res.body;
            });
            // && this.organizationUnit.isParentNode
            if (this.organizationUnit && this.organizationUnit.id) {
                this.organizationUnitService
                    .getOrganizationUnitsActiveExceptID({ id: this.organizationUnit.id })
                    .subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
                        this.allOrganizationUnits = res.body;
                        if (this.organizationUnit.unitType) {
                            this.organizationUnits = this.allOrganizationUnits.filter(
                                a => a.unitType < this.organizationUnit.unitType && a.unitType >= this.currentUserUnitType
                            );
                        }
                    });
            } else {
                this.organizationUnitService.recursiveOrganizationUnitByParentID().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
                    this.allOrganizationUnits = res.body;
                    this.organizationUnits = this.allOrganizationUnits.filter(
                        a =>
                            a.unitType < this.organizationUnit.unitType &&
                            (a.unitType === 0 ||
                                (a.unitType === 1 &&
                                    a.unitType >= this.currentUserUnitType &&
                                    a.id === this.currentAccount.organizationUnit.id))
                    );
                });
            }
        } else {
            this.organizationUnitService.recursiveOrganizationUnitByParentID().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
                this.allOrganizationUnits = res.body;
                if (this.comType === 3 && !this.isUserID && this.currentUserUnitType === 0) {
                    if (this.organizationUnit.unitType === 0) {
                        this.organizationUnits = this.allOrganizationUnits.filter(a => !a.userID && a.unitType === 0);
                    } else if (
                        this.comType === 3 &&
                        ((this.isUserID && this.currentUserUnitType === 0) || (!this.isUserID && this.currentUserUnitType === 2))
                    ) {
                        this.organizationUnits = this.allOrganizationUnits.filter(
                            a =>
                                a.unitType < this.organizationUnit.unitType &&
                                (a.id === this.currentAccount.organizationUnit.id || a.parentID === this.currentAccount.organizationUnit.id)
                        );
                    } else {
                        this.organizationUnits = this.allOrganizationUnits.filter(a => a.unitType < this.organizationUnit.unitType);
                    }
                } else {
                    this.organizationUnits = this.allOrganizationUnits.filter(
                        a =>
                            a.unitType < this.organizationUnit.unitType &&
                            (a.unitType > this.currentUserUnitType ||
                                (a.unitType === this.currentUserUnitType && a.id === this.currentAccount.organizationUnit.id))
                    );
                }
            });
        }
        this.goodsServicePurchaseService.getGoodServicePurchases().subscribe((res: HttpResponse<IGoodsServicePurchase[]>) => {
            this.goodsServicePurchases = res.body;
            if (!this.organizationUnit.id && this.organizationUnit.taxCalculationMethod === 0) {
                this.organizationUnit.goodsServicePurchaseID = this.goodsServicePurchases[0].id;
            }
        });
        this.currencyService.findAllByCompanyIDIsNull().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencies = res.body;
        });
        this.accountListService.getAccountForOrganizationUnit().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountLists = res.body;
        });
        this.copy();
        this.eventSubscriber = this.eventManager.subscribe('saveSuccess', response => {
            this.isCbbSaveAndNew = false;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('saveAndNewSuccess', response => {
            this.isCbbSaveAndNew = true;
            this.registerComboboxSave(response);
            this.utilsService.setShowPopup(false);
        });
        this.eventSubscriber = this.eventManager.subscribe('closePopup', response => {
            this.utilsService.setShowPopup(response.content);
        });
    }

    private onError(errorMessage: string) {
        this.toastr.error(this.translate.instant('ebwebApp.mBCreditCard.error'), this.translate.instant('ebwebApp.mBCreditCard.message'));
    }

    closeForm() {
        if (this.organizationUnitCopy && this.organizationUnitOptionReportCopy) {
            if (
                !this.utilsService.isEquivalent(this.organizationUnit, this.organizationUnitCopy) ||
                !this.utilsService.isEquivalent(this.organizationUnitOptionReport, this.organizationUnitOptionReportCopy)
            ) {
                if (this.modalRef) {
                    this.modalRef.close();
                }
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
        this.router.navigate(['/organization-unit']);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ROLE_MGT, ROLE.CoCauToChuc_Them, ROLE.CoCauToChuc_Sua])
    save() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = false;
        if (this.checkError()) {
            this.organizationUnit.organizationUnitOptionReport = this.organizationUnitOptionReport;
            if (this.organizationUnit.id !== undefined) {
                this.subscribeToSaveResponse(this.organizationUnitService.update(this.organizationUnit));
            } else {
                this.organizationUnit.isActive = true;
                this.subscribeToSaveResponse(this.organizationUnitService.create(this.organizationUnit));
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ROLE_MGT, ROLE.CoCauToChuc_Them])
    saveAndNew() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = true;
        if (this.checkError()) {
            this.organizationUnit.organizationUnitOptionReport = this.organizationUnitOptionReport;
            if (this.organizationUnit.id !== undefined) {
                this.subscribeToSaveResponse(this.organizationUnitService.update(this.organizationUnit));
            } else {
                this.organizationUnit.isActive = true;
                this.subscribeToSaveResponse(this.organizationUnitService.create(this.organizationUnit));
            }
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    if (this.modalRef) {
                        this.modalRef.close();
                    }
                    if (this.organizationUnit.unitType === 0) {
                        this.principal.identity(true).then(account => {
                            this.currentAccount = account;
                        });
                    }
                    this.onSaveSuccess();
                } else {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.systemOption.error'),
                        this.translate.instant('ebwebApp.systemOption.message')
                    );
                }
            },
            (res: HttpErrorResponse) => {
                this.onSaveError(res);
            }
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.toastr.success(
            this.translate.instant('ebwebApp.organizationUnit.saveSuccess'),
            this.translate.instant('ebwebApp.organizationUnit.message')
        );
        if (!this.isSaveAndCreate) {
            this.copy();
            this.router.navigate(['/organization-unit']);
        } else {
            sessionStorage.setItem('unitTypeAfterSave', JSON.stringify(this.organizationUnit.unitType));
            const unitType = this.organizationUnit.unitType;
            this.organizationUnit = {};
            this.router.navigate(['/organization-unit/new']);
            this.organizationUnit.unitType = unitType;
            this.organizationUnit.accountingType =
                this.currentAccount.organizationUnit.accountingType != null ? this.currentAccount.organizationUnit.accountingType : 0;
            this.organizationUnit.taxCalculationMethod =
                this.currentAccount.organizationUnit.taxCalculationMethod != null
                    ? this.currentAccount.organizationUnit.taxCalculationMethod
                    : 0;
            if (
                this.organizationUnit.unitType === 0 &&
                this.goodsServicePurchases &&
                this.goodsServicePurchases.length > 0 &&
                this.careersGroups &&
                this.careersGroups.length > 0
            ) {
                if (this.organizationUnit.taxCalculationMethod === 0) {
                    this.organizationUnit.goodsServicePurchaseID = this.goodsServicePurchases[0].id;
                } else {
                    this.organizationUnit.careerGroupID = this.careersGroups[0].id;
                }
            }
            this.organizationUnit.financialYear =
                this.currentAccount.organizationUnit.financialYear != null
                    ? this.currentAccount.organizationUnit.financialYear
                    : new Date().getFullYear();
            this.organizationUnit.fromDate =
                this.currentAccount.organizationUnit.fromDate != null
                    ? moment(this.currentAccount.organizationUnit.fromDate)
                    : moment('01/01/' + this.organizationUnit.financialYear.toString());
            this.organizationUnit.toDate =
                this.currentAccount.organizationUnit.toDate != null
                    ? moment(this.currentAccount.organizationUnit.toDate)
                    : moment('12/31/' + this.organizationUnit.financialYear.toString());
            this.organizationUnit.startDate =
                this.currentAccount.organizationUnit.startDate != null
                    ? moment(this.currentAccount.organizationUnit.startDate)
                    : this.organizationUnit.fromDate;
            this.organizationUnit.currencyID = this.mainCurrency != null ? this.mainCurrency : 'VND';
            this.organizationUnitOptionReport = {};
            this.organizationUnitOptionReport.isDisplayAccount = true;
            this.organizationUnitOptionReport.isDisplayNameInReport = true;
            this.organizationUnitOptionReport.headerSetting = 0;
            this.organizationUnit.unitType = unitType;
            this.copy();
        }
    }

    private onSaveError(err) {
        // this.toastr.error(
        //     this.translate.instant('ebwebApp.organizationUnit.error'),
        //     this.translate.instant('ebwebApp.organizationUnit.message')
        // );
        this.isSaving = false;
        if (err && err.error && err.error.errorKey === 'existOPAccount') {
            this.toastr.error(this.translate.instant('ebwebApp.organizationUnit.existOPAccount'));
        } else if (err && err.error && err.error.errorKey === 'existOrganizationUnit') {
            this.toastr.error(this.translate.instant('ebwebApp.organizationUnit.existOrganizationUnit'));
        } else if (err && err.error) {
            this.toastr.error(this.translate.instant(`ebwebApp.ebPackage.${err.error.message}`));
        }
    }

    get organizationUnit() {
        return this._organizationUnit;
    }

    set organizationUnit(organizationUnit: IOrganizationUnit) {
        this._organizationUnit = organizationUnit;
    }

    selectChangeUnitType() {
        if (
            !this.organizationUnit.id ||
            (this.organizationUnit.id &&
                ((this.backupUnitType <= 1 && this.organizationUnit.unitType >= 1) ||
                    (this.backupUnitType >= 1 && this.organizationUnit.unitType <= 1)))
        ) {
            const unitType = this.organizationUnit.unitType;
            let organizationUnitID = '';
            if (this.organizationUnit.id) {
                organizationUnitID = this.organizationUnit.id;
            }
            this.organizationUnit = {};
            this.organizationUnit.unitType = unitType;
            if (organizationUnitID) {
                this.organizationUnit.id = organizationUnitID;
            }
            this.organizationUnit.accType = 1;
            this.organizationUnit.organizationUnitOptionReport = {};

            this.organizationUnit.accountingType = 0;
            this.organizationUnit.taxCalculationMethod = 0;
            if (
                this.organizationUnit.unitType === 0 &&
                this.goodsServicePurchases &&
                this.goodsServicePurchases.length > 0 &&
                this.careersGroups &&
                this.careersGroups.length > 0
            ) {
                if (this.organizationUnit.taxCalculationMethod === 0) {
                    this.organizationUnit.goodsServicePurchaseID = this.goodsServicePurchases[0].id;
                } else {
                    this.organizationUnit.careerGroupID = this.careersGroups[0].id;
                }
            }
            this.organizationUnit.accType = 0;
            this.organizationUnit.financialYear = new Date().getFullYear();
            this.organizationUnit.fromDate = moment('01/01/' + this.organizationUnit.financialYear.toString());
            this.organizationUnit.toDate = moment('12/31/' + this.organizationUnit.financialYear.toString());
            this.organizationUnit.startDate = this.organizationUnit.fromDate;
            this.organizationUnit.isActive = true;
            let organizationUnitOptionReportID = '';
            if (this.organizationUnitOptionReport.id) {
                organizationUnitOptionReportID = this.organizationUnitOptionReport.id;
            }
            this.organizationUnitOptionReport = {};
            this.organizationUnitOptionReport.isDisplayAccount = true;
            this.organizationUnitOptionReport.isDisplayNameInReport = true;
            this.organizationUnitOptionReport.headerSetting = 0;
            if (organizationUnitOptionReportID) {
                this.organizationUnitOptionReport.id = organizationUnitOptionReportID;
            }
            if (this.organizationUnit.unitType != null && this.organizationUnits && this.allOrganizationUnits) {
                if (this.comType === 3 && !this.isUserID && this.currentUserUnitType === 0) {
                    if (unitType === 0) {
                        this.organizationUnits = this.allOrganizationUnits.filter(a => !a.userID && a.unitType === 0);
                    } else {
                        this.organizationUnits = this.allOrganizationUnits.filter(a => a.unitType < this.organizationUnit.unitType);
                    }
                } else if (
                    this.comType === 3 &&
                    ((this.isUserID && this.currentUserUnitType === 0) || (!this.isUserID && this.currentUserUnitType === 1))
                ) {
                    this.organizationUnits = this.allOrganizationUnits.filter(
                        a =>
                            a.unitType < this.organizationUnit.unitType &&
                            (a.id === this.currentAccount.organizationUnit.id || a.parentID === this.currentAccount.organizationUnit.id)
                    );
                } else {
                    this.organizationUnits = this.allOrganizationUnits.filter(
                        a =>
                            a.unitType < this.organizationUnit.unitType &&
                            (a.unitType > this.currentUserUnitType ||
                                (a.unitType === this.currentUserUnitType && a.id === this.currentAccount.organizationUnit.id))
                    );
                }
            }
        }
        this.backupUnitType = this.organizationUnit.unitType;
    }

    checkError(): boolean {
        if (!this.organizationUnit.organizationUnitCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.organizationUnit.organizationUnitCodeIsNotBlank'),
                this.translate.instant('ebwebApp.organizationUnit.error')
            );
            return false;
        }
        if (!this.organizationUnit.organizationUnitName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.organizationUnit.organizationUnitNameIsNotBlank'),
                this.translate.instant('ebwebApp.organizationUnit.error')
            );
            return false;
        }
        if (this.organizationUnit.taxCode) {
            if (!this.utilsService.checkMST(this.organizationUnit.taxCode)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.mBTellerPaper.error.taxCodeInvalid'),
                    this.translate.instant('ebwebApp.mBTellerPaper.error.error')
                );
                return false;
            }
        }
        if (
            this.organizationUnit.unitType !== 0 ||
            (this.comType === 3 && !this.isUserID && this.organizationUnit.unitType === 0 && !this.organizationUnit.id)
        ) {
            if (!this.organizationUnit.parentID) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.organizationUnit.parentIDIsNotBlank'),
                    this.translate.instant('ebwebApp.organizationUnit.error')
                );
                return false;
            }
        }
        if (this.organizationUnitOptionReport.governingUnitTaxCode) {
            if (!this.utilsService.checkMST(this.organizationUnitOptionReport.governingUnitTaxCode)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.organizationUnit.governingUnitTaxCodeInvalid'),
                    this.translate.instant('ebwebApp.organizationUnit.error')
                );
                return false;
            }
        }
        if (this.organizationUnitOptionReport.taxAgentTaxCode) {
            if (!this.utilsService.checkMST(this.organizationUnitOptionReport.taxAgentTaxCode)) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.organizationUnit.taxAgentTaxCodeInvalid'),
                    this.translate.instant('ebwebApp.organizationUnit.error')
                );
                return false;
            }
        }
        if (
            this.organizationUnitOptionReport.email &&
            !/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(this.organizationUnitOptionReport.email)
        ) {
            this.toastr.error(this.translate.instant('ebwebApp.organizationUnit.emailInvalid'));
            return false;
        }
        if (
            this.organizationUnit.id &&
            this.organizationUnit.unitType === 0 &&
            this.organizationUnit.taxCalculationMethod !== this.organizationUnitBackUp.taxCalculationMethod
        ) {
            this.modalRef = this.modalService.open(this.popupMethod, { backdrop: 'static' });
            return false;
        }
        if (
            this.organizationUnit.id &&
            this.organizationUnit.unitType === 0 &&
            this.organizationUnit.currencyID !== this.organizationUnitBackUp.currencyID
        ) {
            this.modalRef = this.modalService.open(this.popupChangeCurrency, { backdrop: 'static' });
            return false;
        }
        return true;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.ROLE_MGT, ROLE.CoCauToChuc_Xoa])
    delete() {
        event.preventDefault();
        if (this.organizationUnit.id !== this.currentAccount.organizationUnit.id) {
            if (this.organizationUnit.id && this.organizationUnit.unitType !== 0) {
                this.modalRef = this.modalService.open(this.deleteForm, { backdrop: 'static' });
            } else {
                this.toastr.warning(this.translate.instant('ebwebApp.organizationUnit.warningDeleteCoporation'));
            }
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.organizationUnit.errorDeleteOrgLogin'));
        }
    }

    /*
    * hàm ss du lieu 2 object và 2 mảng object
    * return true: neu tat ca giong nhau
    * return false: neu 1 trong cac object ko giong nhau
    * */
    canDeactive(): boolean {
        return (
            this.utilsService.isEquivalent(this.organizationUnit, this.organizationUnitCopy) &&
            this.utilsService.isEquivalent(this.organizationUnitOptionReport, this.organizationUnitOptionReportCopy)
        );
    }

    copy() {
        this.organizationUnitCopy = Object.assign({}, this.organizationUnit);
        this.organizationUnitOptionReportCopy = Object.assign({}, this.organizationUnitOptionReport);
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    saveContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isSaving = true;
        this.isSaveAndCreate = false;
        if (this.checkError()) {
            this.organizationUnit.organizationUnitOptionReport = this.organizationUnitOptionReport;
            if (this.organizationUnit.id !== undefined) {
                this.subscribeToSaveResponse(this.organizationUnitService.update(this.organizationUnit));
            } else {
                this.organizationUnit.isActive = true;
                this.subscribeToSaveResponse(this.organizationUnitService.create(this.organizationUnit));
            }
        }
    }

    exit() {
        this.skipExistUserOrgID = false;
        if (this.modalRef) {
            this.modalRef.close();
            return;
        }
    }

    close() {
        this.modalRef.close();
        this.copy();
        this.router.navigate(['/organization-unit']);
    }

    clear() {
        if (this.modalRef) {
            this.modalRef.close();
            return;
        }
    }

    continueDelete() {
        this.skipExistUserOrgID = true;
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.confirmDelete(this.organizationUnit.id);
    }

    confirmDelete(id: string) {
        const obj = {
            orgID: this.organizationUnit.id,
            organizationUnit: this.organizationUnit,
            skipExistUserOrgID: this.skipExistUserOrgID
        };
        this.organizationUnitService
            .deleteOrganizationUnitByCompanyID({
                org: JSON.stringify(obj)
            })
            .subscribe(
                (res: HttpResponse<IOrganizationUnit>) => {
                    this.toastr.success(
                        this.translate.instant('ebwebApp.organizationUnit.deleteSuccess'),
                        this.translate.instant('ebwebApp.organizationUnit.message')
                    );
                    this.modalRef.close();
                    this.copy();
                    window.history.back();
                    // this.eventManager.broadcast({
                    //     name: 'organizationUnitListModification',
                    //     content: 'Deleted an organizationUnit'
                    // });
                },
                (res: HttpErrorResponse) => {
                    if (res.error) {
                        if (res.error.errorKey === 'existUserUseOrg') {
                            if (this.modalRef) {
                                this.modalRef.close();
                            }
                            this.modalRef = this.modalService.open(this.deleteUser, { backdrop: 'static' });
                        } else if (res.error.errorKey === 'errorDeleteParent') {
                            this.toastr.error(
                                this.translate.instant('ebwebApp.accountList.errorDeleteParent'),
                                this.translate.instant('ebwebApp.accountList.error')
                            );
                        } else {
                            this.toastr.error(
                                this.translate.instant('ebwebApp.organizationUnit.deleteFail'),
                                this.translate.instant('ebwebApp.organizationUnit.message')
                            );
                            // this.eventManager.broadcast({
                            //     name: 'organizationUnitListModification',
                            //     content: 'Deleted an organizationUnit'
                            // });
                        }
                    }
                }
            );
    }

    changeTaxCalculationMethod() {
        if (this.organizationUnit.taxCalculationMethod === 0) {
            this.organizationUnit.careerGroupID = null;
            this.organizationUnit.goodsServicePurchaseID = this.goodsServicePurchases[0].id;
        } else {
            this.organizationUnit.goodsServicePurchaseID = null;
            this.organizationUnit.careerGroupID = this.careersGroups[0].id;
        }
    }

    continueSave() {
        this.organizationUnit.organizationUnitOptionReport = this.organizationUnitOptionReport;
        if (this.organizationUnit.id !== undefined) {
            this.subscribeToSaveResponse(this.organizationUnitService.update(this.organizationUnit));
        } else {
            this.organizationUnit.isActive = true;
            this.subscribeToSaveResponse(this.organizationUnitService.create(this.organizationUnit));
        }
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.organizationUnitOptionReport;
    }
}
