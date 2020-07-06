import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    AccountingObjectGroupComponent,
    AccountingObjectGroupDetailComponent,
    AccountingObjectGroupUpdateComponent,
    AccountingObjectGroupDeletePopupComponent,
    AccountingObjectGroupDeleteDialogComponent,
    accountingObjectGroupRoute,
    accountingObjectGroupPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...accountingObjectGroupRoute, ...accountingObjectGroupPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AccountingObjectGroupComponent,
        AccountingObjectGroupDetailComponent,
        AccountingObjectGroupUpdateComponent,
        AccountingObjectGroupDeleteDialogComponent,
        AccountingObjectGroupDeletePopupComponent
    ],
    entryComponents: [
        AccountingObjectGroupComponent,
        AccountingObjectGroupUpdateComponent,
        AccountingObjectGroupDeleteDialogComponent,
        AccountingObjectGroupDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebAccountingObjectGroupModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
