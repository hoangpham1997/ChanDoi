import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IType } from 'app/shared/model/type.model';
import { TypeService } from 'app/entities/type';
import { IAccountList } from 'app/shared/model/account-list.model';
import { AccountListService } from 'app/danhmuc/account-list';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { AutoPrincipleService } from 'app/danhmuc/auto-principle';
import { CategoryName } from 'app/app.constants';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'eb-auto-principle-update',
    templateUrl: './auto-principle-update.component.html',
    styleUrls: ['./auto-principle-update.component.css']
})
export class AutoPrincipleComboboxComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _autoPrinciple: IAutoPrinciple;
    isSaving: boolean;
    types: IType[];
    accountLists: IAccountList[];
    listColumnsType: any;
    listHeaderColumnsType: any;
    listColumnsAccount: any;
    listHeaderColumnsAccount: any;
    isSaveAndCreate: boolean;
    isEditUrl: boolean;
    autoPrincipleCopy: IAutoPrinciple;
    modalRef: NgbModalRef;
    isClose: boolean;
    ROLE_DinhKhoanTuDong_Them = ROLE.DanhMucDinhKhoanTuDong_Them;
    ROLE_DinhKhoanTuDong_Sua = ROLE.DanhMucDinhKhoanTuDong_Sua;
    ROLE_DinhKhoanTuDong_Xoa = ROLE.DanhMucDinhKhoanTuDong_Xoa;
    data: IAutoPrinciple;

    constructor(
        private autoPrincipleService: AutoPrincipleService,
        private activatedRoute: ActivatedRoute,
        private typeService: TypeService,
        private router: Router,
        private accountListService: AccountListService,
        private toastr: ToastrService,
        private translate: TranslateService,
        public utilsService: UtilsService,
        private eventManager: JhiEventManager,
        private modalService: NgbModal
    ) {
        super();
    }

    ngOnInit() {
        this.types = [];
        this.isSaving = false;
        this.isSaveAndCreate = false;
        this.autoPrinciple = this.data ? this.data : {};
        this.reloadAutoPrinciple();
        this.copy();
        this.typeService.getAllTypes().subscribe((res: HttpResponse<IType[]>) => {
            this.types = res.body;
        });
        this.accountListService.getAccountListsActive().subscribe((res: HttpResponse<IAccountList[]>) => {
            this.accountLists = res.body;
        });

        this.listColumnsType = ['typeName'];
        this.listHeaderColumnsType = ['Loại chứng từ '];
        this.listColumnsAccount = ['accountNumber', 'accountName'];
        this.listHeaderColumnsAccount = ['Số tài khoản', 'Tên tài khoản'];
    }

    previousState() {
        this.router.navigate(['/auto-principle']);
    }

    copy() {
        this.autoPrincipleCopy = Object.assign({}, this.autoPrinciple);
    }

    closeEdit(content) {
        if (!this.utilsService.isEquivalent(this.autoPrinciple, this.autoPrincipleCopy)) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else {
            this.close();
        }
    }

    reloadAutoPrinciple() {
        this.autoPrinciple = {};
    }

    closeForm() {
        event.preventDefault();
        if (this.autoPrincipleCopy) {
            if (!this.utilsService.isEquivalent(this.autoPrinciple, this.autoPrincipleCopy)) {
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
        this.router.navigate(['/auto-principle']);
    }

    save() {
        event.preventDefault();
        this.isSaveAndCreate = true;
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isSaving = true;
        this.autoPrinciple.accountingType = 0;
        if (!this.autoPrinciple.typeId) {
            this.toastr.error(
                this.translate.instant('ebwebApp.autoPrinciple.typeIdNotNull'),
                this.translate.instant('ebwebApp.autoPrinciple.message')
            );
            return false;
        }
        if (!this.autoPrinciple.autoPrincipleName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.autoPrinciple.autoPrincipleNameNotNull'),
                this.translate.instant('ebwebApp.autoPrinciple.message')
            );
            return false;
        }
        if (this.autoPrinciple.id !== undefined) {
            this.subscribeToSaveResponse(this.autoPrincipleService.update(this.autoPrinciple));
        } else {
            this.autoPrinciple.isActive = true;
            this.subscribeToSaveResponse(this.autoPrincipleService.create(this.autoPrinciple));
        }
        this.close();
    }

    saveAndNew() {
        event.preventDefault();
        this.isSaveAndCreate = true;
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isSaving = true;
        this.autoPrinciple.accountingType = 0;
        if (!this.autoPrinciple.typeId) {
            this.toastr.error(
                this.translate.instant('ebwebApp.autoPrinciple.typeIdNotNull'),
                this.translate.instant('ebwebApp.autoPrinciple.message')
            );
            return false;
        }
        if (!this.autoPrinciple.autoPrincipleName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.autoPrinciple.autoPrincipleNameNotNull'),
                this.translate.instant('ebwebApp.autoPrinciple.message')
            );
            return false;
        }
        if (this.autoPrinciple.id !== undefined) {
            this.subscribeToSaveResponse(this.autoPrincipleService.update(this.autoPrinciple));
        } else {
            this.autoPrinciple.isActive = true;
            this.subscribeToSaveResponse(this.autoPrincipleService.create(this.autoPrinciple));
        }
    }

    private subscribeToSaveResponseAndContinute(result: Observable<HttpResponse<IAutoPrinciple>>) {
        result.subscribe(
            (res: HttpResponse<IAutoPrinciple>) => {
                this.onSaveSuccess();
                this.router.navigate(['/auto-principles', res.body.id, 'edit']).then(() => {
                    this.router.navigate(['/auto-principle', 'new']);
                });
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAutoPrinciple>>) {
        result.subscribe((res: HttpResponse<IAutoPrinciple>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.toastr.success(
            this.translate.instant('ebwebApp.autoPrinciple.insertDataSuccess'),
            this.translate.instant('ebwebApp.autoPrinciple.message')
        );
        if (!this.isSaveAndCreate) {
            this.eventManager.broadcast({
                name: 'saveSuccess',
                content: { name: CategoryName.DINH_KHOAN_TU_DONG, data: this.data }
            });
            this.close();
        } else {
            this.eventManager.broadcast({
                name: 'saveAndNewSuccess',
                content: { name: CategoryName.DINH_KHOAN_TU_DONG, data: this.data }
            });
            this.reloadAutoPrinciple();
        }
    }

    private onSaveError() {
        this.toastr.error(
            this.translate.instant('ebwebApp.autoPrinciple.insertDataFailed'),
            this.translate.instant('ebwebApp.autoPrinciple.message')
        );
        this.isSaving = false;
    }

    get autoPrinciple() {
        return this._autoPrinciple;
    }

    set autoPrinciple(autoPrinciple: IAutoPrinciple) {
        this._autoPrinciple = autoPrinciple;
    }

    delete() {
        this.router.navigate(['/auto-principle', { outlets: { popup: this.autoPrinciple.id + '/delete' } }]);
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.eventManager.broadcast({
            name: 'closePopup',
            content: null
        });
        this.isClose = true;
        this.router.navigate(['auto-principle']);
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    canDeactive(): boolean {
        if (!this.isClose) {
            return this.utilsService.isEquivalent(this.autoPrinciple, this.autoPrincipleCopy);
        }
        return true;
    }
}
