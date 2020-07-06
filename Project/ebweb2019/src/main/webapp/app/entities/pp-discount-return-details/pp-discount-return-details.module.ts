import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    PPDiscountReturnDetailsComponent,
    PPDiscountReturnDetailsDetailComponent,
    PPDiscountReturnDetailsUpdateComponent,
    PPDiscountReturnDetailsDeletePopupComponent,
    PPDiscountReturnDetailsDeleteDialogComponent,
    pPDiscountReturnDetailsRoute,
    pPDiscountReturnDetailsPopupRoute
} from './';

const ENTITY_STATES = [...pPDiscountReturnDetailsRoute, ...pPDiscountReturnDetailsPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PPDiscountReturnDetailsComponent,
        PPDiscountReturnDetailsDetailComponent,
        PPDiscountReturnDetailsUpdateComponent,
        PPDiscountReturnDetailsDeleteDialogComponent,
        PPDiscountReturnDetailsDeletePopupComponent
    ],
    entryComponents: [
        PPDiscountReturnDetailsComponent,
        PPDiscountReturnDetailsUpdateComponent,
        PPDiscountReturnDetailsDeleteDialogComponent,
        PPDiscountReturnDetailsDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPPDiscountReturnDetailsModule {}
