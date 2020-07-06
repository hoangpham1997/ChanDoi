import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFixedAsset } from 'app/shared/model/fixed-asset.model';
import { FixedAssetService } from './fixed-asset.service';

@Component({
    selector: 'eb-fixed-asset-delete-dialog',
    templateUrl: './fixed-asset-delete-dialog.component.html'
})
export class FixedAssetDeleteDialogComponent {
    fixedAsset: IFixedAsset;

    constructor(private fixedAssetService: FixedAssetService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.fixedAssetService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'fixedAssetListModification',
                content: 'Deleted an fixedAsset'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-fixed-asset-delete-popup',
    template: ''
})
export class FixedAssetDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ fixedAsset }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FixedAssetDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.fixedAsset = fixedAsset;
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
