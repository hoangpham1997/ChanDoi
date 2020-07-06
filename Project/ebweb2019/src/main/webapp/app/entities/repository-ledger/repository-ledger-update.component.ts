import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IRepositoryLedger } from 'app/shared/model/repository-ledger.model';
import { RepositoryLedgerService } from './repository-ledger.service';

@Component({
    selector: 'eb-repository-ledger-update',
    templateUrl: './repository-ledger-update.component.html'
})
export class RepositoryLedgerUpdateComponent implements OnInit {
    private _repositoryLedger: IRepositoryLedger;
    isSaving: boolean;
    dateDp: any;
    postedDateDp: any;
    expiryDateDp: any;

    constructor(private repositoryLedgerService: RepositoryLedgerService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ repositoryLedger }) => {
            this.repositoryLedger = repositoryLedger;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.repositoryLedger.id !== undefined) {
            this.subscribeToSaveResponse(this.repositoryLedgerService.update(this.repositoryLedger));
        } else {
            this.subscribeToSaveResponse(this.repositoryLedgerService.create(this.repositoryLedger));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRepositoryLedger>>) {
        result.subscribe((res: HttpResponse<IRepositoryLedger>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get repositoryLedger() {
        return this._repositoryLedger;
    }

    set repositoryLedger(repositoryLedger: IRepositoryLedger) {
        this._repositoryLedger = repositoryLedger;
    }
}
