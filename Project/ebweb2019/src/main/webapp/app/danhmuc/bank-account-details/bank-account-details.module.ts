import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    BankAccountDetailsComponent,
    BankAccountDetailsDetailComponent,
    BankAccountDetailsUpdateComponent,
    BankAccountDetailsDeletePopupComponent,
    BankAccountDetailsDeleteDialogComponent,
    bankAccountDetailsRoute,
    bankAccountDetailsPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...bankAccountDetailsRoute, ...bankAccountDetailsPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BankAccountDetailsComponent,
        BankAccountDetailsDetailComponent,
        BankAccountDetailsUpdateComponent,
        BankAccountDetailsDeleteDialogComponent,
        BankAccountDetailsDeletePopupComponent
    ],
    entryComponents: [
        BankAccountDetailsComponent,
        BankAccountDetailsUpdateComponent,
        BankAccountDetailsDeleteDialogComponent,
        BankAccountDetailsDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebBankAccountDetailsModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
