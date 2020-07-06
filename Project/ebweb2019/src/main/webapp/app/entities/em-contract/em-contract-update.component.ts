import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IEMContract } from 'app/shared/model/em-contract.model';
import { EMContractService } from './em-contract.service';

@Component({
    selector: 'eb-em-contract-update',
    templateUrl: './em-contract-update.component.html'
})
export class EMContractUpdateComponent implements OnInit {
    private _eMContract: IEMContract;
    isSaving: boolean;
    signedDateDp: any;
    startedDateDp: any;
    closedDateDp: any;

    constructor(private eMContractService: EMContractService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ eMContract }) => {
            this.eMContract = eMContract;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.eMContract.id !== undefined) {
            this.subscribeToSaveResponse(this.eMContractService.update(this.eMContract));
        } else {
            this.subscribeToSaveResponse(this.eMContractService.create(this.eMContract));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IEMContract>>) {
        result.subscribe((res: HttpResponse<IEMContract>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get eMContract() {
        return this._eMContract;
    }

    set eMContract(eMContract: IEMContract) {
        this._eMContract = eMContract;
    }
}
