import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    CreditCardComponent,
    CreditCardDetailComponent,
    CreditCardUpdateComponent,
    CreditCardDeletePopupComponent,
    CreditCardDeleteDialogComponent,
    creditCardRoute,
    creditCardPopupRoute
} from './index';
import { ReactiveFormsModule } from '@angular/forms';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { NgxMaskModule } from 'ngx-mask';

const ENTITY_STATES = [...creditCardRoute, ...creditCardPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, ReactiveFormsModule, RouterModule.forChild(ENTITY_STATES), NgxMaskModule],
    declarations: [
        CreditCardComponent,
        CreditCardDetailComponent,
        CreditCardUpdateComponent,
        CreditCardDeleteDialogComponent,
        CreditCardDeletePopupComponent
    ],
    entryComponents: [CreditCardComponent, CreditCardUpdateComponent, CreditCardDeleteDialogComponent, CreditCardDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebCreditCardModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
