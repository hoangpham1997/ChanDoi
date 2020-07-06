import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    PrepaidExpenseComponent,
    PrepaidExpenseDetailComponent,
    PrepaidExpenseUpdateComponent,
    PrepaidExpenseDeletePopupComponent,
    PrepaidExpenseDeleteDialogComponent,
    prepaidExpenseRoute,
    prepaidExpensePopupRoute
} from './';

const ENTITY_STATES = [...prepaidExpenseRoute, ...prepaidExpensePopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PrepaidExpenseComponent,
        PrepaidExpenseDetailComponent,
        PrepaidExpenseUpdateComponent,
        PrepaidExpenseDeleteDialogComponent,
        PrepaidExpenseDeletePopupComponent
    ],
    entryComponents: [
        PrepaidExpenseComponent,
        PrepaidExpenseUpdateComponent,
        PrepaidExpenseDeleteDialogComponent,
        PrepaidExpenseDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPrepaidExpenseModule {}
