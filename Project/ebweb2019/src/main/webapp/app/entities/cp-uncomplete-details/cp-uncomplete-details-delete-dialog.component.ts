import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICPUncompleteDetails } from 'app/shared/model/cp-uncomplete-details.model';
import { CPUncompleteDetailsService } from './cp-uncomplete-details.service';

@Component({
    selector: 'eb-cp-uncomplete-details-delete-dialog',
    templateUrl: './cp-uncomplete-details-delete-dialog.component.html'
})
export class CPUncompleteDetailsDeleteDialogComponent {
    cPUncompleteDetails: ICPUncompleteDetails;

    constructor(
        private cPUncompleteDetailsService: CPUncompleteDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.cPUncompleteDetailsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'cPUncompleteDetailsListModification',
                content: 'Deleted an cPUncompleteDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-cp-uncomplete-details-delete-popup',
    template: ''
})
export class CPUncompleteDetailsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cPUncompleteDetails }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CPUncompleteDetailsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.cPUncompleteDetails = cPUncompleteDetails;
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
