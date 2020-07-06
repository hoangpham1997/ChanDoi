import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    ExpenseItemComponent,
    ExpenseItemDetailComponent,
    ExpenseItemUpdateComponent,
    ExpenseItemDeletePopupComponent,
    ExpenseItemDeleteDialogComponent,
    expenseItemRoute,
    expenseItemPopupRoute
} from './';

const ENTITY_STATES = [...expenseItemRoute, ...expenseItemPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ExpenseItemComponent,
        ExpenseItemDetailComponent,
        ExpenseItemUpdateComponent,
        ExpenseItemDeleteDialogComponent,
        ExpenseItemDeletePopupComponent
    ],
    entryComponents: [ExpenseItemComponent, ExpenseItemUpdateComponent, ExpenseItemDeleteDialogComponent, ExpenseItemDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebExpenseItemModule {}
