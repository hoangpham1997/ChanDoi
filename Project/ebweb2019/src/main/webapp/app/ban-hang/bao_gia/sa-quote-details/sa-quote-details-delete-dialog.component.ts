import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISAQuoteDetails } from 'app/shared/model/sa-quote-details.model';
import { SAQuoteDetailsService } from './sa-quote-details.service';

@Component({
    selector: 'eb-sa-quote-details-delete-dialog',
    templateUrl: './sa-quote-details-delete-dialog.component.html'
})
export class SAQuoteDetailsDeleteDialogComponent {
    sAQuoteDetails: ISAQuoteDetails;

    constructor(
        private sAQuoteDetailsService: SAQuoteDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.sAQuoteDetailsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'sAQuoteDetailsListModification',
                content: 'Deleted an sAQuoteDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-sa-quote-details-delete-popup',
    template: ''
})
export class SAQuoteDetailsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sAQuoteDetails }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SAQuoteDetailsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.sAQuoteDetails = sAQuoteDetails;
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
