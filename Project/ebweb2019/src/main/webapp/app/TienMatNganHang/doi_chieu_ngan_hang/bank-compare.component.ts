import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { DATE_FORMAT } from 'app/shared';
import { Moment } from 'moment';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { ICollectionVoucher } from 'app/shared/model/collection-voucher.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { GeneralLedgerService } from 'app/entities/general-ledger';
import { CurrencyService } from 'app/danhmuc/currency';
import { BankAccountDetailsService } from 'app/danhmuc/bank-account-details';

@Component({
    selector: 'eb-bank-compare',
    templateUrl: './bank-compare.component.html',
    styleUrls: ['./bank-compare.component.css']
})
export class BankCompareComponent implements OnInit {
    currencyID: string;
    bankAccountID: string;
    currencys: ICurrency[];
    bankAccounts: IBankAccountDetails[];
    date: Moment;
    accountingObjects: IAccountingObject[];
    collectionVouchers1: ICollectionVoucher[];
    collectionVouchers2: ICollectionVoucher[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private activatedRoute: ActivatedRoute,
        private currencyIDService: CurrencyService,
        private accountingObjectIDService: AccountingObjectService,
        private bankAccountDetailsService: BankAccountDetailsService,
        private gLService: GeneralLedgerService
    ) {}

    ngOnInit() {
        this.currencyID = 'VND';
        this.currencyIDService.query().subscribe(
            (res: HttpResponse<ICurrency[]>) => {
                this.currencys = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.bankAccountDetailsService.query().subscribe(
            (res: HttpResponse<IBankAccountDetails[]>) => {
                this.bankAccounts = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.accountingObjectIDService.query().subscribe(
            (res: HttpResponse<IAccountingObject[]>) => {
                this.accountingObjects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.collectionVouchers1 = [];
        this.collectionVouchers2 = [];
    }

    previousState() {
        window.history.back();
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCurrencyIDById(index: number, item: ICurrency) {
        return item.currencyCode;
    }

    trackBankAccountById(index: number, item: IBankAccountDetails) {
        return item.bankAccount;
    }

    trackAccountingObjectById(index: number, item: IAccountingObject) {
        return item.id;
    }

    getData(date: Moment, cuID: String, bankAccountID: String) {
        this.gLService
            .getCollectionVoucher({
                auditDate: date != null ? date.format(DATE_FORMAT) : null,
                currencyID: cuID,
                bankID: bankAccountID,
                isMatch: false
            })
            .subscribe((res: HttpResponse<ICollectionVoucher[]>) => {
                console.log(res.body);
                this.collectionVouchers1 = res.body;
            });
        this.gLService
            .getSpendingVoucher({
                auditDate: date != null ? date.format(DATE_FORMAT) : null,
                currencyID: cuID,
                bankID: bankAccountID,
                isMatch: false
            })
            .subscribe((res: HttpResponse<ICollectionVoucher[]>) => {
                console.log(res.body);
                this.collectionVouchers2 = res.body;
            });
    }
}
