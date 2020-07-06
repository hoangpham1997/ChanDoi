import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EbwebSharedModule } from 'app/shared';
import {
    GenCodeComponent,
    GenCodeDetailComponent,
    GenCodeUpdateComponent,
    GenCodeDeletePopupComponent,
    GenCodeDeleteDialogComponent,
    genCodeRoute,
    genCodePopupRoute
} from './';

const ENTITY_STATES = [...genCodeRoute, ...genCodePopupRoute];

@NgModule({
    imports: [EbwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        GenCodeComponent,
        GenCodeDetailComponent,
        GenCodeUpdateComponent,
        GenCodeDeleteDialogComponent,
        GenCodeDeletePopupComponent
    ],
    entryComponents: [GenCodeComponent, GenCodeUpdateComponent, GenCodeDeleteDialogComponent, GenCodeDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EbwebGenCodeModule {}
