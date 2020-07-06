import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMCReceipt } from 'app/shared/model/mc-receipt.model';
import { MCReceiptService } from './mc-receipt.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HttpResponse } from '@angular/common/http';
import { TCKHAC_SDTichHopHDDT, TypeID } from 'app/app.constants';
import { SAInvoiceService } from 'app/ban-hang/ban_hang_chua_thu_tien';
import { Principal } from 'app/core';

@Component({
    selector: 'eb-mc-receipt-delete-dialog',
    templateUrl: './mc-receipt-delete-dialog.component.html'
})
export class MCReceiptDeleteDialogComponent {
    mCReceipt: IMCReceipt;
    PHIEU_THU_TU_BAN_HANG = TypeID.PHIEU_THU_TU_BAN_HANG;
    PHIEU_THU_TU_KIEM_KE_QUY: boolean;
    account: any;
    isTichHopHDDT: boolean;
    typeDelete: number;

    constructor(
        private mCReceiptService: MCReceiptService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router,
        private toastrService: ToastrService,
        private translateService: TranslateService,
        private sAInvoiceService: SAInvoiceService,
        private principal: Principal
    ) {
        this.principal.identity().then(account => {
            this.account = account;
            this.isTichHopHDDT = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
            if (this.mCReceipt.typeID === TypeID.PHIEU_THU_TU_BAN_HANG) {
                this.mCReceiptService.find(this.mCReceipt.id).subscribe((res: HttpResponse<IMCReceipt>) => {
                    this.mCReceipt = this.mCReceipt;
                    this.isTichHopHDDT = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
                    if (res.body.sAIsBill && res.body.sAInvoiceForm === 0 && this.isTichHopHDDT) {
                        if (res.body.sAInvoiceNo) {
                            this.typeDelete = 2;
                        } else {
                            this.typeDelete = 0;
                        }
                    } else if (res.body.sAIsBill) {
                        this.typeDelete = 1;
                    } else {
                        this.typeDelete = 0;
                    }
                });
            }
        });
    }

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string, typeID: number) {
        switch (typeID) {
            case TypeID.PHIEU_THU:
            case TypeID.PHIEU_THU_TIEN_KHACH_HANG:
                this.mCReceiptService.delete(id).subscribe(response => {
                    this.afterDeleteSuccess();
                });
                break;
            case TypeID.PHIEU_THU_TU_BAN_HANG: // phiếu thu từ bán hàng thu tiền ngay
                const sAInvoiceID = this.mCReceipt.sAInvoiceID;
                if (sAInvoiceID && this.typeDelete !== 2) {
                    this.sAInvoiceService.delete(sAInvoiceID).subscribe(response => {
                        this.afterDeleteSuccess();
                    });
                } else {
                    this.toastrService.error(this.translateService.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
                    this.activeModal.dismiss(true);
                }
                break;
            default:
                this.mCReceiptService.delete(id).subscribe(response => {
                    this.afterDeleteSuccess();
                });
                break;
        }
    }

    afterDeleteSuccess() {
        this.eventManager.broadcast({
            name: 'mCReceiptListModification',
            content: 'Deleted an mCReceipt'
        });
        this.toastrService.success(this.translateService.instant('ebwebApp.mCReceipt.deleted'));
        // this.router.navigate(['/mc-receipt']);
        this.activeModal.dismiss(true);
    }
}

@Component({
    selector: 'eb-mc-receipt-delete-popup',
    template: ''
})
export class MCReceiptDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mCReceipt }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MCReceiptDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.mCReceipt = mCReceipt;
                /*if (mCReceipt.mCAuditID) {
                    this.ngbModalRef.componentInstance.PHIEU_THU_TU_KIEM_KE_QUY = true;
                }*/
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
                                    this.router.navigate(['/mc-receipt']);
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
