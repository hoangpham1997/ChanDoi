import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRepositoryLedger } from 'app/shared/model/repository-ledger.model';
import { RepositoryLedgerService } from './repository-ledger.service';

@Component({
    selector: 'eb-repository-ledger-delete-dialog',
    templateUrl: './repository-ledger-delete-dialog.component.html'
})
export class RepositoryLedgerDeleteDialogComponent {
    repositoryLedger: IRepositoryLedger;

    constructor(
        private repositoryLedgerService: RepositoryLedgerService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.repositoryLedgerService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'repositoryLedgerListModification',
                content: 'Deleted an repositoryLedger'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-repository-ledger-delete-popup',
    template: ''
})
export class RepositoryLedgerDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ repositoryLedger }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RepositoryLedgerDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.repositoryLedger = repositoryLedger;
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
