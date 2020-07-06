import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAccountTransfer } from 'app/shared/model/account-transfer.model';
import { AccountTransferService } from './account-transfer.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-account-transfer-delete-dialog',
    templateUrl: './account-transfer-delete-dialog.component.html'
})
export class AccountTransferDeleteDialogComponent {
    accountTransfer: IAccountTransfer;

    constructor(
        private accountTransferService: AccountTransferService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastr: ToastrService,
        private translate: TranslateService,
        private router: Router
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
        window.history.back();
    }

    confirmDelete(id: string) {
        this.accountTransferService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'accountTransferListModification',
                content: 'Deleted an accountTransfer'
            });
            this.activeModal.dismiss(true);
        });
        this.toastr.success(this.translate.instant('ebwebApp.accountList.deleteSuccessful'));
        this.router.navigate(['account-transfer']);
    }
}

@Component({
    selector: 'eb-account-transfer-delete-popup',
    template: ''
})
export class AccountTransferDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ accountTransfer }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AccountTransferDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.accountTransfer = accountTransfer;
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
