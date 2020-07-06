import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    TransportMethodComponent,
    TransportMethodUpdateComponent,
    TransportMethodDeletePopupComponent,
    TransportMethodDeleteDialogComponent,
    transportMethodRoute,
    transportMedthodPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...transportMethodRoute, ...transportMedthodPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TransportMethodComponent,
        TransportMethodUpdateComponent,
        TransportMethodDeleteDialogComponent,
        TransportMethodDeletePopupComponent
    ],
    entryComponents: [
        TransportMethodComponent,
        TransportMethodUpdateComponent,
        TransportMethodDeleteDialogComponent,
        TransportMethodDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TransportMethodModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
