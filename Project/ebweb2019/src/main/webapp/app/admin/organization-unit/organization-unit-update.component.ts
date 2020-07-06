import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitAdminService } from './organization-unit.service';
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
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UnitTypeByOrganizationUnit } from 'app/app.constants';
import { IUser, Principal, UserService } from 'app/core';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IEbPackage } from 'app/shared/model/eb-package.model';
import { EbPackageService } from 'app/admin/eb-package/eb-package.service';
import { OrganizationUnitDeleteDialogAdminComponent } from 'app/admin';

@Component({
    selector: 'eb-organization-unit-update-admin',
    templateUrl: './organization-unit-update.component.html',
    styleUrls: ['./organization-unit-update.component.css']
})
export class OrganizationUnitUpdateAdminComponent extends BaseComponent implements OnInit, OnDestroy {
    @ViewChild('content') content: TemplateRef<any>;
    private _organizationUnit: IOrganizationUnit;
    isSaving: boolean;
    issueDateDp: any;
    accounts: IAccountList[];
    organizationUnits: IOrganizationUnit[];
    organizationUnitOptionReport: IOrganizationUnitOptionReport = {};
    goodsServicePurchases: IGoodsServicePurchase[];
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
    data: IUser[];
    listColumnsUser: any;
    listHeaderColumnsUser: any;
    listEbPackage: IEbPackage[];
    listColumnsEbPackage: any;
    listHeaderColumnsEbPackage: any;
    listStatus = [{ id: 0, name: 'Chưa dùng' }, { id: 1, name: 'Đang dùng' }, { id: 2, name: 'Hủy' }, { id: 3, name: 'Xóa' }];
    private isCheckCandeactive: any;

    constructor(
        private organizationUnitService: OrganizationUnitAdminService,
        private activatedRoute: ActivatedRoute,
        private accountService: AccountListService,
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        private currencyService: CurrencyService,
        private ebPackageService: EbPackageService,
        private userService: UserService,
        private accountListService: AccountListService,
        public utilsService: UtilsService,
        private organizationUnitOptionReportService: OrganizationUnitOptionReportService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private datePipe: DatePipe,
        private router: Router,
        private modalService: NgbModal,
        private principal: Principal
    ) {
        super();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
    }

    ngOnInit() {
        this.isSaving = false;
        this.isCheckCandeactive = true;
        // this.unitType = this.currentAccount.organizationUnit.unitType;
        this.listColumnsOrganizationUnit = ['organizationUnitCode', 'organizationUnitName'];
        this.listHeaderColumnsOrganizationUnit = ['Mã tổ chức/đơn vị', 'Tên tổ chức/đơn vị'];
        this.activatedRoute.data.subscribe(({ organizationUnit }) => {
            this.organizationUnit = organizationUnit;
            if (this.organizationUnit.id) {
                this.organizationUnitOptionReportService
                    .findByOrganizationUnitID({
                        orgID: this.organizationUnit.id
                    })
                    .subscribe(
                        (res: HttpResponse<IOrganizationUnitOptionReport>) => {
                            this.organizationUnitOptionReport = res.body ? res.body : {};
                        },
                        (res: HttpErrorResponse) => this.onError(res.message)
                    );
            } else {
                this.organizationUnitOptionReport = {};
                this.organizationUnitOptionReport.isDisplayAccount = true;
                this.organizationUnitOptionReport.isDisplayNameInReport = true;
                this.organizationUnitOptionReport.headerSetting = 0;
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
        // cbb thuộc đơn vị
        if (this.organizationUnit.id && this.organizationUnit.isParentNode) {
            this.organizationUnitService
                .getOrganizationUnitsActiveExceptID({ id: this.organizationUnit.id })
                .subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
                    this.organizationUnits = res.body;
                });
        } else {
            this.organizationUnitService.getAllOrganizationUnitByParentID().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
                this.organizationUnits = res.body;
            });
        }

        // Set mặc định lúc tạo là hạch toán phụ thuộc
        if (this.organizationUnit.id === undefined) {
            this.organizationUnit.accType = 1;
            this.organizationUnit.organizationUnitOptionReport = {};
            this.organizationUnit.unitType = this.organizationUnit.unitType ? this.organizationUnit.unitType : 0;
            this.organizationUnit.accountingType = 0;
            this.organizationUnit.accType = 0;
            this.organizationUnit.taxCalculationMethod = 0;
            this.organizationUnit.financialYear = new Date().getFullYear();
            this.organizationUnit.fromDate = moment('01/01/' + this.organizationUnit.financialYear.toString());
            this.organizationUnit.toDate = moment('12/31/' + this.organizationUnit.financialYear.toString());
            this.organizationUnit.startDate = this.organizationUnit.fromDate;
            this.copy();
        }
        this.goodsServicePurchaseService.getGoodServicePurchases().subscribe((res: HttpResponse<IGoodsServicePurchase[]>) => {
            this.goodsServicePurchases = res.body;
        });
        this.currencyService.findAllByCompanyIDIsNull().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencies = res.body;
            if (this.organizationUnit.id === undefined) {
                this.organizationUnit.currencyID = 'VND';
            }
            this.copy();
        });
        this.accountListService.getAccountForOrganizationUnit().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountLists = res.body;
        });
        // this.listColumnsEbPackage = ['packageCode', 'packageName'];
        // this.listHeaderColumnsEbPackage = ['Mã gói', 'Tên gói'];
        // this.ebPackageService.queryList().subscribe(res => {
        //     this.listEbPackage = res.body;
        // });
        // this.listColumnsUser = ['login', 'fullName'];
        // this.listHeaderColumnsUser = ['username', 'fullname'];
        // this.userService.queryListUser(this.organizationUnit.id).subscribe(res => {
        //     this.data = res.body;
        // });
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
        this.router.navigate(['/admin/organization-unit']);
    }

    save() {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isSaving = true;
        this.isSaveAndCreate = false;
        if (this.checkError()) {
            this.organizationUnit.organizationUnitOptionReport = this.organizationUnitOptionReport;
            if (this.organizationUnit.id !== undefined) {
                this.subscribeToSaveResponse(this.organizationUnitService.updateOrg(this.organizationUnit));
            } else {
                this.organizationUnit.isActive = true;
                this.subscribeToSaveResponse(this.organizationUnitService.createBigOrg(this.organizationUnit));
            }
        }
    }

    saveAndNew() {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isSaving = true;
        this.isSaveAndCreate = true;
        if (this.checkError()) {
            this.organizationUnit.organizationUnitOptionReport = this.organizationUnitOptionReport;
            if (this.organizationUnit.id !== undefined) {
                this.subscribeToSaveResponse(this.organizationUnitService.updateOrg(this.organizationUnit));
            } else {
                this.organizationUnit.isActive = true;
                this.subscribeToSaveResponse(this.organizationUnitService.createBigOrg(this.organizationUnit));
            }
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                } else {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.organizationUnit.organizationUnitCodeExist'),
                        this.translate.instant('ebwebApp.organizationUnit.error')
                    );
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
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
            this.router.navigate(['/admin/organization-unit']);
        } else {
            this.isCheckCandeactive = false;
            this.router.navigate(['/admin/organization-unit/new']);
            this.organizationUnit = {};
            this.organizationUnit.accType = 1;
            this.organizationUnit.organizationUnitOptionReport = {};
            this.organizationUnitOptionReport = {};
            this.organizationUnitOptionReport.isDisplayAccount = true;
            this.organizationUnitOptionReport.isDisplayNameInReport = true;
            this.organizationUnitOptionReport.headerSetting = 0;
            this.organizationUnit.accountingType = 0;
            this.organizationUnit.taxCalculationMethod = 0;
            this.organizationUnit.accType = 0;
            this.organizationUnit.unitType = 0;
            this.organizationUnit.financialYear = new Date().getFullYear();
            this.organizationUnit.fromDate = moment('01/01/' + this.organizationUnit.financialYear.toString());
            this.organizationUnit.toDate = moment('12/31/' + this.organizationUnit.financialYear.toString());
            this.organizationUnit.startDate = this.organizationUnit.fromDate;
            this.organizationUnit.isActive = true;
            // this.copy();
        }
    }

    private onSaveError() {
        this.isSaving = false;
        this.toastr.error(this.translate.instant('ebwebApp.organizationUnit.organizationUnitCodeExist'));
    }

    get organizationUnit() {
        return this._organizationUnit;
    }

    set organizationUnit(organizationUnit: IOrganizationUnit) {
        this._organizationUnit = organizationUnit;
    }

    selectChangeUnitType() {
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
        if (organizationUnitOptionReportID) {
            this.organizationUnitOptionReport.id = organizationUnitOptionReportID;
        }
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
        if (this.organizationUnit.unitType !== 0) {
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
        /* if (!this.organizationUnit.packageID) {
            this.toastr.error(
                this.translate.instant('ebwebApp.organizationUnit.packageRequired'),
                this.translate.instant('ebwebApp.organizationUnit.error')
            );
            return false;
        }
        if (this.organizationUnit.status == null) {
            this.toastr.error(
                this.translate.instant('ebwebApp.organizationUnit.statusRequired'),
                this.translate.instant('ebwebApp.organizationUnit.error')
            );
            return false;
        }
        if (!this.organizationUnit.userID) {
            this.toastr.error(
                this.translate.instant('ebwebApp.organizationUnit.userRequired'),
                this.translate.instant('ebwebApp.organizationUnit.error')
            );
            return false;
        }*/
        return true;
    }

    delete() {
        event.preventDefault();
        const modalRef = this.modalService.open(OrganizationUnitDeleteDialogAdminComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.organizationUnit = this.organizationUnit;
        modalRef.result.then(
            result => {
                // Left blank intentionally, nothing to do here
            },
            reason => {
                // Left blank intentionally, nothing to do here
            }
        );
    }

    /*
    * hàm ss du lieu 2 object và 2 mảng object
    * return true: neu tat ca giong nhau
    * return false: neu 1 trong cac object ko giong nhau
    * */
    canDeactive(): boolean {
        if (this.isCheckCandeactive) {
            return (
                this.utilsService.isEquivalent(this.organizationUnit, this.organizationUnitCopy) &&
                this.utilsService.isEquivalent(this.organizationUnitOptionReport, this.organizationUnitOptionReportCopy)
            );
        }
        return true;
    }

    copy() {
        this.organizationUnitCopy = Object.assign({}, this.organizationUnit);
        this.organizationUnitOptionReportCopy = Object.assign({}, this.organizationUnitOptionReport);
    }

    copyObject() {
        this.organizationUnitCopy = this.utilsService.deepCopy(this.organizationUnit);
        this.organizationUnitOptionReportCopy = this.utilsService.deepCopy(this.organizationUnitOptionReport);
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

    ngOnDestroy(): void {
        if (!window.location.href.includes('/admin/organization-unit')) {
            sessionStorage.removeItem('pageCurrent');
            sessionStorage.removeItem('selectIndex');
            sessionStorage.removeItem('organizationUnitSearch');
        }
    }
}
