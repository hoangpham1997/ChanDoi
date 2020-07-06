import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    EMContractComponent,
    EMContractDetailComponent,
    EMContractUpdateComponent,
    EMContractDeletePopupComponent,
    EMContractDeleteDialogComponent,
    eMContractRoute,
    eMContractPopupRoute
} from './';

const ENTITY_STATES = [...eMContractRoute, ...eMContractPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        EMContractComponent,
        EMContractDetailComponent,
        EMContractUpdateComponent,
        EMContractDeleteDialogComponent,
        EMContractDeletePopupComponent
    ],
    entryComponents: [EMContractComponent, EMContractUpdateComponent, EMContractDeleteDialogComponent, EMContractDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebEMContractModule {}
