import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { Component, forwardRef, OnDestroy, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { JhiEventManager } from 'ng-jhipster';
import { StatisticsCodeService } from 'app/danhmuc/statistics-code/statistics-code.service';
import { TranslateService } from '@ngx-translate/core';
import { StatisticsCodeComponent } from 'app/danhmuc/statistics-code/statistics-code.component';
@Component({
    providers: [StatisticsCodeComponent],
    selector: 'eb-statistics-code-delete-dialog',
    templateUrl: './statistics-code-delete-dialog.component.html'
})
export class StatisticsCodeDeleteDialogComponent {
    statisticsCode: IStatisticsCode;

    constructor(
        private statisticsCodeService: StatisticsCodeService,
        private statisticsCodeComponent: StatisticsCodeComponent,
        private router: Router,
        private toastr: ToastrService,
        private translate: TranslateService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute
    ) {
        this.activatedRoute.data.subscribe(({ statisticsCode }) => (this.statisticsCode = statisticsCode));
    }

    clear() {
        window.history.back();
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        if (this.statisticsCode.isParentNode === true) {
            this.toastr.error(
                this.translate.instant('ebwebApp.statisticsCode.errorDeleteParent'),
                this.translate.instant('ebwebApp.paymentClause.error')
            );
            this.clear();
            return;
        }
        this.statisticsCodeService.delete(id).subscribe(
            response => {
                this.eventManager.broadcast({
                    name: 'statisticsCodesModification',
                    content: 'Deleted an statistics code'
                });
                this.activeModal.dismiss();
                this.toastr.success(
                    this.translate.instant('ebwebApp.statisticsCode.deleteSuccessfully'),
                    this.translate.instant('ebwebApp.statisticsCode.message')
                );
                this.clear();
            },
            error => {
                this.toastr.error(
                    this.translate.instant('ebwebApp.statisticsCode.existed'),
                    this.translate.instant('ebwebApp.paymentClause.error')
                );
                this.clear();
            }
        );
    }
}

@Component({
    // providers: [forwardRef(() => StatisticsCodeComponent)],
    selector: 'eb-statistics-code-delete-popup',
    template: ''
})
export class StatisticsCodeDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ statisticsCode }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(StatisticsCodeDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.statisticsCode = statisticsCode;
                this.ngbModalRef.result.then(
                    result => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => this.router.navigate(['statistics-code']));
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router
                            .navigate([{ outlets: { popup: null } }], {
                                replaceUrl: true,
                                queryParamsHandling: 'merge'
                            })
                            .then(() => this.router.navigate(['statistics-code']));
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
