import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { TypeID } from 'app/app.constants';
import { SAInvoiceService } from 'app/ban-hang/ban_hang_chua_thu_tien';
import { Principal } from 'app/core';
import { GhiTangService } from 'app/congcudungcu/ghi-tang-ccdc-khac/ghi-tang.service';
import { ITIIncrement } from 'app/shared/model/ti-increment.model';

@Component({
    selector: 'eb-ghi-tang-delete-dialog',
    templateUrl: './ghi-tang-delete-dialog.component.html'
})
export class GhiTangDeleteDialogComponent {
    increment: ITIIncrement;
    PHIEU_THU_TU_BAN_HANG = TypeID.PHIEU_THU_TU_BAN_HANG;
    PHIEU_THU_TU_KIEM_KE_QUY: boolean;
    account: any;
    isTichHopHDDT: boolean;
    typeDelete: number;

    constructor(
        private ghiTangService: GhiTangService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router,
        private toastrService: ToastrService,
        private translateService: TranslateService,
        private sAInvoiceService: SAInvoiceService,
        private principal: Principal
    ) {
        this.principal.identity(true).then(account => {
            this.account = account;
            // this.isTichHopHDDT = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
            // if (this.increment.typeID === TypeID.PHIEU_THU_TU_BAN_HANG) {
            //     this.ghiTangService.find(this.increment.id).subscribe((res: HttpResponse<IMCReceipt>) => {
            //         this.increment = this.increment;
            //         this.isTichHopHDDT = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
            //         if (res.body.sAIsBill && res.body.sAInvoiceForm === 0 && this.isTichHopHDDT) {
            //             if (res.body.sAInvoiceNo) {
            //                 this.typeDelete = 2;
            //             } else {
            //                 this.typeDelete = 0;
            //             }
            //         } else if (res.body.sAIsBill) {
            //             this.typeDelete = 1;
            //         } else {
            //             this.typeDelete = 0;
            //         }
            //     });
            // }
        });
    }

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string, typeID: number) {
        switch (typeID) {
            case TypeID.GHI_TANG_CCDC:
                this.ghiTangService.delete(id).subscribe(response => {
                    this.afterDeleteSuccess();
                });
                break;
            // case TypeID.PHIEU_THU_TU_BAN_HANG: // phiếu thu từ bán hàng thu tiền ngay
            //     const sAInvoiceID = this.increment.sAInvoiceID;
            //     if (sAInvoiceID && this.typeDelete !== 2) {
            //         this.sAInvoiceService.delete(sAInvoiceID).subscribe(response => {
            //             this.afterDeleteSuccess();
            //         });
            //     } else {
            //         this.toastrService.error(this.translateService.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
            //         this.activeModal.dismiss(true);
            //     }
            //     break;
            default:
                this.ghiTangService.delete(id).subscribe(response => {
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
    selector: 'eb-ghi-tang-delete-popup',
    template: ''
})
export class GhiTangDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ tiIncrement }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(GhiTangDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.increment = tiIncrement;
                console.log(tiIncrement);
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
                                    this.router.navigate(['/ghi-tang-ccdc']);
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
