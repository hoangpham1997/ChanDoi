import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITools } from 'app/shared/model/tools.model';
import { ToolsService } from './tools.service';

@Component({
    selector: 'eb-tools-delete-dialog',
    templateUrl: './tools-delete-dialog.component.html'
})
export class ToolsDeleteDialogComponent {
    tools: ITools;

    constructor(private toolsService: ToolsService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.toolsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'toolsListModification',
                content: 'Deleted an tools'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-tools-delete-popup',
    template: ''
})
export class ToolsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ tools }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ToolsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.tools = tools;
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
