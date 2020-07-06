import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { StatisticsCodeService } from './statistics-code.service';

@Component({
    selector: 'eb-statistics-code-delete-dialog',
    templateUrl: './statistics-code-delete-dialog.component.html'
})
export class StatisticsCodeDeleteDialogComponent {
    statisticsCode: IStatisticsCode;

    constructor(
        private statisticsCodeService: StatisticsCodeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.statisticsCodeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'statisticsCodeListModification',
                content: 'Deleted an statisticsCode'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
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
