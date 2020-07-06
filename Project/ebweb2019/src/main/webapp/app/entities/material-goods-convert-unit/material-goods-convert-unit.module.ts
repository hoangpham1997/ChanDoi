import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MaterialGoodsConvertUnitComponent,
    MaterialGoodsConvertUnitDetailComponent,
    MaterialGoodsConvertUnitUpdateComponent,
    MaterialGoodsConvertUnitDeletePopupComponent,
    MaterialGoodsConvertUnitDeleteDialogComponent,
    materialGoodsConvertUnitRoute,
    materialGoodsConvertUnitPopupRoute
} from './';

const ENTITY_STATES = [...materialGoodsConvertUnitRoute, ...materialGoodsConvertUnitPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MaterialGoodsConvertUnitComponent,
        MaterialGoodsConvertUnitDetailComponent,
        MaterialGoodsConvertUnitUpdateComponent,
        MaterialGoodsConvertUnitDeleteDialogComponent,
        MaterialGoodsConvertUnitDeletePopupComponent
    ],
    entryComponents: [
        MaterialGoodsConvertUnitComponent,
        MaterialGoodsConvertUnitUpdateComponent,
        MaterialGoodsConvertUnitDeleteDialogComponent,
        MaterialGoodsConvertUnitDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMaterialGoodsConvertUnitModule {}
