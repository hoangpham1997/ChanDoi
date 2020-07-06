import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from './unit.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'eb-unit-delete-dialog',
    templateUrl: './unit-delete-dialog.component.html'
})
export class UnitDeleteDialogComponent {
    unit: IUnit;

    constructor(
        private unitService: UnitService,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.unitService.delete(id).subscribe(
            response => {
                this.eventManager.broadcast({
                    name: 'unitListModification',
                    content: 'Deleted an unit'
                });
                this.activeModal.dismiss(true);
                this.toastr.success(this.translate.instant('ebwebApp.unit.delete.deleteSuccessful'));
            },
            (res: HttpErrorResponse) => {
                this.onError(res.error.title);
            }
        );
        this.router.navigate(['unit']);
    }

    private onError(errorMessage: string) {
        this.toastr.error(errorMessage, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        this.clear();
    }
}

@Component({
    selector: 'eb-unit-delete-popup',
    template: ''
})
export class UnitDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ unit }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(UnitDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.unit = unit;
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
