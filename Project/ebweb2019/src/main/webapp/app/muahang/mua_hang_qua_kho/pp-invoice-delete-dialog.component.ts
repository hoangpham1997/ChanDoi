import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPPInvoice } from 'app/shared/model/pp-invoice.model';
import { PPInvoiceService } from './pp-invoice.service';

@Component({
    selector: 'eb-pp-invoice-delete-dialog',
    templateUrl: './pp-invoice-delete-dialog.component.html'
})
export class PPInvoiceDeleteDialogComponent {
    pPInvoice: IPPInvoice;

    constructor(private pPInvoiceService: PPInvoiceService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.pPInvoiceService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'pPInvoiceListModification',
                content: 'Deleted an pPInvoice'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-pp-invoice-delete-popup',
    template: ''
})
export class PPInvoiceDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pPInvoice }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PPInvoiceDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.pPInvoice = pPInvoice;
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
