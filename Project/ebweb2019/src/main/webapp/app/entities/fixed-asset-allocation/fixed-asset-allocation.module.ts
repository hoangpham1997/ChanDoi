import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    FixedAssetAllocationComponent,
    FixedAssetAllocationDetailComponent,
    FixedAssetAllocationUpdateComponent,
    FixedAssetAllocationDeletePopupComponent,
    FixedAssetAllocationDeleteDialogComponent,
    fixedAssetAllocationRoute,
    fixedAssetAllocationPopupRoute
} from './';

const ENTITY_STATES = [...fixedAssetAllocationRoute, ...fixedAssetAllocationPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FixedAssetAllocationComponent,
        FixedAssetAllocationDetailComponent,
        FixedAssetAllocationUpdateComponent,
        FixedAssetAllocationDeleteDialogComponent,
        FixedAssetAllocationDeletePopupComponent
    ],
    entryComponents: [
        FixedAssetAllocationComponent,
        FixedAssetAllocationUpdateComponent,
        FixedAssetAllocationDeleteDialogComponent,
        FixedAssetAllocationDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebFixedAssetAllocationModule {}
