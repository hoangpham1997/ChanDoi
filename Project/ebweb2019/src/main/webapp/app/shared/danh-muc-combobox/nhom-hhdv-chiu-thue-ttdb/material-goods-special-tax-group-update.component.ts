import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IMaterialGoodsSpecialTaxGroup } from 'app/shared/model/material-goods-special-tax-group.model';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { MaterialGoodsSpecialTaxGroupService } from 'app/danhmuc/material-goods-special-tax-group';
import { CategoryName } from 'app/app.constants';

@Component({
    selector: 'eb-material-goods-special-tax-group-update',
    templateUrl: './material-goods-special-tax-group-update.component.html',
    styleUrls: ['./material-goods-special-tax-group-update.component.css']
})
export class MaterialGoodsSpecialTaxGroupCoboboxComponent extends BaseComponent implements OnInit {
    private _materialGoodsSpecialTaxGroup: IMaterialGoodsSpecialTaxGroup;
    isSaving: boolean;
    modalRef: NgbModalRef;
    isClose: boolean;
    materialgoodsspecialtaxgroups: IMaterialGoodsSpecialTaxGroup[];
    materialGoodsSpecialTaxGroupCopy: IMaterialGoodsSpecialTaxGroup;
    units: IUnit[];
    listColumnsUnitId: any;
    listHeaderColumnsUnitId: any;
    listColumnsMaterialgoodsspecialtaxgroups: any;
    listHeaderColumnsMaterialgoodsspecialtaxgroups: any;
    isSaveAndCreate: boolean;
    ROLE_HHDVChiuThue_Them = ROLE.DanhMucHHDVChiuThueTTDB_Them;
    ROLE_HHDVChiuThue_Sua = ROLE.DanhMucHHDVChiuThueTTDB_Sua;
    ROLE_HHDVChiuThue_Xoa = ROLE.DanhMucHHDVChiuThueTTDB_Xoa;
    isClosing: boolean;
    data: IMaterialGoodsSpecialTaxGroup;

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
        private eventManager: JhiEventManager,
        private modalService: NgbModal
    ) {
        super();
    }

    ngOnInit() {
        this.isSaving = false;
        this.units = [];
        this.materialgoodsspecialtaxgroups = [];
        this.copy();
        this.reloadMaterialGoodsSpecialTaxGroup();
        // this.activatedRoute.data.subscribe(({ materialGoodsSpecialTaxGroup }) => {
        //     this.materialGoodsSpecialTaxGroup = materialGoodsSpecialTaxGroup;
        // });
        this.materialGoodsSpecialTaxGroup = this.data ? this.data : {};
        this.materialGoodsSpecialTaxGroupService
            .getMaterialGoodsSpecialTaxGroupByCompanyID()
            .subscribe((res: HttpResponse<IMaterialGoodsSpecialTaxGroup[]>) => {
                this.materialgoodsspecialtaxgroups = res.body;
                if (this.materialGoodsSpecialTaxGroup.id) {
                    this.materialGoodsSpecialTaxGroup.parent = this.materialgoodsspecialtaxgroups.find(
                        x => x.id === this.materialGoodsSpecialTaxGroup.parentID
                    );
                }
            });
        this.listColumnsUnitId = ['unitName'];
        this.listHeaderColumnsUnitId = ['Đơn vị tính'];
        this.unitService.getUnits().subscribe((res: HttpResponse<IUnit[]>) => {
            this.units = res.body;
        });
        this.listColumnsMaterialgoodsspecialtaxgroups = ['materialGoodsSpecialTaxGroupName'];
        this.listHeaderColumnsMaterialgoodsspecialtaxgroups = ['Tên nhóm HHDV chịu thuế TTBD'];
        this.materialGoodsSpecialTaxGroupService.getMaterialGoodsSpecialTaxGroup().subscribe(
            (res: HttpResponse<IMaterialGoodsSpecialTaxGroup[]>) => {
                this.materialgoodsspecialtaxgroups = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        if (!this.materialGoodsSpecialTaxGroup.id) {
            this.materialGoodsSpecialTaxGroup.grade = 1;
        }
    }

    previousState() {
        window.history.back();
    }

    reloadMaterialGoodsSpecialTaxGroup() {
        this.materialGoodsSpecialTaxGroup = {};
    }

    save() {
        event.preventDefault();
        this.isSaveAndCreate = true;
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isSaving = true;
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
        if (this.materialGoodsSpecialTaxGroup.id !== undefined) {
            this.subscribeToSaveResponse(this.materialGoodsSpecialTaxGroupService.update(this.materialGoodsSpecialTaxGroup));
        } else {
            this.materialGoodsSpecialTaxGroup.isActive = true;
            this.subscribeToSaveResponse(this.materialGoodsSpecialTaxGroupService.create(this.materialGoodsSpecialTaxGroup));
        }
        this.close();
    }

    saveAndCreate() {
        event.preventDefault();
        this.isSaveAndCreate = true;
        this.isSaving = true;
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

    closeEdit(content) {
        if (!this.utilsService.isEquivalent(this.materialGoodsSpecialTaxGroup, this.materialGoodsSpecialTaxGroupCopy)) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else {
            this.close();
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialGoodsSpecialTaxGroup>>) {
        result.subscribe(
            (res: HttpResponse<IMaterialGoodsSpecialTaxGroup>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.handleError(res)
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        if (this.isSaveAndCreate) {
            this.materialGoodsSpecialTaxGroup = {};
        } else {
            this.isSaving = false;
            this.previousState();
        }
        if (!this.isSaveAndCreate) {
            this.eventManager.broadcast({
                name: 'saveSuccess',
                content: { name: CategoryName.NHOM_HHDV_CHIU_THUE_TTĐB, data: this.data }
            });
            this.close();
        } else {
            this.eventManager.broadcast({
                name: 'saveAndNewSuccess',
                content: { name: CategoryName.NHOM_HHDV_CHIU_THUE_TTĐB, data: this.data }
            });
            this.reloadMaterialGoodsSpecialTaxGroup();
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

    delete() {
        event.preventDefault();
        this.router.navigate([
            '/material-goods-special-tax-group',
            { outlets: { popup: this.materialGoodsSpecialTaxGroup.id + '/delete' } }
        ]);
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
        this.router.navigate(['material-goods-special-tax-group']);
    }

    closeContent() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }

    canDeactive(): boolean {
        if (!this.isClose) {
            return this.utilsService.isEquivalent(this.materialGoodsSpecialTaxGroup, this.materialGoodsSpecialTaxGroupCopy);
        }
        return true;
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
