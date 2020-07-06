import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaterialQuantumDetails } from 'app/shared/model/material-quantum-details.model';
import { MaterialQuantumDetailsService } from './material-quantum-details.service';

@Component({
    selector: 'eb-material-quantum-details-delete-dialog',
    templateUrl: './material-quantum-details-delete-dialog.component.html'
})
export class MaterialQuantumDetailsDeleteDialogComponent {
    materialQuantumDetails: IMaterialQuantumDetails;

    constructor(
        private materialQuantumDetailsService: MaterialQuantumDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.materialQuantumDetailsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'materialQuantumDetailsListModification',
                content: 'Deleted an materialQuantumDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-material-quantum-details-delete-popup',
    template: ''
})
export class MaterialQuantumDetailsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialQuantumDetails }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MaterialQuantumDetailsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.materialQuantumDetails = materialQuantumDetails;
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
