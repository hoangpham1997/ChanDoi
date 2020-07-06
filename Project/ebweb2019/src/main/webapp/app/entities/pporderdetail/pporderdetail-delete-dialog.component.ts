import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPporderdetail } from 'app/shared/model/pporderdetail.model';
import { PporderdetailService } from './pporderdetail.service';

@Component({
    selector: 'eb-pporderdetail-delete-dialog',
    templateUrl: './pporderdetail-delete-dialog.component.html'
})
export class PporderdetailDeleteDialogComponent {
    pporderdetail: IPporderdetail;

    constructor(
        private pporderdetailService: PporderdetailService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: any) {
        this.pporderdetailService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'pporderdetailListModification',
                content: 'Deleted an pporderdetail'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-pporderdetail-delete-popup',
    template: ''
})
export class PporderdetailDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pporderdetail }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PporderdetailDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.pporderdetail = pporderdetail;
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
