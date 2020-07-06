import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPrepaidExpenseAllocation } from 'app/shared/model/prepaid-expense-allocation.model';
import { PrepaidExpenseAllocationService } from './prepaid-expense-allocation.service';

@Component({
    selector: 'eb-prepaid-expense-allocation-delete-dialog',
    templateUrl: './prepaid-expense-allocation-delete-dialog.component.html'
})
export class PrepaidExpenseAllocationDeleteDialogComponent {
    prepaidExpenseAllocation: IPrepaidExpenseAllocation;

    constructor(
        private prepaidExpenseAllocationService: PrepaidExpenseAllocationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.prepaidExpenseAllocationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'prepaidExpenseAllocationListModification',
                content: 'Deleted an prepaidExpenseAllocation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-prepaid-expense-allocation-delete-popup',
    template: ''
})
export class PrepaidExpenseAllocationDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ prepaidExpenseAllocation }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PrepaidExpenseAllocationDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.prepaidExpenseAllocation = prepaidExpenseAllocation;
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
