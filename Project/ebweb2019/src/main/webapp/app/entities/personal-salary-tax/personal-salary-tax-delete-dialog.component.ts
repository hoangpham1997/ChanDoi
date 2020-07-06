import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPersonalSalaryTax } from 'app/shared/model/personal-salary-tax.model';
import { PersonalSalaryTaxService } from './personal-salary-tax.service';

@Component({
    selector: 'eb-personal-salary-tax-delete-dialog',
    templateUrl: './personal-salary-tax-delete-dialog.component.html'
})
export class PersonalSalaryTaxDeleteDialogComponent {
    personalSalaryTax: IPersonalSalaryTax;

    constructor(
        private personalSalaryTaxService: PersonalSalaryTaxService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.personalSalaryTaxService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'personalSalaryTaxListModification',
                content: 'Deleted an personalSalaryTax'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'eb-personal-salary-tax-delete-popup',
    template: ''
})
export class PersonalSalaryTaxDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ personalSalaryTax }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PersonalSalaryTaxDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.personalSalaryTax = personalSalaryTax;
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
