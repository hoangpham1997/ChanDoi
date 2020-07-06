import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IGeneralLedger } from 'app/shared/model/general-ledger.model';
import { GeneralLedgerService } from './general-ledger.service';

@Component({
    selector: 'eb-general-ledger-update',
    templateUrl: './general-ledger-update.component.html'
})
export class GeneralLedgerUpdateComponent implements OnInit {
    private _generalLedger: IGeneralLedger;
    isSaving: boolean;
    dateDp: any;
    postedDateDp: any;
    invoiceDateDp: any;
    refDateTimeDp: any;

    constructor(private generalLedgerService: GeneralLedgerService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ generalLedger }) => {
            this.generalLedger = generalLedger;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.generalLedger.id !== undefined) {
            this.subscribeToSaveResponse(this.generalLedgerService.update(this.generalLedger));
        } else {
            this.subscribeToSaveResponse(this.generalLedgerService.create(this.generalLedger));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IGeneralLedger>>) {
        result.subscribe((res: HttpResponse<IGeneralLedger>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get generalLedger() {
        return this._generalLedger;
    }

    set generalLedger(generalLedger: IGeneralLedger) {
        this._generalLedger = generalLedger;
    }
}
