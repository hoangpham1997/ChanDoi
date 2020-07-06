import { AfterViewInit, Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { BankAccountDetailsService } from './bank-account-details.service';
import { IBank } from 'app/shared/model/bank.model';
import { BankService } from 'app/danhmuc/bank';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IUnit } from 'app/shared/model/unit.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { Principal } from 'app/core';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { IMBDepositDetails } from 'app/shared/model/mb-deposit-details.model';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-bank-account-details-update',
    templateUrl: './bank-account-details-update.component.html',
    styleUrls: ['./bank-account-details-update.component.css']
})
export class BankAccountDetailsUpdateComponent extends BaseComponent implements OnInit, AfterViewInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _bankAccountDetails: IBankAccountDetails;
    isSaving: boolean;
    isSaveAndCreate: boolean;
    isEditUrl: boolean;
    month: any;
    banks: IBank[];
    bankPopup: IBank[];
    listColumnsBank: string[];
    listHeaderColumnsBank: string[];
    organizationunits: IOrganizationUnit[];
    modalRef: NgbModalRef;
    isClose: boolean;
    bankAccountDetailsCopy: IBankAccountDetails;
    bankAccount: any;
    bAnkAccountDetails: IBankAccountDetails[];
    eventSubscriber: Subscription;
    arrAuthorities: any[];
    isRoleSua: boolean;
    isRoleThem: boolean;
    isCreateUrl: boolean;
    test: any;
    ROLE_DanhMucTaiKhoanNganHang_Them = ROLE.DanhMucTaiKhoanNganHang_Them;
    ROLE_DanhMucTaiKhoanNganHang_Sua = ROLE.DanhMucTaiKhoanNganHang_Sua;
    ROLE_DanhMucTaiKhoanNganHang_Xoa = ROLE.DanhMucTaiKhoanNganHang_Xoa;
    ROLE_DanhMucTaiKhoanNganHang_Xem = ROLE.DanhMucTaiKhoanNganHang_Xem;

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
        private eventManager: JhiEventManager
    ) {
        super();
        this.principal.identity().then(account => {
            this.bankAccount = account;
            this.arrAuthorities = account.authorities;
            this.isEditUrl = window.location.href.includes('edit');
            this.isCreateUrl = window.location.href.includes('/new');
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_DanhMucTaiKhoanNganHang_Sua)
                : true;
            this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_DanhMucTaiKhoanNganHang_Them)
                : true;
        });
    }

    ngOnInit() {
        this.isClose = false;
        this.isSaving = false;
        this.isSaveAndCreate = false;
        this.isEditUrl = window.location.href.includes('/edit') || window.location.href.includes('/view');
        this.isCreateUrl = window.location.href.includes('bank-account-details/new');
        this.activatedRoute.data.subscribe(({ bankAccountDetails }) => {
            this.bankAccountDetails = bankAccountDetails;
        });
        this.bankService.getBanks().subscribe(
            (res: HttpResponse<IBank[]>) => {
                this.bankPopup = res.body;
                const getBankPopup = this.bankPopup.filter(n => n.bankName === this.bankAccountDetails.bankName);
                if (getBankPopup !== undefined || getBankPopup !== null) {
                    this.bankAccountDetails.bankID = getBankPopup[0];
                }
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
        this.activatedRoute.data.subscribe(({ bankAccountDetails }) => {
            this.bankAccountDetails = bankAccountDetails;
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
        this.registerIsShowPopup();
    }

    registerIsShowPopup() {
        this.utilsService.checkEvent.subscribe(res => {
            this.isShowPopup = res;
        });
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }

    previousState() {
        this.router.navigate(['/bank-account-details']);
    }

    save() {
        event.preventDefault();
        if ((this.isCreateUrl && !this.utilsService.isShowPopup) || (this.isEditUrl && !this.utilsService.isShowPopup)) {
            this.isSaving = true;
            this.isSaveAndCreate = false;
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
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.onSaveSuccess();
                } else {
                    this.toastr.error(this.translate.instant('ebwebApp.unit.existData'));
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isClose = true;
        if (this.isSaveAndCreate) {
            this.bankAccountDetails = {};
        } else {
            this.isSaving = false;
            this.previousState();
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

    private onSaveError() {
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

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucTaiKhoanNganHang_Xoa])
    delete() {
        event.preventDefault();
        if (this.bankAccountDetails.id) {
            this.router.navigate(['/bank-account-details', { outlets: { popup: this.bankAccountDetails.id + '/delete' } }]);
        }
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isClose = true;
        this.router.navigate(['bank-account-details']);
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

    closeForm() {
        event.preventDefault();
        if (this.bankAccountDetailsCopy) {
            if (!this.utilsService.isEquivalent(this.bankAccountDetails, this.bankAccountDetailsCopy)) {
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
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.router.navigate(['/bank-account-details']);
    }

    saveDetails(i) {
        this.currentRow = i;
        this.details = this.bAnkAccountDetails;
    }

    saveParent() {
        this.currentRow = null;
        this.parent = this.bankAccountDetails;
    }

    addDataToDetail() {
        this.bAnkAccountDetails = this.details ? this.details : this.bAnkAccountDetails;
        this.bankAccountDetails = this.parent ? this.parent : this.bankAccountDetails;
        this.bankService.findIdPopup(this.bankAccountDetails.id).subscribe(res => {
            this.bankAccountDetails.bankID = res.body;
        });
        this.bankAccountDetails.id = undefined;
    }
}
