import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MBTellerPaperDetailTaxComponent,
    MBTellerPaperDetailTaxDetailComponent,
    MBTellerPaperDetailTaxUpdateComponent,
    MBTellerPaperDetailTaxDeletePopupComponent,
    MBTellerPaperDetailTaxDeleteDialogComponent,
    mBTellerPaperDetailTaxRoute,
    mBTellerPaperDetailTaxPopupRoute
} from './index';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...mBTellerPaperDetailTaxRoute, ...mBTellerPaperDetailTaxPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MBTellerPaperDetailTaxComponent,
        MBTellerPaperDetailTaxDetailComponent,
        MBTellerPaperDetailTaxUpdateComponent,
        MBTellerPaperDetailTaxDeleteDialogComponent,
        MBTellerPaperDetailTaxDeletePopupComponent
    ],
    entryComponents: [
        MBTellerPaperDetailTaxComponent,
        MBTellerPaperDetailTaxUpdateComponent,
        MBTellerPaperDetailTaxDeleteDialogComponent,
        MBTellerPaperDetailTaxDeletePopupComponent
    ],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMBTellerPaperDetailTaxModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
