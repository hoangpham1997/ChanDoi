import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ISystemOption } from 'app/shared/model/system-option.model';
import { SystemOptionService } from './system-option.service';

@Component({
    selector: 'eb-system-option-update',
    templateUrl: './system-option-update.component.html'
})
export class SystemOptionUpdateComponent implements OnInit {
    private _systemOption: ISystemOption;
    isSaving: boolean;

    constructor(private systemOptionService: SystemOptionService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ systemOption }) => {
            this.systemOption = systemOption;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.systemOption.id !== undefined) {
            this.subscribeToSaveResponse(this.systemOptionService.update(this.systemOption));
        } else {
            this.subscribeToSaveResponse(this.systemOptionService.create(this.systemOption));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISystemOption>>) {
        result.subscribe((res: HttpResponse<ISystemOption>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get systemOption() {
        return this._systemOption;
    }

    set systemOption(systemOption: ISystemOption) {
        this._systemOption = systemOption;
    }
}
