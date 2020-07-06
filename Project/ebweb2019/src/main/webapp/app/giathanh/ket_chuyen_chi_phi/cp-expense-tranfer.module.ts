import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    CPExpenseTranferComponent,
    CPExpenseTranferUpdateComponent,
    CPExpenseTranferDeletePopupComponent,
    CPExpenseTranferDeleteDialogComponent,
    cPExpenseTranferRoute,
    cPExpenseTranferPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...cPExpenseTranferRoute, ...cPExpenseTranferPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CPExpenseTranferComponent,
        CPExpenseTranferUpdateComponent,
        CPExpenseTranferDeleteDialogComponent,
        CPExpenseTranferDeletePopupComponent
    ],
    entryComponents: [
        CPExpenseTranferComponent,
        CPExpenseTranferUpdateComponent,
        CPExpenseTranferDeleteDialogComponent,
        CPExpenseTranferDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebCPExpenseTranferModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
