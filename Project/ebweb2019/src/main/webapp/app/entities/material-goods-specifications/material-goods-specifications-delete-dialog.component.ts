import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaterialGoodsSpecifications } from 'app/shared/model/material-goods-specifications.model';
import { MaterialGoodsSpecificationsService } from './material-goods-specifications.service';

@Component({
    selector: 'eb-material-goods-specifications-delete-dialog',
    templateUrl: './material-goods-specifications-delete-dialog.component.html'
})
export class MaterialGoodsSpecificationsDeleteDialogComponent {
    materialGoodsSpecifications: IMaterialGoodsSpecifications;

    constructor(
        private materialGoodsSpecificationsService: MaterialGoodsSpecificationsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.materialGoodsSpecificationsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'materialGoodsSpecificationsListModification',
                content: 'Deleted an materialGoodsSpecifications'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-material-goods-specifications-delete-popup',
    template: ''
})
export class MaterialGoodsSpecificationsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialGoodsSpecifications }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MaterialGoodsSpecificationsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.materialGoodsSpecifications = materialGoodsSpecifications;
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
