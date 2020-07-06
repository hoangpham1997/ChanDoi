import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPporder } from 'app/shared/model/pporder.model';
import { PporderService } from 'app/entities/pporder';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { ROLE } from 'app/role.constants';

@Component({
    selector: 'eb-don-mua-hang-delete-dialog',
    templateUrl: './don-mua-hang-delete-dialog.component.html'
})
export class DonMuaHangDeleteDialogComponent {
    ppOrder: IPporder;

    ROLE_XEM = ROLE.DonMuaHang_XEM;
    ROLE_THEM = ROLE.DonMuaHang_THEM;
    ROLE_SUA = ROLE.DonMuaHang_SUA;
    ROLE_XOA = ROLE.DonMuaHang_XOA;
    ROLE_IN = ROLE.DonMuaHang_IN;
    ROLE_KETXUAT = ROLE.DonMuaHang_KET_XUAT;

    constructor(
        private pPOrderService: PporderService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastrService: ToastrService,
        private route: Router,
        private translateService: TranslateService
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.pPOrderService.deleteValidate(id).subscribe(response => {
            if (response.body.message === 'success') {
                this.eventManager.broadcast({
                    name: 'PPOrderListModification',
                    content: 'Deleted an PPOrder'
                });
                this.activeModal.dismiss(true);
                this.toastrService.success(this.translateService.instant('ebwebApp.pporder.deleted'));
            }
            // else {
            //     this.activeModal.dismiss('cancel');
            //     this.eventManager.broadcast({
            //         name: 'duplicateRefPPOrder'
            //     });
            // }
        });
    }
}

@Component({
    selector: 'eb-don-mua-hang-delete-popup',
    template: ''
})
export class DonMuaHangDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(data => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DonMuaHangDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.ppOrder = data.ppOrder;
                this.ngbModalRef.result.then(
                    result => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => this.router.navigate(['don-mua-hang']));
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => this.router.navigate(['don-mua-hang']));
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
