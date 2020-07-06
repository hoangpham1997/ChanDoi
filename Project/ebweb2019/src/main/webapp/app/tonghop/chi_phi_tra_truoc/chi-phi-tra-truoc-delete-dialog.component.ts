import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGOtherVoucher } from 'app/shared/model/g-other-voucher.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { ChiPhiTraTruocService } from 'app/tonghop/chi_phi_tra_truoc/chi-phi-tra-truoc.service';
import { IPrepaidExpense } from 'app/shared/model/prepaid-expense.model';

@Component({
    selector: 'eb-chi-phi-tra-truoc-delete-dialog',
    templateUrl: './chi-phi-tra-truoc-delete-dialog.component.html'
})
export class ChiPhiTraTruocDeleteDialogComponent {
    prepaidExpense: IPrepaidExpense;

    constructor(
        private chiPhiTraTruocService: ChiPhiTraTruocService,
        public activeModal: NgbActiveModal,
        private router: Router,
        private eventManager: JhiEventManager,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: any) {
        this.chiPhiTraTruocService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'ChiPhiTraTruoc',
                content: 'Deleted an chiPhiTraTruoc'
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
export class ChiPhiTraTruocDeleteDialogPopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ data }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ChiPhiTraTruocDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.prepaidExpense = data.prepaidExpense;
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
