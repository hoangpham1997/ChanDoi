import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MaterialGoodsCategoryComponent,
    MaterialGoodsCategoryDetailComponent,
    MaterialGoodsCategoryUpdateComponent,
    MaterialGoodsCategoryDeletePopupComponent,
    MaterialGoodsCategoryDeleteDialogComponent,
    materialGoodsCategoryRoute,
    materialGoodsCategoryPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...materialGoodsCategoryRoute, ...materialGoodsCategoryPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MaterialGoodsCategoryComponent,
        MaterialGoodsCategoryDetailComponent,
        MaterialGoodsCategoryUpdateComponent,
        MaterialGoodsCategoryDeleteDialogComponent,
        MaterialGoodsCategoryDeletePopupComponent
    ],
    entryComponents: [
        MaterialGoodsCategoryComponent,
        MaterialGoodsCategoryUpdateComponent,
        MaterialGoodsCategoryDeleteDialogComponent,
        MaterialGoodsCategoryDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMaterialGoodsCategoryModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
