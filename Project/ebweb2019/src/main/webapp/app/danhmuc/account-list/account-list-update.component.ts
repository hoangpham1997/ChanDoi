import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from './account-list.service';
import { IAccountGroup } from 'app/shared/model/account-group.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { content } from 'html2canvas/dist/types/css/property-descriptors/content';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { Principal } from 'app/core';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-account-list-update',
    templateUrl: './account-list-update.component.html',
    styleUrls: ['./account-list-update.component.css']
})
export class AccountListUpdateComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content: TemplateRef<any>;
    @ViewChild('checkUse') checkUse: TemplateRef<any>;
    private _accountList: IAccountList;
    isSaving: boolean;
    listCheckbox: [{ value?: string; isChecked?: boolean }];
    accountLists: IAccountList[];
    typeAccountingObject: string;
    isSaveAndCreate: boolean;
    allAccountLists: IAccountList[];
    isDuplicateAccountNumber: boolean;
    accountListsCopy: IAccountList;
    backupDetailType: string;
    modalRef: NgbModalRef;

    ROLE_HeThongTaiKhoan_Them = ROLE.HeThongTaiKhoan_Them;
    ROLE_HeThongTaiKhoan_Xem = ROLE.HeThongTaiKhoan_Xem;
    ROLE_HeThongTaiKhoan_Sua = ROLE.HeThongTaiKhoan_Sua;
    ROLE_HeThongTaiKhoan_Xoa = ROLE.HeThongTaiKhoan_Xoa;

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
    currentAccount: any;
    isCreateUrl: boolean;
    isEditUrl: boolean;

    constructor(
        private jhiAlertService: JhiAlertService,
        private accountListService: AccountListService,
        private activatedRoute: ActivatedRoute,
        public utilsService: UtilsService,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService,
        private modalService: NgbModal,
        private principal: Principal
    ) {
        super();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.arrAuthorities = account.authorities;
            this.isEditUrl = window.location.href.includes('edit');
            this.isCreateUrl = window.location.href.includes('/new');
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_HeThongTaiKhoan_Sua)
                : true;
            this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_HeThongTaiKhoan_Them)
                : true;
        });
    }

    ngOnInit() {
        this.listCheckbox = [{}];
        this.accountListsCopy = {};
        this.accountLists = [];
        this.isDuplicateAccountNumber = false;
        for (let i = 0; i <= 8; i++) {
            this.listCheckbox.push({});
        }
        this.isSaving = false;
        this.isSaveAndCreate = false;
        this.activatedRoute.data.subscribe(({ accountList }) => {
            this.accountList = accountList;
            this.backupDetailType = accountList.detailType;
            // if (this.accountList.id) {
            //     if (this.accountList.detailType === '0' || this.accountList.detailType === '1' || this.accountList.detailType === '2') {
            //         this.typeAccountingObject = this.accountList.detailType;
            //         this.accountList.detailType = '0';
            //     }
            // } else {
            //     this.accountList.accountGroupKind = 0;
            // }
        });
        for (let i = 0; i <= 9; i++) {
            this.listCheckbox[i].value = '0';
            this.listCheckbox[i].isChecked = false;
        }
        if (this.accountList.detailType) {
            const lstacc = this.accountList.detailType.split(';');
            for (let j = 0; j < lstacc.length; j++) {
                if (lstacc[j] === '0' || lstacc[j] === '1' || lstacc[j] === '2') {
                    this.listCheckbox[0].isChecked = true;
                    this.typeAccountingObject = lstacc[j];
                } else {
                    this.listCheckbox[parseFloat(lstacc[j]) - 1].isChecked = true;
                }
            }
        }
        if (this.accountList.id) {
            this.accountListService
                .getAccountListsActiveExceptID({ id: this.accountList.id })
                .subscribe((res: HttpResponse<IAccountList[]>) => {
                    this.accountLists = res.body;
                });
        } else {
            this.accountListService.getAccountListsActive().subscribe((res: HttpResponse<IAccountList[]>) => {
                this.accountLists = res.body;
                this.accountList.accountGroupKind = 0;
            });
        }

        this.accountListService.getAccountLists().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.allAccountLists = res.body;
        });
        this.copy();
    }

    closeForm() {
        event.preventDefault();
        if (this.accountListsCopy) {
            if (!this.utilsService.isEquivalent(this.accountList, this.accountListsCopy)) {
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
        this.router.navigate(['/account-list']);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.HeThongTaiKhoan_Them, ROLE.HeThongTaiKhoan_Sua])
    save() {
        event.preventDefault();
        this.isSaving = true;
        this.accountList.detailType = '';
        this.isSaveAndCreate = false;
        for (let i = 0; i < this.listCheckbox.length; i++) {
            if (this.listCheckbox[i].isChecked) {
                if (this.accountList.detailType !== '') {
                    this.accountList.detailType += ';';
                }
                if (i === 0) {
                    this.accountList.detailType += this.typeAccountingObject.toString();
                } else {
                    this.accountList.detailType += (i + 1).toString();
                }
            }
        }
        if (this.accountList.detailType !== this.backupDetailType && this.accountList.id) {
            this.accountListService.checkOPNAndExistData({ accountNumber: this.accountList.accountNumber }).subscribe(
                (res: HttpResponse<any>) => {
                    if (this.accountList.parentAccountID) {
                        this.accountList.isParentNode = false;
                    } else {
                        this.accountList.isParentNode = true;
                    }
                    if (this.checkError()) {
                        if (this.accountList.id !== undefined) {
                            this.subscribeToSaveResponse(this.accountListService.update(this.accountList));
                        } else {
                            this.accountList.isActive = true;
                            this.subscribeToSaveResponse(this.accountListService.create(this.accountList));
                        }
                    }
                },
                (res: HttpErrorResponse) => {
                    if (res.error && res.error.errorKey === 'checkOPNAndExistData') {
                        this.modalRef = this.modalService.open(this.checkUse, { backdrop: 'static' });
                    }
                }
            );
        } else {
            if (this.checkError()) {
                if (this.accountList.id !== undefined) {
                    this.subscribeToSaveResponse(this.accountListService.update(this.accountList));
                } else {
                    this.accountList.isActive = true;
                    this.subscribeToSaveResponse(this.accountListService.create(this.accountList));
                }
            }
        }
    }

    continueSave() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.accountList.parentAccountID) {
            this.accountList.isParentNode = false;
        } else {
            this.accountList.isParentNode = true;
        }
        const checkAcc = this.allAccountLists.find(n => n.accountNumber === this.accountList.accountNumber);
        if (this.checkError()) {
            if (checkAcc && !this.accountList.id) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.accountList.accountNumberAlreadyExists'),
                    this.translate.instant('ebwebApp.accountList.message')
                );
                return;
            } else {
                if (this.accountList.id !== undefined) {
                    this.subscribeToSaveResponse(this.accountListService.update(this.accountList));
                } else {
                    this.accountList.isActive = true;
                    this.subscribeToSaveResponse(this.accountListService.create(this.accountList));
                }
            }
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.HeThongTaiKhoan_Them])
    saveAndNew() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = true;
        if (this.accountList.detailType === '0') {
            this.accountList.detailType = this.typeAccountingObject;
        }
        if (this.accountList.parentAccountID) {
            this.accountList.isParentNode = false;
        } else {
            this.accountList.isParentNode = true;
        }
        if (this.checkError()) {
            if (this.accountList.id !== undefined) {
                this.subscribeToSaveResponse(this.accountListService.update(this.accountList));
            } else {
                this.accountList.isActive = true;
                this.subscribeToSaveResponse(this.accountListService.create(this.accountList));
            }
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAccountList>>) {
        result.subscribe(
            (res: HttpResponse<IAccountList>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => {
                if (res.error.errorKey === 'existConstraint') {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.accountList.errorInsertConstraint'),
                        this.translate.instant('ebwebApp.accountList.error')
                    );
                } else if (res.error.errorKey === 'existAccount') {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.accountList.accountNumberAlreadyExists'),
                        this.translate.instant('ebwebApp.accountList.message')
                    );
                } else {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.accountList.error'),
                        this.translate.instant('ebwebApp.accountList.message')
                    );
                }
                this.onSaveError();
            }
        );
    }

    private onSaveSuccess() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.toastr.success(
            this.translate.instant('ebwebApp.accountList.successful'),
            this.translate.instant('ebwebApp.accountList.message')
        );
        if (this.isSaveAndCreate) {
            if (this.accountList.isActive === true) {
                this.accountLists.push(this.accountList);
            }
            this.accountList = {};
            for (let i = 0; i < this.listCheckbox.length; i++) {
                this.listCheckbox[i].isChecked = false;
            }
            this.accountList.accountGroupKind = 0;
            this.typeAccountingObject = null;
        } else {
            this.isSaving = false;
            this.router.navigate(['/account-list']);
        }
        this.copy();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    get accountList() {
        return this._accountList;
    }

    set accountList(accountList: IAccountList) {
        this._accountList = accountList;
    }

    checkError() {
        if (
            !this.accountList.accountNumber ||
            !this.accountList.accountName ||
            this.accountList.accountGroupKind === null ||
            this.accountList.accountGroupKind === undefined
        ) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountList.dataIsNotNull'),
                this.translate.instant('ebwebApp.accountList.error')
            );
            return false;
        }
        // if (!this.accountList.accountName) {
        //     this.toastr.error(
        //         this.translate.instant('ebwebApp.accountList.accountNameIsNotNull'),
        //         this.translate.instant('ebwebApp.accountList.message')
        //     );
        //     return false;
        // }
        // if (this.accountList.accountGroupKind === null || this.accountList.accountGroupKind === undefined) {
        //     this.toastr.error(
        //         this.translate.instant('ebwebApp.accountList.accountGroupKindIsNotNull'),
        //         this.translate.instant('ebwebApp.accountList.message')
        //     );
        //     return false;
        // }
        // if (!this.accountList.detailType) {
        //     this.toastr.error(this.translate.instant("ebwebApp.accountList.detailTypeIsNotNull"),
        //         this.translate.instant("ebwebApp.accountList.message"));
        //     return false;
        // }
        return true;
    }

    selectChangeAccountingObject(value) {
        if (value === 0 && this.listCheckbox[0].isChecked) {
            this.listCheckbox[4].isChecked = false;
            this.listCheckbox[5].isChecked = false;
            this.accountList.detailByAccountObject = 1;
            if (!this.typeAccountingObject) {
                this.typeAccountingObject = '0';
            }
        } else if (value === 4 && this.listCheckbox[4].isChecked) {
            this.listCheckbox[0].isChecked = false;
            this.listCheckbox[5].isChecked = false;
            this.typeAccountingObject = null;
            this.accountList.detailByAccountObject = 0;
        } else if (value === 5 && this.listCheckbox[5].isChecked) {
            this.listCheckbox[4].isChecked = false;
            this.listCheckbox[0].isChecked = false;
            this.typeAccountingObject = null;
            this.accountList.detailByAccountObject = 0;
        } else if (value === 0 && !this.listCheckbox[0].isChecked) {
            this.typeAccountingObject = null;
            this.accountList.detailByAccountObject = 0;
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.HeThongTaiKhoan_Xoa])
    delete() {
        event.preventDefault();
        if (this.accountList.id) {
            this.router.navigate(['/account-list', { outlets: { popup: this.accountList.id + '/delete' } }]);
        }
    }

    /*
    * hàm ss du lieu 2 object và 2 mảng object
    * return true: neu tat ca giong nhau
    * return fale: neu 1 trong cac object ko giong nhau
    * */
    canDeactive(): boolean {
        return this.utilsService.isEquivalent(this.accountList, this.accountListsCopy);
    }

    copy() {
        this.accountListsCopy = Object.assign({}, this.accountList);
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
        this.accountList.detailType = '';
        this.isSaveAndCreate = false;
        for (let i = 0; i < this.listCheckbox.length; i++) {
            if (this.listCheckbox[i].isChecked) {
                if (this.accountList.detailType !== '') {
                    this.accountList.detailType += ';';
                }
                if (i === 0) {
                    this.accountList.detailType += this.typeAccountingObject.toString();
                } else {
                    this.accountList.detailType += (i + 1).toString();
                }
            }
        }
        if (this.accountList.parentAccountID) {
            this.accountList.isParentNode = false;
        } else {
            this.accountList.isParentNode = true;
        }
        const checkAcc = this.allAccountLists.find(n => n.accountNumber === this.accountList.accountNumber);
        if (this.checkError()) {
            if (checkAcc && !this.accountList.id) {
                this.toastr.error(
                    this.translate.instant('ebwebApp.accountList.accountNumberAlreadyExists'),
                    this.translate.instant('ebwebApp.accountList.message')
                );
                return;
            } else {
                if (this.accountList.id !== undefined) {
                    this.subscribeToSaveResponse(this.accountListService.update(this.accountList));
                } else {
                    this.accountList.isActive = true;
                    this.subscribeToSaveResponse(this.accountListService.create(this.accountList));
                }
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
        this.router.navigate(['/account-list']);
    }
}
