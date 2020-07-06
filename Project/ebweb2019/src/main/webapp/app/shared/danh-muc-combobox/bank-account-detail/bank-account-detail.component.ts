import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IBank } from 'app/shared/model/bank.model';
import { BankService } from 'app/danhmuc/bank';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { CategoryName } from 'app/app.constants';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';
import { Principal } from 'app/core';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';

@Component({
    selector: 'eb-bank-account-details-cobobox',
    templateUrl: './bank-account-detail.component.html',
    styleUrls: ['./bank-account-detail.component.css']
})
export class EbBankAccountDetailComboboxComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content: TemplateRef<any>;
    data: IBankAccountDetails;
    private _bankAccountDetails: IBankAccountDetails;
    isSaving: boolean;
    isSaveAndCreate: boolean;
    isEditUrl: boolean;
    month: any;
    banks: IBank[];
    listColumnsBank: string[];
    listHeaderColumnsBank: string[];
    organizationunits: IOrganizationUnit[];
    modalRef: NgbModalRef;
    isClose: boolean;
    bankAccountDetailsCopy: IBankAccountDetails;
    bankAccount: any;
    isCheck: boolean;
    saveSuccess: boolean;

    constructor(
        private jhiAlertService: JhiAlertService,
        private bankAccountDetailsService: BankAccountDetailsService,
        private bankService: BankService,
        private toastr: ToastrService,
        private organizationUnitService: OrganizationUnitService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private translate: TranslateService,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private principal: Principal,
        private eventManager: JhiEventManager,
        private activeModal: NgbActiveModal
    ) {
        super();
        this.principal.identity().then(account => {
            this.bankAccount = account;
        });
    }

    ngOnInit() {
        this.isClose = false;
        this.isSaving = false;
        this.isSaveAndCreate = false;
        this.isEditUrl = window.location.href.includes('/edit') || window.location.href.includes('/view');
        this.bankAccountDetails = {};
        this.bankService.getBanks().subscribe(
            (res: HttpResponse<IBank[]>) => {
                this.banks = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.organizationUnitService.query().subscribe(
            (res: HttpResponse<IOrganizationUnit[]>) => {
                this.organizationunits = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.listColumnsBank = ['bankCode', 'bankName'];
        this.listHeaderColumnsBank = ['Mã ngân hàng', 'Tên ngân hàng'];
        this.copy();
    }

    previousState() {
        window.history.back();
    }

    save() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = false;
        this.isCheck = false;
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (!this.bankAccountDetails.bankAccount) {
            this.toastr.error(
                this.translate.instant('ebwebApp.bankAccountDetails.missBankAccount'),
                this.translate.instant('ebwebApp.bankAccountDetails.notification')
            );
            return;
        }
        if (!this.bankAccountDetails.bankID) {
            this.toastr.error(
                this.translate.instant('ebwebApp.bankAccountDetails.missBankName'),
                this.translate.instant('ebwebApp.bankAccountDetails.notification')
            );
            return;
        }
        // this.bankAccountDetails.bankName = this.banks.find(n => n.id === this.bankAccountDetails.bankID).bankName;
        this.bankAccountDetails.bankName = this.bankAccountDetails.bankID.bankName;
        if (this.bankAccountDetails.id !== undefined) {
            this.subscribeToSaveResponse(this.bankAccountDetailsService.update(this.bankAccountDetails));
        } else {
            this.bankAccountDetails.isActive = true;
            this.subscribeToSaveResponse(this.bankAccountDetailsService.create(this.bankAccountDetails));
        }
        this.close();
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.data = res.body.bankAccountDetails;
                    this.onSaveSuccess();
                } else {
                    this.toastr.error(this.translate.instant('ebwebApp.unit.existData'));
                }
            },
            (res: HttpErrorResponse) => this.onSaveError(res)
        );
    }

    private onSaveSuccess() {
        this.copy();
        this.isSaving = false;
        this.saveSuccess = true;
        if (this.isSaveAndCreate) {
            this.eventManager.broadcast({
                name: 'saveAndNewSuccess',
                content: { name: CategoryName.TAI_KHOAN_NGAN_HANG, data: this.data }
            });
            this.bankAccountDetails = {};
        } else {
            this.isSaving = false;
            this.eventManager.broadcast({
                name: 'saveSuccess',
                content: { name: CategoryName.TAI_KHOAN_NGAN_HANG, data: this.data }
            });
            this.close();
        }
        if (this.isEditUrl) {
            this.toastr.success(
                this.translate.instant('ebwebApp.bankAccountDetails.insertDataSuccess'),
                this.translate.instant('ebwebApp.bankAccountDetails.notification')
            );
        } else {
            this.toastr.success(
                this.translate.instant('ebwebApp.bankAccountDetails.insertDataSuccess'),
                this.translate.instant('ebwebApp.bankAccountDetails.notification')
            );
        }
    }

    private onSaveError(err) {
        this.isSaving = false;
        if (this.isEditUrl) {
            this.toastr.error(
                this.translate.instant('ebwebApp.bankAccountDetails.editDataFail'),
                this.translate.instant('ebwebApp.bankAccountDetails.notification')
            );
        } else {
            this.toastr.error(
                this.translate.instant('ebwebApp.bankAccountDetails.insertDataFail'),
                this.translate.instant('ebwebApp.bankAccountDetails.notification')
            );
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackBankById(index: number, item: IBank) {
        return item.id;
    }

    trackOrganizationUnitById(index: number, item: IOrganizationUnit) {
        return item.id;
    }

    get bankAccountDetails() {
        return this._bankAccountDetails;
    }

    set bankAccountDetails(bankAccountDetails: IBankAccountDetails) {
        this._bankAccountDetails = bankAccountDetails;
    }

    saveAndNew() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = true;
        this.isCheck = true;
        if (!this.bankAccountDetails.bankAccount) {
            this.toastr.error(
                this.translate.instant('ebwebApp.bankAccountDetails.missBankAccount'),
                this.translate.instant('ebwebApp.bankAccountDetails.notification')
            );
            return;
        }
        if (!this.bankAccountDetails.bankID) {
            this.toastr.error(
                this.translate.instant('ebwebApp.bankAccountDetails.missBankName'),
                this.translate.instant('ebwebApp.bankAccountDetails.notification')
            );
            return;
        }
        this.bankAccountDetails.bankName = this.bankAccountDetails.bankID.bankName;
        if (this.bankAccountDetails.id !== undefined) {
            this.subscribeToSaveResponse(this.bankAccountDetailsService.update(this.bankAccountDetails));
        } else {
            this.bankAccountDetails.isActive = true;
            this.subscribeToSaveResponse(this.bankAccountDetailsService.create(this.bankAccountDetails));
        }
    }

    delete() {
        event.preventDefault();
        if (this.bankAccountDetails.id) {
            this.router.navigate(['/bank-account-details', { outlets: { popup: this.bankAccountDetails.id + '/delete' } }]);
        }
    }

    close() {
        this.eventManager.broadcast({
            name: 'closePopup',
            content: null
        });
        this.activeModal.dismiss(false);
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    closeEdit(content) {
        if (!this.utilsService.isEquivalent(this.bankAccountDetails, this.bankAccountDetailsCopy)) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else {
            this.close();
        }
    }

    copy() {
        this.bankAccountDetailsCopy = Object.assign({}, this.bankAccountDetails);
    }

    canDeactive(): boolean {
        event.preventDefault();
        if (!this.isClose) {
            return this.utilsService.isEquivalent(this.bankAccountDetails, this.bankAccountDetailsCopy);
        }
        return true;
    }
}
