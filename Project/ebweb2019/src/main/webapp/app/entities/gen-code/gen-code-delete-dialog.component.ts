import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGenCode } from 'app/shared/model/gen-code.model';
import { GenCodeService } from './gen-code.service';

@Component({
    selector: 'eb-gen-code-delete-dialog',
    templateUrl: './gen-code-delete-dialog.component.html'
})
export class GenCodeDeleteDialogComponent {
    genCode: IGenCode;

    constructor(private genCodeService: GenCodeService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.genCodeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'genCodeListModification',
                content: 'Deleted an genCode'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-gen-code-delete-popup',
    template: ''
})
export class GenCodeDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ genCode }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(GenCodeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.genCode = genCode;
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
