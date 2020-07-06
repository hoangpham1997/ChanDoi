import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    AccountTransferComponent,
    AccountTransferDetailComponent,
    AccountTransferUpdateComponent,
    AccountTransferDeletePopupComponent,
    AccountTransferDeleteDialogComponent,
    accountTransferRoute,
    accountTransferPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...accountTransferRoute, ...accountTransferPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AccountTransferComponent,
        AccountTransferDetailComponent,
        AccountTransferUpdateComponent,
        AccountTransferDeleteDialogComponent,
        AccountTransferDeletePopupComponent
    ],
    entryComponents: [
        AccountTransferComponent,
        AccountTransferUpdateComponent,
        AccountTransferDeleteDialogComponent,
        AccountTransferDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebAccountTransferModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
