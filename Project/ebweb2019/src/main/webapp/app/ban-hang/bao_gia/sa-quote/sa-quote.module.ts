import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    SAQuoteComponent,
    SAQuoteDetailComponent,
    SAQuoteUpdateComponent,
    SAQuoteDeletePopupComponent,
    SAQuoteDeleteDialogComponent,
    sAQuoteRoute,
    sAQuotePopupRoute
} from './index';
import { EbwebContextMenuModule } from 'app/contextmenu/contexmenu.module';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

const ENTITY_STATES = [...sAQuoteRoute, ...sAQuotePopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), EbwebContextMenuModule],
    declarations: [
        SAQuoteComponent,
        SAQuoteDetailComponent,
        SAQuoteUpdateComponent,
        SAQuoteDeleteDialogComponent,
        SAQuoteDeletePopupComponent
    ],
    entryComponents: [SAQuoteComponent, SAQuoteUpdateComponent, SAQuoteDeleteDialogComponent, SAQuoteDeletePopupComponent],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebSAQuoteModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
