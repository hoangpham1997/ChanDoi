import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISAQuote } from 'app/shared/model/sa-quote.model';
import { SAQuoteService } from './sa-quote.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'eb-sa-quote-delete-dialog',
    templateUrl: './sa-quote-delete-dialog.component.html'
})
export class SAQuoteDeleteDialogComponent {
    sAQuote: ISAQuote;
    hasRef: boolean;

    constructor(
        private sAQuoteService: SAQuoteService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastrService: ToastrService,
        private translateService: TranslateService
    ) {}

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.sAQuoteService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'sAQuoteListModification',
                content: 'Deleted an sAQuote'
            });
            this.toastrService.success(this.translateService.instant('ebwebApp.mBTellerPaper.deleted'));
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-sa-quote-delete-popup',
    template: ''
})
export class SAQuoteDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private modalService: NgbModal,
        private sAQuoteService: SAQuoteService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sAQuote }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SAQuoteDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.sAQuote = sAQuote;
                this.sAQuoteService.checkRelateVoucher({ id: sAQuote.id }).subscribe((response: HttpResponse<boolean>) => {
                    this.ngbModalRef.componentInstance.hasRef = response.body;
                });
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], {
                            replaceUrl: true,
                            queryParamsHandling: 'merge'
                        });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' })
                            .then(() => {
                                if (reason === 'cancel') {
                                    return;
                                } else if (reason === true) {
                                    this.router.navigate(['/sa-quote']);
                                }
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
