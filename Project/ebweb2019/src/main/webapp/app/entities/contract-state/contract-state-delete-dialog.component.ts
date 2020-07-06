import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IContractState } from 'app/shared/model/contract-state.model';
import { ContractStateService } from './contract-state.service';

@Component({
    selector: 'eb-contract-state-delete-dialog',
    templateUrl: './contract-state-delete-dialog.component.html'
})
export class ContractStateDeleteDialogComponent {
    contractState: IContractState;

    constructor(
        private contractStateService: ContractStateService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.contractStateService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'contractStateListModification',
                content: 'Deleted an contractState'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-contract-state-delete-popup',
    template: ''
})
export class ContractStateDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ contractState }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ContractStateDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.contractState = contractState;
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
