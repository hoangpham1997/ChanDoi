import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMBTellerPaperDetailTax } from 'app/shared/model/mb-teller-paper-detail-tax.model';
import { MBTellerPaperDetailTaxService } from './mb-teller-paper-detail-tax.service';

@Component({
    selector: 'eb-mb-teller-paper-detail-tax-delete-dialog',
    templateUrl: './mb-teller-paper-detail-tax-delete-dialog.component.html'
})
export class MBTellerPaperDetailTaxDeleteDialogComponent {
    mBTellerPaperDetailTax: IMBTellerPaperDetailTax;

    constructor(
        private mBTellerPaperDetailTaxService: MBTellerPaperDetailTaxService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.mBTellerPaperDetailTaxService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'mBTellerPaperDetailTaxListModification',
                content: 'Deleted an mBTellerPaperDetailTax'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-mb-teller-paper-detail-tax-delete-popup',
    template: ''
})
export class MBTellerPaperDetailTaxDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBTellerPaperDetailTax }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MBTellerPaperDetailTaxDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.mBTellerPaperDetailTax = mBTellerPaperDetailTax;
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
