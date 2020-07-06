import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MaterialGoodsSpecialTaxGroupComponent,
    MaterialGoodsSpecialTaxGroupDetailComponent,
    MaterialGoodsSpecialTaxGroupUpdateComponent,
    MaterialGoodsSpecialTaxGroupDeletePopupComponent,
    MaterialGoodsSpecialTaxGroupDeleteDialogComponent,
    materialGoodsSpecialTaxGroupRoute,
    materialGoodsSpecialTaxGroupPopupRoute
} from './index';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

const ENTITY_STATES = [...materialGoodsSpecialTaxGroupRoute, ...materialGoodsSpecialTaxGroupPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MaterialGoodsSpecialTaxGroupComponent,
        MaterialGoodsSpecialTaxGroupDetailComponent,
        MaterialGoodsSpecialTaxGroupUpdateComponent,
        MaterialGoodsSpecialTaxGroupDeleteDialogComponent,
        MaterialGoodsSpecialTaxGroupDeletePopupComponent
    ],
    entryComponents: [
        MaterialGoodsSpecialTaxGroupComponent,
        MaterialGoodsSpecialTaxGroupUpdateComponent,
        MaterialGoodsSpecialTaxGroupDeleteDialogComponent,
        MaterialGoodsSpecialTaxGroupDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMaterialGoodsSpecialTaxGroupModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
