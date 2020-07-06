import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IPSSalaryTaxInsuranceRegulation } from 'app/shared/model/ps-salary-tax-insurance-regulation.model';
import { PSSalaryTaxInsuranceRegulationService } from './ps-salary-tax-insurance-regulation.service';

@Component({
    selector: 'eb-ps-salary-tax-insurance-regulation-update',
    templateUrl: './ps-salary-tax-insurance-regulation-update.component.html'
})
export class PSSalaryTaxInsuranceRegulationUpdateComponent implements OnInit {
    private _pSSalaryTaxInsuranceRegulation: IPSSalaryTaxInsuranceRegulation;
    isSaving: boolean;
    fromDateDp: any;
    toDateDp: any;

    constructor(
        private pSSalaryTaxInsuranceRegulationService: PSSalaryTaxInsuranceRegulationService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ pSSalaryTaxInsuranceRegulation }) => {
            this.pSSalaryTaxInsuranceRegulation = pSSalaryTaxInsuranceRegulation;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.pSSalaryTaxInsuranceRegulation.id !== undefined) {
            this.subscribeToSaveResponse(this.pSSalaryTaxInsuranceRegulationService.update(this.pSSalaryTaxInsuranceRegulation));
        } else {
            this.subscribeToSaveResponse(this.pSSalaryTaxInsuranceRegulationService.create(this.pSSalaryTaxInsuranceRegulation));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPSSalaryTaxInsuranceRegulation>>) {
        result.subscribe(
            (res: HttpResponse<IPSSalaryTaxInsuranceRegulation>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get pSSalaryTaxInsuranceRegulation() {
        return this._pSSalaryTaxInsuranceRegulation;
    }

    set pSSalaryTaxInsuranceRegulation(pSSalaryTaxInsuranceRegulation: IPSSalaryTaxInsuranceRegulation) {
        this._pSSalaryTaxInsuranceRegulation = pSSalaryTaxInsuranceRegulation;
    }
}
