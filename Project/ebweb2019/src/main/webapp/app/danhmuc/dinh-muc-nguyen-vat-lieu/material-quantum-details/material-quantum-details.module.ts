import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MaterialQuantumDetailsComponent,
    MaterialQuantumDetailsDetailComponent,
    MaterialQuantumDetailsUpdateComponent,
    MaterialQuantumDetailsDeletePopupComponent,
    MaterialQuantumDetailsDeleteDialogComponent,
    materialQuantumDetailsRoute,
    materialQuantumDetailsPopupRoute
} from './index';

const ENTITY_STATES = [...materialQuantumDetailsRoute, ...materialQuantumDetailsPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MaterialQuantumDetailsComponent,
        MaterialQuantumDetailsDetailComponent,
        MaterialQuantumDetailsUpdateComponent,
        MaterialQuantumDetailsDeleteDialogComponent,
        MaterialQuantumDetailsDeletePopupComponent
    ],
    entryComponents: [
        MaterialQuantumDetailsComponent,
        MaterialQuantumDetailsUpdateComponent,
        MaterialQuantumDetailsDeleteDialogComponent,
        MaterialQuantumDetailsDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMaterialQuantumDetailsModule {}
