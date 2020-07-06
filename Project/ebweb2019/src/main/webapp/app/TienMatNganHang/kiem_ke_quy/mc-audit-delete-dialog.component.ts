import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMCAudit } from 'app/shared/model/mc-audit.model';
import { MCAuditService } from './mc-audit.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-mc-audit-delete-dialog',
    templateUrl: './mc-audit-delete-dialog.component.html'
})
export class MCAuditDeleteDialogComponent {
    mCAudit: IMCAudit;

    constructor(
        private mCAuditService: MCAuditService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastr: ToastrService,
        private translate: TranslateService,
        private router: Router
    ) {}

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.mCAuditService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'mCAuditListModification',
                content: 'Deleted an mCAudit'
            });
            this.activeModal.dismiss(true);
        });
        this.toastr.success(
            this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
        this.router.navigate(['mc-audit']);
    }
}

@Component({
    selector: 'eb-mc-audit-delete-popup',
    template: ''
})
export class MCAuditDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mCAudit }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MCAuditDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.mCAudit = mCAudit;
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
