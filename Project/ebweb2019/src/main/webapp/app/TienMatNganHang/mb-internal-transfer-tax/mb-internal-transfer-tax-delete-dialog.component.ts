import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMBInternalTransferTax } from 'app/shared/model/mb-internal-transfer-tax.model';
import { MBInternalTransferTaxService } from './mb-internal-transfer-tax.service';

@Component({
    selector: 'eb-mb-internal-transfer-tax-delete-dialog',
    templateUrl: './mb-internal-transfer-tax-delete-dialog.component.html'
})
export class MBInternalTransferTaxDeleteDialogComponent {
    mBInternalTransferTax: IMBInternalTransferTax;

    constructor(
        private mBInternalTransferTaxService: MBInternalTransferTaxService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.mBInternalTransferTaxService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'mBInternalTransferTaxListModification',
                content: 'Deleted an mBInternalTransferTax'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-mb-internal-transfer-tax-delete-popup',
    template: ''
})
export class MBInternalTransferTaxDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBInternalTransferTax }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MBInternalTransferTaxDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.mBInternalTransferTax = mBInternalTransferTax;
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
