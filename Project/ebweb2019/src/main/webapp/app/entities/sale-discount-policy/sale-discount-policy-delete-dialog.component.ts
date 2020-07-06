import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';
import { SaleDiscountPolicyService } from './sale-discount-policy.service';

@Component({
    selector: 'eb-sale-discount-policy-delete-dialog',
    templateUrl: './sale-discount-policy-delete-dialog.component.html'
})
export class SaleDiscountPolicyDeleteDialogComponent {
    saleDiscountPolicy: ISaleDiscountPolicy;

    constructor(
        private saleDiscountPolicyService: SaleDiscountPolicyService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.saleDiscountPolicyService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'saleDiscountPolicyListModification',
                content: 'Deleted an saleDiscountPolicy'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-sale-discount-policy-delete-popup',
    template: ''
})
export class SaleDiscountPolicyDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ saleDiscountPolicy }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SaleDiscountPolicyDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.saleDiscountPolicy = saleDiscountPolicy;
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
