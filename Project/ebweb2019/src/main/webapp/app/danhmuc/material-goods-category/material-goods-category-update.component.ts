import { AfterViewInit, Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { MaterialGoodsCategoryService } from './material-goods-category.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IUnit } from 'app/shared/model/unit.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IAccountList } from 'app/shared/model/account-list.model';
import { ROLE } from 'app/role.constants';
import { Principal } from 'app/core';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-material-goods-category-update',
    templateUrl: './material-goods-category-update.component.html',
    styleUrls: ['./material-goods-category-update.component.css']
})
export class MaterialGoodsCategoryUpdateComponent extends BaseComponent implements OnInit, AfterViewInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _materialGoodsCategory: IMaterialGoodsCategory;
    isSaving: boolean;
    isSaveAndCreate: boolean;
    isEditUrl: boolean;
    materialGoodsCategories: IMaterialGoodsCategory[];
    modalRef: NgbModalRef;
    isClose: boolean;
    materialGoodsCategoryCopy: IMaterialGoodsCategory;
    arrAuthorities: any[];
    isRoleSua: boolean;
    isRoleThem: boolean;
    currentAccount: any;
    isCreateUrl: boolean;
    ROLE_DanhMucLoaiVatTuHangHoa_Them = ROLE.DanhMucLoaiVatTuHangHoa_Them;
    ROLE_DanhMucLoaiVatTuHangHoa_Sua = ROLE.DanhMucLoaiVatTuHangHoa_Sua;
    ROLE_DanhMucLoaiVatTuHangHoa_Xoa = ROLE.DanhMucLoaiVatTuHangHoa_Xoa;
    ROLE_DanhMucLoaiVatTuHangHoa_Xem = ROLE.DanhMucLoaiVatTuHangHoa_Xem;

    constructor(
        private materialGoodsCategoryService: MaterialGoodsCategoryService,
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
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_DanhMucLoaiVatTuHangHoa_Sua)
                : true;
            this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_DanhMucLoaiVatTuHangHoa_Them)
                : true;
        });
    }

    ngOnInit() {
        this.isClose = false;
        this.isSaveAndCreate = false;
        this.activatedRoute.data.subscribe(({ materialGoodsCategory }) => {
            if (materialGoodsCategory === undefined) {
                materialGoodsCategory = {};
            }
            this.materialGoodsCategory = materialGoodsCategory;
        });
        this.copy();
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
    }

    previousState() {
        this.router.navigate(['material-goods-category']);
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
            this.materialGoodsCategory = {};
        } else {
            this.isSaving = false;
            this.previousState();
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

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucLoaiVatTuHangHoa_Xoa])
    delete() {
        event.preventDefault();
        this.isClose = true;
        if (this.materialGoodsCategory.id) {
            this.router.navigate(['/material-goods-category', { outlets: { popup: this.materialGoodsCategory.id + '/delete' } }]);
        }
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isClose = true;
        this.router.navigate(['material-goods-category']);
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
        this.materialGoodsCategoryCopy = Object.assign({}, this.materialGoodsCategory);
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
        this.isClose = true;
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.router.navigate(['/material-goods-category']);
    }
}
