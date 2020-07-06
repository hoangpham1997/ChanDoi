import { AfterViewInit, Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from './unit.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { Principal } from 'app/core';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-unit-update',
    templateUrl: './unit-update.component.html',
    styleUrls: ['./unit-update.component.css']
})
export class UnitUpdateComponent extends BaseComponent implements OnInit, AfterViewInit {
    @ViewChild('content') content: TemplateRef<any>;
    private _unit: IUnit;
    isSaving: boolean;
    isSaveAndCreate: boolean;
    isEditUrl: boolean;
    isDelete: boolean;
    units: IUnit[];
    modalRef: NgbModalRef;
    isClose: boolean;
    unitCopy: IUnit;
    arrAuthorities: any[];
    isRoleSua: boolean;
    isRoleThem: boolean;
    currentUnit: any;
    isCreateUrl: boolean;
    ROLE_DanhMucDonViTinh_Them = ROLE.DanhMucDonViTinh_Them;
    ROLE_DanhMucDonViTinh_Sua = ROLE.DanhMucDonViTinh_Sua;
    ROLE_DanhMucDonViTinh_Xoa = ROLE.DanhMucDonViTinh_Xoa;
    ROLE_DanhMucDonViTinh_Xem = ROLE.DanhMucDonViTinh_Xem;

    constructor(
        private unitService: UnitService,
        private activatedRoute: ActivatedRoute,
        private toastr: ToastrService,
        private router: Router,
        private translate: TranslateService,
        public utilsService: UtilsService,
        private modalService: NgbModal,
        private principal: Principal
    ) {
        super();
        this.principal.identity().then(unit => {
            this.currentUnit = unit;
            this.arrAuthorities = unit.authorities;
            this.isEditUrl = window.location.href.includes('edit');
            this.isCreateUrl = window.location.href.includes('/new');
            this.isRoleSua = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_DanhMucDonViTinh_Sua)
                : true;
            this.isRoleThem = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_DanhMucDonViTinh_Them)
                : true;
        });
    }

    ngOnInit() {
        this.isClose = false;
        this.isSaveAndCreate = false;
        this.isSaving = false;
        this.isEditUrl = window.location.href.includes('/edit') || window.location.href.includes('/view');
        this.isDelete = false;
        this.activatedRoute.data.subscribe(({ unit }) => {
            this.unit = unit;
        });
        this.unitService.getAllUnits().subscribe(res => {
            this.units = res.body;
        });
        this.copy();
    }

    previousState() {
        this.router.navigate(['/unit']);
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
                    this.onSaveSuccess();
                } else {
                    this.toastr.error(this.translate.instant('ebwebApp.unit.existData'));
                }
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isClose = true;
        if (this.isSaveAndCreate) {
            this.unit = {};
        } else {
            this.isSaving = false;
            this.previousState();
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

    @ebAuth(['ROLE_ADMIN', ROLE.DanhMucDonViTinh_Xoa])
    delete() {
        event.preventDefault();
        if (this.unit.id) {
            this.router.navigate(['/unit', { outlets: { popup: this.unit.id + '/delete' } }]);
        }
    }

    close() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.isClose = true;
        this.router.navigate(['unit']);
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

    closeForm() {
        event.preventDefault();
        if (this.unitCopy) {
            if (!this.utilsService.isEquivalent(this.unit, this.unitCopy)) {
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
        this.router.navigate(['/unit']);
    }
}
