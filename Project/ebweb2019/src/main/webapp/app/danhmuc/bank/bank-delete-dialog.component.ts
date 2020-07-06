import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBank } from 'app/shared/model/bank.model';
import { BankService } from './bank.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'eb-bank-delete-dialog',
    templateUrl: './bank-delete-dialog.component.html'
})
export class BankDeleteDialogComponent {
    bank: IBank;

    constructor(
        private bankService: BankService,
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
        this.bankService.delete(id).subscribe(
            response => {
                this.eventManager.broadcast({
                    name: 'bankListModification',
                    content: 'Deleted an bank'
                });
                this.activeModal.dismiss(true);
                this.toastr.success(this.translate.instant('ebwebApp.bank.deleteSuccess'));
            },
            (res: HttpErrorResponse) => {
                this.onError(res.error.title);
            }
        );
        this.router.navigate(['bank']);
    }

    private onError(errorMessage: string) {
        this.toastr.error(errorMessage, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        this.clear();
    }
}

@Component({
    selector: 'eb-bank-delete-popup',
    template: ''
})
export class BankDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ bank }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BankDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.bank = bank;
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
