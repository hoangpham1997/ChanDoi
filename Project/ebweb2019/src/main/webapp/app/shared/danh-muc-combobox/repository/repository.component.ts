import { AfterViewInit, Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IRepository } from 'app/shared/model/repository.model';
import { RepositoryService } from 'app/danhmuc/repository';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from 'app/danhmuc/organization-unit';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IUnit } from 'app/shared/model/unit.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { CategoryName } from 'app/app.constants';

@Component({
    selector: 'eb-repository-cobobox',
    templateUrl: './repository.component.html',
    styleUrls: ['./repository.component.css']
})
export class RepositoryComboboxComponent extends BaseComponent implements OnInit, AfterViewInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _repository: IRepository;
    data: IRepository;
    isSaving: boolean;
    isSaveAndCreate: boolean;
    isEditUrl: boolean;
    accountList: IAccountList[];
    listColumnsAccount: string[];
    listHeaderColumnsAccount: string[];
    repositories: IRepository[];
    modalRef: NgbModalRef;
    isClose: boolean;
    saveSuccess: boolean;
    isCheck: boolean;
    repositoryCopy: IRepository;
    organizationunits: IOrganizationUnit[];
    accountslist: IAccountList[];
    ROLE_DanhMucKho_Them = ROLE.DanhMucKho_Them;
    ROLE_DanhMucKho_Sua = ROLE.DanhMucKho_Sua;

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
        private eventManager: JhiEventManager,
        private activeModal: NgbActiveModal
    ) {
        super();
    }

    ngOnInit() {
        this.isClose = false;
        this.isSaving = false;
        this.repository = {};
        this.organizationUnitService.query().subscribe(
            (res: HttpResponse<IOrganizationUnit[]>) => {
                this.organizationunits = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.accountListService.getAccountActive1().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountslist = res.body;
        });
        this.repositoryService.getRepositoryCombobox().subscribe(res => {
            this.repositories = res.body;
        });
        this.listColumnsAccount = ['accountNumber', 'accountName'];
        this.listHeaderColumnsAccount = ['Số tài khoản', 'Tên tài khoản'];
        this.copy();
    }

    previousState() {
        window.history.back();
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }

    save() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = false;
        this.isCheck = false;
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
                    this.data = res.body.repository;
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
        if (this.isSaveAndCreate) {
            this.eventManager.broadcast({
                name: 'saveAndNewSuccess',
                content: { name: CategoryName.KHO, data: this.data }
            });
            this.repository = {};
        } else {
            this.isSaving = false;
            this.eventManager.broadcast({
                name: 'saveSuccess',
                content: { name: CategoryName.KHO, data: this.data }
            });
            this.close();
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

    private onSaveError(err) {
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
        this.isCheck = true;
        if (!this.repository.repositoryCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.repository.missRepositoryCode'),
                this.translate.instant('ebwebApp.repository.notification')
            );
            return;
        }
        if (!this.repository.id) {
            const count = this.repositories.filter(x => x.repositoryCode === this.repository.repositoryCode);
            if (count && count.length > 0) {
                this.toastr.error(this.translate.instant('ebwebApp.repository.existData'));
                return;
            }
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

    close() {
        this.eventManager.broadcast({
            name: 'closePopup',
            content: null
        });
        this.eventManager.broadcast({
            name: 'closePopupMaterialGood',
            content: false
        });
        this.activeModal.dismiss(false);
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

    closeAll() {
        this.router.navigate(['/repository']);
    }
}
