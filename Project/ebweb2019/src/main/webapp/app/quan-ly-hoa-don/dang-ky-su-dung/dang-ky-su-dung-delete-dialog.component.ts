import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { IARegisterInvoice } from 'app/shared/model/ia-register-invoice.model';
import { IARegisterInvoiceService } from 'app/quan-ly-hoa-don/dang-ky-su-dung/ia-register-invoice.service';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-don-mua-hang-delete-dialog',
    templateUrl: './dang-ky-su-dung-delete-dialog.component.html'
})
export class DangKySuDungDeleteDialogComponent {
    iaRegisterInvoice: IARegisterInvoice;

    ROLE_XEM = ROLE.DKSD_XEM;
    ROLE_THEM = ROLE.DKSD_THEM;
    ROLE_SUA = ROLE.DKSD_SUA;
    ROLE_XOA = ROLE.DKSD_XOA;
    ROLE_KETXUAT = ROLE.DKSD_KET_XUAT;

    constructor(
        private iaRegisterInvoiceService: IARegisterInvoiceService,
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
        this.iaRegisterInvoiceService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'IARegisterInvoiceListModification',
                content: 'Deleted an iaRegisterInvoice'
            });
            this.activeModal.dismiss(true);
            this.toastrService.success(this.translateService.instant('ebwebApp.iARegisterInvoice.deleted'));
        });
    }
}

@Component({
    selector: 'eb-dang-ky-su-dung-hoa-don-delete-popup',
    template: ''
})
export class DangKySuDungDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(data => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DangKySuDungDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                console.log(data);
                this.ngbModalRef.componentInstance.iaRegisterInvoice = data.iaRegisterInvoice;
                this.ngbModalRef.result.then(
                    result => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => this.router.navigate(['dang-ky-su-dung-hoa-don']));
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => this.router.navigate(['dang-ky-su-dung-hoa-don']));
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
