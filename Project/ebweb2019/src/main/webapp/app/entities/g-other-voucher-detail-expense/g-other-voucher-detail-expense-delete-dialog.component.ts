import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGOtherVoucherDetailExpense } from 'app/shared/model/g-other-voucher-detail-expense.model';
import { GOtherVoucherDetailExpenseService } from './g-other-voucher-detail-expense.service';

@Component({
    selector: 'eb-g-other-voucher-detail-expense-delete-dialog',
    templateUrl: './g-other-voucher-detail-expense-delete-dialog.component.html'
})
export class GOtherVoucherDetailExpenseDeleteDialogComponent {
    gOtherVoucherDetailExpense: IGOtherVoucherDetailExpense;

    constructor(
        private gOtherVoucherDetailExpenseService: GOtherVoucherDetailExpenseService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.gOtherVoucherDetailExpenseService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'gOtherVoucherDetailExpenseListModification',
                content: 'Deleted an gOtherVoucherDetailExpense'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-g-other-voucher-detail-expense-delete-popup',
    template: ''
})
export class GOtherVoucherDetailExpenseDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ gOtherVoucherDetailExpense }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(GOtherVoucherDetailExpenseDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.gOtherVoucherDetailExpense = gOtherVoucherDetailExpense;
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
