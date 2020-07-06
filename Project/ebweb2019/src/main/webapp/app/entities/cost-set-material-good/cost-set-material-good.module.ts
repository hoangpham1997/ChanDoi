import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    CostSetMaterialGoodComponent,
    CostSetMaterialGoodDetailComponent,
    CostSetMaterialGoodUpdateComponent,
    CostSetMaterialGoodDeletePopupComponent,
    CostSetMaterialGoodDeleteDialogComponent,
    costSetMaterialGoodRoute,
    costSetMaterialGoodPopupRoute
} from './';

const ENTITY_STATES = [...costSetMaterialGoodRoute, ...costSetMaterialGoodPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CostSetMaterialGoodComponent,
        CostSetMaterialGoodDetailComponent,
        CostSetMaterialGoodUpdateComponent,
        CostSetMaterialGoodDeleteDialogComponent,
        CostSetMaterialGoodDeletePopupComponent
    ],
    entryComponents: [
        CostSetMaterialGoodComponent,
        CostSetMaterialGoodUpdateComponent,
        CostSetMaterialGoodDeleteDialogComponent,
        CostSetMaterialGoodDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebCostSetMaterialGoodModule {}
