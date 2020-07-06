import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISAOrder } from 'app/shared/model/sa-order.model';
import { SAOrderService } from './sa-order.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'eb-sa-order-delete-dialog',
    templateUrl: './sa-order-delete-dialog.component.html'
})
export class SAOrderDeleteDialogComponent {
    sAOrder: ISAOrder;
    hasRef: boolean;

    constructor(
        private sAOrderService: SAOrderService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastrService: ToastrService,
        private translateService: TranslateService
    ) {}

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.sAOrderService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'sAOrderListModification',
                content: 'Deleted an sAOrder'
            });
            this.toastrService.success(this.translateService.instant('ebwebApp.sAOrder.deleted'));
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-sa-order-delete-popup',
    template: ''
})
export class SAOrderDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private modalService: NgbModal,
        private sAOrderService: SAOrderService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sAOrder }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SAOrderDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.sAOrder = sAOrder;
                this.sAOrderService.checkRelateVoucher({ id: sAOrder.id }).subscribe((response: HttpResponse<boolean>) => {
                    this.ngbModalRef.componentInstance.hasRef = response.body;
                });
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
                            .navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' })
                            .then(() => {
                                if (reason === 'cancel') {
                                    return;
                                } else if (reason === true) {
                                    this.router.navigate(['/sa-order']);
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
