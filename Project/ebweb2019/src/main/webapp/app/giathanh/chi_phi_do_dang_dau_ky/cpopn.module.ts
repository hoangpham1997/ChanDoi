import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    CPOPNComponent,
    CPOPNDetailComponent,
    CPOPNUpdateComponent,
    CPOPNDeletePopupComponent,
    CPOPNDeleteDialogComponent,
    cPOPNRoute,
    cPOPNPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...cPOPNRoute, ...cPOPNPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [CPOPNComponent, CPOPNDetailComponent, CPOPNUpdateComponent, CPOPNDeleteDialogComponent, CPOPNDeletePopupComponent],
    entryComponents: [CPOPNComponent, CPOPNUpdateComponent, CPOPNDeleteDialogComponent, CPOPNDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebCPOPNModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
