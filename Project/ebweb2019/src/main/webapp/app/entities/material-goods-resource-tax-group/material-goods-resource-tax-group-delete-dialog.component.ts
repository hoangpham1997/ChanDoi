import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaterialGoodsResourceTaxGroup } from 'app/shared/model/material-goods-resource-tax-group.model';
import { MaterialGoodsResourceTaxGroupService } from './material-goods-resource-tax-group.service';

@Component({
    selector: 'eb-material-goods-resource-tax-group-delete-dialog',
    templateUrl: './material-goods-resource-tax-group-delete-dialog.component.html'
})
export class MaterialGoodsResourceTaxGroupDeleteDialogComponent {
    materialGoodsResourceTaxGroup: IMaterialGoodsResourceTaxGroup;

    constructor(
        private materialGoodsResourceTaxGroupService: MaterialGoodsResourceTaxGroupService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.materialGoodsResourceTaxGroupService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'materialGoodsResourceTaxGroupListModification',
                content: 'Deleted an materialGoodsResourceTaxGroup'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-material-goods-resource-tax-group-delete-popup',
    template: ''
})
export class MaterialGoodsResourceTaxGroupDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialGoodsResourceTaxGroup }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MaterialGoodsResourceTaxGroupDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.materialGoodsResourceTaxGroup = materialGoodsResourceTaxGroup;
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
