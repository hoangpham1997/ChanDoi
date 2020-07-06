import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';
import { PPDiscountReturnDetailsService } from './pp-discount-return-details.service';

@Component({
    selector: 'eb-pp-discount-return-details-delete-dialog',
    templateUrl: './pp-discount-return-details-delete-dialog.component.html'
})
export class PPDiscountReturnDetailsDeleteDialogComponent {
    pPDiscountReturnDetails: IPPDiscountReturnDetails;

    constructor(
        private pPDiscountReturnDetailsService: PPDiscountReturnDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: any) {
        this.pPDiscountReturnDetailsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'pPDiscountReturnDetailsListModification',
                content: 'Deleted an pPDiscountReturnDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-pp-discount-return-details-delete-popup',
    template: ''
})
export class PPDiscountReturnDetailsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pPDiscountReturnDetails }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PPDiscountReturnDetailsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.pPDiscountReturnDetails = pPDiscountReturnDetails;
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
