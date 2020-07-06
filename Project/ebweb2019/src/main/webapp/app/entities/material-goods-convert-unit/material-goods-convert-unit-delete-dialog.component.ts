import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaterialGoodsConvertUnit } from 'app/shared/model/material-goods-convert-unit.model';
import { MaterialGoodsConvertUnitService } from './material-goods-convert-unit.service';

@Component({
    selector: 'eb-material-goods-convert-unit-delete-dialog',
    templateUrl: './material-goods-convert-unit-delete-dialog.component.html'
})
export class MaterialGoodsConvertUnitDeleteDialogComponent {
    materialGoodsConvertUnit: IMaterialGoodsConvertUnit;

    constructor(
        private materialGoodsConvertUnitService: MaterialGoodsConvertUnitService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.materialGoodsConvertUnitService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'materialGoodsConvertUnitListModification',
                content: 'Deleted an materialGoodsConvertUnit'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-material-goods-convert-unit-delete-popup',
    template: ''
})
export class MaterialGoodsConvertUnitDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialGoodsConvertUnit }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MaterialGoodsConvertUnitDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.materialGoodsConvertUnit = materialGoodsConvertUnit;
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
