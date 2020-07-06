import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { GOtherVoucherService } from './g-other-voucher.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-g-other-voucher-delete-dialog',
    templateUrl: './g-other-voucher-delete-dialog.component.html'
})
export class GOtherVoucherDeleteDialogComponent extends BaseComponent {
    gOtherVoucher: IGOtherVoucher;

    constructor(
        private gOtherVoucherService: GOtherVoucherService,
        public activeModal: NgbActiveModal,
        private router: Router,
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
        this.gOtherVoucherService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'gOtherVoucherListModification',
                content: 'Deleted an gOtherVoucher'
            });
            this.activeModal.dismiss(true);
        });
        this.toastr.success(
            this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
        this.router.navigate(['g-other-voucher']);
    }
}

@Component({
    selector: 'eb-g-other-voucher-delete-popup',
    template: ''
})
export class GOtherVoucherDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ gOtherVoucher }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(GOtherVoucherDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.gOtherVoucher = gOtherVoucher;
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
