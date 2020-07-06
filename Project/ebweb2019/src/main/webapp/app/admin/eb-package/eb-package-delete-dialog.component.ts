import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEbPackage } from 'app/shared/model/eb-package.model';
import { EbPackageService } from './eb-package.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-eb-package-delete-dialog',
    templateUrl: './eb-package-delete-dialog.component.html'
})
export class EbPackageDeleteDialogComponent {
    ebPackage: IEbPackage;

    constructor(
        private ebPackageService: EbPackageService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastr: ToastrService,
        private translate: TranslateService
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.ebPackageService.delete(id).subscribe(response => {
            this.toastr.success(
                this.translate.instant('ebwebApp.organizationUnit.deleteSuccess'),
                this.translate.instant('ebwebApp.organizationUnit.message')
            );
            this.eventManager.broadcast({
                name: 'ebPackageListModification',
                content: 'Deleted an ebPackage'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-eb-package-delete-popup',
    template: ''
})
export class EbPackageDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ ebPackage }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(EbPackageDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.ebPackage = ebPackage;
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
