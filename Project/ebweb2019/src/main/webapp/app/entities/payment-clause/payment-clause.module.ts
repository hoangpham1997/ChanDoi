import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    PaymentClauseComponent,
    PaymentClauseDetailComponent,
    PaymentClauseUpdateComponent,
    PaymentClauseDeletePopupComponent,
    PaymentClauseDeleteDialogComponent,
    paymentClauseRoute,
    paymentClausePopupRoute
} from './';

const ENTITY_STATES = [...paymentClauseRoute, ...paymentClausePopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PaymentClauseComponent,
        PaymentClauseDetailComponent,
        PaymentClauseUpdateComponent,
        PaymentClauseDeleteDialogComponent,
        PaymentClauseDeletePopupComponent
    ],
    entryComponents: [
        PaymentClauseComponent,
        PaymentClauseUpdateComponent,
        PaymentClauseDeleteDialogComponent,
        PaymentClauseDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPaymentClauseModule {}
