import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IGenCode } from 'app/shared/model/gen-code.model';
import { GenCodeService } from './gen-code.service';

@Component({
    selector: 'eb-gen-code-update',
    templateUrl: './gen-code-update.component.html'
})
export class GenCodeUpdateComponent implements OnInit {
    private _genCode: IGenCode;
    isSaving: boolean;

    constructor(private genCodeService: GenCodeService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ genCode }) => {
            this.genCode = genCode;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.genCode.id !== undefined) {
            this.subscribeToSaveResponse(this.genCodeService.update(this.genCode));
        } else {
            this.subscribeToSaveResponse(this.genCodeService.create(this.genCode));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IGenCode>>) {
        result.subscribe((res: HttpResponse<IGenCode>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get genCode() {
        return this._genCode;
    }

    set genCode(genCode: IGenCode) {
        this._genCode = genCode;
    }
}
