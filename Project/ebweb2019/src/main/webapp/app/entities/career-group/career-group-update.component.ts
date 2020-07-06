import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICareerGroup } from 'app/shared/model/career-group.model';
import { CareerGroupService } from './career-group.service';

@Component({
    selector: 'eb-career-group-update',
    templateUrl: './career-group-update.component.html'
})
export class CareerGroupUpdateComponent implements OnInit {
    private _careerGroup: ICareerGroup;
    isSaving: boolean;

    constructor(private careerGroupService: CareerGroupService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ careerGroup }) => {
            this.careerGroup = careerGroup;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.careerGroup.id !== undefined) {
            this.subscribeToSaveResponse(this.careerGroupService.update(this.careerGroup));
        } else {
            this.subscribeToSaveResponse(this.careerGroupService.create(this.careerGroup));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICareerGroup>>) {
        result.subscribe((res: HttpResponse<ICareerGroup>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get careerGroup() {
        return this._careerGroup;
    }

    set careerGroup(careerGroup: ICareerGroup) {
        this._careerGroup = careerGroup;
    }
}
