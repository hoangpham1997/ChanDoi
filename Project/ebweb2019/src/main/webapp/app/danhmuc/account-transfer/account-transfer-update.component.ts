import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IAccountTransfer } from 'app/shared/model/account-transfer.model';
import { AccountTransferService } from './account-transfer.service';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Principal } from 'app/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-account-transfer-update',
    templateUrl: './account-transfer-update.component.html',
    styleUrls: ['./account-transfer-update.component.css']
})
export class AccountTransferUpdateComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _accountTransfer: IAccountTransfer;
    isSaving: boolean;
    accountLists: IAccountList[];
    isSaveAndCreate: boolean;
    currentAccount: any;
    modalRef: NgbModalRef;
    accountTransferCopy: IAccountTransfer;

    ROLE_TaiKhoanKetChuyen_Them = ROLE.TaiKhoanKetChuyen_Them;
    ROLE_TaiKhoanKetChuyen_Xem = ROLE.TaiKhoanKetChuyen_Xem;
    ROLE_TaiKhoanKetChuyen_Sua = ROLE.TaiKhoanKetChuyen_Sua;
    ROLE_TaiKhoanKetChuyen_Xoa = ROLE.TaiKhoanKetChuyen_Xoa;

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

    arrAuthorities: any[];
    isRoleSua: boolean;
    isRoleThem: boolean;
    isCreateUrl: boolean;
    isEditUrl: boolean;

    constructor(
        private accountTransferService: AccountTransferService,
        private activatedRoute: ActivatedRoute,
        private accountListService: AccountListService,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private router: Router,
        private principal: Principal,
        private modalService: NgbModal
    ) {
        super();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.arrAuthorities = account.authorities;
            this.isEditUrl = window.location.href.includes('edit');
            this.isCreateUrl = window.location.href.includes('/new');
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_TaiKhoanKetChuyen_Sua)
                : true;
            this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_TaiKhoanKetChuyen_Them)
                : true;
        });
    }

    ngOnInit() {
        this.isSaving = false;
        this.isSaveAndCreate = false;
        this.accountTransferCopy = {};
        this.activatedRoute.data.subscribe(({ accountTransfer }) => {
            this.accountTransfer = accountTransfer;
        });
        this.accountListService.getAccountListsActive().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountLists = res.body;
        });
        this.isCreateUrl = false;
        this.isCreateUrl = window.location.href.includes('/new');
        this.copy();
    }

    closeForm() {
        event.preventDefault();
        if (this.accountTransferCopy) {
            if (!this.utilsService.isEquivalent(this.accountTransfer, this.accountTransferCopy)) {
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
        this.router.navigate(['/account-transfer']);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TaiKhoanKetChuyen_Them, ROLE.TaiKhoanNgamDinh_Sua])
    save() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = false;
        if (this.checkError()) {
            if (this.accountTransfer.id !== undefined) {
                this.subscribeToSaveResponse(this.accountTransferService.update(this.accountTransfer));
            } else {
                this.accountTransfer.isActive = true;
                this.subscribeToSaveResponse(this.accountTransferService.create(this.accountTransfer));
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TaiKhoanKetChuyen_Them])
    saveAndNew() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = true;
        if (this.checkError()) {
            if (this.accountTransfer.id !== undefined) {
                this.subscribeToSaveResponse(this.accountTransferService.update(this.accountTransfer));
            } else {
                this.accountTransfer.isActive = true;
                this.subscribeToSaveResponse(this.accountTransferService.create(this.accountTransfer));
            }
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAccountTransfer>>) {
        result.subscribe((res: HttpResponse<IAccountTransfer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.toastr.success(
            this.translate.instant('ebwebApp.accountList.successful'),
            this.translate.instant('ebwebApp.accountList.message')
        );
        if (this.isSaveAndCreate) {
            this.accountTransfer = {};
            this.accountTransfer.isActive = true;
            this.copy();
        } else {
            this.copy();
            this.router.navigate(['/account-transfer']);
        }
    }

    private onSaveError() {
        this.toastr.error(this.translate.instant('ebwebApp.accountList.error'), this.translate.instant('ebwebApp.accountList.message'));
        this.isSaving = false;
    }

    get accountTransfer() {
        return this._accountTransfer;
    }

    set accountTransfer(accountTransfer: IAccountTransfer) {
        this._accountTransfer = accountTransfer;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TaiKhoanKetChuyen_Xoa])
    delete() {
        event.preventDefault();
        if (this.accountTransfer.id) {
            this.router.navigate(['/account-transfer', { outlets: { popup: this.accountTransfer.id + '/delete' } }]);
        }
    }

    checkError(): boolean {
        if (
            !this.accountTransfer.accountTransferOrder ||
            !this.accountTransfer.accountTransferCode ||
            !this.accountTransfer.fromAccount ||
            !this.accountTransfer.toAccount ||
            this.accountTransfer.fromAccountData === null ||
            this.accountTransfer.fromAccountData === undefined ||
            this.accountTransfer.debitAccount === null ||
            this.accountTransfer.debitAccount === undefined ||
            this.accountTransfer.creditAccount === null ||
            this.accountTransfer.creditAccount === undefined
        ) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountTransfer.dataIsNotNull'),
                this.translate.instant('ebwebApp.accountTransfer.error')
            );
            return false;
        }
        // if (!this.accountTransfer.accountTransferCode) {
        //     this.toastr.error(
        //         this.translate.instant('ebwebApp.accountTransfer.dataIsNotNull'),
        //         this.translate.instant('ebwebApp.accountTransfer.error')
        //     );
        //     return false;
        // }
        // if (!this.accountTransfer.fromAccount) {
        //     this.toastr.error(
        //         this.translate.instant('ebwebApp.accountTransfer.dataIsNotNull'),
        //         this.translate.instant('ebwebApp.accountTransfer.error')
        //     );
        //     return false;
        // }
        // if (!this.accountTransfer.toAccount) {
        //     this.toastr.error(
        //         this.translate.instant('ebwebApp.accountTransfer.dataIsNotNull'),
        //         this.translate.instant('ebwebApp.accountTransfer.error')
        //     );
        //     return false;
        // }
        // if (this.accountTransfer.fromAccountData === null || this.accountTransfer.fromAccountData === undefined) {
        //     this.toastr.error(
        //         this.translate.instant('ebwebApp.accountTransfer.dataIsNotNull'),
        //         this.translate.instant('ebwebApp.accountTransfer.error')
        //     );
        //     return false;
        // }
        // if (this.accountTransfer.debitAccount === null || this.accountTransfer.debitAccount === undefined) {
        //     this.toastr.error(
        //         this.translate.instant('ebwebApp.accountTransfer.dataIsNotNull'),
        //         this.translate.instant('ebwebApp.accountTransfer.error')
        //     );
        //     return false;
        // }
        // if (this.accountTransfer.creditAccount === null || this.accountTransfer.creditAccount === undefined) {
        //     this.toastr.error(
        //         this.translate.instant('ebwebApp.accountTransfer.creditAccountIsNotNull'),
        //         this.translate.instant('ebwebApp.accountTransfer.error')
        //     );
        //     return false;
        // }
        return true;
    }

    /*
    * hàm ss du lieu 2 object và 2 mảng object
    * return true: neu tat ca giong nhau
    * return fale: neu 1 trong cac object ko giong nhau
    * */
    canDeactive(): boolean {
        return this.utilsService.isEquivalent(this.accountTransfer, this.accountTransferCopy);
    }

    copy() {
        this.accountTransferCopy = Object.assign({}, this.accountTransfer);
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
            if (this.accountTransfer.id !== undefined) {
                this.subscribeToSaveResponse(this.accountTransferService.update(this.accountTransfer));
            } else {
                this.accountTransfer.isActive = true;
                this.subscribeToSaveResponse(this.accountTransferService.create(this.accountTransfer));
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
        this.router.navigate(['/account-transfer']);
    }
}
