import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    TransportMethodComponent,
    TransportMethodDetailComponent,
    TransportMethodUpdateComponent,
    TransportMethodDeletePopupComponent,
    TransportMethodDeleteDialogComponent,
    transportMethodRoute,
    transportMethodPopupRoute
} from './';

const ENTITY_STATES = [...transportMethodRoute, ...transportMethodPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TransportMethodComponent,
        TransportMethodDetailComponent,
        TransportMethodUpdateComponent,
        TransportMethodDeleteDialogComponent,
        TransportMethodDeletePopupComponent
    ],
    entryComponents: [
        TransportMethodComponent,
        TransportMethodUpdateComponent,
        TransportMethodDeleteDialogComponent,
        TransportMethodDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebTransportMethodModule {}
