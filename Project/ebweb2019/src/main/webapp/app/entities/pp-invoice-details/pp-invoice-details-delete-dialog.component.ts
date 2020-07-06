import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPPInvoiceDetails } from 'app/shared/model/pp-invoice-details.model';
import { PPInvoiceDetailsService } from './pp-invoice-details.service';

@Component({
    selector: 'eb-pp-invoice-details-delete-dialog',
    templateUrl: './pp-invoice-details-delete-dialog.component.html'
})
export class PPInvoiceDetailsDeleteDialogComponent {
    pPInvoiceDetails: IPPInvoiceDetails;

    constructor(
        private pPInvoiceDetailsService: PPInvoiceDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: any) {
        this.pPInvoiceDetailsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'pPInvoiceDetailsListModification',
                content: 'Deleted an pPInvoiceDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-pp-invoice-details-delete-popup',
    template: ''
})
export class PPInvoiceDetailsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pPInvoiceDetails }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PPInvoiceDetailsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.pPInvoiceDetails = pPInvoiceDetails;
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
