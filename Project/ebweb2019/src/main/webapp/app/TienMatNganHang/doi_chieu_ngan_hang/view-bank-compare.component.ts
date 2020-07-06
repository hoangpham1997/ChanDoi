import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ICollectionVoucher } from 'app/shared/model/collection-voucher.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { AccountingObjectService } from 'app/danhmuc/accounting-object';
import { GeneralLedgerService } from 'app/entities/general-ledger';

@Component({
    selector: 'eb-bank-compare',
    templateUrl: './view-bank-compare.component.html',
    styleUrls: ['./bank-compare.component.css']
})
export class ViewBankCompareComponent implements OnInit {
    collectionVouchers: ICollectionVoucher[];
    accountingObjects: IAccountingObject[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private activatedRoute: ActivatedRoute,
        private gLService: GeneralLedgerService,
        private accountingObjectIDService: AccountingObjectService
    ) {}

    ngOnInit() {
        this.accountingObjectIDService.query().subscribe(
            (res: HttpResponse<IAccountingObject[]>) => {
                this.accountingObjects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.gLService.getListMatch().subscribe((res: HttpResponse<ICollectionVoucher[]>) => {
            console.log(res.body);
            this.collectionVouchers = res.body;
        });
    }

    trackAccountingObjectById(index: number, item: IAccountingObject) {
        return item.id;
    }

    previousState() {
        window.history.back();
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
