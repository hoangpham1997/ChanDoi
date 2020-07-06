import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IPporderdetail } from 'app/shared/model/pporderdetail.model';
import { PporderdetailService } from './pporderdetail.service';

@Component({
    selector: 'eb-pporderdetail-update',
    templateUrl: './pporderdetail-update.component.html'
})
export class PporderdetailUpdateComponent implements OnInit {
    isSaving: boolean;

    private _pporderdetail: IPporderdetail;

    constructor(private pporderdetailService: PporderdetailService, private activatedRoute: ActivatedRoute) {}

    get pporderdetail() {
        return this._pporderdetail;
    }

    set pporderdetail(pporderdetail: IPporderdetail) {
        this._pporderdetail = pporderdetail;
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ pporderdetail }) => {
            this.pporderdetail = pporderdetail;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.pporderdetail.id !== undefined) {
            this.subscribeToSaveResponse(this.pporderdetailService.update(this.pporderdetail));
        } else {
            this.subscribeToSaveResponse(this.pporderdetailService.create(this.pporderdetail));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPporderdetail>>) {
        result.subscribe((res: HttpResponse<IPporderdetail>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
