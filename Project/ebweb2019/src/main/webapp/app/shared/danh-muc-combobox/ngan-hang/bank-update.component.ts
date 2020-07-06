import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IBank } from 'app/shared/model/bank.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ICreditCard } from 'app/shared/model/credit-card.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { BankService } from 'app/danhmuc/bank';
import { CategoryName } from 'app/app.constants';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'eb-bank-update',
    templateUrl: './bank-update.component.html',
    styleUrls: ['./bank-update.component.css']
})
export class BankComboboxComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _bank: IBank;
    isSaving: boolean;
    isSaveAndCreate: boolean;
    isEditUrl: boolean;
    banks: IBank[];
    isClose: boolean;
    modalRef: NgbModalRef;
    bankCopy: IBank;
    ROLE_NganHang_Xoa = ROLE.DanhMucNganHang_Xoa;
    ROLE_NganHang_Them = ROLE.DanhMucNganHang_Them;
    ROLE_NganHang_Sua = ROLE.DanhMucNganHang_Sua;
    data: IBank;

    constructor(
        private bankService: BankService,
        private toastr: ToastrService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private translate: TranslateService,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private eventManager: JhiEventManager,
        private activeModal: NgbActiveModal
    ) {
        super();
    }

    ngOnInit() {
        this.isClose = false;
        this.isSaving = false;
        this.bank = this.data ? this.data : {};
        this.reloadBank();
        this.copy();
        this.bankService.getBanks().subscribe((res: HttpResponse<IBank[]>) => {
            this.banks = res.body;
        });
        this.isEditUrl = window.location.href.includes('/edit') || window.location.href.includes('/view');
    }

    previousState() {
        this.router.navigate(['/bank']);
    }

    copy() {
        this.bankCopy = Object.assign({}, this.bank);
    }

    closeEdit(content) {
        if (!this.utilsService.isEquivalent(this.bank, this.bankCopy)) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else {
            this.close();
        }
    }

    save() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = false;
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (!this.bank.bankCode) {
            this.toastr.error(this.translate.instant('ebwebApp.bank.bankCodeNotNull'), this.translate.instant('ebwebApp.bank.message'));
            return false;
        }
        if (!this.bank.bankName) {
            this.toastr.error(this.translate.instant('ebwebApp.bank.bankNameNotNull'), this.translate.instant('ebwebApp.bank.message'));
            return false;
        }
        if (this.bank.id !== undefined) {
            this.subscribeToSaveResponse(this.bankService.update(this.bank));
        } else {
            this.bank.isActive = true;
            this.subscribeToSaveResponse(this.bankService.create(this.bank));
        }
        this.closeContent();
    }

    saveAndNew() {
        event.preventDefault();
        this.isSaveAndCreate = true;
        this.isSaving = true;
        if (!this.bank.bankCode) {
            this.toastr.error(this.translate.instant('ebwebApp.bank.bankCodeNotNull'), this.translate.instant('ebwebApp.bank.message'));
            return;
        }
        if (!this.bank.bankName) {
            this.toastr.error(this.translate.instant('ebwebApp.bank.bankNameNotNull'), this.translate.instant('ebwebApp.bank.message'));
            return;
        }
        if (this.bank.id !== undefined) {
            this.subscribeToSaveResponse(this.bankService.update(this.bank));
        } else {
            this.bank.isActive = true;
            this.subscribeToSaveResponse(this.bankService.create(this.bank));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.data = res.body;
                    this.onSaveSuccess();
                } else {
                    this.toastr.error(this.translate.instant('ebwebApp.bank.check'), this.translate.instant('ebwebApp.bank.message'));
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    delete() {
        this.router.navigate(['/bank', { outlets: { popup: this.bank.id + '/delete' } }]);
    }

    private subscribeToSaveResponseAndContinute(result: Observable<HttpResponse<IBank>>) {
        result.subscribe(
            (res: HttpResponse<IAutoPrinciple>) => {
                this.onSaveSuccess();
                this.router.navigate(['/bank', res.body.id, 'edit']).then(() => {
                    this.router.navigate(['/bank', 'new']);
                });
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.toastr.success(this.translate.instant('ebwebApp.bank.insertDataSuccess'), this.translate.instant('ebwebApp.bank.message'));
        if (!this.isSaveAndCreate) {
            this.eventManager.broadcast({
                name: 'saveSuccess',
                content: { name: CategoryName.NGAN_HANG, data: this.data }
            });
            this.close();
        } else {
            this.eventManager.broadcast({
                name: 'saveAndNewSuccess',
                content: { name: CategoryName.NGAN_HANG, data: this.data }
            });
            this.reloadBank();
        }
    }

    reloadBank() {
        this.bank = {};
    }

    private onSaveError() {
        this.toastr.error(this.translate.instant('ebwebApp.bank.insertDataFailed'), this.translate.instant('ebwebApp.bank.message'));
        this.isSaving = false;
    }

    get bank() {
        return this._bank;
    }

    set bank(bank: IBank) {
        this._bank = bank;
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

    canDeactive(): boolean {
        if (!this.isClose) {
            return this.utilsService.isEquivalent(this.bank, this.bankCopy);
        }
        return true;
    }

    closeForm() {
        event.preventDefault();
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.bankCopy) {
            if (!this.utilsService.isEquivalent(this.bank, this.bankCopy)) {
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
        this.router.navigate(['/bank']);
    }
}
