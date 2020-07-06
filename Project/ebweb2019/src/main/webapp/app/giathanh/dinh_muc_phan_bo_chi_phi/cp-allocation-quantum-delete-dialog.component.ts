import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICPAllocationQuantum } from 'app/shared/model/cp-allocation-quantum.model';
import { CPAllocationQuantumService } from './cp-allocation-quantum.service';

@Component({
    selector: 'eb-cp-allocation-quantum-delete-dialog',
    templateUrl: './cp-allocation-quantum-delete-dialog.component.html'
})
export class CPAllocationQuantumDeleteDialogComponent {
    cPAllocationQuantum: ICPAllocationQuantum;

    constructor(
        private cPAllocationQuantumService: CPAllocationQuantumService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.cPAllocationQuantumService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'cPAllocationQuantumListModification',
                content: 'Deleted an cPAllocationQuantum'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-cp-allocation-quantum-delete-popup',
    template: ''
})
export class CPAllocationQuantumDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cPAllocationQuantum }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CPAllocationQuantumDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.cPAllocationQuantum = cPAllocationQuantum;
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
