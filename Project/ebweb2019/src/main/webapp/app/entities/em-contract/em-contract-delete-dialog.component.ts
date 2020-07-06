import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEMContract } from 'app/shared/model/em-contract.model';
import { EMContractService } from './em-contract.service';

@Component({
    selector: 'eb-em-contract-delete-dialog',
    templateUrl: './em-contract-delete-dialog.component.html'
})
export class EMContractDeleteDialogComponent {
    eMContract: IEMContract;

    constructor(private eMContractService: EMContractService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.eMContractService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'eMContractListModification',
                content: 'Deleted an eMContract'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-em-contract-delete-popup',
    template: ''
})
export class EMContractDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ eMContract }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(EMContractDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.eMContract = eMContract;
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
