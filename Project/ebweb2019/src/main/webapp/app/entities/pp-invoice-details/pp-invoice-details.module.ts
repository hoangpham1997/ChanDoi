import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    PPInvoiceDetailsComponent,
    PPInvoiceDetailsDetailComponent,
    PPInvoiceDetailsUpdateComponent,
    PPInvoiceDetailsDeletePopupComponent,
    PPInvoiceDetailsDeleteDialogComponent,
    pPInvoiceDetailsRoute,
    pPInvoiceDetailsPopupRoute
} from './';

const ENTITY_STATES = [...pPInvoiceDetailsRoute, ...pPInvoiceDetailsPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PPInvoiceDetailsComponent,
        PPInvoiceDetailsDetailComponent,
        PPInvoiceDetailsUpdateComponent,
        PPInvoiceDetailsDeleteDialogComponent,
        PPInvoiceDetailsDeletePopupComponent
    ],
    entryComponents: [
        PPInvoiceDetailsComponent,
        PPInvoiceDetailsUpdateComponent,
        PPInvoiceDetailsDeleteDialogComponent,
        PPInvoiceDetailsDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPPInvoiceDetailsModule {}
