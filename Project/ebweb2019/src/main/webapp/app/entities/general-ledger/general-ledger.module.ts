import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    GeneralLedgerComponent,
    GeneralLedgerDetailComponent,
    GeneralLedgerUpdateComponent,
    GeneralLedgerDeletePopupComponent,
    GeneralLedgerDeleteDialogComponent,
    generalLedgerRoute,
    generalLedgerPopupRoute
} from './';

const ENTITY_STATES = [...generalLedgerRoute, ...generalLedgerPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        GeneralLedgerComponent,
        GeneralLedgerDetailComponent,
        GeneralLedgerUpdateComponent,
        GeneralLedgerDeleteDialogComponent,
        GeneralLedgerDeletePopupComponent
    ],
    entryComponents: [
        GeneralLedgerComponent,
        GeneralLedgerUpdateComponent,
        GeneralLedgerDeleteDialogComponent,
        GeneralLedgerDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebGeneralLedgerModule {}
