import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitService } from './organization-unit.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UnitTypeByOrganizationUnit } from 'app/app.constants';

@Component({
    selector: 'eb-organization-unit-delete-dialog',
    templateUrl: './organization-unit-delete-dialog.component.html'
})
export class OrganizationUnitDeleteDialogComponent {
    organizationUnit: IOrganizationUnit;
    isShowMessage: boolean;
    modalRef: NgbModalRef;
    skipExistUserOrgID: boolean = false;
    @ViewChild('deleteUser') deleteUser: TemplateRef<any>;

    constructor(
        private organizationUnitService: OrganizationUnitService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastr: ToastrService,
        private translate: TranslateService,
        private router: Router,
        private modalService: NgbModal
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
        window.history.back();
    }

    exit() {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.skipExistUserOrgID = false;
    }

    delete() {
        this.skipExistUserOrgID = true;
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.confirmDelete(this.organizationUnit.id);
    }

    confirmDelete(id: string) {
        const obj = {
            orgID: this.organizationUnit.id,
            organizationUnit: this.organizationUnit,
            skipExistUserOrgID: this.skipExistUserOrgID
        };
        this.organizationUnitService.deleteOrganizationUnitByCompanyID(obj).subscribe(
            (res: HttpResponse<IOrganizationUnit>) => {
                this.toastr.success(
                    this.translate.instant('ebwebApp.organizationUnit.deleteSuccess'),
                    this.translate.instant('ebwebApp.organizationUnit.message')
                );
                this.eventManager.broadcast({
                    name: 'organizationUnitListModification',
                    content: 'Deleted an organizationUnit'
                });
            },
            (res: HttpErrorResponse) => {
                if (res.error) {
                    if (res.error.errorKey === 'existUserUseOrg') {
                        this.modalRef = this.modalService.open(this.deleteUser, { backdrop: 'static' });
                    } else if (res.error.errorKey === 'errorDeleteParent') {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.accountList.errorDeleteParent'),
                            this.translate.instant('ebwebApp.accountList.error')
                        );
                    } else if (res.error.errorKey === 'errorExistVoucher') {
                        if (this.organizationUnit.unitType === 0) {
                            this.toastr.error(
                                this.translate.instant('ebwebApp.organizationUnit.companyErrorExistVoucher'),
                                this.translate.instant('ebwebApp.organizationUnit.message')
                            );
                        } else if (this.organizationUnit.unitType === 1) {
                            this.toastr.error(
                                this.translate.instant('ebwebApp.organizationUnit.branchErrorExistVoucher'),
                                this.translate.instant('ebwebApp.organizationUnit.message')
                            );
                        } else if (this.organizationUnit.unitType === 2) {
                            this.toastr.error(
                                this.translate.instant('ebwebApp.organizationUnit.representativeOfficeErrorExistVoucher'),
                                this.translate.instant('ebwebApp.organizationUnit.message')
                            );
                        } else if (this.organizationUnit.unitType === 3) {
                            this.toastr.error(
                                this.translate.instant('ebwebApp.organizationUnit.placeOfBusinessErrorExistVoucher'),
                                this.translate.instant('ebwebApp.organizationUnit.message')
                            );
                        } else if (this.organizationUnit.unitType === 4) {
                            this.toastr.error(
                                this.translate.instant('ebwebApp.organizationUnit.departmentErrorExistVoucher'),
                                this.translate.instant('ebwebApp.organizationUnit.message')
                            );
                        } else if (this.organizationUnit.unitType === 5) {
                            this.toastr.error(
                                this.translate.instant('ebwebApp.organizationUnit.otherErrorExistVoucher'),
                                this.translate.instant('ebwebApp.organizationUnit.message')
                            );
                        }
                    } else {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.organizationUnit.deleteFail'),
                            this.translate.instant('ebwebApp.organizationUnit.message')
                        );
                        this.eventManager.broadcast({
                            name: 'organizationUnitListModification',
                            content: 'Deleted an organizationUnit'
                        });
                    }
                }
            }
        );
        if (this.isShowMessage) {
            this.toastr.success(
                this.translate.instant('ebwebApp.organizationUnit.deleteSuccess'),
                this.translate.instant('ebwebApp.organizationUnit.message')
            );
        }
        this.activeModal.dismiss(true);
        if (!this.skipExistUserOrgID) {
            window.history.back();
        } else {
            this.router.navigate(['organization-unit']);
        }
    }
}

@Component({
    selector: 'eb-organization-unit-delete-popup',
    template: ''
})
export class OrganizationUnitDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ organizationUnit }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(OrganizationUnitDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.organizationUnit = organizationUnit;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], {
                            replaceUrl: true,
                            queryParamsHandling: 'merge'
                        });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], {
                            replaceUrl: true,
                            queryParamsHandling: 'merge'
                        });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
