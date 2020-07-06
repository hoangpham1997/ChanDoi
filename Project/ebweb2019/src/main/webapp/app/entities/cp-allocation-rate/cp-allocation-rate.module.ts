import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    CPAllocationRateComponent,
    CPAllocationRateDetailComponent,
    CPAllocationRateUpdateComponent,
    CPAllocationRateDeletePopupComponent,
    CPAllocationRateDeleteDialogComponent,
    cPAllocationRateRoute,
    cPAllocationRatePopupRoute
} from './';

const ENTITY_STATES = [...cPAllocationRateRoute, ...cPAllocationRatePopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CPAllocationRateComponent,
        CPAllocationRateDetailComponent,
        CPAllocationRateUpdateComponent,
        CPAllocationRateDeleteDialogComponent,
        CPAllocationRateDeletePopupComponent
    ],
    entryComponents: [
        CPAllocationRateComponent,
        CPAllocationRateUpdateComponent,
        CPAllocationRateDeleteDialogComponent,
        CPAllocationRateDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebCPAllocationRateModule {}
