import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    AccountListComponent,
    AccountListUpdateComponent,
    AccountListDeletePopupComponent,
    AccountListDeleteDialogComponent,
    accountListRoute,
    accountListPopupRoute
} from './';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...accountListRoute, ...accountListPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [AccountListComponent, AccountListUpdateComponent, AccountListDeleteDialogComponent, AccountListDeletePopupComponent],
    entryComponents: [AccountListComponent, AccountListUpdateComponent, AccountListDeleteDialogComponent, AccountListDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebAccountListModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
