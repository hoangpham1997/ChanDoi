import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISAInvoice } from 'app/shared/model/sa-invoice.model';
import { SAInvoiceService } from './sa-invoice.service';
import { BH_KIEM_HD, BH_KIEM_PX, NHAP_DON_GIA_VON, SO_LAM_VIEC, TCKHAC_SDSOQUANTRI, TCKHAC_SDTichHopHDDT, TypeID } from 'app/app.constants';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-sa-invoice-delete-dialog',
    templateUrl: './sa-invoice-delete-dialog.component.html'
})
export class SAInvoiceDeleteDialogComponent implements OnInit {
    sAInvoice: ISAInvoice;
    isTichHopHDDT: boolean;
    account: any;
    typeDelete: number;

    constructor(
        private sAInvoiceService: SAInvoiceService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private toasService: ToastrService,
        public translateService: TranslateService
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
            this.isTichHopHDDT = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
            if (this.sAInvoice.isBill && this.sAInvoice.invoiceForm === 0 && this.isTichHopHDDT) {
                if (this.sAInvoice.invoiceNo) {
                    this.typeDelete = 2;
                } else {
                    this.typeDelete = 0;
                }
            } else if (this.sAInvoice.isBill) {
                this.typeDelete = 1;
            } else {
                this.typeDelete = 0;
            }
        });
    }

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: any) {
        if (this.typeDelete === 2) {
            this.toasService.error(this.translateService.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
        } else {
            this.sAInvoiceService.delete(id).subscribe(response => {
                this.eventManager.broadcast({
                    name: 'sAInvoiceListModification',
                    content: 'Deleted an sAInvoice'
                });
                this.toasService.success(this.translateService.instant('ebwebApp.mBTellerPaper.deleted'));
                this.activeModal.dismiss(true);
            });
        }
    }
}

@Component({
    selector: 'eb-sa-invoice-delete-popup',
    template: ''
})
export class SAInvoiceDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sAInvoice }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SAInvoiceDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.sAInvoice = sAInvoice;
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
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => {
                                if (reason === 'cancel') {
                                    return;
                                } else if (reason === true) {
                                    window.history.back();
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
