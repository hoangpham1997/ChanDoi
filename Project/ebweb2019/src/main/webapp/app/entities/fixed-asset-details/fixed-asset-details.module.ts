import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    FixedAssetDetailsComponent,
    FixedAssetDetailsDetailComponent,
    FixedAssetDetailsUpdateComponent,
    FixedAssetDetailsDeletePopupComponent,
    FixedAssetDetailsDeleteDialogComponent,
    fixedAssetDetailsRoute,
    fixedAssetDetailsPopupRoute
} from './';

const ENTITY_STATES = [...fixedAssetDetailsRoute, ...fixedAssetDetailsPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FixedAssetDetailsComponent,
        FixedAssetDetailsDetailComponent,
        FixedAssetDetailsUpdateComponent,
        FixedAssetDetailsDeleteDialogComponent,
        FixedAssetDetailsDeletePopupComponent
    ],
    entryComponents: [
        FixedAssetDetailsComponent,
        FixedAssetDetailsUpdateComponent,
        FixedAssetDetailsDeleteDialogComponent,
        FixedAssetDetailsDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebFixedAssetDetailsModule {}
