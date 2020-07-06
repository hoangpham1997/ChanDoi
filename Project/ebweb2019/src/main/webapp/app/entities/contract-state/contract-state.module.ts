import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    ContractStateComponent,
    ContractStateDetailComponent,
    ContractStateUpdateComponent,
    ContractStateDeletePopupComponent,
    ContractStateDeleteDialogComponent,
    contractStateRoute,
    contractStatePopupRoute
} from './';

const ENTITY_STATES = [...contractStateRoute, ...contractStatePopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ContractStateComponent,
        ContractStateDetailComponent,
        ContractStateUpdateComponent,
        ContractStateDeleteDialogComponent,
        ContractStateDeletePopupComponent
    ],
    entryComponents: [
        ContractStateComponent,
        ContractStateUpdateComponent,
        ContractStateDeleteDialogComponent,
        ContractStateDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebContractStateModule {}
