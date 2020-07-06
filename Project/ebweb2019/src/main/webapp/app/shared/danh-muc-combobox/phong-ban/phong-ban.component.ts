import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { IAccountList } from 'app/shared/model/account-list.model';
import { IOrganizationUnitOptionReport } from 'app/shared/model/organization-unit-option-report.model';
import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from 'app/entities/goods-service-purchase';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/danhmuc/currency';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { OrganizationUnitOptionReportService } from 'app/entities/organization-unit-option-report';
import { DatePipe } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Principal } from 'app/core';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { EbPackageService } from 'app/admin';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { JhiEventManager } from 'ng-jhipster';
import { CategoryName } from 'app/app.constants';

@Component({
    selector: 'eb-phong-ban-combobox',
    templateUrl: './phong-ban.component.html',
    styleUrls: ['./phong-ban.component.css']
})
export class EbPhongBanComboboxComponent extends BaseComponent implements OnInit {
    private _organizationUnit: IOrganizationUnit;
    isSaving: boolean;
    accounts: IAccountList[];
    organizationUnits: IOrganizationUnit[];
    organizationUnitOptionReport: IOrganizationUnitOptionReport = {};
    goodsServicePurchases: IGoodsServicePurchase[];
    currencies: ICurrency[];
    accountLists: IAccountList[];
    isSaveAndCreate: boolean;
    modalRef: NgbModalRef;
    listColumnsOrganizationUnit: string[];
    listHeaderColumnsOrganizationUnit: string[];
    currentAccount: any;
    unitType: number;
    data: IOrganizationUnit;
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
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
        super();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
    }

    reloadOrganizationUnit() {
        this.organizationUnit = {};
        this.organizationUnit.unitType = 4;
        this.organizationUnit.accountingType = 0;
    }

    ngOnInit() {
        this.isSaving = false;
        this.listColumnsOrganizationUnit = ['organizationUnitCode', 'organizationUnitName'];
        this.listHeaderColumnsOrganizationUnit = ['Mã tổ chức/đơn vị', 'Tên tổ chức/đơn vị'];
        this.reloadOrganizationUnit();
        // cbb tài khoản
        this.accountService.query().subscribe(
            (res: HttpResponse<IAccountList[]>) => {
                this.accounts = res.body;
            }
            // (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.goodsServicePurchaseService.getGoodServicePurchases().subscribe((res: HttpResponse<IGoodsServicePurchase[]>) => {
            this.goodsServicePurchases = res.body;
        });
        this.currencyService.findAllByCompanyIDIsNull().subscribe((res: HttpResponse<ICurrency[]>) => {
            this.currencies = res.body;
        });
        this.accountListService.getAccountForOrganizationUnit().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountLists = res.body;
        });
        this.organizationUnitService.recursiveOrganizationUnitByParentIDPopup().subscribe((res: HttpResponse<IOrganizationUnit[]>) => {
            this.organizationUnits = res.body;
        });
    }

    private onError(errorMessage: string) {
        this.toastr.error(this.translate.instant('ebwebApp.mBCreditCard.error'), this.translate.instant('ebwebApp.mBCreditCard.message'));
    }

    closeAll() {
        this.router.navigate(['/organization-unit']);
    }

    save() {
        event.preventDefault();
        console.log(this.isShowPopup);
        this.isSaving = true;
        this.isSaveAndCreate = false;
        if (this.checkError()) {
            this.organizationUnit.isActive = true;
            this.subscribeToSaveResponse(this.organizationUnitService.create(this.organizationUnit));
        }
    }

    saveAndNew() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = true;
        if (this.checkError()) {
            this.organizationUnit.organizationUnitOptionReport = this.organizationUnitOptionReport;
            this.organizationUnit.isActive = true;
            this.subscribeToSaveResponse(this.organizationUnitService.create(this.organizationUnit));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.data = res.body.organizationUnit;
                    this.onSaveSuccess();
                } else {
                    this.toastr.error('error', 'error');
                }
            },
            (res: HttpErrorResponse) => this.onSaveError(res)
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.toastr.success(
            this.translate.instant('ebwebApp.organizationUnit.saveSuccess'),
            this.translate.instant('ebwebApp.organizationUnit.message')
        );
        if (!this.isSaveAndCreate) {
            this.eventManager.broadcast({
                name: 'saveSuccess',
                content: { name: CategoryName.PHONG_BAN, data: this.data }
            });
            this.close();
        } else {
            this.eventManager.broadcast({
                name: 'saveAndNewSuccess',
                content: { name: CategoryName.PHONG_BAN, data: this.data }
            });
            this.reloadOrganizationUnit();
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
        if (!this.organizationUnit.parentID) {
            this.toastr.error(
                this.translate.instant('ebwebApp.organizationUnit.parentIDIsNotBlank'),
                this.translate.instant('ebwebApp.organizationUnit.error')
            );
            return false;
        }
        return true;
    }

    exit() {
        if (this.modalRef) {
            this.modalRef.close();
            return;
        }
    }

    close() {
        this.eventManager.broadcast({
            name: 'closePopup',
            content: null
        });
        this.activeModal.dismiss(false);
    }

    clear() {
        if (this.modalRef) {
            this.modalRef.close();
            return;
        }
    }
}
