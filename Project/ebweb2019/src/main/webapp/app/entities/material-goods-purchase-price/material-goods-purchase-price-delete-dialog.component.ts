import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaterialGoodsPurchasePrice } from 'app/shared/model/material-goods-purchase-price.model';
import { MaterialGoodsPurchasePriceService } from './material-goods-purchase-price.service';

@Component({
    selector: 'eb-material-goods-purchase-price-delete-dialog',
    templateUrl: './material-goods-purchase-price-delete-dialog.component.html'
})
export class MaterialGoodsPurchasePriceDeleteDialogComponent {
    materialGoodsPurchasePrice: IMaterialGoodsPurchasePrice;

    constructor(
        private materialGoodsPurchasePriceService: MaterialGoodsPurchasePriceService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.materialGoodsPurchasePriceService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'materialGoodsPurchasePriceListModification',
                content: 'Deleted an materialGoodsPurchasePrice'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-material-goods-purchase-price-delete-popup',
    template: ''
})
export class MaterialGoodsPurchasePriceDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialGoodsPurchasePrice }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MaterialGoodsPurchasePriceDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.materialGoodsPurchasePrice = materialGoodsPurchasePrice;
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
