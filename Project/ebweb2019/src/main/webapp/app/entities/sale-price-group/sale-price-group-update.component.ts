import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ISalePriceGroup } from 'app/shared/model/sale-price-group.model';
import { SalePriceGroupService } from './sale-price-group.service';

@Component({
    selector: 'eb-sale-price-group-update',
    templateUrl: './sale-price-group-update.component.html'
})
export class SalePriceGroupUpdateComponent implements OnInit {
    private _salePriceGroup: ISalePriceGroup;
    isSaving: boolean;

    constructor(private salePriceGroupService: SalePriceGroupService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ salePriceGroup }) => {
            this.salePriceGroup = salePriceGroup;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.salePriceGroup.id !== undefined) {
            this.subscribeToSaveResponse(this.salePriceGroupService.update(this.salePriceGroup));
        } else {
            this.subscribeToSaveResponse(this.salePriceGroupService.create(this.salePriceGroup));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISalePriceGroup>>) {
        result.subscribe((res: HttpResponse<ISalePriceGroup>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get salePriceGroup() {
        return this._salePriceGroup;
    }

    set salePriceGroup(salePriceGroup: ISalePriceGroup) {
        this._salePriceGroup = salePriceGroup;
    }
}
