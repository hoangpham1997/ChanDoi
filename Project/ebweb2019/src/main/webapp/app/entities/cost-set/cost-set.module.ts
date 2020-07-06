import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    CostSetComponent,
    CostSetDetailComponent,
    CostSetUpdateComponent,
    CostSetDeletePopupComponent,
    CostSetDeleteDialogComponent,
    costSetRoute,
    costSetPopupRoute
} from './';

const ENTITY_STATES = [...costSetRoute, ...costSetPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CostSetComponent,
        CostSetDetailComponent,
        CostSetUpdateComponent,
        CostSetDeleteDialogComponent,
        CostSetDeletePopupComponent
    ],
    entryComponents: [CostSetComponent, CostSetUpdateComponent, CostSetDeleteDialogComponent, CostSetDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebCostSetModule {}
