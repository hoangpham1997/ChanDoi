import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';
import { AccountingObjectGroupService } from './accounting-object-group.service';
import { JhiAlertService } from 'ng-jhipster';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IRepository } from 'app/shared/model/repository.model';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { ROLE } from 'app/role.constants';
import { Principal } from 'app/core';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-accounting-object-group-update',
    templateUrl: './accounting-object-group-update.component.html',
    styleUrls: ['./accounting-object-group-update.component.css']
})
export class AccountingObjectGroupUpdateComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _accountingObjectGroup: IAccountingObjectGroup;
    isSaving: boolean;
    accountingObjectGroups: IAccountingObjectGroup[];
    modalRef: NgbModalRef;
    isClose: boolean;
    accountingObjectGroupCopy: IAccountingObjectGroup;
    isSaveAndCreate: boolean;
    isEditUrl: boolean;
    listAccountingObjectGroup: string[];
    listHeaderColumnsAccountingObjectGroup: string[];
    activeAccountingObjectGroup: IAccountingObjectGroup[];
    arrAuthorities: any[];
    isRoleSua: boolean;
    isRoleThem: boolean;
    currentAccount: any;
    isCreateUrl: boolean;
    ROLE_DanhMucKhachHangVaNhaCungCap_Them = ROLE.DanhMucKhachHangVaNhaCungCap_Them;
    ROLE_DanhMucKhachHangVaNhaCungCap_Sua = ROLE.DanhMucKhachHangVaNhaCungCap_Sua;
    ROLE_DanhMucKhachHangVaNhaCungCap_Xoa = ROLE.DanhMucKhachHangVaNhaCungCap_Xoa;
    ROLE_DanhMucKhachHangVaNhaCungCap_Xem = ROLE.DanhMucKhachHangVaNhaCungCap_Xem;

    constructor(
        private accountingObjectGroupService: AccountingObjectGroupService,
        private activatedRoute: ActivatedRoute,
        private jhiAlertService: JhiAlertService,
        private router: Router,
        private translate: TranslateService,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private toastr: ToastrService,
        private principal: Principal
    ) {
        super();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.arrAuthorities = account.authorities;
            this.isEditUrl = window.location.href.includes('edit');
            this.isCreateUrl = window.location.href.includes('/new');
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_DanhMucKhachHangVaNhaCungCap_Sua)
                : true;
            this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_DanhMucKhachHangVaNhaCungCap_Them)
                : true;
        });
    }

    ngOnInit() {
        this.isClose = false;
        this.isSaving = false;
        this.isSaveAndCreate = false;
        this.activatedRoute.data.subscribe(({ accountingObjectGroup }) => {
            this.accountingObjectGroup = accountingObjectGroup;
            if (this.accountingObjectGroup.id === undefined || this.accountingObjectGroup.id === null) {
                this.accountingObjectGroupService
                    .getLoadAllAccountingObjectGroup()
                    .subscribe((res: HttpResponse<IAccountingObjectGroup[]>) => {
                        this.accountingObjectGroups = res.body;
                    });
            } else {
                this.accountingObjectGroupService
                    .getCbxAccountingObjectGroup(this.accountingObjectGroup.id)
                    .subscribe((res: HttpResponse<IAccountingObjectGroup[]>) => {
                        this.accountingObjectGroups = res.body;
                    });
            }
        });
        this.listHeaderColumnsAccountingObjectGroup = ['Nhóm'];
        this.listAccountingObjectGroup = ['accountingObjectGroupName'];
        this.copy();
    }

    previousState() {
        this.router.navigate(['accounting-object-group']);
    }

    save() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = false;
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (!this.accountingObjectGroup.accountingObjectGroupCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObjectGroup.missAccountingObjectGroupCode'),
                this.translate.instant('ebwebApp.accountingObjectGroup.detail.notification')
            );
            return;
        }
        if (!this.accountingObjectGroup.accountingObjectGroupName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObjectGroup.missAccountingObjectGroupName'),
                this.translate.instant('ebwebApp.accountingObjectGroup.detail.notification')
            );
            return;
        }
        if (this.accountingObjectGroup.id !== undefined) {
            this.subscribeToSaveResponse(this.accountingObjectGroupService.update(this.accountingObjectGroup));
        } else {
            this.accountingObjectGroup.isActive = true;
            this.subscribeToSaveResponse(this.accountingObjectGroupService.create(this.accountingObjectGroup));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                this.onSaveSuccess();
            },
            (res: HttpErrorResponse) => {
                this.onSaveError(res);
            }
        );
    }

    private onSaveSuccess() {
        this.copy();
        if (this.isSaveAndCreate) {
            this.accountingObjectGroup = {};
        } else {
            this.isSaving = false;
            this.previousState();
        }
        if (this.isEditUrl) {
            this.toastr.success(
                this.translate.instant('ebwebApp.accountingObjectGroup.insertDataSuccess'),
                this.translate.instant('ebwebApp.accountingObjectGroup.detail.notification')
            );
        } else {
            this.toastr.success(
                this.translate.instant('ebwebApp.accountingObjectGroup.insertDataSuccess'),
                this.translate.instant('ebwebApp.accountingObjectGroup.detail.notification')
            );
        }
    }
    saveAndNew() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = true;
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (!this.accountingObjectGroup.accountingObjectGroupCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObjectGroup.missAccountingObjectGroupCode'),
                this.translate.instant('ebwebApp.accountingObjectGroup.detail.notification')
            );
            return;
        }
        if (!this.accountingObjectGroup.accountingObjectGroupName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.accountingObjectGroup.missAccountingObjectGroupName'),
                this.translate.instant('ebwebApp.accountingObjectGroup.detail.notification')
            );
            return;
        }
        if (this.accountingObjectGroup.id !== undefined) {
            this.subscribeToSaveResponse(this.accountingObjectGroupService.update(this.accountingObjectGroup));
        } else {
            this.accountingObjectGroup.isActive = true;
            this.subscribeToSaveResponse(this.accountingObjectGroupService.create(this.accountingObjectGroup));
        }
    }

    private onSaveError(response) {
        this.isSaving = false;
        this.toastr.error(this.translate.instant(`ebwebApp.accountingObjectGroup.${response.error.errorKey}`));
    }
    get accountingObjectGroup() {
        return this._accountingObjectGroup;
    }

    set accountingObjectGroup(accountingObjectGroup: IAccountingObjectGroup) {
        this._accountingObjectGroup = accountingObjectGroup;
    }
    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucKhachHangVaNhaCungCap_Xoa])
    delete() {
        event.preventDefault();
        if (this.accountingObjectGroup.id) {
            this.router.navigate(['/accounting-object-group', { outlets: { popup: this.accountingObjectGroup.id + '/delete' } }]);
        }
    }
    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isClose = true;
        this.router.navigate(['accounting-object-group']);
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    closeEdit(content) {
        if (!this.utilsService.isEquivalent(this.accountingObjectGroup, this.accountingObjectGroupCopy)) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else {
            this.close();
        }
    }

    copy() {
        this.accountingObjectGroupCopy = Object.assign({}, this.accountingObjectGroup);
    }
    canDeactive(): boolean {
        event.preventDefault();
        if (!this.isClose) {
            return this.utilsService.isEquivalent(this.accountingObjectGroup, this.accountingObjectGroupCopy);
        }
        return true;
    }
    closeForm() {
        event.preventDefault();
        if (this.accountingObjectGroupCopy) {
            if (!this.utilsService.isEquivalent(this.accountingObjectGroup, this.accountingObjectGroupCopy)) {
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
        this.router.navigate(['/accounting-object-group']);
    }
}
