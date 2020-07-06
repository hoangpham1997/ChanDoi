import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IUnit } from 'app/shared/model/unit.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IAccountList } from 'app/shared/model/account-list.model';
import { ROLE } from 'app/role.constants';
import { MaterialGoodsCategoryService } from 'app/danhmuc/material-goods-category';
import { CategoryName } from 'app/app.constants';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'eb-material-goods-category-update',
    templateUrl: './material-goods-category-update.component.html',
    styleUrls: ['./material-goods-category-update.component.css']
})
export class MaterialGoodsCategoryComboboxComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _materialGoodsCategory: IMaterialGoodsCategory;
    isSaving: boolean;
    isSaveAndCreate: boolean;
    isEditUrl: boolean;
    materialGoodsCategories: IMaterialGoodsCategory[];
    modalRef: NgbModalRef;
    isClose: boolean;
    materialGoodsCategoryCopy: IMaterialGoodsCategory[];
    activeMaterialGoodsCategory: IMaterialGoodsCategory[];
    ROLE_DanhMucLoaiVatTuHangHoa_Them = ROLE.DanhMucLoaiVatTuHangHoa_Them;
    ROLE_DanhMucLoaiVatTuHangHoa_Sua = ROLE.DanhMucLoaiVatTuHangHoa_Sua;
    ROLE_DanhMucLoaiVatTuHangHoa_Xoa = ROLE.DanhMucLoaiVatTuHangHoa_Xoa;
    data: IMaterialGoodsCategory;

    constructor(
        private materialGoodsCategoryService: MaterialGoodsCategoryService,
        private activatedRoute: ActivatedRoute,
        private toastr: ToastrService,
        private router: Router,
        private translate: TranslateService,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
        super();
    }

    ngOnInit() {
        this.isClose = false;
        this.isSaveAndCreate = false;
        this.materialGoodsCategory = {};
        if (this.materialGoodsCategory.id && this.materialGoodsCategory.isParentNode) {
            this.materialGoodsCategoryService
                .getMaterialGoodsCategoriesActiveExceptID({ id: this.materialGoodsCategory.id })
                .subscribe((res: HttpResponse<IMaterialGoodsCategory[]>) => {
                    this.materialGoodsCategories = res.body;
                });
        } else {
            this.materialGoodsCategoryService.getMaterialGoodsCategory().subscribe((res: HttpResponse<IMaterialGoodsCategory[]>) => {
                this.materialGoodsCategories = res.body;
            });
        }
        this.copy();
    }

    previousState() {
        window.history.back();
    }

    save() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = false;
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (!this.materialGoodsCategory.materialGoodsCategoryCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.materialGoodsCategory.missMaterialGoodsCategoryCode'),
                this.translate.instant('ebwebApp.materialGoodsCategory.notification')
            );
            return;
        }
        if (!this.materialGoodsCategory.materialGoodsCategoryName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.materialGoodsCategory.missMaterialGoodsCategoryName'),
                this.translate.instant('ebwebApp.materialGoodsCategory.notification')
            );
            return;
        }
        if (this.materialGoodsCategory.id !== undefined) {
            this.subscribeToSaveResponse(this.materialGoodsCategoryService.update(this.materialGoodsCategory));
        } else {
            this.materialGoodsCategory.isActive = true;
            this.subscribeToSaveResponse(this.materialGoodsCategoryService.create(this.materialGoodsCategory));
        }
    }

    saveAndNew() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = true;
        if (!this.materialGoodsCategory.materialGoodsCategoryCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.materialGoodsCategory.missMaterialGoodsCategoryCode'),
                this.translate.instant('ebwebApp.materialGoodsCategory.notification')
            );
            return;
        }
        if (!this.materialGoodsCategory.materialGoodsCategoryName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.materialGoodsCategory.missMaterialGoodsCategoryName'),
                this.translate.instant('ebwebApp.materialGoodsCategory.notification')
            );
            return;
        }
        if (this.materialGoodsCategory.id !== undefined) {
            this.subscribeToSaveResponse(this.materialGoodsCategoryService.update(this.materialGoodsCategory));
        } else {
            this.materialGoodsCategory.isActive = true;
            this.subscribeToSaveResponse(this.materialGoodsCategoryService.create(this.materialGoodsCategory));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                this.data = res.body;
                this.onSaveSuccess();
            },
            (res: HttpErrorResponse) => {
                this.onSaveError(res);
            }
        );
    }

    private onSaveSuccess() {
        this.isClose = true;
        if (this.isSaveAndCreate) {
            this.eventManager.broadcast({
                name: 'saveAndNewSuccess',
                content: { name: CategoryName.LOAI_VAT_TU_HANG_HOA, data: this.data }
            });
            this.materialGoodsCategory = {};
        } else {
            this.isSaving = false;
            this.eventManager.broadcast({
                name: 'saveSuccess',
                content: { name: CategoryName.LOAI_VAT_TU_HANG_HOA, data: this.data }
            });
            this.close();
        }
        if (this.isEditUrl) {
            this.toastr.success(
                this.translate.instant('ebwebApp.materialGoodsCategory.insertDataSuccess'),
                this.translate.instant('ebwebApp.materialGoodsCategory.notification')
            );
        } else {
            this.toastr.success(
                this.translate.instant('ebwebApp.materialGoodsCategory.insertDataSuccess'),
                this.translate.instant('ebwebApp.materialGoodsCategory.notification')
            );
        }
    }

    private onSaveError(response) {
        this.isSaving = false;
        this.toastr.error(this.translate.instant(`ebwebApp.materialGoodsCategory.${response.error.errorKey}`));
    }

    get materialGoodsCategory() {
        return this._materialGoodsCategory;
    }

    set materialGoodsCategory(materialGoodsCategory: IMaterialGoodsCategory) {
        this._materialGoodsCategory = materialGoodsCategory;
    }

    delete() {
        event.preventDefault();
        this.isClose = true;
        if (this.materialGoodsCategory.id) {
            this.router.navigate(['/material-goods-category', { outlets: { popup: this.materialGoodsCategory.id + '/delete' } }]);
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
        if (!this.utilsService.isEquivalent(this.materialGoodsCategories, this.materialGoodsCategoryCopy)) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else {
            this.close();
        }
    }

    copy() {
        this.materialGoodsCategoryCopy = Object.assign({}, this.materialGoodsCategories);
    }

    canDeactive(): boolean {
        event.preventDefault();
        if (!this.isClose) {
            return this.utilsService.isEquivalent(this.materialGoodsCategories, this.materialGoodsCategoryCopy);
        }
        return true;
    }

    closeForm() {
        event.preventDefault();
        if (this.materialGoodsCategoryCopy) {
            if (!this.utilsService.isEquivalent(this.materialGoodsCategory, this.materialGoodsCategoryCopy)) {
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
        this.router.navigate(['/material-goods-category']);
    }
}
