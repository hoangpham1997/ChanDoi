import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaterialGoodsCategory } from 'app/shared/model/material-goods-category.model';
import { MaterialGoodsCategoryService } from './material-goods-category.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'eb-material-goods-category-delete-dialog',
    templateUrl: './material-goods-category-delete-dialog.component.html'
})
export class MaterialGoodsCategoryDeleteDialogComponent {
    materialGoodsCategory: IMaterialGoodsCategory;

    constructor(
        private materialGoodsCategoryService: MaterialGoodsCategoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {}

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.materialGoodsCategoryService.delete(id).subscribe(
            response => {
                this.eventManager.broadcast({
                    name: 'materialGoodsCategoryListModification',
                    content: 'Deleted an materialGoodsCategory'
                });
                this.activeModal.dismiss(true);
                this.toastr.success(this.translate.instant('ebwebApp.materialGoodsCategory.delete.deleteSuccess'));
            },
            (res: HttpErrorResponse) => {
                this.onError(res.error.title);
            }
        );
        this.router.navigate(['material-goods-category']);
    }

    private onError(errorMessage: string) {
        this.toastr.error(errorMessage, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        this.clear();
    }
}

@Component({
    selector: 'eb-material-goods-category-delete-popup',
    template: ''
})
export class MaterialGoodsCategoryDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialGoodsCategory }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MaterialGoodsCategoryDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.materialGoodsCategory = materialGoodsCategory;
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
