import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { PaymentClauseService } from 'app/entities/payment-clause';
import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';
import { AccountingObjectGroupService } from 'app/danhmuc/accounting-object-group';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { AccountingObjectBankAccountService } from 'app/entities/accounting-object-bank-account';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { KhachHangNhaCungCapService } from 'app/shared/danh-muc-combobox/khach-hang-nha-cung-cap/khach-hang-nha-cung-cap.service';
import { ToastrService } from 'ngx-toastr';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { CategoryName } from 'app/app.constants';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { ComboboxModalService } from 'app/core/login/combobox-modal.service';
import { IBank } from 'app/shared/model/bank.model';
import { BankService } from 'app/danhmuc/bank';
import { ITransportMethod } from 'app/shared/model/transport-method.model';
import { ROLE } from 'app/role.constants';
import { TransportMethodService } from 'app/danhmuc/transport-method';
import { Principal } from 'app/core';
import { UnitService } from 'app/danhmuc/unit';

@Component({
    selector: 'eb-phuong-thuc-van-chuyen-cobobox',
    templateUrl: './phuong-thuc-van-chuyen.html',
    styleUrls: ['./phuong-thuc-van-chuyen.css']
})
export class EbTransportMethodComboboxComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content: any;
    transportMethod: ITransportMethod;
    transportMethodCopy: ITransportMethod;
    modalRef: NgbModalRef;
    data: ITransportMethod;
    roleSua: boolean;
    roleThem: boolean;
    arrAuthorities: any[];
    saveandadd = false;
    ROLE_TransportMethod_Them = ROLE.DanhMucPhuongThucVanChuyen_Them;
    ROLE_TransportMethod_Sua = ROLE.DanhMucPhuongThucVanChuyen_Sua;
    ROLE_TransportMethod_Xoa = ROLE.DanhMucPhuongThucVanChuyen_Xoa;
    isSaving: boolean;
    saveSuccess: boolean;
    isEditUrl: boolean;
    isSaveAndCreate: boolean;
    isCheck: boolean;

    constructor(
        private transportmethodservice: TransportMethodService,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private router: Router,
        private principal: Principal,
        private unitService: UnitService,
        private activeRoute: ActivatedRoute,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        private activeModal: NgbActiveModal
    ) {
        super();
    }

    ngOnInit() {
        this.isSaving = false;
        this.isSaveAndCreate = false;
        this.ROLE_TransportMethod_Them = ROLE.DanhMucPhuongThucVanChuyen_Them;
        this.ROLE_TransportMethod_Sua = ROLE.DanhMucPhuongThucVanChuyen_Sua;
        this.ROLE_TransportMethod_Xoa = ROLE.DanhMucPhuongThucVanChuyen_Xoa;
        this.principal.identity().then(account => {
            this.isEditUrl = window.location.href.includes('/edit') || window.location.href.includes('/view');
            this.arrAuthorities = account.authorities;
            this.roleSua = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_TransportMethod_Sua) : true;
            this.roleThem = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_TransportMethod_Them)
                : true;
        });
        this.transportMethod = {
            transportMethodCode: '',
            transportMethodName: '',
            description: '',
            isActive: true,
            isSecurity: true
        };
        this.copy();
        // this.eventSupcriber= this.eventManager.subscribe("TransportMethod",this.previousState());
    }
    copy() {
        this.transportMethodCopy = Object.assign({}, this.transportMethod);
    }
    ngOnDestroy(): void {}

    delete() {
        event.preventDefault();
        if (this.transportMethod.id) {
            this.router.navigate(['/transport-method', { outlets: { popup: this.transportMethod.id + '/delete' } }]);
        }
    }

    previousState() {
        this.router.navigate(['transport-method']);
    }

    saveAndNew() {
        event.preventDefault();
        this.saveandadd = true;
        this.isSaveAndCreate = true;
        this.isCheck = true;
        if (!this.transportMethod.transportMethodCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.transportMethod.errorInput'),
                this.translate.instant('ebwebApp.bankAccountDetails.notification')
            );
            return;
        }
        if (!this.transportMethod.transportMethodName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.transportMethod.errorInput'),
                this.translate.instant('ebwebApp.bankAccountDetails.notification')
            );
            return;
        }
        // update sau khi sua
        this.saveAfter();
    }
    save() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = false;
        this.isCheck = false;
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (!this.transportMethod.transportMethodCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.transportMethod.errorInput'),
                this.translate.instant('ebwebApp.bankAccountDetails.notification')
            );
            return;
        }
        if (!this.transportMethod.transportMethodName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.transportMethod.errorInput'),
                this.translate.instant('ebwebApp.bankAccountDetails.notification')
            );
            return;
        }
        // update sau khi sua
        this.saveAfter();
        this.close();
    }

    saveAfter() {
        if (this.transportMethod.id) {
            this.transportmethodservice.update(this.transportMethod).subscribe(
                res => {
                    this.data = res.body;
                    this.copy();
                    this.onSaveSuccess();
                    if (!this.saveandadd) {
                        this.previousState();
                    } else {
                        this.router.navigate(['transport-method/new']);
                    }
                },
                error => {
                    this.error(error);
                }
            );
        } else {
            this.transportMethod.isActive = true;
            this.transportmethodservice.create(this.transportMethod).subscribe(
                res => {
                    this.data = res.body;
                    this.copy();
                    this.onSaveSuccess();
                    if (!this.saveandadd) {
                        this.previousState();
                    } else {
                        this.transportMethod = {
                            transportMethodCode: '',
                            transportMethodName: '',
                            description: '',
                            isActive: true,
                            isSecurity: true
                        };
                        this.copy();
                        this.saveandadd = false;
                    }
                },
                error => {
                    this.error(error);
                }
            );
        }
    }

    private onSaveSuccess() {
        this.copy();
        this.isSaving = false;
        this.saveSuccess = true;
        if (this.isSaveAndCreate) {
            this.eventManager.broadcast({
                name: 'saveAndNewSuccess',
                content: { name: CategoryName.PHUONG_THUC_VAN_CHUYEN, data: this.data }
            });
            this.bankAccountDetails = {};
        } else {
            this.isSaving = false;
            this.eventManager.broadcast({
                name: 'saveSuccess',
                content: { name: CategoryName.PHUONG_THUC_VAN_CHUYEN, data: this.data }
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

    error(err) {
        this.toastr.error(this.translate.instant(`ebwebApp.transportMethod.${err.error.message}`));
    }

    close() {
        this.eventManager.broadcast({
            name: 'closePopup',
            content: null
        });
        this.activeModal.dismiss(false);
        this.closeContent();
    }

    closeForm() {
        event.preventDefault();
        if (this.transportMethodCopy) {
            if (!this.utilsService.isEquivalent(this.transportMethod, this.transportMethodCopy)) {
                this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
                return;
            } else {
                this.close();
                return;
            }
        } else {
            this.copy();
            this.closeAll();
            return;
        }
    }

    closeAll() {
        this.close();
    }

    closeContent() {
        this.close();
        this.copy();
        this.previousState();
    }

    saveContent() {
        this.close();
        this.save();
    }

    canDeactive() {
        return this.utilsService.isEquivalent(this.transportMethod, this.transportMethodCopy);
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }
}
