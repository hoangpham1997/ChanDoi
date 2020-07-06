import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { BudgetItem } from 'app/shared/model/budget-item.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { BudgetItemService } from './budget-item.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-budget-item-delete-dialog',
    templateUrl: './budget-item-delete-dialog.component.html'
})
export class BudgetItemDeleteDialogComponent {
    budgetItem: BudgetItem;

    constructor(
        private budgetItemService: BudgetItemService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastr: ToastrService,
        private translate: TranslateService,
        private router: Router
    ) {}

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.budgetItemService.delete(id).subscribe(
            response => {
                this.eventManager.broadcast({
                    name: 'BudgetItem',
                    content: 'Deleted an budgetItem'
                });
                this.activeModal.dismiss(true);
                this.toastr.success(this.translate.instant('ebwebApp.budgetItem.deleteSuccessful'));
            },
            error => {
                this.activeModal.dismiss(true);
                this.toastr.error(this.translate.instant('ebwebApp.budgetItem.deleteError'));
            }
        );
        this.router.navigate(['budget-item']);
    }
}

@Component({
    selector: 'eb-transport-method-delete-popup',
    template: ''
})
export class BudgetItemDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ budgetItem }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BudgetItemDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
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
