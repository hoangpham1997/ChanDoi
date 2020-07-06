import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { RSInwardOutward } from 'app/shared/model/rs-inward-outward.model';
import { ROLE } from 'app/role.constants';
import { RSInwardOutwardService } from 'app/entities/rs-inward-outward/rs-inward-outward.service';

@Component({
    selector: 'eb-nhap-kho-delete-dialog',
    templateUrl: './nhap-kho-delete-dialog.component.html'
})
export class NhapKhoDeleteDialogComponent {
    ppOrder: RSInwardOutward;

    ROLE_XEM = ROLE.NhapKho_XEM;
    ROLE_THEM = ROLE.NhapKho_THEM;
    ROLE_SUA = ROLE.NhapKho_SUA;
    ROLE_XOA = ROLE.NhapKho_XOA;
    ROLE_GHI_SO = ROLE.NhapKho_GHI_SO;
    ROLE_IN = ROLE.NhapKho_IN;
    ROLE_KETXUAT = ROLE.NhapKho_KET_XUAT;

    constructor(
        private pPOrderService: RSInwardOutwardService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastrService: ToastrService,
        private route: Router,
        private translateService: TranslateService
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.pPOrderService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'RSInwardOutwardListModification',
                content: 'Deleted an RSInwardOutward'
            });
            this.activeModal.dismiss(true);
            this.toastrService.success(this.translateService.instant('ebwebApp.rSInwardOutward.deleted'));
        });
    }
}

@Component({
    selector: 'eb-don-mua-hang-delete-popup',
    template: ''
})
export class NhapKhoDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(data => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(NhapKhoDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.ppOrder = data.ppOrder;
                this.ngbModalRef.result.then(
                    result => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => this.router.navigate(['/nhap-kho']));
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => this.router.navigate(['/nhap-kho']));
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
