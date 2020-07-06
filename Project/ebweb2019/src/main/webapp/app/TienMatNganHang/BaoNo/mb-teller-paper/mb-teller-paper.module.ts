import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MBTellerPaperComponent,
    MBTellerPaperDetailComponent,
    MBTellerPaperUpdateComponent,
    MBTellerPaperDeletePopupComponent,
    MBTellerPaperDeleteDialogComponent,
    mBTellerPaperRoute,
    mBTellerPaperPopupRoute
} from './index';
import { EbwebContextMenuModule } from 'app/contextmenu/contexmenu.module';
import { ReactiveFormsModule } from '@angular/forms';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...mBTellerPaperRoute, ...mBTellerPaperPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), EbwebContextMenuModule, ReactiveFormsModule],
    declarations: [
        MBTellerPaperComponent,
        MBTellerPaperDetailComponent,
        MBTellerPaperUpdateComponent,
        MBTellerPaperDeleteDialogComponent,
        MBTellerPaperDeletePopupComponent
    ],
    entryComponents: [
        MBTellerPaperComponent,
        MBTellerPaperUpdateComponent,
        MBTellerPaperDeleteDialogComponent,
        MBTellerPaperDeletePopupComponent
    ],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMBTellerPaperModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
