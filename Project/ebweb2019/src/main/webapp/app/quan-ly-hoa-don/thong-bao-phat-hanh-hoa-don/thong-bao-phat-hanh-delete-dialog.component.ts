import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { IAReportService } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/ia-report.service';
import { IAReport } from 'app/shared/model/ia-report.model';
import { IaPublishInvoice } from 'app/shared/model/ia-publish-invoice.model';
import { IaPublishInvoiceService } from 'app/quan-ly-hoa-don/thong-bao-phat-hanh-hoa-don/ia-publish-invoice.service';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-khoi-tao-hoa-don-delete-dialog',
    templateUrl: './thong-bao-phat-hanh-delete-dialog.component.html'
})
export class ThongBaoPhatHanhDeleteDialogComponent {
    iaPublishInvoice: IaPublishInvoice;

    ROLE_XEM = ROLE.TBPH_XEM;
    ROLE_THEM = ROLE.TBPH_THEM;
    ROLE_SUA = ROLE.TBPH_SUA;
    ROLE_XOA = ROLE.TBPH_XOA;
    ROLE_KETXUAT = ROLE.TBPH_KET_XUAT;

    constructor(
        private iaPublishInvoiceService: IaPublishInvoiceService,
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
        this.iaPublishInvoiceService.delete(id).subscribe(
            response => {
                this.eventManager.broadcast({
                    name: 'iaPublishInvoiceListModification',
                    content: 'Deleted an IAPublishInvoice'
                });
                this.activeModal.dismiss(true);
                this.toastrService.success(this.translateService.instant('ebwebApp.iaPublishInvoice.deleted'));
            },
            err => {
                if (err && err.error) {
                    this.toastrService.error(
                        this.translateService.instant(`ebwebApp.iaPublishInvoice.${err.error.message}`, { title: err.error.title })
                    );
                }
                this.clear();
            }
        );
    }
}

@Component({
    selector: 'eb-thong-bao-phat-hanh-delete-popup',
    template: ''
})
export class ThongBaoPhatHanhDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(data => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ThongBaoPhatHanhDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.iaPublishInvoice = data.iaPublishInvoice;
                this.ngbModalRef.result.then(
                    result => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => this.router.navigate(['thong-bao-phat-hanh-hoa-don']));
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => this.router.navigate(['thong-bao-phat-hanh-hoa-don']));
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
