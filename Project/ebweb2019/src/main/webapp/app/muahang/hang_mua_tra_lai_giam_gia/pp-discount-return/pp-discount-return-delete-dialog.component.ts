import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPPDiscountReturn } from 'app/shared/model/pp-discount-return.model';
import { PPDiscountReturnService } from './pp-discount-return.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { TCKHAC_SDTichHopHDDT } from 'app/app.constants';
import { Principal } from 'app/core';

@Component({
    selector: 'eb-pp-discount-return-delete-dialog',
    templateUrl: './pp-discount-return-delete-dialog.component.html'
})
export class PPDiscountReturnDeleteDialogComponent implements OnInit {
    pPDiscountReturn: IPPDiscountReturn;
    isTichHopHDDT: boolean;
    account: any;
    typeDelete: number;

    constructor(
        private pPDiscountReturnService: PPDiscountReturnService,
        public activeModal: NgbActiveModal,
        private toasService: ToastrService,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private translateService: TranslateService
    ) {}

    ngOnInit() {
        this.principal.identity(true).then(account => {
            this.account = account;
            this.isTichHopHDDT = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
            if (this.pPDiscountReturn.isBill && this.pPDiscountReturn.invoiceForm === 0 && this.isTichHopHDDT) {
                if (this.pPDiscountReturn.invoiceNo) {
                    this.typeDelete = 2;
                } else {
                    this.typeDelete = 0;
                }
            } else if (this.pPDiscountReturn.isBill) {
                this.typeDelete = 1;
            } else {
                this.typeDelete = 0;
            }
        });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: any) {
        if (this.typeDelete === 2) {
            this.toasService.error(this.translateService.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
        } else {
            this.pPDiscountReturnService.delete(id).subscribe(response => {
                this.toasService.success(this.translateService.instant('ebwebApp.pporder.deleted'));
                this.eventManager.broadcast({
                    name: 'pPDiscountReturnListModification',
                    content: 'Deleted an pPDiscountReturn'
                });
                this.activeModal.dismiss(true);
            });
        }
    }
}

@Component({
    selector: 'eb-pp-discount-return-delete-popup',
    template: ''
})
export class PPDiscountReturnDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pPDiscountReturn }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PPDiscountReturnDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.pPDiscountReturn = pPDiscountReturn;
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
