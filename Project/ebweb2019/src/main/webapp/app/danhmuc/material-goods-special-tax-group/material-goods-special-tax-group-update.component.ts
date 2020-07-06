import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IMaterialGoodsSpecialTaxGroup, MaterialGoodsSpecialTaxGroup } from 'app/shared/model/material-goods-special-tax-group.model';
import { MaterialGoodsSpecialTaxGroupService } from './material-goods-special-tax-group.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { Principal } from 'app/core';
import { IAccountList } from 'app/shared/model/account-list.model';
import { ExpenseItem } from 'app/shared/model/expense-item.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-material-goods-special-tax-group-update',
    templateUrl: './material-goods-special-tax-group-update.component.html',
    styleUrls: ['./material-goods-special-tax-group-update.component.css']
})
export class MaterialGoodsSpecialTaxGroupUpdateComponent extends BaseComponent implements OnInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _materialGoodsSpecialTaxGroup: IMaterialGoodsSpecialTaxGroup;
    isSaving: boolean;
    modalRef: NgbModalRef;
    isClose: boolean;
    isReadOnly: boolean;
    materialgoodsspecialtaxgroups: IMaterialGoodsSpecialTaxGroup[];
    materialGoodsSpecialTaxGroupCopy: IMaterialGoodsSpecialTaxGroup;
    units: IUnit[];
    listColumnsUnitId: any;
    listHeaderColumnsUnitId: any;
    listColumnsMaterialGoodsSpecialTaxGroups: any;
    listHeaderColumnsMaterialGoodsSpecialTaxGroups: any;
    isSaveAndCreate: boolean;
    ROLE_HHDVChiuThue_Them = ROLE.DanhMucHHDVChiuThueTTDB_Them;
    ROLE_HHDVChiuThue_Sua = ROLE.DanhMucHHDVChiuThueTTDB_Sua;
    ROLE_HHDVChiuThue_Xoa = ROLE.DanhMucHHDVChiuThueTTDB_Xoa;
    ROLE_HHDVChiuThue_Xem = ROLE.DanhMucDinhKhoanTuDong_Xem;
    isClosing: boolean;
    isEditUrl: boolean;
    currentAccount: any;
    arrAuthorities: any[];
    isRoleSua: boolean;
    isRoleThem: boolean;
    isCreateUrl: boolean;

    constructor(
        private jhiAlertService: JhiAlertService,
        private materialGoodsSpecialTaxGroupService: MaterialGoodsSpecialTaxGroupService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private toastrService: ToastrService,
        private translateService: TranslateService,
        public utilsService: UtilsService,
        private unitService: UnitService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private modalService: NgbModal,
        private principal: Principal,
        private toasService: ToastrService,
        private activeRoute: ActivatedRoute
    ) {
        super();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.arrAuthorities = account.authorities;
            this.isEditUrl = window.location.href.includes('edit');
            this.isCreateUrl = window.location.href.includes('/new');
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_HHDVChiuThue_Sua) : true;
            this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_HHDVChiuThue_Them)
                : true;
        });
    }

    ngOnInit() {
        console.log('NgOnInit');
        this.isClose = false;
        this.isSaveAndCreate = false;
        this.units = [];
        this.materialgoodsspecialtaxgroups = [{}];
        // this.materialGoodsSpecialTaxGroupService
        //     .getMaterialGoodsSpecialTaxGroupByActiveExceptID({ id: this.materialGoodsSpecialTaxGroup.id })
        //     .subscribe((res: HttpResponse<IMaterialGoodsSpecialTaxGroup[]>) => {
        //         this.materialgoodsspecialtaxgroups = res.body;
        //     });
        // this.materialGoodsSpecialTaxGroupService
        //     .getOneMaterialGoodsSpecialTaxGroupByActiveExceptID({ id: this.materialGoodsSpecialTaxGroup.id })
        //     .subscribe((res: HttpResponse<IMaterialGoodsSpecialTaxGroup>) => {
        //         this.materialGoodsSpecialTaxGroup = res.body;
        //     });
        this.listColumnsUnitId = ['unitName'];
        this.listHeaderColumnsUnitId = ['Đơn vị tính'];
        this.unitService.getUnits().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
        });
        this.listColumnsMaterialGoodsSpecialTaxGroups = ['materialGoodsSpecialTaxGroupCode', 'materialGoodsSpecialTaxGroupName'];
        this.listHeaderColumnsMaterialGoodsSpecialTaxGroups = ['Tên nhóm HHDV chịu thuế TTĐB', 'Mã nhóm HHDV chịu thuế TTĐB'];
        this.activatedRoute.data.subscribe(({ materialGoodsSpecialTaxGroup }) => {
            this.materialGoodsSpecialTaxGroup = materialGoodsSpecialTaxGroup;
            if (this.materialGoodsSpecialTaxGroup.id === undefined || this.materialGoodsSpecialTaxGroup.id === null) {
                this.materialGoodsSpecialTaxGroupService
                    .getActiveMaterialGoodsSpecialTaxGroup()
                    .subscribe((res: HttpResponse<IMaterialGoodsSpecialTaxGroup[]>) => {
                        this.materialgoodsspecialtaxgroups = res.body;
                    });
            } else {
                this.materialGoodsSpecialTaxGroupService
                    .getMaterialGoodsSpecialTaxGroupsOne(this.materialGoodsSpecialTaxGroup.id)
                    .subscribe((res: HttpResponse<IMaterialGoodsSpecialTaxGroup[]>) => {
                        this.materialgoodsspecialtaxgroups = res.body;
                    });
            }
        });
        // this.materialGoodsSpecialTaxGroupService
        //     .getMaterialGoodsSpecialTaxGroups()
        //     .subscribe((res: HttpResponse<IMaterialGoodsSpecialTaxGroup[]>) => {
        //         this.materialgoodsspecialtaxgroups = res.body;
        //     });
        this.copy();
    }

    previousState() {
        this.router.navigate(['material-goods-special-tax-group']);
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucHHDVChiuThueTTDB_Them, ROLE.DanhMucHHDVChiuThueTTDB_Sua])
    save() {
        event.preventDefault();
        this.isSaving = true;
        if (this.modalRef) {
            this.modalRef.close();
        }
        if (this.materialGoodsSpecialTaxGroup.isSecurity === null || this.materialGoodsSpecialTaxGroup.isSecurity === undefined) {
            this.materialGoodsSpecialTaxGroup.isSecurity = false;
        }
        this.isSaveAndCreate = false;
        if (!this.materialGoodsSpecialTaxGroup.materialGoodsSpecialTaxGroupCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.materialGoodsSpecialTaxGroup.materialGoodsSpecialTaxGroupCodeNotNull'),
                this.translate.instant('ebwebApp.materialGoodsSpecialTaxGroup.message')
            );
            return false;
        }
        if (!this.materialGoodsSpecialTaxGroup.materialGoodsSpecialTaxGroupName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.materialGoodsSpecialTaxGroup.materialGoodsSpecialTaxGroupNameNotNull'),
                this.translate.instant('ebwebApp.materialGoodsSpecialTaxGroup.message')
            );
            return false;
        }
        // if (this.materialGoodsSpecialTaxGroup.taxRate > 100) {
        //     this.toasService.warning(
        //         this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'),
        //         this.translateService.instant('ebwebApp.mCReceipt.home.message')
        //     );
        //     return false;
        // }
        if (this.materialGoodsSpecialTaxGroup.id !== undefined) {
            this.subscribeToSaveResponse(this.materialGoodsSpecialTaxGroupService.update(this.materialGoodsSpecialTaxGroup));
        } else {
            this.materialGoodsSpecialTaxGroup.isActive = true;
            this.subscribeToSaveResponse(this.materialGoodsSpecialTaxGroupService.create(this.materialGoodsSpecialTaxGroup));
        }
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucHHDVChiuThueTTDB_Them])
    saveAndNew() {
        event.preventDefault();
        this.isSaveAndCreate = true;
        this.isSaving = true;
        if (this.materialGoodsSpecialTaxGroup.isSecurity === null || this.materialGoodsSpecialTaxGroup.isSecurity === undefined) {
            this.materialGoodsSpecialTaxGroup.isSecurity = false;
        }
        if (!this.materialGoodsSpecialTaxGroup.materialGoodsSpecialTaxGroupCode) {
            this.toastr.error(
                this.translate.instant('ebwebApp.materialGoodsSpecialTaxGroup.materialGoodsSpecialTaxGroupCodeNotNull'),
                this.translate.instant('ebwebApp.materialGoodsSpecialTaxGroup.message')
            );
            return false;
        }
        if (!this.materialGoodsSpecialTaxGroup.materialGoodsSpecialTaxGroupName) {
            this.toastr.error(
                this.translate.instant('ebwebApp.materialGoodsSpecialTaxGroup.materialGoodsSpecialTaxGroupNameNotNull'),
                this.translate.instant('ebwebApp.materialGoodsSpecialTaxGroup.message')
            );
            return false;
        }
        // if (this.materialGoodsSpecialTaxGroup.taxRate > 100) {
        //     this.toasService.warning(
        //         this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'),
        //         this.translateService.instant('ebwebApp.mCReceipt.home.message')
        //     );
        //     return false;
        // }
        if (this.materialGoodsSpecialTaxGroup.id !== undefined) {
            this.subscribeToSaveResponse(this.materialGoodsSpecialTaxGroupService.update(this.materialGoodsSpecialTaxGroup));
        } else {
            this.materialGoodsSpecialTaxGroup.isActive = true;
            this.subscribeToSaveResponse(this.materialGoodsSpecialTaxGroupService.create(this.materialGoodsSpecialTaxGroup));
        }
    }

    copy() {
        this.materialGoodsSpecialTaxGroupCopy = Object.assign({}, this.materialGoodsSpecialTaxGroup);
    }

    checkIsChiDent(materialGoodsSpecialTaxGroup: MaterialGoodsSpecialTaxGroup) {
        if (materialGoodsSpecialTaxGroup.parentID === null) {
            return false;
        }
        if (materialGoodsSpecialTaxGroup.parentID === this.materialGoodsSpecialTaxGroup.id) {
            return true;
        }
        const expense = this.expenseItems.filter(a => a.id === materialGoodsSpecialTaxGroup.parentID)[0];
        return this.checkIsChiDent(expense);
    }

    closeForm() {
        event.preventDefault();
        if (this.materialGoodsSpecialTaxGroupCopy) {
            if (!this.utilsService.isEquivalent(this.materialGoodsSpecialTaxGroup, this.materialGoodsSpecialTaxGroupCopy)) {
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
        this.router.navigate(['/material-goods-special-tax-group']);
    }

    closeEdit(content) {
        if (!this.utilsService.isEquivalent(this.materialGoodsSpecialTaxGroup, this.materialGoodsSpecialTaxGroupCopy)) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else {
            this.close();
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                this.onSaveSuccess();
            },
            (res: HttpErrorResponse) => this.handleError(res)
        );
    }

    changeDiscountRate() {
        if (this.materialGoodsSpecialTaxGroup.taxRate > 100) {
            this.toasService.warning(
                this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.discountRateBigger'),
                this.translateService.instant('ebwebApp.mCReceipt.home.message')
            );
        }
    }

    canDeactive(): boolean {
        if (!this.isClose) {
            return this.utilsService.isEquivalent(this.materialGoodsSpecialTaxGroup, this.materialGoodsSpecialTaxGroupCopy);
        }
        return true;
    }

    private onSaveSuccess() {
        this.isClose = true;
        if (this.isSaveAndCreate) {
            this.materialGoodsSpecialTaxGroup = {};
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

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackMaterialGoodsSpecialTaxGroupById(index: number, item: IMaterialGoodsSpecialTaxGroup) {
        return item.id;
    }

    get materialGoodsSpecialTaxGroup() {
        return this._materialGoodsSpecialTaxGroup;
    }

    set materialGoodsSpecialTaxGroup(materialGoodsSpecialTaxGroup: IMaterialGoodsSpecialTaxGroup) {
        this._materialGoodsSpecialTaxGroup = materialGoodsSpecialTaxGroup;
    }

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucHHDVChiuThueTTDB_Xoa])
    delete() {
        event.preventDefault();
        if (this.materialGoodsSpecialTaxGroup.id) {
            this.router.navigate([
                '/material-goods-special-tax-group',
                { outlets: { popup: this.materialGoodsSpecialTaxGroup.id + '/delete' } }
            ]);
            this.copy();
        }
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isClose = true;
        this.router.navigate(['material-goods-special-tax-group']);
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    changeParent() {
        if (!this.materialGoodsSpecialTaxGroup.parent) {
            this.materialGoodsSpecialTaxGroup.grade = 1;
        } else {
            this.materialGoodsSpecialTaxGroup.grade = this.materialGoodsSpecialTaxGroup.parent.grade + 1;
            this.materialGoodsSpecialTaxGroup.parentID = this.materialGoodsSpecialTaxGroup.parent.id;
        }
    }

    handleError(err) {
        this.isSaving = false;
        this.isClosing = false;
        if (err && err.error) {
            this.toastrService.error(
                this.translateService.instant(`ebwebApp.materialGoodsSpecialTaxGroup.${err.error.message}`, { title: err.error.title })
            );
        }
    }
}
