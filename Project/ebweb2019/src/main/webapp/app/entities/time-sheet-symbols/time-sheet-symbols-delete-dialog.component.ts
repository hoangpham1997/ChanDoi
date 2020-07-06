import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITimeSheetSymbols } from 'app/shared/model/time-sheet-symbols.model';
import { TimeSheetSymbolsService } from './time-sheet-symbols.service';

@Component({
    selector: 'eb-time-sheet-symbols-delete-dialog',
    templateUrl: './time-sheet-symbols-delete-dialog.component.html'
})
export class TimeSheetSymbolsDeleteDialogComponent {
    timeSheetSymbols: ITimeSheetSymbols;

    constructor(
        private timeSheetSymbolsService: TimeSheetSymbolsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.timeSheetSymbolsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'timeSheetSymbolsListModification',
                content: 'Deleted an timeSheetSymbols'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-time-sheet-symbols-delete-popup',
    template: ''
})
export class TimeSheetSymbolsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ timeSheetSymbols }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TimeSheetSymbolsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.timeSheetSymbols = timeSheetSymbols;
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
