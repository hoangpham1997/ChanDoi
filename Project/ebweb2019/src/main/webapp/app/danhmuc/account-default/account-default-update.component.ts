import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IAccountDefault } from 'app/shared/model/account-default.model';
import { AccountDefaultService } from './account-default.service';
import { IType } from 'app/shared/model/type.model';
import { TypeService } from 'app/entities/type';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RefModalService } from 'app/core/login/ref-modal.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { Principal } from 'app/core';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-account-default-update',
    templateUrl: './account-default-update.component.html',
    styleUrls: ['./account-default-update.component.css']
})
export class AccountDefaultUpdateComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _accountDefault: IAccountDefault;
    isSaving: boolean;
    types: IType[];
    listColumnsType: string[];
    listHeaderColumnsType: string[];
    accountDefaults: IAccountDefault[];
    contextMenu: ContextMenu;
    select: number;
    currentRow: number;
    typeID: number;
    typeName: string;
    childAccount: IAccountList[];
    listColumnsAccount: string[];
    listHeaderColumnsAccount: string[];
    modalRef: NgbModalRef;
    accountLists: IAccountList[];
    isCheck: boolean;
    listAccountTable: IAccountList[];
    searchAccountNumber: string;
    searchAccountName: string;
    isReadOnly: boolean;
    accountDefaultsCopy: IAccountDefault[];
    isHidePurchase: boolean;
    listTypeIDShowPurchase: number[] = [
        210,
        211,
        212,
        213,
        214,
        215,
        115,
        117,
        118,
        125,
        127,
        128,
        131,
        132,
        135,
        141,
        142,
        145,
        171,
        172,
        175,
        510,
        610
    ];

    ROLE_TaiKhoanNgamDinh_Xem = ROLE.TaiKhoanNgamDinh_Xem;
    ROLE_TaiKhoanNgamDinh_Sua = ROLE.TaiKhoanNgamDinh_Sua;

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
        private accountDefaultService: AccountDefaultService,
        private activatedRoute: ActivatedRoute,
        private typeService: TypeService,
        private accountListService: AccountListService,
        private modalService: NgbModal,
        private toastr: ToastrService,
        private translate: TranslateService,
        public utilsService: UtilsService,
        private router: Router,
        private principal: Principal
    ) {
        super();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.arrAuthorities = account.authorities;
            this.isEditUrl = window.location.href.includes('edit');
            this.isCreateUrl = window.location.href.includes('/new');
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_TaiKhoanNgamDinh_Sua)
                : true;
        });
    }

    ngOnInit() {
        this.isCreateUrl = false;
        this.isCreateUrl = window.location.href.includes('/new');
        this.isCheck = false;
        this.accountDefaults = [];
        this.accountDefaultsCopy = [];
        this.listAccountTable = [];
        this.childAccount = [];
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ accountDefault }) => {
            this.accountDefault = accountDefault;
            this.typeID = JSON.parse(sessionStorage.getItem('typeID'));
            this.accountDefaultService
                .getAccountDefaultByTypeID({ typeID: this.typeID })
                .subscribe((res: HttpResponse<IAccountDefault[]>) => {
                    this.accountDefaults = res.body;
                    for (let i = 0; i < this.accountDefaults.length; i++) {
                        if (this.accountDefaults[i].filterAccount) {
                            this.accountListService
                                .getAccountForAccountDefault({ listFilterAccount: this.accountDefaults[i].filterAccount })
                                .subscribe((res2: HttpResponse<IAccountList[]>) => {
                                    this.accountDefaults[i].childAccount = res2.body;
                                    this.copy();
                                });
                        } else {
                            this.accountDefaults[i].childAccount = [];
                        }
                    }
                });
        });
        this.accountListService.getAccountListsActive().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountLists = res.body;
            this.listAccountTable = res.body;
            // const findPPType = this.accountDefaults.filter(
            //     currentAccountDefault => currentAccountDefault.pPType !== this.accountDefaults[0].pPType
            // );
            if (this.listTypeIDShowPurchase.find(a => a === this.typeID)) {
                this.isHidePurchase = true;
            } else {
                this.isHidePurchase = false;
            }
        });
        this.listColumnsType = ['typeName'];
        this.listHeaderColumnsType = ['Loại chứng từ'];
        this.listColumnsAccount = ['accountNumber', 'accountName'];
        this.listHeaderColumnsAccount = ['Số tài khoản', 'Tên tài khoản'];
        this.typeService.getTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body;
            this.typeName = res.body.find(a => a.id === this.typeID).typeName;
        });
        // Load combobox cho tung row
    }

    closeForm() {
        if (this.accountDefaultsCopy) {
            if (!this.utilsService.isEquivalentArray(this.accountDefaults, this.accountDefaultsCopy)) {
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
        this.router.navigate(['/account-default']);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.TaiKhoanNgamDinh_Sua])
    save(isNew = false) {
        event.preventDefault();
        this.isSaving = true;
        this.subscribeToSaveResponse(this.accountDefaultService.save(this.accountDefaults));
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAccountDefault[]>>) {
        result.subscribe((res: HttpResponse<IAccountDefault[]>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.toastr.success(
            this.translate.instant('ebwebApp.cPProductQuantum.saveSuccess'),
            this.translate.instant('ebwebApp.cPProductQuantum.message')
        );
        this.copy();
        this.router.navigate(['/account-default']);
    }

    private onSaveError() {
        this.isSaving = false;
        this.toastr.error(
            this.translate.instant('ebwebApp.cPProductQuantum.error'),
            this.translate.instant('ebwebApp.cPProductQuantum.message')
        );
    }

    get accountDefault() {
        return this._accountDefault;
    }

    set accountDefault(accountDefault: IAccountDefault) {
        this._accountDefault = accountDefault;
    }

    newArr(lenght: number): any[] {
        if (lenght > 0) {
            return new Array(lenght);
        } else {
            return new Array(0);
        }
    }

    showAccountTable(accountTable, index) {
        this.modalRef = this.modalService.open(accountTable, {
            size: 'lg',
            windowClass: 'width-80',
            backdrop: 'static'
        });
        for (let i = 0; i < this.accountLists.length; i++) {
            this.accountLists[i].isCheck = false;
        }
        this.searchAccountNumber = '';
        this.searchAccountName = '';
        this.currentRow = index;
        const currentFilterAccount = this.accountDefaults[index].filterAccount;
        if (currentFilterAccount) {
            const acc = currentFilterAccount.split(';');
            for (let j = 0; j < this.accountLists.length; j++) {
                for (let i = 0; i < acc.length; i++) {
                    if (acc[i] === this.accountLists[j].accountNumber) {
                        // acc.splice(i, 1);
                        this.accountLists[j].isCheck = true;
                    }
                }
            }
        }
    }

    close() {
        this.modalRef.close();
        this.copy();
        this.router.navigate(['/account-default']);
    }

    closeAccountTable() {
        this.modalRef.close();
        this.listAccountTable = this.accountLists;
        this.copy();
    }

    checkAll() {
        for (let i = 0; i < this.accountLists.length; i++) {
            this.accountLists[i].isCheck = this.isCheck;
        }
    }

    saveFilterAccount() {
        const listActive = [];
        let filterAccount = '';
        for (let i = 0; i < this.accountLists.length; i++) {
            if (this.accountLists[i].isCheck) {
                listActive.push(this.accountLists[i].accountNumber);
            }
        }
        for (let i = 0; i < listActive.length; i++) {
            if (i === 0) {
                filterAccount += listActive[i];
            } else {
                filterAccount += ';' + listActive[i];
            }
        }
        this.accountDefaults[this.currentRow].filterAccount = filterAccount;
        this.modalRef.close();
        this.selectChangeComboboxDefaultAccount();
    }

    selectChangeComboboxDefaultAccount() {
        if (this.accountDefaults[this.currentRow].filterAccount) {
            this.accountListService
                .getAccountForAccountDefault({ listFilterAccount: this.accountDefaults[this.currentRow].filterAccount })
                .subscribe((res: HttpResponse<IAccountList[]>) => {
                    this.accountDefaults[this.currentRow].childAccount = res.body;
                    this.accountDefaults[this.currentRow].defaultAccount = null;
                });
        } else {
            this.accountDefaults[this.currentRow].childAccount = [];
            this.accountDefaults[this.currentRow].defaultAccount = null;
        }
    }

    getPPType(i) {
        if (this.accountDefaults[i].pPType) {
            return this.translate.instant('ebwebApp.receiveBill.import');
        } else {
            return this.translate.instant('ebwebApp.receiveBill.domestic');
        }
    }

    changeSearchValue() {
        this.listAccountTable = this.accountLists.filter(
            n =>
                n.accountNumber.includes(this.searchAccountNumber) &&
                n.accountName.toUpperCase().includes(this.searchAccountName.toUpperCase())
        );
    }

    /*
    * hàm ss du lieu 2 object và 2 mảng object
    * return true: neu tat ca giong nhau
    * return false: neu 1 trong cac object ko giong nhau
    * */
    canDeactive(): boolean {
        return this.utilsService.isEquivalentArray(this.accountDefaults, this.accountDefaultsCopy);
    }

    copy() {
        this.accountDefaultsCopy = this.accountDefaults.map(object => ({ ...object }));
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
        this.subscribeToSaveResponse(this.accountDefaultService.save(this.accountDefaults));
    }

    exit() {
        if (this.modalRef) {
            this.modalRef.close();
            return;
        }
    }
}
