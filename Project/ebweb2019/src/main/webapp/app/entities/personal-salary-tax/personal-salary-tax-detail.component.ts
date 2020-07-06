import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonalSalaryTax } from 'app/shared/model/personal-salary-tax.model';

@Component({
    selector: 'eb-personal-salary-tax-detail',
    templateUrl: './personal-salary-tax-detail.component.html'
})
export class PersonalSalaryTaxDetailComponent implements OnInit {
    personalSalaryTax: IPersonalSalaryTax;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ personalSalaryTax }) => {
            this.personalSalaryTax = personalSalaryTax;
        });
    }

    previousState() {
        window.history.back();
    }
}
