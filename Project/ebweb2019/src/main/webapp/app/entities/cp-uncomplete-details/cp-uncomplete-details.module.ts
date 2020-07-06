import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    CPUncompleteDetailsComponent,
    CPUncompleteDetailsDetailComponent,
    CPUncompleteDetailsUpdateComponent,
    CPUncompleteDetailsDeletePopupComponent,
    CPUncompleteDetailsDeleteDialogComponent,
    cPUncompleteDetailsRoute,
    cPUncompleteDetailsPopupRoute
} from './';

const ENTITY_STATES = [...cPUncompleteDetailsRoute, ...cPUncompleteDetailsPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CPUncompleteDetailsComponent,
        CPUncompleteDetailsDetailComponent,
        CPUncompleteDetailsUpdateComponent,
        CPUncompleteDetailsDeleteDialogComponent,
        CPUncompleteDetailsDeletePopupComponent
    ],
    entryComponents: [
        CPUncompleteDetailsComponent,
        CPUncompleteDetailsUpdateComponent,
        CPUncompleteDetailsDeleteDialogComponent,
        CPUncompleteDetailsDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebCPUncompleteDetailsModule {}
