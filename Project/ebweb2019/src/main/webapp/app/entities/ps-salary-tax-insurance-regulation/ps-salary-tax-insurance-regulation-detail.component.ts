import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPSSalaryTaxInsuranceRegulation } from 'app/shared/model/ps-salary-tax-insurance-regulation.model';

@Component({
    selector: 'eb-ps-salary-tax-insurance-regulation-detail',
    templateUrl: './ps-salary-tax-insurance-regulation-detail.component.html'
})
export class PSSalaryTaxInsuranceRegulationDetailComponent implements OnInit {
    pSSalaryTaxInsuranceRegulation: IPSSalaryTaxInsuranceRegulation;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pSSalaryTaxInsuranceRegulation }) => {
            this.pSSalaryTaxInsuranceRegulation = pSSalaryTaxInsuranceRegulation;
        });
    }

    previousState() {
        window.history.back();
    }
}
