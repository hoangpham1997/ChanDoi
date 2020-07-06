import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAccountingObjectGroup } from 'app/shared/model/accounting-object-group.model';
import { AccountingObjectGroupService } from './accounting-object-group.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'eb-accounting-object-group-delete-dialog',
    templateUrl: './accounting-object-group-delete-dialog.component.html'
})
export class AccountingObjectGroupDeleteDialogComponent {
    accountingObjectGroup: IAccountingObjectGroup;

    constructor(
        private accountingObjectGroupService: AccountingObjectGroupService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {}

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.accountingObjectGroupService.delete(id).subscribe(
            response => {
                this.eventManager.broadcast({
                    name: 'accountingObjectGroupListModification',
                    content: 'Deleted an accountingObjectGroup'
                });
                console.log(response);
                this.activeModal.dismiss(true);
                this.toastr.success(this.translate.instant('ebwebApp.accountingObjectGroup.delete.deleteSuccessful'));
                this.router.navigate(['accounting-object-group']);
            },
            (res: HttpErrorResponse) => {
                this.onError(res.error.title);
            }
        );
    }

    private onError(errorMessage: string) {
        this.toastr.error(errorMessage, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        this.clear();
    }
}

@Component({
    selector: 'eb-accounting-object-group-delete-popup',
    template: ''
})
export class AccountingObjectGroupDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ accountingObjectGroup }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AccountingObjectGroupDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.accountingObjectGroup = accountingObjectGroup;
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
