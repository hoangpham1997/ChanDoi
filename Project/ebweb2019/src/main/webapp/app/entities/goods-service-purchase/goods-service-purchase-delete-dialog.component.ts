import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGoodsServicePurchase } from 'app/shared/model/goods-service-purchase.model';
import { GoodsServicePurchaseService } from './goods-service-purchase.service';

@Component({
    selector: 'eb-goods-service-purchase-delete-dialog',
    templateUrl: './goods-service-purchase-delete-dialog.component.html'
})
export class GoodsServicePurchaseDeleteDialogComponent {
    goodsServicePurchase: IGoodsServicePurchase;

    constructor(
        private goodsServicePurchaseService: GoodsServicePurchaseService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.goodsServicePurchaseService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'goodsServicePurchaseListModification',
                content: 'Deleted an goodsServicePurchase'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-goods-service-purchase-delete-popup',
    template: ''
})
export class GoodsServicePurchaseDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ goodsServicePurchase }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(GoodsServicePurchaseDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.goodsServicePurchase = goodsServicePurchase;
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
