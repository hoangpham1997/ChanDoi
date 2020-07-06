import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaterialQuantum } from 'app/shared/model/material-quantum.model';
import { MaterialQuantumService } from './material-quantum.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-material-quantum-delete-dialog',
    templateUrl: './material-quantum-delete-dialog.component.html'
})
export class MaterialQuantumDeleteDialogComponent {
    @ViewChild('popupDelete') popupDelete: TemplateRef<any>;
    materialQuantum: IMaterialQuantum;
    modalRef: NgbModalRef;

    constructor(
        private materialQuantumService: MaterialQuantumService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastr: ToastrService,
        private translateService: TranslateService,
        private router: Router,
        private modalService: NgbModal
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
        window.history.back();
    }

    checkBeforeDelete() {
        this.activeModal.dismiss(true);
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.modalRef = this.modalService.open(this.popupDelete, { backdrop: 'static' });
    }

    confirmDelete(id: string) {
        if (this.modalRef) {
            this.modalRef.close();
        }
        this.materialQuantumService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'materialQuantumListModification',
                content: 'Deleted an materialQuantum'
            });
            this.activeModal.dismiss(true);
        });
        this.toastr.success(
            this.translateService.instant('ebwebApp.materialQuantum.deleteSuccess'),
            this.translateService.instant('ebwebApp.materialQuantum.message')
        );
        this.router.navigate(['material-quantum']);
    }

    exit() {
        if (this.modalRef) {
            this.modalRef.close();
        }
    }
}

@Component({
    selector: 'eb-material-quantum-delete-popup',
    template: ''
})
export class MaterialQuantumDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ materialQuantum }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MaterialQuantumDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.materialQuantum = materialQuantum;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], {
                            replaceUrl: true,
                            queryParamsHandling: 'merge'
                        });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], {
                            replaceUrl: true,
                            queryParamsHandling: 'merge'
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
