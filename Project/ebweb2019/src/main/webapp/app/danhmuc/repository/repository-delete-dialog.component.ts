import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRepository } from 'app/shared/model/repository.model';
import { RepositoryService } from './repository.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'eb-repository-delete-dialog',
    templateUrl: './repository-delete-dialog.component.html'
})
export class RepositoryDeleteDialogComponent {
    repository: IRepository;
    modalRef: NgbModalRef;

    constructor(
        private repositoryService: RepositoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.repositoryService.delete(id).subscribe(
            response => {
                this.eventManager.broadcast({
                    name: 'repositoryListModification',
                    content: 'Deleted an repository'
                });
                this.activeModal.dismiss(true);
                this.toastr.success(this.translate.instant('ebwebApp.unit.delete.deleteSuccessful'));
                this.router.navigate(['repository']);
            },
            (res: HttpErrorResponse) => {
                this.onError(res.error.title);
            }
        );
        this.router.navigate(['repository']);
    }
    private onError(errorMessage: string) {
        this.toastr.error(errorMessage, this.translate.instant('ebwebApp.mCReceipt.error.error'));
        this.clear();
    }
}

@Component({
    selector: 'eb-repository-delete-popup',
    template: ''
})
export class RepositoryDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ repository }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RepositoryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.repository = repository;
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
