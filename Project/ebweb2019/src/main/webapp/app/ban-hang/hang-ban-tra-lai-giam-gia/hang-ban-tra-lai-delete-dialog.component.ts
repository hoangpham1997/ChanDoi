import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMCReceipt } from 'app/shared/model/mc-receipt.model';
import { SaBillService } from 'app/ban-hang/xuat-hoa-don/sa-bill.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { SaReturnService } from 'app/ban-hang/hang-ban-tra-lai-giam-gia/sa-return.service';
import { TCKHAC_SDTichHopHDDT } from 'app/app.constants';
import { Principal } from 'app/core';
import { ISaReturn } from 'app/shared/model/sa-return.model';

@Component({
    selector: 'eb-hang-ban-tra-lai-delete-dialog',
    templateUrl: './hang-ban-tra-lai-delete-dialog.component.html'
})
export class HangBanTraLaiDeleteDialogComponent implements OnInit {
    deleteOject: ISaReturn;
    isTichHopHDDT: boolean;
    account: any;
    typeDelete: number;

    constructor(
        private saReturnService: SaReturnService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private principal: Principal,
        private toastrService: ToastrService,
        private translateService: TranslateService
    ) {}

    ngOnInit() {
        this.principal.identity(true).then(account => {
            this.account = account;
            this.isTichHopHDDT = this.account.systemOption.some(x => x.code === TCKHAC_SDTichHopHDDT && x.data === '1');
            if (this.deleteOject.isBill && this.deleteOject.invoiceForm === 0 && this.isTichHopHDDT) {
                if (this.deleteOject.invoiceNo) {
                    this.typeDelete = 2;
                } else {
                    this.typeDelete = 0;
                }
            } else if (this.deleteOject.isBill) {
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

    confirmDelete(id: string) {
        if (this.typeDelete === 2) {
            this.toastrService.error(this.translateService.instant('ebwebApp.sAInvoice.delete.notDeleteInvoice'));
        } else {
            this.saReturnService.delete(id).subscribe(response => {
                this.eventManager.broadcast({
                    name: 'hangBanTraLaiModification',
                    content: 'Hàng bán trả lại'
                });
                this.toastrService.success(this.translateService.instant('ebwebApp.saReturn.deleted'));
                this.activeModal.dismiss(true);
            });
        }
    }
}

@Component({
    selector: 'eb-hang-ban-tra-lai-delete-popup',
    template: ''
})
export class HangBanTraLainDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;
    private rsDataSession: any;
    private isFromRSInwardOutward: boolean;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.getSessionDataRS();
        this.activatedRoute.data.subscribe(({ deleteDTO }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(HangBanTraLaiDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.deleteOject = deleteDTO.saReturn;
                this.ngbModalRef.result.then(
                    result => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' })
                            .then(() => {
                                if (this.isFromRSInwardOutward) {
                                    this.backToRS();
                                }
                            });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' })
                            .then(() => {
                                if (this.isFromRSInwardOutward) {
                                    this.backToRS();
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

    backToRS() {
        sessionStorage.removeItem('nhapKhoDataSession');
        this.router.navigate(
            ['/nhap-kho'],
            this.rsDataSession
                ? {
                      queryParams: {
                          page: this.rsDataSession.page,
                          size: this.rsDataSession.itemsPerPage,
                          sort: this.rsDataSession.predicate + ',' + (this.rsDataSession.reverse ? 'asc' : 'desc')
                      }
                  }
                : null
        );
    }

    getSessionDataRS() {
        this.rsDataSession = JSON.parse(sessionStorage.getItem('nhapKhoDataSession'));
        if (this.rsDataSession) {
            this.isFromRSInwardOutward = true;
        } else {
            this.rsDataSession = null;
        }
    }
}
