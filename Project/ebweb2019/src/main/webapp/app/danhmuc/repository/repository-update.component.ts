import { AfterViewInit, Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IRepository } from 'app/shared/model/repository.model';
import { RepositoryService } from './repository.service';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IUnit } from 'app/shared/model/unit.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { Principal } from 'app/core';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-repository-update',
    templateUrl: './repository-update.component.html',
    styleUrls: ['./repository-update.component.css']
})
export class RepositoryUpdateComponent extends BaseComponent implements OnInit, AfterViewInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _repository: IRepository;
    isSaving: boolean;
    isSaveAndCreate: boolean;
    isEditUrl: boolean;
    accountList: IAccountList[];
    listColumnsAccount: string[];
    listHeaderColumnsAccount: string[];
    repositories: IRepository[];
    modalRef: NgbModalRef;
    isClose: boolean;
    repositoryCopy: IRepository;
    organizationunits: IOrganizationUnit[];
    arrAuthorities: any[];
    isRoleSua: boolean;
    isRoleThem: boolean;
    currentAccount: any;
    isCreateUrl: boolean;
    accountslist: IAccountList[];
    ROLE_DanhMucKho_Them = ROLE.DanhMucKho_Them;
    ROLE_DanhMucKho_Sua = ROLE.DanhMucKho_Sua;
    ROLE_DanhMucKho_Xoa = ROLE.DanhMucKho_Xoa;
    ROLE_DanhMucKho_Xem = ROLE.DanhMucKho_Xem;
    constructor(
        private jhiAlertService: JhiAlertService,
        private repositoryService: RepositoryService,
        private organizationUnitService: OrganizationUnitService,
        private accountListService: AccountListService,
        private activatedRoute: ActivatedRoute,
        private toastr: ToastrService,
        private router: Router,
        private translate: TranslateService,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private principal: Principal
    ) {
        super();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.arrAuthorities = account.authorities;
            this.isEditUrl = window.location.href.includes('edit');
            this.isCreateUrl = window.location.href.includes('/new');
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_DanhMucKho_Sua) : true;
            this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_DanhMucKho_Them) : true;
        });
    }

    ngOnInit() {
        this.isClose = false;
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ repository }) => {
            this.repository = repository;
        });
        this.accountListService.getAccountActive1().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountslist = res.body;
        });
        this.listColumnsAccount = ['accountNumber', 'accountName'];
        this.listHeaderColumnsAccount = ['Số tài khoản', 'Tên tài khoản'];
        this.copy();
    }

    previousState() {
        this.router.navigate(['/repository']);
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }

    save() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = false;
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (!this.repository.repositoryCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.repository.missRepositoryCode'),
                this.translate.instant('ebwebApp.repository.notification')
            );
            return;
        }
        if (!this.repository.repositoryName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.repository.missRepositoryName'),
                this.translate.instant('ebwebApp.repository.notification')
            );
            return;
        }
        if (this.repository.id !== undefined) {
            this.subscribeToSaveResponse(this.repositoryService.update(this.repository));
        } else {
            this.repository.isActive = true;
            this.subscribeToSaveResponse(this.repositoryService.create(this.repository));
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
        this.copy();
        if (this.isSaveAndCreate) {
            this.repository = {};
        } else {
            this.isSaving = false;
            this.previousState();
        }
        if (this.isEditUrl) {
            this.toastr.success(
                this.translate.instant('ebwebApp.repository.insertDataSuccess'),
                this.translate.instant('ebwebApp.repository.notification')
            );
        } else {
            this.toastr.success(
                this.translate.instant('ebwebApp.repository.insertDataSuccess'),
                this.translate.instant('ebwebApp.repository.notification')
            );
        }
    }

    private onSaveError() {
        this.isSaving = false;
        if (this.isEditUrl) {
            this.toastr.error(
                this.translate.instant('ebwebApp.repository.editDataFail'),
                this.translate.instant('ebwebApp.repository.notification')
            );
        } else {
            this.toastr.error(
                this.translate.instant('ebwebApp.repository.insertDataFail'),
                this.translate.instant('ebwebApp.repository.notification')
            );
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackOrganizationUnitById(index: number, item: IOrganizationUnit) {
        return item.id;
    }
    trackAccountByAccountNumber(index: number, item: IAccountList) {
        return item.id;
    }
    get repository() {
        return this._repository;
    }

    set repository(repository: IRepository) {
        this._repository = repository;
    }
    clearValue() {
        this.repository.repositoryCode = null;
        this.repository.repositoryName = null;
        this.repository.description = null;
        this.repository.isActive = false;
    }
    saveAndNew() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = true;
        if (!this.repository.repositoryCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.repository.missRepositoryCode'),
                this.translate.instant('ebwebApp.repository.notification')
            );
            return;
        }
        if (!this.repository.repositoryName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.repository.missRepositoryName'),
                this.translate.instant('ebwebApp.repository.notification')
            );
            return;
        }
        if (this.repository.id !== undefined) {
            this.subscribeToSaveResponse(this.repositoryService.update(this.repository));
        } else {
            this.repository.isActive = true;
            this.subscribeToSaveResponse(this.repositoryService.create(this.repository));
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucKho_Xoa])
    delete() {
        if (this.repository.id) {
            this.router.navigate(['/repository', { outlets: { popup: this.repository.id + '/delete' } }]);
        }
    }

    close() {
        this.copy();
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.closeAll();
    }

    exit() {
        if (this.modalRef) {
            this.modalRef.close();
            return;
        }
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    closeEdit(content) {
        if (!this.utilsService.isEquivalent(this.repository, this.repositoryCopy)) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else {
            this.close();
        }
    }

    copy() {
        this.repositoryCopy = Object.assign({}, this.repository);
    }
    canDeactive(): boolean {
        if (!this.isClose) {
            return this.utilsService.isEquivalent(this.repository, this.repositoryCopy);
        }
        return true;
    }

    closeForm() {
        event.preventDefault();
        if (this.repositoryCopy) {
            if (!this.utilsService.isEquivalent(this.repository, this.repositoryCopy)) {
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
        this.router.navigate(['/repository']);
    }

    saveContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.save();
    }
}
