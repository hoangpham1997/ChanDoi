import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { BudgetItemService } from './budget-item.service';

@Component({
    selector: 'eb-budget-item-delete-dialog',
    templateUrl: './budget-item-delete-dialog.component.html'
})
export class BudgetItemDeleteDialogComponent {
    budgetItem: IBudgetItem;

    constructor(private budgetItemService: BudgetItemService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.budgetItemService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'budgetItemListModification',
                content: 'Deleted an budgetItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-budget-item-delete-popup',
    template: ''
})
export class BudgetItemDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ budgetItem }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BudgetItemDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.budgetItem = budgetItem;
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
