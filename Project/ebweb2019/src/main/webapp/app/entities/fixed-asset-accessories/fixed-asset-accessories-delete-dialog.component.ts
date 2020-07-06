import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFixedAssetAccessories } from 'app/shared/model/fixed-asset-accessories.model';
import { FixedAssetAccessoriesService } from './fixed-asset-accessories.service';

@Component({
    selector: 'eb-fixed-asset-accessories-delete-dialog',
    templateUrl: './fixed-asset-accessories-delete-dialog.component.html'
})
export class FixedAssetAccessoriesDeleteDialogComponent {
    fixedAssetAccessories: IFixedAssetAccessories;

    constructor(
        private fixedAssetAccessoriesService: FixedAssetAccessoriesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.fixedAssetAccessoriesService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'fixedAssetAccessoriesListModification',
                content: 'Deleted an fixedAssetAccessories'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-fixed-asset-accessories-delete-popup',
    template: ''
})
export class FixedAssetAccessoriesDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ fixedAssetAccessories }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FixedAssetAccessoriesDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.fixedAssetAccessories = fixedAssetAccessories;
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
