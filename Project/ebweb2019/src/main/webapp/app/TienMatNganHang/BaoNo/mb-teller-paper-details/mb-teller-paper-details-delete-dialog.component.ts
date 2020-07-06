import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMBTellerPaperDetails } from 'app/shared/model/mb-teller-paper-details.model';
import { MBTellerPaperDetailsService } from './mb-teller-paper-details.service';

@Component({
    selector: 'eb-mb-teller-paper-details-delete-dialog',
    templateUrl: './mb-teller-paper-details-delete-dialog.component.html'
})
export class MBTellerPaperDetailsDeleteDialogComponent {
    mBTellerPaperDetails: IMBTellerPaperDetails;

    constructor(
        private mBTellerPaperDetailsService: MBTellerPaperDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.mBTellerPaperDetailsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'mBTellerPaperDetailsListModification',
                content: 'Deleted an mBTellerPaperDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-mb-teller-paper-details-delete-popup',
    template: ''
})
export class MBTellerPaperDetailsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBTellerPaperDetails }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MBTellerPaperDetailsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.mBTellerPaperDetails = mBTellerPaperDetails;
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
