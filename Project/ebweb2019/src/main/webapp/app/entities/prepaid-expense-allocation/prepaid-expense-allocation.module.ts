import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    PrepaidExpenseAllocationComponent,
    PrepaidExpenseAllocationDetailComponent,
    PrepaidExpenseAllocationUpdateComponent,
    PrepaidExpenseAllocationDeletePopupComponent,
    PrepaidExpenseAllocationDeleteDialogComponent,
    prepaidExpenseAllocationRoute,
    prepaidExpenseAllocationPopupRoute
} from './';

const ENTITY_STATES = [...prepaidExpenseAllocationRoute, ...prepaidExpenseAllocationPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PrepaidExpenseAllocationComponent,
        PrepaidExpenseAllocationDetailComponent,
        PrepaidExpenseAllocationUpdateComponent,
        PrepaidExpenseAllocationDeleteDialogComponent,
        PrepaidExpenseAllocationDeletePopupComponent
    ],
    entryComponents: [
        PrepaidExpenseAllocationComponent,
        PrepaidExpenseAllocationUpdateComponent,
        PrepaidExpenseAllocationDeleteDialogComponent,
        PrepaidExpenseAllocationDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPrepaidExpenseAllocationModule {}
