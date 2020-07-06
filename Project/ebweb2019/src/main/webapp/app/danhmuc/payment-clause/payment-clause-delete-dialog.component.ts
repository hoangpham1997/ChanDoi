import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ToastrService } from 'ngx-toastr';
import { IPaymentClause } from 'app/shared/model/payment-clause.model';
import { PaymentClauseService } from './payment-clause.service';
import { TranslateService } from '@ngx-translate/core';
@Component({
    selector: 'eb-payment-clause-delete-dialog',
    templateUrl: './payment-clause-delete-dialog.component.html'
})
export class PaymentClauseDeleteDialogComponent {
    paymentClause: IPaymentClause;
    constructor(
        private paymentClauseService: PaymentClauseService,
        public activeModal: NgbActiveModal,
        private toastr: ToastrService,
        private translate: TranslateService,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: any) {
        this.paymentClauseService.delete(id).subscribe(
            response => {
                this.eventManager.broadcast({
                    name: 'paymentClauseListModification',
                    content: 'Deleted an paymentClause'
                });
                this.activeModal.dismiss();
                this.toastr.success(
                    this.translate.instant('ebwebApp.paymentClause.deleteSuccessfully'),
                    this.translate.instant('ebwebApp.paymentClause.message')
                );
            },
            error => {
                this.activeModal.dismiss(true);
                this.toastr.error(
                    this.translate.instant('ebwebApp.paymentClause.existed'),
                    this.translate.instant('ebwebApp.paymentClause.error')
                );
            }
        );
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
                        this.router
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => this.router.navigate(['payment-clause']));
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => this.router.navigate(['payment-clause']));
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
