import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { IAReportService } from 'app/quan-ly-hoa-don/khoi-tao-mau-hoa-don/ia-report.service';
import { IAReport } from 'app/shared/model/ia-report.model';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-khoi-tao-hoa-don-delete-dialog',
    templateUrl: './khoi-tao-hoa-don-delete-dialog.component.html'
})
export class KhoiTaoHoaDonDeleteDialogComponent {
    iaReport: IAReport;
    ROLE_XEM = ROLE.KTHD_XEM;
    ROLE_THEM = ROLE.KTHD_THEM;
    ROLE_SUA = ROLE.KTHD_SUA;
    ROLE_XOA = ROLE.KTHD_XOA;
    ROLE_KETXUAT = ROLE.KTHD_KET_XUAT;

    constructor(
        private iaReportService: IAReportService,
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
        this.iaReportService.delete(id).subscribe(
            response => {
                this.eventManager.broadcast({
                    name: 'iaReportListModification',
                    content: 'Deleted an IAReport'
                });
                this.activeModal.dismiss(true);
                this.toastrService.success(this.translateService.instant('ebwebApp.iAReport.deleted'));
            },
            error => {
                this.toastrService.error(this.translateService.instant(`ebwebApp.iAReport.${error.error.message}`));
                this.clear();
            }
        );
    }
}

@Component({
    selector: 'eb-khoi-tao-hoa-don-delete-popup',
    template: ''
})
export class KhoiTaoHoaDonDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(data => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(KhoiTaoHoaDonDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.iaReport = data.iaReport;
                this.ngbModalRef.result.then(
                    result => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => this.router.navigate(['khoi-tao-mau-hoa-don']));
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => this.router.navigate(['khoi-tao-mau-hoa-don']));
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
