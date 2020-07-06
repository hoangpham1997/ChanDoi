import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    GOtherVoucherDetailExpenseComponent,
    GOtherVoucherDetailExpenseDetailComponent,
    GOtherVoucherDetailExpenseUpdateComponent,
    GOtherVoucherDetailExpenseDeletePopupComponent,
    GOtherVoucherDetailExpenseDeleteDialogComponent,
    gOtherVoucherDetailExpenseRoute,
    gOtherVoucherDetailExpensePopupRoute
} from './';

const ENTITY_STATES = [...gOtherVoucherDetailExpenseRoute, ...gOtherVoucherDetailExpensePopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        GOtherVoucherDetailExpenseComponent,
        GOtherVoucherDetailExpenseDetailComponent,
        GOtherVoucherDetailExpenseUpdateComponent,
        GOtherVoucherDetailExpenseDeleteDialogComponent,
        GOtherVoucherDetailExpenseDeletePopupComponent
    ],
    entryComponents: [
        GOtherVoucherDetailExpenseComponent,
        GOtherVoucherDetailExpenseUpdateComponent,
        GOtherVoucherDetailExpenseDeleteDialogComponent,
        GOtherVoucherDetailExpenseDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebGOtherVoucherDetailExpenseModule {}
