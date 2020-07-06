import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IPporder } from 'app/shared/model/pporder.model';
import { PporderService } from './pporder.service';

@Component({
    selector: 'eb-pporder-update',
    templateUrl: './pporder-update.component.html'
})
export class PporderUpdateComponent implements OnInit {
    isSaving: boolean;
    dateDp: any;
    deliverDateDp: any;

    private _pporder: IPporder;

    constructor(private pporderService: PporderService, private activatedRoute: ActivatedRoute) {}

    get pporder() {
        return this._pporder;
    }

    set pporder(pporder: IPporder) {
        this._pporder = pporder;
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ pporder }) => {
            this.pporder = pporder;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.pporder.id !== undefined) {
            this.subscribeToSaveResponse(this.pporderService.update(this.pporder));
        } else {
            this.subscribeToSaveResponse(this.pporderService.create(this.pporder));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPporder>>) {
        result.subscribe((res: HttpResponse<IPporder>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
