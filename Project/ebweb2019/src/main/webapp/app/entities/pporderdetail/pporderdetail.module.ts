import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    PporderdetailComponent,
    PporderdetailDeleteDialogComponent,
    PporderdetailDeletePopupComponent,
    PporderdetailDetailComponent,
    pporderdetailPopupRoute,
    pporderdetailRoute,
    PporderdetailUpdateComponent
} from './';

const ENTITY_STATES = [...pporderdetailRoute, ...pporderdetailPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PporderdetailComponent,
        PporderdetailDetailComponent,
        PporderdetailUpdateComponent,
        PporderdetailDeleteDialogComponent,
        PporderdetailDeletePopupComponent
    ],
    entryComponents: [
        PporderdetailComponent,
        PporderdetailUpdateComponent,
        PporderdetailDeleteDialogComponent,
        PporderdetailDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebPporderdetailModule {}
