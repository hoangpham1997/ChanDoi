import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ITransportMethod } from 'app/shared/model/transport-method.model';
import { TransportMethodService } from './transport-method.service';

@Component({
    selector: 'eb-transport-method-update',
    templateUrl: './transport-method-update.component.html'
})
export class TransportMethodUpdateComponent implements OnInit {
    private _transportMethod: ITransportMethod;
    isSaving: boolean;

    constructor(private transportMethodService: TransportMethodService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ transportMethod }) => {
            this.transportMethod = transportMethod;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.transportMethod.id !== undefined) {
            this.subscribeToSaveResponse(this.transportMethodService.update(this.transportMethod));
        } else {
            this.subscribeToSaveResponse(this.transportMethodService.create(this.transportMethod));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITransportMethod>>) {
        result.subscribe((res: HttpResponse<ITransportMethod>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get transportMethod() {
        return this._transportMethod;
    }

    set transportMethod(transportMethod: ITransportMethod) {
        this._transportMethod = transportMethod;
    }
}
