import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITransportMethod } from 'app/shared/model/transport-method.model';
import { TransportMethod } from 'app/shared/model/transport-method.model';
import { TransportMethodService } from './transport-method.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'eb-transport-method-delete-dialog',
    templateUrl: './transport-method-delete-dialog.component.html'
})
export class TransportMethodDeleteDialogComponent {
    transportMethod: ITransportMethod;

    constructor(
        private transportMethodService: TransportMethodService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastr: ToastrService,
        private translate: TranslateService,
        private router: Router
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
        window.history.back();
    }

    confirmDelete(id: string) {
        this.transportMethodService.delete(id).subscribe(
            response => {
                this.eventManager.broadcast({
                    name: 'TransportMethod',
                    content: 'Deleted an TransportMethod'
                });
                this.activeModal.dismiss(true);
                this.toastr.success(this.translate.instant('ebwebApp.transportMethod.deleteSuccessful'));
            },
            error => {
                this.activeModal.dismiss(true);
                this.toastr.error(this.translate.instant('ebwebApp.transportMethod.deleteError'));
            }
        );
        this.router.navigate(['transport-method']);
    }
}

@Component({
    selector: 'eb-transport-method-delete-popup',
    template: ''
})
export class TransportMethodDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ transportMethod }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TransportMethodDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.transportMethod = transportMethod;
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
