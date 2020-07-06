import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICPAllocationRate } from 'app/shared/model/cp-allocation-rate.model';
import { CPAllocationRateService } from './cp-allocation-rate.service';

@Component({
    selector: 'eb-cp-allocation-rate-delete-dialog',
    templateUrl: './cp-allocation-rate-delete-dialog.component.html'
})
export class CPAllocationRateDeleteDialogComponent {
    cPAllocationRate: ICPAllocationRate;

    constructor(
        private cPAllocationRateService: CPAllocationRateService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.cPAllocationRateService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'cPAllocationRateListModification',
                content: 'Deleted an cPAllocationRate'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-cp-allocation-rate-delete-popup',
    template: ''
})
export class CPAllocationRateDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cPAllocationRate }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CPAllocationRateDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.cPAllocationRate = cPAllocationRate;
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
