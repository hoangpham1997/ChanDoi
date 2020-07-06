import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { OrganizationUnitAdminService } from './organization-unit.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UnitTypeByOrganizationUnit } from 'app/app.constants';

@Component({
    selector: 'eb-organization-unit-delete-dialog-admin',
    templateUrl: './organization-unit-delete-dialog.component.html'
})
export class OrganizationUnitDeleteDialogAdminComponent {
    organizationUnit: IOrganizationUnit;
    isShowMessage: boolean;

    constructor(
        private organizationUnitService: OrganizationUnitAdminService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastr: ToastrService,
        private translate: TranslateService,
        private router: Router
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        const obj = { orgID: this.organizationUnit.id, organizationUnit: this.organizationUnit };
        this.organizationUnitService
            .deleteBigOrganizationUnitByCompanyID({
                org: JSON.stringify(obj)
            })
            .subscribe(
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
                    if (res.error.errorKey === 'errorDeleteParent') {
                        this.toastr.error(
                            this.translate.instant('ebwebApp.accountList.errorDeleteParent'),
                            this.translate.instant('ebwebApp.accountList.error')
                        );
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
            );
        if (this.isShowMessage) {
            this.toastr.success(
                this.translate.instant('ebwebApp.organizationUnit.deleteSuccess'),
                this.translate.instant('ebwebApp.organizationUnit.message')
            );
        }
        this.activeModal.dismiss(true);
        this.router.navigate(['/admin/organization-unit']);
    }
}

@Component({
    selector: 'eb-organization-unit-delete-popup',
    template: ''
})
export class OrganizationUnitDeletePopupAdminComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ organizationUnit }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(OrganizationUnitDeleteDialogAdminComponent as Component, {
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
