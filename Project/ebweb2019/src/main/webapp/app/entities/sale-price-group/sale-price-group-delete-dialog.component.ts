import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISalePriceGroup } from 'app/shared/model/sale-price-group.model';
import { SalePriceGroupService } from './sale-price-group.service';

@Component({
    selector: 'eb-sale-price-group-delete-dialog',
    templateUrl: './sale-price-group-delete-dialog.component.html'
})
export class SalePriceGroupDeleteDialogComponent {
    salePriceGroup: ISalePriceGroup;

    constructor(
        private salePriceGroupService: SalePriceGroupService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.salePriceGroupService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'salePriceGroupListModification',
                content: 'Deleted an salePriceGroup'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-sale-price-group-delete-popup',
    template: ''
})
export class SalePriceGroupDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ salePriceGroup }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SalePriceGroupDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.salePriceGroup = salePriceGroup;
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
