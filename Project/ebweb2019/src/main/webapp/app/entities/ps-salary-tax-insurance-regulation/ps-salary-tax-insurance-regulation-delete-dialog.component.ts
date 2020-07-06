import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPSSalaryTaxInsuranceRegulation } from 'app/shared/model/ps-salary-tax-insurance-regulation.model';
import { PSSalaryTaxInsuranceRegulationService } from './ps-salary-tax-insurance-regulation.service';

@Component({
    selector: 'eb-ps-salary-tax-insurance-regulation-delete-dialog',
    templateUrl: './ps-salary-tax-insurance-regulation-delete-dialog.component.html'
})
export class PSSalaryTaxInsuranceRegulationDeleteDialogComponent {
    pSSalaryTaxInsuranceRegulation: IPSSalaryTaxInsuranceRegulation;

    constructor(
        private pSSalaryTaxInsuranceRegulationService: PSSalaryTaxInsuranceRegulationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pSSalaryTaxInsuranceRegulationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'pSSalaryTaxInsuranceRegulationListModification',
                content: 'Deleted an pSSalaryTaxInsuranceRegulation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-ps-salary-tax-insurance-regulation-delete-popup',
    template: ''
})
export class PSSalaryTaxInsuranceRegulationDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pSSalaryTaxInsuranceRegulation }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PSSalaryTaxInsuranceRegulationDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.pSSalaryTaxInsuranceRegulation = pSSalaryTaxInsuranceRegulation;
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
