import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMBCreditCard } from 'app/shared/model/mb-credit-card.model';
import { MBCreditCardService } from './mb-credit-card.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from 'app/shared/base-component/base.component';

@Component({
    selector: 'eb-mb-credit-card-delete-dialog',
    templateUrl: './mb-credit-card-delete-dialog.component.html'
})
export class MBCreditCardDeleteDialogComponent extends BaseComponent {
    mBCreditCard: IMBCreditCard;

    constructor(
        private mBCreditCardService: MBCreditCardService,
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
        this.mBCreditCardService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'mBCreditCardListModification',
                content: 'Deleted an mBCreditCard'
            });
            this.activeModal.dismiss(true);
        });
        this.toastr.success(this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'));
        this.router.navigate(['mb-credit-card']);
    }
}

@Component({
    selector: 'eb-mb-credit-card-delete-popup',
    template: ''
})
export class MBCreditCardDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBCreditCard }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MBCreditCardDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.mBCreditCard = mBCreditCard;
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
