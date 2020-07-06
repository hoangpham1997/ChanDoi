import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFixedAssetDetails } from 'app/shared/model/fixed-asset-details.model';
import { FixedAssetDetailsService } from './fixed-asset-details.service';

@Component({
    selector: 'eb-fixed-asset-details-delete-dialog',
    templateUrl: './fixed-asset-details-delete-dialog.component.html'
})
export class FixedAssetDetailsDeleteDialogComponent {
    fixedAssetDetails: IFixedAssetDetails;

    constructor(
        private fixedAssetDetailsService: FixedAssetDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.fixedAssetDetailsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'fixedAssetDetailsListModification',
                content: 'Deleted an fixedAssetDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-fixed-asset-details-delete-popup',
    template: ''
})
export class FixedAssetDetailsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ fixedAssetDetails }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FixedAssetDetailsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.fixedAssetDetails = fixedAssetDetails;
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
