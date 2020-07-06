import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MaterialGoodsPurchasePriceComponent,
    MaterialGoodsPurchasePriceDetailComponent,
    MaterialGoodsPurchasePriceUpdateComponent,
    MaterialGoodsPurchasePriceDeletePopupComponent,
    MaterialGoodsPurchasePriceDeleteDialogComponent,
    materialGoodsPurchasePriceRoute,
    materialGoodsPurchasePricePopupRoute
} from './';

const ENTITY_STATES = [...materialGoodsPurchasePriceRoute, ...materialGoodsPurchasePricePopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MaterialGoodsPurchasePriceComponent,
        MaterialGoodsPurchasePriceDetailComponent,
        MaterialGoodsPurchasePriceUpdateComponent,
        MaterialGoodsPurchasePriceDeleteDialogComponent,
        MaterialGoodsPurchasePriceDeletePopupComponent
    ],
    entryComponents: [
        MaterialGoodsPurchasePriceComponent,
        MaterialGoodsPurchasePriceUpdateComponent,
        MaterialGoodsPurchasePriceDeleteDialogComponent,
        MaterialGoodsPurchasePriceDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMaterialGoodsPurchasePriceModule {}
