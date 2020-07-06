import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICPExpenseTranfer } from 'app/shared/model/cp-expense-tranfer.model';
import { CPExpenseTranferService } from './cp-expense-tranfer.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-cp-expense-tranfer-delete-dialog',
    templateUrl: './cp-expense-tranfer-delete-dialog.component.html'
})
export class CPExpenseTranferDeleteDialogComponent {
    cPExpenseTranfer: ICPExpenseTranfer;

    constructor(
        private cPExpenseTranferService: CPExpenseTranferService,
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
        this.cPExpenseTranferService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'cPExpenseTranferListModification',
                content: 'Deleted an cPExpenseTranfer'
            });
            this.activeModal.dismiss(true);
        });
        this.toastr.success(
            this.translate.instant('ebwebApp.mBDeposit.deleteSuccessful'),
            this.translate.instant('ebwebApp.mBDeposit.message')
        );
        this.router.navigate(['ket-chuyen-chi-phi']);
    }
}

@Component({
    selector: 'eb-cp-expense-tranfer-delete-popup',
    template: ''
})
export class CPExpenseTranferDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cPExpenseTranfer }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CPExpenseTranferDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.cPExpenseTranfer = cPExpenseTranfer;
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
