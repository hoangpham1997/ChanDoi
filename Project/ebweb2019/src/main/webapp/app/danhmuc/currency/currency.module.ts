import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    CurrencyComponent,
    CurrencyDetailComponent,
    CurrencyUpdateComponent,
    CurrencyDeletePopupComponent,
    CurrencyDeleteDialogComponent,
    currencyRoute,
    currencyPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...currencyRoute, ...currencyPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CurrencyComponent,
        CurrencyDetailComponent,
        CurrencyUpdateComponent,
        CurrencyDeleteDialogComponent,
        CurrencyDeletePopupComponent
    ],
    entryComponents: [CurrencyComponent, CurrencyUpdateComponent, CurrencyDeleteDialogComponent, CurrencyDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebCurrencyModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
