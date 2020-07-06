import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaterialGoodsSpecialTaxGroup } from 'app/shared/model/material-goods-special-tax-group.model';
import { MaterialGoodsSpecialTaxGroupService } from './material-goods-special-tax-group.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
    selector: 'eb-material-goods-special-tax-group-delete-dialog',
    templateUrl: './material-goods-special-tax-group-delete-dialog.component.html'
})
export class MaterialGoodsSpecialTaxGroupDeleteDialogComponent {
    materialGoodsSpecialTaxGroup: IMaterialGoodsSpecialTaxGroup;

    constructor(
        private materialGoodsSpecialTaxGroupService: MaterialGoodsSpecialTaxGroupService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.materialGoodsSpecialTaxGroupService.delete(id).subscribe(
            (res: HttpResponse<any>) => {
                this.eventManager.broadcast({
                    name: 'materialGoodsSpecialTaxGroupListModification',
                    content: 'Deleted an materialGoodsSpecialTaxGroup'
                });
                this.activeModal.dismiss(true);
                this.toastr.success(this.translate.instant('ebwebApp.accountList.deleteSuccessful'));
            },
            (res: HttpErrorResponse) => {
                this.activeModal.dismiss(true);
                if (res.error.errorKey === 'errorDeleteParent') {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.accountList.errorDeleteParent'),
                        this.translate.instant('ebwebApp.accountList.error')
                    );
                } else if (res.error.errorKey === 'existConstraint') {
                    this.toastr.error(
                        this.translate.instant('ebwebApp.accountList.existConstraint'),
                        this.translate.instant('ebwebApp.accountList.error')
                    );
                } else {
                    this.toastr.error(this.translate.instant('ebwebApp.accountList.error'));
                }
                return;
            }
        );
        if (this.materialGoodsSpecialTaxGroup && this.materialGoodsSpecialTaxGroup.id) {
            this.router.navigate(['material-goods-special-tax-group']);
        } else {
            window.history.back();
        }
    }
}

@Component({
    selector: 'eb-material-goods-special-tax-group-delete-popup',
    template: ''
})
export class MaterialGoodsSpecialTaxGroupDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialGoodsSpecialTaxGroup }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MaterialGoodsSpecialTaxGroupDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.materialGoodsSpecialTaxGroup = materialGoodsSpecialTaxGroup;
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
