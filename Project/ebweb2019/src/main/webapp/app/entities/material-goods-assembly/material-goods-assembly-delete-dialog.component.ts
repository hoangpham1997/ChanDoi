import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaterialGoodsAssembly } from 'app/shared/model/material-goods-assembly.model';
import { MaterialGoodsAssemblyService } from './material-goods-assembly.service';

@Component({
    selector: 'eb-material-goods-assembly-delete-dialog',
    templateUrl: './material-goods-assembly-delete-dialog.component.html'
})
export class MaterialGoodsAssemblyDeleteDialogComponent {
    materialGoodsAssembly: IMaterialGoodsAssembly;

    constructor(
        private materialGoodsAssemblyService: MaterialGoodsAssemblyService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.materialGoodsAssemblyService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'materialGoodsAssemblyListModification',
                content: 'Deleted an materialGoodsAssembly'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-material-goods-assembly-delete-popup',
    template: ''
})
export class MaterialGoodsAssemblyDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialGoodsAssembly }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MaterialGoodsAssemblyDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.materialGoodsAssembly = materialGoodsAssembly;
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
