import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MaterialGoodsComponent,
    MaterialGoodsDetailComponent,
    MaterialGoodsUpdateComponent,
    MaterialGoodsDeletePopupComponent,
    MaterialGoodsDeleteDialogComponent,
    materialGoodsRoute,
    materialGoodsPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...materialGoodsRoute, ...materialGoodsPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MaterialGoodsComponent,
        MaterialGoodsDetailComponent,
        MaterialGoodsUpdateComponent,
        MaterialGoodsDeleteDialogComponent,
        MaterialGoodsDeletePopupComponent
    ],
    entryComponents: [
        MaterialGoodsComponent,
        MaterialGoodsUpdateComponent,
        MaterialGoodsDeleteDialogComponent,
        MaterialGoodsDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMaterialGoodsModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
