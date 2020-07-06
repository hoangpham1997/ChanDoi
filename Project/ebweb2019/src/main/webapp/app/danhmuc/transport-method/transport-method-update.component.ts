import { Component, OnInit, OnDestroy, ViewChild, AfterViewInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
// import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// import { Observable } from 'rxjs';
import { TransportMethodService } from './transport-method.service';
import { ITransportMethod } from 'app/shared/model/transport-method.model';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UnitService } from 'app/danhmuc/unit';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from 'app/shared/base-component/base.component';
import { ROLE } from 'app/role.constants';
import { Principal } from 'app/core';
import { ebAuth } from 'app/shared/decorator/ebAuth.decorator';

@Component({
    selector: 'eb-transport-method-update',
    templateUrl: './transport-method-update.component.html',
    styleUrls: ['./transport-method-update.component.css']
})
export class TransportMethodUpdateComponent extends BaseComponent implements OnInit, OnDestroy, AfterViewInit {
    @ViewChild('content') content: any;
    transportMethod: ITransportMethod;
    transportMethodCopy: ITransportMethod;
    modalRef: NgbModalRef;
    roleSua: boolean;
    roleThem: boolean;
    arrAuthorities: any[];
    saveandadd = false;
    ROLE_TransportMethod_Them = ROLE.DanhMucPhuongThucVanChuyen_Them;
    ROLE_TransportMethod_Sua = ROLE.DanhMucPhuongThucVanChuyen_Sua;
    ROLE_TransportMethod_Xoa = ROLE.DanhMucPhuongThucVanChuyen_Xoa;
    isRoleSua: boolean;
    isRoleThem: boolean;
    isCreateUrl: boolean;
    isEditUrl: boolean;

    constructor(
        private transportmethodservice: TransportMethodService,
        public utilsService: UtilsService,
        private toastr: ToastrService,
        private translate: TranslateService,
        private router: Router,
        private principal: Principal,
        private unitService: UnitService,
        private activeRoute: ActivatedRoute,
        private eventManager: JhiEventManager,
        private modalService: NgbModal
    ) {
        super();
    }

    ngOnInit() {
        this.ROLE_TransportMethod_Them = ROLE.DanhMucPhuongThucVanChuyen_Them;
        this.ROLE_TransportMethod_Sua = ROLE.DanhMucPhuongThucVanChuyen_Sua;
        this.ROLE_TransportMethod_Xoa = ROLE.DanhMucPhuongThucVanChuyen_Xoa;
        this.principal.identity().then(account => {
            this.arrAuthorities = account.authorities;
            this.roleSua = !this.arrAuthorities.includes('ROLE_ADMIN') ? this.arrAuthorities.includes(this.ROLE_TransportMethod_Sua) : true;
            this.roleThem = !this.arrAuthorities.includes('ROLE_ADMIN')
                ? this.arrAuthorities.includes(this.ROLE_TransportMethod_Them)
                : true;
        });
        this.transportMethod = {
            transportMethodCode: '',
            transportMethodName: '',
            description: '',
            isActive: true,
            isSecurity: true
        };
        this.activeRoute.data.subscribe(transport => {
            if (transport.transportMethod.id) {
                this.transportMethod = transport.transportMethod;
            }
        });
        this.copy();
        // this.eventSupcriber= this.eventManager.subscribe("TransportMethod",this.previousState());
    }
    copy() {
        this.transportMethodCopy = Object.assign({}, this.transportMethod);
    }
    ngOnDestroy(): void {}

    delete() {
        event.preventDefault();
        if (this.transportMethod.id) {
            this.router.navigate(['/transport-method', { outlets: { popup: this.transportMethod.id + '/delete' } }]);
        }
    }

    previousState() {
        this.router.navigate(['transport-method']);
    }

    saveAndNew() {
        event.preventDefault();
        this.saveandadd = true;
        this.save();
        this.focusFirstInput();
    }
    save() {
        event.preventDefault();
        if (this.transportMethod.transportMethodCode === '' || this.transportMethod.transportMethodName === '') {
            this.toastr.error(this.translate.instant('ebwebApp.transportMethod.errorInput'));
        } else {
            if (this.transportMethod.id) {
                this.transportmethodservice.update(this.transportMethod).subscribe(
                    res => {
                        this.copy();
                        this.toastr.success(this.translate.instant('ebwebApp.transportMethod.successful'));
                        if (!this.saveandadd) {
                            this.previousState();
                        } else {
                            this.router.navigate(['transport-method/new']);
                        }
                    },
                    error => {
                        this.error(error);
                    }
                );
            } else {
                this.transportmethodservice.create(this.transportMethod).subscribe(
                    res => {
                        this.copy();
                        this.toastr.success(this.translate.instant('ebwebApp.transportMethod.successful'));
                        if (!this.saveandadd) {
                            this.previousState();
                        } else {
                            this.transportMethod = {
                                transportMethodCode: '',
                                transportMethodName: '',
                                description: '',
                                isActive: true,
                                isSecurity: true
                            };
                            this.copy();
                            this.saveandadd = false;
                        }
                    },
                    error => {
                        this.error(error);
                    }
                );
            }
        }
    }

    error(err) {
        this.toastr.error(this.translate.instant(`ebwebApp.transportMethod.${err.error.message}`));
    }

    closeForm() {
        event.preventDefault();
        if (!this.utilsService.isEquivalent(this.transportMethodCopy, this.transportMethod)) {
            this.modalRef = this.modalService.open(this.content, { backdrop: 'static' });
        } else {
            this.copy();
            this.previousState();
        }
    }

    close() {
        this.modalRef.close();
    }

    closeContent() {
        this.close();
        this.copy();
        this.previousState();
    }

    saveContent() {
        this.close();
        this.save();
    }

    canDeactive() {
        return this.utilsService.isEquivalent(this.transportMethod, this.transportMethodCopy);
    }

    ngAfterViewInit(): void {
        this.focusFirstInput();
    }
}
