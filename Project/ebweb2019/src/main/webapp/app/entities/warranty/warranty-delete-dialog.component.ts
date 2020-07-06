import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWarranty } from 'app/shared/model/warranty.model';
import { WarrantyService } from './warranty.service';

@Component({
    selector: 'eb-warranty-delete-dialog',
    templateUrl: './warranty-delete-dialog.component.html'
})
export class WarrantyDeleteDialogComponent {
    warranty: IWarranty;

    constructor(private warrantyService: WarrantyService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.warrantyService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'warrantyListModification',
                content: 'Deleted an warranty'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-warranty-delete-popup',
    template: ''
})
export class WarrantyDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ warranty }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(WarrantyDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.warranty = warranty;
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
