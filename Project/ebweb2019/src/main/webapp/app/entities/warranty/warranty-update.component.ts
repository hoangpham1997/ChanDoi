import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IWarranty } from 'app/shared/model/warranty.model';
import { WarrantyService } from './warranty.service';

@Component({
    selector: 'eb-warranty-update',
    templateUrl: './warranty-update.component.html'
})
export class WarrantyUpdateComponent implements OnInit {
    private _warranty: IWarranty;
    isSaving: boolean;

    constructor(private warrantyService: WarrantyService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ warranty }) => {
            this.warranty = warranty;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.warranty.id !== undefined) {
            this.subscribeToSaveResponse(this.warrantyService.update(this.warranty));
        } else {
            this.subscribeToSaveResponse(this.warrantyService.create(this.warranty));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWarranty>>) {
        result.subscribe((res: HttpResponse<IWarranty>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get warranty() {
        return this._warranty;
    }

    set warranty(warranty: IWarranty) {
        this._warranty = warranty;
    }
}
