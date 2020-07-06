import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ExpenseItemService } from './expense-item.service';

@Component({
    selector: 'eb-expense-item-delete-dialog',
    templateUrl: './expense-item-delete-dialog.component.html'
})
export class ExpenseItemDeleteDialogComponent {
    expenseItem: IExpenseItem;

    constructor(
        private expenseItemService: ExpenseItemService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.expenseItemService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'expenseItemListModification',
                content: 'Deleted an expenseItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-expense-item-delete-popup',
    template: ''
})
export class ExpenseItemDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ expenseItem }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ExpenseItemDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.expenseItem = expenseItem;
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
