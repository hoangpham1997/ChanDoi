import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICPOPN } from 'app/shared/model/cpopn.model';
import { CPOPNService } from './cpopn.service';

@Component({
    selector: 'eb-cpopn-update',
    templateUrl: './cpopn-update.component.html'
})
export class CPOPNUpdateComponent implements OnInit {
    private _cPOPN: ICPOPN;
    isSaving: boolean;

    constructor(private cPOPNService: CPOPNService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ cPOPN }) => {
            this.cPOPN = cPOPN;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.cPOPN.id !== undefined) {
            this.subscribeToSaveResponse(this.cPOPNService.update(this.cPOPN));
        } else {
            this.subscribeToSaveResponse(this.cPOPNService.create(this.cPOPN));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICPOPN>>) {
        result.subscribe((res: HttpResponse<ICPOPN>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get cPOPN() {
        return this._cPOPN;
    }

    set cPOPN(cPOPN: ICPOPN) {
        this._cPOPN = cPOPN;
    }
}
