import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { PaymentClauseService } from './payment-clause.service';

@Component({
    selector: 'eb-payment-clause-delete-dialog',
    templateUrl: './payment-clause-delete-dialog.component.html'
})
export class PaymentClauseDeleteDialogComponent {
    paymentClause: IPaymentClause;

    constructor(
        private paymentClauseService: PaymentClauseService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: any) {
        this.paymentClauseService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'paymentClauseListModification',
                content: 'Deleted an paymentClause'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-payment-clause-delete-popup',
    template: ''
})
export class PaymentClauseDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ paymentClause }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PaymentClauseDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.paymentClause = paymentClause;
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
