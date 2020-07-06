import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICareerGroup } from 'app/shared/model/career-group.model';
import { CareerGroupService } from './career-group.service';

@Component({
    selector: 'eb-career-group-delete-dialog',
    templateUrl: './career-group-delete-dialog.component.html'
})
export class CareerGroupDeleteDialogComponent {
    careerGroup: ICareerGroup;

    constructor(
        private careerGroupService: CareerGroupService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.careerGroupService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'careerGroupListModification',
                content: 'Deleted an careerGroup'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-career-group-delete-popup',
    template: ''
})
export class CareerGroupDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ careerGroup }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CareerGroupDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.careerGroup = careerGroup;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], {
                            replaceUrl: true,
                            queryParamsHandling: 'merge'
                        });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], {
                            replaceUrl: true,
                            queryParamsHandling: 'merge'
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
