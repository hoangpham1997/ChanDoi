import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFixedAssetAllocation } from 'app/shared/model/fixed-asset-allocation.model';
import { FixedAssetAllocationService } from './fixed-asset-allocation.service';

@Component({
    selector: 'eb-fixed-asset-allocation-delete-dialog',
    templateUrl: './fixed-asset-allocation-delete-dialog.component.html'
})
export class FixedAssetAllocationDeleteDialogComponent {
    fixedAssetAllocation: IFixedAssetAllocation;

    constructor(
        private fixedAssetAllocationService: FixedAssetAllocationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.fixedAssetAllocationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'fixedAssetAllocationListModification',
                content: 'Deleted an fixedAssetAllocation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-fixed-asset-allocation-delete-popup',
    template: ''
})
export class FixedAssetAllocationDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ fixedAssetAllocation }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FixedAssetAllocationDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.fixedAssetAllocation = fixedAssetAllocation;
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
