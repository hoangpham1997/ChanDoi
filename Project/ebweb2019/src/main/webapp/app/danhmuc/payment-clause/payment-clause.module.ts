import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    PaymentClauseComponent,
    PaymentClauseUpdateComponent,
    PaymentClauseDeletePopupComponent,
    PaymentClauseDeleteDialogComponent,
    paymentClauseRoute,
    paymentClausePopupRoute
} from './';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...paymentClauseRoute, ...paymentClausePopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PaymentClauseComponent,
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
export class EbwebPaymentClauseModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
