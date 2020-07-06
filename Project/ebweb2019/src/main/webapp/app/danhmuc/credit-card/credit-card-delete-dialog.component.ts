import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICreditCard } from 'app/shared/model/credit-card.model';
import { CreditCardService } from './credit-card.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'eb-credit-card-delete-dialog',
    templateUrl: './credit-card-delete-dialog.component.html'
})
export class CreditCardDeleteDialogComponent {
    creditCard: ICreditCard;

    constructor(
        private creditCardService: CreditCardService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {}

    clear() {
        this.activeModal.dismiss(true);
    }

    confirmDelete(id: string) {
        this.creditCardService.delete(id).subscribe(
            response => {
                this.eventManager.broadcast({
                    name: 'creditCardListModification',
                    content: 'Deleted an creditCard'
                });
                this.activeModal.dismiss(true);
                this.toastr.success(this.translate.instant('ebwebApp.creditCard.deleteSuccess'));
            },
            (res: HttpErrorResponse) => {
                this.onError(res.error.title);
            }
        );
        this.router.navigate(['/credit-card']);
    }
    private onError(errorMessage: string) {
        this.toastr.error(errorMessage, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        this.clear();
    }
}

@Component({
    selector: 'eb-credit-card-delete-popup',
    template: ''
})
export class CreditCardDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ creditCard }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CreditCardDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.creditCard = creditCard;
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
