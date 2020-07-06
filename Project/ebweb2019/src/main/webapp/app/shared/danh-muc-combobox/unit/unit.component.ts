import { AfterViewInit, Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from 'app/danhmuc/unit';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { ContextMenu } from 'app/shared/model/context-menu.model';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { IBank } from 'app/shared/model/bank.model';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { JhiEventManager } from 'ng-jhipster';
import { IRepository } from 'app/shared/model/repository.model';
import { CategoryName } from 'app/app.constants';

@Component({
    selector: 'eb-unit-cobobox',
    templateUrl: './unit.component.html',
    styleUrls: ['./unit.component.css']
})
export class UnitComboboxComponent extends BaseComponent implements OnInit, AfterViewInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _unit: IUnit;
    isSaving: boolean;
    data: IUnit;
    isSaveAndCreate: boolean;
    isEditUrl: boolean;
    isDelete: boolean;
    units: IUnit[];
    modalRef: NgbModalRef;
    isClose: boolean;
    unitCopy: IUnit;
    saveSuccess: boolean;
    isCheck: boolean;
    ROLE_DanhMucDonViTinh_Them = ROLE.DanhMucDonViTinh_Them;
    ROLE_DanhMucDonViTinh_Sua = ROLE.DanhMucDonViTinh_Sua;
    ROLE_DanhMucDonViTinh_Xoa = ROLE.DanhMucDonViTinh_Xoa;

    constructor(
        private unitService: UnitService,
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
        this.isSaveAndCreate = false;
        this.isSaving = false;
        this.isEditUrl = window.location.href.includes('/edit') || window.location.href.includes('/view');
        this.isDelete = false;
        this.copy();
        this.isShowPopup = true;
        this.unit = {};
        this.unitService.getAllUnits().subscribe(res => {
            this.units = res.body;
        });
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
        if (!this.unit.unitName) {
            this.toastr.error(this.translate.instant('ebwebApp.unit.missUnit'), this.translate.instant('ebwebApp.unit.notification'));
            return;
        }
        if (this.unit.id !== undefined) {
            this.subscribeToSaveResponse(this.unitService.update(this.unit));
        } else {
            this.subscribeToSaveResponse(this.unitService.create(this.unit));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe(
            (res: HttpResponse<any>) => {
                if (res.body.status === 0) {
                    this.data = res.body.unit;
                    this.onSaveSuccess();
                } else {
                    this.isSaving = false;
                    this.toastr.error(this.translate.instant('ebwebApp.unit.existData'));
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.copy();
        this.isClose = true;
        if (this.isSaveAndCreate) {
            this.eventManager.broadcast({
                name: 'saveAndNewSuccess',
                content: { name: CategoryName.DON_VI_TINH, data: this.data }
            });
            this.unit = {};
        } else {
            this.isSaving = false;
            this.eventManager.broadcast({
                name: 'saveSuccess',
                content: { name: CategoryName.DON_VI_TINH, data: this.data }
            });
            this.close();
        }
        if (this.isEditUrl) {
            this.toastr.success(
                this.translate.instant('ebwebApp.unit.insertDataSuccess'),
                this.translate.instant('ebwebApp.unit.notification')
            );
        } else {
            this.toastr.success(
                this.translate.instant('ebwebApp.unit.insertDataSuccess'),
                this.translate.instant('ebwebApp.unit.notification')
            );
        }
    }

    private onSaveError() {
        this.isSaving = false;
        if (this.isEditUrl) {
            this.toastr.error(this.translate.instant('ebwebApp.unit.editDataFail'), this.translate.instant('ebwebApp.unit.notification'));
        } else {
            this.toastr.error(this.translate.instant('ebwebApp.unit.insertDataFail'), this.translate.instant('ebwebApp.unit.notification'));
        }
    }

    get unit() {
        return this._unit;
    }

    set unit(unit: IUnit) {
        this._unit = unit;
    }

    saveAndNew() {
        event.preventDefault();
        this.isSaving = true;
        this.isSaveAndCreate = true;
        this.isCheck = true;
        if (!this.unit.unitName) {
            this.toastr.error(this.translate.instant('ebwebApp.unit.missUnit'), this.translate.instant('ebwebApp.unit.notification'));
            return;
        }
        if (this.unit.id !== undefined) {
            this.subscribeToSaveResponse(this.unitService.update(this.unit));
        } else {
            this.subscribeToSaveResponse(this.unitService.create(this.unit));
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
        if (!this.utilsService.isEquivalent(this.unit, this.unitCopy)) {
            this.modalRef = this.modalService.open(content, { backdrop: 'static' });
        } else {
            this.close();
        }
    }

    copy() {
        this.unitCopy = Object.assign({}, this.unit);
    }
    canDeactive(): boolean {
        if (!this.isClose) {
            return this.utilsService.isEquivalent(this.unit, this.unitCopy);
        }
        return true;
    }

    closeAll() {
        this.router.navigate(['/unit']);
    }
}
