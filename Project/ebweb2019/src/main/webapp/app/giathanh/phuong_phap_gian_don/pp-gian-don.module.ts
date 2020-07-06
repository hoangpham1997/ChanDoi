import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { PpGianDonComponent, PpGianDonUpdateComponent, ppGianDonRoute } from './';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...ppGianDonRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [PpGianDonComponent, PpGianDonUpdateComponent],
    entryComponents: [PpGianDonComponent, PpGianDonUpdateComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebCPPeriodModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
