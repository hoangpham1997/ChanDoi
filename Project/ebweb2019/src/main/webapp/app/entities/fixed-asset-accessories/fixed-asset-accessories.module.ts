import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    FixedAssetAccessoriesComponent,
    FixedAssetAccessoriesDetailComponent,
    FixedAssetAccessoriesUpdateComponent,
    FixedAssetAccessoriesDeletePopupComponent,
    FixedAssetAccessoriesDeleteDialogComponent,
    fixedAssetAccessoriesRoute,
    fixedAssetAccessoriesPopupRoute
} from './';

const ENTITY_STATES = [...fixedAssetAccessoriesRoute, ...fixedAssetAccessoriesPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FixedAssetAccessoriesComponent,
        FixedAssetAccessoriesDetailComponent,
        FixedAssetAccessoriesUpdateComponent,
        FixedAssetAccessoriesDeleteDialogComponent,
        FixedAssetAccessoriesDeletePopupComponent
    ],
    entryComponents: [
        FixedAssetAccessoriesComponent,
        FixedAssetAccessoriesUpdateComponent,
        FixedAssetAccessoriesDeleteDialogComponent,
        FixedAssetAccessoriesDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebFixedAssetAccessoriesModule {}
