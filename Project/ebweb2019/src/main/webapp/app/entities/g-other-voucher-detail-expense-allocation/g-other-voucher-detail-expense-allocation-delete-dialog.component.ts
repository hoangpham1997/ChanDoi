import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGOtherVoucherDetailExpenseAllocation } from 'app/shared/model/g-other-voucher-detail-expense-allocation.model';
import { GOtherVoucherDetailExpenseAllocationService } from './g-other-voucher-detail-expense-allocation.service';

@Component({
    selector: 'eb-g-other-voucher-detail-expense-allocation-delete-dialog',
    templateUrl: './g-other-voucher-detail-expense-allocation-delete-dialog.component.html'
})
export class GOtherVoucherDetailExpenseAllocationDeleteDialogComponent {
    gOtherVoucherDetailExpenseAllocation: IGOtherVoucherDetailExpenseAllocation;

    constructor(
        private gOtherVoucherDetailExpenseAllocationService: GOtherVoucherDetailExpenseAllocationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.gOtherVoucherDetailExpenseAllocationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'gOtherVoucherDetailExpenseAllocationListModification',
                content: 'Deleted an gOtherVoucherDetailExpenseAllocation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-g-other-voucher-detail-expense-allocation-delete-popup',
    template: ''
})
export class GOtherVoucherDetailExpenseAllocationDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ gOtherVoucherDetailExpenseAllocation }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(GOtherVoucherDetailExpenseAllocationDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.gOtherVoucherDetailExpenseAllocation = gOtherVoucherDetailExpenseAllocation;
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
