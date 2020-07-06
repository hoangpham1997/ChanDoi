import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICPResult } from 'app/shared/model/cp-result.model';
import { CPResultService } from './cp-result.service';

@Component({
    selector: 'eb-cp-result-delete-dialog',
    templateUrl: './cp-result-delete-dialog.component.html'
})
export class CPResultDeleteDialogComponent {
    cPResult: ICPResult;

    constructor(private cPResultService: CPResultService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.cPResultService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'cPResultListModification',
                content: 'Deleted an cPResult'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-cp-result-delete-popup',
    template: ''
})
export class CPResultDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cPResult }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CPResultDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.cPResult = cPResult;
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
