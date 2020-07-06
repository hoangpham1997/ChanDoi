import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGeneralLedger } from 'app/shared/model/general-ledger.model';
import { GeneralLedgerService } from './general-ledger.service';

@Component({
    selector: 'eb-general-ledger-delete-dialog',
    templateUrl: './general-ledger-delete-dialog.component.html'
})
export class GeneralLedgerDeleteDialogComponent {
    generalLedger: IGeneralLedger;

    constructor(
        private generalLedgerService: GeneralLedgerService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.generalLedgerService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'generalLedgerListModification',
                content: 'Deleted an generalLedger'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-general-ledger-delete-popup',
    template: ''
})
export class GeneralLedgerDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ generalLedger }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(GeneralLedgerDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.generalLedger = generalLedger;
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
