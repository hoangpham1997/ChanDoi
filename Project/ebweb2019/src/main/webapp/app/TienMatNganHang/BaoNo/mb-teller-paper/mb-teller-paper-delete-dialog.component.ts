import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMBTellerPaper } from 'app/shared/model/mb-teller-paper.model';
import { MBTellerPaperService } from './mb-teller-paper.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';

@Component({
    selector: 'eb-mb-teller-paper-delete-dialog',
    templateUrl: './mb-teller-paper-delete-dialog.component.html'
})
export class MBTellerPaperDeleteDialogComponent {
    mBTellerPaper: IMBTellerPaper;

    constructor(
        private mBTellerPaperService: MBTellerPaperService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router,
        private toastrService: ToastrService,
        private translateService: TranslateService
    ) {}

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.mBTellerPaperService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'mBTellerPaperListModification',
                content: 'Deleted an mBTellerPaper'
            });
            this.toastrService.success(this.translateService.instant('ebwebApp.mBTellerPaper.deleted'));
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-mb-teller-paper-delete-popup',
    template: ''
})
export class MBTellerPaperDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mBTellerPaper }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(MBTellerPaperDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.mBTellerPaper = mBTellerPaper;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], {
                            replaceUrl: true,
                            queryParamsHandling: 'merge'
                        });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => {
                                if (reason === true) {
                                    this.router.navigate(['/mb-teller-paper']);
                                }
                            });
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
