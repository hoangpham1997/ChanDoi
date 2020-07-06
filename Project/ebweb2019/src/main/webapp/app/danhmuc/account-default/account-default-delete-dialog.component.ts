import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAccountDefault } from 'app/shared/model/account-default.model';
import { AccountDefaultService } from './account-default.service';

@Component({
    selector: 'eb-account-default-delete-dialog',
    templateUrl: './account-default-delete-dialog.component.html'
})
export class AccountDefaultDeleteDialogComponent {
    accountDefault: IAccountDefault;

    constructor(
        private accountDefaultService: AccountDefaultService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.accountDefaultService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'accountDefaultListModification',
                content: 'Deleted an accountDefault'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-account-default-delete-popup',
    template: ''
})
export class AccountDefaultDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ accountDefault }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AccountDefaultDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.accountDefault = accountDefault;
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
