import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFixedAssetCategory } from 'app/shared/model/fixed-asset-category.model';
import { FixedAssetCategoryService } from './fixed-asset-category.service';

@Component({
    selector: 'eb-fixed-asset-category-delete-dialog',
    templateUrl: './fixed-asset-category-delete-dialog.component.html'
})
export class FixedAssetCategoryDeleteDialogComponent {
    fixedAssetCategory: IFixedAssetCategory;

    constructor(
        private fixedAssetCategoryService: FixedAssetCategoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.fixedAssetCategoryService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'fixedAssetCategoryListModification',
                content: 'Deleted an fixedAssetCategory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-fixed-asset-category-delete-popup',
    template: ''
})
export class FixedAssetCategoryDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ fixedAssetCategory }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FixedAssetCategoryDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.fixedAssetCategory = fixedAssetCategory;
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
