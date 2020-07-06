import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
// @ts-ignore
import {
    AccountingObjectComponent,
    AccountingObjectUpdateComponent,
    AccountingObjectDeletePopupComponent,
    AccountingObjectDeleteDialogComponent,
    accountingObjectRoute,
    accountingObjectPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...accountingObjectRoute, ...accountingObjectPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AccountingObjectComponent,
        AccountingObjectUpdateComponent,
        AccountingObjectDeleteDialogComponent,
        AccountingObjectDeletePopupComponent
    ],
    entryComponents: [
        AccountingObjectComponent,
        AccountingObjectUpdateComponent,
        AccountingObjectDeleteDialogComponent,
        AccountingObjectDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebAccountingObjectModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
