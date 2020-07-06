import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    MaterialGoodsAssemblyComponent,
    MaterialGoodsAssemblyDetailComponent,
    MaterialGoodsAssemblyUpdateComponent,
    MaterialGoodsAssemblyDeletePopupComponent,
    MaterialGoodsAssemblyDeleteDialogComponent,
    materialGoodsAssemblyRoute,
    materialGoodsAssemblyPopupRoute
} from './';

const ENTITY_STATES = [...materialGoodsAssemblyRoute, ...materialGoodsAssemblyPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MaterialGoodsAssemblyComponent,
        MaterialGoodsAssemblyDetailComponent,
        MaterialGoodsAssemblyUpdateComponent,
        MaterialGoodsAssemblyDeleteDialogComponent,
        MaterialGoodsAssemblyDeletePopupComponent
    ],
    entryComponents: [
        MaterialGoodsAssemblyComponent,
        MaterialGoodsAssemblyUpdateComponent,
        MaterialGoodsAssemblyDeleteDialogComponent,
        MaterialGoodsAssemblyDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebMaterialGoodsAssemblyModule {}
