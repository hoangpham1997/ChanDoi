import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MBTellerPaperDetailsComponent,
    MBTellerPaperDetailsDetailComponent,
    MBTellerPaperDetailsUpdateComponent,
    MBTellerPaperDetailsDeletePopupComponent,
    MBTellerPaperDetailsDeleteDialogComponent,
    mBTellerPaperDetailsRoute,
    mBTellerPaperDetailsPopupRoute
} from './index';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...mBTellerPaperDetailsRoute, ...mBTellerPaperDetailsPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MBTellerPaperDetailsComponent,
        MBTellerPaperDetailsDetailComponent,
        MBTellerPaperDetailsUpdateComponent,
        MBTellerPaperDetailsDeleteDialogComponent,
        MBTellerPaperDetailsDeletePopupComponent
    ],
    entryComponents: [
        MBTellerPaperDetailsComponent,
        MBTellerPaperDetailsUpdateComponent,
        MBTellerPaperDetailsDeleteDialogComponent,
        MBTellerPaperDetailsDeletePopupComponent
    ],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMBTellerPaperDetailsModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
