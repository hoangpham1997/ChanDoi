import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { AccountingObjectBankAccountService } from './accounting-object-bank-account.service';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

@Component({
    selector: 'eb-accounting-object-bank-account-update',
    templateUrl: './accounting-object-bank-account-update.component.html'
})
export class AccountingObjectBankAccountUpdateComponent implements OnInit {
    private _accountingObjectBankAccount: IAccountingObjectBankAccount;
    isSaving: boolean;

    accountingobjects: IAccountingObject[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private accountingObjectBankAccountService: AccountingObjectBankAccountService,
        private accountingObjectService: AccountingObjectService,
        private activatedRoute: ActivatedRoute // public utilsService: UtilsService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ accountingObjectBankAccount }) => {
            this.accountingObjectBankAccount = accountingObjectBankAccount;
        });
        this.accountingObjectService.query().subscribe(
            (res: HttpResponse<IAccountingObject[]>) => {
                this.accountingobjects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.accountingObjectBankAccount.id !== undefined) {
            this.subscribeToSaveResponse(this.accountingObjectBankAccountService.update(this.accountingObjectBankAccount));
        } else {
            this.subscribeToSaveResponse(this.accountingObjectBankAccountService.create(this.accountingObjectBankAccount));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAccountingObjectBankAccount>>) {
        result.subscribe(
            (res: HttpResponse<IAccountingObjectBankAccount>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackAccountingObjectById(index: number, item: IAccountingObject) {
        return item.id;
    }
    get accountingObjectBankAccount() {
        return this._accountingObjectBankAccount;
    }

    set accountingObjectBankAccount(accountingObjectBankAccount: IAccountingObjectBankAccount) {
        this._accountingObjectBankAccount = accountingObjectBankAccount;
    }
}
