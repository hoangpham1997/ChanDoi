import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICPOPN } from 'app/shared/model/cpopn.model';
import { CPOPNService } from './cpopn.service';

@Component({
    selector: 'eb-cpopn-delete-dialog',
    templateUrl: './cpopn-delete-dialog.component.html'
})
export class CPOPNDeleteDialogComponent {
    cPOPN: ICPOPN;

    constructor(private cPOPNService: CPOPNService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: any) {
        this.cPOPNService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'cPOPNListModification',
                content: 'Deleted an cPOPN'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-cpopn-delete-popup',
    template: ''
})
export class CPOPNDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cPOPN }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CPOPNDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.cPOPN = cPOPN;
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
