import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MaterialGoodsSpecificationsComponent,
    MaterialGoodsSpecificationsDetailComponent,
    MaterialGoodsSpecificationsUpdateComponent,
    MaterialGoodsSpecificationsDeletePopupComponent,
    MaterialGoodsSpecificationsDeleteDialogComponent,
    materialGoodsSpecificationsRoute,
    materialGoodsSpecificationsPopupRoute
} from './';

const ENTITY_STATES = [...materialGoodsSpecificationsRoute, ...materialGoodsSpecificationsPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MaterialGoodsSpecificationsComponent,
        MaterialGoodsSpecificationsDetailComponent,
        MaterialGoodsSpecificationsUpdateComponent,
        MaterialGoodsSpecificationsDeleteDialogComponent,
        MaterialGoodsSpecificationsDeletePopupComponent
    ],
    entryComponents: [
        MaterialGoodsSpecificationsComponent,
        MaterialGoodsSpecificationsUpdateComponent,
        MaterialGoodsSpecificationsDeleteDialogComponent,
        MaterialGoodsSpecificationsDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMaterialGoodsSpecificationsModule {}
