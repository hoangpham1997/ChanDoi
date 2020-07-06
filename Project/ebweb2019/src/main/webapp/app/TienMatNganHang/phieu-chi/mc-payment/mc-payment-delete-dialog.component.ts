import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMCPayment } from 'app/shared/model/mc-payment.model';
import { MCPaymentService } from './mc-payment.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HttpResponse } from '@angular/common/http';
import { TypeID } from 'app/app.constants';
import { PPInvoiceService } from 'app/muahang/mua_hang_qua_kho';
import { MuaDichVuService } from 'app/muahang/mua-dich-vu/mua-dich-vu.service';
import { UpdateDataMessages } from 'app/muahang/mua-dich-vu/mua-dich-vu.constant';

@Component({
    selector: 'eb-mc-payment-delete-dialog',
    templateUrl: './mc-payment-delete-dialog.component.html'
})
export class MCPaymentDeleteDialogComponent {
    mCPayment: IMCPayment;
    PHIEU_CHI = TypeID.PHIEU_CHI;
    PHIEU_CHI_NHA_CUNG_CAP = TypeID.PHIEU_CHI_NHA_CUNG_CAP;
    PHIEU_CHI_TU_KIEM_KE_QUY: boolean;

    constructor(
        private mCPaymentService: MCPaymentService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router,
        private toastrService: ToastrService,
        private translateService: TranslateService,
        private pPInvoiceService: PPInvoiceService,
        private ppServiceService: MuaDichVuService
    ) {}

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string, typeID: number) {
        switch (typeID) {
            case TypeID.PHIEU_CHI:
            case TypeID.PHIEU_CHI_NHA_CUNG_CAP:
                this.mCPaymentService.delete(id).subscribe(response => {
                    this.afterDeleteSuccess();
                });
                break;
            case TypeID.PHIEU_CHI_MUA_DICH_VU:
                this.mCPaymentService.find(id).subscribe((res: HttpResponse<IMCPayment>) => {
                    const ppServiceID = res.body.ppServiceID;
                    if (ppServiceID) {
                        this.ppServiceService.deletePPServiceById({ id: ppServiceID }).subscribe(response => {
                            if (response.body.messages === UpdateDataMessages.DELETE_SUCCESS) {
                                this.afterDeleteSuccess();
                            } else if (response.body.messages === UpdateDataMessages.HAD_REFERENCE) {
                                this.toastrService.error(
                                    this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.hasReferenceSuccess')
                                );
                            }
                        });
                    }
                });

                break;
            case TypeID.PHIEU_CHI_MUA_QUA_KHO_VA_KHONG_QUA_KHO:
                this.mCPaymentService.find(id).subscribe((res: HttpResponse<IMCPayment>) => {
                    const ppInvocieID = res.body.ppInvocieID;
                    if (ppInvocieID) {
                        this.pPInvoiceService.deleteById({ id: ppInvocieID }).subscribe(response => {
                            this.afterDeleteSuccess();
                        });
                    }
                });
                break;
            default:
                this.mCPaymentService.delete(id).subscribe(response => {
                    this.afterDeleteSuccess();
                });
                break;
        }
    }

    afterDeleteSuccess() {
        this.eventManager.broadcast({
            name: 'mCPaymentListModification',
            content: 'Deleted an mCPayment'
        });
        this.toastrService.success(this.translateService.instant('ebwebApp.mCPayment.deleted'));
        this.activeModal.dismiss(true);
    }
}

@Component({
    selector: 'eb-mc-payment-delete-popup',
    template: ''
})
export class MCPaymentDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mCPayment }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MCPaymentDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.mCPayment = mCPayment;
                /*if (mCPayment.mCAuditID) {
                    this.ngbModalRef.componentInstance.PHIEU_CHI_TU_KIEM_KE_QUY = true;
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
                                    this.router.navigate(['/mc-payment']);
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
