import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    PPDiscountReturnComponent,
    PPDiscountReturnDetailComponent,
    PPDiscountReturnUpdateComponent,
    PPDiscountReturnDeletePopupComponent,
    PPDiscountReturnDeleteDialogComponent,
    pPDiscountReturnRoute,
    pPDiscountReturnPopupRoute
} from './index';
import { EbwebContextMenuModule } from 'app/contextmenu/contexmenu.module';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { UtilsService } from 'app/entities/UtilsService/Utils.service';

const ENTITY_STATES = [...pPDiscountReturnRoute, ...pPDiscountReturnPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES), EbwebContextMenuModule],
    declarations: [
        PPDiscountReturnComponent,
        PPDiscountReturnDetailComponent,
        PPDiscountReturnUpdateComponent,
        PPDiscountReturnDeleteDialogComponent,
        PPDiscountReturnDeletePopupComponent
    ],
    entryComponents: [
        PPDiscountReturnComponent,
        PPDiscountReturnUpdateComponent,
        PPDiscountReturnDeleteDialogComponent,
        PPDiscountReturnDeletePopupComponent
    ],
    providers: [UtilsService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPPDiscountReturnModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
