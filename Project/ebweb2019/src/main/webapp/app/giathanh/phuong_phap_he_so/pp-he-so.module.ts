import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { PpHeSoComponent, PpHeSoUpdateComponent, ppHeSoRoute } from './';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...ppHeSoRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [PpHeSoComponent, PpHeSoUpdateComponent],
    entryComponents: [PpHeSoComponent, PpHeSoUpdateComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPhuongPhapHeSoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
