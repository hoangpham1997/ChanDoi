import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    CPResultComponent,
    CPResultDetailComponent,
    CPResultUpdateComponent,
    CPResultDeletePopupComponent,
    CPResultDeleteDialogComponent,
    cPResultRoute,
    cPResultPopupRoute
} from './';

const ENTITY_STATES = [...cPResultRoute, ...cPResultPopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CPResultComponent,
        CPResultDetailComponent,
        CPResultUpdateComponent,
        CPResultDeleteDialogComponent,
        CPResultDeletePopupComponent
    ],
    entryComponents: [CPResultComponent, CPResultUpdateComponent, CPResultDeleteDialogComponent, CPResultDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebCPResultModule {}
