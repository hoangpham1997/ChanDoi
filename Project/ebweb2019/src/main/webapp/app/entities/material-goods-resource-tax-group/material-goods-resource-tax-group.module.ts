import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MaterialGoodsResourceTaxGroupComponent,
    MaterialGoodsResourceTaxGroupDetailComponent,
    MaterialGoodsResourceTaxGroupUpdateComponent,
    MaterialGoodsResourceTaxGroupDeletePopupComponent,
    MaterialGoodsResourceTaxGroupDeleteDialogComponent,
    materialGoodsResourceTaxGroupRoute,
    materialGoodsResourceTaxGroupPopupRoute
} from './';

const ENTITY_STATES = [...materialGoodsResourceTaxGroupRoute, ...materialGoodsResourceTaxGroupPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MaterialGoodsResourceTaxGroupComponent,
        MaterialGoodsResourceTaxGroupDetailComponent,
        MaterialGoodsResourceTaxGroupUpdateComponent,
        MaterialGoodsResourceTaxGroupDeleteDialogComponent,
        MaterialGoodsResourceTaxGroupDeletePopupComponent
    ],
    entryComponents: [
        MaterialGoodsResourceTaxGroupComponent,
        MaterialGoodsResourceTaxGroupUpdateComponent,
        MaterialGoodsResourceTaxGroupDeleteDialogComponent,
        MaterialGoodsResourceTaxGroupDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMaterialGoodsResourceTaxGroupModule {}
