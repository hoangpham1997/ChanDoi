import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICostSet } from 'app/shared/model/cost-set.model';
import { CostSetService } from './cost-set.service';

@Component({
    selector: 'eb-cost-set-delete-dialog',
    templateUrl: './cost-set-delete-dialog.component.html'
})
export class CostSetDeleteDialogComponent {
    costSet: ICostSet;

    constructor(private costSetService: CostSetService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.costSetService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'costSetListModification',
                content: 'Deleted an costSet'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-cost-set-delete-popup',
    template: ''
})
export class CostSetDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ costSet }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CostSetDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.costSet = costSet;
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
