import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    SAQuoteDetailsComponent,
    SAQuoteDetailsDetailComponent,
    SAQuoteDetailsUpdateComponent,
    SAQuoteDetailsDeletePopupComponent,
    SAQuoteDetailsDeleteDialogComponent,
    sAQuoteDetailsRoute,
    sAQuoteDetailsPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

const ENTITY_STATES = [...sAQuoteDetailsRoute, ...sAQuoteDetailsPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SAQuoteDetailsComponent,
        SAQuoteDetailsDetailComponent,
        SAQuoteDetailsUpdateComponent,
        SAQuoteDetailsDeleteDialogComponent,
        SAQuoteDetailsDeletePopupComponent
    ],
    entryComponents: [
        SAQuoteDetailsComponent,
        SAQuoteDetailsUpdateComponent,
        SAQuoteDetailsDeleteDialogComponent,
        SAQuoteDetailsDeletePopupComponent
    ],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebSAQuoteDetailsModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
