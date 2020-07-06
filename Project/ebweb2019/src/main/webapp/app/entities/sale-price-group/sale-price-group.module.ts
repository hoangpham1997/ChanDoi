import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    SalePriceGroupComponent,
    SalePriceGroupDetailComponent,
    SalePriceGroupUpdateComponent,
    SalePriceGroupDeletePopupComponent,
    SalePriceGroupDeleteDialogComponent,
    salePriceGroupRoute,
    salePriceGroupPopupRoute
} from './';

const ENTITY_STATES = [...salePriceGroupRoute, ...salePriceGroupPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SalePriceGroupComponent,
        SalePriceGroupDetailComponent,
        SalePriceGroupUpdateComponent,
        SalePriceGroupDeleteDialogComponent,
        SalePriceGroupDeletePopupComponent
    ],
    entryComponents: [
        SalePriceGroupComponent,
        SalePriceGroupUpdateComponent,
        SalePriceGroupDeleteDialogComponent,
        SalePriceGroupDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebSalePriceGroupModule {}
