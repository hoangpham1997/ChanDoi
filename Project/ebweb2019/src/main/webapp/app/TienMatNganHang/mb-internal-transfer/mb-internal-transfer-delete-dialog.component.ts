import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMBInternalTransfer } from 'app/shared/model/mb-internal-transfer.model';
import { MBInternalTransferService } from './mb-internal-transfer.service';

@Component({
    selector: 'eb-mb-internal-transfer-delete-dialog',
    templateUrl: './mb-internal-transfer-delete-dialog.component.html'
})
export class MBInternalTransferDeleteDialogComponent {
    mBInternalTransfer: IMBInternalTransfer;

    constructor(
        private mBInternalTransferService: MBInternalTransferService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.mBInternalTransferService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'mBInternalTransferListModification',
                content: 'Deleted an mBInternalTransfer'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-mb-internal-transfer-delete-popup',
    template: ''
})
export class MBInternalTransferDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBInternalTransfer }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MBInternalTransferDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.mBInternalTransfer = mBInternalTransfer;
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
