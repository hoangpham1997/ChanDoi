import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    FixedAssetComponent,
    FixedAssetDetailComponent,
    FixedAssetUpdateComponent,
    FixedAssetDeletePopupComponent,
    FixedAssetDeleteDialogComponent,
    fixedAssetRoute,
    fixedAssetPopupRoute
} from './';

const ENTITY_STATES = [...fixedAssetRoute, ...fixedAssetPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FixedAssetComponent,
        FixedAssetDetailComponent,
        FixedAssetUpdateComponent,
        FixedAssetDeleteDialogComponent,
        FixedAssetDeletePopupComponent
    ],
    entryComponents: [FixedAssetComponent, FixedAssetUpdateComponent, FixedAssetDeleteDialogComponent, FixedAssetDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebFixedAssetModule {}
