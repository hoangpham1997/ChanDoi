import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    AccountingObjectBankAccountComponent,
    AccountingObjectBankAccountDetailComponent,
    AccountingObjectBankAccountUpdateComponent,
    AccountingObjectBankAccountDeletePopupComponent,
    AccountingObjectBankAccountDeleteDialogComponent,
    accountingObjectBankAccountRoute,
    accountingObjectBankAccountPopupRoute
} from './';

const ENTITY_STATES = [...accountingObjectBankAccountRoute, ...accountingObjectBankAccountPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AccountingObjectBankAccountComponent,
        AccountingObjectBankAccountDetailComponent,
        AccountingObjectBankAccountUpdateComponent,
        AccountingObjectBankAccountDeleteDialogComponent,
        AccountingObjectBankAccountDeletePopupComponent
    ],
    entryComponents: [
        AccountingObjectBankAccountComponent,
        AccountingObjectBankAccountUpdateComponent,
        AccountingObjectBankAccountDeleteDialogComponent,
        AccountingObjectBankAccountDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebAccountingObjectBankAccountModule {}
