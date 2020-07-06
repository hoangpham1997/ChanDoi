import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPrepaidExpenseVoucher } from 'app/shared/model/prepaid-expense-voucher.model';
import { PrepaidExpenseVoucherService } from './prepaid-expense-voucher.service';

@Component({
    selector: 'eb-prepaid-expense-voucher-delete-dialog',
    templateUrl: './prepaid-expense-voucher-delete-dialog.component.html'
})
export class PrepaidExpenseVoucherDeleteDialogComponent {
    prepaidExpenseVoucher: IPrepaidExpenseVoucher;

    constructor(
        private prepaidExpenseVoucherService: PrepaidExpenseVoucherService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.prepaidExpenseVoucherService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'prepaidExpenseVoucherListModification',
                content: 'Deleted an prepaidExpenseVoucher'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-prepaid-expense-voucher-delete-popup',
    template: ''
})
export class PrepaidExpenseVoucherDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ prepaidExpenseVoucher }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PrepaidExpenseVoucherDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.prepaidExpenseVoucher = prepaidExpenseVoucher;
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
