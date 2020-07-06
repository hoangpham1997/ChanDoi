import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import { PpCongTrinhVuViecComponent, PpCongTrinhVuViecUpdateComponent, ppCongTrinhVuViecRoute } from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...ppCongTrinhVuViecRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [PpCongTrinhVuViecComponent, PpCongTrinhVuViecUpdateComponent],
    entryComponents: [PpCongTrinhVuViecComponent, PpCongTrinhVuViecUpdateComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebGiaThanhTheoCongTrinhVuViecModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
