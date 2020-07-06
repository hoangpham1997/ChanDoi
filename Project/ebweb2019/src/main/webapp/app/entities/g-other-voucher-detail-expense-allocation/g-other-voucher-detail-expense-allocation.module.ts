import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    GOtherVoucherDetailExpenseAllocationComponent,
    GOtherVoucherDetailExpenseAllocationDetailComponent,
    GOtherVoucherDetailExpenseAllocationUpdateComponent,
    GOtherVoucherDetailExpenseAllocationDeletePopupComponent,
    GOtherVoucherDetailExpenseAllocationDeleteDialogComponent,
    gOtherVoucherDetailExpenseAllocationRoute,
    gOtherVoucherDetailExpenseAllocationPopupRoute
} from './';

const ENTITY_STATES = [...gOtherVoucherDetailExpenseAllocationRoute, ...gOtherVoucherDetailExpenseAllocationPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        GOtherVoucherDetailExpenseAllocationComponent,
        GOtherVoucherDetailExpenseAllocationDetailComponent,
        GOtherVoucherDetailExpenseAllocationUpdateComponent,
        GOtherVoucherDetailExpenseAllocationDeleteDialogComponent,
        GOtherVoucherDetailExpenseAllocationDeletePopupComponent
    ],
    entryComponents: [
        GOtherVoucherDetailExpenseAllocationComponent,
        GOtherVoucherDetailExpenseAllocationUpdateComponent,
        GOtherVoucherDetailExpenseAllocationDeleteDialogComponent,
        GOtherVoucherDetailExpenseAllocationDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebGOtherVoucherDetailExpenseAllocationModule {}
