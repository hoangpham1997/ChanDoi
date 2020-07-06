import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    SystemOptionComponent,
    SystemOptionDetailComponent,
    SystemOptionUpdateComponent,
    SystemOptionDeletePopupComponent,
    SystemOptionDeleteDialogComponent,
    systemOptionRoute,
    systemOptionPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...systemOptionRoute, ...systemOptionPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SystemOptionComponent,
        SystemOptionDetailComponent,
        SystemOptionUpdateComponent,
        SystemOptionDeleteDialogComponent,
        SystemOptionDeletePopupComponent
    ],
    entryComponents: [
        SystemOptionComponent,
        SystemOptionUpdateComponent,
        SystemOptionDeleteDialogComponent,
        SystemOptionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebSystemOptionModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
