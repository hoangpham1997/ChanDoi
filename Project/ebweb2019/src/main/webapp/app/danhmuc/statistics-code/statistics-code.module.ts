import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    StatisticsCodeComponent,
    StatisticsCodeUpdateComponent,
    StatisticsCodeDeleteDialogComponent,
    StatisticsCodeDeletePopupComponent,
    statisticsCodeRoute,
    statisticsCodePopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
const ENTITY_STATES = [...statisticsCodeRoute, ...statisticsCodePopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        StatisticsCodeComponent,
        StatisticsCodeUpdateComponent,
        StatisticsCodeDeleteDialogComponent,
        StatisticsCodeDeletePopupComponent
    ],
    entryComponents: [
        StatisticsCodeComponent,
        StatisticsCodeUpdateComponent,
        StatisticsCodeDeleteDialogComponent,
        StatisticsCodeDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebStatisticsCodeModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
