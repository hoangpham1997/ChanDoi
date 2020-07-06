import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMCReceipt } from 'app/shared/model/mc-receipt.model';
import { SaBillService } from 'app/ban-hang/xuat-hoa-don/sa-bill.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { TCKHAC_SDTichHopHDDT } from 'app/app.constants';
import { Principal } from 'app/core';
import { HttpResponse } from '@angular/common/http';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';

@Component({
    selector: 'eb-xuat-hoa-don-delete-dialog',
    templateUrl: './xuat-hoa-don-delete-dialog.component.html'
})
export class XuatHoaDonDeleteDialogComponent implements OnInit {
    xuatHoaDon: any;
    isTichHopHDDT: boolean;
    account: any;
    typeDelete: number;

    constructor(
        private saBill: SaBillService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private toastrService: ToastrService,
        private translateService: TranslateService,
        private saBillService: SaBillService
    ) {}

    ngOnInit() {
        this.principal.identity(true).then(account => {
            this.account = account;
            this.isTichHopHDDT = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
            this.saBillService
                .checkRelateVoucher({
                    sABillID: this.xuatHoaDon.id
                })
                .subscribe((res: HttpResponse<boolean>) => {
                    if (this.xuatHoaDon.invoiceForm === 0 && this.isTichHopHDDT) {
                        if (this.xuatHoaDon.invoiceNo) {
                            this.typeDelete = 3;
                        } else {
                            this.typeDelete = 0;
                        }
                    } else if (res.body) {
                        this.typeDelete = 2;
                    } else {
                        if (this.xuatHoaDon.invoiceNo) {
                            this.typeDelete = 1;
                        } else {
                            this.typeDelete = 0;
                        }
                    }
                });
        });
    }

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        if (this.typeDelete === 3) {
            this.toastrService.error(this.translateService.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
        } else {
            this.saBill.delete(id).subscribe(response => {
                this.eventManager.broadcast({
                    name: 'xuatHoaDonModification',
                    content: 'Xóa xuất hóa đơn'
                });
                this.toastrService.success(this.translateService.instant('ebwebApp.saBill.deleted'));
                this.activeModal.dismiss(true);
            });
        }
    }
}

@Component({
    selector: 'eb-xuat-hoa-don-delete-popup',
    template: ''
})
export class XuatHoaDonDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ xuatHoaDon }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(XuatHoaDonDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.xuatHoaDon = xuatHoaDon.saBill;
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
