import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IContractState } from 'app/shared/model/contract-state.model';
import { ContractStateService } from './contract-state.service';

@Component({
    selector: 'eb-contract-state-update',
    templateUrl: './contract-state-update.component.html'
})
export class ContractStateUpdateComponent implements OnInit {
    private _contractState: IContractState;
    isSaving: boolean;

    constructor(private contractStateService: ContractStateService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ contractState }) => {
            this.contractState = contractState;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.contractState.id !== undefined) {
            this.subscribeToSaveResponse(this.contractStateService.update(this.contractState));
        } else {
            this.subscribeToSaveResponse(this.contractStateService.create(this.contractState));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IContractState>>) {
        result.subscribe((res: HttpResponse<IContractState>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get contractState() {
        return this._contractState;
    }

    set contractState(contractState: IContractState) {
        this._contractState = contractState;
    }
}
