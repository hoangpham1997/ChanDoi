import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAutoPrinciple } from 'app/shared/model/auto-principle.model';
import { AutoPrincipleService } from './auto-principle.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-auto-principle-delete-dialog',
    templateUrl: './auto-principle-delete-dialog.component.html'
})
export class AutoPrincipleDeleteDialogComponent {
    autoPrinciple: IAutoPrinciple;

    constructor(
        private autoPrincipleService: AutoPrincipleService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
        window.history.back();
    }

    confirmDelete(id: string) {
        this.autoPrincipleService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'autoPrincipleListModification',
                content: 'Deleted an autoPrinciple'
            });
            this.activeModal.dismiss(true);
        });
        this.toastr.success(this.translate.instant('ebwebApp.autoPrinciple.deleteSuccess'));
        this.router.navigate(['auto-principle']);
    }
}

@Component({
    selector: 'eb-auto-principle-delete-popup',
    template: ''
})
export class AutoPrincipleDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ autoPrinciple }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AutoPrincipleDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.autoPrinciple = autoPrinciple;
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
