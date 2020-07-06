import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IPersonalSalaryTax } from 'app/shared/model/personal-salary-tax.model';
import { PersonalSalaryTaxService } from './personal-salary-tax.service';

@Component({
    selector: 'eb-personal-salary-tax-update',
    templateUrl: './personal-salary-tax-update.component.html'
})
export class PersonalSalaryTaxUpdateComponent implements OnInit {
    private _personalSalaryTax: IPersonalSalaryTax;
    isSaving: boolean;

    constructor(private personalSalaryTaxService: PersonalSalaryTaxService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ personalSalaryTax }) => {
            this.personalSalaryTax = personalSalaryTax;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.personalSalaryTax.id !== undefined) {
            this.subscribeToSaveResponse(this.personalSalaryTaxService.update(this.personalSalaryTax));
        } else {
            this.subscribeToSaveResponse(this.personalSalaryTaxService.create(this.personalSalaryTax));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPersonalSalaryTax>>) {
        result.subscribe((res: HttpResponse<IPersonalSalaryTax>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get personalSalaryTax() {
        return this._personalSalaryTax;
    }

    set personalSalaryTax(personalSalaryTax: IPersonalSalaryTax) {
        this._personalSalaryTax = personalSalaryTax;
    }
}
