import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICostSetMaterialGood } from 'app/shared/model/cost-set-material-good.model';
import { CostSetMaterialGoodService } from './cost-set-material-good.service';

@Component({
    selector: 'eb-cost-set-material-good-delete-dialog',
    templateUrl: './cost-set-material-good-delete-dialog.component.html'
})
export class CostSetMaterialGoodDeleteDialogComponent {
    costSetMaterialGood: ICostSetMaterialGood;

    constructor(
        private costSetMaterialGoodService: CostSetMaterialGoodService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.costSetMaterialGoodService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'costSetMaterialGoodListModification',
                content: 'Deleted an costSetMaterialGood'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-cost-set-material-good-delete-popup',
    template: ''
})
export class CostSetMaterialGoodDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ costSetMaterialGood }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CostSetMaterialGoodDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.costSetMaterialGood = costSetMaterialGood;
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
