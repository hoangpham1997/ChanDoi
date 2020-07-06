import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPrepaidExpense } from 'app/shared/model/prepaid-expense.model';
import { PrepaidExpenseService } from './prepaid-expense.service';

@Component({
    selector: 'eb-prepaid-expense-delete-dialog',
    templateUrl: './prepaid-expense-delete-dialog.component.html'
})
export class PrepaidExpenseDeleteDialogComponent {
    prepaidExpense: IPrepaidExpense;

    constructor(
        private prepaidExpenseService: PrepaidExpenseService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.prepaidExpenseService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'prepaidExpenseListModification',
                content: 'Deleted an prepaidExpense'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-prepaid-expense-delete-popup',
    template: ''
})
export class PrepaidExpenseDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ prepaidExpense }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PrepaidExpenseDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.prepaidExpense = prepaidExpense;
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
