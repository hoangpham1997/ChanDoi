import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    PrepaidExpenseVoucherComponent,
    PrepaidExpenseVoucherDetailComponent,
    PrepaidExpenseVoucherUpdateComponent,
    PrepaidExpenseVoucherDeletePopupComponent,
    PrepaidExpenseVoucherDeleteDialogComponent,
    prepaidExpenseVoucherRoute,
    prepaidExpenseVoucherPopupRoute
} from './';

const ENTITY_STATES = [...prepaidExpenseVoucherRoute, ...prepaidExpenseVoucherPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PrepaidExpenseVoucherComponent,
        PrepaidExpenseVoucherDetailComponent,
        PrepaidExpenseVoucherUpdateComponent,
        PrepaidExpenseVoucherDeleteDialogComponent,
        PrepaidExpenseVoucherDeletePopupComponent
    ],
    entryComponents: [
        PrepaidExpenseVoucherComponent,
        PrepaidExpenseVoucherUpdateComponent,
        PrepaidExpenseVoucherDeleteDialogComponent,
        PrepaidExpenseVoucherDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPrepaidExpenseVoucherModule {}
