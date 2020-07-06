import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { AccountingObjectBankAccountService } from './accounting-object-bank-account.service';

@Component({
    selector: 'eb-accounting-object-bank-account-delete-dialog',
    templateUrl: './accounting-object-bank-account-delete-dialog.component.html'
})
export class AccountingObjectBankAccountDeleteDialogComponent {
    accountingObjectBankAccount: IAccountingObjectBankAccount;

    constructor(
        private accountingObjectBankAccountService: AccountingObjectBankAccountService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.accountingObjectBankAccountService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'accountingObjectBankAccountListModification',
                content: 'Deleted an accountingObjectBankAccount'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-accounting-object-bank-account-delete-popup',
    template: ''
})
export class AccountingObjectBankAccountDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ accountingObjectBankAccount }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AccountingObjectBankAccountDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.accountingObjectBankAccount = accountingObjectBankAccount;
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
