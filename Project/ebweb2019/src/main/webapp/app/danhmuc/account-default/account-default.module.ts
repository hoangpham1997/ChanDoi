import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    AccountDefaultComponent,
    AccountDefaultUpdateComponent,
    AccountDefaultDeletePopupComponent,
    AccountDefaultDeleteDialogComponent,
    accountDefaultRoute,
    accountDefaultPopupRoute,
    AccountDefaultDetailComponent
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...accountDefaultRoute, ...accountDefaultPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AccountDefaultComponent,
        AccountDefaultUpdateComponent,
        AccountDefaultDetailComponent,
        AccountDefaultDeleteDialogComponent,
        AccountDefaultDeletePopupComponent
    ],
    entryComponents: [
        AccountDefaultComponent,
        AccountDefaultUpdateComponent,
        AccountDefaultDetailComponent,
        AccountDefaultDeleteDialogComponent,
        AccountDefaultDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebAccountDefaultModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
