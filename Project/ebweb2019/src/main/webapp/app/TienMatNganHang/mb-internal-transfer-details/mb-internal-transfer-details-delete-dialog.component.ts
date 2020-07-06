import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMBInternalTransferDetails } from 'app/shared/model/mb-internal-transfer-details.model';
import { MBInternalTransferDetailsService } from './mb-internal-transfer-details.service';

@Component({
    selector: 'eb-mb-internal-transfer-details-delete-dialog',
    templateUrl: './mb-internal-transfer-details-delete-dialog.component.html'
})
export class MBInternalTransferDetailsDeleteDialogComponent {
    mBInternalTransferDetails: IMBInternalTransferDetails;

    constructor(
        private mBInternalTransferDetailsService: MBInternalTransferDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.mBInternalTransferDetailsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'mBInternalTransferDetailsListModification',
                content: 'Deleted an mBInternalTransferDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-mb-internal-transfer-details-delete-popup',
    template: ''
})
export class MBInternalTransferDetailsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBInternalTransferDetails }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MBInternalTransferDetailsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.mBInternalTransferDetails = mBInternalTransferDetails;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
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
