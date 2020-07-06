import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICPAllocationQuantum } from 'app/shared/model/cp-allocation-quantum.model';
import { CPAllocationQuantumService } from './cp-allocation-quantum.service';

@Component({
    selector: 'eb-cp-allocation-quantum-update',
    templateUrl: './cp-allocation-quantum-update.component.html'
})
export class CPAllocationQuantumUpdateComponent implements OnInit {
    private _cPAllocationQuantum: ICPAllocationQuantum;
    isSaving: boolean;

    constructor(private cPAllocationQuantumService: CPAllocationQuantumService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ cPAllocationQuantum }) => {
            this.cPAllocationQuantum = cPAllocationQuantum;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.cPAllocationQuantum.id !== undefined) {
            this.subscribeToSaveResponse(this.cPAllocationQuantumService.update(this.cPAllocationQuantum));
        } else {
            this.subscribeToSaveResponse(this.cPAllocationQuantumService.create(this.cPAllocationQuantum));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICPAllocationQuantum>>) {
        result.subscribe((res: HttpResponse<ICPAllocationQuantum>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get cPAllocationQuantum() {
        return this._cPAllocationQuantum;
    }

    set cPAllocationQuantum(cPAllocationQuantum: ICPAllocationQuantum) {
        this._cPAllocationQuantum = cPAllocationQuantum;
    }
}
