import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMBDeposit } from 'app/shared/model/mb-deposit.model';
import { MBDepositService } from './mb-deposit.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-mb-deposit-delete-dialog',
    templateUrl: './mb-deposit-delete-dialog.component.html'
})
export class MBDepositDeleteDialogComponent extends BaseComponent {
    mBDeposit: IMBDeposit;

    constructor(
        private mBDepositService: MBDepositService,
        private router: Router,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {
        super();
    }

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.mBDepositService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'mBDepositListModification',
                content: 'Deleted an mBDeposit'
            });
            this.activeModal.dismiss(true);
        });
        this.toastr.success(this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'));
        this.router.navigate(['mb-deposit']);
    }
}

@Component({
    selector: 'eb-mb-deposit-delete-popup',
    template: ''
})
export class MBDepositDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBDeposit }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MBDepositDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.mBDeposit = mBDeposit;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], {
                            replaceUrl: true,
                            queryParamsHandling: 'merge'
                        });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], {
                            replaceUrl: true,
                            queryParamsHandling: 'merge'
                        });
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
